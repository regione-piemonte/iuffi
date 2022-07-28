package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

import java.util.ArrayList;
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
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-319-L", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "cuiuffi319l")
public class CUIUFFI319LElencoOgur extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @Autowired
  IQuadroEJB quadroEJB;
  
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(session);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU("CU-IUFFI-319-M");
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB, procedimentoOggetto.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "ogur/elenco";
  }
  
  @RequestMapping(value = "/getElencoOgur", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<OgurDTO> getElencoOgur(
      HttpSession session, 
      Model model)  throws InternalUnexpectedException
  {
    List<OgurDTO> list = quadroIuffiEJB.getElencoOgur(getIdProcedimentoOggetto(session), null); 
    return list!=null ? list : new ArrayList<OgurDTO>();
  }
  
  @RequestMapping(value = "specieOgurExcel_{idOgur}")
  public ModelAndView downloadExcel(Model model,
      @PathVariable("idOgur") long idOgur, HttpSession session)
      throws InternalUnexpectedException
  {
    OgurDTO ogur = quadroIuffiEJB.getOgur(idOgur, true);

    return new ModelAndView("excelSpecieOgurView", "ogur", ogur);
  }

}
