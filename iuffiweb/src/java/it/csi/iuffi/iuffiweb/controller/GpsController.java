package it.csi.iuffi.iuffiweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.business.IGpsFotoEJB;
import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.business.ITipoAreaEJB;
import it.csi.iuffi.iuffiweb.business.ITipoCampioneEJB;
import it.csi.iuffi.iuffiweb.business.ITipoTrappolaEJB;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.GpsDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "GPS", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class GpsController extends TabelleController
{

  
  @Autowired
  private IGpsFotoEJB gpsFotoEJB;
  @Autowired
  private IAnagraficaEJB anagraficaEJB;

  @Autowired
  private ISpecieVegetaleEJB specieVegetaleEJB;
  
  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;
  
  @Autowired
  private ITipoAreaEJB tipoAreaEJB;
  
  @Autowired
  private ITipoTrappolaEJB tipoTrappolaEJB;
  
  @Autowired
  private ITipoCampioneEJB tipocampioneEJB;
  
  @Autowired
  private IQuadroEJB quadroEJB;
  
  @Autowired
  private IRicercaEJB ricercaEJB;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }


  @RequestMapping(value = "/gps/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    GpsDTO gps = new GpsDTO();
    if (session.getAttribute("gpsRequest") != null) {
      gps = (GpsDTO) session.getAttribute("gpsRequest");
    }
    if (gps.getAnno() == null) {
      Calendar now = Calendar.getInstance();
      int currentYear = now.get(Calendar.YEAR);
      gps.setAnno(currentYear);
    }
    model.addAttribute("gps", gps);
    loadPopupCombo(model, session);
    setBreadcrumbs(model, request);
    session.setAttribute("currentPage", 1);

    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("tableGps");
    cleanTableMapsInSession(session, tableNamesToRemove);

    return "gestioneGpsFoto/ricercaGps";
  }
   
  @RequestMapping(value = "/gps/search")
  public String search(Model model, @ModelAttribute("gps") GpsDTO gpsRequest, HttpSession session, HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    try
    {
      if (gpsRequest.checkNull())
        gpsRequest = (GpsDTO) session.getAttribute("gpsRequest");
      if (gpsRequest == null)
        gpsRequest = new GpsDTO();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    //CARICO LE LISTE
    List<Integer> listaTipoArea = gpsRequest.getTipoArea();
    List<Integer> listaSpecie = gpsRequest.getSpecieVegetale();
    List<Integer> listaOrganismo = gpsRequest.getOrganismoNocivo();
    List<Integer> listaIspetAssegnato = gpsRequest.getIspettoreAssegnato();
    List<Integer> listaTrappole = gpsRequest.getTrappole();
    List<Integer> listaCampioni = gpsRequest.getCampioni();
    if(listaTipoArea!=null) {
      String idArea="";
      for(Integer i : listaTipoArea) {
        idArea+=i+",";
      }
      idArea=idArea.substring(0,idArea.length()-1);
      List<TipoAreaDTO> listaAree=tipoAreaEJB.findByIdMultipli(idArea);
      model.addAttribute("listaAree",listaAree);   
    } 
    //specie
    if(listaSpecie!=null) {
      String idSpecie="";
      for(Integer i : listaSpecie) {
        idSpecie+=i+",";
      }
      idSpecie=idSpecie.substring(0,idSpecie.length()-1);
      List<SpecieVegetaleDTO> listaSpecieVeg=specieVegetaleEJB.findByIdMultipli(idSpecie);
      model.addAttribute("listaSpecie",listaSpecieVeg);     

    } 
    //organismi
    if(listaOrganismo!=null) {
      String idOn="";
      for(Integer i : listaOrganismo) {
        idOn+=i+",";
      }
      idOn=idOn.substring(0,idOn.length()-1);
      List<OrganismoNocivoDTO> listaOn=organismoNocivoEJB.findByIdMultipli(idOn);
      model.addAttribute("listaOn",listaOn);          
    }

    //ispettori assegnati
    if(listaIspetAssegnato!=null) {
      String idIsAss="";
      for(Integer i : listaIspetAssegnato) {
        idIsAss+=i+",";
      }
      idIsAss=idIsAss.substring(0,idIsAss.length()-1);
      List<AnagraficaDTO> listaIspettAssegnati=anagraficaEJB.findByIdMultipli(idIsAss);
      model.addAttribute("listaIspettAssegnati",listaIspettAssegnati);          
    }
    //trappole
    if(listaTrappole!=null) {
      String idTrappole="";
      for(Integer i : listaTrappole) {
        idTrappole+=i+",";
      }
      idTrappole=idTrappole.substring(0,idTrappole.length()-1);
      List<TipoTrappolaDTO> listTrappole=tipoTrappolaEJB.findByIdMultipli(idTrappole);
      model.addAttribute("listaTrappole",listTrappole);          
    }
    //campioni
    if(listaCampioni!=null) {
      String idCampioni="";
      for(Integer i : listaCampioni) {
        idCampioni+=i+",";
      }
      idCampioni=idCampioni.substring(0,idCampioni.length()-1);
      List<TipoCampioneDTO> listCampioni=tipocampioneEJB.findByIdMultipli(idCampioni);
      model.addAttribute("listaCampioni",listCampioni);          
    }

    //recupero il comune
    ComuneDTO comuneDTO = quadroEJB.getComune(gpsRequest.getIstatComune()!=null ? gpsRequest.getIstatComune() : "notfou");
    gpsRequest.setComune(comuneDTO!=null ? comuneDTO.getDescrizioneComune() : " ");

    setBreadcrumbs(model, request);
    model.addAttribute("gps", gpsRequest);
    session.setAttribute("gpsRequest", gpsRequest);
    return "gestioneGpsFoto/listaGps";
  }
 
 
  @RequestMapping(value = "/gps/edit")
  public String showFoto(Model model, @RequestParam(value = "id") Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    GpsDTO gps = gpsFotoEJB.findGpsById(id);
    model.addAttribute("gps", gps);
    
    setBreadcrumbs(model, request);
    return "gestioneGpsFoto/dettaglioGps";
  }

  //aggiunto da Barbara
  @RequestMapping(value = "/gps/getGpsJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getGpsJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    GpsDTO gps = (GpsDTO) session.getAttribute("gpsRequest");
    // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    List<GpsDTO> lista = gpsFotoEJB.findGpsByFilter(gps, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte(), null);

    if (lista == null) {
      lista = new ArrayList<>();
    }

    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }
  
  @RequestMapping(value = "/gps/clearFilter")
  public String clearFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    session.removeAttribute("gpsRequest");
    return "redirect:showFilter.do";
  }
 
  /**
   * carico popup
   * @param model
   * @param session
   * @throws InternalUnexpectedException
   */
  private void loadPopupCombo(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    //UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    //int idProcedimentoAgricolo = utenteAbilitazioni.getIdProcedimento();

    GpsDTO gps = (GpsDTO) session.getAttribute("gpsRequest");
    
    List<TipoAreaDTO> all_tipoArea = null;
    if (session.getAttribute("checkboxAllTipoAree") != null && ((String) session.getAttribute("checkboxAllTipoAree")).equals("true")) {
      all_tipoArea = tipoAreaEJB.findAll();
    } else {
      all_tipoArea = tipoAreaEJB.findValidi();
    }
    model.addAttribute("all_tipoArea", all_tipoArea);
    // Ispettore assegnato
    List<AnagraficaDTO> all_ispettoreAssegnato = null;
    if (session.getAttribute("checkboxAllIspettoriAssegnati") != null && ((String) session.getAttribute("checkboxAllIspettoriAssegnati")).equals("true")) {
      all_ispettoreAssegnato = anagraficaEJB.findAll();
    } else {
      all_ispettoreAssegnato = anagraficaEJB.findValidi();
    }
    model.addAttribute("all_ispettoreAssegnato", all_ispettoreAssegnato);
    // Specie vegetale
    List<SpecieVegetaleDTO> all_specieVegetale = null;
    if (session.getAttribute("checkboxAllSpecieVegetali") != null && ((String) session.getAttribute("checkboxAllSpecieVegetali")).equals("true")) {
      all_specieVegetale = specieVegetaleEJB.findAll();
    } else {
      all_specieVegetale = specieVegetaleEJB.findValidi();
    }
    model.addAttribute("all_specieVegetale", all_specieVegetale);
    // Organismo nocivo
    List<OrganismoNocivoDTO> all_organismoNocivo = null;
    if (session.getAttribute("checkboxAllOrganismiNocivi") != null && ((String) session.getAttribute("checkboxAllOrganismiNocivi")).equals("true")) {
      all_organismoNocivo = organismoNocivoEJB.findAll();
    } else {
      all_organismoNocivo = organismoNocivoEJB.findValidi();
    }
    model.addAttribute("all_organismoNocivo", all_organismoNocivo);
    // Tipo campione
    List<TipoCampioneDTO> all_tipoCampione = null;
    if (session.getAttribute("checkboxAllTipoCampioni") != null && ((String) session.getAttribute("checkboxAllTipoCampioni")).equals("true")) {
      all_tipoCampione = tipocampioneEJB.findAll();
    } else {
      all_tipoCampione = tipocampioneEJB.findValidi();
    }
    model.addAttribute("all_campioni", all_tipoCampione);
    //
    // Tipo trappola
    List<TipoTrappolaDTO> all_tipoTrappola = null;
    if (session.getAttribute("checkboxAllTipoTrappole") != null && ((String) session.getAttribute("checkboxAllTipoTrappole")).equals("true")) {
      all_tipoTrappola = tipoTrappolaEJB.findAll();
    } else {
      all_tipoTrappola = tipoTrappolaEJB.findValidi();
    }
    model.addAttribute("all_trappole", all_tipoTrappola);
    //
    
    if (gps != null) {
      // Tipo aree
      if (gps.getTipoArea() != null) {
        String idArea="";
        for(Integer i : gps.getTipoArea()) {
          idArea+=i+",";
        }
        idArea=idArea.substring(0,idArea.length()-1);
        List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
        model.addAttribute("tipoAree", listaAree);   
      }
      // Specie vegetali
      if (gps.getSpecieVegetale() != null) {
        String idSpecie="";
        for (Integer i : gps.getSpecieVegetale()) {
          idSpecie+=i+",";
        }
        idSpecie=idSpecie.substring(0,idSpecie.length()-1);
        List<SpecieVegetaleDTO> listaSpecieVeg = specieVegetaleEJB.findByIdMultipli(idSpecie);
        model.addAttribute("listaSpecieVegetali", listaSpecieVeg);     
      }
      // Organismi nocivi
      if (gps.getOrganismoNocivo() != null) {
          String idOn="";
          for (Integer i : gps.getOrganismoNocivo()) {
            idOn+=i+",";
          }
          idOn=idOn.substring(0,idOn.length()-1);
          List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findByIdMultipli(idOn);
          model.addAttribute("listaON", listaOn);
       }
      // ispettori assegnati
      if (gps.getIspettoreAssegnato() != null && gps.getIspettoreAssegnato().size() > 0)
      {
          String idIsSec="";
          for(Integer i : gps.getIspettoreAssegnato()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettori",listaIspettAssegnati);          
      }
      // ispettori secondari
      if (gps.getIspettoriSecondari() != null && gps.getIspettoriSecondari().size() > 0)
      {
          String idIsSec="";
          for(Integer i : gps.getIspettoriSecondari()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettSecondari=anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettSecondari",listaIspettSecondari);          
      }
      // tipo trappole
      if (gps.getTrappole() != null) {
        String idTipoTrappola="";
        for (Integer i : gps.getTrappole()) {
          idTipoTrappola+=i+",";
        }
        idTipoTrappola=idTipoTrappola.substring(0,idTipoTrappola.length()-1);
        List<TipoTrappolaDTO> listaTipoTrapp = tipoTrappolaEJB.findByIdMultipli(idTipoTrappola);
        model.addAttribute("listaTipoTrappole", listaTipoTrapp);     
      }

      // tipo campione
      if (gps.getCampioni() != null) {
        String idTipoCampione="";
        for (Integer i : gps.getCampioni()) {
          idTipoCampione+=i+",";
        }
        idTipoCampione=idTipoCampione.substring(0,idTipoCampione.length()-1);
        List<TipoCampioneDTO> listaTipoCamp = tipocampioneEJB.findByIdMultipli(idTipoCampione);
        model.addAttribute("listaTipoCampioni", listaTipoCamp);     
      }

      // comune
      if (StringUtils.isNotBlank(gps.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(gps.getIstatComune());
        if (comuneDTO != null)
        {
          model.addAttribute("provSceltaComune", comuneDTO.getSiglaProvincia());
          model.addAttribute("comuneSceltaComune", comuneDTO.getDescrizioneComune());
        }
      }
    }
  }

  @RequestMapping(value = "/gps/searchComuni", produces = "application/json", method = RequestMethod.POST)
  public Map<String, Object> searchComuni(HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HashMap<String, Object> values = new HashMap<String, Object>();
    String prov = request.getParameter("provSceltaComune");
    String comune = request.getParameter("descComune");
    List<ComuneDTO> comuni = ricercaEJB.searchComuni(prov, comune);
    if (comuni != null && comuni.size() == 1)
    {
      values.put("oneResult", "true");
      values.put("prov", comuni.get(0).getSiglaProvincia());
      values.put("comune", comuni.get(0).getDescrizioneComune());
    }
    else
    {
      values.put("comuniDTO", comuni);
    }
    values.put(IuffiConstants.PAGINATION.IS_VALID, "true"); // Serve al
    // javascript per
    // capire che è
    // non ci sono
    // stati errori
    // nella
    // creazione del
    // file json
    return values;
  }

}
