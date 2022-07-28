package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-310-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi310i")
public class CUIUFFI310IInserisciDanniFauna extends CUIUFFI310BaseController
{
	@RequestMapping(value = "/index_{idDannoFauna}", method = RequestMethod.GET)
	public String indexIdDannoFauna(HttpServletRequest request, HttpSession session, Model model, @PathVariable("idDannoFauna") long idDannoFauna) throws InternalUnexpectedException
	{	
		return super.indexIdDannoFauna(request, session, model, idDannoFauna);
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
		session.removeAttribute("conduzioni");
		common(request, session, model);
		return "danniFauna/inserisciDanniFauna";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String confermaDettagli(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
		String idDannoFaunaStr = request.getParameter("idDannoFauna");
		model.addAttribute("indietro","../cuiuffi310i/index_" + idDannoFaunaStr + ".do");
		Long idDannoFauna = null;
		if(idDannoFaunaStr != null && !idDannoFaunaStr.equals(""))
		{
			idDannoFauna = Long.parseLong(idDannoFaunaStr);
		}
		return super.confermaDettagli(request, session, model, idDannoFauna);
	}
	
	protected List<ParticelleDanniDTO> retrieveConduzioni(HttpSession session, 
			  Model model, HttpServletRequest request, long idDannoFauna) throws InternalUnexpectedException
	{
		List<ParticelleDanniDTO> conduzioni = new ArrayList<ParticelleDanniDTO>();
		return conduzioni;
	}
	
}


