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
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaEnteDTO;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class TrappolaEnteDAO extends BaseDAO
{
  
  
  private static final String THIS_CLASS = TrappolaEnteDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_trappola_ente(id_trappola_ente, id_trappola, id_ente, costo, data_inizio_validita, "+
                                      " data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) " + 
                                      "VALUES(SEQ_IUF_D_TRAPPOLA_ON.nextval, :idTrappola, :idEnte, :costo,"+
                                      "sysdate, null, :ext_id_utente_aggiornamento, SYSDATE)";

private static final String UPDATE = "UPDATE iuf_d_trappola_ente SET data_fine_validita = sysdate, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
                                     "WHERE id_trappola_ente = :idTrappolaEnte";

private static final String SELECT_ALL = "SELECT t.id_trappola_ente,t.id_trappola," + 
                                        "t.id_ente,t.costo,t.data_inizio_validita,t.data_fine_validita," + 
                                        "t.ext_id_utente_aggiornamento,t.data_ultimo_aggiornamento," + 
                                        "b.tipologia_trappola as descr_tipo_trappola,a.denominazione as descr_ente, " + 
                                        "CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato "+
                                        "FROM iuf_d_trappola_ente t,iuf_d_ente a, iuf_d_tipo_trappola b " + 
                                        "WHERE t. id_trappola_ente is not null " + 
                                        "and t.id_trappola=b.id_tipo_trappola " + 
                                        "and t.id_ente=a.id_ente ORDER BY UPPER(b.tipologia_trappola)";

private static final String SELECT_VALIDI = "select * from iuf_d_trappola_ente t where T.data_fine_validita is null";

private static final String SELECT_BY_ENTE = "select * from iuf_d_trappola_ente t where t.id_ente=:idEnte";

private static final String SELECT_BY_TRAPPOLA = "select * from iuf_d_trappola_ente t where t.id_trappola=:idTrappola";

private static final String SELECT_BY_ID = "SELECT * FROM iuf_d_trappola_ente WHERE id_trappola_ente = :idTrappolaEnte";

private static final String DELETE = "DELETE FROM iuf_d_trappola_ente WHERE id_trappola_ente = :idTrappolaEnte";

  public TrappolaEnteDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public TrappolaEnteDTO insert(TrappolaEnteDTO obj) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insert]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {

      mapParameterSource.addValue("idTrappola", obj.getIdTrappola(), Types.VARCHAR);
      mapParameterSource.addValue("idEnte", obj.getIdEnte(), Types.VARCHAR);
      mapParameterSource.addValue("costo", obj.getCosto(), Types.DOUBLE);
      mapParameterSource.addValue("dataInizioVal", obj.getDataInizioValidita(), Types.DATE);
      mapParameterSource.addValue("dataFineVal", obj.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", obj.getExtIdUtenteAggiornamento());
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_trappola_ente"});
      obj.setIdTrappolaEnte(holder.getKey().intValue());
      return obj;
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

  public List<TrappolaEnteDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, TrappolaEnteDTO.class);
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

  public List<TrappolaEnteDTO> findByFilter(TrappolaEnteDTO obj) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_FILTER = "SELECT t.id_trappola_ente,t.id_trappola,"+
        "t.id_ente,t.costo,t.data_inizio_validita,t.data_fine_validita," + 
        "t.ext_id_utente_aggiornamento,t.data_ultimo_aggiornamento,"+
        "b.tipologia_trappola as descr_tipo_trappola,a.denominazione as descr_ente " + 
        "FROM iuf_d_trappola_ente t,iuf_d_ente a, iuf_d_tipo_trappola b " + 
        "WHERE t. id_trappola_ente is not null " + 
        "and t.id_trappola=b.id_tipo_trappola " + 
        "and t.id_ente=a.id_ente ";
     
    if(obj.getIdTrappola()!=null && obj.getIdTrappola()>0)
      SELECT_BY_FILTER += "  and t.id_trappola =:idTrappola";

    if(obj.getIdEnte()!=null && obj.getIdEnte()>0)
      SELECT_BY_FILTER += "  and t.id_ente =:idEnte";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappola", obj.getIdTrappola(),Types.INTEGER);
    mapParameterSource.addValue("idEnte", obj.getIdEnte(),Types.INTEGER);

    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
    mapParameterSource.addValue("dataInizioVal", (obj.getDataInizioValidita()!=null)?sdf.format(obj.getDataInizioValidita()):"");
    mapParameterSource.addValue("dataFineVal", (obj.getDataFineValidita()!=null)?sdf.format(obj.getDataFineValidita()):"");
    try
    {
      List<TrappolaEnteDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, TrappolaEnteDTO.class);
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

  
  public void updateDataFineValidita(Integer idTrappolaEnte) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE iuf_d_trappola_ente SET data_fine_validita = SYSDATE," +
    "data_ultimo_aggiornamento = SYSDATE " +
    "WHERE id_trappola_ente = :idTrappolaEnte";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappolaEnte", idTrappolaEnte);
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

  
  public TrappolaEnteDTO findById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappolaEnte", id);
    
    try
    {
      TrappolaEnteDTO trappolaEnte = queryForObject(SELECT_BY_ID, mapParameterSource, TrappolaEnteDTO.class);
      return trappolaEnte;
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

  
  public List<TrappolaEnteDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<TrappolaEnteDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, TrappolaEnteDTO.class);
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

  
  
  /**
   * AGGIUNTO IL 17/02 PER TRACCIARE LE RELAZIONI CHE PRESENTANO UN ENTE DA STORICIZZARE
   * @param id
   * @return
   * @throws InternalUnexpectedException
   */
  public List<TrappolaEnteDTO> findByIdEnte(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdEnte";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idEnte", id);
    try
    {
      List<TrappolaEnteDTO> list = queryForList(SELECT_BY_ENTE, mapParameterSource, TrappolaEnteDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_ENTE, mapParameterSource);
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
  public List<TrappolaEnteDTO> findByIdTrappola(Integer id) throws InternalUnexpectedException
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
      List<TrappolaEnteDTO> list = queryForList(SELECT_BY_TRAPPOLA, mapParameterSource, TrappolaEnteDTO.class);
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
 
  
  public void remove(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappolaEnte", id);
    
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
  
  public void update(TrappolaEnteDTO obj) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTrappola", obj.getIdTrappola(), Types.VARCHAR);
    mapParameterSource.addValue("idEnte", obj.getIdEnte(), Types.VARCHAR);
    mapParameterSource.addValue("dataInizioVal", obj.getDataInizioValidita(), Types.DATE);
    mapParameterSource.addValue("dataFineVal", obj.getDataFineValidita(), Types.DATE);
    mapParameterSource.addValue("ext_id_utente_aggiornamento", obj.getExtIdUtenteAggiornamento());
    mapParameterSource.addValue("idTrappolaEnte", obj.getIdTrappolaEnte());
    
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
