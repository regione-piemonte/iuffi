package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.validator.GenericValidator;

public class MyInputAddOn extends BodyTagSupport
{
  /** serialVersionUID */
  private static final long serialVersionUID = -857481504758912081L;
  protected boolean         left;
  protected boolean         button;

  @Override
  public int doEndTag() throws JspException
  {
    String body = null;
    MyInputGroup ig = (MyInputGroup) findAncestorWithClass(this,
        MyInputGroup.class);
    if (ig != null && bodyContent != null)
    {
      body = bodyContent.getString();
      if (!GenericValidator.isBlankOrNull(body))
      {
        if (!button)
          body = getAddOnClass(body);
        else
          body = getAddOnButtonClass(body);

        ig.addAddOn(left, body);
      }
    }
    return super.doEndTag();
  }

  public static String getAddOnClass(String body)
  {
    return new StringBuilder("<span class=\"input-group-addon\">").append(body)
        .append("</span>").toString();
  }

  public static String getAddOnButtonClass(String body)
  {
    return new StringBuilder("<span class=\"input-group-btn\">").append(body)
        .append("</span>").toString();
  }

  public boolean isLeft()
  {
    return left;
  }

  public void setLeft(boolean left)
  {
    this.left = left;
  }

  public boolean isButton()
  {
    return button;
  }

  public void setButton(boolean button)
  {
    this.button = button;
  }
}
