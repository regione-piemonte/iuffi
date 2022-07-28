package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.validator.GenericValidator;

public class MyRadioList extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = -5602058023177228254L;
  private Object            list;
  private String            valueProperty;
  private String            textProperty;
  private String            selectedValue;
  private Boolean           inline;
  protected Boolean         preferRequestValues;
  private Boolean           onTable          = false;

  @Override
  protected void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    if (errorMessage != null)
    {
      // cssErrorClass += " text-danger";
      sb.append("<div");
      TAG_UTIL.addAttribute(sb, "class", CSS_ERROR_CLASSES);
      addErrorAttributes(sb, errorMessage);
      sb.append(">");
    }
    /* FINE WORKAROUND */
    String valueMethod = "getValue";
    String textMethod = "getLabel";
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
        int count = 0;
        while (iterator.hasNext())
        {
          processElement(sb, iterator.next(), valueMethod, textMethod, ++count);
        }
      }
      else
      {
        if (list.getClass().isArray())
        {
          Object[] array = (Object[]) list;
          int count = 0;
          for (Object element : array)
          {
            processElement(sb, element, valueMethod, textMethod, ++count);
          }
        }
      }
    }
    if (errorMessage != null)
    {
      // cssErrorClass += " text-danger";
      sb.append("</div>");
    }
  }

  private void processElement(StringBuilder sb, Object element,
      String valueMethod, String textMethod, int index)
      throws SecurityException,
      IllegalArgumentException,
      NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    String value = getProperty(element, valueMethod);
    String text = getProperty(element, textMethod);
    writeElement(sb, value, text, index);
  }

  private String getProperty(Object Element, String methodName)
      throws SecurityException, NoSuchMethodException, IllegalArgumentException,
      IllegalAccessException, InvocationTargetException
  {
    Method method = Element.getClass().getMethod(methodName);
    Object value = method.invoke(Element);
    return value == null ? "" : value.toString();
  }

  public void writeElement(StringBuilder sb, String value, String text,
      int index)
  {
    if (isInlineElement())
    {
      writeInlineElement(sb, value, text, index);
    }
    else
    {
      writeNonInlineElement(sb, value, text, index);
    }
  }

  protected void addBaseAttributes(StringBuilder sb, boolean error)
  {
    addNameAttribute(sb, error);
    addCssClassAttribute(sb, error);
    addStyleAttribute(sb, error);
    addDisableAttributeIfSet(sb, error);
    addOnClickAttribute(sb, error);
  }

  @Override
  protected void addCssClassAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "class", cssClass);
  }

  public void writeNonInlineElement(StringBuilder sb, String value, String text,
      int index)
  {
    sb.append("<div");
    addAttribute(sb, "class", "radio");
    addDisableAttributeIfSet(sb, false);
    sb.append(">");
    sb.append("<label>");

    sb.append("<input");
    addAttribute(sb, "type", "radio");
    addBaseAttributes(sb);
    addAttribute(sb, "id", id + "_" + index);
    addAttribute(sb, "value", escapeHtml(value));
    String finalSelectedValue = selectedValue;
    if (preferRequestValues != null && preferRequestValues)
    {
      finalSelectedValue = this.pageContext.getRequest().getParameter(name);
    }
    if (finalSelectedValue != null && finalSelectedValue.equals(value))
    {
      addAttribute(sb, "checked", "checked");
    }
    sb.append(">");

    if (onTable)
      sb.append("<span>");

    sb.append(escapeHtml(text));
    if (onTable)
      sb.append("</span>");
    sb.append("</label></div>");
  }

  public void writeInlineElement(StringBuilder sb, String value, String text,
      int index)
  {
    sb.append("<label");
    addAttribute(sb, "class", "radio-inline");
    addDisableAttributeIfSet(sb, false);
    sb.append("><input");
    addAttribute(sb, "type", "radio");
    addBaseAttributes(sb);
    addAttribute(sb, "id", id + "_" + index);
    addAttribute(sb, "value", escapeHtml(value));
    String finalSelectedValue = selectedValue;
    if (preferRequestValues != null && preferRequestValues)
    {
      finalSelectedValue = this.pageContext.getRequest().getParameter(name);
    }
    if (finalSelectedValue != null && finalSelectedValue.equals(value))
    {
      addAttribute(sb, "checked", "checked");
    }
    addOnClickAttribute(sb, false);
    sb.append(">");
    if (onTable)
      sb.append("<span>");

    sb.append(escapeHtml(text));
    if (onTable)
      sb.append("</span>");
    sb.append("</label>");
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

  public String getSelectedValue()
  {
    return selectedValue;
  }

  public void setSelectedValue(String selectedValue)
  {
    this.selectedValue = selectedValue;
  }

  public Boolean isInline()
  {
    return inline;
  }

  public boolean isInlineElement()
  {
    return inline != null && inline.booleanValue();
  }

  public void setInline(Boolean inline)
  {
    this.inline = inline;
  }

  public Boolean getPreferRequestValues()
  {
    return preferRequestValues;
  }

  public void setPreferRequestValues(Boolean preferRequestValues)
  {
    this.preferRequestValues = preferRequestValues;
  }

  public Boolean getOnTable()
  {
    return onTable;
  }

  public void setOnTable(Boolean onTable)
  {
    this.onTable = onTable;
  }
}
