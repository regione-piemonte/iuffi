package it.csi.iuffi.iuffiweb.presentation.quadro.controlloinloco;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-181", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi181")
public class CUIUFFI181EliminaControlloInLoco extends BaseController
{

  @RequestMapping(value = "popupindex", method = RequestMethod.GET)
  @IsPopup
  public String popupIndex(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    setModelDialogWarning(model,
        "Stai cercando di eliminare l'operazione selezionata, vuoi continuare?",
        "../cuiuffi184/popupindex.do");
    return "dialog/conferma";
  }

  @IsPopup
  @RequestMapping(value = "popupindex", method = RequestMethod.POST)
  public String popupIndexPost(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {

    return "redirect:../cuiuffi185/index.do";
  }

}
