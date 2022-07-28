package it.csi.iuffi.iuffiweb.presentation.nuovoprocedimento;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.INuovoProcedimentoEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.AmmCompetenzaDTO;
import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.dto.FiltroVO;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.MapColonneNascosteVO;
import it.csi.iuffi.iuffiweb.dto.SettoriDiProduzioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.OggettoDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.presentation.interceptor.security.IuffiSecurityManager;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/nuovoprocedimento")
@IuffiSecurity(value = "CU-IUFFI-101", controllo = IuffiSecurity.Controllo.DEFAULT)
public class NuovoProcedimentoController extends BaseController
{

  private static final String     SESSION_CUAA_RICERCA_SINGOLA = "SESSION_CUAA_RICERCA_SINGOLA";
  private static final String     SESSION_BANDO_SELEZIONATO    = "BandoSelezionatoNuovoProcedimentoSession";

  @Autowired
  private INuovoProcedimentoEJB   nuovoProcedimento            = null;
  @Autowired
  private IRicercaEJB             ricercaEJB                   = null;
  @Autowired
  private IuffiSecurityManager securityManager;

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "elencobando", method = RequestMethod.GET)
  public String elencobando(Model model, HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    cleanSession(session);
    /*
     * Rimuovi dalla sessione le info delle bootstrap-table elencate.
     */
	List<String> tableNamesToRemove = new ArrayList<>();
	tableNamesToRemove.add("elencoParticelle");
	tableNamesToRemove.add("elencoParticelleIstruttoria");
	tableNamesToRemove.add("elencoPredisposizioneParticelle");
	tableNamesToRemove.add("elencoProcedimenti");
	tableNamesToRemove.add("elencoOggetti");
	//IUFFI Tables
	tableNamesToRemove.add("tableElencoMotoriAgricoli"); //motori agricoli
	tableNamesToRemove.add("tableElencoSuperificiColture"); //superfici colture
	tableNamesToRemove.add("tableSuperificiColturePlvVegetale");
	tableNamesToRemove.add("tableDettaglioParticellareSuperificiColture");
	tableNamesToRemove.add("tableElencoAllevamenti"); //allevamenti
	tableNamesToRemove.add("tableListPlvZootecnicaDettaglio");
	tableNamesToRemove.add("tableDettaglioAllevamentiPlv");
	tableNamesToRemove.add("tableElencoFabbricati"); //fabbricati
	tableNamesToRemove.add("tableDettaglioFabbricati");
	tableNamesToRemove.add("tableElencoPrestitiAgrari"); //prestiti agrari
	tableNamesToRemove.add("tableElencoDanni"); //danni
	tableNamesToRemove.add("tblRicercaConduzioni");
	tableNamesToRemove.add("tblRicercaConduzioniRiepilogo");
	tableNamesToRemove.add("tblConduzioni");
	tableNamesToRemove.add("tableElencoScorte"); //scorte
    tableNamesToRemove.add("tableElencoScorte"); // scorte
    tableNamesToRemove.add("tableElencoDanniFauna"); // danni fauna CUIUFFI311L
    tableNamesToRemove.add("tableLocalizzazioneDanniFauna"); // danni fauna CUIUFFI311D
    cleanTableMapsInSession(session, tableNamesToRemove);
    
    // E' necessario ripulire gli oggetti legati ai filtri dalla sessione
    final String ID_ELENCO_PARTICELLE = "elencoParticelle";
    final String ID_ELENCO_PARTICELLE_ISTR = "elencoParticelleIstruttoria";
    final String ID_ELENCO_PPREDISP_PARTICELLE = "elencoPredisposizioneParticelle";
    final String ID_ELENCO_AZIENDE = "elencoProcedimenti";
    final String ID_ELENCO_OGGETTI = "elencoOggetti";
    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    if(mapFilters != null)
    {
    	mapFilters.remove(ID_ELENCO_PARTICELLE);
    	mapFilters.remove(ID_ELENCO_PARTICELLE_ISTR);
    	mapFilters.remove(ID_ELENCO_PPREDISP_PARTICELLE);
        mapFilters.remove(ID_ELENCO_PARTICELLE_ISTR);
        mapFilters.remove(ID_ELENCO_PPREDISP_PARTICELLE);
    }
    MapColonneNascosteVO hColumns = (MapColonneNascosteVO) session.getAttribute(
        IuffiConstants.GENERIC.SESSION_VAR_COLONNE_NASCOSTE);
    
    logger.debug("STEP: hcolumns=" + hColumns);
    if(hColumns != null)
    {
    	hColumns.removeTable(ID_ELENCO_PARTICELLE);
    }
    HashMap<String, String> mapNumPagine = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_NUMERO_PAGINA);
    if (mapNumPagine != null)
    {
      mapNumPagine.remove(ID_ELENCO_AZIENDE);
    }
    HashMap<String, String> mapRigheVisibili = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_RIGHE_VISIBILI);
    if (mapRigheVisibili != null)
    {
      mapNumPagine.remove(ID_ELENCO_OGGETTI);
    }

    // questo parametro viene testato in seguito per creare dinamicamente il
    // filo di arianna dei quadri
    session.removeAttribute("comeFromRicerca");
    session.removeAttribute(SESSION_CUAA_RICERCA_SINGOLA);
    session.removeAttribute("vIdAziende");

    logger.debug("STEP: utenteAbilitazioni");
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    logger.debug("STEP: utenteAbilitazioni = " + utenteAbilitazioni);
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
    logger.debug("STEP: verifica procedimento agricolo");
	int idProcedimentoAgricolo = getUtenteAbilitazioni(session).getIdProcedimento();
	ArrayList<BandoDTO> list = (ArrayList<BandoDTO>) nuovoProcedimento
        .getElencoBandiAttivi(idProcedimentoAgricolo, utenteAbilitazioni);
    if (list != null && !list.isEmpty())
    {
      model.addAttribute("bandi", list);
    }
    else
    {
      model.addAttribute("msgError", "Nessun bando attivo");
    }

    // applyFilterToList(session, model, list);

    
    return "nuovoprocedimento/elencoBando";
  }

  @RequestMapping(value = "/json/getElencoBandi", produces = "application/json")
  @ResponseBody
  public List<BandoDTO> getElencoRicevutePagamento(Model model,
      HttpSession session) throws InternalUnexpectedException
  {
	 
	int idProcedimentoAgricolo = getUtenteAbilitazioni(session).getIdProcedimento();
    ArrayList<BandoDTO> elenco = (ArrayList<BandoDTO>) nuovoProcedimento
        .getElencoBandiAttivi(idProcedimentoAgricolo, getUtenteAbilitazioni(session));

	if (elenco != null) {
		for (BandoDTO b : elenco) {
			long idBando = b.getIdBando();
			b.setLivelli(ricercaEJB.getLivelliBando(idBando));
		}
	}   

    return (elenco != null) ? elenco : new ArrayList<>();
  }

  @RequestMapping(value = "proseguiCuaa_{idBando}_{cuaaProvenienza}" ,method = RequestMethod.GET)
  public String proseguiCuaa(@PathVariable(value="idBando") Long idBando , 
      @PathVariable(value="cuaaProvenienza") String cuaaProvenienza , 
      Model model, HttpSession session) throws InternalUnexpectedException, ApplicationException, ParseException
  {
    if(idBando == null)
    {
      String msgErrore = "Per procedere è necessario selezionare un bando dall'elenco!";
      model.addAttribute("msgErrore", msgErrore);
      return elencobando(model,session);
    }
    
    
    /*E' necessario settare questa variabile in sessione poichè se torno in un secondo momento sulla pagina di elenco bandi vedrò il mio bando
      scelto già selezionato*/
    session.setAttribute("idBandoSelezionato",idBando);
    
    BandoDTO bandoSelezionato = nuovoProcedimento.getDettaglioBandoByIdBando(idBando);
    if(bandoSelezionato.getDescrizioneFiltro() != null)
    {
      bandoSelezionato.setDescrizioneFiltro(StringEscapeUtils.escapeHtml4(bandoSelezionato.getDescrizioneFiltro()).replace("\n", "<br/>"));
      }
    session.setAttribute(SESSION_BANDO_SELEZIONATO, bandoSelezionato);
    
        model.addAttribute("bando", bandoSelezionato);
        model.addAttribute("idBando", bandoSelezionato.getIdBando());
      model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
      model.addAttribute("idLegameGruppoOggetto", bandoSelezionato.getIdLegameGruppoOggetto());
        model.addAttribute("descrizioneFiltro", bandoSelezionato.getDescrizioneFiltro());
        
        if(cuaaProvenienza!=null && !cuaaProvenienza.equals("")) {
          model.addAttribute("cuaa", cuaaProvenienza);
          model.addAttribute("cuaaProvenienza", cuaaProvenienza);
        }
        NuovaDomanda nuovaDomanda = new NuovaDomanda();
        nuovaDomanda.setCuaa(cuaaProvenienza);
        nuovaDomanda.setIdBando(idBando);
        nuovaDomanda.setIdBandoOggetto(bandoSelezionato.getIdBandoOggetto());
        model.addAttribute("nuovaDomanda", nuovaDomanda);
        List<Long> aziende = nuovoProcedimento.getAziendeByCUAA(cuaaProvenienza, null, null, getUtenteAbilitazioni(session));
        if(aziende!=null && !aziende.isEmpty())
        {
          nuovaDomanda.setIdAzienda(aziende.get(0));
          //return creaProcedimento(nuovaDomanda, model, session);

        }
        return ricercaBandoSingolo(nuovaDomanda, model, session);
      //return "nuovoprocedimento/dettaglioBando";
  }
  
  @RequestMapping(value = "elencobando", method = RequestMethod.POST)
  public String onSubmit(@ModelAttribute SceltaBando sceltaBando, Model model,
      HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    if (sceltaBando.getIdBandoSelezionato() == null)
    {
      String msgErrore = "Per procedere è necessario selezionare un bando dall'elenco!";
      model.addAttribute("msgErrore", msgErrore);
      return elencobando(model, session);
    }

    /*
     * E' necessario settare questa variabile in sessione poichè se torno in un
     * secondo momento sulla pagina di elenco bandi vedrò il mio bando scelto
     * già selezionato
     */
    session.setAttribute("idBandoSelezionato",
        sceltaBando.getIdBandoSelezionato());

    BandoDTO bandoSelezionato = nuovoProcedimento
        .getDettaglioBandoByIdBando(sceltaBando.getIdBandoSelezionato());
    if (bandoSelezionato.getDescrizioneFiltro() != null)
    {
      bandoSelezionato.setDescrizioneFiltro(
          StringEscapeUtils.escapeHtml4(bandoSelezionato.getDescrizioneFiltro())
              .replace("\n", "<br/>"));
    }
    session.setAttribute(SESSION_BANDO_SELEZIONATO, bandoSelezionato);

    model.addAttribute("bando", bandoSelezionato);
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());
    model.addAttribute("descrizioneFiltro",
        bandoSelezionato.getDescrizioneFiltro());

    return "nuovoprocedimento/dettaglioBando";
  }

  @RequestMapping(value = "prosegui_{idBando}", method = RequestMethod.GET)
  public String prosegui(@PathVariable(value = "idBando") Long idBando,
      Model model, HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    if (idBando == null)
    {
      String msgErrore = "Per procedere è necessario selezionare un bando dall'elenco!";
      model.addAttribute("msgErrore", msgErrore);
      return elencobando(model, session);
    }

    /*
     * E' necessario settare questa variabile in sessione poichè se torno in un
     * secondo momento sulla pagina di elenco bandi vedrò il mio bando scelto
     * già selezionato
     */
    session.setAttribute("idBandoSelezionato", idBando);

    BandoDTO bandoSelezionato = nuovoProcedimento
        .getDettaglioBandoByIdBando(idBando);
    if (bandoSelezionato.getDescrizioneFiltro() != null)
    {
      bandoSelezionato.setDescrizioneFiltro(
          StringEscapeUtils.escapeHtml4(bandoSelezionato.getDescrizioneFiltro())
              .replace("\n", "<br/>"));
    }
    session.setAttribute(SESSION_BANDO_SELEZIONATO, bandoSelezionato);

    model.addAttribute("bando", bandoSelezionato);
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());
    model.addAttribute("descrizioneFiltro",
        bandoSelezionato.getDescrizioneFiltro());

    return "nuovoprocedimento/dettaglioBando";
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "dettaglioBando", method = RequestMethod.GET)
  public String dettaglioBando(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    // E' necessario ripulire gli oggetti legati ai filtri dell'elenco azienda
    session.removeAttribute("vIdAziende");
    final String ID_ELENCO_AZIENDE = "elencoAziende";
    final String ID_ELENCO_OGGETTI = "elencoOggetti";
    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    mapFilters.remove(ID_ELENCO_AZIENDE);
    MapColonneNascosteVO hColumns = (MapColonneNascosteVO) session.getAttribute(
        IuffiConstants.GENERIC.SESSION_VAR_COLONNE_NASCOSTE);
    hColumns.removeTable(ID_ELENCO_AZIENDE);
    HashMap<String, String> mapNumPagine = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_NUMERO_PAGINA);
    if (mapNumPagine != null)
    {
      mapNumPagine.remove(ID_ELENCO_AZIENDE);
    }
    HashMap<String, String> mapRigheVisibili = (HashMap<String, String>) session
        .getAttribute(IuffiConstants.GENERIC.SESSION_VAR_RIGHE_VISIBILI);
    if (mapRigheVisibili != null)
    {
      mapNumPagine.remove(ID_ELENCO_OGGETTI);
    }
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    String sessionCuaa = (String) session
        .getAttribute(SESSION_CUAA_RICERCA_SINGOLA);
    if (sessionCuaa != null)
    {
      model.addAttribute("cuaa", sessionCuaa);
    }
    model.addAttribute("bando", bandoSelezionato);
    model.addAttribute("descrizioneFiltro",
        bandoSelezionato.getDescrizioneFiltro());
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());
    return "nuovoprocedimento/dettaglioBando";
  }

  @RequestMapping(value = "ricercaBandoSingolo", method = RequestMethod.POST)
  public String ricercaBandoSingolo(@ModelAttribute NuovaDomanda nuovaDomanda,
      Model model, HttpSession session)
      throws InternalUnexpectedException, ParseException
  {
    Errors errors = new Errors();
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);

    model.addAttribute("cuaa", nuovaDomanda.getCuaa());
    model.addAttribute("partitaIva", nuovaDomanda.getPartitaIva());
    model.addAttribute("denominazione", nuovaDomanda.getDenominazione());
      model.addAttribute("descrizioneFiltro",
        bandoSelezionato.getDescrizioneFiltro());
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());

    //if (errors.validateMandatory(nuovaDomanda.getCuaa(), "cuaa"))
      //errors.validateCuaa(nuovaDomanda.getCuaa(), "cuaa");
    if(nuovaDomanda.getCuaa()!=null && !"".equals(nuovaDomanda.getCuaa()))
      errors.validateCuaa(nuovaDomanda.getCuaa(), "cuaa"); 
    
    errors.validateFieldMaxLength(nuovaDomanda.getDenominazione(), "denominazione", 1000);
    errors.validateOptionalFieldLength(nuovaDomanda.getPartitaIva(), "partitaIva", 11,11);

    
    if((nuovaDomanda.getCuaa()==null || "".equals(nuovaDomanda.getCuaa())) && 
        (nuovaDomanda.getPartitaIva()==null || "".equals(nuovaDomanda.getPartitaIva())) &&
        (nuovaDomanda.getDenominazione()==null || "".equals(nuovaDomanda.getDenominazione())))
    {
      errors.addError("cuaa", "Valorizzare almeno un campo");
      errors.addError("partitaIva", "Valorizzare almeno un campo");
      errors.addError("denominazione", "Valorizzare almeno un campo");
    }
    
    if (!errors.isEmpty())
    {
      model.addAttribute("bando", bandoSelezionato);
      model.addAttribute("errors", errors);
      return "nuovoprocedimento/dettaglioBando";
    }
    else
    {
      session.setAttribute(SESSION_CUAA_RICERCA_SINGOLA,
          nuovaDomanda.getCuaa());
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      List<Long> vIdAziende = nuovoProcedimento.getAziendeByCUAA(nuovaDomanda.getCuaa(), nuovaDomanda.getPartitaIva(), nuovaDomanda.getDenominazione(), utenteAbilitazioni);     
      return visualizzaRisultatiRicercaBandoSingolo(vIdAziende, nuovaDomanda.getIdBando(), model, session);
    
    }
  }

  @RequestMapping(value = "ricercaBandoMultipla", method = RequestMethod.GET)
  public String ricercaBandoMultiplaGet(Model model, HttpSession session)
      throws InternalUnexpectedException, ParseException
  {
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    NuovaDomanda nuovaDomanda = new NuovaDomanda();
    nuovaDomanda.setIdBando(bandoSelezionato.getIdBando());
    nuovaDomanda.setIdBandoOggetto(bandoSelezionato.getIdBandoOggetto());
    nuovaDomanda
        .setIdLegameGruppoOggetto(bandoSelezionato.getIdLegameGruppoOggetto());
    return ricercaBandoMultipla(nuovaDomanda, model, session);
  }

  @RequestMapping(value = "ricercaBandoMultipla", method = RequestMethod.POST)
  public String ricercaBandoMultipla(@ModelAttribute NuovaDomanda nuovaDomanda,
      Model model, HttpSession session)
      throws InternalUnexpectedException, ParseException
  {
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    model.addAttribute("cuaa", nuovaDomanda.getCuaa());
    model.addAttribute("descrizioneFiltro",
        bandoSelezionato.getDescrizioneFiltro());
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    List<AziendaDTO> elencoAziende = nuovoProcedimento
        .getDettaglioAziendeByIdBando(nuovaDomanda.getIdBando(),
            utenteAbilitazioni, getProcedimentoAgricoloFromSession(session));
    return visualizzaDettaglioRisultati(elencoAziende,
        nuovaDomanda.getIdBando(), model, session);
  }

  private String visualizzaDettaglioRisultati(List<AziendaDTO> elencoAziende,
      long idBando, Model model, HttpSession session)
      throws InternalUnexpectedException, ParseException
  {
    session
        .removeAttribute(IuffiConstants.GENERIC.SESSION_NO_ELENCO_AZIENDA);
    if (elencoAziende == null || elencoAziende.size() <= 0)
    {
      model.addAttribute("msgErrore",
          IuffiConstants.GENERIC_ERRORS.AZIENDE_NON_TROVATE);
      return "nuovoprocedimento/dettaglioBando";
    }
    else
      if (elencoAziende.size() == 1)
      {
        session.setAttribute(
            IuffiConstants.GENERIC.SESSION_NO_ELENCO_AZIENDA, Boolean.TRUE);
        // ho solo un azienda quindi non mostro l'elenco e vado diretto alla
        // pagina successiva
        AziendaDTO azienda = elencoAziende.get(0);

        if (azienda.getIdStatoOggetto() >= 10
            && azienda.getIdStatoOggetto() <= 90)
        {
          BandoDTO bandoDto = nuovoProcedimento.getInformazioniBando(idBando);

          if (IuffiConstants.FLAGS.SI
              .equals(bandoDto.getFlagDomandaMultipla()))
          {
            // SE FLAG_DOMANDA_MULTIPLA VALE S ED HO TROVATO SOLO UNA DOMANDA
            // TRASMESSA; ALLORA CHIEDO SE SI VUOLE CREARE NUOVO PROCEDIMENTO
            model.addAttribute("idAziendaSel", azienda.getIdAzienda());
            model.addAttribute("msgConfermaNuovoProcedimentoAdUnoEsistente",
                "S");
            return "nuovoprocedimento/dettaglioBando";
          }
          else
          {
            model.addAttribute("msgErrore",
                IuffiConstants.GENERIC_ERRORS.PROCEDIMENTO_TRASMESSO);
            return "nuovoprocedimento/dettaglioBando";
          }
        }

        if (!azienda.isProcedimentoEsistente())
        {
          model.addAttribute("idAziendaSel", azienda.getIdAzienda());
          model.addAttribute("msgConfermaNuovoProcedimento", "S");
          return "nuovoprocedimento/dettaglioBando";
        }
        else
        {
          return "redirect:../procedimento/visualizza_procedimento_"
              + azienda.getIdProcediemnto() + ".do";
        }
      }

    // mostro elenco aziende
    BandoDTO bandoDto = nuovoProcedimento.getInformazioniBando(idBando);
    model.addAttribute("denominazionebando", bandoDto.getDenominazione());
    return "nuovoprocedimento/elencoAziende";
  }

  private String visualizzaRisultatiRicercaBandoSingolo(List<Long> vIdAziende,
      long idBando, Model model, HttpSession session)
      throws InternalUnexpectedException, ParseException
  {
   UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
   session
        .removeAttribute(IuffiConstants.GENERIC.SESSION_NO_ELENCO_AZIENDA);
    if (vIdAziende == null || vIdAziende.size() <= 0)
    {
      model.addAttribute("msgErrore",
          IuffiConstants.GENERIC_ERRORS.AZIENDE_NON_TROVATE);
      return "nuovoprocedimento/dettaglioBando";
    }
    else
      if (vIdAziende.size() == 1)
      {
	String errore = checkAbilitazioniProfessionista(session, utenteAbilitazioni, vIdAziende.get(0).longValue(), idBando);
	if (errore != null) {
	    model.addAttribute("msgErrore", errore);
	    return "nuovoprocedimento/dettaglioBando";
	}
        session.setAttribute(
            IuffiConstants.GENERIC.SESSION_NO_ELENCO_AZIENDA, Boolean.TRUE);
        // ho solo un azienda quindi non mostro l'elenco e vado diretto alla
        // pagina successiva
        Vector<Long> vLong = new Vector<Long>();
        vLong.add(new Long(vIdAziende.get(0).longValue()));
        List<AziendaDTO> vAziende = nuovoProcedimento
            .getDettaglioAziendeById(vLong, idBando);
        boolean almenoUnaDomandaInBozza = false;
        BandoDTO bandoDto = nuovoProcedimento.getInformazioniBando(idBando);

        if (vIdAziende != null && vIdAziende.size() > 0)
        {
          List<Long> vIdAziendeL = new ArrayList<>();
          for (AziendaDTO az : vAziende)
          {
            vIdAziendeL.add(az.getIdAzienda());
            if (az.getIdStatoOggetto() < 10)
            {
              almenoUnaDomandaInBozza = true;
            }
          }
          if (almenoUnaDomandaInBozza && IuffiConstants.FLAGS.SI
              .equals(bandoDto.getFlagDomandaMultipla()))
          {
            session.setAttribute("vIdAziende", vIdAziendeL);
            model.addAttribute("denominazionebando",
                bandoDto.getDenominazione());
            return "nuovoprocedimento/elencoAziende";
          }
        }

        AziendaDTO azienda = vAziende.get(0);

        if (azienda.getIdStatoOggetto() >= 10
            && azienda.getIdStatoOggetto() <= 90)
        {
          if (IuffiConstants.FLAGS.SI
              .equals(bandoDto.getFlagDomandaMultipla()))
          {
            // SE FLAG_DOMANDA_MULTIPLA VALE S ED HO TROVATO SOLO UNA DOMANDA
            // TRASMESSA; ALLORA CHIEDO SE SI VUOLE CREARE NUOVO PROCEDIMENTO
            model.addAttribute("idAziendaSel", vIdAziende.get(0).longValue());
            model.addAttribute("msgConfermaNuovoProcedimentoAdUnoEsistente",
                "S");
            return "nuovoprocedimento/dettaglioBando";
          }
          else
          {
            model.addAttribute("msgErrore",
                IuffiConstants.GENERIC_ERRORS.PROCEDIMENTO_TRASMESSO);
            return "nuovoprocedimento/dettaglioBando";
          }
        }

        if (!azienda.isProcedimentoEsistente())
        {
          model.addAttribute("idAziendaSel", vIdAziende.get(0).longValue());
          model.addAttribute("msgConfermaNuovoProcedimento", "S");
          
          String err = checkAbilitazioniProfessionista(session, utenteAbilitazioni, vIdAziende.get(0).longValue(), idBando);
          if (err != null) {
              model.addAttribute("errore", err);
              return "dialog/soloErrore";
          }
  	
          return "nuovoprocedimento/dettaglioBando";

        }
        else
        {
          return "redirect:../procedimento/visualizza_procedimento_"
              + azienda.getIdProcediemnto() + ".do";
        }
      }

    // mostro elenco aziende
    session.setAttribute("vIdAziende", vIdAziende);
    BandoDTO bandoDto = nuovoProcedimento.getInformazioniBando(idBando);
    model.addAttribute("denominazionebando", bandoDto.getDenominazione());
    return "nuovoprocedimento/elencoAziende";
  }
  
  private String checkAbilitazioniProfessionista(HttpSession session, UtenteAbilitazioni utenteAbilitazioni, Long idAzienda, Long idBando) throws InternalUnexpectedException {
      if (utenteAbilitazioni.getRuolo().getIsList() != null && Arrays.asList(utenteAbilitazioni.getRuolo().getIsList()).contains("isUtenteProfessionista")) {
	  HashMap<Long, List<Long>> var = ricercaEJB.getAziendeBandiProfessionista(utenteAbilitazioni, getProcedimentoAgricoloFromSession(session));
	  if (var == null || !var.containsKey(idAzienda)) return "Professionista non abilitato a lavorare sui procedimenti dell'azienda selezionata.";
	  if (var != null && var.containsKey(idAzienda) && idBando != null) {
	      List<Long> idBandi = var.get(idAzienda);
	      if (idBandi != null && !idBandi.contains(idBando)) return "Professionista non abilitato a lavorare sui procedimenti dell'azienda selezionata relativi al bando corrente";
	  }

      }
      return null;
  }

  @RequestMapping(value = "getElencoAziende", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoAziende(HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<Map<String, Object>> aziende = new ArrayList<Map<String, Object>>();
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);

    @SuppressWarnings("unchecked")
    List<Long> vIdAziende = (List<Long>) session.getAttribute("vIdAziende");

    List<AziendaDTO> elencoAziende = null;
    if (vIdAziende == null || vIdAziende.size() <= 0)
    {
      elencoAziende = nuovoProcedimento.getDettaglioAziendeByIdBando(
          bandoSelezionato.getIdBando(), utenteAbilitazioni, getProcedimentoAgricoloFromSession(session));
    }
    else
    {
      elencoAziende = nuovoProcedimento.getDettaglioAziendeById(
          new Vector<Long>(vIdAziende), bandoSelezionato.getIdBando());
    }

    if (elencoAziende != null && elencoAziende.size() > 0)
    {
      long idAziendaLast = 0;
      long idAzienda = elencoAziende.get(0).getIdAzienda();
      for (int i = 0; i < elencoAziende.size(); i++)
      {
        AziendaDTO item = elencoAziende.get(i);
        idAziendaLast = item.getIdAzienda();

        if (idAziendaLast != idAzienda)
        {
          idAzienda = idAziendaLast;
          if ("S".equalsIgnoreCase(elencoAziende.get(i - 1).getProcesistente())
              && IuffiConstants.FLAGS.SI
                  .equals(bandoSelezionato.getFlagDomandaMultipla()))
          {
            writeRow(elencoAziende.get(i - 1), aziende, bandoSelezionato,
                session, request, true);
          }
        }

        if (item.getIdStatoOggetto() < 10 || item.getIdStatoOggetto() > 90)
        {
          writeRow(item, aziende, bandoSelezionato, session, request, false);
        }
      }

      if ("S".equalsIgnoreCase(
          elencoAziende.get(elencoAziende.size() - 1).getProcesistente())
          && IuffiConstants.FLAGS.SI
              .equals(bandoSelezionato.getFlagDomandaMultipla()))
      {
        writeRow(elencoAziende.get(elencoAziende.size() - 1), aziende,
            bandoSelezionato, session, request, true);
      }
    }

    return aziende;
  }

  private void writeRow(AziendaDTO item, List<Map<String, Object>> aziende,
      BandoDTO bandoSelezionato, HttpSession session,
      HttpServletRequest request, boolean forzaNuovo)
      throws InternalUnexpectedException
  {
    String eliminaOggetto = "";
    HashMap<String, Object> azienda = new HashMap<String, Object>();
    azienda.put("cuaa", item.getCuaa());
    azienda.put("partitaIva", item.getPartitaIva());
    azienda.put("denominazione", item.getDenominazione());
    azienda.put("denominazioneIntestazione",
        item.getDenominazioneIntestazione());
    azienda.put("sedeLegale", item.getSedeLegale());
    azienda.put("dataTrasmissione", item.getDataTrasmissione());
    azienda.put("descrComune", item.getDescrComune());
    azienda.put("descrProvincia", item.getDescrProvincia());
    azienda.put("procesistente", item.getProcesistente());
    azienda.put("identificativo", item.getIdentificativo());
    azienda.put("richiedenteDescr", item.getRichiedenteDescr());
    azienda.put("descrStatoOggetto", item.getDescrStatoOggetto());

    if (forzaNuovo)
    {
      azienda.remove("identificativo");
      azienda.remove("descrStatoOggetto");
      azienda.put("identificativo", "");
      azienda.put("richiedenteDescr", "");
      azienda.put("descrStatoOggetto", "");
      azienda.put("azione", "<a href=\"" + item.getAzioneHref()
          + "\" onClick=\"return confermaNuovoProcedimentoAdUnoEsistente('"
          + item.getIdAzienda()
          + "');\" style=\"text-decoration: none;\"><i class=\"icon-circle-arrow-right icon-large\" title=\"Crea nuovo procedimento per questa azienda\"></i></a>");
    }
    else
      if ("N".equalsIgnoreCase(item.getProcesistente()))
      {
        azienda.put("richiedenteDescr", "");
        azienda.put("azione",
            "<a href=\"" + item.getAzioneHref()
                + "\" onClick=\"return creaProcedimento('" + item.getIdAzienda()
                + "');\" style=\"text-decoration: none;\"><i class=\""
                + item.getAzione() + "\" title=\"" + item.getTitleHref()
                + "\"></i></a>");
      }
      else
      {
        final UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(
            session);
        List<GruppoOggettoDTO> listGruppiOggetto = ricercaEJB.getElencoOggetti(
            item.getIdProcediemnto(),
            Arrays.asList(utenteAbilitazioni.getMacroCU()),
            IuffiUtils.PAPUASERV
                .isAttoreBeneficiarioOCAA(utenteAbilitazioni), utenteAbilitazioni.getIdProcedimento());
        if (listGruppiOggetto != null && listGruppiOggetto.size() > 0)
        {
          for (GruppoOggettoDTO gruppo : listGruppiOggetto)
          {
            for (OggettoDTO oggetto : gruppo.getOggetti())
            {
              if (oggetto.getDataFine() == null
                  && oggetto.getFlagIstanza().equals("S"))
              {
                if (securityManager.isAccessoAutorizzatoForCU(request,
                    "CU-IUFFI-137", IuffiSecurity.Controllo.NESSUNO))
                {
                  eliminaOggetto = "<a href=\"elimina_istanza.do\"  "
                      + " 	onclick=\"return loadProcedimentoAndOpenPageInPopup('"
                      + item.getIdProcediemnto()
                      + "', '../cuiuffi137/popupindex_R_"
                      + item.getIdProcediemnto() + "_"
                      + oggetto.getIdProcedimentoOggetto() + "_"
                      + oggetto.getIdBandoOggetto()
                      + ".do','dlgElimina','Elimina Istanza','modal-lg',false)\"  "
                      + "     style=\"text-decoration: none;\"><i class=\"ico24 ico_trash\" title=\"Elimina Istanza\"></i></a>  ";
                }
              }
              else
                if (oggetto.getDataFine() == null
                    && oggetto.getFlagIstanza().equals("N"))
                {
                  if (securityManager.isAccessoAutorizzatoForCU(request,
                      "CU-IUFFI-138", IuffiSecurity.Controllo.NESSUNO))
                  {
                    eliminaOggetto = "<a href=\"elimina_oggetto.do\"  "
                        + " 	onclick=\"return loadProcedimentoAndOpenPageInPopup('"
                        + item.getIdProcediemnto()
                        + "', '../cuiuffi138/popupindex_R_"
                        + item.getIdProcediemnto() + "_"
                        + oggetto.getIdProcedimentoOggetto() + "_"
                        + oggetto.getIdBandoOggetto()
                        + ".do','dlgElimina','Elimina Oggetto','modal-lg',false)\"  "
                        + "     style=\"text-decoration: none;\"><i class=\"ico24 ico_trash\" title=\"Elimina Oggetto\"></i></a>  ";
                  }
                }
            }
          }
        }
        azienda.put("azione",
            "<a href=\"modificaProcedimentoAzienda_" + item.getIdAzienda() + "_"
                + item.getIdProcediemnto()
                + ".do\" style=\"text-decoration: none;\"><i class=\""
                + item.getAzione() + "\" title=\"" + item.getTitleHref()
                + "\"></i></a> " + eliminaOggetto);
      }

    aziende.add(azienda);
  }

  @RequestMapping(value = "creaProcedimento")
  public String creaProcedimento(@ModelAttribute NuovaDomanda nuovaDomanda,
      Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException, ApplicationException
  {
    return creaProcedimento(nuovaDomanda.getIdAzienda(),
        nuovaDomanda.getIdBando(), nuovaDomanda.getIdBandoOggetto(),
        nuovaDomanda.getIdLegameGruppoOggetto(), model, session, request.getParameter("note"));
  }

  @RequestMapping(value = "creaProcedimentoAdUnoEsistente")
  public String creaProcedimentoAdUnoEsistente(
      @ModelAttribute NuovaDomanda nuovaDomanda, Model model,
      HttpSession session, HttpServletRequest request) throws InternalUnexpectedException, ApplicationException
  {
    Vector<Long> vLong = new Vector<Long>();
    vLong.add(new Long(nuovaDomanda.getIdAzienda()));
    List<AziendaDTO> vAziende = nuovoProcedimento.getDettaglioAziendeById(vLong,
        nuovaDomanda.getIdBando());
    AziendaDTO azienda = vAziende.get(0);
    return completeCreaProcedimento(azienda, nuovaDomanda.getIdAzienda(),
        nuovaDomanda.getIdBando(), nuovaDomanda.getIdBandoOggetto(),
        nuovaDomanda.getIdLegameGruppoOggetto(), model, session, request.getParameter("note"));
  }

  @RequestMapping(value = "creaProcedimento_{idAzienda}", method = RequestMethod.POST)
  public String creaProcedimento(
      @PathVariable("idAzienda") @NumberFormat(style = NumberFormat.Style.NUMBER) int idAzienda,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    model.addAttribute("idAzienda", idAzienda);
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());
    model.addAttribute("msgAttesaNuovoProcedimento", "true");
    return "nuovoprocedimento/creaNuovo";
  }

  @RequestMapping(value = "creaProcedimentoAdUnoEsistente_{idAzienda}", method = RequestMethod.POST)
  public String creaProcedimentoAdUnoEsistente(
      @PathVariable("idAzienda") @NumberFormat(style = NumberFormat.Style.NUMBER) int idAzienda,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    model.addAttribute("idAzienda", idAzienda);
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());
    model.addAttribute("msgAttesaNuovoProcedimentoAdUnoEsistente", "true");
    return "nuovoprocedimento/creaNuovo";
  }

  @RequestMapping(value = "/confermaNuovoProcedimentoAdUnoEsistente_{idAzienda}_{idBando}", method = RequestMethod.GET)
  @IsPopup
  public String confermaNuovoProcedimentoAdUnoEsistente(Model model, HttpServletRequest request,
	  @PathVariable("idAzienda") Long idAzienda, @PathVariable("idBando") Long idBando, HttpSession session)
		  throws InternalUnexpectedException {
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      String errore = checkAbilitazioniProfessionista(session, utenteAbilitazioni, idAzienda, idBando);
      if (errore != null) {
	  model.addAttribute("errore", errore);
	  return "dialog/soloErrore";
      }

      setModelDialogWarning(model, "Creazione Nuovo Procedimento",
	      "Sono presenti una o più domande per il bando selezionato e stai cercando di creare un nuovo procedimento per l'azienda selezionata, vuoi continuare ?",
	      "creaProcedimentoAdUnoEsistente_" + idAzienda + ".do");
      return "dialog/conferma";
  }

  @RequestMapping(value = "/confermaNuovoProcedimento_{idAzienda}_{idBando}", method = RequestMethod.GET)
  @IsPopup
  public String confermaNuovoProcedimento(Model model, HttpServletRequest request,
	  @PathVariable("idAzienda") Long idAzienda, @PathVariable("idBando") Long idBando, HttpSession session) throws InternalUnexpectedException {
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      String errore = checkAbilitazioniProfessionista(session, utenteAbilitazioni, idAzienda, idBando);
      if (errore != null) {
	  model.addAttribute("errore", errore);
	  return "dialog/soloErrore";
      }
      setModelDialogWarning(model, "Creazione Nuovo Procedimento",
	      "Stai cercando di creare un nuovo procedimento per l'azienda selezionata, vuoi continuare ?",
	      "creaProcedimento_" + idAzienda + ".do");
      return "dialog/conferma";
  }

  @RequestMapping(value = "modificaProcedimento_{idAzienda}")
  public String modificaProcedimento(
      @PathVariable("idAzienda") @NumberFormat(style = NumberFormat.Style.NUMBER) int idAzienda,
      Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException, ApplicationException
  {
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    model.addAttribute("idAzienda", idAzienda);
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());

    return creaProcedimento(idAzienda, bandoSelezionato.getIdBando(),
        bandoSelezionato.getIdBandoOggetto(),
        bandoSelezionato.getIdLegameGruppoOggetto(), model, session, request.getParameter("note"));
  }

  @RequestMapping(value = "modificaProcedimentoAzienda_{idAzienda}_{idProcedimento}", method = RequestMethod.GET)
  public String modificaProcedimentoAzienda(
      @PathVariable(value = "idAzienda") int idAzienda,
      @PathVariable(value = "idProcedimento") int idProcedimento,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    model.addAttribute("idAzienda", idAzienda);
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());

    return "redirect:../procedimento/visualizza_procedimento_" + idProcedimento
        + ".do";
  }

  @RequestMapping(value = "download_{idAllegatiBando}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> download(
      @PathVariable("idAllegatiBando") long idAllegatiBando,
      HttpSession session) throws IOException, InternalUnexpectedException
  {
    FileAllegatoDTO allegato = nuovoProcedimento
        .getFileAllegato(idAllegatiBando);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        IuffiUtils.FILE.getMimeType(allegato.getNomeFile()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + allegato.getNomeFile() + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        allegato.getFileAllegato(), httpHeaders, HttpStatus.OK);
    return response;
  }

  private String creaProcedimento(long idAzienda, long idBando,
      long idBandoOggetto, long idLegameGruppoOggetto, Model model,
      HttpSession session, String note) throws InternalUnexpectedException, ApplicationException
  {
    Vector<Long> vLong = new Vector<Long>();
    vLong.add(new Long(idAzienda));

    List<AziendaDTO> vAziende = nuovoProcedimento.getDettaglioAziendeById(vLong,
        idBando);
    AziendaDTO azienda = vAziende.get(0);
    if (!azienda.isProcedimentoEsistente())
    {
      return completeCreaProcedimento(azienda, idAzienda, idBando,
          idBandoOggetto, idLegameGruppoOggetto, model, session, note);
    }
    else
    {
      return "redirect:../procedimento/visualizza_procedimento_"
          + azienda.getIdProcediemnto() + ".do";
    }
  }

  private String completeCreaProcedimento(AziendaDTO azienda, long idAzienda, long idBando, long idBandoOggetto,
	  long idLegameGruppoOggetto, Model model, HttpSession session, String note)
		  throws InternalUnexpectedException, ApplicationException {
      // in questo caso effettuo tutte le chiamate per creare il procedimento
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
      if (utenteAbilitazioni.getRuolo().getIsList() != null
	      && Arrays.asList(utenteAbilitazioni.getRuolo().getIsList()).contains("isUtenteProfessionista")) {
	  HashMap<Long, List<Long>> var = ricercaEJB.getAziendeBandiProfessionista(utenteAbilitazioni, getProcedimentoAgricoloFromSession(session));
	  if (var == null || !var.containsKey(idAzienda)) {
	      throw new ApplicationException("Professionista non abilitato a lavorare sui procedimenti dell'azienda selezionata.");
	  } else {
	      List<Long> idBandi = var.get(idAzienda);
	      if (!idBandi.contains(idBando))
		  throw new ApplicationException("Professionista non abilitato a lavorare sui procedimenti del bando selezionato.");
	  }
      }
      
      aggiornaDatiAAEPSian(azienda, utenteAbilitazioni);

      BandoDTO bando = nuovoProcedimento.getInformazioniBando(idBando);
      model.addAttribute("bando", bando);
      model.addAttribute("azienda", azienda);

      // chiamo callMainControlliGravi
      MainControlloDTO controlliGravi = nuovoProcedimento.callMainControlliGravi(idBandoOggetto, null, idAzienda,
	      utenteAbilitazioni.getIdUtenteLogin(), null);
      if (controlliGravi.getRisultato() == IuffiConstants.SQL.RESULT_CODE.ERRORE_CRITICO) {
	  model.addAttribute("msgErrore", IuffiConstants.SQL.MESSAGE.PLSQL_ERRORE_CRITICO + " "
		  + IuffiUtils.STRING.safeHTMLText(controlliGravi.getMessaggio()));
	  return "nuovoprocedimento/creaNuovo";
      } else if (controlliGravi.getRisultato() == IuffiConstants.SQL.RESULT_CODE.ERRORE_GRAVE) {
	  model.addAttribute("controlliGravi", controlliGravi);
	  return "nuovoprocedimento/creaNuovo";
      }

      // Verifico se il bando rientra nella gestione 'BANDO_MIS_16_AZ_1'
      Map<String, String> mapParametri = nuovoProcedimento
	      .getParametri(new String[] { IuffiConstants.PARAMETRO.BANDO_MIS_16_AZ_1 });
      if (mapParametri != null && mapParametri.containsKey(IuffiConstants.PARAMETRO.BANDO_MIS_16_AZ_1)) {
	  if (mapParametri.get(IuffiConstants.PARAMETRO.BANDO_MIS_16_AZ_1).indexOf(idBando + "-") >= 0) {
	      // a questo punto è necessario chiedere su quale procedimento del bando
	      // precedente si vuole creare questo nuovo
	      String[] bandiDaGestire = mapParametri.get(IuffiConstants.PARAMETRO.BANDO_MIS_16_AZ_1).split("#");
	      for (String coppia : bandiDaGestire) {
		  if (coppia.indexOf(idBando + "-") >= 0) {
		      long idBandoRiferimentoVecchio = Long.parseLong(coppia.split("-")[1]);
		      List<Procedimento> procedimenti = nuovoProcedimento
			      .searchProcedimentiBandoEsistente(idBandoRiferimentoVecchio, idAzienda);
		      if (procedimenti == null || procedimenti.isEmpty()) {
			  model.addAttribute("msgErrore",
				  "Attenzione: impossibile procedere con la creazione, domanda su bando di Azione 1 non trovata o non nello stato corretto.");
			  return "nuovoprocedimento/creaNuovo";
		      } else {
			  model.addAttribute("elencoProcedimenti", procedimenti);
			  model.addAttribute("idAzienda", idAzienda);
			  model.addAttribute("cuaa", azienda.getCuaa());
			  model.addAttribute("denominazioneBandovecchio", nuovoProcedimento
				  .getInformazioniBando(idBandoRiferimentoVecchio).getDenominazione());
			  model.addAttribute("idBandoRiferimentoVecchio", idBandoRiferimentoVecchio);
			  return "nuovoprocedimento/scegliProcedimentoVecchio";
		      }
		  }
	      }
	  }
      }

      // BandoDTO bando = nuovoProcedimento.getInformazioniBando(idBando);
      // SE bando misura a premio chiedo se vuole generare bando grafico o no
      if (bando.getCodiceTipoBando().compareTo(IuffiConstants.TIPO_BANDO.PREMIO) == 0) {
	  long idProcedimentoAgricolo = getUtenteAbilitazioni(session).getIdProcedimento();
	  if (nuovoProcedimento.callIsValidazioneGrafica(idAzienda, bando.getAnnoCampagna(),
		  idProcedimentoAgricolo)) {
	      model.addAttribute("idAzienda", idAzienda);
	      return "nuovoprocedimento/scegliTipoDomandaGrafica";
	  }
      }

      return richiamaPLCreazioneProcedimento(idAzienda, idBando, idBandoOggetto, idLegameGruppoOggetto, Boolean.FALSE,
	      model, session, note);
  }

  @RequestMapping(value = "proseguiprocedimento_{idAzienda}_{idProcedimento}_{idBando}")
  public String proseguiprocedimento(
      @PathVariable("idAzienda") @NumberFormat(style = NumberFormat.Style.NUMBER) int idAzienda,
      @PathVariable("idProcedimento") @NumberFormat(style = NumberFormat.Style.NUMBER) int idProcedimentoVecchio,
      @PathVariable("idBando") @NumberFormat(style = NumberFormat.Style.NUMBER) long idBandoVecchio,
      Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    model.addAttribute("idAzienda", idAzienda);
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());

    nuovoProcedimento.inserisciProcDomandaPrec(idProcedimentoVecchio, idAzienda,
        idBandoVecchio);
    return richiamaPLCreazioneProcedimento(idAzienda,
        bandoSelezionato.getIdBando(), bandoSelezionato.getIdBandoOggetto(),
        bandoSelezionato.getIdLegameGruppoOggetto(), Boolean.FALSE, model,
        session, request.getParameter("note"));
  }

  @RequestMapping(value = "proseguitipodomanda_{tipodomanda}_{idAzienda}")
  public String proseguiprocedimento(
      @PathVariable("tipodomanda") String tipodomanda,
      @PathVariable("idAzienda") @NumberFormat(style = NumberFormat.Style.NUMBER) int idAzienda,
      Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    model.addAttribute("idAzienda", idAzienda);
    model.addAttribute("idBando", bandoSelezionato.getIdBando());
    model.addAttribute("idBandoOggetto", bandoSelezionato.getIdBandoOggetto());
    model.addAttribute("idLegameGruppoOggetto",
        bandoSelezionato.getIdLegameGruppoOggetto());
    String note = request.getParameter("note");
    model.addAttribute("note", note);

    return richiamaPLCreazioneProcedimento(idAzienda,
        bandoSelezionato.getIdBando(), bandoSelezionato.getIdBandoOggetto(),
        bandoSelezionato.getIdLegameGruppoOggetto(), ("A").equals(tipodomanda),
        model, session, note);
  }

  private String richiamaPLCreazioneProcedimento(long idAzienda, long idBando,
      long idBandoOggetto, long idLegameGruppoOggetto,
      boolean forzaCreazioneAlfanumerico,
      Model model, HttpSession session, String note) throws InternalUnexpectedException
  {
    // chiamo il pl che crea il procedimento
    // IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte()
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    MainControlloDTO controlliGravi = nuovoProcedimento.callMainCreazione(
        idBando, idLegameGruppoOggetto, null, idAzienda,
        utenteAbilitazioni.getIdUtenteLogin(),
        IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni), null,
        forzaCreazioneAlfanumerico, note);
    if (controlliGravi
        .getRisultato() == IuffiConstants.SQL.RESULT_CODE.ERRORE_CRITICO)
    {
      model.addAttribute("msgErrore",
          IuffiConstants.SQL.MESSAGE.PLSQL_ERRORE_CRITICO + " "
              + IuffiUtils.STRING
                  .safeHTMLText(controlliGravi.getMessaggio()));
      return "nuovoprocedimento/creaNuovo";
    }
    else
      if (controlliGravi
          .getRisultato() == IuffiConstants.SQL.RESULT_CODE.NESSUN_ERRORE)
      {
        return "redirect:../procedimento/visualizza_procedimento_"
            + controlliGravi.getIdProcedimento().intValue() + ".do";
      }
      else
        if (controlliGravi
            .getRisultato() == IuffiConstants.SQL.RESULT_CODE.ERRORE_GRAVE)
        {
          model.addAttribute("msgErrore",
              IuffiConstants.SQL.MESSAGE.PLSQL_ERRORE_GRAVE + " "
                  + IuffiUtils.STRING
                      .safeHTMLText(controlliGravi.getMessaggio()));
          return "nuovoprocedimento/creaNuovo";
        }

    return "";
  }

  @RequestMapping(value = "getElencoAmministrazioniCompetenzaJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoAmministrazioniCompetenzaJson(
      Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
	int idProcedimentoAgricolo = getUtenteAbilitazioni(session).getIdProcedimento();
    List<BandoDTO> elencoBandi = nuovoProcedimento.getElencoBandiAttivi(idProcedimentoAgricolo, getUtenteAbilitazioni(session));

    Map<String, Object> stato;
    List<String> valList = new ArrayList<String>();
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (BandoDTO item : elencoBandi)
    {
      item.setAmministrazioniCompetenza(
          nuovoProcedimento.getAmmCompetenzaAssociate(item.getIdBando()));

      for (AmmCompetenzaDTO a : item.getAmministrazioniCompetenza())
      {
        if (!valList.contains(a.getDescrizione()))
        {
          stato = new HashMap<String, Object>();
          stato.put("id", a.getIdAmmCompetenza());
          stato.put("label", a.getDescrizione());
          ret.add(stato);
          valList.add(a.getDescrizione());
        }
      }
    }

    return ret;
  }

  @RequestMapping(value = "getElencoStatiProcedimenti", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoStatiProcedimenti(
      HttpSession session) throws InternalUnexpectedException
  {
    List<Map<String, Object>> procedimenti = new ArrayList<Map<String, Object>>();
    HashMap<String, Object> stato = null;
    Vector<String> vId = new Vector<String>();

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");

    BandoDTO bandoSelezionato = (BandoDTO) session
        .getAttribute(SESSION_BANDO_SELEZIONATO);
    List<AziendaDTO> elencoAziende = nuovoProcedimento
        .getDettaglioAziendeByIdBando(bandoSelezionato.getIdBando(),
            utenteAbilitazioni, getProcedimentoAgricoloFromSession(session));

    if (elencoAziende != null && elencoAziende.size() > 0)
    {
      for (AziendaDTO item : elencoAziende)
      {
        if (item.getIdStatoOggetto() < 10 || item.getIdStatoOggetto() > 90)
        {
          if (item.getDescrStatoOggetto() != null
              && !vId.contains(item.getDescrStatoOggetto()))
          {
            vId.add(item.getDescrStatoOggetto());
            stato = new HashMap<String, Object>();
            stato.put("label", item.getDescrStatoOggetto());
            stato.put("id", item.getDescrStatoOggetto());
            procedimenti.add(stato);
          }
        }
      }
    }

    return procedimenti;
  }

  public HashMap<String, FiltroVO> parseFilters(String json)
      throws JsonProcessingException, IOException
  {
    HashMap<String, FiltroVO> filtersMap = new HashMap<String, FiltroVO>();
    JsonFactory factory = new JsonFactory();
    ObjectMapper mapper = new ObjectMapper(factory);
    JsonNode rootNode = mapper.readTree(json);

    Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.getFields();
    Iterator<Map.Entry<String, JsonNode>> valuesIterator;
    while (fieldsIterator.hasNext())
    {
      Map.Entry<String, JsonNode> field = fieldsIterator.next();
      filtersMap.put(field.getKey(), new FiltroVO());
      filtersMap.get(field.getKey()).setValues(new ArrayList<Long>());
      filtersMap.get(field.getKey()).setStrValues(new ArrayList<String>());
      valuesIterator = field.getValue().getFields();
      while (valuesIterator.hasNext())
      {
        Map.Entry<String, JsonNode> value = valuesIterator.next();

        // Caso di flitri del tipo checkbox a scelta multipla
        if (value.getValue().isArray()
            && field.getKey().compareTo("dataScadenzaStr") != 0)
        {
          for (final JsonNode objNode : value.getValue())
          {
            if (objNode.isTextual() && isSearchFilter(objNode.asText()))
            {
              populateFilter(filtersMap.get(field.getKey()), objNode.asText(),
                  null);
            }
            else
            {
              if (objNode.isTextual())
              {
                List<String> ss = filtersMap.get(field.getKey()).getStrValues();
                ss.add(objNode.toString());
                // filtersMap.get(field.getKey()).getStrValues().add(objNode.toString());
              }
              else
                filtersMap.get(field.getKey()).getValues()
                    .add(objNode.asLong());
            }
          }
        }
        else
          if (isSearchFilter(value.getKey()))
          {
            populateFilter(filtersMap.get(field.getKey()), value.getKey(),
                value.getValue());
          }
      }
    }
    return filtersMap;
  }

  private boolean isSearchFilter(String key)
  {

    if (IuffiConstants.FILTRI.CONTIENE.equals(key)
        || IuffiConstants.FILTRI.DIVERSO_DA.equals(key)
        || IuffiConstants.FILTRI.NON_CONTIENE.equals(key)
        || IuffiConstants.FILTRI.NON_VUOTO.equals(key)
        || IuffiConstants.FILTRI.UGUALE_A.equals(key)
        || IuffiConstants.FILTRI.VUOTO.equals(key))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  private void populateFilter(FiltroVO filtroVO, String key, JsonNode value)
  {
    if (value != null)
    {
      if (IuffiConstants.FILTRI.CONTIENE.equals(key))
      {
        filtroVO.setContiene(value.asText());
      }
      else
        if (IuffiConstants.FILTRI.DIVERSO_DA.equals(key))
        {
          filtroVO.setDiversoDa(value.asText());
        }
        else
          if (IuffiConstants.FILTRI.NON_CONTIENE.equals(key))
          {
            filtroVO.setNonContiene(value.asText());
          }
          else
            if (IuffiConstants.FILTRI.NON_VUOTO.equals(key))
            {
              filtroVO.setNonVuoto(true);
            }
            else
              if (IuffiConstants.FILTRI.UGUALE_A.equals(key))
              {
                filtroVO.setUgualeA(value.asText());
              }
              else
                if (IuffiConstants.FILTRI.VUOTO.equals(key))
                {
                  filtroVO.setVuoto(true);
                }
    }
  }

  @RequestMapping(value = "getElencoCodiciLivelliMisureJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliMisureJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
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

  @RequestMapping(value = "getElencoCodiciLivelliSottoMisureJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliSottoMisureJson(
      Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = ricercaEJB.getElencoLivelli();
    Map<String, Object> stato;
    List<String> valList = new ArrayList<String>();
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (LivelloDTO item : livelli)
    {
      if (!valList.contains(item.getCodiceSottoMisura()))
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

  @RequestMapping(value = "getElencoCodiciOperazioneJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciOperazioneJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
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

}

class SceltaBando
{
  private Long idBandoSelezionato;

  public Long getIdBandoSelezionato()
  {
    return idBandoSelezionato;
  }

  public void setIdBandoSelezionato(Long idBandoSelezionato)
  {
    this.idBandoSelezionato = idBandoSelezionato;
  }
}

class NuovaDomanda
{
  private String cuaa;
  private String partitaIva;
  private String denominazione;
  private long idBando;
  private long   idBandoOggetto;
  private long   idLegameGruppoOggetto;
  private long   idAzienda;

  public String getCuaa()
  {
    return ((cuaa != null) ? cuaa.trim() : cuaa);
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public Long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(Long idBando)
  {
    this.idBando = idBando;
  }

  public Long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public Long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(Long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public Long getIdLegameGruppoOggetto()
  {
    return idLegameGruppoOggetto;
  }

  public void setIdLegameGruppoOggetto(Long idLegameGruppoOggetto)
  {
    this.idLegameGruppoOggetto = idLegameGruppoOggetto;
  }
  public String getPartitaIva()
  {
    return partitaIva;
  }
  public void setPartitaIva(String partitaIva)
  {
    this.partitaIva = partitaIva;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
}
