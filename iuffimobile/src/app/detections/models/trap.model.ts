import * as Moment from 'moment';

import { TrapDto } from '../dto/trap.dto';

export class Trap {
    public idTrappola: number | null | undefined;
    public codiceSfr?: string | null = null;
    public dataInstallazione?: string | null = null;
    public dataRimozione?: string | null = null;
    public idSpecieVeg: number | undefined;
    public specie = '';
    public idTipoTrappola: number | null | undefined;
    public idOrganismoNocivo: number | null | undefined;
    public latitudine = 0;
    public longitudine = 0;
    public descrTrappola: string | null | undefined;
    constructor(trappolaDto?: TrapDto) {
        if (trappolaDto) {
            this.idTrappola = trappolaDto.idTrappola;
            this.codiceSfr = trappolaDto.codiceSfr;
            this.dataInstallazione = Moment(trappolaDto.dataInstallazione, 'DD.mm.YYYY').format('mm/DD/YYYY');
            this.dataRimozione = trappolaDto.dataRimozione;
            this.idSpecieVeg = trappolaDto.idSpecieVeg;
            this.specie = trappolaDto.specie;
            this.idTipoTrappola = trappolaDto.idTipoTrappola;
            this.idOrganismoNocivo = trappolaDto.idOrganismoNocivo;
            this.latitudine = trappolaDto.latitudine;
            this.longitudine = trappolaDto.longitudine;
            this.descrTrappola = trappolaDto.descrTrappola;
        }
    }
}
