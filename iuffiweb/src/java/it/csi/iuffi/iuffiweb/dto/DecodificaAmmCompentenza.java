package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DecodificaAmmCompentenza implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;
  private int               idAmmCompetenza;
  private String            descrizione;
  private String            codice;

  public DecodificaAmmCompentenza()
  {
  }

  public DecodificaAmmCompentenza(int id, String descrizione)
  {
    this.idAmmCompetenza = id;
    this.descrizione = descrizione;
  }

  public DecodificaAmmCompentenza(int id, String codice, String descrizione)
  {
    this.idAmmCompetenza = id;
    this.descrizione = descrizione;
    this.codice = codice;
  }

  public int getIdAmmCompetenza()
  {
    return idAmmCompetenza;
  }

  public void setIdAmmCompetenza(int idAmmCompetenza)
  {
    this.idAmmCompetenza = idAmmCompetenza;
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
