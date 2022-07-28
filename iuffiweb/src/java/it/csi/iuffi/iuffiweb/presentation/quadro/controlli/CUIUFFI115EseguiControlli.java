package it.csi.iuffi.iuffiweb.presentation.quadro.controlli;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.INuovoProcedimentoEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi115")
@IuffiSecurity(value = "CU-IUFFI-115", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI115EseguiControlli extends BaseController
{
  @Autowired
  private IQuadroEJB            quadroEJB         = null;
  @Autowired
  private INuovoProcedimentoEJB nuovoProcedimento = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String dettaglio(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    Procedimento procedimento = getProcedimentoFromSession(session);
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    TestataProcedimento testataProcedimento = (TestataProcedimento) session
        .getAttribute(TestataProcedimento.SESSION_NAME);

    Vector<Long> vId = new Vector<Long>();
    vId.add(testataProcedimento.getIdAzienda());
    List<AziendaDTO> vAziende = nuovoProcedimento.getDettaglioAziendeById(vId,
        procedimento.getIdBando());
    AziendaDTO azienda = vAziende.get(0);
    aggiornaDatiAAEPSian(azienda, utenteAbilitazioni);

    Long idAzienda = quadroEJB.getAziendaRichiestaVoltura(
        procedimentoOggetto.getIdProcedimentoOggetto());
    if (idAzienda == null || idAzienda.longValue() == 0)
    {
      idAzienda = testataProcedimento.getIdAzienda();
    }

    MainControlloDTO mainControlloDTO = quadroEJB.callMainControlli(
        procedimentoOggetto.getIdBandoOggetto(),
        procedimentoOggetto.getIdProcedimentoOggetto(),
        idAzienda.longValue(),
        utenteAbilitazioni.getIdUtenteLogin());

    if (mainControlloDTO
        .getRisultato() == IuffiConstants.SQL.RESULT_CODE.ERRORE_CRITICO)
    {
      model.addAttribute("msgErrore",
          "Si è verificato un errore di sistema. Contattare l'assistenza comunicando il seguente messaggio: "
              + mainControlloDTO.getMessaggio());
      return "controlli/esito";
    }

    return "redirect:../cuiuffi114/index.do";
  }
}