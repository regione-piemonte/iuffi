package it.csi.iuffi.iuffiweb.dto;

import java.io.Serializable;

public class CheckBox extends Radio implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1177659585397223005L;
  private boolean           checked;

  public CheckBox(String value, String label)
  {
    this(value, label, false);
  }

  public CheckBox(String value, String label, boolean checked)
  {
    super(value, label);
    this.checked = checked;
  }

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }
}
