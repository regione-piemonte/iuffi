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
import it.csi.iuffi.iuffiweb.dto.PianoSelettivoDTO;
import it.csi.iuffi.iuffiweb.dto.danni.UnitaMisuraDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2000-M", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi2000m")
public class CUIUFFI2000MPianoSelettivo extends CUIUFFI2000InserisciModificaPianoSelettivoBase
{

  @Autowired
  IQuadroEJB      quadroEJB;
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;


  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String inserisci(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("modificaInserisci", "Modifica");
    PianoSelettivoDTO piano = quadroIuffiEJB.getPianoSelettivo(getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("piano", piano);
    return super.modificaInserisci(model, request);
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String index(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("modificaInserisci", "Inserisci");
    PianoSelettivoDTO piano = quadroIuffiEJB.getPianoSelettivo(getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("piano", piano);
    return super.conferma(model, request, "M"); 
  }



  @RequestMapping(value = "/getUnitaDiMisura_{idMetodoCensimento}", method = RequestMethod.POST)
  @ResponseBody
  public String getUnitaDiMisura(Model model, HttpSession session, @PathVariable(value = "idMetodoCensimento") long idMetodoCensimento) throws InternalUnexpectedException
  {
    UnitaMisuraDTO um = quadroIuffiEJB.getUnitaMisuraByIdMetodoCensimento(idMetodoCensimento);
    if(um==null)
      return null; 
    return um.getDescrizione(); 
  }

}
