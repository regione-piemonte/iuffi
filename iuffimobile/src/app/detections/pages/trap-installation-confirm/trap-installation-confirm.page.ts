import { Component } from '@angular/core';
import { FileStatus } from '@app/detections/dto/photo.dto';
import { Detection } from '@app/detections/models/detection.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { TrapService } from '@app/detections/services/trap.service';
import { LoggerService } from '@core/logger';
import { ENV } from '@env';
import { NavParams } from '@ionic/angular';
import { TranslateService } from '@ngx-translate/core';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { GeoUtilsService } from '@shared/geo-utils/geo-utils.service';
import { Comune } from '@shared/geo-utils/model/comune.model';
import { Point } from '@shared/geo-utils/model/point.model';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';
import { PlantSpeciesDetail } from '@shared/offline/models/plant-species.model';
import * as Moment from 'moment';
import { from } from 'rxjs';
import { mergeMap, toArray } from 'rxjs/operators';

import { DeviceService } from '../../../core/device/services/device.service';
import { RootNavController } from '../../../core/split-view/services/root-nav.controller';
import { DetectionPage } from '../detection/detection.page';
import { VisualInspectionPage } from '../visual-inspection/visual-inspection.page';

@Component({
    selector: 'trap-installation-confirm',
    templateUrl: 'trap-installation-confirm.page.html',
    styleUrls: ['trap-installation-confirm.page.scss']
})
export class TrapInstallationConfirmPage {
    public detection: Detection;
    public trap: Trapping;
    public visualInspection: VisualInspection;
    public isRelatedToEmergency = false;
    public plantSpecies: PlantSpeciesDetail;
    public harmfulOrganismsAdded: HarmfulOrganism[] = [];
    public comune: Comune | null;
    public trapTypeDescription: string;
    public trapOperationDesc = '';
    public actionRoot: string;
    private isNewTrapping = false;
    constructor(
        private rootNavController: RootNavController,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private navParams: NavParams,
        private detectionService: DetectionService,
        private geoUtilsService: GeoUtilsService,
        public fileManagerService: FileManagerService,
        private trapService: TrapService,
        private translateService: TranslateService
    ) {
        this.detection = this.navParams.get('detection') as Detection;
        this.trap = this.navParams.get('trap') as Trapping;
        if (this.trap.idTrappolaggio === 0) {
            this.isNewTrapping = true;
        }
        this.visualInspection = this.navParams.get('visualInspection') as VisualInspection;
        this.trapTypeDescription = this.navParams.get('trapTypeDescription') as string;
        this.actionRoot = this.navParams.get('actionRoot') as string;
        this.plantSpecies = this.detectionService.getPlantSpeciesDetail(this.trap.idSpecieVegetale) as PlantSpeciesDetail;
        // Tipo di operazione
        if (this.trap.idOperazione === 2) {
            this.trapOperationDesc = this.translateService.instant('MAINTENANCE');
        } else if (this.trap.idOperazione === 3) {
            this.trapOperationDesc = this.translateService.instant('RECHARGE');
        } else if (this.trap.idOperazione === 4) {
            this.trapOperationDesc = this.translateService.instant('REMOVAL');
        }

        const harmfulOrganism = this.detectionService.getHarmfulOrganism(this.trap.idOrganismoNocivo);
        if (harmfulOrganism) {
            this.harmfulOrganismsAdded.push(harmfulOrganism);
        }

        let point: Point;
        if (ENV.devMode) {
            point = new Point(45.240884, 8.260742);
        }
        else {
            point = new Point(this.trap.latitudine, this.trap.longitudine);
        }
        this.comune = this.geoUtilsService.getComuneByPoint(point);
        // Controllo se l'ispezione visiva è all'interno di una rilevazione relativa ad una emergenza
        this.isRelatedToEmergency = this.detection.flagEmergenza === 'S';
    }

    public saveTrap(): void {
        this.logger.debug('TrapConfirmPage::saveTrap');
        this.trap.specie = this.plantSpecies.nomeVolgare;
        this.trap.descrTrappola = this.trapTypeDescription;
        // Orario di inizio
        if (this.isNewTrapping) {
            this.trap.dataOraInizio = Moment().format('HH:mm');
        }
        this.logger.debug(this.trap);
        this.trapService.saveOrUpdateTrap(this.trap).then(
            response => {
                // Aggiorno l'orario di chiusura detection, se detection già chiusa
                if (this.detection.oraFine) {
                    // Se nuovo trappolaggio
                    if (this.isNewTrapping) {
                        this.detection.oraFine = Moment().format('HH:mm');
                        this.detectionService.updateDetectionEndDate({ ...this.detection });
                    }
                }
                this.trap.dataOraInizio = response.dataOraInizio;
                this.trap.dataOraInizioT = response.dataOraInizioT;
                if (this.trap.photos.length == 0) {
                    this.deviceService.alert(this.trap.idOperazione === 4 ? 'REMOVE_TRAP_SUCCESS' : 'SAVE_TRAP_SUCCESS', {
                        handler: () => {
                            this.setRoot(response);
                        }
                    });
                }
                else {
                    const photoToUpload = this.trap.photos.filter(p => p.status == FileStatus.TO_SYNCHRONIZE);
                    from(photoToUpload).pipe(
                        mergeMap(photo => {
                            photo.trapId = response.idTrappolaggio;
                            photo.detectionId = response.idRilevazione;
                            // Spostare in Detection Service?...
                            return this.trapService.uploadPhoto(photo);
                        }),
                        toArray()
                    ).subscribe(val => {
                        this.logger.debug(val);
                        this.deviceService.alert(this.trap.idOperazione === 4 ? 'REMOVE_TRAP_SUCCESS' : 'SAVE_TRAP_SUCCESS', {
                            handler: () => {
                                this.setRoot(response);
                            }
                        });
                    });
                }
            }
        ).catch((err: any) => {
            this.logger.error(err)
            if (err && err.code) {
                if (err.code == 1) {
                    this.deviceService.alert('TRAP_CODE_ERROR');
                }
            }
            else {
                this.deviceService.alert('SAVE_TRAP_ERROR');
            }
        });

    }

    public setRoot(response: any): void {
        if (this.trap.photos.length > 0) {
            this.trap.foto = true;
        }
        if (this.visualInspection) {
            this.rootNavController.setRoot(VisualInspectionPage, {
                visualInspection: this.visualInspection,
                detection: this.detection,
                actionRoot: this.actionRoot
            }, {
                animated: true,
                direction: 'back'
            });
        } else {
            this.rootNavController.setRoot(DetectionPage, {
                missionId: response.idMissione,
                detectionId: response.idRilevazione,
                actionRoot: this.actionRoot
            }, {
                animated: true,
                direction: 'back'
            });
        }
    }
}
