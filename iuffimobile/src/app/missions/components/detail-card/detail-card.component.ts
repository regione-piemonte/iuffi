import { Component, Input, Output } from '@angular/core';
import { DetailAction } from '@app/missions/models/detail-action.enum';
import * as Moment from 'moment';
import { Subject } from 'rxjs';

import { Mission, MissionStatus } from '../../models/mission.model';
import { DeviceService } from './../../../core/device/services/device.service';

@Component({
    selector: 'detail-card-component',
    templateUrl: 'detail-card.component.html',
    styleUrls: ['detail-card.component.scss']
})
export class DetailCardComponent {
    public isSkeleton = true;
    public moment: any = Moment();
    public canDelete = true;
    public canModify = true;
    public MissionStatus = MissionStatus;
    @Input() public mission!: Mission;
    @Output() public action = new Subject<string>();

    constructor(private deviceService: DeviceService) { }
    ngOnInit(): void {

    }

    ngOnChanges(): void {
        this.isSkeleton = (this.mission === null);

        if (this.mission) {
            const now = Moment();
            const missionDate = Moment(new Date(this.mission.dataMissione));
            if (this.mission.stato === MissionStatus.SYNCHRONIZED) {
                if (missionDate.isBefore(now, 'day')) {
                    this.canDelete = false;
                    this.canModify = false;
                }
            }
            else if (this.mission.stato === MissionStatus.DRAFT) {
                if (missionDate.isBefore(now, 'day')) {
                    this.canDelete = false;
                    this.canModify = false;
                }
                else {
                    if (this.deviceService.isOffline()) {
                        this.canDelete = this.mission.idMissione === 0 ? true : false;
                    }
                }
            }
        }
    }

    public getPdfTrasferta(): void {
        this.action.next(DetailAction.DOWNLOAD)
    }

    public modify(): void {
        this.action.next(DetailAction.MODIFY);
    }

    public delete(): void {
        this.action.next(DetailAction.DELETE);
    }
}
