package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SospensioneAnticipoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1355994747893174486L;

  protected long            idSospensioneAnticipo;
  protected long            idLivello;
  protected String          codiceLivello;
  protected String          descrizioneLivello;
  protected String          flagSospensione;
  protected String          motivazione;

  public long getIdSospensioneAnticipo()
  {
    return idSospensioneAnticipo;
  }

  public void setIdSospensioneAnticipo(long idSospensioneAnticipo)
  {
    this.idSospensioneAnticipo = idSospensioneAnticipo;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public String getDescrizioneLivello()
  {
    return descrizioneLivello;
  }

  public void setDescrizioneLivello(String descrizioneLivello)
  {
    this.descrizioneLivello = descrizioneLivello;
  }

  public String getFlagSospensioneStr()
  {
    return ("S".equals(flagSospensione)) ? "Si" : "No";
  }

  public String getFlagSospensione()
  {
    return flagSospensione;
  }

  public void setFlagSospensione(String flagSospensione)
  {
    this.flagSospensione = flagSospensione;
  }

  public String getMotivazione()
  {
    return motivazione;
  }

  public void setMotivazione(String motivazione)
  {
    this.motivazione = motivazione;
  }

}