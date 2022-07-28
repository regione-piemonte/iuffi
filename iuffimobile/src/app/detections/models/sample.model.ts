import { SampleDto } from '../dto/sample.dto';
import { Coordinate } from './coordinate.model';
import { Photo } from './photo.model';

export class Sample {
    public idCampionamento = 0;
    public idRilevazione = 0;
    public idSpecieVegetale = 0;
    public idTipoCampione = 0;
    public idIspezioneVisiva = 0;
    public comune = '';
    public cuaa = '';
    public istatComune = '';
    public idAnfi = 0;
    public idEsito = 0;
    public latitudine = 0;
    public longitudine = 0;
    public dataRilevazione = 0;
    public ispettore = '';
    public specie = '';
    public organismi: number[] = [];
    public organismiNocivi: number[] = [];
    public tipoCampione = '';
    public esito = '';
    public note = '';
    public presenza = 'S';
    public coordinate: Coordinate | null = null;
    public idMissione = 0;
    public idRegistroCampioni?= 0;
    public anagraficaAziendale?: any;
    public idTecnicoLab?= 0;
    public idOrganismoNocivo?= 0;
    public foto = false;
    public photos: Photo[] = [];
    public oraInizio = '';
    public dataOraInizio = '';
    public oraFine = '';
    public dataOraFine = '';

    constructor(sampleDto?: SampleDto) {
        if (sampleDto) {
            this.idCampionamento = sampleDto.idCampionamento;
            this.idRilevazione = sampleDto.idRilevazione;
            this.idSpecieVegetale = sampleDto.idSpecieVegetale;
            this.idTipoCampione = sampleDto.idTipoCampione;
            this.idIspezioneVisiva = sampleDto.idIspezioneVisiva;
            this.istatComune = sampleDto.istatComune;
            this.idAnfi = sampleDto.idAnfi;
            this.idEsito = sampleDto.idEsito;
            this.latitudine = sampleDto.latitudine;
            this.longitudine = sampleDto.longitudine;
            this.dataRilevazione = sampleDto.dataRilevazione;
            this.ispettore = sampleDto.ispettore;
            this.specie = sampleDto.specie;
            this.organismiNocivi = sampleDto.organismiNocivi;
            this.tipoCampione = sampleDto.tipoCampione;
            this.esito = sampleDto.esito;
            this.note = sampleDto.note;
            this.presenza = 'S';
            this.idMissione = sampleDto.idMissione;
            this.idRegistroCampioni = sampleDto.idRegistroCampioni;
            this.anagraficaAziendale = sampleDto.anagraficaAziendale;
            this.idTecnicoLab = sampleDto.idTecnicoLab;
            this.idOrganismoNocivo = sampleDto.idOrganismoNocivo;
            this.foto = sampleDto.foto;
            this.photos = sampleDto.photos ? sampleDto.photos.map(item => new Photo(item)) : [];
            this.oraInizio = sampleDto.oraInizio;
            this.dataOraInizio = sampleDto.dataOraInizio;
            this.oraFine = sampleDto.oraFine;
            this.dataOraFine = sampleDto.dataOraFine;
        }
    }
}
