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

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.business.ITipoTrappolaEJB;
import it.csi.iuffi.iuffiweb.business.ITrappolaEnteEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.EnteDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaEnteDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping(value = "")
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class TrappolaEnteController extends TabelleController
{

  @Autowired
  private ITipoTrappolaEJB tipoTrappolaEJB;

  @Autowired
  private ITrappolaEnteEJB trappolaEnteEJB;

  @Autowired
  private IAnagraficaEJB anagraficaEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/trappolaEnte/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      

    setBreadcrumbs(model, request);
    return "gestionetabelle/trappolaEnte";
  }

  
  @RequestMapping(value = "/trappolaEnte/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
    
    List<TipoTrappolaDTO> listaTrappole = tipoTrappolaEJB.findValidi();
    model.addAttribute("listaTrappole", listaTrappole);

    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      

    setBreadcrumbs(model, request);
    return "gestionetabelle/trappolaEnteAdd";
  }

  @RequestMapping(value = "/trappolaEnte/save")
  public String save(Model model, @ModelAttribute("trappolaEnte") TrappolaEnteDTO trappolaEnte, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    TrappolaEnteDTO trappolaEnteDTO = trappolaEnte;
    Errors errors = new Errors();   
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
    
    List<TipoTrappolaDTO> listaTrappole = tipoTrappolaEJB.findAll();
    model.addAttribute("listaTrappole", listaTrappole);

    if(trappolaEnte.getIdTrappola()==null || trappolaEnte.getIdTrappola()==0) {
      errors.addError("idTrappola", "Campo Obbligatorio");
    }

    if(trappolaEnte.getIdEnte()==null || trappolaEnte.getIdEnte()==0) {
      errors.addError("idEnte", "Campo Obbligatorio");
    }

    if(trappolaEnte.getCosto()==null || trappolaEnte.getCosto().intValue()==0) {
      errors.addError("costo", "Campo Obbligatorio");
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return this.showFilterAdd(model, session, request,response);
    }else {
        if (trappolaEnte.getDataInizioValidita() == null) {
          trappolaEnte.setDataInizioValidita(new Date());
        }
    
        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        trappolaEnte.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
        
        if (trappolaEnte.getIdTrappolaEnte() == null || trappolaEnte.getIdTrappolaEnte() == 0) {
          trappolaEnteDTO = trappolaEnteEJB.insert(trappolaEnte);
        }else {
          trappolaEnteEJB.update(trappolaEnte);
          trappolaEnteDTO = trappolaEnteEJB.insert(trappolaEnte);
        }
        trappolaEnteDTO = trappolaEnteEJB.findById(trappolaEnteDTO.getIdTrappolaEnte());
        model.addAttribute("trappolaEnte", trappolaEnteDTO);
        
        List<TrappolaEnteDTO> list = trappolaEnteEJB.findAll();
        attributes.addFlashAttribute("list", list);
        
        Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
        setBreadcrumbs(model, request);
        model.addAttribute("success", "Associazione Trappola Ente salvata con successo");              
        //return this.showFilter(model, session, request,response);
        return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/trappolaEnte/search")
  public String search(Model model, @ModelAttribute("trappolaEnte") TrappolaEnteDTO trappolaEnte, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<TrappolaEnteDTO> list = trappolaEnteEJB.findByFilter(trappolaEnte);
    model.addAttribute("list", list);
    
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
    
    List<TipoTrappolaDTO> listaTrappole = tipoTrappolaEJB.findAll();
    model.addAttribute("listaTrappole", listaTrappole);

    setBreadcrumbs(model, request);
    return "gestionetabelle/trappolaEnte";
  }

  @RequestMapping(value = "/trappolaEnte/edit")
  public String edit(Model model, HttpSession session,@RequestParam(value = "id") String id, HttpServletRequest request,HttpServletResponse response) throws InternalUnexpectedException
  {
    TrappolaEnteDTO dto = trappolaEnteEJB.findById(Integer.decode(id));
    model.addAttribute("trappolaEnte", dto);
    
    List<TrappolaEnteDTO> list = new ArrayList<TrappolaEnteDTO>();
    list.add(dto);
    model.addAttribute("list", list);
    
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
    
    List<TipoTrappolaDTO> listaTrappole = tipoTrappolaEJB.findAll();
    model.addAttribute("listaTrappole", listaTrappole);
 
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 

    setBreadcrumbs(model, request);
    return "gestionetabelle/trappolaEnteAdd";
  }

  @RequestMapping(value = "/trappolaEnte/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idTrappolaEnte") String idTrappolaEnte, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
   
    try {
      trappolaEnteEJB.remove(Integer.decode(idTrappolaEnte));
    }catch (InternalUnexpectedException e) {
      boolean isFound = e.getCause().getMessage().indexOf("ORA-02292") !=-1? true: false; //true
      if(isFound) {
        trappolaEnteEJB.updateDataFineValidita(Integer.decode(idTrappolaEnte));
      }
    }      

    List<TrappolaEnteDTO> list = trappolaEnteEJB.findAll();
    attributes.addFlashAttribute("list", list);
    
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/rest/trappolaEnteJsonOutput", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<TrappolaEnteDTO>> getTipoTrappolaJsonOutput() throws InternalUnexpectedException
  {
    List<TrappolaEnteDTO> lista = trappolaEnteEJB.findAll();
    return new ResponseEntity<List<TrappolaEnteDTO>>(lista,HttpStatus.OK);
  }

  
  @RequestMapping(value = "/trappolaEnte/getTrappolaEnteJson", produces = "application/json")
  @ResponseBody
  public String getTrappolaEnteJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<TrappolaEnteDTO> lista = trappolaEnteEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }

    return obj;
  }
  
  @Lazy
  @RequestMapping(value = "/trappolaEnte/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableTrappolaEnte") == null || "{}".equals(filtroInSessione.get("tableTrappolaEnte"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableTrappolaEnte");
      } 
     filtroInSessione.put("tableTrappolaEnte", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/trappolaEnte/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableTrappolaEnte");
      return "gestionetabelle/confermaElimina";
  }

 
  @Lazy
  @RequestMapping(value = "/trappolaEnte/trappolaEnteExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<TrappolaEnteDTO> elenco = trappolaEnteEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"trappoleEnte.xls\"");
    
    return new ModelAndView("excelTrappolaEnteView", "elenco", elenco);
  }

}
