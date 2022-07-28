package it.csi.iuffi.iuffiweb.presentation.quadro.controlli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.FiltroVO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli.ControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli.FonteControlloDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;

@Controller
@RequestMapping("/cuiuffi114")
@IuffiSecurity(value = "CU-IUFFI-114", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI114Controlli extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String dettaglio(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    if (procedimentoOggetto.getDataFine() == null)
      model.addAttribute("oggettoAperto", true);

    List<FonteControlloDTO> fonteControlloDTOList = quadroEJB.getControlliList(
        procedimentoOggetto.getIdProcedimentoOggetto(),
        ((procedimentoOggetto.getDataFine() != null)
            ? procedimentoOggetto.getDataFine() : new Date()),
        false);

    if (fonteControlloDTOList.size() > 0)
    {
      // Assegno descrizione utente
      List<Long> idUtenti = new ArrayList<Long>();
      for (FonteControlloDTO fonte : fonteControlloDTOList)
      {
        for (ControlloDTO controllo : fonte.getControlli())
        {
          if (controllo.getIdUtenteEsecuzione() != null
              && controllo.getIdUtenteEsecuzione().longValue() != 0)
          {
            if (!idUtenti.contains(controllo.getIdUtenteEsecuzione()))
              idUtenti.add(controllo.getIdUtenteEsecuzione());
          }

          if (controllo.getGravita() != null
              && controllo.getGravita().compareTo("B") == 0)
          {
            model.addAttribute("esistonoAnomalieBloccanti", true);
          }

          if (controllo.getFlagAnomaliaGiustificabile() != null
              && controllo.getFlagAnomaliaGiustificabile().compareTo("S") == 0)
          {
            model.addAttribute("esistonoAnomalieGiustificabili", true);
          }
        }
      }
      if (idUtenti != null && idUtenti.size() > 0)
      {
        List<UtenteLogin> utentiList = loadRuoloDescr(idUtenti);
        for (FonteControlloDTO fonte : fonteControlloDTOList)
        {
          for (ControlloDTO controllo : fonte.getControlli())
          {
            if (controllo.getIdUtenteEsecuzione() != null)
              controllo.setUtenteEsecuzione(getUtenteDescrizione(
                  controllo.getIdUtenteEsecuzione(), utentiList));
          }
        }
      }
    }

    HashMap<String, FiltroVO> filter = null;

    if (session.getAttribute(
        IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA) != null)
    {
      final String ELENCO_CONTROLLI = "elencoControlli";
      @SuppressWarnings("unchecked")
      HashMap<String, String> mapFilters = (HashMap<String, String>) session
          .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
      if (mapFilters != null)
      {
        String filterStr = mapFilters.get(ELENCO_CONTROLLI);
        if (filterStr != null)
        {
          try
          {
            filter = parseFilters(filterStr);
          }
          catch (JsonParseException e)
          {
            filter = null;
          }
          catch (IOException e)
          {
            filter = null;
          }
        }
      }
    }

    // preparo model
    model.addAttribute("filter", filter);
    if (filter == null || filter.isEmpty())
    {
      if (session.getAttribute(
          IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA) != null)
      {
        final String ELENCO_CONTROLLI = "elencoControlli";
        @SuppressWarnings("unchecked")
        HashMap<String, String> mapFilters = (HashMap<String, String>) session
            .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
        if (mapFilters != null)
        {
          mapFilters.remove(ELENCO_CONTROLLI);
        }
      }
    }
    else
    {
      for (FonteControlloDTO fonte : fonteControlloDTOList)
      {
        Iterator<ControlloDTO> iter = fonte.getControlli().iterator();
        while (iter.hasNext())
        {
          boolean rimaniPerGravita = false;
          boolean rimaniPerDescrizione = false;
          boolean rimaniPerRisolto = false;
          boolean rimaniPerCodice = false;

          ControlloDTO c = iter.next();
          if (filter.get("esito") != null)
          {
            for (String s : filter.get("esito").getStrValues())
            {
              if (c.getGravita() == null)
              {
                if (s.compareTo("\"N\"") == 0)
                  rimaniPerGravita = true;
              }
              else
              {
                String grav = "\"" + c.getGravita() + "\"";
                if (grav.compareTo(s) == 0)
                  rimaniPerGravita = true;

              }
            }

          }
          else
            rimaniPerGravita = true;

          if (filter.get("risolto") != null)
          {
            for (String s : filter.get("risolto").getStrValues())
            {
              if (s.equals("\"S\""))
                if (c.getIdSoluzioneAnomalia() != null
                    && c.getIdSoluzioneAnomalia() != 0)
                  rimaniPerRisolto = true;
              if (s.equals("\"N\""))
                if (c.getIdSoluzioneAnomalia() == null
                    || c.getIdSoluzioneAnomalia() == 0)
                  rimaniPerRisolto = true;
            }
          }
          else
            rimaniPerRisolto = true;

          if (filter.get("descrizione") != null)
          {

            FiltroVO filtro = filter.get("descrizione");
            rimaniPerDescrizione = applySearchFilter(c.getDescrizione(),
                filtro);

          }
          else
            rimaniPerDescrizione = true;

          if (filter.get("codice") != null)
          {
            FiltroVO filtro = filter.get("codice");
            rimaniPerCodice = applySearchFilter(c.getCodice(), filtro);
          }
          else
            rimaniPerCodice = true;

          if (!rimaniPerRisolto || !rimaniPerDescrizione || !rimaniPerGravita
              || !rimaniPerCodice)
            iter.remove();
        }
      }
    }

    model.addAttribute("fonteControlloDTOList", fonteControlloDTOList);

    return "controlli/dettaglioDati";
  }

  private boolean applySearchFilter(String stringToCompare, FiltroVO f)
  {

    boolean esitoFiltro = false;
    boolean uguale = true;
    boolean diverso = true;
    boolean contiene = true;
    boolean nonContiene = true;
    boolean vuoto = true;
    boolean nonVuoto = true;

    if (f != null)
    {
      if (f.getUgualeA() != null)
        if (f.getUgualeA().toLowerCase()
            .compareTo(stringToCompare.toLowerCase()) == 0)
          uguale = true;
        else
          uguale = false;
      if (f.getDiversoDa() != null)
        if (f.getDiversoDa().toLowerCase()
            .compareTo(stringToCompare.toLowerCase()) != 0)
          diverso = true;
        else
          diverso = false;
      if (f.getContiene() != null)
        if (stringToCompare.toLowerCase()
            .contains(f.getContiene().toLowerCase()))
          contiene = true;
        else
          contiene = false;
      if (f.getNonContiene() != null)
        if (!stringToCompare.toLowerCase()
            .contains(f.getNonContiene().toLowerCase()))
          nonContiene = true;
        else
          nonContiene = false;
      if (f.isVuoto())
        if (stringToCompare == null || stringToCompare.compareTo("") == 0)
          vuoto = true;
        else
          vuoto = false;
      if (f.isNonVuoto())
        if (stringToCompare != null && stringToCompare.compareTo("") != 0)
          nonVuoto = true;
        else
          nonVuoto = false;

      if (uguale && diverso && contiene && nonContiene && vuoto && nonVuoto)
        esitoFiltro = true;

      return esitoFiltro;

    }
    else
      return true;

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
        if (value.getValue().isArray()
            && field.getKey().compareTo("dataScadenzaStr") != 0)
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
              if (objNode.isTextual())
              {
                @SuppressWarnings("unused")
                FiltroVO v = filtersMap.get(field.getKey());
                List<String> ss = filtersMap.get(field.getKey()).getStrValues();
                ss.add(objNode.toString());
                // filtersMap.get(field.getKey()).getStrValues().add(objNode.toString());
              }
              else
                filtersMap.get(field.getKey()).getValues()
                    .add(objNode.asLong());
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

  private void populateFilter(FiltroVO filtroVO, String key, JsonNode value)
  {
    if (value != null)
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
  }

}
