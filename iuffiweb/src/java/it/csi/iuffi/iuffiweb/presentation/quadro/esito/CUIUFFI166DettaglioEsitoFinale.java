package it.csi.iuffi.iuffiweb.presentation.quadro.esito;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi166v")
@IuffiSecurity(value = "CU-IUFFI-166-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI166DettaglioEsitoFinale extends BaseController
{
  @Autowired
  IQuadroEJB  quadroEJB  = null;

  @Autowired
  IRicercaEJB ricercaEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    QuadroOggettoDTO quadro = po.findQuadroByCU("CU-IUFFI-166-V");

    EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(
        po.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto());

    List<DecodificaDTO<Long>> elencoAtti = quadroEJB.getElencoAtti();
    model.addAttribute("elencoAtti", elencoAtti);

    if("S".equals(po.getFlagAmmissione()) && "N".equals(esito==null?null:esito.getFlagAltreInfoAtto()))
    {
      DecodificaDTO<String> prot = ricercaEJB.findProtocolloPratica(
          po.getIdProcedimentoOggetto(),
          IuffiConstants.IUFFI.ID_CATEGORIA_DOC_LETTERE_IUF_SU_DOQUIAGRI);
      esito.setNumeroAtto(prot.getCodice());
      esito.setDataAtto(IuffiUtils.DATE.parseDate(prot.getDescrizione()));
    }
    model.addAttribute("esito", esito);
    model.addAttribute("flagAmmisioneS", po.getFlagAmmissione());
    model.addAttribute("attiVisibili", po.getFlagAmmissione());
    return "esitofinale/dettaglioDati";
  }

}