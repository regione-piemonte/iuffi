package it.csi.iuffi.iuffiweb.presentation.quadro.esitodefinitivo;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi264l")
@IuffiSecurity(value = "CU-IUFFI-264-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI264DettaglioEsitoDefinitivo extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);

    EsitoFinaleDTO esito = quadroEJB
        .getEsitoDefinitivo(po.getIdProcedimentoOggetto());
    model.addAttribute("esito", esito);

    QuadroOggettoDTO quadro = po.findQuadroByCU("CU-IUFFI-264-M");
    model.addAttribute("ultimaModifica",
        getUltimaModifica(quadroEJB, po.getIdProcedimentoOggetto(),
            quadro.getIdQuadroOggetto(), po.getIdBandoOggetto()));
    return "esitodefinitivo/dettaglio";
  }

}