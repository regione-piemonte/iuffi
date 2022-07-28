import { HarmfulOrganism } from './../../../shared/offline/models/harmful-organism.model';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TrapTypeModalPage } from '@app/detections/pages/trap-type-modal/trap-type-modal.page';
import { LoggerService } from '@core/logger';
import { ModalController } from '@ionic/angular';
import { TrapType } from '@shared/offline/models/trap-type.model';

@Component({
    selector: 'trap-type-selection',
    templateUrl: './trap-type-selection.component.html',
    styleUrls: ['./trap-type-selection.component.scss'],
})
export class TrapTypeSelectionComponent {
    @Input() public loggerInfo!: string;
    @Input() public plantSpeciesSelected!: any;
    @Input() public harmfulOrganismSelected!: HarmfulOrganism;
    //** Tipi di campionamento già selezionato */
    @Input() public trapType!: TrapType;
    //** Array di selezione corrente dei tipi di campionamento */
    @Output() public trapTypeSelected = new EventEmitter<any>();

    private modal!: HTMLIonModalElement;

    constructor(
        private logger: LoggerService,
        private modalController: ModalController) { }

    ngOnChanges(changes: any): void {
        if (!changes.trapType.firstChange) {
            this.trapType = changes.trapType.currentValue;
        }
    }

    public selectTrapTypes(): void {
        this.logger.debug(`${this.loggerInfo}::selectTrapType`);
        this.openTrapTypesModal();
    }

    private async openTrapTypesModal(): Promise<void> {
        this.logger.debug(`${this.loggerInfo}::openTrapTypeModal`);
        this.modal = await this.modalController.create({
            component: TrapTypeModalPage,
            componentProps: {
                trapType: this.trapType,
                harmfulOrganism: this.harmfulOrganismSelected
            }
        });

        this.modal.onWillDismiss().then(value => {
            this.logger.debug(`${this.loggerInfo}::openTrapTypeModal::onWillDismiss`);
            if (value.data) {
                this.logger.debug(value.data);
                this.trapTypeSelected.emit(value.data);
            }
        });

        return await this.modal.present();
    }

}
