import * as Moment from 'moment';

import { DetectionDto } from '../dto/detection.dto';
import { SampleDto } from '../dto/sample.dto';
import { TrappingDto } from '../dto/trapping.dto';
import { VisualInspectionDto } from '../dto/visual-inspection.dto';
import { Sample } from './sample.model';
import { Trapping } from './trapping.model';
import { VisualInspection } from './visual-inspection.model';

export class Detection {
    public utente?: any;
    public dataModifica?: any;
    public idRilevazione = 0;
    public idMissione = 0;
    public idAnagrafica = 0;
    public idTipoArea: number | null = null;
    public visual = 'N';
    public note = '';
    public campionamento = 'N';
    public trappolaggio = 'N';
    public oraInizio = Moment().format('HH:mm');
    public dataRilevazione = '';
    public flagEmergenza = 'N';
    public oraFine = '';
    public operatore = '';
    public area = '';
    public ispezioniVisive?: VisualInspection[];
    public campionamenti?: Sample[];
    public trappolaggi?: Trapping[];
    public cuaa = '';
    public idUte = '';
    public numeroAviv = '';

    constructor(detectionDto?: DetectionDto) {
        if (detectionDto) {
            this.utente = detectionDto.utente;
            this.dataModifica = detectionDto.dataModifica;
            this.idRilevazione = detectionDto.idRilevazione;
            this.idMissione = detectionDto.idMissione;
            this.idAnagrafica = detectionDto.idAnagrafica;
            this.idTipoArea = detectionDto.idTipoArea;
            this.visual = detectionDto.visual;
            this.note = detectionDto.note;
            this.campionamento = detectionDto.campionamento;
            this.trappolaggio = detectionDto.trappolaggio;
            this.oraInizio = detectionDto.oraInizio;
            this.oraFine = detectionDto.oraFine;
            this.flagEmergenza = detectionDto.flagEmergenza;
            this.operatore = detectionDto.operatore;
            this.area = detectionDto.area;
            this.ispezioniVisive = detectionDto.ispezioniVisive ? detectionDto.ispezioniVisive.map((item: VisualInspectionDto) => new VisualInspection(item)) : [];
            this.campionamenti = detectionDto.campionamenti ? detectionDto.campionamenti.map((item: SampleDto) => new Sample(item)) : [];
            this.trappolaggi = detectionDto.trappolaggi ? detectionDto.trappolaggi.map((item: TrappingDto) => new Trapping(item)) : [];
            this.cuaa = detectionDto.cuaa;
            this.idUte = detectionDto.idUte;
            this.numeroAviv = detectionDto.numeroAviv;
        }
    }
}
