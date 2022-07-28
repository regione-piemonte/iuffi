package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

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
@IuffiSecurity(value = "CU-IUFFI-319-C", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi319c")
public class CUIUFFI319CEliminaDistretto extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @IsPopup
  @RequestMapping(value = "/index_{idOgur}_{idDistretto}", method = RequestMethod.GET)
  public String confermaElilmina(@PathVariable("idOgur") long idOgur ,@PathVariable("idDistretto") long idDistretto, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("idOgur", idOgur);
    model.addAttribute("idDistretto", idDistretto);
    return "ogur/distretti/confermaElimina";
  }
  
  @IsPopup
  @RequestMapping(value = "/elimina_{idOgur}_{idDistretto}", method = RequestMethod.POST)
  public String elimina(@PathVariable("idOgur") long idOgur ,@PathVariable("idDistretto") long idDistretto, Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    quadroIuffiEJB.deleteDistretto(idOgur, idDistretto);
    return "dialog/success";
  }

}
