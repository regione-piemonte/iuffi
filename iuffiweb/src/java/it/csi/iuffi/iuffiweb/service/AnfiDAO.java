package it.csi.iuffi.iuffiweb.service;

import java.sql.Types;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.AnfiDTO;
import it.csi.iuffi.iuffiweb.service.GestioneVerbaleDAO.BlobRowMapper;


public class AnfiDAO extends BaseDAO
{


  private static final String THIS_CLASS = AnfiDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_anfi(id_anfi, tipologia_test_di_laboratorio, costo,data_inizio_validita,data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) " + 
                                        "VALUES(seq_iuf_d_anfi.nextval, UPPER(:tipologiaTestDiLaboratorio), :costo, sysdate,null ," +
                                        ":ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_d_anfi SET tipologia_test_di_laboratorio=:tipologiaTestDiLaboratorio, costo = :costo, " +
                                        " ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
                                        "WHERE id_anfi = :idAnfi";

  private static final String SELECT_ALL = "SELECT t.*,CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato FROM iuf_d_anfi t ORDER BY UPPER(tipologia_test_di_laboratorio),t.data_inizio_validita desc";
  
  private static final String SELECT_ALL_ID_MULTIPLI = "select * from iuf_d_anfi t where id_anfi in(%s)";

  private static final String SELECT_BY_FILTER = "SELECT * FROM iuf_d_anfi \r\n" +
                                                  "WHERE tipologia_test_di_laboratorio LIKE :tipologiaTestDiLaboratorio \r\n" +
                                                  " %s \r\n" +
                                                  " AND NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) >= NVL(TO_DATE(:dataInizioVal,'DD/MM/YYYY'),NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY'))) \r\n" +
                                                  //" AND NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY')) <= NVL(:dataFineVal,NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY'))) \r\n" +
                                                  "ORDER BY tipologia_test_di_laboratorio";
  
  private static final String SELECT_BY_ID = "SELECT * FROM iuf_d_anfi WHERE id_anfi = :idAnfi";

  private static final String DELETE = "DELETE FROM iuf_d_anfi WHERE id_anfi = :idAnfi";

  private static final String SELECT_VALIDI = "select * from iuf_d_anfi t where T.data_fine_validita is null ORDER BY UPPER(tipologia_test_di_laboratorio)";

  public AnfiDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public AnfiDTO insertAnfi(AnfiDTO anfi) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertAnfi]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {

      mapParameterSource.addValue("tipologiaTestDiLaboratorio", anfi.getTipologiaTestDiLaboratorio(), Types.VARCHAR);
      mapParameterSource.addValue("costo", anfi.getCosto(), Types.DOUBLE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", anfi.getExtIdUtenteAggiornamento());
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_anfi"});
      anfi.setIdAnfi(holder.getKey().intValue());
      return anfi;
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

  public List<AnfiDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, AnfiDTO.class);
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

  public List<AnfiDTO> findByFilter(AnfiDTO anfi) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("tipologiaTestDiLaboratorio", anfi.getTipologiaTestDiLaboratorio()+"%");
    String select = null;
    if (anfi.getCosto() != null) {
      select = String.format(SELECT_BY_FILTER, "AND costo = :costo ");
      mapParameterSource.addValue("costo", anfi.getCosto(), Types.DOUBLE);
    }
    else
      select = String.format(SELECT_BY_FILTER, "");
    
    try
    {
      List<AnfiDTO> list = queryForList(select, mapParameterSource, AnfiDTO.class);
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

  public AnfiDTO findById(Integer idAnfi) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idAnfi", idAnfi);
    
    try
    {
      AnfiDTO anfi = queryForObject(SELECT_BY_ID, mapParameterSource, AnfiDTO.class);
      return anfi;
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

  public void remove(Integer idAnfi) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idAnfi", idAnfi);
    
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
  
  public List<AnfiDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<AnfiDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, AnfiDTO.class);
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

  public void updateDataFineValidita(Integer idAnfi) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE iuf_d_anfi SET data_fine_validita = SYSDATE," +
    "data_ultimo_aggiornamento = SYSDATE " +
    "WHERE id_anfi = :idAnfi";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idAnfi", idAnfi);
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

  public void update(AnfiDTO anfi) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idAnfi", anfi.getIdAnfi());
    mapParameterSource.addValue("tipologiaTestDiLaboratorio", anfi.getTipologiaTestDiLaboratorio(), Types.VARCHAR);
    mapParameterSource.addValue("costo", anfi.getCosto(), Types.DOUBLE);
    mapParameterSource.addValue("ext_id_utente_aggiornamento", anfi.getExtIdUtenteAggiornamento());
    
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

  
  public List<AnfiDTO> findByIdMultipli(String idMultipli) throws InternalUnexpectedException
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
      List<AnfiDTO> list = queryForList(select, mapParameterSource, AnfiDTO.class);
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
