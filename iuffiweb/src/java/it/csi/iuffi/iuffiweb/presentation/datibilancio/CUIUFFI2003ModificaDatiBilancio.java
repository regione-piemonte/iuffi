package it.csi.iuffi.iuffiweb.presentation.datibilancio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.datibilancio.DatiBilancioDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi2003m")
@IuffiSecurity(value = "CU-IUFFI-2003-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI2003ModificaDatiBilancio extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-2003-M";
  
  public static final List<DecodificaDTO<String>> tipoDomandaList = new ArrayList<>( Arrays.asList(new DecodificaDTO<String>("N","Nuova domanda"), new DecodificaDTO<String>("R","Rinnovo domanda")));
  public static final List<DecodificaDTO<String>> flagRitenAccontoList = new ArrayList<>( Arrays.asList(new DecodificaDTO<String>("N","No"), new DecodificaDTO<String>("S","Sì")));
  
  @Autowired
  IQuadroIuffiEJB quadroEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
    DatiBilancioDTO datiBilancio = quadroEJB.getDatiBilancio(procedimentoOggetto.getIdProcedimentoOggetto()); 
    if(datiBilancio == null) {
      datiBilancio = new DatiBilancioDTO();
      Long idUte = quadroEJB.getIdUteMaxSuperficie(procedimentoOggetto.getExtIdDichiarazioneConsistenza(),true);
      if(idUte == null) {
        idUte = quadroEJB.getIdUteMaxSuperficie(procedimentoOggetto.getExtIdDichiarazioneConsistenza(),false);
      }
      
      if(idUte!=null) {
        model.addAttribute("elencoZoneAltimetriche",null);
        DecodificaDTO<Long> zonaAltimetrica = quadroEJB.getZonAltimetricaByIdUte(idUte);
        if(zonaAltimetrica!=null) {
          datiBilancio.setExtIdZonaAltimetrica(zonaAltimetrica.getId());
          datiBilancio.setDescZonaAltimetrica(zonaAltimetrica.getDescrizione());
        }
      }
      else {
        List<DecodificaDTO<Long>> elenco = quadroEJB.getZoneAltimetricheByIdAzienda(getTestataProcedimento(session).getIdAzienda());
        if(elenco.size()>1) {
          model.addAttribute("elencoZoneAltimetriche", elenco);
        }else {
          model.addAttribute("elencoZoneAltimetriche",null);
          datiBilancio.setExtIdZonaAltimetrica(elenco.get(0).getId());
          datiBilancio.setDescZonaAltimetrica(elenco.get(0).getDescrizione());
        }
      }
    }
    model.addAttribute("tipoDomandaList", tipoDomandaList);
    model.addAttribute("flagRitenAccontoList", flagRitenAccontoList);
    model.addAttribute("datiBilancio", datiBilancio);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB, procedimentoOggetto.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "datiBilancio/modifica";
  }
  
  @RequestMapping("/confermaModifica")
  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  { 
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    //controllo campi obbligatori
    Errors errors = new Errors();
    //String descZonaAltimetrica = IuffiUtils.STRING.trim(request.getParameter("descZonaAltimetrica"));
    
    String idZonaAltimetrica = request.getParameter("idZonaAltimetrica");
    String dataScadPrestitoPrec = IuffiUtils.STRING.trim(request.getParameter("dataScadPrestitoPrec"));
    String flagRitenAcconto = IuffiUtils.STRING.trim(request.getParameter("flagRitenAcconto"));
    String motivazioneRitAcconto = IuffiUtils.STRING.trim(request.getParameter("motivazioneRitAcconto"));
    String tipoDomanda = IuffiUtils.STRING.trim(request.getParameter("tipoDomanda"));
    String dataUltimoBilancio = IuffiUtils.STRING.trim(request.getParameter("dataUltimoBilancio"));
    String impCreditoClienti = IuffiUtils.STRING.trim(request.getParameter("impCreditoClienti"));
    String impFatturato = IuffiUtils.STRING.trim(request.getParameter("impFatturato"));
    //String tempoEsposizBenef = IuffiUtils.STRING.trim(request.getParameter("tempoEsposizBenef"));
    String tempoEsposizBenef = IuffiUtils.STRING.trim(request.getParameter("tempoEsposizBenef"));
    String dataDeliberaIntervento = IuffiUtils.STRING.trim(request.getParameter("dataDeliberaIntervento"));

    String importoMateriePrime = IuffiUtils.STRING.trim(request.getParameter("importoMateriePrime"));
    String importoServizi = IuffiUtils.STRING.trim(request.getParameter("importoServizi"));
    String importoBeniTerzi = IuffiUtils.STRING.trim(request.getParameter("importoBeniTerzi"));
    String importoPersSalari = IuffiUtils.STRING.trim(request.getParameter("importoPersSalari"));
    String importoPersOneri = IuffiUtils.STRING.trim(request.getParameter("importoPersOneri"));
    
    Date dataScadPrestitoPrecDate = errors.validateMandatoryDate(dataScadPrestitoPrec, "dataScadPrestitoPrec", true);
    errors.validateMandatory(flagRitenAcconto, "flagRitenAcconto");
    if(StringUtils.isNotBlank(flagRitenAcconto) && "N".equals(flagRitenAcconto))
      errors.validateMandatory(motivazioneRitAcconto, "motivazioneRitAcconto");
   
    errors.validateMandatory(tipoDomanda, "tipoDomanda");
    Date dataUltimoBilancioDate = errors.validateMandatoryDate(dataUltimoBilancio, "dataUltimoBilancio", true);
    
    BigDecimal importoMateriePrimeBD = errors.validateMandatoryBigDecimal(importoMateriePrime, "importoMateriePrime", 2);
    BigDecimal importoServiziBD = errors.validateMandatoryBigDecimal(importoServizi, "importoServizi", 2);
    BigDecimal importoBeniTerziBD = errors.validateMandatoryBigDecimal(importoBeniTerzi, "importoBeniTerzi", 2);
    BigDecimal importoPersSalariBD = errors.validateMandatoryBigDecimal(importoPersSalari, "importoPersSalari", 2);
    BigDecimal importoPersOneriBD = errors.validateMandatoryBigDecimal(importoPersOneri, "importoPersOneri", 2);
    
    BigDecimal impCreditoClientiBD = errors.validateMandatoryBigDecimal(impCreditoClienti, "impCreditoClienti", 2);
    BigDecimal impFatturatoBD = errors.validateMandatoryBigDecimal(impFatturato, "impFatturato", 2);
    
    Long tempoEsposizBenefLong = errors.validateMandatoryLong(tempoEsposizBenef, "tempoEsposizBenef");
    if(tempoEsposizBenefLong!=null && tempoEsposizBenefLong.longValue() > 12) {
      errors.addError("tempoEsposizBenef", "Non è possibile indicare un valore maggiore a 12 mesi");
    }
    Date dataDeliberaInterventoDate = errors.validateMandatoryDate(dataDeliberaIntervento, "dataDeliberaIntervento", true);
    
    DatiBilancioDTO datiBilancio = new DatiBilancioDTO();
    datiBilancio.setDataDeliberaIntervento(dataDeliberaInterventoDate);
    datiBilancio.setDataScadPrestitoPrec(dataScadPrestitoPrecDate);
    datiBilancio.setDataUltimoBilancio(dataUltimoBilancioDate);
    //datiBilancio.setDescZonaAltimetrica(cap);
    datiBilancio.setFlagRitenAcconto(flagRitenAcconto);
    datiBilancio.setImpCreditoClienti(impCreditoClientiBD);
    datiBilancio.setImpFatturato(impFatturatoBD);
    datiBilancio.setMotivazioneRitAcconto(motivazioneRitAcconto);
    datiBilancio.setTempoEsposizBenef(tempoEsposizBenefLong);     
    datiBilancio.setTipoDomanda(tipoDomanda); 
    
    datiBilancio.setImportoMateriePrime(importoMateriePrimeBD);
    datiBilancio.setImportoBeniTerzi(importoBeniTerziBD);
    datiBilancio.setImportoPersOneri(importoPersOneriBD);
    datiBilancio.setImportoPersSalari(importoPersSalariBD);
    datiBilancio.setImportoServizi(importoServiziBD);

    if (importoMateriePrimeBD != null && importoServiziBD != null
        && importoBeniTerziBD != null && importoPersSalariBD != null
        && importoPersOneriBD != null)
    {
      BigDecimal impCapitaleAnticAmmissBD = importoMateriePrimeBD.add(importoServiziBD).add(importoBeniTerziBD).add(importoPersSalariBD).add(importoPersOneriBD);
      datiBilancio.setImpCapitaleAnticAmmiss(impCapitaleAnticAmmissBD);
      
      if(tempoEsposizBenefLong!=null) {
        double mult = (double) tempoEsposizBenefLong / 12.00;
        BigDecimal res = impCapitaleAnticAmmissBD.multiply(new BigDecimal(mult));
        res = res.setScale(2, BigDecimal.ROUND_HALF_UP);
        datiBilancio.setImportoTotConcedibile(res);
      }
      
    } else {
      datiBilancio.setImpCapitaleAnticAmmiss(null);
    }
    
    if(impFatturatoBD!=null && impCreditoClientiBD!=null) {
      double uno = impCreditoClientiBD.doubleValue();
      double due = impFatturatoBD.doubleValue();
      double divide = uno / due * 365;
      long res = Math.round(divide);
      datiBilancio.setTempoEsposizDaBilancio(res);
    }else {
      datiBilancio.setTempoEsposizDaBilancio(null);
    } 
    
    Long idUte = quadroEJB.getIdUteMaxSuperficie(procedimentoOggetto.getExtIdDichiarazioneConsistenza(),true);
    if(idUte == null) {
      idUte = quadroEJB.getIdUteMaxSuperficie(procedimentoOggetto.getExtIdDichiarazioneConsistenza(),false);
    }
    if(idUte!=null) {
      model.addAttribute("elencoZoneAltimetriche",null);
      DecodificaDTO<Long> zonaAltimetrica = quadroEJB.getZonAltimetricaByIdUte(idUte);
      if(zonaAltimetrica!=null) {
        datiBilancio.setExtIdZonaAltimetrica(zonaAltimetrica.getId());
        datiBilancio.setDescZonaAltimetrica(zonaAltimetrica.getDescrizione());
      }
    }
    else {
      errors.validateMandatory(idZonaAltimetrica, "idZonaAltimetrica");
      
      List<DecodificaDTO<Long>> elenco = quadroEJB.getZoneAltimetricheByIdAzienda(getTestataProcedimento(session).getIdAzienda());
      if(elenco.size()>1) {
        
        if(errors.isEmpty())
        {
          for(DecodificaDTO<Long> item : elenco) {
            if(item.getId().longValue() == Long.parseLong(idZonaAltimetrica)) {
              datiBilancio.setExtIdZonaAltimetrica(item.getId());
              datiBilancio.setDescZonaAltimetrica(item.getDescrizione());
            }
          }
        }
        
        model.addAttribute("elencoZoneAltimetriche", elenco);
      }else {
        model.addAttribute("elencoZoneAltimetriche",null);
        datiBilancio.setExtIdZonaAltimetrica(elenco.get(0).getId());
        datiBilancio.setDescZonaAltimetrica(elenco.get(0).getDescrizione());
      }
    }

    if (errors.isEmpty()){
      datiBilancio.setIdProcedimentoOggetto(idProcedimentoOggetto);
      quadroEJB.insertDatiBilancio(datiBilancio, getLogOperationOggettoQuadroDTO(session));
      return "redirect:../cuiuffi2003d/index.do";
    }
    
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
    model.addAttribute("errors", errors);
    model.addAttribute("tipoDomandaList", tipoDomandaList);
    model.addAttribute("prefReqValues", Boolean.TRUE);
    
    model.addAttribute("flagRitenAccontoList", flagRitenAccontoList);
    model.addAttribute("datiBilancio", datiBilancio);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB, procedimentoOggetto.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "datiBilancio/modifica";
  }
  
}