package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DecodificaDualLIstDTO<T> implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;
  private T                 id;
  private String            descrizione;
  private String            codice;
  private String            gruppo;

  public DecodificaDualLIstDTO()
  {
  }

  public DecodificaDualLIstDTO(T id, String descrizione)
  {
    this.id = id;
    this.descrizione = descrizione;
  }

  public DecodificaDualLIstDTO(T id, String codice, String descrizione,
      String gruppo)
  {
    this.id = id;
    this.descrizione = descrizione;
    this.codice = codice;
    this.gruppo = gruppo;
  }

  public T getId()
  {
    return id;
  }

  public void setId(T id)
  {
    this.id = id;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getCodiceDescrizione()
  {
    return codice + " - " + descrizione;
  }

  public String getGruppo()
  {
    return gruppo;
  }

  public void setGruppo(String gruppo)
  {
    this.gruppo = gruppo;
  }

}
