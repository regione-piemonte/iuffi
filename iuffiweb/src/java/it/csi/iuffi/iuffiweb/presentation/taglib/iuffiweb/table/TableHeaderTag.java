package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.table;

import javax.servlet.jsp.JspException;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.BaseTag;

/**
 * E' giusto un contenitore, non fa niente altro che
 * "contenere il codice html dell'header"
 * 
 * @author Stefano Einaudi (70399)
 */
public class TableHeaderTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6631325238199327178L;

  @Override
  public int doEndTag() throws JspException
  {
    String body = this.getBodyContent().getString();
    if (!GenericValidator.isBlankOrNull(body))
    {
      TableTag parent = (TableTag) findAncestorWithClass(this,
          TableTag.class);
      if (parent != null)
      {
        parent.setCustomHeader(body);
      }
    }
    return super.doEndTag();
  }
}