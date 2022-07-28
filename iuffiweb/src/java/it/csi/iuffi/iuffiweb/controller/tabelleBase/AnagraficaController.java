package it.csi.iuffi.iuffiweb.controller.tabelleBase;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.EnteDTO;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.TokenUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.decodifiche.Ente;
import it.csi.papua.papuaserv.dto.gestioneutenti.EnteAnagraficaVO;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteAnagraficaVO;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.exception.InternalException;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservRESTClient;

@Controller
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class AnagraficaController extends TabelleController
{
  @Autowired
  private IAnagraficaEJB anagraficaEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/anagrafica/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/anagrafica";
  }

  @RequestMapping(value = "/anagrafica/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    AnagraficaDTO anagrafica = (AnagraficaDTO) model.asMap().get("anagrafica");
    model.addAttribute("anagrafica", anagrafica);
    
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  

    if (anagrafica != null && anagrafica.getCfAnagraficaEst() != null) {
      PapuaservRESTClient client = PapuaservProfilazioneServiceFactory.getRestServiceClient();
      try
      {
        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        UtenteAnagraficaVO[] utentiPapua = client.findUtentiEntiByCodiceFiscaleList(new String[] {anagrafica.getCfAnagraficaEst()}, utente.getIdProcedimento(), "");
        if (utentiPapua != null && utentiPapua.length > 0 && utentiPapua[0].getIdUtente() != null) {
          logger.debug("Utente " + anagrafica.getCfAnagraficaEst() + " trovato su Papua");
          //model.addAttribute("utentePapua", utentiPapua[0]);
          EnteAnagraficaVO[] enti = utentiPapua[0].getArrEnti();
          if (enti != null) {
            for (EnteAnagraficaVO ente : enti) {
              ente.setFaxEnte(ente.getDenominazioneEnte() + " - " + ente.getIndirizzoEnte() + " (" + ente.getPartitaIvaEnte() + ")");
            }
          }
          model.addAttribute("enti", enti);
          if (anagrafica.getIdEnte() != null && anagrafica.getIdEnte() > 0) {
            EnteDTO enteDTO = anagraficaEJB.findEnteById(anagrafica.getIdEnte());
            if (enteDTO != null) {
              anagrafica.setIdEntePapua(Integer.decode(enteDTO.getCodiceEst()));
            }
          }
        } else {
            logger.debug("Utente " + anagrafica.getCfAnagraficaEst() + " non trovato su Papua");
        }
      }
      catch (InternalException e)
      {
        logger.error("Servizio Papua non disponibile. Errore: " + e.getMessage());
      }
    }
    model.addAttribute("disabled", false);
    setBreadcrumbs(model, request);
    return "gestionetabelle/anagraficaAdd";
  }
  
  @RequestMapping(value = "/anagrafica/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableAnagrafica");
      return "gestionetabelle/confermaElimina";
  }

  @RequestMapping(value = "/anagrafica/save")
  public String save(Model model, @ModelAttribute("anagrafica") AnagraficaDTO anagrafica, HttpSession session, HttpServletRequest request,
      HttpServletResponse response, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    AnagraficaDTO dto = anagrafica;
    model.addAttribute("success", "Anagrafica salvata con successo");

    Errors errors = new Errors();
    if (anagrafica.getNome()==null || "".equals(anagrafica.getNome())) {
      errors.addError("nome", "Campo Obbligatorio");
    }
    if (anagrafica.getCognome()==null || "".equals(anagrafica.getCognome())) {
      errors.addError("cognome", "Campo Obbligatorio");
    }
    if (anagrafica.getCfAnagraficaEst()==null || "".equals(anagrafica.getCfAnagraficaEst())) {
      errors.addError("cfAnagraficaEst", "Campo Obbligatorio");
    }
    if (!IuffiUtils.VALIDATION.controlloCf(anagrafica.getCfAnagraficaEst())) {
      errors.addError("cfAnagraficaEst", "Verificare il formato del codice fiscale inserito");
    }

    if (anagrafica.getPagaOraria()==null || anagrafica.getPagaOraria()==0) {
      errors.addError("pagaOraria", "Campo Obbligatorio");
    } else if ((BigDecimal.valueOf(anagrafica.getPagaOraria()).scale() > 2)) {
      errors.addError("pagaOraria", "Formato previsto 00000000.00");
    }
    if (anagrafica.getIdEnte()==null || anagrafica.getIdEnte()==0) {
      errors.addError("idEnte", "Campo Obbligatorio");
    }
    
    if (errors.isEmpty()) {
      try {
        PapuaservRESTClient client = PapuaservProfilazioneServiceFactory.getRestServiceClient();
        UtenteAnagraficaVO[] utentiPapua = client.findUtentiEntiByCodiceFiscaleList(new String[] {anagrafica.getCfAnagraficaEst()}, utente.getIdProcedimento(), "");
        
        if (utentiPapua != null && utentiPapua.length > 0 && utentiPapua[0].getIdUtente() != null) {
          logger.debug("Utente " + anagrafica.getCfAnagraficaEst() + " trovato su Papua");
          anagrafica.setIdAnagraficaEst(utentiPapua[0].getIdUtente());  // Inserisco l'id anagrafica di Papua
        }

        Ente[] enti = client.findEntiByIdEnte(new int[] {anagrafica.getIdEnte()}, false);
        if (enti != null && enti.length > 0) {
          Ente ente = enti[0];
          EnteDTO enteDTO = anagraficaEJB.findEnteByIdPapua(anagrafica.getIdEnte());
          if (enteDTO != null)
            anagrafica.setIdEnte(enteDTO.getIdEnte());
          else {
            // Inserisco l'ente
            enteDTO = new EnteDTO();
            enteDTO.setCodiceEst(String.valueOf(ente.getIdEnte()));   // Inserisco l'id ente di Papua
            enteDTO.setDenominazione(ente.getDenominazione());
            enteDTO = anagraficaEJB.insertEnte(enteDTO);
            anagrafica.setIdEnte(enteDTO.getIdEnte());
          }
        }
      } catch (Exception e) {
          logger.error("Errore nella ricerca dell'ente con id " + anagrafica.getIdEnte());
          errors.addError("idEnte", "Ente non presente su Papua");
      }
    }
    
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("success", null);
      redirectAttributes.addFlashAttribute("anagrafica", dto);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      if(dto.getIdAnagrafica()!=null && dto.getIdAnagrafica()>0)
        return "redirect:edit.do?idAnagrafica="+dto.getIdAnagrafica();
      else
        return "redirect:showFilterAdd.do";
    }
    else
    {
      if (dto.getSubcontractor()==null)
        dto.setSubcontractor("N");
      anagrafica.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
      try {
        if (anagrafica.getIdAnagrafica() == null || anagrafica.getIdAnagrafica() == 0) {
          dto = anagraficaEJB.insertAnagrafica(anagrafica);
        }else {
          dto = anagraficaEJB.findById(dto.getIdAnagrafica());
          anagrafica.setDataInizioValidita(dto.getDataInizioValidita());
          if(anagrafica.isAnnoCorrente()) {
            anagraficaEJB.updateAnagrafica(anagrafica);
          }else {
            anagraficaEJB.updateDataFineValidita(anagrafica.getIdAnagrafica()); 
            dto = anagraficaEJB.insertAnagrafica(anagrafica); 
          }
        }
        AnagraficaDTO dtoNew = anagraficaEJB.findById(dto.getIdAnagrafica());
        model.addAttribute("anagrafica", dtoNew);
      } catch (InternalUnexpectedException e) {
        boolean isFound = e.getCause().getMessage().indexOf("ORA-00001") !=-1? true: false; 
        if(isFound)
            //model.addAttribute("error", "Codice Fiscale già registrato");
            errors.addError("cfAnagraficaEst", "Il Codice Fiscale è già registrato");
        else
            model.addAttribute("error", "Errore in fase di inserimento");
        redirectAttributes.addFlashAttribute("anagrafica", dto);
        redirectAttributes.addFlashAttribute("errors", errors);
        redirectAttributes.addFlashAttribute("success", null);
        redirectAttributes.addFlashAttribute("model", model);
        return "redirect:showFilterAdd.do";
      }
      //imposto i filtri di ricerca
      //filtri
      Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
      session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      setBreadcrumbs(model, request);
      //return this.showFilter(model, session, request,response);
      return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/anagrafica/searchPapua")
  public String searchPapua(Model model, HttpSession session, @RequestParam(value = "cfAnagrafica") String cfAnagrafica, HttpServletRequest request,
      HttpServletResponse response, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    PapuaservRESTClient client = null;
    Errors errors = new Errors();
    try
    {
      UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      client = PapuaservProfilazioneServiceFactory.getRestServiceClient();
      UtenteAnagraficaVO[] utentiPapua = client.findUtentiEntiByCodiceFiscaleList(new String[] {cfAnagrafica}, utente.getIdProcedimento(), "");
      
      if (utentiPapua != null && utentiPapua.length > 0 && utentiPapua[0].getIdUtente() != null) {
          logger.debug("Utente " + cfAnagrafica + " trovato su Papua");
          //model.addAttribute("utentePapua", utentiPapua[0]);
          EnteAnagraficaVO[] enti = utentiPapua[0].getArrEnti();
          if (enti != null) {
            for (EnteAnagraficaVO ente : enti) {
              ente.setFaxEnte(ente.getDenominazioneEnte() + " - " + ente.getIndirizzoEnte() + " (" + ente.getPartitaIvaEnte() + ")");
            }
          }
          model.addAttribute("enti", enti);
          AnagraficaDTO anag = new AnagraficaDTO(cfAnagrafica);
          anag.setCognome(utentiPapua[0].getCognome());
          anag.setNome(utentiPapua[0].getNome());
          model.addAttribute("anagrafica", anag);
      } else {
          logger.debug("Utente " + cfAnagrafica + " non trovato su Papua");
          errors.addError("cfAnagraficaEst", "Codice fiscale non trovato su Papua");
      }
    }
    catch (InternalException e)
    {
      e.printStackTrace();
      logger.debug("Errore nella ricerca del codice fiscale su Papua: " + e.getMessage());
      errors.addError("cfAnagraficaEst", "Servizio Papua non disponibile");
    }
    
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("success", null);
      redirectAttributes.addFlashAttribute("anagrafica", new AnagraficaDTO(cfAnagrafica));
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      return "redirect:showFilterAdd.do";
    }
    
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/anagraficaAdd";
  }

  @RequestMapping(value = "/anagrafica/search")
  public String search(Model model, @ModelAttribute("anagrafica") AnagraficaDTO anagrafica ,HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<AnagraficaDTO> list = anagraficaEJB.findByFilter(anagrafica);
    model.addAttribute("lista", list);
    
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
 
    setBreadcrumbs(model, request);
    return "gestionetabelle/anagrafica";
  }

  @RequestMapping(value = "/anagrafica/edit")
  public String edit(Model model,HttpSession session, @RequestParam(value = "idAnagrafica") String idAnagrafica, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    
    AnagraficaDTO dto = anagraficaEJB.findById(Integer.decode(idAnagrafica));
    
    model.addAttribute("anagrafica", dto);
    List<AnagraficaDTO> list = new ArrayList<AnagraficaDTO>();
    list.add(dto);
    model.addAttribute("lista", list);

    PapuaservRESTClient client = PapuaservProfilazioneServiceFactory.getRestServiceClient();
    try
    {
      UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      //EnteUtente[] enteUtente = client.findEntiForPersonaAndRuoloInApplicazione(utente.getCodiceFiscale(), utente.getIdProcedimento(), utente.getRuolo().getCodice());
      UtenteAnagraficaVO[] utentiPapua = client.findUtentiEntiByCodiceFiscaleList(new String[] {dto.getCfAnagraficaEst()}, utente.getIdProcedimento(), "");
      if (utentiPapua != null && utentiPapua.length > 0 && utentiPapua[0].getIdUtente() != null) {
        logger.debug("Utente " + dto.getCfAnagraficaEst() + " trovato su Papua");
        //model.addAttribute("utentePapua", utentiPapua[0]);
        EnteAnagraficaVO[] enti = utentiPapua[0].getArrEnti();
        if (enti != null) {
          for (EnteAnagraficaVO ente : enti) {
            ente.setFaxEnte(ente.getDenominazioneEnte() + " - " + ente.getIndirizzoEnte() + " (" + ente.getPartitaIvaEnte() + ")");
          }
        }
        model.addAttribute("enti", enti);
      } else {
          logger.debug("Utente " + dto.getCfAnagraficaEst() + " non trovato su Papua");
      }
    }
    catch (InternalException e)
    {
      logger.error("Servizio Papua non disponibile. Errore: " + e.getMessage());
    }

    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
    
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 
    setBreadcrumbs(model, request);
    return "gestionetabelle/anagraficaAdd";
  }
  
  @RequestMapping(value = "/anagrafica/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idAnagrafica") String idAnagrafica, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    AnagraficaDTO anagraficaDTO = null;
    try {
      anagraficaDTO = anagraficaEJB.findById(Integer.decode(idAnagrafica));
      
      if(!anagraficaDTO.isAnnoCorrente()) {
        anagraficaEJB.updateDataFineValidita(Integer.decode(idAnagrafica));
      }else {
        anagraficaEJB.remove(Integer.decode(idAnagrafica));
      }
    }
    catch (InternalUnexpectedException e) {
      if (e.getCause() instanceof DataIntegrityViolationException) {
        anagraficaEJB.updateDataFineValidita(Integer.decode(idAnagrafica));
      }
      else {
        attributes.addFlashAttribute("error", "Si è verificato un errore inaspettato durante l'eliminazione dell'anagrafica");
        logger.debug("Errore nell'eliminazione dell'anagrafica con id = " + idAnagrafica + " : " + e.getMessage());
      }
    }
    return new RedirectView("showFilter.do", true);
  }
  
  @ResponseBody
  @RequestMapping(value = "/rest/ispettori", method = RequestMethod.GET)
  public ResponseEntity<?> getIspettori(@RequestParam(value = "cognome", required = false) String cognome, HttpServletRequest request) throws InternalUnexpectedException
  {
    List<AnagraficaDTO> lista = null;
    AnagraficaDTO anagrafica = new AnagraficaDTO(true);

    if (StringUtils.isNotBlank(cognome)) {
      anagrafica.setCognome(cognome);
    }

    try {
      String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      String cf = TokenUtils.verifyToken(jwt);
      AnagraficaDTO anagFilter = new AnagraficaDTO(cf);
      anagFilter.setActive(true);
      List<AnagraficaDTO> listAnag = anagraficaEJB.findByFilter(anagFilter);
      if (listAnag != null && listAnag.size() > 0) {
        AnagraficaDTO anagIspettore = listAnag.get(0);
        anagrafica.setIdEnte(anagIspettore.getIdEnte());
        lista = anagraficaEJB.findByFilter(anagrafica);
      }
      if (lista == null)
        lista = new ArrayList<AnagraficaDTO>();
      
    } catch (Exception e) {
        logger.debug("Errore nel metodo getIspettori: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella ricerca ispettori");
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<AnagraficaDTO>>(lista,HttpStatus.OK);
  }
  
  @RequestMapping(value = "/rest/api/anagraficaJsonOutput",produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<AnagraficaDTO>> anagraficaJsonOutput() throws InternalUnexpectedException
  {
    List<AnagraficaDTO> lista = anagraficaEJB.findAll();
    return new ResponseEntity<List<AnagraficaDTO>>(lista,HttpStatus.OK);
  }
 
  
  @RequestMapping(value = "/anagrafica/getAnagraficaJson", produces = "application/json")
  @ResponseBody
  public /*List<AnagraficaDTO>*/String getAnagraficaJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    List<AnagraficaDTO> lista = anagraficaEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    
    return obj;
  }
  
  @RequestMapping(value = "/anagrafica/getIspettoriAssegnati", produces = "application/json")
  @ResponseBody
  public String getIspettoriAssegnati(@RequestBody String checkbox, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    
    List<AnagraficaDTO> lista = null;
    
    if (checkbox != null) {
      if (checkbox.toLowerCase().indexOf("true") > -1) {
        session.setAttribute("checkboxAllIspettoriAssegnati", "true");
      } else {
        session.setAttribute("checkboxAllIspettoriAssegnati", "false");
      }
    }
    if (session.getAttribute("checkboxAllIspettoriAssegnati") != null) {
      if (((String) session.getAttribute("checkboxAllIspettoriAssegnati")).equals("true")) {
          lista = anagraficaEJB.findAll();
      } else {
          lista = anagraficaEJB.findValidi();
      }
    } else {
        lista = anagraficaEJB.findAll();
    }
    
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }

  @RequestMapping(value = "/anagrafica/getIspettoriSecondari", produces = "application/json")
  @ResponseBody
  public String getIspettoriSecondari(@RequestBody String checkbox, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    
    List<AnagraficaDTO> lista = null;
    
    if (checkbox != null) {
      if (checkbox.toLowerCase().indexOf("true") > -1) {
        session.setAttribute("checkboxAllIspettoriSecondari", "true");
      } else {
        session.setAttribute("checkboxAllIspettoriSecondari", "false");
      }
    }
    if (session.getAttribute("checkboxAllIspettoriSecondari") != null) {
      if (((String) session.getAttribute("checkboxAllIspettoriSecondari")).equals("true")) {
          lista = anagraficaEJB.findAll();
      } else {
          lista = anagraficaEJB.findValidi();
      }
    } else {
        lista = anagraficaEJB.findAll();
    }
    
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }

  @Lazy
  @RequestMapping(value = "/anagrafica/anagraficaExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<AnagraficaDTO> elenco = anagraficaEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"anagrafica.xls\"");
    
    return new ModelAndView("excelAnagraficaView", "elenco", elenco);
  }
 
  @SuppressWarnings("unchecked")
  @Lazy
  @RequestMapping(value = "/anagrafica/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableAnagrafica") == null || "{}".equals(filtroInSessione.get("tableAnagrafica"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableAnagrafica");
      } 
       // filtroInit =
           // if(anagrafica.getCognome()!=null &&)
          //  + ",\"cognome\":{\"cnt\":[\""+dto.getCognome().toUpperCase()+"\"]}"
          //  filtroInit += "}";  
       // value='{"annoCampagna":{"cnt":"2016"},"descrizione":{"_values":["Liquidato"]}}'
      filtroInSessione.put("tableAnagrafica", filtroInit);
      return filtroInSessione;
  }
 
}
