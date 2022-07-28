package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table.dto;

import java.util.List;

public class TableColumn
{
  protected boolean         visible = true;
  protected String          label;
  protected String          property;
  protected TableCellType   type    = TableCellType.TEXT;
  protected String          cssClass;
  protected List<TableIcon> icons;

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getProperty()
  {
    return property;
  }

  public void setProperty(String property)
  {
    this.property = property;
  }

  public TableCellType getType()
  {
    return type;
  }

  public void setType(TableCellType dataType)
  {
    this.type = dataType;
    if (this.type == null)
    {
      this.type = TableCellType.TEXT;
    }
  }

  public List<TableIcon> getIcons()
  {
    return icons;
  }

  public void setIcons(List<TableIcon> icons)
  {
    this.icons = icons;
  }

  public String getCssClass()
  {
    return cssClass;
  }

  public void setCssClass(String cssClass)
  {
    this.cssClass = cssClass;
  }
}
