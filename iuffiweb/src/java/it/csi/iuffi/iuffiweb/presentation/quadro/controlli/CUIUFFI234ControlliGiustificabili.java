package it.csi.iuffi.iuffiweb.presentation.quadro.controlli;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli.ControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli.GiustificazioneAnomaliaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi234")
@IuffiSecurity(value = "CU-IUFFI-234-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI234ControlliGiustificabili extends BaseController
{
  @Autowired
  private IQuadroEJB quadroEJB = null;

  public GiustificazioneAnomaliaDTO prepareData(Long idSoluzioneAnomalia,
      Long idControllo, HttpSession session, Model model)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    GiustificazioneAnomaliaDTO giustificazione = quadroEJB
        .getGiustificazioneAnomalia(idControllo,
            procedimentoOggetto.getIdProcedimentoOggetto());
    ControlloDTO controllo = quadroEJB.getControllo(idControllo,
        procedimentoOggetto.getIdProcedimentoOggetto());
    List<DecodificaDTO<Long>> tipiRisoluzioneControlli = quadroEJB
        .getTipiRisoluzioneControlli(idControllo);
    if (giustificazione == null)
      model.addAttribute("inserisci", true);
    else
    {
      model.addAttribute("modifica", true);
      model.addAttribute("giustificazione", giustificazione);
    }

    TestataProcedimento testataProcedimento = (TestataProcedimento) session
        .getAttribute(TestataProcedimento.SESSION_NAME);
    model.addAttribute("amm_competenza",
        testataProcedimento.getDescAmmCompetenza());
    controllo.setIdSoluzioneAnomalia(idSoluzioneAnomalia);
    model.addAttribute("controllo", controllo);
    model.addAttribute("tipiRisoluzioneControlli", tipiRisoluzioneControlli);
    model.addAttribute("idControllo", idControllo);
    return giustificazione;
  }

  @RequestMapping(value = "/isTipoGiustificazioneAntimafia_{idTipoSoluzione}", method = RequestMethod.GET)
  @ResponseBody
  public String isTipoGiustificazioneAntimafia(
      @PathVariable("idTipoSoluzione") long idTipoSoluzione)
      throws InternalUnexpectedException
  {
    return (quadroEJB.isTipoGiustificazioneAntimafia(idTipoSoluzione) ? "OK"
        : "KO");
  }

  @RequestMapping(value = "/modificaGiustificazione_{idControllo}_{idSoluzioneAnomalia}", method = RequestMethod.GET)
  public String modificaGiustificazioneAnomalia(
      @PathVariable("idSoluzioneAnomalia") Long idSoluzioneAnomalia,
      @PathVariable("idControllo") long idControllo, Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    /*
     * Mi devo comportare in maniera diversa s sto giustificando un controllo
     * antimafia o no
     */
    GiustificazioneAnomaliaDTO giustificazione = prepareData(
        idSoluzioneAnomalia, idControllo, session, model);
    if (giustificazione != null
        && giustificazione.getIdTipoRisoluzione() != null
        && giustificazione.getIdTipoRisoluzione() != 0)
    {
      String flagFileObbligatorio = quadroEJB.getFlagObbligatorieta(
          giustificazione.getIdTipoRisoluzione(), "file");
      String flagNoteObbligatorie = quadroEJB.getFlagObbligatorieta(
          giustificazione.getIdTipoRisoluzione(), "note");
      model.addAttribute("flagFileObbligatorio", flagFileObbligatorio);
      model.addAttribute("flagNoteObbligatorie", flagNoteObbligatorie);
    }

    return "controlli/modificaGiustificazioneAnomalia";
  }

  @IuffiSecurity(value = "CU-IUFFI-114", controllo = IuffiSecurity.Controllo.DEFAULT)
  @RequestMapping(value = "/showGiustificazione_{idControllo}", method = RequestMethod.GET)
  public String visualizzaGiustificazione(
      @PathVariable("idControllo") long idControllo, Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    GiustificazioneAnomaliaDTO giustificazione = quadroEJB
        .getGiustificazioneAnomalia(idControllo,
            procedimentoOggetto.getIdProcedimentoOggetto());
    ControlloDTO controllo = quadroEJB.getControllo(idControllo,
        procedimentoOggetto.getIdProcedimentoOggetto());

    if (giustificazione != null
        && giustificazione.getIdTipoRisoluzione() != null
        && quadroEJB.isTipoGiustificazioneAntimafia(
            giustificazione.getIdTipoRisoluzione()))
    {
      model.addAttribute("showparametriamf", Boolean.TRUE);
    }

    model.addAttribute("giustificazione", giustificazione);
    model.addAttribute("controllo", controllo);

    long idUtenteLogin = giustificazione.getExtIdUtenteAggiornamento();
    List<Long> l = new LinkedList<Long>();
    l.add(idUtenteLogin);
    List<UtenteLogin> utenti = super.loadRuoloDescr(l);
    model.addAttribute("ultimaModifica",
        getUtenteDescrizione(idUtenteLogin, utenti));

    return "controlli/visualizzaGiustificazioneAnomalia";
  }

  @RequestMapping(value = "/confermaModifica_{idControllo}_{idSoluzioneAnomalia}", method = RequestMethod.POST)
  @IsPopup
  public String confermaModifica(@PathVariable("idControllo") long idControllo,
      @PathVariable("idSoluzioneAnomalia") Long idSoluzioneAnomalia,
      @RequestParam(value = "nomeAllegato", required = false) String nomeAllegato,
      @RequestParam(value = "prov", required = false) String prov,
      @RequestParam(value = "protocollo", required = false) String numProtocollo,
      @RequestParam(value = "data_protocollo", required = false) String dataProtocollo,
      @RequestParam(value = "data_documento", required = false) String dataDocumento,
      @RequestParam(value = "fileDaAllegare", required = false) MultipartFile fileAllegato,
      Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException, IOException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    Errors errors = new Errors();
    Long idTipoRisoluzione = errors.validateMandatoryLong(
        request.getParameter("idTipoRisoluzione"), "idTipoRisoluzione");
    String note = request.getParameter("note");
    if (idTipoRisoluzione != null
        && quadroEJB.isTipoGiustificazioneAntimafia(idTipoRisoluzione))
    {
      // VALIDAZIONE SUBFORM ANTIMAFIA
      errors = validaCertificato(prov, dataDocumento, numProtocollo,
          dataProtocollo, note);
    }
    // else
    // {
    // VALIDAZIONE STANDARD
    String flagFileObbligatorio = quadroEJB
        .getFlagObbligatorieta(idTipoRisoluzione, "file");
    String flagNoteObbligatorie = quadroEJB
        .getFlagObbligatorieta(idTipoRisoluzione, "note");

    GiustificazioneAnomaliaDTO giustificazione = quadroEJB
        .getGiustificazioneAnomalia(idControllo,
            procedimentoOggetto.getIdProcedimentoOggetto());
    boolean isModifica = false;
    boolean hasFile = false;
    if (giustificazione != null)
    {
      isModifica = true;
      if (giustificazione.getAllegato() != null)
      {
        FileAllegatoDTO file = quadroEJB
            .getAllegatoGiustificazione(idSoluzioneAnomalia);
        if (file != null && file.getFileAllegato() != null
            && file.getFileAllegato().length > 0)
          hasFile = true;
      }
    }

    String nomeFile = "";
    if (idTipoRisoluzione != null)
    {
      if (flagNoteObbligatorie.equals("S"))
        errors.validateMandatory(request.getParameter("note"), "note");
      errors.validateFieldLength(request.getParameter("note"), "note", 0, 4000);

      if (flagFileObbligatorio.equals("S"))
      {
        errors.validateMandatory(nomeAllegato, "nomeAllegato");
        // se non è modifica, ma inserimento, faccio il controllo che ci sia il
        // file
        if (!isModifica)
        {
          if (fileAllegato == null || fileAllegato.isEmpty())
          {
            errors.addError("fileDaAllegare", Errors.ERRORE_CAMPO_OBBLIGATORIO);
          }
        }
        else
        {
          // se è in modifica, non in inserimento, se il file esisteva già, ok,
          // tengo quello e va bene cosi
          if (!hasFile && (fileAllegato == null || fileAllegato.isEmpty()))
          {
            errors.addError("fileDaAllegare", Errors.ERRORE_CAMPO_OBBLIGATORIO);
          }
        }
      }

      if (fileAllegato != null && !fileAllegato.isEmpty())
      {
        nomeFile = IuffiUtils.FILE
            .getSafeSubmittedFileName(fileAllegato.getOriginalFilename());
        if (nomeFile.length() > 100)
          errors.addError("fileDaAllegare", Errors.ERRORE_LUNGHEZZA_NOME_FILE);

        String extension = null;
        int pos = nomeFile.lastIndexOf('.');
        if (pos >= 0)
        {
          extension = nomeFile.substring(pos + 1);
        }

        boolean ok = quadroEJB.checkExtension(extension);
        if (!ok)
          errors.addError("fileDaAllegare", Errors.ERRORE_ESTENSIONE_FILE);
      }

      errors.validateFieldLength(nomeAllegato, "nomeAllegato", 0, 255);

      if (!isModifica)
      {
        // se sono in inserimento, se c'è il nome deve esserci anche il file e
        // viceversa
        if (nomeAllegato != null && nomeAllegato.compareTo("") != 0)
          if (fileAllegato == null || fileAllegato.getSize() == 0)
            errors.addError("fileDaAllegare",
                "E' stato inserito il nome del file. Inserire anche il file o rimuovere il nome.");

        if (fileAllegato != null && fileAllegato.getSize() != 0)
          if (nomeAllegato == null || nomeAllegato.compareTo("") == 0)
            errors.addError("nomeAllegato",
                "E' stato inserito il file. Inserire anche il nome.");
      }
      else
      {
        // se sono in modifica, se c'è il nome, o c'è il file o questo esisteva
        // già
        if (nomeAllegato != null && nomeAllegato.compareTo("") != 0)
          if (!hasFile && (fileAllegato == null || fileAllegato.getSize() == 0))
            errors.addError("fileDaAllegare",
                "E' stato inserito il nome del file. Inserire anche il file o rimuovere il nome.");

        // se non c'è il file ma non il nome, errore
        if (fileAllegato != null && fileAllegato.getSize() != 0
            && (nomeAllegato == null || nomeAllegato.compareTo("") == 0))
          errors.addError("nomeAllegato",
              "E' stato inserito il file. Inserire anche il nome.");

        // se non c'è il nome ma c'è il file vero o preesistente
        String nomeFisicoHidden = request.getParameter("nomeFisicoHidden");
        if ((nomeAllegato == null || nomeAllegato.compareTo("") == 0)
            && nomeFisicoHidden != null && nomeFisicoHidden.compareTo("") != 0)
          errors.addError("nomeAllegato",
              "E' stato inserito il file. Inserire anche il nome.");

      }
      // }
      // VALIDAZIONE FINE

      if (errors.isEmpty())
      {
        if (!isModifica)
          quadroEJB.inserisciGiustificazioneAnomaliaControllo(
              idSoluzioneAnomalia, idControllo,
              procedimentoOggetto.getIdProcedimentoOggetto(), idTipoRisoluzione,
              note, fileAllegato, nomeFile, nomeAllegato,
              utenteAbilitazioni.getIdUtenteLogin(), prov,
              IuffiUtils.DATE.parseDate(dataProtocollo),
              IuffiUtils.DATE.parseDate(dataDocumento), numProtocollo);
        else
        {
          boolean maintainOldFile = false;
          if (nomeAllegato != null
              && (fileAllegato != null && fileAllegato.getSize() <= 0))
          {
            maintainOldFile = true;
            nomeFile = giustificazione.getAllegato().getNomeFisico();
          }
          if (nomeAllegato == null || nomeAllegato.compareTo("") == 0)
          {
            nomeFile = null;
            nomeAllegato = null;
            fileAllegato = null;
            maintainOldFile = false;
          }
          quadroEJB.updateGiustificazioneAnomaliaControllo(idSoluzioneAnomalia,
              idControllo, procedimentoOggetto.getIdProcedimentoOggetto(),
              idTipoRisoluzione, note, fileAllegato, nomeFile, nomeAllegato,
              utenteAbilitazioni.getIdUtenteLogin(), prov,
              IuffiUtils.DATE.parseDate(dataProtocollo),
              IuffiUtils.DATE.parseDate(dataDocumento), numProtocollo,
              maintainOldFile);

        }

        return "dialog/success";
      }
    }

    model.addAttribute("preferRequestValue", Boolean.TRUE);
    model.addAttribute("errors", errors);
    prepareData(idSoluzioneAnomalia, idControllo, session, model);
    return "controlli/modificaGiustificazioneAnomalia";
  }

  private Errors validaCertificato(String siglaProvincia, String dataDocumento,
      String numProtocollo, String dataProtocollo, String note)
  {
    Errors errors = new Errors();
    errors.validateMandatoryFieldMaxLength(siglaProvincia, "prov", 2);
    errors.validateMandatoryDateInRange(dataDocumento, "data_documento", null,
        new Date(), false, true);

    if (!GenericValidator.isBlankOrNull(numProtocollo))
    {
      errors.validateMandatoryLongInRange(numProtocollo, "protocollo", 1L,
          null);
      errors.validateMandatoryDate(dataProtocollo, "data_protocollo", true);
    }

    if (!GenericValidator.isBlankOrNull(dataProtocollo))
    {
      errors.validateMandatoryDateInRange(dataProtocollo, "data_protocollo",
          null, new Date(), false, true);
    }

    if (!GenericValidator.isBlankOrNull(note))
    {
      errors.validateFieldMaxLength(note, "noteAntimafia", 500);
    }

    return errors;
  }

  @RequestMapping(value = "/conferma_elimina_{idSoluzioneAnomalia}", method = RequestMethod.GET)
  public String confermaElimina(Model model, HttpSession session,
      @ModelAttribute("idSoluzioneAnomalia") @PathVariable("idSoluzioneAnomalia") long idSoluzioneAnomalia)
      throws InternalUnexpectedException
  {
    model.addAttribute("idSoluzioneAnomalia", idSoluzioneAnomalia);
    return "controlli/confermaEliminaGiustificazione";
  }

  @RequestMapping(value = "/elimina_giustificazione_{idSoluzioneAnomalia}", method = RequestMethod.POST)
  public String eliminaGiustificazione(
      @PathVariable("idSoluzioneAnomalia") long idSoluzioneAnomalia,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    quadroEJB.eliminaGiustificazione(idSoluzioneAnomalia);
    return "dialog/success";
  }

  @IuffiSecurity(value = "CU-IUFFI-114", controllo = IuffiSecurity.Controllo.DEFAULT)
  @RequestMapping(value = "/downloadAllegato_{idSoluzioneAnomalia}", method = RequestMethod.GET)
  public void downloadAllegato(Model model, HttpSession session,
      HttpServletResponse response,
      @ModelAttribute("idSoluzioneAnomalia") @PathVariable("idSoluzioneAnomalia") long idSoluzioneAnomalia)
      throws InternalUnexpectedException, IOException
  {

    FileAllegatoDTO file = quadroEJB
        .getAllegatoGiustificazione(idSoluzioneAnomalia);
    MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
    response.setContentType(mimeTypesMap.getContentType(file.getNomeFile()));
    response.setHeader("Content-disposition",
        "attachment; filename=\"" + file.getNomeFile() + "\"");
    (response.getOutputStream()).write(file.getFileAllegato());
    response.flushBuffer();

  }

  @RequestMapping(value = "/getFlagObbligatorieta_{idTipoRisoluzione}", method = RequestMethod.GET)
  @ResponseBody
  public String getFlagObbligatorieta(
      @PathVariable("idTipoRisoluzione") long idTipoRisoluzione, Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    String flagFileObbligatorio = quadroEJB
        .getFlagObbligatorieta(idTipoRisoluzione, "file");
    String flagNoteObbligatorie = quadroEJB
        .getFlagObbligatorieta(idTipoRisoluzione, "note");

    if (flagFileObbligatorio.compareTo("S") == 0
        && flagNoteObbligatorie.compareTo("S") == 0)
      return "entrambi";
    if (flagFileObbligatorio.compareTo("S") != 0
        && flagNoteObbligatorie.compareTo("S") == 0)
      return "soloNote";
    if (flagFileObbligatorio.compareTo("S") == 0
        && flagNoteObbligatorie.compareTo("S") != 0)
      return "soloFile";
    if (flagFileObbligatorio.compareTo("S") != 0
        && flagNoteObbligatorie.compareTo("S") != 0)
      return "nessuno";

    return "error";
  }
}