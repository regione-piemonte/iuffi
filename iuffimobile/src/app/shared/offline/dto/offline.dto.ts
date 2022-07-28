export interface AreaTypeDto {
    codiceUfficiale: string;
    descTipoArea: string;
    codiceAppInCampo: string;
    dettaglioTipoAree: AreaTypeDetailDto[];
}

export interface AreaTypeDetailDto {
    id: number;
    codiceUfficiale: string;
    descrizione: string;
    dataInizioValidita: string;
    dataFineValidita: string;
}

export interface AreaTypeLocalDto {
    id: number;
    descrizione: string;
    dataInizioValidita: string;
    dataFineValidita: string;
}

export interface PlantSpeciesDto {
    idSpecieVegetale: number;
    genereSpecie: string;
    flagEuro: string;
    listaSpecieVegetali: PlantSpeciesDetailDto[];
}
export interface PlantSpeciesDetailDto {
    idSpecieVegetale: number;
    nomeVolgare: string;
    dataInizioValidita: string;
    dataFineValidita: string;
}
export interface SampleTypeDto {
    idTipoCampione: number;
    tipologiaCampione: string;
    dataInizioValiditaF: string;
    dataFineValiditaF: string;
}

export interface HarmfulOrganismDto {
    idOrganismoNocivo: number;
    nomeLatino: string;
    sigla: string;
    euro: string;
    flagEmergenza: string;
    nomeCompleto: string;
    dataInizioValiditaF: string;
    dataFineValiditaF: string;
}

export interface TrapTypeDto {
    idTipoTrappola: number;
    tipologiaTrappola: string;
    sfrCode: string;
    specie?: any;
    dataInizioValiditaF: string;
    dataFineValiditaF: string;
}

export interface MonitoringSummaryDto {
    idSpecieVegetale: number;
    idTipoCampione: number;
    mese: number;
    idOrganismoNocivo: number;
    dataInizioValiditaF: string;
    dataFineValiditaF: string;
}
export interface TrapTypeByHarmfulOrganismDto {
	idTrappola: number;
	idOn: number;
	descrTipoTrappola: string;
	descrOn: string;
	dataInizioValiditaF: string;
	dataFineValiditaF: string;
}
