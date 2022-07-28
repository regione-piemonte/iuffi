import { ReportAvivDto } from '../dto/report-aviv.dto';

export interface Esito {
	codErr: number;
	descErr?: any;
}

export interface GetReportResponse {
	esito: Esito;
    dati: ReportAvivDto;
}
