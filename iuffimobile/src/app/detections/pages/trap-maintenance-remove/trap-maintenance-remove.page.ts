import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { FileStatus } from '@app/detections/dto/photo.dto';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { Detection } from '@app/detections/models/detection.model';
import { Trap } from '@app/detections/models/trap.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { TrapService } from '@app/detections/services/trap.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { RootNavController } from '@core/split-view';
import { AutoUnsubscribe } from '@core/utils';
import { ENV } from '@env';
import { Camera, PictureSourceType } from '@ionic-native/camera/ngx';
import { ActionSheetController, ModalController, NavParams } from '@ionic/angular';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { GeoUtilsService } from '@shared/geo-utils/geo-utils.service';
import { Comune } from '@shared/geo-utils/model/comune.model';
import { Point } from '@shared/geo-utils/model/point.model';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';
import * as Moment from 'moment';
import { from } from 'rxjs';
import { mergeMap, toArray } from 'rxjs/operators';
import { AvivModalPage } from '../aviv-modal/aviv-modal.page';
import { DetectionPage } from '../detection/detection.page';
import { TrapOpSelectionPage } from '../trap-op-selection/trap-op-selection.page';
import { VisualInspectionPage } from '../visual-inspection/visual-inspection.page';


@Component({
    selector: 'trap-manteinance-remove',
    templateUrl: './trap-maintenance-remove.page.html',
    styleUrls: ['./trap-maintenance-remove.page.scss'],
})
export class TrapMaintenanceRemovePage extends AutoUnsubscribe {
    public trapping: Trapping;
    public trappingInstallation: Trapping | undefined;
    public trappingHistory: Trapping[] = [];
    public headerLabel: string;
    public dataOdierna: Date = new Date();
    public comune: Comune | null;
    public detection: Detection;
    public recharge = false;
    private action: string;
    public actionRoot: string;
    private missionId: string;
    private visualInspection: VisualInspection;
    public enabledOperation = true;
    private isNewTrapping = false;
    public installationDate: string | null = null;
    public harmfulOrganism: HarmfulOrganism | null = null;
    private modal!: HTMLIonModalElement;
    public isRelatedToNursery = false;

    constructor(
        private rootNavController: RootNavController,
        private logger: LoggerService,
        public deviceService: DeviceService,
        private navParams: NavParams,
        private detectionService: DetectionService,
        private modalController: ModalController,
        private trapService: TrapService,
        private geoUtilsService: GeoUtilsService,
        private camera: Camera,
        private actionSheetCtrl: ActionSheetController,
        public fileManagerService: FileManagerService,
        public sanitizer: DomSanitizer
    ) {
        super();
        this.trapping = this.navParams.get('trapping') as Trapping;
        this.action = this.navParams.get('action') as string;
        this.actionRoot = this.navParams.get('actionRoot') as string;
        this.detection = this.navParams.get('detection') as Detection;
        this.trappingHistory = this.navParams.get('trappingHistory');
        this.missionId = this.navParams.get('missionId') as string;
        this.visualInspection = this.navParams.get('visualInspection') as VisualInspection;
        if (this.trapping.idTrappolaggio === 0) {
            this.isNewTrapping = true;
        }
        if (this.trapping.trappola.idSpecieVeg && !this.trapping.trappola.specie) {
            this.trapping.trappola.specie = this.trapping.specie;
        }

        // Tipologia di operazione
        if (this.action) {
            this.trapping.idOperazione = this.action === 'maintenance' ? 2 : 4
        }
        this.trapping.idOperazione === 4 ? this.headerLabel = 'REMOVAL' : this.headerLabel = 'MAINTENANCE'
        this.trapping.idMissione = this.detection.idMissione;

        // Controllo se l'ispezione visiva è all'interno di una rilevazione relativa ad un vivaio
        if (this.detection.idTipoArea) {
            const currentAreaType: AreaTypeLocal | null = this.detectionService.getAreaType(this.detection.idTipoArea);
            if (currentAreaType) {
                this.isRelatedToNursery = currentAreaType.codiceUfficiale === 'V';
            }
        }

        let point: Point;
        if (ENV.devMode) {
            point = new Point(45.240884, 8.260742);
        }
        else {
            point = new Point(this.trapping.trappola.latitudine, this.trapping.trappola.longitudine);
        }
        this.comune = this.geoUtilsService.getComuneByPoint(point);
        this.trapping.istatComune = this.comune ? this.comune.istat : '000000';

        if (this.trappingHistory && this.trappingHistory.length > 0) {
            // Trappola di trapping operazione 1 per ricavare l'installatore e la data di installazione
            this.trappingInstallation = this.trappingHistory.find(el => { return el.idOperazione === 1 });
            if (this.trappingInstallation) {
                this.trapping.ispettore = this.trappingInstallation.ispettore;
                this.harmfulOrganism = this.detectionService.getHarmfulOrganism(this.trappingInstallation.idOrganismoNocivo as number);
            }

            const trappingRemoval = this.trappingHistory.find(el => { return el.idOperazione === 4 });
            if (trappingRemoval) {
                this.enabledOperation = false;
            }
        }

        if (!this.trapping.descrTrappola && this.trapping.trappola.descrTrappola) {
            this.trapping.descrTrappola = this.trapping.trappola.descrTrappola;
        }

        if (this.deviceService.isCordova()) {
            this.geoUtilsService.configGpsTracking();
        }

        // Controllo se ci sono delle foto associate al trappolaggio
        if (this.trapping.foto && this.trapping.idTrappolaggio && this.trapping.idTrappolaggio !== 0) {
            this.trapping.photos = this.trapService.getTrapPhotos(this.trapping.idTrappolaggio);
        }

        if (this.trapping.trappola?.dataInstallazione) {
            this.installationDate = this.trapping.trappola.dataInstallazione;
        }

    }

    public async selectImageSource(): Promise<void> {
        const buttons = [
            {
                text: 'Scatta foto',
                icon: 'camera',
                handler: () => {
                    this.addImage(this.camera.PictureSourceType.CAMERA);
                }
            },
            {
                text: 'Scegli una foto',
                icon: 'image',
                handler: () => {
                    this.addImage(this.camera.PictureSourceType.PHOTOLIBRARY);
                }
            }
        ];
        const actionSheet = await this.actionSheetCtrl.create({
            header: 'Seleziona immagine',
            buttons
        });
        await actionSheet.present();
    }

    public addImage(sourceType: PictureSourceType): void {
        this.actionSheetCtrl.dismiss();
        this.deviceService.showLoading();
        // Se l'actionsheet non fa dismiss in tempo, ios va in conflitto con l'apertura di camera
        setTimeout(() => {
            this.logger.debug('TrapMaintenanceRemovePage::newPhoto');
            this.detectionService.takePicture(sourceType).then(photo => {
                //this.deviceService.showLoading();
                this.logger.debug('TrapMaintenanceRemovePage::newPhoto::photo', photo);
                photo.missionId = this.trapping.idMissione;
                // Carico la Foto in array
                this.trapping.photos.push(photo);
                // Se la localizzazione va a buon fine aggiorno la foto
                this.geoUtilsService.getCurrentPosition().then((res: Coordinate) => {
                    this.deviceService.hideLoading();
                    this.logger.debug(res);
                    photo.latitude = res.latitudine;
                    photo.longitude = res.longitudine;
                }).catch(err => {
                    this.deviceService.hideLoading()
                    this.deviceService.alert('GET_COORDS_ERROR');
                    this.logger.debug(err);
                });
            }).catch(err => {
                this.deviceService.hideLoading();
                this.logger.debug(err);
            });
        }, 100);
    }

    public saveTrap(): void {
        this.logger.debug('TrapMaintenanceRemovePage::saveTrap');
        if (this.recharge) {
            this.trapping.idOperazione = 3;
        }
        this.trapping.idSpecieVegetale = +(this.trapping.trappola.idSpecieVeg ? this.trapping.trappola.idSpecieVeg : 0);
        this.trapping.latitudine = this.trapping.trappola.latitudine;
        this.trapping.longitudine = this.trapping.trappola.longitudine;
        this.trapping.specie = this.trapping.trappola.specie;

        if (this.trappingInstallation) {
            this.trapping.idIspezioneVisiva = (this.trappingInstallation as Trapping).idIspezioneVisiva;
            this.trapping.idOrganismoNocivo = (this.trappingInstallation as Trapping).idOrganismoNocivo;
        }
        const dataInst = this.trapping.trappola.dataInstallazione ? this.trapping.trappola.dataInstallazione : null;
        // La data installazione, dal BE mi arriva in un modo, la devo salvare in altro formato
        dataInst ? this.trapping.trappola.dataInstallazione = Moment(dataInst).format('DD-MM-YYYY').toString() : this.trapping.trappola.dataInstallazione = null;
        this.trapping.dataTrappolaggio = Moment(new Date()).format('DD-MM-YYYY').toString();
        this.trapping.idOperazione === 4 ? (this.trapping.trappola.dataRimozione = Moment(new Date()).format('DD-MM-YYYY').toString()) : (this.trapping.trappola.dataRimozione = null);

        // In modifica, dal get rest potremmo avere una trappola con dati sporchi
        const trappola = new Trap();
        trappola.idTrappola = this.trapping.trappola.idTrappola;
        trappola.codiceSfr = this.trapping.trappola.codiceSfr;
        trappola.dataInstallazione = this.trapping.trappola.dataInstallazione;
        trappola.dataRimozione = this.trapping.trappola.dataRimozione;
        trappola.idSpecieVeg = this.trapping.trappola.idSpecieVeg;
        trappola.specie = this.trapping.trappola.specie;
        trappola.idTipoTrappola = this.trapping.trappola.idTipoTrappola;
        trappola.latitudine = this.trapping.trappola.latitudine;
        trappola.longitudine = this.trapping.trappola.longitudine;
        trappola.descrTrappola = this.trapping.trappola.descrTrappola;
        trappola.idOrganismoNocivo = this.trapping.idOrganismoNocivo;
        this.trapping.trappola = trappola;
        this.trapping.idIspezioneVisiva = 0;

        // Orario di inizio
        if (this.isNewTrapping) {
            this.trapping.dataOraInizio = Moment().format('HH:mm');
        } else {
            if (this.trapping.dataOraInizio) {
                this.trapping.dataOraInizio = Moment(this.trapping.dataOraInizio).format('HH:mm');
            } else {
                this.trapping.dataOraInizio = Moment().format('HH:mm');
            }
        }

        this.logger.debug(this.trapping);

        this.trapService.saveOrUpdateTrap(this.trapping).then(
            response => {
                trappola.dataInstallazione = dataInst;
                this.trapping.dataOraInizio = response.dataOraInizio;
                this.trapping.dataOraInizioT = response.dataOraInizioT;
                // Aggiorno l'orario di chiusura detection, se detection già chiusa
                if (this.detection.oraFine) {
                    // Se nuovo trappolaggio
                    if (this.isNewTrapping) {
                        this.detection.oraFine = Moment().format('HH:mm');
                        this.detectionService.updateDetectionEndDate({ ...this.detection });
                    }
                }
                if (this.trapping.photos.length == 0) {
                    this.deviceService.alert(this.trapping.idOperazione === 4 ? (this.isNewTrapping ? 'REMOVE_TRAP_SUCCESS' : 'MODIFY_REMOVE_TRAP_SUCCESS') : 'SAVE_TRAP_SUCCESS', {
                        handler: () => {
                            this.setRoot(response);
                        }
                    });
                }
                else {
                    const photoToUpload = this.trapping.photos.filter(p => p.status == FileStatus.TO_SYNCHRONIZE);
                    from(photoToUpload).pipe(
                        mergeMap(photo => {
                            photo.trapId = response.idTrappolaggio;
                            photo.detectionId = response.idRilevazione;
                            // Spostare in Detection Service?...
                            return this.trapService.uploadPhoto(photo);
                        }),
                        toArray()
                    ).subscribe(val => {
                        this.logger.debug(val);
                        this.deviceService.alert(this.trapping.idOperazione === 4 ? (this.isNewTrapping ? 'REMOVE_TRAP_SUCCESS' : 'MODIFY_REMOVE_TRAP_SUCCESS') : 'SAVE_TRAP_SUCCESS', {
                            handler: () => {
                                this.setRoot(response);
                            }
                        });
                    });
                }
            }
        ).catch((err: any) => {
            this.logger.error(err)
            this.deviceService.alert('SAVE_TRAP_ERROR');
        });
    }

    public setRoot(response: any): void {
        if (this.trapping.photos.length > 0) {
            this.trapping.foto = true;
        }
        if (this.visualInspection) {
            this.rootNavController.setRoot(VisualInspectionPage, {
                visualInspection: this.visualInspection,
                detection: this.detection,
                actionRoot: this.actionRoot
            }, {
                animated: true,
                direction: 'back'
            });
        } else {
            this.rootNavController.setRoot(DetectionPage, {
                missionId: response.idMissione,
                detectionId: response.idRilevazione,
                actionRoot: this.actionRoot
            }, {
                animated: true,
                direction: 'back'
            });
        }
    }

    public deleteTrap(): void {
        const self = this;
        this.deviceService.confirm('DELETE_TRAP_CONFIRM', {
            buttons: [
                {
                    text: 'RESET'
                },
                {
                    text: 'CONFIRM',
                    handler: () => {
                        self.trapping.idIspezioneVisiva = 0;
                        self.trapService.deleteTrap(self.trapping)
                            .then(() => {
                                self.deviceService.alert('DELETE_TRAP_SUCCESS', {
                                    handler: () => {
                                        self.rootNavController.setRoot(TrapOpSelectionPage, { detection: self.detection, trapping: this.trapping, missionId: this.missionId }, {
                                            animated: true,
                                            direction: 'back'
                                        });
                                    }
                                });
                            }).catch((err: any) => {
                                self.logger.error(err);
                                self.deviceService.alert('DELETE_TRAP_ERROR');
                            });
                    }
                }]
        });
    }

    public async openAvivModal(): Promise<void> {
        this.logger.debug('TrapMaintenanceRemovePage::openAvivModal');
        this.modal = await this.modalController.create({
            component: AvivModalPage,
            componentProps: {
                codAz: this.detection.cuaa,
                idUte: this.detection.idUte
            }
        });
        return await this.modal.present();
    }
}
