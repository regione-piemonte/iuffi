package it.csi.iuffi.iuffiweb.presentation;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.presentation.interceptor.logout.MessaggisticaManager;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;

@Controller
@RequestMapping(value = "/messaggistica")
@IuffiSecurity(value = "", controllo = IuffiSecurity.Controllo.NESSUNO)
public class MessaggisticaController extends BaseController
{
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

  @RequestMapping(value = "/turnoff")
  @SuppressWarnings("unchecked")
  @ResponseBody
  public Map<String, Object> turnoff(HttpSession session)
  {
    final String THIS_METHOD = "[MessaggisticaController::turnoff]";
    try
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " BEGIN.");
      }
      /*
       * La mappa "messaggistica" è mantenuta aggiornata in sessione dal
       * IuffiInterceptor (nello specifico dal LogoutManager richiamato dal
       * IuffiInterceptor) ogni qual volta si accede a una pagina (ovviamente
       * le chiamate ai servizi vengono eseguite soltanto ogni X minuti per non
       * sovraccaricare il sistema)
       */
      Map<String, Object> mapMessaggistica = (Map<String, Object>) session
          .getAttribute("messaggistica");
      mapMessaggistica.put(MessaggisticaManager.KEY_DISABLED_UNTIL_NEXT_REFRESH,
          Boolean.TRUE);
      return mapMessaggistica;
    }
    finally
    {
      logger.debug(THIS_METHOD + " END.");
    }
  }
}
