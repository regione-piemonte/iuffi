package it.csi.iuffi.iuffiweb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "N/A", controllo = IuffiSecurity.Controllo.NESSUNO)
public class HomeController extends BaseController
{

  @RequestMapping("/")
  public String redirectIndex(ModelMap model, HttpSession session)
      throws InternalUnexpectedException
  {
    clearCommonInSession(session);
    IuffiFactory.removeIdProcedimentoInSession(session);
    if (getUtenteAbilitazioni(session).getRuolo().isUtenteMonitoraggio())
    {
      return "redirect:cuiuffi206/index.do";
    }
    return "redirect:home/index.do";
  }

  @RequestMapping(value = "/page/{currentPage}/{currentPageSize}", consumes = { "application/json" }, produces = { "application/json" }, method = RequestMethod.POST)
  public String setPageNumber(ModelMap model, @PathVariable("currentPage") Integer currentPage, @PathVariable("currentPageSize") Integer currentPageSize,
      HttpServletRequest request, HttpSession session, HttpServletResponse response) throws InternalUnexpectedException
  {
    session.setAttribute("currentPage", currentPage);
    session.setAttribute("currentPageSize", currentPageSize);
    return "{\"msg\":\"success\"}";
  }

}
