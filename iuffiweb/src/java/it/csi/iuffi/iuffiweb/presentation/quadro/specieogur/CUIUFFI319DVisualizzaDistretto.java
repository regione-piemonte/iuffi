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
import it.csi.iuffi.iuffiweb.dto.DistrettoDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-319-D", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "cuiuffi319d")
public class CUIUFFI319DVisualizzaDistretto extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @Autowired
  IQuadroEJB quadroEJB;
  
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException
  {
    List<OgurDTO> elencoOgur = quadroIuffiEJB.getElencoOgur(getIdProcedimentoOggetto(session));
    model.addAttribute("elencoOgur", elencoOgur);
    return "ogur/distretti/dettagliodistretto";
  }
  
  
  @RequestMapping(value = "/index_{idOgur}_{idDistretto}", method = RequestMethod.GET)
  public String inserisci(@PathVariable("idOgur") long idOgur, @PathVariable("idDistretto") long idDistretto, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {

    OgurDTO ogur = quadroIuffiEJB.getOgur(idOgur, true);
    model.addAttribute("ogur",ogur);
    if(ogur!=null && ogur.getDistretti()!=null && !ogur.getDistretti().isEmpty())
      for(DistrettoDTO distretto : ogur.getDistretti())
      {
        if(distretto.getIdDistretto().longValue()==idDistretto)
          model.addAttribute("distretto",distretto);
      }

    return "ogur/distretti/dettagliodistretto";
  }
  

}
