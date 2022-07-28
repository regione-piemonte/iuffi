package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DiametroDTO extends TabelleStoricizzateDTO implements ILoggable
{

  private static final long serialVersionUID = 2030054132006915082L;
  
  private Integer idDiametro;
  private String descDiametro;

  
  public DiametroDTO()
  {
    super();
  }


  public Integer getIdDiametro()
  {
    return idDiametro;
  }


  public void setIdDiametro(Integer idDiametro)
  {
    this.idDiametro = idDiametro;
  }


  public String getDescDiametro()
  {
    return descDiametro;
  }


  public void setDescDiametro(String descDiametro)
  {
    this.descDiametro = descDiametro;
  }



  
}
