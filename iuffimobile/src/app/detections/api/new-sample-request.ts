import { Sample } from '../models/sample.model';

export class SampleRequest {
    public idCampionamento = 0;
    public idRilevazione = 0;
    public idSpecieVegetale = 0;
    public idTipoCampione = 0;
    public idIspezioneVisiva = 0;
    public comune = '';
    public istatComune = ''
    public latitudine = 0
    public longitudine = 0;
    public organismiNocivi: number[] = [];
    public presenza = 'S';
    public oraInizio = '';
    public oraFine = '';
    public note = '';

    constructor(request?: Sample) {
        if (request) {
            this.idCampionamento = request.idCampionamento;
            this.idRilevazione = request.idRilevazione;
            this.idSpecieVegetale = request.idSpecieVegetale;
            this.idTipoCampione = request.idTipoCampione;
            this.idIspezioneVisiva = request.idIspezioneVisiva;
            this.latitudine = request.latitudine;
            this.longitudine = request.longitudine;
            this.organismiNocivi = request.organismiNocivi;
            this.presenza = request.presenza;
            this.istatComune = request.istatComune;
            this.comune = request.comune;
            this.oraInizio = request.oraInizio;
            this.oraFine = request.oraFine;
            this.note = request.note;
        }
    }
}
