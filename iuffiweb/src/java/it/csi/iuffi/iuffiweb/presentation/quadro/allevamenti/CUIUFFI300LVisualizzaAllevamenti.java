package it.csi.iuffi.iuffiweb.presentation.quadro.allevamenti;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-300-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi300l")
public class CUIUFFI300LVisualizzaAllevamenti extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model) throws InternalUnexpectedException
	  {
		  List<String> tableNamesToRemove = new ArrayList<String>();
		  tableNamesToRemove.add("tableListPlvZootecnicaDettaglio");
		  tableNamesToRemove.add("tableDettaglioAllevamentiPlv");
		  cleanTableMapsInSession(session, tableNamesToRemove);
		  return "allevamenti/visualizzaAllevamenti";
	  }
	  
	  @RequestMapping(value = "/get_list_allevamenti.json", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<AllevamentiDTO> getListAllevamenti(HttpSession session, Model model) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  List<AllevamentiDTO> lista =  quadroIuffiEJB.getListAllevamenti(idProcedimentoOggetto);
		  return lista;
	  }
	  
}
