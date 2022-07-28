import { PlantSpeciesDetailDto, PlantSpeciesDto } from './../dto/offline.dto';

export class PlantSpecies {
    public idSpecieVegetale = 0;
    public genereSpecie = '';
    public flagEuro = false;
    public listaSpecieVegetali: PlantSpeciesDetail[] = [];

    constructor(plantSpeciesDto?: PlantSpeciesDto) {
        if (plantSpeciesDto) {
            this.idSpecieVegetale = plantSpeciesDto.idSpecieVegetale
            this.genereSpecie = plantSpeciesDto.genereSpecie ? plantSpeciesDto.genereSpecie.toLowerCase() : '';
            this.flagEuro = plantSpeciesDto.flagEuro === 'N' ? false : true;;
            this.listaSpecieVegetali = plantSpeciesDto.listaSpecieVegetali ? plantSpeciesDto.listaSpecieVegetali.map(item => new PlantSpeciesDetail(item)) : [];;
        }
    }
}

export class PlantSpeciesDetail {
    public idSpecieVegetale = 0;
    public nomeVolgare = '';
    public dataInizioValidita = '';
    public dataFineValidita = '';

    constructor(plantSpeciesDetailDto?: PlantSpeciesDetailDto) {
        if (plantSpeciesDetailDto) {
            this.idSpecieVegetale = plantSpeciesDetailDto.idSpecieVegetale;
            this.nomeVolgare = plantSpeciesDetailDto.nomeVolgare ? plantSpeciesDetailDto.nomeVolgare.toLowerCase() : '';
            this.dataInizioValidita = plantSpeciesDetailDto.dataInizioValidita;
            this.dataFineValidita = plantSpeciesDetailDto.dataFineValidita;
        }
    }
}
