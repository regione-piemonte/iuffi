package it.csi.iuffi.iuffiweb.presentation.quadro.controlliamministrativi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.ControlloAmministrativoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.InfoExPostsDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.VisitaLuogoExtDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-165-D", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi165d")
public class CUIUFFI165DGestioneControlliAmministrativiDettaglio
    extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String elenco(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(
        request.getSession());
    List<ControlloAmministrativoDTO> controlliAmministrativi = quadroEJB
        .getControlliAmministrativi(idProcedimentoOggetto,
            IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI, null);
    model.addAttribute("controlli", controlliAmministrativi);
    QuadroOggettoDTO quadro = findQuadroCorrente(request);
    List<VisitaLuogoExtDTO> visite = quadroEJB.getVisiteLuogo(
        idProcedimentoOggetto, quadro.getIdQuadroOggetto(), null);
    model.addAttribute("visite", visite);

    // La funzionalità è presente solo per le domande di saldo (campo
    // IUF_D_OGGETTO.TIPO_PAGAMENTO_SIGOP = 'SALDO')
    ProcedimentoOggetto po = getProcedimentoOggettoFromRequest(request);
    if (IuffiConstants.TIPO_PAGAMENTO_SIGOP.SALDO
        .equals(po.getTipoPagamentoSigop()))
    {
      InfoExPostsDTO infoExPosts = quadroEJB
          .getInformazioniExposts(idProcedimentoOggetto);
      if (infoExPosts == null)
      {
        model.addAttribute("infoexposts", new InfoExPostsDTO());
      }
      else
      {
        model.addAttribute("infoexposts", infoExPosts);
      }
      model.addAttribute("TIPO_PAGAMENTO_SIGOP_SALDO", Boolean.TRUE);
    }

    long idQuadroOggetto = po
        .findQuadroByCodiceQuadro(
            IuffiConstants.QUADRO.CODICE.CONTROLLI_AMMINISTRATIVI)
        .getIdQuadroOggetto();
    EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(idProcedimentoOggetto,
        idQuadroOggetto);
    model.addAttribute("esito", esito);
    model.addAttribute("flagEstratta", IuffiUtils.STRING
        .nvl(quadroEJB.getFlagEstratta(idProcedimentoOggetto)));
    return "controlliamministrativi/dettaglio";
  }
}