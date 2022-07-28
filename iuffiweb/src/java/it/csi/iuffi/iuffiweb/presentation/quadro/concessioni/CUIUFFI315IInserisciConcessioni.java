package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.PraticaConcessioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.EnteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-315-I", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi315i")
public class CUIUFFI315IInserisciConcessioni extends BaseController
{

  @Autowired
  IQuadroEJB      quadroEJB;
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @IsPopup
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String inserisci(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {

    prepareModel(model, request);
    return "concessioni/inserisci";
  }

  private void prepareModel(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    UtenteAbilitazioni u = getUtenteAbilitazioni(request.getSession());
    List<Long> idAmmComp = new ArrayList<>();
    
    if(u.getEntiAbilitati()!=null)
    for(EnteLogin ente : u.getEntiAbilitati())
    {
      if(ente.getAmmCompetenza()!=null)
        idAmmComp.add(ente.getAmmCompetenza().getIdAmmCompetenza());
    }
    
    List<DecodificaDTO<Long>> ammComp = quadroIuffiEJB.getElencoAmmCompetenza(idAmmComp, null);
    model.addAttribute("elencoAmmCompetenza", ammComp);
    model.addAttribute("modificaInserisci", "Inserisci");
  }

  /**
   * Questo metodo riceve in request l'idBando e l'idAmmCompetenza selezionati.
   * In base a questi carica l'elenco delle pratiche legate a quel bando e di competenza dell'amministrazione selezionata.
   * Se non ci sono errori ricarica la pagina visualizzando l'elenco delle pratiche (se ce ne sono).
   * Se ci sono errori ripropone la pagina precedente segnalandoli
   */
  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String index(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {

    Map<String, Object> commons = getCommonFromSession("CUIUFFI315", request.getSession(), true);
    commons.clear();
    saveCommonInSession(commons, request.getSession());
    Errors errors = new Errors();
    ConcessioneDTO concessione = new ConcessioneDTO();
    prepareModel(model, request);

    String note = request.getParameter("note");
    errors.validateFieldLength(note, "note", 0, 4000);
    
    Long idBando = errors.validateMandatoryLong(request.getParameter("idBando"), "idBando");
    Long idAmmCompetenza = errors.validateMandatoryLong(request.getParameter("ammCompetenza"), "ammCompetenza");

    if (errors.isEmpty())
    {
      concessione.setIdBando(idBando);
      concessione.setIdAmmCompetenza(idAmmCompetenza);
      concessione.setNote(note);
      
      List<PraticaConcessioneDTO> elencoPratiche = quadroIuffiEJB.getElencoPraticheConcessione(concessione.getIdBando(), concessione.getIdAmmCompetenza());
      if (elencoPratiche != null && !elencoPratiche.isEmpty())
      {
        model.addAttribute("elencoPratiche", elencoPratiche);
      }
      
      model.addAttribute("praticheCaricate", true);
      model.addAttribute("concessione", concessione);
      commons = getCommonFromSession("CUIUFFI315", request.getSession(), true);
      commons.put("concessione", concessione);
      saveCommonInSession(commons, request.getSession());
      return "concessioni/inserisci";
    }

    model.addAttribute("errors", errors);
    model.addAttribute("perferRequest", true);

    return "concessioni/inserisci";
  }

  /**
   * 
   * Questo metodo invece valida tutti i dati inseriti, inclusi quelli relativi alle pratiche e, se non ci sono errori, 
   * si occupa dell'inserimento. 
   */
  @RequestMapping(value = "/conferma", method = RequestMethod.POST)
  public String conferma(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    ConcessioneDTO concessione = new ConcessioneDTO();
    String note = request.getParameter("note");
    errors.validateFieldLength(note, "note", 0, 4000);
    Long idBando = errors.validateMandatoryLong(request.getParameter("idBando"), "idBando");
    Long idAmmCompetenza = errors.validateMandatoryLong(request.getParameter("ammCompetenza"), "ammCompetenza");

    if (errors.isEmpty())
    {
      concessione.setIdBando(idBando);
      concessione.setIdAmmCompetenza(idAmmCompetenza);
      concessione.setNote(note);

      List<PraticaConcessioneDTO> elencoPratiche = quadroIuffiEJB.getElencoPraticheConcessione(concessione.getIdBando(), concessione.getIdAmmCompetenza());
      if (elencoPratiche != null && !elencoPratiche.isEmpty())
      {
        for (PraticaConcessioneDTO pratica : elencoPratiche)
        {
          // VALIDA PRATICHE
          String notePratica = request.getParameter("note_"+pratica.getIdPratica());
          errors.validateFieldLength(notePratica, "note_"+pratica.getIdPratica(),  0, 4000);
          pratica.setNote(notePratica);
          pratica.setImportoLiquidazione(pratica.getImportoPerizia());
        }
        concessione.setPratiche(elencoPratiche);
        model.addAttribute("elencoPratiche", elencoPratiche);
      }
      model.addAttribute("praticheCaricate", true);

    }

    if (errors.isEmpty())
    {
      quadroIuffiEJB.inserisciConcessione(concessione, getIdUtenteLogin(request.getSession()));
      return "redirect:../cuiuffi315v/index.do";
    }
    
    model.addAttribute("errors", errors);
    model.addAttribute("perferRequest", true);
    prepareModel(model, request);

    return "concessioni/inserisci";
  }

  @RequestMapping(value = "/getElencoPraticheConcessione_{idConcessione}", method = RequestMethod.POST)
  @ResponseBody
  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(Model model, HttpSession session, @PathVariable(value = "idConcessione") long idConcessione) throws InternalUnexpectedException
  {
    if (idConcessione == -1l)
      return new ArrayList<PraticaConcessioneDTO>();
    return quadroIuffiEJB.getElencoPraticheConcessione(idConcessione);
  }

  @RequestMapping(value = "/getElencoPraticheConcessione_{idBando}_{idAmministrazione}", method = RequestMethod.POST)
  @ResponseBody
  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(Model model, HttpSession session, @PathVariable(value = "idBando") long idBando, @PathVariable(value = "idAmministrazione") long idAmministrazione) throws InternalUnexpectedException
  {
    if (idBando == -1l || idAmministrazione == -1)
      return new ArrayList<PraticaConcessioneDTO>();
    return quadroIuffiEJB.getElencoPraticheConcessione(idBando, idAmministrazione);
  }

}
