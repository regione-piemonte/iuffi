import { Component, ViewChild } from '@angular/core';
import { DetectionService } from '@app/detections/services/detection.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { SpeechRecognition, SpeechRecognitionListeningOptions } from '@ionic-native/speech-recognition/ngx';
import { IonSearchbar, ModalController, NavParams } from '@ionic/angular';
import { TranslateService } from '@ngx-translate/core';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';
import { PlantSpeciesDetail } from '@shared/offline/models/plant-species.model';
import * as _ from 'lodash';

import { HarmfulOrganismOptions } from './../../../shared/offline/models/harmful-organism.model';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'harmful-organism-modal',
    templateUrl: 'harmful-organism-modal.page.html',
    styleUrls: ['harmful-organism-modal.page.scss']
})
export class HarmfulOrganismModalPage {
    public harmfulOrganisms: HarmfulOrganismOptions[] = [];
    public harmfulOrganismsToShow: HarmfulOrganismOptions[] = [];
    public harmfulOrganismsAdded!: HarmfulOrganism[];
    public harmfulOrganismsFiltered!: HarmfulOrganismOptions[];
    public harmfulOrganismsFilter!: number[];
    public singleSelection = false;
    public showMoreSelected = false;
    public isListening = false;
    public isRelatedToEmergency = false;
    public currentPlantSpecies: PlantSpeciesDetail;
    public harmfulOrganismSelected!: HarmfulOrganismOptions;
    public harmfulOrganismsValid!: HarmfulOrganismOptions[];
    private onList: any;

    @ViewChild('searchbar') private _searchbar!: IonSearchbar;
    constructor(
        private logger: LoggerService,
        private navParams: NavParams,
        private modalController: ModalController,
        private speechRecognition: SpeechRecognition,
        private deviceService: DeviceService,
        private detectionService: DetectionService,
        public translateService: TranslateService
    ) {
        this.currentPlantSpecies = this.navParams.get('plantSpecies') as PlantSpeciesDetail;
        this.harmfulOrganismsAdded = this.navParams.get('harmfulOrganismsAdded') as HarmfulOrganism[];
        this.harmfulOrganismsFilter = this.navParams.get('harmfulOrganismsFilter') as number[];
        this.singleSelection = this.navParams.get('singleSelection') as boolean;
        this.isRelatedToEmergency = this.navParams.get('isRelatedToEmergency') as boolean;

        this.onList = this.detectionService.getHarmfulOrganisms(this.currentPlantSpecies.idSpecieVegetale);

        // Filtro delle liste
        this.reset();

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

    private _convertToOptions(list: HarmfulOrganism[]): HarmfulOrganismOptions[] {
        const options: HarmfulOrganismOptions[] = list.map((i: HarmfulOrganism) => {
            const option: HarmfulOrganismOptions = new HarmfulOrganismOptions(i);
            if (this.harmfulOrganismsAdded && this.harmfulOrganismsAdded.length > 0) {
                this.harmfulOrganismsAdded.map((t: HarmfulOrganism) => {
                    if (t.idOrganismoNocivo === option.idOrganismoNocivo) {
                        option.selected = true;
                    }
                })
            }
            return option;
        });
        return options;
    }

    public showMore(): void {
        this.showMoreSelected = !this.showMoreSelected;
        if (this.showMoreSelected) {
            this.harmfulOrganismsToShow = this.harmfulOrganismsValid;
        } else {
            this.harmfulOrganismsToShow = this.harmfulOrganismsFiltered;
        }
    }

    public filterList(searchTerm: string): void {
        if (!searchTerm) {
            this.reset();
            return;
        }
        // Lista con on filtrati, primo tentativo per compatibilità, secondo tentativo per validità, terzo tutti gli ON
        if (this.onList.onFilteredCompatibilita.length > 0) {
            this.harmfulOrganismsToShow = this._convertToOptions(this.onList.onFilteredCompatibilita);
            this.harmfulOrganismsFiltered = this.harmfulOrganismsToShow;
        } else if (this.onList.onFilteredValidity.length > 0) {
            this.harmfulOrganismsToShow = this._convertToOptions(this.onList.onFilteredValidity);
            this.harmfulOrganismsFiltered = this.harmfulOrganismsToShow;
        } else if (this.harmfulOrganisms.length > 0) {
            this.harmfulOrganismsToShow = this.harmfulOrganisms;
        }
        // Esiste un filtro in input per gli organismi selezionabili?
        if (this.harmfulOrganismsFilter && this.harmfulOrganismsFilter.length > 0 && this.harmfulOrganisms.length > 0) {
            this.harmfulOrganismsToShow = this.harmfulOrganisms.filter(onF => {
                return (this.harmfulOrganismsFilter.find(on => { return on === onF.idOrganismoNocivo }));
            })
        }
        if (this.showMoreSelected) {
            this.harmfulOrganismsToShow = this.harmfulOrganismsValid;
        } else {
            this.harmfulOrganismsToShow = this.harmfulOrganismsFiltered;
        }
        if (this.harmfulOrganismsToShow.length > 0) {
            let tempFilterList: HarmfulOrganismOptions[] = [];
            this.logger.debug(searchTerm);
            if (searchTerm) {
                const filterInspectorsByFullName: HarmfulOrganismOptions[] = this.harmfulOrganismsToShow.filter((organism: HarmfulOrganismOptions) => {
                    if (organism.nomeCompleto && searchTerm) {
                        return (organism.nomeCompleto.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
                    }
                });
                tempFilterList = filterInspectorsByFullName;
                this.harmfulOrganismsToShow = _.uniqWith(tempFilterList, _.isEqual);
            }
            else {
                this.harmfulOrganismsToShow = this.harmfulOrganisms;
            }
            this.orderListAlpha();
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
        this.orderListAlpha();
    }

    private orderListAlpha(): void {
        // Filtro per emergenza
        if (this.isRelatedToEmergency) {
            const emergencyOn = this.harmfulOrganismsToShow.filter(on => {
                return on.flagEmergenza === 'S'
            });
            if (emergencyOn && emergencyOn.length > 0) {
                this.harmfulOrganismsToShow = emergencyOn;
            }
        }
        // Ordino per nome
        if (this.harmfulOrganismsToShow && this.harmfulOrganismsToShow.length > 0) {
            this.harmfulOrganismsToShow = this.harmfulOrganismsToShow.sort((a, b) => {
                if (a.nomeLatino < b.nomeLatino) { return -1; }
                if (a.nomeLatino > b.nomeLatino) { return 1; }
                return 0;
            })
        }
    }

    public stopListening(): void {
        this.speechRecognition.stopListening();
        this.isListening = false;
    }

    public reset(event?: any): void {
        this.harmfulOrganisms = this._convertToOptions(this.onList.on);
        this.harmfulOrganismsValid = this._convertToOptions(this.onList.onFilteredValidity);
        // Lista con on filtrati, primo tentativo per compatibilità, secondo tentativo per validità, terzo tutti gli ON
        if (this.onList.onFilteredCompatibilita.length > 0) {
            this.harmfulOrganismsToShow = this._convertToOptions(this.onList.onFilteredCompatibilita);
            this.harmfulOrganismsFiltered = this.harmfulOrganismsToShow;
        } else if (this.onList.onFilteredValidity.length > 0) {
            this.harmfulOrganismsToShow = this._convertToOptions(this.onList.onFilteredValidity);
            this.harmfulOrganismsFiltered = this.harmfulOrganismsToShow;
        } else if (this.harmfulOrganisms.length > 0) {
            this.harmfulOrganismsToShow = this.harmfulOrganisms;
        }
        // Esiste un filtro in input per gli organismi selezionabili?
        if (this.harmfulOrganismsFilter && this.harmfulOrganismsFilter.length > 0 && this.harmfulOrganisms.length > 0) {
            this.harmfulOrganismsToShow = this.harmfulOrganisms.filter(onF => {
                return (this.harmfulOrganismsFilter.find(on => { return on === onF.idOrganismoNocivo }));
            })
        }
        if (this.showMoreSelected) {
            this.harmfulOrganismsToShow = this.harmfulOrganismsValid;
        } else {
            this.harmfulOrganismsToShow = this.harmfulOrganismsFiltered;
        }
        this.orderListAlpha();
    }

    public confirm(): void {
        this.harmfulOrganismsAdded = [];
        if (!this.singleSelection) {
            this.harmfulOrganismsToShow.forEach((i: HarmfulOrganismOptions) => {
                if (i.selected) {
                    this.harmfulOrganismsAdded.push(new HarmfulOrganism(i));
                }
            });
        } else if (this.singleSelection && this.harmfulOrganismSelected) {
            this.harmfulOrganismsAdded.push(this.harmfulOrganismSelected);
        }
        if (this.harmfulOrganismsAdded) {
            this.modalController.dismiss(this.harmfulOrganismsAdded);
        }
    }

    public close(): void {
        this.deviceService.closeKeyboard()
        setTimeout(() => {
            this.modalController.dismiss();
        }, 300);
    }
}
