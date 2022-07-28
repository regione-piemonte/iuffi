package it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap;

import javax.servlet.jsp.JspException;

import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.BaseTag;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public abstract class SimpleBoostrapTag2 extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 7230791303640040002L;
  protected int             labelSize        = 3;
  protected int             controlSize      = 9;
  protected String          name;
  protected String          label;
  protected String          labelStyle;
  protected String          id;
  protected String          value;
  protected String          cssClass;
  protected boolean         formControl      = true;
  protected boolean         feedbackIcon     = true;
  protected String          placeholder;

  @Override
  public int doEndTag() throws JspException
  {
    try
    {
      StringBuilder sb = processTag(getErrors());
      this.pageContext.getOut().write(sb.toString());
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
    return super.doEndTag();
  }

  protected StringBuilder processTag(Errors errors) throws Exception
  {
    String error = getError(errors);
    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"form-group\">");
    if (label != null)
    {
      sb.append("<label style=" + labelStyle + "  class=\"col-sm-")
          .append(labelSize).append("\">").append(escapeHtml(label))
          .append("</label>");
    }
    sb.append("<div class=\"col-sm-").append(controlSize);
    if (error != null)
    {
      sb.append(" has-error red-tooltip");
      if (feedbackIcon)
      {
        sb.append(" has-feedback");
      }
    }
    sb.append("\"");
    if (error != null)
    {
      writeError(error, sb);
    }
    sb.append(">");
    writeCustomTagElement(sb);
    // if (error != null && feedbackIcon)
    // {
    // //sb.append("<span class=\"form-control-feedback\"><img
    // src=\"/img/ico_cancel.gif\" /></span>");
    // String ariaDescribedBy = name;
    // if (id != null)
    // {
    // ariaDescribedBy = id;
    // }
    // ariaDescribedBy += "-status";
    //
    // sb.append("<span class=\"glyphicon glyphicon-remove
    // form-control-feedback\" aria-hidden=\"true\"></span>"
    // + " <span id=\"").append(ariaDescribedBy).append("\"
    // class=\"sr-only\">(error)</span>");
    // }
    sb.append("</div></div>");
    return sb;
  }

  public static final StringBuilder getHtmlStaticText(SimpleBoostrapTag2 tag,
      Errors errors) throws Exception
  {
    return tag.processTag(errors);
  }

  protected String getError(Errors errors)
  {
    if (errors != null)
    {
      return errors.get(name);
    }
    return null;
  }

  protected Errors getErrors()
  {
    return (Errors) getELVariableValue("errors");
  }

  protected void writeAttribute(StringBuilder sb, String attrName,
      Object attrValue)
  {
    sb.append(" ").append(attrName).append(" = \"")
        .append(escapeHtml(IuffiUtils.STRING.nvl(attrValue))).append("\"");
  }

  protected void writeNameAttribute(StringBuilder sb)
  {
    writeAttribute(sb, "name", name);
  }

  protected void writeDefaultAttributes(StringBuilder sb)
  {
    writeNameAttribute(sb);
    if (id != null)
    {
      writeAttribute(sb, "aria-describedby", id);
      writeAttribute(sb, "id", id + "-status");
    }
    else
    {
      writeAttribute(sb, "aria-describedby", name + "-status");
    }
    String completeCssClass = getCssClass(); // Potrebbe essere ridefinito nelle
                                             // classi figlie per cambiare al
                                             // volo il cssClass, quindi non uso
                                             // la proprietà ma
                                             // il
    // corrispondente metodo get
    if (completeCssClass != null)
    {
      if (formControl)
      {
        writeAttribute(sb, "class", completeCssClass + " form-control");
      }
      else
      {
        writeAttribute(sb, "class", completeCssClass);
      }
    }
    else
    {
      if (formControl)
      {
        writeAttribute(sb, "class", "form-control");
      }
    }
    if (placeholder != null)
    {
      writeAttribute(sb, "placeholder", placeholder);
    }

  }

  protected void writeError(String error, StringBuilder sb)
  {
    if (error != null)
    {
      sb.append(
          " data-toggle=\"error-tooltip\" title=\"" + escapeHtml(error) + "\"");
    }
  }

  protected abstract void writeCustomTagElement(StringBuilder sb)
      throws Exception;

  public int getLabelSize()
  {
    return labelSize;
  }

  public void setLabelSize(int labelSize)
  {
    this.labelSize = labelSize;
  }

  public int getControlSize()
  {
    return controlSize;
  }

  public void setControlSize(int controlSize)
  {
    this.controlSize = controlSize;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public boolean isFormControl()
  {
    return formControl;
  }

  public void setFormControl(boolean formControl)
  {
    this.formControl = formControl;
  }

  public boolean isFeedbackIcon()
  {
    return feedbackIcon;
  }

  public void setFeedbackIcon(boolean feedbackIcon)
  {
    this.feedbackIcon = feedbackIcon;
  }

  public String getCssClass()
  {
    return cssClass;
  }

  public void setCssClass(String cssClass)
  {
    this.cssClass = cssClass;
  }

  public String getPlaceholder()
  {
    return placeholder;
  }

  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }

  public String getLabelStyle()
  {
    return labelStyle;
  }

  public void setLabelStyle(String labelStyle)
  {
    this.labelStyle = labelStyle;
  }

}
