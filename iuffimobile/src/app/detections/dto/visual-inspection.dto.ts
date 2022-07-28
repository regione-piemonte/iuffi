import { CoordinateDto } from './coordinate.dto';
import { PhotoDto } from './photo.dto';

export interface UbicazioneDto {
    numero?: string;
    nome?: string;
    cognome?: string;
    indirizzo?: string;
    telefono?: string;
    email?: string;
}

export interface InspectionDto {
    quantita: number;
    coordinate: CoordinateDto;
    ubicazione: UbicazioneDto;
    positivita?: number;
    diametro?: number;
    flagTreeClimberIspezione?: string;
    flagTreeClimberTaglio?: string;
    note1?: string;
    note2?: string;
    note3?: string;
}

export interface VisualInspectionDto {
    idIspezione: number;
    idRilevazione: number;
    numeroAviv: number;
    idSpecieVegetale: number;
    superficie?: number;
    numeroPiante: number;
    flagPresenzaOn: string;
    comune: string;
    cuaa: string;
    istatComune: string;
    latitudine: number;
    longitudine: number;
    note?: string;
    foto: boolean;
    ispezioni: InspectionDto[];
    flagIndicatoreIntervento?: string;
    riferimentoUbicazione?: string;
    area: string;
    ispettore: string;
    specie: string;
    organismi: any[];
    idMissione: number;
    photos: PhotoDto[];
    oraInizio: string;
    oraFine: string;
    dataInizio: string;
    dataOraInizio: number;
    dataOraFine: number;
}
