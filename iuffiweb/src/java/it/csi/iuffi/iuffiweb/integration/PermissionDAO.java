package it.csi.iuffi.iuffiweb.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.permission.UpdatePermissionProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException.ExceptionType;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class PermissionDAO extends BaseDAO
{
  private static final String THIS_CLASS = PermissionDAO.class.getSimpleName();

  public PermissionDAO()
  {
  }
  
  
  

  public UpdatePermissionProcedimentoOggetto canUpdateProcedimentoOggetto(
      long idProcedimentoOggetto, boolean throwException)
      throws IuffiPermissionException,
      InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::canUpdateProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                  \n"
        + "   PO.DATA_FINE AS PO_DATA_FINE,                                        \n"
        + "   CASE                                                                 \n"
        + "     WHEN SYSDATE BETWEEN BO.DATA_INIZIO AND NVL(BO.DATA_RITARDO, NVL(BO.DATA_FINE, SYSDATE)) \n"
        + "     THEN NULL                                                          \n"
        + "     ELSE BO.DATA_FINE                                                  \n"
        + "   END            AS BO_DATA_FINE,                                      \n"
        + "   BO.FLAG_ATTIVO AS BO_FLAG_ATTIVO,                                    \n"
        + "   BO.ID_BANDO_OGGETTO AS ID_BANDO_OGGETTO,                             \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       COUNT(*)                                                         \n"
        + "     FROM                                                               \n"
        + "       IUF_T_NOTIFICA N,												   \n"
        + "		  IUF_D_GRAVITA_NOTIFICA G                                         \n"
        + "     WHERE                                                              \n"
        + "       N.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                            \n"
        + "       AND N.ID_GRAVITA_NOTIFICA = G.ID_GRAVITA_NOTIFICA		           \n"
        + "       AND G.CODICE     = 'B'          						           \n"
        + "       AND (N.DATA_FINE  IS NULL OR N.DATA_FINE >SYSDATE)               \n"
        + "   ) AS COUNT_NOTIFICHE_BLOCCANTI,                                      \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       COUNT(*)                                                         \n"
        + "     FROM                                                               \n"
        + "       IUF_T_NOTIFICA N,												   \n"
        + "		  IUF_D_GRAVITA_NOTIFICA G                                         \n"
        + "     WHERE                                                              \n"
        + "       N.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                            \n"
        + "       AND N.ID_GRAVITA_NOTIFICA = G.ID_GRAVITA_NOTIFICA		           \n"
        + "       AND G.CODICE = 'G'                     						   \n"
        + "       AND (N.DATA_FINE  IS NULL OR N.DATA_FINE >SYSDATE)               \n"
        + "   ) AS COUNT_NOTIFICHE_GRAVI,	                                       \n"

        + "   O.FLAG_ISTANZA,	                                                   \n"
        + "   PO.EXT_COD_ATTORE                                                    \n"
        + " FROM                                                                   \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                       \n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                     \n"
        + "   IUF_R_BANDO_OGGETTO BO,                                              \n"
        + "   IUF_D_OGGETTO O,			                                           \n"

        + "   IUF_T_PROCEDIMENTO P                                                 \n"
        + " WHERE                                                                  \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO      = :ID_PROCEDIMENTO_OGGETTO           \n"
        + "   AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO       \n"
        + "   AND O.ID_OGGETTO = LGO.ID_OGGETTO       							   \n"

        + "   AND LGO.ID_LEGAME_GRUPPO_OGGETTO= BO.ID_LEGAME_GRUPPO_OGGETTO        \n"
        + "   AND PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO                  \n"
        + "   AND P.ID_BANDO                  = BO.ID_BANDO                        \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      UpdatePermissionProcedimentoOggetto status = namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<UpdatePermissionProcedimentoOggetto>()
              {

                @Override
                public UpdatePermissionProcedimentoOggetto extractData(
                    ResultSet rs) throws SQLException, DataAccessException
                {
                  UpdatePermissionProcedimentoOggetto status = null;
                  if (rs.next())
                  {
                    status = new UpdatePermissionProcedimentoOggetto();
                    status.setDataFineProcedimentoOggetto(
                        rs.getDate("PO_DATA_FINE"));
                    status.setDataFineBandoOggetto(rs.getDate("BO_DATA_FINE"));
                    status.setFlagBandoOggettoAttivo(
                        rs.getString("BO_FLAG_ATTIVO"));
                    status.setCountNotificheBloccanti(
                        rs.getInt("COUNT_NOTIFICHE_BLOCCANTI"));
                    status.setCountNotificheGravi(
                        rs.getInt("COUNT_NOTIFICHE_GRAVI"));
                    status.setExtCodAttore(rs.getString("EXT_COD_ATTORE"));
                    status.setFlagIstanza(rs.getString("FLAG_ISTANZA"));
                  }
                  return status;
                }
              });
      if (status != null && throwException)
      {
        if (status.getDataFineBandoOggetto() != null)
        {
          throw new IuffiPermissionException(
              ExceptionType.PROCEDIMENTO_CHIUSO);
        }
        if (status.getDataFineProcedimentoOggetto() != null)
        {
          throw new IuffiPermissionException(
              ExceptionType.PROCEDIMENTO_OGGETTO_CHIUSO);
        }
        if (!IuffiConstants.FLAGS.SI
            .equals(status.getFlagBandoOggettoAttivo()))
        {
          throw new IuffiPermissionException(
              ExceptionType.OGGETTO_BANDO_NON_ATTIVO);
        }
        if (status.getCountNotificheBloccanti() > 0)
        {
          if (status.getFlagIstanza() != null
              && status.getFlagIstanza().compareTo("N") == 0)
            throw new IuffiPermissionException(
                ExceptionType.NOTIFICHE_BLOCCANTI);
        }
        if (status.getCountNotificheGravi() > 0)
        {
          throw new IuffiPermissionException(ExceptionType.NOTIFICHE_GRAVI);
        }

      }
      else
      {
        if (throwException)
        {
          throw new IuffiPermissionException(
              ExceptionType.PROCEDIMENTO_OGGETTO_NON_TROVATO);
        }
      }
      return status;
    }
    catch (IuffiPermissionException p)
    {
      throw p;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("throwException", throwException)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public boolean hasDelega(long idIntermediario, long extIdAzienda,
      Date dataRiferimento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::hasDelega]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                                               \n"
        + "   1                                                                                                                  \n"
        + " FROM                                                                                                                 \n"
        + "   SMRGAA_V_DATI_DELEGA D                                                                                                \n"
        + " WHERE                                                                                                                \n"
        + "   D.ID_AZIENDA = :ID_AZIENDA                                                                                         \n"
        + "   AND                                                                                                                \n"
        + "   (                                                                                                                  \n"
        + "     D.ID_INTERMEDIARIO_ZONA    = :ID_INTERMEDIARIO                                                                   \n"
        + "     OR D.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO                                                                   \n"
        + "     OR D.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO                                                                   \n"
        + "   )                                                                                                                  \n"
        + "   AND NVL(:DATA_RIFERIMENTO, SYSDATE) BETWEEN D.DATA_INIZIO_VALIDITA AND NVL(D.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERMEDIARIO", idIntermediario,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_AZIENDA", extIdAzienda, Types.NUMERIC);
      mapParameterSource.addValue("DATA_RIFERIMENTO", dataRiferimento,
          Types.TIMESTAMP);
      boolean result = namedParameterJdbcTemplate.query(QUERY,
          mapParameterSource,
          new ResultSetExtractor<Boolean>()
          {

            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              return rs.next();
            }
          });
      return result;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntermediario", idIntermediario),
              new LogParameter("extIdAzienda", extIdAzienda),
              new LogParameter("dataRiferimento", dataRiferimento)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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

  public boolean hasDelegaForProcedimento(long idIntermediario,
      long idProcedimento, Date dataRiferimento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::hasDelega]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                                                 \n"
        + "   1                                                                                                                    \n"
        + " FROM                                                                                                                   \n"
        + "   SMRGAA_V_DATI_DELEGA D,                                                                                                 \n"
        + "   IUF_T_PROCEDIMENTO P,                                                                                                \n"
        + "   IUF_T_PROCEDIMENTO_AZIENDA PA                                                                                        \n"
        + " WHERE                                                                                                                  \n"
        + "   P.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                                                                 \n"
        + "   AND P.ID_PROCEDIMENTO = PA.ID_PROCEDIMENTO                                                                           \n"
        + "   AND NVL(:DATA_RIFERIMENTO, SYSDATE) BETWEEN PA.DATA_INIZIO AND NVL(PA.DATA_FINE, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
        + "   AND D.ID_AZIENDA = PA.EXT_ID_AZIENDA                                                                                 \n"
        + "   AND                                                                                                                  \n"
        + "   (                                                                                                                    \n"
        + "     D.ID_INTERMEDIARIO_ZONA    = :ID_INTERMEDIARIO                                                                     \n"
        + "     OR D.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO                                                                     \n"
        + "     OR D.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO                                                                     \n"
        + "   )                                                                                                                    \n"
        + "   AND NVL(:DATA_RIFERIMENTO, SYSDATE) BETWEEN D.DATA_INIZIO_VALIDITA AND NVL(D.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY'))   \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERMEDIARIO", idIntermediario,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_RIFERIMENTO", dataRiferimento,
          Types.TIMESTAMP);
      boolean result = namedParameterJdbcTemplate.query(QUERY,
          mapParameterSource,
          new ResultSetExtractor<Boolean>()
          {

            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              return rs.next();
            }
          });
      return result;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntermediario", idIntermediario),
              new LogParameter("idProcedimento", idProcedimento),
              new LogParameter("dataRiferimento", dataRiferimento)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD + "");
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
