import { Component } from '@angular/core';
import { FileStatus } from '@app/detections/dto/photo.dto';
import { Detection } from '@app/detections/models/detection.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
import { LoggerService } from '@core/logger';
import { ENV } from '@env';
import { NavParams } from '@ionic/angular';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { GeoUtilsService } from '@shared/geo-utils/geo-utils.service';
import { Comune } from '@shared/geo-utils/model/comune.model';
import { Point } from '@shared/geo-utils/model/point.model';
import { HarmfulOrganism, HarmfulOrganismOptions } from '@shared/offline/models/harmful-organism.model';
import { PlantSpeciesDetail } from '@shared/offline/models/plant-species.model';
import * as Moment from 'moment';
import { from } from 'rxjs';
import { mergeMap, toArray } from 'rxjs/operators';

import { DeviceService } from '../../../core/device/services/device.service';
import { RootNavController } from '../../../core/split-view/services/root-nav.controller';
import { DetectionPage } from '../detection/detection.page';

@Component({
    selector: 'visual-inspection-confirm',
    templateUrl: 'visual-inspection-confirm.page.html',
    styleUrls: ['visual-inspection-confirm.page.scss']
})
export class VisualInspectionConfirmPage {
    public detection: Detection;
    public visualInspection: VisualInspection;
    public isRelatedToEmergency = false;
    public plantSpecies: PlantSpeciesDetail;
    public harmfulOrganismsAdded: HarmfulOrganism[] = [];
    public comune: Comune | null;
    public actionRoot: string;
    private isNewVisual = false;
    constructor(
        private rootNavController: RootNavController,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private navParams: NavParams,
        private visualInspectionService: VisualInspectionService,
        private detectionService: DetectionService,
        private geoUtilsService: GeoUtilsService,
        public fileManagerService: FileManagerService,
    ) {
        this.detection = this.navParams.get('detection') as Detection;
        this.visualInspection = this.navParams.get('visualInspection') as VisualInspection;
        if (this.visualInspection.idIspezione === 0) {
            this.isNewVisual = true;
        }
        this.plantSpecies = this.detectionService.getPlantSpeciesDetail(this.visualInspection.idSpecieVegetale) as PlantSpeciesDetail;
        this.actionRoot = this.navParams.get('actionRoot') as string;

        if (this.visualInspection.organismi && this.visualInspection.organismi.length > 0) {
            this.harmfulOrganismsAdded = [];
            this.visualInspection.organismi.map(organismo => {
                const harmfulOrganism = this.detectionService.getHarmfulOrganism(organismo.idSpecieOn);
                if (harmfulOrganism) {
                    const harmfulOrganismOption = new HarmfulOrganismOptions(harmfulOrganism);
                    if (organismo.flagTrovato === 'S' || organismo.flagTrovato === true) {
                        harmfulOrganismOption.selected = true;
                    } else {
                        harmfulOrganismOption.selected = false;
                    }
                    this.harmfulOrganismsAdded.push(harmfulOrganismOption);
                }
            });
        }

        let point: Point;
        if (ENV.devMode) {
            point = new Point(45.240884, 8.260742);
        }
        else {
            point = new Point(this.visualInspection.latitudine, this.visualInspection.longitudine);
        }
        this.comune = this.geoUtilsService.getComuneByPoint(point);

        // Controllo se l'ispezione visiva è all'interno di una rilevazione relativa ad una emergenza
        this.isRelatedToEmergency = this.detection.flagEmergenza === 'S';
    }

    public saveVisualInspection(): void {
        this.logger.debug('VisualInspectionPage::saveVisualInspection');
        this.logger.debug(this.visualInspection);

        // Orario di inizio
        if (!this.visualInspection.oraInizio) {
            this.visualInspection.oraInizio = Moment().format('HH:mm');
        } else {
            this.visualInspection.oraInizio = Moment(this.visualInspection.dataOraInizio).format('HH:mm');
        }
        // Ora fine
        if (!this.visualInspection.oraFine) {
            this.visualInspection.oraFine = Moment().format('HH:mm');
        } else {
            this.visualInspection.oraFine= Moment(this.visualInspection.dataOraFine).format('HH:mm');
        }
        this.visualInspectionService.saveOrUpdateVisualInspection(this.visualInspection, this.detection).then(
            response => {
                // Aggiorno l'orario di chiusura detection, se detection già chiusa
                if (this.detection.oraFine) {
                    // Se nuova ispezione
                    if (this.isNewVisual) {
                        this.detection.oraFine = Moment().format('HH:mm');
                        this.detectionService.updateDetectionEndDate({ ...this.detection });
                    }
                }
                if (this.visualInspection.photos.length > 0) {
                    this.visualInspection.foto = true;
                }
                this.visualInspection.dataOraInizio = response.dataOraInizio;
                this.visualInspection.dataOraFine = response.dataOraFine;
                this.visualInspection.oraInizio = Moment(response.dataOraInizio).format('HH:mm') ;
                this.visualInspection.oraFine = Moment(response.dataOraFine).format('HH:mm') ;

                if (this.visualInspection.photos.length == 0) {
                    this.deviceService.alert('SAVE_VISUALINSPECTION_SUCCESS', {
                        handler: () => {

                            this.rootNavController.setRoot(DetectionPage, {
                                missionId: response.idMissione, detectionId: response.idRilevazione,
                                actionRoot: this.actionRoot

                            }, {
                                animated: true,
                                direction: 'back'
                            });
                        }
                    });
                }
                else {
                    const photoToUpload = this.visualInspection.photos.filter(p => p.status == FileStatus.TO_SYNCHRONIZE);
                    from(photoToUpload).pipe(
                        mergeMap(photo => {
                            photo.visualInspectionId = response.idIspezione;
                            photo.detectionId = response.idRilevazione;
                            return this.visualInspectionService.uploadPhoto(photo);
                        }),
                        toArray()
                    ).subscribe(val => {
                        this.logger.debug(val);
                        this.deviceService.alert('SAVE_VISUALINSPECTION_SUCCESS', {
                            handler: () => {
                                if (this.visualInspection.photos.length > 0) {
                                    this.visualInspection.foto = true;
                                }
                                this.rootNavController.setRoot(DetectionPage, { missionId: response.idMissione, detectionId: response.idRilevazione, action: this.actionRoot }, {
                                    animated: true,
                                    direction: 'back'
                                });
                            }
                        });
                    });
                }
            }
        ).catch((err: any) => {
            this.logger.error(err)
            this.deviceService.alert('SAVE_VISUALINSPECTION_ERROR');
        });

    }
}
