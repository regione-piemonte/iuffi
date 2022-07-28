package it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.validator.Errors;

public class File extends SimpleBoostrapTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 83669507999616759L;

  @Override
  protected void writeCustomTagElement(StringBuilder sb)
  {
  }

  @Override
  public String getCssClass()
  {
    if (GenericValidator.isBlankOrNull(cssClass))
    {
      return "input-group";
    }
    else
    {
      if (!cssClass.contains("input-group"))
      {
        return cssClass + " input-group";
      }
    }
    return cssClass;
  }

  @Override
  protected StringBuilder processTag(Errors errors) throws Exception
  {
    String error = getError(errors);

    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"form-group");
    /*
     * if (error != null) { sb.append(" has-error  red-tooltip "); }
     */
    sb.append("\">");
    if (label != null)
    {
      sb.append("<label class=\"col-sm-").append(labelSize);
      if (error != null)
      {
        sb.append(" alert alert-danger error-label");
        sb.append(" \">").append(escapeHtml(label)).append(
            "<span class=\"icon icon-exclamation-sign\" aria-hidden=\"true\"></span></label>");
      }
      else
      {
        sb.append(" control-label");
        sb.append(" \">").append(escapeHtml(label)).append("</label>");

      }

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

    sb.append("<div class=\"input-group ").append("\">");

    String thisId = id != null ? name : id;
    sb.append(
        "<input type=\"text\" class=\"form-control\" readonly=\"readonly\" id=\"fileName_")
        .append(thisId)
        .append(
            "\" /> <span class=\"group-span-filestyle input-group-btn\" tabindex=\"0\">");
    sb.append("<label class=\"btn btn-primary \" onclick=\"$('#")
        .append(thisId);
    sb.append(
        "').click()\"> <span class=\"glyphicon glyphicon-folder-open\"></span> Cerca File</label></span>");
    sb.append("</div>");
    sb.append("</div>");
    sb.append("<input type=\"file\" id=\"").append(thisId).append("\" name=\"")
        .append(name)
        .append("\" onchange=\"fileUploadChangeName(this,$('#fileName_");
    sb.append(thisId).append(
        "'))\" style=\"position: absolute; left: -1000px; clip: rect(0px, 0px, 0px, 0px)\" /></div>");
    sb.append("</div>");
    sb.append("</div>");

    return sb;
  }
}
