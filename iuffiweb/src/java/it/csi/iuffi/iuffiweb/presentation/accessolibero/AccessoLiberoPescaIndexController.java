package it.csi.iuffi.iuffiweb.presentation.accessolibero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.Link;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;

@Controller
@NoLoginRequired
public class AccessoLiberoPescaIndexController extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB;

  @RequestMapping(value = "/licenzepesca/index", method = RequestMethod.GET)
  public String getIndex(ModelMap model, HttpSession session)
      throws InternalUnexpectedException
  {
    session.setAttribute("webContext", IuffiConstants.IUFFIWEB.WEB_CONTEXT);
    List<Link> links = new ArrayList<Link>();
    Map<String, String> mapCdU = quadroEJB.getMapHelpCdu(
        IuffiConstants.USECASE.ACCESSO_LIBERO_PAGAMENTO_PESCATORI,
        IuffiConstants.USECASE.ACCESSO_LIBERO_RICERCA_IUV
        );

    links.add(new Link("cuiuffi3001/index.do", IuffiConstants.USECASE.ACCESSO_LIBERO_PAGAMENTO_PESCATORI, "Rinnova Licenza di pesca", mapCdU
            .get(IuffiConstants.USECASE.ACCESSO_LIBERO_PAGAMENTO_PESCATORI)));
    links.add(new Link("cuiuffi3002/index.do", IuffiConstants.USECASE.ACCESSO_LIBERO_RICERCA_IUV, "Ricerca Codice avviso", mapCdU
        .get(IuffiConstants.USECASE.ACCESSO_LIBERO_RICERCA_IUV)));
    
    
    model.addAttribute("links", links);
    return "accessolibero/index";
  }
}
