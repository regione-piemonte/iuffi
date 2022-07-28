package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import javax.el.ELContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public abstract class BaseTag extends BodyTagSupport
{
  /** serialVersionUID */
  private static final long  serialVersionUID = 7631646001329816975L;
  public static final Logger logger           = Logger
      .getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".presentation");
  public static final String HELP_CONTAINER = "<div id=\"help_container\" class=\"container-fluid help_container_name\" style=\"display:none;margin-top:1em\"><blockquote id=\"help_text\"><span></span></blockquote></div>";

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

  protected String escapeJavascript(String value)
  {
    return StringEscapeUtils.escapeEcmaScript(value);
  }

  protected void addAttribute(StringBuilder sb, String attrName,
      String attrValue)
  {
    sb.append(" ").append(attrName).append(" = \"").append(attrValue)
        .append("\"");
  }
}
