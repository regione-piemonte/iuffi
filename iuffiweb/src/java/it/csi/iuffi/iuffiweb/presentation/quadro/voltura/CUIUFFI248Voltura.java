package it.csi.iuffi.iuffiweb.presentation.quadro.voltura;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.VolturaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-248-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi248l")
public class CUIUFFI248Voltura extends BaseController
{
  @Autowired
  protected IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String elenco(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {

    QuadroOggettoDTO quadro = getProcedimentoOggettoFromSession(
        request.getSession()).findQuadroByCU("CU-IUFFI-248-L");
    final ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        request.getSession());

    model.addAttribute("ultimaModifica",
        getUltimaModifica(quadroEJB,
            procedimentoOggetto.getIdProcedimentoOggetto(),
            quadro.getIdQuadroOggetto(),
            procedimentoOggetto.getIdBandoOggetto()));

    VolturaDTO voltura = quadroEJB
        .getVoltura(procedimentoOggetto.getIdProcedimentoOggetto());
    model.addAttribute("voltura", voltura);

    return "voltura/dettaglio";
  }

}