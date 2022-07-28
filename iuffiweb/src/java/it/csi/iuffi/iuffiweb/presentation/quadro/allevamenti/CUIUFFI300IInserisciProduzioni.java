package it.csi.iuffi.iuffiweb.presentation.quadro.allevamenti;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.ProduzioneCategoriaAnimaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-300-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi300i")
public class CUIUFFI300IInserisciProduzioni extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  final String fieldNameGiornateLavorativeMedie = "giornLavorMedie";
	  final String fieldNamePesoVivoMedio = "pesoVivoMedio";
	  final String fieldNameNote = "note";
	  
	  final String fieldNameIdProduzione = "idProduzione_";
	  final String fieldNameNumeroCapi = "numeroCapi_";
	  final String fieldNameQuantitaProdotta = "quantitaProdotta_";
	  final String fieldNameQuantitaReimpiegata = "quantitaReimpiegata_";
	  final String fieldNamePrezzo = "prezzo_";
	  final int START_VALUE_ID_DA_INSERIRE = -1;
	  
	  
	  
	  @RequestMapping(value = "/index_{idCategoriaAnimale}_{istatComune}", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model,
			  @PathVariable("idCategoriaAnimale") long idCategoriaAnimale,
			  @PathVariable("istatComune") String istatComune) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  AllevamentiDTO allevamento = quadroIuffiEJB.getDettaglioAllevamento(idProcedimentoOggetto, idCategoriaAnimale, istatComune);
		  List<DecodificaDTO<Integer>> listProduzioni = quadroIuffiEJB.getListProduzioniVendibili(idCategoriaAnimale);
		  List<DecodificaDTO<Integer>> listUnitaMisura = quadroIuffiEJB.getListUnitaMisuraProduzioniVendibili(idCategoriaAnimale);
		  List<ProduzioneCategoriaAnimaleDTO> produzioni = quadroIuffiEJB.getListProduzioniVendibiliGiaInserite(idProcedimentoOggetto, idCategoriaAnimale, istatComune); 
		  model.addAttribute("allevamento", allevamento);
		  model.addAttribute("listProduzioni", listProduzioni);
		  model.addAttribute("listUnitaMisura", listUnitaMisura);
		  model.addAttribute("produzioni", produzioni);
		  model.addAttribute("idCategoriaAnimale", idCategoriaAnimale);
		  model.addAttribute("istatComune", istatComune);
		  model.addAttribute("idsDaInserire", null);
		  model.addAttribute("maxIdDaInserire", START_VALUE_ID_DA_INSERIRE + produzioni.size());
		  return "allevamenti/inserisciProduzioni";
	  }
	  
	  @RequestMapping(value = "/inserisci_conferma_{idCategoriaAnimale}_{istatComune}", method = RequestMethod.POST)
	  public String inserisciConferma(
			  HttpSession session,
			  HttpServletRequest request,
			  Model model, 
			  @PathVariable("idCategoriaAnimale") long idCategoriaAnimale,
			  @PathVariable("istatComune") String istatComune) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  long idUtenteLogin = getUtenteAbilitazioni(session).getIdUtenteLogin();
		  LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		  String paginaDaCaricare;
		  Map<String,Boolean> mapIdProduzioniInserite = new HashMap<String,Boolean>();
		  Errors errors = new Errors();
		  Long numeroCapiTotale = new Long(0L);
		  String[] arrayIdProduzione =  getArrayIdProduzione(idProcedimentoOggetto, idCategoriaAnimale, istatComune);
		  String[] idsDaInserire = request.getParameterValues("idDaInserire");
		  
		  Map<Integer,ProduzioneCategoriaAnimaleDTO> mappaIdProduzioneProduzione =  getMapIdProduzioneProduzione(idProcedimentoOggetto,idCategoriaAnimale,istatComune);
		  
		  String fieldNote = request.getParameter(fieldNameNote);
		  String fieldGiornateLavorativeMedie = request.getParameter(fieldNameGiornateLavorativeMedie);
		  String fieldPesoVivoMedio = request.getParameter(fieldNamePesoVivoMedio);
		  
		  ProduzioneCategoriaAnimaleDTO produzioneGenerica = mappaIdProduzioneProduzione.entrySet().iterator().next().getValue();
		  BigDecimal pesoVivoMedioMin = (produzioneGenerica.getPesoVivoMin() != null ? produzioneGenerica.getPesoVivoMin() : new BigDecimal("0.1"));
		  BigDecimal pesoVivoMedioMax = (produzioneGenerica.getPesoVivoMax() != null ? produzioneGenerica.getPesoVivoMax() : new BigDecimal("9999.9")); 
		  BigDecimal pesoVivoMedio =
				  errors.validateMandatoryBigDecimalInRange(fieldPesoVivoMedio, fieldNamePesoVivoMedio, 1, pesoVivoMedioMin, pesoVivoMedioMax);
		  
		  BigDecimal giornateLavorativeMin = (produzioneGenerica.getGiornateLavorativeMin() != null ? produzioneGenerica.getGiornateLavorativeMin() : new BigDecimal("0.01"));
		  BigDecimal giornateLavorativeMax = (produzioneGenerica.getGiornateLavorativeMax() != null ? produzioneGenerica.getGiornateLavorativeMax() : new BigDecimal("366.00"));
		  BigDecimal giornateLavorativeMedie = 	
				  errors.validateMandatoryBigDecimalInRange(fieldGiornateLavorativeMedie, fieldNameGiornateLavorativeMedie, 2, giornateLavorativeMin, giornateLavorativeMax);
		  
		  Integer maxIdDaInserire = START_VALUE_ID_DA_INSERIRE;
		  Long maxNumeroCapi = new Long(0L);
		  List<ProduzioneCategoriaAnimaleDTO> listProduzioniVendibili = new ArrayList<ProduzioneCategoriaAnimaleDTO>();
		  if(idsDaInserire != null)
		  {
			  for(String idDaInserire : idsDaInserire)
			  {
				  Integer currentIdDaInserire = Integer.parseInt(idDaInserire);
				  maxIdDaInserire = ( (maxIdDaInserire == null || currentIdDaInserire.compareTo(maxIdDaInserire) > 0 ) ? currentIdDaInserire : maxIdDaInserire);
				  String currentFieldNameIdProduzione = fieldNameIdProduzione + idDaInserire;
				  String currentFieldNameNumeroCapi = fieldNameNumeroCapi + idDaInserire;
				  String currentFieldNameQuantitaProdotta = fieldNameQuantitaProdotta + idDaInserire;
				  String currentFieldNameQuantitaReimpiegata = fieldNameQuantitaReimpiegata + idDaInserire;
				  String currentFieldNamePrezzo = fieldNamePrezzo + idDaInserire;
				  
				  String currentFieldIdProduzione = request.getParameter(currentFieldNameIdProduzione);
				  String currentFieldNumeroCapi = request.getParameter(currentFieldNameNumeroCapi);
				  String currentFieldQuantitaProdotta = request.getParameter(currentFieldNameQuantitaProdotta);
				  String currentFieldQuantitaReimpiegata = request.getParameter(currentFieldNameQuantitaReimpiegata);
				  String currentFieldPrezzo = request.getParameter(currentFieldNamePrezzo);
				  
				  errors.validateMandatory(currentFieldIdProduzione, currentFieldNameIdProduzione);
				  ProduzioneCategoriaAnimaleDTO produzione;
				  Integer currentIdProduzione;
				  if(currentFieldIdProduzione != null && !currentFieldIdProduzione.equals(""))
				  {
					  currentIdProduzione = Integer.parseInt(currentFieldIdProduzione);
					  produzione = mappaIdProduzioneProduzione.get(currentIdProduzione);
					  maxNumeroCapi = produzione.getQuantita();
				  }
				  else
				  {
					  produzione = null;
				  }
				  
				  if(mapIdProduzioniInserite.containsKey(currentFieldIdProduzione))
				  {
					  errors.addError(currentFieldNameIdProduzione, "Non è consentito inserire più volte la stessa produzione per lo stesso allevamento.");
				  }else
				  {
					  errors.validateMandatoryValueList(currentFieldIdProduzione, currentFieldNameIdProduzione, arrayIdProduzione);
					  mapIdProduzioniInserite.put(currentFieldIdProduzione,true);
				  }
				  Long currentNumeroCapi = errors.validateMandatoryLongInRange(currentFieldNumeroCapi, currentFieldNameNumeroCapi, 1L, 9999999L);
				  BigDecimal currentNumeroCapiBD = new BigDecimal("0");
				  if(currentNumeroCapi != null)
				  {
					  numeroCapiTotale = numeroCapiTotale + currentNumeroCapi;
					  currentNumeroCapiBD = new BigDecimal(currentNumeroCapi);
				  }
				  
				  BigDecimal currentQuantitaProdotta = null;
				  if(produzione != null)
				  {
					  BigDecimal currentQuantitaProdottaMin = ((produzione.getQuantitaProdottaMin() != null && produzione.getQuantitaProdottaMin().compareTo(new BigDecimal("0.0")) > 0 ) ? produzione.getQuantitaProdottaMin() : new BigDecimal("0.1"));
					  BigDecimal currentQuantitaProdottaMax = (produzione.getQuantitaProdottaMax() != null ? produzione.getQuantitaProdottaMax() : new BigDecimal("99999.9"));
					  currentQuantitaProdotta = errors.validateMandatoryBigDecimalInRange(currentFieldQuantitaProdotta, currentFieldNameQuantitaProdotta, 1, currentQuantitaProdottaMin, currentQuantitaProdottaMax);
				  }
				  else
				  {
					  currentQuantitaProdotta = errors.validateMandatoryBigDecimalInRange(currentFieldQuantitaProdotta, currentFieldNameQuantitaProdotta, 1, new BigDecimal("0.1"), new BigDecimal("99999.9"));
				  }
				  
				  BigDecimal currentQuantitaReimpiegata = 
					  errors.validateMandatoryBigDecimalInRange(currentFieldQuantitaReimpiegata, currentFieldNameQuantitaReimpiegata, 1, 
							  new BigDecimal("0.0"), 
							  (currentQuantitaProdotta != null ? new BigDecimal("99999.9").min(currentQuantitaProdotta.multiply(currentNumeroCapiBD).setScale(1, RoundingMode.DOWN)) : new BigDecimal("99999.9")));
				  
				  BigDecimal currentPrezzo = null;
				  if(produzione != null)
				  {
					  BigDecimal currentPrezzoMin = (produzione.getPrezzoMin() != null ? produzione.getPrezzoMin() : new BigDecimal("0.01"));
					  BigDecimal currentPrezzoMax = (produzione.getPrezzoMax() != null ? produzione.getPrezzoMax() : new BigDecimal("999999.9"));
					  currentPrezzo = errors.validateMandatoryBigDecimalInRange(currentFieldPrezzo, currentFieldNamePrezzo, 2, currentPrezzoMin, currentPrezzoMax);
				  }
				  else
				  {
					  currentPrezzo = errors.validateMandatoryBigDecimalInRange(currentFieldPrezzo, currentFieldNamePrezzo, 2, new BigDecimal("0.01"), new BigDecimal("999999.9"));
				  }
				  
				  if(errors.isEmpty())
				  {
					  ProduzioneCategoriaAnimaleDTO produzioneVendibile = new ProduzioneCategoriaAnimaleDTO();
					  produzioneVendibile.setIdProduzione(new Integer(currentFieldIdProduzione));
					  produzioneVendibile.setNumeroCapi((currentNumeroCapi != null ? currentNumeroCapi : 0L));
					  produzioneVendibile.setQuantitaProdotta(currentQuantitaProdotta);
					  produzioneVendibile.setQuantitaReimpiegata(currentQuantitaReimpiegata);
					  produzioneVendibile.setPrezzo(currentPrezzo);
					  listProduzioniVendibili.add(produzioneVendibile);
				  }
			  }
		  }
		  if(numeroCapiTotale > maxNumeroCapi)
		  {
			  errors.validateMandatory(fieldNote,fieldNameNote,
					  "Campo obbligatorio. Il numero dei capi inseriti supera la quantità di capi registrati in anagrafe.");
		  }
		  if(errors.addToModelIfNotEmpty(model))
		  {
			  paginaDaCaricare = index(session, model, idCategoriaAnimale, istatComune);
			  model.addAttribute("produzioni", null);
			  model.addAttribute("idsDaInserire", idsDaInserire);
			  model.addAttribute("maxIdDaInserire", maxIdDaInserire);
			  model.addAttribute("preferRequest", Boolean.TRUE);
		  }
		  else
		  {
			  AllevamentiDTO produzioneZootecnica = new AllevamentiDTO();
			  produzioneZootecnica.setIstatComune(istatComune);
			  produzioneZootecnica.setIdCategoriaAnimale(idCategoriaAnimale);
			  produzioneZootecnica.setPesoVivoMedio(pesoVivoMedio);
			  produzioneZootecnica.setGiornateLavorativeMedie(giornateLavorativeMedie);
			  produzioneZootecnica.setNote(fieldNote);
			  quadroIuffiEJB.inserisciProduzioneZootecnicaEProduzioniVendibili(idProcedimentoOggetto,produzioneZootecnica,listProduzioniVendibili, idUtenteLogin, logOperationOggettoQuadroDTO);
			  paginaDaCaricare = "redirect:../cuiuffi300l/index.do";
		  }
		  
		  return paginaDaCaricare;
	  }
	  
	  private String[] getArrayIdProduzione(long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune) throws InternalUnexpectedException
	  {
		  List<DecodificaDTO<Integer>> lista = quadroIuffiEJB.getListProduzioniVendibili(idCategoriaAnimale);
		  String[] arrayIdProduzione = new String[lista.size()]; 
		  for(int i=0; i<lista.size();i++)
		  {
			  DecodificaDTO<Integer> ddto = lista.get(i);
			  arrayIdProduzione[i] = ddto.getCodice();
		  }
		  return arrayIdProduzione;
	  }
	  
	  private Map<Integer,ProduzioneCategoriaAnimaleDTO> getMapIdProduzioneProduzione (long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune)
	  	throws InternalUnexpectedException
	  {
		  Map<Integer,ProduzioneCategoriaAnimaleDTO> mappa = new HashMap<Integer,ProduzioneCategoriaAnimaleDTO>();
		  List<ProduzioneCategoriaAnimaleDTO> listaProduzioni = 
				  quadroIuffiEJB.getListProduzioni(idProcedimentoOggetto, idCategoriaAnimale, istatComune);
		  
		  for(ProduzioneCategoriaAnimaleDTO produzione : listaProduzioni)
		  {
			  mappa.put(produzione.getIdProduzione(), produzione);
		  }
		  return mappa;
	  }
}
