package it.csi.iuffi.iuffiweb.presentation.quadro.punteggi;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.punteggi.RaggruppamentoLivelloCriterio;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-160", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi160")
public class CUIUFFI160DettaglioPunti extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEjb;

  @RequestMapping("/index")
  public final String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    List<RaggruppamentoLivelloCriterio> lista = null;
    final boolean presentiCriteriManuali = false;
    final ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    final long idProcedimentoOggetto = po.getIdProcedimentoOggetto();
    final Long idDatiProcedimentoPunti = this.quadroEjb
        .getIdDatiProcedimentoPunti(idProcedimentoOggetto);
    final QuadroOggettoDTO quadroOgg = po.findQuadroByCodiceQuadro("PUNTI");
    Procedimento procedimento = getProcedimentoFromSession(session);
    if (idDatiProcedimentoPunti != null)
    {
      lista = this.quadroEjb.getCriteriPunteggio(quadroOgg.getIdOggetto(),
          idDatiProcedimentoPunti, procedimento.getIdBando());
      model.addAttribute("listaRaggruppamento", lista);
    }
    model.addAttribute("isOggettoIstanza",
        this.quadroEjb.isOggettoIstanza(quadroOgg.getIdOggetto()));
    model.addAttribute("ultimaModifica",
        getUltimaModifica(quadroEjb, idProcedimentoOggetto,
            quadroOgg.getIdQuadroOggetto(), po.getIdBandoOggetto()));
    model.addAttribute("presentiCriteriManuali", presentiCriteriManuali);
    model.addAttribute("calcoloPuntiEffettuato", this.quadroEjb
        .isCalcoloPuntiEseguito(procedimento.getIdProcedimento()));
    return "punteggi/dettaglioPunteggi";
  }
}
