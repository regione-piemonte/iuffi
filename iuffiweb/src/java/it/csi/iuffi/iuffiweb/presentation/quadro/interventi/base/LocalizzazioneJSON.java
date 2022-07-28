package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaParticelle;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONAllegatiInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONConduzioneInteventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONParticellaInteventoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class LocalizzazioneJSON extends BaseController
{

  @Autowired
  protected IInterventiEJB interventiEJB;

  @RequestMapping(value = "/load_comuni_piemonte", method = RequestMethod.GET, produces = "application/json")
  public Map<String, Map<String, String>> loadComuniPiemonte(Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    long idIntervento = new Long(request.getParameter("idIntervento"));
    Map<String, Map<String, String>> comuni = interventiEJB
        .getMapComuniPiemontesiNonInInterventoForJSON(
            getIdProcedimentoOggetto(request.getSession()),
            idIntervento);
    return comuni;
  }

  @RequestMapping(value = "/load_comuni_particelle", method = RequestMethod.GET, produces = "application/json")
  public Map<String, Map<String, String>> loadComuniParticelle(Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    long idIntervento = new Long(request.getParameter("idIntervento"));
    Map<String, Map<String, String>> comuni = interventiEJB
        .getMapComuniParticelleNonInInterventoForJSON(
            getIdProcedimentoOggetto(request.getSession()),
            idIntervento, getUtenteAbilitazioni(request.getSession()).getIdProcedimento());
    return comuni;
  }

  @RequestMapping(value = "/load_comuni_selezionati_{idIntervento}", method = RequestMethod.GET, produces = "application/json")
  public Map<String, Map<String, String>> loadComuniSelezionati(Model model,
      @PathVariable("idIntervento") long idIntervento,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Map<String, Map<String, String>> comuni = interventiEJB
        .getMapComuniPiemontesiInInterventoForJSON(
            getIdProcedimentoOggetto(request.getSession()),
            idIntervento);
    return comuni;
  }

  @RequestMapping(value = "/load_comuni_ute", method = RequestMethod.GET, produces = "application/json")
  public Map<String, Map<String, String>> loadComuniUte(Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    long idIntervento = new Long(request.getParameter("idIntervento"));
    Map<String, Map<String, String>> comuni = interventiEJB
        .getMapComuniUteNonInInterventoForJSON(
            getIdProcedimentoOggetto(request.getSession()),
            idIntervento);
    return comuni;
  }

  @RequestMapping(value = "/json/mappe_file_{idIntervento}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<RigaJSONAllegatiInterventoDTO> mappeFileJSON(Model model,
      HttpServletRequest request,
      @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    long idProcedimentoOggetto = getIdProcedimentoOggetto(request.getSession());
    List<RigaJSONAllegatiInterventoDTO> allegati = interventiEJB
        .getAllegatiJSON(idProcedimentoOggetto, idIntervento);
    if (allegati == null)
    {
      allegati = new ArrayList<RigaJSONAllegatiInterventoDTO>();
    }
    else
    {
      for (RigaJSONAllegatiInterventoDTO allegato : allegati)
      {
        allegato.setIconClassMimeType(IuffiUtils.FILE
            .getDocumentCSSIconClass(allegato.getNomeFisico()));
      }
    }
    return allegati;
  }

  @RequestMapping(value = "/json/elenco_particelle_{idIntervento}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<RigaJSONParticellaInteventoDTO> elencoParticelleJSON(Model model,
      HttpServletRequest request,
      @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    // String classFiltro = FiltroRicercaConduzioni.class.getName();
    // FiltroRicercaConduzioni filtroRicercaConduzioni =
    // (FiltroRicercaConduzioni) request.getSession().getAttribute(classFiltro);
    long idProcedimentoOggetto = getIdProcedimentoOggetto(request.getSession());
    List<RigaJSONParticellaInteventoDTO> particelle = interventiEJB
        .getElencoParticelleJSON(idProcedimentoOggetto, idIntervento);
    if (particelle == null)
    {
      particelle = new ArrayList<RigaJSONParticellaInteventoDTO>();
    }
    return particelle;
  }

  @RequestMapping(value = "/json/elenco_conduzioni_{idIntervento}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<RigaJSONConduzioneInteventoDTO> elencoConduzioniJSON(Model model,
      HttpServletRequest request,
      @PathVariable("idIntervento") Long idIntervento)
      throws InternalUnexpectedException
  {
    // String classFiltro = FiltroRicercaConduzioni.class.getName();
    // FiltroRicercaConduzioni filtroRicercaConduzioni =
    // (FiltroRicercaConduzioni) request.getSession().getAttribute(classFiltro);
    long idProcedimentoOggetto = getIdProcedimentoOggetto(request.getSession());
    List<RigaJSONConduzioneInteventoDTO> conduzioni = interventiEJB
        .getElencoConduzioniJSON(idProcedimentoOggetto, idIntervento);
    if (conduzioni == null)
    {
      conduzioni = new ArrayList<RigaJSONConduzioneInteventoDTO>();
    }
    return conduzioni;
  }

  @RequestMapping(value = "/json/ricerca_conduzioni", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<RigaJSONConduzioneInteventoDTO> ricercaConduzioni(Model model,
      HttpSession session)
      throws InternalUnexpectedException
  {
    String classFiltro = FiltroRicercaConduzioni.class.getName();
    FiltroRicercaConduzioni filtroRicercaConduzioni = (FiltroRicercaConduzioni) session
        .getAttribute(classFiltro);
    List<RigaJSONConduzioneInteventoDTO> conduzioni = interventiEJB
        .ricercaConduzioni(filtroRicercaConduzioni,getUtenteAbilitazioni(session).getIdProcedimento());
    if (conduzioni == null)
    {
      conduzioni = new ArrayList<RigaJSONConduzioneInteventoDTO>();
    }
    return conduzioni;
  }

  @RequestMapping(value = "/elenco_comuni_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
  public List<DecodificaDTO<String>> elencoComuniPerProvincia(Model model,
      @PathVariable("istatProvincia") String istatProvincia,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return interventiEJB.getDecodificheComuni(
        IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE, istatProvincia, null);
  }

  @RequestMapping(value = "/json/ricerca_particelle", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<RigaJSONParticellaInteventoDTO> ricercaParticelle(Model model,
      HttpSession session)
      throws InternalUnexpectedException
  {
    String classFiltro = FiltroRicercaParticelle.class.getName();
    FiltroRicercaParticelle filtroRicercaParticelle = (FiltroRicercaParticelle) session
        .getAttribute(classFiltro);
    List<RigaJSONParticellaInteventoDTO> conduzioni = interventiEJB
        .ricercaParticelle(filtroRicercaParticelle);
    if (conduzioni == null)
    {
      conduzioni = new ArrayList<RigaJSONParticellaInteventoDTO>();
    }
    return conduzioni;
  }

  @RequestMapping(value = "/json/ricerca_conduzioni_con_superficie", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<RigaJSONConduzioneInteventoDTO> ricercaConduzioniConSuperficie(
      Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    String classFiltro = FiltroRicercaConduzioni.class.getName();
    FiltroRicercaConduzioni filtroRicercaConduzioni = (FiltroRicercaConduzioni) session
        .getAttribute(classFiltro);
    List<RigaJSONConduzioneInteventoDTO> conduzioni = interventiEJB
        .ricercaConduzioni(filtroRicercaConduzioni,getUtenteAbilitazioni(session).getIdProcedimento());
    if (conduzioni == null)
    {
      conduzioni = new ArrayList<RigaJSONConduzioneInteventoDTO>();
    }
    return conduzioni;
  }

}