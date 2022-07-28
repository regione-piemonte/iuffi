package it.csi.iuffi.iuffiweb.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IMenuItemEJB;
import it.csi.iuffi.iuffiweb.dto.Link;
import it.csi.iuffi.iuffiweb.dto.MenuItemDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.Abilitazione;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "N/A", controllo = IuffiSecurity.Controllo.NESSUNO)
public class IndexController extends BaseController
{

  //@Autowired
  //IQuadroEJB quadroEJB;

  @Autowired
  private IMenuItemEJB menuItemEJB;
  
  @RequestMapping("/index")
  public String redirectIndex(ModelMap model, HttpSession session)
      throws InternalUnexpectedException
  {
    clearCommonInSession(session);
    IuffiFactory.removeIdProcedimentoInSession(session);
    if (getUtenteAbilitazioni(session).getRuolo().isUtenteMonitoraggio())
    {
      return "redirect:cuiuffi206/index.do";
    }
    return "redirect:home/index.do";
  }

  @RequestMapping(value = "/home/index", method = RequestMethod.GET)
  public String getIndex(ModelMap model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    clearCommonInSession(session);
    cleanSession(session);    // Modificato il 05/01/2021 (S.D.)
    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("tableMissione");
    tableNamesToRemove.add("tableGps");
    tableNamesToRemove.add("tableFoto");
    tableNamesToRemove.add("tableCampioni");
    tableNamesToRemove.add("tableTrappole");
    tableNamesToRemove.add("tableVisual");
    cleanTableMapsInSession(session, tableNamesToRemove);
    // Pulisco la sessione dall'id_procedimento e id_procedimento_oggetto
    IuffiFactory.removeIdProcedimentoInSession(session);
    if (getUtenteAbilitazioni(session).getRuolo().isUtenteMonitoraggio())
    {
      return "redirect:cuiuffi206/index.do";
    }

    // Voci menu
    List<Link> links = new ArrayList<Link>();
    
    int idParent = (request.getParameter("idParent") != null) ? Integer.decode(request.getParameter("idParent")) : 0;
     
    UtenteAbilitazioni userEn = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
    
    Abilitazione[] abilitazioni = userEn.getAbilitazioni();
    long idLivello = 0;
    long[] idLivelli = new long[abilitazioni.length];
    
//    for(Abilitazione a : abilitazioni) {
//      if(a.getLivello() !=null) {
//        idLivello = a.getLivello().getIdLivello();
//      }
//    }
    
    for(int l=0; l<abilitazioni.length; l++) { 
      //Con idLivello trovo 6001, 6002, ecc.
        if(abilitazioni[l].getLivello()!=null) {
          idLivello = abilitazioni[l].getLivello().getIdLivello();  
          idLivelli[l] = idLivello;
        }
      }
    
      //  idLivello = 6003;
//    long idLivelli[] = new long[1];
//    idLivelli[0] = 6003;
    List<MenuItemDTO> list = menuItemEJB.getMainMenu(idParent, idLivelli, userEn.isReadOnly());
/*    
    List<MenuItemDTO> list = new ArrayList<MenuItemDTO>();
    if (userEn.isReadOnly()) {
      // Se l'utente è di sola lettura rimuovo eventuali voci relative a funzioni di scrittura
      for (MenuItemDTO menuItem : listMenu) {
        if (menuItem.getUseCase().startsWith("VISUALIZZA"))
          list.add(menuItem);
      }
    }
    else
      list = listMenu;
*/  
    String nextPath = null;
    if (list != null) {
      for (MenuItemDTO menuItem : list) {
        nextPath = (menuItem.getPath().equals("home/index.do")) ? nextPath = menuItem.getPath() + "?idParent=" + menuItem.getIdMenuItem() : menuItem.getPath();
        links.add(new Link(nextPath, menuItem.getUseCase(), false, menuItem.getTitleMenuItem(), menuItem.getUseCase()));
      }
    }

    model.addAttribute("links", links);

    List<Link> breadcrumbs = new ArrayList<Link>();
    
    list = menuItemEJB.getBreadcrumbs(idParent);
    nextPath = null;
    if (list != null) {
      for (MenuItemDTO menuItem : list) {
        nextPath = (menuItem.getPath().equals("home/index.do")) ? nextPath = menuItem.getPath() + "?idParent=" + menuItem.getIdMenuItem() : menuItem.getPath();
        breadcrumbs.add(new Link(nextPath, menuItem.getUseCase(), false, menuItem.getTitleMenuItem(), menuItem.getUseCase()));
      }
    }

    model.addAttribute("breadcrumbs", breadcrumbs);

    // Settaggio attributi in sessione per map service e basic authentication
    ResourceBundle res = safeGetBundle("config");
    //String mapServiceBaseUrl = (res != null) ? res.getString("mapservice.baseurl") : "http://agrigeoweb.csi.it/wmspiemonteagri/service";
    String mapServiceBaseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/proxywms";
    session.setAttribute("mapServiceBaseUrl", mapServiceBaseUrl);
    session.setAttribute("iuffiUsername", res.getString("iuffi.rest.username"));
    session.setAttribute("iuffiPassword", res.getString("iuffi.rest.password"));
    //
    session.setAttribute("currentPage", 1);       // resetto il numero di pagina e il numero di righe per pagina da visualizzare nelle bootstrap datatable
    session.setAttribute("currentPageSize", 10);  //
    //session.removeAttribute("foto");
    session.removeAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
    return "index";
  }
}
