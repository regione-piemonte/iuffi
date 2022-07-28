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

import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IPrevisioneMonitoraggioEJB;
import it.csi.iuffi.iuffiweb.business.IRiepilogoMonitoraggioEJB;
import it.csi.iuffi.iuffiweb.business.ITrappolaOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.SceltaEsclusiva;
import it.csi.iuffi.iuffiweb.model.TrappolaOnDTO;
import it.csi.iuffi.iuffiweb.model.api.RiepilogoMonitoraggio;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller

@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class OrganismoNocivoController extends TabelleController
{
  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;

  @Autowired
  Validator validator;
  
  @Autowired
  private ITrappolaOrganismoNocivoEJB trappolOnEJB;

  @Autowired
  private IRiepilogoMonitoraggioEJB riepilogoEJB;

  @Autowired
  private IPrevisioneMonitoraggioEJB previsioneEJB;

  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/organismoNocivo/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("error",model.asMap().get("error"));
    }
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
    setBreadcrumbs(model, request);
    return "gestionetabelle/organismoNocivo";
  }

  @RequestMapping(value = "/organismoNocivo/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    
    OrganismoNocivoDTO organismoNocivo = (OrganismoNocivoDTO) model.asMap().get("organismoNocivo");
    model.addAttribute("organismoNocivo", organismoNocivo);
    
    if(organismoNocivo==null)organismoNocivo = new OrganismoNocivoDTO();
    //OrganismoNocivoDTO dto = new OrganismoNocivoDTO();
    if (organismoNocivo.getFlagEmergenza() == null)
      organismoNocivo.setFlagEmergenza("N");
    if (organismoNocivo.getEuro() == null)
      organismoNocivo.setEuro("N");
    if (organismoNocivo.getFlagPianificazione() == null)
      organismoNocivo.setFlagPianificazione("N");
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    
    List<SceltaEsclusiva> emergenza = this.scelta();
    model.addAttribute("listaEmergenza", emergenza);
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
   
    List<SceltaEsclusiva> euro = this.scelta();
    model.addAttribute("listaEuro", euro);
    model.addAttribute("organismoNocivo", organismoNocivo);
    setBreadcrumbs(model, request);
    return "gestionetabelle/organismoNocivoAdd";
  }
  
  
  @RequestMapping(value = "/organismoNocivo/save")
  public String save(Model model, @ModelAttribute("organismoNocivo") OrganismoNocivoDTO organismoNocivo, HttpServletResponse response, 
      HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    List<SceltaEsclusiva> emergenza =this.scelta();
    model.addAttribute("listaEmergenza", emergenza);
    
    List<SceltaEsclusiva> euro =this.scelta();
    model.addAttribute("listaEuro", euro);
 
    OrganismoNocivoDTO organismoNocivoDTO = organismoNocivo;
    Errors errors = new Errors();   
    if(organismoNocivo.getNomeLatino()==null || "".equals(organismoNocivo.getNomeLatino())) {
      errors.addError("nomeLatino", "Campo Obbligatorio");
    }
    if(organismoNocivo.getSigla()==null || "".equals(organismoNocivo.getSigla())) {
      errors.addError("sigla", "Campo Obbligatorio");
    }
    if(organismoNocivo.getEuro()==null || "".equals(organismoNocivo.getEuro())) {
      organismoNocivo.setEuro("N");
    }
    if(organismoNocivo.getFlagEmergenza()==null || "".equals(organismoNocivo.getFlagEmergenza())) {
      organismoNocivo.setFlagEmergenza("N");
    }
    if (organismoNocivo.getFlagPianificazione() == null || "".equals(organismoNocivo.getFlagPianificazione())) {
      organismoNocivo.setFlagPianificazione("N");
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("success", null);
      //model.addAttribute("anagrafica",anagrafica);      
      redirectAttributes.addFlashAttribute("organismoNocivo", organismoNocivoDTO);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      if(organismoNocivoDTO.getIdOrganismoNocivo()!=null && organismoNocivoDTO.getIdOrganismoNocivo()>0)
        return "redirect:edit.do?idOrganismoNocivo="+organismoNocivoDTO.getIdOrganismoNocivo();
      else
        return "redirect:showFilterAdd.do";
    } else {
        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        organismoNocivo.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
    
        if (organismoNocivo.getIdOrganismoNocivo() == null || organismoNocivo.getIdOrganismoNocivo()== 0) {
          organismoNocivoDTO = organismoNocivoEJB.insertOrganismoNocivo(organismoNocivo);
        }
        else
        {
          organismoNocivoDTO = organismoNocivoEJB.findById(organismoNocivoDTO.getIdOrganismoNocivo());
          organismoNocivo.setDataInizioValidita(organismoNocivoDTO.getDataInizioValidita());
          if(organismoNocivo.isAnnoCorrente()) {
            organismoNocivoEJB.updateOrganismoNocivo(organismoNocivo);
          }else {
            List<TrappolaOnDTO> listaOn = trappolOnEJB.findByIdOn(organismoNocivo.getIdOrganismoNocivo());
            if (listaOn!=null && !listaOn.isEmpty()) {
              organismoNocivoDTO = organismoNocivoEJB.findById(organismoNocivoDTO.getIdOrganismoNocivo());
              model.addAttribute("organismoNocivo", organismoNocivoDTO);
              redirectAttributes.addFlashAttribute("error", "L'organismo nocivo " + organismoNocivo.getNomeCompleto() + " non può essere modificato in quanto è referenziato dalla tabella TRAPPOLA-ORGANISMO NOCIVO");
              redirectAttributes.addFlashAttribute("model", model);
              return "redirect:edit.do?idOrganismoNocivo="+organismoNocivoDTO.getIdOrganismoNocivo();
            }
            List<RiepilogoMonitoraggio> list = riepilogoEJB.findByIdOrganismoNocivo(organismoNocivo.getIdOrganismoNocivo());
            if (list!=null && !list.isEmpty()) {
              organismoNocivoDTO = organismoNocivoEJB.findById(organismoNocivoDTO.getIdOrganismoNocivo());
              model.addAttribute("organismoNocivo", organismoNocivoDTO);
              redirectAttributes.addFlashAttribute("error", "L'organismo nocivo " + organismoNocivo.getNomeCompleto() + " non può essere modificato in quanto è referenziato dalla tabella COMPATIBILITA' SPECIE/CAMPIONE/PERIODO/ORGANISMO NOCIVO");
              redirectAttributes.addFlashAttribute("model", model);
              return "redirect:edit.do?idOrganismoNocivo="+organismoNocivoDTO.getIdOrganismoNocivo();
            }
            long count = previsioneEJB.countPrevisioniByIdOrganismoNocivo(organismoNocivo.getIdOrganismoNocivo());
            if (count > 0) {
              organismoNocivoDTO = organismoNocivoEJB.findById(organismoNocivoDTO.getIdOrganismoNocivo());
              model.addAttribute("organismoNocivo", organismoNocivoDTO);
              redirectAttributes.addFlashAttribute("error", "L'organismo nocivo " + organismoNocivo.getNomeCompleto() + " non può essere modificato in quanto è referenziato dalla tabella PREVISIONE ATTIVITA' DI MONITORAGGIO");
              redirectAttributes.addFlashAttribute("model", model);
              return "redirect:edit.do?idOrganismoNocivo="+organismoNocivoDTO.getIdOrganismoNocivo();
            } 
//            organismoNocivoEJB.updateOrganismoNocivo(organismoNocivo);
            organismoNocivoEJB.updateDataFineValidita(organismoNocivo.getIdOrganismoNocivo());
            organismoNocivoDTO = organismoNocivoEJB.insertOrganismoNocivo(organismoNocivo);  
          }
        }
        organismoNocivoDTO = organismoNocivoEJB.findById(organismoNocivoDTO.getIdOrganismoNocivo());
        model.addAttribute("organismoNocivo", organismoNocivoDTO);
        List<OrganismoNocivoDTO> list = organismoNocivoEJB.findAll();
        redirectAttributes.addFlashAttribute("list", list);
        
        model.addAttribute("success", "Organismo Nocivo salvato con successo");
        setBreadcrumbs(model, request);
        
        Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  

        return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/organismoNocivo/search")
  public String search(Model model, @ModelAttribute("organismoNocivo") OrganismoNocivoDTO organismoNocivo, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<OrganismoNocivoDTO> list = organismoNocivoEJB.findByFilter(organismoNocivo);
    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/organismoNocivo";
  }

  @RequestMapping(value = "/organismoNocivo/edit")
  public String edit(Model model, HttpSession session, @RequestParam(value = "idOrganismoNocivo") String idOrganismoNocivo, 
      HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    OrganismoNocivoDTO dto = (OrganismoNocivoDTO) model.asMap().get("organismoNocivo");
   
    List<SceltaEsclusiva> emergenza = this.scelta();
    model.addAttribute("listaEmergenza", emergenza);
    
    List<SceltaEsclusiva> euro = this.scelta();
    model.addAttribute("listaEuro", euro);
    
    if (dto==null)
      dto = organismoNocivoEJB.findById(Integer.decode(idOrganismoNocivo));
    
    model.addAttribute("organismoNocivo", dto);
    List<OrganismoNocivoDTO> list = new ArrayList<OrganismoNocivoDTO>();
    list.add(dto);
    
    Map<String,String> filtroInSessione =this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    }

    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/organismoNocivoAdd";
  }

  @RequestMapping(value = "/organismoNocivo/remove")
  public String remove(Model model, @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo, 
      HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, HttpSession session) throws InternalUnexpectedException
  {
    OrganismoNocivoDTO organismoNocivoDTO = null;
    try
    {
      organismoNocivoDTO = organismoNocivoEJB.findById(idOrganismoNocivo);
      
      List<TrappolaOnDTO> listaOn = trappolOnEJB.findByIdOn(idOrganismoNocivo);
      if (listaOn!=null && !listaOn.isEmpty()) {
        model.addAttribute("organismoNocivo", organismoNocivoDTO);
        redirectAttributes.addFlashAttribute("error", "L'organismo nocivo " + organismoNocivoDTO.getNomeCompleto() + " non può essere eliminato in quanto è referenziato dalla tabella TRAPPOLA-ORGANISMO NOCIVO");
        redirectAttributes.addFlashAttribute("model", model);
      } else {
        List<RiepilogoMonitoraggio> list = riepilogoEJB.findByIdOrganismoNocivo(idOrganismoNocivo);
        if (list!=null && !list.isEmpty()) {
          model.addAttribute("organismoNocivo", organismoNocivoDTO);
          redirectAttributes.addFlashAttribute("error", "L'organismo nocivo " + organismoNocivoDTO.getNomeCompleto() + " non può essere eliminato in quanto è referenziato dalla tabella COMPATIBILITA' SPECIE/CAMPIONE/PERIODO/ORGANISMO NOCIVO");
          redirectAttributes.addFlashAttribute("model", model);
        } else {
          long count = previsioneEJB.countPrevisioniByIdOrganismoNocivo(idOrganismoNocivo);
          if (count > 0) {
            model.addAttribute("organismoNocivo", organismoNocivoDTO);
            redirectAttributes.addFlashAttribute("error", "L'organismo nocivo " + organismoNocivoDTO.getNomeCompleto() + " non può essere eliminato in quanto è referenziato dalla tabella PREVISIONE ATTIVITA' DI MONITORAGGIO");
            redirectAttributes.addFlashAttribute("model", model);
          } else {
            if(!organismoNocivoDTO.isAnnoCorrente()) {
              organismoNocivoEJB.updateDataFineValidita(idOrganismoNocivo);
            }else {
              organismoNocivoEJB.remove(idOrganismoNocivo);
            }
          }
        }
      }
    }
    catch (InternalUnexpectedException e)
    {
      if (e.getCause() instanceof DataIntegrityViolationException) {
        organismoNocivoEJB.updateDataFineValidita(idOrganismoNocivo);
      } else {
        redirectAttributes.addFlashAttribute("error", "Si è verificato un errore inaspettato durante l'eliminazione dell'organismo nocivo " + organismoNocivoDTO.getNomeCompleto());
        logger.debug("Errore nell'eliminazione dell'organismo nocivo con id = " + idOrganismoNocivo + " : " + e.getMessage());
      }
    }
    return "redirect:showFilter.do";
  }

  @RequestMapping(value = "/rest/organismi-nocivi", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<OrganismoNocivoDTO>> getOrganismoNocivoJsonOutput() throws InternalUnexpectedException
  {
    List<OrganismoNocivoDTO> lista = organismoNocivoEJB.findAll();
    if (lista == null)
      lista = new ArrayList<OrganismoNocivoDTO>();
    return new ResponseEntity<List<OrganismoNocivoDTO>>(lista,HttpStatus.OK);
  }

  @RequestMapping(value = "/organismoNocivo/getOrganismoNocivoJson", produces = "application/json")
  @ResponseBody
  public String getOrganismoNocivoJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    List<OrganismoNocivoDTO> lista = organismoNocivoEJB.findAll();
    /*
    if (lista != null) {
      for (OrganismoNocivoDTO o : lista) {
        if (o.getEuro() != null && o.getEuro().equalsIgnoreCase("S"))
          o.setEuro("Si");
        else
          o.setEuro("No");
        if (o.getFlagEmergenza() != null && o.getFlagEmergenza().equalsIgnoreCase("S"))
          o.setFlagEmergenza("Si");
        else
          o.setFlagEmergenza("No");
      }
    }
    */
    String obj = mapper.writeValueAsString(lista);
    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }

    return obj;
  }
  
  @RequestMapping(value = "/organismoNocivo/getOrganismoNocivo", produces = "application/json")
  @ResponseBody
  public String getOrganismoNocivo(@RequestBody String checkbox, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    
    List<OrganismoNocivoDTO> lista = null;
    
    if (checkbox != null) {
      if (checkbox.toLowerCase().indexOf("true") > -1) {
        session.setAttribute("checkboxAllOrganismiNocivi", "true");
      } else {
        session.setAttribute("checkboxAllOrganismiNocivi", "false");
      }
    }
    if (session.getAttribute("checkboxAllOrganismiNocivi") != null) {
      if (((String) session.getAttribute("checkboxAllOrganismiNocivi")).equals("true")) {
          lista = organismoNocivoEJB.findAll();
      } else {
          lista = organismoNocivoEJB.findValidi();
      }
    } else {
        lista = organismoNocivoEJB.findAll();
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
  @RequestMapping(value = "/organismoNocivo/organismoNocivoExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<OrganismoNocivoDTO> elenco = organismoNocivoEJB.findAll();
    response.setContentType("application/xls");      
    response.setHeader("Content-Disposition", "attachment; filename=\"organismiNocivi.xls\"");
    
    return new ModelAndView("excelOrganismoNocivoView", "elenco", elenco);
  }
  
  public List<SceltaEsclusiva> scelta() {
    List<SceltaEsclusiva> lista = new ArrayList();
    SceltaEsclusiva obj= new SceltaEsclusiva();
    obj.setId("S");
    obj.setDescrizione("Si");
    lista.add(obj);
    SceltaEsclusiva obj2= new SceltaEsclusiva();
    obj2.setId("N");
    obj2.setDescrizione("No");
    lista.add(obj2);
    return lista;
  }

  @Lazy
  @RequestMapping(value = "/organismoNocivo/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableOrganismoNocivo") == null || "{}".equals(filtroInSessione.get("tableOrganismoNocivo"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableOrganismoNocivo");
      } 
     filtroInSessione.put("tableOrganismoNocivo", filtroInit);
      return filtroInSessione;
  }

  @RequestMapping(value = "/organismoNocivo/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tableOrganismoNocivo");
      return "gestionetabelle/confermaElimina";
  }


}
