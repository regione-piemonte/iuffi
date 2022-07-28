package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-315-A", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi315a")
public class CUIUFFI315AApprovaConcessioni extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @RequestMapping(value = "/index_{idConcessione}", method = RequestMethod.GET)
  public String modifica(@PathVariable("idConcessione") long idConcessione, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("idConcessione", idConcessione);
    ConcessioneDTO concessione = quadroIuffiEJB.getConcessione(idConcessione);
    model.addAttribute("concessione", concessione);
    prepareModel(model, request);

    return "concessioni/approva";
  }
  
  private void prepareModel(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("modificaInserisci", "modifica");
    List<DecodificaDTO<Long>> elencoAtti = quadroIuffiEJB.getElencoTipoAttoConcessione();
    model.addAttribute("elencoAtti", elencoAtti);
  }
  
  @RequestMapping(value = "index_{idConcessione}", method = RequestMethod.POST)
  public String conferma(@PathVariable("idConcessione") long idConcessione, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    Long numeroProtocollo = errors.validateMandatoryLong(request.getParameter("numeroProtocollo"), "numeroProtocollo");
    Date dataProtocollo = errors.validateMandatoryDate(request.getParameter("dataProtocollo"), "dataProtocollo", true);
    Long idTipoAtto = errors.validateMandatoryLong(request.getParameter("idTipoAtto"), "idTipoAtto");
    
    if (errors.isEmpty())
    {
      quadroIuffiEJB.approvaConcessione(idConcessione, numeroProtocollo, dataProtocollo, idTipoAtto, getIdUtenteLogin(request.getSession()));
      return "redirect:../cuiuffi315v/index.do";
    }
    model.addAttribute("errors", errors);
    model.addAttribute("idConcessione", idConcessione);
    model.addAttribute("perferRequest", true);
    ConcessioneDTO concessione = quadroIuffiEJB.getConcessione(idConcessione);
    model.addAttribute("concessione", concessione);
    prepareModel(model, request);
    return "concessioni/approva";
  }
  
}
