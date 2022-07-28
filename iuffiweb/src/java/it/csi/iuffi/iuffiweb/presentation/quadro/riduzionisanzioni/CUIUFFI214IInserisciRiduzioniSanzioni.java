package it.csi.iuffi.iuffiweb.presentation.quadro.riduzionisanzioni;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-214-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi214i")
public class CUIUFFI214IInserisciRiduzioniSanzioni extends BaseController
{

  @Autowired
  IQuadroEJB quadroEJB;

  @IsPopup
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String popupNuovaVisita(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    prepareDataForInserimento(request, model);

    return "riduzionisanzioni/popupNuovaRiduzione";
  }

  private void prepareDataForInserimento(HttpServletRequest request,
      Model model) throws InternalUnexpectedException
  {
    final HttpSession session = request.getSession();
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    final List<LivelloDTO> liv = quadroEJB
        .getLivelliProcOggetto(po.getIdProcedimento());
    final List<DecodificaDTO<Long>> livelli = new ArrayList<>();
    for (LivelloDTO l : liv)
      livelli.add(new DecodificaDTO<Long>(l.getIdLivello(), l.getCodice()));
    model.addAttribute("livelli", livelli);

    final List<DecodificaDTO<String>> tipologieSanzioni = quadroEJB
        .getTipologieSanzioni(po.getIdProcedimentoOggetto(), false);
    model.addAttribute("tipologieSanzioniInvestimentoList", tipologieSanzioni);

    if (tipologieSanzioni.size() == 1)
    {
      model.addAttribute("idSanzInv",
          tipologieSanzioni.get(0).getId().split("-"));
    }
    if (livelli.size() == 1)
    {
      model.addAttribute("idLiv", livelli.get(0).getId());
    }
  }

  @IsPopup
  @RequestMapping(value = "/popup_inserisci_riduzione", method = RequestMethod.POST)
  public String popupInserisciRiduzione(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    final HttpSession session = request.getSession();
    Errors errors = new Errors();

    String idOp = request.getParameter("operazione");
    Long idOperazione = null;
    if (idOp != "")
      idOperazione = Long.parseLong(idOp);
    errors.validateMandatory(request.getParameter("operazione"), "operazione");
    String idTipologiaSanzioneInvestimento = request
        .getParameter("idTipologiaSanzioneInvestimento");
    errors.validateMandatory(
        request.getParameter("idTipologiaSanzioneInvestimento"),
        "idTipologiaSanzioneInvestimento");

    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    BigDecimal importoMax = quadroEJB.calcolaImportoMaxRiduzioneSanzione(
        procedimentoOggetto.getIdProcedimentoOggetto());
    BigDecimal importoParziale = quadroEJB
        .calcolaImportoParzialeRiduzioneSanzione(
            procedimentoOggetto.getIdProcedimentoOggetto());

    // if(importoMax.doubleValue() == 0)
    // importoMax =null;
    if (importoParziale == null)
      importoParziale = BigDecimal.ZERO;

    BigDecimal importo = errors.validateMandatoryBigDecimal(
        request.getParameter("importo"), "importo", 2);

    errors.validateMandatoryBigDecimalInRange(importo, "importo",
        BigDecimal.ZERO, importoMax.subtract(importoParziale));
    String note = request.getParameter("note");
    errors.validateFieldLength(request.getParameter("note"), "note", 0, 4000);

    // Se IUF_D_OGGETTO.TIPO_PAGAMENTO_SIGOP not in (‘ACCON’, ‘SALDO’) --
    // Errore
    String tipoPagamentoSigop = quadroEJB.getTipoPagamentoSigopOggetto(
        procedimentoOggetto.getIdProcedimentoOggetto());
    boolean erroreConfigurazione = false;
    if (tipoPagamentoSigop == null
        || (tipoPagamentoSigop.compareTo("ACCON") != 0
            && tipoPagamentoSigop.compareTo("SALDO") != 0))
    {
      erroreConfigurazione = true;
      model.addAttribute("msgErrore",
          "Si è verificato un errore della configurazione del quadro \"Riduzioni e sanzioni\", l'aggiornamento del contributo erogabile è previsto solo per le istruttorie delle domande di acconto e saldo. Non è possibile proseguire con l'operazione.");
    }

    if (!errors.addToModelIfNotEmpty(model) && !erroreConfigurazione)
    {
      Long idDescrizione = null;
      Long idTipologia = null;
      idDescrizione = Long
          .parseLong(idTipologiaSanzioneInvestimento.split("-")[1]);
      idTipologia = Long
          .parseLong(idTipologiaSanzioneInvestimento.split("-")[0]);

      ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
      quadroEJB.inserisciSanzioneRiduzione(idTipologia, idOperazione,
          idDescrizione, note, importo, po.getIdProcedimentoOggetto(),
          tipoPagamentoSigop,
          getLogOperationOggettoQuadroDTO(request.getSession()));
      return "dialog/success";
    }

    prepareDataForInserimento(request, model);
    model.addAttribute("preferRequest", Boolean.TRUE);
    return "riduzionisanzioni/popupNuovaRiduzione";
  }
}
