import { Component, Input } from '@angular/core';
import { Sample } from '@app/detections/models/sample.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { MissionStatus } from '@app/missions/models/mission.model';
import * as Moment from 'moment';

@Component({
    selector: 'detection-card-content-component',
    templateUrl: 'detection-card-content.component.html',
    styleUrls: ['detection-card-content.component.scss']
})
export class DetectionCardContentComponent {
    public _isSkeleton = true;
    public moment: any = Moment();
    public MissionStatus = MissionStatus;
    public detectionType = '';
    @Input() public detection!: VisualInspection | Trapping | Sample;
    @Input() public isRelatedToEmergency = false;
    constructor() { }
    ngOnChanges(): void {
        this._isSkeleton = (this.detection === null);
        if (this.detection) {
            if (this.detection instanceof VisualInspection) {
                this.detectionType = 'VISUAL_INSPECTION';
            }
            if (this.detection instanceof Sample) {
                this.detectionType = 'SAMPLING';
            }
            if (this.detection instanceof Trapping) {
                this.detectionType = 'TRAPPING';
            }
        }
    }
}
