package it.csi.iuffi.iuffiweb.presentation;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.interceptor.security.IuffiSecurityManager;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/procedimento")
@IuffiSecurity(value = "N/A", controllo = IuffiSecurity.Controllo.NESSUNO)
public class ProcedimentoController extends BaseController {
    @Autowired
    IQuadroEJB quadroEJB = null;

    @RequestMapping(value = "seleziona_procedimento_{idProcedimento}")
    public String selezionaProcedimento(HttpSession session, Model model,
	    @PathVariable("idProcedimento") @NumberFormat(style = NumberFormat.Style.NUMBER) long idProcedimento)
		    throws InternalUnexpectedException, ApplicationException {
	cleanSession(session);
	String ret = verificaAbilitazioni(session, model, idProcedimento);

	if (!GenericValidator.isBlankOrNull(ret)) {
	    return ret;
	}

	session.setAttribute(TestataProcedimento.SESSION_NAME, quadroEJB.getTestataProcedimento(idProcedimento));
	// carico procedimento e lo metto in sessione
	IuffiFactory.setIdProcedimentoInSession(session, idProcedimento);
	IuffiFactory.removeIdProcedimentoOggettoFromSession(session);
	return "redirect:../procedimento/elenco_oggetti.do";
    }

    @RequestMapping(value = "visualizza_procedimento_{idProcedimento}")
    public String visualizzaProcedimento(HttpSession session, Model model,
	    @PathVariable("idProcedimento") @NumberFormat(style = NumberFormat.Style.NUMBER) long idProcedimento)
		    throws InternalUnexpectedException, ApplicationException {

	String ret = verificaAbilitazioni(session, model, idProcedimento);

	if (!GenericValidator.isBlankOrNull(ret)) {
	    return ret;
	}

	// carico procedimento e lo metto in sessione
	IuffiFactory.setIdProcedimentoInSession(session, idProcedimento);
	session.setAttribute(TestataProcedimento.SESSION_NAME, quadroEJB.getTestataProcedimento(idProcedimento));
	return "redirect:../procedimento/switch_" + idProcedimento + "_0.do";
    }

    private String verificaAbilitazioni(HttpSession session, Model model, long idProcedimento)
	    throws InternalUnexpectedException, ApplicationException {
	UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);

	if (utenteAbilitazioni.getRuolo().isUtenteIntermediario()) {
	    String errore = IuffiSecurityManager.validaAccessoIntermediario(utenteAbilitazioni, idProcedimento,
		    quadroEJB);
	    if (errore != null) {
		return returnErrorPage(model, errore);
	    }
	} else {
	    if (utenteAbilitazioni.getRuolo().getIsList() != null
		    && Arrays.asList(utenteAbilitazioni.getRuolo().getIsList()).contains("isUtenteProfessionista")) {
		String errore = IuffiSecurityManager.validaAccessoProfessionista(utenteAbilitazioni, idProcedimento,
			quadroEJB, getProcedimentoAgricoloFromSession(session));
		if (errore != null) {
		    return returnErrorPage(model, errore);
		}
	    }
	    else if (utenteAbilitazioni.getRuolo().isUtenteTitolareCf()
		    || utenteAbilitazioni.getRuolo().isUtenteLegaleRappresentante()) {
		String errore = IuffiSecurityManager.validaAccessoAziendaAgricola(utenteAbilitazioni, idProcedimento,
			quadroEJB);
		if (errore != null) {
		    return returnErrorPage(model, errore);
		}
	    } else {
		if (!utenteAbilitazioni.getRuolo().isUtentePA() && !utenteAbilitazioni.getRuolo().isUtenteServiziAgri()
			&& !utenteAbilitazioni.getRuolo().isUtenteAssistenzaCsi()
			&& !utenteAbilitazioni.getRuolo().isUtenteOPR()) {
		    return returnErrorPage(model, IuffiSecurityManager.ERRORE_UTENTE_CON_RUOLO_NON_AUTORIZZATO);
		}
	    }
	}
	return "";
    }

    protected String returnErrorPage(Model model, String message) {
	model.addAttribute("titolo", "Errore");
	model.addAttribute("messaggio", message);
	return "/errore/messaggio";
    }
    
}
