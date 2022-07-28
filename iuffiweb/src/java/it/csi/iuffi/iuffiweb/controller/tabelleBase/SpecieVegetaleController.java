package it.csi.iuffi.iuffiweb.controller.tabelleBase;


import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import it.csi.iuffi.iuffiweb.business.IRiepilogoMonitoraggioEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TabelleEnum;
import it.csi.iuffi.iuffiweb.model.TrappolaOnDTO;
import it.csi.iuffi.iuffiweb.model.api.GenereSpecieVegetale;
import it.csi.iuffi.iuffiweb.model.api.RiepilogoMonitoraggio;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
@Controller

@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class SpecieVegetaleController  extends TabelleController
{
  @Autowired
  private ISpecieVegetaleEJB specieEJB;

  @Autowired
  private IRiepilogoMonitoraggioEJB riepilogoEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/specieVegetale/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/specieVegetali";
  }

  
  @RequestMapping(value = "/specieVegetale/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {

    SpecieVegetaleDTO specieVegetale = (SpecieVegetaleDTO) model.asMap().get("specieVegetale");
    model.addAttribute("specieVegetale", specieVegetale);

    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/specieVegetaliAdd";
  }

  @RequestMapping(value = "/specieVegetale/save")
  public /*RedirectView*/String save(Model model, @ModelAttribute("specieVegetale") SpecieVegetaleDTO specieVegetale, HttpSession session, HttpServletRequest request,
      HttpServletResponse response, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    Errors errors = new Errors();   
    SpecieVegetaleDTO dto = specieVegetale;
    if (specieVegetale.getGenereSpecie()==null || "".equals(specieVegetale.getGenereSpecie())) {
      errors.addError("genereSpecie", "Campo Obbligatorio");
    }
    if (specieVegetale.getNomeVolgare()==null || "".equals(specieVegetale.getNomeVolgare())) {
      errors.addError("nomeVolgare", "Campo Obbligatorio");
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("specieVegetale", dto);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      if(dto.getIdSpecieVegetale()!=null && dto.getIdSpecieVegetale()>0)
        return "redirect:edit.do?idSpecieVegetale="+dto.getIdSpecieVegetale();
      else
        return "redirect:showFilterAdd.do";
    }
    else
    {
      if (specieVegetale.getDataInizioValidita() == null) {
        specieVegetale.setDataInizioValidita(new Date());
      }
      UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      specieVegetale.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
      if (specieVegetale.getFlagEuro()==null || specieVegetale.getFlagEuro().equals(""))
        specieVegetale.setFlagEuro("N");

      if (specieVegetale.getIdSpecieVegetale() == null || specieVegetale.getIdSpecieVegetale() == 0)
        dto = specieEJB.insertSpecieVegetale(specieVegetale);
      else {
        dto = specieEJB.findById(dto.getIdSpecieVegetale());
        specieVegetale.setDataInizioValidita(dto.getDataInizioValidita());
        if(specieVegetale.isAnnoCorrente()) {
          specieEJB.updateSpecieVegetale(specieVegetale);
        } else {
        //se viene richiesta la modifica del record..la storicizzo e inserisco il nuovo
          List<RiepilogoMonitoraggio> list = riepilogoEJB.findByIdSpecieVegetale(specieVegetale.getIdSpecieVegetale());
          if (list!=null && !list.isEmpty()) {
            specieVegetale = specieEJB.findById(specieVegetale.getIdSpecieVegetale());
            model.addAttribute("specieVegetale", specieVegetale);
            redirectAttributes.addFlashAttribute("error", "La specie vegetale " + specieVegetale.getNomeVolgare() + " non può essere modificata in quanto è referenziato dalla tabella COMPATIBILITA' SPECIE/CAMPIONE/PERIODO/ORGANISMO NOCIVO");
            redirectAttributes.addFlashAttribute("model", model);
            return "redirect:edit.do?idSpecieVegetale="+specieVegetale.getIdSpecieVegetale();
          }
          specieEJB.updateDataFineValidita(specieVegetale.getIdSpecieVegetale());
          dto = specieEJB.insertSpecieVegetale(specieVegetale); 
        }
      }
      return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/specieVegetale/search")
  public String search(Model model, @ModelAttribute("specieVegetale") SpecieVegetaleDTO specieVegetale,HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<SpecieVegetaleDTO> list = specieEJB.findByFilter(specieVegetale);
    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/specieVegetali";
  }

 

  @RequestMapping(value = "/specieVegetale/edit")
  public String edit(Model model,HttpSession session, @RequestParam(value = "idSpecieVegetale") String idSpecieVegetale, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    SpecieVegetaleDTO dto = (SpecieVegetaleDTO) model.asMap().get("specieVegetale");

    if(dto==null)
        dto = specieEJB.findById(Integer.decode(idSpecieVegetale));
    model.addAttribute("specieVegetale", dto);
    List<SpecieVegetaleDTO> list = new ArrayList<SpecieVegetaleDTO>();
    list.add(dto);
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 
    
    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/specieVegetaliAdd";
  }
 
  
  @RequestMapping(value = "/specieVegetale/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idSpecieVegetale") String idSpecieVegetale, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    SpecieVegetaleDTO specieVegetaleDTO = null;
    try {
      specieVegetaleDTO = specieEJB.findById(Integer.decode(idSpecieVegetale));
      
      List<RiepilogoMonitoraggio> list = riepilogoEJB.findByIdSpecieVegetale(Integer.decode(idSpecieVegetale));
      if (list != null && !list.isEmpty()) {
        SpecieVegetaleDTO dto = specieEJB.findById(Integer.decode(idSpecieVegetale));
        attributes.addFlashAttribute("error", "La specie vegetale " + dto.getGenereENomeVolgare() + " non può essere eliminata in quanto referenziata dalla tabella COMPATIBILITA' SPECIE/CAMPIONE/PERIODO/ORGANISMO NOCIVO");
      } else {
        if(!specieVegetaleDTO.isAnnoCorrente()) {
          specieEJB.updateDataFineValidita(Integer.decode(idSpecieVegetale));
        }else {
          specieEJB.remove(Integer.decode(idSpecieVegetale));
        }
      }
    } catch (InternalUnexpectedException e) {
      if (e.getCause() instanceof DataIntegrityViolationException)
      {
        specieEJB.updateDataFineValidita(Integer.decode(idSpecieVegetale));
      } else {
        SpecieVegetaleDTO specieVegetaleDto = specieEJB.findById(Integer.decode(idSpecieVegetale));
        attributes.addFlashAttribute("error", "Si è verificato un errore inaspettato durante l'eliminazione della specie vegetale " + specieVegetaleDto.getNomeVolgare());
        logger.debug("Errore nell'eliminazione della specie vegetale con id = " + idSpecieVegetale + " : " + e.getMessage());
      }
    }
    return new RedirectView("showFilter.do", true);
  }
  
  
  @RequestMapping(value = "/rest/api/specieVegetaleJsonOutput",produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<SpecieVegetaleDTO>> specieVegetaleJsonOutput() throws InternalUnexpectedException
  {
    List<SpecieVegetaleDTO> lista = specieEJB.findAll();
    return new ResponseEntity<List<SpecieVegetaleDTO>>(lista,HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/specie-vegetali", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<?> getSpecieVegetali()
  {
    List<GenereSpecieVegetale> lista = null;
    try
    {
      lista = specieEJB.getSpecieVegetali();
      if (lista == null)
        lista = new ArrayList<GenereSpecieVegetale>();
    } catch (Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        ErrorResponse err = new ErrorResponse();
        err.setMessage("Errore in genereazione lista specie vegetali");
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<GenereSpecieVegetale>>(lista,HttpStatus.OK);
  }
  
  @RequestMapping(value = "/specieVegetale/getSpecieVegetale", produces = "application/json")
  @ResponseBody
  public String getSpecieVegetaleJson(@RequestBody String checkbox, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    
    List<SpecieVegetaleDTO> lista = null;
    
    if (checkbox != null) {
      if (checkbox.toLowerCase().indexOf("true") > -1) {
        session.setAttribute("checkboxAllSpecieVegetali", "true");
      } else {
        session.setAttribute("checkboxAllSpecieVegetali", "false");
      }
    }
    if (session.getAttribute("checkboxAllSpecieVegetali") != null) {
      if (((String) session.getAttribute("checkboxAllSpecieVegetali")).equals("true")) {
          lista = specieEJB.findAll();
      } else {
          lista = specieEJB.findValidi();
      }
    } else {
        lista = specieEJB.findAll();
    }
    
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }
  
  @RequestMapping(value = "/specieVegetale/getSpecieVegetaleJson", produces = "application/json")
  @ResponseBody
  public String getSpecieVegetaleJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<SpecieVegetaleDTO> lista = specieEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }
  
  @RequestMapping(value = "/rest/specieVegetaleJsonInput",
                 consumes = { "application/json" }, 
                 produces = { "application/json" },
                 method = RequestMethod.POST)
   public ResponseEntity<SpecieVegetaleDTO> specieVegetaleJsonInput(@Valid @RequestBody List<SpecieVegetaleDTO> body) throws MalformedURLException, IOException, InternalUnexpectedException
  {
    Iterator iter = body.iterator();        
    while(iter.hasNext()) {
      Object riga =iter.next();
      HashMap<String, String> passedValues = (HashMap<String, String>) riga;
      SpecieVegetaleDTO specie = new SpecieVegetaleDTO();
        for (Entry<String, String> mapTemp : passedValues.entrySet()) {
         if (mapTemp.getKey().equalsIgnoreCase("genereSpecie")) {
          specie.setGenereSpecie(mapTemp.getValue());
         }
         if (mapTemp.getKey().equalsIgnoreCase("nomeVolgare")) {
           specie.setNomeVolgare(mapTemp.getValue());
          }         
        }    
      specieEJB.insertSpecieVegetale(specie);
    }
    return new ResponseEntity<SpecieVegetaleDTO>(HttpStatus.OK);
  }

  @Lazy
  @RequestMapping(value = "/specieVegetale/specieVegetaleExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<SpecieVegetaleDTO> elenco = specieEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"specieVegetali.xls\"");
    
    return new ModelAndView("excelSpecieVegetaleView", "elenco", elenco);
  }

  @Lazy
  @RequestMapping(value = "/specieVegetale/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableSpecieVegetale") == null || "{}".equals(filtroInSessione.get("tableSpecieVegetale"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableSpecieVegetale");
      } 
     filtroInSessione.put("tableSpecieVegetale", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/specieVegetale/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("id",id);
    model.addAttribute("table","tableSpecieVegetale");
    return "gestionetabelle/confermaElimina";
  }

  
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

}
