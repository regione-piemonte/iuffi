import { AvivRegistryDto } from '../dto/registry-aviv.dto';

export interface Esito {
	codErr: number;
	descErr?: any;
}

export interface ArrCA {
	idUte: number;
	denomiUte?: any;
	comuneUte: string;
	indirizzoUte: string;
	numProgr: number;
}

export interface GetAnagraficaAvivResponse {
	esito: Esito;
    dati: AvivRegistryDto;
}
