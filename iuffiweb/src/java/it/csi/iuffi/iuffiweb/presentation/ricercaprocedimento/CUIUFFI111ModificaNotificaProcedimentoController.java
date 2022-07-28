package it.csi.iuffi.iuffiweb.presentation.ricercaprocedimento;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.GravitaNotificaVO;
import it.csi.iuffi.iuffiweb.dto.NotificaDTO;
import it.csi.iuffi.iuffiweb.dto.StatoNotificaVO;
import it.csi.iuffi.iuffiweb.dto.VisibilitaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi111")
@IuffiSecurity(value = "CU-IUFFI-111", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI111ModificaNotificaProcedimentoController
    extends BaseController
{

  private static final long APERTA     = 0;
  private static final long CHIUSA     = 1;

  @Autowired
  private IRicercaEJB       ricercaEJB = null;
  @Autowired
  private IQuadroEJB        quadroEJB  = null;

  @RequestMapping(value = "index_{idNotifica}", method = RequestMethod.GET)
  public String index(Model model, HttpSession session,
      @PathVariable("idNotifica") long idNotifica)
      throws InternalUnexpectedException
  {
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    return defaultView(idNotifica, model, session);
  }

  @RequestMapping(value = "/index_{idNotifica}", method = RequestMethod.POST)
  public String post(Model model, @PathVariable("idNotifica") long idNotifica,
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
    Long idStato = errors.validateMandatoryLong(stato, "stato");
    Long idGravita = errors.validateMandatoryLong(gravita, "gravita");
    Long idVisibilita = errors.validateMandatoryLong(visibilita, "visibilita");

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return defaultView(idNotifica, model, session);
    }

    /*
     * Preparazione nuova notifica da passare al dao per poi fare l'inserimento
     */
    NotificaDTO notificaOld = ricercaEJB.getNotificaById(idNotifica);
    NotificaDTO notificaNew = new NotificaDTO();
    notificaNew.setNote(descrizione);
    notificaNew.setIdNotifica(notificaOld.getIdNotifica());
    notificaNew.setDataInizio(notificaOld.getDataInizio());

    if (idStato == APERTA)
      notificaNew.setStato("Aperta");
    else
      notificaNew.setStato("Chiusa");

    notificaNew.setIdGravitaNotifica(idGravita);
    notificaNew.setIdVisibilita(idVisibilita);

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    notificaNew.setIdUtente(utenteAbilitazioni.getIdUtenteLogin());

    ricercaEJB.updateNotifica(idNotifica, notificaNew, idProcedimento);

    boolean notificheBloccanti = quadroEJB.checkNotifiche(idProcedimento,
        IuffiConstants.FLAG_NOTIFICA.BLOCCANTE,
        IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    session.setAttribute("notificheBloccanti", notificheBloccanti);

    boolean notificheWarning = quadroEJB.checkNotifiche(idProcedimento,
        IuffiConstants.FLAG_NOTIFICA.WARNING,
        IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    session.setAttribute("notificheWarning", notificheWarning);

    boolean notificheGravi = quadroEJB.checkNotifiche(idProcedimento,
        IuffiConstants.FLAG_NOTIFICA.GRAVE,
        IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    session.setAttribute("notificheGravi", notificheGravi);

    return "redirect:../cuiuffi110/index_"
        + getProcedimentoFromSession(session).getIdProcedimento() + ".do";
  }

  public String defaultView(long idNotifica, Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    NotificaDTO notifica = ricercaEJB.getNotificaById(idNotifica);

    List<Long> l = new LinkedList<Long>();
    l.add(new Long(notifica.getIdUtente()));
    List<it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin> utente = super.loadRuoloDescr(
        l);

    String ruoloExtIdUtenteAggiornamento = utente.get(0).getRuolo().getCodice();
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    String ruoloUtenteCorrente = utenteAbilitazioni.getRuolo().getCodice();
    if (ruoloExtIdUtenteAggiornamento.compareTo(ruoloUtenteCorrente) != 0)
    {
      if (ruoloUtenteCorrente.compareTo("SERVIZI_ASSISTENZA@AGRICOLTURA") != 0)
      {
        session.setAttribute("msgErrore",
            "La notifica può essere modificata solo da utenti con ruolo uguale a quello dell'utente connesso");
        return "redirect:../cuiuffi110/index_"
            + getProcedimentoFromSession(session).getIdProcedimento() + ".do";
      }
    }

    String u = getUtenteDescrizione(new Long(notifica.getIdUtente()), utente);
    notifica.setUtente(u);

    if (notifica.getDataFine() == null)
      notifica.setStato("Aperta");
    else
      notifica.setStato("Chiusa");

    model.addAttribute("notifica", notifica);

    List<GravitaNotificaVO> elencoGravita = ricercaEJB
        .getElencoGravitaNotifica();
    model.addAttribute("elencoGravita", elencoGravita);
    model.addAttribute("idGravitaSelezionato", notifica.getIdGravitaNotifica());

    List<StatoNotificaVO> elencoStati = new LinkedList<StatoNotificaVO>();
    StatoNotificaVO s = null;
    s = new StatoNotificaVO();
    s.setId(APERTA);
    s.setDescrizione("Aperta");
    elencoStati.add(s);
    s = new StatoNotificaVO();
    s.setId(CHIUSA);
    s.setDescrizione("Chiusa");
    elencoStati.add(s);
    model.addAttribute("elencoStati", elencoStati);

    if (notifica.getStato().compareTo("Aperta") == 0)
      model.addAttribute("idStatoSelezionato", APERTA);
    else
      model.addAttribute("idStatoSelezionato", CHIUSA);

    List<VisibilitaDTO> elencoVisibilita = new LinkedList<VisibilitaDTO>();
    UtenteAbilitazioni uAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    elencoVisibilita = ricercaEJB.getElencoVisibilitaNotifiche(
        IuffiUtils.PAPUASERV.getFirstCodiceAttore(uAbilitazioni));
    model.addAttribute("elencoVisibilita", elencoVisibilita);

    return "ricercaprocedimento/modificaNotificheProcedimento";
  }

}
