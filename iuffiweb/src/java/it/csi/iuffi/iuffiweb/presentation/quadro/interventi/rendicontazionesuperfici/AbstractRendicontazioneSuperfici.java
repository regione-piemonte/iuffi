package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.rendicontazionesuperfici;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IRendicontazioneEAccertamentoSpeseEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONRendicontazioneSuperficiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public abstract class AbstractRendicontazioneSuperfici extends BaseController
{
  @Autowired
  protected IRendicontazioneEAccertamentoSpeseEJB rendicontazioneEAccertamentoSpeseEJB;

  @RequestMapping(value = "/modifica_{idIntervento}", method = RequestMethod.GET)
  protected String modifica(Model model,
      @PathVariable("idIntervento") long idIntervento,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    checkTipoOggetto(request);
    final List<RigaJSONRendicontazioneSuperficiDTO> elenco = rendicontazioneEAccertamentoSpeseEJB
        .getRendicontazioniSuperficiJSON(
            getIdProcedimentoOggetto(request.getSession()), idIntervento);
    verificaAmmissibilitaDatiSuDB(elenco);
    model.addAttribute("elenco", elenco);
    onFirstPageLoad(elenco);
    calcolaTotali(model, elenco);
    return getBasePath() + "modifica";
  }

  protected void onFirstPageLoad(
      List<RigaJSONRendicontazioneSuperficiDTO> elenco)
  {

  }

  protected void verificaAmmissibilitaDatiSuDB(
      List<RigaJSONRendicontazioneSuperficiDTO> elenco)
      throws InternalUnexpectedException, ApplicationException
  {
    // Nessuna verifica nell'implementazione base (Verrà sicuramente
    // specializzata da CU-IUFFI-255I per verificare che
    // la superficie GIS sia valorizzata)
  }

  @RequestMapping(value = "/modifica_{idIntervento}", method = RequestMethod.POST)
  protected String modificaPost(Model model,
      @PathVariable("idIntervento") long idIntervento,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    checkTipoOggetto(request);
    final List<RigaJSONRendicontazioneSuperficiDTO> elencoParticelle = rendicontazioneEAccertamentoSpeseEJB
        .getRendicontazioniSuperficiJSON(
            getIdProcedimentoOggetto(request.getSession()), idIntervento);
    if (!validateAndUpdate(model, idIntervento, elencoParticelle, request))
    {
      model.addAttribute("elenco", elencoParticelle);
    }
    else
    {
      return "redirect:.." + getParentUrl();
    }
    model.addAttribute("preferRequest", Boolean.TRUE);
    return getBasePath() + "modifica";
  }

  public abstract String getParentUrl();

  public abstract boolean validateAndUpdate(Model model, long idIntervento,
      List<RigaJSONRendicontazioneSuperficiDTO> elencoParticelle,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException;

  abstract protected String getBasePath();

  protected void checkTipoOggetto(HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromRequest(request);
    final boolean requireIstanza = requireIstanza();
    if (IuffiConstants.FLAGS.SI
        .equals(po.getFlagIstanza()) != requireIstanza)
    {
      throw new ApplicationException(
          "Attenzione, si è verificato un errore grave. Si prega di contattare l'assistenza tecnica comunicando il seguente messaggio: errore di configurazione, questo quadro "
              + (requireIstanza ? "" : " NON ")
              + " RICHIEDE un oggetto di tipo ISTANZA");
    }
  }

  protected abstract void calcolaTotali(Model model,
      List<RigaJSONRendicontazioneSuperficiDTO> elenco);

  abstract protected boolean requireIstanza();
}