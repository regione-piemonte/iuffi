package it.csi.iuffi.iuffiweb.integration;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.gestioneeventi.EventiDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public class GestioneEventiDAO extends BaseDAO
{

  private static final String THIS_CLASS = GestioneEventiDAO.class.getSimpleName();

  public GestioneEventiDAO()
  {
  }

  public List<EventiDTO> getListEventoCalamitoso() throws InternalUnexpectedException
  {
		return getListEventoCalamitoso(null);
  }
  
  

	public List<EventiDTO> getListEventoCalamitoso(long[] arrayIdEventoCalamitoso) throws InternalUnexpectedException {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
		if (logger.isDebugEnabled())
		{
		  logger.debug(THIS_METHOD + " BEGIN.");
		}
		String inConditionEventoCalamitoso = " ";
		if(arrayIdEventoCalamitoso != null)
		{
			inConditionEventoCalamitoso = getInCondition("ID_EVENTO_CALAMITOSO", arrayIdEventoCalamitoso);
		}
		final String QUERY = 
				"SELECT 														\r\n" + 
				"    EC.ID_EVENTO_CALAMITOSO,									\r\n" + 
				"    EC.DATA_EVENTO,											\r\n" + 
				"    EC.DESCRIZIONE AS DESC_EVENTO,								\r\n" + 
				"    EC.ID_CATEGORIA_EVENTO,									\r\n" + 
				"    CE.DESCRIZIONE AS DESC_CATEGORIA_EVENTO					\r\n" + 
				"FROM 															\r\n" + 
				"    IUF_D_EVENTO_CALAMITOSO EC,								\r\n" + 
				"    IUF_D_CATEGORIA_EVENTO CE								\r\n" + 
				"WHERE 															\r\n" + 
				"    EC.ID_CATEGORIA_EVENTO = CE.ID_CATEGORIA_EVENTO			\r\n" +
					inConditionEventoCalamitoso +
				"ORDER BY \r\n" +
				"	 EC.DATA_EVENTO, EC.DESCRIZIONE " ;
				;
		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		try
		{
		  return queryForList(QUERY, mapParameterSource, EventiDTO.class);
		}
		catch (Throwable t)
		{
		  InternalUnexpectedException e = new InternalUnexpectedException(t,
		      new LogParameter[]
		      {
		      }, new LogVariable[]
		      {}, QUERY, mapParameterSource);
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
	
	public List<DecodificaDTO<Long>> getListEventoCalamitoso(long idCategoriaEvento) throws InternalUnexpectedException 
	{
		return getListEventoCalamitoso(idCategoriaEvento,null);
	}
	
  public List<DecodificaDTO<Long>> getListEventoCalamitoso(long idCategoriaEvento, long[] arrayIdEventoCalamitoso) throws InternalUnexpectedException {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    String inConditionIdEventoCalamitoso = " ";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    if(arrayIdEventoCalamitoso != null)
	    {
	    	inConditionIdEventoCalamitoso = getInCondition("ID_EVENTO_CALAMITOSO", arrayIdEventoCalamitoso);
	    }
		final String QUERY = 
				"SELECT 															\r\n" + 
				"    EC.ID_EVENTO_CALAMITOSO AS ID,									\r\n" + 
				"    EC.ID_EVENTO_CALAMITOSO AS CODICE,								\r\n" + 
				"    'Data evento: ' || EC.DATA_EVENTO || ' - ' || EC.DESCRIZIONE AS DESCRIZIONE,									\r\n" +
				"	 EC.DESCRIZIONE AS INFO_AGGIUNTIVA, \r\n" +
				"    EC.ID_CATEGORIA_EVENTO											\r\n" + 
				"FROM 																\r\n" + 
				"    IUF_D_EVENTO_CALAMITOSO EC									\r\n" + 
				"WHERE 																\r\n" + 
				"    EC.ID_CATEGORIA_EVENTO = :ID_CATEGORIA_EVENTO					\r\n" + 
					 inConditionIdEventoCalamitoso + 
				"ORDER BY															\r\n" + 
				"    EC.DATA_EVENTO, EC.DESCRIZIONE"
				;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	    	mapParameterSource.addValue("ID_CATEGORIA_EVENTO", idCategoriaEvento);
	    	return queryForDecodificaLong(QUERY, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	             new LogParameter("ID_CATEGORIA_EVENTO",idCategoriaEvento)
	          }, new LogVariable[]
	          {}, QUERY, mapParameterSource);
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
  
  public List<DecodificaDTO<Long>> getListDecodificaCategorieEvento() throws InternalUnexpectedException
  {
	  return getListDecodificaCategorieEvento(null);
  }
	
  public List<DecodificaDTO<Long>> getListDecodificaCategorieEvento(long[] arrayIdCategoriaEvento) throws InternalUnexpectedException
  {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
		final String QUERY = 
				"SELECT 														\r\n" + 
				"    CE.ID_CATEGORIA_EVENTO AS ID,								\r\n" + 
				"    CE.ID_CATEGORIA_EVENTO AS CODICE,							\r\n" + 
				"    CE.DESCRIZIONE AS DESCRIZIONE								\r\n" + 
				"FROM 															\r\n" + 
				"    IUF_D_CATEGORIA_EVENTO CE								\r\n" + 
				"ORDER BY \r\n" +
				"	 CE.DESCRIZIONE " ;
				;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      return queryForDecodificaLong(QUERY, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	          }, new LogVariable[]
	          {}, QUERY, mapParameterSource);
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

public long inserisciEvento(EventiDTO evento) 
	throws InternalUnexpectedException
{
	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT =
    		"INSERT INTO  IUF_D_EVENTO_CALAMITOSO									\r\n" + 
    		"    (																	\r\n" + 
    		"        ID_EVENTO_CALAMITOSO,											\r\n" + 
    		"        DESCRIZIONE,													\r\n" + 
    		"        DATA_EVENTO,													\r\n" + 
    		"        ID_CATEGORIA_EVENTO											\r\n" + 
    		"    )																	\r\n" + 
    		"VALUES 																\r\n" + 
    		"    (																	\r\n" + 
    		"        :ID_EVENTO_CALAMITOSO,											\r\n" + 
    		"        :DESCRIZIONE,													\r\n" + 
    		"        :DATA_EVENTO,													\r\n" + 
    		"        :ID_CATEGORIA_EVENTO   										\r\n" + 
    		"    )"
			;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiDEventoCalamitoso = 0;
    try
    {
      seqIuffiDEventoCalamitoso = getNextSequenceValue("SEQ_IUF_D_EVENTO_CALAMITOSO");
      mapParameterSource.addValue("ID_EVENTO_CALAMITOSO", seqIuffiDEventoCalamitoso, Types.NUMERIC);
      mapParameterSource.addValue("DATA_EVENTO", evento.getDataEvento(), Types.DATE);
      mapParameterSource.addValue("DESCRIZIONE", evento.getDescEvento(), Types.VARCHAR);
      mapParameterSource.addValue("ID_CATEGORIA_EVENTO", evento.getIdCategoriaEvento(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_EVENTO_CALAMITOSO", seqIuffiDEventoCalamitoso),
              new LogParameter("DATA_EVENTO", evento.getDataEvento()),
              new LogParameter("DESCRIZIONE", evento.getDescEvento()),
              new LogParameter("ID_CATEGORIA_EVENTO", evento.getIdCategoriaEvento())
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
    return seqIuffiDEventoCalamitoso;	
}

	public long lockEventoCalamitoso(long idEventoCalamitoso)
	{
		final String QUERY = " SELECT 1 FROM IUF_D_EVENTO_CALAMITOSO WHERE ID_EVENTO_CALAMITOSO = :ID_EVENTO_CALAMITOSO FOR UPDATE ";
		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("ID_EVENTO_CALAMITOSO", idEventoCalamitoso, Types.NUMERIC);
		return namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
	}

public long getNBandiAssociatiAdEvento(long idEventoCalamitoso) throws InternalUnexpectedException
{
	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
	final String QUERY = 
			"SELECT \r\n" + 
			"    COUNT(*) AS N\r\n" + 
			"FROM \r\n" + 
			"    IUF_D_BANDO B\r\n" + 
			"WHERE\r\n" + 
			"    B.ID_EVENTO_CALAMITOSO = :ID_EVENTO_CALAMITOSO"
			;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
    	mapParameterSource.addValue("ID_EVENTO_CALAMITOSO", idEventoCalamitoso, Types.NUMERIC);
    	return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
        		  new LogParameter("ID_EVENTO_CALAMITOSO", idEventoCalamitoso)
          }, new LogVariable[]
          {}, QUERY, mapParameterSource);
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
	public long updateEventoCalamitoso(EventiDTO evento) throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String INSERT =
	    		"UPDATE IUF_D_EVENTO_CALAMITOSO									\r\n" + 
	    		"    SET																	\r\n" + 
	    		"        DESCRIZIONE = :DESCRIZIONE,									\r\n" + 
	    		"        DATA_EVENTO = :DATA_EVENTO									\r\n" +
	    		" WHERE " +
	    		" ID_EVENTO_CALAMITOSO = :ID_EVENTO_CALAMITOSO"
	    		;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_EVENTO_CALAMITOSO", evento.getIdEventoCalamitoso(), Types.NUMERIC);
	      mapParameterSource.addValue("DATA_EVENTO", evento.getDataEvento(), Types.DATE);
	      mapParameterSource.addValue("DESCRIZIONE", evento.getDescEvento(), Types.VARCHAR);
	      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_EVENTO_CALAMITOSO", evento.getIdEventoCalamitoso()),
	              new LogParameter("DATA_EVENTO", evento.getDataEvento()),
	              new LogParameter("DESCRIZIONE", evento.getDescEvento())
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

	public long eliminaEventoCalamitoso(long idEventoCalamitoso) 
			throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
		final String DELETE =
				"DELETE 														\r\n" + 
				"FROM 															\r\n" + 
				"    IUF_D_EVENTO_CALAMITOSO									\r\n" + 
				"WHERE 															\r\n" + 
				"    ID_EVENTO_CALAMITOSO = :ID_EVENTO_CALAMITOSO				\r\n"
				;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_EVENTO_CALAMITOSO", idEventoCalamitoso, Types.NUMERIC);
	      return namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_EVENTO_CALAMITOSO", idEventoCalamitoso)
	          },
	          new LogVariable[]
	          {}, DELETE, mapParameterSource);
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