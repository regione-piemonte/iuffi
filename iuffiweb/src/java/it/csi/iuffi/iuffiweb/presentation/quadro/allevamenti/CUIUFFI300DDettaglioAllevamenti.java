package it.csi.iuffi.iuffiweb.presentation.quadro.allevamenti;

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
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDettaglioPlvDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;

@Controller
@IuffiSecurity(value = "CU-IUFFI-300-D", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi300d")
public class CUIUFFI300DDettaglioAllevamenti extends BaseController
{
	@Autowired
	private IQuadroIuffiEJB quadroIuffiEJB = null;

	@RequestMapping(value = "/index_{idCategoriaAnimale}_{istatComune}", method = RequestMethod.GET)
	public String index(HttpSession session, Model model, 
			@PathVariable("idCategoriaAnimale") long idCategoriaAnimale,
			@PathVariable("istatComune") String istatComune) throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		AllevamentiDTO allevamento = quadroIuffiEJB.getDettaglioAllevamento(idProcedimentoOggetto, idCategoriaAnimale,istatComune);
		String ultimaModificaAllevamento="";
		if(allevamento.getDataUltimoAggiornamento() != null)
		{
			ultimaModificaAllevamento = allevamento.getDataUltimoAggiornamento()+ " " + getUtenteDescrizione(allevamento.getExtIdUtenteAggiornamento());
		}
		model.addAttribute("allevamento", allevamento);
		model.addAttribute("ultimaModifica", ultimaModificaAllevamento);
		model.addAttribute("idCategoriaAnimale", idCategoriaAnimale);
		model.addAttribute("istatComune", istatComune);
		return "allevamenti/dettaglioAllevamenti";
	}

	@RequestMapping(value = "/get_list_dettaglio_allevamenti_{idCategoriaAnimale}_{istatComune}.json", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<AllevamentiDettaglioPlvDTO> getListDettaglioAllevamenti(HttpSession session, Model model,
			@PathVariable("idCategoriaAnimale") long idCategoriaAnimale,
			@PathVariable("istatComune") String istatComune)
			throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		return quadroIuffiEJB.getListDettaglioAllevamenti(idProcedimentoOggetto, idCategoriaAnimale, istatComune);
	}
	
	private String getUtenteDescrizione(long idUtente) throws InternalUnexpectedException
	{
		try{
			long[] aIdUtente = new long[]{idUtente};
			UtenteLogin[] utenti = PapuaservProfilazioneServiceFactory
					.getRestServiceClient().findUtentiLoginByIdList(aIdUtente);
			return utenti[0].getDenominazione();
		}
		catch(Exception e)
		{
			throw new InternalUnexpectedException(e);
		}
	}
  }
