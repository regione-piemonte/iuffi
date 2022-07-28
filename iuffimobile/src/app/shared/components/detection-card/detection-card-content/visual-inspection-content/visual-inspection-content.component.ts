import { Component, Input } from '@angular/core';
import { GetPhotoRequest } from '@app/detections/api/get-photos-request';
import { Detection } from '@app/detections/models/detection.model';
import { Photo } from '@app/detections/models/photo.model';
import { VisualInspection } from '@app/detections/models/visual-inspection.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { VisualInspectionService } from '@app/detections/services/visual-inspection.service';
import { MissionStatus } from '@app/missions/models/mission.model';
import { MissionService } from '@app/missions/services/mission.service';
import { DeviceService } from '@core/device';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { Comune } from '@shared/geo-utils/model/comune.model';
import { Point } from '@shared/geo-utils/model/point.model';
import { HarmfulOrganismOptions } from '@shared/offline/models/harmful-organism.model';
import { PlantSpeciesDetail } from '@shared/offline/models/plant-species.model';
import * as Moment from 'moment';

import { GeoUtilsService } from './../../../../geo-utils/geo-utils.service';

@Component({
    selector: 'visual-inspection-content-component',
    templateUrl: 'visual-inspection-content.component.html',
    styleUrls: ['visual-inspection-content.component.scss']
})
export class VisualInspectionContentComponent {
    public _isSkeleton = true;
    public moment: any = Moment();
    public MissionStatus = MissionStatus;
    public plantSpecies!: PlantSpeciesDetail;
    public isEditable = false;
    public comune: Comune | null = null;
    public harmfulOrganismsAdded: HarmfulOrganismOptions[] = [];
    public photos: Photo[] = [];
    public detection: Detection | undefined;
    @Input() public visualInspection!: VisualInspection;
    @Input() public isRelatedToEmergency = false;
    constructor(
        private missionService: MissionService,
        private visualInspectionService: VisualInspectionService,
        private detectionService: DetectionService,
        private geoUtilsService: GeoUtilsService,
        public fileManagerService: FileManagerService,
        private deviceService: DeviceService
    ) {

    }

    ngOnInit(): void {
        const mission = this.missionService.getMission(this.visualInspection.idMissione)
        if (mission) {
            this.isEditable = !mission.isOutdated();
        }

        this.detection = this.detectionService.getDetection(this.visualInspection.idMissione, this.visualInspection.idRilevazione);

        if (this.visualInspection.longitudine && this.visualInspection.longitudine) {
            const point = new Point(this.visualInspection.latitudine, this.visualInspection.longitudine);
            this.comune = this.geoUtilsService.getComuneByPoint(point);
        }

        this.plantSpecies = this.detectionService.getPlantSpeciesDetail(this.visualInspection.idSpecieVegetale) as PlantSpeciesDetail;
        if (this.visualInspection.organismi && this.visualInspection.organismi.length > 0) {
            this.visualInspection.organismi.map(organismo => {
                const harmfulOrganism = this.detectionService.getHarmfulOrganism(organismo.idSpecieOn);

                if (harmfulOrganism) {
                    const harmfulOption = new HarmfulOrganismOptions(harmfulOrganism);
                    if (organismo.flagTrovato == 'S' || organismo.flagTrovato === true) {
                        harmfulOption.selected = true;
                        organismo.flagTrovato = true;
                    } else {
                        harmfulOption.selected = false;
                        organismo.flagTrovato = false;
                    }
                    this.harmfulOrganismsAdded.push(harmfulOption);
                }
            })
        }
        const photoRequest: GetPhotoRequest = {
            idVisual: this.visualInspection.idIspezione,
            idCampionamento: 0,
            idTrappolaggio: 0
        }

        // this.photos = this.visualInspectionService.getVisualInspectionPhotos(this.visualInspection.idIspezione);
        if (this.visualInspection.foto && this.deviceService.isCordova()) {
            this.visualInspectionService.fetchPhotos(photoRequest).then(res => {
                this.photos = this.visualInspectionService.getVisualInspectionPhotos(this.visualInspection.idIspezione);
            }).catch(err => {

            });
        }

    }

    ngOnChanges(): void {
        this._isSkeleton = (this.visualInspection === null);

    }

    public delete(): void {
        this.visualInspectionService.deleteVisualInspection$.next(this.visualInspection)
    }

    public modify(): void {
        this.visualInspectionService.modifyVisualInspection$.next(this.visualInspection)
    }
}
