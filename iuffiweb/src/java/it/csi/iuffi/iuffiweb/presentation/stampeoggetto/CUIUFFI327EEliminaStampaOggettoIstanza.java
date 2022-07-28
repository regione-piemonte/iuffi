package it.csi.iuffi.iuffiweb.presentation.stampeoggetto;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.error.Errors;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@IuffiSecurity(value = "CU-IUFFI-327-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
@RequestMapping("/cuiuffi327e")
@IsPopup
public class CUIUFFI327EEliminaStampaOggettoIstanza extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/conferma_elimina_{idOggettoIcona}", method = RequestMethod.GET)
  public String confermaElimina(Model model, HttpSession session, @ModelAttribute("idOggettoIcona") @PathVariable("idOggettoIcona") long idOggettoIcona)
      throws InternalUnexpectedException
  {
    return "stampeoggetto/confermaEliminaIstanza";
  }

  @RequestMapping(value = "/elimina_{idOggettoIcona}", method = RequestMethod.GET)
  public String eliminaAllegato(Model model, HttpSession session, @ModelAttribute("idOggettoIcona") @PathVariable("idOggettoIcona") long idOggettoIcona)
      throws InternalUnexpectedException
  {
    try
    {
      quadroEJB.deleteStampeOggettoInseriteDaUtente(getIdProcedimentoOggetto(session), idOggettoIcona);
    }
    catch (ApplicationException e)
    {
      Errors errors = new Errors();
      errors.addError("error", e.getMessage());
      model.addAttribute("errors", errors);
      return "stampeoggetto/confermaEliminaIstanza";
    }
    return "redirect:/cuiuffi126l/lista.do";
  }

}
