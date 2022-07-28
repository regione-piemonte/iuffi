import { Component, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import {
    SingleInspectionCardComponent,
} from '@app/detections/components/single-inspection-card/single-inspection-card.component';
import { FileStatus } from '@app/detections/dto/photo.dto';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { Detection } from '@app/detections/models/detection.model';
import { Trap } from '@app/detections/models/trap.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { TrapService } from '@app/detections/services/trap.service';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
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
import { HarmfulOrganism, HarmfulOrganismOptions } from '@shared/offline/models/harmful-organism.model';
import { PlantSpeciesDetail } from '@shared/offline/models/plant-species.model';
import { SampleType } from '@shared/offline/models/sample-type.model';
import { TrapType } from '@shared/offline/models/trap-type.model';
import * as Moment from 'moment';
import { from } from 'rxjs';
import { mergeMap, toArray } from 'rxjs/operators';

import { AvivModalPage } from '../aviv-modal/aviv-modal.page';
import { DetectionPage } from '../detection/detection.page';
import { MapModalPage } from '../map-modal/map-modal.page';
import { VisualInspectionPage } from '../visual-inspection/visual-inspection.page';

@Component({
    selector: 'trap-installation',
    templateUrl: './trap-installation.page.html',
    styleUrls: ['./trap-installation.page.scss'],
})
export class TrapInstallationPage extends AutoUnsubscribe {
    public plantSpeciesSelected!: PlantSpeciesDetail;
    public trapTypeSelected!: TrapType;
    public harmfulOrganismsSelected: HarmfulOrganism[] | null = null;
    public sampleTypeSelected!: SampleType;
    public visualInspection: VisualInspection | undefined;
    public detection: Detection;
    public trap: Trapping;
    public isRelatedToEmergency = false;
    public isRelatedToNursery = false;
    public comune: Comune | null;
    public trapForm!: FormGroup;
    public missionId: number;
    public actionRoot: string;
    public organismiFilter = new Array<number>();
    public singleSelection = false;
    public singleHarmfulOrganismSelected: HarmfulOrganismOptions | null = null;
    private modal!: HTMLIonModalElement;
    @ViewChildren(SingleInspectionCardComponent) public singleInspectionCardItems!: QueryList<SingleInspectionCardComponent>;
    constructor(
        private rootNavController: RootNavController,
        private logger: LoggerService,
        public deviceService: DeviceService,
        private navParams: NavParams,
        private detectionService: DetectionService,
        private trapService: TrapService,
        private geoUtilsService: GeoUtilsService,
        private camera: Camera,
        private actionSheetCtrl: ActionSheetController,
        public fileManagerService: FileManagerService,
        public sanitizer: DomSanitizer,
        private formBuilder: FormBuilder,
        private visualInspectionService: VisualInspectionService,
        private modalController: ModalController
    ) {
        super();

        this.detection = this.navParams.get('detection') as Detection;
        this.trap = this.navParams.get('trapping') as Trapping;
        this.visualInspection = this.navParams.get('visualInspection') as VisualInspection;
        this.missionId = this.navParams.get('missionId') as number;
        this.actionRoot = this.navParams.get('actionRoot') as string;
        this.trap.idOperazione = 1;
        this.trapForm = this.createInstallationTrapForm(this.trap);
        this.trap.photos = [];

        !this.missionId ? this.missionId = this.detection.idMissione : undefined;

        if (!this.visualInspection && this.trap.idIspezioneVisiva) {
            const visual = this.visualInspectionService.getVisualInspection(this.trap.idMissione, this.trap.idRilevazione, this.trap.idIspezioneVisiva);
            if (visual) {
                this.visualInspection = visual;
            }
        }

        // Controllo se la trappola è all'interno di una rilevazione relativa ad un vivaio
        if (this.detection.idTipoArea) {
            const currentAreaType: AreaTypeLocal | null = this.detectionService.getAreaType(this.detection.idTipoArea);
            if (currentAreaType) {
                this.isRelatedToNursery = currentAreaType.codiceUfficiale === 'V';
            }
        }

        // Controllo se la trappola è in una rilevazione relativa ad una emergenza
        this.isRelatedToEmergency = this.detection.flagEmergenza === 'S';

        let point: Point;
        if (!this.trap.latitudine && !this.trap.longitudine) {
            this.trap.latitudine = this.trap.trappola.latitudine;
            this.trap.longitudine = this.trap.trappola.longitudine;
        }

        if (ENV.devMode) {
            point = new Point(45.240884, 8.260742);
        }
        else {
            point = new Point(this.trap.latitudine, this.trap.longitudine);
        }
        this.comune = this.geoUtilsService.getComuneByPoint(point);
        this.trap.istatComune = this.comune ? this.comune.istat : '000000';
        this.trap.idMissione = this.detection.idMissione;

        if (this.trap.idSpecieVegetale || this.trap.trappola.idSpecieVeg) {
            !this.trap.idSpecieVegetale ? this.trap.idSpecieVegetale = (this.trap.trappola.idSpecieVeg ? this.trap.trappola.idSpecieVeg : 0) : 0;
            this.plantSpeciesSelected = this.trapService.getPlantSpeciesDetail(this.trap.idSpecieVegetale) as PlantSpeciesDetail;
            if (this.trap.trappola.idTipoTrappola) {
                this.trapTypeSelected = new TrapType();
                this.trapTypeSelected.idTipoTrappola = this.trap.trappola.idTipoTrappola;
                this.trapTypeSelected.tipologiaTrappola = this.trap.descrTrappola;
            }
        }
        if (this.deviceService.isCordova()) {
            this.geoUtilsService.configGpsTracking();
        }

        // Controllo se ci sono delle foto associate al trappolaggio
        if (this.trap.foto && this.trap.idTrappolaggio && this.trap.idTrappolaggio !== 0) {
            this.trap.photos = this.trapService.getTrapPhotos(this.trap.idTrappolaggio);
        }

        if (this.trap.idOrganismoNocivo) {
            this.harmfulOrganismsSelected = new Array<HarmfulOrganism>();
            const on = this.detectionService.getHarmfulOrganism(this.trap.idOrganismoNocivo);
            if (on) {
                this.harmfulOrganismsSelected.push(on);
            }
        }

        // Trap da ispezione visiva: eredito gli ON selezionabili
        if (this.trap.idIspezioneVisiva && this.visualInspection && this.visualInspection.organismi) {
            this.singleSelection = true;
            this.harmfulOrganismsSelected = new Array<HarmfulOrganismOptions>();
            this.visualInspection.organismi.map(on => {
                const harmfulOrganism = this.detectionService.getHarmfulOrganism(on.idSpecieOn);
                if (harmfulOrganism && this.harmfulOrganismsSelected) {
                    const harmfulOption = new HarmfulOrganismOptions(harmfulOrganism);
                    this.harmfulOrganismsSelected.push(harmfulOption);
                    if (this.trap.idOrganismoNocivo === on.idSpecieOn) {
                        harmfulOption.selected = true;
                        this.singleHarmfulOrganismSelected = harmfulOption;
                    } else if ((this.visualInspection as VisualInspection).organismi.length === 1) {
                        // se i selezionabili sono uno, lo seleziono automaticamente
                        this.singleHarmfulOrganismSelected = harmfulOption;
                    }
                }
            });
        }

    }

    public async openMapModal(): Promise<void> {
        this.logger.debug('TrapMaintenanceRemovePage::openMapModal');
        this.modal = await this.modalController.create({
            component: MapModalPage,
            componentProps: {
                currentPosition: new Coordinate(this.trap.latitudine, this.trap.longitudine)
            }
        });

        this.modal.onWillDismiss().then((value: any) => {
            this.logger.debug('VisualInspectionPage::openMapModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
                const point = new Point((value.data as Coordinate).latitudine, (value.data as Coordinate).longitudine);
                this.comune = this.geoUtilsService.getComuneByPoint(point);
                this.trap.istatComune = this.comune ? this.comune.istat : '000000';
                this.trap.latitudine = (value.data as Coordinate).latitudine;
                this.trap.longitudine = (value.data as Coordinate).longitudine;

            }
        });
        return await this.modal.present();
    }

    public createInstallationTrapForm(trap: Trapping): FormGroup {
        const trapForm = this.formBuilder.group({
            codiceSfr: [
                trap.trappola.codiceSfr,
                Validators.compose([
                    Validators.required,
                ])
            ]
        });

        return trapForm;
    }

    public onHarmfulOrganismSelected(value: HarmfulOrganism[]): void {
        this.harmfulOrganismsSelected = value;
        if (value && value.length > 0) {
            value.forEach(element => {
                this.trap.idOrganismoNocivo = element.idOrganismoNocivo;
            });
        }
    }

    public isValid(): boolean {
        return this.trapForm.valid;
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
            this.logger.debug('TrapPage::newPhoto');
            this.detectionService.takePicture(sourceType).then(photo => {
                //this.deviceService.showLoading();
                this.logger.debug('TrapPage::newPhoto::photo', photo);
                photo.missionId = this.trap.idMissione;
                // Carico la Foto in array
                this.trap.photos.push(photo);
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

    public onPlantSpeciesSelected(value: any): void {
        this.plantSpeciesSelected = value;
        this.trap.idSpecieVegetale = this.plantSpeciesSelected?.idSpecieVegetale
    }

    public onTrapTypeSelected(value: any): void {
        this.trapTypeSelected = value;
    }

    public saveTrap(): void {
        if (!this.trapForm.controls.codiceSfr.value) {
            this.deviceService.alert('Devi specificare un Codice Trappola');
        } else if (this.isValid()) {
            this.logger.debug('TrapInstallationPage::saveTrap');
            let valid = true;
            let errorMessage = '';
            if (valid && !this.trap.idSpecieVegetale) {
                errorMessage = 'Devi selezionare una Specie vegetale';
                valid = false;
            }
            if (valid && !this.singleSelection && !(this.harmfulOrganismsSelected && this.harmfulOrganismsSelected.length > 0)) {
                errorMessage = 'Devi selezionare un organismo nocivo';
                valid = false;
            }
            if (valid && this.singleSelection && !this.singleHarmfulOrganismSelected) {
                errorMessage = 'Devi selezionare un organismo nocivo';
                valid = false;
            }
            if (valid && !this.trapTypeSelected) {
                errorMessage = 'Devi selezionare una tipologia di Trappola';
                valid = false;
            }

            if (valid) {
                const idTrappola = this.trap.trappola ? this.trap.trappola.idTrappola : 0;
                this.trap.trappola = new Trap();
                this.trap.trappola.idTrappola = idTrappola;
                if (this.singleSelection && this.singleHarmfulOrganismSelected) {
                    this.trap.idOrganismoNocivo = this.singleHarmfulOrganismSelected?.idOrganismoNocivo;
                }
                this.trap.idSpecieVegetale = this.plantSpeciesSelected.idSpecieVegetale;
                this.trap.trappola.idSpecieVeg = this.plantSpeciesSelected.idSpecieVegetale;
                this.trap.trappola.codiceSfr = this.trapForm.controls.codiceSfr.value;
                this.trap.trappola.idTipoTrappola = this.trapTypeSelected.idTipoTrappola;
                this.trap.trappola.latitudine = this.trap.latitudine;
                this.trap.trappola.longitudine = this.trap.longitudine;
                this.trap.trappola.idTrappola = this.trap.trappola.idTrappola ? this.trap.trappola.idTrappola : 0;
                this.trap.trappola.dataInstallazione = Moment(new Date()).format('DD-MM-YYYY').toString();
                this.trap.dataTrappolaggio = this.trap.trappola.dataInstallazione;
                this.logger.debug(this.trap);
                // this.rootNavController.push(TrapInstallationConfirmPage, {
                //     detection: this.detection,
                //     trap: this.trap,
                //     visualInspection: this.visualInspection,
                //     trapTypeDescription: this.trapTypeSelected.tipologiaTrappola,
                //     actionRoot: this.actionRoot
                // });
                this.saveConfirmTrap();
            }
            else {
                this.deviceService.alert(errorMessage);
            }
        }
    }

    public saveConfirmTrap(): void {
        this.logger.debug('TrapConfirmPage::saveTrap');
        this.trap.specie = this.plantSpeciesSelected.nomeVolgare;
        this.trap.descrTrappola = this.trapTypeSelected.tipologiaTrappola;
        this.logger.debug(this.trap);
        // Orario di inizio
        this.trap.dataOraInizio = Moment().format('HH:mm');
        this.trapService.saveOrUpdateTrap(this.trap).then(
            response => {
                // Aggiorno l'orario di chiusura detection, se detection già chiusa
                if (this.detection.oraFine) {
                    // Se nuovo trappolaggio
                    if (this.trap.idTrappolaggio === 0) {
                        this.detection.oraFine = Moment().format('HH:mm');
                        this.detectionService.updateDetectionEndDate({ ...this.detection });
                    }
                }
                this.trap.dataOraInizio = response.dataOraInizio;
                this.trap.dataOraInizioT = response.dataOraInizioT;
                if (response.trappola.idTrappola) {
                    this.trap.trappola.idTrappola = response.trappola.idTrappola;
                }
                if (this.trap.photos.length == 0) {
                    this.deviceService.alert(this.trap.idOperazione === 4 ? 'REMOVE_TRAP_SUCCESS' : 'SAVE_TRAP_SUCCESS', {
                        handler: () => {
                            this.setRoot(response);
                        }
                    });
                }
                else {
                    const photoToUpload = this.trap.photos.filter(p => p.status == FileStatus.TO_SYNCHRONIZE);
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
                        this.deviceService.alert(this.trap.idOperazione === 4 ? 'REMOVE_TRAP_SUCCESS' : 'SAVE_TRAP_SUCCESS', {
                            handler: () => {
                                this.setRoot(response);
                            }
                        });
                    });
                }
            }
        ).catch((err: any) => {
            this.logger.error(err)
            if (err && err.code) {
                if (err.code == 1) {
                    this.deviceService.alert('TRAP_CODE_ERROR');
                }
            }
            else {
                this.deviceService.alert('SAVE_TRAP_ERROR');
            }
        });

    }

    public setRoot(response: any): void {
        if (this.trap.photos.length > 0) {
            this.trap.foto = true;
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
                        self.trapService.deleteTrap(self.trap)
                            .then(() => {
                                self.deviceService.alert('DELETE_TRAP_SUCCESS', {
                                    handler: () => {
                                        if (!this.visualInspection && this.trap.idIspezioneVisiva) {
                                            const visual = this.visualInspectionService.getVisualInspection(this.trap.idMissione, this.trap.idRilevazione, this.trap.idIspezioneVisiva);
                                            if (visual) {
                                                this.visualInspection = visual;
                                            }
                                        }
                                        self.rootNavController.setRoot(
                                            VisualInspectionPage, { visualInspection: self.visualInspection, detection: self.detection }, { animated: true, direction: 'back' });
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
        this.logger.debug('TrapInstallationPage::openAvivModal');
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
