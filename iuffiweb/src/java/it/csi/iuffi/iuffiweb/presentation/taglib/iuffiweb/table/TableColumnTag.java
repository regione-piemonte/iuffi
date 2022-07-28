package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.BaseTag;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table.dto.TableCellType;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table.dto.TableColumn;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table.dto.TableIcon;

public class TableColumnTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6833165635857338283L;
  protected boolean         visible          = true;
  protected String          label;
  protected String          property;
  protected TableCellType   type             = TableCellType.TEXT;
  protected String          cssClass         = null;
  protected List<TableIcon> icons;

  public void reset()
  {
    visible = true;
    label = null;
    property = null;
    type = TableCellType.TEXT;
    icons = null;
    cssClass = null;
  }

  @Override
  public int doEndTag() throws JspException
  {
    TableTag parent = (TableTag) findAncestorWithClass(this,
        TableTag.class);
    if (parent != null)
    {
      TableColumn tc = new TableColumn();
      tc.setType(type);
      tc.setLabel(label);
      tc.setProperty(property);
      tc.setVisible(visible);
      tc.setIcons(icons);
      tc.setCssClass(cssClass);
      parent.addTableColumn(tc);
    }
    reset();
    return super.doEndTag();
  }

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

  public String getType()
  {
    return type.toString();
  }

  public void setType(String dataType)
  {
    if (!GenericValidator.isBlankOrNull(dataType))
    {
      this.type = TableCellType.valueOf(dataType);
    }
    else
    {
      this.type = TableCellType.TEXT;
    }
  }

  public void addIcon(TableIcon ti)
  {
    if (icons == null)
    {
      icons = new ArrayList<TableIcon>();
    }
    icons.add(ti);
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