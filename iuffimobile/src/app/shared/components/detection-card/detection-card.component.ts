import { Component, Input } from '@angular/core';
import { Sample } from '@app/detections/models/sample.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';

@Component({
    selector: 'detection-card-component',
    templateUrl: 'detection-card.component.html',
    styleUrls: ['detection-card.component.scss']
})
export class DetectionCardComponent {
    public _isSkeleton = true;
    public isRelatedToEmergency = false;
    @Input() public detection!: VisualInspection | Trapping | Sample;
    @Input() public flagEmergenza!: string;
    constructor() { }
    ngOnChanges(): void {
        this._isSkeleton = (this.detection === null);
        this.isRelatedToEmergency = this.flagEmergenza === 'S' ? true : false;
    }
}
