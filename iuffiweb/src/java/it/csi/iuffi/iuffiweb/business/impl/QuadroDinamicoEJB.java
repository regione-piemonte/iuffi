package it.csi.iuffi.iuffiweb.business.impl;

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

import org.apache.commons.validator.GenericValidator;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IQuadroDinamicoEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.ElementoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.QuadroDinamicoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.VoceElementoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.QuadroDinamicoDAO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

@Stateless()
@EJB(name = "java:app/QuadroDinamico", beanInterface = IQuadroDinamicoEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class QuadroDinamicoEJB extends IuffiAbstractEJB<QuadroDinamicoDAO>
    implements IQuadroDinamicoEJB
{
  private static final String THIS_CLASS = QuadroDinamicoEJB.class
      .getSimpleName();
  private SessionContext      sessionContext;

  @Resource
  private void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  /**
   * Metodo di utilità richiamto da getQuadroDinamico e
   * getStrutturaQuadroDinamico
   * 
   * @param codiceQuadro
   * @param idProcedimentoOggetto
   * @return
   * @throws InternalUnexpectedException
   */
  private QuadroDinamicoDTO getDatiQuadroDinamico(String codiceQuadro,
      Long idProcedimentoOggetto, Long numProgressivoRecord,
      Long idProcedimentoOggettoPerCaricamentoVociElemento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getQuadroDinamico]";
    final boolean debugLevel = logger.isDebugEnabled();
    if (debugLevel)
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      QuadroDinamicoDTO quadroDinamicoDTO = dao.getQuadroDinamico(codiceQuadro,
          idProcedimentoOggetto, numProgressivoRecord);
      List<ElementoQuadroDTO> elementi = quadroDinamicoDTO.getElementiQuadro();
      caricaVociElemento(quadroDinamicoDTO.getIdQuadro(), elementi,
          idProcedimentoOggetto != null ? idProcedimentoOggetto
              : idProcedimentoOggettoPerCaricamentoVociElemento);
      return quadroDinamicoDTO;
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
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public QuadroDinamicoDTO getQuadroDinamico(String codiceQuadro,
      long idProcedimentoOggetto, Long numProgressivoRecord)
      throws InternalUnexpectedException
  {
    return getDatiQuadroDinamico(codiceQuadro, idProcedimentoOggetto,
        numProgressivoRecord, idProcedimentoOggetto);
  }

  private void caricaVociElemento(long idQuadro,
      List<ElementoQuadroDTO> elementi, Long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final Map<Long, List<VoceElementoDTO>> mapVociElementoPerQuadro = dao
        .getMapVociElementoPerQuadro(idQuadro);
    for (ElementoQuadroDTO elemento : elementi)
    {
      if (!GenericValidator.isBlankOrNull(elemento.getIstruzioneSqlElenco()))
      {
        List<VoceElementoDTO> voci = dao.getVociElementoByCustomQuery(
            elemento.getIdElementoQuadro(), elemento.getIstruzioneSqlElenco(),
            idProcedimentoOggetto);
        elemento.setVociElemento(voci);
      }
      else
      {
        elemento.setVociElemento(
            mapVociElementoPerQuadro.get(elemento.getIdElementoQuadro()));
      }
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public QuadroDinamicoDTO getStrutturaQuadroDinamico(String codiceQuadro,
      long idProcedimentoOggettoPerCaricamentoVociElemento)
      throws InternalUnexpectedException
  {
    return getDatiQuadroDinamico(codiceQuadro, null, null,
        idProcedimentoOggettoPerCaricamentoVociElemento);
  }

  @Override
  public String aggiornaInserisciRecordQuadroDinamico(long idQuadro,
      Map<Long, String[]> mapValues,
      LogOperationOggettoQuadroDTO logOperationDTO,
      Long numProgressivoRecord)
      throws InternalUnexpectedException
  {
    final long idProcedimentoOggetto = logOperationDTO
        .getIdProcedimentoOggetto();
    dao.lockProcedimentoOggetto(idProcedimentoOggetto);
    if (numProgressivoRecord == null)
    {
      numProgressivoRecord = dao
          .getMaxNumProgressivoRecord(idProcedimentoOggetto, idQuadro);
    }
    Long idDatiComuniElemQuadro = dao.getIdDatiComuniElemQuadro(
        idProcedimentoOggetto, idQuadro, numProgressivoRecord);
    String tipoOperazione = null;
    if (idDatiComuniElemQuadro == null)
    {
      idDatiComuniElemQuadro = dao.insertDatiComuniElemQuadro(
          idProcedimentoOggetto, idQuadro, numProgressivoRecord);
      tipoOperazione = IuffiConstants.QUADRO_DINAMICO.TIPO_OPERAZIONE.INSERISCI;
    }
    else
    {
      dao.delete("IUF_T_DATO_ELEMENTO_QUADRO", "ID_DATI_COMUNI_ELEM_QUADRO",
          idDatiComuniElemQuadro);
      tipoOperazione = IuffiConstants.QUADRO_DINAMICO.TIPO_OPERAZIONE.MODIFICA;
    }
    dao.batchInsertDatiElementoQuadro(idDatiComuniElemQuadro, mapValues);
    String errorMessage = null;
    String istruzSqlPostSalvataggio = dao.getIstruzSqlPostSalvataggio(idQuadro);
    logOperationOggettoQuadro(logOperationDTO);
    if (!GenericValidator.isBlankOrNull(istruzSqlPostSalvataggio))
    {
      errorMessage = dao.executeIstruzSqlPostSalvataggio(
          istruzSqlPostSalvataggio, idDatiComuniElemQuadro, tipoOperazione,
          logOperationDTO.getExtIdUtenteAggiornamento());
      if (errorMessage != null)
      {
        sessionContext.setRollbackOnly();
      }
    }
    return errorMessage;
  }

  @Override
  public String executeIstruzioneControlli(String istruzioneSqlControlli,
      String valoreElemento) throws InternalUnexpectedException
  {
    return dao.executeIstruzioneControlli(istruzioneSqlControlli,
        valoreElemento);
  }

  @Override
  public String eliminaRecordQuadroDinamico(long idQuadro,
      int numProgressivoRecord, LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException
  {
    Long idDatiComuniElemQuadro = dao.getIdDatiComuniElemQuadro(
        logOperationDTO.getIdProcedimentoOggetto(), idQuadro,
        numProgressivoRecord);
    if (idDatiComuniElemQuadro != null)
    {
      String errorMessage = null;
      String istruzSqlPostSalvataggio = dao
          .getIstruzSqlPostSalvataggio(idQuadro);
      if (!GenericValidator.isBlankOrNull(istruzSqlPostSalvataggio))
      {
        errorMessage = dao.executeIstruzSqlPostSalvataggio(
            istruzSqlPostSalvataggio, idDatiComuniElemQuadro,
            IuffiConstants.QUADRO_DINAMICO.TIPO_OPERAZIONE.ELIMINA,
            logOperationDTO.getExtIdUtenteAggiornamento());
        if (errorMessage != null)
        {
          return errorMessage;
        }
      }
      logOperationOggettoQuadro(logOperationDTO);
      dao.delete("IUF_T_DATO_ELEMENTO_QUADRO", "ID_DATI_COMUNI_ELEM_QUADRO",
          idDatiComuniElemQuadro);
      dao.delete("IUF_T_DATI_COMUNI_ELEM_QUADR",
          "ID_DATI_COMUNI_ELEM_QUADRO", idDatiComuniElemQuadro);
    }

    return null;
  }
}
