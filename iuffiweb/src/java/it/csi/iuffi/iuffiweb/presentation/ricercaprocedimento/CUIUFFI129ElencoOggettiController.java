package it.csi.iuffi.iuffiweb.presentation.ricercaprocedimento;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.IterProcedimentoGruppoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.OggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.ProcedimentoGruppoVO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.presentation.interceptor.security.IuffiSecurityManager;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi129")
@IuffiSecurity(value = "CU-IUFFI-129", controllo = IuffiSecurity.Controllo.DEFAULT)
public class CUIUFFI129ElencoOggettiController extends BaseController {

    @Autowired
    private IRicercaEJB ricercaEJB = null;
    @Autowired
    private IQuadroEJB quadroEJB = null;

    @RequestMapping(value = "index_{idProcedimento}", method = RequestMethod.GET)
    public String index(Model model, HttpSession session, @PathVariable("idProcedimento") long idProcedimento)
	    throws InternalUnexpectedException, ApplicationException {

	// controlli abilitazioni
	String ret = verificaAbilitazioni(session, model, idProcedimento);

	if (!GenericValidator.isBlankOrNull(ret)) {
	    return ret;
	}
	UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
	boolean isBeneficiarioOCAA = IuffiUtils.PAPUASERV.isAttoreAbilitato(utenteAbilitazioni,
		IuffiConstants.PAPUA.ATTORE.BENEFICIARIO)
		|| IuffiUtils.PAPUASERV.isAttoreAbilitato(utenteAbilitazioni,
			IuffiConstants.PAPUA.ATTORE.INTERMEDIARIO_CAA);
	List<GruppoOggettoDTO> listGruppiOggetto = ricercaEJB.getElencoOggetti(idProcedimento,
		Arrays.asList(utenteAbilitazioni.getMacroCU()), isBeneficiarioOCAA,
		utenteAbilitazioni.getIdProcedimento());
	refreshTestataProcedimento(quadroEJB, session, idProcedimento);

	ProcedimentoOggetto po = quadroEJB.getProcedimentoOggettoByIdProcedimento(idProcedimento);
	IuffiFactory.setIdProcedimentoOggettoInSession(session, po.getIdProcedimentoOggetto(), po.getIdProcedimento());

	boolean notificheBloccanti = quadroEJB.checkNotifiche(idProcedimento, IuffiConstants.FLAG_NOTIFICA.BLOCCANTE,
		IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
	session.setAttribute("notificheBloccanti", notificheBloccanti);

	boolean notificheWarning = quadroEJB.checkNotifiche(idProcedimento, IuffiConstants.FLAG_NOTIFICA.WARNING,
		IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
	session.setAttribute("notificheWarning", notificheWarning);

	boolean notificheGravi = quadroEJB.checkNotifiche(idProcedimento, IuffiConstants.FLAG_NOTIFICA.GRAVE,
		IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
	session.setAttribute("notificheGravi", notificheGravi);

	long idStatoOggetto = getProcedimentoFromSession(session).getIdStatoOggetto();
	if (idStatoOggetto < 10 || idStatoOggetto > 90) {
	    model.addAttribute("procedimentoValido", Boolean.FALSE);
	} else {
	    model.addAttribute("procedimentoValido", Boolean.TRUE);
	}

	if (IuffiUtils.PAPUASERV.isMacroCUAbilitato((UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni"),
		IuffiConstants.MACROCDU.ESTRAZIONE_CAMPIONE)) {
	    boolean flagEstratto = false;
	    if (listGruppiOggetto != null && listGruppiOggetto.size() > 0) {
		for (GruppoOggettoDTO item : listGruppiOggetto) {
		    for (OggettoDTO ogg : item.getOggetti()) {
			String estratta = IuffiFactory
				.getMotivoEsclusioneProcedimentoOggetto(ogg.getIdProcedimentoOggetto());
			if (!GenericValidator.isBlankOrNull(estratta)) {
			    ogg.setMotivoEstrazione(estratta.toUpperCase());
			    flagEstratto = true;
			}
		    }
		}
		if (!flagEstratto) {
		    flagEstratto = false;
		    for (GruppoOggettoDTO item : listGruppiOggetto) {
			String estratta = IuffiFactory.getMotivoEsclusioneExPostProcedimento(item.getIdProcedimento());
			if (!GenericValidator.isBlankOrNull(estratta)) {
			    for (OggettoDTO ogg : item.getOggetti()) {
				ogg.setMotivoEstrazione(estratta.toUpperCase());
				flagEstratto = true;
				model.addAttribute("flagEstrattoExPost", Boolean.TRUE);
			    }
			}
		    }
		}
	    }
	    model.addAttribute("flagEstratto", flagEstratto);
	}

	// Se c'è quadro esito finale e c'è numero atto, il protocollo è quello
	// e il PO ha esito like %APP-%
	if (listGruppiOggetto != null && listGruppiOggetto.size() > 0) {
	    for (GruppoOggettoDTO item : listGruppiOggetto) {
		for (OggettoDTO ogg : item.getOggetti()) {
		    ProcedimentoOggetto poTmp = quadroEJB.getProcedimentoOggetto(ogg.getIdProcedimentoOggetto());
		    QuadroOggettoDTO quadro = poTmp.findQuadroByCU("CU-IUFFI-166-V");
		    if (quadro != null) {
			EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(ogg.getIdProcedimentoOggetto(),
				quadro.getIdQuadroOggetto());
			if (esito != null && !GenericValidator.isBlankOrNull(esito.getNumeroAtto())
				&& (poTmp.getCodiceEsito() != null && poTmp.getCodiceEsito().contains("APP-"))) {
			    ogg.setNumeroProtocollo(esito.getNumeroAtto());
			}
			if (esito != null && !GenericValidator.isBlankOrNull(esito.getDataAttoStr())
				&& (poTmp.getCodiceEsito() != null && poTmp.getCodiceEsito().contains("APP-"))) {
			    ogg.setDataTrasmissioneStr(esito.getDataAttoStr());
			}
		    }
		}
	    }
	}

	model.addAttribute("elenco", listGruppiOggetto);
	session.setAttribute("comeFromRicerca", "TRUE");
	IuffiFactory.removeIdProcedimentoOggettoFromSession(session);
	return "ricercaprocedimento/elencoOggetti";
    }

    @RequestMapping(value = "indexFromRegistroFatture_{idProcedimento}", method = RequestMethod.GET)
    public String indexFromRegistroFatture(Model model, HttpSession session,
	    @PathVariable("idProcedimento") long idProcedimento) throws InternalUnexpectedException, ApplicationException {
	IuffiFactory.setIdProcedimentoInSession(session, idProcedimento);
	model.addAttribute("isFromRegistroFatture", true);
	return index(model, session, idProcedimento);
    }

    @RequestMapping(value = "popupiter_{idProcedimento}_{codRaggruppamento}", method = RequestMethod.GET)
    public String popupiter(Model model, HttpSession session, @PathVariable("idProcedimento") long idProcedimento,
	    @PathVariable("codRaggruppamento") long codRaggruppamento) throws InternalUnexpectedException {
	List<IterProcedimentoGruppoDTO> elenco = ricercaEJB.getIterGruppoOggetto(idProcedimento, codRaggruppamento);
	List<ProcedimentoGruppoVO> elencoGruppi = ricercaEJB.getElencoProcedimentoGruppo(idProcedimento,
		codRaggruppamento);

	if (elenco == null && elencoGruppi == null) {
	    setModelDialogWarning(model, "Iter non presente");
	    return "dialog/soloMessaggio";
	}
	model.addAttribute("elenco", elenco);
	if (elencoGruppi != null && !elencoGruppi.isEmpty()) {
	    model.addAttribute("elencoGruppi", elencoGruppi);
	}
	return "ricercaprocedimento/popupIterGruppo";
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
	    } else if (utenteAbilitazioni.getRuolo().isUtenteTitolareCf()
		    || utenteAbilitazioni.getRuolo().isUtenteLegaleRappresentante()) {
		String errore = IuffiSecurityManager.validaAccessoAziendaAgricola(utenteAbilitazioni, idProcedimento,
			quadroEJB);
		if (errore != null) {
		    return returnErrorPage(model, errore);
		}
	    } else {
		if (!utenteAbilitazioni.getRuolo().isUtentePA() && !utenteAbilitazioni.getRuolo().isUtenteServiziAgri()
			&& !utenteAbilitazioni.getRuolo().isUtenteAssistenzaCsi()
			&& !utenteAbilitazioni.getRuolo().isUtenteOPR()
			&& !utenteAbilitazioni.getRuolo().getCodice().equals("PROFESSIONISTA@AGRICOLTURA")) {
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
