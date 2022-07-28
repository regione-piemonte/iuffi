import { InspectorDto } from '../dto/inspector.dto';

export class InspectorOptions {
    public idAnagrafica = 0;
    public nome?= '';
    public cognome?= '';
    public cfAnagraficaEst?= '';
    public selected = false;

    constructor(inspectorDto?: InspectorDto) {
        if (inspectorDto) {
            this.idAnagrafica = inspectorDto.idAnagrafica;
            this.nome = inspectorDto.nome ? inspectorDto.nome.toLowerCase() : '';
            this.cognome = inspectorDto.cognome ? inspectorDto.cognome.toLowerCase() : '';
            this.cfAnagraficaEst = inspectorDto.cfAnagraficaEst;
        }
    }
}
export class Inspector {
    public idAnagrafica = 0;
    public nome? = '';
    public cognome? = '';
    public cfAnagraficaEst?= '';

    constructor(inspectorDto?: InspectorDto) {
        if (inspectorDto) {
            this.idAnagrafica = inspectorDto.idAnagrafica;
            this.nome = inspectorDto.nome ? inspectorDto.nome.toLowerCase() : '';
            this.cognome = inspectorDto.cognome ? inspectorDto.cognome.toLowerCase() : '';
            this.cfAnagraficaEst = inspectorDto.cfAnagraficaEst;
        }
    }
}
