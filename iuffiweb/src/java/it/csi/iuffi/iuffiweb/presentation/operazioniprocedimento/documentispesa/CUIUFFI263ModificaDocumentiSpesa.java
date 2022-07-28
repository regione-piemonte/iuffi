
package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento.documentispesa;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.dto.DecodificaDualLIstDTO;
import it.csi.iuffi.iuffiweb.dto.ImportoInterventoVO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.RicevutaPagamentoVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TipoDocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DettaglioInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi263m")
@IuffiSecurity(value = "CU-IUFFI-263-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI263ModificaDocumentiSpesa
    extends DocumentiSpesaBaseController
{

  @RequestMapping(value = "/getDatiInserimento_{idDocumentoSpesa}", method = RequestMethod.GET)
  public String getDatiInserimentoModifica(Model model, HttpSession session,
      @PathVariable(value = "idDocumentoSpesa") long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    DocumentoSpesaVO documentoSpesaVO = quadroEJB
        .getDettagioDocumentoSpesa(idDocumentoSpesa);
    model.addAttribute(COMMON_DATA_DOCUMENTO_SPESA_NAME, documentoSpesaVO);
    clearCommonInSession(session);
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    common.put(COMMON_DATA_DOCUMENTO_SPESA_NAME, documentoSpesaVO);
    common.put(COMMON_DATA_CDU, COMMON_DATA_CDU_MODIFICA);
    long[] ids = new long[1];
    ids[0] = idDocumentoSpesa;
    common.put(COMMON_DATA_LIST_ID_DOCUMENTI, ids);

    saveCommonInSession(common, session);
    model.addAttribute(COMMON_DATA_DOCUMENTO_SPESA_NAME, documentoSpesaVO);
    model.addAttribute("prfReqValues", Boolean.FALSE);
    return getDatiInserimento(model, session,
        documentoSpesaVO.getIdTipoDocumentoSpesa());
  }

  @RequestMapping(value = "/inseriscifornitore", method = RequestMethod.GET)
  public String inseriscifornitoreGet(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("displayDatiFornitori", "none");

    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    long[] ids = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
    if (ids != null)
    {
      model.addAttribute("idDocumentoSpesa", ids[0]);
    }

    DocumentoSpesaVO documentoSpesaVO = (DocumentoSpesaVO) common
        .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);
    if (documentoSpesaVO != null)
      model.addAttribute("documentoSpesaVO", documentoSpesaVO);

    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());

    return "documentispesa/inseriscifornitore";
  }

  @RequestMapping(value = "/modificainterventi", method = RequestMethod.POST)
  public String modificainterventi(Model model, HttpSession session,
      HttpServletRequest request,
      @RequestParam(value = "idDocumentoSpesa", required = true) long[] idsDocumentoSpesa)
      throws InternalUnexpectedException, ApplicationException
  {
    model.addAttribute("listIdDocumentiSpesa", idsDocumentoSpesa);
    clearCommonInSession(session);
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    common.put(COMMON_DATA_LIST_ID_DOCUMENTI, idsDocumentoSpesa);
    saveCommonInSession(common, session);
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());

    for (long id : idsDocumentoSpesa)
    {
      long[] ids = new long[1];
      ids[0] = id;
      List<RicevutaPagamentoVO> ric = quadroEJB.getElencoRicevutePagamento(ids);
      if (ric == null || ric.isEmpty())
      {
        throw new ApplicationException(
            "Impossibile procedere con l'associazione degli interventi in quanto per almeno un documento selezionato non sono ancora stati associati dei pagamenti!");
      }
    }

    return "documentispesa/modificainterventi";
  }

  @RequestMapping(value = "/modificainterventi_{idDocumentoSpesa}", method = RequestMethod.GET)
  public String modificainterventi(Model model, HttpSession session,
      HttpServletRequest request,
      @PathVariable(value = "idDocumentoSpesa") long idDocumentoSpesa)
      throws InternalUnexpectedException, ApplicationException
  {
    DocumentoSpesaVO documentoSpesaVO = quadroEJB
        .getDettagioDocumentoSpesa(idDocumentoSpesa);
    TipoDocumentoSpesaVO tipoDocumentoSpesa = quadroEJB
        .getDettagioTipoDocumento(documentoSpesaVO.getIdTipoDocumentoSpesa());

    if ("K".equals(tipoDocumentoSpesa.getFlagIdFornitore())
        || "S".equals(tipoDocumentoSpesa.getFlagIdFornitore()))
      if (documentoSpesaVO.getIdFornitore() == null
          || documentoSpesaVO.getIdFornitore().longValue() <= 0)
      {
        throw new ApplicationException(
            "Impossibile procedere con l'associazione degli interventi in quanto non è stato specificato il fornitore per questo documento!");
      }

    long[] ids = new long[1];
    ids[0] = idDocumentoSpesa;
    List<RicevutaPagamentoVO> ric = quadroEJB.getElencoRicevutePagamento(ids);
    if (ric == null || ric.isEmpty())
    {
      throw new ApplicationException(
          "Impossibile procedere con l'associazione degli interventi in quanto per il documento selezionato non sono ancora stati associati dei pagamenti!");
    }

    model.addAttribute("listIdDocumentiSpesa", ids);
    clearCommonInSession(session);
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    common.put(COMMON_DATA_LIST_ID_DOCUMENTI, ids);
    saveCommonInSession(common, session);
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());

    return "documentispesa/modificainterventi";
  }

  @RequestMapping(value = "/modificainterventi", method = RequestMethod.GET)
  public String modificainterventiGet(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    return "documentispesa/modificainterventi";
  }

  @RequestMapping(value = "/json/load_elenco_interventi", produces = "application/json")
  @ResponseBody
  public List<DecodificaDualLIstDTO<Long>> loadElencoInterventi(Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    long[] idsDocSpesa = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
    Vector<Long> vIds = new Vector<>();
    for (long id : idsDocSpesa)
    {
      vIds.add(id);
    }
    List<DecodificaDualLIstDTO<Long>> elenco = interventiEJB
        .getElencoInterventiPerDocSpesa(getIdProcedimento(session), vIds, true);
    if (elenco == null)
    {
      elenco = new ArrayList<>();
    }
    return elenco;
  }

  @RequestMapping(value = "/json/load_elenco_interventi_selezionati", produces = "application/json")
  @ResponseBody
  public List<DecodificaDualLIstDTO<Long>> loadElencoInterventiSelezionati(
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    long[] idsDocSpesa = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
    Vector<Long> vIds = new Vector<>();
    for (long id : idsDocSpesa)
    {
      vIds.add(id);
    }
    List<DecodificaDualLIstDTO<Long>> elenco = interventiEJB
        .getElencoInterventiPerDocSpesa(getIdProcedimento(session), vIds,
            false);
    if (elenco == null)
    {
      elenco = new ArrayList<>();
    }
    return elenco;
  }

  @RequestMapping(value = "/confermainterventi", method = RequestMethod.GET)
  public String confermainterventiGet(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    return confermainterventi(model, session, request);
  }

  // metodo aggiunto per la navigazione del wizard
  @RequestMapping(value = "/modificaimporto", method = RequestMethod.GET)
  public String modificaimporto(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {

    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    model.addAttribute("listaInterventi", common.get("listaInterventi"));
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    model.addAttribute("flagRendicontazioneConIva",
        getProcedimentoFromSession(session).getFlagRendicontazioneConIva());

    return "documentispesa/modificaimporto";
  }

  @IsPopup
  @RequestMapping(value = "/confermaRimuoviInterventi", method = RequestMethod.GET)
  public String controllaRimuoviInterventi(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    model.addAttribute("interventiRimossi",
        (String) common.get("interventiRimossi"));
    return "documentispesa/confermaRimuoviInterventi";
  }

  @RequestMapping(value = "/controllaRimozioneInterventi", method = RequestMethod.POST)
  @ResponseBody
  public String controllaRimozioneInterventi(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    // nuovi
    String[] idsInterventi = request.getParameterValues("idInterventiHidden");

    // vecchi
    List<DecodificaDualLIstDTO<Long>> elencoInterventiSelezionati = loadElencoInterventiSelezionati(
        model, session);

    String ret = "";
    boolean trovato;
    if (elencoInterventiSelezionati != null
        && !elencoInterventiSelezionati.isEmpty())
    {
      for (DecodificaDualLIstDTO<Long> decInt : elencoInterventiSelezionati)
      {
        trovato = false;
        String s = "";
        if (idsInterventi != null)
        {
          for (String idInt : idsInterventi)
          {
            if (Long.parseLong(idInt) == decInt.getId().longValue())
            {
              trovato = true;

            }
            else
              s = decInt.getDescrizione() + " ";

          }

          if (!trovato)
            ret += s;

        }
        else
        {
          ret += decInt.getDescrizione() + " ";
        }
      }
    }

    if ("".equals(ret))
      return "continue";
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    common.put("interventiRimossi", ret);
    saveCommonInSession(common, session);
    return ret;
  }

  @RequestMapping(value = "/confermainterventi", method = RequestMethod.POST)
  public String confermainterventi(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    BigDecimal importoRendicontato = null;
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    long[] idsDocSpesa = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);

    Map<Long, BigDecimal> mapImportiLordiDocContatiSoloUnaVolta = new HashMap<>();
    Map<Long, BigDecimal> mapImportiNettiDocContatiSoloUnaVolta = new HashMap<>();
    Map<Long, BigDecimal> mapImportiRicevuteContatiSoloUnaVolta = new HashMap<>();

    String[] idsInterventi = request.getParameterValues("idInterventiHidden");

    common.put("idsInterventi", idsInterventi);
    saveCommonInSession(common, session);
    DettaglioInterventoDTO detInt = null;

    List<DettaglioInterventoDTO> listaInterventi = new ArrayList<>();
    RigaElencoInterventi interventoDett = null;
    long idProcedimento = getIdProcedimento(session);

    List<DecodificaDualLIstDTO<Long>> elencoInterventiSelezionati = loadElencoInterventiSelezionati(
        model, session);
    boolean trovato;

    // verifico Se ho già rendicontato un intervento con il documento di
    // spesa e vado in modifica non posso toglierlo dall'elenco degli
    // interventi
    if (elencoInterventiSelezionati != null
        && !elencoInterventiSelezionati.isEmpty())
    {
      for (DecodificaDualLIstDTO<Long> decInt : elencoInterventiSelezionati)
      {
        trovato = false;
        if (idsInterventi != null)
          for (String idInt : idsInterventi)
          {
            if (Long.parseLong(idInt) == decInt.getId().longValue())
            {
              trovato = true;
            }
          }
        if (!trovato)
        {
          // se si può togliere lo devo togliere dal db
          trovato = false;
          for (long idDocSpesa : idsDocSpesa)
          {
            importoRendicontato = quadroEJB.findImportoRendicontaTO(idDocSpesa,
                decInt.getId().longValue());
            if (importoRendicontato != null
                && importoRendicontato.compareTo(BigDecimal.ZERO) > 0)
            {
              trovato = true;
              interventoDett = interventiEJB.getDettaglioInterventoById(
                  idProcedimento, decInt.getId().longValue());
              model.addAttribute("msgErrore",
                  "Non è possibile eliminare l'intervento "
                      + interventoDett.getDescIntervento()
                      + " in quanto è già stato rendicontato con documento spesa!");
              model.addAttribute("idProcedimento",
                  getProcedimentoFromSession(session).getIdProcedimento());
              return "documentispesa/modificainterventi";
            }
          }
          if (!trovato)
          {
            quadroEJB.deleteRDocSpesaIntRicPag(idsDocSpesa,
                decInt.getId().longValue());
            quadroEJB.deletedocSpesaIntervento(idsDocSpesa,
                decInt.getId().longValue());
          }
        }
      }
    }

    if (idsInterventi != null)
      for (String idIntervento : idsInterventi)
      {
        interventoDett = interventiEJB.getDettaglioInterventoById(
            idProcedimento, Long.parseLong(idIntervento));

        List<DocumentoSpesaVO> elencoDocumenti = quadroEJB
            .getElencoDocumentiSpesa(idProcedimento, idsDocSpesa,
                Long.parseLong(idIntervento));
        if (elencoDocumenti != null && !elencoDocumenti.isEmpty())
          for (DocumentoSpesaVO doc : elencoDocumenti)
          {
            if (doc.getNomeFileFisicoDocumentoSpe() != null)
            {
              String icon = IuffiUtils.FILE
                  .getDocumentCSSIconClass(doc.getNomeFileFisicoDocumentoSpe());
              doc.setIconaFile(icon);
            }
          }
        detInt = new DettaglioInterventoDTO();
        detInt.setIntervento(interventoDett);
        detInt.getIntervento().setElencoDocumenti(elencoDocumenti);
        listaInterventi.add(detInt);
      }

    long[] ids = new long[1];
    for (DettaglioInterventoDTO intervento : listaInterventi)
      for (DocumentoSpesaVO doc : intervento.getIntervento()
          .getElencoDocumenti())
      {
        doc.setAllegati(
            quadroEJB.getElencoAllegatiIdIntervento(doc.getIdDocumentoSpesa(),
                intervento.getIntervento().getIdIntervento()));

        mapImportiLordiDocContatiSoloUnaVolta.put(doc.getIdDettDocumentoSpesa(),
            doc.getImportoLordo());
        mapImportiNettiDocContatiSoloUnaVolta.put(doc.getIdDettDocumentoSpesa(),
            doc.getImportoSpesa());

        if (doc.getImportoRendicontato() == null)
          if ("S".equals(getProcedimentoFromSession(session)
              .getFlagRendicontazioneConIva()))
          {
            BigDecimal importoAssociato = quadroEJB
                .getImportoAssociatoDoc(doc.getIdDocumentoSpesa());
            BigDecimal spesa = doc.getImportoLordo();

            if (spesa != null && importoAssociato != null)
              doc.setImportoRendicontato(spesa.subtract(importoAssociato));
            else
              doc.setImportoRendicontato(spesa);
          }
          else
          {
            BigDecimal spesa = doc.getImportoSpesa();
            BigDecimal importoAssociato = quadroEJB
                .getImportoAssociatoDoc(doc.getIdDocumentoSpesa());

            if (spesa != null && importoAssociato != null)
              doc.setImportoRendicontato(spesa.subtract(importoAssociato));
            else
              doc.setImportoRendicontato(spesa);
          }

        ids[0] = doc.getIdDocumentoSpesa();
        List<RicevutaPagamentoVO> listRic = quadroEJB
            .getElencoRicevutePagamento(ids);
        if (listRic != null)
          for (RicevutaPagamentoVO r : listRic)
          {
            BigDecimal importoAssociato = quadroEJB.getImportoDocSpesaIntRic(
                intervento.getIntervento().getIdIntervento(),
                doc.getIdDocumentoSpesa(), r.getIdRicevutaPagamento());
            r.setImportoDaAssociare(importoAssociato);

            mapImportiRicevuteContatiSoloUnaVolta
                .put(r.getIdRicevutaPagamento(), r.getImportoPagamento());

            // se l'importo è null o zero e il flag rendicont con
            // iva è S
            if (importoAssociato == null
                && "S".equals(getProcedimentoFromSession(session)
                    .getFlagRendicontazioneConIva()))
            {
              // metto il default ovvero -> IMPORTO_PAGAMENTO meno
              // sommatoria IUF_R_DOC_SPESA_INT_RICEV_PA.IMPORTO
              // dei record con ID_RICEVUTA_PAGAMENTO e
              // ID_DOCUMENTO_SPESA selezionati
              BigDecimal importoGiaAssociato = quadroEJB.getImportoGiaAssociato(
                  intervento.getIntervento().getIdIntervento(),
                  doc.getIdDocumentoSpesa(), r.getIdRicevutaPagamento());
              BigDecimal spesa = r.getImportoPagamento();

              if (spesa != null && importoGiaAssociato != null)
                r.setImportoDaAssociare(spesa.subtract(importoGiaAssociato));
              else
                r.setImportoDaAssociare(spesa);
            }
          }
        doc.setElencoRicevutePagamento(listRic);

      }

    model.addAttribute("listaInterventi", listaInterventi);
    common.put("listaInterventi", listaInterventi); // aggiunto nel common
    // per garantire una
    // corretta navigazione
    // del wizard
    saveCommonInSession(common, session);
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    model.addAttribute("flagRendicontazioneConIva",
        getProcedimentoFromSession(session).getFlagRendicontazioneConIva());

    BigDecimal totImportoLordoDocContatiSoloUnaVolta = BigDecimal.ZERO;
    BigDecimal totImportoNettoDocContatiSoloUnaVolta = BigDecimal.ZERO;
    BigDecimal totImportoRicevuteContateSoloUnaVolta = BigDecimal.ZERO;

    Iterator<Map.Entry<Long, BigDecimal>> it = mapImportiLordiDocContatiSoloUnaVolta
        .entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<Long, BigDecimal> pair = it.next();
      totImportoLordoDocContatiSoloUnaVolta = totImportoLordoDocContatiSoloUnaVolta
          .add((BigDecimal) pair.getValue());
    }

    it = mapImportiNettiDocContatiSoloUnaVolta.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<Long, BigDecimal> pair = it.next();
      totImportoNettoDocContatiSoloUnaVolta = totImportoNettoDocContatiSoloUnaVolta
          .add((BigDecimal) pair.getValue());
    }

    it = mapImportiRicevuteContatiSoloUnaVolta.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<Long, BigDecimal> pair = it.next();
      totImportoRicevuteContateSoloUnaVolta = totImportoRicevuteContateSoloUnaVolta
          .add((BigDecimal) pair.getValue());
    }

    model.addAttribute("totImportoLordoDocContatiSoloUnaVolta",
        totImportoLordoDocContatiSoloUnaVolta);
    model.addAttribute("totImportoNettoDocContatiSoloUnaVolta",
        totImportoNettoDocContatiSoloUnaVolta);
    model.addAttribute("totImportoRicevuteContateSoloUnaVolta",
        totImportoRicevuteContateSoloUnaVolta);

    if (idsInterventi == null)
      return "redirect:../cuiuffi263l/index.do";

    return "documentispesa/modificaimporto";
  }

  @RequestMapping(value = "/controllaimporti", method = RequestMethod.POST)
  @ResponseBody
  public String controllaimporti(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    // QUESTO IL CONTROLLO SUGLI IMPORTI DEGLI INTERVENTI
    String importo;
    List<ImportoInterventoVO> importi = new ArrayList<>();
    BigDecimal importoBd;
    ImportoInterventoVO impIntervento;
    Errors errors = new Errors();

    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    String[] idsInterventi = (String[]) common.get("idsInterventi");
    long[] idsDocSpesa = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
    String idUnivoco;
    HashMap<Long, List<ImportoInterventoVO>> mappaValoriDaSalvare = new HashMap<>();
    List<ImportoInterventoVO> listImporti;
    boolean warningTrovato = false;
    String errore = "";
    // per ogni doc spesa
    for (long idsDoc : idsDocSpesa)
    {
      DocumentoSpesaVO documentoSpesaVO = quadroEJB
          .getDettagioDocumentoSpesa(idsDoc);

      // importo max del documento
      BigDecimal importoMax = documentoSpesaVO.getImportoSpesa();
      if ("S".equals(
          getProcedimentoFromSession(session).getFlagRendicontazioneConIva()))
        importoMax = importoMax.add(documentoSpesaVO.getImportoIva());

      BigDecimal importoBdTotale = BigDecimal.ZERO;
      listImporti = new ArrayList<>();
      mappaValoriDaSalvare.put(idsDoc, listImporti);
      model.addAttribute("preferRqImporti", Boolean.TRUE);
      // per ogni doc ed intervento
      if (idsInterventi != null)
        for (String id : idsInterventi)
        {
          idUnivoco = id + "_" + idsDoc;
          // leggo e valido importo
          importo = request.getParameter("importo_" + idUnivoco);

          importoBd = errors.validateMandatoryBigDecimalInRange(importo,
              "importo_" + idUnivoco, 2, BigDecimal.ZERO, MAX_IMPORTO_SPESA);
          impIntervento = new ImportoInterventoVO();
          impIntervento.setIdIntervento(Long.parseLong(id));
          impIntervento.setImporto(importoBd);

          importi.add(impIntervento);
          if (importoBd != null)
          {

            BigDecimal importoRendicontato = IuffiUtils.NUMBERS.nvl(
                quadroEJB.findImportoRendicontaTO(idsDoc, Long.parseLong(id)));
            boolean isRendicontatoSuperato = importoRendicontato
                .compareTo(importoBd) > 0;

            if (isRendicontatoSuperato)
            {
              errors.addError("importo_" + idUnivoco,
                  "La somma degli importi sugli interventi del documento "
                      + documentoSpesaVO.getDescrTipoDocumento()
                      + " deve essere maggiore o uguale all'importo rendicontato che è pari a "
                      + IuffiUtils.FORMAT
                          .formatCurrency(importoRendicontato));

              common.put("mappavalori", mappaValoriDaSalvare);
              saveCommonInSession(common, session);
            }
            importoBdTotale = importoBdTotale.add(importoBd);
          }

          listImporti.add(impIntervento);
        }

      switch (importoMax.compareTo(importoBdTotale))
      {
        case 1:
          warningTrovato = true;
          break;
        case -1:
        {
          errore = " La somma degli importi sugli interventi del documento "
              + documentoSpesaVO.getDescrTipoDocumento()
              + " deve essere minore o uguale all'importo documento che è pari a "
              + IuffiUtils.FORMAT.formatCurrency(importoMax);
          if (idsInterventi != null)
            for (String id : idsInterventi)
              errors.addError("importo_" + id + "_"
                  + documentoSpesaVO.getIdDocumentoSpesa(), errore);
        }
          break;
      }

    }

    // leggo gli importi da associare
    @SuppressWarnings("unchecked")
    List<DettaglioInterventoDTO> listaInterventi = (List<DettaglioInterventoDTO>) common
        .get("listaInterventi");

    BigDecimal sommaImpDaAssociare = BigDecimal.ZERO;
    for (DettaglioInterventoDTO intervento : listaInterventi)
      for (DocumentoSpesaVO doc : intervento.getIntervento()
          .getElencoDocumenti())
      {
        doc.setImportoRendicontato(
            quadroEJB.findImportoRendicontaTO(doc.getIdDocumentoSpesa(),
                intervento.getIntervento().getIdIntervento()));
        sommaImpDaAssociare = BigDecimal.ZERO;
        boolean existsInputNotZero = false;
        if (doc != null && doc.getElencoRicevutePagamento() != null)
          for (RicevutaPagamentoVO r : doc.getElencoRicevutePagamento())
          {
            idUnivoco = intervento.getIntervento().getIdIntervento() + "_"
                + doc.getIdDocumentoSpesa() + "_"
                + r.getIdDettRicevutaPagamento();
            String imp = request.getParameter("importoRicevuta_" + idUnivoco);
            // non obbligatorio ma deve essercene almeno uno > 0
            BigDecimal importoDaAssociare = errors
                .validateOptionalBigDecimalInRange(imp,
                    "importoRicevuta_" + idUnivoco, 2,
                    BigDecimal.ZERO, MAX_IMPORTO_SPESA);
            r.setImportoDaAssociare(importoDaAssociare);
            if (importoDaAssociare != null)
              sommaImpDaAssociare = sommaImpDaAssociare.add(importoDaAssociare);

            if (importoDaAssociare != null
                && importoDaAssociare.compareTo(BigDecimal.ZERO) != 0)
              existsInputNotZero = true;
          }

        // deve esistere almeno un importo non null e non zero
        if (!existsInputNotZero)
          for (RicevutaPagamentoVO r : doc.getElencoRicevutePagamento())
          {
            idUnivoco = intervento.getIntervento().getIdIntervento() + "_"
                + doc.getIdDocumentoSpesa() + "_"
                + r.getIdDettRicevutaPagamento();
            errors.addError("importoRicevuta_" + idUnivoco,
                "Almeno un importo deve essere valorizzato");
          }

        idUnivoco = intervento.getIntervento().getIdIntervento() + "_"
            + doc.getIdDocumentoSpesa();
        importo = request.getParameter("importo_" + idUnivoco);
        importoBd = errors.validateMandatoryBigDecimalInRange(importo,
            "importo_" + idUnivoco, 2, BigDecimal.ZERO, MAX_IMPORTO_SPESA);
        doc.setImportoDaRendicontare(importoBd);
        if (sommaImpDaAssociare != null && importoBd != null)
          if (sommaImpDaAssociare.compareTo(importoBd) > 0)
          {
            errors.addError("importo_" + idUnivoco,
                "La somma degli importi da associare deve essere minore o uguale all'importo da rendicontare che è pari a "
                    + IuffiUtils.FORMAT.formatCurrency(importoBd));
            errore += "La somma degli importi da associare deve essere minore o uguale all'importo da rendicontare <br>";
          }

      }

    // Controllo che la somma degli importi da associare ad una ricevuta non
    // sia maggiore dell'importo della ricevuta stessa.
    ArrayList<String> idsUnivocoError = new ArrayList<>();
    for (DettaglioInterventoDTO intervento : listaInterventi)
      for (DocumentoSpesaVO doc : intervento.getIntervento()
          .getElencoDocumenti())
      {
        sommaImpDaAssociare = BigDecimal.ZERO;
        if (doc != null && doc.getElencoRicevutePagamento() != null)
          for (RicevutaPagamentoVO r : doc.getElencoRicevutePagamento())
          {
            idUnivoco = intervento.getIntervento().getIdIntervento() + "_"
                + doc.getIdDocumentoSpesa() + "_"
                + r.getIdDettRicevutaPagamento();
            String imp = request.getParameter("importoRicevuta_" + idUnivoco);
            BigDecimal importoDaAssociare = errors
                .validateOptionalBigDecimalInRange(imp,
                    "importoRicevuta_" + idUnivoco, 2,
                    BigDecimal.ZERO, MAX_IMPORTO_SPESA);
            r.setImportoDaAssociare(importoDaAssociare);

            if (r.getImportoDaAssociare() != null)
              sommaImpDaAssociare = sommaImpDaAssociare
                  .add(r.getImportoDaAssociare());
            // Controllo che la somma degli importi da associare ad
            // una ricevuta non sia maggiore dell'importo della
            // ricevuta stessa.
            BigDecimal tot = BigDecimal.ZERO;
            idsUnivocoError.clear();
            for (DettaglioInterventoDTO intervento2 : listaInterventi)
              for (DocumentoSpesaVO doc2 : intervento2.getIntervento()
                  .getElencoDocumenti())
                if (doc2 != null && doc2.getElencoRicevutePagamento() != null)
                  for (RicevutaPagamentoVO r2 : doc2
                      .getElencoRicevutePagamento())
                  {
                    if (r2.getIdDettRicevutaPagamento().longValue() == r
                        .getIdDettRicevutaPagamento().longValue())
                    {
                      if (r2.getImportoDaAssociare() != null)
                        tot = tot.add(r2.getImportoDaAssociare());
                      idUnivoco = intervento2.getIntervento().getIdIntervento()
                          + "_" + doc2.getIdDocumentoSpesa() + "_"
                          + r2.getIdDettRicevutaPagamento();
                      idsUnivocoError.add(idUnivoco);
                      idsUnivocoError
                          .add(intervento.getIntervento().getIdIntervento()
                              + "_" + doc.getIdDocumentoSpesa() + "_"
                              + r.getIdDettRicevutaPagamento());
                    }
                  }

            if (tot.compareTo(r.getImportoPagamento()) > 0)
            {
              for (String idU : idsUnivocoError)
              {
                errors.addError("importoRicevuta_" + idU,
                    "La somma degli importi da associare per la stessa ricevuta non può superare l'importo totale della ricevuta che è pari a "
                        + r.getImportoPagamentoStr());
              }
              // errore += "La somma degli importi da associare per la stessa
              // ricevuta non può superare l'importo totale della ricevuta.
              // <br>";
            }
          }

        if (sommaImpDaAssociare != null && doc.getImportoRendicontato() != null)
          if (sommaImpDaAssociare.compareTo(doc.getImportoRendicontato()) < 0)
          {
            for (RicevutaPagamentoVO r : doc.getElencoRicevutePagamento())
            {
              errors.addError("importoRicevuta_"
                  + intervento.getIntervento().getIdIntervento() + "_"
                  + doc.getIdDocumentoSpesa() + "_"
                  + r.getIdDettRicevutaPagamento(),
                  "La somma degli importi da associare deve essere maggiore o uguale all'importo rendicontato che è pari a "
                      + doc.getImportoRendicontatoStr2());
              // errore += "La somma degli importi da associare deve essere
              // maggiore o uguale all'importo rendicontato. <br>";
            }
          }
      }

    if (!errors.isEmpty())
    {
      StringBuilder errorMsg = new StringBuilder(
          "Sono stati rilevati degli errori nei dati inseriti. I campi errati sono stati contrassegnati in rosso, è necessario correggerli per proseguire");
      errorMsg.append("<script type='text/javascript'>\nclearErrors();");
      for (String key : errors.keySet())
      {
        errorMsg.append("\nsetError('" + key + "','"
            + StringEscapeUtils.escapeEcmaScript(errors.get(key)) + "');");
      }
      errorMsg.append("\ndoErrorTooltip();");
      errorMsg.append("</script>");
      return errorMsg.toString();
    }

    common.put("listaInterventi", listaInterventi);
    common.put("mappavalori", mappaValoriDaSalvare);
    saveCommonInSession(common, session);

    if (warningTrovato)
    {
      return "WARN";
    }

    if (errore != null && errore.trim().length() > 0)
    {
      return errore;
    }

    return "OK";
  }

  @RequestMapping(value = "confermaimportipopup", method = RequestMethod.GET)
  @IsPopup
  public String confermaimportipopup(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    setModelDialogWarning(model,
        "L'importo totale da rendicontare associato agli interventi risulta inferiore all'importo associato al documento di spesa, vuoi continuare?",
        "../cuiuffi263m/confermaimportipopup.do");
    return "dialog/conferma";
  }

  @RequestMapping(value = "confermaimportipopup", method = RequestMethod.POST)
  public String confermaimportipopup(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    @SuppressWarnings("unchecked")
    HashMap<Long, List<ImportoInterventoVO>> mappaValoriDaSalvare = (HashMap<Long, List<ImportoInterventoVO>>) common
        .get("mappavalori");
    if (mappaValoriDaSalvare != null)
    {
      for (Long key : mappaValoriDaSalvare.keySet())
      {
        quadroEJB.inserisciInterventoSpesa(key, mappaValoriDaSalvare.get(key),
            getIdUtenteLogin(session));
      }
    }

    @SuppressWarnings("unchecked")
    List<DettaglioInterventoDTO> listaInterventi = (List<DettaglioInterventoDTO>) common
        .get("listaInterventi");
    UtenteAbilitazioni u = getUtenteAbilitazioni(session);
    quadroEJB.inserisciImportiInterventoDocRicevuta(listaInterventi,
        u.getIdUtenteLogin());

    saveCommonInSession(common, session);

    return "redirect:../cuiuffi263l/index.do";
  }

  @RequestMapping(value = "/confermaimporti", method = RequestMethod.POST)
  public String confermaimporti(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    String[] idsInterventi = request.getParameterValues("id");
    String importo;
    List<ImportoInterventoVO> importi = new ArrayList<>();
    BigDecimal importoBd;
    ImportoInterventoVO impIntervento;
    Errors errors = new Errors();
    long[] idsDocSpesa = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
    String idUnivoco;

    for (long idsDoc : idsDocSpesa)
    {

      BigDecimal importoBdTotale = BigDecimal.ZERO;
      importi = new ArrayList<>();
      model.addAttribute("preferRqImporti", Boolean.TRUE);
      for (String id : idsInterventi)
      {
        idUnivoco = id + "_" + idsDoc;
        importo = request.getParameter("importo_" + idUnivoco);
        importoBd = errors.validateMandatoryBigDecimal(importo,
            "importo_" + idUnivoco, 2);
        impIntervento = new ImportoInterventoVO();
        impIntervento.setIdIntervento(Long.parseLong(id));
        impIntervento.setImporto(importoBd);
        importi.add(impIntervento);

        if (importoBd != null)
        {
          importoBdTotale = importoBdTotale.add(importoBd);
        }
      }

      if (errors.isEmpty())
      {
        quadroEJB.inserisciInterventoSpesa(idsDoc, importi,
            getIdUtenteLogin(session));

      }
    }

    @SuppressWarnings("unchecked")
    List<DettaglioInterventoDTO> listaInterventi = (List<DettaglioInterventoDTO>) common
        .get("listaInterventi");
    UtenteAbilitazioni u = getUtenteAbilitazioni(session);
    quadroEJB.inserisciImportiInterventoDocRicevuta(listaInterventi,
        u.getIdUtenteLogin());
    saveCommonInSession(common, session);

    return "redirect:../cuiuffi263l/index.do";
  }

  @IuffiSecurity(value = "CU-IUFFI-263-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
  @RequestMapping(value = "download_{idDocumentoSpesaFile}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> download(
      @PathVariable("idDocumentoSpesaFile") long idDocumentoSpesaFile,
      HttpSession session)
      throws IOException, InternalUnexpectedException
  {
    FileAllegatoDTO allegato = quadroEJB
        .getFileAllegatoDocSpesa(idDocumentoSpesaFile);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        IuffiUtils.FILE.getMimeType(allegato.getNomeFile()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + allegato.getNomeFile() + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        allegato.getFileAllegato(), httpHeaders, HttpStatus.OK);
    return response;
  }

  /*
   * 
   * GESTIONE RICEVUTE DI PAGAMENTO
   * 
   */

  @RequestMapping(value = "/elencoricevute", method = RequestMethod.GET)
  public String elencoricevute(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    if (common.containsKey(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME))
    {
      DocumentoSpesaVO doc = (DocumentoSpesaVO) common
          .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);
      doc.setImportoLordoPagamento(
          quadroEJB.getImportoPagamentoLordo(doc.getIdDocumentoSpesa()));
      model.addAttribute("docSpesa", doc);

    }
    return "documentispesa/elencoRicevutePagamento";
  }

  @RequestMapping(value = "/elencoricevute_{idDocSpesa}", method = RequestMethod.GET)
  public String elencoricevuteDocSpesa(
      @PathVariable("idDocSpesa") long idDocSpesa, Model model,
      HttpSession session)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    common.remove(COMMON_DATA_LIST_ID_DOCUMENTI);
    long[] ids = new long[1];
    ids[0] = idDocSpesa;
    common.put(COMMON_DATA_LIST_ID_DOCUMENTI, ids);

    DocumentoSpesaVO documentoSpesaVO = quadroEJB
        .getDettagioDocumentoSpesa(idDocSpesa);
    documentoSpesaVO.setImportoLordoPagamento(
        quadroEJB.getImportoPagamentoLordo(idDocSpesa));
    // model.addAttribute("documentoSpesaVO", documentoSpesaVO);
    if (!common.containsKey(COMMON_DATA_DOCUMENTO_SPESA_NAME))
      common.put(COMMON_DATA_DOCUMENTO_SPESA_NAME, documentoSpesaVO);

    if (!common.containsKey(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME))
      common.put(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME, documentoSpesaVO);

    saveCommonInSession(common, session);
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    model.addAttribute("docSpesa", documentoSpesaVO);

    return "documentispesa/elencoRicevutePagamento";
  }

  @RequestMapping(value = "/json/getElencoRicevutePagamento", produces = "application/json")
  @ResponseBody
  public List<RicevutaPagamentoVO> getElencoRicevutePagamento(Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    long[] idsDocuemntiSpesa = (long[]) common
        .get(COMMON_DATA_LIST_ID_DOCUMENTI);
    List<RicevutaPagamentoVO> elenco = quadroEJB
        .getElencoRicevutePagamento(idsDocuemntiSpesa);
    return (elenco != null) ? elenco : new ArrayList<>();
  }

  @RequestMapping(value = "modificaRicevuta_{idDettRicevutaPagamento}", method = RequestMethod.GET)
  public String modificaRicevuta(
      @PathVariable("idDettRicevutaPagamento") long idDettRicevutaPagamento,
      Model model, HttpSession session)
      throws IOException, InternalUnexpectedException
  {
    modificaRicevuta(model, session, idDettRicevutaPagamento);
    return "documentispesa/inserisciRicevuta";
  }

  private void modificaRicevuta(Model model, HttpSession session,
      long idDettRicevutaPagamento) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    common.put(COMMON_ID_DETTAGLIO_RICEVUTA, idDettRicevutaPagamento);
    long[] idsDocuemntiSpesa = (long[]) common
        .get(COMMON_DATA_LIST_ID_DOCUMENTI);
    if (idsDocuemntiSpesa != null)
    {
      // model.addAttribute("documentoSpesaVO",
      // quadroEJB.getDettagioDocumentoSpesa(idsDocuemntiSpesa[0]));
      DocumentoSpesaVO doc = quadroEJB
          .getDettagioDocumentoSpesa(idsDocuemntiSpesa[0]);
      doc.setImportoLordoPagamento(
          quadroEJB.getImportoPagamentoLordo(idsDocuemntiSpesa[0]));
      model.addAttribute("documentoSpesaVO", doc);
    }
    saveCommonInSession(common, session);
    model.addAttribute("elencoModalitaPagamento",
        quadroEJB.geElencoModalitaPagamento());
    model.addAttribute("ricevutaPagamentoVO",
        quadroEJB.getRicevutaPagamento(idDettRicevutaPagamento));
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
  }

  @RequestMapping(value = "modificaRicevutaDocRendicontato_{idDettRicevutaPagamento}", method = RequestMethod.GET)
  public String modificaRicevutaDocRendicontato(
      @PathVariable("idDettRicevutaPagamento") long idDettRicevutaPagamento,
      Model model, HttpSession session)
      throws IOException, InternalUnexpectedException
  {
    modificaRicevuta(model, session, idDettRicevutaPagamento);
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    common.put("isDocGiaRendicontato", true);
    saveCommonInSession(common, session);
    if (common.containsKey("idDocGiaRendicontato"))
      model.addAttribute("idDocSpesa", common.get("idDocGiaRendicontato"));
    return "documentispesa/inserisciRicevutaDocRendicontato";
  }

  @RequestMapping(value = "/elencoricevuteDocRendicontato_{idDocSpesa}", method = RequestMethod.GET)
  public String elencoricevuteDocRendicontato(
      @PathVariable("idDocSpesa") long idDocSpesa, Model model,
      HttpSession session)
      throws InternalUnexpectedException
  {
    DocumentoSpesaVO docSpesa = quadroEJB
        .getDettagioDocumentoSpesaGiaRendicontato(idDocSpesa);
    docSpesa.setImportoLordoPagamento(
        quadroEJB.getImportoPagamentoLordo(idDocSpesa));

    model.addAttribute("documentoSpesaVO", docSpesa);
    model.addAttribute("idDocSpesa", idDocSpesa);

    TipoDocumentoSpesaVO tipoDocumentoSpesaVO = quadroEJB
        .getDettagioTipoDocumento(docSpesa.getIdTipoDocumentoSpesa());
    model.addAttribute("tipoDocumentoSpesaVO", tipoDocumentoSpesaVO);
    model.addAttribute("flagEliminabile",
        quadroEJB.getDettagioDocumentoSpesa(idDocSpesa).getFlagEliminabile());
    long[] ids = new long[1];
    ids[0] = idDocSpesa;

    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    common.put(COMMON_DATA_LIST_ID_DOCUMENTI, ids);

    DocumentoSpesaVO documentoSpesaVO = quadroEJB
        .getDettagioDocumentoSpesa(idDocSpesa);
    if (!common.containsKey(COMMON_DATA_DOCUMENTO_SPESA_NAME))
      common.put(COMMON_DATA_DOCUMENTO_SPESA_NAME, documentoSpesaVO);

    if (!common.containsKey(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME))
      common.put(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME, documentoSpesaVO);

    common.put("isDocGiaRendicontato", true);
    common.put("idDocGiaRendicontato", idDocSpesa);

    saveCommonInSession(common, session);
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    model.addAttribute("idDocSpesa", idDocSpesa);

    return "documentispesa/elencoricevutedocrendicontato";
  }

  @RequestMapping(value = "/confermaNewFile_{idDocSpesa}", method = RequestMethod.POST)
  public String confermaNewFile(@PathVariable("idDocSpesa") long idDocSpesa,
      Model model, HttpSession session, HttpServletRequest request,
      @RequestParam(value = "nomeAllegato", required = false) String nomeAllegato,
      @RequestParam(value = "idDocSpesaFile", required = false) Long idDocSpesaFile,
      @RequestParam(value = "fileDaAllegare", required = false) MultipartFile fileAllegato)
      throws InternalUnexpectedException
  {

    DocumentoSpesaVO docSpesa = quadroEJB
        .getDettagioDocumentoSpesaGiaRendicontato(idDocSpesa);
    TipoDocumentoSpesaVO tipoDocumentoSpesaVO = quadroEJB
        .getDettagioTipoDocumento(docSpesa.getIdTipoDocumentoSpesa());
    DocumentoSpesaVO d = new DocumentoSpesaVO();
    d.setIdDocumentoSpesa(idDocSpesa);
    Errors errors = new Errors();
    if (fileAllegato != null || !GenericValidator.isBlankOrNull(nomeAllegato)
        || IuffiConstants.FLAGS.SI
            .equals(tipoDocumentoSpesaVO.getFlagFileDocumentoSpesa()))
    {

      if (IuffiConstants.FLAGS.SI
          .equals(tipoDocumentoSpesaVO.getFlagFileDocumentoSpesa())
          || "K".equals(tipoDocumentoSpesaVO.getFlagFileDocumentoSpesa()))
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
        errors.addError("fileDaAllegare", Errors.ERRORE_CAMPO_OBBLIGATORIO);
      }
      else
      {
        String nomeFile = IuffiUtils.FILE
            .getSafeSubmittedFileName(fileAllegato.getOriginalFilename());
        if (nomeFile.length() > 255)
          errors.addError("fileDaAllegare", Errors.ERRORE_LUNGHEZZA_NOME_FILE);

        String extension = null;
        int pos = nomeFile.lastIndexOf('.');
        if (pos >= 0)
        {
          extension = nomeFile.substring(pos + 1);
        }

        boolean ok = quadroEJB.checkExtension(extension);
        if (!ok)
          errors.addError("fileDaAllegare", Errors.ERRORE_ESTENSIONE_FILE);

        if (!errors.isEmpty())
        {
          return elencoricevuteDocRendicontato(idDocSpesa, model, session);
        }

        d.setNomeFileFisicoDocumentoSpe(nomeFile);
        d.setNomeFileLogicoDocumentoSpe(nomeAllegato);
      }
    }

    try
    {
      d.setFileDocumentoSpesa(fileAllegato.getBytes());
      quadroEJB.insertOrUpdateFileAllegatoDocSpesaGiaRendicontato(d,
          IuffiUtils.PAPUASERV.getFirstCodiceAttore(
              getUtenteAbilitazioni(session)),
          idDocSpesaFile);

    }
    catch (Exception e)
    {
      errors.addError("fileDaAllegare",
          "Si prega di riprovare ad allegare il file. Errore durante il caricamento dell'allegato");
      model.addAttribute("errors", errors);
      return elencoricevuteDocRendicontato(idDocSpesa, model, session);
    }

    return "redirect:../cuiuffi263m/elencoricevuteDocRendicontato_" + idDocSpesa
        + ".do";
  }

}