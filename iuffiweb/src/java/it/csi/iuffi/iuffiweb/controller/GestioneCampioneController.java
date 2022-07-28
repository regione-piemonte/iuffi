package it.csi.iuffi.iuffiweb.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.business.IAnfiEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneCampioneEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneVisualEJB;
import it.csi.iuffi.iuffiweb.business.IGpsFotoEJB;
import it.csi.iuffi.iuffiweb.business.IMissioneEJB;
import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.business.IRilevazioneEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.business.ITipoAreaEJB;
import it.csi.iuffi.iuffiweb.business.ITipoCampioneEJB;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.AnfiDTO;
import it.csi.iuffi.iuffiweb.model.CampionamentoDTO;
import it.csi.iuffi.iuffiweb.model.CampionamentoSpecOnDTO;
import it.csi.iuffi.iuffiweb.model.CodiceEsitoDTO;
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.EsitoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RilevazioneDTO;
import it.csi.iuffi.iuffiweb.model.RuoloDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.request.CampionamentoRequest;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.TokenUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
//import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteAnagraficaVO;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@SuppressWarnings("unchecked")
@Controller
@IuffiSecurity(value = "GESTIONECAMPIONE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class GestioneCampioneController extends TabelleController
{

  @Autowired
  private IAnagraficaEJB anagraficaEJB;
  
  @Autowired
  private ISpecieVegetaleEJB specieVegetaleEJB;  

  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;  

  @Autowired
  private ITipoAreaEJB tipoAreaEJB;  

  @Autowired
  private ITipoCampioneEJB tipocampioneEJB;  
  
  @Autowired
  private IQuadroEJB quadroEJB;

  @Autowired
  private IGestioneCampioneEJB campioneEJB;

  @Autowired
  private IMissioneEJB missioneEJB;

  @Autowired
  private IAnfiEJB anfiEJB;

  @Autowired
  private IRicercaEJB ricercaEJB;
  
  @Autowired  
  private IGpsFotoEJB gpsFotoEJB;
  
  @Autowired  
  private IRilevazioneEJB rilevazioneEJB;

  @Autowired  
  private IGestioneVisualEJB visualEJB;
  
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  private static final SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }

  @RequestMapping(value = "/gestioneCampione/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    CampionamentoRequest campionamentoRequest = new CampionamentoRequest();
    if (session.getAttribute("campionamentoRequest") != null) {
      campionamentoRequest = (CampionamentoRequest) session.getAttribute("campionamentoRequest");
    }
  
    if (campionamentoRequest.getAnno() == null) {
      Calendar now = Calendar.getInstance();
      int currentYear = now.get(Calendar.YEAR);
      campionamentoRequest.setAnno(currentYear);
    }
    model.addAttribute("campionamentoRequest", campionamentoRequest);
    loadPopupCombo(model, session);
    setBreadcrumbs(model, request);
    return "gestionecampione/ricercaCampioni";
  }
  
  @RequestMapping(value = "/gestioneCampione/showFoto")
  public String showFoto(Model model, @RequestParam(value = "idCampione") Integer idCampione, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    List<FotoDTO> listaFoto= campioneEJB.findListFotoByIdCampione(idCampione);
    model.addAttribute("listaFoto",listaFoto);
   
    setBreadcrumbs(model, request);
    return "gestionecampione/foto";
  }
  
  @RequestMapping(value = "/gestioneCampione/showOn")
  public String showOn(Model model, @RequestParam(value = "idCampione") Integer idCampione, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    List<CampionamentoSpecOnDTO> listaOn= campioneEJB.findOnByIdCampionamento(idCampione);
    model.addAttribute("listaOn",listaOn);
   
    setBreadcrumbs(model, request);
    return "gestionecampione/organismiNocivi";
  }

  @RequestMapping(value = "/gestioneCampione/getCampioneJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getCampioneJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    CampionamentoRequest campionamentoRequest = (CampionamentoRequest) session.getAttribute("campionamentoRequest");
    
 // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    
    DataFilter dataFilter = getFiltroDati(utente);
    List<CampionamentoDTO> lista = campioneEJB.findByFilter(campionamentoRequest, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
    if (lista == null) {
      lista = new ArrayList<>();
    }
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }
  
  @RequestMapping(value="/gestioneCampione/getFoto", method=RequestMethod.GET)
  public ResponseEntity<byte[]> getFoto(@RequestParam(value = "idFoto") Integer idFoto, HttpServletResponse response1) {

      FotoDTO foto;
      ResponseEntity<byte[]> response = null;
      try
      {
        logger.debug("I'm here!");
        foto = campioneEJB.findFotoById(idFoto);
        byte[] contents = foto.getFoto().getBytes();

        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Here you have to set the actual filename of your pdf
        //headers.setContentDispositionFormData("inline", filename);
        headers.add("Content-Disposition", "inline; filename=" + foto.getNomeFile());
        headers.add("Content-Type", "application/pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return response;
  }
  
  @RequestMapping(value = {"/gestioneCampione/search", "/gestioneCampione/searchFromListaMissioni", "/gestioneCampione/searchFromMissione"})
  public String search(Model model, @ModelAttribute("gestioneCampione") CampionamentoRequest campionamentoRequest, 
      HttpServletResponse response,HttpSession session, HttpServletRequest request, 
      RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    try
    {
      if (campionamentoRequest.checkNull() || campionamentoRequest.getIdCampionamento()!=null)
        campionamentoRequest = (CampionamentoRequest) session.getAttribute("campionamentoRequest");
      if (campionamentoRequest == null)
        campionamentoRequest = new CampionamentoRequest();
      //CampionamentoRequest datiRicerca = campionamentoRequest;
      session.setAttribute("campionamentoRequest", campionamentoRequest);
        
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);

    //CARICO LE LISTE
    List<Integer> listaTipoArea = campionamentoRequest.getTipoArea();
    List<Integer> listaSpecie = campionamentoRequest.getSpecieVegetale();
    List<Integer> listaOrganismo = campionamentoRequest.getOrganismoNocivo();
    List<Integer> listaIspetAssegnato = campionamentoRequest.getIspettoreAssegnato();
    List<Integer> listaIspetSecondario = campionamentoRequest.getIspettoriSecondari();
    List<Integer> listaCampioni = campionamentoRequest.getTipoCampioni();
    //aggiunti 
    List<Integer> listaTipoTest = campionamentoRequest.getTipologiaTest();
    List<Integer> listaEsitoTest = campionamentoRequest.getCodiciTest();
    
    if(listaTipoArea!=null) {
      String idArea="";
      for(Integer i : listaTipoArea) {
        idArea+=i+",";
      }
      idArea=idArea.substring(0,idArea.length()-1);
      List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
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
    //ispettori secondari
    if(listaIspetSecondario!=null) {
      String idIsSec="";
      for(Integer i : listaIspetSecondario) {
        idIsSec+=i+",";
      }
      idIsSec=idIsSec.substring(0,idIsSec.length()-1);
      List<AnagraficaDTO> listaIspettSecondari=anagraficaEJB.findByIdMultipli(idIsSec);
      model.addAttribute("listaIspettSecondari",listaIspettSecondari);          
    }
    //campioni
    if(listaCampioni!=null) {
      String idCampioni="";
      for(Integer i : listaCampioni) {
        idCampioni+=i+",";
      }
      idCampioni=idCampioni.substring(0,idCampioni.length()-1);
      List<TipoCampioneDTO> listCampioni = tipocampioneEJB.findByIdMultipli(idCampioni);
      model.addAttribute("listaCampioni",listCampioni);
    }
    //tipo test laboratorio
    if(listaTipoTest!=null) {
      String id="";
      for(Integer i : listaTipoTest) {
        id+=i+",";
      }
      id=id.substring(0,id.length()-1);
      List<AnfiDTO> listTipoTest = anfiEJB.findByIdMultipli(id);
      model.addAttribute("listaTipoTest",listTipoTest);
    }
    //esito test laboratorio
    if(listaEsitoTest!=null) {
      String id="";
      for(Integer i : listaEsitoTest) {
        id+=i+",";
      }
      id=id.substring(0,id.length()-1);
      List<CodiceEsitoDTO> listEsitoTest = campioneEJB.findByIdMultipliCodiciEsito(id);
      model.addAttribute("listaEsitoTest",listEsitoTest);
    }
   
    //recupero il comune
    ComuneDTO comuneDTO = quadroEJB.getComune(campionamentoRequest.getIstatComune()!=null ? campionamentoRequest.getIstatComune() : "notfou");
    campionamentoRequest.setComune(comuneDTO!=null ? comuneDTO.getDescrizioneComune() : " ");

    // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    model.addAttribute("dataFilter", dataFilter);
    
    model.addAttribute("campionamentoRequest", campionamentoRequest);
    session.setAttribute("campionamentoRequest", campionamentoRequest);
    session.setAttribute("onDtoProv", null);
    model.addAttribute("from", "search");
    setBreadcrumbs(model, request);
    return "gestionecampione/elencoCampioni";
  }

 @Lazy
 @RequestMapping(value = "/gestioneCampione/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableCampioni") == null || "{}".equals(filtroInSessione.get("tableCampioni"))) {
        filtroInit = "";//"{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableCampioni");
      } 
       // filtroInit =
           // if(anagrafica.getCognome()!=null &&)
          //  + ",\"cognome\":{\"cnt\":[\""+dto.getCognome().toUpperCase()+"\"]}"
          //  filtroInit += "}";  
       // value='{"annoCampagna":{"cnt":"2016"},"descrizione":{"_values":["Liquidato"]}}'
      filtroInSessione.put("tableCampioni", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/gestioneCampione/clearFilter")
  public String clearFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    session.removeAttribute("campionamentoRequest");
    return "redirect:showFilter.do";
  }
 
  private void loadPopupCombo(Model model, HttpSession session) throws InternalUnexpectedException
  {
    CampionamentoRequest campionamentoRequest = (CampionamentoRequest) session.getAttribute("campionamentoRequest");
    
    // Tipo area
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
    // Ispettori secondari
    List<AnagraficaDTO> all_ispettoriSecondari = null;
    if (session.getAttribute("checkboxAllIspettoriSecondari") != null && ((String) session.getAttribute("checkboxAllIspettoriSecondari")).equals("true")) {
      all_ispettoriSecondari = anagraficaEJB.findAll();
    } else {
      all_ispettoriSecondari = anagraficaEJB.findValidi();
    }
    model.addAttribute("all_ispettoriSecondari", all_ispettoriSecondari);
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
    // Tipo campioni
    List<TipoCampioneDTO> all_tipoCampioni = null;
    if (session.getAttribute("checkboxAllTipoCampioni") != null && ((String) session.getAttribute("checkboxAllTipoCampioni")).equals("true")) {
      all_tipoCampioni = tipocampioneEJB.findAll();
    } else {
      all_tipoCampioni = tipocampioneEJB.findValidi();
    }
    model.addAttribute("all_tipoCampioni", all_tipoCampioni);
    // Tipologia test
    List<AnfiDTO> all_tipologiaTest = null;
    if (session.getAttribute("checkboxAllTipologiaTest") != null && ((String) session.getAttribute("checkboxAllTipologiaTest")).equals("true")) {
      all_tipologiaTest = anfiEJB.findAll();
    } else {
      all_tipologiaTest = anfiEJB.findValidi();
    }
    model.addAttribute("all_tipologiaTest", all_tipologiaTest);

    // esiti test
    List<CodiceEsitoDTO> all_codiciTest = null;
    if (session.getAttribute("checkboxAllCodiceEsito") != null && ((String) session.getAttribute("checkboxAllCodiceEsito")).equals("true")) {
      all_codiciTest = campioneEJB.findAllCodiciEsito();
    } else {
      all_codiciTest = campioneEJB.findValidiCodiciEsito();
    }
    model.addAttribute("all_codiciTest", all_codiciTest);
    
    List<TipoAreaDTO> tipoAree = new ArrayList<>();
    model.addAttribute("tipoAree", tipoAree);
    
    if (campionamentoRequest != null) {
      // Tipo aree
      if (campionamentoRequest.getTipoArea() != null) {
        String idArea="";
        for(Integer i : campionamentoRequest.getTipoArea()) {
          idArea+=i+",";
        }
        idArea=idArea.substring(0,idArea.length()-1);
        List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
        model.addAttribute("tipoAree", listaAree);   
      }
      // Specie vegetali
      if (campionamentoRequest.getSpecieVegetale() != null) {
        String idSpecie="";
        for (Integer i : campionamentoRequest.getSpecieVegetale()) {
          idSpecie+=i+",";
        }
        idSpecie=idSpecie.substring(0,idSpecie.length()-1);
        List<SpecieVegetaleDTO> listaSpecieVeg = specieVegetaleEJB.findByIdMultipli(idSpecie);
        model.addAttribute("listaSpecieVegetali", listaSpecieVeg);     
      }
      // Organismi nocivi
      if (campionamentoRequest.getOrganismoNocivo() != null) {
          String idOn="";
          for (Integer i : campionamentoRequest.getOrganismoNocivo()) {
            idOn+=i+",";
          }
          idOn=idOn.substring(0,idOn.length()-1);
          List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findByIdMultipli(idOn);
          model.addAttribute("listaON", listaOn);
       }
      // ispettori assegnati
      if (campionamentoRequest.getIspettoreAssegnato() != null && campionamentoRequest.getIspettoreAssegnato().size() > 0)
      {
          String idIsSec="";
          for(Integer i : campionamentoRequest.getIspettoreAssegnato()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettori",listaIspettAssegnati);          
      }
      // ispettori secondari
      if (campionamentoRequest.getIspettoriSecondari() != null && campionamentoRequest.getIspettoriSecondari().size() > 0)
      {
          String idIsSec="";
          for(Integer i : campionamentoRequest.getIspettoriSecondari()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettSecondari=anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettSecondari",listaIspettSecondari);          
      }
      // comune
      if (StringUtils.isNotBlank(campionamentoRequest.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(campionamentoRequest.getIstatComune());
        if (comuneDTO != null)
        {
          model.addAttribute("provSceltaComune", comuneDTO.getSiglaProvincia());
          model.addAttribute("comuneSceltaComune", comuneDTO.getDescrizioneComune());
        }
      }
      // Tipo campioni
      if (campionamentoRequest.getTipoCampioni() != null && campionamentoRequest.getTipoCampioni().size() > 0) {
          String idTipoCampioni = "";
          for (Integer i : campionamentoRequest.getTipoCampioni()) {
            idTipoCampioni += i+",";
          }
          idTipoCampioni = idTipoCampioni.substring(0,idTipoCampioni.length()-1);
          List<TipoCampioneDTO> listaTipoCampioni = tipocampioneEJB.findByIdMultipli(idTipoCampioni);
          model.addAttribute("listaTipoCampioni", listaTipoCampioni);
       }
      // Tipo esami laboratorio
      if (campionamentoRequest.getTipologiaTest() != null && campionamentoRequest.getTipologiaTest().size() > 0) {
          String idTipoTestLaboratorio = "";
          for (Integer i : campionamentoRequest.getTipologiaTest()) {
            idTipoTestLaboratorio += i+",";
          }
          idTipoTestLaboratorio = idTipoTestLaboratorio.substring(0,idTipoTestLaboratorio.length()-1);
          List<AnfiDTO> listaTipoTestLab = anfiEJB.findByIdMultipli(idTipoTestLaboratorio);
          model.addAttribute("listaTipoTestLab", listaTipoTestLab);
       }
      // codice esami laboratorio
      if (campionamentoRequest.getCodiciTest() != null && campionamentoRequest.getCodiciTest().size() > 0) {
          String idEsitoTestLaboratorio = "";
          for (Integer i : campionamentoRequest.getCodiciTest()) {
            idEsitoTestLaboratorio += i+",";
          }
          idEsitoTestLaboratorio = idEsitoTestLaboratorio.substring(0,idEsitoTestLaboratorio.length()-1);
          List<CodiceEsitoDTO> listaCodiciTestLab = campioneEJB.findByIdMultipliCodiciEsito(idEsitoTestLaboratorio);
          model.addAttribute("listaCodiciTestLab", listaCodiciTestLab);
       }
    }
  }

  @RequestMapping(value = {"/gestioneCampione/edit", "gestioneCampione/show", "/gestioneCampione/editCampioni", "/gestioneCampione/showCampioni",
      "/gestioneCampione/editCampioniFromListaMissioni", "/gestioneCampione/editCampioniFromMissione", "/gestioneCampione/showCampioniFromListaMissioni",
      "/gestioneCampione/showCampioniFromMissione", "gestioneCampione/editFromListaMissioni", "gestioneCampione/showFromListaMissioni",
      "gestioneCampione/editFromMissione", "gestioneCampione/showFromMissione"})
  public String edit(Model model, @RequestParam(value = "idCampionamento") Integer idCampionamento, 
       @RequestParam(value = "from") String from,@ModelAttribute("campionamento") CampionamentoDTO campionamento,
       HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    CampionamentoRequest campionamentoRequest = (CampionamentoRequest) session.getAttribute("campionamentoRequest");
    session.removeAttribute("campionamentoRequest");
    session.setAttribute("campionamentoRequest", campionamentoRequest);
    //model.addAttribute("campionamentoRequest", datiRicerca);
     
    CampionamentoDTO dto = campioneEJB.findById(idCampionamento);
    //List<EsitoCampioneDTO> esiti = campioneEJB.findEsitiByIdCampionamento(idCampionamento);
    
    if(from.equals("addOn") || from.equals("deleteOnProv") ||  from.equals("deleteOnDef") 
        || from.equals("deleteFileProv") || from.equals("deleteFileDef") || from.equals("addFile")){
      dto.setLongitudine(campionamento.getLongitudine());
      dto.setLatitudine(campionamento.getLatitudine());
      dto.setNote(campionamento.getNote());
      dto.setIdTipoArea(campionamento.getIdTipoArea());
      dto.setIdSpecieVegetale(campionamento.getIdSpecieVegetale());
      dto.setIdAnagrafica(campionamento.getIdAnagrafica());
    }
     
    //carico gli on provvisori
    if(from!=null && (from.equals("elenco") || from.equals("annulla") || from.equals("save")) ) {
      session.setAttribute("onDtoProv", null); 
      session.setAttribute("onDtoDef", campioneEJB.findOnByIdCampionamento(idCampionamento)); 
    }
    //carico gli esiti
    if(from!=null && (from.equals("elenco") || from.equals("annulla") || from.equals("save")) ) {
      session.setAttribute("fileProv", null);
      session.setAttribute("fileDef", campioneEJB.findEsitiByIdCampionamento(idCampionamento));
    }

    List<OrganismoNocivoDTO> onDtoProv= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    List<CampionamentoSpecOnDTO> onDtoDef= (List<CampionamentoSpecOnDTO>)session.getAttribute("onDtoDef"); 
    List<EsitoCampioneDTO> fileProv= (List<EsitoCampioneDTO>)session.getAttribute("fileProv"); 
    List<EsitoCampioneDTO> fileDef= (List<EsitoCampioneDTO>)session.getAttribute("fileDef"); 
  //carico gli on provvisori
    
    // comune
    String comune = null;
    if (dto != null) {
      if (StringUtils.isNotBlank(dto.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(dto.getIstatComune());
        if (comuneDTO != null)
        {
          comune = comuneDTO.getDescrizioneComune();
          dto.setComune(comune);
          model.addAttribute("comune", comune);
        }
      }
    }

    model.addAttribute("campionamento", dto);

    List<FotoDTO> listaFoto = campioneEJB.findListFotoByIdCampione(idCampionamento);
    model.addAttribute("listaFoto", listaFoto);
    
    //DATI AZIENDA
    if(dto.getCuaa() != null) {
      MissioneController missione = new MissioneController();
      ResponseEntity<?> anag =missione.getAnagraficheAviv(dto.getCuaa(), request, session);
      if(anag.getStatusCode().value()==200)
        model.addAttribute("anagraficaAzienda", anag);
      else {
          model.addAttribute("anagraficaAzienda", null);
	      model.addAttribute("error", "Non è stato possibile recuperare i dati dell'azienda");
      }
    }
    
    List<AnagraficaDTO> ispettoriAggiunti = missioneEJB.getIspettoriAggiunti(dto.getIdMissione().longValue());
    model.addAttribute("ispettoriAggiunti", ispettoriAggiunti);
    
    List<CampionamentoSpecOnDTO> organismiNocivi=new ArrayList<CampionamentoSpecOnDTO>();
    //gestisco gli on provvisori
    if(onDtoProv!=null && !onDtoProv.isEmpty()) {
      for(OrganismoNocivoDTO on : onDtoProv) {
        CampionamentoSpecOnDTO newOn = new CampionamentoSpecOnDTO();
        newOn.setIdCampionamento(idCampionamento);
        newOn.setNomeLatino(on.getNomeCompleto());
        newOn.setPresenza(on.getPresenza());
        newOn.setIdSpecieOn(on.getIdOrganismoNocivo());
        newOn.setDescSigla(on.getSigla());
        newOn.setEuro(on.getEuro());
        newOn.setAssociato("N");
        newOn.setDataUltimoAggiornamento(null);
        organismiNocivi.add(newOn);
      }          
     // session.setAttribute("onDtoProv", null); 
    } 
    if(onDtoDef!=null && !onDtoDef.isEmpty()) {
      for(CampionamentoSpecOnDTO on : onDtoDef) {
         on.setAssociato("S");
         organismiNocivi.add(on);
      }
    }
    //gestisco i file provv
    List<EsitoCampioneDTO> esiti=new ArrayList<EsitoCampioneDTO>();
    if(fileProv!=null && !fileProv.isEmpty()) {
      for(EsitoCampioneDTO obj : fileProv) {    
        AnfiDTO anfi= anfiEJB.findById(obj.getIdAnfi());
        CodiceEsitoDTO objEs = campioneEJB.findCodiceEsitoById(obj.getIdCodiceEsito());
        obj.setDescrizione(objEs.getDescrizione());
        obj.setTipologiaTestDiLaboratorio(anfi.getTipologiaTestDiLaboratorio());
        obj.setAssociato("N");
        esiti.add(obj);
      }          
    } 
    if(fileDef!=null && !fileDef.isEmpty()) {
      for(EsitoCampioneDTO esito : fileDef) {
        esito.setAssociato("S");
        esiti.add(esito);
      }
    }

    model.addAttribute("organismiNocivi", organismiNocivi);
    
    List<OrganismoNocivoDTO> listOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listOn", listOn);
    
   // List<EsitoCampioneDTO> esiti = campioneEJB.findEsitiByIdCampionamento(idCampionamento);
    model.addAttribute("esiti", esiti);

    List<AnfiDTO> listAnfi = anfiEJB.findValidi();
    model.addAttribute("listAnfi", listAnfi);
    
//    List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidi();
    List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidiMissione(dto.getIdMissione().intValue());
    if (listaIspettOperante == null)
      listaIspettOperante = new ArrayList<AnagraficaDTO>();
    if (!listaIspettOperante.contains(new AnagraficaDTO(dto.getIdAnagrafica()))) {
      AnagraficaDTO anag = anagraficaEJB.findById(dto.getIdAnagrafica());
      listaIspettOperante.add(anag);
    }
    model.addAttribute("listaIspettOperante", listaIspettOperante);
        
    List<SpecieVegetaleDTO> listaSpecieVegetali = specieVegetaleEJB.findValidi();
    model.addAttribute("listaSpecieVegetali", listaSpecieVegetali);
    
    List<TipoAreaDTO> tipoAree = tipoAreaEJB.findValidi();
    model.addAttribute("tipoAree", tipoAree);

    List<CodiceEsitoDTO> listaCodiciEsito = campioneEJB.findValidiCodiciEsito();
    model.addAttribute("listaCodiciEsito", listaCodiciEsito);

    setBreadcrumbs(model, request);
    showFlashMessages(model, request);
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");

    if (utente == null) {
      logger.info("utente null");
      if (RequestContextUtils.getInputFlashMap(request) != null) {
        logger.info("ripristino l'utente in sessione!");
        session.setAttribute("utenteAbilitazioni", model.asMap().get("utente"));
        utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      }
    }

    RuoloDTO ruolo = getRuolo(utente);
    model.addAttribute("ruolo", ruolo);
     
    boolean editabile = false;
    boolean editLaboratorio = false;
    boolean editSpecieON = false;
    
    int idStatoVerbale = missioneEJB.getIdStatoVerbaleByIdMissione(dto.getIdMissione());
    if (idStatoVerbale < 2 && request.getServletPath().indexOf("/edit") > -1) {   // lo stato verbale (se presente deve essere max 1 (bozza) per consentire modifiche
      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_CAMPIONI") && utente.isReadWrite();
      editLaboratorio = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_ESITI_LABORATORIO") && utente.isReadWrite();
      editSpecieON = ruolo.getAmministratore() || ruolo.getFunzionarioBO();
    }
    model.addAttribute("editabile", editabile);
    model.addAttribute("editLaboratorio", editLaboratorio);
    model.addAttribute("editSpecieON", editSpecieON);

    loadPopupComboDettaglio(model,session);
    return "gestionecampione/dettaglioCampionamento";
  }
  
  @RequestMapping(value = {"/gestioneCampione/pSave", "/gestioneCampione/pSaveCampioniFromListaMissioni", "/gestioneCampione/pSaveCampioniFromMissione", "/gestioneCampione/pSaveFromListaMissioni", "/gestioneCampione/pSaveFromMissione"})
  public String pSave(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    CampionamentoDTO dto = (CampionamentoDTO) model.asMap().get("campionamento");
    CampionamentoDTO dtoDb = campioneEJB.findById(dto.getIdCampionamento());
    if (dto.getIdAnagrafica()==null || dto.getIdAnagrafica() == 0) {
      dtoDb.setIdAnagrafica(dto.getIdAnagrafica());
    }
    if (dto.getIdSpecieVegetale()==null || dto.getIdSpecieVegetale() == 0) {
      dtoDb.setIdSpecieVegetale(dto.getIdSpecieVegetale());
    }
    if (dto.getIdTipoArea()==null || dto.getIdTipoArea() == 0) {
      dtoDb.setIdTipoArea(dto.getIdTipoArea());
    }
    if (dto.getLongitudine()==null || dto.getLongitudine() == 0) {
      dtoDb.setLongitudine(dto.getLongitudine());
    }
    if (dto.getLatitudine()==null || dto.getLatitudine() == 0) {
      dtoDb.setLatitudine(dto.getLatitudine());
    }

    //lo riassegno
    dto=dtoDb;
    
    List<OrganismoNocivoDTO> onDtoProv= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    List<CampionamentoSpecOnDTO> onDtoDef= (List<CampionamentoSpecOnDTO>)session.getAttribute("onDtoDef"); 
    List<EsitoCampioneDTO> fileProv= (List<EsitoCampioneDTO>)session.getAttribute("fileProv"); 
    List<EsitoCampioneDTO> fileDef= (List<EsitoCampioneDTO>)session.getAttribute("fileDef"); 
    // comune
    String comune = null;
    if (dto != null) {
      if (StringUtils.isNotBlank(dto.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(dto.getIstatComune());
        if (comuneDTO != null)
        {
          comune = comuneDTO.getDescrizioneComune();
          dto.setComune(comune);
          model.addAttribute("comune", comune);
        }
      }
    }

    model.addAttribute("campionamento", dto);

    List<FotoDTO> listaFoto = campioneEJB.findListFotoByIdCampione(dto.getIdCampionamento());
    model.addAttribute("listaFoto", listaFoto);
    
    if (dto.getIdIspezioneVisiva() != null && dto.getIdIspezioneVisiva().intValue() > 0) {
      IspezioneVisivaDTO visual = visualEJB.findById(dto.getIdIspezioneVisiva());
      //DATI AZIENDA
      if(visual.getCuaa() != null) {
        MissioneController missione = new MissioneController();
        ResponseEntity<?> anag = missione.getAnagraficheAviv(visual.getCuaa(), request, session);
        if(anag.getStatusCode().value()==200)
            model.addAttribute("anagraficaAzienda", anag);
          else {
              model.addAttribute("anagraficaAzienda", null);
    	      model.addAttribute("error", "Non è stato possibile recuperare i dati dell'azienda");
          }
      }
    }
    if(dto.getIspettoriSecondari()!=null)  {
      List<AnagraficaDTO> ispettoriSecondari = missioneEJB.getIspettoriAggiunti(dto.getIdMissione().longValue());
      model.addAttribute("ispettoriSecondari", ispettoriSecondari);    
    }else
      model.addAttribute("ispettoriSecondari", null);    
    
    List<CampionamentoSpecOnDTO> organismiNocivi=new ArrayList<CampionamentoSpecOnDTO>();
    //gestisco gli on provvisori
    if(onDtoProv!=null && !onDtoProv.isEmpty()) {
      for(OrganismoNocivoDTO on : onDtoProv) {
        CampionamentoSpecOnDTO newOn = new CampionamentoSpecOnDTO();
        newOn.setIdCampionamento(dto.getIdCampionamento());
        newOn.setNomeLatino(on.getNomeCompleto());
        newOn.setPresenza(on.getPresenza());
        newOn.setIdSpecieOn(on.getIdOrganismoNocivo());
        newOn.setDescSigla(on.getSigla());
        newOn.setEuro(on.getEuro());
        newOn.setAssociato("N");
        newOn.setDataUltimoAggiornamento(null);
        organismiNocivi.add(newOn);
      }          
     // session.setAttribute("onDtoProv", null); 
    } 
    if(onDtoDef!=null && !onDtoDef.isEmpty()) {
      for(CampionamentoSpecOnDTO on : onDtoDef) {
         organismiNocivi.add(on);
      }
    }
     
    //gestisco i file provv
    List<EsitoCampioneDTO> esiti=new ArrayList<EsitoCampioneDTO>();
    if(fileProv!=null && !fileProv.isEmpty()) {
      for(EsitoCampioneDTO obj : fileProv) {    
        esiti.add(obj);
      }          
    } 
    if(fileDef!=null && !fileDef.isEmpty()) {
      for(EsitoCampioneDTO esito : fileDef) {
        esito.setAssociato("S");
        esiti.add(esito);
      }
    } 
    model.addAttribute("organismiNocivi", organismiNocivi);
    
    List<OrganismoNocivoDTO> listOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listOn", listOn);
    
    //List<EsitoCampioneDTO> esiti = campioneEJB.findEsitiByIdCampionamento(dto.getIdCampionamento());
    model.addAttribute("esiti", esiti);

    List<AnfiDTO> listAnfi = anfiEJB.findValidi();
    model.addAttribute("listAnfi", listAnfi);

    //List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidi();
    List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidiMissione(dto.getIdMissione().intValue());
    model.addAttribute("listaIspettOperante", listaIspettOperante);
        
    List<SpecieVegetaleDTO> listaSpecieVegetali = specieVegetaleEJB.findValidi();
    model.addAttribute("listaSpecieVegetali", listaSpecieVegetali);
    
    List<TipoAreaDTO> tipoAree = tipoAreaEJB.findValidi();
    model.addAttribute("tipoAree", tipoAree);

    List<CodiceEsitoDTO> listaCodiciEsito = campioneEJB.findValidiCodiciEsito();
    model.addAttribute("listaCodiciEsito", listaCodiciEsito);

    setBreadcrumbs(model, request);
    showFlashMessages(model, request);
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    RuoloDTO ruolo = getRuolo(utente);
    model.addAttribute("ruolo", ruolo);
    if (utente == null) {
      logger.info("utente null");
      if (RequestContextUtils.getInputFlashMap(request) != null) {
        logger.info("ripristino l'utente in sessione!");
        session.setAttribute("utenteAbilitazioni", model.asMap().get("utente"));
      }
    }
    
    boolean editabile = false;
    boolean editLaboratorio = false;
    boolean editSpecieON = false;
    
    int idStatoVerbale = missioneEJB.getIdStatoVerbaleByIdMissione(dto.getIdMissione());
    if (idStatoVerbale < 2 && request.getServletPath().indexOf("/pSave") > -1) {   // lo stato verbale (se presente deve essere max 1 (bozza) per consentire modifiche
      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_CAMPIONI") && utente.isReadWrite();
      editLaboratorio = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_ESITI_LABORATORIO") && utente.isReadWrite();
      editSpecieON = ruolo.getAmministratore() || ruolo.getFunzionarioBO();
    }
    model.addAttribute("editabile", editabile);
    model.addAttribute("editLaboratorio", editLaboratorio);
    model.addAttribute("editSpecieON", editSpecieON);
    model.addAttribute("model", model);
    EsitoCampioneDTO esitoCampione = (EsitoCampioneDTO) model.asMap().get("esitoCampione");
    model.addAttribute("esitoCampione", esitoCampione);

    loadPopupComboDettaglio(model,session);
  //  model.addAttribute("editabile",true);
    return "gestionecampione/dettaglioCampionamento";
  }

  @RequestMapping(value = {"/gestioneCampione/save", "/gestioneCampione/saveCampioni", "/gestioneCampione/saveFromListaMissioni", "/gestioneCampione/saveFromMissione",
      "/gestioneCampione/saveCampioniFromListaMissioni", "/gestioneCampione/saveCampioniFromMissione"})
  public String save(Model model, @ModelAttribute("esitoCampione") EsitoCampioneDTO esitoCampione,
      @ModelAttribute("campionamento") CampionamentoDTO campionamento, @ModelAttribute("on") OrganismoNocivoDTO on,
      @RequestParam(value = "idCampionamento") Integer idCampionamento,HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    //DataFilter data = getFiltroDati(utente);
    RuoloDTO ruolo = getRuolo(utente);
    model.addAttribute("ruolo", ruolo);
    boolean editabile = false;
    boolean editLaboratorio = false;
    boolean editSpecieON = false;
    
    int idStatoVerbale = missioneEJB.getIdStatoVerbaleByIdMissione(campionamento.getIdMissione());
    if (idStatoVerbale < 2 && request.getServletPath().indexOf("/save") > -1) {   // lo stato verbale (se presente deve essere max 1 (bozza) per consentire modifiche
      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_CAMPIONI") && utente.isReadWrite();
      editLaboratorio = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_ESITI_LABORATORIO") && utente.isReadWrite();
      editSpecieON = ruolo.getAmministratore() || ruolo.getFunzionarioBO();
    }
    model.addAttribute("editabile", editabile);
    model.addAttribute("editLaboratorio", editLaboratorio);
    model.addAttribute("editSpecieON", editSpecieON);
    
    //EsitoCampioneDTO esitoCampioneDTO = esitoCampione;
    Errors errors = new Errors();

    if(editabile) {
      if (campionamento.getIdSpecieVegetale()==null || campionamento.getIdSpecieVegetale() == 0) {
        errors.addError("idSpecieVegetale", "Campo Obbligatorio. Indicare una specie vegetale");
      }

      if (campionamento.getIdTipoArea()==null || campionamento.getIdTipoArea() == 0) {
        errors.addError("idTipoArea", "Campo Obbligatorio. Indicare una tipologia area");
      }
      
      if (campionamento.getIdAnagrafica()==null || campionamento.getIdAnagrafica() == 0) {
        errors.addError("idAnagrafica", "Campo Obbligatorio. Indicare un ispettore operante");
      } 
      
      if (campionamento.getLatitudine()==null || campionamento.getLatitudine() == 0) {
        errors.addError("latitudine", "Campo Obbligatorio. Indicare una latitudine valida");
      }     
      
      if (campionamento.getLongitudine()==null || campionamento.getLongitudine() == 0) {
        errors.addError("longitudine", "Campo Obbligatorio. Indicare una longitudine valida");
      }   
    } 

    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni") > -1)
      prefix = "Campioni";
    if (request.getServletPath().indexOf("FromListaMissioni") > -1)
      prefix += "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione") > -1)
      prefix += "FromMissione";

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("esitoCampione", esitoCampione);
      redirectAttributes.addFlashAttribute("campionamento", campionamento);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      return "redirect:pSave"+prefix+".do";
    } else {
        try {
          if (IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_ESITI_LABORATORIO") && utente.isReadWrite()) {
            List<EsitoCampioneDTO> fileProv= (List<EsitoCampioneDTO>)session.getAttribute("fileProv"); 
            List<EsitoCampioneDTO> fileDef= (List<EsitoCampioneDTO>)session.getAttribute("fileDef"); 
            List<EsitoCampioneDTO> esiti = campioneEJB.findEsitiByIdCampionamento(idCampionamento);
            if(fileProv!=null) {
                for(EsitoCampioneDTO obj :fileProv){
                  obj.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
                  obj.setCodCampione(idCampionamento.toString());
                  obj.setIdEsitoCampione(0);
                  //inserisco l'esito
                  if (obj.getIdEsitoCampione() == null || obj.getIdEsitoCampione() == 0) {
                    obj = campioneEJB.insertEsito(obj);
                  }           
                }
            }
            //effettuo prima le remove
            if(esiti!=null) {
              for(EsitoCampioneDTO obj :esiti) {
                boolean trovato=false;
                for(EsitoCampioneDTO objDef : fileDef) {
                  System.out.println(obj.getIdEsitoCampione());
                  System.out.println(objDef.getIdEsitoCampione());
                  if(obj.getIdEsitoCampione().equals(objDef.getIdEsitoCampione())) {
                    trovato=true;
                  }
                }
                if(!trovato) {
                  campioneEJB.removeEsito(obj.getIdEsitoCampione());
                }
              }              
            }

            //effettuo eventuali modifiche
            if(fileDef!=null) {
              for(EsitoCampioneDTO obj :fileDef){
                if(obj.getModificato()!=null && obj.getModificato().equals("S")){
                  obj.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
                  obj.setCodCampione(idCampionamento.toString());
                    campioneEJB.updateEsito(obj);
                }
              }
            }
            model.addAttribute("success", "Esito Campione salvato con successo");
          }
          
          if(ruolo!= null && (ruolo.getFunzionarioBO() || ruolo.getAmministratore())) {
            List<CampionamentoSpecOnDTO> organismiNocivi = campioneEJB.findOnByIdCampionamento(idCampionamento);
            //aggiungo l'organismo nocivo provvisorio            
            List<OrganismoNocivoDTO> onDto= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
            if(onDto!=null && onDto.size()>0) {
              for(OrganismoNocivoDTO obj : onDto) {
                CampionamentoSpecOnDTO onSpec = new CampionamentoSpecOnDTO(campionamento.getIdCampionamento(),obj.getIdOrganismoNocivo(), (obj.getPresenza()!=null && obj.getPresenza().equalsIgnoreCase("S"))?"S":"N");
                onSpec.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());          
                campioneEJB.insertCampioneSpecieOn(onSpec);            
              }                  
            }
            //gestico la tabella dei definitivi
            List<CampionamentoSpecOnDTO> onDtoDef= (List<CampionamentoSpecOnDTO>)session.getAttribute("onDtoDef"); 

            for(CampionamentoSpecOnDTO obj :organismiNocivi) {
              boolean trovato=false;
              for(CampionamentoSpecOnDTO objDef :onDtoDef) {
                if(obj.getIdSpecieOn()==objDef.getIdSpecieOn()) {
                  trovato=true;
                }
              }
              if(!trovato) {
                campioneEJB.removeOn(idCampionamento, Long.valueOf(obj.getIdSpecieOn()).hashCode());
              }
            }
          }
          //aggiorno la rilevazione
          RilevazioneDTO rilevazione = new RilevazioneDTO();
          rilevazione.setIdRilevazione(campionamento.getIdRilevazione());
          rilevazione.setIdTipoArea(campionamento.getIdTipoArea());
          rilevazioneEJB.updateFilter(rilevazione);
  
          //modifico il campionamento
          CampionamentoDTO dto = campioneEJB.findById(campionamento.getIdCampionamento());
          //dto.setIdSpecieVegetale(campionamento.getIdSpecieVegetale());
          dto.setIdTipoArea(campionamento.getIdTipoArea());
          dto.setIdAnagrafica(campionamento.getIdAnagrafica());
          dto.setNote(campionamento.getNote());
          dto.setLatitudine(campionamento.getLatitudine());
          dto.setLongitudine(campionamento.getLongitudine());
          dto.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
          campioneEJB.update(dto);
          redirectAttributes.addFlashAttribute("campionamento", dto);
          //
          
          model.addAttribute("success", "Campionamento salvato con successo");
          
          Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
          session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
        }
        catch (Exception e) {
          logger.error("Errore nella registrazione dell'esito del campione: " + e.getMessage());
          model.addAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("esitoCampione", esitoCampione);
          redirectAttributes.addFlashAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("success", null);
          redirectAttributes.addFlashAttribute("model", model);
        }
    }
    return "redirect:search"+prefix+".do";
  }

  @RequestMapping(value = {"/gestioneCampione/addFile", "/gestioneCampione/addFileCampioni", "/gestioneCampione/addFileFromListaMissioni", "/gestioneCampione/addFileFromMissione"})
  public String addFile(Model model, @ModelAttribute("esitoCampione") EsitoCampioneDTO esitoCampione,
      @ModelAttribute("campionamento") CampionamentoDTO campionamento, @ModelAttribute("on") OrganismoNocivoDTO on,
      @RequestParam(value = "idCampionamento") Integer idCampionamento, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException, IOException
  {
    //CONTROLLO L'UTENTE
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    //DataFilter data = getFiltroDati(utente);
    RuoloDTO ruolo = getRuolo(utente);
    model.addAttribute("ruolo", ruolo);
    
    boolean editabile = false;
    boolean editLaboratorio = false;
    boolean editSpecieON = false;
    
    int idStatoVerbale = missioneEJB.getIdStatoVerbaleByIdMissione(campionamento.getIdMissione());
    if (idStatoVerbale < 2 && request.getServletPath().indexOf("/addFile") > -1) {   // lo stato verbale (se presente deve essere max 1 (bozza) per consentire modifiche
      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_CAMPIONI") && utente.isReadWrite();
      editLaboratorio = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_ESITI_LABORATORIO") && utente.isReadWrite();
      editSpecieON = ruolo.getAmministratore() || ruolo.getFunzionarioBO();
    }
    model.addAttribute("editabile", editabile);
    model.addAttribute("editLaboratorio", editLaboratorio);
    model.addAttribute("editSpecieON", editSpecieON);

    List<EsitoCampioneDTO> fileProv= (List<EsitoCampioneDTO>)session.getAttribute("fileProv"); 
    if(fileProv==null || fileProv.isEmpty()) {
      fileProv= new ArrayList<EsitoCampioneDTO>();
    }
    
    //mi carico anche i def
    List<EsitoCampioneDTO> fileDef= (List<EsitoCampioneDTO>)session.getAttribute("fileDef");
    
    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni.do") > -1)
      prefix = "Campioni";
    else
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    if(esitoCampione.getAssociato()!=null && !esitoCampione.getAssociato().equals("S")) {
      //gestione prov
      if(esitoCampione.getIdEsitoCampione()>0) {
        for(EsitoCampioneDTO obj : fileProv) {
          if(obj.getIdEsitoCampione().equals(esitoCampione.getIdEsitoCampione())) {
            if(esitoCampione.getReferto()==null || esitoCampione.getReferto().getSize()==0) {
                esitoCampione.setFileByte(obj.getFileByte());           
            }

            fileProv.remove(obj);  
            try
            {
              if(esitoCampione.getReferto()!=null && esitoCampione.getReferto().getSize()!=0) {
                byte[] bytes = esitoCampione.getReferto().getBytes();
                esitoCampione.setFileByte(bytes);            
              }            
            }
            catch (IOException e)
            {
              e.printStackTrace();
            }
            
            fileProv.add(esitoCampione);
            redirectAttributes.addFlashAttribute("campionamento", campionamento);
            redirectAttributes.addFlashAttribute("utente", utente);
            model.addAttribute("campionamento", campionamento);
            return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=addFile";
          }        
        }
      }else {
        int prog = fileProv.size()+1;
        esitoCampione.setIdEsitoCampione(prog);
        try
        {
          byte[] bytes = esitoCampione.getReferto().getBytes();
          esitoCampione.setFileByte(bytes);
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        fileProv.add(esitoCampione);
      }
      session.setAttribute("fileProv", fileProv);
    }
    else
    {
      if(esitoCampione.getIdEsitoCampione()>0) {
        for(EsitoCampioneDTO obj : fileDef) {
          if(obj.getIdEsitoCampione().equals(esitoCampione.getIdEsitoCampione())) {
            AnfiDTO anfi = anfiEJB.findById(esitoCampione.getIdAnfi());
            CodiceEsitoDTO objEs = campioneEJB.findCodiceEsitoById(esitoCampione.getIdCodiceEsito());
            esitoCampione.setDescrizione(objEs.getDescrizione());
            esitoCampione.setTipologiaTestDiLaboratorio(anfi.getTipologiaTestDiLaboratorio());
            if(esitoCampione.getReferto()==null || esitoCampione.getReferto().getSize()==0) {
                byte[] pdf = campioneEJB.getPdf(obj.getIdEsitoCampione());
                //EsitoCampioneDTO esito = campioneEJB.findEsitoById(obj.getIdEsitoCampione());              
                //byte[] pdf = esito.getReferto().getBytes();
                esitoCampione.setFileByte(pdf);
                fileDef.remove(obj);
            }else {
                byte[] bytes = esitoCampione.getReferto().getBytes();
                esitoCampione.setFileByte(bytes);
                fileDef.remove(obj);
            }
            esitoCampione.setModificato("S");
            fileDef.add(esitoCampione);
            redirectAttributes.addFlashAttribute("campionamento", campionamento);
            redirectAttributes.addFlashAttribute("utente", utente);
            model.addAttribute("campionamento", campionamento);
            return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=addFile";
          }
        }
      }
      session.setAttribute("fileDef", fileDef);
     }
    redirectAttributes.addFlashAttribute("campionamento", campionamento);
    redirectAttributes.addFlashAttribute("utente", utente);
    model.addAttribute("campionamento", campionamento);    
    return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=addFile";
  }

  @RequestMapping(value = {"/gestioneCampione/addOrganismoNocivo", "/gestioneCampione/addOrganismoNocivoCampioni", "/gestioneCampione/addOrganismoNocivoFromListaMissioni", "/gestioneCampione/addOrganismoNocivoFromMissione"}, method = RequestMethod.POST)
  public String addOrganismoNocivo(Model model, @ModelAttribute("campionamentoSpecOn") CampionamentoSpecOnDTO campionamentoSpecOn, 
      @RequestParam(value = "idCampionamento") Integer idCampionamento, 
      @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, 
      @RequestParam(value = "presenza") String presenza, @ModelAttribute("campionamento") CampionamentoDTO campionamento,
      HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    List<OrganismoNocivoDTO> listOn= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    if(listOn==null || listOn.isEmpty()) {
      listOn= new ArrayList<OrganismoNocivoDTO>();
    }
    OrganismoNocivoDTO onDto = new OrganismoNocivoDTO();
    onDto = organismoNocivoEJB.findById(idOrganismoNocivo);
 //   onDto.setIdOrganismoNocivo(idOrganismoNocivo);
    onDto.setPresenza(presenza);
    listOn.add(onDto);
    session.setAttribute("onDtoProv", listOn);  
    redirectAttributes.addFlashAttribute("campionamento", campionamento);
    model.addAttribute("campionamento", campionamento);
    
    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni.do") > -1)
      prefix = "Campioni";
    else
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=addOn";
  }

  @RequestMapping(value = {"/gestioneCampione/removeOrganismoNocivo", "/gestioneCampione/removeOrganismoNocivoCampioni", "/gestioneCampione/removeOrganismoNocivoFromListaMissioni", "/gestioneCampione/removeOrganismoNocivoFromMissione"}, method = RequestMethod.GET)
  public String removeOrganismoNocivo(Model model, @ModelAttribute("campionamentoSpecOn") CampionamentoSpecOnDTO campionamentoSpecOn,
      @RequestParam(value = "idCampionamento") Integer idCampionamento, @ModelAttribute("campionamento") CampionamentoDTO campionamento,
      @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    if (idOrganismoNocivo == null || idOrganismoNocivo.intValue() == 0) {
      errors.addError("Organismo nocivo", "Campo Obbligatorio");
    }
    if (idCampionamento == null || idCampionamento.intValue() == 0) {
      errors.addError("Campionamento", "Campo Obbligatorio");
    }
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      //redirectAttributes.addFlashAttribute("esitoCampione", esitoCampione);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
    } else {
        try {
          campioneEJB.removeOn(idCampionamento, idOrganismoNocivo);
          Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
          session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
        }
        catch (Exception e) {
          logger.error("Errore nella aggiunta organismo nocivo al campione: " + e.getMessage());
          model.addAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("success", null);
          redirectAttributes.addFlashAttribute("model", model);
        }
    }
    redirectAttributes.addFlashAttribute("campionamento", campionamento);
    model.addAttribute("campionamento", campionamento);
    
    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni.do") > -1)
      prefix = "Campioni";
    else
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=deleteOn";
  }

  @RequestMapping(value = {"/gestioneCampione/removeOrganismoNocivoProv", "/gestioneCampione/removeOrganismoNocivoProvCampioni", "/gestioneCampione/removeOrganismoNocivoProvFromListaMissioni", "/gestioneCampione/removeOrganismoNocivoProvFromMissione"}, method = RequestMethod.POST)
  public String removeOrganismoNocivoProv(Model model, @RequestParam(value = "idCampionamento") Integer idCampionamento,
      @ModelAttribute("campionamento") CampionamentoDTO campionamento,
      @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    List<OrganismoNocivoDTO> listOn= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    redirectAttributes.addFlashAttribute("campionamento", campionamento);
    model.addAttribute("campionamento", campionamento);  

    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni.do") > -1)
      prefix = "Campioni";
    else
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    for(OrganismoNocivoDTO obj : listOn) {
     // System.out.println(obj.getIdOrganismoNocivo());
      if(obj.getIdOrganismoNocivo().equals(idOrganismoNocivo)) {
        listOn.remove(obj);  
        session.setAttribute("onDtoProv",listOn); 
        return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&editabile=true&from=deleteOnProv";        
       }      
    }
    session.setAttribute("onDtoProv",listOn);
    return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=deleteOnProv";
  }

  @RequestMapping(value = {"/gestioneCampione/removeOrganismoNocivoDef", "/gestioneCampione/removeOrganismoNocivoDefCampioni", "/gestioneCampione/removeOrganismoNocivoDefFromListaMissioni", "/gestioneCampione/removeOrganismoNocivoDefFromMissione"}, method = RequestMethod.POST)
  public String removeOrganismoNocivoDef(Model model, @RequestParam(value = "idCampionamento") Integer idCampionamento,
      @ModelAttribute("campionamento") CampionamentoDTO campionamento,
      @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    redirectAttributes.addFlashAttribute("campionamento", campionamento);
    model.addAttribute("campionamento", campionamento);
    List<CampionamentoSpecOnDTO> organismiNocivi = campioneEJB.findOnByIdCampionamento(idCampionamento);   
    List<CampionamentoSpecOnDTO> onDtoDef= (List<CampionamentoSpecOnDTO>)session.getAttribute("onDtoDef"); 
    if(onDtoDef!=null && !onDtoDef.isEmpty())
      organismiNocivi=onDtoDef;

    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni.do") > -1)
      prefix = "Campioni";
    else
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    for(CampionamentoSpecOnDTO obj : organismiNocivi) {
     // System.out.println(obj.getIdOrganismoNocivo());
      if(obj.getIdSpecieOn()==idOrganismoNocivo) {
             // obj.setAssociato("N"); 
        organismiNocivi.remove(obj);  
        session.setAttribute("onDtoDef",organismiNocivi); 
        return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&editabile=true&from=deleteOnDef";        
       }      
    }
    session.setAttribute("onDtoDef",organismiNocivi); 
    return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=deleteOnDef";
  }

  @RequestMapping(value = {"/gestioneCampione/removeFileProv", "/gestioneCampione/removeFileProvCampioni", "/gestioneCampione/removeFileProvFromListaMissioni", "/gestioneCampione/removeFileProvFromMissione"}, method = RequestMethod.POST)
  public String removeFileProv(Model model, @RequestParam(value = "idCampionamento") Integer idCampionamento,
      @ModelAttribute("campionamento") CampionamentoDTO campionamento,
      @RequestParam(value = "idEsitoCampione") Integer idEsitoCampione, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    List<EsitoCampioneDTO> fileProv= (List<EsitoCampioneDTO>)session.getAttribute("fileProv");
    redirectAttributes.addFlashAttribute("campionamento", campionamento);
    model.addAttribute("campionamento", campionamento);
    
    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni.do") > -1)
      prefix = "Campioni";
    else
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    for(EsitoCampioneDTO obj : fileProv) {
     // System.out.println(obj.getIdOrganismoNocivo());
      if(obj.getIdEsitoCampione().equals(idEsitoCampione)) {
        fileProv.remove(obj);
        session.setAttribute("fileProv",fileProv);
        return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&editabile=true&from=deleteFileProv";
       }
    }
    session.setAttribute("fileProv",fileProv);
    return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=deleteFileProv";
  }
  
  @RequestMapping(value = {"/gestioneCampione/removeFileDef", "/gestioneCampione/removeFileDefCampioni", "/gestioneCampione/removeFileDefFromListaMissioni", "/gestioneCampione/removeFileDefFromMissione"}, method = RequestMethod.POST)
  public String removeFileDef(Model model, @RequestParam(value = "idCampionamento") Integer idCampionamento,
      @ModelAttribute("campionamento") CampionamentoDTO campionamento,
      @RequestParam(value = "idEsitoCampione") Integer idEsitoCampione, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    model.addAttribute("utente", utente);
    
    List<EsitoCampioneDTO> fileDef= (List<EsitoCampioneDTO>)session.getAttribute("fileDef"); 
    redirectAttributes.addFlashAttribute("campionamento", campionamento);
    model.addAttribute("campionamento", campionamento);
    
    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni.do") > -1)
      prefix = "Campioni";
    else
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    for(EsitoCampioneDTO obj : fileDef) {
     // System.out.println(obj.getIdOrganismoNocivo());
      if(obj.getIdEsitoCampione().equals(idEsitoCampione)) {
        fileDef.remove(obj);
        session.setAttribute("fileDef",fileDef);
        return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&editabile=true&from=deleteFileDef";
       }
    }
    session.setAttribute("fileDef",fileDef);
    return "redirect:edit"+prefix+".do?idCampionamento=" + idCampionamento + "&from=deleteFileDef";
  }

  @RequestMapping(value="/gestioneCampione/getReferto", method=RequestMethod.GET, produces = "application/pdf")
  public ResponseEntity<byte[]> getReferto(@RequestParam(value = "idEsitoCampione") Integer idEsitoCampione, HttpServletResponse response) {

      try {
        
        logger.debug("getPDF");
        EsitoCampioneDTO esito = campioneEJB.findEsitoById(idEsitoCampione);
        
        byte[] pdf = esito.getReferto().getBytes();

        HttpHeaders headers = new HttpHeaders();
        //MediaType mediaType = MediaType.parseMediaType("application/pdf");
        //headers.setContentType(mediaType);
        // Here you have to set the actual filename of your pdf
        String filename = "referto_" + idEsitoCampione + ".pdf";
        //headers.setContentDispositionFormData("inline", filename);
        headers.add("Content-Disposition", "inline; filename=" + filename);
        headers.add("Content-Type", "application/pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<byte[]>(pdf, headers, HttpStatus.OK);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
  }

  @RequestMapping(value = {"/gestioneCampione/removeEsito", "/gestioneCampione/removeEsitoCampioni", "/gestioneCampione/removeEsitoFromListaMissioni", "/gestioneCampione/removeEsitoFromMissione"}, method = RequestMethod.GET)
  public String removeEsito(Model model, @RequestParam(value = "id") Integer id, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    EsitoCampioneDTO esito = null;
    try {
      
      esito = campioneEJB.findEsitoById(id);
      
      if (esito == null) {
        logger.debug("Esito campione con id " + id + " non trovato");
        ErrorResponse er = new ErrorResponse();
        er.setMessage("Esito campione con id " + id + " non trovato");
      }
      else {
        campioneEJB.removeEsito(id);
      }
    } catch (Exception e) {
        logger.debug("Errore nel metodo getMissione: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella cancellazione della missione");
    }

    String prefix = "";
    
    if (request.getServletPath().indexOf("saveCampioni.do") > -1)
      prefix = "Campioni";
    else
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    return "redirect:edit"+prefix+".do?idCampionamento=" + esito.getIdCampionamento() + "&from=deleteEsito";
  }

  @RequestMapping(value = "/gestioneCampione/searchComuni", produces = "application/json", method = RequestMethod.POST)
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
  
  @RequestMapping(value = "/gestioneCampione/addOn")
  public String addOn(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableEsitoCampione");
      return "gestionecampione/confermaElimina";
  }

  @RequestMapping(value = "/gestioneCampione/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableEsitoCampione");
      return "gestionecampione/confermaElimina";
  }

  @RequestMapping(value = "/gestioneCampione/deleteOn")
  public String canDeleteOn(Model model, @RequestParam(value = "idCampionamento") String idCampionamento, @RequestParam(value = "idOrganismoNocivo") String idOrganismoNocivo, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("idCampionamento",idCampionamento);
      model.addAttribute("idOrganismoNocivo",idOrganismoNocivo);
      model.addAttribute("table","tableOn");
      return "gestionecampione/confermaEliminaOn";
  }

  @RequestMapping(value = "/rest/campione", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
  public @ResponseBody ResponseEntity<?> creaCampione(@Valid @RequestBody CampionamentoDTO body, HttpServletRequest request) throws MalformedURLException, IOException
  {   
    try {
      String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      String cf = TokenUtils.verifyToken(jwt);
      AnagraficaDTO anagrafica = new AnagraficaDTO();
      anagrafica.setCfAnagraficaEst(cf);
      anagrafica.setActive(true);         // deve prendere l'anagrafica non storicizzata
      List<AnagraficaDTO> ispettori = anagraficaEJB.findByFilter(anagrafica);
      if (ispettori == null || ispettori.size() == 0) {
        logger.debug("Errore Token: codice fiscale ispettore non trovato (creaCampione)");
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", "Errore Token: codice fiscale ispettore non trovato (creaCampione)");
        err.setMessage("Errore Token: codice fiscale ispettore non trovato (creaCampione)");
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
      }

      AnagraficaDTO ispettore = ispettori.get(0);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(body);
      logger.debug(json);

//      if (StringUtils.isBlank(body.getOraInizio())) {
//          throw new Exception("L'ora inizio è obbligatoria");
//        }
      Integer idRilevazione = body.getIdRilevazione();
      RilevazioneDTO rilev = rilevazioneEJB.findById(idRilevazione);
      MissioneDTO missione = missioneEJB.findById(rilev.getIdMissione().longValue());
      
      CampionamentoDTO campionamentoDTO = new CampionamentoDTO();

      //Creo l'oggetto campione
      campionamentoDTO.setIdCampionamento(body.getIdCampionamento());
      campionamentoDTO.setIdRilevazione(body.getIdRilevazione());
      if(body.getIdSpecieVegetale() != null)
        campionamentoDTO.setIdSpecieVegetale(body.getIdSpecieVegetale());
      else
        campionamentoDTO.setIdSpecieVegetale(0);
      if(body.getIdTipoCampione() != null)
        campionamentoDTO.setIdTipoCampione(body.getIdTipoCampione());
      else
        campionamentoDTO.setIdTipoCampione(0);
      campionamentoDTO.setIstatComune(body.getIstatComune());
      campionamentoDTO.setLatitudine(body.getLatitudine());
      campionamentoDTO.setLongitudine(body.getLongitudine());
      campionamentoDTO.setIdRilevazione(body.getIdRilevazione());
      campionamentoDTO.setDataRilevazione(body.getDataRilevazione());
      campionamentoDTO.setExtIdUtenteAggiornamento(body.getExtIdUtenteAggiornamento());
      campionamentoDTO.setDataUltimoAggiornamento(body.getDataUltimoAggiornamento());
      campionamentoDTO.setIdAnagrafica(ispettore.getIdAnagrafica());
      campionamentoDTO.setIdIspezioneVisiva(body.getIdIspezioneVisiva());
      campionamentoDTO.setPresenza(body.getPresenza());
      
      String dataOraInizioStr = null;
      if (StringUtils.isNotBlank(body.getOraInizio()))
    	  dataOraInizioStr  = sdf.format(missione.getDataOraInizioMissione()) + " " + body.getOraInizio() + ":00";
      else
    	  dataOraInizioStr  = sdf.format(missione.getDataOraInizioMissione()) + " " +  "00:00:00";
      Date dataOraInizio = dtf.parse(dataOraInizioStr);
      campionamentoDTO.setDataOraInizio(dataOraInizio);
      Date dataOraFine = null;
      if (StringUtils.isNotBlank(body.getOraFine())) {
        String dataOraFineStr = sdf.format(missione.getDataOraInizioMissione()) + " " + body.getOraFine() + ":00";
        dataOraFine = dtf.parse(dataOraFineStr);
        campionamentoDTO.setDataOraFine(dataOraFine);
      } else
    	  campionamentoDTO.setDataOraFine(null);

      campionamentoDTO.setNote(body.getNote());

      try {
        UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
        campionamentoDTO.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
      } catch (Exception e) {
        campionamentoDTO.setExtIdUtenteAggiornamento(0L);
      }

      Integer idCampionamento = null;
      try
      {
        if (body.getIdCampionamento()== null || body.getIdCampionamento().intValue() == 0) {
          idCampionamento = campioneEJB.insert(campionamentoDTO);
        
          Long i = gpsFotoEJB.selectFotoCampione(Long.valueOf(idCampionamento));
          if (i>0) {
            body.setFoto(true);
          }
          else
            body.setFoto(false);


          body.setIdCampionamento(idCampionamento);
          
          RilevazioneDTO rilevazione = rilevazioneEJB.findById(body.getIdRilevazione());
          Integer idMissione = rilevazione.getIdMissione();
          body.setIdMissione(idMissione);
          
          Date dataInizio = new Date(dataOraInizio.getTime());
          body.setDataOraInizio(dataInizio);
          if(dataOraFine != null)
            body.setDataOraFine(dataOraFine);
       
        }
        else {
          campioneEJB.update(campionamentoDTO);
          
          //campionamentoDTO.setIdCampionamento(idCampionamento);
          campioneEJB.removeCampioneSpecieOn(body.getIdCampionamento());
          for(int j=0; j<body.getOrganismiNocivi().length; j++) {
            //campionamentoDTO.setIdSpecieOn(body.getOrganismiNocivi()[j]);
            CampionamentoSpecOnDTO dto = new CampionamentoSpecOnDTO(campionamentoDTO.getIdCampionamento(), body.getOrganismiNocivi()[j], campionamentoDTO.getPresenza());
            dto.setExtIdUtenteAggiornamento(campionamentoDTO.getExtIdUtenteAggiornamento());
            campioneEJB.insertCampioneSpecieOn(dto);
          }
          //campioneEJB.updateCampioneSpecieOn(campionamentoDTO);
          RilevazioneDTO rilevazione = rilevazioneEJB.findById(body.getIdRilevazione());
          Integer idMissione = rilevazione.getIdMissione();
          body.setIdMissione(idMissione);
          
          body.setDataOraInizio(dataOraInizio);
          if(dataOraFine != null)
            body.setDataOraFine(dataOraFine);
        }
      }
      catch (InternalUnexpectedException e)
      {
        e.printStackTrace();
        logger.debug("Errore nel metodo creaCampione in fase di insert: " + e.getMessage());
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", e.getMessage());
        err.setMessage("Errore nel metodo creaCampione durante la registrazione: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    catch(Throwable e) {
      logger.debug("Errore nel metodo creaCampione durante la registrazione: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo creaCampione durante la registrazione: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<CampionamentoDTO>(body, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/campione/{idCampionamento}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<?> deleteCampione(@PathVariable (value = "idCampionamento") Integer idCampionamento, HttpServletRequest request)
  {
    try {
      logger.debug("id_campionamento: " + idCampionamento);
      CampionamentoRequest campionamentoRequest = new CampionamentoRequest();
      campionamentoRequest.setIdCampionamento(idCampionamento);
      List<CampionamentoDTO> list = campioneEJB.findByFilter(campionamentoRequest, null, null);
      if (list == null || list.size() == 0) {
        throw new Exception("Campionamento con id " + idCampionamento + " non trovato");
      }
      
      campioneEJB.remove(idCampionamento);
      
    } catch (Exception e) {
        logger.debug("Errore nel metodo deleteRilevazione: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella cancellazione della rilevazione: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Map<String, String> ok = new HashMap<String, String>();
    ok.put("status", "ok");
    return new ResponseEntity<java.util.Map<String,String>>(ok,HttpStatus.OK);
  }

  @RequestMapping(value = {"/gestioneCampione/searchCampioni", "/gestioneCampione/searchCampioniFromListaMissioni", "/gestioneCampione/searchCampioniFromMissione"})
  public String searchCampioni(Model model, @RequestParam(value = "idIspezioneVisiva", required = false) Integer idIspezioneVisiva, @RequestParam(value = "editabile", required = false) Boolean editabile,
      HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    Integer idIspezione = idIspezioneVisiva;
    if (idIspezione == null)
      idIspezione = (Integer) session.getAttribute("idIspezioneVisiva");
    else
      session.setAttribute("idIspezioneVisiva", idIspezioneVisiva);

    CampionamentoRequest cr = new CampionamentoRequest();
    cr.setIdIspezioneVisiva(idIspezione);
    session.setAttribute("campionamentoRequest", cr);
    
    setBreadcrumbs(model, request);
    showFlashMessages(model, request);
    // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    model.addAttribute("dataFilter", dataFilter);

    return "gestionecampione/elencoCampioni";
  }
  
  @RequestMapping(value = "/gestioneCampione/getCampioneVisualJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getCampioneVisualJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    Integer idIspezioneVisiva = (Integer) session.getAttribute("idIspezioneVisiva");
    List<CampionamentoDTO> lista = campioneEJB.findByIdIspezione(idIspezioneVisiva);
    if (lista == null) {
      lista = new ArrayList<>();
    }

    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }
  
  @RequestMapping(value = "/gestioneCampione/remove")
  public RedirectView remove(Model model, @RequestParam(value = "id") String id, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    try {
      campioneEJB.remove(Integer.decode(id));
    }
    catch (InternalUnexpectedException e)
    {
        boolean isFound = e.getCause().getMessage().indexOf("ORA-02292") !=-1? true: false; //true
        if(isFound) {
          // campioneEJB.updateDataFineValidita(Integer.decode(id));
        }
    }      
    return new RedirectView("search.do", true);
  }
  
  @Lazy
  @RequestMapping(value = "/gestioneCampione/gestioneCampioniExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    CampionamentoRequest campionamentoRequest = (CampionamentoRequest) session.getAttribute("campionamentoRequest") ;
    // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    List<CampionamentoDTO> elenco = campioneEJB.findByFilter(campionamentoRequest, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
//    for(CampionamentoDTO campionamento: elenco) {        
//      ComuneDTO comuneDTO = quadroEJB.getComune(campionamento.getIstatComune()!=null ? campionamento.getIstatComune() : "notfou");
//      campionamento.setComune(comuneDTO!=null ? comuneDTO.getDescrizioneComune() : " ");
//    }
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"campionamento.xls\"");
    return new ModelAndView("excelGestioneCampioniView", "elenco", elenco);
  }
  
  private void loadPopupComboDettaglio(Model model, HttpSession session) throws InternalUnexpectedException
  {
    CampionamentoDTO campionamento = (CampionamentoDTO) model.asMap().get("campionamento");
    
    // Ispettori secondari
    List<AnagraficaDTO> all_ispettoriSecondari = null;
    if (session.getAttribute("checkboxAllIspettoriSecondari") != null && ((String) session.getAttribute("checkboxAllIspettoriSecondari")).equals("true")) {
      all_ispettoriSecondari = anagraficaEJB.findAll();
    } else {
      all_ispettoriSecondari = anagraficaEJB.findValidi();
    }
    model.addAttribute("all_ispettoriSecondari", all_ispettoriSecondari);
 
    if (campionamento != null) {
      // ispettori secondari
      if (campionamento.getIspettoriAggiunti() != null && !campionamento.getIspettoriAggiunti().equals(""))
      {
          List<AnagraficaDTO> listaIspettSecondari=anagraficaEJB.findByIdMultipli(campionamento.getIspettoriAggiunti());
          model.addAttribute("listaIspettSecondari",listaIspettSecondari);          
      }
    }
  }
  
  @RequestMapping(value = "/gestioneCampione/getCodiceEsitoJson", produces = "application/json")
  @ResponseBody
  public /*List<AnagraficaDTO>*/String getCodiceEsitoJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    List<CodiceEsitoDTO> lista = campioneEJB.findAllCodiciEsito();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }
  
  @RequestMapping(value="/gestioneCampione/getpdf", method=RequestMethod.GET, produces = "application/pdf")
  public ResponseEntity<byte[]> getPDF(@RequestParam(value = "id") Integer id,
      @RequestParam(value = "associato") String associato,
      HttpServletResponse response, HttpSession session) {

      byte[] pdf = null;
      EsitoCampioneDTO esito=null;
      List<EsitoCampioneDTO> fileProv= (List<EsitoCampioneDTO>)session.getAttribute("fileProv"); 
      try
      {
        logger.debug("getPDF");
        if(associato.equals("N")) {
          for(EsitoCampioneDTO ob : fileProv) {
            if(ob.getIdEsitoCampione()==id) {
               pdf = ob.getFileByte();
               esito=ob;
            }
          }
        }else {
          pdf = campioneEJB.getPdf(id);
          esito = campioneEJB.findEsitoById(id);
        }
        HttpHeaders headers = new HttpHeaders();
        //MediaType mediaType = MediaType.parseMediaType("application/pdf");
        //headers.setContentType(mediaType);
        // Here you have to set the actual filename of your pdf
        //String filename = "verbale.pdf";
        //headers.setContentDispositionFormData("inline", filename);
        
        headers.add("Content-Disposition", "inline; filename=" + esito.getNomeFile()+".pdf");
        headers.add("Content-Type", "application/pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<byte[]>(pdf, headers, HttpStatus.OK);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
  }
  
  @RequestMapping(value = "/gestioneCampione/foto")
  public String showFoto(Model model, @RequestParam(value = "id") Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    FotoDTO foto = gpsFotoEJB.findFotoById(id);
    // comune
    String comune = null;
    if(foto != null) {
      if (StringUtils.isNotBlank(foto.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(foto.getIstatComune());
        if (comuneDTO != null)
        {
          comune = comuneDTO.getDescrizioneComune();
          foto.setDescrComune(comune);
         // model.addAttribute("comune", comune);
        } else   {
          foto.setDescrComune("Non disponibile");
        }
      }
    }
    model.addAttribute("foto", foto);
    setBreadcrumbs(model, request);
    return "gestioneGpsFoto/dettaglioFoto";
  }

}
