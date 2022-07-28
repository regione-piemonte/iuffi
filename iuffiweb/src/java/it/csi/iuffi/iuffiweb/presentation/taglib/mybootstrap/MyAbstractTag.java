package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import javax.el.ELContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringEscapeUtils;

import it.csi.iuffi.iuffiweb.util.CustomTagUtils;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public abstract class MyAbstractTag extends BodyTagSupport
{
  /** serialVersionUID */
  private static final long      serialVersionUID  = 464083766951647178L;
  public static final String     CSS_ERROR_CLASSES = "has-error red-tooltip";
  protected final CustomTagUtils TAG_UTIL          = IuffiUtils.TAG;

  public MyAbstractTag()
  {

  }

  protected Errors getErrors()
  {
    return (Errors) getELVariableValue("errors");
  }

  protected String getError(Errors errors, String errorName)
  {
    if (errors != null)
    {
      return errors.get(errorName);
    }
    return null;
  }

  protected String getError(String errorName)
  {
    return getError(getErrors(), errorName);
  }

  protected Object getELVariableValue(String base, String name)
  {
    ELContext elContext = this.pageContext.getELContext();
    return elContext.getELResolver().getValue(elContext, base, name);
  }

  protected Object getELVariableValue(String name)
  {
    return getELVariableValue(null, name);
  }

  protected String escapeHtml(String value)
  {
    return StringEscapeUtils.escapeHtml4(value);
  }

  protected void addAttribute(StringBuilder sb, String attrName,
      String attrValue)
  {
    sb.append(" ").append(attrName).append(" = \"").append(attrValue)
        .append("\"");
  }

  public StringBuilder addAttribute(StringBuilder sb, String name, Object value)
  {
    return TAG_UTIL.addAttribute(sb, name, value);
  }

  public StringBuilder addAttributeIfNotNull(StringBuilder sb, String name,
      Object value)
  {
    return TAG_UTIL.addAttributeIfNotNull(sb, name, value);
  }
}
