package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-310-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi310m")
public class CUIUFFI310MModificaDanniFauna extends CUIUFFI310BaseController
{
	@RequestMapping(value = "/index_{idDannoFauna}", method = RequestMethod.GET)
	public String indexIdDannoFauna(HttpServletRequest request, HttpSession session, Model model, @PathVariable("idDannoFauna") long idDannoFauna) throws InternalUnexpectedException
	{	
		return super.indexIdDannoFauna(request, session, model, idDannoFauna);
	}
	
	@RequestMapping(value = "/localizzazione_{idDannoFauna}", method = RequestMethod.GET)
	public String localizzazione(HttpServletRequest request, HttpSession session, Model model, @PathVariable("idDannoFauna") long idDannoFauna) throws InternalUnexpectedException
	{	
		model.addAttribute("idDannoFauna",idDannoFauna);
		model.addAttribute("indietro","./index_" + idDannoFauna + ".do");
		model.addAttribute("hideWizard", "true");
		common(request, session, model);
		return "danniFauna/elencoConduzioni";
	}
	
	@RequestMapping(value = "/index_{idDannoFauna}", method = RequestMethod.POST)
	public String confermaDettagli(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable("idDannoFauna") long idDannoFauna) throws InternalUnexpectedException
	{
		model.addAttribute("indietro","../cuiuffi310m/index_" + idDannoFauna + ".do");
		return super.confermaDettagli(request, session, model, idDannoFauna);
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String confermaDettagli(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
		long idDannoFauna = Long.parseLong(request.getParameter("idDannoFauna"));
		return confermaDettagli(request, session, model, idDannoFauna);
	}
	
	protected List<ParticelleDanniDTO> retrieveConduzioni(HttpSession session, Model model, HttpServletRequest request, long idDannoFauna) throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		List<DannoFaunaDTO> danni = quadroIuffiEJB.getListaDanniFaunaDettaglio(getIdProcedimentoOggetto(session), new long[]{idDannoFauna}, true);
		long[] idsUtilizzoDichiarato = new long[danni.size()];
		Map<Long,DannoFaunaDTO> mapDanni = new HashMap<Long,DannoFaunaDTO>();
		for(int i = 0; i<danni.size(); i++)
		{
			DannoFaunaDTO d = danni.get(i);
			idsUtilizzoDichiarato[i]=d.getIdUtilizzoDichiarato();
			mapDanni.put(d.getIdUtilizzoDichiarato(),d);
		}
		List<ParticelleDanniDTO> conduzioni = quadroIuffiEJB.getListConduzioniDannoGiaSelezionate(idProcedimentoOggetto, idsUtilizzoDichiarato, false);
		if(conduzioni == null)
		{
			conduzioni = new ArrayList<ParticelleDanniDTO>();
		}
		else
		{
			for(ParticelleDanniDTO conduzione : conduzioni)
			{
				DannoFaunaDTO d = mapDanni.get(conduzione.getIdUtilizzoDichiarato());
				conduzione.setSuperficieCoinvolta(d.getSuperficieDanneggiata());
				conduzione.setSuperficieCoinvoltaStr(IuffiUtils.FORMAT.formatDecimal4(d.getSuperficieDanneggiata()));
				conduzione.setColturaSecondaria(d.getFlagUtilizzoSec());
				conduzione.setIdUtilizzoRiscontrato(d.getIdUtilizzoRiscontrato());
			}
		}
		return conduzioni;
	}
	
	@Override
	public void addParametersToModel(Model model, HttpServletRequest request) 
	{
		String idDannoFauna = request.getParameter(FIELD_NAME_ID_DANNO_FAUNA);
		model.addAttribute(FIELD_NAME_ID_DANNO_FAUNA,idDannoFauna);
	}
	
}
