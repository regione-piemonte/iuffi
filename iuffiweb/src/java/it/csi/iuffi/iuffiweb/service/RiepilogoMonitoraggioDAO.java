package it.csi.iuffi.iuffiweb.service;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RiepilogoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.api.RiepilogoMonitoraggio;


public class RiepilogoMonitoraggioDAO extends BaseDAO
{
  
  private static final String THIS_CLASS = RiepilogoMonitoraggioDAO.class.getSimpleName();

  private static final String SELECT_ALL = "SELECT id_specie_vegetale,nome_comune_specie,nome_latino_specie,codice_eppo,id_tipo_campione,desc_tipo_campione,mesi,\r\n" +
                                          "       LISTAGG(id_on_da_monitorare||','||sigla,';') WITHIN GROUP (ORDER BY sigla) AS organismi_nocivi\r\n" +
                                          "  FROM (\r\n" +
                                          "        SELECT t.id_specie_vegetale,s.nome_volgare nome_comune_specie,s.genere_specie nome_latino_specie,s.codice_eppo,t.id_tipo_campione,\r\n" +
                                          "               tc.tipologia_campione desc_tipo_campione,\r\n" +
                                          "               t.id_on_da_monitorare,o.sigla,\r\n" +
                                          "               LISTAGG(t.mese_di_monitoraggio,',') WITHIN GROUP (ORDER BY t.mese_di_monitoraggio) mesi\r\n" +
                                          "        FROM iuf_r_specie_on_periodo t, iuf_d_organismo_nocivo o, iuf_d_specie_vegetale s, iuf_d_tipo_campione tc\r\n" +
                                          "        WHERE t.id_on_da_monitorare = o.id_organismo_nocivo\r\n" +
                                          "        AND t.id_specie_vegetale = s.id_specie_vegetale\r\n" +
                                          "        AND t.id_tipo_campione = tc.id_tipo_campione (+)\r\n" +
                                          "        AND t.data_fine_validita IS NULL \r\n" +
                                          "        AND o.data_fine_validita IS NULL \r\n" +
                                          "        AND s.data_fine_validita IS NULL \r\n" +
                                          "        AND tc.data_fine_validita IS NULL \r\n" +
                                          "        GROUP BY t.id_specie_vegetale,s.genere_specie,s.nome_volgare,s.codice_eppo,t.id_on_da_monitorare,\r\n" +
                                          "                 o.sigla,t.id_tipo_campione,tc.tipologia_campione\r\n" +
                                          "        )\r\n" +
                                          "GROUP BY id_specie_vegetale,nome_comune_specie,nome_latino_specie,codice_eppo,id_tipo_campione,desc_tipo_campione,mesi\r\n" +
                                          "ORDER BY nome_comune_specie,desc_tipo_campione,codice_eppo,mesi";
  
  private static final String SELECT_RIEPILOGO_MONITORAGGIO = "SELECT r.id_specie_vegetale, \r\n" + 
                                                              "       r.id_tipo_campione, \r\n" + 
                                                              "       r.mese_di_monitoraggio mese, \r\n" + 
                                                              "       r.id_on_da_monitorare id_organismo_nocivo,\r\n" + 
                                                              "       r.data_inizio_validita,\r\n" + 
                                                              "       r.data_fine_validita \r\n" + 
                                                              "FROM iuf_r_specie_on_periodo r, iuf_d_specie_vegetale s, iuf_d_organismo_nocivo o, iuf_d_tipo_campione tc\r\n" + 
                                                              "WHERE r.id_specie_vegetale = s.id_specie_vegetale\r\n" + 
                                                              "  AND r.id_on_da_monitorare = o.id_organismo_nocivo\r\n" + 
                                                              "  AND r.id_tipo_campione = tc.id_tipo_campione (+)\r\n" + 
                                                              "  AND SYSDATE BETWEEN r.data_inizio_validita AND NVL(r.data_fine_validita,SYSDATE+1)\r\n" + 
                                                              "  AND SYSDATE BETWEEN s.data_inizio_validita AND NVL(s.data_fine_validita,SYSDATE+1)\r\n" + 
                                                              "  AND SYSDATE BETWEEN o.data_inizio_validita AND NVL(o.data_fine_validita,SYSDATE+1)\r\n" + 
                                                              "  AND SYSDATE BETWEEN NVL(tc.data_inizio_validita,SYSDATE-1) AND NVL(tc.data_fine_validita,SYSDATE+1)\r\n" + 
                                                              "ORDER BY r.id_specie_vegetale,r.id_tipo_campione,r.mese_di_monitoraggio,r.id_on_da_monitorare";
  
  private static final String SELECT_BY_ID = "SELECT * FROM (" +
                                                "SELECT id_specie_vegetale,nome_comune_specie,nome_latino_specie,codice_eppo,id_tipo_campione,desc_tipo_campione,mesi,\r\n" + 
                                                "       LISTAGG(id_on_da_monitorare||','||sigla,';') WITHIN GROUP (ORDER BY sigla) AS organismi_nocivi\r\n" + 
                                                "  FROM (\r\n" + 
                                                "        SELECT t.id_specie_vegetale,s.nome_volgare nome_comune_specie,s.genere_specie nome_latino_specie,s.codice_eppo,t.id_tipo_campione,\r\n" + 
                                                "               tc.tipologia_campione desc_tipo_campione,\r\n" + 
                                                "               t.id_on_da_monitorare,o.sigla,\r\n" + 
                                                "               LISTAGG(t.mese_di_monitoraggio,',') WITHIN GROUP (ORDER BY t.mese_di_monitoraggio) mesi\r\n" + 
                                                "        FROM iuf_r_specie_on_periodo t, iuf_d_organismo_nocivo o, iuf_d_specie_vegetale s, iuf_d_tipo_campione tc\r\n" + 
                                                "        WHERE t.id_on_da_monitorare = o.id_organismo_nocivo\r\n" + 
                                                "        AND t.id_specie_vegetale = s.id_specie_vegetale\r\n" + 
                                                "        AND t.id_tipo_campione = tc.id_tipo_campione (+)\r\n" + 
                                                "        AND t.data_fine_validita IS NULL \r\n" +
                                                "        AND o.data_fine_validita IS NULL \r\n" +
                                                "        AND s.data_fine_validita IS NULL \r\n" +
                                                "        AND tc.data_fine_validita IS NULL \r\n" +
                                                "        GROUP BY t.id_specie_vegetale,s.genere_specie,s.nome_volgare,s.codice_eppo,t.id_on_da_monitorare,\r\n" + 
                                                "                 o.sigla,t.id_tipo_campione,tc.tipologia_campione\r\n" + 
                                                "        )\r\n" + 
                                                "GROUP BY id_specie_vegetale,nome_comune_specie,nome_latino_specie,codice_eppo,id_tipo_campione,desc_tipo_campione,mesi)\r\n" + 
                                                "WHERE id_specie_vegetale = :idSpecieVegetale \r\n" +
                                                "AND NVL(id_tipo_campione,0) = NVL(:idTipoCampione,0) \r\n" +
                                                "AND mesi = :mesi";

  public RiepilogoMonitoraggioDAO() {  }

  
  public RiepilogoMonitoraggio insert(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insert]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    final String INSERT = "INSERT INTO iuf_r_specie_on_periodo(id_specie_on_periodo,id_specie_vegetale,id_on_da_monitorare,id_tipo_campione,\r\n" +
                              "mese_di_monitoraggio,data_inizio_validita,data_fine_validita,ext_id_utente_aggiornamento,data_ultimo_aggiornamento) \r\n" +
                          "VALUES(seq_iuf_r_specie_on_periodo.nextval,:idSpecieVegetale,:idOrganismoNocivo,:idTipoCampione,\r\n" +
                              ":mese,SYSDATE,NULL,:ext_id_utente_aggiornamento,SYSDATE)";
    try  {
      mapParameterSource.addValue("idSpecieVegetale", riepilogo.getIdSpecieVegetale());
      mapParameterSource.addValue("idOrganismoNocivo", riepilogo.getIdOrganismoNocivo());
      mapParameterSource.addValue("idTipoCampione", riepilogo.getIdTipoCampione());
      mapParameterSource.addValue("mese", riepilogo.getMese());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", riepilogo.getExtIdUtenteAggiornamento());
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_specie_on_periodo"});
      riepilogo.setIdSpecieOnPeriodo(holder.getKey().intValue());
      return riepilogo;
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

  public void update(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieOnPeriodo", riepilogo.getIdSpecieOnPeriodo());
    mapParameterSource.addValue("idTipoCampione", riepilogo.getIdTipoCampione());
    mapParameterSource.addValue("ext_id_utente_aggiornamento", riepilogo.getExtIdUtenteAggiornamento());

    final String UPDATE = "UPDATE iuf_r_specie_on_periodo \r\n" +
                          "SET id_tipo_campione = :idTipoCampione,\r\n" +
                          "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento,\r\n" +
                          "data_ultimo_aggiornamento = SYSDATE \r\n" +
                          "WHERE id_specie_on_periodo = :idSpecieOnPeriodo";
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

  public void storicizza(RiepilogoMonitoraggio riepilogo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "storicizza";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieOnPeriodo", riepilogo.getIdSpecieOnPeriodo());
    mapParameterSource.addValue("ext_id_utente_aggiornamento", riepilogo.getExtIdUtenteAggiornamento());

    final String UPDATE = "UPDATE iuf_r_specie_on_periodo \r\n" +
                          "SET data_fine_validita = SYSDATE,\r\n" +
                              "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento,\r\n" +
                              "data_ultimo_aggiornamento = SYSDATE \r\n" +
                          "WHERE id_specie_on_periodo = :idSpecieOnPeriodo";
    try
    {
      logger.debug(UPDATE);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      logger.debug("UPDATE EFFETTUATO");
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
  
  public void removeMesi(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, String mesi, Long idUtente) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "removeMesi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
    mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
    mapParameterSource.addValue("idUtente", idUtente);
    final String DELETE = String.format("UPDATE iuf_r_specie_on_periodo SET data_fine_validita = SYSDATE,\r\n" +
                                        " ext_id_utente_aggiornamento = :idUtente,\r\n" +
                                        " data_ultimo_aggiornamento = SYSDATE \r\n" +
                                        "WHERE id_specie_vegetale = :idSpecieVegetale " +
                                        "AND NVL(id_tipo_campione,0) = NVL(:idTipoCampione,0) \r\n" +
                                        "AND id_on_da_monitorare = :idOrganismoNocivo \r\n" +
                                        "AND data_fine_validita IS NULL \r\n" +
                                        "AND mese_di_monitoraggio IN (%s)", mesi);
    
    try
    {
      logger.debug(DELETE);
      int recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug(THIS_METHOD + " - record storicizzati: " + recs);
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

  public void removeOrganismoNocivo(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, Long idUtente) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "removeOrganismoNocivo";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
    mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
    mapParameterSource.addValue("idUtente", idUtente);

    final String DELETE = String.format("UPDATE iuf_r_specie_on_periodo \r\n" +
                                          "SET data_fine_validita = SYSDATE,\r\n" +
                                          " ext_id_utente_aggiornamento = :idUtente,\r\n" +
                                          " data_ultimo_aggiornamento = SYSDATE \r\n" +
                                        "WHERE id_specie_vegetale = :idSpecieVegetale \r\n" +
                                        "AND NVL(id_tipo_campione,0) = NVL(:idTipoCampione,0) \r\n" +
                                        "AND data_fine_validita IS NULL \r\n" +
                                        "AND id_on_da_monitorare = :idOrganismoNocivo");
    try
    {
      logger.debug(DELETE);
      int recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug(THIS_METHOD + " - record storicizzati: " + recs);
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
  
  public void remove(Integer idSpecieVegetale, Integer idTipoCampione, String mesi, String on, Long idUtente) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
    mapParameterSource.addValue("idUtente", idUtente);

    final String DELETE = String.format("UPDATE iuf_r_specie_on_periodo \r\n" +
                                          "SET data_fine_validita = SYSDATE,\r\n" +
                                          " ext_id_utente_aggiornamento = :idUtente,\r\n" +
                                          " data_ultimo_aggiornamento = SYSDATE \r\n" +
                                        "WHERE id_specie_vegetale = :idSpecieVegetale \r\n" +
                                        "AND NVL(id_tipo_campione,0) = NVL(:idTipoCampione,0) \r\n" +
                                        "AND data_fine_validita IS NULL \r\n" +
                                        "AND id_on_da_monitorare IN (" + on + ") \r\n" +
                                        "AND mese_di_monitoraggio IN (%s)", mesi);
    try
    {
      logger.debug(DELETE);
      int recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug(THIS_METHOD + " - record storicizzati: " + recs);
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
  
  public List<RiepilogoMonitoraggioDTO> findAll() throws InternalUnexpectedException
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
      //List<RiepilogoMonitoraggioDTO> list = queryForList(SELECT_ALL, mapParameterSource, RiepilogoMonitoraggioDTO.class);
      List<RiepilogoMonitoraggioDTO> list = namedParameterJdbcTemplate.query(SELECT_ALL, mapParameterSource, getRowMapper());
      
      logger.debug(THIS_METHOD + " record : " + list.size());
      return list;
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

  public RiepilogoMonitoraggioDTO findBySpecieVegetaleTipoCampioneAndMesi(Integer idSpecieVegetale, Integer idTipoCampione, String mesi) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findBySpecieVegetaleTipoCampioneAndMesi";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
    mapParameterSource.addValue("mesi", mesi);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      RiepilogoMonitoraggioDTO riepilogo = null;
      List<RiepilogoMonitoraggioDTO> list = namedParameterJdbcTemplate.query(SELECT_BY_ID, mapParameterSource, getRowMapper());
      if (list != null && list.size() > 0)
        riepilogo = list.get(0);
      return riepilogo;
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

  public List<RiepilogoMonitoraggio> findByIdOrganismoNocivo(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdOrganismoNocivo";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
    final String SELECT = "SELECT * FROM iuf_r_specie_on_periodo r WHERE id_on_da_monitorare = :idOrganismoNocivo AND data_fine_validita IS NULL";
    try
    {
      logger.debug(SELECT);
      logger.debug("idOrganismoNocivo="+idOrganismoNocivo);
      List<RiepilogoMonitoraggio> list = queryForList(SELECT, mapParameterSource, RiepilogoMonitoraggio.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
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
  
  public List<RiepilogoMonitoraggio> findByIdSpecieVegetale(Integer idSpecieVegetale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdSpecieVegetale";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
    final String SELECT = "SELECT * FROM iuf_r_specie_on_periodo r WHERE id_specie_vegetale = :idSpecieVegetale AND data_fine_validita IS NULL";
    try
    {
      logger.debug(SELECT);
      logger.debug("idSpecieVegetale="+idSpecieVegetale);
      List<RiepilogoMonitoraggio> list = queryForList(SELECT, mapParameterSource, RiepilogoMonitoraggio.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
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

  public List<RiepilogoMonitoraggio> findByIdTipoCampione(Integer idTipoCampione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdTipoCampione";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
    final String SELECT = "SELECT * FROM iuf_r_specie_on_periodo r WHERE id_tipo_campione = :idTipoCampione AND data_fine_validita IS NULL";
    try
    {
      logger.debug(SELECT);
      logger.debug("idTipoCampione="+idTipoCampione);
      List<RiepilogoMonitoraggio> list = queryForList(SELECT, mapParameterSource, RiepilogoMonitoraggio.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
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

  public RiepilogoMonitoraggio findByUniqueKey(Integer idSpecieVegetale, Integer idTipoCampione, Integer idOrganismoNocivo, Integer mese) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByUniqueKey";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
    mapParameterSource.addValue("idTipoCampione", idTipoCampione);
    mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
    mapParameterSource.addValue("mese", mese);

    final String SELECT = "SELECT id_specie_on_periodo, \r\n" + 
                                "id_specie_vegetale, \r\n" + 
                                "id_on_da_monitorare id_organismo_nocivo, \r\n" + 
                                "id_tipo_campione, \r\n" + 
                                "mese_di_monitoraggio mese, \r\n" + 
                                "data_inizio_validita, \r\n" + 
                                "data_fine_validita, \r\n" + 
                                "ext_id_utente_aggiornamento, \r\n" + 
                                "data_ultimo_aggiornamento \r\n" + 
                          "FROM iuf_r_specie_on_periodo s \r\n" +
                          "WHERE s.id_specie_vegetale = :idSpecieVegetale \r\n" +
                          "AND s.id_on_da_monitorare = :idOrganismoNocivo \r\n" +
                          "AND NVL(s.id_tipo_campione,0) = NVL(:idTipoCampione,0) \r\n" +
                          "AND s.data_fine_validita IS NULL \r\n" +
                          "AND s.mese_di_monitoraggio = :mese";
    try
    {
      logger.debug(SELECT);
      RiepilogoMonitoraggio riepilogo = queryForObject(SELECT, mapParameterSource, RiepilogoMonitoraggio.class);
      return riepilogo;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
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

  public Long getMaxCountON() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findMaxCountON";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    final String SELECT = "SELECT MAX(num_on)\r\n" + 
                            "FROM (\r\n" + 
                            "      SELECT COUNT(id_on_da_monitorare) num_on\r\n" + 
                            "        FROM (\r\n" + 
                            "              SELECT t.id_specie_vegetale,s.nome_volgare nome_comune_specie,s.genere_specie nome_latino_specie,t.id_tipo_campione,\r\n" + 
                            "                     tc.tipologia_campione desc_tipo_campione,\r\n" + 
                            "                     t.id_on_da_monitorare,o.sigla,\r\n" + 
                            "                     LISTAGG(t.mese_di_monitoraggio,',') WITHIN GROUP (ORDER BY t.mese_di_monitoraggio) mesi\r\n" + 
                            "              FROM iuf_r_specie_on_periodo t, iuf_d_organismo_nocivo o, iuf_d_specie_vegetale s, iuf_d_tipo_campione tc\r\n" + 
                            "              WHERE t.id_on_da_monitorare = o.id_organismo_nocivo\r\n" + 
                            "              AND t.id_specie_vegetale = s.id_specie_vegetale\r\n" + 
                            "              AND t.id_tipo_campione = tc.id_tipo_campione (+)\r\n" + 
                            "              AND SYSDATE BETWEEN t.data_inizio_validita AND NVL(t.data_fine_validita,SYSDATE+1) \r\n" + 
                            "              AND SYSDATE BETWEEN o.data_inizio_validita AND NVL(o.data_fine_validita,SYSDATE+1) \r\n" + 
                            "              AND SYSDATE BETWEEN s.data_inizio_validita AND NVL(s.data_fine_validita,SYSDATE+1) \r\n" + 
                            "              AND SYSDATE BETWEEN NVL(tc.data_inizio_validita,SYSDATE-1) AND NVL(tc.data_fine_validita,SYSDATE+1) \r\n" + 
                            "              GROUP BY t.id_specie_vegetale,s.genere_specie,s.nome_volgare,t.id_on_da_monitorare,\r\n" + 
                            "                       o.sigla,t.id_tipo_campione,tc.tipologia_campione\r\n" + 
                            "              )\r\n" + 
                            "      GROUP BY id_specie_vegetale,nome_comune_specie,nome_latino_specie,id_tipo_campione,desc_tipo_campione,mesi)";
    try
    {
      logger.debug(SELECT);
      Long maxON = queryForLong(SELECT, mapParameterSource);
      return maxON;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
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
  
  public List<RiepilogoMonitoraggio> getRiepilogoMonitoraggio() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRiepilogoMonitoraggio";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      //return namedParameterJdbcTemplate.query(SELECT_RIEPILOGO_MONITORAGGIO, mapParameterSource, getRowMapper());
      logger.debug(SELECT_RIEPILOGO_MONITORAGGIO);
      List<RiepilogoMonitoraggio> list = queryForList(SELECT_RIEPILOGO_MONITORAGGIO, mapParameterSource, RiepilogoMonitoraggio.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_RIEPILOGO_MONITORAGGIO, mapParameterSource);
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

  private RowMapper<RiepilogoMonitoraggioDTO> getRowMapper() {
    return new RowMapper<RiepilogoMonitoraggioDTO>() {
      
      @Override
      public RiepilogoMonitoraggioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RiepilogoMonitoraggioDTO riepMon = new RiepilogoMonitoraggioDTO();
        riepMon.setIdSpecieVegetale(rs.getInt("id_specie_vegetale"));
        riepMon.setNomeComuneSpecie(rs.getString("nome_comune_specie"));
        riepMon.setNomeLatinoSpecie(rs.getString("nome_latino_specie"));
        riepMon.setCodiceEppo(rs.getString("codice_eppo"));
        riepMon.setIdTipoCampione((rs.getString("id_tipo_campione")!=null)?rs.getInt("id_tipo_campione"):null);
        riepMon.setDescTipoCampione(rs.getString("desc_tipo_campione"));
        String mesi = rs.getString("mesi");
        riepMon.setMesiStr(mesi);
        String[] mesiArray = mesi.split(",");
        List<String> mesiList = Arrays.asList(mesiArray);
        riepMon.setGennaio((mesiList.contains("1"))?1:0);
        riepMon.setFebbraio((mesiList.contains("2"))?1:0);
        riepMon.setMarzo((mesiList.contains("3"))?1:0);
        riepMon.setAprile((mesiList.contains("4"))?1:0);
        riepMon.setMaggio((mesiList.contains("5"))?1:0);
        riepMon.setGiugno((mesiList.contains("6"))?1:0);
        riepMon.setLuglio((mesiList.contains("7"))?1:0);
        riepMon.setAgosto((mesiList.contains("8"))?1:0);
        riepMon.setSettembre((mesiList.contains("9"))?1:0);
        riepMon.setOttobre((mesiList.contains("10"))?1:0);
        riepMon.setNovembre((mesiList.contains("11"))?1:0);
        riepMon.setDicembre((mesiList.contains("12"))?1:0);
        String s = rs.getString("organismi_nocivi");
        riepMon.setOrganismiNociviStr(s);
        OrganismoNocivoDTO dto = null;
        List<OrganismoNocivoDTO> list = new ArrayList<OrganismoNocivoDTO>();
        if (s.length() > 1) {
          String[] array = s.split(";");
          for (String o : array) {
            String[] on = o.split(",");
            int id = Integer.decode(on[0]);
            String sigla = on[1];
            dto = new OrganismoNocivoDTO(id, sigla);
            list.add(dto);
          }
        }
        riepMon.setOrganismiNocivi(list);
        return riepMon;
      }
    };
  }

}
