package it.csi.iuffi.iuffiweb.presentation.quadro.motoriagricoli;

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
import it.csi.iuffi.iuffiweb.dto.motoriagricoli.MotoriAgricoliDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-301", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi301")
public class CUIUFFI301MotoriAgricoli extends BaseController
{

	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB = null;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpSession session, Model model, HttpServletRequest request) throws InternalUnexpectedException
	{
		return "motoriagricoli/visualizzaMotoriAgricoli";
	}

	@RequestMapping(value = "/visualizza_motori_agricoli", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<MotoriAgricoliDTO> visualizzaMotoriAgricoli(HttpSession session, Model model,
			HttpServletRequest request) throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		return quadroIuffiEJB.getListMotoriAgricoli(idProcedimentoOggetto, getUtenteAbilitazioni(session).getIdProcedimento());
	}

}
