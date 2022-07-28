package it.csi.iuffi.iuffiweb.presentation.quadro.pianoselettivocinghiali;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.danni.UnitaMisuraDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2000-I", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi2000i")
public class CUIUFFI2000IPianoSelettivo extends CUIUFFI2000InserisciModificaPianoSelettivoBase
{

  @Autowired
  IQuadroEJB      quadroEJB;
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String inserisci(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("modificaInserisci", "Inserisci");
    return super.modificaInserisci(model, request); 
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String index(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("modificaInserisci", "Inserisci");
    return super.conferma(model, request, "I"); 
  }

  @RequestMapping(value = "/getUnitaDiMisura_{idMetodoCensimento}", method = RequestMethod.GET)
  @ResponseBody
  public String getUnitaDiMisura(Model model, HttpSession session, @PathVariable(value = "idMetodoCensimento") long idMetodoCensimento) throws InternalUnexpectedException
  {
    UnitaMisuraDTO um = quadroIuffiEJB.getUnitaMisuraByIdMetodoCensimento(idMetodoCensimento);
    if(um==null || um.getIdUnitaMisura()==0)
      return null; 
    return um.getDescunita(); 
  }

}
