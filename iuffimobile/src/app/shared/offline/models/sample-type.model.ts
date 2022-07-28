import { SampleTypeDto } from '../dto/offline.dto';

export class SampleType {
    public idTipoCampione = 0;
    public tipologiaCampione = '';
    public dataInizioValiditaF = '';
    public dataFineValiditaF = '';

    constructor(sampleTypeDto?: SampleTypeDto) {
        if (sampleTypeDto) {
            this.idTipoCampione = sampleTypeDto.idTipoCampione;
            this.tipologiaCampione = sampleTypeDto.tipologiaCampione.toLowerCase();
            this.dataInizioValiditaF = sampleTypeDto.dataInizioValiditaF;
            this.dataFineValiditaF = sampleTypeDto.dataFineValiditaF;
        }
    }
}
