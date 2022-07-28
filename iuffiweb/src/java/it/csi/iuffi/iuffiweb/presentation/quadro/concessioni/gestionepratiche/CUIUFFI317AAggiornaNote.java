package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni.gestionepratiche;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-317-A", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi317a")
public class CUIUFFI317AAggiornaNote extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;
  @Autowired
  IQuadroEJB quadroEJB;

  @IsPopup
  @RequestMapping(value = "/index_{idConcessione}_{idPraticaConcessione}", method = RequestMethod.GET)
  public String index(@PathVariable("idConcessione") long idConcessione, @PathVariable("idPraticaConcessione") long idPraticaConcessione, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {

    ConcessioneDTO concessione = quadroIuffiEJB.getConcessione(idConcessione);
    if(concessione.getIdStatoConcessione()!=IuffiConstants.CONCESSIONI.STATO.BOZZA && concessione.getIdStatoConcessione()!=IuffiConstants.CONCESSIONI.STATO.RICHIESTA_VERCOR )
    {
      String msgErrore = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.PRATICA_NON_IN_BOZZA_O_VERCOR);
      model.addAttribute("msgErrore", msgErrore);
      return "concessioni/popupNotePratica";
    }
    
    model.addAttribute("idConcessione", idConcessione);
    model.addAttribute("idPraticaConcessione", idPraticaConcessione);
    String note = quadroIuffiEJB.getNotePratica(idPraticaConcessione);
    model.addAttribute("note", note);
    
    return "concessioni/popupNotePratica";
  }
  
  @IsPopup
  @RequestMapping(value = "/index_{idConcessione}_{idPraticaConcessione}", method = RequestMethod.POST)
  public String post(@PathVariable("idConcessione") long idConcessione, @PathVariable("idPraticaConcessione") long idPraticaConcessione, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {

    String note = request.getParameter("note");
    Errors errors = new Errors();
    errors.validateFieldMaxLength(note, "note", 4000);
    
    if(errors.isEmpty())
    {
      quadroIuffiEJB.aggiornaNotePraticaConcessione(idPraticaConcessione, note, getIdUtenteLogin(request.getSession()));
      return "dialog/success";
    }
    
    model.addAttribute("preferRequest", true);
    model.addAttribute("errors", errors);
    model.addAttribute("idConcessione", idConcessione);
    model.addAttribute("idPraticaConcessione", idPraticaConcessione);
    
    return "concessioni/popupNotePratica";
  }
  

}
