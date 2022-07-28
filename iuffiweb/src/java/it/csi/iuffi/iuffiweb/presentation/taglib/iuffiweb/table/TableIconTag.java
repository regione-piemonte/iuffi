package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table;

import javax.servlet.jsp.JspException;

import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.BaseTag;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table.dto.TableIcon;

public class TableIconTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6833165635857338283L;
  private String            cssClass;
  private String            href;
  private boolean           headerIcon       = false;
  private boolean           visible          = true;
  private String            tooltip          = null;
  private String            onclick;

  @Override
  public int doEndTag() throws JspException
  {
    TableColumnTag parent = (TableColumnTag) findAncestorWithClass(this,
        TableColumnTag.class);
    if (parent != null)
    {
      TableIcon ti = new TableIcon();
      ti.setCssClass(cssClass);
      ti.setHeaderIcon(headerIcon);
      ti.setHref(href);
      ti.setVisible(visible);
      ti.setOnclick(onclick);
      ti.setTooltip(tooltip);
      parent.addIcon(ti);
    }
    reset();
    return super.doEndTag();
  }

  private void reset()
  {
    cssClass = null;
    href = null;
    headerIcon = false;
    visible = true;
    onclick = null;
  }

  public String getCssClass()
  {
    return cssClass;
  }

  public void setCssClass(String cssClass)
  {
    this.cssClass = cssClass;
  }

  public String getHref()
  {
    return href;
  }

  public void setHref(String href)
  {
    this.href = href;
  }

  public boolean isHeaderIcon()
  {
    return headerIcon;
  }

  public void setHeaderIcon(boolean headerIcon)
  {
    this.headerIcon = headerIcon;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }

  public String getOnclick()
  {
    return onclick;
  }

  public void setOnclick(String onclick)
  {
    this.onclick = onclick;
  }

  public String getTooltip()
  {
    return tooltip;
  }

  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }

}