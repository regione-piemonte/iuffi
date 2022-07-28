package it.csi.iuffi.iuffiweb.presentation.quadro.allegati;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.ContenutoFileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.FileAllegatiDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity.Controllo;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi121")
@IuffiSecurity(value = "CU-IUFFI-121", errorPage = "/WEB-INF/jsp/dialog/soloErrore.jsp", controllo = Controllo.PROCEDIMENTO_OGGETTO)
@IsPopup
public class CUIUFFI121GestisciAllegatiFisici extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-108";
  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping(value = "/inserisci_{idDettaglioInfo}", method = RequestMethod.GET)
  public String inserisciGet(Model model, HttpSession session,
      @ModelAttribute("idDettaglioInfo") @PathVariable("idDettaglioInfo") long idDettaglioInfo)
      throws InternalUnexpectedException
  {
    return "allegati/allegaFile";
  }

  @RequestMapping(value = "/inserisci_{idDettaglioInfo}", method = RequestMethod.POST)
  public String inserisciPost(Model model, HttpSession session,
      @ModelAttribute("idDettaglioInfo") @PathVariable("idDettaglioInfo") long idDettaglioInfo,
      @ModelAttribute("errors") Errors errors,
      @RequestParam(value = "nomeAllegato", required = false) String nomeAllegato,
      @RequestParam(value = "fileDaAllegare", required = false) MultipartFile fileAllegato)
      throws InternalUnexpectedException, IOException
  {
    errors.validateMandatory(nomeAllegato, "nomeAllegato");
    errors.validateFieldLength(nomeAllegato, "nomeAllegato", 0, 255);
    if (fileAllegato == null || fileAllegato.isEmpty())
    {
      errors.addError("fileDaAllegare", Errors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      String nomeFile = IuffiUtils.FILE
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

    if (errors.isEmpty())
    {
      ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
      FileAllegatiDTO fileAllegatiDTO = new FileAllegatiDTO();
      fileAllegatiDTO.setIdDettaglioInfo(idDettaglioInfo);
      fileAllegatiDTO.setNomeLogico(nomeAllegato);
      fileAllegatiDTO.setNomeFisico(IuffiUtils.FILE
          .getSafeSubmittedFileName(fileAllegato.getOriginalFilename()));

      quadroEJB.insertFileAllegati(po.getIdProcedimentoOggetto(),
          fileAllegatiDTO, fileAllegato.getBytes());
      return "allegati/allegaFileOk";
    }
    model.addAttribute("nomeAllegato", nomeAllegato);

    return "allegati/allegaFile";
  }

  @RequestMapping(value = "/conferma_elimina_{idFileAllegati}", method = RequestMethod.GET)
  public String confermaElimina(Model model, HttpSession session,
      @ModelAttribute("idFileAllegati") @PathVariable("idFileAllegati") long idFileAllegati)
      throws InternalUnexpectedException
  {
    return "allegati/confermaElimina";
  }

  @RequestMapping(value = "/elimina_{idFileAllegati}", method = RequestMethod.GET)
  public String eliminaAllegato(Model model, HttpSession session,
      @ModelAttribute("idFileAllegati") @PathVariable("idFileAllegati") long idFileAllegati)
      throws InternalUnexpectedException
  {
    try
    {
      quadroEJB.deleteFileAllegati(getIdProcedimentoOggetto(session),
          idFileAllegati);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "allegati/confermaElimina";
  }

  @IuffiSecurity(value = "CU-IUFFI-121", errorPage = "/WEB-INF/jsp/dialog/soloErrore.jsp", controllo = Controllo.NESSUNO)
  @RequestMapping(value = "/scarica_allegato_{idFileAllegati}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> scaricaAllegato(HttpSession session,
      @PathVariable("idFileAllegati") long idFileAllegati)
      throws InternalUnexpectedException
  {
    ContenutoFileAllegatiDTO contenuto = quadroEJB.getFileFisicoAllegato(
        getIdProcedimentoOggetto(session), idFileAllegati);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        IuffiUtils.FILE.getMimeType(contenuto.getNomeFisico()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + contenuto.getNomeFisico() + "\"");

    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        contenuto.getContenuto(), httpHeaders, HttpStatus.OK);
    return response;
  }
}
