import { Component, Input, Output } from '@angular/core';
import { Detection } from '@app/detections/models/detection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { AreaTypeLocal } from '@shared/offline/models/area-type.model';
import * as Moment from 'moment';
import { Subject } from 'rxjs';
import { DeviceService } from './../../../core/device/services/device.service';
import { Mission, MissionStatus } from './../../models/mission.model';
import { MissionService } from './../../services/mission.service';

@Component({
    selector: 'detection-header-component',
    templateUrl: 'detection-header.component.html',
    styleUrls: ['detection-header.component.scss']
})
export class DetectionHeaderComponent {
    public isSkeleton = true;
    public canDelete = true;
    public canModify = true;
    public areaTypeSelected: AreaTypeLocal | null = null;
    private mission: Mission | null = null;
    @Input() public detection!: Detection;
    @Output() public delete = new Subject<Detection>();
    @Output() public modify = new Subject<Detection>();
    constructor(
        private missionService: MissionService,
        private detectionService: DetectionService,
        private deviceService: DeviceService
    ) {

    }

    ngOnInit(): void {
        const now = Moment();

        this.mission = this.missionService.getMissionDetail(this.detection.idMissione);
        if (this.mission) {
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
            if (this.detection.idTipoArea) {
                this.areaTypeSelected = this.detectionService.getAreaType(this.detection.idTipoArea);
            }
        }

    }

    ngOnChanges(): void {
        this.isSkeleton = (this.detection === null);
    }

    // public modify(): void {
    //     this.action.next(DetailAction.MODIFY);
    // }

    // public delete(): void {
    //     this.action.next(DetailAction.DELETE);
    // }
}
