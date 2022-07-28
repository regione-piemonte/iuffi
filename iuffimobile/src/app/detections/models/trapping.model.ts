import { TrappingDto } from '../dto/trapping.dto';
import { Photo } from './photo.model';
import { Trap } from './trap.model';

export class Trapping {
    public idTrappolaggio = 0;
    public idMissione = 0;
    public idRilevazione = 0;
    public idOperazione = 0;//1 =Installazione; 2=Manutenzione/Controllo; 3=Ricarica; 4=Rimozione
    public idIspezioneVisiva = 0;
    public idSpecieVegetale = 0;
    public istatComune = '';
    public ispettore = '';
    public dataTrappolaggio = '';
    public descrOperazione = '';
    public dataOraInizio = '';
    public dataOraFine?= '';
    public operazione?= '';
    public descrTrappola = '';
    public specie = '';
    public latitudine = 0;
    public longitudine = 0;
    public foto = false;
    public photos: Photo[] = [];
    public trappola: Trap = new Trap();
    public note = '';
    public idOrganismoNocivo = 0;
    public dataOraInizioT = 0;
    public dataOraFineT = 0;

    constructor(trapDto?: TrappingDto) {
        if (trapDto) {
            this.idTrappolaggio = trapDto.idTrappolaggio;
            this.idMissione = trapDto.idMissione;
            this.idRilevazione = trapDto.idRilevazione;
            this.idOperazione = trapDto.idOperazione;
            this.idIspezioneVisiva = trapDto.idIspezioneVisiva;
            this.idSpecieVegetale = trapDto.idSpecieVegetale;
            this.trappola = trapDto.trappola ? trapDto.trappola : new Trap();
            this.istatComune = trapDto.istatComune;
            this.dataTrappolaggio = trapDto.dataTrappolaggio;
            this.descrOperazione = trapDto.descrOperazione;
            this.dataOraInizio = trapDto.dataOraInizio;
            this.dataOraFine = trapDto.dataOraFine;
            this.operazione = trapDto.operazione;
            this.descrTrappola = trapDto.descrTrappola;
            this.specie = trapDto.specie;
            this.latitudine = trapDto.latitudine;
            this.longitudine = trapDto.longitudine;
            this.photos = trapDto.photos ? trapDto.photos.map(item => new Photo(item)) : [];
            this.note = trapDto.note;
            this.foto = trapDto.foto;
            this.ispettore = trapDto.ispettore
            this.idOrganismoNocivo = trapDto.idOrganismoNocivo;
            this.dataOraInizioT = trapDto.dataOraInizioT;
            this.dataOraFineT = trapDto.dataOraFineT;
        }
    }
}
