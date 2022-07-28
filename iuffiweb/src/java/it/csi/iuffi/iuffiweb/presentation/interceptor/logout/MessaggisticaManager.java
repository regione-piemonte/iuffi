package it.csi.iuffi.iuffiweb.presentation.interceptor.logout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.csi.iuffi.iuffiweb.dto.messaggistica.InfoMessaggio;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.IMessaggisticaWS;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.ListaMessaggi;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.LogoutException_Exception;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.Messaggio;
import it.csi.iuffi.iuffiweb.presentation.interceptor.BaseManager;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.WsUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class MessaggisticaManager extends BaseManager
{
  public static final String KEY_TIMESTAMP                       = "timestamp";
  public static final String KEY_NUMERO_MESSAGGI_TESTATA         = "numeroMessaggiTestata";
  public static final String KEY_NUMERO_MESSAGGI_GENERICI        = "numeroMessaggiGenerici";
  public static final String KEY_NUMERO_MESSAGGI_LOGOUT          = "numeroMessaggiLogout";
  public static final String KEY_NUMERO_TOTALE_MESSAGGI          = "numeroTotaleMessaggi";
  public static final String KEY_MESSAGGI                        = "messaggi";
  public static final String KEY_STATUS_DESC                     = "statusDesc";
  public static final String KEY_STATUS_CODE                     = "statusCode";
  public static final String KEY_TIMESTAMP_PRIMA_VISUALIZZAZIONE = "timestampVisualizzazione";
  public static final String KEY_DISABLED_UNTIL_NEXT_REFRESH     = "disabled";
  public static final int    FLAG_GENERICO                       = 1;
  public static final int    FLAG_TESTATA                        = 2;
  public static final int    FLAG_LOGOUT                         = 4;
  // DELTA_ERROR = 2/3 del refresh ossia 20 minuti
  public static final int    STATUS_OK                           = 0;
  public static final int    STATUS_LOGOUT                       = -1;
  public static final int    STATUS_ERROR                        = 1;
  public static final String DESC_STATUS_ERROR                   = "Servizio di messaggistica attualmente non disponibile";
  public static final String LOGOUT_ERROR_MESSAGE                = "logoutErrorMessage";
  public static long         MINUTI_VISUALIZZAZIONE_MESSAGGI     = 2;                                                      // PARAMETRO
                                                                                                                           // DB_PARAMETRO.MESE
  public static long         MINUTI_VERIFICA_LOGOUT              = 5;                                                      // PARAMETRO
                                                                                                                           // DB_PARAMETRO.MESL
  public static long         MINUTI_VERIFICA_NUOVI_MESSAGGI      = 45;                                                     // PARAMETRO
                                                                                                                           // DB_PARAMETRO.MESL

  static void loadMessaggistica()
  {
    try
    {
      Map<String, String> mapParametri = IuffiUtils.APPLICATION
          .getEjbQuadro().getParametriComune(
              IuffiConstants.PARAMETRO.MESSAGGISTICA_MINUTI_VISUALIZZAZIONE_TESTATA,
              IuffiConstants.PARAMETRO.MESSAGGISTICA_MINUTI_ATTESA_VERIFICA_LOGOUT,
              IuffiConstants.PARAMETRO.MESSAGGISTICA_MINUTI_ATTESA_VERIFICA_REFRESH);
      Long minutiVisualizzazioneMessaggi = IuffiUtils.NUMBERS
          .getNumericValue(mapParametri.get(
              IuffiConstants.PARAMETRO.MESSAGGISTICA_MINUTI_VISUALIZZAZIONE_TESTATA));
      if (minutiVisualizzazioneMessaggi != null)
      {
        MINUTI_VISUALIZZAZIONE_MESSAGGI = minutiVisualizzazioneMessaggi;
      }
      Long minutiVerificaLogout = IuffiUtils.NUMBERS
          .getNumericValue(mapParametri.get(
              IuffiConstants.PARAMETRO.MESSAGGISTICA_MINUTI_ATTESA_VERIFICA_LOGOUT));
      if (minutiVerificaLogout != null)
      {
        MINUTI_VERIFICA_LOGOUT = minutiVerificaLogout;
      }
      Long minutiVerificaNuoviMessaggi = IuffiUtils.NUMBERS
          .getNumericValue(mapParametri.get(
              IuffiConstants.PARAMETRO.MESSAGGISTICA_MINUTI_ATTESA_VERIFICA_REFRESH));
      if (minutiVerificaNuoviMessaggi != null)
      {
        MINUTI_VERIFICA_NUOVI_MESSAGGI = minutiVerificaNuoviMessaggi;
      }
    }
    catch (Exception e)
    {
      logger.error(
          "[MessaggisticaManager::static] Impossibile caricare i parametri della messaggistica",
          e);
    }
  }

  static
  {
    loadMessaggistica();
  }

  @Override
  public Return validate(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception
  {
    HttpSession session = request.getSession();
    @SuppressWarnings("unchecked")
    Map<String, Object> mapMessaggistica = (Map<String, Object>) session
        .getAttribute("messaggistica");
    if (mapMessaggistica == null)
    {
      /*
       * Non c'è in sessione l'hashmap della messaggista ==> Non si è passati
       * per la pagina della messaggistica di login ==> Non è il percorso
       * standard di accesso all'applicativo (link nei preferiti?!?!?) ==> Non
       * ho certezza che non ci siano messaggi da leggere obbligatori ==> SI
       * RITORNA AL LOGIN!
       */
      response.sendRedirect("/" + IuffiConstants.IUFFIWEB.WEB_CONTEXT
          + "/cuiuffi201/index.do");
      return Return.SKIP_ALL_MANAGER_AND_CONTROLLER;
    }

    String logoutErrorMessage = checkMessaggistica(session, mapMessaggistica);
    if (logoutErrorMessage == null)
    {
      // Nessuna richiesta di logout ==> si procede
      return Return.CONTINUE_TO_NEXT_MANAGER;
    }
    else
    {
      /*
       * Se il metodo checkMessaggistica ha restituito una stringa !=null è
       * perchè è in corso un logout forzato, quindi redirigo sulla pagina di
       * logout dove verrà visualizzato il messaggio restituito dal servizio
       * (passato come cookie)
       */
      redirectToLoggedOutPage(response, logoutErrorMessage);
      return Return.SKIP_ALL_MANAGER_AND_CONTROLLER;
    }
  }

  public static void redirectToLoggedOutPage(HttpServletResponse response,
      String logoutErrorMessage) throws IOException
  {
    final Cookie cookie = new Cookie(LOGOUT_ERROR_MESSAGE, logoutErrorMessage);
    cookie.setMaxAge(-1);
    response.addCookie(cookie);
    response.sendRedirect(
        "/" + IuffiConstants.IUFFIWEB.WEB_CONTEXT + "/cuiuffi201/logout.do");
  }

  public static String checkMessaggistica(HttpSession session,
      Map<String, Object> mapMessaggistica)
  {
    final String THIS_METHOD = "[MessaggisticaController::status]";
    boolean refreshSession = false;
    final long TIMESTAMP = System.currentTimeMillis();
    long timestamp = 0;
    timestamp = (Long) mapMessaggistica.get(KEY_TIMESTAMP);
    final long deltaTimestamp = TIMESTAMP - timestamp;
    if (deltaTimestamp > MINUTI_VERIFICA_NUOVI_MESSAGGI * 60 * 1000)
    {
      // Se eseguo un refresh dovrò aggiornare i dati in sessione
      // (indipendentemente che sia andato bene o male)
      refreshSession = true;
      try
      {
        final IMessaggisticaWS messaggisticaWS = IuffiUtils.WS
            .getMessaggisticaWithTimeout(WsUtils.SHORT_TIMEOUT);
        UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) getUtenteAbilitazioni(
            session);
        ListaMessaggi listaMessaggi = messaggisticaWS.getListaMessaggi(
            utenteAbilitazioni.getIdProcedimento(),
            utenteAbilitazioni.getRuolo().getCodice(),
            utenteAbilitazioni.getCodiceFiscale(), FLAG_TESTATA | FLAG_LOGOUT,
            Boolean.FALSE, null, Boolean.TRUE);
        mapMessaggistica.put(KEY_MESSAGGI,
            convertiMessaggi(listaMessaggi.getMessaggi()));
        mapMessaggistica.put(KEY_TIMESTAMP, TIMESTAMP);
        mapMessaggistica.put(KEY_TIMESTAMP_PRIMA_VISUALIZZAZIONE, null);
        mapMessaggistica.put(KEY_DISABLED_UNTIL_NEXT_REFRESH, null);
        mapMessaggistica.put(KEY_NUMERO_TOTALE_MESSAGGI,
            listaMessaggi.getNumeroTotaleMessaggi());
        mapMessaggistica.put(KEY_NUMERO_MESSAGGI_GENERICI,
            listaMessaggi.getNumeroMessaggiGenerici());
        mapMessaggistica.put(KEY_NUMERO_MESSAGGI_TESTATA,
            listaMessaggi.getNumeroMessaggiTestata());
        mapMessaggistica.put(KEY_NUMERO_MESSAGGI_LOGOUT,
            listaMessaggi.getNumeroMessaggiLogout());
      }
      catch (LogoutException_Exception e)
      {
        return performLogout(session, e);
      }
      catch (Exception e)
      {
        logger.warn(
            THIS_METHOD
                + " Errore nell'accesso al ws della messaggistica, metodo getListaMessaggi",
            e);
        // Ignoro l'eccezione ma setto il valore di status nella mappa
        mapMessaggistica.put(KEY_STATUS_CODE, STATUS_ERROR);
        mapMessaggistica.put(KEY_STATUS_DESC, DESC_STATUS_ERROR);
        // In caso di errore riduco il timestamp di DELTA_ERROR in modo da
        // aspettare un po' di minuti prima di riprovare ma senza dover di nuovo
        // aspettare
        // tutto il tempo del refresh (che di default dovrebbe essere a 30
        // minuti)
        final long DELTA_ERROR = (MINUTI_VERIFICA_NUOVI_MESSAGGI * 2) / 3; // 2/3
                                                                           // del
                                                                           // tempo
                                                                           // di
                                                                           // refresh
        mapMessaggistica.put(KEY_TIMESTAMP,
            Long.valueOf(TIMESTAMP - DELTA_ERROR));
        refreshSession = true;
      }
    }
    else
    {
      if (deltaTimestamp > MINUTI_VERIFICA_LOGOUT * 60 * 1000)
      {
        try
        {
          final IMessaggisticaWS messaggisticaWS = IuffiUtils.WS
              .getMessaggisticaWithTimeout(WsUtils.SHORT_TIMEOUT);
          UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) getUtenteAbilitazioni(
              session);
          messaggisticaWS.verificaLogout(utenteAbilitazioni.getIdProcedimento(),
              utenteAbilitazioni.getRuolo().getCodice());
        }
        catch (LogoutException_Exception e)
        {
          // Se eseguo un logout dovrò aggiornare i dati in sessione
          refreshSession = true;
          return performLogout(session, e);
        }
        catch (Exception e)
        {
          // Se eseguo un refresh dovrò aggiornare i dati in sessione
          refreshSession = true;
          logger.warn(
              THIS_METHOD
                  + " Errore nell'accesso al ws della messaggistica, metodo verificaLogout",
              e);
          // Ignoro l'eccezione ma setto il valore di status nella mappa
          mapMessaggistica.put(KEY_STATUS_CODE, STATUS_ERROR);
          mapMessaggistica.put(KEY_STATUS_DESC, DESC_STATUS_ERROR);
        }
      }
    }
    if (refreshSession)
    {
      // Rimetto in sessione la mappa e aggiorno il timestamp
      session.setAttribute("messaggistica", mapMessaggistica);
    }
    return null;
  }

  public static List<InfoMessaggio> convertiMessaggi(List<Messaggio> messaggi)
  {
    List<InfoMessaggio> list = new ArrayList<InfoMessaggio>();
    if (messaggi != null && !messaggi.isEmpty())
    {
      for (Messaggio m : messaggi)
      {
        InfoMessaggio infoMessaggio = new InfoMessaggio(m);
        list.add(infoMessaggio);
      }
    }
    return list;
  }

  private static UtenteAbilitazioni getUtenteAbilitazioni(HttpSession session)
  {
    return (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
  }

  public static String performLogout(HttpSession session,
      LogoutException_Exception e)
  {
    Enumeration<String> attrNames = session.getAttributeNames();
    /*
     * Richiesto un Logout ==> CANCELLO/DISINTEGRO la sessione utente in modo
     * che non possa fare più nulla!
     */
    if (attrNames != null)
    {
      for (; attrNames.hasMoreElements();)
      {
        session.removeAttribute(attrNames.nextElement());
      }
    }

    /*
     * e per finire invalido la sesione... In pratica mi basterebbe questo
     * ultimo passaggio ma per sicurezza preferisco anche aver eliminato tutto
     * dalla sessione
     */
    session.invalidate();
    return e.getMessage();
  }

}
