import { Trap } from '../models/trap.model';
import { PhotoDto } from './photo.dto';

export interface TrappingDto {
    idTrappolaggio: number;
    idMissione: number;
    idRilevazione: number;
    idOperazione: number;
    idIspezioneVisiva: number;
    idSpecieVegetale: number;
    istatComune: string;
    dataTrappolaggio: string;
    descrOperazione: string;
    dataOraInizio: string;
    dataOraFine?: string;
    operazione?: string;
    descrTrappola: string;
    specie: string;
    latitudine: number;
    longitudine: number;
    codiceSft: string;
    photos: PhotoDto[];
    foto: boolean;
    trappola: Trap;
    note: string;
    ispettore: string;
    idOrganismoNocivo: number;
    dataOraInizioT: number;
    dataOraFineT: number;
}
