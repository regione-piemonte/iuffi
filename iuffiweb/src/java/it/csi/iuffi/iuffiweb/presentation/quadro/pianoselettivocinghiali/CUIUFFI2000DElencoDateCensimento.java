package it.csi.iuffi.iuffiweb.presentation.quadro.pianoselettivocinghiali;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2000-D", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi2000d")
public class CUIUFFI2000DElencoDateCensimento extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;
  
  @RequestMapping(value = "/index_{idInfoCinghiali}", method = RequestMethod.GET)
  public String index(@PathVariable("idInfoCinghiali") long idConcessione, Model model, HttpServletRequest request, HttpSession session) throws InternalUnexpectedException
  {
    
    return "pianoselettivocinghiali/elencoDate";
  }

  
}
