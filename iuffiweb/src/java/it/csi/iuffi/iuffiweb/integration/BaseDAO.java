package it.csi.iuffi.iuffiweb.integration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoEProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.AzioneDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONConduzioneInteventoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.DumpUtils;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class BaseDAO
{
  private static final String          THIS_CLASS = BaseDAO.class
      .getSimpleName();
  protected static final Logger        logger     = Logger
      .getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  protected final int                  PASSO      = 900;

  @Autowired
  protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired
  protected ApplicationContext         appContext;

  public BaseDAO()
  {
  }

  public <T> T queryForObject(String query, SqlParameterSource parameters,
      Class<T> objClass, ResultSetExtractor<T> re)
  {
    return namedParameterJdbcTemplate.query(query, parameters, re);
  }

  public <T> T queryForObject(String query, SqlParameterSource parameters,
      Class<T> objClass)
  {
    ResultSetExtractor<T> re = new GenericObjectExtractor<T>(objClass);
    return namedParameterJdbcTemplate.query(query, parameters, re);
  }

  public String queryForString(String query, SqlParameterSource parameters,
      final String field)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<String>()
        {
          @Override
          public String extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            String sql = "";
            while (rs.next())
            {
              sql = rs.getString(field);
            }
            return sql;
          }
        });
  }

  /**
   * Assicurarsi che i nomi dei campi del DTO siano UGUALI ai campi (o alias)
   * richiesti nella query ma in camel case senza spazi e punteggiatura,
   * <b>rispettando anche i tipi.</b> (es: "TIPO_RICHIESTA" sul db =>
   * "tipoRichiesta" sul dto)
   */
  public <T> List<T> queryForList(String query, SqlParameterSource parameters,
      Class<T> objClass)
  {
    ResultSetExtractor<List<T>> re = new GenericListEstractor<T>(objClass);
    return namedParameterJdbcTemplate.query(query, parameters, re);
  }

  public int update(String query, MapSqlParameterSource parameters)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "chiudiUltimoStato";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      return namedParameterJdbcTemplate.update(query, parameters);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, query,
          parameters);
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
      }
    }

  }

  //TODO: evitare getInCondition con String
  public String getInCondition(String campo, List<?> vId)
  {
    int cicli = vId.size() / PASSO;
    if (vId.size() % PASSO != 0)
      cicli++;

    StringBuffer condition = new StringBuffer(" AND ( ");
    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < vId.size(); i++)
      {
        if (primo)
        {
          condition.append(" " + campo + " IN (" + getIdFromvId(vId, i));
          primo = false;
        }
        else
        {
          condition.append("," + getIdFromvId(vId, i));
        }
      }
      condition.append(")");
    }
    condition.append(")");

    return condition.toString();

  }

  public String getInCondition(String campo, Vector<? extends Number> vId, boolean andClause)
  {
    int cicli = vId.size() / PASSO;
    if (vId.size() % PASSO != 0)
      cicli++;
    StringBuffer condition = new StringBuffer("  ");

    if (andClause)
      condition.append(" AND ( ");

    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < vId.size(); i++)
      {
        if (primo)
        {
          condition.append(" " + campo + " IN (" + getIdFromvId(vId, i));
          primo = false;
        }
        else
        {
          condition.append("," + getIdFromvId(vId, i));
        }
      }
      condition.append(")");
    }

    if (andClause)
      condition.append(")");

    return condition.toString();

  }

  public String getNotInCondition(String campo, List<?> vId)
  {
    int cicli = vId.size() / PASSO;
    if (vId.size() % PASSO != 0)
      cicli++;

    StringBuffer condition = new StringBuffer(" AND ( ");
    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < vId.size(); i++)
      {
        if (primo)
        {
          condition.append(" " + campo + " NOT IN (" + getIdFromvId(vId, i));
          primo = false;
        }
        else
        {
          condition.append("," + getIdFromvId(vId, i));
        }
      }
      condition.append(")");
    }
    condition.append(")");
    return condition.toString();
  }
  
  protected String getIdFromvId(List<?> vId, int idx)
  {

    Object o = vId.get(idx);

    if (o instanceof String)
    {
      return "'" + (String) o + "'";
    }
    else
      return o.toString();
  }

  public long getNextSequenceValue(String sequenceName)
  {
    String query = " SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
    return namedParameterJdbcTemplate.queryForLong(query,
        (SqlParameterSource) null);
  }

  protected void logInternalUnexpectedException(InternalUnexpectedException e,
      String logHeader)
  {
    DumpUtils.logInternalUnexpectedException(logger, e, logHeader);
  }

  public Long getLongNull(ResultSet rs, String name) throws SQLException
  {
    String value = rs.getString(name);
    if (value != null)
    {
      return new Long(value);
    }
    return null;
  }

  public Long getLongNull(ResultSet rs, String name, Long defaultOnNull)
      throws SQLException
  {
    String value = rs.getString(name);
    if (value != null)
    {
      return new Long(value);
    }
    return defaultOnNull;
  }

  public Integer getIntegerNull(ResultSet rs, String name) throws SQLException
  {
    String value = rs.getString(name);
    if (value != null)
    {
      return new Integer(value);
    }
    return null;
  }

  public Long queryForLong(String query,
      MapSqlParameterSource mapParameterSource)
  {
    return namedParameterJdbcTemplate.query(query, mapParameterSource,
        new ResultSetExtractor<Long>()
        {

          @Override
          public Long extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            if (rs.next())
            {
              return rs.getLong(1);
            }
            else
            {
              return null;
            }
          }

        });
  }

  public Long queryForLongNull(String query,
      MapSqlParameterSource mapParameterSource)
  {
    return namedParameterJdbcTemplate.query(query, mapParameterSource,
        new ResultSetExtractor<Long>()
        {

          @Override
          public Long extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            if (rs.next())
            {
              BigDecimal bd = rs.getBigDecimal(1);
              if (bd != null)
              {
                return bd.longValue();
              }
            }
            return null;
          }

        });
  }
  
  public BigDecimal queryForBigDecimal(String query,
	      MapSqlParameterSource mapParameterSource)
	  {
	    return namedParameterJdbcTemplate.query(query, mapParameterSource,
	        new ResultSetExtractor<BigDecimal>()
	        {

	          @Override
	          public BigDecimal extractData(ResultSet rs)
	              throws SQLException, DataAccessException
	          {
	            if (rs.next())
	            {
	              return rs.getBigDecimal(1);
	            }
	            else
	            {
	              return null;
	            }
	          }
	        });
	  }

  public String queryForString(String query,
      MapSqlParameterSource mapParameterSource)
  {
    return namedParameterJdbcTemplate.query(query, mapParameterSource,
        new ResultSetExtractor<String>()
        {

          @Override
          public String extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            if (rs.next())
            {
              return rs.getString(1);
            }
            else
            {
              return null;
            }
          }

        });
  }

  public List<DecodificaDTO<String>> queryForDecodificaString(String query,
      SqlParameterSource parameters)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<List<DecodificaDTO<String>>>()
        {
          @Override
          public List<DecodificaDTO<String>> extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            List<DecodificaDTO<String>> list = new ArrayList<DecodificaDTO<String>>();
            while (rs.next())
            {
              DecodificaDTO<String> d = new DecodificaDTO<String>();
              d.setCodice(rs.getString("CODICE"));
              d.setDescrizione(rs.getString("DESCRIZIONE"));
              try
              {
                d.setCodiceDescrizione(rs.getString("CODICE_DESCRIZIONE"));
              }
              catch (Exception e)
              {
                /* NON TUTTE LE QUERY HANNO CODICE_DESCRIZIONE TRA LE COLONNE */}
              d.setId(rs.getString("ID"));
              list.add(d);
            }
            return list;
          }
        });
  }

  public List<DecodificaDTO<Long>> queryForDecodificaLong(String query,
      SqlParameterSource parameters)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<List<DecodificaDTO<Long>>>()
        {
          @Override
          public List<DecodificaDTO<Long>> extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            List<DecodificaDTO<Long>> list = new ArrayList<DecodificaDTO<Long>>();
            while (rs.next())
            {
              DecodificaDTO<Long> d = new DecodificaDTO<Long>();
              d.setCodice(rs.getString("CODICE"));
              d.setDescrizione(rs.getString("DESCRIZIONE"));
              d.setId(rs.getLong("ID"));
              list.add(d);
            }
            return list;
          }
        });
  }

  public List<DecodificaDTO<BigDecimal>> queryForDecodificaBigDecimal(
      String query, SqlParameterSource parameters)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<List<DecodificaDTO<BigDecimal>>>()
        {
          @Override
          public List<DecodificaDTO<BigDecimal>> extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            List<DecodificaDTO<BigDecimal>> list = new ArrayList<DecodificaDTO<BigDecimal>>();
            while (rs.next())
            {
              DecodificaDTO<BigDecimal> d = new DecodificaDTO<BigDecimal>();
              d.setCodice(rs.getString("CODICE"));
              d.setDescrizione(rs.getString("DESCRIZIONE"));
              d.setId(rs.getBigDecimal("ID"));
              list.add(d);
            }
            return list;
          }
        });
  }

  public List<DecodificaDTO<Integer>> queryForDecodificaInteger(String query,
      SqlParameterSource parameters)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<List<DecodificaDTO<Integer>>>()
        {
          @Override
          public List<DecodificaDTO<Integer>> extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            List<DecodificaDTO<Integer>> list = new ArrayList<DecodificaDTO<Integer>>();
            while (rs.next())
            {
              DecodificaDTO<Integer> d = new DecodificaDTO<Integer>();
              d.setCodice(rs.getString("CODICE"));
              d.setDescrizione(rs.getString("DESCRIZIONE"));
              d.setId(rs.getInt("ID"));
              list.add(d);
            }
            return list;
          }
        });
  }

  public void lockBandoByIdProcedimento(long idProcedimento)
  {
    final String QUERY = " SELECT 1 FROM IUF_D_BANDO WHERE ID_BANDO = ( SELECT A.ID_BANDO FROM IUF_T_PROCEDIMENTO A WHERE A.ID_PROCEDIMENTO = :ID_PROCEDIMENTO )FOR UPDATE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
  }

  public void lockBando(long idBando)
  {
    final String QUERY = " SELECT 1 FROM IUF_D_BANDO WHERE ID_BANDO = :ID_BANDO FOR UPDATE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
  }

  public void lockProcedimento(long idProcedimento)
  {
    final String QUERY = " SELECT 1 FROM IUF_T_PROCEDIMENTO WHERE ID_PROCEDIMENTO = :ID_PROCEDIMENTO FOR UPDATE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
  }

  public long lockProcedimentoByIdProcedimentoOggetto(
      long idProcedimentoOggetto)
  {
    final String QUERY = " SELECT ID_PROCEDIMENTO FROM IUF_T_PROCEDIMENTO WHERE ID_PROCEDIMENTO = (SELECT PO.ID_PROCEDIMENTO FROM IUF_T_PROCEDIMENTO_OGGETTO PO WHERE PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO) FOR UPDATE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    return namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
  }

  public boolean lockProcedimentoOggetto(long idProcedimentoOggetto)
  {
    final String QUERY = " SELECT ID_PROCEDIMENTO_OGGETTO FROM IUF_T_PROCEDIMENTO_OGGETTO WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO FOR UPDATE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    return namedParameterJdbcTemplate.queryForLong(QUERY,
        mapParameterSource) != 0;
  }

  public void updateUtenteAggiornamentoProcedimentoOggetto(
      long idProcedimentoOggetto, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::updateUtenteAggiornamentoProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                                        \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO                                  \n"
        + " SET                                                           \n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO, \n"
        + "   DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE                       \n"
        + " WHERE                                                         \n"
        + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("extIdUtenteAggiornamento",
                  extIdUtenteAggiornamento)
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public void delete(String tableName, String idName, long idValue)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::delete]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " DELETE FROM " + tableName + " WHERE " + idName
        + " = " + idValue;
    ;
    try
    {
      namedParameterJdbcTemplate.update(UPDATE, (MapSqlParameterSource) null);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, UPDATE, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public void delete(String tableName, String idName, long idValue,
      String idName2, long idValue2)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::delete]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " DELETE FROM " + tableName + " WHERE " + idName
        + " = " + idValue + " AND " + idName2 + " = "
        + idValue2;
    try
    {
      namedParameterJdbcTemplate.update(UPDATE, (MapSqlParameterSource) null);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, UPDATE, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public List<DecodificaDTO<Integer>> getTabellaDecodifica(String nomeTabella,
      boolean orderByDesc)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getCompetenzeGal]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String ID = nomeTabella.replace("IUF_D", "ID");
    StringBuilder queryBuilder = new StringBuilder(" SELECT T.").append(ID)
        .append(" AS ID,\n"
            + " NULL AS CODICE, \n"
            + " T.DESCRIZIONE \n"
            + " FROM ")
        .append(nomeTabella).append(" T ORDER BY ");
    if (orderByDesc)
    {
      queryBuilder.append("T.DESCRIZIONE");
    }
    else
    {
      queryBuilder.append("T.").append(ID);
    }
    queryBuilder.append(" ASC");
    final String QUERY = queryBuilder.toString();
    try
    {
      return queryForDecodificaInteger(QUERY, (MapSqlParameterSource) null);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public Map<String, String> getParametri(String[] paramNames)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametri]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder query = new StringBuilder();
    query.append(
        " SELECT CODICE, VALORE FROM IUF_D_PARAMETRO P WHERE P.CODICE IN (");
    boolean needComma = false;
    try
    {
      int len = paramNames.length;
      MapSqlParameterSource params = new MapSqlParameterSource();
      for (int i = 0; i < len; ++i)
      {
        if (needComma)
        {
          query.append(",");
        }
        else
        {
          needComma = true;
        }
        String name = "PARAM_" + i;
        query.append(':').append(name);
        params.addValue(name, paramNames[i], Types.VARCHAR);
      }
      query.append(")");
      return null; /*namedParameterJdbcTemplate.query(query.toString(), params,
          new ResultSetExtractor<Map<String, String>>()
          {

            @Override
            public Map<String, String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<String, String> map = new HashMap<String, String>();
              while (rs.next())
              {
                map.put(rs.getString("CODICE"), rs.getString("VALORE"));
              }
              return map;
            }
          });*/
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, query.toString(), (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public List<ComuneDTO> getComuni(String idRegione, String istatProvincia,
      String flagEstinto, String flagEstero)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getComuni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder queryBuilder = new StringBuilder(
        " SELECT                       \n"
            + "   DG.ID_REGIONE,                  \n"
            + "   DG.DESCRIZIONE_REGIONE,         \n"
            + "   DG.ISTAT_PROVINCIA,             \n"
            + "   DG.SIGLA_PROVINCIA,             \n"
            + "   DG.DESCRIZIONE_PROVINCIA,       \n"
            + "   DG.ISTAT_COMUNE,                \n"
            + "   DG.DESCRIZIONE_COMUNE,          \n"
            + "   DG.CAP,                         \n"
            + "   DG.FLAG_ESTERO                  \n"
            + " FROM                              \n"
            + "   SMRGAA_V_DATI_AMMINISTRATIVI DG \n");
    boolean firstCondition = true;
    if (!GenericValidator.isBlankOrNull(idRegione))
    {
      queryBuilder.append(" WHERE DG.ID_REGIONE = :ID_REGIONE \n");
      firstCondition = false;
    }
    if (!GenericValidator.isBlankOrNull(istatProvincia))
    {
      if (firstCondition)
      {
        firstCondition = false;
        queryBuilder.append(" WHERE \n");
      }
      else
      {
        queryBuilder.append(" AND ");
      }
      queryBuilder.append(" DG.ISTAT_PROVINCIA = :ISTAT_PROVINCIA \n");
    }
    if (!GenericValidator.isBlankOrNull(flagEstinto))
    {
      if (firstCondition)
      {
        firstCondition = false;
        queryBuilder.append(" WHERE \n");
      }
      else
      {
        queryBuilder.append(" AND ");
      }
      queryBuilder.append(" DG.FLAG_ESTINTO = :FLAG_ESTINTO \n");
    }
    if (!GenericValidator.isBlankOrNull(flagEstero))
    {
      if (firstCondition)
      {
        firstCondition = false;
        queryBuilder.append(" WHERE \n");
      }
      else
      {
        queryBuilder.append(" AND ");
      }
      queryBuilder.append(" DG.FLAG_ESTERO = :FLAG_ESTERO \n");
    }
    queryBuilder.append(" ORDER BY DG.DESCRIZIONE_COMUNE ASC");
    final String QUERY = queryBuilder.toString();
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ID_REGIONE", idRegione, Types.VARCHAR);
    mapSqlParameterSource.addValue("ISTAT_PROVINCIA", istatProvincia,
        Types.VARCHAR);
    mapSqlParameterSource.addValue("FLAG_ESTINTO", flagEstinto, Types.VARCHAR);
    mapSqlParameterSource.addValue("FLAG_ESTERO", flagEstero, Types.VARCHAR);
    try
    {
      ((JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations())
          .setMaxRows(10000);
      return (List<ComuneDTO>) queryForList(QUERY, mapSqlParameterSource,
          ComuneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public List<DecodificaDTO<String>> getDecodificheComuni(String idRegione,
      String istatProvincia, String flagEstinto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDecodificheComuni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder queryBuilder = new StringBuilder(
        " SELECT                             \n"
            + "   DG.ISTAT_COMUNE AS ID,                \n"
            + "   DG.DESCRIZIONE_COMUNE AS DESCRIZIONE, \n"
            + "   DG.CAP AS CODICE             \n"
            + " FROM                                    \n"
            + "   SMRGAA_V_DATI_AMMINISTRATIVI DG       \n");
    boolean firstCondition = true;
    if (!GenericValidator.isBlankOrNull(idRegione))
    {
      queryBuilder.append(" WHERE DG.ID_REGIONE = :ID_REGIONE \n");
      firstCondition = false;
    }
    if (!GenericValidator.isBlankOrNull(istatProvincia))
    {
      if (firstCondition)
      {
        firstCondition = false;
        queryBuilder.append(" WHERE \n");
      }
      else
      {
        queryBuilder.append(" AND ");
      }
      queryBuilder.append(" DG.ISTAT_PROVINCIA = :ISTAT_PROVINCIA \n");
    }
    if (!GenericValidator.isBlankOrNull(flagEstinto))
    {
      if (firstCondition)
      {
        firstCondition = false;
        queryBuilder.append(" WHERE \n");
      }
      else
      {
        queryBuilder.append(" AND ");
      }
      queryBuilder.append(" DG.FLAG_ESTINTO = :FLAG_ESTINTO \n");
    }
    queryBuilder.append(" ORDER BY DG.DESCRIZIONE_COMUNE ASC");
    final String QUERY = queryBuilder.toString();
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ID_REGIONE", idRegione, Types.VARCHAR);
    mapSqlParameterSource.addValue("ISTAT_PROVINCIA", istatProvincia,
        Types.VARCHAR);
    mapSqlParameterSource.addValue("FLAG_ESTINTO", flagEstinto, Types.VARCHAR);
    try
    {
      return queryForDecodificaString(QUERY, mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idRegione", idRegione), new LogParameter("istatProvincia", istatProvincia), new LogParameter("flagEstinto", flagEstinto)
          },
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public List<ComuneDTO> getDecodificheComuniWidthProv(String idRegione,
      String istatProvincia, String flagEstinto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDecodificheComuni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder queryBuilder = new StringBuilder(
        " SELECT                             \n"
            + "   DG.ISTAT_COMUNE,                \n"
            + "   DG.DESCRIZIONE_COMUNE, \n"
            + "   DG.SIGLA_PROVINCIA,             \n"
            + "   DG.CAP             \n"
            + " FROM                                    \n"
            + "   SMRGAA_V_DATI_AMMINISTRATIVI DG       \n");
    boolean firstCondition = true;
    if (!GenericValidator.isBlankOrNull(idRegione))
    {
      queryBuilder.append(" WHERE DG.ID_REGIONE = :ID_REGIONE \n");
      firstCondition = false;
    }
    if (!GenericValidator.isBlankOrNull(istatProvincia))
    {
      if (firstCondition)
      {
        firstCondition = false;
        queryBuilder.append(" WHERE \n");
      }
      else
      {
        queryBuilder.append(" AND ");
      }
      queryBuilder.append(" DG.ISTAT_PROVINCIA = :ISTAT_PROVINCIA \n");
    }
    if (!GenericValidator.isBlankOrNull(flagEstinto))
    {
      if (firstCondition)
      {
        firstCondition = false;
        queryBuilder.append(" WHERE \n");
      }
      else
      {
        queryBuilder.append(" AND ");
      }
      queryBuilder.append(" DG.FLAG_ESTINTO = :FLAG_ESTINTO \n");
    }
    queryBuilder.append(" ORDER BY DG.DESCRIZIONE_COMUNE ASC");
    final String QUERY = queryBuilder.toString();
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ID_REGIONE", idRegione, Types.VARCHAR);
    mapSqlParameterSource.addValue("ISTAT_PROVINCIA", istatProvincia,
        Types.VARCHAR);
    mapSqlParameterSource.addValue("FLAG_ESTINTO", flagEstinto, Types.VARCHAR);
    try
    {
      return queryForList(QUERY, mapSqlParameterSource, ComuneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idRegione", idRegione), new LogParameter("istatProvincia", istatProvincia), new LogParameter("flagEstinto", flagEstinto)
          },
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public List<DecodificaDTO<String>> getSezioniPerComune(String istatComune)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDecodificheComuni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder queryBuilder = new StringBuilder(
        " SELECT                                           \n"
            + "   SEZIONE AS ID,                                 \n"
            + "   SEZIONE AS CODICE,                             \n"
            + "   SEZIONE || ' - ' || DESCRIZIONE AS DESCRIZIONE \n"
            + " FROM                                             \n"
            + "   DB_SEZIONE S                                   \n"
            + " WHERE                                            \n"
            + "   S.ISTAT_COMUNE = :ISTAT_COMUNE                 \n");
    final String QUERY = queryBuilder.toString();
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
    try
    {
      List<DecodificaDTO<String>> list = queryForDecodificaString(QUERY,
          mapSqlParameterSource);
      if (list.size() == 0)
      {
        list.add(new DecodificaDTO<String>("null", "null", "Non presente"));
      }
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("istatComune", istatComune) },
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public int updateProcedimOggettoQuadro(long idProcedimentoOggetto,
      long idQuadroOggetto, long idBandoOggetto,
      long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateProcedimOggettoQuadro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_PROCEDIM_OGGETTO_QUADR               \n"
        + " SET                               \n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,   \n"
        + "   DATA_ULTIMO_AGGIORNAMENTO = SYSDATE,              \n"
        + "   FLAG_COMPILATO = 'S'                      \n"
        + " WHERE                               \n"
        + "  ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       \n"
        + "  AND ID_QUADRO_OGGETTO   = :ID_QUADRO_OGGETTO           \n"
        + "  AND ID_BANDO_OGGETTO    = :ID_BANDO_OGGETTO          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    int rowElab = 0;
    try
    {
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      rowElab = namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("extIdUtenteAggiornamento",
                  extIdUtenteAggiornamento),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idQuadroOggetto", idQuadroOggetto),
              new LogParameter("idBandoOggetto", idBandoOggetto)
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
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
    return rowElab;
  }

  public int insertProcedimOggettoQuadro(long idProcedimentoOggetto,
      long idQuadroOggetto, long idBandoOggetto,
      long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateProcedimOggettoQuadro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_PROCEDIM_OGGETTO_QUADR    \n"
        + "( ID_PROCEDIM_OGGETTO_QUADRO,                \n"
        + "  ID_PROCEDIMENTO_OGGETTO,                   \n"
        + "  ID_QUADRO_OGGETTO,                         \n"
        + "  ID_BANDO_OGGETTO,                          \n"
        + "  EXT_ID_UTENTE_AGGIORNAMENTO,               \n"
        + "  DATA_ULTIMO_AGGIORNAMENTO,                 \n"
        + "  FLAG_COMPILATO                             \n"
        + ")                                            \n"
        + "VALUES                                       \n"
        + "(                                            \n"
        + "  :ID_PROCEDIM_OGGETTO_QUADRO,               \n"
        + "  :ID_PROCEDIMENTO_OGGETTO,                  \n"
        + "  :ID_QUADRO_OGGETTO,                        \n"
        + "  :ID_BANDO_OGGETTO,                         \n"
        + "  :EXT_ID_UTENTE_AGGIORNAMENTO,              \n"
        + "  SYSDATE,                                   \n"
        + "  'S'                                        \n"
        + ")                                            \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    int rowElab = 0;
    try
    {
      mapParameterSource.addValue("ID_PROCEDIM_OGGETTO_QUADRO",
          getNextSequenceValue("SEQ_IUF_T_PROCEDIM_OGGET_QUA"),
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      rowElab = namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("extIdUtenteAggiornamento",
                  extIdUtenteAggiornamento),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idQuadroOggetto", idQuadroOggetto),
              new LogParameter("idBandoOggetto", idBandoOggetto)
          },
          new LogVariable[]
          {}, INSERT, mapParameterSource);
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
    return rowElab;
  }

  public List<DecodificaDTO<String>> getProvince(final String idRegione,
      boolean visualizzaStatiEsteri)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getProvince]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        "SELECT DISTINCT                             \n"
            + "   DESCRIZIONE_PROVINCIA AS DESCRIZIONE,     \n"
            + "   ISTAT_PROVINCIA AS CODICE,                \n"
            + "   ISTAT_PROVINCIA AS ID                     \n"
            + " FROM                                        \n"
            + "   SMRGAA_V_DATI_AMMINISTRATIVI              \n"
            + " WHERE                                       \n"
            + "   ID_REGIONE = NVL(:ID_REGIONE, ID_REGIONE) \n");

    if (!visualizzaStatiEsteri)
    {
      QUERY.append("   AND FLAG_ESTERO = 'N'                     \n");
    }

    QUERY.append("   AND FLAG_ESTINTO = 'N'                    \n"
        + " ORDER BY                                    \n"
        + "   DESCRIZIONE_PROVINCIA ASC                 \n");
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ID_REGIONE", idRegione, Types.VARCHAR);
    try
    {
      return queryForDecodificaString(QUERY.toString(), mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY.toString(), (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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
  
  public List<DecodificaDTO<String>> getSiglaProvince(final String idRegione,
      boolean visualizzaStatiEsteri)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getSiglaProvince]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        "SELECT DISTINCT                             \n"
            + "   sigla_provincia AS DESCRIZIONE,     \n"
            + "   sigla_provincia AS CODICE,                \n"
            + "   ISTAT_PROVINCIA AS ID                     \n"
            + " FROM                                        \n"
            + "   SMRGAA_V_DATI_AMMINISTRATIVI              \n"
            + " WHERE                                       \n"
            + "   ID_REGIONE = NVL(:ID_REGIONE, ID_REGIONE) \n");

    if (!visualizzaStatiEsteri)
    {
      QUERY.append("   AND FLAG_ESTERO = 'N'                     \n");
    }

    QUERY.append("   AND FLAG_ESTINTO = 'N'                    \n"
        + " ORDER BY                                    \n"
        + "   sigla_provincia ASC                 \n");
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ID_REGIONE", idRegione, Types.VARCHAR);
    try
    {
      return queryForDecodificaString(QUERY.toString(), mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY.toString(), (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  protected String safeMessaggioPLSQL(String msg)
  {
    if (msg != null && msg.indexOf("ORA-") >= 0)
    {
      logger.info(
          "[BaseDAO::safeMessaggio] Il pl/sql ha ritornato un messaggio contenente informazioni non visualizzabili all'utente, messaggio: "
              + msg);
      msg = "Si è verificato un errore di sistema";
    }
    return msg;
  }

  public List<DecodificaDTO<String>> getProvincieConTerreniInConduzione(
      long idProcedimentoOggetto, String idRegione, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getProvincieConTerreniInConduzione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                       \n"
        + "   DICHIARAZIONE AS                                                         \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       MAX(DC.ID_DICHIARAZIONE_CONSISTENZA) AS ID_DICHIARAZIONE_CONSISTENZA \n"
        + "     FROM                                                                   \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                     \n"
        + "       IUF_T_PROCEDIMENTO_AZIENDA PA,                                     \n"
        + "       SMRGAA_V_DICH_CONSISTENZA DC                                         \n"
        + "     WHERE                                                                  \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                \n"
        + "       AND PO.ID_PROCEDIMENTO     = PA.ID_PROCEDIMENTO                      \n"
        + "       AND PA.DATA_FINE          IS NULL                                    \n"
        + "       AND DC.ID_AZIENDA          = PA.EXT_ID_AZIENDA                       \n"
        + "       AND DC.ID_PROCEDIMENTO     = :ID_PROCEDIMENTO_IUFFIWEB                    \n"
        + "   )                                                                        \n"
        + " SELECT DISTINCT                                                            \n"
        + "   CU.ISTAT_PROVINCIA AS CODICE,                                            \n"
        + "   CU.ISTAT_PROVINCIA AS ID,                                                \n"
        + "   CU.DESC_PROVINCIA  AS DESCRIZIONE                                        \n"
        + " FROM                                                                       \n"
        + "   SMRGAA_V_CONDUZIONE_UTILIZZO CU,                                         \n"
        + "   DICHIARAZIONE D                                                          \n"
        + " WHERE                                                                      \n"
        + "   CU.ID_DICHIARAZIONE_CONSISTENZA = D.ID_DICHIARAZIONE_CONSISTENZA         \n"
        + "   AND CU.ID_REGIONE               =  :ID_REGIONE                           \n";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ID_REGIONE", idRegione, Types.VARCHAR);
    mapSqlParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapSqlParameterSource.addValue("ID_PROCEDIMENTO_IUFFIWEB",
        idProcedimentoAgricoltura, Types.NUMERIC);
    try
    {
      return queryForDecodificaString(QUERY, mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idRegione", idRegione)
          },
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public List<DecodificaDTO<String>> getComuniPerProvinciaConTerreniInConduzione(
      long idProcedimentoOggetto,
      String istatProvincia, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getComuniPerProvinciaConTerreniInConduzione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                       \n"
        + "   DICHIARAZIONE AS                                                         \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       MAX(DC.ID_DICHIARAZIONE_CONSISTENZA) AS ID_DICHIARAZIONE_CONSISTENZA \n"
        + "     FROM                                                                   \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                       \n"
        + "       IUF_T_PROCEDIMENTO_AZIENDA PA,                                       \n"
        + "       SMRGAA_V_DICH_CONSISTENZA DC                                         \n"
        + "     WHERE                                                                  \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                \n"
        + "       AND PO.ID_PROCEDIMENTO     = PA.ID_PROCEDIMENTO                      \n"
        + "       AND PA.DATA_FINE          IS NULL                                    \n"
        + "       AND DC.ID_AZIENDA          = PA.EXT_ID_AZIENDA                       \n"
        + "       AND DC.ID_PROCEDIMENTO     = :ID_PROCEDIMENTO_IUFFIWEB                    \n"
        + "   )                                                                        \n"
        + " SELECT DISTINCT                                                            \n"
        + "   CU.COMUNE AS ID,                                                         \n"
        + "   CU.COMUNE AS CODICE,                                                     \n"
        + "   CU.DESC_COMUNE AS DESCRIZIONE                                            \n"
        + " FROM                                                                       \n"
        + "   SMRGAA_V_CONDUZIONE_UTILIZZO CU,                                         \n"
        + "   DICHIARAZIONE D                                                          \n"
        + " WHERE                                                                      \n"
        + "   CU.ID_DICHIARAZIONE_CONSISTENZA = D.ID_DICHIARAZIONE_CONSISTENZA         \n"
        + "   AND CU.ISTAT_PROVINCIA               = :ISTAT_PROVINCIA                  \n";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ISTAT_PROVINCIA", istatProvincia,
        Types.VARCHAR);
    mapSqlParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapSqlParameterSource.addValue("ID_PROCEDIMENTO_IUFFIWEB",
        idProcedimentoAgricoltura, Types.NUMERIC);
    try
    {
      return queryForDecodificaString(QUERY, mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("istatProvincia", istatProvincia)
          },
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public List<DecodificaDTO<String>> getSezioniPerComuneConTerreniInConduzione(
      long idProcedimentoOggetto,
      String istatComune, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getSezioniPerComuneConTerreniInConduzione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                       \n"
        + "   DICHIARAZIONE AS                                                         \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       MAX(DC.ID_DICHIARAZIONE_CONSISTENZA) AS ID_DICHIARAZIONE_CONSISTENZA \n"
        + "     FROM                                                                   \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                       \n"
        + "       IUF_T_PROCEDIMENTO_AZIENDA PA,                                       \n"
        + "       SMRGAA_V_DICH_CONSISTENZA DC                                         \n"
        + "     WHERE                                                                  \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                \n"
        + "       AND PO.ID_PROCEDIMENTO     = PA.ID_PROCEDIMENTO                      \n"
        + "       AND PA.DATA_FINE          IS NULL                                    \n"
        + "       AND DC.ID_AZIENDA          = PA.EXT_ID_AZIENDA                       \n"
        + "       AND DC.ID_PROCEDIMENTO     = :ID_PROCEDIMENTO_IUFFIWEB                    \n"
        + "   )                                                                        \n"
        + " SELECT DISTINCT                                                            \n"
        + "   NVL(CU.SEZIONE,'null') AS ID,                                            \n"
        + "   NVL(CU.SEZIONE,'null') AS CODICE,                                        \n"
        + "   DECODE(CU.SEZIONE, NULL, 'Non presente', CU.SEZIONE                      \n"
        + "   || ' - '                                                                 \n"
        + "   || CU.DESC_SEZIONE) AS DESCRIZIONE                                       \n"
        + " FROM                                                                       \n"
        + "   SMRGAA_V_CONDUZIONE_UTILIZZO CU,                                         \n"
        + "   DICHIARAZIONE D                                                          \n"
        + " WHERE                                                                      \n"
        + "   CU.ID_DICHIARAZIONE_CONSISTENZA = D.ID_DICHIARAZIONE_CONSISTENZA         \n"
        + "   AND CU.COMUNE                   = :ISTAT_COMUNE                          \n";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
    mapSqlParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapSqlParameterSource.addValue("ID_PROCEDIMENTO_IUFFIWEB",
        idProcedimentoAgricoltura, Types.NUMERIC);
    try
    {
      return queryForDecodificaString(QUERY, mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("istatComune", istatComune)
          },
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public List<RigaJSONConduzioneInteventoDTO> ricercaConduzioni(
      FiltroRicercaConduzioni filtro, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::ricercaConduzioni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String[] chiaviConduzioniInserite = filtro.getChiaviConduzioniInserite();
    boolean chiaviConduzioniInseriteEmpty = chiaviConduzioniInserite == null
        || chiaviConduzioniInserite.length == 0;
    List<String> listChiavi = null;
    if (!chiaviConduzioniInseriteEmpty)
    {
      listChiavi = new ArrayList<String>();
      for (String chiave : chiaviConduzioniInserite)
      {
        if (chiave != null)
        {
          // Rimuovo tutti i caratteri che non siano numeri o underscore per
          // evitare sql-injection dato che poi la
          // notInCondition in
          // caso di stringhe non fa escape
          chiave = chiave.replaceAll("[^0-9_]+", "");
        }
        listChiavi.add(chiave);
      }
    }
    final String QUERY = new StringBuilder(
        " SELECT                                                      \n"
            + "   CU.ID_DICHIARAZIONE_CONSISTENZA,                          \n"
            + "   CU.ID_CONDUZIONE_DICHIARATA,                              \n"
            + "   CU.ID_UTILIZZO_DICHIARATO,                                \n"
            + "   CU.COMUNE AS ISTAT_COMUNE,                                \n"
            + "   CU.DESC_COMUNE || ' (' || CU.SIGLA_PROVINCIA || ')'       \n"
            + "         AS DESC_COMUNE,                                     \n"
            + "   CU.SEZIONE,                                               \n"
            + "   CU.FOGLIO,                                                \n"
            + "   CU.PARTICELLA,                                            \n"
            + "   CU.SUBALTERNO,                                            \n"
            + "   CU.SUP_CATASTALE,                                         \n"
            + "   CU.DESC_TIPO_UTILIZZO,                                    \n"
            + "   DECODE(CU.COD_TIPO_UTILIZZO, NULL, '','[' || CU.COD_TIPO_UTILIZZO || '] ')               \n"
            + "   || CU.DESC_TIPO_UTILIZZO AS DESC_TIPO_UTILIZZO,                                          \n"
            + "   DECODE(CU.CODICE_DESTINAZIONE, NULL, '','[' || CU.CODICE_DESTINAZIONE || '] ')           \n"
            + "   || CU.DESCRIZIONE_DESTINAZIONE AS DESCRIZIONE_DESTINAZIONE,                              \n"
            + "   DECODE(CU.COD_DETTAGLIO_USO, NULL, '','[' || CU.COD_DETTAGLIO_USO || '] ')               \n"
            + "   || CU.DESC_TIPO_DETTAGLIO_USO AS DESC_TIPO_DETTAGLIO_USO,                                \n"
            + "   DECODE(CU.CODICE_QUALITA_USO, NULL, '','[' || CU.CODICE_QUALITA_USO || '] ')             \n"
            + "   || CU.DESCRIZIONE_QUALITA_USO AS DESCRIZIONE_QUALITA_USO,                                \n"
            + "   DECODE(CU.COD_TIPO_VARIETA, NULL, '','[' || CU.COD_TIPO_VARIETA || '] ')                 \n"
            + "   || CU.DESC_TIPO_VARIETA AS DESC_TIPO_VARIETA,                                            \n"
            + " '[' || CU.COD_TIPO_VARIETA || ']' ||    \n"
            + "   CU.DESC_TIPO_VARIETA AS VARIETA,      \n"
            + "   '[' || CU.COD_DETTAGLIO_USO || ']' || \n"
            + "   CU.DESC_TIPO_DETTAGLIO_USO AS USO,    \n"
            + "   CU.SUPERFICIE_UTILIZZATA                                  \n"
            + " FROM                                                        \n"
            + "   SMRGAA_V_CONDUZIONE_UTILIZZO CU                           \n"
            + " WHERE                                                       \n"
            + "   CU.ID_DICHIARAZIONE_CONSISTENZA =   (                     \n"
            + "     SELECT                                                  \n"
            + "       MAX(DC.ID_DICHIARAZIONE_CONSISTENZA)                  \n"
            + "     FROM                                                    \n"
            + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                        \n"
            + "       IUF_T_PROCEDIMENTO_AZIENDA PA,                        \n"
            + "       SMRGAA_V_DICH_CONSISTENZA DC                          \n"
            + "     WHERE                                                   \n"
            + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
            + "       AND PO.ID_PROCEDIMENTO     = PA.ID_PROCEDIMENTO       \n"
            + "       AND PA.DATA_FINE          IS NULL                     \n"
            + "       AND DC.ID_AZIENDA          = PA.EXT_ID_AZIENDA        \n"
            + "       AND DC.ID_PROCEDIMENTO     = :ID_PROCEDIMENTO_IUFFI   \n"
            + "       AND DC.ESCLUSO            <> 'S'                      \n"
            + "   )                                                         \n"
            + "   AND CU.COMUNE                   = :ISTAT_COMUNE           \n"
            + "   AND CU.FOGLIO = :FOGLIO                                   \n"
            + "   AND NVL(CU.SEZIONE,'--') = NVL(:SEZIONE,'--')             \n")
                .append(filtro.getParticella() == null ? ""
                    : "   AND CU.PARTICELLA = :PARTICELLA                     \n")
                .append(
                    GenericValidator.isBlankOrNull(filtro.getSubalterno()) ? ""
                        : "   AND CU.SUBALTERNO = :SUBALTERNO                     \n")
                .append(
                    //protetto da SQL Injection
                		chiaviConduzioniInseriteEmpty ? ""
                        : getNotInCondition(
                            "ID_DICHIARAZIONE_CONSISTENZA || '_' || ID_CONDUZIONE_DICHIARATA || '_' || ID_UTILIZZO_DICHIARATO",
                            listChiavi))
                .append(
                    "   ORDER BY CU.SEZIONE, CU.PARTICELLA, CU.SUBALTERNO, CU.DESC_TIPO_UTILIZZO\n")
                .toString();
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          filtro.getIdProcedimentoOggetto(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_IUFFI",
          idProcedimentoAgricoltura, Types.VARCHAR);
      mapParameterSource.addValue("ISTAT_COMUNE", filtro.getIstatComune(),
          Types.VARCHAR);
      mapParameterSource.addValue("SEZIONE", filtro.getSezione(),
          Types.VARCHAR);
      mapParameterSource.addValue("FOGLIO", filtro.getFoglio(), Types.NUMERIC);
      mapParameterSource.addValue("PARTICELLA", filtro.getParticella(),
          Types.NUMERIC);
      mapParameterSource.addValue("SUBALTERNO", filtro.getSubalterno(),
          Types.VARCHAR);
      return (List<RigaJSONConduzioneInteventoDTO>) queryForList(QUERY,
          mapParameterSource,
          RigaJSONConduzioneInteventoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("filtro", filtro)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public String getParametroComune(String idParametro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametroComune]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT VALORE FROM DB_PARAMETRO WHERE ID_PARAMETRO = :ID_PARAMETRO";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PARAMETRO", idParametro, Types.VARCHAR);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getString(1);
              }
              else
              {
                return null;
              }
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idParametro", idParametro)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public Map<String, String> getParametriComune(String... idParametro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametroComune]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (idParametro == null)
    {
      logger.error(
          THIS_METHOD + " idParametro è null! NullPointerException in arrivo!");
    }
    StringBuilder queryBuilder = new StringBuilder(
        "SELECT ID_PARAMETRO, VALORE FROM DB_PARAMETRO WHERE ID_PARAMETRO IN (");
    int i = 1;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    for (String id : idParametro)
    {
      if (i > 1)
      {
        queryBuilder.append(",");
      }
      String paramName = "PARAMETRO" + i;
      mapParameterSource.addValue(paramName, id, Types.VARCHAR);
      queryBuilder.append(":" + paramName);
      ++i;
    }
    queryBuilder.append(")");
    final String QUERY = queryBuilder.toString();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<String, String>>()
          {
            @Override
            public Map<String, String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<String, String> map = new HashMap<String, String>();
              while (rs.next())
              {
                map.put(rs.getString("ID_PARAMETRO"), rs.getString("VALORE"));
              }
              return map;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idParametro", idParametro)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public Map<String, String> getMapHelpCdu(String... codCdu)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametroComune]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (codCdu == null)
    {
      logger.error(
          THIS_METHOD + " codcdu è null! NullPointerException in arrivo!");
    }
    StringBuilder queryBuilder = new StringBuilder(
        "SELECT CODICE_CDU, HELP_CDU FROM IUF_D_ELENCO_CDU WHERE CODICE_CDU IN (");
    int i = 1;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    for (String id : codCdu)
    {
      if (i > 1)
      {
        queryBuilder.append(",");
      }
      String paramName = "CODICE_CDU" + i;
      mapParameterSource.addValue(paramName, id, Types.VARCHAR);
      queryBuilder.append(":" + paramName);
      ++i;
    }
    queryBuilder.append(")");
    final String QUERY = queryBuilder.toString();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<String, String>>()
          {
            @Override
            public Map<String, String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<String, String> map = new HashMap<String, String>();
              while (rs.next())
              {
                map.put(rs.getString("CODICE_CDU"), rs.getString("HELP_CDU"));
              }
              return map;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("codCdu", codCdu)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public LogOperationOggettoQuadroDTO getIdUtenteUltimoModifica(
      long idProcediemntoOggetto, long idQuadroOggetto,
      long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getIdUtenteUltimoModifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                  	\n"
        + "  *                                    									\n"
        + " FROM                                                    				\n"
        + "  IUF_T_PROCEDIM_OGGETTO_QUADR                          				\n"
        + " WHERE                                                   				\n"
        + "  ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO          		  					\n"
        + "  AND ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO          	  				\n"
        + "  AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 				\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcediemntoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource,
          LogOperationOggettoQuadroDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcediemntoOggetto),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto),
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public int updateProcedimento(long idProcedimento, long idStatoOggetto,
      long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateProcedimento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_PROCEDIMENTO               			\n"
        + " SET                               								\n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,   \n"
        + "   DATA_ULTIMO_AGGIORNAMENTO = SYSDATE,              			\n"
        + "   ID_STATO_OGGETTO = :ID_STATO_OGGETTO                      	\n"
        + " WHERE                               							\n"
        + "  ID_PROCEDIMENTO = :ID_PROCEDIMENTO       						\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    int rowElab = 0;
    try
    {
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("ID_STATO_OGGETTO", idStatoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      rowElab = namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("extIdUtenteAggiornamento",
                  extIdUtenteAggiornamento),
              new LogParameter("idProcedimento", idProcedimento),
              new LogParameter("idStatoOggetto", idStatoOggetto)
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
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
    return rowElab;
  }

  public int updateProcedimentoOggetto(long idProcedimentoOggetto, long idEsito,
      long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_PROCEDIMENTO_OGGETTO               				  	\n"
        + " SET                               								              	\n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,   				\n"
        + "   ID_ESITO = :ID_ESITO                      					          		\n"
        + " WHERE                               							              	\n"
        + "  ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       		  				\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    int rowElab = 0;
    try
    {
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("ID_ESITO", idEsito, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      rowElab = namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("extIdUtenteAggiornamento",
                  extIdUtenteAggiornamento),
              new LogParameter("idEsito", idEsito),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
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
    return rowElab;
  }

  public List<String[]> getStatoDatabase() throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getStatoDatabase]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                  \n"
        + "   OBJECT_NAME,                                                         \n"
        + "   STATUS                                                               \n"
        + " FROM                                                                   \n"
        + "   ALL_OBJECTS                                                          \n"
        + " WHERE                                                                  \n"
        + "   OWNER            = 'IUFFI'                                             \n"
        + "   AND OBJECT_TYPE IN ('PACKAGE','PACKAGE BODY','PROCEDURE','FUNCTION') \n"
        + " ORDER BY                                                               \n"
        + "   OBJECT_NAME ASC                                                      \n";
    try
    {
      return namedParameterJdbcTemplate.query(QUERY,
          (MapSqlParameterSource) null,
          new ResultSetExtractor<List<String[]>>()
          {

            @Override
            public List<String[]> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<String[]> list = new ArrayList<String[]>();
              while (rs.next())
              {
                list.add(new String[]
                { rs.getString("OBJECT_NAME"), rs.getString("STATUS"),
                    null /*
                          * Verrà riempito dalla controller con il nome
                          * dell'icona da visualizzare
                          */ });
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public Date getSysDate() throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getSysDate]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT SYSDATE FROM DUAL \n";
    try
    {
      return namedParameterJdbcTemplate.queryForObject(QUERY,
          (MapSqlParameterSource) null, Date.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getInformazioniBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                            \n"
          + "   B.ID_BANDO,                                                        \n"
          + "   B.ID_TIPO_LIVELLO,                                                 \n"
          + "   B.REFERENTE_BANDO,                                                 \n"
          + "   B.EMAIL_REFERENTE_BANDO,                                           \n"
          + "   B.FLAG_RIBASSO_INTERVENTI,                                         \n"
          + "   B.FLAG_MASTER,                                                     \n"
          + "   B.FLAG_DOMANDA_MULTIPLA,                                           \n"
          + "   B.DENOMINAZIONE,                                           		   \n"
          + "   B.ISTRUZIONE_SQL_FILTRO,                                           \n"
          + "   B.DESCRIZIONE_FILTRO,                                              \n"
          + "   DECODE(B.FLAG_MASTER,'S',NULL,B.DENOMINAZIONE) AS DENOMINAZIONE,   \n"
          + "   B.DATA_INIZIO,        											   \n"
          + "   B.DATA_FINE,        											   \n"
          + "   B.PERC_CONTRIBUTO_EROGABILE,        											   \n"
          + "   B.PERC_CONTRIBUTO_MAX_CONCESSA,        											   \n"
          + "   C.CODICE AS COD_TIPOLOGIA,                                         \n"
          + "   C.DESCRIZIONE AS TIPOLOGIA,                                        \n"
          + "   NVL(B.ANNO_CAMPAGNA,TO_CHAR(SYSDATE, 'YYYY')) AS ANNO_CAMPAGNA,    \n"
          + "   NVL(B.FLAG_TITOLARITA_REGIONALE, 'N') AS FLAG_TITOLARITA_REGIONALE, \n"
          + "   NVL(D.ID_BANDO_MASTER, B.ID_BANDO) AS ID_BANDO_MASTER              \n"
          + " FROM                                                                 \n"
          + "   IUF_D_BANDO B,                                                     \n"
          + "   IUF_D_TIPO_LIVELLO C,                                              \n"
          + "   IUF_R_BANDO_MASTER D                                               \n"
          + " WHERE                                                                \n"
          + "   C.ID_TIPO_LIVELLO = B.ID_TIPO_LIVELLO                              \n"
          + "   AND D.ID_BANDO(+) = B.ID_BANDO		                               \n"
          + "   AND B.ID_BANDO = :ID_BANDO                                         \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BandoDTO>()
          {
            @Override
            public BandoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BandoDTO bando = new BandoDTO();
              while (rs.next())
              {
                String flagMaster = rs.getString("FLAG_MASTER");
                bando.setIdBando(rs.getLong("ID_BANDO"));
                bando.setIdBandoMaster(rs.getLong("ID_BANDO_MASTER"));
                bando.setIdTipoLivello(rs.getLong("ID_TIPO_LIVELLO"));
                bando.setFlagTitolaritaRegionale(
                    rs.getString("FLAG_TITOLARITA_REGIONALE"));
                bando.setFlagDomandaMultipla(
                    rs.getString("FLAG_DOMANDA_MULTIPLA"));
                bando.setDenominazione(rs.getString("DENOMINAZIONE"));
                bando.setDescrTipoBando(rs.getString("TIPOLOGIA"));
                bando.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                bando.setCodiceTipoBando(rs.getString("COD_TIPOLOGIA"));
                bando.setDescrizioneFiltro(rs.getString("DESCRIZIONE_FILTRO"));
                bando.setIstruzioneSqlFiltro(
                    rs.getString("ISTRUZIONE_SQL_FILTRO"));
                bando.setReferenteBando(rs.getString("REFERENTE_BANDO"));
                bando.setEmailReferenteBando(
                    rs.getString("EMAIL_REFERENTE_BANDO"));
                bando.setFlagRibassoInterventi(
                    rs.getString("FLAG_RIBASSO_INTERVENTI"));
                bando.setDenominazione(rs.getString("DENOMINAZIONE"));
                bando.setFlagMaster(flagMaster);
                bando.setPercContributoErogabile(rs.getBigDecimal("PERC_CONTRIBUTO_EROGABILE"));
                bando.setPercContributoMaxConcessa(rs.getBigDecimal("PERC_CONTRIBUTO_MAX_CONCESSA"));
                if (flagMaster.equals("N"))
                {
                  bando.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                  bando.setDataFine(rs.getTimestamp("DATA_FINE"));
                }
              }
              return bando;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
      }
    }
  }

  public BandoDTO getInformazioniBandoByIdProcedimento(long idProcedimento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getInformazioniBandoByIdProcedimento";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                            \n"
          + "   B.ID_BANDO,                                                        \n"
          + "   B.ID_TIPO_LIVELLO,                                                 \n"
          + "   B.REFERENTE_BANDO,                                                 \n"
          + "   B.EMAIL_REFERENTE_BANDO,                                           \n"
          + "   B.FLAG_RIBASSO_INTERVENTI,                                         \n"
          + "   B.FLAG_MASTER,                                                     \n"
          + "   B.FLAG_DOMANDA_MULTIPLA,                                           \n"
          + "   B.ISTRUZIONE_SQL_FILTRO,                                           \n"
          + "   B.DESCRIZIONE_FILTRO,                                              \n"
          + "   DECODE(B.FLAG_MASTER,'S',NULL,B.DENOMINAZIONE) AS DENOMINAZIONE,   \n"
          + "   B.DATA_INIZIO,        											   \n"
          + "   B.DATA_FINE,        											   \n"
          + "   B.PERC_CONTRIBUTO_MAX_CONCESSA,        											   \n"
          + "   B.PERC_CONTRIBUTO_EROGABILE,        											   \n"
          + "   B.ID_PROCEDIMENTO_AGRICOLO, "
          + "   C.CODICE AS COD_TIPOLOGIA,                                         \n"
          + "   C.DESCRIZIONE AS TIPOLOGIA,                                        \n"
          + "   NVL(B.ANNO_CAMPAGNA,TO_CHAR(SYSDATE, 'YYYY')) AS ANNO_CAMPAGNA,    \n"
          + "   NVL(B.FLAG_TITOLARITA_REGIONALE, 'N') AS FLAG_TITOLARITA_REGIONALE, \n"
          + "   NVL(D.ID_BANDO_MASTER, B.ID_BANDO) AS ID_BANDO_MASTER              \n"
          + " FROM                                                                 \n"
          + "   IUF_T_PROCEDIMENTO P,                                              \n"
          + "   IUF_D_BANDO B,                                                     \n"
          + "   IUF_D_TIPO_LIVELLO C,                                              \n"
          + "   IUF_R_BANDO_MASTER D                                               \n"
          + " WHERE                                                                \n"
          + "   P.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                               \n"
          + "   AND P.ID_BANDO = B.ID_BANDO                            			   \n"
          + "   AND C.ID_TIPO_LIVELLO = B.ID_TIPO_LIVELLO                          \n"
          + "   AND D.ID_BANDO(+) = B.ID_BANDO		                               \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BandoDTO>()
          {
            @Override
            public BandoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BandoDTO bando = new BandoDTO();
              while (rs.next())
              {
                String flagMaster = rs.getString("FLAG_MASTER");
                bando.setIdBando(rs.getLong("ID_BANDO"));
                bando.setIdBandoMaster(rs.getLong("ID_BANDO_MASTER"));
                bando.setIdTipoLivello(rs.getLong("ID_TIPO_LIVELLO"));
                bando.setFlagTitolaritaRegionale(
                    rs.getString("FLAG_TITOLARITA_REGIONALE"));
                bando.setFlagDomandaMultipla(
                    rs.getString("FLAG_DOMANDA_MULTIPLA"));
                bando.setDenominazione(rs.getString("DENOMINAZIONE"));
                bando.setDescrTipoBando(rs.getString("TIPOLOGIA"));
                bando.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                bando.setCodiceTipoBando(rs.getString("COD_TIPOLOGIA"));
                bando.setDescrizioneFiltro(rs.getString("DESCRIZIONE_FILTRO"));
                bando.setIstruzioneSqlFiltro(
                    rs.getString("ISTRUZIONE_SQL_FILTRO"));
                bando.setReferenteBando(rs.getString("REFERENTE_BANDO"));
                bando.setEmailReferenteBando(
                    rs.getString("EMAIL_REFERENTE_BANDO"));
                bando.setFlagRibassoInterventi(
                    rs.getString("FLAG_RIBASSO_INTERVENTI"));

                bando.setFlagMaster(flagMaster);
                bando.setPercContributoErogabile(rs.getBigDecimal("PERC_CONTRIBUTO_EROGABILE"));
                bando.setPercContributoMaxConcessa(rs.getBigDecimal("PERC_CONTRIBUTO_MAX_CONCESSA"));
                bando.setIdProcedimentoAgricolo(rs.getInt("ID_PROCEDIMENTO_AGRICOLO"));
                if (flagMaster.equals("N"))
                {
                  bando.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                  bando.setDataFine(rs.getTimestamp("DATA_FINE"));
                }
              }
              return bando;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
      }
    }
  }

  protected BigDecimal setBigDecimalScale(BigDecimal bigDecimal, int scale)
  {
    if (bigDecimal != null)
    {
      if (bigDecimal.scale() != scale)
      {
        bigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP);
      }
    }
    return bigDecimal;
  }

  public ComuneDTO getComune(String istatComune)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getComune";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                   \n"
        + "   DG.ID_REGIONE,                  \n"
        + "   DG.DESCRIZIONE_REGIONE,         \n"
        + "   DG.ISTAT_PROVINCIA,             \n"
        + "   DG.SIGLA_PROVINCIA,             \n"
        + "   DG.DESCRIZIONE_PROVINCIA,       \n"
        + "   DG.ISTAT_COMUNE,                \n"
        + "   DG.DESCRIZIONE_COMUNE,          \n"
        + "   DG.CAP,                         \n"
        + "   DG.FLAG_ESTERO                  \n"
        + " FROM                              \n"
        + "   SMRGAA_V_DATI_AMMINISTRATIVI DG \n"
        + " WHERE                             \n"
        + "   DG.ISTAT_COMUNE = :ISTAT_COMUNE \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
    try
    {
      return queryForObject(QUERY, mapParameterSource, ComuneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          // new LogParameter("idVariabile", idVariabile),
          },
          new LogVariable[]
          {
          // new LogParameter("idVariabile", idVariabile),
          }, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  protected String getInCondition(String column, long[] ids)
  {
    int cicli = ids.length / PASSO;
    if (ids.length % PASSO != 0)
      cicli++;

    StringBuffer condition = new StringBuffer(" AND ( ");
    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < ids.length; i++)
      {
        if (primo)
        {
          condition.append(" ").append(column).append(" IN (").append(ids[i]);
          primo = false;
        }
        else
        {
          condition.append(",").append(ids[i]);
        }
      }
      condition.append(")");
    }
    condition.append(")");

    return condition.toString();
  }

//  protected String getInCondition(String column, String[] ids)
//  {
//    int cicli = ids.length / PASSO;
//    if (ids.length % PASSO != 0)
//      cicli++;
//
//    StringBuffer condition = new StringBuffer(" AND ( ");
//    for (int j = 0; j < cicli; j++)
//    {
//      if (j != 0)
//      {
//        condition.append(" OR ");
//      }
//      boolean primo = true;
//      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < ids.length; i++)
//      {
//        if (primo)
//        {
//          condition.append(" ").append(column).append(" IN (").append(ids[i]);
//          primo = false;
//        }
//        else
//        {
//          condition.append(",").append(ids[i]);
//        }
//      }
//      condition.append(")");
//    }
//    condition.append(")");
//
//    return condition.toString();
//  }

  public ProcedimentoEProcedimentoOggetto getProcedimentoEProcedimentoOggettoByIdProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getProcedimentoEProcedimentoOggettoByIdProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                 \n"
        + "   PO.ID_PROCEDIMENTO,                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO,                                         \n"
        + "   PO.EXT_COD_ATTORE              AS CODICE_ATTORE,                    \n"
        + "   PO.EXT_ID_UTENTE_AGGIORNAMENTO AS ID_UTENTE_AGGIORNAMENTO,          \n"
        + "   PO.DATA_FINE,                                                       \n"
        + "   PO.DATA_INIZIO,                                                     \n"
        + "   PO.ID_ESITO,                                                        \n"
        + "   PO.IDENTIFICATIVO,                                                  \n"
        + "   PO.FLAG_PREMIO_ACCERTATO,                                           \n"
        + "   PO.CODICE_RAGGRUPPAMENTO,                                           \n"
        + "   PO.EXT_ID_DICHIARAZIONE_CONSISTEN,                                  \n"
        + "   PO.FLAG_VALIDAZIONE_GRAFICA,                                        \n"
        + "   PO.ID_LEGAME_GRUPPO_OGGETTO,                                        \n"
        + "   O.FLAG_ISTANZA,                                                     \n"
        + "   O.TIPO_PAGAMENTO_SIGOP,                                             \n"
        + "   O.FLAG_AMMISSIONE,                                                  \n"
        + "   O.DESCRIZIONE,                                                      \n"
        + "   IPO.DATA_INIZIO AS DATA_INIZIO_LAST_ITER,                           \n"
        + "   IPO.ID_STATO_OGGETTO,                                               \n"
        + "   E.DESCRIZIONE  AS DESC_ESITO,                                       \n"
        + "   E.CODICE AS CODICE_ESITO,                                           \n"
        + "   SO.DESCRIZIONE AS DESC_STATO ,                                      \n"
        + "   O.CODICE     AS CODICE_OGGETTO,                      				  \n"
        + "   IPO2.NOTE      AS SPECIFICITA,                                      \n"
        + "   P.ID_PROCEDIMENTO_AGRICOLO,                						  \n"
        + "   P.ID_STATO_OGGETTO            AS P_ID_STATO_OGGETTO,                \n"
        + "   P.FLAG_RENDICONTAZIONE_CON_IVA,                             		  \n"
        + "   P.ID_BANDO                    AS P_ID_BANDO,                        \n"
        + "   P.IDENTIFICATIVO              AS P_IDENTIFICATIVO,                  \n"
        + "   P.EXT_ID_UTENTE_AGGIORNAMENTO AS P_EXT_ID_UTENTE_AGGIORNAMENTO,     \n"
        + "   P.DATA_ULTIMO_AGGIORNAMENTO   AS P_DATA_ULTIMO_AGGIORNAMENTO,       \n"
        + "   (SELECT B1.FLAG_RENDICONTAZIONE_DOC_SPESA FROM IUF_D_BANDO B1 WHERE B1.ID_BANDO = P.ID_BANDO ) AS FLAG_RENDICONTAZIONE_DOC_SPESA,       								\n"
        + "   (SELECT TL.CODICE FROM IUF_D_TIPO_LIVELLO TL, IUF_D_BANDO B1 WHERE B1.ID_BANDO = P.ID_BANDO AND B1.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO ) AS COD_TIPO_LIVELLO,       	\n"
        + "   SO2.DESCRIZIONE               AS P_DESCR_STATO                     \n"
        + " FROM                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                      \n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                    \n"
        + "   IUF_D_OGGETTO O,                                                    \n"
        + "   IUF_D_ESITO E,                                                      \n"
        + "   IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                                 \n"
        + "   IUF_T_ITER_PROCEDIMENTO_OGGE IPO2,                                \n"
        + "   IUF_D_STATO_OGGETTO SO,                                             \n"
        + "   IUF_T_PROCEDIMENTO P,                                               \n"
        + "   IUF_D_STATO_OGGETTO SO2                                             \n"
        + " WHERE                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO           = :ID_PROCEDIMENTO_OGGETTO     \n"
        + "   AND PO.ID_LEGAME_GRUPPO_OGGETTO      = LGO.ID_LEGAME_GRUPPO_OGGETTO \n"
        + "   AND LGO.ID_OGGETTO                   = O.ID_OGGETTO                 \n"
        + "   AND PO.ID_ESITO                      = E.ID_ESITO(+)                \n"
        + "   AND PO.ID_PROCEDIMENTO_OGGETTO       = IPO.ID_PROCEDIMENTO_OGGETTO  \n"
        + "   AND IPO.DATA_FINE                   IS NULL                         \n"
        + "   AND IPO.ID_STATO_OGGETTO             = SO.ID_STATO_OGGETTO          \n"
        + "   AND IPO2.ID_ITER_PROCEDIMENTO_OGGETT =                              \n"
        + "   (                                                                   \n"
        + "     SELECT                                                            \n"
        + "       MIN(ID_ITER_PROCEDIMENTO_OGGETT)                                \n"
        + "     FROM                                                              \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO2                             \n"
        + "     WHERE                                                             \n"
        + "       IPO2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO         \n"
        + "   )                                                                   \n"
        + "   AND PO.ID_PROCEDIMENTO  = P.ID_PROCEDIMENTO                         \n"
        + "   AND P.ID_STATO_OGGETTO = SO2.ID_STATO_OGGETTO                       \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<ProcedimentoEProcedimentoOggetto>()
          {
            @Override
            public ProcedimentoEProcedimentoOggetto extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ProcedimentoEProcedimentoOggetto result = null;
              if (rs.next())
              {
                result = new ProcedimentoEProcedimentoOggetto();
                ProcedimentoOggetto po = new ProcedimentoOggetto();
                po.setIdProcedimento(rs.getLong("ID_PROCEDIMENTO"));
                po.setIdProcedimentoOggetto(
                    rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
                po.setCodiceAttore(rs.getString("CODICE_ATTORE"));
                po.setCodiceRaggruppamento(rs.getLong("CODICE_RAGGRUPPAMENTO"));
                po.setIdUtenteAggiornamento(
                    rs.getLong("ID_UTENTE_AGGIORNAMENTO"));
                po.setDataFine(rs.getTimestamp("DATA_FINE"));
                po.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                po.setIdEsito(getLongNull(rs, "ID_ESITO"));
                po.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                po.setFlagPremioAccertato(
                    rs.getString("FLAG_PREMIO_ACCERTATO"));
                po.setFlagValidazioneGrafica(
                    rs.getString("FLAG_VALIDAZIONE_GRAFICA"));
                po.setDescrizione(rs.getString("DESCRIZIONE"));
                po.setFlagIstanza(rs.getString("FLAG_ISTANZA"));
                po.setTipoPagamentoSigop(rs.getString("TIPO_PAGAMENTO_SIGOP"));
                po.setFlagAmmissione(rs.getString("FLAG_AMMISSIONE"));
                po.setDataInizioLastIter(
                    rs.getTimestamp("DATA_INIZIO_LAST_ITER"));
                po.setIdStatoOggetto(getLongNull(rs, "ID_STATO_OGGETTO"));
                po.setDescEsito(rs.getString("DESC_ESITO"));
                po.setCodiceEsito(rs.getString("CODICE_ESITO"));
                po.setDescStato(rs.getString("DESC_STATO"));
                po.setCodOggetto(rs.getString("CODICE_OGGETTO"));
                po.setSpecificita(rs.getString("SPECIFICITA"));
                po.setExtIdDichiarazioneConsistenza(
                    rs.getLong("EXT_ID_DICHIARAZIONE_CONSISTEN"));
                po.setiDLegameGruppoOggetto(rs.getLong("ID_LEGAME_GRUPPO_OGGETTO"));
                result.setProcedimentoOggetto(po);
                Procedimento p = new Procedimento();
                p.setIdProcedimento(po.getIdProcedimento());
                p.setDataUltimoAggiornamento(
                    rs.getTimestamp("P_DATA_ULTIMO_AGGIORNAMENTO"));
                p.setIdentificativo(rs.getString("P_IDENTIFICATIVO"));
                p.setDescrStato(rs.getString("P_DESCR_STATO"));
                p.setIdBando(rs.getLong("P_ID_BANDO"));
                p.setIdStatoOggetto(rs.getLong("P_ID_STATO_OGGETTO"));
                p.setExtIdUtenteAggiornamento(
                    rs.getLong("P_EXT_ID_UTENTE_AGGIORNAMENTO"));
                p.setFlagRendicontazioneConIva(
                    rs.getString("FLAG_RENDICONTAZIONE_CON_IVA"));
                p.setFlagRendicontazioneDocSpesa(
                    rs.getString("FLAG_RENDICONTAZIONE_DOC_SPESA"));
                p.setCodiceTipoBando(rs.getString("COD_TIPO_LIVELLO"));
                p.setIdProcedimentoAgricolo(rs.getInt("ID_PROCEDIMENTO_AGRICOLO"));
                result.setProcedimento(p);
              }
              return result;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public Procedimento getProcedimento(long idProcedimento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getProcedimento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT                        		\n"
        + "   P.ID_PROCEDIMENTO,                              	\n"
        + "   P.ID_STATO_OGGETTO,                             	\n"
        + "   P.ID_BANDO,                             			\n"
        + "   P.IDENTIFICATIVO,                             	\n"
        + "   P.FLAG_RENDICONTAZIONE_CON_IVA,                   \n"
        + "   P.ID_PROCEDIMENTO_AGRICOLO,                   	\n"
        + "   (SELECT B1.FLAG_RENDICONTAZIONE_DOC_SPESA FROM IUF_D_BANDO B1 WHERE B1.ID_BANDO = P.ID_BANDO ) AS FLAG_RENDICONTAZIONE_DOC_SPESA,       								\n"
        + "   P.DATA_ULTIMO_AGGIORNAMENTO,            			\n"
        + "   SO.DESCRIZIONE AS DESCR_STATO                     \n"
        + " FROM                                                \n"
        + "   IUF_T_PROCEDIMENTO P,                         	\n"
        + "   IUF_D_STATO_OGGETTO SO                        	\n"
        + " WHERE                                               \n"
        + "   P.ID_PROCEDIMENTO = :ID_PROCEDIMENTO        		\n"
        + "   AND P.ID_STATO_OGGETTO = SO.ID_STATO_OGGETTO    	\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource, Procedimento.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO", idProcedimento)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public List<QuadroOggettoDTO> getQuadriProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getQuadriProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                                \n"
        + "         Q.ID_QUADRO,                                                  \n"
        + "         Q.CODICE AS COD_QUADRO,                                       \n"
        + "         Q.DESCRIZIONE AS DESC_QUADRO,                                 \n"
        + "         QO.ID_QUADRO_OGGETTO,                                         \n"
        + "         QO.ID_OGGETTO,                                                \n"
        + "         BO.ID_BANDO_OGGETTO,                                          \n"
        + "         QO.FLAG_OBBLIGATORIO,                                         \n"
        + "         QO.ORDINE AS QO_ORDINE,                                       \n"
        + "         A.CODICE CODICE_AZIONE,                                       \n"
        + "         A.LABEL LABEL_AZIONE,                                         \n"
        + "         EC.CODICE_CDU,                                                \n"
        + "         EC.TIPO_AZIONE,                                               \n"
        + "         QOA.PRIORITA,                                                 \n"
        + "         QOA.EXT_COD_MACRO_CDU                                         \n"
        + "       FROM                                                            \n"
        + "         IUF_T_PROCEDIMENTO_OGGETTO PO,                                \n"
        + "         IUF_T_PROCEDIMENTO P,                                         \n"
        + "         IUF_R_BANDO_OGGETTO BO,                                       \n"
        + "         IUF_R_BANDO_OGGETTO_QUADRO BOQ,                               \n"
        + "         IUF_R_QUADRO_OGGETTO QO,                                      \n"
        + "         IUF_R_QUADRO_OGGETTO_AZIONE QOA,                              \n"
        + "         IUF_D_AZIONE A,                                               \n"
        + "         IUF_D_ELENCO_CDU EC,                                          \n"
        + "         IUF_D_QUADRO Q                                                \n"
        + "       WHERE                                                           \n"
        + "         PO.ID_PROCEDIMENTO_OGGETTO      = :ID_PROCEDIMENTO_OGGETTO    \n"
        + "         AND P.ID_PROCEDIMENTO           = PO.ID_PROCEDIMENTO          \n"
        + "         AND P.ID_BANDO                  = BO.ID_BANDO                 \n"
        + "         AND PO.ID_LEGAME_GRUPPO_OGGETTO = BO.ID_LEGAME_GRUPPO_OGGETTO \n"
        + "         AND BOQ.ID_BANDO_OGGETTO        = BO.ID_BANDO_OGGETTO         \n"
        + "         AND BOQ.ID_QUADRO_OGGETTO       = QO.ID_QUADRO_OGGETTO        \n"
        + "         AND QOA.ID_QUADRO_OGGETTO       = QO.ID_QUADRO_OGGETTO        \n"
        + "         AND QOA.ID_AZIONE               = A.ID_AZIONE                 \n"
        + "         AND EC.ID_ELENCO_CDU            = QOA.ID_ELENCO_CDU           \n"
        + "         AND Q.ID_QUADRO                 = QO.ID_QUADRO                \n"
        + "       ORDER BY                                                        \n"
        + "         QO.ORDINE,                                                    \n"
        + "         QO.ID_QUADRO_OGGETTO,                                         \n"
        + "         QOA.ORDINE,                                                   \n"
        + "         QOA.PRIORITA ASC                                              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<QuadroOggettoDTO>>()
          {

            @Override
            public List<QuadroOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<QuadroOggettoDTO> list = new ArrayList<QuadroOggettoDTO>();
              Long lastKey = null;
              List<AzioneDTO> azioni = null;
              while (rs.next())
              {
                long idQuadroOggetto = rs.getLong("ID_QUADRO_OGGETTO");
                if (lastKey == null || lastKey != idQuadroOggetto)
                {
                  QuadroOggettoDTO quadroOggettoDTO = null;
                  quadroOggettoDTO = new QuadroOggettoDTO();
                  quadroOggettoDTO.setIdQuadro(rs.getLong("ID_QUADRO"));
                  quadroOggettoDTO.setCodQuadro(rs.getString("COD_QUADRO"));
                  quadroOggettoDTO.setDescQuadro(rs.getString("DESC_QUADRO"));
                  quadroOggettoDTO.setIdOggetto(rs.getLong("ID_OGGETTO"));
                  quadroOggettoDTO
                      .setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                  quadroOggettoDTO
                      .setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
                  quadroOggettoDTO.setOrdine(rs.getInt("QO_ORDINE"));
                  quadroOggettoDTO.setIdQuadroOggetto(idQuadroOggetto);
                  quadroOggettoDTO.setAzioni(new ArrayList<AzioneDTO>());
                  azioni = new ArrayList<AzioneDTO>();
                  quadroOggettoDTO.setAzioni(azioni);
                  list.add(quadroOggettoDTO);
                  lastKey = idQuadroOggetto;
                }
                AzioneDTO azioneDTO = new AzioneDTO();
                azioneDTO.setCodiceAzione(rs.getString("CODICE_AZIONE"));
                azioneDTO.setLabelAzione(rs.getString("LABEL_AZIONE"));
                azioneDTO.setCodiceCdu(rs.getString("CODICE_CDU"));
                azioneDTO.setExtCodMacroCdu(rs.getString("EXT_COD_MACRO_CDU"));
                azioneDTO.setTipoAzione(rs.getString("TIPO_AZIONE"));
                azioneDTO.setPriorita(getIntegerNull(rs, "PRIORITA"));
                azioni.add(azioneDTO);
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public String getHelpCdu(String codcdu, Long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getHelpCdu]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                            \n"
        + "    A.HELP_CDU AS A_HELP_CDU,                      \n"
        + "    (SELECT                                        \n"
        + "       B.HELP_CDU                                  \n"
        + "     FROM                                          \n"
        + "       IUF_R_QUADRO_OGGETTO_AZIONE B               \n"
        + "     WHERE                                         \n"
        + "      B.ID_ELENCO_CDU = A.ID_ELENCO_CDU            \n"
        + "      AND B.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO \n"
        + "      AND B.HELP_CDU IS NOT NULL                   \n"
        + "     ) AS B_HELP_CDU                               \n"
        + "  FROM                                             \n"
        + "    IUF_D_ELENCO_CDU A                             \n"
        + "  WHERE                                            \n"
        + "    A.CODICE_CDU = :CODICE_CDU                     \n"
        + "                                                   \n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("CODICE_CDU", codcdu, Types.VARCHAR);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO",
          IuffiUtils.NUMBERS.nvl(idQuadroOggetto, 0), Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                String helpCdu = rs.getString("A_HELP_CDU");
                String helpQuadroAzione = rs.getString("B_HELP_CDU");
                if (GenericValidator.isBlankOrNull(helpQuadroAzione))
                {
                  return helpCdu;
                }
                else
                {
                  return helpQuadroAzione;
                }
              }
              return "";
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
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

  public List<String> getValoriParametroControllo(String codiceControllo,
      long idBandoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getValoriParametroControllo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                     \n"
        + "   VALORE                                      \n"
        + " FROM                                          \n"
        + "   IUF_T_VALORI_PARAMETRI VP,                  \n"
        + "   IUF_D_CONTROLLO C                           \n"
        + " WHERE                                         \n"
        + "   VP.ID_CONTROLLO         = C.ID_CONTROLLO    \n"
        + "   AND VP.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n"
        + "   AND C.CODICE            = :CODICE           \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
        Types.NUMERIC);
    mapParameterSource.addValue("CODICE", codiceControllo, Types.VARCHAR);
    try
    {
      return namedParameterJdbcTemplate.queryForList(QUERY, mapParameterSource,
          String.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("codiceControllo", codiceControllo),
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

  public List<ComuneDTO> getDecodificheComuniWidthProvByComune(
      String denominazioneComune, String flagEstinto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getDecodificheComuniWidthProvByComune]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder queryBuilder = new StringBuilder(" SELECT       	\n"
        + "   DG.ISTAT_COMUNE,                						\n"
        + "   DG.DESCRIZIONE_COMUNE, 								\n"
        + "   DG.SIGLA_PROVINCIA,             						\n"
        + "   DG.CAP             									\n"
        + " FROM                                    				\n"
        + "   SMRGAA_V_DATI_AMMINISTRATIVI DG       				\n"
        + "WHERE DG.DESCRIZIONE_COMUNE LIKE :DESCRIZIONE_COMUNE  	\n");

    if (!GenericValidator.isBlankOrNull(flagEstinto))
    {
      queryBuilder.append(" AND  DG.FLAG_ESTINTO = :FLAG_ESTINTO \n");
    }
    queryBuilder.append(" ORDER BY DG.DESCRIZIONE_COMUNE ASC");
    final String QUERY = queryBuilder.toString();
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource
        .addValue(
            "DESCRIZIONE_COMUNE", "%" + IuffiUtils.STRING
                .nvl(denominazioneComune).trim().toUpperCase() + "%",
            Types.VARCHAR);
    mapSqlParameterSource.addValue("FLAG_ESTINTO", flagEstinto, Types.VARCHAR);
    try
    {
      return queryForList(QUERY, mapSqlParameterSource, ComuneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("denominazioneComune", denominazioneComune), new LogParameter("flagEstinto", flagEstinto)
          },
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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



  public Integer getLasDichiarazioneConsistenza(long idAzienda, long idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getLasDichiarazioneConsistenza]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder queryBuff = new StringBuilder(
        " SELECT PCK_IUF_UTILITY.ReturnDichConsistenza(:ID_AZIENDA, NULL, :ID_PROCEDIMENTO_AGRICOLO) AS ID FROM DUAL \n");

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_AZIENDA", idAzienda, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(queryBuff.toString(),
          mapParameterSource, new ResultSetExtractor<Integer>()
          {
            @Override
            public Integer extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getInt("ID");
              }
              return null;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, queryBuff.toString(), (MapSqlParameterSource) null);
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

  public int getIdProcedimentoAgricoloByIdProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
  final String  QUERY =    " SELECT                                                  \n"
      + "    P.ID_PROCEDIMENTO_AGRICOLO                           \n"
      + "  FROM                                                   \n"
      + "    IUF_T_PROCEDIMENTO P,                              \n"
      + "    IUF_T_PROCEDIMENTO_OGGETTO PO                      \n"
      + "  WHERE                                                  \n"
      + "    PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO \n"
      + "    AND PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO           \n" 
      
      ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.queryForInt(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
          }, new LogVariable[]
          {}, QUERY, mapParameterSource);
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
  
  public HashMap<Long, List<Long>> getAziendeBandiProfessionista(UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo)
	  throws InternalUnexpectedException {
      String THIS_METHOD = "[" + THIS_CLASS + "::getAziendeBandiProfessionista]";
      if (logger.isDebugEnabled()) {
	  logger.debug(THIS_METHOD + " BEGIN.");
      }

      String QUERY = " SELECT                                                              \n"
	      + "  S.ID_AZIENDA, EXT_BANDO_TIPO_PRATICA ID_BANDO							\n"
	      + " FROM                                                                      \n"
	      + "  SMRGAA.SMRGAA_V_PROFESSIONISTI S,									    \n"
	      + "  PAPUA.PAPUA_V_RUOLI_ANAG VRA 		   								    \n"
	      + " WHERE                                                                     \n"
	      + "  VRA.ID_PROCEDIMENTO = S.ID_PROCEDIMENTO									\n"
	      + "  AND S.ID_RUOLO = VRA.EXT_ID_RUOLO_ANAG									\n"
	      + "  AND S.CODICE_FISCALE = :CODICE_FISCALE                       			\n"
	      + "  AND S.ID_PROCEDIMENTO=:EXT_ID_PROCEDIMENTO				   	    		\n"
	      + "  AND VRA.CODICE_RUOLO_PAPUA = :CODICE_RUOLO_PAPUA							\n"
	      + "  AND SYSDATE BETWEEN S.DATA_INIZIO_VALIDITA_PROF_AZ AND NVL(DATA_FINE_VALIDITA_PROF_AZ,SYSDATE) 					\n"
	      + " ORDER BY S.ID_AZIENDA, EXT_BANDO_TIPO_PRATICA								\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      mapParameterSource.addValue("CODICE_FISCALE", utenteAbilitazioni.getCodiceFiscale(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_PROCEDIMENTO", idProcedimentoAgricolo, Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RUOLO_PAPUA", utenteAbilitazioni.getRuolo().getCodice(), Types.VARCHAR);

      try {
	  return namedParameterJdbcTemplate.query(QUERY.toString(), mapParameterSource,
		  new ResultSetExtractor<HashMap<Long, List<Long>>>() {

	      @Override
	      public HashMap<Long, List<Long>> extractData(ResultSet rs)
		      throws SQLException, DataAccessException {
		  HashMap<Long, List<Long>> map = new HashMap<>();
		  List<Long> bandi = new ArrayList<>();
		  Long key = -1l;
		  Long keyOld = -2l;
		  while (rs.next()) {
		      key = rs.getLong("ID_AZIENDA");
		      if (key.longValue() != keyOld.longValue()) {
			  keyOld = key;

			  bandi = new ArrayList<>();
			  map.put(key, bandi);
		      }
		      bandi.add(rs.getLong("ID_BANDO"));
		  }
		  return map;
	      }
	  });
      } catch (Throwable t) {
	  InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {},
		  new LogVariable[] {}, QUERY, mapParameterSource);
	  logInternalUnexpectedException(e, THIS_METHOD);
	  throw e;
      } finally {
	  if (logger.isDebugEnabled()) {
	      logger.debug(THIS_METHOD + " END.");
	  }
      }
  }
  
}
