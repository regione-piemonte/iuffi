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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.ITipoTrappolaEJB;
import it.csi.iuffi.iuffiweb.business.ITrappolaOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaOnDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping(value = "")
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class TrappolaOnController extends TabelleController
{

  @Autowired
  private ITipoTrappolaEJB tipoTrappolaEJB;

  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;

  @Autowired
  private ITrappolaOrganismoNocivoEJB trappolaOnEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/trappolaOn/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }

    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    

    setBreadcrumbs(model, request);
    return "gestionetabelle/trappolaOn";
  }

  @RequestMapping(value = "/trappolaOn/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {

    TrappolaOnDTO trappolaOn = (TrappolaOnDTO) model.asMap().get("trappolaOn");
    model.addAttribute("trappolaOn", trappolaOn);

    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listaOn", listaOn);
    
    List<TipoTrappolaDTO> listaTrappole = tipoTrappolaEJB.findValidi();
    model.addAttribute("listaTrappole", listaTrappole);

    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      

    setBreadcrumbs(model, request);
    return "gestionetabelle/trappolaOnAdd";
  }

  
  @RequestMapping(value = "/trappolaOn/save")
  public String save(Model model, @ModelAttribute("trappolaOn") TrappolaOnDTO trappolaOn, HttpSession session,HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {

    List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listaOn", listaOn);
    
    List<TipoTrappolaDTO> listaTrappole = tipoTrappolaEJB.findValidi();
    model.addAttribute("listaTrappole", listaTrappole);

    TrappolaOnDTO trappolaOnDTO = trappolaOn;
    Errors errors = new Errors();   
    if(trappolaOn.getIdTrappola()==null || trappolaOn.getIdTrappola()==0) {
      errors.addError("idTrappola", "Campo Obbligatorio");
    }
    if(trappolaOn.getIdOn()==null || trappolaOn.getIdOn()==0) {
      errors.addError("idOn", "Campo Obbligatorio");
    }
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("trappolaOn", trappolaOnDTO);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      if(trappolaOnDTO.getIdTrappolaOn()!=null && trappolaOnDTO.getIdTrappolaOn()>0)
        return "redirect:edit.do?id="+trappolaOnDTO.getIdTrappolaOn();
      else
        return "redirect:showFilterAdd.do";
    }else {
        if (trappolaOn.getDataInizioValidita() == null) {
          trappolaOn.setDataInizioValidita(new Date());
        }
    
        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        trappolaOn.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
        try {
            if (trappolaOn.getIdTrappolaOn() == null || trappolaOn.getIdTrappolaOn() == 0) {
              trappolaOnDTO = trappolaOnEJB.insert(trappolaOn);
            } else {
              trappolaOnDTO = trappolaOnEJB.findById(trappolaOnDTO.getIdTrappolaOn());
              trappolaOn.setDataInizioValidita(trappolaOnDTO.getDataInizioValidita());
              if(trappolaOn.isAnnoCorrente()) {
                trappolaOnEJB.update(trappolaOn);
              }else {
                trappolaOnEJB.updateDataFineValidita(trappolaOn.getIdTrappolaOn());
                trappolaOnDTO = trappolaOnEJB.insert(trappolaOn);
              }
            }
            trappolaOnDTO = trappolaOnEJB.findById(trappolaOnDTO.getIdTrappolaOn());       
            model.addAttribute("trappolaOn", trappolaOnDTO);
        
        } catch (InternalUnexpectedException e) {
          boolean isFound = e.getCause().getMessage().indexOf("ORA-00001") !=-1? true: false; 
          if(isFound) {
              //model.addAttribute("error", "Codice Fiscale già registrato");
              errors.addError("idOn", "Coppia trappola-organismo nocivo già esistente");
              errors.addError("idTrappola", "Coppia trappola-organismo nocivo già esistente");
          } else
              model.addAttribute("error", "Errore in fase di inserimento");
          redirectAttributes.addFlashAttribute("trappolaOn", trappolaOnDTO);
          redirectAttributes.addFlashAttribute("errors", errors);
          redirectAttributes.addFlashAttribute("success", null);
          redirectAttributes.addFlashAttribute("model", model);
          return "redirect:showFilterAdd.do";
        }

        List<TrappolaOnDTO> list = trappolaOnEJB.findAll();
        redirectAttributes.addFlashAttribute("list", list);
        
        Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
        setBreadcrumbs(model, request);
        model.addAttribute("success", "Associazione Trappola Organismo Nocivo salvata con successo");                      
        //return this.showFilter(model, session, request,response);
        return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/trappolaOn/search")
  public String search(Model model, @ModelAttribute("trappolaOn") TrappolaOnDTO trappolaOn, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<TrappolaOnDTO> list = trappolaOnEJB.findByFilter(trappolaOn);
    model.addAttribute("list", list);
    
    List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findAll();
    model.addAttribute("listaOn", listaOn);
    
    List<TipoTrappolaDTO> listaTrappole = tipoTrappolaEJB.findAll();
    model.addAttribute("listaTrappole", listaTrappole);
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/trappolaOn";
  }

  @RequestMapping(value = "/trappolaOn/edit")
  public String edit(Model model, HttpSession session,@RequestParam(value = "id") String id, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    TrappolaOnDTO dto = (TrappolaOnDTO) model.asMap().get("trappolaOn");

    if(dto==null)
      dto = trappolaOnEJB.findById(Integer.decode(id));
     
    model.addAttribute("trappolaOn", dto);
    
    List<TrappolaOnDTO> list = new ArrayList<TrappolaOnDTO>();
    list.add(dto);
    model.addAttribute("list", list);

    List<OrganismoNocivoDTO> listaOn;
    List<TipoTrappolaDTO> listaTrappole;
    if(dto.getDataFineValidita()==null) {
      listaOn = organismoNocivoEJB.findValidi();      
      listaTrappole = tipoTrappolaEJB.findValidi();
     }else{
       listaOn = organismoNocivoEJB.findAll();      
       listaTrappole = tipoTrappolaEJB.findAll();
     }   
    model.addAttribute("listaOn", listaOn);
    model.addAttribute("listaTrappole", listaTrappole);      

    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 

    setBreadcrumbs(model, request);
    return "gestionetabelle/trappolaOnAdd";
  }

  @RequestMapping(value = "/trappolaOn/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idTrappolaOn") String idTrappolaOn, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    TrappolaOnDTO trappolaOnDTO = null;
    try
    {
      trappolaOnDTO = trappolaOnEJB.findById(Integer.decode(idTrappolaOn));
      
      if(!trappolaOnDTO.isAnnoCorrente()) {
        trappolaOnEJB.updateDataFineValidita(Integer.decode(idTrappolaOn));
      }else {
        trappolaOnEJB.remove(Integer.decode(idTrappolaOn));
      }
    }
    catch (InternalUnexpectedException e)
    {
      attributes.addFlashAttribute("error", "Si è verificato un errore inaspettato durante l'eliminazione della coppia trappola-organismo");
      logger.debug("Errore nell'eliminazione della coppia trappola-organismo con id = " + idTrappolaOn + " : " + e.getMessage());
    }
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/rest/trappola-on", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<TrappolaOnDTO>> getTipoTrappolaJsonOutput() throws InternalUnexpectedException
  {
    List<TrappolaOnDTO> lista = trappolaOnEJB.findAll();
    if (lista == null)
      lista = new ArrayList<TrappolaOnDTO>();
    return new ResponseEntity<List<TrappolaOnDTO>>(lista,HttpStatus.OK);
  }
  
  @RequestMapping(value = "/trappolaOn/getTrappolaOnJson", produces = "application/json")
  @ResponseBody
  public String getTrappolaOnJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<TrappolaOnDTO> lista = trappolaOnEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }

    return obj;
  }
  
  @Lazy
  @RequestMapping(value = "/trappolaOn/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableTrappolaOn") == null || "{}".equals(filtroInSessione.get("tableTrappolaOn"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableTrappolaOn");
      } 
     filtroInSessione.put("tableTrappolaOn", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/trappolaOn/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableTrappolaOn");
       return "gestionetabelle/confermaElimina";
  }


  @Lazy
  @RequestMapping(value = "/trappolaOn/trappolaOnExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<TrappolaOnDTO> elenco = trappolaOnEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"trappoleOn.xls\"");
    
    return new ModelAndView("excelTrappolaOnView", "elenco", elenco);
  }

}
