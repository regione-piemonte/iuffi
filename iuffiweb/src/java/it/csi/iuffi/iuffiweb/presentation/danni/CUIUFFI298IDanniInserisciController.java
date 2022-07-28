package it.csi.iuffi.iuffiweb.presentation.danni;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.UnitaMisuraDTO;
import it.csi.iuffi.iuffiweb.dto.fabbricati.FabbricatiDTO;
import it.csi.iuffi.iuffiweb.dto.motoriagricoli.MotoriAgricoliDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.QuadroIuffiDAO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-298-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi298i")
public class CUIUFFI298IDanniInserisciController extends CUIUFFI298DanniBaseController
{
	  private static final String LIST_UNITA_MISURA = "listUnitaMisura";
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  ObjectWriter jacksonWriter = new ObjectMapper().writer(new DefaultPrettyPrinter());
	  
	  final static String fieldNameDescrizione = "descrizione";
	  final static String fieldNameUnitaMisura = "unitaMisura";
	  final static String fieldNameQuantita = "quantita";
	  final static String fieldNameImporto = "importo";
	  final static String fieldNameIdDanno = "idDanno";
	  final static String fieldNameIdScortaMagazzino = "idScortaMagazzino";
	  final static String cuId = "CU-IUFFI-298-I";
	  final static String commonFieldIdDanno ="idDanno";
	  final static String inserisciDannoConduzioniDettaglio = "inserisci_danno_conduzioni_dettaglio.do";

	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model) throws InternalUnexpectedException
	  {
	      long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
	      session.removeAttribute(FiltroRicercaConduzioni.class.getName());
	      model.addAttribute("idProcedimentoOggetto",idProcedimentoOggetto);
	      List<DecodificaDTO<Integer>> listTipologieDanni = quadroIuffiEJB.getTabellaDecodifica("IUF_D_DANNO",true);
	      model.addAttribute("listTipologieDanni",listTipologieDanni);
	      return "danni/inserisciDanniTipo";  
	  }	  
	  
	  @RequestMapping(value = "inserisci_danno_dettaglio", method = RequestMethod.POST)
	  public String inserisciDannoDettaglio(
			  HttpSession session, 
			  Model model,
			  HttpServletRequest request
			  ) throws InternalUnexpectedException,JsonGenerationException,JsonMappingException, IOException
	  {
		  Errors errors = new Errors();
		  boolean errorNothingSelected = false;
		  String paginaDaCaricare="";
		  
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
	      String fieldIdDanno = request.getParameter(fieldNameIdDanno);
	      errors.validateMandatoryLong(fieldIdDanno, fieldNameIdDanno);
	      boolean piantagioniArboree = false;
	      if(errors.isEmpty())
	      {
	    	  Integer idDanno = Integer.parseInt(fieldIdDanno);
	    	  
	    	  Map<String, Object> common = getCommonFromSession(cuId, session, true);
	    	  common.put(commonFieldIdDanno, idDanno);
	    	  saveCommonInSession(common, session);
	    	  UnitaMisuraDTO unitaMisura = quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno);
	    	  model.addAttribute(fieldNameIdDanno,idDanno);
	    	  switch(idDanno)
		      {
		      case IuffiConstants.DANNI.SCORTA:
		      case IuffiConstants.DANNI.SCORTE_MORTE:
		    	  String[] arrayIdScortaMagazzino = request.getParameterValues("scorteDualList");
		    	  if(arrayIdScortaMagazzino == null || arrayIdScortaMagazzino.length == 0)
		    	  {
		    		  errorNothingSelected = true;
		    		  model.addAttribute("errorNothingSelected", errorNothingSelected);
		    	  }
		    	  else
		    	  {
		    		  List<ScorteDTO> listScorte = quadroIuffiEJB.getScorteByIds(IuffiUtils.ARRAY.toLong(arrayIdScortaMagazzino),idProcedimentoOggetto);
		    		  model.addAttribute("listScorte",listScorte);
		    		  paginaDaCaricare="danni/inserisciDanniDettaglio";
		    	  }
		    	  break;
		      case IuffiConstants.DANNI.MACCHINA_AGRICOLA:
		      case IuffiConstants.DANNI.ATTREZZATURA:
		    	  String[] arrayIdMacchine = request.getParameterValues("macchineDualList");
		    	  UnitaMisuraDTO danno = quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno);
		    	  if(arrayIdMacchine == null || arrayIdMacchine.length == 0)
		    	  {
		    		  errorNothingSelected=true;
		    		  model.addAttribute("errorNothingSelected", errorNothingSelected);
		    		  paginaDaCaricare="danni/inserisciDanniTipo";
		    	  }
		    	  else
		    	  {
		    		  List<MotoriAgricoliDTO> listMacchine = quadroIuffiEJB.getListMotoriAgricoliNonDanneggiati(IuffiUtils.ARRAY.toLong(arrayIdMacchine),idProcedimentoOggetto, getUtenteAbilitazioni(session).getIdProcedimento());
		    		  model.addAttribute("listMacchine",listMacchine);
		    		  model.addAttribute("danno", danno);
		    		  model.addAttribute("listMacchine", listMacchine);
		    		  paginaDaCaricare="danni/inserisciDanniDettaglio";
		    	  }
		    	  break;
		    	  
		      case IuffiConstants.DANNI.FABBRICATO:
		    	  long[] arrayIdFabbricato = IuffiUtils.ARRAY.toLong(request.getParameterValues("fabbricatiDualList"));
		    	  
		    	  UnitaMisuraDTO dannoFabbricato = quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno);
		    	  if(arrayIdFabbricato == null || arrayIdFabbricato.length == 0)
		    	  {
		    		  errorNothingSelected=true;
		    		  model.addAttribute("errorNothingSelected", errorNothingSelected);
		    		  paginaDaCaricare="danni/inserisciDanniTipo";
		    	  }
		    	  else
		    	  {
		    		  List<FabbricatiDTO> listFabbricati = quadroIuffiEJB.getListFabbricatiNonDanneggiati(idProcedimentoOggetto,arrayIdFabbricato, getUtenteAbilitazioni(session).getIdProcedimento());
		    		  model.addAttribute("listFabbricati",listFabbricati);
		    		  model.addAttribute("danno", dannoFabbricato);
		    		  paginaDaCaricare="danni/inserisciDanniDettaglio";
		    	  }
		    	  break;
		      case IuffiConstants.DANNI.PIANTAGIONI_ARBOREE:
		    	  piantagioniArboree = true;
		      case IuffiConstants.DANNI.TERRENI_RIPRISTINABILI:
		      case IuffiConstants.DANNI.TERRENI_NON_RIPRISTINABILI:
		      case IuffiConstants.DANNI.ALTRE_PIANTAGIONI:
		    	  	String indietro = request.getParameter("indietro");
		    	  	if(indietro != null && indietro.equals("true"))
		    	  	{
		    	  		//bootstrap table popolare in json
		    	  		long[] arrayIdUtilizzoDichiarato = IuffiUtils.ARRAY.toLong(request.getParameterValues("idUtilizzoDichiarato"));
		    	  		List<ParticelleDanniDTO> listConduzioni =  quadroIuffiEJB.getListConduzioniDannoGiaSelezionate(idProcedimentoOggetto, arrayIdUtilizzoDichiarato, piantagioniArboree);
		    		    StringWriter sw = new StringWriter();
		    		    jacksonWriter.writeValue(sw, listConduzioni);
		    		    model.addAttribute("json", sw.toString());
		    	  	}
		    	    model.addAttribute("provincie",quadroIuffiEJB.getListProvinciaConTerreniInConduzione(idProcedimentoOggetto, IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE));
	        		model.addAttribute("comuneDisabled", Boolean.TRUE);
	        		model.addAttribute("sezioneDisabled", Boolean.TRUE);
	        		model.addAttribute("idDanno",fieldIdDanno);
	        		model.addAttribute("cu", "298i");
					model.addAttribute("urlDannoDettaglio",inserisciDannoConduzioniDettaglio);
	        		paginaDaCaricare = "danni/elencoConduzioni";
		    	  
		      break;
		      case IuffiConstants.DANNI.ALLEVAMENTO:
		    	  long[] arrayIdAllevamento = IuffiUtils.ARRAY.toLong(request.getParameterValues("allevamentiDualList"));
		    	  UnitaMisuraDTO dannoAllevamenti = quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno);
		    	  if(arrayIdAllevamento == null || arrayIdAllevamento.length == 0)
		    	  {
		    		  errorNothingSelected=true;
		    		  model.addAttribute("errorNothingSelected", errorNothingSelected);
		    		  paginaDaCaricare="danni/inserisciDanniTipo";
		    	  }
		    	  else
		    	  {
		    		  List<AllevamentiDTO> listAllevamenti = quadroIuffiEJB.getListAllevamentiSingoliNonDanneggiati(idProcedimentoOggetto, arrayIdAllevamento);
		    		  model.addAttribute("listAllevamenti",listAllevamenti);
		    		  model.addAttribute("danno", dannoAllevamenti);
		    		  paginaDaCaricare="danni/inserisciDanniDettaglio";
		    	  }
		    	  
	    	  break;
		      case IuffiConstants.DANNI.ALTRO:
		      default:
		    	  paginaDaCaricare="danni/inserisciDanniDettaglio";
	    		  if(unitaMisura != null)
	    		  {
	    			  model.addAttribute("descUnitaMisura",unitaMisura.getDescrizione());
	    		  }
	    		  else
	    		  {
	    			  //danni ad altro o danni che comunque non riportano l'unita di misura nella IUF_d_danno
	    			  List<DecodificaDTO<Long>> listUnitaMisura = quadroIuffiEJB.getListUnitaDiMisura();
	    			  model.addAttribute(LIST_UNITA_MISURA, listUnitaMisura);
	    		  }
		    	  
	    		  paginaDaCaricare="danni/inserisciDanniDettaglio";
		    	  break;
		      }
	      }
	      if(errors.addToModelIfNotEmpty(model) || errorNothingSelected)
	      {
	    	  model.addAttribute("preferRequest", Boolean.TRUE);
	    	  return this.index(session, model);
	      }
	      else
	      {
		      List<DecodificaDTO<Long>> listUnitaMisura = quadroIuffiEJB.getListUnitaDiMisura();
		      model.addAttribute(LIST_UNITA_MISURA,listUnitaMisura);
	    	  return paginaDaCaricare;
	      }
	  }
	  
	  @RequestMapping(value = "inserisci_danni_conferma", method = RequestMethod.POST)
	  public String inserisciDanniConferma(
			  HttpSession session, 
			  Model model,
			  HttpServletRequest request
			  ) throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
	  {
		  Errors errors = new Errors();
		  Map<String, Object> common = getCommonFromSession(cuId, session, true);
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  final Integer idDanno = (Integer)common.get(commonFieldIdDanno);
		  List<DanniDTO> listDanniDTO = new ArrayList<DanniDTO>();
		  List<ScorteDTO> listScorte = null;
		  final String paginaDettaglio = "danni/inserisciDanniDettaglio";
		  LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		  Long idUnitaMisura = null;
		  String fieldDescrizione;
		  String fieldQuantita;
		  String fieldImporto;
		  String fieldUnitaMisura;
		  
		  switch(idDanno)
		  {
		  	  case IuffiConstants.DANNI.SCORTA: //caso scorte
		  	  case IuffiConstants.DANNI.SCORTE_MORTE: //caso scorte
				  long arrayIdScortaMagazzino[] = IuffiUtils.ARRAY.toLong(request.getParameterValues("hiddenIdScortaMagazzino"));
				  for(long idScortaMagazzino : arrayIdScortaMagazzino)
				  {
					  validaCampiInseritiDanno(request, errors, idScortaMagazzino, idDanno);
				  }
				  if(errors.addToModelIfNotEmpty(model))
				  {
						listScorte = quadroIuffiEJB.getScorteByIds(arrayIdScortaMagazzino, idProcedimentoOggetto);
						model.addAttribute("listScorte", listScorte);
						return ritornaPaginaInserimento(model, idDanno, paginaDettaglio);
				  }
				  else
				  {
					  for(long idScortaMagazzino : arrayIdScortaMagazzino)
					  {
						  inserisciDanniInLista(request, idProcedimentoOggetto, idDanno, idScortaMagazzino,listDanniDTO);
					  }
					  quadroIuffiEJB.inserisciDanni(
							  				listDanniDTO,
							  				idProcedimentoOggetto,
							  				idDanno,
							  				logOperationOggettoQuadroDTO, getUtenteAbilitazioni(session).getIdProcedimento());
					  return "redirect:../cuiuffi298l/index.do";
				  }
			  case IuffiConstants.DANNI.MACCHINA_AGRICOLA: //caso Motori Agricoli - Macchine
			  case IuffiConstants.DANNI.ATTREZZATURA:
				  long [] arrayIdMacchina = IuffiUtils.ARRAY.toLong(request.getParameterValues("hiddenIdMacchina"));
				  for(long idMacchina : arrayIdMacchina)
				  {
					  validaCampiInseritiDanno(request, errors, idMacchina, idDanno);
				  }
				  if(errors.addToModelIfNotEmpty(model))
				  {
						List<MotoriAgricoliDTO> listMacchine = quadroIuffiEJB.getListMotoriAgricoliNonDanneggiati(arrayIdMacchina, idProcedimentoOggetto, getUtenteAbilitazioni(session).getIdProcedimento());
						UnitaMisuraDTO danno = quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno);
						model.addAttribute("listMacchine", listMacchine);
						model.addAttribute("idDanno", idDanno);
						model.addAttribute("danno",danno);
						return ritornaPaginaInserimento(model, idDanno, paginaDettaglio);
				  }
				  else
				  {
					  for(long idMacchina : arrayIdMacchina)
					  {
						  inserisciDanniInLista(request, idProcedimentoOggetto, idDanno, idMacchina, listDanniDTO);
					  }
					  quadroIuffiEJB.inserisciDanni(
				  				listDanniDTO,
				  				idProcedimentoOggetto,
				  				idDanno,
				  				logOperationOggettoQuadroDTO, getUtenteAbilitazioni(session).getIdProcedimento());
					  return "redirect:../cuiuffi298l/index.do";
				  }
			  case IuffiConstants.DANNI.FABBRICATO:
				  long[] arrayIdFabbricato = IuffiUtils.ARRAY.toLong(request.getParameterValues("hiddenIdFabbricato"));
				  
				  for(long idFabbricato : arrayIdFabbricato)
				  {
					  validaCampiInseritiDanno(request, errors, idFabbricato, idDanno);
				  }
				  if(errors.addToModelIfNotEmpty(model))
				  {
						List<FabbricatiDTO> listFabbricati= quadroIuffiEJB.getListFabbricatiNonDanneggiati(idProcedimentoOggetto,arrayIdFabbricato, getUtenteAbilitazioni(session).getIdProcedimento());
						UnitaMisuraDTO danno = quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno);
						model.addAttribute("listFabbricati", listFabbricati);
						model.addAttribute("idDanno", idDanno);
						model.addAttribute("danno",danno);
						return ritornaPaginaInserimento(model, idDanno, paginaDettaglio);
				  }
				  else
				  {
					  for(long idFabbricato : arrayIdFabbricato)
					  {
						  inserisciDanniInLista(request, idProcedimentoOggetto, idDanno, idFabbricato, listDanniDTO);
					  }
					  quadroIuffiEJB.inserisciDanni(
				  				listDanniDTO,
				  				idProcedimentoOggetto,
				  				idDanno,
				  				logOperationOggettoQuadroDTO, getUtenteAbilitazioni(session).getIdProcedimento());
					  return "redirect:../cuiuffi298l/index.do";
				  }	
			  case IuffiConstants.DANNI.TERRENI_RIPRISTINABILI:
			  case IuffiConstants.DANNI.TERRENI_NON_RIPRISTINABILI:
			  case IuffiConstants.DANNI.PIANTAGIONI_ARBOREE:
			  case IuffiConstants.DANNI.ALTRE_PIANTAGIONI:
				  String fieldNameIdUtilizzoDichiarato = "idUtilizzoDichiarato";
				  long[] arrayIdUtilizzoDichiarato = IuffiUtils.ARRAY.toLong(request.getParameterValues(fieldNameIdUtilizzoDichiarato));  
				  fieldDescrizione = request.getParameter(fieldNameDescrizione);
				  fieldQuantita   = request.getParameter(fieldNameQuantita);
				  fieldImporto = request.getParameter(fieldNameImporto);
				  BigDecimal sumSuperficiCatastali = 
						  quadroIuffiEJB.getSumSuperficiCatastaliParticelle(idProcedimentoOggetto,arrayIdUtilizzoDichiarato);			  
				  BigDecimal quantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, fieldNameQuantita, 4, 
						  new BigDecimal("0.0001"),
						  new BigDecimal("99999.9999").min(sumSuperficiCatastali));
				  
				  errors.validateMandatoryFieldLength(fieldDescrizione, 1, 4000, fieldNameDescrizione);
				  BigDecimal importo = errors.validateMandatoryBigDecimalInRange(fieldImporto, fieldNameImporto, 2, new BigDecimal("0.01"), new BigDecimal("9999999.99"));
				  if(errors.addToModelIfNotEmpty(model)){
					  model.addAttribute("preferRequest", Boolean.TRUE);
					  return inserisciDannoConduzioniDettaglio(session, model, request);
				  }
				  else
				  {
					  DanniDTO danno = new DanniDTO();
					  danno.setIdProcedimentoOggetto(idProcedimentoOggetto);
					  danno.setDescrizione(fieldDescrizione);
					  danno.setIdDanno(idDanno);
					  danno.setImporto(importo);
					  danno.setQuantita(quantita);
					  danno.setIdUnitaMisura(quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno).getIdUnitaMisura());
					  
					  quadroIuffiEJB.inserisciDanniConduzioni(danno, idProcedimentoOggetto, arrayIdUtilizzoDichiarato, logOperationOggettoQuadroDTO);
				  }
				  break;
			  case IuffiConstants.DANNI.ALLEVAMENTO:
				  long[] arrayIdAllevamento = IuffiUtils.ARRAY.toLong(request.getParameterValues("hiddenIdAllevamento"));
				  List<AllevamentiDTO> listaAllevamenti = quadroIuffiEJB.getListAllevamentiSingoliNonDanneggiati(idProcedimentoOggetto, arrayIdAllevamento);
				  Map<Long,Long> mappaIdAllevamentoQuantita = new HashMap<Long,Long>();
				  listDanniDTO = new ArrayList<DanniDTO>();
				  for(AllevamentiDTO allevamento : listaAllevamenti)
				  {
					  mappaIdAllevamentoQuantita.put(allevamento.getIdAllevamento(), allevamento.getQuantita());
					  
				  }
				  for(long idAllevamento : arrayIdAllevamento)
				  {
					  if(mappaIdAllevamentoQuantita.containsKey(idAllevamento))
					  {
						  fieldDescrizione = request.getParameter(fieldNameDescrizione + "_" + idAllevamento);
						  fieldQuantita   = request.getParameter(fieldNameQuantita + "_" + idAllevamento);
						  fieldImporto = request.getParameter(fieldNameImporto + "_" + idAllevamento);
						  
						  errors.validateMandatory(fieldDescrizione, fieldNameDescrizione + "_" + idAllevamento);
						  importo = errors.validateMandatoryBigDecimalInRange(fieldImporto, fieldNameImporto + "_" + idAllevamento , 2, new BigDecimal("0.01"), new BigDecimal("9999999.99"));
						  
						  Long maxNumeroCapi = mappaIdAllevamentoQuantita.get(idAllevamento);
						  quantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, fieldNameQuantita + "_" + idAllevamento, 0, new BigDecimal("1"), new BigDecimal(maxNumeroCapi));
						  
						  if(errors.isEmpty())
						  {
							  DanniDTO danno = new DanniDTO();
							  danno.setDescrizione(fieldDescrizione);
							  danno.setQuantita(quantita);
							  danno.setImporto(importo);
							  danno.setExtIdEntitaDanneggiata(idAllevamento);
							  danno.setIdDanno(idDanno);
							  danno.setIdProcedimentoOggetto(idProcedimentoOggetto);
							  listDanniDTO.add(danno);
						  }
					  }
				  }
				  if(errors.addToModelIfNotEmpty(model))
				  {
		    		  List<AllevamentiDTO> listAllevamenti = quadroIuffiEJB.getListAllevamentiSingoliNonDanneggiati(idProcedimentoOggetto, arrayIdAllevamento);
		    		  UnitaMisuraDTO dannoAllevamenti = quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno);
		    		  model.addAttribute("listAllevamenti",listAllevamenti);
		    		  model.addAttribute("danno", dannoAllevamenti);
		    		  model.addAttribute("idDanno", idDanno);
		    		  model.addAttribute("preferRequest", Boolean.TRUE);
		    		  return "danni/inserisciDanniDettaglio";
					 
				  }
				  else
				  {
					  quadroIuffiEJB.inserisciDanni(listDanniDTO, idProcedimentoOggetto, idDanno, logOperationOggettoQuadroDTO, getUtenteAbilitazioni(session).getIdProcedimento());
				  }
				  break;
			 
			  //i danni che non sono contemplati in questa lista devvono essere trattati in modo generico  
			  case IuffiConstants.DANNI.ALTRO:
			  default:
				  fieldDescrizione = request.getParameter(fieldNameDescrizione);
				  fieldQuantita = request.getParameter(fieldNameQuantita);
				  fieldImporto = request.getParameter(fieldNameImporto);
				  fieldUnitaMisura = request.getParameter(fieldNameUnitaMisura);
				  errors.validateMandatory(fieldDescrizione, fieldNameDescrizione);
				  
				  quantita = errors.validateMandatoryBigDecimalInRange(fieldQuantita, fieldNameQuantita, 4, new BigDecimal("0.01"),new BigDecimal("99999.9999"));
				  importo = errors.validateMandatoryBigDecimalInRange(fieldImporto, fieldNameImporto, 2, new BigDecimal("0.01"), new BigDecimal("9999999.9999"));
				  boolean gestisciUnitaMisura = quadroIuffiEJB.getGestisciUnitaMisuraByIdDanno(idDanno);
				  if(gestisciUnitaMisura)
				  {
					  fieldUnitaMisura = request.getParameter(fieldNameUnitaMisura);
					  idUnitaMisura = errors.validateMandatoryLong(fieldUnitaMisura, fieldNameUnitaMisura);
				  }
				  
				  if(errors.addToModelIfNotEmpty(model))
				  {
		    		  model.addAttribute("idDanno", idDanno);
		    		  model.addAttribute("preferRequest", Boolean.TRUE);
		    		  UnitaMisuraDTO unitaMisura = quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno);
		    		  if(unitaMisura != null)
		    		  {
		    			  model.addAttribute("descUnitaMisura",unitaMisura.getDescrizione());
		    		  }
		    		  return ritornaPaginaInserimento(model, idDanno, paginaDettaglio);
				  }
				  else
				  {
					  listDanniDTO = new ArrayList<DanniDTO>();
					  DanniDTO danno = new DanniDTO();
					  danno.setDescrizione(fieldDescrizione);
					  danno.setQuantita(quantita);
					  danno.setImporto(importo);
					  danno.setIdProcedimentoOggetto(idProcedimentoOggetto);
					  danno.setIdDanno(idDanno);
					  danno.setIdUnitaMisura(idUnitaMisura);
					  listDanniDTO.add(danno);
					  quadroIuffiEJB.inserisciDanni(listDanniDTO, idProcedimentoOggetto, idDanno, logOperationOggettoQuadroDTO, getUtenteAbilitazioni(session).getIdProcedimento());
				  }
				  break;
		  }
		  return "redirect:../cuiuffi298l/index.do";
	  }

	private void inserisciDanniInLista(HttpServletRequest request, long idProcedimentoOggetto, final Integer idDanno, long idElemento,List<DanniDTO> listDanniDTO)
	{
		  String fieldDescrizione = request.getParameter(fieldNameDescrizione + "_" + idElemento);
		  String fieldQuantita   = request.getParameter(fieldNameQuantita + "_" + idElemento);
		  String fieldImporto = request.getParameter(fieldNameImporto + "_" + idElemento);
		  
		  DanniDTO tmpDanniDTO = new DanniDTO();
		  switch(idDanno)
		  {
			  case IuffiConstants.DANNI.SCORTA:
			  case IuffiConstants.DANNI.SCORTE_MORTE:
				  tmpDanniDTO.setDescrizione(fieldDescrizione);
				  tmpDanniDTO.setQuantita(new BigDecimal(fieldQuantita.replace(',', '.')));
				  tmpDanniDTO.setImporto(new BigDecimal(fieldImporto.replace(',', '.')));
				  tmpDanniDTO.setIdDanno(idDanno);
				  tmpDanniDTO.setIdProcedimentoOggetto(idProcedimentoOggetto);
 				  tmpDanniDTO.setExtIdEntitaDanneggiata(idElemento);
				  break;
				  
			  case IuffiConstants.DANNI.MACCHINA_AGRICOLA:
			  case IuffiConstants.DANNI.ATTREZZATURA:
				  tmpDanniDTO.setDescrizione(fieldDescrizione);
				  tmpDanniDTO.setQuantita(new BigDecimal("1.0"));
				  tmpDanniDTO.setImporto(new BigDecimal(fieldImporto.replace(',', '.')));
				  tmpDanniDTO.setIdDanno(idDanno);
				  tmpDanniDTO.setIdProcedimentoOggetto(idProcedimentoOggetto);
				  tmpDanniDTO.setExtIdEntitaDanneggiata(idElemento);
				  break;
				  
			  case IuffiConstants.DANNI.FABBRICATO:
				  
				  tmpDanniDTO.setDescrizione(fieldDescrizione);
				  tmpDanniDTO.setQuantita(new BigDecimal(fieldQuantita.replace(',', '.')));
				  tmpDanniDTO.setImporto(new BigDecimal(fieldImporto.replace(',', '.')));
				  tmpDanniDTO.setIdDanno(idDanno);
				  tmpDanniDTO.setIdProcedimentoOggetto(idProcedimentoOggetto);
				  tmpDanniDTO.setExtIdEntitaDanneggiata(idElemento);
				  break;
			  default:
				  //la gestione dei danni a superfici, essendo diversa, è eseguita nel metodo chiamante
				  break;
		  }
		  listDanniDTO.add(tmpDanniDTO);
	}

	private void validaCampiInseritiDanno(HttpServletRequest request, Errors errors, long idElemento, Integer idDanno) throws InternalUnexpectedException
	{
		  String fieldDescrizione = request.getParameter(fieldNameDescrizione + "_" + idElemento);
		  String fieldQuantita   = request.getParameter(fieldNameQuantita + "_" + idElemento);
		  String fieldImporto = request.getParameter(fieldNameImporto + "_" + idElemento);
		  
		  switch(idDanno)
		  {
			  case IuffiConstants.DANNI.SCORTA:
			  case IuffiConstants.DANNI.SCORTE_MORTE:
				  errors.validateMandatory(fieldDescrizione, fieldNameDescrizione + "_" + idElemento);
				  BigDecimal maxQuantita = quadroIuffiEJB.getScortaByIdScortaMagazzino(new Long(idElemento)).getQuantita();
				  errors.validateMandatoryBigDecimalInRange(fieldQuantita, fieldNameQuantita + "_" + idElemento, 2, new BigDecimal("0.01"),new BigDecimal("99999.99"));
				  errors.validateMandatoryBigDecimalInRange(fieldQuantita, fieldNameQuantita + "_" + idElemento, 2, new BigDecimal("0.01"),maxQuantita);
				  errors.validateMandatoryBigDecimalInRange(fieldImporto, fieldNameImporto + "_" + idElemento , 2, new BigDecimal("0.01"), new BigDecimal("9999999.99"));
				  
				  break;
			  case IuffiConstants.DANNI.FABBRICATO:
				  errors.validateMandatoryBigDecimalInRange(fieldQuantita, fieldNameQuantita + "_" + idElemento, 2, new BigDecimal("0.01"), new BigDecimal("99999.99"));
				  errors.validateMandatory(fieldDescrizione, fieldNameDescrizione + "_" + idElemento);
				  errors.validateMandatoryBigDecimalInRange(fieldImporto, fieldNameImporto + "_" + idElemento , 2, new BigDecimal("0.01"), new BigDecimal("9999999.99"));
				  break;
				  
			  case IuffiConstants.DANNI.MACCHINA_AGRICOLA:
			  case IuffiConstants.DANNI.ATTREZZATURA:
				  errors.validateMandatory(fieldDescrizione, fieldNameDescrizione + "_" + idElemento);
				  errors.validateMandatoryBigDecimalInRange(fieldImporto, fieldNameImporto + "_" + idElemento , 2, new BigDecimal("0.01"), new BigDecimal("9999999.99"));
				  break;

			  case IuffiConstants.DANNI.ALLEVAMENTO:
				  //non validato qua
				  break;
		
		  }
	}

	private String ritornaPaginaInserimento(Model model, final Integer idDanno, String paginaDettaglio) throws InternalUnexpectedException
	{
		List<DecodificaDTO<Long>> listUnitaMisura;
		listUnitaMisura = quadroIuffiEJB.getListUnitaDiMisura();
		model.addAttribute(LIST_UNITA_MISURA, listUnitaMisura);
		model.addAttribute(fieldNameIdDanno, idDanno);
		model.addAttribute("preferRequest", Boolean.TRUE);
		return paginaDettaglio;
	}

	  @RequestMapping(value = "/inserisci_danno_conduzioni_dettaglio", method = RequestMethod.POST)
	  public String inserisciDannoConduzioniDettaglio(HttpSession session, Model model, HttpServletRequest request)
	      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  long[] arrayIdUtilizzoDichiarato = IuffiUtils.ARRAY.toLong(request.getParameterValues("idUtilizzoDichiarato"));
		  String fieldNameIdDanno = "idDanno";
		  String fieldIdDanno = request.getParameter(fieldNameIdDanno);
		  boolean piantagioniArboree = false;
		  int idDanno = Integer.parseInt(fieldIdDanno);
		  String paginaDaCaricare = null; 
		  List<Integer> listDanniEquivalentiConduzioni = QuadroIuffiDAO.getListDanniEquivalenti(IuffiConstants.DANNI.TERRENI_RIPRISTINABILI);
		  if(listDanniEquivalentiConduzioni.contains(idDanno))
		  {
			  if(idDanno == IuffiConstants.DANNI.PIANTAGIONI_ARBOREE)
			  {
				  piantagioniArboree = true;
			  }
			  if(arrayIdUtilizzoDichiarato == null || arrayIdUtilizzoDichiarato.length == 0)
			  {
				  paginaDaCaricare = inserisciDannoDettaglio(session, model, request);
				  model.addAttribute("errorNoConduzioni", Boolean.TRUE);
				  model.addAttribute("dataUrl", "");
				  
			  }
			  else
			  {
				  List<ParticelleDanniDTO> listConduzioni = quadroIuffiEJB.getListConduzioniDannoGiaSelezionate(idProcedimentoOggetto, arrayIdUtilizzoDichiarato, piantagioniArboree);
				  model.addAttribute("descUnitaMisura", quadroIuffiEJB.getUnitaMisuraByIdDanno(idDanno).getDescrizione());
				  model.addAttribute(fieldNameIdDanno, fieldIdDanno);
				  model.addAttribute("listConduzioni", listConduzioni);
				  paginaDaCaricare = "danni/inserisciDanniDettaglio";
			  }
		  }
		  else
		  {
			  paginaDaCaricare = index(session,model);
		  }
		  return paginaDaCaricare;
	  }
	  
		@RequestMapping(value = "/get_elenco_fabbricati_non_danneggiati", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public List<FabbricatiDTO> getElencoFabbricatiNonDanneggiati(HttpSession session, Model model,
				HttpServletRequest request) throws InternalUnexpectedException
		{
			long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
			List<FabbricatiDTO> lista = quadroIuffiEJB.getListFabbricatiNonDanneggiati(idProcedimentoOggetto,null, getUtenteAbilitazioni(session).getIdProcedimento());
			return lista;
		}
		
		@RequestMapping(value = "/get_elenco_motori_agricoli_non_danneggiati", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public List<MotoriAgricoliDTO> getMotoriAgricoliNonDanneggiati(HttpSession session, Model model,
				HttpServletRequest request) throws InternalUnexpectedException
		{
			long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
			return quadroIuffiEJB.getListMotoriAgricoliNonDanneggiati(idProcedimentoOggetto, getUtenteAbilitazioni(session).getIdProcedimento());
		}
		
		@RequestMapping(value = "/get_list_allevamenti_singoli_non_danneggiati.json", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public List<AllevamentiDTO> getListAllevamentiSingoliNonDanneggiati(HttpSession session, Model model) throws InternalUnexpectedException
		{
			long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
			List<AllevamentiDTO> lista =  quadroIuffiEJB.getListAllevamentiSingoliNonDanneggiati(idProcedimentoOggetto);
			return lista;
		}
		
		@RequestMapping(value = "/get_elenco_scorte_non_danneggiate", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public List<ScorteDTO> getListaScorteNonDanneggiateByProcedimentoOggetto(
				  HttpSession session, 
				  Model model
				  )  throws InternalUnexpectedException
		{
			long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
			return quadroIuffiEJB.getListaScorteNonDanneggiateByProcedimentoOggetto(idProcedimentoOggetto);
		}
}
