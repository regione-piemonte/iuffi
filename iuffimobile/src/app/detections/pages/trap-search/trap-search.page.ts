import { Component } from '@angular/core';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { Detection } from '@app/detections/models/detection.model';
import { Trap } from '@app/detections/models/trap.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { TrapService } from '@app/detections/services/trap.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { RootNavController } from '@core/split-view';
import { NavParams } from '@ionic/angular';
import { GeoUtilsService } from '@shared/geo-utils/geo-utils.service';

import { TrapMaintenanceRemovePage } from '../trap-maintenance-remove/trap-maintenance-remove.page';
import { TrapsMapPage } from '../traps-map/traps-map.page';

/* eslint-disable indent */
@Component({
    selector: 'trap-search',
    templateUrl: 'trap-search.page.html',
    styleUrls: ['trap-search.page.scss'],
})
export class TrapSearchPage {
    public action: 'removal' | 'maintenance';
    public trap: Trap | undefined;
    public trapping: Trapping;
    public trappingHistory: Trapping[] = [];
    public sfrCode = '';
    public detection: Detection;
    private missionId: string;
    private actionRoot: string;

    constructor(
        private rootNavController: RootNavController,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private navParams: NavParams,
        private trapService: TrapService,
        private geoUtilsService: GeoUtilsService,
        private detectionService: DetectionService
    ) {
        this.trapping = this.navParams.get('trapping') as Trapping;
        this.detection = this.navParams.get('detection') as Detection;
        this.missionId = this.navParams.get('missionId') as string;
        this.action = this.navParams.get('action') as 'removal' | 'maintenance';
        this.actionRoot = this.navParams.get('actionRoot') as string;
    }

    public searchTrapByCode(): void {
        if (this.sfrCode) {
            this.deviceService.showLoading();
            this.trapService.getTrapByCode(this.sfrCode)
                .then(
                    (res: Trap) => {
                        this.deviceService.hideLoading();
                        this.logger.debug(res);
                        this.trap = res;
                        this.trapping.trappola = res;
                    })
                .catch((err: any) => {
                    this.deviceService.hideLoading();
                    this.deviceService.alert('SEARCH_TRAP_ERROR')
                    this.logger.debug(err);
                });

        }
    }

    public searchByHistory(trapId: number): void {
        this.deviceService.showLoading();
        this.trapService.getTrapHistory(trapId)
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
            });
    }

    public goForward(): void {
        this.rootNavController.push(
            TrapMaintenanceRemovePage,
            {
                trapping: this.trapping,
                detection: this.detection,
                trappingHistory: this.trappingHistory,
                missionId: this.missionId,
                action: this.action,
                actionRoot: this.actionRoot
            }, {
            animated: true,
            direction: 'forward',
        });
    }

    public goToMap(): void {
        this.deviceService.showLoading();
        this.sfrCode = '';
        this.trap = undefined;
        this.geoUtilsService.getCurrentPosition().then((res: Coordinate) => {
            this.logger.debug(res);
            this.trapping.latitudine = res.latitudine;
            this.trapping.longitudine = res.longitudine;
            this.rootNavController.push(
                TrapsMapPage, {
                trapping: this.trapping,
                action: this.action,
                detection: this.detection,
                actionRoot: this.actionRoot
            }, {
                animated: true,
                direction: 'forward',
            });
            setTimeout(() => { this.deviceService.hideLoading() }, 100);
        }).catch(err => {
            setTimeout(() => { this.deviceService.hideLoading() }, 100);
            this.deviceService.alert('GET_CURRENT_POSITION_ERROR');
            this.logger.debug(err);
        });

    }
}
