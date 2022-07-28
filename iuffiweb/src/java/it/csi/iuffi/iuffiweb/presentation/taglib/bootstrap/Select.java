package it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class Select extends SimpleBoostrapTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1080560085783747016L;
  private String            selectedValue;
  protected int             size             = 0;
  private Object            list;
  private String            valueProperty;
  private String            textProperty;
  private Object            headerKey        = "";
  private String            headerValue      = " -- selezionare --";
  private boolean           header           = true;
  private boolean           multiple         = false;
  private boolean           readonly         = false;

  @Override
  protected void writeCustomTagElement(StringBuilder sb)
      throws SecurityException, IllegalArgumentException, NoSuchMethodException,
      IllegalAccessException, InvocationTargetException
  {
    sb.append("<select");
    writeDefaultAttributes(sb);
    if (multiple)
      sb.append(" multiple ");
    if (disabled)
      sb.append(" disabled=\"disabled\" ");
    if (readonly)
      sb.append(" readonly=\"readonly\" ");
    if (size > 0)
      sb.append(" size=\"" + size + "\" ");
    sb.append(">");
    writeOptions(sb);
    sb.append("</select>");
  }

  protected void writeOptions(StringBuilder sb)
      throws SecurityException, IllegalArgumentException, NoSuchMethodException,
      IllegalAccessException, InvocationTargetException
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

  public String getSelectedValue()
  {
    return selectedValue;
  }

  public void setSelectedValue(String selectedValue)
  {
    this.selectedValue = selectedValue;
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

  public boolean isHeader()
  {
    return header;
  }

  public void setHeader(boolean header)
  {
    this.header = header;
  }

  public boolean isMultiple()
  {
    return multiple;
  }

  public void setMultiple(boolean multiple)
  {
    this.multiple = multiple;
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public void setReadonly(boolean readonly)
  {
    this.readonly = readonly;
  }

  public int getSize()
  {
    return size;
  }

  public void setSize(int size)
  {
    this.size = size;
  }

}
