package it.csi.iuffi.iuffiweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TipoTrappolaDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -7821223841459850112L;
  
  private Integer idTipoTrappola;
  private String tipologiaTrappola;
  private String sfrCode;
  //private BigDecimal costo;
  //aggiunti
  private String specie;
  @JsonIgnore
  private String flagArchiviato; 

  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }

  public Integer getIdTipoTrappola()
  {
    return idTipoTrappola;
  }
  public void setIdTipoTrappola(Integer idTipoTrappola)
  {
    this.idTipoTrappola = idTipoTrappola;
  }
  public String getTipologiaTrappola()
  {
    return tipologiaTrappola;
  }
  public void setTipologiaTrappola(String tipologiaTrappola)
  {
    this.tipologiaTrappola = tipologiaTrappola;
  }
  public String getSfrCode()
  {
    return sfrCode;
  }
  public void setSfrCode(String sfrCode)
  {
    this.sfrCode = sfrCode;
  }
//  public BigDecimal getCosto()
//  {
//    return costo;
//  }
//  public void setCosto(BigDecimal costo)
//  {
//    this.costo = costo;
//  }
  public String getSpecie()
  {
    return specie;
  }
  public void setSpecie(String specie)
  {
    this.specie = specie;
  }

}
