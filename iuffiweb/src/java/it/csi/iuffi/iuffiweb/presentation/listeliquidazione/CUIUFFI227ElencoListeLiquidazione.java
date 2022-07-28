package it.csi.iuffi.iuffiweb.presentation.listeliquidazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoPraticheApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.presentation.listeliquidazione.CUIUFFI226CreaNuovaListaLiquidazione.BandoOperazioneParametersVO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.AmmCompetenza;
import it.csi.papua.papuaserv.dto.gestioneutenti.EnteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi227")
@IuffiSecurity(value = "CU-IUFFI-227", controllo = IuffiSecurity.Controllo.DEFAULT)
public class CUIUFFI227ElencoListeLiquidazione extends BaseController
{
  private static final String BASE_JSP_URL         = "listeliquidazione/";
  @Autowired
  IListeLiquidazioneEJB       listeLiquidazioneEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {
    String filtroIniziale = null;
    @SuppressWarnings("unchecked")
    Map<String, String> filtroInSessione = (Map<String, String>) request
        .getSession()
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    if (filtroInSessione != null)
    {
      filtroIniziale = filtroInSessione.get("elencoListeLiquidazione");
    }
    if (filtroIniziale == null)
    {
      filtroIniziale = getFiltroIniziale(
          getUtenteAbilitazioni(request.getSession()));
    }
    model.addAttribute("filtroIniziale", filtroIniziale);
    return BASE_JSP_URL + "elenco";
  }

  private String getFiltroIniziale(UtenteAbilitazioni utenteAbilitazioni)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"extIdAmmCompetenza\":{\"_values\":[");
    AmmCompetenza ammCompetenza = null;
    boolean comma = false;
    for (EnteLogin ente : utenteAbilitazioni.getEntiAbilitati())
    {
      ammCompetenza = ente.getAmmCompetenza();
      if (ammCompetenza != null)
      {
        if (comma)
        {
          sb.append(", ");
        }
        sb.append("\"").append(ammCompetenza.getIdAmmCompetenza()).append("\"");
        comma = true;
      }
    }
    sb.append("]}}");
    return sb.toString();
  }

  @ResponseBody
  @RequestMapping(value = "/json/elenco", method = RequestMethod.GET)
  public List<RigaJSONElencoListaLiquidazioneDTO> jsonElenco()
      throws InternalUnexpectedException
  {
    List<RigaJSONElencoListaLiquidazioneDTO> listeLiquidazione = listeLiquidazioneEJB
        .getListeLiquidazione();
    if (listeLiquidazione == null)
    {
      listeLiquidazione = new ArrayList<RigaJSONElencoListaLiquidazioneDTO>();
    }
    return listeLiquidazione;
  }

  @ResponseBody
  @RequestMapping(value = "/json/ammCompetenzeListe", method = RequestMethod.GET)
  public List<Map<String, Object>> jsonAmmCompetenzeListe()
      throws InternalUnexpectedException
  {
    return listeLiquidazioneEJB.getAmministrazioniCompetenzaListe();
  }

  @RequestMapping(value = "/json/elenco_pratiche_nuova_lista_{idListaLiquidazione}", method = RequestMethod.GET)
  @IsPopup
  public String elencoPraticheNuovaLista(HttpSession session, Model model,
      @ModelAttribute("idListaLiquidazione") @PathVariable("idListaLiquidazione") long idListaLiquidazione)
      throws InternalUnexpectedException
  {

    Long idBando = listeLiquidazioneEJB
        .getIdBandoByIdListaLiquidazione(idListaLiquidazione);
    BandoDTO bando = listeLiquidazioneEJB.getInformazioniBando(idBando);
    if (bando != null && bando.getCodiceTipoBando().compareTo("P") == 0)
      model.addAttribute("isBandoPremio", true);

    model.addAttribute("idListaLiquidazione", idListaLiquidazione);

    model.addAttribute("readOnly", true);

    return BASE_JSP_URL + "elencoPratiche";
  }

  @ResponseBody
  @RequestMapping(value = "/json/pratiche_nuova_lista_{idListaLiquidazione}", method = RequestMethod.GET, produces = "application/json")
  public List<RiepilogoPraticheApprovazioneDTO> jsonRiepilogoPratiche(
      HttpSession session,
      @PathVariable("idListaLiquidazione") long idListaLiquidazione,
      BandoOperazioneParametersVO parameters)
      throws InternalUnexpectedException
  {
    Long idBando = listeLiquidazioneEJB
        .getIdBandoByIdListaLiquidazione(idListaLiquidazione);
    BandoDTO bando = listeLiquidazioneEJB.getInformazioniBando(idBando);
    boolean isPremio = false;
    if (bando != null && bando.getCodiceTipoBando().compareTo("P") == 0)
      isPremio = true;
    List<RiepilogoPraticheApprovazioneDTO> riepilogo = listeLiquidazioneEJB
        .getRiepilogoPraticheListaLiquidazione(idListaLiquidazione, isPremio);
    if (riepilogo == null)
    {
      riepilogo = new ArrayList<RiepilogoPraticheApprovazioneDTO>();
    }

    return riepilogo;
  }

  @RequestMapping(value = "downloadExcelPratiche_{idListaLiquidazione}")
  public ModelAndView downloadExcel(HttpSession session,
      @PathVariable("idListaLiquidazione") long idListaLiquidazione,
      Model model)
      throws InternalServiceException, InternalUnexpectedException
  {

    Long idBando = listeLiquidazioneEJB
        .getIdBandoByIdListaLiquidazione(idListaLiquidazione);
    BandoDTO bando = listeLiquidazioneEJB.getInformazioniBando(idBando);
    boolean isPremio = false;
    if (bando != null && bando.getCodiceTipoBando().compareTo("P") == 0)
      isPremio = true;

    List<RiepilogoPraticheApprovazioneDTO> pratiche = listeLiquidazioneEJB
        .getRiepilogoPraticheListaLiquidazione(idListaLiquidazione, isPremio);

    return new ModelAndView("excelElencoPraticheListaLiquidazioneView",
        "pratiche", pratiche);
  }

  @RequestMapping(value = "getElencoCodiciOperazioneJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = listeLiquidazioneEJB.getElencoLivelli();
    List<LivelloDTO> liv = new LinkedList<LivelloDTO>();
    boolean aggiungi = true;

    for (LivelloDTO item : livelli)
    {
      for (LivelloDTO d : liv)
      {
        if (d.getCodiceLivello().compareTo(item.getCodiceLivello()) == 0)
        {
          aggiungi = false;
        }
      }

      if (aggiungi)
        liv.add(item);
      aggiungi = true;
    }

    Map<String, Object> stato;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (LivelloDTO item : liv)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodiceLivello());
      stato.put("label", item.getCodiceLivello());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoCodiciLivelliMisureJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliMisureJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = listeLiquidazioneEJB.getElencoLivelli();
    Map<String, Object> stato;
    List<String> valList = new ArrayList<String>();
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (LivelloDTO item : livelli)
    {
      if (!valList.contains(item.getCodiceMisura()))
      {
        stato = new HashMap<String, Object>();
        stato.put("id", item.getCodiceMisura());
        stato.put("label", item.getCodiceMisura());
        ret.add(stato);
        valList.add(item.getCodiceMisura());
      }
    }

    return ret;
  }

}
