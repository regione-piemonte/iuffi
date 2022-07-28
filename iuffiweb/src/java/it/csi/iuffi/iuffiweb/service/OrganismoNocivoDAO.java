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
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.util.DateUtils;

public class OrganismoNocivoDAO extends BaseDAO
{
  
  private static final String THIS_CLASS = OrganismoNocivoDAO.class.getSimpleName();

  private static final String INSERT = "INSERT INTO iuf_d_organismo_nocivo(id_organismo_nocivo, nome_latino, sigla, euro, flag_emergenza, data_inizio_validita, data_fine_validita,\r\n" +
                                                    "ext_id_utente_aggiornamento, data_ultimo_aggiornamento, flag_pianificazione) \r\n" + 
                                        "VALUES(seq_iuf_d_organismo_nocivo.nextval, UPPER(:nomeLatino), UPPER(:sigla), :euro,:flagEmergenza, sysdate, null, \r\n" + 
                                        ":ext_id_utente_aggiornamento, SYSDATE, :flagPianificazione)";

  private static final String UPDATE = "UPDATE iuf_d_organismo_nocivo SET nome_latino = UPPER(:nomeLatino), sigla = UPPER(:sigla), euro = :euro, flag_emergenza = :flagEmergenza,\r\n" +
                                          " data_fine_validita = :dataFineVal, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE,\r\n" +
                                          " flag_pianificazione = :flagPianificazione \r\n" +
                                        "WHERE id_organismo_nocivo = :idOrganismoNocivo";
  
/*  private static final String UPDATE_AND_CLOSE_NODE = "UPDATE iuf_d_organismo_nocivo SET " +
      " data_fine_validita = sysdate, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE \r\n" +
    "WHERE id_organismo_nocivo = :idOrganismoNocivo";
*/
  private static final String SELECT_VALIDI = "select * from iuf_d_organismo_nocivo t where t.data_fine_validita is null ORDER BY UPPER(nome_latino)";

  private static final String SELECT_BY_PIANIFICAZIONE = "SELECT * FROM iuf_d_organismo_nocivo t WHERE t.data_fine_validita IS NULL AND t.flag_pianificazione = 'S' ORDER BY UPPER(nome_latino)";

  private static final String SELECT_ALL = "SELECT t.*, CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato "+
                                           " FROM iuf_d_organismo_nocivo t ORDER BY UPPER(t.nome_latino),t.data_inizio_validita desc";
  
  private static final String SELECT_BY_FILTER = "SELECT * FROM iuf_d_organismo_nocivo \r\n" +
                                                  "WHERE nome_latino LIKE :nomeLatino \r\n" +
                                                  " %s \r\n" +
                                                  " AND NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY')) >= NVL(TO_DATE(:dataInizioVal,'DD/MM/YYYY'),NVL(data_inizio_validita,TO_DATE('01/01/1900','DD/MM/YYYY'))) \r\n" +
                                                  //" AND NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY')) <= NVL(:dataFineVal,NVL(data_fine_validita,TO_DATE('31/12/2100','DD/MM/YYYY'))) \r\n" +
                                                  "ORDER BY nome_latino";
  
  private static final String SELECT_BY_ID = "SELECT * FROM iuf_d_organismo_nocivo WHERE id_organismo_nocivo = :idOrganismoNocivo";

  private static final String DELETE = "DELETE FROM iuf_d_organismo_nocivo WHERE id_organismo_nocivo = :idOrganismoNocivo";

  private static final String SELECT_ALL_ID_MULTIPLI = "select * from iuf_d_organismo_nocivo t where id_organismo_nocivo in(%s)";
  
  public OrganismoNocivoDAO() {  }

  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public OrganismoNocivoDTO insertOrganismoNocivo(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertOrganismoNocivo]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {

      mapParameterSource.addValue("nomeLatino", organismoNocivo.getNomeLatino(), Types.VARCHAR);
      mapParameterSource.addValue("sigla", organismoNocivo.getSigla(), Types.VARCHAR);
      mapParameterSource.addValue("euro", organismoNocivo.getEuro(), Types.VARCHAR);
      mapParameterSource.addValue("flagEmergenza", organismoNocivo.getFlagEmergenza(), Types.VARCHAR);
      mapParameterSource.addValue("flagPianificazione", organismoNocivo.getFlagPianificazione(), Types.VARCHAR);
      //mapParameterSource.addValue("dataInizioVal", organismoNocivo.getDataInizioValidita(), Types.DATE);
      mapParameterSource.addValue("dataFineVal", organismoNocivo.getDataFineValidita(), Types.DATE);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", organismoNocivo.getExtIdUtenteAggiornamento());
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_organismo_nocivo"});
      organismoNocivo.setIdOrganismoNocivo(holder.getKey().intValue());
      return organismoNocivo;
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

  public List<OrganismoNocivoDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, OrganismoNocivoDTO.class);
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

  public List<OrganismoNocivoDTO> findByFilter(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("nomeLatino", organismoNocivo.getNomeLatino()+"%");
    mapParameterSource.addValue("sigla", organismoNocivo.getSigla()+"%");
    String select = null;
    if (organismoNocivo.getEuro() != null) {
      select = String.format(SELECT_BY_FILTER, "");
//      select = String.format(SELECT_BY_FILTER, "AND euro = :euro ");
//      mapParameterSource.addValue("euro", organismoNocivo.getEuro(), Types.VARCHAR);
    }
    else
      select = String.format(SELECT_BY_FILTER, "");
    
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_STRING);
    mapParameterSource.addValue("dataInizioVal", (organismoNocivo.getDataInizioValidita()!=null)?sdf.format(organismoNocivo.getDataInizioValidita()):"");
    mapParameterSource.addValue("dataFineVal", (organismoNocivo.getDataFineValidita()!=null)?sdf.format(organismoNocivo.getDataFineValidita()):"");
    try
    {
      List<OrganismoNocivoDTO> list = queryForList(select, mapParameterSource, OrganismoNocivoDTO.class);
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

  public List<OrganismoNocivoDTO> findValidi() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findValidi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<OrganismoNocivoDTO> list = queryForList(SELECT_VALIDI, mapParameterSource, OrganismoNocivoDTO.class);
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
  
  public List<OrganismoNocivoDTO> findByPianificazione() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByPianificazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      List<OrganismoNocivoDTO> list = queryForList(SELECT_BY_PIANIFICAZIONE, mapParameterSource, OrganismoNocivoDTO.class);
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

  public OrganismoNocivoDTO findById(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
    
    try
    {
      OrganismoNocivoDTO organismoNocivo = queryForObject(SELECT_BY_ID, mapParameterSource, OrganismoNocivoDTO.class);
      return organismoNocivo;
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

  public void remove(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
    
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
  
  public void updateDataFineValidita(Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    
    String UPDATE = "UPDATE iuf_d_organismo_nocivo SET data_fine_validita = SYSDATE," +
    "data_ultimo_aggiornamento = SYSDATE " +
    " WHERE id_organismo_nocivo = :idOrganismoNocivo";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
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

  public List<OrganismoNocivoDTO> findByIdMultipli(String idMultipli) throws InternalUnexpectedException
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
      List<OrganismoNocivoDTO> list = queryForList(select, mapParameterSource, OrganismoNocivoDTO.class);
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
  
  public void update(OrganismoNocivoDTO organismoNocivo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "update";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idOrganismoNocivo", organismoNocivo.getIdOrganismoNocivo());
    mapParameterSource.addValue("nomeLatino", organismoNocivo.getNomeLatino(), Types.VARCHAR);
    mapParameterSource.addValue("sigla", organismoNocivo.getSigla(), Types.VARCHAR);
    mapParameterSource.addValue("euro", organismoNocivo.getEuro(), Types.VARCHAR);
    mapParameterSource.addValue("flagEmergenza", organismoNocivo.getFlagEmergenza(), Types.VARCHAR);
    mapParameterSource.addValue("flagPianificazione", organismoNocivo.getFlagPianificazione(), Types.VARCHAR);
    //mapParameterSource.addValue("dataInizioVal", organismoNocivo.getDataInizioValidita(), Types.DATE);
    mapParameterSource.addValue("dataFineVal", organismoNocivo.getDataFineValidita(), Types.DATE);
    mapParameterSource.addValue("ext_id_utente_aggiornamento", organismoNocivo.getExtIdUtenteAggiornamento());
    
//    Boolean isAnnoCorrente = organismoNocivo.isAnnoCorrente();
    
    try
    {
//      namedParameterJdbcTemplate.update(isAnnoCorrente ? UPDATE : UPDATE_AND_CLOSE_NODE, mapParameterSource);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, UPDATE, mapParameterSource);
      //      {}, null, isAnnoCorrente ? UPDATE : UPDATE_AND_CLOSE_NODE, mapParameterSource);
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
