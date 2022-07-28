package it.csi.iuffi.iuffiweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TrappolaOnDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -7821223841459850112L;
  
  @JsonIgnore
  private Integer idTrappolaOn;
  private Integer idTrappola;
  private Integer idOn;
  //aggiunti
  private String descrTipoTrappola;
  private String descrOn;
  @JsonIgnore
  private String flagArchiviato; 
 
  
  public TrappolaOnDTO()
  {
    super();
  }


  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }


  public Integer getIdTrappolaOn()
  {
    return idTrappolaOn;
  }


  public void setIdTrappolaOn(Integer idTrappolaOn)
  {
    this.idTrappolaOn = idTrappolaOn;
  }


  public Integer getIdTrappola()
  {
    return idTrappola;
  }


  public void setIdTrappola(Integer idTrappola)
  {
    this.idTrappola = idTrappola;
  }


  public Integer getIdOn()
  {
    return idOn;
  }


  public void setIdOn(Integer idOn)
  {
    this.idOn = idOn;
  }


  public String getDescrTipoTrappola()
  {
    return descrTipoTrappola;
  }


  public void setDescrTipoTrappola(String descrTipoTrappola)
  {
    this.descrTipoTrappola = descrTipoTrappola;
  }


  public String getDescrOn()
  {
    return descrOn;
  }


  public void setDescrOn(String descrOn)
  {
    this.descrOn = descrOn;
  }
  

  
}
