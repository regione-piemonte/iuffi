package it.csi.iuffi.iuffiweb.model;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AnfiDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1475593026374293938L;
  
  private Integer idAnfi;
  private String tipologiaTestDiLaboratorio;
  private BigDecimal costo;
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

  public Integer getIdAnfi()
  {
    return idAnfi;
  }
  public void setIdAnfi(Integer idAnfi)
  {
    this.idAnfi = idAnfi;
  }
  public String getTipologiaTestDiLaboratorio()
  {
    return tipologiaTestDiLaboratorio;
  }
  public void setTipologiaTestDiLaboratorio(String tipologiaTestDiLaboratorio)
  {
    this.tipologiaTestDiLaboratorio = tipologiaTestDiLaboratorio;
  }
  public BigDecimal getCosto()
  {
    return costo;
  }
  public void setCosto(BigDecimal costo)
  {
    this.costo = costo;
  }

}
