import { Component, ViewChild } from '@angular/core';
import { PlantSpeciesAviv } from '@app/detections/models/plant-species-aviv.model';
import { IonInfiniteScroll, ModalController, NavParams } from '@ionic/angular';

declare const Speech: { initRecognition: any };
@Component({
    selector: 'aviv-plant-species-modal',
    templateUrl: 'aviv-plant-species-modal.page.html',
    styleUrls: ['aviv-plant-species-modal.page.scss']
})
export class AvivPlantSpeciesModalPage {
    public plantSpecies: PlantSpeciesAviv[] = [];
    public plantSpeciesScroll: PlantSpeciesAviv[] = [];

    @ViewChild(IonInfiniteScroll) public infiniteScroll: IonInfiniteScroll | undefined;

    constructor(
        private navParams: NavParams,
        private modalController: ModalController
    ) {
        this.plantSpecies = this.navParams.get('plantSpecies');
        this.plantSpeciesScroll = this.plantSpecies.slice(this.plantSpeciesScroll.length, this.plantSpeciesScroll.length + 10);
    }

    public loadData(event: any): void {
        setTimeout(() => {
            //10 item per volta
            this.plantSpeciesScroll = this.plantSpeciesScroll.concat(this.plantSpecies.slice(this.plantSpeciesScroll.length, this.plantSpeciesScroll.length + 10));
            event.target.complete();
            // Raggiunto il limite lista
            if (this.plantSpecies.length === this.plantSpeciesScroll.length) {
                event.target.disabled = true;
            }
        }, 500);
    }

    public confirm(): void {
        this.modalController.dismiss();
    }

    public close(): void {
        this.modalController.dismiss();
    }
}
