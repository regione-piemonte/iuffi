package it.csi.iuffi.iuffiweb.presentation.estrazionicampione;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.FiltroVO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.EstrazioneACampioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-215", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping(value = "/cuiuffi215")
public class CUIUFFI215VisualizzaEstrazioniController extends BaseController
{
  @Autowired
  IQuadroEJB  quadroEJB;
  @Autowired
  IRicercaEJB ricercaEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String visualizzaestrazioni(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {

    if (session.getAttribute(
        IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA) != null)
    {
      @SuppressWarnings("unchecked")
      HashMap<String, String> mapFilters = (HashMap<String, String>) session
          .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
      mapFilters.remove("elencoProcedimenti");
    }

    return "estrazioniacampione/visualizzaEstrazioni";
  }

  @RequestMapping(value = "getElencoEstrazioniJson", produces = "application/json")
  @ResponseBody
  public List<EstrazioneACampioneDTO> getElencoEstrazioniACampione(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    List<EstrazioneACampioneDTO> elenco = quadroEJB
        .getElencoEstrazioniACampione(utenteAbilitazioni.isUtenteGAL());
    if (elenco != null && elenco.size() > 0)
    {
      return elenco;
    }
    else
    {
      return new ArrayList<EstrazioneACampioneDTO>();
    }
  }

  @RequestMapping(value = "getElencoTipologieFiltroJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoTipologieFiltroJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);

    List<EstrazioneACampioneDTO> estrazioni = quadroEJB
        .getElencoEstrazioniACampione(utenteAbilitazioni.isUtenteGAL());

    Map<String, Object> oggetto;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    List<Long> ids = new ArrayList<>();
    for (EstrazioneACampioneDTO e : estrazioni)
    {
      if (!ids.contains(e.getIdTipoEstrazione()))
      {
        oggetto = new HashMap<String, Object>();
        oggetto.put("id", e.getIdTipoEstrazione());
        oggetto.put("label", e.getDescrizioneTipologia());
        ret.add(oggetto);
        ids.add(e.getIdTipoEstrazione());
      }
    }

    return ret;
  }

  @RequestMapping(value = "getElencoFlagEstrazione")
  @ResponseBody
  public String getCustomFilterImpegni(HttpSession session)
      throws InternalUnexpectedException
  {
    StringBuffer html = new StringBuffer("<div style=\"min-width:250px\">");

    Map<String, Object> oggetto;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    oggetto = new HashMap<String, Object>();
    oggetto.put("id", -1);
    oggetto.put("label", "Tutti");
    ret.add(oggetto);

    oggetto = new HashMap<String, Object>();
    oggetto.put("id", 1);
    oggetto.put("label", "Estratti a campione");
    ret.add(oggetto);

    html.append("<div class=\"container-fluid\" style=\"min-width:25em;\">");
    html.append(" <div class=\"radio\">");
    html.append(
        "<label><input class=\"filter-enabled\" type=\"radio\" data-val=\"0\" name=\"optradio\">Estratti a campione (tutti)</label>");
    html.append("</div>");
    html.append(" <div class=\"radio\">");
    html.append(
        "<label><input class=\"filter-enabled\" type=\"radio\" data-val=\"1\" name=\"optradio\">Estratti a campione (controllo in loco)</label>");
    html.append("</div>");
    html.append("<div class=\"radio\">");
    html.append(
        "<label><input  class=\"filter-enabled\" type=\"radio\" data-val=\"-1\" name=\"optradio\" checked>Tutti</label>");
    html.append("</div></div>");

    return html.toString();
  }

  @RequestMapping(value = "/dettaglio_{idEstrazione}", method = RequestMethod.GET)
  public String visualizzaDettaglioEstrazione(
      @PathVariable("idEstrazione") long idEstrazione, HttpSession session,
      Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {

    model.addAttribute("idEstrazione", idEstrazione);

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);

    List<EstrazioneACampioneDTO> estrazioni = quadroEJB
        .getElencoEstrazioniACampione(utenteAbilitazioni.isUtenteGAL());
    for (EstrazioneACampioneDTO e : estrazioni)
      if (e.getIdEstrazioneCampione() == idEstrazione)
      {
        model.addAttribute("descrizioneEstrazione", e.getDescrizione());
        model.addAttribute("tipologiaEstrazione", e.getDescrizioneTipologia());
        model.addAttribute("dataEstrazioneStr", e.getDataEstrazioneStr());
        if (e.getNumeroEstrazione() != null)
          model.addAttribute("numeroEstrazione", e.getNumeroEstrazione());
      }

    List<Long> po = ricercaEJB.elencoIdProcedimentiOggettoEstratti(
        getUtenteAbilitazioni(session), idEstrazione);

    if (po != null && po.size() != 0)
      model.addAttribute("PO", 0);
    else
      model.addAttribute("PO", 1);

    return "estrazioniacampione/dettaglioEstrazione";
  }

  @RequestMapping(value = "getElencoProcedimenti_{idEstrazione}", produces = "application/json")
  @ResponseBody
  public HashMap<String, Object> getElencoProcedimenti(Model model,
      @PathVariable("idEstrazione") long idEstrazione, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {

    String limit = request.getParameter("limit");
    String offset = request.getParameter("offset");
    String filter = request.getParameter("filter");
    HashMap<String, FiltroVO> mapFilters = null;
    HashMap<String, Object> elenco = new HashMap<String, Object>();

    if (filter != null)
    {
      try
      {
        mapFilters = parseFilters(filter);
      }
      catch (JsonParseException e)
      {
        mapFilters = null;
      }
      catch (IOException e)
      {
        mapFilters = null;
      }
    }

    int cntRows = 0;

    cntRows = ricercaEJB.searchProcedimentiEstrazioneCount(idEstrazione,
        getUtenteAbilitazioni(session), mapFilters);
    elenco.put("total", cntRows);

    List<ProcedimentoOggettoVO> el = ricercaEJB.searchProcedimentiEstrazione(
        idEstrazione, getUtenteAbilitazioni(session), mapFilters, limit,
        offset);
    List<HashMap<String, Object>> risultato = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> risultati;
    if (el != null)
      for (ProcedimentoOggettoVO item : el)
      {
        risultati = new HashMap<String, Object>();
        risultati.put("identificativo", item.getIdentificativo());
        risultati.put("descrAmmCompetenza", item.getDescrAmmCompetenza());
        risultati.put("annoCampagna", item.getAnnoCampagna());
        risultati.put("elencoCodiciLivelliHtml",
            item.getElencoCodiciLivelliHtml());
        risultati.put("denominazioneBando", item.getDenominazioneBando());
        risultati.put("descrizione", item.getDescrizione());
        risultati.put("identificativoOggetto", item.getIdentificativoOggetto());
        risultati.put("tipoOggetto", item.getTipoOggetto());
        risultati.put("statoOggetto", item.getStatoOggetto());
        risultati.put("flagEstrattaStr", item.getFlagEstrattaStr());
        risultati.put("cuaa", item.getCuaa());
        risultati.put("denominazioneAzienda", item.getDenominazioneAzienda());
        risultati.put("descrComune", item.getDescrComune());
        risultati.put("descrProvincia", item.getDescrProvincia());
        risultati.put("indirizzoSedeLegale", item.getIndirizzoSedeLegale());
        risultati.put("denominzioneDelega", item.getDenominzioneDelega());
        risultato.add(risultati);
      }

    elenco.put("rows", risultato);

    return elenco;
  }

  @RequestMapping(value = "elencoProcedimentiEstrazioneExcel_{idEstrazione}")
  public ModelAndView downloadExcel(Model model,
      @PathVariable("idEstrazione") long idEstrazione, HttpSession session)
      throws InternalUnexpectedException
  {

    List<ProcedimentoOggettoVO> el = ricercaEJB.searchProcedimentiEstrazione(
        idEstrazione, getUtenteAbilitazioni(session), null, null, null);
    return new ModelAndView("excelElencoProcedimentiEstrazioneView", "elenco",
        el);
  }

  @RequestMapping(value = "getElencoTipologieEstrazione", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoTipologieEstrazione(
      HttpSession session) throws InternalUnexpectedException
  {

    List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
    HashMap<String, Object> tipo = null;
    Vector<String> vId = new Vector<String>();

    List<ProcedimentoOggettoVO> elenco = ricercaEJB
        .getElencoTipologieEstrazione();

    for (ProcedimentoOggettoVO p : elenco)
    {
      if (p.getFlagEstratta() != null && !p.getFlagEstratta().equals(""))
      {
        if (!p.getFlagEstrattaStr().equals("")
            && !vId.contains(p.getFlagEstrattaStr()))
        {
          vId.add(p.getFlagEstrattaStr());
          tipo = new HashMap<String, Object>();
          tipo.put("label", p.getFlagEstrattaStr());
          tipo.put("id", p.getIdFlagEstratta());
          ret.add(tipo);
        }
      }
    }

    return ret;
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
            if (objNode.isTextual() && isSearchFilter(objNode.asText()))
            {
              populateFilter(filtersMap.get(field.getKey()), objNode.asText(),
                  null);
            }
            else
            {
              // {"impegni":{"_values":["149&&8&&sel","152&&19&&nsel"]}}
              if ("impegni".equals(field.getKey()))
              {
                filtersMap.get(field.getKey()).getStrValues()
                    .add(objNode.asText());
              }
              else
              {
                filtersMap.get(field.getKey()).getValues()
                    .add(objNode.asLong());
              }
            }
          }
        }
        else
          if (isSearchFilter(value.getKey()))
          {
            populateFilter(filtersMap.get(field.getKey()), value.getKey(),
                value.getValue());
          }
      }
    }
    return filtersMap;
  }

  private void populateFilter(FiltroVO filtroVO, String key, JsonNode value)
  {
    if (IuffiConstants.FILTRI.CONTIENE.equals(key))
    {
      filtroVO.setContiene(value.asText());
    }
    else
      if (IuffiConstants.FILTRI.DIVERSO_DA.equals(key))
      {
        filtroVO.setDiversoDa(value.asText());
      }
      else
        if (IuffiConstants.FILTRI.NON_CONTIENE.equals(key))
        {
          filtroVO.setNonContiene(value.asText());
        }
        else
          if (IuffiConstants.FILTRI.NON_VUOTO.equals(key))
          {
            filtroVO.setNonVuoto(true);
          }
          else
            if (IuffiConstants.FILTRI.UGUALE_A.equals(key))
            {
              filtroVO.setUgualeA(value.asText());
            }
            else
              if (IuffiConstants.FILTRI.VUOTO.equals(key))
              {
                filtroVO.setVuoto(true);
              }
  }

  private boolean isSearchFilter(String key)
  {

    if (IuffiConstants.FILTRI.CONTIENE.equals(key)
        || IuffiConstants.FILTRI.DIVERSO_DA.equals(key)
        || IuffiConstants.FILTRI.NON_CONTIENE.equals(key)
        || IuffiConstants.FILTRI.NON_VUOTO.equals(key)
        || IuffiConstants.FILTRI.UGUALE_A.equals(key)
        || IuffiConstants.FILTRI.VUOTO.equals(key))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

}