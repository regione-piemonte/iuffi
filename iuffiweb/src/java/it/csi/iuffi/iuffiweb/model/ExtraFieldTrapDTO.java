package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ExtraFieldTrapDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -199944819321943227L;
  
  private Integer idExtraFieldTrap;
  private String label;
  private String value;
  
  public ExtraFieldTrapDTO()
  {
    super();
  }

  
  public Integer getIdExtraFieldTrap()
  {
    return idExtraFieldTrap;
  }
  
  public void setIdExtraFieldTrap(Integer idExtraFieldTrap)
  {
    this.idExtraFieldTrap = idExtraFieldTrap;
  }
  
  public String getLabel()
  {
    return label;
  }
  
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  public String getValue()
  {
    return value;
  }
  
  public void setValue(String value)
  {
    this.value = value;
  }

}
