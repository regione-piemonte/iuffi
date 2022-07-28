package it.csi.iuffi.iuffiweb.presentation.quadro.coltureaziendali;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.assicurazionicolture.AssicurazioniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDettaglioDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-305-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi305l")
public class CUIUFFI305LVisualizzaColtureAziendali extends CUIUFFI305BaseController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpSession session, HttpServletRequest request, Model model) throws InternalUnexpectedException {
	long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	ColtureAziendaliDTO riepilogo = quadroIuffiEJB.getRiepilogoColtureAziendali(idProcedimentoOggetto);
	LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
	String ultimaModifica = getUltimaModifica(quadroIuffiEJB, logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), logOperationOggettoQuadroDTO.getIdQuadroOggetto(), logOperationOggettoQuadroDTO.getIdBandoOggetto());
	Map<Long, StringBuilder> mapIdSuperficieColturaAnomalia = this.getMapIdSuperficieColturaAnomalia(idProcedimentoOggetto, request);

	// assicurazioni
	List<AssicurazioniColtureDTO> listAssicurazioniColture = quadroIuffiEJB.getListAssicurazioniColture(idProcedimentoOggetto);
	AssicurazioniColtureDTO assicurazioniColture = new AssicurazioniColtureDTO();
	if (listAssicurazioniColture != null && listAssicurazioniColture.size() > 0) {
	    BigDecimal sommaImportoPremio = BigDecimal.ZERO, sommaImportoAssicurato = BigDecimal.ZERO, sommaImportoRimborso = BigDecimal.ZERO;
	    for (AssicurazioniColtureDTO assic : listAssicurazioniColture) {
		if (assic.getImportoPremio() != null) sommaImportoPremio = sommaImportoPremio.add(assic.getImportoPremio());
		if (assic.getImportoAssicurato() != null) sommaImportoAssicurato = sommaImportoAssicurato.add(assic.getImportoAssicurato());
		if (assic.getImportoRimborso() != null) sommaImportoRimborso = sommaImportoRimborso.add(assic.getImportoRimborso());
	    }
	    assicurazioniColture.setImportoPremio(sommaImportoPremio);
	    assicurazioniColture.setImportoAssicurato(sommaImportoAssicurato);
	    assicurazioniColture.setImportoRimborso(sommaImportoRimborso);
	} else {
	    assicurazioniColture.setImportoPremio(BigDecimal.ZERO);
	    assicurazioniColture.setImportoAssicurato(BigDecimal.ZERO);
	    assicurazioniColture.setImportoRimborso(BigDecimal.ZERO);
	}

	if (riepilogo.getTotalePlvEffettiva() != null) {
	    riepilogo.setPlvRicalcolataConAssicurazioni(riepilogo.getTotalePlvEffettiva().subtract(assicurazioniColture.getImportoPremio()).add(assicurazioniColture.getImportoRimborso()));
	} else {
	    riepilogo.setPlvRicalcolataConAssicurazioni(null);
	}
	if (riepilogo.getTotalePlvOrdinaria() != null) {
	    riepilogo.setPlvOrdinariaConAssicurazioni(riepilogo.getTotalePlvOrdinaria().subtract(IuffiUtils.NUMBERS.nvl(riepilogo.getPlvRicalcolataConAssicurazioni())));
	} else {
	    riepilogo.setPlvOrdinariaConAssicurazioni(null);
	}

	if (riepilogo.getTotalePlvOrdinaria() == null || riepilogo.getTotalePlvOrdinaria().equals(BigDecimal.ZERO)) {
	    riepilogo.setPercentualeDannoConAssicurazioni(BigDecimal.ZERO);
	} else {
	    riepilogo.setPercentualeDannoConAssicurazioni(riepilogo.getPlvOrdinariaConAssicurazioni().divide(riepilogo.getTotalePlvOrdinaria(), MathContext.DECIMAL64).multiply(new BigDecimal("100.00")));
	}

	if (riepilogo.getTotalePlvEffettiva() != null && assicurazioniColture.getImportoRimborso() != null) {
	    riepilogo.setIndennizzoRichiesto(riepilogo.getTotalePlvEffettiva().subtract(assicurazioniColture.getImportoRimborso()));
	} else {
	    riepilogo.setIndennizzoRichiesto(null);
	}

	model.addAttribute("assicurazioniColture", assicurazioniColture);
	model.addAttribute("riepilogo", riepilogo);
	model.addAttribute("ultimaModifica", ultimaModifica);
	model.addAttribute("mapIdSuperficieColturaAnomalia", mapIdSuperficieColturaAnomalia);
	return "coltureaziendali/visualizzaColtureAziendali";
    }

    @RequestMapping(value = "/get_list_colture_aziendali.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<ColtureAziendaliDettaglioDTO> getListColtureAziendali(HttpSession session, Model model) throws InternalUnexpectedException {
	long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
	List<ColtureAziendaliDettaglioDTO> listSuperficiColtureDettaglio = quadroIuffiEJB.getListColtureAziendali(idProcedimentoOggetto);
	return listSuperficiColtureDettaglio;
    }

    @Override
    protected long[] getArrayIdSuperficieColtura(HttpServletRequest request) {
	// li voglio scaricare tutti
	return null;
    }
    
    @RequestMapping(value = "dichiarazionePLVExcel")
    public ModelAndView downloadExcel(Model model, HttpSession session) throws InternalUnexpectedException {
	long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
	List<ColtureAziendaliDettaglioDTO> listSuperficiColtureDettaglio = quadroIuffiEJB.getListColtureAziendali(idProcedimentoOggetto);
	//listSuperficiColtureDettaglio.add(calcolaTotali(listSuperficiColtureDettaglio));
	return new ModelAndView("excelDichiarazionePLVView", "elenco", listSuperficiColtureDettaglio);
    }
    
//    private ColtureAziendaliDettaglioDTO calcolaTotali(List<ColtureAziendaliDettaglioDTO> elenco) {
//	ColtureAziendaliDettaglioDTO rigaTotali = new ColtureAziendaliDettaglioDTO();	
//	rigaTotali.setSuperficieUtilizzata(BigDecimal.ZERO);
//
//	rigaTotali.setProduzioneHa(BigDecimal.ZERO);
//	rigaTotali.setProduzioneDichiarata(BigDecimal.ZERO);
//	rigaTotali.setPrezzo(BigDecimal.ZERO);
//	rigaTotali.setTotaleEuroPlvOrd(BigDecimal.ZERO);
//	rigaTotali.setGiornateLavorateHa(BigDecimal.ZERO);
//	rigaTotali.setGiornateLavorate(BigDecimal.ZERO);
//
//	rigaTotali.setProduzioneHaDanno(BigDecimal.ZERO);
//	rigaTotali.setProduzioneTotaleDanno(BigDecimal.ZERO);
//	rigaTotali.setPrezzoDanneggiato(BigDecimal.ZERO);
//	rigaTotali.setTotaleEuroPlvEff(BigDecimal.ZERO);
//	rigaTotali.setImportoRimborso(BigDecimal.ZERO);
//	rigaTotali.setTotConRimborsi(BigDecimal.ZERO);
//	
//	rigaTotali.setEuroDanno(BigDecimal.ZERO);
//
//	for (ColtureAziendaliDettaglioDTO i : elenco) {	    
//	    if (i.getSuperficieUtilizzata() != null) rigaTotali.setSuperficieUtilizzata(rigaTotali.getSuperficieUtilizzata().add(i.getSuperficieUtilizzata()));
//
//	    if (i.getProduzioneHa() != null) rigaTotali.setProduzioneHa(rigaTotali.getProduzioneHa().add(i.getProduzioneHa()));
//	    if (i.getProduzioneDichiarata() != null) rigaTotali.setProduzioneDichiarata(rigaTotali.getProduzioneDichiarata().add(i.getProduzioneDichiarata()));
//	    if (i.getPrezzo() != null) rigaTotali.setPrezzo(rigaTotali.getPrezzo().add(i.getPrezzo()));
//	    if (i.getTotaleEuroPlvOrd() != null) rigaTotali.setTotaleEuroPlvOrd(rigaTotali.getTotaleEuroPlvOrd().add(i.getTotaleEuroPlvOrd()));
//	    if (i.getGiornateLavorateHa() != null) rigaTotali.setGiornateLavorateHa(rigaTotali.getGiornateLavorateHa().add(i.getGiornateLavorateHa()));
//	    if (i.getGiornateLavorate() != null) rigaTotali.setGiornateLavorate(rigaTotali.getGiornateLavorate().add(i.getGiornateLavorate()));
//
//	    if (i.getProduzioneHaDanno() != null) rigaTotali.setProduzioneHaDanno(rigaTotali.getProduzioneHaDanno().add(i.getProduzioneHaDanno()));
//	    if (i.getProduzioneTotaleDanno() != null) rigaTotali.setProduzioneTotaleDanno(rigaTotali.getProduzioneTotaleDanno().add(i.getProduzioneTotaleDanno()));
//	    if (i.getPrezzoDanneggiato() != null) rigaTotali.setPrezzoDanneggiato(rigaTotali.getPrezzoDanneggiato().add(i.getPrezzoDanneggiato()));
//	    if (i.getTotaleEuroPlvEff() != null) rigaTotali.setTotaleEuroPlvEff(rigaTotali.getTotaleEuroPlvEff().add(i.getTotaleEuroPlvEff()));
//	    if (i.getImportoRimborso() != null) rigaTotali.setImportoRimborso(rigaTotali.getImportoRimborso().add(i.getImportoRimborso()));
//	    if (i.getTotConRimborsi() != null) rigaTotali.setTotConRimborsi(rigaTotali.getTotConRimborsi().add(i.getTotConRimborsi()));	    
//
//	    if (i.getFlagDanneggiato() != null && i.getFlagDanneggiato().equalsIgnoreCase("S") && i.getTotaleEuroPlvEff() != null && i.getEuroDanno() != null) rigaTotali.setEuroDanno(rigaTotali.getEuroDanno().add(i.getEuroDanno()));	    
//	}
//	
//	if (rigaTotali.getSuperficieUtilizzata() == BigDecimal.ZERO) rigaTotali.setSuperficieUtilizzata(null);
//
//	if (rigaTotali.getProduzioneHa() == BigDecimal.ZERO) rigaTotali.setProduzioneHa(null);
//	if (rigaTotali.getProduzioneDichiarata() == BigDecimal.ZERO) rigaTotali.setProduzioneDichiarata(null);
//	if (rigaTotali.getPrezzo() == BigDecimal.ZERO) rigaTotali.setPrezzo(null);
//	if (rigaTotali.getTotaleEuroPlvOrd() == BigDecimal.ZERO) rigaTotali.setTotaleEuroPlvOrd(null);
//	if (rigaTotali.getGiornateLavorateHa() == BigDecimal.ZERO) rigaTotali.setGiornateLavorateHa(null);
//	if (rigaTotali.getGiornateLavorate() == BigDecimal.ZERO) rigaTotali.setGiornateLavorate(null);
//
//	if (rigaTotali.getProduzioneHaDanno() == BigDecimal.ZERO) rigaTotali.setProduzioneHaDanno(null);
//	if (rigaTotali.getProduzioneTotaleDanno() == BigDecimal.ZERO) rigaTotali.setProduzioneTotaleDanno(null);
//	if (rigaTotali.getPrezzoDanneggiato() == BigDecimal.ZERO) rigaTotali.setPrezzoDanneggiato(null);
//	if (rigaTotali.getTotaleEuroPlvEff() == BigDecimal.ZERO) rigaTotali.setTotaleEuroPlvEff(null);
//	if (rigaTotali.getImportoRimborso().equals(BigDecimal.ZERO)) rigaTotali.setImportoRimborso(null);
//	if (rigaTotali.getTotConRimborsi() == BigDecimal.ZERO) rigaTotali.setTotConRimborsi(null);	 
//
//	BigDecimal div2 = BigDecimal.valueOf(rigaTotali.getTotConRimborsi().doubleValue() / rigaTotali.getTotaleEuroPlvOrd().doubleValue());	    
//	BigDecimal sub = IuffiUtils.NUMBERS.subtract(new BigDecimal(1), div2);
//	BigDecimal res = IuffiUtils.NUMBERS.multiply(sub, new BigDecimal(100));
//	rigaTotali.setPercentualeDanno(res);
//	
//	
//	return rigaTotali;
//    }
    
}
