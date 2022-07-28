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
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class TipoTrappolaDAO extends BaseDAO
{
  
  
  private static final String THIS_CLASS = TipoTrappolaDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_tipo_trappola(id_tipo_trappola, tipologia_trappola, sfr_code, costo, data_inizio_validita, data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) \r\n" + 
                                        "VALUES(seq_iuf_d_tipo_trappola.nextval, UPPER(:tipologiaTrappola),'-', 0, sysdate, null, \r\n" + 
                                        ":ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_d_tipo_trappola SET tipologia_trappola = :tipologiaTrappola, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE \r\n" +
                                        "WHERE id_tipo_trappola = :idTipoTrappola";

  private static final String SELECT_ALL = "SELECT t.*,CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato "+
                                            "FROM iuf_d_tipo_trappola t ORDER BY UPPER(t.tipologia_trappola),t.data_inizio_validita desc";
  
  private static final String SELECT_BY_FILTER = "SELECT * FROM iuf_d_tipo_trappola \r\n" +
                                                  "WHERE tipologia_trappola LIKE :tipologiaTrappola \r\n" +
                                                  " %s \r\n" +
                                                  " AND NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) >= NVL(TO_DATE(:dataInizioVal,'DD/MM/YYYY'),NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY'))) \r\n" +
                                                  //" AND NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY')) <= NVL(:dataFineVal,NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY'))) \r\n" +
                                                  "ORDER BY tipologia_trappola";
  
  private static final String SELECT_BY_ID = "SELECT * FROM iuf_d_tipo_trappola WHERE id_tipo_trappola = :idTipoTrappola";

  private static final String DELETE = "DELETE FROM iuf_d_tipo_trappola WHERE id_tipo_trappola = :idTipoTrappola";

  private static final String SELECT_VALIDI = "select * from iuf_d_tipo_trappola t where T.data_fine_validita is null ORDER BY UPPER(tipologia_trappola)";

  private static final String SELECT_ALL_ID_MULTIPLI = "select * from iuf_d_tipo_trappola t where id_tipo_trappola in(%s)";

  public TipoTrappolaDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public TipoTrappolaDTO insertTipoTrappola(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertTipoTrappola]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {

      mapParameterSource.addValue("tipologiaTrappola", tipoTrappola.getTipologiaTrappola(), Types.VARCHAR);
      //mapParameterSource.addValue("sfrCode", tipoTrappola.getSfrCode(), Types.VARCHAR);
      //mapParameterSource.addValue("costo", tipoTrappola.getCosto(), Types.DOUBLE);
      mapParameterSource.addValue("dataInizioVal", tipoTrappola.getDataInizioValidita(), Types.DATE);
      mapParameterSource.addValue("dataFineVal", tipoTrappola.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", tipoTrappola.getExtIdUtenteAggiornamento());
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_tipo_trappola"});
      tipoTrappola.setIdTipoTrappola(holder.getKey().intValue());
      return tipoTrappola;
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

  public List<TipoTrappolaDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, TipoTrappolaDTO.class);
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

  public List<TipoTrappolaDTO> findByFilter(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("tipologiaTrappola", tipoTrappola.getTipologiaTrappola()+"%");
    mapParameterSource.addValue("sfrCode", tipoTrappola.getSfrCode()+"%");
    String select = null;
//    if (tipoTrappola.getCosto() != null) {
//      select = String.format(SELECT_BY_FILTER, "AND costo = :costo ");
//      mapParameterSource.addValue("costo", tipoTrappola.getCosto(), Types.DOUBLE);
//    }
//    else
      select = String.format(SELECT_BY_FILTER, "");
    
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
    mapParameterSource.addValue("dataInizioVal", (tipoTrappola.getDataInizioValidita()!=null)?sdf.format(tipoTrappola.getDataInizioValidita()):"");
    mapParameterSource.addValue("dataFineVal", (tipoTrappola.getDataFineValidita()!=null)?sdf.format(tipoTrappola.getDataFineValidita()):"");
    try
    {
      List<TipoTrappolaDTO> list = queryForList(select, mapParameterSource, TipoTrappolaDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, select, mapParameterSource);
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

  
  public void updateDataFineValidita(Integer idTipoTrappola) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE iuf_d_tipo_trappola SET data_fine_validita = SYSDATE," +
    "data_ultimo_aggiornamento = SYSDATE " +
    "WHERE id_tipo_trappola = :idTipoTrappola";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoTrappola", idTipoTrappola);
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

  
  public TipoTrappolaDTO findById(Integer idTipoTrappola) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoTrappola", idTipoTrappola);
    
    try
    {
      TipoTrappolaDTO tipoTrappola = queryForObject(SELECT_BY_ID, mapParameterSource, TipoTrappolaDTO.class);
      return tipoTrappola;
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

  
  public List<TipoTrappolaDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<TipoTrappolaDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, TipoTrappolaDTO.class);
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

  public void remove(Integer idTipoTrappola) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoTrappola", idTipoTrappola);
    
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
  
  public void update(TipoTrappolaDTO tipoTrappola) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("tipologiaTrappola", tipoTrappola.getTipologiaTrappola(), Types.VARCHAR);
    mapParameterSource.addValue("idTipoTrappola", tipoTrappola.getIdTipoTrappola());
    mapParameterSource.addValue("ext_id_utente_aggiornamento", tipoTrappola.getExtIdUtenteAggiornamento());
//    mapParameterSource.addValue("sfrCode", tipoTrappola.getSfrCode(), Types.VARCHAR);
    //mapParameterSource.addValue("costo", tipoTrappola.getCosto(), Types.DOUBLE);
//    mapParameterSource.addValue("dataInizioVal", tipoTrappola.getDataInizioValidita(), Types.DATE);
//    mapParameterSource.addValue("dataFineVal", tipoTrappola.getDataFineValidita(), Types.DATE);
    
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

  
  public List<TipoTrappolaDTO> findByIdMultipli(String idMultipli) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
  //  mapParameterSource.addValue("idMultipli", idMultipli, Types.INTEGER);
    String select = String.format(SELECT_ALL_ID_MULTIPLI,idMultipli);
    try
    {
      List<TipoTrappolaDTO> list = queryForList(select, mapParameterSource, TipoTrappolaDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL_ID_MULTIPLI, mapParameterSource);
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
