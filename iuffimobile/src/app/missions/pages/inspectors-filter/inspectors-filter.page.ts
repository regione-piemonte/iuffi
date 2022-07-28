import { Component, ViewChild } from '@angular/core';
import { AuthService, User } from '@core/auth';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { SpeechRecognition, SpeechRecognitionListeningOptions } from '@ionic-native/speech-recognition/ngx';
import { IonSearchbar, ModalController, NavParams } from '@ionic/angular';
import * as _ from 'lodash';

import { InspectorOptions } from './../../models/inspector.model';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'inspectors-filter',
    templateUrl: 'inspectors-filter.page.html',
    styleUrls: ['inspectors-filter.page.scss']
})
export class InspectorsFilterPage {
    public inspectors: InspectorOptions[] = [];
    public inspectorsFiltered: InspectorOptions[] = [];
    public isListening = false;
    public inspectorCf: string;
    @ViewChild('searchbar') private _searchbar!: IonSearchbar;
    constructor(
        private logger: LoggerService,
        private navParams: NavParams,
        private modalController: ModalController,
        private speechRecognition: SpeechRecognition,
        private authService: AuthService,
        private deviceService: DeviceService) {
        this.inspectors = this.navParams.get('inspectorList') as InspectorOptions[];
        this.inspectorCf = this.navParams.get('inspectorCf') as string;
        this.inspectorsFiltered = this.inspectors;

        if (this.deviceService.isCordova()) {
            // Escludo l'inspettore in sessione per filtrare gli inspettori in affiancamento
            const user: User | null = this.authService.getUser();
            if (user && this.inspectorsFiltered.length > 0) {
                this.inspectorsFiltered = this.inspectorsFiltered.filter(el => {
                    return el.cfAnagraficaEst != user.fiscalCode;
                })
            }

            // Devo escludere anche l'ispettore TITOLARE della missione dalla lista ispettori
            if (!this.inspectorCf)
                this.inspectorsFiltered = this.inspectorsFiltered.filter(el => {
                    return el.cfAnagraficaEst != this.inspectorCf;
                });

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

    public filterList(searchTerm: string): void {
        let tempFilterList: InspectorOptions[] = [];
        this.logger.debug(searchTerm);
        if (searchTerm) {
            const filterInspectorsByName: InspectorOptions[] = this.inspectors.filter((inspector: InspectorOptions) => {
                if (inspector.nome && searchTerm) {
                    return (inspector.nome.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
                }
            });
            tempFilterList = filterInspectorsByName;
            const filterInspectorsBySurname: InspectorOptions[] = this.inspectors.filter((inspector: InspectorOptions) => {
                if (inspector.cognome && searchTerm) {
                    return (inspector.cognome.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
                }
            });
            tempFilterList = tempFilterList.concat(filterInspectorsBySurname);
            this.inspectorsFiltered = _.uniqWith(tempFilterList, _.isEqual);
        }
        else {
            this.inspectorsFiltered = this.inspectors;
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
        this.inspectorsFiltered = this.inspectors;
    }

    public confirm(): void {
        this.modalController.dismiss(this.inspectorsFiltered);
    }

    public close(): void {
        this.deviceService.closeKeyboard()
        setTimeout(() => {
            this.modalController.dismiss();
        }, 300);
    }
}
