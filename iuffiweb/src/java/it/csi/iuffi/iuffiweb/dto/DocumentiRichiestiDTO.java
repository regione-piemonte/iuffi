package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DocumentiRichiestiDTO  implements ILoggable{
	
	private static final long serialVersionUID = 6035364196001956453L;

	  private long              idDocumentiRichiesti;
	  private long            	idProcedimentoOggetto;
	  private String			altroDocRichiesto;
	  
	  
	public DocumentiRichiestiDTO() {
		super();
	}
	
	public long getIdDocumentiRichiesti() {
		return idDocumentiRichiesti;
	}
	public void setIdDocumentiRichiesti(long idDocumentiRichiesti) {
		this.idDocumentiRichiesti = idDocumentiRichiesti;
	}
	public long getIdProcedimentoOggetto() {
		return idProcedimentoOggetto;
	}
	public void setIdProcedimentoOggetto(long idProcedimentoOggetto) {
		this.idProcedimentoOggetto = idProcedimentoOggetto;
	}
	public String getAltroDocRichiesto() {
		return altroDocRichiesto;
	}
	public void setAltroDocRichiesto(String altroDocRichiesto) {
		this.altroDocRichiesto = altroDocRichiesto;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	  
	  


}
