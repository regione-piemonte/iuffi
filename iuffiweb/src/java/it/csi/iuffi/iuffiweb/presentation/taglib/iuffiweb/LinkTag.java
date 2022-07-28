package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.ElencoCduDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class LinkTag extends BaseTag
{
  /** serialVersionUID */
  protected static final long serialVersionUID = -7620410490326047187L;
  public String               useCase;
  public String               title;
  public String               description;
  public boolean              forceVisualization;
  public String               href;
  public String               onClick;
  public Boolean              readWrite;

  public int doEndTag() throws JspException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) this.pageContext
        .getSession().getAttribute("utenteAbilitazioni");
    StringBuilder buffer = new StringBuilder();
    String onclickTxt = "";
    if (!GenericValidator.isBlankOrNull(onClick))
    {
      onclickTxt = " onClick=\"" + onClick + "\" ";
    }
    // Se il caso d'uso è di scrittura ma l'utente è di sola lettura allora esco
   // if (readWrite.booleanValue()
    //    && IuffiUtils.PAPUASERV.isUtenteReadOnly(utenteAbilitazioni))
     // return super.doEndTag();
    ElencoCduDTO cdu = IuffiUtils.APPLICATION.getCdu(useCase);
    //if (forceVisualization || (cdu != null && IuffiUtils.PAPUASERV
    //    .isMacroCUAbilitato(utenteAbilitazioni, cdu.getExtCodMacroCdu())))
   // {
      buffer.append(
          "<h4 class=\"accordion-heading\"><a href=\"#\" id=\"toggle_handle_"
              + useCase
              + "\" class=\"toggle_handle espandi\" title=\"visualizza/nascondi informazioni\"> "
              +
              " <span style=\"text-decoration: none; font-size:18px\" id=\"icona_espandi\" class=\"glyphicon glyphicon-info-sign icona_espandi_"
              + useCase + "\"></span>" +
              " <span class=\"hidden\">visualizza dettaglio</span>" +
              " </a>" +
              " <a " + onclickTxt + " href=\"" + this.href + "\">" + this.title
              + "</a>"
              + " <a " + onclickTxt + " href=\"" + this.href
              + "\" class=\"pull-right vaiasezione\">[Vai alla funzione]</a></h4>");
      buffer.append("<div class=\"titledetail toggle_target_" + useCase
          + "\" style=\"display: none;\"><p class=\"text-justify\">"
          + IuffiUtils.STRING.nvl(this.description).replace("\n", "<br />")
          + "</p></div>");
      try
      {
        this.pageContext.getOut().write(buffer.toString());
      }
      catch (IOException e)
      {
        e.printStackTrace();
        throw new JspException(e);
      }
  //  }
    return super.doEndTag();
  }

  public String getUseCase()
  {
    return useCase;
  }

  public void setUseCase(String useCase)
  {
    this.useCase = useCase;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getHref()
  {
    return href;
  }

  public void setHref(String href)
  {
    this.href = href;
  }

  public Boolean getReadWrite()
  {
    return readWrite;
  }

  public void setReadWrite(Boolean readWrite)
  {
    this.readWrite = readWrite;
  }

  public String getOnClick()
  {
    return onClick;
  }

  public void setOnClick(String onClick)
  {
    this.onClick = onClick;
  }

  public boolean isForceVisualization()
  {
    return forceVisualization;
  }

  public void setForceVisualization(boolean forceVisualization)
  {
    this.forceVisualization = forceVisualization;
  }

}