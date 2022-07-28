import { Component, ViewChild } from '@angular/core';
import { PlantSpeciesAviv } from '@app/detections/models/plant-species-aviv.model';
import { AvivRegistry, BusinessCenter } from '@app/detections/models/registry-aviv.model';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { IonInfiniteScroll, ModalController, NavParams } from '@ionic/angular';
import { PDFService } from '@shared/file-manager/services/pdf.service';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';

import { AvivPlantSpeciesModalPage } from '../aviv-plant-species-modal/aviv-plant-species-modal.page';
import { AvivServiceModalPage } from '../aviv-service-modal/aviv-service-modal.page';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'aviv-modal',
    templateUrl: 'aviv-modal.page.html',
    styleUrls: ['aviv-modal.page.scss']
})
export class AvivModalPage {
    private modal!: HTMLIonModalElement;
    public areaTypeSelected: AreaTypeLocal | null = null;
    public businessCenter: BusinessCenter | null = null;
    public codAz = '';
    public idUte = '';
    public avivRegistry: AvivRegistry | null = null;

    @ViewChild(IonInfiniteScroll) public infiniteScroll: IonInfiniteScroll | undefined;

    constructor(
        private modalController: ModalController,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private visualInspectionService: VisualInspectionService,
        private pdfService: PDFService,
        private navParams: NavParams
    ) {
        this.codAz = this.navParams.get('codAz');
        this.idUte = this.navParams.get('idUte');
        if (this.codAz) {
            this.deviceService.showLoading();
            this.visualInspectionService.getAnagraficheAviv(this.codAz).then((res: any) => {
                this.logger.debug(res);
                if (!res) {
                    throw Error();
                }
                this.avivRegistry = res;
                if (this.idUte) {
                    if (this.avivRegistry && this.avivRegistry.arrCA.length > 0) {
                        const bcFound = this.avivRegistry.arrCA.find(bc => {
                            return bc.idUte == +this.idUte
                        });
                        if (bcFound) {
                            this.businessCenter   = bcFound;
                        }
                    }
                }
                setTimeout(() => { this.deviceService.hideLoading(); },300);

            }).catch((err: any) => {
                setTimeout(() => { this.deviceService.hideLoading(); },300);
                this.deviceService.alert('AVIV_REGISTRY_ERROR')
                this.logger.debug(err);
            });
        }
    }

    public viewLastReportAviv(): void {
        if ((this.businessCenter as BusinessCenter).idUte) {
            this.logger.debug('VisualInspectionPage::viewLastReportAviv');
            this.deviceService.showLoading();
            this.visualInspectionService.downloadReportAviv((this.businessCenter as BusinessCenter).idUte).then(
                report => {
                    if (report && report.file && report.nome) {
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
        });

        return await this.modal.present();
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

    public confirm(): void {
        this.modalController.dismiss();
    }

    public close(): void {
        this.modalController.dismiss();
    }
}
