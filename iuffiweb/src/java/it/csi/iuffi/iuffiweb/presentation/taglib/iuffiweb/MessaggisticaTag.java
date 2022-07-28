package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import it.csi.iuffi.iuffiweb.dto.messaggistica.InfoMessaggio;
import it.csi.iuffi.iuffiweb.presentation.interceptor.logout.MessaggisticaManager;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class MessaggisticaTag extends BaseTag
{
  /** serialVersionUID */
  protected static final long serialVersionUID = 4558965659815071438L;

  @Override
  public int doEndTag() throws JspException
  {
    HttpSession session = this.pageContext.getSession();
    StringBuilder sb = new StringBuilder();
    writeMessaggistica(sb, session);
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

  @SuppressWarnings("unchecked")
  private void writeMessaggistica(StringBuilder sb, HttpSession session)
  {
    try
    {
      Map<String, Object> mapMessaggistica = (Map<String, Object>) session
          .getAttribute("messaggistica");
      if (mapMessaggistica != null && mapMessaggistica
          .get(MessaggisticaManager.KEY_DISABLED_UNTIL_NEXT_REFRESH) == null)
      {
        long totaleMessaggi = (Long) mapMessaggistica
            .get(MessaggisticaManager.KEY_NUMERO_TOTALE_MESSAGGI);
        if (totaleMessaggi > 0)
        {
          final Long numeroMessaggiGenerici = (Long) mapMessaggistica
              .get(MessaggisticaManager.KEY_NUMERO_MESSAGGI_GENERICI);
          long numeroMessaggiDaLeggere = numeroMessaggiGenerici +
              (Long) mapMessaggistica
                  .get(MessaggisticaManager.KEY_NUMERO_MESSAGGI_LOGOUT);

          long tempoDiVisualizzazioneRimanente = 0;
          Long timestampPrimaVisualizzazione = (Long) mapMessaggistica
              .get(MessaggisticaManager.KEY_TIMESTAMP_PRIMA_VISUALIZZAZIONE);
          final long MILLISECONDI_VISUALIZZAZIONE = MessaggisticaManager.MINUTI_VISUALIZZAZIONE_MESSAGGI
              * 60 * 1000;
          if (timestampPrimaVisualizzazione == null)
          {
            // I messaggi non sono ancora stati visualizzati ==> inserisco il
            // timestamp di prima visualizzazione nella mappa e la rimetto in
            // sessione in modo
            // che possa essere serializzata sul tutti i server del cluster.
            timestampPrimaVisualizzazione = System.currentTimeMillis();
            mapMessaggistica.put(
                MessaggisticaManager.KEY_TIMESTAMP_PRIMA_VISUALIZZAZIONE,
                timestampPrimaVisualizzazione);
            session.setAttribute("messaggistica", mapMessaggistica);
            if (numeroMessaggiDaLeggere == 0)
            {
              // Se non ho messaggi (generici+logout) da leggere ma solo
              // messaggi di testata allora il banner è a tempo
              tempoDiVisualizzazioneRimanente = MILLISECONDI_VISUALIZZAZIONE;
            }
          }
          else
          {
            if (numeroMessaggiDaLeggere == 0)
            {
              // Se non ho messaggi (generici+logout) da leggere ma solo
              // messaggi di testata allora il banner è a tempo
              if (System.currentTimeMillis()
                  - timestampPrimaVisualizzazione > MILLISECONDI_VISUALIZZAZIONE)
              {
                // Superato il tempo di visualizzazione del messaggio ==>
                // attendo il prossimo refresh per visualizzarlo nuovamente
                return;
              }
              tempoDiVisualizzazioneRimanente = timestampPrimaVisualizzazione
                  + MILLISECONDI_VISUALIZZAZIONE - System.currentTimeMillis();
            }
          }
          List<InfoMessaggio> messaggi = (List<InfoMessaggio>) mapMessaggistica
              .get("messaggi");
          writeTestoMessaggio(sb, messaggi, tempoDiVisualizzazioneRimanente,
              numeroMessaggiGenerici);
        }
      }
    }
    catch (Exception e)
    {
      logger.error(
          "[TestataTag::writeMessaggistica] Si è verificato un errore imprevisto "
              + e.getMessage());
    }
  }

  protected void writeTestoMessaggio(StringBuilder sb,
      List<InfoMessaggio> messaggi, long tempoDiVisualizzazioneRimanente,
      long numeroMessaggiDaLeggere)
  {
    String testoMessaggio = "";
    StringBuilder messaggiBuilder = new StringBuilder();
    if (messaggi != null)
    {
      for (InfoMessaggio m : messaggi)
      {
        if (messaggiBuilder.length() > 0)
        {
          messaggiBuilder.append(" *** ");
        }
        messaggiBuilder.append(m.getTitolo());
      }
    }
    String testoConNumeroMessaggiDaLeggere = null;
    if (numeroMessaggiDaLeggere > 0)
    {
      if (numeroMessaggiDaLeggere == 1)
      {
        testoConNumeroMessaggiDaLeggere = "E' presente un messaggio da leggere";
      }
      else
      {
        testoConNumeroMessaggiDaLeggere = "Sono presenti "
            + numeroMessaggiDaLeggere + " messaggi da leggere";
      }
    }
    if (testoConNumeroMessaggiDaLeggere != null)
    {
      if (messaggiBuilder.length() > 0)
      {
        messaggiBuilder.append(" *** ");
      }
      messaggiBuilder.append(testoConNumeroMessaggiDaLeggere);
    }

    testoMessaggio = messaggiBuilder.toString();
    sb.append(
        "<div class=\"container-fluid\" id=\"container-messaggistica\">"
            + "<a href=\"#\" style=\"padding-right:8px\" onclick=\"return turnOffMessaggistica()\" class=\"pull-right\" data-dismiss=\"alert\">&times;</a>");
    if (numeroMessaggiDaLeggere > 0)
    {
      sb.append("<a href=\"/").append(IuffiConstants.IUFFIWEB.WEB_CONTEXT)
          .append(
              "/cuiuffi202/index.do\" style=\"margin-top:8px\" class=\"ico32 ico_youvegotmail pull-right\" title=\"");
      sb.append(testoConNumeroMessaggiDaLeggere).append("\"></a>");
    }
    sb.append(
        "<div class=\"alert alert-info testo-messaggistica\" id=\"banner_messaggistica\" data-time=\"")
        .append(
            tempoDiVisualizzazioneRimanente)
        .append("\"");
    if (numeroMessaggiDaLeggere > 0)
    {
      sb.append("style=\"padding-right:52px\" ");
    }
    sb.append(
        " onmouseover=\"_messaggistica_rolling=false\" onmouseout=\"_messaggistica_rolling=true\">"
            + "<div class=\"msg-container\">"
            + "<div class=\"infinite\"><span id=\"messaggi-testata\">")
        .append(escapeHtml(testoMessaggio))
        .append("</span></div></div></div></div>");
  }
}