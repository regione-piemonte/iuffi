package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class VolturaDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -2786693453918753891L;

  private Long              idVoltura;
  private Long              idAzienda;
  private String            cuaa;
  private String            denominazioneAzienda;
  private String            sedeLegale;
  private String            rappresentanteLegale;
  private String            note;

  public Long getIdVoltura()
  {
    return idVoltura;
  }

  public void setIdVoltura(Long idVoltura)
  {
    this.idVoltura = idVoltura;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazioneAzienda()
  {
    return denominazioneAzienda;
  }

  public void setDenominazioneAzienda(String denominazioneAzienda)
  {
    this.denominazioneAzienda = denominazioneAzienda;
  }

  public String getSedeLegale()
  {
    return sedeLegale;
  }

  public void setSedeLegale(String sedeLegale)
  {
    this.sedeLegale = sedeLegale;
  }

  public String getRappresentanteLegale()
  {
    return rappresentanteLegale;
  }

  public void setRappresentanteLegale(String rappresentanteLegale)
  {
    this.rappresentanteLegale = rappresentanteLegale;
  }

  public Long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

}
