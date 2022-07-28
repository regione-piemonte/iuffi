package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PositivitaDTO extends TabelleStoricizzateDTO implements ILoggable
{

  private static final long serialVersionUID = 2030054132006915082L;
  
  private Integer idPositivita;
  private String descPositivita;

  
  public PositivitaDTO()
  {
    super();
  }


  public Integer getIdPositivita()
  {
    return idPositivita;
  }


  public void setIdPositivita(Integer idPositivita)
  {
    this.idPositivita = idPositivita;
  }


  public String getDescPositivita()
  {
    return descPositivita;
  }


  public void setDescPositivita(String descPositivita)
  {
    this.descPositivita = descPositivita;
  }


   
}
