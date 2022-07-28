import { Trap } from '../models/trap.model';
import { Trapping } from '../models/trapping.model';

export class TrapRequest {
    public idTrappolaggio = 0;
    public idRilevazione = 0;
    public idIspezioneVisiva!: number | null;
    public idOperazione = 0; // 1=Installazione; 2=Manutenzione/Controllo; 3=Ricarica; 4=Rimozione
    public istatComune = '';
    public note = '';
    public dataTrappolaggio = '';
    public trappola!: Trap | null;
    public idOrganismoNocivo!: number | null;
    public dataOraInizio = '';

    constructor(request?: Trapping) {
        if (request) {
            this.idTrappolaggio = request.idTrappolaggio;
            this.idRilevazione = request.idRilevazione;
            this.idOperazione = request.idOperazione;
            this.istatComune = request.istatComune;
            this.dataTrappolaggio = request.dataTrappolaggio;
            this.note = request.note;
            this.idIspezioneVisiva = request.idIspezioneVisiva ? request.idIspezioneVisiva : null;
            this.trappola = request.trappola ? request.trappola : null;
            this.idOrganismoNocivo = request.idOrganismoNocivo ? request.idOrganismoNocivo : null;
            this.dataOraInizio = request.dataOraInizio;
        }
    }
}
