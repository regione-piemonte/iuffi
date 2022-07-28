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
import org.springframework.dao.DuplicateKeyException;
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
import it.csi.iuffi.iuffiweb.business.IGestioneTrappolaEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneVisualEJB;
import it.csi.iuffi.iuffiweb.business.IGpsFotoEJB;
import it.csi.iuffi.iuffiweb.business.IMissioneEJB;
import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.business.IRilevazioneEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.business.ITipoAreaEJB;
import it.csi.iuffi.iuffiweb.business.ITipoTrappolaEJB;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.OperazioneTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RilevazioneDTO;
import it.csi.iuffi.iuffiweb.model.RuoloDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaggioDTO;
import it.csi.iuffi.iuffiweb.model.api.Trappola;
import it.csi.iuffi.iuffiweb.model.api.Trappolaggio;
import it.csi.iuffi.iuffiweb.model.request.TrappolaggioRequest;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.TokenUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteAnagraficaVO;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservRESTClient;

@Controller
@IuffiSecurity(value = "GESTIONETRAPPOLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class GestioneTrappoleController extends TabelleController
{
  
  @Autowired
  private IGestioneTrappolaEJB trappoleEJB;
  @Autowired
  private IAnagraficaEJB anagraficaEJB;
  @Autowired
  private ISpecieVegetaleEJB specieVegetaleEJB;  
  @Autowired
  private ITipoAreaEJB tipoAreaEJB;  
  @Autowired
  private ITipoTrappolaEJB tipoTrappolaEJB;  
  @Autowired
  private IQuadroEJB quadroEJB;
  @Autowired
  private IRicercaEJB ricercaEJB;
  @Autowired
  private IRilevazioneEJB rilevazioneEJB;
  @Autowired
  private IMissioneEJB missioneEJB;
  @Autowired
  private IGestioneVisualEJB visualEJB;
  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;
  @Autowired  
  private IGpsFotoEJB gpsFotoEJB;
  
  private static final SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/gestioneTrappole/showFoto")
  public String showFoto(Model model, @RequestParam(value = "idTrappolaggio") Integer idTrappolaggio, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    List<FotoDTO> listaFoto= trappoleEJB.findListFotoByIdTrappolaggio(idTrappolaggio);
    model.addAttribute("listaFoto",listaFoto);
   
    setBreadcrumbs(model, request);
    return "gestionetrappole/foto";
  }

  @RequestMapping(value = "/gestioneTrappole/showOn")
  public String showOn(Model model, @RequestParam(value = "idTrappola") Integer idTrappola, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    List<OrganismoNocivoDTO> listaOn= trappoleEJB.findOnByIdTrappola(idTrappola);
    model.addAttribute("listaOn",listaOn);
   
    setBreadcrumbs(model, request);
    return "gestionetrappole/organismiNocivi";
  }
  
  @RequestMapping(value = "/gestioneTrappole/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    TrappolaggioRequest trappolaggioRequest = new TrappolaggioRequest();
    if (session.getAttribute("trappolaggioRequest") != null) {
      trappolaggioRequest = (TrappolaggioRequest) session.getAttribute("trappolaggioRequest");
    }
  
    if (trappolaggioRequest.getAnno() == null) {
      Calendar now = Calendar.getInstance();
      int currentYear = now.get(Calendar.YEAR);
      trappolaggioRequest.setAnno(currentYear);
    }
    model.addAttribute("trappolaggioRequest", trappolaggioRequest);
    loadPopupCombo(model, session);
    setBreadcrumbs(model, request);
    session.setAttribute("currentPage", 1);

    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("tableTrappole");
    cleanTableMapsInSession(session, tableNamesToRemove);
    
    return "gestionetrappole/ricercaTrappole";
  }
  
  @RequestMapping(value = {"/gestioneTrappole/search", "/gestioneTrappole/searchFromListaMissioni", "/gestioneTrappole/searchFromMissione"})
  public String search(Model model, @ModelAttribute("gestioneTrappola") TrappolaggioRequest trappolaggioRequest, HttpSession session, HttpServletResponse response,HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    try
    {
      if (trappolaggioRequest.checkNull() || trappolaggioRequest.getIdTrappolaggio()!=null)
        trappolaggioRequest = (TrappolaggioRequest) session.getAttribute("trappolaggioRequest");
      if (trappolaggioRequest == null)
        trappolaggioRequest = new TrappolaggioRequest();
      
      //TrappolaggioRequest datiRicerca = trappolaggioRequest;
      session.setAttribute("trappolaggioRequest", trappolaggioRequest);
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);

    //CARICO LE LISTE
    List<Integer> listaTipoArea = trappolaggioRequest.getTipoArea();
    List<Integer> listaSpecie = trappolaggioRequest.getSpecieVegetale();
    List<Integer> listaOrganismo = trappolaggioRequest.getOrganismoNocivo();
    List<Integer> listaIspetAssegnato = trappolaggioRequest.getIspettoreAssegnato();
    List<Integer> listaIspetSecondario = trappolaggioRequest.getIspettoriSecondari();
    List<Integer> listaTipoTrappole = trappolaggioRequest.getTipoTrappola();
    List<OperazioneTrappolaDTO> listaTipoOperazione = new ArrayList<OperazioneTrappolaDTO>(); //.getTipoOperazione();

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
    //trappole
    if(listaTipoTrappole!=null) {
      String idTrappole="";
      for(Integer i : listaTipoTrappole) {
        idTrappole+=i+",";
      }
      idTrappole=idTrappole.substring(0,idTrappole.length()-1);
      List<TipoTrappolaDTO> listaTrappole=tipoTrappolaEJB.findByIdMultipli(idTrappole);
      model.addAttribute("listaTrappole",listaTrappole);
    }
//    //tipo operazione
//    if(listaTipoOperazione!=null) {
//      String idTrappole="";
//      for(Integer i : listaTipoTrappole) {
//        idTrappole+=i+",";
//      }
//      idTrappole=idTrappole.substring(0,idTrappole.length()-1);
//      List<TipoTrappolaDTO> listaTrappole=tipoTrappolaEJB.findByIdMultipli(idTrappole);
//      model.addAttribute("listaTrappole",listaTrappole);
//    }
//    
    // Tipo operazione
    if (trappolaggioRequest.getTipoOperazione() != null) {
        String ids="";
        for (Integer i : trappolaggioRequest.getTipoOperazione()) {
          ids+=i+",";
        }
        ids=ids.substring(0,ids.length()-1);
        listaTipoOperazione = trappoleEJB.findByIdMultipli(ids);
        model.addAttribute("listaTipoOperazione", listaTipoOperazione);
     }
    
    // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    model.addAttribute("dataFilter", dataFilter);

    model.addAttribute("trappolaggioRequest", trappolaggioRequest);
    session.setAttribute("trappolaggioRequest", trappolaggioRequest);
    model.addAttribute("from", "search");
    setBreadcrumbs(model, request);
    return "gestionetrappole/elencoTrappole";
  }

  @Lazy
  @RequestMapping(value = "/gestioneTrappole/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    @SuppressWarnings("unchecked")
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableTrappole") == null || "{}".equals(filtroInSessione.get("tableTrappole"))) {
        filtroInit = "";//"{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableTrappole");
      } 
       // filtroInit =
           // if(anagrafica.getCognome()!=null &&)
          //  + ",\"cognome\":{\"cnt\":[\""+dto.getCognome().toUpperCase()+"\"]}"
          //  filtroInit += "}";  
       // value='{"annoCampagna":{"cnt":"2016"},"descrizione":{"_values":["Liquidato"]}}'
      filtroInSessione.put("tableTrappole", filtroInit);
      return filtroInSessione;
  }
  
  @RequestMapping(value = "/gestioneTrappole/clearFilter")
  public String clearFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    session.removeAttribute("trappolaggioRequest");
    return "redirect:showFilter.do";
  }
  
  @RequestMapping(value = {"/gestioneTrappole/edit", "gestioneTrappole/show", "/gestioneTrappole/editTrappole", "/gestioneTrappole/showTrappole",
      "/gestioneTrappole/editTrappoleFromListaMissioni", "/gestioneTrappole/showTrappoleFromListaMissioni",
      "/gestioneTrappole/editTrappoleFromMissione", "/gestioneTrappole/showTrappoleFromMissione",
      "gestioneTrappole/editFromListaMissioni", "gestioneTrappole/showFromListaMissioni",
      "gestioneTrappole/editFromMissione", "gestioneTrappole/showFromMissione"})
  public String edit(Model model, @RequestParam(value = "idTrappolaggio") Integer idTrappolaggio, @RequestParam(value = "from") String from,
      HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    TrappolaggioRequest trappolaggioRequest = (TrappolaggioRequest) session.getAttribute("trappolaggioRequest");
    session.removeAttribute("trappolaggioRequest");
    session.setAttribute("trappolaggioRequest", trappolaggioRequest);
    
    TrappolaggioRequest trappolaggioFilter = new TrappolaggioRequest();
    trappolaggioFilter.setIdTrappolaggio(idTrappolaggio);
    
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    
    DataFilter dataFilter = getFiltroDati(utente);
    List<TrappolaggioDTO> list = trappoleEJB.findByFilterByTrappolaggioRequest(trappolaggioFilter, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());    
    TrappolaggioDTO dto = list.get(0);
    
    // comune
    String comune = null;
    if (dto != null) {
      if (StringUtils.isNotBlank(dto.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(dto.getIstatComune());
        if (comuneDTO != null)
        {
          comune = comuneDTO.getDescrizioneComune();
          dto.setDescrComune(comune);
          model.addAttribute("comune", comune);
        }
      }
    }

    TrappolaDTO trappola = trappoleEJB.findTrappolaById(dto.getIdTrappola());
    dto.setTrappola(trappola);
   
    RilevazioneDTO rilevazione = rilevazioneEJB.findById(dto.getIdRilevazione());
    TipoAreaDTO tipoArea = tipoAreaEJB.findById(rilevazione.getIdTipoArea());
    dto.setArea(tipoArea.getDescTipoArea());
    dto.setDettaglioArea(tipoArea.getDettaglioTipoArea());
    if (dto.getIdOrganismoNocivo() != null && dto.getIdOrganismoNocivo() > 0) {
      OrganismoNocivoDTO on = organismoNocivoEJB.findById(dto.getIdOrganismoNocivo());
      dto.setNomeLatino(on.getNomeLatino());
      dto.setSigla(on.getSigla());
    }
    model.addAttribute("trappolaggio", dto);
 
    List<FotoDTO> listaFoto = trappoleEJB.findListFotoByIdTrappolaggio(idTrappolaggio);
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

    List<AnagraficaDTO> ispettoriAggiunti = missioneEJB.getIspettoriAggiunti(dto.getIdMissione().longValue());
    model.addAttribute("ispettoriAggiunti", ispettoriAggiunti);

    List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidiMissione(dto.getIdMissione().intValue());
    if (listaIspettOperante == null)
      listaIspettOperante = new ArrayList<AnagraficaDTO>();
    if (!listaIspettOperante.contains(new AnagraficaDTO(dto.getIdAnagrafica()))) {
      AnagraficaDTO anag = anagraficaEJB.findById(dto.getIdAnagrafica());
      listaIspettOperante.add(anag);
    }
    model.addAttribute("listaIspettOperante", listaIspettOperante);

    List<SpecieVegetaleDTO> listaSpecieVegetali = specieVegetaleEJB.findValidi();
    if (!listaSpecieVegetali.contains(new SpecieVegetaleDTO(trappola.getIdSpecieVeg())) && trappola.getIdSpecieVeg() != null) {
      SpecieVegetaleDTO specieVeg = specieVegetaleEJB.findById(trappola.getIdSpecieVeg());
      listaSpecieVegetali.add(specieVeg);
    }
    model.addAttribute("listaSpecieVegetali", listaSpecieVegetali);
    
    List<TipoAreaDTO> tipoAree = tipoAreaEJB.findValidi();
    model.addAttribute("tipoAree", tipoAree);

    
    List<TipoTrappolaDTO> tipoTrappole =tipoTrappolaEJB.findValidi();
    model.addAttribute("tipoTrappole", tipoTrappole);
    
    List<TrappolaggioDTO> listaStoricoTrappola = trappoleEJB.findStoriaTrappolaByCodice(dto.getTrappola().getCodiceSfr());
    model.addAttribute("listaStoricoTrappola", listaStoricoTrappola);
    //ricerco l'installazione 
   /* for(TrappolaggioDTO obj: listaStoricoTrappola) {
      if(obj.getIdOperazione()!=null &&  obj.getIdOperazione().equals(1)) {
        dto.setIdOrganismoNocivo(obj.getIdOrganismoNocivo());
      }
         }*/
    
    List<OrganismoNocivoDTO> listOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listOn", listOn);
    
   // model.addAttribute("editabile", editabile);

    setBreadcrumbs(model, request);
    showFlashMessages(model, request);
    //UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
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
    boolean editSpecieON = false;
    int idStatoVerbale = missioneEJB.getIdStatoVerbaleByIdMissione(dto.getIdMissione());
    if (idStatoVerbale < 2 && request.getServletPath().indexOf("/edit") > -1) {   // lo stato verbale (se presente deve essere max 1 (bozza) per consentire modifiche
      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_TRAPPOLE") && utente.isReadWrite();
      editSpecieON = ruolo.getAmministratore() || ruolo.getFunzionarioBO();
    }
    model.addAttribute("editabile", editabile);
    model.addAttribute("editSpecieON", editSpecieON);


//    if (request.getServletPath().indexOf("/edit.do") > -1) {
//      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_TRAPPOLE") && utente.isReadWrite();
//    }
//    model.addAttribute("editabile", editabile);    
    return "gestionetrappole/dettaglioTrappolaggio";
  }

  @RequestMapping(value = "/gestioneTrappole/delete")
  public String canDelete(Model model, @RequestParam(value = "id") Long id, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableTrappole");
      return "gestionetabelle/confermaElimina";
  }

  @RequestMapping(value = "/gestioneTrappole/remove")
  public RedirectView remove(Model model, @RequestParam(value = "id") String id, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    try {
      trappoleEJB.remove(Integer.decode(id));
    }
    catch (InternalUnexpectedException e)
    {
      logger.error("Errore nella cancellazione del trappolaggio " + id + ": " + e.getMessage());
      //boolean isFound = e.getCause().getMessage().indexOf("ORA-02292") != -1? true: false;
    }
    return new RedirectView("search.do", true);
  }

  @RequestMapping(value = {"/gestioneTrappole/pSave", "/gestioneTrappole/pSaveTrappoleFromListaMissioni", "/gestioneTrappole/pSaveTrappoleFromMissione", "/gestioneTrappole/pSaveFromListaMissioni", "/gestioneTrappole/pSaveFromMissione"})
  public String pSave(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    TrappolaggioDTO dto = (TrappolaggioDTO) model.asMap().get("trappolaggio");
    
    TrappolaggioRequest trappolaggioFilter = new TrappolaggioRequest();
    trappolaggioFilter.setIdTrappolaggio(dto.getIdTrappolaggio());
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    
    DataFilter dataFilter = getFiltroDati(utente);
    List<TrappolaggioDTO> list = trappoleEJB.findByFilterByTrappolaggioRequest(trappolaggioFilter, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());    
    TrappolaggioDTO dtoDb = list.get(0);
    
    if (dto.getIdAnagrafica()==null || dto.getIdAnagrafica() == 0) {
      dtoDb.setIdAnagrafica(dto.getIdAnagrafica());
    }
    if (dto.getIdSpecieVegetale()==null || dto.getIdSpecieVegetale() == 0) {
      dtoDb.setIdSpecieVegetale(dto.getIdSpecieVegetale());
    }
    if (dto.getIdTipoArea()==null || dto.getIdTipoArea() == 0) {
      dtoDb.setIdTipoArea(dto.getIdTipoArea());
    }
    
    if (dto.getTrappola()!=null && (dto.getTrappola().getLatitudine()==null || dto.getTrappola().getLatitudine() == 0)) {
      dtoDb.getTrappola().setLatitudine(dto.getTrappola().getLatitudine());
    }     
    
    if (dto.getTrappola()!=null && (dto.getTrappola().getLongitudine()==null || dto.getTrappola().getLongitudine() == 0)) {
      dtoDb.getTrappola().setLongitudine(dto.getTrappola().getLongitudine());
    }      

    if (dto.getIdOrganismoNocivo()==null || dto.getIdOrganismoNocivo() == 0) {
      dtoDb.setIdOrganismoNocivo(dto.getIdOrganismoNocivo());
    }       

    if (dto.getIdTrappola()==null || dto.getIdTrappola() == 0) {
      dtoDb.setIdTrappola(dto.getIdTrappola());
    } 
    //lo riassegno
    dto=dtoDb;
    
    // comune
    String comune = null;
    if (dto != null) {
      if (StringUtils.isNotBlank(dto.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(dto.getIstatComune());
        if (comuneDTO != null)
        {
          comune = comuneDTO.getDescrizioneComune();
          dto.setDescrComune(comune);
          model.addAttribute("comune", comune);
        }
      }
    }

    TrappolaDTO trappola = trappoleEJB.findTrappolaById(dto.getIdTrappola());
    dto.setTrappola(trappola);
   
    model.addAttribute("trappolaggio", dto);
 
    List<FotoDTO> listaFoto = trappoleEJB.findListFotoByIdTrappolaggio(dto.getIdTrappolaggio());
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

    List<AnagraficaDTO> ispettoriAggiunti = missioneEJB.getIspettoriAggiunti(dto.getIdMissione().longValue());
    model.addAttribute("ispettoriAggiunti", ispettoriAggiunti);

    List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidiMissione(dto.getIdMissione().intValue());
    //List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidi();
    model.addAttribute("listaIspettOperante", listaIspettOperante);

    List<SpecieVegetaleDTO> listaSpecieVegetali = specieVegetaleEJB.findValidi();
    model.addAttribute("listaSpecieVegetali", listaSpecieVegetali);
    
    List<TipoAreaDTO> tipoAree = tipoAreaEJB.findValidi();
    model.addAttribute("tipoAree", tipoAree);

    List<TrappolaggioDTO> listaStoricoTrappola = trappoleEJB.findStoriaTrappolaByCodice(dto.getCodiceSfr());
    model.addAttribute("listaStoricoTrappola", listaStoricoTrappola);
        
    List<OrganismoNocivoDTO> listOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listOn", listOn);

    List<TipoTrappolaDTO> tipoTrappole =tipoTrappolaEJB.findValidi();
    model.addAttribute("tipoTrappole", tipoTrappole);
    
    if(dto.getTrappola()==null) {
      TrappolaDTO trapDto = trappoleEJB.findTrappolaByCodiceSfr(dto.getCodiceSfr());
      trapDto.setIdTipoTrappola(dto.getIdTrappola());
      //trapDto.setCodiceSfr(dto.getCodiceSfr());
      dto.setTrappola(trapDto);
    }
    model.addAttribute("trappolaggio", dto);

    setBreadcrumbs(model, request);
    showFlashMessages(model, request);
    //UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
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
    boolean editSpecieON = false;
    int idStatoVerbale = missioneEJB.getIdStatoVerbaleByIdMissione(dto.getIdMissione());
    if (idStatoVerbale < 2 && request.getServletPath().indexOf("/pSave") > -1) {   // lo stato verbale (se presente deve essere max 1 (bozza) per consentire modifiche
      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_TRAPPOLE") && utente.isReadWrite();
      editSpecieON = ruolo.getAmministratore() || ruolo.getFunzionarioBO();
    }
    model.addAttribute("editabile", editabile);
    model.addAttribute("editSpecieON", editSpecieON);

    return "gestionetrappole/dettaglioTrappolaggio";
  }

  @RequestMapping(value = "/gestioneTrappole/getTrappoleJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getTrappoleJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    TrappolaggioRequest trappolaggioRequest = (TrappolaggioRequest) session.getAttribute("trappolaggioRequest");
    
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    
    DataFilter dataFilter = getFiltroDati(utente);
    
    List<TrappolaggioDTO> lista = trappoleEJB.findByFilterByTrappolaggioRequest(trappolaggioRequest, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());

    if (lista == null) {
      lista = new ArrayList<>();
    }

    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }
  
  private void loadPopupCombo(Model model, HttpSession session) throws InternalUnexpectedException
  {
    TrappolaggioRequest trappolaggioRequest = (TrappolaggioRequest) session.getAttribute("trappolaggioRequest");
    
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
    // Tipo trappola
    List<TipoTrappolaDTO> all_tipoTrappola = null;
    if (session.getAttribute("checkboxAllTipoTrappola") != null && ((String) session.getAttribute("checkboxAllTipoTrappola")).equals("true")) {
      all_tipoTrappola = tipoTrappolaEJB.findAll();
    } else {
      all_tipoTrappola = tipoTrappolaEJB.findValidi();
    }
    model.addAttribute("all_tipoTrappola", all_tipoTrappola);
 // Tipo operazione
    List<OperazioneTrappolaDTO> all_tipoOperazione = null;
    //if (session.getAttribute("checkboxAllTipoOperazione") != null && ((String) session.getAttribute("checkboxAllTipoOperazione")).equals("true")) {
    all_tipoOperazione = trappoleEJB.findTipoOperazione();
    //} 
    model.addAttribute("all_tipoOperazione", all_tipoOperazione);

    List<TipoAreaDTO> tipoAree = new ArrayList<>();
    model.addAttribute("tipoAree", tipoAree);
    
    if (trappolaggioRequest != null) {
      // Tipo aree
      if (trappolaggioRequest.getTipoArea() != null) {
        String idArea="";
        for(Integer i : trappolaggioRequest.getTipoArea()) {
          idArea+=i+",";
        }
        idArea=idArea.substring(0,idArea.length()-1);
        List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
        model.addAttribute("tipoAree", listaAree);   
      }
      // Specie vegetali
      if (trappolaggioRequest.getSpecieVegetale() != null) {
        String idSpecie="";
        for (Integer i : trappolaggioRequest.getSpecieVegetale()) {
          idSpecie+=i+",";
        }
        idSpecie=idSpecie.substring(0,idSpecie.length()-1);
        List<SpecieVegetaleDTO> listaSpecieVeg = specieVegetaleEJB.findByIdMultipli(idSpecie);
        model.addAttribute("listaSpecieVegetali", listaSpecieVeg);     
      }
      // Organismi nocivi
      if (trappolaggioRequest.getOrganismoNocivo() != null) {
          String idOn="";
          for (Integer i : trappolaggioRequest.getOrganismoNocivo()) {
            idOn+=i+",";
          }
          idOn=idOn.substring(0,idOn.length()-1);
          List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findByIdMultipli(idOn);
          model.addAttribute("listaON", listaOn);
       }
      // Tipo trappole
      if (trappolaggioRequest.getTipoTrappola() != null) {
          String ids="";
          for (Integer i : trappolaggioRequest.getTipoTrappola()) {
            ids+=i+",";
          }
          ids=ids.substring(0,ids.length()-1);
          List<TipoTrappolaDTO> listaTipoTrappole = tipoTrappolaEJB.findByIdMultipli(ids);
          model.addAttribute("listaTipoTrappole", listaTipoTrappole);
       }
      // ispettori assegnati
      if (trappolaggioRequest.getIspettoreAssegnato() != null && trappolaggioRequest.getIspettoreAssegnato().size() > 0)
      {
          String idIsSec="";
          for(Integer i : trappolaggioRequest.getIspettoreAssegnato()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettori",listaIspettAssegnati);          
      }
      // ispettori secondari
      if (trappolaggioRequest.getIspettoriSecondari() != null && trappolaggioRequest.getIspettoriSecondari().size() > 0)
      {
          String idIsSec="";
          for(Integer i : trappolaggioRequest.getIspettoriSecondari()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettSecondari=anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettSecondari",listaIspettSecondari);          
      }
      // comune
      if (StringUtils.isNotBlank(trappolaggioRequest.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(trappolaggioRequest.getIstatComune());
        if (comuneDTO != null)
        {
          model.addAttribute("provSceltaComune", comuneDTO.getSiglaProvincia());
          model.addAttribute("comuneSceltaComune", comuneDTO.getDescrizioneComune());
        }
      }
      // Tipo operazione
      if (trappolaggioRequest.getTipoOperazione() != null) {
          String ids="";
          for (Integer i : trappolaggioRequest.getTipoOperazione()) {
            ids+=i+",";
          }
          ids=ids.substring(0,ids.length()-1);
          List<OperazioneTrappolaDTO> listaTipoOperazione = trappoleEJB.findByIdMultipli(ids);
          model.addAttribute("listaTipoOperazione", listaTipoOperazione);
       }
    }
  }
  
  @RequestMapping(value = "/gestioneTrappole/searchComuni", produces = "application/json", method = RequestMethod.POST)
  public Map<String, Object> searchComuni(HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HashMap<String, Object> values = new HashMap<String, Object>();
    String prov = request.getParameter("provSceltaComune");
    String comune = request.getParameter("descrComune");
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

  @RequestMapping(value = "/rest/trappola", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
  public @ResponseBody ResponseEntity<?> creaTrappola(@Valid @RequestBody Trappolaggio body, HttpServletRequest request) throws MalformedURLException, IOException
  {   
    // api per installazione/controllo/ricarica/rimozione nuova trappola
    TrappolaggioDTO trappolaggio = new TrappolaggioDTO();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    try {
      String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      String cf = TokenUtils.verifyToken(jwt);
      AnagraficaDTO anagrafica = new AnagraficaDTO();
      anagrafica.setCfAnagraficaEst(cf);
      anagrafica.setActive(true);         // deve prendere l'anagrafica non storicizzata
      List<AnagraficaDTO> ispettori = anagraficaEJB.findByFilter(anagrafica);
      if (ispettori == null || ispettori.size() == 0) {
        logger.debug("Errore Token: codice fiscale ispettore non trovato (creaTrappola)");
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", "Errore Token: codice fiscale ispettore non trovato (creaTrappola)");
        err.setMessage("Errore Token: codice fiscale ispettore non trovato (creaTrappola)");
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
      }

      AnagraficaDTO ispettore = ispettori.get(0);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(body);
      logger.debug(json);

      TrappolaDTO trappola = new TrappolaDTO();
      boolean nuovaTrappola = true;

      try {
        PapuaservRESTClient client = PapuaservProfilazioneServiceFactory.getRestServiceClient();
        UtenteAnagraficaVO[] utenti = client.findUtentiEntiByCodiceFiscaleList(new String[] { cf }, null, "");
        trappola.setExtIdUtenteAggiornamento(utenti[0].getIdUtente());
        trappolaggio.setExtIdUtenteAggiornamento(utenti[0].getIdUtente());
      } catch (Exception e) {
          trappola.setExtIdUtenteAggiornamento(0L);
          trappolaggio.setExtIdUtenteAggiornamento(0L);
      }

      if (body.getTrappola().getIdTrappola() != null && body.getTrappola().getIdTrappola().intValue() > 0) {
        trappola = trappoleEJB.findTrappolaById(body.getTrappola().getIdTrappola());
        nuovaTrappola = false;
      }
      // Popolamento oggetto trappola
      trappola.setCodiceSfr(body.getTrappola().getCodiceSfr());
      trappola.setDataInstallazione(sdf.parse(body.getTrappola().getDataInstallazione()));
      trappola.setDataRimozione((body.getTrappola().getDataRimozione()!=null)?sdf.parse(body.getTrappola().getDataRimozione()):null);
      trappola.setIdSpecieVeg(body.getTrappola().getIdSpecieVeg());
      trappola.setIdTipoTrappola(body.getTrappola().getIdTipoTrappola());
      trappola.setLatitudine(body.getTrappola().getLatitudine());
      trappola.setLongitudine(body.getTrappola().getLongitudine());
      
      TrappolaDTO newTrappola = null;
      
      if (nuovaTrappola) { 
        newTrappola = trappoleEJB.insertTrappola(trappola);
      } else {
        trappoleEJB.updateTrappola(trappola);
        newTrappola = trappola;
      }
      trappolaggio.setDataTrappolaggio(sdf.parse(body.getDataTrappolaggio()));
//      trappolaggio.setDataOraInizio(sdf.parse(body.getDataTrappolaggio()));
//      trappolaggio.setDataTrappolaggio(trappolaggio.getDataOraFine());
//      trappolaggio.setDataOraFine(sdf.parse(body.getDataTrappolaggio()));
      
      String dataOraInizioStr = null;
      if (StringUtils.isNotBlank(body.getDataOraInizio()))
    	  dataOraInizioStr = body.getDataTrappolaggio().replaceAll("-", "/") + " " + body.getDataOraInizio() + ":00";
      else
    	  dataOraInizioStr = body.getDataTrappolaggio().replaceAll("-", "/") + " " + "00:00:00";
      Date dataOraInizio = dtf.parse(dataOraInizioStr);
      trappolaggio.setDataOraInizio(dataOraInizio);
      Date dataOraFine = null;
      if (StringUtils.isNotBlank(body.getDataOraFine())) {
        String dataOraFineStr = body.getDataTrappolaggio().replaceAll("-", "/") + " " + body.getDataOraFine() + ":00";
        dataOraFine = dtf.parse(dataOraFineStr);
        trappolaggio.setDataOraFine(dataOraFine);
      }
      
      trappolaggio.setIdRilevazione(body.getIdRilevazione());
      trappolaggio.setTrappola(newTrappola);
      trappolaggio.setIdIspezioneVisiva(body.getIdIspezioneVisiva());
      trappolaggio.setIdOperazione(body.getIdOperazione()); // 1=Installazione; 2=Manutenzione/Controllo; 3=Ricarica; 4=Rimozione
      trappolaggio.setIstatComune(body.getIstatComune());
      trappolaggio.setNote(body.getNote());
      trappolaggio.setIdAnagrafica(ispettore.getIdAnagrafica());
      //trappolaggio.setIdAnagrafica(13);
      trappolaggio.setIdOrganismoNocivo(body.getIdOrganismoNocivo());
      
      if (body.getIdTrappolaggio() != null && body.getIdTrappolaggio().intValue() > 0) {
        // update
        trappolaggio.setIdTrappolaggio(body.getIdTrappolaggio());
        trappoleEJB.update(trappolaggio);
        trappolaggio.setOraInizio(body.getDataOraInizio());
        if(dataOraFine != null) 
          trappolaggio.setDataOraFine(dataOraFine);
      } else {
          trappolaggio = trappoleEJB.insert(trappolaggio);
          trappolaggio.setOraInizio(body.getDataOraInizio());
          if(dataOraFine != null) 
            trappolaggio.setDataOraFine(dataOraFine);
      }
      RilevazioneDTO rilevazione = rilevazioneEJB.findById(trappolaggio.getIdRilevazione());
      trappolaggio.setIdMissione(rilevazione.getIdMissione().longValue());
    }
    catch(Throwable e) {
      if (e.getCause() instanceof DuplicateKeyException) {
        return new ResponseEntity<String>("{ \"codErrore\" : 1 }", HttpStatus.CONFLICT);
      }
      logger.debug("Errore nel metodo creaTrappola durante la registrazione: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo creaTrappola durante la registrazione: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<TrappolaggioDTO>(trappolaggio, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/rest/trappolaggio/{idTrappolaggio}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<?> deleteTrappolaggio(@PathVariable(value = "idTrappolaggio") Integer idTrappolaggio, HttpServletRequest request)
  {
    try {
      //String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      //verifyToken(jwt);
      logger.debug("id_trappolaggio: " + idTrappolaggio);
      TrappolaggioDTO trappolaggio = trappoleEJB.findById(idTrappolaggio);
      if (trappolaggio == null) {
        logger.debug("Trappolaggio con id " + idTrappolaggio + " non trovato");
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", "Trappolaggio con id " + idTrappolaggio + " non trovato");
        er.setMessage("Trappolaggio con id " + idTrappolaggio + " non trovato");
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.NOT_FOUND);
      }
      trappoleEJB.remove(idTrappolaggio);
      TrappolaggioDTO trappolaggioFilter = new TrappolaggioDTO();
      trappolaggioFilter.setTrappola(new TrappolaDTO(trappolaggio.getIdTrappola()));
      List<TrappolaggioDTO> list = trappoleEJB.findByFilter(trappolaggioFilter);
      // se non ci sono altri trappolaggi per quella trappola elimino anche la trappola
      if (list == null || list.size() == 0) {
        trappoleEJB.eliminaTrappola(trappolaggio.getIdTrappola());
      }
    } catch (Exception e) {
        logger.debug("Errore nel metodo deleteTrappolaggio: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella cancellazione del trappolaggio: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Map<String, String> ok = new HashMap<String, String>();
    ok.put("status", "ok");
    return new ResponseEntity<java.util.Map<String,String>>(ok,HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/trappole/{latitudine}/{longitudine}/{raggio}/", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<?> getTrappoleByCoordinates(@PathVariable(value = "latitudine") Double latitudine, @PathVariable(value = "longitudine") Double longitudine,
            @PathVariable(value = "raggio") Integer raggio) throws InternalUnexpectedException
  {
    List<Trappola> lista = null;
    try {
      lista = trappoleEJB.findTrappolaByCoordinates(latitudine, longitudine, raggio);
      if (lista == null) {
        lista = new ArrayList<Trappola>();
      }
    }
    catch (Exception e) {
      logger.debug("Errore nel metodo getTrappoleByCoordinates: " + e.getMessage());
      ErrorResponse er = new ErrorResponse();
      er.addError("Errore", e.getMessage());
      er.setMessage("Errore interno nel metodo getTrappoleByCoordinates: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<Trappola>>(lista,HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/trappola/{codTrappola}", method = RequestMethod.GET)
  public ResponseEntity<?> getTrappolaByCodice(@PathVariable(value = "codTrappola") String codTrappola) throws InternalUnexpectedException
  {
    Trappola trappola = null;
    try {
      
      trappola = trappoleEJB.findTrappolaByCodice(codTrappola);
      
      if (trappola == null) {
        logger.debug("getTrappolaByCodice: trappola con codice: " + codTrappola + " non trovata");
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", "Trappola con codice: " + codTrappola + " non trovata");
        er.setMessage("Trappola con codice: " + codTrappola + " non trovata");
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.NOT_FOUND);
      }
    }
    catch (Exception e) {
      logger.debug("Errore nel metodo getTrappolaByCodice: " + e.getMessage());
      ErrorResponse er = new ErrorResponse();
      er.addError("Errore", e.getMessage());
      er.setMessage("Errore interno nel metodo getTrappolaByCodice: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Trappola>(trappola,HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/trappolaggi/{idTrappola}", method = RequestMethod.GET)
  public ResponseEntity<?> getTrappolaggiByIdTrappola(@PathVariable(value = "idTrappola") String idTrappola) throws InternalUnexpectedException
  {
    List<TrappolaggioDTO> list = null;
    try {
      TrappolaggioDTO trappolaggioFilter = new TrappolaggioDTO();
      trappolaggioFilter.setTrappola(new TrappolaDTO(Integer.parseInt(idTrappola)));
      list = trappoleEJB.findByFilter(trappolaggioFilter);
    }
    catch (Exception e) {
      logger.debug("Errore nel metodo getTrappolaByCodice: " + e.getMessage());
      ErrorResponse er = new ErrorResponse();
      er.addError("Errore", e.getMessage());
      er.setMessage("Errore interno nel metodo getTrappolaggiByIdTrappola: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<TrappolaggioDTO>>(list,HttpStatus.OK);
  }

  @RequestMapping(value = {"/gestioneTrappole/searchTrappole", "/gestioneTrappole/searchTrappoleFromListaMissioni", "/gestioneTrappole/searchTrappoleFromMissione"})
  public String searchTrappole(Model model, @RequestParam(value = "idIspezioneVisiva", required = false) Integer idIspezioneVisiva, @RequestParam(value = "editabile", required = false) Boolean editabile, HttpSession session, HttpServletResponse response,HttpServletRequest request) throws InternalUnexpectedException
  {
    Integer idIspezione = idIspezioneVisiva;
    if (idIspezione == null)
      idIspezione = (Integer) session.getAttribute("idIspezioneVisiva");
    else
      session.setAttribute("idIspezioneVisiva", idIspezioneVisiva); 

    TrappolaggioRequest tr = new TrappolaggioRequest();
    tr.setIdIspezioneVisiva(idIspezione);
    session.setAttribute("trappolaggioRequest", tr);
    
    setBreadcrumbs(model, request);
    showFlashMessages(model, request);
    
    UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    model.addAttribute("dataFilter", dataFilter);

    return "gestionetrappole/elencoTrappole";
  }
  
  @RequestMapping(value = "/gestioneTrappole/getTrappoleVisualJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getTrappoleVisualJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    Integer idIspezioneVisiva = (Integer) session.getAttribute("idIspezioneVisiva");
    List<TrappolaggioDTO> lista = trappoleEJB.findByIdIspezione(idIspezioneVisiva);

    if (lista == null) {
      lista = new ArrayList<>();
    }

    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }
  
  @RequestMapping(value = {"/gestioneTrappole/save", "/gestioneTrappole/saveTrappole", "/gestioneTrappole/saveFromListaMissioni", "/gestioneTrappole/saveFromMissione",
      "/gestioneTrappole/saveTrappoleFromListaMissioni", "/gestioneTrappole/saveTrappoleFromMissione"})
  public String save(Model model, @ModelAttribute("trappolaggio") TrappolaggioDTO trappolaggio, 
      @RequestParam(value = "idTrappolaggio") Integer idTrappolaggio,HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    //DataFilter data = getFiltroDati(utente);
    RuoloDTO ruolo = getRuolo(utente);
    model.addAttribute("ruolo", ruolo);
    
    Errors errors = new Errors();

    if(ruolo!= null && (ruolo.getAmministratore()!=null && ruolo.getAmministratore().equals(true)) 
                  || (ruolo.getFunzionarioBO()!=null && ruolo.getFunzionarioBO().equals(true)) ) {
      
      if (trappolaggio.getIdSpecieVegetale()==null || trappolaggio.getIdSpecieVegetale() == 0) {
        errors.addError("idSpecieVegetale", "Campo Obbligatorio. Indicare una specie vegetale");
      }

      if (trappolaggio.getIdTipoArea()==null || trappolaggio.getIdTipoArea() == 0) {
        errors.addError("idTipoArea", "Campo Obbligatorio. Indicare una tipologia area");
      }
      
      if (trappolaggio.getIdAnagrafica()==null || trappolaggio.getIdAnagrafica() == 0) {
        errors.addError("idAnagrafica", "Campo Obbligatorio. Indicare un ispettore operante");
      } 
      
      if (trappolaggio.getTrappola()!=null && (trappolaggio.getTrappola().getLatitudine()==null || trappolaggio.getTrappola().getLatitudine() == 0)) {
        errors.addError("latitudine", "Campo Obbligatorio. Indicare una latitudine valida");
      }     
      
      if (trappolaggio.getTrappola()!=null && (trappolaggio.getTrappola().getLongitudine()==null || trappolaggio.getTrappola().getLongitudine() == 0)) {
        errors.addError("longitudine", "Campo Obbligatorio. Indicare una longitudine valida");
      }      
      
      if (trappolaggio.getIdTrappola()==null || trappolaggio.getIdTrappola() == 0) {
        errors.addError("idTrappola", "Campo Obbligatorio. Indicare una trappola");
      }       
    }

    String prefix = "";
    
    if (request.getServletPath().indexOf("saveTrappole") > -1)
      prefix = "Trappole";
    if (request.getServletPath().indexOf("FromListaMissioni") > -1)
      prefix += "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione") > -1)
      prefix += "FromMissione";

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("trappolaggio", trappolaggio);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      return "redirect:pSave"+prefix+".do";
    }
    else
    {
        try {
          
          if(ruolo!= null && ruolo.getFunzionarioLaboratorio()!=null && ruolo.getFunzionarioLaboratorio().equals(false)) {
           
                //aggiorno la rilevazione
                  RilevazioneDTO rilevazione = new RilevazioneDTO();
                  rilevazione.setIdRilevazione(trappolaggio.getIdRilevazione());
                  rilevazione.setIdTipoArea(trappolaggio.getIdTipoArea());
                  rilevazioneEJB.updateFilter(rilevazione);
          
                  //modifico il trappolaggio
                  TrappolaggioDTO dto = trappoleEJB.findById(idTrappolaggio);                                       
                  dto.setIdAnagrafica(trappolaggio.getIdAnagrafica());
                  dto.setNote(trappolaggio.getNote());             
                  dto.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
                  dto.setIdOrganismoNocivo((trappolaggio.getIdOrganismoNocivo()==null || trappolaggio.getIdOrganismoNocivo().intValue()==0)?null:trappolaggio.getIdOrganismoNocivo());
                  trappoleEJB.update(dto);
                  redirectAttributes.addFlashAttribute("trappolaggio", dto);

                  //aggiorno la trappola
                  List<TrappolaggioDTO> listaStoricoTrappola = trappoleEJB.findStoriaTrappolaByCodice(trappolaggio.getCodiceTrappola());
                  model.addAttribute("listaStoricoTrappola", listaStoricoTrappola);
                  //ricerco l'installazione 
                  TrappolaDTO trapDto=null;
                  for(TrappolaggioDTO obj: listaStoricoTrappola) {
                    if(obj.getIdOperazione()!=null &&  obj.getIdOperazione().equals(1)) {
                      trapDto =trappoleEJB.findTrappolaByCodiceSfr(obj.getCodiceSfr());
                    }
                  }
                  //la modifico
                  trapDto.setIdTipoTrappola(trappolaggio.getIdTrappola());
                  trapDto.setLatitudine(trappolaggio.getLatitudine());
                  trapDto.setLongitudine(trappolaggio.getLongitudine());
                  trapDto.setIdSpecieVeg(trappolaggio.getIdSpecieVegetale());
                  trappoleEJB.updateTrappola(trapDto);
          }
          Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
          session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
        }
        catch (Exception e) {
          logger.error("Errore nella registrazione: " + e.getMessage());
          model.addAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("success", null);
          redirectAttributes.addFlashAttribute("model", model);
        }
    }
    return "redirect:search"+prefix+".do";
  }

  @RequestMapping(value = "/gestioneTrappole/foto")
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
  
  @Lazy
  @RequestMapping(value = "/gestioneTrappole/gestioneTrappoleExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    TrappolaggioRequest trappolaggioRequest = (TrappolaggioRequest) session.getAttribute("trappolaggioRequest");
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    
    DataFilter dataFilter = getFiltroDati(utente);
    List<TrappolaggioDTO> elenco = trappoleEJB.findByFilterByTrappolaggioRequest(trappolaggioRequest, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"trappole.xls\"");
    return new ModelAndView("excelGestioneTrappoleView", "elenco", elenco);
  }
  
  @RequestMapping(value = "/gestioneTrappole/getTipoOperazioneJson", produces = "application/json")
  @ResponseBody
  public String getTipoTrappoleJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<OperazioneTrappolaDTO> lista = trappoleEJB.findTipoOperazione();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }

}
