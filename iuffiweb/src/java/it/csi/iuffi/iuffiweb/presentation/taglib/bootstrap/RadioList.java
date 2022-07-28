package it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap;

import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RadioList extends SimpleBoostrapTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 83669507999616759L;
  protected List<?>         list;
  protected boolean         horizontal       = true;
  private String            valueProperty;
  private String            labelProperty;
  private boolean           radioFirst       = true;

  @Override
  protected void writeCustomTagElement(StringBuilder sb) throws Exception
  {
    sb.append("<div");
    setFormControl(false);
    writeDefaultAttributes(sb);
    sb.append(">");
    writeRadios(sb);
    sb.append("</div>");
  }

  @Override
  public String getCssClass()
  {
    return IuffiUtils.STRING.concat(" ", cssClass, "radio");
  }

  protected void writeRadios(StringBuilder sb) throws Exception
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
    int idx = 0;
    if (list != null)
    {
      for (Object obj : list)
      {
        writeRadio(sb, obj, labelMethod, valueMethod, ++idx);
      }
    }
  }

  private void writeRadio(StringBuilder sb, Object obj, String labelMethod,
      String valueMethod, int idx) throws Exception
  {
    Class<?> objClass = obj.getClass();
    String radioValue = IuffiUtils.STRING
        .nvl(objClass.getMethod(valueMethod).invoke(obj));
    String radioLabel = IuffiUtils.STRING
        .nvl(objClass.getMethod(labelMethod).invoke(obj));
    String radioID = id == null ? name : id;
    radioID = radioID + "_" + idx;
    sb.append("\n<label");
    if (horizontal)
    {
      addAttribute(sb, "class", "label-control");
    }
    else
    {
      addAttribute(sb, "class", "radio label-control");
    }
    sb.append(">");

    if (!radioFirst)
    {
      sb.append(radioLabel);
    }
    sb.append("\n<input type=\"radio\"");
    addAttribute(sb, "id", radioID);
    addAttribute(sb, "name", name);
    addAttribute(sb, "value", radioValue);
    if (radioValue.equals(value))
    {
      addAttribute(sb, "checked", "checked");
    }
    sb.append("/>");
    if (radioFirst)
    {
      sb.append(radioLabel);
    }
    sb.append("\n</label>");
  }

  public List<?> getList()
  {
    return list;
  }

  public void setList(List<?> list)
  {
    this.list = list;
  }

  @Override
  protected void writeNameAttribute(StringBuilder sb)
  {
    // Non voglio che scriva il name sul tag. Il tag più esterno è un DIV non il
    // RADIO.
  }

  public boolean isHorizontal()
  {
    return horizontal;
  }

  public void setHorizontal(boolean horizontal)
  {
    this.horizontal = horizontal;
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

  public boolean isRadioFirst()
  {
    return radioFirst;
  }

  public void setRadioFirst(boolean radioFirst)
  {
    this.radioFirst = radioFirst;
  }
}
