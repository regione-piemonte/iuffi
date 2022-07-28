package it.csi.iuffi.iuffiweb.presentation.quadro.assicurazionicolture;




import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.assicurazionicolture.AssicurazioniColtureDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-306-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi306i")
public class CUIUFFI306IInserisciAssicurazioniColture extends CUIUFFI306BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB;
	
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, 
			  HttpServletRequest request,
			  Model model) throws InternalUnexpectedException
	  {
		  List<DecodificaDTO<String>> province = quadroIuffiEJB.getProvincie(IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE);
		  model.addAttribute("province",province);
		  model.addAttribute("idAssicurazioniColture",null);
		  model.addAttribute("cdu","CU-IUFFI-306-I");
		  return "assicurazionicolture/inserisciAssicurazioniColture";
	  }
	  
	  @RequestMapping(value="/index", method = RequestMethod.POST)
	  public String inserisci(
			  HttpSession session, 
			  HttpServletRequest request,
			  Model model) throws InternalUnexpectedException
	  {
		  return super.inserisci(session, request, model);
	  }
	
	  public void inserisci(
			  	HttpServletRequest request,
			  	HttpSession session,
			  	Model model,
			  	long idProcedimentoOggetto, AssicurazioniColtureDTO assicurazioniColture, LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
			    throws InternalUnexpectedException
	    {
		   quadroIuffiEJB.inserisciAssicurazioniColture(idProcedimentoOggetto,assicurazioniColture,logOperationOggettoQuadro);
	    }
}
