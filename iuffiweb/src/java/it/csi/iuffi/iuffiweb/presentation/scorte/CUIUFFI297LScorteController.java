package it.csi.iuffi.iuffiweb.presentation.scorte;

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
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-297-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi297l")
public class CUIUFFI297LScorteController extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;

	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model) throws InternalUnexpectedException
	  {
	      long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
	      long idStatoProcedimento = quadroIuffiEJB.getIdStatoProcedimento(idProcedimentoOggetto);
	      model.addAttribute("idProcedimentoOggetto",idProcedimentoOggetto);
	      model.addAttribute("idStatoProcedimento", idStatoProcedimento);
	      clearCommonInSession(session);
		  return "scorte/visualizzaScorte";
	  }
	  
	  @RequestMapping(value = "/get_elenco_scorte", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<ScorteDTO> getListaScorteByProcedimentoOggetto(
			  HttpSession session, 
			  Model model)  throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  return quadroIuffiEJB.getListaScorteByProcedimentoOggetto(idProcedimentoOggetto);
	  }
	  
	  @RequestMapping(value = "/visualizza_scorte_popup", method = RequestMethod.GET)
	  public String visualizzaScortePopup(HttpSession session, Model model) throws InternalUnexpectedException
	  {
		  this.index(session, model);
		  return "scorte/visualizzaScortePopup";
	  }
	  
	 
	  @RequestMapping(value = "/get_n_danni_scorte", method = RequestMethod.POST, produces = "text/plain")
	  @ResponseBody
	  public String getNDanniScorte(HttpSession session, Model model, HttpServletRequest request) throws InternalUnexpectedException
	  {
		  String[] arrayIdScortaMagazzinoString = request.getParameterValues("idScortaMagazzino");
		  long[] arrayIdScortaMagazzino = IuffiUtils.ARRAY.toLong(arrayIdScortaMagazzinoString);
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  return quadroIuffiEJB.getNDanniScorte(idProcedimentoOggetto, arrayIdScortaMagazzino).toString();
	  }
	  
}
