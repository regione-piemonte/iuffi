package it.csi.iuffi.iuffiweb.presentation.quadro.prestitiagrari;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.prestitiagrari.PrestitiAgrariDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-302-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi302i")
public class CUIUFFI302IInserisciPrestitiAgrariController extends CUIUFFI302BaseController
{
	
	@Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model, HttpServletRequest request) throws InternalUnexpectedException
	  {
		  return "prestitiagrari/inserisciPrestitiAgrari";
	  }
	  
	  
	@RequestMapping(value = "/inserisci_prestito_conferma", method = RequestMethod.POST)
	public String inserisciPrestitoConferma(HttpSession session, Model model, HttpServletRequest request)
			throws InternalUnexpectedException
	{
		Errors errors = new Errors();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dateMin=null,dateMax=null;
		try
		{
			dateMin = sdf.parse("01/01/1900");
			dateMax = sdf.parse("01/01/2200");
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		String fieldFinalitaPrestito = request.getParameter(fieldNameFinalitaPrestito);
		String fieldIstitutoErogante = request.getParameter(fieldNameIstitutoErogante);
		String fieldImporto = request.getParameter(fieldNameImporto);
		String fieldDataScadenza = request.getParameter(fieldNameDataScadenza);
		errors.validateMandatoryFieldLength(fieldFinalitaPrestito, 1, 100, fieldNameFinalitaPrestito, true);
		errors.validateMandatoryFieldLength(fieldIstitutoErogante, 1, 100, fieldNameIstitutoErogante, false);
		BigDecimal importo = errors.validateMandatoryBigDecimalInRange(fieldImporto, fieldNameImporto, 2, new BigDecimal("0.01"), new BigDecimal("99999999.99"));
		Date dataScadenza = errors.validateMandatoryDateInRange(fieldDataScadenza, fieldNameDataScadenza, dateMin, dateMax, true, true);
		
		if(errors.addToModelIfNotEmpty(model))
		{
			model.addAttribute("preferRequest", Boolean.TRUE);
			return "prestitiagrari/inserisciPrestitiAgrari";
		}
		PrestitiAgrariDTO prestito = new PrestitiAgrariDTO();
		prestito.setFinalitaPrestito(fieldFinalitaPrestito);
		prestito.setIstitutoErogante(fieldIstitutoErogante);
		prestito.setDataScadenza(dataScadenza);
		prestito.setImporto(importo);
		prestito.setIdProcedimentoOggetto(getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto());
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		quadroIuffiEJB.inserisciPrestitoAgrario(prestito,logOperationOggettoQuadroDTO);
		return "redirect:../cuiuffi302l/index.do";
	}
}
