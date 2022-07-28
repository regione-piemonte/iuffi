package it.csi.iuffi.iuffiweb.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.iuffi.iuffiweb.business.IIuffiAbstractEJB;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoEProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONConduzioneInteventoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public abstract class IuffiAbstractEJB<TDAO extends BaseDAO>
    implements IIuffiAbstractEJB
{
  protected static final Logger logger     = Logger
      .getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".business");
  private static final String   THIS_CLASS = IuffiAbstractEJB.class
      .getSimpleName();
  @Resource
  protected EJBContext          context;
  protected TDAO                dao;

  @Autowired
  public void setDao(TDAO dao)
  {
    this.dao = dao;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<Integer>> getTabellaDecodifica(String nomeTabella,
      boolean orderByDesc) throws InternalUnexpectedException
  {
    return dao.getTabellaDecodifica(nomeTabella, orderByDesc);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public Map<String, String> getParametri(String[] paramNames)
      throws InternalUnexpectedException
  {
    return dao.getParametri(paramNames);
  }

  protected void logOperationOggettoQuadro(
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException
  {
    logOperationOggettoQuadro(logOperationDTO.getIdProcedimentoOggetto(),
        logOperationDTO.getIdQuadroOggetto(),
        logOperationDTO.getIdBandoOggetto(),
        logOperationDTO.getExtIdUtenteAggiornamento());
  }

  protected void logOperationOggettoQuadro(long idProcedimentoOggetto,
      long idQuadroOggetto, long idBandoOggetto, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    int rows = dao.updateProcedimOggettoQuadro(idProcedimentoOggetto,
        idQuadroOggetto, idBandoOggetto, extIdUtenteAggiornamento);

    if (rows <= 0)
    {
      dao.insertProcedimOggettoQuadro(idProcedimentoOggetto, idQuadroOggetto,
          idBandoOggetto, extIdUtenteAggiornamento);
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getProvincie(final String idRegione)
      throws InternalUnexpectedException
  {
    return dao.getProvince(idRegione, false);
  }
  
  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getSiglaProvince(final String idRegione)
      throws InternalUnexpectedException
  {
    return dao.getSiglaProvince(idRegione, false);
  }
  
  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<String> getProvinceCodici(final String idRegione)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> lista = dao.getProvince(idRegione, false);
    List<String> provinceCodici = new ArrayList<String>();
    for(DecodificaDTO<String> p : lista)
    {
    	provinceCodici.add(p.getId());
    }
    return provinceCodici;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getProvincie(final String idRegione,
      boolean visualizzaStatiEsteri) throws InternalUnexpectedException
  {
    return dao.getProvince(idRegione, visualizzaStatiEsteri);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getDecodificheComuni(String idRegione,
      String istatProvincia, String flagEstinto)
      throws InternalUnexpectedException
  {
    return dao.getDecodificheComuni(idRegione, istatProvincia, flagEstinto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ComuneDTO> getDecodificheComuniWidthProv(String idRegione,
      String istatProvincia, String flagEstinto)
      throws InternalUnexpectedException
  {
    return dao.getDecodificheComuniWidthProv(idRegione, istatProvincia,
        flagEstinto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getProvincieConTerreniInConduzione(
      long idProcedimentoOggetto, String istatRegione, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    return dao.getProvincieConTerreniInConduzione(idProcedimentoOggetto,
        istatRegione, idProcedimentoAgricoltura);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getSezioniPerComune(String istatComune)
      throws InternalUnexpectedException
  {
    return dao.getSezioniPerComune(istatComune);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getComuniPerProvinciaConTerreniInConduzione(
      long idProcedimentoOggetto, String istatRegione, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    return dao.getComuniPerProvinciaConTerreniInConduzione(
        idProcedimentoOggetto, istatRegione,idProcedimentoAgricoltura);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getSezioniPerComuneConTerreniInConduzione(
      long idProcedimentoOggetto, String istatProvincia, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    return dao.getSezioniPerComuneConTerreniInConduzione(idProcedimentoOggetto,
        istatProvincia, idProcedimentoAgricoltura);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<RigaJSONConduzioneInteventoDTO> ricercaConduzioni(
      FiltroRicercaConduzioni filtro, int idProcedimentoAgricoltura) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::ricercaConduzioni]";
    final boolean debugLevel = logger.isDebugEnabled();
    if (debugLevel)
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.ricercaConduzioni(filtro, idProcedimentoAgricoltura);
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
  public String getHelpCdu(String codcdu, Long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    return dao.getHelpCdu(codcdu, idQuadroOggetto);
  }

  @Override
  public Map<String, String> getMapHelpCdu(String... codcdu)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getMapHelpCdu]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getMapHelpCdu(codcdu);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public LogOperationOggettoQuadroDTO getIdUtenteUltimoModifica(
      long idProcediemntoOggetto, long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getIdUtenteUltimoModifica(idProcediemntoOggetto, idQuadroOggetto,
        idBandoOggetto);
  }

  public String getParametroComune(String idParametro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametroComune]";
    final boolean debugLevel = logger.isDebugEnabled();
    if (debugLevel)
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getParametroComune(idParametro);
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
  public Map<String, String> getParametriComune(String... idParametro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametriComune]";
    final boolean debugLevel = logger.isDebugEnabled();
    if (debugLevel)
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getParametriComune(idParametro);
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
  public List<String[]> getStatoDatabase() throws InternalUnexpectedException
  {
    return dao.getStatoDatabase();
  }

  @Override
  public Date getSysDate() throws InternalUnexpectedException
  {
    return dao.getSysDate();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getInformazioniBando(idBando);
  }

  @Override
  public List<ComuneDTO> getComuni(String idRegione, String istatProvincia,
      String flagEstinto, String flagEstero) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getComuni";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getComuni(idRegione, istatProvincia, flagEstinto, flagEstero);
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
  public ComuneDTO getComune(String istatComune)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getComune";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getComune(istatComune);
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
  public ProcedimentoEProcedimentoOggetto getProcedimentoEProcedimentoOggettoByIdProcedimentoOggetto(
      long idProcedimentoOggetto, boolean ricaricaQuadri)
      throws InternalUnexpectedException
  {
    ProcedimentoEProcedimentoOggetto ppo = dao
        .getProcedimentoEProcedimentoOggettoByIdProcedimentoOggetto(
            idProcedimentoOggetto);
    if (ppo != null)
    {
      final ProcedimentoOggetto procedimentoOggetto = ppo
          .getProcedimentoOggetto();
      if (procedimentoOggetto != null && ricaricaQuadri)
      {
        procedimentoOggetto
            .setQuadri(dao.getQuadriProcedimentoOggetto(idProcedimentoOggetto));
      }
    }
    return ppo;
  }

  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<QuadroOggettoDTO> getQuadriProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return dao.getQuadriProcedimentoOggetto(idProcedimentoOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public Procedimento getProcedimento(long idProcedimento)
      throws InternalUnexpectedException
  {
    return dao.getProcedimento(idProcedimento);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ComuneDTO> getDecodificheComuniWidthProvByComune(
      String denominazioneComune, String flagEstinto)
      throws InternalUnexpectedException
  {
    return dao.getDecodificheComuniWidthProvByComune(denominazioneComune,
        flagEstinto);
  }


  @Override
  public int getIdProcedimentoAgricoloByIdProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return dao.getIdProcedimentoAgricoloByIdProcedimentoOggetto(idProcedimentoOggetto);
  } 
}
