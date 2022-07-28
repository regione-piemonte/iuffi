package it.csi.iuffi.iuffiweb.presentation.estrazionicampione;

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

import it.csi.iuffi.iuffiweb.business.IEstrazioniEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.RigaFiltroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.estrazionecampione.RigaSimulazioneEstrazioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-218", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping("cuiuffi218")
public class CUIUFFI218SimulazioneEstrazioneCampione extends BaseController
{

  @Autowired
  private IQuadroEJB     quadroEjb;

  @Autowired
  private IEstrazioniEJB estrazioniEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public final String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    addCommonData(model);
    return "estrazioniacampione/simulazione";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public final String indexPost(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    String idTipoEstrazione = request.getParameter("idTipoEstrazione");
    errors.validateMandatoryLong(idTipoEstrazione, "idTipoEstrazione");
    model.addAttribute("prfvalues", Boolean.TRUE);

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
    }
    else
    {
      model.addAttribute("visualizzaRisultati", Boolean.TRUE);
      model.addAttribute("idTipoEstrazione", idTipoEstrazione);
    }

    return index(model, session);
  }

  @RequestMapping(value = "getElencoRisultatiSimulazione_{idTipoEstrazione}", produces = "application/json")
  @ResponseBody
  public List<RigaSimulazioneEstrazioneDTO> getElencoRisultatiSimulazione(
      HttpSession session,
      @PathVariable(value = "idTipoEstrazione") long idTipoEstrazione)
      throws InternalUnexpectedException
  {
    List<RigaSimulazioneEstrazioneDTO> elenco = quadroEjb
        .getElencoRisultatiSimulazione(idTipoEstrazione);
    if (elenco == null)
    {
      elenco = new ArrayList<>();
    }
    return elenco;
  }

  private void addCommonData(Model model) throws InternalUnexpectedException
  {
    List<RigaFiltroDTO> elenco = estrazioniEJB
        .getElencoTipoEstrazioniCaricabili();
    if (elenco == null)
    {
      elenco = new ArrayList<>();
    }
    model.addAttribute("listTipoEstrazione", elenco);
  }

}
