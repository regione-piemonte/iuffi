package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class MyList extends MyInputGroup
{
  /** serialVersionUID */
  private static final long serialVersionUID  = -5412221111769893273L;

  private String[]          selectedValues;
  protected int             size              = 0;
  private Object            list;
  private String            valueProperty;
  private String            textProperty;
  private String            onClickItem       = null;
  protected Boolean         preferRequestValues;
  protected boolean         multipleSelection = false;

  public MyList()
  {
    super("my-list pre-scrollable list-group");
  }

  @Override
  protected void includeRequiredJSFile()
  {
    addJSFileToPage("/"+IuffiConstants.IUFFIWEB.WEB_CONTEXT+"/js/list.js");
  }

  protected void writeCustomTag(StringBuilder sb, String errorMessage,
      boolean wrappedInAGroup) throws Exception
  {
    String[] finalSelectedValues = selectedValues;
    if (preferRequestValues != null && preferRequestValues.booleanValue())
    {
      finalSelectedValues = this.pageContext.getRequest()
          .getParameterValues(name);
    }
    sb.append("<div");
    TAG_UTIL.addAttribute(sb, "data-multiple-selection",
        Boolean.valueOf(multipleSelection));
    addBaseAttributes(sb, errorMessage != null);
    sb.append(">");
    writeOptions(sb, finalSelectedValues);
    sb.append("</div>");
  }

  protected void writeOptions(StringBuilder sb, String[] finalSelectedValues)
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
              finalSelectedValues);
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
                finalSelectedValues);
          }
        }
      }
    }
  }

  private void processOption(StringBuilder sb, Object option,
      String valueMethod, String textMethod,
      String[] finalSelectedValue) throws SecurityException,
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
      String[] finalSelectedValue)
  {
    writeOption(sb, value, text, finalSelectedValue, true);
  }

  public void writeOption(StringBuilder sb, String value, String text,
      String[] finalSelectedValue, boolean searchable)
  {
    final String escapedText = StringEscapeUtils.escapeHtml4(text);
    final String escapedValue = StringEscapeUtils.escapeHtml4(value);
    if (disabled == null || !disabled)
    {
      sb.append("<a");
      TAG_UTIL.addAttribute(sb, "href", "");
      TAG_UTIL.addAttribute(sb, "class", "list-group-item");
      if (onClickItem != null)
      {
        TAG_UTIL.addAttribute(sb, "onclick",
            "return " + onClickItem + "(this,'" + escapedValue + "')");
      }
      else
      {
        TAG_UTIL.addAttribute(sb, "onclick",
            "return myList_defaultOnClickItem(this,'" + escapedValue + "')");
      }
      TAG_UTIL.addAttribute(sb, "title", escapedText);
      TAG_UTIL.addAttribute(sb, "data-value", escapedValue);
      sb.append(">").append(escapedText).append("</a>");
    }
    else
    {
      sb.append("<span");
      TAG_UTIL.addAttribute(sb, "class", "list-group-item");
      sb.append(">").append(escapedText).append("</span>");
    }
  }

  public String[] getSelectedValues()
  {

    return selectedValues;
  }

  public void setSelectedValue(Object selectedValue)
  {
    if (selectedValue == null)
    {
      this.selectedValues = null;
    }
    else
    {
      if (selectedValue instanceof String[])
      {
        this.selectedValues = (String[]) selectedValue;
      }
      else
      {
        if (selectedValue instanceof String)
        {
          this.selectedValues = new String[]
          { (String) selectedValue };
        }
        else
        {
          if (selectedValue instanceof List)
          {
            List<?> list = (List<?>) selectedValue;
            int size = list.size();
            this.selectedValues = new String[size];
            for (int i = 0; i < size; ++i)
            {
              Object obj = list.get(i);
              selectedValues[i] = obj == null ? null : obj.toString();
            }
          }
          else
          {
            this.selectedValues = new String[]
            { selectedValue.toString() };
          }
        }
      }
    }
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

  public Boolean getPreferRequestValues()
  {
    return preferRequestValues;
  }

  public void setPreferRequestValues(Boolean preferRequestValues)
  {
    this.preferRequestValues = preferRequestValues;
  }

  public String getOnclickItem()
  {
    return onClickItem;
  }

  public void setOnclickItem(String onclick)
  {
    this.onClickItem = onclick;
  }

  public boolean isMultipleSelection()
  {
    return multipleSelection;
  }

  public void setMultipleSelection(boolean multipleSelection)
  {
    this.multipleSelection = multipleSelection;
  }
}
