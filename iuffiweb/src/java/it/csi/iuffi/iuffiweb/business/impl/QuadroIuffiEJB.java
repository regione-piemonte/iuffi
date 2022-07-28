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

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.AnnoCensitoDTO;
import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.DocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.dto.IpotesiPrelievoDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.dto.PianoSelettivoDTO;
import it.csi.iuffi.iuffiweb.dto.PraticaConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.dto.ReferenteProgettoDTO;
import it.csi.iuffi.iuffiweb.dto.SezioneDocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.dto.SoggettoDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDettaglioPlvDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.ProduzioneCategoriaAnimaleDTO;
import it.csi.iuffi.iuffiweb.dto.assicurazionicolture.AssicurazioniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.UtilizzoReseDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DannoDTO;
import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.UnitaMisuraDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.AccertamentoDannoDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoDaFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.IstitutoDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.ParticelleFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.RiepilogoDannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.dannicolture.DanniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.datibilancio.DatiBilancioDTO;
import it.csi.iuffi.iuffiweb.dto.fabbricati.FabbricatiDTO;
import it.csi.iuffi.iuffiweb.dto.gestionelog.LogDTO;
import it.csi.iuffi.iuffiweb.dto.licenzapesca.ImportoLicenzaDTO;
import it.csi.iuffi.iuffiweb.dto.licenzapesca.VersamentoLicenzaDTO;
import it.csi.iuffi.iuffiweb.dto.motoriagricoli.MotoriAgricoliDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.prestitiagrari.PrestitiAgrariDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali.CaratteristicheAziendali;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.DataCensimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.Distretto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.RicercaDistretto;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDTO;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.ControlloColturaDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioParticellareDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioPsrDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColturePlvVegetaleDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureRiepilogoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.QuadroIuffiDAO;
import it.csi.iuffi.iuffiweb.integration.ws.regata.DatiAziendaDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

@Stateless()
@EJB(name = "java:app/QuadroIuffi", beanInterface = IQuadroIuffiEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class QuadroIuffiEJB extends IuffiAbstractEJB<QuadroIuffiDAO>
    implements IQuadroIuffiEJB
{
  @SuppressWarnings("unused")
  private SessionContext sessionContext;

  @Resource
  private void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  @Override
  public List<ScorteDTO> getListaScorteByProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    List<ScorteDTO> listaScorte = null;
    listaScorte = dao.getListScorteByProcedimentoOggetto(idProcedimentoOggetto);
    if (listaScorte == null)
    {
      listaScorte = new ArrayList<ScorteDTO>();
    }
    return listaScorte;
  }

  @Override
  public List<ScorteDTO> getListaScorteNonDanneggiateByProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<ScorteDTO> listaScorte = null;
    listaScorte = dao.getListScorteNonDanneggiateByProcedimentoOggetto(
        idProcedimentoOggetto);
    if (listaScorte == null)
    {
      listaScorte = new ArrayList<ScorteDTO>();
    }
    return listaScorte;
  }

  @Override
  public long getIdStatoProcedimento(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    long idStatoProcedimento = dao
        .getIdStatoProcedimento(idProcedimentoOggetto);
    return idStatoProcedimento;
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoTipologieScorte()
      throws InternalUnexpectedException
  {
    return dao.getElencoTipologieScorte();
  }

  @Override
  public List<DecodificaDTO<Long>> getListUnitaDiMisura()
      throws InternalUnexpectedException
  {
    return dao.getListUnitaDiMisura();
  }

  @Override
  public Long getUnitaMisuraByScorta(long idScorta)
      throws InternalUnexpectedException
  {
    return dao.getUnitaMisuraByScorta(idScorta);
  }

  @Override
  public long getIdScorteAltro() throws InternalUnexpectedException
  {
    return dao.getIdScorteAltro();
  }

  @Override
  public long inserisciScorte(long idProcedimentoOggetto, long idScorta,
      BigDecimal quantita, Long idUnitaMisura, String descrizione,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    long idScortaMagazzino = dao.inserisciScorte(idProcedimentoOggetto,
        idScorta, quantita, idUnitaMisura, descrizione);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return idScortaMagazzino;
  }

  @Override
  public long modificaScorte(List<ScorteDTO> listScorte,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    long returnValue;
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    List<Long> arrayIdScortaMagazzino = new ArrayList<Long>();
    for (int i = 0; i < listScorte.size(); i++)
    {
      ScorteDTO scorta = listScorte.get(i);
      arrayIdScortaMagazzino.add(scorta.getIdScortaMagazzino());
    }
    long nInterventi = dao.getNInterventiAssociatiDanniScorte(
        idProcedimentoOggetto, arrayIdScortaMagazzino);
    if (nInterventi > 0L)
    {
      returnValue = IuffiConstants.ERRORI.ELIMINAZIONE_SCORTE_CON_DANNI_CON_INTERVENTI;
    }
    else
    {
      dao.eliminaDanniAssociatiAlleScorteMagazzinoModificateORimosse(
          arrayIdScortaMagazzino, idProcedimentoOggetto);
      for (ScorteDTO scorta : listScorte)
      {
        dao.modificaScorta(scorta, idProcedimentoOggetto);
      }
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
      returnValue = listScorte.size();
    }
    return returnValue;
  }

  @Override
  public ScorteDTO getScortaByIdScortaMagazzino(long idScortaMagazzino)
      throws InternalUnexpectedException
  {
    return dao.getScortaByIdScortaMagazzino(idScortaMagazzino);
  }

  @Override
  public long eliminaScorte(List<Long> listIdScortaMagazzino,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      long idProcedimentoOggetto)
      throws InternalUnexpectedException, ApplicationException
  {
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    long nInterventi = dao.getNInterventiAssociatiDanniScorte(
        idProcedimentoOggetto, listIdScortaMagazzino);
    if (nInterventi == 0L)
    {
      long nScorteMagazzinoRimosse = dao
          .eliminaScorteMagazzino(listIdScortaMagazzino, idProcedimentoOggetto);
      dao.eliminaDanniAssociatiAlleScorteMagazzinoModificateORimosse(
          listIdScortaMagazzino, idProcedimentoOggetto);
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
      return nScorteMagazzinoRimosse;
    }
    else
    {
      return IuffiConstants.ERRORI.ELIMINAZIONE_SCORTE_CON_DANNI_CON_INTERVENTI;
    }
  }

  @Override
  public List<DanniDTO> getListaDanniByProcedimentoOggetto(
      long idProcedimentoOggetto, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    List<DanniDTO> listaDanniByProcedimentoOggetto = dao
        .getListDanniByProcedimentoOggettoAndArrayIdDannoAtm(null,
            idProcedimentoOggetto, idProcedimentoAgricoltura);
    if (listaDanniByProcedimentoOggetto == null)
    {
      listaDanniByProcedimentoOggetto = new ArrayList<DanniDTO>();
    }
    return listaDanniByProcedimentoOggetto;
  }

  @Override
  public long eliminaDanni(List<Long> listIdDannoAtm,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      long idProcedimentoOggetto)
      throws InternalUnexpectedException, ApplicationException
  {
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    long[] arrayIdDannoAtm = new long[listIdDannoAtm.size()];
    for (int i = 0; i < listIdDannoAtm.size(); i++)
    {
      arrayIdDannoAtm[i] = listIdDannoAtm.get(i);
    }
    long nInterventiDanni = dao
        .getNInterventiAssociatiDanni(idProcedimentoOggetto, arrayIdDannoAtm);
    if (nInterventiDanni > 0L)
    {
      return IuffiConstants.ERRORI.ELIMINAZIONE_DANNI_CON_INTERVENTI;
    }
    List<DanniDTO> danniConduzioni = dao
        .getListDanniConduzioni(idProcedimentoOggetto, arrayIdDannoAtm);
    List<Long> listIdDannoAtmConduzioni = new ArrayList<Long>();
    if (danniConduzioni != null)
    {
      for (DanniDTO d : danniConduzioni)
      {
        listIdDannoAtmConduzioni.add(d.getIdDannoAtm());
      }
      dao.eliminaDanniConduzioniFromTParticellaDanneggiata(
          idProcedimentoOggetto, listIdDannoAtmConduzioni);
    }
    long nDanniRimossi = dao.eliminaDanni(listIdDannoAtm,
        idProcedimentoOggetto);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return nDanniRimossi;
  }

  @Override
  public List<ScorteDTO> getScorteByIds(long[] arrayIdScortaMagazzino,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    List<ScorteDTO> lista = null;
    lista = dao.getListScorteByIds(arrayIdScortaMagazzino,
        idProcedimentoOggetto);
    if (lista == null)
    {
      lista = new ArrayList<ScorteDTO>();
    }
    return lista;
  }

  @Override
  public long inserisciDanni(List<DanniDTO> listDanniDTO,
      long idProcedimentoOggetto, Integer idDanno,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      int idProcedimentoAgricoltura) throws InternalUnexpectedException
  {
    long nDanni = 0L;
    String nomeTabella = dao.getNomeTabellaByIdDanno(idDanno);
    if (nomeTabella != null)
    {
      nDanni = dao.getNumDanniGiaEsistenti(listDanniDTO, idProcedimentoOggetto,
          idDanno);
    }
    boolean isUtenteAutorizzatoInserimentoDanniIsProprietarioDegliElementi = dao
        .isUtenteAutorizzatoInserimentoDanni(listDanniDTO,
            idProcedimentoOggetto, idDanno, idProcedimentoAgricoltura);
    if (nDanni == 0
        && isUtenteAutorizzatoInserimentoDanniIsProprietarioDegliElementi)
    {
      if (listDanniDTO != null && listDanniDTO.size() > 0)
      {
        for (DanniDTO danno : listDanniDTO)
        {
          dao.inserisciDanno(danno, idDanno);
        }
      }
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
      return listDanniDTO.size();
    }
    else
    {
      return 0;
    }
  }

  @Override
  public UnitaMisuraDTO getUnitaMisuraByIdDanno(Integer idDanno)
      throws InternalUnexpectedException
  {
    return dao.getUnitaMisuraByIdDanno(idDanno);
  }

  @Override
  public DannoDTO getDannoByIdDanno(int idDanno)
      throws InternalUnexpectedException
  {
    return dao.getDannoByIdDanno(idDanno);
  }

  public boolean getGestisciUnitaMisuraByIdDanno(int idDanno)
      throws InternalUnexpectedException
  {
    boolean isGestisciUnitaMisura = false;
    DannoDTO dDanno = dao.getDannoByIdDanno(idDanno);
    if (dDanno.getIdUnitaMisura() == null && dDanno.getNomeTabella() == null)
    {
      isGestisciUnitaMisura = true;
    }
    else
    {
      isGestisciUnitaMisura = false;
    }
    return isGestisciUnitaMisura;
  }

  @Override
  public List<DanniDTO> getDanniByIdDannoAtm(long[] arrayIdDannoAtm,
      long idProcedimentoOggetto, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    List<DanniDTO> listDanniByIdsDannoAtm = dao
        .getListDanniByProcedimentoOggettoAndArrayIdDannoAtm(arrayIdDannoAtm,
            idProcedimentoOggetto, idProcedimentoAgricoltura);
    if (listDanniByIdsDannoAtm == null)
    {
      listDanniByIdsDannoAtm = new ArrayList<DanniDTO>();
    }
    return listDanniByIdsDannoAtm;
  }

  @Override
  public int modificaDanniConduzioni(DanniDTO danno, long idProcedimentoOggetto,
      long[] arrayIdUtilizzoDichiarato,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    List<Long> listaIdDannoAtm = new ArrayList<Long>();
    listaIdDannoAtm.add(danno.getIdDannoAtm());
    dao.eliminaDanniConduzioniFromTParticellaDanneggiata(idProcedimentoOggetto,
        listaIdDannoAtm);
    for (long idUtilizzoDichiarato : arrayIdUtilizzoDichiarato)
    {
      dao.inserisciConduzioneDanneggiata(idProcedimentoOggetto, danno,
          idUtilizzoDichiarato);
    }
    dao.modificaDanno(danno, idProcedimentoOggetto);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return 1;
  }

  @Override
  public int modificaDanni(List<DanniDTO> listDanni, long idProcedimentoOggetto,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    for (DanniDTO danno : listDanni)
    {
      dao.modificaDanno(danno, idProcedimentoOggetto);
    }
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return listDanni.size();
  }

  @Override
  public Map<Long, Long> getMapTipologiaScorteUnitaDiMisura()
      throws InternalUnexpectedException
  {

    List<ScorteDecodificaDTO> listDecodificaScorte = dao
        .getListDecodicaScorta();
    Map<Long, Long> mappa = new HashMap<Long, Long>();
    for (ScorteDecodificaDTO sdd : listDecodificaScorte)
    {
      mappa.put(sdd.getIdScorta(), sdd.getIdUnitaMisura());
    }
    return mappa;
  }

  @Override
  public List<MotoriAgricoliDTO> getListMotoriAgricoli(
      long idProcedimentoOggetto, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    List<MotoriAgricoliDTO> listMotoriAgricoli = dao.getListMotoriAgricoli(
        idProcedimentoOggetto, idProcedimentoAgricoltura);
    if (listMotoriAgricoli == null)
    {
      listMotoriAgricoli = new ArrayList<MotoriAgricoliDTO>();
    }
    return listMotoriAgricoli;
  }

  @Override
  public List<MotoriAgricoliDTO> getListMotoriAgricoli(
      long idProcedimentoOggetto, long[] arrayIdMacchina,
      int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    List<MotoriAgricoliDTO> listMotoriAgricoli = dao.getListMotoriAgricoli(
        idProcedimentoOggetto, arrayIdMacchina, idProcedimentoAgricoltura);
    if (listMotoriAgricoli == null)
    {
      listMotoriAgricoli = new ArrayList<MotoriAgricoliDTO>();
    }
    return listMotoriAgricoli;
  }

  @Override
  public List<MotoriAgricoliDTO> getListMotoriAgricoliNonDanneggiati(
      long idProcedimentoOggetto, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    List<MotoriAgricoliDTO> listMotoriAgricoli = dao
        .getListMotoriAgricoliNonDanneggiati(idProcedimentoOggetto,
            idProcedimentoAgricoltura);
    if (listMotoriAgricoli == null)
    {
      listMotoriAgricoli = new ArrayList<MotoriAgricoliDTO>();
    }
    return listMotoriAgricoli;
  }

  @Override
  public List<MotoriAgricoliDTO> getListMotoriAgricoliNonDanneggiati(
      long[] arrayIdMacchina, long idProcedimentoOggetto,
      int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    return dao.getListMotoriAgricoliNonDanneggiati(arrayIdMacchina,
        idProcedimentoOggetto, idProcedimentoAgricoltura);
  }

  @Override
  public List<DanniDTO> getListDanniByIdsProcedimentoOggetto(
      long idProcedimentoOggetto, long[] arrayIdDannoAtm,
      int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    List<DanniDTO> listDanni = dao
        .getListDanniByProcedimentoOggettoAndArrayIdDannoAtm(arrayIdDannoAtm,
            idProcedimentoOggetto, idProcedimentoAgricoltura);
    if (listDanni == null)
    {
      listDanni = new ArrayList<DanniDTO>();
    }
    return listDanni;
  }

  @Override
  public List<PrestitiAgrariDTO> getListPrestitiAgrari(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return getListPrestitiAgrari(idProcedimentoOggetto, null);
  }

  @Override
  public List<PrestitiAgrariDTO> getListPrestitiAgrari(
      long idProcedimentoOggetto, long[] arrayIdPrestitiAgrari)
      throws InternalUnexpectedException
  {
    List<PrestitiAgrariDTO> listPrestitiAgrari = dao
        .getListPrestitiAgrari(idProcedimentoOggetto, arrayIdPrestitiAgrari);
    if (listPrestitiAgrari == null)
    {
      listPrestitiAgrari = new ArrayList<PrestitiAgrariDTO>();
    }
    return listPrestitiAgrari;
  }

  @Override
  public long inserisciPrestitoAgrario(PrestitiAgrariDTO prestito,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    long idPrestitiAgrari = dao.inserisciPrestitoAgrario(prestito);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return idPrestitiAgrari;
  }

  @Override
  public int eliminaPrestitiAgrari(List<Long> listIdPrestitiAgrari,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    int nPrestitiEliminati = dao.eliminaPrestitiAgrari(listIdPrestitiAgrari,
        idProcedimentoOggetto);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return nPrestitiEliminati;
  }

  @Override
  public int modificaPrestitiAgrari(long idProcedimentoOggetto,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      List<PrestitiAgrariDTO> listPrestitiAgrari)
      throws InternalUnexpectedException
  {
    int nModificati = 0;
    for (PrestitiAgrariDTO prestito : listPrestitiAgrari)
    {
      nModificati += dao.modificaPrestitiAgrari(idProcedimentoOggetto,
          prestito);
    }
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return nModificati;
  }

  @Override
  public List<FabbricatiDTO> getListFabbricati(long idProcedimentoOggetto,
      int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    List<FabbricatiDTO> lista = dao.getListFabbricati(idProcedimentoOggetto,
        idProcedimentoAgricolo);
    if (lista == null)
    {
      lista = new ArrayList<FabbricatiDTO>();
    }
    return lista;
  }

  @Override
  public FabbricatiDTO getFabbricato(long idProcedimentoOggetto,
      long idFabbricato, int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getFabbricato(idProcedimentoOggetto, idFabbricato,
        idProcedimentoAgricolo);
  }

  @Override
  public Long getNDanniScorte(long idProcedimentoOggetto,
      long[] arrayIdScortaMagazzino)
      throws InternalUnexpectedException
  {
    Long nDanniScorte = dao.getNDanniScorte(idProcedimentoOggetto,
        arrayIdScortaMagazzino);
    return nDanniScorte;
  }

  @Override
  public List<FabbricatiDTO> getListFabbricatiNonDanneggiati(
      long idProcedimentoOggetto, long[] arrayIdFabbricato,
      int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    List<FabbricatiDTO> listFabbricatiNonDanneggiati = null;
    listFabbricatiNonDanneggiati = dao.getListFabbricatiNonDanneggiati(
        idProcedimentoOggetto, arrayIdFabbricato, idProcedimentoAgricolo);
    if (listFabbricatiNonDanneggiati == null)
    {
      listFabbricatiNonDanneggiati = new ArrayList<FabbricatiDTO>();
    }
    return listFabbricatiNonDanneggiati;
  }

  @Override
  public SuperficiColtureRiepilogoDTO getSuperficiColtureRiepilogo(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    SuperficiColtureRiepilogoDTO superficiColtureRiepilogoDTO = dao
        .getSuperficiColtureRiepilogo(idProcedimentoOggetto);
    return superficiColtureRiepilogoDTO;
  }

  @Override
  public List<SuperficiColtureDettaglioDTO> getListSuperficiColtureDettaglio(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<SuperficiColtureDettaglioDTO> listSuperficiColtureDettaglio = dao
        .getListSuperficiColtureDettaglio(idProcedimentoOggetto);
    if (listSuperficiColtureDettaglio == null)
    {
      listSuperficiColtureDettaglio = new ArrayList<SuperficiColtureDettaglioDTO>();
    }
    return listSuperficiColtureDettaglio;
  }

  @Override
  public SuperficiColtureDettaglioDTO getSuperficiColtureDettaglio(
      long idProcedimentoOggetto, long idSuperficieColtura)
      throws InternalUnexpectedException
  {
    return dao.getSuperficiColtureDettaglio(idProcedimentoOggetto,
        idSuperficieColtura);
  }

  @Override
  public List<ControlloColturaDTO> getListControlloColtura(
      long idProcedimentoOggetto, long[] arrayIdSuperficieColtura)
      throws InternalUnexpectedException
  {
    List<ControlloColturaDTO> listControlloColtura = dao
        .getListControlloColtura(idProcedimentoOggetto,
            arrayIdSuperficieColtura);
    if (listControlloColtura == null)
    {
      listControlloColtura = new ArrayList<ControlloColturaDTO>();
    }
    return listControlloColtura;
  }

  @Override
  public SuperficiColtureDettaglioPsrDTO getSuperficiColtureDettaglioPsrDTO(
      long idProcedimentoOggetto,
      long idSuperficieColtura) throws InternalUnexpectedException
  {
    return dao.getSuperficiColtureDettaglioPsrDTO(idProcedimentoOggetto,
        idSuperficieColtura);
  }

  @Override
  public List<SuperficiColtureDettaglioParticellareDTO> getListDettaglioParticellareSuperficiColture(
      long idProcedimentoOggetto, long idSuperficieColtura)
      throws InternalUnexpectedException
  {
    List<SuperficiColtureDettaglioParticellareDTO> list = dao
        .getListDettaglioParticellareSuperficiColture(idProcedimentoOggetto,
            idSuperficieColtura);
    if (list == null)
    {
      list = new ArrayList<SuperficiColtureDettaglioParticellareDTO>();
    }
    return list;
  }

  @Override
  public void modificaSuperficieColtura(long idProcedimentoOggetto,
      SuperficiColtureDettaglioPsrDTO superficieColturaDettaglioDTO,
      List<ControlloColturaDTO> listControlloColtura,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    List<Long> listIdSuperficieColtura = new ArrayList<Long>();
    for (ControlloColturaDTO controlloColtura : listControlloColtura)
    {
      listIdSuperficieColtura.add(controlloColtura.getIdSuperficieColtura());
    }
    dao.eliminaControlloColtura(idProcedimentoOggetto,
        superficieColturaDettaglioDTO.getIdSuperficieColtura());
    dao.updateSuperficieColtura(idProcedimentoOggetto,
        superficieColturaDettaglioDTO);
    for (ControlloColturaDTO controlloColtura : listControlloColtura)
    {
      dao.inserisciControlloColtura(idProcedimentoOggetto,
          superficieColturaDettaglioDTO.getIdSuperficieColtura(),
          controlloColtura);
    }
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public List<SuperficiColturePlvVegetaleDTO> getListSuperficiColturePlvVegetale(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<SuperficiColturePlvVegetaleDTO> lista = dao
        .getListSuperficiColturePlvVegetale(idProcedimentoOggetto);
    if (lista == null)
    {
      lista = new ArrayList<SuperficiColturePlvVegetaleDTO>();
    }
    return lista;
  }

  @Override
  public List<DecodificaDTO<String>> getListProvinciaConTerreniInConduzione(
      long idProcedimentoOggetto,
      String ID_REGIONE_PIEMONTE) throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> lista = dao
        .getListProvinciaConTerreniInConduzione(idProcedimentoOggetto,
            ID_REGIONE_PIEMONTE);
    if (lista == null)
    {
      lista = new ArrayList<DecodificaDTO<String>>();
    }
    return lista;
  }

  @Override
  public List<DecodificaDTO<String>> getListComuniPerProvinciaConTerreniInConduzione(
      long idProcedimentoOggetto,
      String istatProvincia) throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> lista = dao
        .getListComuniPerProvinciaConTerreniInConduzioneDanniSuperficiColture(
            idProcedimentoOggetto, istatProvincia);
    if (lista == null)
    {
      lista = new ArrayList<DecodificaDTO<String>>();
    }
    return lista;
  }

  @Override
  public List<DecodificaDTO<String>> getListSezioniPerComuneDanniSuperficiColture(
      long idProcedimentoOggetto,
      String istatComune) throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> lista = dao
        .getListSezioniPerComuneConTerreniInConduzioneDanniSuperficiColture(
            idProcedimentoOggetto, istatComune);
    if (lista == null)
    {
      lista = new ArrayList<DecodificaDTO<String>>();
    }
    return lista;
  }

  @Override
  public List<ParticelleDanniDTO> getListConduzioniDannoEscludendoGiaSelezionate(
      long idProcedimentoOggetto,
      FiltroRicercaConduzioni filtroRicercaConduzioni,
      boolean piantagioniArboree)
      throws InternalUnexpectedException
  {
    List<ParticelleDanniDTO> lista = dao
        .getListConduzioniDannoEscludendoGiaSelezionate(
            idProcedimentoOggetto,
            filtroRicercaConduzioni,
            piantagioniArboree);
    if (lista == null)
    {
      lista = new ArrayList<ParticelleDanniDTO>();
    }
    lista.size();
    return lista;
  }

  @Override
  public List<ParticelleDanniDTO> getListConduzioniEscludendoGiaSelezionate(
      long idProcedimentoOggetto,
      FiltroRicercaConduzioni filtroRicercaConduzioni)
      throws InternalUnexpectedException
  {
    List<ParticelleDanniDTO> lista = dao
        .getListConduzioniEscludendoGiaSelezionate(
            idProcedimentoOggetto,
            filtroRicercaConduzioni);
    if (lista == null)
    {
      lista = new ArrayList<ParticelleDanniDTO>();
    }
    lista.size();
    return lista;
  }

  @Override
  public List<ParticelleDanniDTO> getListConduzioniDannoGiaSelezionate(
      long idProcedimentoOggetto,
      long[] arrayIdUtilizzoDichiarato,
      boolean piantagioniArboree) throws InternalUnexpectedException
  {
    if (arrayIdUtilizzoDichiarato == null)
    {
      return new ArrayList<ParticelleDanniDTO>();
    }
    List<ParticelleDanniDTO> lista = dao.getListConduzioniDannoSelezionate(
        idProcedimentoOggetto,
        arrayIdUtilizzoDichiarato,
        piantagioniArboree);
    if (lista == null)
    {
      lista = new ArrayList<ParticelleDanniDTO>();
    }
    lista.size();
    return lista;
  }

  @Override
  public List<ParticelleDanniDTO> getListConduzioniDanno(
      long idProcedimentoOggetto, long idDannoAtm)
      throws InternalUnexpectedException
  {
    List<ParticelleDanniDTO> lista = dao
        .getListConduzioniDanno(idProcedimentoOggetto, idDannoAtm);
    if (lista == null)
    {
      lista = new ArrayList<ParticelleDanniDTO>();
    }
    return lista;
  }

  @Override
  public List<ParticelleDanniDTO> getListConduzioniDanni(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<ParticelleDanniDTO> lista = dao
        .getListConduzioniDanno(idProcedimentoOggetto, null);
    if (lista == null)
    {
      lista = new ArrayList<ParticelleDanniDTO>();
    }
    return lista;
  }

  @Override
  public BigDecimal getSumSuperficiCatastaliParticelle(
      long idProcedimentoOggetto, long[] arrayIdUtilizzoDichiarato)
      throws InternalUnexpectedException
  {
    if (arrayIdUtilizzoDichiarato == null
        || arrayIdUtilizzoDichiarato.length == 0)
    {
      return new BigDecimal("0.0");
    }
    return dao.getSumSuperficiCatastaliParticelle(idProcedimentoOggetto,
        arrayIdUtilizzoDichiarato);
  }

  @Override
  public int inserisciDanniConduzioni(
      DanniDTO danno,
      long idProcedimentoOggetto,
      long[] arrayIdUtilizzoDichiarato,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {

    boolean piantagioniArboree = (danno
        .getIdDanno() == IuffiConstants.DANNI.PIANTAGIONI_ARBOREE);
    List<Integer> listDanniEquivalentiConduzioni = QuadroIuffiDAO
        .getListDanniEquivalenti(IuffiConstants.DANNI.PIANTAGIONI_ARBOREE);

    if (listDanniEquivalentiConduzioni.contains(danno.getIdDanno()))
    {
      List<ParticelleDanniDTO> listConduzioni = dao
          .getListConduzioniDannoSelezionate(idProcedimentoOggetto,
              arrayIdUtilizzoDichiarato, piantagioniArboree);
      if (listConduzioni != null && listConduzioni.size() > 0)
      {
        long[] arrayIdUtilizzoDichiaratoDaInserire;
        if (listConduzioni.size() == arrayIdUtilizzoDichiarato.length)
        {
          arrayIdUtilizzoDichiaratoDaInserire = arrayIdUtilizzoDichiarato;
        }
        else
        {
          arrayIdUtilizzoDichiaratoDaInserire = new long[listConduzioni.size()];
          int i = 0;
          for (ParticelleDanniDTO conduzione : listConduzioni)
          {
            arrayIdUtilizzoDichiaratoDaInserire[i++] = conduzione
                .getIdUtilizzoDichiarato();
          }
        }
        long idDannoAtm = dao.inserisciDanno(danno, danno.getIdDanno());
        danno.setIdDannoAtm(idDannoAtm);
        for (long idUtilizzoDichiarato : arrayIdUtilizzoDichiaratoDaInserire)
        {
          dao.inserisciConduzioneDanneggiata(idProcedimentoOggetto, danno,
              idUtilizzoDichiarato);
        }
        logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
      }
    }
    return 0;
  }

  @Override
  public List<AllevamentiDTO> getListAllevamenti(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<AllevamentiDTO> lista = dao.getListAllevamenti(idProcedimentoOggetto);
    if (lista == null)
    {
      lista = new ArrayList<AllevamentiDTO>();
    }
    return lista;
  }

  @Override
  public AllevamentiDTO getDettaglioAllevamento(long idProcedimentoOggetto,
      long idCategoriaAnimale,
      String istatComune) throws InternalUnexpectedException
  {
    List<AllevamentiDTO> lista = dao.getListRiepilogoAllevamenti(
        idProcedimentoOggetto, idCategoriaAnimale, istatComune, true);
    if (lista != null)
    {
      return lista.get(0);
    }
    else
    {
      return null;
    }
  }

  @Override
  public List<AllevamentiDettaglioPlvDTO> getListDettaglioAllevamenti(
      long idProcedimentoOggetto,
      long idCategoriaAnimale, String istatComune)
      throws InternalUnexpectedException
  {
    List<AllevamentiDettaglioPlvDTO> lista = dao.getListDettaglioAllevamenti(
        idProcedimentoOggetto, idCategoriaAnimale, istatComune);
    if (lista == null)
    {
      lista = new ArrayList<AllevamentiDettaglioPlvDTO>();
    }
    return lista;
  }

  @Override
  public List<DecodificaDTO<Integer>> getListProduzioniVendibili(
      long idCategoriaAnimale) throws InternalUnexpectedException
  {
    return dao.getListProduzioniVendibili(idCategoriaAnimale);
  }

  @Override
  public List<DecodificaDTO<Integer>> getListUnitaMisuraProduzioniVendibili(
      long idCategoriaAnimale)
      throws InternalUnexpectedException
  {
    return dao.getListUnitaMisuraProduzioniVendibili(idCategoriaAnimale);
  }

  @Override
  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioniCategorieAnimali(
      long idProcedimentoOggetto,
      long idCategoriaAnimale, String istatComune)
      throws InternalUnexpectedException
  {
    List<ProduzioneCategoriaAnimaleDTO> lista = dao
        .getListProduzioniCategorieAnimali(idProcedimentoOggetto,
            idCategoriaAnimale, istatComune);
    if (lista == null)
    {
      lista = new ArrayList<ProduzioneCategoriaAnimaleDTO>();
    }
    return lista;
  }

  @Override
  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioniVendibiliGiaInserite(
      long idProcedimentoOggetto,
      long idCategoriaAnimale, String istatComune)
      throws InternalUnexpectedException
  {
    List<ProduzioneCategoriaAnimaleDTO> lista = dao
        .getListProduzioniVendibiliGiaInserite(idProcedimentoOggetto,
            idCategoriaAnimale, istatComune);
    if (lista == null)
    {
      lista = new ArrayList<ProduzioneCategoriaAnimaleDTO>();
    }
    return lista;
  }

  @Override
  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioni(
      long idProcedimentoOggetto, long idCategoriaAnimale,
      String istatComune) throws InternalUnexpectedException
  {
    List<ProduzioneCategoriaAnimaleDTO> lista = dao.getListProduzioni(
        idProcedimentoOggetto, idCategoriaAnimale, istatComune);
    if (lista == null)
    {
      lista = new ArrayList<ProduzioneCategoriaAnimaleDTO>();
    }
    return lista;
  }

  @Override
  public void inserisciProduzioneZootecnicaEProduzioniVendibili(
      long idProcedimentoOggetto,
      AllevamentiDTO produzioneZootecnica,
      List<ProduzioneCategoriaAnimaleDTO> listProduzioniVendibili,
      long idUtenteLogin,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    List<AllevamentiDTO> listAllevamenti = dao.getListRiepilogoAllevamenti(
        idProcedimentoOggetto, produzioneZootecnica.getIdCategoriaAnimale(),
        produzioneZootecnica.getIstatComune(), true);
    if (listAllevamenti != null && listAllevamenti.size() > 0)
    {
      AllevamentiDTO allevamento = listAllevamenti.get(0);
      long idProduzioneZootecnica;
      if (allevamento.getIdProduzioneZootecnica() == null)
      {
        idProduzioneZootecnica = dao.inserisciProduzioneZootecnica(
            idProcedimentoOggetto, produzioneZootecnica, idUtenteLogin);
      }
      else
      {
        idProduzioneZootecnica = allevamento.getIdProduzioneZootecnica();
        produzioneZootecnica.setIdProduzioneZootecnica(idProduzioneZootecnica);
        dao.modificaProduzioneZootecnica(idProcedimentoOggetto,
            produzioneZootecnica, idUtenteLogin);
        dao.eliminaProduzioniVendibili(idProduzioneZootecnica);
      }
      if (listProduzioniVendibili != null && listProduzioniVendibili.size() > 0)
      {
        for (ProduzioneCategoriaAnimaleDTO produzione : listProduzioniVendibili)
        {
          dao.inserisciProduzioneVendibile(idProcedimentoOggetto, produzione,
              idProduzioneZootecnica);
        }
      }
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    }
  }

  @Override
  public BigDecimal getPlvZootecnicaUfProdotte(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getPlvZootecnicaUfProdotte(idProcedimentoOggetto);
  }

  @Override
  public BigDecimal getPlvZootecnicaUfNecessarie(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getPlvZootecnicaUfNecessarie(idProcedimentoOggetto);
  }

  @Override
  public BigDecimal getPlvZootecnicaUba(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getPlvZootecnicaUba(idProcedimentoOggetto);
  }

  @Override
  public BigDecimal getPlvZootecnicaSau(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getPlvZootecnicaSau(idProcedimentoOggetto);
  }

  @Override
  public List<AllevamentiDettaglioPlvDTO> getListPlvZootecnicaDettaglioAllevamenti(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<AllevamentiDettaglioPlvDTO> lista = dao
        .getListPlvZootecnicaDettaglioAllevamenti(idProcedimentoOggetto);
    if (lista == null)
    {
      lista = new ArrayList<AllevamentiDettaglioPlvDTO>();
    }
    return lista;
  }

  @Override
  public List<AllevamentiDTO> getListAllevamentiSingoliNonDanneggiati(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<AllevamentiDTO> lista = dao
        .getListAllevamentiSingoliNonDanneggiati(idProcedimentoOggetto, null);
    if (lista == null)
    {
      lista = new ArrayList<AllevamentiDTO>();
    }
    return lista;
  }

  @Override
  public List<AllevamentiDTO> getListAllevamentiSingoliNonDanneggiati(
      long idProcedimentoOggetto,
      long[] arrayIdAllevamento) throws InternalUnexpectedException
  {
    List<AllevamentiDTO> lista = dao.getListAllevamentiSingoliNonDanneggiati(
        idProcedimentoOggetto, arrayIdAllevamento);
    if (lista == null)
    {
      lista = new ArrayList<AllevamentiDTO>();
    }
    return lista;
  }

  @Override
  public List<AllevamentiDTO> getListAllevamentiSingoli(
      long idProcedimentoOggetto, long[] arrayIdAllevamento)
      throws InternalUnexpectedException
  {
    List<AllevamentiDTO> lista = dao.getListAllevamentiSingoli(
        idProcedimentoOggetto, arrayIdAllevamento, false);
    if (lista == null)
    {
      lista = new ArrayList<AllevamentiDTO>();
    }
    return lista;
  }

  @Override
  public List<AllevamentiDTO> getListAllevamentiByIdDannoAtm(
      long idProcedimentoOggetto,
      long[] arrayIdDannoAtm) throws InternalUnexpectedException
  {
    List<AllevamentiDTO> lista = dao
        .getListAllevamentiByIdDannoAtm(idProcedimentoOggetto, arrayIdDannoAtm);
    if (lista == null)
    {
      lista = new ArrayList<AllevamentiDTO>();
    }
    return lista;

  }

  @Override
  public List<DecodificaDTO<Long>> getListDanniDecodificaDTO(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> lista = dao
        .getListDanniDecodificaDTO(idProcedimentoOggetto);
    if (lista == null)
    {
      lista = new ArrayList<DecodificaDTO<Long>>();
    }
    return lista;
  }

  @Override
  public long getNInterventiAssociatiDanni(long idProcedimentoOggetto,
      long[] arrayIdDannoAtm)
      throws InternalUnexpectedException
  {
    return dao.getNInterventiAssociatiDanni(idProcedimentoOggetto,
        arrayIdDannoAtm);
  }

  @Override
  public long getNInterventiAssociatiDanniScorte(long idProcedimentoOggetto,
      List<Long> listIdScortaMagazzino)
      throws InternalUnexpectedException
  {
    return dao.getNInterventiAssociatiDanniScorte(idProcedimentoOggetto,
        listIdScortaMagazzino);
  }

  @Override
  public ColtureAziendaliDTO getRiepilogoColtureAziendali(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    ColtureAziendaliDTO riepilogo = dao
        .getRiepilogoColtureAziendali(idProcedimentoOggetto);
    return riepilogo;
  }

  @Override
  public ColtureAziendaliDTO getRiepilogoColtureAziendali(
      long idProcedimentoOggetto, long idProcedimento)
      throws InternalUnexpectedException
  {
    ColtureAziendaliDTO riepilogo = dao
        .getRiepilogoColtureAziendali(idProcedimentoOggetto);
    BandoDTO bando = dao.getInformazioniBandoByIdProcedimento(idProcedimento);
    riepilogo.setBando(bando);
    return riepilogo;
  }

  @Override
  public List<ColtureAziendaliDettaglioDTO> getListColtureAziendali(long idProcedimentoOggetto) throws InternalUnexpectedException {
      List<ColtureAziendaliDettaglioDTO> lista = dao.getListColtureAziendali(idProcedimentoOggetto, null);
      if (lista == null) lista = new ArrayList<ColtureAziendaliDettaglioDTO>();
      return lista;
  }

  @Override
  public List<ColtureAziendaliDettaglioDTO> getListColtureAziendali(long idProcedimentoOggetto, long[] arrayIdSuperficieColtura) throws InternalUnexpectedException {
      List<ColtureAziendaliDettaglioDTO> lista = dao.getListColtureAziendali(idProcedimentoOggetto, arrayIdSuperficieColtura);
      if (lista == null) lista = new ArrayList<ColtureAziendaliDettaglioDTO>();
      return lista;
  }
  
//  @Override
//  public Integer getCountBandoUtilizzoDanno(long idBando, long extIdUtilizzo)throws InternalUnexpectedException {
//    return dao.getCountBandoUtilizzoDanno(idBando, extIdUtilizzo);
//  }
  
  @Override
  public UtilizzoReseDTO getUtilizzoRese(ColtureAziendaliDettaglioDTO coltura) throws InternalUnexpectedException {
      return dao.getUtilizzoRese(coltura);
  }

  @Override
  public void updateColtureAziendali(long idProcedimentoOggetto, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO, List<ColtureAziendaliDettaglioDTO> listColtureAziendaliModificate) throws InternalUnexpectedException {
      dao.lockProcedimentoOggetto(idProcedimentoOggetto);
      int nUpdated = 0;
      for (ColtureAziendaliDettaglioDTO coltura : listColtureAziendaliModificate) {
	  if (dao.updateColturaAziendale(idProcedimentoOggetto, coltura) == 1) {
	      nUpdated++;
	      dao.eliminaControlloColtura(idProcedimentoOggetto, coltura.getIdSuperficieColtura());
	      UtilizzoReseDTO utilizzoRese = dao.getUtilizzoRese(coltura);
	      if (coltura.getProduzioneHa().compareTo(utilizzoRese.getResaMin()) < 0 || coltura.getProduzioneHa().compareTo(utilizzoRese.getResaMax()) > 0) {
		  ControlloColturaDTO controlloColtura = new ControlloColturaDTO();
		  controlloColtura.setIdSuperficieColtura(coltura.getIdSuperficieColtura());
		  controlloColtura.setDescrizioneAnomalia("La produzione q/ha deve essere un valore compreso tra " + utilizzoRese.getResaMin() + " e " + utilizzoRese.getResaMax());
		  controlloColtura.setBloccante("N");
		  dao.inserisciControlloColtura(idProcedimentoOggetto, coltura.getIdSuperficieColtura(), controlloColtura);
	      }
	      if (coltura.getPrezzo().compareTo(utilizzoRese.getPrezzoMin()) < 0 || coltura.getPrezzo().compareTo(utilizzoRese.getPrezzoMax()) > 0) {
		  ControlloColturaDTO controlloColtura = new ControlloColturaDTO();
		  controlloColtura.setIdSuperficieColtura(coltura.getIdSuperficieColtura());
		  controlloColtura.setDescrizioneAnomalia("Il prezzo/q deve essere un valore compreso tra " + utilizzoRese.getPrezzoMin() + " e " + utilizzoRese.getPrezzoMax());
		  controlloColtura.setBloccante("N");
		  dao.inserisciControlloColtura(idProcedimentoOggetto, coltura.getIdSuperficieColtura(), controlloColtura);
	      }
//	      if (coltura.getGiornateLavorate().compareTo(utilizzoRese.getGiornateLavorateMin()) < 0 || coltura.getGiornateLavorate().compareTo(utilizzoRese.getGiornateLavorateMax()) > 0) {
//		  ControlloColturaDTO controlloColtura = new ControlloColturaDTO();
//		  controlloColtura.setIdSuperficieColtura(coltura.getIdSuperficieColtura());
//		  controlloColtura.setDescrizioneAnomalia("Le giornate lavorate deve essere un valore compreso tra " + utilizzoRese.getGiornateLavorateMin() + " e " + utilizzoRese.getGiornateLavorateMax());
//		  controlloColtura.setBloccante("N");
//		  dao.inserisciControlloColtura(idProcedimentoOggetto, coltura.getIdSuperficieColtura(), controlloColtura);
//	      }
	  }
      }
      if (nUpdated > 0) {
	  logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
      }
  }

  @Override
  public List<AssicurazioniColtureDTO> getListAssicurazioniColture(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<AssicurazioniColtureDTO> lista = dao
        .getListAssicurazioniColture(idProcedimentoOggetto);
    if (lista == null)
    {
      lista = new ArrayList<AssicurazioniColtureDTO>();
    }
    return lista;
  }

  @Override
  public AssicurazioniColtureDTO getRiepilogoAssicurazioniColture(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    AssicurazioniColtureDTO riepilogo = dao
        .getRiepilogoAssicurazioniColture(idProcedimentoOggetto);
    if (riepilogo == null)
    {
      riepilogo = new AssicurazioniColtureDTO();
    }
    return riepilogo;
  }

  @Override
  public List<AssicurazioniColtureDTO> getListAssicurazioniColture(
      long idProcedimentoOggetto,
      long[] idAssicurazioniColture) throws InternalUnexpectedException
  {
    List<AssicurazioniColtureDTO> lista = dao.getListAssicurazioniColture(
        idProcedimentoOggetto, idAssicurazioniColture);
    if (lista == null)
    {
      lista = new ArrayList<AssicurazioniColtureDTO>();
    }
    return lista;
  }

  @Override
  public int eliminaAssicurazioniColture(long idProcedimentoOggetto,
      long[] arrayIdAssicurazioniColture)
      throws InternalUnexpectedException
  {
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    return dao.eliminaAssicurazioniColture(idProcedimentoOggetto,
        arrayIdAssicurazioniColture);
  }

  @Override
  public long inserisciAssicurazioniColture(long idProcedimentoOggetto,
      AssicurazioniColtureDTO assicurazioniColture,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
      throws InternalUnexpectedException
  {
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    dao.inserisciAssicurazioniColture(idProcedimentoOggetto,
        assicurazioniColture);
    logOperationOggettoQuadro(logOperationOggettoQuadro);
    return 1;
  }

  @Override
  public List<DecodificaDTO<Integer>> getListConsorzi(String idProvincia)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Integer>> lista = dao.getListConsorzi(idProvincia);
    if (lista == null)
    {
      lista = new ArrayList<DecodificaDTO<Integer>>();
    }
    return lista;
  }

  @Override
  public long modificaAssicurazioniColture(long idProcedimentoOggetto,
      AssicurazioniColtureDTO assicurazioniColture,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
      throws InternalUnexpectedException
  {
    return dao.modificaAssicurazioniColture(idProcedimentoOggetto,
        assicurazioniColture);
  }

  @Override
  public List<DanniColtureDTO> getListDanniColture(long idProcedimentoOggetto,
      long idProcedimento)
      throws InternalUnexpectedException
  {
    List<DanniColtureDTO> lista = dao
        .getListDanniColture(idProcedimentoOggetto);
    BandoDTO bando = dao.getInformazioniBandoByIdProcedimento(idProcedimento);
    if (lista == null)
    {
      lista = new ArrayList<DanniColtureDTO>();
    }
    for (DanniColtureDTO danno : lista)
    {
      danno.setBando(bando);
    }
    return lista;
  }

  @Override
  public Long getNColtureDanneggiate(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getNColtureDanneggiate(idProcedimentoOggetto);
  }

  @Override
  public void eliminaCensitoPrelievoOgur(long idPianoDistretto) throws InternalUnexpectedException
  {
    dao.eliminaCensitoPrelievoOgur(idPianoDistretto);
  }
  
  @Override
  public void eliminaDateCensimento(long idPianoDistretto) throws InternalUnexpectedException
  {
    dao.eliminaDateCensimento(idPianoDistretto);
  }
  
  @Override
  public void deleteRTipoDocumentiRichiesti(long idDocumentiRichiesti,
      long idTipoDocumentiRichiesti) throws InternalUnexpectedException
  {
    dao.deleteRTipoDocRichiesti(idDocumentiRichiesti);
  }


  @Override
  public List<SezioneDocumentiRichiestiDTO> getListDocumentiRichiestiDaVisualizzare(
      long idProcedimentoOggetto, Boolean isVisualizzazione)
      throws InternalUnexpectedException
  {
    return dao.getListDocumentiRichiestiDaVisualizzare(idProcedimentoOggetto,
        isVisualizzazione);
  }

  @Override
  public List<DocumentiRichiestiDTO> getDocumentiRichiesti(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return dao.getDocumentiRichiesti(idProcedimentoOggetto);
  }

  @Override
  public int aggiornaDocumentiRichiesti(long idProcedimentoOggetto,
      List<String> requestList, String HValue)
      throws InternalUnexpectedException
  {
    // aggiorno altro doc su IUF_T_DOCUMENTI_RICHIESTI:
    // se il record esiste -> update
    // se il record non esiste ancora -> insert
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    dao.updateAltroDocRichiesto(idProcedimentoOggetto, HValue);

    // cancello ogni doc legato all' idDocRichiesti su
    // IUF_R_TIPO_DOCUMENTI_RICHIESTI
    dao.deleteRTipoDocRichiesti(idProcedimentoOggetto);

    // per ogni checkbox controllo:
    // se è checkata faccio insert su IUF_R_TIPO_DOCUMENTI_RICHIESTI
    List<Long> listaTipoDoc = new ArrayList<>();
    for (String checkVal : requestList)
    {
      listaTipoDoc.add(Long.parseLong(checkVal));
    }
    if (listaTipoDoc.size() > 0)
      dao.insertRTipoDocRichiesti(idProcedimentoOggetto, listaTipoDoc);

    return 0;
  }

  @Override
  public ReferenteProgettoDTO getReferenteProgettoByIdProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {

    return dao
        .getReferenteProgettoByIdProcedimentoOggetto(idProcedimentoOggetto);
  }

  @Override
  public void insertOrUpdateReferenteProgettoByIdProcedimentoOggetto(
      long idProcedimentoOggetto, String nome,
      String cognome, String codiceFiscale, String comune, String cap,
      String telefono,
      String cellulare, String email,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
      throws InternalUnexpectedException
  {
    ReferenteProgettoDTO referenteVecchio = dao
        .getReferenteProgettoByIdProcedimentoOggetto(idProcedimentoOggetto);
    if (referenteVecchio == null)
    {
      // insert
      dao.insertReferenteProgettoByIdProcedimentoOggetto(idProcedimentoOggetto,
          nome,
          cognome, codiceFiscale, comune, cap, telefono,
          cellulare, email);
    }
    else
    {
      // update
      dao.updateReferenteProgettoByIdProcedimentoOggetto(idProcedimentoOggetto,
          nome,
          cognome, codiceFiscale, comune, cap, telefono,
          cellulare, email);
    }
    logOperationOggettoQuadro(logOperationOggettoQuadro);
  }
  
  @Override
  public void insertOrUpdateCaratteristicheAziendali(CaratteristicheAziendali dati, boolean inserimento, 
      long idDatiProcedimento, String denominazione,
      String descodc, String desccatattiva, String descrizione, String altrotipoattivita,
      String altrafiliera,
      String altrafunz, String desctrasformazione,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro, String [] filiere, String [] metodiCol, String [] descmulti)
      throws InternalUnexpectedException {
  
    
    
    if (inserimento) {
      // insert
      long idAzienda = dao.insertIuffiTAziendaBio(idDatiProcedimento, denominazione, descodc, desccatattiva, 
          descrizione, altrotipoattivita, altrafiliera, altrafunz, 
          desctrasformazione, logOperationOggettoQuadro);
      if(descmulti != null) {
        for (String string : descmulti) {
          dao.insertIuffiTAziendaBioMulti(idAzienda, new Long(string), logOperationOggettoQuadro);
        }
      }
      
      for (String string : filiere) {
        dao.insertIuffiTAziendaBioFiliere(idAzienda, new Long(string), logOperationOggettoQuadro);
      }
      for (String string : metodiCol) {
        dao.insertIuffiTAziendaBioMetodiColtivazione(idAzienda, new Long(string), logOperationOggettoQuadro);
      }
      
      
    }
    else
    {
      // update
      dao.eliminaListaFiliereAttivi(dati.getId());
      dao.eliminaListaMetodiAttivi(dati.getId());
      dao.eliminaListaMultiAttivi(dati.getId());
      
      dao.updateCaratterisicheAziendali(dati.getId(), denominazione, descodc, desccatattiva, 
          descrizione, altrotipoattivita, altrafiliera, altrafunz, 
          desctrasformazione, logOperationOggettoQuadro);
      if(descmulti != null) {
        for (String string : descmulti) {
          dao.insertIuffiTAziendaBioMulti(dati.getId(), new Long(string), logOperationOggettoQuadro);
        }
      }
      for (String string : filiere) {
        dao.insertIuffiTAziendaBioFiliere(dati.getId(), new Long(string), logOperationOggettoQuadro);
      }
      for (String string : metodiCol) {
        dao.insertIuffiTAziendaBioMetodiColtivazione(dati.getId(), new Long(string), logOperationOggettoQuadro);
      }
    }
    logOperationOggettoQuadro(logOperationOggettoQuadro);
  }
  
  @Override
  public void updatePianoDistrettoOgur(long piano, int totaleC, int indetC,  int totaleP, int indetP, BigDecimal max, String esito, long utente) throws InternalUnexpectedException { 
    
    dao.updatePianoDistrettoOgur(piano, totaleC, indetC, totaleP, indetP, max, esito, utente);

  }
  
  @Override
  public void updateConsistenzaAziendaleVegetale(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, String pubblica) throws InternalUnexpectedException { 
    
    dao.updateConsistenzaAziendaleVegetale(logOperationOggQuadro, idDatiProcedimento, pubblica);

    logOperationOggettoQuadro(logOperationOggQuadro);
  }
  
  @Override
  public void insertCensitoPrelievoOgur(Long idPianoDistretto, long idSpecie, int progressivo, int censito, int prelevato, BigDecimal percentuale, String esito, LogOperationOggettoQuadroDTO logOperationOggQuadro) throws InternalUnexpectedException { 
    
    dao.insertCensitoPrelievoOgur(idPianoDistretto, idSpecie, progressivo, censito, prelevato, percentuale, esito);

    logOperationOggettoQuadro(logOperationOggQuadro);
  }
  
  @Override
  public void insertDateCensimento(Long idPianoDistretto, Date data, Long metodo, BigDecimal valore, long extIdUtenteAggiornamento, LogOperationOggettoQuadroDTO logOperationOggQuadro) throws InternalUnexpectedException { 
    
    dao.insertDateCensimento(idPianoDistretto, data, metodo, valore, extIdUtenteAggiornamento);

    logOperationOggettoQuadro(logOperationOggQuadro);
  }
  
  @Override
  public void insertConsistenzaAziendaleVegetale(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, 
      long idConduzione, long idUtilizzo, long idUtilizzoDichiarato, String pubblica) throws InternalUnexpectedException { 
    
    dao.insertConsistenzaAziendaleVegetale(logOperationOggQuadro, idDatiProcedimento, idConduzione, 
        idUtilizzo, idUtilizzoDichiarato, pubblica);

    logOperationOggettoQuadro(logOperationOggQuadro);
  }
  
  @Override
  public void insertConsistenzaAziendaleAnimale(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, 
      long idConduzione, long idUtilizzo, long idUtilizzoDichiarato, String pubblica) throws InternalUnexpectedException { 
    
    dao.insertConsistenzaAziendaleAnimale(logOperationOggQuadro, idDatiProcedimento, idConduzione, 
        idUtilizzo, idUtilizzoDichiarato, pubblica);

    logOperationOggettoQuadro(logOperationOggQuadro);
  }
  
  @Override
  public void insertProdottiTrasformati(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, long idProdottoo, String desc) throws InternalUnexpectedException { 
    
    dao.insertProdottiTrasformati(logOperationOggQuadro, idDatiProcedimento, idProdottoo, desc);

    logOperationOggettoQuadro(logOperationOggQuadro);
  }
  
  @Override
  public void insertProduzioniCertificate(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, Long idProduzioneCertificata, Long idProduzioneTradizionale, String flagBio) throws InternalUnexpectedException { 
    
    dao.insertProduzioniCertificate(idDatiProcedimento, idProduzioneCertificata, idProduzioneTradizionale, flagBio);

    logOperationOggettoQuadro(logOperationOggQuadro);
  }
  
  @Override
  public void getImmagineDaVisualizzare(Long idImmagine) throws InternalUnexpectedException {     
    dao.getImmagineDaVisualizzare(idImmagine);;
  }
  
  @Override
  public VersamentoLicenzaDTO cercaIuv(String iuv, String cf, String cittadinanza) throws InternalUnexpectedException {    
    return dao.cercaIuv(iuv, cf, cittadinanza);
  }
  
  public String cercaAnagrafica(String cf) throws InternalUnexpectedException {   
    return dao.cercaAnagrafica(cf);
  }
  @Override
  public void insertCanaliContatti(long idDatiProcedimento, Long idImmagine) throws InternalUnexpectedException {     
    dao.insertCanaliContatti(idDatiProcedimento, idImmagine);;
  }
  
  @Override
  public long insertCanaliVendita(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, 
      String altriCanali, String sitoWeb, String amazon, String orari, String indirizzo, String telefono, String email, String luogo, String info, String facebook, 
      String instagram, String note, Long idImmagine) throws InternalUnexpectedException { 
    logOperationOggettoQuadro(logOperationOggQuadro);
   return  dao.insertCanaliVendita(idDatiProcedimento, 
        altriCanali, sitoWeb, amazon, orari, indirizzo, telefono, email, luogo, info, facebook, 
        instagram, note, idImmagine);

    
  }
  
  @Override
  public void updateConsistenzaAziendaleAnimale(LogOperationOggettoQuadroDTO logOperationOggQuadro, long idDatiProcedimento, String pubblica) throws InternalUnexpectedException { 
    
    dao.updateConsistenzaAziendaleAnimale(logOperationOggQuadro, idDatiProcedimento, pubblica);

    logOperationOggettoQuadro(logOperationOggQuadro);
  }

  public DannoDaFaunaDTO getDatiIdentificativiDanniDaFauna(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return dao.getDatiIdentificativiDanniDaFauna(idProcedimentoOggetto);
  }

  @Override
  public void updateDatiIdentificativiDanniDaFauna(DannoDaFaunaDTO danno,
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException
  {
    if (dao.getCountDanniFauna(danno.getIdProcedimentoOggetto()) != 0)
    {
      dao.updateDatiIdentificativiDanniDaFauna(danno);
    }
    else
    {
      dao.insertDatiIdentificativiDanniDaFauna(danno);
    }
    logOperationOggettoQuadro(logOperationDTO);
  }

  @Override
  public Integer getCountDanniFauna(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getCountDanniFauna(idProcedimentoOggetto);
  }

  @Override
  public List<IstitutoDTO> getListaIstitutiDanniFauna(long idAmmCompetenza)
      throws InternalUnexpectedException
  {
    return dao.getListaIstitutiDanniFauna(idAmmCompetenza);
  }

  @Override
  public List<IstitutoDTO> getListaNominativiDanniFauna(long idIstitutoDF)
      throws InternalUnexpectedException
  {
    return dao.getListaNominativiDanniFauna(idIstitutoDF);
  }

  @Override
  public List<DecodificaDTO<Long>> getListaMotiviUrgenza(Date dataValidita)
      throws InternalUnexpectedException
  {
    return dao.getListaMotiviUrgenza(dataValidita);
  }

  @Override
  public String getDecodificaMotivoUrgenza(long idMotivoUrgenza)
      throws InternalUnexpectedException {
    return dao.getDecodificaMotivoUrgenza(idMotivoUrgenza);
  }
  
  @Override
  public List<DannoFaunaDTO> getListaDanniFauna(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    List<DannoFaunaDTO> lista = dao.getListaDanniFauna(idProcedimentoOggetto);
    if (lista == null)
    {
      lista = new ArrayList<DannoFaunaDTO>();
    }
    return lista;
  }

  @Override
  public List<DannoFaunaDTO> getListaDanniFauna(long idProcedimentoOggetto,
      long[] idsDannoFauna)
      throws InternalUnexpectedException
  {
    List<DannoFaunaDTO> lista = dao.getListaDanniFauna(idProcedimentoOggetto,
        idsDannoFauna);
    if (lista == null)
    {
      lista = new ArrayList<DannoFaunaDTO>();
    }
    return lista;
  }

  @Override
  public List<DannoFaunaDTO> getListaDanniFaunaDettaglio(
      long idProcedimentoOggetto, long[] idsDannoFauna, boolean onlyLocalizzati)
      throws InternalUnexpectedException
  {
    List<DannoFaunaDTO> lista = dao
        .getListaDanniFaunaDettaglio(idProcedimentoOggetto, idsDannoFauna, onlyLocalizzati);
    if (lista == null)
    {
      lista = new ArrayList<DannoFaunaDTO>();
    }
    return lista;
  }

  @Override
  public List<RiepilogoDannoFaunaDTO> getListaRiepilogoDanniFaunaDettaglio(
      long idProcedimentoOggetto, String[] idsRiepilogo)
      throws InternalUnexpectedException
  {
    List<RiepilogoDannoFaunaDTO> lista = dao
        .getListaRiepilogoDanniFaunaDettaglio(idProcedimentoOggetto,
            idsRiepilogo);
    if (lista == null)
    {
      lista = new ArrayList<RiepilogoDannoFaunaDTO>();
    }
    return lista;
  }

  @Override
  public RiepilogoDannoFaunaDTO getTotaliRiepilogoDanniFauna(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getTotaliRiepilogoDanniFauna(idProcedimentoOggetto);
  }

  @Override
  public void eliminaDanniFauna(long idProcedimentoOggetto,
      long[] idsDannoFauna,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    dao.eliminaParticelleFauna(idProcedimentoOggetto, idsDannoFauna);
    dao.deleteRiepilogoDannoFaunaByIdDannoFauna(idsDannoFauna);
    dao.eliminaDannoFauna(idProcedimentoOggetto, idsDannoFauna);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return;
  }

  @Override
  public List<DecodificaDTO<Long>> getListaSpecieFauna()
      throws InternalUnexpectedException
  {
    return dao.getListaSpecieFauna();
  }

  @Override
  public List<DecodificaDTO<Long>> getListaTipoDannoFauna(long idSpecieFauna)
      throws InternalUnexpectedException
  {
    return dao.getListaTipoDannoFauna(idSpecieFauna);
  }

  @Override
  public void inserisciDannoFaunaParticelleFauna(long idProcedimentoOggetto,
      DannoFaunaDTO danno, List<ParticelleFaunaDTO> particelleFauna,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    long idDannoFauna = dao.inserisciDannoFauna(idProcedimentoOggetto, danno);
    dao.inserisciParticelleFauna(idDannoFauna, particelleFauna);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public void inserisciParticelleFauna(long idProcedimentoOggetto,
      long idDannoFauna, List<ParticelleFaunaDTO> particelleFauna,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    List<DannoFaunaDTO> lista = dao.getListaDanniFauna(idProcedimentoOggetto,
        new long[]
        { idDannoFauna });
    if (lista != null && lista.size() > 0)
    {
      dao.delete("IUF_T_PARTICELLE_FAUNA", "ID_DANNO_FAUNA", idDannoFauna);
      dao.delete("IUF_T_RIEPILOGO_DANNO_FAUNA", "ID_DANNO_FAUNA",
          idDannoFauna);
      dao.inserisciParticelleFauna(idDannoFauna, particelleFauna);
      logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    }
  }

  @Override
  public long memorizzaDannoFauna(long idProcedimentoOggetto,
      DannoFaunaDTO danno,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException
  {
    Long idDannoFauna = null;
    if (danno.getIdDannoFauna() == null)
    {
      idDannoFauna = dao.inserisciDannoFauna(idProcedimentoOggetto, danno);
    }
    else
    {
      idDannoFauna = danno.getIdDannoFauna();
      dao.lockProcedimentoOggetto(idProcedimentoOggetto);
      if (idDannoFauna != null)
      {
        dao.delete("IUF_T_RIEPILOGO_DANNO_FAUNA", "ID_DANNO_FAUNA",
            idDannoFauna);
      }
      dao.updateDannoFauna(idProcedimentoOggetto, danno);
    }
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
    return idDannoFauna;
  }

  @Override
  public AccertamentoDannoDTO getAccertamentoDanno(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getAccertamentoDanno(idProcedimentoOggetto);
  }

  @Override
  public List<DecodificaDTO<String>> getListaFunzionariIstruttoriByIdAmmComp(
      Long idProcedimento, Long idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    return dao.getListaFunzionariIstruttoriByIdAmmComp(idProcedimento,
        idProcedimentoAgricolo);
  }

  @Override
  public BigDecimal getImportoTotaleAccertato(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getImportoTotaleAccertato(idProcedimentoOggetto);
  }

  @Override
  public long inserisciAccertamentoDanno(AccertamentoDannoDTO acdan,
      long idProcedimentoOggetto, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    return dao.inserisciAccertamentoDanno(acdan, idProcedimentoOggetto,
        extIdUtenteAggiornamento);
  }

  @Override
  public long modificaAccertamentoDanno(AccertamentoDannoDTO acdan,
      long idProcedimentoOggetto, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    return dao.modificaAccertamentoDanno(acdan, idProcedimentoOggetto,
        extIdUtenteAggiornamento);
  }

  @Override
  public long eliminaAccertamentoDanno(long idAccertamentoDanno)
      throws InternalUnexpectedException
  {
    return dao.eliminaAccertamentoDanno(idAccertamentoDanno);
  }

  @Override
  public void updateRiepilogoDannoFauna(long idProcedimentoOggetto,
      List<RiepilogoDannoFaunaDTO> riepilogoDanni)
      throws InternalUnexpectedException
  {
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    List<Long> listIdsRiepilogoDannoFauna = new ArrayList<Long>();
    for (RiepilogoDannoFaunaDTO r : riepilogoDanni)
    {
      if (r.getIdRiepilogoDannoFauna() != null)
      {
        listIdsRiepilogoDannoFauna.add(r.getIdRiepilogoDannoFauna());
      }
    }
    if (!listIdsRiepilogoDannoFauna.isEmpty())
    {
      dao.deleteRiepilogoDannoFauna(listIdsRiepilogoDannoFauna);
    }
    dao.insertRiepilogoDannoFauna(riepilogoDanni);
  }

  @Override
  public void deleteConcessioni(long idConcessione) throws InternalUnexpectedException
  {
    dao.updateProcedimentoPratiche(idConcessione);
    dao.delete("IUF_T_PRATICHE_CONCESSIONE", "ID_CONCESSIONE", idConcessione);
    dao.delete("IUF_T_ITER_CONCESSIONE", "ID_CONCESSIONE", idConcessione);
    dao.delete("IUF_T_CONCESSIONE", "ID_CONCESSIONE", idConcessione);
  }

  @Override
  public List<ConcessioneDTO> getElencoConcessioni() throws InternalUnexpectedException
  {
    return dao.getElencoConcessioni();
  }

  @Override
  public ConcessioneDTO getConcessione(long idConcessione) throws InternalUnexpectedException
  {
    ConcessioneDTO concessione = dao.getConcessione(idConcessione);
    concessione.setPratiche(dao.getElencoPraticheConcessione(idConcessione));
    return concessione;
  }

  @Override
  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(long idConcessione) throws InternalUnexpectedException
  {
    return dao.getElencoPraticheConcessione(idConcessione);
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoAmmCompetenza(List<Long> idAmmComp, Long idPadre) throws InternalUnexpectedException
  {
    return dao.getElencoAmmCompetenza(idAmmComp, idPadre);
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoBandi(int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    return dao.getElencoBandi(idProcedimentoAgricolo);
  }

  @Override
  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(long idBando, long idAmministrazione) throws InternalUnexpectedException
  {
     return dao.getElencoPraticheConcessione(idBando,idAmministrazione);
  }

  @Override
  public void inserisciConcessione(ConcessioneDTO concessione, long idUtenteLogin) throws InternalUnexpectedException
  {
    Long idConcessione = dao.insertConcessione(concessione, idUtenteLogin);
    concessione.setIdConcessione(idConcessione);
    dao.insertIterConcessione(idConcessione, idUtenteLogin, 10);
    for(PraticaConcessioneDTO pratica : concessione.getPratiche())
    {
      dao.insertPraticaConcessione(idConcessione, pratica, idUtenteLogin, pratica.getIdPratica());
    }
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoTipiAtti() throws InternalUnexpectedException
  {
    return dao.getElencoAtti();
  }

  @Override
  public void approvaConcessione(long idConcessione, Long numeroProtocollo, Date dataProtocollo, Long idTipoAtto, long idUtenteLogin) throws InternalUnexpectedException
  {
    dao.updateIterConcessione(idConcessione, idUtenteLogin);//data fine = sysdate
    dao.insertIterConcessione(idConcessione, idUtenteLogin,40);
    dao.updateConcessione(idConcessione, numeroProtocollo, dataProtocollo, idTipoAtto, idUtenteLogin);
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoBandiConcessioni() throws InternalUnexpectedException
  {
    return dao.getElencoBandiConcessioni();
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoStatiConcessioni() throws InternalUnexpectedException
  {
    return dao.getElencoStatiConcessioni();
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoTipiAttiConcessione() throws InternalUnexpectedException
  {
    return dao.getElencoTipiAttiConcessione();

  }

  @Override
  public List<DecodificaDTO<Long>> getAmmCompetenzaConcessioni() throws InternalUnexpectedException
  {
    return dao.getAmmCompetenzaConcessioni();

  }

  @Override
  public void deletePraticaConcessione(long idPraticaConcessione) throws InternalUnexpectedException
  {
    dao.updateProcedimentoPratica(idPraticaConcessione);
    dao.delete("IUF_T_PRATICHE_CONCESSIONE", "ID_PRATICHE_CONCESSIONE", idPraticaConcessione);
  }

  @Override
  public void chiudiConcessione(long idConcessione, long idUtenteLogin) throws InternalUnexpectedException
  {
    dao.updateIterConcessione(idConcessione, idUtenteLogin);//data fine = sysdate
    dao.insertIterConcessione(idConcessione, idUtenteLogin, 30);
  }

  @Override
  public boolean canCambioStato(long idConcessione) throws InternalUnexpectedException
  {
    return dao.canCambioStato(idConcessione);
  }
  
  @Override
  public List<DecodificaDTO<Long>> getElencoTipoAttoConcessione()
      throws InternalUnexpectedException
  {
    return dao.getElencoTipoAttoConcessione();
  }

  @Override
  public void deleteOgur(long idOgur) throws InternalUnexpectedException
  {
    dao.deleteIpotesiPrelievo(idOgur);
    dao.deleteAnniCensiti(idOgur);
    dao.deleteOgurCentismento(idOgur);
    dao.delete("IUF_T_OGUR_DISTRETTO", "ID_OGUR", idOgur);
    dao.delete("IUF_T_OGUR", "ID_OGUR", idOgur);
  }

  @Override
  public List<OgurDTO> getElencoOgur(long idProcedimentoOggetto, Long idOgur) throws InternalUnexpectedException
  {
    return dao.getElencoOgur(idProcedimentoOggetto, idOgur);
  }

  @Override
  public List<DecodificaDTO<Long>> getMetodiCensimento(Long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return dao.getMetodiCensimento(idProcedimentoOggetto);
  }
  
  @Override
  public List<DecodificaDTO<Long>> getElencoSpecieOgur(boolean forInsert, Long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return dao.getElencoSpecieOgur(forInsert, idProcedimentoOggetto);
  }
  
  @Override
  public List<RicercaDistretto> getIdDistrettoOgur(Long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return dao.getIdDistrettoOgur(idProcedimentoOggetto);
  }
  
  @Override
  public long getMetodoSpecieCensimento(long metodo, long specie) throws InternalUnexpectedException
  {
    return dao.getMetodoSpecieCensimento(metodo, specie);
  }
  
  @Override
  public List<DataCensimento> getDateCensimento(long idPiano, long specie) throws InternalUnexpectedException
  {
    return dao.getDateCensimento(idPiano, specie);
  }
  
  @Override
  public List<Distretto> getElencoDistrettiOgur(boolean regionale, String idDistretto, long specie, Long piano, long procedimento) throws InternalUnexpectedException
  {
    return dao.getElencoDistrettiOgur(regionale, idDistretto, specie, piano, procedimento);
  }

  @Override
  public void inserisciModificaOgur(Long idProcedimentoOggetto, Long idOgur, Long idSpecieOgur, BigDecimal superficie, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException
  {
    if(idOgur==null)
      dao.inserisciOgur(idProcedimentoOggetto, idSpecieOgur, superficie, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    else
      dao.updateOgur(idOgur, superficie, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }


  @Override
  public void deleteDistretto(long idOgur, long idDistretto) throws InternalUnexpectedException
  {
    dao.deleteIpotesiPrelievoDistretto(idDistretto);
    dao.deleteAnniCensitiDistretto(idDistretto);
    dao.deleteOgurCentismentoDistretto(idDistretto);
    dao.delete("IUF_T_OGUR_DISTRETTO", "ID_DISTRETTO", idDistretto);
  }

  @Override
  public OgurDTO getOgur(long idOgur, boolean showDetails) throws InternalUnexpectedException
  {
    return dao.getOgur(idOgur, showDetails);
  }

  @Override
  public void inserisciModificaDistrettoOgur(long idProcedimentoOggetto, Long idDistretto, long idOgur,  it.csi.iuffi.iuffiweb.dto.DistrettoDTO distretto, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException
  {
    if(idDistretto==null)
      idDistretto = dao.inserisciDistrettoOgur(idOgur, distretto.getNominDistretto(), distretto.getSuperficieDistretto(), distretto.getSuperfVenabDistretto(), distretto.getSus(), logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    else
      dao.updateDistrettoOgur(idDistretto, distretto.getNominDistretto(), distretto.getSuperficieDistretto(), distretto.getSuperfVenabDistretto(), distretto.getSus(), logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    
    dao.deleteIpotesiPrelievoDistretto(idDistretto);
    dao.deleteAnniCensitiDistretto(idDistretto);
    dao.deleteOgurCentismentoDistretto(idDistretto);
    
    for(AnnoCensitoDTO annoCensito : distretto.getAnniCensiti())
    {
      dao.insertAnnoCensito(idDistretto, annoCensito, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    }
    
    dao.insertCensimento(idDistretto, distretto.getCensimento(), logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    
    for(IpotesiPrelievoDTO ipotesi : distretto.getIpotesiPrelievo())
    {
      dao.insertIpotesiPrelievo(idDistretto, ipotesi, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    }
    
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);    
  }

  @Override
  public List<OgurDTO> getElencoOgur(long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return dao.getElencoOgur(idProcedimentoOggetto);
  }

  @Override
  public void aggiornaVisuraConcessione(long idConcessione, Long visuraRitornataDalWs, long idUtenteLogin) throws InternalUnexpectedException
  {
    dao.updateIterConcessione(idConcessione, idUtenteLogin);
    dao.insertIterConcessione(idConcessione, idUtenteLogin, IuffiConstants.CONCESSIONI.STATO.RICHIESTA_VERCOR);
    dao.aggiornaVisuraConcessione(idConcessione, visuraRitornataDalWs, idUtenteLogin);
  }
  
  @Override
  public void aggiornaFlagVisuraConcessione(long idConcessione, long idUtenteLogin) throws InternalUnexpectedException
  {
    dao.aggiornaFlagVisuraConcessione(idConcessione, idUtenteLogin);
  }

  @Override
  public void aggiornaNotePraticaConcessione(long idPraticaConcessione, String note, long idUtenteLogin) throws InternalUnexpectedException
  {
    dao.aggiornaNotePraticaConcessione(idPraticaConcessione, note, idUtenteLogin);
  }

  @Override
  public String getNotePratica(long idPraticaConcessione) throws InternalUnexpectedException
  {
    return dao.getNotePratica(idPraticaConcessione);
  }

  @Override
  public List<DatiAziendaDTO> getElencoAziendeConcessione(long idConcessione) throws InternalUnexpectedException
  {
    return dao.getElencoAziendeConcessione(idConcessione);
  }

  @Override
  public int getNumeroAziendeInseriteCorrettamente(List<String> cuaaAziende, Long extIdVisMassivaRna) throws InternalUnexpectedException
  {
    return dao.getNumeroAziendeInseriteCorrettamente(cuaaAziende, extIdVisMassivaRna);
  }

  @Override
  public boolean esisteIdVisMassivaRnaSuVista(Long extIdVisMassivaRna) throws InternalUnexpectedException
  {
    return dao.esisteIdVisMassivaRnaSuVista(extIdVisMassivaRna);
  }

  @Override
  public Long getIdRichiesta(long idConcessione) throws InternalUnexpectedException
  {
    return dao.getIdRichiesta(idConcessione);
  }

  @Override
  public List<ProcedimentoOggettoVO> getAziendeDaVistaRegata(Long extIdVisMassivaRna) throws InternalUnexpectedException
  {
    return dao.getAziendeDaVistaRegata(extIdVisMassivaRna);
  }

  @Override
  public void updateVercorProcedimento(long idProcedimento, Date dataEsitoRna, String numeroVercor, long idUtenteLogin) throws InternalUnexpectedException
  {
    dao.updateVercorProcedimento(idProcedimento, dataEsitoRna, numeroVercor, idUtenteLogin);
  }

  @Override
  public void updatePraticaConcessioneEVercorProcedimento(long idProcedimento, long idPraticheConcessione, String numeroVercor, Date dataEsitoRna, long idUtenteLogin) throws InternalUnexpectedException
  {
    dao.updatePraticaConcessione(idProcedimento, idPraticheConcessione, numeroVercor, dataEsitoRna, idUtenteLogin);
    dao.updateVercorProcedimento(idProcedimento, dataEsitoRna, numeroVercor, idUtenteLogin);
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoMetodiCensimento() throws InternalUnexpectedException
  {
    return dao.getElencoMetodiCensimento();
  }

  @Override
  public UnitaMisuraDTO getUnitaMisuraByIdMetodoCensimento(long idMetodoCensimento) throws InternalUnexpectedException
  {
    return dao.getUnitaMisuraByIdMetodoCensimento(idMetodoCensimento);
  }

  @Override
  public void deletePianoSelettivo(long idInfoCinghiale, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException
  {
    dao.deleteDateCensCinghiali(idInfoCinghiale, null);
    dao.delete("IUF_T_INFO_CINGHIALI", "ID_INFO_CINGHIALI", idInfoCinghiale);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public void deleteDataCensimento(long idInfoCinghiale, long progressivoInfo, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException
  {
    dao.deleteDateCensCinghiali(idInfoCinghiale, progressivoInfo);
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public PianoSelettivoDTO getPianoSelettivo(long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    PianoSelettivoDTO piano = dao.getPianoSelettivo(idProcedimentoOggetto);
    if(piano!=null)
      piano.setElencoDate(dao.getElencoDateCensimentoPianoSelettivo(idProcedimentoOggetto));;
    return piano;
  }

  @Override
  public void inserisciInfoCinghiale(PianoSelettivoDTO piano, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException
  {
    Long idInfoCinghiali = dao.inserisciInfoCinghiale(piano, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    for(Date data : piano.getElencoDate())
      dao.inserisciDataCensimentoCinghiali(logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), idInfoCinghiali, data, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
   logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public void updateInfoCinghiale(PianoSelettivoDTO piano, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException
  {
    dao.updateInfoCinghiale(piano, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    //delete insert delle date
    PianoSelettivoDTO pEsistente = dao.getPianoSelettivo(logOperationOggettoQuadroDTO.getIdProcedimentoOggetto());
    dao.deleteDateCensCinghiali(pEsistente.getIdInfoCinghiali(), null);
    for(Date data : piano.getElencoDate())
      if(data!=null)
        dao.inserisciDataCensimentoCinghiali(logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), pEsistente.getIdInfoCinghiali(), data, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public Long callPckSmrgaaUtilityGraficheScriviParametri(Map<String, String> pArrayParametri) throws InternalUnexpectedException, ApplicationException
  {
      return dao.callPckSmrgaaUtilityGraficheScriviParametri(pArrayParametri);

  }
  
  @Override
  public long getIdDichConsistenza(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    long idStatoProcedimento = dao
        .getIdDichConsistenza(idProcedimentoOggetto);
    return idStatoProcedimento;
  }
  
  @Override
  public DatiBilancioDTO getDatiBilancio(long idProcedimentoOggetto)
      throws InternalUnexpectedException 
  {
    return dao.getDatiBilancio(idProcedimentoOggetto);
  }
  
  @Override
  public void insertDatiBilancio(DatiBilancioDTO dati, LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO) throws InternalUnexpectedException{
    dao.deleteDatiBilancio(dati.getIdProcedimentoOggetto());
    dao.insertDatiBilancio(dati, logOperationOggettoQuadroDTO.getExtIdUtenteAggiornamento());
    logOperationOggettoQuadro(logOperationOggettoQuadroDTO);
  }

  @Override
  public Long getIdUteMaxSuperficie(long idDichiarazioneConsistenza, boolean flagSauEqualsS) throws InternalUnexpectedException{
    return dao.getIdUteMaxSuperficie(idDichiarazioneConsistenza,flagSauEqualsS);
  }
  
  @Override
  public DecodificaDTO<Long> getZonAltimetricaByIdUte(long idUte) throws InternalUnexpectedException{
    return dao.getZonAltimetricaByIdUte(idUte);
  }
  
  @Override
  public List<DecodificaDTO<Long>> getDecodificheUtilizzo() throws InternalUnexpectedException
  {
    return dao.getDecodificheUtilizzo();
  }
  
  @Override
  public List<LogDTO> getElencoLog(Date istanzaDataDa, Date istanzaDataA, String rtesto) throws InternalUnexpectedException
  {
    return dao.getElencoLog(istanzaDataDa, istanzaDataA, rtesto);
  }

  @Override
  public String getStatoConcessione(long idConcessione) throws InternalUnexpectedException
  {
    return dao.getStatoConcessione(idConcessione);
  }

  @Override
  public SoggettoDTO getSoggettoAnagrafePesca(String codiceFiscale)throws InternalUnexpectedException{
    return dao.getSoggettoAnagrafePesca(codiceFiscale);
  }
  
  @Override
  public String getNextCodiceFiscaleEstero()throws InternalUnexpectedException{
    String codFiscale = dao.getNextCodiceFiscalePescaEstero();
    return codFiscale;
  }
  
  @Override
  public List<ImportoLicenzaDTO> getImportiLicenzePesca()throws InternalUnexpectedException{
    return dao.getImportiLicenzePesca();
  }
  
  @Override
  public List<ImportoLicenzaDTO> getAllImportiLicenzePesca()throws InternalUnexpectedException{
    return dao.getAllImportiLicenzePesca();
  }
  
  @Override
  public Long aggiornaAnagraficaPescatore(SoggettoDTO soggettoDTO)throws InternalUnexpectedException{
    if(soggettoDTO.getIdAnagrafePesca()!=null) {
      dao.aggiornaAnagraficaPescatore(soggettoDTO);
      return soggettoDTO.getIdAnagrafePesca();
    }else
      return dao.inserisciAnagraficaPescatore(soggettoDTO);
  }
  
  @Override
  public void inserisciPagamento(String iuv, String idAnagraficaPesca, String esito,String tariffa, String tipo_pagamento)throws InternalUnexpectedException{
      dao.inserisciPagamento(iuv,idAnagraficaPesca,esito,tariffa,tipo_pagamento);
  }
  
  @Override
  public List<DecodificaDTO<Long>> getZoneAltimetricheByIdAzienda(long idAzienda)throws InternalUnexpectedException{
    return dao.getZoneAltimetricheByIdAzienda(idAzienda);
  }
}
