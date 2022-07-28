import { Component, Input } from '@angular/core';
import { GetPhotoRequest } from '@app/detections/api/get-photos-request';
import { Photo } from '@app/detections/models/photo.model';
import { Sample } from '@app/detections/models/sample.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { SampleService } from '@app/detections/services/sample.service';
import { MissionStatus } from '@app/missions/models/mission.model';
import { MissionService } from '@app/missions/services/mission.service';
import { DeviceService } from '@core/device';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';
import { GeoUtilsService } from '@shared/geo-utils/geo-utils.service';
import { Comune } from '@shared/geo-utils/model/comune.model';
import { Point } from '@shared/geo-utils/model/point.model';
import { HarmfulOrganism } from '@shared/offline/models/harmful-organism.model';
import { PlantSpeciesDetail } from '@shared/offline/models/plant-species.model';
import * as Moment from 'moment';

@Component({
    selector: 'sampling-content-component',
    templateUrl: 'sampling-content.component.html',
    styleUrls: ['sampling-content.component.scss']
})
export class SamplingContentComponent {
    public _isSkeleton = true;
    public moment: any = Moment();
    public MissionStatus = MissionStatus;
    public plantSpecies!: PlantSpeciesDetail;
    public isEditable = false;
    public comune: Comune | null = null;
    public harmfulOrganismsAdded: HarmfulOrganism[] = [];
    public photos: Photo[] = [];
    @Input() public sample!: Sample;
    @Input() public isRelatedToEmergency = false;
    constructor(
        private missionService: MissionService,
        private sampleService: SampleService,
        private detectionService: DetectionService,
        private geoUtilsService: GeoUtilsService,
        public fileManagerService: FileManagerService,
        private deviceService: DeviceService
    ) {

    }

    ngOnInit(): void {
        const mission = this.missionService.getMission(this.sample.idMissione)
        if (mission) {
            this.isEditable = !mission.isOutdated();
        }

        if (this.sample.longitudine && this.sample.longitudine) {
            const point = new Point(this.sample.latitudine, this.sample.longitudine);
            this.comune = this.geoUtilsService.getComuneByPoint(point);
        }

        this.plantSpecies = this.detectionService.getPlantSpeciesDetail(this.sample.idSpecieVegetale) as PlantSpeciesDetail;
        if (this.sample.organismiNocivi && this.sample.organismiNocivi.length > 0) {
            this.sample.organismiNocivi.map(id => {
                const harmfulOrganism = this.detectionService.getHarmfulOrganism(id);
                if (harmfulOrganism) {
                    this.harmfulOrganismsAdded.push(harmfulOrganism);
                }
            })
        }
        const photoRequest: GetPhotoRequest = {
            idCampionamento: this.sample.idCampionamento,
            idVisual: 0,
            idTrappolaggio: 0
        }

        // this.photos = this.visualInspectionService.getVisualInspectionPhotos(this.visualInspection.idIspezione);
        if (this.sample.foto && this.deviceService.isCordova()) {
            this.sampleService.fetchPhotos(photoRequest).then(res => {
                this.photos = this.sampleService.getSamplePhotos(this.sample.idCampionamento);
            }).catch(err => {

            });
        }
    }

    ngOnChanges(): void {
        console.log('ngOnChanges');
        this._isSkeleton = (this.sample === null);

    }

    public delete(): void {
        this.sampleService.deleteSample$.next(this.sample)
    }

    public modify(): void {
        this.sampleService.modifySample$.next(this.sample)
    }

}
