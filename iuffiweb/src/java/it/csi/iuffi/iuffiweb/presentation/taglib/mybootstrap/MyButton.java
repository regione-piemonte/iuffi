package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class MyButton extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = -6246716032995448646L;
  private String            btnType          = null;
  private String            value            = null;
  private String            type             = null;
  private Boolean           display          = null;

  @Override
  public void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    sb.append("<button");
    if (type == null)
    {
      TAG_UTIL.addAttribute(sb, "type", "submit");
    }
    else
    {
      TAG_UTIL.addAttribute(sb, "type", type);
    }
    if (value != null)
    {
      TAG_UTIL.addAttribute(sb, "value", value);
    }
    addBaseAttributes(sb);
    sb.append(">");
    if (this.bodyContent != null)
    {
      sb.append(this.bodyContent.getString());
    }
    sb.append("</button>");
  }

  @Override
  protected void addCssClassAttribute(StringBuilder sb, boolean error)
  {
    String btnStyle = btnType;
    if (btnStyle == null)
    {
      btnStyle = "default";
    }
    TAG_UTIL.addAttribute(sb, "class",
        IuffiUtils.STRING.concat(" ", cssClass, "btn", "btn-" + btnStyle));
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public Boolean getDisplay()
  {
    return display;
  }

  public void setDisplay(Boolean display)
  {
    this.display = display;
  }

  @Override
  protected void addStyleAttribute(StringBuilder sb, boolean error)
  {
    String tmpStyle = style;
    if (display != null && !display.booleanValue())
    {
      tmpStyle = IuffiUtils.STRING.concat(" ", "display:none;", tmpStyle);
    }
    TAG_UTIL.addAttributeIfNotNull(sb, "style", tmpStyle);
  }

  public String getBtnType()
  {
    return btnType;
  }

  public void setBtnType(String btnType)
  {
    this.btnType = btnType;
  }
}