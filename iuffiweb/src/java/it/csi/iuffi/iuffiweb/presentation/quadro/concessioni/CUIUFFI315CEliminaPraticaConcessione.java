package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-315-C", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi315c")
public class CUIUFFI315CEliminaPraticaConcessione extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @IsPopup
  @RequestMapping(value = "/index_{idPraticaConcessione}", method = RequestMethod.GET)
  public String confermaElilmina(@PathVariable("idPraticaConcessione") long idPraticaConcessione, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("idPraticaConcessione", idPraticaConcessione);
    return "concessioni/confermaEliminaPratica";
  }
  
  @IsPopup
  @RequestMapping(value = "/elimina_{idPraticaConcessione}", method = RequestMethod.POST)
  public String elimina(@PathVariable("idPraticaConcessione") long idPraticaConcessione, Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    quadroIuffiEJB.deletePraticaConcessione(idPraticaConcessione);
    return "dialog/success";
  }

}
