package it.csi.iuffi.iuffiweb.presentation.quadro.quadrodinamico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroDinamicoEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.QuadroDinamicoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi118_{codiceQuadro}")
@IuffiSecurity(value = "CU-IUFFI-118", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO, tipoMapping = IuffiSecurity.TipoMapping.QUADRO_DINAMICO)
public class CUIUFFI118EliminaRecordQuadroDinamico extends BaseController
{
  @Autowired
  IQuadroDinamicoEJB quadroDinamicoEJB;

  @IsPopup
  @RequestMapping(value = "/index_{numProgressivoRecord}", method = RequestMethod.GET)
  public String dettaglio(Model model, HttpSession session,
      @PathVariable("codiceQuadro") String codiceQuadro,
      @ModelAttribute("numProgressivoRecord") @PathVariable("numProgressivoRecord") int numProgressivoRecord)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    model.addAttribute("codiceQuadroLC", codiceQuadro);
    codiceQuadro = codiceQuadro.toUpperCase();
    String cu = "CU-IUFFI-118_" + codiceQuadro;
    model.addAttribute("useCase", cu);
    QuadroDTO quadroDTO = po.findQuadroByCU(cu);
    model.addAttribute("quadro", quadroDTO);
    QuadroDinamicoDTO quadroDinamico = quadroDinamicoEJB.getQuadroDinamico(
        codiceQuadro, po.getIdProcedimentoOggetto(),
        (long) numProgressivoRecord);
    model.addAttribute("quadroDinamico", quadroDinamico);
    return "quadridinamici/confermaElimina";
  }

  @RequestMapping(value = "/elimina_{numProgressivoRecord}", method = RequestMethod.POST)
  public String modifica(Model model, HttpServletRequest request,
      @PathVariable("codiceQuadro") String codiceQuadro,
      @PathVariable("numProgressivoRecord") int numProgressivoRecord)
      throws InternalUnexpectedException, ApplicationException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(
        request.getSession());
    model.addAttribute("codiceQuadroLC", codiceQuadro);
    String codiceQuadroLC = codiceQuadro;
    codiceQuadro = codiceQuadro.toUpperCase();
    String cu = "CU-IUFFI-118_" + codiceQuadro;
    model.addAttribute("useCase", cu);
    QuadroDTO quadroDTO = po.findQuadroByCU(cu);
    model.addAttribute("quadro", quadroDTO);
    LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
        "CU-IUFFI-118_" + quadroDTO.getCodQuadro(),
        request.getSession());
    String errorMessage = quadroDinamicoEJB.eliminaRecordQuadroDinamico(
        quadroDTO.getIdQuadro(), numProgressivoRecord,
        logOperationOggettoQuadroDTO);
    if (errorMessage != null)
    {
      Errors errors = new Errors();
      errors.addError("error", errorMessage);
      request.getSession().setAttribute("qd_errors", errors);
    }
    return "redirect:../procedimento/quadro_" + codiceQuadroLC + ".do";
  }
}