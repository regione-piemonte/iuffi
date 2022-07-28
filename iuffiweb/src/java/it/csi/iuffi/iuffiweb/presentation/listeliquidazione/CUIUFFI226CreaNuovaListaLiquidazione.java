package it.csi.iuffi.iuffiweb.presentation.listeliquidazione;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IAsyncEJB;
import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.StampaListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.DatiCreazioneListaDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.DatiListaDaCreareDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.LivelliBandoDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoPraticheApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONBandiNuovaListaDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RisorseImportiOperazioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi226")
@IuffiSecurity(value = "CU-IUFFI-226", controllo = IuffiSecurity.Controllo.DEFAULT)
public class CUIUFFI226CreaNuovaListaLiquidazione extends BaseController
{
  private static final String BASE_JSP_URL         = "listeliquidazione/nuova/";
  @Autowired
  IListeLiquidazioneEJB       listeLiquidazioneEJB = null;
  @Autowired
  IAsyncEJB                   asyncEJB             = null;
  @Autowired
  IQuadroEJB                  quadroEJB            = null;

  @RequestMapping(value = "/elenco_bandi", method = RequestMethod.GET)
  public String elencoBandi() throws InternalUnexpectedException
  {
    return BASE_JSP_URL + "elencoBandi";
  }

  @RequestMapping(value = "/elenco_operazioni_bando_{idBando}", method = RequestMethod.GET)
  public String bandoOperazione(HttpSession session, Model model,
      @ModelAttribute("idBando") @PathVariable("idBando") long idBando)
      throws InternalUnexpectedException
  {
    clearCommonInSession(session);
    final LivelliBandoDTO livelliBando = listeLiquidazioneEJB
        .getLivelliBando(idBando);
    assert livelliBando != null : "listeLiquidazioneEJB.getLivelliBando non ha trovato dati per idBando = "
        + idBando;
    final List<DecodificaDTO<Long>> amministrazioni = listeLiquidazioneEJB
        .findAmministrazioniInProcedimentiBando(idBando,
            getListIdAmmCompetenza(session));
    final List<DecodificaDTO<Integer>> listTipiImporto = listeLiquidazioneEJB
        .getTabellaDecodifica("IUF_D_TIPO_IMPORTO",
            true);
    defaultModelBandoOperazione(model, livelliBando, amministrazioni,
        listTipiImporto);

    return BASE_JSP_URL + "bandoOperazione";
  }

  protected void defaultModelBandoOperazione(Model model,
      final LivelliBandoDTO livelliBando,
      final List<DecodificaDTO<Long>> amministrazioni,
      final List<DecodificaDTO<Integer>> listTipiImporto)
  {
    model.addAttribute("livelliBando", livelliBando);
    model.addAttribute("listAmmCompetenza", amministrazioni);
    if (amministrazioni != null && amministrazioni.size() == 1)
    {
      model.addAttribute("idAmmCompSel", amministrazioni.get(0).getId());
    }
    model.addAttribute("listTipiImporto", listTipiImporto);
    if (listTipiImporto != null && listTipiImporto.size() == 1)
    {
      model.addAttribute("idTipoImportoSel", listTipiImporto.get(0).getId());
    }
  }

  public static class BandoOperazioneParametersVO
  {
    private String extIdAmmCompetenza;
    private String idTipoImporto;

    public String getExtIdAmmCompetenza()
    {
      return extIdAmmCompetenza;
    }

    public void setExtIdAmmCompetenza(String extIdAmmCompetenza)
    {
      this.extIdAmmCompetenza = extIdAmmCompetenza;
    }

    public String getIdTipoImporto()
    {
      return idTipoImporto;
    }

    public void setIdTipoImporto(String idTipoImporto)
    {
      this.idTipoImporto = idTipoImporto;
    }
  }

  @RequestMapping(value = "/elenco_operazioni_bando_{idBando}", method = RequestMethod.POST)
  public String bandoOperazionePost(HttpSession session, Model model,
      @ModelAttribute("idBando") @PathVariable("idBando") long idBando,
      BandoOperazioneParametersVO parameters)
      throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    Long extIdAmmCompetenza = errors.validateMandatoryID(
        parameters.extIdAmmCompetenza, "extIdAmmCompetenza");
    final List<DecodificaDTO<Long>> amministrazioni = listeLiquidazioneEJB
        .findAmministrazioniInProcedimentiBando(idBando,
            getListIdAmmCompetenza(session));
    DecodificaDTO<Long> ammCompetenza = null;
    if (extIdAmmCompetenza != null)
    {
      ammCompetenza = errors.validateIDInDecodificaRange(extIdAmmCompetenza,
          "extIdAmmCompetenza", amministrazioni,
          "L'amministrazione selezionata non rientra tra quelle di propria competenza");
    }
    Long idTipoImporto = errors.validateMandatoryID(parameters.idTipoImporto,
        "idTipoImporto");
    final List<DecodificaDTO<Integer>> listTipiImporto = listeLiquidazioneEJB
        .getTabellaDecodifica("IUF_D_TIPO_IMPORTO",
            true);
    DecodificaDTO<Integer> tipoImporto = null;
    if (idTipoImporto != null)
    {
      tipoImporto = errors.validateIDInDecodificaRange(idTipoImporto.intValue(),
          "idTipoImporto", listTipiImporto,
          "La tipologia di importo indicata non è tra quelle disponibili");
    }

    if (errors.addToModelIfNotEmpty(model))
    {
      final LivelliBandoDTO livelliBando = listeLiquidazioneEJB
          .getLivelliBando(idBando);
      assert livelliBando != null : "listeLiquidazioneEJB.getLivelliBando non ha trovato dati per idBando = "
          + idBando;
      defaultModelBandoOperazione(model, livelliBando, amministrazioni,
          listTipiImporto);
      model.addAttribute("preferRequest", Boolean.TRUE);
      return BASE_JSP_URL + "bandoOperazione";
    }
    else
    {
      Map<String, Object> common = getCommonFromSession("CU-IUFFI-226", session,
          false);
      common.put("idBando", idBando);
      common.put("ammCompetenza", ammCompetenza);
      common.put("tipoImporto", tipoImporto);
      saveCommonInSession(common, session);
      return "redirect:crea_lista_" + idBando + ".do";
    }
  }

  @ResponseBody
  @RequestMapping(value = "/json/elenco", method = RequestMethod.GET, produces = "application/json")
  public List<RigaJSONBandiNuovaListaDTO> jsonElenco(HttpSession session)
      throws InternalUnexpectedException
  {
    List<Long> lIdAmmCompetenza = getListIdAmmCompetenza(session);
    return listeLiquidazioneEJB
        .getBandiProntiPerListeLiquidazione(lIdAmmCompetenza);
  }

  @ResponseBody
  @RequestMapping(value = "/json/stampa", method = RequestMethod.GET, produces = "application/json")
  public Map<String, Boolean> jsonStampa(HttpSession session)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-226", session,
        false);
    DatiCreazioneListaDTO datiCreazioneListaDTO = (DatiCreazioneListaDTO) common
        .get("datiCreazione");
    Boolean finished = Boolean.TRUE;
    if (datiCreazioneListaDTO != null)
    {
      StampaListaLiquidazioneDTO stampa = listeLiquidazioneEJB
          .getStampaListaLiquidazione(
              datiCreazioneListaDTO.getIdListaLiquidazione());
      if (stampa != null)
      {
        final long idStatoStampa = stampa.getIdStatoStampa().longValue();
        if (idStatoStampa == IuffiConstants.STATO.STAMPA.ID.GENERAZIONE_STAMPA_IN_CORSO
            ||
            idStatoStampa == IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA ||
            idStatoStampa == IuffiConstants.STATO.STAMPA.ID.ANNULLATO)
        {
          finished = Boolean.FALSE;
        }
      }
    }
    Map<String, Boolean> result = new HashMap<String, Boolean>();
    result.put("finished", finished);
    return result;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/crea_lista_{idBando}", method = RequestMethod.GET)
  public String creaLista(HttpSession session, Model model,
      @ModelAttribute("idBando") @PathVariable("idBando") long idBando)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-226", session,
        false);
    Long idBandoCommon = (Long) common.get("idBando");
    if (idBandoCommon == null || idBando != idBandoCommon.longValue())
    {
      return BASE_JSP_URL + "erroreCommon";
    }

    final LivelliBandoDTO livelliBando = listeLiquidazioneEJB
        .getLivelliBando(idBando);
    assert livelliBando != null : "listeLiquidazioneEJB.getLivelliBando non ha trovato dati per idBando = "
        + idBando;
    DecodificaDTO<Integer> tipoImporto = (DecodificaDTO<Integer>) common
        .get("tipoImporto");
    DecodificaDTO<Long> ammCompetenza = (DecodificaDTO<Long>) common
        .get("ammCompetenza");
    final Long idAmmCompetenza = ammCompetenza.getId();
    List<DecodificaDTO<Long>> tecnici = listeLiquidazioneEJB
        .getTecniciLiquidatori(idAmmCompetenza, getUtenteAbilitazioni(session).getIdProcedimento());
    final Integer idTipoImporto = tipoImporto.getId();

    // AALLORA -> dalle risorse devo escludere quelle che l'utente sceglie di
    // escludere dalla popup con l'elenco delle pratiche
    // gli idsPO sono in common come "idsPODaEscludere". Quindi dovrei andare a
    // rimuovere tutte quelle relative a questo PO
    List<Long> idsPODaEscludere = (List<Long>) common.get("idsPODaEscludere");
    DatiListaDaCreareDTO datiListaDaCreareDTO = listeLiquidazioneEJB
        .getDatiListaDaCreare(idBando, idAmmCompetenza,
            idTipoImporto, idsPODaEscludere);
    List<RisorseImportiOperazioneDTO> risorse = datiListaDaCreareDTO
        .getRisorse();

    common.put("DATI_LISTA", datiListaDaCreareDTO);
    saveCommonInSession(common, session);
    model.addAttribute("errorMsg", canCreate(risorse));
    model.addAttribute("livelliBando", livelliBando);
    model.addAttribute("descAmmCompetenza", ammCompetenza.getDescrizione());
    model.addAttribute("descTipoImporto", tipoImporto.getDescrizione());
    model.addAttribute("livelliBando", livelliBando);
    model.addAttribute("tecnici", tecnici);

    Long idTecnicoLiquidatore = (Long) common.get("idTecnicoLiquidatore");
    if (idTecnicoLiquidatore != null)
      model.addAttribute("idTecnicoLiquidatore", idTecnicoLiquidatore);
    // common.remove("idTecnicoLiquidatore");

    model.addAttribute("risorse", risorse);
    for (RisorseImportiOperazioneDTO r : risorse)
      if (r.getRaggruppamento() != null
          && r.getRaggruppamento().compareTo("") != 0)
      {
        model.addAttribute("showColumnUlterioreSpecializzazione", true);
        break;
      }
    return BASE_JSP_URL + "creaLista";
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/crea_lista_{idBando}", method = RequestMethod.POST)
  public String creaListaPost(HttpSession session, Model model,
      @ModelAttribute("idBando") @PathVariable("idBando") long idBando,
      @RequestParam(value = "idTecnico", required = false) String idTecnico)
      throws InternalUnexpectedException, JsonGenerationException,
      JsonMappingException, IOException
  {
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-226", session,
        false);
    Long idBandoCommon = (Long) common.get("idBando");
    if (idBandoCommon == null || idBando != idBandoCommon.longValue())
    {
      return BASE_JSP_URL + "erroreCommon";
    }
    final LivelliBandoDTO livelliBando = listeLiquidazioneEJB
        .getLivelliBando(idBando);
    assert livelliBando != null : "listeLiquidazioneEJB.getLivelliBando non ha trovato dati per idBando = "
        + idBando;
    DecodificaDTO<Integer> tipoImporto = (DecodificaDTO<Integer>) common
        .get("tipoImporto");
    DecodificaDTO<Long> ammCompetenza = (DecodificaDTO<Long>) common
        .get("ammCompetenza");
    final Long idAmmCompetenza = ammCompetenza.getId();
    List<DecodificaDTO<Long>> tecnici = listeLiquidazioneEJB
        .getTecniciLiquidatori(idAmmCompetenza, getUtenteAbilitazioni(session).getIdProcedimento());
    Errors errors = new Errors();
    Long idTecnicoLiquidatore = errors.validateMandatoryID(idTecnico,
        "idTecnico");
    if (idTecnicoLiquidatore != null)
    {
      errors.validateIDInDecodificaRange(idTecnicoLiquidatore, "idTecnico",
          tecnici,
          "Il funzionario liquidatore selezionato non è presente nell'elenco dei funzionari abilitati");
    }
    final Integer idTipoImporto = tipoImporto.getId();
    if (errors.isEmpty())
    {
      // Nessun errore di validazione ==> procedo alla creazione della lista.
      // Il medoto effettuerà ancora una verifica sulla congruenza dei dati, per
      // questo gli passo la situzione dei dati
      // che ho in sessione
      try
      {
        List<Long> idsPODaEscludere = (List<Long>) common
            .get("idsPODaEscludere");
        common.put("idTecnicoLiquidatore", idTecnicoLiquidatore);
        int idProcedimentoAgricolo = getUtenteAbilitazioni(session).getIdProcedimento();
        DatiCreazioneListaDTO datiCreazioneListaDTO = listeLiquidazioneEJB
            .creaListaLiquidazione(idBando,
                idAmmCompetenza, idTipoImporto, idTecnicoLiquidatore,
                (DatiListaDaCreareDTO) common.get("DATI_LISTA"),
                getIdUtenteLogin(session), idsPODaEscludere, idProcedimentoAgricolo);
        common.put("datiCreazione", datiCreazioneListaDTO);
        saveCommonInSession(common, session);
        // tutto ok... si ritorna all'elenco delle liste di liquidazione
        impostaFiltro(session, datiCreazioneListaDTO);
        asyncEJB.generaStampaListaLiquidazione(
            datiCreazioneListaDTO.getIdListaLiquidazione());
        return "redirect:../cuiuffi226/creazione_lista_terminata_" + idBando
            + ".do";
      }
      catch (ApplicationException e)
      {
        // Si è verificato un errore di congruenza dati su db (qualcuno ha
        // creato una nuova lista o modificato le
        // risorse/pagamenti in modo da rendere la situazione non più
        // compatibile con quella di partenza
        // Segnalo la cosa all'utente e ricarico la pagina

        // se scateno l'eccezioe con codice -123, significa che il problema è
        // sul tecnico (vari controlli fatti nell'ejb).
        // il tecnico liquidatore risulta aver ricoperto il ruolo di istruttore
        // in almeno uno dei pagamenti in liquidazione
        if (e.getErrorCode() == -123)
        {
          // in questo caso devo andare a cercarmi i procedimenti ogg (pratiche)
          // su cui questo tecnico ha ricoperto il ruolo
          // di istruttore in almeno uno dei pagamenti in liquidazione.
          // a queste pratiche devo aggiungere le anomalie

          // 1) findPratiche
          int idProcedimentoAgricolo = getUtenteAbilitazioni(session).getIdProcedimento();
          List<RiepilogoPraticheApprovazioneDTO> riepilogo = listeLiquidazioneEJB
              .getRiepilogoPraticheInNuovaListaWithAnomalia(idBando,
                  ammCompetenza.getId().longValue(),
                  tipoImporto.getId().intValue(), idTecnicoLiquidatore, idProcedimentoAgricolo);
          // 2) addInCommon
          common.put("praticheConAnomalia", riepilogo);
          saveCommonInSession(common, session);
        }

        errors.addError("error", e.getMessage());
      }
    }
    // Se sono arrivato qua è perchè c'è stato un errore
    errors.addToModel(model);
    // Errori di validazione o nella creazione della lista ==> Segnalo
    // all'utente ricaricando la pagina
    List<Long> idsPODaEscludere = (List<Long>) common.get("idsPODaEscludere");
    DatiListaDaCreareDTO datiListaDaCreareDTO = listeLiquidazioneEJB
        .getDatiListaDaCreare(idBando, idAmmCompetenza,
            idTipoImporto, idsPODaEscludere);
    List<RisorseImportiOperazioneDTO> risorse = datiListaDaCreareDTO
        .getRisorse();
    common.put("DATI_LISTA", datiListaDaCreareDTO);
    saveCommonInSession(common, session);
    model.addAttribute("errorMsg", canCreate(risorse));
    model.addAttribute("livelliBando", livelliBando);
    model.addAttribute("descAmmCompetenza", ammCompetenza.getDescrizione());
    model.addAttribute("descTipoImporto", tipoImporto.getDescrizione());
    model.addAttribute("livelliBando", livelliBando);
    model.addAttribute("tecnici", tecnici);
    model.addAttribute("risorse", risorse);
    if (idTecnicoLiquidatore != null)
      model.addAttribute("idTecnicoLiquidatore", idTecnicoLiquidatore);

    for (RisorseImportiOperazioneDTO r : risorse)
      if (r.getRaggruppamento() != null
          && r.getRaggruppamento().compareTo("") != 0)
      {
        model.addAttribute("showColumnUlterioreSpecializzazione", true);
        break;
      }

    model.addAttribute("preferRequest", Boolean.TRUE);
    return BASE_JSP_URL + "creaLista";
  }

  @SuppressWarnings("unchecked")
  protected void impostaFiltro(HttpSession session,
      DatiCreazioneListaDTO datiCreazioneListaDTO)
      throws JsonGenerationException, JsonMappingException, IOException
  {
    Map<String, Object> mapFiltro = new HashMap<String, Object>();

    Map<String, String> mapNumeroLista = new HashMap<String, String>();
    mapNumeroLista.put("eq", "" + datiCreazioneListaDTO.getNumeroLista() + "");
    mapFiltro.put("numListaStr", mapNumeroLista);

    Map<String, String> mapDenominazioneBando = new HashMap<String, String>();
    mapDenominazioneBando.put("cnt",
        datiCreazioneListaDTO.getDenominazioneBando());
    mapFiltro.put("denominazioneBando", mapDenominazioneBando);

    ObjectMapper mapper = new ObjectMapper();
    String filtro = mapper.writeValueAsString(mapFiltro);
    filtro = filtro.replace("\\\"", "\\\\\"");
    Map<String, String> filtroInSessione = (Map<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    filtroInSessione.put("elencoListeLiquidazione", filtro);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA,
        filtroInSessione);
  }

  @RequestMapping("/creazione_lista_terminata_{idBando}")
  public String creazioneListaTerminata(HttpSession session,
      @ModelAttribute("idBando") @PathVariable("idBando") long idBando)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-226", session,
        false);
    Long idBandoCommon = (Long) common.get("idBando");
    if (idBandoCommon == null || idBando != idBandoCommon.longValue())
    {
      return BASE_JSP_URL + "erroreCommon";
    }
    return BASE_JSP_URL + "creazioneListaTerminata";
  }

  private String canCreate(List<RisorseImportiOperazioneDTO> risorse)
  {
    if (risorse != null)
    {
      int countPratiche = 0;
      for (RisorseImportiOperazioneDTO risorsa : risorse)
      {
        countPratiche += risorsa.getNumeroPagamentiLista();
        if (risorsa.getImportoRimanente().compareTo(BigDecimal.ZERO) < 0)
        {
          return "I fondi a disposizione non sono sufficienti, impossibile proseguire con la creazione della lista";
        }
      }
      if (countPratiche == 0)
      {
        return "Non ci sono pagamenti da liquidare oppure non ci sono Fondi disponibili attivi, impossibile proseguire con la creazione della lista";
      }
      return null;
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @ResponseBody
  @RequestMapping(value = "/json/pratiche_nuova_lista_{idBando}", method = RequestMethod.GET, produces = "application/json")
  public List<RiepilogoPraticheApprovazioneDTO> jsonRiepilogoPratiche(
      HttpSession session,
      @PathVariable("idBando") long idBando,
      BandoOperazioneParametersVO parameters) throws InternalUnexpectedException
  {
    Map<String, Object> common = (HashMap<String, Object>) getCommonFromSession(
        "CU-IUFFI-226", session, false);
    DecodificaDTO<Integer> tipoImporto = (DecodificaDTO<Integer>) common
        .get("tipoImporto");
    DecodificaDTO<Long> ammCompetenza = (DecodificaDTO<Long>) common
        .get("ammCompetenza");
    List<RiepilogoPraticheApprovazioneDTO> riepilogo = listeLiquidazioneEJB
        .getRiepilogoPraticheInNuovaLista(idBando, ammCompetenza.getId(),
            tipoImporto.getId());
    if (riepilogo == null)
    {
      riepilogo = new ArrayList<RiepilogoPraticheApprovazioneDTO>();
    }

    // leggo da common la lista delle pratiche con anomalia relativa al tecnico
    // calcolata prima (può non esserci)
    // setto l'anomalia anche nelle pratiche appena lette
    List<RiepilogoPraticheApprovazioneDTO> riepilogoConAnomalia = (List<RiepilogoPraticheApprovazioneDTO>) common
        .get("praticheConAnomalia");
    if (riepilogoConAnomalia != null)
      for (RiepilogoPraticheApprovazioneDTO ra : riepilogoConAnomalia)
      {
        for (RiepilogoPraticheApprovazioneDTO r : riepilogo)
          if (ra.getIdentificativo().compareTo(r.getIdentificativo()) == 0
              && ra.getAnomalia().compareTo("S") == 0)
            r.setAnomalia("S");
      }

    // setto la var checked per vedere se sono checkate per essere escluse
    List<Long> idsPODaEscludere = (List<Long>) common.get("idsPODaEscludere");
    if (idsPODaEscludere != null && !idsPODaEscludere.isEmpty())
    {
      for (RiepilogoPraticheApprovazioneDTO r : riepilogo)
        for (Long id : idsPODaEscludere)
          if (id.longValue() == r.getIdProcedimentoOggetto().longValue())
            r.setChecked(true);
    }

    return riepilogo;
  }

  @RequestMapping(value = "/json/elenco_pratiche_nuova_lista_{idBando}", method = RequestMethod.GET)
  public String elencoPraticheNuovaLista(HttpSession session, Model model,
      @ModelAttribute("idBando") @PathVariable("idBando") long idBando)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-226", session,
        false);
    Long idBandoCommon = (Long) common.get("idBando");
    if (idBandoCommon == null || idBando != idBandoCommon.longValue())
    {
      return BASE_JSP_URL + "erroreCommon";
    }

    BandoDTO bando = quadroEJB.getInformazioniBando(idBando);
    if (bando != null && bando.getCodiceTipoBando().compareTo("P") == 0)
      model.addAttribute("isBandoPremio", true);

    return BASE_JSP_URL + "elencoPratiche";
  }

  @RequestMapping(value = "/escludiPratiche_{idBando}", method = RequestMethod.POST)
  public String escludiPratiche(Model model,
      @PathVariable("idBando") long idBando, HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException, IuffiPermissionException
  {

    String[] idsPOv = request.getParameterValues("idPOCheck");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idsPOv);
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-226", session,
        false);

    if (ids != null && !ids.isEmpty())
    {
      common.put("idsPODaEscludere", ids);
      saveCommonInSession(common, session);
    }
    else
    {
      common.remove("idsPODaEscludere");
      saveCommonInSession(common, session);
    }

    return "redirect:../cuiuffi226/crea_lista_" + idBando + ".do";
  }

  @RequestMapping(value = "downloadExcelPratiche")
  public ModelAndView downloadExcel(HttpSession session, Model model)
      throws InternalServiceException, InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("CU-IUFFI-226", session,
        false);
    Long idBandoCommon = (Long) common.get("idBando");
    List<RiepilogoPraticheApprovazioneDTO> pratiche = jsonRiepilogoPratiche(
        session, idBandoCommon, null);
    return new ModelAndView("excelElencoPraticheListaLiquidazioneView",
        "pratiche", pratiche);
  }
}
