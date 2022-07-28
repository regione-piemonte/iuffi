package it.csi.iuffi.iuffiweb.presentation.ricercaprocedimento;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.GravitaNotificaVO;
import it.csi.iuffi.iuffiweb.dto.NotificaDTO;
import it.csi.iuffi.iuffiweb.dto.VisibilitaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi112")
@IuffiSecurity(value = "CU-IUFFI-112", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI112NuovaNotificaController extends BaseController
{

  @Autowired
  private IRicercaEJB ricercaEJB = null;

  @RequestMapping(value = "index", method = RequestMethod.GET)
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    return defaultView(model, session);
  }

  @RequestMapping(value = "/index", method =
  { RequestMethod.POST })
  public String post(Model model,
      @RequestParam(value = "descrizione", required = false) String descrizione,
      @RequestParam(value = "gravita", required = false) String gravita,
      @RequestParam(value = "stato", required = false) String stato,
      @RequestParam(value = "visibilita", required = false) String visibilita,
      HttpSession session)
      throws InternalUnexpectedException, IuffiPermissionException
  {
    long idProcedimento = getProcedimentoFromSession(session)
        .getIdProcedimento();
    model.addAttribute("idProcedimento", idProcedimento);
    model.addAttribute("prfvalues", Boolean.TRUE);

    Errors errors = new Errors();

    /* Validazione input */
    descrizione = descrizione.trim();
    errors.validateFieldLength(descrizione, "descrizione", 5, 4000);
    Long idGravita = errors.validateMandatoryLong(gravita, "gravita");
    Long idVisibilita = errors.validateMandatoryLong(visibilita, "visibilita");

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return defaultView(model, session);
    }

    /*
     * Preparazione nuova notifica da passare al dao per poi fare l'inserimento
     */
    NotificaDTO notificaNew = new NotificaDTO();
    notificaNew.setNote(descrizione);
    // notificaNew.setDescrizione(descrizione);
    notificaNew.setStato("Aperta");
    notificaNew.setIdGravitaNotifica(idGravita);
    notificaNew.setIdVisibilita(idVisibilita);

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    notificaNew.setIdUtente(utenteAbilitazioni.getIdUtenteLogin());

    ricercaEJB.insertNuovaNotifica(notificaNew, idProcedimento);

    return "redirect:../cuiuffi110/index_"
        + getProcedimentoFromSession(session).getIdProcedimento() + ".do";
  }

  public String defaultView(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    boolean inserisciNuovaNotifica = true;
    model.addAttribute("inserisciNuovaNotifica", inserisciNuovaNotifica);

    List<GravitaNotificaVO> elencoGravita = ricercaEJB
        .getElencoGravitaNotifica();
    model.addAttribute("elencoGravita", elencoGravita);
    // model.addAttribute("idGravitaSelezionato", elencoGravita.get(0));

    List<VisibilitaDTO> elencoVisibilita = new LinkedList<VisibilitaDTO>();
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    elencoVisibilita = ricercaEJB.getElencoVisibilitaNotifiche(
        IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    model.addAttribute("elencoVisibilita", elencoVisibilita);

    return "ricercaprocedimento/inserimentoNuovaNotifica";
  }

}
