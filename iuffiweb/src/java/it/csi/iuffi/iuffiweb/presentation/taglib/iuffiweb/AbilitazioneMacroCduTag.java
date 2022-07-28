package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class AbilitazioneMacroCduTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6582461845796172740L;
  private String            codiceMacroCdu;

  @Override
  public int doEndTag() throws JspException
  {
    if (IuffiUtils.PAPUASERV
        .isMacroCUAbilitato((UtenteAbilitazioni) pageContext.getSession()
            .getAttribute("utenteAbilitazioni"), codiceMacroCdu)
        && this.bodyContent != null)
    {
      try
      {
        this.pageContext.getOut().write(this.bodyContent.getString());
      }
      catch (IOException e)
      {
        throw new JspException(e);
      }
    }
    return super.doEndTag();
  }

  public String getCodiceMacroCdu()
  {
    return codiceMacroCdu;
  }

  public void setCodiceMacroCdu(String codiceMacroCdu)
  {
    this.codiceMacroCdu = codiceMacroCdu;
  }

}
