package it.csi.iuffi.iuffiweb.presentation.quadro.assicurazionicolture;




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
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.assicurazionicolture.AssicurazioniColtureDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-306-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi306l")
public class CUIUFFI306LVisualizzaAssicurazioniColture extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB;
	
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, 
			  HttpServletRequest request,
			  Model model) throws InternalUnexpectedException
	  {
		  LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		  String ultimaModifica = getUltimaModifica(quadroIuffiEJB, logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), logOperationOggettoQuadroDTO.getIdQuadroOggetto(),logOperationOggettoQuadroDTO.getIdBandoOggetto());
		  model.addAttribute("ultimaModifica", ultimaModifica);
		  return "assicurazionicolture/visualizzaAssicurazioniColture";
	  }
	  
	  @RequestMapping(value="/get_list_assicurazioni_colture", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<AssicurazioniColtureDTO> getListAssicurazioniColture(
			  HttpSession session, 
			  HttpServletRequest request,
			  Model model) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  return quadroIuffiEJB.getListAssicurazioniColture(idProcedimentoOggetto);
	  }
	  
	
}
