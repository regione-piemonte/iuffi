import { Component } from '@angular/core';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { Detection } from '@app/detections/models/detection.model';
import { PlantSpeciesAviv } from '@app/detections/models/plant-species-aviv.model';
import { AvivRegistry, BusinessCenter } from '@app/detections/models/registry-aviv.model';
import { Sample } from '@app/detections/models/sample.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { SampleService } from '@app/detections/services/sample.service';
import { TrapService } from '@app/detections/services/trap.service';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
import { MissionService } from '@app/missions/services/mission.service';
import { LoggerService } from '@core/logger';
import { AutoUnsubscribe } from '@core/utils';
import { ModalController, NavParams } from '@ionic/angular';
import { PDFService } from '@shared/file-manager/services/pdf.service';
import { GeoUtilsService } from '@shared/geo-utils/geo-utils.service';
import { Comune } from '@shared/geo-utils/model/comune.model';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';
import * as Moment from 'moment';
import { Subscription } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DeviceService } from '../../../core/device/services/device.service';
import { RootNavController } from '../../../core/split-view/services/root-nav.controller';
import { AreaTypeModalPage } from '../area-type-modal/area-type-modal.page';
import { AvivPlantSpeciesModalPage } from '../aviv-plant-species-modal/aviv-plant-species-modal.page';
import { AvivServiceModalPage } from '../aviv-service-modal/aviv-service-modal.page';
import { SamplePage } from '../sample/sample.page';
import { TrapInstallationPage } from '../trap-installation/trap-installation.page';
import { TrapMaintenanceRemovePage } from '../trap-maintenance-remove/trap-maintenance-remove.page';
import { TrapOpSelectionPage } from '../trap-op-selection/trap-op-selection.page';
import { MissionDetailPage } from './../../../missions/pages/mission-detail/mission-detail.page';
import { VisualInspection } from './../../models/visual-inspection.model';
import { VisualInspectionPage } from './../visual-inspection/visual-inspection.page';


@Component({
    selector: 'detection',
    templateUrl: 'detection.page.html',
    styleUrls: ['detection.page.scss']
})
export class DetectionPage extends AutoUnsubscribe {
    private modal!: HTMLIonModalElement;
    public areaTypeSelected: AreaTypeLocal | null = null;
    public missionId: number;
    public missionDate: number;
    public detectionId: number;
    public detection: Detection | undefined;
    public tempDetection: Detection | undefined;
    public emergencySelected = false;
    public comune: Comune | null = null;
    public actionRoot = '';
    public subsAction = new Array<Subscription>();
    public avivRegistry: AvivRegistry | null = null;
    public businessCenter: BusinessCenter | null = null;
    public codAz = '';
    public isRelatedToNursery = false;

    constructor(

        private rootNavController: RootNavController,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private navParams: NavParams,
        private detectionService: DetectionService,
        private modalController: ModalController,
        private geoUtilsService: GeoUtilsService,
        private visualInspectionService: VisualInspectionService,
        private sampleService: SampleService,
        private trapService: TrapService,
        private missionService: MissionService,
        private pdfService: PDFService
    ) {
        super()

        this.missionId = this.navParams.get('missionId');
        this.missionDate = this.navParams.get('missionDate');
        this.detectionId = this.navParams.get('detectionId');
        this.actionRoot = this.navParams.get('actionRoot') as string || 'NEW';
        this.tempDetection = this.navParams.get('tempDetection');

    }

    ionViewDidEnter(): void {
        this.missionService.getMissionList();
        // Se la rilevazione è nuovo, ancora offline, se va a fare il get perde gli array di ispezioni, campionamenti e trappolaggi
        if (this.tempDetection) {
            this.detection = this.tempDetection;
        } else {
            this.detection = this.detectionService.getDetection(this.missionId, this.detectionId);
        }
        if (this.detection) {
            if (this.detection.idTipoArea) {
                this.areaTypeSelected = this.detectionService.getAreaType(this.detection.idTipoArea);
                // L'area selezionata è relativa ad un vivaio?
                if (this.detection && this.detection.idTipoArea) {
                    const currentAreaType: AreaTypeLocal | null = this.detectionService.getAreaType(this.detection.idTipoArea);
                    if (currentAreaType) {
                        this.isRelatedToNursery = currentAreaType.codiceUfficiale === 'V';
                    }
                }
            }
            this.emergencySelected = (this.detection as Detection).flagEmergenza === 'S';

            // Ordinamento di attività
            if (this.detection.ispezioniVisive && this.detection.ispezioniVisive.length>0) {
                this.detection.ispezioniVisive = this.detection.ispezioniVisive.sort((a: VisualInspection, b: VisualInspection) => {
                    return new Date(a.dataOraInizio).getTime() - new Date(b.dataOraInizio).getTime();
                });
            }
            if (this.detection.campionamenti && this.detection.campionamenti.length>0) {
                this.detection.campionamenti = this.detection.campionamenti.sort((a: Sample, b: Sample) => {
                    return new Date(a.dataOraInizio).getTime() - new Date(b.dataOraInizio).getTime();
                });
            }
            if (this.detection.trappolaggi && this.detection.trappolaggi.length>0) {
                this.detection.trappolaggi = this.detection.trappolaggi.sort((a: Trapping, b: Trapping) => {
                    return new Date(a.dataOraInizio).getTime() - new Date(b.dataOraInizio).getTime();
                });
            }
        }

        // Azienda associata?
        this.getBusinessCenter();

        this.subsAction.push(this.visualInspectionService.deleteVisualInspection$
            .pipe(takeUntil(this.destroy$))
            .subscribe(visualInspection => this._deleteVisualInspection(visualInspection)));

        this.subsAction.push(this.sampleService.deleteSample$
            .pipe(takeUntil(this.destroy$))
            .subscribe(sample => this._deleteSample(sample)));

        this.subsAction.push(this.trapService.deleteTrap$
            .pipe(takeUntil(this.destroy$))
            .subscribe(trap => this._deleteTrap(trap)));

        this.subsAction.push(this.visualInspectionService.modifyVisualInspection$
            .pipe(takeUntil(this.destroy$))
            .subscribe(visualInspection => this._editItem(visualInspection)));

        this.subsAction.push(this.sampleService.modifySample$
            .pipe(takeUntil(this.destroy$))
            .subscribe(sample => this._editItem(sample)));

        this.subsAction.push(this.trapService.modifyTrap$
            .pipe(takeUntil(this.destroy$))
            .subscribe(trap => this._editItem(trap)));

        //Interrompo eventuali gps tracking lasciati attivi in una ispezione visiva
        try {
            if (this.deviceService.isIos()) {
                this.geoUtilsService.stopIosGpsTracking();
            }
            else {
                this.geoUtilsService.stopAndroidGpsTracking();
            }
        }
        catch {}
    }

    ionViewDidLeave(): void {
        if (this.subsAction.length > 0) {
            this.subsAction.forEach(element => {
                element.unsubscribe();
            });
        }
    }

    private _deleteVisualInspection(visualInspection: VisualInspection | null): void {
        this.logger.debug('DELETE ', visualInspection)
        const self = this;
        if (visualInspection) {
            this.deviceService.confirm('DELETE_VISUALINSPECTION_CONFIRM', {
                buttons: [
                    {
                        text: 'RESET'
                    },
                    {
                        text: 'CONFIRM',
                        handler: () => {
                            this.visualInspectionService.deleteVisualInspection(visualInspection).then(() => {
                                self.deviceService.alert('DELETE_VISUALINSPECTION_SUCCESS');
                            }).catch((err: any) => {
                                this.logger.error(err);
                            });
                        }
                    }]
            });

        }
    }

    private _deleteSample(sample: Sample | null): void {
        this.logger.debug('DELETE ', sample)
        const self = this;
        if (sample) {
            this.deviceService.confirm('DELETE_SAMPLE_CONFIRM', {
                buttons: [
                    {
                        text: 'RESET'
                    },
                    {
                        text: 'CONFIRM',
                        handler: () => {
                            this.sampleService.deleteSample(sample).then(() => {
                                self.deviceService.alert('DELETE_SAMPLE_SUCCESS');
                            }).catch((err: any) => {
                                this.logger.error(err);
                                self.deviceService.alert('DELETE_SAMPLE_ERROR');
                            });
                        }
                    }]
            });

        }
    }

    private _deleteTrap(trap: Trapping | null): void {
        this.logger.debug('DELETE ', trap)
        const self = this;
        if (trap) {
            this.deviceService.confirm('DELETE_TRAP_CONFIRM', {
                buttons: [
                    {
                        text: 'RESET'
                    },
                    {
                        text: 'CONFIRM',
                        handler: () => {
                            this.trapService.deleteTrap(trap).then(() => {
                                self.deviceService.alert('DELETE_TRAP_SUCCESS');
                            }).catch((err: any) => {
                                this.logger.error(err);
                                self.deviceService.alert('DELETE_TRAP_ERROR');
                            });
                        }
                    }]
            });

        }
    }

    private _editItem(item: any | null): void {
        this.logger.debug('MODIFY ', item);
        let pageClass;
        if (item.hasOwnProperty('idIspezione') && item['idIspezione']) {
            pageClass = VisualInspectionPage
        } else if (item.hasOwnProperty('idCampionamento') && item['idCampionamento']) {
            pageClass = SamplePage
        } else if (item.hasOwnProperty('idTrappolaggio') && item['idTrappolaggio'] && item['idOperazione'] === 1) {
            pageClass = TrapInstallationPage
        } else if (item.hasOwnProperty('idTrappolaggio') && item['idTrappolaggio'] && (item['idOperazione'] === 2 || item['idOperazione'] === 3 || item['idOperazione'] === 4)) {
            pageClass = TrapMaintenanceRemovePage
        }
        this.rootNavController.push(pageClass, {
            visualInspection: pageClass === VisualInspectionPage ? item : null,
            sample: pageClass === SamplePage ? item : null,
            trapping: (pageClass === TrapInstallationPage || pageClass === TrapMaintenanceRemovePage) ? item : null,
            detection: this.detectionService.getDetection(item.idMissione, item.idRilevazione),
            actionRoot: 'MODIFY'
        });
    }

    public onChangeEmergency(): void {
        this.logger.debug(this.emergencySelected);
        (this.detection as Detection).flagEmergenza = this.emergencySelected ? 'S' : 'N';
    }

    public selectAreaType(): void {
        this.logger.debug('NewDetectionPage::selectAreaType');
        const list = this.detectionService.getAreaTypes();
        this.openAreaTypesModal(list);
    }

    private async openAreaTypesModal(list: AreaTypeLocal[]): Promise<void> {
        this.logger.debug('NewDetectionPage::openAreaTypesModal');
        this.modal = await this.modalController.create({
            component: AreaTypeModalPage,
            componentProps: {
                areaTypeList: list
            }
        });

        this.modal.onWillDismiss().then(value => {
            this.logger.debug('NewDetectionPage::openAreTypesModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
                this.areaTypeSelected = value.data;
                (this.detection as Detection).idTipoArea = (this.areaTypeSelected as AreaTypeLocal).id;
                // L'area selezionata è relativa ad un vivaio?
                if (this.detection && this.detection.idTipoArea) {
                    const currentAreaType: AreaTypeLocal | null = this.detectionService.getAreaType(this.detection.idTipoArea);
                    if (currentAreaType) {
                        this.isRelatedToNursery = currentAreaType.codiceUfficiale === 'V';
                    }
                }
            }
        });

        return await this.modal.present();
    }

    public bussinessDelete():void {
        if (this.detection) {
            this.detection.cuaa = '';
            this.detection.idUte = '';
            this.detection.numeroAviv = '';
            this.avivRegistry = null;
            this.businessCenter = null;
        }
    }

    public newVisualInspection(): void {
        this.logger.debug('NewDetectionPage::newVisualInspection');
        this.deviceService.showLoading();
        this.detectionService.saveOrUpdateDetection((this.detection as Detection)).then(
            () => {
                const visualInspection: VisualInspection = new VisualInspection()
                visualInspection.idMissione = (this.detection as Detection).idMissione;
                visualInspection.idRilevazione = (this.detection as Detection).idRilevazione;
                this.getGpsCoordinates(visualInspection, () => { this.setRoot(VisualInspection, visualInspection) });
            }
        ).catch(err => {
            this.deviceService.hideLoading();
            this.logger.debug(err);
        });
    }

    public newSample(): void {
        this.logger.debug('NewSamplePage::newSample');
        this.deviceService.showLoading();
        this.detectionService.saveOrUpdateDetection((this.detection as Detection)).then(
            () => {
                const sample: Sample = new Sample();
                sample.idMissione = (this.detection as Detection).idMissione;
                sample.idRilevazione = (this.detection as Detection).idRilevazione;
                this.getGpsCoordinates(sample, () => { this.setRoot(Sample, sample) });
            }
        ).catch(err => {
            this.deviceService.hideLoading();
            this.logger.debug(err);
        });
    }

    public newTrap(): void {
        this.logger.debug('NewTrapPage::newTrap');
        this.deviceService.showLoading();
        this.detectionService.saveOrUpdateDetection((this.detection as Detection)).then(
            () => {
                this.deviceService.hideLoading();
                const trap: Trapping = new Trapping();
                trap.idMissione = (this.detection as Detection).idMissione;
                trap.idRilevazione = (this.detection as Detection).idRilevazione;
                this.setRoot(Trapping, trap);
                // this.getGpsCoordinates(trap, () => { this.setRoot(Trapping, trap) });
            }
        ).catch(err => {
            this.deviceService.hideLoading();
            this.logger.debug(err);
        });
    }

    public deleteDetection(): void {
        const self = this;
        this.deviceService.confirm('DELETE_DETECTION_CONFIRM', {
            buttons: [
                {
                    text: 'RESET'
                },
                {
                    text: 'CONFIRM',
                    handler: () => {
                        this.detectionService.deleteDetection(self.detection as Detection).then(
                            () => {
                                self.deviceService.alert('DELETE_DETECTION_SUCCESS', {
                                    handler: () => {
                                        self.rootNavController.setRoot(MissionDetailPage, { missionId: this.missionId, missionDate: this.missionDate }, {
                                            animated: true,
                                            direction: 'forward'
                                        });
                                    }
                                });
                            },
                            () => {
                                self.deviceService.alert('DELETE_DETECTION_ERROR');
                            }
                        );
                    }
                }]
        });
    }

    public saveDetection(): void {
        const self = this;
        if (!(this.detection as Detection).oraFine) {
            (this.detection as Detection).oraFine = Moment().format('HH:mm');
        }

        this.detectionService.saveOrUpdateDetection(this.detection as Detection).then(
            () => {
                self.deviceService.alert('SAVE_DETECTION_SUCCESS', {
                    handler: () => {
                        self.rootNavController.setRoot(MissionDetailPage, { missionId: (this.detection as Detection).idMissione, missionDate: this.missionDate }, {
                            animated: true,
                            direction: 'forward'
                        });
                    }
                });
            },
            () => {
                self.deviceService.alert('SAVE_DETECTION_ERROR');
            }
        );
    }

    //** Recupero di coordinate ed esecuzione funzione di callback  */
    private getGpsCoordinates(entity: any, callbackAnyway: any, callbackError?: any): void {
        // Se l'oggetto possiede le proprietà di coordinata, tento il get
        if (entity.hasOwnProperty('latitudine') && entity.hasOwnProperty('longitudine')) {
            this.geoUtilsService.getCurrentPosition().then((res: Coordinate) => {
                this.logger.debug(res);
                entity.latitudine = res.latitudine;
                entity.longitudine = res.longitudine;
                setTimeout(() => { this.deviceService.hideLoading() }, 100);
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

    public async openPlantSpeciesAvivModal(list: PlantSpeciesAviv[]): Promise<void> {
        this.logger.debug('VisualInspectionPage::openPlantSpeciesAvivModal');
        this.modal = await this.modalController.create({
            component: AvivPlantSpeciesModalPage,
            componentProps: {
                plantSpecies: list
            }
        });
        this.modal.onWillDismiss().then(value => {
            this.logger.debug('VisualInspectionPage::openAvivServiceModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
            }
        });
        return await this.modal.present();
    }

    public viewLastReportAviv(): void {
        if ((this.businessCenter as BusinessCenter).idUte) {
            this.logger.debug('VisualInspectionPage::viewLastReportAviv');
            this.deviceService.showLoading();
            this.visualInspectionService.downloadReportAviv((this.businessCenter as BusinessCenter).idUte).then(
                report => {
                    if (this.deviceService.isCordova() && report && report.file && report.nome) {
                        this.pdfService.saveAndOpenPdf(report.file, report.nome);
                    }
                    this.deviceService.showLoading();
                    // La generazione del pdf può richiedere alcuni secondi
                    setTimeout(() => {
                        this.deviceService.hideLoading();
                    },3000);
                }
            ).catch((err: any) => {
                setTimeout(() => { this.deviceService.hideLoading(); },300);
                this.deviceService.alert('READ_REPORT_ERROR');
                this.logger.debug(err);
            });
        }
    }

    public selectPlantSpeciesAviv(): void {
        this.logger.debug('VisualInspectionPage::selectPlantSpeciesAviv');
        this.deviceService.showLoading();
        if (this.codAz) {
            this.visualInspectionService.getPlantSpeciesAviv(this.codAz).then(
                list => {
                    this.deviceService.hideLoading();
                    this.openPlantSpeciesAvivModal(list);
                }
            ).catch((err: any) => {
                this.deviceService.hideLoading();
                this.logger.debug(err);
            });
        }
    }

    public async openAvivServiceModal(): Promise<void> {
        this.logger.debug('VisualInspectionPage::openAvivServiceModal');
        this.modal = await this.modalController.create({
            component: AvivServiceModalPage
        });

        this.modal.onWillDismiss().then(value => {
            this.logger.debug('VisualInspectionPage::openAvivServiceModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
                this.codAz = value.data.codAz;
                this.avivRegistry = value.data.avivRegistry;
                this.businessCenter = value.data.businessCenter;
            }
            if (this.detection && value.data) {
                this.detection.cuaa = value.data.codAz;
                this.detection.idUte = value.data.businessCenter.idUte;
                this.detection.numeroAviv = value.data.avivRegistry.anagRuop;
            }
        });

        return await this.modal.present();
    }

    public getBusinessCenter(): void {
        try {
            if (this.detection && this.detection?.cuaa) {
                this.codAz = this.detection.cuaa;
                const idUte =  this.detection.idUte;
                this.deviceService.showLoading();
                this.visualInspectionService.getAnagraficheAviv(this.codAz).then((res: any) => {
                    this.logger.debug(res);
                    this.avivRegistry = res;
                    if (!res) {
                        throw Error();
                    }
                    if (this.detection && this.detection?.idUte) {
                        if (this.avivRegistry && this.avivRegistry.arrCA.length > 0) {
                            const bcFound = this.avivRegistry.arrCA.find(bc => {
                                return bc.idUte == +idUte
                            });
                            if (bcFound) {
                                this.businessCenter   = bcFound;
                            }
                        }
                    }
                    setTimeout(() => { this.deviceService.hideLoading(); },300);
                }).catch((err: any) => {
                    setTimeout(() => { this.deviceService.hideLoading(); },300);
                    this.deviceService.alert('AVIV_REGISTRY_ERROR');
                    this.logger.debug(err);
                });
            }
        } catch (err) {
            setTimeout(() => { this.deviceService.hideLoading(); },300);
            this.deviceService.alert('AVIV_REGISTRY_ERROR');
            this.logger.debug(err);
        }
    }

    //** SetRoot di navController */
    private setRoot(entityClass: any, entity: any): void {
        const entityPage = entityClass === VisualInspection ? VisualInspectionPage : (entityClass === Sample ? SamplePage : TrapOpSelectionPage)
        this.rootNavController.setRoot(entityPage, {
            visualInspection: entity instanceof VisualInspection ? entity : null,
            trapping: entity instanceof Trapping ? entity : null,
            sample: entity instanceof Sample ? entity : null,
            detection: this.detection,
            missionId: this.missionId,
            actionRoot: this.actionRoot
        }, {
            animated: true,
            direction: 'forward',

        });
    }
}
