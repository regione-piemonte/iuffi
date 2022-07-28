import { Component, Input } from '@angular/core';
import { Inspection } from '@app/detections/models/visual-inspection.model';

export interface Item {
    value: number;
    label: string;
}

@Component({
    selector: 'single-inspection-card-detail',
    templateUrl: 'single-inspection-card-detail.component.html',
    styleUrls: ['single-inspection-card-detail.component.scss']
})
export class SingleInspectionCardDetailComponent {
    public positivityList: Item[] = [];
    public diameterList: Item[] = [];
    public positivity!: Item;
    public diameter!: Item;
    public flagTreeClimberIspezione = '';
    public flagTreeClimberTaglio = '';
    @Input() public inspection!: Inspection;
    @Input() public isRelatedToEmergency = false;        
    constructor(
    ) {
        this.positivityList = [
            {
                value: 1,
                label: 'POSITIVE',
            },
            {
                value: 2,
                label: 'NEGATIVE',
            },
            {
                value: 3,
                label: 'DOUBTFUL',
            }
        ];

        this.diameterList = [
            {
                value: 1,
                label: 'LESS_THAN_10',
            },
            {
                value: 2,
                label: 'BETWEEN_10_AND_20',
            },
            {
                value: 3,
                label: 'GREATER_THAN_20',
            }
        ];
    }

    ngOnInit(): void {
        this.positivity = this.positivityList.find(item => item.value === this.inspection.positivita) as Item;
        this.diameter = this.diameterList.find(item => item.value === this.inspection.diametro) as Item;
        this.flagTreeClimberIspezione = this.inspection.flagTreeClimberIspezione == 'S' ? 'SI' : 'NO';
        this.flagTreeClimberTaglio = this.inspection.flagTreeClimberTaglio == 'S' ? 'SI' : 'NO';
    }
}
