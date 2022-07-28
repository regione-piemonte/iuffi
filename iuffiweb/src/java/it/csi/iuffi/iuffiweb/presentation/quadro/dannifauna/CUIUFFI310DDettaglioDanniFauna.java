package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-310-D", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi310d")
public class CUIUFFI310DDettaglioDanniFauna extends BaseController
{
	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB;

	@RequestMapping(value = "/index_{idDannoFauna}", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable("idDannoFauna") long idDannoFauna) throws InternalUnexpectedException
	{
		List<DannoFaunaDTO> danni = quadroIuffiEJB.getListaDanniFauna(getIdProcedimentoOggetto(session), new long[]{idDannoFauna});
		model.addAttribute("ultimaModifica", IuffiUtils.DATE.formatDateTime(danni.get(0).getDataAggiornamento()) + "  " + danni.get(0).getDescUtenteAggiornamento()); 
		model.addAttribute("danno",danni.get(0));
		
		ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
		model.addAttribute("isIstruttoria", isIstruttoria(po));

		return "danniFauna/dettaglioDanniFauna";
	}
	
	@RequestMapping(value = "localizzazione_danno_fauna_{idDannoFauna}", method = RequestMethod.GET)
	@ResponseBody
	public List<DannoFaunaDTO> localizzazioneDannoFauna(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable("idDannoFauna") long idDannoFauna) throws InternalUnexpectedException
	{
		List<DannoFaunaDTO> danni = quadroIuffiEJB.getListaDanniFaunaDettaglio(getIdProcedimentoOggetto(session), new long[]{idDannoFauna}, true);
		return danni;
	}
}
