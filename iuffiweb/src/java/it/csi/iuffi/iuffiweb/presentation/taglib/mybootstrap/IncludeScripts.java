package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class IncludeScripts extends SimpleTagSupport
{

  @SuppressWarnings("unchecked")
  @Override
  public void doTag() throws JspException, IOException
  {
    List<String> list = (List<String>) this.getJspContext()
        .getAttribute("__scriptList");
    if (list != null)
    {
      StringBuilder sb = new StringBuilder();
      for (String jsFile : list)
      {
        sb.append("<script type=\"text/javascript\" src=\"").append(jsFile)
            .append("\"></script>");
      }
      this.getJspContext().getOut().write(sb.toString());
    }
  }
}
