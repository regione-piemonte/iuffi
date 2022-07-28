package it.csi.iuffi.iuffiweb.presentation.quadro.trasformazioneprodotti;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.trasformazioneprodotti.ProdottoTrasformato;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi321v")
@IuffiSecurity(value = "CU-IUFFI-321-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI321VisualizzaTrasformazioneProdotti extends BaseController {
  public static final String CU_NAME = "CU-IUFFI-321-V";
  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping("/index")
  public String index(Model model, HttpSession session) throws InternalUnexpectedException {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);    
    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
    List<ProdottoTrasformato> listaProdottoTrasformato = quadroEJB.getProdottoTrasformato(idDatiProcedimento);

    model.addAttribute("listaProdottoTrasformato", listaProdottoTrasformato);

    model.addAttribute("datiInseriti", listaProdottoTrasformato != null && listaProdottoTrasformato.size() > 0);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
        idProcedimentoOggetto,
        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "trasformazioneprodotti/dettaglioTrasformazioneProdotti";
  }
  

}
