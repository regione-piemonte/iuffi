import { Component, ViewChild } from '@angular/core';
import { TrapService } from '@app/detections/services/trap.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { SpeechRecognition, SpeechRecognitionListeningOptions } from '@ionic-native/speech-recognition/ngx';
import { IonSearchbar, ModalController, NavParams } from '@ionic/angular';
import { TranslateService } from '@ngx-translate/core';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';
import { TrapType } from '@shared/offline/models/trap-type.model';
import * as _ from 'lodash';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'trap-type-modal',
    templateUrl: 'trap-type-modal.page.html',
    styleUrls: ['trap-type-modal.page.scss']
})
export class TrapTypeModalPage {
    public trapTypes: TrapType[] = [];
    public trapTypesValid: TrapType[] = [];
    public trapTypesCompatibilità: TrapType[] = [];
    public trapTypesFiltered: TrapType[] = [];
    public trapTypesToShow: TrapType[] = [];
    public trapTypeSelected: TrapType;
    public showMoreSelected = false;
    public isListening = false;
    public currentHarmfulOrganisms: HarmfulOrganism;
    @ViewChild('searchbar') private _searchbar!: IonSearchbar;
    constructor(
        private logger: LoggerService,
        private navParams: NavParams,
        private modalController: ModalController,
        private speechRecognition: SpeechRecognition,
        private deviceService: DeviceService,
        private trapService: TrapService,
        public translateService: TranslateService
    ) {
        this.trapTypeSelected = this.navParams.get('trapType') as TrapType;
        this.currentHarmfulOrganisms = this.navParams.get('harmfulOrganism') as HarmfulOrganism;

        const lists = this.trapService.getTrapTypes(this.currentHarmfulOrganisms);

        // Lista con tutti i sample types
        this.trapTypes = lists.trapTypes;
        this.trapTypesValid = lists.trapTypesFilteredValidity;
        this.trapTypesCompatibilità = lists.trapTypesFilteredCompatibilita;
        // Lista con on filtrate, primo tentativo per compatibilità, secondo tentativo per validità, terzo tutti gli ON
        if (lists.trapTypesFilteredCompatibilita.length > 0) {
            this.trapTypesToShow = lists.trapTypesFilteredCompatibilita;
            this.trapTypesFiltered = this.trapTypesToShow;
        } else if (lists.trapTypesFilteredValidity.length > 0) {
            this.showMoreSelected = true;
            this.trapTypesToShow = lists.trapTypesFilteredValidity;
            this.trapTypesFiltered = this.trapTypesToShow;
        }

        if (this.deviceService.isCordova()) {
            this.speechRecognition.hasPermission()
                .then((hasPermission: boolean) => {
                    this.logger.debug(hasPermission);
                    if (!hasPermission) {
                        this.speechRecognition.requestPermission()
                            .then(
                                () => this.logger.debug('Granted'),
                                () => this.logger.debug('Denied')
                            )
                    }
                });
        }
    }

    public showMore(): void {
        this.showMoreSelected = !this.showMoreSelected;
        if (this.showMoreSelected) {
            this.trapTypesToShow = this.trapTypesValid;
        } else {
            this.trapTypesToShow = this.trapTypesCompatibilità;
        }
    }

    public filterList(searchTerm: string): void {
        if (this.trapTypesToShow.length > 0) {
            let tempFilterList: TrapType[] = [];
            this.logger.debug(searchTerm);
            if (searchTerm) {
                const filterInspectorsByFullName: TrapType[] = this.trapTypesToShow.filter((trapType: TrapType) => {
                    if (trapType.tipologiaTrappola && searchTerm) {
                        return (trapType.tipologiaTrappola.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
                    }
                });
                tempFilterList = filterInspectorsByFullName;
                this.trapTypesToShow = _.uniqWith(tempFilterList, _.isEqual);
            }
            else {
                this.trapTypesToShow = this.showMoreSelected ? this.trapTypesValid : this.trapTypesCompatibilità;
            }
        }
    }

    public startListening(): void {
        if (this.deviceService.isCordova()) {
            const options: SpeechRecognitionListeningOptions = {
                language: 'it-IT',
                matches: 5,
                showPartial: true
            }
            const self = this;
            this.speechRecognition.isRecognitionAvailable().then(
                (available: boolean) => {
                    this.logger.debug(available)
                    this.isListening = true;
                    self.speechRecognition.startListening(options)
                        .subscribe(
                            (matches: string[]) => {
                                this.logger.debug(matches)
                                self._searchbar.value = matches[0];
                                self.filterList(matches[0]);
                                this.isListening = false;
                                if (this.deviceService.isIos()) {
                                    this.stopListening()
                                }
                            },
                            onerror => {
                                this.logger.debug(onerror);
                                this.isListening = false;
                            }
                        )
                });
        }
        else {
            this.isListening = true;
            this._searchbar.setFocus();
            this._searchbar.value = 'sandro';
            this.filterList('sandro');
        }
    }

    public stopListening(): void {
        this.speechRecognition.stopListening();
        this.isListening = false;
    }

    public reset(event: any): void {
        this.trapTypesFiltered = this.trapTypes;
    }

    public confirm(): void {
        if (this.trapTypeSelected) {
            this.modalController.dismiss(this.trapTypeSelected);
        }

    }

    public close(): void {
        this.deviceService.closeKeyboard()
        setTimeout(() => {
            this.modalController.dismiss();
        }, 300);
    }
}
