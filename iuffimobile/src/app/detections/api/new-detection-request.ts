import * as Moment from 'moment';

export class DetectionRequest {
    public idRilevazione = 0;
	public idMissione = 0;
    public idAnagrafica = 0;
    public idTipoArea: number | null = null;
    public visual = 'N';
    public note = '';
    public campionamento = 'N';
    public trappolaggio = 'N';
    public oraInizio = Moment().format('HH:mm');
    public oraFine = '';
    public flagEmergenza = 'N';
    public cuaa = null;
    public idUte = null;
    public numeroAviv = null;

    constructor(request?: any) {
        if (request) {
            this.idRilevazione = request.idRilevazione || 0;
            this.idMissione = request.idMissione || 0;
            this.idAnagrafica = request.idAnagrafica;
            this.idTipoArea = request.idTipoArea;
            this.visual = request.visual;
            this.note = request.note;
            this.campionamento = request.campionamento;
            this.trappolaggio = request.trappolaggio;
            this.oraInizio = request.oraInizio;
            this.oraFine = request.oraFine;
            this.flagEmergenza = request.flagEmergenza;
            this.cuaa = request.cuaa? request.cuaa : null;
            this.idUte = request.idUte ? request.idUte  : null;
            this.numeroAviv = request.numeroAviv ? request.numeroAviv  :null;
        }
    }
}
