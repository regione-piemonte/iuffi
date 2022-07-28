import { Component, NgZone } from '@angular/core';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { LoggerService } from '@core/logger';
import { ModalController, NavParams } from '@ionic/angular';
import { Comune } from '@shared/geo-utils/model/comune.model';
import { Point } from '@shared/geo-utils/model/point.model';

import { GeoUtilsService } from './../../../shared/geo-utils/geo-utils.service';

@Component({
    selector: 'map-modal',
    templateUrl: 'map-modal.page.html',
    styleUrls: ['map-modal.page.scss']
})
export class MapModalPage {
    public modal!: HTMLIonModalElement;
    public currentPosition: Coordinate | null = null;
    public latitude: number;
    public longitude: number;
    public comune: Comune | null;
    constructor(
        private logger: LoggerService,
        private navParams: NavParams,
        private geoUtilsService: GeoUtilsService,
        private modalController: ModalController,
        private zone: NgZone,
    ) {
        this.currentPosition = this.navParams.get('currentPosition') as Coordinate;
        if (this.currentPosition.latitudine === 0 || this.currentPosition.longitudine === 0) {
            this.latitude = 45.067692;
            this.longitude = 7.660817;
            this.currentPosition.latitudine = this.latitude;
            this.currentPosition.longitudine = this.longitude;
        }
        else {
            this.latitude = this.currentPosition.latitudine;
            this.longitude = this.currentPosition.longitudine;
        }

        const point = new Point(this.latitude, this.longitude);
        this.comune = this.geoUtilsService.getComuneByPoint(point);
    }

    public updateCoords(coords: Coordinate): void {
        this.zone.run(() => {
            this.currentPosition = coords;
            this.logger.debug('MapModalPage::updateCoords');
            this.latitude = coords.latitudine;
            this.longitude = coords.longitudine;
            const point = new Point(this.latitude, this.longitude);
            this.comune = this.geoUtilsService.getComuneByPoint(point);
        });
    }

    public confirm(): void {
        this.modalController.dismiss(this.currentPosition);
    }

    public close(): void {
        this.modalController.dismiss();
    }
}
