import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SampleTypeModalPage } from '@app/detections/pages/sample-type-modal/sample-type-modal.page';
import { LoggerService } from '@core/logger';
import { ModalController } from '@ionic/angular';
import { SampleType } from '@shared/offline/models/sample-type.model';

@Component({
    selector: 'sample-type-selection',
    templateUrl: './sample-type-selection.component.html',
    styleUrls: ['./sample-type-selection.component.scss'],
})
export class SampleTypeSelectionComponent {
    @Input() public loggerInfo!: string;
    @Input() public plantSpeciesSelected!: any;
    @Input() public harmfulOrganismsSelected!: any;
    //** Tipi di campionamento già selezionato */
    @Input() public sampleType!: SampleType;
    //** Array di selezione corrente dei tipi di campionamento */
    @Output() public sampleTypeSelected = new EventEmitter<any>();

    private modal!: HTMLIonModalElement;

    constructor(
        private logger: LoggerService,
        private modalController: ModalController) { }

    ngOnChanges(changes: any): void {
        if (changes && changes.sampleType && !changes.sampleType.firstChange) {
            this.sampleType = changes.sampleType.currentValue;
        }
        if (changes && changes.harmfulOrganismsSelected && !changes.harmfulOrganismsSelected.firstChange) {
            this.harmfulOrganismsSelected = changes.harmfulOrganismsSelected.currentValue;
        }
    }

    public selectSampleTypes(): void {
        this.logger.debug(`${this.loggerInfo}::selectSampleType`);
        this.openSampleTypesModal();
    }

    private async openSampleTypesModal(): Promise<void> {
        this.logger.debug(`${this.loggerInfo}::openSampleTypeModal`);
        this.modal = await this.modalController.create({
            component: SampleTypeModalPage,
            componentProps: {
                sampleType: this.sampleType,
                plantSpecies: this.plantSpeciesSelected,
                harmfulOrganisms: this.harmfulOrganismsSelected
            }
        });

        this.modal.onWillDismiss().then(value => {
            this.logger.debug(`${this.loggerInfo}::openSampleTypeModal::onWillDismiss`);
            if (value.data) {
                this.logger.debug(value.data);
                this.sampleTypeSelected.emit(value.data);
            }
        });

        return await this.modal.present();
    }

}
