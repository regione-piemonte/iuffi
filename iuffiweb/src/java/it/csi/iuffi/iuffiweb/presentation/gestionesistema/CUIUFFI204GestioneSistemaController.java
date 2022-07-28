package it.csi.iuffi.iuffiweb.presentation.gestionesistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.Link;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.MessaggisticaBaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;

@Controller
@RequestMapping(value = "/cuiuffi204")
@IuffiSecurity(value = "CU-IUFFI-204", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class CUIUFFI204GestioneSistemaController
    extends MessaggisticaBaseController
{
  @Autowired
  private IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session,
      HttpServletResponse response) throws InternalUnexpectedException
  {
    List<Link> links = new ArrayList<Link>();
    Map<String, String> mapCdU = quadroEJB.getMapHelpCdu(
        IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.MONITORAGGIO,
        IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.REFRESH_ELENCO_CDU);
    links.add(new Link("../cuiuffi206/index.do",
        IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.MONITORAGGIO, false,
        "Stato del sistema", mapCdU
            .get(
                IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.MONITORAGGIO)));
    links.add(new Link("refresh_elenco_cdu.do",
        IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.REFRESH_ELENCO_CDU,
        false, "Rilettura Elenco CDU", mapCdU
            .get(
                IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.REFRESH_ELENCO_CDU),
        "openPageInPopup('../cuiuffi205/index.do','dlgRefreshCdu','Rilettura Elenco CDU', 'modal-large');return false;"));
    
    links.add(new Link("../cuiuffi2009/index.do",
        IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.RICERCA_LOG, false,
        "Ricerca Log", mapCdU
            .get(
                IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.RICERCA_LOG)));
    
    
    model.addAttribute("links", links);
    return "gestionesistema/elencoUtilita";
  }

}
