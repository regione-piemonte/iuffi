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
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.DiametroDTO;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaPiantaDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaSpecieOnDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.PositivitaDTO;
import it.csi.iuffi.iuffiweb.model.RilevazioneDTO;
import it.csi.iuffi.iuffiweb.model.RuoloDTO;
import it.csi.iuffi.iuffiweb.model.SceltaEsclusiva;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.api.Coordinate;
import it.csi.iuffi.iuffiweb.model.api.Ubicazione;
import it.csi.iuffi.iuffiweb.model.request.IspezioneVisivaRequest;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.TokenUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;


@SuppressWarnings("unchecked")
@Controller
@IuffiSecurity(value = "GESTIONEVISUAL", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class GestioneVisualController extends TabelleController
{

  @Autowired
  private ISpecieVegetaleEJB specieEJB;

  @Autowired
  private IOrganismoNocivoEJB onEJB;
  
  @Autowired
  private IGestioneVisualEJB ispezioneVisivaEJB;
  
  @Autowired
  private IAnagraficaEJB anagraficaEJB;
  
  @Autowired
  private IIspezioneVisivaPiantaEJB ispezioneVisivaPiantaEJB;
  
  @Autowired
  private ITipoAreaEJB tipoAreaEJB;
  
  @Autowired
  private IQuadroEJB quadroEJB;
  
  @Autowired
  private IRicercaEJB ricercaEJB;
  
  @Autowired
  private IRilevazioneEJB rilevazioneEJB;
  
  @Autowired  
  private IGpsFotoEJB gpsFotoEJB;
  
  @Autowired
  private IMissioneEJB missioneEJB;
  
  @Autowired
  private ISpecieVegetaleEJB specieVegetaleEJB;  
  
  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;  
  
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  private static final SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }


  @RequestMapping(value = "/gestioneVisual/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    IspezioneVisivaRequest ivr = new IspezioneVisivaRequest();
    if (session.getAttribute("ispezioneVisivaRequest") != null) {
      ivr = (IspezioneVisivaRequest) session.getAttribute("ispezioneVisivaRequest");
    }
    if (ivr.getAnno() == null) {
      Calendar now = Calendar.getInstance();
      int currentYear = now.get(Calendar.YEAR);
      ivr.setAnno(new Long(currentYear));
    }
    model.addAttribute("ispezioneVisivaRequest", ivr);
    
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    List<SpecieVegetaleDTO> listaSpecie= specieEJB.findAll();
    model.addAttribute("listaSpecie",listaSpecie);

    List<OrganismoNocivoDTO> listaOn= onEJB.findAll();
    model.addAttribute("listaOn",listaOn);

    setBreadcrumbs(model, request);
    loadPopupCombo(model, session);
    session.setAttribute("currentPage", 1);
    
    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("tableVisual");
    cleanTableMapsInSession(session, tableNamesToRemove);
    
    return "gestionevisual/ricercaVisual";
  }
  
  @RequestMapping(value = {"/gestioneVisual/save", "/gestioneVisual/saveFromListaMissioni", "/gestioneVisual/saveFromMissione"})
  public String save(Model model, @ModelAttribute("ispezioneVisiva") IspezioneVisivaDTO ispezioneVisiva,  @ModelAttribute("on") OrganismoNocivoDTO on,  
      @ModelAttribute("pianta") IspezioneVisivaPiantaDTO pianta, HttpSession session, HttpServletRequest request,
      HttpServletResponse response, RedirectAttributes redirectAttributes,BindingResult bindingResult) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    //DataFilter data =getFiltroDati(utente);
    
    RuoloDTO ruolo = getRuolo(utente);
    model.addAttribute("ruolo", ruolo);
    //IspezioneVisivaDTO dto = ispezioneVisiva;
    //dto = ispezioneVisivaEJB.findById(dto.getIdIspezione());
    IspezioneVisivaDTO dto = ispezioneVisivaEJB.findById(ispezioneVisiva.getIdIspezione());
    //Integer idAnInt = (int) ispezioneVisiva.getIdAnagrafica();
    //Integer idTipoArea = (int) ispezioneVisiva.getIdTipoArea();
    if(ruolo!= null && (ruolo.getAmministratore()!=null && ruolo.getAmministratore().equals(true)) 
            || (ruolo.getFunzionarioBO()!=null && ruolo.getFunzionarioBO().equals(true)) )
    {
  		if (ispezioneVisiva.getIdSpecieVegetale()==null || ispezioneVisiva.getIdSpecieVegetale() == 0) {
  		  errors.addError("idSpecieVegetale", "Campo Obbligatorio. Indicare una specie vegetale");
  		}
		
	    if (ispezioneVisiva.getIdTipoArea()==null || ispezioneVisiva.getIdTipoArea() == 0) {
	      errors.addError("idTipoArea", "Campo Obbligatorio. Indicare una tipologia area");
	    }

	    if (ispezioneVisiva.getIdAnagrafica()==null || ispezioneVisiva.getIdAnagrafica() == 0) {
	      errors.addError("idAnagrafica", "Campo Obbligatorio. Indicare un ispettore operante");
	    }
    }

    boolean editSpecieON = true;
    model.addAttribute("editSpecieON", editSpecieON);

    String prefix = "";
    
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      return "redirect:pSave"+prefix+".do";
    }
    else
    {
        List<IspezioneVisivaSpecieOnDTO> organismiNocivi = ispezioneVisivaEJB.findOnByIdIspezione(dto.getIdIspezione());
        //aggiungo l'organismo nocivo provvisorio            
        List<OrganismoNocivoDTO> onDto= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
        if(onDto!=null && onDto.size()>0) {
          for(OrganismoNocivoDTO obj : onDto) {
            IspezioneVisivaSpecieOnDTO onSpec = new IspezioneVisivaSpecieOnDTO(ispezioneVisiva.getIdIspezione(),obj.getIdOrganismoNocivo(), (obj.getPresenza()!=null && obj.getPresenza().equalsIgnoreCase("S"))?"S":"N");
            onSpec.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());          
            ispezioneVisivaEJB.insertVisualSpecieOn(onSpec);            
          }                  
        }
        
        //gestico la tabella dei definitivi
        List<IspezioneVisivaSpecieOnDTO> onDtoDef= (List<IspezioneVisivaSpecieOnDTO>)session.getAttribute("onDtoDef"); 
        if(organismiNocivi != null) {
  	      for(IspezioneVisivaSpecieOnDTO obj :organismiNocivi) {
  	        boolean trovato=false;
  	        for(IspezioneVisivaSpecieOnDTO objDef :onDtoDef) {
  	          if(obj.getIdSpecieOn().equals(objDef.getIdSpecieOn()) ){
  	            trovato=true;
  	          }
  	        }
  	        if(!trovato) {
  	          ispezioneVisivaEJB.removeOn(ispezioneVisiva.getIdIspezione(), Long.valueOf(obj.getIdSpecieOn()).hashCode());
  	        }
  	      }
        }
        
        //inserisco le piante
        List<IspezioneVisivaPiantaDTO> piantaDtoProv= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoProv");
        List<IspezioneVisivaPiantaDTO> piantaDtoDef= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoDef");
        List<IspezioneVisivaPiantaDTO> listaDef= ispezioneVisivaPiantaEJB.findByIdIspezioneVisiva(ispezioneVisiva.getIdIspezione());
        
        if(piantaDtoProv!=null && piantaDtoProv.size()>0) {
          for(IspezioneVisivaPiantaDTO obj : piantaDtoProv) {  
            obj.setIdIspezioneVisivaPianta(null);
            obj.setIdIspezioneVisiva(ispezioneVisiva.getIdIspezione());
            obj.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());  
            ispezioneVisivaPiantaEJB.insert(obj);            
          }                  
        }

        //effettuo prima le remove
        if(listaDef!=null) {
          for(IspezioneVisivaPiantaDTO obj :listaDef) {
            boolean trovato=false;
            for(IspezioneVisivaPiantaDTO objDef : piantaDtoDef) {                         
              if(obj.getIdIspezioneVisivaPianta().equals(objDef.getIdIspezioneVisivaPianta())) {           
                trovato=true;
              }
            }
            if(!trovato) {
              ispezioneVisivaPiantaEJB.removePianta(obj.getIdIspezioneVisiva(),obj.getIdIspezioneVisivaPianta());
            }
           
          }              
        }
  
        //effettuo eventuali modifiche
        if(piantaDtoDef!=null) {
          for(IspezioneVisivaPiantaDTO obj :piantaDtoDef){
            if(obj.getModificato()!=null && obj.getModificato().equals("S")){
              obj.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
              obj.setIdIspezioneVisiva(ispezioneVisiva.getIdIspezione());
              ispezioneVisivaPiantaEJB.update(obj);
            }
          }  
        }

      RilevazioneDTO rilevazione= rilevazioneEJB.findById(dto.getIdRilevazione());
      
      if(ispezioneVisiva.getLatitudine()!=null && !ispezioneVisiva.getLatitudine().equals(dto.getLatitudine()))
        dto.setLatitudine(ispezioneVisiva.getLatitudine());
      if(ispezioneVisiva.getLongitudine()!=null && !ispezioneVisiva.getLongitudine().equals(dto.getLongitudine()))
        dto.setLongitudine(ispezioneVisiva.getLongitudine());
      if(ispezioneVisiva.getNote()!=null && !ispezioneVisiva.getNote().equals(dto.getNote()))
        dto.setNote(ispezioneVisiva.getNote());
      if(ispezioneVisiva.getSuperficie()!=null && !ispezioneVisiva.getSuperficie().equals(dto.getSuperficie()))
        dto.setSuperficie(ispezioneVisiva.getSuperficie());
      if(ispezioneVisiva.getIdSpecieVegetale()!=null && !ispezioneVisiva.getIdSpecieVegetale().equals(dto.getIdSpecieVegetale()))
        dto.setIdSpecieVegetale(ispezioneVisiva.getIdSpecieVegetale());
      if((ispezioneVisiva.getIdAnagrafica()!=null && !ispezioneVisiva.getIdAnagrafica().equals(dto.getIdAnagrafica())))
        dto.setIdAnagrafica(ispezioneVisiva.getIdAnagrafica());
      ispezioneVisivaEJB.update(dto);
     
      if(ispezioneVisiva.getIdTipoArea()>0 && ispezioneVisiva.getIdTipoArea()!=dto.getIdTipoArea()) {
        rilevazione.setIdTipoArea(Long.valueOf(ispezioneVisiva.getIdTipoArea()).intValue());
        rilevazioneEJB.updateFilter(rilevazione);
      }

      setBreadcrumbs(model, request);
      showFlashMessages(model, request);
      
      if (utente == null) {
        logger.info("utente null");
        if (RequestContextUtils.getInputFlashMap(request) != null) {
          logger.info("ripristino l'utente in sessione!");
          session.setAttribute("utenteAbilitazioni", model.asMap().get("utente"));
        }
      }
      model.addAttribute("success","Dettaglio ispezione visiva salvato");
    }      
    return "redirect:search"+prefix+".do";
  }
  
  @RequestMapping(value = {"/gestioneVisual/pSave", "/gestioneVisual/pSaveFromListaMissioni", "/gestioneVisual/pSaveFromMissione"})
  public String pSave(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    IspezioneVisivaDTO dto = (IspezioneVisivaDTO) model.asMap().get("ispezioneVisiva");
    IspezioneVisivaDTO dtoDb = ispezioneVisivaEJB.findById(dto.getIdIspezione());
    Integer idAnInt = (int) dto.getIdAnagrafica();
    Integer idTipoArea = (int) dto.getIdTipoArea();
    if (idAnInt==null || dto.getIdAnagrafica() == 0) {
      dtoDb.setIdAnagrafica(dto.getIdAnagrafica());
    }
    if (dto.getIdSpecieVegetale()==null || dto.getIdSpecieVegetale() == 0) {
      dtoDb.setIdSpecieVegetale(dto.getIdSpecieVegetale());
    }
    if (idTipoArea==null || dto.getIdTipoArea() == 0) {
      dtoDb.setIdTipoArea(dto.getIdTipoArea());
    }

    List<OrganismoNocivoDTO> onDtoProv= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    List<IspezioneVisivaSpecieOnDTO> onDtoDef= (List<IspezioneVisivaSpecieOnDTO>)session.getAttribute("onDtoDef");
    
    
    //carico le piante
    session.setAttribute("piantaDtoProv", null); 
    session.setAttribute("piantaDtoDef",  ispezioneVisivaPiantaEJB.findByIdIspezioneVisiva(dto.getIdIspezione()));
    List<IspezioneVisivaPiantaDTO> piantaDtoProv= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoProv"); 
    List<IspezioneVisivaPiantaDTO> piantaDtoDef= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoDef"); 
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
          dto.setComune(comune);
          model.addAttribute("comune", comune);
        }
      }
    }

    model.addAttribute("ispezioneVisiva", dto);

    List<FotoDTO> listaFoto = ispezioneVisivaEJB.findListFotoByIdIspezioneVisiva(dto.getIdIspezione());
    model.addAttribute("listaFoto", listaFoto);
    
    if (dto.getIdIspezione() != null && dto.getIdIspezione().intValue() > 0) {
      IspezioneVisivaDTO visual = ispezioneVisivaEJB.findById(dto.getIdIspezione());
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
    
    List<IspezioneVisivaSpecieOnDTO> organismiNocivi=new ArrayList<IspezioneVisivaSpecieOnDTO>();
    //gestisco gli on provvisori
    if(onDtoProv!=null && !onDtoProv.isEmpty()) {
      for(OrganismoNocivoDTO on : onDtoProv) {
        IspezioneVisivaSpecieOnDTO newOn = new IspezioneVisivaSpecieOnDTO();
        newOn.setIdIspezioneVisiva(dto.getIdIspezione());
        newOn.setNomeLatino(on.getNomeCompleto());
        newOn.setPresenza(on.getPresenza());
        newOn.setIdSpecieOn(on.getIdOrganismoNocivo());
        newOn.setDescSigla(on.getSigla());
        newOn.setEuro(on.getEuro());
        newOn.setAssociato("N");
        newOn.setDataUltimoAggiornamento(null);
        organismiNocivi.add(newOn);
      }          
    } 
    if(onDtoDef!=null && !onDtoDef.isEmpty()) {
      for(IspezioneVisivaSpecieOnDTO on : onDtoDef) {
         on.setAssociato("S");
         organismiNocivi.add(on);
      }
    }    
    model.addAttribute("listaOrganismi", organismiNocivi);
    
    List<OrganismoNocivoDTO> listOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listOn", listOn);

    Integer idRilevazione  = dtoDb.getIdRilevazione();
    RilevazioneDTO rilevazione = rilevazioneEJB.findById(idRilevazione);
    Integer idMissione = rilevazione.getIdMissione();
    
    List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidiMissione(idMissione);
    AnagraficaDTO ispettoreOperante = (dto.getIdAnagrafica() != null && dto.getIdAnagrafica().intValue() > 0) ? anagraficaEJB.findById(dto.getIdAnagrafica().intValue()) : null;
    if (listaIspettOperante == null)
      listaIspettOperante = new ArrayList<AnagraficaDTO>();
    if (ispettoreOperante != null && !listaIspettOperante.contains(ispettoreOperante)) {
      listaIspettOperante.add(ispettoreOperante);
    }
    model.addAttribute("listaIspettOperante", listaIspettOperante);
        
    List<SpecieVegetaleDTO> listaSpecieVegetali = specieVegetaleEJB.findValidi();
    model.addAttribute("listaSpecieVegetali", listaSpecieVegetali);
    
    //gestisco le piante provv
    List<IspezioneVisivaPiantaDTO> piante=new ArrayList<IspezioneVisivaPiantaDTO>();
    if(piantaDtoProv!=null && !piantaDtoProv.isEmpty()) {
      for(IspezioneVisivaPiantaDTO obj : piantaDtoProv) {
        obj.setAssociatoP("N");
        piante.add(obj);
      }
    }
    if(piantaDtoDef!=null && !piantaDtoDef.isEmpty()) {
      for(IspezioneVisivaPiantaDTO obj  : piantaDtoDef) {
         obj.setAssociatoP("S");
         piante.add(obj);
      }
    }
    //List<IspezioneVisivaPiantaDTO> piantaDTO = ispezioneVisivaPiantaEJB.findByIdIspezioneVisiva(dto.getIdIspezione());
    model.addAttribute("listaPiante", piante);
    
    List<TipoAreaDTO> tipoAree = tipoAreaEJB.findValidi();
    model.addAttribute("listaAree", tipoAree);

    //List<IspezioneVisivaSpecieOnDTO> listaOrganismi = ispezioneVisivaEJB.findOrganismiNociviByVisualCompleto(dto.getIdIspezione());
   // model.addAttribute("listaOrganismi", listaOrganismi);

    //DATI MISSIONE
    Long idMisL = new Long(idMissione);
    MissioneDTO missione = missioneEJB.findById(idMisL);
    model.addAttribute("missione", missione);
    
    
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
    
    boolean editabile = true;
    boolean editSpecieON = false;
    editSpecieON = ruolo.getAmministratore() || ruolo.getFunzionarioBO();
    model.addAttribute("editabile", editabile);
    model.addAttribute("editSpecieON", editSpecieON);
    
    //loadPopupComboDettaglio(model,session);
    return "gestionevisual/dettaglioVisual";
  }
  
  @RequestMapping(value = {"/gestioneVisual/search", "/gestioneVisual/searchFromListaMissioni", "/gestioneVisual/searchFromMissione"})
  public String search(Model model, @ModelAttribute("gestioneVisual") IspezioneVisivaRequest ispezioneVisivaRequest, HttpServletResponse response, HttpSession session, HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    try
    {
      if (ispezioneVisivaRequest.checkNull())
        ispezioneVisivaRequest = (IspezioneVisivaRequest) session.getAttribute("ispezioneVisivaRequest");
      if (ispezioneVisivaRequest == null)
        ispezioneVisivaRequest = new IspezioneVisivaRequest();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  

    //model.addAttribute("list", list);
    //CARICO LE LISTE
    List<Integer> listaTipoArea = ispezioneVisivaRequest.getTipoArea();
    List<Integer> listaSpecie = ispezioneVisivaRequest.getSpecieVegetale();
    List<Integer> listaOrganismo = ispezioneVisivaRequest.getOrganismoNocivo();
    List<Integer> listaIspetAssegnato = ispezioneVisivaRequest.getIspettoreAssegnato();
    List<Integer> listaIspetSecondario = ispezioneVisivaRequest.getIspettoriSecondari();

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
        List<SpecieVegetaleDTO> listaSpecieVeg=specieEJB.findByIdMultipli(idSpecie);
        model.addAttribute("listaSpecie",listaSpecieVeg);
        
      } 
        //organismi
        if(listaOrganismo!=null) {
          String idOn="";
          for(Integer i : listaOrganismo) {
            idOn+=i+",";
          }
          idOn=idOn.substring(0,idOn.length()-1);
          List<OrganismoNocivoDTO> listaOn=onEJB.findByIdMultipli(idOn);
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
        
    UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    model.addAttribute("dataFilter", dataFilter);
        
    model.addAttribute("ispezioneVisivaRequest", ispezioneVisivaRequest);
    session.setAttribute("ispezioneVisivaRequest", ispezioneVisivaRequest);
    session.setAttribute("onDtoProv", null);
    setBreadcrumbs(model, request);
    return "gestionevisual/elencoVisual";
    
    //VECCHIA RICERCA
    
    /*
     * List<IspezioneVisivaDTO> list =
     * ispezioneVisivaEJB.findByFilter(ispezioneVisiva);
     * model.addAttribute("list", list);
     * 
     * List<OrganismoNocivoDTO> listaOn= onEJB.findAll();
     * model.addAttribute("listaOn",listaOn);
     * 
     * setBreadcrumbs(model, request); return "gestionevisual/elencoVisual";
     */
  }

  @RequestMapping(value = "/gestioneVisual/elenco", method = RequestMethod.GET)
  public String elenco(Model model, HttpServletResponse response, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    //IspezioneVisivaRequest ispezioneVisivaRequest = (IspezioneVisivaRequest) session.getAttribute("ispezioneVisivaRequest");
    setBreadcrumbs(model, request);
    return "gestionevisual/elencoVisual";
  }

  @Lazy
  @RequestMapping(value = "/gestioneVisual/filtri")
  public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableVisual") == null || "{}".equals(filtroInSessione.get("tableVisual"))) {
        filtroInit = "";//"{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableVisual");
      } 
       // filtroInit =
           // if(anagrafica.getCognome()!=null &&)
          //  + ",\"cognome\":{\"cnt\":[\""+dto.getCognome().toUpperCase()+"\"]}"
          //  filtroInit += "}";  
       // value='{"annoCampagna":{"cnt":"2016"},"descrizione":{"_values":["Liquidato"]}}'
      filtroInSessione.put("tableVisual", filtroInit);
      return filtroInSessione;
  }
  
  @RequestMapping(value = "/gestioneVisual/showFoto")
  public String showFoto(Model model, @RequestParam(value = "idIspezione") Integer idIspezione, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    FotoDTO foto= ispezioneVisivaEJB.findFotoById(idIspezione);
    model.addAttribute("foto",foto);

    setBreadcrumbs(model, request);
    return "gestionevisual/fotoVisual";
  }
  
  
  @RequestMapping(value="/gestioneVisual/getFoto", method=RequestMethod.GET)
  public ResponseEntity<byte[]> getFoto(@RequestParam(value = "idFoto") Integer idFoto, HttpServletResponse response1) {

      FotoDTO foto;
      ResponseEntity<byte[]> response = null;
      try
      {
        logger.debug("I'm here!");
        foto = ispezioneVisivaEJB.findFotoById(idFoto);
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
  
  @RequestMapping(value = "/rest/ispezione-visiva", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
  public @ResponseBody ResponseEntity<?> creaIspezioneVisiva(@Valid @RequestBody IspezioneVisivaDTO body, HttpServletRequest request) throws MalformedURLException, IOException
  {   

    try {
      String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      String cf = TokenUtils.verifyToken(jwt);
      AnagraficaDTO anagrafica = new AnagraficaDTO();
      anagrafica.setCfAnagraficaEst(cf);
      anagrafica.setActive(true);         // deve prendere l'anagrafica non storicizzata
      List<AnagraficaDTO> ispettori = anagraficaEJB.findByFilter(anagrafica);
      if (ispettori == null || ispettori.size() == 0) {
        logger.debug("Errore Token: codice fiscale ispettore non trovato (creaIspezioneVisiva)");
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", "Errore Token: codice fiscale ispettore non trovato (creaIspezioneVisiva)");
        err.setMessage("Errore Token: codice fiscale ispettore non trovato (creaIspezioneVisiva)");
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
      }

      AnagraficaDTO ispettore = ispettori.get(0);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(body);
      logger.debug(json);
      
      Integer idRilevazione = body.getIdRilevazione();
      RilevazioneDTO rilev = rilevazioneEJB.findById(idRilevazione);
      MissioneDTO missione = missioneEJB.findById(rilev.getIdMissione().longValue());

      IspezioneVisivaDTO ispVisDTO = new IspezioneVisivaDTO();
      IspezioneVisivaPiantaDTO ispVisPiantaDTO = null;
      List<IspezioneVisivaPiantaDTO> ispVisPiantaDTOArray = new ArrayList<IspezioneVisivaPiantaDTO>();

      Coordinate coordinate = null;
      Ubicazione ubicazione = null;

      // ispVis = ObjectMapper.readValue (json, IspezioneVisivaDTO.class);
      //Creo l'oggetto ispezione visiva
      ispVisDTO.setIdIspezione(body.getIdIspezione());
      ispVisDTO.setIdRilevazione(body.getIdRilevazione());
      ispVisDTO.setNumeroAviv(body.getNumeroAviv());
      ispVisDTO.setIdSpecieVegetale(body.getIdSpecieVegetale());
      ispVisDTO.setSuperficie(body.getSuperficie());
      ispVisDTO.setNumeroPiante(body.getNumeroPiante());
      ispVisDTO.setFlagPresenzaOn(body.getFlagPresenzaOn());
      ispVisDTO.setIstatComune(body.getIstatComune());
      ispVisDTO.setLatitudine(body.getLatitudine());
      ispVisDTO.setLongitudine(body.getLongitudine());
      ispVisDTO.setNote(body.getNote());
      ispVisDTO.setFlagIndicatoreIntervento(body.getFlagIndicatoreIntervento());
      ispVisDTO.setRiferimentoUbicazione(body.getRiferimentoUbicazione());
      //ispVisDTO.setArea(body.getArea());
      ispVisDTO.setIdAnagrafica(ispettore.getIdAnagrafica());//.longValue());
      //ispVisDTO.setIdAnagrafica(13);
      //ispVisDTO.setSpecie(body.getSpecie());
      ispVisDTO.setOrganismi(body.getOrganismi());
      ispVisDTO.setIdMissione(body.getIdMissione());
      ispVisDTO.setCuaa(body.getCuaa());
      
      String dataOraInizioStr = null;
      if(StringUtils.isNotBlank(body.getOraInizio()))
    	  dataOraInizioStr = sdf.format(missione.getDataOraInizioMissione()) + " " + body.getOraInizio() + ":00";
      else
    	  dataOraInizioStr = sdf.format(missione.getDataOraInizioMissione()) + " " + "00:00:00";  
      Date dataOraInizio = dtf.parse(dataOraInizioStr);
      ispVisDTO.setDataOraInizio(dataOraInizio);
      Date dataOraFine = null;
      if (StringUtils.isNotBlank(body.getOraFine())) {
        String dataOraFineStr = sdf.format(missione.getDataOraInizioMissione()) + " " + body.getOraFine() + ":00";
        dataOraFine = dtf.parse(dataOraFineStr);
        ispVisDTO.setDataOraFine(dataOraFine);
      }

      //Creo l'oggetto ispezione visiva pianta

      //List<IspezioneVisivaPiantaDTO> listaIspVisPianta = ispezioneVisivaPiantaEJB.findByIdIspezioneVisiva(body.getIdIspezione());

      for(int i=0; i<body.getIspezioni().length; i++) {
        ispVisPiantaDTO = new IspezioneVisivaPiantaDTO();
//        if (body.getIdIspezione() != null) {
//          if(body.getIdIspezione().longValue() != 0) {
//            ispVisPiantaDTO.setIdIspezioneVisiva(body.getIdIspezione());
//            ispVisPiantaDTO.setIdIspezioneVisivaPianta(listaIspVisPianta.get(i).getIdIspezioneVisivaPianta());
//          }
//        }
        ispVisPiantaDTO.setIdSpecieVegetale(body.getIdSpecieVegetale());
        //ispVisPiantaDTO.setNumeroPianta(body.getIspezioni()[i].getNumeroPianta());
        if(body.getIspezioni()[i].getCoordinate() !=null) {
          coordinate = new Coordinate();  // Modificato il 28/04/2021 (S.D) - bug rilevato testanto l'app
          coordinate.setLatitudine(body.getIspezioni()[i].getCoordinate().getLatitudine());
          coordinate.setLongitudine(body.getIspezioni()[i].getCoordinate().getLongitudine());
        }
        ispVisPiantaDTO.setCoordinate(coordinate);
        ubicazione = new Ubicazione();    // Modificato il 28/04/2021 (S.D) - bug rilevato testanto l'app
        ubicazione.setNome(body.getIspezioni()[i].getUbicazione().getNome());
        ubicazione.setCognome(body.getIspezioni()[i].getUbicazione().getCognome());
        ubicazione.setIndirizzo(body.getIspezioni()[i].getUbicazione().getIndirizzo());
        ubicazione.setTelefono(body.getIspezioni()[i].getUbicazione().getTelefono());
        ubicazione.setEmail(body.getIspezioni()[i].getUbicazione().getEmail());
        ubicazione.setNumero(body.getIspezioni()[i].getUbicazione().getNumero());
        ispVisPiantaDTO.setNumeroPianta(body.getIspezioni()[i].getQuantita());
        ispVisPiantaDTO.setUbicazione(ubicazione);
        ispVisPiantaDTO.setPositivita(body.getIspezioni()[i].getPositivita());
        ispVisPiantaDTO.setDiametro(body.getIspezioni()[i].getDiametro());
        ispVisPiantaDTO.setFlagTreeClimberIspezione(body.getIspezioni()[i].getFlagTreeClimberIspezione());
        ispVisPiantaDTO.setFlagTreeClimberTaglio(body.getIspezioni()[i].getFlagTreeClimberTaglio());
        ispVisPiantaDTO.setNote1(body.getIspezioni()[i].getNote1());
        ispVisPiantaDTO.setNote2(body.getIspezioni()[i].getNote2());
        ispVisPiantaDTO.setNote3(body.getIspezioni()[i].getNote3());
        //ispVisPiantaDTO.setIdIspezioneVisiva(body.getIspezioni()[i].getIdIspezioneVisiva());
        try {
          UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
          ispVisPiantaDTO.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
        } catch (Exception e) {
          ispVisPiantaDTO.setExtIdUtenteAggiornamento(0L);
        }
        ispVisPiantaDTOArray.add(ispVisPiantaDTO);
      }

      try {
        UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
        ispVisDTO.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
      } catch (Exception e) {
        ispVisDTO.setExtIdUtenteAggiornamento(0L);
      }

      Integer idIspezione = null;
      try
      {
        if (body.getIdIspezione() == null || body.getIdIspezione().intValue() == 0) {
          idIspezione = ispezioneVisivaEJB.insert(ispVisDTO);

          ispVisDTO.setIdIspezione(idIspezione);
          for(int j=0; j<body.getOrganismi().length; j++) {
            //modificato per il booleano trovato
            ispVisDTO.setIdSpecieOn(body.getOrganismi()[j].getIdSpecieOn());
            String trovato=body.getOrganismi()[j].getFlagTrovato();  
            //barbara
            if(trovato.equals("true")) 
              ispVisDTO.setFlagTrovato("S");
            else 
              ispVisDTO.setFlagTrovato("N");
           ispezioneVisivaEJB.insertSpecieOn(ispVisDTO);
          }

         // ispVisPiantaDTO.setIdIspezioneVisiva(idIspezione);

          for(int n=0; n<ispVisPiantaDTOArray.size(); n++) {
            ispVisPiantaDTOArray.get(n).setIdIspezioneVisiva(idIspezione);
            ispezioneVisivaPiantaEJB.insert(ispVisPiantaDTOArray.get(n));
          }

          body.setIdIspezione(idIspezione);
          
          Long i = gpsFotoEJB.selectFotoVisual(Long.valueOf(idIspezione));
          if (i>0)
            body.setFoto(true);
          else
            body.setFoto(false);
          
          body.setDataOraInizio(dataOraInizio);
          if(dataOraFine != null)
            body.setDataOraFine(dataOraFine);
        }
        else {
          ispezioneVisivaEJB.update(ispVisDTO);
          body.setFoto(null);
          ispezioneVisivaPiantaEJB.remove(body.getIdIspezione());
          //ispVisPiantaDTO.setIdIspezioneVisiva(body.getIdIspezione());
          for(int m=0; m<ispVisPiantaDTOArray.size(); m++) {
            ispVisPiantaDTOArray.get(m).setIdIspezioneVisiva(body.getIdIspezione());
            ispezioneVisivaPiantaEJB.insert(ispVisPiantaDTOArray.get(m));
          }
          for(int j=0; j<body.getOrganismi().length; j++) {
            //ispVisDTO.setIdSpecieOn(body.getOrganismi()[j]);
            //modificato per il booleano trovato
           ispVisDTO.setIdSpecieOn(body.getOrganismi()[j].getIdSpecieOn());
            //barbara
            String trovato=body.getOrganismi()[j].getFlagTrovato();
            if(trovato.equals("true")) 
              ispVisDTO.setFlagTrovato("S");
            else 
              ispVisDTO.setFlagTrovato("N");         
            ispezioneVisivaEJB.updateSpecieOn(ispVisDTO);
          }
          Long i = gpsFotoEJB.selectFotoVisual(Long.valueOf(ispVisDTO.getIdIspezione()));
          if (i>0)
            body.setFoto(true);
          else
            body.setFoto(false);
          
          body.setDataOraInizio(dataOraInizio);
          if(dataOraFine != null)
            body.setDataOraFine(dataOraFine);
        }
      }
      catch (InternalUnexpectedException e)
      {
        e.printStackTrace();
        logger.debug("Errore nel metodo creaIspezioneVisiva in fase di insert: " + e.getMessage());
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", e.getMessage());
        err.setMessage("Errore nel metodo creaIspezioneVisiva durante la registrazione: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    catch(Throwable e) {
      logger.debug("Errore nel metodo creaIspezioneVisiva durante la registrazione: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo creaIspezioneVisiva durante la registrazione: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<IspezioneVisivaDTO>(body, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/rest/visual/{idIspezione}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<?> deleteVisual(@PathVariable (value = "idIspezione") Integer idIspezione, HttpServletRequest request)
  {
    try {
      //String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      //verifyToken(jwt);
      logger.debug("id_rilevazione: " + idIspezione);
      IspezioneVisivaDTO visualDTO = new IspezioneVisivaDTO();
      visualDTO.setIdIspezione(idIspezione);
      List<IspezioneVisivaDTO> list = ispezioneVisivaEJB.findByFilter(visualDTO);
      if (list == null || list.size() == 0) {
        throw new Exception("Ispezioni con id " + idIspezione + " non trovata");
      }
      
      ispezioneVisivaEJB.remove(idIspezione);
      
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
  
  private void loadPopupCombo(Model model, HttpSession session) throws InternalUnexpectedException
  {
    IspezioneVisivaRequest ispezioneVisivaRequest = (IspezioneVisivaRequest) session.getAttribute("ispezioneVisivaRequest");
    
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
      all_specieVegetale = specieEJB.findAll();
    } else {
      all_specieVegetale = specieEJB.findValidi();
    }
    model.addAttribute("all_specieVegetale", all_specieVegetale);
    // Organismo nocivo
    List<OrganismoNocivoDTO> all_organismoNocivo = null;
    if (session.getAttribute("checkboxAllOrganismiNocivi") != null && ((String) session.getAttribute("checkboxAllOrganismiNocivi")).equals("true")) {
      all_organismoNocivo = onEJB.findAll();
    } else {
      all_organismoNocivo = onEJB.findValidi();
    }
    model.addAttribute("all_organismoNocivo", all_organismoNocivo);
    //

    List<TipoAreaDTO> tipoAree = new ArrayList<>();
    model.addAttribute("tipoAree", tipoAree);
    
    if (ispezioneVisivaRequest != null) {
      // Tipo aree
      if (ispezioneVisivaRequest.getTipoArea() != null) {
        String idArea="";
        for(Integer i : ispezioneVisivaRequest.getTipoArea()) {
          idArea+=i+",";
        }
        idArea=idArea.substring(0,idArea.length()-1);
        List<TipoAreaDTO> listaAree = tipoAreaEJB.findByIdMultipli(idArea);
        model.addAttribute("tipoAree", listaAree);   
      }
      // Specie vegetali
      if (ispezioneVisivaRequest.getSpecieVegetale() != null) {
        String idSpecie="";
        for (Integer i : ispezioneVisivaRequest.getSpecieVegetale()) {
          idSpecie+=i+",";
        }
        idSpecie=idSpecie.substring(0,idSpecie.length()-1);
        List<SpecieVegetaleDTO> listaSpecieVeg = specieEJB.findByIdMultipli(idSpecie);
        model.addAttribute("listaSpecieVegetali", listaSpecieVeg);     
      }
      // Organismi nocivi
      if (ispezioneVisivaRequest.getOrganismoNocivo() != null) {
          String idOn="";
          for (Integer i : ispezioneVisivaRequest.getOrganismoNocivo()) {
            idOn+=i+",";
          }
          idOn=idOn.substring(0,idOn.length()-1);
          List<OrganismoNocivoDTO> listaOn = onEJB.findByIdMultipli(idOn);
          model.addAttribute("listaON", listaOn);
       }
      // ispettori assegnati
      if (ispezioneVisivaRequest.getIspettoreAssegnato() != null && ispezioneVisivaRequest.getIspettoreAssegnato().size() > 0)
      {
          String idIsSec="";
          for(Integer i : ispezioneVisivaRequest.getIspettoreAssegnato()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettAssegnati = anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettori",listaIspettAssegnati);          
      }
      // ispettori secondari
      if (ispezioneVisivaRequest.getIspettoriSecondari() != null && ispezioneVisivaRequest.getIspettoriSecondari().size() > 0)
      {
          String idIsSec="";
          for(Integer i : ispezioneVisivaRequest.getIspettoriSecondari()) {
            idIsSec+=i+",";
          }
          idIsSec=idIsSec.substring(0,idIsSec.length()-1);
          List<AnagraficaDTO> listaIspettSecondari=anagraficaEJB.findByIdMultipli(idIsSec);
          model.addAttribute("listaIspettSecondari",listaIspettSecondari);          
      }
      // comune
      if (StringUtils.isNotBlank(ispezioneVisivaRequest.getIstatComune())) {
        ComuneDTO comuneDTO = quadroEJB.getComune(ispezioneVisivaRequest.getIstatComune());
        if (comuneDTO != null)
        {
          model.addAttribute("provSceltaComune", comuneDTO.getSiglaProvincia());
          model.addAttribute("comuneSceltaComune", comuneDTO.getDescrizioneComune());
        }
      }
    }
  }
  
  @RequestMapping(value = "/gestioneVisual/clearFilter")
  public String clearFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    setBreadcrumbs(model, request);
    session.removeAttribute("ispezioneVisivaRequest");
    return showFilter(model, session, request, response);
  }

  @Lazy
  @RequestMapping(value = "/gestioneVisual/gestioneVisualExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    IspezioneVisivaRequest ispezioneVisivaRequest = (IspezioneVisivaRequest) session.getAttribute("ispezioneVisivaRequest");
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    List<IspezioneVisivaDTO> elenco = ispezioneVisivaEJB.findByFilterFromRequest(ispezioneVisivaRequest,dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());

 //   List<IspezioneVisivaDTO> elenco = ispezioneVisivaEJB.findByFilterFromRequest(ispezioneVisivaRequest);
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"visual.xls\"");
    return new ModelAndView("excelGestioneVisualView", "elenco", elenco);
  }
  
  @RequestMapping(value = "/gestioneVisual/getVisualJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getVisualJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    IspezioneVisivaRequest ispezioneVisivaRequest = (IspezioneVisivaRequest) session.getAttribute("ispezioneVisivaRequest");
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    DataFilter dataFilter = getFiltroDati(utente);
    List<IspezioneVisivaDTO> lista = ispezioneVisivaEJB.findByFilterFromRequest(ispezioneVisivaRequest,dataFilter.getIdAnagrafica(), dataFilter.getIdEnte());
    
    if (lista == null) {
      lista = new ArrayList<>();
    }

    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }

  @RequestMapping(value = "/gestioneVisual/editPianteIspezione")
  public String editPianteIspezione(Model model, @RequestParam(value = "id") Integer id, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    IspezioneVisivaPiantaDTO pianta= ispezioneVisivaPiantaEJB.findByIdIspVisPianta(id);
    model.addAttribute("pianta",pianta);

    List<PositivitaDTO> listaPositivita = ispezioneVisivaPiantaEJB.findPositivita();
    model.addAttribute("listaPositivita",listaPositivita);
    
    List<DiametroDTO> listaDiametro = ispezioneVisivaPiantaEJB.findDiametro();
    model.addAttribute("listaDiametro",listaDiametro);
    
    IspezioneVisivaDTO visual = ispezioneVisivaEJB.findById(pianta.getIdIspezioneVisiva());
    
    boolean emergenza = false;
    RilevazioneDTO rilev = rilevazioneEJB.findById(visual.getIdRilevazione());
    String flagEmergenza = rilev.getFlagEmergenza();
    if(flagEmergenza.equals("S"))
    	emergenza = true;
    model.addAttribute("emergenza", emergenza);

    setBreadcrumbs(model, request);
    return "gestionevisual/dettaglioPianta";
  }

  @RequestMapping(value = {"/gestioneVisual/edit", "gestioneVisual/show", "gestioneVisual/editFromListaMissioni", "gestioneVisual/showFromListaMissioni", "gestioneVisual/editFromMissione", "gestioneVisual/showFromMissione"})
  public String edit(Model model, @RequestParam(value = "idIspezione") String idIspezione,
      @RequestParam(value = "from") String from,
      @ModelAttribute("ispezioneVisiva") IspezioneVisivaDTO ispezioneVisiva,
      HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    IspezioneVisivaDTO dto = ispezioneVisivaEJB.findById(Integer.decode(idIspezione));
    
    if(from.equals("addOn") || from.equals("deleteOnProv") ||  from.equals("deleteOnDef") || from.equals("deletePianteProv") 
    		||  from.equals("deletePianteDef") || from.equals("add") || from.equals("addPianta")){
    	dto.setLatitudine(ispezioneVisiva.getLatitudine());
        dto.setLongitudine(ispezioneVisiva.getLongitudine());
        dto.setNote(ispezioneVisiva.getNote());
        dto.setIdTipoArea(ispezioneVisiva.getIdTipoArea());
        dto.setIdSpecieVegetale(ispezioneVisiva.getIdSpecieVegetale());
        dto.setIdAnagrafica(ispezioneVisiva.getIdAnagrafica());
        dto.setSuperficie(ispezioneVisiva.getSuperficie());
      }
    
    //carico gli on provvisori
    if(from!=null && (from.equals("elenco") || from.equals("annulla") || from.equals("save")) ) {
      session.setAttribute("onDtoProv", null); 
      List<IspezioneVisivaSpecieOnDTO> onDtoFromQuery = ispezioneVisivaEJB.findOnByIdIspezione(Integer.decode(idIspezione));
      session.setAttribute("onDtoDef", onDtoFromQuery); 
    }

    //carico le piante
    if(from!=null && (from.equals("elenco") || from.equals("annulla") || from.equals("save")) ) {
      session.setAttribute("piantaDtoProv", null); 
      session.setAttribute("piantaDtoDef",  ispezioneVisivaPiantaEJB.findByIdIspezioneVisiva(Integer.decode(idIspezione)));
    }
 
    List<OrganismoNocivoDTO> onDtoProv= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    List<IspezioneVisivaSpecieOnDTO> onDtoDef= (List<IspezioneVisivaSpecieOnDTO>)session.getAttribute("onDtoDef"); 
     //carico gli on provvisori
    List<IspezioneVisivaPiantaDTO> piantaDtoProv= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoProv"); 
    List<IspezioneVisivaPiantaDTO> piantaDtoDef= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoDef"); 
    
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
    model.addAttribute("ispezioneVisiva", dto);
   
    List<IspezioneVisivaSpecieOnDTO> organismiNocivi=new ArrayList<IspezioneVisivaSpecieOnDTO>();
    //gestisco gli on provvisori
    if(onDtoProv!=null && !onDtoProv.isEmpty()) {
      for(OrganismoNocivoDTO on : onDtoProv) {
        IspezioneVisivaSpecieOnDTO newOn = new IspezioneVisivaSpecieOnDTO();
        newOn.setIdIspezioneVisiva(Integer.decode(idIspezione));
        newOn.setNomeLatino(on.getNomeCompleto());
        newOn.setPresenza(on.getPresenza());
        newOn.setIdSpecieOn(on.getIdOrganismoNocivo());
        newOn.setDescSigla(on.getSigla());
        newOn.setSigla(on.getSigla());
        newOn.setEuro(on.getEuro());
        newOn.setAssociato("N");
        newOn.setDataUltimoAggiornamento(null);
        organismiNocivi.add(newOn);
      }          
    } 
    if(onDtoDef!=null && !onDtoDef.isEmpty()) {
      for(IspezioneVisivaSpecieOnDTO on : onDtoDef) {
         on.setAssociato("S");
         organismiNocivi.add(on);
      }
    }
    model.addAttribute("listaOrganismi", organismiNocivi);
    
    //gestisco le piante provv
    List<IspezioneVisivaPiantaDTO> piante=new ArrayList<IspezioneVisivaPiantaDTO>();
    if(piantaDtoProv!=null && !piantaDtoProv.isEmpty()) {
      for(IspezioneVisivaPiantaDTO obj : piantaDtoProv) {
        obj.setAssociatoP("N");
        piante.add(obj);
      }
    }
    if(piantaDtoDef!=null && !piantaDtoDef.isEmpty()) {
      for(IspezioneVisivaPiantaDTO obj  : piantaDtoDef) {
         obj.setAssociatoP("S");
         piante.add(obj);
      }
    }

  // List<IspezioneVisivaPiantaDTO> piantaDTO = ispezioneVisivaPiantaEJB.findByIdIspezioneVisiva(Integer.decode(idIspezione));
    model.addAttribute("listaPiante", piante);
    
    List<AnagraficaDTO> ispettoriAggiunti = missioneEJB.getIspettoriAggiunti(dto.getIdMissione().longValue());
    model.addAttribute("ispettoriAggiunti", ispettoriAggiunti);   
  
    
    List<OrganismoNocivoDTO> listOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listOn", listOn);
 
    List<FotoDTO> listaFoto =ispezioneVisivaEJB.findListFotoByIdIspezioneVisiva(Integer.decode(idIspezione));
    model.addAttribute("listaFoto", listaFoto);
    
    List<TipoAreaDTO> listaAree=tipoAreaEJB.findValidi();
    model.addAttribute("listaAree", listaAree);
    
    List<SpecieVegetaleDTO> listaSpecieVegetali = specieVegetaleEJB.findValidi();
    model.addAttribute("listaSpecieVegetali", listaSpecieVegetali);
    
    List<SceltaEsclusiva> listaIspezione = this.getSelectScelta();
    model.addAttribute("listaIspezione", listaIspezione);
       
    List<SceltaEsclusiva> listaTaglio = this.getSelectScelta();
    model.addAttribute("listaTaglio", listaTaglio);
    
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

    //DATI MISSIONE
    Integer idRilevazione  = dto.getIdRilevazione();
    RilevazioneDTO rilevazione = rilevazioneEJB.findById(idRilevazione);
    Integer idMissione = rilevazione.getIdMissione();
    Long idMisL = new Long(idMissione);
    MissioneDTO missione = missioneEJB.findById(idMisL);
    model.addAttribute("missione", missione);
    
    List<AnagraficaDTO> listaIspettori = anagraficaEJB.findByFilter(new AnagraficaDTO(true));

    model.addAttribute("listaIspettori", listaIspettori);
    //model.addAttribute("editabile", editabile);
 
    List<AnagraficaDTO> listaIspettOperante = anagraficaEJB.findValidiMissione(idMissione);
    AnagraficaDTO ispettoreOperante = (dto.getIdAnagrafica() != null && dto.getIdAnagrafica().intValue() > 0) ? anagraficaEJB.findById(dto.getIdAnagrafica().intValue()) : null;
    if (listaIspettOperante == null)
      listaIspettOperante = new ArrayList<AnagraficaDTO>();
    if (ispettoreOperante != null && !listaIspettOperante.contains(ispettoreOperante)) {
      listaIspettOperante.add(ispettoreOperante);
    }

//    if (listaIspettOperante == null)
//      listaIspettOperante = new ArrayList<AnagraficaDTO>();
    model.addAttribute("listaIspettOperante", listaIspettOperante);
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
    boolean editSpecieON = false;
    int idStatoVerbale = missioneEJB.getIdStatoVerbaleByIdMissione(idMisL);
    if (idStatoVerbale < 2 && request.getServletPath().indexOf("/edit") > -1) {   // lo stato verbale se presente deve essere max 1 (bozza) per consentire modifiche
      editabile = IuffiUtils.PAPUASERV.isMacroCUAbilitato(utente, "GESTIONE_VISUAL") && utente.isReadWrite();
      editSpecieON = ruolo.getAmministratore() || ruolo.getFunzionarioBO();
    }
    model.addAttribute("editabile", editabile);
    model.addAttribute("editSpecieON", editSpecieON);
    
    boolean emergenza = false;
    //RilevazioneDTO rilev = rilevazioneEJB.findById(dto.getIdRilevazione());
    String flagEmergenza = rilevazione.getFlagEmergenza();
    if(flagEmergenza.equals("S"))
    	emergenza = true;
    model.addAttribute("emergenza", emergenza);

    List<PositivitaDTO> listaPositivita = ispezioneVisivaPiantaEJB.findPositivita();
    model.addAttribute("listaPositivita",listaPositivita);
   
    List<DiametroDTO> listaDiametro = ispezioneVisivaPiantaEJB.findDiametro();
    model.addAttribute("listaDiametro",listaDiametro);
    
    return "gestionevisual/dettaglioVisual";
  }
  
  @RequestMapping(value = "/gestioneVisual/searchComuni", produces = "application/json", method = RequestMethod.POST)
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
  
  @RequestMapping(value = "/gestioneVisual/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      return "gestionevisual/confermaElimina";
  }

  @RequestMapping(value = "/gestioneVisual/deleteOn")
  public String canDeleteOn(Model model, @RequestParam(value = "idIspezione") String idIspezione, @RequestParam(value = "idOrganismoNocivo") String idOrganismoNocivo, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("idIspezione",idIspezione);
      model.addAttribute("idOrganismoNocivo",idOrganismoNocivo);
      model.addAttribute("table","tableOn");
      return "gestionevisual/confermaEliminaOn";
  }
  
  @RequestMapping(value = "/gestioneVisual/deletePianta")
  public String canDeletePiante(Model model, @RequestParam(value = "idIspezione") String idIspezione, @RequestParam(value = "idIspezioneVisivaPianta") String idIspezioneVisivaPianta, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("idIspezione",idIspezione);
      model.addAttribute("idIspezioneVisivaPianta",idIspezioneVisivaPianta);
      model.addAttribute("table","piante");
      return "gestionevisual/confermaEliminaPianta";
  }
  
  @RequestMapping(value = "/gestioneVisual/remove")
  public RedirectView remove(Model model, @RequestParam(value = "id") String id, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
        
    try {
      ispezioneVisivaEJB.remove(Integer.decode(id));
    }catch (InternalUnexpectedException e) {
      boolean isFound = e.getCause().getMessage().indexOf("ORA-02292") !=-1? true: false; //true
      if(isFound) {
       // campioneEJB.updateDataFineValidita(Integer.decode(id));
      }
    }      
    
   // List<gest> list = campioneEJB.findAll();
    //attributes.addFlashAttribute("list", list);
    return new RedirectView("search.do", true);
  }
  
  @RequestMapping(value = "/gestioneVisual/removeOrganismoNocivo", method = RequestMethod.GET)
  public String removeOrganismoNocivo(Model model, @ModelAttribute("ispezioneSpecOn") IspezioneVisivaSpecieOnDTO ispezioneSpecOn, @RequestParam(value = "idIspezione") Integer idIspezione, @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    if (idOrganismoNocivo == null || idOrganismoNocivo.intValue() == 0) {
      errors.addError("Organismo nocivo", "Campo Obbligatorio");
    }
    if (idIspezione == null || idIspezione.intValue() == 0) {
      errors.addError("Ispezione Visiva", "Campo Obbligatorio");
    }
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
    } else {
        try {
          ispezioneVisivaEJB.removeOn(idIspezione, idOrganismoNocivo);
          Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
          session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
          
//          List<OrganismoNocivoDTO> listOnCompleta= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
//          List<OrganismoNocivoDTO> listOn = new ArrayList<OrganismoNocivoDTO>();
//          OrganismoNocivoDTO onDto = new OrganismoNocivoDTO();
//          onDto = organismoNocivoEJB.findById(idOrganismoNocivo);
//          for(OrganismoNocivoDTO on : listOnCompleta) {
//        	  if(!on.getNomeLatino().equals(onDto.getNomeLatino()))
//        		  listOn.add(on);
//          }
//          session.setAttribute("onDtoProv", listOn);         
          
        }
        catch (Exception e) {
          logger.error("Errore nella aggiunta organismo nocivo al campione: " + e.getMessage());
          model.addAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("success", null);
          redirectAttributes.addFlashAttribute("model", model);
        }
    }
    return "redirect:edit.do?idIspezione=" + idIspezione + "&editabile=true&from=deleteOn";
  }
  
  @RequestMapping(value = "/gestioneVisual/removeOrganismoNocivoProv", method = RequestMethod.GET)
  public String removeOrganismoNocivoProv(Model model, @RequestParam(value = "idIspezione") Integer idIspezione, 
      @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    
    List<OrganismoNocivoDTO> listOn= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    for(OrganismoNocivoDTO obj : listOn) {
     // System.out.println(obj.getIdOrganismoNocivo());
      if(obj.getIdOrganismoNocivo().equals(idOrganismoNocivo)) {
        listOn.remove(obj);  
        session.setAttribute("onDtoProv",listOn); 
        return "redirect:edit.do?idIspezione=" + idIspezione + "&editabile=true&from=deleteOn";        
       }      
    }
    session.setAttribute("onDtoProv",listOn); 
    return "redirect:edit.do?idIspezione=" + idIspezione + "&editabile=true&from=deleteOn";
  }

  @RequestMapping(value = "/gestioneVisual/removePianta", method = RequestMethod.GET)
  public String removePianta(Model model, @ModelAttribute("ispezionePianta") IspezioneVisivaPiantaDTO ispezioneSpecOn, @RequestParam(value = "idIspezione") Integer idIspezione, @RequestParam(value = "idIspezioneVisivaPianta") Integer idIspezioneVisivaPianta, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    if (idIspezioneVisivaPianta == null || idIspezioneVisivaPianta.intValue() == 0) {
      errors.addError("Pianta", "Campo Obbligatorio");
    }
    if (idIspezione == null || idIspezione.intValue() == 0) {
      errors.addError("Ispezione Visiva", "Campo Obbligatorio");
    }
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
    } else {
        try {
          ispezioneVisivaPiantaEJB.removePianta(idIspezione, idIspezioneVisivaPianta);
          Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
          session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
        }
        catch (Exception e) {
          model.addAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("success", null);
          redirectAttributes.addFlashAttribute("model", model);
        }
    }
    return "redirect:edit.do?idIspezione=" + idIspezione + "&editabile=true&from=deletePianta";
  }
  
  @RequestMapping(value = "/gestioneVisual/savePianta")
  public String savePianta(Model model, @RequestParam(value = "idIspezione") String idIspezione, @RequestParam(value = "idSpecieVegetale") Integer idSpecieVegetale, 
		  @RequestParam(value = "flagTreeClimberIspezione") String flagTreeClimberIspezione, @RequestParam(value = "flagTreeClimberTaglio") String flagTreeClimberTaglio, @ModelAttribute("ispezioneVisivaPianta") IspezioneVisivaPiantaDTO ispezioneVisivaPianta, HttpSession session, HttpServletRequest request,
      HttpServletResponse response, RedirectAttributes redirectAttributes,BindingResult bindingResult) throws InternalUnexpectedException
  {
 
	  
	  
	  	UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
	    
	  	IspezioneVisivaPiantaDTO ispVisPianta = new IspezioneVisivaPiantaDTO();

	    //Coordinate coordinate = new Coordinate();
	    //Ubicazione ubicazione = new Ubicazione();
	    //if(ispezioneVisivaPianta.getLat() !=null) 
	    //    coordinate.setLatitudine(ispezioneVisivaPianta.getLat());
	    //if(ispezioneVisivaPianta.getLon() != null)
	    //	coordinate.setLongitudine(ispezioneVisivaPianta.getLon());
	    ispVisPianta.setCoordinate(ispezioneVisivaPianta.getCoordinate()); 
	    ispVisPianta.setUbicazione(ispezioneVisivaPianta.getUbicazione());
	    if(ispezioneVisivaPianta.getNumeroPianta() != null)
	    	ispVisPianta.setNumeroPianta(ispezioneVisivaPianta.getNumeroPianta());
	    ispVisPianta.setIdIspezioneVisivaPianta(ispezioneVisivaPianta.getIdIspezioneVisivaPianta());
	    ispVisPianta.setIdSpecieVegetale(idSpecieVegetale);
	    //ispVisPianta.setLat(ispezioneVisivaPianta.getLatitudine());
	    //ispVisPianta.setLon(ispezioneVisivaPianta.getLongitudine());
	    ispVisPianta.setDescPositivita(ispezioneVisivaPianta.getDescPositivita());
	    ispVisPianta.setDescDiametro(ispezioneVisivaPianta.getDescDiametro());
	    ispVisPianta.setNote1(ispezioneVisivaPianta.getNote1());
	    ispVisPianta.setNote2(ispezioneVisivaPianta.getNote2());
	    ispVisPianta.setNote3(ispezioneVisivaPianta.getNote3());
	    ispVisPianta.setFlagTreeClimberIspezione(ispezioneVisivaPianta.getFlagTreeClimberIspezione());
	    ispVisPianta.setFlagTreeClimberTaglio(ispezioneVisivaPianta.getFlagTreeClimberTaglio());
	    try {
	    	ispVisPianta.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
	      } catch (Exception e) {
	    	  ispVisPianta.setExtIdUtenteAggiornamento(0L);
	      }
	    ispezioneVisivaPiantaEJB.update(ispVisPianta);
	    
	    return "redirect:edit.do?idIspezione="+idIspezione+"&editabile=true&from=editPianteIspezione";
    }
  
  
  @RequestMapping(value = "/gestioneVisual/pSavePianta")
  public String pSavePianta(Model model,
      HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
	    if (RequestContextUtils.getInputFlashMap(request) != null) {
	        model.addAttribute("list", model.asMap().get("list"));
	      }
	    
	      IspezioneVisivaPiantaDTO dto = (IspezioneVisivaPiantaDTO) model.asMap().get("ispezioneVisivaPianta");
	      IspezioneVisivaPiantaDTO pianta= ispezioneVisivaPiantaEJB.findById(dto.getIdIspezioneVisivaPianta());
	      model.addAttribute("pianta",pianta);

	      setBreadcrumbs(model, request);
	      return "gestionevisual/dettaglioPianta";
  }
  
  @RequestMapping(value = {"/gestioneVisual/addOrganismoNocivo", "/gestioneVisual/addOrganismoNocivoFromListaMissioni", "/gestioneVisual/addOrganismoNocivoFromMissione"}, method = RequestMethod.POST)
  public String addOrganismoNocivo(Model model, @ModelAttribute("ispezioneVisSpecOn") IspezioneVisivaSpecieOnDTO ispezioneVisSpecOn, 
	  @ModelAttribute("ispezioneVisiva") IspezioneVisivaDTO ispezioneVisiva,
      @RequestParam(value = "idIspezione") Integer idIspezione, 
      @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, 
      @RequestParam(value = "presenza") String presenza, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    List<OrganismoNocivoDTO> listOn= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    if(listOn==null || listOn.isEmpty()) {
      listOn= new ArrayList<OrganismoNocivoDTO>();
    }
    OrganismoNocivoDTO onDto = new OrganismoNocivoDTO();
    onDto = organismoNocivoEJB.findById(idOrganismoNocivo);

    onDto.setPresenza(presenza);
    listOn.add(onDto);
    session.setAttribute("onDtoProv", listOn);  
    redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
    model.addAttribute("ispezioneVisiva", ispezioneVisiva);

    String prefix = "";
    
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=add";
  }
  
  @RequestMapping(value = {"/gestioneVisual/removeOrganismoNocivoProv", "/gestioneVisual/removeOrganismoNocivoProvFromListaMissioni", "/gestioneVisual/removeOrganismoNocivoProvFromMissione"}, method = RequestMethod.POST)
  public String removeOrganismoNocivoProv(Model model,  @RequestParam(value = "idIspezione") Integer idIspezione, 
      @ModelAttribute("ispezioneVisiva") IspezioneVisivaDTO ispezioneVisiva,
      @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    List<OrganismoNocivoDTO> listOn= (List<OrganismoNocivoDTO>)session.getAttribute("onDtoProv"); 
    redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
    model.addAttribute("ispezioneVisiva", ispezioneVisiva);
    
    String prefix = "";
    
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
        return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=deleteOnProv";        
       }      
    }
    session.setAttribute("onDtoProv",listOn); 
    return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&from=deleteOnProv";
  }

  @RequestMapping(value = {"/gestioneVisual/removeOrganismoNocivoDef", "/gestioneVisual/removeOrganismoNocivoDefFromListaMissioni", "/gestioneVisual/removeOrganismoNocivoDefFromMissione"}, method = RequestMethod.POST)
  public String removeOrganismoNocivoDef(Model model,   @RequestParam(value = "idIspezione") Integer idIspezione, 
      @ModelAttribute("ispezioneVisiva") IspezioneVisivaDTO ispezioneVisiva,
      @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    Errors error = new Errors();  
	  
    redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
    model.addAttribute("ispezioneVisiva", ispezioneVisiva);
    List<IspezioneVisivaSpecieOnDTO> organismiNocivi = ispezioneVisivaEJB.findOnByIdIspezione(idIspezione);  
    
    List<IspezioneVisivaSpecieOnDTO> onDtoDef= (List<IspezioneVisivaSpecieOnDTO>)session.getAttribute("onDtoDef"); 
    if(onDtoDef!=null && !onDtoDef.isEmpty())
      organismiNocivi=onDtoDef;
    
    if (organismiNocivi.size()==1 || onDtoDef.size()==1) {
	      error.addError("Pianta", "Impossibile cancellare. Deve essere presente almeno un organismo nocivo");
    }

    String prefix = "";
    
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    if (!error.isEmpty())
    {
      model.addAttribute("error", error);
      redirectAttributes.addFlashAttribute("error", "Impossibile cancellare. Deve essere presente almeno un organismo nocivo");
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
    } else {
    	
        for(IspezioneVisivaSpecieOnDTO obj : organismiNocivi) {
            // System.out.println(obj.getIdOrganismoNocivo());
             if(obj.getIdSpecieOn().equals(idOrganismoNocivo)) {
                    // obj.setAssociato("N"); 
               organismiNocivi.remove(obj);  
               session.setAttribute("onDtoDef",organismiNocivi); 
               return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=deleteOnDef";        
              }      
           }
    }
    session.setAttribute("onDtoDef",organismiNocivi); 
    return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&from=deleteOnDef";
  }

  @RequestMapping(value = {"/gestioneVisual/addPianta", "/gestioneVisual/addPiantaFromListaMissioni", "/gestioneVisual/addPiantaFromMissione"}, method = RequestMethod.POST)
  public String addPianta(Model model,@ModelAttribute("pianta") IspezioneVisivaPiantaDTO pianta,
      @ModelAttribute("ispezioneVisiva") IspezioneVisivaDTO ispezioneVisiva, @ModelAttribute("on") OrganismoNocivoDTO on,
      @RequestParam(value = "idIspezione") Integer idIspezione, 
      HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    List<IspezioneVisivaPiantaDTO> listPianta= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoProv"); 
    if(listPianta==null || listPianta.isEmpty()) {
      listPianta= new ArrayList<IspezioneVisivaPiantaDTO>();
    }

    //mi carico anche i def
    List<IspezioneVisivaPiantaDTO> listPiantaDef= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoDef");
    
    String prefix = "";
    
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";


    if(pianta.getAssociatoP()!=null && !pianta.getAssociatoP().equals("S")) {
      if(pianta.getIdIspezioneVisivaPianta()>0) {
        for(IspezioneVisivaPiantaDTO obj : listPianta) {
          if(obj.getIdIspezioneVisivaPianta().equals(pianta.getIdIspezioneVisivaPianta())) {
            pianta.setIdSpecieVegetale(pianta.getIdSpecie());
            Coordinate coo= new Coordinate();
            coo.setLatitudine(pianta.getLat());
            coo.setLongitudine(pianta.getLon());
            pianta.setCoordinate(coo);
            //carico la specie
            SpecieVegetaleDTO specie = specieEJB.findById(pianta.getIdSpecie());
            pianta.setSpecie(specie.getGenereENomeVolgare());
            if (pianta.getPositivita() != null) {
              //carico la positivita
              PositivitaDTO positivita = ispezioneVisivaPiantaEJB.findPositivitaById(pianta.getPositivita());
              pianta.setDescPositivita((positivita!=null)?positivita.getDescPositivita():null);
            }
            if (pianta.getDiametro() != null) {
              //carico il diametro
              DiametroDTO diametro = ispezioneVisivaPiantaEJB.findDiametroById(pianta.getDiametro());
              pianta.setDescDiametro((diametro!=null)?diametro.getDescDiametro():null);
            }
            Ubicazione ub = new Ubicazione();
            ub.setNome(pianta.getNome());
            ub.setCognome(pianta.getCognome());
            ub.setEmail(pianta.getEmail());
            ub.setIndirizzo(pianta.getIndirizzo());
            ub.setNumero(pianta.getNumero());
            ub.setTelefono(pianta.getTelefono());
            pianta.setUbicazione(ub);
            pianta.setNote1(pianta.getNote1());
            pianta.setNote2(pianta.getNote2());
            pianta.setNote3(pianta.getNote3());
            listPianta.remove(obj);
            listPianta.add(pianta);
            redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
            model.addAttribute("ispezioneVisiva", ispezioneVisiva);
            return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=addPianta";
          }
        }
      } else {
        int prog = listPianta.size()+1;
        pianta.setIdIspezioneVisivaPianta(prog);
        pianta.setIdSpecieVegetale(pianta.getIdSpecie());
        Coordinate coo= new Coordinate();
        coo.setLatitudine(pianta.getLat());
        coo.setLongitudine(pianta.getLon());
        pianta.setCoordinate(coo);
        //carico la specie
        SpecieVegetaleDTO specie = specieEJB.findById(pianta.getIdSpecie());
        pianta.setSpecie(specie.getGenereENomeVolgare());
        //carico la positivita
        if(pianta.getPositivita() != null) {
        	PositivitaDTO positivita = ispezioneVisivaPiantaEJB.findPositivitaById(pianta.getPositivita());
        	pianta.setDescPositivita((positivita!=null)?positivita.getDescPositivita():null);
        }
        //carico il diametro
        if(pianta.getDiametro() != null) {
        	DiametroDTO diametro = ispezioneVisivaPiantaEJB.findDiametroById(pianta.getDiametro());
        	pianta.setDescDiametro((diametro!=null)?diametro.getDescDiametro():null);
        }
        Ubicazione ub = new Ubicazione();
        ub.setNome(pianta.getNome());
        ub.setCognome(pianta.getCognome());
        ub.setEmail(pianta.getEmail());
        ub.setIndirizzo(pianta.getIndirizzo());
        ub.setNumero(pianta.getNumero());
        ub.setTelefono(pianta.getTelefono());
        pianta.setUbicazione(ub);
        pianta.setNote1(pianta.getNote1());
        pianta.setNote2(pianta.getNote2());
        pianta.setNote3(pianta.getNote3());
        listPianta.add(pianta);
      }
      session.setAttribute("piantaDtoProv", listPianta);
    } else {
      if(pianta.getIdIspezioneVisivaPianta()>0) {
        for(IspezioneVisivaPiantaDTO obj : listPiantaDef) {
          if(obj.getIdIspezioneVisivaPianta().equals(pianta.getIdIspezioneVisivaPianta())) {
            pianta.setIdSpecieVegetale(pianta.getIdSpecie());
            Coordinate coo= new Coordinate();
            coo.setLatitudine(pianta.getLat());
            coo.setLongitudine(pianta.getLon());
            pianta.setCoordinate(coo);
            //carico la specie
            SpecieVegetaleDTO specie = specieEJB.findById(pianta.getIdSpecie());
            pianta.setSpecie(specie.getGenereENomeVolgare());
            if (pianta.getPositivita() != null) {
              //carico la positivita
              PositivitaDTO positivita = ispezioneVisivaPiantaEJB.findPositivitaById(pianta.getPositivita());
              pianta.setDescPositivita((positivita!=null)?positivita.getDescPositivita():null);
            }
            if (pianta.getDiametro() != null) {
              //carico il diametro
              DiametroDTO diametro = ispezioneVisivaPiantaEJB.findDiametroById(pianta.getDiametro());
              pianta.setDescDiametro((diametro!=null)?diametro.getDescDiametro():null);
            }
            Ubicazione ub = new Ubicazione();
            ub.setNome(pianta.getNome());
            ub.setCognome(pianta.getCognome());
            ub.setEmail(pianta.getEmail());
            ub.setIndirizzo(pianta.getIndirizzo());
            ub.setNumero(pianta.getNumero());
            ub.setTelefono(pianta.getTelefono());
            pianta.setUbicazione(ub);
            pianta.setModificato("S");
            listPiantaDef.remove(obj);
            listPiantaDef.add(pianta);
            redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
            model.addAttribute("ispezioneVisiva", ispezioneVisiva);
            return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=addPianta";
          }
        }
      }
      session.setAttribute("piantaDtoDef", listPiantaDef);
    }
    session.setAttribute("piantaDtoProv", listPianta);
    redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
    model.addAttribute("ispezioneVisiva", ispezioneVisiva);
    return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=addPianta";
  }

  @RequestMapping(value = {"/gestioneVisual/removePiantaProv", "/gestioneVisual/removePiantaProvFromListaMissioni", "/gestioneVisual/removePiantaProvFromMissione"}, method = RequestMethod.POST)
  public String removePiantaProv(Model model,
      @ModelAttribute("pianta") IspezioneVisivaPiantaDTO pianta,
      @ModelAttribute("ispezioneVisiva") IspezioneVisivaDTO ispezioneVisiva, 
      @RequestParam(value = "idIspezione") Integer idIspezione, 
      @RequestParam(value = "idIspezioneVisivaPianta") Integer idIspezioneVisivaPianta,
      HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    List<IspezioneVisivaPiantaDTO> piantaDtoProv= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoProv");
    
    redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
    model.addAttribute("ispezioneVisiva", ispezioneVisiva);

    String prefix = "";
    
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    for(IspezioneVisivaPiantaDTO obj : piantaDtoProv) {
     // System.out.println(obj.getIdOrganismoNocivo());
      if(obj.getIdIspezioneVisivaPianta().equals(idIspezioneVisivaPianta)) {
        piantaDtoProv.remove(obj);  
        session.setAttribute("piantaDtoProv",piantaDtoProv); 
        return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=deletePianteProv";        
       }     
    }
    session.setAttribute("piantaDtoProv",piantaDtoProv); 
    return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=deletePianteProv";  
  }

  @RequestMapping(value = {"/gestioneVisual/removePiantaDef", "/gestioneVisual/removePiantaDefFromListaMissioni", "/gestioneVisual/removePiantaDefFromMissione"}, method = RequestMethod.POST)
  public String removePiantaDef(Model model,
      @ModelAttribute("pianta") IspezioneVisivaPiantaDTO pianta,
      @ModelAttribute("ispezioneVisiva") IspezioneVisivaDTO ispezioneVisiva, 
      @RequestParam(value = "idIspezione") Integer idIspezione, 
      @RequestParam(value = "idIspezioneVisivaPianta") Integer idIspezioneVisivaPianta,
      HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    Errors error = new Errors();
	  
    redirectAttributes.addFlashAttribute("ispezioneVisiva", ispezioneVisiva);
    model.addAttribute("ispezioneVisiva", ispezioneVisiva);
    
    List<IspezioneVisivaPiantaDTO> listaDef= ispezioneVisivaPiantaEJB.findByIdIspezioneVisiva(idIspezione);  
   
    List<IspezioneVisivaPiantaDTO> piantaDtoDef= (List<IspezioneVisivaPiantaDTO>)session.getAttribute("piantaDtoDef"); 
  
    if (listaDef.size()==1 || piantaDtoDef.size()==1) {
	      error.addError("Pianta", "Impossibile cancellare. Deve essere presente almeno una pianta");
    }

    if(piantaDtoDef!=null && !piantaDtoDef.isEmpty())
      listaDef=piantaDtoDef;

    String prefix = "";
    
    if (request.getServletPath().indexOf("FromListaMissioni.do") > -1)
      prefix = "FromListaMissioni";
    else
    if (request.getServletPath().indexOf("FromMissione.do") > -1)
      prefix = "FromMissione";

    if (!error.isEmpty())
    {
      model.addAttribute("error", error);
      redirectAttributes.addFlashAttribute("error", "Impossibile cancellare. Deve essere presente almeno una pianta");
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
    } else {
        for(IspezioneVisivaPiantaDTO obj : listaDef) {
            // System.out.println(obj.getIdOrganismoNocivo());
             if(obj.getIdIspezioneVisivaPianta().equals(idIspezioneVisivaPianta)) {
               listaDef.remove(obj);  
               session.setAttribute("piantaDtoDef",listaDef); 
               return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=deletePianteDef";        
              }      
           }
    }
    session.setAttribute("piantaDtoDef",listaDef);
    return "redirect:edit"+prefix+".do?idIspezione=" + idIspezione + "&editabile=true&from=deletePianteDef";
  }

  private List<SceltaEsclusiva> getSelectScelta(){
    
    List<SceltaEsclusiva> lista = new ArrayList<SceltaEsclusiva>();    
    SceltaEsclusiva sceltaSi = new SceltaEsclusiva();
    sceltaSi.setId("S");
    sceltaSi.setDescrizione("Si");
    lista.add(sceltaSi);
    
    SceltaEsclusiva sceltaNo = new SceltaEsclusiva();
    sceltaNo.setId("N");
    sceltaNo.setDescrizione("No");
    lista.add(sceltaNo);
    
    return lista;
  }

}
