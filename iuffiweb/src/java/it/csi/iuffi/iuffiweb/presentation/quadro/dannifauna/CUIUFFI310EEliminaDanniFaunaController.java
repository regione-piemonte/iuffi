package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

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
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-310-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi310e")
public class CUIUFFI310EEliminaDanniFaunaController extends BaseController
{

	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB;
	
	@IsPopup
	@RequestMapping(value = "/index_{idDannoFauna}", method = RequestMethod.GET)
	public String index(
			Model model,
			HttpSession session,
			HttpServletRequest request,
			@PathVariable("idDannoFauna") long idDannoFauna) throws InternalUnexpectedException
	{	
		String parameters = "<input type=\"hidden\" name=\"idDannoFauna\" value=\"" + idDannoFauna + "\" />";
		String messaggio = "Sei sicuro di voler eliminare il danno?";
		model.addAttribute("elementi", parameters);
		setModelDialogWarning(model,messaggio,"../cuiuffi310e/confermaaggiorna.do");	
		return "dialog/confermaNoNote";
	}
	
	@IsPopup
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String indexPost(
			Model model,
			HttpSession session,
			HttpServletRequest request) throws InternalUnexpectedException
	{	
		String[] idsDannoFauna = request.getParameterValues("idDannoFauna");
		String parameters = "";
		
		for(String idDannoFauna : idsDannoFauna)
		{
			parameters = parameters + 
					"<input type=\"hidden\" name=\"idDannoFauna\" value=\"" + idDannoFauna + "\" />";
		}
		
		String messaggio = "Sei sicuro di voler eliminare i danni selezionati?";
		model.addAttribute("elementi", parameters);
		model.addAttribute("method","POST");
		setModelDialogWarning(model,messaggio,"../cuiuffi310e/confermaaggiorna.do");	
		return "dialog/confermaNoNote";
	}
	
	@RequestMapping(value = "/confermaaggiorna", method = RequestMethod.POST)
	public String confermaaggiorna(Model model, HttpSession session,  HttpServletRequest request) throws InternalUnexpectedException
	{	
		String[] idsDannoFaunaStr = request.getParameterValues("idDannoFauna");
		long[] idsDannoFauna = IuffiUtils.ARRAY.toLong(idsDannoFaunaStr);
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		quadroIuffiEJB.eliminaDanniFauna(getIdProcedimentoOggetto(session), idsDannoFauna, logOperationOggettoQuadroDTO);
		return "redirect:../cuiuffi310l/index.do";
	}
}
