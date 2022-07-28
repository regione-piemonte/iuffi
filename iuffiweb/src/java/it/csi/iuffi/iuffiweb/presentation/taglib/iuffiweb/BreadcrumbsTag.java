package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.AzioneDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;

public class BreadcrumbsTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4558965659815071438L;
  private String            cdu              = null;

  @Override
  public int doEndTag() throws JspException
  {
    ProcedimentoOggetto procedimentoOggetto = null;
    try
    {
      procedimentoOggetto = IuffiFactory.getProcedimentoOggetto(
          (HttpServletRequest) this.pageContext.getRequest());
    }
    catch (InternalUnexpectedException e)
    {
      throw new JspException(e);
    }
    String comeFromRicerca = (String) this.pageContext.getSession()
        .getAttribute("comeFromRicerca");
    JspWriter writer = this.pageContext.getOut();
    StringBuilder buffer = new StringBuilder();
    try
    {
      buffer.append("<div class=\"container-fluid\">"
          + "    <div class=\"row IUF_breadcrumb\">"
          // + " <div class=\"moduletable\">"
          + "        <ul class=\"breadcrumb\">"
          + "          <li><a href=\"../index.do\">Home</a><span class=\"divider\">/</span></li>");
      if (!GenericValidator.isBlankOrNull(comeFromRicerca)
          && "TRUE".equals(comeFromRicerca))
      {
        buffer.append(
            " <li><a href=\"../ricercaprocedimento/index.do\">Ricerca procedimento</a> <span class=\"divider\">/</span></li> ");
        buffer.append(
            " <li><a href=\"../ricercaprocedimento/restoreElencoProcedimenti.do\">Elenco procedimenti</a> <span class=\"divider\">/</span></li> ");
        if (procedimentoOggetto != null)
        {
          buffer.append(" <li><a href=\"../cuiuffi129/index_"
              + procedimentoOggetto.getIdProcedimento()
              + ".do\">Dettaglio Oggetto</a><span class=\"divider\">/</span></li>  ");
        }
      }
      else
        if (procedimentoOggetto != null)
        {
          buffer.append(
              " <li><a href=\"../nuovoprocedimento/elencobando.do\">Elenco bandi</a><span class=\"divider\">/</span></li>");
          buffer.append(
              " <li><a href=\"../nuovoprocedimento/dettaglioBando.do\">Dettaglio bando</a><span class=\"divider\">/</span></li>");

          Boolean noElenco = (Boolean) this.pageContext.getSession()
              .getAttribute(
                  IuffiConstants.GENERIC.SESSION_NO_ELENCO_AZIENDA);

          if (noElenco == null || !noElenco.booleanValue())
          {
            buffer.append(
                " <li><a href=\"../nuovoprocedimento/ricercaBandoMultipla.do\">Elenco aziende</a><span class=\"divider\">/</span></li>");
          }
        }

      if (procedimentoOggetto != null)
      {
        QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(cdu);
        if (quadro != null)
        {
          AzioneDTO azione = quadro.azioneConCU(cdu);
          Integer priorita = azione.getPriorita();
          boolean isDefault = priorita != null && priorita == 1;
          if (!isDefault)
          {
            buffer.append("<li><a href=\"../procedimento/quadro_")
                .append(quadro.getCodQuadro().toLowerCase()).append(".do\">");
            buffer.append(quadro.getDescQuadro());
            buffer.append("</a>");
            buffer.append("<span class=\"divider\">/</span></li>");
            buffer.append("<li class=\"active\">")
                .append(WordUtils.capitalize(azione.getLabelAzione()))
                .append("</li>");
          }
          else
          {
            buffer.append("<li class=\"active\">");
            buffer.append(quadro.getDescQuadro());
          }
          buffer.append("<li>");
        }
      }
      buffer.append("        </ul>"
          + "      </div>"
          // + " </div>"
          + "  </div>");
      writer.write(buffer.toString());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return super.doEndTag();
  }

  public String getCdu()
  {
    return cdu;
  }

  public void setCdu(String cdu)
  {
    this.cdu = cdu;
  }

}
