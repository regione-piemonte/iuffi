import { MonitoringSummaryDto } from '../dto/offline.dto';

export class MonitoringSummary {
    public idSpecieVegetale = 0;
    public idTipoCampione = 0;
    public mese = 0;
    public idOrganismoNocivo = 0;
    public dataInizioValiditaF = '';
    public dataFineValiditaF = '';
    constructor(monitoringSummaryDto: MonitoringSummaryDto) {
        if (monitoringSummaryDto) {
            this.idSpecieVegetale = monitoringSummaryDto.idSpecieVegetale;
            this.idTipoCampione = monitoringSummaryDto.idTipoCampione;
            this.mese = monitoringSummaryDto.mese;
            this.idOrganismoNocivo = monitoringSummaryDto.idOrganismoNocivo;
            this.dataInizioValiditaF = monitoringSummaryDto.dataInizioValiditaF;
            this.dataFineValiditaF = monitoringSummaryDto.dataFineValiditaF;
        }
    }
}
