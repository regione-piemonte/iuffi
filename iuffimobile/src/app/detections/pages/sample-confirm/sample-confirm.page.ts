import { Component } from '@angular/core';
import { FileStatus } from '@app/detections/dto/photo.dto';
import { Detection } from '@app/detections/models/detection.model';
import { Sample } from '@app/detections/models/sample.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { SampleService } from '@app/detections/services/sample.service';
import { LoggerService } from '@core/logger';
import { ENV } from '@env';
import { NavParams } from '@ionic/angular';
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
    selector: 'sample-confirm',
    templateUrl: 'sample-confirm.page.html',
    styleUrls: ['sample-confirm.page.scss']
})
export class SampleConfirmPage {
    public detection: Detection;
    public sample: Sample;
    public visualInspection: VisualInspection;
    public isRelatedToEmergency = false;
    public plantSpecies: PlantSpeciesDetail;
    public harmfulOrganismsAdded: HarmfulOrganism[] = [];
    public comune: Comune | null;
    public actionRoot: string;
    constructor(
        private rootNavController: RootNavController,
        private logger: LoggerService,
        private deviceService: DeviceService,
        private navParams: NavParams,
        private detectionService: DetectionService,
        private geoUtilsService: GeoUtilsService,
        public fileManagerService: FileManagerService,
        private sampleService: SampleService
    ) {
        this.detection = this.navParams.get('detection') as Detection;
        this.sample = this.navParams.get('sample') as Sample;
        this.actionRoot = this.navParams.get('actionRoot') as string;
        this.visualInspection = this.navParams.get('visualInspection') as VisualInspection;
        this.plantSpecies = this.detectionService.getPlantSpeciesDetail(this.sample.idSpecieVegetale) as PlantSpeciesDetail;

        this.sample.organismiNocivi.map(id => {
            const harmfulOrganism = this.detectionService.getHarmfulOrganism(id);
            if (harmfulOrganism) {
                this.harmfulOrganismsAdded.push(harmfulOrganism);
            }
        })

        let point: Point;
        if (ENV.devMode) {
            point = new Point(45.240884, 8.260742);
        }
        else {
            point = new Point(this.sample.latitudine, this.sample.longitudine);
        }
        this.comune = this.geoUtilsService.getComuneByPoint(point);
        // Controllo se l'ispezione visiva è all'interno di una rilevazione relativa ad una emergenza
        this.isRelatedToEmergency = this.detection.flagEmergenza === 'S';
    }

    public saveSample(): void {
        this.logger.debug('SamplePage::saveSample');
        this.logger.debug(this.sample);
        // Ora di fine
        if (this.sample.dataOraFine) {
            this.sample.oraFine = Moment(this.sample.dataOraFine).format('HH:mm');
        } else {
            this.sample.oraFine = Moment().format('HH:mm');
        }

        this.sampleService.saveOrUpdateSample(this.sample,this.detection).then(
            response => {
                // Aggiorno l'orario di chiusura detection,se detection già chiusa
                if (this.detection.oraFine) {
                    // Se create e non update
                    const foundEl = this.detection.campionamenti?.find(el => { return el.idCampionamento === this.sample.idCampionamento })
                    // Se nuovo, non ho On sul campionamento
                    if (!foundEl?.idOrganismoNocivo) {
                        this.detection.oraFine = Moment().format('HH:mm');
                        this.detectionService.updateDetectionEndDate({ ...this.detection });
                    }
                }
                this.sample.dataOraInizio = response.dataOraInizio;
                this.sample.dataOraFine = response.dataOraFine;
                this.sample.oraFine = Moment(this.sample.dataOraFine).format('HH:mm');
                this.sample.oraInizio = Moment(this.sample.dataOraInizio).format('HH:mm');
                if (this.sample.photos.length == 0) {
                    this.deviceService.alert('SAVE_SAMPLE_SUCCESS', {
                        handler: () => {
                            this.setRoot(response);
                        }
                    });
                }
                else {
                    const photoToUpload = this.sample.photos.filter(p => p.status == FileStatus.TO_SYNCHRONIZE);
                    from(photoToUpload).pipe(
                        mergeMap(photo => {
                            photo.sampleId = response.idCampionamento
                            photo.detectionId = response.idRilevazione;
                            return this.sampleService.uploadPhoto(photo);
                        }),
                        toArray()
                    ).subscribe(val => {
                        this.logger.debug(val);
                        this.deviceService.alert('SAVE_SAMPLE_SUCCESS', {
                            handler: () => {
                                this.setRoot(response);
                            }
                        });
                    });
                }
            }
        ).catch((err: any) => {
            this.logger.error(err)
            this.deviceService.alert('SAVE_SAMPLE_ERROR');
        });

    }

    public setRoot(response: any): void {
        if (this.sample.photos.length > 0) {
            this.sample.foto = true;
        }
        if (this.sample.idIspezioneVisiva) {
            this.rootNavController.setRoot(VisualInspectionPage, {
                visualInspection: this.visualInspection,
                detection: this.detection,
                actionRoot: this.actionRoot
            }, {
                animated: true,
                direction: 'back'
            });
        } else {
            this.rootNavController.setRoot(DetectionPage, { missionId: response.idMissione, detectionId: response.idRilevazione, actionRoot: this.actionRoot }, {
                animated: true,
                direction: 'back'
            });
        }
    }
}
