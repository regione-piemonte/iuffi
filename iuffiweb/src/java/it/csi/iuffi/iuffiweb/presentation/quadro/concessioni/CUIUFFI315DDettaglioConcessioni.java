package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni;

import java.util.List;

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
import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.PraticaConcessioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-315-D", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi315d")
public class CUIUFFI315DDettaglioConcessioni extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;
  
  @RequestMapping(value = "/index_{idConcessione}", method = RequestMethod.GET)
  public String index(@PathVariable("idConcessione") long idConcessione, Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException
  {
    ConcessioneDTO concessione = quadroIuffiEJB.getConcessione(idConcessione);
    model.addAttribute("concessione", concessione);
    model.addAttribute("idConcessione", idConcessione);
    
    if(concessione.getIdStatoConcessione().longValue()==10 || concessione.getIdStatoConcessione().longValue()==20) {
      model.addAttribute("isModifica", true);
    }
    
    return "concessioni/dettaglio";
  }
  
  
  @RequestMapping(value = "/getElencoPraticheConcessione_{idConcessione}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(
      @PathVariable("idConcessione") long idConcessione, HttpSession session, 
      Model model)  throws InternalUnexpectedException
  {
    return quadroIuffiEJB.getElencoPraticheConcessione(idConcessione);
  }

  
  @RequestMapping(value = "/getStatoConcessione_{idConcessione}", method = RequestMethod.GET)
  @ResponseBody
  public String getStatoConcessione(
      @PathVariable("idConcessione") long idConcessione, HttpSession session, 
      Model model)  throws InternalUnexpectedException
  {
    return quadroIuffiEJB.getStatoConcessione(idConcessione);
  }
  
}
