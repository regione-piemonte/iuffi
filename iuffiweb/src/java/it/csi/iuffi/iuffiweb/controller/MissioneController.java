package it.csi.iuffi.iuffiweb.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneCampioneEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneTrappolaEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneVisualEJB;
import it.csi.iuffi.iuffiweb.business.IGpsFotoEJB;
import it.csi.iuffi.iuffiweb.business.IIspezioneVisivaPiantaEJB;
import it.csi.iuffi.iuffiweb.business.IMissioneEJB;
import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.business.IRilevazioneEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.business.ITipoAreaEJB;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.CampionamentoDTO;
import it.csi.iuffi.iuffiweb.model.CampionamentoSpecOnDTO;
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaPiantaDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RilevazioneDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.StatoEnum;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaggioDTO;
import it.csi.iuffi.iuffiweb.model.api.Missione;
import it.csi.iuffi.iuffiweb.model.api.OrganismiNocivi;
import it.csi.iuffi.iuffiweb.model.api.Rilevazione;
import it.csi.iuffi.iuffiweb.model.request.CampionamentoRequest;
import it.csi.iuffi.iuffiweb.model.request.MissioneRequest;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.TokenUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.smraviv.avivws.dto.external.iuffi.Attivita;
import it.csi.smraviv.avivws.dto.external.iuffi.Azienda;
import it.csi.smraviv.avivws.dto.external.iuffi.Pdf;
import it.csi.smraviv.avivws.dto.external.iuffi.Risposta;
import it.csi.smraviv.avivws.presentation.client.rest.iuffi.AvivwsRESTClient;
import it.csi.smraviv.avivws.presentation.client.rest.iuffi.AvivwsServiceFactory;

@Controller
@IuffiSecurity(value = "VISUALIZZA_MISSIONI", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class MissioneController extends TabelleController
{

  @Autowired
  private IMissioneEJB missioneEJB;

  @Autowired
  private IAnagraficaEJB anagraficaEJB;

  @Autowired
  private ISpecieVegetaleEJB specieVegetaleEJB;
  
  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;
  
  @Autowired
  private IRilevazioneEJB rilevazioneEJB;

  @Autowired
  private IGestioneVisualEJB gestioneVisualEJB;

  @Autowired
  private IGestioneCampioneEJB gestioneCampioneEJB;

  @Autowired
  private IGestioneTrappolaEJB gestioneTrappolaEJB;

  @Autowired
  private ITipoAreaEJB tipoAreaEJB;
  
  @Autowired
  protected SmartValidator validator;

  @Autowired
  private IRicercaEJB ricercaEJB;

  @Autowired
  private IQuadroEJB quadroEJB;

  @Autowired
  private IIspezioneVisivaPiantaEJB ispezioneVisivaPiantaEJB;
  
  @Autowired  
  private IGpsFotoEJB gpsFotoEJB;
  
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  private static final SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }


  @RequestMapping(value = "/missione/save", method = RequestMethod.POST)
  public String save(Model model, @ModelAttribute("missione") MissioneDTO missioneDTO, HttpSession session, HttpServletRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws InternalUnexpectedException, ParseException
  {
    MissioneDTO missione = missioneDTO;
    Errors errors = new Errors();
    String dataOraInizioStr = null;
    String dataOraFineStr = null;
    Date dataOraInizio = null;
    Date dataOraFine = null;
    
    if (missione.getDataOraInizioMissione() == null) {
      errors.addError("dataOraInizioMissione", "Campo Obbligatorio");
    }
    if (StringUtils.isBlank(missione.getOraInizio())) {
      errors.addError("oraInizio", "Campo Obbligatorio");
    }
    if (missione.getIdIspettoreAssegnato() == null) {
      errors.addError("idIspettoreAssegnato", "Campo Obbligatorio");
    }
    if (StringUtils.isNotBlank(missione.getNomeFile())) {
      String regexp = "^[A-Za-z0-9\\.\\-_]+$";
      if (!missione.getNomeFile().toUpperCase().endsWith(".PDF") || !missione.getNomeFile().matches(regexp) || missione.getNomeFile().length() > 80) {
        errors.addError("nomeFile", "Nome file non valido. Sono ammessi solo i seguenti: lettere, numeri, .  -  _  (max 80 car.)");
      }
    }
    try {
      dataOraInizioStr = sdf.format(missione.getDataOraInizioMissione()) + " " + ((StringUtils.isNotBlank(missioneDTO.getOraInizio()))?missioneDTO.getOraInizio():"00:00") + ":00";
      dataOraInizio = dtf.parse(dataOraInizioStr);
      missione.setDataOraInizioMissione(dataOraInizio);
    } catch (Exception e) {
        errors.addError("oraInizio", "Valore non valido");
        logger.error(e.getMessage());
    }
    try {
      dataOraFineStr = (missione.getOraFine()!=null)?sdf.format(missione.getDataOraInizioMissione()) + " " + missioneDTO.getOraFine() + ":00":null;
      dataOraFine = dtf.parse(dataOraFineStr);
      missione.setDataOraFineMissione(dataOraFine);
    } catch (Exception e) {
        logger.error(e.getMessage());
    }
    if (dataOraInizio != null && dataOraFine != null && dataOraInizio.after(dataOraFine)) {
      errors.addError("oraInizio", "Ora inizio missione successiva a Ora fine");
    }

    List<AnagraficaDTO> ispettoriAggiunti = new ArrayList<AnagraficaDTO>();
    AnagraficaDTO ispettoreAssegnato = null;
    
    if (missione.getIdIspettoreAssegnato() != null) {
      List<MissioneDTO> missioni = missioneEJB.findByDateAndIspettore(dataOraInizioStr, missione.getIdIspettoreAssegnato().intValue(), missione.getIdMissione());
      if (missioni != null && missioni.size() > 0) {
        errors.addError("idIspettoreAssegnato", "Ispettore assegnato già inserito in un altra missione");
        logger.error("Ispettore assegnato già inserito in un altra missione");
      }
      ispettoreAssegnato = anagraficaEJB.findById(missione.getIdIspettoreAssegnato().intValue());
    }
    
    if (missione.getIspettoriSelezionati() != null && missione.getIspettoriSelezionati().size() > 0)
    {
      for (Integer i : missione.getIspettoriSelezionati())
      {
        AnagraficaDTO ispettoreAggiunto = anagraficaEJB.findById(i);
        if (ispettoreAssegnato != null && i.intValue() == ispettoreAssegnato.getIdAnagrafica().intValue()) {
          logger.error("Ispettore aggiunto uguale ad ispettore assegnato");
          errors.addError("ispettoriSelezionati", "Ispettore aggiunto uguale ad ispettore assegnato");
          //break;
        }
        List<MissioneDTO> list = missioneEJB.findByDateAndIspettore(dataOraInizioStr, ispettoreAggiunto.getIdAnagrafica(), missione.getIdMissione());
        String msg = "Ispettore secondario " + ispettoreAggiunto.getCognomeNome() + " già inserito in un altra missione per la data/ora indicata";
        if (list != null && list.size() > 0) {
          logger.error(msg);
          errors.addError("ispettoriSelezionati", msg);
          //break;
        }
        if (ispettoreAssegnato != null && ispettoreAssegnato.getIdEnte() != null && ispettoreAggiunto.getIdEnte() != null) {
          if (ispettoreAssegnato.getIdEnte().intValue() != ispettoreAggiunto.getIdEnte().intValue()) {
            msg = "Ispettore secondario " + ispettoreAggiunto.getCognomeNome() + " non è dello stesso ente dell'ispettore assegnato (" + ispettoreAssegnato.getEnteAppartenenza() + ")";
            logger.error(msg);
            errors.addError("ispettoriSelezionati", msg);
            //break;
          }
        }
        ispettoriAggiunti.add(ispettoreAggiunto);
      } // for
      model.addAttribute("ispettoriAggiunti", ispettoriAggiunti);
    }
    
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("success", null);
      
      if (StringUtils.isNotBlank(missione.getNomeFile()) && !errors.containsKey("nomeFile")) {
        
        boolean segnalaErrore = false;
        
        if (missione.getIdMissione() != null && missione.getIdMissione() > 0) {
          // Siamo in modifica occorre controllare che il file sul db, se esistente, sia uguale a quello che si vuole salvare
          // altrimenti occorre segnalare all'utente che occorre ricaricare il file pdf dal browser
          MissioneDTO missioneDB = missioneEJB.findById(missione.getIdMissione());
          if (missioneDB.getNomeFile() == null || !missioneDB.getNomeFile().equals(missione.getNomeFile()))
            segnalaErrore = true;
        }
        else {
          if (StringUtils.isNotBlank(missione.getNomeFile())) // Se sono in inserimento e c'è un errore occorre sempre ricaricare il file locale
            segnalaErrore = true;
        }
        if (segnalaErrore)
          errors.addError("nomeFile", "Occorre premere Cerca File e ricaricare il file locale");
      }
      model.addAttribute("missione", missione);
      
      if (request.getParameter("from") != null && request.getParameter("from").equalsIgnoreCase("elenco") ||
          missione.getIdMissione() != null && missione.getIdMissione().intValue() > 0) {
        redirectAttributes.addFlashAttribute("success", null);
        redirectAttributes.addFlashAttribute("model", model);
        redirectAttributes.addFlashAttribute("errors", errors);
        redirectAttributes.addFlashAttribute("missione", missione);
        redirectAttributes.addFlashAttribute("ispettoriAggiunti", ispettoriAggiunti);
        if (missione.getIdMissione() == null || missione.getIdMissione().intValue() == 0) {
          return "redirect:add.do?from=" + request.getParameter("from");
        }
        return "redirect:edit.do?idMissione=" + missione.getIdMissione();
      }
      //return this.nuovaMissione(model, request.getParameter("from"), session, request);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("missione", missione);
      redirectAttributes.addFlashAttribute("ispettoriAggiunti", ispettoriAggiunti);
      return "redirect:new.do";
    }
    else
    {
      UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      if (utente != null)
        missione.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
      else
        missione.setExtIdUtenteAggiornamento(0L);
  
      Long idMissione = missione.getIdMissione();
      if (missione.getIdMissione() == null || missione.getIdMissione().intValue() == 0) {
        missione.setStato(StatoEnum.APERTA_BO.getName());
        idMissione = missioneEJB.insertMissione(missione);
      }
      else {
        missioneEJB.updateMissione(missione);
        return "redirect:search.do";
        //return "gestionemissione/elencoMissioni";
      }
      
      logger.info("missione salvata!");
      missione.setIdMissione(idMissione);
      //model.addAttribute("missione", missione);
      //return "redirect:edit.do?idMissione="+missione.getIdMissione()+"&editabile=true";
      if (request.getParameter("from") != null && request.getParameter("from").equalsIgnoreCase("elenco")) {
        return "redirect:search.do";
      }
      return "redirect:/home/index.do?idParent=2";
    }
  }

  @RequestMapping(value = "/missione/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    MissioneRequest mr = new MissioneRequest();
    if (session.getAttribute("missioneRequest") != null) {
      mr = (MissioneRequest) session.getAttribute("missioneRequest");
    }
    if (mr.getAnno() == null) {
      Calendar now = Calendar.getInstance();
      int currentYear = now.get(Calendar.YEAR);
      mr.setAnno(new Long(currentYear));
    }
    model.addAttribute("missioneRequest", mr);
    setBreadcrumbs(model, request);
    loadPopupCombo(model, session);
    session.setAttribute("currentPage", 1);
    
    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("tableMissione");
    cleanTableMapsInSession(session, tableNamesToRemove);
    
    return "gestionemissione/ricercaMissione";
  }

  @RequestMapping(value = "/missione/clearFilter")
  public String clearFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    cleanSession(session);
    return showFilter(model, session, request, response);
  }
  
  @RequestMapping(value = {"/missione/new", "/missione/add"})
  public String nuovaMissione(Model model, @RequestParam(value = "from", required = false) String from, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException, ParseException
  {
    MissioneDTO missione = (MissioneDTO) model.asMap().get("missione");
    
    if (missione == null) {
      missione = new MissioneDTO(StatoEnum.APERTA_BO.getName());
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      missione.setDataOraInizioMissione(formatter.parse(formatter.format(new Date())));
    }
    model.addAttribute("missione", missione);
    
    List<AnagraficaDTO> listaIspettori = anagraficaEJB.findByFilter(new AnagraficaDTO(true));
    model.addAttribute("listaIspettori", listaIspettori);
    List<StatoEnum> stati = Arrays.asList(StatoEnum.values());
    model.addAttribute("stati", stati);
    model.addAttribute("editabile", true);
    setBreadcrumbs(model, request);
    showFlashMessages(model, request);
    model.addAttribute("from", from);
    return "gestionemissione/nuovaMissione";
  }

  @RequestMapping(value = {"/missione/edit", "/missione/show"})
  public String edit(Model model, @RequestParam(value = "idMissione", required = false) String idMissione, HttpServletRequest request, HttpServletResponse response, HttpSession session,
      RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    if (StringUtils.isBlank(idMissione)) {
      idMissione = (String) session.getAttribute("idMissione");
    }
    else
      session.setAttribute("idMissione", idMissione);
    
    MissioneDTO dto = (model.asMap().get("missione") != null) ? (MissioneDTO) model.asMap().get("missione") : missioneEJB.findById(Long.decode(idMissione));
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    AnagraficaDTO ispettoreAssegnato = (dto.getIdIspettoreAssegnato() != null && dto.getIdIspettoreAssegnato().intValue() > 0) ? anagraficaEJB.findById(dto.getIdIspettoreAssegnato().intValue()) : null;
    List<AnagraficaDTO> ispettoriAggiunti = (model.asMap().get("ispettoriAggiunti") != null) ? (List<AnagraficaDTO>) model.asMap().get("ispettoriAggiunti") : missioneEJB.getIspettoriAggiunti(Long.decode(idMissione));

    if (ispettoreAssegnato != null && !isUtenteAuthorizedByMissione(utente, dto)) {
      redirectAttributes.addFlashAttribute("error", "Utente non autorizzato");
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      return "redirect:search.do";
    }
    
    model.addAttribute("missione", dto);
    List<AnagraficaDTO> listaIspettori = anagraficaEJB.findByFilter(new AnagraficaDTO(true));
    if (ispettoreAssegnato != null && !listaIspettori.contains(ispettoreAssegnato))
      listaIspettori.add(ispettoreAssegnato);
    List<StatoEnum> stati = Arrays.asList(StatoEnum.values());
    model.addAttribute("stati", stati);
    model.addAttribute("listaIspettori", listaIspettori);
    model.addAttribute("ispettoriAggiunti", ispettoriAggiunti);
    //lista rilevazioni
    RilevazioneDTO rilevazioneFilter = new RilevazioneDTO();
    rilevazioneFilter.setIdMissione(new Integer(idMissione).intValue());
    List<RilevazioneDTO> listaRilevazioni = rilevazioneEJB.findByFilter(rilevazioneFilter);
    if (listaRilevazioni != null) {
      for(RilevazioneDTO rilevazione : listaRilevazioni) {
        rilevazione.setCampionamenti(gestioneCampioneEJB.findByIdRilevazione(rilevazione.getIdRilevazione()));
        rilevazione.setTrappolaggi(gestioneTrappolaEJB.findByFilter(new TrappolaggioDTO(rilevazione.getIdRilevazione())));
        rilevazione.setIspezioniVisive(gestioneVisualEJB.findByFilter(new IspezioneVisivaDTO(rilevazione.getIdRilevazione())));
      }
    }
    model.addAttribute("listaRilevazioni", listaRilevazioni);
    
    setBreadcrumbs(model, request);
    showFlashMessages(model, request);
    
    if (utente == null) {
      logger.info("utente null");
      if (RequestContextUtils.getInputFlashMap(request) != null) {
        logger.info("ripristino l'utente in sessione!");
        session.setAttribute("utenteAbilitazioni", model.asMap().get("utente"));
      }
    }
    boolean editabile = false;
    int idStatoVerbale = missioneEJB.getIdStatoVerbaleByIdMissione(dto.getIdMissione());  // lo stato verbale (se presente deve essere max 1 (bozza) per consentire modifiche
    if (request.getServletPath().indexOf("/edit.do") > -1 && idStatoVerbale < 2) {
      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_MISSIONI") && utente.isReadWrite();
    }
    model.addAttribute("editabile", editabile);
    return "gestionemissione/nuovaMissione";
  }

  @RequestMapping(value = "/missione/search")
  public String search(Model model, @ModelAttribute("missioneRequest") MissioneRequest missioneRequest, HttpSession session, HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    try
    {
      if (missioneRequest.checkNull())
        missioneRequest = (MissioneRequest) session.getAttribute("missioneRequest");
      if (missioneRequest == null)
        missioneRequest = new MissioneRequest();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    
    //CARICO LE LISTE
    List<Integer> listaTipoArea = missioneRequest.getTipoArea();
    List<Integer> listaSpecie = missioneRequest.getSpecieVegetale();
    List<Integer> listaOrganismo = missioneRequest.getOrganismoNocivo();
    List<Integer> listaIspetAssegnato = missioneRequest.getIspettoreAssegnato();

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
      List<SpecieVegetaleDTO> listaSpecieVeg = specieVegetaleEJB.findByIdMultipli(idSpecie);
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
    model.addAttribute("missioneRequest", missioneRequest);
    session.setAttribute("missioneRequest", missioneRequest);
    model.addAttribute("from", "search");
    setBreadcrumbs(model, request);
    return "gestionemissione/elencoMissioni";
  }

  @Lazy
  @RequestMapping(value = "/missione/filtri")
  public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableMissione") == null || "{}".equals(filtroInSessione.get("tableMissione"))) {
        filtroInit = "";//"{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableMissione");
      } 
       // filtroInit =
           // if(anagrafica.getCognome()!=null &&)
          //  + ",\"cognome\":{\"cnt\":[\""+dto.getCognome().toUpperCase()+"\"]}"
          //  filtroInit += "}";  
       // value='{"annoCampagna":{"cnt":"2016"},"descrizione":{"_values":["Liquidato"]}}'
      filtroInSessione.put("tableMissione", filtroInit);
      return filtroInSessione;
  }
  
  @RequestMapping(value="/missione/getpdf", method=RequestMethod.GET, produces = "application/pdf")
  public ResponseEntity<byte[]> getPDF(@RequestParam(value = "idMissione") Long idMissione, HttpServletResponse response) {

      //MissioneDTO missione = null;
      //ResponseEntity<byte[]> response = null;
      try
      {
        logger.debug("getPDF");
        //missione = missioneEJB.findById(idMissione);
        //byte[] contents = missione.getPdfTrasferta().getBytes();
        byte[] pdf = missioneEJB.getPdfTrasferta(idMissione);

        HttpHeaders headers = new HttpHeaders();
        //MediaType mediaType = MediaType.parseMediaType("application/pdf");
        //headers.setContentType(mediaType);
        // Here you have to set the actual filename of your pdf
        String filename = "trasferta_" + idMissione + ".pdf";
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

  @Lazy
  @RequestMapping(value = "/missione/downloadExcel")
  public ModelAndView downloadExcel(Model model, HttpServletRequest request) throws InternalUnexpectedException {

    MissioneRequest missioneRequest = (MissioneRequest) request.getSession().getAttribute("missioneRequest");
    if (missioneRequest == null) {
      missioneRequest = new MissioneRequest();
    }
    // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    List<MissioneDTO> missioni = missioneEJB.findByFilter(missioneRequest, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
    HashMap<String, Object> modelForXls = new HashMap<String, Object>();
    
    AnagraficaDTO ispettore = null;
    if (missioneRequest.getIdIspettore() != null && missioneRequest.getIdIspettore() > 0) {
      ispettore = anagraficaEJB.findById(missioneRequest.getIdIspettore().intValue());
      modelForXls.put("ispettore", ispettore);
    }
    if (missioneRequest.getIdSpecieVegetale() != null) {
      SpecieVegetaleDTO specie = specieVegetaleEJB.findById(missioneRequest.getIdSpecieVegetale());
      modelForXls.put("specie", specie);
    }
    if (missioneRequest.getIdOrganismoNocivo() != null && missioneRequest.getIdOrganismoNocivo() > 0) {
      OrganismoNocivoDTO organismoNocivo = organismoNocivoEJB.findById(missioneRequest.getIdOrganismoNocivo());
      modelForXls.put("organismoNocivo", organismoNocivo);
    }
    modelForXls.put("missioni", missioni);
    modelForXls.put("missioneRequest", missioneRequest);
    return new ModelAndView("excelMissioniView", modelForXls);
  }

  @ResponseBody
  @RequestMapping(value = "/rest/missione", consumes = { "application/json" }, produces = { "application/json" }, method = RequestMethod.POST)
  public ResponseEntity<?> saveMissione(@Valid @RequestBody Missione body, HttpServletRequest request) throws MalformedURLException, IOException
  {
    Missione missione = body;
    
    try {
      
      //String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      //String cf = verifyToken(jwt);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(missione);
      logger.debug(json);
      
      if (StringUtils.isBlank(missione.getDataMissione())) {
        throw new Exception("La Data missione è obbligatoria");
      }
      if (StringUtils.isBlank(missione.getOraInizio())) {
        throw new Exception("L'Ora inizio è obbligatoria");
      }
      if (StringUtils.isBlank(missione.getCfIspettore())) {
        throw new Exception("Specificare il Codice fiscale dell'ispettore");
      }
      MissioneDTO missioneDTO = new MissioneDTO();
      BeanUtils.copyProperties(body, missioneDTO);
    
      AnagraficaDTO anagraficaFilter = new AnagraficaDTO(missione.getCfIspettore());
      anagraficaFilter.setActive(true);
      List<AnagraficaDTO> ispettori = anagraficaEJB.findByFilter(anagraficaFilter);
      if (ispettori == null || ispettori.size() == 0) {
        throw new Exception("Codice fiscale ispettore non trovato");
      }
      AnagraficaDTO ispettore = ispettori.get(0);
      missioneDTO.setIdIspettoreAssegnato(ispettore.getIdAnagrafica().longValue());
      
      if (body.getIspettoriAggiunti() != null) {
        List<Integer> ispettoriAggiunti = new ArrayList<Integer>();
        for (int i=0; i<body.getIspettoriAggiunti().size(); i++) {
          ispettoriAggiunti.add(body.getIspettoriAggiunti().get(i).getIdAnagrafica());
        }
        missioneDTO.setIspettoriSelezionati(ispettoriAggiunti);
      }
    
      String dataOraInizioStr = body.getDataMissione().replaceAll("-", "/") + " " + body.getOraInizio() + ":00";
      Date dataOraInizio = dtf.parse(dataOraInizioStr);
      missioneDTO.setDataOraInizioMissione(dataOraInizio);
  
      if (StringUtils.isNotBlank(body.getOraFine())) {
        String dataOraFineStr = body.getDataMissione().replaceAll("-", "/") + " " + body.getOraFine() + ":00";
        Date dataOraFine = dtf.parse(dataOraFineStr);
        missioneDTO.setDataOraFineMissione(dataOraFine);
      }
    
      // inizio controllo ispettori della missione, non devono essere in altre missioni per il giorno
      List<MissioneDTO> list = missioneEJB.findByDateAndIspettore(dataOraInizioStr, ispettore.getIdAnagrafica(), missione.getIdMissione());
      if (list != null && list.size() > 0) {
        String msg = "L'ispettore " + ispettore.getCognomeNome() + " ha già una missione assegnata per il giorno " + sdf.format(dataOraInizio);
        logger.error(msg);
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", msg);
        err.setMessage(msg);
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.CONFLICT);
      }

      if (missioneDTO.getIspettoriSelezionati() != null && missioneDTO.getIspettoriSelezionati().size() > 0) {
        List<AnagraficaDTO> ispettoriAggiunti = new ArrayList<AnagraficaDTO>();
        for (int i : missioneDTO.getIspettoriSelezionati()) {
          AnagraficaDTO anagrafica = anagraficaEJB.findById(i);
          if (i == ispettore.getIdAnagrafica().intValue()) {    // L'ispettore principale non può essere fra gli ispettori aggiunti
            String msg = "L'ispettore aggiunto " + anagrafica.getCognomeNome() + " è l'ispettore principale, non può comparire fra gli ispettori aggiunti";
            logger.error(msg);
            ErrorResponse err = new ErrorResponse();
            err.addError("Errore", msg);
            err.setMessage(msg);
            return new ResponseEntity<ErrorResponse>(err, HttpStatus.CONFLICT);
          }
          ispettoriAggiunti.add(anagrafica);
          List<MissioneDTO> missioni = missioneEJB.findByDateAndIspettore(dataOraInizioStr, anagrafica.getIdAnagrafica(), missione.getIdMissione());
          if (missioni != null && missioni.size() > 0) {
            String msg = "L'ispettore aggiunto " + anagrafica.getCognomeNome() + " ha già una missione assegnata per il giorno " + sdf.format(dataOraInizio);
            logger.error(msg);
            ErrorResponse err = new ErrorResponse();
            err.addError("Errore", msg);
            err.setMessage(msg);
            return new ResponseEntity<ErrorResponse>(err, HttpStatus.CONFLICT);
          }
        } // for
      }
      // fine controllo
      
      missioneDTO.setExtIdUtenteAggiornamento(0L);  // da modificare una volta che sarà possibile integrare con Papua
      
      Long idMissione = missione.getIdMissione();
      if (missione.getIdMissione() == null || missione.getIdMissione().intValue() == 0) {
        missioneDTO.setStato(StatoEnum.APERTA_APP.getName());
        idMissione = missioneEJB.insertMissione(missioneDTO);
      }
      else {
        missioneEJB.updateMissione(missioneDTO);
      }
      missione.setIdMissione(idMissione);
      //AnagraficaDTO ispettoreAssegnato = anagraficaEJB.findById(missione.getIdIspettoreAssegnato().intValue());
      missione.setCognomeIspettore(ispettore.getCognome());
      missione.setNomeIspettore(ispettore.getNome());
    }
    catch (Throwable e) {
      logger.debug("Errore nel metodo saveMissione durante la registrazione: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo saveMissione durante la registrazione: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Missione>(missione, HttpStatus.OK);
  }

  private List<Missione> fillMissioni(List<Missione> list) throws InternalUnexpectedException {
    List<Missione> listFull = new ArrayList<Missione>();
    if (list != null) {
      Missione missione = null;
      for (int i=0; i<list.size(); i++) {
        missione = list.get(i);
        RilevazioneDTO rilevazioneFilter = new RilevazioneDTO();
        rilevazioneFilter.setIdMissione(missione.getIdMissione().intValue());
        List<Rilevazione> listaRilevazioni = rilevazioneEJB.findByFilterForApi(rilevazioneFilter);
        if (listaRilevazioni != null && listaRilevazioni.size() > 0) {
          for (int j=0; j<listaRilevazioni.size(); j++) {
            Rilevazione rilev = listaRilevazioni.get(j);
            List<IspezioneVisivaDTO> ispezioniVisive = gestioneVisualEJB.findByFilter(new IspezioneVisivaDTO(rilev.getIdRilevazione()));
            if (ispezioniVisive == null) {
              ispezioniVisive = new ArrayList<IspezioneVisivaDTO>();
            }
            else
            {
              for (int k=0; k<ispezioniVisive.size(); k++) {
                IspezioneVisivaDTO ispezione = ispezioniVisive.get(k);
                
                Long l = gpsFotoEJB.selectFotoVisual(Long.valueOf(ispezioniVisive.get(k).getIdIspezione()));
                if (l>0)
                  ispezioniVisive.get(k).setFoto(true);
                else
                  ispezioniVisive.get(k).setFoto(false);
                
                List<IspezioneVisivaPiantaDTO> piante = ispezioneVisivaPiantaEJB.findByIdIspezioneVisiva(ispezione.getIdIspezione());
                if (piante != null) {
                  ispezione.setIspezioni(piante.toArray(new IspezioneVisivaPiantaDTO[piante.size()]));
                }
                //List<Integer> organismiNocivi = gestioneVisualEJB.findOrganismiNociviByVisual(ispezione.getIdIspezione());
                List<OrganismiNocivi> organismiNocivi = gestioneVisualEJB.findOrganismiNociviFlagTrovatoByVisual(ispezione.getIdIspezione());
                if (organismiNocivi != null) {
                  try {
                  //  ispezione.setOrganismi(organismiNocivi.toArray(new Integer[organismiNocivi.size()]));
                    ispezione.setOrganismi(organismiNocivi.toArray(new OrganismiNocivi[organismiNocivi.size()]));
                  }
                  catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              }
            }
            rilev.setIspezioniVisive(ispezioniVisive);
            List<CampionamentoDTO> campionamenti = gestioneCampioneEJB.findByFilter(new CampionamentoRequest(rilev.getIdRilevazione()), null, null);
            if (campionamenti == null) {
              campionamenti = new ArrayList<CampionamentoDTO>();
            }
            else
            {
              for (int k=0; k<campionamenti.size(); k++) {
                CampionamentoDTO campionamento = campionamenti.get(k);
                
                Long l = gpsFotoEJB.selectFotoCampione(Long.valueOf(campionamenti.get(k).getIdCampionamento()));
                if (l>0)
                  campionamenti.get(k).setFoto(true);
                else
                  campionamenti.get(k).setFoto(false);
                
                List<CampionamentoSpecOnDTO> organismiNocivi = gestioneCampioneEJB.findOnByIdCampionamento(campionamenti.get(k).getIdCampionamento());
                if (organismiNocivi != null) {
                  List<Integer> organismi = new ArrayList<Integer>();
                  for (CampionamentoSpecOnDTO on : organismiNocivi) {
                    organismi.add((int) on.getIdSpecieOn());
                  }
                  campionamento.setOrganismiNocivi(organismi.toArray(new Integer[organismiNocivi.size()]));
                }
                else
                {
                  campionamento.setOrganismiNocivi(new Integer[0]);
                }
              }
            }
            rilev.setCampionamenti(campionamenti);
            TrappolaggioDTO trappolaggioFilter = new TrappolaggioDTO();
            trappolaggioFilter.setIdRilevazione(rilev.getIdRilevazione());
            List<TrappolaggioDTO> trappolaggi = gestioneTrappolaEJB.findByFilter(trappolaggioFilter);
            if (trappolaggi == null) {
              trappolaggi = new ArrayList<TrappolaggioDTO>();
            }
            rilev.setTrappolaggi(trappolaggi);
          }
        } else {
          listaRilevazioni = new ArrayList<Rilevazione>();
        }
        missione.setRilevazioni(listaRilevazioni);
        AnagraficaDTO ispettoreAssegnato = anagraficaEJB.findById(missione.getIdIspettoreAssegnato().intValue());
        missione.setCognomeIspettore(ispettoreAssegnato.getCognome());
        missione.setNomeIspettore(ispettoreAssegnato.getNome());
        missione.setCfIspettore(ispettoreAssegnato.getCfAnagraficaEst());
        List<AnagraficaDTO> ispettoriAggiunti = missioneEJB.getIspettoriAggiunti(missione.getIdMissione());
        missione.setIspettoriAggiunti(ispettoriAggiunti);
        listFull.add(missione);
      }
    }
    return listFull;
  }
  
  // API PER APP MOBILE
  @RequestMapping(value = "/rest/missione/{idMissione}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> getMissione(@PathVariable( value = "idMissione") Long idMissione, HttpServletRequest request)
  {
    Missione missione = null;
    try {

      logger.debug("id missione: " + idMissione);
      
      List<Missione> list = missioneEJB.getMissioni(new MissioneRequest(idMissione));
      
      if (list != null && list.size() > 0) {
        list = this.fillMissioni(new ArrayList<Missione>(list));
        missione = list.get(0);
      }
      else {
        ErrorResponse er = new ErrorResponse();
        er.addError("idMissione", "id missione non valido");
        er.setMessage("Errore nella ricerca della missione");
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
        logger.debug("Errore nel metodo getMissione: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella ricerca della missione: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Missione>(missione, HttpStatus.OK);
  }

  @RequestMapping(value = "/missione/delete")
  public String canDelete(Model model, @RequestParam(value = "id") Long id, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableMissione");
      return "gestionetabelle/confermaElimina";
  }

   @RequestMapping(value = "/missione/remove", method = RequestMethod.GET)
   public RedirectView remove(Model model, @RequestParam(value = "id") Long idMissione, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
   {
     try {
       List<MissioneDTO> list = missioneEJB.findByFilter(new MissioneRequest(idMissione), null, null);
       if (list == null || list.size() == 0) {
         throw new Exception("Missione con id " + idMissione + " non trovata");
       }
       missioneEJB.deleteMissione(idMissione);
       List<MissioneDTO> missioni = missioneEJB.findByFilter(new MissioneRequest(), null, null);  // Aggiungere filtro id anagrafica/ente
       setBreadcrumbs(model, request);
       model.addAttribute("missioni", missioni);
     } catch (Exception e) {
         logger.debug("Errore nel metodo getMissione: " + e.getMessage());
         ErrorResponse er = new ErrorResponse();
         er.addError("Errore", e.getMessage());
         er.setMessage("Errore interno nella cancellazione della missione");
     }
     //return "gestionemissione/elencoMissioni";
     return new RedirectView("search.do", true);
   }
   
  // API PER APP MOBILE
  @RequestMapping(value = "/rest/missione/{idMissione}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<?> deleteMissione(@PathVariable (value = "idMissione") Long idMissione, HttpServletRequest request)
  {
    try {
      logger.debug("id_missione: " + idMissione);
      List<MissioneDTO> list = missioneEJB.findByFilter(new MissioneRequest(idMissione), null, null);
      if (list == null || list.size() == 0) {
        throw new Exception("Missione con id " + idMissione + " non trovata");
      }
      
      missioneEJB.deleteMissione(idMissione);
      
    } catch (Exception e) {
        logger.debug("Errore nel metodo getMissione: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella cancellazione della missione: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Map<String, String> ok = new HashMap<String, String>();
    ok.put("status", "ok");
    return new ResponseEntity<java.util.Map<String,String>>(ok,HttpStatus.OK);
  }

  @RequestMapping(value="/rest/pdf-trasferta/{idMissione}", method=RequestMethod.GET, headers = {"Accept=*/*"}, produces = "application/pdf")
  public ResponseEntity<?> getPdfTrasferta(@PathVariable(value = "idMissione") Long idMissione, HttpServletRequest request, HttpServletResponse response) {

      MissioneDTO missione = null;
      ResponseEntity<byte[]> resp = null;
      try
      {
        logger.debug("getPdfTrasferta");
        missione = missioneEJB.findById(idMissione);
        if (missione != null) {
          byte[] pdf = missioneEJB.getPdfTrasferta(idMissione);
          //if (missione.getPdfTrasferta() == null || missione.getPdfTrasferta().isEmpty()) {
          if (pdf == null || pdf.length == 0) {
            ErrorResponse er = new ErrorResponse();
            er.addError("pdf trasferta", "non presente");
            er.setMessage("Pdf trasferta non presente");
            return new ResponseEntity<ErrorResponse>(er,HttpStatus.NOT_FOUND);
          }
          //byte[] contents = missione.getPdfTrasferta().getBytes();
          byte[] encoded = java.util.Base64.getEncoder().encode(pdf);
          resp = new ResponseEntity<byte[]>(encoded, HttpStatus.OK);
        }
        else {
          ErrorResponse er = new ErrorResponse();
          er.addError("idMissione", "id missione non valido");
          er.setMessage("Errore nella ricerca della missione");
          return new ResponseEntity<ErrorResponse>(er,HttpStatus.NOT_FOUND);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
        logger.debug("Errore nel metodo getPdfTrasferta: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella ricerca della missione");
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return resp;
  }

  @RequestMapping(value = "/rest/missioni", method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
  @ResponseBody
  public ResponseEntity<?> getMissioni(@Valid @RequestBody MissioneRequest missioneRequest, HttpServletRequest request)
  {
    List<Missione> missioni = null;
    logger.debug("getMissioni");
    try
    {
      String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      String cf = TokenUtils.verifyToken(jwt);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(missioneRequest);
      logger.debug(json);
      missioneRequest.setCfIspettore(cf);
      missioni = missioneEJB.getMissioni(missioneRequest);
      missioni = this.fillMissioni(new ArrayList<Missione>(missioni));
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      logger.debug("Errore nel metodo getMissioni: " + e.getMessage());
      ErrorResponse er = new ErrorResponse();
      er.addError("Errore", e.getMessage());
      er.setMessage("Errore interno nella ricerca delle missioni: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<Missione>>(missioni, HttpStatus.OK);
  }

  private void loadPopupCombo(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    MissioneRequest missioneRequest = (MissioneRequest) session.getAttribute("missioneRequest");
    
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
    //

    List<TipoAreaDTO> tipoAree = new ArrayList<>();
    model.addAttribute("tipoAree", tipoAree);
    
    if (missioneRequest != null) {
      // Tipo aree
      if (missioneRequest.getTipoArea() != null) {
        String idArea="";
        for(Integer i : missioneRequest.getTipoArea()) {
          idArea+=i+",";
        }
        idArea=idArea.substring(0,idArea.length()-1);
        List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
        model.addAttribute("tipoAree", listaAree);   
      }
      // Specie vegetali
      if (missioneRequest.getSpecieVegetale() != null) {
        String idSpecie="";
        for (Integer i : missioneRequest.getSpecieVegetale()) {
          idSpecie+=i+",";
        }
        idSpecie=idSpecie.substring(0,idSpecie.length()-1);
        List<SpecieVegetaleDTO> listaSpecieVeg = specieVegetaleEJB.findByIdMultipli(idSpecie);
        model.addAttribute("listaSpecieVegetali", listaSpecieVeg);     
      }
      // Organismi nocivi
      if (missioneRequest.getOrganismoNocivo() != null) {
          String idOn="";
          for (Integer i : missioneRequest.getOrganismoNocivo()) {
            idOn+=i+",";
          }
          idOn=idOn.substring(0,idOn.length()-1);
          List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findByIdMultipli(idOn);
          model.addAttribute("listaON", listaOn);
       }
      // ispettori assegnati
      if (missioneRequest.getIspettoreAssegnato() != null && missioneRequest.getIspettoreAssegnato().size() > 0)
      {
          String idIsSec="";
          for(Integer i : missioneRequest.getIspettoreAssegnato()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettori",listaIspettAssegnati);          
      }
      // comune
      if (StringUtils.isNotBlank(missioneRequest.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(missioneRequest.getIstatComune());
        if (comuneDTO != null)
        {
          model.addAttribute("provSceltaComune", comuneDTO.getSiglaProvincia());
          model.addAttribute("comuneSceltaComune", comuneDTO.getDescrizioneComune());
        }
      }
    }

  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(MethodArgumentNotValidException exception) {
    
    ErrorResponse err = new ErrorResponse();

    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
    for (int i=0; i<fieldErrors.size(); i++) {
      err.addError(fieldErrors.get(i).getField(), fieldErrors.get(i).getDefaultMessage());
    }
    
    return err;
  }

  @RequestMapping(value = "/missione/searchComuni", produces = "application/json", method = RequestMethod.POST)
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

  //aggiunto da Barbara
  @RequestMapping(value = "/missione/getMissioneJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getMissioneJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    MissioneRequest missioneRequest = (MissioneRequest) session.getAttribute("missioneRequest");
    
    // Impostazione id anagrafica e id ente da passare al service per la ricerca delle missioni
    // filtrata in base al profilo (livello) dell'utente loggato
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    
    DataFilter dataFilter = getFiltroDati(utente);
    List<MissioneDTO> lista = missioneEJB.findByFilter(missioneRequest, dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
    
    if (lista == null) {
      lista = new ArrayList<>();
    }
    //session.removeAttribute("missioneRequest");
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }

  private void checkEnvironment() {

    ResourceBundle res = safeGetBundle("avivws");
    
    if (res == null)
      res = safeGetBundle("config");
    
    logger.debug("Base url ws Aviv: " + System.getProperty(AvivwsServiceFactory.AVIVWS_REST_PROPERTY_BASE_URL));
    
    if (res != null) {
      String restServiceUrl = res.getString(AvivwsServiceFactory.AVIVWS_REST_PROPERTY_BASE_URL);
      String usr = res.getString(AvivwsServiceFactory.AVIVWS_REST_PROPERTY_USERNAME);
      String psw = res.getString(AvivwsServiceFactory.AVIVWS_REST_PROPERTY_PASSWORD);
      if (restServiceUrl != null)
        System.setProperty(AvivwsServiceFactory.AVIVWS_REST_PROPERTY_BASE_URL, restServiceUrl); // https://<WEB_SERVER_HOST:PORT>/avivws/iuffi/
      if (usr != null)
        System.setProperty(AvivwsServiceFactory.AVIVWS_REST_PROPERTY_USERNAME, usr);            // iuffi
      if (psw != null)
        System.setProperty(AvivwsServiceFactory.AVIVWS_REST_PROPERTY_PASSWORD, psw);            // mypass
    }
  }
  
  @RequestMapping(value = "/rest/anagrafica-aviv/{codAz}", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> getAnagraficheAviv(@PathVariable(value = "codAz") String codAz, HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    logger.debug("********** inizio metodo chiamata servizio /rest/anagrafica-aviv/" + codAz);
    checkEnvironment();
    logger.debug("********** checkEnvironment OK");
    AvivwsRESTClient avivwsClient = null;
    Risposta<Azienda> risposta = null;
    try
    {
      AvivwsServiceFactory.setLoggerName(logger.getName()); // l'oggetto logger viene ereditato da IuffiAbstractEJB
      logger.debug("********** LoggerName settato");
      avivwsClient = AvivwsServiceFactory.getRestServiceClient();
      avivwsClient.setTimeout(6000);
      avivwsClient.setConnectionTimeout(6000);
      logger.debug("********* timeout: " + avivwsClient.getTimeout());
      logger.debug("********* connectionTimeout: " + avivwsClient.getConnectionTimeout());
      risposta = avivwsClient.ricercaAzienda(codAz);
      logger.debug("********** ricercaAzienda OK");
    }
    catch (Exception e)
    {
      String errore = "WS Aviv (ricercaAzienda) - Chiamata in errore: " + e.getMessage();
      logger.error(errore);
      ErrorResponse er = new ErrorResponse();
      er.addError("Errore", errore);
      er.setMessage(errore);
      return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Risposta<Azienda>>(risposta, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/specie-aviv/{codAz}", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> getSpecieAviv(@PathVariable(value = "codAz") String codAz, HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    logger.debug("********** inizio metodo chiamata servizio /rest/specie-aviv/" + codAz);
    checkEnvironment();
    logger.debug("********** checkEnvironment OK");
    AvivwsRESTClient avivwsClient = null;
    Risposta<List<Attivita>> risposta = null;
    
    try
    {
      AvivwsServiceFactory.setLoggerName(logger.getName()); // l'oggetto logger viene ereditato da IuffiAbstractEJB
      logger.debug("********** LoggerName settato");
      avivwsClient = AvivwsServiceFactory.getRestServiceClient();
      avivwsClient.setTimeout(6000);
      avivwsClient.setConnectionTimeout(6000);
      logger.debug("********* timeout: " + avivwsClient.getTimeout());
      logger.debug("********* connectionTimeout: " + avivwsClient.getConnectionTimeout());
      risposta = avivwsClient.estraiSpecie(codAz);
      logger.debug("********** estraiSpecie OK");
    }
    catch (Exception e)
    {
      String errore = "WS Aviv (estraiSpecie) - Chiamata in errore: " + e.getMessage();
      logger.error(errore);
      ErrorResponse er = new ErrorResponse();
      er.addError("Errore", errore);
      er.setMessage(errore);
      return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Risposta<List<Attivita>>>(risposta, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/verbale-aviv/{idUte}", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> scaricaVerbale(@PathVariable(value = "idUte") Integer idUte, HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    logger.debug("********** inizio metodo chiamata servizio /rest/verbale-aviv/" + idUte);
    checkEnvironment();
    logger.debug("********** checkEnvironment OK");
    AvivwsRESTClient avivwsClient = null;
    Risposta<Pdf> risposta = null;

    try
    {
      AvivwsServiceFactory.setLoggerName(logger.getName()); // l'oggetto logger viene ereditato da IuffiAbstractEJB
      logger.debug("********** LoggerName settato");
      avivwsClient = AvivwsServiceFactory.getRestServiceClient();
      avivwsClient.setTimeout(6000);
      avivwsClient.setConnectionTimeout(6000);
      logger.debug("********* timeout: " + avivwsClient.getTimeout());
      logger.debug("********* connectionTimeout: " + avivwsClient.getConnectionTimeout());
      risposta = avivwsClient.scaricaVerbale(idUte);
      logger.debug("********** scaricaVerbale OK");
      
      try {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(risposta);
        logger.debug(json);
      } catch (Exception e) {
          logger.error("Errore loggando la risposta di Aviv: " + e.getMessage());
      }
        
    }
    catch (Exception e)
    {
      String errore = "WS Aviv (scaricaVerbale) - Chiamata in errore: " + e.getMessage();
      logger.error(errore);
      ErrorResponse er = new ErrorResponse();
      er.addError("Errore", errore);
      er.setMessage(errore);
      return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Risposta<Pdf>>(risposta, HttpStatus.OK);
  }

}
