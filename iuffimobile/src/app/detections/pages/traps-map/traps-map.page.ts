import { Component } from '@angular/core';
import { MapData } from '@app/detections/components/leaflet-map/leaflet-map.component';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { Detection } from '@app/detections/models/detection.model';
import { Trap } from '@app/detections/models/trap.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { TrapService } from '@app/detections/services/trap.service';
import { LoggerService } from '@core/logger';
import { RootNavController } from '@core/split-view';
import { ModalController, NavParams } from '@ionic/angular';

import { TrapMaintenanceRemovePage } from '../trap-maintenance-remove/trap-maintenance-remove.page';
import { DeviceService } from './../../../core/device/services/device.service';
import { TrapBottomModalPage } from './../trap-bottom-modal/trap-bottom-modal.page';

declare const document: any;
@Component({
    selector: 'traps-map',
    templateUrl: 'traps-map.page.html',
    styleUrls: ['traps-map.page.scss']
})
export class TrapsMapPage {
    public modal!: HTMLIonModalElement;
    public currentPosition: Coordinate | null = null;
    public action: 'removal' | 'maintenance';
    public trapping: Trapping;
    public trappingHistory: Trapping[] = [];
    private detection: Detection;
    public traps: Trap[] = [];
    private actionRoot: string;
    constructor(
        private logger: LoggerService,
        private modalController: ModalController,
        private navParams: NavParams,
        private deviceService: DeviceService,
        private trapService: TrapService,
        private rootNavController: RootNavController,
    ) {
        this.trapping = this.navParams.get('trapping');
        this.action = this.navParams.get('action') as 'removal' | 'maintenance';
        this.actionRoot = this.navParams.get('actionRoot') as string;
        this.detection = this.navParams.get('detection') as Detection;
        this.currentPosition = new Coordinate(this.trapping.latitudine, this.trapping.longitudine);
        if (this.currentPosition.latitudine === 0 || this.currentPosition.longitudine === 0) {
            this.currentPosition.latitudine = 45.067692;
            this.currentPosition.longitudine = 7.660817;
        }
        // const mapData: MapData = {
        //     position: this.currentPosition,
        //     radius: '1500'
        // }
        // this.getTraps(mapData);
    }

    public getTrap(trap: Trap): void {
        this.logger.debug('TrapsMapPage::getTrap', trap);
    }

    public getTraps(mapData: MapData): void {
        this.deviceService.showLoading();
        this.trapService.getTrapsByPosition(mapData.position, mapData.radius).then(
            (traps: Trap[]) => {
                this.deviceService.hideLoading();
                this.traps = traps;
            }
        ).catch(e => {
            this.deviceService.hideLoading();
            this.logger.error(e);
        });
    }

    public async openTrapBottomModal(trap: Trap): Promise<void> {
        this.logger.debug('TrapsMapPage::openTrapBottomModal');
        this.modal = await this.modalController.create({
            component: TrapBottomModalPage,
            componentProps: {
                trap: trap
            },
            cssClass: 'bottom-modal'
        });

        this.modal.onWillDismiss().then((value: any) => {
            this.logger.debug('TrapsMapPage::openTrapBottomModal::onWillDismiss');
            if (value.data) {
                this.logger.debug(value.data);
                this.trapping.trappola = value.data;
                this.trapService.getTrapHistory(value.data.idTrappola as number)
                    .then(
                        (res: Trapping[]) => {
                            this.deviceService.hideLoading();
                            this.trappingHistory = res;
                            this.goForward();
                        }
                    )
                    .catch((err: any) => {
                        this.deviceService.hideLoading();
                        this.deviceService.alert('SEARCH_TRAP_ERROR')
                        this.logger.debug(err);
                    })
            }
        });
        return await this.modal.present();
    }

    public goForward(): void {
        const self = this;
        this.modal.dismiss().then(
            () => {
                // document.getElementById('map').outerHTML = '';
                self.rootNavController.push(
                    TrapMaintenanceRemovePage,
                    {
                        trapping: self.trapping,
                        detection: self.detection,
                        trappingHistory: self.trappingHistory,
                        action: self.action,
                        actionRoot: self.actionRoot
                    }, {
                        animated: true,
                        direction: 'forward',
                    });
            }
        );

    }
}
