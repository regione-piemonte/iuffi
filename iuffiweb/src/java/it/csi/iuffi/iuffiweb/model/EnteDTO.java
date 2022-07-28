package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class EnteDTO extends TabelleStoricizzateDTO implements ILoggable
{

  private static final long serialVersionUID = 2030054132006915082L;
  
  private Integer idEnte;
  private String denominazione;
  private String codiceEst;

  
  public EnteDTO()
  {
    super();
  }


  public Integer getIdEnte()
  {
    return idEnte;
  }


  public void setIdEnte(Integer idEnte)
  {
    this.idEnte = idEnte;
  }


  public String getDenominazione()
  {
    return denominazione;
  }


  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }


  public String getCodiceEst()
  {
    return codiceEst;
  }


  public void setCodiceEst(String codiceEst)
  {
    this.codiceEst = codiceEst;
  }
  
}
