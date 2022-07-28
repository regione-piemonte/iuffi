package it.csi.iuffi.iuffiweb.presentation.taglib.bootstrap;

import javax.servlet.jsp.JspException;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.BaseTag;

/**
 * E' giusto un contenitore, non fa niente altro che
 * "contenere il codice html del title del Panel e inserirlo nel Panel che lo contiene"
 * 
 * @author Stefano Einaudi (70399)
 */
public class Title extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6631325238199327178L;

  @Override
  public int doEndTag() throws JspException
  {
    String body = this.getBodyContent().getString();
    if (!GenericValidator.isBlankOrNull(body))
    {
      Titled parent = (Titled) findAncestorWithClass(this,
          Titled.class);
      if (parent != null)
      {
        parent.setTitle(body);
      }
    }
    return super.doEndTag();
  }
}