package it.csi.iuffi.iuffiweb.service;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;

public class UtilServiceDAO extends BaseDAO
{


  private static final String THIS_CLASS = UtilServiceDAO.class.getSimpleName();
 
  private static final String SELECT_MAX_DATA_MODIFICA = "SELECT MAX(data_ultimo_aggiornamento) as last_change FROM (\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_d_anfi\r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_r_specie_on_periodo \r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_d_check_list_item\r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_d_trappola_on \r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_d_organismo_nocivo\r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_d_specie_vegetale\r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_d_tipo_area\r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_d_tipo_campione\r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_d_tipo_trappola\r\n" + 
      "UNION\r\n" + 
      "SELECT data_ultimo_aggiornamento FROM iuf_t_anagrafica\r\n" + 
      ")";

  public UtilServiceDAO() {  }


  public String getLastChange() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getLastChange";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    try
    {
      String result = queryForString(SELECT_MAX_DATA_MODIFICA, mapParameterSource, "last_change");
      return result;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_MAX_DATA_MODIFICA, mapParameterSource);
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

  public String getTableByConstraint(String constraintName) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getTableByConstraint";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    final String SELECT = "SELECT a.table_name FROM all_constraints a\r\n" +
                          "WHERE LOWER(a.CONSTRAINT_NAME) = LOWER(:constraintName)";
    try
    {
      mapParameterSource.addValue("constraintName", constraintName);
      logger.debug(SELECT);
      String result = queryForString(SELECT, mapParameterSource, "table_name");
      return result;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
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
  
  public String getTableCommentsByTableName(String tableName) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getTableByConstraint";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    final String SELECT = "SELECT atc.COMMENTS FROM all_tab_comments atc WHERE LOWER(atc.TABLE_NAME) = LOWER(:tableName)";
    try
    {
      mapParameterSource.addValue("tableName", tableName);
      logger.debug(SELECT);
      String result = queryForString(SELECT, mapParameterSource, "comments");
      return result;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
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
