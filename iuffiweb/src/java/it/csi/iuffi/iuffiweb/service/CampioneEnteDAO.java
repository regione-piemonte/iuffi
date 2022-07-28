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
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.CampioneEnteDTO;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class CampioneEnteDAO extends BaseDAO
{
  
  
  private static final String THIS_CLASS = CampioneEnteDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO IUF_R_TIPO_CAMPIONE_ENTE(id_tipo_campione_ente, id_tipo_campione, id_ente, costo, data_inizio_validita, "+
                                      " data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) " + 
                                      "VALUES(SEQ_IUF_D_TRAPPOLA_ON.nextval, :idTipoCampione, :idEnte, :costo,"+
                                      "sysdate, null, :ext_id_utente_aggiornamento, SYSDATE)";

private static final String UPDATE = "UPDATE IUF_R_TIPO_CAMPIONE_ENTE SET data_fine_validita = sysdate, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
                                     "WHERE id_tipo_campione_ente = :idTipoCampioneEnte";

private static final String SELECT_ALL = "SELECT t.id_tipo_campione_ente,t.id_tipo_campione," + 
                                        " t.id_ente,t.costo,t.data_inizio_validita,t.data_fine_validita," + 
                                        "  t.ext_id_utente_aggiornamento,t.data_ultimo_aggiornamento," + 
                                        "  b.tipologia_campione as descr_tipo_campione,a.denominazione as descr_ente," + 
                                        "  CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato " + 
                                        "  FROM IUF_R_TIPO_CAMPIONE_ENTE t,iuf_d_ente a, iuf_d_tipo_campione b  " + 
                                        "  WHERE t.id_tipo_campione_ente is not null " + 
                                        "  and t.id_tipo_campione=b.id_tipo_campione " + 
                                        "  and t.id_ente=a.id_ente ORDER BY UPPER(b.tipologia_campione)";


private static final String SELECT_BY_ID = "SELECT * FROM IUF_R_TIPO_CAMPIONE_ENTE WHERE id_tipo_campione_ente = :idTipoCampioneEnte";

private static final String DELETE = "DELETE FROM IUF_R_TIPO_CAMPIONE_ENTE WHERE id_tipo_campione_ente = :idTipoCampioneEnte";

private static final String SELECT_VALIDI = "select * from IUF_R_TIPO_CAMPIONE_ENTE t where T.data_fine_validita is null";

  public CampioneEnteDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public CampioneEnteDTO insert(CampioneEnteDTO obj) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insert]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {

      mapParameterSource.addValue("idTipoCampione", obj.getIdTipoCampione(), Types.VARCHAR);
      mapParameterSource.addValue("idEnte", obj.getIdEnte(), Types.VARCHAR);
      mapParameterSource.addValue("costo", obj.getCosto(), Types.DOUBLE);
     // mapParameterSource.addValue("dataInizioVal", obj.getDataInizioValidita(), Types.DATE);
     // mapParameterSource.addValue("dataFineVal", obj.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", obj.getExtIdUtenteAggiornamento());
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_tipo_campione_ente"});
      obj.setIdTipoCampioneEnte(holder.getKey().intValue());
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

  public List<CampioneEnteDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, CampioneEnteDTO.class);
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

  public List<CampioneEnteDTO> findByFilter(CampioneEnteDTO obj) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_FILTER  = "SELECT t.id_tipo_campione_ente,t.id_tipo_campione," + 
        " t.id_ente,t.costo,t.data_inizio_validita,t.data_fine_validita," + 
        "  t.ext_id_utente_aggiornamento,t.data_ultimo_aggiornamento," + 
        "  b.tipologia_campione as descr_tipo_campione,a.denominazione as descr_ente," + 
        "  CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato " + 
        "  FROM IUF_R_TIPO_CAMPIONE_ENTE t,iuf_d_ente a, iuf_d_tipo_campione b  " + 
        "  WHERE t.id_tipo_campione_ente is not null " + 
        "  and t.id_tipo_campione=b.id_tipo_campione " + 
        "  and t.id_ente=a.id_ente";

     
    if(obj.getIdTipoCampione()!=null && obj.getIdTipoCampione()>0)
      SELECT_BY_FILTER += "  and t.id_tipo_campione =:idTipoCampione";

    if(obj.getIdEnte()!=null && obj.getIdEnte()>0)
      SELECT_BY_FILTER += "  and t.id_ente =:idEnte";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoCampione", obj.getIdTipoCampione(),Types.INTEGER);
    mapParameterSource.addValue("idEnte", obj.getIdEnte(),Types.INTEGER);

    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
    mapParameterSource.addValue("dataInizioVal", (obj.getDataInizioValidita()!=null)?sdf.format(obj.getDataInizioValidita()):"");
    mapParameterSource.addValue("dataFineVal", (obj.getDataFineValidita()!=null)?sdf.format(obj.getDataFineValidita()):"");
    try
    {
      List<CampioneEnteDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, CampioneEnteDTO.class);
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

  public CampioneEnteDTO findById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoCampioneEnte", id);
    
    try
    {
      CampioneEnteDTO trappolaEnte = queryForObject(SELECT_BY_ID, mapParameterSource, CampioneEnteDTO.class);
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

  public void remove(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoCampioneEnte", id);
    
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

  public void updateDataFineValidita(Integer idCampioneEnte) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE IUF_R_TIPO_CAMPIONE_ENTE SET data_fine_validita = SYSDATE," +
    "data_ultimo_aggiornamento = SYSDATE " +
    "WHERE id_tipo_campione_ente = :idCampioneEnte";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idCampioneEnte", idCampioneEnte);
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

  
  public List<CampioneEnteDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<CampioneEnteDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, CampioneEnteDTO.class);
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

  public void update(CampioneEnteDTO obj) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoCampione", obj.getIdTipoCampione(), Types.VARCHAR);
    mapParameterSource.addValue("idEnte", obj.getIdEnte(), Types.VARCHAR);
    mapParameterSource.addValue("dataInizioVal", obj.getDataInizioValidita(), Types.DATE);
    mapParameterSource.addValue("dataFineVal", obj.getDataFineValidita(), Types.DATE);
    mapParameterSource.addValue("ext_id_utente_aggiornamento", obj.getExtIdUtenteAggiornamento());
    mapParameterSource.addValue("idTipoCampioneEnte", obj.getIdTipoCampioneEnte());
    
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
