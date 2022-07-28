import { DetectionDto } from '../../detections/dto/detection.dto';
import { InspectorDto } from './inspector.dto';

export interface MissionDto {
    idMissione: number;
    numeroTrasferta?: number;
    pdf?: boolean;
    dataMissione: string;
    oraInizio: string;
    oraFine?: string;
    idIspettoreAssegnato?: number;
    cfIspettore: string;
    nomeIspettore: string;
    cognomeIspettore: string;
    ispettoriAggiunti?: InspectorDto[];
    numRilevazioni?: number;
    rilevazioni?: DetectionDto[];
}
