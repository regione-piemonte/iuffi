package it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

public class CheckGroup extends SimpleBoostrapTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1080560085783747016L;
  private Object            list;
  private String            valueProperty;
  private String            labelProperty;
  private boolean           horizontalAlign  = false;
  private boolean           checked          = false;

  /*
   * Queste due propietà blankVal<NOME_PROPRITA> sono previste per ovviare ai
   * casi in qui valueProperty è vuota e si vuole visualizzare una checkbox dove
   * value e label sono appunto blankValValue e blankValLabel
   */
  private String            blankValValue;
  private String            blankValLabel;

  private List<String>      values;

  @Override
  protected void writeCustomTagElement(StringBuilder sb)
      throws SecurityException, IllegalArgumentException, NoSuchMethodException,
      IllegalAccessException, InvocationTargetException
  {
    values = new ArrayList<String>();
    setFormControl(false);
    processItems(sb);
  }

  private void processItems(StringBuilder sb)
      throws SecurityException, IllegalArgumentException, NoSuchMethodException,
      IllegalAccessException, InvocationTargetException
  {
    String valueMethod = "getId";
    String labelMethod = "getDescrizione";

    if (!GenericValidator.isBlankOrNull(valueProperty))
    {
      valueMethod = "get" + Character.toUpperCase(valueProperty.charAt(0))
          + valueProperty.substring(1);
    }
    if (!GenericValidator.isBlankOrNull(labelProperty))
    {
      labelMethod = "get" + Character.toUpperCase(labelProperty.charAt(0))
          + labelProperty.substring(1);
    }

    if (horizontalAlign)
      sb.append(" <div class=\"checkbox\"> ");

    if (list != null)
    {
      if (list instanceof Iterable)
      {
        Iterator<?> iterator = ((Iterable<?>) list).iterator();
        while (iterator.hasNext())
        {
          processCheckBox(sb, iterator.next(), valueMethod, labelMethod);
        }
      }
      else
      {
        if (list.getClass().isArray())
        {
          Object[] array = (Object[]) list;
          for (Object option : array)
          {
            processCheckBox(sb, option, valueMethod, labelMethod);
          }
        }
      }
    }

    if (horizontalAlign)
      sb.append(" </div> ");

  }

  private void processCheckBox(StringBuilder sb, Object obj, String valueMethod,
      String labelMethod) throws SecurityException, IllegalArgumentException,
      NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    String value = getProperty(obj, valueMethod);
    String label = getProperty(obj, labelMethod);
    writeCheckBox(sb, value, label);
  }

  public void writeCheckBox(StringBuilder sb, String value, String label)
  {
    if (values.contains(value))
      return;

    if (GenericValidator.isBlankOrNull(value))
    {
      if (!GenericValidator.isBlankOrNull(blankValValue))
      {
        writeCheckBox(sb, blankValValue, blankValLabel);
        return;
      }
    }

    values.add(value);
    if (!horizontalAlign)
      sb.append(" <div class=\"checkbox\"> ");

    sb.append(
        "<label style=\"margin-right:1em\" ><input type=\"checkbox\" value=\"")
        .append(StringEscapeUtils.escapeHtml4(value)).append("\"");
    writeDefaultAttributes(sb);

    if (checked)
      sb.append(" checked=\"checked\" ");

    sb.append(" />");

    if (label != null)
      sb.append(StringEscapeUtils.escapeHtml4(label));

    sb.append("</label>");

    if (!horizontalAlign)
      sb.append(" </div> ");
  }

  private String getProperty(Object option, String methodName)
      throws SecurityException, NoSuchMethodException, IllegalArgumentException,
      IllegalAccessException, InvocationTargetException
  {
    Method method = option.getClass().getMethod(methodName);
    Object value = method.invoke(option);
    return value == null ? "" : value.toString();
  }

  public Object getList()
  {
    return list;
  }

  public void setList(Object list)
  {
    this.list = list;
  }

  public boolean isHorizontalAlign()
  {
    return horizontalAlign;
  }

  public void setHorizontalAlign(boolean horizontalAlign)
  {
    this.horizontalAlign = horizontalAlign;
  }

  public String getValueProperty()
  {
    return valueProperty;
  }

  public void setValueProperty(String valueProperty)
  {
    this.valueProperty = valueProperty;
  }

  public String getLabelProperty()
  {
    return labelProperty;
  }

  public void setLabelProperty(String labelProperty)
  {
    this.labelProperty = labelProperty;
  }

  public String getBlankValValue()
  {
    return blankValValue;
  }

  public void setBlankValValue(String blankValValue)
  {
    this.blankValValue = blankValValue;
  }

  public String getBlankValLabel()
  {
    return blankValLabel;
  }

  public void setBlankValLabel(String blankValLabel)
  {
    this.blankValLabel = blankValLabel;
  }

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }

}
