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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import it.csi.iuffi.iuffiweb.business.ITipoTrappolaEJB;
import it.csi.iuffi.iuffiweb.business.ITrappolaOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
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
public class TipoTrappoleController extends TabelleController
{

  @Autowired
  private ITipoTrappolaEJB tipoTrappolaEJB;

  @Autowired
  private ITrappolaOrganismoNocivoEJB trappolOnEJB;


  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/tipoTrappole/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes,HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
      model.addAttribute("errors",model.asMap().get("errors"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    
    setBreadcrumbs(model, request);
   // redirectAttributes.addFlashAttribute("model", model);
    return "gestionetabelle/tipoTrappole";
  }

  
  @RequestMapping(value = "/tipoTrappole/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    
    TipoTrappolaDTO tipoTrappola = (TipoTrappolaDTO) model.asMap().get("tipoTrappola");
    model.addAttribute("tipoTrappola", tipoTrappola);

    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoTrappoleAdd";
  }
  
  @RequestMapping(value = "/tipoTrappole/save")
  public String save(Model model, @ModelAttribute("tipoTrappola") TipoTrappolaDTO tipoTrappola, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    TipoTrappolaDTO tipoTrappolaDTO = tipoTrappola;
    Errors errors = new Errors();   

    if(tipoTrappola.getTipologiaTrappola()==null || "".equals(tipoTrappola.getTipologiaTrappola())) {
      errors.addError("tipologiaTrappola", "Campo Obbligatorio");
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("tipoTrappola", tipoTrappolaDTO);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      if(tipoTrappolaDTO.getIdTipoTrappola()!=null && tipoTrappolaDTO.getIdTipoTrappola()>0)
        return "redirect:edit.do?idTipoTrappola="+tipoTrappolaDTO.getIdTipoTrappola();
      else
        return "redirect:showFilterAdd.do";
    }
    else
    {
      if (tipoTrappola.getDataInizioValidita() == null) {
        tipoTrappola.setDataInizioValidita(new Date());
      }

      UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      tipoTrappola.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());

      if (tipoTrappola.getIdTipoTrappola() == null || tipoTrappola.getIdTipoTrappola() == 0) {
        tipoTrappolaDTO = tipoTrappolaEJB.insertTipoTrappola(tipoTrappola);
      }
      else
      {
        tipoTrappolaDTO = tipoTrappolaEJB.findById(tipoTrappolaDTO.getIdTipoTrappola());
        tipoTrappola.setDataInizioValidita(tipoTrappolaDTO.getDataInizioValidita());
        if(tipoTrappola.isAnnoCorrente()) {
          tipoTrappolaEJB.updateTipoTrappola(tipoTrappola);
        }else {
          List<TrappolaOnDTO> listaTrappole = trappolOnEJB.findByIdTrappola(tipoTrappola.getIdTipoTrappola());

          if (listaTrappole!=null && !listaTrappole.isEmpty()) {
            tipoTrappolaDTO = tipoTrappolaEJB.findById(tipoTrappolaDTO.getIdTipoTrappola());
            model.addAttribute("tipoTrappola", tipoTrappolaDTO);
            redirectAttributes.addFlashAttribute("error", "Il tipo trappola " + tipoTrappolaDTO.getTipologiaTrappola() + " non può essere modificato in quanto è referenziato dalla tabella ASSOCIAZIONE TRAPPOLA-ORGANISMO NOCIVO");
            redirectAttributes.addFlashAttribute("model", model);
            return "redirect:edit.do?idTipoTrappola=" + tipoTrappolaDTO.getIdTipoTrappola();
          }
          tipoTrappolaEJB.updateDataFineValidita(tipoTrappola.getIdTipoTrappola());
          tipoTrappolaDTO = tipoTrappolaEJB.insertTipoTrappola(tipoTrappola); 
        }
      }

      tipoTrappolaDTO = tipoTrappolaEJB.findById(tipoTrappolaDTO.getIdTipoTrappola());
      model.addAttribute("tipoTrappola", tipoTrappolaDTO);

      List<TipoTrappolaDTO> list = tipoTrappolaEJB.findAll();
      redirectAttributes.addFlashAttribute("list", list);

      Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
      session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
      setBreadcrumbs(model, request);
      model.addAttribute("success", "Tipologia Trappola salvata con successo");                        
      //return this.showFilter(model, session, request,response);
      return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/tipoTrappole/search")
  public String search(Model model, @ModelAttribute("tipoTrappola") TipoTrappolaDTO tipoTrappola, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<TipoTrappolaDTO> list = tipoTrappolaEJB.findByFilter(tipoTrappola);
    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoTrappole";
  }

  @RequestMapping(value = "/tipoTrappole/edit")
  public String edit(Model model,HttpSession session, @RequestParam(value = "idTipoTrappola") String idTipoTrappola,HttpServletResponse response, HttpServletRequest request) throws InternalUnexpectedException
  {
    TipoTrappolaDTO dto = (TipoTrappolaDTO) model.asMap().get("tipoTrappola");

    if(dto==null)
        dto = tipoTrappolaEJB.findById(Integer.decode(idTipoTrappola));
    
    model.addAttribute("tipoTrappola", dto);
    List<TipoTrappolaDTO> list = new ArrayList<TipoTrappolaDTO>();
    list.add(dto);
    model.addAttribute("list", list);

    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 

    setBreadcrumbs(model, request);
    return "gestionetabelle/tipoTrappoleAdd";
  }

  @RequestMapping(value = "/tipoTrappole/remove")
  public String remove(Model model, @RequestParam(value = "idTipoTrappola") Integer idTipoTrappola, 
      HttpServletRequest request, RedirectAttributes redirectAttributes,HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    TipoTrappolaDTO tipoTrappolaDTO = null;
    try
    {
      tipoTrappolaDTO = tipoTrappolaEJB.findById(idTipoTrappola);
      
      List<TrappolaOnDTO> list = trappolOnEJB.findByIdTrappola(idTipoTrappola);
      if (list != null && list.size() > 0) {
        TipoTrappolaDTO dto = tipoTrappolaEJB.findById(idTipoTrappola);
        redirectAttributes.addFlashAttribute("error", "Il tipo trappola " + dto.getTipologiaTrappola() + " non può essere eliminato in quanto referenziato dalla tabella ASSOCIAZIONE TRAPPOLA-ORGANISMO NOCIVO");
      }else {
        if(!tipoTrappolaDTO.isAnnoCorrente()) {
          tipoTrappolaEJB.updateDataFineValidita(idTipoTrappola);
        }else {
          tipoTrappolaEJB.remove(idTipoTrappola);
        }
      }
    }
    catch (InternalUnexpectedException e)
    {
        if (e.getCause() instanceof DataIntegrityViolationException) {
          tipoTrappolaEJB.updateDataFineValidita(idTipoTrappola);          
        }
        else {
          redirectAttributes.addFlashAttribute("error", "Si è verificato un errore inaspettato durante l'eliminazione del tipo trappola");
          logger.debug("Errore nell'eliminazione del tipo trappola con id = " + idTipoTrappola + " : " + e.getMessage());
        }
    }
    return "redirect:showFilter.do";
  }

  @RequestMapping(value = "/rest/tipo-trappole", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<TipoTrappolaDTO>> getTipoTrappolaJsonOutput() throws InternalUnexpectedException
  {
    List<TipoTrappolaDTO> lista = tipoTrappolaEJB.findAll();
    if (lista == null)
      lista = new ArrayList<TipoTrappolaDTO>();
    return new ResponseEntity<List<TipoTrappolaDTO>>(lista,HttpStatus.OK);
  }
  
  @RequestMapping(value = "/tipoTrappole/getTipoTrappoleJson", produces = "application/json")
  @ResponseBody
  public String getTipoTrappoleJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<TipoTrappolaDTO> lista = tipoTrappolaEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }
  
  
  @RequestMapping(value = "/tipoTrappole/getTipoTrappole", produces = "application/json")
  @ResponseBody
  public String getTipoTrappole(@RequestBody String checkbox, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    
    List<TipoTrappolaDTO> lista = null;
    
    if (checkbox != null) {
      if (checkbox.toLowerCase().indexOf("true") > -1) {
        session.setAttribute("checkboxAllTrappole", "true");
      } else {
        session.setAttribute("checkboxAllTrappole", "false");
      }
    }
    if (session.getAttribute("checkboxAllTrappole") != null) {
      if (((String) session.getAttribute("checkboxAllTrappole")).equals("true")) {
          lista = tipoTrappolaEJB.findAll();
      } else {
          lista = tipoTrappolaEJB.findValidi();
      }
    } else {
        lista = tipoTrappolaEJB.findAll();
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
  @RequestMapping(value = "/tipoTrappole/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    @SuppressWarnings("unchecked")
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableTipoTrappole") == null || "{}".equals(filtroInSessione.get("tableTipoTrappole"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableTipoTrappole");
      } 
     filtroInSessione.put("tableTipoTrappole", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/tipoTrappole/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableTipoTrappole");
       return "gestionetabelle/confermaElimina";
  }



  @Lazy
  @RequestMapping(value = "/tipoTrappole/tipoTrappoleExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<TipoTrappolaDTO> elenco = tipoTrappolaEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"tipoTrappole.xls\"");
    
    return new ModelAndView("excelTipoTrappolaView", "elenco", elenco);
  }

}
