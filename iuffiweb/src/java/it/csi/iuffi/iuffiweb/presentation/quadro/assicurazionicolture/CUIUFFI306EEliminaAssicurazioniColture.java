package it.csi.iuffi.iuffiweb.presentation.quadro.assicurazionicolture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-306-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi306e")
public class CUIUFFI306EEliminaAssicurazioniColture extends BaseController
{
	  private static final String FIELD_NAME_ID_NAME = "fieldNameIdName";
	  final String idsName = "chkIdAssicurazioniColture";
	  final String fieldNameIdName = "idsName";
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB;
	
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  @IsPopup
	  public String index(HttpSession session, 
			  HttpServletRequest request,
			  Model model) throws InternalUnexpectedException
	  {
		  long[] arrayIdAssicurazioniColture = IuffiUtils.ARRAY.toLong(request.getParameterValues(idsName));
		  if(arrayIdAssicurazioniColture!=null)
		  {
			  model.addAttribute("len", arrayIdAssicurazioniColture.length);
			  model.addAttribute("ids",arrayIdAssicurazioniColture);
		  }else
		  {
			  model.addAttribute("len", 0);
		  }
		  model.addAttribute(fieldNameIdName,idsName);
		  model.addAttribute(FIELD_NAME_ID_NAME, fieldNameIdName);
		  return "assicurazionicolture/popupEliminaAssicurazioniColture";
	  }
	  
	  @RequestMapping(value = "/index_{chkIdAssicurazioniColture}", method = RequestMethod.GET)
	  @IsPopup
	  public String index(HttpSession session, 
			  HttpServletRequest request,
			  Model model,
			  @PathVariable("chkIdAssicurazioniColture") long idAssicurazioniColture) throws InternalUnexpectedException
	  {
		  long[] arrayIdAssicurazioniColture = new long[]{idAssicurazioniColture};
		  model.addAttribute("ids", arrayIdAssicurazioniColture);
		  model.addAttribute("len", arrayIdAssicurazioniColture.length);
		  model.addAttribute(fieldNameIdName,idsName);
		  model.addAttribute(FIELD_NAME_ID_NAME, fieldNameIdName);
		  return "assicurazionicolture/popupEliminaAssicurazioniColture";
	  }
	  
	  @RequestMapping(value = "/elimina", method = RequestMethod.POST)
	  public String elimina(HttpSession session, 
			  HttpServletRequest request,
			  Model model) throws InternalUnexpectedException
	  {
		  String fieldName = request.getParameter(FIELD_NAME_ID_NAME);
		  long[] arrayIdAssicurazioniColture = IuffiUtils.ARRAY.toLong(request.getParameterValues(fieldName));
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  quadroIuffiEJB.eliminaAssicurazioniColture(idProcedimentoOggetto, arrayIdAssicurazioniColture);
		  return "dialog/success";
	  }
	
}
