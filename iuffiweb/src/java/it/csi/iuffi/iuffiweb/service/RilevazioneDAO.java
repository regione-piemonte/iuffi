package it.csi.iuffi.iuffiweb.service;


import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.RilevazioneDTO;
import it.csi.iuffi.iuffiweb.model.api.Rilevazione;


public class RilevazioneDAO extends BaseDAO
{

  
  private static final String THIS_CLASS = RilevazioneDAO.class.getSimpleName();

  public RilevazioneDAO(){  }
  
  private static final String SELECT_BY_ID = "SELECT id_rilevazione, \r\n" + 
      "id_missione, \r\n" + 
      "id_anagrafica, \r\n" + 
      "id_tipo_area, \r\n" + 
      "visual, \r\n" + 
      "note, \r\n" + 
      "campionamento, \r\n" + 
      "trappolaggio, \r\n" + 
      "ora_inizio, \r\n" + 
      "ora_fine, \r\n" + 
      "flag_emergenza, \r\n" + 
      "ext_id_utente_aggiornamento, \r\n" + 
      "data_ultimo_aggiornamento, \r\n" + 
      "data_ora_inizio, \r\n" + 
      "data_ora_fine, \r\n" + 
      "ext_numero_aviv numero_aviv, \r\n" + 
      "cuaa, \r\n" + 
      "ext_id_ute id_ute \r\n" + 
      " FROM iuf_t_rilevazione WHERE id_rilevazione = :idRilevazione";
  
  private static final String INSERT = "INSERT INTO iuf_t_rilevazione(" +
     "id_rilevazione," + 
     "id_missione," + 
     "id_anagrafica," +
     "id_tipo_area," +
     "visual," +
     "note," +
     "campionamento," +
     "trappolaggio," +
     "data_ora_inizio," +
     "data_ora_fine," +
     "flag_emergenza," +
     "ext_numero_aviv," +
     "cuaa," +
     "ext_id_ute," +
     "ext_id_utente_aggiornamento," +
     "data_ultimo_aggiornamento" +
 ") " +
 "VALUES(seq_iuf_t_rilevazione.nextval, :idMissione, :idAnagrafica, :idTipoArea, :visual, :note, :campionamento, :trappolaggio, :dataOraInizio, :dataOraFine,  :flagEmergenza," +
 ":numeroAviv,:cuaa,:idUte,:ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_t_rilevazione " +
  "SET id_missione = :idMissione, id_anagrafica = :idAnagrafica, id_tipo_area = :idTipoArea, visual = :visual, note = :note, campionamento = :campionamento, " + 
  "trappolaggio = :trappolaggio, data_ora_inizio = :dataOraInizio, data_ora_fine = :dataOraFine, flag_emergenza = :flagEmergenza," +
  "ext_numero_aviv = :numeroAviv, cuaa = :cuaa, ext_id_ute = :idUte, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
  "WHERE id_rilevazione = :idRilevazione";
 
  private static final String UPDATE_TIPO_AREA = "UPDATE iuf_t_rilevazione " +
              "set id_tipo_area = :idTipoArea where id_rilevazione = :idRilevazione";

  public List<RilevazioneDTO> findByFilter(RilevazioneDTO rilevazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String SELECT_BY_FILTER = "SELECT t.id_rilevazione,t.id_missione,t.id_anagrafica,t.id_tipo_area,\r\n" +
                                "       DECODE((SELECT count(*) FROM iuf_t_ispezione_visiva v WHERE v.id_rilevazione = t.id_rilevazione),0,'N','S') visual,\r\n" +
                                "       DECODE((SELECT count(*) FROM iuf_t_campionamento ca WHERE ca.id_rilevazione = t.id_rilevazione),0,'N','S') campionamento,\r\n" +
                                "       DECODE((SELECT count(*) FROM iuf_t_trappolaggio tr WHERE tr.id_rilevazione = t.id_rilevazione),0,'N','S') trappolaggio,\r\n" +
                                "       t.note,t.data_ora_inizio,t.data_ora_fine,t.ora_inizio,t.ora_fine,\r\n" +
                                "       b.cognome || ' ' || b.nome as operatore,c.dettaglio_tipo_area as area,\r\n" +
                                "       t.flag_emergenza,\r\n" +
                                "       t.ext_numero_aviv as numero_aviv,\r\n" +
                                "       t.cuaa,\r\n" +
                                "       t.ext_id_ute as id_ute,\r\n" +
                                "       to_char(t.data_ora_inizio, 'HH24:MI') as ora_inizio_s,\r\n" +
                                "       to_char(t.data_ora_fine, 'HH24:MI') as ora_fine_s \r\n" +
                                "FROM iuf_t_rilevazione t,iuf_t_missione a,iuf_t_anagrafica b,iuf_d_tipo_area c \r\n" +
                                "WHERE t.id_missione = a.id_missione \r\n" +
                                "AND decode(nvl(t.id_anagrafica,0),0,a.id_ispettore_assegnato,t.id_anagrafica) = b.id_anagrafica \r\n" +
                                "AND t.id_tipo_area = c.id_tipo_area (+) ";
    
    if (rilevazione.getIdMissione()!= null && rilevazione.getIdMissione()>0) {
      SELECT_BY_FILTER += "AND t.id_missione = :idMissione";
      mapParameterSource.addValue("idMissione", rilevazione.getIdMissione());
    }
    
    SELECT_BY_FILTER += " ORDER BY t.id_missione, t.data_ora_inizio";
    
   try
    {
      List<RilevazioneDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, RilevazioneDTO.class);
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
  
  public List<Rilevazione> findByFilterForApi(RilevazioneDTO rilevazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilterForApi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String SELECT_BY_FILTER = "SELECT t.id_rilevazione,t.id_missione,t.id_anagrafica,t.id_tipo_area," +
                                      "NVL(t.visual,'N') visual," +
                                      "t.note,NVL(t.campionamento,'N') campionamento," +
                                      "NVL(t.trappolaggio,'N') trappolaggio," +
                                      "TO_CHAR(t.data_ora_inizio,'DD-MM-YYYY') data_rilevazione," +
                                      "TO_CHAR(t.data_ora_inizio,'HH24:MI') ora_inizio," +
                                      "TO_CHAR(t.data_ora_fine,'HH24:MI') ora_fine," +
                                      "flag_emergenza," +
                                      "t.ext_numero_aviv as numero_aviv,\r\n" +
                                      "t.cuaa,\r\n" +
                                      "t.ext_id_ute as id_ute,\r\n" +
                                      "b.cognome || ' ' || b.nome as operatore,c.desc_tipo_area as area " +
                              "FROM iuf_t_rilevazione t,iuf_t_missione a,iuf_t_anagrafica b,iuf_d_tipo_area c " +
                              "WHERE t.id_missione = a.id_missione " +
                              "AND a.id_ispettore_assegnato = b.id_anagrafica " +
                              "AND t.id_tipo_area = c.id_tipo_area (+) ";
    
    if (rilevazione.getIdMissione()!= null && rilevazione.getIdMissione()>0) {
      SELECT_BY_FILTER += "and t.id_missione = :idMissione";
      mapParameterSource.addValue("idMissione", rilevazione.getIdMissione());
    }
    
    SELECT_BY_FILTER += " ORDER BY t.id_missione, t.id_rilevazione";
    
   try
    {
      List<Rilevazione> list = queryForList(SELECT_BY_FILTER, mapParameterSource, Rilevazione.class);
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
  
  public Integer insert(RilevazioneDTO rilevazione) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertIspezione]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
       
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      mapParameterSource.addValue("idRilevazione", rilevazione.getIdRilevazione());
      mapParameterSource.addValue("idMissione", rilevazione.getIdMissione());
      mapParameterSource.addValue("idAnagrafica", rilevazione.getIdAnagrafica());
      mapParameterSource.addValue("idTipoArea", rilevazione.getIdTipoArea());      
      mapParameterSource.addValue("visual", rilevazione.getVisual(), Types.VARCHAR);
      mapParameterSource.addValue("note", rilevazione.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("campionamento", rilevazione.getCampionamento(), Types.VARCHAR);
      mapParameterSource.addValue("trappolaggio", rilevazione.getTrappolaggio(), Types.VARCHAR);
      mapParameterSource.addValue("dataOraInizio", rilevazione.getDataOraInizio());
      //mapParameterSource.addValue("minutiInizio", rilevazione.getMinutiInizio());
      mapParameterSource.addValue("dataOraFine", rilevazione.getDataOraFine());
      //mapParameterSource.addValue("minutiFine", rilevazione.getMinutiFine());
      mapParameterSource.addValue("flagEmergenza", rilevazione.getFlagEmergenza(), Types.VARCHAR);
      mapParameterSource.addValue("numeroAviv", rilevazione.getNumeroAviv(), Types.VARCHAR);
      mapParameterSource.addValue("cuaa", rilevazione.getCuaa(), Types.VARCHAR);
      mapParameterSource.addValue("idUte", rilevazione.getIdUte());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", rilevazione.getExtIdUtenteAggiornamento());     
      
      logger.debug(INSERT);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_rilevazione"});
      Integer idRilevazione = holder.getKey().intValue();
      rilevazione.setIdRilevazione(idRilevazione);
      return idRilevazione;
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
  
  public void remove(Integer idRilevazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String DELETE = null;
    
    try
    {
      mapParameterSource.addValue("idRilevazione", idRilevazione);
      // DELETE_R_CAMPIONAMENTO_SPEC_ON
      DELETE = "DELETE FROM iuf_r_campionamento_spec_on r \r\n" + 
          "WHERE EXISTS (SELECT 1 FROM iuf_t_campionamento c \r\n" + 
          "              WHERE c.id_campionamento = r.id_campionamento\r\n" + 
          "                AND c.id_rilevazione = :idRilevazione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_campione_foto
      DELETE = "DELETE FROM iuf_r_campione_foto f \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_campionamento c\r\n" + 
          "              WHERE c.id_campionamento = f.id_campionamento\r\n" + 
          "                AND c.id_rilevazione = :idRilevazione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_esito_campione
      DELETE = "DELETE FROM iuf_t_esito_campione e \r\n" +
                "WHERE EXISTS (SELECT 1 FROM iuf_t_campionamento c\r\n" + 
                "              WHERE c.id_campionamento = e.id_campionamento\r\n" + 
                "                AND c.id_rilevazione = :idRilevazione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE CAMPIONAMENTO
      DELETE = "DELETE FROM iuf_t_campionamento t \r\n" + 
                "WHERE t.id_rilevazione = :idRilevazione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_trappolaggio_foto
      DELETE = "DELETE FROM iuf_r_trappolaggio_foto f \r\n" + 
                "WHERE EXISTS (SELECT 1 FROM iuf_t_trappolaggio t\r\n" + 
                "              WHERE t.id_trappolaggio = f.id_trappolaggio\r\n" + 
                "                AND t.id_rilevazione = :idRilevazione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE TRAPPOLAGGIO
      DELETE = "DELETE FROM iuf_t_trappolaggio t \r\n" + 
                "WHERE t.id_rilevazione = :idRilevazione";
      logger.debug(DELETE);
      int recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug("trappolaggi eliminati: " + recs);
      // DELETE iuf_t_trappola
      DELETE = "DELETE FROM iuf_t_trappola t\r\n" +     // Elimino eventuali trappole inutilizzate
                "WHERE NOT EXISTS (SELECT 1 FROM iuf_t_trappolaggio tr " +
                                   "WHERE tr.id_trappola = t.id_trappola)";
      logger.debug(DELETE);
      recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug("trappole eliminate: " + recs);
      // DELETE iuf_r_isp_visiva_spec_on
      DELETE = "DELETE FROM iuf_r_isp_visiva_spec_on i \r\n" + 
                "WHERE EXISTS (SELECT 1 FROM iuf_t_ispezione_visiva v \r\n" + 
                "               WHERE v.id_ispezione = i.id_ispezione\r\n" + 
                "                 AND v.id_rilevazione = :idRilevazione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_isp_visiva_foto
      DELETE = "DELETE FROM iuf_r_isp_visiva_foto i \r\n" + 
                "WHERE EXISTS (SELECT 1 FROM iuf_t_ispezione_visiva v \r\n" + 
                "              WHERE v.id_ispezione = i.id_ispezione_visiva\r\n" + 
                "                AND v.id_rilevazione = :idRilevazione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_isp_visiva_pianta
      DELETE = "DELETE FROM iuf_t_isp_visiva_pianta i \r\n" + 
                "WHERE EXISTS (SELECT 1 FROM iuf_t_ispezione_visiva v \r\n" + 
                "              WHERE v.id_ispezione = i.id_ispezione_visiva\r\n" + 
                "                AND v.id_rilevazione = :idRilevazione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_ispezione_visiva
      DELETE = "DELETE FROM iuf_t_ispezione_visiva i \r\n" + 
                "WHERE i.id_rilevazione = :idRilevazione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_percorso_rilevazioni
      DELETE = "DELETE FROM iuf_t_percorso_rilevazioni pr \r\n" + 
                "WHERE pr.id_rilevazione = :idRilevazione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_rilevazione
      DELETE = "DELETE FROM iuf_t_rilevazione WHERE id_rilevazione = :idRilevazione";
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
  
  public void update(RilevazioneDTO rilevazione) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateRilevazione]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {

      
      mapParameterSource.addValue("idRilevazione", rilevazione.getIdRilevazione());
      mapParameterSource.addValue("idMissione", rilevazione.getIdMissione());
      mapParameterSource.addValue("idAnagrafica", rilevazione.getIdAnagrafica());
      mapParameterSource.addValue("idTipoArea", rilevazione.getIdTipoArea());      
      mapParameterSource.addValue("visual", rilevazione.getVisual(), Types.VARCHAR);
      mapParameterSource.addValue("note", rilevazione.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("campionamento", rilevazione.getCampionamento(), Types.VARCHAR);
      mapParameterSource.addValue("trappolaggio", rilevazione.getTrappolaggio(), Types.VARCHAR);
      mapParameterSource.addValue("dataOraInizio", rilevazione.getDataOraInizio());
      //mapParameterSource.addValue("minutiInizio", rilevazione.getMinutiInizio());
      mapParameterSource.addValue("dataOraFine", rilevazione.getDataOraFine());
      //mapParameterSource.addValue("minutiFine", rilevazione.getMinutiFine());
      mapParameterSource.addValue("flagEmergenza", rilevazione.getFlagEmergenza(), Types.VARCHAR);
      mapParameterSource.addValue("numeroAviv", rilevazione.getNumeroAviv(), Types.VARCHAR);
      mapParameterSource.addValue("cuaa", rilevazione.getCuaa(), Types.VARCHAR);
      mapParameterSource.addValue("idUte", rilevazione.getIdUte());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", rilevazione.getExtIdUtenteAggiornamento());     
      
      logger.debug(UPDATE);
      int result = namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {
              //new LogParameter("idProcedimento", idProcedimento),
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
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
  
  public void updateFilter(RilevazioneDTO rilevazione) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateFilter]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {

      
      mapParameterSource.addValue("idRilevazione", rilevazione.getIdRilevazione());
      if(rilevazione.getIdTipoArea()>0)       
          mapParameterSource.addValue("idTipoArea", rilevazione.getIdTipoArea());      
      
      logger.debug(UPDATE_TIPO_AREA);
      int result = namedParameterJdbcTemplate.update(UPDATE_TIPO_AREA, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {
              //new LogParameter("idProcedimento", idProcedimento),
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
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
 
  public RilevazioneDTO findById(Integer idRilevazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idRilevazione", idRilevazione);
    
    try
    {
      RilevazioneDTO rilevazione = queryForObject(SELECT_BY_ID, mapParameterSource, RilevazioneDTO.class);
      return rilevazione;
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
 
}