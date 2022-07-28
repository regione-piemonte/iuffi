package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

public class MyStaticText extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 92816793350454424L;

  @Override
  public void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    sb.append("<span ");
    addAttribute(sb, "class", "form-control-static");
    addBaseAttributes(sb);
    sb.append(">");
    if (bodyContent != null)
    {
      sb.append(bodyContent.getString());
    }
    sb.append("</span>");
  }
}