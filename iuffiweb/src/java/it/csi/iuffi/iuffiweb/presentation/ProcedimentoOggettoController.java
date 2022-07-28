package it.csi.iuffi.iuffiweb.presentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.MapColonneNascosteVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.AzioneDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.OggettoIconaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.interceptor.security.IuffiSecurityManager;
import it.csi.iuffi.iuffiweb.util.IuffiApplication;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping(value = "/procedimento")
@IuffiSecurity(value = "N/A", controllo = IuffiSecurity.Controllo.NESSUNO)
public class ProcedimentoOggettoController extends BaseController
{
  private static final String ERRORE_PROCEDIMENTO_OGGETTO = "Il procedimento oggetto scelto non è valido";
  private static final String ERRORE_PROCEDIMENTO_AGRICOLO = "Il procedimento selezionato si può gestire esclusivamente con l'applicativo "; 

  @Autowired
  IQuadroEJB                  quadroEJB                   = null;
  @Autowired
  IuffiSecurityManager     securityManager;

  @RequestMapping(value = "/switch_{idProcedimento}_{idProcedimentoOggetto}")
  public String selezionaProcedimentoOggetto(HttpSession session,
      HttpServletRequest request,
      Model model,
      @PathVariable("idProcedimento") @NumberFormat(style = NumberFormat.Style.NUMBER) long idProcedimento,
      @PathVariable("idProcedimentoOggetto") @NumberFormat(style = NumberFormat.Style.NUMBER) long idProcedimentoOggetto)
      throws InternalUnexpectedException,
      ApplicationException
  {

    // Ripulisco eventuali filtri presenti in sessione
    cleanFilters(session);
    cleanSession(session);

    ProcedimentoOggetto procedimentoOggetto = null;

    if (idProcedimentoOggetto > 0)
    {
      procedimentoOggetto = quadroEJB
          .getProcedimentoOggetto(idProcedimentoOggetto);
    }
    else
      if (idProcedimento > 0)
      {
        procedimentoOggetto = quadroEJB
            .getProcedimentoOggettoByIdProcedimento(idProcedimento);
      }
      else
      {
        throw new ApplicationException(ERRORE_PROCEDIMENTO_OGGETTO);
      }

    Procedimento procedimento = getProcedimentoFromSession(session);

    if (procedimentoOggetto != null && procedimentoOggetto
        .getIdProcedimento() == procedimento.getIdProcedimento())
    {
      IuffiFactory.setIdProcedimentoOggettoInSession(session,
          procedimentoOggetto.getIdProcedimentoOggetto(),
          procedimento.getIdProcedimento());
      String retURL = null;
      boolean removed = false;
      
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) request
    	        .getSession()
    	        .getAttribute("utenteAbilitazioni");
  	  if(utenteAbilitazioni.getIdProcedimento() != procedimento.getIdProcedimentoAgricolo())
      {
  		  throw new ApplicationException(ERRORE_PROCEDIMENTO_AGRICOLO + IuffiApplication.getNomeApplicativoByIdProcedimentoAgricolo(procedimento.getIdProcedimentoAgricolo()));
      }
      
      
      final List<QuadroOggettoDTO> quadri = procedimentoOggetto.getQuadri();
      for (int i = quadri.size() - 1; i >= 0; i--)
      {
        try
        {
          retURL = getRedirectQuadroURL(procedimentoOggetto, quadri.get(i),
              request, session);
        }
        catch (Exception e)
        {
          removed = true;
          quadri.remove(i);
        }
      }

      if (quadri.size() > 0)
      {
        IuffiFactory.setIdProcedimentoOggettoInSession(session,
            procedimentoOggetto.getIdProcedimentoOggetto(),
            procedimento.getIdProcedimento());
        if (removed)
        {
          IuffiFactory.setQuadri(session, quadri);
        }
        retURL = getRedirectQuadroURL(procedimentoOggetto, quadri.get(0),
            request, session);
        // Metto in sessione l'elenco delle icone visualizzabili per questo
        // procedimento
        Map<String, OggettoIconaDTO> iconeTestata = quadroEJB
            .getIconeTestata(procedimentoOggetto.getIdProcedimentoOggetto());
        if (iconeTestata != null)
        {
          session.setAttribute("iconeTestata", iconeTestata);
        }
        else
        {
          session.removeAttribute("iconeTestata");
        }
        return retURL;
      }
      else
      {
        IuffiFactory.removeIdProcedimentoOggettoFromSession(session);
        throw new ApplicationException(
            "L'utente corrente non è abilitato ad accedere ai dati dell'oggetto \""
                + procedimentoOggetto.getDescrizione() + "\"");
      }
    }
    else
    {
      throw new ApplicationException(ERRORE_PROCEDIMENTO_OGGETTO);
    }
  }

  @RequestMapping(value = "/quadro_{codQuadro}")
  public String selezionaQuadro(HttpSession session,
      Model model,
      @PathVariable("codQuadro") String codQuadro, HttpServletRequest request)
      throws InternalUnexpectedException,
      ApplicationException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    QuadroOggettoDTO quadro = null;

    for (QuadroOggettoDTO qdr : procedimentoOggetto.getQuadri())
    {
      if (qdr.getCodQuadro().equalsIgnoreCase(codQuadro))
      {
        quadro = qdr;
        break;
      }
    }

    String retURL = getRedirectQuadroURL(procedimentoOggetto, quadro, request,
        session);
    if (!GenericValidator.isBlankOrNull(retURL))
      return retURL;

    model.addAttribute("titolo", "Errore");
    model.addAttribute("messaggio",
        "Si è verificato un errore interno. Contattare l'assistenza comunicando il seguente messaggio: mapping di pagina non trovato per il quadro:"
            + procedimentoOggetto.getQuadri().get(0).getCodQuadro());
    return "errore/messaggio";
  }

  private String getRedirectQuadroURL(ProcedimentoOggetto procedimentoOggetto,
      QuadroOggettoDTO quadro, HttpServletRequest request, HttpSession session)
      throws ApplicationException
  {
    AzioneDTO azione1 = null;
    AzioneDTO azione2 = null;

    if (quadro == null)
      return null;

    // Decido su quale quadro andare e instrado
    for (AzioneDTO azione : quadro.getAzioni())
    {
      if (azione.getPriorita() != null && azione.getPriorita() == 1)
      {
        azione1 = azione;
      }
      else
        if (azione.getPriorita() != null && azione.getPriorita() == 2)
        {
          azione2 = azione;
        }
    }

    if (azione1.getPriorita() != null && azione1.getPriorita() == 1)
    {
      if (securityManager.isAccessoAutorizzatoForCU(request,
          azione1.getCodiceCdu(),
          IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO))
      {
        return "redirect:../"
            + azione1.getCodiceCdu().replace("-", "").toLowerCase()
            + "/index.do";
      }
    }
    if (azione2 == null)
    {
      throw new ApplicationException(
          "L'utente corrente non è abilitato ad accedere ai dati dell'oggetto \""
              + procedimentoOggetto.getDescrizione() + "\"");
    }
    else
    {
      if (securityManager.isAccessoAutorizzatoForCU(request,
          azione2.getCodiceCdu(),
          IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO))
      {
        return "redirect:../"
            + azione2.getCodiceCdu().replace("-", "").toLowerCase()
            + "/index.do";
      }
      else
      {
        throw new ApplicationException(
            "L'utente corrente non è abilitato ad accedere ai dati dell'oggetto \""
                + procedimentoOggetto.getDescrizione() + "\"");
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void cleanFilters(HttpSession session)
  {
    final String ID_ELENCO_PARTICELLE = "elencoParticelle";
    final String ID_ELENCO_PARTICELLE_ISTR = "elencoParticelleIstruttoria";
    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    mapFilters.remove(ID_ELENCO_PARTICELLE);
    mapFilters.remove(ID_ELENCO_PARTICELLE_ISTR);
    MapColonneNascosteVO hColumns = (MapColonneNascosteVO) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_COLONNE_NASCOSTE);
    hColumns.removeTable(ID_ELENCO_PARTICELLE);
    mapFilters.remove(ID_ELENCO_PARTICELLE_ISTR);
  }

}
