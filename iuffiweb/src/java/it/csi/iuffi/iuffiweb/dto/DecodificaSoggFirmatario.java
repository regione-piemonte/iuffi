package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DecodificaSoggFirmatario implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;
  private int               idContitolare;
  private String            descrizione;
  private String            codice;

  public DecodificaSoggFirmatario()
  {
  }

  public DecodificaSoggFirmatario(int id, String descrizione)
  {
    this.idContitolare = id;
    this.descrizione = descrizione;
  }

  public DecodificaSoggFirmatario(int id, String codice, String descrizione)
  {
    this.idContitolare = id;
    this.descrizione = descrizione;
    this.codice = codice;
  }

  public int getIdContitolare()
  {
    return idContitolare;
  }

  public void setIdContitolare(int idContitolare)
  {
    this.idContitolare = idContitolare;
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

}
