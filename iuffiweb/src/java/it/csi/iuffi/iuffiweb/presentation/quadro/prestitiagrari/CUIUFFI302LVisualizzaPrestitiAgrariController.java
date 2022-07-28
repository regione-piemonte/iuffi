package it.csi.iuffi.iuffiweb.presentation.quadro.prestitiagrari;

import java.util.ArrayList;
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
import it.csi.iuffi.iuffiweb.dto.prestitiagrari.PrestitiAgrariDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-302-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi302l")
public class CUIUFFI302LVisualizzaPrestitiAgrariController extends CUIUFFI302BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model, HttpServletRequest request) throws InternalUnexpectedException
	  {
		  LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		  String ultimaModifica = getUltimaModifica(quadroIuffiEJB, logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), logOperationOggettoQuadroDTO.getIdQuadroOggetto(),logOperationOggettoQuadroDTO.getIdBandoOggetto());
		  model.addAttribute("ultimaModifica",ultimaModifica);
		  model.addAttribute("cuNumber", cuNumber);
		  return "prestitiagrari/visualizzaPrestitiAgrari";
	  }
	  
	  @RequestMapping(value = "/visualizza_prestiti_agrari", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<PrestitiAgrariDTO> visualizzaPrestitiAgrari(HttpSession session, Model model, HttpServletRequest request) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  List<PrestitiAgrariDTO> listPrestitiAgrari = quadroIuffiEJB.getListPrestitiAgrari(idProcedimentoOggetto);
		  if(listPrestitiAgrari == null)
		  {
			  listPrestitiAgrari = new ArrayList<PrestitiAgrariDTO>();
		  }
		  return listPrestitiAgrari;
	  }
	
}
