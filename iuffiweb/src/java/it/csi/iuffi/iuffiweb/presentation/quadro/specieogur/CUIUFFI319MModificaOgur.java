package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-319-M", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi319m")
public class CUIUFFI319MModificaOgur extends BaseController
{

  @Autowired
  IQuadroEJB      quadroEJB;
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @IsPopup
  @RequestMapping(value = "/index_{idOgur}", method = RequestMethod.GET)
  public String inserisci(@PathVariable("idOgur") long idOgur, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> specieOgur = quadroIuffiEJB.getElencoSpecieOgur(false, getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("specieOgur", specieOgur);
    model.addAttribute("action", "../cuiuffi319m/index_"+idOgur+".do");
    model.addAttribute("ogur",quadroIuffiEJB.getOgur(idOgur, false));
    return "ogur/inserisci";
  }
 
  @RequestMapping(value = "/index_{idOgur}", method = RequestMethod.POST)
  public String conferma(@PathVariable("idOgur") long idOgur, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    BigDecimal superficie = errors.validateMandatoryBigDecimal(request.getParameter("superficie"), "superficie", 2);
    if(errors.isEmpty())
    {
      quadroIuffiEJB.inserisciModificaOgur(getIdProcedimentoOggetto(request.getSession()), idOgur, null, superficie, getLogOperationOggettoQuadroDTO(request.getSession()));
      return "ogur/elenco";
    }
    
    List<DecodificaDTO<Long>> specieOgur = quadroIuffiEJB.getElencoSpecieOgur(false, getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("specieOgur", specieOgur);
    model.addAttribute("action", "../cuiuffi319m/index_"+idOgur+".do");
    model.addAttribute("preferRequest", true);
    model.addAttribute("errors", errors);
    model.addAttribute("ogur", quadroIuffiEJB.getOgur(idOgur, false));

    return "ogur/inserisci";
  }

}
