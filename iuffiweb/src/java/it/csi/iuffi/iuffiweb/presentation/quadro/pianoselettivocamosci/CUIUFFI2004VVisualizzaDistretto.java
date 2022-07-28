package it.csi.iuffi.iuffiweb.presentation.quadro.pianoselettivocamosci;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.DataCensimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.Distretto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2004-V", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "cuiuffi2004v")
public class CUIUFFI2004VVisualizzaDistretto extends BaseController
{

  public static final String CU_NAME = "CU-IUFFI-2004-V";
  public static final long idSpecie= 1;
  
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @Autowired
  IQuadroEJB quadroEJB;
  
  @RequestMapping(value = "/index_{idDistretto}_{tipo}_{idPianoDistrettoOgur}", method = RequestMethod.GET)
  public String index(@PathVariable("tipo") String tipo, @PathVariable("idDistretto") long idDistretto, @PathVariable("idPianoDistrettoOgur") long idPianoDistrettoOgur, Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    List<Distretto> distretto = null;
    if(tipo.equalsIgnoreCase("distretto")) {
      distretto = quadroIuffiEJB.getElencoDistrettiOgur(false, "("+ String.valueOf(idDistretto) + ")", idSpecie, idPianoDistrettoOgur, idProcedimentoOggetto);
    }
    else {
      distretto = quadroIuffiEJB.getElencoDistrettiOgur(true, "("+ String.valueOf(idDistretto) + ")", idSpecie, idPianoDistrettoOgur, idProcedimentoOggetto);
    }
    if(distretto.get(0) != null) {
      long idPiano = distretto.get(0).getIdPianoDistrettoOgur();
      List<DataCensimento> date = quadroIuffiEJB.getDateCensimento(idPiano, idSpecie);
      model.addAttribute("date", date);
      model.addAttribute("dateInserite", date != null);
    }
    if(distretto.get(0).getMaxCapiPrelievo() == null || distretto.get(0).getMaxCapiPrelievo().equalsIgnoreCase("0")) {
      distretto.get(0).setPercentualeTotale(new BigDecimal(0));
    }
    else {
      distretto.get(0).setPercentualeTotale(new BigDecimal(distretto.get(0).getTotalePrelievo()).divide(new BigDecimal(distretto.get(0).getMaxCapiPrelievo()), 2, RoundingMode.HALF_UP));
    }
    
    model.addAttribute("distretto",distretto.get(0));
    return "pianoselettivocamosci/dettagliodistretto";
  }
  

}
