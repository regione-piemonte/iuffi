package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento.documentispesa;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.FornitoreDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.RicevutaPagamentoVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TipoDocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.smranags.gaaserv.dto.anagraficatributaria.AnagraficaTributariaVO;

public abstract class DocumentiSpesaBaseController extends BaseController
{
  @Autowired
  IQuadroEJB                     quadroEJB                            = null;

  @Autowired
  IInterventiEJB                 interventiEJB;
  public static final BigDecimal MAX_IMPORTO_SPESA                    = new BigDecimal(
      "9999999.99");

  protected static String        COMMON_SESSION_NAME                  = "CU-IUFFI-263";
  protected static String        COMMON_DATA_DOCUMENTO_SPESA_NAME     = "documentoSpesaVO";
  protected static String        COMMON_DATA_CDU                      = "COMMON_DATA_CDU";
  protected static String        COMMON_DATA_CDU_MODIFICA             = "COMMON_DATA_CDU";
  protected static String        COMMON_DATA_LIST_ID_DOCUMENTI        = "listIdDocumentiSpesa";
  protected static String        COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME = "documentoSpesaNewVO";
  protected static String        COMMON_ID_DETTAGLIO_RICEVUTA         = "modIdDettRicevutaPagamento";

  private final String           FLAG_KEY                             = "K";

  @RequestMapping(value = "/getDatiInserimento", method = RequestMethod.GET)
  public String getDatiInserimentoGet(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    DocumentoSpesaVO documentoSpesaVO = (DocumentoSpesaVO) common
        .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);
    documentoSpesaVO = quadroEJB
        .getDettagioDocumentoSpesa(documentoSpesaVO.getIdDocumentoSpesa());
    common.put(COMMON_DATA_DOCUMENTO_SPESA_NAME, documentoSpesaVO);
    saveCommonInSession(common, session);
    model.addAttribute(COMMON_DATA_DOCUMENTO_SPESA_NAME, documentoSpesaVO);
    model.addAttribute("prfReqValues", Boolean.FALSE);
    return getDatiInserimento(model, session,
        documentoSpesaVO.getIdTipoDocumentoSpesa());
  }

  @RequestMapping(value = "/getDatiInserimento", method = RequestMethod.POST)
  public String getDatiInserimento(Model model, HttpSession session,
      @RequestParam(value = "idTipoDocumento", required = false) long idTipoDocumento)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> elencoTipiDocumenti = quadroEJB
        .geElencoTipiDocumenti();
    TipoDocumentoSpesaVO tipoDocumentoSpesaVO = quadroEJB
        .getDettagioTipoDocumento(idTipoDocumento);

    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    model.addAttribute("elencoTipiDocumenti", elencoTipiDocumenti);
    /*
     * if (!model.containsAttribute("prfReqValues")) {
     * model.addAttribute("prfReqValues", Boolean.TRUE); }
     */
    model.addAttribute("tipoDocumentoSpesaVO", tipoDocumentoSpesaVO);

    if (tipoDocumentoSpesaVO != null)
    {
      if (IuffiConstants.FLAGS.SI
          .equals(tipoDocumentoSpesaVO.getFlagIdModalitaPagamento()))
      {
        model.addAttribute("elencoModalitaPagamento",
            quadroEJB.geElencoModalitaPagamento());
      }
    }

    if (!model.containsAttribute(COMMON_DATA_DOCUMENTO_SPESA_NAME))
    {
      model.addAttribute(COMMON_DATA_DOCUMENTO_SPESA_NAME,
          new DocumentoSpesaVO());
      DocumentoSpesaVO doc = new DocumentoSpesaVO();
      doc.setIdTipoDocumentoSpesa(
          tipoDocumentoSpesaVO.getIdTipoDocumentoSpesa());
      model.addAttribute(COMMON_DATA_DOCUMENTO_SPESA_NAME, doc);
    }

    return "documentispesa/datiDocumento";
  }

  @RequestMapping(value = "/confermaDatiDocumento", method = RequestMethod.POST)
  public String confermaDatiDocumento(Model model, HttpSession session,
      HttpServletRequest request,
      @RequestParam(value = "idTipoDocumento", required = true) long idTipoDocumento,
      @RequestParam(value = "dataDocumento", required = false) String dataDocumento,
      @RequestParam(value = "numeroDocumento", required = false) String numeroDocumento,
      @RequestParam(value = "dataPagamento", required = false) String dataPagamento,
      @RequestParam(value = "idModalitaPagamento", required = false) Long idModalitaPagamento,
      @RequestParam(value = "importoSpesa", required = false) String importoSpesa,
      @RequestParam(value = "importoIva", required = false) String importoIva,
      @RequestParam(value = "note", required = false) String note,
      @RequestParam(value = "nomeAllegato", required = false) String nomeAllegato,
      @RequestParam(value = "fileDaAllegare", required = false) MultipartFile fileAllegato)
      throws InternalUnexpectedException, ApplicationException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    DocumentoSpesaVO documentoSpesaVOSession = (DocumentoSpesaVO) common
        .get(COMMON_DATA_DOCUMENTO_SPESA_NAME);
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());

    Errors errors = new Errors();
    TipoDocumentoSpesaVO tipoDocumentoSpesaVO = quadroEJB
        .getDettagioTipoDocumento(idTipoDocumento);
    DocumentoSpesaVO documentoVO = new DocumentoSpesaVO();
    documentoVO.setIdProcedimento(getIdProcedimento(session));
    if (tipoDocumentoSpesaVO != null)
    {
      documentoVO.setIdTipoDocumentoSpesa(idTipoDocumento);

      if (idModalitaPagamento != null || IuffiConstants.FLAGS.SI
          .equals(tipoDocumentoSpesaVO.getFlagIdModalitaPagamento())
          || FLAG_KEY.equals(tipoDocumentoSpesaVO.getFlagIdModalitaPagamento()))
      {
        if (errors.validateMandatory(idModalitaPagamento,
            "idModalitaPagamento"))
        {
          documentoVO.setIdModalitaPagamento(idModalitaPagamento);
        }
      }
      if (!GenericValidator.isBlankOrNull(dataDocumento)
          || IuffiConstants.FLAGS.SI
              .equals(tipoDocumentoSpesaVO.getFlagDataDocumentoSpesa())
          || FLAG_KEY.equals(tipoDocumentoSpesaVO.getFlagDataDocumentoSpesa()))
      {
        documentoVO.setDataDocumentoSpesa(
            errors.validateMandatoryDate(dataDocumento, "dataDocumento", true));
      }
      if (!GenericValidator.isBlankOrNull(numeroDocumento)
          || IuffiConstants.FLAGS.SI
              .equals(tipoDocumentoSpesaVO.getFlagNumeroDocumentoSpesa())
          || FLAG_KEY
              .equals(tipoDocumentoSpesaVO.getFlagNumeroDocumentoSpesa()))
      {
        if (errors.validateMandatoryFieldMaxLength(numeroDocumento,
            "numeroDocumento", 60))
        {
          documentoVO.setNumeroDocumentoSpesa(numeroDocumento);
        }
      }
      if (!GenericValidator.isBlankOrNull(dataPagamento)
          || IuffiConstants.FLAGS.SI
              .equals(tipoDocumentoSpesaVO.getFlagDataPagamento())
          || FLAG_KEY.equals(tipoDocumentoSpesaVO.getFlagDataPagamento()))
      {
        documentoVO.setDataPagamento(
            errors.validateMandatoryDate(dataPagamento, "dataPagamento", true));
      }

      BigDecimal sommaImportiInterventi = BigDecimal.ZERO;
      if (documentoSpesaVOSession != null)
      {
        Long id = documentoSpesaVOSession.getIdDocumentoSpesa();
        if (id != null)
        {
          sommaImportiInterventi = interventiEJB
              .getSommamportiDocumentoSpesaIntervento(id);
        }
      }

      String flagRendicontazioneConIva = getProcedimentoFromSession(request)
          .getFlagRendicontazioneConIva();
      if ("N".equals(flagRendicontazioneConIva))
      {
        if (sommaImportiInterventi == null)
          sommaImportiInterventi = BigDecimal.ZERO;
        documentoVO.setImportoSpesa(
            errors.validateMandatoryBigDecimalInRange(importoSpesa,
                "importoSpesa", 2, sommaImportiInterventi, MAX_IMPORTO_SPESA));
        documentoVO.setImportoIva(errors.validateMandatoryBigDecimalInRange(
            importoIva, "importoIva", 2, BigDecimal.ZERO, MAX_IMPORTO_SPESA));

      }
      else
        if ("S".equals(flagRendicontazioneConIva))
        {
          BigDecimal importoSpesaNetto = errors
              .validateMandatoryBigDecimalInRange(importoSpesa, "importoSpesa",
                  2, BigDecimal.ZERO, MAX_IMPORTO_SPESA);
          BigDecimal importoIvaBD = errors.validateMandatoryBigDecimalInRange(
              importoIva, "importoIva", 2, BigDecimal.ZERO, MAX_IMPORTO_SPESA);
          if (importoSpesaNetto != null && importoIvaBD != null
              && sommaImportiInterventi != null)
            if (importoSpesaNetto.add(importoIvaBD)
                .subtract(sommaImportiInterventi)
                .compareTo(BigDecimal.ZERO) < 0)
            {
              errors.addError("importoLordo",
                  "L'importo lordo deve essere superiore al valore minimo: "
                      + sommaImportiInterventi);
            }
          documentoVO.setImportoSpesa(importoSpesaNetto);
          documentoVO.setImportoIva(importoIvaBD);
        }

      // TODO
      // fare controllo che l'impo non sia minore della somma ricevute
      if (documentoSpesaVOSession != null
          && documentoSpesaVOSession.getIdDocumentoSpesa() != 0)
      {
        BigDecimal impRic = BigDecimal.ZERO;
        long[] idsDocumentoSpesa = new long[1];
        idsDocumentoSpesa[0] = documentoSpesaVOSession.getIdDocumentoSpesa();
        List<RicevutaPagamentoVO> ricevuteDoc = quadroEJB
            .getElencoRicevutePagamento(idsDocumentoSpesa);
        if (ricevuteDoc != null && !ricevuteDoc.isEmpty())
        {
          for (RicevutaPagamentoVO ric : ricevuteDoc)
            if (ric.getImportoPagamento() != null)
              impRic = impRic.add(ric.getImportoPagamento());

          if (documentoVO.getImportoSpesa() != null)
            if (documentoVO.getImportoLordo().subtract(impRic)
                .compareTo(BigDecimal.ZERO) < 0)
            {
              errors.addError("importoLordo",
                  "L'importo lordo deve essere superiore alla somma degli importi delle ricevute già inserite che è: "
                      + impRic);
            }
        }
      }

      if (!GenericValidator.isBlankOrNull(note) || IuffiConstants.FLAGS.SI
          .equals(tipoDocumentoSpesaVO.getFlagNote())
          || FLAG_KEY.equals(tipoDocumentoSpesaVO.getFlagNote()))
      {
        if (errors.validateMandatoryFieldMaxLength(note, "note", 4000))
        {
          documentoVO.setNote(note);
        }
      }

      if (fileAllegato != null || !GenericValidator.isBlankOrNull(nomeAllegato)
          || IuffiConstants.FLAGS.SI
              .equals(tipoDocumentoSpesaVO.getFlagFileDocumentoSpesa()))
      {
        // if (documentoSpesaVOSession == null ||
        // documentoSpesaVOSession.getNomeFileFisicoDocumentoSpe() ==
        // null) {
        if (IuffiConstants.FLAGS.SI
            .equals(tipoDocumentoSpesaVO.getFlagFileDocumentoSpesa())
            || FLAG_KEY
                .equals(tipoDocumentoSpesaVO.getFlagFileDocumentoSpesa()))
        {
          errors.validateMandatoryFieldLength(nomeAllegato, 0, 255,
              "nomeAllegato");
        }
        else
        {
          errors.validateFieldLength(nomeAllegato, "nomeAllegato", 0, 255);
        }

        if (fileAllegato == null || fileAllegato.isEmpty())
        {
          if (documentoSpesaVOSession == null || documentoSpesaVOSession
              .getNomeFileFisicoDocumentoSpe() == null)
            errors.addError("fileDaAllegare", Errors.ERRORE_CAMPO_OBBLIGATORIO);
        }
        else
        {
          String nomeFile = IuffiUtils.FILE
              .getSafeSubmittedFileName(fileAllegato.getOriginalFilename());
          if (nomeFile.length() > 255)
            errors.addError("fileDaAllegare",
                Errors.ERRORE_LUNGHEZZA_NOME_FILE);

          String extension = null;
          int pos = nomeFile.lastIndexOf('.');
          if (pos >= 0)
          {
            extension = nomeFile.substring(pos + 1);
          }

          boolean ok = quadroEJB.checkExtension(extension);
          if (!ok)
            errors.addError("fileDaAllegare", Errors.ERRORE_ESTENSIONE_FILE);

          if (errors.isEmpty())
          {
            try
            {
              documentoVO.setFileDocumentoSpesa(fileAllegato.getBytes());
            }
            catch (IOException e)
            {
              errors.addError("fileDaAllegare",
                  "Si prega di riprovare ad allegare il file. Errore durante il caricamento dell'allegato");
            }
            documentoVO.setNomeFileFisicoDocumentoSpe(nomeFile);
            documentoVO.setNomeFileLogicoDocumentoSpe(nomeAllegato);
          }
        }
        // }
        documentoVO.setNomeFileLogicoDocumentoSpe(nomeAllegato);
      }

      if (errors.isEmpty())
      {
        if (documentoSpesaVOSession != null)
        {
          if (documentoVO.getFileDocumentoSpesa() == null)
          {
            documentoVO.setFileDocumentoSpesa(
                documentoSpesaVOSession.getFileDocumentoSpesa());
          }
          if (documentoVO.getNomeFileFisicoDocumentoSpe() == null)
          {
            documentoVO.setNomeFileFisicoDocumentoSpe(
                documentoSpesaVOSession.getNomeFileFisicoDocumentoSpe());
          }
          if (documentoVO.getNomeFileLogicoDocumentoSpe() == null)
          {
            documentoVO.setNomeFileLogicoDocumentoSpe(
                documentoSpesaVOSession.getNomeFileLogicoDocumentoSpe());
          }
          documentoVO.setIdDettDocumentoSpesa(
              documentoSpesaVOSession.getIdDettDocumentoSpesa());
          documentoVO.setIdFornitore(documentoSpesaVOSession.getIdFornitore());
          documentoVO.setIdDocumentoSpesa(
              documentoSpesaVOSession.getIdDocumentoSpesa());
        }

        if (documentoSpesaVOSession != null)
        {
          documentoVO.setAllegati(documentoSpesaVOSession.getAllegati());
          documentoVO.setIdDocumentoSpesaFile(
              documentoSpesaVOSession.getIdDocumentoSpesaFile());
        }

        long idDocumentoSpesa = quadroEJB.updateOrInsertDatiDocumentoSpesa(
            getIdProcedimento(session),
            IuffiUtils.PAPUASERV.getFirstCodiceAttore(
                getUtenteAbilitazioni(session)),
            documentoVO);

        documentoVO.setIdDocumentoSpesa(idDocumentoSpesa);
        // Rimuovo dalla sessione il file fisico!!
        documentoVO.setFileDocumentoSpesa(null);
        documentoVO = quadroEJB.getDettagioDocumentoSpesa(idDocumentoSpesa);
        long[] ids = new long[1];
        ids[0] = idDocumentoSpesa;
        common.put(COMMON_DATA_LIST_ID_DOCUMENTI, ids);
        common.put(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME, documentoVO);

        saveCommonInSession(common, session);

        if ("N".equals(tipoDocumentoSpesaVO.getFlagIdFornitore()))
        {
          // Se non è prevista la gestione fornitore salvo subito i
          // dati
          return confermaInseriscifornitore(model, session, request, null);
        }
        model.addAttribute("displayDatiFornitori", "none");
        return "documentispesa/inseriscifornitore";
      }
    }

    model.addAttribute("errors", errors);
    model.addAttribute("prfReqValues", Boolean.TRUE);

    return getDatiInserimento(model, session, idTipoDocumento);
  }

  @RequestMapping(value = "/confermaInserisciDocumento", method = RequestMethod.POST)
  public String confermaInserisciDocumento(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    DocumentoSpesaVO documentoSpesaVOSession = (DocumentoSpesaVO) common
        .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);

    quadroEJB.updateOrInsertDatiDocumentoSpesa(getIdProcedimento(session),
        IuffiUtils.PAPUASERV
            .getFirstCodiceAttore(getUtenteAbilitazioni(session)),
        documentoSpesaVOSession);
    return "redirect:../cuiuffi263l/elencoricevute.do";
  }

  @RequestMapping(value = "/ricerca_fornitori", produces = "application/json", method = RequestMethod.POST)
  @ResponseBody
  public List<DecodificaDTO<Long>> popupRicercaFornitori(Model model,
      HttpSession session, HttpServletRequest request,
      @RequestParam(value = "piva") String piva)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> list = null;

    if (piva != null)
    {
      piva = piva.trim().toUpperCase();
    }

    if (piva == null || piva == "")
    {
      list = new ArrayList<DecodificaDTO<Long>>();

      Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
          session, false);
      DocumentoSpesaVO documentoSpesaVOSession = (DocumentoSpesaVO) common
          .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);
      list = (ArrayList<DecodificaDTO<Long>>) quadroEJB
          .geElencoFornitoriProcedimento(
              getProcedimentoFromSession(request).getIdProcedimento());

      if (documentoSpesaVOSession != null)
      {

        for (DecodificaDTO<Long> d : list)
        {
          if (documentoSpesaVOSession.getIdFornitore() != null
              && d.getId().longValue() == documentoSpesaVOSession
                  .getIdFornitore().longValue())
            d.setCodice("checked");
          else
            d.setCodice("false");
        }
      }

      model.addAttribute("cuaa", piva);
      return list;
    }
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    String[] elencoCuaa = new String[1];
    elencoCuaa[0] = piva.trim();
    try
    {
      // prelevo i dati da tributaria
      AnagraficaTributariaVO[] tributariaVOArray = IuffiUtils.PORTADELEGATA
          .getGaaServiceHlCSIInterface()
          .gaaservGetAnagraficaTributariaByCUAARange(elencoCuaa,
              IuffiUtils.PORTADELEGATA.getSianUtenteVO(utenteAbilitazioni),
              AnagraficaTributariaVO.FLAG_DATI_AZIENDA_TRIBUTARIA);
      if (tributariaVOArray != null && tributariaVOArray[0] != null
          && tributariaVOArray[0].getAziendaTributariaVO() != null)
      {
        String flagPresenteAT = tributariaVOArray[0].getAziendaTributariaVO()
            .getFlagPresenteAT();
        String flagPresenteGAA = tributariaVOArray[0].getAziendaTributariaVO()
            .getFlagPresenteGAA();

        if ("I".equals(flagPresenteAT))
        {
          model.addAttribute("cuaa", piva);
          list = new ArrayList<DecodificaDTO<Long>>();
          list.add(new DecodificaDTO<Long>(new Long(-1),
              "Servizio di consultazione SIAN al momento non disponibile. Qualora non sia già presente in elenco il fornitore con i dati coincidenti a quelli riportati nel documento di spesa allegato, è possibile inserirli a mano mediante il pulsante \"inserisci\""));
          list.addAll(quadroEJB.geElencoFornitori(piva));
          return list;
        }

        if (flagPresenteAT != null && flagPresenteAT.compareTo("S") == 0
            && flagPresenteGAA != null && flagPresenteGAA.compareTo("S") == 0)
        {
          String istatComune = quadroEJB.findIstatComune(tributariaVOArray[0]
              .getAziendaTributariaVO().getComuneSedeLegale());
          FornitoreDTO fornitoreNostro = quadroEJB.getDettaglioFornitoreByPiva(
              tributariaVOArray[0].getAziendaTributariaVO().getCuaa());
          FornitoreDTO fornitoreTributaria = new FornitoreDTO();
          fornitoreTributaria.setExtIstatComuneSedeLegale(
              (istatComune != null) ? istatComune : "");
          fornitoreTributaria.setIndirizzoSedeLegale(tributariaVOArray[0]
              .getAziendaTributariaVO().getIndirizzoSedeLegale());
          fornitoreTributaria.setCodiceFornitore(
              tributariaVOArray[0].getAziendaTributariaVO().getCuaa());
          fornitoreTributaria.setRagioneSociale(
              tributariaVOArray[0].getAziendaTributariaVO().getDenominazione());
          if (fornitoreNostro != null)
          {
            // Se è diverso lo storicizzo
            if (!replaceSpecialCharNvl(fornitoreNostro.getRagioneSociale())
                .equals(replaceSpecialCharNvl(
                    fornitoreTributaria.getRagioneSociale()))
                || !replaceSpecialCharNvl(
                    fornitoreNostro.getIndirizzoSedeLegale())
                        .equals(replaceSpecialCharNvl(
                            fornitoreTributaria.getIndirizzoSedeLegale()))
                || !replaceSpecialCharNvl(
                    fornitoreNostro.getExtIstatComuneSedeLegale())
                        .equals(replaceSpecialCharNvl(
                            fornitoreTributaria.getExtIstatComuneSedeLegale())))
            {
              quadroEJB.storicizzaFornitore(fornitoreNostro.getIdFornitore(),
                  fornitoreTributaria);
            }
            else
              fornitoreNostro.getIdFornitore();
          }
          else
          {
            quadroEJB.inserisciFornitore(fornitoreTributaria, false, null);
          }
        }
      }
    }
    catch (Exception e)
    {
      logger.error(
          "[popupRicercaFornitori] Errore durante la ricerca del fornitore con codice: "
              + piva);
    }
    // aggiungo sempre tutto lo storico che c'è sul nostro db
    list = new ArrayList<DecodificaDTO<Long>>();
    list.add(new DecodificaDTO<Long>(new Long(-1),
        "Qualora non sia già presente in elenco il fornitore con i dati coincidenti a quelli riportati nel documento di spesa allegato, è possibile inserirli a mano mediante il pulsante \"inserisci\""));
    list.addAll(
        (ArrayList<DecodificaDTO<Long>>) quadroEJB.geElencoFornitori(piva));

    model.addAttribute("cuaa", piva);
    return list;
  }

  private String replaceSpecialCharNvl(String txt)
  {
    return IuffiUtils.STRING.nvl(txt).replace(" ", "&");
  }

  @RequestMapping(value = "/confermaDatiNuovoFornitore", method = RequestMethod.POST)
  public String confermaDatiNuovoFornitorePost(
      @RequestParam(value = "cuaa", required = false) String cuaa,
      @RequestParam(value = "denominazione", required = false) String denominazione,
      @RequestParam(value = "indirizzoSedeLegale", required = false) String indirizzoSedeLegale,
      @RequestParam(value = "extIstatComune", required = false) String extIstatComune,
      @RequestParam(value = "comuneSedeLegaleHidden", required = false) String comuneSedeLegale,
      @RequestParam(value = "istatProvincia", required = false) String istatProvincia,
      Model model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    Errors errors = new Errors();

    errors.validateMandatoryFieldMaxLength(cuaa, "cuaa", 16);
    model.addAttribute("istatProvincia", istatProvincia);

    errors.validateMandatoryFieldMaxLength(denominazione, "denominazione", 900);
    errors.validateMandatoryFieldMaxLength(indirizzoSedeLegale,
        "indirizzoSedeLegale", 240);
    if (!errors.validateMandatory(extIstatComune, "extIstatComune"))
    {
      errors.addError("comuneSedeLegale", "Campo obbligatorio");
    }

    FornitoreDTO fornitoreDTO = new FornitoreDTO();

    fornitoreDTO.setCodiceFornitore(cuaa);
    fornitoreDTO.setRagioneSociale(denominazione);
    fornitoreDTO.setIndirizzoSedeLegale(indirizzoSedeLegale);
    fornitoreDTO.setExtIstatComuneSedeLegale(extIstatComune);
    if (!errors.isEmpty())
    {
      // model.addAttribute("errors", errors);
      // model.addAttribute("preferRqValue", Boolean.TRUE);
      model.addAttribute("displayDatiFornitori", "none");
      model.addAttribute("preferRqValue", Boolean.TRUE);
      model.addAttribute("forceDatiFornitori", "true");
      errors.addToModelIfNotEmpty(model);
      return "documentispesa/inseriscifornitore";
    }
    else
    {

      if (quadroEJB.findFornitore(fornitoreDTO))
      {
        // Fornitore già presente!!
        model.addAttribute("displayDatiFornitori", "none");
        model.addAttribute("msgErrore",
            "Fornitore già esistente. Selezionarlo dal relativo elenco!");
        return "documentispesa/inseriscifornitore";
      }

      Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
          session, false);
      DocumentoSpesaVO documentoSpesaVOSession = (DocumentoSpesaVO) common
          .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);
      long idFornitore = quadroEJB.inserisciFornitore(fornitoreDTO, true,
          documentoSpesaVOSession.getIdDettDocumentoSpesa());
      return confermaInseriscifornitore(model, session, request, idFornitore);
    }

  }

  @RequestMapping(value = "/confermaInseriscifornitore", method = RequestMethod.POST)
  public String confermaInseriscifornitore(Model model, HttpSession session,
      HttpServletRequest request,
      @RequestParam(value = "idFornitoreSelezionato") Long idFornitore)
      throws InternalUnexpectedException, ApplicationException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    DocumentoSpesaVO documentoSpesaVOSession = (DocumentoSpesaVO) common
        .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);
    if (idFornitore != null)
    {
      documentoSpesaVOSession.setIdFornitore(idFornitore);
      documentoSpesaVOSession.setCodiceFornitore(
          quadroEJB.getDettaglioFornitore(idFornitore).getCodiceFornitore());
    }
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());

    List<Long> idProcedimentoList = quadroEJB
        .searchDocumentoSpesaByKey(documentoSpesaVOSession);
    long idProcedimento = getIdProcedimento(session);

    if (idProcedimentoList == null
        || !idProcedimentoList.contains(idProcedimento))
    {
      long idDocuemntoSpesaNew = quadroEJB.updateOrInsertDatiDocumentoSpesa(
          idProcedimento, IuffiUtils.PAPUASERV.getFirstCodiceAttore(
              getUtenteAbilitazioni(session)),
          documentoSpesaVOSession);
      documentoSpesaVOSession.setIdDocumentoSpesa(idDocuemntoSpesaNew);
      if (idFornitore != null)
        documentoSpesaVOSession.setRagioneSociale(
            quadroEJB.getDettaglioFornitore(idFornitore).getRagioneSociale());
      model.addAttribute("idDocumentoSpesa", idDocuemntoSpesaNew);
      documentoSpesaVOSession
          .setImportoLordoPagamento(quadroEJB.getImportoPagamentoLordo(
              documentoSpesaVOSession.getIdDocumentoSpesa()));
      model.addAttribute("docSpesa", documentoSpesaVOSession);

      if (idFornitore == null)
        return "documentispesa/elencoRicevutePagamento";
      else
      {
        DocumentoSpesaVO documentoSpesaVO = (DocumentoSpesaVO) common
            .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);
        if (documentoSpesaVO != null)
        {
          documentoSpesaVO.setIdFornitore(idFornitore);
          common.put(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME, documentoSpesaVO);
          saveCommonInSession(common, session);
        }
      }
    }
    else
      if (idProcedimentoList.contains(idProcedimento))
      {
        model.addAttribute("msgErrore",
            "Non è possibile caricare il medesimo documento di spesa per lo stesso procedimento!");
        model.addAttribute("displayDatiFornitori", "none");

        return "documentispesa/inseriscifornitore";
      }
      else
      {
        // Stiamo inserrendo lo stesso documento già presente su un
        // altro documento, chiedo conferma
        common.put(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME,
            documentoSpesaVOSession);
        saveCommonInSession(common, session);
        return "documentispesa/confermaDocumento";
      }

    return "documentispesa/elencoRicevutePagamento";
  }

  @RequestMapping(value = "/popup_ricerca_comune", method = RequestMethod.GET)
  public String popupRicercaComuni(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    model.addAttribute("province", quadroEJB.getProvincie(null, true));
    return "documentispesa/popupRicercaComune";
  }

}