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
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.EnteDTO;


public class AnagraficaDAO extends BaseDAO
{

  
  private static final String THIS_CLASS = AnagraficaDAO.class.getSimpleName();

  private static final String SELECT_ENTI = " SELECT * FROM IUF_D_ENTE t WHERE TRUNC(SYSDATE) BETWEEN TRUNC(data_inizio_validita) AND NVL(data_fine_validita,SYSDATE) ORDER BY t.denominazione";
 
  private static final String SELECT_ALL_ID_MULTIPLI = "select * from IUF_T_ANAGRAFICA t where ID_ANAGRAFICA in(%s)";

  private static final String SELECT_VALIDI = "select * from IUF_T_ANAGRAFICA t where T.data_fine_validita is null ORDER BY UPPER(COGNOME)";
  
  private static final String SELECT_BY_CF = "select id_anagrafica from IUF_T_ANAGRAFICA t where CF_ANAGRAFICA_EST = :cfAnagraficaEst and data_fine_validita is null";
  
  private static final String SELECT_ISPETTORI_MISSIONE = "SELECT a.* FROM iuf_t_anagrafica a, iuf_r_ispettore_aggiunto ia\r\n" + 
  		"WHERE ia.id_anagrafica = a.id_anagrafica \r\n" + 
  		"AND a.data_fine_validita is null \r\n" +
  		"AND ia.id_missione = :idMissione\r\n" + 
  		"union\r\n" + 
  		"SELECT b.* FROM iuf_t_anagrafica b, iuf_t_missione m\r\n" + 
  		"WHERE m.id_ispettore_assegnato = b.id_anagrafica\r\n" + 
  		"AND b.data_fine_validita is null \r\n" +
  		"AND m.id_missione = :idMissione";
  
  public AnagraficaDAO(){  }

  
  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public AnagraficaDTO insertAnagrafica(AnagraficaDTO anagrafica) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertAnagrafica]";
    KeyHolder holder = new GeneratedKeyHolder();
    
    String INSERT = "INSERT INTO iuf_t_anagrafica(id_anagrafica,nome,cognome,id_anagrafica_est," +
                          "cf_anagrafica_est,paga_oraria,subcontractor,id_ente," +
                          "data_inizio_validita,data_fine_validita,ext_id_utente_aggiornamento,data_ultimo_aggiornamento) " +
                    "VALUES(SEQ_IUF_T_ANAGRAFICA.NEXTVAL,UPPER(:nome),UPPER(:cognome),:idAnagraficaEst,UPPER(:cfAnagraficaEst)," +
                          ":pagaOraria,:subcontractor,:idEnte,sysdate,null,:ext_id_utente_aggiornamento,SYSDATE)";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      mapParameterSource.addValue("nome", anagrafica.getNome() ,Types.VARCHAR);
      mapParameterSource.addValue("cognome", anagrafica.getCognome(),Types.VARCHAR);
      mapParameterSource.addValue("idAnagraficaEst", anagrafica.getIdAnagraficaEst());
      mapParameterSource.addValue("cfAnagraficaEst",anagrafica.getCfAnagraficaEst(), Types.VARCHAR);
      mapParameterSource.addValue("pagaOraria",anagrafica.getPagaOraria(), Types.DOUBLE);
      mapParameterSource.addValue("subcontractor",anagrafica.getSubcontractor(), Types.VARCHAR);
      mapParameterSource.addValue("idEnte", anagrafica.getIdEnte());
      //mapParameterSource.addValue("dataInizioVal", anagrafica.getDataInizioValidita(), Types.DATE);
      //mapParameterSource.addValue("dataFineVal", anagrafica.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", anagrafica.getExtIdUtenteAggiornamento());
     
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource,holder, new String[] {"ID_ANAGRAFICA"});
      anagrafica.setIdAnagrafica(holder.getKey().intValue());
      return anagrafica;
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
  
  public EnteDTO insertEnte(EnteDTO ente) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertEnte]";
    KeyHolder holder = new GeneratedKeyHolder();
    
    String INSERT = "INSERT INTO iuf_d_ente(id_ente,denominazione,codice_est,data_inizio_validita,data_fine_validita) " +
                    "VALUES((SELECT NVL(MAX(id_ente),0)+1 FROM iuf_d_ente), :denominazione, :codiceEst, SYSDATE, NULL)";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      mapParameterSource.addValue("denominazione", ente.getDenominazione(), Types.VARCHAR);
      mapParameterSource.addValue("codiceEst", ente.getCodiceEst());
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_ente"});
      ente.setIdEnte(holder.getKey().intValue());
      return ente;
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
  
  public List<AnagraficaDTO> listAnagrafica() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "listAnagrafica";
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
      return queryForList(QUERY, mapParameterSource, AnagraficaDTO.class);
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
  
  public List<AnagraficaDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    String SELECT_ALL = "SELECT T.ID_ANAGRAFICA,T.NOME,T.COGNOME,T.CF_ANAGRAFICA_EST," +
                             "T.PAGA_ORARIA,T.SUBCONTRACTOR,E.ID_ENTE,DENOMINAZIONE AS ENTE_APPARTENENZA,t.data_inizio_validita,t.data_fine_validita," +
                             "CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato " +
                        "FROM IUF_T_ANAGRAFICA T,IUF_D_ENTE E " +
                        "WHERE T.ID_ENTE = E.ID_ENTE (+) " +
                        "ORDER BY UPPER(t.cognome), UPPER(t.nome),t.data_inizio_validita desc";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, AnagraficaDTO.class);
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
  
  public List<AnagraficaDTO> findByFilter(AnagraficaDTO anagrafica) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT_BY_FILTER = "SELECT T.ID_ANAGRAFICA,T.NOME,T.COGNOME,T.CF_ANAGRAFICA_EST," + 
                                    "T.PAGA_ORARIA,T.SUBCONTRACTOR,T.ID_ENTE,E.DENOMINAZIONE AS ENTE_APPARTENENZA " + 
                                "FROM IUF_T_ANAGRAFICA T,IUF_D_ENTE E " + 
                                "WHERE T.ID_ENTE = E.ID_ENTE (+) ";
    
        if(anagrafica.getNome()!=null && !anagrafica.getNome().equals(""))
           SELECT_BY_FILTER += " and UPPER(T.NOME) LIKE UPPER(:nome)";
        if(anagrafica.getCognome()!=null && !anagrafica.getCognome().equals(""))
          SELECT_BY_FILTER += " and UPPER(T.COGNOME) LIKE UPPER(:cognome)";
        if(anagrafica.getCfAnagraficaEst()!=null && !anagrafica.getCfAnagraficaEst().equals(""))
          SELECT_BY_FILTER += " and UPPER(T.CF_ANAGRAFICA_EST) LIKE UPPER(:cfAnagraficaEst)";
        if(anagrafica.getPagaOraria()!=null && anagrafica.getPagaOraria()>0)
          SELECT_BY_FILTER += " and T.PAGA_ORARIA =:paga";
        if(anagrafica.getIdEnte()!=null && anagrafica.getIdEnte()>0)
          SELECT_BY_FILTER += " and T.ID_ENTE =:idEnte";
        if(anagrafica.getSubcontractor()!=null && !anagrafica.getSubcontractor().equals(""))
          SELECT_BY_FILTER += " and T.SUBCONTRACTOR =:subcontractor";
        if (anagrafica.getActive() != null && anagrafica.getActive())
          SELECT_BY_FILTER += " AND NVL(t.data_fine_validita,SYSDATE+1) > SYSDATE";
      
        SELECT_BY_FILTER +=" ORDER BY COGNOME,NOME";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("nome", "%"+anagrafica.getNome()+"%", Types.VARCHAR);
    mapParameterSource.addValue("cognome", "%"+anagrafica.getCognome()+"%", Types.VARCHAR);
    mapParameterSource.addValue("cfAnagraficaEst","%"+ anagrafica.getCfAnagraficaEst()+"%", Types.VARCHAR);
    mapParameterSource.addValue("paga", anagrafica.getPagaOraria(), Types.DOUBLE);
    mapParameterSource.addValue("subcontractor", anagrafica.getSubcontractor(), Types.VARCHAR);
    mapParameterSource.addValue("idEnte", anagrafica.getIdEnte());
       
   try
    {
       logger.debug(SELECT_BY_FILTER);
       List<AnagraficaDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, AnagraficaDTO.class);
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

  public void remove(Integer idAnagrafica) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
     String DELETE = "DELETE FROM IUF_T_ANAGRAFICA WHERE id_anagrafica = :idAnagrafica";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idAnagrafica", idAnagrafica);
    
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
  
  public void update(AnagraficaDTO anagrafica) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
//    String INSERT = "INSERT INTO iuf_t_anagrafica(id_anagrafica,nome,cognome,id_anagrafica_est," +
//        "cf_anagrafica_est,paga_oraria,subcontractor,id_ente," +
//        "data_inizio_validita,data_fine_validita,ext_id_utente_aggiornamento,data_ultimo_aggiornamento) " +
//  "VALUES(SEQ_IUF_T_ANAGRAFICA.NEXTVAL,UPPER(:nome),UPPER(:cognome),:idAnagraficaEst,UPPER(:cfAnagraficaEst)," +
//        ":pagaOraria,:subcontractor,:idEnte,sysdate,null,:ext_id_utente_aggiornamento,SYSDATE)";
    
    String UPDATE = "UPDATE IUF_T_ANAGRAFICA SET nome = :nome, cognome = :cognome, id_anagrafica_est = :idAnagraficaEst, "+      
        "paga_oraria = :pagaOraria, subcontractor = :subcontractor, cf_anagrafica_est = :cfAnagraficaEst, id_ente = :idEnte, "+
        "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
        "WHERE id_anagrafica = :idAnagrafica";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("nome", anagrafica.getNome() ,Types.VARCHAR);
    mapParameterSource.addValue("cognome", anagrafica.getCognome(),Types.VARCHAR);
    mapParameterSource.addValue("idAnagraficaEst",anagrafica.getIdAnagraficaEst());
    mapParameterSource.addValue("pagaOraria",anagrafica.getPagaOraria(), Types.DOUBLE);
    mapParameterSource.addValue("subcontractor",anagrafica.getSubcontractor(), Types.VARCHAR);
    mapParameterSource.addValue("cfAnagraficaEst",anagrafica.getCfAnagraficaEst(), Types.VARCHAR);
    mapParameterSource.addValue("idEnte", anagrafica.getIdEnte());
    mapParameterSource.addValue("idAnagrafica", anagrafica.getIdAnagrafica());
    //mapParameterSource.addValue("dataInizioVal", anagrafica.getDataInizioValidita(), Types.DATE);
    //mapParameterSource.addValue("dataFineVal", anagrafica.getDataFineValidita(), Types.DATE);
    mapParameterSource.addValue("ext_id_utente_aggiornamento", anagrafica.getExtIdUtenteAggiornamento());

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
  
  public AnagraficaDTO findById(Integer idAnagrafica) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    
    String SELECT_BY_ID = "SELECT T.ID_ANAGRAFICA,T.NOME,T.COGNOME,T.CF_ANAGRAFICA_EST," +
                              "T.PAGA_ORARIA,T.SUBCONTRACTOR," +
                              "T.ID_ENTE,T.data_inizio_validita,T.data_fine_validita,E.DENOMINAZIONE AS ENTE_APPARTENENZA," +
                              "e.codice_est id_ente_papua " +
                          "FROM IUF_T_ANAGRAFICA T, IUF_D_ENTE E " +
                          "WHERE T.ID_ENTE = E.ID_ENTE (+) AND id_anagrafica = :idAnagrafica";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idAnagrafica", idAnagrafica);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      AnagraficaDTO anagrafica = queryForObject(SELECT_BY_ID, mapParameterSource, AnagraficaDTO.class);
      return anagrafica;
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

  public List<AnagraficaDTO> findByIdMultipli(String idMultipli) throws InternalUnexpectedException
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
      List<AnagraficaDTO> list = queryForList(select, mapParameterSource, AnagraficaDTO.class);
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

  
  public List<AnagraficaDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<AnagraficaDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, AnagraficaDTO.class);
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

  public void updateDataFineValidita(Integer idAnagrafica) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE IUF_T_ANAGRAFICA SET data_fine_validita = SYSDATE," +
    "data_ultimo_aggiornamento = SYSDATE " +
    "WHERE id_anagrafica = :idAnagrafica";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idAnagrafica", idAnagrafica);
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

  public List<EnteDTO> getEnti() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getEnti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ENTI);
      List<EnteDTO> list = queryForList(SELECT_ENTI, mapParameterSource, EnteDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ENTI, mapParameterSource);
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
  
  public EnteDTO findEnteById(Integer idEnte) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findEnteById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idEnte", idEnte);
    final String SELECT_ENTE_BY_ID = "SELECT * FROM iuf_d_ente WHERE id_ente = :idEnte";
    try
    {
      logger.debug(SELECT_ENTE_BY_ID);
      EnteDTO ente = queryForObject(SELECT_ENTE_BY_ID, mapParameterSource, EnteDTO.class);
      return ente;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ENTE_BY_ID, mapParameterSource);
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
  
  public EnteDTO findEnteByIdPapua(Integer idEntePapua) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findEnteByIdPapua";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idEntePapua", idEntePapua);
    final String SELECT_ENTE_BY_ID_PAPUA = "SELECT * FROM iuf_d_ente WHERE codice_est = :idEntePapua";
    try
    {
      logger.debug(SELECT_ENTE_BY_ID_PAPUA);
      EnteDTO ente = queryForObject(SELECT_ENTE_BY_ID_PAPUA, mapParameterSource, EnteDTO.class);
      return ente;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ENTE_BY_ID_PAPUA, mapParameterSource);
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
  
  public AnagraficaDTO getIdAnagraficaFromCf(String cf) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getIdAnagraficaFromCf";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("cfAnagraficaEst", cf, Types.VARCHAR);
    String select = String.format(SELECT_BY_CF,cf);
    try
    {
      AnagraficaDTO anagrafica = queryForObject(select, mapParameterSource, AnagraficaDTO.class);
      return anagrafica;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_CF, mapParameterSource);
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
  
  public List<AnagraficaDTO> findValidiMissione(Integer idMissione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidiMissione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    try
    {
      List<AnagraficaDTO> list = queryForList(SELECT_ISPETTORI_MISSIONE, mapParameterSource, AnagraficaDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ISPETTORI_MISSIONE, mapParameterSource);
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