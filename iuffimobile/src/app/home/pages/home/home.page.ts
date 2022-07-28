import { Component, ViewChild } from '@angular/core';
import { SearchBox } from '@app/home/components/search-box/search-box.component';
import { Mission, MissionStatus } from '@app/missions/models/mission.model';
import { MissionDetailPage } from '@app/missions/pages/mission-detail/mission-detail.page';
import { NewMissionPage } from '@app/missions/pages/new-mission/new-mission.page';
import { MissionService } from '@app/missions/services/mission.service';
import { AppError } from '@core/error';
import { RootNavController } from '@core/split-view';
import { AutoUnsubscribe } from '@core/utils';
import { GeoUtilsService } from '@shared/geo-utils/geo-utils.service';
import { OfflineService } from '@shared/offline/services/offline.service';
import * as Moment from 'moment';
import { takeUntil } from 'rxjs/operators';
import { DeviceService } from './../../../core/device/services/device.service';
import { LoggerService } from './../../../core/logger/services/logger.service';
import { HomeService } from './../../services/home.service';

@Component({
    selector: 'app-home',
    templateUrl: 'home.page.html',
    styleUrls: ['home.page.scss'],
})
export class HomePage extends AutoUnsubscribe {
    @ViewChild('searchBox') private searchBox: SearchBox | undefined

    public missionList: Mission[] = Array(16).fill(null);
    public selectClass = 'slide-out-top';
    public isOpen = false;
    constructor(
        private rootNavController: RootNavController,
        private offlineService: OfflineService,
        private missionService: MissionService,
        private homeService: HomeService,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private geoUtilsService: GeoUtilsService
    ) {
        super();
        this.offlineService.initializeDb();
        this.geoUtilsService.initCompleted;
        this.homeService.getInspectors();

        this.deviceService.networkStatusChanges$
            .pipe(takeUntil(this.destroy$))
            .subscribe(isOnline => {
                if (isOnline) {
                    this.missionList = Array(16).fill(null);
                    this.getMissionList();
                    this._missionSynchronizationCheck();
                }
            });
    }

    ionViewDidEnter(): void {
        this.missionList = Array(16).fill(null);
        this.getMissionList();
    }

    private _missionSynchronizationCheck(): void {
        const founded = this.missionService.searchMissionByStatus(MissionStatus.TO_SYNCHRONIZE);
        if (founded.length > 0) {
            this.deviceService.confirm('MISSION_SYNCHRONIZATION_CONFIRM', {
                buttons: [
                    {
                        text: 'RESET'
                    },
                    {
                        text: 'CONFIRM',
                        handler: () => {
                            const missionToSync = founded.map(mission => {
                                return this.missionService.synchronizeMission(mission);
                            });
                            Promise.all(missionToSync).then(() => {
                                this.deviceService.alert('MISSION_SYNCHRONIZATION_SUCCESS', {
                                    handler: () => {
                                        this.missionList = Array(16).fill(null);
                                        this.getMissionList();
                                    }
                                });
                            }).catch(() => {
                                this.deviceService.alert('MISSION_SYNCHRONIZATION_ERROR');
                            });

                        }
                    }]
            })
        }
    }

    public getMissionList(event?: any): void {
        this.missionService.fetchMissionList().then(
            () => {
                // Mese corrente
                const date = new Date(), y = date.getFullYear(), m = date.getMonth();
                const fromDate = Moment(new Date(y, m, 1)).set({ hour: 0, minute: 0, second: 0, millisecond: 0 }).toDate().getTime()
                const toDate = Moment(new Date(y, m + 1, 0)).set({ hour: 0, minute: 0, second: 0, millisecond: 0 }).toDate().getTime()
                setTimeout(() => {
                    this.missionList = this.missionService.getMissionList(fromDate, toDate);
                    // Refresher
                    if (event && this.searchBox) {
                        this.searchBox.close();
                        event.target.complete();
                    }
                }, 1000);

            },
            (err: AppError) => {
                this.logger.error(err);
                this.deviceService.alert(err.message);
            }
        );
    }

    public openNewMission(): void {
        this.rootNavController.push(NewMissionPage, { mission: new Mission() });
    }

    public openMissionDetail(missionId: number, missionDate: number): void {
        this.rootNavController.push(MissionDetailPage, { missionId: missionId, missionDate: missionDate });
    }

    public toggle(value: boolean): void {
        this.isOpen = value;
    }

    public searchByDate(event: any): void {
        this.logger.debug(event);
        this.missionList = Array(16).fill(null);
        const fromDate = event.fromDate ? Moment(event.fromDate).set({ hour: 0, minute: 0, second: 0, millisecond: 0 }).toDate().getTime() : undefined;
        const toDate = event.toDate ? Moment(event.toDate).set({ hour: 0, minute: 0, second: 0, millisecond: 0 }).toDate().getTime() : undefined;
        setTimeout(() => {
            this.missionList = this.missionService.getMissionList(fromDate, toDate);
        }, 1000);
    }

    public resetSearch(event: any): void {
        this.logger.debug(event);
        this.missionList = Array(16).fill(null);
        setTimeout(() => {
            this.missionList = this.missionService.getMissionList();
        }, 1000);
    }
}
