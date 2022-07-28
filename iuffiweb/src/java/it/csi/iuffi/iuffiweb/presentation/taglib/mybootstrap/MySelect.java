package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class MySelect extends MyInputGroup
{
  static final String MY_SELECT_CHOICE_SELECTED_VALUE = "mySelectChoiceSelectedValue";
	/** serialVersionUID */
  private static final long       serialVersionUID   = -5412221111769893273L;

  private HashMap<String, String> insertedValuesMap  = null;

  private String[]                selectedValues;
  protected int                   size               = 0;
  private Object                  list;
  private String                  valueProperty;
  private String                  textProperty;
  private String            	  codeProperty;  
  private Object                  headerKey          = "";
  private String                  headerValue        = " -- selezionare --";
  private boolean                 header             = true;
  private boolean                 multiple           = false;
  private boolean                 disabled           = false;
  private boolean                 disabledOptions    = false;
  private boolean                 readonly           = false;
  private boolean           	  dataChoice         = false;  
  private boolean                 forceRequestValues = false;
  private boolean                 addOptionsTitle    = false;
  protected Boolean               preferRequestValues;
  private final String 			  GET_CODICE = "getCodice";
  final String 				      CODE_DEFAULT = "CODE_DEFAULT";

  public MySelect()
  {
    super("form-control");
  }
  
  public MySelect(String cssClass)
  {
	  super(cssClass);
  }
  
  public MySelect(String cssClass, String styleCss)
  {
	  super(cssClass,styleCss);
  }

  protected void writeCustomTag(StringBuilder sb, String errorMessage,
      boolean wrappedInAGroup) throws Exception
  {
    insertedValuesMap = new HashMap<String, String>();
    sb.append("<select");
    addBaseAttributes(sb, errorMessage != null);
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

  protected void writeOptions(StringBuilder sb) throws SecurityException,
      IllegalArgumentException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException
  {
    String valueMethod = "getId";
    String textMethod = "getDescrizione";
    String codeMethod = GET_CODICE;
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
    if(!GenericValidator.isBlankOrNull(codeProperty))
    {
    	codeMethod = "get" + Character.toUpperCase(codeProperty.charAt(0)) + codeProperty.substring(1);
    }
    if (header)
    {
    	writeOption(sb, IuffiUtils.STRING.checkNull(headerKey), headerValue,CODE_DEFAULT, null, null);
    }
    String[] finalSelectedValues = selectedValues;
    String finalChoiceValue = (String)getSelectedChoice();//choice impostata dall'utente
    if (preferRequestValues != null && preferRequestValues.booleanValue())
    {
      finalSelectedValues = this.pageContext.getRequest()
          .getParameterValues(name);
      finalChoiceValue = getChoiceValueFromRequest(); //choice da request
    }

    if (list != null)
    {
      if (list instanceof Iterable)
      {
        Iterator<?> iterator = ((Iterable<?>) list).iterator();
        while (iterator.hasNext())
        {
        	processOption(sb, iterator.next(), valueMethod, textMethod, codeMethod, finalSelectedValues,finalChoiceValue);
        }
      }
      else
      {
        if (list.getClass().isArray())
        {
          Object[] array = (Object[]) list;
          for (Object option : array)
          {
        	  processOption(sb, option, valueMethod, textMethod, codeMethod, finalSelectedValues, finalChoiceValue);
          }
        }
      }
    }

    if (forceRequestValues)
    {
      // Forzo inserimento di options che magari non sono presenti nella mia
      // list appena elaborata
      if (finalSelectedValues != null)
      {
        for (String val : finalSelectedValues)
        {
          if (!insertedValuesMap.containsKey(val))
          {
        	  writeOption(sb, val, val, null, finalSelectedValues, finalChoiceValue);
          }
        }
      }
    }
  }

  private void processOption(StringBuilder sb, Object option, String valueMethod, String textMethod, String codeMethod, String[] finalSelectedValue, String finalChoiceValue)
      throws SecurityException,
      IllegalArgumentException,
      NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    String value = getProperty(option, valueMethod);
    String text = getProperty(option, textMethod);
    String code = getProperty(option, codeMethod);
    insertedValuesMap.put(value, text);
    writeOption(sb, value, text, code, finalSelectedValue, finalChoiceValue);
  }

  String getProperty(Object option, String methodName) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
  		IllegalAccessException, InvocationTargetException
  {
	try
	{
		Method method = option.getClass().getMethod(methodName);
		Object value = method.invoke(option);
		return value == null ? "" : value.toString();
	}catch(NoSuchMethodException e)
	{
		if(methodName.equals(GET_CODICE)) //non tutte le MySelect devono avere necessariamente il codice 
		{
			return "";
		}
		else
		{
			throw e;
		}
	}
  }

	public void writeOption(StringBuilder sb, String value, String text, String code, String[] finalSelectedValue,
			String finalChoiceValue)
	{
	
		sb.append("<option value=\"").append(StringEscapeUtils.escapeHtml4(value)).append("\"");
		if (finalSelectedValue != null && IuffiUtils.ARRAY.contains(finalSelectedValue, value))
		{
			sb.append(" selected=\"selected\" ");
			if(this instanceof MySelectChoice)
			{
				this.pageContext.getRequest().setAttribute(MY_SELECT_CHOICE_SELECTED_VALUE, code);
			}
		}
		if (code != null)
		{
			sb.append(" data-code=\"" + code + "\"");
		}
		if (addOptionsTitle)
		{
			sb.append(" title=\"" + StringEscapeUtils.escapeHtml4(text).replace("'", "''") + "\" ");
		}
		if (disabledOptions)
		{
			sb.append(" disabled ");
		}
		if (addOptionsTitle)
		{
			sb.append(" title=\"" + StringEscapeUtils.escapeHtml4(text).replace("'", "''") + "\" ");
		}
		if (disabledOptions)
		{
			sb.append(" disabled ");
		}
		sb.append(">").append(StringEscapeUtils.escapeHtml4(text)).append("</option>");
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

  public boolean isForceRequestValues()
  {
    return forceRequestValues;
  }

  public void setForceRequestValues(boolean forceRequestValues)
  {
    this.forceRequestValues = forceRequestValues;
  }

  public boolean isAddOptionsTitle()
  {
    return addOptionsTitle;
  }

  public void setAddOptionsTitle(boolean addOptionsTitle)
  {
    this.addOptionsTitle = addOptionsTitle;
  }
  
	public boolean isDataChoice()
	{
		return dataChoice;
	}
	
	public void setDataChoice(boolean dataChoice)
	{
		this.dataChoice = dataChoice;
	}
	
	public String getCodeProperty()
	{
		return codeProperty;
	}
	
	public void setCodeProperty(String codeProperty)
	{
		this.codeProperty = codeProperty;
	}
	
	public Object getSelectedChoice()
	{
		return null;
	}
	
	public String getChoiceValueFromRequest()
	{
		return null;
	}
}
