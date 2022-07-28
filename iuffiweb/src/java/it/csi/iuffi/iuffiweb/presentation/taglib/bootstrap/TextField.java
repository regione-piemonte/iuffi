package it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap;

import java.util.Random;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class TextField extends SimpleBoostrapTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 83669507999616759L;
  protected Integer         maxlength;
  protected TextFieldType   type             = TextFieldType.TEXT;

  @Override
  protected void writeCustomTagElement(StringBuilder sb)
  {
    boolean inputGroup = type == TextFieldType.DATE;
    boolean dateInput = type == TextFieldType.DATE;
    if (inputGroup)
    {
      sb.append("<div class=\"input-group\">");
    }
    sb.append("<input type=\"text\"");
    writeDefaultAttributes(sb);
    String inputId = id;
    if (inputId == null)
    {
      int r = new Random(1000).nextInt();
      inputId = "id_" + System.currentTimeMillis() + "_" + (r < 0 ? -r : r); // E
                                                                             // se
                                                                             // non
                                                                             // è
                                                                             // un
                                                                             // id
                                                                             // univoco
                                                                             // questo
                                                                             // non
                                                                             // so
                                                                             // che
                                                                             // farci!
      writeAttribute(sb, "id", inputId);
    }
    if (dateInput)
    {
      writeAttribute(sb, "data-date-picker", "true");
    }
    if (maxlength != null)
    {
      writeAttribute(sb, "maxlength", maxlength);
    }

    writeAttribute(sb, "value", escapeHtml(IuffiUtils.STRING.nvl(value)));
    sb.append("/>");

    if (!GenericValidator.isBlankOrNull(addonPostLabel))
    {
      sb.append(
          " <div class=\"input-group-addon\">" + addonPostLabel + "</div>");
    }
    if (dateInput)
    {
      sb.append(" <label for=\"").append(inputId).append(
          "\" class=\"input-group-addon btn\"><i class=\"icon-large icon-calendar\"></i></label>");
    }
    if (inputGroup)
    {
      sb.append("</div>");
    }
  }

  public Integer getMaxlength()
  {
    return maxlength;
  }

  public void setMaxlength(Integer maxlength)
  {
    this.maxlength = maxlength;
  }

  public String getType()
  {
    return type == null ? null : type.toString();
  }

  public void setType(String type)
  {
    this.type = TextFieldType.valueOf(type);
  }
}
