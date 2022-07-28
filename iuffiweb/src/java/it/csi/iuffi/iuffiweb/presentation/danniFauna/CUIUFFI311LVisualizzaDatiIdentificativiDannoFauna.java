package it.csi.iuffi.iuffiweb.presentation.danniFauna;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoDaFaunaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-311-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi311l")
public class CUIUFFI311LVisualizzaDatiIdentificativiDannoFauna extends BaseController {

	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB = null;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpSession session, Model model) throws InternalUnexpectedException {
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		Integer numRow = quadroIuffiEJB.getCountDanniFauna(idProcedimentoOggetto);
		if(numRow!=0){
			DannoDaFaunaDTO datiIdentificativi = quadroIuffiEJB.getDatiIdentificativiDanniDaFauna(idProcedimentoOggetto);
			model.addAttribute("datiIdentificativi", datiIdentificativi);
			if(datiIdentificativi.getUrgenzaPerizia()!=null && datiIdentificativi.getUrgenzaPerizia()){
				model.addAttribute("isUrgente", "checked='checked'");
			}
		}else{
			model.addAttribute("datiIdentificativi", null);
		}
		
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		String ultimaModifica = getUltimaModifica(quadroIuffiEJB,
				logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(),
				logOperationOggettoQuadroDTO.getIdQuadroOggetto(), logOperationOggettoQuadroDTO.getIdBandoOggetto());
		model.addAttribute("ultimaModifica", ultimaModifica);
		
		return "danniFauna/visualizzaDatiIdentificativi";
	}

}
