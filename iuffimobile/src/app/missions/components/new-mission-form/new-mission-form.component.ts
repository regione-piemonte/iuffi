import { Component, Input, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Mission } from '@app/missions/models/mission.model';
import { InspectorsFilterPage } from '@app/missions/pages/inspectors-filter/inspectors-filter.page';
import { MissionService } from '@app/missions/services/mission.service';
import { User } from '@core/auth';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { IonDatetime, ModalController } from '@ionic/angular';
import * as Moment from 'moment';

import { AuthService } from './../../../core/auth/services/auth.service';
import { InspectorDto } from './../../dto/inspector.dto';
import { MissionDto } from './../../dto/mission.dto';
import { Inspector, InspectorOptions } from './../../models/inspector.model';

@Component({
    selector: 'new-mission-form',
    templateUrl: 'new-mission-form.component.html',
    styleUrls: ['new-mission-form.component.scss']
})
export class NewMissionForm {
    private modal!: HTMLIonModalElement;
    public missionForm!: FormGroup;
    public monthNames: string[] = [];
    public inspectorsAdded: InspectorDto[] = [];
    public minCreationDate = Moment();
    public minEndMissionDate = Moment();
    @Input() public mission!: Mission;
    @ViewChild('datePickerDay') private _datePickerDay!: IonDatetime;
    constructor(
        private missionService: MissionService,
        private modalController: ModalController,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private authService: AuthService
    ) {
        this.monthNames = Moment.months('MMMM').map(i => i.charAt(0).toUpperCase() + i.substr(1));
    }

    ngOnInit(): void {
        const user: User | null = this.authService.getUser();
        if (this.mission.idMissione === 0) {
            if (this.deviceService.isCordova()) {
                if (user) {
                    this.mission.nomeIspettore = user.firstName;
                    this.mission.cognomeIspettore = user.lastName;
                    this.mission.cfIspettore = user.fiscalCode;
                }
            }
            else {
                this.mission.nomeIspettore = 'Mario';
                this.mission.cognomeIspettore = 'Rossi';
                this.mission.cfIspettore = 'AAAAAA00B77B000F';
            }
        }

        if (this.mission.ispettoriAggiunti && this.mission.ispettoriAggiunti.length > 0) {
            this.mission.ispettoriAggiunti.forEach((i: Inspector) => {
                const inspector: InspectorOptions = new InspectorOptions(i);
                inspector.selected = true;
                this.inspectorsAdded.push(inspector)
            });
        }

        this.missionForm = this.missionService.createMissionForm(this.mission);

        if (this.missionForm.controls.oraInizio) {
            this.minEndMissionDate = Moment().startOf('day').add((+this.missionForm.controls.oraInizio.value.substr(0, 2)), 'hours').add((+this.missionForm.controls.oraInizio.value.substr(3, 2) + 1), 'minutes');
        }
    }

    public valid(): MissionDto | null {
        this.missionForm.markAllAsTouched();
        let missionDto: MissionDto | null = null;
        if (this.missionForm.valid) {
            missionDto = this.missionForm.getRawValue();
            if (missionDto) {
                missionDto.dataMissione = Moment(missionDto.dataMissione).format('DD-MM-YYYY');
                if (missionDto.oraFine && missionDto.oraFine.length > 5) {
                    missionDto.oraFine = Moment(missionDto.oraFine).format('HH:mm');
                }
                missionDto.ispettoriAggiunti = [];
                if (this.inspectorsAdded && this.inspectorsAdded.length > 0) {
                    // Conversione ispettori Options
                    this.inspectorsAdded.forEach(el => {
                        const isp = new Inspector();
                        isp.cfAnagraficaEst = el.cfAnagraficaEst;
                        isp.cognome = el.cognome;
                        isp.nome = el.nome;
                        isp.idAnagrafica = el.idAnagrafica;
                        missionDto?.ispettoriAggiunti?.push(isp);
                    });
                } else {
                    missionDto.ispettoriAggiunti = [];
                }
            }
            return missionDto;
        }
        return null;
    }

    public isValid(): boolean {
        return this.missionForm.valid;
    }

    public updateEndMinDate(event: any): void {
        if (this.missionForm.controls.oraInizio) {
            this.minEndMissionDate = Moment().startOf('day').add((+this.missionForm.controls.oraInizio.value.substr(0, 2)), 'hours').add((+this.missionForm.controls.oraInizio.value.substr(3, 2) + 1), 'minutes');
        }
    }

    public removeInspector(index: number): void {
        this.logger.debug('NewMissionForm::addInspector');
        this.inspectorsAdded.splice(index, 1);
    }

    public fetchInspectors(): void {
        const inspectorList = this.missionService.getInspectors();
        const inspectors: InspectorOptions[] = inspectorList.map((i: Inspector) => {
            const inspector: InspectorOptions = new InspectorOptions(i);
            if (this.inspectorsAdded && this.inspectorsAdded.length > 0) {
                this.inspectorsAdded.map((t: Inspector) => {
                    if (t.cfAnagraficaEst === inspector.cfAnagraficaEst) {
                        inspector.selected = true;
                    }
                })
            }
            return inspector
        });
        this.openInspectorsModal(inspectors);
    }

    private async openInspectorsModal(list: InspectorOptions[]): Promise<void> {
        this.modal = await this.modalController.create({
            component: InspectorsFilterPage,
            cssClass: 'delivery-modal',
            componentProps: {
                inspectorList: list,
                inspectorCf: ( this.mission && this.mission.cfIspettore)?  this.mission.cfIspettore : null
            }
        });

        this.modal.onWillDismiss().then(value => {
            if (value.data) {
                this.inspectorsAdded = [];
                value.data.forEach((i: InspectorOptions) => {
                    if (i.selected) {
                        this.inspectorsAdded.push(new Inspector(i));
                    }
                });
            }
        });

        return await this.modal.present();
    }
}
