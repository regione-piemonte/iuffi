import { Injectable } from '@angular/core';
import { Detection } from '@app/detections/models/detection.model';
import { Photo } from '@app/detections/models/photo.model';
import { Mission, MissionStatus } from '@app/missions/models/mission.model';
import { LoggerService } from '@core/logger/services/logger.service';
import { Camera, CameraOptions, PictureSourceType } from '@ionic-native/camera/ngx';
import { File } from '@ionic-native/file/ngx';
import { WebView } from '@ionic-native/ionic-webview/ngx';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';
import { MonitoringSummary } from '@shared/offline/models/monitoring-summary.model';
import { SampleType } from '@shared/offline/models/sample-type.model';
import { OfflineService } from '@shared/offline/services/offline.service';
import * as moment from 'moment';

import { ApiService } from '../../core/api/services/api.service';
import { LokiDB } from '../../core/db/models/lokidb.model';
import { DeviceService } from '../../core/device/services/device.service';
import { DetectionRequest } from '../api/new-detection-request';
import { DetectionDto } from '../dto/detection.dto';
import { FileStatus } from '../dto/photo.dto';
import { PlantSpecies, PlantSpeciesDetail } from './../../shared/offline/models/plant-species.model';

// import {Resultset} from 'lokijs'
@Injectable()
export class DetectionService {
    private _db: LokiDB | null = null;
    private _userDB: LokiDB | null = null;
    private missionCollection!: Collection<Mission>;
    private photoCollection!: Collection<Photo>;
    private areaTypeCollection!: Collection<AreaTypeLocal>;
    private plantSpeciesCollection!: Collection<PlantSpecies>;
    private monitoringSummaryCollection!: Collection<MonitoringSummary>;
    private harmfulOrganismCollection!: Collection<HarmfulOrganism>;
    private sampleTypesCollection!: Collection<SampleType>;

    constructor(
        private apiService: ApiService,
        private offlineService: OfflineService,
        private deviceService: DeviceService,
        private camera: Camera,
        private logger: LoggerService,
        private file: File,
        private webview: WebView,
        private fileManager: FileManagerService
    ) {
        this._init();
    }

    private _init(): void {
        this._db = this.offlineService.getDB();
        this._userDB = this.offlineService.getUserDB();
        if (this._db) {
            this.areaTypeCollection = this._db.initCollection<AreaTypeLocal>('areaTypes');
            this.plantSpeciesCollection = this._db.initCollection<PlantSpecies>('plantSpecies');
            this.monitoringSummaryCollection = this._db.initCollection<MonitoringSummary>('monitoringSummary');
            this.harmfulOrganismCollection = this._db.initCollection<HarmfulOrganism>('harmfulOrganisms');
            this.sampleTypesCollection = this._db.initCollection<SampleType>('sampleTypes');

        }
        if (this._userDB) {
            this.missionCollection = this._userDB.initCollection<Mission>('missions');
            this.photoCollection = this._userDB.initCollection<Photo>('photos');
        }
    }

    public getAreaType(id: number): AreaTypeLocal | null {
        return this.areaTypeCollection.findOne({ id: id })
    }

    public getAreaTypes(): AreaTypeLocal[] {
        let areaTypeFiltered = this.areaTypeCollection.find();
        // Filtro per periodo di validità
        areaTypeFiltered = areaTypeFiltered.filter(area => {
            // if (el.dettaglioTipoAree && el.dettaglioTipoAree.length > 0) {
            // el.dettaglioTipoAree = el.dettaglioTipoAree.filter(area => {
            if (area.dataInizioValidita && !area.dataFineValidita) {
                if (moment().toDate() >= moment(area.dataInizioValidita, 'mm/dd/yyyy').toDate()) {
                    return area;
                }
            } else if (!area.dataInizioValidita && !area.dataFineValidita) {
                return area;
            }
            // })
            // if (el.dettaglioTipoAree && el.dettaglioTipoAree.length>0) {
            //     return el;
            // }
            // }
        });

        return areaTypeFiltered;
    }

    public getDetection(missionId: number, detectionId: number): Detection | undefined {
        let detection: Detection | undefined;
        const mission = this.missionCollection.findOne({ idMissione: missionId });
        if (mission) {
            detection = mission.rilevazioni?.find(item => item.idRilevazione == detectionId);
        }
        return detection;
    }

    public getPlantSpecies(): { 'plantsFilteredCompatibilita': PlantSpecies[]; 'plantsFilteredValidity': PlantSpecies[]; 'plants': PlantSpecies[] } {
        const month = parseInt(moment().format('M'));
        let monitoringSummaryList = this.monitoringSummaryCollection.find({ mese: month });
        let plants: PlantSpecies[] = [];
        let plantsFilteredValidity: PlantSpecies[] = [];
        const plantsFilteredCompatibilita: PlantSpecies[] = [];

        // Filtro validità lista compatibilità
        monitoringSummaryList = monitoringSummaryList.filter(el => {
            // if (el.dataInizioValiditaF && el.dataFineValiditaF) {
            //     if (moment().toDate() > moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate() &&
            //         moment().toDate() <= moment(el.dataFineValiditaF, 'mm/dd/yyyy').toDate()) {
            //         return el;
            //     }
            // } else
            if (el.dataInizioValiditaF && !el.dataFineValiditaF) {
                if (moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate()) {
                    return el;
                }
            } else if (!el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return el;
            }
        });

        // Tutte le specie
        plants = this.plantSpeciesCollection.find();

        // Primo filtro: validità Dal - Al
        plantsFilteredValidity = plants.filter(el => {
            if (el.listaSpecieVegetali && el.listaSpecieVegetali.length > 0) {
                //Devo filtrare l'array di specie vegetali per ottenere solo specie del periodo
                el.listaSpecieVegetali = el.listaSpecieVegetali.filter(plant => {
                    // if (plant.dataInizioValidita && plant.dataFineValidita) {
                    //     if (moment().toDate() > moment(plant.dataInizioValidita, 'mm/dd/yyyy').toDate() &&
                    //         moment().toDate() <= moment(plant.dataFineValidita, 'mm/dd/yyyy').toDate()) {
                    //         return plant;
                    //     }
                    // } else
                    if (plant.dataInizioValidita && !plant.dataFineValidita) {
                        if (moment().toDate() >= moment(plant.dataInizioValidita, 'mm/dd/yyyy').toDate()) {
                            return plant;
                        }
                    } else if (!plant.dataInizioValidita && !plant.dataFineValidita) {
                        return plant;
                    }
                })
                if (el.listaSpecieVegetali.length > 0) {
                    return el;
                }
            }
        });

        // Filtro lista di compatibilità
        if (monitoringSummaryList.length > 0) {
            plantsFilteredValidity.forEach(plant => {
                plant.listaSpecieVegetali.forEach(sp => {
                    const found = monitoringSummaryList.find(el => {
                        return el.idSpecieVegetale === sp.idSpecieVegetale;
                    })
                    if (found) {
                        plantsFilteredCompatibilita.push(plant);
                    }
                })

            });
        }

        return {
            plantsFilteredCompatibilita: plantsFilteredCompatibilita,
            plantsFilteredValidity: plantsFilteredValidity,
            plants: plants
        };
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

    public getHarmfulOrganisms(plantSpeciesId: number): { 'onFilteredCompatibilita': HarmfulOrganism[]; 'onFilteredValidity': HarmfulOrganism[]; 'on': HarmfulOrganism[] } {
        const month = parseInt(moment().format('M'));
        let monitoringSummaryList = this.monitoringSummaryCollection.find({ mese: month, idSpecieVegetale: plantSpeciesId });
        let on: HarmfulOrganism[] = [];
        let onFilteredValidity: HarmfulOrganism[] = [];
        const onFilteredCompatibilita: HarmfulOrganism[] = [];

        // Filtro validità lista compatibilità
        monitoringSummaryList = monitoringSummaryList.filter(el => {
            // if (el.dataInizioValiditaF && el.dataFineValiditaF) {
            //     if (moment().toDate() > moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate() &&
            //         moment().toDate() <= moment(el.dataFineValiditaF, 'mm/dd/yyyy').toDate()) {
            //         return el;
            //     }
            // } else
            if (el.dataInizioValiditaF && !el.dataFineValiditaF) {
                if (moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate()) {
                    return el;
                }
            } else if (!el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return el;
            }
        });

        // Tutti gli organismi
        on = this.harmfulOrganismCollection.find();

        // Primo filtro: validità Dal - Al
        onFilteredValidity = on.filter(el => {
            // if (el.dataInizioValiditaF && el.dataFineValiditaF) {
            //     return moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate() && moment().toDate() <= moment(el.dataFineValiditaF, 'mm/dd/yyyy').toDate()
            // } else
            if (el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate()
            } else if (!el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return el;
            }
        });

        // Filtro lista di compatibilità
        if (monitoringSummaryList.length > 0) {
            onFilteredValidity.forEach(on => {
                const found = monitoringSummaryList.find(el => {
                    return el.idOrganismoNocivo === on.idOrganismoNocivo;
                })
                if (found) {
                    onFilteredCompatibilita.push(on);
                }
            });
        }

        return {
            onFilteredCompatibilita: onFilteredCompatibilita,
            onFilteredValidity: onFilteredValidity,
            on: on
        };
    }

    public getHarmfulOrganism(id: number): HarmfulOrganism | null {
        return this.harmfulOrganismCollection.findOne({ idOrganismoNocivo: id });
    }

    public getSampleTypes(plantSpeciesId: number, harmfulOrganisms: number[]): { 'sampleTypesFilteredCompatibilita': SampleType[]; 'sampleTypesFilteredValidity': SampleType[]; 'sampleTypes': SampleType[]; 'atLeastOneNotFound' : boolean } {
        const month = parseInt(moment().format('M'));
        let monitoringSummaryList = new Array<MonitoringSummary>();

        let atLeastOneNotFound = false;

        harmfulOrganisms.forEach(harmfulOrganism => {
            const found = this.monitoringSummaryCollection.find({ mese: month, idSpecieVegetale: plantSpeciesId, idOrganismoNocivo: harmfulOrganism });
            found ? (monitoringSummaryList = monitoringSummaryList.concat(found)) : null;
        })

        // Se non viene trovata la compatibilità di almeno uno tra tutti gli on passati come filtro
        // Sono costretto a mostrare una lista completa dei tipi di campione
        harmfulOrganisms.forEach(harmfulOrganism => {
            const found = monitoringSummaryList.find(el => el.idOrganismoNocivo === harmfulOrganism);
            if (!found) {
                atLeastOneNotFound = true;
                return;
            }
        });

        let sampleTypes: SampleType[] = [];
        let sampleTypesFilteredValidity: SampleType[] = [];
        const sampleTypesFilteredCompatibilita: SampleType[] = [];

        // Filtro validità lista compatibilità
        monitoringSummaryList = monitoringSummaryList.filter(el => {
            if (el.dataInizioValiditaF && !el.dataFineValiditaF) {
                if (moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate()) {
                    return el;
                }
            } else if (!el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return el;
            }
        });

        // Tutti gli organismi
        sampleTypes = this.sampleTypesCollection.find();

        // Primo filtro: validità Dal - Al
        sampleTypesFilteredValidity = sampleTypes.filter(el => {
            if (el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return moment().toDate() >= moment(el.dataInizioValiditaF, 'mm/dd/yyyy').toDate()
            } else if (!el.dataInizioValiditaF && !el.dataFineValiditaF) {
                return el;
            }
        });

        // Filtro lista di compatibilità
        if (monitoringSummaryList.length > 0) {
            sampleTypesFilteredValidity.forEach(on => {
                const found = monitoringSummaryList.find(el => {
                    return el.idTipoCampione === on.idTipoCampione
                });
                if (found) {
                    sampleTypesFilteredCompatibilita.push(on);
                }
            });
        }

        return {
            sampleTypesFilteredCompatibilita: sampleTypesFilteredCompatibilita,
            sampleTypesFilteredValidity: sampleTypesFilteredValidity,
            sampleTypes: sampleTypes,
            atLeastOneNotFound: atLeastOneNotFound
        };
    }

    public getSampleType(id: number): SampleType | null {
        return this.sampleTypesCollection.findOne({ idTipoCampione: id });
    }

    public updateDetectionEndDate(detection: Detection): void {
        if (this.deviceService.isOnline()) {
            const requestBody: DetectionRequest = new DetectionRequest(detection);
            this.apiService.callApi<DetectionDto>('saveDetection', {
                body: requestBody
            }).subscribe(
                detectionDto => {
                    if (detectionDto) {
                        const detection: Detection = new Detection(detectionDto);
                        const mission = this.missionCollection.findOne({ idMissione: detection.idMissione });
                        if (mission && mission.rilevazioni) {
                            if (detectionDto.idRilevazione !== 0) {
                                const detectionFound = mission.rilevazioni.find(d => {
                                    return d.idRilevazione === detection.idRilevazione;
                                });
                                if (detectionFound) {
                                    detectionFound.oraFine = detection.oraFine;
                                }

                            }
                        }
                    }
                }
            )
        }
    }

    public saveOrUpdateDetection(detection: Detection): Promise<Detection> {
        // Le attività connesse sono riassegnate alla detection dopo il save
        const campionamentiTemp = detection.campionamenti ? detection.campionamenti : undefined;
        const trappolaggiTemp = detection.campionamenti ? detection.trappolaggi : undefined;
        const ispezioniVisTemp = detection.campionamenti ? detection.ispezioniVisive : undefined;
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                const requestBody: DetectionRequest = new DetectionRequest(detection);
                this.apiService.callApi<DetectionDto>('saveDetection', {
                    body: requestBody
                }).subscribe(
                    detectionDto => {
                        if (detectionDto) {
                            const detection: Detection = new Detection(detectionDto);
                            const mission = this.missionCollection.findOne({ idMissione: detection.idMissione });
                            if (mission && mission.rilevazioni) {
                                if (detectionDto.idRilevazione !== 0) {
                                    const index = mission.rilevazioni.findIndex(d => {
                                        return d.idRilevazione === detection.idRilevazione;
                                    });
                                    if (index > -1) {
                                        mission.rilevazioni?.splice(index, 1);
                                    }
                                }
                                // Recupero attività
                                detection.campionamenti = campionamentiTemp;
                                detection.trappolaggi = trappolaggiTemp;
                                detection.ispezioniVisive = ispezioniVisTemp;
                                mission.rilevazioni.push(detection);
                                this.missionCollection.findAndRemove({ idMissione: mission.idMissione });
                                this.missionCollection.insert(mission.clone());
                                this.offlineService.saveUserDB();
                                resolve(detection);
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
                )
            }
            else {

                const mission = this.missionCollection.findOne({ idMissione: detection.idMissione });
                if (mission && mission.rilevazioni) {
                    if (detection.idRilevazione !== 0) {
                        const index = mission.rilevazioni.findIndex(d => {
                            return d.idRilevazione === detection.idRilevazione;
                        });
                        if (index > -1) {
                            mission.rilevazioni.splice(index, 1);
                        }
                    }
                    // Recupero attività
                    detection.campionamenti = campionamentiTemp;
                    detection.trappolaggi = trappolaggiTemp;
                    detection.ispezioniVisive = ispezioniVisTemp;
                    mission.rilevazioni.push(detection);
                    mission.stato = MissionStatus.TO_SYNCHRONIZE;
                    this.missionCollection.findAndRemove({ idMissione: mission.idMissione });
                    this.missionCollection.insert(mission.clone());
                    this.offlineService.saveUserDB();
                    resolve(detection);
                }
                else {
                    reject();
                }
            }
        });
    }

    public deleteDetection(detection: Detection): Promise<void> {
        return new Promise((resolve, reject) => {
            if (detection.idRilevazione !== 0) {
                if (this.deviceService.isOnline()) {
                    this.apiService.callApi<any>('deleteDetection', {
                        paths: {
                            idRilevazione: detection.idRilevazione
                        }
                    }).subscribe(
                        () => {
                            const mission = this.missionCollection.findOne({ idMissione: detection.idMissione });
                            if (mission && mission.rilevazioni) {
                                const index = mission.rilevazioni.findIndex(d => {
                                    return d.idRilevazione === detection.idRilevazione
                                });
                                if (index > -1) {
                                    mission.rilevazioni.splice(index, 1);
                                    this.missionCollection.update(mission);
                                    this.offlineService.saveUserDB();
                                    resolve();
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

            }
            else {
                const mission = this.missionCollection.findOne({ idMissione: detection.idMissione });
                if (mission && mission.rilevazioni) {
                    const index = mission.rilevazioni.findIndex(d => {
                        return d.idRilevazione === detection.idRilevazione
                    });
                    if (index > -1) {
                        mission.rilevazioni.splice(index, 1);
                        mission.stato = MissionStatus.TO_SYNCHRONIZE;
                        this.missionCollection.update(mission);
                        this.offlineService.saveUserDB();
                        resolve();
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

    public async takePicture(sourceType: PictureSourceType): Promise<Photo> {
        // Selezione da fotogallery
        if (sourceType !== 1) {
            return this.fileManager.selectImage().then(imagePath => {
                const correctPath = imagePath.substr(0, imagePath.lastIndexOf('/') + 1);
                const currentName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                return this._copyFileToLocalDir(correctPath, currentName);
            });
        }
        else
        // Scatto della fotografia
        {
            const options: CameraOptions = {
                quality: 20,
                sourceType: 1,
                encodingType: this.camera.EncodingType.JPEG,
                mediaType: this.camera.MediaType.PICTURE,
                saveToPhotoAlbum: false,
                correctOrientation: true
            };
            const imagePath = await this.camera.getPicture(options);
            let currentName = '';
            // if (this.deviceService.isAndroid()) {
            // return this.filePath.resolveNativePath(imagePath)
            //     .then(filePath => {
            //         const correctPath = filePath.substr(0, filePath.lastIndexOf('/') + 1);
            //         currentName = imagePath.substring(imagePath.lastIndexOf('/') + 1, imagePath.lastIndexOf('?'));
            //         return this._copyFileToLocalDir(correctPath, currentName);
            //     });
            // } else {
            const correctPath = imagePath.substr(0, imagePath.lastIndexOf('/') + 1);
            currentName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
            return this._copyFileToLocalDir(correctPath, currentName);
            // }
        }
    }

    private _copyFileToLocalDir(namePath: string, currentName: string): Promise<Photo> {
        return this.file.copyFile(namePath, currentName, this.file.dataDirectory, currentName).then(
            () => {
                const photo = new Photo();
                photo.nativeFilePath = this.file.dataDirectory + currentName;
                photo.fileName = currentName;
                photo.displayUrl = this.webview.convertFileSrc(this.file.dataDirectory + currentName);
                return photo;
            }
        )
    }

    public removePhoto(photo: Photo): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                this.apiService.callApi<any>('deletePhoto', {
                    paths: {
                        idPhoto: photo.photoId
                    }
                }).subscribe(
                    () => {
                        this.photoCollection.findAndRemove({ photoId: photo.photoId });
                        this.offlineService.saveUserDB();
                        resolve();
                    },
                    err => {
                        this.logger.debug(err);
                        reject();
                    }
                );

            }
            else {
                const photoToDelete = this.photoCollection.findOne({ photoId: photo.photoId });
                if (photoToDelete) {
                    photoToDelete.status = FileStatus.TO_DELETE;
                    this.photoCollection.update(photoToDelete);
                    this.offlineService.saveUserDB();
                }
                resolve();
            }
        });
    }
}
