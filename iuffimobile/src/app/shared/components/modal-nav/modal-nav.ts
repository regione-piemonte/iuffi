import { Component, ViewChild } from '@angular/core';
import { IonNav, ModalController, NavParams } from '@ionic/angular';

@Component({
    selector: 'modal-nav-page',
    templateUrl: 'modal-nav.html',
    styleUrls: ['./modal-nav.scss'],
})
export class ModalNavPage {
    @ViewChild('content') public ionNav!: IonNav;

    constructor(
        public navParams: NavParams,
        public modalCtrl: ModalController
    ) { }

    ngAfterViewInit(): void {
        const modalPage = this.navParams.get('page');
        const modalParams = this.navParams.get('params') || {};
        this.ionNav.setRoot(modalPage, modalParams);
    }

    public dismissModal(val?: any): void {
        this.modalCtrl.dismiss(val);
    }
}
