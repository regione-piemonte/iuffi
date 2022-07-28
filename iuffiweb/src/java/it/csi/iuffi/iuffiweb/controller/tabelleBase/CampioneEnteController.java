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
import it.csi.iuffi.iuffiweb.business.ICampioneEnteEJB;
import it.csi.iuffi.iuffiweb.business.ITipoCampioneEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.CampioneEnteDTO;
import it.csi.iuffi.iuffiweb.model.EnteDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping(value = "")
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class CampioneEnteController extends TabelleController
{

  @Autowired
  private ITipoCampioneEJB tipoCampioneEJB;

  @Autowired
  private ICampioneEnteEJB campioneEnteEJB;

  @Autowired
  private IAnagraficaEJB anagraficaEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/campioneEnte/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      

    setBreadcrumbs(model, request);
    return "gestionetabelle/campioneEnte";
  }

  
  @RequestMapping(value = "/campioneEnte/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
    
    List<TipoCampioneDTO> listaCampioni = tipoCampioneEJB.findValidi();
    model.addAttribute("listaCampioni", listaCampioni);

    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      

    setBreadcrumbs(model, request);
    return "gestionetabelle/campioneEnteAdd";
  }

  @RequestMapping(value = "/campioneEnte/save")
  public String save(Model model, @ModelAttribute("campioneEnte") CampioneEnteDTO campioneEnte, HttpSession session, HttpServletRequest request, 
      HttpServletResponse response,RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    CampioneEnteDTO campioneEnteDTO = campioneEnte;
    Errors errors = new Errors();   
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
    
    List<TipoCampioneDTO> listaCampioni = tipoCampioneEJB.findAll();
    model.addAttribute("listaCampioni", listaCampioni);

    if(campioneEnte.getIdTipoCampione()==null || campioneEnte.getIdTipoCampione()==0) {
      errors.addError("idTipoCampione", "Campo Obbligatorio");
    }

    if(campioneEnte.getIdEnte()==null || campioneEnte.getIdEnte()==0) {
      errors.addError("idEnte", "Campo Obbligatorio");
    }

    if(campioneEnte.getCosto()==null || campioneEnte.getCosto().intValue()==0) {
      errors.addError("costo", "Campo Obbligatorio");
     }else if ((campioneEnte.getCosto()).scale() > 2){
      errors.addError("pagaOraria", "Formato previsto 00000000.00");
    }
    
    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return this.showFilterAdd(model, session, request,response);
    }else {
        if (campioneEnte.getDataInizioValidita() == null) {
          campioneEnte.setDataInizioValidita(new Date());
        }
    
        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        campioneEnte.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
        
        if (campioneEnte.getIdTipoCampioneEnte() == null || campioneEnte.getIdTipoCampioneEnte() == 0) {
          campioneEnteDTO = campioneEnteEJB.insert(campioneEnte);
        }else {
          campioneEnteEJB.update(campioneEnte);
          campioneEnteDTO = campioneEnteEJB.insert(campioneEnte);
        }
        campioneEnteDTO = campioneEnteEJB.findById(campioneEnteDTO.getIdTipoCampioneEnte());
        model.addAttribute("campioneEnte", campioneEnteDTO);
        
        List<CampioneEnteDTO> list = campioneEnteEJB.findAll();
        attributes.addFlashAttribute("list", list);
        Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
        
        model.addAttribute("success", "Associazione Campione Ente salvata con successo");              
        //return this.showFilter(model, session, request,response);
        return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/campioneEnte/search")
  public String search(Model model, @ModelAttribute("campioneEnte") CampioneEnteDTO campioneEnte, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<CampioneEnteDTO> list = campioneEnteEJB.findByFilter(campioneEnte);
    model.addAttribute("list", list);
    
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
    
    List<TipoCampioneDTO> listaCampioni = tipoCampioneEJB.findAll();
    model.addAttribute("listaCampioni", listaCampioni);

    setBreadcrumbs(model, request);
    return "gestionetabelle/campioneEnte";
  }

  
  @RequestMapping(value = "/campioneEnte/edit")
  public String edit(Model model,HttpSession session, @RequestParam(value = "id") String id, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    CampioneEnteDTO dto = campioneEnteEJB.findById(Integer.decode(id));
    model.addAttribute("campioneEnte", dto);
    
    List<CampioneEnteDTO> list = new ArrayList<CampioneEnteDTO>();
    list.add(dto);
    model.addAttribute("list", list);
    
    List<EnteDTO> listaEnti = anagraficaEJB.getEnti();
    model.addAttribute("listaEnti", listaEnti);
    
    List<TipoCampioneDTO> listaCampioni = tipoCampioneEJB.findAll();
    model.addAttribute("listaCampioni", listaCampioni);
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/campioneEnteAdd";
  }

  
  
  @RequestMapping(value = "/campioneEnte/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idCampioneEnte") String idCampioneEnte, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    
    try {
      campioneEnteEJB.remove(Integer.decode(idCampioneEnte));
    }catch (InternalUnexpectedException e) {
      boolean isFound = e.getCause().getMessage().indexOf("ORA-02292") !=-1? true: false; //true
      if(isFound) {
        campioneEnteEJB.updateDataFineValidita(Integer.decode(idCampioneEnte));
      }
    }      
   
    List<CampioneEnteDTO> list = campioneEnteEJB.findAll();
    attributes.addFlashAttribute("list", list);
    
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/rest/campioneEnteJsonOutput", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<CampioneEnteDTO>> getTipoTrappolaJsonOutput() throws InternalUnexpectedException
  {
    List<CampioneEnteDTO> lista = campioneEnteEJB.findAll();
    return new ResponseEntity<List<CampioneEnteDTO>>(lista,HttpStatus.OK);
  }

  
  @RequestMapping(value = "/campioneEnte/getCampioneEnteJson", produces = "application/json")
  @ResponseBody
  public String getCampioneEnteJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    List<CampioneEnteDTO> lista = campioneEnteEJB.findAll();
    String obj = mapper.writeValueAsString(lista);

    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }

    return obj;
  }
  
  @Lazy
  @RequestMapping(value = "/campioneEnte/campioneEnteExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<CampioneEnteDTO> elenco = campioneEnteEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"campioniEnte.xls\"");
    
    return new ModelAndView("excelCampioneEnteView", "elenco", elenco);
  }

  
  @Lazy
  @RequestMapping(value = "/campioneEnte/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableCampioneEnte") == null || "{}".equals(filtroInSessione.get("tableCampioneEnte"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableCampioneEnte");
      } 
     filtroInSessione.put("tableCampioneEnte", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/campioneEnte/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
       return "gestionetabelle/confermaElimina";
  }

}
