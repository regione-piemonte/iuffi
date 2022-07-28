package it.csi.iuffi.iuffiweb.presentation.accessolibero;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.licenzapesca.VersamentoLicenzaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.ProcedimOggettoStampaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.smrcomms.agripagopasrv.business.pagopa.EsitoLeggiDocumento;
import it.csi.smrcomms.agripagopasrv.business.pagopa.PagoPAWS;
import it.csi.smrcomms.agripagopasrv.business.pagopa.ParametriLeggiDocumento;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellEsitoVO;
import weblogic.management.ApplicationException;

@Controller
@NoLoginRequired
@RequestMapping("/cuiuffi3002")
public class CUIUFFI3002AccessoLiberoRicercaIUV extends BaseController {
  
  public final long idApplicativoIuffi = 70;
  
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB ;
  
  final String step1 = "accessolibero/ricercaIUVStep1";
  final String step2 = "accessolibero/ricercaIUVStep2";
  
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String step01(Model model, HttpSession session) throws InternalUnexpectedException {    
    return step1;
  }
  
  @RequestMapping("/cercaIuv_{cittadinanza}")
  public String cercaIuv(Model model, HttpSession session, HttpServletRequest request, @PathVariable(value = "cittadinanza") String cittadinanza) throws InternalUnexpectedException { 
    String iuv = IuffiUtils.STRING.trim(request.getParameter("iuv"));
    String codicefiscale = IuffiUtils.STRING.trim(request.getParameter("codicefiscale"));
    model.addAttribute("versamentoTrovato", false);
    Errors errors = new Errors();
    errors.validateMandatory(iuv, "iuv");    
    String cf = null;
    VersamentoLicenzaDTO versamento = null;
    if(cittadinanza.equalsIgnoreCase("01")) {    
      if(errors.validateMandatory(codicefiscale, "codicefiscale")) {
        if(!IuffiUtils.VALIDATION.controlloCf(codicefiscale)) {
          errors.addError("codicefiscale", "Verificare il formato del codice fiscale inserito!");
        }
      }
    }
    if(!errors.isEmpty()) {
      model.addAttribute("errors", errors);
      model.addAttribute("iuv", iuv);
      model.addAttribute("codiceFiscale", codicefiscale);
      model.addAttribute("cittadinanza", cittadinanza);
      return step2;
    }
    if(cittadinanza.equalsIgnoreCase("01")) { 
      cf = quadroIuffiEJB.cercaAnagrafica(codicefiscale);
      if(cf == null) {
        model.addAttribute("msgErrore", "ATTENZIONE: Il Codice fiscale inserito non è presente in archivio");
      }
    }                       
    versamento = quadroIuffiEJB.cercaIuv(iuv.startsWith("3") ? iuv.substring(1) : iuv, codicefiscale, cittadinanza);
    if(versamento == null) {
      model.addAttribute("msgErrore", "ATTENZIONE: Non esistono pagamenti o licenze di pesca con il codice indicato");
    }
    else{
      model.addAttribute("versamento", versamento);
      model.addAttribute("versamentoTrovato", true);    
    }              
    model.addAttribute("iuv", iuv);
    model.addAttribute("codiceFiscale", codicefiscale);
    model.addAttribute("cittadinanza", cittadinanza);
    return step2;
  }
  
  @RequestMapping(value = "/confermacittadinanza_{cittadinanza}", method = RequestMethod.GET)
  public String step01Post(Model model, HttpSession session,@PathVariable(value = "cittadinanza") String cittadinanza) throws InternalUnexpectedException, ApplicationException {
    model.addAttribute("preferRequestValues", Boolean.TRUE);
    Errors errors = new Errors();
    if(!errors.validateMandatory(cittadinanza, "cittadinanza")) {
      model.addAttribute("errors", errors);
      return step01(model, session);
    }
    model.addAttribute("cittadinanza", cittadinanza);
    return step2;
  }
  
  @RequestMapping(value = "documento/leggiDocumento", method = RequestMethod.GET)
  public @ResponseBody ResponseEntity<byte[]> leggiDocumento(HttpSession session, Model model,
      @RequestParam(value = "iuv", required = true) String iuv,
      @RequestParam(value = "cf", required = true) String cf,
      @RequestParam(value = "importo", required = true) String importo,
      @RequestParam(value = "tipo", required = true) String tipo) throws Exception {
    
    PagoPAWS pagopaWs = IuffiUtils.WS.getPagoPAWS();
    
    ParametriLeggiDocumento param = new ParametriLeggiDocumento();    
    param.setCodiceFiscalePIVA(cf);
    param.setImporto(importo == null || importo.equalsIgnoreCase("") ? new BigDecimal(0) : new BigDecimal(importo));   
    param.setIuv(iuv);
    param.setTipoDocumento(tipo);
    param.setIdApplicativo(idApplicativoIuffi); 
    String nomefile = tipo.equalsIgnoreCase("RT") ? "RicevutaTelematica_" + cf  : "AvvisoDiPagamento_" + cf;
    EsitoLeggiDocumento esito =  pagopaWs.leggiDocumento(param);
    
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", "application/pdf");
    httpHeaders.add("Content-Disposition", "attachment; filename=\"" + nomefile + "_.pdf" + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(esito.getFile(), httpHeaders, HttpStatus.OK);
    return response;
  }

}