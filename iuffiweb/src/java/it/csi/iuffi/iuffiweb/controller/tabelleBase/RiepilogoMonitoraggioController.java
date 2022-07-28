package it.csi.iuffi.iuffiweb.controller.tabelleBase;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IRiepilogoMonitoraggioEJB;
import it.csi.iuffi.iuffiweb.business.ISpecieVegetaleEJB;
import it.csi.iuffi.iuffiweb.business.ITipoCampioneEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RiepilogoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.api.RiepilogoMonitoraggio;
import it.csi.iuffi.iuffiweb.model.form.RiepilogoMonitoraggioForm;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;


@Controller
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class RiepilogoMonitoraggioController extends TabelleController
{
 
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  
  @Autowired
  private ITipoCampioneEJB tipoCampioneEJB;

  @Autowired
  private IRiepilogoMonitoraggioEJB riepilogoMonitoraggioEJB;

  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;

  @Autowired
  private ISpecieVegetaleEJB specieVegetaleEJB;

  @Autowired
  Validator validator;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/monitoraggio/riepilogo")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    Long maxON = riepilogoMonitoraggioEJB.getMaxCountON();
    model.addAttribute("maxON", maxON);
    List<RiepilogoMonitoraggioDTO> list = riepilogoMonitoraggioEJB.findAll();
    RiepilogoMonitoraggioForm form = new RiepilogoMonitoraggioForm();
    form.setRiepilogoList(list);
    model.addAttribute("riepilogoMonitoraggioForm", form);
    model.addAttribute("list", list);
    logger.debug("list size: " + list.size());
    setBreadcrumbs(model, request);
    return "gestionetabelle/riepilogoMonitoraggio";
  }

  @RequestMapping(value = "/monitoraggio/editRiepilogo")
  public String edit(Model model,HttpSession session, @RequestParam(value = "idSpecieVegetale") Integer idSpecieVegetale,
      @RequestParam(value = "idTipoCampione") Integer idTipoCampione, @RequestParam(value = "mesi") String mesi,
      HttpServletResponse response, HttpServletRequest request, RedirectAttributes redirectAttributes) throws InternalUnexpectedException
  {
    RiepilogoMonitoraggioDTO dto = riepilogoMonitoraggioEJB.findBySpecieVegetaleTipoCampioneAndMesi(idSpecieVegetale, idTipoCampione, mesi);
    
    if (dto == null) {
      String error = "La combinazione Specie vegetale - Tipo Campione - periodo selezionata non trovata";
      model.addAttribute("error", error);
      model.addAttribute("success", null);
      redirectAttributes.addFlashAttribute("error", error);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      return "redirect:riepilogo.do";
    }
    dto.setOldIdTipoCampione(dto.getIdTipoCampione());
    dto.setOldIdSpecieVegetale(dto.getIdSpecieVegetale());
    model.addAttribute("riepilogo", dto);
    List<SpecieVegetaleDTO> listaSpecieVegetali = specieVegetaleEJB.findValidi();
    model.addAttribute("listaSpecieVegetali", listaSpecieVegetali);
    if (dto.getOrganismiNocivi() != null) {
      for (OrganismoNocivoDTO o : dto.getOrganismiNocivi()) {
        OrganismoNocivoDTO onFull = organismoNocivoEJB.findById(o.getIdOrganismoNocivo());
        o.setNomeLatino(onFull.getNomeLatino());
      }
    } else {
        dto.setOrganismiNocivi(new ArrayList<OrganismoNocivoDTO>());
    }
    dto.setOldOrganismiNocivi(dto.getOrganismiNocivi());
    List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listaOn", listaOn);
    List<TipoCampioneDTO> listaCampioni = tipoCampioneEJB.findValidi();
    model.addAttribute("listaCampioni", listaCampioni);
    model.addAttribute("mode", "modifica");
    setBreadcrumbs(model, request);
    return "gestionetabelle/riepilogoMonitoraggioEdit";
  }
 
  @RequestMapping(value = "/monitoraggio/addRiepilogo")
  public String add(Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request) throws InternalUnexpectedException
  {
    RiepilogoMonitoraggioDTO dto = new RiepilogoMonitoraggioDTO();
    if (model.asMap().get("riepilogo") != null) {
      dto = (RiepilogoMonitoraggioDTO) model.asMap().get("riepilogo");
    }
    dto.setOldIdTipoCampione(null);
    model.addAttribute("riepilogo", dto);
    List<SpecieVegetaleDTO> listaSpecieVegetali = specieVegetaleEJB.findValidi();
    model.addAttribute("listaSpecieVegetali", listaSpecieVegetali);
    List<OrganismoNocivoDTO> listaOn = organismoNocivoEJB.findValidi();
    model.addAttribute("listaOn", listaOn);
    List<TipoCampioneDTO> listaCampioni = tipoCampioneEJB.findValidi();
    model.addAttribute("listaCampioni", listaCampioni);
    model.addAttribute("mode", "inserimento");
    setBreadcrumbs(model, request);
    return "gestionetabelle/riepilogoMonitoraggioEdit";
  }
 
  @RequestMapping(value = "/monitoraggio/saveRiepilogo")
  public String saveRiepilogo(Model model, @ModelAttribute("riepilogoMonitoraggioDTO") RiepilogoMonitoraggioDTO riepilogoDTO, HttpServletResponse response,
      HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    List<Integer> mesi = getMesi(riepilogoDTO);
    List<Integer> mesiOld = getMesiOld(riepilogoDTO);
    List<RiepilogoMonitoraggio> lista = new ArrayList<RiepilogoMonitoraggio>();
    List<RiepilogoMonitoraggio> listaOld = new ArrayList<RiepilogoMonitoraggio>();
    
    Errors errors = new Errors();
    if (riepilogoDTO.getIdSpecieVegetale() == null || riepilogoDTO.getIdSpecieVegetale() == 0) {
      errors.addError("idSpecieVegetale", "Campo Obbligatorio");
    }
    
    if (mesi == null || mesi.size() == 0) {
      errors.addError("mesi", "Campo Obbligatorio");
    }
    if (riepilogoDTO.getOrganismiNocivi() == null || riepilogoDTO.getOrganismiNocivi().size() == 0) {
      riepilogoDTO.setOrganismiNocivi(new ArrayList<OrganismoNocivoDTO>());
      errors.addError("idOrganismoNocivo", "Campo Obbligatorio");
    }

    if (riepilogoDTO.getIdTipoCampione() != null && riepilogoDTO.getIdTipoCampione() == 0)
      riepilogoDTO.setIdTipoCampione(null);
    if (riepilogoDTO.getOldIdTipoCampione() != null && riepilogoDTO.getOldIdTipoCampione() == 0)
      riepilogoDTO.setOldIdTipoCampione(null);
    
    if (errors.isEmpty()) {
      
      boolean continua = true;
      
      for (OrganismoNocivoDTO on: riepilogoDTO.getOrganismiNocivi()) {
        
        if (continua == false)
          break;
        
        if (on.getIdOrganismoNocivo() == null || on.getIdOrganismoNocivo() == 0)
          continue;
        
        if (on.getIdOrganismoNocivo() != null && on.getIdOrganismoNocivo() > 0) {
          
          for (int j : mesi) {
            RiepilogoMonitoraggio riep = riepilogoMonitoraggioEJB.findByUniqueKey(riepilogoDTO.getIdSpecieVegetale(), riepilogoDTO.getIdTipoCampione(), on.getIdOrganismoNocivo(), j);
            if (riep == null) {
              // insert
              riep = new RiepilogoMonitoraggio(riepilogoDTO.getIdSpecieVegetale(), riepilogoDTO.getIdTipoCampione(), j, on.getIdOrganismoNocivo());
              riep.setExtIdUtenteAggiornamento(utenteAbilitazioni.getIdUtenteLogin());
              try {
                riepilogoMonitoraggioEJB.insert(riep);
              } catch (Exception e) {
                  logger.error("Errore nell'inserimento nella tabella di compatibilità specie-on nel periodo: " + e.getMessage());
                  errors.addError("idSpecieVegetale", "Record già presente");
                  errors.addError("idTipoCampione", "Record già presente");
                  errors.addError("idOrganismoNocivo", "Record già presente");
                  errors.addError("mesi", "Record già presente");
                  continua = false;
                  break;
              }
            }
            else
            {
              // se sono in modalità inserimento non devono esserci record già presenti
              if (riepilogoDTO.getOldIdSpecieVegetale() == null || riepilogoDTO.getOldIdSpecieVegetale() == 0) {
                logger.error("Errore nell'inserimento nella tabella di compatibilità specie-on nel periodo: record già presente");
                errors.addError("idSpecieVegetale", "Record già presente");
                errors.addError("idTipoCampione", "Record già presente");
                errors.addError("idOrganismoNocivo", "Record già presente");
                errors.addError("mesi", "Record già presente");
                continua = false;
                break;
              }
            }
            lista.add(riep);
          }   // for interno (mesi)
          //onString += (onString.length()>0)?","+on.getIdOrganismoNocivo():on.getIdOrganismoNocivo();
        } // if
      }   // for esterno (organismo nocivi)
      
      if (errors.isEmpty()) {
        if (riepilogoDTO.getOldOrganismiNocivi() != null && riepilogoDTO.getOldOrganismiNocivi().size() > 0) {
          // Preparo la lista dei record precedente
          for (OrganismoNocivoDTO on : riepilogoDTO.getOldOrganismiNocivi()) {
            if (mesiOld != null && mesiOld.size() > 0) {
              for (int k : mesiOld) {
                RiepilogoMonitoraggio riepOld = new RiepilogoMonitoraggio(riepilogoDTO.getOldIdSpecieVegetale(), riepilogoDTO.getOldIdTipoCampione(), k, on.getIdOrganismoNocivo());
                listaOld.add(riepOld);
              }
            }
          }
          if (listaOld != null && listaOld.size() > 0) {
            List<RiepilogoMonitoraggio> diff = new ArrayList<RiepilogoMonitoraggio>(listaOld);
            diff.removeAll(lista);  // elimino dalla lista dei vecchi record quelli uguali ai nuovi per ottenere la lista dei record da eliminare (storicizzare)
            if (diff != null && diff.size() > 0) {
              for (RiepilogoMonitoraggio riep : diff) {
                riep = riepilogoMonitoraggioEJB.findByUniqueKey(riep.getIdSpecieVegetale(), riep.getIdTipoCampione(), riep.getIdOrganismoNocivo(), riep.getMese());
                riepilogoMonitoraggioEJB.storicizza(riep);
              }
            }
          }
        }
      }
    }   // if error is empty
    if (!errors.isEmpty()) {
      model.addAttribute("errors", errors);
      model.addAttribute("success", null);
      redirectAttributes.addFlashAttribute("riepilogo", riepilogoDTO);
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("success", null);
      redirectAttributes.addFlashAttribute("model", model);
      return "redirect:addRiepilogo.do";
    }
    return "redirect:riepilogo.do";
  }

  @RequestMapping(value = "/monitoraggio/removeRiepilogoOn", method = RequestMethod.GET)
  public String eliminaOn(Model model, @RequestParam(value = "idSpecieVegetale") Integer idSpecieVegetale,
      @RequestParam(value = "idTipoCampione") Integer idTipoCampione, @RequestParam(value = "idOrganismoNocivo") Integer idOrganismoNocivo,
      @RequestParam(value = "mesi") String mesi, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    // Elimina (storicizza) un singolo organismo nocivo dalla tabella iuf_r_specie_on_periodo avente in base ai parametri specificati nella firma del metodo
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    riepilogoMonitoraggioEJB.removeOrganismoNocivo(idSpecieVegetale, idTipoCampione, idOrganismoNocivo, utenteAbilitazioni.getIdUtenteLogin());   // elimino eventuali record con organismi nocivi diversi da quelli presenti
    logger.debug("cancellazione organismi nocivi effettuata");
    //return "redirect:editRiepilogo.do?idSpecieVegetale="+idSpecieVegetale+"&idTipoCampione="+idTipoCampione+"&mesi="+mesi;
    return "redirect:riepilogo.do";
  }

  @RequestMapping(value = "/monitoraggio/deleteRiepilogo")
  public String canDelete(Model model, @RequestParam(value = "idSpecieVegetale") Integer idSpecieVegetale, @RequestParam(value = "idTipoCampione") Integer idTipoCampione,
          @RequestParam(value = "mesi") String mesi, @RequestParam(value = "on") String on, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
      model.addAttribute("idSpecieVegetale",idSpecieVegetale);
      model.addAttribute("idTipoCampione",idTipoCampione);
      model.addAttribute("mesi",mesi);
      model.addAttribute("on",on);
      model.addAttribute("table","tableRiepilogoMonitoraggio");
      return "gestionetabelle/confermaEliminaRiepilogoMonitoraggio";
  }
  
  @RequestMapping(value = "/monitoraggio/removeRiepilogo", method = RequestMethod.GET)
  public String remove(Model model, HttpSession session, @RequestParam(value = "idSpecieVegetale") Integer idSpecieVegetale,
      @RequestParam(value = "idTipoCampione") Integer idTipoCampione, @RequestParam(value = "mesi") String mesi, @RequestParam(value = "on") String on,
      HttpServletResponse response, HttpServletRequest request) throws InternalUnexpectedException
  {
    // Elimina tutti gli organismi nocivi dalla tabella iuf_r_specie_on_periodo in base ai parametri specificati nella firma del metodo
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    String[] s = on.split(";");
    String onStr = "";
    for (String o : s) {
      onStr += o.split(",")[0] + ",";
    }
    onStr = onStr.substring(0, onStr.length()-1);
    riepilogoMonitoraggioEJB.remove(idSpecieVegetale, idTipoCampione, mesi, onStr, utenteAbilitazioni.getIdUtenteLogin());
    return "redirect:riepilogo.do";
  }
  
  @RequestMapping(value = "/rest/riepilogo-monitoraggio", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<List<RiepilogoMonitoraggio>> getRiepilogoMonitoraggioJsonOutput() throws InternalUnexpectedException
  {
    List<RiepilogoMonitoraggio> lista = riepilogoMonitoraggioEJB.getRiepilogoMonitoraggio();
    if (lista == null)
      lista = new ArrayList<RiepilogoMonitoraggio>();
    return new ResponseEntity<List<RiepilogoMonitoraggio>>(lista,HttpStatus.OK);
  }

  @Lazy
  @RequestMapping(value = "/monitoraggio/riepilogoMonitoraggioExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InternalUnexpectedException
  {
    List<RiepilogoMonitoraggioDTO> elenco = riepilogoMonitoraggioEJB.findAll();
    response.setContentType("application/xls");
    response.setHeader("Content-Disposition", "attachment; filename=\"tabellaCompatibilitaSpecie-ON.xls\"");
    
    return new ModelAndView("excelRiepilogoMonitoraggioView", "elenco", elenco);
  }
  
  private List<Integer> getMesi(RiepilogoMonitoraggioDTO dto) {
    List<Integer> mesi = new ArrayList<Integer>();
    if (dto.getGennaio() == 1)
      mesi.add(1);
    if (dto.getFebbraio() == 1)
      mesi.add(2);
    if (dto.getMarzo() == 1)
      mesi.add(3);
    if (dto.getAprile() == 1)
      mesi.add(4);
    if (dto.getMaggio() == 1)
      mesi.add(5);
    if (dto.getGiugno() == 1)
      mesi.add(6);
    if (dto.getLuglio() == 1)
      mesi.add(7);
    if (dto.getAgosto() == 1)
      mesi.add(8);
    if (dto.getSettembre() == 1)
      mesi.add(9);
    if (dto.getOttobre() == 1)
      mesi.add(10);
    if (dto.getNovembre() == 1)
      mesi.add(11);
    if (dto.getDicembre() == 1)
      mesi.add(12);
    return mesi;
  }
  
  private List<Integer> getMesiOld(RiepilogoMonitoraggioDTO dto) {
    List<Integer> mesi = new ArrayList<Integer>();
    if (dto.getOldGennaio() == 1)
      mesi.add(1);
    if (dto.getOldFebbraio() == 1)
      mesi.add(2);
    if (dto.getOldMarzo() == 1)
      mesi.add(3);
    if (dto.getOldAprile() == 1)
      mesi.add(4);
    if (dto.getOldMaggio() == 1)
      mesi.add(5);
    if (dto.getOldGiugno() == 1)
      mesi.add(6);
    if (dto.getOldLuglio() == 1)
      mesi.add(7);
    if (dto.getOldAgosto() == 1)
      mesi.add(8);
    if (dto.getOldSettembre() == 1)
      mesi.add(9);
    if (dto.getOldOttobre() == 1)
      mesi.add(10);
    if (dto.getOldNovembre() == 1)
      mesi.add(11);
    if (dto.getOldDicembre() == 1)
      mesi.add(12);
    return mesi;
  }

}
