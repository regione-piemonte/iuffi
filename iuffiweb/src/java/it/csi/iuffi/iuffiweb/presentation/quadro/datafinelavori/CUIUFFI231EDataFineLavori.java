package it.csi.iuffi.iuffiweb.presentation.quadro.datafinelavori;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-231-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi231e")
public class CUIUFFI231EDataFineLavori extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEjb;

  @RequestMapping(value = "popupindex_{idFineLavori}", method = RequestMethod.GET)
  @IsPopup
  public String popupIndex(Model model, HttpSession session,
      @PathVariable(value = "idFineLavori") long idFineLavori)
      throws InternalUnexpectedException
  {

    setModelDialogWarning(model,
        "Stai cercando di eliminare il record, vuoi continuare?",
        "../cuiuffi231e/popupindex_" + idFineLavori + ".do");
    return "dialog/conferma";
  }

  @IsPopup
  @RequestMapping(value = "popupindex_{idFineLavori}", method = RequestMethod.POST)
  public String popupIndexPost(Model model, HttpSession session,
      HttpServletRequest request,
      @PathVariable(value = "idFineLavori") long idFineLavori)
      throws InternalUnexpectedException
  {
    quadroEjb.deleteDataFineLavori(idFineLavori,
        getLogOperationOggettoQuadroDTO(session));
    return "redirect:../cuiuffi231l/index.do";
  }

}
