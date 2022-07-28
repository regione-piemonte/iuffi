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
import it.csi.iuffi.iuffiweb.model.ExtraFieldCampDTO;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class ExtraFieldCampDAO extends BaseDAO
{

  private static final String THIS_CLASS = ExtraFieldCampDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_extra_field_camp(id_extra_field_camp, label, value, data_inizio_validita, data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) \r\n" + 
                                       "VALUES(seq_iuf_d_extra_field_camp.nextval, :label, :value, :dataInizioVal, :dataFineVal, :ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_d_extra_field_camp SET label = :label, value = :value, \r\n" +
                                            "data_inizio_validita = :dataInizioVal, data_fine_validita = :dataFineVal,\r\n" +
                                            "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE \r\n" +
                                        "WHERE id_extra_field_camp = :idExtraFieldCamp";

  private static final String SELECT_ALL = "SELECT * FROM iuf_d_extra_field_camp ORDER BY label";
  
  private static final String SELECT_BY_FILTER = "SELECT * FROM iuf_d_extra_field_camp \r\n" +
                                                  "WHERE label LIKE :label \r\n" +
                                                  " AND value LIKE :value \r\n" +
                                                  " AND NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) <= NVL(TO_DATE(:dataInizioVal,'DD/MM/YYYY'),NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY'))) \r\n" +
                                                  " AND NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY')) >= NVL(TO_DATE(:dataFineVal,'DD/MM/YYYY'),NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY'))) \r\n" +
                                                  "ORDER BY label";
  
  private static final String SELECT_BY_ID = "SELECT * FROM iuf_d_extra_field_camp WHERE id_extra_field_camp = :idExtraFieldCamp";

  private static final String DELETE = "DELETE FROM iuf_d_extra_field_camp WHERE id_extra_field_camp = :idExtraFieldCamp";

  public ExtraFieldCampDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public ExtraFieldCampDTO insert(ExtraFieldCampDTO extraField) throws InternalUnexpectedException
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
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_extra_field_camp"});
      extraField.setIdExtraFieldCamp(holder.getKey().intValue());
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

  public List<ExtraFieldCampDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, ExtraFieldCampDTO.class);
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

  public List<ExtraFieldCampDTO> findByFilter(ExtraFieldCampDTO extraField) throws InternalUnexpectedException
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
      List<ExtraFieldCampDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, ExtraFieldCampDTO.class);
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

  public ExtraFieldCampDTO findById(Integer idExtraFieldCamp) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idExtraFieldCamp", idExtraFieldCamp);
    
    try
    {
      ExtraFieldCampDTO extraField = queryForObject(SELECT_BY_ID, mapParameterSource, ExtraFieldCampDTO.class);
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

  public void remove(Integer idExtraFieldCamp) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idExtraFieldCamp", idExtraFieldCamp);
    
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
  
  public void update(ExtraFieldCampDTO extraField) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idExtraFieldCamp", extraField.getIdExtraFieldCamp());
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
