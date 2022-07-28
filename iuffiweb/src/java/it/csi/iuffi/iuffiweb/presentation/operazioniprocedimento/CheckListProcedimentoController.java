package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.ImportoLiquidatoDTO;
import it.csi.iuffi.iuffiweb.dto.RipartizioneImportoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.OggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.ControlloAmministrativoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.VisitaLuogoExtDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliinlocomisureinvestimento.DatiSpecificiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RigaJSONInterventoQuadroEconomicoByLivelloDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-270", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CheckListProcedimentoController extends BaseController
{
  @Autowired
  IQuadroEJB     quadroEJB  = null;
  @Autowired
  IRicercaEJB    ricercaEJB = null;
  @Autowired
  IInterventiEJB interventiEJB;

  @RequestMapping(value = "/cuiuffi270/index", method = RequestMethod.POST)
  @IsPopup
  @ResponseBody
  public String elencoIterHtml(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    StringBuffer sb = new StringBuffer("<div class=\"panel-body\">");
    sb.append(
        "\n<div class=\"row\"><a style=\"margin-bottom:2em\" class=\"pull-right ico_spaced ico24 ico_print\" href=\"../cuiuffi274/stampa.do\" title=\"Stampa\"></a></div>");
    final UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(
        session);
    List<GruppoOggettoDTO> listGruppiOggetto = ricercaEJB
        .getElencoOggettiChiusi(getIdProcedimento(session),
            Arrays.asList(utenteAbilitazioni.getMacroCU()),
            IuffiUtils.PAPUASERV
                .isAttoreBeneficiarioOCAA(utenteAbilitazioni), utenteAbilitazioni.getIdProcedimento());
    if (listGruppiOggetto != null)
    {
      for (GruppoOggettoDTO gruppo : listGruppiOggetto)
        for (OggettoDTO oggetto : gruppo.getOggetti())
        {
          visualizzaOggetto(sb, oggetto.getCodice(), oggetto);
        }
    }

    writejavascript(sb);

    return sb.toString();
  }

  private void writejavascript(StringBuffer sb)
  {
    sb.append(" <script> "
        + "  function visualizza(clasname){ "
        + "     $('#'+clasname).show(); "
        + "     $('#espandi_'+clasname).html('<a style=\"text-decoration: none;font-size:20px;vertical-align:middle;margin-bottom:1em;\" class=\"icon icon-collapse\"  href=\"#\" onclick=\"nascondi(&#39;'+clasname+'&#39;);\"></a>'); "
        + "  }"
        + "  function nascondi(clasname){ "
        + "     $('#'+clasname).hide(); "
        + "     $('#espandi_'+clasname).html('<a style=\"text-decoration: none;font-size:20px;vertical-align:middle;margin-bottom:1em;\" class=\"icon icon-expand\"  href=\"#\" onclick=\"visualizza(&#39;'+clasname+'&#39;);\"></a>'); "
        + "   }  "
        + "</script>");
  }

  private void visualizzaOggetto(StringBuffer sb, String codice,
      OggettoDTO oggetto) throws InternalUnexpectedException
  {
    if (codice.equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_SOSTEGNO)
        || codice
            .equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_SOSTEGNO_PAGAMENTO)
        || codice
            .equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_SOSTEGNO_PREMIO))
    {
      writeDatiIdentificativi(sb, oggetto);
      writeDomandaAiuto(sb, oggetto);
    }
    else
      if (codice.equals(
          IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO)
          || codice.equals(
              IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO_GAL)
          || codice.equals(
              IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIBILITA_FINANZIAMENTO_GAL)
          || codice.equals(
              IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIBILITA_FINANZIAMENTO))
      {
        writeIstruttoriaInvestimento(sb, oggetto);
      }
      else
        if (codice.equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_SALDO)
            || codice.equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_ACCONTO)
            || codice.equals(IuffiConstants.OGGETTO.CODICE.DOMANDA_ANTICIPO))
        {
          writeDomandaPagamento(sb, oggetto);
        }
        else
          if (codice
              .equals(IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_ACCONTO)
              || codice
                  .equals(IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_SALDO)
              || codice.equals(
                  IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_ANTICIPO))
          {
            writeIstruttoriaPagamento(sb, oggetto);
          }

  }

  private void writeIstruttoriaInvestimento(StringBuffer sb, OggettoDTO oggetto)
      throws InternalUnexpectedException
  {
    sb.append("<fieldset><legend><span id=\"espandi_divistrinv_"
        + oggetto.getCodice() + oggetto.getIdProcedimentoOggetto()
        + "\"><a style=\"text-decoration: none;font-size:20px;vertical-align:middle;margin-bottom:1em;\" class=\"icon icon-expand\"  href=\"#\" onclick=\"visualizza('divistrinv_"
        + oggetto.getCodice() + oggetto.getIdProcedimentoOggetto()
        + "');\"></a></span>   "
        + StringEscapeUtils.escapeHtml4(oggetto.getDescrizione())
        + "</legend></fieldset>");
    sb.append("<div id=\"divistrinv_" + oggetto.getCodice()
        + oggetto.getIdProcedimentoOggetto() + "\" style=\"display:none\">");
    List<ControlloAmministrativoDTO> controlliAmministrativi = quadroEJB
        .getControlliAmministrativi(oggetto.getIdProcedimentoOggetto(),
            IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI, null);
    if (controlliAmministrativi != null)
    {
      // CONTROLLO TECNICO AMMINISTRATIVO
      sb.append(
          "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"contramm\">"
              + "<div class=\"panel-heading \">"
              + "<h3 id=\"panel-title_contramm\" class=\"panel-title\">"
              + "<a href=\"#content_contramm\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_contramm\"><i class=\"icon icon-collapse\"></i>&nbsp;Controlli tecnico amministrativo</a></h3>"
              + "</div>  <div class=\"collapse in\" id=\"content_contramm\">"
              + "<div class=\"panel-body\">");

      for (ControlloAmministrativoDTO controllo : controlliAmministrativi)
      {
        if (controllo.getIdControlloAmministratPadre() != null)
        {
          sb.append("<div class=\"row\">"
              + "<div class=\"col-md-10\">" + controllo.getCodice() + " "
              + StringEscapeUtils.escapeHtml4(controllo.getDescrizione())
              + "</div><div class=\"col-md-2\">" + controllo.getDescEsito()
              + "</div>"
              + "</div>");
        }
        else
        {
          sb.append("<div class=\"row\" >"
              + "<div class=\"col-md-12\" style=\"background-color:#DDD\"><label style=\"font-size:1.1em\">"
              + StringEscapeUtils.escapeHtml4(controllo.getDescrizione())
              + "</label></div>"
              + "</div>");
        }
      }
      sb.append("</div>"
          + "</div>"
          + "</div>");
    }

    ProcedimentoOggetto procedimentoOggetto = quadroEJB
        .getProcedimentoOggetto(oggetto.getIdProcedimentoOggetto());
    long idQuadroOggetto = procedimentoOggetto
        .findQuadroByCodiceQuadro(
            IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI)
        .getIdQuadroOggetto();
    writeVisiteSulLuogo(procedimentoOggetto, idQuadroOggetto, sb,
        "Visita sul luogo");
    writeEsitoTecnico(sb, procedimentoOggetto, idQuadroOggetto,
        "Esito Tecnico");

    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCU("CU-IUFFI-166-V");
    if (quadro != null)
    {
      EsitoFinaleDTO esitoFinale = quadroEJB.getEsitoFinale(
          procedimentoOggetto.getIdProcedimentoOggetto(),
          quadro.getIdQuadroOggetto());
      if (esitoFinale != null)
      {
        // ESITO FINALE
        sb.append(
            "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"esitoFinale\">"
                + "<div class=\"panel-heading \">"
                + "<h3 id=\"panel-title_esitoFinale\" class=\"panel-title\">"
                + "<a href=\"#content_esitoFinale\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_esitoFinale\"><i class=\"icon icon-collapse\"></i>&nbsp;Esito Finale</a></h3>"
                + "</div>  <div class=\"collapse in\" id=\"content_esitoFinale\">"
                + "<div class=\"panel-body\">"

                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Esito finale</label></div><div class=\"col-md-9\">"
                + StringEscapeUtils.escapeHtml4(esitoFinale.getDescrEsito())
                + "</div>"
                + "</div>"
                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Motivazioni</label></div><div class=\"col-md-9\">"
                + IuffiUtils.STRING.nvl(
                    StringEscapeUtils.escapeHtml4(esitoFinale.getMotivazione()))
                + "</div>"
                + "</div>"
                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Prescrizioni</label></div><div class=\"col-md-9\">"
                + IuffiUtils.STRING.nvl(StringEscapeUtils
                    .escapeHtml4(esitoFinale.getPrescrizioni()))
                + "</div>"
                + "</div>"
                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Funzionario istruttore</label></div><div class=\"col-md-9\">"
                + StringEscapeUtils.escapeHtml4(
                    IuffiUtils.STRING.nvl(esitoFinale.getDescrTecnico()))
                + "</div>"
                + "</div>"
                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Funzionario di grado superiore</label></div><div class=\"col-md-9\">"
                + StringEscapeUtils.escapeHtml4(esitoFinale.getDescrGradoSup())
                + "</div>"
                + "</div>");

        // IMPORTI CONCESSI
        List<RigaJSONInterventoQuadroEconomicoByLivelloDTO> interventi = interventiEJB
            .getElencoInterventiByLivelliQuadroEconomico(
                procedimentoOggetto.getIdProcedimentoOggetto());

        if (interventi != null)
        {
          sb.append(
              "<table class=\"table table-hover table-bordered table-condensed tableBlueTh\" >"
                  + "<thead>"
                  + "<tr>"
                  + " <th>Misura</th>"
                  + " <th>Data ammissione</th>"
                  + " <th>Importo richiesto</th>  "
                  + " <th>Spesa ammessa</th>  "
                  + " <th>Contributo concesso</th>  "
                  + "</tr>"
                  + "</thead><tbody>");
          for (RigaJSONInterventoQuadroEconomicoByLivelloDTO item : interventi)
          {
            // ESITO FINALE
            sb.append("<tr>"
                + "<td>" + item.getCodiceLivello() + "</td>"
                + "<td>"
                + IuffiUtils.DATE.formatDate(item.getDataAmmissione())
                + "</td>"
                + "<td>&euro; " + IuffiUtils.FORMAT
                    .formatCurrency(item.getImportoInvestimento())
                + "</td>"
                + "<td>&euro; "
                + IuffiUtils.FORMAT.formatCurrency(item.getImportoAmmesso())
                + "</td>"
                + "<td>&euro; " + IuffiUtils.FORMAT
                    .formatCurrency(item.getImportoContributo())
                + "</td>"
                + "</tr>");
          }
          sb.append("</tbody></table>");
        }
        sb.append("</div>"
            + "</div>"
            + "</div>");
      }
    }

    sb.append("</div>");

  }

  private void writeVisiteSulLuogo(ProcedimentoOggetto procedimentoOggetto,
      long idQuadroOggetto, StringBuffer sb, String title)
      throws InternalUnexpectedException
  {
    List<VisitaLuogoExtDTO> visite = quadroEJB.getVisiteLuogo(
        procedimentoOggetto.getIdProcedimentoOggetto(), idQuadroOggetto, null);

    if (visite != null)
    {
      // VISITE SUL LUOGO
      sb.append(
          "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"visita\">"
              + "<div class=\"panel-heading \">"
              + "<h3 id=\"panel-title_visita\" class=\"panel-title\">"
              + "<a href=\"#content_visita\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_visita\"><i class=\"icon icon-collapse\"></i>&nbsp;"
              + StringEscapeUtils.escapeHtml4(title) + "</a></h3>"
              + "</div>  <div class=\"collapse in\" id=\"content_visita\">"
              + "<div class=\"panel-body\">");

      sb.append(
          "<table class=\"table table-hover table-bordered table-condensed tableBlueTh\" >"
              + "<thead>"
              + "<tr>"
              + " <th>Data visita</th>"
              + " <th>Funzionario controllore</th>"
              + " <th>Esito</th>  "
              + " <th>Data verbale</th>  "
              + " <th>Numero verbale</th>  "
              + "</tr>"
              + "</thead><tbody>");

      for (VisitaLuogoExtDTO item : visite)
      {
        sb.append("<tr>"
            + "<td>" + IuffiUtils.DATE.formatDate(item.getDataVisita())
            + "</td>"
            + "<td>" + StringEscapeUtils.escapeHtml4(
                IuffiUtils.STRING.nvl(item.getDescTecnico()))
            + "</td>"
            + "<td>" + IuffiUtils.STRING.nvl(item.getDescEsito()) + "</td>"
            + "<td>" + IuffiUtils.DATE.formatDate(item.getDataVerbale())
            + "</td>"
            + "<td>" + IuffiUtils.STRING.nvl(item.getNumeroVerbale())
            + "</td>"
            + "</tr>");
      }
      sb.append("</tbody></table>");
      sb.append("</div>"
          + "</div>"
          + "</div>");
    }
  }

  private void writeDomandaAiuto(StringBuffer sb, OggettoDTO oggetto)
      throws InternalUnexpectedException
  {
    DecodificaDTO<String> datiProt = ricercaEJB
        .findProtocolloPraticaByIdPOAndCodice(oggetto.getIdProcedimentoOggetto(), 2002);
    ProcedimentoOggetto procedimentoOggetto = quadroEJB
        .getProcedimentoOggetto(oggetto.getIdProcedimentoOggetto());
    String organismoDelegato = quadroEJB
        .getTestataProcedimento(procedimentoOggetto.getIdProcedimento())
        .getDescAmmCompetenza();

    // DOMANDA AIUTO

    sb.append(
        "<fieldset><legend><span id=\"espandi_divaiuto_" + oggetto.getCodice()
            + "\"><a style=\"text-decoration: none;font-size:20px;vertical-align:middle;margin-bottom:1em;\" class=\"icon icon-expand\"  href=\"#\" onclick=\"visualizza('divaiuto_"
            + oggetto.getCodice() + "');\"></a></span>   "
            + StringEscapeUtils.escapeHtml4(oggetto.getDescrizione())
            + "</legend></fieldset>");
    sb.append("<div id=\"divaiuto_" + oggetto.getCodice()
        + "\" style=\"display:none\">");

    sb.append(
        "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"domandaAiuto\">"
            + "<div class=\"panel-heading \">"
            + "<h3 id=\"panel-title_domandaAiuto\" class=\"panel-title\">"
            + "<a href=\"#content_domandaAiuto\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_domandaAiuto\"><i class=\"icon icon-collapse\"></i></a></h3>"
            + "</div>  <div class=\"collapse in\" id=\"content_domandaAiuto\">"
            + "<div class=\"panel-body\">"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >N. domanda</label></div><div class=\"col-md-9\">"
            + IuffiUtils.STRING.nvl(oggetto.getCodiceDomanda()) + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >N. Protocollo pratica</label></div><div class=\"col-md-9\">"
            + IuffiUtils.STRING.nvl(datiProt.getCodice()) + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Data Protocollo pratica</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                IuffiUtils.STRING.nvl(datiProt.getDescrizione()))
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Organismo delegato</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                IuffiUtils.STRING.nvl(organismoDelegato))
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Resp. procedimento</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                IuffiUtils.STRING.nvl(quadroEJB.getResponsabileProcedimento(
                    procedimentoOggetto.getIdProcedimento())))
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div></div>");
  }

  private void writeDatiIdentificativi(StringBuffer sb, OggettoDTO oggetto)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = quadroEJB
        .getProcedimentoOggetto(oggetto.getIdProcedimentoOggetto());
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCodiceQuadro(
        IuffiConstants.QUADRO.CODICE.DATI_IDENTIFICATIVI);
    DatiIdentificativi datiIdentificativi = quadroEJB
        .getDatiIdentificativiProcedimentoOggetto(
            procedimentoOggetto.getIdProcedimentoOggetto(),
            quadro.getIdQuadroOggetto(), procedimentoOggetto.getDataFine(), 0);

    // DATI ANAGRAFICI - AZIENDA
    sb.append(
        "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"datiGenerali\">"
            + "<div class=\"panel-heading \">"
            + "<h3 id=\"panel-title_datiGenerali\" class=\"panel-title\">"
            + "<a href=\"#content_datiGenerali\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_datiGenerali\"><i class=\"icon icon-collapse\"></i>&nbsp;Dati anagrafici- AZIENDA</a></h3>"
            + "</div>  <div class=\"collapse in\" id=\"content_datiGenerali\">"
            + "<div class=\"panel-body\">"
            + "<div class=\"row\">"
            + "<div class=\"col-md-1\"><label >Cuaa</label></div><div class=\"col-md-3\">"
            + datiIdentificativi.getAzienda().getCuaa() + "</div>"
            + "<div class=\"col-md-2\"><label >Partita IVA</label></div><div class=\"col-md-3\">"
            + StringEscapeUtils.escapeHtml4(
                datiIdentificativi.getAzienda().getPartitaIva())
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-2\"><label >Denominazione</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                datiIdentificativi.getAzienda().getDenominazione())
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-2\"><label >Forma giuridica</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                datiIdentificativi.getAzienda().getFormaGiuridica())
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-2\"><label >Sede legale</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                datiIdentificativi.getAzienda().getIndirizzoSedeLegale())
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div>");

    // DATI RAPPRESENTANTE LEGALE
    sb.append(
        "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"rapprLegale\">"
            + "<div class=\"panel-heading \">"
            + "<h3 id=\"panel-title_rapprLegale\" class=\"panel-title\">"
            + "<a href=\"#content_rapprLegale\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_rapprLegale\"><i class=\"icon icon-collapse\"></i>&nbsp;Rappresentante legale / Titolare</a></h3>"
            + "</div>  <div class=\"collapse in\" id=\"content_rapprLegale\">"
            + "<div class=\"panel-body\">"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Codice Fiscale</label></div><div class=\"col-md-3\">"
            + datiIdentificativi.getRappLegale().getCodiceFiscale() + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Cognome</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                datiIdentificativi.getRappLegale().getCognome())
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Nome</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                datiIdentificativi.getRappLegale().getNome())
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Indirizzo di residenza</label></div><div class=\"col-md-9\">"
            + StringEscapeUtils.escapeHtml4(
                datiIdentificativi.getRappLegale().getIndirizzoResidenza())
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div>");

  }

  private void writeDomandaPagamento(StringBuffer sb, OggettoDTO oggetto)
      throws InternalUnexpectedException
  {
	  DecodificaDTO<String> datiProt = new DecodificaDTO<>();
	  if((IuffiConstants.OGGETTO.CODICE.DOMANDA_SALDO).equals(oggetto.getCodice())){
		  datiProt =ricercaEJB.findProtocolloPraticaByIdPOAndCodice(oggetto.getIdProcedimentoOggetto(), 2007);
	  }else if((IuffiConstants.OGGETTO.CODICE.DOMANDA_ACCONTO).equals(oggetto.getCodice())){
		  datiProt =ricercaEJB.findProtocolloPraticaByIdPOAndCodice(oggetto.getIdProcedimentoOggetto(), 2006);
	  }else if((IuffiConstants.OGGETTO.CODICE.DOMANDA_ANTICIPO).equals(oggetto.getCodice())){
		  datiProt =ricercaEJB.findProtocolloPraticaByIdPOAndCodice(oggetto.getIdProcedimentoOggetto(), 2005);
	  }
    ProcedimentoOggetto procedimentoOggetto = quadroEJB
        .getProcedimentoOggetto(oggetto.getIdProcedimentoOggetto());
    String organismoDelegato = quadroEJB
        .getTestataProcedimento(procedimentoOggetto.getIdProcedimento())
        .getDescAmmCompetenza();

    // DOMANDA PAGAMENTO
    sb.append(
        "<fieldset><legend><span id=\"espandi_divpag_" + oggetto.getCodice()
            + "\"><a style=\"text-decoration: none;font-size:20px;vertical-align:middle;margin-bottom:1em;\" class=\"icon icon-expand\"  href=\"#\" onclick=\"visualizza('divpag_"
            + oggetto.getCodice() + "');\"></a></span>   "
            + StringEscapeUtils.escapeHtml4(oggetto.getDescrizione())
            + "</legend></fieldset>");
    sb.append("<div id=\"divpag_" + oggetto.getCodice()
        + "\" style=\"display:none\">");

    sb.append(
        "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"domandaPagamento\">"
            + "<div class=\"panel-heading \">"
            + "<h3 id=\"panel-title_domandaPagamento\" class=\"panel-title\">"
            + "<a href=\"#content_domandaPagamento\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_domandaPagamento\"><i class=\"icon icon-collapse\"></i></a></h3>"
            + "</div>  <div class=\"collapse in\" id=\"content_domandaPagamento\">"
            + "<div class=\"panel-body\">"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Identificativo</label></div><div class=\"col-md-9\">"
            + IuffiUtils.STRING.nvl(procedimentoOggetto.getIdentificativo())
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >N. Protocollo pratica</label></div><div class=\"col-md-9\">"
            + IuffiUtils.STRING.nvl(datiProt.getCodice()) + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Data Protocollo pratica</label></div><div class=\"col-md-9\">"
            + IuffiUtils.STRING.nvl(datiProt.getDescrizione()) + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Organismo delegato</label></div><div class=\"col-md-9\">"
            + IuffiUtils.STRING.nvl(
                StringEscapeUtils.escapeHtml4(organismoDelegato))
            + "</div>"
            + "</div>"
            + "<div class=\"row\">"
            + "<div class=\"col-md-3\"><label >Resp. procedimento</label></div><div class=\"col-md-9\">"
            + IuffiUtils.STRING.nvl(quadroEJB.getResponsabileProcedimento(
                procedimentoOggetto.getIdProcedimento()))
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div>"
            + "</div></div>");
  }

  private void writeControlli(StringBuffer sb, OggettoDTO oggetto,
      String codQuadro, String title) throws InternalUnexpectedException
  {
    List<ControlloAmministrativoDTO> controlliAmministrativi = quadroEJB
        .getControlliAmministrativi(oggetto.getIdProcedimentoOggetto(),
            codQuadro, null);
    if (controlliAmministrativi != null)
    {
      // CONTROLLO TECNICO AMMINISTRATIVO
      sb.append(
          "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"contramm\">"
              + "<div class=\"panel-heading \">"
              + "<h3 id=\"panel-title_contramm\" class=\"panel-title\">"
              + "<a href=\"#content_contramm\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_contramm\"><i class=\"icon icon-collapse\"></i>&nbsp;"
              + StringEscapeUtils.escapeHtml4(title) + "</a></h3>"
              + "</div>  <div class=\"collapse in\" id=\"content_contramm\">"
              + "<div class=\"panel-body\">");

      for (ControlloAmministrativoDTO controllo : controlliAmministrativi)
      {
        if (controllo.getIdControlloAmministratPadre() != null)
        {
          sb.append("<div class=\"row\">"
              + "<div class=\"col-md-10\">" + controllo.getCodice() + " "
              + StringEscapeUtils.escapeHtml4(controllo.getDescrizione())
              + "</div><div class=\"col-md-2\">"
              + IuffiUtils.STRING.nvl(controllo.getDescEsito()) + "</div>"
              + "</div>");
        }
        else
        {
          sb.append("<div class=\"row\" >"
              + "<div class=\"col-md-12\" style=\"background-color:#DDD\"><label style=\"font-size:1.1em\">"
              + StringEscapeUtils.escapeHtml4(controllo.getDescrizione())
              + "</label></div>"
              + "</div>");
        }
      }
      sb.append("</div>"
          + "</div>"
          + "</div>");
    }
  }

  private void writeIstruttoriaPagamento(StringBuffer sb, OggettoDTO oggetto)
      throws InternalUnexpectedException
  {
    sb.append("<fieldset><legend><span id=\"espandi_divistrinv_"
        + oggetto.getCodice() + oggetto.getIdProcedimentoOggetto()
        + "\"><a style=\"text-decoration: none;font-size:20px;vertical-align:middle;margin-bottom:1em;\" class=\"icon icon-expand\"  href=\"#\" onclick=\"visualizza('divistrinv_"
        + oggetto.getCodice() + oggetto.getIdProcedimentoOggetto()
        + "');\"></a></span>   "
        + StringEscapeUtils.escapeHtml4(oggetto.getDescrizione())
        + "</legend></fieldset>");
    sb.append("<div id=\"divistrinv_" + oggetto.getCodice()
        + oggetto.getIdProcedimentoOggetto() + "\" style=\"display:none\">");

    writeControlli(sb, oggetto,
        IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI,
        "Controlli tecnico amministrativo");

    ProcedimentoOggetto procedimentoOggetto = quadroEJB
        .getProcedimentoOggetto(oggetto.getIdProcedimentoOggetto());
    long idQuadroOggetto = procedimentoOggetto
        .findQuadroByCodiceQuadro(
            IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI)
        .getIdQuadroOggetto();

    writeVisiteSulLuogo(procedimentoOggetto, idQuadroOggetto, sb,
        "Visita sul luogo");
    writeEsitoTecnico(sb, procedimentoOggetto, idQuadroOggetto,
        "Esito Tecnico");

    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCU("CU-IUFFI-166-V");
    if (quadro != null)
    {
      EsitoFinaleDTO esitoFinale = quadroEJB.getEsitoFinale(
          procedimentoOggetto.getIdProcedimentoOggetto(),
          quadro.getIdQuadroOggetto());
      if (esitoFinale != null)
      {
        // ESITO FINALE
        sb.append(
            "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"esitoFinale\">"
                + "<div class=\"panel-heading \">"
                + "<h3 id=\"panel-title_esitoFinale\" class=\"panel-title\">"
                + "<a href=\"#content_esitoFinale\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_esitoFinale\"><i class=\"icon icon-collapse\"></i>&nbsp;Esito Finale</a></h3>"
                + "</div>  <div class=\"collapse in\" id=\"content_esitoFinale\">"
                + "<div class=\"panel-body\">"

                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Esito finale</label></div><div class=\"col-md-9\">"
                + esitoFinale.getDescrEsito() + "</div>"
                + "</div>"
                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Motivazioni</label></div><div class=\"col-md-9\">"
                + IuffiUtils.STRING.nvl(
                    StringEscapeUtils.escapeHtml4(esitoFinale.getMotivazione()))
                + "</div>"
                + "</div>"
                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Prescrizioni</label></div><div class=\"col-md-9\">"
                + IuffiUtils.STRING.nvl(StringEscapeUtils
                    .escapeHtml4(esitoFinale.getPrescrizioni()))
                + "</div>"
                + "</div>"
                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Funzionario istruttore</label></div><div class=\"col-md-9\">"
                + IuffiUtils.STRING.nvl(esitoFinale.getDescrTecnico())
                + "</div>"
                + "</div>"
                + "<div class=\"row\">"
                + "<div class=\"col-md-3\"><label >Funzionario di grado superiore</label></div><div class=\"col-md-9\">"
                + esitoFinale.getDescrGradoSup() + "</div>"
                + "</div>");

        // IMPORTI CONCESSI
        List<RigaJSONInterventoQuadroEconomicoByLivelloDTO> interventi = interventiEJB
            .getElencoInterventiByLivelliQuadroEconomico(
                procedimentoOggetto.getIdProcedimentoOggetto());

        if (interventi != null)
        {
          sb.append(
              "<table class=\"table table-hover table-bordered table-condensed tableBlueTh\" >"
                  + "<thead>"
                  + "<tr>"
                  + " <th>Misura</th>"
                  + " <th>Data ammissione</th>"
                  + " <th>Importo richiesto</th>  "
                  + " <th>Spesa ammessa</th>  "
                  + " <th>Contributo concesso</th>  "
                  + "</tr>"
                  + "</thead><tbody>");
          for (RigaJSONInterventoQuadroEconomicoByLivelloDTO item : interventi)
          {
            // ESITO FINALE
            sb.append("<tr>"
                + "<td>" + item.getCodiceLivello() + "</td>"
                + "<td>"
                + IuffiUtils.DATE.formatDate(item.getDataAmmissione())
                + "</td>"
                + "<td>&euro; " + IuffiUtils.FORMAT
                    .formatCurrency(item.getImportoInvestimento())
                + "</td>"
                + "<td>&euro; "
                + IuffiUtils.FORMAT.formatCurrency(item.getImportoAmmesso())
                + "</td>"
                + "<td>&euro; " + IuffiUtils.FORMAT
                    .formatCurrency(item.getImportoContributo())
                + "</td>"
                + "</tr>");
          }
          sb.append("</tbody></table>");
        }

        sb.append("</div>"
            + "</div>"
            + "</div>");
      }
    }

    // ESTRAZIONE
    DatiSpecificiDTO datiSpecifici = quadroEJB.getDatiSpecifici(
        procedimentoOggetto.getIdProcedimentoOggetto(),
        procedimentoOggetto.getIdProcedimento());
    if (datiSpecifici != null)
    {
      sb.append(
          "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"estrazione\">"
              + "<div class=\"panel-heading \">"
              + "<h3 id=\"panel-title_estrazione\" class=\"panel-title\">"
              + "<a href=\"#content_esito\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_estrazione\"><i class=\"icon icon-collapse\"></i>&nbsp;Dati specifici</a></h3>"
              + "</div>  <div class=\"collapse in\" id=\"content_estrazione\">"
              + "<div class=\"panel-body\">"

              + "<div class=\"row\">"
              + "<div class=\"col-md-3\"><label >Domanda sottoposta a estrazione</label></div><div class=\"col-md-9\">"
              + ("S".equals(datiSpecifici.getFlagSottopostaEstrazione()) ? "Si"
                  : "No")
              + "</div>"
              + "</div>"
              + "<div class=\"row\">"
              + "<div class=\"col-md-3\"><label >Data estrazione</label></div><div class=\"col-md-9\">"
              + IuffiUtils.DATE.formatDate(datiSpecifici.getDataEstrazione())
              + "</div>"
              + "</div>"
              + "<div class=\"row\">"
              + "<div class=\"col-md-3\"><label >Domanda estratta a campione</label></div><div class=\"col-md-9\">"
              + ("S".equals(datiSpecifici.getFlagEstratta()) ? "Si" : "No")
              + "</div>"
              + "</div>"
              + "</div>"
              + "</div>"
              + "</div>");
    }
    writeControlli(sb, oggetto,
        IuffiConstants.QUADRO.CODICE.CONTROLLI_IN_LOCO_MISURE_INVESTIMENTO,
        "CONTROLLI TECNICO AMMINISTRATIVI (PER CONTROLLO IN LOCO)");
    QuadroOggettoDTO quadroControlliLoco = procedimentoOggetto
        .findQuadroByCodiceQuadro(
            IuffiConstants.QUADRO.CODICE.CONTROLLI_IN_LOCO_MISURE_INVESTIMENTO);

    if (quadroControlliLoco != null)
    {
      idQuadroOggetto = quadroControlliLoco.getIdQuadroOggetto();
      writeVisiteSulLuogo(procedimentoOggetto, idQuadroOggetto, sb,
          "VISITA PER CONTROLLO IN LOCO");
      writeEsitoTecnico(sb, procedimentoOggetto, idQuadroOggetto,
          "ESITO CONTROLLO IN LOCO");
    }
    // LIQUIDAZIONE
    List<ImportoLiquidatoDTO> importi = quadroEJB.getElencoImportiLiquidazione(
        procedimentoOggetto.getIdProcedimentoOggetto());
    if (importi != null)
    {
      sb.append(
          "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"esitoFinale\">"
              + "<div class=\"panel-heading \">"
              + "<h3 id=\"panel-title_esitoFinale\" class=\"panel-title\">"
              + "<a href=\"#content_esitoFinale\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_esitoFinale\"><i class=\"icon icon-collapse\"></i>&nbsp;Liquidazione</a></h3>"
              + "</div>  <div class=\"collapse in\" id=\"content_esitoFinale\">"
              + "<div class=\"panel-body\">");

      sb.append(
          "<table class=\"table table-hover table-bordered table-condensed tableBlueTh\" >"
              + "<thead>"
              + "<tr>"
              + " <th>Tipo operazione</th>"
              + " <th>Importo</th>"
              + " <th>Numero lista</th>  "
              + " <th>Stato lista</th>  "
              + " <th>Tecnico liquidatore</th>  "
              + "</tr>"
              + "</thead><tbody>");
      for (ImportoLiquidatoDTO item : importi)
      {
        // ESITO FINALE
        sb.append("<tr>"
            + "<td>" + item.getCodiceLivello() + "</td>"
            + "<td>&euro; "
            + IuffiUtils.FORMAT.formatCurrency(item.getImportoLiquidato())
            + "</td>"
            + "<td>" + item.getNumeroLista() + "</td>"
            + "<td>" + item.getStatoLista() + "</td>"
            + "<td>" + item.getTecnico() + "</td>"
            + "</tr>");

        List<RipartizioneImportoDTO> quote = quadroEJB
            .getRipartizioneImporto(item.getIdListaLiquidazImpLiq());
        if (quote != null)
        {
          sb.append("<tr><td colspan=\"5\">");

          sb.append(
              "<table class=\"table table-hover table-bordered table-condensed tableBlueTh\" >"
                  + "<thead>"
                  + "<tr>"
                  + " <th>Voce ripartizione</th>"
                  + " <th>Percentuale ripartizione</th>"
                  + " <th>Importo ripartito</th>  "
                  + "</tr>"
                  + "</thead><tbody>");
          for (RipartizioneImportoDTO itemq : quote)
          {
            // ESITO FINALE
            sb.append("<tr>"
                + "<td>" + itemq.getVoceRipartizione() + "</td>"
                + "<td>" + IuffiUtils.FORMAT.formatCurrency(
                    itemq.getPercentualeRipartizione())
                + "%</td>"
                + "<td>&euro; " + IuffiUtils.FORMAT
                    .formatCurrency(itemq.getImportoRipartito())
                + "</td>"
                + "</tr>");
          }

          sb.append("</tbody></table>");
          sb.append("</td></tr>");
        }
      }
      sb.append("</tbody></table>");

      sb.append("</div>"
          + "</div></div>");
    }

    sb.append("</div>");
  }

  private void writeEsitoTecnico(StringBuffer sb,
      ProcedimentoOggetto procedimentoOggetto, long idQuadroOggetto,
      String title)
      throws InternalUnexpectedException
  {
    EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(
        procedimentoOggetto.getIdProcedimentoOggetto(), idQuadroOggetto);
    if (esito != null)
    {
      // ESITO TECNICO
      sb.append(
          "<div class=\"panel panel-primary custom-collapsible-panel\" id=\"esito\">"
              + "<div class=\"panel-heading \">"
              + "<h3 id=\"panel-title_esito\" class=\"panel-title\">"
              + "<a href=\"#content_esito\" onclick=\"return false\" class=\"no-decoration\" data-toggle=\"collapse\" data-target=\"#content_esito\"><i class=\"icon icon-collapse\"></i>&nbsp;"
              + title + "</a></h3>"
              + "</div>  <div class=\"collapse in\" id=\"content_esito\">"
              + "<div class=\"panel-body\">"

              + "<div class=\"row\">"
              + "<div class=\"col-md-3\"><label >Funzionario istruttore</label></div><div class=\"col-md-9\">"
              + IuffiUtils.STRING
                  .nvl(StringEscapeUtils.escapeHtml4(esito.getDescrTecnico()))
              + "</div>"
              + "</div>"
              + "<div class=\"row\">"
              + "<div class=\"col-md-3\"><label >Funzionario di grado superiore</label></div><div class=\"col-md-9\">"
              + IuffiUtils.STRING
                  .nvl(StringEscapeUtils.escapeHtml4(esito.getDescrGradoSup()))
              + "</div>"
              + "</div>"
              + "<div class=\"row\">"
              + "<div class=\"col-md-3\"><label >Esito controlli</label></div><div class=\"col-md-9\">"
              + IuffiUtils.STRING
                  .nvl(StringEscapeUtils.escapeHtml4(esito.getDescrEsito()))
              + "</div>"
              + "</div>"

              + "</div>"
              + "</div>"
              + "</div>");
    }
  }

}