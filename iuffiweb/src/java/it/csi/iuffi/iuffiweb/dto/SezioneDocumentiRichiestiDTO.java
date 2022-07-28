package it.csi.iuffi.iuffiweb.dto;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SezioneDocumentiRichiestiDTO implements ILoggable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 38750556022661111L;
	private String idSezione;
	private String descrizione;
	private List<DocumentiRichiestiDaVisualizzareDTO> list;
	private int contatoreDoc;
	
	public int getContatoreDoc() {
		return contatoreDoc;
	}

	public void setContatoreDoc(int contatoreDoc) {
		this.contatoreDoc = contatoreDoc;
	}

	public SezioneDocumentiRichiestiDTO() {
		super();
	}

	public String getIdSezione() {
		return idSezione;
	}

	public void setIdSezione(String idSezione) {
		this.idSezione = idSezione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<DocumentiRichiestiDaVisualizzareDTO> getList() {
		return list;
	}

	public void setList(List<DocumentiRichiestiDaVisualizzareDTO> list) {
		this.list = list;
	}
	
	
	
}
