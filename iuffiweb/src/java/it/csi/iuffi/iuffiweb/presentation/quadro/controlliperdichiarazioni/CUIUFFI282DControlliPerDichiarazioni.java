package it.csi.iuffi.iuffiweb.presentation.quadro.controlliperdichiarazioni;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliinlocomisureinvestimento.DatiSpecificiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-282-D", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi282d")
public class CUIUFFI282DControlliPerDichiarazioni extends BaseController
{
  public static final String BASE_JSP_URL = "controlliperdichiarazioni/";

  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String elenco(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromRequest(request);
    final long idProcedimentoOggetto = po.getIdProcedimentoOggetto();
    QuadroOggettoDTO quadro = po
        .findQuadroByCU(IuffiConstants.USECASE.CONTROLLI_DICHIARAZIONI);
    BandoDTO bando = quadroEJB
        .getInformazioniBandoByProcedimento(po.getIdProcedimento());
    model.addAttribute("flagEstratta",
        quadroEJB.getFlagEstrattaControlliDichiarazione(po.getIdProcedimento(),
            po.getCodiceRaggruppamento(), idProcedimentoOggetto,
            bando.getCodiceTipoBando()));
    EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(idProcedimentoOggetto,
        quadro.getIdQuadroOggetto());
    DatiSpecificiDTO datiSpecifici = quadroEJB
        .getDatiSpecifici(idProcedimentoOggetto, po.getIdProcedimento());
    model.addAttribute("datiSpecifici", datiSpecifici);
    model.addAttribute("modalitaSelezione",
        getHtmlDecodificaModalitaSelezione(datiSpecifici.getFlagEstratta()));
    model.addAttribute("esito", esito);
    model.addAttribute("ultimaModifica",
        getUltimaModifica(quadroEJB, idProcedimentoOggetto,
            quadro.getIdQuadroOggetto(), po.getIdBandoOggetto()));

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