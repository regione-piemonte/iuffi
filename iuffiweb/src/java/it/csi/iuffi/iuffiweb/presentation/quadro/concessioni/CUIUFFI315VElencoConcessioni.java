package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-315-V", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "cuiuffi315v")
public class CUIUFFI315VElencoConcessioni extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @Autowired
  IQuadroEJB quadroEJB;
  
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException
  {
    //Rimuovo filtri della tabella delle pratiche dalla sessione
    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("tablePraticheConcessioni");
    cleanTableMapsInSession(session, tableNamesToRemove);
    setErrorMessages(model);
    return "concessioni/elenco";
  }
  
  
  @RequestMapping(value = "/getElencoConcessioni", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<ConcessioneDTO> getListaConcessioni(
      HttpSession session, 
      Model model)  throws InternalUnexpectedException
  {
    List<ConcessioneDTO> list = quadroIuffiEJB.getElencoConcessioni(); 
    if(list==null) {
      list = new ArrayList<ConcessioneDTO>();
    }
    return list;
  }

  @RequestMapping(value = "getElencoBandi", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoBandi(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> elenco = quadroIuffiEJB.getElencoBandiConcessioni();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    
    for (DecodificaDTO<Long> item : elenco)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getId());
      stato.put("label", item.getDescrizione());
      ret.add(stato);
    }

    return ret;
  }
  
  

  @RequestMapping(value = "getElencoAmmCompetenza", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoAmmCompetenza(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> elenco = quadroIuffiEJB.getAmmCompetenzaConcessioni();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();

    for (DecodificaDTO<Long> item : elenco)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getId());
      stato.put("label", item.getDescrizione());
      ret.add(stato);
    }

    return ret;
  }
  
  @RequestMapping(value = "getTipoAtto", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getTipoAtto(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> elenco = quadroIuffiEJB.getElencoTipiAttiConcessione();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();

    for (DecodificaDTO<Long> item : elenco)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getId());
      stato.put("label", item.getDescrizione());
      ret.add(stato);
    }

    return ret;
  }
  
  @RequestMapping(value = "getStatiConcessioni", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getStatiConcessioni(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> elenco = quadroIuffiEJB.getElencoStatiConcessioni();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();

    for (DecodificaDTO<Long> item : elenco)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getId());
      stato.put("label", item.getDescrizione());
      ret.add(stato);
    }

    return ret;
  }
  
  private void setErrorMessages(Model model) throws InternalUnexpectedException {
    model.addAttribute("NON_ELIMINABILE",quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.PRATICA_NON_ELIMINABILE));
    model.addAttribute("NON_CHIUSA",quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.CONCESSIONE_NON_CHIUSA));
    model.addAttribute("RICHIESTA_VERCOR",quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.CONCESSIONE_NON_VERCOR));
    model.addAttribute("DATI_PRATICA",quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.PROBLEMA_DATI_VISURA));
  }
}
