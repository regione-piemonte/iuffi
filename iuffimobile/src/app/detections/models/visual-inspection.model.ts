import { InspectionDto, UbicazioneDto, VisualInspectionDto } from '../dto/visual-inspection.dto';
import { Coordinate } from './coordinate.model';
import { Photo } from './photo.model';

export class Ubicazione {
    public numero?: string | null = null;
    public nome?: string | null = null;
    public cognome?: string | null = null;
    public indirizzo?: string | null = null;
    public telefono?: string | null = null;
    public email?: string | null = null;
    constructor(ubicazioneDto?: UbicazioneDto) {
        if (ubicazioneDto) {
            this.numero = ubicazioneDto.numero;
            this.nome = ubicazioneDto.nome;
            this.cognome = ubicazioneDto.cognome;
            this.indirizzo = ubicazioneDto.indirizzo;
            this.telefono = ubicazioneDto.telefono;
            this.email = ubicazioneDto.email;
        }
    }
}

export class Inspection {
    public quantita = 0;
    public coordinate: Coordinate | null = null;
    public ubicazione: Ubicazione = new Ubicazione();
    public positivita?: number | null = null;
    public diametro?: number | null = null;
    public flagTreeClimberIspezione?: string | null = null;
    public flagTreeClimberTaglio?: string | null = null;
    public note1?: string | null = null;
    public note2?: string | null = null;
    public note3?: string | null = null;

    constructor(ispezioniDto?: InspectionDto) {
        if (ispezioniDto) {
            this.quantita = ispezioniDto.quantita;
            this.coordinate = ispezioniDto.coordinate;
            this.ubicazione = ispezioniDto.ubicazione ? ispezioniDto.ubicazione : new Ubicazione();
            this.positivita = ispezioniDto.positivita;
            this.diametro = ispezioniDto.diametro;
            this.flagTreeClimberIspezione = ispezioniDto.flagTreeClimberIspezione;
            this.flagTreeClimberTaglio = ispezioniDto.flagTreeClimberTaglio;
            this.note1 = ispezioniDto.note1;
            this.note2 = ispezioniDto.note2;
            this.note3 = ispezioniDto.note3;
        }
    }
}

export class VisualInspection {
    public idIspezione = 0;
    public idRilevazione = 0;
    public numeroAviv = 0;
    public idSpecieVegetale = 0;
    public superficie?= 0;
    public numeroPiante = 0;
    public flagPresenzaOn = 'N';
    public comune = '';
    public cuaa = '';
    public istatComune = '';
    public latitudine = 0;
    public longitudine = 0;
    public note?= '';
    public foto = false;
    public ispezioni: Inspection[] = [];
    public flagIndicatoreIntervento?= 'N';
    public riferimentoUbicazione?= '';
    public area = '';
    public ispettore = '';
    public specie = '';
    public organismi: any[] = [];
    public idMissione = 0;
    public photos: Photo[] = [];
    public oraInizio = '';
    public oraFine = '';
    public dataInizio = '';
    public dataOraInizio = 0;
    public dataOraFine = 0;

    constructor(visualInspectionDto?: VisualInspectionDto) {
        if (visualInspectionDto) {
            this.idIspezione = visualInspectionDto.idIspezione;
            this.idRilevazione = visualInspectionDto.idRilevazione;
            this.numeroAviv = visualInspectionDto.numeroAviv;
            this.idSpecieVegetale = visualInspectionDto.idSpecieVegetale;
            this.superficie = visualInspectionDto.superficie;
            this.numeroPiante = visualInspectionDto.numeroPiante;
            this.flagPresenzaOn = visualInspectionDto.flagPresenzaOn;
            this.comune = visualInspectionDto.comune;
            this.cuaa = visualInspectionDto.cuaa;
            this.istatComune = visualInspectionDto.istatComune;
            this.latitudine = visualInspectionDto.latitudine;
            this.longitudine = visualInspectionDto.longitudine;
            this.note = visualInspectionDto.note;
            this.foto = visualInspectionDto.foto;
            this.ispezioni = visualInspectionDto.ispezioni ? visualInspectionDto.ispezioni.map(item => new Inspection(item)) : [];
            this.flagIndicatoreIntervento = visualInspectionDto.flagIndicatoreIntervento;
            this.riferimentoUbicazione = visualInspectionDto.riferimentoUbicazione;
            this.area = visualInspectionDto.area;
            this.ispettore = visualInspectionDto.ispettore;
            this.specie = visualInspectionDto.specie;
            this.organismi = visualInspectionDto.organismi;
            this.idMissione = visualInspectionDto.idMissione;
            this.photos = visualInspectionDto.photos ? visualInspectionDto.photos.map(item => new Photo(item)) : [];
            this.oraInizio = visualInspectionDto.oraInizio;
            this.dataInizio = visualInspectionDto.dataInizio;
            this.dataOraInizio = visualInspectionDto.dataOraInizio;
            this.dataOraFine = visualInspectionDto.dataOraFine;
            this.oraFine = visualInspectionDto.oraFine;
        }
    }
}
