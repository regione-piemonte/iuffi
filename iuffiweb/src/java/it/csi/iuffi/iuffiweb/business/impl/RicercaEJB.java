package it.csi.iuffi.iuffiweb.business.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.AmmCompetenzaDTO;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.FiltroVO;
import it.csi.iuffi.iuffiweb.dto.FocusAreaDTO;
import it.csi.iuffi.iuffiweb.dto.GraduatoriaDTO;
import it.csi.iuffi.iuffiweb.dto.GravitaNotificaVO;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.IterProcedimentoGruppoDTO;
import it.csi.iuffi.iuffiweb.dto.NotificaDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.dto.RicercaProcedimentiVO;
import it.csi.iuffi.iuffiweb.dto.SettoriDiProduzioneDTO;
import it.csi.iuffi.iuffiweb.dto.VisibilitaDTO;
import it.csi.iuffi.iuffiweb.dto.gestioneeventi.EventiDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.ProcedimentoGruppoVO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.RicercaDAO;
import it.csi.papua.papuaserv.dto.gestioneutenti.MacroCU;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Stateless()
@EJB(name = "java:app/Ricerca", beanInterface = IRicercaEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RicercaEJB extends IuffiAbstractEJB<RicercaDAO>
    implements IRicercaEJB
{

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<LivelloDTO> getLivelliAttivi(int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    return dao.getLivelliAttivi(idProcedimentoAgricolo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<BandoDTO> getBandiAttivi(
		  long[] lIdLivelli,long[] lIdEventi, int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getBandiAttivi(lIdLivelli, lIdEventi, idProcedimentoAgricolo);
  }
  
  @Override
  public List<EventiDTO> getEventiAttivi(long[] idLivelli, int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
  	return dao.getEventiAttivi(idLivelli, idProcedimentoAgricolo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<AmmCompetenzaDTO> getAmministrazioniAttive(
      long[] lIdBando) throws InternalUnexpectedException
  {
    return dao.getAmministrazioniAttive(lIdBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ProcedimentoDTO> getStatiProcedimentiAttivi(
     long[] lIdLivelli, 
     long[] lIdBando,
     long[] lIdAmministrazioni) throws InternalUnexpectedException
  {
    return dao.getStatiProcedimentiAttivi(lIdLivelli, lIdBando,
        lIdAmministrazioni);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<Long> searchIdProcedimenti(RicercaProcedimentiVO vo,
      UtenteAbilitazioni utenteAbilitazioni, String orderColumn,
      String orderType) throws InternalUnexpectedException
  {
    return dao.searchIdProcedimenti(vo, utenteAbilitazioni, orderColumn,
        orderType);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ProcedimentoOggettoVO> getDettaglioProcedimentiOggettiById(
      Vector<Long> vIdProcedimento) throws InternalUnexpectedException
  {
    return dao.getDettaglioProcedimentiOggettiById(vIdProcedimento);

  }

  /*
   * @Override
   * 
   * @TransactionAttribute(value = TransactionAttributeType.SUPPORTS) public
   * List<OggettoDTO> getOggettiProcedimentiAttivi(Vector<String>
   * lIdLivelli,Vector<String> lIdBando,Vector<String> lIdAmministrazioni,
   * Vector<String> lIdStati, HashMap<Long, Vector<Long>> statiGruppiMap, String
   * tipoFiltroGruppi) throws InternalUnexpectedException { return
   * dao.getOggettiProcedimentiAttivi(lIdLivelli, lIdBando, lIdAmministrazioni,
   * lIdStati, statiGruppiMap, tipoFiltroGruppi); }
   */
  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getOggettiProcedimentiAttivi(
      long[] lIdLivelli, long[] lIdBando,
      long[] lIdAmministrazioni, long[] lIdStati,
      UtenteAbilitazioni utenteAbilitazioni) throws InternalUnexpectedException
  {
    return dao.getOggettiProcedimentiAttivi(lIdLivelli, lIdBando,
        lIdAmministrazioni, lIdStati, utenteAbilitazioni);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ComuneDTO> searchComuni(String prov, String descrComune)
      throws InternalUnexpectedException
  {
    return dao.searchComuniPiemonte(prov, descrComune);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoOggetti(long idProcedimento,
      List<MacroCU> lMacroCdu, boolean isPerBeneficiarioOCAA, int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getElencoOggetti(idProcedimento, lMacroCdu, Boolean.FALSE,
        isPerBeneficiarioOCAA, idProcedimentoAgricolo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ProcedimentoOggettoVO> searchProcedimenti(
      RicercaProcedimentiVO ricercaVO, UtenteAbilitazioni utenteAbilitazioni)
      throws InternalUnexpectedException
  {
    return dao.searchProcedimenti(ricercaVO, utenteAbilitazioni);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoOggettiDisponibili(long idBando,
      boolean flagIstanza, Long idGruppoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getElencoOggettiDisponibili(idBando, flagIstanza,
        idGruppoOggetto);
  }

  @Override
  public List<BandoDTO> getElencoBandi(String attore, int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getElencoBandi(attore, idProcedimentoAgricolo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GraduatoriaDTO> getGraduatorieBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getGraduatorieBando(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<FileAllegatoDTO> getAllegatiGraduatoria(long idGraduatoria)
      throws InternalUnexpectedException
  {
    return dao.getAllegatiGraduatoria(idGraduatoria);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public FileAllegatoDTO getFileAllegatoGraduatoria(long idAllegatiGraduatoria)
      throws InternalUnexpectedException
  {
    return dao.getFileAllegatoGraduatoria(idAllegatiGraduatoria);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<LivelloDTO> getLivelliBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getLivelliBando(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<LivelloDTO> getElencoLivelli() throws InternalUnexpectedException
  {
    return dao.getElencoLivelli();
  }
  
  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<LivelloDTO> getElencoLivelliByProcedimentoAgricolo(int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    return dao.getElencoLivelliByProcedimentoAgricolo(idProcedimentoAgricolo);
  }


  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<NotificaDTO> getNotifiche(long idProcedimento, String attore)
      throws InternalUnexpectedException
  {
    return dao.getNotifiche(idProcedimento, attore);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public NotificaDTO getNotificaById(long idNotifica)
      throws InternalUnexpectedException
  {
    return dao.getNotificaById(idNotifica);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<VisibilitaDTO> getElencoVisibilitaNotifiche(String attore)
      throws InternalUnexpectedException
  {
    return dao.getElencoVisibilitaNotifiche(attore);
  }

  @Override
  public void updateNotifica(long idNotifica, NotificaDTO notifica,
      long idProcedimento) throws InternalUnexpectedException
  {
    dao.deleteNotifica(idNotifica);
    dao.insertNotifica(notifica, idProcedimento, false);
  }

  @Override
  public void insertNuovaNotifica(NotificaDTO notificaNew, long idProcedimento)
      throws InternalUnexpectedException
  {
    dao.insertNotifica(notificaNew, idProcedimento, true);
  }

  @Override
  public void eliminaNotifica(long idNotifica)
      throws InternalUnexpectedException
  {
    dao.deleteNotifica(idNotifica);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<LivelloDTO> getMisure() throws InternalUnexpectedException
  {
    return dao.getMisure();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<LivelloDTO> getOperazioni(Vector<Long> idMisureSelezionate)
      throws InternalUnexpectedException
  {
    return dao.getOperazioni(idMisureSelezionate);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<SettoriDiProduzioneDTO> getElencoSettoriBandi()
      throws InternalUnexpectedException
  {
    return dao.getElencoSettoriBandi();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<FocusAreaDTO> getElencoFocusAreaBandi(int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getElencoFocusAreaBandi(idProcedimentoAgricolo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<FocusAreaDTO> getElencoFocusArea(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoFocusArea(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<SettoriDiProduzioneDTO> getElencoSettori(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoSettori(idBando);
  }

  @Override
  public List<ProcedimentoOggettoVO> searchProcedimentiEstrazione(
      Long idEstrazione, UtenteAbilitazioni utenteAbilitazioni,
      HashMap<String, FiltroVO> mapFilters, String limit, String offset)
      throws InternalUnexpectedException
  {
    List<Long> elencoProcedimentiEstratti = dao
        .elencoIdProcedimentiEstratti(idEstrazione, limit, offset, mapFilters);
    List<Long> elencoProcedimentiOggettoEstratti = dao
        .elencoIdProcedimentiOggettoEstratti(idEstrazione, limit, offset,
            mapFilters);
    List<Long> elencoProcedimentiEstrattiExPost = dao
        .elencoIdProcedimentiEstrattiExPost(idEstrazione, limit, offset,
            mapFilters);

    if (elencoProcedimentiEstratti == null
        && elencoProcedimentiOggettoEstratti == null
        && elencoProcedimentiEstrattiExPost == null
        )
      return null;

    List<ProcedimentoOggettoVO> lista = dao.searchProcedimentiEstrazione(
        elencoProcedimentiEstratti, elencoProcedimentiOggettoEstratti,
        elencoProcedimentiEstrattiExPost,
        null, utenteAbilitazioni, mapFilters,
        limit, offset);
    return lista;
  }

  @Override
  public int searchProcedimentiEstrazioneCount(Long idEstrazione,
      UtenteAbilitazioni utenteAbilitazioni,
      HashMap<String, FiltroVO> mapFilters)
      throws InternalUnexpectedException
  {
    List<Long> elencoProcedimentiEstratti = dao
        .elencoIdProcedimentiEstratti(idEstrazione, null, null, mapFilters);
    List<Long> elencoProcedimentiOggettoEstratti = dao
        .elencoIdProcedimentiOggettoEstratti(idEstrazione, null, null,
            mapFilters);
    List<Long> elencoProcedimentiEstrattiExPost = dao
        .elencoIdProcedimentiEstrattiExPost(idEstrazione, null, null,
            mapFilters);
    
    if (elencoProcedimentiOggettoEstratti != null
        && !elencoProcedimentiOggettoEstratti.isEmpty())
      return elencoProcedimentiOggettoEstratti.size();
    if (elencoProcedimentiEstratti != null)
      return elencoProcedimentiEstratti.size();
    if (elencoProcedimentiEstrattiExPost != null)
      return elencoProcedimentiEstrattiExPost.size();

    return 0;
  }

  @Override
  public List<Long> elencoIdProcedimentiEstratti(
      UtenteAbilitazioni utenteAbilitazioni, long idEstrazione)
      throws InternalUnexpectedException
  {
    return dao.elencoIdProcedimentiEstratti(idEstrazione, null, null, null);
  }

  @Override
  public List<Long> elencoIdProcedimentiEstrattiExPost(
      UtenteAbilitazioni utenteAbilitazioni, long idEstrazione)
      throws InternalUnexpectedException
  {
    return dao.elencoIdProcedimentiEstrattiExPost(idEstrazione, null, null,
        null);
  }

  @Override
  public List<Long> elencoIdProcedimentiOggettoEstrattiExPost(
      UtenteAbilitazioni utenteAbilitazioni, long idEstrazione)
      throws InternalUnexpectedException
  {
    return dao.elencoIdProcedimentiOggettoEstrattiExPost(idEstrazione, null,
        null, null);
  }

  @Override
  public List<Long> elencoIdProcedimentiOggettoEstratti(
      UtenteAbilitazioni utenteAbilitazioni, long idEstrazione)
      throws InternalUnexpectedException
  {
    return dao.elencoIdProcedimentiOggettoEstratti(idEstrazione, null, null,
        null);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ProcedimentoOggettoVO> getElencoTipologieEstrazione()
      throws InternalUnexpectedException
  {
    return dao.getElencoTipologieEstrazione();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getGestoriFascicolo(long idBando,
      long idIntermediario) throws InternalUnexpectedException
  {
    return dao.getGestoriFascicolo(idBando, idIntermediario);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<LivelloDTO> getMisureConFlagTipo(int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getMisureConFlagTipo(idProcedimentoAgricolo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<Long>> getTipiMisure(int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getTipiMisure(idProcedimentoAgricolo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<LivelloDTO> getOperazioniMisure(Vector<Long> vect,
      String codiceMisura) throws InternalUnexpectedException
  {
    return dao.getOperazioniMisureByTipo(vect, codiceMisura);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GravitaNotificaVO> getElencoGravitaNotifica()
      throws InternalUnexpectedException
  {
    return dao.getElencoGravitaNotifica();
  }

  @Override
  public DecodificaDTO<String> findProtocolloPratica(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.findProtocolloPratica(idProcedimentoOggetto);
  }

  @Override
  public DecodificaDTO<String> findProtocolloPratica(long idProcedimentoOggetto,
      long idCategoriaDocLettereIuffiWebSuDoquiagri)
      throws InternalUnexpectedException
  {
    return dao.findProtocolloPratica(idProcedimentoOggetto,
        idCategoriaDocLettereIuffiWebSuDoquiagri);
  }

  @Override
  public List<ProcedimentoDTO> getElencoProcedimentiRegistroFatture(
      long[] idsTipologiaDocumento, long[] idsModalitaPagamento,
      Long idFornitore,
      Date dataDa, Date dataA) throws InternalUnexpectedException
  {
    return dao.getElencoProcedimentiRegistroFatture(idsTipologiaDocumento,
        idsModalitaPagamento, idFornitore, dataDa, dataA);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoOggettiChiusi(long idProcedimento,
      List<MacroCU> lMacroCdu, boolean isBeneficiarioOCAA, int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getElencoOggetti(idProcedimento, lMacroCdu, Boolean.TRUE,
        isBeneficiarioOCAA, idProcedimentoAgricolo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<IterProcedimentoGruppoDTO> getIterGruppoOggetto(
      long idProcedimento, long codRaggruppamento)
      throws InternalUnexpectedException
  {
    return dao.getIterGruppoOggetto(idProcedimento, codRaggruppamento);
  }

  //TODO: FIXME: rimuovere, probabilmente in disuso
  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getStatiAmmProcedimentiAttivi(
      Vector<Long> lIdLivelli, Vector<Long> lIdBando,
      Vector<Long> lIdAmministrazioni, Vector<Long> lIdStatiProc)
      throws InternalUnexpectedException
  {
    return dao.getStatiAmmProcedimentiAttivi(lIdLivelli, lIdBando,
        lIdAmministrazioni, lIdStatiProc);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getElencoDescrizioniGruppi()
      throws InternalUnexpectedException
  {
    return dao.getElencoDescrizioniGruppi();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getElencoDescrizioneStatiOggetti()
      throws InternalUnexpectedException
  {
    return dao.getElencoDescrizioneStatiOggetti();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ProcedimentoGruppoVO> getElencoProcedimentoGruppo(
      long idProcedimento, long codiceRaggruppamento)
      throws InternalUnexpectedException
  {
    return dao.getElencoProcedimentoGruppo(idProcedimento,
        codiceRaggruppamento);
  }

  @Override
  public void sbloccaGruppoOggetto(long idProcedimento, long codRaggruppamento,
      String note, Long idUtenteLogin)
      throws InternalUnexpectedException, ApplicationException
  {
    dao.lockProcedimento(idProcedimento);
    if (dao.isGruppoOggettoBloccato(idProcedimento, codRaggruppamento))
    {
      dao.storicizzaBloccoGruppoOggetto(idProcedimento, codRaggruppamento);
      dao.inserisciSbloccoGruppoOggetto(idProcedimento, codRaggruppamento, note,
          idUtenteLogin);
    }
    else
    {
      throw new ApplicationException(
          "impossibile effettuare l'operazione in quanto il gruppo selezionato non è bloccato");
    }

  }

  @Override
  public DecodificaDTO<String> findProtocolloPraticaByIdPOAndCodice(long idProcedimentoOggetto, long idTipoDoc)
	  throws InternalUnexpectedException {
      return dao.findProtocolloPraticaByIdPOAndCodice(idProcedimentoOggetto, idTipoDoc);
  }

  @Override
  public HashMap<Long, List<Long>> getAziendeBandiProfessionista(UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo)
	  throws InternalUnexpectedException {
      return dao.getAziendeBandiProfessionista(utenteAbilitazioni, idProcedimentoAgricolo);
  }

  @Override
  public List<Long> getIdBandiPerAziendaProfessionista(UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo)
	  throws InternalUnexpectedException {
      return dao.getIdBandiPerAziendaProfessionista(utenteAbilitazioni, idProcedimentoAgricolo);
  }

  @Override
  public String getNomeCognomeProfessionista(UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo)
	  throws InternalUnexpectedException {
      return dao.getNomeCognomeProfessionista(utenteAbilitazioni, idProcedimentoAgricolo);
  }

  @Override
  public HashMap<String, List<String>> getAziendeBandiProfessionistaPerRiepilogo(
	  UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo) throws InternalUnexpectedException {
      return dao.getAziendeBandiProfessionistaPerRiepilogo(utenteAbilitazioni, idProcedimentoAgricolo);
  }

}
