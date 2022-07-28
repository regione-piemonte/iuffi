package it.csi.iuffi.iuffiweb.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.TipoAreaDTO;
import it.csi.iuffi.iuffiweb.model.VelocitaDTO;
import it.csi.iuffi.iuffiweb.model.api.DettaglioTipoArea;
import it.csi.iuffi.iuffiweb.model.api.TipoArea;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class TipoAreaDAO extends BaseDAO
{

  private static final String THIS_CLASS = TipoAreaDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_tipo_area(id_tipo_area, desc_tipo_area, codice_ufficiale, codice_app_in_campo, dettaglio_tipo_area," +
                                            "typology_of_location,velocita,data_inizio_validita, data_fine_validita,\r\n" +
                                            "ext_id_utente_aggiornamento, data_ultimo_aggiornamento) \r\n" + 
                                        "VALUES(seq_iuf_d_tipo_area.nextval, UPPER(:descTipoArea), UPPER(:codiceUfficiale), UPPER(:codiceAppInCampo)," +
                                            "UPPER(:dettaglioTipoArea), :typologyOfLocation, :velocita,sysdate, null,\r\n" +
                                            ":ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_d_tipo_area " +
                                        "SET desc_tipo_area = UPPER(:descTipoArea)," +
                                           "codice_ufficiale = UPPER(:codiceUfficiale)," +
                                           "dettaglio_tipo_area = UPPER(:dettaglioTipoArea)," +
                                           "typology_of_location = :typologyOfLocation," +
                                           "velocita = :velocita," +
                                           "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento," +
                                           "data_ultimo_aggiornamento = SYSDATE \r\n" +
                                        "WHERE id_tipo_area = :idTipoArea";

  private static final String SELECT_ALL = "SELECT t.*,CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato "+ 
                                           "FROM iuf_d_tipo_area t ORDER BY UPPER(t.desc_tipo_area),UPPER(t.dettaglio_tipo_area),t.data_inizio_validita desc";
  
  private static final String SELECT_ALL_ORDER_DETT = "SELECT t.*,CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato "+ 
                                                      "FROM iuf_d_tipo_area t ORDER BY UPPER(t.dettaglio_tipo_area),t.data_inizio_validita desc";

  private static final String SELECT_BY_FILTER = "SELECT * FROM iuf_d_tipo_area \r\n" +
                                                  "WHERE desc_tipo_area LIKE :descTipoArea \r\n" +
                                                  " AND NVL(codice_ufficiale,'.') LIKE :codiceUfficiale\r\n" +
                                                  " AND NVL(codice_app_in_campo,'.') LIKE :codiceAppInCampo\r\n" +
                                                  " AND NVL(dettaglio_tipo_area,'.') LIKE :dettaglioTipoArea\r\n" +
                                                  " AND NVL(velocita,'0') LIKE :velocita\r\n" +
                                                  " AND NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) <= NVL(TO_DATE(:dataInizioVal,'DD/MM/YYYY'),NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY'))) \r\n" +
                                                  " AND NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY')) >= NVL(TO_DATE(:dataFineVal,'DD/MM/YYYY'),NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY'))) \r\n" +
                                                  "ORDER BY desc_tipo_area";

  private static final String SELECT_VALIDI = "SELECT * FROM iuf_d_tipo_area t WHERE t.data_fine_validita IS NULL ORDER BY UPPER(t.desc_tipo_area),UPPER(t.dettaglio_tipo_area)";

  private static final String SELECT_VALIDI_ORDER_DETT = "SELECT * FROM iuf_d_tipo_area t WHERE t.data_fine_validita IS NULL ORDER BY UPPER(t.dettaglio_tipo_area)";

  private static final String SELECT_BY_ID = "SELECT * FROM iuf_d_tipo_area WHERE id_tipo_area = :idTipoArea";

  private static final String DELETE = "DELETE FROM iuf_d_tipo_area WHERE id_tipo_area = :idTipoArea";

  private static final String SELECT_TIPO_AREE = "SELECT\r\n" + 
                                                  "    desc_tipo_area,codice_ufficiale,\r\n" + 
                                                  "    LISTAGG(id_tipo_area||'#'||dettaglio_tipo_area||'#'||TO_CHAR(data_inizio_validita,'DD-MM-YYYY')||'#'||TO_CHAR(data_fine_validita,'DD-MM-YYYY'),'@')\r\n" +
                                                  "    WITHIN GROUP(ORDER BY dettaglio_tipo_area) AS dettaglio_tipo_aree \r\n" + 
                                                  "FROM\r\n" + 
                                                  "    iuf_d_tipo_area \r\n" + 
                                                  "GROUP BY\r\n" + 
                                                  "    desc_tipo_area,codice_ufficiale \r\n" + 
                                                  "ORDER BY\r\n" + 
                                                  "    desc_tipo_area";
  
  private static final String SELECT_ALL_ID_MULTIPLI = "select * from IUF_D_TIPO_AREA t where id_tipo_area in(%s)";
  

  public TipoAreaDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public TipoAreaDTO insertTipoArea(TipoAreaDTO tipoArea) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertTipoArea]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {

      mapParameterSource.addValue("descTipoArea", tipoArea.getDescTipoArea(), Types.VARCHAR);
      mapParameterSource.addValue("codiceUfficiale", tipoArea.getCodiceUfficiale(), Types.VARCHAR);
      mapParameterSource.addValue("codiceAppInCampo", (StringUtils.isBlank(tipoArea.getCodiceAppInCampo()))?" ":tipoArea.getCodiceAppInCampo(), Types.VARCHAR);
      mapParameterSource.addValue("dettaglioTipoArea", tipoArea.getDettaglioTipoArea(), Types.VARCHAR);
      mapParameterSource.addValue("typologyOfLocation", tipoArea.getTypologyOfLocation(), Types.VARCHAR);
      mapParameterSource.addValue("velocita", tipoArea.getVelocita(), Types.INTEGER);
      mapParameterSource.addValue("dataInizioVal", tipoArea.getDataInizioValidita(), Types.DATE);
      mapParameterSource.addValue("dataFineVal", tipoArea.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", tipoArea.getExtIdUtenteAggiornamento());
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_tipo_area"});
      tipoArea.setIdTipoArea(holder.getKey().intValue());
      return tipoArea;
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

  public List<TipoAreaDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL_ORDER_DETT, mapParameterSource, TipoAreaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL_ORDER_DETT, mapParameterSource);
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

  public List<VelocitaDTO> findVelocita() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findVelocita";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String SELECT_VELOCITA = "SELECT DISTINCT velocita AS id, TO_CHAR(velocita) AS label FROM iuf_d_tipo_area WHERE velocita IS NOT NULL ORDER BY 1";
    try
    {
      return queryForList(SELECT_VELOCITA, mapParameterSource, VelocitaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_VELOCITA, mapParameterSource);
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
  
  public List<TipoAreaDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<TipoAreaDTO> list = queryForList(SELECT_VALIDI_ORDER_DETT, mapParameterSource, TipoAreaDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_VALIDI_ORDER_DETT, mapParameterSource);
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

  public List<TipoAreaDTO> findByFilter(TipoAreaDTO tipoArea) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("descTipoArea", tipoArea.getDescTipoArea()+"%");
    mapParameterSource.addValue("codiceAppInCampo", tipoArea.getCodiceAppInCampo()+"%", Types.VARCHAR);
    mapParameterSource.addValue("codiceUfficiale", tipoArea.getCodiceUfficiale()+"%", Types.VARCHAR);
    mapParameterSource.addValue("dettaglioTipoArea", tipoArea.getDettaglioTipoArea()+"%", Types.VARCHAR);
    mapParameterSource.addValue("velocita", tipoArea.getVelocita()+"%", Types.INTEGER);
    
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
    mapParameterSource.addValue("dataInizioVal", (tipoArea.getDataInizioValidita()!=null)?sdf.format(tipoArea.getDataInizioValidita()):"");
    mapParameterSource.addValue("dataFineVal", (tipoArea.getDataFineValidita()!=null)?sdf.format(tipoArea.getDataFineValidita()):"");
    try
    {
      List<TipoAreaDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, TipoAreaDTO.class);
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

  public TipoAreaDTO findById(Integer idTipoArea) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoArea", idTipoArea);
    
    try
    {
      TipoAreaDTO tipoArea = queryForObject(SELECT_BY_ID, mapParameterSource, TipoAreaDTO.class);
      return tipoArea;
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

  
  public List<TipoAreaDTO> findByIdMultipli(String idMultipli) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdMultipli";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
  //  mapParameterSource.addValue("idMultipli", idMultipli, Types.INTEGER);
    String select = String.format(SELECT_ALL_ID_MULTIPLI,idMultipli);
    try
    {
      List<TipoAreaDTO> list = queryForList(select, mapParameterSource, TipoAreaDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      logger.error(SELECT_ALL_ID_MULTIPLI);
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

  public void remove(Integer idTipoArea) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoArea", idTipoArea);
    
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
  
  public void update(TipoAreaDTO tipoArea) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoArea", tipoArea.getIdTipoArea());
    mapParameterSource.addValue("descTipoArea", tipoArea.getDescTipoArea(), Types.VARCHAR);
    mapParameterSource.addValue("codiceUfficiale", tipoArea.getCodiceUfficiale(), Types.VARCHAR);
    mapParameterSource.addValue("codiceAppInCampo", (StringUtils.isBlank(tipoArea.getCodiceAppInCampo()))?" ":tipoArea.getCodiceAppInCampo(), Types.VARCHAR);
    mapParameterSource.addValue("dettaglioTipoArea", tipoArea.getDettaglioTipoArea(), Types.VARCHAR);
    mapParameterSource.addValue("typologyOfLocation", tipoArea.getTypologyOfLocation());
    mapParameterSource.addValue("velocita", tipoArea.getVelocita());
    mapParameterSource.addValue("dataInizioVal", tipoArea.getDataInizioValidita(), Types.DATE);
    mapParameterSource.addValue("ext_id_utente_aggiornamento", tipoArea.getExtIdUtenteAggiornamento());
    
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

  
  public void updateDataFineValidita(Integer idTipoArea) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE iuf_d_tipo_area SET data_fine_validita = SYSDATE," +
    "data_ultimo_aggiornamento = SYSDATE " +
    "WHERE id_tipo_area = :idTipoArea";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoArea", idTipoArea);
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

  
  public List<TipoArea> getTipoAree() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getTipoAree";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return namedParameterJdbcTemplate.query(SELECT_TIPO_AREE, mapParameterSource, getRowMapper());
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_TIPO_AREE, mapParameterSource);
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

  private RowMapper<TipoArea> getRowMapper() {
    return new RowMapper<TipoArea>() {
      
      @Override
      public TipoArea mapRow(ResultSet rs, int rowNum) throws SQLException {
        TipoArea tipoArea = new TipoArea();
        tipoArea.setDescTipoArea(rs.getString("desc_tipo_area"));
        tipoArea.setCodiceUfficiale(rs.getString("codice_ufficiale"));
       // tipoArea.setCodiceAppInCampo(rs.getString("codice_app_in_campo"));
        String dettaglio = rs.getString("dettaglio_tipo_aree");
        if (dettaglio != null) {
          String[] k = dettaglio.split("@");
          DettaglioTipoArea dta = null;
          List<DettaglioTipoArea> list = new ArrayList<DettaglioTipoArea>();
          for (int i=0; i<k.length; i++) {
            String[] rec = k[i].split("#");
            if (rec.length > 3)
              dta = new DettaglioTipoArea(Integer.decode(rec[0]), rec[1], rec[2], rec[3]);
            else
              dta = new DettaglioTipoArea(Integer.decode(rec[0]), rec[1], rec[2]);
            list.add(dta);
          }
          tipoArea.setDettaglioTipoAree(list);
        }
        return tipoArea;
      }
    };
  }

}
