package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InfoRiduzione;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb.AbilitazioneAzioneTag;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public abstract class Elenco extends BaseController
{
  @Autowired
  protected IInterventiEJB interventiEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String elenco(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    clearCommonInSession(request.getSession());
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(
        request.getSession());
    boolean ribassoInterventi = isBandoConPercentualeRiduzione(
        idProcedimentoOggetto);
    model.addAttribute("ribassoInterventi", Boolean.valueOf(ribassoInterventi));
    if (ribassoInterventi)
    {
      model.addAttribute("inModifica", Boolean.FALSE);
      final InfoRiduzione infoRiduzione = interventiEJB.getInfoRiduzione(idProcedimentoOggetto);
      if (infoRiduzione != null)
      {
        model.addAttribute("percRiduzione", IuffiUtils.FORMAT
            .formatDecimal2(infoRiduzione.getPercentuale()));
        model.addAttribute("totaleInterventi", IuffiUtils.FORMAT
            .formatDecimal2(infoRiduzione.getTotaleImporto()));
        model.addAttribute("totaleRichiesto",
            infoRiduzione.getTotaleRichiestoEuro());
      }
    }
    addExtraAttributeToModel(model, request);
    isInterventoWithDanni(model);
    return getJSPBaseFolder() + "/elenco";
  }

  protected String getJSPBaseFolder()
  {
    return "interventi";
  }

  protected abstract boolean isBandoConPercentualeRiduzione(
      final long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  @RequestMapping(value = "/json/elenco", produces = "application/json")
  @ResponseBody
  public List<RigaElencoInterventi> elenco_json(Model model,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    final String codiceQuadro = getCodiceQuadro();
    List<RigaElencoInterventi> interventi = getElencoInterventiForJSON(
        getIdProcedimentoOggetto(request.getSession()), request);
    boolean isElimina = AbilitazioneAzioneTag.validate(codiceQuadro,
        IuffiConstants.AZIONE.CODICE.ELIMINA,
        getProcedimentoOggettoFromSession(session),
        getUtenteAbilitazioni(session), request);
    boolean isModificaSal = AbilitazioneAzioneTag.validate(codiceQuadro,
        IuffiConstants.AZIONE.CODICE.MODIFICA_SAL,
        getProcedimentoOggettoFromSession(session),
        getUtenteAbilitazioni(session), request);
    boolean isModifica = AbilitazioneAzioneTag.validate(codiceQuadro,
        IuffiConstants.AZIONE.CODICE.MODIFICA,
        getProcedimentoOggettoFromSession(session),
        getUtenteAbilitazioni(session), request);
    boolean isLocalizza = AbilitazioneAzioneTag.validate(codiceQuadro,
        IuffiConstants.AZIONE.CODICE.LOCALIZZA,
        getProcedimentoOggettoFromSession(session),
        getUtenteAbilitazioni(session), request);
    StringBuilder sb = new StringBuilder();
    for (RigaElencoInterventi intervento : interventi)
    {
      sb.setLength(0);
      sb.append("DE"); // Dettaglio
      if (!IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_ELIMINAZIONE
          .equals(intervento.getFlagTipoOperazione()))
      {
        // Per semplificare al js il parsing uso SEMPRE 2 caratteri per indicare
        // l'icona a causa della localizzazione in
        // cui devo specificare anche il tipo di
        // localizzazione
        if (isModifica && !IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE.equalsIgnoreCase(intervento.getCodiceIdentificativo()))
        {
          sb.append("MO");
        }
        if (isElimina  && !IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE.equalsIgnoreCase(intervento.getCodiceIdentificativo()))
        {
          sb.append("EL");
        }
        if (isLocalizza)
        {
          sb.append("L" + intervento.getIdTipoLocalizzazione());
        }
        if (isModificaSal && intervento.isInterventoModificabileASaldo()  && !IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE.equalsIgnoreCase(intervento.getCodiceIdentificativo()))
        {
          sb.append("MS");
        }
      }
      intervento.setIcone(sb.toString());
    }
    return interventi;
  }

  protected List<RigaElencoInterventi> getElencoInterventiForJSON(
      long idProcedimentoOggetto, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return interventiEJB.getElencoInterventiProcedimentoOggetto(
        idProcedimentoOggetto, getFlagEscludiCatalogo(),
        isMisuraConPartecipanti(),
        getDataValiditaProcOggetto(request.getSession()));
  }

  protected boolean isMisuraConPartecipanti()
  {
    /*
     * Valore di default a false, non ci sono partecipanti. Sarà true solo per
     * la misura 16. CU-IUFFI-266
     */
    return false;
  }

  public abstract String getFlagEscludiCatalogo();

  public abstract String getCodiceQuadro();

  protected abstract void addExtraAttributeToModel(Model model,
      HttpServletRequest request)
      throws InternalUnexpectedException;
  
  protected abstract void isInterventoWithDanni(Model model);
}