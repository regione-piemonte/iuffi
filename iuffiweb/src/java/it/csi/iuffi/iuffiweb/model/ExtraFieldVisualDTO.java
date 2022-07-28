package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ExtraFieldVisualDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -199944819321943227L;
  
  private Integer idExtraFieldVisual;
  private String label;
  private String value;
  
  public ExtraFieldVisualDTO()
  {
    super();
  }

  public Integer getIdExtraFieldVisual()
  {
    return idExtraFieldVisual;
  }

  public void setIdExtraFieldVisual(Integer idExtraFieldVisual)
  {
    this.idExtraFieldVisual = idExtraFieldVisual;
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
