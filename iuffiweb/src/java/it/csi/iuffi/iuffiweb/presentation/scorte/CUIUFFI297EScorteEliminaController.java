package it.csi.iuffi.iuffiweb.presentation.scorte;

import java.util.ArrayList;
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
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-297-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi297e")
public class CUIUFFI297EScorteEliminaController extends BaseController
{
	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB = null;

	@RequestMapping(value = "/conferma_elimina_scorta_{idScortaMagazzino}", method = RequestMethod.GET)
	public String confermaEliminaScorta(
			Model model, 
			HttpSession session,
			HttpServletRequest request,
			@PathVariable("idScortaMagazzino") long idScortaMagazzino) throws InternalUnexpectedException
	{	
		long[] arrayIdScortaMagazzino = new long[]{idScortaMagazzino};
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		return confermaEliminaGenerico(model, arrayIdScortaMagazzino, idProcedimentoOggetto);
	}
	
	@RequestMapping(value = "/conferma_elimina_scorte", method = RequestMethod.POST)
	public String confermaEliminaScorte(
			Model model,
			HttpSession session,
			HttpServletRequest request
			) throws InternalUnexpectedException
	{
		long[] arrayIdScortaMagazzino = IuffiUtils.ARRAY.toLong(request.getParameterValues("idScortaMagazzino"));
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		return confermaEliminaGenerico(model, arrayIdScortaMagazzino, idProcedimentoOggetto);
	}

	private String confermaEliminaGenerico(Model model, long[] arrayIdScortaMagazzino, long idProcedimentoOggetto)
			throws InternalUnexpectedException
	{
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
		Long nInterventiDanniScorte = quadroIuffiEJB.getNInterventiAssociatiDanniScorte(idProcedimentoOggetto, listIdScortaMagazzino);

		model.addAttribute("ids", arrayIdScortaMagazzino);
		model.addAttribute("len", arrayIdScortaMagazzino.length);
		model.addAttribute("nDanniScorte",nDanniScorte);
		model.addAttribute("nInterventi", nInterventiDanniScorte);
		return "scorte/confermaEliminaScorte";
	}
	
	@IsPopup
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String eliminaIntervento(Model model, HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException, ApplicationException
	{
		List<Long> listIdScortaMagazzino = IuffiUtils.LIST.toListOfLong(request.getParameterValues("idScortaMagazzino"));
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		long nScorteEliminate = quadroIuffiEJB.eliminaScorte(listIdScortaMagazzino, logOperationOggettoQuadroDTO,idProcedimentoOggetto);
		if(nScorteEliminate == IuffiConstants.ERRORI.ELIMINAZIONE_SCORTE_CON_DANNI_CON_INTERVENTI)
		{
			return "scorte/failureScorteDanniInterventi";
		}
		return "dialog/success";
	}
}
