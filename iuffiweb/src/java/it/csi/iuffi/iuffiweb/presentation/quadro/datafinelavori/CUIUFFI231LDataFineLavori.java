package it.csi.iuffi.iuffiweb.presentation.quadro.datafinelavori;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datafinelavori.DataFineLavoriDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-231-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi231l")
public class CUIUFFI231LDataFineLavori extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEjb;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public final String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    QuadroOggettoDTO quadro = getProcedimentoOggettoFromSession(session)
        .findQuadroByCU("CU-IUFFI-231-L");
    final ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcOggetto = getIdProcedimentoOggetto(session);
    List<DataFineLavoriDTO> elenco = quadroEjb.getElencoDateFineLavori(
        getProcedimentoFromSession(session).getIdProcedimento(), idProcOggetto);
    boolean noInserisci = Boolean.FALSE;
    if (elenco != null)
    {
      for (DataFineLavoriDTO data : elenco)
      {
        if (data.getIdProcedimentoOggetto() == idProcOggetto)
        {
          noInserisci = Boolean.TRUE;
          break;
        }
      }
    }

    model.addAttribute("noInserisci", noInserisci);

    DataFineLavoriDTO lastRow = quadroEjb.getLastDataFineLavori(
        getProcedimentoFromSession(session).getIdProcedimento());
    if (lastRow != null && lastRow.getIdProcedimentoOggetto() == idProcOggetto)
    {
      model.addAttribute("modificaAbilitata", Boolean.TRUE);
    }

    model.addAttribute("ultimaModifica",
        getUltimaModifica(quadroEjb,
            procedimentoOggetto.getIdProcedimentoOggetto(),
            quadro.getIdQuadroOggetto(),
            procedimentoOggetto.getIdBandoOggetto()));

    return "datafinelavori/elenco";
  }

  @RequestMapping(value = "getElencoDate", produces = "application/json")
  @ResponseBody
  public List<DataFineLavoriDTO> getElencoProcedimenti(HttpSession session)
      throws InternalUnexpectedException
  {
    List<DataFineLavoriDTO> elenco = quadroEjb.getElencoDateFineLavori(
        getProcedimentoFromSession(session).getIdProcedimento(),
        getIdProcedimentoOggetto(session));
    if (elenco == null)
    {
      elenco = new ArrayList<>();
    }
    return elenco;
  }

}
