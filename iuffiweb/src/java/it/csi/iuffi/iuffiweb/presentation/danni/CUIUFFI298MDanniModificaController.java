package it.csi.iuffi.iuffiweb.presentation.danni;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.QuadroIuffiDAO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiConstants.DANNI;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-298-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi298m")
public class CUIUFFI298MDanniModificaController extends CUIUFFI298DanniBaseController
{

	private static final String cu = "298m";
	private final String urlDannoDettaglio = "modifica_danno_conduzioni_";
	private final String dataUrl  = "get_list_conduzioni_danno_";
	
	ObjectWriter jacksonWriter = new ObjectMapper().writer(new DefaultPrettyPrinter());
	
	@RequestMapping(value = "/modifica_danni_multipla", method = RequestMethod.POST)
	public String modificaDanniMultipla(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam("idDannoAtm") String[] arrayIdDannoAtm,
			RedirectAttributes redirectAttributes) throws InternalUnexpectedException, ApplicationException
	{
		
		return modificaDanniGenerico(session, model, IuffiUtils.ARRAY.toLong(arrayIdDannoAtm), redirectAttributes);
	}

	@RequestMapping(value = "/modifica_danno_{idDannoAtm}", method = RequestMethod.GET)
	public String modificaDanno(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable("idDannoAtm") long idDannoAtm) throws InternalUnexpectedException, ApplicationException
	{
		long[] arrayIdDannoAtm = new long[] { idDannoAtm };
		return modificaDanniGenerico(session, model, arrayIdDannoAtm, null);
	}
	
	@RequestMapping(value = "/modifica_danno_{idDannoAtm}", method = RequestMethod.POST)
	public String modificaDannoIndietro(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable("idDannoAtm") long idDannoAtm) throws InternalUnexpectedException,JsonGenerationException,JsonMappingException, IOException, ApplicationException
	{
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		String paginaDaCaricare = null;
		long[] arrayIdDannoAtm = new long[] { idDannoAtm };
		long[] arrayIdUtilizzoDichiarato = IuffiUtils.ARRAY.toLong(request.getParameterValues("idUtilizzoDichiarato"));
		List<DanniDTO> danni = quadroIuffiEJB.getListDanniByIdsProcedimentoOggetto(idProcedimentoOggetto,arrayIdDannoAtm, getUtenteAbilitazioni(session).getIdProcedimento());
		DanniDTO danno = danni.get(0);
		int idDanno = danno.getIdDanno();
		boolean piantagioniArboree = (danno.getIdDanno() == IuffiConstants.DANNI.PIANTAGIONI_ARBOREE);
		List<Integer> listaIdDannoConduzioni = QuadroIuffiDAO.getListDanniEquivalenti(IuffiConstants.DANNI.TERRENI_RIPRISTINABILI);
		if(listaIdDannoConduzioni.contains(idDanno))
		{
			List<ParticelleDanniDTO> listConduzioni = quadroIuffiEJB.getListConduzioniDannoGiaSelezionate(idProcedimentoOggetto, arrayIdUtilizzoDichiarato, piantagioniArboree);
			paginaDaCaricare = modificaDanniGenerico(session, model, arrayIdDannoAtm, null);
			model.addAttribute("dataUrl","");
			
			//coverte le conduzioni in json per poterle appendere alla bootstrap table
		    StringWriter sw = new StringWriter();
		    jacksonWriter.writeValue(sw, listConduzioni);
		    model.addAttribute("json", sw.toString());
		}
		else
		{
			paginaDaCaricare = "redirect:../cuiuffi298l/index.do";
		}
		return paginaDaCaricare;
	}

	private String modificaDanniGenerico(HttpSession session, 
			Model model, 
			long[] arrayIdDannoAtm,
			RedirectAttributes redirectAttributes)
			throws InternalUnexpectedException,ApplicationException
	{
		session.removeAttribute(FiltroRicercaConduzioni.class.getName());
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		
		long nInterventi = quadroIuffiEJB.getNInterventiAssociatiDanni(idProcedimentoOggetto, arrayIdDannoAtm);
		if(nInterventi > 0L)
		{
			throw new ApplicationException("Impossibile modificare danni per i quali esistono degli interventi associati",10005);
		}
		
		List<DecodificaDTO<Long>> listUnitaDiMisura = quadroIuffiEJB.getListUnitaDiMisura();
		if (arrayIdDannoAtm != null && arrayIdDannoAtm.length != 0)
		{
			List<DanniDTO> danni = quadroIuffiEJB.getDanniByIdDannoAtm(arrayIdDannoAtm, idProcedimentoOggetto, getUtenteAbilitazioni(session).getIdProcedimento());
			List<Long> listIdDannoAtm = new ArrayList<Long>();
			int counterDanniConduzioni = 0;
			List<Integer> listIdDannoConduzioni = QuadroIuffiDAO.getListDanniEquivalenti(IuffiConstants.DANNI.TERRENI_RIPRISTINABILI);
			for (DanniDTO danno : danni)
			{
				listIdDannoAtm.add(danno.getIdDannoAtm());
				if(listIdDannoConduzioni.contains(danno.getIdDanno()))
				{
					counterDanniConduzioni++;
				}
			}
			if(counterDanniConduzioni > 0 && danni.size() > 1)
			{
				redirectAttributes.addAttribute("errorModificaMultipla","true");
				return "redirect:../cuiuffi298l/index.do";
			}else if(counterDanniConduzioni > 0)
			{
				DanniDTO danno = danni.get(0);
				boolean piantagioniArboree = false;
				switch(danno.getIdDanno())
				{
					case IuffiConstants.DANNI.PIANTAGIONI_ARBOREE:
						piantagioniArboree = true;
					case IuffiConstants.DANNI.TERRENI_RIPRISTINABILI:
					case IuffiConstants.DANNI.TERRENI_NON_RIPRISTINABILI:
					case IuffiConstants.DANNI.ALTRE_PIANTAGIONI:
						model.addAttribute("provincie",quadroIuffiEJB.getListProvinciaConTerreniInConduzione(idProcedimentoOggetto, IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE));
						model.addAttribute("comuneDisabled", Boolean.TRUE);
						model.addAttribute("sezioneDisabled", Boolean.TRUE);
						model.addAttribute("idDanno",danno.getIdDanno());
						model.addAttribute("piantagioniArboree",piantagioniArboree);
						model.addAttribute("modifica",Boolean.TRUE);
						model.addAttribute("cu", cu);
						model.addAttribute("idDannoAtm",danno.getIdDannoAtm());
						model.addAttribute("dataUrl", dataUrl + danno.getIdDannoAtm() + ".do");
						model.addAttribute("urlDannoDettaglio", urlDannoDettaglio + danno.getIdDannoAtm() + ".do");
				}
				return "danni/elencoConduzioni";
			}
			else
			{
				model.addAttribute("listUnitaDiMisura", listUnitaDiMisura);
				model.addAttribute("danni", danni);
				return "danni/modificaDanni";
			}
		} else
		{
			return "redirect:../cuiuffi298l/index.do";
		}
	}
	
	@RequestMapping(value = "/modifica_danno_conduzioni_{idDannoAtm}", method = RequestMethod.POST)
	public String modificaDannoConduzioni(
			HttpServletRequest request, 
			HttpSession session, 
			Model model,
			@PathVariable("idDannoAtm") long idDannoAtm) throws InternalUnexpectedException, ApplicationException
	{
		session.removeAttribute(FiltroRicercaConduzioni.class.getName());
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		long[] arrayIdUtilizzoDichiarato = IuffiUtils.ARRAY.toLong(request.getParameterValues("idUtilizzoDichiarato"));
		long[] arrayIdDannoAtm = new long[] {idDannoAtm};
		boolean piantagioniArboree = false;
		List<DanniDTO> danni = quadroIuffiEJB.getDanniByIdDannoAtm(arrayIdDannoAtm, idProcedimentoOggetto, getUtenteAbilitazioni(session).getIdProcedimento());
		DanniDTO danno = danni.get(0);
		if(danno.getIdDanno() == IuffiConstants.DANNI.PIANTAGIONI_ARBOREE)
		{
			piantagioniArboree = true;
		}
		List<DecodificaDTO<Long>> listUnitaDiMisura = quadroIuffiEJB.getListUnitaDiMisura();
		List<ParticelleDanniDTO> listConduzioni = quadroIuffiEJB.getListConduzioniDannoGiaSelezionate(idProcedimentoOggetto, arrayIdUtilizzoDichiarato, piantagioniArboree);
		//nessuna conduzione, ricarica la view precedente 
		if(listConduzioni.size() == 0)
		{
			modificaDanniGenerico(session,model,arrayIdDannoAtm,null);
			model.addAttribute("dataUrl", "");
			model.addAttribute("errorNoConduzioni", Boolean.TRUE);
			return "danni/elencoConduzioni";
		}
		else
		{
			model.addAttribute("listUnitaDiMisura", listUnitaDiMisura);
			model.addAttribute("danni", danni);
			model.addAttribute("idDanno", danno.getIdDanno());
			model.addAttribute("listConduzioni", listConduzioni);
			return "danni/modificaDanni";
		}
	}

	@RequestMapping(value = "/get_list_conduzioni_danno_{idDannoAtm}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<ParticelleDanniDTO> elencoParticelleDanni(
			  HttpSession session, 
			  Model model, HttpServletRequest request,
			  @PathVariable("idDannoAtm") long idDannoAtm)
	    throws InternalUnexpectedException
	{
		 long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		 List<ParticelleDanniDTO> lista = quadroIuffiEJB.getListConduzioniDanno(idProcedimentoOggetto,idDannoAtm);
		 return lista;
	}
	 
		
	@RequestMapping(value = "/modifica_danni_conferma", method = RequestMethod.POST)
	public String modificaDanniConferma(HttpServletRequest request, HttpSession session, Model model)
			throws InternalUnexpectedException, ApplicationException
	{
			long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
			LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
			Errors errors = new Errors();
			long[] arrayIdDannoAtm = IuffiUtils.ARRAY.toLong(request.getParameterValues("idDannoAtm"));
			
			long nInterventi = quadroIuffiEJB.getNInterventiAssociatiDanni(idProcedimentoOggetto, arrayIdDannoAtm);
			if(nInterventi > 0)
			{
				throw new ApplicationException("Impossibile modificare i danni per i quali esistono interventi associati.",10004);
			}
			
			long[] arrayIdUtilizzoDichiarato = null;
			BigDecimal quantita = null;
			if (arrayIdDannoAtm != null && arrayIdDannoAtm.length > 0)
			{
				List<DanniDTO> danni = quadroIuffiEJB.getListDanniByIdsProcedimentoOggetto(idProcedimentoOggetto,arrayIdDannoAtm, getUtenteAbilitazioni(session).getIdProcedimento());
				Map<Long, DanniDTO> mappaDanniOriginali = new HashMap<Long, DanniDTO>();
				int counterDanniSuperficiColture=0;
				List<Integer> listaDanniEquivalentiConduzioni = QuadroIuffiDAO.getListDanniEquivalenti(DANNI.TERRENI_RIPRISTINABILI);
				for (DanniDTO danno : danni)
				{
					mappaDanniOriginali.put(danno.getIdDannoAtm(), danno);
					//verifico che non si stia cercando di modificare più danni associati a delle superfici contemporaneamente
					if(listaDanniEquivalentiConduzioni.contains(danno.getIdDanno()))
					{
						counterDanniSuperficiColture++;
					}
				}
				if(counterDanniSuperficiColture > 0 && danni.size()>1)
				{
					return "redirect:../cuiuffi298l/index.do";
				}
				List<DanniDTO> listDanni = new ArrayList<DanniDTO>();
				for (long idDannoAtm : arrayIdDannoAtm)
				{
					DanniDTO dannoOriginale = mappaDanniOriginali.get(idDannoAtm);
					String currentFieldNameDescrizione = fieldNameDescrizione + "_" + idDannoAtm;
					String currentFieldNameImporto = fieldNameImporto + "_" + idDannoAtm;
					String currentFieldNameQuantita = fieldNameQuantita + "_" + idDannoAtm;

					String fieldDescrizione = request.getParameter(currentFieldNameDescrizione);
					String fieldImporto = request.getParameter(currentFieldNameImporto);
					String fieldQuantita = request.getParameter(currentFieldNameQuantita);

					errors.validateMandatory(fieldDescrizione, currentFieldNameDescrizione);
					errors.validateMandatoryBigDecimalInRange(fieldImporto, currentFieldNameImporto, 2,new BigDecimal("0.01"), new BigDecimal("9999999.99"));
					if (!dannoOriginale.getIsModificaQuantitaDisabled()
							&& !listaDanniEquivalentiConduzioni.contains(dannoOriginale.getIdDanno())
							&& dannoOriginale.getIdDanno() != IuffiConstants.DANNI.ALTRO
							)
					{
						errors.validateMandatoryBigDecimalInRange(fieldQuantita, currentFieldNameQuantita, 2,
								new BigDecimal("0.01"), new BigDecimal("99999.99"));
					}
					DanniDTO danno = mappaDanniOriginali.get(idDannoAtm);
					if(danno.getNomeTabella() == null) //altro e altri
					{
						quantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, currentFieldNameQuantita, 4, BigDecimal.ZERO, new BigDecimal("99999.9999"));
					}
					else if (danno.getIdDanno() == IuffiConstants.DANNI.SCORTA
							|| danno.getIdDanno() == IuffiConstants.DANNI.SCORTE_MORTE)
					{
						ScorteDTO scorta = quadroIuffiEJB.getScortaByIdScortaMagazzino(danno.getExtIdEntitaDanneggiata());
						quantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, currentFieldNameQuantita, 2,
								new BigDecimal("0.01"), scorta.getQuantita());
					} 
					else if(danno.getIdDanno() == IuffiConstants.DANNI.ALLEVAMENTO)
					{
						long[] arrayIdDannoAtmSingolo = new long[]{danno.getIdDannoAtm()};
						AllevamentiDTO allevamento = quadroIuffiEJB.getListAllevamentiByIdDannoAtm(idProcedimentoOggetto, arrayIdDannoAtmSingolo).get(0);
						quantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, currentFieldNameQuantita,0, new BigDecimal("1"), new BigDecimal(allevamento.getQuantita()));
	
					}
					else if(danno.getIdDanno() == IuffiConstants.DANNI.FABBRICATO)
					{
						quantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, currentFieldNameQuantita, 2, new BigDecimal("0.01"), new BigDecimal("99999.99"));
					}	
					else
					{
						List<Integer> listDanniEquivalentiConduzioni = QuadroIuffiDAO.getListDanniEquivalenti(IuffiConstants.DANNI.TERRENI_RIPRISTINABILI);
						if(listDanniEquivalentiConduzioni
								.contains(dannoOriginale.getIdDanno()))
						{
							arrayIdUtilizzoDichiarato = IuffiUtils.ARRAY.toLong(request.getParameterValues("idUtilizzoDichiarato"));
							BigDecimal sumSuperficiCatastali = 
									  quadroIuffiEJB.getSumSuperficiCatastaliParticelle(idProcedimentoOggetto,arrayIdUtilizzoDichiarato);			  
							quantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, currentFieldNameQuantita, 4, 
									  new BigDecimal("0.0001"),
									  new BigDecimal("99999.9999").min(sumSuperficiCatastali));
							if (errors.addToModelIfNotEmpty(model))
							{
								model.addAttribute("preferRequest", Boolean.TRUE);
								return this.modificaDannoConduzioni(request, session, model, danno.getIdDannoAtm());
							}
						}
					}
					
					if (!dannoOriginale.getIsModificaQuantitaDisabled())
					{
						danno.setQuantita(quantita);
					}
				}
				if (errors.addToModelIfNotEmpty(model))
				{
					model.addAttribute("preferRequest", Boolean.TRUE);
					return this.modificaDanniGenerico(session, model, arrayIdDannoAtm, null);
				} else
				{
					for (long idDannoAtm : arrayIdDannoAtm)
					{
						DanniDTO dannoOriginale = mappaDanniOriginali.get(idDannoAtm);
						String currentFieldNameDescrizione = fieldNameDescrizione + "_" + idDannoAtm;
						String currentFieldNameImporto = fieldNameImporto + "_" + idDannoAtm;

						String fieldDescrizione = request.getParameter(currentFieldNameDescrizione);
						String fieldImporto = request.getParameter(currentFieldNameImporto);

						DanniDTO danno = new DanniDTO();
						danno.setIdDannoAtm(idDannoAtm);
						danno.setDescrizione(fieldDescrizione);
						danno.setImporto(new BigDecimal(fieldImporto.replace(',', '.')));
						danno.setIdDanno(dannoOriginale.getIdDanno());
						danno.setQuantita(dannoOriginale.getQuantita());
						
						listDanni.add(danno);
					}
					if(arrayIdUtilizzoDichiarato == null)
					{
						quadroIuffiEJB.modificaDanni(listDanni, idProcedimentoOggetto, logOperationOggettoQuadroDTO);
					}
					else
					{
						//danni superfici colture
						quadroIuffiEJB.modificaDanniConduzioni(listDanni.get(0), idProcedimentoOggetto, arrayIdUtilizzoDichiarato, logOperationOggettoQuadroDTO);
						
					}
				}
				return "redirect:../cuiuffi298l/index.do";
			} else
			{
				return "redirect:../cuiuffi298l/index.do";
			}
		}
	
	@RequestMapping(value = "/get_n_interventi_danno_{idDannoAtm}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public long getNInterventiDanni(
			  HttpSession session, 
			  Model model, HttpServletRequest request,
			  @PathVariable("idDannoAtm") long idDannoAtm)
	    throws InternalUnexpectedException
	{
		 long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		 return quadroIuffiEJB.getNInterventiAssociatiDanni(idProcedimentoOggetto, new long[]{idDannoAtm});
	}
	
	@RequestMapping(value = "/get_n_interventi_danni", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public long getNInterventiDanni(
			  HttpSession session, 
			  Model model, HttpServletRequest request)
	    throws InternalUnexpectedException
	{
		 long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		 String[] arrayIdDannoAtm = request.getParameterValues("idDannoAtm");
		 return quadroIuffiEJB.getNInterventiAssociatiDanni(idProcedimentoOggetto, IuffiUtils.ARRAY.toLong(arrayIdDannoAtm));
	}
	
	@IsPopup
	@RequestMapping(value = "/failure_modifica_danni_interventi", method = RequestMethod.GET)
	public String confermaModificaScorta(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
	{
		model.addAttribute("azione", "modificare");
		return "danni/failureInterventiDanni";
	}
	 
}
