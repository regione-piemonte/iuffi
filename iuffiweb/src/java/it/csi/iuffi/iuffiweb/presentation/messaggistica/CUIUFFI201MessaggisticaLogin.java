package it.csi.iuffi.iuffiweb.presentation.messaggistica;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.exception.InternalException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.DettagliMessaggio;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.InternalException_Exception;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.ListaMessaggi;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.LogoutException_Exception;
import it.csi.iuffi.iuffiweb.presentation.MessaggisticaBaseController;
import it.csi.iuffi.iuffiweb.presentation.interceptor.logout.MessaggisticaManager;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping(value = "/cuiuffi201")
@IuffiSecurity(value = "CU-IUFFI-201", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class CUIUFFI201MessaggisticaLogin extends MessaggisticaBaseController
{
  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session,
      HttpServletResponse response)
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) getUtenteAbilitazioni(
        session);
    try
    {
      // Richiamo alla messaggistica senza timeout MAX
      ListaMessaggi listaMessaggi = IuffiUtils.WS.getMessaggistica()
          .getListaMessaggi(utenteAbilitazioni.getIdProcedimento(),
              utenteAbilitazioni.getRuolo().getCodice(),
              utenteAbilitazioni.getCodiceFiscale(),
              MessaggisticaManager.FLAG_GENERICO, Boolean.FALSE, Boolean.TRUE,
              Boolean.TRUE);
      if (listaMessaggi.getNumeroMessaggiGenerici() > 0)
      {
        /*
         * Ops ci sono dei messaggi da leggere obbligatoriamente ==> Non posso
         * lasciare andare avanti l'utente finchè non li ha letti ==> metto
         * l'elenco messaggi nel model e lascio che la pagina jsp li visualizzi
         */
        model.addAttribute("messaggi",
            MessaggisticaManager.convertiMessaggi(listaMessaggi.getMessaggi()));
      }
      else
      {
        /*
         * Non ci sono messaggi bloccanti da leggere ==> metto in sessione la
         * hashmap con i dati della messaggistica in modo che il logout manager
         * da ora in poi trovi la hashmap in sessione e quindi sappia che si è
         * fatto accesso correttamente. Imposto il timeout a 0 in modo che il
         * logout manager richiami immediatamente i servizi di papua per avere i
         * dati di messaggi di testata.
         */
        Map<String, Object> mapMessaggistica = new HashMap<String, Object>();
        mapMessaggistica.put(MessaggisticaManager.KEY_STATUS_CODE,
            MessaggisticaManager.STATUS_OK);
        mapMessaggistica.put(MessaggisticaManager.KEY_STATUS_DESC, null);
        mapMessaggistica.put(MessaggisticaManager.KEY_TIMESTAMP, 0l);
        session.setAttribute("messaggistica", mapMessaggistica);
        /*
         * Infine rimando alla pagina di index per permettere all'utente di
         * fruire dell'applicativo
         */
        return "redirect:../index.do";
      }
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (InternalException_Exception e)
    {
      e.printStackTrace();
    }
    catch (LogoutException_Exception e)
    {
      // E' in corso un logout ==> butto fuori l'utente dall'applicativo
      try
      {

        String messaggioLogout = MessaggisticaManager.performLogout(session, e);
        MessaggisticaManager.redirectToLoggedOutPage(response, messaggioLogout);
      }
      catch (Exception e1)
      {
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "messaggistica/login/lista";
  }

  @RequestMapping(value = "/dettaglio_{idElencoMessaggi}", method = RequestMethod.GET)
  @IsPopup
  public String dettaglio(Model model, HttpServletRequest request,
      @PathVariable("idElencoMessaggi") long idElencoMessaggi)
      throws InternalException
  {
    DettagliMessaggio dm = null;
    try
    {
      dm = IuffiUtils.WS.getMessaggistica().getDettagliMessaggio(
          idElencoMessaggi,
          getUtenteAbilitazioni(request.getSession()).getCodiceFiscale());
      model.addAttribute("dataInserimento",
          IuffiUtils.DATE.formatDateTime(IuffiUtils.DATE
              .fromXMLGregorianCalendar(dm.getDataInizioValidita())));
      if (dm != null)
      {
        String messaggio = dm.getTestoMessaggio();
        if (messaggio != null)
        {
          dm.setTestoMessaggio(messaggio.replace("\n", "<br />"));
        }
      }
      model.addAttribute("messaggio", dm);
      model.addAttribute("idElencoMessaggi", idElencoMessaggi);
    }
    catch (Exception e)
    {

    }
    return "messaggistica/login/dettaglio";
  }

  @RequestMapping(value = "/dettaglio_{idElencoMessaggi}", method = RequestMethod.POST)
  @IsPopup
  public String dettaglioPost(Model model, HttpServletRequest request,
      @PathVariable("idElencoMessaggi") long idElencoMessaggi)
      throws InternalException
  {
    try
    {
      Errors errors = new Errors();
      errors.validateMandatory(request.getParameter("confermoLettura"),
          "confermoLettura",
          "Per proseguire bisogna indicare di aver letto il messaggio");
      if (errors.isEmpty())
      {
        IuffiUtils.WS.getMessaggistica().confermaLetturaMessaggio(
            idElencoMessaggi,
            getUtenteAbilitazioni(request.getSession()).getCodiceFiscale());
        return "dialog/success";
      }
      else
      {
        errors.addError("error",
            "Per proseguire bisogna indicare di aver letto il messaggio");
        errors.addToModel(model);
        DettagliMessaggio dm = null;
        dm = IuffiUtils.WS.getMessaggistica().getDettagliMessaggio(
            idElencoMessaggi,
            getUtenteAbilitazioni(request.getSession()).getCodiceFiscale());
        model.addAttribute("dataInserimento",
            IuffiUtils.DATE.formatDateTime(IuffiUtils.DATE
                .fromXMLGregorianCalendar(dm.getDataInizioValidita())));
        model.addAttribute("messaggio", dm);
      }
    }
    catch (Exception e)
    {

    }
    return "messaggistica/login/dettaglio";
  }

  @RequestMapping(value = "/logout")
  @NoLoginRequired
  public String logout(Model model, HttpServletRequest request)
  {
    Cookie[] cookies = request.getCookies();
    String logoutErrorMessage = "";
    if (cookies != null)
    {
      for (Cookie cookie : cookies)
      {
        if (MessaggisticaManager.LOGOUT_ERROR_MESSAGE.equals(cookie.getName()))
        {
          logoutErrorMessage = cookie.getValue();
          break;
        }
      }
    }
    if (logoutErrorMessage == null)
    {
      logoutErrorMessage = "Logout programmato, descrizione attualmente non disponibile";
    }
    model.addAttribute(MessaggisticaManager.LOGOUT_ERROR_MESSAGE,
        logoutErrorMessage);
    return "messaggistica/logout";
  }

  @RequestMapping(value = "/download_allegato_{idElencoMessaggi}_{idAllegato}", method = RequestMethod.GET)
  @IsPopup
  public ResponseEntity<byte[]> downloadAllegato(HttpSession session,
      @PathVariable("idElencoMessaggi") long idElencoMessaggi,
      @PathVariable("idAllegato") long idAllegato)
      throws InternalUnexpectedException
  {
    return super.downloadAllegato(session, idElencoMessaggi, idAllegato);
  }

}
