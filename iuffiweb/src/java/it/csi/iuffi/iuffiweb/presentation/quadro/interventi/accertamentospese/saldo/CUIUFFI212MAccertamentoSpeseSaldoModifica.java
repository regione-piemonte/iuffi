package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.accertamentospese.saldo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.AccertamentoSpeseDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoSpese;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-212-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi212m")
public class CUIUFFI212MAccertamentoSpeseSaldoModifica
    extends CUIUFFI212AccertamentoSpeseSaldoAbstract
{

  @RequestMapping(value = "/modifica", method = RequestMethod.POST)
  public String controllerModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    String[] idIntervento = request.getParameterValues("id");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);

    HttpSession session = request.getSession();
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaAccertamentoSpese> list = rendicontazioneEAccertamentoSpeseEJB
        .getElencoAccertamentoSpese(idProcedimentoOggetto, ids);
    if (request.getParameter("confermaModificaInterventi") != null)
    {
      if (validateAndUpdate(model, request, list))
      {
        return "redirect:../" + CU_BASE_NAME + "l/index.do";
      }
      else
      {
        model.addAttribute("preferRequest", Boolean.TRUE);
      }
    }
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../" + CU_BASE_NAME + "m/modifica.do");
    return JSP_BASE_PATH + "modificaMultipla";
  }

  private boolean validateAndUpdate(Model model, HttpServletRequest request,
      List<RigaAccertamentoSpese> elenco)
      throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    List<AccertamentoSpeseDTO> list = new ArrayList<AccertamentoSpeseDTO>();
    for (RigaAccertamentoSpese riga : elenco)
    {
      final long idIntervento = riga.getIdIntervento();
      final String nameNote = "note_" + idIntervento;
      String note = request.getParameter(nameNote);
      if (errors.validateFieldMaxLength(note, nameNote, 4000))
      {
        riga.setNote(note);
      }
      final boolean isSpesaSostenutaAttuale = BigDecimal.ZERO
          .compareTo(riga.getSpesaSostenutaAttuale()) < 0;
      if (isSpesaSostenutaAttuale)
      {
        AccertamentoSpeseDTO accertamentoSpeseDTO = new AccertamentoSpeseDTO();
        accertamentoSpeseDTO.setIdIntervento(idIntervento);
        accertamentoSpeseDTO.setIdProcedimentoOggetto(
            getIdProcedimentoOggetto(request.getSession()));
        String nameFlagInterventoCompletato = "flagInterventoCompletato_"
            + idIntervento;
        if (request.getParameter(nameFlagInterventoCompletato) != null)
        {
          accertamentoSpeseDTO
              .setFlagInterventoCompletato(IuffiConstants.FLAGS.SI);
        }
        else
        {
          accertamentoSpeseDTO
              .setFlagInterventoCompletato(IuffiConstants.FLAGS.NO);
        }
        riga.setFlagInterventoCompletato(
            accertamentoSpeseDTO.getFlagInterventoCompletato());
        accertamentoSpeseDTO.setNote(note);

        boolean usaDocumentiSpesa = IuffiConstants.FLAGS.SI
            .equals(riga.getUsaDocumentiSpesa());
        accertamentoSpeseDTO.setUsaDocumentiSpesa(usaDocumentiSpesa);
        if (!usaDocumentiSpesa)
        {
          // Se entra qua dentro è perchè c'è qualcosa che non va, dovrebbe
          // essere possibile modificare solo le
          // righe con spesa sostenuta attuale < 0
          String nameSpeseAccertateAttuali = "speseAccertateAttuali_"
              + idIntervento;
          BigDecimal speseAccertateAttuali = errors
              .validateMandatoryBigDecimalInRange(
                  request.getParameter(nameSpeseAccertateAttuali),
                  nameSpeseAccertateAttuali, 2, BigDecimal.ZERO,
                  riga.getSpesaSostenutaAttuale());
          riga.setSpeseAccertateAttuali(speseAccertateAttuali);
          String nameSpesaRiconosciutaPerCalcolo = "spesaRiconosciutaPerCalcolo_"
              + idIntervento;
          BigDecimal spesaRiconosciutaPerCalcolo = errors
              .validateMandatoryBigDecimalInRange(
                  request.getParameter(nameSpesaRiconosciutaPerCalcolo),
                  nameSpesaRiconosciutaPerCalcolo, 2, BigDecimal.ZERO,
                  speseAccertateAttuali);
          riga.setSpesaRiconosciutaPerCalcolo(spesaRiconosciutaPerCalcolo);
          String nameImportoNonRiconosciutoSanzionabile = "importoNonRiconosciutoSanzionabile_"
              + idIntervento;
          BigDecimal importoNonRiconosciutoSanzionabile = errors
              .validateMandatoryBigDecimalInRange(
                  request.getParameter(nameImportoNonRiconosciutoSanzionabile),
                  nameImportoNonRiconosciutoSanzionabile, 2, BigDecimal.ZERO,
                  riga.getSpesaSostenutaAttuale().subtract(
                      IuffiUtils.NUMBERS.nvl(speseAccertateAttuali)));
          accertamentoSpeseDTO.setImportoAccertato(speseAccertateAttuali);
          accertamentoSpeseDTO
              .setImportoCalcoloContributo(spesaRiconosciutaPerCalcolo);
          if (importoNonRiconosciutoSanzionabile != null)
          {
            accertamentoSpeseDTO
                .setImportoNonRiconosciuto(importoNonRiconosciutoSanzionabile);
            if (spesaRiconosciutaPerCalcolo != null)
            {
              BigDecimal importoDisponibile = riga.getSpesaSostenutaAttuale()
                  .subtract(spesaRiconosciutaPerCalcolo)
                  .subtract(importoNonRiconosciutoSanzionabile);
              accertamentoSpeseDTO.setImportoDisponibile(importoDisponibile);
            }
          }
          if (spesaRiconosciutaPerCalcolo != null)
          {
            BigDecimal contributoCalcolato = spesaRiconosciutaPerCalcolo
                .multiply(riga.getPercentualeContributo(),
                    MathContext.DECIMAL128)
                .divide(new BigDecimal(100), MathContext.DECIMAL128)
                .setScale(2, RoundingMode.HALF_UP);
            accertamentoSpeseDTO.setContributoCalcolato(contributoCalcolato);
          }
        }
        list.add(accertamentoSpeseDTO);
      }
      else
      {
        // Se non entra qua dentro è perchè c'è qualcosa che non va, dovrebbe
        // essere possibile modificare solo le righe
        // con spesa sostenuta attuale < 0
        riga.setSpeseAccertateAttuali(BigDecimal.ZERO);
        riga.setSpesaRiconosciutaPerCalcolo(BigDecimal.ZERO);
        AccertamentoSpeseDTO accertamentoSpeseDTO = new AccertamentoSpeseDTO();
        accertamentoSpeseDTO.setIdIntervento(idIntervento);
        accertamentoSpeseDTO.setIdProcedimentoOggetto(
            getIdProcedimentoOggetto(request.getSession()));
        String nameFlagInterventoCompletato = "flagInterventoCompletato_"
            + idIntervento;
        if (request.getParameter(nameFlagInterventoCompletato) != null)
        {
          accertamentoSpeseDTO
              .setFlagInterventoCompletato(IuffiConstants.FLAGS.SI);
        }
        else
        {
          accertamentoSpeseDTO
              .setFlagInterventoCompletato(IuffiConstants.FLAGS.NO);
        }
        riga.setFlagInterventoCompletato(
            accertamentoSpeseDTO.getFlagInterventoCompletato());
        accertamentoSpeseDTO.setImportoAccertato(BigDecimal.ZERO);
        accertamentoSpeseDTO.setImportoCalcoloContributo(BigDecimal.ZERO);
        accertamentoSpeseDTO.setImportoNonRiconosciuto(BigDecimal.ZERO);
        accertamentoSpeseDTO.setImportoDisponibile(BigDecimal.ZERO);
        accertamentoSpeseDTO.setContributoCalcolato(BigDecimal.ZERO);
        accertamentoSpeseDTO.setNote(note);
        list.add(accertamentoSpeseDTO);
      }
    }
    if (!errors.addToModelIfNotEmpty(model))
    {
      rendicontazioneEAccertamentoSpeseEJB.updateAccertamentoSpeseSaldo(list,
          getLogOperationOggettoQuadroDTO(request.getSession()));
      return true;
    }
    else
    {
      model.addAttribute("preferRequest", Boolean.TRUE);
      return false;
    }
  }

  @RequestMapping(value = "/modifica_multipla", method = RequestMethod.POST)
  public String modificaMultipla(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    String[] idIntervento = request.getParameterValues("idIntervento");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);

    HttpSession session = request.getSession();
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaAccertamentoSpese> list = rendicontazioneEAccertamentoSpeseEJB
        .getElencoAccertamentoSpese(idProcedimentoOggetto, ids);
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../" + CU_BASE_NAME + "m/modifica.do");
    return JSP_BASE_PATH + "modificaMultipla";
  }

  @RequestMapping(value = "/modifica_singola_{idIntervento}", method = RequestMethod.GET)
  public String modificaSingola(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    List<Long> ids = new ArrayList<Long>();
    ids.add(idIntervento);
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaAccertamentoSpese> list = rendicontazioneEAccertamentoSpeseEJB
        .getElencoAccertamentoSpese(idProcedimentoOggetto, ids);
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../" + CU_BASE_NAME + "m/modifica.do");
    return JSP_BASE_PATH + "modificaMultipla";
  }

  @RequestMapping(value = "/confermaModifica", method = RequestMethod.GET)
  public String confermaModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return JSP_BASE_PATH + "confermaModifica";
  }
}
