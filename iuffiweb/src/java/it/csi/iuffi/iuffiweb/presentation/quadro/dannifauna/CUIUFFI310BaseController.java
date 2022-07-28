package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.ParticelleFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Localizza;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public abstract class CUIUFFI310BaseController extends Localizza
{
	protected static final String FIELD_NAME_QUANTITA = "quantita";
	protected static final String FIELD_NAME_IMPORTO = "importoDannoEffettivo";
	protected static final String FIELD_NAME_ULTERIORI_INFORMAZIONI = "ulterioriInformazioni";
	protected static final String FIELD_NAME_ID_DANNO_SPECIE = "idDannoSpecie";
	protected static final String FIELD_NAME_ID_SPECIE_FAUNA = "idSpecieFauna";
	protected static final String FIELD_NAME_HIDE_WIZARD = "hideWizard";
	protected static final String FIELD_NAME_ID_DANNO_FAUNA = "idDannoFauna";
	
	@Autowired
	protected IQuadroIuffiEJB quadroIuffiEJB;


	
	public String indexIdDannoFauna(HttpServletRequest request, HttpSession session, Model model, long idDannoFauna) throws InternalUnexpectedException
	{	
		session.removeAttribute("conduzioni");
		common(request, session, model);
		model.addAttribute("idDannoFauna", idDannoFauna);
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		DannoFaunaDTO danno = quadroIuffiEJB.getListaDanniFauna(idProcedimentoOggetto, new long[]{idDannoFauna}).get(0);
		if(danno == null)
		{
			danno = quadroIuffiEJB.getListaDanniFauna(getIdProcedimentoOggetto(session), new long[]{idDannoFauna}).get(0);
		}
		
		List<DecodificaDTO<Long>> tipiDanno = getListaTipoDannoFauna(request,session,model,danno.getIdSpecieFauna());
		model.addAttribute("tipiDanno",tipiDanno);
		model.addAttribute("danno",danno);
		return "danniFauna/inserisciDanniFauna";
	}
	
	protected void common(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		model.addAttribute("specie",quadroIuffiEJB.getListaSpecieFauna());
		model.addAttribute("provincie",quadroIuffiEJB.getListProvinciaConTerreniInConduzione(idProcedimentoOggetto, IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE));
		model.addAttribute("comuneDisabled", Boolean.TRUE);
		model.addAttribute("unitaMisura",quadroIuffiEJB.getListUnitaDiMisura());
		model.addAttribute("sezioneDisabled", Boolean.TRUE);
		model.addAttribute("utilizzi",quadroIuffiEJB.getDecodificheUtilizzo());
		ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    model.addAttribute("isIstruttoria",isIstruttoria(po));
    
		if(session.getAttribute("danno") != null)
		{
			DannoFaunaDTO danno = (DannoFaunaDTO)session.getAttribute("danno");
			List<DecodificaDTO<Long>> tipiDanno = getListaTipoDannoFauna(request,session,model,danno.getIdSpecieFauna());
			model.addAttribute("danno",danno);
			model.addAttribute("tipiDanno",tipiDanno);
		}
	}
	
	public String confermaDettagli(HttpServletRequest request, HttpSession session, Model model, Long idDannoFauna) throws InternalUnexpectedException
	{
		Errors errors = new Errors();
		String idSpecieFaunaStr = request.getParameter(FIELD_NAME_ID_SPECIE_FAUNA);
		String idDannoSpecieStr = request.getParameter(FIELD_NAME_ID_DANNO_SPECIE);
		String ulterioriInformazioni = request.getParameter(FIELD_NAME_ULTERIORI_INFORMAZIONI);
		String quantitaStr = request.getParameter(FIELD_NAME_QUANTITA);
		String hideWizard = request.getParameter(FIELD_NAME_HIDE_WIZARD);
		
		Long idSpecieFauna = errors.validateMandatoryLong(idSpecieFaunaStr, FIELD_NAME_ID_SPECIE_FAUNA);
		Long idDannoSpecie = errors.validateMandatoryLong(idDannoSpecieStr, FIELD_NAME_ID_DANNO_SPECIE);
		
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		
		errors.validateFieldLength(ulterioriInformazioni, FIELD_NAME_ULTERIORI_INFORMAZIONI, 0, 4000);
		BigDecimal quantita = errors.validateMandatoryBigDecimalInRange(quantitaStr, FIELD_NAME_QUANTITA, 4, new BigDecimal("0.0001"), new BigDecimal("99999.9999"));

		BigDecimal importo = null;
	    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);

	    if(errors.addToModelIfNotEmpty(model))
		{
			common(request, session, model);
			if(idSpecieFauna != null)
			{
				List<DecodificaDTO<Long>> tipiDanno = quadroIuffiEJB.getListaTipoDannoFauna(idSpecieFauna);
				model.addAttribute("tipiDanno",tipiDanno);
				model.addAttribute("preferRequest", Boolean.TRUE);
				model.addAttribute("idDannoFauna",idDannoFauna);
			}
		}
		else
		{
			UtenteAbilitazioni utente = getUtenteAbilitazioni(session);
			DannoFaunaDTO danno = new DannoFaunaDTO();
			if(idDannoFauna != null)
			{
				danno.setIdDannoFauna(idDannoFauna);
			}
			danno.setIdSpecieFauna(idSpecieFauna);
			danno.setIdDannoSpecie(idDannoSpecie);
			danno.setUlterioriInformazioni(ulterioriInformazioni);
			danno.setQuantita(quantita);
			danno.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
			if(idDannoFauna != null)
			{
				danno.setIdDannoFauna(idDannoFauna);
			}
			else
			{
				danno.setIdDannoFauna(null);
			}
			LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
			idDannoFauna = quadroIuffiEJB.memorizzaDannoFauna(idProcedimentoOggetto, danno, logOperationOggettoQuadroDTO);
			model.addAttribute("idDannoFauna",idDannoFauna);
			model.addAttribute("indietro","./index_" + idDannoFauna + ".do");
			model.addAttribute(FIELD_NAME_HIDE_WIZARD, hideWizard);
			common(request, session, model);
			return "danniFauna/elencoConduzioni";
		}
		return "danniFauna/inserisciDanniFauna";
	}
	
	@RequestMapping(value = "/conferma", method = RequestMethod.POST)
	public String conferma(HttpServletRequest request, HttpSession session, Model model) throws InternalUnexpectedException
	{
		String returnValue="";
		Errors errors = new Errors();
		UtenteAbilitazioni utente = getUtenteAbilitazioni(session);
		String[] idsUtilizzoDichiaratoStr = request.getParameterValues("idUtilizzoDichiarato");
		String hideWizard = request.getParameter(FIELD_NAME_HIDE_WIZARD);
		long idDannoFauna = Long.parseLong(request.getParameter(FIELD_NAME_ID_DANNO_FAUNA));
		
		boolean isIstruttoria = isIstruttoria(getProcedimentoOggettoFromSession(session));

		model.addAttribute(FIELD_NAME_ID_DANNO_FAUNA, idDannoFauna);
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		
		if(idsUtilizzoDichiaratoStr == null || idsUtilizzoDichiaratoStr.length == 0)
		{
			model.addAttribute("errorNoConduzioni", "true");
			model.addAttribute(FIELD_NAME_HIDE_WIZARD, hideWizard);
			common(request, session, model);
			returnValue = "danniFauna/elencoConduzioni";
		}
		else
		{
			
			long[] idsUtilizzoDichiarato = IuffiUtils.ARRAY.toLong(idsUtilizzoDichiaratoStr);
			;
			DannoFaunaDTO danno = quadroIuffiEJB.getListaDanniFauna(idProcedimentoOggetto, new long[]{idDannoFauna}).get(0);
			danno.setExtIdUtenteAggiornamento(utente.getIdUtenteLogin());
			//conduzioni associate al procedimento oggetto
			List<ParticelleDanniDTO> conduzioniEsistenti = quadroIuffiEJB.getListConduzioniDannoGiaSelezionate(idProcedimentoOggetto, idsUtilizzoDichiarato, false);
			Map<Long,ParticelleDanniDTO> mapConduzioni = new HashMap<Long,ParticelleDanniDTO>();
			idsUtilizzoDichiarato = new long[conduzioniEsistenti.size()];
			int i=0;
			for(ParticelleDanniDTO conduzione : conduzioniEsistenti)
			{
				mapConduzioni.put(conduzione.getIdUtilizzoDichiarato(),conduzione);
				idsUtilizzoDichiarato[i]=conduzione.getIdUtilizzoDichiarato();
				i++;
			}
			List<ParticelleFaunaDTO> particelleFauna = new ArrayList<ParticelleFaunaDTO>();
			for(long idUtilizzoDichiarato : mapConduzioni.keySet())
			{
				ParticelleDanniDTO conduzione = mapConduzioni.get(idUtilizzoDichiarato);
				BigDecimal superficieUtilizzata = conduzione.getSuperficieUtilizzata();
				superficieUtilizzata = IuffiUtils.NUMBERS.nvl(superficieUtilizzata, new BigDecimal("999999.9999"));
				
				String fieldNameSuperficieCoinvolta = "superficieCoinvolta_" + idUtilizzoDichiarato;
				String superficieCoinvoltaStr = request.getParameter(fieldNameSuperficieCoinvolta);
				BigDecimal superficieCoinvolta = errors.validateMandatoryBigDecimalInRange(superficieCoinvoltaStr, fieldNameSuperficieCoinvolta, 4, new BigDecimal("0.0001"), superficieUtilizzata);
				conduzione.setSuperficieCoinvoltaStr(superficieCoinvoltaStr);

				String fieldNameColturaSecondaria = "colturaSecondaria_" + idUtilizzoDichiarato;
				String colturaSecondaria =  request.getParameter(fieldNameColturaSecondaria);
				
				Long idUtilizzoRiscontrato = null;
		    if(isIstruttoria)
		    {
		      String idUtilizzoRiscontratoStr = request.getParameter("utilizzoRiscontrato_" + idUtilizzoDichiarato);
		      if(idUtilizzoRiscontratoStr != null && !"".equals(idUtilizzoRiscontratoStr.trim()))
		      {
		        idUtilizzoRiscontrato = Long.parseLong(idUtilizzoRiscontratoStr);
		      }
		      
		    }
				
				if(superficieCoinvolta!=null)
				{
					ParticelleFaunaDTO particella = new ParticelleFaunaDTO();
					particella.setExtIdUtilizzoDichiarato(idUtilizzoDichiarato);
					particella.setSuperficieDanneggiata(superficieCoinvolta);
					particella.setFlagUtilizzoSec(colturaSecondaria);
					particella.setIdUtilizzoRiscontrato(idUtilizzoRiscontrato);
					particelleFauna.add(particella);
					conduzione.setErrorSuperficieCoinvolta(false);
				}
				else
				{
					conduzione.setErrorSuperficieCoinvolta(true);
					String descErrore = errors.get(fieldNameSuperficieCoinvolta);
					conduzione.setDescErrorSuperficieCoinvolta(StringEscapeUtils.escapeHtml4(descErrore));
					conduzione.setColturaSecondaria(colturaSecondaria);
					conduzione.setIdUtilizzoRiscontrato(idUtilizzoRiscontrato);
				}
			}
			if(errors.addToModelIfNotEmpty(model))
			{
				common(request, session, model);
				model.addAttribute("conduzioni", conduzioniEsistenti);
				session.setAttribute("conduzioni",conduzioniEsistenti);
				model.addAttribute(FIELD_NAME_HIDE_WIZARD, hideWizard);
				returnValue = "danniFauna/elencoConduzioni";
			}
			else
			{
				LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
				memorizzaParticellaFauna(session, idProcedimentoOggetto, idDannoFauna, particelleFauna, logOperationOggettoQuadroDTO);
				session.removeAttribute("danno");
				returnValue ="redirect:../cuiuffi310l/index.do"; 
			}
		}
		return returnValue;
	}
	
	@RequestMapping(value = "/get_lista_tipo_danno_{idSpecieFauna}", method = RequestMethod.GET)
	public List<DecodificaDTO<Long>> getListaTipoDannoFauna(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable(FIELD_NAME_ID_SPECIE_FAUNA) long idSpecieFauna) throws InternalUnexpectedException
	{
		List<DecodificaDTO<Long>> tipiDanno = quadroIuffiEJB.getListaTipoDannoFauna(idSpecieFauna);
		return tipiDanno;
	}
	

	@Override
	public String getFlagEscludiCatalogo()
	{
		return IuffiConstants.FLAGS.NO;
	}
	
	@Override
	public String getJSPBaseName()
	{
		return "danniFauna";
	}
	
	@RequestMapping(value = "/elenco_particelle", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<ParticelleDanniDTO> elencoParticelleDanni(
			  HttpSession session, 
			  Model model, HttpServletRequest request)
	      throws InternalUnexpectedException
	{
		long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	    String classFiltro = FiltroRicercaConduzioni.class.getName();
	    FiltroRicercaConduzioni filtroRicercaConduzioni = (FiltroRicercaConduzioni) session.getAttribute(classFiltro);
	    session.removeAttribute(classFiltro);
		List<ParticelleDanniDTO> lista = quadroIuffiEJB.getListConduzioniEscludendoGiaSelezionate(idProcedimentoOggetto, filtroRicercaConduzioni);
	    return lista;
	}
	
	@RequestMapping(value = "/elenco_conduzioni_{idDannoFauna}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<ParticelleDanniDTO> elencoConduzioni(
			  HttpSession session, 
			  Model model, HttpServletRequest request,
			  @PathVariable("idDannoFauna") long idDannoFauna)
	      throws InternalUnexpectedException
	{
		List<ParticelleDanniDTO> conduzioni = (List<ParticelleDanniDTO>) session.getAttribute("conduzioni");
		if(conduzioni == null)
		{
			conduzioni = retrieveConduzioni(session,model,request, idDannoFauna);
		}
		session.removeAttribute("conduzioni");
		return conduzioni;
	}
	
	protected abstract List<ParticelleDanniDTO> retrieveConduzioni(HttpSession session, 
			  Model model, HttpServletRequest request, long idDannoFauna) throws InternalUnexpectedException;
	
	
	protected long memorizzaDannoFauna(HttpSession session, long idProcedimentoOggetto, DannoFaunaDTO danno) throws InternalUnexpectedException
	{
		LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		long idDannoFauna = quadroIuffiEJB.memorizzaDannoFauna(idProcedimentoOggetto, danno, logOperationOggettoQuadroDTO);
		return idDannoFauna;
	}
	
	protected void memorizzaParticellaFauna(HttpSession session, long idProcedimentoOggetto, long idDannoFauna, List<ParticelleFaunaDTO> particelleFauna, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException
	{
		quadroIuffiEJB.inserisciParticelleFauna(idProcedimentoOggetto, idDannoFauna, particelleFauna, logOperationOggettoQuadroDTO);
	}
	
	@Override
	public String getFiltriInserimentoConduzione()
	{
		return "danniFauna/include/filtriInserimentoConduzione";
	}
	
}
