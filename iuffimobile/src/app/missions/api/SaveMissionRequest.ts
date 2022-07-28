import { Detection } from '../../detections/models/detection.model';

export interface IspettoriAggiunti {
	idAnagrafica: number;
}

export class SaveMissionRequest {
	public idMissione?: number;
	public numeroTrasferta?: number;
	public dataMissione: string;
	public oraInizio: string;
    public oraFine?: string;
    public idIspettoreAssegnato?: number;
	public cognomeIspettore: string;
    public nomeIspettore: string;
	public cfIspettore: string;
    public numRilevazioni?: number;
	public rilevazioni?: Detection[];
    public ispettoriAggiunti?: IspettoriAggiunti[] | null;

    constructor(request: any) {
        this.idMissione = request.idMissione || 0;
        this.numeroTrasferta = request.numeroTrasferta;
        this.dataMissione = request.dataMissione;
        this.oraInizio = request.oraInizio;
        this.oraFine = request.oraFine;
        this.idIspettoreAssegnato = request.idIspettoreAssegnato;
        this.cognomeIspettore = request.cognomeIspettore;
        this.nomeIspettore = request.nomeIspettore;
        this.cfIspettore = request.cfIspettore;
        this.numRilevazioni = request.numRilevazioni;
        this.rilevazioni = request.rilevazioni;
        this.ispettoriAggiunti = request.ispettoriAggiunti || null;
    }
}
