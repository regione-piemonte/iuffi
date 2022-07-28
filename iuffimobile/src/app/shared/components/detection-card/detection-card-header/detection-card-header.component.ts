import { Component, Input } from '@angular/core';
import { Sample } from '@app/detections/models/sample.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';

@Component({
    selector: 'detection-card-header-component',
    templateUrl: 'detection-card-header.component.html',
    styleUrls: ['detection-card-header.component.scss']
})
export class DetectionCardHeaderComponent {
    public isSkeleton = true;
    public icon = '';
    public text = '';
    @Input() public detection!: VisualInspection | Trapping | Sample;
    constructor() {

    }
    ngOnChanges(): void {
        console.log('ngOnChanges');
        this.isSkeleton = (this.detection === null);
        if (this.detection) {
            if (this.detection instanceof VisualInspection) {
                this.icon = 'iuffi-inspection';
                this.text = 'VISUAL_INSPECTION';
            }
            if (this.detection instanceof Sample) {
                this.icon = 'iuffi-sampling';
                this.text = 'SAMPLING';
            }
            if (this.detection instanceof Trapping) {
                this.icon = 'iuffi-trapping';
                this.text = 'TRAPPING';
            }
        }
    }
}
