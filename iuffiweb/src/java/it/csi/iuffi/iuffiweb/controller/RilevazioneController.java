package it.csi.iuffi.iuffiweb.controller;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.business.IGestioneVisualEJB;
import it.csi.iuffi.iuffiweb.business.IMissioneEJB;
import it.csi.iuffi.iuffiweb.business.IRilevazioneEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.ErrorResponse;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.RilevazioneDTO;
import it.csi.iuffi.iuffiweb.model.api.Rilevazione;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.TokenUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class RilevazioneController  extends TabelleController
{

  @Autowired
  private IRilevazioneEJB rilevazioneEJB;
  
  @Autowired
  private IAnagraficaEJB anagraficaEJB;
  
  @Autowired
  private IGestioneVisualEJB visualEJB;
  
  @Autowired
  private IMissioneEJB missioneEJB;
  
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  private static final SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  private static final SimpleDateFormat htf = new SimpleDateFormat("HH:mm:ss");

  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }
  
  @RequestMapping(value = "/rilevazione/search")
  public String search(Model model, @ModelAttribute("rilevazione") RilevazioneDTO rilevazione ,HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    List<RilevazioneDTO> list = rilevazioneEJB.findByFilter(rilevazione);
    model.addAttribute("listaRilevazione", list);
    
    setBreadcrumbs(model, request);
    return "gestionetabelle/anagrafica";
  }

  @RequestMapping(value = "/rilevazione/remove")
  public RedirectView remove(Model model, @RequestParam(value = "idAnagrafica") String idAnagrafica, HttpServletRequest request, RedirectAttributes attributes) throws InternalUnexpectedException
  {
   /* anagraficaEJB.remove(Integer.decode(idAnagrafica));
    List<AnagraficaDTO> list = anagraficaEJB.findAll();
    attributes.addFlashAttribute("lista", list);*/
    return new RedirectView("showFilter.do", true);
  }
  
  @RequestMapping(value = "/rest/rilevazione", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
  public @ResponseBody ResponseEntity<?> creaRilevazione(@Valid @RequestBody Rilevazione body, HttpServletRequest request) throws MalformedURLException, IOException
  {   
    Rilevazione rilevazione = body;
    Integer idRilevazione = null;
    try {
      String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      String cf = TokenUtils.verifyToken(jwt);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(rilevazione);
      logger.debug(json);

      if (rilevazione.getIdMissione() == null) {
        throw new Exception("L'idMissione è obbligatorio");
      }

      if (StringUtils.isBlank(rilevazione.getOraInizio())) {
        throw new Exception("L'ora inizio è obbligatoria");
      }

      MissioneDTO missione = missioneEJB.findById(body.getIdMissione().longValue());
      
      if (missione == null) {
        throw new Exception("Id missione non trovato");
      }
      
      RilevazioneDTO rilDTO = new RilevazioneDTO();

      AnagraficaDTO anagraficaFilter = new AnagraficaDTO(true);   // Modificato il 28/04/2021 (S.D.) - Rilevato un bug testando l'app
      anagraficaFilter.setCfAnagraficaEst(cf);
      List<AnagraficaDTO> list = anagraficaEJB.findByFilter(anagraficaFilter);
      if (list != null && list.size() > 0) {
        rilDTO.setIdAnagrafica(list.get(0).getIdAnagrafica());
      } else {
        rilDTO.setIdAnagrafica(body.getIdAnagrafica());
      }

      rilDTO.setIdRilevazione(body.getIdRilevazione());
      rilDTO.setIdMissione(body.getIdMissione());
      rilDTO.setIdTipoArea(body.getIdTipoArea());
      rilDTO.setVisual(body.getVisual());
      rilDTO.setNote(body.getNote());
      rilDTO.setCampionamento(body.getCampionamento());
      rilDTO.setTrappolaggio(body.getTrappolaggio());

      String dataOraInizioStr = ((StringUtils.isBlank(body.getDataRilevazione()))?sdf.format(missione.getDataOraInizioMissione()):body.getDataRilevazione()) + " " + body.getOraInizio() + ":00";
      Date dataOraInizio = dtf.parse(dataOraInizioStr);
      rilDTO.setDataOraInizio(dataOraInizio);

      if (StringUtils.isNotBlank(body.getOraFine())) {
        String dataOraFineStr = ((StringUtils.isBlank(body.getDataRilevazione()))?sdf.format(missione.getDataOraInizioMissione()):body.getDataRilevazione()) + " " + body.getOraFine() + ":00";
        Date dataOraFine = dtf.parse(dataOraFineStr);
        rilDTO.setDataOraFine(dataOraFine);
      } else
        rilDTO.setDataOraFine(null);

      rilDTO.setFlagEmergenza(body.getFlagEmergenza());
      rilDTO.setNumeroAviv(body.getNumeroAviv());
      rilDTO.setCuaa(body.getCuaa());
      rilDTO.setIdUte(body.getIdUte());

      try {
        UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
        rilDTO.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
      } catch (Exception e) {
        rilDTO.setExtIdUtenteAggiornamento(0L);
      }

      try
      {
        idRilevazione = rilevazione.getIdRilevazione();
        if (rilevazione.getIdRilevazione() == null || rilevazione.getIdRilevazione().intValue() == 0) {
          idRilevazione = rilevazioneEJB.insert(rilDTO);
        }
        else {
          rilevazioneEJB.update(rilDTO);
        }

        rilevazione.setIdRilevazione(idRilevazione);

        List<IspezioneVisivaDTO> ispezioniVisive = visualEJB.findByFilter(new IspezioneVisivaDTO(rilevazione.getIdRilevazione()));
        rilevazione.setIspezioniVisive(ispezioniVisive);

      }
      catch (InternalUnexpectedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
        logger.debug("Errore nel metodo creaRilevazione in fase di insert: " + e.getMessage());
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", e.getMessage());
        err.setMessage("Errore nel metodo creaRilevazione durante la registrazione: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    catch(Throwable e) {
      logger.debug("Errore nel metodo creaRilevazione durante la registrazione: " + e.getMessage());
      ErrorResponse err = new ErrorResponse();
      err.addError("Errore", e.getMessage());
      err.setMessage("Errore nel metodo creaRilevazione durante la registrazione: " + e.getMessage());
      return new ResponseEntity<ErrorResponse>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Rilevazione>(rilevazione, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/rest/rilevazione/{idRilevazione}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<?> deleteRilevazione(@PathVariable (value = "idRilevazione") Integer idRilevazione, HttpServletRequest request)
  {
    try {
      logger.debug("id_rilevazione: " + idRilevazione);
      RilevazioneDTO rilDTO = new RilevazioneDTO();
      rilDTO.setIdRilevazione(idRilevazione);
      List<RilevazioneDTO> list = rilevazioneEJB.findByFilter(rilDTO);
      if (list == null || list.size() == 0) {
        throw new Exception("Rilevazione con id " + idRilevazione + " non trovata");
      }
      
      rilevazioneEJB.remove(idRilevazione);
      
    } catch (Exception e) {
        logger.debug("Errore nel metodo getMissione: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nella cancellazione della missione: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Map<String, String> ok = new HashMap<String, String>();
    ok.put("status", "ok");
    return new ResponseEntity<java.util.Map<String,String>>(ok,HttpStatus.OK);
  }
  
  @RequestMapping(value = "/rest/rilevazione", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
  @ResponseBody
  public ResponseEntity<?> updateRilevazione(@Valid @RequestBody Rilevazione body, HttpServletRequest request)
  {
    try {
      String jwt = request.getHeader(IuffiConstants.HEADER_TOKEN);
      String cf = TokenUtils.verifyToken(jwt);
      //logger.debug("id_rilevazione: " + idRilevazione);
      RilevazioneDTO rilDTO = new RilevazioneDTO();
      rilDTO.setIdRilevazione(body.getIdRilevazione());
      List<RilevazioneDTO> list = rilevazioneEJB.findByFilter(rilDTO);
      if (list == null || list.size() == 0) {
        logger.debug("Rilevazione con id " + body.getIdRilevazione() + " non trovata");
        ErrorResponse err = new ErrorResponse();
        err.addError("Errore", "Rilevazione con id " + body.getIdRilevazione() + " non trovata");
        err.setMessage("Rilevazione con id " + body.getIdRilevazione() + " non trovata");
        return new ResponseEntity<ErrorResponse>(err, HttpStatus.NOT_FOUND);
      }

      List<AnagraficaDTO> listAnag = anagraficaEJB.findByFilter(new AnagraficaDTO(cf));
      if (listAnag != null && listAnag.size() > 0) {
          rilDTO.setIdAnagrafica(listAnag.get(0).getIdAnagrafica());
      } else {
          rilDTO.setIdAnagrafica(body.getIdAnagrafica());
      }
      
    //rilDTO = ObjectMapper.readValue (json, RilevazioneDTO.class);
    //rilDTO.setIdRilevazione(body.getIdRilevazione());
    rilDTO.setIdMissione(body.getIdMissione());
    //rilDTO.setIdAnagrafica(body.getIdAnagrafica());
    rilDTO.setIdTipoArea(body.getIdTipoArea());
    rilDTO.setVisual(body.getVisual());
    rilDTO.setNote(body.getNote());
    rilDTO.setCampionamento(body.getCampionamento());
    rilDTO.setTrappolaggio(body.getTrappolaggio());
//    rilDTO.setOraInizio(body.getOraInizio());
//    rilDTO.setOraFine(body.getOraFine());
    
    String dataOraInizioStr = body.getDataRilevazione().replaceAll("-", "/") + " " + body.getOraInizio() + ":00";
    Date dataOraInizio = dtf.parse(dataOraInizioStr);
    rilDTO.setDataOraInizio(dataOraInizio);

    if (StringUtils.isNotBlank(body.getOraFine())) {
      String dataOraFineStr = body.getDataRilevazione().replaceAll("-", "/") + " " + body.getOraFine() + ":00";
      Date dataOraFine = dtf.parse(dataOraFineStr);
      rilDTO.setDataOraFine(dataOraFine);
    }
          
     rilDTO.setFlagEmergenza(body.getFlagEmergenza());
    //ril.setExtIdUtenteAggiornamento(body.getExtIdUtenteAggiornamento());
   
    try {
      UtenteAbilitazioni utente = (UtenteAbilitazioni) request.getSession().getAttribute("utenteAbilitazioni");
      rilDTO.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
    } catch (Exception e) {
      rilDTO.setExtIdUtenteAggiornamento(0L);
    }
      
      rilevazioneEJB.update(rilDTO);
      
    } catch (Exception e) {
        logger.debug("Errore: " + e.getMessage());
        ErrorResponse er = new ErrorResponse();
        er.addError("Errore", e.getMessage());
        er.setMessage("Errore interno nell'update: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Map<String, String> ok = new HashMap<String, String>();
    ok.put("status", "ok");
    return new ResponseEntity<java.util.Map<String,String>>(ok,HttpStatus.OK);
  }
  
  
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }


  
}
