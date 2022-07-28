package it.csi.iuffi.iuffiweb.presentation.quadro.punteggi;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.GraduatoriaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-175", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi175")
public class CUIUFFI175PosizioniGraduatoria extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEjb;

  @RequestMapping("/index")
  public final String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    final Procedimento procedimento = getProcedimentoFromSession(session);
    final long idProcedimento = procedimento.getIdProcedimento();

    List<GraduatoriaDTO> graduatorie = quadroEjb
        .getGraduatorieByIdProcedimento(idProcedimento);
    if (graduatorie != null && graduatorie.size() <= 0)
      graduatorie = null;
    model.addAttribute("elencoGraduatorie", graduatorie);

    return "punteggi/posizioneGraduatoria";
  }

  @RequestMapping(value = "getElencoGraduatorie", produces = "application/json")
  @ResponseBody
  public List<GraduatoriaDTO> getGraduatorieJson(Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    final Procedimento procedimento = getProcedimentoFromSession(session);
    final long idProcedimento = procedimento.getIdProcedimento();

    List<GraduatoriaDTO> graduatorie = quadroEjb
        .getGraduatorieByIdProcedimento(idProcedimento);

    return graduatorie;
  }
}
