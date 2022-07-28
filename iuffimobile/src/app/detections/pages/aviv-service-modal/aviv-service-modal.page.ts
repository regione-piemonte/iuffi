import { Component } from '@angular/core';
import { AvivRegistry, BusinessCenter } from '@app/detections/models/registry-aviv.model';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { ModalController, NavParams } from '@ionic/angular';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'aviv-service-modal',
    templateUrl: 'aviv-service-modal.page.html',
    styleUrls: ['aviv-service-modal.page.scss']
})
export class AvivServiceModalPage {
    public codAz = '';
    public avivRegistry: AvivRegistry | null = null;
    public businessCenterSelected!: BusinessCenter;
    constructor(
        private logger: LoggerService,
        private navParams: NavParams,
        private modalController: ModalController,
        private deviceService: DeviceService,
        private visualInspectionService: VisualInspectionService
    ) {

    }
    public searchAnagraficaAviv(): void {
        if (this.codAz) {
            this.codAz = this.codAz.toUpperCase()
            if (this.codAz.length > 0 && this.codAz.length < 4 ) {
                const isnum = /^\d+$/.test(this.codAz);
                // Aggiungo gli zeri per arrivare a 4 solo se il campo è tutto numerico
                this.codAz = isnum? this.pad(this.codAz, 4) : this.codAz;
            }
            if (this.codAz.length < 4 || this.codAz.length > 4 && this.codAz.length < 11 || this.codAz.length > 11 && this.codAz.length < 16) {
                this.deviceService.alert('La lunghezza del codice inserito non è ammessa');
            }
            else {
                this.deviceService.showLoading();
                this.visualInspectionService.getAnagraficheAviv(this.codAz).then((res: any) => {
                    this.logger.debug(res);
                    this.avivRegistry = res;
                    if (this.avivRegistry && this.avivRegistry.arrCA.length ===  1) {
                        this.businessCenterSelected = this.avivRegistry.arrCA[0];
                        this.confirm();
                    }
                    setTimeout(() => { this.deviceService.hideLoading(); },300);

                }).catch((err: any) => {
                    setTimeout(() => { this.deviceService.hideLoading(); },300);
                    this.deviceService.alert('AVIV_REGISTRY_ERROR')
                    this.logger.debug(err);
                });
            }
        }
    }
    public confirm(): void {
        if (this.businessCenterSelected) {
            this.modalController.dismiss({ codAz: this.codAz, avivRegistry: this.avivRegistry, businessCenter: this.businessCenterSelected });
        }
        else {
            if (this.avivRegistry && this.avivRegistry.arrCA.length>0) {
                this.deviceService.alert('SELECT_BUSINESS_CENTER');
            } else {
                this.modalController.dismiss({ codAz: this.codAz, avivRegistry: this.avivRegistry, businessCenter: null });
            }

        }

    }

    public pad(num: string, size: number): string {
        let s = num + '';
        while (s.length < size) s = '0' + s;
        return s;
    }

    public close(): void {
        this.deviceService.closeKeyboard()
        setTimeout(() => {
            this.modalController.dismiss();
        }, 300);
    }
}
