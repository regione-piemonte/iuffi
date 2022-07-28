package it.csi.iuffi.iuffiweb.presentation.quadro.quadrodinamico;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroDinamicoEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.QuadroDinamicoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi116e_{codiceQuadro}")
@IuffiSecurity(value = "CU-IUFFI-116", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO, tipoMapping = IuffiSecurity.TipoMapping.QUADRO_DINAMICO)
public class CUIUFFI116EElencoDatiQuadroDinamicoRecordMultipli
    extends BaseController
{
  @Autowired
  IQuadroDinamicoEJB quadroDinamicoEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String dettaglio(Model model, HttpSession session,
      @PathVariable("codiceQuadro") String codiceQuadro)
      throws ApplicationException, InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    model.addAttribute("codiceQuadroLC", codiceQuadro);
    codiceQuadro = codiceQuadro.toUpperCase();
    String cu = "CU-IUFFI-116-E_" + codiceQuadro;
    model.addAttribute("useCase", cu);
    QuadroDTO quadroDTO = po.findQuadroByCU(cu);
    model.addAttribute("quadro", quadroDTO);
    QuadroDinamicoDTO quadroDinamico = quadroDinamicoEJB
        .getQuadroDinamico(codiceQuadro, po.getIdProcedimentoOggetto(), null);
    verificaTipologiaQuadroDinamico(quadroDinamico);
    model.addAttribute("quadroDinamico", quadroDinamico);
    Errors errors = (Errors) session.getAttribute("qd_errors");
    if (errors != null)
    {
      errors.addToModelIfNotEmpty(model);
      session.removeAttribute("qd_errors");
    }
    return "quadridinamici/recordMultipli";
  }

  @RequestMapping(value = "/dettaglio_{numProgressivoRecord}", method = RequestMethod.GET)
  public String dettaglio(Model model, HttpSession session,
      @PathVariable("codiceQuadro") String codiceQuadro,
      @PathVariable("numProgressivoRecord") Long numProgressivoRecord)
      throws ApplicationException, InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    model.addAttribute("codiceQuadroLC", codiceQuadro);
    codiceQuadro = codiceQuadro.toUpperCase();
    String cu = "CU-IUFFI-116-E_" + codiceQuadro;
    model.addAttribute("useCase", cu);
    QuadroDTO quadroDTO = po.findQuadroByCU(cu);
    model.addAttribute("quadro", quadroDTO);
    QuadroDinamicoDTO quadroDinamico = quadroDinamicoEJB.getQuadroDinamico(
        codiceQuadro, po.getIdProcedimentoOggetto(), numProgressivoRecord);
    if (quadroDinamico.getNumRecords() != 1)
    {
      throw new ApplicationException(
          "Impossibile trovare l'elemento indicato. Potrebbe essere stato cancellato da qualche altro utente");
    }
    verificaTipologiaQuadroDinamico(quadroDinamico);
    model.addAttribute("quadroDinamico", quadroDinamico);
    model.addAttribute("btnBack", Boolean.TRUE);
    return "quadridinamici/recordSingolo";
  }

  protected void verificaTipologiaQuadroDinamico(
      QuadroDinamicoDTO quadroDinamico) throws ApplicationException
  {
    if (!IuffiConstants.FLAGS.SI
        .equals(quadroDinamico.getFlagVisualizzazioneElenco()))
    {
      throw new ApplicationException(
          "Si è verificato un errore interno grave. Contattare l'assistenza tecnica comunicando il seguente messaggio: "
              +
              "Il quadro dinamico #" + quadroDinamico.getIdQuadro()
              + " è associato al caso d'uso di quadro dinamico con elenco ma è stato configurato come quadro a record singolo");
    }
  }
}