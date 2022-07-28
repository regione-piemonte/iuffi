import { Component, ViewChild } from '@angular/core';
import { DetectionService } from '@app/detections/services/detection.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { SpeechRecognition, SpeechRecognitionListeningOptions } from '@ionic-native/speech-recognition/ngx';
import { IonSearchbar, ModalController, NavParams } from '@ionic/angular';
import { TranslateService } from '@ngx-translate/core';
import { PlantSpecies } from '@shared/offline/models/plant-species.model';
import * as _ from 'lodash';

import { PlantSpeciesDetail } from './../../../shared/offline/models/plant-species.model';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'plant-species-modal',
    templateUrl: 'plant-species-modal.page.html',
    styleUrls: ['plant-species-modal.page.scss']
})
export class PlantSpeciesModalPage {
    public plantSpecies: PlantSpecies[] = [];
    public plantSpeciesFiltered: PlantSpecies[] = [];
    public plantSpeciesDetailFiltered: PlantSpeciesDetail[] = [];
    public plantSpeciesSelected!: PlantSpecies;
    public plantSpeciesDetailSelected!: PlantSpeciesDetail;
    public showMoreSelected = false;
    public isListening = false;
    private plantList: any;
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
        this.plantSpecies = this.navParams.get('plantSpeciesList') as PlantSpecies[] || [];
        this.plantSpeciesDetailSelected = this.navParams.get('plantSpeciesSelected') as PlantSpeciesDetail;

        this.plantSpeciesDetailFiltered = new Array<PlantSpeciesDetail>();

        if (this.plantSpecies.length === 0) {
            this.plantList = this.detectionService.getPlantSpecies();
            // Lista con tutte le specie
            this.plantSpecies = this.plantList.plants;
            // Lista con specie filtrate, primo tentativo per compatibilità, secondo tentativo per validità
            if (this.plantList.plantsFilteredCompatibilita.length > 0) {
                this.plantSpeciesFiltered = this.plantList.plantsFilteredCompatibilita;
            } else if (this.plantList.plantsFilteredValidity.length > 0) {
                this.plantSpeciesFiltered = this.plantList.plantsFilteredValidity;
            }
        }
        // Trasformazione delle liste in liste  di secondo livello
        this.transformSecondLevelList(this.plantSpeciesFiltered.length > 0 ? false : true);

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
        this.transformSecondLevelList(this.showMoreSelected ? true : false);
    }

    private transformSecondLevelList(all: boolean): void {
        this.plantSpeciesDetailFiltered = [];
        if (!all) {
            if (this.plantSpeciesFiltered && this.plantSpeciesFiltered.length > 0) {
                this.plantSpeciesFiltered.forEach((specie: PlantSpecies) => {
                    if (specie.listaSpecieVegetali && specie.listaSpecieVegetali.length > 0) {
                        this.plantSpeciesDetailFiltered = this.plantSpeciesDetailFiltered.concat(specie.listaSpecieVegetali);
                    }
                });
            }
        }
        else {
            if (this.plantSpecies && this.plantSpecies.length > 0) {
                this.plantSpecies.forEach((specie: PlantSpecies) => {
                    if (specie.listaSpecieVegetali && specie.listaSpecieVegetali.length > 0) {
                        this.plantSpeciesDetailFiltered = this.plantSpeciesDetailFiltered.concat(specie.listaSpecieVegetali);
                    }
                });
            }
        }
        this.orderListAlpha();
    }

    public filterList(searchTerm: string): void {
        if (this.plantList.plantsFilteredCompatibilita.length > 0) {
            this.plantSpeciesFiltered = this.plantList.plantsFilteredCompatibilita;
        } else if (this.plantList.plantsFilteredValidity.length > 0) {
            this.plantSpeciesFiltered = this.plantList.plantsFilteredValidity;
        }
        this.transformSecondLevelList(this.showMoreSelected ? true : false);
        if (this.plantSpeciesDetailFiltered.length > 0) {
            let tempFilterList: PlantSpeciesDetail[] = [];
            this.logger.debug(searchTerm);
            if (searchTerm) {
                const filterInspectorsByName: PlantSpeciesDetail[] = this.plantSpeciesDetailFiltered.filter((plant: PlantSpeciesDetail) => {
                    if (plant.nomeVolgare && searchTerm) {
                        return (plant.nomeVolgare.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
                    }
                });
                tempFilterList = filterInspectorsByName;
                this.plantSpeciesDetailFiltered = _.uniqWith(tempFilterList, _.isEqual);
            }

            this.orderListAlpha();
        }

    }

    private orderListAlpha(): void {
        if (this.plantSpeciesDetailFiltered && this.plantSpeciesDetailFiltered.length > 0) {
            this.plantSpeciesDetailFiltered = this.plantSpeciesDetailFiltered.sort((a, b) => {
                if (a.nomeVolgare < b.nomeVolgare) { return -1; }
                if (a.nomeVolgare > b.nomeVolgare) { return 1; }
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
    }

    public stopListening(): void {
        this.speechRecognition.stopListening();
        this.isListening = false;
    }

    public reset(event: any): void {
        // Lista con tutte le specie
        this.plantSpecies = this.plantList.plants;
        // Lista con specie filtrate, primo tentativo per compatibilità, secondo tentativo per validità
        if (this.plantList.plantsFilteredCompatibilita.length > 0) {
            this.plantSpeciesFiltered = this.plantList.plantsFilteredCompatibilita;
        } else if (this.plantList.plantsFilteredValidity.length > 0) {
            this.plantSpeciesFiltered = this.plantList.plantsFilteredValidity;
        }
        // Trasformazione delle liste in liste  di secondo livello
        this.transformSecondLevelList(this.showMoreSelected ? true : false);
    }

    public confirm(plantSpeciesDetail: PlantSpeciesDetail): void {
        this.plantSpeciesDetailSelected = plantSpeciesDetail;
        this.modalController.dismiss(this.plantSpeciesDetailSelected);
    }

    public close(): void {
        this.deviceService.closeKeyboard()
        setTimeout(() => {
            this.modalController.dismiss();
        }, 300);
    }
}
