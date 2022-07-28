import { DeviceService } from './../../../core/device/services/device.service';
import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Detection } from '@app/detections/models/detection.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { LoggerService } from '@core/logger';
import { RootNavController } from '@core/split-view';
import { AutoUnsubscribe } from '@core/utils';
import { NavParams } from '@ionic/angular';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';

import { DetectionPage } from '../detection/detection.page';
import { TrapInstallationPage } from '../trap-installation/trap-installation.page';
import { TrapSearchPage } from '../trap-search/trap-search.page';

/* eslint-disable indent */
@Component({
    selector: 'trap-op-selection',
    templateUrl: 'trap-op-selection.page.html',
    styleUrls: ['trap-op-selection.page.scss'],
})
export class TrapOpSelectionPage extends AutoUnsubscribe {
    public detection: Detection;
    public missionId: number;
    public action: number;
    public trapping: Trapping;
    public actionRoot: string;
    constructor(
        private rootNavController: RootNavController,
        private deviceService: DeviceService,
        private logger: LoggerService,
        private navParams: NavParams,
        public fileManagerService: FileManagerService,
        public sanitizer: DomSanitizer
    ) {
        super();
        this.detection = this.navParams.get('detection') as Detection;
        this.missionId = this.navParams.get('missionId') as number;
        this.action = this.navParams.get('action') as number;
        this.actionRoot = this.navParams.get('actionRoot') as string;

        this.trapping = this.navParams.get('trapping') as Trapping;
        this.trapping.idMissione = this.missionId;
        this.trapping.idRilevazione = this.detection.idRilevazione;

        this.logger.debug('TrapOpSelectionPage::trapOpSelection');
    }

    public backToDetection(): void {
        this.logger.debug('TrapOpSelectionPage::backToDetection');
        this.rootNavController.setRoot(DetectionPage, {
            detection: this.detection,
            missionId: this.missionId,
            action: this.action,
            detectionId: this.detection.idRilevazione,
            actionRoot: this.actionRoot
        }, {
            animated: true,
            direction: 'back',
        });
    }

    public goForward(destination: 'installation' | 'removal' | 'maintenance'): void {
        this.logger.debug('TrapOpSelectionPage::goForward');
        if (this.deviceService.isOnline()) {
            this.rootNavController.push(
                destination === 'installation' ? TrapInstallationPage : destination === 'maintenance' || 'removal' ? TrapSearchPage : null,
                {
                    detection: this.detection,
                    missionId: this.missionId,
                    detectionId: this.detection.idRilevazione,
                    trapping: this.trapping,
                    action: destination,
                    actionRoot: this.actionRoot
                }, {
                animated: true,
                direction: 'forward'
            });
        }
        else {
            this.deviceService.alert('La funzionalità selezionata non può essere utilizzata in modalità offline');
        }

    }
}
