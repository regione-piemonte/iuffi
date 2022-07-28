import { PlantSpeciesAvivDto } from '../dto/plant-species-aviv.dto';

export class PlantSpeciesAviv {
    public denomAutorizzazione = '';
    public denomAttivita = '';
    public specieNomeBotanico = '';
    public specieNomeVolgare = '';
    public flgPassUpp = false;
    public flgPassZp = false;
    public flgAccr = false;
    public flgColtCampo = false;
    public flgColtCont = false;
    public flgColtSerra = false;
    public qtaProduzione = 0;
    public unitMisura = '';

    constructor(plantSpeciesAvivDto?: PlantSpeciesAvivDto) {
        if (plantSpeciesAvivDto) {
            this.denomAutorizzazione = plantSpeciesAvivDto.denomAutorizzazione;
            this.denomAttivita = plantSpeciesAvivDto.denomAttivita;
            this.specieNomeBotanico = plantSpeciesAvivDto.specieNomeBotanico;
            this.specieNomeVolgare = plantSpeciesAvivDto.specieNomeVolgare;
            this.flgPassUpp = plantSpeciesAvivDto.flgPassUpp;
            this.flgPassZp = plantSpeciesAvivDto.flgPassZp;
            this.flgAccr = plantSpeciesAvivDto.flgAccr;
            this.flgColtCampo = plantSpeciesAvivDto.flgColtCampo;
            this.flgColtCont = plantSpeciesAvivDto.flgColtCont;
            this.flgColtSerra = plantSpeciesAvivDto.flgColtSerra;
            this.qtaProduzione = plantSpeciesAvivDto.qtaProduzione;
            this.unitMisura = plantSpeciesAvivDto.unitMisura;
        }
    }
}
