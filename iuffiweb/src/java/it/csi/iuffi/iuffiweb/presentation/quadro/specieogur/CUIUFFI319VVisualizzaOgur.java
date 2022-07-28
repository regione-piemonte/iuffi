package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

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
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-319-V", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "cuiuffi319v")
public class CUIUFFI319VVisualizzaOgur extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @Autowired
  IQuadroEJB quadroEJB;
  
  @RequestMapping(value = "/index_{idOgur}")
  public String index(@PathVariable("idOgur") long idOgur, Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> specieOgur = quadroIuffiEJB.getElencoSpecieOgur(false, getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("specieOgur", specieOgur);
    model.addAttribute("ogur",quadroIuffiEJB.getOgur(idOgur, false));
    return "ogur/elencodistretti";
  }
  
  @RequestMapping(value = "/dettaglio_{idOgur}", method = RequestMethod.GET)
  public String dettaglio(@PathVariable("idOgur") long idOgur, Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException
  {
    OgurDTO ogur = quadroIuffiEJB.getOgur(idOgur, true);
    model.addAttribute("ogur",ogur);
    return "ogur/dettaglio";
  }
  
}
