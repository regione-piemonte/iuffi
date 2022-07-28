package it.csi.iuffi.iuffiweb.presentation.quadro.coltureaziendali;

import java.math.BigDecimal;
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
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.UtilizzoReseDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-305-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi305m")
public class CUIUFFI305MModificaColtureAziendali extends BaseController {
    @Autowired
    protected IQuadroIuffiEJB quadroIuffiEJB = null;
    private String paginaModifica = "coltureaziendali/modificaColtureAziendali";

    
    private final String fieldNameFlagDanneggiato = "txtFlagDanneggiato_";

    private final String fieldNameProduzioneHaOrd = "txtProduzioneHaOrd_";
    private final String fieldNamePrezzoOrd = "txtPrezzoOrd_";
    private final String fieldNameGiornateLavorateHaOrd = "txtGiornateLavorateHaOrd_";
    private final String fieldNameNoteOrd = "txtNoteOrd_";
    
    private final String fieldNameProduzioneHaDannoEff = "txtProduzioneHaDannoEff_";
    private final String fieldNamePrezzoDanneggiatoEff = "txtPrezzoDanneggiatoEff_";
    private final String fieldNameImpportoRimborsoEff = "txtImportoRimborsoEff_";
    
    private final BigDecimal zero = new BigDecimal("0.00");
    private final BigDecimal _999999_99 = new BigDecimal("999999.99");
    
    
    @RequestMapping(value = "/index_{idSuperficieColtura}", method = RequestMethod.GET)
    public String index(HttpSession session, Model model, HttpServletRequest request, @PathVariable("idSuperficieColtura") long idSuperficieColtura) throws InternalUnexpectedException {
	long[] arrayIdSuperficieColtura = new long[] { idSuperficieColtura };
	return modificaColtureAziendaliGenerico(request, session, model, arrayIdSuperficieColtura);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String index(HttpSession session, Model model, HttpServletRequest request) throws InternalUnexpectedException {
	long[] arrayIdSuperficieColtura = IuffiUtils.ARRAY.toLong(request.getParameterValues("idSuperficieColtura"));
	return modificaColtureAziendaliGenerico(request, session, model, arrayIdSuperficieColtura);
    }

    @RequestMapping(value = "/modifica", method = RequestMethod.POST)
    public String modifica(HttpSession session, Model model, HttpServletRequest request) throws InternalUnexpectedException {
	Errors errors = new Errors();
	long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	long[] arrayIdSuperficieColtura = IuffiUtils.ARRAY.toLong(request.getParameterValues("idSuperficieColtura"));
	String paginaDaCaricare = null;

	Map<Long, ColtureAziendaliDettaglioDTO> mapColtureAziendali = new HashMap<Long, ColtureAziendaliDettaglioDTO>();
	List<ColtureAziendaliDettaglioDTO> listColtureAziendaliModificate = new ArrayList<ColtureAziendaliDettaglioDTO>();
	List<ColtureAziendaliDettaglioDTO> listColtureAziendali = quadroIuffiEJB.getListColtureAziendali(idProcedimentoOggetto, arrayIdSuperficieColtura);
	for (ColtureAziendaliDettaglioDTO coltura : listColtureAziendali) {
	    mapColtureAziendali.put(coltura.getIdSuperficieColtura(), coltura);
	}

	for (long idSuperficieColtura : arrayIdSuperficieColtura) {
	    String fieldNameFlagDanneggiato = this.fieldNameFlagDanneggiato + idSuperficieColtura;
	    String fieldNameProduzioneHaOrd = this.fieldNameProduzioneHaOrd + idSuperficieColtura;
	    String fieldNamePrezzoOrd = this.fieldNamePrezzoOrd + idSuperficieColtura;
	    String fieldNameGiornateLavorateHaOrd = this.fieldNameGiornateLavorateHaOrd + idSuperficieColtura;
	    String fieldNameNoteOrd = this.fieldNameNoteOrd + idSuperficieColtura;
	    String fieldNameProduzioneHaDannoEff = this.fieldNameProduzioneHaDannoEff + idSuperficieColtura;
	    String fieldNamePrezzoDanneggiatoEff = this.fieldNamePrezzoDanneggiatoEff + idSuperficieColtura;
	    String fieldNameImportoRimborsoEff = this.fieldNameImpportoRimborsoEff + idSuperficieColtura;
	    
	    String fieldFlagDanneggiato = request.getParameter(fieldNameFlagDanneggiato);
	    String fieldProduzioneHaOrd = request.getParameter(fieldNameProduzioneHaOrd);
	    String fieldPrezzoOrd = request.getParameter(fieldNamePrezzoOrd);
	    String fieldGiornateLavorateHaOrd = request.getParameter(fieldNameGiornateLavorateHaOrd);
	    String fieldNoteOrd = request.getParameter(fieldNameNoteOrd);
	    String fieldProduzioneHaDannoEff = request.getParameter(fieldNameProduzioneHaDannoEff);
	    String fieldPrezzoDanneggiatoEff = request.getParameter(fieldNamePrezzoDanneggiatoEff);
	    String fieldImportoRimborsoEff = request.getParameter(fieldNameImportoRimborsoEff);

	    
	    //inizio controlli validità
	    BigDecimal produzioneHaOrd = errors.validateMandatoryBigDecimal(fieldProduzioneHaOrd, fieldNameProduzioneHaOrd, 2);
	    BigDecimal prezzoOrd = errors.validateMandatoryBigDecimal(fieldPrezzoOrd, fieldNamePrezzoOrd, 2);
	    BigDecimal giornateLavorateHaOrd = errors.validateMandatoryBigDecimal(fieldGiornateLavorateHaOrd, fieldNameGiornateLavorateHaOrd, 0);
	    BigDecimal produzioneHaDannoEff = errors.validateMandatoryBigDecimalInRange(fieldProduzioneHaDannoEff, fieldNameProduzioneHaDannoEff, 2, zero, _999999_99);
	    BigDecimal prezzoDanneggiatoEff = errors.validateMandatoryBigDecimalInRange(fieldPrezzoDanneggiatoEff, fieldNamePrezzoDanneggiatoEff, 2, zero, _999999_99);
	    BigDecimal importoRimborsoEff = errors.validateBigDecimalInRange(fieldImportoRimborsoEff, fieldNameImportoRimborsoEff, 2, zero, _999999_99);
	    
	    if (errors.isEmpty()) {
		UtilizzoReseDTO utilizzoRese = quadroIuffiEJB.getUtilizzoRese(mapColtureAziendali.get(idSuperficieColtura));
		boolean anomalia = false;
		if (produzioneHaOrd.compareTo(utilizzoRese.getResaMin()) < 0 || produzioneHaOrd.compareTo(utilizzoRese.getResaMax()) > 0) anomalia = true;
		if (prezzoOrd.compareTo(utilizzoRese.getPrezzoMin()) < 0 || prezzoOrd.compareTo(utilizzoRese.getPrezzoMax()) > 0) anomalia = true;
		if (anomalia && !errors.validateFieldLength(fieldNoteOrd, fieldNameNoteOrd, 1, 4000, true)) {
		    errors.addError(fieldNameNoteOrd, "Siccome i valori inseriti per 'Q.li/ha' e/o per 'Prezzo al q.le' della PLV ordinaria sforano dai range consentiti (per 'Q.li/ha' min: "+IuffiUtils.FORMAT.formatDecimal2(utilizzoRese.getResaMin())+" e max: "+IuffiUtils.FORMAT.formatDecimal2(utilizzoRese.getResaMax())+" invece per 'Prezzo al q.le' min: "+IuffiUtils.FORMAT.formatDecimal2(utilizzoRese.getPrezzoMin())+" € e max: "+IuffiUtils.FORMAT.formatDecimal2(utilizzoRese.getPrezzoMax())+" €) è obbligatorio valorizzare il campo 'Note'");
		}
		if (giornateLavorateHaOrd.compareTo(utilizzoRese.getGiornateLavorateMin()) < 0 || giornateLavorateHaOrd.compareTo(utilizzoRese.getGiornateLavorateMax()) > 0) {
		    errors.addError(fieldNameGiornateLavorateHaOrd, "Il valore delle giornate lavorate non rispetta i valori indicati nel range: (min: "+IuffiUtils.FORMAT.formatDecimal2(utilizzoRese.getGiornateLavorateMin())+", max: "+IuffiUtils.FORMAT.formatDecimal2(utilizzoRese.getGiornateLavorateMax())+")");
		}
	    }
	    
	    if (errors.isEmpty()) {
		ColtureAziendaliDettaglioDTO colturaModificata = mapColtureAziendali.get(idSuperficieColtura);
		if (fieldFlagDanneggiato != null && fieldFlagDanneggiato.equalsIgnoreCase("S")) colturaModificata.setFlagDanneggiato("S");
		else colturaModificata.setFlagDanneggiato("N");
		colturaModificata.setProduzioneHa(produzioneHaOrd);
		colturaModificata.setPrezzo(prezzoOrd);
		colturaModificata.setGiornateLavorateHa(giornateLavorateHaOrd);
		colturaModificata.setNote(fieldNoteOrd);
		colturaModificata.setProduzioneHaDanno(produzioneHaDannoEff);
		colturaModificata.setPrezzoDanneggiato(prezzoDanneggiatoEff);
		colturaModificata.setImportoRimborso(importoRimborsoEff);
		listColtureAziendaliModificate.add(colturaModificata);
	    }
	}
	if (errors.addToModelIfNotEmpty(model)) {
	    for (ColtureAziendaliDettaglioDTO coltureAziendaliDettaglioDTO : listColtureAziendali) {
		coltureAziendaliDettaglioDTO.setIsModificaPrezzoDanneggiatoDisabled(false);
		coltureAziendaliDettaglioDTO.setIsModificaProduzioneHaDannoDisabled(false);
		String fieldFlagDanneggiato = request.getParameter(this.fieldNameFlagDanneggiato + coltureAziendaliDettaglioDTO.getIdSuperficieColtura());
		if (fieldFlagDanneggiato != null) coltureAziendaliDettaglioDTO.setFlagDanneggiato(fieldFlagDanneggiato);
		else coltureAziendaliDettaglioDTO.setFlagDanneggiato("N");
	    }
	    model.addAttribute("listColtureAziendali", listColtureAziendali);
	    model.addAttribute("preferRequest", Boolean.TRUE);
	    paginaDaCaricare = paginaModifica;
	} else {
	    paginaDaCaricare = "redirect:../cuiuffi305l/index.do";
	    LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
	    quadroIuffiEJB.updateColtureAziendali(idProcedimentoOggetto, logOperationOggettoQuadroDTO, listColtureAziendaliModificate);
	}
	return paginaDaCaricare;
    }

    private String modificaColtureAziendaliGenerico(HttpServletRequest request, HttpSession session, Model model, long[] arrayIdSuperficieColtura) throws InternalUnexpectedException {
	long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
	List<ColtureAziendaliDettaglioDTO> listColtureAziendali = quadroIuffiEJB.getListColtureAziendali(idProcedimentoOggetto, arrayIdSuperficieColtura);
	for (ColtureAziendaliDettaglioDTO coltureAziendaliDettaglioDTO : listColtureAziendali) {
	    //commentato perchè la modifica di plv eff è sempre possibile...nel caso bisognasse di nuovo implementare tale controllo 
	    //scommentare la parte sotto e copiarla anche nell'id dell'addToModelIfNotEmpty sopra
//	     Integer count =
//	     quadroIuffiEJB.getCountBandoUtilizzoDanno(coltureAziendaliDettaglioDTO.getIdBando().longValue(),
	    // coltureAziendaliDettaglioDTO.getExtIdUtilizzo().longValue());
	    // if (count != 0) {
	    coltureAziendaliDettaglioDTO.setIsModificaPrezzoDanneggiatoDisabled(false);
	    coltureAziendaliDettaglioDTO.setIsModificaProduzioneHaDannoDisabled(false);
	    // } else {
	    // coltureAziendaliDettaglioDTO.setIsModificaPrezzoDanneggiatoDisabled(true);
	    // coltureAziendaliDettaglioDTO.setIsModificaProduzioneHaDannoDisabled(true);
	    // }
//	    String fieldFlagDanneggiato = request.getParameter(this.fieldNameFlagDanneggiato + coltureAziendaliDettaglioDTO.getIdSuperficieColtura());
//	    if (fieldFlagDanneggiato != null) coltureAziendaliDettaglioDTO.setFlagDanneggiato(fieldFlagDanneggiato);
//	    else coltureAziendaliDettaglioDTO.setFlagDanneggiato("N");
	}
	model.addAttribute("listColtureAziendali", listColtureAziendali);
	return paginaModifica;
    }

}
