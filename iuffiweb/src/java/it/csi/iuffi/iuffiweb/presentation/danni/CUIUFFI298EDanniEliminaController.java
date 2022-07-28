package it.csi.iuffi.iuffiweb.presentation.danni;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-298-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi298e")
public class CUIUFFI298EDanniEliminaController extends CUIUFFI298DanniBaseController
{

	@IsPopup
	@RequestMapping(value = "/conferma_elimina_danno_{idDannoAtm}", method = RequestMethod.GET)
	public String confermaEliminaInterventi(
			Model model,
			HttpSession session,
			HttpServletRequest request,
			@PathVariable("idDannoAtm") long idDannoAtm) throws InternalUnexpectedException
	{	
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		long[] arrayIdDannoAtm = new long[1];
		arrayIdDannoAtm[0] = idDannoAtm;
		long nInterventi = quadroIuffiEJB.getNInterventiAssociatiDanni(idProcedimentoOggetto, new long[]{idDannoAtm});
		model.addAttribute("ids",arrayIdDannoAtm);
		model.addAttribute("len",arrayIdDannoAtm.length);
		model.addAttribute("nInterventi", nInterventi);
		return "danni/confermaEliminaDanni";
	}
	
	@IsPopup
	@RequestMapping(value = "/conferma_elimina_danni", method = RequestMethod.GET)
	public String confermaEliminaInterventi(
			Model model,
			HttpSession session,
			HttpServletRequest request
			) throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		String[] arrayIdDannoAtmString = request.getParameterValues("idDannoAtm");
		if(arrayIdDannoAtmString == null)
		{
			arrayIdDannoAtmString = new String[0];
		}
		long[] arrayIdDannoAtm = IuffiUtils.ARRAY.toLong(arrayIdDannoAtmString);
		long nInterventi = quadroIuffiEJB.getNInterventiAssociatiDanni(idProcedimentoOggetto, arrayIdDannoAtm);
		model.addAttribute("ids", arrayIdDannoAtm);
		model.addAttribute("len", arrayIdDannoAtm.length);
		model.addAttribute("nInterventi",nInterventi);
		return "danni/confermaEliminaDanni";
	}
	
	@IsPopup
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String eliminaIntervento(Model model, HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException, ApplicationException
	{
		List<Long> listIdDannoAtm = IuffiUtils.LIST.toListOfLong(request.getParameterValues("idDannoAtm"));
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		long nDanniEliminati = quadroIuffiEJB.eliminaDanni(listIdDannoAtm, logOperationOggettoQuadroDTO,idProcedimentoOggetto);
		if(nDanniEliminati == IuffiConstants.ERRORI.ELIMINAZIONE_DANNI_CON_INTERVENTI)
		{
			model.addAttribute("azione", "eliminare");
			return "danni/failureInterventiDanni";
		}
		return "dialog/success";
	}
}
