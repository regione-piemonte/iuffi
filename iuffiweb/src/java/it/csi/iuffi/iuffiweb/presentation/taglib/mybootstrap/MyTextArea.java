package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

public class MyTextArea extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 92816793350454424L;
  protected Integer         cols;
  protected Integer         rows;
  protected String          placeholder;
  private Boolean           preferRequestValues;
  private Boolean           escapeHtml;

  public MyTextArea()
  {
    super("form-control");
  }

  @Override
  public void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    sb.append("<textarea");
    addAttribute(sb, "type", "text");
    addAttributeIfNotNull(sb, "placeholder", escapeHtml(placeholder));
    addBaseAttributes(sb);
    TAG_UTIL.addAttributeIfNotNull(sb, "cols", cols);
    TAG_UTIL.addAttributeIfNotNull(sb, "rows", rows);
    sb.append(">");
    String textValue = null;
    if (preferRequestValues != null && preferRequestValues.booleanValue())
    {
      textValue = this.pageContext.getRequest().getParameter(name);
    }
    else
    {
      if (bodyContent != null)
      {
        textValue = bodyContent.getString();
      }
    }
    if (textValue != null)
    {
      if (escapeHtml == null || escapeHtml.booleanValue())
      {
        textValue = escapeHtml(textValue);
      }
      sb.append(textValue);
    }
    sb.append("</textarea>");
  }

  public Integer getCols()
  {
    return cols;
  }

  public void setCols(Integer cols)
  {
    this.cols = cols;
  }

  public Integer getRows()
  {
    return rows;
  }

  public void setRows(Integer rows)
  {
    this.rows = rows;
  }

  public String getPlaceholder()
  {
    return placeholder;
  }

  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }

  public Boolean getPreferRequestValues()
  {
    return preferRequestValues;
  }

  public void setPreferRequestValues(Boolean preferRequestValues)
  {
    this.preferRequestValues = preferRequestValues;
  }

  public Boolean getEscapeHtml()
  {
    return escapeHtml;
  }

  public void setEscapeHtml(Boolean escapeHtml)
  {
    this.escapeHtml = escapeHtml;
  }
}