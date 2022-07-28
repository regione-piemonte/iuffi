package it.csi.iuffi.iuffiweb.dto;

public class DocumentiRichiestiDaVisualizzareDTO {

	private static final long serialVersionUID = 6035364196001956453L;

	  private Long              idDocumentiRichiesti;
	  private long            	idTipoDocRichiesti;
	  private String			descrizione;
	  private Long 				ordine;
	  
	public Long getOrdine() {
		return ordine;
	}

	public void setOrdine(Long ordine) {
		this.ordine = ordine;
	}

	public DocumentiRichiestiDaVisualizzareDTO() {
		super();
	}

	public Long getIdDocumentiRichiesti() {
		return idDocumentiRichiesti;
	}

	public void setIdDocumentiRichiesti(Long idDocumentiRichiesti) {
		this.idDocumentiRichiesti = idDocumentiRichiesti;
	}

	public long getIdTipoDocRichiesti() {
		return idTipoDocRichiesti;
	} 

	public void setIdTipoDocRichiesti(long idTipoDocRichiesti) {
		this.idTipoDocRichiesti = idTipoDocRichiesti;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	  
	  
}
