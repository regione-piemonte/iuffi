package it.csi.iuffi.iuffiweb.presentation.estrazionicampione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IEstrazioniEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-224", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping("cuiuffi224")
public class CUIUFFI224AnnullamentoEstrazioneCampione extends BaseController
{

  @Autowired
  private IEstrazioniEJB estrazioniEjb;

  @RequestMapping(value = "/index_{idNumeroLotto}_{idStatoEstrazione}_{idTipoEstrazione}")
  public String index(Model model, HttpSession session,
      @PathVariable(value = "idNumeroLotto") long idNumeroLotto,
      @PathVariable(value = "idStatoEstrazione") long idStatoEstrazione,
      @PathVariable(value = "idTipoEstrazione") long idTipoEstrazione)
      throws InternalUnexpectedException
  {
    if (idStatoEstrazione != IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.STATO_ESTRAZIONE.REGISTRATA)
    {
      model.addAttribute("messaggio",
          "Operazione non consentita: l'estrazione a campione deve trovarsi nello stato REGISTRATA");
      return "errore/messaggio";
    }

    if (!estrazioniEjb.isEstrazioneAnnullabile(idNumeroLotto, idTipoEstrazione))
    {
      model.addAttribute("messaggio",
          "Operazione non consentita: l'estrazione a campione non è l'ultima registrata per il dato tipo estrazione");
      return "errore/messaggio";
    }

    model.addAttribute("idNumeroLotto", idNumeroLotto);
    model.addAttribute("idStatoEstrazione", idStatoEstrazione);
    model.addAttribute("idTipoEstrazione", idStatoEstrazione);
    return "estrazioniacampione/confermaAnnulla";
  }

  @RequestMapping(value = "/index_{idNumeroLotto}_{idStatoEstrazione}_{idTipoEstrazione}", method = RequestMethod.POST)
  public String indexPost(Model model, HttpSession session,
      HttpServletRequest request,
      @PathVariable(value = "idNumeroLotto") long idNumeroLotto,
      @PathVariable(value = "idStatoEstrazione") long idStatoEstrazione,
      @PathVariable(value = "idTipoEstrazione") long idTipoEstrazione)
      throws InternalUnexpectedException
  {
    model.addAttribute("prfRequestValues", Boolean.TRUE);
    String motivo = (String) request.getParameter("motivo");
    Errors errors = new Errors();
    errors.validateMandatoryFieldMaxLength(motivo, "motivo", 2000);
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return index(model, session, idNumeroLotto, idStatoEstrazione,
          idTipoEstrazione);
    }
    estrazioniEjb.annullaEstrazioni(idNumeroLotto, motivo);
    return "redirect:../cuiuffi219/index_" + idNumeroLotto + ".do";
  }

}
