package it.csi.iuffi.iuffiweb.dto;

public class SezioneOrdineDTO {
	
	private Long              ordine;
	private String            codice;
	private Long			  idTipo;

	public SezioneOrdineDTO() {
		super();
	}

	public Long getOrdine() {
		return ordine;
	}

	public void setOrdine(Long ordine) {
		this.ordine = ordine;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	
	public Long getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}
		  
	
	  
	  
	  
}
