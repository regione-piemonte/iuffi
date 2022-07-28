package it.csi.iuffi.iuffiweb.presentation.quadro.fabbricati;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.fabbricati.FabbricatiDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-1303-D", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi1303d")
public class CUIUFFI1303DDettaglioFabbricatiController extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  private String cuNumber = "1303";
	  
	  @RequestMapping(value = "/index_{idFabbricato}", method = RequestMethod.GET)
	  public String index(
			  		HttpSession session, 
			  		Model model, 
			  		@PathVariable(value = "idFabbricato") long idFabbricato) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  FabbricatiDTO fabbricato = quadroIuffiEJB.getFabbricato(idProcedimentoOggetto,idFabbricato, getUtenteAbilitazioni(session).getIdProcedimento());
		  model.addAttribute("fabbricato", fabbricato);
		  model.addAttribute("cuNumber", cuNumber);
		  return "fabbricati/dettagliFabbricato";
	  }
	  

}
