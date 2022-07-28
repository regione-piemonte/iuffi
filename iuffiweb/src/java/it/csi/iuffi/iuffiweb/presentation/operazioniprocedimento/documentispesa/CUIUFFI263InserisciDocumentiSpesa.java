package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento.documentispesa;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.RicevutaPagamentoVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TipoDocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi263i")
@IuffiSecurity(value = "CU-IUFFI-263-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI263InserisciDocumentiSpesa
    extends DocumentiSpesaBaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    clearCommonInSession(session);
    List<DecodificaDTO<Long>> elencoTipiDocumenti = quadroEJB
        .geElencoTipiDocumenti();
    model.addAttribute("elencoTipiDocumenti", elencoTipiDocumenti);
    return "documentispesa/datiDocumento";
  }

  @IuffiSecurity(value = "CU-IUFFI-263-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
  @RequestMapping(value = "download_{idDettDocumentoSpesa}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> download(
      @PathVariable("idDettDocumentoSpesa") long idDettDocumentoSpesa,
      HttpSession session)
      throws IOException, InternalUnexpectedException
  {
    FileAllegatoDTO allegato = quadroEJB
        .getFileAllegatoDocSpesa(idDettDocumentoSpesa);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        IuffiUtils.FILE.getMimeType(allegato.getNomeFile()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + allegato.getNomeFile() + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        allegato.getFileAllegato(), httpHeaders, HttpStatus.OK);
    return response;
  }

  @RequestMapping(value = "/inserisciricevuta", method = RequestMethod.GET)
  public String inserisciricevuta(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    model.addAttribute("elencoModalitaPagamento",
        quadroEJB.geElencoModalitaPagamento());

    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    if (common.containsKey("isDocGiaRendicontato"))
    {
      if (common.containsKey(COMMON_DATA_LIST_ID_DOCUMENTI))
      {
        long[] ids = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
        model.addAttribute("idDocSpesa", ids[0]);
        DocumentoSpesaVO doc = quadroEJB.getDettagioDocumentoSpesa(ids[0]);
        doc.setImportoLordoPagamento(
            quadroEJB.getImportoPagamentoLordo(ids[0]));
        model.addAttribute("documentoSpesaVO", doc);
      }
      return "documentispesa/inserisciRicevutaDocRendicontato";
    }

    long[] ids = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
    DocumentoSpesaVO doc = quadroEJB.getDettagioDocumentoSpesa(ids[0]);
    doc.setImportoLordoPagamento(quadroEJB.getImportoPagamentoLordo(ids[0]));
    model.addAttribute("documentoSpesaVO", doc);

    return "documentispesa/inserisciRicevuta";
  }

  @RequestMapping(value = "/inserisciricevuta", method = RequestMethod.POST)
  public String inserisciricevutaPost(Model model, HttpSession session,
      @RequestParam(value = "numero", required = false) String numero,
      @RequestParam(value = "dataPagamento", required = false) String dataPagamento,
      @RequestParam(value = "idModalitaPagamento", required = false) Long idModalitaPagamento,
      @RequestParam(value = "importoPagamento", required = false) String importoPagamento,
      @RequestParam(value = "note", required = false) String note)
      throws InternalUnexpectedException
  {
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());

    Errors errors = new Errors();
    RicevutaPagamentoVO ricevuta = new RicevutaPagamentoVO();

    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    if (common.containsKey(COMMON_ID_DETTAGLIO_RICEVUTA))
    {
      ricevuta.setIdDettRicevutaPagamento(
          (long) common.get(COMMON_ID_DETTAGLIO_RICEVUTA));
    }

    if (errors.validateMandatoryFieldMaxLength(numero, "numero", 60))
    {
      ricevuta.setNumero(numero);
    }

    if (errors.validateMandatory(idModalitaPagamento, "idModalitaPagamento"))
    {
      ricevuta.setIdModalitaPagamento(idModalitaPagamento);
    }

    ricevuta.setDataPagamento(
        errors.validateMandatoryDate(dataPagamento, "dataPagamento", true));
    ricevuta.setImportoPagamento(
        errors.validateMandatoryBigDecimalInRange(importoPagamento,
            "importoPagamento", 2, BigDecimal.ONE, new BigDecimal(99999999)));
    // controllo che la somma delle ricevute non superi l'importo del
    // documento
    DocumentoSpesaVO documento = (DocumentoSpesaVO) common
        .get(COMMON_DATA_DOCUMENTO_SPESA_NAME);
    if (documento == null)
      documento = (DocumentoSpesaVO) common
          .get(COMMON_DATA_DOCUMENTO_SPESA_NEW_NAME);
    documento = quadroEJB
        .getDettagioDocumentoSpesa(documento.getIdDocumentoSpesa());

    if (errors.isEmpty())
      if (!quadroEJB.controlloSommaRicevute(documento, ricevuta,
          getProcedimentoFromSession(session).getFlagRendicontazioneConIva()))
      {
        BigDecimal importoSpesa = documento.getImportoSpesa();

        if (documento.getImportoIva() != null)
          importoSpesa = importoSpesa.add(documento.getImportoIva());

        errors.addError("importoPagamento",
            "La somma delle ricevute di pagamento supera l'importo della spesa che è pari a: "
                + importoSpesa);
      }

    if (errors.validateFieldMaxLength(note, "note", 4000))
    {
      ricevuta.setNote(note);
    }

    if (errors.isEmpty())
    {
      if (ricevuta.getIdDettRicevutaPagamento() != null)
        ricevuta.setIdRicevutaPagamento(quadroEJB
            .getRicevutaPagamento(ricevuta.getIdDettRicevutaPagamento())
            .getIdRicevutaPagamento());
      BigDecimal sumIUF_R_DOC_SPESA_INT_RICEV_PA = quadroEJB
          .getImportoDocSpesaIntRicPag(ricevuta.getIdRicevutaPagamento());
      if (sumIUF_R_DOC_SPESA_INT_RICEV_PA != null)
        if (ricevuta.getImportoPagamento()
            .compareTo(sumIUF_R_DOC_SPESA_INT_RICEV_PA) < 0)
          errors.addError("importoPagamento",
              "Il valore non deve essere inferiore a quanto già associato agli interventi, "
                  + sumIUF_R_DOC_SPESA_INT_RICEV_PA);

      long[] idsDocuemntiSpesa = (long[]) common
          .get(COMMON_DATA_LIST_ID_DOCUMENTI);

      quadroEJB.updateOrInsertRicevutaPagamento(idsDocuemntiSpesa, ricevuta,
          IuffiUtils.PAPUASERV
              .getFirstCodiceAttore(getUtenteAbilitazioni(session)));
      model.addAttribute("docSpesa", documento);

      if (common.containsKey("isDocGiaRendicontato"))
      {
        Long idDocSpesa = documento.getIdDocumentoSpesa();
        // PREPARE DATA FOR MODEL
        DocumentoSpesaVO docSpesa = quadroEJB
            .getDettagioDocumentoSpesaGiaRendicontato(idDocSpesa);
        docSpesa.setImportoLordoPagamento(
            quadroEJB.getImportoPagamentoLordo(idsDocuemntiSpesa[0]));

        model.addAttribute("documentoSpesaVO", docSpesa);
        model.addAttribute("idDocSpesa", idDocSpesa);

        TipoDocumentoSpesaVO tipoDocumentoSpesaVO = quadroEJB
            .getDettagioTipoDocumento(docSpesa.getIdTipoDocumentoSpesa());
        model.addAttribute("tipoDocumentoSpesaVO", tipoDocumentoSpesaVO);
        model.addAttribute("flagEliminabile", quadroEJB
            .getDettagioDocumentoSpesa(idDocSpesa).getFlagEliminabile());

        model.addAttribute("idProcedimento",
            getProcedimentoFromSession(session).getIdProcedimento());
        model.addAttribute("idDocSpesa", idDocSpesa);
        return "documentispesa/elencoricevutedocrendicontato";
      }

      DocumentoSpesaVO doc = quadroEJB
          .getDettagioDocumentoSpesa(documento.getIdDocumentoSpesa());
      doc.setImportoLordoPagamento(
          quadroEJB.getImportoPagamentoLordo(documento.getIdDocumentoSpesa()));
      model.addAttribute("documentoSpesaVO", doc);
      model.addAttribute("docSpesa", doc);

      return "documentispesa/elencoRicevutePagamento";
    }
    model.addAttribute("prfReqValues", Boolean.TRUE);

    model.addAttribute("errors", errors);
    return inserisciricevuta(model, session);
  }

  @RequestMapping(value = "/json/getElencoRicevutePagamento", produces = "application/json")
  @ResponseBody
  public List<RicevutaPagamentoVO> getElencoRicevutePagamento(Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);

    if (common.containsKey(COMMON_ID_DETTAGLIO_RICEVUTA))
      common.remove(COMMON_ID_DETTAGLIO_RICEVUTA);
    long[] idsDocuemntiSpesa = (long[]) common
        .get(COMMON_DATA_LIST_ID_DOCUMENTI);
    List<RicevutaPagamentoVO> elenco = quadroEJB
        .getElencoRicevutePagamento(idsDocuemntiSpesa);
    return (elenco != null) ? elenco : new ArrayList<>();
  }

  @RequestMapping(value = "/inserisciricevutaDocRendicontato", method = RequestMethod.GET)
  public String inserisciricevutaDocRendicontato(Model model,
      HttpSession session)
      throws InternalUnexpectedException
  {

    Map<String, Object> common = getCommonFromSession(COMMON_SESSION_NAME,
        session, false);
    if (common.containsKey(COMMON_DATA_LIST_ID_DOCUMENTI))
    {
      long[] ids = (long[]) common.get(COMMON_DATA_LIST_ID_DOCUMENTI);
      model.addAttribute("idDocSpesa", ids[0]);
      DocumentoSpesaVO doc = quadroEJB.getDettagioDocumentoSpesa(ids[0]);
      doc.setImportoLordoPagamento(quadroEJB.getImportoPagamentoLordo(ids[0]));
      model.addAttribute("documentoSpesaVO", doc);
    }

    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());
    model.addAttribute("elencoModalitaPagamento",
        quadroEJB.geElencoModalitaPagamento());
    common.put("isDocGiaRendicontato", true);
    saveCommonInSession(common, session);

    return "documentispesa/inserisciRicevutaDocRendicontato";
  }

}