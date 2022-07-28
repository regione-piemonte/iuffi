package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.RiepilogoDannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-314-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi314v")
public class CUIUFFI314VVisualizzaRiepilogoDanniFauna extends BaseController
{
	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
	    LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		
	    String ultimaModifica = getUltimaModifica(quadroIuffiEJB,
				logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
				logOperationOggettoQuadroDTO.getIdQuadroOggetto(), logOperationOggettoQuadroDTO.getIdBandoOggetto());
		model.addAttribute("ultimaModifica", ultimaModifica);

		RiepilogoDannoFaunaDTO totRiepilogo = quadroIuffiEJB.getTotaliRiepilogoDanniFauna(logOperationOggettoQuadroDTO.getIdProcedimentoOggetto());
		model.addAttribute("totRiepilogo", totRiepilogo);
		
	    return "danniFauna/visualizzaRiepilogoDanniFauna";
	}

	@RequestMapping(value = "/get_elenco_riepilogo_danni_fauna", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<RiepilogoDannoFaunaDTO> getListaDanniFauna(HttpSession session, Model model) throws InternalUnexpectedException
	{
		return quadroIuffiEJB.getListaRiepilogoDanniFaunaDettaglio(getIdProcedimentoOggetto(session), null);
	}
	
	@RequestMapping(value = "riepilogoDanniFaunaExcel")
	public ModelAndView downloadExcel(Model model, HttpServletRequest request, HttpServletResponse response,
	    HttpSession session) throws InternalUnexpectedException
	{
	  
	  model.addAttribute("isIstruttoria", isIstruttoria(getProcedimentoOggettoFromSession(session)));
	  
	  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	  List<RiepilogoDannoFaunaDTO> elenco = quadroIuffiEJB.getListaRiepilogoDanniFaunaDettaglio(idProcedimentoOggetto, null);
	  List<DannoFaunaDTO> danniFauna = quadroIuffiEJB.getListaDanniFaunaDettaglio(idProcedimentoOggetto, null, false);
	  
	  TestataProcedimento testataProcedimento = (TestataProcedimento) session.getAttribute(TestataProcedimento.SESSION_NAME);
	  response.setContentType("application/xls");      
      response.setHeader("Content-Disposition", "attachment; filename=\"riepilogoDanniFauna_" + testataProcedimento.getCuaa() + ".xls\"");
	  
      model.addAttribute("danniFauna",danniFauna);
	  return new ModelAndView("excelRiepilogoDanniFaunaView", "elenco", elenco);
	}
}
