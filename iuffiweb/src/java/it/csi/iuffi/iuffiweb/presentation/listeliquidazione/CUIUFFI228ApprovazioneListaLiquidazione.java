package it.csi.iuffi.iuffiweb.presentation.listeliquidazione;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.dto.StampaListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.LivelliBandoDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoImportiApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi228")
@IuffiSecurity(value = "CU-IUFFI-228", controllo = IuffiSecurity.Controllo.DEFAULT)
public class CUIUFFI228ApprovazioneListaLiquidazione extends BaseController
    implements HandlerExceptionResolver
{
  private static final String BASE_JSP_URL         = "listeliquidazione/";
  @Autowired
  IListeLiquidazioneEJB       listeLiquidazioneEJB = null;

  @RequestMapping(value = "/approva_{idListaLiquidazione}", method = RequestMethod.GET)
  public String approva(HttpSession session, Model model,
      @ModelAttribute("idListaLiquidazione") @PathVariable("idListaLiquidazione") long idListaLiquidazione)
      throws InternalUnexpectedException, ApplicationException
  {
    Map<String, Object> common = getCommonFromSession("APPROVA_LISTA", session,
        true);
    final String error = (String) common.get("error");
    if (error != null)
    {
      /**
       * Se c'è un attributo "common" in sessione per questa controller è dovuto
       * ad un errore di MaxUploadSizeExceededException durante l'upload del
       * file della stampa firmata. Segnalo l'errore
       */
      Errors errors = new Errors();
      errors.addError("stampaFirmata", error);
      errors.addToModel(model);
      clearCommonInSession(session);
    }
    RigaJSONElencoListaLiquidazioneDTO lista = listeLiquidazioneEJB
        .getListaLiquidazioneById(idListaLiquidazione);
    model.addAttribute("lista", lista);
    validaAccessoLista(lista, getUtenteAbilitazioni(session));
    creaModelloDatiPerApprovazione(model, idListaLiquidazione, lista);
    return BASE_JSP_URL + "approva";
  }

  protected void creaModelloDatiPerApprovazione(Model model,
      long idListaLiquidazione,
      RigaJSONElencoListaLiquidazioneDTO lista)
      throws InternalUnexpectedException
  {
    final long idBando = lista.getIdBando();
    model.addAttribute("descAmmCompetenza", lista.getDescOrganismoDelegato());
    model.addAttribute("descTipoImporto", lista.getDescTipoImporto());
    String codFiscTecnicoLiquidatore = lista.getCodFiscTecnicoLiquidatore();
    if (!GenericValidator.isBlankOrNull(codFiscTecnicoLiquidatore))
    {
      model.addAttribute("descTecnico",
          IuffiUtils.STRING.concat(" ", lista.getCognomeTecnicoLiquidatore(),
              lista.getNomeTecnicoLiquidatore(), "-",
              codFiscTecnicoLiquidatore));
    }
    final LivelliBandoDTO livelliBando = listeLiquidazioneEJB
        .getLivelliBando(idBando);
    assert livelliBando != null : "listeLiquidazioneEJB.getLivelliBando non ha trovato dati per idBando = "
        + idBando;
    List<RiepilogoImportiApprovazioneDTO> riepilogo = listeLiquidazioneEJB
        .getRiepilogoImportiApprovazione(idListaLiquidazione);
    int totaleNumPagamenti = 0;
    BigDecimal totaleImporto = BigDecimal.ZERO;
    for (RiepilogoImportiApprovazioneDTO riepilogoImporto : riepilogo)
    {
      totaleNumPagamenti += riepilogoImporto.getNumPagamenti();
      totaleImporto = totaleImporto.add(riepilogoImporto.getImportoLiquidato());
    }
    model.addAttribute("riepilogo", riepilogo);
    model.addAttribute("totaleNumPagamenti", totaleNumPagamenti);
    model.addAttribute("totaleImporto", totaleImporto);
    model.addAttribute("livelliBando", livelliBando);
  }

  @RequestMapping(value = "/approva_{idListaLiquidazione}", method = RequestMethod.POST)
  public String approvaPost(HttpSession session, Model model,
      @ModelAttribute("idListaLiquidazione") @PathVariable("idListaLiquidazione") long idListaLiquidazione,
      @RequestParam(value = "stampaFirmata", required = false) MultipartFile stampaFirmata)
      throws InternalUnexpectedException, ApplicationException
  {
    clearCommonInSession(session);
    RigaJSONElencoListaLiquidazioneDTO lista = listeLiquidazioneEJB
        .getListaLiquidazioneById(idListaLiquidazione);
    model.addAttribute("lista", lista);
    validaAccessoLista(lista, getUtenteAbilitazioni(session));
    if (!IuffiConstants.STATO.LISTE_LIQUIDAZIONE.FLAG.DA_APPROVARE
        .equals(lista.getFlagStatoLista()))
    {
      throw new ApplicationException(
          "La lista di liquidazione non si trova nello stato corretto per l'operazione, si prega di rieseguire la validazione da capo, impossibile procedere");
    }
    if (validateAndUpdate(model, idListaLiquidazione, stampaFirmata, session))
    {
      // Se è andato tutto bene nella validazione la lista ha cambiato stato ==>
      // redirigo sull'elenco liste
      return "redirect:../cuiuffi227/index.do";
    }
    // Altrimenti ripropongo la pagina con l'errore
    creaModelloDatiPerApprovazione(model, idListaLiquidazione, lista);
    return BASE_JSP_URL + "approva";
  }

  private boolean validateAndUpdate(Model model, long idListaLiquidazione,
      MultipartFile stampaFirmata,
      HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    Errors errors = new Errors();
    if (stampaFirmata == null || stampaFirmata.isEmpty())
    {
      errors.addError("stampaFirmata", Errors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      String nomeFile = IuffiUtils.FILE
          .getSafeSubmittedFileName(stampaFirmata.getOriginalFilename());
      if (nomeFile == null)
      {
        errors.addError("stampaFirmata",
            "Impossibile individuare il nome del file inviato. Si prega di verificare che il browser in uso sia supportato");
      }
      else
      {
        if (nomeFile.length() > 255)
        {
          // Teoricamente nei più usati filesystem la lunghezza di un nome di
          // file è già limitata a valori inferiori a
          // uguali a 255 caratteri...
          errors.addError("stampaFirmata",
              "Il mome del file è troppo lungo, la lunghezza massima ammessa è di 255 caratteri");
        }
      }
    }
    if (errors.addToModelIfNotEmpty(model))
    {
      return false;
    }
    
    /*
     * Il metodo approvaLista non rilancia eccezioni ApplicationException in
     * quanto DEVE sempre fare commit delle modifiche (vedi descrizione
     * specifica sul metodo stesso) e l'ApplicationException forza invece il
     * rollback. Quindi il metodo restituisce l'eventuale eccezione che rilancio
     * a mano
     */
    ApplicationException exception = listeLiquidazioneEJB.approvaLista(
        idListaLiquidazione, stampaFirmata,
        getUtenteAbilitazioni(session));
    if (exception != null)
    {
      throw exception;
    }
    return true;
  }

  @RequestMapping(value = "/da_approvare_{idListaLiquidazione}", method = RequestMethod.GET)
  public String daApprovare(HttpSession session, Model model,
      @PathVariable("idListaLiquidazione") long idListaLiquidazione)
      throws InternalUnexpectedException, ApplicationException
  {
    RigaJSONElencoListaLiquidazioneDTO lista = listeLiquidazioneEJB
        .getListaLiquidazioneById(idListaLiquidazione);
    validaAccessoLista(lista, getUtenteAbilitazioni(session));
    if (IuffiConstants.STATO.LISTE_LIQUIDAZIONE.FLAG.BOZZA
        .equals(lista.getFlagStatoLista()))
    {
      listeLiquidazioneEJB.aggiornaStatoLista(idListaLiquidazione,
          IuffiConstants.STATO.LISTE_LIQUIDAZIONE.FLAG.DA_APPROVARE,
          getIdUtenteLogin(session));
    }
    return "redirect:approva_" + idListaLiquidazione + ".do";
  }

  private void validaAccessoLista(RigaJSONElencoListaLiquidazioneDTO lista,
      UtenteAbilitazioni utenteAbilitazioni)
      throws ApplicationException, InternalUnexpectedException
  {
    if (!IuffiUtils.PAPUASERV.hasAmministrazioneCompetenza(
        utenteAbilitazioni, lista.getExtIdAmmCompetenza()))
    {
      throw new ApplicationException(
          "La lista è di un'amministrazione (organismo delegato) su cui non si ha competenza");
    }
    if (lista
        .getIdStatoStampa() == IuffiConstants.STATO.STAMPA.ID.GENERAZIONE_STAMPA_IN_CORSO)
    {
      throw new ApplicationException(
          "La stampa di questa lista di liquidazione è ancora in corso, impossibile procedere con l'approvazione");
    }
    else
    {
      if (lista
          .getIdStatoStampa() == IuffiConstants.STATO.STAMPA.ID.GENERAZIONE_STAMPA_IN_CORSO
          ||
          lista
              .getIdStatoStampa() == IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA
          ||
          lista
              .getIdStatoStampa() == IuffiConstants.STATO.STAMPA.ID.ANNULLATO)
      {
        throw new ApplicationException(
            "La stampa di questa lista di liquidazione è andata in errore, si prega di rigenerarla dall'elenco delle liste di liquidazione, impossibile procedere con l'approvazione");
      }
    }
    if (IuffiConstants.STATO.LISTE_LIQUIDAZIONE.FLAG.APPROVATA
        .equals(lista.getFlagStatoLista()))
    {
      throw new ApplicationException(
          "Questa lista di liquidazione è già approvata, impossibile procedere");
    }
    else
    {
      final long idListaLiquidazione = lista.getIdListaLiquidazione();
      if (listeLiquidazioneEJB.isListaLiquidazioneCorrotta(idListaLiquidazione))
      {
        throw new ApplicationException(
            "La lista di liquidazione indicata è corrotta (a causa di un precedente tentativo non riuscito di approvazione), impossibile procedere. Si prega di contattare l'assistenza tecnica comunicando il seguente messaggio: La lista di liquidazione con ID="
                + idListaLiquidazione + " è corrotta");

      }
    }
  }

  @RequestMapping(value = "/stampa_da_firmare_{idListaLiquidazione}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> scaricaAllegato(HttpServletRequest request,
      @PathVariable("idListaLiquidazione") long idListaLiquidazione)
      throws Exception
  {
    final String referer = request.getHeader("referer");
    if (referer == null || !referer
        .contains("cuiuffi228/approva_" + idListaLiquidazione + ".do"))
    {
      // Questa stampa può essere solo richiamata dall'approvazione, in tutti
      // gli altri casi si ha un 404
      return new ResponseEntity<byte[]>(
          null, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
    StampaListaLiquidazioneDTO fileStampa = listeLiquidazioneEJB
        .getStampaListaLiquidazione(idListaLiquidazione);
    HttpHeaders httpHeaders = new HttpHeaders();
    final String nomeFile = fileStampa.getNomeFile();
    httpHeaders.add("Content-type", IuffiUtils.FILE.getMimeType(nomeFile));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + nomeFile + "\"");
    Stampa stampa = IuffiUtils.STAMPA
        .getStampaFromCdU(IuffiConstants.USECASE.LISTE_LIQUIDAZIONE.STAMPA);
    assert stampa != null : "Impossibile trovare l'oggetto di stampa per la tipologia Lista di liquidazione";
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        stampa.genera(idListaLiquidazione,
            IuffiConstants.USECASE.LISTE_LIQUIDAZIONE.STAMPA),
        httpHeaders, HttpStatus.OK);
    return response;
  }

  @RequestMapping(value = "/visualizza_file", method = RequestMethod.POST)
  public ResponseEntity<byte[]> visualizzaFile(HttpServletRequest request,
      @RequestParam(value = "stampaFirmata", required = false) MultipartFile stampaFirmata)
      throws Exception
  {

    HttpHeaders httpHeaders = new HttpHeaders();
    final String nomeFile = stampaFirmata.getOriginalFilename();
    httpHeaders.add("Content-type", IuffiUtils.FILE.getMimeType(nomeFile));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + nomeFile + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        stampaFirmata.getBytes(), httpHeaders, HttpStatus.OK);
    return response;
  }

  @ExceptionHandler(
  { org.springframework.web.multipart.MaxUploadSizeExceededException.class })
  @ResponseBody
  public ResponseEntity<Exception> onException(HttpServletRequest request,
      Exception exception)
  {
    return new ResponseEntity<Exception>(exception,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ModelAndView resolveException(HttpServletRequest request,
      HttpServletResponse response, Object handler,
      Exception ex)
  {
    if (ex instanceof MaxUploadSizeExceededException)
    {
      String requestUri = request.getRequestURI();
      if (requestUri.indexOf("cuiuffi228") > -1)
      {
        MaxUploadSizeExceededException mex = (MaxUploadSizeExceededException) ex;
        final HttpSession session = request.getSession();
        Map<String, Object> common = getCommonFromSession("APPROVA_LISTA",
            session, true);
        String sMaxSize = null;
        BigDecimal bdMax = new BigDecimal(mex.getMaxUploadSize());
        final BigDecimal MEGABYTE = new BigDecimal(1024 * 1024);
        if (bdMax.remainder(MEGABYTE).compareTo(BigDecimal.ZERO) == 0)
        {
          sMaxSize = bdMax.divide(MEGABYTE, MathContext.DECIMAL128) + "MB";
        }
        else
        {
          final BigDecimal KILOBYTE = new BigDecimal(1024);
          if (bdMax.remainder(KILOBYTE).compareTo(BigDecimal.ZERO) == 0)
          {
            sMaxSize = bdMax.divide(KILOBYTE, MathContext.DECIMAL128) + "KB";
          }
          else
          {
            sMaxSize = bdMax.longValue() + " byte";
          }
        }
        common.put("error",
            "File di dimensione troppo grande. La dimensione massima accettata è di "
                + sMaxSize);
        saveCommonInSession(common, session);
        return new ModelAndView("redirect:" + request.getRequestURL(), null);
      }
    }
    return null;
  }
}