import { Component, ViewChild } from '@angular/core';
import { Detection } from '@app/detections/models/detection.model';
import { Sample } from '@app/detections/models/sample.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionPage } from '@app/detections/pages/detection/detection.page';
import { SamplePage } from '@app/detections/pages/sample/sample.page';
import { TrapInstallationPage } from '@app/detections/pages/trap-installation/trap-installation.page';
import { TrapMaintenanceRemovePage } from '@app/detections/pages/trap-maintenance-remove/trap-maintenance-remove.page';
import { VisualInspectionPage } from '@app/detections/pages/visual-inspection/visual-inspection.page';
import { DetectionService } from '@app/detections/services/detection.service';
import { SampleService } from '@app/detections/services/sample.service';
import { TrapService } from '@app/detections/services/trap.service';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
import { HomePage } from '@app/home/pages/home/home.page';
import { DetailAction } from '@app/missions/models/detail-action.enum';
import { Mission } from '@app/missions/models/mission.model';
import { MissionService } from '@app/missions/services/mission.service';
import { AppError } from '@core/error';
import { LoggerService } from '@core/logger';
import { AutoUnsubscribe } from '@core/utils';
import { NavParams } from '@ionic/angular';
import { IuffiAnimatedCardComponent } from '@shared/components/iuffi-animated-card/iuffi-animated-card';
import { PDFService } from '@shared/file-manager/services/pdf.service';
import * as Moment from 'moment';
import { Subscription } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DeviceService } from './../../../core/device/services/device.service';
import { RootNavController } from './../../../core/split-view/services/root-nav.controller';
import { NewMissionPage } from './../new-mission/new-mission.page';

@Component({
    selector: 'mission-detail',
    templateUrl: 'mission-detail.page.html',
    styleUrls: ['mission-detail.page.scss']
})
export class MissionDetailPage extends AutoUnsubscribe {
    public newDetectionVisible = false;
    public mission: Mission | null = null;
    public missionId = 0;
    public missionDate = 0;
    public saveMissionVisible = false;
    public subsAction = new Array<Subscription>();

    @ViewChild(IuffiAnimatedCardComponent) public animatedCard!: IuffiAnimatedCardComponent;
    constructor(
        private navParams: NavParams,
        private rootNavController: RootNavController,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private missionService: MissionService,
        private detectionService: DetectionService,
        private visualInspectionService: VisualInspectionService,
        private sampleService: SampleService,
        private trapService: TrapService,
        private pdfService: PDFService
    ) {
        super();
        this.missionId = this.navParams.get('missionId');
        this.missionDate = this.navParams.get('missionDate');
    }

    ionViewDidEnter(): void {
        this._getMissionDetail();

        this.subsAction.push(this.visualInspectionService.deleteVisualInspection$
            .pipe(takeUntil(this.destroy$))
            .subscribe(visualInspection => this._deleteVisualInspection(visualInspection)));

        this.subsAction.push(this.sampleService.deleteSample$
            .pipe(takeUntil(this.destroy$))
            .subscribe(sample => this._deleteSample(sample)));

        this.subsAction.push(this.trapService.deleteTrap$
            .pipe(takeUntil(this.destroy$))
            .subscribe(trap => this._deleteTrap(trap)));

        this.subsAction.push(this.visualInspectionService.modifyVisualInspection$
            .pipe(takeUntil(this.destroy$))
            .subscribe(visualInspection => this._editItem(visualInspection)));

        this.subsAction.push(this.sampleService.modifySample$
            .pipe(takeUntil(this.destroy$))
            .subscribe(sample => this._editItem(sample)));

        this.subsAction.push(this.trapService.modifyTrap$
            .pipe(takeUntil(this.destroy$))
            .subscribe(trap => this._editItem(trap)));
    }

    ionViewDidLeave(): void {
        if (this.subsAction.length > 0) {
            this.subsAction.forEach(element => {
                element.unsubscribe();
            });
        }
    }
    private _editItem(item: any | null): void {
        this.logger.debug('MODIFY ', item);
        let pageClass;
        if (item.hasOwnProperty('idIspezione') && item['idIspezione']) {
            pageClass = VisualInspectionPage
        } else if (item.hasOwnProperty('idCampionamento') && item['idCampionamento']) {
            pageClass = SamplePage
        } else if (item.hasOwnProperty('idTrappolaggio') && item['idTrappolaggio'] && item['idOperazione'] === 1) {
            pageClass = TrapInstallationPage
        } else if (item.hasOwnProperty('idTrappolaggio') && item['idTrappolaggio'] && (item['idOperazione'] === 2 || item['idOperazione'] === 3 || item['idOperazione'] === 4)) {
            pageClass = TrapMaintenanceRemovePage
        }
        this.rootNavController.push(pageClass, {
            visualInspection: pageClass === VisualInspectionPage ? { ...item } : null,
            sample: pageClass === SamplePage ? item : null,
            trapping: (pageClass === TrapInstallationPage || pageClass === TrapMaintenanceRemovePage) ? item : null,
            detection: this.detectionService.getDetection(item.idMissione, item.idRilevazione),
            actionRoot: 'MODIFY'
        });
    }

    private _deleteVisualInspection(visualInspection: VisualInspection | null): void {
        this.logger.debug('DELETE ', visualInspection)
        const self = this;
        if (visualInspection) {
            if (this.visualInspectionService.checkInternalDetection(visualInspection)) {
                this.deviceService.confirm('DELETE_VISUALINSPECTION_ALERT', {
                    buttons: [
                        {
                            text: 'RESET'
                        },
                        {
                            text: 'CONFIRM',
                            handler: () => {
                                this.visualInspectionService.deleteVisualAndInternalDetection(visualInspection).then(() => {
                                    self.deviceService.alert('DELETE_VISUALINSPECTION_SUCCESS');
                                }).catch((err: any) => {
                                    this.logger.error(err);
                                });
                            }
                        }]
                });
            }
            else {
                this.deviceService.confirm('DELETE_VISUALINSPECTION_CONFIRM', {
                    buttons: [
                        {
                            text: 'RESET'
                        },
                        {
                            text: 'CONFIRM',
                            handler: () => {
                                this.visualInspectionService.deleteVisualInspection(visualInspection).then(() => {
                                    self.deviceService.alert('DELETE_VISUALINSPECTION_SUCCESS');
                                }).catch((err: any) => {
                                    this.logger.error(err);
                                });
                            }
                        }]
                });
            }

        }
    }

    private _deleteSample(sample: Sample | null): void {
        this.logger.debug('DELETE ', sample)
        const self = this;
        if (sample) {
            this.deviceService.confirm('DELETE_SAMPLE_CONFIRM', {
                buttons: [
                    {
                        text: 'RESET'
                    },
                    {
                        text: 'CONFIRM',
                        handler: () => {
                            this.sampleService.deleteSample(sample).then(() => {
                                self.deviceService.alert('DELETE_SAMPLE_SUCCESS');
                            }).catch((err: any) => {
                                this.logger.error(err);
                            });
                        }
                    }]
            });

        }
    }

    private _deleteTrap(trap: Trapping | null): void {
        this.logger.debug('DELETE ', trap)
        const self = this;
        if (trap) {
            this.deviceService.confirm('DELETE_TRAP_CONFIRM', {
                buttons: [
                    {
                        text: 'RESET'
                    },
                    {
                        text: 'CONFIRM',
                        handler: () => {
                            this.trapService.deleteTrap(trap).then(() => {
                                self.deviceService.alert('DELETE_TRAP_SUCCESS');
                            }).catch((err: any) => {
                                this.logger.error(err);
                            });
                        }
                    }]
            });

        }
    }

    private _getMissionDetail(): void {
        this.missionService.fetchMissionDetail(this.missionId).then(
            () => {
                this.mission = this.missionService.getMissionDetail(this.missionId, this.missionDate);
                if (this.mission) {
                    this.newDetectionVisible = !this.mission.isOutdated();
                    this.saveMissionVisible = !this.mission.isOutdated() && this.mission.rilevazioni != undefined && this.mission.rilevazioni?.length > 0;
                }
                if (this.mission && this.mission.rilevazioni && this.mission.rilevazioni.length>0) {
                    this.mission.rilevazioni.forEach(detection => {
                        // Ordinamento di attività
                        if (detection.ispezioniVisive && detection.ispezioniVisive.length>0) {
                            detection.ispezioniVisive = detection.ispezioniVisive.sort((a: VisualInspection, b: VisualInspection) => {
                                return new Date(a.dataOraInizio).getTime() - new Date(b.dataOraInizio).getTime();
                            });
                        }
                        if (detection.campionamenti && detection.campionamenti.length>0) {
                            detection.campionamenti = detection.campionamenti.sort((a: Sample, b: Sample) => {
                                return new Date(a.dataOraInizio).getTime() - new Date(b.dataOraInizio).getTime();
                            });
                        }
                        if (detection.trappolaggi && detection.trappolaggi.length>0) {
                            detection.trappolaggi = detection.trappolaggi.sort((a: Trapping, b: Trapping) => {
                                return new Date(a.dataOraInizio).getTime() - new Date(b.dataOraInizio).getTime();
                            });
                        }
                    });
                }
            },
            (err: AppError) => {
                this.logger.error(err);
                this.deviceService.alert(err.message);
            }
        );
    }

    public manageMission(action: DetailAction): void {
        const self = this;
        if (action === DetailAction.MODIFY) {
            self.rootNavController.push(NewMissionPage, { mission: self.mission, action: action })
        }
        if (action === DetailAction.DELETE) {
            self.deviceService.confirm('DELETE_MISSION_CONFIRM', {
                buttons: [
                    {
                        text: 'RESET'
                    },
                    {
                        text: 'CONFIRM',
                        handler: () => {
                            this.missionService.deleteMission(self.mission as Mission).then(
                                () => {
                                    self.deviceService.alert('DELETE_MISSION_SUCCESS', {
                                        handler: () => {
                                            self.rootNavController.setRoot(HomePage, {}, {
                                                animated: true,
                                                direction: 'back',

                                            });
                                        }
                                    });
                                },
                                () => {
                                    self.deviceService.alert('DELETE_MISSION_ERROR');
                                }
                            );
                        }
                    }]
            });
        }
        if (action === DetailAction.DOWNLOAD) {
            if (this.mission) {
                this.missionService.downloadPdf(this.mission.idMissione).then(
                    (res: string) => {
                        self.pdfService.saveAndOpenPdf(res, self.mission?.numeroTrasferta + 'pdf')
                    },
                    () => {
                        this.deviceService.alert('DOWNLOAD_PDF_ERROR');
                    }
                );
            }
        }
    }

    public modifyDetection(detection: Detection): void {
        this.logger.debug('MissionDetailPage::modifyDetection');
        this.rootNavController.push(DetectionPage, { missionId: detection.idMissione, detectionId: detection.idRilevazione, actionRoot: 'MODIFY' }, {
            animated: true,
            direction: 'forward'
        });
    }

    public deleteDetection(detection: Detection): void {
        this.logger.debug('MissionDetailPage::deleteDetection');
        const self = this;
        this.deviceService.confirm('DELETE_DETECTION_CONFIRM', {
            buttons: [
                {
                    text: 'RESET'
                },
                {
                    text: 'CONFIRM',
                    handler: () => {
                        this.detectionService.deleteDetection(detection).then(
                            () => {
                                self.deviceService.alert('DELETE_DETECTION_SUCCESS');
                                if (this.mission) {
                                    this.saveMissionVisible = !this.mission.isOutdated() && this.mission.rilevazioni != undefined && this.mission.rilevazioni?.length > 0;
                                }
                            },
                            () => {
                                self.deviceService.alert('DELETE_DETECTION_ERROR');
                            }
                        );
                    }
                }]
        });
    }

    public newDetection(): void {
        const detection: Detection = new Detection()
        detection.idMissione = (this.mission as Mission).idMissione;
        this.detectionService.saveOrUpdateDetection(detection).then(
            (detection: Detection) => {
                this.rootNavController.setRoot(DetectionPage, { missionId: (this.mission as Mission).idMissione, detectionId: detection.idRilevazione, actionRoot: 'NEW', missionDate: this.missionDate }, {
                    animated: true,
                    direction: 'forward'
                });
            },
            (err: any) => {
                this.logger.debug(err);
                this.deviceService.alert('SAVE_DETECTION_ERROR');
            }
        )
    }

    public saveMission(): void {
        (this.mission as Mission).oraFine = Moment().format('HH:mm');
        this.missionService.synchronizeMission(this.mission as Mission).then(() => {
            this.deviceService.alert('SAVE_MISSION_SUCCESS', {
                handler: () => {
                    this.rootNavController.setRoot(HomePage, {}, {
                        animated: true,
                        direction: 'back'
                    });
                }
            });
        }).catch(() => {
            this.deviceService.alert('SAVE_MISSION_ERROR');
        });
    }

    public backToMissions(): void {
        this.logger.debug('MissionDetailPage::backToMissions');
        this.rootNavController.setRoot(HomePage), {
            animated: true,
            direction: 'back',
        };
    }
}
