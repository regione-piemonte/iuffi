package it.csi.iuffi.iuffiweb.service;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.AutorizzazioniDTO;

public class AutorizzazioniDAO extends BaseDAO
{
  private static final String THIS_CLASS = AutorizzazioniDAO.class.getSimpleName();

  public AutorizzazioniDAO(){  }
  
  public List<AutorizzazioniDTO> findById(long id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT * FROM IUF_R_DISABILITA_CDU WHERE EXT_ID_LIVELLO = :id";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      return queryForList(SELECT_BY_ID, mapParameterSource, AutorizzazioniDTO.class);
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
  
  public List<AutorizzazioniDTO> findByIdLivelloAndReadOnly(long idLivello) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdLivelloAndReadOnly";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT * FROM IUF_R_DISABILITA_CDU WHERE EXT_ID_LIVELLO = :idLivello \r\n" +
                          "UNION \r\n" +
                          "SELECT :idLivello, e.ext_cod_macro_cdu \r\n" +
                          "  FROM iuf_d_elenco_cdu e \r\n" +
                          " WHERE e.tipo_azione = 'W'";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idLivello", idLivello);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      return queryForList(SELECT_BY_ID, mapParameterSource, AutorizzazioniDTO.class);
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
