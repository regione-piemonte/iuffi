package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.accertamentospese.saldo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DocumentoAllegatoDownloadDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InterventoAccertamentoDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoSpese;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-212-R", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi212r")
public class CUIUFFI212RAccertamentoMedianteDocumentiSpesa
    extends CUIUFFI212AccertamentoSpeseSaldoAbstract
{

  @RequestMapping(value = "/modifica", method = RequestMethod.POST)
  public String controllerModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    if (request.getParameter("confermaModificaRendicontazione") != null)
    {
      if (validateAndUpdate(model, request))
      {
        return "redirect:../" + CU_BASE_NAME + "l/index.do";
      }
      else
      {
        model.addAttribute("preferRequest", Boolean.TRUE);
      }
    }
    model.addAttribute("action", "../" + CU_BASE_NAME + "r/modifica.do");
    addInfoRendicontazioneIVA(model,
        getIdProcedimentoOggetto(request.getSession()));
    return JSP_BASE_PATH + "accertamentoDocumentiMultipla";
  }

  private boolean validateAndUpdate(Model model, HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    HttpSession session = request.getSession();
    Errors errors = new Errors();

    Map<String, Object> common = getCommonFromSession("CU-IUFFI-212-R", session,
        true);
    String[] idIntervento = request.getParameterValues("idIntervento");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);
    List<RigaAccertamentoSpese> elenco = rendicontazioneEAccertamentoSpeseEJB
        .getElencoAccertamentoSpese(getIdProcedimentoOggetto(session), ids);
    Map<Long, InterventoAccertamentoDocumentiSpesaDTO> interventi = getAccertamentoDocumentiSpesaPerIntervento(
        getIdProcedimentoOggetto(session), ids, elenco);
    common.put("mapModificaRendicontazioneDocumenti", interventi);
    Map<Long, RigaAccertamentoSpese> mapAccertamento = new HashMap<>();
    for (RigaAccertamentoSpese riga : elenco)
    {
      mapAccertamento.put(riga.getIdIntervento(), riga);
    }

    for (InterventoAccertamentoDocumentiSpesaDTO intervento : interventi
        .values())
    {
      String nameFlagInterventoCompletato = "flagInterventoCompletato_"
          + intervento.getIdIntervento();
      if (request.getParameter(nameFlagInterventoCompletato) != null)
      {
        intervento.setFlagInterventoCompletato(IuffiConstants.FLAGS.SI);
      }
      else
      {
        intervento.setFlagInterventoCompletato(IuffiConstants.FLAGS.NO);
      }

      String nameImportoNonRiconosciutoSanzionabile = "importoNonRiconosciutoSanzionabile";
      final BigDecimal spesaRendicontataAttuale = IuffiUtils.NUMBERS
          .nvl(intervento.getSpesaRendicontataAttuale());
      final boolean isSpesaRendicontataAttualeMaggioreZero = BigDecimal.ZERO
          .compareTo(spesaRendicontataAttuale) < 0;

      BigDecimal importoNonRiconosciutoSanzionabile = null;
      if (isSpesaRendicontataAttualeMaggioreZero)
      {
        importoNonRiconosciutoSanzionabile = errors.validateMandatoryBigDecimal(
            request.getParameter(nameImportoNonRiconosciutoSanzionabile),
            nameImportoNonRiconosciutoSanzionabile,
            2);
      }
      else
      {
        importoNonRiconosciutoSanzionabile = BigDecimal.ZERO;
      }
      RigaAccertamentoSpese rigaAccertamento = mapAccertamento
          .get(intervento.getIdIntervento());
      intervento.setSpesaAmmessa(rigaAccertamento.getSpesaAmmessa());
      intervento.setSpesaRendicontataAttuale(
          rigaAccertamento.getSpesaSostenutaAttuale());
      if (importoNonRiconosciutoSanzionabile != null)
      {
        intervento.setImportoNonRiconosciutoSanzionabile(
            importoNonRiconosciutoSanzionabile);
      }
      final String nameNote = "note_" + intervento.getIdIntervento();
      String note = request.getParameter(nameNote);
      if (errors.validateOptionalFieldMaxLength(note, 4000, nameNote))
      {
        intervento.setNote(note);
      }
      BigDecimal totImportoCalcoloContributo = BigDecimal.ZERO;
      BigDecimal totImportoAccertato = BigDecimal.ZERO;
      BigDecimal totImportoNonRiconosciuto = BigDecimal.ZERO;
      BigDecimal totImportoDisponibile = BigDecimal.ZERO;

      for (RigaAccertamentoDocumentiSpesaDTO riga : intervento
          .getAccertamento())
      {
        final long idDocumentoSpesaInterven = riga
            .getIdDocumentoSpesaInterven();

        final boolean isSpesaSostenutaAttuale = BigDecimal.ZERO
            .compareTo(riga.getImportoRendicontato()) < 0;
        if (isSpesaSostenutaAttuale)
        {

          String nameimportoAccertato = "importoAccertato_"
              + idDocumentoSpesaInterven;
          String nameimportoCalcoloContributo = "importoCalcoloContributo_"
              + idDocumentoSpesaInterven;
          String nameimportoNonRiconosciuto = "importoNonRiconosciuto_"
              + idDocumentoSpesaInterven;
          String nameimportoDisponibile = "importoDisponibile_"
              + idDocumentoSpesaInterven;

          String importoAccertato = request.getParameter(nameimportoAccertato);
          String importoCalcoloContributo = request
              .getParameter(nameimportoCalcoloContributo);
          String importoNonRiconosciuto = request
              .getParameter(nameimportoNonRiconosciuto);
          String importoDisponibile = request
              .getParameter(nameimportoDisponibile);

          BigDecimal bdimportoAccertato = errors
              .validateMandatoryBigDecimalInRange(
                  importoAccertato,
                  nameimportoAccertato, 2, BigDecimal.ZERO,
                  riga.getImportoRendicontato());
          if (bdimportoAccertato != null)
          {
            totImportoAccertato = IuffiUtils.NUMBERS.add(totImportoAccertato,
                bdimportoAccertato);
            riga.setImportoAccertato(bdimportoAccertato);
          }
          else
          {
            riga.setImportoAccertato(importoAccertato);
          }
          BigDecimal bdimportoCalcoloContributo = errors
              .validateMandatoryBigDecimalInRange(importoCalcoloContributo,
                  nameimportoCalcoloContributo, 2, BigDecimal.ZERO,
                  bdimportoAccertato);
          if (bdimportoCalcoloContributo != null)
          {
            totImportoCalcoloContributo = IuffiUtils.NUMBERS
                .add(totImportoCalcoloContributo, bdimportoCalcoloContributo);
            riga.setImportoCalcoloContributo(bdimportoCalcoloContributo);
          }
          else
          {
            riga.setImportoCalcoloContributo(importoCalcoloContributo);
          }
          BigDecimal bdimportoNonRiconosciuto = errors
              .validateMandatoryBigDecimalInRange(importoNonRiconosciuto,
                  nameimportoNonRiconosciuto, 2, BigDecimal.ZERO,
                  riga.getImportoRendicontato()
                      .subtract(IuffiUtils.NUMBERS.nvl(bdimportoAccertato)));
          if (bdimportoNonRiconosciuto != null)
          {
            totImportoNonRiconosciuto = IuffiUtils.NUMBERS
                .add(totImportoNonRiconosciuto, bdimportoNonRiconosciuto);
            riga.setImportoNonRiconosciuto(bdimportoNonRiconosciuto);
          }
          else
          {
            riga.setImportoNonRiconosciuto(importoNonRiconosciuto);
          }
          BigDecimal bdimportoDisponibile = errors.validateMandatoryBigDecimal(
              importoDisponibile,
              nameimportoDisponibile, 2);
          if (bdimportoDisponibile != null)
          {
            totImportoDisponibile = IuffiUtils.NUMBERS
                .add(totImportoDisponibile, bdimportoDisponibile);
            riga.setImportoDisponibile(bdimportoDisponibile);
          }
          else
          {
            riga.setImportoDisponibile(importoDisponibile);
          }
          if (errors.get(nameimportoNonRiconosciuto) == null
              && bdimportoNonRiconosciuto != null
              && bdimportoAccertato != null
              && errors.get(nameimportoAccertato) == null)
          {
            errors.validateMandatoryBigDecimalInRange(bdimportoNonRiconosciuto,
                nameimportoNonRiconosciuto, BigDecimal.ZERO,
                IuffiUtils.NUMBERS.subtract(riga.getImportoRendicontato(),
                    bdimportoAccertato));
          }
          if (errors.get(nameimportoDisponibile) == null
              && bdimportoDisponibile != null
              && errors.get(nameimportoCalcoloContributo) == null
              && bdimportoCalcoloContributo != null)
          {
            errors.validateMandatoryBigDecimalInRange(bdimportoDisponibile,
                nameimportoDisponibile, BigDecimal.ZERO,
                IuffiUtils.NUMBERS.subtract(riga.getImportoRendicontato(),
                    bdimportoCalcoloContributo));
          }
          if (errors.get(nameimportoNonRiconosciuto) == null
              && bdimportoNonRiconosciuto != null
              && errors.get(nameimportoDisponibile) == null
              && bdimportoDisponibile != null
              && errors.get(nameimportoCalcoloContributo) == null
              && bdimportoCalcoloContributo != null)
          {
            BigDecimal somma = IuffiUtils.NUMBERS
                .add(bdimportoNonRiconosciuto, bdimportoDisponibile);
            BigDecimal spesa = IuffiUtils.NUMBERS.subtract(
                riga.getImportoRendicontato(), bdimportoCalcoloContributo);
            if (somma.compareTo(spesa) > 0)
            {
              final String errorMessage = "La somma di 'Importo non riconosciuto sanzionabile' e di 'Importo non riconosciuto non sanzionabile' non può superare la differenza tra la 'Spesa rendicontata attuale' e la 'Spesa riconosciuta per il calcolo del contributo'";
              errors.addError(nameimportoNonRiconosciuto, errorMessage);
              errors.addError(nameimportoDisponibile, errorMessage);
            }
          }
          riga.setErrorMessageImportoAccertato(
              errors.get(nameimportoAccertato));
          riga.setErrorMessageImportoCalcoloContributo(
              errors.get(nameimportoCalcoloContributo));
          riga.setErrorMessageImportoNonRiconosciuto(
              errors.get(nameimportoNonRiconosciuto));
          riga.setErrorMessageImportoDisponibile(
              errors.get(nameimportoDisponibile));
        }
        else
        {
          riga.setImportoAccertato(BigDecimal.ZERO);
          riga.setImportoCalcoloContributo(BigDecimal.ZERO);
          riga.setImportoNonRiconosciuto(BigDecimal.ZERO);
          riga.setImportoDisponibile(BigDecimal.ZERO);
        }
      }
      if (isSpesaRendicontataAttualeMaggioreZero)
      {
        if (importoNonRiconosciutoSanzionabile != null)
        {
          errors.validateMandatoryBigDecimalInRange(importoNonRiconosciutoSanzionabile,
              nameImportoNonRiconosciutoSanzionabile,
              BigDecimal.ZERO, rigaAccertamento.getSpesaSostenutaAttuale()
                  .subtract(totImportoAccertato));
          BigDecimal importoNonRiconosciutoNonSanzionabile = rigaAccertamento
              .getSpesaSostenutaAttuale()
              .subtract(totImportoCalcoloContributo)
              .subtract(importoNonRiconosciutoSanzionabile);
          intervento.setImportoNonRiconosciutoNonSanzionabile(
              importoNonRiconosciutoNonSanzionabile);
          if (errors.size() == 0)
          {
            final boolean erroreImportoNonRiconosciuto = totImportoNonRiconosciuto
                .compareTo(importoNonRiconosciutoSanzionabile) != 0;
            final boolean erroreImportoNonRiconosciutoNonSanzionabile = totImportoDisponibile
                .compareTo(importoNonRiconosciutoNonSanzionabile) != 0;
            if (erroreImportoNonRiconosciuto
                || erroreImportoNonRiconosciutoNonSanzionabile)
            {
              String error = null;
              if (erroreImportoNonRiconosciuto == erroreImportoNonRiconosciutoNonSanzionabile)
              {
                error = "L'Importo non riconosciuto sanzionabile e l'importo non riconosciuto non sanzionabile definiti a livello di intervento non corrispondono ai totali definiti a livello di documenti";
              }
              else
              {
                if (erroreImportoNonRiconosciuto)
                {
                  error = "L'Importo non riconosciuto sanzionabile definito a livello di intervento non corrisponde al totale definito a livello di documenti";
                }
                else
                {
                  if (erroreImportoNonRiconosciutoNonSanzionabile)
                  {
                    error = "L'Importo non riconosciuto non sanzionabile definito a livello di intervento non corrisponde al totale definito a livello di documenti";
                  }
                }
              }
              if (error != null)
              {
                errors.addError("error", error);
              }
            }
          }
        }
      }
      else
      {
        intervento.setImportoNonRiconosciutoNonSanzionabile(BigDecimal.ZERO);
        intervento.setImportoNonRiconosciutoSanzionabile(BigDecimal.ZERO);
      }
    }

    if (!errors.addToModelIfNotEmpty(model))
    {
      try
      {
        final String warningConfirmed = request
            .getParameter("warningConfirmed");
        if (!"true".equals(warningConfirmed))
        {
          model.addAttribute("warningConfirmed", Boolean.FALSE);
        }
        else
        {
          rendicontazioneEAccertamentoSpeseEJB
              .updateAccertamentoSpeseSaldoDocumenti(interventi,
                  getLogOperationOggettoQuadroDTO(session));
          clearCommonInSession(session);
          return true;
        }
      }
      catch (ApplicationException e)
      {
        errors.addError("error", e.getMessage());
        errors.addToModel(model);
      }
    }
    model.addAttribute("interventi", interventi);
    saveCommonInSession(common, session);
    return false;

  }

  @RequestMapping(value = "/accertamento_documenti_singola_{idIntervento}", method = RequestMethod.GET)
  public String modificaSingola(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    List<Long> ids = new ArrayList<Long>();
    ids.add(idIntervento);
    List<RigaAccertamentoSpese> elenco = rendicontazioneEAccertamentoSpeseEJB
        .getElencoAccertamentoSpese(getIdProcedimentoOggetto(session), ids);
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-212-R", session,
        true);
    Map<Long, InterventoAccertamentoDocumentiSpesaDTO> interventi = getAccertamentoDocumentiSpesaPerIntervento(
        getIdProcedimentoOggetto(session), ids, elenco);
    common.put("mapModificaRendicontazioneDocumenti", interventi);
    saveCommonInSession(common, session);
    model.addAttribute("interventi", interventi);
    model.addAttribute("action", "../" + CU_BASE_NAME + "r/modifica.do");
    addInfoRendicontazioneIVA(model,
        getIdProcedimentoOggetto(request.getSession()));
    return JSP_BASE_PATH + "accertamentoDocumentiMultipla";
  }

  private Map<Long, InterventoAccertamentoDocumentiSpesaDTO> getAccertamentoDocumentiSpesaPerIntervento(
      long idProcedimentoOggetto, List<Long> ids,
      List<RigaAccertamentoSpese> elenco) throws InternalUnexpectedException
  {
    Map<Long, InterventoAccertamentoDocumentiSpesaDTO> interventi = rendicontazioneEAccertamentoSpeseEJB
        .getAccertamentoDocumentiSpesaPerIntervento(idProcedimentoOggetto, ids);

    if (interventi != null && interventi.size() > 0)
    {
      for (RigaAccertamentoSpese item : elenco)
      {
        final InterventoAccertamentoDocumentiSpesaDTO intervento = interventi
            .get(item.getIdIntervento());
        if (intervento != null)
        {
          intervento
              .setFlagInterventoCompletato(item.getFlagInterventoCompletato());
          intervento.setImportoNonRiconosciutoSanzionabile(
              item.getImportoNonRiconosciuto());
          intervento.setImportoNonRiconosciutoNonSanzionabile(
              item.getImportoRispendibile());
          intervento.setSpesaAmmessa(item.getSpesaAmmessa());
          intervento
              .setSpesaRendicontataAttuale(item.getSpesaSostenutaAttuale());
          intervento.setNote(item.getNote());
        }
      }
    }
    else
    {
      RigaAccertamentoSpese item = elenco.get(0);
      if (item != null)
      {
        final InterventoAccertamentoDocumentiSpesaDTO intervento = new InterventoAccertamentoDocumentiSpesaDTO();
        intervento
            .setFlagInterventoCompletato(item.getFlagInterventoCompletato());
        intervento.setImportoNonRiconosciutoSanzionabile(
            item.getImportoNonRiconosciuto());
        intervento.setImportoNonRiconosciutoNonSanzionabile(
            item.getImportoRispendibile());
        intervento.setSpesaAmmessa(item.getSpesaAmmessa());
        intervento.setSpesaRendicontataAttuale(item.getSpesaSostenutaAttuale());
        intervento.setNote(item.getNote());
        final Long idIntervento = ids.get(0);
        intervento.setIdIntervento(idIntervento);
        intervento.setAccertamento(new ArrayList<>());
        interventi.put(idIntervento, intervento);
      }
    }
    return interventi;
  }

  @RequestMapping(value = "/json/accertamento_{idIntervento}", produces = "application/json")
  @ResponseBody
  @SuppressWarnings("unchecked")
  public List<RigaAccertamentoDocumentiSpesaDTO> elenco_json(Model model,
      HttpSession session,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-212-R", session,
        true);
    Map<Long, InterventoAccertamentoDocumentiSpesaDTO> interventi = (Map<Long, InterventoAccertamentoDocumentiSpesaDTO>) common
        .get("mapModificaRendicontazioneDocumenti");
    if (interventi != null)
    {
      InterventoAccertamentoDocumentiSpesaDTO intervento = interventi
          .get(idIntervento);
      if (intervento != null)
      {
        return intervento.getAccertamento();
      }
    }
    return null;
  }

  @RequestMapping(value = "/documento_{idDocumentoSpesa}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> downloadDocumento(Model model,
      HttpSession session,
      @PathVariable("idDocumentoSpesa") long idDocumentoSpesa)
      throws InternalUnexpectedException, ApplicationException
  {
    DocumentoAllegatoDownloadDTO documento = rendicontazioneEAccertamentoSpeseEJB
        .getDocumentoSpesaInRendicontazioneByIdProcedimentOggettoIstruttoria(
            getIdProcedimentoOggetto(session),
            idDocumentoSpesa);
    HttpHeaders httpHeaders = new HttpHeaders();
    final String nomeFile = documento.getFileName();
    httpHeaders.add("Content-type", IuffiUtils.FILE.getMimeType(nomeFile));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + nomeFile + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        documento.getBytes(), httpHeaders, HttpStatus.OK);
    return response;
  }

}
