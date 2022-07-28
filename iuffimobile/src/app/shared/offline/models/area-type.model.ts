import { AreaTypeDetailDto, AreaTypeDto } from './../dto/offline.dto';

export class AreaType {

    public codiceUfficiale = '';
    public codiceAppInCampo = '';
    public descTipoArea = '';
    public dettaglioTipoAree: AreaTypeDetailDto[] | undefined;

    constructor(areaTypeDto?: AreaTypeDto) {
        if (areaTypeDto) {
            this.codiceUfficiale = areaTypeDto.codiceUfficiale;
            this.descTipoArea = areaTypeDto.descTipoArea;
            this.codiceAppInCampo = areaTypeDto.codiceAppInCampo;
            this.dettaglioTipoAree = areaTypeDto.dettaglioTipoAree;
        }
    }
}

export class AreaTypeDetail {
    public id = 0;
    public descrizione = '';
    public dataInizioValidita = '';
    public dataFineValidita = '';

    constructor(areaTypeDetailDto?: AreaTypeDetailDto) {
        if (areaTypeDetailDto) {
            this.id = areaTypeDetailDto.id;
            this.descrizione = areaTypeDetailDto.descrizione;
            this.dataInizioValidita = areaTypeDetailDto.dataInizioValidita;
            this.dataFineValidita = areaTypeDetailDto.dataFineValidita;
        }
    }
}

export class AreaTypeLocal {
    public id = 0;
    public codiceUfficiale = '';
    public descrizione = '';
    public dataInizioValidita = '';
    public dataFineValidita = '';

    constructor(areaTypeDetailDto?: AreaTypeDetailDto) {
        if (areaTypeDetailDto) {
            this.id = areaTypeDetailDto.id;
            this.codiceUfficiale = areaTypeDetailDto.codiceUfficiale;
            this.descrizione = areaTypeDetailDto.descrizione ? areaTypeDetailDto.descrizione.toLowerCase() : '';
            this.dataInizioValidita = areaTypeDetailDto.dataInizioValidita;
            this.dataFineValidita = areaTypeDetailDto.dataFineValidita;
        }
    }
}
