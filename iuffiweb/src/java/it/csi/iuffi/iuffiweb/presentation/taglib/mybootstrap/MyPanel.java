package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.util.Random;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.validator.GenericValidator;

public class MyPanel extends BodyTagSupport
{
  private String            id;
  private String            cssClass;
  private boolean           collapsible      = true;
  private boolean           startOpened      = true;
  protected String          title;
  protected boolean         visible          = true;
  /** serialVersionUID */
  private static final long serialVersionUID = 83669507999616759L;

  @Override
  public int doEndTag() throws JspException
  {
    try
    {
      if (visible)
      {
        StringBuilder sb = processTag();
        this.pageContext.getOut().write(sb.toString());
      }
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
    return super.doEndTag();
  }

  protected StringBuilder processTag() throws Exception
  {
    StringBuilder sb = new StringBuilder();
    String panelID = id;
    if (id == null)
    {
      int r = new Random(1000).nextInt();
      panelID = "id_" + System.currentTimeMillis() + "_" + (r < 0 ? -r : r); // E
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
    }
    final String contentID = "content_" + panelID;

    sb.append("\n<div class=\"panel panel-primary");
    if (collapsible)
    {
      sb.append(" custom-collapsible-panel");
    }
    if (!GenericValidator.isBlankOrNull(cssClass))
    {
      sb.append(" ").append(cssClass);
    }
    sb.append("\" id=\"").append(panelID).append("\">");
    if (!GenericValidator.isBlankOrNull(title))
    {
      writePanelTitle(sb, panelID, contentID);
    }
    writePanelContent(sb, contentID);
    sb.append("\n</div>");
    return sb;
  }

  protected void writePanelContent(StringBuilder sb, String contentID)
  {
    sb.append("  <div class=\"collapse");
    if (!collapsible || startOpened)
    {
      sb.append(" in");
    }
    sb.append("\" id=\"").append(contentID).append("\">"
        + "\n<div class=\"panel-body\">");
    sb.append(this.getBodyContent().getString());
    sb.append("\n</div>\n</div>");
  }

  protected void writePanelTitle(StringBuilder sb, String panelID,
      String contentID)
  {
    sb.append("\n<div class=\"panel-heading \">");
    sb.append("\n<h3 id=\"panel-title_").append(panelID)
        .append("\" class=\"panel-title\">");
    if (collapsible)
    {
      sb.append("\n<a href=\"#").append(contentID).append(
          "\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#")
          .append(contentID)
          .append("\">");
      if (startOpened)
      {
        sb.append("<i class=\"icon icon-collapse\" ></i>&nbsp;");
      }
      else
      {
        sb.append("<i class=\"icon icon-expand\" ></i>&nbsp;");
      }
    }
    sb.append(title);
    if (collapsible)
    {
      sb.append("</a>");
    }
    sb.append("</h3>\n</div>");
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getCssClass()
  {
    return cssClass;
  }

  public void setCssClass(String cssClass)
  {
    this.cssClass = cssClass;
  }

  public boolean isCollapsible()
  {
    return collapsible;
  }

  public void setCollapsible(boolean collapsible)
  {
    this.collapsible = collapsible;
  }

  public boolean isStartOpened()
  {
    return startOpened;
  }

  public void setStartOpened(boolean startOpened)
  {
    this.startOpened = startOpened;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }
}
