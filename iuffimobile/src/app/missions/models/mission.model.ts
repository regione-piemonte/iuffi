import * as Moment from 'moment';

import { Detection } from '../../detections/models/detection.model';
import { MissionDto } from '../dto/mission.dto';
import { Inspector } from './inspector.model';

export enum MissionStatus {
    DRAFT = 'DRAFT',
    TO_SYNCHRONIZE = 'TO_SYNCHRONIZE',
    SYNCHRONIZED = 'SYNCHRONIZED',
    FAILED = 'FAILED'
}

export class Mission {
    public idMissione = 0;
    public numeroTrasferta?: number;
    public pdf? = false;
    public dataMissione: number = new Date().getTime();
    public oraInizio = Moment().format('HH:mm');
    public oraFine?: string;
    public idIspettoreAssegnato?: number;
    public cfIspettore = ''
    public nomeIspettore = '';
    public cognomeIspettore = '';
    public ispettoriAggiunti?: Inspector[] = [];
    public numRilevazioni?: number;
    public rilevazioni?: Detection[] = [];
    public stato?: MissionStatus = MissionStatus.DRAFT

    constructor(mission?: MissionDto) {
        if (mission) {
            this.idMissione = mission.idMissione;
            this.numeroTrasferta = mission.numeroTrasferta;
            this.pdf = mission.pdf;
            this.dataMissione = Moment(mission.dataMissione, 'DD-MM-YYYY').set({ hour: 0, minute: 0, second: 0, millisecond: 0 }).toDate().getTime()
            this.oraInizio = mission.oraInizio;
            this.oraFine = mission.oraFine;
            this.idIspettoreAssegnato = mission.idIspettoreAssegnato;
            this.cfIspettore = mission.cfIspettore;
            this.nomeIspettore = mission.nomeIspettore;
            this.cognomeIspettore = mission.cognomeIspettore;
            this.ispettoriAggiunti = mission.ispettoriAggiunti ? mission.ispettoriAggiunti.map(item => new Inspector(item)) : [];
            this.numRilevazioni = mission.numRilevazioni;
            this.rilevazioni = mission.rilevazioni ? mission.rilevazioni.map(item => new Detection(item)) : [];
            this.stato = this.getStatus();
        }
    }

    private getStatus(): MissionStatus {
        const now = Moment();
        const missionDate = Moment(new Date(this.dataMissione));
        if (this.idMissione === 0) {
            return MissionStatus.TO_SYNCHRONIZE;
        }
        else {
            if (this.stato != MissionStatus.TO_SYNCHRONIZE) {
                if (missionDate.isBefore(now, 'day')) {
                    return MissionStatus.SYNCHRONIZED;
                }
                else {
                    if (this.rilevazioni && this.rilevazioni.length > 0) {
                        return MissionStatus.SYNCHRONIZED;
                    }
                    else {
                        return MissionStatus.DRAFT;
                    }
                }
            }
            return this.stato;

        }
    }

    public getDto(): MissionDto {
        const missionDto: MissionDto = {
            idMissione: this.idMissione,
            numeroTrasferta: this.numeroTrasferta,
            dataMissione: Moment(new Date(this.dataMissione)).format('DD-MM-YYYY'),
            oraInizio: this.oraInizio,
            oraFine: this.oraFine,
            idIspettoreAssegnato: this.idIspettoreAssegnato,
            cfIspettore: this.cfIspettore,
            nomeIspettore: this.nomeIspettore,
            cognomeIspettore: this.cognomeIspettore,
            ispettoriAggiunti: this.ispettoriAggiunti,
            numRilevazioni: this.numRilevazioni,
            rilevazioni: []
        };

        return missionDto;
    }

    public clone(): Mission {
        const clonedMission = new Mission();
        clonedMission.idMissione = this.idMissione;
        clonedMission.numeroTrasferta = this.numeroTrasferta;
        clonedMission.dataMissione = this.dataMissione;
        clonedMission.oraInizio = this.oraInizio;
        clonedMission.oraFine = this.oraFine;
        clonedMission.idIspettoreAssegnato = this.idIspettoreAssegnato;
        clonedMission.cfIspettore = this.cfIspettore;
        clonedMission.nomeIspettore = this.nomeIspettore;
        clonedMission.cognomeIspettore = this.cognomeIspettore;
        clonedMission.ispettoriAggiunti = this.ispettoriAggiunti;
        clonedMission.numRilevazioni = this.numRilevazioni;
        clonedMission.rilevazioni = this.rilevazioni;
        clonedMission.stato = this.getStatus();
        return clonedMission;
    }

    public isOutdated(): boolean {
        const now = Moment();
        const missionDate = Moment(new Date(this.dataMissione));
        return missionDate.isBefore(now, 'day');
    }
}
