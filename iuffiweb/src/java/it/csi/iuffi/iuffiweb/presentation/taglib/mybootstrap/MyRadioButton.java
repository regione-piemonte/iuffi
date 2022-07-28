package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

public class MyRadioButton extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1080560085783747016L;
  protected String          value;
  private boolean           checked          = false;
  private boolean           readonly         = false;
  private String            chkLabel;
  protected Boolean         preferRequestValues;

  @Override
  protected void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    boolean customErrorLabel = false;
    String _chkLabel = chkLabel;
    boolean _checked = checked;
    if (errorMessage != null)
    {
      _chkLabel = " <span style=\"float:right\" class=\"icon icon-exclamation-sign\" aria-hidden=\"true\"></span>";
      errorMessage = null;
      customErrorLabel = true;
    }

    sb.append(" <div class=\"radio\"> ");
    sb.append(
        "<label style=\"margin-right:1em\" ><input type=\"radio\" value=\"")
        .append(StringEscapeUtils.escapeHtml4(value)).append("\"");

    addBaseAttributes(sb, errorMessage != null);

    if (preferRequestValues != null && preferRequestValues.booleanValue())
    {
      String val = this.pageContext.getRequest().getParameter(name);
      if (val != null)
      {
        if (val.equals(value))
          _checked = true;
      }
    }

    if (_checked)
      sb.append(" checked=\"checked\" ");

    if (readonly)
      sb.append(" readonly=\"readonly\" ");

    sb.append(" />");

    if (_chkLabel != null)
    {
      if (!customErrorLabel)
      {
        _chkLabel = StringEscapeUtils.escapeHtml4(_chkLabel);
      }

      sb.append(_chkLabel);
    }

    sb.append("</label>");
    sb.append(" </div> ");
  }

  @Override
  protected boolean isCleanOnError()
  {
    return !(GenericValidator.isBlankOrNull(chkLabel));
  }

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }

  public String getChkLabel()
  {
    return chkLabel;
  }

  public void setChkLabel(String chkLabel)
  {
    this.chkLabel = chkLabel;
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public void setReadonly(boolean readonly)
  {
    this.readonly = readonly;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public Boolean getPreferRequestValues()
  {
    return preferRequestValues;
  }

  public void setPreferRequestValues(Boolean preferRequestValues)
  {
    this.preferRequestValues = preferRequestValues;
  }

}
