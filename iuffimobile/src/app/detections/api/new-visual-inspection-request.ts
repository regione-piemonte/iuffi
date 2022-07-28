import { Inspection } from '../models/visual-inspection.model';

export class VisualInspectionRequest {
    public idIspezione = 0;
    public idRilevazione = 0;
    public numeroAviv = 0;
    public idSpecieVegetale = 0;
    public superficie = 0;
    public numeroPiante = 0;
    public flagPresenzaOn = 'N';
    public comune = '';
    public cuaa = '';
    public istatComune = '';
    public latitudine = 0;
    public longitudine = 0;
    public note = '';
    public ispezioni = [];
    public flagIndicatoreIntervento = 'N';
    public riferimentoUbicazione = '';
    public area = '';
    public ispettore = '';
    public specie = '';
    public organismi = [];
    public idMissione = 0;
    public oraInizio = '';
    public oraFine = '';

    constructor(request?: any) {
        if (request) {
            this.idIspezione = request.idIspezione;
            this.idRilevazione = request.idRilevazione;
            this.numeroAviv = request.numeroAviv;
            this.idSpecieVegetale = request.idSpecieVegetale;
            this.superficie = request.superficie;
            this.numeroPiante = request.numeroPiante;
            this.flagPresenzaOn = request.flagPresenzaOn;
            this.comune = request.comune;
            this.cuaa = request.cuaa;
            this.istatComune = request.istatComune;
            this.latitudine = request.latitudine;
            this.longitudine = request.longitudine;
            this.note = request.note;
            this.ispezioni = request.ispezioni ? request.ispezioni.map((item: any) => new Inspection(item)) : [];
            this.flagIndicatoreIntervento = request.flagIndicatoreIntervento;
            this.riferimentoUbicazione = request.riferimentoUbicazione;
            this.area = request.area;
            this.ispettore = request.ispettore;
            this.specie = request.specie;
            this.organismi = request.organismi;
            this.idMissione = request.idMissione;
            this.oraInizio = request.oraInizio;
            this.oraFine= request.oraFine;
        }
    }
}
