package it.csi.iuffi.iuffiweb.presentation.quadro.prestitiagrari;

import java.util.List;

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
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-302-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi302e")
public class CUIUFFI302EEliminaPrestitiAgrariController extends CUIUFFI302BaseController
{
	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB = null;
	private final String dlgName = "dlgEliminaScorte";

	@IsPopup
	@RequestMapping(value = "/conferma_elimina_prestito_{idPrestitiAgrari}", method = RequestMethod.GET)
	public String confermaEliminaPrestitoAgrario(
			Model model, 
			HttpServletRequest request,
			@PathVariable("idPrestitiAgrari") long idPrestitiAgrari) throws InternalUnexpectedException
	{	
		long[] arrayIdPrestitiAgrari = new long[1];
		arrayIdPrestitiAgrari[0] = idPrestitiAgrari;
		model.addAttribute("ids",arrayIdPrestitiAgrari);
		model.addAttribute("len",arrayIdPrestitiAgrari.length);
		model.addAttribute("dlgName",dlgName);
		return "prestitiagrari/confermaEliminaPrestiti";
	}
	
	@IsPopup
	@RequestMapping(value = "/conferma_elimina_prestiti", method = RequestMethod.GET)
	public String confermaEliminaPrestitiAgrari(
			Model model,
			HttpSession session,
			HttpServletRequest request
			) throws InternalUnexpectedException
	{
		String[] arrayIdPrestitiAgrari = request.getParameterValues("idPrestitiAgrari");
		if(arrayIdPrestitiAgrari == null)
		{
			arrayIdPrestitiAgrari = new String[0];
		}
		model.addAttribute("ids", arrayIdPrestitiAgrari);
		model.addAttribute("len", arrayIdPrestitiAgrari.length);
		model.addAttribute("dlgName",dlgName);
		return "prestitiagrari/confermaEliminaPrestiti";
	}
	
	@IsPopup
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String eliminaPrestitiAgrari(Model model, HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException
	{
		List<Long> listIdPrestitiAgrari = IuffiUtils.LIST.toListOfLong(request.getParameterValues("idPrestitiAgrari"));
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		quadroIuffiEJB.eliminaPrestitiAgrari(listIdPrestitiAgrari, logOperationOggettoQuadroDTO,idProcedimentoOggetto);
		return "dialog/success";
	}
}