import { Component, QueryList, ViewChildren } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { FileStatus } from '@app/detections/dto/photo.dto';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { Detection } from '@app/detections/models/detection.model';
import { AvivRegistry } from '@app/detections/models/registry-aviv.model';
import { Sample } from '@app/detections/models/sample.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
import { LoggerService } from '@core/logger';
import { AutoUnsubscribe } from '@core/utils';
import { ENV } from '@env';
import { Camera, PictureSourceType } from '@ionic-native/camera/ngx';
import { ActionSheetController, ModalController, NavParams, ToastController } from '@ionic/angular';
import { TranslateService } from '@ngx-translate/core';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { GeoUtilsService } from '@shared/geo-utils/geo-utils.service';
import { Comune } from '@shared/geo-utils/model/comune.model';
import { Point } from '@shared/geo-utils/model/point.model';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';
import { StringUtil } from '@shared/utils/string.util';
import * as Moment from 'moment';
import { from } from 'rxjs';
import { mergeMap, takeUntil, toArray } from 'rxjs/operators';

import { DeviceService } from '../../../core/device/services/device.service';
import { RootNavController } from '../../../core/split-view/services/root-nav.controller';
import { AvivModalPage } from '../aviv-modal/aviv-modal.page';
import { DetectionPage } from '../detection/detection.page';
import { MapModalPage } from '../map-modal/map-modal.page';
import { SamplePage } from '../sample/sample.page';
import { TrapInstallationPage } from '../trap-installation/trap-installation.page';
import { HarmfulOrganism, HarmfulOrganismOptions } from './../../../shared/offline/models/harmful-organism.model';
import { PlantSpeciesDetail } from './../../../shared/offline/models/plant-species.model';
import { SingleInspectionCardComponent } from './../../components/single-inspection-card/single-inspection-card.component';
import { BusinessCenter } from './../../models/registry-aviv.model';
import { Inspection } from './../../models/visual-inspection.model';
import { VisualInspectionConfirmPage } from './../visual-inspection-confirm/visual-inspection-confirm.page';

@Component({
    selector: 'visual-inspection',
    templateUrl: 'visual-inspection.page.html',
    styleUrls: ['visual-inspection.page.scss']
})
export class VisualInspectionPage extends AutoUnsubscribe {
    private modal!: HTMLIonModalElement;
    public plantSpeciesSelected!: PlantSpeciesDetail;
    public harmfulOrganismsAdded: HarmfulOrganismOptions[] = [];
    public harmfulOrganismsSelected = false;
    public detection: Detection;
    public visualInspection: VisualInspection;
    public inspections: Inspection[] = [];
    public isRelatedToEmergency = false;
    public isRelatedToNursery = false;
    public isGPSTrackingStarted = false;
    public quantity = 1;
    public inspectedArea = 0;
    public inspectedAreaPoints: Point[] = [];
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
        private visualInspectionService: VisualInspectionService,
        private modalController: ModalController,
        private geoUtilsService: GeoUtilsService,
        private actionSheetCtrl: ActionSheetController,
        public fileManagerService: FileManagerService,
        private toastController: ToastController,
        private translateService: TranslateService,
        private camera: Camera,
        public sanitizer: DomSanitizer
    ) {
        super();
        this.detection = this.navParams.get('detection') as Detection;
        this.visualInspection = this.navParams.get('visualInspection') as VisualInspection;
        if (this.visualInspection.ispezioni.length > 0) {
            this.visualInspection.ispezioni = [...this.visualInspection.ispezioni];
        }
        this.actionRoot = this.navParams.get('actionRoot') as string;
        this.logger.debug('VisualInspectionPage::visualInspection ', this.visualInspection);

        // Controllo se l'ispezione visiva è all'interno di una rilevazione relativa ad un vivaio
        if (this.detection.idTipoArea) {
            const currentAreaType: AreaTypeLocal | null = this.detectionService.getAreaType(this.detection.idTipoArea);
            if (currentAreaType) {
                this.isRelatedToNursery = currentAreaType.codiceUfficiale === 'V';
                this.codAz = this.detection.cuaa;
            }
        }

        // Controllo se l'ispezione visiva è all'interno di una rilevazione relativa ad una emergenza
        this.isRelatedToEmergency = this.detection.flagEmergenza === 'S';

        let point: Point;
        if (ENV.devMode) {
            point = new Point(45.240884, 8.260742);
        }
        else {
            point = new Point(this.visualInspection.latitudine, this.visualInspection.longitudine);
        }
        this.comune = this.geoUtilsService.getComuneByPoint(point);
        this.visualInspection.comune = this.comune ? this.comune.nome : '';
        this.visualInspection.istatComune = this.comune ? this.comune.istat : '000000';

        if (this.visualInspection.idSpecieVegetale) {
            this.plantSpeciesSelected = this.detectionService.getPlantSpeciesDetail(this.visualInspection.idSpecieVegetale) as PlantSpeciesDetail;
        }
        if (this.deviceService.isCordova()) {
            this.geoUtilsService.configGpsTracking();
        }

        if (this.visualInspection.organismi && this.visualInspection.organismi.length > 0) {
            this.harmfulOrganismsAdded = [];
            this.visualInspection.organismi.map(organismo => {
                const harmfulOrganism = this.detectionService.getHarmfulOrganism(organismo.idSpecieOn);
                if (harmfulOrganism) {
                    const harmfulOrganismOption = new HarmfulOrganismOptions(harmfulOrganism);
                    if (organismo.flagTrovato == 'S' || organismo.flagTrovato == true) {
                        harmfulOrganismOption.selected = true;
                        organismo.flagTrovato = true;
                    } else {
                        harmfulOrganismOption.selected = false;
                        organismo.flagTrovato = false;
                    }
                    this.harmfulOrganismsAdded.push(harmfulOrganismOption);
                }
            });
        }

        this.geoUtilsService.onSendLocation$
            .pipe(takeUntil(this.destroy$))
            .subscribe(point => {
                if (point) {
                    this.logger.debug('VisualInspectionPage::onSendLocation$::point ', point);
                    this.inspectedAreaPoints.push(point);
                    this.logger.debug('VisualInspectionPage::inspectedAreaPoints ', this.inspectedAreaPoints);
                }
            });

        // Controllo se ci sono delle foto associate all'ispezione visiva
        if ((this.visualInspection && this.visualInspection.foto)) {
            this.visualInspection.photos = this.visualInspectionService.getVisualInspectionPhotos(this.visualInspection.idIspezione);
        }

        // Formattazioni di orario
        if (!this.visualInspection.dataOraInizio) {
            this.visualInspection.dataOraInizio = new Date().getTime();
            this.visualInspection.oraInizio = Moment().format('HH:mm');
        } else {
            this.visualInspection.oraInizio = Moment(this.visualInspection.dataOraInizio).format('HH:mm');
        }
        if (this.visualInspection.dataOraFine && !this.visualInspection.oraFine) {
            this.visualInspection.oraFine = Moment(this.visualInspection.dataOraFine).format('HH:mm');
        }

    }

    public backToDetection(): void {
        this.logger.debug('VisualInspectionSelectionPage::backToDetection');
        this.rootNavController.setRoot(DetectionPage, {
            detection: this.detection,
            missionId: this.detection.idMissione,
            detectionId: this.detection.idRilevazione,
            actionRoot: this.actionRoot
        }, {
            animated: true,
            direction: 'back',
        });
    }

    public async presentToast(message: string): Promise<void> {
        const toast = await this.toastController.create({
            message: this.translateService.instant(message),
            position: 'bottom',
            duration: 3000
        });
        toast.present();
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
            this.logger.debug('VisualInspectionPage::newPhoto');
            this.detectionService.takePicture(sourceType).then(photo => {
                //this.deviceService.showLoading();
                this.logger.debug('VisualInspectionPage::newPhoto::photo', photo);
                photo.missionId = this.visualInspection.idMissione;
                // Carico la Foto in array
                this.visualInspection.photos.push(photo);
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
                //this.deviceService.hideLoading();
            }).catch(err => {
                this.deviceService.hideLoading();
                this.logger.debug(err);
            });
        }, 100);
    }

    public addInspection(): void {
        const MY_REGEXP = /^\d+$/;
        const regExp = new RegExp(MY_REGEXP);

        if (!regExp.test(this.quantity + '')) {
            this.deviceService.alert('Possono essere inseriti solo valori interi', {
                handler: () => {
                    this.quantity = 1;
                }
            })
        }
        else {
            this.deviceService.showLoading();
            const inspection: Inspection = new Inspection()
            inspection.quantita = this.quantity;
            this.visualInspection.numeroPiante += this.quantity;
            this.geoUtilsService.getCurrentPosition().then((res: Coordinate) => {
                this.logger.debug(res);
                inspection.coordinate = res;
                this.visualInspection.ispezioni.unshift(inspection);
                setTimeout(() => { this.deviceService.hideLoading() }, 100);
            }).catch(err => {
                inspection.coordinate = new Coordinate();
                this.deviceService.alert('GET_COORDS_ERROR', {
                    handler: () => {
                        this.visualInspection.ispezioni.unshift(inspection);
                    }
                })
                setTimeout(() => { this.deviceService.hideLoading() }, 100);
                this.logger.debug(err);
            });
        }
    }

    public deleteInspection(index: number): void {
        const self = this;
        this.deviceService.confirm('DELETE_SINGLE_VISUALINSPECTION_CONFIRM', {
            buttons: [
                {
                    text: 'RESET'
                },
                {
                    text: 'CONFIRM',
                    handler: () => {
                        self.visualInspection.ispezioni.splice(index, 1);
                    }
                }]
        });
    }

    public addInspectedArea(): void {
        this.visualInspection.superficie = this.inspectedArea
    }

    public async openMapModal(): Promise<void> {
        this.logger.debug('VisualInspectionPage::openMapModal');
        this.modal = await this.modalController.create({
            component: MapModalPage,
            componentProps: {
                currentPosition: new Coordinate(this.visualInspection.latitudine, this.visualInspection.longitudine)
            }
        });

        this.modal.onWillDismiss().then((value: any) => {
            this.logger.debug('VisualInspectionPage::openMapModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
                const point = new Point((value.data as Coordinate).latitudine, (value.data as Coordinate).longitudine);
                this.comune = this.geoUtilsService.getComuneByPoint(point);
                this.visualInspection.comune = this.comune ? this.comune.nome : '';
                this.visualInspection.istatComune = this.comune ? this.comune.istat : '000000';
                this.visualInspection.latitudine = (value.data as Coordinate).latitudine;
                this.visualInspection.longitudine = (value.data as Coordinate).longitudine;

            }
        });
        return await this.modal.present();
    }

    public manageGPSTracking(): void {
        const self = this;
        if (this.isGPSTrackingStarted) {
            this.stopGPSTracking();
        }
        else {
            this.deviceService.confirm('TRACKING_GPS_CONFIRM', {
                buttons: [
                    {
                        text: 'RESET'
                    },
                    {
                        text: 'CONFIRM',
                        handler: () => {
                            self.startGPSTracking();
                        }
                    }]
            });
        }
    }

    public startGPSTracking(): void {
        this.inspectedAreaPoints = [];
        this.isGPSTrackingStarted = true;
        if (this.deviceService.isIos()) {
            this.geoUtilsService.startIosGpsTracking();
        }
        else {
            this.geoUtilsService.startAndroidGpsTracking()
        }
        // Salvaguardia di stop tracking dopo 2 ore
        setTimeout(() => {
            try {
                this.stopGPSTracking();
            }
            catch {  }
        }, 60000*120);
    }

    public stopGPSTracking(): void {
        if (this.deviceService.isIos()) {
            this.geoUtilsService.stopIosGpsTracking();
        }
        else {
            this.geoUtilsService.stopAndroidGpsTracking();
        }
        this.isGPSTrackingStarted = false;
        this.visualInspection.superficie = Math.round(this.geoUtilsService.getConvexPolygonArea(this.inspectedAreaPoints));
    }

    public onPlantSpeciesSelected(value: any): void {
        this.plantSpeciesSelected = value;
        this.visualInspection.idSpecieVegetale = this.plantSpeciesSelected?.idSpecieVegetale;

        // Se cambio la specie vegetale, devo svuotare gli ON, che sono sempre legati alla specie selezionata
        this.visualInspection.organismi = []; // Trovati
        this.harmfulOrganismsAdded = []; // Cercati

        // Preselezione ON
        const lists = this.detectionService.getHarmfulOrganisms(this.visualInspection.idSpecieVegetale);
        // Lista con tutti gli on
        const harmfulOrganisms = this._convertToOptions(lists.on);
        // Lista con on filtrati, primo tentativo per compatibilità, secondo tentativo per validità
        if (lists.onFilteredCompatibilita.length > 0) {
            this.harmfulOrganismsAdded = this._convertToOptions(lists.onFilteredCompatibilita);
        } else if (lists.onFilteredCompatibilita.length === 0) {
            // Se la lista di on non ha filtri, non effettuo la preselezione, non posso selezionare tutti gli on
            this.harmfulOrganismsAdded = [];
            this.visualInspection.organismi = []
        }
        if (this.harmfulOrganismsAdded.length > 0) {
            this.harmfulOrganismsAdded.forEach(i => {
                i.selected = false;
                // Selezione come cercati e trovati
                this.visualInspection.organismi.push({ idSpecieOn: i.idOrganismoNocivo, flagTrovato: false });
            })
        }

        this.orderListAlpha();

    }

    public alertChangeSpecie(): void {
        if (this.harmfulOrganismsAdded && this.harmfulOrganismsAdded.length > 0) {
            this.deviceService.alert('DESELECTION_ON');
        }
    }

    private orderListAlpha(): void {
        // Filtro per emergenza
        if (this.isRelatedToEmergency) {
            const emergencyOn = this.harmfulOrganismsAdded.filter(on => {
                return on.flagEmergenza === 'S'
            });
            if (emergencyOn && emergencyOn.length > 0) {
                this.harmfulOrganismsAdded = emergencyOn;
                this.visualInspection.organismi = [];
                this.harmfulOrganismsAdded.forEach(i => {
                    i.selected = false;
                    // Selezione come cercati e trovati
                    this.visualInspection.organismi.push({ idSpecieOn: i.idOrganismoNocivo, flagTrovato: false });
                })
            }
        }
        // Ordino per nome
        if (this.harmfulOrganismsAdded && this.harmfulOrganismsAdded.length > 0) {
            this.harmfulOrganismsAdded = this.harmfulOrganismsAdded.sort((a, b) => {
                if (a.nomeLatino < b.nomeLatino) { return -1; }
                if (a.nomeLatino > b.nomeLatino) { return 1; }
                return 0;
            })
        }
    }

    private _convertToOptions(list: HarmfulOrganism[]): HarmfulOrganismOptions[] {
        const options: HarmfulOrganismOptions[] = list.map((i: HarmfulOrganism) => {
            const option: HarmfulOrganismOptions = new HarmfulOrganismOptions(i);
            option.selected = false;
            return option;
        });
        return options;
    }

    public onHarmfulOrganismSelected(value: any): void {
        this.visualInspection.organismi = [];
        // Se ci sono già organismi selezionati, faccio in modo che nella nuova lista vengano riselezionati
        const previousOrganismAdded = this.harmfulOrganismsAdded;
        this.harmfulOrganismsAdded = value;
        this.harmfulOrganismsAdded.forEach(el => {
            const found = previousOrganismAdded.find(f => {
                return f.idOrganismoNocivo === el.idOrganismoNocivo
            });
            // Se ho trovato l'on tra gli on precedenti, applico il suo flag, altrimenti di default l'on è selezionato, quindi flag default a true
            if (found) {
                el.selected = found.selected ? true : false;
            } else {
                el.selected = false;
            }
            // Organismi nocivi cercati
            const foundSearched = this.visualInspection.organismi.find(on => {
                return on.idSpecieOn === el.idOrganismoNocivo;
            });
            this.visualInspection.organismi.push({ idSpecieOn: el.idOrganismoNocivo, flagTrovato: foundSearched ? foundSearched.flagTrovato : false });
        });

    }

    public async openAvivModal(): Promise<void> {
        this.logger.debug('VisualInspectionPage::openAvivModal');
        this.modal = await this.modalController.create({
            component: AvivModalPage,
            componentProps: {
                codAz: this.detection.cuaa,
                idUte: this.detection.idUte
            }
        });
        return await this.modal.present();
    }

    public organismFounded(item: HarmfulOrganismOptions): void {
        this.logger.debug('VisualInspectionPage::organismFounded');
        this.logger.debug(item);

        // Check, aggiunge organismo solo se non è già in lista
        const foundItem = this.visualInspection.organismi.find(el => { return el.idSpecieOn === item.idOrganismoNocivo });
        if (foundItem && item.selected) {
            foundItem.flagTrovato = true;
        } else if (foundItem && !item.selected) {
            foundItem.flagTrovato = false;
        }
        this.logger.debug(this.visualInspection.organismi);
    }

    public newSample(): void {
        // Prima di aprire la pagina di sampling, salvo l'ispezione corrente
        this.saveVisualInspectionOpenSample();
    }

    public newTrapping(): void {
        // Prima di aprire la pagina di trapping, salvo l'ispezione corrente
        this.saveVisualInspectionOpenTrapping();
    }

    //** Recupero di coordinate ed esecuzione funzione di callback  */
    private getGpsCoordinates(entity: any, callbackAnyway: any, callbackError?: any): void {
        // Se l'oggetto possiede le proprietà di coordinata, tento il get
        if (entity.hasOwnProperty('latitudine') && entity.hasOwnProperty('longitudine')) {
            this.geoUtilsService.getCurrentPosition().then((res: Coordinate) => {
                this.logger.debug(res);
                entity.latitudine = res.latitudine;
                entity.longitudine = res.longitudine;
                callbackAnyway();
            }).catch(err => {
                setTimeout(() => { this.deviceService.hideLoading() }, 100);
                this.deviceService.alert('GET_COORDS_ERROR', {
                    handler: () => {
                        entity.latitudine = 0;
                        entity.longitudine = 0;
                        // Callback di default se non esiste una specifica callback di errore
                        callbackError ? callbackError() : callbackAnyway();
                    }
                })
                this.logger.debug(err);
            });
        }
        else {
            callbackAnyway();
        }
    }

    //** SetRoot sample */
    private setRootSample(sample: Sample, visualInspection: VisualInspection): void {
        setTimeout(() => { this.deviceService.hideLoading() }, 100);
        this.rootNavController.setRoot(SamplePage, {
            visualInspection: visualInspection,
            sample: sample,
            actionRoot: this.actionRoot,
            detection: this.detection
        }, {
            animated: true,
            direction: 'forward'
        });
    }

    //** SetRoot trapping */
    private setRootTrapping(trap: Trapping, visualInspection: VisualInspection): void {
        setTimeout(() => { this.deviceService.hideLoading() }, 100);
        this.rootNavController.setRoot(TrapInstallationPage, {
            visualInspection: visualInspection,
            trapping: trap,
            actionRott: this.actionRoot,
            detection: this.detection
        }, {
            animated: true,
            direction: 'forward'
        });
    }

    private validation(): string {
        let valid = true;
        let errorMessage = '';
        if (valid && !this.visualInspection.idSpecieVegetale) {
            errorMessage = 'Devi selezionare una Specie vegetale';
            valid = false;
        }

        if (valid && this.visualInspection.ispezioni.length === 0 && this.visualInspection.superficie === 0) {
            errorMessage = 'Devi aggiungere almeno una pianta o valorizzare l\'area ispezionata';
            valid = false;
        }
        // if (valid && this.visualInspection.ispezioni.length === 0) {
        //     errorMessage = 'Devi aggiungere almeno una pianta';
        //     valid = false;
        // }
        else {
            const inspections: Inspection[] = [];
            this.singleInspectionCardItems.map(item => {
                const inspection = item.getFormData(this.isRelatedToEmergency);
                if (inspection) {
                    inspections.push(inspection)
                }
                else {
                    errorMessage = 'Devi inserire i campi obbligatori';
                    valid = false;
                }
            });
            if (valid && inspections.length > 0) {
                this.visualInspection.ispezioni = [];
                this.visualInspection.ispezioni = inspections;
            }
        }

        if (valid && this.isGPSTrackingStarted) {
            if (this.deviceService.isIos()) {
                this.geoUtilsService.stopIosGpsTracking();
            }
            else {
                this.geoUtilsService.stopAndroidGpsTracking();
            }
            this.isGPSTrackingStarted = false;
            this.visualInspection.superficie = Math.round(this.geoUtilsService.getConvexPolygonArea(this.inspectedAreaPoints));
        }

        if (valid && this.visualInspection.organismi && this.visualInspection.organismi.length === 0) {
            errorMessage = 'Devi selezionare almeno un organismo nocivo';
            valid = false;
        }

        return errorMessage;
    }

    public saveVisualInspection(): void {
        this.logger.debug('VisualInspectionPage::saveVisualInspection');
        this.logger.debug(this.visualInspection);
        const errorMessage = this.validation();
        if (!errorMessage) {
            this.rootNavController.push(VisualInspectionConfirmPage, {
                detection: this.detection,
                visualInspection: this.visualInspection,
                actionRoot: this.actionRoot
            });
        } else {
            this.deviceService.alert(errorMessage);
        }
    }

    public deleteVisualInspection(): void {
        const self = this;

        this.deviceService.confirm('DELETE_VISUALINSPECTION_CONFIRM', {
            buttons: [
                {
                    text: 'RESET'
                },
                {
                    text: 'CONFIRM',
                    handler: () => {
                        // Se la visual inspection non esiste ancora
                        if (self.visualInspection.idIspezione === 0) {
                            self.deviceService.alert('DELETE_VISUALINSPECTION_SUCCESS', {
                                handler: () => {
                                    this.redirectAfterDelete();
                                }
                            });
                        } else {
                            self.visualInspectionService.deleteVisualInspection(self.visualInspection)
                                .then(() => {
                                    self.deviceService.alert('DELETE_VISUALINSPECTION_SUCCESS', {
                                        handler: () => {
                                            this.redirectAfterDelete();
                                        }
                                    });
                                }).catch((err: any) => {
                                    self.logger.error(err);
                                    self.deviceService.alert('DELETE_VISUALINSPECTION_ERROR');
                                    this.redirectAfterDelete();
                                });
                        }
                    }
                }]
        });
    }

    public redirectAfterDelete(): void {
        this.rootNavController.setRoot(DetectionPage, {
            missionId: this.visualInspection.idMissione,
            detectionId: this.visualInspection.idRilevazione,
            actionRoot: this.actionRoot,
            tempDetection: this.detection
        }, {
            animated: true,
            direction: 'back'
        }).then(() => {
            if (this.deviceService.isIos()) {
                this.geoUtilsService.stopIosGpsTracking();
            }
            else {
                this.geoUtilsService.stopAndroidGpsTracking();
            }
            this.isGPSTrackingStarted = false;
        });
    }

    public saveVisualInspectionOpenSample(): void {
        const errorMessage = this.validation();
        if (errorMessage) {
            this.deviceService.alert(errorMessage);
        } else {
            this.deviceService.showLoading();
            this.logger.debug('VisualInspectionPage::saveVisualInspection');
            // Aggiorno l'ora fine
            if (this.visualInspection.oraFine) {
                this.visualInspection.oraFine = Moment().format('HH:mm');
            }
            this.logger.debug(this.visualInspection);
            this.visualInspectionService.saveOrUpdateVisualInspection(this.visualInspection,this.detection).then(
                response => {
                    this.deviceService.hideLoading();
                    if (this.visualInspection.photos.length == 0) {
                        this.deviceService.alert('SAVE_VISUALINSPECTION_SUCCESS', {
                            handler: () => {
                                this.deviceService.showLoading();
                                const sample: Sample = new Sample();
                                sample.idMissione = (this.detection as Detection).idMissione;
                                sample.idRilevazione = (this.detection as Detection).idRilevazione;
                                sample.idIspezioneVisiva = this.visualInspection.idIspezione;
                                sample.idSpecieVegetale = this.visualInspection.idSpecieVegetale;
                                if (this.visualInspection.organismi.length > 0) {
                                    this.visualInspection.organismi.forEach(on => {
                                        if (on.flagTrovato) {
                                            sample.organismiNocivi.push(on.idSpecieOn)
                                        }
                                    })
                                }
                                sample.photos = [];
                                this.getGpsCoordinates(sample, () => { this.setRootSample(sample, this.visualInspection) });
                            }
                        });
                    }
                    else {
                        const photoToUpload = this.visualInspection.photos.filter(p => p.status == FileStatus.TO_SYNCHRONIZE);
                        from(photoToUpload).pipe(
                            mergeMap(photo => {
                                photo.visualInspectionId = response.idIspezione;
                                photo.detectionId = response.idRilevazione;
                                return this.visualInspectionService.uploadPhoto(photo);
                            }),
                            toArray()
                        ).subscribe(val => {
                            this.logger.debug(val);
                            this.deviceService.alert('SAVE_VISUALINSPECTION_SUCCESS', {
                                handler: () => {
                                    this.deviceService.showLoading();
                                    const sample: Sample = new Sample();
                                    sample.idMissione = (this.detection as Detection).idMissione;
                                    sample.idRilevazione = (this.detection as Detection).idRilevazione;
                                    sample.idIspezioneVisiva = this.visualInspection.idIspezione;
                                    sample.idSpecieVegetale = this.visualInspection.idSpecieVegetale;
                                    if (this.visualInspection.organismi.length > 0) {
                                        this.visualInspection.organismi.forEach(on => {
                                            if (on.flagTrovato) {
                                                sample.organismiNocivi.push(on.idSpecieOn)
                                            }
                                        })
                                    }
                                    sample.photos = [];
                                    this.getGpsCoordinates(sample, () => { this.setRootSample(sample, this.visualInspection) });
                                }
                            });
                        });
                    }
                }
            ).catch((err: any) => {
                this.logger.error(err);
                this.deviceService.hideLoading();
                this.deviceService.alert('SAVE_VISUALINSPECTION_ERROR');
            });
        }

    }

    public saveVisualInspectionOpenTrapping(): void {
        const errorMessage = this.validation();
        if (errorMessage) {
            this.deviceService.alert(errorMessage);
        } else {
            this.logger.debug('VisualInspectionPage::saveVisualInspection');
            // Aggiorno l'ora fine
            if (this.visualInspection.oraFine) {
                this.visualInspection.oraFine = Moment().format('HH:mm');
            }
            this.logger.debug(this.visualInspection);
            this.visualInspectionService.saveOrUpdateVisualInspection(this.visualInspection,this.detection).then(
                response => {
                    if (this.visualInspection.photos.length == 0) {
                        this.deviceService.alert('SAVE_VISUALINSPECTION_SUCCESS', {
                            handler: () => {
                                this.deviceService.showLoading();
                                const trapping: Trapping = new Trapping();
                                trapping.idMissione = (this.detection as Detection).idMissione;
                                trapping.idRilevazione = (this.detection as Detection).idRilevazione;
                                trapping.idIspezioneVisiva = this.visualInspection.idIspezione;
                                trapping.idSpecieVegetale = this.visualInspection.idSpecieVegetale;
                                trapping.photos = [];
                                this.getGpsCoordinates(trapping, () => { this.setRootTrapping(trapping, this.visualInspection) });
                            }
                        });
                    }
                    else {
                        const photoToUpload = this.visualInspection.photos.filter(p => p.status == FileStatus.TO_SYNCHRONIZE);
                        from(photoToUpload).pipe(
                            mergeMap(photo => {
                                photo.visualInspectionId = response.idIspezione;
                                photo.detectionId = response.idRilevazione;
                                return this.visualInspectionService.uploadPhoto(photo);
                            }),
                            toArray()
                        ).subscribe(val => {
                            this.logger.debug(val);
                            this.deviceService.alert('SAVE_VISUALINSPECTION_SUCCESS', {
                                handler: () => {
                                    this.deviceService.showLoading();
                                    const trapping: Trapping = new Trapping();
                                    trapping.idMissione = (this.detection as Detection).idMissione;
                                    trapping.idRilevazione = (this.detection as Detection).idRilevazione;
                                    trapping.idIspezioneVisiva = this.visualInspection.idIspezione;
                                    trapping.idSpecieVegetale = this.visualInspection.idSpecieVegetale;
                                    trapping.photos = [];
                                    this.getGpsCoordinates(trapping, () => { this.setRootTrapping(trapping, this.visualInspection) });
                                }
                            });
                        });
                    }
                }
            ).catch((err: any) => {
                this.logger.error(err)
                this.deviceService.alert('SAVE_VISUALINSPECTION_ERROR');
            });
        }
    }

    public onInput(event: any): void {
        const controlValue = StringUtil.removeNonNumerics(event.target.value);
        this.quantity = parseInt(controlValue);
    }

    public iskeyUpNumber(evt: any): void {
        let controlValue = evt.target.value;
        controlValue = isNaN(controlValue) ? controlValue.slice(0, -1) : controlValue.trim();
    }

    public onKeyUp(event: any): void {
        const MY_REGEXP = /[0-9]+/g;
        const newValue = event.target.value;
        const regExp = new RegExp(MY_REGEXP);

        if (!regExp.test(newValue)) {
            event.target.value = newValue.slice(0, -1);
        }
    }
}
