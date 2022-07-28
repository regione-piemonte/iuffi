package it.csi.iuffi.iuffiweb.presentation.quadro.regimeaiuto;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.regimeaiuto.RegimeAiuto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi2001d")
@IuffiSecurity(value = "CU-IUFFI-2001-D", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI2001RegimeAiuto extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-2001-D";
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String dettaglio(Model model, HttpSession session) throws InternalUnexpectedException { 
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(session);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);   
    RegimeAiuto datiProcedimento = quadroEJB.getRegimeAiutoProcedimentoOggetto(procedimentoOggetto.getIdProcedimentoOggetto());
    model.addAttribute("datiProcedimento", datiProcedimento);
    model.addAttribute("datiInseriti", datiProcedimento != null);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB, procedimentoOggetto.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));  
    return "regimeaiuto/dettaglioRegimeAiuto";
  }
}