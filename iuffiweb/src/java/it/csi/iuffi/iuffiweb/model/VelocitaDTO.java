package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class VelocitaDTO implements ILoggable
{

  private static final long serialVersionUID = 3672687362216246324L;
  
  private Integer id;
  private String label; 
  
  public VelocitaDTO()
  {
    super();
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

}
