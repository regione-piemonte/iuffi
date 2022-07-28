import { Injectable } from '@angular/core';
import { Mission, MissionStatus } from '@app/missions/models/mission.model';
import { LoggerService } from '@core/logger/services/logger.service';
import { File, FileEntry } from '@ionic-native/file/ngx';
import { WebView } from '@ionic-native/ionic-webview/ngx';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';
import { MonitoringSummary } from '@shared/offline/models/monitoring-summary.model';
import { TrapTypeByHarmfulOrganism } from '@shared/offline/models/trap-type-by-harmful-organism.model';
import { TrapType } from '@shared/offline/models/trap-type.model';
import { OfflineService } from '@shared/offline/services/offline.service';
import * as moment from 'moment';
import { from, Subject } from 'rxjs';
import { mergeMap, toArray } from 'rxjs/operators';

import { ApiService } from '../../core/api/services/api.service';
import { LokiDB } from '../../core/db/models/lokidb.model';
import { DeviceService } from '../../core/device/services/device.service';
import { PlantSpecies, PlantSpeciesDetail } from '../../shared/offline/models/plant-species.model';
import { GetPhotoRequest } from '../api/get-photos-request';
import { PhotoItemResponse } from '../api/get-photos-response';
import { InfoPhotoRequest } from '../api/info-photo-request';
import { TrapRequest } from '../api/new-trap-request';
import { FileStatus } from '../dto/photo.dto';
import { TrappingDto } from '../dto/trapping.dto';
import { Coordinate } from '../models/coordinate.model';
import { Detection } from '../models/detection.model';
import { Photo } from '../models/photo.model';
import { Trap } from '../models/trap.model';
import { Trapping } from '../models/trapping.model';
import { TrapDto } from './../dto/trap.dto';

@Injectable()
export class TrapService {
    private _db: LokiDB | null = null;
    private _userDB: LokiDB | null = null;
    private missionCollection!: Collection<Mission>;
    private photoCollection!: Collection<Photo>;
    private areaTypeCollection!: Collection<AreaTypeLocal>;
    private plantSpeciesCollection!: Collection<PlantSpecies>;
    private trapTypesCollection!: Collection<TrapType>;
    private monitoringSummaryCollection!: Collection<MonitoringSummary>;
    private trapTypeByHOCollection!: Collection<TrapTypeByHarmfulOrganism>;
    public deleteTrap$: Subject<Trapping> = new Subject<Trapping>();
    public modifyTrap$: Subject<Trapping> = new Subject<Trapping>();
    constructor(
        private apiService: ApiService,
        private offlineService: OfflineService,
        private deviceService: DeviceService,
        private logger: LoggerService,
        private fileManager: FileManagerService,
        private file: File,
        private webview: WebView
    ) {
        this._init();
    }

    private _init(): void {
        this._db = this.offlineService.getDB();
        this._userDB = this.offlineService.getUserDB();
        if (this._db) {
            this.areaTypeCollection = this._db.initCollection<AreaTypeLocal>('areaTypes');
            this.plantSpeciesCollection = this._db.initCollection<PlantSpecies>('plantSpecies');
            this.trapTypesCollection = this._db.initCollection<TrapType>('trapTypes');
            this.monitoringSummaryCollection = this._db.initCollection<MonitoringSummary>('monitoringSummary');
            this.trapTypeByHOCollection = this._db.initCollection<TrapTypeByHarmfulOrganism>('trapTypesByHO');
        }
        if (this._userDB) {
            this.missionCollection = this._userDB.initCollection<Mission>('missions');
            this.photoCollection = this._userDB.initCollection<Photo>('photos');
        }
    }

    public getAreaType(id: number): AreaTypeLocal | null {
        return this.areaTypeCollection.findOne({ id: id })
    }

    public getPlantSpeciesDetail(id: number): PlantSpeciesDetail | null {
        const list = this.plantSpeciesCollection.find();
        let result: PlantSpeciesDetail | null = null;
        list.map(item => {
            item.listaSpecieVegetali.map(item => {
                if (item.idSpecieVegetale === id) {
                    result = item;
                };
            })
        });
        return result;
    }

    // public getTrapTypes(): TrapType[] {
    //     let sampleTypesFiltered = this.trapTypesCollection.find();
    //     // Filtro per periodo di validità
    //     sampleTypesFiltered = sampleTypesFiltered.filter(el => {
    //         if (el.dataInizioValiditaF && el.dataFineValiditaF) {
    //             return moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate() && moment().toDate() <= moment(el.dataFineValiditaF, 'mm/dd/yyyy').toDate()
    //         } else if (el.dataInizioValiditaF) {
    //             return moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate()
    //         }
    //     });
    //     return sampleTypesFiltered;
    // }

    public getTrapTypes(harmfulOrganism: HarmfulOrganism): { 'trapTypesFilteredCompatibilita': TrapType[]; 'trapTypesFilteredValidity': TrapType[]; 'trapTypes': TrapType[] } {

        let trapTypesByHO: TrapTypeByHarmfulOrganism[] = [];
        let trapTypes: TrapType[] = [];
        let trapTypesFilteredValidity: TrapType[] = [];
        const trapTypesFilteredCompatibilita: TrapType[] = [];

        trapTypesByHO = this.trapTypeByHOCollection.find();
        trapTypes = this.trapTypesCollection.find();

        // Filtro validità lista compatibilità
        trapTypesByHO = trapTypesByHO.filter(el => {
            if (el.dataInizioValiditaF && !el.dataFineValiditaF) {
                if (moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate()) {
                    return el;
                }
            } else if (!el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return el;
            }
        });

        // Primo filtro: validità Dal - Al
        trapTypesFilteredValidity = trapTypes.filter(el => {
            if (el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate()
            } else if (!el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return el;
            }
        });

        trapTypesByHO = trapTypesByHO.filter(trap => {
            return trap.idOn === harmfulOrganism.idOrganismoNocivo;
        })

        // Filtro lista di compatibilità
        if (trapTypesByHO.length > 0) {
            trapTypesByHO.forEach(el => {
                const found = trapTypesFilteredValidity.find(trap => {
                    return el.idTrappola === trap.idTipoTrappola
                });
                if (found) {
                    trapTypesFilteredCompatibilita.push(found);
                }
            });
        }

        return {
            trapTypesFilteredCompatibilita: trapTypesFilteredCompatibilita,
            trapTypesFilteredValidity: trapTypesFilteredValidity,
            trapTypes: trapTypes
        };
    }

    public getTrapType(id: number): TrapType | null {
        return this.trapTypesCollection.findOne({ idTipoTrappola: id });
    }

    /**
    * Restituisce le trappole nel raggio di tot metri dalla posizione passata in input
    * @param {Coordinate} position
    * @param {string} radius
    * @return {*}  {Promise<Trap[]>}
    * @memberof TrapService
    */
    public getTrapsByPosition(position: Coordinate, radius: string): Promise<Trap[]> {
        return new Promise((resolve, reject) => {
            this.apiService.callApi<TrapDto[]>('getTraps', {
                paths: {
                    lat: position.latitudine,
                    lng: position.longitudine,
                    radius: radius
                }
            }).subscribe(
                res => {
                    const traps = res.map(t => new Trap(t))
                    resolve(traps);
                },
                reject
            );
        });
    }

    /**
     * Restituisce la trappola dato il codice sfr
     * @param {string} sfrCode
     * @return {*}  {Promise<Trap>}
     * @memberof TrapService
     */
    public getTrapByCode(sfrCode: string): Promise<Trap> {
        return new Promise((resolve, reject) => {
            this.apiService.callApi<TrapDto>('getTrapByCode', {
                paths: {
                    sfrCode: sfrCode
                }
            }).subscribe(
                res => {
                    resolve(new Trap(res));
                },
                reject
            );
        });
    }

    /**
     * Restituisce la storia delle attività effettuate su una trappola
     * @param {number} trapId
     * @return {*}  {Promise<Trapping[]>}
     * @memberof TrapService
     */
    public getTrapHistory(trapId: number): Promise<Trapping[]> {
        return new Promise((resolve, reject) => {
            this.apiService.callApi<TrappingDto[]>('getTrapHistory', {
                paths: {
                    trapId: trapId
                }
            }).subscribe(
                res => {
                    const trappings = res.map(t => new Trapping(t))
                    resolve(trappings);
                },
                reject
            );
        });
    }

    public saveOrUpdateTrap(trap: Trapping, detectionIn?: Detection): Promise<Trapping> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                const body = new TrapRequest(trap);
                this.apiService.callApi<Trapping>('trappolaggio', {
                    body: body
                }).subscribe(
                    (res: Trapping) => {
                        this.logger.debug(res);
                        const mission = this.missionCollection.findOne({ idMissione: trap.idMissione });
                        if (mission && mission.rilevazioni) {
                            const detection = mission.rilevazioni.find(d => {
                                return d.idRilevazione === trap.idRilevazione
                            });
                            if (detection && detectionIn) {
                                detection.campionamenti = detectionIn.campionamenti;
                                detection.ispezioniVisive = detectionIn.ispezioniVisive;
                                detection.trappolaggi = detectionIn.trappolaggi;
                            }
                            if (detection) {
                                if (detection.trappolaggi && detection.trappolaggi.length > 0) {
                                    const index = detection.trappolaggi.findIndex(d => {
                                        return d.idTrappolaggio === trap.idTrappolaggio
                                    });
                                    if (index >= 0) {
                                        detection.trappolaggi.splice(index, 1);
                                    }
                                }
                                else {
                                    detection.trappolaggi = [];
                                }
                                trap.idTrappolaggio = res.idTrappolaggio;
                                !detection.trappolaggi ? detection.trappolaggi = new Array<Trapping>() : null;
                                detection.trappolaggi.push(trap);
                                this.missionCollection.update(mission);
                                this.offlineService.saveUserDB();
                                resolve(res);
                            }
                            else {
                                reject();
                            }
                        }
                        else {
                            reject();
                        }
                    },
                    reject
                );
            }
            else {
                const mission = this.missionCollection.findOne({ idMissione: trap.idMissione });
                if (mission && mission.rilevazioni) {
                    const detection = mission.rilevazioni.find(d => {
                        return d.idRilevazione === trap.idRilevazione
                    });
                    if (detection) {
                        if (detection.trappolaggi && detection.trappolaggi.length > 0) {
                            const index = detection.trappolaggi.findIndex(d => {
                                return d.idRilevazione === detection.idRilevazione
                            });
                            detection.trappolaggi.splice(index, 1);
                        }
                        else {
                            detection.trappolaggi = [];
                        }
                        detection.trappolaggi.push(trap);
                        mission.stato = MissionStatus.TO_SYNCHRONIZE;
                        this.missionCollection.update(mission);
                        this.offlineService.saveUserDB();
                        resolve(trap);
                    }
                    else {
                        reject();
                    }
                }
                else {
                    reject();
                }
            }
        });
    }

    public deleteTrap(trap: Trapping): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline() && trap.idTrappolaggio !== 0) {
                this.apiService.callApi<any>('deleteTrap', {
                    paths: {
                        idTrappolaggio: trap.idTrappolaggio
                    }
                }).subscribe(
                    () => {
                        if (this._deleteTrapFromDB(trap, MissionStatus.SYNCHRONIZED)) {
                            resolve();
                        }
                        else {
                            reject();
                        }
                    },
                    reject
                );
            }
            else {
                if (this._deleteTrapFromDB(trap, MissionStatus.TO_SYNCHRONIZE)) {
                    resolve();
                }
                else {
                    reject();
                }
            }
        });
    }

    private _deleteTrapFromDB(trap: Trapping, status: MissionStatus): boolean {
        const mission = this.missionCollection.findOne({ idMissione: trap.idMissione });
        if (mission && mission.rilevazioni) {
            const detection = mission.rilevazioni.find(d => {
                return d.idRilevazione === trap.idRilevazione
            });
            if (detection && detection.trappolaggi) {
                const index = detection.trappolaggi.findIndex(d => {
                    return d.idTrappolaggio === trap.idTrappolaggio
                });
                if (index >= 0) {
                    detection.trappolaggi.splice(index, 1);
                }
                mission.stato = status;
                this.missionCollection.update(mission);
                this.offlineService.saveUserDB();
                return true;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public uploadPhoto(photo: Photo): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                this.file.resolveLocalFilesystemUrl(photo.nativeFilePath)
                    .then(entry => {
                        (entry as FileEntry).file(file => {
                            const formData = new FormData();
                            this.fileManager
                                .readFileAsByteArray(file).then(
                                    output => {
                                        const infoPhotoRequest = new InfoPhotoRequest(photo);
                                        const json = JSON.stringify(infoPhotoRequest);
                                        const jsonBlob = new Blob([json], {
                                            type: 'application/json'
                                        });
                                        formData.append('infoFoto', jsonBlob);
                                        formData.append('image', output.file, photo.fileName);
                                        const body = formData;
                                        this.apiService.callApi<any>('savePhoto', {
                                            body: body,
                                            headers: {
                                                enctype: 'multipart/form-data;',
                                                Accept: 'application/json',
                                            }
                                        }).subscribe(
                                            resolve,
                                            err => {
                                                this.savePhotoToSyncronize(photo);
                                                this.logger.debug(err);
                                                reject();
                                            }
                                        );
                                    }
                                ).catch();
                        })
                    })
                    .catch(() => {
                        reject()
                    });
            }
            else {
                this.savePhotoToSyncronize(photo);
                resolve();
            }
        });
    }

    public fetchPhotos(photoRequest: GetPhotoRequest): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                this.apiService.callApi<PhotoItemResponse[]>('getPhotos', {
                    body: photoRequest
                }).subscribe(
                    res => {
                        this._removeAllPhotos(photoRequest);
                        const savedFiles: Promise<Photo>[] = [];
                        res.map((item: PhotoItemResponse) => {
                            const photo = this.photoCollection.findOne({ photoId: item.idFoto });
                            if (!photo) {
                                savedFiles.push(this._saveImageToFileSystem(item));
                            }
                        });

                        from(savedFiles).pipe(
                            mergeMap(photo => photo),
                            toArray()
                        ).subscribe(res => {
                            this.logger.debug('fetchPhotos::', res);
                            resolve();
                        })
                    },
                );
            }
            else {
                resolve();
            }
        });
    }

    private _saveImageToFileSystem(photoItem: PhotoItemResponse): Promise<Photo> {
        return new Promise((resolve, reject) => {
            const blob: Blob = this.fileManager.b64toBlob(photoItem.base64, 'image/jpeg');
            this.file
                .writeFile(this.file.dataDirectory, photoItem.nomeFile, blob, { replace: true })
                .then((res: FileEntry) => {
                    this.logger.debug(res);
                    const photo = new Photo();
                    photo.photoId = photoItem.idFoto;
                    photo.visualInspectionId = photoItem.idVisual;
                    photo.sampleId = photoItem.idCampionamento;
                    photo.trapId = photoItem.idTrappolaggio;
                    photo.fileName = photoItem.nomeFile;
                    photo.latitude = photoItem.latitudine;
                    photo.longitude = photoItem.longitudine;
                    photo.nativeFilePath = res.nativeURL;
                    photo.displayUrl = this.webview.convertFileSrc(res.nativeURL);
                    photo.status = FileStatus.SYNCHRONIZED;
                    this.photoCollection.insert(photo);
                    this.offlineService.saveUserDB();
                    resolve(photo);
                })
                .catch((err: any) => {
                    reject();
                    this.logger.debug(err);
                });
        });
    }

    private _removeAllPhotos(photoRequest: GetPhotoRequest): void {
        if (photoRequest.idVisual) {
            this.photoCollection.findAndRemove({ visualInspectionId: photoRequest.idVisual, status: { $ne: FileStatus.TO_DELETE } });
        }
        if (photoRequest.idCampionamento) {
            this.photoCollection.findAndRemove({ sampleId: photoRequest.idCampionamento, status: { $ne: FileStatus.TO_DELETE } });
        }
        if (photoRequest.idTrappolaggio) {
            this.photoCollection.findAndRemove({ trapId: photoRequest.idTrappolaggio, status: { $ne: FileStatus.TO_DELETE } });
        }
        this.offlineService.saveUserDB();
    }

    public getTrapPhotos(trapId: number): Photo[] {
        this.logger.debug('getTrapPhotos::trapId', trapId);
        const photos = this.photoCollection.find({ trapId: trapId, status: { $ne: FileStatus.TO_DELETE } });
        this.logger.debug('getTrapPhotos::photos', photos);
        return photos;
    }

    public savePhotoToSyncronize(photo: Photo): void {
        const mission = this.missionCollection.findOne({ idMissione: photo.missionId });
        if (mission && mission.rilevazioni) {
            const detection = mission.rilevazioni.find(d => {
                return d.idRilevazione === photo.sampleId;
            });

            if (detection && detection.trappolaggi) {
                const sample = detection.trappolaggi.find(d => {
                    return d.idTrappolaggio === photo.trapId;
                });
                if (sample) {
                    sample.photos.push(photo);
                    mission.stato = MissionStatus.TO_SYNCHRONIZE;
                    this.missionCollection.update(mission);
                    this.offlineService.saveUserDB();
                }

            }
        }
    }
}
