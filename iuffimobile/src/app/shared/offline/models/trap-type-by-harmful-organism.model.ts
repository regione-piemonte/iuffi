import { TrapTypeByHarmfulOrganismDto } from '../dto/offline.dto';

export class TrapTypeByHarmfulOrganism {
    public idTrappola = 0;
    public idOn = 0;
    public descrTipoTrappola = '';
    public descrOn = '';
    public dataInizioValiditaF = '';
    public dataFineValiditaF = '';

    constructor(trapTypeByHarmfulOrganismDto?: TrapTypeByHarmfulOrganismDto) {
        if (trapTypeByHarmfulOrganismDto) {
            this.idTrappola = trapTypeByHarmfulOrganismDto.idTrappola;
            this.idOn = trapTypeByHarmfulOrganismDto.idOn;
            this.descrTipoTrappola = trapTypeByHarmfulOrganismDto.descrTipoTrappola;
            this.descrOn = trapTypeByHarmfulOrganismDto.descrOn;
            this.dataInizioValiditaF = trapTypeByHarmfulOrganismDto.dataInizioValiditaF;
            this.dataFineValiditaF = trapTypeByHarmfulOrganismDto.dataFineValiditaF;
        }
    }
}
