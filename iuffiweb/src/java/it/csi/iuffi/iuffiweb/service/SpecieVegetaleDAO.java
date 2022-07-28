package it.csi.iuffi.iuffiweb.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.api.GenereSpecieVegetale;
import it.csi.iuffi.iuffiweb.model.api.SpecieVegetale;

public class SpecieVegetaleDAO extends BaseDAO
{

  
  private static final String THIS_CLASS = SpecieVegetaleDAO.class.getSimpleName();

  private static final String SELECT_ALL_ID_MULTIPLI = "select * from IUF_D_SPECIE_VEGETALE t where ID_SPECIE_VEGETALE in(%s)";

  private static final String SELECT_VALIDI = "SELECT * FROM iuf_d_specie_vegetale t WHERE t.data_fine_validita IS NULL ORDER BY UPPER(t.nome_volgare),UPPER(t.genere_specie)";

  public SpecieVegetaleDAO(){  }

 
  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public SpecieVegetaleDTO insertSpecieVegetale(SpecieVegetaleDTO specie) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertSpecieVegetale]";
    KeyHolder holder = new GeneratedKeyHolder();
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    String INSERT = "INSERT INTO IUF_D_SPECIE_VEGETALE(id_specie_vegetale,genere_specie,nome_volgare,codice_eppo," + 
                              "data_inizio_validita,data_fine_validita,flag_euro,ext_id_utente_aggiornamento,data_ultimo_aggiornamento) " +
                    "VALUES(SEQ_IUF_D_SPECIE_VEGETALE.NEXTVAL," +
                              "UPPER(:GENERE_SPECIE),UPPER(:NOME_VOLGARE),:codiceEppo,SYSDATE,null,'N',:ext_id_utente_aggiornamento,SYSDATE)";
    
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      mapParameterSource.addValue("GENERE_SPECIE", specie.getGenereSpecie() ,Types.VARCHAR);
      mapParameterSource.addValue("NOME_VOLGARE", specie.getNomeVolgare(),Types.VARCHAR);
      mapParameterSource.addValue("codiceEppo", specie.getCodiceEppo(),Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento",specie.getExtIdUtenteAggiornamento());
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource,holder, new String[] {"ID_SPECIE_VEGETALE"});
      specie.setIdSpecieVegetale(holder.getKey().intValue());
      return specie;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {
              
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

  public List<SpecieVegetaleDTO> listSpecieVegetali() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getSpescieVegetaliList";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    
    String QUERY = "SELECT t.id_specie_vegetale," + 
                        "t.genere_specie," + 
                        "t.nome_volgare,t.codice_eppo," +
                        "t.data_inizio_validita,t.data_fine_validita " + 
                    "FROM iuf_d_specie_vegetale t "+
                    " ORDER BY t.genere_specie ";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(QUERY, mapParameterSource, SpecieVegetaleDTO.class);
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
  
  public List<SpecieVegetaleDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT_ALL = "SELECT t.id_specie_vegetale," + 
                              "t.genere_specie," + 
                              "t.nome_volgare,t.codice_eppo," +
                              "t.data_inizio_validita,t.data_fine_validita, t.flag_euro, " + 
                              "CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato "+
                              "FROM IUF_D_SPECIE_VEGETALE t " +
                              "ORDER BY t.nome_volgare, t.genere_specie, t.data_inizio_validita desc ";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, SpecieVegetaleDTO.class);
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

  public List<SpecieVegetaleDTO> findByFilter(SpecieVegetaleDTO specieVegetale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT_BY_FILTER = "SELECT * FROM IUF_D_SPECIE_VEGETALE " +
        "WHERE ID_SPECIE_VEGETALE is not null ";
          if(specieVegetale.getGenereSpecie()!=null && !specieVegetale.getGenereSpecie().equals(""))
            SELECT_BY_FILTER += " and UPPER(genere_specie) LIKE UPPER(:genereSpecie) ";
          if(specieVegetale.getNomeVolgare()!=null && !specieVegetale.getNomeVolgare().equals(""))
            SELECT_BY_FILTER += " and UPPER(nome_volgare) LIKE UPPER(:nomeVolgare) ";
          if(specieVegetale.getCodiceEppo()!=null && !specieVegetale.getCodiceEppo().equals(""))
            SELECT_BY_FILTER += " and UPPER(codice_eppo) LIKE UPPER(:codiceEppo) ";
          if(specieVegetale.getDataInizioValidita()!=null)
           SELECT_BY_FILTER += " AND NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) >= NVL(:dataInizio,NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')))";
          if(specieVegetale.getDataFineValidita()!=null)
            SELECT_BY_FILTER += " AND NVL(data_fine_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) >= NVL(:dataFine,NVL(data_fine_validita,TO_DATE('01/01/1900','DD/MM/YYYY')))";
       
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("genereSpecie", "%"+specieVegetale.getGenereSpecie()+"%",Types.VARCHAR);
    mapParameterSource.addValue("nomeVolgare", "%"+specieVegetale.getNomeVolgare()+"%",Types.VARCHAR);
    mapParameterSource.addValue("codiceEppo", "%"+specieVegetale.getCodiceEppo()+"%",Types.VARCHAR);
    mapParameterSource.addValue("dataInizio", specieVegetale.getDataInizioValidita(),Types.DATE);
    mapParameterSource.addValue("dataFine", specieVegetale.getDataFineValidita(),Types.DATE);
        
    try
    {
      List<SpecieVegetaleDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, SpecieVegetaleDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      logger.error(SELECT_BY_FILTER);
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

  public void remove(Integer idSpecieVegetale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
     String DELETE = "DELETE FROM IUF_D_SPECIE_VEGETALE WHERE id_specie_vegetale = :idSpecieVegetale";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
    
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
      //throw e;
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
  
  public void updateDataFineValidita(Integer idSpecieVegetale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE IUF_D_SPECIE_VEGETALE " +
                    "SET data_fine_validita = SYSDATE," +
                            "data_ultimo_aggiornamento = SYSDATE " +
                    " WHERE id_specie_vegetale = :idSpecieVegetale";

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
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

  public List<SpecieVegetaleDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<SpecieVegetaleDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, SpecieVegetaleDTO.class);
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

  public void update(SpecieVegetaleDTO specie) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
 
    String UPDATE = "UPDATE IUF_D_SPECIE_VEGETALE SET genere_specie = :GENERE_SPECIE, nome_volgare = :NOME_VOLGARE, codice_eppo = :CODICE_EPPO," +
        " ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE WHERE id_specie_vegetale = :ID_SPECIE_VEGETALE";
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("GENERE_SPECIE", specie.getGenereSpecie() ,Types.VARCHAR);
    mapParameterSource.addValue("NOME_VOLGARE", specie.getNomeVolgare(),Types.VARCHAR);
    mapParameterSource.addValue("CODICE_EPPO", specie.getCodiceEppo(),Types.VARCHAR);
    mapParameterSource.addValue("ID_SPECIE_VEGETALE", specie.getIdSpecieVegetale());
    mapParameterSource.addValue("ext_id_utente_aggiornamento",specie.getExtIdUtenteAggiornamento());

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

  public SpecieVegetaleDTO findById(Integer idSpecieVegetale) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT_BY_ID = "SELECT * FROM IUF_D_SPECIE_VEGETALE WHERE id_specie_vegetale = :idSpecieVegetale";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idSpecieVegetale", idSpecieVegetale);
    
    try
    {
      SpecieVegetaleDTO specieVegetale = queryForObject(SELECT_BY_ID, mapParameterSource, SpecieVegetaleDTO.class);
      return specieVegetale;
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

  public List<GenereSpecieVegetale> getSpecieVegetali() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getSpecieVegetali";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    /*
    String SELECT_SPECIE_VEGETALI = "SELECT MIN(sv.id_specie_vegetale) id_genere_specie,sv.codice_eppo,\r\n" + 
                                    "      sv.genere_specie,sv.flag_euro,\r\n" + 
                                    "      SUBSTRING(LISTAGG(sv.id_specie_vegetale||'#'||sv.nome_volgare||'#'||TO_CHAR(sv.data_inizio_validita,'DD-MM-YYYY')||'#'||TO_CHAR(sv.data_fine_validita,'DD-MM-YYYY'),'@') \r\n" + 
                                    "WITHIN GROUP(ORDER BY sv.nome_volgare) AS dettaglio_specie_vegetale \r\n" + 
                                    "FROM \r\n" + 
                                    "      iuf_d_specie_vegetale sv\r\n" + 
                                    "GROUP BY \r\n" + 
                                    "      sv.genere_specie,sv.flag_euro,sv.codice_eppo \r\n" + 
                                    "ORDER BY \r\n" + 
                                    "      sv.genere_specie,sv.flag_euro";
                                    */
    String SELECT_SPECIE_VEGETALI = "SELECT MIN(sv.id_specie_vegetale) id_genere_specie,sv.codice_eppo,\r\n" + 
        "      sv.genere_specie,sv.flag_euro,\r\n" + 
        "      rtrim(xmlagg(xmlelement(e,sv.id_specie_vegetale||'#'||sv.nome_volgare||'#'||TO_CHAR(sv.data_inizio_validita,'DD-MM-YYYY')||'#'||TO_CHAR(sv.data_fine_validita,'DD-MM-YYYY'),'@').extract('//text()') order by sv.nome_volgare).getclobval(),', ') dettaglio_specie_vegetale\r\n" + 
        "FROM \r\n" + 
        "      iuf_d_specie_vegetale sv\r\n" + 
        "GROUP BY \r\n" + 
        "      sv.genere_specie,sv.flag_euro,sv.codice_eppo\r\n" + 
        "ORDER BY \r\n" + 
        "      genere_specie,flag_euro";
    
    try
    {
      return namedParameterJdbcTemplate.query(SELECT_SPECIE_VEGETALI, mapParameterSource, getRowMapper());
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_SPECIE_VEGETALI, mapParameterSource);
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

  
  public List<SpecieVegetaleDTO> findByIdMultipli(String idMultipli) throws InternalUnexpectedException
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
      List<SpecieVegetaleDTO> list = queryForList(select, mapParameterSource, SpecieVegetaleDTO.class);
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

  private RowMapper<GenereSpecieVegetale> getRowMapper() {
    
    return new RowMapper<GenereSpecieVegetale>() {
      
      @Override
      public GenereSpecieVegetale mapRow(ResultSet rs, int rowNum) throws SQLException {
        GenereSpecieVegetale genereSpecieVegetale = new GenereSpecieVegetale();
        genereSpecieVegetale.setIdGenereSpecie(rs.getInt("id_genere_specie"));
        genereSpecieVegetale.setGenereSpecie(rs.getString("genere_specie"));
        genereSpecieVegetale.setCodiceEppo(rs.getString("codice_eppo"));
        genereSpecieVegetale.setFlagEuro(rs.getString("flag_euro"));
        String dettaglio = rs.getString("dettaglio_specie_vegetale");
        if (dettaglio != null) {
          String[] k = dettaglio.split("@");
          SpecieVegetale sv = null;
          List<SpecieVegetale> list = new ArrayList<SpecieVegetale>();
          for (int i=0; i<k.length; i++) {
            String[] rec = k[i].split("#");
            if (rec.length > 3)
              sv = new SpecieVegetale(Integer.decode(rec[0]), rec[1], rec[2], rec[3]);
            else
              sv = new SpecieVegetale(Integer.decode(rec[0]), rec[1], rec[2]);
            list.add(sv);
          }
          genereSpecieVegetale.setListaSpecieVegetali(list);
        }
        return genereSpecieVegetale;
      }
    };
  }

}