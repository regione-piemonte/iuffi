package it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.BaseTag;

public class Error extends BaseTag
{
  /** serialVersionUID */
  protected static final long serialVersionUID = -7620410490326047187L;
  protected boolean           force            = false;

  public int doEndTag() throws JspException
  {
    @SuppressWarnings("unchecked")
    Map<String, String> errors = (Map<String, String>) getELVariableValue(
        "errors");
    StringBuilder sb = new StringBuilder();
    if (errors != null)
    {
      // Sono presenti degli errori ==> visualizzo sicuramente un messaggio ==>
      // devo decidere quale messaggio
      String error = errors.get("error");
      boolean done = false;
      if (error != null)
      {
        writeErrorBox(sb, error);
        done = true;
      }
      String warning = errors.get("warning");
      if (warning != null)
      {
        writeWarningBox(sb, warning);
      }
      if (!done || force)
      {
        String bodyContent = null;
        BodyContent bc = this.getBodyContent();
        if (bc != null)
        {
          bodyContent = bc.getString();
        }
        if (bodyContent != null)
        {
          bodyContent = bodyContent.trim();
        }
        if (GenericValidator.isBlankOrNull(bodyContent))
        {
          bodyContent = "Sono stati rilevati degli errori nei dati inseriti. I campi errati sono stati contrassegnati in rosso, è necessario correggerli per proseguire";
        }
        writeErrorBox(sb, bodyContent);
      }

      try
      {
        String html = sb.toString();
        if (!GenericValidator.isBlankOrNull(html))
        {
          this.pageContext.getOut().write(html);
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
        throw new JspException(e);
      }
    }

    return super.doEndTag();
  }

  protected void writeBox(StringBuilder sb, String boxClass, String message)
  {
    sb.append("<div class=\"alert ").append(escapeHtml(boxClass)).append("\">")
        .append(
            "<a href=\"#\" onclick=\"return false\" class=\"close\" data-dismiss=\"alert\">&times;</a>"
                + "<strong>")
        .append(message).append("</strong></div>").toString();
  }

  protected void writeErrorBox(StringBuilder sb, String message)
  {
    writeBox(sb, "alert-danger alert-error", message);
  }

  protected void writeWarningBox(StringBuilder sb, String message)
  {
    writeBox(sb, "alert-warning", message);

  }

  public boolean isForce()
  {
    return force;
  }

  public void setForce(boolean force)
  {
    this.force = force;
  }

}