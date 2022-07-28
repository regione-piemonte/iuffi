package it.csi.iuffi.iuffiweb.presentation.quadro.pianoselettivocinghiali;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.PianoSelettivoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2000-V", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "cuiuffi2000v")
public class CUIUFFI2000VPianoSelettivo extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @Autowired
  IQuadroEJB quadroEJB;
  
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException
  {
    PianoSelettivoDTO piano = quadroIuffiEJB.getPianoSelettivo(getIdProcedimentoOggetto(session));
    model.addAttribute("piano", piano);
    return "pianoselettivocinghiali/elenco";
  }
  
}
