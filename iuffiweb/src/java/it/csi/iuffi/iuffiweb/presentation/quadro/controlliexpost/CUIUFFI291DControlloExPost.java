package it.csi.iuffi.iuffiweb.presentation.quadro.controlliexpost;

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
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.VisitaLuogoExtDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliinlocomisureinvestimento.DatiSpecificiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-291-D", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi291d")
public class CUIUFFI291DControlloExPost extends BaseController
{
  public static final String BASE_JSP_URL = "controlliinlocomisureinvestimento/";

  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String elenco(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromRequest(request);
    final long idProcedimentoOggetto = po.getIdProcedimentoOggetto();
    List<ControlloAmministrativoDTO> controlliAmministrativi = quadroEJB
        .getControlliAmministrativi(idProcedimentoOggetto,
            IuffiConstants.QUADRO.CODICE.CONTROLLI_IN_LOCO_MISURE_INVESTIMENTO,
            null);
    model.addAttribute("controlli", controlliAmministrativi);
    DatiSpecificiDTO datiSpecifici = quadroEJB
        .getDatiSpecifici(idProcedimentoOggetto, po.getIdProcedimento());
    model.addAttribute("datiSpecifici", datiSpecifici);
    model.addAttribute("modalitaSelezione",
        getHtmlDecodificaModalitaSelezione(datiSpecifici.getFlagEstratta()));

    QuadroOggettoDTO quadro = findQuadroCorrente(po);
    final long idQuadroOggetto = quadro.getIdQuadroOggetto();
    List<VisitaLuogoExtDTO> visite = quadroEJB
        .getVisiteLuogo(idProcedimentoOggetto, idQuadroOggetto, null);
    model.addAttribute("visite", visite);
    EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(idProcedimentoOggetto,
        idQuadroOggetto);
    model.addAttribute("esito", esito);

    if (datiSpecifici != null && (datiSpecifici.getFlagControllo() == null
        || IuffiConstants.FLAGS.NO.equals(datiSpecifici.getFlagControllo())))
    {
      model.addAttribute("disabilitaControlloLoco", Boolean.TRUE);
    }
    else
    {
      model.addAttribute("disabilitaControlloLoco", Boolean.FALSE);
    }

    return BASE_JSP_URL + "dettaglio";
  }

  public static String getHtmlDecodificaModalitaSelezione(String flagEstratta)
  {
    switch (flagEstratta)
    {
      case IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.CASUALE:
        return IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.DECODIFICA.CASUALE;
      case IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.RISCHIO:
        return IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.DECODIFICA.RISCHIO;
      case IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.MANUALE:
        return IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.DECODIFICA.MANUALE;
      case IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.NON_ESTRATTA:
      case IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.DICHIARAZIONI_SOSTITUTIVE:
        return "";
      default:
        return IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.MODALITA_SELEZIONE.DECODIFICA.SCONOSCIUTA;
    }
  }
}