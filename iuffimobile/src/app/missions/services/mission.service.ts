import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Detection } from '@app/detections/models/detection.model';
import { Sample } from '@app/detections/models/sample.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { SampleService } from '@app/detections/services/sample.service';
import { TrapService } from '@app/detections/services/trap.service';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
import { ResponseTypes } from '@core/api';
import { LoggerService } from '@core/logger/services/logger.service';
import { OfflineService } from '@shared/offline/services/offline.service';
import * as Moment from 'moment';
import { map } from 'rxjs/operators';
import { ApiService } from '../../core/api/services/api.service';
import { LokiDB } from '../../core/db/models/lokidb.model';
import { DeviceService } from '../../core/device/services/device.service';
import { GetMissionListRequest } from '../api/GetMissionListRequest';
import { SaveMissionRequest } from '../api/SaveMissionRequest';
import { MissionDto } from '../dto/mission.dto';
import { Inspector } from '../models/inspector.model';
import { Mission, MissionStatus } from '../models/mission.model';

// import {Resultset} from 'lokijs'
@Injectable()
export class MissionService {
    private _userDB: LokiDB | null = null;
    private missionCollection!: Collection<Mission>;
    private inspectorCollection!: Collection<Inspector>;
    constructor(
        private formBuilder: FormBuilder,
        private apiService: ApiService,
        private offlineService: OfflineService,
        private deviceService: DeviceService,
        private detectionService: DetectionService,
        private visualInspectionService: VisualInspectionService,
        private logger: LoggerService,
        private sampleService: SampleService,
        private trapService: TrapService
    ) {
        this._init();
    }

    private _init(): void {
        this._userDB = this.offlineService.getUserDB();
        if (this._userDB) {
            this.inspectorCollection = this._userDB.initCollection<Inspector>('inspectors');
            this.missionCollection = this._userDB.initCollection<Mission>('missions');
        }

    }

    /**
     * Initialize mission creation form
     * @return FormGroup
     */
    public createMissionForm(mission: Mission): FormGroup {
        const regNumbers = /^\d+$/;
        const missionForm = this.formBuilder.group({
            idMissione: [mission.idMissione],
            cfIspettore: [mission.cfIspettore],
            numeroTrasferta: [mission.numeroTrasferta, Validators.pattern(regNumbers)],
            dataMissione: [
                Moment(new Date(mission.dataMissione)).toISOString(),
                Validators.compose([
                    Validators.required
                ])
            ],
            oraInizio: [
                mission.oraInizio,
                Validators.compose([
                    Validators.required,
                ])
            ],
            oraFine: [mission.oraFine],
            nomeIspettore: [
                mission.nomeIspettore
            ],
            cognomeIspettore: [
                mission.cognomeIspettore
            ],
            ispettoriAggiunti: [mission.ispettoriAggiunti]
        });

        return missionForm;
    }

    public searchMissionByStatus(status: MissionStatus): Mission[] {
        return this.missionCollection.find({ stato: status });
    }

    public getMission(missionId: number): Mission | null {
        return this.missionCollection.findOne({ idMissione: missionId });
    }

    public getMissionList(fromDate?: number, toDate?: number): Mission[] {
        let missionList: Mission[] = []
        if (fromDate && toDate) {
            missionList = this.missionCollection.where(t => {
                return t.dataMissione >= fromDate && t.dataMissione <= toDate
            });
        }
        else if (fromDate) {
            missionList = this.missionCollection.find({ dataMissione: { $jgte: fromDate } });
        }
        else if (toDate) {
            missionList = this.missionCollection.where(t => {
                return t.dataMissione <= toDate
            });
        }
        else {
            missionList = this.missionCollection.chain().find().simplesort('dataMissione', true).data();
        }
        missionList = missionList.sort((a, b) => {
            return (new Date(b.dataMissione).getTime()) - (new Date(a.dataMissione).getTime());
        });
        return missionList;
    }

    public checkIfMissionExist(missionDto: MissionDto): boolean {
        console.log()
        const mission: Mission | null = this.missionCollection.findOne({ dataMissione: Moment(missionDto.dataMissione, 'DD-MM-YYYY').set({ hour: 0, minute: 0, second: 0, millisecond: 0 }).toDate().getTime() });
        return mission ? true : false;
    }

    public insertMission(missionDto: MissionDto): Promise<MissionDto | Mission> {
        return new Promise((resolve, reject) => {
            // Controllo se esiste una missione creata per lo stesso giorno
            if (missionDto.idMissione != 0 && this.checkIfMissionExist(missionDto)) {
                this.deviceService.alert('EXISTING_MISSION_MESSAGE');
            }
            else {
                if (this.deviceService.isOnline()) {
                    const requestBody: SaveMissionRequest = new SaveMissionRequest(missionDto);
                    this.apiService.callApi<any>('saveMission', {
                        body: requestBody
                    }).subscribe(
                        (res: any) => {
                            const mission: Mission = new Mission(res);
                            this._saveMission(mission);
                            resolve(res);
                        },
                        error => { reject(error); }
                    );
                }
                else {
                    const mission: Mission = new Mission(missionDto);
                    mission.stato = MissionStatus.TO_SYNCHRONIZE;
                    this._saveMission(mission);
                    resolve(mission);
                }
            }
        });
    }

    public updateMission(missionDto: MissionDto): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                const requestBody: SaveMissionRequest = new SaveMissionRequest(missionDto);
                this.apiService.callApi<MissionDto>('saveMission', {
                    body: requestBody
                }).subscribe(
                    (res: MissionDto) => {
                        const mission: Mission = new Mission(res);
                        this._saveMission(mission);
                        resolve();
                    },
                    reject
                );
            }
            else {
                const mission: Mission = new Mission(missionDto);
                mission.stato = MissionStatus.TO_SYNCHRONIZE;
                this._saveMission(mission);
                resolve();
            }
        });
    }

    public synchronizeMission(mission: Mission): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                if (mission.idMissione !== 0) {
                    if (mission.rilevazioni) {
                        this.updateMission(mission.getDto()).then(() => {
                            Promise.all(this._synchronizeDetection(mission)).then(() => {
                                const promises: Promise<Detection | VisualInspection | Sample | Trapping | void>[] = [];
                                if (mission.rilevazioni) {
                                    mission.rilevazioni.forEach(d => {
                                        if (d.ispezioniVisive && d.ispezioniVisive.length > 0) {
                                            // Ispezioni visive
                                            promises.concat(this._synchronizeVisualInspection(d.ispezioniVisive));
                                        }
                                        if (d.campionamenti && d.campionamenti.length > 0) {
                                            // Campionamenti
                                            promises.concat(this._synchronizeSampling(d.campionamenti));
                                        }
                                        if (d.trappolaggi && d.trappolaggi.length > 0) {
                                            // Trappolaggi
                                            promises.concat(this._synchronizeTrapping(d.trappolaggi));
                                        }
                                    });
                                }
                                // Risoluzione promises per detection
                                Promise.all(promises).then(() => {
                                    resolve();
                                }, reject
                                );
                            }, reject);

                        }).catch(() => reject);
                    }
                    else {
                        this.updateMission(mission.getDto()).then(() => resolve).catch(() => reject);
                    }
                }
                else {
                    this.insertMission(mission.getDto()).then(
                        () => {
                            this._synchronizeDetection(mission);
                        }
                    )
                }
            }
            else {
                resolve();
            }
        });
    }

    private _synchronizeDetection(mission: Mission): Promise<Detection>[] {
        const promises: Promise<Detection>[] = [];
        (mission.rilevazioni as Detection[]).forEach((d: Detection) => {
            promises.push(this.detectionService.saveOrUpdateDetection(d));
        });
        return promises;
    }

    private _synchronizeVisualInspection(ispezioniVisive: VisualInspection[]): Promise<VisualInspection | void>[] {
        const promises: Promise<VisualInspection | void>[] = [];
        ispezioniVisive.forEach((v: VisualInspection) => {
            v.oraInizio = Moment(v.dataOraInizio).format('HH:mm');
            promises.push(this.visualInspectionService.saveOrUpdateVisualInspection(v));
            if (v.photos && v.photos.length > 0) {
                v.photos.forEach(photo => {
                    promises.push(this.visualInspectionService.uploadPhoto(photo));
                })
            }
        });
        return promises;
    }

    private _synchronizeSampling(campionamenti: Sample[]): Promise<Sample | void>[] {
        const promises: Promise<Sample | void>[] = [];
        campionamenti.forEach((s: Sample) => {
            s.oraInizio = Moment(s.dataOraInizio).format('HH:mm');
            promises.push(this.sampleService.saveOrUpdateSample(s));
            if (s.photos && s.photos.length > 0) {
                s.photos.forEach(photo => {
                    promises.push(this.sampleService.uploadPhoto(photo));
                })
            }
        });
        return promises;
    }

    private _synchronizeTrapping(trappolaggi: Trapping[]): Promise<Trapping | void>[] {
        const promises: Promise<Trapping | void>[] = [];
        trappolaggi.forEach((t: Trapping) => {
            t.dataOraInizio = Moment(t.dataOraInizioT).format('HH:mm');
            promises.push(this.trapService.saveOrUpdateTrap(t));
            if (t.photos && t.photos.length > 0) {
                t.photos.forEach(photo => {
                    promises.push(this.trapService.uploadPhoto(photo));
                })
            }
        });
        return promises;
    }

    private _saveMission(mission: Mission): void {
        this.missionCollection.findAndRemove({ idMissione: 0, dataMissione: mission.dataMissione });
        this.missionCollection.findAndRemove({ idMissione: mission.idMissione, dataMissione: mission.dataMissione });
        this.missionCollection.insert(mission);
        this.offlineService.saveUserDB();
    }

    public fetchMissionList(): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                const body: GetMissionListRequest = new GetMissionListRequest();
                return this.apiService.callApi<MissionDto[]>('getMissionList', {
                    body: body
                }).pipe(map(response => {
                    if (response) {
                        // Rimuovo dal DB tutte le missioni che hanno lo stato diverso da TO_SYNCHRONIZE
                        this.missionCollection.findAndRemove({
                            stato: {
                                $ne: MissionStatus.TO_SYNCHRONIZE
                            }
                        });
                        // Lista delle missioni da sincronizzare
                        const dbMissionList = this.missionCollection.find();
                        this.logger.debug('fetchMissionList:: ', dbMissionList);
                        if (dbMissionList.length != 0) {
                            response.map(t => {
                                const missionResult = dbMissionList.find(p => {
                                    return t.idMissione === p.idMissione && t.dataMissione === Moment(new Date(p.dataMissione)).format('DD-MM-YYYY');
                                })
                                if (!missionResult) {
                                    this.missionCollection.insert(new Mission(t));
                                }
                            });
                        }
                        else {
                            response.map(t => {
                                const mission = new Mission(t);
                                this.missionCollection.insert(mission);
                            });
                        }
                        this.offlineService.saveUserDB();
                    }

                })).subscribe(
                    resolve,
                    reject
                );
            }
            else {
                resolve();
            }
        });

    }

    public getInspectors(): Inspector[] {
        return this.inspectorCollection.find()
    }

    public fetchMissionDetail(missionId: number): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                if (missionId != 0) {
                    return this.apiService.callApi<MissionDto>('getMissionDetail', {
                        paths: {
                            idMissione: missionId
                        }
                    }).pipe(map(response => {
                        const mission = new Mission(response);
                        if (mission.stato === MissionStatus.SYNCHRONIZED) {
                            this.missionCollection.findAndRemove({ idMissione: mission.idMissione, dataMissione: mission.dataMissione });
                            this.missionCollection.insert(mission);
                            this.offlineService.saveUserDB();
                        }

                    })).subscribe(
                        resolve,
                        reject
                    );
                }
                else {
                    resolve();
                }
            }
            else {
                resolve();
            }
        });
    }

    public getMissionDetail(missionId: number, missionDate?: number): Mission | null {
        if (missionId != 0) {
            return this.missionCollection.findOne({ idMissione: missionId });
        }
        return this.missionCollection.findOne({ idMissione: missionId, dataMissione: missionDate });
    }

    public deleteMission(mission: Mission): Promise<void> {
        return new Promise((resolve, reject) => {
            if (mission.idMissione !== 0) {
                this.apiService.callApi<MissionDto>('deleteMission', {
                    paths: {
                        idMissione: mission.idMissione
                    }
                }).subscribe(
                    () => {
                        this.missionCollection.findAndRemove({ idMissione: mission.idMissione });
                        this.offlineService.saveUserDB();
                        resolve();
                    },
                    reject
                );
            }
            else {
                this.missionCollection.findAndRemove({ idMissione: mission.idMissione });
                this.offlineService.saveUserDB();
                resolve();
            }
        });
    }

    public downloadPdf(missionId: number): Promise<string> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                return this.apiService.callApi<string>('getPdfTrasferta', {
                    paths: {
                        idMissione: missionId
                    },
                    responseType: ResponseTypes.TEXT
                }).subscribe(
                    res => {
                        resolve(res)
                    },
                    reject
                );
            }
            else {
                resolve('OFFLINE');
            }
        });
    }

}
