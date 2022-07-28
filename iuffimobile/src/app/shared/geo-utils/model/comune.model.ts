export class Comune {
	public istat: string;
	public nome: string;
	public provinciaIstat: string;
	public provinciaNome: string;

	constructor(istat: string, nome: string, provinciaIstat: string, provinciaNome: string) {
	    this.istat = istat;
	    this.nome = nome;
	    this.provinciaIstat = provinciaIstat;
	    this.provinciaNome = provinciaNome;
	}
}
