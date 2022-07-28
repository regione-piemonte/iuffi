import { PhotoDto } from './photo.dto';

export interface SampleDto {
    idCampionamento: number;
    idRilevazione: number;
    idSpecieVegetale: number;
    idTipoCampione: number;
    idIspezioneVisiva: number;
    istatComune: string;
    idAnfi: number;
    idEsito: number;
    latitudine: number;
    longitudine: number;
    dataRilevazione: number;
    ispettore: string;
    specie: string;
    area: string;
    organismiNocivi: number[];
    tipoCampione: string;
    esito: string;
    note: string;
    presenza: string;
    idMissione: number;
    idRegistroCampioni?: any;
    anagraficaAziendale?: any;
    idTecnicoLab?: any;
    idOrganismoNocivo?: any;
    photos: PhotoDto[];
    foto: boolean;
    oraInizio: string;
    dataOraInizio: string;
    oraFine: string;
    dataOraFine: string;
}
