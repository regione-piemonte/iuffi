package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-315-H", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi315h")
public class CUIUFFI315HChiudiConcessione extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @IsPopup
  @RequestMapping(value = "/index_{idConcessione}", method = RequestMethod.GET)
  public String index(@PathVariable("idConcessione") long idConcessione, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("idConcessione", idConcessione);
    return "concessioni/confermaChiusura";
  }
  
  @IsPopup
  @RequestMapping(value = "/chiudi_{idConcessione}", method = RequestMethod.POST)
  public String chiudi(@PathVariable("idConcessione") long idConcessione, Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    quadroIuffiEJB.chiudiConcessione(idConcessione, getUtenteAbilitazioni(session).getIdUtenteLogin());
    return "dialog/success";
  }
  
  @RequestMapping(value = "/canCambioStato_{idConcessione}", method = RequestMethod.GET)
  @ResponseBody
  public String canCambioStato(@PathVariable("idConcessione") long idConcessione, Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    if(quadroIuffiEJB.canCambioStato(idConcessione))
      return "SI";
    else return "NO";
  }

}
