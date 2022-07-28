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

import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.NotificaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi110")
@IuffiSecurity(value = "CU-IUFFI-110", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI110NotificheProcedimentoController extends BaseController
{

  @Autowired
  private IRicercaEJB ricercaEJB = null;

  @RequestMapping(value = "index_{idProcedimento}", method = RequestMethod.GET)
  public String index(Model model,
      HttpSession session,
      @PathVariable("idProcedimento") long idProcedimento)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");

    List<NotificaDTO> notifiche = ricercaEJB.getNotifiche(idProcedimento,
        IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    if (notifiche != null)
    {
      for (NotificaDTO n : notifiche)
      {
        List<Long> l = new LinkedList<Long>();
        l.add(new Long(n.getIdUtente()));
        List<it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin> utente = super.loadRuoloDescr(
            l);
        String u = getUtenteDescrizione(new Long(n.getIdUtente()), utente);
        n.setUtente(u);
        n.setUtenteCorrenteAbilitato(true);
        String ruoloExtIdUtenteAggiornamento = utente.get(0).getRuolo()
            .getCodice();
        utenteAbilitazioni = getUtenteAbilitazioni(session);
        String ruoloUtenteCorrente = utenteAbilitazioni.getRuolo().getCodice();
        if (ruoloExtIdUtenteAggiornamento.compareTo(ruoloUtenteCorrente) != 0)
        {
          if (ruoloUtenteCorrente
              .compareTo("SERVIZI_ASSISTENZA@AGRICOLTURA") != 0)
          {
            n.setUtenteCorrenteAbilitato(false);
          }
        }
      }
    }

    String msgErrore = (String) session.getAttribute("msgErrore");
    if (msgErrore != null && msgErrore != "")
    {
      model.addAttribute("msgErrore", msgErrore);
      session.removeAttribute("msgErrore");
    }

    model.addAttribute("notifiche", notifiche);
    model.addAttribute("idProcedimento", idProcedimento);

    return "ricercaprocedimento/notificheProcedimento";
  }

}
