package it.csi.iuffi.iuffiweb.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.validator.GenericValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDualLIstDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DanniDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.AccertamentoSpeseDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DatiLocalizzazioneParticellarePerStampa;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DecodificaInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FileAllegatoInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaParticelle;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InfoMisurazioneIntervento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InfoRiduzione;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.MisurazioneDescrizioneInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.MisurazioneInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaInserimentoMultiploInterventiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONAllegatiInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONConduzioneInteventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONParticellaInteventoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaModificaMultiplaInterventiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.SuperficieConduzione;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RangePercentuale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RigaJSONInterventoQuadroEconomicoByLivelloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RigaJSONInterventoQuadroEconomicoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.ZonaAltimetricaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class InterventiBaseDAO extends BaseDAO
{
  private static final String THIS_CLASS = InterventiBaseDAO.class
      .getSimpleName();

  public InterventiBaseDAO()
  {
  }
  
  public List<DecodificaInterventoDTO> getListInterventiPossibiliPerDannoAtmByIdProcedimentoOggetto(
		  long idProcedimentoOggetto,
		  long idDannoAtm
		  ) throws InternalUnexpectedException
  {
	  return getListInterventiPossibiliByIdProcedimentoOggettoGenerico(idProcedimentoOggetto,idDannoAtm);
  }

  public List<DecodificaInterventoDTO> getListInterventiPossibiliByIdProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return getListInterventiPossibiliByIdProcedimentoOggettoGenerico(idProcedimentoOggetto,null);
  }

private List<DecodificaInterventoDTO> getListInterventiPossibiliByIdProcedimentoOggettoGenerico(
		long idProcedimentoOggetto,
		Long idDannoAtm
		) throws InternalUnexpectedException
{
	String THIS_METHOD = "[" + THIS_CLASS
        + "::getListInterventiPossibiliByIdProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = "  SELECT                                                                       \n"
		        + "         DI.ID_DESCRIZIONE_INTERVENTO,                                         \n"
		        + "         DI.DESCRIZIONE AS DESC_INTERVENTO,                                    \n"
		        + "         TA.DESCRIZIONE AS DESC_AGGREGAZIONE_1L,                               \n"
		        + "         LBI.ID_LIVELLO                                                        \n"
		        + "       FROM                                                                    \n"
		        + "         IUF_T_PROCEDIMENTO_OGGETTO PO,                                        \n"
		        + "         IUF_T_PROCEDIMENTO P,                                                 \n"
		        + "         IUF_R_LIV_BANDO_INTERVENTO LBI,                                       \n" //messo da conf - tra tutti gli interventi possibili associati al bnado
		        + "         IUF_D_DESCRIZIONE_INTERVENTO DI,                                      \n"
		        + "         IUF_R_AGGREGAZIONE_INTERVENT AI,                                     \n"
		        + "         IUF_D_TIPO_AGGREGAZIONE TA "
		        ;
        if(idDannoAtm != null)
        {
        		QUERY = QUERY
    			+ ", \n" 
        		+ "		IUF_R_DANNO_INTERVENTO DAIN, \n"
    			+ "		IUF_T_DANNO_ATM DA";
        }
		        QUERY = QUERY 
		        + "       WHERE                                                                   \n"
		        + "         PO.ID_PROCEDIMENTO                    = P.ID_PROCEDIMENTO             \n"
		        + "			AND (DI.CODICE_IDENTIFICATIVO NOT LIKE 'PREV' OR DI.CODICE_IDENTIFICATIVO IS NULL)\n"
		        + "         AND P.ID_BANDO                        = LBI.ID_BANDO                  \n"
		        + "         AND DI.ID_DESCRIZIONE_INTERVENTO      = LBI.ID_DESCRIZIONE_INTERVENTO \n"
		        + "         AND PO.ID_PROCEDIMENTO_OGGETTO        = :ID_PROCEDIMENTO_OGGETTO      \n"
		        + "         AND TRUNC(NVL(DI.DATA_CESSAZIONE,SYSDATE))   <= TRUNC(SYSDATE)        \n"
		        + "         AND DI.ID_DESCRIZIONE_INTERVENTO      = AI.ID_DESCRIZIONE_INTERVENTO  \n"
		        + "         AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE       \n"
		        ;
        if(idDannoAtm != null)
        {
        		QUERY = QUERY+	"         AND PO.ID_PROCEDIMENTO_OGGETTO        = DA.ID_PROCEDIMENTO_OGGETTO      \n"
        		+ "   	AND DI.ID_DESCRIZIONE_INTERVENTO = DAIN.ID_DESCRIZIONE_INTERVENTO  \n"  
        		+ "   	AND DAIN.ID_DANNO = DA.ID_DANNO										\n"  
        		+ "   	AND DA.ID_DANNO_ATM = :ID_DANNO_ATM									\n"
        		+ "		AND DI.ID_DESCRIZIONE_INTERVENTO NOT IN ( 							\r\n" +
        		"				SELECT 																			\r\n" + 
        		"   				I2.ID_DESCRIZIONE_INTERVENTO												\r\n" + 
        		"				FROM    																		\r\n" + 
        		"    				IUF_R_DANNO_ATM_INTERVENTO DAIN2,											\r\n" + 
        		"    				IUF_T_INTERVENTO I2,														\r\n" + 
        		"    				IUF_T_DANNO_ATM DA2														\r\n" + 
        		"				WHERE																			\r\n" + 
        		"        				DAIN2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO				\r\n" + 
        		"    				AND DAIN2.ID_INTERVENTO = I2.ID_INTERVENTO									\r\n" + 
        		"    				AND DAIN2.PROGRESSIVO = DA2.PROGRESSIVO										\r\n" + 
        		"    				AND DA2.ID_PROCEDIMENTO_OGGETTO = DAIN2.ID_PROCEDIMENTO_OGGETTO				\r\n" + 
        		"    				AND DA2.ID_DANNO_ATM = :ID_DANNO_ATM 										\r\n"	
        		+ " ) "
        		;
        }
		        QUERY = QUERY
		        + "       ORDER BY                                                                \n"
		        + "         DI.DESCRIZIONE                                                        \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DANNO_ATM",idDannoAtm, Types.NUMERIC);
      
      return (List<DecodificaInterventoDTO>) namedParameterJdbcTemplate.query(
          QUERY, mapParameterSource,
          new RowMapper<DecodificaInterventoDTO>()
          {
            @Override
            public DecodificaInterventoDTO mapRow(ResultSet rs, int arg1)
                throws SQLException
            {
              DecodificaInterventoDTO decodificaInterventoDTO = new DecodificaInterventoDTO();
              decodificaInterventoDTO
                  .setId(rs.getLong("ID_DESCRIZIONE_INTERVENTO"));
              decodificaInterventoDTO
                  .setDescrizione(rs.getString("DESC_INTERVENTO"));
              decodificaInterventoDTO.setDescrizioneAggregazionePrimoLivello(
                  rs.getString("DESC_AGGREGAZIONE_1L"));
              decodificaInterventoDTO.setCodice(rs.getString("ID_LIVELLO"));
              return decodificaInterventoDTO;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idDannoAtm", idDannoAtm)
          },
          new LogVariable[]
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

  public List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerInserimentoByIdDescrizioneIntervento(
      List<Long> listIdDescrizioneIntervento, long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getInfoInterventiPerInserimentoByIdDescrizioneIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = (" SELECT                                                             \n"
        + "   DI.DESCRIZIONE,                                                  \n"
        + "   DI.FLAG_GESTIONE_COSTO_UNITARIO,                                 \n"
        + "   DI.ID_DESCRIZIONE_INTERVENTO,                                    \n"
        + "   DI.ID_TIPO_CLASSIFICAZIONE,                                      \n"
        + "   DI.ID_TIPO_LOCALIZZAZIONE,                                       \n"
        + "   DI.FLAG_BENEFICIARIO,                                       	   \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                              \n"
        + "   UM.ID_UNITA_MISURA,                                              \n"
        + "   UM.CODICE AS CODICE_MISURA,                                      \n"
        + "   UM.DESCRIZIONE AS DESC_MISURA,                                   \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO,                                    \n"
        + "   LBI.COSTO_UNITARIO_MINIMO,                                       \n"
        + "   LBI.COSTO_UNITARIO_MASSIMO                                       \n"
        + " FROM                                                               \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DI,                                 \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                 \n"
        + "   IUF_D_UNITA_MISURA UM,                                           \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI                                   \n"
        + " WHERE                                                              \n"
        + "   DI.ID_DESCRIZIONE_INTERVENTO = MI.ID_DESCRIZIONE_INTERVENTO      \n"
        + "   AND LBI.ID_BANDO = :ID_BANDO                                     \n"
        + "   AND LBI.ID_DESCRIZIONE_INTERVENTO = DI.ID_DESCRIZIONE_INTERVENTO \n"
        + "   :IN_CONDITION                                                    \n"
        + "   AND MI.ID_UNITA_MISURA           = UM.ID_UNITA_MISURA(+)         \n"
        + "   AND TRUNC(NVL(UM.DATA_FINE,SYSDATE))<=TRUNC(SYSDATE)             \n"
        + " ORDER BY                                                           \n"
        + "   DI.DESCRIZIONE ASC,                                              \n"
        + "   DI.ID_DESCRIZIONE_INTERVENTO ASC,                                \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO ASC                                 \n")
            .replace(":IN_CONDITION",
                getInCondition("DI.ID_DESCRIZIONE_INTERVENTO",
                    listIdDescrizioneIntervento));
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    try
    {
      return (List<RigaModificaMultiplaInterventiDTO>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<List<RigaModificaMultiplaInterventiDTO>>()
              {
                @Override
                public List<RigaModificaMultiplaInterventiDTO> extractData(
                    ResultSet rs)
                    throws SQLException, DataAccessException
                {
                  List<RigaModificaMultiplaInterventiDTO> list = new ArrayList<RigaModificaMultiplaInterventiDTO>();
                  Long lastIdDescrizioneIntervento = null;
                  RigaModificaMultiplaInterventiDTO rigaDTO = null;
                  while (rs.next())
                  {
                    long idDescrizioneIntervento = rs
                        .getLong("ID_DESCRIZIONE_INTERVENTO");
                    if (lastIdDescrizioneIntervento == null
                        || lastIdDescrizioneIntervento != idDescrizioneIntervento)
                    {
                      rigaDTO = new RigaInserimentoMultiploInterventiDTO();
                      rigaDTO.setDescIntervento(rs.getString("DESCRIZIONE"));
                      rigaDTO.setFlagGestioneCostoUnitario(
                          rs.getString("FLAG_GESTIONE_COSTO_UNITARIO"));
                      rigaDTO.setFlagBeneficiario(
                          rs.getString("FLAG_BENEFICIARIO"));
                      rigaDTO
                          .setIdDescrizioneIntervento(idDescrizioneIntervento);
                      rigaDTO.setIdTipoClassificazione(
                          rs.getLong("ID_TIPO_CLASSIFICAZIONE"));
                      rigaDTO.setIdTipoLocalizzazione(
                          rs.getLong("ID_TIPO_LOCALIZZAZIONE"));
                      rigaDTO.setMisurazioneIntervento(
                          new ArrayList<MisurazioneInterventoDTO>());
                      rigaDTO.setCostoUnitarioMinimo(
                          rs.getBigDecimal("COSTO_UNITARIO_MINIMO"));
                      rigaDTO.setCostoUnitarioMassimo(
                          rs.getBigDecimal("COSTO_UNITARIO_MASSIMO"));
                      lastIdDescrizioneIntervento = idDescrizioneIntervento;
                      list.add(rigaDTO);
                    }
                    MisurazioneInterventoDTO m = new MisurazioneInterventoDTO();
                    m.setDescMisurazione(rs.getString("DESC_MISURAZIONE"));
                    m.setDescUnitaMisura(rs.getString("DESC_MISURA"));
                    m.setIdUnitaMisura(getLongNull(rs, "ID_UNITA_MISURA"));
                    m.setCodiceUnitaMisura(rs.getString("CODICE_MISURA"));
                    m.setIdMisurazioneIntervento(
                        rs.getLong("ID_MISURAZIONE_INTERVENTO"));
                    rigaDTO.getMisurazioneIntervento().add(m);
                  }
                  return list;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("listIdDescrizioneIntervento",
                  listIdDescrizioneIntervento),
              new LogParameter("idBando", idBando)
          },
          new LogVariable[]
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

  public List<RigaModificaMultiplaInterventiDTO> getInfoInvestimentiPerInserimentoByIdDescrizioneIntervento(
      List<Long> listIdDescrizioneIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getInfoInvestimentiPerInserimentoByIdDescrizioneIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = (" SELECT                                                             \n"
        + "   DI.DESCRIZIONE,                                                  \n"
        + "   DI.FLAG_GESTIONE_COSTO_UNITARIO,                                 \n"
        + "   DI.ID_DESCRIZIONE_INTERVENTO,                                    \n"
        + "   DI.ID_TIPO_CLASSIFICAZIONE,                                      \n"
        + "   DI.ID_TIPO_LOCALIZZAZIONE,                                       \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                              \n"
        + "   UM.ID_UNITA_MISURA,                                              \n"
        + "   UM.CODICE AS CODICE_MISURA,                                      \n"
        + "   UM.DESCRIZIONE AS DESC_MISURA,                                   \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO                                     \n"
        + " FROM                                                               \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DI,                                 \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                 \n"
        + "   IUF_D_UNITA_MISURA UM,                                           \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI                                    \n"
        + " WHERE                                                              \n"
        + "   DI.ID_DESCRIZIONE_INTERVENTO = MI.ID_DESCRIZIONE_INTERVENTO      \n"
        + "   :IN_CONDITION                                                    \n"
        + "   AND MI.ID_UNITA_MISURA           = UM.ID_UNITA_MISURA(+)         \n"
        + "   AND TRUNC(NVL(UM.DATA_FINE,SYSDATE))<=TRUNC(SYSDATE)             \n"
        + "   AND DI.ID_CATEGORIA_INTERVENTO = CI.ID_CATEGORIA_INTERVENTO  \n"
        + "   AND CI.FLAG_ESCLUDI_CATALOGO = :FLAG_ESCLUDI_CATALOGO            \n"
        + " ORDER BY                                                           \n"
        + "   DI.DESCRIZIONE ASC,                                              \n"
        + "   DI.ID_DESCRIZIONE_INTERVENTO ASC,                                \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO ASC                                 \n")
            .replace(":IN_CONDITION",
                getInCondition("DI.ID_DESCRIZIONE_INTERVENTO",
                    listIdDescrizioneIntervento));
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.SI);
    try
    {
      return (List<RigaModificaMultiplaInterventiDTO>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<List<RigaModificaMultiplaInterventiDTO>>()
              {
                @Override
                public List<RigaModificaMultiplaInterventiDTO> extractData(
                    ResultSet rs)
                    throws SQLException, DataAccessException
                {
                  List<RigaModificaMultiplaInterventiDTO> list = new ArrayList<RigaModificaMultiplaInterventiDTO>();
                  Long lastIdDescrizioneIntervento = null;
                  RigaModificaMultiplaInterventiDTO rigaDTO = null;
                  while (rs.next())
                  {
                    long idDescrizioneIntervento = rs
                        .getLong("ID_DESCRIZIONE_INTERVENTO");
                    if (lastIdDescrizioneIntervento == null
                        || lastIdDescrizioneIntervento != idDescrizioneIntervento)
                    {
                      rigaDTO = new RigaInserimentoMultiploInterventiDTO();
                      rigaDTO.setDescIntervento(rs.getString("DESCRIZIONE"));
                      rigaDTO.setFlagGestioneCostoUnitario(
                          rs.getString("FLAG_GESTIONE_COSTO_UNITARIO"));
                      rigaDTO
                          .setIdDescrizioneIntervento(idDescrizioneIntervento);
                      rigaDTO.setIdTipoClassificazione(
                          rs.getLong("ID_TIPO_CLASSIFICAZIONE"));
                      rigaDTO.setIdTipoLocalizzazione(
                          rs.getLong("ID_TIPO_LOCALIZZAZIONE"));
                      rigaDTO.setMisurazioneIntervento(
                          new ArrayList<MisurazioneInterventoDTO>());
                      lastIdDescrizioneIntervento = idDescrizioneIntervento;
                      list.add(rigaDTO);
                    }
                    MisurazioneInterventoDTO m = new MisurazioneInterventoDTO();
                    m.setDescMisurazione(rs.getString("DESC_MISURAZIONE"));
                    m.setDescUnitaMisura(rs.getString("DESC_MISURA"));
                    m.setIdUnitaMisura(getLongNull(rs, "ID_UNITA_MISURA"));
                    m.setCodiceUnitaMisura(rs.getString("CODICE_MISURA"));
                    m.setIdMisurazioneIntervento(
                        rs.getLong("ID_MISURAZIONE_INTERVENTO"));
                    rigaDTO.getMisurazioneIntervento().add(m);
                  }
                  return list;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("listIdDescrizioneIntervento",
                  listIdDescrizioneIntervento)
          },
          new LogVariable[]
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

  public long insertIntervento(RigaModificaMultiplaInterventiDTO intervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
      if (intervento == null)
      {
        logger.error(THIS_METHOD
            + " intervento == null! NullPointerException in arrivo!");
      }
    }
    final String INSERT = " INSERT                                                      \n"
        + " INTO                                                        \n"
        + "   IUF_T_INTERVENTO                                          \n"
        + "   (                                                         \n"
        + "     ID_INTERVENTO,                                          \n"
        + "     ID_PROCEDIMENTO,                                        \n"
        + "     ID_DESCRIZIONE_INTERVENTO                               \n"
        + "   )                                                         \n"
        + "   (                                                         \n"
        + "     SELECT                                                  \n"
        + "       :ID_INTERVENTO,                                       \n"
        + "       PO.ID_PROCEDIMENTO,                                   \n"
        + "       :ID_DESCRIZIONE_INTERVENTO                            \n"
        + "     FROM                                                    \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO                         \n"
        + "     WHERE                                                   \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "   )                                                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long idIntervento = getNextSequenceValue(
        IuffiConstants.SQL.SEQUENCE.IUF_T_INTERVENTO);
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          intervento.getIdProcedimentoOggetto(), Types.NUMERIC);
      mapParameterSource.addValue("ID_DESCRIZIONE_INTERVENTO",
          intervento.getIdDescrizioneIntervento(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idIntervento;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("intervento", intervento),
              new LogParameter("idProcedimentoOggetto",
                  intervento.getIdProcedimentoOggetto())
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public long insertInterventoNonDefinitivo(
      RigaModificaMultiplaInterventiDTO intervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertInterventoNonDefinitivo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
      if (intervento == null)
      {
        logger.error(THIS_METHOD
            + " intervento == null! NullPointerException in arrivo!");
      }
    }
    final String INSERT = " INSERT                        \n"
        + " INTO                          \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG  \n"
        + "   (                           \n"
        + "     ID_DETT_INTERV_PROC_OGG,  \n"
        + "     ID_PROCEDIMENTO_OGGETTO,  \n"
        + "     ID_INTERVENTO,            \n"
        + "     FLAG_TIPO_OPERAZIONE,     \n"
        + "     IMPORTO_INVESTIMENTO,     \n"
        + "     ULTERIORI_INFORMAZIONI,   \n"
        + "     CUAA_BENEFICIARIO,   \n"
        + "     IMPORTO_UNITARIO          \n"
        + "   )                           \n"
        + "   VALUES                      \n"
        + "   (                           \n"
        + "     :ID_DETT_INTERV_PROC_OGG, \n"
        + "     :ID_PROCEDIMENTO_OGGETTO, \n"
        + "     :ID_INTERVENTO,           \n"
        + "     :FLAG_TIPO_OPERAZIONE,    \n"
        + "     :IMPORTO_INVESTIMENTO,    \n"
        + "     :ULTERIORI_INFORMAZIONI,  \n"
        + "     :CUAA_BENEFICIARIO,  \n"
        + "     :IMPORTO_UNITARIO         \n"
        + "   )                           \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long idDettIntervProcOgg = getNextSequenceValue(
        IuffiConstants.SQL.SEQUENCE.IUF_W_DETT_INTERV_PROC_OGG);
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          intervento.getIdProcedimentoOggetto(), Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", intervento.getIdIntervento(),
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_TIPO_OPERAZIONE",
          IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_INSERIMENTO,
          Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_INVESTIMENTO",
          intervento.getImporto(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_UNITARIO",
          intervento.getImportoUnitario(), Types.NUMERIC);
      mapParameterSource.addValue("ULTERIORI_INFORMAZIONI",
          intervento.getUlterioriInformazioni(), Types.VARCHAR);
      mapParameterSource.addValue("CUAA_BENEFICIARIO",
          intervento.getCuaaPartecipante(), Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idDettIntervProcOgg;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("intervento", intervento)
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public void insertMisurazioneInterventoNonDefinitivo(long idDettIntervProcOgg,
      long idMisurazioneIntervento,
      BigDecimal valore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertInterventoNonDefinitivo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                           \n"
        + " INTO                             \n"
        + "   IUF_R_DETT_INTE_PROC_OGG_MIS \n"
        + "   (                              \n"
        + "     ID_DETT_INTERV_PROC_OGG,     \n"
        + "     ID_MISURAZIONE_INTERVENTO,   \n"
        + "     QUANTITA                     \n"
        + "   )                              \n"
        + "   VALUES                         \n"
        + "   (                              \n"
        + "     :ID_DETT_INTERV_PROC_OGG,    \n"
        + "     :ID_MISURAZIONE_INTERVENTO,  \n"
        + "     :QUANTITA                    \n"
        + "   )                              \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_MISURAZIONE_INTERVENTO",
          idMisurazioneIntervento, Types.NUMERIC);
      mapParameterSource.addValue("QUANTITA", valore, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("idMisurazioneIntervento",
                  idMisurazioneIntervento),
              new LogParameter("valore", valore)
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public void eliminazioneCondizionaleIntervento(long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::eliminazioneCondizionaleIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " DELETE                                \n"
        + " FROM                                  \n"
        + "   IUF_T_INTERVENTO T                  \n"
        + " WHERE                                 \n"
        + "   T.ID_INTERVENTO = :ID_INTERVENTO    \n"
        + "   AND NOT EXISTS                      \n"
        + "   (                                   \n"
        + "     SELECT                            \n"
        + "       *                               \n"
        + "     FROM                              \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG      \n"
        + "     WHERE                             \n"
        + "       ID_INTERVENTO = T.ID_INTERVENTO \n"
        + "   )                                   \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntervento", idIntervento)
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public List<RigaModificaMultiplaInterventiDTO> getInfoInterventiPerModifica(
      final long idProcedimentoOggetto,
      List<Long> idIntervento, long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getInfoInterventiPerModifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String idInterventoInCondition = getInCondition("I.ID_INTERVENTO",
        idIntervento);
    final String QUERY = " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   NULL AS ID_DETT_INTERV_PROC_OGG,                                                    \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DESCINT.ID_DESCRIZIONE_INTERVENTO,                                                  \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   DESCINT.FLAG_BENEFICIARIO,                                               		      \n"
        + "   DESCINT.ID_TIPO_CLASSIFICAZIONE,                                                    \n"
        + "   DESCINT.FLAG_GESTIONE_COSTO_UNITARIO,                                               \n"
        + "   DI.IMPORTO_INVESTIMENTO,                                                            \n"
        + "   DI.IMPORTO_UNITARIO,                                                                \n"
        + "   DI.CUAA_BENEFICIARIO,                                                                \n"
        + "   DIM.QUANTITA,                                                                       \n"
        + "   LBI.COSTO_UNITARIO_MINIMO,                                                          \n"
        + "   LBI.COSTO_UNITARIO_MASSIMO,                                                         \n"
        + "   NULL           AS FLAG_TIPO_OPERAZIONE,                                             \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                                                 \n"
        + "   UM.ID_UNITA_MISURA,                                                                 \n"
        + "   UM.CODICE      AS CODICE_MISURA,                                                    \n"
        + "   UM.DESCRIZIONE AS DESC_MISURA,                                                      \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO,                                                       \n"
        + "   NULL AS FLAG_TIPO_OPERAZIONE,                                                       \n"
        + "   DI.IMPORTO_AMMESSO,                                                                  \n"
        + "	  DESCINT.CODICE_IDENTIFICATIVO														  \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_R_DETT_INTERV_MISURAZION DIM,                                                  \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                                    \n"
        + "   IUF_D_UNITA_MISURA UM,                                                              \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI                                                      \n"
        + " WHERE                                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "   AND I.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                                     \n"
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "   AND I.ID_INTERVENTO               = DI.ID_INTERVENTO                                \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO   = DESCINT.ID_DESCRIZIONE_INTERVENTO               \n"
        + "   AND DI.ID_DETTAGLIO_INTERVENTO    = DIM.ID_DETTAGLIO_INTERVENTO                     \n"
        + "   AND DIM.ID_MISURAZIONE_INTERVENTO = MI.ID_MISURAZIONE_INTERVENTO                    \n"
        + "   AND MI.ID_UNITA_MISURA            = UM.ID_UNITA_MISURA(+)                           \n"
        + "   AND LBI.ID_DESCRIZIONE_INTERVENTO = I.ID_DESCRIZIONE_INTERVENTO                     \n"
        + "   AND LBI.ID_BANDO                  = :ID_BANDO                                       \n"
        + idInterventoInCondition
        + "   AND NOT EXISTS                                                                      \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       *                                                                               \n"
        + "     FROM                                                                              \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "     WHERE                                                                             \n"
        + "       WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "       AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "   )                                                                                   \n"
        + " UNION                                                                                 \n"
        + " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   DIPO.ID_DETT_INTERV_PROC_OGG,                                                       \n"
        + "   DIPO.ULTERIORI_INFORMAZIONI,                                                        \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DESCINT.ID_DESCRIZIONE_INTERVENTO,                                                  \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   DESCINT.FLAG_BENEFICIARIO,                                               		      \n"
        + "   DESCINT.ID_TIPO_CLASSIFICAZIONE,                                                    \n"
        + "   DESCINT.FLAG_GESTIONE_COSTO_UNITARIO,                                               \n"
        + "   DIPO.IMPORTO_INVESTIMENTO,                                                          \n"
        + "   DIPO.IMPORTO_UNITARIO,                                                              \n"
        + "   DIPO.CUAA_BENEFICIARIO,                                                              \n"
        + "   DIPOM.QUANTITA,                                                                     \n"
        + "   LBI.COSTO_UNITARIO_MINIMO,                                                          \n"
        + "   LBI.COSTO_UNITARIO_MASSIMO,                                                         \n"
        + "   DIPO.FLAG_TIPO_OPERAZIONE,                                                          \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                                                 \n"
        + "   UM.ID_UNITA_MISURA,                                                                 \n"
        + "   UM.CODICE      AS CODICE_MISURA,                                                    \n"
        + "   UM.DESCRIZIONE AS DESC_MISURA,                                                      \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO,                                                       \n"
        + "   DIPO.FLAG_TIPO_OPERAZIONE,                                                          \n"
        + "   DIPO.IMPORTO_AMMESSO,                                                                \n"
        + "	  DESCINT.CODICE_IDENTIFICATIVO														  \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "   IUF_R_DETT_INTE_PROC_OGG_MIS DIPOM,                                               \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                                    \n"
        + "   IUF_D_UNITA_MISURA UM,                                                              \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI                                                      \n"
        + " WHERE                                                                                 \n"
        + "   I.ID_DESCRIZIONE_INTERVENTO         = DESCINT.ID_DESCRIZIONE_INTERVENTO             \n"
        + "   AND I.ID_INTERVENTO                 = DI.ID_INTERVENTO(+)                           \n"
        + "   AND DIPO.ID_INTERVENTO              = I.ID_INTERVENTO                               \n"
        + "   AND DIPO.ID_DETT_INTERV_PROC_OGG    = DIPOM.ID_DETT_INTERV_PROC_OGG                 \n"
        + "   AND DIPOM.ID_MISURAZIONE_INTERVENTO = MI.ID_MISURAZIONE_INTERVENTO                  \n"
        + "   AND MI.ID_UNITA_MISURA              = UM.ID_UNITA_MISURA(+)                         \n"
        + "   AND DIPO.ID_PROCEDIMENTO_OGGETTO    = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "   AND LBI.ID_DESCRIZIONE_INTERVENTO   = I.ID_DESCRIZIONE_INTERVENTO                   \n"
        + "   AND LBI.ID_BANDO                    = :ID_BANDO                                     \n"
        + idInterventoInCondition
        + " ORDER BY                                                                              \n"
        + "   PROGRESSIVO ASC NULLS LAST,                                                         \n"
        + "   ID_DETT_INTERV_PROC_OGG ASC,                                                        \n"
        + "   ID_MISURAZIONE_INTERVENTO ASC                                                       \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    try
    {
      return (List<RigaModificaMultiplaInterventiDTO>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<List<RigaModificaMultiplaInterventiDTO>>()
              {
                @Override
                public List<RigaModificaMultiplaInterventiDTO> extractData(
                    ResultSet rs)
                    throws SQLException, DataAccessException
                {
                  List<RigaModificaMultiplaInterventiDTO> list = new ArrayList<RigaModificaMultiplaInterventiDTO>();
                  Long lastIdIntervento = null;
                  RigaModificaMultiplaInterventiDTO rigaDTO = null;
                  while (rs.next())
                  {
                    long idIntervento = rs.getLong("ID_INTERVENTO");
                    if (lastIdIntervento == null
                        || lastIdIntervento != idIntervento)
                    {
                      rigaDTO = new RigaModificaMultiplaInterventiDTO();

                      rigaDTO.setIdIntervento(idIntervento);
                      rigaDTO.setIdTipoLocalizzazione(
                          rs.getInt("ID_TIPO_LOCALIZZAZIONE"));
                      rigaDTO.setProgressivo(getIntegerNull(rs, "PROGRESSIVO"));
                      rigaDTO.setUlterioriInformazioni(
                          rs.getString("ULTERIORI_INFORMAZIONI"));
                      rigaDTO
                          .setDescIntervento(rs.getString("DESC_INTERVENTO"));
                      rigaDTO
                          .setImporto(rs.getBigDecimal("IMPORTO_INVESTIMENTO"));
                      rigaDTO.setFlagBeneficiario(
                          rs.getString("FLAG_BENEFICIARIO"));
                      rigaDTO.setCuaaPartecipante(
                          rs.getString("CUAA_BENEFICIARIO"));
                      rigaDTO.setImportoUnitario(
                          rs.getBigDecimal("IMPORTO_UNITARIO"));
                      rigaDTO.setIdProcedimentoOggetto(idProcedimentoOggetto);
                      rigaDTO.setIdDescrizioneIntervento(
                          rs.getLong("ID_DESCRIZIONE_INTERVENTO"));
                      rigaDTO.setIdTipoClassificazione(
                          rs.getLong("ID_TIPO_CLASSIFICAZIONE"));
                      rigaDTO.setCostoUnitarioMinimo(
                          rs.getBigDecimal("COSTO_UNITARIO_MINIMO"));
                      rigaDTO.setCostoUnitarioMassimo(
                          rs.getBigDecimal("COSTO_UNITARIO_MASSIMO"));
                      rigaDTO.setFlagGestioneCostoUnitario(
                          rs.getString("FLAG_GESTIONE_COSTO_UNITARIO"));
                      rigaDTO.setFlagTipoOperazione(
                          rs.getString("FLAG_TIPO_OPERAZIONE"));
                      rigaDTO.setMisurazioneIntervento(
                          new ArrayList<MisurazioneInterventoDTO>());
                      rigaDTO.setImportoAmmesso(
                          rs.getBigDecimal("IMPORTO_AMMESSO"));
                      rigaDTO.setCodiceIdentificativo(
                              rs.getString("CODICE_IDENTIFICATIVO"));
                      lastIdIntervento = idIntervento;
                      list.add(rigaDTO);
                    }
                    MisurazioneInterventoDTO m = new MisurazioneInterventoDTO();
                    m.setDescMisurazione(rs.getString("DESC_MISURAZIONE"));
                    m.setDescUnitaMisura(rs.getString("DESC_MISURA"));
                    m.setIdUnitaMisura(getLongNull(rs, "ID_UNITA_MISURA"));
                    m.setCodiceUnitaMisura(rs.getString("CODICE_MISURA"));
                    m.setIdMisurazioneIntervento(
                        rs.getLong("ID_MISURAZIONE_INTERVENTO"));
                    m.setValore(rs.getBigDecimal("QUANTITA"));
                    rigaDTO.getMisurazioneIntervento().add(m);
                  }
                  return list;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idBando", idBando)
          },
          new LogVariable[]
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

  public List<RigaModificaMultiplaInterventiDTO> getInfoInvestimentiPerModifica(
      final long idProcedimentoOggetto,
      List<Long> idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getInfoInvestimentiPerModifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String idInterventoInCondition = getInCondition("I.ID_INTERVENTO",
        idIntervento);
    final String QUERY = " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   NULL AS ID_DETT_INTERV_PROC_OGG,                                                    \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DESCINT.ID_DESCRIZIONE_INTERVENTO,                                                  \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   DESCINT.ID_TIPO_CLASSIFICAZIONE,                                                    \n"
        + "   DESCINT.FLAG_GESTIONE_COSTO_UNITARIO,                                               \n"
        + "   DI.IMPORTO_INVESTIMENTO,                                                            \n"
        + "   DI.IMPORTO_UNITARIO,                                                                \n"
        + "   DIM.QUANTITA,                                                                       \n"
        + "   NULL           AS FLAG_TIPO_OPERAZIONE,                                             \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                                                 \n"
        + "   UM.ID_UNITA_MISURA,                                                                 \n"
        + "   UM.CODICE      AS CODICE_MISURA,                                                    \n"
        + "   UM.DESCRIZIONE AS DESC_MISURA,                                                      \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO,                                                       \n"
        + "   NULL AS FLAG_TIPO_OPERAZIONE                                                        \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_R_DETT_INTERV_MISURAZION DIM,                                                  \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                                    \n"
        + "   IUF_D_UNITA_MISURA UM,                                                              \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + " WHERE                                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO           = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "   AND I.ID_PROCEDIMENTO                = PO.ID_PROCEDIMENTO                           \n"
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "   AND I.ID_INTERVENTO                  = DI.ID_INTERVENTO                             \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO      = DESCINT.ID_DESCRIZIONE_INTERVENTO            \n"
        + "   AND DI.ID_DETTAGLIO_INTERVENTO       = DIM.ID_DETTAGLIO_INTERVENTO                  \n"
        + "   AND DIM.ID_MISURAZIONE_INTERVENTO    = MI.ID_MISURAZIONE_INTERVENTO                 \n"
        + "   AND MI.ID_UNITA_MISURA               = UM.ID_UNITA_MISURA(+)                        \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO  = CI.ID_CATEGORIA_INTERVENTO                   \n"
        + "   AND CI.FLAG_ESCLUDI_CATALOGO         = :FLAG_ESCLUDI_CATALOGO                       \n"
        + idInterventoInCondition
        + "   AND NOT EXISTS                                                                      \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       *                                                                               \n"
        + "     FROM                                                                              \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "     WHERE                                                                             \n"
        + "       WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "       AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "   )                                                                                   \n"
        + " UNION                                                                                 \n"
        + " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   DIPO.ID_DETT_INTERV_PROC_OGG,                                                       \n"
        + "   DIPO.ULTERIORI_INFORMAZIONI,                                                        \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DESCINT.ID_DESCRIZIONE_INTERVENTO,                                                  \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   DESCINT.ID_TIPO_CLASSIFICAZIONE,                                                    \n"
        + "   DESCINT.FLAG_GESTIONE_COSTO_UNITARIO,                                               \n"
        + "   DIPO.IMPORTO_INVESTIMENTO,                                                          \n"
        + "   DIPO.IMPORTO_UNITARIO,                                                              \n"
        + "   DIPOM.QUANTITA,                                                                     \n"
        + "   DIPO.FLAG_TIPO_OPERAZIONE,                                                          \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                                                 \n"
        + "   UM.ID_UNITA_MISURA,                                                                 \n"
        + "   UM.CODICE      AS CODICE_MISURA,                                                    \n"
        + "   UM.DESCRIZIONE AS DESC_MISURA,                                                      \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO,                                                       \n"
        + "   DIPO.FLAG_TIPO_OPERAZIONE                                                           \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "   IUF_R_DETT_INTE_PROC_OGG_MIS DIPOM,                                               \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                                    \n"
        + "   IUF_D_UNITA_MISURA UM,                                                              \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + " WHERE                                                                                 \n"
        + "   I.ID_DESCRIZIONE_INTERVENTO         = DESCINT.ID_DESCRIZIONE_INTERVENTO             \n"
        + "   AND I.ID_INTERVENTO                 = DI.ID_INTERVENTO(+)                           \n"
        + "   AND DIPO.ID_INTERVENTO              = I.ID_INTERVENTO                               \n"
        + "   AND DIPO.ID_DETT_INTERV_PROC_OGG    = DIPOM.ID_DETT_INTERV_PROC_OGG                 \n"
        + "   AND DIPOM.ID_MISURAZIONE_INTERVENTO = MI.ID_MISURAZIONE_INTERVENTO                  \n"
        + "   AND MI.ID_UNITA_MISURA              = UM.ID_UNITA_MISURA(+)                         \n"
        + "   AND DIPO.ID_PROCEDIMENTO_OGGETTO    = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO = CI.ID_CATEGORIA_INTERVENTO                    \n"
        + "   AND CI.FLAG_ESCLUDI_CATALOGO        = :FLAG_ESCLUDI_CATALOGO                        \n"
        + idInterventoInCondition
        + " ORDER BY                                                                              \n"
        + "   PROGRESSIVO ASC NULLS LAST,                                                         \n"
        + "   ID_DETT_INTERV_PROC_OGG ASC                                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.SI);
    try
    {
      return (List<RigaModificaMultiplaInterventiDTO>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<List<RigaModificaMultiplaInterventiDTO>>()
              {
                @Override
                public List<RigaModificaMultiplaInterventiDTO> extractData(
                    ResultSet rs)
                    throws SQLException, DataAccessException
                {
                  List<RigaModificaMultiplaInterventiDTO> list = new ArrayList<RigaModificaMultiplaInterventiDTO>();
                  Long lastIdIntervento = null;
                  RigaModificaMultiplaInterventiDTO rigaDTO = null;
                  while (rs.next())
                  {
                    long idIntervento = rs.getLong("ID_INTERVENTO");
                    if (lastIdIntervento == null
                        || lastIdIntervento != idIntervento)
                    {
                      rigaDTO = new RigaModificaMultiplaInterventiDTO();

                      rigaDTO.setIdIntervento(idIntervento);
                      rigaDTO.setIdTipoLocalizzazione(
                          rs.getInt("ID_TIPO_LOCALIZZAZIONE"));
                      rigaDTO.setProgressivo(getIntegerNull(rs, "PROGRESSIVO"));
                      rigaDTO.setUlterioriInformazioni(
                          rs.getString("ULTERIORI_INFORMAZIONI"));
                      rigaDTO
                          .setDescIntervento(rs.getString("DESC_INTERVENTO"));
                      rigaDTO
                          .setImporto(rs.getBigDecimal("IMPORTO_INVESTIMENTO"));
                      rigaDTO.setImportoUnitario(
                          rs.getBigDecimal("IMPORTO_UNITARIO"));
                      rigaDTO.setIdProcedimentoOggetto(idProcedimentoOggetto);
                      rigaDTO.setIdDescrizioneIntervento(
                          rs.getLong("ID_DESCRIZIONE_INTERVENTO"));
                      rigaDTO.setIdTipoClassificazione(
                          rs.getLong("ID_TIPO_CLASSIFICAZIONE"));
                      rigaDTO.setFlagGestioneCostoUnitario(
                          rs.getString("FLAG_GESTIONE_COSTO_UNITARIO"));
                      rigaDTO.setFlagTipoOperazione(
                          rs.getString("FLAG_TIPO_OPERAZIONE"));
                      rigaDTO.setMisurazioneIntervento(
                          new ArrayList<MisurazioneInterventoDTO>());
                      lastIdIntervento = idIntervento;
                      list.add(rigaDTO);
                    }
                    MisurazioneInterventoDTO m = new MisurazioneInterventoDTO();
                    m.setDescMisurazione(rs.getString("DESC_MISURAZIONE"));
                    m.setDescUnitaMisura(rs.getString("DESC_MISURA"));
                    m.setIdUnitaMisura(getLongNull(rs, "ID_UNITA_MISURA"));
                    m.setCodiceUnitaMisura(rs.getString("CODICE_MISURA"));
                    m.setIdMisurazioneIntervento(
                        rs.getLong("ID_MISURAZIONE_INTERVENTO"));
                    m.setValore(rs.getBigDecimal("QUANTITA"));
                    rigaDTO.getMisurazioneIntervento().add(m);
                  }
                  return list;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
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

  public List<RigaJSONInterventoQuadroEconomicoDTO> getInterventiQuadroEconomicoPerModifica(
      final long idProcedimentoOggetto, List<Long> idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getInterventiQuadroEconomicoPerModifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String idInterventoInCondition = getInCondition("I.ID_INTERVENTO",
        idIntervento);
    final String QUERY = " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DI.ID_DETTAGLIO_INTERVENTO,                                                         \n"
        + "   NULL AS ID_DETT_INTERV_PROC_OGG,                                                    \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "   DI.IMPORTO_INVESTIMENTO,                                                            \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   NULL                AS FLAG_TIPO_OPERAZIONE,                                        \n"
        + "   DI.IMPORTO_AMMESSO,                                                                 \n"
        + "   DI.PERCENTUALE_CONTRIBUTO,                                                          \n"
        + "   DI.IMPORTO_CONTRIBUTO,                                                              \n"
        + "   LB.ID_LIVELLO,                                                                      \n"
        + "   NVL(LB.PERCENTUALE_CONTRIBUTO_MASSIMA,100) AS PERCENTUALE_CONTRIBUTO_MASSIMA,         \n"
        + "   NVL(LB.PERCENTUALE_CONTRIBUTO_MINIMA,0) AS PERCENTUALE_CONTRIBUTO_MINIMA            \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_PROCEDIMENTO P,                                                               \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI,                                                     \n"
        + "   IUF_R_LIVELLO_BANDO LB                                                              \n"
        + " WHERE                                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "   AND PO.ID_PROCEDIMENTO     = P.ID_PROCEDIMENTO                                      \n"
        + "   AND I.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                                     \n"
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "   AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO                            \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + idInterventoInCondition + "\n"
        + "   AND DESCINT.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO               \n"
        + "   AND LBI.ID_BANDO                      = LB.ID_BANDO                                 \n"
        + "   AND LBI.ID_LIVELLO                    = LB.ID_LIVELLO                               \n"
        + "   AND P.ID_BANDO                        = LBI.ID_BANDO                                \n"
        + "   AND NOT EXISTS                                                                      \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       *                                                                               \n"
        + "     FROM                                                                              \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "     WHERE                                                                             \n"
        + "       WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "       AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "   )                                                                                   \n"
        + " UNION                                                                                 \n"
        + " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   NULL AS ID_DETTAGLIO_INTERVENTO,                                                    \n"
        + "   DIPO.ID_DETT_INTERV_PROC_OGG,                                                       \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DIPO.ULTERIORI_INFORMAZIONI,                                                        \n"
        + "   DIPO.IMPORTO_INVESTIMENTO,                                                          \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   DIPO.FLAG_TIPO_OPERAZIONE,                                                          \n"
        + "   DIPO.IMPORTO_AMMESSO,                                                               \n"
        + "   DIPO.PERCENTUALE_CONTRIBUTO,                                                        \n"
        + "   DIPO.IMPORTO_CONTRIBUTO,                                                            \n"
        + "   LB.ID_LIVELLO,                                                                      \n"
        + "   NVL(LB.PERCENTUALE_CONTRIBUTO_MASSIMA,100) AS PERCENTUALE_CONTRIBUTO_MASSIMA,       \n"
        + "   NVL(LB.PERCENTUALE_CONTRIBUTO_MINIMA,0) AS PERCENTUALE_CONTRIBUTO_MINIMA            \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_PROCEDIMENTO P,                                                               \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI,                                                     \n"
        + "   IUF_R_LIVELLO_BANDO LB                                                              \n"
        + " WHERE                                                                                 \n"
        + "   I.ID_DESCRIZIONE_INTERVENTO           = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + "   AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO(+)                         \n"
        + "   AND DIPO.ID_INTERVENTO                = I.ID_INTERVENTO                             \n"
        + "   AND DIPO.ID_PROCEDIMENTO_OGGETTO      = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "   AND DIPO.ID_PROCEDIMENTO_OGGETTO      = PO.ID_PROCEDIMENTO_OGGETTO                  \n"
        + "   AND PO.ID_PROCEDIMENTO                = P.ID_PROCEDIMENTO                           \n"
        + "   AND DESCINT.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO               \n"
        + "   AND LBI.ID_BANDO                      = LB.ID_BANDO                                 \n"
        + "   AND LBI.ID_LIVELLO                    = LB.ID_LIVELLO                               \n"
        + "   AND P.ID_BANDO                        = LBI.ID_BANDO                                \n"
        + idInterventoInCondition + "\n"
        + " ORDER BY                                                                              \n"
        + "   PROGRESSIVO ASC NULLS LAST,                                                         \n"
        + "   ID_DETT_INTERV_PROC_OGG ASC                                                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return (List<RigaJSONInterventoQuadroEconomicoDTO>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<List<RigaJSONInterventoQuadroEconomicoDTO>>()
              {
                @Override
                public List<RigaJSONInterventoQuadroEconomicoDTO> extractData(
                    ResultSet rs)
                    throws SQLException, DataAccessException
                {
                  List<RigaJSONInterventoQuadroEconomicoDTO> list = new ArrayList<RigaJSONInterventoQuadroEconomicoDTO>();
                  RigaJSONInterventoQuadroEconomicoDTO rigaDTO = null;
                  while (rs.next())
                  {
                    rigaDTO = new RigaJSONInterventoQuadroEconomicoDTO();
                    rigaDTO.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                    rigaDTO.setFlagTipoOperazione(
                        rs.getString("FLAG_TIPO_OPERAZIONE"));
                    rigaDTO.setIdDettaglioIntervento(
                        rs.getLong("ID_DETTAGLIO_INTERVENTO"));
                    rigaDTO.setIdDettIntervProcOgg(
                        rs.getLong("ID_DETT_INTERV_PROC_OGG"));
                    rigaDTO.setIdIntervento(rs.getLong("ID_INTERVENTO"));
                    rigaDTO.setImportoAmmesso(setBigDecimalScale(
                        rs.getBigDecimal("IMPORTO_AMMESSO"), 2));
                    rigaDTO.setImportoContributo(
                        rs.getBigDecimal("IMPORTO_CONTRIBUTO"));
                    rigaDTO.setImportoInvestimento(setBigDecimalScale(
                        rs.getBigDecimal("IMPORTO_INVESTIMENTO"), 2));
                    rigaDTO.setPercentualeContributo(setBigDecimalScale(
                        rs.getBigDecimal("PERCENTUALE_CONTRIBUTO"), 2));
                    rigaDTO.setProgressivo(getIntegerNull(rs, "PROGRESSIVO"));
                    rigaDTO.setUlterioriInformazioni(
                        rs.getString("ULTERIORI_INFORMAZIONI"));
                    rigaDTO.setIdLivello(rs.getLong("ID_LIVELLO"));
                    rigaDTO.setPercentualeContributoMassima(
                        setBigDecimalScale(
                            rs.getBigDecimal("PERCENTUALE_CONTRIBUTO_MASSIMA"),
                            2));
                    rigaDTO.setPercentualeContributoMinima(
                        setBigDecimalScale(
                            rs.getBigDecimal("PERCENTUALE_CONTRIBUTO_MINIMA"),
                            2));
                    list.add(rigaDTO);
                  }
                  return list;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
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

  public List<RigaElencoInterventi> getElencoInterventiProcedimentoOggetto(
      long idProcedimentoOggetto,
      Long idIntervento, String flagEscludiCatalogo, Date dataValidita)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getListInterventiByIdProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY_INTERVENTI = " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   DI.ID_DETTAGLIO_INTERVENTO,                                                         \n"
        + "   NULL AS ID_DETT_INTERV_PROC_OGG,                                                    \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "   TC.DESCRIZIONE AS DESC_TIPO_CLASSIFICAZIONE,                                        \n"
        + "   DI.IMPORTO_INVESTIMENTO,                                                            \n"
        + "   DI.IMPORTO_UNITARIO,                                                                \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   DIM.QUANTITA,                                                                       \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                                                 \n"
        + "   UM.CODICE      AS CODICE_MISURA,                                                    \n"
        + "   NULL AS FLAG_TIPO_OPERAZIONE,                                                       \n"
        + "   TA.DESCRIZIONE AS DESC_TIPO_AGGREGAZIONE,                                           \n"
        + "   DI.FLAG_ASSOCIATO_ALTRA_MISURA,                                                     \n"
        + "   LIV.CODICE AS OPERAZIONE,			                                                  \n"
        + "   DI.IMPORTO_AMMESSO,                                                                 \n"
        + "   DI.PERCENTUALE_CONTRIBUTO,                                                          \n"
        + "   DI.IMPORTO_CONTRIBUTO,                                                              \n"
        + "   DESCINT.ID_DESC_INTERVENTO_ASSOCIATO,                                               \n"
        + "   DI.CUAA_BENEFICIARIO AS CUAA_PARTEC,												  \n"
        + "   DESCINT.FLAG_BENEFICIARIO,                                               			  \n"

        // denominazione azienda
        + "     (                                                                                                                                            \n"
        + "       NVL(                                                                                                                                       \n"
        + "         (                                                                                                                                        \n"
        + "         SELECT                                                                                                                                   \n"
        + "           TP.DENOMINAZIONE                                                                                                                       \n"
        + "         FROM                                                                                                                                     \n"
        + "           IUF_T_PARTECIPANTE TP,                                                                                                                 \n"
        + "           IUF_T_DATI_PROCEDIMENTO TDP                                                                                                            \n"
        + "         WHERE                                                                                                                                    \n"
        + "           TDP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                                                                               \n"
        + "           AND TDP.ID_DATI_PROCEDIMENTO = TP.ID_DATI_PROCEDIMENTO                                                                                 \n"
        + "           AND TP.CUAA = DI.CUAA_BENEFICIARIO )                                                                                                   \n"
        + "         ,                                                                                                                                        \n"
        + "         (                                                                                                                                        \n"
        + "          SELECT                                                                                                                                  \n"
        + "            DA.DENOMINAZIONE                                                                                                                      \n"
        + "          FROM                                                                                                                                    \n"
        + "            SMRGAA_V_DATI_ANAGRAFICI DA                                                                                                           \n"
        + "          WHERE                                                                                                                                   \n"
        + "            TRUNC(NVL(:DATA_VALIDITA,SYSDATE)) BETWEEN DA.DATA_INIZIO_VALIDITA AND NVL(DA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
        + "            AND DI.CUAA_BENEFICIARIO = DA.CUAA                                                                                                    \n"
        + "            AND ROWNUM=1						                                                                                                     \n"
        + "         )                                                                                                                                        \n"
        + "       )                                                                                                                                          \n"
        + "     )AS CUAA_DESCR,                                                                                                                              \n"

        + "   DESCINT.FLAG_GESTIONE_COSTO_UNITARIO,                                               \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO,                                                        \n"
        + "   DA.ID_DANNO_ATM,                                                       			 	\n"
        + "   DA.DESCRIZIONE AS DESC_DANNO,                                                        \n"
        + "   DA.PROGRESSIVO AS PROGRESSIVO_DANNO,                                                        \n"
        + "	  DD.ID_DANNO, \n"
        + "	  DD.DESCRIZIONE AS DESC_TIPO_DANNO, \n"
        + "	  DESCINT.CODICE_IDENTIFICATIVO														  \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_R_DETT_INTERV_MISURAZION DIM,                                                  \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                                    \n"
        + "   IUF_D_UNITA_MISURA UM,                                                              \n"
        + "   IUF_D_TIPO_CLASSIFICAZIONE TC,                                                      \n"
        + "   IUF_R_AGGREGAZIONE_INTERVENT AI,                                                   \n"
        + "   IUF_D_TIPO_AGGREGAZIONE TA,                                                         \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI,                                                     \n"
        + "   IUF_D_LIVELLO LIV,			                                                      \n"
        + "   IUF_T_PROCEDIMENTO PROC,		                                                      \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI,                                                    \n"
        
        + "   IUF_R_DANNO_ATM_INTERVENTO DAIN,                                                  \n"
        + "   IUF_T_DANNO_ATM DA,                                                       		  \n"
        + "   IUF_D_DANNO DD                                                       		  \n"
        + " WHERE                                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "   AND I.ID_PROCEDIMENTO    = PO.ID_PROCEDIMENTO                                       \n"
        + ((idIntervento == null) ? ""
            : "    AND I.ID_INTERVENTO = :ID_INTERVENTO \n")
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "   AND I.ID_INTERVENTO               = DI.ID_INTERVENTO                                \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO   = DESCINT.ID_DESCRIZIONE_INTERVENTO               \n"
        + "   AND DI.ID_DETTAGLIO_INTERVENTO    = DIM.ID_DETTAGLIO_INTERVENTO                     \n"
        + "   AND DIM.ID_MISURAZIONE_INTERVENTO = MI.ID_MISURAZIONE_INTERVENTO                    \n"
        + "   AND MI.ID_UNITA_MISURA            = UM.ID_UNITA_MISURA(+)                           \n"
        + "   AND DESCINT.ID_TIPO_CLASSIFICAZIONE      = TC.ID_TIPO_CLASSIFICAZIONE               \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO        = CI.ID_CATEGORIA_INTERVENTO(+)          \n"
        + "   AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "   AND NOT EXISTS                                                                      \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       *                                                                               \n"
        + "     FROM                                                                              \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "     WHERE                                                                             \n"
        + "       WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "       AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "   )                                                                                   \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = AI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "   AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE                     \n"

        + "   AND LBI.ID_DESCRIZIONE_INTERVENTO = DESCINT.ID_DESCRIZIONE_INTERVENTO               \n"
        + "   AND LBI.ID_LIVELLO = LIV.ID_LIVELLO							                      \n"
        + "   AND I.ID_PROCEDIMENTO = PROC.ID_PROCEDIMENTO					                      \n"
        + "   AND PROC.ID_BANDO = LBI.ID_BANDO								                      \n"
        
        + "   AND I.ID_INTERVENTO = DAIN.ID_INTERVENTO(+)						                  \n"
        + "   AND DAIN.ID_PROCEDIMENTO_OGGETTO = DA.ID_PROCEDIMENTO_OGGETTO(+)                    \n"
        + "   AND DAIN.PROGRESSIVO = DA.PROGRESSIVO(+)						                      \n"
        + "   AND DA.ID_DANNO = DD.ID_DANNO(+)								                      \n"

        + " UNION ALL                                                                                 \n"
        + " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   NULL AS ID_DETTAGLIO_INTERVENTO,                                                    \n"
        + "   DIPO.ID_DETT_INTERV_PROC_OGG,                                                       \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DIPO.ULTERIORI_INFORMAZIONI,                                                        \n"
        + "   TC.DESCRIZIONE AS DESC_TIPO_CLASSIFICAZIONE,                                        \n"
        + "   DIPO.IMPORTO_INVESTIMENTO,                                                          \n"
        + "   DIPO.IMPORTO_UNITARIO,                                                              \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   DIPOM.QUANTITA,                                                                     \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                                                 \n"
        + "   UM.CODICE      AS CODICE_MISURA,                                                    \n"
        + "   DIPO.FLAG_TIPO_OPERAZIONE,                                                          \n"
        + "   TA.DESCRIZIONE AS DESC_TIPO_AGGREGAZIONE,                                           \n"
        + "   DIPO.FLAG_ASSOCIATO_ALTRA_MISURA,                                                   \n"
        + "   LIV.CODICE AS OPERAZIONE,			                                                      \n"
        + "   DIPO.IMPORTO_AMMESSO,                                                           	  \n"
        + "   DIPO.PERCENTUALE_CONTRIBUTO,                                                    	  \n"
        + "   DIPO.IMPORTO_CONTRIBUTO,                                                         	  \n"
        + "   DESCINT.ID_DESC_INTERVENTO_ASSOCIATO,                                               \n"
        + "   DIPO.CUAA_BENEFICIARIO AS CUAA_PARTEC,											  \n"
        + "   DESCINT.FLAG_BENEFICIARIO,                                                          \n"

        // denominazione azienda
        + "     (                                                                                                                                            \n"
        + "       NVL(                                                                                                                                       \n"
        + "         (                                                                                                                                        \n"
        + "         SELECT                                                                                                                                   \n"
        + "           TP.DENOMINAZIONE                                                                                                                       \n"
        + "         FROM                                                                                                                                     \n"
        + "           IUF_T_PARTECIPANTE TP,                                                                                                                 \n"
        + "           IUF_T_DATI_PROCEDIMENTO TDP                                                                                                            \n"
        + "         WHERE                                                                                                                                    \n"
        + "           TDP.ID_PROCEDIMENTO_OGGETTO = DIPO.ID_PROCEDIMENTO_OGGETTO                                                                              \n"
        + "           AND TDP.ID_DATI_PROCEDIMENTO = TP.ID_DATI_PROCEDIMENTO                                                                                 \n"
        + "           AND TP.CUAA = DIPO.CUAA_BENEFICIARIO )                                                                                                   \n"
        + "         ,                                                                                                                                        \n"
        + "         (                                                                                                                                        \n"
        + "          SELECT                                                                                                                                  \n"
        + "            DA.DENOMINAZIONE                                                                                                                      \n"
        + "          FROM                                                                                                                                    \n"
        + "            SMRGAA_V_DATI_ANAGRAFICI DA                                                                                                           \n"
        + "          WHERE                                                                                                                                   \n"
        + "            TRUNC(NVL(:DATA_VALIDITA,SYSDATE)) BETWEEN DA.DATA_INIZIO_VALIDITA AND NVL(DA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
        + "            AND DIPO.CUAA_BENEFICIARIO = DA.CUAA                                                                                                    \n"
        + "            AND ROWNUM=1						                                                                                                     \n"
        + "         )                                                                                                                                        \n"
        + "       )                                                                                                                                          \n"
        + "     )AS CUAA_DESCR,                                                                                                                              \n"

        + "   DESCINT.FLAG_GESTIONE_COSTO_UNITARIO,                                               \n"
        + "   MI.ID_MISURAZIONE_INTERVENTO,                                                        \n"
        
        + "   DA.ID_DANNO_ATM,                                                        				\n"
        + "   DA.DESCRIZIONE AS DESC_DANNO,                                                        \n"
        + "   DA.PROGRESSIVO AS PROGRESSIVO_DANNO,                                                        \n"
        + "	  DD.ID_DANNO, \n"
        + "	  DD.DESCRIZIONE AS DESC_TIPO_DANNO, \n"
        + "	  DESCINT.CODICE_IDENTIFICATIVO														  \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI,                                                     \n"
        + "   IUF_D_LIVELLO LIV,			                                                      \n"
        + "   IUF_T_PROCEDIMENTO PROC,		                                                      \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "   IUF_R_DETT_INTE_PROC_OGG_MIS DIPOM,                                               \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                                    \n"
        + "   IUF_D_UNITA_MISURA UM,                                                              \n"
        + "   IUF_D_TIPO_CLASSIFICAZIONE TC,                                                      \n"
        + "   IUF_R_AGGREGAZIONE_INTERVENT AI,                                                   \n"
        + "   IUF_D_TIPO_AGGREGAZIONE TA,                                                         \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI,                                                       \n"
        
        + "   IUF_R_DANNO_ATM_INTERVENTO DAIN,                                                  \n"
        + "   IUF_T_DANNO_ATM DA,                                                       		  \n"
        + "   IUF_D_DANNO DD   																  \n"
        + " WHERE                                                                                 \n"
        + "   I.ID_DESCRIZIONE_INTERVENTO           = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + ((idIntervento == null) ? ""
            : "    AND I.ID_INTERVENTO = :ID_INTERVENTO \n")
        + "   AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO(+)                         \n"
        + "   AND DIPO.ID_INTERVENTO                = I.ID_INTERVENTO                             \n"
        + "   AND DIPO.ID_DETT_INTERV_PROC_OGG      = DIPOM.ID_DETT_INTERV_PROC_OGG               \n"
        + "   AND DIPOM.ID_MISURAZIONE_INTERVENTO   = MI.ID_MISURAZIONE_INTERVENTO                \n"
        + "   AND MI.ID_UNITA_MISURA                = UM.ID_UNITA_MISURA(+)                       \n"
        + "   AND DIPO.ID_PROCEDIMENTO_OGGETTO      = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "   AND DESCINT.ID_TIPO_CLASSIFICAZIONE   = TC.ID_TIPO_CLASSIFICAZIONE                  \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = AI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "   AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE                     \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)               \n"
        + "   AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "   AND LBI.ID_DESCRIZIONE_INTERVENTO = DESCINT.ID_DESCRIZIONE_INTERVENTO               \n"
        + "   AND LBI.ID_LIVELLO = LIV.ID_LIVELLO							                      \n"

        + "   AND I.ID_PROCEDIMENTO = PROC.ID_PROCEDIMENTO					                      \n"
        + "   AND PROC.ID_BANDO = LBI.ID_BANDO								                      \n"
        
        + "   AND I.ID_INTERVENTO = DAIN.ID_INTERVENTO(+)								          \n"
        + "   AND DAIN.ID_PROCEDIMENTO_OGGETTO = DA.ID_PROCEDIMENTO_OGGETTO(+)					  \n"
        + "   AND DAIN.PROGRESSIVO = DA.PROGRESSIVO(+)						                      \n"
        + "   AND DA.ID_DANNO = DD.ID_DANNO(+)	\n" 

        + " ORDER BY                                                                              \n"
        + "   PROGRESSIVO ASC NULLS LAST,                                                         \n"
        + "   ID_DETT_INTERV_PROC_OGG ASC,                                                        \n"
        + "   ID_MISURAZIONE_INTERVENTO ASC                                                       \n";

    final String QUERY_INVESTIMENTI = " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   DI.ID_DETTAGLIO_INTERVENTO,                                                         \n"
        + "   NULL AS ID_DETT_INTERV_PROC_OGG,                                                    \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "   TC.DESCRIZIONE AS DESC_TIPO_CLASSIFICAZIONE,                                        \n"
        + "   DI.IMPORTO_INVESTIMENTO,                                                            \n"
        + "   DI.IMPORTO_UNITARIO,                                                                \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "DI.CUAA_BENEFICIARIO  AS CUAA_PARTEC,																	  \n"
        + "DESCINT.FLAG_BENEFICIARIO,																	  \n"

        // denominazione azienda
        + "     (                                                                                                                                            \n"
        + "       NVL(                                                                                                                                       \n"
        + "         (                                                                                                                                        \n"
        + "         SELECT                                                                                                                                   \n"
        + "           TP.DENOMINAZIONE                                                                                                                       \n"
        + "         FROM                                                                                                                                     \n"
        + "           IUF_T_PARTECIPANTE TP,                                                                                                                 \n"
        + "           IUF_T_DATI_PROCEDIMENTO TDP                                                                                                            \n"
        + "         WHERE                                                                                                                                    \n"
        + "           TDP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                                                                              \n"
        + "           AND TDP.ID_DATI_PROCEDIMENTO = TP.ID_DATI_PROCEDIMENTO                                                                                 \n"
        + "           AND TP.CUAA = DI.CUAA_BENEFICIARIO )                                                                                                   \n"
        + "         ,                                                                                                                                        \n"
        + "         (                                                                                                                                        \n"
        + "          SELECT                                                                                                                                  \n"
        + "            DA.DENOMINAZIONE                                                                                                                      \n"
        + "          FROM                                                                                                                                    \n"
        + "            SMRGAA_V_DATI_ANAGRAFICI DA                                                                                                           \n"
        + "          WHERE                                                                                                                                   \n"
        + "            TRUNC(NVL(:DATA_VALIDITA,SYSDATE)) BETWEEN DA.DATA_INIZIO_VALIDITA AND NVL(DA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
        + "            AND DI.CUAA_BENEFICIARIO = DA.CUAA                                                                                                    \n"
        + "            AND ROWNUM=1						                                                                                                     \n"
        + "         )                                                                                                                                        \n"
        + "       )                                                                                                                                          \n"
        + "     )AS CUAA_DESCR,                                                                                                                              \n"

        + "   DIM.QUANTITA,                                                                       \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                                                 \n"
        + "   UM.CODICE      AS CODICE_MISURA,                                                    \n"
        + "   NULL AS FLAG_TIPO_OPERAZIONE,                                                       \n"
        + "   TA.DESCRIZIONE AS DESC_TIPO_AGGREGAZIONE,                                           \n"
        + "   DI.FLAG_ASSOCIATO_ALTRA_MISURA,                                                     \n"
        + "   DESCINT.ID_DESC_INTERVENTO_ASSOCIATO,                                                \n"
        + "	  DESCINT.CODICE_IDENTIFICATIVO														  \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_R_DETT_INTERV_MISURAZION DIM,                                                  \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                                    \n"
        + "   IUF_D_UNITA_MISURA UM,                                                              \n"
        + "   IUF_D_TIPO_CLASSIFICAZIONE TC,                                                      \n"
        + "   IUF_R_AGGREGAZIONE_INTERVENT AI,                                                   \n"
        + "   IUF_D_TIPO_AGGREGAZIONE TA,                                                         \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + " WHERE                                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "   AND I.ID_PROCEDIMENTO    = PO.ID_PROCEDIMENTO                                       \n"
        + ((idIntervento == null) ? ""
            : "    AND I.ID_INTERVENTO = :ID_INTERVENTO \n")
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "   AND I.ID_INTERVENTO               = DI.ID_INTERVENTO                                \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO   = DESCINT.ID_DESCRIZIONE_INTERVENTO               \n"
        + "   AND DI.ID_DETTAGLIO_INTERVENTO    = DIM.ID_DETTAGLIO_INTERVENTO                     \n"
        + "   AND DIM.ID_MISURAZIONE_INTERVENTO = MI.ID_MISURAZIONE_INTERVENTO                    \n"
        + "   AND MI.ID_UNITA_MISURA            = UM.ID_UNITA_MISURA(+)                           \n"
        + "   AND DESCINT.ID_TIPO_CLASSIFICAZIONE      = TC.ID_TIPO_CLASSIFICAZIONE               \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO        = CI.ID_CATEGORIA_INTERVENTO(+)          \n"
        + "   AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "   AND NOT EXISTS                                                                      \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       *                                                                               \n"
        + "     FROM                                                                              \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "     WHERE                                                                             \n"
        + "       WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "       AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "   )                                                                                   \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = AI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "   AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE                     \n"
        + " UNION                                                                                 \n"
        + " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   NULL AS ID_DETTAGLIO_INTERVENTO,                                                    \n"
        + "   DIPO.ID_DETT_INTERV_PROC_OGG,                                                       \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   DIPO.ULTERIORI_INFORMAZIONI,                                                        \n"
        + "   TC.DESCRIZIONE AS DESC_TIPO_CLASSIFICAZIONE,                                        \n"
        + "   DIPO.IMPORTO_INVESTIMENTO,                                                          \n"
        + "   DIPO.IMPORTO_UNITARIO,                                                              \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"

        + "DIPO.CUAA_BENEFICIARIO AS CUAA_PARTEC,																	  \n"
        + "DESCINT.FLAG_BENEFICIARIO,																	  \n"

        // denominazione azienda
        + "     (                                                                                                                                            \n"
        + "       NVL(                                                                                                                                       \n"
        + "         (                                                                                                                                        \n"
        + "         SELECT                                                                                                                                   \n"
        + "           TP.DENOMINAZIONE                                                                                                                       \n"
        + "         FROM                                                                                                                                     \n"
        + "           IUF_T_PARTECIPANTE TP,                                                                                                                 \n"
        + "           IUF_T_DATI_PROCEDIMENTO TDP                                                                                                            \n"
        + "         WHERE                                                                                                                                    \n"
        + "           TDP.ID_PROCEDIMENTO_OGGETTO = DIPO.ID_PROCEDIMENTO_OGGETTO                                                                              \n"
        + "           AND TDP.ID_DATI_PROCEDIMENTO = TP.ID_DATI_PROCEDIMENTO                                                                                 \n"
        + "           AND TP.CUAA = DIPO.CUAA_BENEFICIARIO )                                                                                                   \n"
        + "         ,                                                                                                                                        \n"
        + "         (                                                                                                                                        \n"
        + "          SELECT                                                                                                                                  \n"
        + "            DA.DENOMINAZIONE                                                                                                                      \n"
        + "          FROM                                                                                                                                    \n"
        + "            SMRGAA_V_DATI_ANAGRAFICI DA                                                                                                           \n"
        + "          WHERE                                                                                                                                   \n"
        + "            TRUNC(NVL(:DATA_VALIDITA,SYSDATE)) BETWEEN DA.DATA_INIZIO_VALIDITA AND NVL(DA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
        + "            AND DIPO.CUAA_BENEFICIARIO = DA.CUAA                                                                                                  \n"
        + "            AND ROWNUM=1						                                                                                                     \n"
        + "         )                                                                                                                                        \n"
        + "       )                                                                                                                                          \n"
        + "     )AS CUAA_DESCR,                                                                                                                              \n"

        + "   DIPOM.QUANTITA,                                                                     \n"
        + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                                                 \n"
        + "   UM.CODICE      AS CODICE_MISURA,                                                    \n"
        + "   DIPO.FLAG_TIPO_OPERAZIONE,                                                          \n"
        + "   TA.DESCRIZIONE AS DESC_TIPO_AGGREGAZIONE,                                           \n"
        + "   DIPO.FLAG_ASSOCIATO_ALTRA_MISURA,                                                   \n"
        + "   DESCINT.ID_DESC_INTERVENTO_ASSOCIATO,                                                \n"
        + "	  DESCINT.CODICE_IDENTIFICATIVO														  \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "   IUF_R_DETT_INTE_PROC_OGG_MIS DIPOM,                                               \n"
        + "   IUF_R_MISURAZIONE_INTERVENTO MI,                                                    \n"
        + "   IUF_D_UNITA_MISURA UM,                                                              \n"
        + "   IUF_D_TIPO_CLASSIFICAZIONE TC,                                                      \n"
        + "   IUF_R_AGGREGAZIONE_INTERVENT AI,                                                   \n"
        + "   IUF_D_TIPO_AGGREGAZIONE TA,                                                         \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + " WHERE                                                                                 \n"
        + "   I.ID_DESCRIZIONE_INTERVENTO           = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + ((idIntervento == null) ? ""
            : "    AND I.ID_INTERVENTO = :ID_INTERVENTO \n")
        + "   AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO(+)                         \n"
        + "   AND DIPO.ID_INTERVENTO                = I.ID_INTERVENTO                             \n"
        + "   AND DIPO.ID_DETT_INTERV_PROC_OGG      = DIPOM.ID_DETT_INTERV_PROC_OGG               \n"
        + "   AND DIPOM.ID_MISURAZIONE_INTERVENTO   = MI.ID_MISURAZIONE_INTERVENTO                \n"
        + "   AND MI.ID_UNITA_MISURA                = UM.ID_UNITA_MISURA(+)                       \n"
        + "   AND DIPO.ID_PROCEDIMENTO_OGGETTO      = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "   AND DESCINT.ID_TIPO_CLASSIFICAZIONE   = TC.ID_TIPO_CLASSIFICAZIONE                  \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = AI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "   AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE                     \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)               \n"
        + "   AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + " ORDER BY                                                                              \n"
        + "   PROGRESSIVO ASC NULLS LAST,                                                         \n"
        + "   ID_DETT_INTERV_PROC_OGG ASC                                                         \n";
    final boolean isInvestimenti = IuffiConstants.FLAGS.SI
        .equals(flagEscludiCatalogo);
    final String QUERY = isInvestimenti ? QUERY_INVESTIMENTI : QUERY_INTERVENTI;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO", flagEscludiCatalogo,
        Types.VARCHAR);
    mapParameterSource.addValue("DATA_VALIDITA", dataValidita, Types.DATE);

    try
    {
      return (List<RigaElencoInterventi>) namedParameterJdbcTemplate.query(
          QUERY, mapParameterSource,
          new ResultSetExtractor<List<RigaElencoInterventi>>()
          {
            @Override
            public List<RigaElencoInterventi> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<RigaElencoInterventi> list = new ArrayList<RigaElencoInterventi>();
              Long lastIdIntervento = null;
              RigaElencoInterventi rigaDTO = null;
              while (rs.next())
              {
                long idIntervento = rs.getLong("ID_INTERVENTO");
                if (lastIdIntervento == null
                    || lastIdIntervento != idIntervento)
                {
                  rigaDTO = new RigaElencoInterventi();
                  rigaDTO.setIdIntervento(idIntervento);
                  rigaDTO.setIdDettaglioIntervento(
                      getLongNull(rs, "ID_DETTAGLIO_INTERVENTO"));
                  rigaDTO.setIdDettIntervProcOgg(
                      getLongNull(rs, "ID_DETT_INTERV_PROC_OGG"));
                  rigaDTO.setProgressivo(getIntegerNull(rs, "PROGRESSIVO"));
                  rigaDTO.setImportoInvestimento(
                      rs.getBigDecimal("IMPORTO_INVESTIMENTO"));
                  rigaDTO.setCuaaPartecipante(rs.getString("CUAA_PARTEC"));
                  rigaDTO
                      .setFlagBeneficiario(rs.getString("FLAG_BENEFICIARIO"));
                  rigaDTO.setCuaaPartecipanteDenominazione(
                      rs.getString("CUAA_DESCR"));
                  rigaDTO
                      .setImportoUnitario(rs.getBigDecimal("IMPORTO_UNITARIO"));
                  rigaDTO.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                  rigaDTO.setUlterioriInformazioni(
                      rs.getString("ULTERIORI_INFORMAZIONI"));
                  rigaDTO.setDescTipoClassificazione(
                      rs.getString("DESC_TIPO_CLASSIFICAZIONE"));
                  rigaDTO.setIdTipoLocalizzazione(
                      rs.getInt("ID_TIPO_LOCALIZZAZIONE"));
                  rigaDTO.setFlagTipoOperazione(
                      rs.getString("FLAG_TIPO_OPERAZIONE"));
                  rigaDTO.setDescTipoAggregazione(
                      rs.getString("DESC_TIPO_AGGREGAZIONE"));
                  rigaDTO.setFlagAssociatoAltraMisura(
                      rs.getString("FLAG_ASSOCIATO_ALTRA_MISURA"));
                  rigaDTO.setIdDescInterventoAssociato(
                      getLongNull(rs, "ID_DESC_INTERVENTO_ASSOCIATO"));
                  rigaDTO.setCodiceIdentificativo(rs.getString("CODICE_IDENTIFICATIVO"));
                  if (!isInvestimenti)
                  {
                	 
                    rigaDTO.setOperazione(rs.getString("OPERAZIONE"));

                    BigDecimal iVal = rs.getBigDecimal("IMPORTO_CONTRIBUTO");
                    if (!rs.wasNull())
                    {
                      // handle NULL field value
                      rigaDTO.setContributoConcesso(iVal);
                    }

                    iVal = rs.getBigDecimal("PERCENTUALE_CONTRIBUTO");
                    if (!rs.wasNull())
                    {
                      rigaDTO.setPercentualeContributo(iVal);
                    }

                    iVal = rs.getBigDecimal("IMPORTO_AMMESSO");
                    if (!rs.wasNull())
                    {
                      rigaDTO.setSpesaAmmessa(iVal);
                    }
                    rigaDTO.setInterventoModificabileASaldo(
                        !IuffiConstants.FLAGS.SI.equals(
                            rs.getString("FLAG_GESTIONE_COSTO_UNITARIO")) &&
                            IuffiConstants.INTERVENTI.LOCALIZZAZIONE.PARTICELLE_AZIENDALI_IMPIANTI_BOSCHIVI != rs
                                .getInt("ID_TIPO_LOCALIZZAZIONE"));
                  }

                  lastIdIntervento = idIntervento;
                  rigaDTO.setMisurazioni(
                      new ArrayList<InfoMisurazioneIntervento>());
                  if(getLongNull(rs, "ID_DANNO_ATM") != null)
                  {
                	  rigaDTO.setIdDannoAtm(getLongNull(rs, "ID_DANNO_ATM"));
                	  rigaDTO.setDescDanno(rs.getString("DESC_DANNO"));
                	  rigaDTO.setIdDanno(rs.getLong("ID_DANNO"));
                	  rigaDTO.setDescTipoDanno(rs.getString("DESC_TIPO_DANNO"));
                	  rigaDTO.setProgressivoDanno(rs.getLong("PROGRESSIVO_DANNO"));
                  }
                  list.add(rigaDTO);
                }
                InfoMisurazioneIntervento m = new InfoMisurazioneIntervento();
                m.setDescMisurazione(rs.getString("DESC_MISURAZIONE"));
                final String codiceUnitaMisura = rs.getString("CODICE_MISURA");
                m.setCodiceUnitaMisura(codiceUnitaMisura);
                rigaDTO.enforceInterventoModificabileASaldo(
                    !InfoMisurazioneIntervento.CODICE_NESSUNA_MISURAZIONE
                        .equals(codiceUnitaMisura));
                m.setValore(rs.getBigDecimal("QUANTITA"));
                rigaDTO.getMisurazioni().add(m);
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("flagEscludiCatalogo", flagEscludiCatalogo)
          },
          new LogVariable[]
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

  public List<RigaJSONInterventoQuadroEconomicoDTO> getElencoInterventiQuadroEconomico(
      long idProcedimentoOggetto, Date dataValidita)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoInterventiQuadroEconomico]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                                                                     \n"
        + "         I.ID_INTERVENTO,                                                                    \n"
        + "         DI.ID_DETTAGLIO_INTERVENTO,                                                         \n"
        + "         NULL AS ID_DETT_INTERV_PROC_OGG,                                                    \n"
        + "         DI.PROGRESSIVO,                                                                     \n"
        + "         DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "         DI.IMPORTO_INVESTIMENTO,                                                            \n"
        + "         DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"

        + "   DI.CUAA_BENEFICIARIO AS CUAA_PARTEC,												  \n"
        + "   DESCINT.FLAG_BENEFICIARIO,                                               			  \n"

        // denominazione azienda
        + "     (                                                                                                                                            \n"
        + "       NVL(                                                                                                                                       \n"
        + "         (                                                                                                                                        \n"
        + "         SELECT                                                                                                                                   \n"
        + "           TP.DENOMINAZIONE                                                                                                                       \n"
        + "         FROM                                                                                                                                     \n"
        + "           IUF_T_PARTECIPANTE TP,                                                                                                                 \n"
        + "           IUF_T_DATI_PROCEDIMENTO TDP                                                                                                            \n"
        + "         WHERE                                                                                                                                    \n"
        + "           TDP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                                                                               \n"
        + "           AND TDP.ID_DATI_PROCEDIMENTO = TP.ID_DATI_PROCEDIMENTO                                                                                 \n"
        + "           AND TP.CUAA = DI.CUAA_BENEFICIARIO )                                                                                                   \n"
        + "         ,                                                                                                                                        \n"
        + "         (                                                                                                                                        \n"
        + "          SELECT                                                                                                                                  \n"
        + "            DA.DENOMINAZIONE                                                                                                                      \n"
        + "          FROM                                                                                                                                    \n"
        + "            SMRGAA_V_DATI_ANAGRAFICI DA                                                                                                           \n"
        + "          WHERE                                                                                                                                   \n"
        + "            TRUNC(NVL(:DATA_VALIDITA,SYSDATE)) BETWEEN DA.DATA_INIZIO_VALIDITA AND NVL(DA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
        + "            AND DI.CUAA_BENEFICIARIO = DA.CUAA                                                                                                    \n"
        + "            AND ROWNUM=1						                                                                                                     \n"
        + "         )                                                                                                                                        \n"
        + "       )                                                                                                                                          \n"
        + "     )AS CUAA_DESCR,                                                                                                                              \n"

        + "         NULL AS FLAG_TIPO_OPERAZIONE,                                                       \n"
        + "         DI.IMPORTO_AMMESSO,                                                                 \n"
        + "         DI.PERCENTUALE_CONTRIBUTO,                                                          \n"
        + "         DI.IMPORTO_CONTRIBUTO                                                               \n"
        + "       FROM                                                                                  \n"
        + "         IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "         IUF_T_INTERVENTO I,                                                                 \n"
        + "         IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "         IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "         IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + "       WHERE                                                                                 \n"
        + "         PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "         AND I.ID_PROCEDIMENTO    = PO.ID_PROCEDIMENTO                                       \n"
        + "         AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "         AND I.ID_INTERVENTO               = DI.ID_INTERVENTO                                \n"
        + "         AND I.ID_DESCRIZIONE_INTERVENTO   = DESCINT.ID_DESCRIZIONE_INTERVENTO               \n"
        + "         AND DESCINT.ID_CATEGORIA_INTERVENTO        = CI.ID_CATEGORIA_INTERVENTO(+)          \n"
        + "         AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "         AND NOT EXISTS                                                                      \n"
        + "         (                                                                                   \n"
        + "           SELECT                                                                            \n"
        + "             *                                                                               \n"
        + "           FROM                                                                              \n"
        + "             IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "           WHERE                                                                             \n"
        + "             WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "             AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "         )                                                                                   \n"
        + "       UNION                                                                                 \n"
        + "       SELECT                                                                                \n"
        + "         I.ID_INTERVENTO,                                                                    \n"
        + "         NULL AS ID_DETTAGLIO_INTERVENTO,                                                    \n"
        + "         DIPO.ID_DETT_INTERV_PROC_OGG,                                                       \n"
        + "         DI.PROGRESSIVO,                                                                     \n"
        + "         DIPO.ULTERIORI_INFORMAZIONI,                                                        \n"
        + "         DIPO.IMPORTO_INVESTIMENTO,                                                          \n"
        + "         DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"

        + "   DIPO.CUAA_BENEFICIARIO AS CUAA_PARTEC,												  \n"
        + "   DESCINT.FLAG_BENEFICIARIO,                                               			  \n"

        // denominazione azienda
        + "     (                                                                                                                                            \n"
        + "       NVL(                                                                                                                                       \n"
        + "         (                                                                                                                                        \n"
        + "         SELECT                                                                                                                                   \n"
        + "           TP.DENOMINAZIONE                                                                                                                       \n"
        + "         FROM                                                                                                                                     \n"
        + "           IUF_T_PARTECIPANTE TP,                                                                                                                 \n"
        + "           IUF_T_DATI_PROCEDIMENTO TDP                                                                                                            \n"
        + "         WHERE                                                                                                                                    \n"
        + "           TDP.ID_PROCEDIMENTO_OGGETTO = DIPO.ID_PROCEDIMENTO_OGGETTO                                                                               \n"
        + "           AND TDP.ID_DATI_PROCEDIMENTO = TP.ID_DATI_PROCEDIMENTO                                                                                 \n"
        + "           AND TP.CUAA = DIPO.CUAA_BENEFICIARIO )                                                                                                   \n"
        + "         ,                                                                                                                                        \n"
        + "         (                                                                                                                                        \n"
        + "          SELECT                                                                                                                                  \n"
        + "            DA.DENOMINAZIONE                                                                                                                      \n"
        + "          FROM                                                                                                                                    \n"
        + "            SMRGAA_V_DATI_ANAGRAFICI DA                                                                                                           \n"
        + "          WHERE                                                                                                                                   \n"
        + "            TRUNC(NVL(:DATA_VALIDITA,SYSDATE)) BETWEEN DA.DATA_INIZIO_VALIDITA AND NVL(DA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
        + "            AND DIPO.CUAA_BENEFICIARIO = DA.CUAA                                                                                                    \n"
        + "            AND ROWNUM=1						                                                                                                     \n"
        + "         )                                                                                                                                        \n"
        + "       )                                                                                                                                          \n"
        + "     )AS CUAA_DESCR,                                                                                                                              \n"

        + "         DIPO.FLAG_TIPO_OPERAZIONE,                                                          \n"
        + "         DIPO.IMPORTO_AMMESSO,                                                               \n"
        + "         DIPO.PERCENTUALE_CONTRIBUTO,                                                        \n"
        + "         DIPO.IMPORTO_CONTRIBUTO                                                             \n"
        + "       FROM                                                                                  \n"
        + "         IUF_T_INTERVENTO I,                                                                 \n"
        + "         IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "         IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "         IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "         IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + "       WHERE                                                                                 \n"
        + "         I.ID_DESCRIZIONE_INTERVENTO           = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + "         AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO(+)                         \n"
        + "         AND DIPO.ID_INTERVENTO                = I.ID_INTERVENTO                             \n"
        + "         AND DIPO.ID_PROCEDIMENTO_OGGETTO      = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "         AND DIPO.FLAG_TIPO_OPERAZIONE        <> :FLAG_TIPO_OPERAZIONE                       \n"
        + "         AND DESCINT.ID_CATEGORIA_INTERVENTO  = CI.ID_CATEGORIA_INTERVENTO(+)                \n"
        + "         AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "       ORDER BY                                                                              \n"
        + "         PROGRESSIVO ASC NULLS LAST,                                                         \n"
        + "         ID_DETT_INTERV_PROC_OGG ASC                                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_TIPO_OPERAZIONE",
        IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_ELIMINAZIONE,
        Types.VARCHAR);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.NO, Types.VARCHAR);
    mapParameterSource.addValue("DATA_VALIDITA", dataValidita, Types.DATE);

    try
    {
      return (List<RigaJSONInterventoQuadroEconomicoDTO>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<List<RigaJSONInterventoQuadroEconomicoDTO>>()
              {
                @Override
                public List<RigaJSONInterventoQuadroEconomicoDTO> extractData(
                    ResultSet rs)
                    throws SQLException, DataAccessException
                {
                  List<RigaJSONInterventoQuadroEconomicoDTO> list = new ArrayList<RigaJSONInterventoQuadroEconomicoDTO>();
                  RigaJSONInterventoQuadroEconomicoDTO rigaDTO = null;
                  while (rs.next())
                  {
                    long idIntervento = rs.getLong("ID_INTERVENTO");
                    rigaDTO = new RigaJSONInterventoQuadroEconomicoDTO();
                    rigaDTO.setIdIntervento(idIntervento);
                    rigaDTO.setIdDettaglioIntervento(
                        getLongNull(rs, "ID_DETTAGLIO_INTERVENTO"));
                    rigaDTO.setIdDettIntervProcOgg(
                        getLongNull(rs, "ID_DETT_INTERV_PROC_OGG"));
                    rigaDTO.setProgressivo(getIntegerNull(rs, "PROGRESSIVO"));
                    rigaDTO.setImportoInvestimento(
                        rs.getBigDecimal("IMPORTO_INVESTIMENTO"));
                    rigaDTO.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                    rigaDTO.setUlterioriInformazioni(
                        rs.getString("ULTERIORI_INFORMAZIONI"));
                    rigaDTO.setFlagTipoOperazione(
                        rs.getString("FLAG_TIPO_OPERAZIONE"));
                    rigaDTO
                        .setImportoAmmesso(rs.getBigDecimal("IMPORTO_AMMESSO"));
                    rigaDTO.setCuaaPartecipante(rs.getString("CUAA_PARTEC"));
                    rigaDTO
                        .setFlagBeneficiario(rs.getString("FLAG_BENEFICIARIO"));
                    rigaDTO.setCuaaPartecipanteDenominazione(
                        rs.getString("CUAA_DESCR"));
                    rigaDTO.setPercentualeContributo(
                        rs.getBigDecimal("PERCENTUALE_CONTRIBUTO"));
                    rigaDTO.setImportoContributo(
                        rs.getBigDecimal("IMPORTO_CONTRIBUTO"));
                    list.add(rigaDTO);
                  }
                  return list;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
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

  public Map<Long, StringBuilder> getMapLocalizzazioneComuniInterventi(
      long idProcedimentoOggetto,
      String flagEscludiCatalogo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getMapLocalizzazioneComuniInterventi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DECODE (DG.DESCRIZIONE_COMUNE, NULL, NULL, DG.DESCRIZIONE_COMUNE                    \n"
        + "   || ' ('                                                                             \n"
        + "   || DG.SIGLA_PROVINCIA                                                               \n"
        + "   || ')') AS DESC_COMUNE                                                              \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_T_LOCALIZZAZIONE_INTERV LI,                                                     \n"
        + "   SMRGAA_V_DATI_AMMINISTRATIVI DG,                                                    \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + " WHERE                                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "   AND I.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                                     \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO           = DESCINT.ID_DESCRIZIONE_INTERVENTO        \n"
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "   AND I.ID_INTERVENTO                 = DI.ID_INTERVENTO                              \n"
        + "   AND DI.ID_DETTAGLIO_INTERVENTO      = LI.ID_DETTAGLIO_INTERVENTO                    \n"
        + "   AND LI.ISTAT_COMUNE                 = DG.ISTAT_COMUNE                               \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO        = CI.ID_CATEGORIA_INTERVENTO(+)          \n"
        + "   AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "   AND NOT EXISTS                                                                      \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       *                                                                               \n"
        + "     FROM                                                                              \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "     WHERE                                                                             \n"
        + "       WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "       AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "   )                                                                                   \n"
        + " UNION                                                                                 \n"
        + " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DECODE (DG.DESCRIZIONE_COMUNE, NULL, NULL, DG.DESCRIZIONE_COMUNE                    \n"
        + "   || ' ('                                                                             \n"
        + "   || DG.SIGLA_PROVINCIA                                                               \n"
        + "   || ')') AS DESC_COMUNE                                                              \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "   IUF_W_LOCAL_INTERV_PROC_OGG LIPO,                                                   \n"
        + "   SMRGAA_V_DATI_AMMINISTRATIVI DG,                                                    \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + " WHERE                                                                                 \n"
        + "   DIPO.ID_INTERVENTO                  = I.ID_INTERVENTO                               \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO           = DESCINT.ID_DESCRIZIONE_INTERVENTO        \n"
        + "   AND DIPO.ID_PROCEDIMENTO_OGGETTO    = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "   AND DIPO.ID_DETT_INTERV_PROC_OGG    = LIPO.ID_DETT_INTERV_PROC_OGG                  \n"
        + "   AND LIPO.ISTAT_COMUNE               = DG.ISTAT_COMUNE                               \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO        = CI.ID_CATEGORIA_INTERVENTO(+)          \n"
        + "   AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + " ORDER BY                                                                              \n"
        + "   ID_INTERVENTO, DESC_COMUNE                                                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO", flagEscludiCatalogo,
        Types.VARCHAR);
    try
    {
      return (Map<Long, StringBuilder>) namedParameterJdbcTemplate.query(QUERY,
          mapParameterSource,
          new ResultSetExtractor<Map<Long, StringBuilder>>()
          {
            @Override
            public Map<Long, StringBuilder> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<Long, StringBuilder> map = new HashMap<Long, StringBuilder>();
              while (rs.next())
              {
                final long idIntervento = rs.getLong("ID_INTERVENTO");
                final String descComune = rs.getString("DESC_COMUNE");
                StringBuilder sb = map.get(idIntervento);
                if (sb == null)
                {
                  sb = new StringBuilder();
                  map.put(idIntervento, sb);
                }
                else
                {
                  sb.append("<br />");
                }
                sb.append(descComune);
              }
              return map;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("flagEscludiCatalogo", flagEscludiCatalogo)
          },
          new LogVariable[]
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

  public List<DecodificaDTO<String>> geLocalizzazioneComuniIntervento(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getMapLocalizzazioneComuniInterventi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                  \n"
        + "   ISTAT_COMUNI AS                                                     \n"
        + "   (                                                                   \n"
        + "     SELECT                                                            \n"
        + "       LI.ISTAT_COMUNE                                                 \n"
        + "     FROM                                                              \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                  \n"
        + "       IUF_T_LOCALIZZAZIONE_INTERV LI                                  \n"
        + "     WHERE                                                             \n"
        + "       DI.ID_INTERVENTO               = :ID_INTERVENTO                 \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = LI.ID_DETTAGLIO_INTERVENTO     \n"
        + "       AND NOT EXISTS                                                  \n"
        + "       (                                                               \n"
        + "         SELECT                                                        \n"
        + "           *                                                           \n"
        + "         FROM                                                          \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                             \n"
        + "         WHERE                                                         \n"
        + "           WTMP.ID_INTERVENTO               = DI.ID_INTERVENTO         \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "       )                                                               \n"
        + "     UNION                                                             \n"
        + "     SELECT                                                            \n"
        + "       LIPO.ISTAT_COMUNE                                               \n"
        + "     FROM                                                              \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                \n"
        + "       IUF_W_LOCAL_INTERV_PROC_OGG LIPO                                \n"
        + "     WHERE                                                             \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO               \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO     \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = LIPO.ID_DETT_INTERV_PROC_OGG \n"
        + "   )                                                                   \n"
        + " SELECT                                                                \n"
        + "   DA.ISTAT_COMUNE AS ID,                                              \n"
        + "   DA.SIGLA_PROVINCIA AS CODICE,                                       \n"
        + "   DA.DESCRIZIONE_COMUNE AS DESCRIZIONE                                \n"
        + " FROM                                                                  \n"
        + "   ISTAT_COMUNI IC,                                                    \n"
        + "   SMRGAA_V_DATI_AMMINISTRATIVI DA                                     \n"
        + " WHERE                                                                 \n"
        + "   IC.ISTAT_COMUNE = DA.ISTAT_COMUNE                                   \n"
        + " ORDER BY                                                              \n"
        + "   DA.DESCRIZIONE_COMUNE                                               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
    try
    {
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento)
          },
          new LogVariable[]
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

  public void insertLocalizzazioneComuni(long idDettIntervProcOgg, String[] ids)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertLocalizzazioneComuni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                                        \n"
        + " INTO                                                          \n"
        + "   IUF_W_LOCAL_INTERV_PROC_OGG                                 \n"
        + "   (                                                           \n"
        + "     ID_LOCAL_INTERV_PROC_OGG,                                 \n"
        + "     ID_DETT_INTERV_PROC_OGG,                                  \n"
        + "     ISTAT_COMUNE                                              \n"
        + "   )                                                           \n"
        + "   (                                                           \n"
        + "     SELECT                                                    \n"
        + "       SEQ_IUF_W_LOCAL_INTE_PROC_OG.NEXTVAL,                 \n"
        + "       DIPO.ID_DETT_INTERV_PROC_OGG,                           \n"
        + "       :ISTAT                                                  \n"
        + "     FROM                                                      \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO                         \n"
        + "     WHERE                                                     \n"
        + "       DIPO.ID_DETT_INTERV_PROC_OGG = :ID_DETT_INTERV_PROC_OGG \n"
        + "   )                                                           \n";

    int length = ids.length;
    MapSqlParameterSource[] batchParameters = new MapSqlParameterSource[length];
    try
    {
      int idx = 0;
      for (String istat : ids)
      {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
            idDettIntervProcOgg, Types.NUMERIC);
        mapParameterSource.addValue("ISTAT", istat, Types.VARCHAR);
        batchParameters[idx++] = mapParameterSource;
      }
      namedParameterJdbcTemplate.batchUpdate(INSERT, batchParameters);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("ids", ids)
          },
          (LogVariable[]) null, INSERT, batchParameters);
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

  public Long findIdWDettIntervProcOgg(long idProcedimentoOggetto,
      long idIntervento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::findWDettIntervProcOgg]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                    \n"
        + "   DIPO.ID_DETT_INTERV_PROC_OGG                            \n"
        + " FROM                                                      \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG DIPO                         \n"
        + " WHERE                                                     \n"
        + "   DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "   AND DIPO.ID_INTERVENTO       = :ID_INTERVENTO           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.INTEGER);
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.INTEGER);
    try
    {
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idIntervento", idIntervento), new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, null, QUERY, mapParameterSource);
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

  public Long findIdDettaglioMisurazioneIntervento(long idProcedimentoOggetto,
      long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::findIdDettaglioMisurazioneIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                \n"
        + "   DI.ID_DETTAGLIO_INTERVENTO                                                          \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI                                                       \n"
        + " WHERE                                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "   AND DI.ID_INTERVENTO       = :ID_INTERVENTO                                         \n"
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggeto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento)
          },
          null, QUERY, mapParameterSource);
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

  public int copiaMisurazioneInterventoSuTemporaneo(long idDettaglioIntervento,
      long idDettIntervProcOgg)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::copiaMisurazioneInterventoSuTemporaneo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                                       \n"
        + " INTO                                                         \n"
        + "   IUF_R_DETT_INTE_PROC_OGG_MIS                             \n"
        + "   (                                                          \n"
        + "     ID_DETT_INTERV_PROC_OGG,                                 \n"
        + "     ID_MISURAZIONE_INTERVENTO,                               \n"
        + "     QUANTITA                                                 \n"
        + "   )                                                          \n"
        + "   (                                                          \n"
        + "     SELECT                                                   \n"
        + "       :ID_DETT_INTERV_PROC_OGG,                              \n"
        + "       DIM.ID_MISURAZIONE_INTERVENTO,                         \n"
        + "       DIM.QUANTITA                                           \n"
        + "     FROM                                                     \n"
        + "       IUF_R_DETT_INTERV_MISURAZION DIM                      \n"
        + "     WHERE                                                    \n"
        + "       DIM.ID_DETTAGLIO_INTERVENTO = :ID_DETTAGLIO_INTERVENTO \n"
        + "   )                                                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETTAGLIO_INTERVENTO",
          idDettaglioIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettaglioIntervento", idDettaglioIntervento),
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg)
          }, null, INSERT,
          mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD + "");
      throw e;
    }
  }

  public int copiaAllegatiInterventoSuTemporaneo(long idDettaglioIntervento,
      long idDettIntervProcOgg,
      Long idFileAllegatiInterventoDaEscludere)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::copiaAllegatiInterventoSuTemporaneo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                                        \n"
        + " INTO                                                          \n"
        + "   IUF_W_FILE_ALL_INTE_PROC_OGG                              \n"
        + "   (                                                           \n"
        + "     ID_DETT_INTERV_PROC_OGG,                                  \n"
        + "     ID_FILE_ALLEGATI_INTERVENTO                               \n"
        + "   )                                                           \n"
        + "   (                                                           \n"
        + "     SELECT                                                    \n"
        + "     :ID_DETT_INTERV_PROC_OGG,                                 \n"
        + "      FADI.ID_FILE_ALLEGATI_INTERVENTO                         \n"
        + "     FROM                                                      \n"
        + "       IUF_R_FILE_ALLEGATI_DETT_INT FADI                     \n"
        + "     WHERE                                                     \n"
        + "       FADI.ID_DETTAGLIO_INTERVENTO = :ID_DETTAGLIO_INTERVENTO \n"
        + "       AND FADI.ID_FILE_ALLEGATI_INTERVENTO <> :ID_ESCLUSO     \n"
        + "   )                                                           \n"
        + "                                                               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETTAGLIO_INTERVENTO",
          idDettaglioIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_ESCLUSO",
          idFileAllegatiInterventoDaEscludere, Types.NUMERIC);
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettaglioIntervento", idDettaglioIntervento),
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("idFileAllegatiInterventoDaEscludere",
                  idFileAllegatiInterventoDaEscludere)
          }, null, INSERT,
          mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD + "");
      throw e;
    }
  }

  public int copiaLocalizzazioneInterventoSuTemporaneo(
      long idDettaglioIntervento, long idDettIntervProcOgg)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::copiaMisurazioneInterventoSuTemporaneo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                                      \n"
        + " INTO                                                        \n"
        + "   IUF_W_LOCAL_INTERV_PROC_OGG                               \n"
        + "   (                                                         \n"
        + "     ID_LOCAL_INTERV_PROC_OGG,                               \n"
        + "     ID_DETT_INTERV_PROC_OGG,                                \n"
        + "     EXT_ID_CONDUZIONE_DICHIARATA,                           \n"
        + "     EXT_ID_PARTICELLA_CERTIFICATA,                          \n"
        + "     EXT_ID_UTILIZZO_DICHIARATO,                             \n"
        + "     ISTAT_COMUNE,                                           \n"
        + "     SUPERFICIE_IMPEGNO,                                     \n"
        + "     SUPERFICIE_EFFETTIVA,                                   \n"
        + "     SUPERFICIE_ISTRUTTORIA,                                 \n"
        + "     SUPERFICIE_ACCERTATA_GIS                                \n"
        + "   )                                                         \n"
        + "   (                                                         \n"
        + "     SELECT                                                  \n"
        + "       SEQ_IUF_W_LOCAL_INTE_PROC_OG.NEXTVAL,               \n"
        + "       :ID_DETT_INTERV_PROC_OGG,                             \n"
        + "       LI.EXT_ID_CONDUZIONE_DICHIARATA,                      \n"
        + "       LI.EXT_ID_PARTICELLA_CERTIFICATA,                     \n"
        + "       LI.EXT_ID_UTILIZZO_DICHIARATO,                        \n"
        + "       LI.ISTAT_COMUNE,                                      \n"
        + "       LI.SUPERFICIE_IMPEGNO,                                \n"
        + "       LI.SUPERFICIE_EFFETTIVA,                              \n"
        + "       LI.SUPERFICIE_ISTRUTTORIA,                            \n"
        + "       LI.SUPERFICIE_ACCERTATA_GIS                           \n"
        + "     FROM                                                    \n"
        + "       IUF_T_LOCALIZZAZIONE_INTERV LI                        \n"
        + "     WHERE                                                   \n"
        + "       LI.ID_DETTAGLIO_INTERVENTO = :ID_DETTAGLIO_INTERVENTO \n"
        + "   )                                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETTAGLIO_INTERVENTO",
          idDettaglioIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettaglioIntervento", idDettaglioIntervento),
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg)
          }, null, INSERT,
          mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD + "");
      throw e;
    }
  }

  public Map<String, Map<String, String>> getMapComuniPiemontesiNonInInterventoForJSON(
      long idProcedimentoOggetto,
      long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getMapComuniPiemontesiNonInInterventoForJSON]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                      \n"
        + "   ISTAT AS                                                                                \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       LI.ISTAT_COMUNE                                                                     \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "       IUF_T_LOCALIZZAZIONE_INTERV LI                                                      \n"
        + "     WHERE                                                                                 \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "       AND DI.ID_INTERVENTO           = :ID_INTERVENTO                                     \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = LI.ID_DETTAGLIO_INTERVENTO                         \n"
        + "       AND NOT EXISTS                                                                      \n"
        + "       (                                                                                   \n"
        + "         SELECT                                                                            \n"
        + "           *                                                                               \n"
        + "         FROM                                                                              \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "         WHERE                                                                             \n"
        + "           WTMP.ID_INTERVENTO               = DI.ID_INTERVENTO                             \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = DI.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       )                                                                                   \n"
        + "     UNION                                                                                 \n"
        + "     SELECT                                                                                \n"
        + "       LIPO.ISTAT_COMUNE                                                                   \n"
        + "     FROM                                                                                  \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "       IUF_W_LOCAL_INTERV_PROC_OGG LIPO                                                    \n"
        + "     WHERE                                                                                 \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                   \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                         \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = LIPO.ID_DETT_INTERV_PROC_OGG                     \n"
        + "   )                                                                                       \n"
        + " SELECT                                                                                    \n"
        + "   DG.ISTAT_COMUNE,                                                                        \n"
        + "   DG.DESCRIZIONE_COMUNE                                                                   \n"
        + "   || ' ('                                                                                 \n"
        + "   || DG.SIGLA_PROVINCIA                                                                   \n"
        + "   || ')'                   AS DESC_COMUNE,                                                \n"
        + "   DG.DESCRIZIONE_PROVINCIA AS GRUPPO                                                      \n"
        + " FROM                                                                                      \n"
        + "   SMRGAA_V_DATI_AMMINISTRATIVI DG                                                         \n"
        + " WHERE                                                                                     \n"
        + "   DG.ID_REGIONE       = '01'                                                              \n"
        + "   AND DG.FLAG_ESTINTO = 'N'                                                               \n"
        + "   AND NOT EXISTS                                                                          \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       *                                                                                   \n"
        + "     FROM                                                                                  \n"
        + "       ISTAT                                                                               \n"
        + "     WHERE                                                                                 \n"
        + "       ISTAT.ISTAT_COMUNE = DG.ISTAT_COMUNE                                                \n"
        + "   )                                                                                       \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
    try
    {
      return (Map<String, Map<String, String>>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<Map<String, Map<String, String>>>()
              {
                @Override
                public Map<String, Map<String, String>> extractData(
                    ResultSet rs) throws SQLException, DataAccessException
                {
                  Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
                  while (rs.next())
                  {
                    Map<String, String> element = new HashMap<String, String>();
                    String istatComune = rs.getString("ISTAT_COMUNE");
                    element.put("istatComune", istatComune);
                    element.put("descrizioneComune",
                        rs.getString("DESC_COMUNE"));
                    element.put("gruppo", rs.getString("GRUPPO"));
                    map.put(istatComune, element);
                  }
                  return map;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento)
          },
          new LogVariable[]
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

  public Map<String, Map<String, String>> getMapComuniParticelleNonInInterventoForJSON(
      long idProcedimentoOggetto,
      long idIntervento, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getMapComuniParticelleNonInInterventoForJSON]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                      \n"
        + "   ISTAT AS                                                                                \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       LI.ISTAT_COMUNE                                                                     \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "       IUF_T_LOCALIZZAZIONE_INTERV LI                                                      \n"
        + "     WHERE                                                                                 \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "       AND DI.ID_INTERVENTO           = :ID_INTERVENTO                                     \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = LI.ID_DETTAGLIO_INTERVENTO                         \n"
        + "       AND NOT EXISTS                                                                      \n"
        + "       (                                                                                   \n"
        + "         SELECT                                                                            \n"
        + "           *                                                                               \n"
        + "         FROM                                                                              \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "         WHERE                                                                             \n"
        + "           WTMP.ID_INTERVENTO               = DI.ID_INTERVENTO                             \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = DI.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       )                                                                                   \n"
        + "     UNION                                                                                 \n"
        + "     SELECT                                                                                \n"
        + "       LIPO.ISTAT_COMUNE                                                                   \n"
        + "     FROM                                                                                  \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "       IUF_W_LOCAL_INTERV_PROC_OGG LIPO                                                    \n"
        + "     WHERE                                                                                 \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                   \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                         \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = LIPO.ID_DETT_INTERV_PROC_OGG                     \n"
        + "   )                                                                                       \n"
        + "   ,                                                                                       \n"
        + "   DICH AS                                                                                 \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       MAX(DC.ID_DICHIARAZIONE_CONSISTENZA) AS ID_DICHIARAZIONE_CONSISTENZA                \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "       IUF_T_PROCEDIMENTO_AZIENDA PA,                                                      \n"
        + "       SMRGAA_V_DICH_CONSISTENZA DC                                                        \n"
        + "     WHERE                                                                                 \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "       AND PO.ID_PROCEDIMENTO     = PA.ID_PROCEDIMENTO                                     \n"
        + "       AND PA.DATA_FINE          IS NULL                                                   \n"
        + "       AND DC.ID_AZIENDA          = PA.EXT_ID_AZIENDA                                      \n"
        + "       AND DC.ID_PROCEDIMENTO     = :ID_PROCEDIMENTO                                       \n"
        + "   )                                                                                       \n"
        + " SELECT DISTINCT                                                                           \n"
        + "   DG.ISTAT_COMUNE,                                                                        \n"
        + "   DG.DESCRIZIONE_COMUNE                                                                   \n"
        + "   || ' ('                                                                                 \n"
        + "   || DG.SIGLA_PROVINCIA                                                                   \n"
        + "   || ')'                   AS DESC_COMUNE,                                                \n"
        + "   DG.DESCRIZIONE_PROVINCIA AS GRUPPO                                                      \n"
        + " FROM                                                                                      \n"
        + "   SMRGAA_V_CONDUZIONE_UTILIZZO CU,                                                        \n"
        + "   SMRGAA_V_DATI_AMMINISTRATIVI DG,                                                        \n"
        + "   DICH                                                                                    \n"
        + " WHERE                                                                                     \n"
        + "   CU.ID_DICHIARAZIONE_CONSISTENZA = DICH.ID_DICHIARAZIONE_CONSISTENZA                     \n"
        + "   AND CU.COMUNE                   = DG.ISTAT_COMUNE                                       \n"
        + "   AND NOT EXISTS                                                                          \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       *                                                                                   \n"
        + "     FROM                                                                                  \n"
        + "       ISTAT                                                                               \n"
        + "     WHERE                                                                                 \n"
        + "       ISTAT.ISTAT_COMUNE = DG.ISTAT_COMUNE                                                \n"
        + "   )                                                                                       \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_PROCEDIMENTO",
        idProcedimentoAgricoltura, Types.NUMERIC);
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
    try
    {
      return (Map<String, Map<String, String>>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<Map<String, Map<String, String>>>()
              {
                @Override
                public Map<String, Map<String, String>> extractData(
                    ResultSet rs) throws SQLException, DataAccessException
                {
                  Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
                  while (rs.next())
                  {
                    Map<String, String> element = new HashMap<String, String>();
                    String istatComune = rs.getString("ISTAT_COMUNE");
                    element.put("istatComune", istatComune);
                    element.put("descrizioneComune",
                        rs.getString("DESC_COMUNE"));
                    element.put("gruppo", rs.getString("GRUPPO"));
                    map.put(istatComune, element);
                  }
                  return map;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento)
          },
          new LogVariable[]
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

  public Map<String, Map<String, String>> getMapComuniUteNonInInterventoForJSON(
      long idProcedimentoOggetto,
      long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getMapComuniUteNonInInterventoForJSON]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                       \n"
        + "   ISTAT AS                                                                                 \n"
        + "   (                                                                                        \n"
        + "     SELECT                                                                                 \n"
        + "       LI.ISTAT_COMUNE                                                                      \n"
        + "     FROM                                                                                   \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                       \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                       \n"
        + "       IUF_T_LOCALIZZAZIONE_INTERV LI                                                       \n"
        + "     WHERE                                                                                  \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE)  \n"
        + "       AND DI.ID_INTERVENTO           = :ID_INTERVENTO                                      \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = LI.ID_DETTAGLIO_INTERVENTO                          \n"
        + "       AND NOT EXISTS                                                                       \n"
        + "       (                                                                                    \n"
        + "         SELECT                                                                             \n"
        + "           *                                                                                \n"
        + "         FROM                                                                               \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                  \n"
        + "         WHERE                                                                              \n"
        + "           WTMP.ID_INTERVENTO               = DI.ID_INTERVENTO                              \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = DI.ID_PROCEDIMENTO_OGGETTO                    \n"
        + "       )                                                                                    \n"
        + "     UNION                                                                                  \n"
        + "     SELECT                                                                                 \n"
        + "       LIPO.ISTAT_COMUNE                                                                    \n"
        + "     FROM                                                                                   \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                     \n"
        + "       IUF_W_LOCAL_INTERV_PROC_OGG LIPO                                                     \n"
        + "     WHERE                                                                                  \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                    \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                          \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = LIPO.ID_DETT_INTERV_PROC_OGG                      \n"
        + "   )                                                                                        \n"
        + " SELECT                                                                                     \n"
        + "   DISTINCT U.ISTAT_COMUNE,                                                                 \n"
        + "   U.DESCRIZIONE_COMUNE                                                                     \n"
        + "   || ' ('                                                                                  \n"
        + "   || U.SIGLA_PROVINCIA                                                                     \n"
        + "   || ')'                  AS DESC_COMUNE,                                                  \n"
        + "   U.DESC_PROVINCIA AS GRUPPO                                                               \n"
        + " FROM                                                                                       \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                           \n"
        + "   IUF_T_PROCEDIMENTO_AZIENDA PA,                                                           \n"
        + "   SMRGAA_V_UTE U                                                                           \n"
        + " WHERE                                                                                      \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                    \n"
        + "   AND PO.ID_PROCEDIMENTO     = PA.ID_PROCEDIMENTO                                          \n"
        + "   AND PA.DATA_FINE          IS NULL                                                        \n"
        + "   AND U.ID_AZIENDA           = PA.EXT_ID_AZIENDA                                           \n"
        + "   AND U.ID_REGIONE = :ID_REGIONE                                                           \n"
        + "   AND SYSDATE BETWEEN U.DATA_INIZIO_ATTIVITA_UTE AND NVL(U.DATA_FINE_ATTIVITA_UTE,SYSDATE) \n"
        + "   AND NOT EXISTS                                                                           \n"
        + "   (                                                                                        \n"
        + "     SELECT                                                                                 \n"
        + "       *                                                                                    \n"
        + "     FROM                                                                                   \n"
        + "       ISTAT                                                                                \n"
        + "     WHERE                                                                                  \n"
        + "       ISTAT.ISTAT_COMUNE = U.ISTAT_COMUNE                                                  \n"
        + "   )                                                                                        \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
    mapParameterSource.addValue("ID_REGIONE",
        IuffiConstants.GENERIC.ID_REGIONE_PIEMONTE, Types.VARCHAR);
    try
    {
      return (Map<String, Map<String, String>>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<Map<String, Map<String, String>>>()
              {
                @Override
                public Map<String, Map<String, String>> extractData(
                    ResultSet rs) throws SQLException, DataAccessException
                {
                  Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
                  while (rs.next())
                  {
                    Map<String, String> element = new HashMap<String, String>();
                    String istatComune = rs.getString("ISTAT_COMUNE");
                    element.put("istatComune", istatComune);
                    element.put("descrizioneComune",
                        rs.getString("DESC_COMUNE"));
                    element.put("gruppo", rs.getString("GRUPPO"));
                    map.put(istatComune, element);
                  }
                  return map;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento)
          },
          new LogVariable[]
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

  public Map<String, Map<String, String>> getMapComuniPiemontesiInInterventoForJSON(
      long idProcedimentoOggetto,
      long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getMapComuniPiemontesiInInterventoForJSON]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                      \n"
        + "   COMUNI AS                                                                               \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       LI.ISTAT_COMUNE                                                                     \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "       IUF_T_LOCALIZZAZIONE_INTERV LI                                                      \n"
        + "     WHERE                                                                                 \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "       AND DI.ID_INTERVENTO           = :ID_INTERVENTO                                     \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = LI.ID_DETTAGLIO_INTERVENTO                         \n"
        + "       AND NOT EXISTS                                                                      \n"
        + "       (                                                                                   \n"
        + "         SELECT                                                                            \n"
        + "           *                                                                               \n"
        + "         FROM                                                                              \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "         WHERE                                                                             \n"
        + "           WTMP.ID_INTERVENTO               = DI.ID_INTERVENTO                             \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       )                                                                                   \n"
        + "     UNION                                                                                 \n"
        + "     SELECT                                                                                \n"
        + "       LIPO.ISTAT_COMUNE                                                                   \n"
        + "     FROM                                                                                  \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "       IUF_W_LOCAL_INTERV_PROC_OGG LIPO                                                    \n"
        + "     WHERE                                                                                 \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                   \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                         \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = LIPO.ID_DETT_INTERV_PROC_OGG                     \n"
        + "   )                                                                                       \n"
        + " SELECT                                                                                    \n"
        + "   DG.ISTAT_COMUNE,                                                                        \n"
        + "   DG.DESCRIZIONE_COMUNE                                                                   \n"
        + "   || ' ('                                                                                 \n"
        + "   || DG.SIGLA_PROVINCIA                                                                   \n"
        + "   || ')'                   AS DESC_COMUNE,                                                \n"
        + "   DG.DESCRIZIONE_PROVINCIA AS GRUPPO                                                      \n"
        + " FROM                                                                                      \n"
        + "   SMRGAA_V_DATI_AMMINISTRATIVI DG,                                                        \n"
        + "   COMUNI C                                                                                \n"
        + " WHERE                                                                                     \n"
        + "   DG.ISTAT_COMUNE = C.ISTAT_COMUNE                                                        \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
    try
    {
      return (Map<String, Map<String, String>>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<Map<String, Map<String, String>>>()
              {
                @Override
                public Map<String, Map<String, String>> extractData(
                    ResultSet rs) throws SQLException, DataAccessException
                {
                  Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
                  while (rs.next())
                  {
                    Map<String, String> element = new HashMap<String, String>();
                    String istatComune = rs.getString("ISTAT_COMUNE");
                    element.put("istatComune", istatComune);
                    element.put("descrizioneComune",
                        rs.getString("DESC_COMUNE"));
                    element.put("gruppo", rs.getString("GRUPPO"));
                    map.put(istatComune, element);
                  }
                  return map;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento)
          },
          new LogVariable[]
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

  public long copiaDettaglioInterventoSuTemporaneo(long idDettaglioIntervento,
      long idProcedimentoOggetto,
      String flagTipoOperazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::copiaDettaglioInterventoSuTemporaneo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT =  " INSERT INTO IUF_W_DETT_INTERV_PROC_OGG (                        \n"
			    		 + "         ID_DETT_INTERV_PROC_OGG,                                  \n"
			    		 + "         ID_PROCEDIMENTO_OGGETTO,                                  \n"
			    		 + "         ID_INTERVENTO,                                            \n"
			    		 + "         IMPORTO_INVESTIMENTO,                                     \n"
			    		 + "         IMPORTO_AMMESSO,                                          \n"
			    		 + "         PERCENTUALE_CONTRIBUTO,                                   \n"
			    		 + "         IMPORTO_CONTRIBUTO,                                       \n"
			    		 + "         FLAG_TIPO_OPERAZIONE,                                     \n"
			    		 + "         ULTERIORI_INFORMAZIONI,                                   \n"
			    		 + "         IMPORTO_UNITARIO,                                         \n"
			    		 + "         CUAA_BENEFICIARIO,                                        \n"
			    		 + "         FLAG_ASSOCIATO_ALTRA_MISURA,                              \n"
			    		 + "         FLAG_CANALE,                                              \n"
			    		 + "         FLAG_OPERA_PRESA,                                         \n"
			    		 + "         FLAG_CONDOTTA                                             \n"
			    		 + "     )                                                             \n"
			    		 + "         ( SELECT                                                  \n"
			    		 + "             :ID_DETT_INTERV_PROC_OGG,                             \n"
			    		 + "             :ID_PROCEDIMENTO_OGGETTO,                             \n"
			    		 + "             DI.ID_INTERVENTO,                                     \n"
			    		 + "             DI.IMPORTO_INVESTIMENTO,                              \n"
			    		 + "             DI.IMPORTO_AMMESSO,                                   \n"
			    		 + "             DI.PERCENTUALE_CONTRIBUTO,                            \n"
			    		 + "             DI.IMPORTO_CONTRIBUTO,                                \n"
			    		 + "             :FLAG_TIPO_OPERAZIONE,                                \n"
			    		 + "             DI.ULTERIORI_INFORMAZIONI,                            \n"
			    		 + "             DI.IMPORTO_UNITARIO,                                  \n"
			    		 + "             DI.CUAA_BENEFICIARIO,                                 \n"
			    		 + "             FLAG_ASSOCIATO_ALTRA_MISURA,                          \n"
			    		 + "             DI.FLAG_CANALE,                                         \n"
			    		 + "             DI.FLAG_OPERA_PRESA,                                    \n"
			    		 + "             DI.FLAG_CONDOTTA                                        \n"
			    		 + "         FROM                                                      \n"
			    		 + "             IUF_T_DETTAGLIO_INTERVENTO DI                       \n"
			    		 + "         WHERE                                                     \n"
			    		 + "             DI.ID_DETTAGLIO_INTERVENTO = :ID_DETTAGLIO_INTERVENTO \n"
			    		 + "         )                                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    final long idDettIntervProcOgg = getNextSequenceValue(
        IuffiConstants.SQL.SEQUENCE.IUF_W_DETT_INTERV_PROC_OGG);
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_DETTAGLIO_INTERVENTO",
          idDettaglioIntervento, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_TIPO_OPERAZIONE", flagTipoOperazione,
          Types.VARCHAR);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idDettIntervProcOgg;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettaglioIntervento", idDettaglioIntervento),
              new LogParameter("flagTipoOperazione", flagTipoOperazione)
          },
          new LogVariable[]
          { new LogVariable("idDettIntervProcOgg", idDettIntervProcOgg) }, INSERT,
          mapParameterSource);
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

  public long copiaDettaglioInterventoSuTemporaneoConModifica(
      long idDettaglioIntervento,
      RigaModificaMultiplaInterventiDTO intervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::copiaDettaglioInterventoSuTemporaneoConModifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                                      \n"
        + " INTO                                                        \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG                                \n"
        + "   (                                                         \n"
        + "     ID_DETT_INTERV_PROC_OGG,                                \n"
        + "     ID_PROCEDIMENTO_OGGETTO,                                \n"
        + "     ID_INTERVENTO,                                          \n"
        + "     IMPORTO_INVESTIMENTO,                                   \n"
        + "     IMPORTO_AMMESSO,                                        \n"
        + "     PERCENTUALE_CONTRIBUTO,                                 \n"
        + "     IMPORTO_CONTRIBUTO,                                     \n"
        + "     FLAG_TIPO_OPERAZIONE,                                   \n"
        + "     ULTERIORI_INFORMAZIONI,                                 \n"
        + "     CUAA_BENEFICIARIO,                                 \n"
        + "     IMPORTO_UNITARIO                                        \n"
        + "   )                                                         \n"
        + "   (                                                         \n"
        + "     SELECT                                                  \n"
        + "       :ID_DETT_INTERV_PROC_OGG,                             \n"
        + "       :ID_PROCEDIMENTO_OGGETTO,                             \n"
        + "       DI.ID_INTERVENTO,                                     \n"
        + "       :IMPORTO_INVESTIMENTO,                                \n"
        + "       DI.IMPORTO_AMMESSO,                                   \n"
        + "       DI.PERCENTUALE_CONTRIBUTO,                            \n"
        + "       DI.IMPORTO_CONTRIBUTO,                                \n"
        + "       :FLAG_TIPO_OPERAZIONE,                                \n"
        + "       :ULTERIORI_INFORMAZIONI,                              \n"
        + "       :CUAA_BENEFICIARIO,                              \n"
        + "       :IMPORTO_UNITARIO                                     \n"
        + "     FROM                                                    \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI                         \n"
        + "     WHERE                                                   \n"
        + "       DI.ID_DETTAGLIO_INTERVENTO = :ID_DETTAGLIO_INTERVENTO \n"
        + "   )                                                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    final long idDettIntervProcOgg = getNextSequenceValue(
        IuffiConstants.SQL.SEQUENCE.IUF_W_DETT_INTERV_PROC_OGG);
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          intervento.getIdProcedimentoOggetto(), Types.NUMERIC);
      mapParameterSource.addValue("ID_DETTAGLIO_INTERVENTO",
          idDettaglioIntervento, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_INVESTIMENTO",
          intervento.getImporto(), Types.NUMERIC);
      mapParameterSource.addValue("FLAG_TIPO_OPERAZIONE",
          IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_MODIFICA,
          Types.VARCHAR);
      mapParameterSource.addValue("ULTERIORI_INFORMAZIONI",
          intervento.getUlterioriInformazioni(), Types.VARCHAR);
      mapParameterSource.addValue("CUAA_BENEFICIARIO",
          intervento.getCuaaPartecipante(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_UNITARIO",
          intervento.getImportoUnitario(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idDettIntervProcOgg;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("intervento", intervento)
          },
          new LogVariable[]
          { new LogVariable("idDettIntervProcOgg", idDettIntervProcOgg) }, INSERT,
          mapParameterSource);
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

  public long updateDettaglioInterventoTemporaneo(long idDettIntervProcOgg,
      RigaModificaMultiplaInterventiDTO intervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::copiaDettaglioInterventoSuTemporaneoConModifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                               \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG                         \n"
        + " SET                                                  \n"
        + "   IMPORTO_INVESTIMENTO   = :IMPORTO_INVESTIMENTO,    \n"
        + "   CUAA_BENEFICIARIO       = :CUAA_BENEFICIARIO,        \n"
        + "   IMPORTO_UNITARIO       = :IMPORTO_UNITARIO,        \n"
        + "   ULTERIORI_INFORMAZIONI = :ULTERIORI_INFORMAZIONI,  \n"
        + "   FLAG_TIPO_OPERAZIONE   =                           \n"
        + "     DECODE(FLAG_TIPO_OPERAZIONE,                     \n"
        + "            :FLAG_QUADRO_ECONOMICO,                   \n"
        + "            :FLAG_MODIFICA,FLAG_TIPO_OPERAZIONE)      \n"
        + " WHERE                                                \n"
        + "   ID_DETT_INTERV_PROC_OGG = :ID_DETT_INTERV_PROC_OGG \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_INVESTIMENTO",
          intervento.getImporto(), Types.NUMERIC);
      mapParameterSource.addValue("ULTERIORI_INFORMAZIONI",
          intervento.getUlterioriInformazioni(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_QUADRO_ECONOMICO",
          IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_MODIFICA_QUADRO_ECONOMICO,
          Types.VARCHAR);
      mapParameterSource.addValue("FLAG_MODIFICA",
          IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_MODIFICA, Types.VARCHAR);
      mapParameterSource.addValue("CUAA_BENEFICIARIO",
          intervento.getCuaaPartecipante(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_UNITARIO",
          intervento.getImportoUnitario(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      return idDettIntervProcOgg;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("intervento", intervento)
          },
          new LogVariable[]
          { new LogVariable("idDettIntervProcOgg", idDettIntervProcOgg) }, UPDATE,
          mapParameterSource);
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

  public <T> List<T> getElencoConduzioniJSON(long idProcedimentoOggetto,
      long idIntervento, Class<T> clazz)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoConduzioniJSON]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                                \n"
        + "   CONDUZIONI AS                                                                                     \n"
        + "   (                                                                                                 \n"
        + "     SELECT                                                                                          \n"
        + "       LI.EXT_ID_CONDUZIONE_DICHIARATA,                                                              \n"
        + "       LI.EXT_ID_UTILIZZO_DICHIARATO,                                                                \n"
        + "       LI.SUPERFICIE_IMPEGNO,                                                                        \n"
        + "       LI.SUPERFICIE_EFFETTIVA,                                                                      \n"
        + "       LI.SUPERFICIE_ISTRUTTORIA,                                                                    \n"
        + "       LI.SUPERFICIE_ACCERTATA_GIS                                                                   \n"
        + "     FROM                                                                                            \n"
        + "       IUF_T_INTERVENTO I,                                                                           \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                \n"
        + "       IUF_T_LOCALIZZAZIONE_INTERV LI                                                                \n"
        + "     WHERE                                                                                           \n"
        + "       I.ID_INTERVENTO                = :ID_INTERVENTO                                               \n"
        + "       AND I.ID_PROCEDIMENTO          = PO.ID_PROCEDIMENTO                                           \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                     \n"
        + "       AND I.ID_INTERVENTO            = DI.ID_INTERVENTO                                             \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = LI.ID_DETTAGLIO_INTERVENTO                                   \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE)           \n"
        + "       AND NOT EXISTS                                                                                \n"
        + "       (                                                                                             \n"
        + "         SELECT                                                                                      \n"
        + "           *                                                                                         \n"
        + "         FROM                                                                                        \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                           \n"
        + "         WHERE                                                                                       \n"
        + "           WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                                        \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                             \n"
        + "       )                                                                                             \n"
        + "     UNION                                                                                           \n"
        + "     SELECT                                                                                          \n"
        + "       LIPO.EXT_ID_CONDUZIONE_DICHIARATA,                                                            \n"
        + "       LIPO.EXT_ID_UTILIZZO_DICHIARATO,                                                              \n"
        + "       LIPO.SUPERFICIE_IMPEGNO,                                                                      \n"
        + "       LIPO.SUPERFICIE_EFFETTIVA,                                                                    \n"
        + "       LIPO.SUPERFICIE_ISTRUTTORIA,                                                                  \n"
        + "       LIPO.SUPERFICIE_ACCERTATA_GIS                                                                 \n"
        + "     FROM                                                                                            \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                              \n"
        + "       IUF_W_LOCAL_INTERV_PROC_OGG LIPO                                                              \n"
        + "     WHERE                                                                                           \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                             \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                   \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = LIPO.ID_DETT_INTERV_PROC_OGG                               \n"
        + "   )                                                                                                 \n"
        + " SELECT                                                                                              \n"
        + "   CU.ID_DICHIARAZIONE_CONSISTENZA,                                                                  \n"
        + "   CU.ID_CONDUZIONE_DICHIARATA,                                                                      \n"
        + "   CU.ID_UTILIZZO_DICHIARATO,                                                                        \n"
        + "   CU.COMUNE AS ISTAT_COMUNE,                                                                        \n"
        + "   CU.DESC_COMUNE                                                                                    \n"
        + "   || ' ('                                                                                           \n"
        + "   || CU.SIGLA_PROVINCIA                                                                             \n"
        + "   || ')' AS DESC_COMUNE,                                                                            \n"
        + "   CU.SEZIONE,                                                                                       \n"
        + "   CU.FOGLIO,                                                                                        \n"
        + "   CU.PARTICELLA,                                                                                    \n"
        + "   CU.SUBALTERNO,                                                                                    \n"
        + "   CU.SUP_CATASTALE,                                                                                 \n"
        + "   DECODE(CU.COD_TIPO_UTILIZZO, NULL, '','[' || CU.COD_TIPO_UTILIZZO || '] ')                        \n"
        + "   || CU.DESC_TIPO_UTILIZZO AS DESC_TIPO_UTILIZZO,                                                   \n"
        + "   DECODE(CU.CODICE_DESTINAZIONE, NULL, '','[' || CU.CODICE_DESTINAZIONE || '] ')                    \n"
        + "   || CU.DESCRIZIONE_DESTINAZIONE AS DESCRIZIONE_DESTINAZIONE,                                       \n"
        + "   DECODE(CU.COD_DETTAGLIO_USO, NULL, '','[' || CU.COD_DETTAGLIO_USO || '] ')                        \n"
        + "   || CU.DESC_TIPO_DETTAGLIO_USO AS DESC_TIPO_DETTAGLIO_USO,                                         \n"
        + "   DECODE(CU.CODICE_QUALITA_USO, NULL, '','[' || CU.CODICE_QUALITA_USO || '] ')                      \n"
        + "   || CU.DESCRIZIONE_QUALITA_USO AS DESCRIZIONE_QUALITA_USO,                                         \n"
        + "   DECODE(CU.COD_TIPO_VARIETA, NULL, '','[' || CU.COD_TIPO_VARIETA || '] ')                          \n"
        + "   || CU.DESC_TIPO_VARIETA AS DESC_TIPO_VARIETA,                                                     \n"
        + "   CU.SUPERFICIE_UTILIZZATA,                                                                         \n"
        + "   REPLACE(TRIM(TO_CHAR(C.SUPERFICIE_IMPEGNO,'999990.9999')),'.',',') AS SUPERFICIE_IMPEGNO,         \n"
        + "   REPLACE(TRIM(TO_CHAR(C.SUPERFICIE_EFFETTIVA,'999990.9999')),'.',',') AS SUPERFICIE_EFFETTIVA,     \n"
        + "   REPLACE(TRIM(TO_CHAR(C.SUPERFICIE_ISTRUTTORIA,'999990.9999')),'.',',') AS SUPERFICIE_ISTRUTTORIA, \n"
        + "   C.SUPERFICIE_ACCERTATA_GIS                                                                        \n"
        + " FROM                                                                                                \n"
        + "   SMRGAA_V_CONDUZIONE_UTILIZZO CU,                                                                  \n"
        + "   CONDUZIONI C                                                                                      \n"
        + " WHERE                                                                                               \n"
        + "   C.EXT_ID_CONDUZIONE_DICHIARATA   = CU.ID_CONDUZIONE_DICHIARATA                                    \n"
        + "   AND C.EXT_ID_UTILIZZO_DICHIARATO = CU.ID_UTILIZZO_DICHIARATO                                      \n"
        + " ORDER BY                                                                                            \n"
        + "   CU.DESC_COMUNE,                                                                                   \n"
        + "   CU.SEZIONE,                                                                                       \n"
        + "   CU.FOGLIO,                                                                                        \n"
        + "   CU.PARTICELLA,                                                                                    \n"
        + "   CU.SUBALTERNO,                                                                                    \n"
        + "   CU.DESC_TIPO_UTILIZZO                                                                             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, clazz);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
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

  public List<RigaJSONParticellaInteventoDTO> getElencoParticelleJSON(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoParticelleJSON]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                                       \n"
        + "   PARTICELLE AS                                                                                            \n"
        + "   (                                                                                                        \n"
        + "     SELECT                                                                                                 \n"
        + "       LI.EXT_ID_PARTICELLA_CERTIFICATA                                                                     \n"
        + "     FROM                                                                                                   \n"
        + "       IUF_T_INTERVENTO I,                                                                                  \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                                       \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                       \n"
        + "       IUF_T_LOCALIZZAZIONE_INTERV LI                                                                       \n"
        + "     WHERE                                                                                                  \n"
        + "       I.ID_INTERVENTO                = :ID_INTERVENTO                                                      \n"
        + "       AND I.ID_PROCEDIMENTO          = PO.ID_PROCEDIMENTO                                                  \n"
        + "       AND I.ID_INTERVENTO            = DI.ID_INTERVENTO                                                    \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = LI.ID_DETTAGLIO_INTERVENTO                                          \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE)                  \n"
        + "       AND NOT EXISTS                                                                                       \n"
        + "       (                                                                                                    \n"
        + "         SELECT                                                                                             \n"
        + "           *                                                                                                \n"
        + "         FROM                                                                                               \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                                  \n"
        + "         WHERE                                                                                              \n"
        + "           WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                                               \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO                                       \n"
        + "       )                                                                                                    \n"
        + "     UNION                                                                                                  \n"
        + "     SELECT                                                                                                 \n"
        + "       LIPO.EXT_ID_PARTICELLA_CERTIFICATA                                                                   \n"
        + "     FROM                                                                                                   \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                                     \n"
        + "       IUF_W_LOCAL_INTERV_PROC_OGG LIPO                                                                     \n"
        + "     WHERE                                                                                                  \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                                    \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                          \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = LIPO.ID_DETT_INTERV_PROC_OGG                                      \n"
        + "   )                                                                                                        \n"
        + " SELECT                                                                                                     \n"
        + "   PC.ID_PARTICELLA_CERTIFICATA,                                                                            \n"
        + "   PC.ISTAT_COMUNE,                                                                                         \n"
        + "   PC.DESCOM || ' (' || PC.SIGLA_PROVINCIA || ')' AS DESC_COMUNE,                                           \n"
        + "   NVL((SELECT S.SEZIONE || ' - ' || S.DESCRIZIONE FROM DB_SEZIONE S WHERE S.ISTAT_COMUNE = PC.ISTAT_COMUNE \n"
        + "     AND PC.SEZIONE = S.SEZIONE), 'Non presente') AS SEZIONE,                                               \n"
        + "   PC.FOGLIO,                                                                                               \n"
        + "   PC.SUBALTERNO,                                                                                           \n"
        + "   PC.PARTICELLA,                                                                                           \n"
        + "   PC.SUP_CATASTALE                                                                                         \n"
        + " FROM                                                                                                       \n"
        + "   PARTICELLE P,                                                                                            \n"
        + "   SMRGAA_V_PARTICELLA_CERTIFICAT PC                                                                        \n"
        + " WHERE                                                                                                      \n"
        + "   P.EXT_ID_PARTICELLA_CERTIFICATA = PC.ID_PARTICELLA_CERTIFICATA                                           \n"
        + " ORDER BY                                                                                                   \n"
        + "   DESC_COMUNE,                                                                                             \n"
        + "   PC.SEZIONE,                                                                                              \n"
        + "   PC.FOGLIO,                                                                                               \n"
        + "   PC.PARTICELLA,                                                                                           \n"
        + "   PC.SUBALTERNO                                                                                            \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      return (List<RigaJSONParticellaInteventoDTO>) queryForList(QUERY,
          mapParameterSource,
          RigaJSONParticellaInteventoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
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

  public List<RigaJSONConduzioneInteventoDTO> getElencoConduzioniJSON(
      long idProcedimentoOggetto,
      String[] idChiaveConduzione, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoConduzioniJSON]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder queryBuilder = new StringBuilder(
        " SELECT                                                                                               \n"
            + "   CU.ID_DICHIARAZIONE_CONSISTENZA,                                                                   \n"
            + "   CU.ID_CONDUZIONE_DICHIARATA,                                                                       \n"
            + "   CU.ID_UTILIZZO_DICHIARATO,                                                                         \n"
            + "   CU.COMUNE AS ISTAT_COMUNE,                                                                         \n"
            + "   CU.DESC_COMUNE                                                                                     \n"
            + "   || ' ('                                                                                            \n"
            + "   || CU.SIGLA_PROVINCIA                                                                              \n"
            + "   || ')' AS DESC_COMUNE,                                                                             \n"
            + "   CU.SEZIONE,                                                                                        \n"
            + "   CU.FOGLIO,                                                                                         \n"
            + "   CU.PARTICELLA,                                                                                     \n"
            + "   CU.SUBALTERNO,                                                                                     \n"
            + "   CU.SUP_CATASTALE,                                                                                  \n"
            + "   DECODE(CU.COD_TIPO_UTILIZZO, NULL, '','[' || CU.COD_TIPO_UTILIZZO || '] ')                         \n"
            + "   || CU.DESC_TIPO_UTILIZZO AS DESC_TIPO_UTILIZZO,                                                    \n"
            + "   DECODE(CU.CODICE_DESTINAZIONE, NULL, '','[' || CU.CODICE_DESTINAZIONE || '] ')                     \n"
            + "   || CU.DESCRIZIONE_DESTINAZIONE AS DESCRIZIONE_DESTINAZIONE,                                        \n"
            + "   DECODE(CU.COD_DETTAGLIO_USO, NULL, '','[' || CU.COD_DETTAGLIO_USO || '] ')                         \n"
            + "   || CU.DESC_TIPO_DETTAGLIO_USO AS DESC_TIPO_DETTAGLIO_USO,                                          \n"
            + "   DECODE(CU.CODICE_QUALITA_USO, NULL, '','[' || CU.CODICE_QUALITA_USO || '] ')                       \n"
            + "   || CU.DESCRIZIONE_QUALITA_USO AS DESCRIZIONE_QUALITA_USO,                                          \n"
            + "   DECODE(CU.COD_TIPO_VARIETA, NULL, '','[' || CU.COD_TIPO_VARIETA || '] ')                           \n"
            + "   || CU.DESC_TIPO_VARIETA AS DESC_TIPO_VARIETA,                                                      \n"
            + "   CU.SUPERFICIE_UTILIZZATA                                                                           \n"
            + " FROM                                                                                                 \n"
            + "   SMRGAA_V_CONDUZIONE_UTILIZZO CU ,                                                                  \n"
            + "   SMRGAA_V_DICH_CONSISTENZA DC                                                                       \n"
            + " WHERE                                                                                                \n"
            + "   CU.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA                                  \n"
            + "   AND DC.ID_AZIENDA               =                                                                  \n"
            + "   (                                                                                                  \n"
            + "     SELECT                                                                                           \n"
            + "       PAZ.EXT_ID_AZIENDA                                                                             \n"
            + "     FROM                                                                                             \n"
            + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                 \n"
            + "       IUF_T_PROCEDIMENTO_AZIENDA PAZ                                                                 \n"
            + "     WHERE                                                                                            \n"
            + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                          \n"
            + "       AND PO.ID_PROCEDIMENTO     = PAZ.ID_PROCEDIMENTO                                               \n"
            + "       AND SYSDATE BETWEEN PAZ.DATA_INIZIO AND NVL(PAZ.DATA_FINE, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
            + "   )                                                                                                  \n"
            + "   AND DC.ID_PROCEDIMENTO = :PROCEDIMENTO                                                             \n"
            + "   AND DC.ESCLUSO         = 'N'                                                                       \n");
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("PROCEDIMENTO",idProcedimentoAgricoltura, Types.NUMERIC);
    if (idChiaveConduzione != null)
    {
      queryBuilder.append(" AND \n (\n");
      int idx = 0;
      for (String chiave : idChiaveConduzione)
      {
        if (idx > 0)
        {
          queryBuilder.append(" OR ");
        }
        queryBuilder
            .append(
                "(CU.ID_DICHIARAZIONE_CONSISTENZA || '_' || CU.ID_CONDUZIONE_DICHIARATA || '_' || ID_UTILIZZO_DICHIARATO = :CHIAVE_")
            .append(idx)
            .append(")\n");
        mapParameterSource.addValue("CHIAVE_" + idx, chiave, Types.VARCHAR);
        idx++;
      }
      queryBuilder.append(")\n");
    }
    final String QUERY = queryBuilder.toString();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      List<RigaJSONConduzioneInteventoDTO> list = queryForList(QUERY,
          mapParameterSource,
          RigaJSONConduzioneInteventoDTO.class);
      if (list != null)
      {
        /*
         * Riordino gli elementi in base all'ordire delle chiavi passate in modo
         * che sia l'output sia ordinato in modo congruente con l'input. Uso una
         * mappa con chiave l'id (tripletta di valori separati da _) della riga
         */
        Map<String, RigaJSONConduzioneInteventoDTO> map = new HashMap<String, RigaJSONConduzioneInteventoDTO>();
        for (RigaJSONConduzioneInteventoDTO riga : list)
        {
          map.put(riga.getId(), riga);
        }
        list.clear();
        /*
         * Dopo aver azzerato la lista la riempio con lo stesso ordine delle
         * chiavi passate in input
         */
        for (String key : idChiaveConduzione)
        {
          list.add(map.get(key));
        }
      }
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
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

  public void insertLocalizzazioneConduzioni(long idProcedimentoOggetto,
      long idDettIntervProcOgg,
      String[] idChiaveConduzione, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertLocalizzazioneConduzioni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
      if (idChiaveConduzione == null)
      {
        logger.error(THIS_METHOD
            + " idChiaveConduzione == null! NullPointerException in arrivo!");
      }
    }
    StringBuilder insertBuilder = new StringBuilder(
        " INSERT                                                                  \n"
            + " INTO                                                                    \n"
            + "   IUF_W_LOCAL_INTERV_PROC_OGG                                           \n"
            + "   (                                                                     \n"
            + "     ID_LOCAL_INTERV_PROC_OGG,                                           \n"
            + "     ID_DETT_INTERV_PROC_OGG,                                            \n"
            + "     EXT_ID_CONDUZIONE_DICHIARATA,                                       \n"
            + "     EXT_ID_UTILIZZO_DICHIARATO                                          \n"
            + "   )                                                                     \n"
            + "   (                                                                     \n"
            + "     SELECT                                                              \n"
            + "       SEQ_IUF_W_LOCAL_INTE_PROC_OG.NEXTVAL,                           \n"
            + "       :ID_DETT_INTERV_PROC_OGG,                                         \n"
            + "       CU.ID_CONDUZIONE_DICHIARATA,                                      \n"
            + "       CU.ID_UTILIZZO_DICHIARATO                                         \n"
            + "     FROM                                                                \n"
            + "       SMRGAA_V_CONDUZIONE_UTILIZZO CU ,                                 \n"
            + "       SMRGAA_V_DICH_CONSISTENZA DC,                                     \n"
            + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                    \n"
            + "       IUF_T_PROCEDIMENTO_AZIENDA PA                                     \n"
            + "     WHERE                                                               \n"
            + "       CU.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA \n"
            + "       AND DC.ID_PROCEDIMENTO = :ID_PROCEDIMENTO_IUFFIWEB                     \n"
            + "       AND DC.ESCLUSO         = 'N'                                      \n"
            + "       AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO         \n"
            + "       AND DC.ID_AZIENDA = PA.EXT_ID_AZIENDA                             \n"
            + "       AND PA.DATA_FINE IS NULL                                          \n"
            + "       AND PA.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                       \n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    if (idChiaveConduzione != null)
    {
      insertBuilder.append(" AND \n (\n");
      int idx = 0;
      for (String chiave : idChiaveConduzione)
      {
        if (idx > 0)
        {
          insertBuilder.append(" OR ");
        }
        insertBuilder
            .append(
                "(CU.ID_DICHIARAZIONE_CONSISTENZA || '_' || CU.ID_CONDUZIONE_DICHIARATA || '_' || ID_UTILIZZO_DICHIARATO = :CHIAVE_")
            .append(idx)
            .append(")\n");
        mapParameterSource.addValue("CHIAVE_" + idx, chiave, Types.VARCHAR);
        ++idx;
      }
      insertBuilder.append(")\n");
    }
    insertBuilder.append(")");
    final String INSERT = insertBuilder.toString();
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_IUFFIWEB",
          idProcedimentoAgricoltura, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      int numRows = namedParameterJdbcTemplate.update(INSERT,
          mapParameterSource);
      logger.info(THIS_METHOD + "Inseriti #" + numRows
          + " conduzioni/utilizzi per l'idDettIntervProcOgg "
          + idDettIntervProcOgg);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("idChiaveConduzione", idChiaveConduzione)
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public void insertLocalizzazioneConduzioniBatch(long idDettIntervProcOgg,
      List<SuperficieConduzione> superfici)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertLocalizzazioneComuni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                      \n"
        + " INTO                                        \n"
        + "   IUF_W_LOCAL_INTERV_PROC_OGG               \n"
        + "   (                                         \n"
        + "     ID_LOCAL_INTERV_PROC_OGG,               \n"
        + "     ID_DETT_INTERV_PROC_OGG,                \n"
        + "     EXT_ID_CONDUZIONE_DICHIARATA,           \n"
        + "     EXT_ID_UTILIZZO_DICHIARATO,             \n"
        + "     SUPERFICIE_IMPEGNO                      \n"
        + "   )                                         \n"
        + "   VALUES                                    \n"
        + "   (                                         \n"
        + "     SEQ_IUF_W_LOCAL_INTE_PROC_OG.NEXTVAL, \n"
        + "     :ID_DETT_INTERV_PROC_OGG,               \n"
        + "     :EXT_ID_CONDUZIONE_DICHIARATA,          \n"
        + "     :EXT_ID_UTILIZZO_DICHIARATO,            \n"
        + "     :SUPERFICIE_IMPEGNO                     \n"
        + "   )                                         \n";

    int length = superfici.size();
    MapSqlParameterSource[] batchParameters = new MapSqlParameterSource[length];
    try
    {
      int idx = 0;
      for (SuperficieConduzione superficie : superfici)
      {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
            idDettIntervProcOgg, Types.NUMERIC);
        mapParameterSource.addValue("EXT_ID_CONDUZIONE_DICHIARATA",
            superficie.getIdConduzioneDichiarata(),
            Types.NUMERIC);
        mapParameterSource.addValue("EXT_ID_UTILIZZO_DICHIARATO",
            superficie.getIdUtilizzoDichiarato(), Types.NUMERIC);
        mapParameterSource.addValue("SUPERFICIE_IMPEGNO",
            superficie.getSuperficieImpegno(), Types.NUMERIC);
        batchParameters[idx++] = mapParameterSource;
      }
      namedParameterJdbcTemplate.batchUpdate(INSERT, batchParameters);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("superfici", superfici)
          },
          (LogVariable[]) null, INSERT, batchParameters);
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

  public void insertLocalizzazioneParticelle(long idDettIntervProcOgg,
      List<Long> idParticellaCertificata)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertLocalizzazioneParticelle]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
      if (idParticellaCertificata == null)
      {
        logger.error(THIS_METHOD
            + " idParticellaCertificata == null! NullPointerException in arrivo!");
      }
    }
    final String INSERT = " INSERT                                        \n"
        + " INTO                                          \n"
        + "   IUF_W_LOCAL_INTERV_PROC_OGG                 \n"
        + "   (                                           \n"
        + "     ID_LOCAL_INTERV_PROC_OGG,                 \n"
        + "     ID_DETT_INTERV_PROC_OGG,                  \n"
        + "     EXT_ID_PARTICELLA_CERTIFICATA             \n"
        + "   )                                           \n"
        + "   (                                           \n"
        + "     SELECT                                    \n"
        + "       SEQ_IUF_W_LOCAL_INTE_PROC_OG.NEXTVAL, \n"
        + "       :ID_DETT_INTERV_PROC_OGG,               \n"
        + "       PC.ID_PARTICELLA_CERTIFICATA            \n"
        + "     FROM                                      \n"
        + "       SMRGAA_V_PARTICELLA_CERTIFICAT PC       \n"
        + "     WHERE                                     \n"
        + "       PC.DATA_FINE_VALIDITA IS NULL           \n"
        + "       AND PC.DATA_SOPPRESSIONE IS NULL        \n"
        + getInCondition("PC.ID_PARTICELLA_CERTIFICATA",
            idParticellaCertificata)
        + "   )";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      int numRows = namedParameterJdbcTemplate.update(INSERT,
          mapParameterSource);
      logger
          .info(THIS_METHOD + "Inseriti #" + numRows
              + " particelle per l'idDettIntervProcOgg " + idDettIntervProcOgg);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public List<RigaJSONParticellaInteventoDTO> ricercaParticelle(
      FiltroRicercaParticelle filtro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::ricercaParticelle]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder queryBuilder = new StringBuilder(
        " SELECT                                   \n"
            + "   PC.ID_PARTICELLA_CERTIFICATA,          \n"
            + "   PC.ISTAT_COMUNE,                       \n"
            + "   PC.DESCOM || ' (' || PC.SIGLA_PROVINCIA\n"
            + "             || ')' AS DESC_COMUNE,       \n"
            + "   DECODE(PC.SEZIONE,NULL, 'Non presente',\n"
            + "          S.SEZIONE || ' - ' ||           \n"
            + "          S.DESCRIZIONE) AS SEZIONE,      \n"
            + "   PC.FOGLIO,                             \n"
            + "   PC.SUBALTERNO,                         \n"
            + "   PC.PARTICELLA,                         \n"
            + "   PC.SUP_CATASTALE                       \n"
            + " FROM                                     \n"
            + "   SMRGAA_V_PARTICELLA_CERTIFICAT PC,     \n"
            + "   DB_SEZIONE S                           \n"
            + " WHERE                                    \n"
            + "   PC.ISTAT_COMUNE = :ISTAT_COMUNE        \n"
            + "   AND PC.ISTAT_COMUNE = S.ISTAT_COMUNE(+)\n"
            + "   AND PC.SEZIONE = S.SEZIONE(+)          \n"
            + "   AND PC.DATA_FINE_VALIDITA IS NULL      \n"
            + "   AND PC.DATA_SOPPRESSIONE IS NULL       \n"
            + "   AND NVL(PC.SEZIONE,'null') = :SEZIONE  \n"
            + "   AND FOGLIO = :FOGLIO                   \n");
    if (filtro.getParticella() != null)
    {
      queryBuilder
          .append("   AND PC.PARTICELLA = :PARTICELLA                     \n");
    }
    if (!GenericValidator.isBlankOrNull(filtro.getSubalterno()))
    {
      queryBuilder
          .append("   AND PC.SUBALTERNO = :SUBALTERNO                     \n");
    }
    List<Long> idParticellaDichiarata = filtro.getIdParticellaCertificata();
    if (idParticellaDichiarata != null && !idParticellaDichiarata.isEmpty())
    {
      queryBuilder.append(getNotInCondition("PC.ID_PARTICELLA_CERTIFICATA",
          idParticellaDichiarata));
    }
    queryBuilder.append(
        "   ORDER BY PC.DESCOM, PC.SEZIONE, PC.FOGLIO, PC.PARTICELLA, PC.SUBALTERNO\n");
    final String QUERY = queryBuilder.toString();
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ISTAT_COMUNE", filtro.getIstatComune(),
          Types.VARCHAR);
      mapParameterSource.addValue("SEZIONE", filtro.getSezione(),
          Types.VARCHAR);
      mapParameterSource.addValue("FOGLIO", filtro.getFoglio(), Types.NUMERIC);
      mapParameterSource.addValue("PARTICELLA", filtro.getParticella(),
          Types.NUMERIC);
      mapParameterSource.addValue("SUBALTERNO", filtro.getSubalterno(),
          Types.VARCHAR);
      return (List<RigaJSONParticellaInteventoDTO>) queryForList(QUERY,
          mapParameterSource,
          RigaJSONParticellaInteventoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("filtro", filtro)
          },
          new LogVariable[]
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

  public List<RigaJSONAllegatiInterventoDTO> getAllegatiJSON(
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getAllegatiJSON]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " WITH                                                                                      \n"
        + "   FILES AS                                                                                \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       FADI.ID_FILE_ALLEGATI_INTERVENTO                                                    \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_INTERVENTO I,                                                                 \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "       IUF_R_FILE_ALLEGATI_DETT_INT FADI                                                 \n"
        + "     WHERE                                                                                 \n"
        + "       I.ID_INTERVENTO                = :ID_INTERVENTO                                     \n"
        + "       AND I.ID_PROCEDIMENTO          = PO.ID_PROCEDIMENTO                                 \n"
        + "       AND I.ID_INTERVENTO            = DI.ID_INTERVENTO                                   \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = FADI.ID_DETTAGLIO_INTERVENTO                       \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "       AND NOT EXISTS                                                                      \n"
        + "       (                                                                                   \n"
        + "         SELECT                                                                            \n"
        + "           *                                                                               \n"
        + "         FROM                                                                              \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "         WHERE                                                                             \n"
        + "           WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       )                                                                                   \n"
        + "     UNION                                                                                 \n"
        + "     SELECT                                                                                \n"
        + "       FAIPO.ID_FILE_ALLEGATI_INTERVENTO                                                   \n"
        + "     FROM                                                                                  \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "       IUF_W_FILE_ALL_INTE_PROC_OGG FAIPO                                                \n"
        + "     WHERE                                                                                 \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                   \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                         \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = FAIPO.ID_DETT_INTERV_PROC_OGG                    \n"
        + "   )                                                                                       \n"
        + " SELECT                                                                                    \n"
        + "   FAI.ID_FILE_ALLEGATI_INTERVENTO,                                                        \n"
        + "   FAI.NOME_LOGICO,                                                                        \n"
        + "   FAI.NOME_FISICO                                                                         \n"
        + " FROM                                                                                      \n"
        + "   FILES F,                                                                                \n"
        + "   IUF_T_FILE_ALLEGATI_INTERVEN FAI                                                      \n"
        + " WHERE                                                                                     \n"
        + "   F.ID_FILE_ALLEGATI_INTERVENTO = FAI.ID_FILE_ALLEGATI_INTERVENTO                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      return (List<RigaJSONAllegatiInterventoDTO>) queryForList(QUERY,
          mapParameterSource,
          RigaJSONAllegatiInterventoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
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

  public long insertFileAllegatoIntervento(FileAllegatoInterventoDTO file)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertFileAllegatoIntervento]";
    byte[] contenuto = null;
    if (file == null)
    {
      logger.error(THIS_METHOD
          + " ATTENZIONE! ERRORE GRAVE: file is NULL! NullPointer in arrivo");
    }
    else
    {
      contenuto = file.getFileAllegato();
      if (contenuto == null)
      {
        logger.warn(THIS_METHOD
            + "ATTENZIONE! ERRORE GRAVE: file.getContenuto() is NULL! NullPointer in arrivo");
      }
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " BEGIN.");
        if (file != null)
        {
          logger.debug(
              THIS_METHOD + " file.getNomeFisico() = " + file.getNomeFisico());
          logger.debug(
              THIS_METHOD + " file.getNomeLogico() = " + file.getNomeLogico());

          if (contenuto != null)
          {
            logger.debug(THIS_METHOD + " file.getContenuto().length = "
                + contenuto.length);
          }
        }
      }
    }

    final String UPDATE = " INSERT                            \n"
        + " INTO                              \n"
        + "   IUF_T_FILE_ALLEGATI_INTERVEN  \n"
        + "   (                               \n"
        + "     ID_FILE_ALLEGATI_INTERVENTO,  \n"
        + "     NOME_LOGICO,                  \n"
        + "     NOME_FISICO,                  \n"
        + "     FILE_ALLEGATO                 \n"
        + "   )                               \n"
        + "   VALUES                          \n"
        + "   (                               \n"
        + "     :ID_FILE_ALLEGATI_INTERVENTO, \n"
        + "     :NOME_LOGICO,                 \n"
        + "     :NOME_FISICO,                 \n"
        + "     :FILE_ALLEGATO                \n"
        + "   )                               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long idFileAllegatiIntervento = getNextSequenceValue(
        IuffiConstants.SQL.SEQUENCE.IUF_T_FILE_ALLEGATI_INTERVENTO);
    try
    {
      mapParameterSource.addValue("ID_FILE_ALLEGATI_INTERVENTO",
          idFileAllegatiIntervento, Types.NUMERIC);
      mapParameterSource.addValue("NOME_LOGICO", file.getNomeLogico(),
          Types.VARCHAR);
      mapParameterSource.addValue("NOME_FISICO", file.getNomeFisico(),
          Types.VARCHAR);
      mapParameterSource.addValue("FILE_ALLEGATO", new SqlLobValue(contenuto),
          Types.BLOB);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      return idFileAllegatiIntervento;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("file.getNomeFisico()", file.getNomeFisico()),
              new LogParameter("file.getNomeLogico()", file.getNomeLogico()),
              new LogParameter("contenuto.length", (contenuto == null
                  ? (Integer) null : new Integer(contenuto.length)))
          },
          new LogVariable[]
          { new LogVariable("idFileAllegatiIntervento", idFileAllegatiIntervento) }, UPDATE, mapParameterSource);
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

  public void insertLocalizzazioneMappeFile(Long idDettIntervProcOgg,
      long idFileAllegatiIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertLocalizzazioneMappeFile]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                           \n"
        + " INTO                             \n"
        + "   IUF_W_FILE_ALL_INTE_PROC_OGG \n"
        + "   (                              \n"
        + "     ID_DETT_INTERV_PROC_OGG,     \n"
        + "     ID_FILE_ALLEGATI_INTERVENTO  \n"
        + "   )                              \n"
        + "   VALUES                         \n"
        + "   (                              \n"
        + "     :ID_DETT_INTERV_PROC_OGG,    \n"
        + "     :ID_FILE_ALLEGATI_INTERVENTO \n"
        + "   )                              \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_FILE_ALLEGATI_INTERVENTO",
          idFileAllegatiIntervento, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter(
                  "idMisurazioneidFileAllegatiInterventoIntervento",
                  idFileAllegatiIntervento),
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public FileAllegatoInterventoDTO getFileFisicoAllegato(
      long idProcedimentoOggetto, long idIntervento,
      long idFileAllegatiIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getFileFisicoAllegato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
      logger.debug(
          THIS_METHOD + " idProcedimentoOggetto = " + idProcedimentoOggetto);
      logger.debug(THIS_METHOD + " idIntervento = " + idIntervento);
      logger.debug(THIS_METHOD + " idFileAllegatiIntervento = "
          + idFileAllegatiIntervento);
    }
    final String QUERY = " WITH                                                                                      \n"
        + "   FILE_ALLEGATI AS                                                                        \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       FADI.ID_FILE_ALLEGATI_INTERVENTO                                                    \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_INTERVENTO I,                                                                 \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "       IUF_R_FILE_ALLEGATI_DETT_INT FADI                                                 \n"
        + "     WHERE                                                                                 \n"
        + "       I.ID_INTERVENTO                = :ID_INTERVENTO                                     \n"
        + "       AND I.ID_PROCEDIMENTO          = PO.ID_PROCEDIMENTO                                 \n"
        + "       AND I.ID_INTERVENTO            = DI.ID_INTERVENTO                                   \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = FADI.ID_DETTAGLIO_INTERVENTO                       \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "       AND NOT EXISTS                                                                      \n"
        + "       (                                                                                   \n"
        + "         SELECT                                                                            \n"
        + "           *                                                                               \n"
        + "         FROM                                                                              \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "         WHERE                                                                             \n"
        + "           WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       )                                                                                   \n"
        + "     UNION                                                                                 \n"
        + "     SELECT                                                                                \n"
        + "       FAIPO.ID_FILE_ALLEGATI_INTERVENTO                                                   \n"
        + "     FROM                                                                                  \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "       IUF_W_FILE_ALL_INTE_PROC_OGG FAIPO                                                \n"
        + "     WHERE                                                                                 \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                   \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                         \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = FAIPO.ID_DETT_INTERV_PROC_OGG                    \n"
        + "   )                                                                                       \n"
        + " SELECT                                                                                    \n"
        + "   FAI.ID_FILE_ALLEGATI_INTERVENTO,                                                        \n"
        + "   FAI.NOME_LOGICO,                                                                        \n"
        + "   FAI.NOME_FISICO,                                                                        \n"
        + "   FAI.FILE_ALLEGATO                                                                       \n"
        + " FROM                                                                                      \n"
        + "   FILE_ALLEGATI F,                                                                        \n"
        + "   IUF_T_FILE_ALLEGATI_INTERVEN FAI                                                      \n"
        + " WHERE                                                                                     \n"
        + "   F.ID_FILE_ALLEGATI_INTERVENTO = FAI.ID_FILE_ALLEGATI_INTERVENTO                         \n"
        + "   AND FAI.ID_FILE_ALLEGATI_INTERVENTO = :ID_FILE_ALLEGATI_INTERVENTO                      \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_FILE_ALLEGATI_INTERVENTO",
          idFileAllegatiIntervento, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<FileAllegatoInterventoDTO>()
          {
            @Override
            public FileAllegatoInterventoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                FileAllegatoInterventoDTO file = new FileAllegatoInterventoDTO();
                file.setFileAllegato(rs.getBytes("FILE_ALLEGATO"));
                file.setNomeFisico(rs.getString("NOME_FISICO"));
                file.setNomeLogico(rs.getString("NOME_LOGICO"));
                file.setIdFileAllegatiIntervento(
                    rs.getLong("ID_FILE_ALLEGATI_INTERVENTO"));
                return file;
              }
              else
              {
                return null;
              }
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("idFileAllegatiIntervento",
                  idFileAllegatiIntervento)
          },
          new LogVariable[]
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

  public boolean isAllegatoAppartenenteProcedimentoOggettoEIntervento(
      long idFileAllegatiIntervento,
      long idProcedimentoOggetto, long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::isAllegatoAppartenenteProcedimentoOggettoEIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                      \n"
        + "   FILE_ALLEGATI AS                                                                        \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       FADI.ID_FILE_ALLEGATI_INTERVENTO                                                    \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_INTERVENTO I,                                                                 \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "       IUF_R_FILE_ALLEGATI_DETT_INT FADI                                                 \n"
        + "     WHERE                                                                                 \n"
        + "       I.ID_INTERVENTO                = :ID_INTERVENTO                                     \n"
        + "       AND I.ID_PROCEDIMENTO          = PO.ID_PROCEDIMENTO                                 \n"
        + "       AND I.ID_INTERVENTO            = DI.ID_INTERVENTO                                   \n"
        + "       AND DI.ID_DETTAGLIO_INTERVENTO = FADI.ID_DETTAGLIO_INTERVENTO                       \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "       AND NOT EXISTS                                                                      \n"
        + "       (                                                                                   \n"
        + "         SELECT                                                                            \n"
        + "           *                                                                               \n"
        + "         FROM                                                                              \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "         WHERE                                                                             \n"
        + "           WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       )                                                                                   \n"
        + "     UNION                                                                                 \n"
        + "     SELECT                                                                                \n"
        + "       FAIPO.ID_FILE_ALLEGATI_INTERVENTO                                                   \n"
        + "     FROM                                                                                  \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "       IUF_W_FILE_ALL_INTE_PROC_OGG FAIPO                                                \n"
        + "     WHERE                                                                                 \n"
        + "       DIPO.ID_INTERVENTO               = :ID_INTERVENTO                                   \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                         \n"
        + "       AND DIPO.ID_DETT_INTERV_PROC_OGG = FAIPO.ID_DETT_INTERV_PROC_OGG                    \n"
        + "   )                                                                                       \n"
        + " SELECT                                                                                    \n"
        + "   COUNT(*)                                                                                \n"
        + " FROM                                                                                      \n"
        + "   FILE_ALLEGATI F                                                                         \n"
        + " WHERE                                                                                     \n"
        + "   F.ID_FILE_ALLEGATI_INTERVENTO = :ID_FILE_ALLEGATI_INTERVENTO                            \n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_FILE_ALLEGATI_INTERVENTO",
          idFileAllegatiIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      return namedParameterJdbcTemplate.queryForLong(QUERY,
          mapParameterSource) > 0;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idFileAllegatiIntervento",
                  idFileAllegatiIntervento),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento)
          }, null, QUERY, (MapSqlParameterSource) null);
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

  public void deleteFileAllegatiInterventoSeNonReferenziato(
      long idFileAllegatiIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::deleteFileAllegatiInterventoSeNonReferenziato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " DELETE                                                                 \n"
        + " FROM                                                                   \n"
        + "   IUF_T_FILE_ALLEGATI_INTERVEN                                       \n"
        + " WHERE                                                                  \n"
        + "   ID_FILE_ALLEGATI_INTERVENTO = :ID_FILE_ALLEGATI_INTERVENTO           \n"
        + "   AND NOT EXISTS                                                       \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       *                                                                \n"
        + "     FROM                                                               \n"
        + "       IUF_R_FILE_ALLEGATI_DETT_INT FADI                              \n"
        + "     WHERE                                                              \n"
        + "       FADI.ID_FILE_ALLEGATI_INTERVENTO = :ID_FILE_ALLEGATI_INTERVENTO  \n"
        + "   )                                                                    \n"
        + "   AND NOT EXISTS                                                       \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       *                                                                \n"
        + "     FROM                                                               \n"
        + "       IUF_W_FILE_ALL_INTE_PROC_OGG FAIPO                             \n"
        + "     WHERE                                                              \n"
        + "       FAIPO.ID_FILE_ALLEGATI_INTERVENTO = :ID_FILE_ALLEGATI_INTERVENTO \n"
        + "   )                                                                    \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_FILE_ALLEGATI_INTERVENTO",
          idFileAllegatiIntervento, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idFileAllegatiIntervento",
                  idFileAllegatiIntervento)
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public List<DatiLocalizzazioneParticellarePerStampa> getLocalizzazioneParticellePerStampa(
      long idProcedimentoOggetto,
      String flagEscludiCatalogo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getLocalizzazioneParticellePerStampa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "   WITH                                                                                       \n"
        + "      PARTICELLE AS                                                                           \n"
        + "      (                                                                                       \n"
        + "        SELECT                                                                                \n"
        + "          DI.PROGRESSIVO,                                                                     \n"
        + "          I.ID_INTERVENTO,                                                                    \n"
        + "          DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "          LI.EXT_ID_PARTICELLA_CERTIFICATA,                                                   \n"
        + "          LI.EXT_ID_UTILIZZO_DICHIARATO,                                                      \n"
        + "          LI.EXT_ID_CONDUZIONE_DICHIARATA,                                                    \n"
        + "          LI.SUPERFICIE_IMPEGNO,                                                              \n"
        + "          LI.SUPERFICIE_EFFETTIVA,                                                            \n"
        + "          LI.SUPERFICIE_ISTRUTTORIA,                                                          \n"
        + "          LI.SUPERFICIE_ACCERTATA_GIS                                                         \n"
        + "        FROM                                                                                  \n"
        + "          IUF_T_INTERVENTO I,                                                                 \n"
        + "          IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "          IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "          IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "          IUF_T_LOCALIZZAZIONE_INTERV LI,                                                     \n"
        + "          IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + "        WHERE                                                                                 \n"
        + "          PO.ID_PROCEDIMENTO_OGGETTO          = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "          AND I.ID_PROCEDIMENTO               = PO.ID_PROCEDIMENTO                            \n"
        + "          AND I.ID_DESCRIZIONE_INTERVENTO     = DESCINT.ID_DESCRIZIONE_INTERVENTO             \n"
        + "          AND DESCINT.ID_TIPO_LOCALIZZAZIONE IN (3,4, 8)                                      \n"
        + "          AND I.ID_INTERVENTO                 = DI.ID_INTERVENTO                              \n"
        + "          AND DI.ID_DETTAGLIO_INTERVENTO      = LI.ID_DETTAGLIO_INTERVENTO                    \n"
        + "          AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "          AND DESCINT.ID_CATEGORIA_INTERVENTO = CI.ID_CATEGORIA_INTERVENTO(+)                 \n"
        + "          AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "          AND NOT EXISTS                                                                      \n"
        + "          (                                                                                   \n"
        + "            SELECT                                                                            \n"
        + "              *                                                                               \n"
        + "            FROM                                                                              \n"
        + "              IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "            WHERE                                                                             \n"
        + "              WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "              AND WTMP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "          )                                                                                   \n"
        + "        UNION                                                                                 \n"
        + "        SELECT                                                                                \n"
        + "          DI.PROGRESSIVO AS PROGRESSIVO,                                                                \n"
        + "          DIPO.ID_INTERVENTO,                                                                 \n"
        + "          DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "          LIPO.EXT_ID_PARTICELLA_CERTIFICATA,                                                 \n"
        + "          LIPO.EXT_ID_UTILIZZO_DICHIARATO,                                                    \n"
        + "          LIPO.EXT_ID_CONDUZIONE_DICHIARATA,                                                  \n"
        + "          LIPO.SUPERFICIE_IMPEGNO,                                                            \n"
        + "          LIPO.SUPERFICIE_EFFETTIVA,                                                          \n"
        + "          LIPO.SUPERFICIE_ISTRUTTORIA,                                                        \n"
        + "          LIPO.SUPERFICIE_ACCERTATA_GIS                                                       \n"
        + "        FROM                                                                                  \n"
        + "          IUF_T_INTERVENTO I,                                                                 \n"
        + "          IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "          IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "          IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                    \n"
        + "          IUF_W_LOCAL_INTERV_PROC_OGG LIPO,                                                   \n"
        + "          IUF_D_CATEGORIA_INTERVENTO CI                                                       \n"
        + "        WHERE                                                                                 \n"
        + "          DIPO.ID_PROCEDIMENTO_OGGETTO        = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "          AND DIPO.ID_DETT_INTERV_PROC_OGG    = LIPO.ID_DETT_INTERV_PROC_OGG                  \n"
        + "          AND DIPO.ID_INTERVENTO              = I.ID_INTERVENTO                               \n"
        + "          AND I.ID_DESCRIZIONE_INTERVENTO     = DESCINT.ID_DESCRIZIONE_INTERVENTO             \n"
        + "          AND DI.ID_INTERVENTO(+)   = I.ID_INTERVENTO             							 \n"
        + "          AND DESCINT.ID_CATEGORIA_INTERVENTO = CI.ID_CATEGORIA_INTERVENTO(+)                 \n"
        + "          AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "          AND DIPO.FLAG_TIPO_OPERAZIONE <> 'D'                                                \n"
        + "          AND DESCINT.ID_TIPO_LOCALIZZAZIONE IN (3,4, 8)                                      \n"
        + "      )                                                                                       \n"
        + "    SELECT                                                                                    \n"
        + "      P.PROGRESSIVO,                                                                          \n"
        + "      P.ID_INTERVENTO,                                                                        \n"
        + "      P.DESC_INTERVENTO,                                                                      \n"
        + "      P.SUPERFICIE_IMPEGNO,                                                                   \n"
        + "      PC.ISTAT_COMUNE,                                                                        \n"
        + "      PC.DESCOM                                                                               \n"
        + "      || ' ('                                                                                 \n"
        + "      || PC.SIGLA_PROVINCIA                                                                   \n"
        + "      || ')' AS DESC_COMUNE,                                                                  \n"
        + "      NVL(                                                                                    \n"
        + "      (                                                                                       \n"
        + "        SELECT                                                                                \n"
        + "          S.SEZIONE                                                                           \n"
        + "          || ' - '                                                                            \n"
        + "          || S.DESCRIZIONE                                                                    \n"
        + "        FROM                                                                                  \n"
        + "          DB_SEZIONE S                                                                        \n"
        + "        WHERE                                                                                 \n"
        + "          S.ISTAT_COMUNE = PC.ISTAT_COMUNE                                                    \n"
        + "          AND PC.SEZIONE = S.SEZIONE                                                          \n"
        + "      )                                                                                       \n"
        + "      , ' - ') AS SEZIONE,                                                                    \n"
        + "      PC.FOGLIO,                                                                              \n"
        + "      PC.SUBALTERNO,                                                                          \n"
        + "      PC.PARTICELLA,                                                                          \n"
        + "      PC.SUP_CATASTALE,                                                                       \n"
        + "      NULL AS DESC_TIPO_UTILIZZO,                                                             \n"
        + "      NULL AS SUPERFICIE_UTILIZZATA,                                                          \n"
        + "      NULL AS SUPERFICIE_EFFETTIVA,                                                           \n"
        + "      NULL AS SUPERFICIE_ISTRUTTORIA,                                                         \n"
        + "      NULL AS SUPERFICIE_ACCERTATA_GIS                                                        \n"
        + "    FROM                                                                                      \n"
        + "      PARTICELLE P,                                                                           \n"
        + "      SMRGAA_V_PARTICELLA_CERTIFICAT PC                                                       \n"
        + "    WHERE                                                                                     \n"
        + "      P.EXT_ID_PARTICELLA_CERTIFICATA = PC.ID_PARTICELLA_CERTIFICATA                          \n"
        + "    UNION                                                                                     \n"
        + "    SELECT                                                                                    \n"
        + "      P.PROGRESSIVO,                                                                          \n"
        + "      P.ID_INTERVENTO,                                                                        \n"
        + "      P.DESC_INTERVENTO,                                                                      \n"
        + "      P.SUPERFICIE_IMPEGNO,                                                                   \n"
        + "      CU.COMUNE,                                                                              \n"
        + "      CU.DESC_COMUNE                                                                          \n"
        + "      || ' ('                                                                                 \n"
        + "      || CU.SIGLA_PROVINCIA                                                                   \n"
        + "      || ')' AS DESC_COMUNE,                                                                  \n"
        + "      NVL(                                                                                    \n"
        + "      (                                                                                       \n"
        + "        SELECT                                                                                \n"
        + "          S.SEZIONE                                                                           \n"
        + "          || ' - '                                                                            \n"
        + "          || S.DESCRIZIONE                                                                    \n"
        + "        FROM                                                                                  \n"
        + "          DB_SEZIONE S                                                                        \n"
        + "        WHERE                                                                                 \n"
        + "          S.ISTAT_COMUNE = CU.COMUNE                                                          \n"
        + "          AND CU.SEZIONE = S.SEZIONE                                                          \n"
        + "      )                                                                                       \n"
        + "      , ' - ') AS SEZIONE,                                                                    \n"
        + "      CU.FOGLIO,                                                                              \n"
        + "      CU.SUBALTERNO,                                                                          \n"
        + "      CU.PARTICELLA,                                                                          \n"
        + "      CU.SUP_CATASTALE,                                                                       \n"
        + "      CU.DESC_TIPO_UTILIZZO,                                                                  \n"
        + "      CU.SUPERFICIE_UTILIZZATA,                                                               \n"
        + "      P.SUPERFICIE_EFFETTIVA,                                                                 \n"
        + "      P.SUPERFICIE_ISTRUTTORIA,                                                               \n"
        + "      P.SUPERFICIE_ACCERTATA_GIS                                                              \n"
        + "    FROM                                                                                      \n"
        + "      PARTICELLE P,                                                                           \n"
        + "      SMRGAA_V_CONDUZIONE_UTILIZZO CU                                                         \n"
        + "    WHERE                                                                                     \n"
        + "      P.EXT_ID_CONDUZIONE_DICHIARATA   = CU.ID_CONDUZIONE_DICHIARATA                          \n"
        + "      AND P.EXT_ID_UTILIZZO_DICHIARATO = CU.ID_UTILIZZO_DICHIARATO                            \n"
        + "    ORDER BY                                                                                  \n"
        + "      PROGRESSIVO,                                                                            \n"
        + "      DESC_COMUNE,                                                                            \n"
        + "      SEZIONE,                                                                                \n"
        + "      FOGLIO,                                                                                 \n"
        + "      PARTICELLA,                                                                             \n"
        + "      SUBALTERNO NULLS FIRST                                                                  \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO", flagEscludiCatalogo,
          Types.VARCHAR);
      return (List<DatiLocalizzazioneParticellarePerStampa>) queryForList(QUERY,
          mapParameterSource,
          DatiLocalizzazioneParticellarePerStampa.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("flagEscludiCatalogo", flagEscludiCatalogo)
          },
          new LogVariable[]
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

  public boolean isBandoConPercentualeRiduzione(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::isBandoConPercentualeRiduzione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                  \n"
        + "   B.FLAG_RIBASSO_INTERVENTI                             \n"
        + " FROM                                                    \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                        \n"
        + "   IUF_T_PROCEDIMENTO P, IUF_D_BANDO B                   \n"
        + " WHERE                                                   \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "   AND P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO            \n"
        + "   AND P.ID_BANDO = B.ID_BANDO                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.INTEGER);
    try
    {
      return IuffiConstants.FLAGS.SI
          .equals(namedParameterJdbcTemplate.queryForObject(QUERY,
              mapParameterSource, String.class));
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, null, QUERY, mapParameterSource);
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

  public void updatePercentualeRibassoInterventi(long idProcedimentoOggetto,
      BigDecimal percentuale)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertInterventoNonDefinitivo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " UPDATE IUF_T_PROCEDIMENTO_OGGETTO SET PERCENTUALE_RIBASSO_INTERVENTI=:PERCENTUALE WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("PERCENTUALE", percentuale, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("percentuale", percentuale),
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public InfoRiduzione getInfoRiduzione(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getInfoRiduzione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                                                                        \n"
        + "   PERCENTUALE AS                                                                                                                            \n"
        + "   (                                                                                                                                         \n"
        + "     SELECT                                                                                                                                  \n"
        + "       P.PERCENTUALE_RIBASSO_INTERVENTI,                                                                                                     \n"
        + "       P.ID_PROCEDIMENTO                                                                                                                     \n"
        + "     FROM                                                                                                                                    \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO P                                                                                                          \n"
        + "     WHERE                                                                                                                                   \n"
        + "       P.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                                                  \n"
        + "   )                                                                                                                                         \n"
        + " SELECT                                                                                                                                      \n"
        + "   PERC_RIBASSO.PERCENTUALE_RIBASSO_INTERVENTI     AS PERCENTUALE,                                                                           \n"
        + "   NVL(SUM(NVL(IMPORTO.IMPORTO_INVESTIMENTO,0)),0) AS TOTALE_IMPORTO,                                                                        \n"
        + "   NVL(SUM(NVL(IMPORTO.IMPORTO_INVESTIMENTO - ROUND(IMPORTO.IMPORTO_INVESTIMENTO * PERC_RIBASSO.PERCENTUALE_RIBASSO_INTERVENTI/100,2),0)),0) \n"
        + "   AS TOTALE_RICHIESTO                                                                                                                       \n"
        + " FROM                                                                                                                                        \n"
        + "   (                                                                                                                                         \n"
        + "     SELECT                                                                                                                                  \n"
        + "       DI.IMPORTO_INVESTIMENTO,                                                                                                              \n"
        + "       I.ID_PROCEDIMENTO                                                                                                                     \n"
        + "     FROM                                                                                                                                    \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                                                        \n"
        + "       IUF_T_INTERVENTO I,                                                                                                                   \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                                                                        \n"
        + "       IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                                                                                 \n"
        + "       IUF_D_CATEGORIA_INTERVENTO CI                                                                                                         \n"
        + "     WHERE                                                                                                                                   \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                                                 \n"
        + "       AND I.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                                                                                       \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE)                                                   \n"
        + "       AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO                                                                              \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO                                                             \n"
        + "       AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)                                                                 \n"
        + "       AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                                                                        \n"
        + "       AND NOT EXISTS                                                                                                                        \n"
        + "       (                                                                                                                                     \n"
        + "         SELECT                                                                                                                              \n"
        + "           *                                                                                                                                 \n"
        + "         FROM                                                                                                                                \n"
        + "           IUF_W_DETT_INTERV_PROC_OGG WTMP                                                                                                   \n"
        + "         WHERE                                                                                                                               \n"
        + "           WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                                                                                \n"
        + "           AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                                       \n"
        + "       )                                                                                                                                     \n"
        + "     UNION ALL                                                                                                                               \n"
        + "     SELECT                                                                                                                                  \n"
        + "       DIPO.IMPORTO_INVESTIMENTO,                                                                                                            \n"
        + "       I.ID_PROCEDIMENTO                                                                                                                     \n"
        + "     FROM                                                                                                                                    \n"
        + "       IUF_T_INTERVENTO I,                                                                                                                   \n"
        + "       IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                                                                                 \n"
        + "       IUF_D_CATEGORIA_INTERVENTO CI,                                                                                                        \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO                                                                                                       \n"
        + "     WHERE                                                                                                                                   \n"
        + "       DIPO.ID_INTERVENTO                    = I.ID_INTERVENTO                                                                               \n"
        + "       AND DIPO.ID_PROCEDIMENTO_OGGETTO      = :ID_PROCEDIMENTO_OGGETTO                                                                      \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO                                                             \n"
        + "       AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)                                                                 \n"
        + "       AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                                                                        \n"
        + "       AND DIPO.FLAG_TIPO_OPERAZIONE        <> 'D'                                                                                           \n"
        + "   )                                                                                                                                         \n"
        + "   IMPORTO,                                                                                                                                  \n"
        + "   PERCENTUALE PERC_RIBASSO                                                                                                                  \n"
        + " WHERE                                                                                                                                       \n"
        + "   PERC_RIBASSO.ID_PROCEDIMENTO = IMPORTO.ID_PROCEDIMENTO(+)                                                                                 \n"
        + " GROUP BY                                                                                                                                    \n"
        + "   PERC_RIBASSO.PERCENTUALE_RIBASSO_INTERVENTI                                                                                               \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.INTEGER);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.NO, Types.VARCHAR);
    try
    {
      return queryForObject(QUERY, mapParameterSource, InfoRiduzione.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, null, QUERY, mapParameterSource);
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

  public void updateInterventoDatiQuadroEconomico(Long idDettIntervProcOgg,
      BigDecimal importoAmmesso,
      BigDecimal percentualeContributo,
      BigDecimal importoContributo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::updateInterventoDatiQuadroEconomico]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " UPDATE                                               \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG                         \n"
        + " SET                                                  \n"
        + "   IMPORTO_AMMESSO        = :IMPORTO_AMMESSO,         \n"
        + "   PERCENTUALE_CONTRIBUTO = :PERCENTUALE_CONTRIBUTO,  \n"
        + "   IMPORTO_CONTRIBUTO     = :IMPORTO_CONTRIBUTO       \n"
        + " WHERE                                                \n"
        + "   ID_DETT_INTERV_PROC_OGG = :ID_DETT_INTERV_PROC_OGG \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("PERCENTUALE_CONTRIBUTO",
          percentualeContributo, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_AMMESSO", importoAmmesso,
          Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_CONTRIBUTO", importoContributo,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("percentualeContributo", percentualeContributo),
              new LogParameter("importoAmmesso", importoAmmesso),
              new LogParameter("percentualeContributo", percentualeContributo),
              new LogParameter("importoContributo", importoContributo)
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public void updatePercentualeInterventoQuadroEconomico(
      Long idDettIntervProcOgg, BigDecimal percentualeContributo,
      BigDecimal fattorePercNettaRiduzione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::updatePercentualeInterventoQuadroEconomico]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " UPDATE                                                                                                                    \n"
        + "   IUF_W_DETT_INTERV_PROC_OGG                                                                                              \n"
        + " SET                                                                                                                       \n"
        + "   PERCENTUALE_CONTRIBUTO = :PERCENTUALE_CONTRIBUTO,                                                                       \n"
        + "   IMPORTO_AMMESSO        = IMPORTO_INVESTIMENTO         - ROUND(IMPORTO_INVESTIMENTO * :FATTORE_PERC_NETTA_RIDUZIONE,2),  \n"
        + "   IMPORTO_CONTRIBUTO     = ROUND(((IMPORTO_INVESTIMENTO - ROUND(IMPORTO_INVESTIMENTO * :FATTORE_PERC_NETTA_RIDUZIONE,2))* \n"
        + "   :PERCENTUALE_CONTRIBUTO)                              / 100,2)                                                          \n"
        + " WHERE                                                                                                                     \n"
        + "   ID_DETT_INTERV_PROC_OGG = :ID_DETT_INTERV_PROC_OGG                                                                      \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("PERCENTUALE_CONTRIBUTO",
          percentualeContributo, Types.NUMERIC);
      mapParameterSource.addValue("FATTORE_PERC_NETTA_RIDUZIONE",
          fattorePercNettaRiduzione, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("percentualeContributo", percentualeContributo),
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public List<RangePercentuale> getRangePercentuali(long[] idIntervento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRangePercentuali";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                                         \n"
        + "   I.ID_INTERVENTO,                                                             \n"
        + "   L.ID_LIVELLO,                                                                \n"
        + "   L.CODICE_LIVELLO,                                                            \n"
        + "   L.CODICE,                                                                    \n"
        + "   NVL(LB.PERCENTUALE_CONTRIBUTO_MINIMA,0) AS PERCENTUALE_CONTRIBUTO_MINIMA,    \n"
        + "   NVL(LB.PERCENTUALE_CONTRIBUTO_MASSIMA,100) AS PERCENTUALE_CONTRIBUTO_MASSIMA \n"
        + " FROM                                                                           \n"
        + "   IUF_T_INTERVENTO I,                                                          \n"
        + "   IUF_T_PROCEDIMENTO P,                                                        \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DI,                                             \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI,                                              \n"
        + "   IUF_R_LIVELLO_BANDO LB,                                                      \n"
        + "   IUF_D_LIVELLO L                                                              \n"
        + " WHERE                                                                          \n"
        + "   I.ID_DESCRIZIONE_INTERVENTO      = DI.ID_DESCRIZIONE_INTERVENTO              \n"
        + "   AND I.ID_PROCEDIMENTO            = P.ID_PROCEDIMENTO                         \n"
        + "   AND DI.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO             \n"
        + "   AND P.ID_BANDO                   = LBI.ID_BANDO                              \n"
        + "   AND LBI.ID_BANDO                 = LB.ID_BANDO                               \n"
        + "   AND LBI.ID_LIVELLO               = LB.ID_LIVELLO                             \n"
        + "   AND LB.ID_LIVELLO                = L.ID_LIVELLO                              \n"
        + this.getInCondition("I.ID_INTERVENTO", idIntervento)
        + " ORDER BY                                                                       \n"
        + "   L.CODICE_LIVELLO,                                                            \n"
        + "   NVL(LB.PERCENTUALE_CONTRIBUTO_MASSIMA,100),                                  \n"
        + "   NVL(LB.PERCENTUALE_CONTRIBUTO_MINIMA,0),                                     \n"
        + "   L.ORDINAMENTO                                                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<RangePercentuale>>()
          {
            @Override
            public List<RangePercentuale> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<RangePercentuale> result = new ArrayList<RangePercentuale>();
              String lastCodiceLivello = null;
              BigDecimal lastPercentualeContributoMassima = null;
              BigDecimal lastPercentualeContributoMinima = null;
              RangePercentuale range = null;
              Integer lastIdLivello = null;
              while (rs.next())
              {
                final String codiceLivello = rs.getString("CODICE_LIVELLO");
                final BigDecimal percentualeContributoMassima = rs
                    .getBigDecimal("PERCENTUALE_CONTRIBUTO_MASSIMA");
                final BigDecimal percentualeContributoMinima = rs
                    .getBigDecimal("PERCENTUALE_CONTRIBUTO_MINIMA");
                if (!codiceLivello.equals(lastCodiceLivello) ||
                    lastPercentualeContributoMassima == null
                    || percentualeContributoMassima
                        .compareTo(lastPercentualeContributoMassima) != 0
                    || lastPercentualeContributoMinima == null
                    || percentualeContributoMinima
                        .compareTo(lastPercentualeContributoMinima) != 0)
                {
                  range = new RangePercentuale();
                  lastCodiceLivello = codiceLivello;
                  lastPercentualeContributoMassima = percentualeContributoMassima;
                  lastPercentualeContributoMinima = percentualeContributoMinima;
                  range.setCodiceLivello(codiceLivello);
                  range.setPercentualeContributoMassima(
                      percentualeContributoMassima);
                  range.setPercentualeContributoMinima(
                      percentualeContributoMinima);
                  result.add(range);
                }
                int idLivello = rs.getInt("ID_LIVELLO");
                if (lastIdLivello == null
                    || idLivello != lastIdLivello.intValue())
                {
                  range.getLivelli().add(new DecodificaDTO<Integer>(idLivello,
                      rs.getString("CODICE"), null));
                }
                range.getIdInterventi().add(rs.getLong("ID_INTERVENTO"));
              }
              return result.isEmpty() ? null : result;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntervento", idIntervento),
          },
          new LogVariable[]
          {
          // new LogParameter("idVariabile", idVariabile),
          }, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  public List<RigaRendicontazioneSpese> getElencoRendicontazioneSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoRendicontazioneSpese]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                  \n"
        + "   REN_CON_DOC_LEGACY AS                                                               \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       DECODE(COUNT(*),0,'S','N') AS REND_DOC                                          \n"
        + "     FROM                                                                              \n"
        + "       IUF_T_RENDICONTAZIONE_SPESE RS,                                                 \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                             \n"
        + "       IUF_T_PROCEDIMENTO P,                                                           \n"
        + "       IUF_D_BANDO B,                                                                  \n"
        + "       IUF_R_LIVELLO_INTERVENTO LI,                                                    \n"
        + "       IUF_R_LIV_BANDO_INTERVENTO LBI,                                                 \n"
        + "       IUF_T_INTERVENTO I                                                              \n"
        + "     WHERE                                                                             \n"
        + "       PO_ORIG.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "       AND PO_ORIG.ID_PROCEDIMENTO       = PO.ID_PROCEDIMENTO                          \n"
        + "       AND PO.ID_PROCEDIMENTO            = P.ID_PROCEDIMENTO                           \n"
        + "       AND P.ID_BANDO                    = B.ID_BANDO                                  \n"
        + "       AND LI.ID_LIVELLO                 = LBI.ID_LIVELLO                              \n"
        + "       AND LBI.ID_BANDO                  = B.ID_BANDO                                  \n"
        + "       AND LBI.ID_DESCRIZIONE_INTERVENTO = LI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "       AND RS.ID_PROCEDIMENTO_OGGETTO    = PO.ID_PROCEDIMENTO_OGGETTO                  \n"
        + "       AND RS.IMPORTO_SPESA              > 0                                           \n"
        + "       AND RS.ID_INTERVENTO              = I.ID_INTERVENTO                             \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO   = LI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "       AND LI.FLAG_DOCUMENTO_SPESA       = 'S'                                         \n"
        + "       AND NOT EXISTS                                                                  \n"
        + "       (                                                                               \n"
        + "         SELECT                                                                        \n"
        + "           *                                                                           \n"
        + "         FROM                                                                          \n"
        + "           IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,                                         \n"
        + "           IUF_R_DOCUMENTO_SPESA_INTERV DSI                                          \n"
        + "         WHERE                                                                         \n"
        + "           DSI.ID_DOCUMENTO_SPESA_INTERVEN   = DSIPO.ID_DOCUMENTO_SPESA_INTERVEN       \n"
        + "           AND DSIPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO              \n"
        + "           AND DSI.ID_INTERVENTO             = I.ID_INTERVENTO                         \n"
        + "       )                                                                               \n"
        + "   )                                                                                   \n"
        + "   ,                                                                                   \n"
        + "   REN_CON_DOC AS                                                                      \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       LI.ID_LIVELLO,                                                                  \n"
        + "       LI.ID_DESCRIZIONE_INTERVENTO,                                                   \n"
        + "       CASE RCDL.REND_DOC                                                              \n"
        + "         WHEN 'N'                                                                      \n"
        + "         THEN 'N'                                                                      \n"
        + "         ELSE DECODE(B.FLAG_RENDICONTAZIONE_DOC_SPESA,'S',LI.FLAG_DOCUMENTO_SPESA)     \n"
        + "       END AS FLAG_DOCUMENTO_SPESA                                                     \n"
        + "     FROM                                                                              \n"
        + "       REN_CON_DOC_LEGACY RCDL,                                                        \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                  \n"
        + "       IUF_T_PROCEDIMENTO P,                                                           \n"
        + "       IUF_D_BANDO B,                                                                  \n"
        + "       IUF_R_LIVELLO_INTERVENTO LI,                                                    \n"
        + "       IUF_R_LIV_BANDO_INTERVENTO LBI                                                  \n"
        + "     WHERE                                                                             \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO        = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "       AND PO.ID_PROCEDIMENTO            = P.ID_PROCEDIMENTO                           \n"
        + "       AND P.ID_BANDO                    = B.ID_BANDO                                  \n"
        + "       AND LI.ID_LIVELLO                 = LBI.ID_LIVELLO                              \n"
        + "       AND LBI.ID_BANDO                  = B.ID_BANDO                                  \n"
        + "       AND LBI.ID_DESCRIZIONE_INTERVENTO = LI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "   )                                                                                   \n"
        + "   ,                                                                                   \n"
        + "   REN_PREC AS                                                                         \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       RS.*                                                                            \n"
        + "     FROM                                                                              \n"
        + "       IUF_T_RENDICONTAZIONE_SPESE RS,                                                 \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                             \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                  \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO                                              \n"
        + "     WHERE                                                                             \n"
        + "       PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "       AND PO_ORIG.ID_PROCEDIMENTO     = PO.ID_PROCEDIMENTO                            \n"
        + "       AND RS.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO                    \n"
        + "       AND PO.CODICE_RAGGRUPPAMENTO    < PO_ORIG.CODICE_RAGGRUPPAMENTO                 \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO  = IPO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                      \n"
        + "       AND IPO.DATA_FINE IS NULL                                                       \n"
        + "       AND EXISTS                                                                      \n"
        + "       (                                                                               \n"
        + "         SELECT                                                                        \n"
        + "           *                                                                           \n"
        + "         FROM                                                                          \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO ISTR,                                            \n"
        + "           IUF_D_ESITO ES                                                              \n"
        + "         WHERE                                                                         \n"
        + "           ISTR.ID_PROCEDIMENTO            = PO.ID_PROCEDIMENTO                        \n"
        + "           AND RS.ID_PROCEDIMENTO_OGGETTO <> ISTR.ID_PROCEDIMENTO_OGGETTO              \n"
        + "           AND PO.CODICE_RAGGRUPPAMENTO    = ISTR.CODICE_RAGGRUPPAMENTO                \n"
        + "           AND ISTR.ID_ESITO               = ES.ID_ESITO                               \n"
        + "           AND ES.CODICE                   = 'APP-P'                                   \n"
        + "       )                                                                               \n"
        + "   )                                                                                   \n"
        + "   ,                                                                                   \n"
        + "   ACC_PREC AS                                                                         \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       TAS.*                                                                           \n"
        + "     FROM                                                                              \n"
        + "       IUF_T_ACCERTAMENTO_SPESE TAS ,                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                             \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                  \n"
        + "       IUF_D_ESITO E,                                                                  \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO                                              \n"
        + "     WHERE                                                                             \n"
        + "       PO_ORIG.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "       AND PO_ORIG.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                           \n"
        + "       AND TAS.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       AND TAS.ID_PROCEDIMENTO_OGGETTO <> PO_ORIG.ID_PROCEDIMENTO_OGGETTO              \n"
        + "       AND PO.DATA_FINE                <= NVL(PO_ORIG.DATA_FINE,SYSDATE)               \n"
        + "       AND IPO.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       AND IPO.DATA_FINE               IS NULL                                         \n"
        + "       AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                      \n"
        + "       AND PO.ID_ESITO = E.ID_ESITO                                                    \n"
        + "       AND E.CODICE = 'APP-P'                                                          \n"
        + "   )                                                                                   \n"
        + "   ,                                                                                   \n"
        + "   RENDICONTAZIONE_CORRENTE AS                                                         \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       *                                                                               \n"
        + "     FROM                                                                              \n"
        + "       IUF_T_RENDICONTAZIONE_SPESE                                                     \n"
        + "     WHERE                                                                             \n"
        + "       ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                              \n"
        + "   )                                                                                   \n"
        + " SELECT                                                                                \n"
        + "   I.ID_INTERVENTO,                                                                    \n"
        + "   DI.ID_DETTAGLIO_INTERVENTO,                                                         \n"
        + "   DI.PROGRESSIVO,                                                                     \n"
        + "   TA.DESCRIZIONE      AS DESC_TIPO_INTERVENTO,                                        \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "   NVL(DI.IMPORTO_AMMESSO,0)        AS SPESA_AMMESSA,                                  \n"
        + "   NVL(DI.PERCENTUALE_CONTRIBUTO,0) AS PERCENTUALE_CONTRIBUTO,                         \n"
        + "   NVL(DI.IMPORTO_CONTRIBUTO,0)     AS IMPORTO_CONTRIBUTO,                             \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       NVL(SUM(NVL(REN_PREC.IMPORTO_SPESA,0)),0)                                       \n"
        + "     FROM                                                                              \n"
        + "       REN_PREC                                                                        \n"
        + "     WHERE                                                                             \n"
        + "       REN_PREC.ID_INTERVENTO = I.ID_INTERVENTO                                        \n"
        + "   ) AS SPESE_SOSTENUTE,                                                               \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       NVL(SUM(NVL(ACC_PREC.IMPORTO_CALCOLO_CONTRIBUTO,0)),0)                          \n"
        + "     FROM                                                                              \n"
        + "       ACC_PREC                                                                        \n"
        + "     WHERE                                                                             \n"
        + "       ACC_PREC.ID_INTERVENTO = I.ID_INTERVENTO                                        \n"
        + "   )                       AS SPESE_ACCERTATE,                                         \n"
        + "   NVL(RS.IMPORTO_SPESA,0) AS IMPORTO_SPESA,                                           \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       NVL(SUM(NVL(ACC_PREC.IMPORTO_NON_RICONOSCIUTO,0)),0)                            \n"
        + "     FROM                                                                              \n"
        + "       ACC_PREC                                                                        \n"
        + "     WHERE                                                                             \n"
        + "       ACC_PREC.ID_INTERVENTO = I.ID_INTERVENTO                                        \n"
        + "   )                              AS IMPORTO_NON_RICONOSCIUTO_PREC,                    \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       NVL(SUM(NVL(ACC_PREC.IMPORTO_DISPONIBILE,0)),0)                                 \n"
        + "     FROM                                                                              \n"
        + "       ACC_PREC                                                                        \n"
        + "     WHERE                                                                             \n"
        + "       ACC_PREC.ID_INTERVENTO = I.ID_INTERVENTO                                        \n"
        + "   )                              AS IMPORTO_DISPONIBILE_PREC,                         \n"
        + "   NVL(RS.CONTRIBUTO_RICHIESTO,0) AS CONTRIBUTO_RICHIESTO,                             \n"
        + "   RS.FLAG_INTERVENTO_COMPLETATO,                                                      \n"
        + "   RS.NOTE,                                                                            \n"
        + "   DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "   RCD.FLAG_DOCUMENTO_SPESA AS USA_DOCUMENTI_SPESA,                                    \n"
        + "   (                                                                                   \n"
        + "     SELECT                                                                            \n"
        + "       DECODE(COUNT(*),0,'N','S')                                                      \n"
        + "     FROM                                                                              \n"
        + "       IUF_R_DOCUMENTO_SPESA_INTERV DSI2                                             \n"
        + "     WHERE                                                                             \n"
        + "       DSI2.ID_INTERVENTO = I.ID_INTERVENTO                                            \n"
        + "   ) AS HAS_DOCUMENTO_SPESA,                                                           \n"
        + "   L.CODICE_LIVELLO                                                                    \n"
        + " FROM                                                                                  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "   IUF_T_INTERVENTO I,                                                                 \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "   IUF_D_TIPO_CLASSIFICAZIONE TC,                                                      \n"
        + "   IUF_R_AGGREGAZIONE_INTERVENT AI,                                                   \n"
        + "   IUF_D_TIPO_AGGREGAZIONE TA,                                                         \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI,                                                      \n"
        + "   RENDICONTAZIONE_CORRENTE RS,                                                        \n"
        + "   REN_CON_DOC RCD,                                                                    \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI,                                                     \n"
        + "   IUF_T_PROCEDIMENTO P,                                                               \n"
        + "   IUF_D_LIVELLO L                                                                     \n"
        + " WHERE                                                                                 \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "   AND I.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                                     \n"
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "   AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO                            \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + "   AND DESCINT.ID_TIPO_CLASSIFICAZIONE   = TC.ID_TIPO_CLASSIFICAZIONE                  \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)               \n"
        + "   AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = AI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "   AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE                     \n"
        + "   AND DI.FLAG_TIPO_OPERAZIONE          <> :TIPO_OPERAZIONE_ELIMINAZIONE               \n"
        + "   AND I.ID_INTERVENTO                   = RS.ID_INTERVENTO(+)                         \n"
        + "   AND RCD.ID_DESCRIZIONE_INTERVENTO     = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO                     \n"
        + "   AND PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                                          \n"
        + "   AND LBI.ID_BANDO = P.ID_BANDO                                                       \n"
        + "   AND LBI.ID_LIVELLO = L.ID_LIVELLO                                                   \n"
        + ((ids != null && !ids.isEmpty())
            ? getInCondition("I.ID_INTERVENTO", ids) : "")
        + " ORDER BY                                                                                      \n"
        + "   DI.PROGRESSIVO ASC                                                                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("TIPO_OPERAZIONE_ELIMINAZIONE",
        IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_ELIMINAZIONE,
        Types.VARCHAR);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.NO, Types.VARCHAR);
    try
    {
      return queryForList(QUERY, mapParameterSource,
          RigaRendicontazioneSpese.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, null, QUERY, mapParameterSource);
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

  public int updateRendicontazioneSpese(long idProcedimentoOggetto,
      RigaRendicontazioneSpese riga)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateRendicontazioneSpese]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                                                         \n"
        + "   IUF_T_RENDICONTAZIONE_SPESE                                                  \n"
        + " SET                                                                            \n"
        + "   CONTRIBUTO_RICHIESTO       = NVL(:CONTRIBUTO_RICHIESTO,CONTRIBUTO_RICHIESTO),\n"
        + "   FLAG_INTERVENTO_COMPLETATO = :FLAG_INTERVENTO_COMPLETATO,                    \n"
        + "   IMPORTO_SPESA              = NVL(:IMPORTO_SPESA,IMPORTO_SPESA),              \n"
        + "   NOTE                       = :NOTE                                           \n"
        + " WHERE                                                                          \n"
        + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                           \n"
        + "   AND ID_INTERVENTO       = :ID_INTERVENTO                                     \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", riga.getIdIntervento(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("NOTE", riga.getNote(), Types.VARCHAR);

      if (!IuffiConstants.FLAGS.SI.equals(riga.getUsaDocumentiSpesa()))
      {
        mapParameterSource.addValue("CONTRIBUTO_RICHIESTO",
            riga.getContributoRichiesto(), Types.NUMERIC);
        mapParameterSource.addValue("IMPORTO_SPESA", riga.getImportoSpesa(),
            Types.NUMERIC);
      }
      else
      {
        mapParameterSource.addValue("CONTRIBUTO_RICHIESTO", null,
            Types.NUMERIC);
        mapParameterSource.addValue("IMPORTO_SPESA", null, Types.NUMERIC);
      }
      mapParameterSource.addValue("FLAG_INTERVENTO_COMPLETATO",
          riga.getFlagInterventoCompletato(), Types.VARCHAR);
      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("riga", riga)
          }, null, UPDATE,
          mapParameterSource);
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

  public int insertRendicontazioneSpese(long idProcedimentoOggetto,
      RigaRendicontazioneSpese riga)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertRendicontazioneSpese]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                      \n"
        + " INTO                                        \n"
        + "   IUF_T_RENDICONTAZIONE_SPESE               \n"
        + "   (                                         \n"
        + "     ID_RENDICONTAZIONE_SPESE,               \n"
        + "     CONTRIBUTO_RICHIESTO,                   \n"
        + "     FLAG_INTERVENTO_COMPLETATO,             \n"
        + "     ID_INTERVENTO,                          \n"
        + "     ID_PROCEDIMENTO_OGGETTO,                \n"
        + "     IMPORTO_SPESA,                          \n"
        + "     NOTE                                    \n"
        + "   )                                         \n"
        + "   VALUES                                    \n"
        + "   (                                         \n"
        + "     SEQ_IUF_T_RENDICONTAZIO_SPES.NEXTVAL, \n"
        + "     :CONTRIBUTO_RICHIESTO ,                 \n"
        + "     :FLAG_INTERVENTO_COMPLETATO ,           \n"
        + "     :ID_INTERVENTO ,                        \n"
        + "     :ID_PROCEDIMENTO_OGGETTO ,              \n"
        + "     :IMPORTO_SPESA ,                        \n"
        + "     :NOTE                                   \n"
        + "   )                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", riga.getIdIntervento(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("NOTE", riga.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_INTERVENTO_COMPLETATO",
          riga.getFlagInterventoCompletato(), Types.VARCHAR);
      if (!IuffiConstants.FLAGS.SI.equals(riga.getUsaDocumentiSpesa()))
      {
        mapParameterSource.addValue("IMPORTO_SPESA", riga.getImportoSpesa(),
            Types.NUMERIC);
        mapParameterSource.addValue("CONTRIBUTO_RICHIESTO",
            riga.getContributoRichiesto(), Types.NUMERIC);
      }
      else
      {
        // Non sono nullabili quindi vanno a ZERO!
        mapParameterSource.addValue("IMPORTO_SPESA", BigDecimal.ZERO,
            Types.NUMERIC);
        mapParameterSource.addValue("CONTRIBUTO_RICHIESTO", BigDecimal.ZERO,
            Types.NUMERIC);
      }
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("riga", riga)
          }, null, INSERT,
          mapParameterSource);
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

  public List<RigaAccertamentoSpese> getElencoAccertamentoSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoAccertamentoSpese]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                   \n"
        + "    REN_CON_DOC_LEGACY AS                                                               \n"
        + "    (                                                                                   \n"
        + "      SELECT                                                                            \n"
        + "        DECODE(COUNT(*),0,'S','N') AS REND_DOC                                          \n"
        + "      FROM                                                                              \n"
        + "        IUF_T_RENDICONTAZIONE_SPESE RS,                                                 \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO,                                                  \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                             \n"
        + "        IUF_T_PROCEDIMENTO P,                                                           \n"
        + "        IUF_D_BANDO B,                                                                  \n"
        + "        IUF_R_LIVELLO_INTERVENTO LI,                                                    \n"
        + "        IUF_R_LIV_BANDO_INTERVENTO LBI,                                                 \n"
        + "        IUF_T_INTERVENTO I                                                              \n"
        + "      WHERE                                                                             \n"
        + "        PO_ORIG.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "        AND PO_ORIG.ID_PROCEDIMENTO       = PO.ID_PROCEDIMENTO                          \n"
        + "        AND PO.ID_PROCEDIMENTO            = P.ID_PROCEDIMENTO                           \n"
        + "        AND P.ID_BANDO                    = B.ID_BANDO                                  \n"
        + "        AND LI.ID_LIVELLO                 = LBI.ID_LIVELLO                              \n"
        + "        AND LBI.ID_BANDO                  = B.ID_BANDO                                  \n"
        + "        AND LBI.ID_DESCRIZIONE_INTERVENTO = LI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "        AND RS.ID_PROCEDIMENTO_OGGETTO    = PO.ID_PROCEDIMENTO_OGGETTO                  \n"
        + "        AND RS.IMPORTO_SPESA              > 0                                           \n"
        + "        AND RS.ID_INTERVENTO              = I.ID_INTERVENTO                             \n"
        + "        AND I.ID_DESCRIZIONE_INTERVENTO   = LI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "        AND LI.FLAG_DOCUMENTO_SPESA       = 'S'                                         \n"
        + "        AND NOT EXISTS                                                                  \n"
        + "        (                                                                               \n"
        + "          SELECT                                                                        \n"
        + "            *                                                                           \n"
        + "          FROM                                                                          \n"
        + "            IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,                                         \n"
        + "            IUF_R_DOCUMENTO_SPESA_INTERV DSI                                          \n"
        + "          WHERE                                                                         \n"
        + "            DSI.ID_DOCUMENTO_SPESA_INTERVEN   = DSIPO.ID_DOCUMENTO_SPESA_INTERVEN       \n"
        + "            AND DSIPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO              \n"
        + "            AND DSI.ID_INTERVENTO             = I.ID_INTERVENTO                         \n"
        + "        )                                                                               \n"
        + "    )                                                                                   \n"
        + "    ,                                                                                   \n"
        + "    REN_CON_DOC AS                                                                      \n"
        + "    (                                                                                   \n"
        + "      SELECT                                                                            \n"
        + "        LI.ID_LIVELLO,                                                                  \n"
        + "        LI.ID_DESCRIZIONE_INTERVENTO,                                                   \n"
        + "        CASE RCDL.REND_DOC                                                              \n"
        + "          WHEN 'N'                                                                      \n"
        + "          THEN 'N'                                                                      \n"
        + "          ELSE DECODE(B.FLAG_RENDICONTAZIONE_DOC_SPESA,'S',LI.FLAG_DOCUMENTO_SPESA)     \n"
        + "        END AS FLAG_DOCUMENTO_SPESA                                                     \n"
        + "      FROM                                                                              \n"
        + "        REN_CON_DOC_LEGACY RCDL,                                                        \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO,                                                  \n"
        + "        IUF_T_PROCEDIMENTO P,                                                           \n"
        + "        IUF_D_BANDO B,                                                                  \n"
        + "        IUF_R_LIVELLO_INTERVENTO LI,                                                    \n"
        + "        IUF_R_LIV_BANDO_INTERVENTO LBI                                                  \n"
        + "      WHERE                                                                             \n"
        + "        PO.ID_PROCEDIMENTO_OGGETTO        = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "        AND PO.ID_PROCEDIMENTO            = P.ID_PROCEDIMENTO                           \n"
        + "        AND P.ID_BANDO                    = B.ID_BANDO                                  \n"
        + "        AND LI.ID_LIVELLO                 = LBI.ID_LIVELLO                              \n"
        + "        AND LBI.ID_BANDO                  = B.ID_BANDO                                  \n"
        + "        AND LBI.ID_DESCRIZIONE_INTERVENTO = LI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "    ),                                                                                  \n"
        + "    REN_CORR AS                                                                         \n"
        + "    (                                                                                   \n"
        + " SELECT                                                                  \n"
        + "   RS.CONTRIBUTO_RICHIESTO,                                              \n"
        + "   RS.IMPORTO_SPESA,                                                     \n"
        + "   RS.ID_INTERVENTO,                                                     \n"
        + "   RS.ID_PROCEDIMENTO_OGGETTO                                            \n"
        + " FROM                                                                    \n"
        + "   IUF_T_RENDICONTAZIONE_SPESE RS                                        \n"
        + " WHERE                                                                   \n"
        + "   RS.ID_PROCEDIMENTO_OGGETTO =                                          \n"
        + "   (                                                                     \n"
        + "     SELECT                                                              \n"
        + "       MAX(PO.ID_PROCEDIMENTO_OGGETTO)                                   \n"
        + "     FROM                                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                               \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                    \n"
        + "       IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                  \n"
        + "       IUF_D_OGGETTO O,                                                  \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                               \n"
        + "       IUF_T_RENDICONTAZIONE_SPESE RS2                                   \n"
        + "     WHERE                                                               \n"
        + "       PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
        + "       AND PO_ORIG.ID_PROCEDIMENTO     = PO.ID_PROCEDIMENTO              \n"
        + "       AND PO.CODICE_RAGGRUPPAMENTO    = PO_ORIG.CODICE_RAGGRUPPAMENTO   \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO <> PO_ORIG.ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO  = IPO.ID_PROCEDIMENTO_OGGETTO     \n"
        + "       AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO    \n"
        + "       AND LGO.ID_OGGETTO              = O.ID_OGGETTO                    \n"
        + "       AND O.FLAG_ISTANZA              = 'S'                             \n"
        + "       AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                        \n"
        + "       AND IPO.DATA_FINE IS NULL                                         \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO = RS2.ID_PROCEDIMENTO_OGGETTO      \n"
        + " )                                                                       \n"
        + "    )                                                                                   \n"
        + "    ,                                                                                   \n"
        + "    REN_PREC AS                                                                         \n"
        + "    (                                                                                   \n"
        + "      SELECT                                                                            \n"
        + "        RS.ID_INTERVENTO,                                                               \n"
        + "        SUM(RS.IMPORTO_SPESA) AS SPESE_SOSTENUTE                                        \n"
        + "      FROM                                                                              \n"
        + "        IUF_T_RENDICONTAZIONE_SPESE RS,                                                 \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                             \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO,                                                  \n"
        + "        IUF_T_ITER_PROCEDIMENTO_OGGE IPO                                              \n"
        + "      WHERE                                                                             \n"
        + "        PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "        AND PO_ORIG.ID_PROCEDIMENTO     = PO.ID_PROCEDIMENTO                            \n"
        + "        AND RS.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO                    \n"
        + "        AND PO.CODICE_RAGGRUPPAMENTO    < PO_ORIG.CODICE_RAGGRUPPAMENTO                 \n"
        + "        AND PO.ID_PROCEDIMENTO_OGGETTO  = IPO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "        AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                      \n"
        + "        AND IPO.DATA_FINE IS NULL                                                       \n"
        + "    AND EXISTS                                                                          \n"
        + "    (                                                                                   \n"
        + "      SELECT                                                                            \n"
        + "        *                                                                               \n"
        + "      FROM                                                                              \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO ISTR,                                                \n"
        + "        IUF_D_ESITO ES                                                                  \n"
        + "      WHERE                                                                             \n"
        + "        ISTR.ID_PROCEDIMENTO         = PO.ID_PROCEDIMENTO                               \n"
        + "        AND RS.ID_PROCEDIMENTO_OGGETTO <> ISTR.ID_PROCEDIMENTO_OGGETTO                  \n"
        + "        AND PO.CODICE_RAGGRUPPAMENTO = ISTR.CODICE_RAGGRUPPAMENTO                       \n"
        + "        AND ISTR.ID_ESITO            = ES.ID_ESITO                                      \n"
        + "        AND ES.CODICE                = 'APP-P'                                          \n"
        + "    )                                                                                   \n"
        + "      GROUP BY                                                                          \n"
        + "        RS.ID_INTERVENTO                                                                \n"
        + "    )                                                                                   \n"
        + "    ,                                                                                   \n"
        + "    ACC_PREC AS                                                                         \n"
        + "    (                                                                                   \n"
        + "      SELECT                                                                            \n"
        + "        ASP.ID_INTERVENTO,                                                              \n"
        + "        SUM(ASP.IMPORTO_ACCERTATO+ASP.IMPORTO_NON_RICONOSCIUTO) AS SPESE_ACCERTATE      \n"
        + "      FROM                                                                              \n"
        + "        IUF_T_ACCERTAMENTO_SPESE ASP,                                                   \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                             \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO,                                                  \n"
        + "        IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                                             \n"
        + "        IUF_D_ESITO E                                                                   \n"
        + "      WHERE                                                                             \n"
        + "        PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "        AND PO_ORIG.ID_PROCEDIMENTO     = PO.ID_PROCEDIMENTO                            \n"
        + "        AND ASP.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                    \n"
        + "        AND PO.CODICE_RAGGRUPPAMENTO    < PO_ORIG.CODICE_RAGGRUPPAMENTO                 \n"
        + "        AND PO.ID_PROCEDIMENTO_OGGETTO  = IPO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "        AND IPO.DATA_FINE              IS NULL                                          \n"
        + "        AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                      \n"
        + "        AND PO.ID_ESITO = E.ID_ESITO                                                    \n"
        + "        AND E.CODICE = 'APP-P'                                                         \n"
        + "      GROUP BY                                                                          \n"
        + "        ASP.ID_INTERVENTO                                                               \n"
        + "    )                                                                                   \n"
        + "    ,                                                                                   \n"
        + "    ACCERTAMENTO_CORRENTE AS                                                            \n"
        + "    (                                                                                   \n"
        + "      SELECT                                                                            \n"
        + "        *                                                                               \n"
        + "      FROM                                                                              \n"
        + "        IUF_T_ACCERTAMENTO_SPESE                                                        \n"
        + "      WHERE                                                                             \n"
        + "        ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                              \n"
        + "    )                                                                                   \n"
        + "  SELECT                                                                                \n"
        + "    I.ID_INTERVENTO,                                                                    \n"
        + "    DI.ID_DETTAGLIO_INTERVENTO,                                                         \n"
        + "    DI.PROGRESSIVO,                                                                     \n"
        + "    TA.DESCRIZIONE      AS DESC_TIPO_INTERVENTO,                                        \n"
        + "    DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "    DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "    NVL(DI.IMPORTO_AMMESSO,0)             AS SPESA_AMMESSA,                             \n"
        + "    NVL(DI.PERCENTUALE_CONTRIBUTO,0)      AS PERCENTUALE_CONTRIBUTO,                    \n"
        + "    NVL(DI.IMPORTO_CONTRIBUTO,0)          AS IMPORTO_CONTRIBUTO,                        \n"
        + "    NVL(REN_PREC.SPESE_SOSTENUTE,0)       AS SPESE_SOSTENUTE,                           \n"
        + "    NVL(ACC_PREC.SPESE_ACCERTATE,0)       AS SPESE_ACCERTATE,                           \n"
        + "    NVL(REN_CORR.IMPORTO_SPESA,0)         AS SPESA_SOSTENUTA_ATTUALE,                   \n"
        + "    NVL(ASP.IMPORTO_ACCERTATO,0)          AS SPESE_ACCERTATE_ATTUALI,                   \n"
        + "    NVL(ASP.IMPORTO_CALCOLO_CONTRIBUTO,0) AS SPESA_RICONOSCIUTA_PER_CALCOLO,            \n"
        + "    NVL(ASP.IMPORTO_NON_RICONOSCIUTO,0)   AS IMPORTO_NON_RICONOSCIUTO,                  \n"
        + "    NVL(ASP.IMPORTO_DISPONIBILE,0)        AS IMPORTO_RISPENDIBILE,                      \n"
        + "    NVL(ASP.CONTRIBUTO_CALCOLATO,0)       AS CONTRIBUTO_CALCOLATO,                      \n"
        + "    ASP.FLAG_INTERVENTO_COMPLETATO,                                                     \n"
        + "    ASP.NOTE,                                                                           \n"
        + "    ASP.CONTRIBUTO_ABBATTUTO,                                                           \n"
        + "    DESCINT.ID_TIPO_LOCALIZZAZIONE,                                                     \n"
        + "    RCD.FLAG_DOCUMENTO_SPESA AS USA_DOCUMENTI_SPESA,                                    \n"
        + "    (                                                                                   \n"
        + "      SELECT                                                                            \n"
        + "        DECODE(COUNT(*),0,'N','S')                                                      \n"
        + "      FROM                                                                              \n"
        + "        IUF_R_DOCUMENTO_SPESA_INTERV DSI2                                             \n"
        + "      WHERE                                                                             \n"
        + "        DSI2.ID_INTERVENTO = I.ID_INTERVENTO                                            \n"
        + "    ) AS HAS_DOCUMENTO_SPESA                                                            \n"
        + "  FROM                                                                                  \n"
        + "    IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "    IUF_T_INTERVENTO I,                                                                 \n"
        + "    IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "    IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "    IUF_D_TIPO_CLASSIFICAZIONE TC,                                                      \n"
        + "    IUF_R_AGGREGAZIONE_INTERVENT AI,                                                   \n"
        + "    IUF_D_TIPO_AGGREGAZIONE TA,                                                         \n"
        + "    IUF_D_CATEGORIA_INTERVENTO CI,                                                      \n"
        + "    ACCERTAMENTO_CORRENTE ASP,                                                          \n"
        + "    REN_PREC,                                                                           \n"
        + "    REN_CORR,                                                                           \n"
        + "    ACC_PREC,                                                                           \n"
        + "    REN_CON_DOC RCD                                                                     \n"
        + "  WHERE                                                                                 \n"
        + "    PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "    AND I.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                                     \n"
        + "    AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "    AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO                            \n"
        + "    AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + "    AND DESCINT.ID_TIPO_CLASSIFICAZIONE   = TC.ID_TIPO_CLASSIFICAZIONE                  \n"
        + "    AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)               \n"
        + "    AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "    AND I.ID_DESCRIZIONE_INTERVENTO       = AI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "    AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE                     \n"
        + "    AND DI.FLAG_TIPO_OPERAZIONE          <> :TIPO_OPERAZIONE_ELIMINAZIONE               \n"
        + "    AND DI.ID_INTERVENTO                  = ASP.ID_INTERVENTO(+)                        \n"
        + "    AND DI.ID_INTERVENTO                  = REN_CORR.ID_INTERVENTO(+)                   \n"
        + "    AND DI.ID_INTERVENTO                  = REN_PREC.ID_INTERVENTO(+)                   \n"
        + "    AND DI.ID_INTERVENTO                  = ACC_PREC.ID_INTERVENTO(+)                   \n"
        + "    AND RCD.ID_DESCRIZIONE_INTERVENTO     = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + ((ids != null && !ids.isEmpty())
            ? getInCondition("I.ID_INTERVENTO", ids) : "")
        + " ORDER BY                                                                                      \n"
        + "   DI.PROGRESSIVO ASC                                                                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("TIPO_OPERAZIONE_ELIMINAZIONE",
        IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_ELIMINAZIONE,
        Types.VARCHAR);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.NO, Types.VARCHAR);
    try
    {
      return queryForList(QUERY, mapParameterSource,
          RigaAccertamentoSpese.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, null, QUERY, mapParameterSource);
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

  public int updateAccertamentoSpese(long idProcedimentoOggetto,
      AccertamentoSpeseDTO riga)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateAccertamentoSpese]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    boolean presenteFlagIntervento = !GenericValidator
        .isBlankOrNull(riga.getFlagInterventoCompletato());

    final StringBuilder UPDATE = new StringBuilder(
        " UPDATE                                                      \n"
            + "   IUF_T_ACCERTAMENTO_SPESE                                  \n"
            + " SET                                                         \n");

    if (presenteFlagIntervento)
    {
      UPDATE.append(
          "   FLAG_INTERVENTO_COMPLETATO = :FLAG_INTERVENTO_COMPLETATO, \n");
    }
    if (!riga.isUsaDocumentiSpesa())
    {
      UPDATE.append(
          "   IMPORTO_ACCERTATO          = :IMPORTO_ACCERTATO,          \n"
              + "   IMPORTO_CALCOLO_CONTRIBUTO = :IMPORTO_CALCOLO_CONTRIBUTO, \n"
              + "   IMPORTO_DISPONIBILE        = :IMPORTO_DISPONIBILE,        \n"
              + "   IMPORTO_NON_RICONOSCIUTO   = :IMPORTO_NON_RICONOSCIUTO,   \n"
              + "   CONTRIBUTO_CALCOLATO       = :CONTRIBUTO_CALCOLATO,       \n");
    }
    UPDATE.append(
        "   NOTE                       = :NOTE                        \n"
            + " WHERE                                                       \n"
            + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
            + "   AND ID_INTERVENTO       = :ID_INTERVENTO                  \n");
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", riga.getIdIntervento(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("NOTE", riga.getNote(), Types.VARCHAR);
      if (presenteFlagIntervento)
      {
        mapParameterSource.addValue("FLAG_INTERVENTO_COMPLETATO",
            riga.getFlagInterventoCompletato(), Types.VARCHAR);
      }
      mapParameterSource.addValue("IMPORTO_ACCERTATO",
          riga.getImportoAccertato(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_CALCOLO_CONTRIBUTO",
          riga.getImportoCalcoloContributo(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_DISPONIBILE",
          riga.getImportoDisponibile(), Types.NUMERIC);
      mapParameterSource.addValue("CONTRIBUTO_CALCOLATO",
          riga.getContributoCalcolato(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_NON_RICONOSCIUTO",
          riga.getImportoNonRiconosciuto(), Types.NUMERIC);
      return namedParameterJdbcTemplate.update(UPDATE.toString(),
          mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("riga", riga)
          }, null, UPDATE.toString(),
          mapParameterSource);
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

  public int insertAccertamentoSpese(long idProcedimentoOggetto,
      AccertamentoSpeseDTO riga)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertAccertamentoSpese]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                    \n"
        + " INTO                                      \n"
        + "   IUF_T_ACCERTAMENTO_SPESE                \n"
        + "   (                                       \n"
        + "     FLAG_INTERVENTO_COMPLETATO,           \n"
        + "     ID_ACCERTAMENTO_SPESE,                \n"
        + "     ID_INTERVENTO,                        \n"
        + "     ID_PROCEDIMENTO_OGGETTO,              \n"
        + "     IMPORTO_ACCERTATO,                    \n"
        + "     IMPORTO_CALCOLO_CONTRIBUTO,           \n"
        + "     IMPORTO_DISPONIBILE,                  \n"
        + "     IMPORTO_NON_RICONOSCIUTO,             \n"
        + "     CONTRIBUTO_CALCOLATO,                 \n"
        + "     NOTE                                  \n"
        + "   )                                       \n"
        + "   VALUES                                  \n"
        + "   (                                       \n"
        + "     :FLAG_INTERVENTO_COMPLETATO,          \n"
        + "     SEQ_IUF_T_ACCERTAMENTO_SPESE.NEXTVAL, \n"
        + "     :ID_INTERVENTO,                       \n"
        + "     :ID_PROCEDIMENTO_OGGETTO,             \n"
        + "     :IMPORTO_ACCERTATO,                   \n"
        + "     :IMPORTO_CALCOLO_CONTRIBUTO,          \n"
        + "     :IMPORTO_DISPONIBILE,                 \n"
        + "     :IMPORTO_NON_RICONOSCIUTO,            \n"
        + "     :CONTRIBUTO_CALCOLATO,                \n"
        + "     :NOTE                                 \n"
        + "   )                                       \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", riga.getIdIntervento(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("NOTE", riga.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_INTERVENTO_COMPLETATO",
          riga.getFlagInterventoCompletato(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_ACCERTATO",
          riga.getImportoAccertato(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_CALCOLO_CONTRIBUTO",
          riga.getImportoCalcoloContributo(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_DISPONIBILE",
          riga.getImportoDisponibile(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_NON_RICONOSCIUTO",
          riga.getImportoNonRiconosciuto(), Types.NUMERIC);
      mapParameterSource.addValue("CONTRIBUTO_CALCOLATO",
          riga.getContributoCalcolato(), Types.NUMERIC);
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("riga", riga)
          }, null, INSERT,
          mapParameterSource);
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

  public void updateMisurazioniInterventoSaldo(long idDettIntervProcOgg,
      List<MisurazioneInterventoDTO> list)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::updateMisurazioniInterventoSaldo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                                       \n"
        + "   IUF_R_DETT_INTE_PROC_OGG_MIS                             \n"
        + " SET                                                          \n"
        + "   QUANTITA = :QUANTITA                                       \n"
        + " WHERE                                                        \n"
        + "   ID_DETT_INTERV_PROC_OGG       = :ID_DETT_INTERV_PROC_OGG   \n"
        + "   AND ID_MISURAZIONE_INTERVENTO = :ID_MISURAZIONE_INTERVENTO \n";

    int size = list.size();
    MapSqlParameterSource[] batchParameters = new MapSqlParameterSource[size];
    try
    {
      int idx = 0;
      for (MisurazioneInterventoDTO misurazione : list)
      {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
            idDettIntervProcOgg, Types.NUMERIC);
        mapParameterSource.addValue("ID_MISURAZIONE_INTERVENTO",
            misurazione.getIdMisurazioneIntervento(),
            Types.NUMERIC);
        mapParameterSource.addValue("QUANTITA", misurazione.getValore(),
            Types.NUMERIC);
        batchParameters[idx++] = mapParameterSource;
      }
      namedParameterJdbcTemplate.batchUpdate(UPDATE, batchParameters);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("list", list)
          },
          (LogVariable[]) null, UPDATE, batchParameters);
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

  public List<DecodificaDTO<Long>> findAttivitaDisponibiliPerProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAttivitaDisponibiliPerProcedimentoOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                       \n"
        + "   A.ID_ATTIVITA AS CODICE,                                   \n"
        + "   A.ID_ATTIVITA AS ID,                                       \n"
        + "   A.TITOLO_ATTIVITA AS DESCRIZIONE                           \n"
        + " FROM                                                         \n"
        + "   IUF_T_ATTIVITA A                                           \n"
        + " WHERE                                                        \n"
        + "   EXISTS                                                     \n"
        + "   (                                                          \n"
        + "     SELECT                                                   \n"
        + "       *                                                      \n"
        + "     FROM                                                     \n"
        + "       IUF_T_DATI_PROCEDIMENTO DP,                            \n"
        + "       IUF_R_ATTIVITA_PARTECIPANTE AP                         \n"
        + "     WHERE                                                    \n"
        + "       DP.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND DP.ID_DATI_PROCEDIMENTO = A.ID_DATI_PROCEDIMENTO   \n"
        + "       AND A.ID_ATTIVITA           = AP.ID_ATTIVITA           \n"
        + "   )                                                          \n"
        + " ORDER BY                                                     \n"
        + "   A.TITOLO_ATTIVITA                                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
          {
          // new LogParameter("idVariabile", idVariabile),
          }, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  public List<DecodificaDTO<Long>> findPartecipantiAttivitaInProcedimentoOggetto(
      long idProcedimentoOggetto, long idAttivita)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findPartecipantiAttivitaInProcedimentoOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                                                                           \n"
        + "   P.ID_PARTECIPANTE                      AS CODICE,                                                              \n"
        + "   P.ID_PARTECIPANTE                      AS ID,                                                                  \n"
        + "   NVL(P.DENOMINAZIONE, DA.DENOMINAZIONE) AS DESCRIZIONE                                                          \n"
        + " FROM                                                                                                             \n"
        + "   IUF_T_DATI_PROCEDIMENTO DP,                                                                                    \n"
        + "   IUF_R_ATTIVITA_PARTECIPANTE AP,                                                                                \n"
        + "   IUF_T_ATTIVITA A,                                                                                              \n"
        + "   IUF_T_PARTECIPANTE P                                                                                           \n"
        + " LEFT JOIN SMRGAA_V_DATI_ANAGRAFICI DA                                                                            \n"
        + " ON                                                                                                               \n"
        + "   P.EXT_ID_AZIENDA = DA.ID_AZIENDA                                                                               \n"
        + "   AND SYSDATE BETWEEN DA.DATA_INIZIO_VALIDITA AND NVL(DA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
        + " WHERE                                                                                                            \n"
        + "   DP.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO                                                         \n"
        + "   AND DP.ID_DATI_PROCEDIMENTO = A.ID_DATI_PROCEDIMENTO                                                           \n"
        + "   AND A.ID_ATTIVITA           = AP.ID_ATTIVITA                                                                   \n"
        + "   AND A.ID_ATTIVITA           = :ID_ATTIVITA                                                                     \n"
        + "   AND P.ID_PARTECIPANTE       = AP.ID_PARTECIPANTE                                                               \n"
        + " ORDER BY                                                                                                         \n"
        + "   NVL(P.DENOMINAZIONE, DA.DENOMINAZIONE) ASC                                                                     \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_ATTIVITA", idAttivita, Types.NUMERIC);
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idAttivita", idAttivita)
          },
          new LogVariable[]
          {
          }, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  public void insertLegameInterventoPartecipantiAttivita(long idIntervento,
      Long idProcedimentoOggetto, Long idAttivita, Long idPartecipante)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                                       \n"
        + " INTO                                                         \n"
        + "   IUF_R_ATTIV_PARTECIP_INTERV                                \n"
        + "   (                                                          \n"
        + "     ID_ATTIVITA_PARTECIPANTE,                                \n"
        + "     ID_INTERVENTO                                            \n"
        + "   )                                                          \n"
        + "   (                                                          \n"
        + "     SELECT                                                   \n"
        + "       AP.ID_ATTIVITA_PARTECIPANTE,                           \n"
        + "       :ID_INTERVENTO                                         \n"
        + "     FROM                                                     \n"
        + "       IUF_T_DATI_PROCEDIMENTO DP,                            \n"
        + "       IUF_R_ATTIVITA_PARTECIPANTE AP,                        \n"
        + "       IUF_T_ATTIVITA A,                                      \n"
        + "       IUF_T_PARTECIPANTE P                                   \n"
        + "     WHERE                                                    \n"
        + "       DP.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND DP.ID_DATI_PROCEDIMENTO = A.ID_DATI_PROCEDIMENTO   \n"
        + "       AND A.ID_ATTIVITA           = AP.ID_ATTIVITA           \n"
        + "       AND A.ID_ATTIVITA           = :ID_ATTIVITA             \n"
        + "       AND P.ID_PARTECIPANTE       = AP.ID_PARTECIPANTE       \n"
        + "       AND P.ID_PARTECIPANTE       = :ID_PARTECIPANTE         \n"
        + "   )                                                          \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_ATTIVITA", idAttivita, Types.NUMERIC);
      mapParameterSource.addValue("ID_PARTECIPANTE", idPartecipante,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("idAttivita", idAttivita),
              new LogParameter("idPartecipante", idPartecipante)
          },
          (LogVariable[]) null, INSERT, mapParameterSource);
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

  public List<DecodificaDualLIstDTO<Long>> getElencoInterventiPerDocSpesa(
      long idProcedimento, Vector<Long> idsDocSpesa, boolean disponibili)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoInterventiDisponibiliPerDocSpesa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                       \n"
        + "    A.ID_INTERVENTO,                                                          \n"
        + "    B.PROGRESSIVO || ' - ' || DESCINT.DESCRIZIONE || ' - ' || b.ULTERIORI_INFORMAZIONI as DESCRIZIONE, \n"
        + "    DECODE((                                                                  \n"
        + "        SELECT MAX(B1.ID_INTERVENTO )                                              \n"
        + "        FROM                                                                  \n"
        + "          IUF_T_RENDICONTAZIONE_SPESE B1                                      \n"
        + "        WHERE                                                                 \n"
        + "          B1.ID_INTERVENTO = A.ID_INTERVENTO                                  \n"
        + "          AND B1.IMPORTO_SPESA > 0	                                    \n"
        + "    ),NULL,'N','S') AS FLAG_RENDICONTATO,                                     \n"
        + "    DECODE((                                                                  \n"
        + "        SELECT MAX(B1.ID_INTERVENTO)                                               \n"
        + "        FROM                                                                  \n"
        + "          IUF_T_RENDICONTAZIONE_SPESE B1,                                     \n"
        + "          IUF_R_DOC_SPESA_INT_PROC_OGG C1,                                    \n"
        + "          IUF_R_DOCUMENTO_SPESA_INTERV D1                                   \n"
        + "        WHERE                                                                 \n"
        + "          B1.ID_INTERVENTO = A.ID_INTERVENTO                                  \n"
        + "          AND D1.ID_INTERVENTO = B1.ID_INTERVENTO                             \n"
        + "          AND C1.ID_DOCUMENTO_SPESA_INTERVEN = D1.ID_DOCUMENTO_SPESA_INTERVEN \n"
        + "    ),NULL,'N','S') AS FLAG_REND_DOC_SPESA ,                                   \n"
        + "         TA.DESCRIZIONE AS GRUPPO    	                                  \n"
        + "  FROM                                                                        \n"
        + "    IUF_T_INTERVENTO A,                                                       \n"
        + "    IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                     \n"
        + "    IUF_T_DETTAGLIO_INTERVENTO B,                                             \n"
        + "    IUF_R_LIV_BANDO_INTERVENTO BI,                                             \n"
        + "    IUF_R_LIVELLO_INTERVENTO C,                                               \n"
        + "         IUF_R_AGGREGAZIONE_INTERVENT AI,                                     \n"
        + "         IUF_D_TIPO_AGGREGAZIONE TA                                            \n"

        + "  WHERE                                                                       \n"
        + "    A.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                                   \n"
        + "    AND A.ID_INTERVENTO = B.ID_INTERVENTO                                     \n"
        + "    AND DESCINT.ID_DESCRIZIONE_INTERVENTO = A.ID_DESCRIZIONE_INTERVENTO       \n"
        + "    AND SYSDATE BETWEEN B.DATA_INIZIO AND NVL(B.DATA_FINE,SYSDATE)            \n"
        + "    AND C.ID_DESCRIZIONE_INTERVENTO = A.ID_DESCRIZIONE_INTERVENTO             \n"

        + "    AND BI.ID_DESCRIZIONE_INTERVENTO = A.ID_DESCRIZIONE_INTERVENTO             \n"
        + "    AND BI.ID_BANDO = (SELECT ID_BANDO FROM IUF_T_PROCEDIMENTO WHERE ID_PROCEDIMENTO = A.ID_PROCEDIMENTO)             \n"

        + "    AND C.FLAG_DOCUMENTO_SPESA <> 'N'                                         \n"
        + "    AND C.ID_LIVELLO = BI.ID_LIVELLO                                           \n"
        + "         AND DESCINT.ID_DESCRIZIONE_INTERVENTO      = AI.ID_DESCRIZIONE_INTERVENTO  \n"
        + "         AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE       \n"

        + "   AND " + (disponibili ? "NOT" : "")
        + " EXISTS (                                                          \n"
        + "       SELECT DSI.ID_INTERVENTO                                              \n"
        + "       FROM IUF_R_DOCUMENTO_SPESA_INTERV DSI                               \n"
        + "       WHERE "
        + getInCondition("DSI.ID_DOCUMENTO_SPESA", idsDocSpesa, false) + " \n"
        + " AND DSI.ID_INTERVENTO = A.ID_INTERVENTO                             		\n"
        + "   )                                                                         \n"
        + " ORDER BY B.PROGRESSIVO, DESCINT.DESCRIZIONE  ,TA.ID_TIPO_AGGREGAZIONE                                               \n"
        + "                                                                             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    try
    {
      return (List<DecodificaDualLIstDTO<Long>>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<List<DecodificaDualLIstDTO<Long>>>()
              {
                @Override
                public List<DecodificaDualLIstDTO<Long>> extractData(
                    ResultSet rs)
                    throws SQLException, DataAccessException
                {
                  List<DecodificaDualLIstDTO<Long>> list = new ArrayList<>();
                  while (rs.next())
                  {
                    if ("S".equals(rs.getString("FLAG_RENDICONTATO"))
                        && "N".equals(rs.getString("FLAG_REND_DOC_SPESA")))
                      return null;

                    list.add(new DecodificaDualLIstDTO<Long>(
                        rs.getLong("ID_INTERVENTO"),
                        rs.getString("DESCRIZIONE"),
                        rs.getString("DESCRIZIONE"), rs.getString("GRUPPO")));
                  }

                  return list;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimento", idProcedimento)
          },
          new LogVariable[]
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

  public List<RigaJSONInterventoQuadroEconomicoByLivelloDTO> getElencoInterventiByLivelliQuadroEconomico(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoInterventiByLivelliQuadroEconomico]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                                                                     \n"
        + "         DL.ID_LIVELLO,                                                                    \n"
        + "         DL.CODICE_LIVELLO,                                                                    \n"
        + "         I.ID_INTERVENTO,                                                                    \n"

        + "         DI.ID_DETTAGLIO_INTERVENTO,                                                         \n"
        + "         NULL AS ID_DETT_INTERV_PROC_OGG,                                                    \n"
        + "         DI.PROGRESSIVO,                                                                     \n"
        + "         DI.ULTERIORI_INFORMAZIONI,                                                          \n"
        + "         DI.IMPORTO_INVESTIMENTO,                                                            \n"
        + "         DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                             \n"
        + "         NULL AS FLAG_TIPO_OPERAZIONE,                                                       \n"
        + "         DI.IMPORTO_AMMESSO,                                                                 \n"
        + "         DI.PERCENTUALE_CONTRIBUTO,                                                          \n"
        + "         DI.IMPORTO_CONTRIBUTO,                                                               \n"
        + "         (SELECT DATA_AMMISSIONE FROM IUF_R_PROCEDIMENTO_LIVELLO 								\n"
        + "			  WHERE ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO AND ID_LIVELLO = LBI.ID_LIVELLO   ) AS DATA_AMMISSIONE  \n"
        + "       FROM                                                                                  \n"
        + "         IUF_T_PROCEDIMENTO P,                                                      			\n"
        + "         IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "         IUF_T_INTERVENTO I,                                                                 \n"
        + "         IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "         IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "         IUF_D_CATEGORIA_INTERVENTO CI,                                                      \n"
        + "         IUF_R_LIV_BANDO_INTERVENTO LBI,                                                     \n"
        + "         IUF_D_LIVELLO DL                                                                    \n"
        + "       WHERE                                                                                 \n"
        + "         PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "         AND I.ID_PROCEDIMENTO    = PO.ID_PROCEDIMENTO                                       \n"
        + "         AND P.ID_PROCEDIMENTO    = PO.ID_PROCEDIMENTO                                       \n"
        + "         AND P.ID_BANDO    = LBI.ID_BANDO                                       				\n"
        + "         AND DESCINT.ID_DESCRIZIONE_INTERVENTO    = LBI.ID_DESCRIZIONE_INTERVENTO            \n"
        + "         AND DL.ID_LIVELLO    = LBI.ID_LIVELLO            \n"
        + "         AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "         AND I.ID_INTERVENTO               = DI.ID_INTERVENTO                                \n"
        + "         AND DI.FLAG_TIPO_OPERAZIONE <> 'D'					                                \n"
        + "         AND I.ID_DESCRIZIONE_INTERVENTO   = DESCINT.ID_DESCRIZIONE_INTERVENTO               \n"
        + "         AND DESCINT.ID_CATEGORIA_INTERVENTO        = CI.ID_CATEGORIA_INTERVENTO(+)          \n"
        + "         AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "         AND NOT EXISTS                                                                      \n"
        + "         (                                                                                   \n"
        + "           SELECT                                                                            \n"
        + "             *                                                                               \n"
        + "           FROM                                                                              \n"
        + "             IUF_W_DETT_INTERV_PROC_OGG WTMP                                                 \n"
        + "           WHERE                                                                             \n"
        + "             WTMP.ID_INTERVENTO               = I.ID_INTERVENTO                              \n"
        + "             AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "         )                                                                                   \n"
        + "       ORDER BY                  DL.ORDINAMENTO,                                             \n"
        + "         DI.PROGRESSIVO ASC NULLS LAST                                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.NO, Types.VARCHAR);
    try
    {
      return (List<RigaJSONInterventoQuadroEconomicoByLivelloDTO>) namedParameterJdbcTemplate
          .query(QUERY, mapParameterSource,
              new ResultSetExtractor<List<RigaJSONInterventoQuadroEconomicoByLivelloDTO>>()
              {
                @Override
                public List<RigaJSONInterventoQuadroEconomicoByLivelloDTO> extractData(
                    ResultSet rs)
                    throws SQLException, DataAccessException
                {
                  List<RigaJSONInterventoQuadroEconomicoByLivelloDTO> list = new ArrayList<RigaJSONInterventoQuadroEconomicoByLivelloDTO>();
                  RigaJSONInterventoQuadroEconomicoByLivelloDTO rigaDTO = null;
                  long idLivelloLast, idLivelloRef = 0;
                  while (rs.next())
                  {
                    idLivelloLast = rs.getLong("ID_LIVELLO");
                    if (idLivelloLast != idLivelloRef)
                    {
                      idLivelloRef = idLivelloLast;
                      long idIntervento = rs.getLong("ID_INTERVENTO");
                      rigaDTO = new RigaJSONInterventoQuadroEconomicoByLivelloDTO();
                      rigaDTO.setIdLivello(idLivelloLast);
                      rigaDTO.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                      rigaDTO.setIdIntervento(idIntervento);
                      rigaDTO.setIdDettaglioIntervento(
                          getLongNull(rs, "ID_DETTAGLIO_INTERVENTO"));
                      rigaDTO.setIdDettIntervProcOgg(
                          getLongNull(rs, "ID_DETT_INTERV_PROC_OGG"));
                      rigaDTO.setProgressivo(getIntegerNull(rs, "PROGRESSIVO"));
                      rigaDTO
                          .setDescIntervento(rs.getString("DESC_INTERVENTO"));
                      rigaDTO.setUlterioriInformazioni(
                          rs.getString("ULTERIORI_INFORMAZIONI"));
                      rigaDTO.setFlagTipoOperazione(
                          rs.getString("FLAG_TIPO_OPERAZIONE"));

                      rigaDTO.setImportoInvestimento(BigDecimal.ZERO);
                      rigaDTO.setImportoAmmesso(BigDecimal.ZERO);
                      rigaDTO.setImportoContributo(BigDecimal.ZERO);
                      rigaDTO.setDataAmmissione(rs.getDate("DATA_AMMISSIONE"));
                      list.add(rigaDTO);
                    }

                    rigaDTO
                        .setImportoInvestimento(rigaDTO.getImportoInvestimento()
                            .add(rs.getBigDecimal("IMPORTO_INVESTIMENTO")));
                    rigaDTO.setImportoAmmesso(rigaDTO.getImportoAmmesso()
                        .add(rs.getBigDecimal("IMPORTO_AMMESSO")));
                    rigaDTO.setImportoContributo(rigaDTO.getImportoContributo()
                        .add(rs.getBigDecimal("IMPORTO_CONTRIBUTO")));
                    if (rigaDTO.getImportoContributo()
                        .compareTo(BigDecimal.ZERO) == 0)
                    {
                      rigaDTO.setDataAmmissione(null);
                    }
                  }
                  return list;
                }
              });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
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

  public RigaElencoInterventi getDettaglioInterventoById(long idProcedimento,
      long idIntervento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDettaglioInterventoById]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                              \n"
        + "   DI.PROGRESSIVO,                                                   \n"
        + "   A.ID_INTERVENTO,                                                  \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                                        \n"
        + "   B.DESCRIZIONE AS DESC_INTERVENTO                                  \n"
        + " FROM                                                                \n"
        + "   IUF_T_INTERVENTO A,                                               \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO B,                                   \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI                                     \n"
        + " WHERE                                                               \n"
        + "   A.ID_INTERVENTO = :ID_INTERVENTO                                  \n"
        + "   AND A.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                          \n"
        + "   AND A.ID_DESCRIZIONE_INTERVENTO = B.ID_DESCRIZIONE_INTERVENTO     \n"
        + "   AND DI.ID_INTERVENTO = A.ID_INTERVENTO                            \n"
        + "   AND SYSDATE BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.INTEGER);
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.INTEGER);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<RigaElencoInterventi>()
          {

            @Override
            public RigaElencoInterventi extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              RigaElencoInterventi result = null;
              if (rs.next())
              {
                result = new RigaElencoInterventi();
                result.setProgressivo(rs.getInt("PROGRESSIVO"));
                result.setIdIntervento(rs.getLong("ID_INTERVENTO"));
                result.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                result.setUlterioriInformazioni(
                    rs.getString("ULTERIORI_INFORMAZIONI"));
              }
              return result;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimento", idProcedimento) }, null, QUERY, mapParameterSource);
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

  public BigDecimal getSommamportiDocumentoSpesaIntervento(
      long idDocumentoSpesa) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getSommamportiDocumentoSpesaIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                         \n"
        + "   NVL(SUM(NVL(DSI.IMPORTO,0)),0)               \n"
        + " FROM                                           \n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV DSI           \n"
        + " WHERE                                          \n"
        + "   DSI.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.queryForObject(QUERY,
          mapParameterSource, BigDecimal.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
          }, null, QUERY, mapParameterSource);
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
  
  public List<DecodificaInterventoDTO> getListInvestimentiPossibili(long idProcedimentoOggetto)
	      throws InternalUnexpectedException
	  {
	    String THIS_METHOD = "[" + THIS_CLASS + "::getListInvestimentiPossibili]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String QUERY = " WITH                                                                     \n"
	        + "   BANDO_CON_412 AS                                                       \n"
	        + "   (                                                                      \n"
	        + "     SELECT                                                               \n"
	        + "       COUNT(*) AS PRESENZA                                               \n"
	        + "     FROM                                                                 \n"
	        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                     \n"
	        + "       IUF_R_PROCEDIMENTO_LIVELLO PL,                                     \n"
	        + "       IUF_D_LIVELLO L                                                    \n"
	        + "     WHERE                                                                \n"
	        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO              \n"
	        + "       AND PL.ID_PROCEDIMENTO     = PO.ID_PROCEDIMENTO                    \n"
	        + "       AND PL.ID_LIVELLO          = L.ID_LIVELLO                          \n"
	        + "       AND L.CODICE_LIVELLO       = '4.1.2'                               \n"
	        + "   )                                                                      \n"
	        + " SELECT                                                                   \n"
	        + "   DI.ID_DESCRIZIONE_INTERVENTO,                                          \n"
	        + "   DI.DESCRIZIONE AS DESC_INTERVENTO,                                     \n"
	        + "   TA.DESCRIZIONE AS DESC_AGGREGAZIONE_1L                                 \n"
	        + " FROM                                                                     \n"
	        + "   IUF_D_DESCRIZIONE_INTERVENTO DI,                                       \n"
	        + "   IUF_R_AGGREGAZIONE_INTERVENT AI,                                      \n"
	        + "   IUF_D_TIPO_AGGREGAZIONE TA,                                            \n"
	        + "   IUF_D_CATEGORIA_INTERVENTO CI,                                         \n"
	        + "   BANDO_CON_412 B412                                                     \n"
	        + " WHERE                                                                    \n"
	        + "   TRUNC(NVL(DI.DATA_CESSAZIONE,SYSDATE)) <= TRUNC(SYSDATE)               \n"
	        + "   AND DI.ID_DESCRIZIONE_INTERVENTO        = AI.ID_DESCRIZIONE_INTERVENTO \n"
	        + "   AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV   = TA.ID_TIPO_AGGREGAZIONE      \n"
	        + "   AND DI.ID_CATEGORIA_INTERVENTO          = CI.ID_CATEGORIA_INTERVENTO   \n"
	        + "   AND CI.FLAG_ESCLUDI_CATALOGO            = :FLAG_ESCLUDI_CATALOGO       \n"
	        + "   AND                                                                    \n"
	        + "   (                                                                      \n"
	        + "     (                                                                    \n"
	        + "       B412.PRESENZA                        >0                            \n"
	        + "       AND DI.ID_DESC_INTERVENTO_ASSOCIATO IS NULL                        \n"
	        + "     )                                                                    \n"
	        + "     OR B412.PRESENZA = 0                                                 \n"
	        + "   )                                                                      \n"
	        + " ORDER BY                                                                 \n"
	        + "   DI.DESCRIZIONE                                                         \n";
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
	      mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO", IuffiConstants.FLAGS.SI, Types.VARCHAR);
	      return (List<DecodificaInterventoDTO>) namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
	          new RowMapper<DecodificaInterventoDTO>()
	          {
	            @Override
	            public DecodificaInterventoDTO mapRow(ResultSet rs, int arg1) throws SQLException
	            {
	              DecodificaInterventoDTO decodificaInterventoDTO = new DecodificaInterventoDTO();
	              decodificaInterventoDTO.setId(rs.getLong("ID_DESCRIZIONE_INTERVENTO"));
	              decodificaInterventoDTO.setDescrizione(rs.getString("DESC_INTERVENTO"));
	              decodificaInterventoDTO.setDescrizioneAggregazionePrimoLivello(rs.getString("DESC_AGGREGAZIONE_1L"));
	              return decodificaInterventoDTO;
	            }
	          });
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) },
	          new LogVariable[]
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
  
  public void updateFlagAssociatoAltraMisura(long idDettIntervProcOgg, String flagAssociatoAltraMisura)
	      throws InternalUnexpectedException
	  {
	    String THIS_METHOD = "[" + THIS_CLASS + "::updateFlagAssociatoAltraMisura]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String INSERT = " UPDATE                                                                    \n"
	        + "   IUF_W_DETT_INTERV_PROC_OGG DIPO                                         \n"
	        + " SET                                                                       \n"
	        + "   DIPO.FLAG_ASSOCIATO_ALTRA_MISURA = :FLAG_ASSOCIATO_ALTRA_MISURA         \n"
	        + " WHERE                                                                     \n"
	        + "   DIPO.ID_DETT_INTERV_PROC_OGG = :ID_DETT_INTERV_PROC_OGG                 \n"
	        + "   AND EXISTS                                                              \n"
	        + "   (                                                                       \n"
	        + "     SELECT                                                                \n"
	        + "       *                                                                   \n"
	        + "     FROM                                                                  \n"
	        + "       IUF_T_INTERVENTO I,                                                 \n"
	        + "       IUF_D_DESCRIZIONE_INTERVENTO DESCINT                                \n"
	        + "     WHERE                                                                 \n"
	        + "       DIPO.ID_INTERVENTO      = I.ID_INTERVENTO                           \n"
	        + "       AND I.ID_DESCRIZIONE_INTERVENTO = DESCINT.ID_DESCRIZIONE_INTERVENTO \n"
	        + "       AND DESCINT.ID_DESC_INTERVENTO_ASSOCIATO IS NOT NULL                \n"
	        + "   )                                                                       \n";

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG", idDettIntervProcOgg, Types.NUMERIC);
	      mapParameterSource.addValue("FLAG_ASSOCIATO_ALTRA_MISURA", flagAssociatoAltraMisura, Types.VARCHAR);
	      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
	              new LogParameter("flagAssociatoAltraMisura", flagAssociatoAltraMisura)
	          },
	          (LogVariable[]) null, INSERT, mapParameterSource);
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
  
  	public DanniDTO getDannoByIdDannoAtm(long idProcedimentoOggetto, long idDannoAtm) throws InternalUnexpectedException
  	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
		
	    final String QUERY =
				"SELECT																	\r\n" + 
				"    DA.ID_DANNO_ATM,													\r\n" + 
				"    DA.PROGRESSIVO														\r\n" + 
				"FROM																	\r\n" + 
				"    IUF_T_DANNO_ATM DA												\r\n" + 
				"WHERE 																	\r\n" + 
				"    DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 				\r\n" +
				" 	 AND ID_DANNO_ATM = :ID_DANNO_ATM "	
				;
		
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
	      mapParameterSource.addValue("ID_DANNO_ATM", idDannoAtm, Types.NUMERIC);
	      return queryForObject(QUERY, mapParameterSource, DanniDTO.class);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	        		  new LogParameter("ID_PROCEDIMENTO_OGGETTO",idProcedimentoOggetto),
	        		  new LogParameter("ID_DANNO_ATM",idDannoAtm)
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
  	
	public int inserisciDannoAtmIntervento(long idProcedimentoOggetto, long idIntervento, DanniDTO danno) throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String INSERT =  
	    		"INSERT INTO IUF_R_DANNO_ATM_INTERVENTO					\r\n" + 
	    		"    (														\r\n" + 
	    		"        ID_INTERVENTO,										\r\n" + 
	    		"        ID_PROCEDIMENTO_OGGETTO,							\r\n" + 
	    		"        PROGRESSIVO										\r\n" + 
	    		"    )														\r\n" + 
	    		"VALUES 													\r\n" + 
	    		"    (														\r\n" + 
	    		"        :ID_INTERVENTO,									\r\n" + 
	    		"        :ID_PROCEDIMENTO_OGGETTO,							\r\n" + 
	    		"        :PROGRESSIVO										\r\n" + 
	    		"    )"  
				;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
	      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
	      mapParameterSource.addValue("PROGRESSIVO", danno.getProgressivo(), Types.NUMERIC);
	      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_INTERVENTO", idIntervento),
	              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto),
	              new LogParameter("PROGRESSIVO",  danno.getProgressivo())
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
	
	public boolean isInterventoAssociatoADanni(long idProcedimentoOggetto, long idIntervento)
		throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
		
	    final String QUERY =
				"SELECT COUNT(*) AS N												\r\n" + 
				"FROM 																\r\n" + 
				"    IUF_R_DANNO_ATM_INTERVENTO									\r\n" + 
				"WHERE 																\r\n" + 
				"        ID_INTERVENTO = :ID_INTERVENTO								\r\n" + 
				"    AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO				"	
				;
		
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
	      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
	      Long n =  queryForLong(QUERY, mapParameterSource);
	      if(n > 0L)
	      {
	    	  return true;
	      }
	      else
	      {
	    	  return false;
	      }
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	        		  new LogParameter("ID_PROCEDIMENTO_OGGETTO",idProcedimentoOggetto),
	        		  new LogParameter("ID_INTERVENTO",idIntervento)
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
	
	public Boolean isCategoriaInverventoNonInseritePerDannoAtm(
			long idProcedimentoOggetto, 
			Long idDannoAtm, 
			List<Long> arrayIdDescrizioneIntervento) throws InternalUnexpectedException
		{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
		
	    final String QUERY =
				"SELECT 																							\r\n" + 
				"    COUNT(*) AS N_CATEGORIE_INSERIBILI																\r\n" + 
				"FROM 																								\r\n" + 
				"    IUF_D_DESCRIZIONE_INTERVENTO DI,																\r\n" + 
				"    IUF_R_DANNO_INTERVENTO DAIN																	\r\n" + 
				"WHERE 																								\r\n" + 
				"        DI.ID_DESCRIZIONE_INTERVENTO = DAIN.ID_DESCRIZIONE_INTERVENTO								\r\n" + 
				"    AND DAIN.ID_DANNO IN(																			\r\n" + 
				"        SELECT  ID_DANNO																			\r\n" + 
				"        FROM    IUF_T_DANNO_ATM																	\r\n" + 
				"        WHERE   ID_DANNO_ATM = :ID_DANNO_ATM														\r\n" + 
				"        )																							\r\n" + 
				getInCondition("DI.ID_DESCRIZIONE_INTERVENTO", arrayIdDescrizioneIntervento) +
				"    AND DI.ID_DESCRIZIONE_INTERVENTO NOT IN ( 														\r\n" +  //--descrizione interventi inseriti per tale procedimento oggetto e per tale danno
				"        SELECT 																					\r\n" + 
				"            I2.ID_DESCRIZIONE_INTERVENTO															\r\n" + 
				"        FROM    																					\r\n" + 
				"            IUF_R_DANNO_ATM_INTERVENTO DAIN2,													\r\n" + 
				"            IUF_T_INTERVENTO I2,																	\r\n" + 
				"            IUF_T_DANNO_ATM DA2																	\r\n" + 
				"        WHERE																						\r\n" + 
				"                DAIN2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\r\n" + 
				"            AND DAIN2.ID_INTERVENTO = I2.ID_INTERVENTO												\r\n" + 
				"            AND DAIN2.PROGRESSIVO = DA2.PROGRESSIVO												\r\n" + 
				"            AND DA2.ID_PROCEDIMENTO_OGGETTO = DAIN2.ID_PROCEDIMENTO_OGGETTO						\r\n" + 
				"            AND DA2.ID_DANNO_ATM = :ID_DANNO_ATM													\r\n" + 
				"    )"
				;
		
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
	      mapParameterSource.addValue("ID_DANNO_ATM", idDannoAtm, Types.NUMERIC);
	      Long n =  queryForLong(QUERY, mapParameterSource);
	      if(n > 0L && n == arrayIdDescrizioneIntervento.size())
	      {
	    	  return true;
	      }
	      else
	      {
	    	  return false;
	      }
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	        		  new LogParameter("ID_PROCEDIMENTO_OGGETTO",idProcedimentoOggetto),
	        		  new LogParameter("ID_DANNO_ATM",idDannoAtm)
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
	
	
	public ZonaAltimetricaDTO getZonaAltimetricaProcedimento(long idProcedimentoOggetto, int idProcedimentoAgricoltura)
		throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String QUERY = 
	    	"SELECT *															\r\n" + 
	    	"FROM																\r\n" + 
	    	"(																	\r\n" + 
	    	"    SELECT 														\r\n" + 
	    	"        SVU.ID_UTE,												\r\n" + 
	    	"        SVU.ID_ZONA_ALTIMETRICA,									\r\n" + 
	    	"        SVU.DESC_ZONA_ALTIMETRICA,									\r\n" + 
	    	"        B.PERC_RIPARTO_MONTAGNA,									\r\n" + 
	    	"        B.PERC_RIPARTO_PIANURA,									\r\n" + 
	    	"        DECODE(SVU.ID_ZONA_ALTIMETRICA,1,							\r\n" + 
	    	"            B.PERC_RIPARTO_MONTAGNA,								\r\n" + 
	    	"            B.PERC_RIPARTO_PIANURA) AS PERC_CONTR_ZONA_ALTIMETRICA	\r\n" + 
	    	"    FROM 															\r\n" + 
	    	"        SMRGAA_V_UTE SVU,											\r\n" + 
	    	"        IUF_T_PROCEDIMENTO_OGGETTO PO,							\r\n" + 
	    	"        IUF_T_PROCEDIMENTO P,									\r\n" + 
	    	"        SMRGAA_V_DICH_CONSISTENZA DC,								\r\n" + 
	    	"        IUF_D_BANDO B											\r\n" + 
	    	"    WHERE 															\r\n" + 
	    	"        PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO		\r\n" + 
	    	"        AND PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO					\r\n" + 
	    	"        AND P.ID_BANDO = B.ID_BANDO								\r\n" + 
	    	"        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA\r\n" + 
	    	"        AND DC.ID_AZIENDA = SVU.ID_AZIENDA							\r\n" + 
	    	"        AND DC.ID_PROCEDIMENTO = :ID_PROCEDIMENTO_IUFFI			\r\n" + 
	    	"    ORDER BY 														\r\n" + 
	    	"        SVU.ID_UTE													\r\n" + 
	    	")																	\r\n" + 
	    	"WHERE ROWNUM < 2													\r\n" + 
	    	""
	    	;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      
	      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
	      mapParameterSource.addValue("ID_PROCEDIMENTO_IUFFI", idProcedimentoAgricoltura, Types.NUMERIC);
	      return queryForObject(QUERY, mapParameterSource, ZonaAltimetricaDTO.class);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto),
	              new LogParameter("ID_PROCEDIMENTO_IUFFI", idProcedimentoAgricoltura)
	          },
	          new LogVariable[]
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
	
	public void updateWDettIntervProcOgg(long idDettIntervProcOgg, String flagCanale, String flagOpereDiPresa, String flagCondotta) throws InternalUnexpectedException{
	    String THIS_METHOD = "[" + THIS_CLASS
	        + "::updateWDettIntervProcOgg]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String UPDATE =  " UPDATE IUF_W_DETT_INTERV_PROC_OGG                    \n"
				    		 + " SET                                                    \n"
				    		 + "     FLAG_CANALE = :FLAG_CANALE,                        \n"
				    		 + "     FLAG_OPERA_PRESA = :FLAG_OPERA_PRESA,              \n"
				    		 + "     FLAG_CONDOTTA = :FLAG_CONDOTTA                     \n"
				    		 + " WHERE                                                  \n"
				    		 + "     ID_DETT_INTERV_PROC_OGG = :ID_DETT_INTERV_PROC_OGG \n";

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
	          idDettIntervProcOgg, Types.NUMERIC);
	      mapParameterSource.addValue("FLAG_CANALE",
	          flagCanale, Types.VARCHAR);
	      mapParameterSource.addValue("FLAG_OPERA_PRESA",
		      flagOpereDiPresa, Types.VARCHAR);
	      mapParameterSource.addValue("FLAG_CONDOTTA",
		      flagCondotta, Types.VARCHAR);
	      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg)
	          },
	          new LogVariable[]
	          { new LogVariable("idDettIntervProcOgg", idDettIntervProcOgg) }, UPDATE,
	          mapParameterSource);
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
	
	public List<String> getFlagCanaleOpereCondotta(long idDettIntervProcedimentoOggetto) throws InternalUnexpectedException{
		{
		    final String THIS_METHOD = "getFlagCanaleOpereCondotta";
		    if (logger.isDebugEnabled())
		    {
		      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		    }
		    String QUERY = " SELECT                                                 \n"
			    		 + "     FLAG_CANALE,                                       \n"
			    		 + "     FLAG_OPERA_PRESA,                                  \n"
			    		 + "     FLAG_CONDOTTA                                      \n"
			    		 + " FROM                                                   \n"
			    		 + "     IUF_W_DETT_INTERV_PROC_OGG                       \n"
			    		 + " WHERE                                                  \n"
			    		 + "     ID_DETT_INTERV_PROC_OGG = :ID_DETT_INTERV_PROC_OGG \n";
		    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		    mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
		        idDettIntervProcedimentoOggetto, Types.NUMERIC);
		    try
		    {
		      return (List<String>) namedParameterJdbcTemplate
		          .query(QUERY, mapParameterSource,
		              new ResultSetExtractor<List<String>>()
		              {
		                @Override
		                public List<String> extractData(
		                    ResultSet rs)
		                    throws SQLException, DataAccessException
		                {
		                  List<String> list = new ArrayList<String>();
		                  
		                  while (rs.next())
		                  {
		                    list.add(rs.getString("FLAG_CANALE"));
		                    list.add(rs.getString("FLAG_OPERA_PRESA"));
		                    list.add(rs.getString("FLAG_CONDOTTA"));
		                    break;
		                  }
		                  return list;
		                }
		              });
		    }
		    catch (Throwable t)
		    {
		      InternalUnexpectedException e = new InternalUnexpectedException(t,
		          new LogParameter[]
		          {
		              new LogParameter("idDettIntervProcedimentoOggetto", idDettIntervProcedimentoOggetto)
		          },
		          new LogVariable[]
		          {
		          // new LogParameter("idVariabile", idVariabile),
		          }, QUERY, mapParameterSource);
		      logInternalUnexpectedException(e, THIS_METHOD);
		      throw e;
		    }
		    finally
		    {
		      if (logger.isDebugEnabled())
		      {
		        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
		      }
		    }
	}
	}
	
	
	public Long findIdTDettIntervProcOgg(long idProcedimentoOggetto,
		      long idIntervento) throws InternalUnexpectedException
		  {
		    String THIS_METHOD = "[" + THIS_CLASS + "::findWDettIntervProcOgg]";
		    if (logger.isDebugEnabled())
		    {
		      logger.debug(THIS_METHOD + " BEGIN.");
		    }
		    final String QUERY =   " SELECT                                                      \n"
					    		 + "     DIPO.ID_DETTAGLIO_INTERVENTO                            \n"
					    		 + " FROM                                                        \n"
					    		 + "     IUF_T_DETTAGLIO_INTERVENTO DIPO                       \n"
					    		 + " WHERE                                                       \n"
					    		 + "     DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
					    		 + "     AND DIPO.ID_INTERVENTO = :ID_INTERVENTO                 \n";
		    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.INTEGER);
		    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
		        idProcedimentoOggetto, Types.INTEGER);
		    try
		    {
		      return queryForLong(QUERY, mapParameterSource);
		    }
		    catch (Throwable t)
		    {
		      InternalUnexpectedException e = new InternalUnexpectedException(t,
		          new LogParameter[]
		          { new LogParameter("idIntervento", idIntervento), new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, null, QUERY, mapParameterSource);
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
	
	
	public List<String> getTFlagCanaleOpereCondotta(long idDettIntervProcedimentoOggetto) throws InternalUnexpectedException{
		{
		    final String THIS_METHOD = "getFlagCanaleOpereCondotta";
		    if (logger.isDebugEnabled())
		    {
		      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		    }
		    String QUERY = " SELECT                                                 \n"
			    		 + "     FLAG_CANALE,                                       \n"
			    		 + "     FLAG_OPERA_PRESA,                                  \n"
			    		 + "     FLAG_CONDOTTA                                      \n"
			    		 + " FROM                                                   \n"
			    		 + "     IUF_T_DETTAGLIO_INTERVENTO                       \n"
			    		 + " WHERE                                                  \n"
			    		 + "     ID_DETTAGLIO_INTERVENTO = :ID_DETTAGLIO_INTERVENTO \n";
		    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		    mapParameterSource.addValue("ID_DETTAGLIO_INTERVENTO",
		        idDettIntervProcedimentoOggetto, Types.NUMERIC);
		    try
		    {
		      return (List<String>) namedParameterJdbcTemplate
		          .query(QUERY, mapParameterSource,
		              new ResultSetExtractor<List<String>>()
		              {
		                @Override
		                public List<String> extractData(
		                    ResultSet rs)
		                    throws SQLException, DataAccessException
		                {
		                  List<String> list = new ArrayList<String>();
		                  
		                  while (rs.next())
		                  {
		                    list.add(rs.getString("FLAG_CANALE"));
		                    list.add(rs.getString("FLAG_OPERA_PRESA"));
		                    list.add(rs.getString("FLAG_CONDOTTA"));
		                    break;
		                  }
		                  return list;
		                }
		              });
		    }
		    catch (Throwable t)
		    {
		      InternalUnexpectedException e = new InternalUnexpectedException(t,
		          new LogParameter[]
		          {
		              new LogParameter("idDettIntervProcedimentoOggetto", idDettIntervProcedimentoOggetto)
		          },
		          new LogVariable[]
		          {
		          // new LogParameter("idVariabile", idVariabile),
		          }, QUERY, mapParameterSource);
		      logInternalUnexpectedException(e, THIS_METHOD);
		      throw e;
		    }
		    finally
		    {
		      if (logger.isDebugEnabled())
		      {
		        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
		      }
		    }
	}
	}
	
	public List<RigaElencoInterventi> getListInterventiPrevenzioneTemporanei(long idProcedimentoOggetto) throws InternalUnexpectedException
	{
	    final String THIS_METHOD = "getListInterventiPrevenzioneTemporanei";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
	    }
	    String QUERY = 
	    		     " SELECT                                                                 \n"
				   + "     DIPO.ID_DETT_INTERV_PROC_OGG,                                      \n"
				   + "     I.ID_INTERVENTO,                                      			  \n"
				   + "     DI.ID_DESCRIZIONE_INTERVENTO,                                      \n"
				   + "     DI.DESCRIZIONE AS DESC_INTERVENTO,                                 \n"
				   + "     DIPO.ID_DETT_INTERV_PROC_OGG,                                      \n"
				   + "     DIPOM.ID_MISURAZIONE_INTERVENTO,                                   \n"
				   + "     DIPOM.QUANTITA,                                                    \n"
				   + "     DIPO.IMPORTO_UNITARIO,                                             \n"
				   + "     DIPO.IMPORTO_UNITARIO * DIPOM.QUANTITA AS IMPORTO                  \n"
				   + " FROM                                                                   \n"
				   + "     IUF_D_DESCRIZIONE_INTERVENTO DI,                                 \n"
				   + "     IUF_T_INTERVENTO I,                                              \n"
				   + "     IUF_W_DETT_INTERV_PROC_OGG DIPO,                                 \n"
				   + "     IUF_R_DETT_INTE_PROC_OGG_MIS DIPOM,                              \n"
				   + "     IUF_R_MISURAZIONE_INTERVENTO MI,                                 \n"
				   + "     IUF_T_PROCEDIMENTO_OGGETTO PO                                    \n"
				   + " WHERE                                                                  \n"
				   + "     DI.CODICE_IDENTIFICATIVO = :CODICE_IDENTIFICATIVO                  \n"
				   + "     AND DI.ID_DESCRIZIONE_INTERVENTO = I.ID_DESCRIZIONE_INTERVENTO     \n"
				   + "     AND I.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                         \n"
				   + "     AND I.ID_INTERVENTO = DIPO.ID_INTERVENTO                           \n"
				   + "     AND DIPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO      \n"
				   + "     AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
				   + "     AND DIPO.ID_DETT_INTERV_PROC_OGG = DIPOM.ID_DETT_INTERV_PROC_OGG   \n"
				   + "     AND DIPOM.ID_MISURAZIONE_INTERVENTO = MI.ID_MISURAZIONE_INTERVENTO \n"
				   ;

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
	    mapParameterSource.addValue("CODICE_IDENTIFICATIVO", IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE, Types.VARCHAR);
	    try
	    {
	    	return queryForList(QUERY, mapParameterSource, RigaElencoInterventi.class);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto),
	              new LogParameter("CODICE_IDENTIFICATIVO", IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE),
	          },
	          new LogVariable[]
	          {
	          }, QUERY, mapParameterSource);
	      logInternalUnexpectedException(e, THIS_METHOD);
	      throw e;
	    }
	    finally
	    {
	      if (logger.isDebugEnabled())
	      {
	        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
	      }
	    }
	}
	
	public List<RigaElencoInterventi> getListInterventiPrevenzioneConsolidati(long idProcedimentoOggetto) throws InternalUnexpectedException
	{
		final String THIS_METHOD = "getListInterventiPrevenzioneConsolidati";
		if (logger.isDebugEnabled())
		{
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}
		String QUERY = 
				" SELECT                                                               	\r\n" + 
				"     I.ID_INTERVENTO,                                    		        \r\n" + 
				"     DI.ID_DESCRIZIONE_INTERVENTO,                                    	\r\n" + 
				"     DI.DESCRIZIONE AS DESC_INTERVENTO,                               	\r\n" + 
				"     DETTIN.ID_DETTAGLIO_INTERVENTO,                                  	\r\n" + 
				"     DIM.ID_MISURAZIONE_INTERVENTO,                                   	\r\n" + 
				"     DIM.QUANTITA,                                                    	\r\n" + 
				"     DETTIN.IMPORTO_UNITARIO,											\r\n" + 
				"     DETTIN.ID_PROCEDIMENTO_OGGETTO,									\r\n" + 
				"     DETTIN.IMPORTO_UNITARIO * DIM.QUANTITA AS IMPORTO                	\r\n" + 
				" FROM                                                                 	\r\n" + 
				"     IUF_D_DESCRIZIONE_INTERVENTO DI,                               	\r\n" + 
				"     IUF_T_INTERVENTO I,                                            	\r\n" + 
				"     IUF_T_DETTAGLIO_INTERVENTO DETTIN,                             	\r\n" + 
				"     IUF_R_DETT_INTERV_MISURAZION DIM,                              	\r\n" + 
				"     IUF_R_MISURAZIONE_INTERVENTO MI,								\r\n" + 
				"     IUF_T_PROCEDIMENTO_OGGETTO PO									\r\n" + 
				" WHERE                                                                	\r\n" + 
				"     PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO				\r\n" + 
				"     AND PO.ID_PROCEDIMENTO = I.ID_PROCEDIMENTO						\r\n" + 
				"     AND DI.CODICE_IDENTIFICATIVO = :CODICE_IDENTIFICATIVO             \r\n" + 
				"     AND DI.ID_DESCRIZIONE_INTERVENTO = I.ID_DESCRIZIONE_INTERVENTO   	\r\n" + 
				"     AND I.ID_INTERVENTO = DETTIN.ID_INTERVENTO                       \r\n" + 
				"     AND DETTIN.ID_DETTAGLIO_INTERVENTO = DIM.ID_DETTAGLIO_INTERVENTO \r\n" + 
				"     AND DIM.ID_MISURAZIONE_INTERVENTO = MI.ID_MISURAZIONE_INTERVENTO \r\n" + 
				"     AND NVL(PO.DATA_FINE,SYSDATE) >= DETTIN.DATA_INIZIO AND NVL(PO.DATA_FINE,SYSDATE)<= NVL(DETTIN.DATA_FINE,SYSDATE)"
				;
		
		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
		mapParameterSource.addValue("CODICE_IDENTIFICATIVO", IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE, Types.VARCHAR);
		try
		{
			return queryForList(QUERY, mapParameterSource, RigaElencoInterventi.class);
		}
		catch (Throwable t)
		{
			InternalUnexpectedException e = new InternalUnexpectedException(t,
					new LogParameter[]
							{
									new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto),
									new LogParameter("CODICE_IDENTIFICATIVO", IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE),
							},
							new LogVariable[]
									{
									}, QUERY, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		}
		finally
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
			}
		}
	}

	public MisurazioneDescrizioneInterventoDTO getIdMisurazioneInterventoPrevenzione() throws InternalUnexpectedException
	{
		final String THIS_METHOD = "getIdMisurazioneInterventoPrevenzione";
		if (logger.isDebugEnabled())
		{
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}
		String query = 
				  " SELECT                                                          \n"
				+ "     DI.ID_DESCRIZIONE_INTERVENTO,                                 \n"
				+ "     MI.ID_MISURAZIONE_INTERVENTO                                \n"
				+ " FROM                                                            \n"
				+ "     IUF_R_MISURAZIONE_INTERVENTO MI,                          \n"
				+ "     IUF_D_DESCRIZIONE_INTERVENTO DI                           \n"
				+ " WHERE                                                           \n"
				+ "     DI.ID_DESCRIZIONE_INTERVENTO = MI.ID_DESCRIZIONE_INTERVENTO \n"
				+ "     AND DI.CODICE_IDENTIFICATIVO = :CODICE_IDENTIFICATIVO       \n"
				;
		
		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("CODICE_IDENTIFICATIVO", IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE, Types.VARCHAR);
		try
		{
			return queryForObject(query, mapParameterSource,MisurazioneDescrizioneInterventoDTO.class);
		}
		catch (Throwable t)
		{
			InternalUnexpectedException e = new InternalUnexpectedException(t,
				new LogParameter[]
				{
						new LogParameter("CODICE_IDENTIFICATIVO", IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE),
				},
				new LogVariable[]
				{
				}, query, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		}
		finally
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
			}
		}
	}
	
	
	public String getCodiceIdentificativoIntervento(long idIntervento) throws InternalUnexpectedException
	{
		final String THIS_METHOD = "getCodiceIdentificativoIntervento";
		if (logger.isDebugEnabled())
		{
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}
		String query = " SELECT                                                            \n"
					 + "     D.CODICE_IDENTIFICATIVO                                       \n"
					 + " FROM                                                              \n"
					 + "     IUF_D_DESCRIZIONE_INTERVENTO D,                             \n"
					 + "     IUF_T_INTERVENTO I                                          \n"
					 + " WHERE                                                             \n"
					 + "     I.ID_INTERVENTO = :ID_INTERVENTO                              \n"
					 + "     AND I.ID_DESCRIZIONE_INTERVENTO = D.ID_DESCRIZIONE_INTERVENTO \n";
		
		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
		try
		{
			return queryForString(query, mapParameterSource);
		}
		catch (Throwable t)
		{
			InternalUnexpectedException e = new InternalUnexpectedException(t,
				new LogParameter[]
				{
						new LogParameter("ID_INTERVENTO", idIntervento),
				},
				new LogVariable[]
				{
				}, query, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		}
		finally
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
			}
		}
	}
	
	public int updateFlagTipoOperazioneInterventoW(long idIntervento, String flagTipoOperazione) throws InternalUnexpectedException
	{
	    String THIS_METHOD = "[" + THIS_CLASS + "::setFlagTipoOperazioneInterventoW]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String UPDATE = " UPDATE IUF_W_DETT_INTERV_PROC_OGG SET FLAG_TIPO_OPERAZIONE =:FLAG_TIPO_OPERAZIONE WHERE ID_INTERVENTO = :ID_INTERVENTO ";

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      mapParameterSource.addValue("FLAG_TIPO_OPERAZIONE", flagTipoOperazione, Types.VARCHAR);
	      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
	      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("FLAG_TIPO_OPERAZIONE", flagTipoOperazione),
	              new LogParameter("ID_INTERVENTO", idIntervento),
	          },
	          (LogVariable[]) null, UPDATE, mapParameterSource);
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
