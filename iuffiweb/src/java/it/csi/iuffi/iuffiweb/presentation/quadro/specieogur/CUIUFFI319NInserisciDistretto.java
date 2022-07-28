package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

import java.time.LocalDate;
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
import it.csi.iuffi.iuffiweb.dto.DistrettoDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-319-N", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi319n")
public class CUIUFFI319NInserisciDistretto extends CUIUFFI319InserisciModificaOgurBase
{

  @Autowired
  IQuadroEJB      quadroEJB;
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @RequestMapping(value = "/index_{idOgur}", method = RequestMethod.GET)
  public String inserisci(@PathVariable("idOgur") long idOgur, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> specieOgur = quadroIuffiEJB.getElencoSpecieOgur(false, getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("specieOgur", specieOgur);
    model.addAttribute("action", "../cuiuffi319n/index_"+idOgur+".do");
    OgurDTO ogur = quadroIuffiEJB.getOgur(idOgur, false);
  
    DistrettoDTO distretto = new DistrettoDTO();
    model.addAttribute("distretto",distretto);
    model.addAttribute("currentYear",LocalDate.now().getYear());
    model.addAttribute("ogur",ogur);

    return "ogur/distretti/inseriscidistretto";
  }
  
  @RequestMapping(value = "/index_{idOgur}", method = RequestMethod.POST)
  public String conferma(@PathVariable("idOgur") long idOgur, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    OgurDTO ogur = quadroIuffiEJB.getOgur(idOgur, false);
    model.addAttribute("currentYear",LocalDate.now().getYear());
    model.addAttribute("ogur",ogur);
    model.addAttribute("action", "../cuiuffi319n/index_"+idOgur+".do");

    return validateAndUpdate(idOgur, null, quadroIuffiEJB, model, request);

  }


}
