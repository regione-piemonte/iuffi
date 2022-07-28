import { DetectionService } from './../../services/detection.service';
import { Component, Input } from '@angular/core';
import { Trap } from '@app/detections/models/trap.model';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';

@Component({
    selector: 'trap-map-item-selection',
    templateUrl: './trap-map-item-selection.component.html',
    styleUrls: ['./trap-map-item-selection.component.scss'],
})
export class TrapMapItemSelectionComponent {
    @Input('trap') public trap: Trap | undefined;
    public harmfulOrganism: HarmfulOrganism | null = null;
    constructor(private detectionService: DetectionService) { }

    ngOnChanges(): void {
        if (this.trap) {
            this.harmfulOrganism = this.detectionService.getHarmfulOrganism(this.trap.idOrganismoNocivo as number);
        }
    }
}
