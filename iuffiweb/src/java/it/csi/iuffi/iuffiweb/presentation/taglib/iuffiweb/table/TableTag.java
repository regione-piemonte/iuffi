package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.BaseTag;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table.dto.TableCellType;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table.dto.TableColumn;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table.dto.TableIcon;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class TableTag extends BaseTag
{
  /** serialVersionUID */
  protected static final long   serialVersionUID           = -100976940891149600L;
  protected static final String DEFAULT_CSS                = "table table-hover table-striped table-bordered";
  private static final String   DEFAULT_EMPTY_LIST_MESSAGE = "Nessun elemento trovato";
  protected List<TableColumn>   columns                    = null;
  protected List<?>             list                       = null;
  protected String              cssClass                   = null;
  protected String              id;
  private String                customHeader;
  private String                emptyListMessage           = DEFAULT_EMPTY_LIST_MESSAGE;

  @Override
  public int doStartTag() throws JspException
  {
    columns = new ArrayList<TableColumn>();
    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException
  {
    try
    {
      this.pageContext.getOut().write(getHtml());
      reset();
    }
    catch (IOException e)
    {
      throw new JspException(e);
    }
    return super.doEndTag();
  }

  private void reset()
  {
    columns = null;
    list = null;
    cssClass = null;
    id = null;
    customHeader = null;
    emptyListMessage = DEFAULT_EMPTY_LIST_MESSAGE;
  }

  public void addTableColumn(TableColumn column)
  {
    columns.add(column);
  }

  protected String getHtml()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"table-responsive\">");
    sb.append("<table");
    if (!GenericValidator.isBlankOrNull(id))
    {
      addAttribute(sb, "id", id);
    }
    if (GenericValidator.isBlankOrNull(cssClass))
    {
      addAttribute(sb, "class", DEFAULT_CSS);
    }
    else
    {
      addAttribute(sb, "class", DEFAULT_CSS + " " + cssClass);
    }
    sb.append(">");
    writeTableHeader(sb);
    writeTableBody(sb);
    sb.append("</table></div>");
    return sb.toString();
  }

  protected void writeTableHeader(StringBuilder sb)
  {
    if (!GenericValidator.isBlankOrNull(customHeader))
    {
      sb.append(customHeader);
    }
    else
    {
      sb.append("<tr>");
      for (TableColumn th : columns)
      {
        if (th.isVisible())
        {
          writeColumnHeader(sb, th);
        }
      }
      sb.append("<tr>");
    }
  }

  private void writeColumnHeader(StringBuilder sb, TableColumn th)
  {
    sb.append("<th");
    String css = th.getCssClass();
    if (!GenericValidator.isBlankOrNull(css))
    {
      addAttribute(sb, "class", css);
    }
    sb.append(">");
    String label = th.getLabel();
    List<TableIcon> icons = th.getIcons();
    if (icons != null)
    {
      for (TableIcon icon : icons)
      {
        if (icon.isHeaderIcon() && icon.isVisible())
        {
          sb.append("<a");
          String onclick = icon.getOnclick();
          if (onclick != null)
          {
            addAttribute(sb, "onclick", onclick);
          }
          addAttribute(sb, "class", icon.getCssClass());
          addAttribute(sb, "href", icon.getHref());
          sb.append("></a>");
        }
      }
    }
    if (!GenericValidator.isBlankOrNull(label))
    {
      sb.append(escapeHtml(th.getLabel()));
    }
    sb.append("</th>");
  }

  private String getIconClass(Object element, Map<String, Method> methods,
      TableIcon icon)
  {
    String cssClass = icon.getCssClass();
    if (cssClass.startsWith("property:"))
    {
      return IuffiUtils.STRING
          .nvl(invokeGetter(element, methods, cssClass.substring(9)));
    }
    else
    {
      return cssClass;
    }
  }

  protected void writeTableBody(StringBuilder sb)
  {
    if (list != null && list.size() > 0)
    {
      Map<String, Method> methods = null;
      for (Object rowData : list)
      {
        if (rowData != null)
        {
          if (methods == null)
          {
            methods = getMethods(list.get(0));
          }
          sb.append("<tr>");
          for (TableColumn column : columns)
          {
            if (column.isVisible())
            {
              writeCell(sb, column, rowData, methods);
            }
          }
          sb.append("</tr>");
        }
      }
    }
    else
    {
      int size = columns.size();
      sb.append("<tr><td");
      addAttribute(sb, "colspan", String.valueOf(size));
      sb.append(">").append(emptyListMessage).append("</td>");
    }
  }

  private void writeCell(StringBuilder sb, TableColumn column, Object element,
      Map<String, Method> methods)
  {
    String value = getValue(element, column, methods);
    sb.append("<td");
    String cssClass = column.getCssClass();
    List<TableIcon> icons = column.getIcons();
    TableCellType type = column.getType();
    if (cssClass != null)
    {
      if (icons != null && !icons.isEmpty())
      {
        addAttribute(sb, "class", cssClass + " icons_cell");
      }
      else
      {
        if (type != TableCellType.TEXT &&
            type != TableCellType.CHECK &&
            type != TableCellType.RADIO)
        {
          addAttribute(sb, "class", cssClass + " alignRight");
        }
        else
        {
          addAttribute(sb, "class", cssClass);
        }
      }
    }
    else
    {
      if (icons != null && !icons.isEmpty())
      {
        addAttribute(sb, "class", "icons_cell");
      }
    }

    sb.append(">");
    if (icons != null)
    {
      for (TableIcon icon : icons)
      {
        if (!icon.isHeaderIcon() && icon.isVisible())
        {
          String cssIcon = getIconClass(element, methods, icon);
          if (!GenericValidator.isBlankOrNull(cssIcon))
          {
            String href = icon.getHref();
            if (GenericValidator.isBlankOrNull(href))
            {
              sb.append("<span");
              addAttribute(sb, "class", cssIcon);
              String title = getIconTooltip(element, methods, icon);
              if (title != null)
              {
                addAttribute(sb, "title", escapeHtml(title));
              }
              sb.append("></span>");
            }
            else
            {
              if (href.indexOf("{") >= 0)
              {
                href = href.replace("{" + column.getProperty() + "}", value);
              }
              sb.append("<a");
              String onclick = icon.getOnclick();
              if (onclick != null)
              {
                String placeHolder = "{" + column.getProperty() + "}";
                if (onclick.indexOf(placeHolder) >= 0)
                {
                  onclick = onclick.replace(placeHolder, "" + value);
                }
                addAttribute(sb, "onclick", onclick);
              }
              addAttribute(sb, "class", getIconClass(element, methods, icon));
              addAttribute(sb, "href", href);
              sb.append("></a>");
            }
          }
        }
      }
    }

    if (type != TableCellType.NO_TEXT && value != null)
    {
      sb.append(IuffiUtils.STRING.nvl(value));
    }
    sb.append("</td>");
  }

  private String getIconTooltip(Object element, Map<String, Method> methods,
      TableIcon icon)
  {
    String tooltip = icon.getTooltip();
    if (tooltip != null && tooltip.startsWith("property:"))
    {
      return IuffiUtils.STRING
          .nvl(invokeGetter(element, methods, tooltip.substring(9)));
    }
    else
    {
      return tooltip;
    }
  }

  protected Map<String, Method> getMethods(Object elemento)
  {
    HashMap<String, Method> metodi = new HashMap<String, Method>();
    for (TableColumn column : columns)
    {
      aggiungiMetodoAllaMappa(metodi, elemento, column.getProperty());
      readIconMethodProperties(metodi, elemento, column.getIcons());
    }
    return metodi;
  }

  private void readIconMethodProperties(HashMap<String, Method> metodi,
      Object elemento, List<TableIcon> icons)
  {
    if (icons == null)
    {
      return;
    }
    for (TableIcon icon : icons)
    {
      String css = icon.getCssClass();
      if (css.startsWith("property:"))
      {
        aggiungiMetodoAllaMappa(metodi, elemento, css.substring(9));
      }
      String tooltip = icon.getTooltip();
      if (tooltip != null && tooltip.startsWith("property:"))
      {
        aggiungiMetodoAllaMappa(metodi, elemento, tooltip.substring(9));
      }
    }
  }

  private void aggiungiMetodoAllaMappa(HashMap<String, Method> metodi,
      Object elemento, String nomeProprieta)
  {
    Method metodo = null;
    String getter = getGetter(nomeProprieta);
    if (getter != null && getter.trim().length() > 0)
    {
      try
      {
        metodo = elemento.getClass().getMethod(getter);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      metodi.put(nomeProprieta, metodo);
    }
  }

  public String getGetter(String nomeProprieta)
  {
    StringBuilder sb = new StringBuilder();
    if (nomeProprieta != null)
    {
      int len = nomeProprieta.length();
      if (len > 0)
      {
        sb.append("get")
            .append(String.valueOf(nomeProprieta.charAt(0)).toUpperCase());
        if (len > 1)
        {
          sb.append(nomeProprieta.substring(1));
        }
      }
    }
    return sb.toString();
  }

  public String getValue(Object elemento, TableColumn colonna,
      Map<String, Method> metodi)
  {
    String property = colonna.getProperty();
    if (GenericValidator.isBlankOrNull(property))
    {
      return "";
    }
    Object valoreProprieta = invokeGetter(elemento, metodi, property);
    TableCellType tipologia = colonna.getType();
    return formatValore(tipologia, valoreProprieta, colonna);
  }

  public Object invokeGetter(Object elemento, Map<String, Method> metodi,
      String property)
  {
    Object valoreProprieta = "ERRORE!";
    Method metodo = metodi.get(property);
    if (metodo != null)
    {
      try
      {
        Object result = metodo.invoke(elemento);
        if (result == null)
        {
          return null;
        }
        else
        {
          valoreProprieta = result;
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return valoreProprieta;
  }

  protected String formatValore(TableCellType tipologia, Object valoreProprieta,
      TableColumn colonna)
  {
    if (valoreProprieta == null)
    {
      return null;
    }
    if (tipologia == null)
    {
      return escapeHtml(IuffiUtils.STRING.nvl(valoreProprieta));
    }
    switch (tipologia)
    {
      case RADIO:
        return getInputTag("radio",
            escapeHtml(IuffiUtils.STRING.nvl(valoreProprieta)),
            colonna.getProperty());
      case CHECK:
        return getInputTag("checkbox",
            escapeHtml(IuffiUtils.STRING.nvl(valoreProprieta)),
            colonna.getProperty());
      case DATETIME:
        return IuffiUtils.DATE.formatDateTime((Date) valoreProprieta);
      case DATE:
        return IuffiUtils.DATE.formatDate((Date) valoreProprieta);
      case INTEGER:
        return IuffiUtils.FORMAT.formatGenericNumber(
            new BigDecimal(valoreProprieta.toString()), 0, false);
      case EURO:
        return IuffiUtils.FORMAT.formatGenericNumber(
            new BigDecimal(valoreProprieta.toString()), 2, true) + " &euro;";
      case DECIMAL:
        return IuffiUtils.FORMAT.formatGenericNumber(
            new BigDecimal(valoreProprieta.toString()), 10, false);
      case DECIMAL_2:
        return IuffiUtils.FORMAT.formatGenericNumber(
            new BigDecimal(valoreProprieta.toString()), 2, true);
      case DECIMAL_4:
        return IuffiUtils.FORMAT.formatGenericNumber(
            new BigDecimal(valoreProprieta.toString()), 4, true);
      default:
        return escapeHtml(IuffiUtils.STRING.nvl(valoreProprieta));
    }
  }

  protected String getInputTag(String type, String value, String name)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<input");
    addAttribute(sb, "type", type);
    addAttribute(sb, "value", escapeHtml(value));
    if (!GenericValidator.isBlankOrNull(name))
    {
      addAttribute(sb, "name", name);
    }
    sb.append("/>");
    return sb.toString();
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public List<?> getList()
  {
    return list;
  }

  public void setList(List<Object> list)
  {
    this.list = list;
  }

  public void setCustomHeader(String customHeader)
  {
    this.customHeader = customHeader;
  }

  public String getEmptyListMessage()
  {
    return emptyListMessage;
  }

  public void setEmptyListMessage(String emptyListMessage)
  {
    this.emptyListMessage = emptyListMessage;
  }

}
