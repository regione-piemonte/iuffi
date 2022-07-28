package it.csi.iuffi.iuffiweb.presentation.scorte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-297-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi297m")
public class CUIUFFI297MScorteModificaController extends BaseController
{
	private static final String FIELD_NAME_DESCRIZIONE_SCORTA = "descrizione";
	private static final String FIELD_NAME_UNITA_DI_MISURA_HIDDEN = "unitaDiMisuraHidden";
	private static final String FIELD_NAME_UNITA_DI_MISURA = "unitaDiMisura";
	private static final String FIELD_NAME_ID_SCORTA = "idScorta";
	private static final String FIELD_NAME_QUANTITA = "quantita";
	
	@Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	
	@RequestMapping(value = "/modifica_scorte_dettaglio", method = RequestMethod.POST)
	public String modificaScorte(HttpSession session, Model model, HttpServletRequest request,
			@RequestParam("idScortaMagazzino") String[] arrayIdScortaMagazzino)
			throws InternalUnexpectedException
	{

		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		long idScortaAltro = quadroIuffiEJB.getIdScorteAltro();
		List<ScorteDTO> scorte = new ArrayList<ScorteDTO>();
		Map<Long,Boolean> mappaDisabledDescrizione = new HashMap<Long,Boolean>();
		scorte = quadroIuffiEJB.getScorteByIds(IuffiUtils.ARRAY.toLong(arrayIdScortaMagazzino), idProcedimentoOggetto);
		List<DecodificaDTO<Long>> elencoTipologieScorte = quadroIuffiEJB.getElencoTipologieScorte();
		List<DecodificaDTO<Long>> elencoUnitaMisura = quadroIuffiEJB.getListUnitaDiMisura();
		Map<Long,Long> mappaTipologiaScorteUnitaDiMisura = quadroIuffiEJB.getMapTipologiaScorteUnitaDiMisura();
		for(ScorteDTO scorta : scorte)
		{
			if(scorta.getIdScorta() == idScortaAltro)
			{
				mappaDisabledDescrizione.put(scorta.getIdScortaMagazzino(),false);
			}
			else
			{
				mappaDisabledDescrizione.put(scorta.getIdScortaMagazzino(),true);
			}
		}
		model.addAttribute("elencoTipologieScorte", elencoTipologieScorte);
		model.addAttribute("elencoUnitaMisura", elencoUnitaMisura);
		model.addAttribute("idScortaAltro", idScortaAltro);
		model.addAttribute("mappaTipologiaScorteUnitaDiMisura", mappaTipologiaScorteUnitaDiMisura);
		model.addAttribute("mappaDisabledDescrizione", mappaDisabledDescrizione);
		model.addAttribute("scorte", scorte);
		return "scorte/modificaScorteMultipla";
	}
	
	@RequestMapping(value = "/modifica_scorte_conferma", method = RequestMethod.POST)
	public String modificaScorteConferma(HttpSession session, HttpServletRequest request, Model model)
			throws InternalUnexpectedException, ApplicationException
	{
		boolean isErrato=false;
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		String[] arrayIdScortaMagazzino = request.getParameterValues("idScortaMagazzino");
		List<String> listaTipologiaScorte = new ArrayList<String>();
		List<DecodificaDTO<Long>> elencoTipologieScorte = quadroIuffiEJB.getElencoTipologieScorte();
		List<DecodificaDTO<Long>> elencoUnitaMisura = quadroIuffiEJB.getListUnitaDiMisura();
		Map<Long,Long> mappaTipologiaScorteUnitaDiMisura = quadroIuffiEJB.getMapTipologiaScorteUnitaDiMisura();
		List<ScorteDTO> listScorte = new ArrayList<ScorteDTO>();
		Map<Long,Boolean> mappaDisabledDescrizione = new HashMap<Long,Boolean>();
		
		
		List<String> listIdUnitaMisura = new ArrayList<String>();
		for (DecodificaDTO<Long> dt : elencoUnitaMisura)
		{
			listIdUnitaMisura.add(Long.toString(dt.getId()));
		}
		String[] arrayIdUnitaMisura = listIdUnitaMisura.toArray(new String[listIdUnitaMisura.size()]);
		for(DecodificaDTO<Long> elem : elencoTipologieScorte)
		{
	    	  listaTipologiaScorte.add(elem.getId().toString());
	    }
		long idScortaAltro = quadroIuffiEJB.getIdScorteAltro();
	      
		Errors errors = new Errors();
		for(String idScortaMagazzino : arrayIdScortaMagazzino)
		{
			String fieldNameIdScorta = FIELD_NAME_ID_SCORTA + "_" + idScortaMagazzino;
			String fieldNameDescrizione = FIELD_NAME_DESCRIZIONE_SCORTA + "_" + idScortaMagazzino;
			String fieldNameQuantita = FIELD_NAME_QUANTITA + "_" + idScortaMagazzino;
			String fieldNameUnitaDiMisura = FIELD_NAME_UNITA_DI_MISURA + "_" + idScortaMagazzino;
			String fieldNameUnitaDiMisuraHidden = FIELD_NAME_UNITA_DI_MISURA_HIDDEN + "_" + idScortaMagazzino;
			
			String fieldIdScorta = request.getParameter(fieldNameIdScorta);
			String fieldDescrizione = request.getParameter(fieldNameDescrizione);
			String fieldQuantita = request.getParameter(fieldNameQuantita);
			String fieldUnitaDiMisuraHidden = request.getParameter(fieldNameUnitaDiMisuraHidden);
			String descrizione;
			Long idUnitaDiMisura=null;
			errors.validateMandatoryValueList(fieldIdScorta, fieldNameIdScorta, listaTipologiaScorte.toArray(new String[listaTipologiaScorte.size()]));
			BigDecimal bdQuantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, fieldNameQuantita, 2, new BigDecimal("0.01"), new BigDecimal("99999.99"));
			errors.validateMandatoryValueList(fieldUnitaDiMisuraHidden, fieldNameUnitaDiMisuraHidden, arrayIdUnitaMisura);
			
			if (Long.toString(idScortaAltro).equals(fieldIdScorta))
			{
				errors.validateMandatoryFieldLength(fieldDescrizione, 1, 4000, fieldNameDescrizione, true);
				errors.validateMandatoryValueList(fieldUnitaDiMisuraHidden, fieldNameUnitaDiMisura, arrayIdUnitaMisura);
				descrizione = fieldDescrizione;
				if(fieldUnitaDiMisuraHidden != null && !fieldUnitaDiMisuraHidden.equals(""))
				{
					idUnitaDiMisura = new Long(fieldUnitaDiMisuraHidden);
				}
				mappaDisabledDescrizione.put(new Long(idScortaMagazzino), false);
			} 
			else
			{
				fieldDescrizione = null;
				descrizione = null;
				idUnitaDiMisura = null;
				mappaDisabledDescrizione.put(new Long(idScortaMagazzino), true);
				
			}
			if (errors.size() != 0)
			{
				isErrato = true;
			}
			if(!isErrato)
			{
				ScorteDTO scorta = new ScorteDTO();
				scorta.setIdScortaMagazzino(IuffiUtils.NUMBERS.getNumericValue(idScortaMagazzino));
			    scorta.setIdScorta(IuffiUtils.NUMBERS.getNumericValue(fieldIdScorta));
			    scorta.setQuantita(bdQuantita);
			    scorta.setIdUnitaMisura(idUnitaDiMisura);
			    scorta.setDescrizione(descrizione);
			    listScorte.add(scorta);
			}
		}
		
		if(isErrato)
		{
			errors.addToModelIfNotEmpty(model);
			List<ScorteDTO> scorte = quadroIuffiEJB.getScorteByIds(IuffiUtils.ARRAY.toLong(arrayIdScortaMagazzino), idProcedimentoOggetto);
			for(ScorteDTO scorta : scorte)
			{
				String idScorta = request.getParameter("idScorta_" + scorta.getIdScortaMagazzino());
				if(IuffiUtils.NUMBERS.isNumericValue(idScorta) && IuffiUtils.NUMBERS.checkLong(idScorta) != idScortaAltro)
				{
					if(mappaTipologiaScorteUnitaDiMisura.containsKey(new Long(idScorta)))
					{
						Long idUnitaMisura = mappaTipologiaScorteUnitaDiMisura.get(Long.parseLong(idScorta));
						scorta.setIdUnitaMisura(idUnitaMisura);
					}
				}
				else
				{
					scorta.setIdUnitaMisura(null);
				}
			}
			model.addAttribute("preferRequest", Boolean.TRUE);
			model.addAttribute("elencoTipologieScorte", elencoTipologieScorte);
			model.addAttribute("elencoUnitaMisura", elencoUnitaMisura);
			model.addAttribute("idScortaAltro", idScortaAltro);
			model.addAttribute("mappaTipologiaScorteUnitaDiMisura", mappaTipologiaScorteUnitaDiMisura);
			model.addAttribute("mappaDisabledDescrizione",mappaDisabledDescrizione);
			model.addAttribute("scorte",scorte);
			return "scorte/modificaScorteMultipla";
		}
		else
		{
			long nScorteModificate = 
					quadroIuffiEJB.modificaScorte(listScorte, getLogOperationOggettoQuadroDTO(session), idProcedimentoOggetto);
			if(nScorteModificate == IuffiConstants.ERRORI.ELIMINAZIONE_SCORTE_CON_DANNI_CON_INTERVENTI)
			{
				throw new ApplicationException("Impossibile eliminare le scorte desiderate perchè esistono degli interventi associati ai danni delle scorte",10003);
			}
		}
		return "redirect:../cuiuffi297l/index.do";
	}
	
	  @RequestMapping(value = "/get_unita_misura_by_scorta_{idScorta}", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public Long getUnitaMisuraByScorta(
			  HttpSession session, 
			  Model model,
			  @PathVariable(FIELD_NAME_ID_SCORTA) long idScorta)  throws InternalUnexpectedException
	  {
		  return quadroIuffiEJB.getUnitaMisuraByScorta(idScorta);
	  }
	  
	@IsPopup
	@RequestMapping(value = "/conferma_modifica_scorta_{idScortaMagazzino}", method = RequestMethod.GET)
	public String confermaModificaScorta(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable("idScortaMagazzino") long idScortaMagazzino) throws InternalUnexpectedException
	{
		long [] arrayIdScortaMagazzino = new long[]{idScortaMagazzino};
		return confermaModificaGenerico(model, session, arrayIdScortaMagazzino);
	}
	
	@IsPopup
	@RequestMapping(value = "/conferma_modifica_scorte", method = RequestMethod.POST)
	public String confermaModificaScorte(
			Model model,
			HttpSession session,
			HttpServletRequest request
			) throws InternalUnexpectedException
	{
		String[] arrayIdScortaMagazzino = request.getParameterValues("idScortaMagazzino");
		return confermaModificaGenerico(model, session, IuffiUtils.ARRAY.toLong(arrayIdScortaMagazzino));
	}

	private String confermaModificaGenerico(Model model, HttpSession session, long[] arrayIdScortaMagazzino)
			throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		if(arrayIdScortaMagazzino == null)
		{
			arrayIdScortaMagazzino = new long[0];
		}
		Long nDanniScorte = quadroIuffiEJB.getNDanniScorte(idProcedimentoOggetto, arrayIdScortaMagazzino);
		List<Long> listIdScortaMagazzino = new ArrayList<Long>();
		for(long l : arrayIdScortaMagazzino)
		{
			listIdScortaMagazzino.add(l);
		}
		long n = quadroIuffiEJB.getNInterventiAssociatiDanniScorte(idProcedimentoOggetto, listIdScortaMagazzino);
		if(n > 0L)
		{
			model.addAttribute("azione", "modificare");
			return "scorte/failureScorteDanniInterventi";
		}
		model.addAttribute("ids", arrayIdScortaMagazzino);
		model.addAttribute("len", arrayIdScortaMagazzino.length);
		model.addAttribute("nDanniScorte",nDanniScorte);
		return "scorte/confermaModificaScorte";
	}
	  
}
