package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.math.BigDecimal;
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

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.RiepilogoDannoFaunaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-314-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi314m")
public class CUIUFFI314MModificaRiepilogoDanniFauna extends BaseController
{
	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB;
	
	final String FIELD_NAME_ID_RIEPILOGO = "idRiepilogo"; 
	final String FIELD_NAME_SUPERFICIE_ACCERTATA = "superficieAccertata_"; 
	final String FIELD_NAME_IMPORTO_DANNO_EFFETTIVO = "importoDannoEffettivo_"; 

	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String index(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
	    return indexCommon(request, session, model);
	}
	
	@RequestMapping(value = "/index_{idRiepilogo}", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpSession session, Model model, @PathVariable("idRiepilogo") String idRiepilogo) throws InternalUnexpectedException
	{
	    return indexCommon(request, session, model);
	}

	private String indexCommon(HttpServletRequest request, HttpSession session, Model model)
			throws InternalUnexpectedException
	{
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		
		boolean isIstruttoria = isIstruttoria(getProcedimentoOggettoFromSession(session));
		
	    String ultimaModifica = getUltimaModifica(quadroIuffiEJB,
				logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
				logOperationOggettoQuadroDTO.getIdQuadroOggetto(), logOperationOggettoQuadroDTO.getIdBandoOggetto());
		model.addAttribute("ultimaModifica", ultimaModifica);
		
		String[] idsRiepilogo = request.getParameterValues(FIELD_NAME_ID_RIEPILOGO);
		List<RiepilogoDannoFaunaDTO> riepilogoDanni = quadroIuffiEJB.getListaRiepilogoDanniFaunaDettaglio(getIdProcedimentoOggetto(session), idsRiepilogo);
		model.addAttribute("riepilogoDanni", riepilogoDanni);
		model.addAttribute("isIstruttoria",isIstruttoria);
	  return "danniFauna/modificaRiepilogoDanniFauna";
	}
	
	@RequestMapping(value = "/modifica", method = RequestMethod.POST)
	public String modifica(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
	    Errors errors = new Errors();
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
	    String ultimaModifica = getUltimaModifica(quadroIuffiEJB,
				logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
				logOperationOggettoQuadroDTO.getIdQuadroOggetto(), logOperationOggettoQuadroDTO.getIdBandoOggetto());
		model.addAttribute("ultimaModifica", ultimaModifica);
		
		String[] idsRiepilogo = request.getParameterValues(FIELD_NAME_ID_RIEPILOGO);
		List<RiepilogoDannoFaunaDTO> riepilogoDanni = quadroIuffiEJB.getListaRiepilogoDanniFaunaDettaglio(getIdProcedimentoOggetto(session), idsRiepilogo);
		Map<String,RiepilogoDannoFaunaDTO> mapRiepilogoDanni = new HashMap<String,RiepilogoDannoFaunaDTO>();
		for(RiepilogoDannoFaunaDTO r : riepilogoDanni)
		{
			mapRiepilogoDanni.put(r.getIdRiepilogo(), r);
		}
		for(String idRiepilogo : idsRiepilogo)
		{
			String[] ids = idRiepilogo.split("_");
			RiepilogoDannoFaunaDTO r = mapRiepilogoDanni.get(idRiepilogo);
			String importoDannoEffettivoStr = request.getParameter(FIELD_NAME_IMPORTO_DANNO_EFFETTIVO + idRiepilogo);
			String superficieAccertataStr   = request.getParameter(FIELD_NAME_SUPERFICIE_ACCERTATA + idRiepilogo);
			BigDecimal superficieAccertata = errors.validateMandatoryBigDecimalInRange(superficieAccertataStr, 
							FIELD_NAME_SUPERFICIE_ACCERTATA + idRiepilogo, 4, 
							new BigDecimal("0"), 
							r.getSuperficieCoinvolta());

			BigDecimal importoDannoEffettivo = errors.validateMandatoryBigDecimalInRange(importoDannoEffettivoStr, 
					FIELD_NAME_IMPORTO_DANNO_EFFETTIVO + idRiepilogo, 2, 
					new BigDecimal("0"), 
					new BigDecimal("9999999999.99"));
			
			if(superficieAccertata!=null && importoDannoEffettivo!=null) {
			  if(BigDecimal.ZERO.compareTo(superficieAccertata) > -1 && BigDecimal.ZERO.compareTo(importoDannoEffettivo) < 0) {
			    errors.addError(FIELD_NAME_IMPORTO_DANNO_EFFETTIVO + idRiepilogo, "Se la superficie accertata è valorizzata con 0 l'importo non può essere maggiore di 0");
			  }
			}
			
			if(errors.isEmpty())
			{
				r.setImportoDannoEffettivo(importoDannoEffettivo); 
				r.setSuperficieAccertata(superficieAccertata);
			}
		}
		if(errors.addToModelIfNotEmpty(model))
		{
			model.addAttribute("preferRequest", Boolean.TRUE);
			model.addAttribute("riepilogoDanni", riepilogoDanni);
			return "danniFauna/modificaRiepilogoDanniFauna";
		}
		else
		{
			quadroIuffiEJB.updateRiepilogoDannoFauna(getIdProcedimentoOggetto(session),riepilogoDanni);
			return "redirect:../cuiuffi314v/index.do";
		}
	}
}
