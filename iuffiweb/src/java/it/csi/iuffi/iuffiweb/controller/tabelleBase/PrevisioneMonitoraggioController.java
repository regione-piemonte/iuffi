package it.csi.iuffi.iuffiweb.controller.tabelleBase;


import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import it.csi.iuffi.iuffiweb.business.IPrevisioneMonitoraggioEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.PianoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.PrevisioneMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.SceltaEsclusiva;
import it.csi.iuffi.iuffiweb.model.form.PrevisioneMonitoraggioForm;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;

@Controller
@RequestMapping(value = "/monitoraggio")
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class PrevisioneMonitoraggioController extends TabelleController
{
 
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  
  @Autowired
  private IPrevisioneMonitoraggioEJB previsioneMonitoraggioEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    setBreadcrumbs(model, request);
    return "gestionetabelle/pianoMonitoraggio";
  }

  @RequestMapping(value = "/showFilterAdd")
  public String showFilterAdd(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    
    PianoMonitoraggioDTO dto = new PianoMonitoraggioDTO();

    if (RequestContextUtils.getInputFlashMap(request) != null) {
      model.addAttribute("list", model.asMap().get("list"));
    }
    
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  
   
    model.addAttribute("pianoMonitoraggio", dto);
    setBreadcrumbs(model, request);
    return "gestionetabelle/pianoMonitoraggioAdd";
  }
  
  private List<SceltaEsclusiva> scelta() {
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
  
  @RequestMapping(value = "/save")
  public String save(Model model, @ModelAttribute("pianoMonitoraggio") PianoMonitoraggioDTO pianoMonitoraggio, HttpServletResponse response, HttpSession session, HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    
    PianoMonitoraggioDTO piano = pianoMonitoraggio;
    Errors errors = new Errors();
    if (piano.getAnno() == null || piano.getAnno().intValue() == 0) {
      errors.addError("anno", "Campo Obbligatorio");
    }
    else
    if (piano.getAnno().intValue() < 2000 || piano.getAnno().intValue() > 2100) {
      errors.addError("anno", "Anno non valido");
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("success", null);
      return this.showFilterAdd(model, session, request,response);
    } else {

        UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
        piano.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
    
        if (piano.getIdPianoMonitoraggio() == null || piano.getIdPianoMonitoraggio() == 0) {
          piano = previsioneMonitoraggioEJB.insertPiano(piano);
        } else {
          previsioneMonitoraggioEJB.updatePiano(piano);
        }
        piano = previsioneMonitoraggioEJB.findPianoById(piano.getIdPianoMonitoraggio());
        model.addAttribute("pianoMonitoraggio", piano);
        List<PianoMonitoraggioDTO> list = previsioneMonitoraggioEJB.findPianiAll();
        attributes.addFlashAttribute("list", list);
        
        model.addAttribute("success", "Piano monitoraggio salvato con successo");
        setBreadcrumbs(model, request);
        
        Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);  

        return "redirect:showFilter.do";
    }
  }

  @RequestMapping(value = "/search")
  public String search(Model model, @ModelAttribute("pianoMonitoraggio") PianoMonitoraggioDTO pianoMonitoraggio, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<PianoMonitoraggioDTO> list = previsioneMonitoraggioEJB.findPianiByFilter(pianoMonitoraggio);
    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/pianoMonitoraggio";
  }

  @RequestMapping(value = "/edit")
  public String edit(Model model, HttpSession session, @RequestParam(value = "idPianoMonitoraggio") Integer idPianoMonitoraggio, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    
    PianoMonitoraggioDTO dto = previsioneMonitoraggioEJB.findPianoById(idPianoMonitoraggio);
    model.addAttribute("pianoMonitoraggio", dto);
    List<PianoMonitoraggioDTO> list = new ArrayList<PianoMonitoraggioDTO>();
    list.add(dto);
    
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);      
    if(dto.getDataFineValidita()!=null) {
      model.addAttribute("disabled",true);
    } 

    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/pianoMonitoraggioAdd";
  }

  @RequestMapping(value = "/remove")
  public RedirectView remove(Model model, @RequestParam(value = "id") Integer idPianoMonitoraggio, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    try {
      previsioneMonitoraggioEJB.removePiano(idPianoMonitoraggio);
    } catch (InternalUnexpectedException e) {
      boolean isFound = e.getCause().getMessage().indexOf("ORA-02292") !=-1? true: false; //true
      if(isFound) {
        previsioneMonitoraggioEJB.updateDataFineValidita(idPianoMonitoraggio);
      }
    }      
    
    List<PianoMonitoraggioDTO> list = previsioneMonitoraggioEJB.findPianiAll();
    attributes.addFlashAttribute("list", list);
    return new RedirectView("showFilter.do", true);
  }

  @Lazy
  @RequestMapping(value = "/previsione/filtri")
  public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
    if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
    }
    String filtroInit;
    if(filtroInSessione.get("tablePianoMonitoraggio") == null || "{}".equals(filtroInSessione.get("tablePianoMonitoraggio"))) {
        filtroInit = "{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
    } else {
        filtroInit=filtroInSessione.get("tablePianoMonitoraggio");
    }
    filtroInSessione.put("tablePianoMonitoraggio", filtroInit);
    return filtroInSessione;
  }

  @RequestMapping(value = "/delete")
  public String canDelete(Model model, @RequestParam(value = "id") String id,HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("id",id);
      model.addAttribute("table","tablePianoMonitoraggio");
      return "gestionetabelle/confermaElimina";
  }

  @RequestMapping(value = "/getPianiMonitoraggio", produces = "application/json")
  @ResponseBody
  public String getPianiMonitoraggioJson(HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);
    List<PianoMonitoraggioDTO> lista = previsioneMonitoraggioEJB.findPianiAll();
    String obj = mapper.writeValueAsString(lista);
    if (lista == null)
    {
      lista = new ArrayList<>();
      obj = mapper.writeValueAsString(lista);
    }
    return obj;
  }

  @RequestMapping(value = "/dettaglioPrevisione")
  public String previsione(Model model, @RequestParam(value = "idPianoMonitoraggio") Integer idPianoMonitoraggio, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<PrevisioneMonitoraggioDTO> list = new ArrayList<PrevisioneMonitoraggioDTO>();
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      list = (List<PrevisioneMonitoraggioDTO>) model.asMap().get("list");
    } else {
      list = previsioneMonitoraggioEJB.findPrevisioneByIdPiano(idPianoMonitoraggio);
    }
    PianoMonitoraggioDTO pianoMonitoraggio = previsioneMonitoraggioEJB.findPianoById(idPianoMonitoraggio);
    String utente = "";
    try {
      UtenteAbilitazioni utenteAbilitazioni = PapuaservProfilazioneServiceFactory.getRestServiceClient()
          .getUtenteAbilitazioniByIdUtenteLogin(pianoMonitoraggio.getExtIdUtenteAggiornamento());
      utente = utenteAbilitazioni.getCognome() + " " + utenteAbilitazioni.getNome();
    } catch (Exception e) {
        logger.error("Si è verificato un errore nel recupero dall'utente Papua: " + e.getMessage());
    }
    //UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    PrevisioneMonitoraggioForm form = new PrevisioneMonitoraggioForm();
    form.setPrevisioneList(list);
    model.addAttribute("previsioneMonitoraggioForm", form);
    model.addAttribute("pianoMonitoraggio", pianoMonitoraggio);
    model.addAttribute("utente", utente);
    model.addAttribute("editabile", pianoMonitoraggio.getDataFineValidita() == null);
    setBreadcrumbs(model, request);
    return "gestionetabelle/previsioneMonitoraggio";
  }

  @RequestMapping(value = "/savePrevisione")
  public RedirectView savePrevisione(Model model, @ModelAttribute("previsioneMonitoraggioForm") PrevisioneMonitoraggioForm previsioneMonitoraggioForm, HttpServletResponse response, HttpSession session, HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    Integer idPianoMonitoraggio = Integer.decode(request.getParameter("idPianoMonitoraggio"));
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");

    if (previsioneMonitoraggioForm != null && previsioneMonitoraggioForm.getPrevisioneList() != null && previsioneMonitoraggioForm.getPrevisioneList().size() > 0) {
      for (int i=0; i<previsioneMonitoraggioForm.getPrevisioneList().size(); i++) {
        PrevisioneMonitoraggioDTO previsione = previsioneMonitoraggioForm.getPrevisioneList().get(i);
        previsione.setIdPianoMonitoraggio(idPianoMonitoraggio);
        previsioneMonitoraggioEJB.savePrevisione(previsione, utenteAbilitazioni.getIdUtenteLogin());
      }
    }
    return new RedirectView("dettaglioPrevisione.do?idPianoMonitoraggio=" + idPianoMonitoraggio, true);
  }

  @Lazy
  @RequestMapping(value = "/pianoMonitoraggioExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    List<PianoMonitoraggioDTO> elenco = previsioneMonitoraggioEJB.findPianiAll();
    response.setContentType("application/xls");
    response.setHeader("Content-Disposition", "attachment; filename=\"pianoMonitoraggio.xls\"");
    
    return new ModelAndView("excelPianoMonitoraggioView", "elenco", elenco);
  }
  
  @Lazy
  @RequestMapping(value = "/pianoMonitoraggioDettaglioExcel")
  public ModelAndView downloadDettaglioExcel(@RequestParam("idPianoMonitoraggio") Integer idPianoMonitoraggio, Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    PianoMonitoraggioDTO piano = previsioneMonitoraggioEJB.findPianoById(idPianoMonitoraggio);
    List<PrevisioneMonitoraggioDTO> list = previsioneMonitoraggioEJB.findPrevisioneByIdPiano(idPianoMonitoraggio);
    response.setContentType("application/xls");
    response.setHeader("Content-Disposition", "attachment; filename=\"pianoMonitoraggioDettaglio.xls\"");
    Map<String, Object> modelForExcel = new HashMap<String, Object>();
    modelForExcel.put("piano", piano);
    modelForExcel.put("list", list);
    return new ModelAndView("excelPianoMonitoraggioDettaglioView", modelForExcel);
  }

  
  
}
