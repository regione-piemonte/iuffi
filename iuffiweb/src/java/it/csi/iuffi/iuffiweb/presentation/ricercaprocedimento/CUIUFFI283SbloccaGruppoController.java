package it.csi.iuffi.iuffiweb.presentation.ricercaprocedimento;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.ProcedimentoGruppoVO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi283")
@IuffiSecurity(value = "CU-IUFFI-283", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI283SbloccaGruppoController extends BaseController
{

  @Autowired
  private IRicercaEJB ricercaEJB = null;

  @RequestMapping(value = "/popupsblocca_{idGruppoOggetto}_{idProcedimento}_{codRaggruppamento}", method = RequestMethod.GET)
  @IsPopup
  public String popupSblocca(Model model,
      HttpSession session,
      @PathVariable("idGruppoOggetto") long idGruppoOggetto,
      @PathVariable("idProcedimento") long idProcedimento,
      @PathVariable("codRaggruppamento") long codRaggruppamento)
      throws InternalUnexpectedException
  {
    List<ProcedimentoGruppoVO> elencoGruppi = ricercaEJB
        .getElencoProcedimentoGruppo(idProcedimento, codRaggruppamento);
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    boolean isBeneficiarioOCAA = IuffiUtils.PAPUASERV.isAttoreAbilitato(
        utenteAbilitazioni,
        IuffiConstants.PAPUA.ATTORE.BENEFICIARIO) ||
        IuffiUtils.PAPUASERV.isAttoreAbilitato(utenteAbilitazioni,
            IuffiConstants.PAPUA.ATTORE.INTERMEDIARIO_CAA);
    List<GruppoOggettoDTO> listGruppiOggetto = ricercaEJB.getElencoOggetti(
        idProcedimento,
        Arrays.asList(utenteAbilitazioni.getMacroCU()), isBeneficiarioOCAA, utenteAbilitazioni.getIdProcedimento());
    String title = "";
    for (GruppoOggettoDTO gruppo : listGruppiOggetto)
    {
      if (gruppo.getIdGruppoOggetto() == idGruppoOggetto)
      {
        title = "Il gruppo " + gruppo.getDescrizione();
      }
    }

    for (ProcedimentoGruppoVO item : elencoGruppi)
    {
      if (item.getDataFine() == null)
      {
        title += " è bloccato per l'operatività dal " + item.getDataInizioStr()
            + ". Se si vuole sbloccare il gruppo inserire le motivazioni e premere conferma.";
      }
    }
    model.addAttribute("title", title);
    return "ricercaprocedimento/popupSbloccoGruppo";
  }

  @RequestMapping(value = "/popupsblocca_{idGruppoOggetto}_{idProcedimento}_{codRaggruppamento}", method = RequestMethod.POST)
  public String popupSbloccaPOs(Model model,
      HttpSession session,
      @PathVariable("idGruppoOggetto") long idGruppoOggetto,
      @PathVariable("idProcedimento") long idProcedimento,
      @PathVariable("codRaggruppamento") long codRaggruppamento,
      @RequestParam(value = "note", required = false) String note)
      throws InternalUnexpectedException
  {

    model.addAttribute("preferRequest", Boolean.TRUE);

    Errors errors = new Errors();
    if (note != null)
    {
      errors.validateFieldMaxLength(note, "note", 4000);
    }

    model.addAttribute("errors", errors);

    if (!errors.isEmpty())
    {
      return popupSblocca(model, session, idGruppoOggetto, idProcedimento,
          codRaggruppamento);
    }

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    try
    {
      ricercaEJB.sbloccaGruppoOggetto(idProcedimento, codRaggruppamento, note,
          utenteAbilitazioni.getIdUtenteLogin());
    }
    catch (ApplicationException e)
    {
      errors.addError("error", e.getMessage());
      model.addAttribute("errors", errors);
      return popupSblocca(model, session, idGruppoOggetto, idProcedimento,
          codRaggruppamento);
    }

    return "redirect:popupsuccess.do";
  }

  @RequestMapping(value = "/popupsuccess", method = RequestMethod.GET)
  @ResponseBody
  public String popupSuccess()
  {
    return "success";
  }

}
