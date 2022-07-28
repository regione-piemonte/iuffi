import { Component, ViewChild } from '@angular/core';
import { DetectionService } from '@app/detections/services/detection.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { SpeechRecognition, SpeechRecognitionListeningOptions } from '@ionic-native/speech-recognition/ngx';
import { IonSearchbar, ModalController, NavParams } from '@ionic/angular';
import { TranslateService } from '@ngx-translate/core';
import { PlantSpeciesDetail } from '@shared/offline/models/plant-species.model';
import { SampleType } from '@shared/offline/models/sample-type.model';
import * as _ from 'lodash';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'sample-type-modal',
    templateUrl: 'sample-type-modal.page.html',
    styleUrls: ['sample-type-modal.page.scss']
})
export class SampleTypeModalPage {
    public sampleTypes: SampleType[] = [];
    public sampleTypesValid: SampleType[] = [];
    public sampleTypesFiltered: SampleType[] = [];
    public sampleTypesToShow: SampleType[] = [];
    public sampleTypeSelected: SampleType;
    public currentPlantSpecies: PlantSpeciesDetail;
    public currentHarmfulOrganisms: number[];
    public showMoreSelected = false;
    public isListening = false;
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
        this.currentHarmfulOrganisms = this.navParams.get('harmfulOrganisms') as number[];
        this.sampleTypeSelected = this.navParams.get('sampleType') as SampleType;

        const lists = this.detectionService.getSampleTypes(this.currentPlantSpecies.idSpecieVegetale, this.currentHarmfulOrganisms);

        // Lista con tutti i sample types
        this.sampleTypes = lists.sampleTypes;
        this.sampleTypesValid = lists.sampleTypesFilteredValidity;
        // Lista con on filtrate, primo tentativo per compatibilità, secondo tentativo per validità, terzo tutti gli ON

        if (lists.sampleTypesFilteredCompatibilita.length > 0) {
            this.sampleTypesToShow = lists.sampleTypesFilteredCompatibilita;
            this.sampleTypesFiltered = this.sampleTypesToShow;
        } else if (lists.sampleTypesFilteredValidity.length > 0) {
            this.sampleTypesToShow = lists.sampleTypesFilteredValidity;
            this.sampleTypesFiltered = this.sampleTypesToShow;
        } else if (this.sampleTypes.length > 0) {
            this.sampleTypesToShow = this.sampleTypes;
        }
        // Se c' almeno un ON che non rientra nella compatibilità, di default mostro tutti i campioni
        if (lists.atLeastOneNotFound) {
            this.showMore();
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
        this.orderListAlpha();
    }

    public showMore(): void {
        this.showMoreSelected = !this.showMoreSelected;
        if (this.showMoreSelected) {
            this.sampleTypesToShow = this.sampleTypesValid;
        } else {
            this.sampleTypesToShow = this.sampleTypesFiltered;
        }
    }

    public filterList(searchTerm: string): void {
        if (this.sampleTypes.length > 0) {
            let tempFilterList: SampleType[] = [];
            this.logger.debug(searchTerm);
            if (searchTerm) {
                const filterInspectorsByFullName: SampleType[] = this.sampleTypes.filter((sampleType: SampleType) => {
                    if (sampleType.tipologiaCampione && searchTerm) {
                        return (sampleType.tipologiaCampione.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
                    }
                });
                tempFilterList = filterInspectorsByFullName;
                this.sampleTypesFiltered = _.uniqWith(tempFilterList, _.isEqual);
            }
            else {
                this.sampleTypesFiltered = this.sampleTypes;
            }
            this.orderListAlpha();
        }
    }

    private orderListAlpha(): void {
        if (this.sampleTypesToShow && this.sampleTypesToShow.length > 0) {
            this.sampleTypesToShow = this.sampleTypesToShow.sort((a, b) => {
                if (a.tipologiaCampione < b.tipologiaCampione) { return -1; }
                if (a.tipologiaCampione > b.tipologiaCampione) { return 1; }
                return 0;
            })
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

    public stopListening(): void {
        this.speechRecognition.stopListening();
        this.isListening = false;
    }

    public reset(event: any): void {
        this.sampleTypesFiltered = this.sampleTypes;
    }

    public confirm(sampleTypeSelected: SampleType): void {
        this.sampleTypeSelected = sampleTypeSelected;
        if (this.sampleTypeSelected) {
            this.modalController.dismiss(this.sampleTypeSelected);
        }

    }

    public close(): void {
        this.deviceService.closeKeyboard()
        setTimeout(() => {
            this.modalController.dismiss();
        }, 300);
    }
}
