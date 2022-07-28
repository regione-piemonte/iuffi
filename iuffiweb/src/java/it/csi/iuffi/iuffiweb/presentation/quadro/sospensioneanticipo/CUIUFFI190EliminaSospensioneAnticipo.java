package it.csi.iuffi.iuffiweb.presentation.quadro.sospensioneanticipo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-190", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi190")
public class CUIUFFI190EliminaSospensioneAnticipo extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEjb;

  @RequestMapping(value = "popupindex", method = RequestMethod.GET)
  @IsPopup
  public String popupIndex(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    setModelDialogWarning(model,
        "Stai cercando di eliminare la sospensione dell'anticipo, vuoi continuare?",
        "../cuiuffi190/popupindex.do");
    return "dialog/conferma";
  }

  @IsPopup
  @RequestMapping(value = "popupindex", method = RequestMethod.POST)
  public String popupIndexPost(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    final ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);

    quadroEjb.eliminaSospensioneAnticipo(
        procedimentoOggetto.getIdProcedimentoOggetto(),
        getLogOperationOggettoQuadroDTO(session));
    return "redirect:../cuiuffi188/index.do";
  }

}
