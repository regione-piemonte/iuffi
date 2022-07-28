package it.csi.iuffi.iuffiweb.presentation.ricercaprocedimento;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IEstrazioniEJB;
import it.csi.iuffi.iuffiweb.business.IReportisticaEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.AmmCompetenzaDTO;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.FocusAreaDTO;
import it.csi.iuffi.iuffiweb.dto.GravitaNotificaVO;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.MapColonneNascosteVO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.dto.RicercaProcedimentiVO;
import it.csi.iuffi.iuffiweb.dto.SettoriDiProduzioneDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.FlagEstrazioneDTO;
import it.csi.iuffi.iuffiweb.dto.gestioneeventi.EventiDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.OggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.EsitoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ParametriQueryReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ReportVO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.ComeFromEnumUtils;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/ricercaprocedimento")
@IuffiSecurity(value = "CU-IUFFI-102", controllo = IuffiSecurity.Controllo.DEFAULT)
public class RicercaProcedimentoController extends BaseController
{

  private static String    SESSION_NAME_ELENCO_PROCEDIMENTI = "elencoProcedimenti";
  // private static String SESSION_NAME_MAPPA_ESITI = "mappaEsiti";

  @Autowired
  private IRicercaEJB      ricercaEJB                       = null;

  @Autowired
  private IEstrazioniEJB   estrazioneEJB                    = null;

  @Autowired
  private IReportisticaEJB reportisticaEJB                  = null;

  /*
   * ===========================================================================
   * ==========
   */
  @SuppressWarnings("unchecked")
  /* Metodo richiamato quando si accede alla pagina */
  /*
   * ===========================================================================
   * ==========
   */
  @RequestMapping(value = "index", method = RequestMethod.GET)
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    cleanSession(session);
    /*
     * Rimuovi dalla sessione le info delle bootstrap-table elencate.
     */
    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("elencoProcedimenti");
    tableNamesToRemove.add("elencoParticelle");
    tableNamesToRemove.add("elencoParticelleIstruttoria");
    tableNamesToRemove.add("elencoPredisposizioneParticelle");
    tableNamesToRemove.add("elencoDocumenti");
    tableNamesToRemove.add("elencoInterventi");
    tableNamesToRemove.add("elencoOggetti");

    // IUFFI Tables
    tableNamesToRemove.add("tableElencoMotoriAgricoli"); // motori agricoli
    tableNamesToRemove.add("tableElencoSuperificiColture"); // superfici colture
    tableNamesToRemove.add("tableSuperificiColturePlvVegetale");
    tableNamesToRemove.add("tableDettaglioParticellareSuperificiColture");
    tableNamesToRemove.add("tableElencoAllevamenti"); // allevamenti
    tableNamesToRemove.add("tableListPlvZootecnicaDettaglio");
    tableNamesToRemove.add("tableDettaglioAllevamentiPlv");
    tableNamesToRemove.add("tableElencoFabbricati"); // fabbricati
    tableNamesToRemove.add("tableDettaglioFabbricati");
    tableNamesToRemove.add("tableElencoPrestitiAgrari"); // prestiti agrari
    tableNamesToRemove.add("tableElencoDanni"); // danni
    tableNamesToRemove.add("tblRicercaConduzioni");
    tableNamesToRemove.add("tblRicercaConduzioniRiepilogo");
    tableNamesToRemove.add("tblConduzioni");
    tableNamesToRemove.add("tableElencoScorte"); // scorte
    tableNamesToRemove.add("tableElencoScorte"); // scorte
    tableNamesToRemove.add("tableElencoDanniFauna"); // danni fauna CUIUFFI311L
    tableNamesToRemove.add("tableLocalizzazioneDanniFauna"); // danni fauna
                                                             // CUIUFFI311D
    cleanTableMapsInSession(session, tableNamesToRemove);

    final String ID_ELENCO_AZIENDE = "elencoProcedimenti";
    final String ID_ELENCO_PARTICELLE = "elencoParticelle";
    final String ID_ELENCO_PARTICELLE_ISTR = "elencoParticelleIstruttoria";
    final String ID_ELENCO_PPREDISP_PARTICELLE = "elencoPredisposizioneParticelle";
    final String ID_ELENCO_DOCUMENTI = "elencoDocumenti";

    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    mapFilters.remove(ID_ELENCO_AZIENDE);
    mapFilters.remove(ID_ELENCO_PARTICELLE);
    mapFilters.remove(ID_ELENCO_PARTICELLE_ISTR);
    mapFilters.remove(ID_ELENCO_PPREDISP_PARTICELLE);
    mapFilters.remove(ID_ELENCO_DOCUMENTI);
    MapColonneNascosteVO hColumns = (MapColonneNascosteVO) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_COLONNE_NASCOSTE);
    hColumns.removeTable(ID_ELENCO_AZIENDE);
    hColumns.removeTable(ID_ELENCO_PARTICELLE);
    hColumns.removeTable(ID_ELENCO_DOCUMENTI);
    hColumns.removeTable(ID_ELENCO_PPREDISP_PARTICELLE);
    mapFilters.remove(ID_ELENCO_PARTICELLE_ISTR);
    HashMap<String, String> mapNumPagine = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_NUMERO_PAGINA);
    if (mapNumPagine != null)
    {
      mapNumPagine.remove(ID_ELENCO_AZIENDE);
      mapNumPagine.remove(ID_ELENCO_DOCUMENTI);

    }
    final String ID_ELENCO_OGGETTI = "elencoOggetti";
    HashMap<String, String> mapRigheVisibili = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_RIGHE_VISIBILI);
    if (mapRigheVisibili != null)
    {
      mapRigheVisibili.remove(ID_ELENCO_OGGETTI);
    }
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    if (utenteAbilitazioni.getRuolo().isUtenteTitolareCf())
    {
      if (utenteAbilitazioni.getEntiAbilitati() == null
          || utenteAbilitazioni.getEntiAbilitati().length <= 0)
      {
        throw new it.csi.iuffi.iuffiweb.exception.ApplicationException(
            "Funzione non abilitata. L'utente corrente non e' collegato a nessuna azienda nell'anagrafe delle aziende agricole.",
            0);
      }
    }
    if (utenteAbilitazioni.getRuolo().getIsList() != null
        && Arrays.asList(utenteAbilitazioni.getRuolo().getIsList())
            .contains("isUtenteProfessionista"))
    {
      throw new it.csi.iuffi.iuffiweb.exception.ApplicationException(
          "Funzione non abilitata per gli utenti professionisti.", 0);
    }

    readSessionValues(model, session);
    loadPopupCombo(model, session);
    model.addAttribute("isAvversitaAtmosferica", Boolean.FALSE);
    return "ricercaprocedimento/index";
  }

  @RequestMapping(value = "pulisciFiltri", method = RequestMethod.GET)
  public String pulisciFiltri(Model model, HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    if (session.getAttribute("RicercaProcedimentiVO") != null)
      session.removeAttribute("RicercaProcedimentiVO");
    if (session.getAttribute("checkboxMisureSelezionate") != null)
      session.removeAttribute("checkboxMisureSelezionate");
    if (session.getAttribute("all_gruppiProcedimento") != null)
      session.removeAttribute("all_gruppiProcedimento");
    if (session.getAttribute("selectGuppiRicerca") != null)
      session.removeAttribute("selectGuppiRicerca");
    return index(model, session);
  }

  /*
   * ===========================================================================
   * ==========
   */
  /* Metodo richiamato quando si effettua una ricerca per Codice Domanda/CUAA */
  /*
   * ===========================================================================
   * ==========
   */
  @RequestMapping(value = "ricercapuntuale", method = RequestMethod.POST)
  public String eseguiRicercaPuntuale(Model model, HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    Errors errors = new Errors();
    RicercaProcedimentiVO ricercaVO = new RicercaProcedimentiVO();
    String cuaa = request.getParameter("cuaa");
    String identificativo = request.getParameter("identificativo");

    salvaDatiPagina(request, model, session);

    if (!GenericValidator.isBlankOrNull(cuaa)
        && !GenericValidator.isBlankOrNull(identificativo))
    {
      model.addAttribute("msgErrore",
          "Codice Domanda o il CUAA devono essere valorizzati in alternativa");
      return index(model, session);
    }
    else
      if (!GenericValidator.isBlankOrNull(cuaa))
      {
        cuaa = cuaa.trim();
        errors.validateCuaa(cuaa, "cuaa");
        ricercaVO.setCuaa(cuaa.trim());
      }
      else
        if (!GenericValidator.isBlankOrNull(identificativo))
        {
          errors.validateMandatoryFieldLength(identificativo, 0, 20,
              "identificativo");
          ricercaVO.setIdentificativo(identificativo);
        }
        else
        {
          model.addAttribute("msgErrore",
              "Non hai inserito nessun parametro di ricerca");
          return index(model, session);
        }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return index(model, session);
    }
    else
    {
      return eseguiRicerca(ricercaVO, model, session);
    }
  }

  /*
   * ===========================================================================
   * ==========
   */
  /* Metodo richiamato quando si effettua una ricerca mediande i filtri */
  /*
   * ===========================================================================
   * ==========
   */
  @RequestMapping(value = "ricercaprocedimenti", method = RequestMethod.POST)
  public String eseguiRicercaConFiltri(Model model, HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    Errors errors = new Errors();
    salvaDatiPagina(request, model, session);

    Vector<String> idLivelli = deserialize(request, "serializedLivello");
    Vector<String> idEvento = deserialize(request, "serializedEvento");
    Vector<String> idBando = deserialize(request, "serializedBando");
    Vector<String> idAmministrazione = deserialize(request,
        "serializedAmministrazione");
    Vector<String> idStatoProc = deserialize(request, "serializedStato");
    Vector<String> idEstrazione = deserializeEstrazioni(request);
    Vector<String> idEstrazioneExPost = deserializeEstrazioniExPost(request);
    Vector<String> idNotifiche = deserialize(request, "serializedNotifica");

    if (!isRicercaDatiAnagrafici(request)
        && (idLivelli == null || idLivelli.size() <= 0))
    {
      errors.addError("misura", "Campo obbligatorio");
    }

    RicercaProcedimentiVO ricercaVO = new RicercaProcedimentiVO();
    ricercaVO.setVctIdLivelli(idLivelli);
    ricercaVO.setVctIdEventi(idEvento);
    ricercaVO.setVctIdBando(idBando);
    ricercaVO.setVctIdAmministrazione(idAmministrazione);
    ricercaVO.setVctIdStatoProcedimento(idStatoProc);
    ricercaVO.setVctFlagEstrazione(idEstrazione);
    ricercaVO.setVctFlagEstrazioneExPost(idEstrazioneExPost);
    ricercaVO.setMapOggetti(createMapOggetti(request, session));
    ricercaVO.setMapGruppi(createMapGruppi(request, session));

    ricercaVO.setTipoFiltroOggetto(request.getParameter("tipoFiltroOggetti"));
    ricercaVO.setIstanzaDataA(
        IuffiUtils.DATE.parseDate(request.getParameter("istanzaDataA")));
    ricercaVO.setIstanzaDataDa(
        IuffiUtils.DATE.parseDate(request.getParameter("istanzaDataDa")));
    ricercaVO.setVctNotifiche(idNotifiche);
    // Valido e aggiungo se valorizzati i filtri sui dati anangrafici
    validaDatiAnagrafici(errors, ricercaVO, request, model);

    if (!errors.isEmpty())
    {
      loadPopupCombo(model, session);
      model.addAttribute("errors", errors);
      session.removeAttribute("procedimento");
      UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
      model.addAttribute("isAvversitaAtmosferica", Boolean.FALSE);
      return "ricercaprocedimento/index";
    }

    selectOggettiPopup(session, model);

    return eseguiRicerca(ricercaVO, model, session);
  }

  private Vector<String> deserializeEstrazioni(HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Vector<String> elenco = new Vector<>();
    Vector<String> idEstrazione = deserialize(request, "serializedEstrazione");
    List<FlagEstrazioneDTO> elencoFlag = estrazioneEJB
        .getElencoFlagEstrazioni();
    if (idEstrazione != null && elencoFlag != null)
    {
      for (String ids : idEstrazione)
      {
        for (FlagEstrazioneDTO flag : elencoFlag)
        {
          if (flag.getIdEstrazione() == Long.parseLong(ids))
          {
            elenco.addElement(flag.getFlagEstratta());
          }
        }
      }
    }
    return elenco;
  }

  private Vector<String> deserializeEstrazioniExPost(HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Vector<String> elenco = new Vector<>();
    Vector<String> idEstrazione = deserialize(request,
        "serializedEstrazioneExPost");
    List<FlagEstrazioneDTO> elencoFlag = estrazioneEJB
        .getElencoFlagEstrazioniExPost();
    if (idEstrazione != null && elencoFlag != null)
    {
      for (String ids : idEstrazione)
      {
        for (FlagEstrazioneDTO flag : elencoFlag)
        {
          if (flag.getIdEstrazione() == Long.parseLong(ids))
          {
            elenco.addElement(flag.getFlagEstratta());
          }
        }
      }
    }
    return elenco;
  }

  @RequestMapping(value = "restoreElencoProcedimenti", method = RequestMethod.GET)
  public String restoreElencoProcedimenti(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    Map<String, String> mapParametri = ricercaEJB
        .getParametri(new String[]
        { IuffiConstants.PARAMETRO.ESTRAI_PAGAMENTI, });
    if (IuffiConstants.FLAGS.SI
        .equals(mapParametri.get(IuffiConstants.PARAMETRO.ESTRAI_PAGAMENTI))
        && IuffiUtils.PAPUASERV.isMacroCUAbilitato(
            getUtenteAbilitazioni(session),
            IuffiConstants.MACROCDU.ESTRAZIONE_PAGAMENTI))
    {
      model.addAttribute("showpagamenti", Boolean.TRUE);
    }
    loadPopupCombo(model, session);
    return "ricercaprocedimento/elencoProcedimenti";
  }

  /*
   * ===========================================================================
   * ==========
   */
  /* Metodo richiamato quando si ricaricano i filtri mediante chiamata Ajax */
  /*
   * ===========================================================================
   * ==========
   */
  @RequestMapping(value = "ricalcolalistbox_{listRef}", produces = "application/json", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> ricalcolaListBox(
      @PathVariable("listRef") String listRef, HttpSession session,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    HashMap<String, Object> values = new HashMap<String, Object>();
    String[] idLivelli = request.getParameterValues("misura");
    String[] idEventi = request.getParameterValues("evento");
    String[] idBandi = request.getParameterValues("bando");
    String[] idAmministrazioni = request.getParameterValues("amministrazione");
    String[] idStati = request.getParameterValues("stato");

    if (idLivelli == null)
      return null;

    long[] vIdLivelli = IuffiUtils.ARRAY.toLong(idLivelli);
    long[] vIdEventi = null;
    long[] vIdBandi = null;
    long[] vIdAmm = null;
    long[] vIdStati = null;

    if (idBandi != null)
      vIdBandi = IuffiUtils.ARRAY.toLong(idBandi);

    if (idEventi != null)
      vIdEventi = IuffiUtils.ARRAY.toLong(idEventi);

    values.put("inOggettiHidden", "");

    if (listRef.equals("misura"))
    {
      vIdEventi = refreshEventi(vIdLivelli, values, model, session);

      // aggiorno bandi e a cascata tutto il resto
      vIdBandi = refreshBandi(vIdLivelli, null, values, model, session);

      if (vIdBandi != null && vIdBandi.length > 0)
      {
        vIdAmm = refreshAmministrazioni(vIdBandi, values, utenteAbilitazioni,
            model, session);
        if (vIdAmm != null && vIdAmm.length > 0)
        {

          refreshStatiProcedimenti(vIdLivelli, vIdBandi, vIdAmm, values, model,
              session);
        }
      }
    }
    else
      if (listRef.equals("evento"))
      {
        // aggiorno bandi e a cascata tutto il resto
        vIdBandi = refreshBandi(vIdLivelli, vIdEventi, values, model, session);
        if (vIdBandi != null && vIdBandi.length > 0)
        {
          vIdAmm = refreshAmministrazioni(vIdBandi, values, utenteAbilitazioni,
              model, session);
          if (vIdAmm != null && vIdAmm.length > 0)
          {
            refreshStatiProcedimenti(vIdLivelli, vIdBandi, vIdAmm, values,
                model, session);
          }
        }
      }
      else
        if (listRef.equals("bando"))
        {
          // aggiorno amministrazioni e a cascata tutto il resto
          vIdAmm = refreshAmministrazioni(vIdBandi, values, utenteAbilitazioni,
              model, session);
          if (vIdAmm != null && vIdAmm.length > 0)
          {
            refreshStatiProcedimenti(vIdLivelli, vIdBandi, vIdAmm, values,
                model, session);
          }
        }
        else
          if (listRef.equals("amministrazione"))
          {
            vIdAmm = IuffiUtils.ARRAY.toLong(idAmministrazioni);
            if (vIdAmm != null && vIdAmm.length > 0)
            {
              refreshStatiProcedimenti(vIdLivelli, vIdBandi, vIdAmm, values,
                  model, session);
            }
          }
          else
            if (listRef.equals("stato"))
            {
              vIdStati = IuffiUtils.ARRAY.toLong(idStati);
              vIdAmm = IuffiUtils.ARRAY.toLong(idAmministrazioni);
              if (vIdStati != null && vIdStati.length > 0)
              {
                refreshOggettiProcedimenti(vIdLivelli, vIdBandi, vIdAmm,
                    vIdStati, values, model, session);
              }
            }
    return values;
  }

  @RequestMapping(value = "getElencoProcedimenti", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoProcedimenti(HttpSession session)
      throws InternalUnexpectedException
  {
    List<Map<String, Object>> procedimenti = new ArrayList<Map<String, Object>>();
    HashMap<String, Object> procedimento = null;

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    RicercaProcedimentiVO ricercaVO = (RicercaProcedimentiVO) session
        .getAttribute("RicercaProcedimentiVO");
    List<ProcedimentoOggettoVO> elenco = ricercaEJB
        .searchProcedimenti(ricercaVO, utenteAbilitazioni);

    if (elenco != null && elenco.size() > 0)
    {
      String portal = (String) session
          .getAttribute(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL);
      for (ProcedimentoOggettoVO item : elenco)
      {
        procedimento = new HashMap<String, Object>();
        procedimento.put("identificativo", item.getIdentificativo());
        procedimento.put("descrAmmCompetenza", item.getDescrAmmCompetenza());
        procedimento.put("annoCampagna", item.getAnnoCampagna());
        procedimento.put("denominazioneBando", item.getDenominazioneBando());
        procedimento.put("denominazioneIntestazione",
            item.getDenominazioneIntestazione());
        procedimento.put("descrAmmCompetenzaInfo",
            item.getDescrAmmCompetenzaInfo());
        procedimento.put("descrUfficioZona", item.getDescrUfficioZona());
        procedimento.put("descrizione", item.getDescrizione());
        procedimento.put("dataUltimoAggiornamento",
            item.getDataUltimoAggiornamento());
        procedimento.put("cuaa", item.getCuaa());
        procedimento.put("denominazioneAzienda",
            item.getDenominazioneAzienda());
        procedimento.put("indirizzoSedeLegale", item.getIndirizzoSedeLegale());
        procedimento.put("denominzioneDelega", item.getDenominzioneDelega());
        procedimento.put("descrComune", item.getDescrComune());
        procedimento.put("elencoLivelli", item.getElencoCodiciLivelliHtml());
        procedimento.put("descrProvincia", item.getDescrProvincia());
        procedimento.put("elencoCodiciLivelliMisure",
            item.getElencoCodiciLivelliMisure());
        procedimento.put("elencoCodiciLivelliSottoMisure",
            item.getElencoCodiciLivelliSottoMisure());
        procedimento.put("elencoCodiciOperazione",
            item.getElencoCodiciOperazione());
        procedimento.put("responsabileProcedimento",
            item.getResponsabileProcedimento());
        procedimento.put("tecnicoIstruttore", item.getTecnicoIstruttore());
        procedimento.put("descrUltimoGruppo", item.getDescrUltimoGruppo());
        procedimento.put("statoAmm", item.getStatoAmm());
        procedimento.put("dataStatoAmmStr", item.getDataStatoAmmStr());
        procedimento.put("dataUltimaIstanzaStr",
            item.getDataUltimaIstanzaStr());
        procedimento.put("descrUltimaIstanza", item.getDescrUltimaIstanza());

        if (item.getProcedimContributoConcesso()
            .compareTo(BigDecimal.ZERO) == 0)
        {
          item.setProcedimContributoConcesso(null);
        }
        if (item.getProcedimImportoInvestimento()
            .compareTo(BigDecimal.ZERO) == 0)
        {
          item.setProcedimImportoInvestimento(null);
        }
        if (item.getProcedimSpesaAmmessa().compareTo(BigDecimal.ZERO) == 0)
        {
          item.setProcedimSpesaAmmessa(null);
        }

        /*
         * procedimento.put("procedimImportoInvestimento",
         * IuffiUtils.FORMAT.formatGenericNumber(item.
         * getProcedimImportoInvestimento(),2,false));
         * procedimento.put("procedimSpesaAmmessa",
         * IuffiUtils.FORMAT.formatGenericNumber(item.getProcedimSpesaAmmessa
         * (),2,false)); procedimento.put("procedimContributoConcesso",
         * IuffiUtils.FORMAT.formatGenericNumber(item.
         * getProcedimContributoConcesso(),2,false));
         * procedimento.put("importoLiquidato",
         * IuffiUtils.FORMAT.formatGenericNumber(item.getImportoLiquidato(),2
         * ,false));
         */

        procedimento.put("procedimImportoInvestimento",
            item.getProcedimImportoInvestimento());
        procedimento.put("procedimSpesaAmmessa",
            item.getProcedimSpesaAmmessa());
        procedimento.put("procedimContributoConcesso",
            item.getProcedimContributoConcesso());
        procedimento.put("importoLiquidato", item.getImportoLiquidato());

        String azione = "<a href=\"" + item.getAzioneHref()
            + "\" style=\"text-decoration: none;\"><i class=\""
            + item.getAzione() + "\" title=\"" + item.getTitleHref()
            + "\"></i></a><span style=\"padding-left:1em;\"></span><input type=\"checkbox\" class=\"cBP\" name=\"cBP\" value=\""
            + item.getIdProcedimento() + "\">";

        procedimento.put("notificheHtml", item.getNotificheHtml());
        procedimento.put("notificheForFilter", item.getNotificheForFilter());

        azione += IuffiUtils.AGRIWELL.getHtmlIconaAgriwellWeb(
            utenteAbilitazioni, item.getIdAzienda(),
            item.getIdProcedimento(), portal, "ico24 ico_agriwell");

        procedimento.put("azione", azione);
        procedimenti.add(procedimento);
      }
    }

    return procedimenti;
  }

  @RequestMapping(value = "getElencoStatiProcedimenti", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoStatiProcedimenti(
      HttpSession session)
      throws InternalUnexpectedException
  {
    List<Map<String, Object>> procedimenti = new ArrayList<Map<String, Object>>();
    HashMap<String, Object> stato = null;
    Vector<String> vId = new Vector<String>();

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    RicercaProcedimentiVO ricercaVO = (RicercaProcedimentiVO) session
        .getAttribute("RicercaProcedimentiVO");

    List<ProcedimentoOggettoVO> elenco2 = ricercaEJB
        .searchProcedimenti(ricercaVO, utenteAbilitazioni);

    if (elenco2 != null && elenco2.size() > 0)
    {
      for (ProcedimentoOggettoVO item : elenco2)
      {
        if (item.getDescrizione() != null
            && !vId.contains(item.getDescrizione()))
        {
          vId.add(item.getDescrizione());
          stato = new HashMap<String, Object>();
          stato.put("label", item.getDescrizione());
          stato.put("id", item.getDescrizione());
          procedimenti.add(stato);
        }
      }
    }

    return procedimenti;
  }

  @RequestMapping(value = "elencoProcedimentiExcel")
  public ModelAndView downloadExcel(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    RicercaProcedimentiVO ricercaVO = (RicercaProcedimentiVO) session
        .getAttribute("RicercaProcedimentiVO");
    List<ProcedimentoOggettoVO> elenco = ricercaEJB
        .searchProcedimenti(ricercaVO, utenteAbilitazioni);
    return new ModelAndView("excelElencoProcedimentiView", "elenco", elenco);
  }

  @RequestMapping(value = "elencoPagamentiExcel")
  public ModelAndView downloadPagamentiExcel(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    RicercaProcedimentiVO ricercaVO = (RicercaProcedimentiVO) session
        .getAttribute("RicercaProcedimentiVO");
    List<Long> listIdProcedimenti = ricercaEJB.searchIdProcedimenti(ricercaVO,
        utenteAbilitazioni, null, null);

    ParametriQueryReportVO parametri = new ParametriQueryReportVO();
    parametri.setIstruzioneSQL(reportisticaEJB.getQueryParametroPagamenti());
    parametri.setlIdsProcedimenti(listIdProcedimenti);
    ReportVO reportPagamenti = reportisticaEJB.getReportBando(parametri);

    return new ModelAndView("excelElencoPagamentiView", "reportPagamenti",
        reportPagamenti);
  }

  @RequestMapping(value = "ajaxFiltraPopupEventi", produces = "application/json", method = RequestMethod.POST)
  public Map<String, String> ajaxFiltraPopupEventi(HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HashMap<String, String> values = new HashMap<String, String>();
    String[] anniEventi = request.getParameterValues("chk_anno_evento");
    if (anniEventi == null)
    {
      return values;
    }

    List<String> lAnniEventi = Arrays.asList(anniEventi);
    @SuppressWarnings("unchecked")
    List<EventiDTO> all_eventi = (List<EventiDTO>) session
        .getAttribute("all_eventi");

    for (EventiDTO evento : all_eventi)
    {
      if (lAnniEventi.contains(Integer.toString(evento.getAnnoEvento())))
      {
        values.put(String.valueOf(evento.getIdEventoCalamitoso()),
            evento.getDescrizione());
      }
      // Dalla request mi arriva 0 se cerco i bandi senza anno campagna
      // else
      // if (lAnniEventi.contains("0")
      // && GenericValidator.isBlankOrNull(evento.getAnnoEvento()))
      // {
      // values.put(String.valueOf(bando.getIdBando()),
      // bando.getDescrizione());
      // }

    }
    return values;
  }

  @RequestMapping(value = "ajaxFiltraPopupBandi", produces = "application/json", method = RequestMethod.POST)
  public Map<String, String> ajaxFiltraPopupBandi(HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HashMap<String, String> values = new HashMap<String, String>();
    String[] anniCampagna = request.getParameterValues("chk_anno_campagna");
    if (anniCampagna == null)
      return values;

    List<String> lAnniCampagna = Arrays.asList(anniCampagna);
    @SuppressWarnings("unchecked")
    List<BandoDTO> all_bandi = (List<BandoDTO>) session
        .getAttribute("all_bandi");

    if (all_bandi != null)
    {
      for (BandoDTO bando : all_bandi)
      {
        if (lAnniCampagna.contains(bando.getAnnoCampagna()))
        {
          values.put(String.valueOf(bando.getIdBando()),
              bando.getDescrizione());
        }
        // Dalla request mi arriva 0 se cerco i bandi senza anno campagna
        else
          if (lAnniCampagna.contains("0")
              && GenericValidator.isBlankOrNull(bando.getAnnoCampagna()))
          {
            values.put(String.valueOf(bando.getIdBando()),
                bando.getDescrizione());
          }

      }
    }

    return values;
  }

  @RequestMapping(value = "ajaxFiltraPopupAmministrazioni", produces = "application/json", method = RequestMethod.POST)
  public Map<String, String> ajaxFiltraPopupAmministrazioni(HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    TreeMap<String, String> values = new TreeMap<String, String>();
    List<String> lTipoAmministraz = null;
    String chkTutteAmministrazioni = request
        .getParameter("chkTutteAmministrazioni");
    String ischkTutteAmministrazioniClickEvnt = request
        .getParameter("ischkTutteAmministrazioniClickEvnt");
    String[] chkTipoAmministraz = request
        .getParameterValues("chkTipoAmministraz");

    @SuppressWarnings("unchecked")
    List<AmmCompetenzaDTO> amministrazioni = (List<AmmCompetenzaDTO>) session
        .getAttribute("all_amministrazioni");
    if (amministrazioni == null)
      return null;

    if (chkTipoAmministraz != null && chkTipoAmministraz.length > 0)
    {
      lTipoAmministraz = Arrays.asList(chkTipoAmministraz);
    }

    if (GenericValidator.isBlankOrNull(chkTutteAmministrazioni))
    {
      // Filtro solo amministrazioni abilitate per l'utente corrente
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      if (utenteAbilitazioni.getRuolo().isUtentePA())
        amministrazioni = filtraAmministrazioniAbilitate(utenteAbilitazioni,
            amministrazioni);
    }
    else
    {
      if (!GenericValidator.isBlankOrNull(ischkTutteAmministrazioniClickEvnt))
      {
        lTipoAmministraz = null;
        chkTipoAmministraz = null;
      }
    }

    // Per creare i check relativi a "Tipologia di amministrazione" mi faso
    // sull'elenco visibile nell'elenco delle amministrazionmi disponibili per i
    // bandi selezionati
    if (amministrazioni != null)
    {
      for (AmmCompetenzaDTO amministrazione : amministrazioni)
      {
        if (!values.containsKey(
            "DESCR&&" + amministrazione.getDescEstesaTipoAmministraz()))
        {
          if (lTipoAmministraz == null || (lTipoAmministraz != null
              && lTipoAmministraz
                  .contains(amministrazione.getDescEstesaTipoAmministraz())))
            values.put(
                "DESCR&&" + amministrazione.getDescEstesaTipoAmministraz(),
                "TRUE");
          else
            values.put(
                "DESCR&&" + amministrazione.getDescEstesaTipoAmministraz(),
                "FALSE");
        }
      }
    }

    if (chkTipoAmministraz != null && chkTipoAmministraz.length > 0)
    {
      List<AmmCompetenzaDTO> ammFiltrate = new Vector<AmmCompetenzaDTO>();
      for (AmmCompetenzaDTO amm : amministrazioni)
      {
        if (lTipoAmministraz.contains(amm.getDescEstesaTipoAmministraz()))
        {
          ammFiltrate.add(amm);
        }
      }
      amministrazioni = ammFiltrate;
    }

    for (AmmCompetenzaDTO amministrazione : amministrazioni)
    {
      values.put(String.valueOf(amministrazione.getIdAmmCompetenza()),
          amministrazione.getDescrizione());
    }

    return values;
  }

  @RequestMapping(value = "searchComuni", produces = "application/json", method = RequestMethod.POST)
  public Map<String, Object> searchComuni(HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HashMap<String, Object> values = new HashMap<String, Object>();
    String prov = request.getParameter("provSceltaComune");
    String comune = request.getParameter("comuneSceltaComune");
    List<ComuneDTO> comuni = ricercaEJB.searchComuni(prov, comune);
    if (comuni != null && comuni.size() == 1)
    {
      values.put("oneResult", "true");
      values.put("prov", comuni.get(0).getSiglaProvincia());
      values.put("comune", comuni.get(0).getDescrizioneComune());
    }
    else
    {
      values.put("comuniDTO", comuni);
    }
    values.put(IuffiConstants.PAGINATION.IS_VALID, "true"); // Serve al
    // javascript per
    // capire che è
    // non ci sono
    // stati errori
    // nella
    // creazione del
    // file json
    return values;
  }

  @RequestMapping(value = "reloadOggettoModal")
  public String reloadOggettoModal(HttpSession session)
      throws InternalUnexpectedException
  {
    return "ricercaprocedimento/reloadOggettoModal";
  }

  @RequestMapping(value = "reloadAnnoCampagna")
  public String reloadAnnoCampagna(HttpSession session)
      throws InternalUnexpectedException
  {
    return "ricercaprocedimento/reloadAnnoCampagna";
  }

  @RequestMapping(value = "reloadAnnoEvento")
  public String reloadAnnoEvento(HttpSession session)
      throws InternalUnexpectedException
  {
    return "ricercaprocedimento/reloadAnnoEvento";
  }

  /*
   * ===========================================================================
   * ==========
   */
  /* Metodi Privati della controller */
  /*
   * ===========================================================================
   * ==========
   */
  @SuppressWarnings("unchecked")
  private void salvaDatiPagina(HttpServletRequest request, Model model,
      HttpSession session)
      throws InternalUnexpectedException
  {
    Vector<String> idLivelli = deserialize(request, "serializedLivello");
    Vector<String> idEvento = deserialize(request, "serializedEvento");
    Vector<String> idBando = deserialize(request, "serializedBando");
    Vector<String> idAmministrazione = deserialize(request,
        "serializedAmministrazione");
    Vector<String> idStatoProc = deserialize(request, "serializedStato");
    Vector<String> idNotifiche = deserialize(request, "serializedNotifica");

    Vector<String> flagEstrazioni = deserializeEstrazioni(request);
    Vector<String> flagEstrazioniExPost = deserializeEstrazioniExPost(request);

    // Reperisco le liste complete che devo poi filtrare per rimettere nel model
    // le combo INI
    List<EventiDTO> all_eventi = (List<EventiDTO>) session
        .getAttribute("all_eventi");
    List<BandoDTO> all_bandi = (List<BandoDTO>) session
        .getAttribute("all_bandi");
    List<LivelloDTO> all_livelli = ricercaEJB
        .getLivelliAttivi(getUtenteAbilitazioni(session).getIdProcedimento());

    List<AmmCompetenzaDTO> all_amministrazioni = (List<AmmCompetenzaDTO>) session
        .getAttribute("all_amministrazioni");
    List<ProcedimentoDTO> all_statiProcedimento = (List<ProcedimentoDTO>) session
        .getAttribute("all_statiProcedimento");

    List<EventiDTO> eventi = new ArrayList<EventiDTO>();
    List<BandoDTO> bandi = new ArrayList<BandoDTO>();
    List<LivelloDTO> livelli = new ArrayList<LivelloDTO>();
    List<AmmCompetenzaDTO> amministrazioni = new ArrayList<AmmCompetenzaDTO>();
    List<ProcedimentoDTO> statiProcediemnto = new ArrayList<ProcedimentoDTO>();

    if (all_eventi != null && idEvento != null)
      for (EventiDTO e : all_eventi)
      {
        if (idEvento.contains(String.valueOf(e.getIdEventoCalamitoso())))
          eventi.add(e);
      }

    if (all_bandi != null && idBando != null)
      for (BandoDTO a : all_bandi)
      {
        if (idBando.contains(String.valueOf(a.getIdBando())))
          bandi.add(a);
      }

    if (all_livelli != null && idLivelli != null)
      for (LivelloDTO a : all_livelli)
      {
        if (idLivelli.contains(String.valueOf(a.getIdLivello())))
          livelli.add(a);
      }

    if (all_amministrazioni != null && idAmministrazione != null)
      for (AmmCompetenzaDTO a : all_amministrazioni)
      {
        if (idAmministrazione.contains(String.valueOf(a.getIdAmmCompetenza())))
          amministrazioni.add(a);
      }

    if (all_statiProcedimento != null && idStatoProc != null)
      for (ProcedimentoDTO a : all_statiProcedimento)
      {
        if (idStatoProc.contains(String.valueOf(a.getIdStatoOggetto())))
          statiProcediemnto.add(a);
      }

    if (all_statiProcedimento != null && idStatoProc != null)
      for (ProcedimentoDTO a : all_statiProcedimento)
      {
        if (idStatoProc.contains(String.valueOf(a.getIdStatoOggetto())))
          statiProcediemnto.add(a);
      }

    List<FlagEstrazioneDTO> flagFiltrati = new ArrayList<FlagEstrazioneDTO>();
    List<FlagEstrazioneDTO> all_flagFiltrati = estrazioneEJB
        .getElencoFlagEstrazioni();
    if (all_flagFiltrati != null && flagEstrazioni != null)
      for (FlagEstrazioneDTO a : all_flagFiltrati)
      {
        if (flagEstrazioni.contains(String.valueOf(a.getFlagEstratta())))
          flagFiltrati.add(a);
      }

    List<FlagEstrazioneDTO> flagFiltratiExPost = new ArrayList<FlagEstrazioneDTO>();
    List<FlagEstrazioneDTO> all_flagFiltratiExPost = estrazioneEJB
        .getElencoFlagEstrazioniExPost();
    if (all_flagFiltratiExPost != null && flagFiltratiExPost != null)
      for (FlagEstrazioneDTO a : all_flagFiltratiExPost)
      {
        if (flagEstrazioniExPost.contains(String.valueOf(a.getFlagEstratta())))
          flagFiltratiExPost.add(a);
      }

    List<GravitaNotificaVO> all_notifica = ricercaEJB
        .getElencoGravitaNotifica();
    GravitaNotificaVO g = new GravitaNotificaVO();
    g.setGravita("Nessuna notifica");
    g.setId(99);
    all_notifica.add(g);
    List<GravitaNotificaVO> notificheProcedimento = new ArrayList<>();
    if (all_notifica != null && idNotifiche != null)
      for (GravitaNotificaVO a : all_notifica)
      {
        if (idNotifiche.contains(String.valueOf(a.getId())))
          notificheProcedimento.add(a);
      }

    model.addAttribute("eventi", eventi);
    model.addAttribute("bandi", bandi);
    model.addAttribute("livelli", livelli);
    model.addAttribute("flagFiltrati", flagFiltrati);
    model.addAttribute("flagFiltratiExPost", flagFiltratiExPost);
    model.addAttribute("amministrazioni", amministrazioni);
    model.addAttribute("statiProcediemnto", statiProcediemnto);
    model.addAttribute("notificheProcedimento", notificheProcedimento);

    // FINE

    String cuaa = request.getParameter("cuaa");
    String identificativo = request.getParameter("identificativo");
    String cuaaProcedimenti = request.getParameter("cuaaProcedimenti");
    String pivaProcedimenti = request.getParameter("pivaProcedimenti");
    String istanzaDataDa = request.getParameter("istanzaDataDa");
    String istanzaDataA = request.getParameter("istanzaDataA");
    String denominazioneProcedimenti = request
        .getParameter("denominazioneProcedimenti");
    String provProcedimenti = request.getParameter("provSceltaComune");
    String comuneProcedimenti = request.getParameter("comuneSceltaComune");
    String tipoFiltroOggetti = request.getParameter("tipoFiltroOggetti");
    String tutteAmministrazioni = request
        .getParameter("chkTutteAmministrazioni");

    model.addAttribute("cuaa", cuaa);
    model.addAttribute("identificativo", identificativo);
    model.addAttribute("cuaaProcedimenti", cuaaProcedimenti);
    model.addAttribute("denominazioneProcedimenti", denominazioneProcedimenti);
    model.addAttribute("pivaProcedimenti", pivaProcedimenti);
    model.addAttribute("provSceltaComune", provProcedimenti);
    model.addAttribute("comuneSceltaComune", comuneProcedimenti);
    model.addAttribute("tipoFiltroOggetti", tipoFiltroOggetti);

    // Salvo VO in sessione
    RicercaProcedimentiVO ricercaProcedimentiVO = new RicercaProcedimentiVO();
    ricercaProcedimentiVO.setCuaa(cuaa);
    ricercaProcedimentiVO.setIdentificativo(identificativo);
    ricercaProcedimentiVO.setEventi(eventi);
    ricercaProcedimentiVO.setBandi(bandi);
    ricercaProcedimentiVO.setLivelli(livelli);
    ricercaProcedimentiVO.setAmministrazioni(amministrazioni);
    ricercaProcedimentiVO.setFlagShowAllAmministrazioni(
        !GenericValidator.isBlankOrNull(tutteAmministrazioni));
    ricercaProcedimentiVO.setStatiProcedimento(statiProcediemnto);
    ricercaProcedimentiVO.setCuaaProcedimenti(cuaaProcedimenti);
    ricercaProcedimentiVO.setPiva(pivaProcedimenti);
    ricercaProcedimentiVO.setDenominazione(denominazioneProcedimenti);
    ricercaProcedimentiVO.setProvSedeLegale(provProcedimenti);
    ricercaProcedimentiVO.setComuneSedeLegale(comuneProcedimenti);
    ricercaProcedimentiVO
        .setIstanzaDataA(IuffiUtils.DATE.parseDate(istanzaDataA));
    ricercaProcedimentiVO
        .setIstanzaDataDa(IuffiUtils.DATE.parseDate(istanzaDataDa));
    ricercaProcedimentiVO.setVctNotifiche(idNotifiche);
    ricercaProcedimentiVO.setVctIdLivelli(idLivelli);
    ricercaProcedimentiVO.setVctFlagEstrazione(flagEstrazioni);
    ricercaProcedimentiVO.setVctFlagEstrazioneExPost(flagEstrazioniExPost);
    ricercaProcedimentiVO.setVctIdEventi(idEvento);
    ricercaProcedimentiVO.setVctIdBando(idBando);
    ricercaProcedimentiVO.setVctIdAmministrazione(idAmministrazione);
    ricercaProcedimentiVO.setVctIdStatoProcedimento(idStatoProc);
    ricercaProcedimentiVO.setMapOggetti(createMapOggetti(request, session));
    ricercaProcedimentiVO.setMapGruppi(createMapGruppi(request, session));
    ricercaProcedimentiVO.setTipoFiltroOggetto(tipoFiltroOggetti);

    session.setAttribute("RicercaProcedimentiVO", ricercaProcedimentiVO);
  }

  private boolean isRicercaDatiAnagrafici(HttpServletRequest request)
  {
    String cuaa = request.getParameter("cuaaProcedimenti");
    String piva = request.getParameter("pivaProcedimenti");
    String denominazione = request.getParameter("denominazioneProcedimenti");
    String provSedeLegale = request.getParameter("provSceltaComune");
    String comuneSedeLegale = request.getParameter("comuneSceltaComune");

    if (!GenericValidator.isBlankOrNull(cuaa)
        || !GenericValidator.isBlankOrNull(piva)
        || !GenericValidator.isBlankOrNull(denominazione)
        || !GenericValidator.isBlankOrNull(provSedeLegale)
        || !GenericValidator.isBlankOrNull(comuneSedeLegale))
    {
      return true;
    }
    else
    {
      return false;
    }

  }

  private void validaDatiAnagrafici(Errors errors,
      RicercaProcedimentiVO ricercaVO, HttpServletRequest request,
      Model model)
  {
    String cuaa = request.getParameter("cuaaProcedimenti");
    String piva = request.getParameter("pivaProcedimenti");
    String denominazione = request.getParameter("denominazioneProcedimenti");
    String provSedeLegale = request.getParameter("provSceltaComune");
    String comuneSedeLegale = request.getParameter("comuneSceltaComune");

    ricercaVO.setCuaaProcedimenti(IuffiUtils.FORMAT.trim(cuaa));
    ricercaVO.setPiva(IuffiUtils.FORMAT.trim(piva));
    ricercaVO.setDenominazione(IuffiUtils.FORMAT.trim(denominazione));
    ricercaVO.setProvSedeLegale(IuffiUtils.FORMAT.trim(provSedeLegale));
    ricercaVO.setComuneSedeLegale(IuffiUtils.FORMAT.trim(comuneSedeLegale));

    // Se viene valorizzato il CUAA nessuno degli altri filtri sui dati
    // anagrafici può essere valorizzato e viceversa.
    if (!GenericValidator.isBlankOrNull(cuaa))
    {
      errors.validateCuaa(cuaa, "cuaaProcedimenti");

      if (!GenericValidator.isBlankOrNull(piva)
          || !GenericValidator.isBlankOrNull(denominazione)
          || !GenericValidator.isBlankOrNull(provSedeLegale)
          || !GenericValidator.isBlankOrNull(comuneSedeLegale))
      {
        errors.addError("cuaaProcedimenti",
            "Se viene valorizzato il CUAA nessuno degli altri filtri sui dati anagrafici può essere valorizzato e viceversa.");
        return;
      }
    }
    else
      if (!GenericValidator.isBlankOrNull(piva)
          || !GenericValidator.isBlankOrNull(denominazione)
          || !GenericValidator.isBlankOrNull(provSedeLegale)
          || !GenericValidator.isBlankOrNull(comuneSedeLegale))
      {
        if (!GenericValidator.isBlankOrNull(cuaa))
        {
          errors.addError("cuaaProcedimenti",
              "Se viene valorizzato il CUAA nessuno degli altri filtri sui dati anagrafici può essere valorizzato e viceversa.");
          return;
        }
      }

    // Se viene valorizzata la PIVA nessuno degli altri filtri sui dati
    // anagrafici può essere valorizzato e viceversa.
    if (!GenericValidator.isBlankOrNull(piva))
    {
      if (!GenericValidator.isBlankOrNull(cuaa)
          || !GenericValidator.isBlankOrNull(denominazione)
          || !GenericValidator.isBlankOrNull(provSedeLegale)
          || !GenericValidator.isBlankOrNull(comuneSedeLegale))
      {
        errors.addError("pivaProcedimenti",
            "Se viene valorizzata la Partita Iva nessuno degli altri filtri sui dati anagrafici può essere valorizzato e viceversa.");
        return;
      }
    }
    else
      if (!GenericValidator.isBlankOrNull(cuaa)
          || !GenericValidator.isBlankOrNull(denominazione)
          || !GenericValidator.isBlankOrNull(provSedeLegale)
          || !GenericValidator.isBlankOrNull(comuneSedeLegale))
      {
        if (!GenericValidator.isBlankOrNull(piva))
        {
          errors.addError("pivaProcedimenti",
              "Se viene valorizzata la Partita Iva nessuno degli altri filtri sui dati anagrafici può essere valorizzato e viceversa.");
          return;
        }
      }

    if (!GenericValidator.isBlankOrNull(denominazione))
    {
      errors.validateFieldMaxLength(denominazione, "denominazioneProcedimenti",
          1000);
    }
  }

  private String eseguiRicerca(RicercaProcedimentiVO ricercaVO, Model model,
      HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    session.removeAttribute(IuffiConstants.GENERIC.SESSION_NO_ELENCO_AZIENDA);
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    List<Long> lId = ricercaEJB.searchIdProcedimenti(ricercaVO,
        utenteAbilitazioni, null, null);
    session.removeAttribute(SESSION_NAME_ELENCO_PROCEDIMENTI);

    if (lId == null || lId.size() <= 0)
    {
      model.addAttribute("msgErrore",
          "Non è stato trovato nessun procedimento per i parametri impostati.");
      if (utenteAbilitazioni.getRuolo().getIsList() != null
          && Arrays.asList(utenteAbilitazioni.getRuolo().getIsList())
              .contains("isUtenteProfessionista"))
        return "ricercaprocedimento/elencoProcedimenti";
      else
        return index(model, session);
    }
    else
      if (lId.size() > 1)
      {
        model.addAttribute("refListID", lId);

        Map<String, String> mapParametri = ricercaEJB
            .getParametri(new String[]
            { IuffiConstants.PARAMETRO.ESTRAI_PAGAMENTI, });
        if (IuffiConstants.FLAGS.SI
            .equals(mapParametri.get(IuffiConstants.PARAMETRO.ESTRAI_PAGAMENTI))
            && IuffiUtils.PAPUASERV.isMacroCUAbilitato(
                getUtenteAbilitazioni(session),
                "SUPER_USER" /*
                              * IuffiConstants.MACROCDU. ESTRAZIONE_PAGAMENTI
                              */))
        {
          model.addAttribute("showpagamenti", Boolean.TRUE);
        }
        loadPopupCombo(model, session);
        return "ricercaprocedimento/elencoProcedimenti";
      }
      else
      {
        session.setAttribute(IuffiConstants.GENERIC.SESSION_NO_ELENCO_AZIENDA,
            Boolean.TRUE);
        model.addAttribute("refListID", lId);
        return "redirect:../cuiuffi129/index_" + lId.get(0) + ".do";
      }
  }

  private Vector<String> deserialize(HttpServletRequest request, String reqName)
  {
    Vector<String> ids = null;
    String param = request.getParameter(reqName);
    // Ottengo una serie di elName=val&elName=val
    if (param != null)
    {
      for (String singleParam : param.split("&"))
      {
        if (singleParam != null && singleParam.trim().length() > 0)
        {
          if (ids == null)
          {
            ids = new Vector<String>();
          }
          ids.add(singleParam.split("=")[1]);
        }
      }
    }
    return ids;
  }

  private long[] refreshBandi(long[] idLivelli, long[] idEventi,
      HashMap<String, Object> values, Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    Vector<String> vIdBandi = null;
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    List<BandoDTO> bandi = ricercaEJB.getBandiAttivi(idLivelli, idEventi,
        utenteAbilitazioni.getIdProcedimento());
    session.setAttribute("all_bandi", bandi);
    model.addAttribute("all_bandi", bandi);
    if (bandi != null)
    {
      vIdBandi = new Vector<String>();
      StringBuffer sbOptBandi = new StringBuffer();
      for (BandoDTO bandoDTO : bandi)
      {
        sbOptBandi
            .append("<option  value='" + bandoDTO.getIdBando() + "' title=\""
                + bandoDTO.getDescrizione().replace("\"", "&quot;") + "\" >"
                + bandoDTO.getDescrizione()
                + "</option>");
        vIdBandi.add(String.valueOf(bandoDTO.getIdBando()));
      }
      values.put("optBandi", sbOptBandi.toString());
    }
    return IuffiUtils.ARRAY.toLong(vIdBandi);
  }

  private long[] refreshEventi(long[] idLivelli, HashMap<String, Object> values,
      Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    Vector<String> vIdEventi = null;
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    List<EventiDTO> eventi = ricercaEJB.getEventiAttivi(idLivelli,
        utenteAbilitazioni.getIdProcedimento());
    session.setAttribute("all_eventi", eventi);
    model.addAttribute("all_eventi", eventi);
    if (eventi != null)
    {
      vIdEventi = new Vector<String>();
      StringBuffer sbOptEventi = new StringBuffer();
      for (EventiDTO eventoDTO : eventi)
      {
        sbOptEventi.append("<option  value='"
            + eventoDTO.getIdEventoCalamitoso() + "' title=\""
            + eventoDTO.getDescrizione().replace("\"", "&quot;") + "\" >"
            + eventoDTO.getDescrizione()
            + "</option>");
        vIdEventi.add(String.valueOf(eventoDTO.getIdEventoCalamitoso()));
      }
      values.put("optEventi", sbOptEventi.toString());
    }
    return IuffiUtils.ARRAY.toLong(vIdEventi);
  }

  private long[] refreshAmministrazioni(long[] vIdBandi,
      HashMap<String, Object> values,
      UtenteAbilitazioni utenteAbilitazioni, Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    Vector<String> vIdAmm = null;
    List<AmmCompetenzaDTO> amministrazioni = ricercaEJB
        .getAmministrazioniAttive(vIdBandi);
    session.setAttribute("all_amministrazioni", amministrazioni);
    model.addAttribute("all_amministrazioni", amministrazioni);

    if (utenteAbilitazioni.getRuolo().isUtentePA())
      amministrazioni = filtraAmministrazioniAbilitate(utenteAbilitazioni,
          amministrazioni);

    if (amministrazioni != null)
    {
      vIdAmm = new Vector<String>();
      StringBuffer sbOptAmm = new StringBuffer();
      for (AmmCompetenzaDTO ammDTO : amministrazioni)
      {
        sbOptAmm.append(
            "<option value='" + ammDTO.getIdAmmCompetenza() + "'>"
                + ammDTO.getDescrizione() + "</option>");
        vIdAmm.add(String.valueOf(ammDTO.getIdAmmCompetenza()));
      }
      values.put("optAmministrazioni", sbOptAmm.toString());
    }
    return IuffiUtils.ARRAY.toLong(vIdAmm);
  }

  private Vector<String> refreshStatiProcedimenti(long[] vIdLivelli,
      long[] vIdBandi, long[] vIdAmm,
      HashMap<String, Object> values, Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    Vector<String> vIdStati = null;
    List<ProcedimentoDTO> statiProcediemnto = ricercaEJB
        .getStatiProcedimentiAttivi(vIdLivelli, vIdBandi, vIdAmm);
    session.setAttribute("all_statiProcedimento", statiProcediemnto);
    model.addAttribute("all_statiProcedimento", statiProcediemnto);

    if (statiProcediemnto != null)
    {
      vIdStati = new Vector<String>();
      StringBuffer sbOptProc = new StringBuffer();
      for (ProcedimentoDTO procDTO : statiProcediemnto)
      {
        sbOptProc.append("<option value='" + procDTO.getIdStatoOggetto() + "'>"
            + procDTO.getDescrStatoOggetto()
            + "</option>");
        vIdStati.add(String.valueOf(procDTO.getIdStatoOggetto()));
      }
      values.put("optProcedimenti", sbOptProc.toString());
    }

    refreshOggettiProcedimenti(vIdLivelli, vIdBandi, vIdAmm,
        IuffiUtils.ARRAY.toLong(vIdStati), values, model,
        session);

    return vIdStati;
  }

  private Vector<String> refreshOggettiProcedimenti(long[] vIdLivelli,
      long[] vIdBandi, long[] vIdAmm,
      long[] vIdStati, HashMap<String, Object> values, Model model,
      HttpSession session)
      throws InternalUnexpectedException
  {
    Vector<String> vIdOggetti = null, vIdGruppi = null;
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");

    List<GruppoOggettoDTO> gruppiProcedimento = ricercaEJB
        .getOggettiProcedimentiAttivi(vIdLivelli, vIdBandi,
            vIdAmm, vIdStati, utenteAbilitazioni);

    session.setAttribute("all_gruppiProcedimento", gruppiProcedimento);
    model.addAttribute("all_gruppiProcedimento", gruppiProcedimento);

    if (gruppiProcedimento != null)
    {
      vIdGruppi = new Vector<String>();
      StringBuffer sbOptProcHidden = new StringBuffer();
      StringBuffer sbOptProcGruppi = new StringBuffer();
      StringBuffer sbOptProcHiddenGruppi = new StringBuffer();
      vIdOggetti = new Vector<String>();

      for (GruppoOggettoDTO gruppoDTO : gruppiProcedimento)
      {
        // se non ho stati vuol dire che il gruppo è a premio, quindi non
        // comparirà nella select
        // quando verranno bonificate anche le misure a premio per la gestione
        // degli stati compariranno anche loro TOP
        if (gruppoDTO.getStati() != null && !gruppoDTO.getStati().isEmpty()
            && gruppoDTO.getStati().size() > 1) // maggiore
        // di
        // 1
        // perché
        // c'è
        // il
        // "Non
        // presente"
        {
          sbOptProcGruppi.append("<option value='"
              + gruppoDTO.getIdGruppoOggetto() + "'>"
              + gruppoDTO.getElencoDescrStatiSenzaNonPresente() + "</option>");
          for (EsitoOggettoDTO stato : gruppoDTO.getStati())
          {
            if (stato.getIdEsito() != 0)// Non presente di default non cè
            {
              sbOptProcHiddenGruppi.append(
                  "<input type=\"hidden\" name=\"hGruppi\" data-oggetto=\""
                      + gruppoDTO.getIdGruppoOggetto() + "\"  value=\""
                      + gruppoDTO.getIdGruppoOggetto()
                      + "&&" + stato.getIdEsito() + "\" />");
            }
          }

          vIdGruppi.add(String.valueOf(gruppoDTO.getIdGruppoOggetto()));

        }
        else
        {
          // se il gruppo non ha stati (o ne ha solo 1 ovvero il non presente)
          // allora metto solo gli oggetti
          if (gruppoDTO != null && gruppoDTO.getOggetti() != null)
            for (OggettoDTO oggDTO : gruppoDTO.getOggetti())
            {

              sbOptProcGruppi.append(
                  "<option value='" + oggDTO.getIdLegameGruppoOggetto() + "'>"
                      + oggDTO.getDescrizioneElencoEsitiSenzaNonPresente()
                      + "</option>");

              for (EsitoOggettoDTO esito : oggDTO.getEsitiOggetto())
              {
                if (esito.getIdEsito() != 0)// Non presente di default non cè
                {
                  sbOptProcHidden.append(
                      "<input type=\"hidden\" name=\"hOggetti\" data-oggetto=\""
                          + oggDTO.getIdLegameGruppoOggetto() + "\"  value=\""
                          + oggDTO.getIdLegameGruppoOggetto() + "&&"
                          + esito.getIdEsito() + "\" />");
                }
              }

              if (!gruppoDTO.isSelected())
                vIdOggetti
                    .add(String.valueOf(oggDTO.getIdLegameGruppoOggetto()));

            }
        }

        values.put("inOggettiHidden", sbOptProcHidden.toString());
      }
      values.put("optGruppi", sbOptProcGruppi.toString());
      values.put("inGruppiHidden", sbOptProcHiddenGruppi.toString());
    }

    selectOggettiPopup(session, model);
    return vIdOggetti;
  }

  private void readSessionValues(Model model, HttpSession session)
  {
    RicercaProcedimentiVO ricercaProcedimentiVO = (RicercaProcedimentiVO) session
        .getAttribute("RicercaProcedimentiVO");

    if (ricercaProcedimentiVO != null)
    {
      if (!GenericValidator.isBlankOrNull(ricercaProcedimentiVO.getCuaa()))
        model.addAttribute("cuaa", ricercaProcedimentiVO.getCuaa());
      if (!GenericValidator
          .isBlankOrNull(ricercaProcedimentiVO.getIdentificativo()))
        model.addAttribute("identificativo",
            ricercaProcedimentiVO.getIdentificativo());
      if (!GenericValidator
          .isBlankOrNull(ricercaProcedimentiVO.getCuaaProcedimenti()))
        model.addAttribute("cuaaProcedimenti",
            ricercaProcedimentiVO.getCuaaProcedimenti());
      if (!GenericValidator
          .isBlankOrNull(ricercaProcedimentiVO.getDenominazione()))
        model.addAttribute("denominazioneProcedimenti",
            ricercaProcedimentiVO.getDenominazione());
      if (!GenericValidator.isBlankOrNull(ricercaProcedimentiVO.getPiva()))
        model.addAttribute("pivaProcedimenti", ricercaProcedimentiVO.getPiva());
      if (!GenericValidator
          .isBlankOrNull(ricercaProcedimentiVO.getProvSedeLegale()))
        model.addAttribute("provSceltaComune",
            ricercaProcedimentiVO.getProvSedeLegale());
      if (!GenericValidator
          .isBlankOrNull(ricercaProcedimentiVO.getComuneSedeLegale()))
        model.addAttribute("comuneSceltaComune",
            ricercaProcedimentiVO.getComuneSedeLegale());
      if (ricercaProcedimentiVO.getIstanzaDataA() != null)
        model.addAttribute("istanzaDataA", IuffiUtils.DATE
            .formatDate(ricercaProcedimentiVO.getIstanzaDataA()));
      if (ricercaProcedimentiVO.getIstanzaDataDa() != null)
        model.addAttribute("istanzaDataDa",
            IuffiUtils.DATE
                .formatDate(ricercaProcedimentiVO.getIstanzaDataDa()));
    }
  }

  private void loadPopupCombo(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    int idProcedimentoAgricolo = utenteAbilitazioni.getIdProcedimento();

    List<LivelloDTO> livelli = ricercaEJB
        .getLivelliAttivi(idProcedimentoAgricolo);
    List<FlagEstrazioneDTO> flagEstrazione = estrazioneEJB
        .getElencoFlagEstrazioni();
    List<FlagEstrazioneDTO> flagEstrazioneExPost = estrazioneEJB
        .getElencoFlagEstrazioniExPost();
    List<BandoDTO> bandi = ricercaEJB.getBandiAttivi(null, null,
        utenteAbilitazioni.getIdProcedimento());
    List<AmmCompetenzaDTO> amministrazioni = ricercaEJB
        .getAmministrazioniAttive(null);
    List<ProcedimentoDTO> statiProcedimento = ricercaEJB
        .getStatiProcedimentiAttivi(null, null, null);
    List<EventiDTO> eventi = ricercaEJB.getEventiAttivi(null,
        utenteAbilitazioni.getIdProcedimento());

    model.addAttribute("all_eventi", eventi);
    model.addAttribute("all_livelli", livelli);
    model.addAttribute("all_flagEstrazione", flagEstrazione);
    model.addAttribute("all_flagEstrazioneExPost", flagEstrazioneExPost);
    List<LivelloDTO> all_misure = ricercaEJB
        .getMisureConFlagTipo(idProcedimentoAgricolo);
    List<DecodificaDTO<Long>> tipiMisure = ricercaEJB
        .getTipiMisure(idProcedimentoAgricolo);
    model.addAttribute("all_misure", all_misure);
    model.addAttribute("tipoFiltroOggetti", "OR");
    model.addAttribute("tipiMisure", tipiMisure);

    List<GravitaNotificaVO> all_notifica = ricercaEJB
        .getElencoGravitaNotifica();
    GravitaNotificaVO g = new GravitaNotificaVO();
    g.setGravita("Nessuna notifica");
    g.setId(99);
    all_notifica.add(g);
    model.addAttribute("all_notifica", all_notifica);

    RicercaProcedimentiVO procedimentiVO = (RicercaProcedimentiVO) session
        .getAttribute("RicercaProcedimentiVO");
    if (procedimentiVO != null)
    {
      if (procedimentiVO.getVctIdLivelli() != null
          && procedimentiVO.getVctIdLivelli().size() > 0)
      {
        List<LivelloDTO> livelliFiltrati = new ArrayList<LivelloDTO>();
        for (LivelloDTO livello : livelli)
        {
          if (procedimentiVO.getVctIdLivelli()
              .contains(String.valueOf(livello.getIdLivello())))
          {
            livelliFiltrati.add(livello);
          }
        }
        model.addAttribute("livelli", livelliFiltrati);
      }

      // eventi
      if (procedimentiVO.getVctIdEventi() != null
          && procedimentiVO.getVctIdEventi().size() > 0)
      {
        List<EventiDTO> eventiFiltrati = new ArrayList<EventiDTO>();
        for (EventiDTO evento : eventi)
        {
          if (procedimentiVO.getVctIdEventi()
              .contains(String.valueOf(evento.getIdEventoCalamitoso())))
          {
            eventiFiltrati.add(evento);
          }
        }
        model.addAttribute("eventi", eventiFiltrati);
      }

      if (procedimentiVO.getVctIdBando() != null
          && procedimentiVO.getVctIdBando().size() > 0)
      {
        List<BandoDTO> bandiFiltrati = new ArrayList<BandoDTO>();
        if (bandi != null)
        {
          for (BandoDTO bando : bandi)
          {
            if (procedimentiVO.getVctIdBando()
                .contains(String.valueOf(bando.getIdBando())))
            {
              bandiFiltrati.add(bando);
            }
          }
        }

        model.addAttribute("bandi", bandiFiltrati);
      }

      if (procedimentiVO.getVctIdAmministrazione() != null
          && procedimentiVO.getVctIdAmministrazione().size() > 0)
      {
        List<AmmCompetenzaDTO> ammFiltrati = new ArrayList<AmmCompetenzaDTO>();
        for (AmmCompetenzaDTO amm : amministrazioni)
        {
          if (procedimentiVO.getVctIdAmministrazione()
              .contains(String.valueOf(amm.getIdAmmCompetenza())))
          {
            ammFiltrati.add(amm);
          }
        }
        model.addAttribute("amministrazioni", ammFiltrati);
        model.addAttribute("tutteAmministrazioniChecked",
            procedimentiVO.isFlagShowAllAmministrazioni());
      }

      if (procedimentiVO.getVctFlagEstrazione() != null
          && procedimentiVO.getVctFlagEstrazione().size() > 0)
      {
        List<FlagEstrazioneDTO> flagFiltrati = new ArrayList<FlagEstrazioneDTO>();
        for (FlagEstrazioneDTO flag : flagEstrazione)
        {
          if (procedimentiVO.getVctFlagEstrazione()
              .contains(String.valueOf(flag.getFlagEstratta())))
          {
            flagFiltrati.add(flag);
          }
        }
        model.addAttribute("flagFiltrati", flagFiltrati);
      }

      if (procedimentiVO.getVctFlagEstrazioneExPost() != null
          && procedimentiVO.getVctFlagEstrazioneExPost().size() > 0)
      {
        List<FlagEstrazioneDTO> flagFiltratiExPost = new ArrayList<FlagEstrazioneDTO>();
        for (FlagEstrazioneDTO flag : flagEstrazioneExPost)
        {
          if (procedimentiVO.getVctFlagEstrazioneExPost()
              .contains(String.valueOf(flag.getFlagEstratta())))
          {
            flagFiltratiExPost.add(flag);
          }
        }
        model.addAttribute("flagFiltratiExPost", flagFiltratiExPost);
      }

      if (procedimentiVO.getVctIdStatoProcedimento() != null
          && procedimentiVO.getVctIdStatoProcedimento().size() > 0)
      {
        List<ProcedimentoDTO> statiFiltrati = new ArrayList<ProcedimentoDTO>();
        for (ProcedimentoDTO stato : statiProcedimento)
        {
          if (procedimentiVO.getVctIdStatoProcedimento()
              .contains(String.valueOf(stato.getIdStatoOggetto())))
          {
            statiFiltrati.add(stato);
          }
        }
        model.addAttribute("statiProcedimento", statiFiltrati);
      }

      if (procedimentiVO.getVctNotifiche() != null
          && procedimentiVO.getVctNotifiche().size() > 0)
      {
        List<GravitaNotificaVO> notificheProcedimento = new ArrayList<>();
        for (GravitaNotificaVO notifica : all_notifica)
        {
          if (procedimentiVO.getVctNotifiche()
              .contains(String.valueOf(notifica.getId())))
          {
            notificheProcedimento.add(notifica);
          }
        }
        model.addAttribute("notificheProcedimento", notificheProcedimento);
      }

      if ((procedimentiVO.getMapGruppi() != null
          && procedimentiVO.getMapGruppi().size() > 0)
          || (procedimentiVO.getMapOggetti() != null
              && procedimentiVO.getMapOggetti().size() > 0))
      {
        selectOggettiPopup(session, model);
      }

      if (!GenericValidator
          .isBlankOrNull(procedimentiVO.getTipoFiltroOggetto()))
      {
        model.addAttribute("tipoFiltroOggetti",
            procedimentiVO.getTipoFiltroOggetto());
      }
      else
      {
        model.addAttribute("tipoFiltroOggetti", "OR");
      }

    }
    else
    {
      model.addAttribute("tipoFiltroOggetti", "OR");
    }
  }

  @SuppressWarnings("unchecked")
  private void selectOggettiPopup(HttpSession session, Model model)
  {
    // decido quali checkare a video
    List<GruppoOggettoDTO> gruppiProcedimento = (List<GruppoOggettoDTO>) session
        .getAttribute("all_gruppiProcedimento");
    RicercaProcedimentiVO procedimentiVO = (RicercaProcedimentiVO) session
        .getAttribute("RicercaProcedimentiVO");

    List<DecodificaDTO<Long>> listDec = new ArrayList<>();

    if (gruppiProcedimento != null && procedimentiVO != null
        && procedimentiVO.getMapGruppi() != null)
    {
      for (GruppoOggettoDTO gruppo : gruppiProcedimento)
      {
        // Ripulisco l'oggetto
        gruppo.setSelected(false);
        if (procedimentiVO.getMapGruppi()
            .containsKey(gruppo.getIdGruppoOggetto()))
        {
          gruppo.setSelected(true);
          if (gruppo.getStati() == null || gruppo.getStati().isEmpty()
              || gruppo.getStati().size() == 1)
            gruppo.setSelected(false);

          for (EsitoOggettoDTO es : gruppo.getStati())
          {
            es.setSelected(false);
            for (Long idSel : procedimentiVO.getMapGruppi()
                .get(gruppo.getIdGruppoOggetto()))
            {
              if (idSel.longValue() == es.getIdEsito())
              {
                es.setSelected(true);
                break;
              }
            }
          }
          if (gruppo.getStati() != null && !gruppo.getStati().isEmpty())
            listDec.add(new DecodificaDTO<Long>(gruppo.getIdGruppoOggetto(),
                gruppo.getElencoDescrStati()));

        }
      }
    }

    // se la mapGruppi è vuota, deseleziono a priori tutti i gruppi
    if (gruppiProcedimento != null && procedimentiVO != null
        && procedimentiVO.getMapGruppi() == null)
    {
      for (GruppoOggettoDTO gruppo : gruppiProcedimento)
        gruppo.setSelected(false);
    }
    // se la mapOggetti è vuota, deseleziono a priori tutti gli oggetti
    if (gruppiProcedimento != null && procedimentiVO != null
        && procedimentiVO.getMapOggetti() == null)
    {
      for (GruppoOggettoDTO gruppo : gruppiProcedimento)
        if (gruppo.getOggetti() != null)
          for (OggettoDTO o : gruppo.getOggetti())
            o.setSelected(false);
    }

    // devo guardare gli oggetti a parte, perché potrei aver selezionato oggetti
    // ma senza aver selezionato il gruppo a cui appartengono
    if (gruppiProcedimento != null && procedimentiVO != null
        && procedimentiVO.getMapOggetti() != null)
    {
      for (GruppoOggettoDTO gruppo : gruppiProcedimento)
      {
        if (!gruppo.isSelected()) // se il gruppo è selezionato, inutile
          // guardare gli oggetti, non possono essere
          // stati selezionati
          if (procedimentiVO != null && procedimentiVO.getMapOggetti() != null)
          {
            if (gruppo.getOggetti() != null)
              for (OggettoDTO ogg : gruppo.getOggetti())
              {
                // Ripulisco l'oggetto
                ogg.setSelected(false);
                if (procedimentiVO.getMapOggetti()
                    .containsKey(ogg.getIdLegameGruppoOggetto()))
                {
                  ogg.setSelected(true);
                  for (EsitoOggettoDTO es : ogg.getEsitiOggetto())
                  {
                    es.setSelected(false);
                    for (Long idSel : procedimentiVO.getMapOggetti()
                        .get(ogg.getIdLegameGruppoOggetto()))
                    {
                      if (idSel.longValue() == es.getIdEsito())
                      {
                        es.setSelected(true);
                        break;
                      }
                    }
                  }
                  if (!gruppo.isSelected())
                    listDec.add(
                        new DecodificaDTO<Long>(ogg.getIdLegameGruppoOggetto(),
                            ogg.getDescrizioneElencoEsiti()));
                }
              }
          }
      }
    }

    // lista per popolare la multi-select
    model.addAttribute("selectGuppiRicerca", listDec);
    session.setAttribute("selectGuppiRicerca", listDec);
    session.setAttribute("all_gruppiProcedimento", gruppiProcedimento);
    // lista per popolare gli hidden (hGruppi e hOggetti)
    session.setAttribute("gruppiProcedimento", gruppiProcedimento);
    model.addAttribute("gruppiProcedimento", gruppiProcedimento);

  }

  private List<AmmCompetenzaDTO> filtraAmministrazioniAbilitate(
      UtenteAbilitazioni utenteAbilitazioni,
      List<AmmCompetenzaDTO> amministrazioni)
  {
    List<AmmCompetenzaDTO> ammFiltrate = new Vector<AmmCompetenzaDTO>();
    for (AmmCompetenzaDTO amm : amministrazioni)
    {
      if (IuffiUtils.PAPUASERV.hasAmministrazioneCompetenza(utenteAbilitazioni,
          amm.getIdAmmCompetenza()))
        ammFiltrate.add(amm);
    }
    return ammFiltrate;
  }

  private HashMap<Long, Vector<Long>> createMapOggetti(
      HttpServletRequest request, HttpSession session)
  {
    HashMap<Long, Vector<Long>> mapOggetti = null; // mappa del tipo
    // <idLegameGruppoOggetto,
    // List<Descrizione esito>>
    String[] hOggetti = request.getParameterValues("hOggetti"); // il value è
    // del tipo

    if (hOggetti != null)
    {
      mapOggetti = new HashMap<Long, Vector<Long>>();
      for (String oggetto : hOggetti)
      {
        if (oggetto.split("&&") != null && oggetto.split("&&")[0] != null)
        {
          Long idLegameGruppoOggetto = Long.parseLong(oggetto.split("&&")[0]);
          String idEsito = oggetto.split("&&")[1];

          if (!mapOggetti.containsKey(idLegameGruppoOggetto))
          {
            mapOggetti.put(idLegameGruppoOggetto, new Vector<Long>());
          }
          mapOggetti.get(idLegameGruppoOggetto).add(Long.parseLong(idEsito));
        }

      }
    }
    return mapOggetti;
  }

  private HashMap<Long, Vector<Long>> createMapGruppi(
      HttpServletRequest request, HttpSession session)
  {
    HashMap<Long, Vector<Long>> mapOggetti = null; // mappa del tipo
    // <idGruppoOggetto,
    // List<Descrizione esito>>
    String[] hGruppi = request.getParameterValues("hGruppi"); // il value è del
    // tipo

    if (hGruppi != null)
    {
      mapOggetti = new HashMap<Long, Vector<Long>>();
      for (String gruppo : hGruppi)
      {
        Long idGruppo = Long.parseLong(gruppo.split("&&")[0]);
        String idStato = gruppo.split("&&")[1];

        if (!mapOggetti.containsKey(idGruppo))
        {
          mapOggetti.put(idGruppo, new Vector<Long>());
        }
        mapOggetti.get(idGruppo).add(Long.parseLong(idStato));
      }
    }
    return mapOggetti;
  }

  @RequestMapping(value = "ajaxFiltraPopupLivelli", produces = "application/json", method = RequestMethod.POST)
  public @ResponseBody LinkedHashMap<Long, LivelloDTO> ajaxFiltraPopupLivelli(
      @RequestBody String ids,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {

    LinkedHashMap<Long, LivelloDTO> results = new LinkedHashMap<Long, LivelloDTO>();
    if (ids == null || ids == "")
      return null;
    ids = ids.replaceAll("=", "");
    String[] idMisureSelezionate = ids.split("&");
    session.setAttribute("checkboxMisureSelezionate", idMisureSelezionate);

    Vector<Long> vect = new Vector<Long>();
    String codiceMisura = null;
    for (String s : idMisureSelezionate)
    {
      try
      {
        vect.addElement(Long.parseLong(s));

      }
      catch (NumberFormatException e)
      {
        codiceMisura = s;
      }
    }

    if (codiceMisura != null && codiceMisura.compareTo("X") == 0)
      codiceMisura = null;

    List<LivelloDTO> op = ricercaEJB.getOperazioniMisure(vect, codiceMisura);

    if (op == null)
      return null;

    Long i = new Long(0);
    for (LivelloDTO l : op)
    {
      i++;
      results.put(i, l);
    }

    return results;
  }

  @RequestMapping(value = "ajaxIdMisureSelezionate", produces = "application/json", method = RequestMethod.POST)
  public @ResponseBody String[] ajaxIdMisureSelezionate(HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    if (session.getAttribute("checkboxMisureSelezionate") != null)
      return (String[]) session.getAttribute("checkboxMisureSelezionate");
    else
      return null;
  }

  @RequestMapping(value = "getElencoCodiciLivelliMisureJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliMisureJson(Model model,
      HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = ricercaEJB.getElencoLivelli();
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

  @RequestMapping(value = "indexProfessionisti", method = RequestMethod.GET)
  public String indexProfessionisti(Model model, HttpSession session)
      throws InternalUnexpectedException,
      ApplicationException, JsonGenerationException, JsonMappingException,
      IOException
  {
    cleanSession(session);
    session.removeAttribute("idProcedimentoOggettoDomandaGrafica");

    removeTablesFromSession(session);

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    if (utenteAbilitazioni.getRuolo().isUtenteTitolareCf())
    {
      if (utenteAbilitazioni.getEntiAbilitati() == null
          || utenteAbilitazioni.getEntiAbilitati().length <= 0)
      {
        throw new it.csi.iuffi.iuffiweb.exception.ApplicationException(
            "Funzione non abilitata. L'utente corrente non e' collegato a nessuna azienda nell'anagrafe delle aziende agricole.",
            0);
      }
    }

    session.setAttribute("comeFrom", ComeFromEnumUtils.RICERCA_PROFESSIONISTA);
    List<Long> idBandiPerAziendaProfessionista = ricercaEJB
        .getIdBandiPerAziendaProfessionista(utenteAbilitazioni,
            getProcedimentoAgricoloFromSession(session));
    Vector<String> ids = new Vector<>();
    if (idBandiPerAziendaProfessionista != null)
      for (Long i : idBandiPerAziendaProfessionista)
      {
        ids.addElement("" + i);
      }
    RicercaProcedimentiVO ric = new RicercaProcedimentiVO();
    // if(!idBandiPerAziendaProfessionista.contains(null))
    // ric.setVctIdBando(ids);

    session.setAttribute("RicercaProcedimentiVO", ric);
    HashMap<Long, List<Long>> coppieAziedaBandi = ricercaEJB
        .getAziendeBandiProfessionista(utenteAbilitazioni,
            getProcedimentoAgricoloFromSession(session));
    ric.setIdAziedaIdBandiProfessionsita(coppieAziedaBandi);

    HashMap<String, List<String>> coppieAziedaBandiPerRiepilogo = ricercaEJB
        .getAziendeBandiProfessionistaPerRiepilogo(utenteAbilitazioni,
            getProcedimentoAgricoloFromSession(session));

    String nomeCognomeProfessionista = ricercaEJB.getNomeCognomeProfessionista(
        utenteAbilitazioni, getProcedimentoAgricoloFromSession(session));
    session.setAttribute("RicercaProcedimentiVO", ric);
    model.addAttribute("infoProfessionista", "L'utente connesso "
        + nomeCognomeProfessionista
        + " risulta delegato ad operare sulle seguenti coppie azienda/bando.");
    if (coppieAziedaBandiPerRiepilogo != null)
    {
      model.addAttribute("coppieAziedaBandiPerRiepilogo",
          coppieAziedaBandiPerRiepilogo);
    }

    return eseguiRicerca(ric, model, session);
  }

  @RequestMapping(value = "getElencoCodiciLivelliSottoMisureJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliSottoMisureJson(
      Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = ricercaEJB.getElencoLivelli();
    Map<String, Object> stato;
    List<String> valList = new ArrayList<String>();
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (LivelloDTO item : livelli)
    {
      if (!GenericValidator.isBlankOrNull(item.getCodiceSottoMisura())
          && !valList.contains(item.getCodiceSottoMisura()))
      {
        stato = new HashMap<String, Object>();
        stato.put("id", item.getCodiceSottoMisura());
        stato.put("label", item.getCodiceSottoMisura());
        ret.add(stato);
        valList.add(item.getCodiceSottoMisura());
      }
    }

    return ret;
  }

  @RequestMapping(value = "getElencoSettoriJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoSettoriJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<SettoriDiProduzioneDTO> livelli = ricercaEJB.getElencoSettoriBandi();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (SettoriDiProduzioneDTO item : livelli)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getDescrizione());
      stato.put("label", item.getDescrizione());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoFocusAreaJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoFocusAreaJson(Model model,
      HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    List<FocusAreaDTO> livelli = ricercaEJB
        .getElencoFocusAreaBandi(utenteAbilitazioni.getIdProcedimento());
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (FocusAreaDTO item : livelli)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoCodiciOperazioneJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciOperazioneJson(Model model,
      HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = ricercaEJB.getElencoLivelli();
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

  @RequestMapping(value = "getNotificheFilter", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getNotificheFilter(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Map<String, Object> notifica;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;

    notifica = new HashMap<String, Object>();
    notifica.put("id", "1");
    notifica.put("label",
        "<img title=\"Warning\" src=\"../img/24/warning.png\">");
    ret.add(notifica);
    notifica = new HashMap<String, Object>();
    notifica.put("id", "3");
    notifica.put("label",
        "<img title=\"Bloccante\" src=\"../img/24/errorB.png\">");
    ret.add(notifica);
    notifica = new HashMap<String, Object>();
    notifica.put("id", "2");
    notifica.put("label", "<img title=\"Grave\" src=\"../img/24/errorG.png\">");
    ret.add(notifica);
    notifica = new HashMap<String, Object>();
    notifica.put("id", "4");
    notifica.put("label", "Nessuna notifica");
    ret.add(notifica);
    return ret;
  }

  @RequestMapping(value = "getElencoDescrizioniGruppi", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoDescrizioniGruppi(Model model,
      HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> gruppi = ricercaEJB
        .getElencoDescrizioniGruppi();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<String> item : gruppi)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoStatiOggetti", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoStatiOggetti(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> stati = ricercaEJB
        .getElencoDescrizioneStatiOggetti();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<String> item : stati)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  private void removeTablesFromSession(HttpSession session)
  {
    List<String> tableNamesToRemove = new ArrayList<>();
    tableNamesToRemove.add("elencoProcedimenti");
    tableNamesToRemove.add("elencoParticelle");
    tableNamesToRemove.add("elencoParticelleIstruttoria");
    tableNamesToRemove.add("elencoPredisposizioneParticelle");
    tableNamesToRemove.add("elencoDocumenti");
    tableNamesToRemove.add("elencoInterventi");
    tableNamesToRemove.add("elencoOggetti");
    tableNamesToRemove.add("elencoParticelleDomandaGrafica");
    tableNamesToRemove.add("dettEstrazioneTable");
    tableNamesToRemove.add("elencoDocumenti");
    tableNamesToRemove.add("elencoPartecipanti");
    cleanTableMapsInSession(session, tableNamesToRemove);
  }

}
