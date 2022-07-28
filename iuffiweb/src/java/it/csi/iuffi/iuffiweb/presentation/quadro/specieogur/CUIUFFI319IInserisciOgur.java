package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-319-I", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi319i")
public class CUIUFFI319IInserisciOgur extends BaseController
{

  @Autowired
  IQuadroEJB      quadroEJB;
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String inserisci(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> specieOgur = quadroIuffiEJB.getElencoSpecieOgur(true, getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("specieOgur", specieOgur);
    model.addAttribute("action", "../cuiuffi319i/index.do");
    return "ogur/inserisci";
  }
  
  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String conferma(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    Long idSpecieOgur = errors.validateMandatoryLong(request.getParameter("idSpecieOgur"), "idSpecieOgur");
    BigDecimal superficie = errors.validateMandatoryBigDecimal(request.getParameter("superficie"), "superficie", 2);
    if(errors.isEmpty())
    {
      Long idOgur = errors.validateLong(request.getParameter("idOgur"), "idOgur");
      quadroIuffiEJB.inserisciModificaOgur(getIdProcedimentoOggetto(request.getSession()), idOgur, idSpecieOgur, superficie, getLogOperationOggettoQuadroDTO(request.getSession()));
      return "ogur/elenco";
    }
    
    List<DecodificaDTO<Long>> specieOgur = quadroIuffiEJB.getElencoSpecieOgur(true, getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("specieOgur", specieOgur);
    model.addAttribute("action", "../cuiuffi319i/index.do");
    model.addAttribute("preferRequest", true);
    model.addAttribute("errors", errors);

    return "ogur/inserisci";
  }


}
