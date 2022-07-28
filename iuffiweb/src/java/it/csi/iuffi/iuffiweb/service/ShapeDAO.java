package it.csi.iuffi.iuffiweb.service;


import java.sql.ResultSet;
import java.sql.SQLException;
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
import it.csi.iuffi.iuffiweb.model.ShapeDTO;
import it.csi.iuffi.iuffiweb.model.api.Coordinate;


public class ShapeDAO extends BaseDAO
{

  
  private static final String THIS_CLASS = ShapeDAO.class.getSimpleName();

  
  public ShapeDAO(){  }

  
  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public ShapeDTO insert(ShapeDTO shape) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insert]";
    KeyHolder holder = new GeneratedKeyHolder();
    
    String INSERT = null;

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      INSERT = "INSERT INTO iuf_d_shape_master(id_shape_master, descrizione, data_attivita, id_tipo_attivita,\r\n" +
                      "data_inizio_validita, data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento, id_organismo_nocivo,\r\n" +
                      "comune,\r\n" +
                      "campionamento,\r\n" +
                      "trappola,\r\n" +
                      "cod_tipo_trappola,\r\n" +
                      "desc_tipo_trappola,\r\n" +
                      "n_visual,\r\n" +
                      "note,\r\n" +
                      "quadrante) \r\n" +
               "VALUES(seq_iuf_d_shape_master.NEXTVAL, :descrizione, NULL, :idTipoAttivita," +
                     "SYSDATE, NULL, :ext_id_utente_aggiornamento, SYSDATE, :idOrganismoNocivo," +
                     ":comune, :campionamento, :trappola, :codTipoTrappola, :descTipoTrappola," +
                     ":nVisual, :note, :quadrante)";
      mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("descrizione", shape.getDescrizione());
      mapParameterSource.addValue("idTipoAttivita", shape.getIdTipoAttivita());
      mapParameterSource.addValue("idOrganismoNocivo", shape.getIdOrganismoNocivo());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", shape.getExtIdUtenteAggiornamento());
      mapParameterSource.addValue("comune", shape.getComune());
      mapParameterSource.addValue("campionamento", shape.getCampionamento());
      mapParameterSource.addValue("trappola", shape.getTrappola());
      mapParameterSource.addValue("codTipoTrappola", shape.getCodTipoTrappola());
      mapParameterSource.addValue("descTipoTrappola", shape.getDescTipoTrappola());
      mapParameterSource.addValue("nVisual", shape.getnVisual());
      mapParameterSource.addValue("note", shape.getNote());
      mapParameterSource.addValue("quadrante", shape.getQuadrante());
      //logger.debug(INSERT);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource,holder, new String[] {"id_shape_master"});
      shape.setIdShapeMaster(holder.getKey().intValue());
      // Inserimento coordinate nella tabella di dettaglio
      for (Coordinate detail : shape.getDetails()) {
        INSERT = "INSERT INTO iuf_d_shape_detail(id_shape_detail, latitudine, longitudine, id_shape_master) " +
                 "VALUES(seq_iuf_d_shape_detail.NEXTVAL, :latitudine, :longitudine, :id_shape_master)";
        mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue("latitudine", detail.getLatitudine());
        mapParameterSource.addValue("longitudine", detail.getLongitudine());
        mapParameterSource.addValue("id_shape_master", shape.getIdShapeMaster());
        //logger.debug(INSERT);
        namedParameterJdbcTemplate.update(INSERT, mapParameterSource,holder);
      }
      return shape;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]{
          },
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

  public void deleteByOn(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteByOn]";
    String DELETE = null;
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      DELETE = "DELETE FROM iuf_d_shape_detail d\r\n" + 
               " WHERE EXISTS (SELECT 1 FROM iuf_d_shape_master m\r\n" + 
               "                WHERE m.id_shape_master = d.id_shape_master\r\n" + 
               "                  AND m.id_organismo_nocivo = :idOrganismoNocivo)";
      mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      DELETE = "DELETE FROM iuf_d_shape_master\r\n" + 
               " WHERE id_organismo_nocivo = :idOrganismoNocivo";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]{
          },
          new LogVariable[]
          {}, DELETE, mapParameterSource);
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

  public void deleteByOnAndTipoAttivita(Integer idOrganismoNocivo, Long idTipoAttivita) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteByOnAndTipoAttivita]";
    String DELETE = null;
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      DELETE = "DELETE FROM iuf_d_shape_detail d\r\n" + 
               " WHERE EXISTS (SELECT 1 FROM iuf_d_shape_master m\r\n" + 
               "                WHERE m.id_shape_master = d.id_shape_master\r\n" + 
               "                  AND m.id_organismo_nocivo = :idOrganismoNocivo\r\n" +
               "                  AND m.id_tipo_attivita = :idTipoAttivita)";
      mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
      mapParameterSource.addValue("idTipoAttivita", idTipoAttivita);
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      DELETE = "DELETE FROM iuf_d_shape_master\r\n" + 
               " WHERE id_organismo_nocivo = :idOrganismoNocivo\r\n" +
               "   AND id_tipo_attivita = :idTipoAttivita";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]{
          },
          new LogVariable[]
          {}, DELETE, mapParameterSource);
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
  
  public List<ShapeDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "list";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    
    String QUERY = "SELECT ID_ANAGRAFICA,NOME,COGNOME,CF_ANAGRAFICA_EST,\r\n" + 
                     "PAGA_ORARIA,SUBCONTRACTOR,ID_ENTE " + 
                  "FROM IUF_T_ANAGRAFICA t "+
                  " ORDER BY t.ID_ANAGRAFICA ";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(QUERY, mapParameterSource, ShapeDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, null, QUERY, mapParameterSource);
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
  
  
  public List<ShapeDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String SELECT_VALIDI = "SELECT t.id_shape_master, t.descrizione, t.data_attivita, t.id_tipo_attivita, t.data_inizio_validita,\r\n" + 
                          "       t.data_fine_validita, t.ext_id_utente_aggiornamento, t.data_ultimo_aggiornamento,\r\n" + 
                          "       listagg(d.latitudine||'-'||d.longitudine, ';') WITHIN GROUP (ORDER BY d.id_shape_detail) AS coordinate\r\n" + 
                          "  FROM IUF_D_SHAPE_MASTER t, iuf_d_shape_detail d\r\n" + 
                          " WHERE t.id_shape_master = d.id_shape_master\r\n" +
                          " AND t.data_fine_validita IS NULL\r\n" +
                          " GROUP BY t.id_shape_master, t.descrizione, t.data_attivita, t.id_tipo_attivita, t.data_inizio_validita,\r\n" + 
                          "       t.data_fine_validita, t.ext_id_utente_aggiornamento, t.data_ultimo_aggiornamento\r\n" + 
                          " ORDER BY t.id_shape_master";
    try
    {
      List<ShapeDTO> list = namedParameterJdbcTemplate.query(SELECT_VALIDI, mapParameterSource, getRowMapper());
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

  public List<ShapeDTO> findByType(List<Integer> organismi, String tipoAttività, String type) throws InternalUnexpectedException
  {
    // Se type = 'POINT' ritorna i punti
    // se type = 'AREA' ritorna le aree
    final String THIS_METHOD = "findByType";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    String TIPO_ATTIVITA = "";
    if(tipoAttività != null && StringUtils.isNotBlank(tipoAttività)) {
      TIPO_ATTIVITA += " AND t.id_tipo_attivita = :id_tipo_attivita ";
      if(tipoAttività.toUpperCase().equals("V")) {
        mapParameterSource.addValue("id_tipo_attivita", 1); 
      }else if (tipoAttività.toUpperCase().equals("C")) {
        mapParameterSource.addValue("id_tipo_attivita", 2);
      }else if(tipoAttività.toUpperCase().equals("T")) {
        mapParameterSource.addValue("id_tipo_attivita", 3);
      }
    }
    
    String ON_IN_LIST = "";
    if(organismi != null && organismi.size() > 0) {
      ON_IN_LIST += " AND t.id_organismo_nocivo IN (";
      for(int i=0; i<organismi.size(); i++) {
        if(i == organismi.size()-1) {
          ON_IN_LIST += organismi.get(i).toString();
        }else {
          ON_IN_LIST += organismi.get(i).toString() + ",";          
        }
      }
      ON_IN_LIST += ") ";
    }

    String SELECT_VALIDI = "SELECT t.id_shape_master, t.descrizione, t.data_attivita, t.id_tipo_attivita, t.data_inizio_validita,\r\n" + 
                          "       t.data_fine_validita, t.ext_id_utente_aggiornamento, t.data_ultimo_aggiornamento,\r\n" +
                          "       t.id_organismo_nocivo,t.comune,t.campionamento,t.trappola,t.cod_tipo_trappola,t.desc_tipo_trappola,t.n_visual,t.note,\r\n" +
                          "       t.quadrante,\r\n" +
                          "       listagg(d.latitudine||'-'||d.longitudine, ';') WITHIN GROUP (ORDER BY d.id_shape_detail) AS coordinate\r\n" + 
                          "  FROM IUF_D_SHAPE_MASTER t, iuf_d_shape_detail d\r\n" + 
                          " WHERE t.id_shape_master = d.id_shape_master\r\n" +
                          " AND t.data_fine_validita IS NULL\r\n" +
                          TIPO_ATTIVITA +
                          ON_IN_LIST +
                          " GROUP BY t.id_shape_master, t.descrizione, t.data_attivita, t.id_tipo_attivita, t.data_inizio_validita,\r\n" + 
                          "       t.data_fine_validita, t.ext_id_utente_aggiornamento, t.data_ultimo_aggiornamento,t.id_organismo_nocivo,\r\n" +
                          "       t.comune,t.campionamento,t.trappola,t.cod_tipo_trappola,t.desc_tipo_trappola,t.n_visual,t.note,t.quadrante\r\n";
    try
    {
      if (StringUtils.isNotBlank(type) && type.equals("AREA")) {
        SELECT_VALIDI += " HAVING COUNT(*) > 1\r\n";
      } else {
        SELECT_VALIDI += " HAVING COUNT(*) = 1\r\n";
      }
      
      SELECT_VALIDI += " ORDER BY t.id_shape_master";
      logger.debug(SELECT_VALIDI);
      List<ShapeDTO> list = namedParameterJdbcTemplate.query(SELECT_VALIDI, mapParameterSource, getRowMapper());
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

  private RowMapper<ShapeDTO> getRowMapper() {
    return new RowMapper<ShapeDTO>() {
      
      @Override
      public ShapeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        ShapeDTO shape = new ShapeDTO();
        shape.setIdShapeMaster(rs.getInt("id_shape_master"));
        shape.setDataAttivita(rs.getTimestamp("data_attivita"));
        shape.setDataInizioValidita(rs.getTimestamp("data_inizio_validita"));
        shape.setDataFineValidita(rs.getTimestamp("data_fine_validita"));
        shape.setDataUltimoAggiornamento(rs.getTimestamp("data_ultimo_aggiornamento"));
        shape.setDescrizione(rs.getString("descrizione"));
        shape.setIdTipoAttivita(rs.getLong("id_tipo_attivita"));
        shape.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
        shape.setIdOrganismoNocivo(rs.getInt("id_organismo_nocivo"));
        shape.setCampionamento(rs.getString("campionamento"));
        shape.setCodTipoTrappola(rs.getString("cod_tipo_trappola"));
        shape.setDescTipoTrappola(rs.getString("desc_tipo_trappola"));
        shape.setTrappola(rs.getString("trappola"));
        shape.setnVisual(rs.getString("n_visual"));
        shape.setComune(rs.getString("comune"));
        shape.setNote(rs.getString("note"));
        shape.setQuadrante((StringUtils.isNotBlank(rs.getString("quadrante")))?Long.decode(rs.getString("quadrante")):null);
        String[] coordinate = rs.getString("coordinate").split(";");
        List<Coordinate> list = new ArrayList<Coordinate>();
        for (String rec : coordinate) {
          String[] dummy = rec.split("-");
          Double latitudine = Double.valueOf(dummy[0].replace(",", "."));
          Double longitudine = Double.valueOf(dummy[1].replace(",", "."));
          Coordinate pos = new Coordinate(latitudine, longitudine);
          list.add(pos);
        }
        shape.setDetails(list);
        return shape;
      }
    };
  }

}