package it.csi.iuffi.iuffiweb.presentation.stampeoggetto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.iuffi.iuffiweb.business.IAsyncEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.ProcedimOggettoStampaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoIconaDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.pdf.PDFWriteSignatureData;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellEsitoVO;
import it.csi.smrcomms.smrcomm.exception.SmrcommInternalException;

@Controller
@IuffiSecurity(value = "CU-IUFFI-126-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi126l")
public class CUIUFFI126LListaStampeOggetto extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;
  @Autowired
  IAsyncEJB  asyncEJB  = null;

  @RequestMapping(value = "/conferma_rigenera_{idOggettoIcona}")
  public String confermaRigenera(HttpSession session, Model model,
      @PathVariable("idOggettoIcona") long idOggettoIcona)
      throws InternalUnexpectedException
  {
    ProcedimOggettoStampaDTO procedimOggettoStampaDTO = quadroEJB
        .getProcedimOggettoStampaByIdOggetoIcona(
            getIdProcedimentoOggetto(session), idOggettoIcona);
    if (procedimOggettoStampaDTO.getExtIdDocumentoIndex() != null)
    {
      Errors errors = new Errors();
      errors.addError("error", "La stampa e' attualmente disponibile");
      errors.addToModelIfNotEmpty(model);
    }
    else
    {
      if (procedimOggettoStampaDTO
          .getIdStatoStampa() == IuffiConstants.STATO.STAMPA.ID.GENERAZIONE_STAMPA_IN_CORSO)
      {
        if (IuffiUtils.DATE.diffInSeconds(new Date(),
            procedimOggettoStampaDTO
                .getDataInizio()) < IuffiConstants.TEMPO.SECONDI_PRIMA_DI_RIGENERARE_UNA_STAMPA) // 10
        // minuti
        {
          Errors errors = new Errors();
          errors.addError("error",
              "La stampa richiesta e' attualmente in corso di generazione, l'operazione potrebbe richiedere alcuni minuti, si prega di attendere");
          errors.addToModelIfNotEmpty(model);
        }
        else
        {
          model.addAttribute("messaggio",
              "La stampa è in corso di generazione da più di 10 minuti, &egrave; possibile che si sia verificato un errore, si desidera rigenerarla?");
        }
      }
      else
      {
        model.addAttribute("messaggio",
            "La stampa è terminata con degli errori, &egrave; possibile che il problema fosse solo temporaneo, si desidera provare rigenerare la stampa?");
      }
    }
    return "stampeoggetto/confermaRigenera";
  }

  @RequestMapping(value = "/rigenera_{idOggettoIcona}", method = RequestMethod.GET)
  public String rigeneraPost(HttpSession session, Model model,
      @PathVariable("idOggettoIcona") long idOggettoIcona)
      throws InternalUnexpectedException
  {
    Boolean daRistampare = quadroEJB.ripristinaStampaOggetto(
        getIdProcedimentoOggetto(session), idOggettoIcona,
        getIdUtenteLogin(session));
    if (Boolean.TRUE.equals(daRistampare))
    {
      asyncEJB.generaStampa(getIdProcedimentoOggetto(session),
          getIdProcedimento(session), idOggettoIcona);
    }
    return "redirect:/cuiuffi126l/attendere_prego_" + idOggettoIcona + ".do";
  }

  @RequestMapping(value = "/attendere_prego_{idOggettoIcona}")
  public String attenderePrego(HttpSession session, Model model,
      @PathVariable("idOggettoIcona") long idOggettoIcona)
      throws InternalUnexpectedException
  {
    ProcedimOggettoStampaDTO stampa = quadroEJB
        .getProcedimOggettoStampaByIdOggetoIcona(
            getIdProcedimentoOggetto(session), idOggettoIcona);
    if (stampa != null)
    {
      if (stampa.getExtIdDocumentoIndex() != null)
      {
        // Ho trovato il documento, non mi interessa lo stato, DEVE ESSERE uno
        // di quelli OK
        model.addAttribute("pdf",
            "../cuiuffi126l/visualizza_" + idOggettoIcona + ".do");
        return "stampeoggetto/documentoDisponibile";
      }
      else
      {
        long idStatoStampa = stampa.getIdStatoStampa();
        if (idStatoStampa == IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA)
        {
          model.addAttribute("messaggio",
              "La generazione della stampa &egrave; fallita");
          return "stampeoggetto/errore";
        }
      }
    }
    else
    {
      model.addAttribute("messaggio",
          "La stampa richiesta non &egrave; stata trovata. E' possibile che sia stata cancellata da qualche altro utente");
      return "stampeoggetto/errore";
    }
    return "stampeoggetto/attenderePrego";
  }

  @RequestMapping("/lista")
  public String lista(HttpSession session, Model model)
      throws InternalUnexpectedException,
      ApplicationException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    final long idProcedimentoOggetto = procedimentoOggetto
        .getIdProcedimentoOggetto();
    List<StampaOggettoDTO> stampe = quadroEJB
        .getElencoStampeOggetto(idProcedimentoOggetto, null);
    if (stampe != null && !stampe.isEmpty())
    {
      List<Long> ids = new ArrayList<Long>();
      List<StampaOggettoDTO> stampeNew = new ArrayList<>();
      for (StampaOggettoDTO stampa : stampe)
      {
        /*
         * if(procedimentoOggetto.getIdStatoOggetto().longValue() !=
         * IuffiConstants.STATO.OGGETTO.ID.APPROVATO ||
         * (procedimentoOggetto.getIdStatoOggetto().longValue() ==
         * IuffiConstants.STATO.OGGETTO.ID.APPROVATO &&
         * stampa.getCodiceCdu().equals(IuffiConstants.USECASE.STAMPE_OGGETTO
         * .INSERISCI_STAMPA_OGGETTO))) { stampeNew.add(stampa); Long
         * extIdUtenteAggiornamento = stampa.getExtIdUtenteAggiornamento(); if
         * (extIdUtenteAggiornamento != null) {
         * ids.add(extIdUtenteAggiornamento); } }
         */
        stampeNew.add(stampa);
        Long extIdUtenteAggiornamento = stampa.getExtIdUtenteAggiornamento();
        if (extIdUtenteAggiornamento != null)
        {
          ids.add(extIdUtenteAggiornamento);
        }

      }
      stampe = stampeNew;
      Map<Long, UtenteLogin> utenti = getMapUtenti(ids);
      for (StampaOggettoDTO stampa : stampe)
      {
        Long idUtenteAggiornamento = stampa.getExtIdUtenteAggiornamento();
        if (idUtenteAggiornamento != null)
        {
          stampa.setDescUltimoAggiornamento(
              getDescUltimoAggiornamento(utenti.get(idUtenteAggiornamento),
                  stampa.getDataUltimoAggiornamento()));
        }
      }
    }
    boolean oggettoChiuso = procedimentoOggetto.getDataFine() != null;
    boolean stampeUtenteVisibili = oggettoChiuso;
    model.addAttribute("oggettoChiuso", Boolean.valueOf(oggettoChiuso));
    
    if(IuffiConstants.FLAGS.SI.equals(procedimentoOggetto.getFlagIstanza()))
    {
      //gestione inserimento doc istanze
      boolean stampaIstanzaCancellabile = procedimentoOggetto.getDataFine() != null && procedimentoOggetto.getIdStatoOggetto().longValue() < IuffiConstants.STATO.ITER.ID.TRASMESSO;
      stampeUtenteVisibili = stampaIstanzaCancellabile;
      model.addAttribute("stampaIstanzaCancellabile", Boolean.valueOf(stampaIstanzaCancellabile));
      if (stampeUtenteVisibili)
      {
        List<StampaOggettoIconaDTO> list = quadroEJB.getElencoDocumentiStampeDaAllegare(idProcedimentoOggetto, null,IuffiConstants.USECASE.STAMPE_OGGETTO.INSERISCI_STAMPA_OGGETTO_ISTANZA);
        stampeUtenteVisibili = list != null && !list.isEmpty();
      }
      model.addAttribute("stampeUtenteIstanzaVisibili", Boolean.valueOf(stampeUtenteVisibili));
    }else 
    {
      //  gestione inserimento doc istruttorie
      boolean stampaCancellabile = procedimentoOggetto.getDataFine() != null && (procedimentoOggetto.getIdEsito() == null || !quadroEJB.isEsitoApprovato(procedimentoOggetto.getIdEsito()));
      model.addAttribute("stampaCancellabile", Boolean.valueOf(stampaCancellabile));
      if (stampeUtenteVisibili)
      {
        List<StampaOggettoIconaDTO> list = quadroEJB.getElencoDocumentiStampeDaAllegare(idProcedimentoOggetto, null,IuffiConstants.USECASE.STAMPE_OGGETTO.INSERISCI_STAMPA_OGGETTO);
        stampeUtenteVisibili = list != null && !list.isEmpty();
       }
      model.addAttribute("stampeUtenteVisibili", Boolean.valueOf(stampeUtenteVisibili));
    }
    
    model.addAttribute("stampe", stampe);
    return "stampeoggetto/lista";
  }

  @RequestMapping(value = "/visualizza_{idOggettoIcona}", method = RequestMethod.POST, produces = "text/html")
  @ResponseBody
  public String visualizzaPost(HttpSession session, Model model,
      @PathVariable("idOggettoIcona") long idOggettoIcona)
      throws InternalUnexpectedException, ApplicationException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    long idProcedimentoOggetto = po.getIdProcedimentoOggetto();
    if (po.getDataFine() == null)
    {
      List<StampaOggettoDTO> stampe = quadroEJB
          .getElencoStampeOggetto(idProcedimentoOggetto, idOggettoIcona);
      if (stampe != null && !stampe.isEmpty())
      {
        StampaOggettoDTO stampa = stampe.get(0);
        if (!IuffiConstants.FLAGS.SI
            .equals(stampa.getFlagStampaOggettoAperto()))
        {
          return "<error>Impossibile stampare questo documento fino a quando non si esegue la chiusura dell'oggetto corrente</error>";
        }
        else
        {
          return getTagUrlStampa(stampa.getCodiceCdu());
        }
      }
      else
      {
        return "<error>Stampa non trovata. E' possibile che sia stata cancellata da un altro utente</error>";
      }
    }
    else
    {
      ProcedimOggettoStampaDTO procedimOggettoStampaDTO = quadroEJB
          .getProcedimOggettoStampaByIdOggetoIcona(idProcedimentoOggetto,
              idOggettoIcona);
      if (procedimOggettoStampaDTO != null)
      {
        if (procedimOggettoStampaDTO.getExtIdDocumentoIndex() != null) // Esiste
                                                                       // un
                                                                       // record
                                                                       // su db
        {
          return "<stampa>../cuiuffi126l/visualizza_"
              + procedimOggettoStampaDTO.getIdOggettoIcona() + ".do</stampa>";
        }
        else
        {
          if (procedimOggettoStampaDTO
              .getIdStatoStampa() == IuffiConstants.STATO.STAMPA.ID.GENERAZIONE_STAMPA_IN_CORSO)
          {
            if (IuffiUtils.DATE.diffInSeconds(new Date(),
                procedimOggettoStampaDTO
                    .getDataInizio()) > IuffiConstants.TEMPO.SECONDI_PRIMA_DI_RIGENERARE_UNA_STAMPA) // 10
            // minuti
            {
              return "<renew>../cuiuffi126l/conferma_rigenera_"
                  + procedimOggettoStampaDTO.getIdOggettoIcona()
                  + ".do</renew>";
            }
            else
            {
              return "<error>La stampa richiesta e' attualmente in corso di generazione, l'operazione potrebbe richiedere alcuni minuti, si prega di attendere</error>";
            }
          }
          else
          {
            return "<renew>../cuiuffi126l/conferma_rigenera_"
                + procedimOggettoStampaDTO.getIdOggettoIcona() + ".do</renew>";
          }
        }
      }
      else
      {
        return "<error>Errore di configurazione, per questa stampa non e' mai stata richiesta la generazione e non puo' essere prodotta a oggetto chiuso</error>";
      }
    }
  }

  @RequestMapping(value = "/visualizza_{idOggettoIcona}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> visualizzaGet(HttpSession session, Model model,
      @PathVariable("idOggettoIcona") long idOggettoIcona) throws Exception
  {
    ProcedimOggettoStampaDTO procedimOggettoStampaDTO = quadroEJB
        .getProcedimOggettoStampaByIdOggetoIcona(
            getIdProcedimentoOggetto(session), idOggettoIcona);
    AgriWellEsitoVO esito = IuffiUtils.PORTADELEGATA
        .getAgriwellCSIInterface().agriwellServiceLeggiDoquiAgri(
            procedimOggettoStampaDTO.getExtIdDocumentoIndex());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        IuffiUtils.FILE.getMimeType(esito.getNomeFile()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + esito.getNomeFile() + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        esito.getContenutoFile(), httpHeaders, HttpStatus.OK);
    return response;
  }
  
  @RequestMapping(value = "/contenutoStampaProcedimentoOggetto", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String contenutostampa(HttpSession session, Model model) throws Exception
  {
	  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	  List<StampaOggettoDTO> stampe = quadroEJB.getElencoStampeOggetto(idProcedimentoOggetto, null);
      if (stampe != null && !stampe.isEmpty())
      {
    	long idOggettoIcona = stampe.get(0).getIdOggettoIcona();
    	ProcedimOggettoStampaDTO procedimOggettoStampaDTO = quadroEJB.getProcedimOggettoStampaByIdOggetoIcona(idProcedimentoOggetto, idOggettoIcona);
        AgriWellEsitoVO esito = IuffiUtils.PORTADELEGATA.getAgriwellCSIInterface().agriwellServiceLeggiDoquiAgri(procedimOggettoStampaDTO.getExtIdDocumentoIndex());
        return  org.apache.axis.encoding.Base64.encode(esito.getContenutoFile());
      }
	  return "";
  }
  
  
  @RequestMapping(value = "/getNumPaginaDichiarazioni", method = RequestMethod.GET)
  @ResponseBody
  public int getNumPaginaDichiarazioni(HttpSession session, Model model) throws Exception
  {
	return getNumPaginaFromPDF(session, "Quadro - Dichiarazioni");  
  }
  
  @RequestMapping(value = "/getNumPaginaImpegni", method = RequestMethod.GET)
  @ResponseBody
  public int getNumPaginaImpegni(HttpSession session, Model model) throws Exception
  {
	return getNumPaginaFromPDF(session, "Quadro - Impegni");  
  }
  
  
  private int getNumPaginaFromPDF(HttpSession session, String textToFind) throws InternalUnexpectedException, IOException, SmrcommInternalException, InternalServiceException, UnrecoverableException
  {
	  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	  List<StampaOggettoDTO> stampe = quadroEJB.getElencoStampeOggetto(idProcedimentoOggetto, null);
      if (stampe != null && !stampe.isEmpty())
      {
    	long idOggettoIcona = stampe.get(0).getIdOggettoIcona();
    	ProcedimOggettoStampaDTO procedimOggettoStampaDTO = quadroEJB.getProcedimOggettoStampaByIdOggetoIcona(idProcedimentoOggetto, idOggettoIcona);
        AgriWellEsitoVO esito = IuffiUtils.PORTADELEGATA.getAgriwellCSIInterface().agriwellServiceLeggiDoquiAgri(procedimOggettoStampaDTO.getExtIdDocumentoIndex());
        return  PDFWriteSignatureData.getPageFromString(esito.getContenutoFile(), textToFind);
      }
	  return 1;
  }

  protected String getTagUrlStampa(String codiceCdu)
  {
    codiceCdu = codiceCdu.replace("-", "").toLowerCase();
    return "<stampa>../" + codiceCdu + "/stampa.do</stampa>";
  }
}
