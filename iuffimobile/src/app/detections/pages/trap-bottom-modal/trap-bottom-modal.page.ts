import { Component } from '@angular/core';
import { Trap } from '@app/detections/models/trap.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { ModalController, NavParams } from '@ionic/angular';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'trap-bottom-modal',
    templateUrl: 'trap-bottom-modal.page.html',
    styleUrls: ['trap-bottom-modal.page.scss']
})
export class TrapBottomModalPage {
    public trap: Trap;
    constructor(
        private logger: LoggerService,
        private navParams: NavParams,
        private modalController: ModalController,
        private deviceService: DeviceService,
        private DetectionService: DetectionService
    )
    {
        this.trap = this.navParams.get('trap');
        const PlantSpeciesDetail = this.trap.idSpecieVeg ? this.DetectionService.getPlantSpeciesDetail(this.trap.idSpecieVeg) : null;
        this.trap.specie = PlantSpeciesDetail? PlantSpeciesDetail?.nomeVolgare : '';
    }

    public confirm(): void {
        this.modalController.dismiss(this.trap);
    }

    public close(): void {
        this.modalController.dismiss();
    }
}
