package it.csi.iuffi.iuffiweb.business.impl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.ImportiRipartitiListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.StampaListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.DatiCreazioneListaDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.DatiListaDaCreareDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.LivelliBandoDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoImportiApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoPraticheApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONBandiNuovaListaDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RisorseImportiOperazioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.ListeLiquidazioneDAO;
import it.csi.iuffi.iuffiweb.util.DumpUtils;
import it.csi.iuffi.iuffiweb.util.InputStreamDataSource;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.smrcomms.siapcommws.dto.smrcomm.EsitoDocumentoVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsDocumentoInputVO;
import it.csi.smrcomms.siapcommws.exception.smrcomm.InvalidParameterException;
import it.csi.smrcomms.siapcommws.exception.smrcomm.ProtocollaSystemException;
import it.csi.smrcomms.siapcommws.exception.smrcomm.UnrecoverableException;

@Stateless()
@EJB(name = "java:app/ListeLiquidazione", beanInterface = IListeLiquidazioneEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ListeLiquidazioneEJB extends
    IuffiAbstractEJB<ListeLiquidazioneDAO> implements IListeLiquidazioneEJB
{
  protected static final String THIS_CLASS = ListeLiquidazioneEJB.class
      .getSimpleName();
  protected SessionContext      sessionContext;

  @Resource
  private void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<RigaJSONElencoListaLiquidazioneDTO> getListeLiquidazione()
      throws InternalUnexpectedException
  {
    List<RigaJSONElencoListaLiquidazioneDTO> liste = dao
        .getListeLiquidazione(null);
    for (RigaJSONElencoListaLiquidazioneDTO r : liste)
    {
      List<LivelloDTO> livs = dao.getElencoLivelliByIdBando(r.getIdBando());
      r.setLivelli(livs);
    }
    return liste;

  }

  @Override
  public List<Map<String, Object>> getAmministrazioniCompetenzaListe()
      throws InternalUnexpectedException
  {
    return dao.getAmministrazioniCompetenzaListe();
  }

  @Override
  public List<RigaJSONBandiNuovaListaDTO> getBandiProntiPerListeLiquidazione(
      List<Long> lIdAmmCompetenza)
      throws InternalUnexpectedException
  {
    return dao.getBandiProntiPerListeLiquidazione(lIdAmmCompetenza);
  }

  @Override
  public LivelliBandoDTO getLivelliBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getLivelliBando(idBando);
  }

  @Override
  public List<DecodificaDTO<Long>> findAmministrazioniInProcedimentiBando(
      long idBando, List<Long> lIdAmmCompetenza)
      throws InternalUnexpectedException
  {
    return dao.findAmministrazioniInProcedimentiBando(idBando,
        lIdAmmCompetenza);
  }

  @Override
  public List<DecodificaDTO<Long>> getTecniciLiquidatori(long idAmmCompetenza, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    return dao.getTecniciLiquidatori(idAmmCompetenza, idProcedimentoAgricoltura);
  }

  @Override
  public DatiListaDaCreareDTO getDatiListaDaCreare(long idBando,
      long idAmmCompetenza, long idTipoImporto, List<Long> idsPODaEscludere)
      throws InternalUnexpectedException
  {
    dao.lockBando(idBando);
    DatiListaDaCreareDTO datiListaDaCreareDTO = new DatiListaDaCreareDTO();
    datiListaDaCreareDTO
        .setIdMaxListaLiquidazioneCorrente(dao.getMaxIdListaLiquidazione(
            idBando, idAmmCompetenza, idTipoImporto));

    datiListaDaCreareDTO.setRisorse(dao.getDatiListaDaCreare(idBando,
        idAmmCompetenza, idTipoImporto, idsPODaEscludere));
    return datiListaDaCreareDTO;
  }

  @Override
  public DatiCreazioneListaDTO creaListaLiquidazione(long idBando,
      long idAmmCompetenza, int idTipoImporto,
      Long idTecnicoLiquidatore, DatiListaDaCreareDTO datiListaDaCreareDTO,
      long idUtenteAggiornamento, List<Long> idsPODaEscludere, long idProcedimentoAgricolo)
      throws InternalUnexpectedException, ApplicationException
  {
    // Per prima cosa richiedo accesso esclusivo al bando
    dao.lockBando(idBando);
    // Ora carichiamo i dati attuali e verifichiamo la congruenza con quanto
    // visualizzato all'utente, se non fossero
    // compatibili non sarebbe possibile procedere alla creazione della lista
    Long idMaxListaLiquidazioneCorrente = dao.getMaxIdListaLiquidazione(idBando,
        idAmmCompetenza, idTipoImporto);

    if (IuffiUtils.NUMBERS.nvl(idMaxListaLiquidazioneCorrente,
        Long.MIN_VALUE) != IuffiUtils.NUMBERS
            .nvl(datiListaDaCreareDTO.getIdMaxListaLiquidazioneCorrente(),
                Long.MIN_VALUE))
    {
      /*
       * Qualcuno ho creato una nuova lista di liquidazione per la tripletta
       * bando/amm competenza/tipo importo in concorrenza con l'utente
       * Incompatibilità grave ==> impossibile creare la lista ==> segnalo
       * all'utente la situazione forzando il ricarico dei dati con una
       * ApplicationException
       */
      throw new ApplicationException(
          "Attenzione! Si è verificato un errore grave nella creazione lista: un altro utente ha creato nel frattempo una lista di liquidazione su questo stesso bando, per la stessa amministrazione e lo stesso Tipo importo. Impossibile proseguire con la creazione");
    }
    /*
     * Devo verificare che il tecnico liquidatore non sia stato un tecnico
     * istruttore nelle pratiche in corso di liquidazione
     */
    if (!dao.checkTecnicoLiquidatoreNonIstruttore(idBando, idAmmCompetenza,
        idTipoImporto, idTecnicoLiquidatore, null, idsPODaEscludere, idProcedimentoAgricolo))
    {
      throw new ApplicationException(
          "Impossibile creare la lista di liquidazione: il funzionario liquidatore risulta aver ricoperto il ruolo di istruttore in almeno uno dei pagamenti in liquidazione; per visualizzare i pagamenti con l'anomalia selezionare il pulsante \"Elenco pratiche in lista\" e filtrare quelle con l'errore.",
          -123);
    }
    // Se sono arrivato qua è perchè non sono state create nuove liste che
    // possono dare fastidio, verifico la congruenza
    // delle risorse
    List<RisorseImportiOperazioneDTO> risorseAttuali = dao.getDatiListaDaCreare(
        idBando, idAmmCompetenza,
        idTipoImporto, idsPODaEscludere);
    if (risorseAttuali == null || risorseAttuali.isEmpty())
    {
      // In teoria non dovrebbe mai avvenire (o quanto meno solo in casi rari),
      // comunque meglio una verifica in più che
      // una in meno
      throw new ApplicationException(
          "Attenzione! Si è verificato un errore grave nella creazione lista: Non sono state assegnate delle risorse finanziarie per il bando selezionato");
    }
    List<RisorseImportiOperazioneDTO> risorseUtente = datiListaDaCreareDTO
        .getRisorse();
    if (risorseAttuali.size() != risorseUtente.size())
    {
      // Se il numero di risorse disponibili attuali non coincide con il numero
      // di risorse disponibili viste dall'utente
      // allora c'è sicuramente stato un cambiamento che non può essere ignorato
      // ==> Errore e visualizzazione dei dati
      // corretti all'utente
      throw new ApplicationException(
          "Attenzione! Si è verificato un errore grave nella creazione lista: un altro utente nel frattempo ha modificato le risorse finanziarie assegnate al Bando. Impossibile proseguire con la creazione");
    }
    Map<Long, RisorseImportiOperazioneDTO> mapRisorse = new HashMap<Long, RisorseImportiOperazioneDTO>();
    // Le risorse attuali e quelle visualizzate dall'utente sono uguali in
    // numero, quindi a questo punto mi basta
    // verificare che ogni risorsa visualizzata all'utente esista ancora con
    // valori di importi compatibili
    for (RisorseImportiOperazioneDTO risorsa : risorseAttuali)
    {
      mapRisorse.put(risorsa.getIdRisorseLivelloBando(), risorsa);
    }
    boolean hasImportiDaLiquidare = false;
    BigDecimal bdTotaleDaLiquidareInListaPrimaCreazione = BigDecimal.ZERO;
    int totaleNumeroPagamentiPrimaCreazione = 0;

    for (RisorseImportiOperazioneDTO risorsa : risorseUtente)
    {
      RisorseImportiOperazioneDTO risorsaAttuale = mapRisorse
          .get(risorsa.getIdRisorseLivelloBando());
      if (risorsaAttuale == null)
      {
        // Manca ==> Errore grave!
        throw new ApplicationException(
            "Attenzione! Si è verificato un errore grave nella creazione lista: un altro utente nel frattempo ha modificato le risorse finanziarie assegnate al Bando. Impossibile proseguire con la creazione");
      }
      if (risorsaAttuale.getNumeroPagamentiLista() != risorsa
          .getNumeroPagamentiLista())
      {
        // Numero differente di pagamenti ==> Errore grave!
        throw new ApplicationException(
            "Attenzione! Si è verificato un errore grave nella creazione lista: il numero di pratiche e/o l'importo totale da liquidare è variato, probabilmente nel frattempo sono state ammesse a pagamento altre pratiche");
      }
      final BigDecimal importoDaLiquidare = risorsa.getImportoDaLiquidare();
      if (risorsaAttuale.getImportoDaLiquidare()
          .compareTo(importoDaLiquidare) != 0)
      {
        // Cambiato l'importo da liquidare ==> Errore grave!
        throw new ApplicationException(
            "Attenzione! Si è verificato un errore grave nella creazione lista: il numero di pratiche e/o l'importo totale da liquidare è variato, probabilmente nel frattempo sono state ammesse a pagamento altre pratiche");
      }
      boolean isImportoDaLiquidare = BigDecimal.ZERO
          .compareTo(importoDaLiquidare) < 0;
      hasImportiDaLiquidare |= isImportoDaLiquidare;
      bdTotaleDaLiquidareInListaPrimaCreazione = bdTotaleDaLiquidareInListaPrimaCreazione
          .add(importoDaLiquidare,
              MathContext.DECIMAL128)
          .setScale(2, RoundingMode.HALF_UP);
      totaleNumeroPagamentiPrimaCreazione += risorsa.getNumeroPagamentiLista();
      if (risorsaAttuale.getImportoRimanente().compareTo(BigDecimal.ZERO) < 0
          && isImportoDaLiquidare)
      {
        // Per questa operazione ci sono delle pratiche da liquidare ma non c'è
        // copertura ==> Errore grave!
        throw new ApplicationException(
            "Attenzione! Si è verificato un errore grave nella creazione lista: Nel frattempo sono intervenute delle modifiche sulle risorse finanziarie, oppure sono state gerenate da un altro utente liste di liquidazione sullo stesso bando che non permettono più la copertura finanziaria");
      }
    }
    if (!hasImportiDaLiquidare)
    {
      // Nessuna delle operazioni che vengono pagate in questa lista ha delle
      // pratiche da liquidare ==> inutile creare
      // una lista vuota ==> Errore grave!
      throw new ApplicationException(
          "Attenzione! Si è verificato un errore grave nella creazione lista: Non esistono pratiche da pagare per questa lista");
    }
    // uff che fatica, ho superato tutti i controlli posso procedere alla
    // creazione della lista
    long idListaLiquidazione = dao.insertListaLiquidazione(idBando,
        idAmmCompetenza, idTipoImporto,
        idTecnicoLiquidatore, idUtenteAggiornamento);

    /**
     * ATTENZIONE: L'ordine seguente dei 2 metodi è importante, PRIMA bisogna
     * inserire la tabella IUF_R_RISORSE_LIV_BANDO e poi la tabella
     * IUF_R_LISTA_LIQUIDAZ_IMP_LIQ perchè la query che estrae i record da
     * inserire in entrambi i casi si basa sul fatto che non siano ancora
     * presenti su IUF_R_RISORSE_LIV_BANDO, quindi chiamando in ordine errato
     * i metodi si ottiene di non inserire nulla sulla tabella di relazione
     * IUF_R_RISORSE_LIV_BANDO.
     */
    dao.insertRelazioneTraRisorseLivelloBandoEImportiLiquidati(idBando,
        idAmmCompetenza, idTipoImporto, idsPODaEscludere);
    dao.insertRelazioneTraImportiLiquidatiEListaLiquidazione(
        idListaLiquidazione, idBando, idAmmCompetenza,
        idTipoImporto, idsPODaEscludere);
    /**
     * Ho inserito le relazioni per la lista, ora mi resta da ripartire gli
     * importi liquidati. Lo faccio in 2 tempi. Prima inserisco tutti gli
     * importi ripartiti come percentuale cioè quelli con
     * FLAG_CRITERIO_RIPARTIZIONE = 'R' che vengono calcolati come percentuale
     * dell'importo liquidato. Poi inserisco i restanti importi ripartiti per
     * differenza cioè quelli con FLAG_CRITERIO_RIPARTIZIONE = 'D' (in sostanza
     * mi disinteresso della percentuale di ripartizione ma faccio la differenza
     * tra l'importo liquidato e i record già inseriti al punto precedente per
     * ogni singolo importo liquidato) in modo da avere sempre il 100% come
     * somma delle ripartizioni. Anche in questo caso, ovviamente, L'ORDINE DI
     * CHIAMATA DEI METODI E' IMPORTANTE!
     */
    dao.insertImportiRipartitiConPercentuale(idListaLiquidazione);
    dao.insertImportiRipartitiPerDifferenza(idListaLiquidazione);
    /** Infine inserisco il record per la stampa... uff che fatica! */
    dao.insertFileListaLiquidazione(idListaLiquidazione);
    /**
     * Inizio con le verifiche post creazione Carico la lista appena caricata...
     * Prendo l'elemento con indice 0. Nessun controllo... Esiste perchè l'ho
     * appena inserita e nessuno può averla cancellata dato che non è ancora
     * visibile al di fuori della transazione.
     */
    RigaJSONElencoListaLiquidazioneDTO lista = dao
        .getListeLiquidazione(idListaLiquidazione).get(0);
    /**
     * Perchè tutto sia andato a buon fine occorre che il numero pagamenti e
     * l'importo totale da liquidare della lista siano uguali a quelli
     * presentati all'utente, altrimenti è intervenuto qualcosa che ha cambiato
     * i dati sul db mentre stavo inserendo i dati (nel periodo di tempo che
     * intercorre tra la validazione dei dati e gli insert degli stessi)
     */
    if (lista.getImporto()
        .compareTo(bdTotaleDaLiquidareInListaPrimaCreazione) != 0 ||
        lista.getNumPagamenti() != totaleNumeroPagamentiPrimaCreazione)
    {
      /**
       * In questo caso, al 99% il problema è dovuto al fatto che qualche utente
       * abbia messo come liquidabile una pratica proprio mentre si inseriva la
       * lista di liquidazione. Indipendentemente dal motivo non posso creare
       * una lista di liquidazione con dati differenti da quelli presentati a
       * video ==> Errore!
       */
      throw new ApplicationException(
          "Attenzione! Si è verificato un errore grave nella creazione lista: sono avvenute delle modifiche che hanno comportato una variazione del numero delle pratiche o importo liquidabili. Impossibile procedere");
    }
    // Evvai! Lista fatta e finita! ==> ritorno i dati di creazione della lista
    return dao.getDatiCreazioneLista(idListaLiquidazione);
  }

  @Override
  public void deleteListaLiquidazione(Long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    try
    {
      dao.delete("IUF_T_FILE_LISTA_LIQUIDAZION", "ID_LISTA_LIQUIDAZIONE",
          idListaLiquidazione);
      dao.deleteRisorseLivBandImpLiq(idListaLiquidazione);
      dao.deleteRImportiRipartiti(idListaLiquidazione); // non c'è nell'analisi
                                                        // ma direi che va anche
                                                        // pulita questa
      dao.delete("IUF_R_LISTA_LIQUIDAZ_IMP_LIQ", "ID_LISTA_LIQUIDAZIONE",
          idListaLiquidazione);
      dao.delete("IUF_T_LISTA_LIQUIDAZIONE", "ID_LISTA_LIQUIDAZIONE",
          idListaLiquidazione);
    }
    catch (InternalUnexpectedException e)
    {
      sessionContext.setRollbackOnly();
    }
  }

  @Override
  public StampaListaLiquidazioneDTO getStampaListaLiquidazione(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    return dao.getStampaListaLiquidazione(idListaLiquidazione);
  }

  @Override
  public String getStatoListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    return dao.getStatoListaLiquidazione(idListaLiquidazione);
  }

  @Override
  public Boolean ripristinaStampaListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {

    dao.lockListaLiquidazione(idListaLiquidazione);
    StampaListaLiquidazioneDTO stampaListaLiq = dao
        .getStampaListaLiquidazione(idListaLiquidazione);
    if (stampaListaLiq != null)
    {
      if (IuffiUtils.DATE.diffInSeconds(new Date(),
          stampaListaLiq
              .getDataUltimoAggiornamento()) > IuffiConstants.TEMPO.SECONDI_PRIMA_DI_RIGENERARE_UNA_STAMPA) // 10
      // minuti
      {
        // Se lo stato è generazione in corso, per aggiornare il record deve
        // essere passato un certo tempo (10 minuti di
        // default)
        // Non è ancora passato il lasso di tempo per permettere la
        // rigenerazione == > non faccio nulla, una stampa è
        // già in corso
        // Questo caso, se non ci sono errori è dovuto al fatto che più utenti
        // abbiano richiesto la rigenerazione in
        // contemporanea,
        // partono entrambi da una situazione valida per richiedere la
        // rigenerazione ma il secondo che arriva si trova
        // il record
        // già aggiornato, quindi non fa nulla.
        dao.ripristinaListaLiquidazione(stampaListaLiq);
        return true;
      }
      else
      {
        return false;
      }

    }
    else
    {
      // Non mi metto a controllare lo stato, TANTO NON ESSENDOCI
      // EXT_ID_DOCUMENTO_INDEX non può essere andato bene...
      // ==> RIGENERO
      dao.ripristinaListaLiquidazione(stampaListaLiq);
      return true;
    }

  }

  @Override
  public void setStatoListaLiquidazione(long idListaLiquidazione, int stato)
      throws InternalUnexpectedException
  {
    dao.lockListaLiquidazione(idListaLiquidazione);
    dao.setStatoListaLiquidazione(idListaLiquidazione, stato);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public byte[] getContenutoFileListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    return dao.getContenutoFileListaLiquidazione(idListaLiquidazione);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean isContenutoFileListaLiquidazioneDisponibile(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    return dao.isContenutoFileListaLiquidazioneDisponibile(idListaLiquidazione);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public RigaJSONElencoListaLiquidazioneDTO getListaLiquidazioneById(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    List<RigaJSONElencoListaLiquidazioneDTO> list = dao
        .getListeLiquidazione(idListaLiquidazione);
    return (list == null || list.isEmpty()) ? null : list.get(0);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ImportiRipartitiListaLiquidazioneDTO> getImportiRipartitiListaLiquidazione(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    return dao.getImportiRipartitiListaLiquidazione(idListaLiquidazione);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<RiepilogoImportiApprovazioneDTO> getRiepilogoImportiApprovazione(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRiepilogoImportiApprovazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getRiepilogoImportiApprovazione(idListaLiquidazione);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public void aggiornaStatoLista(long idListaLiquidazione,
      String flagStatoLista, long idUtenteAggiornamento)
      throws InternalUnexpectedException, ApplicationException
  {
    final String THIS_METHOD = "aggiornaStatoLista";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      dao.lockListaLiquidazione(idListaLiquidazione);
      List<RigaJSONElencoListaLiquidazioneDTO> liste = dao
          .getListeLiquidazione(idListaLiquidazione);
      RigaJSONElencoListaLiquidazioneDTO listaLiquidazioneDTO = null;
      if (liste != null && liste.size() > 0)
      {
        listaLiquidazioneDTO = liste.get(0);
      }
      if (listaLiquidazioneDTO == null)
      {
        throw new ApplicationException(
            "La lista di liquidazione indicata non esiste. E' stato probabilmente cancellata da qualche altro utente");
      }
      if (listaLiquidazioneDTO
          .getIdStatoStampa() == IuffiConstants.STATO.STAMPA.ID.FIRMATO_DIGITALMENTE)
      {
        throw new ApplicationException(
            "La lista di liquidazione indicata è approvata, impossibile proseguire");
      }
      dao.aggiornaStatoLista(idListaLiquidazione, flagStatoLista,
          idUtenteAggiornamento);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  /**
   * Il metodo, in caso di errore applicativo gestito, non lancia l'eccezione
   * ApplicationException ma la ritorna al chiamante. Questo perchè, nel caso di
   * errore nella scrittura/protocollazione sul documentale Agriwellweb, il
   * messaggio di errore viene scritto su db, quindi serve che la transazione
   * esegua il commit, cosa che entra in contrasto con il fatto che in caso di
   * eccezione venga eseguito il rollback di default. Per non creare
   * un'eccezione che non forzi il rollback della transazione (cosa che sarebbe
   * potenzialmente pericolosa) si è preferito restituire l'eccezione.
   */
  @Override
  public ApplicationException approvaLista(long idListaLiquidazione,
      MultipartFile stampaFirmata,
      UtenteAbilitazioni utenteAbilitazioni)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "approvaLista";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      long idUtenteAggiornamento=utenteAbilitazioni.getIdUtenteLogin();
      dao.lockListaLiquidazione(idListaLiquidazione);
      List<RigaJSONElencoListaLiquidazioneDTO> liste = dao
          .getListeLiquidazione(idListaLiquidazione);
      RigaJSONElencoListaLiquidazioneDTO listaLiquidazioneDTO = null;
      if (liste != null && liste.size() > 0)
      {
        listaLiquidazioneDTO = liste.get(0);
      }
      if (listaLiquidazioneDTO == null)
      {
        throw new ApplicationException(
            "La lista di liquidazione indicata non esiste. E' stato probabilmente cancellata da qualche altro utente");
      }
      if (listaLiquidazioneDTO
          .getIdStatoStampa() == IuffiConstants.STATO.STAMPA.ID.FIRMATO_DIGITALMENTE)
      {
        throw new ApplicationException(
            "La lista di liquidazione indicata è già approvata, impossibile proseguire");
      }

      if (dao.isListaLiquidazioneCorrotta(idListaLiquidazione))
      {
        throw new ApplicationException(
            "La lista di liquidazione indicata è corrotta (a causa di un precedente tentativo non riuscito di approvazione), impossibile procedere. Si prega di contattare l'assistenza tecnica comunicando il seguente messaggio: La lista di liquidazione con ID="
                + idListaLiquidazione + " è corrotta");
      }
      String emailAmmCompetenza = dao
          .getEmailAmmCompetenzaLista(idListaLiquidazione);

      String[] PARAM_NAMES =
      {
          IuffiConstants.PARAMETRO.DOQUIAGRI_USR_PSW_SC,
          IuffiConstants.PARAMETRO.DOQUIAGRI_ID_TIPO_DOCUMENTO_LISTA_LIQUIDAZIONE,
          IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG_LISTE_LIQUIDAZIONE,
          IuffiConstants.PARAMETRO.DOQUIAGRI_FASCICOLA_LISTE_LIQUIDAZIONE,
          IuffiConstants.PARAMETRO.DOQUIAGRI_CARTELLA,
      };
      Map<String, String> mapParametri = getParametri(PARAM_NAMES);
      for (String param : PARAM_NAMES)
      {
        if (mapParametri.get(param) == null)
        {
          throw new ApplicationException(
              "Si è verificato un errore interno grave, impossibile proseguire. Contattare l'assistenza tecnica comunicando il seguente messaggio: Parametro \""
                  + param + "\" non presente");
        }
      }
      
      
      String codClassLiquidazione = dao.getCodClassLiquidazione(utenteAbilitazioni.getIdProcedimento(),listaLiquidazioneDTO.getIdBando());
      mapParametri.put(IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG_LISTE_LIQUIDAZIONE, codClassLiquidazione);
      
      /* Per prima cosa inserisco la lista sul documentale e la protocollo */
      EsitoDocumentoVO esito = archiviaProtocollaLista(listaLiquidazioneDTO,
          stampaFirmata, idUtenteAggiornamento,
          mapParametri, emailAmmCompetenza, utenteAbilitazioni.getIdProcedimento());
      /*
       * A questo punto la lista è su Agriwell / Doqui. Quindi nel caso qualcosa
       * andasse male nelle fasi sequenti sarebbe un problema GRAVE da segnalare
       * all'utente e che necessiterebbe una correzione manuale, dato che non ho
       * transazionalità sui WS.
       */
      long extIdDocumentoIndex = esito.getIdDocumentoIndex();
      try
      {
        dao.approvaFileListaLiquidazione(idListaLiquidazione, esito,
            stampaFirmata.getOriginalFilename());
        dao.approvaLista(idListaLiquidazione, idUtenteAggiornamento);
      }
      catch (Exception e)
      {
        /*
         * Converto in ApplicationException in modo che venga catturata dal
         * catch successivo e restituita senza generare rollback. Non mi importa
         * se i dati su db non resteranno congruenti: l'obiettivo è tenere
         * traccia in qualsiasi modo che la lista sia già stata mandata al
         * documentale ed evitare di rimandarla. Se l'errore è avvenuto
         * sull'aggiornamento di IUF_T_FILE_LISTA_LIQUIDAZION non c'è nulla
         * per cui fare rollback (quindi inutile farlo), mentre se l'errore
         * fosse avvenuto sull'aggiornamento di IUF_T_LISTA_LIQUIDAZIONE, ho i
         * dati del documentale su IUF_T_FILE_LISTA_LIQUIDAZION e posso
         * recuperare la situazione (e controllando la loro presenza anomala su
         * db, posso inibire eventuali tentativi di ri-approvazione che
         * creerebbero solo problemi), quindi meglio evitare il rollback
         */
        throw new ApplicationException(
            "Si è verificato un errore interno GRAVE nell'applicazione. "
                + "La lista di liquidazione è stata registrata e protocollata sul documentale ma non è stato possibile aggiornare la base dati. "
                + "Si prega di NON ripetere l'operazione ma contattare immediatamente l'assistenza tecnica e comunicare il seguente messaggio di errore: "
                + "Errore nel cambio di stato della lista di liquidazione con ID="
                + idListaLiquidazione
                + " registrata sul documentale con ID=" + extIdDocumentoIndex,
            e);
      }
      return null;
    }
    catch (ApplicationException e)
    {
      logger.error("Si è verificato una eccezione ApplicationException", e);
      DumpUtils.logGenericException(logger, null, e, (LogParameter[]) null,
          (LogVariable[]) null,
          "[" + THIS_CLASS + "." + THIS_METHOD + "]");
      return e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  protected EsitoDocumentoVO archiviaProtocollaLista(
      RigaJSONElencoListaLiquidazioneDTO listaLiquidazioneDTO,
      MultipartFile stampaFirmata,
      long idUtenteAggiornamento, Map<String, String> mapParametri,
      String emailAmmCompetenza, int idProcedimentoAgricolo)
      throws InternalUnexpectedException, ApplicationException
  {
    final String THIS_METHOD = "archiviaProtocollaLista";
    Integer codice = null;
    final long idListaLiquidazione = listaLiquidazioneDTO
        .getIdListaLiquidazione();
    try
    {
      SiapCommWsDocumentoInputVO inputVO = IuffiUtils.WS
          .getSiapCommDocumentoVOPerArchiviazioneListeLiquidazione(
              listaLiquidazioneDTO,
              mapParametri,
              IuffiUtils.FILE.getSafeSubmittedFileName(
                  stampaFirmata.getOriginalFilename()),
              emailAmmCompetenza,
              idUtenteAggiornamento,
              idProcedimentoAgricolo);
      if (IuffiConstants.FLAGS.SI.equals(inputVO.getFlagTimbroProtocollo()))
      {
        String[] PARAM_NAMES_COORDINATE_LISTE_LIQUIDAZIONE =
        {
            IuffiConstants.PARAMETRO.DOQUIAGRI_LISTE_LIQUIDAZIONE_X_DESTRA,
            IuffiConstants.PARAMETRO.DOQUIAGRI_LISTE_LIQUIDAZIONE_X_SINISTRA,
            IuffiConstants.PARAMETRO.DOQUIAGRI_LISTE_LIQUIDAZIONE_Y_ALTO,
            IuffiConstants.PARAMETRO.DOQUIAGRI_LISTE_LIQUIDAZIONE_Y_BASSO
        };
        Map<String, String> mapParametriCoordinate = getParametri(
            PARAM_NAMES_COORDINATE_LISTE_LIQUIDAZIONE);
        for (String param : PARAM_NAMES_COORDINATE_LISTE_LIQUIDAZIONE)
        {
          if (mapParametriCoordinate.get(param) == null)
          {
            throw new ApplicationException(
                "Si è verificato un errore interno grave, impossibile proseguire. Contattare l'assistenza tecnica comunicando il seguente messaggio: Parametro \""
                    + param + "\" non presente");
          }
        }
        IuffiUtils.WS.addMetadatiCoordinateProtocolloListaLiquidazione(
            inputVO.getNuovoDocumento(), mapParametriCoordinate);
      }
      DataHandler handler = new DataHandler(
          new InputStreamDataSource(
              new ByteArrayInputStream(stampaFirmata.getBytes())));

      EsitoDocumentoVO esito = IuffiUtils.WS.getSiapComm()
          .archiviaProtocollaDocumento(inputVO, handler);
      codice = esito == null ? null : esito.getEsitoVO().getCodice();
      if (codice != null
          && codice == IuffiConstants.SERVICE.AGRIWELL.RETURN_CODE.SUCCESS)
      {
        logger.info("[" + THIS_CLASS + "." + THIS_METHOD
            + "] La lista di liquidazione " + idListaLiquidazione
            + " è stata archiviata e protocollata con idDocumentoIndex = "
            + esito.getIdDocumentoIndex()
            + ", numeroProtocolloDocumento = "
            + esito.getNumeroProtocolloDocumento()
            + ", dataProtocolloDocumento = "
            + esito.getDataProtocolloDocumento()
            + ", numeroProtocolloEmergenza = "
            + esito.getNumeroProtocolloEmergenza()
            + ", dataProtocolloEmergenza = "
            + esito.getDataProtocolloEmergenza());
        return esito;
      }
      else
      {
        String messaggioErrore = null;
        if (esito == null || esito.getEsitoVO() == null)
        {
          messaggioErrore = "esito==null";
        }
        else
        {
          messaggioErrore = esito.getEsitoVO().getMessaggio();
        }
        final String errorMessage = "Si è verificato un errore nel richiamo dels servizio archiviaProtocollaDocumento() di agriwell. Il messaggio di errore restituito è: "
            + messaggioErrore;
        logger.error("[" + THIS_CLASS + "." + THIS_METHOD + " " + errorMessage);
        // Registro l'errore
        dao.scriviEccezioneAgriwell(idListaLiquidazione,
            new ApplicationException(errorMessage));
        // E rilancio un'eccezione "pulita" (senza quindi informazioni tecniche,
        // già registrate su db) che verrà
        // visualizzata all'utente
        throw new ApplicationException(
            "Si è verificato un errore nella registrazione/protocollazione della lista di liquidazione sul sistema documentale. Se il problema persistesse si prega di contattare l'assistenza tecnica");
      }
    }
    catch (ApplicationException e)
    {
      throw e;
    }
    catch (InvalidParameterException | ProtocollaSystemException
        | MalformedURLException | UnrecoverableException e)
    {
      logger.error("[" + THIS_CLASS + "." + THIS_METHOD
          + " Si è verificato un errore nel richiamo del servizio archiviaProtocollaDocumento() di agriwell. Il messaggio di errore restituito è: "
          + e.getMessage());
      dao.scriviEccezioneAgriwell(idListaLiquidazione, e);
      throw new ApplicationException(
          "Si è verificato un errore nella registrazione/protocollazione della lista di liquidazione sul sistema documentale. Se il problema persistesse si prega di contattare l'assistenza tecnica");
    }
    catch (Exception e)
    {
      logger.error("[" + THIS_CLASS + "." + THIS_METHOD
          + " Si è verificato un errore generico nell'invio della lista di liquidazione sul documentale di agriwell. Il messaggio di errore restituito è: "
          + e.getMessage());
      dao.scriviEccezioneAgriwell(idListaLiquidazione, e);
      throw new ApplicationException(
          "Si è verificato un errore nella registrazione/protocollazione della lista di liquidazione sul sistema documentale. Se il problema persistesse si prega di contattare l'assistenza tecnica");
    }
  }

  @Override
  public List<RiepilogoPraticheApprovazioneDTO> getRiepilogoPraticheInNuovaLista(
      long idBando, long idAmmCompetenza,
      int idTipoImporto) throws InternalUnexpectedException
  {
    return dao.getRiepilogoPraticheInNuovaLista(idBando, idAmmCompetenza,
        idTipoImporto);
  }

  @Override
  public String getTitoloListaLiquidazioneByIdBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getTitoloListaLiquidazioneByIdBando(idBando);
  }

  @Override
  public boolean isListaLiquidazioneCorrotta(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    return dao.isListaLiquidazioneCorrotta(idListaLiquidazione);
  }

  @Override
  public List<RiepilogoPraticheApprovazioneDTO> getRiepilogoPraticheInNuovaListaWithAnomalia(
      long idBando, long idAmmCompetenza, int idTipoImporto,
      long idTecnicoLiquidatore, long idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    List<RiepilogoPraticheApprovazioneDTO> lista = dao
        .getRiepilogoPraticheInNuovaLista(idBando, idAmmCompetenza,
            idTipoImporto);

    Iterator<RiepilogoPraticheApprovazioneDTO> iter = lista.iterator();

    // rimuovo elementi con anomalia
    while (iter.hasNext())
    {

      RiepilogoPraticheApprovazioneDTO r = iter.next();

      if (!dao.checkTecnicoLiquidatoreNonIstruttore(idBando, idAmmCompetenza,
          idTipoImporto, idTecnicoLiquidatore, r.getIdProcedimentoOggetto(),
          null, idProcedimentoAgricolo))
        r.setAnomalia("S");
      else
        iter.remove();
    }

    return lista;
  }

  @Override
  public long getIdBandoByIdListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    return dao.getIdBandoByIdListaLiquidazione(idListaLiquidazione);
  }

  @Override
  public List<RiepilogoPraticheApprovazioneDTO> getRiepilogoPraticheListaLiquidazione(
      long idListaLiquidazione, boolean isPremio)
      throws InternalUnexpectedException
  {
    return dao.getRiepilogoPraticheListaLiquidazione(idListaLiquidazione,
        isPremio);
  }

  @Override
  public List<LivelloDTO> getElencoLivelli() throws InternalUnexpectedException
  {
    return dao.getElencoLivelli();
  }
}
