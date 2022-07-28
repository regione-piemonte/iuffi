package it.csi.iuffi.iuffiweb.dto;

public class RTipoDocumentiRichiestiDTO {

	long idDocumentiRichiesti;
	long iIdTipoDocumentiRichiesti;
	
	public RTipoDocumentiRichiestiDTO() {
		super();
	}

	public RTipoDocumentiRichiestiDTO(long idDocumentiRichiesti, long iIdTipoDocumentiRichiesti) {
		super();
		this.idDocumentiRichiesti = idDocumentiRichiesti;
		this.iIdTipoDocumentiRichiesti = iIdTipoDocumentiRichiesti;
	}

	public long getIdDocumentiRichiesti() {
		return idDocumentiRichiesti;
	}

	public void setIdDocumentiRichiesti(long idDocumentiRichiesti) {
		this.idDocumentiRichiesti = idDocumentiRichiesti;
	}

	public long getiIdTipoDocumentiRichiesti() {
		return iIdTipoDocumentiRichiesti;
	}

	public void setiIdTipoDocumentiRichiesti(long iIdTipoDocumentiRichiesti) {
		this.iIdTipoDocumentiRichiesti = iIdTipoDocumentiRichiesti;
	}
	
	@Override
	public boolean equals(Object object)
	{
	    boolean isEqual= false;

	    if (object != null && object instanceof RTipoDocumentiRichiestiDTO)
	    {
	        isEqual = (this.idDocumentiRichiesti == ((RTipoDocumentiRichiestiDTO) object).idDocumentiRichiesti
	        		&& this.iIdTipoDocumentiRichiesti == ((RTipoDocumentiRichiestiDTO) object).iIdTipoDocumentiRichiesti);
	    }

	    return isEqual;
	}

	
}
