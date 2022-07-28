package it.csi.iuffi.iuffiweb.presentation.danni;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.ParametriRicercaParticelle;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public class CUIUFFI298DanniBaseController extends BaseController
{
	  
	  final protected String fieldNameDescrizione = "descrizione";
	  final protected String fieldNameUnitaMisura = "unitaMisura";
	  final protected String fieldNameQuantita = "quantita";
	  final protected String fieldNameImporto = "importo";
	  final protected String fieldNameIdDanno = "idDanno";
	  final protected String fieldNameIdScortaMagazzino = "idScortaMagazzino";

	  @Autowired
	  protected IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  @RequestMapping(value = "/elenco_comuni_conduzioni_superfici_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<DecodificaDTO<String>> getListComuniPerProvinciaConTerreniInConduzione(
				Model model,
				@PathVariable("istatProvincia") String istatProvincia,
				HttpSession session)
						throws InternalUnexpectedException
	  {
		  return quadroIuffiEJB.getListComuniPerProvinciaConTerreniInConduzione(
				getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto(),
				istatProvincia);
	  }
	  
	  @RequestMapping(value = "/elenco_sezioni_comune_{istatComune}", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<DecodificaDTO<String>> getListSezioniPerComune(
			  Model model,
			  @PathVariable("istatComune") String istatComune,
			  HttpSession session) throws InternalUnexpectedException
	  {
		  return quadroIuffiEJB.getListSezioniPerComuneDanniSuperficiColture(getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto(), istatComune);
	  }
	  

	@IsPopup
	@RequestMapping(value = "/popup_ricerca_conduzioni", method = RequestMethod.GET)
	public String popupRicercaConduzioni(Model model, HttpServletRequest request, HttpSession session,
	   @ModelAttribute("filtroParticelle") ParametriRicercaParticelle filtro)
	   throws InternalUnexpectedException
	{
		String fieldNameIdDanno = "idDanno";
		String fieldIdDanno = request.getParameter(fieldNameIdDanno);
		boolean piantagioniArboree = false;
		if(Integer.parseInt(fieldIdDanno) == IuffiConstants.DANNI.PIANTAGIONI_ARBOREE)
		{
			piantagioniArboree = true;
		}
		FiltroRicercaConduzioni filtroRicercaConduzioni = new FiltroRicercaConduzioni();
		Errors errors = new Errors();
		boolean hasComune = errors.validateMandatory(filtro.getIstatComune(),"istatComune");
		errors.validateMandatory(filtro.getIstatProvincia(), "istatProvincia");
		errors.validateMandatory(filtro.getSezione(), "sezione");
		model.addAttribute("sezioneDisabled",Boolean.valueOf(filtro.getSezione() == null));
		Long foglio = errors.validateMandatoryLongInRange(filtro.getFoglio(), "foglio", 1l, 9999l);
		if (foglio != null)
		{
		  filtroRicercaConduzioni.setFoglio(foglio);
		}
		filtroRicercaConduzioni.setParticella(errors.validateOptionalLongInRange(
		    filtro.getParticella(), "particella", 1l, 99999l));
		errors.validateOptionalFieldLength(filtro.getSubalterno(), "subalterno", 1, 3);
		if (errors.addToModelIfNotEmpty(model))
		{
			model.addAttribute(fieldNameIdDanno, fieldIdDanno);
			model.addAttribute("preferRequest", Boolean.TRUE);
			long idProcedimentoOggetto = getIdProcedimentoOggetto(request.getSession());
			model.addAttribute("provincie",quadroIuffiEJB.getListProvinciaConTerreniInConduzione(idProcedimentoOggetto, IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE));
			if (hasComune)
			{
				  model.addAttribute("sezione",quadroIuffiEJB.getListSezioniPerComuneDanniSuperficiColture(idProcedimentoOggetto, filtro.getIstatComune()));
			}
			final String istatProvincia = filtro.getIstatProvincia();
			if (!GenericValidator.isBlankOrNull(istatProvincia))
			{
				  List<DecodificaDTO<String>> comuni = quadroIuffiEJB.getListComuniPerProvinciaConTerreniInConduzione(idProcedimentoOggetto, istatProvincia);
				  model.addAttribute("comuni", comuni);
			}
			else
			{
				  model.addAttribute("comuneDisabled", Boolean.TRUE);
			}
			return "danni/include/filtriInserimentoConduzione";
		}
		else
		{
			  filtroRicercaConduzioni.setIstatComune(filtro.getIstatComune());
			  String sezione = filtro.getSezione();
			  if ("null".equalsIgnoreCase(sezione))
			  {
			    sezione = null;
			  }
			  filtroRicercaConduzioni.setSezione(sezione);
			  filtroRicercaConduzioni.setSubalterno(filtro.getSubalterno());
			  filtroRicercaConduzioni.setChiaviConduzioniInserite(request.getParameterValues("idChiaveConduzione"));
			  filtroRicercaConduzioni.setIdProcedimentoOggetto(getIdProcedimentoOggetto(session));
			  filtroRicercaConduzioni.setChiaviConduzioniInserite(request.getParameterValues("idUtilizzoDichiarato"));
			  
			  session.setAttribute(FiltroRicercaConduzioni.class.getName(),filtroRicercaConduzioni);
			  model.addAttribute("piantagioniArboree", piantagioniArboree);
			  return "danni/popupRicercaConduzioni";
		}
	  }
	
	  @RequestMapping(value = "/elenco_particelle_danni_{piantagioniArboree}", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<ParticelleDanniDTO> elencoParticelleDanni(
			  HttpSession session, 
			  Model model, HttpServletRequest request,
			  @PathVariable("piantagioniArboree") boolean piantagioniArboree)
	      throws InternalUnexpectedException
	  {
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	    String classFiltro = FiltroRicercaConduzioni.class.getName();
	    FiltroRicercaConduzioni filtroRicercaConduzioni = (FiltroRicercaConduzioni) session.getAttribute(classFiltro);
	    session.removeAttribute(classFiltro);
		List<ParticelleDanniDTO> lista = quadroIuffiEJB.getListConduzioniDannoEscludendoGiaSelezionate(idProcedimentoOggetto, filtroRicercaConduzioni,piantagioniArboree);
	    return lista;
	  }
}
