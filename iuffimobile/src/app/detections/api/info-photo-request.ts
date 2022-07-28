import { Photo } from '../models/photo.model';

export class InfoPhotoRequest {
    public idMissione = 0;
    public idVisual = 0;
    public idCampionamento = 0;
    public idTrappolaggio = 0;
    public longitudine = 0;
    public latitudine = 0;
    public note = '';
    public nomeFile = '';

    constructor(photo?: Photo) {
        if (photo) {
            this.idMissione = photo.missionId;
            this.idVisual = photo.visualInspectionId;
            this.idCampionamento = photo.sampleId;
            this.idTrappolaggio = photo.trapId;
            this.latitudine = photo.latitude;
            this.longitudine = photo.longitude;
            this.nomeFile = photo.fileName;
        }
    }
}
