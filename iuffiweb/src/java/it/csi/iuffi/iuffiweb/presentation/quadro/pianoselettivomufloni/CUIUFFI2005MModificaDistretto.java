package it.csi.iuffi.iuffiweb.presentation.quadro.pianoselettivomufloni;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.DataCensimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.Distretto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2005-M", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "cuiuffi2005m")
public class CUIUFFI2005MModificaDistretto extends BaseController {

  public static final String CU_NAME = "CU-IUFFI-2005-M";
  public static final long idSpecie= 5;
  
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
      int j = 1;
      List<DataCensimento> date = quadroIuffiEJB.getDateCensimento(idPiano, idSpecie);
      if(date != null) {
        for (DataCensimento dataCensimento : date)
        {
          dataCensimento.setId(new Long(j));
          j++;
        }
      }      
      model.addAttribute("date", date);
      model.addAttribute("dateInserite", date != null);   
      List<DataCensimento> dateNew = new ArrayList<DataCensimento>();
      for(int i = date != null ? date.size()+1 : 1; i<=100; i++) {
        DataCensimento nuova = new DataCensimento();       
        nuova.setId(new Long(i));        
        dateNew.add(nuova);
      }
      model.addAttribute("dateNew", dateNew);
      model.addAttribute("numerorigheCertificate",date != null ? date.size() : 1);
    }
    List<DecodificaDTO<Long>> metodi = quadroIuffiEJB.getMetodiCensimento(idSpecie);
    if(distretto.get(0).getMaxCapiPrelievo() == null || distretto.get(0).getMaxCapiPrelievo().equalsIgnoreCase("0") || distretto.get(0).getTotalePrelievo() == null || distretto.get(0).getTotalePrelievo().equalsIgnoreCase("0")) {
      distretto.get(0).setPercentualeTotale(new BigDecimal(0));
    }
    else {
      distretto.get(0).setPercentualeTotale(new BigDecimal(distretto.get(0).getTotalePrelievo()).divide(new BigDecimal(distretto.get(0).getMaxCapiPrelievo()), 2, RoundingMode.HALF_UP));
    }   
    model.addAttribute("distretto",distretto.get(0));    
    model.addAttribute("metodi", metodi);
    model.addAttribute("idDistretto", idDistretto);
    model.addAttribute("tipo", tipo);
    return "pianoselettivomufloni/modificadistretto";
  }
  
  @RequestMapping("/confermaModifica")
  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException { 
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    int max = (IuffiUtils.STRING.trim(request.getParameter("max")) != null && !IuffiUtils.STRING.trim(request.getParameter("max")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("max"))) : 0);
    String idPiano = IuffiUtils.STRING.trim(request.getParameter("idPiano"));
    //controllo campi obbligatori
    Errors errors = new Errors();
    int censito1 = (IuffiUtils.STRING.trim(request.getParameter("censito1")) != null && !IuffiUtils.STRING.trim(request.getParameter("censito1")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("censito1"))) : 0);
    int censito2 = (IuffiUtils.STRING.trim(request.getParameter("censito2")) != null && !IuffiUtils.STRING.trim(request.getParameter("censito2")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("censito2"))) : 0);
    int censito3 = (IuffiUtils.STRING.trim(request.getParameter("censito3")) != null && !IuffiUtils.STRING.trim(request.getParameter("censito3")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("censito3"))) : 0);
    int censito4 = (IuffiUtils.STRING.trim(request.getParameter("censito4")) != null && !IuffiUtils.STRING.trim(request.getParameter("censito4")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("censito4"))) : 0);
    int indetC = (IuffiUtils.STRING.trim(request.getParameter("indetC")) != null && !IuffiUtils.STRING.trim(request.getParameter("indetC")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("indetC"))) : 0);
    int prelievo1 = (IuffiUtils.STRING.trim(request.getParameter("prelievo1")) != null && !IuffiUtils.STRING.trim(request.getParameter("prelievo1")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("prelievo1"))) : 0);
    int prelievo2 = (IuffiUtils.STRING.trim(request.getParameter("prelievo2")) != null && !IuffiUtils.STRING.trim(request.getParameter("prelievo2")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("prelievo2"))) : 0);
    int prelievo3 = (IuffiUtils.STRING.trim(request.getParameter("prelievo3")) != null && !IuffiUtils.STRING.trim(request.getParameter("prelievo3")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("prelievo3"))) : 0);
    int prelievo4 = (IuffiUtils.STRING.trim(request.getParameter("prelievo4")) != null && !IuffiUtils.STRING.trim(request.getParameter("prelievo4")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("prelievo4"))) : 0);
    int indeterminatiP = (IuffiUtils.STRING.trim(request.getParameter("indp")) != null && !IuffiUtils.STRING.trim(request.getParameter("indp")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("indp"))) : 0);
    BigDecimal percentuale1 = null;
    BigDecimal percentuale2 = null;
    BigDecimal percentuale3 = null;
    BigDecimal percentuale4 = null;
    if(censito1 != 0) {
      percentuale1 = (new BigDecimal(prelievo1).divide(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP), 2, RoundingMode.HALF_UP));
    }
    else {
      percentuale1 = new BigDecimal("0");
    }
    if(censito2 != 0) {
      percentuale2 = (new BigDecimal(prelievo2).divide(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP), 2, RoundingMode.HALF_UP));
    }
    else {
      percentuale2 = new BigDecimal("0");
    }
    if(censito3 != 0) {
      percentuale3 = (new BigDecimal(prelievo3).divide(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP), 2, RoundingMode.HALF_UP));
    }
    else {
      percentuale3 = new BigDecimal("0");
    }
    if(censito4 != 0) {
      percentuale4 = (new BigDecimal(prelievo4).divide(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP), 2, RoundingMode.HALF_UP));
    }
    else {
      percentuale4 = new BigDecimal("0");
    }   
    BigDecimal da1 = new BigDecimal(IuffiUtils.STRING.trim(request.getParameter("da1")));
    BigDecimal da2 = new BigDecimal(IuffiUtils.STRING.trim(request.getParameter("da2")));
    BigDecimal da3 = new BigDecimal(IuffiUtils.STRING.trim(request.getParameter("da3")));
    BigDecimal da4 = new BigDecimal(IuffiUtils.STRING.trim(request.getParameter("da4")));
    BigDecimal a1 = new BigDecimal(IuffiUtils.STRING.trim(request.getParameter("a1")));
    BigDecimal a2 = new BigDecimal(IuffiUtils.STRING.trim(request.getParameter("a2")));
    BigDecimal a3 = new BigDecimal(IuffiUtils.STRING.trim(request.getParameter("a3")));
    BigDecimal a4 = new BigDecimal(IuffiUtils.STRING.trim(request.getParameter("a4")));
    String esito1 = "";
    String esito2 = "";
    String esito3 = "";
    String esito4 = "";
    String esitoTot = "";
    int percMax = (IuffiUtils.STRING.trim(request.getParameter("percMax")) != null && !IuffiUtils.STRING.trim(request.getParameter("percMax")).equalsIgnoreCase("") ? Integer.parseInt(IuffiUtils.STRING.trim(request.getParameter("percMax").substring(0, 2))) : 0);
    if(new BigDecimal(prelievo1).compareTo(a1.multiply(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP))) > 0 || new BigDecimal(prelievo1).compareTo(da1.multiply(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP))) < 0) {
      esito1 = "N";
    }
    else {
      esito1 = "S";
    }
    if(new BigDecimal(prelievo2).compareTo(a2.multiply(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP))) > 0 || new BigDecimal(prelievo2).compareTo(da2.multiply(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP))) < 0) {
      esito2 = "N";
    }
    else {
      esito2 = "S";
    }
    if(new BigDecimal(prelievo3).compareTo(a3.multiply(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP))) > 0 || new BigDecimal(prelievo3).compareTo(da3.multiply(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP))) < 0) {
      esito3 = "N";
    }
    else {
      esito3 = "S";
    }
    if(new BigDecimal(prelievo4).compareTo(a4.multiply(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP))) > 0 || new BigDecimal(prelievo4).compareTo(da4.multiply(new BigDecimal(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP))) < 0) {
      esito4 = "N";
    }
    else {
      esito4 = "S";
    }
    if(prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP <= max) {
      esitoTot = "S";
    }
    else {
      esitoTot = "N";
    }
    //DATE CENSIMENTO
    Enumeration <String> iere = request.getParameterNames();
    List<DataCensimento> date = new ArrayList<DataCensimento>();
    for (Object o : Collections.list(iere)) {
      String s = o.toString();      
        String [] tmp = s.split("_");
        if(tmp != null && tmp.length > 1) {    
          if(tmp[0].contains("data")) {
            DataCensimento data = new DataCensimento();
            String dataS = IuffiUtils.STRING.trim(request.getParameter("data_" + tmp[1]));
            Date dataD = errors.validateDate(dataS, "dataCensimento", true);
            data.setDataCensimento(dataD);
            data.setDescMetodologia(IuffiUtils.STRING.trim(request.getParameter("desc_" + tmp[1])));
            data.setDescrizione(IuffiUtils.STRING.trim(request.getParameter("unit_" + tmp[1])));
            if(dataD != null || (data.getDescMetodologia() != null && !data.getDescMetodologia().equalsIgnoreCase("")) || (data.getDescrizione() != null && !data.getDescrizione().equalsIgnoreCase(""))) {
              date.add(data);
            }
          }
          else if(tmp[0].contains("errda")) {
            DataCensimento data = new DataCensimento();    
            String dataS = IuffiUtils.STRING.trim(request.getParameter("errda_" + tmp[1]));
            Date dataD = errors.validateDate(dataS, "dataCensimento", true);
            data.setDataCensimento(dataD);
            data.setDescMetodologia(IuffiUtils.STRING.trim(request.getParameter("errd_" + tmp[1])));
            data.setDescrizione(IuffiUtils.STRING.trim(request.getParameter("erru_" + tmp[1])));
            if(dataD != null || (data.getDescMetodologia() != null && !data.getDescMetodologia().equalsIgnoreCase("")) || (data.getDescrizione() != null && !data.getDescrizione().equalsIgnoreCase(""))) {
              date.add(data);
            }         
          }
          else if(tmp[0].contains("newda")) {
            DataCensimento data = new DataCensimento();    
            String dataS = IuffiUtils.STRING.trim(request.getParameter("newda_" + tmp[1]));
            Date dataD = errors.validateDate(dataS, "dataCensimento", true);
            data.setDataCensimento(dataD);
            data.setDescMetodologia(IuffiUtils.STRING.trim(request.getParameter("newde_" + tmp[1])));
            data.setDescrizione(IuffiUtils.STRING.trim(request.getParameter("newunit_" + tmp[1])));
            if(dataD != null || (data.getDescMetodologia() != null && !data.getDescMetodologia().equalsIgnoreCase("")) || (data.getDescrizione() != null && !data.getDescrizione().equalsIgnoreCase(""))) {
              date.add(data);
            }           
          }
        }
      }
      errors = new Errors();
      List<DataCensimento> dateError = new ArrayList<DataCensimento>();
      int index = 1;
      if(date != null) {
        for (DataCensimento dataCensimento : date){
          if(dataCensimento.getDataCensimento() == null) {
            dataCensimento.setId(new Long(index));
            errors.validateMandatory(dataCensimento.getDataCensimento(), "errda_" + index);
          }
          if(dataCensimento.getDescMetodologia() == null || dataCensimento.getDescMetodologia().equalsIgnoreCase("")) {
            dataCensimento.setId(new Long(index));
            dataCensimento.setDescMetodologia(null);
            errors.validateMandatory(dataCensimento.getDescMetodologia(), "errd_" + index);
          }
          else {
            dataCensimento.setId(new Long(index));
            dataCensimento.setUnita(quadroEJB.getDescUnitaMisura(new Long(dataCensimento.getDescMetodologia()), idSpecie));
          }
          if(dataCensimento.getDescrizione() == null || dataCensimento.getDescrizione().equalsIgnoreCase("")) {
            dataCensimento.setId(new Long(index));
            dataCensimento.setDescrizione(null);
            errors.validateMandatory(dataCensimento.getDescrizione(), "erru_" + index);
          }
          dateError.add(dataCensimento);
          index++;
        }
      }
      if (!errors.isEmpty()) {
        model.addAttribute("inErrore", true);
        model.addAttribute("errors", errors);
        model.addAttribute("dateE", dateError);
        model.addAttribute("numerorigheCertificate",dateError != null ? dateError.size() : 1);
        String tipo = IuffiUtils.STRING.trim(request.getParameter("tipo"));
        String idDistretto = IuffiUtils.STRING.trim(request.getParameter("idDistretto"));
        List<Distretto> distretto = null;
        if(tipo.equalsIgnoreCase("distretto")) {
          distretto = quadroIuffiEJB.getElencoDistrettiOgur(false, "("+ String.valueOf(idDistretto) + ")", idSpecie, new Long(idPiano), idProcedimentoOggetto);
        }
        else {
          distretto = quadroIuffiEJB.getElencoDistrettiOgur(true, "("+ String.valueOf(idDistretto) + ")", idSpecie, new Long(idPiano), idProcedimentoOggetto);
        }
        if(distretto.get(0) != null) {
          List<DataCensimento> dateNew = new ArrayList<DataCensimento>();
          for(int i = date != null ? date.size()+1 : 1; i<=100; i++) {
            DataCensimento nuova = new DataCensimento();
            nuova.setId(new Long(i));
            dateNew.add(nuova);
          }
          model.addAttribute("dateNew", dateNew);
          model.addAttribute("numerorigheCertificate",date != null ? date.size() : 1);
        }
        List<DecodificaDTO<Long>> metodi = quadroIuffiEJB.getMetodiCensimento(idSpecie);
        if(distretto.get(0).getMaxCapiPrelievo() == null || distretto.get(0).getMaxCapiPrelievo().equalsIgnoreCase("0")) {
          distretto.get(0).setPercentualeTotale(new BigDecimal(0));
        }
        else {
          distretto.get(0).setPercentualeTotale(new BigDecimal(distretto.get(0).getTotalePrelievo()).divide(new BigDecimal(distretto.get(0).getMaxCapiPrelievo()), 2, RoundingMode.HALF_UP));
        }  
        Distretto nuovo = distretto.get(0);
        nuovo.setCensito1(String.valueOf(censito1));
        nuovo.setCensito2(String.valueOf(censito2));
        nuovo.setCensito3(String.valueOf(censito3));
        nuovo.setCensito4(String.valueOf(censito4));
        nuovo.setPrelievo1(String.valueOf(prelievo1));
        nuovo.setPrelievo2(String.valueOf(prelievo2));
        nuovo.setPrelievo3(String.valueOf(prelievo3));
        nuovo.setPrelievo4(String.valueOf(prelievo4));
        nuovo.setEsito1(esito1);
        nuovo.setEsito2(esito2);
        nuovo.setEsito3(esito3);
        nuovo.setEsito4(esito4);
        nuovo.setEsitoTotalePrelievo(esitoTot);
        nuovo.setIndeterminatiCensito(String.valueOf(indetC));
        nuovo.setIndeterminatiPrelievo(String.valueOf(indeterminatiP));
        nuovo.setPerc1(percentuale1);
        nuovo.setPerc2(percentuale2);
        nuovo.setPerc3(percentuale3);
        nuovo.setPerc4(percentuale4);
        model.addAttribute("distretto",nuovo);      
        model.addAttribute("metodi", metodi);
        model.addAttribute("idDistretto", idDistretto);
        model.addAttribute("tipo", tipo);
        return "pianoselettivomufloni/modificadistretto";
      }
      else {
        String tipo = IuffiUtils.STRING.trim(request.getParameter("tipo"));
        String idDistretto = IuffiUtils.STRING.trim(request.getParameter("idDistretto"));
        quadroIuffiEJB.eliminaCensitoPrelievoOgur(new Long(idPiano));
        quadroIuffiEJB.eliminaDateCensimento(new Long(idPiano));
        for (DataCensimento dataCensimento : dateError) {
          quadroIuffiEJB.insertDateCensimento(new Long(idPiano), dataCensimento.getDataCensimento(), 
              quadroIuffiEJB.getMetodoSpecieCensimento(new Long(dataCensimento.getDescMetodologia()), idSpecie), 
              new BigDecimal(dataCensimento.getDescrizione().toString().replace(",", ".")), getLogOperationOggettoQuadroDTO(session).getExtIdUtenteAggiornamento(), 
              getLogOperationOggettoQuadroDTO(session));
        }
        //SALVO SU DB E RICARICO
        quadroIuffiEJB.updatePianoDistrettoOgur(new Long(idPiano), censito1+censito2+censito3+censito4+indetC, indetC, 
            prelievo1+prelievo2+prelievo3+prelievo4+indeterminatiP, indeterminatiP, 
            new BigDecimal(censito2+censito3+censito4+indetC).multiply(new BigDecimal(percMax).divide(new BigDecimal(100))), esitoTot, indeterminatiP);
        quadroIuffiEJB.insertCensitoPrelievoOgur(new Long(idPiano), idSpecie, 1, censito1, prelievo1, percentuale1, esito1, getLogOperationOggettoQuadroDTO(session));
        quadroIuffiEJB.insertCensitoPrelievoOgur(new Long(idPiano), idSpecie, 2, censito2, prelievo2, percentuale2, esito2, getLogOperationOggettoQuadroDTO(session));
        quadroIuffiEJB.insertCensitoPrelievoOgur(new Long(idPiano), idSpecie, 3, censito3, prelievo3, percentuale3, esito3, getLogOperationOggettoQuadroDTO(session));
        quadroIuffiEJB.insertCensitoPrelievoOgur(new Long(idPiano), idSpecie, 4, censito4, prelievo4, percentuale4, esito4, getLogOperationOggettoQuadroDTO(session));
        
        return "redirect:../cuiuffi2005v/index_"+idDistretto+"_"+tipo+"_"+idPiano+".do";
      }
    }
  
    @RequestMapping(value = "/descrizione_unita_misura_{id}", method = RequestMethod.GET, produces = "application/text")
    @ResponseBody
    public String descUnitaDiMisura(Model model,
        @PathVariable("id") String idProduzione, HttpServletRequest request)
        throws Exception {
      if(idProduzione != null && !idProduzione.equalsIgnoreCase("")) {
        String result = quadroEJB.getDescUnitaMisura(new Long(idProduzione), idSpecie);
        return result;
      }
      else 
        return "";
    }
    
    @RequestMapping(value = "/esito_{prelievo}_{c1}_{c2}_{c3}_{c4}_{indp}_{arg1}", method = RequestMethod.GET, produces = "application/text")
    @ResponseBody
    public String esito(Model model,
        @PathVariable("prelievo") String prelievo, @PathVariable("c1") String c1, @PathVariable("c2") String c2, @PathVariable("c3") String c3,
        @PathVariable("c4") String c4, @PathVariable("indp") String indp, @PathVariable("arg1") String arg1,
        HttpServletRequest request)
        throws Exception {
     
      BigDecimal da = quadroEJB.getValoreDa(idSpecie, new Long(arg1));
      BigDecimal a = quadroEJB.getValoreA(idSpecie, new Long(arg1));
      BigDecimal prel = null;
      BigDecimal prelievo1 = null;
      BigDecimal prelievo2 = null;
      BigDecimal prelievo3 = null;
      BigDecimal prelievo4 = null;
      BigDecimal indet = null;
      if(da != null && a != null ) {
        if(!prelievo.equalsIgnoreCase("nan")) {
          prel = new BigDecimal(prelievo) ;
        }
        else {
          prel = new BigDecimal(0) ;
        }
        String esito = "";
        if(!c1.equalsIgnoreCase("nan")) {
          prelievo1 = new BigDecimal(c1) ;
        }
        else {
          prelievo1 = new BigDecimal(0) ;
        }
        if(!c2.equalsIgnoreCase("nan")) {
          prelievo2 = new BigDecimal(c2) ;
        }
        else {
          prelievo2 = new BigDecimal(0) ;
        }
        if(!c3.equalsIgnoreCase("nan")) {
          prelievo3 = new BigDecimal(c3) ;
        }
        else {
          prelievo3 = new BigDecimal(0) ;
        }
        if(!c4.equalsIgnoreCase("nan")) {
          prelievo4 = new BigDecimal(c4) ;
        }
        else {
          prelievo4 = new BigDecimal(0) ;
        }
        if(!indp.equalsIgnoreCase("nan")) {
          indet = new BigDecimal(indp) ;
        }
        else {
          indet = new BigDecimal(0) ;
        }        
        if(prel.compareTo(a.multiply(prelievo1.add(prelievo2.add(prelievo3.add(prelievo4.add(indet)))))) > 0 || prel.compareTo(da.multiply(prelievo1.add(prelievo2.add(prelievo3.add(prelievo4.add(indet)))))) < 0) {
          esito = "ko";
        }
        else {
          esito = "ok";
        }
        return esito;
      }
      return null;
      
    }
}
