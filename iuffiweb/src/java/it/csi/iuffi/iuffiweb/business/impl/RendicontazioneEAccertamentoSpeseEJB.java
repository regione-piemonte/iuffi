package it.csi.iuffi.iuffiweb.business.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IRendicontazioneEAccertamentoSpeseEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.RiduzioniSanzioniDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.AccertamentoSpeseDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DocumentoAllegatoDownloadDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InterventoAccertamentoDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InterventoRendicontazioneDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.PotenzialeErogabileESanzioniRendicontazioneDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONRendicontazioneSuperficiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaProspetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.TotaleContributoAccertamentoElencoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.UpdateSuperficieLocalizzazioneDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.RendicontazioneEAccertamentoSpeseDAO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

@Stateless()
@EJB(name = "java:app/RendicontazioneEAccertamentoSpese", beanInterface = IRendicontazioneEAccertamentoSpeseEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RendicontazioneEAccertamentoSpeseEJB
    extends IuffiAbstractEJB<RendicontazioneEAccertamentoSpeseDAO>
    implements IRendicontazioneEAccertamentoSpeseEJB
{
  private static final String THIS_CLASS = RendicontazioneEAccertamentoSpeseEJB.class
      .getSimpleName();
  @SuppressWarnings("unused")
  private SessionContext      sessionContext;

  @Resource
  private void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  @Override
  public List<RigaRendicontazioneSpese> getElencoRendicontazioneSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getElencoRendicontazioneSpese";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getElencoRendicontazioneSpese(idProcedimentoOggetto, ids);
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
  public List<RigaAccertamentoSpese> getElencoAccertamentoSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getElencoAccertamentoSpese";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getElencoAccertamentoSpese(idProcedimentoOggetto, ids);
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
  public void updateRendicontazioneSpese(List<RigaRendicontazioneSpese> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    final long idProcedimentoOggetto = logOperationOggettoQuadroDTO
        .getIdProcedimentoOggetto();
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    for (RigaRendicontazioneSpese riga : list)
    {
      // Provo ad eseguire l'aggiornamento del record
      int numRecord = dao.updateRendicontazioneSpese(idProcedimentoOggetto,
          riga);
      if (numRecord == 0)
      {
        // Se non ho aggiornato nessun record vuol dire che il record non c'è
        // ==> lo inserisco
        dao.insertRendicontazioneSpese(idProcedimentoOggetto, riga);
      }
    }

    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public void updateAccertamentoSpeseAcconto(List<AccertamentoSpeseDTO> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    final long idProcedimentoOggetto = logOperationOggettoQuadroDTO
        .getIdProcedimentoOggetto();
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    for (AccertamentoSpeseDTO riga : list)
    {
      // Provo ad eseguire l'aggiornamento del record
      int numRecord = dao.updateAccertamentoSpese(idProcedimentoOggetto, riga);
      if (numRecord == 0)
      {
        // Se non ho aggiornato nessun record vuol dire che il record non c'è
        // ==> lo inserisco
        dao.insertAccertamentoSpese(idProcedimentoOggetto, riga);
      }
    }

    // Elimino tutti i record su IUF_R_PROCEDIMENTO_OGG_LIVEL
    dao.delete("IUF_R_PROCEDIMENTO_OGG_LIVEL", "ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto);
    // E li ricalcolo (ad esclusione del contributo Erogabile e Non Erogabile
    // per cui ho bisogno di fare un secondo
    // giro di calcolo
    dao.insertProcedimentoOggLivello(idProcedimentoOggetto);
    /*
     * Prima di fare i conteggi del contributo erogabile e del non erogabile
     * verifico se devono essere assegnate delle sanzioni e/o riduzioni che ne
     * influenzerebbero i valori Cioè se il
     * "Contributo non riconosciuto sanzionabile" (totale) supera del 10% il
     * "Contributo calcolato" (totale) allora devo inserire una sanzione su ogni
     * livello con valore pari all'aggregato (ovviamente per livello) del
     * "Contributo non riconosciuto sanzionabile" solamente nel caso che questo
     * sia maggiore di 0 Per gestire correttamente le sanzioni opero in questo
     * modo:
     */

    /*
     * Devo fare l'unsplit di tutte le riduzioni/sanzioni automatiche che sono
     * state splittate.
     */

    unsplitSanzioni(idProcedimentoOggetto);

    /*
     * 1) elimino le eventuali sanzioni già inserite
     */
    dao.deleteSanzioniAccertamentoAutomatiche(idProcedimentoOggetto);
    /*
     * 2) Re-Inserisco le sanzioni che mancano
     */
    dao.insertSanzioniAutomaticheAccertamento(idProcedimentoOggetto);
    /*
     * 3) Aggiorno IUF_R_PROCEDIMENTO_OGG_LIVEL.SANZIONI con la sommatoria
     * IUF_T_PROC_OGGETTO_SANZIONE.IMPORTO per l’ID_LIVELLO
     */
    dao.updateRProcedimentoOggettoSanzione(idProcedimentoOggetto);

    /* Finalmente posso calcolare il contributo erogabile / non erogabile */
    aggiornaAggregatiContributiLivelloPerAccertamentoSpese(
        idProcedimentoOggetto, dao, true);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public void updateAccertamentoSpeseSaldo(List<AccertamentoSpeseDTO> list,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    final long idProcedimentoOggetto = logOperationOggettoQuadroDTO
        .getIdProcedimentoOggetto();
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    for (AccertamentoSpeseDTO riga : list)
    {
      // Provo ad eseguire l'aggiornamento del record
      int numRecord = dao.updateAccertamentoSpese(idProcedimentoOggetto, riga);
      if (numRecord == 0)
      {
        // Se non ho aggiornato nessun record vuol dire che il record non c'è
        // ==> lo inserisco
        dao.insertAccertamentoSpese(idProcedimentoOggetto, riga);
      }
    }

    // Elimino tutti i record su IUF_R_PROCEDIMENTO_OGG_LIVEL
    dao.delete("IUF_R_PROCEDIMENTO_OGG_LIVEL", "ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto);
    // E li ricalcolo (ad esclusione del contributo Erogabile e Non Erogabile
    // per cui ho bisogno di fare un secondo
    // giro di calcolo
    dao.insertProcedimentoOggLivello(idProcedimentoOggetto);
    /*
     * Prima di fare i conteggi del contributo erogabile e del non erogabile
     * verifico se devono essere assegnate delle sanzioni e/o riduzioni che ne
     * influenzerebbero i valori Cioè se il
     * "Contributo non riconosciuto sanzionabile" (totale) supera del 10% il
     * "Contributo calcolato" (totale) allora devo inserire una sanzione su ogni
     * livello con valore pari all'aggregato (ovviamente per livello) del
     * "Contributo non riconosciuto sanzionabile" solamente nel caso che questo
     * sia maggiore di 0 Per gestire correttamente le sanzioni opero in questo
     * modo:
     */

    /*
     * Devo fare l'unsplit di tutte le riduzioni/sanzioni automatiche che sono
     * state splittate.
     */

    unsplitSanzioni(idProcedimentoOggetto);

    /*
     * 1) elimino le eventuali sanzioni già inserite
     */
    dao.deleteSanzioniAccertamentoAutomatiche(idProcedimentoOggetto);
    /*
     * 2) Re-Inserisco le sanzioni che mancano
     */
    dao.insertSanzioniAutomaticheAccertamento(idProcedimentoOggetto);
    /*
     * 3) Aggiorno IUF_R_PROCEDIMENTO_OGG_LIVEL.SANZIONI con la sommatoria
     * IUF_T_PROC_OGGETTO_SANZIONE.IMPORTO per l’ID_LIVELLO
     */
    dao.updateRProcedimentoOggettoSanzione(idProcedimentoOggetto);

    /* Finalmente posso calcolare il contributo erogabile / non erogabile */
    aggiornaAggregatiContributiLivelloPerAccertamentoSpese(
        idProcedimentoOggetto, dao, false);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  private void unsplitSanzioni(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    try
    {
      String flagControlloInLoco = dao.getFlagControllo(idProcedimentoOggetto);

      // cancello il record corretto in base al flag
      dao.deleteRiduzioneUnsplit(flagControlloInLoco, idProcedimentoOggetto);
      // leggo quello che rimane
      List<RiduzioniSanzioniDTO> riduzioni = dao
          .getElencoRiduzioniSanzioni(idProcedimentoOggetto);

      for (RiduzioniSanzioniDTO r : riduzioni)
      {
        if (r.isSplitted())
        {
          if (r != null && r.getIdProcOggSanzione() != null)
            dao.delete("IUF_T_PROC_OGGETTO_SANZIONE",
                "ID_PROC_OGGETTO_SANZIONE", r.getIdProcOggSanzione());
          else
            dao.delete("IUF_T_PROC_OGGETTO_SANZIONE",
                "ID_PROC_OGGETTO_SANZIONE",
                r.getIdProcOggSanzioneSecondRecordAfterSplit());

          dao.inserisciSanzioneRiduzione(r.getIdTipologia(),
              r.getIdOperazione(),
              r.getIdDescrizioneSecondRecordAfterSplit(), r.getNoteB(),
              r.getImporto(),
              idProcedimentoOggetto);
        }
      }

    }
    catch (Exception e)
    {
      context.setRollbackOnly();
      throw new InternalUnexpectedException(e.getMessage(), e.getCause());
    }
  }

  /**
   * Metodo di utilità da richiamare da altri ejb per aggiornare i totali per
   * livello su IUF_R_PROCEDIMENTO_OGG_LIVEL
   * 
   * @param idProcedimentoOggetto
   * @param dao
   * @throws InternalUnexpectedException
   */
  public static void aggiornaAggregatiContributiLivelloPerAccertamentoSpese(
      final long idProcedimentoOggetto,
      RendicontazioneEAccertamentoSpeseDAO dao, boolean isAcconto)
      throws InternalUnexpectedException
  {
    List<Long> idLivelli = dao
        .getIdLivelliProcOggLivello(idProcedimentoOggetto);
    if (idLivelli != null && !idLivelli.isEmpty())
    {
      if (isAcconto)
      {
        for (Long idLivello : idLivelli)
        {
          dao.updateContributoErogabileNonErogabileAcconto(
              idProcedimentoOggetto, idLivello);
        }
        /**
         * Aggiorno eventuali contributi erogabili negativi con il valore 0. Lo
         * faccio solo in acconto in quanto a saldo viene fatto dal metodo
         * updateContributoErogabileNonErogabileAbbattutoSaldo() prima di
         * calcolare il non erogabile (dato che mi serve l'erogabile come base
         * per il calcolo)
         */
        dao.updateContributoErogabileNegativo(idProcedimentoOggetto);
      }
      else
      {
        for (Long idLivello : idLivelli)
        {
          dao.updateContributoErogabileNonErogabileSaldo(idProcedimentoOggetto,
              idLivello);
        }
      }
    }
    /*
     * Ora devo aggiornare il contributo abbattuto su
     * IUF_T_ACCERTAMENTO_SPESE.
     */
    dao.updateContributoAbbattutoAccertamentoSpese(idProcedimentoOggetto);
    /*
     * Ma per chiudere la partita devo ancora calcolarmi lo scarto sul
     * contributo abbattuto dovuto agli arrotondamenti
     */
    dao.updateContributoAbbattutoPerLivelloDaAccertamentoSpeseEArrotondamenti(
        idProcedimentoOggetto);
  }

  @Override
  public List<TotaleContributoAccertamentoElencoDTO> getTotaleContributoErogabileNonErogabileESanzioniAcconto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getTotaleContributoErogabileNonErogabileESanzioni(
        idProcedimentoOggetto);
  }

  @Override
  public Map<String, BigDecimal[]> getCalcoloImportiPerRendicontazioneSpese(
      long idProcedimentoOggetto,
      Date dataRiferimento,
      List<Long> ids)
      throws InternalUnexpectedException
  {
    return dao.getCalcoloImportiPerRendicontazioneSpese(idProcedimentoOggetto,
        dataRiferimento, ids);
  }

  @Override
  public PotenzialeErogabileESanzioniRendicontazioneDTO getPotenzialeErogabileETotaleSanzioniRendicontazione(
      long idProcedimentoOggetto, boolean isSaldo)
      throws InternalUnexpectedException
  {
    PotenzialeErogabileESanzioniRendicontazioneDTO ps = new PotenzialeErogabileESanzioniRendicontazioneDTO();
    ps.setPotenzialeErogabile(dao
        .getPotenzialeErogabileRendicontazione(idProcedimentoOggetto, isSaldo));
    ps.setTotaleSanzioni(
        dao.getTotaliSanzioniPrecedentementeErogateRendicontazione(
            idProcedimentoOggetto));
    return ps;
  }

  @Override
  public List<RigaProspetto> getElencoProspetto(long idProcedimento,
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getElencoProspetto(idProcedimento, idProcedimentoOggetto);
  }

  @Override
  public BigDecimal getPercentualeMassimaContributoInRendicontazioneSpese(
      long idProcedimento, long idBandoOggetto)
      throws InternalUnexpectedException, ApplicationException
  {
    List<String> valori = dao.getValoriParametroControllo("REN01",
        idBandoOggetto);
    String valore = null;
    if (valori != null)
    {
      if (valori.size() == 1)
      {
        valore = valori.get(0);
      }
      else
      {
        throw new ApplicationException(
            "Errore bloccante grave, si prega di contattare l'assistenza tecnica e comunicare il seguente messaggio: VALORE PARAMETRO per controllo REN01 non univoco");
      }
    }
    if (valore == null)
    {
      throw new ApplicationException(
          "Errore bloccante grave, si prega di contattare l'assistenza tecnica e comunicare il seguente messaggio: VALORE PARAMETRO per controllo REN01 mancante");
    }
    String[] percentuali = valore.split("#");
    if (percentuali.length == 0 || percentuali.length > 2)
    {
      throw new ApplicationException(
          "Errore bloccante grave, si prega di contattare l'assistenza tecnica e comunicare il seguente messaggio: Formato VALORE PARAMETRO per controllo REN01 non corretto, rilevate "
              + percentuali.length + " percentuali");
    }
    if (percentuali.length == 1)
    {
      BigDecimal percentuale = IuffiUtils.NUMBERS
          .getBigDecimal(percentuali[0]);
      if (percentuale == null)
      {
        throw new ApplicationException(
            "Errore bloccante grave, si prega di contattare l'assistenza tecnica e comunicare il seguente messaggio: Formato VALORE PARAMETRO per controllo REN01 non corretto, la prima percentuale non è un numero valido");
      }
      return percentuale;
    }
    else
    {
      // Ci sono 2 percentuali ==> la seconda vale solo per le aziende il cui
      // tipo è indicato nel parametro
      // ID_TP_ENTE_PUBBLICO
      BigDecimal percentuale1 = IuffiUtils.NUMBERS
          .getBigDecimal(percentuali[0]);
      if (percentuale1 == null)
      {
        throw new ApplicationException(
            "Errore bloccante grave, si prega di contattare l'assistenza tecnica e comunicare il seguente messaggio: Formato VALORE PARAMETRO per controllo REN01 non corretto, la prima percentuale non è un numero valido");
      }
      BigDecimal percentuale2 = IuffiUtils.NUMBERS
          .getBigDecimal(percentuali[1]);
      if (percentuale2 == null)
      {
        throw new ApplicationException(
            "Errore bloccante grave, si prega di contattare l'assistenza tecnica e comunicare il seguente messaggio: Formato VALORE PARAMETRO per controllo REN01 non corretto, la seconda percentuale non è un numero valido");
      }
      return dao.isAziendaAttualmenteTipoEntePubblico(idProcedimento)
          ? percentuale2 : percentuale1;
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<RigaJSONRendicontazioneSuperficiDTO> getRendicontazioniSuperficiJSON(
      long idProcedimentoOggetto,
      long idIntervento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoConduzioniJSON]";
    final boolean debugLevel = logger.isDebugEnabled();
    if (debugLevel)
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getElencoConduzioniJSON(idProcedimentoOggetto, idIntervento,
          RigaJSONRendicontazioneSuperficiDTO.class);
    }
    finally
    {
      if (debugLevel)
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public void updateSuperficieEffettivaLocalizzazioneIntervento(
      long idIntervento,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      List<UpdateSuperficieLocalizzazioneDTO> list)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateSuperficieEffettivaLocalizzazioneIntervento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      final long idProcedimentoOggetto = logOperationOggettoQuadroDTO
          .getIdProcedimentoOggetto();
      dao.lockProcedimentoOggetto(idProcedimentoOggetto);
      long idDettIntervProcOgg = forzaInterventoSuTabelleDiWorking(idIntervento,
          idProcedimentoOggetto,
          IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_MODIFICA_QUADRO_ECONOMICO);
      dao.updateSuperficieEffettivaLocalizzazioneIntervento(idDettIntervProcOgg,
          list);
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
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
  public void updateSuperficieIstruttoriaLocalizzazioneIntervento(
      long idIntervento,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      List<UpdateSuperficieLocalizzazioneDTO> list)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateSuperficieIstruttoriaLocalizzazioneIntervento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      final long idProcedimentoOggetto = logOperationOggettoQuadroDTO
          .getIdProcedimentoOggetto();
      dao.lockProcedimentoOggetto(idProcedimentoOggetto);
      Long idDettIntervProcOgg = dao
          .findIdWDettIntervProcOgg(idProcedimentoOggetto, idIntervento);
      dao.updateSuperficieIstruttoriaLocalizzazioneIntervento(
          idDettIntervProcOgg, list);
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  protected long forzaInterventoSuTabelleDiWorking(long idIntervento,
      long idProcedimentoOggetto,
      String flagTipoOperazione)
      throws InternalUnexpectedException
  {
    Long idDettIntervProcOgg = dao
        .findIdWDettIntervProcOgg(idProcedimentoOggetto, idIntervento);
    if (idDettIntervProcOgg == null)
    {
      // Peccato, non è sulla temporanea, devo duplicarlo dal consolidato sulla
      // temporanea.
      Long idDettaglioIntervento = dao.findIdDettaglioMisurazioneIntervento(
          idProcedimentoOggetto, idIntervento);
      // Duplico la IUF_W_DETT_INTERV_PROC_OGG con i dati inseriti dall'utente
      // e quelli del consolidato per i campi che
      // non sono oggetto di modifica a video
      idDettIntervProcOgg = dao.copiaDettaglioInterventoSuTemporaneo(
          idDettaglioIntervento, idProcedimentoOggetto,
          flagTipoOperazione);
      dao.copiaMisurazioneInterventoSuTemporaneo(idDettaglioIntervento,
          idDettIntervProcOgg);
      // e la IUF_W_LOCAL_INTERV_PROC_OGG con le localizzazioni
      dao.copiaLocalizzazioneInterventoSuTemporaneo(idDettaglioIntervento,
          idDettIntervProcOgg);
      dao.copiaAllegatiInterventoSuTemporaneo(idDettaglioIntervento,
          idDettIntervProcOgg, null);
    }
    return idDettIntervProcOgg;
  }

  @Override
  public void importaSuperficiGIS(
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, int anno)
      throws InternalUnexpectedException, ApplicationException
  {
    final long idProcedimentoOggetto = logOperationOggettoQuadroDTO
        .getIdProcedimentoOggetto();
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    List<RigaAccertamentoSpese> elenco = getElencoAccertamentoSpese(
        idProcedimentoOggetto, null);
    if (elenco == null || elenco.isEmpty())
    {
      throw new ApplicationException(
          "Non sono stati trovati interventi su cui eseguire l'importazione");
    }
    for (RigaAccertamentoSpese riga : elenco)
    {
      if (riga
          .getIdTipoLocalizzazione() == IuffiConstants.INTERVENTI.LOCALIZZAZIONE.PARTICELLE_AZIENDALI_IMPIANTI_BOSCHIVI)
      {
        long idDettIntervProcOgg = forzaInterventoSuTabelleDiWorking(
            riga.getIdIntervento(), idProcedimentoOggetto,
            IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_MODIFICA_QUADRO_ECONOMICO);
        List<UpdateSuperficieLocalizzazioneDTO> superficiGis = dao
            .getRipartizioneSuperficiGisSuLocalizzazione(idDettIntervProcOgg,
                anno);
        dao.updateSuperficiGisLocalizzazione(idDettIntervProcOgg, superficiGis);
      }
    }
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public Map<Long, InterventoRendicontazioneDocumentiSpesaDTO> getRendicontazioneDocumentiSpesaPerIntervento(
      long idProcedimentoOggetto, List<Long> idIntervento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRendicontazioneDocumentiSpesaPerIntervento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getRendicontazioneDocumentiSpesaPerIntervento(
          idProcedimentoOggetto, idIntervento);
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
  public void updateRendicontazioneSpeseDocumenti(
      Map<Long, InterventoRendicontazioneDocumentiSpesaDTO> interventi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException, ApplicationException
  {
    final String THIS_METHOD = "updateRendicontazioneSpeseDocumenti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      long idProcedimentoOggetto = logOperationOggettoQuadroDTO
          .getIdProcedimentoOggetto();
      HashMap<Long, Long> mapIdDocumentiSpesaDaAggiornare = new HashMap<>();
      HashMap<Long, Long> mapIdDocumentiSpesaDaEliminare = new HashMap<>();
      for (InterventoRendicontazioneDocumentiSpesaDTO intervento : interventi
          .values())
      {
        for (RigaRendicontazioneDocumentiSpesaDTO riga : intervento
            .getRendicontazione())
        {
          if (BigDecimal.ZERO.compareTo(riga.getImportoRendicontato()) == 0)
          {
            final long idDocumentoSpesa = riga.getIdDocumentoSpesa();
            mapIdDocumentiSpesaDaEliminare.put(idDocumentoSpesa,
                idDocumentoSpesa);
            dao.deleteRendicontazioneSpeseDocumenti(idProcedimentoOggetto,
                riga.getIdDocumentoSpesaInterven());
          }
          else
          {
            final long idDocumentoSpesa = riga.getIdDocumentoSpesa();
            mapIdDocumentiSpesaDaAggiornare.put(idDocumentoSpesa,
                idDocumentoSpesa);
            dao.updateOrInsertRendicontazioneSpeseDocumenti(
                idProcedimentoOggetto, riga,
                logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
          }
          if (!dao.checkIntegrityRendicontazioneSpeseDocumenti(
              idProcedimentoOggetto,
              riga.getIdDocumentoSpesaInterven()))
          {
            // Per qualche motivo la somma di
            // IUF_R_DOC_SPESA_INT_PROC_OGG.IMPORTO_RENDICONTATO per lo stesso
            // ID_DOCUMENTO_SPESA_INTERVEN supera l'importo di
            // IUF_R_DOCUMENTO_SPESA_INTERV ==> Può essere un problema
            // di concorrenza o di altra natura ma non è una situazione
            // ammissibile! ==> ROLLBACK e segnalazione
            // all'utente
            throw new ApplicationException(
                "Errore nella rendicontazione per l'intervento "
                    + intervento.getDescIntervento()
                    + " sul documento spesa numero "
                    + riga.getNumeroDocumentoSpesa()
                    + ": gli importi rendicontati superano l'importo disponibile");
          }
        }
        final long idIntervento = intervento.getIdIntervento();
        dao.updateOrInsertRendicontazioneSpesePerModificaDocumentiDiRendicontazione(
            idProcedimentoOggetto, idIntervento,
            intervento.getFlagInterventoCompletato(), intervento.getNote());
        dao.ricalcolaContributoRichiesto(idProcedimentoOggetto, idIntervento);
      }
      /**
       * Integrazione con i file di spesa (tabella IUF_R_DOC_SPESA_PROC_OGG)
       */
      final ArrayList<Long> idsEliminazione = new ArrayList<>(
          mapIdDocumentiSpesaDaEliminare.keySet());
      // Gli id da cancellare sono TUTTI quelli interessati, e poi si
      // inseriranno solo quelli con importo > 0
      final ArrayList<Long> idsAggiornamento = new ArrayList<>(
          mapIdDocumentiSpesaDaAggiornare.keySet());
      idsEliminazione.addAll(idsAggiornamento);
      if (idsEliminazione != null && idsEliminazione.size() > 0)
      {
        dao.deleteDocSpesaProcOggPerProcedimentoOggettoByIdDocumentoSpesaList(
            idProcedimentoOggetto, idsEliminazione);
      }
      if (idsAggiornamento.size() > 0)
      {
        dao.insertDocSpesaProcOgg(idProcedimentoOggetto, idsAggiornamento);
      }
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
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
  public DocumentoAllegatoDownloadDTO getDocumentoSpesaCorrenteByIdProcedimentoOggetto(
      long idProcedimentoOggetto, long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDocumentoSpesaCorrenteByIdProcedimentoOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getDocumentoSpesaCorrenteByIdProcedimentoOggetto(
          idProcedimentoOggetto, idDocumentoSpesa);
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
  public DocumentoAllegatoDownloadDTO getDocumentoSpesaInRendicontazioneByIdProcedimentOggettoIstruttoria(
      long idProcedimentoOggetto, long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDocumentoSpesaInRendicontazioneByIdProcedimentOggettoIstruttoria";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao
          .getDocumentoSpesaInRendicontazioneByIdProcedimentOggettoIstruttoria(
              idProcedimentoOggetto, idDocumentoSpesa);
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
  public String getInfoSePossibileRendicontazioneConIVAByIdProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getInfoSePossibileRendicontazioneConIVAByIdProcedimentoOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getInfoSePossibileRendicontazioneConIVAByIdProcedimentoOggetto(
          idProcedimentoOggetto);
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
  public Map<Long, InterventoAccertamentoDocumentiSpesaDTO> getAccertamentoDocumentiSpesaPerIntervento(
      long idProcedimentoOggetto,
      List<Long> idIntervento) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getAccertamentoDocumentiSpesaPerIntervento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getAccertamentoDocumentiSpesaPerIntervento(
          idProcedimentoOggetto, idIntervento);
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
  public void updateAccertamentoSpeseDocumenti(
      Map<Long, InterventoAccertamentoDocumentiSpesaDTO> interventi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException, ApplicationException
  {
    final String THIS_METHOD = "updateAccertamentoSpeseDocumenti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      List<AccertamentoSpeseDTO> listaAccertamenti = new ArrayList<>();
      AccertamentoSpeseDTO acccertamento = null;
      for (InterventoAccertamentoDocumentiSpesaDTO intervento : interventi
          .values())
      {
        acccertamento = new AccertamentoSpeseDTO();
        acccertamento.setIdIntervento(intervento.getIdIntervento());
        final long idProcedimentoOggetto = logOperationOggettoQuadroDTO
            .getIdProcedimentoOggetto();
        acccertamento.setIdProcedimentoOggetto(idProcedimentoOggetto);
        acccertamento.setFlagInterventoCompletato(
            intervento.getFlagInterventoCompletato());
        acccertamento.setNote(intervento.getNote());
        listaAccertamenti.add(acccertamento);
        BigDecimal contributoCalcolato = BigDecimal.ZERO;
        BigDecimal importoAccertato = BigDecimal.ZERO;
        BigDecimal importoCalcoloContributo = BigDecimal.ZERO;
        BigDecimal importoDisponibile = BigDecimal.ZERO;
        BigDecimal importoNonRiconosciuto = BigDecimal.ZERO;
        for (RigaAccertamentoDocumentiSpesaDTO riga : intervento
            .getAccertamento())
        {
          // Aggiorno dati per il documento spesa
          contributoCalcolato = IuffiUtils.NUMBERS.add(contributoCalcolato,
              riga.getImportoCalcoloContributoBD());
          importoAccertato = IuffiUtils.NUMBERS.add(importoAccertato,
              riga.getImportoAccertatoBD());
          importoCalcoloContributo = IuffiUtils.NUMBERS.add(
              importoCalcoloContributo,
              riga.getImportoCalcoloContributoBD());
          importoDisponibile = IuffiUtils.NUMBERS.add(importoDisponibile,
              riga.getImportoDisponibileBD());
          importoNonRiconosciuto = IuffiUtils.NUMBERS
              .add(importoNonRiconosciuto, riga.getImportoNonRiconosciutoBD());
          dao.updateOrInsertAccertamentoSpeseDocumenti(idProcedimentoOggetto,
              riga,
              logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
        }
        contributoCalcolato = IuffiUtils.NUMBERS.multiply(
            contributoCalcolato, intervento.getPercentualeContributo())
            .scaleByPowerOfTen(-2);
        acccertamento.setContributoCalcolato(contributoCalcolato);
        acccertamento.setImportoAccertato(importoAccertato);
        acccertamento.setImportoCalcoloContributo(importoCalcoloContributo);
        acccertamento.setImportoDisponibile(
            intervento.getImportoNonRiconosciutoNonSanzionabile());
        acccertamento.setImportoNonRiconosciuto(
            intervento.getImportoNonRiconosciutoSanzionabile());
      }
      // aggiorno dati relativi all'accertamento
      updateAccertamentoSpeseAcconto(listaAccertamenti,
          logOperationOggettoQuadroDTO);
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
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
  public void updateAccertamentoSpeseSaldoDocumenti(
      Map<Long, InterventoAccertamentoDocumentiSpesaDTO> interventi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateAccertamentoSpeseSaldoDocumenti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      List<AccertamentoSpeseDTO> listaAccertamenti = new ArrayList<>();
      AccertamentoSpeseDTO acccertamento = null;
      for (InterventoAccertamentoDocumentiSpesaDTO intervento : interventi
          .values())
      {
        acccertamento = new AccertamentoSpeseDTO();
        acccertamento.setIdIntervento(intervento.getIdIntervento());
        final long idProcedimentoOggetto = logOperationOggettoQuadroDTO
            .getIdProcedimentoOggetto();
        acccertamento.setIdProcedimentoOggetto(idProcedimentoOggetto);
        acccertamento.setFlagInterventoCompletato(
            intervento.getFlagInterventoCompletato());
        acccertamento.setNote(intervento.getNote());
        listaAccertamenti.add(acccertamento);
        BigDecimal contributoCalcolato = BigDecimal.ZERO;
        BigDecimal importoAccertato = BigDecimal.ZERO;
        BigDecimal importoCalcoloContributo = BigDecimal.ZERO;
        BigDecimal importoDisponibile = BigDecimal.ZERO;
        BigDecimal importoNonRiconosciuto = BigDecimal.ZERO;
        for (RigaAccertamentoDocumentiSpesaDTO riga : intervento
            .getAccertamento())
        {
          // Aggiorno dati per il documento spesa
          contributoCalcolato = IuffiUtils.NUMBERS.add(contributoCalcolato,
              riga.getImportoCalcoloContributoBD());
          importoAccertato = IuffiUtils.NUMBERS.add(importoAccertato,
              riga.getImportoAccertatoBD());
          importoCalcoloContributo = IuffiUtils.NUMBERS.add(
              importoCalcoloContributo,
              riga.getImportoCalcoloContributoBD());
          importoDisponibile = IuffiUtils.NUMBERS.add(importoDisponibile,
              riga.getImportoDisponibileBD());
          importoNonRiconosciuto = IuffiUtils.NUMBERS
              .add(importoNonRiconosciuto, riga.getImportoNonRiconosciutoBD());
          dao.updateOrInsertAccertamentoSpeseDocumenti(idProcedimentoOggetto,
              riga,
              logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
        }
        contributoCalcolato = IuffiUtils.NUMBERS.multiply(
            contributoCalcolato, intervento.getPercentualeContributo())
            .scaleByPowerOfTen(-2);
        acccertamento.setContributoCalcolato(contributoCalcolato);
        acccertamento.setImportoAccertato(importoAccertato);
        acccertamento.setImportoCalcoloContributo(importoCalcoloContributo);
        acccertamento.setImportoDisponibile(
            intervento.getImportoNonRiconosciutoNonSanzionabile());
        acccertamento.setImportoNonRiconosciuto(
            intervento.getImportoNonRiconosciutoSanzionabile());
      }
      // aggiorno dati relativi all'accertamento
      updateAccertamentoSpeseSaldo(listaAccertamenti,
          logOperationOggettoQuadroDTO);
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

}
