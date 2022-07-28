import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { MapModalPage } from '@app/detections/pages/map-modal/map-modal.page';
import { LoggerService } from '@core/logger';
import { ModalController } from '@ionic/angular';

import { InspectionDto } from './../../dto/visual-inspection.dto';
import { Inspection } from './../../models/visual-inspection.model';

@Component({
    selector: 'single-inspection-card-component',
    templateUrl: 'single-inspection-card.component.html',
    styleUrls: ['single-inspection-card.component.scss']
})
export class SingleInspectionCardComponent {
    public inspectionForm!: FormGroup;
    public positivity: any[] = [];
    public diameter: any[] = [];
    private modal!: HTMLIonModalElement;
    @Input() public index = 0;
    @Input() public inspection!: Inspection;
    @Input() public isRelatedToEmergency = false;
    @Output() public onDelete: EventEmitter<number> = new EventEmitter()
    constructor(
        private formBuilder: FormBuilder,
        private logger: LoggerService,
        private modalController: ModalController
    ) {
        this.positivity = [
            {
                value: 1,
                label: 'POSITIVE',
            },
            {
                value: 2,
                label: 'NEGATIVE',
            },
            {
                value: 3,
                label: 'DOUBTFUL',
            }
        ];

        this.diameter = [
            {
                value: 1,
                label: 'LESS_THAN_10',
            },
            {
                value: 2,
                label: 'BETWEEN_10_AND_20',
            },
            {
                value: 3,
                label: 'GREATER_THAN_20',
            }
        ];
    }

    ngOnInit(): void {
        this.inspectionForm = this.formBuilder.group({
            ubicazione: new FormGroup({
                numero: new FormControl(this.inspection.ubicazione?.numero, Validators.compose([
                    Validators.required
                ])),
                nome: new FormControl(this.inspection.ubicazione?.nome, Validators.compose([
                    Validators.required
                ])),
                cognome: new FormControl(this.inspection.ubicazione?.cognome, Validators.compose([
                    Validators.required
                ])),
                indirizzo: new FormControl(this.inspection.ubicazione?.indirizzo, Validators.compose([
                    Validators.required
                ])),
                telefono: new FormControl(this.inspection.ubicazione?.telefono, Validators.compose([
                    Validators.required
                ])),
                email: new FormControl(this.inspection.ubicazione?.email, Validators.compose([
                    Validators.required
                ]))
            }),
            positivita: new FormControl(this.inspection.positivita, Validators.compose([
                Validators.required
            ])),
            diametro: new FormControl(this.inspection.diametro, Validators.compose([
                Validators.required
            ])),
            flagTreeClimberIspezione: new FormControl(this.inspection.flagTreeClimberIspezione == 'S' ? true : false),
            flagTreeClimberTaglio: new FormControl(this.inspection.flagTreeClimberTaglio == 'S' ? true : false),
            note1: new FormControl(this.inspection.note1),
            note2: new FormControl(this.inspection.note2),
            note3: new FormControl(this.inspection.note3),
        });
    }

    public delete(): void {
        this.onDelete.emit(this.index);
    }

    public getFormData(validate: boolean): Inspection | null {
        this.logger.info('SingleInspectionCardComponent:getFormData');
        if (validate) {
            this.inspectionForm.markAllAsTouched();
            if (this.inspectionForm.valid) {
                const inspectionDto: InspectionDto = this.inspectionForm.getRawValue() as InspectionDto;
                if (this.isRelatedToEmergency) {
                    this.inspection.ubicazione = inspectionDto.ubicazione;
                    this.inspection.positivita = inspectionDto.positivita ? inspectionDto.positivita : null;
                    this.inspection.diametro = inspectionDto.diametro ? inspectionDto.diametro : null;
                    this.inspection.flagTreeClimberIspezione = inspectionDto.flagTreeClimberIspezione ? 'S' : 'N';
                    this.inspection.flagTreeClimberTaglio = inspectionDto.flagTreeClimberTaglio ? 'S' : 'N';
                    this.inspection.note1 = inspectionDto.note1;
                    this.inspection.note2 = inspectionDto.note2;
                    this.inspection.note3 = inspectionDto.note3;
                }
                return this.inspection;
            }
            return null;
        }
        return this.inspection;

    }

    public async openMapModal(): Promise<void> {
        this.logger.debug('VisualInspectionPage::openMapModal');
        this.modal = await this.modalController.create({
            component: MapModalPage,
            componentProps: {
                currentPosition: new Coordinate(this.inspection.coordinate?.latitudine, this.inspection.coordinate?.longitudine)
            }
        });
        this.modal.onWillDismiss().then((value: any) => {
            this.logger.debug('VisualInspectionPage::openMapModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
                this.inspection.coordinate = new Coordinate((value.data as Coordinate).latitudine,(value.data as Coordinate).longitudine);

            }
        });
        return await this.modal.present();
    }

}
