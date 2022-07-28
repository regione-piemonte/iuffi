import { TrapTypeDto } from '../dto/offline.dto';

export class TrapType {
    public idTipoTrappola = 0;
    public tipologiaTrappola = '';
    public sfrCode = '';
    public specie: any;
    public dataInizioValiditaF = '';
    public dataFineValiditaF = '';

    constructor(trapTypeDto?: TrapTypeDto) {
        if (trapTypeDto) {
            this.idTipoTrappola = trapTypeDto.idTipoTrappola;
            this.tipologiaTrappola = trapTypeDto.tipologiaTrappola ? trapTypeDto.tipologiaTrappola.toLowerCase() : '';
            this.sfrCode = trapTypeDto.sfrCode;
            this.specie = trapTypeDto.specie;
            this.dataInizioValiditaF = trapTypeDto.dataInizioValiditaF;
            this.dataFineValiditaF = trapTypeDto.dataFineValiditaF;
        }
    }
}
