package it.csi.iuffi.iuffiweb.service;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.ExtraFieldTrapDTO;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class ExtraFieldTrapDAO extends BaseDAO
{

  private static final String THIS_CLASS = ExtraFieldTrapDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_extra_field_trap(id_extra_field_trap, label, value, data_inizio_validita, data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) \r\n" + 
                                       "VALUES(seq_iuf_d_extra_field_trap.nextval, :label, :value, :dataInizioVal, :dataFineVal, :ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_d_extra_field_trap SET label = :label, value = :value, \r\n" +
                                            "data_inizio_validita = :dataInizioVal, data_fine_validita = :dataFineVal,\r\n" +
                                            "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE \r\n" +
                                        "WHERE id_extra_field_trap = :idExtraFieldTrap";

  private static final String SELECT_ALL = "SELECT * FROM iuf_d_extra_field_trap ORDER BY label";
  
  private static final String SELECT_BY_FILTER = "SELECT * FROM iuf_d_extra_field_trap \r\n" +
                                                  "WHERE label LIKE :label \r\n" +
                                                  " AND value LIKE :value \r\n" +
                                                  " AND NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) <= NVL(TO_DATE(:dataInizioVal,'DD/MM/YYYY'),NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY'))) \r\n" +
                                                  " AND NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY')) >= NVL(TO_DATE(:dataFineVal,'DD/MM/YYYY'),NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY'))) \r\n" +
                                                  "ORDER BY label";
  
  private static final String SELECT_BY_ID = "SELECT * FROM iuf_d_extra_field_trap WHERE id_extra_field_trap = :idExtraFieldTrap";

  private static final String DELETE = "DELETE FROM iuf_d_extra_field_trap WHERE id_extra_field_trap = :idExtraFieldTrap";

  public ExtraFieldTrapDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public ExtraFieldTrapDTO insert(ExtraFieldTrapDTO extraField) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insert]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {

      mapParameterSource.addValue("label", extraField.getLabel(), Types.VARCHAR);
      mapParameterSource.addValue("value", extraField.getValue(), Types.VARCHAR);
      mapParameterSource.addValue("dataInizioVal", extraField.getDataInizioValidita(), Types.DATE);
      mapParameterSource.addValue("dataFineVal", extraField.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", extraField.getExtIdUtenteAggiornamento());
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_extra_field_trap"});
      extraField.setIdExtraFieldTrap(holder.getKey().intValue());
      return extraField;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
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
  }

  public List<ExtraFieldTrapDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, ExtraFieldTrapDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL, mapParameterSource);
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

  public List<ExtraFieldTrapDTO> findByFilter(ExtraFieldTrapDTO extraField) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("label", extraField.getLabel()+"%");
    mapParameterSource.addValue("value", extraField.getValue()+"%", Types.VARCHAR);
    
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
    mapParameterSource.addValue("dataInizioVal", (extraField.getDataInizioValidita()!=null)?sdf.format(extraField.getDataInizioValidita()):"");
    mapParameterSource.addValue("dataFineVal", (extraField.getDataFineValidita()!=null)?sdf.format(extraField.getDataFineValidita()):"");
    try
    {
      List<ExtraFieldTrapDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, ExtraFieldTrapDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_FILTER, mapParameterSource);
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

  public ExtraFieldTrapDTO findById(Integer idExtraFieldTrap) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idExtraFieldTrap", idExtraFieldTrap);
    
    try
    {
      ExtraFieldTrapDTO extraField = queryForObject(SELECT_BY_ID, mapParameterSource, ExtraFieldTrapDTO.class);
      return extraField;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_ID, mapParameterSource);
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

  public void remove(Integer idExtraFieldTrap) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idExtraFieldTrap", idExtraFieldTrap);
    
    try
    {
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, DELETE, mapParameterSource);
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
  
  public void update(ExtraFieldTrapDTO extraField) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idExtraFieldTrap", extraField.getIdExtraFieldTrap());
    mapParameterSource.addValue("label", extraField.getLabel(), Types.VARCHAR);
    mapParameterSource.addValue("value", extraField.getValue(), Types.VARCHAR);
    mapParameterSource.addValue("dataInizioVal", extraField.getDataInizioValidita(), Types.DATE);
    mapParameterSource.addValue("dataFineVal", extraField.getDataFineValidita(), Types.DATE);
    mapParameterSource.addValue("ext_id_utente_aggiornamento", extraField.getExtIdUtenteAggiornamento());
    
    try
    {
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, UPDATE, mapParameterSource);
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

