package it.csi.iuffi.iuffiweb.presentation.quadro.referenteProgetto;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.ReferenteProgettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi309l")
@IuffiSecurity(value = "CU-IUFFI-309-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI309LVisualizzaReferenteProgettoController extends BaseController{

	 public static final String CU_NAME = "CU-IUFFI-309-L";

	  @Autowired
	  IQuadroIuffiEJB                 quadroIuffiEJB;

	  @RequestMapping("/index")
	  public String index(Model model, HttpSession session)
	      throws InternalUnexpectedException
	  { 
		//recupero se esiste il referente del progetto
		ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(session);
		long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
		long idBandoOggetto = procedimentoOggetto.getIdBandoOggetto();
		QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU("CU-IUFFI-309-L");
		ReferenteProgettoDTO referenteProgetto = quadroIuffiEJB.getReferenteProgettoByIdProcedimentoOggetto(idProcedimentoOggetto);
		if(referenteProgetto==null){
			model.addAttribute("referenteAssegnato", Boolean.FALSE);
		}else{
			model.addAttribute("referenteAssegnato", Boolean.TRUE);
		}
		model.addAttribute("ultimaModifica",
		          getUltimaModifica(quadroIuffiEJB,
		              idProcedimentoOggetto,
		              quadro.getIdQuadroOggetto(),
		              idBandoOggetto));
		model.addAttribute("referente", referenteProgetto);
	    return "referenteProgetto/visualizzaReferenteProgetto";
	  }
	  
	
	  
}
