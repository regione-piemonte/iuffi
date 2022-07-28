
package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.OggettoIconaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class TestataTag extends BaseTag
{
  /** serialVersionUID */
  protected static final long serialVersionUID = 4558965659815071438L;
  protected String            cu;
  protected boolean           onlyCompanyData  = false;
  protected boolean           hideTabBar       = false;
  protected boolean           showIter         = false;
  protected boolean           hideOperazioni   = false;

  @Override
  public int doEndTag() throws JspException
  {
    HttpSession session = this.pageContext.getSession();
    Procedimento procedimento = null;
    ProcedimentoOggetto procedimentoOggetto = null;
    final HttpServletRequest request = (HttpServletRequest) this.pageContext
        .getRequest();
    try
    {
      procedimentoOggetto = IuffiFactory.getProcedimentoOggetto(request);
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
    try
    {
      procedimento = IuffiFactory.getProcedimento(request);
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
    TestataProcedimento testataProcedimento = (TestataProcedimento) session
        .getAttribute(TestataProcedimento.SESSION_NAME);
    StringBuilder sb = new StringBuilder();
    try
    {
      writeHeader(sb, testataProcedimento, procedimento, procedimentoOggetto);
    }
    catch (InternalUnexpectedException e1)
    {
      throw new JspException();
    }
    // writeDatiOggetto(sb, procedimentoOggetto);
    if (!onlyCompanyData)
    {
      writePanelStampe(sb);
      if (!hideTabBar)
      {
        writeTabBar(session, sb, procedimentoOggetto);
      }
    }
    try
    {
      if (sb.length() > 0)
      {
        this.pageContext.getOut().write(sb.toString());
      }
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
    return super.doEndTag();
  }

  protected void writeDatiOggetto(StringBuilder sb,
      ProcedimentoOggetto procedimentoOggetto)
  {
    sb.append(" <div class=\"container-fluid\">");
    sb.append(" <table class=\"table table-bordered table-striped\">");
    sb.append("<tr>");
    sb.append(
        "<th style=\"background-color: #428BCA !important; color:white;\">Oggetto/Istanza</th>");
    sb.append(
        "<th style=\"background-color: #428BCA !important; color:white;\">Codice domanda</th>");
    sb.append(
        "<th style=\"background-color: #428BCA !important; color:white;\">Data inizio</th>");
    sb.append(
        "<th style=\"background-color: #428BCA !important; color:white;\">Data fine</th>");
    sb.append(
        "<th style=\"background-color: #428BCA !important; color:white;\">Esito</th>");
    sb.append(
        "<th style=\"background-color: #428BCA !important; color:white;\">Stato</th>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td>").append(escapeHtml(procedimentoOggetto.getDescrizione()))
        .append("</td>");
    sb.append("<td>")
        .append(
            IuffiUtils.STRING.nvl(procedimentoOggetto.getIdentificativo()))
        .append("</td>");
    sb.append("<td>")
        .append(IuffiUtils.DATE
            .formatDateTime(procedimentoOggetto.getDataInizio()))
        .append("</td>");
    sb.append("<td>").append(
        IuffiUtils.DATE.formatDateTime(procedimentoOggetto.getDataFine()))
        .append("</td>");
    sb.append("<td>")
        .append(escapeHtml(
            IuffiUtils.STRING.nvl(procedimentoOggetto.getDescEsito())))
        .append("</td>");
    sb.append("<td>").append(escapeHtml(procedimentoOggetto.getDescStato()))
        .append("</td>");
    sb.append("</tr>");
    sb.append("</table>").append("</div>");
  }

  protected void writeHeader(StringBuilder sb,
      TestataProcedimento testataProcedimento, Procedimento procedimento,
      ProcedimentoOggetto procedimentoOggetto)
      throws JspException, InternalUnexpectedException
  {
    if (testataProcedimento != null)
    {
      Date dataCessazione = testataProcedimento.getDataCessazioneAzienda();
      if (dataCessazione != null)
      {
        sb.append(
            "<div class=\"container-fluid\"> <div class=\"alert alert-danger\"><strong>Azienda Cessata in data ")
            .append(IuffiUtils.DATE.formatDate(dataCessazione));

        if (!GenericValidator
            .isBlankOrNull(testataProcedimento.getMotivoCessazione()))
        {
          sb.append("<br>Motivazione: " + IuffiUtils.STRING
              .safeHTMLText(testataProcedimento.getMotivoCessazione()));
        }
        sb.append(" </strong></div></div>");
      }

      sb.append(" <div class=\"container-fluid\" id=\"testata_di_pagina\">"
          + "\n<div class=\"panel-group\" id=\"accordion\">"
          + "\n<div class=\"panel panel-primary\">"
          + "\n<div class=\"panel-heading\">");

      appendIconExpandi(sb);
      boolean documentale = false;
      if (!onlyCompanyData)
      {
        appendIcons(sb, procedimentoOggetto, testataProcedimento);
        appendIconStampe(sb);
        appendIconIterProcedimento(sb);
        appendIconCheckList(procedimento, sb);
        appendIconDocumentale(sb, procedimento, testataProcedimento);
        documentale = true;
      }
      else
        if (showIter)
        {
          appendIconIterProcedimento(sb);
          appendIconCheckList(procedimento, sb);
        }

      if (!hideOperazioni)
      {
        appendDropDownOperazioni(sb, procedimento, procedimentoOggetto);
        if (!documentale)
        {
          appendIconDocumentale(sb, procedimento, testataProcedimento);
          documentale = true;
        }
      }

      sb.append("\n<div id='header_info'>");

      sb.append(testataProcedimento.getCuaa())
          .append(" - ")
          .append(testataProcedimento.getDenominazioneAzienda());

      sb.append("<br />");

      if (testataProcedimento.getAnnoCampagna() != null)
      {
        sb.append(testataProcedimento.getAnnoCampagna())
            .append(" - ");
      }

      sb.append(testataProcedimento.getDenominazioneBando());
      sb.append("<br />");

      if (!GenericValidator.isBlankOrNull(procedimento.getIdentificativo()))
      {
        sb.append("Procedimento numero: " + procedimento.getIdentificativo());
        sb.append(" - ");
      }
      sb.append(procedimento.getDescrStato());
      sb.append(" dal ").append(IuffiUtils.DATE
          .formatDateTime(procedimento.getDataUltimoAggiornamento()));

      if (IuffiUtils.PAPUASERV.isMacroCUAbilitato(
          (UtenteAbilitazioni) pageContext.getSession()
              .getAttribute("utenteAbilitazioni"),
          IuffiConstants.MACROCDU.ESTRAZIONE_CAMPIONE))
      {
        String estratta = IuffiFactory
            .getMotivoEsclusioneProcedimento(procedimento.getIdProcedimento());
        if (!GenericValidator.isBlankOrNull(estratta))
        {
          sb.append(
              " -  <span style=\"color:yellow; font-size: 1.2em;\"> ESTRATTO A CAMPIONE (motivo: "
                  + estratta.toUpperCase() + ")</span>");
        }
        else
        {
          estratta = IuffiFactory.getMotivoEsclusioneExPostProcedimento(
              procedimento.getIdProcedimento());
          if (!GenericValidator.isBlankOrNull(estratta))
          {
            sb.append(
                " -  <span style=\"color:yellow; font-size: 1.2em;\"> ESTRATTO A CAMPIONE EX POST (motivo: "
                    + estratta.toUpperCase() + ")</span>");
          }
        }
      }

      if (!onlyCompanyData)
      {
        sb.append("<br />")
            .append(escapeHtml(procedimentoOggetto.getDescrizione()));
        sb.append(" - ").append(escapeHtml(procedimentoOggetto.getDescStato()));
        sb.append(" dal ").append(IuffiUtils.DATE
            .formatDateTime(procedimentoOggetto.getDataInizioLastIter()));

        if (procedimentoOggetto != null
            && procedimentoOggetto.getIdentificativo() != null)
          sb.append(
              " - Codice domanda : " + procedimentoOggetto.getIdentificativo());

        if (IuffiUtils.PAPUASERV.isMacroCUAbilitato(
            (UtenteAbilitazioni) pageContext.getSession()
                .getAttribute("utenteAbilitazioni"),
            IuffiConstants.MACROCDU.ESTRAZIONE_CAMPIONE))
        {
          String estratta = IuffiFactory
              .getMotivoEsclusioneProcedimentoOggetto(
                  procedimentoOggetto.getIdProcedimentoOggetto());
          if (!GenericValidator.isBlankOrNull(estratta))
          {
            sb.append(
                " -  <span style=\"color:yellow; font-size: 1.2em;\"> ESTRATTO A CAMPIONE (motivo: "
                    + estratta.toUpperCase() + ")</span>");
          }
          else
          {
            estratta = IuffiFactory.getMotivoEsclusioneExPostProcedimento(
                procedimento.getIdProcedimento());
            if (!GenericValidator.isBlankOrNull(estratta))
            {
              sb.append(
                  " -  <span style=\"color:yellow; font-size: 1.2em;\"> ESTRATTO A CAMPIONE EX POST (motivo: "
                      + estratta.toUpperCase() + ")</span>");
            }
          }
        }

      }
      else
      {
        sb.append("<br />");
      }

      sb.append("          </div>"
          + "\n</div>");

      sb.append(
          "\n<div style=\"\" id=\"testata\" class=\"panel-collapse collapse\">"
              + "\n<div class=\"panel-body\">");
      sb.append("\n<h4>Misure:</h4>");
      for (String misura : testataProcedimento.getMisure())
      {
        sb.append("<span>").append(misura).append("</span><br />");
      }
      String organismoDelegato = testataProcedimento.getDescAmmCompetenza();
      if (organismoDelegato != null)
      {
        sb.append("\n<h4>Organismo delegato:</h4>");
        if (testataProcedimento.getDenominazioneAmmCompetenzaUno() != null
            && testataProcedimento.getDenominazioneAmmCompetenzaUno().trim()
                .length() > 0)
          sb.append(organismoDelegato + " - "
              + testataProcedimento.getDenominazioneAmmCompetenzaUno());
        else
          sb.append(organismoDelegato);
      }
      String ufficioZona = testataProcedimento.getDescUfficioZona();
      if (ufficioZona != null && ufficioZona.compareTo("") != 0
          && ufficioZona.compareTo(" ") != 0)
      {
        sb.append("\n<h4>Ufficio di zona:</h4>");
        sb.append(ufficioZona);
      }
      String tecnico = testataProcedimento.getDescFunzionarioIstruttore();
      if (tecnico != null && tecnico.compareTo("") != 0
          && tecnico.compareTo(" ") != 0)
      {
        sb.append("\n<h4>Funzionario istruttore:</h4>");
        sb.append(tecnico);
      }

      sb.append("</div>");

      if (!onlyCompanyData)
      {
        writeDatiOggetto(sb, procedimentoOggetto);
      }

      sb.append("\n</div>"
          + "\n</div>"
          + "\n</div>"
          + "\n</div>");
      sb.append(
          " <div class=\"modal fade\" id=\"iterModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"iterModalLabel\" aria-hidden=\"true\">  		\n"
              + "   <div class=\"modal-dialog\" style=\"width: 930px\">                                                                                                         			\n"
              + "     <div class=\"modal-content\">                                                                                                      			\n"
              + "       <div class=\"modal-header\">                                                                                                     			\n"
              + "         <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button> 	\n"
              + "         <h4 class=\"modal-title\" id=\"myModalLabel\">Iter procedimento</h4>                                                               		\n"
              + "       </div>                                                                                                                         				\n"
              + "       <div class=\"modal-body\">                                                                                                     				\n"
              + "         	                                                                                                                       					\n"
              + "       </div>                                                                                                                         				\n"
              + "       <div class=\"modal-footer\">                                                                                                   				\n"
              + "         <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Chiudi</button>                                      				\n"
              + "       </div>                                                                                                                         				\n"
              + "     </div>                                                                                                                           				\n"
              + "   </div>                                                                                                                             				\n"
              + " </div>                                                                                                                               				\n");

      sb.append(
          " <div class=\"modal fade\" id=\"checklistModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"checklistModalLabel\" aria-hidden=\"true\">  		\n"
              + "   <div class=\"modal-dialog\" style=\"width: 930px\">                                                                                                         			\n"
              + "     <div class=\"modal-content\">                                                                                                      			\n"
              + "       <div class=\"modal-header\">                                                                                                     			\n"
              + "         <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button> 	\n"
              + "         <h4 class=\"modal-title\" id=\"myModalLabel\">Check list procedimento</h4>                                                               		\n"
              + "       </div>                                                                                                                         				\n"
              + "       <div class=\"modal-body\">                                                                                                     				\n"
              + "         	                                                                                                                       					\n"
              + "       </div>                                                                                                                         				\n"
              + "       <div class=\"modal-footer\">                                                                                                   				\n"
              + "         <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Chiudi</button>                                      				\n"
              + "       </div>                                                                                                                         				\n"
              + "     </div>                                                                                                                           				\n"
              + "   </div>                                                                                                                             				\n"
              + " </div>                                                                                                                               				\n");
    }

    checkNotifiche(sb, procedimentoOggetto, (UtenteAbilitazioni) pageContext
        .getSession().getAttribute("utenteAbilitazioni"));

  }

  private void checkNotifiche(StringBuilder sb,
      ProcedimentoOggetto procedimentoOggetto,
      UtenteAbilitazioni utenteAbilitazioni) throws InternalUnexpectedException
  {
    checkNotificheBloccanti(sb, procedimentoOggetto);
    if (!utenteAbilitazioni.isAziendaAgricola())
    {
      checkNotificheWarning(sb);
    }
    checkNotificheGravi(sb);

  }

  private void checkNotificheGravi(StringBuilder sb)
      throws InternalUnexpectedException
  {

    Boolean notificheGravi = (Boolean) this.pageContext.getSession()
        .getAttribute("notificheGravi");
    if (notificheGravi != null && notificheGravi.booleanValue())
      sb.append(
          "<div class=\"container-fluid\"><div id=\"error-box\" class=\"alert alert-danger alert-error\"><a href=\"#\" onclick=\"return false\" class=\"close\" data-dismiss=\"alert\">×</a><strong>Sono presenti delle notifiche gravi. Non &egrave; permessa nessuna operazione di modifica. Selezionare il pulsante in alto a destra Operazioni / Notifiche per visualizzare il dettaglio.</strong></div></div>");
  }

  private void checkNotificheBloccanti(StringBuilder sb,
      ProcedimentoOggetto procedimentoOggetto)
      throws InternalUnexpectedException
  {
    if (procedimentoOggetto != null)
    {
      Boolean notificheBloccanti = (Boolean) this.pageContext.getSession()
          .getAttribute("notificheBloccanti");
      if (notificheBloccanti != null && notificheBloccanti.booleanValue())
        if (procedimentoOggetto.getFlagIstanza() != null
            && procedimentoOggetto.getFlagIstanza().compareTo("N") == 0)
          sb.append(
              "<div class=\"container-fluid\"><div id=\"error-box\" class=\"alert alert-danger alert-error\"><a href=\"#\" onclick=\"return false\" class=\"close\" data-dismiss=\"alert\">×</a><strong>Sono presenti delle notifiche bloccanti. Non &egrave; permessa nessuna operazione di modifica sulle istruttorie. Selezionare il pulsante in alto a destra Operazioni / Notifiche per visualizzare il dettaglio.</strong></div></div>");
    }
  }

  private void checkNotificheWarning(StringBuilder sb)
      throws InternalUnexpectedException
  {

    Boolean notificheBloccanti = (Boolean) this.pageContext.getSession()
        .getAttribute("notificheWarning");
    if (notificheBloccanti != null && notificheBloccanti.booleanValue())
      sb.append(
          "<div class=\"container-fluid\"><div class=\"alert alert-warning\"><a href=\"#\" onclick=\"return false\" class=\"close\" data-dismiss=\"alert\">×</a><strong>Sono presenti delle notifiche warning. Selezionare il pulsante in alto a destra Operazioni / Notifiche per visualizzare il dettaglio.</strong></div></div>");
  }

  private void appendDropDownOperazioni(StringBuilder sb,
      Procedimento procedimento, ProcedimentoOggetto procedimentoOggetto)
  {

    sb.append(
        "\n<div class=\"pull-right dropdown\" style=\"padding-left:4px;\">" +
            "<button class=\"btn btn-default dropdown-toggle\"  style=\"color:black;\" type=\"button\" data-toggle=\"dropdown\">Operazioni&nbsp;"
            +
            "<span class=\"caret\"></span></button>" +
            "<ul class=\"dropdown-menu operazioniUl\">");

    if (IuffiConstants.FLAGS.SI
        .equals(procedimento.getFlagRendicontazioneDocSpesa()))
    {
      sb.append(
          "<li><a href=\"../cuiuffi263l/index.do\">Documenti Spesa</a></li>");
    }

    sb.append("<li><a href=\"../cuiuffi110/index_"
        + procedimento.getIdProcedimento() + ".do\">Notifiche</a></li>" +
        "<li><a onClick=\"openPageInPopup('../cuiuffi251/index.do','dlgModificaOD','Trasferimento pratica/ufficio di zona/funzionario istruttore', 'modal-large');return false;\" href=\"../cuiuffi251/.do\">Trasferimento Pratica/Assegnazione Istruttore  <span style=\"padding-left:4px\"></span></a></li>");
    sb.append(
        "<li><a href=\"../cuiuffi254l/index.do\">Prospetto economico</a></li>");
    sb.append("</ul> </div>");

  }

  protected void appendIconExpandi(StringBuilder sb)
  {
    sb.append("\n<a data-toggle=\"collapse\""
        + "\ndata-parent=\"#accordion\" href=\"#testata\" onclick=\"return false\" style=\"float:left;color:white;font-size:32px\"><i id=\"header_icon_collapse\" class=\"glyphicon glyphicon-collapse-down\"></i></a>");
  }

  protected void appendIconDocumentale(StringBuilder sb,
      Procedimento procedimentoOggetto, TestataProcedimento testataProcedimento)
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) this.pageContext
        .getSession().getAttribute("utenteAbilitazioni");
    String portal = (String) pageContext.getSession()
        .getAttribute(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL);
    sb.append(IuffiUtils.AGRIWELL.getHtmlIconaAgriwellWeb(utenteAbilitazioni,
        testataProcedimento.getIdAzienda(),
        procedimentoOggetto.getIdProcedimento(), portal,
        "pull-right ico24 ico_agriwell"));
  }

  protected void appendIconIterProcedimento(StringBuilder sb)
  {
    sb.append(
        "\n<a class=\"pull-right ico_spaced ico24 ico_document\" data-toggle=\"modal\" data-target=\"#iterModal\" "
            + "\n  href=\"#testata\" title=\"Mostra Iter procedimento\"></a>");
  }

  protected void appendIconCheckList(Procedimento procedimento,
      StringBuilder sb)
  {
    if (IuffiConstants.TIPO_BANDO.INVESTIMENTO
        .equals(procedimento.getCodiceTipoBando()))
    {
      sb.append(
          "\n<a class=\"pull-right ico_spaced ico24 ico_checklist\" data-toggle=\"modal\" data-target=\"#checklistModal\" "
              + "\n  href=\"#testata\" title=\"Mostra Check List\"></a>");
    }
  }

  protected void appendIconStampe(StringBuilder sb)
  {
    sb.append(
        "\n<a class=\"pull-right ico_spaced ico24 ico_print\" data-toggle=\"collapse\" data-parent=\"#pannello_stampe\" href=\"#pannello_stampe\" onclick=\"return false;\"></a>");
  }

  protected void appendIcons(StringBuilder sb,
      ProcedimentoOggetto procedimentoOggetto,
      TestataProcedimento testataProcedimento)
  {
    @SuppressWarnings("unchecked")
    Map<String, OggettoIconaDTO> iconeTestata = (Map<String, OggettoIconaDTO>) this.pageContext
        .getSession()
        .getAttribute("iconeTestata");
    if (iconeTestata != null)
    {
      OggettoIconaDTO oggettoIconaDTO = null;
      if (procedimentoOggetto.getDataFine() == null)
      {
        oggettoIconaDTO = iconeTestata
            .get(IuffiConstants.USECASE.CHIUSURA_OGGETTO.ISTANZA);
        if (oggettoIconaDTO == null)
        {
          oggettoIconaDTO = iconeTestata
              .get(IuffiConstants.USECASE.CHIUSURA_OGGETTO.ISTRUTTORIA);
        }
        if (oggettoIconaDTO != null)
        {
          appendIconChiudiOggetto(sb, oggettoIconaDTO);
        }
      }
      else
      {
        final boolean isEsitoOggettoNonRiapribile = IuffiUtils.VALIDATION
            .isEsitoOggettoNonRiapribile(procedimentoOggetto.getCodiceEsito());

        if (isEsitoOggettoNonRiapribile)
        {
          oggettoIconaDTO = iconeTestata
              .get(IuffiConstants.USECASE.CHIUSURA_OGGETTO_DEFINITIVA.ISTANZA);
          String errorMessage = "l'istanza non è più apribile perchè già trasmessa";
          if (oggettoIconaDTO == null)
          {
            errorMessage = "l'oggetto non è più apribile perchè già approvato ";
            oggettoIconaDTO = iconeTestata.get(
                IuffiConstants.USECASE.CHIUSURA_OGGETTO_DEFINITIVA.ISTRUTTORIA);
          }
          if (oggettoIconaDTO != null)
          {
            appendIconOggettoNonRiapribile(sb, oggettoIconaDTO, errorMessage);
          }
        }
        else
        {
          oggettoIconaDTO = iconeTestata
              .get(IuffiConstants.USECASE.RIAPERTURA_OGGETTO.ISTANZA);
          if (oggettoIconaDTO == null)
          {
            oggettoIconaDTO = iconeTestata
                .get(IuffiConstants.USECASE.RIAPERTURA_OGGETTO.ISTRUTTORIA);
          }
          if (oggettoIconaDTO != null)
          {
            appendIconRiapriOggetto(sb, oggettoIconaDTO);
          }
        }
      }

      oggettoIconaDTO = iconeTestata
          .get(IuffiConstants.USECASE.TRASMISSIONE_ISTANZA);
      if (oggettoIconaDTO != null
          && procedimentoOggetto.getDataFine() != null
          && procedimentoOggetto
              .getIdEsito() == IuffiConstants.PROCEDIMENTO_OGGETTO.ESITO.POSITIVO
          && procedimentoOggetto.getIdStatoOggetto()
              .longValue() < (new Long("10")).longValue())
      {
        appendIconTrasmettiIstanza(sb, oggettoIconaDTO);
      }

      oggettoIconaDTO = iconeTestata
          .get(IuffiConstants.USECASE.APPROVAZIONE_ISTRUTTORIA);
      if (oggettoIconaDTO != null
          && procedimentoOggetto.getDataFine() != null
          && (procedimentoOggetto.getIdEsito() != null)
          &&
          (procedimentoOggetto.getIdStatoOggetto().longValue() == 51L ||
              (procedimentoOggetto.getIdStatoOggetto().longValue() > 10L
                  &&
                  procedimentoOggetto.getIdStatoOggetto().longValue() < 17L)))
      {
        appendIconApprovazioneIstanza(sb, oggettoIconaDTO);
      }
    }
  }

  protected void appendIconOggettoNonRiapribile(StringBuilder sb,
      OggettoIconaDTO oggettoIconaDTO, String errorMessage)
  {
    if (errorMessage != null)
    {
      errorMessage = errorMessage.replace("'", "&#39");
    }
    sb.append("\n<a class=\"pull-right ico_spaced ")
        .append(oggettoIconaDTO.getIcona().getNomeIcona()).append(
            "\" href=\"#\" onclick=\"showMessageBox('Errore','")
        .append(escapeHtml(errorMessage)).append("')\"");
    String tooltip = oggettoIconaDTO.getIcona().getTooltip();
    if (!GenericValidator.isBlankOrNull(tooltip))
    {
      sb.append(" title=\"").append(escapeHtml(tooltip)).append("\"");
    }
    sb.append("></a>");
  }

  protected void appendIconChiudiOggetto(StringBuilder sb,
      OggettoIconaDTO oggettoIconaDTO)
  {
    sb.append("\n<a class=\"pull-right ico_spaced ")
        .append(oggettoIconaDTO.getIcona().getNomeIcona())
        .append("\" href=\"../" + oggettoIconaDTO.getCdu().getCodiceCdu()
            .replace("-", "").toLowerCase() + "/index.do\"  ");
    String tooltip = oggettoIconaDTO.getIcona().getTooltip();
    if (!GenericValidator.isBlankOrNull(tooltip))
    {
      sb.append(" title=\"").append(escapeHtml(tooltip)).append("\"");
    }
    sb.append("></a>");
  }

  protected void appendIconRiapriOggetto(StringBuilder sb,
      OggettoIconaDTO oggettoIconaDTO)
  {
    sb.append("\n<a class=\"pull-right ico_spaced ")
        .append(oggettoIconaDTO.getIcona().getNomeIcona())
        .append("\" href=\"../" + oggettoIconaDTO.getCdu().getCodiceCdu()
            .replace("-", "").toLowerCase() + "/index.do\"");
    String tooltip = oggettoIconaDTO.getIcona().getTooltip();
    if (!GenericValidator.isBlankOrNull(tooltip))
    {
      sb.append(" title=\"").append(escapeHtml(tooltip)).append("\"");
    }
    sb.append("></a>");
  }

  protected void appendIconTrasmettiIstanza(StringBuilder sb,
      OggettoIconaDTO oggettoIconaDTO)
  {
    sb.append("\n<a class=\"pull-right ico_spaced ")
        .append(oggettoIconaDTO.getIcona().getNomeIcona())
        .append(
            "\" href=\"../cuiuffi140/index.do\" onclick=\"openPageInPopup('../cuiuffi140/popupindex.do', 'dlgTrasmetti', 'Trasmissione', 'modal-large', true);return false;\"");
    String tooltip = oggettoIconaDTO.getIcona().getTooltip();
    if (!GenericValidator.isBlankOrNull(tooltip))
    {
      sb.append(" title=\"").append(escapeHtml(tooltip)).append("\"");
    }
    sb.append("></a>");
  }

  protected void appendIconApprovazioneIstanza(StringBuilder sb,
      OggettoIconaDTO oggettoIconaDTO)
  {
    sb.append("\n<a class=\"pull-right ico_spaced ")
        .append(oggettoIconaDTO.getIcona().getNomeIcona())
        .append(
            "\" href=\"../cuiuffi140/index.do\" onclick=\"openPageInPopup('../cuiuffi232/popupindex.do', 'dlgApprova', 'Approvazione', 'modal-large', true);return false;\"");
    String tooltip = oggettoIconaDTO.getIcona().getTooltip();
    if (!GenericValidator.isBlankOrNull(tooltip))
    {
      sb.append(" title=\"").append(escapeHtml(tooltip)).append("\"");
    }
    sb.append("></a>");
  }

  protected void writePanelStampe(StringBuilder sb)
  {
    sb.append(
        "<div id=\"pannello_stampe\" class=\"container-fluid panel-group panel-collapse collapse\">");
    sb.append("<div class=\"panel panel-primary\" id=\"panel_elenco_stampe\">"
        + "\n<div class=\"panel-heading\">"
        + "\n  <h4 class=\"panel-title\">Stampe</h4>"
        + "\n</div>"
        + "\n<div class=\"panel-body\">"
        + "\n</div>"
        + "\n</div>");
    sb.append("</div>");
  }

  protected void writeTabBar(HttpSession session, StringBuilder sb,
      ProcedimentoOggetto procedimentoOggetto) throws JspException
  {
    boolean showHelp = false;
    if (procedimentoOggetto != null)
    {
      List<QuadroOggettoDTO> quadri = procedimentoOggetto.getQuadri();
      if (quadri != null)
      {
        // sb.append("<style>.nav-tabs>li{margin-bottom: -2px !important;}
        // .nav-tabs { border-bottom: 3px solid #428BCA } .nav-tabs li a
        // {color:#000000; border-color:#428BCA !important; height:56px;
        // background-color:#DDDDDD} .nav-tabs .active a {height:56px;
        // color:#000000!important; border:3px solid !important;
        // margin-bottom:-2px; border-color:#428BCA !important;
        // border-bottom:4px solid #FFF !important; font-weight:bold!important;
        // background-color:#FFF !important; }</style>");
        sb.append(" <div class=\"container-fluid\">");
        sb.append("<ul class=\"nav nav-tabs no-border-bottom\">");
        QuadroOggettoDTO quadroAzioneCorrente = procedimentoOggetto
            .findQuadroByCU(cu);
        String codQuadroCorrente = null;
        if (quadroAzioneCorrente != null)
        {
          codQuadroCorrente = quadroAzioneCorrente.getCodQuadro();
        }

        // Gestione Help
        /*
         * for (QuadroOggettoDTO quadro : quadri) { if (codQuadroCorrente !=
         * null && codQuadroCorrente.equals(quadro.getCodQuadro())) {
         * ElencoCduDTO dettagliCdu = IuffiUtils.APPLICATION.getCdu(cu);
         * if(IuffiConstants.FLAGS.SI.equals(dettagliCdu.getFlagHelp())) {
         * showHelp = true;
         * 
         * 
         * sb.append(
         * "<li role=\"presentation\" style=\"border-bottom:1px solid #337ab7\">"
         * ); sb.append("<a href=\"#\" onClick=\"toggleHelp('"+cu+
         * "');return false;\"  style=\"background-color:#FFFFFF;border:0px\" class=\"toggle_help\" title=\"Visualizza Help\">"
         * ); sb.append(
         * "<span style=\"text-decoration: none; font-size:18px\" id=\"icona_help\" class=\"icon icon-info\"></span>"
         * ); sb.append("</a></li>"); } } }
         */
        showHelp = true;
        sb.append(
            "<li role=\"presentation\" style=\"border-bottom:1px solid #337ab7\">");
        sb.append("<a href=\"#\" onClick=\"toggleHelp('" + cu
            + "');return false;\"  style=\"background-color:#FFFFFF;border:0px\" class=\"toggle_help\" title=\"Visualizza Help\">");
        sb.append(
            "<span style=\"text-decoration: none; font-size:18px\" id=\"icona_help\" class=\"icon icon-info\"></span>");
        sb.append("</a></li>");

        for (QuadroOggettoDTO quadro : quadri)
        {
          sb.append("<li role=\"presentation\"");
          if (codQuadroCorrente != null
              && codQuadroCorrente.equals(quadro.getCodQuadro()))
          {
            sb.append(" class=\"active\"");
          }
          sb.append("><a href=\"/")
              .append(IuffiConstants.IUFFIWEB.WEB_CONTEXT)
              .append("/procedimento/quadro_")
              .append(quadro.getCodQuadro().toLowerCase())
              .append(".do\">");

          
          if(quadro.getDescQuadro().equalsIgnoreCase("Riepilogo Danni Colture"))
          {
        	  sb.append("Riepilogo<br/>Danni Colture");
          }
          else if(quadro.getDescQuadro().equalsIgnoreCase("Date fine Lavori"))
          {
        	  sb.append("Date fine<br/>Lavori");
          }
          else if(quadro.getDescQuadro().equalsIgnoreCase("Interventi Consorzi Irrigui"))
          {
        	  sb.append("Interventi<br/>Consorzi Irrigui");
          }
          else if(quadro.getDescQuadro().equalsIgnoreCase("Controlli SIGC (informatici)"))
          {
        	  sb.append("Controlli SIGC<br/>(informatici)");
          }
          else if(quadro.getDescQuadro().equalsIgnoreCase("Dati Identificativi del danno"))
          {
        	  sb.append("Dati Identificativi<br/>del danno");
          }
          else
          {
        	  sb.append(escapeHtml(quadro.getDescQuadro()).replaceAll("[\\s]+", "<br />"));
          }
          sb.append("</a></li>");
        }
        sb.append("</ul></div>");

        if (showHelp)
        {
          sb.append(
        		  BaseTag.HELP_CONTAINER);
          String helpSession = (String) session.getAttribute(
              IuffiConstants.GENERIC.SESSION_VAR_HELP_IS_ACTIVE);
          String helpDefault = (String) session
              .getAttribute(IuffiConstants.PARAMETRO.HELP_DEFAULT_OPEN);

          if (helpSession != null
              && IuffiConstants.FLAGS.SI.equals(helpSession))
          {
            sb.append(
                "<script>document.addEventListener('DOMContentLoaded', function() {toggleHelp('"
                    + cu + "');}, false);</script>");
          }
          else
            if (helpSession == null && "S".equals(helpDefault))
            {
              sb.append(
                  "<script>document.addEventListener('DOMContentLoaded', function() {toggleHelp('"
                      + cu + "');}, false);</script>");
            }
        }

      }
    }
  }

  public String getCu()
  {
    return cu;
  }

  public void setCu(String cu)
  {
    this.cu = cu;
  }

  public boolean isOnlyCompanyData()
  {
    return onlyCompanyData;
  }

  public void setOnlyCompanyData(boolean onlyCompanyData)
  {
    this.onlyCompanyData = onlyCompanyData;
  }

  public boolean isHideTabBar()
  {
    return hideTabBar;
  }

  public void setHideTabBar(boolean hideTabBar)
  {
    this.hideTabBar = hideTabBar;
  }

  public boolean isShowIter()
  {
    return showIter;
  }

  public void setShowIter(boolean showIter)
  {
    this.showIter = showIter;
  }

  public boolean isHideOperazioni()
  {
    return hideOperazioni;
  }

  public void setHideOperazioni(boolean hideOperazioni)
  {
    this.hideOperazioni = hideOperazioni;
  }

}