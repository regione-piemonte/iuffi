package it.csi.iuffi.iuffiweb.presentation.quadro.anticipo;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo.DatiAnticipo;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.RigaAnticipoLivello;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-169-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi169v")
public class CUIUFFI169VVisualizzaDatiAnticipo extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String visualizzaModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(
        request.getSession());
    final DatiAnticipo datiAnticipo = quadroEJB
        .getDatiAnticipo(idProcedimentoOggetto);
    final List<RigaAnticipoLivello> ripartizioneAnticipo = datiAnticipo
        .getRipartizioneAnticipo();
    model.addAttribute("datiAnticipo", datiAnticipo);
    model.addAttribute("ripartizioneAnticipo", ripartizioneAnticipo);
    BigDecimal totImportoAmmesso = BigDecimal.ZERO;
    BigDecimal totImportoInvestimento = BigDecimal.ZERO;
    BigDecimal totImportoContributo = BigDecimal.ZERO;
    BigDecimal totImportoAnticipo = BigDecimal.ZERO;
    if (ripartizioneAnticipo != null)
    {
      for (RigaAnticipoLivello riga : ripartizioneAnticipo)
      {
        totImportoAmmesso = IuffiUtils.NUMBERS.add(totImportoAmmesso,
            riga.getImportoAmmesso());
        totImportoInvestimento = IuffiUtils.NUMBERS
            .add(totImportoInvestimento, riga.getImportoInvestimento());
        totImportoContributo = IuffiUtils.NUMBERS.add(totImportoContributo,
            riga.getImportoContributo());
        totImportoAnticipo = IuffiUtils.NUMBERS.add(totImportoAnticipo,
            riga.getImportoAnticipo());
      }
    }
    model.addAttribute("totImportoAmmesso", totImportoAmmesso);
    model.addAttribute("totImportoInvestimento", totImportoInvestimento);
    model.addAttribute("totImportoContributo", totImportoContributo);
    model.addAttribute("totImportoAnticipo", totImportoAnticipo);
    return "anticipo/visualizzaDati";
  }
}