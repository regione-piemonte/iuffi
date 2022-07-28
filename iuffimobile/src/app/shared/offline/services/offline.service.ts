import { Injectable } from '@angular/core';
import { Photo } from '@app/detections/models/photo.model';
import { Inspector } from '@app/missions/models/inspector.model';
import { Mission } from '@app/missions/models/mission.model';
import { ApiService } from '@core/api';
import { LokiDB, LokiDBOptions } from '@core/db';
import { ENV } from '@env';
import { Storage } from '@ionic/storage';
import * as LokiJS from 'lokijs';
import * as Moment from 'moment';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { AreaTypeLocal } from '../models/area-type.model';
import { PlantSpecies } from '../models/plant-species.model';
import { LokiDbConfig } from './../../../core/db/models/lokidb-config.model';
import { DeviceService } from './../../../core/device/services/device.service';
import { LoggerService } from './../../../core/logger/services/logger.service';
import {
    AreaTypeDto,
    HarmfulOrganismDto,
    MonitoringSummaryDto,
    PlantSpeciesDto,
    SampleTypeDto,
    TrapTypeByHarmfulOrganismDto,
    TrapTypeDto,
} from './../dto/offline.dto';
import { HarmfulOrganism } from './../models/harmful-organism.model';
import { MonitoringSummary } from './../models/monitoring-summary.model';
import { SampleType } from './../models/sample-type.model';
import { TrapTypeByHarmfulOrganism } from './../models/trap-type-by-harmful-organism.model';
import { TrapType } from './../models/trap-type.model';

const storageKeys = {
    lastDbUpdate: 'lastDbUpdate'
};

@Injectable()
export class OfflineService {
    private _db: LokiDB | null = null;
    private _userDB: LokiDB | null = null;
    private _storage: Storage;
    private _lastDbUpdate: Moment.Moment | undefined;
    private areaTypeCollection!: Collection<AreaTypeLocal>;
    private plantSpeciesCollection!: Collection<PlantSpecies>;
    private harmfulOrganismCollection!: Collection<HarmfulOrganism>;
    private sampleTypeCollection!: Collection<SampleType>;
    private trapTypeCollection!: Collection<TrapType>;
    private trapTypeByHOCollection!: Collection<TrapTypeByHarmfulOrganism>;
    private monitoringSummaryCollection!: Collection<MonitoringSummary>;
    constructor(
        private logger: LoggerService,
        private apiService: ApiService,
        private deviceService: DeviceService
    ) {
        this._storage = new Storage({
            name: ENV.storePrefix || 'storage',
            storeName: 'auth',
            driverOrder: ['localstorage'],
        });

    }

    public init(dbConfig: LokiDbConfig): Promise<LokiJS> {
        this.logger.debug('INIT DB');
        const self = this;

        // Prendo la data dell'ultimo aggiornamento fatto dal localStorage
        this._storage.get(storageKeys.lastDbUpdate).then(
            (lastDbUpdate: string) => {
                if (lastDbUpdate) {
                    self._lastDbUpdate = Moment(lastDbUpdate, 'YYYY-MM-DD HH:mm:ss');
                }
            }
        )

        if (!dbConfig.dbName) {
            dbConfig.dbName = ENV.storePrefix || 'db';
        }

        const dbOptions: LokiDBOptions = LokiDBOptions.fromConfig(dbConfig);

        this._db = new LokiDB(
            dbConfig.dbName,
            dbOptions,
            {
                areaTypes: AreaTypeLocal,
                plantSpecies: PlantSpecies,
                harmfulOrganisms: HarmfulOrganism,
                sampleTypes: SampleType,
                trapTypes: TrapType,
                trapTypesByHO: TrapTypeByHarmfulOrganism,
                monitoringSummary: MonitoringSummary,
                inspectors: Inspector,
            }
        );
        return this._db.load();
    }

    public initUserDb(dbConfig: LokiDbConfig, key: string): Promise<LokiJS> {
        this.logger.debug('INIT USER DB');

        dbConfig.dbName = 'Iuffi_' + key + '.db';

        const dbOptions: LokiDBOptions = LokiDBOptions.fromConfig(dbConfig);

        this._userDB = new LokiDB(
            dbConfig.dbName,
            dbOptions,
            {
                missions: Mission,
                photos: Photo
            }
        );
        return this._userDB.load();
    }

    public getDB(): LokiDB | null {
        return this._db;
    }

    public getUserDB(): LokiDB | null {
        return this._userDB;
    }

    public initializeDb(): void {
        const self = this;
        this._storage.get(storageKeys.lastDbUpdate).then(
            (lastDbUpdate: string) => {
                if (lastDbUpdate) {
                    self._lastDbUpdate = Moment(lastDbUpdate, 'YYYY-MM-DD HH:mm:ss');
                }
            }
        )
        if (this._db) {
            this.areaTypeCollection = this._db.initCollection<AreaTypeLocal>('areaTypes');
            this.plantSpeciesCollection = this._db.initCollection<PlantSpecies>('plantSpecies');
            this.harmfulOrganismCollection = this._db.initCollection<HarmfulOrganism>('harmfulOrganisms');
            this.sampleTypeCollection = this._db.initCollection<SampleType>('sampleTypes');
            this.trapTypeCollection = this._db.initCollection<TrapType>('trapTypes');
            this.trapTypeByHOCollection = this._db.initCollection<TrapTypeByHarmfulOrganism>('trapTypesByHO');
            this.monitoringSummaryCollection = this._db.initCollection<MonitoringSummary>('monitoringSummary');
        }

        if (this.deviceService.isOnline()) {
            this._checkUpdateStatus().subscribe(
                (status: boolean) => {
                    if (status) {
                        this._resetCollections();
                        this._getInitialOfflineData().subscribe(
                            responses => {
                                this._saveInitialOfflineData(responses);
                            }
                        );
                    }
                },
                (err: any) => {
                    this.logger.debug(err);
                    if (!self._lastDbUpdate) {
                        this._resetCollections();
                        this._getInitialOfflineData().subscribe(
                            responses => {
                                this._saveInitialOfflineData(responses);
                            }
                        );
                    }

                }
            );
        }
        else {
            this.saveDB();
        }
    }

    private _resetCollections(): void {
        this.areaTypeCollection.clear();
        this.plantSpeciesCollection.clear();
        this.harmfulOrganismCollection.clear();
        this.sampleTypeCollection.clear();
        this.trapTypeCollection.clear();
        this.trapTypeByHOCollection.clear();
        this.monitoringSummaryCollection.clear();
        this.saveDB();
    }

    private _getInitialOfflineData(): Observable<
        {
            areaTypes: AreaTypeLocal[];
            harmfulOrganism: HarmfulOrganism[];
            plantSpecies: PlantSpecies[];
            sampleTypes: SampleType[];
            trapTypes: TrapType[];
            trapTypesByHO: TrapTypeByHarmfulOrganism[];
            monitoringSummary: MonitoringSummary[];
        }> {
        return forkJoin({
            areaTypes: this._getAreaTypes(),
            harmfulOrganism: this._getHarmfulOrganisms(),
            plantSpecies: this._getPlantSpecies(),
            sampleTypes: this._getSampleTypes(),
            trapTypes: this._getTrapTypes(),
            trapTypesByHO: this._getTrapTypesByHarmfulOrganism(),
            monitoringSummary: this._getMonitoringSummary()
        }).pipe(map(responses => {
            return responses;
        }));
    }

    private _saveInitialOfflineData(responses: {
        areaTypes: AreaTypeLocal[];
        harmfulOrganism: HarmfulOrganism[];
        plantSpecies: PlantSpecies[];
        sampleTypes: SampleType[];
        trapTypes: TrapType[];
        trapTypesByHO: TrapTypeByHarmfulOrganism[];
        monitoringSummary: MonitoringSummary[];
    }): void {
        this.areaTypeCollection.insert(responses.areaTypes);
        this.plantSpeciesCollection.insert(responses.plantSpecies);
        this.harmfulOrganismCollection.insert(responses.harmfulOrganism);
        this.sampleTypeCollection.insert(responses.sampleTypes);
        this.trapTypeCollection.insert(responses.trapTypes);
        this.trapTypeByHOCollection.insert(responses.trapTypesByHO);
        this.monitoringSummaryCollection.insert(responses.monitoringSummary);
        this.saveDB();
    }

    private _checkUpdateStatus(): Observable<boolean> {
        const self = this;
        return this.apiService.callApi<any>('getLastChangeTimestamp')
            .pipe(map(
                (response: any) => {
                    if (self._lastDbUpdate) {
                        const lastTimestamp = Moment(response.lastTimestamp, 'YYYY-MM-DD HH:mm:ss');
                        if (lastTimestamp.isAfter(self._lastDbUpdate)) {
                            this._storage.set(storageKeys.lastDbUpdate, response.lastTimestamp);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                        this._storage.set(storageKeys.lastDbUpdate, response.lastTimestamp);
                        return true;
                    }
                }
            ));
    }

    // private _getAreaTypes(): Observable<AreaType[]> {
    //     return this.apiService.callApi<AreaTypeDto[]>('getAreaTypes')
    //         .pipe(map(response => {
    //             return response.map(t => new AreaType(t))
    //         }));
    // }

    private _getAreaTypes(): Observable<AreaTypeLocal[]> {
        return this.apiService.callApi<AreaTypeDto[]>('getAreaTypes')
            .pipe(map(response => {
                const areaTypes: AreaTypeLocal[] = [];
                response.map(t => {
                    if (t.dettaglioTipoAree) {
                        t.dettaglioTipoAree.map(d => {
                            const areaType = new AreaTypeLocal(d);
                            areaType.codiceUfficiale = t.codiceUfficiale;
                            areaTypes.push(areaType);
                        })
                    }
                });
                return areaTypes;
            }));
    }

    private _getPlantSpecies(): Observable<PlantSpecies[]> {
        return this.apiService.callApi<PlantSpeciesDto[]>('getPlantSpecies')
            .pipe(map(response => {
                if (response) {
                    return response.map(t => new PlantSpecies(t));
                }
                return [];
            }));
    }

    private _getHarmfulOrganisms(): Observable<HarmfulOrganism[]> {
        return this.apiService.callApi<HarmfulOrganismDto[]>('getHarmfulOrganisms')
            .pipe(map(response => {
                if (response) {
                    return response.map(t => new HarmfulOrganism(t));
                }
                return [];
            }));
    }

    private _getSampleTypes(): Observable<SampleType[]> {
        return this.apiService.callApi<SampleTypeDto[]>('getSampleTypes')
            .pipe(map(response => {
                if (response) {
                    return response.map(t => new SampleType(t));
                }
                return [];
            }));
    }

    private _getTrapTypes(): Observable<TrapType[]> {
        return this.apiService.callApi<TrapTypeDto[]>('getTrapTypes')
            .pipe(map(response => {
                if (response) {
                    return response.map(t => new TrapType(t));
                }
                return [];
            }));
    }

    private _getTrapTypesByHarmfulOrganism(): Observable<TrapTypeByHarmfulOrganism[]> {
        return this.apiService.callApi<TrapTypeByHarmfulOrganismDto[]>('getTrapTypesByHO')
            .pipe(map(response => {
                if (response) {
                    return response.map(t => new TrapTypeByHarmfulOrganism(t));
                }
                return [];
            }));
    }

    private _getMonitoringSummary(): Observable<MonitoringSummary[]> {
        return this.apiService.callApi<MonitoringSummaryDto[]>('getMonitoringSummary')
            .pipe(map(response => {
                if (response) {
                    return response.map(t => new MonitoringSummary(t));
                }
                return [];
            }));
    }

    public saveDB(): void {
        const self = this;
        setTimeout(() => {
            (self._db as LokiDB).save();
        }, 1000);

    }

    public saveUserDB(): void {
        const self = this;
        setTimeout(() => {
            (self._userDB as LokiDB).save();
        }, 1000);

    }

}
