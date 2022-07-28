package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class MyIcon extends SimpleTagSupport
{
  protected String id;
  protected String icon;
  protected int    size = 24;
  protected String onclick;
  protected String href;

  public void doTag() throws JspException, IOException
  {
    super.doTag();
    try
    {
      String html = getIconHtml();
      if (!GenericValidator.isBlankOrNull(html))
      {
        this.getJspContext().getOut().write(html);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
      throw new JspException(e);
    }
  }

  private String getIconHtml()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<a");
    if (id != null)
    {
      IuffiUtils.TAG.addAttribute(sb, "id", id);
    }
    if (href != null)
    {
      IuffiUtils.TAG.addAttribute(sb, "href", href);
    }
    if (onclick != null)
    {
      IuffiUtils.TAG.addAttribute(sb, "onclick", onclick);
    }
    sb.append(" class = \"ico").append(size).append(" ico_").append(icon)
        .append("\"></a>");
    return sb.toString();
  }

  public String getIcon()
  {
    return icon;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  public int getSize()
  {
    return size;
  }

  public void setSize(int size)
  {
    this.size = size;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getOnclick()
  {
    return onclick;
  }

  public void setOnclick(String onclick)
  {
    this.onclick = onclick;
  }

  public String getHref()
  {
    return href;
  }

  public void setHref(String href)
  {
    this.href = href;
  }

}