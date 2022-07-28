package it.csi.iuffi.iuffiweb.presentation.taglib.html;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SelectTag extends SimpleTagSupport
{
  private String  name;
  private String  id;
  private String  cssClass;
  private Object  list;
  private String  valueProperty;
  private String  textProperty;
  private String  selectedValue;
  private boolean header      = true;
  private Object  headerKey   = "";
  private String  headerValue = "-- selezionare --";

  public void doTag() throws JspException
  {
    try
    {
      StringBuilder sb = new StringBuilder();
      sb.append("<select class=\"form-control\" ");
      if (id != null)
      {
        sb.append(" id = \"").append(id).append("\"");
      }
      if (name != null)
      {
        sb.append(" name = \"").append(name).append("\"");
      }
      if (cssClass != null)
      {
        sb.append(" class = \"").append(cssClass).append("\"");
      }
      sb.append(">");
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
      if (header)
      {
        writeOption(sb, IuffiUtils.STRING.checkNull(headerKey), headerValue);
      }
      if (list != null)
      {
        if (list instanceof Iterable)
        {
          Iterator<?> iterator = ((Iterable<?>) list).iterator();
          while (iterator.hasNext())
          {
            processOption(sb, iterator.next(), valueMethod, textMethod);
          }
        }
        else
        {
          if (list.getClass().isArray())
          {
            Object[] array = (Object[]) list;
            for (Object option : array)
            {
              processOption(sb, option, valueMethod, textMethod);
            }
          }
        }
      }
      sb.append("</select>");
      getJspContext().getOut().write(sb.toString());
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
  }

  private void processOption(StringBuilder sb, Object option,
      String valueMethod, String textMethod)
      throws SecurityException, IllegalArgumentException,
      NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    String value = getProperty(option, valueMethod);
    String text = getProperty(option, textMethod);
    writeOption(sb, value, text);
  }

  private String getProperty(Object option, String methodName)
      throws SecurityException, NoSuchMethodException, IllegalArgumentException,
      IllegalAccessException, InvocationTargetException
  {
    Method method = option.getClass().getMethod(methodName);
    Object value = method.invoke(option);
    return value == null ? "" : value.toString();
  }

  public void writeOption(StringBuilder sb, String value, String text)
  {
    sb.append("<option value=\"").append(StringEscapeUtils.escapeHtml4(value))
        .append("\"");
    if (selectedValue != null && selectedValue.equals(value))
    {
      sb.append(" selected='selected' ");
    }
    sb.append(">").append(StringEscapeUtils.escapeHtml4(text))
        .append("</option>");
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
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

  public Object getSelectedValue()
  {
    return selectedValue;
  }

  public void setSelectedValue(Object selectedValue)
  {
    if (selectedValue == null || selectedValue instanceof String)
    {
      this.selectedValue = (String) selectedValue;
    }
    else
    {
      this.selectedValue = selectedValue.toString();
    }
  }

  public boolean isHeader()
  {
    return header;
  }

  public void setHeader(boolean header)
  {
    this.header = header;
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

}
