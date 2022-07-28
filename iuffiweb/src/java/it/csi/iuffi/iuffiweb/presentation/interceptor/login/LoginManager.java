package it.csi.iuffi.iuffiweb.presentation.interceptor.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.csi.iride2.policy.entity.Identita;
import it.csi.iuffi.iuffiweb.presentation.interceptor.BaseManager;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class LoginManager extends BaseManager
{
  @Override
  public Return validate(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception
  {
    if (findHandlerAnnotation(NoLoginRequired.class, handler) != null)
    {
      return Return.SKIP_MANAGERS_AND_GO_TO_CONTROLLER;
    }
    HttpSession session = request.getSession();
    Identita identita = (Identita) session.getAttribute("identita");
    if (identita == null)
    {
    	request.setAttribute("webContext", IuffiConstants.IUFFIWEB.WEB_CONTEXT);
    	forward(IuffiConstants.PAGE.JSP.ERROR.SESSION_EXPIRED, request,
          response);
    	return Return.SKIP_ALL_MANAGER_AND_CONTROLLER;
    }
    else
    {
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      if (utenteAbilitazioni == null)
      {
        response.sendRedirect(IuffiConstants.PAGE.DO.LOGIN);
        return Return.SKIP_ALL_MANAGER_AND_CONTROLLER;
      }
    }
    session.setAttribute("webContext", IuffiConstants.IUFFIWEB.WEB_CONTEXT);
    return Return.CONTINUE_TO_NEXT_MANAGER;
  }

}
