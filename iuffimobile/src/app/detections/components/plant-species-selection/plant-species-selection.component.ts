import { PlantSpeciesDetail } from './../../../shared/offline/models/plant-species.model';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PlantSpeciesModalPage } from '@app/detections/pages/plant-species-modal/plant-species-modal.page';
import { DetectionService } from '@app/detections/services/detection.service';
import { LoggerService } from '@core/logger';
import { ModalController } from '@ionic/angular';
import { ModalNavPage } from '@shared/components/modal-nav';

@Component({
    selector: 'plant-species-selection',
    templateUrl: './plant-species-selection.component.html',
    styleUrls: ['./plant-species-selection.component.scss'],
})
export class PlantSpeciesSelectionComponent {
    @Input() public loggerInfo!: string;
    @Input() public readOnly = false;
    @Input() public plantSpeciesDetailSelected!: PlantSpeciesDetail;
    @Output() public plantSpeciesSelected = new EventEmitter<any>();

    private modal!: HTMLIonModalElement;

    constructor(
        private logger: LoggerService,
        private detectionService: DetectionService,
        private modalController: ModalController
    ) { }

    public selectPlantSpecies(): void {
        this.logger.debug(`${this.loggerInfo}::selectPlantSpecies`);
        if (!this.readOnly) {
            this.openPlantSpeciesModal();
        }
    }

    private async openPlantSpeciesModal(): Promise<void> {
        this.logger.debug(`${this.loggerInfo}::openPlantSpeciesModal`);
        this.modal = await this.modalController.create({
            component: PlantSpeciesModalPage,
            componentProps: {
                plantSpeciesSelected: this.plantSpeciesDetailSelected
            }
        });

        this.modal.onWillDismiss().then(value => {
            this.logger.debug(`${this.loggerInfo}::openPlantSpeciesModal::onWillDismiss`);
            if (value.data) {
                this.logger.debug(value.data);
                this.plantSpeciesSelected.emit(value.data);
            }
        });

        return await this.modal.present();
    }

}
