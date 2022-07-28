package it.csi.iuffi.iuffiweb.controller.tabelleBase;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import it.csi.iuffi.iuffiweb.business.IExtraFieldTrapEJB;
import it.csi.iuffi.iuffiweb.controller.TabelleController;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.ExtraFieldTrapDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping(value = "/extraFieldTrap")
//@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class ExtraFieldTrapController extends TabelleController
{
 
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  
  @Autowired
  private IExtraFieldTrapEJB extraFieldTrapEJB;
  
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
    setBreadcrumbs(model, request);
    return "gestionetabelle/extraFieldTrap";
  }

  @RequestMapping(value = "/save")
  public RedirectView save(Model model, @ModelAttribute("extraFieldTrap") ExtraFieldTrapDTO extraFieldTrap, HttpSession session, HttpServletRequest request, RedirectAttributes attributes, BindingResult bindingResult) throws InternalUnexpectedException
  {
    ExtraFieldTrapDTO extraFieldTrapDTO = extraFieldTrap;
    if (extraFieldTrap.getDataInizioValidita() == null) {
      extraFieldTrap.setDataInizioValidita(new Date());
    }

    UtenteAbilitazioni utente = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    extraFieldTrap.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());

    if (extraFieldTrap.getIdExtraFieldTrap() == null || extraFieldTrap.getIdExtraFieldTrap() == 0)
      extraFieldTrapDTO = extraFieldTrapEJB.insert(extraFieldTrap);
    else
      extraFieldTrapEJB.update(extraFieldTrap);
    
    model.addAttribute("extraFieldTrap", extraFieldTrapDTO);
    List<ExtraFieldTrapDTO> list = extraFieldTrapEJB.findAll();
    attributes.addFlashAttribute("list", list);
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/search")
  public String search(Model model, @ModelAttribute("extraFieldTrap") ExtraFieldTrapDTO extraFieldTrap, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<ExtraFieldTrapDTO> list = extraFieldTrapEJB.findByFilter(extraFieldTrap);
    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/extraFieldTrap";
  }

  @RequestMapping(value = "/edit")
  public String edit(Model model, @RequestParam(value = "idExtraFieldTrap") String idExtraFieldTrap, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    ExtraFieldTrapDTO dto = extraFieldTrapEJB.findById(Integer.decode(idExtraFieldTrap));
    model.addAttribute("extraFieldTrap", dto);
    List<ExtraFieldTrapDTO> list = new ArrayList<ExtraFieldTrapDTO>();
    list.add(dto);
    model.addAttribute("list", list);
    setBreadcrumbs(model, request);
    return "gestionetabelle/extraFieldTrap";
  }

  @RequestMapping(value = "/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idExtraFieldTrap") String idExtraFieldTrap, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
    extraFieldTrapEJB.remove(Integer.decode(idExtraFieldTrap));
    List<ExtraFieldTrapDTO> list = extraFieldTrapEJB.findAll();
    attributes.addFlashAttribute("list", list);
    return new RedirectView("showFilter.do", true);
  }

  @RequestMapping(value = "/getTipoAree", produces = { "application/json" }, method = RequestMethod.GET)
  public ResponseEntity<?> getTipoAreaJsonOutput()
  {
    List<ExtraFieldTrapDTO> lista;
    try
    {
      lista = extraFieldTrapEJB.findAll();
    } catch (Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return new ResponseEntity<Map<String,String>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<ExtraFieldTrapDTO>>(lista,HttpStatus.OK);
  }


}
