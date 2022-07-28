package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.AnnoCensitoDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.DistrettoDTO;
import it.csi.iuffi.iuffiweb.dto.IpotesiPrelievoDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

public class CUIUFFI319InserisciModificaOgurBase extends BaseController
{

  public String validateAndUpdate(Long idOgur, Long idDistretto, IQuadroIuffiEJB quadroIuffiEJB, Model model, HttpServletRequest request) throws InternalUnexpectedException{
    
    Errors errors = new Errors();
    DistrettoDTO distretto = new DistrettoDTO();
    OgurDTO ogur = quadroIuffiEJB.getOgur(idOgur, true);
    model.addAttribute("ogur",ogur);
    
    if(idDistretto!=null)
    if(ogur!=null && ogur.getDistretti()!=null && !ogur.getDistretti().isEmpty())
      for(DistrettoDTO d : ogur.getDistretti())
      {
        if(d.getIdDistretto().longValue()==idDistretto)
          distretto = d;
      }
    
    String nominDistretto = request.getParameter("nominDistretto");
    errors.validateMandatoryFieldMaxLength(nominDistretto, "nominDistretto", 250);
    BigDecimal superficieDistretto = errors.validateMandatoryBigDecimal(request.getParameter("superficieDistretto"), "superficieDistretto", 2);
    BigDecimal superfVenabDistretto = errors.validateMandatoryBigDecimal(request.getParameter("superfVenabDistretto"), "superfVenabDistretto", 2);
    BigDecimal sus = errors.validateMandatoryBigDecimal(request.getParameter("sus"), "sus", 2);   
    distretto.setNominDistretto(nominDistretto);
    distretto.setSuperficieDistretto(superficieDistretto);
    distretto.setSuperfVenabDistretto(superfVenabDistretto);
    distretto.setSus(sus);
    
    //VALIDATE ANNI CENSITI
    for(AnnoCensitoDTO annoCensito : distretto.getAnniCensiti())
    {
      int anno = annoCensito.getAnno();
      BigDecimal totCensito = errors.validateMandatoryBigDecimal(request.getParameter("totCensito_"+anno), "totCensito_"+anno, 2);
      BigDecimal superfCensita = errors.validateMandatoryBigDecimal(request.getParameter("superfCensita_"+anno), "superfCensita_"+anno, 2);
      BigDecimal pianoNumerico = errors.validateMandatoryBigDecimal(request.getParameter("pianoNumerico_"+anno), "pianoNumerico_"+anno, 2);
      BigDecimal totPrelevato = errors.validateMandatoryBigDecimal(request.getParameter("totPrelevato_"+anno), "totPrelevato_"+anno, 2);
      distretto.getAnnoCensito(anno).setTotCensito(totCensito);
      distretto.getAnnoCensito(anno).setSuperfCensita(superfCensita);
      distretto.getAnnoCensito(anno).setPianoNumerico(pianoNumerico);
      distretto.getAnnoCensito(anno).setTotPrelevato(totPrelevato);
      
      //VALIDATE DANNI CAUSATI
      BigDecimal danniCausati = errors.validateMandatoryBigDecimal(request.getParameter("danniCausati_"+anno), "danniCausati_"+anno, 2);
      distretto.getAnnoCensito(anno).setDanniCausati(danniCausati);
      //VALIDATE INCIDENTI STRADALI
      BigDecimal incidentiStradali = errors.validateMandatoryBigDecimal(request.getParameter("incidentiStradali_"+anno), "incidentiStradali_"+anno, 2);
      distretto.getAnnoCensito(anno).setIncidentiStradali(incidentiStradali);
    }
    //VALIDATE CENSIMENTO
    int annoCensimento = distretto.getCensimento().getAnno();
    BigDecimal densitaSupCens = errors.validateMandatoryBigDecimal(request.getParameter("densitaSupCens_"+annoCensimento), "densitaSupCens_"+annoCensimento, 2);
    BigDecimal densitaCapiSus = errors.validateMandatoryBigDecimal(request.getParameter("densitaCapiSus_"+annoCensimento), "densitaCapiSus_"+annoCensimento, 2);
    BigDecimal densitaObiettivo = errors.validateMandatoryBigDecimal(request.getParameter("densitaObiettivo_"+annoCensimento), "densitaObiettivo_"+annoCensimento, 2);
    BigDecimal consistenzaPotenz = errors.validateMandatoryBigDecimal(request.getParameter("consistenzaPotenz_"+annoCensimento), "consistenzaPotenz_"+annoCensimento, 2);
    distretto.getCensimento().setDensitaSupCens(densitaSupCens);
    distretto.getCensimento().setDensitaCapiSus(densitaCapiSus);
    distretto.getCensimento().setDensitaObiettivo(densitaObiettivo);
    distretto.getCensimento().setConsistenzaPotenz(consistenzaPotenz);
    
    //VALIDATE IPOTESI PRELIEVO
    for(IpotesiPrelievoDTO ipotesi : distretto.getIpotesiPrelievo())
    {
      int anno = ipotesi.getAnno();
      BigDecimal percentuale = errors.validateMandatoryBigDecimal(request.getParameter("percentuale_"+anno), "percentuale_"+anno, 2);
      ipotesi.setPercentuale(percentuale);
    }
    
    if(errors.isEmpty())
    {
      quadroIuffiEJB.inserisciModificaDistrettoOgur(getIdProcedimentoOggetto(request.getSession()), idDistretto, idOgur, distretto, getLogOperationOggettoQuadroDTO(request.getSession()));
      return "redirect:../cuiuffi319v/index_" + idOgur + ".do";
    }
    
    List<DecodificaDTO<Long>> specieOgur = quadroIuffiEJB.getElencoSpecieOgur(false, getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("specieOgur", specieOgur);  
    model.addAttribute("preferRequest", true);
    model.addAttribute("ogur",quadroIuffiEJB.getOgur(idOgur, true));
    model.addAttribute("errors", errors);
    model.addAttribute("distretto",distretto);
    model.addAttribute("currentYear",LocalDate.now().getYear());
    
    return "ogur/distretti/inseriscidistretto";
  }
}
