import { HarmfulOrganismDto } from '../dto/offline.dto';

export class HarmfulOrganism {
    public idOrganismoNocivo = 0;
    public nomeLatino = '';
    public sigla = '';
    public euro = 'N';
    public flagEmergenza = 'N';
    public nomeCompleto = '';
    public dataInizioValiditaF = '';
    public dataFineValiditaF = '';

    constructor(harmfulOrganismDto?: HarmfulOrganismDto | HarmfulOrganism) {
        if (harmfulOrganismDto) {
            this.idOrganismoNocivo = harmfulOrganismDto.idOrganismoNocivo;
            this.nomeLatino = harmfulOrganismDto.nomeLatino ? harmfulOrganismDto.nomeLatino.toLowerCase() : '';
            this.sigla = harmfulOrganismDto.sigla;
            this.euro = harmfulOrganismDto.euro;
            this.flagEmergenza = harmfulOrganismDto.flagEmergenza;
            this.nomeCompleto = harmfulOrganismDto.nomeCompleto ? harmfulOrganismDto.nomeCompleto.toLowerCase() : '';
            this.dataInizioValiditaF = harmfulOrganismDto.dataInizioValiditaF;
            this.dataFineValiditaF = harmfulOrganismDto.dataFineValiditaF;
        }
    }
}

export class HarmfulOrganismOptions {
    public idOrganismoNocivo = 0;
    public nomeLatino = '';
    public sigla = '';
    public euro = 'N';
    public flagEmergenza = 'N';
    public nomeCompleto = '';
    public dataInizioValiditaF = '';
    public dataFineValiditaF = '';
    public selected = false;

    constructor(harmfulOrganism?: HarmfulOrganism) {
        if (harmfulOrganism) {
            this.idOrganismoNocivo = harmfulOrganism.idOrganismoNocivo;
            this.nomeLatino = harmfulOrganism.nomeLatino ? harmfulOrganism.nomeLatino.toLowerCase() : '';
            this.sigla = harmfulOrganism.sigla;
            this.euro = harmfulOrganism.euro;
            this.flagEmergenza = harmfulOrganism.flagEmergenza;
            this.nomeCompleto = harmfulOrganism.nomeCompleto ? harmfulOrganism.nomeCompleto.toLowerCase() : '';
            this.dataInizioValiditaF = harmfulOrganism.dataInizioValiditaF;
            this.dataFineValiditaF = harmfulOrganism.dataFineValiditaF;
        }
    }
}
