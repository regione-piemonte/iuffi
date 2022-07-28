package it.csi.iuffi.iuffiweb.presentation.stampeoggetto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.ProcedimOggettoStampaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoIconaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellDocumentoVO;

@Controller
@IuffiSecurity(value = "CU-IUFFI-126-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO)
@RequestMapping("/cuiuffi126i")
@IsPopup
public class CUIUFFI126IInserisciStampaOggetto extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/inserisci", method = RequestMethod.GET)
  public String inserisciGet(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    loadTipologie(model, session);
    return "stampeoggetto/allegaDocumento";
  }

  public void loadTipologie(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    List<StampaOggettoIconaDTO> tipologie = quadroEJB 
        .getElencoDocumentiStampeDaAllegare(getIdProcedimentoOggetto(session),
            null,IuffiConstants.USECASE.STAMPE_OGGETTO.INSERISCI_STAMPA_OGGETTO);
    model.addAttribute("tipologie", tipologie);
  }

  @RequestMapping(value = "/inserisci", method = RequestMethod.POST)
  public String inserisciPost(Model model, HttpSession session,
      @ModelAttribute("errors") Errors errors,
      @RequestParam(value = "idTipoDocumento", required = false) String idTipoDocumento,
      @RequestParam(value = "fileDaAllegare", required = false) MultipartFile fileAllegato)
      throws InternalUnexpectedException, IOException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    final long idProcedimentoOggetto = procedimentoOggetto
        .getIdProcedimentoOggetto();
    Long lIdTipoDocumento = errors.validateMandatoryID(idTipoDocumento,
        "idTipoDocumento");
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    StampaOggettoIconaDTO oggettoIconaDTO = null;
    if (lIdTipoDocumento != null)
    {
      List<StampaOggettoIconaDTO> tipoSelezionato = quadroEJB
          .getElencoDocumentiStampeDaAllegare(idProcedimentoOggetto,
              lIdTipoDocumento,IuffiConstants.USECASE.STAMPE_OGGETTO.INSERISCI_STAMPA_OGGETTO);
      if (tipoSelezionato != null && !tipoSelezionato.isEmpty())
      {
        oggettoIconaDTO = tipoSelezionato.get(0);

        if (utenteAbilitazioni.getRuolo().isUtenteIntermediario()
            && IuffiConstants.FLAGS.SI
                .equals(oggettoIconaDTO.getFlagFirmaGrafometrica()))
        {
          errors.addError("idTipoDocumento",
              "Errore di configurazione: Il tipo di documento selezionato è impostato per la firma grafometrica che non è al momento disponibile."
                  + " Si prega di contattare l'assistenza tecnica e segnalare l'errore");
        }
      }
      else
      {
        errors.addError("idTipoDocumento",
            "Il tipo di documento selezionato al momento non è disponibile");
      }
    }
    if (fileAllegato == null || fileAllegato.isEmpty())
    {
      errors.addError("nomeAllegato", Errors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    if (fileAllegato == null || fileAllegato.isEmpty())
    {
      errors.addError("fileDaAllegare", Errors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    boolean hasErrors = !errors.isEmpty();
    if (!hasErrors)
    {
      AgriWellDocumentoVO agriWellDocumentoVO = getAgriWellDocumentoVO(
          quadroEJB, session, oggettoIconaDTO, fileAllegato.getBytes(),
          fileAllegato.getOriginalFilename(), utenteAbilitazioni,
          idProcedimentoOggetto);
      // AgriWellDocumentoVO agriWellDocumentoVO =
      // getAgriWellDocumentoVO(session, lIdTipoDocumento, fileAllegato,
      // oggettoIconaDTO, utenteAbilitazioni);
      ProcedimOggettoStampaDTO stampa = new ProcedimOggettoStampaDTO();
      stampa.setExtIdUtenteAggiornamento(getIdUtenteLogin(session));
      stampa.setIdBandoOggetto(procedimentoOggetto.getIdBandoOggetto());
      stampa.setIdOggettoIcona(oggettoIconaDTO.getIdOggettoIcona());
      stampa.setIdProcedimento(getIdProcedimento(session));
      stampa.setIdProcedimentoOggetto(idProcedimentoOggetto);
      stampa.setIdStatoStampa((IuffiConstants.FLAGS.SI
          .equals(oggettoIconaDTO.getFlagDaFirmare()))
              ? IuffiConstants.STATO.STAMPA.ID.FIRMATO_DIGITALMENTE
              : IuffiConstants.STATO.STAMPA.ID.STAMPA_ALLEGATA);
      String error = quadroEJB.aggiungiOggettoStampa(stampa,
          agriWellDocumentoVO, getUtenteAbilitazioni(session),null);
      if (error != null)
      {
        errors.addError("error", error);
        hasErrors = true;
      }
    }
    if (!hasErrors)
    {
      return "redirect:../cuiuffi126l/lista.do";
    }
    else
    {
      model.addAttribute("idTipoDocumento", idTipoDocumento);
      loadTipologie(model, session);
      return "stampeoggetto/allegaDocumento";
    }
  }

  private AgriWellDocumentoVO getAgriWellDocumentoVO(IQuadroEJB quadroEJB,
      HttpSession session, StampaOggettoIconaDTO oggettoIconaDTO, byte[] pdf,
      String fileName,
      UtenteAbilitazioni utenteAbilitazioni, long extIdAzienda)
      throws InternalUnexpectedException, IOException
  {
    Map<String, String> mapParametri = quadroEJB.getParametri(new String[]
    { IuffiConstants.PARAMETRO.DOQUIAGRI_CARTELLA,
        IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG,
        IuffiConstants.PARAMETRO.DOQUIAGRI_FASCICOLA });
    
    int idProcedimentoAgricolo = getUtenteAbilitazioni(session).getIdProcedimento();

    
    String codClassReg = quadroEJB.getCodClassRegionale(idProcedimentoAgricolo,getProcedimentoFromSession(session).getIdBando());
    mapParametri.put(IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG, codClassReg);
    
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    TestataProcedimento testataProcedimento = (TestataProcedimento) session
        .getAttribute(TestataProcedimento.SESSION_NAME);
    DatiIdentificativi datiIdentificativi = quadroEJB
        .getDatiIdentificativiProcedimentoOggetto(po.getIdProcedimentoOggetto(),
            0, null, idProcedimentoAgricolo);
    String identificativo = quadroEJB
        .getIdentificativo(po.getIdProcedimentoOggetto());
    return IuffiUtils.AGRIWELL.getAgriWellDocumentoVO(
        oggettoIconaDTO.getExtIdTipoDocumento(), pdf, fileName, mapParametri,
        utenteAbilitazioni,
        IuffiConstants.FLAGS.FIRMA_GRAFOMETRICA.DA_NON_FIRMARE,
        testataProcedimento, datiIdentificativi,
        identificativo);
  }
}
