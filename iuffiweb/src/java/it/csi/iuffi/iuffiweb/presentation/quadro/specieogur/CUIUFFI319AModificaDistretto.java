package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

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
@IuffiSecurity(value = "CU-IUFFI-319-A", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi319a")
public class CUIUFFI319AModificaDistretto extends CUIUFFI319InserisciModificaOgurBase
{

  @Autowired
  IQuadroEJB      quadroEJB;
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @RequestMapping(value = "/index_{idOgur}_{idDistretto}", method = RequestMethod.GET)
  public String inserisci(@PathVariable("idOgur") long idOgur, @PathVariable("idDistretto") long idDistretto, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> specieOgur = quadroIuffiEJB.getElencoSpecieOgur(false, getIdProcedimentoOggetto(request.getSession()));
    model.addAttribute("specieOgur", specieOgur);
    model.addAttribute("action", "../cuiuffi319a/index_"+idOgur+"_"+idDistretto+".do");
    OgurDTO ogur = quadroIuffiEJB.getOgur(idOgur, true);
    model.addAttribute("ogur",ogur);
    if(ogur!=null && ogur.getDistretti()!=null && !ogur.getDistretti().isEmpty())
      for(DistrettoDTO distretto : ogur.getDistretti())
      {
        if(distretto.getIdDistretto().longValue()==idDistretto)
          model.addAttribute("distretto",distretto);
      }

    return "ogur/distretti/inseriscidistretto";
  }
  
  @RequestMapping(value = "/index_{idOgur}_{idDistretto}", method = RequestMethod.POST)
  public String conferma(@PathVariable("idOgur") long idOgur, @PathVariable("idDistretto") long idDistretto, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    OgurDTO ogur = quadroIuffiEJB.getOgur(idOgur, true);
    model.addAttribute("ogur",ogur);
    if(ogur!=null && ogur.getDistretti()!=null && !ogur.getDistretti().isEmpty())
      for(DistrettoDTO distretto : ogur.getDistretti())
      {
        if(distretto.getIdDistretto().longValue()==idDistretto)
          model.addAttribute("distretto",distretto);
      }

    return validateAndUpdate(idOgur, idDistretto, quadroIuffiEJB, model, request);
  }

}
