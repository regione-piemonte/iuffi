package it.csi.iuffi.iuffiweb.presentation.ricercalog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.gestionelog.LogDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.DateValidator;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2009", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping(value = "/cuiuffi2009")
public class CUIUFFI2009RicercaLog extends BaseController 
{
  @Autowired
  IQuadroIuffiEJB               quadroIuffiEJB;

  
  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session) throws InternalUnexpectedException {
    
    return "ricercalog/ricercalog";
  }
  
  @RequestMapping(value = "/submitData", method = RequestMethod.POST)
  public String submitForm(Model model, HttpSession session,
      @RequestParam(value="istanzaDataDa") String istanzaDataDa,
      @RequestParam(value="istanzaDataA") String istanzaDataA,
      @RequestParam(value="rtesto") String rtesto
      ) throws InternalUnexpectedException {

    Errors errors = new Errors();
    Date locIstanzaDataDa = null;
    Date locIstanzaDataA = null;
    try {
      locIstanzaDataDa = new SimpleDateFormat("dd/MM/yyyy").parse(istanzaDataDa);
      model.addAttribute("istanzaDataDa", locIstanzaDataDa);
      errors.validateDateInRange(locIstanzaDataDa, "istanzaDataDa", null, new Date(), true, true);
    } catch (ParseException e) { 
      errors.addError("istanzaDataDa", DateValidator.ERRORE_DATA_NON_VALIDA);
    }
    try {
      locIstanzaDataA = new SimpleDateFormat("dd/MM/yyyy").parse(istanzaDataA);
      model.addAttribute("istanzaDataA", locIstanzaDataA); 
      errors.validateDateInRange(locIstanzaDataA, "istanzaDataA", locIstanzaDataDa, new Date(), true, true);
    } catch (ParseException e) {      
      errors.addError("istanzaDataA", DateValidator.ERRORE_DATA_NON_VALIDA);
    }
    
    if (rtesto != null && rtesto.length() > 0) {
      model.addAttribute("rtesto", rtesto);
      errors.validateFieldLength(rtesto, "rtesto", 0, 50);
    }
    else model.addAttribute("rtesto",null);
    
    errors.addToModelIfNotEmpty(model);
    
    return index(model, session);
  }
  
  @RequestMapping(value = "/getLog", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<LogDTO> getElencoLog(
      HttpSession session, 
      Model model,
      @RequestParam(value="errors") String errors,
      @RequestParam(value="istanzaDataDa") String istanzaDataDa,
      @RequestParam(value="istanzaDataA") String istanzaDataA,
      @RequestParam(value="rtesto") String rtesto
      )  throws InternalUnexpectedException, ParseException
  {

    if (errors.length()>0) return new ArrayList<LogDTO>();
    
    // Non c'e errore, facciamo richiesta
    Date loc_istanzaDataDa = null;
    Date loc_istanzaDataA = null;
    try {
      loc_istanzaDataDa = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(istanzaDataDa);
      loc_istanzaDataA = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(istanzaDataA);
    } catch (ParseException e) { 
    }
    
    Date borderA = null;
    if (loc_istanzaDataA != null) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( loc_istanzaDataA );
        cal.add(Calendar.DATE, 1); //riceviamo mezzanotte del prossimo giorno
        borderA = cal.getTime();
    } 
    List<LogDTO> list = quadroIuffiEJB.getElencoLog(loc_istanzaDataDa, borderA, rtesto ); 
    if(list==null) {
      list = new ArrayList<LogDTO>();
    }
    return list;
  }

}
