import { Component, Input } from '@angular/core';
import { Mission, MissionStatus } from '@app/missions/models/mission.model';
import * as Moment from 'moment';

@Component({
    selector: 'mission-card',
    templateUrl: 'mission-card.component.html',
    styleUrls: ['mission-card.component.scss']
})
export class MissionCard {
    public _isSkeleton = true;
    public moment: any = Moment();
    public MissionStatus = MissionStatus;
    @Input() public mission!: Mission;
    constructor() { }
    ngOnChanges(): void {
        console.log('ngOnChanges');
        this._isSkeleton = (this.mission === null);
    }
}
