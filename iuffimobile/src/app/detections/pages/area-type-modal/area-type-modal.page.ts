import { Component, ViewChild } from '@angular/core';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { SpeechRecognition, SpeechRecognitionListeningOptions } from '@ionic-native/speech-recognition/ngx';
import { IonSearchbar, ModalController, NavParams } from '@ionic/angular';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';
import * as _ from 'lodash';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'area-type-modal',
    templateUrl: 'area-type-modal.page.html',
    styleUrls: ['area-type-modal.page.scss']
})
export class AreaTypeModalPage {
    public areaTypes: AreaTypeLocal[] = [];
    public areaTypeFiltered: AreaTypeLocal[] = [];
    public areaTypeselected!: AreaTypeLocal;
    public isListening = false;
    @ViewChild('searchbar') private _searchbar!: IonSearchbar;
    constructor(
        private logger: LoggerService,
        private navParams: NavParams,
        private modalController: ModalController,
        private speechRecognition: SpeechRecognition,
        private deviceService: DeviceService) {
        this.areaTypes = this.navParams.get('areaTypeList') as AreaTypeLocal[];
        this.areaTypeFiltered = this.areaTypes;
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
        this.orderListAlpha();
    }

    public filterList(searchTerm: string): void {
        let tempFilterList: AreaTypeLocal[] = [];
        this.logger.debug(searchTerm);
        if (searchTerm) {
            const filterAreaTypeByDescription: AreaTypeLocal[] = this.areaTypes.filter((areaType: AreaTypeLocal) => {
                if (areaType.descrizione && searchTerm) {
                    return (areaType.descrizione.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
                }
            });
            tempFilterList = filterAreaTypeByDescription;
            this.areaTypeFiltered = _.uniqWith(tempFilterList, _.isEqual);
        }
        else {
            this.areaTypeFiltered = this.areaTypes;
        }
        this.orderListAlpha();
    }

    private orderListAlpha(): void {
        let orderedList;
        if (this.areaTypeFiltered && this.areaTypeFiltered.length > 0) {
            orderedList = this.areaTypeFiltered.sort((a, b) => {
                if (a.descrizione.trim() < b.descrizione.trim()) { return -1; }
                if (a.descrizione.trim() > b.descrizione.trim()) { return 1; }
                return 0;
            });
        }
        this.areaTypeFiltered = [];
        // Filtro per possibili id duplicati
        orderedList?.forEach(el => {
            const found = this.areaTypeFiltered.find(f => { return f.id === el.id });
            if (!found) {
                this.areaTypeFiltered.push(el);
            }
        });
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
                                this.orderListAlpha();
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
            this._searchbar.value = 'soia';
            this.filterList('soia');
            this.orderListAlpha();
        }
    }

    public stopListening(): void {
        this.speechRecognition.stopListening();
        this.isListening = false;
    }

    public reset(event: any): void {
        this.areaTypeFiltered = this.areaTypes;
    }

    public confirm(areaTypeselected: AreaTypeLocal): void {
        this.areaTypeselected = areaTypeselected;
        this.modalController.dismiss(this.areaTypeselected);
    }

    public close(): void {
        this.deviceService.closeKeyboard()
        setTimeout(() => {
            this.modalController.dismiss();
        }, 300);
    }
}
