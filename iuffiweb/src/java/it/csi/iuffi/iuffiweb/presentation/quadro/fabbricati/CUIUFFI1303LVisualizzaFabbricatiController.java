package it.csi.iuffi.iuffiweb.presentation.quadro.fabbricati;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.fabbricati.FabbricatiDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-1303-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi1303l")
public class CUIUFFI1303LVisualizzaFabbricatiController extends BaseController
{
	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB = null;

	private String cuNumber = "1303";

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpSession session, Model model, HttpServletRequest request) throws InternalUnexpectedException
	{
		model.addAttribute("cuNumber", cuNumber);
		return "fabbricati/visualizzaFabbricati";
	}

	@RequestMapping(value = "/visualizza_fabbricati", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<FabbricatiDTO> visualizzaFabbricati(HttpSession session, Model model, HttpServletRequest request)
			throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		return quadroIuffiEJB.getListFabbricati(idProcedimentoOggetto, getUtenteAbilitazioni(session).getIdProcedimento());
	}

}
