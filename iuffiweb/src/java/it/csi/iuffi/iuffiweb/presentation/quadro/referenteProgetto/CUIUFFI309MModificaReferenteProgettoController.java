package it.csi.iuffi.iuffiweb.presentation.quadro.referenteProgetto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.ReferenteProgettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi309m")
@IuffiSecurity(value = "CU-IUFFI-309-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI309MModificaReferenteProgettoController extends BaseController{

	public static final String CU_NAME = "CU-IUFFI-309-M";
	
	  @Autowired
	  IQuadroIuffiEJB                 quadroIuffiEJB;
	  @Autowired
	  IQuadroIuffiEJB                 quadroEJB;
	  @Autowired
	  IInterventiEJB interventiEJB;

	  @RequestMapping("/index")
	  public String index(Model model, HttpSession session)
	      throws InternalUnexpectedException
	  { 
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		ReferenteProgettoDTO referenteProgetto = quadroIuffiEJB.getReferenteProgettoByIdProcedimentoOggetto(idProcedimentoOggetto);
		model.addAttribute("referente",referenteProgetto);
		model.addAttribute("prefReqValues",Boolean.FALSE);
		if(referenteProgetto!=null && referenteProgetto.getExtIstatComune()!=null){
			model.addAttribute("extIstatComune",referenteProgetto.getExtIstatComune());
		}else{
			model.addAttribute("extIstatComune","");
		}
		return "referenteProgetto/modificaReferenteProgetto";
	  }
	  
	  @IsPopup
	  @RequestMapping(value = "/popup_ricerca_comune", method = RequestMethod.GET)
	  public String popupRicercaComuniAltroistituto(Model model, HttpServletRequest request) throws InternalUnexpectedException
	  {
	    model.addAttribute("province", quadroEJB.getProvincie(null));
	    return "referenteProgetto/popupRicercaComune";
	  }
	  
	  @RequestMapping("/confermaModifica")
	  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request)
	      throws InternalUnexpectedException
	  { 
		//controllo campi obbligatori
		Errors errors = new Errors();
		
		String cognome = IuffiUtils.STRING.trim(request.getParameter("cognome"));
		String nome = IuffiUtils.STRING.trim(request.getParameter("nome"));
		String codiceFiscale = IuffiUtils.STRING.trim(request.getParameter("codiceFiscale"));
		String extIstatComune = IuffiUtils.STRING.trim(request.getParameter("extIstatComune"));
		String cap = IuffiUtils.STRING.trim(request.getParameter("cap"));
		String telefono = IuffiUtils.STRING.trim(request.getParameter("telefono"));
		String cellulare = IuffiUtils.STRING.trim(request.getParameter("cellulare"));
		String email = IuffiUtils.STRING.trim(request.getParameter("email"));


	   errors.validateMandatory(cognome, "cognome");
	   errors.validateMandatory(nome, "nome");
	   errors.controlloCf(codiceFiscale.toUpperCase(), "codiceFiscale");
	   errors.validateMandatory(extIstatComune, "comune");
	   errors.validateMandatoryFieldLength(cap, 5, 5, "cap");
	   
	    		
	    if (!errors.isEmpty())
	    {
	      model.addAttribute("extIstatComune",extIstatComune);
		  model.addAttribute("errors", errors);
	      model.addAttribute("prefReqValues", Boolean.TRUE);
	      return "referenteProgetto/modificaReferenteProgetto";
	    }else{
	    	//SALVO SU DB E RICARICO
	    	long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
	    	quadroIuffiEJB.insertOrUpdateReferenteProgettoByIdProcedimentoOggetto(idProcedimentoOggetto,
	    			nome, cognome, codiceFiscale, extIstatComune, cap, telefono, cellulare, email, getLogOperationOggettoQuadroDTO(session));
	    }
	    return "redirect:../cuiuffi309l/index.do";
	  }
	  
	  
}
