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
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.TipoCampioneDTO;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class TipoCampioneDAO extends BaseDAO
{

  private static final String THIS_CLASS = TipoCampioneDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_tipo_campione(id_tipo_campione, tipologia_campione, typology_of_samples, data_inizio_validita, data_fine_validita," +
                                                  "ext_id_utente_aggiornamento, data_ultimo_aggiornamento) \r\n" + 
                                        "VALUES(seq_iuf_d_tipo_campione.nextval, UPPER(:tipologiaCampione), :typologyOfSamples, SYSDATE, null,\r\n" +
                                                  ":ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_d_tipo_campione SET tipologia_campione = :tipologiaCampione, typology_of_samples = :typologyOfSamples,"
      + " ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE WHERE id_tipo_campione = :idTipoCampione";

  private static final String SELECT_ALL = "SELECT t.*,CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato FROM iuf_d_tipo_campione t "+
                                            "ORDER BY UPPER(tipologia_campione),t.data_inizio_validita desc";
  
  private static final String SELECT_ALL_ATTIVI = "SELECT t.* FROM iuf_d_tipo_campione t "+
                                                   "WHERE t.data_fine_validita is null ORDER BY UPPER(tipologia_campione)";

  private static final String SELECT_BY_FILTER = "SELECT * FROM iuf_d_tipo_campione \r\n" +
                                                  "WHERE tipologia_campione LIKE :tipologiaCampione \r\n" +
                                                  " %s \r\n" +
                                                  " AND NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) <= NVL(TO_DATE(:dataInizioVal,'DD/MM/YYYY'),NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY'))) \r\n" +
                                                  " AND NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY')) >= NVL(TO_DATE(:dataFineVal,'DD/MM/YYYY'),NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY'))) \r\n" +
                                                  "ORDER BY tipologia_campione";
  
  private static final String SELECT_VALIDI = "select * from iuf_d_tipo_campione t where T.data_fine_validita is null order by upper(tipologia_campione)";

  private static final String SELECT_BY_ID = "SELECT * FROM iuf_d_tipo_campione WHERE id_tipo_campione = UPPER(:idTipoCampione)";

  private static final String DELETE = "DELETE FROM iuf_d_tipo_campione WHERE id_tipo_campione = :idTipoCampione";

  private static final String SELECT_ALL_ID_MULTIPLI = "select * from iuf_d_tipo_campione t where id_tipo_campione in(%s)";

  public TipoCampioneDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public TipoCampioneDTO insertTipoCampione(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertTipoCampione]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {
      mapParameterSource.addValue("tipologiaCampione", tipoCampione.getTipologiaCampione(), Types.VARCHAR);
      mapParameterSource.addValue("typologyOfSamples", tipoCampione.getTypologyOfSamples(), Types.VARCHAR);
      mapParameterSource.addValue("dataInizioVal", tipoCampione.getDataInizioValidita(), Types.DATE);
      mapParameterSource.addValue("dataFineVal", tipoCampione.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", tipoCampione.getExtIdUtenteAggiornamento());
      logger.debug(INSERT);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_tipo_campione"});
      tipoCampione.setIdTipoCampione(holder.getKey().intValue());
      return tipoCampione;
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

  public List<TipoCampioneDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL);
      return queryForList(SELECT_ALL, mapParameterSource, TipoCampioneDTO.class);
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


  public void updateDataFineValidita(Integer idTipoCampione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE iuf_d_tipo_campione SET data_fine_validita = SYSDATE," +
                      "data_ultimo_aggiornamento = SYSDATE " +
                      "WHERE id_tipo_campione = :idTipoCampione";

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
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

  
  public List<TipoCampioneDTO> findAllAttivi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL_ATTIVI);
      return queryForList(SELECT_ALL_ATTIVI, mapParameterSource, TipoCampioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL_ATTIVI, mapParameterSource);
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

  public List<TipoCampioneDTO> findByFilter(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("tipologiaCampione", tipoCampione.getTipologiaCampione()+"%");
    String select = null;
    select = String.format(SELECT_BY_FILTER, "");
    
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
    mapParameterSource.addValue("dataInizioVal", (tipoCampione.getDataInizioValidita()!=null)?sdf.format(tipoCampione.getDataInizioValidita()):"");
    mapParameterSource.addValue("dataFineVal", (tipoCampione.getDataFineValidita()!=null)?sdf.format(tipoCampione.getDataFineValidita()):"");
    try
    {
      logger.debug(select);
      List<TipoCampioneDTO> list = queryForList(select, mapParameterSource, TipoCampioneDTO.class);
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

  
  public List<TipoCampioneDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<TipoCampioneDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, TipoCampioneDTO.class);
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

  public TipoCampioneDTO findById(Integer idTipoCampione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      TipoCampioneDTO tipoCampione = queryForObject(SELECT_BY_ID, mapParameterSource, TipoCampioneDTO.class);
      return tipoCampione;
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

  public void remove(Integer idTipoCampione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
    
    try
    {
      logger.debug(DELETE);
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
  
  public void update(TipoCampioneDTO tipoCampione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("tipologiaCampione", tipoCampione.getTipologiaCampione(), Types.VARCHAR);
    mapParameterSource.addValue("typologyOfSamples", tipoCampione.getTypologyOfSamples(), Types.VARCHAR);
    mapParameterSource.addValue("idTipoCampione", tipoCampione.getIdTipoCampione());
    mapParameterSource.addValue("ext_id_utente_aggiornamento", tipoCampione.getExtIdUtenteAggiornamento());
    
    try
    {
      logger.debug(UPDATE);
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

  
  public List<TipoCampioneDTO> findByIdMultipli(String idMultipli) throws InternalUnexpectedException
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
      List<TipoCampioneDTO> list = queryForList(select, mapParameterSource, TipoCampioneDTO.class);
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
