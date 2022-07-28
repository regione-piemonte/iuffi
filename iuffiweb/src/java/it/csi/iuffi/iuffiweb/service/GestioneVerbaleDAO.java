package it.csi.iuffi.iuffiweb.service;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.VerbaleDTO;
import it.csi.iuffi.iuffiweb.model.api.VerbaleCampioni;
import it.csi.iuffi.iuffiweb.model.api.VerbaleTrappole;
import it.csi.iuffi.iuffiweb.model.api.VerbaleVisual;
import it.csi.iuffi.iuffiweb.model.request.VerbaleDati;
import it.csi.iuffi.iuffiweb.util.DateUtils;
import oracle.jdbc.OracleTypes;

public class GestioneVerbaleDAO extends BaseDAO
{

  
  private static final String THIS_CLASS = GestioneVerbaleDAO.class.getSimpleName();

  public GestioneVerbaleDAO(){  }
  
  
  private static final String SELECT_VISUAL = "SELECT r.id_missione,v.id_ispezione id_visual, v.istat_comune,comuni.DESCRIZIONE_COMUNE comune_visual,v.latitudine lat_visual,v.longitudine long_visual,\r\n" + 
      "       v.id_specie_vegetale,sv.nome_volgare||' ('||sv.genere_specie||')' specie_vegetale_indagata,ta.id_tipo_area,\r\n" + 
      "       ta.desc_tipo_area||' ('||ta.dettaglio_tipo_area||')' tipologia_area,v.note note_visual,\r\n" + 
      "       LISTAGG(o.sigla,'; ') WITHIN GROUP( ORDER BY o.sigla) AS organismi_nocivi_visual\r\n" + 
      "FROM iuf_t_ispezione_visiva v, smrgaa_v_dati_amministrativi comuni,iuf_d_specie_vegetale sv,iuf_t_rilevazione r,iuf_d_tipo_area ta,\r\n" + 
      "     iuf_r_isp_visiva_spec_on vso,iuf_d_organismo_nocivo o\r\n" + 
      "WHERE v.istat_comune = comuni.ISTAT_COMUNE (+)\r\n" + 
      "AND v.id_specie_vegetale = sv.id_specie_vegetale\r\n" + 
      "AND r.id_rilevazione = v.id_rilevazione\r\n" + 
      "AND r.id_tipo_area = ta.id_tipo_area\r\n" + 
      "AND vso.id_ispezione (+) = v.id_ispezione\r\n" + 
      "AND vso.id_specie_on = o.id_organismo_nocivo (+)\r\n" + 
      "AND r.id_missione = :idMissione\r\n" + 
      "GROUP BY r.id_missione,v.id_ispezione,v.istat_comune,comuni.DESCRIZIONE_COMUNE,v.latitudine,v.longitudine,\r\n" + 
      "       v.id_specie_vegetale,sv.nome_volgare||' ('||sv.genere_specie||')',ta.id_tipo_area,\r\n" + 
      "       ta.desc_tipo_area||' ('||ta.dettaglio_tipo_area||')',v.note\r\n" + 
      "";
  private static final String SELECT_CAMPIONI = "SELECT r.id_missione,r.id_rilevazione,c.id_campionamento,\r\n" + 
                                                "       (SELECT LISTAGG(oc.sigla,'; ') WITHIN GROUP( ORDER BY oc.sigla)\r\n" + 
                                                "         FROM iuf_d_organismo_nocivo oc,iuf_r_campionamento_spec_on cso\r\n" + 
                                                "        WHERE oc.id_organismo_nocivo = cso.id_specie_on\r\n" + 
                                                "          AND cso.id_campionamento = c.id_campionamento) AS organismi_nocivi_camp,\r\n" + 
                                                "       1 n_campioni,\r\n" + 
                                                "       tc.tipologia_campione,\r\n" + 
                                                "       c.note\r\n" + 
                                                " FROM iuf_t_rilevazione r,iuf_t_campionamento c,\r\n" + 
                                                "      iuf_d_tipo_campione tc\r\n" + 
                                                " WHERE r.id_rilevazione = c.id_rilevazione\r\n" + 
                                                " AND c.id_tipo_campione = tc.id_tipo_campione (+)\r\n" + 
                                                " AND r.id_missione = :idMissione\r\n" + 
                                                " ORDER BY c.id_campionamento";
  
  private static final String SELECT_TRAPPOLE = "SELECT r.id_missione,r.id_rilevazione,t.id_trappolaggio, ttr.tipologia_trappola,tr.latitudine,tr.longitudine,\r\n" + 
      "       NULL AS raccolta,tr.codice_sfr AS codice_trappola,t.note\r\n" + 
      "FROM iuf_t_rilevazione r,iuf_t_trappolaggio t, iuf_t_trappola tr, iuf_d_tipo_trappola ttr\r\n" + 
      "WHERE r.id_rilevazione = t.id_rilevazione\r\n" + 
      "AND t.id_trappola = tr.id_trappola\r\n" + 
      "AND tr.id_tipo_trappola = ttr.id_tipo_trappola\r\n" + 
      "AND r.id_missione = :idMissione\r\n" + 
      "";
  
  private static final String SELECT_VISUAL_ISPETTORE = "SELECT r.id_missione,v.id_ispezione id_visual, v.istat_comune,comuni.DESCRIZIONE_COMUNE comune_visual,v.latitudine lat_visual,v.longitudine long_visual,\r\n" + 
      "       v.id_specie_vegetale,sv.nome_volgare||' ('||sv.genere_specie||')' specie_vegetale_indagata,ta.id_tipo_area,\r\n" + 
      "       ta.desc_tipo_area||' ('||ta.dettaglio_tipo_area||')' tipologia_area,v.note note_visual,\r\n" + 
      "       LISTAGG(o.sigla,'; ') WITHIN GROUP( ORDER BY o.sigla) AS organismi_nocivi_visual\r\n" + 
      "FROM iuf_t_ispezione_visiva v, smrgaa_v_dati_amministrativi comuni,iuf_d_specie_vegetale sv,iuf_t_rilevazione r,iuf_d_tipo_area ta,\r\n" + 
      "     iuf_r_isp_visiva_spec_on vso,iuf_d_organismo_nocivo o\r\n" + 
      "WHERE v.istat_comune = comuni.ISTAT_COMUNE (+)\r\n" + 
      "AND v.id_specie_vegetale = sv.id_specie_vegetale\r\n" + 
      "AND r.id_rilevazione = v.id_rilevazione\r\n" + 
      "AND r.id_tipo_area = ta.id_tipo_area\r\n" + 
      "AND vso.id_ispezione (+) = v.id_ispezione\r\n" + 
      "AND vso.id_specie_on = o.id_organismo_nocivo (+)\r\n" + 
      "AND r.id_missione = :idMissione\r\n" + 
      "AND v.id_anagrafica = :idAnagrafica\r\n" +
      "GROUP BY r.id_missione,v.id_ispezione,v.istat_comune,comuni.DESCRIZIONE_COMUNE,v.latitudine,v.longitudine,\r\n" + 
      "       v.id_specie_vegetale,sv.nome_volgare||' ('||sv.genere_specie||')',ta.id_tipo_area,\r\n" + 
      "       ta.desc_tipo_area||' ('||ta.dettaglio_tipo_area||')',v.note";

  private static final String SELECT_CAMPIONI_ISPETTORE = "SELECT r.id_missione,r.id_rilevazione,c.id_campionamento,\r\n" + 
                                                          "       (SELECT LISTAGG(oc.sigla,'; ') WITHIN GROUP( ORDER BY oc.sigla)\r\n" + 
                                                          "          FROM iuf_d_organismo_nocivo oc,iuf_r_campionamento_spec_on cso\r\n" + 
                                                          "         WHERE oc.id_organismo_nocivo = cso.id_specie_on\r\n" + 
                                                          "           AND cso.id_campionamento = c.id_campionamento) AS organismi_nocivi_camp,\r\n" + 
                                                          "       1 n_campioni,\r\n" + 
                                                          "       tc.tipologia_campione,\r\n" + 
                                                          "       c.note  \r\n" + 
                                                          "  FROM iuf_t_rilevazione r,iuf_t_campionamento c, \r\n" + 
                                                          "       iuf_d_tipo_campione tc\r\n" + 
                                                          " WHERE r.id_rilevazione = c.id_rilevazione \r\n" + 
                                                          "   AND c.id_tipo_campione = tc.id_tipo_campione (+) \r\n" + 
                                                          "   AND r.id_missione = :idMissione\r\n" + 
                                                          "   AND c.id_anagrafica = :idAnagrafica\r\n" + 
                                                          " ORDER BY c.id_campionamento";

  private static final String SELECT_TRAPPOLE_ISPETTORE = "SELECT r.id_missione,r.id_rilevazione,t.id_trappolaggio, ttr.tipologia_trappola,tr.latitudine,tr.longitudine,\r\n" + 
      "       NULL AS raccolta,tr.codice_sfr AS codice_trappola,t.note\r\n" + 
      "FROM iuf_t_rilevazione r,iuf_t_trappolaggio t, iuf_t_trappola tr, iuf_d_tipo_trappola ttr\r\n" + 
      "WHERE r.id_rilevazione = t.id_rilevazione\r\n" + 
      "AND t.id_trappola = tr.id_trappola\r\n" + 
      "AND tr.id_tipo_trappola = ttr.id_tipo_trappola\r\n" + 
      "AND r.id_missione = :idMissione\r\n" + 
      "AND t.id_anagrafica = :idAnagrafica" +
      "";
/*  
  private static final String SELECT_INTESTAZIONE_VERBALE = "SELECT v.data_ora_inizio_missione, v.data_ora_fine_missione, a.nome || ' ' || a.cognome as rilevatore\r\n" + 
      "      FROM iuf_t_missione v, iuf_t_anagrafica a\r\n" + 
      "      WHERE v.id_ispettore_assegnato = a.id_anagrafica \r\n" + 
      "      AND v.id_missione = :idMissione\r\n";
*/
  // Modificato il 09/07/2021 (S.D.)
  private static final String SELECT_INTESTAZIONE_VERBALE =
      "SELECT TO_CHAR(v.data_ora_inizio_missione,'DD/MM/YYYY') AS data,\r\n" +
      "       TO_CHAR(v.data_ora_inizio_missione,'HH24:MI') AS ora_inizio,\r\n" +
      "       TO_CHAR(v.data_ora_fine_missione,'HH24:MI') AS ora_fine, a.nome || ' ' || a.cognome as rilevatore\r\n" + 
      "  FROM iuf_t_missione v, iuf_t_anagrafica a\r\n" + 
      " WHERE v.id_ispettore_assegnato = a.id_anagrafica \r\n" + 
      "   AND v.id_missione = :idMissione\r\n";

  private static final String INSERT = "INSERT INTO iuf_t_verbale(" +
     "id_verbale," + 
     "data_verbale," + 
     "id_missione," +
     "id_ispettore," +
     "pdf_verbale," +
     "ext_id_utente_aggiornamento," +
     "data_ultimo_aggiornamento," +
     "num_verbale," +
     "id_stato_verbale," +
     "ext_id_documento_index" +
 ") " + 
 "VALUES(seq_iuf_t_verbale.nextval, SYSDATE, :idMissione, :idIspettore, :pdfVerbale, " +
     ":ext_id_utente_aggiornamento, SYSDATE, :numVerbale, :idStatoVerbale, :extIdDocumentoIndex)";
  
  private static final String UPDATE  = "UPDATE iuf_t_verbale set pdf_verbale = :pdfVerbale, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, "
      + "data_ultimo_aggiornamento = SYSDATE, num_verbale = NVL(:numVerbale,num_verbale), id_stato_verbale = :idStatoVerbale, ext_id_documento_index = :extIdDocumentoIndex"
      + " WHERE id_verbale = :idVerbale";

  private static final String UPDATE_STATO_VERBALE  = "UPDATE iuf_t_verbale SET ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento,\r\n"
      + "data_ultimo_aggiornamento = SYSDATE, id_stato_verbale = :idStatoVerbale, ext_id_documento_index = :extIdDocumentoIndex \r\n"
      + " WHERE id_verbale = :idVerbale";

  private static final String SELECT_VERBALE_FROM_MISSIONE = "SELECT id_verbale, \r\n" + 
      "data_verbale, \r\n" + 
      "id_missione, \r\n" + 
      "id_ispettore, \r\n" + 
      //"pdf_verbale, \r\n" + 
      "ext_id_utente_aggiornamento, \r\n" + 
      "data_ultimo_aggiornamento, \r\n" + 
      "num_verbale, \r\n" + 
      "id_stato_verbale, \r\n" + 
      "ext_id_documento_index\r\n" + 
      " FROM iuf_t_verbale WHERE id_missione = :idMissione ";
  
  private static final String SELECT_VERBALE = "SELECT id_verbale, \r\n" + 
      "data_verbale, \r\n" + 
      "id_missione, \r\n" + 
      "id_ispettore, \r\n" + 
      //"pdf_verbale, \r\n" + 
      "ext_id_utente_aggiornamento, \r\n" + 
      "data_ultimo_aggiornamento, \r\n" + 
      "num_verbale, \r\n" + 
      "id_stato_verbale, \r\n" + 
      "ext_id_documento_index\r\n" + 
      " FROM iuf_t_verbale WHERE id_verbale = :idVerbale ";
  
 
  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public VerbaleDTO insert(VerbaleDTO verbale) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertVerbale]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {

      mapParameterSource.addValue("idMissione", verbale.getIdMissione());
      mapParameterSource.addValue("idIspettore", verbale.getIdIspettore());      

      if (verbale.getPdfVerbale() != null) {
        logger.debug("pdf presente");
        byte[] bytes = verbale.getPdfVerbale().getBytes();
        mapParameterSource.addValue("pdfVerbale", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), OracleTypes.BLOB);
      } else {
          logger.debug("pdf non presente");
          if (verbale.getPdfJasper() != null) {
            logger.debug("pdf jasper presente");
            byte[] bytes = verbale.getPdfJasper();
            mapParameterSource.addValue("pdfVerbale", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), OracleTypes.BLOB);
          }
          else
          {
            logger.debug("pdf jasper non presente");
            mapParameterSource.addValue("pdfVerbale", null, Types.BLOB);
          }
      }

      mapParameterSource.addValue("numVerbale", verbale.getNumVerbale(), Types.VARCHAR);
      mapParameterSource.addValue("idStatoVerbale", verbale.getIdStatoVerbale());
      mapParameterSource.addValue("extIdDocumentoIndex", null, Types.NULL);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", verbale.getExtIdUtenteAggiornamento());
      logger.debug(INSERT);
      int result = namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_verbale"});
      Integer idVerbale = holder.getKey().intValue();
      verbale.setIdVerbale(idVerbale);
      return verbale;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {
              //new LogParameter("idProcedimento", idProcedimento),
          },
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
  
  public List<VerbaleDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT_ALL = " SELECT * FROM IUF_T_CAMPIONAMENTO t  ORDER BY t.ID_CAMPIONAMENTO ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL);
      return queryForList(SELECT_ALL, mapParameterSource, VerbaleDTO.class);
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
   
  public List<VerbaleDTO> findByFilter(VerbaleDTO verbale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_FILTER = "SELECT t.id_verbale,t.data_verbale,t.id_missione,t.id_ispettore, a.numero_trasferta \r\n" +
                              "FROM iuf_t_verbale t, iuf_t_missione a \r\n" +
                              "WHERE t.id_missione = a.id_missione \r\n" +
                              "ORDER BY id_missione";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    /*mapParameterSource.addValue("tipologiaCampione", tipoCampione.getTipologiaCampione()+"%");*/
    String select = null;
    /*if (tipoCampione.getCosto() != null) {
      select = String.format(SELECT_BY_FILTER, "AND costo = :costo ");
      mapParameterSource.addValue("costo", tipoCampione.getCosto(), Types.DOUBLE);
    }
    else*/
      select = String.format(SELECT_BY_FILTER, "");
    
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
   // mapParameterSource.addValue("dataInizioVal", (tipoCampione.getDataInizioVal()!=null)?sdf.format(tipoCampione.getDataInizioVal()):"");
  //  mapParameterSource.addValue("dataFineVal", (tipoCampione.getDataFineVal()!=null)?sdf.format(tipoCampione.getDataFineVal()):"");
    try
    {
      logger.debug(select);
      List<VerbaleDTO> list = queryForList(select, mapParameterSource, VerbaleDTO.class);
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
  
  public VerbaleDTO findById(Integer idVerbale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idVerbale", idVerbale);
    
    try
    {
      logger.debug(SELECT_VERBALE);
      VerbaleDTO campione = queryForObject(SELECT_VERBALE, mapParameterSource, VerbaleDTO.class);
      return campione;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_VERBALE, mapParameterSource);
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

    String DELETE = "DELETE FROM IUF_D_CATEGORIA_ANAG WHERE ID_CATEGORIA_ANAG = :id";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
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

  public void update(VerbaleDTO verbale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("idVerbale", verbale.getIdVerbale());
      if (verbale.getPdfVerbale() != null) {
        logger.debug("pdf verbale presente");
        byte[] bytes = verbale.getPdfVerbale().getBytes();
        mapParameterSource.addValue("pdfVerbale", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), OracleTypes.BLOB);
      }
      else {
        logger.debug("pdf verbale non presente");
        if (verbale.getPdfJasper() != null) {
          logger.debug("pdf jasper presente");
          byte[] bytes = verbale.getPdfJasper();
          mapParameterSource.addValue("pdfVerbale", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), OracleTypes.BLOB);
        }
        else
        {
          logger.debug("pdf jasper non presente");
          mapParameterSource.addValue("pdfVerbale", null, Types.BLOB);
        }
      }
      
      mapParameterSource.addValue("numVerbale", verbale.getNumVerbale(), Types.VARCHAR);
      mapParameterSource.addValue("idStatoVerbale", verbale.getIdStatoVerbale());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", verbale.getExtIdUtenteAggiornamento());
      mapParameterSource.addValue("extIdDocumentoIndex", verbale.getExtIdDocumentoIndex());

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

  public void updateStatoVerbale(VerbaleDTO verbale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateStatoVerbale";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("idVerbale", verbale.getIdVerbale());
      mapParameterSource.addValue("idStatoVerbale", verbale.getIdStatoVerbale());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", verbale.getExtIdUtenteAggiornamento());
      mapParameterSource.addValue("extIdDocumentoIndex", verbale.getExtIdDocumentoIndex());
      
      logger.debug(UPDATE_STATO_VERBALE);
      namedParameterJdbcTemplate.update(UPDATE_STATO_VERBALE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, UPDATE_STATO_VERBALE, mapParameterSource);
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

  public List<VerbaleVisual> getVisual(Integer idMissione) throws InternalUnexpectedException {
    
    final String THIS_METHOD = "getVisual";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    
    try
    {
      logger.debug(SELECT_VISUAL);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      List<VerbaleVisual> visual = queryForList(SELECT_VISUAL, mapParameterSource, VerbaleVisual.class);
      return visual;
    } catch (EmptyResultDataAccessException e) {
        logger.debug("ID Missione non trovato");
        return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_VISUAL, mapParameterSource);
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
  
  public List<VerbaleCampioni> getCampioni(Integer idMissione) throws InternalUnexpectedException {
    
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    
    try
    {
      logger.debug(SELECT_CAMPIONI);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      List<VerbaleCampioni> campioni = namedParameterJdbcTemplate.query(SELECT_CAMPIONI, mapParameterSource, getRowMapper());
      return campioni;
    } catch (EmptyResultDataAccessException e) {
        logger.debug("ID Missione non trovato");
        return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_CAMPIONI, mapParameterSource);
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
  
  public List<VerbaleTrappole> getTrappole(Integer idMissione) throws InternalUnexpectedException {
    
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    
    try
    {
      logger.debug(SELECT_TRAPPOLE);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      List<VerbaleTrappole> trappole = queryForList(SELECT_TRAPPOLE, mapParameterSource, VerbaleTrappole.class);
      return trappole;
    } catch (EmptyResultDataAccessException e) {
        logger.debug("ID Missione non trovato");
        return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_TRAPPOLE, mapParameterSource);
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
  
public VerbaleDati getIntestazione(Integer idMissione) throws InternalUnexpectedException {
    
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    
    try
    {
      logger.debug(SELECT_INTESTAZIONE_VERBALE);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      VerbaleDati intestazione = queryForObject(SELECT_INTESTAZIONE_VERBALE, mapParameterSource, VerbaleDati.class);
      return intestazione;
    } catch (EmptyResultDataAccessException e) {
        logger.debug("ID Missione non trovato");
        return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_INTESTAZIONE_VERBALE, mapParameterSource);
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

  public VerbaleDTO findByIdMissione(Integer idMissione) throws InternalUnexpectedException 
  {
    final String THIS_METHOD = "findByIdMissione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
  
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
  
    try
    {
      logger.debug(SELECT_VERBALE_FROM_MISSIONE);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      VerbaleDTO verbale = queryForObject(SELECT_VERBALE_FROM_MISSIONE, mapParameterSource, VerbaleDTO.class);
      return verbale;
    } catch (EmptyResultDataAccessException e) {
      logger.debug("ID Missione non trovato");
      return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_VERBALE_FROM_MISSIONE, mapParameterSource);
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
  
  private RowMapper<VerbaleCampioni> getRowMapper() {
    return new RowMapper<VerbaleCampioni>() {
  
      @Override
      public VerbaleCampioni mapRow(ResultSet rs, int rowNum) throws SQLException {
        VerbaleCampioni verbaleCampioni = new VerbaleCampioni();
        verbaleCampioni.setOrganismiNociviCamp(rs.getString("organismi_nocivi_camp"));
        verbaleCampioni.setnCampioni(rs.getInt("n_campioni"));
        //verbaleCampioni.setCodiceCampioni(rs.getString("codice_campioni"));
        verbaleCampioni.setTipologiaCampione(rs.getString("tipologia_campione"));
        verbaleCampioni.setIdCampionamento(rs.getInt("id_campionamento"));
        verbaleCampioni.setNote(rs.getString("note"));
        return verbaleCampioni;
      }
    };
  } 
  
  public List<VerbaleVisual> getVisualDiCompetenza(Integer idMissione, Integer idAnagrafica) throws InternalUnexpectedException {
  
    final String THIS_METHOD = "getVisualDiCompetenza";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
  
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    mapParameterSource.addValue("idAnagrafica", idAnagrafica);
  
    try
    {
      logger.debug(SELECT_VISUAL_ISPETTORE);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      List<VerbaleVisual> visual = queryForList(SELECT_VISUAL_ISPETTORE, mapParameterSource, VerbaleVisual.class);
      return visual;
    } catch (EmptyResultDataAccessException e) {
      logger.debug("ID Missione non trovato");
      return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_VISUAL_ISPETTORE, mapParameterSource);
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
  
  public List<VerbaleCampioni> getCampioniDiCompetenza(Integer idMissione, Integer idAnagrafica) throws InternalUnexpectedException {
  
    final String THIS_METHOD = "getCampioniDiCompetenza";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
  
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    mapParameterSource.addValue("idAnagrafica", idAnagrafica);
  
    try
    {
      logger.debug(SELECT_CAMPIONI_ISPETTORE);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      List<VerbaleCampioni> campioni = namedParameterJdbcTemplate.query(SELECT_CAMPIONI_ISPETTORE, mapParameterSource, getRowMapper());
      return campioni;
    } catch (EmptyResultDataAccessException e) {
      logger.debug("ID Missione non trovato");
      return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_CAMPIONI_ISPETTORE, mapParameterSource);
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
  
  public List<VerbaleTrappole> getTrappoleDiCompetenza(Integer idMissione, Integer idAnagrafica) throws InternalUnexpectedException {
  
    final String THIS_METHOD = "getTrappoleDiCompetenza";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
  
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    mapParameterSource.addValue("idAnagrafica", idAnagrafica);
  
    try
    {
      logger.debug(SELECT_TRAPPOLE_ISPETTORE);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      List<VerbaleTrappole> trappole = queryForList(SELECT_TRAPPOLE_ISPETTORE, mapParameterSource, VerbaleTrappole.class);
      return trappole;
    } catch (EmptyResultDataAccessException e) {
      logger.debug("ID Missione non trovato");
      return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_TRAPPOLE_ISPETTORE, mapParameterSource);
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
  
  public byte[] getPdfVerbale(Integer idVerbale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getPdfVerbale";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
  
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idVerbale", idVerbale);
    String SELECT_PDF_VERBALE = "SELECT t.pdf_verbale FROM iuf_t_verbale t WHERE t.id_verbale = :idVerbale";
    try
    {
      logger.debug(SELECT_PDF_VERBALE);
      byte[] pdf = namedParameterJdbcTemplate.queryForObject(SELECT_PDF_VERBALE, mapParameterSource, new BlobRowMapper());
  
      return pdf;
  
    } catch (EmptyResultDataAccessException e) {
      logger.debug("ID Verbale non trovato");
      return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_PDF_VERBALE, mapParameterSource);
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

  public Integer getProgVerbale(String pattern, String escape) throws InternalUnexpectedException {
    
    final String THIS_METHOD = "getProgVerbale";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
  
    String SELECT_PROG_VERBALE = "SELECT NVL(MAX(to_number(SUBSTR(v.num_verbale,INSTR(v.num_verbale,'_',1,2)+1)))+1,1) AS prog_verbale\r\n" + 
                                 "  FROM iuf_t_verbale v\r\n" + 
                                 " WHERE v.num_verbale LIKE :pattern ESCAPE :escape";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("pattern", pattern);
    mapParameterSource.addValue("escape", escape);
  
    try
    {
      logger.debug(SELECT_PROG_VERBALE);
      Long progVerbale = queryForLong(SELECT_PROG_VERBALE, mapParameterSource);
      return progVerbale.intValue();
      
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_PROG_VERBALE, mapParameterSource);
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
  
  public class BlobRowMapper implements RowMapper<byte[]> {
  
    public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
      Blob column = rs.getBlob("PDF_VERBALE");
      return column.getBytes(1L, (int) column.length());
    }
  }

}
