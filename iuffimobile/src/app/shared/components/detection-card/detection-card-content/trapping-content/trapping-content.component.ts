import { Component, Input } from '@angular/core';
import { GetPhotoRequest } from '@app/detections/api/get-photos-request';
import { Photo } from '@app/detections/models/photo.model';
import { Trapping } from '@app/detections/models/trapping.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { TrapService } from '@app/detections/services/trap.service';
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
    selector: 'trapping-content-component',
    templateUrl: 'trapping-content.component.html',
    styleUrls: ['trapping-content.component.scss']
})
export class TrappingContentComponent {
    public _isSkeleton = true;
    public moment: any = Moment();
    public MissionStatus = MissionStatus;
    public isEditable = false;
    public comune: Comune | null = null;
    public photos: Photo[] = [];
    public harmfulOrganismsAdded: HarmfulOrganism[] = [];
    public specieVegetaleDetail = '';
    @Input() public trap!: Trapping;
    @Input() public isRelatedToEmergency = false;
    constructor(
        private missionService: MissionService,
        private trapService: TrapService,
        private geoUtilsService: GeoUtilsService,
        public fileManagerService: FileManagerService,
        private deviceService: DeviceService,
        private detectionService: DetectionService
    ) {

    }

    ngOnInit(): void {
        const mission = this.missionService.getMission(this.trap.idMissione)
        if (mission) {
            this.isEditable = !mission.isOutdated();
        }

        if (this.trap.longitudine && this.trap.longitudine) {
            const point = new Point(this.trap.latitudine, this.trap.longitudine);
            this.comune = this.geoUtilsService.getComuneByPoint(point);
        }

        const photoRequest: GetPhotoRequest = {
            idTrappolaggio: this.trap.idTrappolaggio,
            idVisual: 0,
            idCampionamento: 0
        }

        if (this.trap.foto && this.deviceService.isCordova()) {
            this.trapService.fetchPhotos(photoRequest).then(res => {
                this.photos = this.trapService.getTrapPhotos(this.trap.idTrappolaggio);
            }).catch(err => {
            });
        }

        this.specieVegetaleDetail = this.trap.specie;
        if (this.trap.trappola.idSpecieVeg) {
            const specieDetail = this.detectionService.getPlantSpeciesDetail(this.trap.trappola.idSpecieVeg)as PlantSpeciesDetail;
            if (specieDetail) {
                this.specieVegetaleDetail = specieDetail.nomeVolgare;
            }
        }

        const harmfulOrganism = this.detectionService.getHarmfulOrganism(this.trap.idOrganismoNocivo);
        if (harmfulOrganism) {
            this.harmfulOrganismsAdded.push(harmfulOrganism);
        }
    }

    ngOnChanges(): void {
        console.log('ngOnChanges');
        this._isSkeleton = (this.trap === null);

    }

    public delete(): void {
        this.trapService.deleteTrap$.next(this.trap)
    }

    public modify(): void {
        this.trapService.modifyTrap$.next(this.trap)
    }

}
