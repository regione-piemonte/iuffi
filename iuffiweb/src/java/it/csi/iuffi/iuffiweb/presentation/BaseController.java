package it.csi.iuffi.iuffiweb.presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import it.csi.iuffi.iuffiweb.business.IIuffiAbstractEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.AmmCompetenza;
import it.csi.papua.papuaserv.dto.gestioneutenti.EnteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;
import it.csi.solmr.dto.anag.sian.SianUtenteVO;

@Controller
public class BaseController
{
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".presentation");

  protected TestataProcedimento getTestataProcedimento(HttpSession session)
  {
    return (TestataProcedimento) session
        .getAttribute(TestataProcedimento.SESSION_NAME);
  }

  protected void refreshTestataProcedimento(IQuadroEJB quadroEJB,
      HttpSession session, long idProcedimento)
      throws InternalUnexpectedException
  {
    TestataProcedimento testataProcedimento = quadroEJB
        .getTestataProcedimento(idProcedimento);
    session.setAttribute(TestataProcedimento.SESSION_NAME, testataProcedimento);
  }

  protected UtenteAbilitazioni getUtenteAbilitazioni(HttpSession session)
  {
    return (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
  }

  protected long getIdUtenteLogin(HttpSession session)
  {
    return getUtenteAbilitazioni(session).getIdUtenteLogin();
  }

  protected long getIdProcedimentoOggetto(HttpSession session)
      throws InternalUnexpectedException
  {
    return getProcedimentoOggettoFromSession(session)
        .getIdProcedimentoOggetto();
  }

  protected long getIdProcedimento(HttpSession session)
      throws InternalUnexpectedException
  {
    return getProcedimentoFromSession(session).getIdProcedimento();
  }

  protected List<UtenteLogin> loadRuoloDescr(List<Long> lIdUtente)
      throws InternalUnexpectedException
  {
    try
    {
      int size = lIdUtente.size();
      long[] aIdUtente = new long[size];
      for (int i = 0; i < size; ++i)
      {
        aIdUtente[i] = lIdUtente.get(i);
      }
      UtenteLogin[] utenti = PapuaservProfilazioneServiceFactory
          .getRestServiceClient().findUtentiLoginByIdList(aIdUtente);
      if (utenti != null && utenti.length > 0)
      {
        return Arrays.asList(utenti);
      }
      else
      {
        // Ritorno un array vuoto in modo che se il chiamante poi non fa il
        // controllo del null ma magri esegue un for non va in eccezione
        return new ArrayList<>();
      }

    }
    catch (Exception e)
    {
      throw new InternalUnexpectedException(e, new LogVariable[]
      { new LogVariable("lIdUtente", lIdUtente) });
    }
  }

  protected Map<Long, UtenteLogin> getMapUtenti(List<Long> lIdUtente)
      throws InternalUnexpectedException
  {
    Map<Long, UtenteLogin> map = new HashMap<Long, UtenteLogin>();
    if (lIdUtente != null && lIdUtente.size() > 0)
    {
      List<UtenteLogin> list = loadRuoloDescr(lIdUtente);
      if (list != null)
      {
        for (UtenteLogin utente : list)
        {
          map.put(utente.getIdUtenteLogin(), utente);
        }
      }
    }
    return map;
  }

  protected String getUtenteDescrizione(Long idUtente, List<UtenteLogin> utenti)
  {
    if (utenti != null)
    {
      for (UtenteLogin utente : utenti)
      {
        if (utente != null && utente.getIdUtenteLogin() != null
            && utente.getIdUtenteLogin().longValue() == idUtente.longValue())
        {
          StringBuilder sb = new StringBuilder();
          sb.append(utente.getCognome()).append(" ").append(utente.getNome());
          String denominazioneEnte = utente.getEnte().getDenominazioneEnte();
          if (denominazioneEnte != null)
          {
            sb.append(" (").append(denominazioneEnte).append(")");
          }
          return sb.toString();
        }
      }
    }
    return "";
  }

  protected void setModelDialogWarning(Model model, String messaggio)
  {
    setModelDialogWarning(model, null, messaggio, null);
  }

  protected void setModelDialogWarning(Model model, String messaggio,
      String action)
  {
    setModelDialogWarning(model, null, messaggio, action);
  }

  protected void setModelDialogWarningPopup(Model model, String messaggio,
      String action, String titolo, String idPopup)
  {
    model.addAttribute("titolo", titolo);
    model.addAttribute("idPopup", idPopup);
    setModelDialogWarning(model, null, messaggio, action);
  }

  protected void setModelDialogWarning(Model model, String messaggioAttesa,
      String messaggio, String action)
  {
    model.addAttribute("chiudi", "no, chiudi");
    model.addAttribute("prosegui", "si, prosegui");
    model.addAttribute("messaggio", messaggio);
    if (messaggioAttesa != null)
    {
      model.addAttribute("messaggioAttendere", messaggioAttesa);
    }
    else
    {
      model.addAttribute("messaggioAttendere",
          "Attendere prego, operazione in corso...");
    }
    if (action != null)
    {
      model.addAttribute("action", action);
    }
  }

  public void writePlainText(HttpServletResponse response, String text)
      throws IOException
  {
    response.setContentType("text/plain");
    response.getWriter().write(text);
  }

  protected Procedimento getProcedimentoFromSession(HttpSession session)
      throws InternalUnexpectedException
  {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest();
    return (Procedimento) getProcedimentoFromSession(request);
  }

  protected Procedimento getProcedimentoFromSession(HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return IuffiFactory.getProcedimento(request);
  }

  protected ProcedimentoOggetto getProcedimentoOggettoFromSession(
      HttpSession session) throws InternalUnexpectedException
  {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest();
    return getProcedimentoOggettoFromRequest(request);
  }

  protected ProcedimentoOggetto getProcedimentoOggettoFromRequest(
      HttpServletRequest request) throws InternalUnexpectedException
  {
    return IuffiFactory.getProcedimentoOggetto(request);
  }

  protected void aggiornaDatiAAEPSian(AziendaDTO azienda,
      UtenteAbilitazioni utenteAbilitazioni) throws InternalUnexpectedException
  {
    try
    {

      IuffiUtils.PORTADELEGATA.getAnagServiceCSIInterface()
          .serviceAggiornaDatiAAEP(azienda.getCuaa());
      serviceSianAggiornaDatiTributaria(azienda,
          IuffiUtils.PORTADELEGATA.getSianUtenteVO(utenteAbilitazioni));
    }
    catch (Exception e)
    {
      // Devo ignorare qualunque errore. Eventuali anomalie vengono proposte
      // dalla proceura PL seguente
    }
  }

  protected Date getDataValiditaProcOggetto(HttpSession session)
      throws InternalUnexpectedException
  {
    return getProcedimentoOggettoFromSession(session).getDataFine();
  }

  private void serviceSianAggiornaDatiTributaria(AziendaDTO azienda,
      SianUtenteVO sianUtenteVO)
  {
    try
    {
      IuffiUtils.PORTADELEGATA.getAnagServiceCSIInterface()
          .serviceSianAggiornaDatiTributaria(azienda.getCuaa(), sianUtenteVO);

      if (!IuffiConstants.ANAGRAFE.ID_FORMA_GIURIDICA_DITTA_INDIVIDUALE
          .equals(azienda.getIdFormaGiuridica())
          && !IuffiConstants.ANAGRAFE.ID_FORMA_GIURIDICA_PERS_FISICA_NON_ESERC_ATT_IMPRESA
              .equals(azienda.getIdFormaGiuridica()))
      {
        // I dati del titolare/rappresentante legale vengono utilizzati per
        // richiamare il servizio ServiceSianAggiornaDatiTributaria
        // solo se l'azienda legata alla pratica in questione ha forma giuridica
        // diversa da 1 (DITTA INDIVIDUALE) e da 52 (PERSONA FISICA CHE NON
        // ESERCITA
        // ATTIVITA' DI IMPRESA)
        it.csi.solmr.dto.anag.PersonaFisicaVO personaFisicaVOAnag = IuffiUtils.PORTADELEGATA
            .getAnagServiceCSIInterface()
            .serviceGetTitolareORappresentanteLegaleAzienda(
                azienda.getIdAzienda(), new Date(System.currentTimeMillis()));
        if (personaFisicaVOAnag != null && IuffiUtils.VALIDATION
            .isNotEmpty(personaFisicaVOAnag.getCodiceFiscale()))
        {
          // Il servizio viene richiamato una seconda volta passando come
          // parametro il codice fiscale del Titolare/Rappresentante legale
          IuffiUtils.PORTADELEGATA.getAnagServiceCSIInterface()
              .serviceSianAggiornaDatiTributaria(
                  personaFisicaVOAnag.getCodiceFiscale(), sianUtenteVO);
        }
      }
    }
    catch (Exception ex)
    {
      // Devo ignorare qualunque errore. Eventuali anomalie vengono proposte
      // dalla proceura PL seguente
    }
  }

  public String getDescUltimoAggiornamento(UtenteLogin utenteLogin,
      Date dataUltimoAggiornamento)
  {
    StringBuilder sb = new StringBuilder();
    boolean isDate = false;
    if (dataUltimoAggiornamento != null)
    {
      sb.append(IuffiUtils.DATE.formatDateTime(dataUltimoAggiornamento));
      isDate = true;
    }
    if (utenteLogin != null)
    {
      if (isDate)
      {
        sb.append(" - ");
      }
      sb.append(utenteLogin.getCognome()).append(" ")
          .append(utenteLogin.getNome());
      final String denominazioneEnte = utenteLogin.getEnte()
          .getDenominazioneEnte();
      if (!GenericValidator.isBlankOrNull(denominazioneEnte))
      {
        sb.append(" (").append(denominazioneEnte).append(")");
      }
    }
    return sb.toString();
  }

  public String returnErrorPopup(Model model, String messaggio)
  {
    model.addAttribute("messaggio", messaggio);
    return "errorePopup";
  }

  public Map<String, Object> getCommonFromSession(String id,
      HttpSession session, boolean removeIfDifferentOwner)
  {
    @SuppressWarnings("unchecked")
    Map<String, Object> common = (Map<String, Object>) session
        .getAttribute("common");
    if (common != null)
    {
      String ownerID = (String) common.get("ID");
      if (ownerID == null)
      {
        common = new HashMap<String, Object>();
        common.put("ID", id);
      }
      else
      {
        if (!ownerID.equals(id))
        {
          common = new HashMap<String, Object>();
          common.put("ID", id);
          if (removeIfDifferentOwner)
          {
            session.removeAttribute("common");
          }
        }
      }
    }
    else
    {
      common = new HashMap<String, Object>();
      common.put("ID", id);
    }
    return common;
  }

  public void saveCommonInSession(Map<String, Object> common,
      HttpSession session)
  {
    session.setAttribute("common", common);
  }

  public void clearCommonInSession(HttpSession session)
  {
    session.removeAttribute("common");
  }

  protected LogOperationOggettoQuadroDTO getLogOperationOggettoQuadroDTO(
      HttpSession session) throws InternalUnexpectedException
  {
    return getLogOperationOggettoQuadroDTO(
        this.getClass().getAnnotation(IuffiSecurity.class).value(), session);
  }

  protected LogOperationOggettoQuadroDTO getLogOperationOggettoQuadroDTO(
      String cuName, HttpSession session) throws InternalUnexpectedException
  {
    LogOperationOggettoQuadroDTO logOperation = new LogOperationOggettoQuadroDTO();
    logOperation.setExtIdUtenteAggiornamento(getIdUtenteLogin(session));
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    logOperation.setIdProcedimentoOggetto(po.getIdProcedimentoOggetto());
    logOperation.setIdBandoOggetto(po.getIdBandoOggetto());
    QuadroOggettoDTO quadro = po.findQuadroByCU(cuName);
    if (quadro == null)
    {
      logger.error("[" + this.getClass().getSimpleName()
          + ":getLogOperationOggettoQuadroDTO] Errore: Impossibile trovare il quadro con CU = "
          + cuName
          + ", NullPointerException in arrivo!", null);
    }
    logOperation.setIdQuadroOggetto(quadro.getIdQuadroOggetto());
    return logOperation;
  }

  protected String getUltimaModifica(IIuffiAbstractEJB quadroEJB,
      long idProcedimentoOggetto, long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    LogOperationOggettoQuadroDTO logObj = quadroEJB.getIdUtenteUltimoModifica(
        idProcedimentoOggetto, idQuadroOggetto, idBandoOggetto);
    if (logObj != null)
    {
      List<Long> idUtenti = new ArrayList<Long>();
      idUtenti.add(logObj.getExtIdUtenteAggiornamento());
      List<UtenteLogin> utentiList = loadRuoloDescr(idUtenti);
      return IuffiUtils.DATE
          .formatDateTime(logObj.getDataUltimoAggiornamento()) + " "
          + getUtenteDescrizione(logObj.getExtIdUtenteAggiornamento(),
              utentiList);
    }
    return null;
  }

  public QuadroOggettoDTO findQuadroCorrente(HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return findQuadroCorrente(getProcedimentoOggettoFromRequest(request));
  }

  public QuadroOggettoDTO findQuadroCorrente(HttpSession session)
      throws InternalUnexpectedException
  {
    return findQuadroCorrente(getProcedimentoOggettoFromSession(session));
  }

  public QuadroOggettoDTO findQuadroCorrente(ProcedimentoOggetto po)
  {
    return po.findQuadroByCU(
        this.getClass().getAnnotation(IuffiSecurity.class).value());
  }

  public List<Long> getListIdAmmCompetenza(HttpSession session)
  {
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    EnteLogin[] entiAbilitati = utenteAbilitazioni.getEntiAbilitati();
    List<Long> lIdAmmCompetenza = new ArrayList<Long>();
    for (EnteLogin ente : entiAbilitati)
    {
      AmmCompetenza ammCompetenza = ente.getAmmCompetenza();
      if (ammCompetenza != null)
      {
        lIdAmmCompetenza.add(ammCompetenza.getIdAmmCompetenza());
      }
    }
    return lIdAmmCompetenza;
  }

  public void cleanSession(HttpSession session)
  {
    // Rimuovo dalla sessione tutti gli oggetti 'pesanti' che sono stati
    // erroneamente inseriti in sessione nei vari quadri.
    /*
    session.removeAttribute("elencoFrazionaParticelleSession");
    session.removeAttribute("pascolo");
    session.removeAttribute("sessionVarAllevamenti");
    session.removeAttribute("RegistriDiStallaVO");
    session.removeAttribute("righeDati");
    session.removeAttribute("CU171Partecipante");
    session.removeAttribute("elencoComuniGAL");
    session.removeAttribute("checkboxMisureSelezionate");
    session.removeAttribute("riepilogo");
    session.removeAttribute("premioComplessivo");
    session.removeAttribute("allevamenti");
    session.removeAttribute("sanzioniSuperfici");
    session.removeAttribute("sanzioniAllevamenti");
    session.removeAttribute("listaDaVisualizzare");
    session.removeAttribute(FiltroRicercaConduzioni.class.getName());
    session.removeAttribute("danno");
    session.removeAttribute("conduzioni");
    */
    //
    session.removeAttribute("missioneRequest");               // S.D.
    session.removeAttribute("checkboxAllSpecieVegetali");     // S.D.
    session.removeAttribute("checkboxAllTipoAree");           // S.D.
    session.removeAttribute("checkboxAllIspettoriAssegnati"); // S.D.
    session.removeAttribute("checkboxAllIspettoriSecondari"); // S.D.
    session.removeAttribute("checkboxAllOrganismiNocivi");    // S.D.
    session.removeAttribute("checkboxAllTipoTrappole");       // S.D.
    session.removeAttribute("ispezioneVisivaRequest");   // Diana S.
    session.removeAttribute("campionamentoRequest");          // S.D.
    session.removeAttribute("trappolaggioRequest");          // S.D.
    session.removeAttribute("onDtoProv");          // B.S.
    session.removeAttribute("onDtoDef");          // B.S.    
    session.removeAttribute("fileProv");          // B.S.
    session.removeAttribute("fileDef");          // B.S.    
    session.removeAttribute("gpsRequest");    // S.D.
    session.removeAttribute("foto");          // S.D.
    session.removeAttribute("idMissione");    // S.D.
  }
  
  /**
   * 
   * @author nicolo.mandrile
   * @param session
   * @param tableNamesToRemove: lista dei nomi delle tabelle per le quali voglio rimuovere le informazioni
   * (filtri, colonne visibili/nascoste, ordinamento, pagina, ...) dalla sessione.
   * 
   */
  @SuppressWarnings("unchecked")
  public void cleanTableMapsInSession(HttpSession session, List<String> tableNamesToRemove){

  		HashMap<String, String> mapFilters = (HashMap<String, String>)session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  		HashMap<String, String> mapFiltersColOrd = (HashMap<String, String>)session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_COLONNA_ORDINAMENTO);
  		HashMap<String, String> mapFiltersTipoOrd = (HashMap<String, String>)session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_TIPO_ORDINAMENTO);
  		HashMap<String, String> mapFiltersNumPag = (HashMap<String, String>)session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_NUMERO_PAGINA);
  		HashMap<String, String> mapFiltersPageSize = (HashMap<String, String>)session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_PAGE_SIZE);
  		HashMap<String, String> mapFiltersColonneNascoste = (HashMap<String, String>)session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_COLONNE_NASCOSTE);
  		HashMap<String, String> mapRigheVisibili = (HashMap<String, String>)session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_RIGHE_VISIBILI);

  		removeTableInfoFromMap(mapFiltersColonneNascoste, tableNamesToRemove); 
  		removeTableInfoFromMap(mapFilters, tableNamesToRemove);
  		removeTableInfoFromMap(mapFiltersColOrd, tableNamesToRemove);
  		removeTableInfoFromMap(mapFiltersTipoOrd, tableNamesToRemove);
  		removeTableInfoFromMap(mapFiltersNumPag, tableNamesToRemove);
  		removeTableInfoFromMap(mapFiltersPageSize, tableNamesToRemove);
  		removeTableInfoFromMap(mapRigheVisibili, tableNamesToRemove);
  	}
  	
	private void removeTableInfoFromMap(HashMap<String, String> mapFilters, List<String> tableNamesToRemove) {
		if(mapFilters!=null && tableNamesToRemove!=null && !tableNamesToRemove.isEmpty()){
			for(String tableName : tableNamesToRemove)
			{
				mapFilters.remove(tableName); 		
			}
		}
	}
	
	public List<DecodificaDTO<String>> getListUfficiZonaFunzionari()
	{
		List<DecodificaDTO<String>> ufficiZona = new ArrayList<DecodificaDTO<String>>();
		final String STESSO_UFFICIO = "STESSO_UFFICIO";
		ufficiZona.add(new DecodificaDTO<String>(STESSO_UFFICIO,"Singolo ufficio zona"));
		ufficiZona.add(new DecodificaDTO<String>(null,"Tutti gli uffici zona"));
		return ufficiZona;
  	}
	
	protected int getProcedimentoAgricoloFromSession(
		      HttpSession session) throws InternalUnexpectedException
	  {
	    return getUtenteAbilitazioni(session).getIdProcedimento();
	  }


  public static Map<String,Boolean> getCodiciPunteggioPerInterventiPrevenzione()
  {
	  Map<String,Boolean> mappa = new HashMap<String,Boolean>();
	  mappa.put(IuffiConstants.PUNTEGGI.CODICE_LUPI01, Boolean.TRUE);
	  mappa.put(IuffiConstants.PUNTEGGI.CODICE_LUPI02, Boolean.TRUE);
	  mappa.put(IuffiConstants.PUNTEGGI.CODICE_LUPI03, Boolean.TRUE);
	  return mappa;
  }
  
  public boolean isIstruttoria(ProcedimentoOggetto po)
  {
    return (po.getiDLegameGruppoOggetto() != null && po.getiDLegameGruppoOggetto() == 9L);
  }

  protected static String getFileAsString(File file) 
  {
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) 
    {

      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) 
      {
        contentBuilder.append(sCurrentLine).append("\n");
      }
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
    }
    return contentBuilder.toString();
  }

  protected static ResourceBundle safeGetBundle(String name) {
    try {
      return ResourceBundle.getBundle(name);
    } catch (Exception e) {
      return null;
    }
  }

}
