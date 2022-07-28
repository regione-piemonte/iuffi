package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ExtraFieldCampDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -199944819321943227L;
  
  private Integer idExtraFieldCamp;
  private String label;
  private String value;
  
  
  public ExtraFieldCampDTO()
  {

  }

  public Integer getIdExtraFieldCamp()
  {
    return idExtraFieldCamp;
  }
  public void setIdExtraFieldCamp(Integer idExtraFieldCamp)
  {
    this.idExtraFieldCamp = idExtraFieldCamp;
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
