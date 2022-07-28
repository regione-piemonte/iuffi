package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DescrizioneComuneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 8443394238176282879L;
  protected String          istatComune;
  protected String          descrizioneComune;
  protected String          descrizioneProvincia;
  protected String          cap;

  public DescrizioneComuneDTO(String istatComune, String descrizioneComune,
      String descrizioneProvincia, String cap)
  {
    this.istatComune = istatComune;
    this.descrizioneComune = descrizioneComune;
    this.descrizioneProvincia = descrizioneProvincia;
    this.cap = cap;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getDescrizioneComune()
  {
    return descrizioneComune;
  }

  public void setDescrizioneComune(String descrizioneComune)
  {
    this.descrizioneComune = descrizioneComune;
  }

  public String getDescrizioneProvincia()
  {
    return descrizioneProvincia;
  }

  public void setDescrizioneProvincia(String descrizioneProvincia)
  {
    this.descrizioneProvincia = descrizioneProvincia;
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
