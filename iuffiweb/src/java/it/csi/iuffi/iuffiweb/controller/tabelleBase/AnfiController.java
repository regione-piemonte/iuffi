package it.csi.iuffi.iuffiweb.controller.tabelleBase;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import it.csi.iuffi.iuffiweb.business.IAnfiEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnfiDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class AnfiController extends TabelleController
{
 
  @Autowired
  private IAnfiEJB anfiEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/anfi/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
        
    setBreadcrumbs(model, request);
    return "gestionetabelle/anfi";
  }

  @RequestMapping(value = "/anfi/save")
  public String save(Model model, @ModelAttribute("anfi") AnfiDTO anfi, HttpSession session, HttpServletRequest request,  HttpServletResponse response,
      RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    AnfiDTO anfiDTO = anfi;
    Errors errors = new Errors();   

    if(anfi.getTipologiaTestDiLaboratorio()==null || "".equals(anfi.getTipologiaTestDiLaboratorio())) {
      errors.addError("tipologiaTestDiLaboratorio", "Campo Obbligatorio");
    }
    if(anfi.getCosto()==null || anfi.getCosto().intValue()==0) {
      errors.addError("costo", "Campo Obbligatorio");
    }

    setBreadcrumbs(model, request);
    
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("anfi", anfiDTO);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      if(anfiDTO.getIdAnfi()!=null && anfiDTO.getIdAnfi()>0)
        return "redirect:edit.do?idAnfi="+anfiDTO.getIdAnfi();
      else
        return "redirect:showFilterAdd.do";
    }else {

        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        anfi.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
    
        if (anfi.getIdAnfi() == null || anfi.getIdAnfi() == 0) {
          anfiDTO = anfiEJB.insertAnfi(anfi);
        } else {
          anfiDTO = anfiEJB.findById(anfiDTO.getIdAnfi());
          anfi.setDataInizioValidita(anfiDTO.getDataInizioValidita());
          if(anfi.isAnnoCorrente()) {
            anfiEJB.updateAnfi(anfi);
          }else {
            anfiEJB.updateDataFineValidita(anfi.getIdAnfi());
            anfiDTO = anfiEJB.insertAnfi(anfi);
          }
        }
        anfiDTO = anfiEJB.findById(anfiDTO.getIdAnfi());
        model.addAttribute("anfi", anfiDTO);
        
        List<AnfiDTO> list = anfiEJB.findAll();
        redirectAttributes.addFlashAttribute("list", list);
 
        Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
        

        model.addAttribute("success", "Tipologia Test di Laboratorio salvata con successo");        
        //return this.showFilter(model, session, request,response);
        return "redirect:showFilter.do";
    }   
  }

  
  
  @RequestMapping(value = "/anfi/search")
  public String search(Model model, @ModelAttribute("anfi") AnfiDTO anfi, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<AnfiDTO> list = anfiEJB.findAll();
    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/anfi";
  }

  @RequestMapping(value = "/anfi/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    AnfiDTO anfi = (AnfiDTO) model.asMap().get("anfi");
    model.addAttribute("anfi", anfi);

    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
 
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/anfiAdd";
  }

  @RequestMapping(value = "/anfi/edit")
  public String edit(Model model,HttpSession session, @RequestParam(value = "idAnfi") String idAnfi, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    
    AnfiDTO dto = (AnfiDTO) model.asMap().get("anfi");

    if(dto==null)
      dto = anfiEJB.findById(Integer.decode(idAnfi));

    
    model.addAttribute("anfi", dto);
    List<AnfiDTO> list = new ArrayList<AnfiDTO>();
    list.add(dto);
    model.addAttribute("list", list);
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
    
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/anfiAdd";
  }

  @RequestMapping(value = "/anfi/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idAnfi") Integer idAnfi, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    AnfiDTO anfiDTO = null;
    try {
      anfiDTO = anfiEJB.findById(idAnfi);
      if(!anfiDTO.isAnnoCorrente()) {
        anfiEJB.updateDataFineValidita(idAnfi);
      }else {
        anfiEJB.remove(idAnfi);
      }
    }
    catch (InternalUnexpectedException e)
    {
      if (e.getCause() instanceof DataIntegrityViolationException)
      {
        anfiEJB.updateDataFineValidita(idAnfi);
      }
      else
      {
        attributes.addFlashAttribute("error", "Si è verificato un errore inaspettato durante l'eliminazione della tipologia test di laboratorio");
        logger.debug("Errore nell'eliminazione della tipologia test di laboratorio con id = " + idAnfi + " : " + e.getMessage());
      }
    }
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/rest/anfi", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<AnfiDTO>> getAnfiJsonOutput() throws InternalUnexpectedException
  {
    List<AnfiDTO> lista = anfiEJB.findAll();
    if (lista == null)
      lista = new ArrayList<AnfiDTO>();
    return new ResponseEntity<List<AnfiDTO>>(lista,HttpStatus.OK);
  }

  @RequestMapping(value = "/anfi/getAnfiJson", produces = "application/json")
  @ResponseBody
  public /*List<AnagraficaDTO>*/String getAnfiJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    List<AnfiDTO> lista = anfiEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }

    return obj;
  }
  
  @RequestMapping(value = "/anfi/getAnfi", produces = "application/json")
  @ResponseBody
  public String getAnfi(@RequestBody String checkbox, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    
    List<AnfiDTO> lista = null;
    
    if (checkbox != null) {
      if (checkbox.toLowerCase().indexOf("true") > -1) {
        session.setAttribute("checkboxAllTipologiaTest", "true");
      } else {
        session.setAttribute("checkboxAllTipologiaTest", "false");
      }
    }
    if (session.getAttribute("checkboxAllTipologiaTest") != null) {
      if (((String) session.getAttribute("checkboxAllTipologiaTest")).equals("true")) {
          lista = anfiEJB.findAll();
      } else {
          lista = anfiEJB.findValidi();
      }
    } else {
        lista = anfiEJB.findAll();
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
  @RequestMapping(value = "/anfi/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableAnfi") == null || "{}".equals(filtroInSessione.get("tableAnfi"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableAnfi");
      } 
     filtroInSessione.put("tableAnfi", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/anfi/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableAnfi");
       return "gestionetabelle/confermaElimina";
  }

  @Lazy
  @RequestMapping(value = "/anfi/anfiExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<AnfiDTO> elenco = anfiEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"testLaboratorio.xls\"");
    
    return new ModelAndView("excelAnfiView", "elenco", elenco);
  }

}
