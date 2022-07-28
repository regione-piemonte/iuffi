import { Component, EventEmitter, Input, Output } from '@angular/core';
import { HarmfulOrganismModalPage } from '@app/detections/pages/harmful-organism-modal/harmful-organism-modal.page';
import { LoggerService } from '@core/logger';
import { ModalController } from '@ionic/angular';
import { HarmfulOrganismOptions } from '@shared/offline/models/harmful-organism.model';

@Component({
    selector: 'harmful-organism-selection',
    templateUrl: './harmful-organism-selection.component.html',
    styleUrls: ['./harmful-organism-selection.component.scss'],
})
export class HarmfulOrganismSelectionComponent {
    @Input() public loggerInfo!: string;
    @Input() public plantSpeciesSelected!: any;
    //** Array degli organismi già selezionati */
    @Input() public harmfulOrganismsAdded: HarmfulOrganismOptions[] = [];
    //** Filtro degli organismi da mostare */
    @Input() public harmfulOrganismsFilter: number[] = [];
    //** Check o radio button */
    @Input() public singleSelection = false;
    //** Array di selezione corrente degli organismi */
    @Output() public harmfulOrganismSelected = new EventEmitter<any>();
    @Input() public readOnly = false;
    @Input() public isRelatedToEmergency = false;

    private modal!: HTMLIonModalElement;

    constructor(
        private logger: LoggerService,
        private modalController: ModalController) { }

    public selectHarmfulOrganism(): void {
        if (!this.readOnly) {
            this.logger.debug(`${this.loggerInfo}::selectHarmfulOrganism`);
            this.openHarmfulOrganismsModal();
        }
    }

    private async openHarmfulOrganismsModal(): Promise<void> {
        this.logger.debug(`${this.loggerInfo}::openHarmfulOrganismsModal`);
        this.modal = await this.modalController.create({
            component: HarmfulOrganismModalPage,
            componentProps: {
                plantSpecies: this.plantSpeciesSelected,
                harmfulOrganismsAdded: this.harmfulOrganismsAdded,
                harmfulOrganismsFilter: this.harmfulOrganismsFilter,
                singleSelection: this.singleSelection,
                isRelatedToEmergency: this.isRelatedToEmergency
            }
        });

        this.modal.onWillDismiss().then(value => {
            this.logger.debug(`${this.loggerInfo}::openHarmfulOrganismsModal::onWillDismiss`);
            if (value.data) {
                this.logger.debug(value.data);
                this.harmfulOrganismSelected.emit(value.data);
            }
        });

        return await this.modal.present();
    }

}
