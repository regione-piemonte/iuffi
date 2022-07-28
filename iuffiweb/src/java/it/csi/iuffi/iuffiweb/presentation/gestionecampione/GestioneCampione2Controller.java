package it.csi.iuffi.iuffiweb.presentation.gestionecampione;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.Link;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.CampionamentoDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.presentation.MessaggisticaBaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;

@Controller
@RequestMapping(value = "/campioni")
@IuffiSecurity(value = "CAMPIONE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
public class GestioneCampione2Controller   extends MessaggisticaBaseController
{
  @Autowired
  private IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session,
      HttpServletResponse response) throws InternalUnexpectedException
  {
    List<Link> links = new ArrayList<Link>();
    //Map<String, String> mapCdU = quadroEJB.getMapHelpCdu(IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.MONITORAGGIO,IuffiConstants.USECASE.GESTIONE_SISTEMA_FUNZIONI.REFRESH_ELENCO_CDU);
    
    links.add(new Link("nuovaMissione.do",IuffiConstants.USECASE.LISTE_GESTIONE_CAMPIONE.RICERCA, false,
        "Ricerca Campioni", IuffiConstants.USECASE.LISTE_GESTIONE_CAMPIONE.RICERCA));
    
    links.add(new Link("nuovaMissione.do",IuffiConstants.USECASE.LISTE_GESTIONE_CAMPIONE.DETTAGLIO, false, 
        "Visualizza dettaglio campione", IuffiConstants.USECASE.LISTE_GESTIONE_CAMPIONE.DETTAGLIO));

    links.add(new Link("nuovaMissione.do",IuffiConstants.USECASE.LISTE_GESTIONE_CAMPIONE.ANFI, false, 
        "Gestione ANFI", IuffiConstants.USECASE.LISTE_GESTIONE_CAMPIONE.ANFI));
       // "openPageInPopup('../cuiuffi205/index.do','dlgRefreshCdu','Rilettura Elenco CDU', 'modal-large');return false;"));
        
    model.addAttribute("links", links);
    return "gestionecampioni/elencoUtilita";
  }

  @RequestMapping(value = "/campioneJson")
  public ResponseEntity<CampionamentoDTO> campioneJsonOutput()
  {

    return null;

  }
 
  
  @RequestMapping(value = "/campioneJsonInput",
                 consumes = { "application/json" }, 
                 produces = { "application/json" },
                 method = RequestMethod.POST)
   public ResponseEntity<CampionamentoDTO> campionamentoJsonInput(@Valid @RequestBody CampionamentoDTO body) throws MalformedURLException, IOException
  {
    
      body.getIdCampionamento();

      CampionamentoDTO sample = new CampionamentoDTO();

        return new ResponseEntity<CampionamentoDTO>(sample,HttpStatus.OK);

  }

  
  
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  
}