package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class MyDropdown extends MyInputGroup
{
  /** serialVersionUID */
  private static final long serialVersionUID = -5412221111769893273L;

  private String            selectedValue;
  protected int             size             = 0;
  private Object            list;
  private String            valueProperty;
  private String            textProperty;
  private Object            headerKey        = "";
  private String            headerValue      = " -- selezionare --";
  private boolean           disabled         = false;
  private boolean           disabledOptions  = false;
  private boolean           readonly         = false;
  private boolean           leftDropDown     = true;
  protected Boolean         preferRequestValues;

  public MyDropdown()
  {
    super("my-dropdown dropdown");
  }

  @Override
  protected void includeRequiredJSFile()
  {
    addJSFileToPage("/"+IuffiConstants.IUFFIWEB.WEB_CONTEXT+"/js/list.js");
  }

  protected void writeCustomTag(StringBuilder sb, String errorMessage,
      boolean wrappedInAGroup) throws Exception
  {
    String finalSelectedValue = selectedValue;
    if (preferRequestValues != null && preferRequestValues.booleanValue())
    {
      finalSelectedValue = this.pageContext.getRequest().getParameter(name);
    }
    sb.append("<div");
    addBaseAttributes(sb, errorMessage != null);
    TAG_UTIL.addAttribute(sb, "onkeydown",
        "return dropdown_onKeydown(this, event)");
    sb.append("><button");
    TAG_UTIL.addAttribute(sb, "id", id + "_btn");
    TAG_UTIL.addAttribute(sb, "class",
        "btn btn-default dropdown-select-button dropdown-toggle");
    TAG_UTIL.addAttribute(sb, "data-toggle", "dropdown");
    TAG_UTIL.addAttribute(sb, "aria-haspopup", "true");
    TAG_UTIL.addAttribute(sb, "aria-expanded", "false");
    sb.append("><span class=\"dropdown-value\"");
    TAG_UTIL.addAttribute(sb, "id", id + "_value");
    sb.append(">").append(headerValue).append(
        "</span> <span style=\"position:absolute;right:10px;top:14px\" class=\"caret\"></span></button>");
    sb.append("<input type=\"hidden\"");
    if (disabled)
    {
      sb.append(" disabled=\"disabled\" ");
    }
    TAG_UTIL.addAttribute(sb, "class", "my-dropdown-hidden");
    super.addIdAttribute(sb, false);
    super.addNameAttribute(sb, false);
    TAG_UTIL.addAttribute(sb, "value", "");
    sb.append(" /><ul ");
    TAG_UTIL.addAttribute(sb, "id", id + "_list");
    TAG_UTIL.addAttribute(sb, "class", "dropdown-menu dropdown-menu-"
        + (leftDropDown ? "left" : "right") + " pre-scollable");
    sb.append(">");
    sb.append(
        "<li style=\"padding-left:8px;padding-right:8px\"><div class=\"input-group\" style=\"width:100%\">"
            + "<input type=\"text\" class=\"form-control dropdown-filter\" id=\"")
        .append(id)
        .append(
            "_filter\" onclick=\"event.preventDefault();event.stopPropagation()\" onkeyup=\"applyFiltroDropdown(this)\" onchange=\"event.preventDefault();event.stopPropagation()\"/></div></li>");
    writeOption(sb, (String) headerKey, headerValue, (String) null, false);
    writeOptions(sb, finalSelectedValue);
    sb.append("</ul></div>");
  }

  @Override
  protected void addIdAttribute(StringBuilder sb, boolean error)
  {
    // Non deve fare nulla perchè l'id che viene passato NON deve finire sul
    // <div> della dropdown ma sull'input type="hidden" al suo interno, quindi
    // lo gestisco
    // a mano
  }

  @Override
  protected void addNameAttribute(StringBuilder sb, boolean error)
  {
    // Non deve fare nulla perchè ilname che viene passato NON deve finire sul
    // <div> della dropdown ma sull'input type="hidden" al suo interno, quindi
    // lo gestisco
    // a mano
  }

  protected void writeOptions(StringBuilder sb, String finalSelectedValue)
      throws SecurityException, IllegalArgumentException, NoSuchMethodException,
      IllegalAccessException,
      InvocationTargetException
  {
    String valueMethod = "getId";
    String textMethod = "getDescrizione";
    if (!GenericValidator.isBlankOrNull(valueProperty))
    {
      valueMethod = "get" + Character.toUpperCase(valueProperty.charAt(0))
          + valueProperty.substring(1);
    }
    if (!GenericValidator.isBlankOrNull(textProperty))
    {
      textMethod = "get" + Character.toUpperCase(textProperty.charAt(0))
          + textProperty.substring(1);
    }
    if (list != null)
    {
      if (list instanceof Iterable)
      {
        Iterator<?> iterator = ((Iterable<?>) list).iterator();
        while (iterator.hasNext())
        {
          processOption(sb, iterator.next(), valueMethod, textMethod,
              finalSelectedValue);
        }
      }
      else
      {
        if (list.getClass().isArray())
        {
          Object[] array = (Object[]) list;
          for (Object option : array)
          {
            processOption(sb, option, valueMethod, textMethod,
                finalSelectedValue);
          }
        }
      }
    }
  }

  private void processOption(StringBuilder sb, Object option,
      String valueMethod, String textMethod, String finalSelectedValue)
      throws SecurityException,
      IllegalArgumentException,
      NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    String value = getProperty(option, valueMethod);
    String text = getProperty(option, textMethod);
    writeOption(sb, value, text, finalSelectedValue);
  }

  private String getProperty(Object option, String methodName)
      throws SecurityException, NoSuchMethodException, IllegalArgumentException,
      IllegalAccessException, InvocationTargetException
  {
    Method method = option.getClass().getMethod(methodName);
    Object value = method.invoke(option);
    return value == null ? "" : value.toString();
  }

  public void writeOption(StringBuilder sb, String value, String text,
      String finalSelectedValue)
  {
    writeOption(sb, value, text, finalSelectedValue, true);
  }

  public void writeOption(StringBuilder sb, String value, String text,
      String finalSelectedValue, boolean searchable)
  {
    final String escapedText = StringEscapeUtils.escapeHtml4(text);
    final String escapedValue = StringEscapeUtils.escapeHtml4(value);
    sb.append("<li><a href=\"#\" class=\"dropdown-list-item");
    final boolean selected = escapedValue != null
        && escapedValue.equals(finalSelectedValue);
    if (selected)
    {
      sb.append(" active");
    }
    sb.append("\" data-searchable=\"").append(searchable).append("\"");
    sb.append(" title=\"").append(StringEscapeUtils.escapeHtml4(escapedText));
    sb.append("\" data-value=\"").append(StringEscapeUtils.escapeHtml4(value))
        .append("\"  onclick=\"return selectDropdownItem(this)\">")
        .append(escapedText).append("</a></li>");
  }

  public String getSelectedValue()
  {

    return selectedValue;
  }

  public void setSelectedValue(String selectedValue)
  {
    this.selectedValue = selectedValue;
  }

  public int getSize()
  {
    return size;
  }

  public void setSize(int size)
  {
    this.size = size;
  }

  public Object getList()
  {
    return list;
  }

  public void setList(Object list)
  {
    this.list = list;
  }

  public String getValueProperty()
  {
    return valueProperty;
  }

  public void setValueProperty(String valueProperty)
  {
    this.valueProperty = valueProperty;
  }

  public String getTextProperty()
  {
    return textProperty;
  }

  public void setTextProperty(String textProperty)
  {
    this.textProperty = textProperty;
  }

  public Object getHeaderKey()
  {
    return headerKey;
  }

  public void setHeaderKey(Object headerKey)
  {
    this.headerKey = headerKey;
  }

  public String getHeaderValue()
  {
    return headerValue;
  }

  public void setHeaderValue(String headerValue)
  {
    this.headerValue = headerValue;
  }

  public boolean isDisabled()
  {
    return disabled;
  }

  public void setDisabled(boolean disabled)
  {
    this.disabled = disabled;
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public void setReadonly(boolean readonly)
  {
    this.readonly = readonly;
  }

  public Boolean getPreferRequestValues()
  {
    return preferRequestValues;
  }

  public void setPreferRequestValues(Boolean preferRequestValues)
  {
    this.preferRequestValues = preferRequestValues;
  }

  public boolean isDisabledOptions()
  {
    return disabledOptions;
  }

  public void setDisabledOptions(boolean disabledOptions)
  {
    this.disabledOptions = disabledOptions;
  }

  public void setLeftDropDown(boolean leftDropDown)
  {
    this.leftDropDown = leftDropDown;
  }
}
