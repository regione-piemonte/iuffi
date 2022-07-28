package it.csi.iuffi.iuffiweb.presentation.licenzapesca;

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
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.smrcomms.agripagopasrv.business.pagopa.EsitoLeggiDocumento;
import it.csi.smrcomms.agripagopasrv.business.pagopa.PagoPAWS;
import it.csi.smrcomms.agripagopasrv.business.pagopa.ParametriLeggiDocumento;
import weblogic.management.ApplicationException;

@Controller
@NoLoginRequired
@RequestMapping("/cuiuffi3004")
public class CUIUFFI3004RicercaIUV extends BaseController {
  
  public final long idApplicativoIuffi = 70;
  
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB ;
  
  final String step2 = "licenzepesca/ricercaIUVStep2";
  
  @RequestMapping("/index")
  public String index(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException { 
    String iuv = IuffiUtils.STRING.trim(request.getParameter("iuv"));
    model.addAttribute("iuv", iuv);
    return step2;
  }
  
  @RequestMapping("/cerca")
  public String cercaIuv(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException { 
    String iuv = IuffiUtils.STRING.trim(request.getParameter("iuv"));
    String codicefiscale = getUtenteAbilitazioni(session).getCodiceFiscale().toUpperCase();
    model.addAttribute("versamentoTrovato", false);
    Errors errors = new Errors();
    errors.validateMandatory(iuv, "iuv");    
    VersamentoLicenzaDTO versamento = null;
    if(!errors.isEmpty()) {
      model.addAttribute("errors", errors);
      model.addAttribute("iuv", iuv);
      model.addAttribute("codiceFiscale", codicefiscale);
      return step2;
    }                  
    versamento = quadroIuffiEJB.cercaIuv(iuv.startsWith("3") ? iuv.substring(1) : iuv, codicefiscale, "01");
    if(versamento == null) {
      model.addAttribute("msgErrore", "ATTENZIONE: Non esistono pagamenti o licenze di pesca con il codice indicato");
    }
    else{
      model.addAttribute("versamento", versamento);
      model.addAttribute("versamentoTrovato", true);    
    }              
    model.addAttribute("iuv", iuv);
    model.addAttribute("codiceFiscale", codicefiscale);
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