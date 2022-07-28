import { VisualInspectionDto } from '@app/detections/dto/visual-inspection.dto';

import { SampleDto } from './sample.dto';
import { TrappingDto } from './trapping.dto';

export interface DetectionDto {
    utente?: any;
    dataModifica?: any;
    idRilevazione: number;
    idMissione: number;
    idAnagrafica: number;
    idTipoArea: number;
    visual: string;
    note: string;
    campionamento: string;
    trappolaggio: string;
    oraInizio: string;
    oraFine: string;
    flagEmergenza: string;
    operatore: string;
    area: string;
    ispezioniVisive?: VisualInspectionDto[];
    campionamenti?: SampleDto[];
    trappolaggi?: TrappingDto[];
    cuaa : string;
    idUte : string;
    numeroAviv : string;
}
