package it.csi.iuffi.iuffiweb.model;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class CampioneEnteDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -7821223841459850112L;
  
  private Integer idTipoCampioneEnte;
  private Integer idTipoCampione;
  private Integer idEnte;
  private BigDecimal costo;
  //aggiunti
  private String descrTipoCampione;
  private String descrEnte;
  @JsonIgnore
  private String flagArchiviato; 
  
  
  public CampioneEnteDTO()
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


  public String getDescrEnte()
  {
    return descrEnte;
  }


  public void setDescrEnte(String descrEnte)
  {
    this.descrEnte = descrEnte;
  }

  public Integer getIdTipoCampione()
  {
    return idTipoCampione;
  }

  public void setIdTipoCampione(Integer idTipoCampione)
  {
    this.idTipoCampione = idTipoCampione;
  }

  public Integer getIdTipoCampioneEnte()
  {
    return idTipoCampioneEnte;
  }

  public void setIdTipoCampioneEnte(Integer idTipoCampioneEnte)
  {
    this.idTipoCampioneEnte = idTipoCampioneEnte;
  }

  public String getDescrTipoCampione()
  {
    return descrTipoCampione;
  }

  public void setDescrTipoCampione(String descrTipoCampione)
  {
    this.descrTipoCampione = descrTipoCampione;
  }

  
}
