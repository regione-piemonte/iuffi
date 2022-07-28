package it.csi.iuffi.iuffiweb.dto;

import java.io.Serializable;

public class Radio implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1177659585397223005L;
  private String            value;
  private String            label;

  public Radio(String value, String label)
  {
    this.value = value;
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

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }
}
