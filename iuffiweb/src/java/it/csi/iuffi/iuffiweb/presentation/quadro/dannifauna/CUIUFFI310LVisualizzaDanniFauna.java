package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-310-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi310l")
public class CUIUFFI310LVisualizzaDanniFauna extends BaseController
{
	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB;
	
	@Autowired
  private IQuadroEJB quadroEJB;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
		session.removeAttribute("danno");
	    session.removeAttribute(FiltroRicercaConduzioni.class.getName());
	    session.removeAttribute("conduzioni");
		
	    LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		String ultimaModifica = getUltimaModifica(quadroIuffiEJB,
				logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
				logOperationOggettoQuadroDTO.getIdQuadroOggetto(), logOperationOggettoQuadroDTO.getIdBandoOggetto());
		model.addAttribute("ultimaModifica", ultimaModifica);
		
		ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    model.addAttribute("isIstruttoria", isIstruttoria(po));
		
	    return "danniFauna/visualizzaDanniFauna";
	}

	@RequestMapping(value = "/get_elenco_danni_fauna", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<DannoFaunaDTO> getListaDanniFauna(HttpSession session, Model model) throws InternalUnexpectedException
	{
		return quadroIuffiEJB.getListaDanniFauna(getIdProcedimentoOggetto(session));
	}
	
	@RequestMapping(value = "danniFaunaExcel")
	public ModelAndView downloadExcel(Model model, HttpServletRequest request, HttpServletResponse response,
	    HttpSession session) throws InternalUnexpectedException
	{	  
	  List<DannoFaunaDTO> elenco = quadroIuffiEJB.getListaDanniFaunaDettaglio(getIdProcedimentoOggetto(session), null, false);
	  model.addAttribute("isIstruttoria",isIstruttoria(getProcedimentoOggettoFromSession(session)));
	  
	  TestataProcedimento testataProcedimento = (TestataProcedimento) session.getAttribute(TestataProcedimento.SESSION_NAME);
	  response.setContentType("application/xls");      
      response.setHeader("Content-Disposition", "attachment; filename=\"danniFauna_" + testataProcedimento.getCuaa() + ".xls\"");
      
	  return new ModelAndView("excelDanniFaunaView", "elenco", elenco);
	}
	
	
	@RequestMapping(value = "/getNewConsultaPianoGraficoUrl", method = RequestMethod.GET)
  @ResponseBody
  public String getNewConsultaPianoGraficoUrl(Model model, HttpSession session) throws InternalUnexpectedException, ApplicationException {

    long idProcedimentoOgg = getIdProcedimentoOggetto(session);
    
    ProcedimentoOggetto po = quadroEJB.getProcedimentoOggetto(idProcedimentoOgg);
    
    if(!"S".equals(po.getFlagValidazioneGrafica())) {
      return "Impossibile procedere in quanto la validazione non è di tipo grafico";
    }
    
    Map<String, String> pArrayParametri = new HashMap<String, String>();
    pArrayParametri.put("EDITING", "N");
    pArrayParametri.put("UTENTE", ""+getIdUtenteLogin(session));
    pArrayParametri.put("PROC", ""+IuffiConstants.IUFFIWEB.ID_DANNI_FAUNA);
    pArrayParametri.put("ATTO", ""+po.getIdAttoAmmi().longValue());
    Long token = (Long) quadroIuffiEJB.callPckSmrgaaUtilityGraficheScriviParametri(pArrayParametri);
      
    Map<String, String> parametri = quadroIuffiEJB.getParametri(new String[]
          { IuffiConstants.PARAMETRO.URL_NEW_WEBGRAFICO});


    return "http://localhost:9090/webgrafico/danfa/accedi.html"+"?token="+token;
  }
}
