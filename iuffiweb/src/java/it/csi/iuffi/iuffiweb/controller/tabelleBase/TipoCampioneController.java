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

import it.csi.iuffi.iuffiweb.business.IRiepilogoMonitoraggioEJB;
import it.csi.iuffi.iuffiweb.business.ITipoCampioneEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.api.RiepilogoMonitoraggio;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller

@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class TipoCampioneController extends TabelleController
{
 
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  
  @Autowired
  private ITipoCampioneEJB tipoCampioneEJB;
  
  @Autowired
  private IRiepilogoMonitoraggioEJB riepilogoEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/tipoCampione/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    //filtri
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoCampione";
  }

  @RequestMapping(value = "/tipoCampione/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {

    TipoCampioneDTO tipoCampione = (TipoCampioneDTO) model.asMap().get("tipoCampione");
    model.addAttribute("tipoCampione", tipoCampione);

    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoCampioneAdd";
  }
  
  @RequestMapping(value = "/tipoCampione/save")
  public String save(Model model, @ModelAttribute("tipoCampione") TipoCampioneDTO tipoCampione, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    TipoCampioneDTO tipoCampioneDTO = tipoCampione;
    Errors errors = new Errors();   
    if(tipoCampione.getTipologiaCampione()==null || "".equals(tipoCampione.getTipologiaCampione())) {
      errors.addError("tipologiaCampione", "Campo Obbligatorio");
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("tipoCampione", tipoCampioneDTO);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      if(tipoCampioneDTO.getIdTipoCampione()!=null && tipoCampioneDTO.getIdTipoCampione()>0)
        return "redirect:edit.do?idTipoCampione="+tipoCampioneDTO.getIdTipoCampione();
      else
        return "redirect:showFilterAdd.do";
    }
    else
    {
        if (tipoCampione.getDataInizioValidita() == null) {
          tipoCampione.setDataInizioValidita(new Date());
        }
    
        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        tipoCampione.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
        
        if (tipoCampione.getIdTipoCampione() == null || tipoCampione.getIdTipoCampione() == 0) {
          tipoCampioneDTO = tipoCampioneEJB.insertTipoCampione(tipoCampione);
        }
        else
        {
          tipoCampioneDTO = tipoCampioneEJB.findById(tipoCampioneDTO.getIdTipoCampione());
          tipoCampione.setDataInizioValidita(tipoCampioneDTO.getDataInizioValidita());
          if(tipoCampione.isAnnoCorrente()) {
            tipoCampioneEJB.updateTipoCampione(tipoCampione);
          }else {
          //se viene richiesta la modifica del record..la storicizzo e inserisco il nuovo
            List<RiepilogoMonitoraggio> list = riepilogoEJB.findByIdTipoCampione(tipoCampione.getIdTipoCampione());
            if (list!=null && !list.isEmpty()) {
              tipoCampione = tipoCampioneEJB.findById(tipoCampione.getIdTipoCampione());
              model.addAttribute("tipoCampione", tipoCampione);
              redirectAttributes.addFlashAttribute("error", "Il tipo campione " + tipoCampione.getTipologiaCampione() + " non può essere modificato in quanto è referenziato dalla tabella COMPATIBILITA' SPECIE/CAMPIONE/PERIODO/ORGANISMO NOCIVO");
              redirectAttributes.addFlashAttribute("model", model);
              return "redirect:edit.do?idTipoCampione="+tipoCampione.getIdTipoCampione();
            }
            tipoCampioneEJB.updateDataFineValidita(tipoCampione.getIdTipoCampione());
            tipoCampioneDTO = tipoCampioneEJB.insertTipoCampione(tipoCampione); 
          }
        }
        tipoCampioneDTO = tipoCampioneEJB.findById(tipoCampioneDTO.getIdTipoCampione());
        model.addAttribute("tipoCampione", tipoCampioneDTO);
        return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/tipoCampione/search")
  public RedirectView search(Model model, @ModelAttribute("tipoCampione") TipoCampioneDTO tipoCampione, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    List<TipoCampioneDTO> list = tipoCampioneEJB.findByFilter(tipoCampione);
    attributes.addFlashAttribute("list", list);
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/tipoCampione/edit")
  public String edit(Model model,HttpSession session, @RequestParam(value = "idTipoCampione") String idTipoCampione, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    
    TipoCampioneDTO dto = (TipoCampioneDTO) model.asMap().get("tipoCampione");

    if(dto==null)
      dto = tipoCampioneEJB.findById(Integer.decode(idTipoCampione));
     
    model.addAttribute("tipoCampione", dto);
    List<TipoCampioneDTO> list = new ArrayList<TipoCampioneDTO>();
    list.add(dto);
    model.addAttribute("list", list);
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoCampioneAdd";
  }

  @RequestMapping(value = "/tipoCampione/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idTipoCampione") Integer idTipoCampione, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    TipoCampioneDTO tipoCampioneDTO = null;
    try
    {
      tipoCampioneDTO = tipoCampioneEJB.findById(idTipoCampione);
      
      List<RiepilogoMonitoraggio> list = riepilogoEJB.findByIdTipoCampione(idTipoCampione);
      if (list!=null && !list.isEmpty()) {
        TipoCampioneDTO tipoCampione = tipoCampioneEJB.findById(idTipoCampione);
        model.addAttribute("tipoCampione", tipoCampione);
        attributes.addFlashAttribute("error", "Il tipo campione " + tipoCampione.getTipologiaCampione() + " non può essere eliminato in quanto è referenziato dalla tabella COMPATIBILITA' SPECIE/CAMPIONE/PERIODO/ORGANISMO NOCIVO");
        attributes.addFlashAttribute("model", model);
      } else {
        if(!tipoCampioneDTO.isAnnoCorrente()) {
          tipoCampioneEJB.updateDataFineValidita(idTipoCampione);
        }else {
          tipoCampioneEJB.remove(idTipoCampione);
        }
      }
    }
    catch (InternalUnexpectedException e)
    {
      if (e.getCause() instanceof DataIntegrityViolationException) {
        tipoCampioneEJB.updateDataFineValidita(idTipoCampione);
      }
      else {
        attributes.addFlashAttribute("error", "Si è verificato un errore inaspettato durante l'eliminazione del tipo campione");
        logger.debug("Errore nell'eliminazione del tipo campione con id = " + idTipoCampione + " : " + e.getMessage());
      }
    }
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/rest/tipo-campioni", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<?> getTipoCampioneJsonOutput()
  {
    List<TipoCampioneDTO> lista = null;
    try
    {
      lista = tipoCampioneEJB.findAll();
      if (lista == null)
        lista = new ArrayList<TipoCampioneDTO>();
    } catch (Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return new ResponseEntity<Map<String,String>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<TipoCampioneDTO>>(lista,HttpStatus.OK);
  }

  @RequestMapping(value = "/tipoCampione/getTipoCampioneJson", produces = "application/json")
  @ResponseBody
  public String getTipoCampioneJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<TipoCampioneDTO> lista = tipoCampioneEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }
 

  @RequestMapping(value = "/tipoCampione/getTipoCampione", produces = "application/json")
  @ResponseBody
  public String getTipoCampione(@RequestBody String checkbox, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    
    List<TipoCampioneDTO> lista = null;
    
    if (checkbox != null) {
      if (checkbox.toLowerCase().indexOf("true") > -1) {
        session.setAttribute("checkboxAllTipoCampioni", "true");
      } else {
        session.setAttribute("checkboxAllTipoCampioni", "false");
      }
    }
    if (session.getAttribute("checkboxAllTipoCampioni") != null) {
      if (((String) session.getAttribute("checkboxAllTipoCampioni")).equals("true")) {
          lista = tipoCampioneEJB.findAll();
      } else {
          lista = tipoCampioneEJB.findValidi();
      }
    } else {
        lista = tipoCampioneEJB.findAll();
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
  @RequestMapping(value = "/tipoCampione/filtri")
  public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);

    if(filtroInSessione == null) {
      filtroInSessione = new HashMap<String, String>();
      session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
    }
    String filtroInit;
    if(filtroInSessione.get("tableTipoCampione") == null || "{}".equals(filtroInSessione.get("tableTipoCampione"))) {
      filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
    } else {
      filtroInit=filtroInSessione.get("tableTipoCampione");
    } 
    filtroInSessione.put("tableTipoCampione", filtroInit);
    return filtroInSessione;
  }

  @RequestMapping(value = "/tipoCampione/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableTipoCampione");
      return "gestionetabelle/confermaElimina";
  }


  @Lazy
  @RequestMapping(value = "/tipoCampione/tipoCampioneExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<TipoCampioneDTO> elenco = tipoCampioneEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"tipoCampione.xls\"");
    
    return new ModelAndView("excelTipoCampioniView", "elenco", elenco);
  }
  
  
}
