package it.csi.iuffi.iuffiweb.integration.ws.regata;

public class CredenzialiDTO {
	protected String codiceFruitore;
	protected Long idUtenteIride;
	protected Long idRichiestaMassiva;
	
	public Long getIdUtenteIride() {
		return idUtenteIride;
	}
	public void setIdUtenteIride(Long idUtenteIride) {
		this.idUtenteIride = idUtenteIride;
	}
	public Long getIdRichiestaMassiva() {
		return idRichiestaMassiva;
	}
	public void setIdRichiestaMassiva(Long idVisuraMassiva) {
		this.idRichiestaMassiva = idVisuraMassiva;
	}
	public String getCodiceFruitore() {
		return codiceFruitore;
	}
	public void setCodiceFruitore(String codiceFruitore) {
		this.codiceFruitore = codiceFruitore;
	}
}
