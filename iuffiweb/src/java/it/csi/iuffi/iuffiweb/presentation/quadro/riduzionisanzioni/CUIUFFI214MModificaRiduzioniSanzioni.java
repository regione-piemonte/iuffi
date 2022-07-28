package it.csi.iuffi.iuffiweb.presentation.quadro.riduzionisanzioni;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.RiduzioniSanzioniDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-214-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi214m")
public class CUIUFFI214MModificaRiduzioniSanzioni extends BaseController
{

  @Autowired
  IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index_{idProcOggSanzione}", method = RequestMethod.GET)
  public String modificaUno(
      @PathVariable("idProcOggSanzione") long idProcOggSanzione, Model model,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    final HttpSession session = request.getSession();
    List<Long> ids = new LinkedList<>();
    ids.add(idProcOggSanzione);
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    prepareDataForInserimento(request, model);
    List<RiduzioniSanzioniDTO> riduzioni = new LinkedList<>();
    List<RiduzioniSanzioniDTO> rid = quadroEJB.getElencoRiduzioniSanzioni(
        procedimentoOggetto.getIdProcedimentoOggetto());
    for (RiduzioniSanzioniDTO r : rid)
      if (r.getIdProcOggSanzione() == idProcOggSanzione)
        riduzioni.add(r);
    model.addAttribute("riduzioni", riduzioni);

    return "riduzionisanzioni/modificaRiduzioniSanzioni";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String modificaList(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<Long> ids = IuffiUtils.LIST
        .toListOfLong(request.getParameterValues("idProcOggSanzione"));
    final HttpSession session = request.getSession();
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    prepareDataForInserimento(request, model);

    List<RiduzioniSanzioniDTO> riduzioni = new LinkedList<>();
    List<RiduzioniSanzioniDTO> rid = quadroEJB.getElencoRiduzioniSanzioni(
        procedimentoOggetto.getIdProcedimentoOggetto());
    for (RiduzioniSanzioniDTO r : rid)
      for (Long id : ids)
        if (r.getIdProcOggSanzione().longValue() == id.longValue())
          riduzioni.add(r);
    model.addAttribute("riduzioni", riduzioni);

    return "riduzionisanzioni/modificaRiduzioniSanzioni";
  }

  @RequestMapping(value = "/confermaModifica", method = RequestMethod.POST)
  public String confermaDatiModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    final HttpSession session = request.getSession();
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    List<Long> ids = IuffiUtils.LIST
        .toListOfLong(request.getParameterValues("idProcOggSanzione"));
    Errors errors = new Errors();
    List<RiduzioniSanzioniDTO> riduzioni = new LinkedList<>();
    // importo delle riduzioni/sanzioni prima della modifica
    BigDecimal importoParzialePrec = quadroEJB
        .calcolaImportoParzialeRiduzioneSanzione(po.getIdProcedimentoOggetto());
    List<RiduzioniSanzioniDTO> riduzioniSuDb = quadroEJB
        .getElencoRiduzioniSanzioni(po.getIdProcedimentoOggetto());

    for (Long id : ids)
    {
      // valido
      riduzioni.add(validateRiduzioneSanzione(id, errors, request, po));

      // cerco nelle riduzionisanzioni già inserite e, se sono tra quelle che
      // sto modificando
      // tolgo il loro importo da quello parziale su db
      if (importoParzialePrec != null)
        for (RiduzioniSanzioniDTO r : riduzioniSuDb)
        {
          if (r.getIdProcOggSanzione().longValue() == id.longValue())
            importoParzialePrec = importoParzialePrec
                .subtract(r.getImportoFirstRecord());
        }

    }

    // se non ci sono errori di validazione dei campi controllo che la sommad
    // degli importi inseriti non superi il valore massimo possibile
    if (errors.isEmpty())
    {
      BigDecimal importoParzialeInserito = new BigDecimal(0);
      // aggiungo ad importoParzialeInserito tutti quelli che l'utente ha
      // inserito nel form
      for (RiduzioniSanzioniDTO r : riduzioni)
      {
        importoParzialeInserito = importoParzialeInserito.add(r.getImporto());
      }

      // importo totale massimo che la somma di tutte le riduzioni/sanzioni non
      // può superare
      BigDecimal importoMax = quadroEJB
          .calcolaImportoMaxRiduzioneSanzione(po.getIdProcedimentoOggetto());
      if (importoMax.doubleValue() == 0)
        importoMax = null;

      boolean addErrorImporto = false;
      if (importoMax != null && importoParzialePrec != null
          && importoParzialeInserito != null)
        if (importoParzialeInserito
            .compareTo(importoMax.subtract(importoParzialePrec)) > 0)
          addErrorImporto = true;

      if (addErrorImporto)
        for (Long id : ids)
        {
          errors.addError("importo_" + id,
              "Il totale degli importi inseriti non può superare il valore: "
                  + IuffiUtils.FORMAT.formatDecimal2(
                      importoMax.subtract(importoParzialePrec)));
        }
    }

    // Se IUF_D_OGGETTO.TIPO_PAGAMENTO_SIGOP not in (‘ACCON’, ‘SALDO’) --
    // Errore
    String tipoPagamentoSigop = quadroEJB
        .getTipoPagamentoSigopOggetto(po.getIdProcedimentoOggetto());
    boolean erroreConfigurazione = false;
    if (tipoPagamentoSigop == null
        || (tipoPagamentoSigop.compareTo("ACCON") != 0
            && tipoPagamentoSigop.compareTo("SALDO") != 0))
    {
      erroreConfigurazione = true;
      model.addAttribute("msgErrore",
          "Si è verificato un errore della configurazione del quadro \"Riduzioni e sanzioni\", l'aggiornamento del contributo erogabile è previsto solo per le istruttorie delle domande di acconto e saldo. Non è possibile proseguire con l'operazione.");
    }

    if (!errors.isEmpty() || erroreConfigurazione)
    {
      if (!errors.isEmpty())
        model.addAttribute("errors", errors);
      prepareDataForInserimento(request, model);
      List<RiduzioniSanzioniDTO> riduzz = new LinkedList<>();
      List<RiduzioniSanzioniDTO> rid = quadroEJB
          .getElencoRiduzioniSanzioni(po.getIdProcedimentoOggetto());
      for (RiduzioniSanzioniDTO r : rid)
        for (Long id : ids)
          if (r.getIdProcOggSanzione().longValue() == id.longValue())
            riduzz.add(r);
      model.addAttribute("riduzioni", riduzz);

      model.addAttribute("preferRequest", true);
      model.addAttribute("ids", ids);
      return "riduzionisanzioni/modificaRiduzioniSanzioni";
    }

    quadroEJB.modificaRiduzioneSanzione(riduzioni,
        getLogOperationOggettoQuadroDTO(request.getSession()),
        po.getIdProcedimentoOggetto(), tipoPagamentoSigop);

    return "redirect:../cuiuffi214l/index.do";
  }

  private RiduzioniSanzioniDTO validateRiduzioneSanzione(Long id, Errors errors,
      HttpServletRequest request, ProcedimentoOggetto po)
      throws InternalUnexpectedException
  {

    String idOp = request.getParameter("operazione_" + id);
    Long idOperazione = null;
    if (idOp != null && idOp != "")
      idOperazione = Long.parseLong(idOp);

    String isSplittedStr = request.getParameter("isSplitted_" + id);
    boolean isSplitted = false;
    if (isSplittedStr != null && isSplittedStr.compareTo("true") == 0)
      isSplitted = true;

    String idTipologiaSanzioneInvestimento = request
        .getParameter("idTipologiaSanzioneInvestimento_" + id);
    if (!isSplitted)
      errors.validateMandatory(
          request.getParameter("idTipologiaSanzioneInvestimento_" + id),
          "idTipologiaSanzioneInvestimento_" + id);

    String note = request.getParameter("note_" + id);
    errors.validateFieldLength(request.getParameter("note_" + id), "note_" + id,
        0, 4000);

    String noteB = request.getParameter("noteB_" + id);
    errors.validateFieldLength(request.getParameter("noteB_" + id),
        "noteB_" + id, 0, 4000);

    RiduzioniSanzioniDTO r = new RiduzioniSanzioniDTO();
    r.setIdProcOggSanzione(id);
    if (idTipologiaSanzioneInvestimento != null
        && idTipologiaSanzioneInvestimento != "")
      r.setIdDescrizione(
          Long.parseLong(idTipologiaSanzioneInvestimento.split("-")[1]));
    r.setIdOperazione(idOperazione);
    if (idTipologiaSanzioneInvestimento != null
        && idTipologiaSanzioneInvestimento != "")
      r.setIdTipologia(
          Long.parseLong(idTipologiaSanzioneInvestimento.split("-")[0]));

    BigDecimal importo = BigDecimal.ZERO;
    BigDecimal importoB = BigDecimal.ZERO;

    // l'operazione è obbligatoria solo se l'idTipologia non è 3
    if (r.getIdTipologia() != null && r.getIdTipologia() != 3)
    {
      errors.validateMandatory(request.getParameter("operazione_" + id),
          "operazione_" + id);
      importo = errors.validateMandatoryBigDecimal(
          request.getParameter("importo_" + id), "importo_" + id, 2);
    }
    else
    {
      if (isSplitted)
      {
        importo = errors.validateOptionalBigDecimal(
            request.getParameter("importo_" + id), "importo_" + id, 2);
        importoB = errors.validateOptionalBigDecimal(
            request.getParameter("importoB_" + id), "importoB_" + id, 2);
      }
    }

    BigDecimal importoTOTSplit = null;
    if (isSplitted)
      r.setIdTipologia(3l);
    // se la tipologia è di tipo 3, l'idOperazione e l'importo non sono
    // modificabili, dunque prendo i valori precedenti (se è UNSPLITTED)
    if (r.getIdTipologia() != null && r.getIdTipologia() == 3)
    {
      List<RiduzioniSanzioniDTO> rid = quadroEJB
          .getElencoRiduzioniSanzioni(po.getIdProcedimentoOggetto());
      for (RiduzioniSanzioniDTO rr : rid)
        if (rr.getIdProcOggSanzione().longValue() == id.longValue())
        {
          importoTOTSplit = rr.getImporto();
          r.setIdOperazione(rr.getIdOperazione());
          r.setIdTipologia(rr.getIdTipologia());
          if (isSplitted)
          {
            r.setSplitted(true);
            r.setIdProcOggSanzioneSecondRecordAfterSplit(
                rr.getIdProcOggSanzioneSecondRecordAfterSplit());
            r.setIdDescrizioneSecondRecordAfterSplit(
                rr.getIdDescrizioneSecondRecordAfterSplit());
            r.setIdDescrizione(rr.getIdDescrizione());
          }
          else
          {
            r.setImporto(rr.getImporto());
            r.setImportoFirstRecord(rr.getImporto());
          }
        }
    }
    else
    {
      r.setImporto(importo);
      r.setImportoFirstRecord(importo);

    }

    r.setNote(note);
    r.setNoteB(noteB);

    if (importo == null)
      importo = BigDecimal.ZERO;

    if (isSplitted && importo != null && importoB != null)
    {
      r.setImportoFirstRecord(importo);
      r.setImportoSecondRecordAfterSplit(importoB);
      if (importo != null && importoB != null)
      {
        if (importoTOTSplit.compareTo(importo.add(importoB)) != 0)
        {
          errors.addError("importo_" + id,
              "L'importo complessivo differisce da quello calcolato dal sistema, pari a  "
                  + importoTOTSplit);
          errors.addError("importoB_" + id,
              "L'importo complessivo differisce da quello calcolato dal sistema, pari a  "
                  + importoTOTSplit);
        }

        if (importo.compareTo(BigDecimal.ZERO) <= 0
            || importoB.compareTo(BigDecimal.ZERO) <= 0)
        {
          errors.addError("importo_" + id,
              "Gli importi inseriti devono essere maggiori di zero.");
          errors.addError("importoB_" + id,
              "Gli importi inseriti devono essere maggiori di zero.");
        }
      }
      r.setImporto(importoB.add(importo));
    }
    /*
     * else { r.setImporto(importo); r.setImportoFirstRecord(importo); }
     */

    return r;

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
    final List<DecodificaDTO<String>> tipologieSanzioniAutomatiche = quadroEJB
        .getTipologieSanzioni(po.getIdProcedimentoOggetto(), true);

    model.addAttribute("tipologieSanzioniInvestimentoList", tipologieSanzioni);
    model.addAttribute("tipologieSanzioniInvestimentoAutomaticheList",
        tipologieSanzioniAutomatiche);

  }

  @RequestMapping(value = "/split_{idProcOggSanzione}", method = RequestMethod.GET)
  @IsPopup
  public String split(@PathVariable("idProcOggSanzione") long idProcOggSanzione,
      Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    final HttpSession session = request.getSession();
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    RiduzioniSanzioniDTO riduzione = quadroEJB.getRiduzioneSanzione(
        idProcOggSanzione, procedimentoOggetto.getIdProcedimentoOggetto());
    model.addAttribute("riduzione", riduzione);

    // mi serve la descrizione da assegnare al secondo record
    String descrizioneS01 = quadroEJB.getDescrizioneSanzioneSplit("S01");
    String descrizioneS02 = quadroEJB.getDescrizioneSanzioneSplit("S02");
    String descEsistente = riduzione.getTipologia() + " - "
        + riduzione.getDescrizione();
    if (descEsistente.compareTo(descrizioneS01) == 0)
      model.addAttribute("descrizione2", descrizioneS02);
    else
      model.addAttribute("descrizione2", descrizioneS01);

    return "riduzionisanzioni/split";
  }

  @RequestMapping(value = "/confermaSplit_{idProcOggSanzione}", method = RequestMethod.POST)
  @IsPopup
  public String confermaSplit_(
      @PathVariable("idProcOggSanzione") long idProcOggSanzione, Model model,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    final HttpSession session = request.getSession();
    Errors errors = new Errors();

    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    RiduzioniSanzioniDTO riduzione = quadroEJB.getRiduzioneSanzione(
        idProcOggSanzione, procedimentoOggetto.getIdProcedimentoOggetto());

    BigDecimal importoA = errors.validateMandatoryBigDecimal(
        request.getParameter("importoA"), "importoA", 2);
    BigDecimal importoB = errors.validateMandatoryBigDecimal(
        request.getParameter("importoB"), "importoB", 2);

    String noteA = request.getParameter("noteA");
    errors.validateFieldLength(request.getParameter("noteA"), "noteA", 0, 4000);
    String noteB = request.getParameter("noteB");
    errors.validateFieldLength(request.getParameter("noteB"), "noteB", 0, 4000);

    if (importoA != null && importoB != null)
    {
      if (riduzione.getImporto().compareTo(importoA.add(importoB)) != 0)
      {
        errors.addError("importoA",
            "L'importo complessivo differisce da quello calcolato dal sistema, pari a  "
                + riduzione.getImporto());
        errors.addError("importoB",
            "L'importo complessivo differisce da quello calcolato dal sistema, pari a  "
                + riduzione.getImporto());
      }

      if (importoA.compareTo(BigDecimal.ZERO) <= 0
          || importoB.compareTo(BigDecimal.ZERO) <= 0)
      {
        errors.addError("importoA",
            "Gli importi inseriti devono essere maggiori di zero.");
        errors.addError("importoB",
            "Gli importi inseriti devono essere maggiori di zero.");
      }
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("riduzione", riduzione);
      // mi serve la descrizione da assegnare al secondo record
      String descrizioneS01 = quadroEJB.getDescrizioneSanzioneSplit("S01");
      String descrizioneS02 = quadroEJB.getDescrizioneSanzioneSplit("S02");
      String descEsistente = riduzione.getTipologia() + " - "
          + riduzione.getDescrizione();
      if (descEsistente.compareTo(descrizioneS01) == 0)
        model.addAttribute("descrizione2", descrizioneS02);
      else
        model.addAttribute("descrizione2", descrizioneS01);

      model.addAttribute("preferRequest", true);

      return "riduzionisanzioni/split";
    }

    // eseguo lo split
    String descrizioneS01 = quadroEJB.getDescrizioneSanzioneSplit("S01");
    String descEsistente = riduzione.getTipologia() + " - "
        + riduzione.getDescrizione();
    if (descEsistente.compareTo(descrizioneS01) == 0)
      quadroEJB.splitRiduzioneSanzione(riduzione, idProcOggSanzione,
          getLogOperationOggettoQuadroDTO(session), importoA, importoB, noteA,
          noteB, "S02");
    else
      quadroEJB.splitRiduzioneSanzione(riduzione, idProcOggSanzione,
          getLogOperationOggettoQuadroDTO(session), importoA, importoB, noteA,
          noteB, "S01");

    return "dialog/success";

  }

  @RequestMapping(value = "/confermaUnsplit_{idProcOggSanzione}", method = RequestMethod.POST)
  public String confermaUnsplit(
      @PathVariable("idProcOggSanzione") long idProcOggSanzione, Model model,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    final HttpSession session = request.getSession();

    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);

    List<RiduzioniSanzioniDTO> riduzioni = new LinkedList<>();
    List<RiduzioniSanzioniDTO> rid = quadroEJB.getElencoRiduzioniSanzioni(
        procedimentoOggetto.getIdProcedimentoOggetto());
    for (RiduzioniSanzioniDTO r : rid)
      if (r.getIdProcOggSanzione() == idProcOggSanzione)
        riduzioni.add(r);

    if (!riduzioni.isEmpty())
      quadroEJB.unsplitRiduzioneSanzione(idProcOggSanzione,
          getLogOperationOggettoQuadroDTO(session), riduzioni.get(0));

    return "redirect:../cuiuffi214l/index.do";
  }

  @RequestMapping(value = "/unsplit_{idProcOggSanzione}", method = RequestMethod.GET)
  public String unsplit(
      @PathVariable("idProcOggSanzione") long idProcOggSanzione, Model model,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {

    model.addAttribute("idProcOggSanzione", idProcOggSanzione);
    return "riduzionisanzioni/confermaUnsplit";
  }

}
