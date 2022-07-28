package it.csi.iuffi.iuffiweb.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.ProcedimOggettoStampaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public class TrasmissioneMassivaDAO extends BaseDAO
{
  private static final String THIS_CLASS                = TrasmissioneMassivaDAO.class
      .getSimpleName();
  public static final int     FIRMATA_GRAFOMETRICAMENTE = 5;

  public TrasmissioneMassivaDAO()
  {
  }

  public void initializeDAO() throws NamingException
  {
    DataSource datasource = (DataSource) InitialContext
        .doLookup("java:/iuffiweb/jdbc/iuffiwebDS");
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        datasource);
  }

  public void aggiornaDataTrasmissioneDopoTrasmissioneMassiva(
      long idProcedimentoOggetto, Date dataTrasmissione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "scriviEsitoElaborazioneMassiva";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("DATA_TRASMISSIONE", dataTrasmissione,
        Types.TIMESTAMP);
    try
    {
      final String QUERY_CLOSED_ITER = " UPDATE                                                   \n"
          + "   IUF_T_ITER_PROCEDIMENTO_OGGE                         \n"
          + " SET                                                      \n"
          + "   DATA_FINE = :DATA_TRASMISSIONE                         \n"
          + " WHERE                                                    \n"
          + "   ID_ITER_PROCEDIMENTO_OGGETT =                          \n"
          + "   (                                                      \n"
          + "     SELECT                                               \n"
          + "       MAX(ID_ITER_PROCEDIMENTO_OGGETT)                   \n"
          + "     FROM                                                 \n"
          + "       IUF_T_ITER_PROCEDIMENTO_OGGE                     \n"
          + "     WHERE                                                \n"
          + "       ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
          + "       AND DATA_FINE          IS NOT NULL                 \n"
          + "   )                                                      \n";
      try
      {
        namedParameterJdbcTemplate.update(QUERY_CLOSED_ITER,
            mapParameterSource);
      }
      catch (Throwable t)
      {
        InternalUnexpectedException e = new InternalUnexpectedException(t,
            new LogParameter[]
            {
                new LogParameter("idProcedimentoOggetto",
                    idProcedimentoOggetto),
                new LogParameter("dataTrasmissione", dataTrasmissione)
            },
            new LogVariable[]
            {
            }, QUERY_CLOSED_ITER, mapParameterSource);
        logInternalUnexpectedException(e, THIS_METHOD);
        throw e;
      }
      final String QUERY_CURRENT_ITER = " UPDATE                                               \n"
          + "   IUF_T_ITER_PROCEDIMENTO_OGGE                     \n"
          + " SET                                                  \n"
          + "   DATA_INIZIO = :DATA_TRASMISSIONE                   \n"
          + " WHERE                                                \n"
          + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
          + "   AND DATA_FINE          IS NULL                     \n";
      try
      {
        namedParameterJdbcTemplate.update(QUERY_CURRENT_ITER,
            mapParameterSource);
      }
      catch (Throwable t)
      {
        InternalUnexpectedException e = new InternalUnexpectedException(t,
            new LogParameter[]
            {
                new LogParameter("idProcedimentoOggetto",
                    idProcedimentoOggetto),
                new LogParameter("dataTrasmissione", dataTrasmissione)
            },
            new LogVariable[]
            {
            }, QUERY_CURRENT_ITER, mapParameterSource);
        logInternalUnexpectedException(e, THIS_METHOD);
        throw e;
      }
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  public Long lockProcedimentoOggettoAndProcedimentoByIdDocumentoIndexPerTrmsmissione(
      long idDocumentoIndex)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::lockProcedimentoOggettoAndProcedimentoByIdDocumentoIndexPerTrmsmissione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO                           \n"
        + " FROM                                                   \n"
        + "   IUF_T_PROCEDIMENTO P,                                \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO                        \n"
        + " WHERE                                                  \n"
        + "   P.ID_PROCEDIMENTO              = PO.ID_PROCEDIMENTO  \n"
        + "   AND PO.ID_PROCEDIMENTO_OGGETTO =                     \n"
        + "   (                                                    \n"
        + "     SELECT                                             \n"
        + "       POS.ID_PROCEDIMENTO_OGGETTO                      \n"
        + "     FROM                                               \n"
        + "       IUF_T_PROCEDIM_OGGETTO_STAMP POS                \n"
        + "     WHERE                                              \n"
        + "       POS.EXT_ID_DOCUMENTO_INDEX = :ID_DOCUMENTO_INDEX \n"
        + "   )                                                    \n";
    // + " FOR UPDATE \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_DOCUMENTO_INDEX", idDocumentoIndex,
        Types.NUMERIC);
    try
    {
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idDocumentoIndex", idDocumentoIndex) }, null, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public ProcedimOggettoStampaDTO findIdProcedimOggettoStampaByIdDocumentoIndex(
      long idDocumentoIndex)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::findIdProcedimOggettoStampaByIdDocumentoIndex]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                         \n"
        + "   ID_PROCEDIM_OGGETTO_STAMPA,                  \n"
        + "   ID_PROCEDIMENTO_OGGETTO,                     \n"
        + "   ID_BANDO_OGGETTO,                            \n"
        + "   ID_OGGETTO_ICONA,                            \n"
        + "   EXT_ID_DOCUMENTO_INDEX,                      \n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO,                 \n"
        + "   DATA_ULTIMO_AGGIORNAMENTO,                   \n"
        + "   DATA_INIZIO,                                 \n"
        + "   DATA_FINE,                                   \n"
        + "   ID_STATO_STAMPA,                             \n"
        + "   DESC_ANOMALIA                                \n"
        + " FROM                                           \n"
        + "   IUF_T_PROCEDIM_OGGETTO_STAMP                \n"
        + " WHERE                                          \n"
        + "   EXT_ID_DOCUMENTO_INDEX = :ID_DOCUMENTO_INDEX \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_DOCUMENTO_INDEX", idDocumentoIndex,
        Types.NUMERIC);
    try
    {
      List<ProcedimOggettoStampaDTO> list = namedParameterJdbcTemplate.query(
          QUERY, mapParameterSource,
          new RowMapper<ProcedimOggettoStampaDTO>()
          {

            @Override
            public ProcedimOggettoStampaDTO mapRow(ResultSet rs, int row)
                throws SQLException
            {
              ProcedimOggettoStampaDTO procedimOggettoStampaDTO = new ProcedimOggettoStampaDTO();
              procedimOggettoStampaDTO.setDataFine(rs.getDate("DATA_FINE"));
              procedimOggettoStampaDTO.setDataInizio(rs.getDate("DATA_INIZIO"));
              procedimOggettoStampaDTO.setDataUltimoAggiornamento(
                  rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
              procedimOggettoStampaDTO
                  .setDescAnomalia(rs.getString("DESC_ANOMALIA"));
              procedimOggettoStampaDTO
                  .setExtIdDocumentoIndex(rs.getLong("EXT_ID_DOCUMENTO_INDEX"));
              procedimOggettoStampaDTO.setExtIdUtenteAggiornamento(
                  rs.getLong("EXT_ID_UTENTE_AGGIORNAMENTO"));
              procedimOggettoStampaDTO
                  .setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
              procedimOggettoStampaDTO
                  .setIdOggettoIcona(rs.getLong("ID_OGGETTO_ICONA"));
              procedimOggettoStampaDTO.setIdProcedimentoOggetto(
                  rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
              procedimOggettoStampaDTO.setIdProcedimOggettoStampa(
                  rs.getLong("ID_PROCEDIM_OGGETTO_STAMPA"));
              procedimOggettoStampaDTO
                  .setIdStatoStampa(rs.getInt("ID_STATO_STAMPA"));
              return procedimOggettoStampaDTO;
            }
          });
      return list == null || list.isEmpty() ? null : list.get(0);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idDocumentoIndex", idDocumentoIndex) }, null, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public ProcedimentoOggetto findProcedimentoOggetto(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::findProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                  \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO,                           \n"
        + "   PO.ID_PROCEDIMENTO,                                   \n"
        + "   PO.DATA_INIZIO,                                       \n"
        + "   PO.DATA_FINE,                                         \n"
        + "   PO.IDENTIFICATIVO,                                    \n"
        + "   PO.ID_ESITO                                           \n"
        + " FROM                                                    \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO                         \n"
        + " WHERE                                                   \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      List<ProcedimentoOggetto> list = namedParameterJdbcTemplate.query(QUERY,
          mapParameterSource,
          new RowMapper<ProcedimentoOggetto>()
          {

            @Override
            public ProcedimentoOggetto mapRow(ResultSet rs, int row)
                throws SQLException
            {
              ProcedimentoOggetto procedimentoOggetto = new ProcedimentoOggetto();
              procedimentoOggetto.setIdProcedimentoOggetto(
                  rs.getInt("ID_PROCEDIMENTO_OGGETTO"));
              procedimentoOggetto
                  .setIdProcedimento(rs.getInt("ID_PROCEDIMENTO"));
              procedimentoOggetto.setDataInizio(rs.getDate("DATA_INIZIO"));
              procedimentoOggetto.setDataFine(rs.getDate("DATA_FINE"));
              procedimentoOggetto
                  .setIdentificativo(rs.getString("IDENTIFICATIVO"));
              procedimentoOggetto.setIdEsito(getLongNull(rs, "ID_ESITO"));
              return procedimentoOggetto;
            }
          });
      return list == null || list.isEmpty() ? null : list.get(0);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, null, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public long countStampeInCorso(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::findProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                   \n"
        + "   COUNT(*)                                               \n"
        + " FROM                                                     \n"
        + "   IUF_T_PROCEDIM_OGGETTO_STAMP POS                      \n"
        + " WHERE                                                    \n"
        + "   POS.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "   AND POS.DATA_FINE IS NULL                              \n"
        + "   AND POS.ID_STATO_STAMPA IN (1, 8)                      \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, null, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public void closeProcedimOggettoStampa(long idProcedimOggettoStampa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::closeProcedimOggettoStampa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " UPDATE                                                     \n"
        + "   IUF_T_PROCEDIM_OGGETTO_STAMP                            \n"
        + " SET                                                        \n"
        + "   DATA_FINE = SYSDATE                                      \n"
        + " WHERE                                                      \n"
        + "   ID_PROCEDIM_OGGETTO_STAMPA = :ID_PROCEDIM_OGGETTO_STAMPA \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIM_OGGETTO_STAMPA",
        idProcedimOggettoStampa, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimOggettoStampa", idProcedimOggettoStampa) }, null, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public void cloneProcedimOggettoStampaAndUpdateExtIdDocumentoIndex(
      long idProcedimOggettoStampa,
      long idDocumentoIndex, long idUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::cloneProcedimOggettoStampaAndUpdateExtIdDocumentoIndex]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " INSERT                                                         \n"
        + " INTO                                                           \n"
        + "   IUF_T_PROCEDIM_OGGETTO_STAMP                                \n"
        + "   (                                                            \n"
        + "     ID_STATO_STAMPA,                                           \n"
        + "     ID_PROCEDIMENTO_OGGETTO,                                   \n"
        + "     ID_PROCEDIM_OGGETTO_STAMPA,                                \n"
        + "     ID_OGGETTO_ICONA,                                          \n"
        + "     ID_BANDO_OGGETTO,                                          \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,                               \n"
        + "     EXT_ID_DOCUMENTO_INDEX,                                    \n"
        + "     DESC_ANOMALIA,                                             \n"
        + "     DATA_ULTIMO_AGGIORNAMENTO,                                 \n"
        + "     DATA_INIZIO,                                               \n"
        + "     DATA_FINE                                                  \n"
        + "   )                                                            \n"
        + "   (                                                            \n"
        + "     SELECT                                                     \n"
        + "       :ID_STATO_STAMPA,                                        \n"
        + "       ID_PROCEDIMENTO_OGGETTO,                                 \n"
        + "       SEQ_IUF_T_PROCEDIM_OGGET_STA.NEXTVAL,                  \n"
        + "       ID_OGGETTO_ICONA,                                        \n"
        + "       ID_BANDO_OGGETTO,                                        \n"
        + "       :EXT_ID_UTENTE_AGGIORNAMENTO,                            \n"
        + "       :EXT_ID_DOCUMENTO_INDEX,                                 \n"
        + "       DESC_ANOMALIA,                                           \n"
        + "       SYSDATE,                                                 \n"
        + "       SYSDATE,                                                 \n"
        + "       NULL AS DATA_FINE                                        \n"
        + "     FROM                                                       \n"
        + "       IUF_T_PROCEDIM_OGGETTO_STAMP                            \n"
        + "     WHERE                                                      \n"
        + "       ID_PROCEDIM_OGGETTO_STAMPA = :ID_PROCEDIM_OGGETTO_STAMPA \n"
        + "   )                                                            \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_STATO_STAMPA", FIRMATA_GRAFOMETRICAMENTE,
        Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
        idUtenteAggiornamento, Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_DOCUMENTO_INDEX", idDocumentoIndex,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_PROCEDIM_OGGETTO_STAMPA",
        idProcedimOggettoStampa, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimOggettoStampa",
                  idProcedimOggettoStampa),
              new LogParameter("idDocumentoIndex", idDocumentoIndex),
              new LogParameter("idUtenteAggiornamento", idUtenteAggiornamento)
          }, null, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

}