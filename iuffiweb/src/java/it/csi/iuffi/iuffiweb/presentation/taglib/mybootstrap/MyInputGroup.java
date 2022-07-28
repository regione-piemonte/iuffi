package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public abstract class MyInputGroup extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = -5602058023177228254L;
  private String            leftAddOnHtml    = null;
  private String            rightAddOnHtml   = null;
  private String            groupCssClass    = null;
  private Boolean           isDate           = false;
  String INPUT_GROUP						 =" input-group";

  public MyInputGroup()
  {
    super();
  }

  public MyInputGroup(String additionalCssClass)
  {
    super(additionalCssClass);
  }
  
  public MyInputGroup(String additionalCssClass, String additionalStyleCss)
  {
    super(additionalCssClass, additionalStyleCss);
  } 

  public void addAddOn(boolean left, String html)
  {
    if (left)
    {
      leftAddOnHtml = html;
    }
    else
    {
      rightAddOnHtml = html;
    }
  }

  @Override
  protected void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    prepareInputGroup();
	boolean isInputGroup = leftAddOnHtml != null || rightAddOnHtml != null;
    if (isInputGroup)
    {
      sb.append("<div");
      if (groupCssClass != null)
      {
        if (errorMessage != null)
        {
          TAG_UTIL.addAttribute(sb, "class", IuffiUtils.STRING.concat(" ", CSS_ERROR_CLASSES + INPUT_GROUP, groupCssClass));
          addErrorAttributes(sb, errorMessage);
        }
        else
        {
          if (isDate)
          {
        	  TAG_UTIL.addAttribute(sb, "class", IuffiUtils.STRING.concat(" ", INPUT_GROUP, groupCssClass));	
          }
          else
          {
        	  TAG_UTIL.addAttribute(sb, "class", IuffiUtils.STRING.concat(" ", INPUT_GROUP, groupCssClass));
          }
        }
      }
      else
      {
        if (errorMessage != null)
        {
          TAG_UTIL.addAttribute(sb, "class", CSS_ERROR_CLASSES + INPUT_GROUP);
          addErrorAttributes(sb, errorMessage);
        }
        else
        {
          TAG_UTIL.addAttribute(sb, "class", INPUT_GROUP);
        }
      }
      sb.append(">");
      if (leftAddOnHtml != null)
      {
        sb.append(leftAddOnHtml);
      }
    }
    else
    {

      if (errorMessage != null)
      {
        sb.append("<div");
        TAG_UTIL.addAttribute(sb, "class", CSS_ERROR_CLASSES);
        addErrorAttributes(sb, errorMessage);
        sb.append(">");
      }
    }
    writeCustomTag(sb, errorMessage, isInputGroup);

    if (isDate)
    {

      if (rightAddOnHtml != null)
      {
        sb.append(rightAddOnHtml);
      }
    }

    if (isInputGroup)
    {
      if (rightAddOnHtml != null)
      {
        sb.append(rightAddOnHtml);
      }
      sb.append("</div>");
    }
    else
    {
      if (errorMessage != null)
      {
        sb.append("</div>");
      }
    }
    leftAddOnHtml = null;
    rightAddOnHtml = null;
  }

  protected void prepareInputGroup() throws Exception {
		
  }
  
  protected abstract void writeCustomTag(StringBuilder sb, String errorMessage,
      boolean wrappedInAGroup) throws Exception;

  public String getGroupCssClass()
  {
    return groupCssClass;
  }

  public void setGroupCssClass(String groupCssClass)
  {
    this.groupCssClass = groupCssClass;
  }

  public Boolean getIsDate()
  {
    return isDate;
  }

  public void setIsDate(Boolean isDate)
  {
    this.isDate = isDate;
  }
  
	public String getLeftAddOnHtml() {
		return leftAddOnHtml;
	}
	
	public void setLeftAddOnHtml(String leftAddOnHtml) {
		this.leftAddOnHtml = leftAddOnHtml;
	}
	
	public String getRightAddOnHtml() {
		return rightAddOnHtml;
	}
	
	public void setRightAddOnHtml(String rightAddOnHtml) {
		this.rightAddOnHtml = rightAddOnHtml;
	}
  
  
}
