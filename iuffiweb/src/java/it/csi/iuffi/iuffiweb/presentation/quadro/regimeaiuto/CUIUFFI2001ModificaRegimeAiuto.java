package it.csi.iuffi.iuffiweb.presentation.quadro.regimeaiuto;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.SportelloDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.regimeaiuto.RegimeAiuto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi2001m")
@IuffiSecurity(value = "CU-IUFFI-2001-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI2001ModificaRegimeAiuto extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-2001-M";
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
    RegimeAiuto datiProcedimento = quadroEJB.getRegimeAiutoProcedimentoOggetto(procedimentoOggetto.getIdProcedimentoOggetto()); 
    BandoDTO bando = quadroEJB.getInformazioniBandoByIdProcedimento(procedimentoOggetto.getIdProcedimento());
    List<DecodificaDTO<Long>> tipiAiuto = quadroEJB.getListaTipiAiuto(new Long(0), 4);
    for (DecodificaDTO<Long> decodificaDTO : tipiAiuto) {
      if(datiProcedimento != null && decodificaDTO.getDescrizione().equalsIgnoreCase(datiProcedimento.getDescbreve())) {
        datiProcedimento.setDescbreve(decodificaDTO.getId() + "");
      }
    }
    model.addAttribute("tipiAiuto", tipiAiuto);
    model.addAttribute("datiProcedimento", datiProcedimento);
    
    model.addAttribute("datiInseriti", datiProcedimento != null);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB, procedimentoOggetto.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "regimeaiuto/modificaRegimeAiuto";
  }
  
  @IsPopup
  @RequestMapping(value = "/elenco_sportelli_{abi}_{cab}_{denominazione}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<SportelloDTO> elencosportelli(Model model,
      @PathVariable("abi") String abi, @PathVariable("cab") String cab, @PathVariable("denominazione") String denominazione,
      HttpServletRequest request)
      throws Exception {
    List<SportelloDTO> sportelli = quadroEJB.getSportelliList(abi, cab, denominazione); 
    if(sportelli == null) {
      // In caso non venga trovato nessuna banca lancio una eccezzione in modo da segnalarlo alla popup
      throw new Exception();
    }
    return sportelli;
  }
  

  @RequestMapping("/confermaModifica")
  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  { 
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    RegimeAiuto datiProcedimento = quadroEJB.getRegimeAiutoProcedimentoOggetto(procedimentoOggetto.getIdProcedimentoOggetto()); 
    //controllo campi obbligatori
    Errors errors = new Errors();
    // Recupero il tipo di aiuto richiesto
    String tipoAiuto = IuffiUtils.STRING.trim(request.getParameter("tipo"));
    String idSportello = IuffiUtils.STRING.trim(request.getParameter("id"));
    String altroistituto = IuffiUtils.STRING.trim(request.getParameter("altroistituto"));
    String abi = IuffiUtils.STRING.trim(request.getParameter("abi"));
    String cab = IuffiUtils.STRING.trim(request.getParameter("cab"));
    String cap = IuffiUtils.STRING.trim(request.getParameter("cap"));
    String descBreve = IuffiUtils.STRING.trim(request.getParameter("tipo"));
    String pec = IuffiUtils.STRING.trim(request.getParameter("pec"));
    String email = IuffiUtils.STRING.trim(request.getParameter("email"));
    String denominazione = IuffiUtils.STRING.trim(request.getParameter("denominazione"));
    String desccomune = IuffiUtils.STRING.trim(request.getParameter("desccomune"));
    String des = IuffiUtils.STRING.trim(request.getParameter("des"));
    String provincia = IuffiUtils.STRING.trim(request.getParameter("provincia"));
    errors.validateMandatory(pec, "pec");
    errors.validateMandatory(email, "email");
    String erroreABI = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.ABI_NON_TROVATO);
    String erroreCAB = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.CAB_NON_TROVATO);
    String erroreDENOM = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.DENOM_NON_TROVATO);
    String erroreTOT = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.TOT_NON_TROVATO);
    String errore = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.MUTUOOPRESTITO);
    if(tipoAiuto.equalsIgnoreCase("")) {
      tipoAiuto = null;
      errors.validateMandatory(tipoAiuto, "tipo");
    }
    else if(tipoAiuto != null && (tipoAiuto.equalsIgnoreCase("1") || tipoAiuto.equalsIgnoreCase("2"))) {
      // Se il tipo di aiuto scelto è relativo ad un Mutuo/Prestito (M) si ritengono obbligatori in alternativa (non possono essere 
      // indicati entrambi) “Istituto bancario di emissione/ Filiale di sportello” o “Altro Istituto di emissione”
      if(abi.equalsIgnoreCase("")) {
        abi = null;
      }
      if(cab.equalsIgnoreCase("")) {
        cab = null;
      }
      if(denominazione.equalsIgnoreCase("")) {
        denominazione = null;
      }
      if(altroistituto.equalsIgnoreCase("")) {
        altroistituto = null;
      }
      if((abi != null || cab != null || denominazione != null) && altroistituto != null) {
        errors.addError("abi", "Inserire solo uno tra ABI, CAB DENOMINAZIONE oppure Denominazione indirizzo in caso di ALTRO ISTITUTO DI EMISSIONE (SE NON BANCARIO)");
        errors.addError("cab", "Inserire solo uno tra ABI, CAB DENOMINAZIONE oppure Denominazione indirizzo in caso di ALTRO ISTITUTO DI EMISSIONE (SE NON BANCARIO)");
        errors.addError("denominazione", "Inserire solo uno tra ABI, CAB DENOMINAZIONE oppure Denominazione indirizzo in caso di ALTRO ISTITUTO DI EMISSIONE (SE NON BANCARIO)");
        errors.addError("altroistituto", "Inserire solo uno tra ABI, CAB DENOMINAZIONE oppure Denominazione indirizzo in caso di ALTRO ISTITUTO DI EMISSIONE (SE NON BANCARIO)");
      } 
      else if(abi == null && cab == null && denominazione == null && altroistituto == null) {
        errors.addError("abi", errore);
        errors.addError("cab", errore);
        errors.addError("denominazione", errore);
        errors.addError("altroistituto", errore);
      }   
      else if(abi != null || cab != null || denominazione != null) {
        List<SportelloDTO> sportelli = quadroEJB.getSportelliList(abi, cab, denominazione);
        if(sportelli == null) {
          if((abi != null && cab != null && denominazione != null && !abi.equalsIgnoreCase("") && !cab.equalsIgnoreCase("") && !denominazione.equalsIgnoreCase("")) 
              || (abi != null && cab != null && !abi.equalsIgnoreCase("") && !cab.equalsIgnoreCase("")) 
              || (cab != null && denominazione != null && !cab.equalsIgnoreCase("") && !denominazione.equalsIgnoreCase("")) 
              || (abi != null && denominazione != null && !abi.equalsIgnoreCase("") && !denominazione.equalsIgnoreCase(""))) {
            if(abi != null) {
              errors.addError("abi", erroreTOT);
            }
            if(cab != null) {
              errors.addError("cab", erroreTOT);
            }
            if(denominazione != null) {
              errors.addError("denominazione", erroreTOT);
            } 
          }
          else {
            if(abi != null) {
              errors.addError("abi", erroreABI);
            }
            if(cab != null) {
              errors.addError("cab", erroreCAB);
            }
            if(denominazione != null) {
              errors.addError("denominazione", erroreDENOM);
            }                
          }
        }
        else if(sportelli.size() > 1) {
          errors.addError("abi", "Più di un risultato trovato. Specificare ulteriori filtri");
          errors.addError("cab", "Più di un risultato trovato. Specificare ulteriori filtri");
          errors.addError("denominazione", "Più di un risultato trovato. Specificare ulteriori filtri");
        }
        else {
          SportelloDTO sportello = sportelli.get(0);
          idSportello = String.valueOf(sportello.getId());
        }
      }   
    }
    else {
      List<SportelloDTO> sportelli = quadroEJB.getSportelliList(abi, cab, denominazione);
      if(sportelli == null) {
        if((abi != null && cab != null && denominazione != null && !abi.equalsIgnoreCase("") && !cab.equalsIgnoreCase("") && !denominazione.equalsIgnoreCase("")) 
            || (abi != null && cab != null && !abi.equalsIgnoreCase("") && !cab.equalsIgnoreCase("")) 
            || (cab != null && denominazione != null && !cab.equalsIgnoreCase("") && !denominazione.equalsIgnoreCase("")) 
            || (abi != null && denominazione != null && !abi.equalsIgnoreCase("") && !denominazione.equalsIgnoreCase(""))) {          
          if(abi != null && !abi.equalsIgnoreCase("")) {
            errors.addError("abi", erroreTOT);
          }
          if(cab != null && !cab.equalsIgnoreCase("")) {
            errors.addError("cab", erroreTOT);
          }
          if(denominazione != null && !denominazione.equalsIgnoreCase("")) {
            errors.addError("denominazione", erroreTOT);
          } 
        }
        else {
          if(abi != null && !abi.equalsIgnoreCase("")) {
            errors.addError("abi", erroreABI);
          }
          if(cab != null && !cab.equalsIgnoreCase("")) {
            errors.addError("cab", erroreCAB);
          }
          if(denominazione != null && !denominazione.equalsIgnoreCase("")) {
            errors.addError("denominazione", erroreDENOM);
          }                
        }
      }
      else if(sportelli.size() > 1) {
        errors.addError("abi", "Più di un risultato trovato. Specificare ulteriori filtri");
        errors.addError("cab", "Più di un risultato trovato. Specificare ulteriori filtri");
        errors.addError("denominazione", "Più di un risultato trovato. Specificare ulteriori filtri");
      }
      else {
        SportelloDTO sportello = sportelli.get(0);
        idSportello = String.valueOf(sportello.getId());
      }
    }  
    if (!errors.isEmpty()){
      QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
      List<DecodificaDTO<Long>> tipiAiuto = quadroEJB.getListaTipiAiuto(new Long(0), 4);     
      RegimeAiuto datiProcedimentoIns = new RegimeAiuto();
      datiProcedimentoIns.setAbi(abi);
      datiProcedimentoIns.setAltroistituto(altroistituto);
      datiProcedimentoIns.setCab(cab);
      datiProcedimentoIns.setCap(cap);
      datiProcedimentoIns.setDenominazione(denominazione);
      datiProcedimentoIns.setDescbreve(descBreve);
      datiProcedimentoIns.setDesccomune(desccomune);
      datiProcedimentoIns.setDescestesa(des);
      datiProcedimentoIns.setEmail(email);
      datiProcedimentoIns.setPec(pec);
      datiProcedimentoIns.setProvincia(provincia);     
      model.addAttribute("tipiAiuto", tipiAiuto);
      model.addAttribute("datiProcedimento", datiProcedimentoIns);
      model.addAttribute("errors", errors);
      model.addAttribute("datiInseriti", datiProcedimento != null);
      model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB, procedimentoOggetto.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
      return "regimeaiuto/modificaRegimeAiuto";
    }else{
      //SALVO SU DB E RICARICO
      Long sportello = null;
      if(altroistituto != null && !altroistituto.equalsIgnoreCase("")) {
        sportello = null;
      }
      else if(!idSportello.equalsIgnoreCase("")) {
        sportello = new Long(idSportello);
      }
      else {
        List<SportelloDTO> sportelli = quadroEJB.getSportelliList(abi, cab, denominazione);
        if(sportelli != null && sportelli.size() == 1) {
          SportelloDTO sportelloS = sportelli.get(0);
          idSportello = String.valueOf(sportelloS.getId());
          sportello = new Long(idSportello);
        }
          
      }
      quadroEJB.deleteRegimeAiuti(idProcedimentoOggetto);
      quadroEJB.insertRegimeAiuti(idProcedimentoOggetto, Long.parseLong(tipoAiuto), sportello, pec, email, altroistituto, getLogOperationOggettoQuadroDTO(session), getLogOperationOggettoQuadroDTO(session).getExtIdUtenteAggiornamento());
    }
    return "redirect:../cuiuffi2001d/index.do";
  }
  
  @IsPopup
  @RequestMapping(value = "/popup_ricerca_sportello_{abi}_{cab}_{denominazione}", method = RequestMethod.GET)
  public String popupRicercaComuniAltroistituto(Model model,  @PathVariable("abi") String abi, @PathVariable("cab") String cab, @PathVariable("denominazione") String denominazione, HttpServletRequest request) throws InternalUnexpectedException {
    String erroreABI = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.ABI_NON_TROVATO);
    String erroreCAB = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.CAB_NON_TROVATO);
    String erroreDENOM = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.DENOM_NON_TROVATO);
    String erroreTOT = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.TOT_NON_TROVATO);
    model.addAttribute("erroreABI", erroreABI);
    model.addAttribute("erroreCAB", erroreCAB);
    model.addAttribute("erroreDENOM", erroreDENOM);
    model.addAttribute("erroreTOT", erroreTOT);
    model.addAttribute("abi", abi);
    model.addAttribute("cab", cab);
    model.addAttribute("denominazione", denominazione);
    return "regimeaiuto/popupRicercaSportello";
  }
}