package it.csi.iuffi.iuffiweb.presentation.quadro.controlloinloco;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoEstrattoVO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-179", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi179")
public class CUIUFFI179DettaglioControlloInLoco extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEjb;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public final String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    QuadroOggettoDTO quadro = getProcedimentoOggettoFromSession(session)
        .findQuadroByCU("CU-IUFFI-179");
    final ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);

    ProcedimentoEstrattoVO procEstratto = quadroEjb.getProcedimentoEstratto(
        procedimentoOggetto.getIdProcedimento(),
        procedimentoOggetto.getIdProcedimentoOggetto());

    model.addAttribute("procedimentoEstratto", procEstratto);

    List<DecodificaDTO<Long>> elencoTecnici = quadroEjb
        .getElencoTecniciDisponibili(
            procedimentoOggetto.getIdProcedimentoOggetto());

    LinkedList<LivelloDTO> livelli = quadroEjb.getLivelliControlloInLoco(
        procedimentoOggetto.getIdProcedimento(),
        procedimentoOggetto.getIdProcedimentoOggetto());
    if (livelli != null)
      for (LivelloDTO l : livelli)
        for (DecodificaDTO<Long> t : elencoTecnici)
          if (t.getId().longValue() == l.getExtIdTecnico().longValue())
            l.setDecodificaTecnico(t.getDescrizione());

    model.addAttribute("operazioni", livelli);
    model.addAttribute("ultimaModifica",
        getUltimaModifica(quadroEjb,
            procedimentoOggetto.getIdProcedimentoOggetto(),
            quadro.getIdQuadroOggetto(),
            procedimentoOggetto.getIdBandoOggetto()));

    return "controlloinloco/dettaglioControlloInLoco";
  }

}
