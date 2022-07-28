package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ComuneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idRegione;
  private String            istatProvincia;
  private String            istatComune;
  private String            descrizioneRegione;
  private String            descrizioneProvincia;
  private String            descrizioneComune;
  private String            siglaProvincia;
  private String            flagEstero;
  private String            cap;

  public long getIdRegione()
  {
    return idRegione;
  }

  public void setIdRegione(long idRegione)
  {
    this.idRegione = idRegione;
  }

  public String getIstatProvincia()
  {
    return istatProvincia;
  }

  public void setIstatProvincia(String istatProvincia)
  {
    this.istatProvincia = istatProvincia;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getDescrizioneRegione()
  {
    return descrizioneRegione;
  }

  public void setDescrizioneRegione(String descrizioneRegione)
  {
    this.descrizioneRegione = descrizioneRegione;
  }

  public String getDescrizioneProvincia()
  {
    return descrizioneProvincia;
  }

  public void setDescrizioneProvincia(String descrizioneProvincia)
  {
    this.descrizioneProvincia = descrizioneProvincia;
  }

  public String getDescrizioneComune()
  {
    return descrizioneComune;
  }

  public void setDescrizioneComune(String descrizioneComune)
  {
    this.descrizioneComune = descrizioneComune;
  }

  public String getSiglaProvincia()
  {
    return siglaProvincia;
  }

  public void setSiglaProvincia(String siglaProvincia)
  {
    this.siglaProvincia = siglaProvincia;
  }

  public String getFlagEstero()
  {
    return flagEstero;
  }

  public void setFlagEstero(String flagEstero)
  {
    this.flagEstero = flagEstero;
  }

  public String getCap()
  {
    return cap;
  }

  public void setCap(String cap)
  {
    this.cap = cap;
  }

}
