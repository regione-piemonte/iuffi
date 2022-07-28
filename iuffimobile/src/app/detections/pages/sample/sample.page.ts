import { Component, QueryList, ViewChildren } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import {
    SingleInspectionCardComponent,
} from '@app/detections/components/single-inspection-card/single-inspection-card.component';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { Detection } from '@app/detections/models/detection.model';
import { PlantSpeciesAviv } from '@app/detections/models/plant-species-aviv.model';
import { AvivRegistry, BusinessCenter } from '@app/detections/models/registry-aviv.model';
import { Sample } from '@app/detections/models/sample.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { SampleService } from '@app/detections/services/sample.service';
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
import { HarmfulOrganismOptions } from '@shared/offline/models/harmful-organism.model';
import { PlantSpeciesDetail } from '@shared/offline/models/plant-species.model';
import { SampleType } from '@shared/offline/models/sample-type.model';
import * as Moment from 'moment';

import { AvivModalPage } from '../aviv-modal/aviv-modal.page';
import { AvivPlantSpeciesModalPage } from '../aviv-plant-species-modal/aviv-plant-species-modal.page';
import { AvivServiceModalPage } from '../aviv-service-modal/aviv-service-modal.page';
import { DetectionPage } from '../detection/detection.page';
import { MapModalPage } from '../map-modal/map-modal.page';
import { SampleConfirmPage } from '../sample-confirm/sample-confirm.page';
import { VisualInspectionPage } from '../visual-inspection/visual-inspection.page';

@Component({
    selector: 'sample',
    templateUrl: './sample.page.html',
    styleUrls: ['./sample.page.scss'],
})
export class SamplePage extends AutoUnsubscribe {
    private modal!: HTMLIonModalElement;
    public plantSpeciesSelected!: PlantSpeciesDetail;
    public harmfulOrganismsSelected: HarmfulOrganismOptions[] | null = null;
    public sampleTypeSelected: SampleType | undefined;
    public detection: Detection;
    public sample: Sample;
    public visualInspection: VisualInspection | undefined;
    public isRelatedToEmergency = false;
    public isRelatedToNursery = false;
    public comune: Comune | null;
    public avivRegistry: AvivRegistry | null = null;
    public businessCenter: BusinessCenter | null = null;
    public codAz = '';
    public actionRoot: string;
    @ViewChildren(SingleInspectionCardComponent) public singleInspectionCardItems!: QueryList<SingleInspectionCardComponent>;
    constructor(
        private rootNavController: RootNavController,
        private logger: LoggerService,
        public deviceService: DeviceService,
        private navParams: NavParams,
        private detectionService: DetectionService,
        private sampleService: SampleService,
        private modalController: ModalController,
        private geoUtilsService: GeoUtilsService,
        private camera: Camera,
        private actionSheetCtrl: ActionSheetController,
        public fileManagerService: FileManagerService,
        public sanitizer: DomSanitizer,
        private visualInspectionService: VisualInspectionService
    ) {
        super();
        this.detection = this.navParams.get('detection') as Detection;
        this.sample = this.navParams.get('sample') as Sample;
        this.visualInspection = this.navParams.get('visualInspection') as VisualInspection;
        this.actionRoot = this.navParams.get('actionRoot') as string;
        this.sample.photos = [];

        // Controllo se il campionamento è all'interno di una rilevazione relativa ad un vivaio
        if (this.detection.idTipoArea) {
            const currentAreaType: AreaTypeLocal | null = this.detectionService.getAreaType(this.detection.idTipoArea);
            if (currentAreaType) {
                this.isRelatedToNursery = currentAreaType.codiceUfficiale === 'V';
            }
        }

        // Controllo se l'ispezione visiva è all'interno di una rilevazione relativa ad una emergenza
        this.isRelatedToEmergency = this.detection.flagEmergenza === 'S';

        let point: Point;
        if (ENV.devMode) {
            point = new Point(45.240884, 8.260742);
        }
        else {
            point = new Point(this.sample.latitudine, this.sample.longitudine);
        }
        this.comune = this.geoUtilsService.getComuneByPoint(point);
        this.sample.comune = this.comune ? this.comune.nome : '';
        this.sample.istatComune = this.comune ? this.comune.istat : '000000';
        this.sample.idMissione = this.detection.idMissione;

        if (this.sample.idIspezioneVisiva && !this.visualInspection) {
            this.visualInspection = this.visualInspectionService.getVisualInspection(this.sample.idMissione, this.detection.idRilevazione, this.sample.idIspezioneVisiva);
        }

        // Controllo se ci sono delle foto associate al campionamento
        if (this.sample.foto && this.sample.idCampionamento && this.sample.idCampionamento !== 0) {
            this.sample.photos = this.sampleService.getSamplePhotos(this.sample.idCampionamento);
        }

        // La label deve rimanere SAVE se ho la specie vegetale ma vengo da campionamento di ispezione visiva
        if (this.sample.idSpecieVegetale) {
            this.plantSpeciesSelected = this.sampleService.getPlantSpeciesDetail(this.sample.idSpecieVegetale) as PlantSpeciesDetail;
        }
        if (this.sample.idTipoCampione) {
            this.sampleTypeSelected = {
                idTipoCampione: this.sample.idTipoCampione,
                tipologiaCampione: this.sample.tipoCampione,
                dataFineValiditaF: '',
                dataInizioValiditaF: ''
            }
        }
        if (this.deviceService.isCordova()) {
            this.geoUtilsService.configGpsTracking();
        }

        if (this.visualInspection && this.visualInspection.organismi) {
            this.sample.organismiNocivi = new Array<number>();
            this.visualInspection.organismi.forEach(on => {
                //if (on.flagTrovato) {
                this.sample.organismiNocivi.push(on.idSpecieOn);
                //}
            });
        }

        if (this.sample.organismiNocivi && this.sample.organismiNocivi.length > 0) {
            this.harmfulOrganismsSelected = new Array<HarmfulOrganismOptions>();
            this.sample.organismiNocivi.map(id => {
                const harmfulOrganism = this.detectionService.getHarmfulOrganism(id);
                if (harmfulOrganism && this.harmfulOrganismsSelected) {
                    const harmfulOption = new HarmfulOrganismOptions(harmfulOrganism);
                    harmfulOption.selected = true;
                    this.harmfulOrganismsSelected.push(harmfulOption);
                }
            })
        }

        // Formattazioni di orario
        if (this.sample.dataOraInizio && !this.sample.oraInizio) {
            this.sample.oraInizio = Moment(this.sample.dataOraInizio).format('HH:mm');
        }
        if (this.sample.dataOraFine && !this.sample.oraFine) {
            this.sample.oraFine = Moment(this.sample.dataOraFine).format('HH:mm');
        }

    }

    ionViewDidEnter(): void {
        // Effettuo un presalvataggio ed ottengo l'id del campionamento
        if (!this.sample.idCampionamento && this.deviceService.isOnline()) {
            this.deviceService.showLoading();
            this.sample.oraInizio = Moment().format('HH:mm');
            this.sampleService.saveOrUpdateSample(this.sample, this.detection).then(sampleResponse => {
                this.sample.idCampionamento = sampleResponse.idCampionamento;
                this.sample.dataOraInizio = sampleResponse.dataOraInizio;
                setTimeout(() => {
                    this.deviceService.hideLoading();
                }, 100);

            }
            );
        }
    }

    public async openMapModal(): Promise<void> {
        this.logger.debug('VisualInspectionPage::openMapModal');
        this.modal = await this.modalController.create({
            component: MapModalPage,
            componentProps: {
                currentPosition: new Coordinate(this.sample.latitudine, this.sample.longitudine)
            }
        });

        this.modal.onWillDismiss().then((value: any) => {
            this.logger.debug('VisualInspectionPage::openMapModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
                const point = new Point((value.data as Coordinate).latitudine, (value.data as Coordinate).longitudine);
                this.comune = this.geoUtilsService.getComuneByPoint(point);
                this.sample.comune = this.comune ? this.comune.nome : '';
                this.sample.istatComune = this.comune ? this.comune.istat : '000000';
                this.sample.latitudine = (value.data as Coordinate).latitudine;
                this.sample.longitudine = (value.data as Coordinate).longitudine;

            }
        });
        return await this.modal.present();
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
            this.logger.debug('SamplePage::newPhoto');
            this.detectionService.takePicture(sourceType).then(photo => {
                //this.deviceService.showLoading();
                this.logger.debug('SamplePage::newPhoto::photo', photo);
                photo.missionId = this.sample.idMissione;
                // Carico la Foto in array
                this.sample.photos.push(photo);
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

    public selectPlantSpeciesAviv(): void {
        this.logger.debug('SamplePage::selectPlantSpeciesAviv');
        this.deviceService.showLoading();
        this.sampleService.getPlantSpeciesAviv(this.codAz).then(
            list => {
                this.deviceService.hideLoading();
                this.openPlantSpeciesAvivModal(list);
            }
        ).catch((err: any) => {
            this.deviceService.hideLoading();
            this.logger.debug(err);
        });

    }
    public async openPlantSpeciesAvivModal(list: PlantSpeciesAviv[]): Promise<void> {
        this.logger.debug('SamplePage::openPlantSpeciesAvivModal');
        this.modal = await this.modalController.create({
            component: AvivPlantSpeciesModalPage,
            componentProps: {
                plantSpecies: list
            }
        });

        this.modal.onWillDismiss().then(value => {
            this.logger.debug('SamplePage::openAvivServiceModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
            }
        });

        return await this.modal.present();
    }

    public organismFounded(item: HarmfulOrganismOptions): void {
        this.logger.debug('SamplePage::organismFounded');
        this.logger.debug(item);
        this.sample.organismiNocivi = this.sample.organismiNocivi || [];
        // Check, aggiunge organismo solo se non è già in lista
        const foundItem = this.sample.organismiNocivi.find(el => { return el === item.idOrganismoNocivo });
        if (item.selected && !foundItem) {
            this.sample.organismiNocivi.push(item.idOrganismoNocivo);
        }
        else if (!item.selected && foundItem) {
            const index = this.sample.organismiNocivi.findIndex(d => {
                return d === item.idOrganismoNocivo
            });
            if (index > -1) {
                this.sample.organismiNocivi.splice(index, 1);
            }
        }
        this.logger.debug(this.sample.organismiNocivi);
    }

    public onPlantSpeciesSelected(value: any): void {
        // Se la specie vegetale selezionata è diversa da quella attuale devo azzerare
        // l'elenco degli ON selezionati per obbligare la riselezione in faso di salvataggio
        if (value.idSpecieVegetale !== this.sample.idSpecieVegetale) {
            this.harmfulOrganismsSelected = [];
            this.sample.organismiNocivi = [];
            // Devo resettare anche il tipo di campione
            this.sampleTypeSelected = undefined;
            this.sample.idTipoCampione = 0;
            this.sample.tipoCampione = '';
        }
        this.plantSpeciesSelected = value;
        this.sample.idSpecieVegetale = this.plantSpeciesSelected?.idSpecieVegetale;
    }

    public onHarmfulOrganismSelected(value: HarmfulOrganismOptions[]): void {
        this.harmfulOrganismsSelected = value;
        this.sample.organismiNocivi = [];
        if (value && value.length > 0) {
            value.forEach(element => {
                this.sample.organismiNocivi.push(element.idOrganismoNocivo);
            });
        }
    }

    public onSampleTypeSelected(value: any): void {
        this.sampleTypeSelected = value;
        this.sample.idTipoCampione = value.idTipoCampione;
        this.sample.tipoCampione = value.tipologiaCampione;
    }
    public async openAvivServiceModal(): Promise<void> {
        this.logger.debug('SamplePage::openAvivServiceModal');
        this.modal = await this.modalController.create({
            component: AvivServiceModalPage
        });

        this.modal.onWillDismiss().then(value => {
            this.logger.debug('SamplePage::openAvivServiceModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
                this.codAz = value.data.codAz;
                this.avivRegistry = value.data.avivRegistry;
                this.businessCenter = value.data.businessCenter;
                this.sample.cuaa = this.codAz;
            }
        });

        return await this.modal.present();
    }

    public saveSample(): void {
        this.logger.debug('SamplePage::saveSample');
        this.sample.idAnfi = 1;
        this.sample.presenza = 'S';
        this.logger.debug(this.sample);
        let valid = true;
        let errorMessage = '';
        if (valid && !this.sample.idSpecieVegetale) {
            errorMessage = 'Devi selezionare una Specie vegetale';
            valid = false;
        }

        if (valid && this.sample.organismiNocivi && this.sample.organismiNocivi.length === 0) {
            errorMessage = 'Devi selezionare almeno un organismo nocivo';
            valid = false;
        }

        if (valid && !this.sampleTypeSelected) {
            errorMessage = 'Devi selezionare la tipologia di campionamento';
            valid = false;
        }

        if (valid) {
            // Non ho la visual?
            if (!this.visualInspection && this.sample.idIspezioneVisiva) {
                this.visualInspection = this.visualInspectionService.getVisualInspection(this.detection.idMissione, this.detection.idRilevazione, this.sample.idIspezioneVisiva);
            }
            this.rootNavController.push(SampleConfirmPage, {
                detection: this.detection,
                sample: this.sample,
                visualInspection: this.visualInspection,
                actionRoot: this.actionRoot
            });
        }
        else {
            this.deviceService.alert(errorMessage);
        }
    }

    public deleteSample(): void {
        const self = this;
        this.deviceService.confirm('DELETE_SAMPLE_CONFIRM', {
            buttons: [
                {
                    text: 'RESET'
                },
                {
                    text: 'CONFIRM',
                    handler: () => {
                        self.sampleService.deleteSample(self.sample)
                            .then(() => {
                                self.deviceService.alert('DELETE_SAMPLE_SUCCESS', {
                                    handler: () => {
                                        if (this.visualInspection) {
                                            self.rootNavController.setRoot(VisualInspectionPage, { visualInspection: this.visualInspection, detection: this.detection }, { animated: true, direction: 'back' });
                                        }
                                        else {
                                            self.rootNavController.setRoot(DetectionPage, { missionId: self.sample.idMissione, detectionId: self.sample.idRilevazione }, {
                                                animated: true,
                                                direction: 'back'
                                            });
                                        }
                                    }
                                });
                            }).catch((err: any) => {
                                self.logger.error(err);
                                self.deviceService.alert('DELETE_SAMPLE_ERROR');
                            });
                    }
                }]
        });
    }

    public async openAvivModal(): Promise<void> {
        this.logger.debug('SamplePage::openAvivModal');
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
