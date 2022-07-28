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
import { PlantSpecies, PlantSpeciesDetail } from '../../shared/offline/models/plant-species.model';
import { GetAnagraficaAvivResponse } from '../api/get-anagrafica-aviv-response';
import { GetPhotoRequest } from '../api/get-photos-request';
import { PhotoItemResponse } from '../api/get-photos-response';
import { GetSpecieAvivResponse } from '../api/get-specie-aviv-response';
import { InfoPhotoRequest } from '../api/info-photo-request';
import { SampleRequest } from '../api/new-sample-request';
import { FileStatus } from '../dto/photo.dto';
import { Detection } from '../models/detection.model';
import { Photo } from '../models/photo.model';
import { PlantSpeciesAviv } from '../models/plant-species-aviv.model';
import { AvivRegistry } from '../models/registry-aviv.model';
import { Sample } from '../models/sample.model';

@Injectable()
export class SampleService {
    private _db: LokiDB | null = null;
    private _userDB: LokiDB | null = null;
    private missionCollection!: Collection<Mission>;
    private photoCollection!: Collection<Photo>;
    private areaTypeCollection!: Collection<AreaTypeLocal>;
    private plantSpeciesCollection!: Collection<PlantSpecies>;
    private harmfulOrganismCollection!: Collection<HarmfulOrganism>;
    private monitoringSummaryCollection!: Collection<MonitoringSummary>;
    public deleteSample$: Subject<Sample> = new Subject<Sample>();
    public modifySample$: Subject<Sample> = new Subject<Sample>();
    constructor(
        private formBuilder: FormBuilder,
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

    public saveOrUpdateSample(sample: Sample, detectionIn?: Detection): Promise<Sample> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                const body = new SampleRequest(sample);
                this.apiService.callApi<Sample>('saveSampling', {
                    body: body
                }).subscribe(
                    (res: Sample) => {
                        this.logger.debug(res);
                        const mission = this.missionCollection.findOne({ idMissione: sample.idMissione });
                        if (mission && mission.rilevazioni) {
                            const detection = mission.rilevazioni.find(d => {
                                return d.idRilevazione === sample.idRilevazione
                            });
                            if (detection && detectionIn) {
                                detection.campionamenti = detectionIn.campionamenti;
                                detection.ispezioniVisive = detectionIn.ispezioniVisive;
                                detection.trappolaggi = detectionIn.trappolaggi;
                            }
                            if (detection) {
                                if (detection.campionamenti && detection.campionamenti.length > 0) {
                                    const index = detection.campionamenti.findIndex(d => {
                                        return d.idCampionamento === sample.idCampionamento
                                    });
                                    if (index >= 0) {
                                        detection.campionamenti.splice(index, 1);
                                    }
                                }
                                else {
                                    detection.campionamenti = [];
                                }
                                sample.idCampionamento = res.idCampionamento;
                                detection.campionamenti.push(sample);
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
                const mission = this.missionCollection.findOne({ idMissione: sample.idMissione });
                if (mission && mission.rilevazioni) {
                    const detection = mission.rilevazioni.find(d => {
                        return d.idRilevazione === sample.idRilevazione
                    });
                    if (detection) {
                        if (detection.campionamenti && detection.campionamenti.length > 0) {
                            const index = detection.campionamenti.findIndex(d => {
                                return d.idRilevazione === detection.idRilevazione
                            });
                            detection.campionamenti.splice(index, 1);
                        }
                        else {
                            detection.campionamenti = [];
                        }
                        detection.campionamenti.push(sample);
                        mission.stato = MissionStatus.TO_SYNCHRONIZE;
                        this.missionCollection.update(mission);
                        this.offlineService.saveUserDB();
                        resolve(sample);
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

    public deleteSample(sample: Sample): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline() && sample.idCampionamento !== 0) {
                this.apiService.callApi<any>('deleteSampling', {
                    paths: {
                        idCampionamento: sample.idCampionamento
                    }
                }).subscribe(
                    () => {
                        if (this._deleteSampleFromDB(sample, MissionStatus.SYNCHRONIZED)) {
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
                if (this._deleteSampleFromDB(sample, MissionStatus.TO_SYNCHRONIZE)) {
                    resolve();
                }
                else {
                    reject();
                }
            }
        });
    }

    private _deleteSampleFromDB(sample: Sample, status: MissionStatus): boolean {
        const mission = this.missionCollection.findOne({ idMissione: sample.idMissione });
        if (mission && mission.rilevazioni) {
            const detection = mission.rilevazioni.find(d => {
                return d.idRilevazione === sample.idRilevazione
            });
            if (detection && detection.campionamenti) {
                const index = detection.campionamenti.findIndex(d => {
                    return d.idCampionamento === sample.idCampionamento
                });
                if (index >= 0) {
                    detection.campionamenti.splice(index, 1);
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

    public getSamplePhotos(sampleId: number): Photo[] {
        this.logger.debug('getSamplePhotos::sampleId', sampleId);
        const photos = this.photoCollection.find({ sampleId: sampleId, status: { $ne: FileStatus.TO_DELETE } });
        this.logger.debug('getSamplePhotos::photos', photos);
        return photos;
    }

    public savePhotoToSyncronize(photo: Photo): void {
        const mission = this.missionCollection.findOne({ idMissione: photo.missionId });
        if (mission && mission.rilevazioni) {
            const detection = mission.rilevazioni.find(d => {
                return d.idRilevazione === photo.sampleId;
            });

            if (detection && detection.campionamenti) {
                const sample = detection.campionamenti.find(d => {
                    return d.idCampionamento === photo.sampleId;
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
