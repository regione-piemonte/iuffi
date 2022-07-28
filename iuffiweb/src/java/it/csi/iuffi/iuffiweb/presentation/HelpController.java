package it.csi.iuffi.iuffiweb.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.INuovoProcedimentoEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping(value = "/help")
@IuffiSecurity(value = "", controllo = IuffiSecurity.Controllo.NESSUNO)
public class HelpController extends BaseController
{
  @Autowired
  private INuovoProcedimentoEJB nuovoProcedimento = null;

  @RequestMapping(value = "gethelp_{codcdu}", method = RequestMethod.POST)
  @ResponseBody
  public String gethelp(@PathVariable("codcdu") String codcdu, Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_HELP_IS_ACTIVE,
        IuffiConstants.FLAGS.SI);
    ProcedimentoOggetto procedimentoOggetto = IuffiFactory
        .getProcedimentoOggetto(request);
    if (procedimentoOggetto == null)
    {
      return nuovoProcedimento.getHelpCdu(codcdu, null);
    }

    QuadroOggettoDTO qo = procedimentoOggetto.findQuadroByCU(codcdu);
    if (qo == null)
    {
      return nuovoProcedimento.getHelpCdu(codcdu, null);
    }

    return nuovoProcedimento.getHelpCdu(codcdu, qo.getIdQuadroOggetto());
  }

  @RequestMapping(value = "cleanhelp_{codcdu}", method = RequestMethod.POST)
  @ResponseBody
  public String cleanhelp(@PathVariable("codcdu") String codcdu, Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_HELP_IS_ACTIVE,
        IuffiConstants.FLAGS.NO);
    return "OK";
  }
}
