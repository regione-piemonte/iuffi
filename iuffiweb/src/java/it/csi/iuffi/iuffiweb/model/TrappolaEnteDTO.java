package it.csi.iuffi.iuffiweb.model;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TrappolaEnteDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -7821223841459850112L;
  
  private Integer idTrappolaEnte;
  private Integer idTrappola;
  private Integer idEnte;
  private BigDecimal costo;
  //aggiunti
  private String descrTipoTrappola;
  private String descrEnte;
  @JsonIgnore
  private String flagArchiviato; 
  
  
  public TrappolaEnteDTO()
  {

  }

  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }


  public Integer getIdTrappolaEnte()
  {
    return idTrappolaEnte;
  }


  public void setIdTrappolaEnte(Integer idTrappolaEnte)
  {
    this.idTrappolaEnte = idTrappolaEnte;
  }


  public Integer getIdTrappola()
  {
    return idTrappola;
  }


  public void setIdTrappola(Integer idTrappola)
  {
    this.idTrappola = idTrappola;
  }


  public Integer getIdEnte()
  {
    return idEnte;
  }


  public void setIdEnte(Integer idEnte)
  {
    this.idEnte = idEnte;
  }


  public BigDecimal getCosto()
  {
    return costo;
  }


  public void setCosto(BigDecimal costo)
  {
    this.costo = costo;
  }


  public String getDescrTipoTrappola()
  {
    return descrTipoTrappola;
  }


  public void setDescrTipoTrappola(String descrTipoTrappola)
  {
    this.descrTipoTrappola = descrTipoTrappola;
  }


  public String getDescrEnte()
  {
    return descrEnte;
  }


  public void setDescrEnte(String descrEnte)
  {
    this.descrEnte = descrEnte;
  }

  
}
