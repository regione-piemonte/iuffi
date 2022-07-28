package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento.documentispesa;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.INuovoProcedimentoEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDualLIstDTO;
import it.csi.iuffi.iuffiweb.dto.ExcelRicevutePagInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.ExcelRigaDomandaDTO;
import it.csi.iuffi.iuffiweb.dto.FiltroVO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimento.RicevutaPagamentoVO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DettaglioInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi263l")
@IuffiSecurity(value = "CU-IUFFI-263-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
public class CUIUFFI263lElencoDocumentiSpesa extends BaseController
{
  @Autowired
  IQuadroEJB                    quadroEJB         = null;

  @Autowired
  IInterventiEJB                interventiEJB;

  @Autowired
  private INuovoProcedimentoEJB nuovoProcedimento = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    clearCommonInSession(session);

    /*
     * La funzionalità è presente solo se
     * IUF_D_BANDO.RENDICONTAZIONE_DOC_SPESA = ‘S’. Consento l'accesso alla
     * funzionalità solo se non è già stata fatta una rendicontazione prima
     * dell'abilitazione dei doc di spesa. Quindi se
     * IUF_T_RENDICONTAZIONE_SPESE ci sono record col PO validi del mio P ma
     * non documenti collegati.
     */
    if (!quadroEJB.canUseDocumentiSpesa(
        getProcedimentoFromSession(session).getIdProcedimento(),
        getProcedimentoFromSession(session).getIdBando()))
      throw new ApplicationException(
          "Per la pratica selezionata non è possibile procedere poiché esiste una rendicontazione, effettuata senza i documenti di spesa, precedente all'abilitazione della funzionalità richiesta.");
    /*
     * Nel caso particolare in cui risultino associati al procedimento solo
     * interventi per cui non è prevista la gestione dei documenti di spesa (e
     * cioè che hanno IUF_R_LIVELLO_INTERVENTO.FLAG_DOCUMENTO_SPESA = ‘N’),
     * occorre emettere un messaggio di errore e interrompere l’operazione.
     */
    if (0 == quadroEJB.contaInterventiRendicontazConFlagS(
        getProcedimentoFromSession(session).getIdProcedimento(),
        getProcedimentoFromSession(session).getIdBando()))
      throw new ApplicationException(
          "Per la pratica selezionata non è possibile procedere poiché non ci sono interventi in domanda per cui è prevista la rendicontazione mediante documenti di spesa.");

    String flag = getProcedimentoFromSession(session)
        .getFlagRendicontazioneConIva();
    if (GenericValidator.isBlankOrNull(flag))
    {
      flag = "X";
    }
    model.addAttribute("flagRendicontazioneConIva", flag);
    model.addAttribute("flagRendicontazioneConIvaDescr",
        flag.replace("S", "Si").replace("N", "No"));
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());

    String hasHelp = nuovoProcedimento.getHelpCdu("CU-IUFFI-263-L", null);
    if (hasHelp != null)
      model.addAttribute("hasHelp", hasHelp);

    return "documentispesa/elenco";
  }

  @RequestMapping(value = "/index_{idProcedimento}", method = RequestMethod.GET)
  public String get(Model model, HttpSession session,
      @PathVariable(value = "idProcedimento") long idProcedimento)
      throws InternalUnexpectedException
  {
    IuffiFactory.setIdProcedimentoInSession(session, idProcedimento);
    model.addAttribute("isFromRegistroFatture", true);
    model.addAttribute("idProcedimento", idProcedimento);
    String flag = quadroEJB.getProcedimento(idProcedimento)
        .getFlagRendicontazioneConIva();
    model.addAttribute("flagRendicontazioneConIvaDescr",
        flag.replace("S", "Si").replace("N", "No"));

    return "documentispesa/elenco";
  }

  @IsPopup
  @RequestMapping(value = "/popup_flag_rendicontazione", method = RequestMethod.GET)
  public String popupFlag(Model model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    clearCommonInSession(session);
    model.addAttribute("flagRendicontazioneConIva",
        getProcedimentoFromSession(session).getFlagRendicontazioneConIva());
    model.addAttribute("canModifyRendicontazioneIva",
        quadroEJB.canModifyRendicontazioneIva(
            getProcedimentoFromSession(session).getIdProcedimento()));

    List<DecodificaDTO<String>> flagRendicontazioneList = new ArrayList<>();
    flagRendicontazioneList.add(new DecodificaDTO<String>("S", "Si"));
    flagRendicontazioneList.add(new DecodificaDTO<String>("N", "No"));
    model.addAttribute("flagRendicontazioneList", flagRendicontazioneList);

    List<DocumentoSpesaVO> elenco = quadroEJB
        .getElencoDocumentiSpesa(getIdProcedimento(session), null, null);
    if (elenco != null && elenco.size() > 0)
    {
      model.addAttribute("showMsgWarning", Boolean.TRUE);
    }

    return "documentispesa/popupFlagRendicontazione";
  }

  @RequestMapping(value = "/aggiornaFlagRendizontazione", method = RequestMethod.POST)
  public String aggiornaFlagRendizontazione(Model model, HttpSession session,
      @RequestParam(value = "flagRendicontazione", required = true) String flagRendicontazione)
      throws InternalUnexpectedException, ApplicationException
  {
    long idProcediemnto = getIdProcedimento(session);
    quadroEJB.aggiornaFlagRendizontazione(flagRendicontazione, idProcediemnto);
    model.addAttribute("updateSuccess", "S");
    IuffiFactory.setIdProcedimentoInSession(session, idProcediemnto);
    return get(model, session);
  }

  public HashMap<String, FiltroVO> parseFilters(String json)
      throws JsonProcessingException, IOException
  {
    HashMap<String, FiltroVO> filtersMap = new HashMap<String, FiltroVO>();
    JsonFactory factory = new JsonFactory();
    ObjectMapper mapper = new ObjectMapper(factory);
    JsonNode rootNode = mapper.readTree(json);

    Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.getFields();
    Iterator<Map.Entry<String, JsonNode>> valuesIterator;
    while (fieldsIterator.hasNext())
    {
      Map.Entry<String, JsonNode> field = fieldsIterator.next();
      filtersMap.put(field.getKey(), new FiltroVO());
      filtersMap.get(field.getKey()).setValues(new ArrayList<Long>());
      filtersMap.get(field.getKey()).setStrValues(new ArrayList<String>());
      valuesIterator = field.getValue().getFields();
      while (valuesIterator.hasNext())
      {
        Map.Entry<String, JsonNode> value = valuesIterator.next();

        // Caso di flitri del tipo checkbox a scelta multipla
        if (value.getValue().isArray())
        {
          for (final JsonNode objNode : value.getValue())
          {
            if ("codOggettiDoc".equals(field.getKey()))
            {
              filtersMap.get(field.getKey()).getStrValues()
                  .add(objNode.asText());
            }
          }
        }
      }
    }
    return filtersMap;
  }

  @RequestMapping(value = "getElencoDocumenti", produces = "application/json")
  @ResponseBody
  public List<DocumentoSpesaVO> getElencoDocumenti(HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException,
      JsonParseException, JsonMappingException, IOException
  {
    @SuppressWarnings("unchecked")
    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    ArrayList<String> idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato = null;
    if (mapFilters != null)
    {
      if (mapFilters.containsKey("elencoDocumenti"))
      {
        String s = mapFilters.get("elencoDocumenti");

        HashMap<String, FiltroVO> f = parseFilters(s);
        FiltroVO fVo = f.get("codOggettiDoc");
        if (fVo != null && fVo.getStrValues() != null)
          idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato = (ArrayList<String>) fVo
              .getStrValues();
      }
    }

    Procedimento procedimento = getProcedimentoFromSession(session);

    // qua dovrei leggere il filtro e selezionare per l'importo rendicontato
    // solo i proc oggetti che derivano dal filtro
    List<DocumentoSpesaVO> elenco = quadroEJB.getElencoDocumentiSpesa(
        procedimento.getIdProcedimento(),
        idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato,
        procedimento.getFlagRendicontazioneConIva());
    if (elenco == null)
    {
      elenco = new ArrayList<>();
    }
    else
    {
      for (DocumentoSpesaVO doc : elenco)
      {
        if (doc.getNomeFileFisicoDocumentoSpe() != null)
        {
          String icon = IuffiUtils.FILE
              .getDocumentCSSIconClass(doc.getNomeFileFisicoDocumentoSpe());
          doc.setIconaFile(icon);
        }

        if ("N".equals(doc.getFlagEliminabile()))
        {
          String flagRendicontazioneConIva = getProcedimentoFromSession(session)
              .getFlagRendicontazioneConIva();
          if (GenericValidator.isBlankOrNull(flagRendicontazioneConIva))
          {
            flagRendicontazioneConIva = "X";
          }
          doc.setFlagShowIconDocGiaRendicontato(quadroEJB
              .showIconDocGiaRendicontato(doc, flagRendicontazioneConIva));
        }
      }
    }
    return elenco;
  }

  @RequestMapping(value = "/getInterventi_{idDocumentoSpesa}", method = RequestMethod.GET)
  @ResponseBody
  public String getInterventifordocspesa(Model model,
      HttpServletRequest request, HttpSession session,
      @PathVariable(value = "idDocumentoSpesa") long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    StringBuilder html = new StringBuilder();

    List<RigaRendicontazioneDocumentiSpesaDTO> elenco = quadroEJB
        .getListInterventiByIdDocumentoSpesa(idDocumentoSpesa);
    if (elenco != null && !elenco.isEmpty())
    {
      html.append(
          "<table class=\"bootstrap-table table table-hover table-striped table-bordered tableBlueTh \">");
      html.append("<thead>");
      html.append("<tr>");
      html.append("<th>Progr.</th>");
      html.append("<th>Descrizione Intervento</th>");
      html.append("<th>Importo rendicontato</th>");
      html.append("<th>Importo da rendicontare</th>");
      html.append("</tr>");
      html.append("</thead>");
      html.append("<tbody>");

      for (RigaRendicontazioneDocumentiSpesaDTO item : elenco)
      {
        html.append("<tr>");
        html.append(
            "<td style=\"text-align: center\"> <span class=\"badge\" style=\"background-color: white; color: black; border: 1px solid black\">"
                + item.getProgressivo() + "</span></td>");
        html.append(
            "<td>" + StringEscapeUtils.escapeHtml4(item.getDescIntervento())
                + "</td>");
        html.append("<td class=\"numero\">" + item.getImportoRendicontatoStr()
            + "</td>");
        html.append("<td class=\"numero\">" + item.getImportoDaRendicontareStr()
            + "</td>");
        html.append("</tr>");
      }

      html.append("</tbody>");
      html.append("</table>");
    }
    else
    {
      html.append(
          "<div class=\"stdMessagePanel\"><div class=\"alert alert-info\"><p>Nessun intervento presente</p></div></div>");
    }

    return html.toString();
  }

  @RequestMapping(value = "getTipiDocumento", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getTipiDocumento(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> elencoTipiDocumenti = quadroEJB
        .geElencoTipiDocumenti();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<Long> item : elencoTipiDocumenti)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getId());
      stato.put("label", item.getDescrizione());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getModalitaPagamento", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getModalitaPagamento(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> elencoTipiDocumenti = quadroEJB
        .geElencoModalitaPagamento();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<Long> item : elencoTipiDocumenti)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getId());
      stato.put("label", item.getDescrizione());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "/dettagliointerventi", method = RequestMethod.POST)
  public String confermainterventi(Model model, HttpSession session,
      HttpServletRequest request,
      @RequestParam(value = "idDocumentoSpesa", required = true) long[] idsDocumentoSpesa)
      throws InternalUnexpectedException
  {
    String flag = getProcedimentoFromSession(session)
        .getFlagRendicontazioneConIva();
    if (GenericValidator.isBlankOrNull(flag))
    {
      flag = "X";
    }
    model.addAttribute("flagRendicontazioneConIva", flag);
    model.addAttribute("idProcedimento",
        getProcedimentoFromSession(session).getIdProcedimento());

    Map<Long, BigDecimal> mapImportiLordiDocContatiSoloUnaVolta = new HashMap<>();
    Map<Long, BigDecimal> mapImportiNettiDocContatiSoloUnaVolta = new HashMap<>();
    Map<Long, BigDecimal> mapImportiRicevuteContatiSoloUnaVolta = new HashMap<>();

    List<List<DettaglioInterventoDTO>> listaTabelle = new ArrayList<>();

    for (long idDocumentoSpesa : idsDocumentoSpesa)
    {
      long[] idsDocSpesa = new long[1];
      idsDocSpesa[0] = idDocumentoSpesa;

      DettaglioInterventoDTO detInt = null;

      List<DettaglioInterventoDTO> listaInterventi = new ArrayList<>();
      RigaElencoInterventi interventoDett = null;
      long idProcedimento = getIdProcedimento(session);

      Vector<Long> vct = new Vector<Long>();
      vct.add(idDocumentoSpesa);
      List<DecodificaDualLIstDTO<Long>> elencoInterventiSelezionati = interventiEJB
          .getElencoInterventiPerDocSpesa(getIdProcedimento(session), vct,
              false);
      if (elencoInterventiSelezionati != null)
      {
        for (DecodificaDualLIstDTO<Long> item : elencoInterventiSelezionati)
        {
          long idIntervento = item.getId();
          interventoDett = interventiEJB
              .getDettaglioInterventoById(idProcedimento, idIntervento);

          List<DocumentoSpesaVO> elencoDocumenti = quadroEJB
              .getElencoDocumentiSpesa(idProcedimento, idsDocSpesa,
                  idIntervento);
          if (elencoDocumenti != null && !elencoDocumenti.isEmpty())
            for (DocumentoSpesaVO doc : elencoDocumenti)
            {

              doc.setAllegati(quadroEJB.getElencoAllegatiIdIntervento(
                  idDocumentoSpesa, idIntervento));

              if (doc.getNomeFileFisicoDocumentoSpe() != null)
              {
                String icon = IuffiUtils.FILE.getDocumentCSSIconClass(
                    doc.getNomeFileFisicoDocumentoSpe());
                doc.setIconaFile(icon);
              }
              doc.setImportoDaRendicontare(quadroEJB
                  .findImportoRendicontaTO(idDocumentoSpesa, idIntervento));
            }
          detInt = new DettaglioInterventoDTO();
          detInt.setIntervento(interventoDett);
          detInt.getIntervento().setElencoDocumenti(elencoDocumenti);
          listaInterventi.add(detInt);

        }

        listaTabelle.add(listaInterventi);

        long[] ids = new long[1];
        for (DettaglioInterventoDTO intervento : listaInterventi)
          for (DocumentoSpesaVO doc : intervento.getIntervento()
              .getElencoDocumenti())
          {
            mapImportiLordiDocContatiSoloUnaVolta
                .put(doc.getIdDettDocumentoSpesa(), doc.getImportoLordo());
            mapImportiNettiDocContatiSoloUnaVolta
                .put(doc.getIdDettDocumentoSpesa(), doc.getImportoSpesa());

            ids[0] = doc.getIdDocumentoSpesa();
            List<RicevutaPagamentoVO> listRic = quadroEJB
                .getElencoRicevutePagamento(ids);
            if (listRic != null)
              for (RicevutaPagamentoVO r : listRic)
              {
                BigDecimal importoAssociato = quadroEJB
                    .getImportoDocSpesaIntRic(
                        intervento.getIntervento().getIdIntervento(),
                        doc.getIdDocumentoSpesa(), r.getIdRicevutaPagamento());
                r.setImportoDaAssociare(importoAssociato);
                mapImportiRicevuteContatiSoloUnaVolta
                    .put(r.getIdRicevutaPagamento(), r.getImportoPagamento());
              }
            doc.setElencoRicevutePagamento(listRic);

          }
      }
    }

    Map<Integer, List<DettaglioInterventoDTO>> map = new TreeMap<>();
    for (List<DettaglioInterventoDTO> list : listaTabelle)
    {
      for (DettaglioInterventoDTO dettaglio : list)
      {
        Integer progressivo = dettaglio.getIntervento().getProgressivo();
        List<DettaglioInterventoDTO> l = map.get(progressivo);
        if (l == null)
        {
          l = new ArrayList<>();
          map.put(progressivo, l);
        }
        l.add(dettaglio);
      }
    }
    listaTabelle.clear();
    for (Integer progressivo : map.keySet())
    {
      listaTabelle.add(map.get(progressivo));
    }

    model.addAttribute("listaTabelle",
        (!listaTabelle.isEmpty() ? listaTabelle : null));

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

    return "documentispesa/dettaglioImporto";
  }

  @RequestMapping(value = "elencoDocumentiExcel")
  public ModelAndView downloadExcel(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    // Documenti
    List<DocumentoSpesaVO> documentiConAllegatiEDomande = quadroEJB
        .getElencoDocumentiSpesa(getIdProcedimento(session), null,
            getProcedimentoFromSession(session).getFlagRendicontazioneConIva());
    for (DocumentoSpesaVO doc : documentiConAllegatiEDomande)
    {
      ArrayList<Long> idsDomande = new ArrayList<>();
      if (doc.getOggettiDoc() != null)
        for (ProcedimentoOggettoDTO proc : doc.getOggettiDoc())
        {
          if (doc.getPrefix() != null)
            idsDomande.add(proc.getIdProcedimentoOggetto());
        }
      doc.setElencoRicevutePagamento(
          quadroEJB.getElencoRicevutePagamentoDomande(doc.getIdDocumentoSpesa(),
              idsDomande));
    }
    session.setAttribute("documentiConAllegatiEDomande",
        documentiConAllegatiEDomande);

    // Creo a partire dai documenti, l'elenco delle domande del procedimento a
    // cui i documenti sono collegati
    List<ExcelRigaDomandaDTO> elencoDomande = new ArrayList<>();
    if (documentiConAllegatiEDomande != null)
    {
      for (DocumentoSpesaVO doc : documentiConAllegatiEDomande)
      {
        if (doc.getOggettiDoc() != null)
        {
          for (ProcedimentoOggettoDTO ogg : doc.getOggettiDoc())
          {
            // lo inserisco solo se non c'è già
            boolean insert = true;

            for (ExcelRigaDomandaDTO domanda : elencoDomande)
              if ((ogg.getPrefix() != null
                  && ogg.getPrefix().equals(domanda.getPrefix()))
                  || domanda.getPrefix() == null)
                insert = false;
            if (insert && ogg.getPrefix() != null)
            {
              ExcelRigaDomandaDTO domanda = new ExcelRigaDomandaDTO();
              domanda.setDescrOggetto(ogg.getDescrOggetto());
              domanda.setIdProcedimentoOggetto(ogg.getIdProcedimentoOggetto());
              domanda.setImportoRendicontato(ogg.getImportoRendicontato());
              domanda.setPrefix(ogg.getPrefix());
              elencoDomande.add(domanda);
            }
          }
        }
      }
    }

    // Interventi
    for (ExcelRigaDomandaDTO domanda : elencoDomande)
    {
      List<ExcelRicevutePagInterventoDTO> interventi = quadroEJB
          .getElencoExcelRicevutePagamentoInterventoPerDomanda(
              getIdProcedimento(session), domanda.getIdProcedimentoOggetto());
      domanda.setInterventi(interventi);
    }
    session.setAttribute("elencoDomande", elencoDomande);

    // ORA PER OGNI DOMANDA HO I SUOI INTERVNETI
    session.setAttribute("elencoRicevute",
        quadroEJB.getElencoExcelRicevutePagamento(getIdProcedimento(session)));
    List<ExcelRicevutePagInterventoDTO> elencoInterventi = quadroEJB
        .getElencoExcelRicevutePagamentoIntervento(getIdProcedimento(session));
    return new ModelAndView("excelElencoDocumentiView", "elencoInterventi",
        elencoInterventi);
  }

  @RequestMapping(value = "getDomandePagamento", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getDomandePagamento(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {

    List<DocumentoSpesaVO> documenti = quadroEJB.getElencoDocumentiSpesa(
        getProcedimentoFromSession(request).getIdProcedimento(), null, null);
    HashMap<String, String> myMap = new HashMap<>();
    List<String> myList = new ArrayList<>();
    for (DocumentoSpesaVO doc : documenti)
    {
      if (doc.getOggettiDoc() != null)
      {
        for (ProcedimentoOggettoDTO p : doc.getOggettiDoc())
        {
          if (p.getPrefix() != null)
          {
            myList.add(p.getDescrOggetto() + " - " + p.getPrefix());
          }
        }
      }
    }

    Collections.sort(myList);
    for (String s : myList)
      myMap.put(s, s);

    Map<String, String> map = new TreeMap<String, String>(myMap);

    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    Iterator<Entry<String, String>> it = map.entrySet().iterator();
    while (it.hasNext())
    {
      Entry<String, String> pair = it.next();
      stato = new HashMap<String, Object>();
      stato.put("id", pair.getKey());
      stato.put("label", pair.getValue());
      ret.add(stato);
      it.remove();
    }
    stato = new HashMap<String, Object>();
    stato.put("id", "000");
    stato.put("label", "Da rendicontare");
    ret.add(stato);

    return ret;
  }

}