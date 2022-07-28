package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;




import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.danniFauna.AccertamentoDannoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-312-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi312v")
public class CUIUFFI312VVisualizzaAccertamentoDanno extends BaseController
{
	  @Autowired
	  IQuadroIuffiEJB quadroIuffiEJB;
	  
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, 
			  HttpServletRequest request,
			  Model model) throws InternalUnexpectedException
	  {
	    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	    AccertamentoDannoDTO acdan = quadroIuffiEJB.getAccertamentoDanno(idProcedimentoOggetto);
	    if(acdan!=null) {
	      BigDecimal importoTotaleAccertato = quadroIuffiEJB.getImportoTotaleAccertato(idProcedimentoOggetto);
	      acdan.setImportoTotaleAccertato(importoTotaleAccertato);
	    }
	    model.addAttribute("acdan", acdan);
		  return "danniFauna/visualizzaAccertamentoDanno";
	  }
	  
}
