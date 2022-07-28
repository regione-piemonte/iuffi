import { PlantSpeciesAvivDto } from '../dto/plant-species-aviv.dto';

export interface Esito {
	codErr: number;
	descErr?: any;
}

export interface GetSpecieAvivResponse {
	esito: Esito;
    dati: PlantSpeciesAvivDto[];
}
