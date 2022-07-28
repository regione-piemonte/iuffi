package it.csi.iuffi.iuffiweb.controller.tabelleBase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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

import it.csi.iuffi.iuffiweb.business.ITipoAreaEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.VelocitaDTO;
import it.csi.iuffi.iuffiweb.model.api.TipoArea;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller

//@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class TipoAreaController extends TabelleController
{
 
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  
  @Autowired
  private ITipoAreaEJB tipoAreaEJB;
  
  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/tipoArea/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoArea";
  }

  
  @RequestMapping(value = "/tipoArea/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    TipoAreaDTO tipoArea = (TipoAreaDTO) model.asMap().get("tipoArea");
    model.addAttribute("tipoArea", tipoArea);

    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoAreaAdd";
  }


  
  @RequestMapping(value = "/tipoArea/save")
  public String save(Model model, @ModelAttribute("tipoArea") TipoAreaDTO tipoArea, HttpSession session, HttpServletRequest request,HttpServletResponse response,
      RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    TipoAreaDTO tipoAreaDTO = tipoArea;
    Errors errors = new Errors();

    if(tipoArea.getDescTipoArea()==null || "".equals(tipoArea.getDescTipoArea())) {
      errors.addError("descTipoArea", "Campo Obbligatorio");
    }
    if(tipoArea.getDettaglioTipoArea()==null || "".equals(tipoArea.getDettaglioTipoArea())) {
      errors.addError("dettaglioTipoArea", "Campo Obbligatorio");
    }
    if(tipoArea.getCodiceUfficiale()==null || "".equals(tipoArea.getCodiceUfficiale())) {
      errors.addError("codiceUfficiale", "Campo Obbligatorio");
    }
    if(tipoArea.getVelocita()==null || tipoArea.getVelocita().intValue()==0) {
      errors.addError("velocita", "Campo Obbligatorio");
    }   
    
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("tipoArea", tipoAreaDTO);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      if(tipoAreaDTO.getIdTipoArea()!=null && tipoAreaDTO.getIdTipoArea()>0)
        return "redirect:edit.do?idTipoArea="+tipoAreaDTO.getIdTipoArea();
      else
        return "redirect:showFilterAdd.do";
    }else {
        if (tipoArea.getDataInizioValidita() == null) {
          tipoArea.setDataInizioValidita(new Date());
        }
    
        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        tipoArea.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
    
        if (tipoArea.getIdTipoArea() == null || tipoArea.getIdTipoArea() == 0) {
          tipoAreaDTO = tipoAreaEJB.insertTipoArea(tipoArea);
        } else {
          tipoAreaDTO = tipoAreaEJB.findById(tipoAreaDTO.getIdTipoArea());
          tipoArea.setDataInizioValidita(tipoAreaDTO.getDataInizioValidita());
          if(tipoArea.isAnnoCorrente()) {
            tipoAreaEJB.updateTipoArea(tipoArea);
          }else {
            tipoAreaEJB.updateDataFineValidita(tipoArea.getIdTipoArea());
            tipoAreaDTO = tipoAreaEJB.insertTipoArea(tipoArea);
          }
        }
        tipoArea = tipoAreaEJB.findById(tipoArea.getIdTipoArea());
        model.addAttribute("tipoArea", tipoArea);
        
        Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
        
        model.addAttribute("success", "Tipo Area salvata con successo");
        setBreadcrumbs(model, request);
       // return this.showFilter(model, session, request,response);
        return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/tipoArea/search")
  public RedirectView search(Model model, @ModelAttribute("tipoArea") TipoAreaDTO tipoArea, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    List<TipoAreaDTO> list = tipoAreaEJB.findByFilter(tipoArea);
    attributes.addFlashAttribute("list", list);
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/tipoArea/edit")
  public String edit(Model model, HttpSession session, @RequestParam(value = "idTipoArea") String idTipoArea, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    TipoAreaDTO dto = (TipoAreaDTO) model.asMap().get("tipoArea");

    if(dto==null)
      dto = tipoAreaEJB.findById(Integer.decode(idTipoArea));

    model.addAttribute("tipoArea", dto);
    List<TipoAreaDTO> list = new ArrayList<TipoAreaDTO>();
    list.add(dto);
    model.addAttribute("list", list);
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 

    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoAreaAdd";
  }

  @RequestMapping(value = "/tipoArea/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idTipoArea") Integer idTipoArea, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    TipoAreaDTO tipoAreaDTO = null;
    try
    {
      tipoAreaDTO = tipoAreaEJB.findById(idTipoArea);
      if(!tipoAreaDTO.isAnnoCorrente()) {
        tipoAreaEJB.updateDataFineValidita(idTipoArea);
      }else {
        tipoAreaEJB.remove(idTipoArea);
      }
    }
    catch (InternalUnexpectedException e)
    {
      if (e.getCause() instanceof DataIntegrityViolationException)
      {
        tipoAreaEJB.updateDataFineValidita(idTipoArea);
      }
      else
      {
        attributes.addFlashAttribute("error", "Si è verificato un errore inaspettato durante l'eliminazione del tipo area");
        logger.debug("Errore nell'eliminazione del tipo area con id = " + idTipoArea + " : " + e.getMessage());
      }
    }
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/rest/tipo-aree", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<?> getTipoAreaJsonOutput()
  {
    List<TipoArea> lista = null;
    try
    {
      lista = tipoAreaEJB.getTipoAree();
      if (lista == null)
        lista = new ArrayList<TipoArea>();
    } catch (Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return new ResponseEntity<Map<String,String>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<TipoArea>>(lista,HttpStatus.OK);
  }

  @RequestMapping(value = "/tipoArea/getVelocitaJson", produces = "application/json")
  @ResponseBody
  public String getVelocitaJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<VelocitaDTO> lista = tipoAreaEJB.findVelocita();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }

  @RequestMapping(value = "/tipoArea/getTipoAreaJson", produces = "application/json")
  @ResponseBody
  public String getTipoAreaJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<TipoAreaDTO> lista = tipoAreaEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }

  @RequestMapping(value = "/tipoArea/getTipoArea", produces = "application/json")
  @ResponseBody
  public String getTipoArea(@RequestBody String checkbox, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    
    List<TipoAreaDTO> lista = null;
    
    if (checkbox != null) {
      if (checkbox.toLowerCase().indexOf("true") > -1) {
        session.setAttribute("checkboxAllTipoAree", "true");
      } else {
        session.setAttribute("checkboxAllTipoAree", "false");
      }
    }
    if (session.getAttribute("checkboxAllTipoAree") != null) {
      if (((String) session.getAttribute("checkboxAllTipoAree")).equals("true")) {
          lista = tipoAreaEJB.findAll();
      } else {
          lista = tipoAreaEJB.findValidi();
      }
    } else {
        lista = tipoAreaEJB.findAll();
    }
    
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }
  
  @SuppressWarnings("unchecked")
  @Lazy
  @RequestMapping(value = "/tipoArea/filtri")
  public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableArea") == null || "{}".equals(filtroInSessione.get("tableArea"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableArea");
      } 
     filtroInSessione.put("tableArea", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/tipoArea/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableArea");
       return "gestionetabelle/confermaElimina";
  }

  @Lazy
  @RequestMapping(value = "/tipoArea/tipoAreaExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<TipoAreaDTO> elenco = tipoAreaEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"tipoArea.xls\"");
    
    return new ModelAndView("excelTipoAreaView", "elenco", elenco);
  }
}
