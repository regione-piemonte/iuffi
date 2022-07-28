import { Component, EventEmitter, Output } from '@angular/core';
import { LoggerService } from '@core/logger/services/logger.service';

/***
 * Wrapper dello ion-fab con stile custom
 */
@Component({
    selector: 'iuffi-fab',
    templateUrl: './iuffi-fab.component.html',
    styleUrls: ['./iuffi-fab.component.scss'],
})
export class IuffiFabComponent {

    @Output() public fabActionSelected = new EventEmitter();

    constructor(
        private loggerService: LoggerService,
    ) { }

    public fabClick(): void {
        this.loggerService.debug('iuffi-fab::fabClick');
        this.fabActionSelected.emit();
    }
}
