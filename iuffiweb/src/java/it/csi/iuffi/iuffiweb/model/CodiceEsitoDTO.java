package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class CodiceEsitoDTO extends TabelleStoricizzateDTO implements ILoggable
{

  private static final long serialVersionUID = 2030054132006915082L;
  
  private Integer idCodiceEsito;
  private String descrizione;
  
  public CodiceEsitoDTO()
  {
    super();
  }
  

  public Integer getIdCodiceEsito()
  {
    return idCodiceEsito;
  }

  public void setIdCodiceEsito(Integer idCodiceEsito)
  {
    this.idCodiceEsito = idCodiceEsito;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
}
