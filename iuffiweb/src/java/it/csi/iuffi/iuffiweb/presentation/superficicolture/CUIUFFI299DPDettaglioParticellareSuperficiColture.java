package it.csi.iuffi.iuffiweb.presentation.superficicolture;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioParticellareDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-299-DP", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi299dp")
public class CUIUFFI299DPDettaglioParticellareSuperficiColture extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  @RequestMapping(value = "/index_{idSuperficieColtura}", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model, @PathVariable("idSuperficieColtura") long idSuperficieColtura) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  SuperficiColtureDettaglioDTO superficiColturaDettaglio = quadroIuffiEJB.getSuperficiColtureDettaglio(idProcedimentoOggetto,idSuperficieColtura);
		  model.addAttribute("superficiColturaDettaglio",superficiColturaDettaglio);
		  model.addAttribute("idSuperficieColtura", idSuperficieColtura);
		  return "superficicolture/dettaglioParticellareSuperficiColture";
	  }
	  
	  
	  @RequestMapping(value = "/get_list_dettaglio_particellare_superfici_colture_{idSuperficieColtura}.json", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<SuperficiColtureDettaglioParticellareDTO> getListDettaglioParticellareSuperficiColture(HttpSession session, Model model,
			  @PathVariable("idSuperficieColtura") long idSuperficieColtura) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  return quadroIuffiEJB.getListDettaglioParticellareSuperficiColture(idProcedimentoOggetto, idSuperficieColtura);
	  }
}
