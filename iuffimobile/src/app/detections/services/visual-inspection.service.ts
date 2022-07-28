import { Injectable } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Mission, MissionStatus } from '@app/missions/models/mission.model';
import { LoggerService } from '@core/logger/services/logger.service';
import { File, FileEntry } from '@ionic-native/file/ngx';
import { WebView } from '@ionic-native/ionic-webview/ngx';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';
import { MonitoringSummary } from '@shared/offline/models/monitoring-summary.model';
import { OfflineService } from '@shared/offline/services/offline.service';
import { from, Subject } from 'rxjs';
import { mergeMap, toArray } from 'rxjs/operators';

import { ApiService } from '../../core/api/services/api.service';
import { LokiDB } from '../../core/db/models/lokidb.model';
import { DeviceService } from '../../core/device/services/device.service';
import { GetAnagraficaAvivResponse } from '../api/get-anagrafica-aviv-response';
import { GetPhotoRequest } from '../api/get-photos-request';
import { PhotoItemResponse } from '../api/get-photos-response';
import { InfoPhotoRequest } from '../api/info-photo-request';
import { VisualInspectionRequest } from '../api/new-visual-inspection-request';
import { FileStatus } from '../dto/photo.dto';
import { Detection } from '../models/detection.model';
import { Photo } from '../models/photo.model';
import { PlantSpeciesAviv } from '../models/plant-species-aviv.model';
import { Sample } from '../models/sample.model';
import { Trapping } from '../models/trapping.model';
import { GetSpecieAvivResponse } from './../api/get-specie-aviv-response';
import { GetReportResponse } from './../api/get-verbale-aviv-response';
import { ReportAvivDto } from './../dto/report-aviv.dto';
import { AvivRegistry } from './../models/registry-aviv.model';
import { VisualInspection } from './../models/visual-inspection.model';
import { DetectionService } from './detection.service';
import { SampleService } from './sample.service';
import { TrapService } from './trap.service';

@Injectable()
export class VisualInspectionService {
    private _db: LokiDB | null = null;
    private _userDB: LokiDB | null = null;
    private missionCollection!: Collection<Mission>;
    private photoCollection!: Collection<Photo>;
    private areaTypeCollection!: Collection<AreaTypeLocal>;
    private harmfulOrganismCollection!: Collection<HarmfulOrganism>;
    private monitoringSummaryCollection!: Collection<MonitoringSummary>;
    public deleteVisualInspection$: Subject<VisualInspection> = new Subject<VisualInspection>();
    public modifyVisualInspection$: Subject<VisualInspection> = new Subject<VisualInspection>();
    constructor(
        private formBuilder: FormBuilder,
        private apiService: ApiService,
        private offlineService: OfflineService,
        private deviceService: DeviceService,
        private detectionService: DetectionService,
        private sampleService: SampleService,
        private trapService: TrapService,
        private logger: LoggerService,
        private fileManager: FileManagerService,
        private file: File,
        private webview: WebView,
    ) {
        this._init();
    }

    private _init(): void {
        this._db = this.offlineService.getDB();
        this._userDB = this.offlineService.getUserDB();
        if (this._db) {
            this.areaTypeCollection = this._db.initCollection<AreaTypeLocal>('areaTypes');
            this.harmfulOrganismCollection = this._db.initCollection<HarmfulOrganism>('harmfulOrganisms');
            this.monitoringSummaryCollection = this._db.initCollection<MonitoringSummary>('monitoringSummary');
        }
        if (this._userDB) {
            this.missionCollection = this._userDB.initCollection<Mission>('missions');
            this.photoCollection = this._userDB.initCollection<Photo>('photos');
        }
    }

    public getAreaType(id: number): AreaTypeLocal | null {
        return this.areaTypeCollection.findOne({ id: id })
    }

    public getVisualInspection(missionId: number, detectionId: number, visualId: number): VisualInspection | undefined {
        let visual: VisualInspection | undefined;
        let detection: Detection | undefined;
        const mission = this.missionCollection.findOne({ idMissione: missionId });
        if (mission) {
            detection = mission.rilevazioni?.find(item => item.idRilevazione == detectionId);
        }
        if (detection) {
            visual = detection.ispezioniVisive?.find(item => item.idIspezione == visualId);
        }

        if (visual) {
            return visual;
        }

    }

    public saveOrUpdateVisualInspection(visualInspection: VisualInspection, detectionIn?: Detection): Promise<VisualInspection> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                const body = new VisualInspectionRequest(visualInspection);
                this.apiService.callApi<VisualInspection>('saveVisualInspection', {
                    body: body
                }).subscribe(
                    (res: VisualInspection) => {
                        this.logger.debug(res);
                        // Se la detection è ancora offline
                        const mission = this.missionCollection.findOne({ idMissione: visualInspection.idMissione });
                        if (mission && mission.rilevazioni) {
                            const detection = mission.rilevazioni.find(d => {
                                return d.idRilevazione === visualInspection.idRilevazione
                            });
                            if (detection && detectionIn) {
                                detection.campionamenti = detectionIn.campionamenti;
                                detection.ispezioniVisive = detectionIn.ispezioniVisive;
                                detection.trappolaggi = detectionIn.trappolaggi;
                            }
                            if (detection) {
                                // Ispezioni vi
                                if (detection.ispezioniVisive && detection.ispezioniVisive.length > 0) {
                                    const index = detection.ispezioniVisive.findIndex(d => {
                                        return d.idIspezione === visualInspection.idIspezione
                                    });
                                    if (index >= 0) {
                                        detection.ispezioniVisive.splice(index, 1);
                                    }
                                }
                                else {
                                    detection.ispezioniVisive = [];
                                }
                                visualInspection.idIspezione = res.idIspezione;
                                detection.ispezioniVisive.push(visualInspection);
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
                const mission = this.missionCollection.findOne({ idMissione: visualInspection.idMissione });
                if (mission && mission.rilevazioni) {
                    const detection = mission.rilevazioni.find(d => {
                        return d.idRilevazione === visualInspection.idRilevazione
                    });
                    if (detection) {
                        if (detection.ispezioniVisive && detection.ispezioniVisive.length > 0) {
                            const index = detection.ispezioniVisive.findIndex(d => {
                                return d.idRilevazione === detection.idRilevazione
                            });
                            detection.ispezioniVisive.splice(index, 1);
                        }
                        else {
                            detection.ispezioniVisive = [];
                        }
                        detection.ispezioniVisive.push(visualInspection);
                        mission.stato = MissionStatus.TO_SYNCHRONIZE;
                        this.missionCollection.update(mission);
                        this.offlineService.saveUserDB();
                        resolve(visualInspection);
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

    public deleteVisualInspection(visualInspection: VisualInspection): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline() && visualInspection.idIspezione !== 0) {
                this.apiService.callApi<any>('deleteVisualInspection', {
                    paths: {
                        idIspezione: visualInspection.idIspezione
                    }
                }).subscribe(
                    () => {
                        if (this._deleteVisualInspectionFromDB(visualInspection, MissionStatus.SYNCHRONIZED)) {
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
                if (visualInspection.idIspezione === 0) {
                    resolve();
                }
                else if (this._deleteVisualInspectionFromDB(visualInspection, MissionStatus.TO_SYNCHRONIZE)) {
                    resolve();
                }
                else {
                    reject();
                }
            }
        });
    }

    private _deleteVisualInspectionFromDB(visualInspection: VisualInspection, status: MissionStatus): boolean {
        const mission = this.missionCollection.findOne({ idMissione: visualInspection.idMissione });
        if (mission && mission.rilevazioni) {
            const detection = mission.rilevazioni.find(d => {
                return d.idRilevazione === visualInspection.idRilevazione
            });
            if (detection && detection.ispezioniVisive) {
                const index = detection.ispezioniVisive.findIndex(d => {
                    return d.idIspezione === visualInspection.idIspezione
                });
                if (index >= 0) {
                    detection.ispezioniVisive.splice(index, 1);
                }
                mission.stato = status;
                this.missionCollection.update(mission);
                this.offlineService.saveUserDB();
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public getAnagraficheAviv(codAz: string): Promise<AvivRegistry | null> {
        return new Promise((resolve, reject) => {
            this.apiService.callApi<GetAnagraficaAvivResponse>('getAnagraficheAviv', {
                paths: {
                    codAz: codAz
                }
            }).subscribe(
                res => {
                    if (res.esito.codErr === 0) {
                        resolve(new AvivRegistry(res.dati))
                    }
                    else if (res.esito.codErr === 1) {
                        this.deviceService.alert('NO_BUSINESS');
                        resolve(null);
                    }
                    else {
                        reject(res.esito.descErr);
                    }
                },
                reject
            );
        });
    }

    public getPlantSpeciesAviv(codAz: string): Promise<PlantSpeciesAviv[]> {
        return new Promise((resolve, reject) => {
            this.apiService.callApi<GetSpecieAvivResponse>('getSpecieAviv', {
                paths: {
                    codAz: codAz
                }
            }).subscribe(
                res => {
                    if (res.esito.codErr === 0) {
                        const results: PlantSpeciesAviv[] = res.dati.map(t => new PlantSpeciesAviv(t));
                        resolve(results)
                    }
                    else if (res.esito.codErr === 1) {
                        resolve([]);
                    }
                    else {
                        reject(res.esito.descErr);
                    }
                },
                reject
            );
        });
    }

    public downloadReportAviv(idUte: number): Promise<ReportAvivDto> {
        return new Promise((resolve, reject) => {
            this.apiService.callApi<GetReportResponse>('getVerbaleAviv', {
                paths: {
                    idUte: idUte
                }
            }).subscribe(
                res => {
                    if (res.esito.codErr === 0) {
                        resolve(res.dati)
                    }
                    if (res.esito.codErr === 1) {
                        this.deviceService.alert('NO_REPORT_CODE1');
                        // Presento comunque un pdf da scaricare
                        if (res.dati) {
                            resolve(res.dati);
                        }
                    } else  if (res.esito.codErr === 2) {
                        this.deviceService.alert('NO_REPORT_CODE2');
                        resolve(res.esito.descErr);
                    }
                    else  if (res.esito.codErr === -1) {
                        this.deviceService.alert('NO_REPORT_CODEM1');
                        resolve(res.esito.descErr);
                    }
                },
                (err: any) => {
                    reject(err);
                }
            );
        });
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
                    .catch(err => {
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

    public getVisualInspectionPhotos(visualInspectionId: number): Photo[] {
        this.logger.debug('getVisualInspectionPhotos::visualInspectionId', visualInspectionId);
        const photos = this.photoCollection.find({ visualInspectionId: visualInspectionId, status: { $ne: FileStatus.TO_DELETE } });
        this.logger.debug('getVisualInspectionPhotos::photos', photos);
        return photos;
    }

    public savePhotoToSyncronize(photo: Photo): void {
        const mission = this.missionCollection.findOne({ idMissione: photo.missionId });
        if (mission && mission.rilevazioni) {
            const detection = mission.rilevazioni.find(d => {
                return d.idRilevazione === photo.detectionId;
            });

            if (detection && detection.ispezioniVisive) {
                const visualInspection = detection.ispezioniVisive.find(d => {
                    return d.idIspezione === photo.visualInspectionId;
                });
                if (visualInspection) {
                    visualInspection.photos.push(photo);
                    mission.stato = MissionStatus.TO_SYNCHRONIZE;
                    this.missionCollection.update(mission);
                    this.offlineService.saveUserDB();
                }

            }
        }
    }

    public checkInternalDetection(visualInspection: VisualInspection): boolean {
        let result = false;
        const detection = this.detectionService.getDetection(visualInspection.idMissione, visualInspection.idRilevazione);
        if (detection) {
            const samplings: Sample[] | undefined = detection.campionamenti || [];
            if (samplings.length > 0) {
                samplings.map(s => {
                    if (s.idIspezioneVisiva === visualInspection.idIspezione) {
                        result = true;
                    }
                })
            }
            const trappings: Trapping[] | undefined = detection.trappolaggi || [];
            if (trappings.length > 0) {
                trappings.map(t => {
                    if (t.idIspezioneVisiva === visualInspection.idIspezione) {
                        result = true;
                    }
                })
            }
        }
        return result;
    }

    public deleteVisualAndInternalDetection(visualInspection: VisualInspection): Promise<void> {
        return new Promise((resolve, reject) => {
            const promises: Promise<Sample | Trapping | void>[] = [];
            const detection = this.detectionService.getDetection(visualInspection.idMissione, visualInspection.idRilevazione);
            if (detection) {
                const samplings: Sample[] | undefined = detection.campionamenti || [];
                const trappings: Trapping[] | undefined = detection.trappolaggi || [];

                if (samplings.length > 0) {
                    samplings.forEach((s: Sample) => {
                        if (s.idIspezioneVisiva === visualInspection.idIspezione) {
                            promises.push(this.sampleService.deleteSample(s));
                        }
                    });
                }

                if (trappings.length > 0) {
                    trappings.forEach((t: Trapping) => {
                        if (t.idIspezioneVisiva === visualInspection.idIspezione) {
                            promises.push(this.trapService.deleteTrap(t));
                        }
                    });
                }

                // Risoluzione promises
                Promise.all(promises).then(() => {
                    this.deleteVisualInspection(visualInspection).then(
                        resolve,
                        reject
                    );
                }, reject);
            }
            else {
                reject();
            }
        });

    }
}
