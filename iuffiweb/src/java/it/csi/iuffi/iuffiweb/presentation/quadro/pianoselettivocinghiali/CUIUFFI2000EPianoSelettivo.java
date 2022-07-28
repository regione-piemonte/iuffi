package it.csi.iuffi.iuffiweb.presentation.quadro.pianoselettivocinghiali;

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
@IuffiSecurity(value = "CU-IUFFI-2000-E", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi2000e")
public class CUIUFFI2000EPianoSelettivo extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @IsPopup
  @RequestMapping(value = "/index_{idInfoCinghiali}", method = RequestMethod.GET)
  public String confermaElilmina(@PathVariable("idInfoCinghiali") long idInfoCinghiali, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("idInfoCinghiali", idInfoCinghiali);
    return "pianoselettivocinghiali/confermaElimina";
  }
  
  @IsPopup
  @RequestMapping(value = "/elimina_{idInfoCinghiali}", method = RequestMethod.POST)
  public String elimina(@PathVariable("idInfoCinghiali") long idInfoCinghiali, Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    quadroIuffiEJB.deletePianoSelettivo(idInfoCinghiali, getLogOperationOggettoQuadroDTO(session));
    return "dialog/success";
  }

}
