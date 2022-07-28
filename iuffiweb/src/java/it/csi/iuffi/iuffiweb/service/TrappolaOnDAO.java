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
import it.csi.iuffi.iuffiweb.model.TrappolaOnDTO;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class TrappolaOnDAO extends BaseDAO
{
  
  
  private static final String THIS_CLASS = TrappolaOnDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_trappola_on(id_trappola_on, id_trappola, id_on, data_inizio_validita, "+
                                       " data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) " + 
                                       "VALUES(SEQ_IUF_D_TRAPPOLA_ON.nextval, :idTrappola, :idOn,"+
                                       "sysdate, null, :ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_d_trappola_on SET id_trappola = :idTrappola, id_on = :idOn, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
                                        "WHERE id_trappola_on = :idTrappolaOn";

  private static final String SELECT_ALL = "SELECT t.id_trappola_on,t.id_trappola," + 
                                           "t.id_on,t.data_inizio_validita,t.data_fine_validita," + 
                                           "t.ext_id_utente_aggiornamento,t.data_ultimo_aggiornamento," + 
                                           "b.tipologia_trappola as descr_tipo_trappola,a.nome_latino as descr_on, " + 
                                           "CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato "+                                           
                                           "FROM iuf_d_trappola_on t,iuf_d_organismo_nocivo a, iuf_d_tipo_trappola b " + 
                                           "WHERE t.id_trappola=b.id_tipo_trappola " + 
                                           "AND t.id_on=a.id_organismo_nocivo " + 
                                           " ORDER BY UPPER(b.tipologia_trappola),UPPER(a.nome_latino),t.data_inizio_validita desc";

  private static final String SELECT_BY_ID = "SELECT t.id_trappola_on,t.id_trappola," + 
                                          "t.id_on,t.data_inizio_validita,t.data_fine_validita," + 
                                          "t.ext_id_utente_aggiornamento,t.data_ultimo_aggiornamento," + 
                                          "b.tipologia_trappola as descr_tipo_trappola,a.nome_latino as descr_on " + 
                                          "FROM iuf_d_trappola_on t,iuf_d_organismo_nocivo a, iuf_d_tipo_trappola b " + 
                                          "WHERE t. id_trappola_on is not null " + 
                                          "and t.id_trappola=b.id_tipo_trappola " + 
                                          "and t.id_on=a.id_organismo_nocivo " + 
                                          "and id_trappola_on = :idTrappolaOn";
    
  private static final String DELETE = "DELETE FROM iuf_d_trappola_on WHERE id_trappola_on = :idTrappolaOn";

  private static final String SELECT_VALIDI = "select * from iuf_d_trappola_on t where T.data_fine_validita is null";

  private static final String SELECT_BY_ON = "select * from iuf_d_trappola_on t where t.id_on=:idOn and t.data_fine_validita is null";

  private static final String SELECT_BY_TRAPPOLA = "select * from iuf_d_trappola_on t where t.id_trappola=:idTrappola and t.data_fine_validita is null";

  public TrappolaOnDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public TrappolaOnDTO insert(TrappolaOnDTO obj) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insert]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {

      mapParameterSource.addValue("idTrappola", obj.getIdTrappola(), Types.VARCHAR);
      mapParameterSource.addValue("idOn", obj.getIdOn(), Types.VARCHAR);
      mapParameterSource.addValue("dataInizioVal", obj.getDataInizioValidita(), Types.DATE);
      mapParameterSource.addValue("dataFineVal", obj.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", obj.getExtIdUtenteAggiornamento());
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_trappola_on"});
      obj.setIdTrappolaOn(holder.getKey().intValue());
      return obj;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
          new LogVariable[]
              {}, INSERT, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw new InternalUnexpectedException(t);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<TrappolaOnDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, TrappolaOnDTO.class);
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

  public List<TrappolaOnDTO> findByFilter(TrappolaOnDTO obj) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    
      String SELECT_BY_FILTER = "SELECT t.id_trappola_on,t.id_trappola," + 
          "t.id_on,t.data_inizio_validita,t.data_fine_validita," + 
          "t.ext_id_utente_aggiornamento,t.data_ultimo_aggiornamento," + 
          "b.tipologia_trappola as descr_tipo_trappola,a.nome_latino as descr_on  " + 
          "FROM iuf_d_trappola_on t,iuf_d_organismo_nocivo a, iuf_d_tipo_trappola b  " + 
          "WHERE t. id_trappola_on is not null " + 
          "and t.id_trappola=b.id_tipo_trappola " + 
          "and t.id_on=a.id_organismo_nocivo "; 

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
    mapParameterSource.addValue("dataInizioVal", (obj.getDataInizioValidita()!=null)?sdf.format(obj.getDataInizioValidita()):"");
    mapParameterSource.addValue("dataFineVal", (obj.getDataFineValidita()!=null)?sdf.format(obj.getDataFineValidita()):"");
    try
    {
      List<TrappolaOnDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, TrappolaOnDTO.class);
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

  
  public void updateDataFineValidita(Integer idTrappolaOn) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE iuf_d_trappola_on SET data_fine_validita = SYSDATE," +
    "data_ultimo_aggiornamento = SYSDATE " +
    "WHERE id_trappola_on = :idTrappolaOn";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappolaOn", idTrappolaOn);
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

  public TrappolaOnDTO findById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappolaOn", id);
    
    try
    {
      TrappolaOnDTO trappolaOn = queryForObject(SELECT_BY_ID, mapParameterSource, TrappolaOnDTO.class);
      return trappolaOn;
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

  
  public List<TrappolaOnDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<TrappolaOnDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, TrappolaOnDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_VALIDI, mapParameterSource);
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

  public void remove(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappolaOn", id);
    
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
      throw new InternalUnexpectedException(t);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
  
  public void update(TrappolaOnDTO obj) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappola", obj.getIdTrappola());
    mapParameterSource.addValue("idOn", obj.getIdOn());
//    mapParameterSource.addValue("dataInizioVal", obj.getDataInizioValidita(), Types.DATE);
//    mapParameterSource.addValue("dataFineVal", obj.getDataFineValidita(), Types.DATE);
    mapParameterSource.addValue("ext_id_utente_aggiornamento", obj.getExtIdUtenteAggiornamento());
    mapParameterSource.addValue("idTrappolaOn", obj.getIdTrappolaOn());
    
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

  
  
  /**
   * AGGIUNTO IL 17/02 PER TRACCIARE LE RELAZIONI CHE PRESENTANO UNA TRAPPOLA DA STORICIZZARE
   * @param id
   * @return
   * @throws InternalUnexpectedException
   */
  public List<TrappolaOnDTO> findByIdTrappola(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdTrappola";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappola", id);
    try
    {
      List<TrappolaOnDTO> list = queryForList(SELECT_BY_TRAPPOLA, mapParameterSource, TrappolaOnDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_TRAPPOLA, mapParameterSource);
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
 
  
  /**
   * AGGIUNTO IL 17/02 PER TRACCIARE LE RELAZIONI CHE PRESENTANO UN ENTE DA STORICIZZARE
   * @param id
   * @return
   * @throws InternalUnexpectedException
   */
  public List<TrappolaOnDTO> findByIdOn(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdOn";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idOn", id);
    try
    {
      List<TrappolaOnDTO> list = queryForList(SELECT_BY_ON, mapParameterSource, TrappolaOnDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_ON, mapParameterSource);
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
