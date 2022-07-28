package it.csi.iuffi.iuffiweb.integration;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;

import it.csi.iuffi.iuffiweb.dto.AnnoCensitoDTO;
import it.csi.iuffi.iuffiweb.dto.CensimentoDTO;
import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.DistrettoDTO;
import it.csi.iuffi.iuffiweb.dto.DocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.dto.DocumentiRichiestiDaVisualizzareDTO;
import it.csi.iuffi.iuffiweb.dto.IpotesiPrelievoDTO;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.dto.PianoSelettivoDTO;
import it.csi.iuffi.iuffiweb.dto.PraticaConcessioneDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.dto.ReferenteProgettoDTO;
import it.csi.iuffi.iuffiweb.dto.SezioneDocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.dto.SoggettoDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDettaglioPlvDTO;
import it.csi.iuffi.iuffiweb.dto.allevamenti.ProduzioneCategoriaAnimaleDTO;
import it.csi.iuffi.iuffiweb.dto.assicurazionicolture.AssicurazioniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.UtilizzoReseDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.DannoDTO;
import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.UnitaMisuraDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.AccertamentoDannoDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoDaFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.IstitutoDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.ParticelleFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.RiepilogoDannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.dannicolture.DanniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.datibilancio.DatiBilancioDTO;
import it.csi.iuffi.iuffiweb.dto.fabbricati.FabbricatiDTO;
import it.csi.iuffi.iuffiweb.dto.gestionelog.LogDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.licenzapesca.ImportoLicenzaDTO;

import it.csi.iuffi.iuffiweb.dto.licenzapesca.VersamentoLicenzaDTO;
import it.csi.iuffi.iuffiweb.dto.motoriagricoli.MotoriAgricoliDTO;
import it.csi.iuffi.iuffiweb.dto.prestitiagrari.PrestitiAgrariDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.ContenutoFileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.FiltroRicercaConduzioni;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.DataCensimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.Distretto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.RicercaDistretto;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDTO;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.ControlloColturaDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioParticellareDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioPsrDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColturePlvVegetaleDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureRiepilogoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.ws.regata.DatiAziendaDTO;
import it.csi.iuffi.iuffiweb.util.DumpUtils;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

public class QuadroIuffiDAO extends QuadroDAO
{
  private static final String WITH_TMP_ALL_PROD_ZOO_PROD_VEND = " TMP_ALL_PROD_ZOO_PROD_VEND AS (\r\n"
      +
      "    SELECT 																											\r\n" +
      "        A.ID_CATEGORIA_ANIMALE,\r\n" +
      "        A.DESCRIZIONE_SPECIE_ANIMALE,																				\r\n"
      +
      "        A.DESCRIZIONE_CATEGORIA_ANIMALE,        \r\n" +
      "        A.ISTAT_COMUNE,\r\n" +

      "        PZ.PESO_VIVO_MEDIO,\r\n" +
      "        PZ.GIORNATE_LAVORATIVE_MEDIE * A.QUANTITA AS GIORNATE_LAVORATIVE_TOTALI, \r\n"
      +
      "        PZ.EXT_ID_UTENTE_AGGIORNAMENTO, \r\n" +
      "        PZ.DATA_ULTIMO_AGGIORNAMENTO, \r\n" +

      "        PZ.ID_PRODUZIONE_ZOOTECNICA,\r\n" +
      "        PZ.GIORNATE_LAVORATIVE_MEDIE,\r\n" +
      "        PZ.ID_PRODUZIONE_ZOOTECNICA AS PZ_ID_PRODUZIONE_ZOOTECNICA,																					\r\n"
      +
      "        PV.ID_PRODUZIONE_ZOOTECNICA AS PV_ID_PRODUZIONE_ZOOTECNICA,												\r\n"
      +
      "        DECODE(PV.ID_PRODUZIONE_ZOOTECNICA, NULL,\r\n" +
      "             0, \r\n" +
      "            (SUM((PV.NUMERO_CAPI * PV.QUANTITA_PRODOTTA - NVL(PV.QUANTITA_REIMPIEGATA,0)) * PV.PREZZO))) AS PLV\r\n"
      +
      "    FROM																												\r\n" +
      "        TMP_ALLEVAMENTI A,																								\r\n"
      +
      "        PRODUZIONE_ZOOTECNICA PZ,																						\r\n"
      +
      "        IUF_T_PRODUZIONE_VENDIBILE PV,																				\r\n"
      +
      "        IUF_D_PRODUZIONE P																							\r\n"
      +
      "    WHERE 																												\r\n" +
      "             A.ID_CATEGORIA_ANIMALE = PZ.EXT_ID_CATEGORIA_ANIMALE														\r\n"
      +
      "        AND A.ISTAT_COMUNE = PZ.EXT_ISTAT_COMUNE																		\r\n"
      +
      "        AND PZ.ID_PRODUZIONE_ZOOTECNICA = PV.ID_PRODUZIONE_ZOOTECNICA (+)													\r\n"
      +
      "        AND P.ID_PRODUZIONE (+) = PV.ID_PRODUZIONE																		\r\n"
      +
      "    GROUP BY         																									\r\n"
      +
      "        A.ID_CATEGORIA_ANIMALE,																						\r\n"
      +
      "        A.DESCRIZIONE_SPECIE_ANIMALE,																					\r\n"
      +
      "        A.DESCRIZIONE_CATEGORIA_ANIMALE,        \r\n" +
      "        A.ISTAT_COMUNE,\r\n" +

      "        PZ.PESO_VIVO_MEDIO,\r\n" +
      "        PZ.GIORNATE_LAVORATIVE_MEDIE * A.QUANTITA, \r\n" +
      "        PZ.EXT_ID_UTENTE_AGGIORNAMENTO, \r\n" +
      "        PZ.DATA_ULTIMO_AGGIORNAMENTO, \r\n" +

      "        PZ.ID_PRODUZIONE_ZOOTECNICA,\r\n" +
      "        PZ.GIORNATE_LAVORATIVE_MEDIE,\r\n" +
      "        PZ.ID_PRODUZIONE_ZOOTECNICA,\r\n" +
      "        PV.ID_PRODUZIONE_ZOOTECNICA\r\n" +
      ")\r\n";
  private static final String WITH_TMP_ALLEVAMENTO_CAT_PROD   = " TMP_ALLEVAMENTO_CAT_PROD AS (\r\n"
      +
      "    SELECT 																											\r\n" +
      "        DAMM.DESCRIZIONE_COMUNE || ' (' || DAMM.SIGLA_PROVINCIA || ')' AS UBICAZIONE_ALLEVAMENTO,						\r\n"
      +
      "        DAMM.SIGLA_PROVINCIA,																							\r\n"
      +
      "        DAMM.DESCRIZIONE_COMUNE,																						\r\n"
      +
      "		 A.CODICE_AZIENDA_ZOOTECNICA, 																					\r\n"
      +
      "        A.ID_CATEGORIA_ANIMALE,																						\r\n"
      +
      "        A.ISTAT_COMUNE,																								\r\n"
      +
      "        A.DESCRIZIONE_SPECIE_ANIMALE,																					\r\n"
      +
      "        A.DESCRIZIONE_CATEGORIA_ANIMALE,																				\r\n"
      +
      "        A.QUANTITA,																									\r\n"
      +
      "        A.UNITA_MISURA_SPECIE,																							\r\n"
      +
      "        CA.GIORNATE_LAVORATIVE_MEDIE,																					\r\n"
      +
      "        CA.PESO_VIVO_MEDIO,																							\r\n"
      +
      "        DA.COEFFICIENTE_UBA,																							\r\n"
      +
      "        (DA.CONSUMO_ANNUO_UF * A.QUANTITA ) AS UNITA_FORAGGERE,														\r\n"
      +
      "        P.ID_PRODUZIONE																								\r\n"
      +
      "    FROM																												\r\n" +
      "        TMP_ALLEVAMENTI A,																								\r\n"
      +
      "        SMRGAA_V_DECO_ALLEVAMENTI DA,																					\r\n"
      +
      "        SMRGAA_V_DATI_AMMINISTRATIVI DAMM,																				\r\n"
      +
      "        IUF_D_CATEGORIA_ANIMALE CA,																					\r\n"
      +
      "        IUF_D_PRODUZIONE P																							\r\n"
      +
      "    WHERE 																												\r\n" +
      "            A.ISTAT_COMUNE = DAMM.ISTAT_COMUNE																			\r\n"
      +
      "        AND A.ID_CATEGORIA_ANIMALE = CA.EXT_ID_CATEGORIA_ANIMALE														\r\n"
      +
      "        AND CA.EXT_ID_CATEGORIA_ANIMALE = P.EXT_ID_CATEGORIA_ANIMALE													\r\n"
      +
      "        AND CA.EXT_ID_CATEGORIA_ANIMALE = DA.ID_CATEGORIA_ANIMALE														\r\n"
      +
      "\r\n" +
      ") ";
  private static final String WITH_TMP_ALLEVAMENTI            = " TMP_ALLEVAMENTI AS (																							\r\n"
      +
      "    SELECT 																										\r\n" +
      "        A.CODICE_AZIENDA_ZOOTECNICA,																				\r\n"
      +
      "        A.ID_CATEGORIA_ANIMALE,																					\r\n"
      +
      "        A.ISTAT_COMUNE,																							\r\n"
      +
      "        A.DESCRIZIONE_SPECIE_ANIMALE,																				\r\n"
      +
      "        A.DESCRIZIONE_CATEGORIA_ANIMALE,																			\r\n"
      +
      "        A.UNITA_MISURA_SPECIE,																						\r\n"
      +
      "        SUM(A.QUANTITA) AS QUANTITA																				\r\n"
      +
      "    FROM 																											\r\n" +
      "        IUF_T_PROCEDIMENTO_OGGETTO PO,																			\r\n"
      +
      "        SMRGAA_V_ALLEVAMENTI A																						\r\n"
      +
      "    WHERE																											\r\n" +
      "        PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO														\r\n"
      +
      "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA										\r\n"
      +
      "    GROUP BY																										\r\n" +
      "        A.CODICE_AZIENDA_ZOOTECNICA,																				\r\n"
      +
      "        A.ID_CATEGORIA_ANIMALE,																					\r\n"
      +
      "        A.ISTAT_COMUNE,																							\r\n"
      +
      "        A.DESCRIZIONE_SPECIE_ANIMALE,																				\r\n"
      +
      "        A.DESCRIZIONE_CATEGORIA_ANIMALE,																			\r\n"
      +
      "        A.UNITA_MISURA_SPECIE																						\r\n"
      +
      ") ";

  private static final String WITH_PRODUZIONE_ZOOTECNICA      = "PRODUZIONE_ZOOTECNICA AS(																						\r\n"
      +
      "    SELECT *																										\r\n" +
      "    FROM IUF_T_PRODUZIONE_ZOOTECNICA																				\r\n"
      +
      "    WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO														\r\n"
      +
      ")";
  private static final String THIS_CLASS                      = QuadroIuffiDAO.class
      .getSimpleName();

  public List<ScorteDTO> getListScorteByProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 																\r\n" +
        "        SM.ID_SCORTA_MAGAZZINO,													\r\n" +
        "        S.DESCRIZIONE AS DESCRIZIONE_SCORTA,										\r\n" +
        "        SM.DESCRIZIONE,															\r\n" +
        "        SM.QUANTITA,																\r\n" +
        "        UM.ID_UNITA_MISURA,														\r\n" +
        "        UM.DESCRIZIONE AS DESC_UNITA_MISURA										\r\n" +
        "FROM IUF_T_SCORTA_MAGAZZINO SM,													\r\n" +
        "     IUF_D_SCORTA S,																\r\n" +
        "     IUF_D_UNITA_MISURA UM,														\r\n" +
        "     IUF_T_PROCEDIMENTO_OGGETTO PO												\r\n" +
        "WHERE SM.ID_SCORTA = S.ID_SCORTA													\r\n" +
        "      AND UM.ID_UNITA_MISURA = NVL(S.ID_UNITA_MISURA, SM.ID_UNITA_MISURA)			\r\n"
        +
        "      AND SM.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO					\r\n"
        +
        "      AND SM.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, ScorteDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public List<ScorteDTO> getListScorteNonDanneggiateByProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 																					\r\n"
        +
        "        SM.ID_SCORTA_MAGAZZINO,															\r\n" +
        "        S.DESCRIZIONE AS DESCRIZIONE_SCORTA,												\r\n"
        +
        "        SM.DESCRIZIONE,																	\r\n" +
        "        SM.QUANTITA,																		\r\n" +
        "        UM.ID_UNITA_MISURA,																\r\n" +
        "        UM.DESCRIZIONE AS DESC_UNITA_MISURA												\r\n"
        +
        "FROM IUF_T_SCORTA_MAGAZZINO SM,														\r\n" +
        "     IUF_D_SCORTA S,																	\r\n" +
        "     IUF_D_UNITA_MISURA UM,															\r\n" +
        "     IUF_T_PROCEDIMENTO_OGGETTO PO														\r\n" +
        "WHERE SM.ID_SCORTA = S.ID_SCORTA															\r\n" +
        "      AND UM.ID_UNITA_MISURA = NVL(S.ID_UNITA_MISURA, SM.ID_UNITA_MISURA)				\r\n"
        +
        "      AND SM.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO						\r\n"
        +
        "      AND SM.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\r\n"
        +
        "      AND SM.ID_SCORTA_MAGAZZINO NOT IN (												\r\n"
        +
        "            SELECT DA.EXT_ID_ENTITA_DANNEGGIATA											\r\n"
        +
        "            FROM IUF_T_DANNO_ATM DA													\r\n" +
        "            WHERE DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 					\r\n"
        +
        "            AND DA.EXT_ID_ENTITA_DANNEGGIATA IS NOT NULL					\r\n"
        +
        getInCondition("DA.ID_DANNO",
            getListDanniEquivalenti(IuffiConstants.DANNI.SCORTA))
        + "\r\n" +
        "            )\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, ScorteDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_PROCEDIMENTO_OGGETTO",
              idProcedimentoOggetto) },
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

  public List<ScorteDTO> getListScorteByIds(long[] arrayIdScortaMagazzino,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT  SM.ID_SCORTA_MAGAZZINO,\r\n" +
        "        SM.ID_PROCEDIMENTO_OGGETTO,\r\n" +
        "        SM.ID_SCORTA,\r\n" +
        "        SM.DESCRIZIONE,\r\n" +
        "        S.DESCRIZIONE AS DESCRIZIONE_SCORTA,\r\n" +
        "        SM.QUANTITA,\r\n" +
        "        NVL(S.ID_UNITA_MISURA, SM.ID_UNITA_MISURA) AS ID_UNITA_MISURA,\r\n"
        +
        "        UM.DESCRIZIONE AS DESC_UNITA_MISURA\r\n" +
        "FROM    IUF_T_SCORTA_MAGAZZINO  SM, \r\n" +
        "        IUF_D_SCORTA S,\r\n" +
        "        IUF_D_UNITA_MISURA UM\r\n" +
        "WHERE   SM.ID_SCORTA = S.ID_SCORTA\r\n" +
        "        AND UM.ID_UNITA_MISURA = NVL(S.ID_UNITA_MISURA, SM.ID_UNITA_MISURA)\r\n"
        +
        "        AND SM.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO\r\n"
        +
        getInCondition("SM.ID_SCORTA_MAGAZZINO", arrayIdScortaMagazzino);
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, ScorteDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ARRAY_ID_SCORTA_MAGAZZINO",
                  arrayIdScortaMagazzino),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public ScorteDTO getScortaByIdScortaMagazzino(long idScortaMagazzino)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 																\r\n" +
        "        SM.ID_SCORTA_MAGAZZINO,													\r\n" +
        "        S.DESCRIZIONE AS DESCRIZIONE_SCORTA,										\r\n" +
        "        S.ID_SCORTA,																\r\n" +
        "        SM.DESCRIZIONE,															\r\n" +
        "        SM.QUANTITA,																\r\n" +
        "        NVL(S.ID_UNITA_MISURA,SM.ID_UNITA_MISURA) AS ID_UNITA_MISURA				\r\n"
        +
        "FROM IUF_T_SCORTA_MAGAZZINO SM,													\r\n" +
        "     IUF_D_SCORTA S																\r\n" +
        "WHERE SM.ID_SCORTA = S.ID_SCORTA													\r\n" +
        " 		AND SM.ID_SCORTA_MAGAZZINO = :ID_SCORTA_MAGAZZINO ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_SCORTA_MAGAZZINO", idScortaMagazzino,
          Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource, ScorteDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_SCORTA_MAGAZZINO", idScortaMagazzino)
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

  public long getIdStatoProcedimento(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT ID_STATO_OGGETTO\r\n" +
        "FROM    IUF_T_PROCEDIMENTO_OGGETTO PO,\r\n" +
        "        IUF_T_PROCEDIMENTO P\r\n" +
        "        WHERE   PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO\r\n" +
        "                AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public List<DecodificaDTO<Long>> getElencoTipologieScorte()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT  S.ID_SCORTA AS ID,\r\n" +
        "        S.DESCRIZIONE AS DESCRIZIONE,\r\n" +
        "        S.DESCRIZIONE AS CODICE\r\n" +
        "     \r\n" +
        "FROM     IUF_D_SCORTA S \r\n" +
        "ORDER BY DESCRIZIONE";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public List<DecodificaDTO<Long>> getUnitaMisuraSelezionata(long idScorta)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT  \r\n" +
        "        UM.ID_UNITA_MISURA AS ID,\r\n" +
        "        UM.DESCRIZIONE AS DESCRIZIONE,\r\n" +
        "        UM.DESCRIZIONE AS CODICE\r\n" +
        "FROM   \r\n" +
        "        IUF_D_UNITA_MISURA UM,\r\n" +
        "        IUF_D_SCORTA S\r\n" +
        "WHERE   \r\n" +
        "        UM.ID_UNITA_MISURA = S.ID_UNITA_MISURA\r\n" +
        "        AND S.ID_SCORTA = :ID_SCORTA";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_SCORTA", idScorta);
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_SCORTA", idScorta),
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

  public List<DecodificaDTO<Long>> getListUnitaDiMisura()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT  							\r\n" +
        "        UM.ID_UNITA_MISURA AS ID,				\r\n" +
        "        UM.DESCRIZIONE AS DESCRIZIONE,			\r\n" +
        "        UM.DESCRIZIONE AS CODICE				\r\n" +
        "FROM   										\r\n" +
        "        IUF_D_UNITA_MISURA UM				\r\n" +
        "WHERE   										\r\n" +
        "		 DATA_FINE IS NULL						\r\n" +
        "ORDER BY DESCRIZIONE";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public Long getUnitaMisuraByScorta(long idScorta)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT  						\r\n" +
        "        S.ID_UNITA_MISURA					\r\n" +
        "FROM   									\r\n" +
        "        IUF_D_SCORTA S					\r\n" +
        "WHERE   									\r\n" +
        "        S.ID_SCORTA = :ID_SCORTA";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_SCORTA", idScorta);
      return queryForLongNull(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  public long getIdScorteAltro() throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT ID_SCORTA FROM IUF_D_SCORTA WHERE ID_UNITA_MISURA IS NULL";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  public long inserisciScorte(long idProcedimentoOggetto, long idScorta,
      BigDecimal quantita, Long idUnitaMisura, String descrizione)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_SCORTA_MAGAZZINO 								\r\n"
        +
        "    (ID_SCORTA_MAGAZZINO,															\r\n" +
        "    ID_PROCEDIMENTO_OGGETTO,														\r\n" +
        "    ID_SCORTA,																		\r\n" +
        "    DESCRIZIONE,																	\r\n" +
        "    QUANTITA,																		\r\n" +
        "	 ID_UNITA_MISURA																\r\n)" +
        "VALUES (																			\r\n" +
        "    :SEQ_IUF_T_SCORTA_MAGAZZINO,													\r\n" +
        "    :ID_PROCEDIMENTO_OGGETTO,														\r\n" +
        "    :ID_SCORTA,																	\r\n" +
        "    :DESCRIZIONE,																	\r\n" +
        "    :QUANTITA,																		\r\n" +
        "	 :ID_UNITA_MISURA)";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTScortaMagazzino = 0;
    try
    {
      seqIuffiTScortaMagazzino = getNextSequenceValue(
          "SEQ_IUF_T_SCORTA_MAGAZZINO");
      mapParameterSource.addValue("SEQ_IUF_T_SCORTA_MAGAZZINO",
          seqIuffiTScortaMagazzino, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_SCORTA", idScorta, Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      mapParameterSource.addValue("QUANTITA", quantita, Types.NUMERIC);
      mapParameterSource.addValue("ID_UNITA_MISURA", idUnitaMisura,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_SCORTA_MAGAZZINO", seqIuffiTScortaMagazzino),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_SCORTA", idScorta),
              new LogParameter("DESCRIZIONE", descrizione),
              new LogParameter("QUANTITA", quantita),
              new LogParameter("ID_UNITA_MISURA", idUnitaMisura)
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
    return seqIuffiTScortaMagazzino;
  }

  public long modificaScorta(
      ScorteDTO scorta,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "	 UPDATE IUF_T_SCORTA_MAGAZZINO 									\r\n"
        +
        "    SET 																\r\n" +
        "    ID_SCORTA = :ID_SCORTA,											\r\n" +
        "    DESCRIZIONE = :DESCRIZIONE,										\r\n" +
        "    QUANTITA = :QUANTITA,												\r\n" +
        "    ID_UNITA_MISURA = :ID_UNITA_MISURA									\r\n" +
        "    WHERE  ID_SCORTA_MAGAZZINO = :ID_SCORTA_MAGAZZINO					\r\n" +
        "	 AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTScortaMagazzino = 0;
    try
    {
      mapParameterSource.addValue("ID_SCORTA_MAGAZZINO",
          scorta.getIdScortaMagazzino(), Types.NUMERIC);
      mapParameterSource.addValue("ID_SCORTA", scorta.getIdScorta(),
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", scorta.getDescrizione(),
          Types.VARCHAR);
      mapParameterSource.addValue("QUANTITA", scorta.getQuantita(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_UNITA_MISURA", scorta.getIdUnitaMisura(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_SCORTA_MAGAZZINO",
                  scorta.getIdScortaMagazzino()),
              new LogParameter("ID_SCORTA", scorta.getIdScorta()),
              new LogParameter("DESCRIZIONE", scorta.getDescrizione()),
              new LogParameter("QUANTITA", scorta.getQuantita()),
              new LogParameter("ID_UNITA_MISURA", scorta.getIdUnitaMisura()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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
    return seqIuffiTScortaMagazzino;
  }

  public long eliminaScorteMagazzino(List<Long> listIdScortaMagazzino,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_T_SCORTA_MAGAZZINO WHERE 1=1 " +
        getInCondition("ID_SCORTA_MAGAZZINO", listIdScortaMagazzino) +
        " AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_SCORTA_MAGAZZINO", listIdScortaMagazzino,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_SCORTA_MAGAZZINO", listIdScortaMagazzino),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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
    return listIdScortaMagazzino.size();
  }

  public int eliminaDanniAssociatiAlleScorteMagazzinoModificateORimosse(
      List<Long> listIdScortaMagazzino, long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_T_DANNO_ATM														\r\n"
        +
        "WHERE																				\r\n" +
        "    ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO								\r\n"
        +
        getInCondition("ID_DANNO",
            getListDanniEquivalenti(IuffiConstants.DANNI.SCORTA))
        + "	\r\n" +
        getInCondition("EXT_ID_ENTITA_DANNEGGIATA", listIdScortaMagazzino);
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_DANNO",
                  getListDanniEquivalenti(IuffiConstants.DANNI.SCORTA)),
              new LogParameter("EXT_ID_ENTITA_DANNEGGIATA",
                  listIdScortaMagazzino)
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

  public long eliminaDanni(List<Long> listIdDannoAtm,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_T_DANNO_ATM WHERE 1=1 " +
        getInCondition("ID_DANNO_ATM", listIdDannoAtm) +
        " AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DANNO_ATM", listIdDannoAtm,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DANNO_ATM", listIdDannoAtm),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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
    return listIdDannoAtm.size();
  }

  public long eliminaDanniConduzioniFromTParticellaDanneggiata(
      long idProcedimentoOggetto, List<Long> listIdDannoAtm)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    // Attenzione. Non c'è il filtro sul procedimento oggetto. Deve essere fatto
    // precedentemente
    final String DELETE = "DELETE FROM IUF_R_PARTICELLA_DANNEGGIATA WHERE 1=1 "
        +
        getInCondition("ID_DANNO_ATM", listIdDannoAtm);
    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DANNO_ATM", listIdDannoAtm,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DANNO_ATM", listIdDannoAtm),
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

  public long getNumDanniGiaEsistenti(List<DanniDTO> listDanniDTO,
      long idProcedimentoOggetto, Integer idDanno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    List<Long> listIdExtEntitaDanneggiata = new ArrayList<Long>();
    for (DanniDTO danno : listDanniDTO)
    {
      listIdExtEntitaDanneggiata.add(danno.getExtIdEntitaDanneggiata());
    }
    final String QUERY = " SELECT COUNT(*)																\r\n"
        +
        " FROM IUF_T_DANNO_ATM														\r\n" +
        " WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO						\r\n"
        +
        getInCondition("ID_DANNO",
            QuadroIuffiDAO.getListDanniEquivalenti(idDanno))
        + "\r\n" +
        getInCondition("EXT_ID_ENTITA_DANNEGGIATA", listIdExtEntitaDanneggiata);
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DANNO", idDanno, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_DANNO", idDanno)
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

  /**
   * Verifica se un utente che sta inserendo i danni è veramente autorizzato: -
   * inserisce danni di una scorta da lui posseduta - inserisce danni di un
   * motore agricolo posseduto dalla sua azienda - inserisce danni di un
   * fabbricato di sua proprietà rispetto alla dichiarazione di consistenza
   * 
   * @param listDanniDTO
   * @param idProcedimentoOggetto
   * @param idDanno
   * @return true: se è lecito l'inserimento false: se è illecito l'inserimento
   * @throws InternalUnexpectedException
   */
  public boolean isUtenteAutorizzatoInserimentoDanni(
      List<DanniDTO> listDanniDTO, long idProcedimentoOggetto, Integer idDanno,
      int idProcedimentoAgricoltura) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY;
    List<Long> listIdEntitaDanneggiata = new ArrayList<Long>();
    for (DanniDTO danno : listDanniDTO)
    {
      listIdEntitaDanneggiata.add(danno.getExtIdEntitaDanneggiata());
    }
    switch (idDanno)
    {

      case IuffiConstants.DANNI.SCORTA:
      case IuffiConstants.DANNI.SCORTE_MORTE:

        QUERY = "	WITH TMP_SCORTE_MAGAZZINO_PO AS (\r\n" + // conto le scorte
                                                           // magazzino
                                                           // dell'utente e del
                                                           // rispettivo po
            "		   SELECT 	SM.ID_SCORTA_MAGAZZINO														\r\n"
            +
            "		   FROM 	IUF_T_SCORTA_MAGAZZINO SM														\r\n"
            +
            "		   WHERE	SM.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO\r\n"
            +
            "	)\r\n" +
            "	SELECT COUNT(*)\r\n" + // conto quante delle scorte che voglio
                                     // insierire sono di proprietà del PO
            "	FROM TMP_SCORTE_MAGAZZINO_PO\r\n" +
            "WHERE 1=1 \r\n"
            + getInCondition("ID_SCORTA_MAGAZZINO", listIdEntitaDanneggiata);
        break;
      case IuffiConstants.DANNI.MACCHINA_AGRICOLA:
      case IuffiConstants.DANNI.ATTREZZATURA:
        QUERY = "	WITH TMP_MACCHINE_UTENTE AS (\r\n" +
            "        SELECT M2.ID_MACCHINA																								\r\n"
            +
            "        FROM 																												\r\n"
            +
            "            SMRGAA_V_MACCHINE M2,																							\r\n"
            +
            "            IUF_T_PROCEDIMENTO_OGGETTO PO2,																				\r\n"
            +
            "            SMRGAA_V_DICH_CONSISTENZA DC2																					\r\n"
            +
            "        WHERE																												\r\n"
            +
            "            PO2.EXT_ID_DICHIARAZIONE_CONSISTEN = DC2.ID_DICHIARAZIONE_CONSISTENZA											\r\n"
            +
            "            AND DC2.ID_AZIENDA = M2.ID_AZIENDA																				\r\n"
            +
            "            AND DC2.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN M2.DATA_INIZIO_VALIDITA AND NVL(M2.DATA_FINE_VALIDITA, SYSDATE)	\r\n"
            +
            "            AND DC2.ID_PROCEDIMENTO = :DC_ID_PROCEDIMENTO																	\r\n"
            +
            "            AND PO2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO														\r\n"
            +
            "	)																														\r\n"
            +
            "	SELECT COUNT(*)																											\r\n"
            +
            "	FROM TMP_MACCHINE_UTENTE																								\r\n"
            +
            "	WHERE 	1=1																												\r\n"
            +
            getInCondition("ID_MACCHINA", listIdEntitaDanneggiata);

        break;
      case IuffiConstants.DANNI.FABBRICATO:
        QUERY = " WITH TMP_FABBRICATI_UTENTE AS (\r\n" + // elenco dei
                                                         // fabbricati
                                                         // dell'utente
            "				SELECT  																											\r\n"
            +
            "						F.ID_FABBRICATO																						       																		\r\n"
            +
            "				FROM    SMRGAA_V_FABBRICATI F,																						\r\n"
            +
            "						SMRGAA_V_UTE U,																								\r\n"
            +
            "						SMRGAA_V_DICH_CONSISTENZA DC,																				\r\n"
            +
            "						IUF_T_PROCEDIMENTO_OGGETTO PO																				\r\n"
            +
            "				WHERE   F.ID_UTE = U.ID_UTE																							\r\n"
            +
            "						AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA										\r\n"
            +
            "						AND DC.ID_AZIENDA = F.ID_AZIENDA																			\r\n"
            +
            "						AND DC.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN F.DATA_INIZIO_VAL_FABBR AND NVL(DATA_FINE_VAL_FABBR,SYSDATE)	\r\n"
            +
            "						AND DC.ID_PROCEDIMENTO = :DC_ID_PROCEDIMENTO																	\r\n"
            +
            "						AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO													\r\n"
            +
            ")																																	\r\n"
            +
            "																																	\r\n"
            +
            "	SELECT COUNT(*)																													\r\n"
            + // conto quanti fabbricati che devo modificare non sono
              // dell'utente
            "	FROM TMP_FABBRICATI_UTENTE																										\r\n"
            +
            "	WHERE 1=1																														\r\n"
            +
            getInCondition("ID_FABBRICATO", listIdEntitaDanneggiata);

        break;

      case IuffiConstants.DANNI.ALLEVAMENTO:
        QUERY = "WITH TMP_ALLEVAMENTI AS (																										\r\n"
            +
            "    SELECT 																														\r\n"
            +
            "        A.ID_ALLEVAMENTO																											\r\n"
            +
            "    FROM 																														\r\n"
            +
            "        SMRGAA_V_ALLEVAMENTI A,																									\r\n"
            +
            "        IUF_T_PROCEDIMENTO_OGGETTO PO,																							\r\n"
            +
            "        SMRGAA_V_DICH_CONSISTENZA DC																								\r\n"
            +
            "    WHERE 																														\r\n"
            +
            "            PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO																\r\n"
            +
            "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA													\r\n"
            +
            "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA													\r\n"
            +
            "        AND DC.ID_PROCEDIMENTO = :DC_ID_PROCEDIMENTO																				\r\n"
            +
            "        AND A.ID_ALLEVAMENTO NOT IN 																								\r\n"
            +
            "            (																													\r\n"
            +
            "                SELECT																											\r\n"
            +
            "                    DA.EXT_ID_ENTITA_DANNEGGIATA																					\r\n"
            +
            "                FROM 																											\r\n"
            +
            "                    IUF_T_DANNO_ATM DA																							\r\n"
            +
            "                WHERE 																											\r\n"
            +
            "                        ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO														\r\n"
            +
            getInCondition("DA.ID_DANNO",
                QuadroIuffiDAO
                    .getListDanniEquivalenti(IuffiConstants.DANNI.ALLEVAMENTO))
            +
            "            )																													\r\n"
            +
            ")																																\r\n"
            +
            "SELECT 																															\r\n"
            +
            "    COUNT(*)																														\r\n"
            +
            "FROM																																\r\n"
            +
            "    TMP_ALLEVAMENTI																												\r\n"
            +
            "WHERE 1=1 " +
            getInCondition("ID_ALLEVAMENTO", listIdEntitaDanneggiata);
        break;
      case IuffiConstants.DANNI.ALTRO:
      default:
        return true;
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("DC_ID_PROCEDIMENTO",
          idProcedimentoAgricoltura, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      Long nValide = queryForLong(QUERY, mapParameterSource);

      if (nValide != listIdEntitaDanneggiata.size())
      {
        return false;
      }
      else
      {
        return true;
      }

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO", idProcedimentoAgricoltura),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("EXT_ID_ENTITA_DANNEGGIATA",
                  listIdEntitaDanneggiata)
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

  public static List<Integer> getListDanniEquivalenti(int idDanno)
  {
    List<Integer> listDanniEquivalenti = new ArrayList<Integer>();
    switch (idDanno)
    {
      case IuffiConstants.DANNI.SCORTA:
      case IuffiConstants.DANNI.SCORTE_MORTE:
        listDanniEquivalenti.add(IuffiConstants.DANNI.SCORTA);
        listDanniEquivalenti.add(IuffiConstants.DANNI.SCORTE_MORTE);
        break;
      case IuffiConstants.DANNI.MACCHINA_AGRICOLA:
      case IuffiConstants.DANNI.ATTREZZATURA:
        listDanniEquivalenti.add(IuffiConstants.DANNI.MACCHINA_AGRICOLA);
        listDanniEquivalenti.add(IuffiConstants.DANNI.ATTREZZATURA);
        break;
      case IuffiConstants.DANNI.ALLEVAMENTO:
        listDanniEquivalenti.add(IuffiConstants.DANNI.ALLEVAMENTO);
        break;
      case IuffiConstants.DANNI.FABBRICATO:
        listDanniEquivalenti.add(IuffiConstants.DANNI.FABBRICATO);
        break;
      case IuffiConstants.DANNI.TERRENI_RIPRISTINABILI:
      case IuffiConstants.DANNI.TERRENI_NON_RIPRISTINABILI:
      case IuffiConstants.DANNI.PIANTAGIONI_ARBOREE:
      case IuffiConstants.DANNI.ALTRE_PIANTAGIONI:
        listDanniEquivalenti.add(IuffiConstants.DANNI.TERRENI_RIPRISTINABILI);
        listDanniEquivalenti
            .add(IuffiConstants.DANNI.TERRENI_NON_RIPRISTINABILI);
        listDanniEquivalenti.add(IuffiConstants.DANNI.PIANTAGIONI_ARBOREE);
        listDanniEquivalenti.add(IuffiConstants.DANNI.ALTRE_PIANTAGIONI);
        break;

      default:
        listDanniEquivalenti.add(idDanno);
        break;
    }
    return listDanniEquivalenti;
  }

  public static List<Integer> getListDanniRiconosciuti()
  {
    List<Integer> listDanniEquivalenti = new ArrayList<Integer>();
    listDanniEquivalenti.add(IuffiConstants.DANNI.SCORTA);
    listDanniEquivalenti.add(IuffiConstants.DANNI.SCORTE_MORTE);
    listDanniEquivalenti.add(IuffiConstants.DANNI.MACCHINA_AGRICOLA);
    listDanniEquivalenti.add(IuffiConstants.DANNI.ATTREZZATURA);
    listDanniEquivalenti.add(IuffiConstants.DANNI.ALLEVAMENTO);
    listDanniEquivalenti.add(IuffiConstants.DANNI.FABBRICATO);
    listDanniEquivalenti.add(IuffiConstants.DANNI.TERRENI_RIPRISTINABILI);
    listDanniEquivalenti.add(IuffiConstants.DANNI.TERRENI_NON_RIPRISTINABILI);
    listDanniEquivalenti.add(IuffiConstants.DANNI.PIANTAGIONI_ARBOREE);
    listDanniEquivalenti.add(IuffiConstants.DANNI.ALTRE_PIANTAGIONI);
    listDanniEquivalenti.add(IuffiConstants.DANNI.ALTRO);
    return listDanniEquivalenti;
  }

  public long inserisciDanno(DanniDTO danno, Integer idDanno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_DANNO_ATM (												\r\n"
        +
        "    ID_DANNO_ATM,																			\r\n" +
        "    ID_DANNO,																				\r\n" +
        "    EXT_ID_ENTITA_DANNEGGIATA,																\r\n" +
        "    PROGRESSIVO,																			\r\n" +
        "    ID_PROCEDIMENTO_OGGETTO,																\r\n" +
        "    DESCRIZIONE,																			\r\n" +
        "    QUANTITA,																				\r\n" +
        "    IMPORTO,																				\r\n" +
        "    ID_UNITA_MISURA)																		\r\n" +
        "VALUES (																					\r\n" +
        "    :ID_DANNO_ATM,																			\r\n" +
        "    :ID_DANNO,																				\r\n" +
        "    :EXT_ID_ENTITA_DANNEGGIATA,															\r\n" +
        "    (SELECT NVL(MAX(PROGRESSIVO),0)+1														\r\n"
        +
        "		FROM IUF_T_DANNO_ATM WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO),	\r\n"
        +
        "    :ID_PROCEDIMENTO_OGGETTO,																\r\n" +
        "    :DESCRIZIONE,																			\r\n" +
        "    :QUANTITA,																				\r\n" +
        "    :IMPORTO,																				\r\n" +
        "    :ID_UNITA_MISURA																		\r\n" +
        ")";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTDannoATM = 0;
    try
    {
      seqIuffiTDannoATM = getNextSequenceValue("SEQ_IUF_T_DANNO_ATM");

      mapParameterSource.addValue("ID_DANNO_ATM", seqIuffiTDannoATM,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_DANNO", danno.getIdDanno(),
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_ENTITA_DANNEGGIATA",
          danno.getExtIdEntitaDanneggiata(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          danno.getIdProcedimentoOggetto(), Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", danno.getDescrizione(),
          Types.VARCHAR);
      mapParameterSource.addValue("QUANTITA", danno.getQuantita(),
          Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", danno.getImporto(), Types.NUMERIC);
      mapParameterSource.addValue("ID_UNITA_MISURA", danno.getIdUnitaMisura(),
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return seqIuffiTDannoATM;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DANNO_ATM", seqIuffiTDannoATM),
              new LogParameter("ID_DANNO", danno.getIdDanno()),
              new LogParameter("EXT_ID_ENTITA_DANNEGGIATA",
                  danno.getExtIdEntitaDanneggiata()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  danno.getIdProcedimentoOggetto()),
              new LogParameter("DESCRIZIONE", danno.getDescrizione()),
              new LogParameter("QUANTITA", danno.getQuantita()),
              new LogParameter("IMPORTO", danno.getImporto()),
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

  public int inserisciConduzioneDanneggiata(long idProcedimentoOggetto,
      DanniDTO danno,
      long idUtilizzoDichiarato) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_R_PARTICELLA_DANNEGGIATA 				\r\n"
        +
        "    (ID_DANNO_ATM, EXT_ID_UTILIZZO_DICHIARATO)			\r\n" +
        "VALUES (:ID_DANNO_ATM, :EXT_ID_UTILIZZO_DICHIARATO)			\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DANNO_ATM", danno.getIdDannoAtm(),
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTILIZZO_DICHIARATO",
          idUtilizzoDichiarato, Types.NUMERIC);
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DANNO_ATM", danno.getIdDannoAtm()),
              new LogParameter("EXT_ID_ID_UTILIZZO_DICHIARATO",
                  idUtilizzoDichiarato)
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

  public UnitaMisuraDTO getUnitaMisuraByIdDanno(Integer idDanno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 											"
        + "UM.ID_UNITA_MISURA, 							"
        + "UM.CODICE, 									"
        + "UM.DESCRIZIONE,								"
        + "UM.DATA_FINE, 									"
        + "D.ID_DANNO, 									"
        + "D.DESCRIZIONE AS DESC_DANNO 					"
        + "FROM IUF_D_UNITA_MISURA UM, IUF_D_DANNO D 	"
        + "WHERE UM.ID_UNITA_MISURA = D.ID_UNITA_MISURA 	"
        + "AND D.ID_DANNO = :ID_DANNO 						"
        + "AND DATA_FINE IS NULL 							";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DANNO", idDanno);
      return queryForObject(QUERY, mapParameterSource, UnitaMisuraDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  public DannoDTO getDannoByIdDanno(int idDanno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT											\r\n" +
        "    ID_DANNO,									\r\n" +
        "    DESCRIZIONE,								\r\n" +
        "    ID_UNITA_MISURA,							\r\n" +
        "    NOME_TABELLA,								\r\n" +
        "    CODICE										\r\n" +
        "FROM 											\r\n" +
        "    IUF_D_DANNO								\r\n" +
        "WHERE 											\r\n" +
        "    ID_DANNO = :ID_DANNO						\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DANNO", idDanno);
      return queryForObject(QUERY, mapParameterSource, DannoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  private String queryDanniConduzioni(String inConditionArrayIdDannoAtm)
  {
    return "SELECT																					  							\r\n"
        +
        "     DA.ID_DANNO_ATM,																								\r\n"
        +
        "     DA.ID_DANNO,																									\r\n"
        +
        "     D.DESCRIZIONE AS TIPO_DANNO,																					\r\n"
        +
        "     DA.DESCRIZIONE AS DESCRIZIONE,																				\r\n"
        +
        "     NULL AS EXT_ID_ENTITA_DANNEGGIATA,																			\r\n"
        +
        "     NULL AS ID_ELEMENTO,																							\r\n"
        +
        "     NULL AS DENOMINAZIONE,																						\r\n"
        +
        "     NULL AS DESC_ENTITA_DANNEGGIATA,																				\r\n"
        +
        "     DA.QUANTITA,																									\r\n"
        +
        "     UM.ID_UNITA_MISURA,																							\r\n"
        +
        "     UM.DESCRIZIONE AS DESC_UNITA_MISURA,																			\r\n"
        +
        "     DA.IMPORTO,																							 		\r\n"
        +
        "     DA.PROGRESSIVO,																							 	\r\n"
        +
        "     D.NOME_TABELLA																								\r\n"
        +
        " FROM																								  				\r\n" +
        "	  IUF_T_PROCEDIMENTO_OGGETTO PO,																  				\r\n"
        +
        "     IUF_T_DANNO_ATM DA,																							\r\n"
        +
        "     IUF_D_DANNO D,																								\r\n"
        +
        "     IUF_D_UNITA_MISURA UM																						\r\n"
        +
        " WHERE																								  				\r\n" +
        "        PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO														\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = DA.ID_PROCEDIMENTO_OGGETTO													\r\n"
        +
        "    AND DA.ID_DANNO = D.ID_DANNO																					\r\n"
        +
        "    AND D.ID_UNITA_MISURA = UM.ID_UNITA_MISURA																		\r\n"
        +
        getInCondition("DA.ID_DANNO",
            getListDanniEquivalenti(
                IuffiConstants.DANNI.TERRENI_RIPRISTINABILI))
        + "\r\n" +
        inConditionArrayIdDannoAtm;
  }

  // TODO: FIXME: devon considerare che quando vado in modifica e cancellazione
  // non basta filtrare per EXT_ID_ENTITA_DANNEGGIATA ma anche per tipo danno,
  // altrimenti rischio di cancellare delle cose buone!
  public List<DanniDTO> getListDanniByProcedimentoOggettoAndArrayIdDannoAtm(
      long[] arrayIdDannoAtm, long idProcedimentoOggetto,
      int idProcedimentoAgricoltura) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionArrayIdDannoAtm = " ";
    if (arrayIdDannoAtm != null)
    {
      inConditionArrayIdDannoAtm = getInCondition("DA.ID_DANNO_ATM",
          arrayIdDannoAtm);
    }
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY =
        // scorte
        "SELECT DA.ID_DANNO_ATM,																					\r\n"
            +
            "       DA.ID_DANNO,																							\r\n"
            +
            "       D.DESCRIZIONE AS TIPO_DANNO,																			\r\n"
            +
            "       DA.DESCRIZIONE AS DESCRIZIONE, 																			\r\n"
            +
            "       DA.EXT_ID_ENTITA_DANNEGGIATA,																			\r\n"
            +
            "       S.ID_SCORTA AS ID_ELEMENTO,																				\r\n"
            +
            "       S.DESCRIZIONE AS DENOMINAZIONE, 																		\r\n"
            +
            "       NVL(SM.DESCRIZIONE,'')	AS DESC_ENTITA_DANNEGGIATA,														\r\n"
            +
            "       DA.QUANTITA,																							\r\n"
            +
            "       UM.ID_UNITA_MISURA,																						\r\n"
            +
            "       UM.DESCRIZIONE AS DESC_UNITA_MISURA,																	\r\n"
            +
            "       DA.IMPORTO,																								\r\n"
            +
            "       DA.PROGRESSIVO,																							\r\n"
            +
            "       D.NOME_TABELLA																							\r\n"
            +
            "FROM 	 IUF_T_DANNO_ATM DA,																					\r\n"
            +
            "        IUF_D_DANNO D,																						\r\n"
            +
            "        IUF_T_SCORTA_MAGAZZINO SM,																			\r\n"
            +
            "        IUF_D_SCORTA S,																						\r\n"
            +
            "        IUF_D_UNITA_MISURA UM																				\r\n"
            +
            "WHERE   DA.EXT_ID_ENTITA_DANNEGGIATA = SM.ID_SCORTA_MAGAZZINO													\r\n"
            +
            "        AND D.ID_DANNO = DA.ID_DANNO																			\r\n"
            +
            "        AND SM.ID_SCORTA = S.ID_SCORTA																			\r\n"
            +
            "        AND UM.ID_UNITA_MISURA = NVL(SM.ID_UNITA_MISURA, S.ID_UNITA_MISURA)									\r\n"
            +
            getInCondition("DA.ID_DANNO",
                getListDanniEquivalenti(IuffiConstants.DANNI.SCORTA))
            +
            "        AND DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       										\r\n"
            +
            inConditionArrayIdDannoAtm
            + "																					\r\n" +

            // scorte non censite
            "UNION ALL																					\r\n" +
            "SELECT DA.ID_DANNO_ATM,																					\r\n"
            +
            "       DA.ID_DANNO,																							\r\n"
            +
            "       D.DESCRIZIONE AS TIPO_DANNO,																			\r\n"
            +
            "       DA.DESCRIZIONE AS DESCRIZIONE, 																			\r\n"
            +
            "       DA.EXT_ID_ENTITA_DANNEGGIATA,																			\r\n"
            +
            "       NULL ID_ELEMENTO,																				\r\n"
            +
            "       NULL AS DENOMINAZIONE, 																		\r\n"
            +
            "       NULL	AS DESC_ENTITA_DANNEGGIATA,																\r\n"
            +
            "       DA.QUANTITA,																							\r\n"
            +
            "       NULL AS ID_UNITA_MISURA,																						\r\n"
            +
            "       NULL AS DESC_UNITA_MISURA,																	\r\n"
            +
            "       DA.IMPORTO,																								\r\n"
            +
            "       DA.PROGRESSIVO,																								\r\n"
            +
            "       D.NOME_TABELLA																							\r\n"
            +
            "FROM 	 IUF_T_DANNO_ATM DA,																					\r\n"
            +
            "        IUF_D_DANNO D																						\r\n"
            +
            "WHERE   																										\r\n" +
            "        D.ID_DANNO = DA.ID_DANNO																			\r\n"
            +
            "        AND DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       										\r\n"
            +
            "        AND DA.EXT_ID_ENTITA_DANNEGGIATA IS NULL       										\r\n"
            +
            getInCondition("DA.ID_DANNO",
                getListDanniEquivalenti(IuffiConstants.DANNI.SCORTA))
            +
            inConditionArrayIdDannoAtm +

            // macchine agricole
            " UNION ALL \r\n" +
            "SELECT DA.ID_DANNO_ATM,																						\r\n"
            +
            "	   DA.ID_DANNO,																								\r\n"
            +
            "	   D.DESCRIZIONE AS TIPO_DANNO,																				\r\n"
            +
            "	   DA.DESCRIZIONE AS DESCRIZIONE, 																			\r\n"
            +
            "	   DA.EXT_ID_ENTITA_DANNEGGIATA,																			\r\n"
            +
            "	   M.ID_MACCHINA AS ID_ELEMENTO,																			\r\n"
            +
            "	   M.TIPO_MACCHINA || ' ' || M.DESC_TIPO_MARCA || ' ' || M.DESC_TIPO_CATEGORIA AS DENOMINAZIONE,													\r\n"
            +
            "       M.DESC_TIPO_GENERE_MACCHINA AS DESC_ENTITA_DANNEGGIATA,													\r\n"
            +
            "	   DA.QUANTITA,																								\r\n"
            +
            "	   UM.ID_UNITA_MISURA,																						\r\n"
            +
            "	   UM.DESCRIZIONE AS DESC_UNITA_MISURA,																		\r\n"
            +
            "	   DA.IMPORTO,																								\r\n"
            +
            "	   DA.PROGRESSIVO,																								\r\n"
            +
            "      D.NOME_TABELLA																							\r\n"
            +
            "FROM 	IUF_T_DANNO_ATM DA,																					\r\n"
            +
            "		IUF_D_DANNO D,																						\r\n"
            +
            "		IUF_D_UNITA_MISURA UM,																				\r\n"
            +
            "       SMRGAA_V_MACCHINE M,																					\r\n"
            +
            "       IUF_T_PROCEDIMENTO_OGGETTO PO,																		\r\n"
            +
            "       SMRGAA_V_DICH_CONSISTENZA DC																			\r\n"
            +
            "WHERE   DA.EXT_ID_ENTITA_DANNEGGIATA =  M.ID_MACCHINA															\r\n"
            +
            "		AND D.ID_DANNO = DA.ID_DANNO																			\r\n"
            +
            "		AND UM.ID_UNITA_MISURA = D.ID_UNITA_MISURA																\r\n"
            +
            "		AND DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       										\r\n"
            +
            "		AND DA.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO       										\r\n"
            +
            "		AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA									\r\n"
            +
            "		AND DC.ID_AZIENDA = M.ID_AZIENDA																		\r\n"
            +
            "		AND DC.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN M.DATA_INIZIO_VALIDITA AND NVL(M.DATA_FINE_VALIDITA,SYSDATE)	\r\n"
            +
            "		AND DC.ID_PROCEDIMENTO = :ID_PROCEDIMENTO															\r\n"
            +
            "		AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO												\r\n"
            +
            getInCondition("DA.ID_DANNO",
                getListDanniEquivalenti(IuffiConstants.DANNI.MACCHINA_AGRICOLA))
            + "\r\n" +
            inConditionArrayIdDannoAtm + "\r\n" +

            // macchine agricole non censite
            " UNION ALL \r\n" +
            "SELECT DA.ID_DANNO_ATM,																						\r\n"
            +
            "	   DA.ID_DANNO,																								\r\n"
            +
            "	   D.DESCRIZIONE AS TIPO_DANNO,																				\r\n"
            +
            "	   DA.DESCRIZIONE AS DESCRIZIONE, 																			\r\n"
            +
            "	   DA.EXT_ID_ENTITA_DANNEGGIATA,																			\r\n"
            +
            "	   NULL AS ID_ELEMENTO,																						\r\n"
            +
            "	   NULL AS DENOMINAZIONE,																					\r\n"
            +
            "      NULL AS DESC_ENTITA_DANNEGGIATA,																			\r\n"
            +
            "	   DA.QUANTITA,																								\r\n"
            +
            "	   UM.ID_UNITA_MISURA,																						\r\n"
            +
            "	   UM.DESCRIZIONE AS DESC_UNITA_MISURA,																		\r\n"
            +
            "	   DA.IMPORTO,																								\r\n"
            +
            "	   DA.PROGRESSIVO,																							\r\n"
            +
            "      D.NOME_TABELLA																							\r\n"
            +
            "FROM 	IUF_T_DANNO_ATM DA,																					\r\n"
            +
            "		IUF_D_DANNO D,																						\r\n"
            +
            "		IUF_D_UNITA_MISURA UM,																				\r\n"
            +
            "       IUF_T_PROCEDIMENTO_OGGETTO PO																			\r\n"
            +
            "WHERE  																										\r\n" +
            "		D.ID_DANNO = DA.ID_DANNO																				\r\n"
            +
            "		AND UM.ID_UNITA_MISURA = D.ID_UNITA_MISURA																\r\n"
            +
            "		AND DA.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO       										\r\n"
            +
            "		AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO												\r\n"
            +
            "		AND DA.EXT_ID_ENTITA_DANNEGGIATA IS NULL																\r\n"
            +
            getInCondition("DA.ID_DANNO",
                getListDanniEquivalenti(IuffiConstants.DANNI.MACCHINA_AGRICOLA))
            + "\r\n" +
            inConditionArrayIdDannoAtm + "\r\n" +

            // fabbricati censiti
            "UNION ALL \r\n" +
            "SELECT \r\n" +
            "       DA.ID_DANNO_ATM,																						\r\n"
            +
            "       DA.ID_DANNO,																							\r\n"
            +
            "       D.DESCRIZIONE AS TIPO_DANNO,																			\r\n"
            +
            "       DA.DESCRIZIONE AS DESCRIZIONE, 																			\r\n"
            +
            "       DA.EXT_ID_ENTITA_DANNEGGIATA,																			\r\n"
            +
            "       F.ID_FABBRICATO AS ID_ELEMENTO,																			\r\n"
            +
            "       NVL(F.DENOMINAZIONE,'') AS DENOMINAZIONE, 																\r\n"
            +
            "       F.DESC_TIPOLOGIA_FABBR	AS DESC_ENTITA_DANNEGGIATA,														\r\n"
            +
            "       DA.QUANTITA,																							\r\n"
            +
            "       UM.ID_UNITA_MISURA,																						\r\n"
            +
            "       UM.DESCRIZIONE AS DESC_UNITA_MISURA,																	\r\n"
            +
            "       DA.IMPORTO,																								\r\n"
            +
            "       DA.PROGRESSIVO,																								\r\n"
            +
            "       D.NOME_TABELLA																							\r\n"
            +
            "FROM 	IUF_T_DANNO_ATM DA,																					\r\n"
            +
            "        IUF_D_DANNO D,																						\r\n"
            +
            "        IUF_D_UNITA_MISURA UM,																				\r\n"
            +
            "        SMRGAA_V_FABBRICATI F,																					\r\n"
            +
            "        IUF_T_PROCEDIMENTO_OGGETTO PO,																		\r\n"
            +
            "        SMRGAA_V_DICH_CONSISTENZA DC																			\r\n"
            +
            "WHERE           																								\r\n"
            +
            "        DA.EXT_ID_ENTITA_DANNEGGIATA = F.ID_FABBRICATO															\r\n"
            +
            "        AND D.ID_DANNO = DA.ID_DANNO																			\r\n"
            +
            "        AND UM.ID_UNITA_MISURA = D.ID_UNITA_MISURA																\r\n"
            +
            "        AND DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       										\r\n"
            +
            "		 AND DA.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO											\r\n"
            +
            "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA								\r\n"
            +
            "        AND DC.ID_AZIENDA = F.ID_AZIENDA																		\r\n"
            +
            "        AND DC.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN F.DATA_INIZIO_VAL_FABBR AND NVL(DATA_FINE_VAL_FABBR,SYSDATE)	\r\n"
            +
            "        AND DC.ID_PROCEDIMENTO = :ID_PROCEDIMENTO																	\r\n"
            +
            "        AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 													\r\n"
            +
            getInCondition("DA.ID_DANNO",
                getListDanniEquivalenti(IuffiConstants.DANNI.FABBRICATO))
            + "\r\n" +
            inConditionArrayIdDannoAtm + "\r\n" +

            // fabbricati non censiti
            "UNION ALL 																										\r\n"
            +
            "SELECT 																										\r\n" +
            "       DA.ID_DANNO_ATM,																						\r\n"
            +
            "       DA.ID_DANNO,																							\r\n"
            +
            "       D.DESCRIZIONE AS TIPO_DANNO,																			\r\n"
            +
            "       DA.DESCRIZIONE AS DESCRIZIONE, 																			\r\n"
            +
            "       DA.EXT_ID_ENTITA_DANNEGGIATA,																			\r\n"
            +
            "       NULL AS ID_ELEMENTO,																					\r\n"
            +
            "       NULL AS DENOMINAZIONE, 																					\r\n"
            +
            "       NULL AS DESC_ENTITA_DANNEGGIATA,																		\r\n"
            +
            "       DA.QUANTITA,																							\r\n"
            +
            "       UM.ID_UNITA_MISURA,																						\r\n"
            +
            "       UM.DESCRIZIONE AS DESC_UNITA_MISURA,																	\r\n"
            +
            "       DA.IMPORTO,																								\r\n"
            +
            "       DA.PROGRESSIVO,																							\r\n"
            +
            "       D.NOME_TABELLA																							\r\n"
            +
            "FROM 	IUF_T_DANNO_ATM DA,																					\r\n"
            +
            "        IUF_D_DANNO D,																						\r\n"
            +
            "        IUF_D_UNITA_MISURA UM,																				\r\n"
            +
            "        IUF_T_PROCEDIMENTO_OGGETTO PO																		\r\n"
            +
            "WHERE           																								\r\n"
            +
            "        D.ID_DANNO = DA.ID_DANNO																				\r\n"
            +
            "        AND UM.ID_UNITA_MISURA = D.ID_UNITA_MISURA																\r\n"
            +
            "		 AND DA.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO											\r\n"
            +
            "        AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 												\r\n"
            +
            "        AND DA.EXT_ID_ENTITA_DANNEGGIATA IS NULL 																\r\n"
            +
            getInCondition("DA.ID_DANNO",
                getListDanniEquivalenti(IuffiConstants.DANNI.FABBRICATO))
            + "\r\n" +
            inConditionArrayIdDannoAtm + "\r\n" +

            "UNION ALL \r\n" +

            // conduzioni
            queryDanniConduzioni(inConditionArrayIdDannoAtm) + "\r\n" +

            // allevamenti
            "UNION ALL 																											\r\n"
            +
            "SELECT  																											\r\n"
            +
            "       DA.ID_DANNO_ATM,																						 	\r\n"
            +
            "       DA.ID_DANNO,																							 	\r\n"
            +
            "       D.DESCRIZIONE AS TIPO_DANNO,																			 	\r\n"
            +
            "       DA.DESCRIZIONE AS DESCRIZIONE, 																			 	\r\n"
            +
            "       DA.EXT_ID_ENTITA_DANNEGGIATA,																			 	\r\n"
            +
            "       A.ID_ALLEVAMENTO AS ID_ELEMENTO,																			\r\n"
            +
            "       DECODE(A.DENOMINAZIONE_ALLEVAMENTO, NULL, '', A.DENOMINAZIONE_ALLEVAMENTO || '<br/>' ) || DAM.DESCRIZIONE_COMUNE || ' (' || DAM.SIGLA_PROVINCIA || ')' || '<br/>' || A.INDIRIZZO  AS DENOMINAZIONE, 													 	\r\n"
            +
            "       A.DESCRIZIONE_CATEGORIA_ANIMALE  	AS DESC_ENTITA_DANNEGGIATA,														 \r\n"
            +
            "       DA.QUANTITA,																							 	\r\n"
            +
            "       UM.ID_UNITA_MISURA,																						 	\r\n"
            +
            "       UM.DESCRIZIONE AS DESC_UNITA_MISURA,																	 	\r\n"
            +
            "       DA.IMPORTO,																								 	\r\n"
            +
            "       DA.PROGRESSIVO,																								\r\n"
            +
            "       D.NOME_TABELLA																								\r\n"
            +
            "FROM 																												\r\n"
            +
            "        IUF_T_DANNO_ATM DA,																					 	\r\n"
            +
            "        IUF_D_DANNO D,																						 	\r\n"
            +
            "        IUF_D_UNITA_MISURA UM,																				 	\r\n"
            +
            "        SMRGAA_V_ALLEVAMENTI A,																					\r\n"
            +
            "        SMRGAA_V_DATI_AMMINISTRATIVI DAM,																			\r\n"
            +
            "        IUF_T_PROCEDIMENTO_OGGETTO PO,																		 	\r\n"
            +
            "        SMRGAA_V_DICH_CONSISTENZA DC																			 	\r\n"
            +
            "WHERE  																											\r\n"
            +
            "            PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO													\r\n"
            +
            "		 AND PO.ID_PROCEDIMENTO_OGGETTO = DA.ID_PROCEDIMENTO_OGGETTO												\r\n"
            +
            "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA										\r\n"
            +
            "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA									\r\n"
            +
            "        AND DC.ID_PROCEDIMENTO = :ID_PROCEDIMENTO																\r\n"
            +
            "        AND A.ISTAT_COMUNE = DAM.ISTAT_COMUNE																		\r\n"
            +
            "        AND A.ID_ALLEVAMENTO = DA.EXT_ID_ENTITA_DANNEGGIATA														\r\n"
            +
            "        AND DA.ID_DANNO = D.ID_DANNO																				\r\n"
            +
            "        AND D.ID_UNITA_MISURA = UM.ID_UNITA_MISURA																	\r\n"
            +
            "		 AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 													\r\n"
            +
            getInCondition("DA.ID_DANNO",
                QuadroIuffiDAO
                    .getListDanniEquivalenti(IuffiConstants.DANNI.ALLEVAMENTO))
            + "\r\n" +
            inConditionArrayIdDannoAtm
            + "																				\r\n" +

            // allevamenti non censiti
            "UNION ALL 																											\r\n"
            +
            "SELECT  																											\r\n"
            +
            "       DA.ID_DANNO_ATM,																						 	\r\n"
            +
            "       DA.ID_DANNO,																							 	\r\n"
            +
            "       D.DESCRIZIONE AS TIPO_DANNO,																			 	\r\n"
            +
            "       DA.DESCRIZIONE AS DESCRIZIONE, 																			 	\r\n"
            +
            "       DA.EXT_ID_ENTITA_DANNEGGIATA,																			 	\r\n"
            +
            "       NULL AS ID_ELEMENTO,																						\r\n"
            +
            "       NULL  AS DENOMINAZIONE, 													 								\r\n"
            +
            "       NULL  	AS DESC_ENTITA_DANNEGGIATA,														 					\r\n"
            +
            "       DA.QUANTITA,																							 	\r\n"
            +
            "       UM.ID_UNITA_MISURA,																						 	\r\n"
            +
            "       UM.DESCRIZIONE AS DESC_UNITA_MISURA,																	 	\r\n"
            +
            "       DA.IMPORTO,																								 	\r\n"
            +
            "       DA.PROGRESSIVO,																								\r\n"
            +
            "       D.NOME_TABELLA																								\r\n"
            +
            "FROM 																												\r\n"
            +
            "        IUF_T_DANNO_ATM DA,																					 	\r\n"
            +
            "        IUF_D_DANNO D,																						 	\r\n"
            +
            "        IUF_D_UNITA_MISURA UM,																				 	\r\n"
            +
            "        IUF_T_PROCEDIMENTO_OGGETTO PO																		 	\r\n"
            +
            "WHERE  																											\r\n"
            +
            "            PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO													\r\n"
            +
            "		 AND PO.ID_PROCEDIMENTO_OGGETTO = DA.ID_PROCEDIMENTO_OGGETTO												\r\n"
            +
            "        AND DA.ID_DANNO = D.ID_DANNO																				\r\n"
            +
            "        AND D.ID_UNITA_MISURA = UM.ID_UNITA_MISURA																	\r\n"
            +
            "		 AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 													\r\n"
            +
            "		 AND DA.EXT_ID_ENTITA_DANNEGGIATA IS NULL				 													\r\n"
            +
            getInCondition("DA.ID_DANNO",
                QuadroIuffiDAO
                    .getListDanniEquivalenti(IuffiConstants.DANNI.ALLEVAMENTO))
            + "\r\n" +
            inConditionArrayIdDannoAtm
            + "																				\r\n" +

            // altro
            "UNION ALL																									\r\n" +
            "SELECT DA.ID_DANNO_ATM,																					\r\n"
            +
            "	   DA.ID_DANNO,																							\r\n"
            +
            "	   D.DESCRIZIONE AS TIPO_DANNO,																			\r\n"
            +
            "	   DA.DESCRIZIONE AS DESCRIZIONE, 																		\r\n"
            +
            "	   DA.EXT_ID_ENTITA_DANNEGGIATA,																		\r\n"
            +
            "      NULL AS ID_ELEMENTO,																					\r\n"
            +
            "      NULL AS DENOMINAZIONE,																				\r\n"
            +
            "      NULL AS DESC_ENTITA_DANNEGGIATA,																		\r\n"
            +
            "	   DA.QUANTITA,																							\r\n"
            +
            "	   DA.ID_UNITA_MISURA,																					\r\n"
            +
            "	   UM.DESCRIZIONE AS DESC_UNITA_MISURA,																	\r\n"
            +
            "	   DA.IMPORTO,																							\r\n"
            +
            "	   DA.PROGRESSIVO,																						\r\n"
            +
            "      D.NOME_TABELLA																						\r\n"
            +
            "FROM 																										\r\n" +
            "       IUF_T_DANNO_ATM DA,																				\r\n"
            +
            "       IUF_D_DANNO D,																					\r\n"
            +
            "       IUF_D_UNITA_MISURA UM,																			\r\n"
            +
            "       IUF_T_PROCEDIMENTO_OGGETTO PO																		\r\n"
            +
            "WHERE   																									\r\n" +
            "		D.ID_DANNO = DA.ID_DANNO																			\r\n"
            +
            "		AND UM.ID_UNITA_MISURA = DA.ID_UNITA_MISURA															\r\n"
            +
            "		AND DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       									\r\n"
            +
            "		AND DA.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO       									\r\n"
            +
            "		AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO											\r\n"
            +
            getInCondition("DA.ID_DANNO",
                QuadroIuffiDAO
                    .getListDanniEquivalenti(IuffiConstants.DANNI.ALTRO))
            + "		\r\n" +
            inConditionArrayIdDannoAtm + "\r\n" +

            // tutti i danni non contemplati precedentemente
            "UNION ALL																									\r\n" +
            "SELECT DA.ID_DANNO_ATM,																					\r\n"
            +
            "	   DA.ID_DANNO,																							\r\n"
            +
            "	   D.DESCRIZIONE AS TIPO_DANNO,																			\r\n"
            +
            "	   DA.DESCRIZIONE AS DESCRIZIONE, 																		\r\n"
            +
            "	   DA.EXT_ID_ENTITA_DANNEGGIATA,																		\r\n"
            +
            "      NULL AS ID_ELEMENTO,																					\r\n"
            +
            "      NULL AS DENOMINAZIONE,																				\r\n"
            +
            "      NULL AS DESC_ENTITA_DANNEGGIATA,																		\r\n"
            +
            "	   DA.QUANTITA,																							\r\n"
            +
            "	   NVL(DA.ID_UNITA_MISURA, D.ID_UNITA_MISURA) AS ID_UNITA_MISURA,																					\r\n"
            +
            "	   UM.DESCRIZIONE AS DESC_UNITA_MISURA,																	\r\n"
            +
            "	   DA.IMPORTO,																							\r\n"
            +
            "	   DA.PROGRESSIVO,																						\r\n"
            +
            "      D.NOME_TABELLA																						\r\n"
            +
            "FROM 																										\r\n" +
            "       IUF_T_DANNO_ATM DA,																				\r\n"
            +
            "       IUF_D_DANNO D,																					\r\n"
            +
            "       IUF_D_UNITA_MISURA UM,																			\r\n"
            +
            "       IUF_T_PROCEDIMENTO_OGGETTO PO																		\r\n"
            +
            "WHERE   																									\r\n" +
            "		D.ID_DANNO = DA.ID_DANNO																			\r\n"
            +
            "		AND UM.ID_UNITA_MISURA = NVL(DA.ID_UNITA_MISURA,D.ID_UNITA_MISURA)															\r\n"
            +
            "		AND DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       									\r\n"
            +
            "		AND DA.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO       									\r\n"
            +
            "		AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO											\r\n"
            +
            getNotInCondition("DA.ID_DANNO",
                QuadroIuffiDAO.getListDanniRiconosciuti())
            + "		\r\n" +
            inConditionArrayIdDannoAtm + "\r\n" +

            "ORDER BY PROGRESSIVO																								\r\n"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimentoAgricoltura,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, DanniDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_PROCEDIMENTO", idProcedimentoAgricoltura),
              new LogParameter("ID_DANNO_ATM", arrayIdDannoAtm)
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

  public List<AllevamentiDTO> getListAllevamentiByIdDannoAtm(
      long idProcedimentoOggetto, long[] arrayIdDannoAtm)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionArrayIdDannoAtm = " ";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (arrayIdDannoAtm != null)
    {
      inConditionArrayIdDannoAtm = getInCondition("DAT.ID_DANNO_ATM",
          arrayIdDannoAtm);
    }
    final String QUERY = "SELECT																																	\r\n"
        +
        "    A.ID_ALLEVAMENTO,																													\r\n"
        +
        "    A.DESCRIZIONE_CATEGORIA_ANIMALE,																									\r\n"
        +
        "    A.DESCRIZIONE_SPECIE_ANIMALE,																										\r\n"
        +
        "    A.INDIRIZZO,																														\r\n"
        +
        "    A.DENOMINAZIONE_ALLEVAMENTO,																										\r\n"
        +
        "    A.QUANTITA,																														\r\n"
        +
        "    DA.DESCRIZIONE_COMUNE,																												\r\n"
        +
        "    DA.SIGLA_PROVINCIA																													\r\n"
        +
        "FROM 																																	\r\n"
        +
        "    SMRGAA_V_ALLEVAMENTI A,																											\r\n"
        +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,																									\r\n"
        +
        "    SMRGAA_V_DATI_AMMINISTRATIVI DA,\r\n" +
        "    IUF_T_DANNO_ATM DAT\r\n" +
        "WHERE 																																	\r\n"
        +
        "        PO.ID_PROCEDIMENTO_OGGETTO  =:ID_PROCEDIMENTO_OGGETTO																			\r\n"
        +
        "    AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN =  A.ID_DICHIARAZIONE_CONSISTENZA\r\n"
        +
        "    AND A.ID_ALLEVAMENTO = DAT.EXT_ID_ENTITA_DANNEGGIATA\r\n" +
        "    AND A.ISTAT_COMUNE = DA.ISTAT_COMUNE																\r\n"
        +
        getInCondition("DAT.ID_DANNO",
            QuadroIuffiDAO
                .getListDanniEquivalenti(IuffiConstants.DANNI.ALLEVAMENTO))
        + "\r\n" +
        inConditionArrayIdDannoAtm;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, AllevamentiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_DANNO_ATM", arrayIdDannoAtm)
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

  public List<DanniDTO> getListDanniConduzioni(long idProcedimentoOggetto,
      long[] arrayIdDannoAtm)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionArrayIdDannoAtm = " ";
    if (arrayIdDannoAtm != null)
    {
      inConditionArrayIdDannoAtm = getInCondition("DA.ID_DANNO_ATM",
          arrayIdDannoAtm);
    }
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = queryDanniConduzioni(inConditionArrayIdDannoAtm);

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, DanniDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_DANNO_ATM", arrayIdDannoAtm)
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

  public int modificaDanno(DanniDTO danno, long idProcedimentoOggetto)
      throws InternalUnexpectedException

  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_DANNO_ATM	SET											\r\n"
        +
        "    DESCRIZIONE = 	:DESCRIZIONE,										\r\n" +
        "    QUANTITA = 	:QUANTITA,											\r\n" +
        "    IMPORTO = 		:IMPORTO											\r\n" +
        "WHERE 																	\r\n" +
        "    ID_PROCEDIMENTO_OGGETTO = 	:ID_PROCEDIMENTO_OGGETTO				\r\n" +
        "    AND ID_DANNO_ATM = 		:ID_DANNO_ATM							\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("DESCRIZIONE", danno.getDescrizione(),
          Types.VARCHAR);
      mapParameterSource.addValue("QUANTITA", danno.getQuantita(),
          Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", danno.getImporto(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DANNO_ATM", danno.getIdDannoAtm(),
          Types.NUMERIC);
      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("DESCRIZIONE", danno.getDescrizione()),
              new LogParameter("QUANTITA", danno.getQuantita()),
              new LogParameter("IMPORTO", danno.getImporto()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  danno.getIdProcedimentoOggetto()),
              new LogParameter("ID_DANNO_ATM", danno.getIdDannoAtm()),
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

  public List<ScorteDecodificaDTO> getListDecodicaScorta()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 													\r\n" +
        "    ID_SCORTA,												\r\n" +
        "    DESCRIZIONE,											\r\n" +
        "    ID_UNITA_MISURA										\r\n" +
        "FROM IUF_D_SCORTA";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(QUERY, mapParameterSource, ScorteDecodificaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public List<MotoriAgricoliDTO> getListMotoriAgricoli(
      long idProcedimentoOggetto, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    return getListMotoriAgricoli(idProcedimentoOggetto, null,
        idProcedimentoAgricoltura);
  }

  public List<MotoriAgricoliDTO> getListMotoriAgricoli(
      long idProcedimentoOggetto, long[] arrayIdMacchina,
      int idProcedimentoAgricoltura) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionIdMacchina = " ";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (arrayIdMacchina != null)
    {
      inConditionIdMacchina = getInCondition("ID_MACCHINA", arrayIdMacchina);
    }

    final String QUERY = "SELECT   																									\r\n"
        +
        "    M.ID_MACCHINA,  																										\r\n"
        +
        "    M.DESC_TIPO_GENERE_MACCHINA,  																							\r\n"
        +
        "    M.DESC_TIPO_CATEGORIA,  																								\r\n"
        +
        "    M.DESC_TIPO_MARCA,  																									\r\n"
        +
        "    M.TIPO_MACCHINA,   																									\r\n"
        +
        "    M.POTENZA_KW,  																										\r\n"
        +
        "    M.DATA_CARICO  																										\r\n"
        +
        "FROM 	 																													\r\n"
        +
        "	SMRGAA_V_MACCHINE M,							  																		\r\n"
        +
        " 	IUF_T_PROCEDIMENTO_OGGETTO PO,																						\r\n"
        +
        "    SMRGAA_V_DICH_CONSISTENZA DC																							\r\n"
        +
        "WHERE 																														\r\n"
        +
        "    PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA													\r\n"
        +
        "    AND DC.ID_AZIENDA = M.ID_AZIENDA																						\r\n"
        +
        "    AND DC.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN M.DATA_INIZIO_VALIDITA AND NVL(M.DATA_FINE_VALIDITA, SYSDATE)			\r\n"
        +
        "    AND DC.ID_PROCEDIMENTO = :DC_ID_PROCEDIMENTO																			\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO																\r\n"
        +
        inConditionIdMacchina + " \r\n" +
        "ORDER BY   																												\r\n"
        +
        "    M.DESC_TIPO_GENERE_MACCHINA,  																							\r\n"
        +
        "    M.DESC_TIPO_CATEGORIA, 																								\r\n"
        +
        "    M.DESC_TIPO_MARCA,  																									\r\n"
        +
        "    M.TIPO_MACCHINA ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("DC_ID_PROCEDIMENTO",
          idProcedimentoAgricoltura, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, MotoriAgricoliDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("DC_ID_PROCEDIMENTO", idProcedimentoAgricoltura),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public List<MotoriAgricoliDTO> getListMotoriAgricoliNonDanneggiati(
      long idProcedimentoOggetto, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    return getListMotoriAgricoliNonDanneggiati(null, idProcedimentoOggetto,
        idProcedimentoAgricoltura);
  }

  public List<MotoriAgricoliDTO> getListMotoriAgricoliNonDanneggiati(
      long[] arrayIdMacchina, long idProcedimentoOggetto,
      int idProcedimentoAgricoltura) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String condizioneIdMacchina = " ";
    if (arrayIdMacchina != null)
    {
      condizioneIdMacchina = getInCondition("M.ID_MACCHINA", arrayIdMacchina);
    }

    final String QUERY = "SELECT   																							\r\n"
        +
        "    M.ID_MACCHINA,  																								\r\n"
        +
        "    M.DESC_TIPO_GENERE_MACCHINA,  																					\r\n"
        +
        "    M.DESC_TIPO_CATEGORIA,  																						\r\n"
        +
        "    M.DESC_TIPO_MARCA,  																							\r\n"
        +
        "    M.TIPO_MACCHINA,  																								\r\n"
        +
        "    M.POTENZA_KW,  																								\r\n"
        +
        "    M.DATA_CARICO  																								\r\n"
        +
        "FROM   																											\r\n" +
        "    SMRGAA_V_MACCHINE M,																							\r\n"
        +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,																				\r\n"
        +
        "    SMRGAA_V_DICH_CONSISTENZA DC																					\r\n"
        +
        "WHERE   																											\r\n" +
        "     M.ID_MACCHINA NOT IN 	 																						\r\n"
        +
        "    (  																											\r\n" +
        "        SELECT  EXT_ID_ENTITA_DANNEGGIATA									  										\r\n"
        +
        "        FROM    IUF_T_DANNO_ATM DA										  										\r\n"
        +
        "        WHERE										  										\r\n" +
        "        		 DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO	  											\r\n"
        +
        getInCondition("DA.ID_DANNO",
            getListDanniEquivalenti(IuffiConstants.DANNI.MACCHINA_AGRICOLA))
        + "\r\n" +
        "    )   																											\r\n" +
        "    AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA										\r\n"
        +
        "    AND DC.ID_AZIENDA = M.ID_AZIENDA																				\r\n"
        +
        "    AND DC.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN M.DATA_INIZIO_VALIDITA AND NVL(M.DATA_FINE_VALIDITA, SYSDATE)	\r\n"
        +
        "    AND DC.ID_PROCEDIMENTO = :DC_ID_PROCEDIMENTO																	\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO														\r\n"
        +
        condizioneIdMacchina
        + "																								\r\n " +
        " ORDER BY   																										\r\n" +
        "    M.DESC_TIPO_GENERE_MACCHINA,  																					\r\n"
        +
        "    M.DESC_TIPO_CATEGORIA,  																						\r\n"
        +
        "    M.DESC_TIPO_MARCA,  																							\r\n"
        +
        "    M.TIPO_MACCHINA ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("DC_ID_PROCEDIMENTO",
          idProcedimentoAgricoltura, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, MotoriAgricoliDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("DC_ID_PROCEDIMENTO", idProcedimentoAgricoltura),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_MACCHINA", condizioneIdMacchina)
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

  public List<PrestitiAgrariDTO> getListPrestitiAgrari(
      long idProcedimentoOggetto, long[] arrayIdPrestitiAgrari)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionPrestitiAgrari = " ";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (arrayIdPrestitiAgrari != null)
    {
      inConditionPrestitiAgrari = getInCondition("PA.ID_PRESTITI_AGRARI",
          arrayIdPrestitiAgrari);
    }
    final String QUERY = "SELECT \r\n" +
        "    PA.ID_PRESTITI_AGRARI,\r\n" +
        "    PA.DATA_SCADENZA,\r\n" +
        "    PA.FINALITA_PRESTITO,\r\n" +
        "    PA.IMPORTO,\r\n" +
        "    PA.ISTITUTO_EROGANTE,\r\n" +
        "    PA.ID_PROCEDIMENTO_OGGETTO\r\n" +
        "FROM \r\n" +
        "    IUF_T_PRESTITI_AGRARI PA\r\n" +
        "WHERE \r\n" +
        "    PA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO " +
        inConditionPrestitiAgrari

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, PrestitiAgrariDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_PRESTITI_AGRARI", arrayIdPrestitiAgrari)
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

  public long inserisciPrestitoAgrario(PrestitiAgrariDTO prestito)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "	 INSERT INTO IUF_T_PRESTITI_AGRARI (\r\n" +
        "    ID_PRESTITI_AGRARI,     \r\n" +
        "    DATA_SCADENZA,          \r\n" +
        "    FINALITA_PRESTITO,      \r\n" +
        "    IMPORTO,                \r\n" +
        "    ISTITUTO_EROGANTE,      \r\n" +
        "    ID_PROCEDIMENTO_OGGETTO)\r\n" +
        "VALUES (\r\n" +
        "    :SEQ_IUF_T_PRESTITI_AGRARI,     \r\n" +
        "    :DATA_SCADENZA,          \r\n" +
        "    :FINALITA_PRESTITO,      \r\n" +
        "    :IMPORTO,               \r\n" +
        "    :ISTITUTO_EROGANTE,      \r\n" +
        "    :ID_PROCEDIMENTO_OGGETTO)";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTPrestitiAgrari = 0;
    try
    {
      seqIuffiTPrestitiAgrari = getNextSequenceValue(
          "SEQ_IUF_T_PRESTITI_AGRARI");
      mapParameterSource.addValue("SEQ_IUF_T_PRESTITI_AGRARI",
          seqIuffiTPrestitiAgrari, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          prestito.getIdProcedimentoOggetto(), Types.NUMERIC);
      mapParameterSource.addValue("DATA_SCADENZA", prestito.getDataScadenza(),
          Types.DATE);
      mapParameterSource.addValue("FINALITA_PRESTITO",
          prestito.getFinalitaPrestito(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO", prestito.getImporto(),
          Types.NUMERIC);
      mapParameterSource.addValue("ISTITUTO_EROGANTE",
          prestito.getIstitutoErogante(), Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("SEQ_IUF_T_PRESTITI_AGRARI",
                  seqIuffiTPrestitiAgrari),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  prestito.getIdProcedimentoOggetto()),
              new LogParameter("DATA_SCADENZA", prestito.getDataScadenza()),
              new LogParameter("FINALITA_PRESTITO",
                  prestito.getFinalitaPrestito()),
              new LogParameter("IMPORTO", prestito.getImporto()),
              new LogParameter("ISTITUTO_EROGANTE",
                  prestito.getIstitutoErogante())
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
    return seqIuffiTPrestitiAgrari;
  }

  public int eliminaPrestitiAgrari(List<Long> listIdPrestitiAgrari,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_T_PRESTITI_AGRARI										\r\n"
        +
        "WHERE 																		\r\n" +
        "    ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO						\r\n"
        +
        getInCondition("ID_PRESTITI_AGRARI", listIdPrestitiAgrari);
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PRESTITI_AGRARI", listIdPrestitiAgrari),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public int modificaPrestitiAgrari(long idProcedimentoOggetto,
      PrestitiAgrariDTO prestito) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_PRESTITI_AGRARI SET																		\r\n"
        +
        "    FINALITA_PRESTITO = :FINALITA_PRESTITO,															\r\n"
        +
        "    ISTITUTO_EROGANTE = :ISTITUTO_EROGANTE,															\r\n"
        +
        "    IMPORTO = :IMPORTO,																				\r\n" +
        "    DATA_SCADENZA = :DATA_SCADENZA																		\r\n"
        +
        "WHERE 																									\r\n" +
        "    ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO													\r\n"
        +
        " 	 AND ID_PRESTITI_AGRARI  = :ID_PRESTITI_AGRARI														\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("FINALITA_PRESTITO",
          prestito.getFinalitaPrestito(), Types.VARCHAR);
      mapParameterSource.addValue("ISTITUTO_EROGANTE",
          prestito.getIstitutoErogante(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO", prestito.getImporto(),
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_SCADENZA", prestito.getDataScadenza(),
          Types.DATE);
      mapParameterSource.addValue("ID_PRESTITI_AGRARI",
          prestito.getIdPrestitiAgrari(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("FINALITA_PRESTITO",
                  prestito.getFinalitaPrestito()),
              new LogParameter("ISTITUTO_EROGANTE",
                  prestito.getIstitutoErogante()),
              new LogParameter("IMPORTO", prestito.getImporto()),
              new LogParameter("DATA_SCADENZA", prestito.getDataScadenza()),
              new LogParameter("ID_PRESTITI_AGRARI",
                  prestito.getIdPrestitiAgrari()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public List<FabbricatiDTO> getListFabbricati(long idProcedimentoOggetto,
      int idProcedimentoAgricoltura) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT  F.ID_UTE,																										\r\n"
        +
        "        U.DESCRIZIONE_COMUNE,																							\r\n"
        +
        "        F.ID_FABBRICATO,																							\r\n"
        +
        "        U.SIGLA_PROVINCIA,																								\r\n"
        +
        "        U.INDIRIZZO,																									\r\n"
        +
        "        F.DESC_TIPOLOGIA_FABBR AS TIPO_FABBRICATO,																		\r\n"
        +
        "        F.DESC_FORMA_FABBR AS TIPOLOGIA,																				\r\n"
        +
        "        F.SUPERFICIE,																									\r\n"
        +
        "        F.DIMENSIONE,																									\r\n"
        +
        "        F.UNITA_MISURA_TIPOLOGIA_FABBR        																			\r\n"
        +
        "FROM    SMRGAA_V_FABBRICATI F,																							\r\n"
        +
        "        SMRGAA_V_UTE U,																								\r\n"
        +
        "        SMRGAA_V_DICH_CONSISTENZA DC,																					\r\n"
        +
        "        IUF_T_PROCEDIMENTO_OGGETTO PO																				\r\n"
        +
        "WHERE   F.ID_UTE = U.ID_UTE																							\r\n"
        +
        "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA										\r\n"
        +
        "        AND DC.ID_AZIENDA = F.ID_AZIENDA																				\r\n"
        +
        "        AND DC.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN F.DATA_INIZIO_VAL_FABBR AND NVL(DATA_FINE_VAL_FABBR,SYSDATE)		\r\n"
        +
        "        AND DC.ID_PROCEDIMENTO = :ID_PROCEDIMENTO																		\r\n"
        +
        "        AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO															";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimentoAgricoltura,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, FabbricatiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO", idProcedimentoAgricoltura),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public FabbricatiDTO getFabbricato(long idProcedimentoOggetto,
      long idFabbricato, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT  F.ID_UTE,																									 	\r\n"
        +
        "		 F.ID_FABBRICATO,																								\r\n"
        +
        "        U.DESCRIZIONE_COMUNE,																						 	\r\n"
        +
        "        U.SIGLA_PROVINCIA,																							 	\r\n"
        +
        "        U.INDIRIZZO,																								 	\r\n"
        +
        "        F.DESC_TIPOLOGIA_FABBR AS TIPO_FABBRICATO,																	 	\r\n"
        +
        "        F.DESC_FORMA_FABBR AS TIPOLOGIA,																			 	\r\n"
        +
        "        F.DENOMINAZIONE,																								\r\n"
        +
        "        F.LARGHEZZA,																									\r\n"
        +
        "        F.LUNGHEZZA,																									\r\n"
        +
        "        F.ALTEZZA,																										\r\n"
        +
        "        F.SUPERFICIE,																								 	\r\n"
        +
        "        F.DIMENSIONE,																									\r\n"
        +
        "        F.UNITA_MISURA_TIPOLOGIA_FABBR,																				\r\n"
        +
        "        F.ANNO_COSTRUZIONE,																							\r\n"
        +
        "        F.UTM_X,																										\r\n"
        +
        "        F.UTM_Y,																										\r\n"
        +
        "        F.NOTE,																										\r\n"
        +
        "        F.DATA_INIZIO_VAL_FABBR,																						\r\n"
        +
        "        F.DATA_FINE_VAL_FABBR      																		 			\r\n"
        +
        "FROM    SMRGAA_V_FABBRICATI F,																						 	\r\n"
        +
        "        SMRGAA_V_UTE U,																								\r\n"
        +
        "        SMRGAA_V_DICH_CONSISTENZA DC,																				 	\r\n"
        +
        "        IUF_T_PROCEDIMENTO_OGGETTO PO																				\r\n"
        +
        "WHERE   F.ID_UTE = U.ID_UTE																							\r\n"
        +
        "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA										\r\n"
        +
        "        AND DC.ID_AZIENDA = F.ID_AZIENDA																				\r\n"
        +
        "        AND DC.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN F.DATA_INIZIO_VAL_FABBR AND NVL(DATA_FINE_VAL_FABBR,SYSDATE)	 	\r\n"
        +
        "        AND DC.ID_PROCEDIMENTO = :ID_PROCEDIMENTO																	 	\r\n"
        +
        "        AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO														\r\n"
        +
        "		 AND ID_FABBRICATO = :ID_FABBRICATO																							\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimentoAgricoltura,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_FABBRICATO", idFabbricato, Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource, FabbricatiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO", idProcedimentoAgricoltura),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_UTE", idFabbricato)
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

  public Long getNDanniScorte(long idProcedimentoOggetto,
      long[] arrayIdScortaMagazzino) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT COUNT(*) AS N_DANNI_SCORTE														\r\n"
        +
        "FROM IUF_T_DANNO_ATM																	\r\n" +
        "WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO  								\r\n"
        +
        getInCondition("ID_DANNO",
            getListDanniEquivalenti(IuffiConstants.DANNI.SCORTA))
        + "	\r\n" +
        getInCondition("EXT_ID_ENTITA_DANNEGGIATA", arrayIdScortaMagazzino);

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DANNO",
                  getListDanniEquivalenti(IuffiConstants.DANNI.SCORTA)),
              new LogParameter("EXT_ID_ENTITA_DANNEGGIATA",
                  arrayIdScortaMagazzino)
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

  /*
   * NOTE: non ho considerato la dichiarazione di consistenza della
   * SMRGAA_V_FABBRICATO perché non risulta mai essere valorizzata nella
   * DB_FABBRICATO di anagrafe.
   */
  public List<FabbricatiDTO> getListFabbricatiNonDanneggiati(
      long idProcedimentoOggetto, long[] arrayIdFabbricato,
      int idProcedimentoAgricoltura) throws InternalUnexpectedException
  {

    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionIdFabbricato = "";
    if (arrayIdFabbricato != null)
    {
      inConditionIdFabbricato = getInCondition("F.ID_FABBRICATO",
          arrayIdFabbricato);
    }
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT  F.ID_UTE,																						\r\n"
        + "        U.DESCRIZIONE_COMUNE,																						\r\n"
        + "        F.ID_FABBRICATO,																								\r\n"
        + "        U.SIGLA_PROVINCIA,																							\r\n"
        + "        U.INDIRIZZO,																									\r\n"
        + "        F.DESC_TIPOLOGIA_FABBR AS TIPO_FABBRICATO,																	\r\n"
        + "        F.DESC_FORMA_FABBR AS TIPOLOGIA,																				\r\n"
        + "        F.SUPERFICIE,																								\r\n"
        + "        F.DIMENSIONE,																								\r\n"
        + "        F.UNITA_MISURA_TIPOLOGIA_FABBR        																		\r\n"
        + "FROM    SMRGAA_V_FABBRICATI F,																						\r\n"
        + "        SMRGAA_V_UTE U,																								\r\n"
        + "        SMRGAA_V_DICH_CONSISTENZA DC,																				\r\n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO																				\r\n"
        + "WHERE   F.ID_UTE = U.ID_UTE																							\r\n"
        + "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = DC.ID_DICHIARAZIONE_CONSISTENZA										\r\n"
        + "        AND DC.ID_AZIENDA = F.ID_AZIENDA																				\r\n"
        + "        AND DC.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN F.DATA_INIZIO_VAL_FABBR AND NVL(DATA_FINE_VAL_FABBR,SYSDATE)	\r\n"
        + "        AND DC.ID_PROCEDIMENTO = :ID_PROCEDIMENTO																	\r\n"
        + "        AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO													\r\n"
        + inConditionIdFabbricato + "\r\n" 
        + "        AND F.ID_FABBRICATO NOT IN (																					\r\n"
        + "                    SELECT 																																											\r\n"
        + "                            F2.ID_FABBRICATO																																						\r\n"
        + "                    FROM 	IUF_T_DANNO_ATM DA2,																								\r\n"
        + "                            IUF_D_DANNO D2,																									\r\n"
        + "                            IUF_D_UNITA_MISURA UM2,																							\r\n"
        + "                            SMRGAA_V_FABBRICATI F2,																								\r\n"
        + "                            IUF_T_PROCEDIMENTO_OGGETTO PO2,																					\r\n"
        + "                            SMRGAA_V_DICH_CONSISTENZA DC2																						\r\n"
        + "                    WHERE           																												\r\n"
        + "                            DA2.EXT_ID_ENTITA_DANNEGGIATA = F2.ID_FABBRICATO																		\r\n"
        + "                            AND D2.ID_DANNO = DA2.ID_DANNO																						\r\n"
        + "                            AND UM2.ID_UNITA_MISURA = D2.ID_UNITA_MISURA																			\r\n"
        + "                            AND DA2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       													\r\n"
        + "                            AND PO2.EXT_ID_DICHIARAZIONE_CONSISTEN = DC2.ID_DICHIARAZIONE_CONSISTENZA											\r\n"
        + "                            AND DC2.ID_AZIENDA = F2.ID_AZIENDA																					\r\n"
        + "                            AND DC2.DATA_INSERIMENTO_DICHIARAZIONE BETWEEN F2.DATA_INIZIO_VAL_FABBR AND NVL(F2.DATA_FINE_VAL_FABBR,SYSDATE)		\r\n"
        + "                            AND DC2.ID_PROCEDIMENTO = :ID_PROCEDIMENTO																			\r\n"
        + "                            AND PO2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 															\r\n"
        + "        )";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimentoAgricoltura,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, FabbricatiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO", idProcedimentoAgricoltura),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public SuperficiColtureRiepilogoDTO getSuperficiColtureRiepilogo(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "WITH particelle AS (																				\r\n"
        +
        "    SELECT																							\r\n" +
        "        sc.id_superficie_coltura,																	\r\n"
        +
        "        sp.id_particella,																			\r\n" +
        "        sp.sup_catastale,																			\r\n" +
        "        cu.superficie_utilizzata,																	\r\n"
        +
        "        cu.flag_sau																				\r\n" +
        "    FROM																							\r\n" +
        "        IUF_t_superficie_coltura sc,																\r\n"
        +
        "        IUF_t_procedimento_oggetto po,															\r\n"
        +
        "        smrgaa_v_storico_particella sp,															\r\n"
        +
        "        smrgaa_v_conduzione_utilizzo cu															\r\n"
        +
        "    WHERE																							\r\n" +
        "        sc.id_procedimento_oggetto = po.id_procedimento_oggetto									\r\n"
        +
        "        AND sc.ext_id_utilizzo = cu.id_utilizzo													\r\n"
        +
        "        AND po.ext_id_dichiarazione_consisten = cu.id_dichiarazione_consistenza					\r\n"
        +
        "        AND cu.id_storico_particella = sp.id_storico_particella									\r\n"
        +
        "        AND po.id_procedimento_oggetto =:ID_PROCEDIMENTO_OGGETTO									\r\n"
        +
        "        AND cu.comune = sc.ext_istat_comune									\r\n" +
        "),sau AS (																							\r\n" +
        "    SELECT																							\r\n" +
        "        SUM(DECODE(flag_sau,'S',superficie_utilizzata,0) ) AS sau_s,								\r\n"
        +
        "        SUM(DECODE(flag_sau,'N',superficie_utilizzata,0) ) AS sau_n,								\r\n"
        +
        "        SUM(DECODE(flag_sau,'A',superficie_utilizzata,0) ) AS sau_a								\r\n"
        +
        "    FROM																							\r\n" +
        "        particelle																					\r\n" +
        ") SELECT																							\r\n" +
        "    (																								\r\n" +
        "        SELECT																						\r\n" +
        "            SUM(sup_catastale)																		\r\n"
        +
        "        FROM																						\r\n" +
        "            (																						\r\n" +
        "                SELECT DISTINCT																	\r\n"
        +
        "                    id_particella,																	\r\n"
        +
        "                    sup_catastale																	\r\n"
        +
        "                FROM																				\r\n" +
        "                    particelle																		\r\n"
        +
        "            )																						\r\n" +
        "    ) AS sup_catastale,																			\r\n" +
        "    (																								\r\n" +
        "        SELECT																						\r\n" +
        "            SUM(superficie_utilizzata)																\r\n"
        +
        "        FROM																						\r\n" +
        "            particelle																				\r\n" +
        "    ) AS superficie_utilizzata,																	\r\n"
        +
        "    sau_s,																							\r\n" +
        "    sau_n,																							\r\n" +
        "    sau_a																							\r\n" +
        "  FROM																								\r\n" +
        "    sau ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource,
          SuperficiColtureRiepilogoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public List<SuperficiColtureDettaglioDTO> getListSuperficiColtureDettaglio(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return getListSuperficiColtureDettaglio(idProcedimentoOggetto, null);
  }

  private String getWithRiepilogoSuperficieColtura(
      String inConditionIdSuperficieColtura)
  {
    // TMP_SUPERFICIE_COLTURA
    final String WITH_RIEPILOGO_SUPERFICIE_COLTURA = "WITH TMP_SUPERFICIE_COLTURA AS (    																\r\n"
        +
        "     SELECT 																						\r\n" +
        "        DA.SIGLA_PROVINCIA,						 												\r\n" +
        "		DA.DESCRIZIONE_PROVINCIA AS DESC_PROVINCIA,													\r\n"
        +
        "		DA.DESCRIZIONE_COMUNE AS DESC_COMUNE,														\r\n"
        +
        "		CU.ID_UTILIZZO,																				\r\n" +
        "		CU.COD_TIPO_UTILIZZO,																		\r\n" +
        "		CU.DESC_TIPO_UTILIZZO,																		\r\n" +
        "		CU.COD_TIPO_UTILIZZO_SECONDARIO,															\r\n"
        +
        "		CU.DESC_TIPO_UTILIZZO_SECONDARIO,															\r\n"
        +
        "       CU.SUPERFICIE_UTILIZZATA,																	\r\n"
        +
        "		SC.ID_SUPERFICIE_COLTURA,																	\r\n" +
        "       SC.EXT_ISTAT_COMUNE,																		\r\n" +
        "       SC.COLTURA_SECONDARIA,																 		\r\n"
        +
        "		SC.PRODUZIONE_HA,																			\r\n" +
        "		SC.GIORNATE_LAVORATE,																		\r\n" +
        "		SC.UF_REIMPIEGATE,																			\r\n" +
        "		SC.QLI_REIMPIEGATI,																		 	\r\n" +
        "		SC.PREZZO,																					\r\n" +
        "       SC.NOTE,																					\r\n" +
        "       SC.RECORD_MODIFICATO,																		\r\n" +
        "        (SELECT U.UF_PRODOTTE																		\r\n"
        +
        "			FROM IUF_D_UTILIZZO U																	\r\n" +
        "			WHERE U.EXT_ID_UTILIZZO = SC.EXT_ID_UTILIZZO) AS UF_PRODOTTE,							\r\n"
        +
        "		SC.PRODUZIONE_TOTALE_DANNO, 																		\r\n"
        +
        "		SC.PREZZO_DANNEGGIATO 																				\r\n"
        +
        "	FROM																							\r\n" +
        "		IUF_T_SUPERFICIE_COLTURA SC,																\r\n"
        +
        "		IUF_T_PROCEDIMENTO_OGGETTO PO,															\r\n"
        +
        "		SMRGAA_V_CONDUZIONE_UTILIZZO CU,															\r\n"
        +
        "       SMRGAA_V_DATI_AMMINISTRATIVI DA																\r\n"
        +
        "	WHERE																							\r\n" +
        "		PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO										\r\n"
        +
        "            AND PO.ID_PROCEDIMENTO_OGGETTO = SC.ID_PROCEDIMENTO_OGGETTO						 	\r\n"
        +
        "                AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA			\r\n"
        +
        "                    AND CU.ID_UTILIZZO = SC.EXT_ID_UTILIZZO										\r\n"
        +
        "                        AND CU.COMUNE = SC.EXT_ISTAT_COMUNE										\r\n"
        +
        "                            AND SC.EXT_ISTAT_COMUNE = DA.ISTAT_COMUNE								\r\n"
        +
        inConditionIdSuperficieColtura + " 										\r\n" +
        "), TMP_RIEPILOGO_SC AS (																			\r\n" +
        "SELECT 																							\r\n" +
        "            ID_SUPERFICIE_COLTURA,																	\r\n"
        +
        "            EXT_ISTAT_COMUNE,																		\r\n"
        +
        "            NOTE,																					\r\n" +
        "            RECORD_MODIFICATO,																		\r\n"
        +
        "            DESC_COMUNE || ' (' || SIGLA_PROVINCIA || ')' AS UBICAZIONE_TERRENO,					\r\n"
        +
        "            DESC_PROVINCIA,																		\r\n" +
        "            DESC_COMUNE,																			\r\n" +
        "            ID_UTILIZZO,																			\r\n" +
        "            COD_TIPO_UTILIZZO,																		\r\n"
        +
        "            DESC_TIPO_UTILIZZO,																	\r\n"
        +
        "            COD_TIPO_UTILIZZO_SECONDARIO,															\r\n"
        +
        "            DESC_TIPO_UTILIZZO_SECONDARIO,															\r\n"
        +
        "            DECODE(COLTURA_SECONDARIA,'S',															\r\n"
        +
        "                NVL(DESC_TIPO_UTILIZZO_SECONDARIO,DESC_TIPO_UTILIZZO) || ' [' || NVL(COD_TIPO_UTILIZZO_SECONDARIO,COD_TIPO_UTILIZZO) || '] ' || '(secondario)',	  \r\n"
        +
        "                    DESC_TIPO_UTILIZZO || ' [' || COD_TIPO_UTILIZZO || ']'							\r\n"
        +
        "                ) AS TIPO_UTILIZZO_DESCRIZIONE,													\r\n"
        +
        "            COLTURA_SECONDARIA,																	\r\n"
        +
        "            SUM(SUPERFICIE_UTILIZZATA) AS SUPERFICIE_UTILIZZATA,									\r\n"
        +
        "            PRODUZIONE_HA,																			\r\n" +
        "            ROUND(PRODUZIONE_HA * SUM(SUPERFICIE_UTILIZZATA),2) AS PRODUZIONE_DICHIARATA,			\r\n"
        +
        "            ROUND(GIORNATE_LAVORATE * SUM(SUPERFICIE_UTILIZZATA),2) AS GIORNATE_LAVORATIVE_DICH,	\r\n"
        +
        "            ROUND((UF_PRODOTTE * PRODUZIONE_HA * SUM(SUPERFICIE_UTILIZZATA)),2) AS UF_TOTALI,		\r\n"
        +
        "            QLI_REIMPIEGATI,																		\r\n" +
        "            UF_REIMPIEGATE,																		\r\n" +
        "            DECODE(																				\r\n" +
        "                NVL(UF_REIMPIEGATE,0), 0,															\r\n"
        +
        "                ROUND((PRODUZIONE_HA * SUM(SUPERFICIE_UTILIZZATA) - NVL(QLI_REIMPIEGATI,0)),2),	\r\n"
        +
        "                ROUND((PRODUZIONE_HA * SUM(SUPERFICIE_UTILIZZATA) - (NVL(UF_REIMPIEGATE,0) / UF_PRODOTTE )),2)    	  \r\n"
        +
        "                )AS PLV_TOT_QUINTALI,																\r\n"
        +
        "            PREZZO,																				\r\n" +
        "            ROUND(PREZZO * 																		\r\n" +
        "                DECODE(																			\r\n" +
        "                 NVL(UF_REIMPIEGATE,0), 0,															\r\n"
        +
        "                ROUND((PRODUZIONE_HA * SUM(SUPERFICIE_UTILIZZATA) - NVL(QLI_REIMPIEGATI,0)),2),	\r\n"
        +
        "                ROUND((PRODUZIONE_HA * SUM(SUPERFICIE_UTILIZZATA) - (NVL(UF_REIMPIEGATE,0) / UF_PRODOTTE )),2) \r\n"
        +
        "                ),2) AS PLV_TOT_DICH,																\r\n"
        +
        "            UF_PRODOTTE, 																			\r\n" +
        "            PRODUZIONE_TOTALE_DANNO, 																			\r\n"
        +
        "            PREZZO_DANNEGGIATO 																				\r\n"
        +
        "FROM 																								\r\n" +
        "    TMP_SUPERFICIE_COLTURA TSC																		\r\n"
        +
        "GROUP BY 																							\r\n" +
        "    ID_SUPERFICIE_COLTURA, 																		\r\n" +
        "    PRODUZIONE_TOTALE_DANNO, 																			\r\n"
        +
        "    PREZZO_DANNEGGIATO, 																				\r\n" +
        "    EXT_ISTAT_COMUNE, 																				\r\n" +
        "    SIGLA_PROVINCIA, 																				\r\n" +
        "    DESC_PROVINCIA, 																				\r\n" +
        "    DESC_COMUNE, 																					\r\n" +
        "    ID_UTILIZZO, 																					\r\n" +
        "    COD_TIPO_UTILIZZO,	 																			\r\n" +
        "    DESC_TIPO_UTILIZZO, 																			\r\n" +
        "    COD_TIPO_UTILIZZO_SECONDARIO, 																	\r\n"
        +
        "    DESC_TIPO_UTILIZZO_SECONDARIO, 																\r\n"
        +
        "    COLTURA_SECONDARIA,																			\r\n" +
        "    PRODUZIONE_HA, 																				\r\n" +
        "    GIORNATE_LAVORATE, 																			\r\n" +
        "    UF_REIMPIEGATE, 																				\r\n" +
        "    QLI_REIMPIEGATI, 																				\r\n" +
        "    PREZZO, 																						\r\n" +
        "    NOTE, 																							\r\n" +
        "    RECORD_MODIFICATO, 																			\r\n" +
        "    UF_PRODOTTE, 																					\r\n" +
        "    DESC_COMUNE || ' (' || SIGLA_PROVINCIA || ')', 												\r\n"
        +
        "    DESC_PROVINCIA, 																				\r\n" +
        "    DESC_COMUNE, 																					\r\n" +
        "    DECODE(COLTURA_SECONDARIA,'S', 																\r\n"
        +
        "        NVL(DESC_TIPO_UTILIZZO_SECONDARIO,DESC_TIPO_UTILIZZO) || ' [' || NVL(COD_TIPO_UTILIZZO_SECONDARIO,COD_TIPO_UTILIZZO) || '] ' || '(secondario)', DESC_TIPO_UTILIZZO || ' [' || COD_TIPO_UTILIZZO || ']' )\r\n"
        +
        " ORDER BY	 																						\r\n" +
        " 	SIGLA_PROVINCIA, 																				\r\n" +
        " 	DESC_COMUNE, 																					\r\n" +
        " 	TIPO_UTILIZZO_DESCRIZIONE 																		\r\n"
        +
        " ) 																								\r\n";
    return WITH_RIEPILOGO_SUPERFICIE_COLTURA;
  }

  public List<SuperficiColtureDettaglioDTO> getListSuperficiColtureDettaglio(
      long idProcedimentoOggetto, List<Long> idSuperficieColtura)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionIdSuperficieColtura = " ";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    if (idSuperficieColtura != null
        && !idSuperficieColtura.toString().equals(""))
    {
      inConditionIdSuperficieColtura = getInCondition("ID_SUPERFICIE_COLTURA",
          idSuperficieColtura);
    }

    final String QUERY = getWithRiepilogoSuperficieColtura(
        inConditionIdSuperficieColtura) +
        "SELECT * FROM TMP_RIEPILOGO_SC";
    ;

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          SuperficiColtureDettaglioDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public List<ControlloColturaDTO> getListControlloColtura(
      long idProcedimentoOggetto, long[] arrayIdSuperficieColtura)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String inConditionIdSuperficieColtura = " ";
    if (arrayIdSuperficieColtura != null)
    {
      inConditionIdSuperficieColtura = getInCondition(
          "CC.ID_SUPERFICIE_COLTURA", arrayIdSuperficieColtura);
    }

    final String QUERY = "SELECT																								\r\n"
        +
        "    cc.id_superficie_coltura,																		\r\n"
        +
        "    cc.id_controllo_coltura,																		\r\n" +
        "    cc.bloccante,																					\r\n" +
        "    cc.descrizione_anomalia																		\r\n" +
        "FROM																								\r\n" +
        "    IUF_t_superficie_coltura sc,																	\r\n"
        +
        "    IUF_t_procedimento_oggetto po,																\r\n"
        +
        "    smrgaa_v_storico_particella sp,																\r\n"
        +
        "    smrgaa_v_conduzione_utilizzo cu,																\r\n"
        +
        "    IUF_t_controllo_coltura cc																	\r\n"
        +
        "WHERE																								\r\n" +
        "    po.id_procedimento_oggetto = sc.id_procedimento_oggetto										\r\n"
        +
        "    AND po.ext_id_dichiarazione_consisten = cu.id_dichiarazione_consistenza						\r\n"
        +
        "        AND sc.ext_id_utilizzo = cu.id_utilizzo													\r\n"
        +
        "            AND cu.id_storico_particella = sp.id_storico_particella								\r\n"
        +
        "                AND po.id_procedimento_oggetto =:ID_PROCEDIMENTO_OGGETTO							\r\n"
        +
        "                	AND sc.id_superficie_coltura = cc.id_superficie_coltura \r\n"
        +
        inConditionIdSuperficieColtura +
        "GROUP BY cc.id_superficie_coltura, cc.id_controllo_coltura, cc.bloccante, cc.descrizione_anomalia	\r\n"
        +
        "ORDER BY																							\r\n" +
        "    cc.id_superficie_coltura, cc.id_controllo_coltura"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, ControlloColturaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public SuperficiColtureDettaglioDTO getSuperficiColtureDettaglio(
      long idProcedimentoOggetto,
      long idSuperficieColtura) throws InternalUnexpectedException
  {
    List<Long> listIdSuperficieColtura = new ArrayList<Long>();
    listIdSuperficieColtura.add(idSuperficieColtura);
    List<SuperficiColtureDettaglioDTO> list = this
        .getListSuperficiColtureDettaglio(idProcedimentoOggetto,
            listIdSuperficieColtura);
    if (list == null)
    {
      return null;
    }
    else
    {
      return list.get(0);
    }
  }

  public SuperficiColtureDettaglioPsrDTO getSuperficiColtureDettaglioPsrDTO(
      long idProcedimentoOggetto, long idSuperficieColtura)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " WITH TMP_PARTICELLE AS (																										\r\n"
        +
        "SELECT																															\r\n"
        +
        "    SC.ID_SUPERFICIE_COLTURA,																									\r\n"
        +
        "    SC.PRODUZIONE_HA,																											\r\n"
        +
        "    (																															\r\n"
        +
        "        SELECT																													\r\n"
        +
        "            PH.PRODUZIONE_HA_MEDIA																								\r\n"
        +
        "        FROM																													\r\n"
        +
        "            IUF_D_PRODUZIONE_HA PH																							\r\n"
        +
        "        WHERE																													\r\n"
        +
        "            PH.EXT_ID_ZONA_ALTIMETRICA = SP.ID_ZONA_ALTIMETRICA_COMUNE															\r\n"
        +
        "            AND PH.EXT_ID_UTILIZZO = CU.ID_UTILIZZO																			\r\n"
        +
        "    ) AS PRODUZIONE_HA_MEDIA,        																							\r\n"
        +

        "    (\r\n" +
        "        SELECT\r\n" +
        "            PH.PRODUZIONE_HA_MIN\r\n" +
        "        FROM\r\n" +
        "            IUF_D_PRODUZIONE_HA PH\r\n" +
        "        WHERE\r\n" +
        "            PH.EXT_ID_ZONA_ALTIMETRICA = SP.ID_ZONA_ALTIMETRICA_COMUNE\r\n"
        +
        "            AND PH.EXT_ID_UTILIZZO = CU.ID_UTILIZZO\r\n" +
        "    ) AS PRODUZIONE_HA_MIN,\r\n" +
        "    (\r\n" +
        "        SELECT\r\n" +
        "            PH.PRODUZIONE_HA_MAX\r\n" +
        "        FROM\r\n" +
        "            IUF_D_PRODUZIONE_HA PH\r\n" +
        "        WHERE\r\n" +
        "            PH.EXT_ID_ZONA_ALTIMETRICA = SP.ID_ZONA_ALTIMETRICA_COMUNE\r\n"
        +
        "            AND PH.EXT_ID_UTILIZZO = CU.ID_UTILIZZO\r\n" +
        "    ) AS PRODUZIONE_HA_MAX, \r\n" +

        "    SC.GIORNATE_LAVORATE,																										\r\n"
        +
        "    (																															\r\n"
        +
        "        SELECT																													\r\n"
        +
        "            GL.GIORNATE_LAVORATE_MEDIE																							\r\n"
        +
        "        FROM																													\r\n"
        +
        "            IUF_D_GG_LAVORATE GL																								\r\n"
        +
        "        WHERE																													\r\n"
        +
        "            GL.EXT_ID_ZONA_ALTIMETRICA = SP.ID_ZONA_ALTIMETRICA_COMUNE															\r\n"
        +
        "            AND GL.EXT_ID_UTILIZZO = CU.ID_UTILIZZO																			\r\n"
        +
        "    ) AS GIORNATE_LAVORATE_MEDIE,																								\r\n"
        +

        "	(																															\r\n" +
        "        SELECT																													\r\n"
        +
        "            GL.GIORNATE_LAVORATE_MIN																							\r\n"
        +
        "        FROM																													\r\n"
        +
        "            IUF_D_GG_LAVORATE GL																								\r\n"
        +
        "        WHERE																													\r\n"
        +
        "            GL.EXT_ID_ZONA_ALTIMETRICA = SP.ID_ZONA_ALTIMETRICA_COMUNE															\r\n"
        +
        "            AND GL.EXT_ID_UTILIZZO = CU.ID_UTILIZZO																			\r\n"
        +
        "    ) AS GIORNATE_LAVORATE_MIN,																								\r\n"
        +
        "    (																															\r\n"
        +
        "        SELECT																													\r\n"
        +
        "            GL.GIORNATE_LAVORATE_MAX																							\r\n"
        +
        "        FROM																													\r\n"
        +
        "            IUF_D_GG_LAVORATE GL																								\r\n"
        +
        "        WHERE																													\r\n"
        +
        "            GL.EXT_ID_ZONA_ALTIMETRICA = SP.ID_ZONA_ALTIMETRICA_COMUNE															\r\n"
        +
        "            AND GL.EXT_ID_UTILIZZO = CU.ID_UTILIZZO																			\r\n"
        +
        "    ) AS GIORNATE_LAVORATE_MAX,																								\r\n"
        +

        "    SC.QLI_REIMPIEGATI,																										\r\n"
        +
        "    SC.UF_REIMPIEGATE,																											\r\n"
        +
        "    SC.PREZZO,																													\r\n"
        +
        "    U.PREZZO_MEDIO,																											\r\n"
        +
        "    U.PREZZO_MIN,																											\r\n"
        +
        "    U.PREZZO_MAX,																											\r\n"
        +
        "    U.UF_PRODOTTE,																												\r\n"
        +
        "    SUM(CU.SUPERFICIE_UTILIZZATA) AS SUM_SUPERFICIE_UTILIZZATA																	\r\n"
        +
        "FROM																															\r\n"
        +
        "    IUF_T_SUPERFICIE_COLTURA SC,																								\r\n"
        +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,																							\r\n"
        +
        "    SMRGAA_V_STORICO_PARTICELLA SP,																							\r\n"
        +
        "    SMRGAA_V_CONDUZIONE_UTILIZZO CU,																							\r\n"
        +
        "    IUF_D_UTILIZZO U																											\r\n"
        +
        "WHERE																															\r\n"
        +
        "    PO.ID_PROCEDIMENTO_OGGETTO = SC.ID_PROCEDIMENTO_OGGETTO																	\r\n"
        +
        "    AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA													\r\n"
        +
        "    AND SC.EXT_ID_UTILIZZO = CU.ID_UTILIZZO																					\r\n"
        +
        "    AND SC.EXT_ISTAT_COMUNE = CU.COMUNE																						\r\n"
        +
        "    AND CU.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA																	\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO																	\r\n"
        +
        "    AND SC.ID_SUPERFICIE_COLTURA =:ID_SUPERFICIE_COLTURA																		\r\n"
        +
        "    AND SC.EXT_ID_UTILIZZO = U.EXT_ID_UTILIZZO																					\r\n"
        +
        "GROUP BY																														\r\n"
        +
        "    SC.ID_SUPERFICIE_COLTURA,																									\r\n"
        +
        "    SC.PRODUZIONE_HA,																											\r\n"
        +
        "    SC.QLI_REIMPIEGATI,																										\r\n"
        +
        "    SC.GIORNATE_LAVORATE,																										\r\n"
        +
        "    SC.UF_REIMPIEGATE,																											\r\n"
        +
        "    SC.PREZZO,																													\r\n"
        +
        "    U.UF_PRODOTTE,																												\r\n"
        +
        "    U.PREZZO_MEDIO,																											\r\n"
        +
        "    U.PREZZO_MIN,																											\r\n"
        +
        "    U.PREZZO_MAX,																											\r\n"
        +
        "    CU.ID_UTILIZZO,																											\r\n"
        +
        "    SP.ID_ZONA_ALTIMETRICA_COMUNE																								\r\n"
        +
        "),																																\r\n"
        +
        "TMP_PLV_PARTICELLE AS (																										\r\n"
        +
        "    SELECT 																													\r\n"
        +
        "        P.ID_SUPERFICIE_COLTURA,																								\r\n"
        +
        "        DECODE(NVL(P.UF_REIMPIEGATE,0), 0,																					  	\r\n"
        +
        "            ROUND((P.PRODUZIONE_HA * SUM_SUPERFICIE_UTILIZZATA - NVL(P.QLI_REIMPIEGATI,0)),2),									\r\n"
        +
        "            ROUND(((P.PRODUZIONE_HA * SUM_SUPERFICIE_UTILIZZATA) - (NVL(P.UF_REIMPIEGATE,0) / P.UF_PRODOTTE)),2)   			\r\n"
        +
        "        ) AS PLV_TOT_Q,																										\r\n"
        +
        "        DECODE(NVL(P.UF_REIMPIEGATE,0), 0,																					  	\r\n"
        +
        "            ROUND((P.PRODUZIONE_HA_MEDIA * SUM_SUPERFICIE_UTILIZZATA - NVL(P.QLI_REIMPIEGATI,0)),2),							\r\n"
        +
        "            ROUND(((P.PRODUZIONE_HA_MEDIA * SUM_SUPERFICIE_UTILIZZATA) - (NVL(P.UF_REIMPIEGATE,0) / P.UF_PRODOTTE )),2)   		\r\n"
        +
        "        ) AS PLV_TOT_Q_CALC      																								\r\n"
        +
        "    FROM TMP_PARTICELLE P																										\r\n"
        +
        ")																																\r\n"
        +
        "SELECT 																														\r\n"
        +
        "    ROUND(P.PRODUZIONE_HA * SUM_SUPERFICIE_UTILIZZATA,2) AS PROD_TOTALE,														\r\n"
        +
        "    P.PRODUZIONE_HA_MEDIA * SUM_SUPERFICIE_UTILIZZATA AS PROD_TOTALE_CALC,														\r\n"
        +
        "    ROUND(P.GIORNATE_LAVORATE * SUM_SUPERFICIE_UTILIZZATA,2)  AS GIORNATE_LAVORATIVE_TOT,										\r\n"
        +
        "    P.GIORNATE_LAVORATE_MEDIE * SUM_SUPERFICIE_UTILIZZATA AS GIORNATE_LAVORATIVE_TOT_CALC,										\r\n"
        +
        "    ROUND(P.UF_PRODOTTE * P.PRODUZIONE_HA * SUM_SUPERFICIE_UTILIZZATA,2) AS UF_TOT,											\r\n"
        +
        "    P.UF_PRODOTTE * P.PRODUZIONE_HA_MEDIA * SUM_SUPERFICIE_UTILIZZATA AS UF_TOT_CALC,											\r\n"
        +
        "    PP.PLV_TOT_Q,																												\r\n"
        +
        "    PP.PLV_TOT_Q_CALC,																											\r\n"
        +
        "    ROUND((PP.PLV_TOT_Q * P.PREZZO),2) AS PLV_TOT_DICH,																					\r\n"
        +
        "    (PLV_TOT_Q_CALC * P.PREZZO_MEDIO) AS PLV_TOT_DICH_CALC,																	\r\n"
        +
        "    P.*       																													\r\n"
        +
        "FROM TMP_PARTICELLE P,																											\r\n"
        +
        "     TMP_PLV_PARTICELLE PP																										\r\n"
        +
        "WHERE P.ID_SUPERFICIE_COLTURA = PP.ID_SUPERFICIE_COLTURA"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_SUPERFICIE_COLTURA", idSuperficieColtura,
          Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource,
          SuperficiColtureDettaglioPsrDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_SUPERFICIE_COLTURA", idSuperficieColtura),
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

  public List<SuperficiColtureDettaglioParticellareDTO> getListDettaglioParticellareSuperficiColture(
      long idProcedimentoOggetto, long idSuperficieColtura)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT\r\n" +
        "    sp.id_storico_particella,																\r\n" +
        "    sp.sezione,																			\r\n" +
        "    sp.foglio,																				\r\n" +
        "    sp.particella,																			\r\n" +
        "    sp.subalterno,																			\r\n" +
        "    sp.sup_catastale,																		\r\n" +
        "    cu.superficie_utilizzata,																\r\n" +
        "    cu.desc_titolo_possesso,																\r\n" +
        "    cu.desc_zona_altimetrica																\r\n" +
        "FROM																						\r\n" +
        "    IUF_t_superficie_coltura sc,															\r\n" +
        "    IUF_t_procedimento_oggetto po,														\r\n" +
        "    smrgaa_v_storico_particella sp,														\r\n" +
        "    smrgaa_v_conduzione_utilizzo cu,														\r\n" +
        "    IUF_d_utilizzo u																		\r\n" +
        "WHERE																						\r\n" +
        "    po.id_procedimento_oggetto = sc.id_procedimento_oggetto								\r\n"
        +
        "    AND po.ext_id_dichiarazione_consisten = cu.id_dichiarazione_consistenza				\r\n"
        +
        "    AND sc.ext_id_utilizzo = cu.id_utilizzo												\r\n"
        +
        "    AND cu.id_storico_particella = sp.id_storico_particella								\r\n"
        +
        "    AND sc.ext_id_utilizzo = u.ext_id_utilizzo \r\n" +
        "    AND po.id_procedimento_oggetto =:ID_PROCEDIMENTO_OGGETTO								\r\n"
        +
        "    AND sc.id_superficie_coltura =:ID_SUPERFICIE_COLTURA									\r\n"
        +
        "    AND cu.comune = sc.ext_istat_comune													\r\n";

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_SUPERFICIE_COLTURA", idSuperficieColtura,
          Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          SuperficiColtureDettaglioParticellareDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_SUPERFICIE_COLTURA", idSuperficieColtura),
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

  public int eliminaControlloColtura(long idProcedimentoOggetto,
      long idSuperficieColtura) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_T_CONTROLLO_COLTURA							\r\n"
        +
        "WHERE 															\r\n" +
        "	 ID_SUPERFICIE_COLTURA IN 									\r\n" +
        "    (SELECT ID_SUPERFICIE_COLTURA								\r\n" +
        "    FROM IUF_T_SUPERFICIE_COLTURA							\r\n" +
        "    WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 	\r\n" +
        " 			AND ID_SUPERFICIE_COLTURA =:ID_SUPERFICIE_COLTURA ) \r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_SUPERFICIE_COLTURA", idSuperficieColtura,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_SUPERFICIE_COLTURA", idSuperficieColtura)
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

  public int inserisciControlloColtura(long idProcedimentoOggetto,
      long idSuperficieColtura,
      ControlloColturaDTO controlloColtura) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_CONTROLLO_COLTURA								\r\n"
        +
        "(ID_CONTROLLO_COLTURA,ID_SUPERFICIE_COLTURA,DESCRIZIONE_ANOMALIA,	\r\n"
        +
        "BLOCCANTE)															\r\n" +
        "VALUES (   														\r\n" +
        "    :SEQ_IUF_T_CONTROLLO_COLTURA,								\r\n" +
        "    :ID_SUPERFICIE_COLTURA,										\r\n" +
        "    :DESCRIZIONE_ANOMALIA,											\r\n" +
        "    :BLOCCANTE)";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTControlloColtura = 0;
    try
    {
      seqIuffiTControlloColtura = getNextSequenceValue(
          "SEQ_IUF_T_CONTROLLO_COLTURA");
      mapParameterSource.addValue("SEQ_IUF_T_CONTROLLO_COLTURA",
          seqIuffiTControlloColtura, Types.NUMERIC);
      mapParameterSource.addValue("ID_SUPERFICIE_COLTURA", idSuperficieColtura,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE_ANOMALIA",
          controlloColtura.getDescrizioneAnomalia(), Types.VARCHAR);
      mapParameterSource.addValue("BLOCCANTE", controlloColtura.getBloccante(),
          Types.VARCHAR);
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("SEQ_IUF_T_CONTROLLO_COLTURA",
                  seqIuffiTControlloColtura),
              new LogParameter("ID_SUPERFICIE_COLTURA", idSuperficieColtura),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("DESCRIZIONE_ANOMALIA",
                  controlloColtura.getDescrizioneAnomalia()),
              new LogParameter("BLOCCANTE", controlloColtura.getBloccante()),
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

  public void updateSuperficieColtura(long idProcedimentoOggetto,
      SuperficiColtureDettaglioPsrDTO superficieColturaDettaglioDTO)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_SUPERFICIE_COLTURA									\r\n"
        +
        "SET 																\r\n" +
        "        PRODUZIONE_HA = :PRODUZIONE_HA,							\r\n" +
        "        GIORNATE_LAVORATE = :GIORNATE_LAVORATE,					\r\n" +
        "        QLI_REIMPIEGATI = :QLI_REIMPIEGATI,						\r\n" +
        "        UF_REIMPIEGATE = :UF_REIMPIEGATE,							\r\n" +
        "        PREZZO = :PREZZO,											\r\n" +
        "        NOTE = :NOTE,												\r\n" +
        "		 RECORD_MODIFICATO = 'S'									\r\n" +
        "WHERE   ID_SUPERFICIE_COLTURA = :ID_SUPERFICIE_COLTURA				\r\n" +
        "        AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO		\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_SUPERFICIE_COLTURA",
          superficieColturaDettaglioDTO.getIdSuperficieColtura(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("PRODUZIONE_HA",
          superficieColturaDettaglioDTO.getProduzioneHa(), Types.NUMERIC);
      mapParameterSource.addValue("GIORNATE_LAVORATE",
          superficieColturaDettaglioDTO.getGiornateLavorate(), Types.NUMERIC);
      mapParameterSource.addValue("QLI_REIMPIEGATI",
          superficieColturaDettaglioDTO.getQliReimpiegati(), Types.NUMERIC);
      mapParameterSource.addValue("UF_REIMPIEGATE",
          superficieColturaDettaglioDTO.getUfReimpiegate(), Types.NUMERIC);
      mapParameterSource.addValue("PREZZO",
          superficieColturaDettaglioDTO.getPrezzo(), Types.NUMERIC);
      mapParameterSource.addValue("NOTE",
          superficieColturaDettaglioDTO.getNote(), Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_SUPERFICIE_COLTURA",
                  superficieColturaDettaglioDTO.getIdSuperficieColtura()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("PRODUZIONE_HA",
                  superficieColturaDettaglioDTO.getProduzioneHa()),
              new LogParameter("GIORNATE_LAVORATE",
                  superficieColturaDettaglioDTO.getGiornateLavorate()),
              new LogParameter("QLI_REIMPIEGATI",
                  superficieColturaDettaglioDTO.getQliReimpiegati()),
              new LogParameter("UF_REIMPIEGATE",
                  superficieColturaDettaglioDTO.getUfReimpiegate()),
              new LogParameter("PREZZO",
                  superficieColturaDettaglioDTO.getPrezzo()),
              new LogParameter("NOTE", superficieColturaDettaglioDTO.getNote())
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

  public List<SuperficiColturePlvVegetaleDTO> getListSuperficiColturePlvVegetale(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = getWithRiepilogoSuperficieColtura(" ") +
        "SELECT  																																								\r\n"
        +
        "    ID_UTILIZZO,	 																																					\r\n"
        +
        "    TIPO_UTILIZZO_DESCRIZIONE, 																																		\r\n"
        +
        "    SUM(SUPERFICIE_UTILIZZATA) as SUPERFICIE_UTILIZZATA, 																												\r\n"
        +
        "    SUM(PRODUZIONE_HA * SUPERFICIE_UTILIZZATA) as PRODUZIONE_Q, 																										\r\n"
        +
        "    SUM(GIORNATE_LAVORATIVE_DICH) as GIORNATE_LAV_PER_SUP_UTIL, 																										\r\n"
        +
        "    SUM(UF_TOTALI) as UF, 																																				\r\n"
        +
        "    SUM(NVL(QLI_REIMPIEGATI,0)) AS REIMPIEGHI_Q, 																														\r\n"
        +
        "    SUM(NVL(UF_REIMPIEGATE,0)) AS REIMPIEGHI_UF, 																														\r\n"
        +
        "    SUM(PLV_TOT_DICH) AS PLV 																																			\r\n"
        +
        "FROM TMP_RIEPILOGO_SC PV 																																				\r\n"
        +
        "GROUP BY  																																								\r\n"
        +
        "    ID_UTILIZZO, 																																						\r\n"
        +
        "    TIPO_UTILIZZO_DESCRIZIONE 		"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          SuperficiColturePlvVegetaleDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public List<DecodificaDTO<String>> getListComuniPerProvinciaConTerreniInConduzioneDanniSuperficiColture(
      long idProcedimentoOggetto, String istatProvincia)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT 																						\r\n"
        +
        "    CU.COMUNE AS CODICE,																		\r\n" +
        "    CU.COMUNE AS ID,																			\r\n" +
        "    CU.DESC_COMUNE AS DESCRIZIONE																\r\n"
        +
        "FROM 																							\r\n" +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,															\r\n"
        +
        "    SMRGAA_V_CONDUZIONE_UTILIZZO CU															\r\n"
        +
        "WHERE 																							\r\n" +
        "        PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA					\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO									\r\n"
        +
        "    AND CU.ISTAT_PROVINCIA = :ISTAT_PROVINCIA													\r\n"
        +
        "GROUP BY 																						\r\n" +
        "    CU.COMUNE,																					\r\n" +
        "    CU.DESC_COMUNE																				\r\n" +
        "ORDER BY CU.DESC_COMUNE ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ISTAT_PROVINCIA", istatProvincia,
          Types.VARCHAR);
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ISTAT_PROVINCIA", istatProvincia)
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

  public List<DecodificaDTO<String>> getListProvinciaConTerreniInConduzione(
      long idProcedimentoOggetto,
      String ID_REGIONE_PIEMONTE) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT 																				\r\n" +
        "    CU.ISTAT_PROVINCIA AS CODICE,														\r\n" +
        "    CU.ISTAT_PROVINCIA AS ID,															\r\n" +
        "    CU.DESC_PROVINCIA AS DESCRIZIONE													\r\n" +
        "FROM 																					\r\n" +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,													\r\n" +
        "    SMRGAA_V_CONDUZIONE_UTILIZZO CU													\r\n" +
        "WHERE 																					\r\n" +
        "    	 PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA			\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\r\n"
        +
        "    AND CU.ID_REGIONE = :ID_REGIONE													\r\n" +
        "GROUP BY 																				\r\n" +
        "    CU.ISTAT_PROVINCIA,																\r\n" +
        "    CU.DESC_PROVINCIA																	\r\n" +
        "ORDER BY CU.DESC_PROVINCIA ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_REGIONE", ID_REGIONE_PIEMONTE,
          Types.VARCHAR);
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_REGIONE", ID_REGIONE_PIEMONTE)
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

  public List<DecodificaDTO<String>> getListSezioniPerComuneConTerreniInConduzioneDanniSuperficiColture(
      long idProcedimentoOggetto, String istatComune)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT 																											\r\n"
        +
        "    NVL(CU.SEZIONE,'null') AS ID,                                             										\r\n"
        +
        "    NVL(CU.SEZIONE,'null') AS CODICE,                                         										\r\n"
        +
        "    DECODE(CU.SEZIONE, NULL, 'Non presente', CU.SEZIONE || ' - ' || CU.DESC_SEZIONE) AS DESCRIZIONE               	\r\n"
        +
        "FROM 																												\r\n" +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,																				\r\n"
        +
        "    SMRGAA_V_CONDUZIONE_UTILIZZO CU																				\r\n"
        +
        "WHERE 																												\r\n" +
        "    PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA										\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO														\r\n"
        +
        "    AND CU.COMUNE = :ISTAT_COMUNE																					\r\n"
        +
        "GROUP BY 																											\r\n" +
        "    CU.SEZIONE, CU.DESC_SEZIONE, NVL(CU.SEZIONE,'null'), DECODE(CU.SEZIONE, NULL, 'Non presente', CU.SEZIONE || ' - ' || CU.DESC_SEZIONE)	\r\n"
        +
        "ORDER BY CU.DESC_SEZIONE	";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ISTAT_COMUNE", istatComune)
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

  public List<ParticelleDanniDTO> getListConduzioniDannoSelezionate(
      long idProcedimentoOggetto,
      long[] arrayIdUtilizzoDichiarato,
      boolean piantagioniArboree) throws InternalUnexpectedException
  {
    return getListConduzioniDanno(
        idProcedimentoOggetto,
        null,
        false,
        arrayIdUtilizzoDichiarato,
        piantagioniArboree);
  }

  public List<ParticelleDanniDTO> getListConduzioniDannoEscludendoGiaSelezionate(
      long idProcedimentoOggetto,
      FiltroRicercaConduzioni filtroRicercaConduzioni,
      boolean piantagioniArboreee) throws InternalUnexpectedException
  {
    return getListConduzioniDanno(
        idProcedimentoOggetto,
        filtroRicercaConduzioni,
        true,
        null,
        piantagioniArboreee);
  }

  public List<ParticelleDanniDTO> getListConduzioniEscludendoGiaSelezionate(
      long idProcedimentoOggetto,
      FiltroRicercaConduzioni filtroRicercaConduzioni)
      throws InternalUnexpectedException
  {
    return getListConduzioniDanno(
        idProcedimentoOggetto,
        filtroRicercaConduzioni,
        true,
        null,
        false);
  }

  public List<ParticelleDanniDTO> getListConduzioniDanno(
      long idProcedimentoOggetto,
      FiltroRicercaConduzioni filtroRicercaConduzioni,
      boolean escludiDaRicercaParticelleFiltro,
      long[] arrayIdUtilizzoDichiarato,
      boolean piantagioniArboree)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT																					  				\r\n")
        .append(
            "     CU.ID_UTILIZZO_DICHIARATO,															  						\r\n")
        .append(
            "     CU.FLAG_ARBOREO,															  									\r\n")
        .append(
            "     SP.ISTAT_COMUNE,															  									\r\n")
        .append(
            "     SP.DESC_COMUNE || ' (' || SP.SIGLA_PROVINCIA || ')' as COMUNE,												\r\n")
        .append(
            "     SP.SEZIONE,																									\r\n")
        .append(
            "     SP.SEZIONE || '-' || SP.DESC_SEZIONE AS DESC_SEZIONE,															\r\n")
        .append(
            "     SP.FOGLIO,																									\r\n")
        .append(
            "     SP.PARTICELLA,																								\r\n")
        .append(
            "     SP.SUBALTERNO,																								\r\n")
        .append(
            "     SP.SUP_CATASTALE,																								\r\n")
        .append(
            "     '[' || CU.COD_TIPO_UTILIZZO || '] ' || CU.DESC_TIPO_UTILIZZO AS OCCUPAZIONE_SUOLO,							\r\n")
        .append(
            "     NVL2 (CU.COD_TIPO_UTILIZZO_SECONDARIO, '[' || CU.COD_TIPO_UTILIZZO_SECONDARIO || '] ' || CU.DESC_TIPO_UTILIZZO_SECONDARIO, '') AS OCCUPAZIONE_SUOLO_SECONDARIO,							\r\n")
        .append(
            "     '[' || CU.CODICE_DESTINAZIONE || '] ' || CU.DESCRIZIONE_DESTINAZIONE AS DESTINAZIONE,							\r\n")
        .append(
            "     NVL2(CU.CODICE_DESTINAZIONE_SECONDARIO, '[' || CU.CODICE_DESTINAZIONE_SECONDARIO || '] ' || CU.DESCRI_DESTINAZIONE_SECONDARIA, '') AS DESTINAZIONE_SECONDARIO,							\r\n")
        .append(
            "     '[' || CU.COD_DETTAGLIO_USO || '] ' || CU.DESC_TIPO_DETTAGLIO_USO AS USO,										\r\n")
        .append(
            "     NVL2(CU.COD_DETTAGLIO_USO_SECONDARIO, '[' || CU.COD_DETTAGLIO_USO_SECONDARIO || '] ' || CU.DESC_TIPO_DETTAGLIO_USO_SEC, '') AS USO_SECONDARIO,										\r\n")
        .append(
            "     '[' || CU.CODICE_QUALITA_USO || '] ' || CU.DESCRIZIONE_QUALITA_USO AS QUALITA,								\r\n")
        .append(
            "     NVL2(CU.CODICE_QUALITA_USO_SECONDARIO, '[' || CU.CODICE_QUALITA_USO_SECONDARIO || '] ' || CU.DESCRIZ_QUALITA_USO_SECONDARIA, '') AS QUALITA_SECONDARIO,								\r\n")
        .append(
            "     '[' || CU.COD_TIPO_VARIETA || '] ' || CU.DESC_TIPO_VARIETA AS VARIETA,            							\r\n")
        .append(
            "     CU.SUPERFICIE_UTILIZZATA,																						\r\n")
        .append(
            "     CU.SUP_UTILIZZATA_SECONDARIA,																						\r\n")
        .append(
            "     CU.SUPERFICIE_UTILIZZATA AS SUPERFICIE_COINVOLTA,																\r\n")
        .append(
            "     CU.DESC_ZONA_ALTIMETRICA																						\r\n")
        .append(
            " FROM																								  				\r\n")
        .append(
            "     IUF_T_PROCEDIMENTO_OGGETTO PO,																  				\r\n")
        .append(
            "     SMRGAA_V_STORICO_PARTICELLA SP,																  				\r\n")
        .append(
            "     SMRGAA_V_CONDUZIONE_UTILIZZO CU																				\r\n")
        .append(
            " WHERE																								  				\r\n")
        .append(
            "     	  PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA						  				\r\n")
        .append(
            "     AND CU.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA								  						\r\n")
        .append(
            "     AND PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO														\r\n");
    if (filtroRicercaConduzioni != null)
    {
      sb.append("		AND SP.FOGLIO = :FOGLIO \r\n");
      sb.append("		AND SP.ISTAT_COMUNE = :ISTAT_COMUNE \r\n");
      if (filtroRicercaConduzioni.getSezione() != null
          && !filtroRicercaConduzioni.getSezione().equals(""))
      {
        sb.append("		AND SP.SEZIONE = :SEZIONE \r\n");
      }
      if (filtroRicercaConduzioni.getParticella() != null)
      {
        sb.append("		AND SP.PARTICELLA = :PARTICELLA \r\n");
      }
      if (filtroRicercaConduzioni.getSubalterno() != null
          && !filtroRicercaConduzioni.getSubalterno().equals(""))
      {
        sb.append("		AND SP.SUBALTERNO = :SUBALTERNO \r\n");
      }
      if (filtroRicercaConduzioni.getChiaviConduzioniInserite() != null
          && filtroRicercaConduzioni.getChiaviConduzioniInserite().length > 0
          && escludiDaRicercaParticelleFiltro)
      {

        sb.append(getNotInCondition("CU.ID_UTILIZZO_DICHIARATO",
            IuffiUtils.ARRAY.toListOfLong(
                filtroRicercaConduzioni.getChiaviConduzioniInserite())));
      }
    }
    if (piantagioniArboree == true)
    {
      sb.append("		AND CU.FLAG_ARBOREO = 'S' \r\n");
    }

    if (arrayIdUtilizzoDichiarato != null
        && arrayIdUtilizzoDichiarato.length > 0)
    {
      sb.append(getInCondition("CU.ID_UTILIZZO_DICHIARATO",
          arrayIdUtilizzoDichiarato));
    }
    sb.append("  ORDER BY SP.SIGLA_PROVINCIA, SP.DESC_COMUNE");

    final String QUERY = sb.toString();
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      if (filtroRicercaConduzioni != null)
      {
        mapParameterSource.addValue("ISTAT_COMUNE",
            filtroRicercaConduzioni.getIstatComune(), Types.VARCHAR);
        mapParameterSource.addValue("SEZIONE",
            filtroRicercaConduzioni.getSezione(), Types.VARCHAR);
        mapParameterSource.addValue("FOGLIO",
            filtroRicercaConduzioni.getFoglio(), Types.NUMERIC);
        if (filtroRicercaConduzioni.getParticella() != null)
        {
          mapParameterSource.addValue("PARTICELLA",
              filtroRicercaConduzioni.getParticella(), Types.NUMERIC);
        }
        if (filtroRicercaConduzioni.getSubalterno() != null
            && !filtroRicercaConduzioni.getSubalterno().equals(""))
        {
          mapParameterSource.addValue("SUBALTERNO",
              filtroRicercaConduzioni.getSubalterno(), Types.VARCHAR);
        }
      }
      return queryForList(QUERY, mapParameterSource, ParticelleDanniDTO.class);
    }
    catch (Throwable t)
    {
      LogParameter[] logParameters;
      List<LogParameter> listLogParamaters = new ArrayList<LogParameter>();
      listLogParamaters.add(
          new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto));
      if (filtroRicercaConduzioni != null)
      {
        listLogParamaters.add(new LogParameter("ISTAT_COMUNE",
            filtroRicercaConduzioni.getIstatComune()));
        listLogParamaters.add(
            new LogParameter("SEZIONE", filtroRicercaConduzioni.getSezione()));
        listLogParamaters.add(
            new LogParameter("FOGLIO", filtroRicercaConduzioni.getFoglio()));
        listLogParamaters.add(new LogParameter("PARTICELLA",
            filtroRicercaConduzioni.getParticella()));
        listLogParamaters.add(new LogParameter("SUBALTERNO",
            filtroRicercaConduzioni.getSubalterno()));
        listLogParamaters.add(new LogParameter("ID_UTILIZZO_DICHIARATO",
            filtroRicercaConduzioni.getChiaviConduzioniInserite()));
      }
      listLogParamaters.add(new LogParameter("escludiDaRicercaParticelleFiltro",
          escludiDaRicercaParticelleFiltro));
      listLogParamaters.add(new LogParameter("arrayIdUtilizzoDichiarato",
          arrayIdUtilizzoDichiarato));
      logParameters = new LogParameter[listLogParamaters.size()];
      logParameters = listLogParamaters.toArray(logParameters);

      InternalUnexpectedException e = new InternalUnexpectedException(t,
          logParameters,
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

  public List<ParticelleDanniDTO> getListConduzioniDanno(
      long idProcedimentoOggetto,
      long idDannoAtm) throws InternalUnexpectedException
  {
    return getListConduzioniDanno(idProcedimentoOggetto, new long[]
    { idDannoAtm });
  }

  public List<ParticelleDanniDTO> getListConduzioniDanno(
      long idProcedimentoOggetto,
      long[] arrayIdDannoAtm) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String inConditionIdDannoAtm = " ";
    if (arrayIdDannoAtm != null)
    {
      inConditionIdDannoAtm = getInCondition("DA.ID_DANNO_ATM",
          arrayIdDannoAtm);
    }

    StringBuilder sb = new StringBuilder();

    sb.append(
        " SELECT																					  				\r\n")
        .append(
            "     CU.ID_UTILIZZO_DICHIARATO,															  						\r\n")
        .append(
            "     CU.FLAG_ARBOREO,															  									\r\n")
        .append(
            "     SP.ISTAT_COMUNE,															  									\r\n")
        .append(
            "     SP.DESC_PROVINCIA,															  									\r\n")
        .append(
            "     SP.DESC_COMUNE,															  									\r\n")
        .append(
            "     SP.DESC_COMUNE || ' (' || SP.SIGLA_PROVINCIA || ')' as COMUNE,												\r\n")
        .append(
            "     SP.SEZIONE,																									\r\n")
        .append(
            "     SP.SEZIONE || '-' || SP.DESC_SEZIONE AS DESC_SEZIONE,															\r\n")
        .append(
            "     SP.FOGLIO,																									\r\n")
        .append(
            "     SP.PARTICELLA,																								\r\n")
        .append(
            "     SP.SUBALTERNO,																								\r\n")
        .append(
            "     SP.SUP_CATASTALE,																								\r\n")
        .append(
            "     CU.DESC_TIPO_UTILIZZO,																								\r\n")
        .append(
            "     '[' || CU.COD_TIPO_UTILIZZO || '] ' || CU.DESC_TIPO_UTILIZZO AS OCCUPAZIONE_SUOLO,							\r\n")
        .append(
            "     '[' || CU.CODICE_DESTINAZIONE || '] ' || CU.DESCRIZIONE_DESTINAZIONE AS DESTINAZIONE,							\r\n")
        .append(
            "     '[' || CU.COD_DETTAGLIO_USO || '] ' || CU.DESC_TIPO_DETTAGLIO_USO AS USO,										\r\n")
        .append(
            "     '[' || CU.CODICE_QUALITA_USO || '] ' || CU.DESCRIZIONE_QUALITA_USO AS QUALITA,								\r\n")
        .append(
            "     '[' || CU.COD_TIPO_VARIETA || '] ' || CU.DESC_TIPO_VARIETA AS VARIETA,            							\r\n")
        .append(
            "     CU.SUPERFICIE_UTILIZZATA,																						\r\n")
        .append(
            "     DA.ID_DANNO_ATM,																								\r\n")
        .append(
            "     DA.PROGRESSIVO																								\r\n")
        .append(
            " FROM																								  				\r\n")
        .append(
            "     IUF_T_PROCEDIMENTO_OGGETTO PO,																  				\r\n")
        .append(
            "     SMRGAA_V_STORICO_PARTICELLA SP,																  				\r\n")
        .append(
            "     SMRGAA_V_CONDUZIONE_UTILIZZO CU,																				\r\n")
        .append(
            "     IUF_R_PARTICELLA_DANNEGGIATA PD,																				\r\n")
        .append(
            "     IUF_T_DANNO_ATM DA																				\r\n")
        .append(
            " WHERE																								  				\r\n")
        .append(
            "     	  PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA						  				\r\n")
        .append(
            "     AND CU.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA								  						\r\n")
        .append(
            "     AND PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO														\r\n")
        .append(
            "     AND DA.ID_DANNO_ATM = PD.ID_DANNO_ATM																			\r\n")
        .append(
            "	  AND PD.EXT_ID_UTILIZZO_DICHIARATO = CU.ID_UTILIZZO_DICHIARATO 												\r\n")
        .append(
            inConditionIdDannoAtm)
        .append(
            " ORDER BY DA.PROGRESSIVO");
    ;
    final String QUERY = sb.toString();
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, ParticelleDanniDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_DANNO_ATM", arrayIdDannoAtm)
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

  public BigDecimal getSumSuperficiCatastaliParticelle(
      long idProcedimentoOggetto, long[] arrayIdUtilizzoDichiarato)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT 																					\r\n"
        +
        "        SUM(CU.SUPERFICIE_UTILIZZATA) AS SUPERFICIE_UTILIZZATA						     	\r\n"
        +
        " FROM																				 		\r\n" +
        "        IUF_T_PROCEDIMENTO_OGGETTO PO,												 	\r\n"
        +
        "        SMRGAA_V_STORICO_PARTICELLA SP,												 	\r\n"
        +
        "        SMRGAA_V_CONDUZIONE_UTILIZZO CU												 	\r\n"
        +
        " WHERE																				 		\r\n" +
        "            PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA		 	\r\n"
        +
        "        AND CU.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA 						 	\r\n"
        +
        "        AND PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO							\r\n"
        +
        getInCondition("CU.ID_UTILIZZO_DICHIARATO", arrayIdUtilizzoDichiarato);

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return new BigDecimal(queryForString(QUERY, mapParameterSource));
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_UTILIZZO_DICHIARATO",
                  arrayIdUtilizzoDichiarato)
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

  public List<AllevamentiDTO> getDettaglioAllevamento(
      long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune)
      throws InternalUnexpectedException
  {
    return getListRiepilogoAllevamenti(idProcedimentoOggetto,
        idCategoriaAnimale, istatComune, true);
  }

  private String getWithTmpAllevamenti(boolean isDettaglio)
  {
    String queryTmpAllevamenti = "TMP_ALLEVAMENTI AS (																							\r\n"
        +
        "    SELECT 																										\r\n" +
        "        A.CODICE_AZIENDA_ZOOTECNICA,																				\r\n"
        +
        "        A.ID_CATEGORIA_ANIMALE,																					\r\n"
        +
        "        A.ISTAT_COMUNE,																							\r\n"
        +
        "        A.DESCRIZIONE_SPECIE_ANIMALE,																				\r\n"
        +
        "        A.DESCRIZIONE_CATEGORIA_ANIMALE,																			\r\n"
        +
        "        A.UNITA_MISURA_SPECIE,\r\n" +
        "        SUM(A.QUANTITA) AS QUANTITA\r\n" +
        "    FROM \r\n" +
        "        IUF_T_PROCEDIMENTO_OGGETTO PO,\r\n" +
        "        SMRGAA_V_ALLEVAMENTI A\r\n" +
        "    WHERE\r\n" +
        "        PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO\r\n" +
        "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA\r\n";
    if (isDettaglio)
    {
      queryTmpAllevamenti = queryTmpAllevamenti +

          "        AND A.ID_CATEGORIA_ANIMALE = :ID_CATEGORIA_ANIMALE 															\r\n"
          +
          "        AND A.ISTAT_COMUNE = :ISTAT_COMUNE																			\r\n";
    }
    queryTmpAllevamenti = queryTmpAllevamenti +
        "    GROUP BY																										\r\n" +
        "        A.CODICE_AZIENDA_ZOOTECNICA,																				\r\n"
        +
        "        A.ID_CATEGORIA_ANIMALE,																					\r\n"
        +
        "        A.ISTAT_COMUNE,																							\r\n"
        +
        "        A.DESCRIZIONE_SPECIE_ANIMALE,																				\r\n"
        +
        "        A.DESCRIZIONE_CATEGORIA_ANIMALE,																																												\r\n"
        +
        "        A.UNITA_MISURA_SPECIE\r\n" +
        ") 	";
    return queryTmpAllevamenti;
  }

  // Questa query fu sviluppata per gestire casi di assenza di record in
  // IUF_T_PRODUZIONE_ZOOTECNICA. Funzionando a dovere non l'ho più
  // modificata.
  public List<AllevamentiDTO> getListRiepilogoAllevamenti(
      long idProcedimentoOggetto, Long idCategoriaAnimale, String istatComune,
      boolean isDettaglio) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = "WITH " +
        WITH_PRODUZIONE_ZOOTECNICA +
        ", " +
        WITH_TMP_ALLEVAMENTI +
        " , " +
        WITH_TMP_ALLEVAMENTO_CAT_PROD +
        " , " +
        WITH_TMP_ALL_PROD_ZOO_PROD_VEND +

        ", TMP_FINALE AS ( \r\n" +
        "SELECT \r\n" +
        "	 TACP.CODICE_AZIENDA_ZOOTECNICA, \r\n" +
        "    TACP.SIGLA_PROVINCIA,\r\n" +
        "    TACP.DESCRIZIONE_COMUNE,\r\n" +
        "    TACP.UBICAZIONE_ALLEVAMENTO,\r\n" +
        "    TACP.ID_CATEGORIA_ANIMALE,																						\r\n"
        +
        "    TACP.ISTAT_COMUNE,																								\r\n"
        +
        "    TACP.DESCRIZIONE_SPECIE_ANIMALE,																					\r\n"
        +
        "    TACP.DESCRIZIONE_CATEGORIA_ANIMALE,																				\r\n"
        +
        "    TACP.QUANTITA,																									\r\n"
        +
        "    TACP.UNITA_MISURA_SPECIE,																							\r\n"
        +
        "    TACP.COEFFICIENTE_UBA,																							\r\n"
        +
        "    TACP.UNITA_FORAGGERE,\r\n" +
        "    NVL(TAPZPV.GIORNATE_LAVORATIVE_MEDIE,TACP.GIORNATE_LAVORATIVE_MEDIE) AS GIORNATE_LAVORATIVE_MEDIE,\r\n"
        +
        "    (NVL(TAPZPV.GIORNATE_LAVORATIVE_MEDIE,TACP.GIORNATE_LAVORATIVE_MEDIE) * TACP.QUANTITA) AS GIORNATE_LAVORATIVE,\r\n"
        +
        "    TAPZPV.PZ_ID_PRODUZIONE_ZOOTECNICA,\r\n" +
        "    TAPZPV.ID_PRODUZIONE_ZOOTECNICA,\r\n" +
        "    TAPZPV.PV_ID_PRODUZIONE_ZOOTECNICA,\r\n" +
        "    TAPZPV.DATA_ULTIMO_AGGIORNAMENTO,\r\n" +
        "    TAPZPV.EXT_ID_UTENTE_AGGIORNAMENTO,\r\n" +

        "    NVL(TAPZPV.PESO_VIVO_MEDIO,TACP.PESO_VIVO_MEDIO) AS PESO_VIVO_MEDIO,\r\n"
        +
        "    TAPZPV.GIORNATE_LAVORATIVE_TOTALI, \r\n" +

        "    DECODE(TAPZPV.PLV, NULL, 0, TAPZPV.PLV) AS PLV\r\n" +
        "FROM     \r\n" +
        "    TMP_ALLEVAMENTO_CAT_PROD TACP,\r\n" +
        "    TMP_ALL_PROD_ZOO_PROD_VEND TAPZPV\r\n" +
        "WHERE\r\n" +
        "        TACP.ID_CATEGORIA_ANIMALE = TAPZPV.ID_CATEGORIA_ANIMALE\r\n" +
        "    AND TACP.ISTAT_COMUNE = TAPZPV.ISTAT_COMUNE\r\n" +
        "GROUP BY\r\n" +
        "	 TACP.CODICE_AZIENDA_ZOOTECNICA, \r\n" +
        "    TACP.SIGLA_PROVINCIA,\r\n" +
        "    TACP.DESCRIZIONE_COMUNE,\r\n" +
        "    TACP.UBICAZIONE_ALLEVAMENTO,\r\n" +
        "    TACP.ID_CATEGORIA_ANIMALE,																						\r\n"
        +
        "    TACP.ISTAT_COMUNE,																								\r\n"
        +
        "    TACP.DESCRIZIONE_SPECIE_ANIMALE,																					\r\n"
        +
        "    TACP.DESCRIZIONE_CATEGORIA_ANIMALE,																				\r\n"
        +
        "    TACP.QUANTITA,																									\r\n"
        +
        "    TACP.UNITA_MISURA_SPECIE,																							\r\n"
        +
        "    TACP.COEFFICIENTE_UBA,																							\r\n"
        +
        "    TACP.UNITA_FORAGGERE,\r\n" +
        "    NVL(TAPZPV.GIORNATE_LAVORATIVE_MEDIE,TACP.GIORNATE_LAVORATIVE_MEDIE),\r\n"
        +
        "    (NVL(TAPZPV.GIORNATE_LAVORATIVE_MEDIE,TACP.GIORNATE_LAVORATIVE_MEDIE) * TACP.QUANTITA),\r\n"
        +
        "    TAPZPV.PZ_ID_PRODUZIONE_ZOOTECNICA,\r\n" +
        "    TAPZPV.ID_PRODUZIONE_ZOOTECNICA,\r\n" +
        "    TAPZPV.PV_ID_PRODUZIONE_ZOOTECNICA,\r\n" +
        "    TAPZPV.DATA_ULTIMO_AGGIORNAMENTO,\r\n" +
        "    TAPZPV.EXT_ID_UTENTE_AGGIORNAMENTO,\r\n" +

        "    NVL(TAPZPV.PESO_VIVO_MEDIO,TACP.PESO_VIVO_MEDIO),\r\n" +
        "    TAPZPV.GIORNATE_LAVORATIVE_TOTALI, \r\n" +

        "    DECODE(TAPZPV.PLV, NULL, 0, TAPZPV.PLV)\r\n" +
        "ORDER BY\r\n" +
        "    TACP.SIGLA_PROVINCIA,\r\n" +
        "    TACP.DESCRIZIONE_COMUNE,\r\n" +
        "    TACP.DESCRIZIONE_SPECIE_ANIMALE,																					\r\n"
        +
        "    TACP.DESCRIZIONE_CATEGORIA_ANIMALE \r\n" +
        ") \r\n" +
        "SELECT \r\n" +
        "    TF.*,\r\n" +
        "    (SELECT NOTE\r\n" +
        "     FROM IUF_T_PRODUZIONE_ZOOTECNICA PZ\r\n" +
        "     WHERE \r\n" +
        "            PZ.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO\r\n"
        +
        "        AND PZ.EXT_ID_CATEGORIA_ANIMALE = TF.ID_CATEGORIA_ANIMALE\r\n"
        +
        "        AND PZ.EXT_ISTAT_COMUNE = TF.ISTAT_COMUNE \r\n" +
        "    ) AS NOTE\r\n" +
        "FROM \r\n" +
        "    TMP_FINALE TF ";
    if (isDettaglio)
    {
      QUERY = QUERY +
          "WHERE \r\n" +
          "	 		TF.ID_CATEGORIA_ANIMALE = :ID_CATEGORIA_ANIMALE 	\r\n" +
          "		AND TF.ISTAT_COMUNE = :ISTAT_COMUNE 					\r\n";
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      if (isDettaglio)
      {
        mapParameterSource.addValue("ID_CATEGORIA_ANIMALE", idCategoriaAnimale,
            Types.NUMERIC);
      }
      if (isDettaglio)
      {
        mapParameterSource.addValue("ID_CATEGORIA_ANIMALE", idCategoriaAnimale,
            Types.NUMERIC);
        mapParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
      }
      return queryForList(QUERY, mapParameterSource, AllevamentiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_CATEGORIA_ANIMALE", idCategoriaAnimale),
              new LogParameter("ISTAT_COMUNE", istatComune)
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

  public List<AllevamentiDettaglioPlvDTO> getListDettaglioAllevamenti(
      long idProcedimentoOggetto, long idCategoriaAnimale, String istatComune)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "WITH " +
        WITH_PRODUZIONE_ZOOTECNICA +
        ", " +
        WITH_TMP_ALLEVAMENTI +
        ", " +
        " TMP_PROD_VEND_DETT AS (\r\n" +
        "    SELECT 																											\r\n"
        +
        "        P.ID_PRODUZIONE,\r\n" +
        "        P.DESCRIZIONE AS DESC_PRODUZIONE,\r\n" +
        "        PV.NUMERO_CAPI,\r\n" +
        "        PV.QUANTITA_PRODOTTA,\r\n" +
        "        UM.ID_UNITA_MISURA,\r\n" +
        "        UM.DESCRIZIONE AS DESC_UNITA_MISURA,\r\n" +
        "        PV.QUANTITA_PRODOTTA * PV.NUMERO_CAPI AS PROD_LORDA,\r\n" +
        "        PV.QUANTITA_REIMPIEGATA,\r\n" +
        "        PV.QUANTITA_PRODOTTA * PV.NUMERO_CAPI - PV.QUANTITA_REIMPIEGATA AS PROD_NETTA,\r\n"
        +
        "        PV.PREZZO,\r\n" +
        "        (PV.NUMERO_CAPI * PV.QUANTITA_PRODOTTA - PV.QUANTITA_REIMPIEGATA) * PV.PREZZO AS IMPORTO_TOTALE,\r\n"
        +
        "        A.ISTAT_COMUNE,\r\n" +
        "		 A.CODICE_AZIENDA_ZOOTECNICA,\r\n" +
        "        PZ.ID_PRODUZIONE_ZOOTECNICA,\r\n" +
        "        PZ.GIORNATE_LAVORATIVE_MEDIE,\r\n" +
        "        PZ.ID_PRODUZIONE_ZOOTECNICA AS PZ_ID_PRODUZIONE_ZOOTECNICA,																					\r\n"
        +
        "        PV.ID_PRODUZIONE_ZOOTECNICA AS PV_ID_PRODUZIONE_ZOOTECNICA												\r\n"
        +
        "    FROM																												\r\n" +
        "        TMP_ALLEVAMENTI A,		\r\n" +
        "        PRODUZIONE_ZOOTECNICA PZ,\r\n" +
        "        IUF_T_PRODUZIONE_VENDIBILE PV,																				\r\n"
        +
        "        IUF_D_PRODUZIONE P,\r\n" +
        "        IUF_D_UNITA_MISURA UM\r\n" +
        "    WHERE 																												\r\n"
        +
        "             A.ID_CATEGORIA_ANIMALE = PZ.EXT_ID_CATEGORIA_ANIMALE	  											\r\n"
        +
        "        AND PZ.ID_PRODUZIONE_ZOOTECNICA = PV.ID_PRODUZIONE_ZOOTECNICA 												\r\n"
        +
        "        AND P.ID_PRODUZIONE = PV.ID_PRODUZIONE                    \r\n"
        +
        "        AND P.ID_UNITA_MISURA = UM.ID_UNITA_MISURA\r\n" +
        "        AND A.ID_CATEGORIA_ANIMALE = :ID_CATEGORIA_ANIMALE\r\n" +
        "        AND A.ISTAT_COMUNE = :ISTAT_COMUNE\r\n" +
        ")\r\n" +
        "SELECT * \r\n" +
        "FROM   TMP_PROD_VEND_DETT\r\n" +
        "ORDER BY DESC_PRODUZIONE";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_CATEGORIA_ANIMALE", idCategoriaAnimale,
          Types.NUMERIC);
      mapParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
      return queryForList(QUERY, mapParameterSource,
          AllevamentiDettaglioPlvDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_CATEGORIA_ANIMALE", idCategoriaAnimale),
              new LogParameter("ISTAT_COMUNE", istatComune)
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

  public List<DecodificaDTO<Integer>> getListProduzioniVendibili(
      long idCategoriaAnimale)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = "    SELECT 																						\r\n"
        +
        "        P.ID_PRODUZIONE AS ID,																		\r\n"
        +
        "        P.ID_PRODUZIONE AS CODICE,																	\r\n"
        +
        "        P.DESCRIZIONE AS DESCRIZIONE																\r\n"
        +
        "    FROM IUF_D_PRODUZIONE P																		\r\n"
        +
        "    WHERE P.EXT_ID_CATEGORIA_ANIMALE = :ID_CATEGORIA_ANIMALE										\r\n"
        +
        "    ORDER BY DESCRIZIONE ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CATEGORIA_ANIMALE", idCategoriaAnimale,
          Types.NUMERIC);
      return queryForDecodificaInteger(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_CATEGORIA_ANIMALE", idCategoriaAnimale)
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

  public List<DecodificaDTO<Integer>> getListUnitaMisuraProduzioniVendibili(
      long idCategoriaAnimale)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = "    SELECT 																							\r\n"
        +
        "        	P.ID_PRODUZIONE AS ID,																		\r\n"
        +
        "       	P.ID_PRODUZIONE AS CODICE,																	\r\n"
        +
        "        	UM.DESCRIZIONE AS DESCRIZIONE																\r\n"
        +
        "    FROM 	IUF_D_PRODUZIONE P,																		\r\n"
        +
        "			IUF_D_UNITA_MISURA UM 																	\r\n" +
        "    WHERE 		P.EXT_ID_CATEGORIA_ANIMALE = :ID_CATEGORIA_ANIMALE										\r\n"
        +
        "    		AND P.ID_UNITA_MISURA = UM.ID_UNITA_MISURA													\r\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CATEGORIA_ANIMALE", idCategoriaAnimale,
          Types.NUMERIC);
      return queryForDecodificaInteger(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_CATEGORIA_ANIMALE", idCategoriaAnimale)
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

  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioniCategorieAnimali(
      long idProcedimentoOggetto,
      long idCategoriaAnimale,
      String istatComune)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "WITH PRODUZIONE_VENDIBILE AS (																	\r\n"
        +
        "	SELECT																						\r\n" +
        "		PV.ID_PRODUZIONE																		\r\n" +
        "	FROM 																						\r\n" +
        "		IUF_T_PROCEDIMENTO_OGGETTO PO,														\r\n" +
        "		SMRGAA_V_ALLEVAMENTI A,																	\r\n" +
        "		IUF_T_PRODUZIONE_ZOOTECNICA PZ,														\r\n" +
        "		IUF_T_PRODUZIONE_VENDIBILE PV															\r\n" +
        "	WHERE																						\r\n" +
        "		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO									\r\n"
        +
        "		AND A.ID_CATEGORIA_ANIMALE = :ID_CATEGORIA_ANIMALE										\r\n"
        +
        "		AND A.ISTAT_COMUNE = :ISTAT_COMUNE														\r\n"
        +
        "		AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA					\r\n"
        +
        "		AND PO.ID_PROCEDIMENTO_OGGETTO  = PZ.ID_PROCEDIMENTO_OGGETTO							\r\n"
        +
        "		AND A.ID_CATEGORIA_ANIMALE = PZ.EXT_ID_CATEGORIA_ANIMALE								\r\n"
        +
        "		AND PZ.ID_PRODUZIONE_ZOOTECNICA = PV.ID_PRODUZIONE_ZOOTECNICA							\r\n"
        +
        "	GROUP BY PV.ID_PRODUZIONE																	\r\n" +
        "), PRODUZIONE_ZOOTECNICA AS (																	\r\n" +
        "    SELECT  *																					\r\n" +
        "    FROM																						\r\n" +
        "        IUF_T_PRODUZIONE_ZOOTECNICA PZ														\r\n"
        +
        "    WHERE 																						\r\n" +
        "        PZ.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO									\r\n"
        +
        ")																								\r\n" +
        "SELECT 																						\r\n" +
        "	 P.*,																						\r\n" +
        "    CA.*,																						\r\n" +
        "    UM.*																						\r\n" +
        "FROM 																							\r\n" +
        "    IUF_D_PRODUZIONE P,																		\r\n" +
        "    IUF_D_CATEGORIA_ANIMALE CA,																\r\n"
        +
        "    IUF_D_UNITA_MISURA UM																	\r\n" +
        "WHERE 																							\r\n" +
        "    P.EXT_ID_CATEGORIA_ANIMALE = :ID_CATEGORIA_ANIMALE											\r\n"
        +
        "    AND P.EXT_ID_CATEGORIA_ANIMALE = CA.EXT_ID_CATEGORIA_ANIMALE								\r\n"
        +
        "    AND P.ID_UNITA_MISURA = UM.ID_UNITA_MISURA													\r\n"
        +
        "	AND P.ID_PRODUZIONE NOT IN																	\r\n" +
        "        (																						\r\n" +
        "			SELECT PV.ID_PRODUZIONE																\r\n" +
        "			FROM 																				\r\n" +
        "                PRODUZIONE_VENDIBILE PV														\r\n"
        +
        "		)																						\r\n" +
        "ORDER BY P.DESCRIZIONE ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_CATEGORIA_ANIMALE", idCategoriaAnimale,
          Types.NUMERIC);
      mapParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
      return queryForList(QUERY, mapParameterSource,
          ProduzioneCategoriaAnimaleDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_CATEGORIA_ANIMALE", idCategoriaAnimale),
              new LogParameter("ISTAT_COMUNE", istatComune)
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

  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioniVendibiliGiaInserite(
      long idProcedimentoOggetto,
      long idCategoriaAnimale,
      String istatComune)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder sb = new StringBuilder();
    sb.append(
        "SELECT																						\r\n").append(
            "        PV.ID_PRODUZIONE,																	\r\n")
        .append(
            "        PV.ID_PRODUZIONE_VENDIBILE,														\r\n")
        .append(
            "        PV.ID_PRODUZIONE_ZOOTECNICA,														\r\n")
        .append(
            "        PV.NUMERO_CAPI,																	\r\n")
        .append(
            "        PV.PREZZO,																			\r\n")
        .append(
            "        PV.QUANTITA_PRODOTTA,																\r\n")
        .append(
            "        PV.QUANTITA_REIMPIEGATA,															\r\n")
        .append(
            "        P.PREZZO_MIN,																		\r\n")
        .append(
            "        P.PREZZO_MEDIO,																	\r\n")
        .append(
            "        P.PREZZO_MAX,																		\r\n")
        .append(
            "        P.QUANTITA_PRODOTTA_MIN,															\r\n")
        .append(
            "        P.QUANTITA_PRODOTTA_MAX,															\r\n")
        .append(
            "        P.QUANTITA_PRODOTTA_MEDIA,															\r\n")
        .append(
            "        P.DESCRIZIONE AS DESC_PRODUZIONE,													\r\n")
        .append(
            "        UM.ID_UNITA_MISURA,																\r\n")
        .append(
            "        UM.DESCRIZIONE AS DESC_UNITA_MISURA,												\r\n")
        .append(
            "        CA.PESO_VIVO_MIN,																	\r\n")
        .append(
            "        CA.PESO_VIVO_MEDIO,																\r\n")
        .append(
            "        CA.PESO_VIVO_MAX,																	\r\n")
        .append(
            "        CA.GIORNATE_LAVORATIVE_MIN,														\r\n")
        .append(
            "        CA.GIORNATE_LAVORATIVE_MEDIE,														\r\n")
        .append(
            "        CA.GIORNATE_LAVORATIVE_MAX,														\r\n")
        .append(
            "        CA.CONSUMO_ANNUO_UF,																\r\n")
        .append(
            "        CA.NUMERO_MAX_ANIMALI_PER_HA,														\r\n")
        .append(
            "        CA.AGEA_COD																		\r\n")
        .append(
            "	FROM 																					\r\n")
        .append(
            "		IUF_T_PROCEDIMENTO_OGGETTO PO,													\r\n")
        .append(
            "		SMRGAA_V_ALLEVAMENTI A,																\r\n")
        .append(
            "		IUF_T_PRODUZIONE_ZOOTECNICA PZ,													\r\n")
        .append(
            "		IUF_T_PRODUZIONE_VENDIBILE PV,													\r\n")
        .append(
            "       IUF_D_PRODUZIONE P,																\r\n")
        .append(
            "       IUF_D_UNITA_MISURA UM,															\r\n")
        .append(
            "       IUF_D_CATEGORIA_ANIMALE CA														\r\n")
        .append(
            "	WHERE																					\r\n")
        .append(
            "		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO								\r\n")
        .append(
            "		AND A.ID_CATEGORIA_ANIMALE = :ID_CATEGORIA_ANIMALE									\r\n")
        .append(
            "		AND A.ISTAT_COMUNE = :ISTAT_COMUNE													\r\n")
        .append(
            "		AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA				\r\n")
        .append(
            "		AND PO.ID_PROCEDIMENTO_OGGETTO  = PZ.ID_PROCEDIMENTO_OGGETTO						\r\n")
        .append(
            "		AND A.ID_CATEGORIA_ANIMALE = PZ.EXT_ID_CATEGORIA_ANIMALE							\r\n")
        .append(
            "		AND PZ.ID_PRODUZIONE_ZOOTECNICA = PV.ID_PRODUZIONE_ZOOTECNICA						\r\n")
        .append(
            "       AND PV.ID_PRODUZIONE = P.ID_PRODUZIONE												\r\n")
        .append(
            "       AND P.ID_UNITA_MISURA = UM.ID_UNITA_MISURA											\r\n")
        .append(
            "       AND P.EXT_ID_CATEGORIA_ANIMALE = CA.EXT_ID_CATEGORIA_ANIMALE						\r\n")
        .append(
            "GROUP BY																					\r\n")
        .append(
            "        PV.ID_PRODUZIONE,																	\r\n")
        .append(
            "        PV.ID_PRODUZIONE_VENDIBILE,														\r\n")
        .append(
            "        PV.ID_PRODUZIONE_ZOOTECNICA,														\r\n")
        .append(
            "        PV.NUMERO_CAPI,																	\r\n")
        .append(
            "        PV.PREZZO,																			\r\n")
        .append(
            "        PV.QUANTITA_PRODOTTA,																\r\n")
        .append(
            "        PV.QUANTITA_REIMPIEGATA,															\r\n")
        .append(
            "        P.PREZZO_MIN,																		\r\n")
        .append(
            "        P.DESCRIZIONE,																		\r\n")
        .append(
            "        P.PREZZO_MEDIO,																	\r\n")
        .append(
            "        P.PREZZO_MAX,																		\r\n")
        .append(
            "        P.QUANTITA_PRODOTTA_MIN,															\r\n")
        .append(
            "        P.QUANTITA_PRODOTTA_MAX,															\r\n")
        .append(
            "        P.QUANTITA_PRODOTTA_MEDIA,															\r\n")
        .append(
            "        UM.ID_UNITA_MISURA,																\r\n")
        .append(
            "        UM.DESCRIZIONE,																	\r\n")
        .append(
            "        CA.PESO_VIVO_MIN,																	\r\n")
        .append(
            "        CA.PESO_VIVO_MEDIO,																\r\n")
        .append(
            "        CA.PESO_VIVO_MAX,																	\r\n")
        .append(
            "        CA.GIORNATE_LAVORATIVE_MIN,														\r\n")
        .append(
            "        CA.GIORNATE_LAVORATIVE_MEDIE,														\r\n")
        .append(
            "        CA.GIORNATE_LAVORATIVE_MAX,														\r\n")
        .append(
            "        CA.CONSUMO_ANNUO_UF,																\r\n")
        .append(
            "        CA.NUMERO_MAX_ANIMALI_PER_HA,														\r\n")
        .append(
            "        CA.AGEA_COD");
    ;
    final String QUERY = sb.toString();
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_CATEGORIA_ANIMALE", idCategoriaAnimale,
          Types.NUMERIC);
      mapParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
      return queryForList(QUERY, mapParameterSource,
          ProduzioneCategoriaAnimaleDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_CATEGORIA_ANIMALE", idCategoriaAnimale),
              new LogParameter("ISTAT_COMUNE", istatComune)
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

  public List<ProduzioneCategoriaAnimaleDTO> getListProduzioni(
      long idProcedimentoOggetto,
      long idCategoriaAnimale,
      String istatComune)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = "WITH " +
        getWithTmpAllevamenti(true) + " \r\n" +
        "SELECT																				\r\n" +
        "        P.ID_PRODUZIONE,															\r\n" +
        "        P.PREZZO_MEDIO,																\r\n" +
        "        P.PREZZO_MAX,																\r\n" +
        "        P.QUANTITA_PRODOTTA_MIN,													\r\n" +
        "        P.QUANTITA_PRODOTTA_MAX,													\r\n" +
        "        P.QUANTITA_PRODOTTA_MEDIA,													\r\n" +
        "        P.DESCRIZIONE AS DESC_PRODUZIONE,											\r\n" +
        "        UM.ID_UNITA_MISURA,															\r\n" +
        "        UM.DESCRIZIONE AS DESC_UNITA_MISURA,										\r\n" +
        "        CA.PESO_VIVO_MIN,															\r\n" +
        "        CA.PESO_VIVO_MEDIO,															\r\n" +
        "        CA.PESO_VIVO_MAX,															\r\n" +
        "        CA.GIORNATE_LAVORATIVE_MIN,													\r\n" +
        "        CA.GIORNATE_LAVORATIVE_MEDIE,												\r\n" +
        "        CA.GIORNATE_LAVORATIVE_MAX,													\r\n" +
        "        CA.CONSUMO_ANNUO_UF,														\r\n" +
        "        CA.NUMERO_MAX_ANIMALI_PER_HA,												\r\n" +
        "        CA.AGEA_COD,																\r\n" +
        "        A.QUANTITA																	\r\n" +
        "	FROM 																													\r\n" +
        "        TMP_ALLEVAMENTI A,														\r\n" +
        "        IUF_D_PRODUZIONE P,														\r\n" +
        "        IUF_D_UNITA_MISURA UM,														\r\n" +
        "        IUF_D_CATEGORIA_ANIMALE CA													\r\n" +
        "	WHERE																															\r\n"
        +
        "        A.ISTAT_COMUNE = :ISTAT_COMUNE													\r\n" +
        "		AND A.ID_CATEGORIA_ANIMALE = CA.EXT_ID_CATEGORIA_ANIMALE						\r\n"
        +
        "		AND CA.EXT_ID_CATEGORIA_ANIMALE = P.EXT_ID_CATEGORIA_ANIMALE					\r\n"
        +
        "        AND P.ID_UNITA_MISURA = UM.ID_UNITA_MISURA										\r\n"
        +
        "GROUP BY																				\r\n" +
        "        P.ID_PRODUZIONE,																\r\n" +
        "        P.PREZZO_MIN,																	\r\n" +
        "        P.DESCRIZIONE,																	\r\n" +
        "        P.PREZZO_MEDIO,																\r\n" +
        "        P.PREZZO_MAX,																	\r\n" +
        "        P.QUANTITA_PRODOTTA_MIN,														\r\n" +
        "        P.QUANTITA_PRODOTTA_MAX,														\r\n" +
        "        P.QUANTITA_PRODOTTA_MEDIA,														\r\n" +
        "        UM.ID_UNITA_MISURA,															\r\n" +
        "        UM.DESCRIZIONE,																\r\n" +
        "        CA.PESO_VIVO_MIN,																\r\n" +
        "        CA.PESO_VIVO_MEDIO,															\r\n" +
        "        CA.PESO_VIVO_MAX,																\r\n" +
        "        CA.GIORNATE_LAVORATIVE_MIN,													\r\n" +
        "        CA.GIORNATE_LAVORATIVE_MEDIE,													\r\n" +
        "        CA.GIORNATE_LAVORATIVE_MAX,													\r\n" +
        "        CA.CONSUMO_ANNUO_UF,															\r\n" +
        "        CA.NUMERO_MAX_ANIMALI_PER_HA,													\r\n" +
        "        CA.AGEA_COD, 																	\r\n" +
        "        A.QUANTITA ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_CATEGORIA_ANIMALE", idCategoriaAnimale,
          Types.NUMERIC);
      mapParameterSource.addValue("ISTAT_COMUNE", istatComune, Types.VARCHAR);
      return queryForList(QUERY, mapParameterSource,
          ProduzioneCategoriaAnimaleDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_CATEGORIA_ANIMALE", idCategoriaAnimale),
              new LogParameter("ISTAT_COMUNE", istatComune)
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

  public long inserisciProduzioneZootecnica(long idProcedimentoOggetto,
      AllevamentiDTO produzioneZootecnica, long idUtenteLogin)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_PRODUZIONE_ZOOTECNICA						\r\n"
        +
        "    (   ID_PRODUZIONE_ZOOTECNICA,								\r\n" +
        "        ID_PROCEDIMENTO_OGGETTO,								\r\n" +
        "        EXT_ID_CATEGORIA_ANIMALE,								\r\n" +
        "        EXT_ISTAT_COMUNE,										\r\n" +
        "        PESO_VIVO_MEDIO,										\r\n" +
        "        GIORNATE_LAVORATIVE_MEDIE,								\r\n" +
        "        NOTE,													\r\n" +
        "        DATA_ULTIMO_AGGIORNAMENTO,								\r\n" +
        "        EXT_ID_UTENTE_AGGIORNAMENTO							\r\n" +
        "    )															\r\n" +
        "VALUES															\r\n" +
        "    (   														\r\n" +
        "        :ID_PRODUZIONE_ZOOTECNICA,								\r\n" +
        "        :ID_PROCEDIMENTO_OGGETTO,								\r\n" +
        "        :EXT_ID_CATEGORIA_ANIMALE,								\r\n" +
        "        :EXT_ISTAT_COMUNE,										\r\n" +
        "        :PESO_VIVO_MEDIO,										\r\n" +
        "        :GIORNATE_LAVORATIVE_MEDIE,							\r\n" +
        "        :NOTE,													\r\n" +
        "        SYSDATE,												\r\n" +
        "        :EXT_ID_UTENTE_AGGIORNAMENTO							\r\n" +
        "    )";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiProduzioneZootecnica = 0;
    try
    {
      seqIuffiProduzioneZootecnica = getNextSequenceValue(
          "SEQ_IUF_T_PRODUZIONE_ZOOTECN");
      mapParameterSource.addValue("ID_PRODUZIONE_ZOOTECNICA",
          seqIuffiProduzioneZootecnica, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_CATEGORIA_ANIMALE",
          produzioneZootecnica.getIdCategoriaAnimale(), Types.NUMERIC);
      mapParameterSource.addValue("EXT_ISTAT_COMUNE",
          produzioneZootecnica.getIstatComune(), Types.VARCHAR);
      mapParameterSource.addValue("PESO_VIVO_MEDIO",
          produzioneZootecnica.getPesoVivoMedio(), Types.NUMERIC);
      mapParameterSource.addValue("GIORNATE_LAVORATIVE_MEDIE",
          produzioneZootecnica.getGiornateLavorativeMedie(), Types.NUMERIC);
      mapParameterSource.addValue("NOTE", produzioneZootecnica.getNote(),
          Types.CLOB);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PRODUZIONE_ZOOTECNICA",
                  seqIuffiProduzioneZootecnica),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("EXT_ID_CATEGORIA_ANIMALE",
                  produzioneZootecnica.getIdCategoriaAnimale()),
              new LogParameter("PESO_VIVO_MEDIO",
                  produzioneZootecnica.getPesoVivoMedio()),
              new LogParameter("GIORNATE_LAVORATIVE_MEDIE",
                  produzioneZootecnica.getGiornateLavorativeMedie()),
              new LogParameter("NOTE", produzioneZootecnica.getNote()),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin)
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
    return seqIuffiProduzioneZootecnica;
  }

  public void modificaProduzioneZootecnica(
      long idProcedimentoOggetto,
      AllevamentiDTO produzioneZootecnica,
      long idUtenteLogin)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_PRODUZIONE_ZOOTECNICA													\r\n"
        +
        "SET 																					\r\n" +
        "    PESO_VIVO_MEDIO = :PESO_VIVO_MEDIO,												\r\n" +
        "    GIORNATE_LAVORATIVE_MEDIE = :GIORNATE_LAVORATIVE_MEDIE,							\r\n"
        +
        "    DATA_ULTIMO_AGGIORNAMENTO = SYSDATE,												\r\n" +
        "    EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,												\r\n"
        +
        "    NOTE = :NOTE																		\r\n" +
        "WHERE 																					\r\n" +
        "    	 ID_PRODUZIONE_ZOOTECNICA = :ID_PRODUZIONE_ZOOTECNICA							\r\n"
        +
        "    AND ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO							\r\n"
        +
        "    AND EXT_ISTAT_COMUNE = :EXT_ISTAT_COMUNE											\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PRODUZIONE_ZOOTECNICA",
          produzioneZootecnica.getIdProduzioneZootecnica(), Types.NUMERIC);
      mapParameterSource.addValue("EXT_ISTAT_COMUNE",
          produzioneZootecnica.getIstatComune(), Types.VARCHAR);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("PESO_VIVO_MEDIO",
          produzioneZootecnica.getPesoVivoMedio(), Types.NUMERIC);
      mapParameterSource.addValue("GIORNATE_LAVORATIVE_MEDIE",
          produzioneZootecnica.getGiornateLavorativeMedie(), Types.NUMERIC);
      mapParameterSource.addValue("NOTE", produzioneZootecnica.getNote(),
          Types.CLOB);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PRODUZIONE_ZOOTECNICA",
                  produzioneZootecnica.getIdProduzioneZootecnica()),
              new LogParameter("EXT_ISTAT_COMUNE",
                  produzioneZootecnica.getIstatComune()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("PESO_VIVO_MEDIO",
                  produzioneZootecnica.getPesoVivoMedio()),
              new LogParameter("GIORNATE_LAVORATIVE_MEDIE",
                  produzioneZootecnica.getGiornateLavorativeMedie()),
              new LogParameter("NOTE", produzioneZootecnica.getNote()),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin)
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

  public long inserisciProduzioneVendibile(long idProcedimentoOggetto,
      ProduzioneCategoriaAnimaleDTO produzione, long idProduzioneZootecnica)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_PRODUZIONE_VENDIBILE								\r\n"
        +
        " (																		\r\n" +
        "     ID_PRODUZIONE_VENDIBILE,											\r\n" +
        "     ID_PRODUZIONE_ZOOTECNICA,											\r\n" +
        "     ID_PRODUZIONE,													\r\n" +
        "     NUMERO_CAPI,														\r\n" +
        "     QUANTITA_PRODOTTA,												\r\n" +
        "     QUANTITA_REIMPIEGATA,												\r\n" +
        "     PREZZO															\r\n" +
        " )																		\r\n" +
        "VALUES																	\r\n" +
        " (																		\r\n" +
        "     :ID_PRODUZIONE_VENDIBILE,											\r\n" +
        "     :ID_PRODUZIONE_ZOOTECNICA,										\r\n" +
        "     :ID_PRODUZIONE,													\r\n" +
        "     :NUMERO_CAPI,														\r\n" +
        "     :QUANTITA_PRODOTTA,												\r\n" +
        "     :QUANTITA_REIMPIEGATA,											\r\n" +
        "     :PREZZO															\r\n" +
        " )";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTProduzioneVendibile = 0;
    try
    {
      seqIuffiTProduzioneVendibile = getNextSequenceValue(
          "SEQ_IUF_T_PRODUZIONE_VENDIBI");
      mapParameterSource.addValue("ID_PRODUZIONE_VENDIBILE",
          seqIuffiTProduzioneVendibile, Types.NUMERIC);
      mapParameterSource.addValue("ID_PRODUZIONE_ZOOTECNICA",
          idProduzioneZootecnica, Types.NUMERIC);
      mapParameterSource.addValue("ID_PRODUZIONE", produzione.getIdProduzione(),
          Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_CAPI", produzione.getNumeroCapi(),
          Types.NUMERIC);
      mapParameterSource.addValue("QUANTITA_PRODOTTA",
          produzione.getQuantitaProdotta(), Types.NUMERIC);
      mapParameterSource.addValue("QUANTITA_REIMPIEGATA",
          produzione.getQuantitaReimpiegata(), Types.NUMERIC);
      mapParameterSource.addValue("PREZZO", produzione.getPrezzo(),
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PRODUZIONE_VENDIBILE",
                  seqIuffiTProduzioneVendibile),
              new LogParameter("ID_PRODUZIONE_ZOOTECNICA",
                  idProduzioneZootecnica),
              new LogParameter("ID_PRODUZIONE", produzione.getIdProduzione()),
              new LogParameter("NUMERO_CAPI", produzione.getNumeroCapi()),
              new LogParameter("QUANTITA_PRODOTTA",
                  produzione.getQuantitaProdotta()),
              new LogParameter("QUANTITA_REIMPIEGATA",
                  produzione.getQuantitaReimpiegata()),
              new LogParameter("PREZZO", produzione.getPrezzo())
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
    return seqIuffiTProduzioneVendibile;
  }

  public void eliminaProduzioniVendibili(long idProduzioneZootecnica)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = " DELETE FROM IUF_T_PRODUZIONE_VENDIBILE							\r\n"
        +
        " WHERE ID_PRODUZIONE_ZOOTECNICA = :ID_PRODUZIONE_ZOOTECNICA		\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PRODUZIONE_ZOOTECNICA",
          idProduzioneZootecnica, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PRODUZIONE_ZOOTECNICA",
                  idProduzioneZootecnica)
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

  public BigDecimal getPlvZootecnicaUfProdotte(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT  																														\r\n"
        +
        "    NVL(SUM(U.UF_PRODOTTE * NVL(SC.PRODUZIONE_HA,0)  * CU.SUPERFICIE_UTILIZZATA - NVL(SC.UF_REIMPIEGATE,0)),0)  AS UF_PRODOTTE_NETTE	\r\n"
        +
        "FROM 																															\r\n"
        +
        "    IUF_T_SUPERFICIE_COLTURA SC,																								\r\n"
        +
        "    IUF_D_UTILIZZO U,																										\r\n"
        +
        "    SMRGAA_V_CONDUZIONE_UTILIZZO CU,																							\r\n"
        +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO																							\r\n"
        +
        "WHERE 																															\r\n"
        +
        "    PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO																		\r\n"
        +
        "    AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA													\r\n"
        +
        "    AND CU.ID_UTILIZZO = SC.EXT_ID_UTILIZZO																					\r\n"
        +
        "    AND SC.EXT_ID_UTILIZZO = U.EXT_ID_UTILIZZO																					\r\n"
        +
        "GROUP BY																														\r\n"
        +
        "    CU.ID_UTILIZZO																												\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForBigDecimal(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public BigDecimal getPlvZootecnicaUfNecessarie(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 																	\r\n" +
        "    NVL(SUM(CA.CONSUMO_ANNUO_UF * A.QUANTITA),0) AS UF_NECESSARIE  				\r\n"
        +
        "FROM 																		\r\n" +
        "    SMRGAA_V_ALLEVAMENTI A,												\r\n" +
        "    IUF_D_CATEGORIA_ANIMALE CA,											\r\n" +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO										\r\n" +
        "WHERE 																		\r\n" +
        "    PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO					\r\n"
        +
        "    AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA	\r\n"
        +
        "    AND A.ID_CATEGORIA_ANIMALE = CA.EXT_ID_CATEGORIA_ANIMALE				\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForBigDecimal(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  private String WITH_UBA_SAU = "WITH TMP_UBA AS (																				\r\n"
      +
      "    SELECT																						\r\n" +
      "       SUM(A.QUANTITA * DA.COEFFICIENTE_UBA) AS UBA_TOTALE										\r\n"
      +
      "    FROM 																						\r\n" +
      "        SMRGAA_V_ALLEVAMENTI A,																\r\n" +
      "        IUF_D_CATEGORIA_ANIMALE CA,															\r\n"
      +
      "        IUF_T_PROCEDIMENTO_OGGETTO PO,														\r\n"
      +
      "        SMRGAA_V_DECO_ALLEVAMENTI DA															\r\n" +
      "    WHERE 																						\r\n" +
      "        PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO									\r\n"
      +
      "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA					\r\n"
      +
      "        AND A.ID_CATEGORIA_ANIMALE = CA.EXT_ID_CATEGORIA_ANIMALE								\r\n"
      +
      "        AND A.ID_CATEGORIA_ANIMALE = DA.ID_CATEGORIA_ANIMALE									\r\n"
      +
      "), TMP_SAU AS (																				\r\n" +
      "    SELECT																						\r\n" +
      "        SUM(CU.SUPERFICIE_UTILIZZATA) AS SAU_TOTALE											\r\n"
      +
      "    FROM 																						\r\n" +
      "        IUF_T_PROCEDIMENTO_OGGETTO PO,														\r\n"
      +
      "        SMRGAA_V_CONDUZIONE_UTILIZZO CU														\r\n"
      +
      "    WHERE 																						\r\n" +
      "        PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO									\r\n"
      +
      "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA				\r\n"
      +
      "        AND CU.FLAG_SAU = 'S'																	\r\n" +
      ")																								\r\n";

  public BigDecimal getPlvZootecnicaRapportoUbaSau(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = WITH_UBA_SAU +
        "SELECT																							\r\n" +
        "    UBA_TOTALE / SAU_TOTALE AS RAPPORTO_UBA_SAU 												\r\n"
        +
        "FROM																							\r\n" +
        "    TMP_UBA,																					\r\n" +
        "    TMP_SAU";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForBigDecimal(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public BigDecimal getPlvZootecnicaUba(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = WITH_UBA_SAU +
        "SELECT																							\r\n" +
        "    NVL(UBA_TOTALE,0)																					\r\n" +
        "FROM																							\r\n" +
        "    TMP_UBA																					\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForBigDecimal(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public BigDecimal getPlvZootecnicaSau(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = WITH_UBA_SAU +
        "SELECT																							\r\n" +
        "    NVL(SAU_TOTALE,0)																					\r\n" +
        "FROM																							\r\n" +
        "    TMP_SAU";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForBigDecimal(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public List<AllevamentiDTO> getListAllevamenti(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return getListRiepilogoAllevamenti(idProcedimentoOggetto, null, null,
        false);
  }

  // Esempio di query con doppia outer join - in cui lo stessa dichiarazione di
  // consistenza potrebbe essere condivisa da due procedimento_oggetto diversi
  public List<AllevamentiDettaglioPlvDTO> getListPlvZootecnicaDettaglioAllevamenti(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "WITH PRODUZIONE_ZOOTECNICA AS (																								\r\n"
        +
        "    SELECT *																													\r\n"
        +
        "    FROM IUF_T_PRODUZIONE_ZOOTECNICA																							\r\n"
        +
        "    WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO																	\r\n"
        +
        ")																																\r\n"
        +
        "SELECT 																														\r\n"
        +
        "    A.CODICE_AZIENDA_ZOOTECNICA,  																								\r\n"
        +
        "    A.DESCRIZIONE_SPECIE_ANIMALE, 																								\r\n"
        +
        "    A.DESCRIZIONE_CATEGORIA_ANIMALE, 																							\r\n"
        +
        "    PV.ID_PRODUZIONE, 																											\r\n"
        +
        "    P.DESCRIZIONE AS DESC_PRODUZIONE,																							\r\n"
        +
        "    PV.NUMERO_CAPI,																											\r\n"
        +
        "    PV.QUANTITA_PRODOTTA,																										\r\n"
        +
        "    PV.QUANTITA_PRODOTTA * PV.NUMERO_CAPI AS PROD_LORDA,																		\r\n"
        +
        "    PV.QUANTITA_REIMPIEGATA,																									\r\n"
        +
        "    PV.QUANTITA_PRODOTTA * PV.NUMERO_CAPI - NVL(PV.QUANTITA_REIMPIEGATA,0) AS PROD_NETTA,												\r\n"
        +
        "    UM.DESCRIZIONE AS DESC_UNITA_MISURA,																						\r\n"
        +
        "    PV.PREZZO,																													\r\n"
        +
        "    (PV.NUMERO_CAPI * PV.QUANTITA_PRODOTTA - NVL(PV.QUANTITA_REIMPIEGATA,0)) * PV.PREZZO AS PLV										\r\n"
        +
        "FROM																															\r\n"
        +
        "     IUF_T_PROCEDIMENTO_OGGETTO PO,																							\r\n"
        +
        "     IUF_T_PRODUZIONE_VENDIBILE PV,																							\r\n"
        +
        "     PRODUZIONE_ZOOTECNICA PZ,																									\r\n"
        +
        "     SMRGAA_V_ALLEVAMENTI A,																									\r\n"
        +
        "     IUF_D_PRODUZIONE P,																										\r\n"
        +
        "     IUF_D_UNITA_MISURA UM																									\r\n"
        +
        "WHERE 																															\r\n"
        +
        "        PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO																	\r\n"
        +
        "    AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA														\r\n"
        +
        "    AND A.ID_CATEGORIA_ANIMALE = PZ.EXT_ID_CATEGORIA_ANIMALE																	\r\n"
        +
        "    AND PZ.ID_PRODUZIONE_ZOOTECNICA = PV.ID_PRODUZIONE_ZOOTECNICA																\r\n"
        +
        "    AND P.ID_UNITA_MISURA = UM.ID_UNITA_MISURA																					\r\n"
        +
        "    AND PV.ID_PRODUZIONE = P.ID_PRODUZIONE																						\r\n"
        +
        "GROUP BY																														\r\n"
        +
        "    A.CODICE_AZIENDA_ZOOTECNICA,																								\r\n"
        +
        "    A.DESCRIZIONE_SPECIE_ANIMALE,																								\r\n"
        +
        "    A.DESCRIZIONE_CATEGORIA_ANIMALE,																							\r\n"
        +
        "    PV.ID_PRODUZIONE,																											\r\n"
        +
        "    P.DESCRIZIONE,																												\r\n"
        +
        "    PV.NUMERO_CAPI,																											\r\n"
        +
        "    PV.QUANTITA_PRODOTTA,																										\r\n"
        +
        "    PV.QUANTITA_PRODOTTA * PV.NUMERO_CAPI,																						\r\n"
        +
        "    PV.QUANTITA_REIMPIEGATA,																									\r\n"
        +
        "    PV.QUANTITA_PRODOTTA * PV.NUMERO_CAPI - NVL(PV.QUANTITA_REIMPIEGATA,0),															\r\n"
        +
        "    UM.DESCRIZIONE,																											\r\n"
        +
        "    PV.PREZZO,																													\r\n"
        +
        "    (PV.QUANTITA_PRODOTTA * PV.NUMERO_CAPI - NVL(PV.QUANTITA_REIMPIEGATA,0)) * PV.PREZZO 												\r\n"
        +
        "ORDER BY 																														\r\n"
        +
        "	 A.DESCRIZIONE_SPECIE_ANIMALE, 																								\r\n"
        +
        "	 A.DESCRIZIONE_CATEGORIA_ANIMALE, 																							\r\n"
        +
        "	 P.DESCRIZIONE 																												\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          AllevamentiDettaglioPlvDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public List<AllevamentiDTO> getListAllevamentiSingoli(
      long idProcedimentoOggetto, long[] arrayIdAllevamento,
      boolean onlyNonDanneggiati) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionIdAllevamento = " ";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (arrayIdAllevamento != null)
    {
      inConditionIdAllevamento = getInCondition("A.ID_ALLEVAMENTO",
          arrayIdAllevamento);
    }

    String QUERY = "SELECT																																	\r\n"
        +
        "    A.ID_ALLEVAMENTO,																													\r\n"
        +
        "    A.DESCRIZIONE_CATEGORIA_ANIMALE,																									\r\n"
        +
        "    A.DESCRIZIONE_SPECIE_ANIMALE,																										\r\n"
        +
        "    A.INDIRIZZO,																														\r\n"
        +
        "    A.DENOMINAZIONE_ALLEVAMENTO,																										\r\n"
        +
        "    A.QUANTITA,																														\r\n"
        +
        "    DA.DESCRIZIONE_COMUNE,																												\r\n"
        +
        "    DA.SIGLA_PROVINCIA																													\r\n"
        +
        "FROM 																																	\r\n"
        +
        "    SMRGAA_V_ALLEVAMENTI A,																											\r\n"
        +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,																									\r\n"
        +
        "    SMRGAA_V_DATI_AMMINISTRATIVI DA																									\r\n"
        +
        "WHERE 																																	\r\n"
        +
        "        PO.ID_PROCEDIMENTO_OGGETTO  =:ID_PROCEDIMENTO_OGGETTO																			\r\n"
        +
        "    AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = A.ID_DICHIARAZIONE_CONSISTENZA																\r\n"
        +
        "    AND A.ISTAT_COMUNE = DA.ISTAT_COMUNE 																								\r\n"
        +
        inConditionIdAllevamento;
    if (onlyNonDanneggiati)
    {
      QUERY = QUERY +
          "    AND A.ID_ALLEVAMENTO NOT IN																										\r\n"
          +
          "        (																																\r\n"
          +
          "            SELECT 																													\r\n"
          +
          "                DAT.EXT_ID_ENTITA_DANNEGGIATA																							\r\n"
          +
          "            FROM 																														\r\n"
          +
          "                IUF_T_DANNO_ATM DAT																									\r\n"
          +
          "            WHERE 																														\r\n"
          +
          "                DAT.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO																	\r\n"
          +
          getInCondition("DAT.ID_DANNO",
              QuadroIuffiDAO
                  .getListDanniEquivalenti(IuffiConstants.DANNI.ALLEVAMENTO))
          + " 			\r\n" +
          "        ) 																																\r\n";
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, AllevamentiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public List<AllevamentiDTO> getListAllevamentiSingoliNonDanneggiati(
      long idProcedimentoOggetto, long[] arrayIdAllevamento)
      throws InternalUnexpectedException
  {
    return getListAllevamentiSingoli(idProcedimentoOggetto, arrayIdAllevamento,
        true);
  }

  public List<DecodificaDTO<Long>> getListDanniDecodificaDTO(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT																	\r\n" +
        "    DA.ID_DANNO_ATM AS CODICE,												\r\n" +
        "    DA.ID_DANNO_ATM AS ID,											\r\n" +
        "    DA.DESCRIZIONE AS DESCRIZIONE										\r\n" +
        "FROM																	\r\n" +
        "    IUF_T_DANNO_ATM DA												\r\n" +
        "WHERE 																	\r\n" +
        "    DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  private String getListProgressivoInterventi()
  {
    String query = "        SELECT 																\r\n" +
        "            PROGRESSIVO														\r\n" +
        "        FROM 																	\r\n" +
        "            IUF_R_DANNO_ATM_INTERVENTO										\r\n" +
        "        WHERE 																	\r\n" +
        "            ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO					\r\n";
    return query;
  }

  public long getNInterventiAssociatiDanni(long idProcedimentoOggetto,
      long[] arrayIdDannoAtm)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 																		\r\n" +
        "    COUNT(*) AS N_INTERVENTI_DANNI												\r\n" +
        "FROM 																			\r\n" +
        "    IUF_T_DANNO_ATM 															\r\n" +
        "WHERE 																			\r\n" +
        "        ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO						\r\n"
        +
        getInCondition("ID_DANNO_ATM", arrayIdDannoAtm) +
        "    AND PROGRESSIVO IN (   													\r\n" +
        getListProgressivoInterventi() +
        "    )";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_DANNO_ATM", arrayIdDannoAtm)
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

  public long getNInterventiAssociatiDanniScorte(long idProcedimentoOggetto,
      List<Long> listIdScortaMagazzino)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 																		  					\r\n"
        +
        "	COUNT(*) AS N_INTER_DANNI_SCORTE																\r\n"
        +
        "FROM 																			  					\r\n" +
        "	IUF_T_DANNO_ATM DA,																			\r\n" +
        "   IUF_T_SCORTA_MAGAZZINO SM																		\r\n"
        +
        "WHERE 																			  					\r\n" +
        "        DA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO						  				\r\n"
        +
        "    AND DA.EXT_ID_ENTITA_DANNEGGIATA = SM.ID_SCORTA_MAGAZZINO										\r\n"
        +
        getInCondition("SM.ID_SCORTA_MAGAZZINO", listIdScortaMagazzino)
        + "	\r\n" +
        getInCondition("DA.ID_DANNO",
            QuadroIuffiDAO.getListDanniEquivalenti(IuffiConstants.DANNI.SCORTA))
        + "\r\n" +
        "	AND DA.PROGRESSIVO IN (   													  					\r\n"
        +
        getListProgressivoInterventi() +
        "	)";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_SCORTA_MAGAZZINO", listIdScortaMagazzino)
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

  public ColtureAziendaliDTO getRiepilogoColtureAziendali(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
	String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
      logger.debug(THIS_METHOD + " BEGIN.");
	
    final String QUERY = " WITH TMP_PLV_ELENCO AS (												\r\n"
        +
    	"SELECT	 																\r\n" + 
    	"        SC.EXT_ISTAT_COMUNE,														\r\n" + 
    	"        CU.ID_UTILIZZO,														\r\n" + 
        "        SUM(SC.SUPERFICIE_UTILIZZATA) AS SUPERFICIE_UTILIZZATA,									\r\n"
        +
    	"        SC.ID_SUPERFICIE_COLTURA,													\r\n" + 
    	"        SC.PRODUZIONE_HA_DANNO,													\r\n" + 
    	"        SC.PREZZO_DANNEGGIATO,														\r\n" + 
    	"        SC.PERCENTUALE_DANNO,														\r\n" + 
    	"        SC.PREZZO,															\r\n" + 
    	"        SC.PRODUZIONE_HA														\r\n" + 
    	"FROM				 													\r\n" + 
    	"	 IUF_T_SUPERFICIE_COLTURA SC,													\r\n" + 
    	"	 IUF_T_PROCEDIMENTO_OGGETTO PO,												\r\n" + 
    	"	 SMRGAA_V_CONDUZIONE_UTILIZZO CU												\r\n" + 
    	"WHERE				 													\r\n" + 
        " 	 PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO										\r\n"
        +
        "	 AND PO.ID_PROCEDIMENTO_OGGETTO = SC.ID_PROCEDIMENTO_OGGETTO									\r\n"
        +
        "	 AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA			 				\r\n"
        +
    	"	 AND CU.ID_UTILIZZO = SC.EXT_ID_UTILIZZO											\r\n" + 
    	"	 AND CU.COMUNE = SC.EXT_ISTAT_COMUNE												\r\n" + 
    	"GROUP BY 																\r\n" + 
    	"        SC.EXT_ISTAT_COMUNE,														\r\n" + 
    	"        CU.ID_UTILIZZO,														\r\n" + 
    	"        SC.ID_SUPERFICIE_COLTURA,													\r\n" + 
    	"        SC.PRODUZIONE_HA_DANNO,													\r\n" + 
    	"        SC.PREZZO_DANNEGGIATO,														\r\n" + 
    	"        SC.PERCENTUALE_DANNO,														\r\n" + 
    	"        SC.PREZZO,															\r\n" + 
    	"        SC.PRODUZIONE_HA    														\r\n" + 
    	" )																	\r\n" + 
    	"SELECT 																\r\n" + 
        "    	 SUM(PE.SUPERFICIE_UTILIZZATA) AS SUPERFICIE_UTILIZZATA,									\r\n"
        +
        "    	 SUM(PE.PRODUZIONE_HA * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO) AS TOTALE_PLV_ORDINARIA,						\r\n"
        +
        "    	 SUM(DECODE(PE.PRODUZIONE_HA_DANNO, NULL,											\r\n"
        +
        "       	   (PE.PRODUZIONE_HA * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO),								\r\n"
        +
        "       	   (PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO_DANNEGGIATO))) AS TOTALE_PLV_EFFETTIVA		\r\n"
        +
    	"FROM TMP_PLV_ELENCO PE  ";
	MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource,
          ColtureAziendaliDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_PROCEDIMENTO_OGGETTO",
              idProcedimentoOggetto) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
	    logInternalUnexpectedException(e, THIS_METHOD);
	    throw e;
	}
    finally
    {
      if (logger.isDebugEnabled())
        logger.debug(THIS_METHOD + " END.");
    }
  }
  
  // public Integer getCountBandoUtilizzoDanno(long idBando, long extIdUtilizzo)
  // throws InternalUnexpectedException {
//	String THIS_METHOD = "[" + THIS_CLASS + "::getCountBandoUtilizzoDanno]";
//	if (logger.isDebugEnabled()) logger.debug(THIS_METHOD + " BEGIN.");
//	final String QUERY = " SELECT                                       \n"
//		+ "     COUNT(*)                                                \n"
//		+ " FROM                                                        \n"
//		+ "     IUF_R_BANDO_UTILIZZO_DANNO RBUD                       \n"
//		+ " WHERE                                                       \n"
//		+ "     RBUD.ID_BANDO = :ID_BANDO                               \n"
//		+ "     AND RBUD.EXT_ID_UTILIZZO = :EXT_ID_UTILIZZO            \n";
//	MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
//	try {
//	    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
  // mapParameterSource.addValue("EXT_ID_UTILIZZO", extIdUtilizzo,
  // Types.NUMERIC);
//	    return namedParameterJdbcTemplate.queryForInt(QUERY, mapParameterSource);
//	} catch (Throwable t) {
  // InternalUnexpectedException e = new InternalUnexpectedException(t, new
  // LogParameter[] {}, new LogVariable[] {}, QUERY, new
  // MapSqlParameterSource());
//	    logInternalUnexpectedException(e, THIS_METHOD);
//	    throw e;
//	} finally {
//	    if (logger.isDebugEnabled()) logger.debug(THIS_METHOD + " END.");
//	}
//    }
    
  public UtilizzoReseDTO getUtilizzoRese(ColtureAziendaliDettaglioDTO coltura)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
	String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
      logger.debug(THIS_METHOD + " BEGIN.");
	final String QUERY = " SELECT " + 
		"	A.EXT_ID_UTILIZZO, " + 
		"	A.RESA_MIN, " + 
		"	A.RESA_MAX, " + 
		"	A.PREZZO_MIN, " + 
		"	A.PREZZO_MAX, " + 
		"	A.GIORNATE_LAVORATE_MIN, " + 
		"	A.GIORNATE_LAVORATE_MAX " + 
		"FROM" + 
		"	IUF_D_UTILIZZO_RESE A " + 
		"WHERE" + 
		"	A.EXT_ID_UTILIZZO = :EXT_ID_UTILIZZO " + 
        "	AND NVL(A.CODICE_UTILIZZO, '-') = NVL(:CODICE_UTILIZZO, NVL(A.CODICE_UTILIZZO, '-')) "
        +
        "	AND NVL(A.CODICE_DESTINAZIONE, '-') = NVL(:CODICE_DESTINAZIONE, NVL(A.CODICE_DESTINAZIONE, '-')) "
        +
        "	AND NVL(A.CODICE_DETTAGLIO_USO, '-') = NVL(:CODICE_DETTAGLIO_USO, NVL(A.CODICE_DETTAGLIO_USO, '-')) "
        +
        "	AND NVL(A.CODICE_QUALITA_USO, '-') = NVL(:CODICE_QUALITA_USO, NVL(A.CODICE_QUALITA_USO, '-')) "
        +
        "	AND NVL(A.CODICE_VARIETA, '-') = NVL(:CODICE_VARIETA, NVL(A.CODICE_VARIETA, '-')) "
        +
		"	AND A.DATA_FINE IS NULL ";
	MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("EXT_ID_UTILIZZO", coltura.getExtIdUtilizzo(),
          Types.NUMERIC);
      mapParameterSource.addValue("CODICE_UTILIZZO",
          coltura.getCodiceUtilizzo(), Types.VARCHAR);
      mapParameterSource.addValue("CODICE_DESTINAZIONE",
          coltura.getCodiceDestinazione(), Types.VARCHAR);
      mapParameterSource.addValue("CODICE_DETTAGLIO_USO",
          coltura.getCodiceDettaglioUso(), Types.VARCHAR);
      mapParameterSource.addValue("CODICE_QUALITA_USO",
          coltura.getCodiceQualitaUso(), Types.VARCHAR);
      mapParameterSource.addValue("CODICE_VARIETA", coltura.getCodiceVarieta(),
          Types.VARCHAR);
      UtilizzoReseDTO res = queryForObject(QUERY, mapParameterSource,
          UtilizzoReseDTO.class);
      if (res == null)
        res = new UtilizzoReseDTO();
	    res.setExtIdUtilizzo(coltura.getExtIdUtilizzo());
      if (res.getResaMin() == null)
        res.setResaMin(new BigDecimal(0));
      if (res.getResaMax() == null)
        res.setResaMax(new BigDecimal(999999.99));
      if (res.getPrezzoMin() == null)
        res.setPrezzoMin(new BigDecimal(0));
      if (res.getPrezzoMax() == null)
        res.setPrezzoMax(new BigDecimal(999999.99));
      if (res.getGiornateLavorateMin() == null)
        res.setGiornateLavorateMin(new BigDecimal(0));
      if (res.getGiornateLavorateMax() == null)
        res.setGiornateLavorateMax(new BigDecimal(999999.9));
	    return res;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("EXT_ID_UTILIZZO", coltura.getExtIdUtilizzo()) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
	    logInternalUnexpectedException(e, THIS_METHOD);
	    throw e;
	}
    finally
    {
      if (logger.isDebugEnabled())
        logger.debug(THIS_METHOD + " END.");
    }
  }
  
  public List<ColtureAziendaliDettaglioDTO> getListColtureAziendali(
      long idProcedimentoOggetto, long[] arrayIdSuperficieColtura)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
	}.getClass().getEnclosingMethod().getName();
	String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
	    logger.debug(THIS_METHOD + " BEGIN.");
	}
	String inConditionArrayIdSuperficieColtura = " ";
    if (arrayIdSuperficieColtura != null)
    {
      inConditionArrayIdSuperficieColtura = getInCondition(
          "SC.ID_SUPERFICIE_COLTURA", arrayIdSuperficieColtura);
	}
	final String QUERY = "WITH TMP_PLV_ELENCO AS (																																		\r\n"
		+ "SELECT	 																																					\r\n"
		+ "	SC.EXT_ISTAT_COMUNE,																																	\r\n"
		+ "	SC.EXT_ID_UTILIZZO,		                                                  \r\n"
		+ "     P.ID_BANDO,                                                                  "
		+ "     DA.SIGLA_PROVINCIA,																																	\r\n"
		+ "	DA.DESCRIZIONE_PROVINCIA AS DESC_PROVINCIA,													 														\r\n"
		+ "	DA.DESCRIZIONE_COMUNE AS DESC_COMUNE,																													\r\n"
		+ "	CU.ID_UTILIZZO,																																		\r\n"
		+ "	DECODE(COLTURA_SECONDARIA,'S',																   															\r\n"
		+ "	    NVL(DESC_TIPO_UTILIZZO_SECONDARIO, DESC_TIPO_UTILIZZO) || '(secondario)' || ' [' || NVL(COD_TIPO_UTILIZZO_SECONDARIO,COD_TIPO_UTILIZZO) || '] ',	\r\n"
		+ "	    DESC_TIPO_UTILIZZO || ' [' || COD_TIPO_UTILIZZO || ']'						   																\r\n"
		+ "	    ) AS TIPO_UTILIZZO_DESCRIZIONE,	"
		+ "	(SELECT DISTINCT vm.CODICE_UTILIZZO FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_UTILIZZO = vm.CODICE_UTILIZZO AND vm.ANNO_FINE IS NULL) AS CODICE_UTILIZZO,   \r\n"
		+ "	(SELECT DISTINCT vm.CODICE_DESTINAZIONE FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_DESTINAZIONE = vm.CODICE_DESTINAZIONE AND vm.ANNO_FINE IS NULL) AS CODICE_DESTINAZIONE,   \r\n"
		+ "	(SELECT DISTINCT vm.CODICE_DETTAGLIO_USO FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_DETTAGLIO_USO = vm.CODICE_DETTAGLIO_USO AND vm.ANNO_FINE IS NULL) AS CODICE_DETTAGLIO_USO,   \r\n"
		+ "	(SELECT DISTINCT vm.CODICE_QUALITA_USO FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_QUALITA_USO = vm.CODICE_QUALITA_USO AND vm.ANNO_FINE IS NULL) AS CODICE_QUALITA_USO,   \r\n"
		+ "	(SELECT DISTINCT vm.CODICE_VARIETA FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_VARIETA = vm.CODICE_VARIETA AND vm.ANNO_FINE IS NULL) AS CODICE_VARIETA,   																												\r\n"
		+ "	(SELECT DISTINCT vm.DESCRIZIONE_DESTINAZIONE || ' [' || vm.CODICE_DESTINAZIONE || ']' FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_DESTINAZIONE = vm.CODICE_DESTINAZIONE AND vm.ANNO_FINE IS NULL) AS DESTINAZIONE_DESCRIZIONE,   \r\n"
		+ "	(SELECT DISTINCT vm.DESCRIZIONE_DETTAGLIO_USO || ' [' || vm.CODICE_DETTAGLIO_USO || ']' FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_DETTAGLIO_USO = vm.CODICE_DETTAGLIO_USO AND vm.ANNO_FINE IS NULL) AS DETTAGLIO_USO_DESCRIZIONE,   \r\n"
		+ "	(SELECT DISTINCT vm.DESCRIZIONE_QUALITA_USO || ' [' || vm.CODICE_QUALITA_USO || ']' FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_QUALITA_USO = vm.CODICE_QUALITA_USO AND vm.ANNO_FINE IS NULL) AS QUALITA_USO_DESCRIZIONE,    \r\n"
		+ "	(SELECT DISTINCT vm.DESCRIZIONE_VARIETA || ' [' || vm.CODICE_VARIETA || ']' FROM SMRGAA_V_MATRICE vm WHERE SC.CODICE_VARIETA = vm.CODICE_VARIETA AND vm.ANNO_FINE IS NULL) AS VARIETA_DESCRIZIONE,   \r\n"
		+ "	SC.SUPERFICIE_UTILIZZATA,																																\r\n"
		+ "	SC.ID_SUPERFICIE_COLTURA,																																\r\n"
		+ "	SC.PRODUZIONE_TOTALE_DANNO,																															\r\n"
		+ "	SC.PREZZO_DANNEGGIATO,																																	\r\n"
		+ "	SC.NOTE,									       \r\n"
		+ "	SC.RECORD_MODIFICATO,																																	\r\n"
		+ "	SC.PREZZO,																																				\r\n"
		+ "	SC.PRODUZIONE_HA,																																		\r\n"
		+ "	SC.PRODUZIONE_HA_DANNO,"
		+ "	SC.GIORNATE_LAVORATE,							      "
		+ "	SC.IMPORTO_RIMBORSO,  "
		+"	SC.FLAG_DANNEGGIATO,"
		+ "	(SELECT COUNT(*) AS N_S FROM IUF_T_CONTROLLO_COLTURA CC1 WHERE CC1.ID_SUPERFICIE_COLTURA = SC.ID_SUPERFICIE_COLTURA AND CC1.BLOCCANTE = 'S') AS N_BLOCCANTE_S,																											\r\n"
		+ "	(SELECT COUNT(*) AS N_N FROM IUF_T_CONTROLLO_COLTURA CC2 WHERE CC2.ID_SUPERFICIE_COLTURA = SC.ID_SUPERFICIE_COLTURA AND CC2.BLOCCANTE = 'N') AS N_BLOCCANTE_N																											\r\n"
		+ "FROM																							 															\r\n"
		+ "	IUF_T_SUPERFICIE_COLTURA SC,																 															\r\n"
		+ "   	IUF_T_PROCEDIMENTO P,                                                                      "
		+ "	IUF_T_PROCEDIMENTO_OGGETTO PO,																	 													\r\n"
		+ "	SMRGAA_V_CONDUZIONE_UTILIZZO CU,															 															\r\n"
		+ "	SMRGAA_V_DATI_AMMINISTRATIVI DA												"
		+ "WHERE																							 															\r\n"
		+ "	PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO										 															\r\n"
		+ "	AND PO.ID_PROCEDIMENTO_OGGETTO = SC.ID_PROCEDIMENTO_OGGETTO						 	 																\r\n"
		+ "	AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA			 																\r\n"
		+ "	AND CU.ID_UTILIZZO = SC.EXT_ID_UTILIZZO										 																\r\n"
		+ "	AND CU.COMUNE = SC.EXT_ISTAT_COMUNE										 																\r\n"
		+ "	AND SC.EXT_ISTAT_COMUNE = DA.ISTAT_COMUNE																							\r\n"
		+ "	AND P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO"
		+ inConditionArrayIdSuperficieColtura
		+ ")																																								\r\n"
		+ "SELECT 																																						\r\n"
		+ "    PE.ID_SUPERFICIE_COLTURA,																																	\r\n"
		+ "    DECODE(N_BLOCCANTE_S, 0, DECODE(N_BLOCCANTE_N, 0, NULL, 'N'), 'S') AS BLOCCANTE,																								\r\n"
		+ "    PE.EXT_ISTAT_COMUNE,																																		\r\n"
		+ "    PE.EXT_ID_UTILIZZO,                                                                      \r\n"
		+ "    PE.ID_BANDO,                                                                      "
		+ "    PE.ID_UTILIZZO,																																			\r\n"
		+ "    PE.TIPO_UTILIZZO_DESCRIZIONE,		"
		+ "    PE.CODICE_UTILIZZO,\r\n" 
		+ "    PE.CODICE_DESTINAZIONE,\r\n"
		+ "    PE.CODICE_DETTAGLIO_USO,\r\n" 
		+ "    PE.CODICE_QUALITA_USO,\r\n"
		+ "    PE.CODICE_VARIETA,																																		\r\n"
		+ "    PE.DESTINAZIONE_DESCRIZIONE,                                                     \r\n"
		+ "    PE.DETTAGLIO_USO_DESCRIZIONE,                                                      \r\n"
		+ "    PE.QUALITA_USO_DESCRIZIONE,                                                      \r\n"
		+"     PE.FLAG_DANNEGGIATO,"
		+ "    PE.VARIETA_DESCRIZIONE,                                                          \r\n"
		+ "    PE.SIGLA_PROVINCIA,							"
		+ "    PE.IMPORTO_RIMBORSO,																											\r\n"
		+ "    PE.DESC_COMUNE,																																			\r\n"
		+ "    PE.DESC_COMUNE || ' (' || PE.SIGLA_PROVINCIA || ')' AS UBICAZIONE_TERRENO,																					\r\n"
		+ "    PE.SUPERFICIE_UTILIZZATA,																									\r\n"
		+ "    PE.RECORD_MODIFICATO,																																		\r\n"
		+ "    -- PLV ORDINARIA\r\n"
		+ "    PE.PRODUZIONE_HA, 																																			\r\n"
		+ "    TO_NUMBER(DECODE(PE.PRODUZIONE_HA, NULL,																													\r\n"
		+ "        NULL,																																					\r\n"
		+ "        ROUND(PE.PRODUZIONE_HA * SUPERFICIE_UTILIZZATA, 2))) AS PRODUZIONE_DICHIARATA, 																			\r\n"
		+ "    PE.PREZZO,																																					\r\n"
		+ "    TO_NUMBER(DECODE(PE.PRODUZIONE_HA, NULL,																													\r\n"
		+ "        NULL,																																					\r\n"
		+ "        ROUND(PE.PRODUZIONE_HA * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO, 2))) AS TOTALE_EURO_PLV_ORD,																	\r\n"
		+ "    PE.GIORNATE_LAVORATE AS GIORNATE_LAVORATE_HA,                                                                     \r\n"
		+ "      TO_NUMBER(DECODE(PE.GIORNATE_LAVORATE, NULL,																													\r\n"
		+ "          NULL,																																					\r\n"
		+ "          ROUND(PE.GIORNATE_LAVORATE * PE.SUPERFICIE_UTILIZZATA, 2))) AS GIORNATE_LAVORATE,  "
		+ "    PE.NOTE,                                                                    \r\n"
		+ "    --PLV EFFETTIVA																																			\r\n"
		+ "    PE.PRODUZIONE_HA_DANNO,"
		+ "    TO_NUMBER(DECODE(PE.PRODUZIONE_HA_DANNO, NULL,                                                         \r\n"
		+ "        NULL,                                                                         \r\n"
		+ "        ROUND(PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA, 2))) AS PRODUZIONE_TOTALE_DANNO,          "
		+ "    PE.PREZZO_DANNEGGIATO,																																		\r\n"
		+ "    TO_NUMBER(DECODE(PE.PRODUZIONE_HA_DANNO, NULL,																													\r\n"
		+ "        NULL,																																					\r\n"
		+ "        ROUND(PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO_DANNEGGIATO, 2))) AS TOTALE_EURO_PLV_EFF,"
		+ "    TO_NUMBER(DECODE(TO_NUMBER(DECODE(PE.PRODUZIONE_HA_DANNO, NULL, NULL, ROUND(PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO_DANNEGGIATO, 2))), NULL,																													\r\n"
		+ "        NULL,																																					\r\n"
		+ "        ROUND(TO_NUMBER(DECODE(PE.PRODUZIONE_HA_DANNO, NULL, NULL, ROUND(PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO_DANNEGGIATO, 2))) + NVL(PE.IMPORTO_RIMBORSO, 0), 2))) AS TOT_CON_RIMBORSI,    \r\n"
		+ "    --DANNO												\r\n"
		+ "    TO_NUMBER(DECODE(TO_NUMBER(DECODE(PE.PRODUZIONE_HA_DANNO, NULL, NULL, PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO)), NULL,																													\r\n"
		+ "        NULL,																																					\r\n"
		+ "        ROUND(TO_NUMBER(DECODE(PE.PRODUZIONE_HA, NULL, NULL, ROUND(PE.PRODUZIONE_HA * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO, 2))) - (TO_NUMBER(DECODE(PE.PRODUZIONE_HA_DANNO, NULL, NULL, ROUND(PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO_DANNEGGIATO, 2))) + NVL(PE.IMPORTO_RIMBORSO, 0)), 2))) AS EURO_DANNO,    \r\n"
		+ "    TO_NUMBER(DECODE(TO_NUMBER(DECODE(PE.PRODUZIONE_HA_DANNO, NULL, NULL, PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO)), NULL,																													\r\n"
		+ "        NULL, 											\r\n"
		+ "        ROUND((1 - ((TO_NUMBER(DECODE(PE.PRODUZIONE_HA_DANNO, NULL, NULL, ROUND(PE.PRODUZIONE_HA_DANNO * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO_DANNEGGIATO, 2))) + NVL(PE.IMPORTO_RIMBORSO, 0)) / TO_NUMBER(DECODE(PE.PRODUZIONE_HA, NULL, NULL, PE.PRODUZIONE_HA * PE.SUPERFICIE_UTILIZZATA * PE.PREZZO)))) * 100, 2))) AS PERCENTUALE_DANNO			\r\n"
		+ "FROM																																							\r\n"
		+ "    TMP_PLV_ELENCO PE																																			\r\n"
		+ "GROUP BY																																						\r\n"
		+ "    PE.ID_SUPERFICIE_COLTURA,																																	\r\n"
		+ "    DECODE(N_BLOCCANTE_S, 0,	DECODE(N_BLOCCANTE_N, 0, NULL, 'N'), 'S'),																												\r\n"
		+ "    PE.EXT_ISTAT_COMUNE,																																		\r\n"
		+ "    PE.EXT_ID_UTILIZZO,                                                                            \r\n"
		+ "    PE.ID_BANDO,                                                                "
		+ "    PE.ID_UTILIZZO,	" 
		+ "    PE.TIPO_UTILIZZO_DESCRIZIONE,																																\r\n"
		+ "    PE.CODICE_UTILIZZO,\r\n" 
		+ "    PE.CODICE_DESTINAZIONE,\r\n"
		+ "    PE.CODICE_DETTAGLIO_USO,\r\n" 
		+ "    PE.CODICE_QUALITA_USO,\r\n"
		+ "    PE.CODICE_VARIETA,																																		\r\n"
		+ "    PE.DESTINAZIONE_DESCRIZIONE,                                                     \r\n"
		+ "    PE.DETTAGLIO_USO_DESCRIZIONE,                                                      \r\n"
		+ "    PE.QUALITA_USO_DESCRIZIONE,                                                      \r\n"
		+ "    PE.VARIETA_DESCRIZIONE,                                                          \r\n"
		+ "    PE.SIGLA_PROVINCIA,																																		\r\n"
		+ "    PE.DESC_COMUNE,																																			\r\n"
		+ "    PE.DESC_COMUNE || ' (' || PE.SIGLA_PROVINCIA || ')',																										\r\n"
		+ "    PE.PRODUZIONE_HA,																																			\r\n"
		+ "    PE.PRODUZIONE_HA_DANNO,                                                              "
		+ "    PE.IMPORTO_RIMBORSO,   "
		+"     PE.FLAG_DANNEGGIATO,"
		+ "    PE.PREZZO,																																					\r\n"
		+ "    PE.SUPERFICIE_UTILIZZATA,                                                             "
		+ "    PE.PRODUZIONE_TOTALE_DANNO,																																\r\n"
		+ "    PE.PREZZO_DANNEGGIATO,																																		\r\n"
		+ "    PE.NOTE,                                                                    \r\n"
		+ "    PE.GIORNATE_LAVORATE,                                                                  "
		+ "    PE.RECORD_MODIFICATO																																		\r\n"
		+ "ORDER BY    																																					\r\n"
		+ "    PE.SIGLA_PROVINCIA,																																		\r\n"
		+ "    PE.DESC_COMUNE,																																			\r\n"
		+ "    PE.TIPO_UTILIZZO_DESCRIZIONE";
	MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          ColtureAziendaliDettaglioDTO.class);
    }
    catch (Throwable t)
    {
	    InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_PROCEDIMENTO_OGGETTO",
              idProcedimentoOggetto) },
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

  public long updateColturaAziendale(long idProcedimentoOggetto,
      ColtureAziendaliDettaglioDTO coltura) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
	String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
      logger.debug(THIS_METHOD + " BEGIN.");
	final String UPDATE = "	 UPDATE IUF_T_SUPERFICIE_COLTURA 										\r\n"
		+ "    SET 															\r\n"
		+ "    PRODUZIONE_HA = :PRODUZIONE_HA,												\r\n"
		+ "    FLAG_DANNEGGIATO = :FLAG_DANNEGGIATO,											\r\n"
		+ "    IMPORTO_RIMBORSO = :IMPORTO_RIMBORSO,											\r\n"
		+ "    PREZZO = :PREZZO,													\r\n"		
		+ "    GIORNATE_LAVORATE = :GIORNATE_LAVORATE_HA,										\r\n"
		+ "    NOTE = :NOTE,														\r\n"
		+ "    PRODUZIONE_HA_DANNO = :PRODUZIONE_HA_DANNO,										\r\n"
		+ "    PREZZO_DANNEGGIATO = :PREZZO_DANNEGGIATO,										\r\n"
		+ "    RECORD_MODIFICATO = 'S'													\r\n"
		+ "    WHERE  	ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO								\r\n"
		+ "	    AND ID_SUPERFICIE_COLTURA = :ID_SUPERFICIE_COLTURA ";
	MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("PRODUZIONE_HA", coltura.getProduzioneHa(),
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_DANNEGGIATO",
          coltura.getFlagDanneggiato(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_RIMBORSO",
          coltura.getImportoRimborso(), Types.NUMERIC);
	    mapParameterSource.addValue("PREZZO", coltura.getPrezzo(), Types.NUMERIC);
      mapParameterSource.addValue("GIORNATE_LAVORATE_HA",
          coltura.getGiornateLavorateHa(), Types.NUMERIC);
	    mapParameterSource.addValue("NOTE", coltura.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("PRODUZIONE_HA_DANNO",
          coltura.getProduzioneHaDanno(), Types.NUMERIC);
      mapParameterSource.addValue("PREZZO_DANNEGGIATO",
          coltura.getPrezzoDanneggiato(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_SUPERFICIE_COLTURA",
          coltura.getIdSuperficieColtura(), Types.NUMERIC);
	    return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
	    InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("PRODUZIONE_HA", coltura.getProduzioneHa()),
              new LogParameter("FLAG_DANNEGGIATO",
                  coltura.getFlagDanneggiato()),
              new LogParameter("IMPORTO_RIMBORSO",
                  coltura.getImportoRimborso()),
        	    		 	 new LogParameter("PREZZO", coltura.getPrezzo()),
              new LogParameter("GIORNATE_LAVORATE_HA",
                  coltura.getGiornateLavorateHa()),
                			 new LogParameter("NOTE", coltura.getNote()),
              new LogParameter("PRODUZIONE_HA_DANNO",
                  coltura.getProduzioneHaDanno()),
              new LogParameter("PREZZO_DANNEGGIATO",
                  coltura.getPrezzoDanneggiato()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_SUPERFICIE_COLTURA",
                  coltura.getIdSuperficieColtura()) },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
	    logInternalUnexpectedException(e, THIS_METHOD);
	    throw e;
	}
    finally
    {
      if (logger.isDebugEnabled())
        logger.debug(THIS_METHOD + " END.");
    }
  }

  public List<AssicurazioniColtureDTO> getListAssicurazioniColture(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
	return getListAssicurazioniColture(idProcedimentoOggetto, null);
    }

  public List<AssicurazioniColtureDTO> getListAssicurazioniColture(
      long idProcedimentoOggetto, long[] arrayIdAssicurazioniColture)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    String inConditionAssicurazioniColture = "";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (arrayIdAssicurazioniColture != null)
    {
      inConditionAssicurazioniColture = getInCondition(
          "ID_ASSICURAZIONI_COLTURE", arrayIdAssicurazioniColture);
    }
    final String QUERY = "SELECT 																	\r\n" +
        "    AC.ID_ASSICURAZIONI_COLTURE,											\r\n" +
        "    AC.ID_PROCEDIMENTO_OGGETTO,											\r\n" +
        "    AC.ID_CONSORZIO_DIFESA,												\r\n" +
        "    AC.NOME_ENTE_PRIVATO,													\r\n" +
        "    AC.NUMERO_SOCIO_POLIZZA,												\r\n" +
        "    AC.IMPORTO_PREMIO,														\r\n" +
        "    AC.IMPORTO_ASSICURATO,													\r\n" +
        "    AC.IMPORTO_RIMBORSO,													\r\n" +
        "    CD.EXT_ID_PROVINCIA,													\r\n" +
        "    CD.DESCRIZIONE AS DESCRIZIONE_CONSORZIO								\r\n" +
        "FROM    																	\r\n" +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,										\r\n" +
        "    IUF_T_ASSICURAZIONI_COLTURE AC,										\r\n" +
        "    IUF_D_CONSORZIO_DIFESA CD											\r\n" +
        "WHERE 																		\r\n" +
        "    AC.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO				\r\n"
        +
        "    AND AC.ID_CONSORZIO_DIFESA = CD.ID_CONSORZIO_DIFESA (+)				\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 				\r\n"
        +
        inConditionAssicurazioniColture;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          AssicurazioniColtureDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_ASSICURAZIONI_COLTURE",
                  arrayIdAssicurazioniColture)
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

  public AssicurazioniColtureDTO getRiepilogoAssicurazioniColture(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 																	\r\n" +
        "    SUM(AC.IMPORTO_PREMIO),												\r\n" +
        "    SUM(AC.IMPORTO_ASSICURATO),											\r\n" +
        "    SUM(AC.IMPORTO_RIMBORSO) 												\r\n" +
        "FROM    																	\r\n" +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,										\r\n" +
        "    IUF_T_ASSICURAZIONI_COLTURE AC										\r\n" +
        "WHERE 																		\r\n" +
        "    AC.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO				\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 				\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource,
          AssicurazioniColtureDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public int eliminaAssicurazioniColture(long idProcedimentoOggetto,
      long[] arrayIdAssicurazioniColture)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_T_ASSICURAZIONI_COLTURE					\r\n"
        +
        "WHERE 														\r\n" +
        "    ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO		\r\n" +
        getInCondition("ID_ASSICURAZIONI_COLTURE", arrayIdAssicurazioniColture);
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_ASSICURAZIONI_COLTURE",
                  arrayIdAssicurazioniColture),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public long inserisciAssicurazioniColture(long idProcedimentoOggetto,
      AssicurazioniColtureDTO assicurazioniColture)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_ASSICURAZIONI_COLTURE				\r\n"
        +
        "        (ID_ASSICURAZIONI_COLTURE,						\r\n" +
        "        ID_CONSORZIO_DIFESA,							\r\n" +
        "        NOME_ENTE_PRIVATO,								\r\n" +
        "        NUMERO_SOCIO_POLIZZA,							\r\n" +
        "        IMPORTO_PREMIO,								\r\n" +
        "        IMPORTO_ASSICURATO,							\r\n" +
        "        IMPORTO_RIMBORSO,								\r\n" +
        "        ID_PROCEDIMENTO_OGGETTO						\r\n" +
        "    )													\r\n" +
        "VALUES 												\r\n" +
        "    (													\r\n" +
        "        :ID_ASSICURAZIONI_COLTURE,						\r\n" +
        "        :ID_CONSORZIO_DIFESA,							\r\n" +
        "        :NOME_ENTE_PRIVATO,							\r\n" +
        "        :NUMERO_SOCIO_POLIZZA,							\r\n" +
        "        :IMPORTO_PREMIO,								\r\n" +
        "        :IMPORTO_ASSICURATO,							\r\n" +
        "        :IMPORTO_RIMBORSO,								\r\n" +
        "        :ID_PROCEDIMENTO_OGGETTO						\r\n" +
        "    )";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long idAssicurazioniColture = 0;
    try
    {
      idAssicurazioniColture = getNextSequenceValue(
          "SEQ_IUF_T_ASSICURAZIONI_COLT");
      mapParameterSource.addValue("ID_ASSICURAZIONI_COLTURE",
          idAssicurazioniColture, Types.NUMERIC);
      mapParameterSource.addValue("ID_CONSORZIO_DIFESA",
          assicurazioniColture.getIdConsorzioDifesa(), Types.NUMERIC);
      mapParameterSource.addValue("NOME_ENTE_PRIVATO",
          assicurazioniColture.getNomeEntePrivato(), Types.VARCHAR);
      mapParameterSource.addValue("NUMERO_SOCIO_POLIZZA",
          assicurazioniColture.getNumeroSocioPolizza(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_PREMIO",
          assicurazioniColture.getImportoPremio(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_ASSICURATO",
          assicurazioniColture.getImportoAssicurato(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_RIMBORSO",
          assicurazioniColture.getImportoRimborso(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_ASSICURAZIONI_COLTURE",
                  idAssicurazioniColture),
              new LogParameter("ID_CONSORZIO_DIFESA",
                  assicurazioniColture.getIdConsorzioDifesa()),
              new LogParameter("NOME_ENTE_PRIVATO",
                  assicurazioniColture.getNomeEntePrivato()),
              new LogParameter("NUMERO_SOCIO_POLIZZA",
                  assicurazioniColture.getNumeroSocioPolizza()),
              new LogParameter("IMPORTO_PREMIO",
                  assicurazioniColture.getImportoPremio()),
              new LogParameter("IMPORTO_ASSICURATO",
                  assicurazioniColture.getImportoAssicurato()),
              new LogParameter("IMPORTO_RIMBORSO",
                  assicurazioniColture.getImportoRimborso()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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
    return idAssicurazioniColture;
  }

  public List<DecodificaDTO<Integer>> getListConsorzi(String idProvincia)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 												\r\n" +
        "    ID_CONSORZIO_DIFESA AS ID,							\r\n" +
        "    ID_CONSORZIO_DIFESA AS CODICE,						\r\n" +
        "    DESCRIZIONE										\r\n" +
        "FROM 													\r\n" +
        "    IUF_D_CONSORZIO_DIFESA							\r\n" +
        "WHERE													\r\n" +
        "    EXT_ID_PROVINCIA = :ID_PROVINCIA					\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROVINCIA", idProvincia, Types.VARCHAR);
      return queryForDecodificaInteger(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROVINCIA", idProvincia)
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

  public List<Long> getListConsorzioDifesa(String[] arrayProvincePiemonte)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 												\r\n" +
        "    ID_CONSORZIO_DIFESA AS ID							\r\n" +
        "FROM 													\r\n" +
        "    IUF_D_CONSORZIO_DIFESA							\r\n" +
        "WHERE													\r\n" +
        "    1=1 \r\n" +
        getInCondition("EXT_ID_PROVINCIA",
            Arrays.asList(arrayProvincePiemonte));
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(QUERY, mapParameterSource, Long.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("EXT_ID_PROVINCIA", arrayProvincePiemonte)
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

  public long modificaAssicurazioniColture(long idProcedimentoOggetto,
      AssicurazioniColtureDTO assicurazioniColture)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_ASSICURAZIONI_COLTURE										\r\n"
        +
        "SET  																		\r\n" +
        "    ID_CONSORZIO_DIFESA = :ID_CONSORZIO_DIFESA,							\r\n" +
        "    NOME_ENTE_PRIVATO = :NOME_ENTE_PRIVATO,								\r\n" +
        "    NUMERO_SOCIO_POLIZZA = :NUMERO_SOCIO_POLIZZA,							\r\n" +
        "    IMPORTO_PREMIO = :IMPORTO_PREMIO,										\r\n" +
        "    IMPORTO_ASSICURATO = :IMPORTO_ASSICURATO,								\r\n" +
        "    IMPORTO_RIMBORSO = :IMPORTO_RIMBORSO									\r\n" +
        "WHERE																		\r\n" +
        "        ID_ASSICURAZIONI_COLTURE =:ID_ASSICURAZIONI_COLTURE				\r\n"
        +
        "    AND ID_PROCEDIMENTO_OGGETTO  =:ID_PROCEDIMENTO_OGGETTO					\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_ASSICURAZIONI_COLTURE",
          assicurazioniColture.getIdAssicurazioniColture(), Types.NUMERIC);
      mapParameterSource.addValue("ID_CONSORZIO_DIFESA",
          assicurazioniColture.getIdConsorzioDifesa(), Types.NUMERIC);
      mapParameterSource.addValue("NOME_ENTE_PRIVATO",
          assicurazioniColture.getNomeEntePrivato(), Types.VARCHAR);
      mapParameterSource.addValue("NUMERO_SOCIO_POLIZZA",
          assicurazioniColture.getNumeroSocioPolizza(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_PREMIO",
          assicurazioniColture.getImportoPremio(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_ASSICURATO",
          assicurazioniColture.getImportoAssicurato(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_RIMBORSO",
          assicurazioniColture.getImportoRimborso(), Types.NUMERIC);
      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_ASSICURAZIONI_COLTURE",
                  assicurazioniColture.getIdAssicurazioniColture()),
              new LogParameter("ID_CONSORZIO_DIFESA",
                  assicurazioniColture.getIdConsorzioDifesa()),
              new LogParameter("NOME_ENTE_PRIVATO",
                  assicurazioniColture.getNomeEntePrivato()),
              new LogParameter("NUMERO_SOCIO_POLIZZA",
                  assicurazioniColture.getNumeroSocioPolizza()),
              new LogParameter("IMPORTO_PREMIO",
                  assicurazioniColture.getImportoPremio()),
              new LogParameter("IMPORTO_ASSICURATO",
                  assicurazioniColture.getImportoAssicurato()),
              new LogParameter("IMPORTO_RIMBORSO",
                  assicurazioniColture.getImportoRimborso())
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

  // Query Complessa PLV
  public List<DanniColtureDTO> getListDanniColture(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "WITH TMP_SUPERFICI AS (    \r\n" +
        "SELECT \r\n" +
        "    CU.ID_UTILIZZO,																			\r\n" +
        "    CU.COD_TIPO_UTILIZZO,																	\r\n" +
        "    CU.DESC_TIPO_UTILIZZO,																	\r\n" +
        "    CU.COD_TIPO_UTILIZZO_SECONDARIO,														\r\n" +
        "    CU.DESC_TIPO_UTILIZZO_SECONDARIO,\r\n" +
        "    SC.COLTURA_SECONDARIA,\r\n" +
        "    DECODE(SC.COLTURA_SECONDARIA, 'S',\r\n" +
        "        NVL(DESC_TIPO_UTILIZZO_SECONDARIO, DESC_TIPO_UTILIZZO) || '(secondario)' || ' [' || NVL(COD_TIPO_UTILIZZO_SECONDARIO,COD_TIPO_UTILIZZO) || '] ',\r\n"
        +
        "        DESC_TIPO_UTILIZZO || ' [' || COD_TIPO_UTILIZZO || ']'\r\n" +
        "    ) AS TIPO_UTILIZZO_DESCRIZIONE,\r\n" +
        "    \r\n" +
        "    SUM(NVL(SC.PRODUZIONE_HA * CU.SUPERFICIE_UTILIZZATA,0)) AS TOT_QLI_PLV_ORD,\r\n"
        +
        "    SUM(NVL(SC.PRODUZIONE_HA * CU.SUPERFICIE_UTILIZZATA * SC.PREZZO,0)) AS TOT_EURO_PLV_ORD,\r\n"
        +
        "    SUM(SUPERFICIE_UTILIZZATA) AS SUPERFICIE_UTILIZZATA,\r\n" +
        "    \r\n" +
        "    DECODE(SC.PRODUZIONE_TOTALE_DANNO, NULL,\r\n" +
        "        SUM(NVL(SC.PRODUZIONE_HA * CU.SUPERFICIE_UTILIZZATA, 0)),\r\n"
        +
        "        SC.PRODUZIONE_TOTALE_DANNO) AS TOT_QLI_PLV_EFF,\r\n" +
        "        \r\n" +
        "    DECODE(SC.PRODUZIONE_TOTALE_DANNO, NULL,\r\n" +
        "        SUM(NVL(SC.PRODUZIONE_HA * CU.SUPERFICIE_UTILIZZATA * SC.PREZZO, 0)),\r\n"
        +
        "        SC.PRODUZIONE_TOTALE_DANNO * SC.PREZZO_DANNEGGIATO) AS TOT_EURO_PLV_EFF        \r\n"
        +
        "FROM																						\r\n" +
        "    IUF_T_SUPERFICIE_COLTURA SC,															\r\n" +
        "    IUF_T_PROCEDIMENTO_OGGETTO PO,														\r\n" +
        "    SMRGAA_V_CONDUZIONE_UTILIZZO CU																											\r\n"
        +
        "WHERE																						\r\n" +
        "    PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO									\r\n"
        +
        "    AND PO.ID_PROCEDIMENTO_OGGETTO = SC.ID_PROCEDIMENTO_OGGETTO						 	\r\n"
        +
        "        AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN = CU.ID_DICHIARAZIONE_CONSISTENZA			\r\n"
        +
        "            AND CU.ID_UTILIZZO = SC.EXT_ID_UTILIZZO										\r\n"
        +
        "                AND CU.COMUNE = SC.EXT_ISTAT_COMUNE			\r\n" +
        "\r\n" +
        "GROUP BY\r\n" +
        "    \r\n" +
        "    CU.ID_UTILIZZO,																			\r\n" +
        "    CU.COD_TIPO_UTILIZZO,																	\r\n" +
        "    CU.DESC_TIPO_UTILIZZO,																	\r\n" +
        "    CU.COD_TIPO_UTILIZZO_SECONDARIO,														\r\n" +
        "    CU.DESC_TIPO_UTILIZZO_SECONDARIO,\r\n" +
        "    SC.ID_SUPERFICIE_COLTURA,\r\n" +
        "    SC.PRODUZIONE_TOTALE_DANNO,\r\n" +
        "    SC.PREZZO_DANNEGGIATO,\r\n" +
        "    SC.COLTURA_SECONDARIA\r\n" +
        ")\r\n" +
        "    SELECT \r\n" +
        "        ID_UTILIZZO,																			\r\n" +
        "        COD_TIPO_UTILIZZO,																	\r\n" +
        "        DESC_TIPO_UTILIZZO,																	\r\n" +
        "        COD_TIPO_UTILIZZO_SECONDARIO,														\r\n"
        +
        "        DESC_TIPO_UTILIZZO_SECONDARIO,\r\n" +
        "        COLTURA_SECONDARIA,\r\n" +
        "        TIPO_UTILIZZO_DESCRIZIONE,\r\n" +
        "        SUM(SUPERFICIE_UTILIZZATA) AS SUPERFICIE_UTILIZZATA,\r\n" +
        "        SUM(TOT_QLI_PLV_ORD) AS TOT_QLI_PLV_ORD,\r\n" +
        "        SUM(TOT_EURO_PLV_ORD) AS TOT_EURO_PLV_ORD,\r\n" +
        "        SUM(TOT_QLI_PLV_EFF) AS TOT_QLI_PLV_EFF,\r\n" +
        "        SUM(TOT_EURO_PLV_EFF) AS TOT_EURO_PLV_EFF\r\n" +
        "    FROM \r\n" +
        "        TMP_SUPERFICI\r\n" +
        "    GROUP BY    \r\n" +
        "        ID_UTILIZZO,																			\r\n" +
        "        COD_TIPO_UTILIZZO,																	\r\n" +
        "        DESC_TIPO_UTILIZZO,																	\r\n" +
        "        COD_TIPO_UTILIZZO_SECONDARIO,														\r\n"
        +
        "        DESC_TIPO_UTILIZZO_SECONDARIO,\r\n" +
        "        COLTURA_SECONDARIA,\r\n" +
        "        TIPO_UTILIZZO_DESCRIZIONE\r\n" +
        "    ORDER BY\r\n" +
        "        TIPO_UTILIZZO_DESCRIZIONE\r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, DanniColtureDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public Long getNColtureDanneggiate(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 														\r\n" +
        "    COUNT(*) AS N												\r\n" +
        "FROM 															\r\n" +
        "    IUF_T_SUPERFICIE_COLTURA									\r\n" +
        "WHERE 															\r\n" +
        "    ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO			\r\n" +
        "    AND PRODUZIONE_TOTALE_DANNO IS NOT NULL";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public void updateAltroDocRichiesto(long idProcedimentoOggetto,
      String altroDocRichiesto) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateAltroDocRichiesto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    long result;
    final String QUERY = " UPDATE IUF_T_DOCUMENTI_RICHIESTI                    \n"
        + " SET                                                   \n"
        + "     ALTRO_DOC_RICHIESTO = :ALTRODOCRICHIESTO          \n"
        + " WHERE                                                 \n"
        + "     ID_PROCEDIMENTO_OGGETTO = :IDPROCEDIMENTOOGGETTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ALTRODOCRICHIESTO", altroDocRichiesto,
          Types.VARCHAR);
      mapParameterSource.addValue("IDPROCEDIMENTOOGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      result = namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
      if (result == 0)
      {
        final String QUERY_I = " INSERT INTO IUF_T_DOCUMENTI_RICHIESTI (     	\n"
            + "     ID_DOCUMENTI_RICHIESTI,                   		\n"
            + "     ID_PROCEDIMENTO_OGGETTO,                  \n"
            + "     ALTRO_DOC_RICHIESTO                       \n"
            + " ) VALUES (                                    \n"
            + "     SEQ_IUF_T_DOCUMENTI_RICHIEST.NEXTVAL, \n"
            + "     :IDPROCEDIMENTOOGGETTO,                 	\n"
            + "     :ALTRODOCRICHIESTO                      	\n"
            + " )                                             \n";
        mapParameterSource.addValue("IDPROCEDIMENTOOGGETTO",
            idProcedimentoOggetto, Types.NUMERIC);
        mapParameterSource.addValue("ALTRODOCRICHIESTO", altroDocRichiesto,
            Types.VARCHAR);
        result = namedParameterJdbcTemplate.update(QUERY_I, mapParameterSource);
      }

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("IDPROCEDIMENTOOGGETTO", idProcedimentoOggetto),
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

  public void insertRTipoDocRichiesti(long idProcedimentoOggetto,
      List<Long> listTipoDoc) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertRTipoDocRichiesti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " INSERT INTO IUF_R_TIPO_DOCUMENTI_RICHIES (          \n"
        + " ID_DOCUMENTI_RICHIESTI,                               \n"
        + " ID_TIPO_DOC_RICHIESTI                                 \n"
        + " )                                                     \n"
        + " SELECT                                                \n"
        + " DR.ID_DOCUMENTI_RICHIESTI,                            \n"
        + " :IDTIPODOCUMENTIRICHIESTI                             \n"
        + " FROM                                                  \n"
        + " IUF_T_DOCUMENTI_RICHIESTI DR                        \n"
        + " WHERE                                                 \n"
        + " DR.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n";

    MapSqlParameterSource[] mapParameterSource = new MapSqlParameterSource[listTipoDoc
        .size()];
    try
    {
      for (int i = 0; i < listTipoDoc.size(); i++)
      {
        mapParameterSource[i] = new MapSqlParameterSource();
        mapParameterSource[i].addValue("ID_PROCEDIMENTO_OGGETTO",
            idProcedimentoOggetto, Types.NUMERIC);
        mapParameterSource[i].addValue("IDTIPODOCUMENTIRICHIESTI",
            listTipoDoc.get(i), Types.NUMERIC);
      }

      namedParameterJdbcTemplate.batchUpdate(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("listTipoDoc", listTipoDoc)
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

  public void deleteRTipoDocRichiesti(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRTipoDocRichiesti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_R_TIPO_DOCUMENTI_RICHIES                        \n"
        + " WHERE                                                             \n"
        + "     ID_DOCUMENTI_RICHIESTI = (                                    \n"
        + "         SELECT                                                    \n"
        + "             DR.ID_DOCUMENTI_RICHIESTI                             \n"
        + "         FROM                                                      \n"
        + "             IUF_T_DOCUMENTI_RICHIESTI DR                        \n"
        + "         WHERE                                                     \n"
        + "             DR.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "     )					                                            \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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
  
  public void eliminaCensitoPrelievoOgur(long idPianoDistretto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRTipoDocRichiesti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_T_CENSITO_PRELIEVO_OGUR WHERE ID_PIANO_DISTRETTO_OGUR = :ID_PIANO_DISTRETTO_OGUR";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_DISTRETTO_OGUR",
          idPianoDistretto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PIANO_DISTRETTO_OGUR",
                  idPianoDistretto),
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
  
  public void eliminaDateCensimento(long idPianoDistretto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::eliminaDateCensimento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_T_DATE_CENS_OGUR WHERE ID_PIANO_DISTRETTO_OGUR = :ID_PIANO_DISTRETTO_OGUR";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_DISTRETTO_OGUR",
          idPianoDistretto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PIANO_DISTRETTO_OGUR",
                  idPianoDistretto),
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

  public List<SezioneDocumentiRichiestiDTO> getListDocumentiRichiestiDaVisualizzare(
      long idProcedimentoOggetto, Boolean isVisualizzazione)
      throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS
        + "::getListDocumentiRichiestiDaVisualizzare]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH DOCUMENTI_RICHIESTI AS (                                                                                 \n"
        + "     SELECT                                                                                                    \n"
        + "         DR.*,                                                                                                 \n"
        + "         TDR.ID_TIPO_DOC_RICHIESTI                                                                             \n"
        + "     FROM                                                                                                      \n"
        + "         IUF_T_DOCUMENTI_RICHIESTI DR,                                                                       \n"
        + "         IUF_R_TIPO_DOCUMENTI_RICHIES TDR                                                                    \n"
        + "     WHERE                                                                                                     \n"
        + "         DR.ID_DOCUMENTI_RICHIESTI = TDR.ID_DOCUMENTI_RICHIESTI                                                \n"
        + "         AND DR.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                             \n"
        + " )                                                                                                             \n"
        + " SELECT                                                                                                        \n"
        + "     RTDR.ID_DOCUMENTI_RICHIESTI,                                                                              \n"
        + "     TDR.ID_TIPO_DOC_RICHIESTI,                                                                                \n"
        + "     TDR.DESCRIZIONE,                                                                                          \n"
        + "     TDR.ORDINE,                                                                                               \n"
        + "     SDR.CODICE,                                                                                               \n"
        + "     SDR.TESTO_SEZIONE                                                                                         \n"
        + " FROM                                                                                                          \n"
        + "     IUF_D_TIPO_DOC_RICHIESTI TDR                                                                            \n"
        + "     LEFT OUTER JOIN DOCUMENTI_RICHIESTI RTDR ON ( TDR.ID_TIPO_DOC_RICHIESTI = RTDR.ID_TIPO_DOC_RICHIESTI ),   \n"
        + "     IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                          \n"
        + "     IUF_D_SEZIONE_DOC_RICHIESTI SDR,                                                                        \n"
        + "     IUF_R_TIPO_DOC_RIC_OGGETTO RTDRO,                                                                       \n"
        + "     IUF_R_TIPO_DOC_RIC_BANDO_OGG RTDRBO,                                                                    \n"
        + "     IUF_R_BANDO_OGGETTO BO,                                                                                 \n"
        + "     IUF_T_PROCEDIMENTO P                                                                                    \n"
        + " WHERE                                                                                                         \n"
        + "     TDR.DATA_INIZIO_VALIDITA <= NVL(PO.DATA_FINE, SYSDATE)                                                    \n"
        + "     AND SDR.ID_SEZIONE_DOC_RICHIESTI = TDR.ID_SEZIONE_DOC_RICHIESTI                                           \n"
        + "     AND ( TDR.DATA_FINE_VALIDITA IS NULL                                                                      \n"
        + "           OR TDR.DATA_FINE_VALIDITA >= TRUNC(NVL(PO.DATA_FINE, SYSDATE)) )                                    \n"
        + "     AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                 \n"
        + "     AND PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                                                                \n"
        + "     AND BO.ID_LEGAME_GRUPPO_OGGETTO = PO.ID_LEGAME_GRUPPO_OGGETTO                                             \n"
        + "     AND BO.ID_BANDO = P.ID_BANDO                                                                              \n"
        + "     AND RTDRBO.ID_BANDO_OGGETTO = BO.ID_BANDO_OGGETTO                                                         \n"
        + "     AND RTDRBO.ID_TIPO_DOC_RIC_OGGETTO = RTDRO.ID_TIPO_DOC_RIC_OGGETTO                                        \n"
        + "     AND RTDRO.ID_TIPO_DOC_RICHIESTI = TDR.ID_TIPO_DOC_RICHIESTI                                               \n"
        + " UNION ALL                                                                                                     \n"
        + " SELECT                                                                                                        \n"
        + "     DR.ID_DOCUMENTI_RICHIESTI,                                                                                \n"
        + "     NULL,                                                                                                     \n"
        + "     DR.ALTRO_DOC_RICHIESTO   AS DESCRIZIONE,                                                                  \n"
        + "     9999999999,                                                                                               \n"
        + "     SDR.CODICE,                                                                                               \n"
        + "     SDR.TESTO_SEZIONE                                                                                         \n"
        + " FROM                                                                                                          \n"
        + "     IUF_D_SEZIONE_DOC_RICHIESTI SDR, IUF_T_PROCEDIMENTO_OGGETTO PO                                        \n"
        + "     LEFT OUTER JOIN IUF_T_DOCUMENTI_RICHIESTI DR ON DR.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO \n"
        + " WHERE                                                                                                         \n"
        + "     PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                     \n"
        + (isVisualizzazione ? " AND DR.ALTRO_DOC_RICHIESTO IS NOT NULL\n" : "")
        + "     AND SDR.CODICE = 'H'                                                                                      \n"
        + " ORDER BY                                                                                                      \n"
        + "     5,                                                                                                        \n"
        + "     4                                                                                                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<SezioneDocumentiRichiestiDTO>>()
          {
            @Override
            public List<SezioneDocumentiRichiestiDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<SezioneDocumentiRichiestiDTO> list = new ArrayList<SezioneDocumentiRichiestiDTO>();
              ArrayList<DocumentiRichiestiDaVisualizzareDTO> listDoc = new ArrayList<DocumentiRichiestiDaVisualizzareDTO>();
              SezioneDocumentiRichiestiDTO sezioneDocumentiRichiestiDTO = null;
              DocumentiRichiestiDaVisualizzareDTO doc = null;
              String lastIdSezione = null;
              String idSezione = null;
              while (rs.next())
              {
                idSezione = rs.getString("CODICE");
                if (lastIdSezione == null || !lastIdSezione.equals(idSezione))
                {
                  if ((isVisualizzazione
                      && rs.getLong("ID_DOCUMENTI_RICHIESTI") != 0)
                      || !isVisualizzazione)
                  {
                    if (sezioneDocumentiRichiestiDTO != null)
                      list.add(sezioneDocumentiRichiestiDTO);

                    sezioneDocumentiRichiestiDTO = new SezioneDocumentiRichiestiDTO();

                    doc = new DocumentiRichiestiDaVisualizzareDTO();

                    sezioneDocumentiRichiestiDTO
                        .setDescrizione(rs.getString("TESTO_SEZIONE"));
                    sezioneDocumentiRichiestiDTO
                        .setIdSezione(rs.getString("CODICE"));
                    if (rs.getString("ID_DOCUMENTI_RICHIESTI") != null)
                    {
                      sezioneDocumentiRichiestiDTO.setContatoreDoc(
                          sezioneDocumentiRichiestiDTO.getContatoreDoc() + 1);
                      doc.setIdDocumentiRichiesti(
                          rs.getLong("ID_DOCUMENTI_RICHIESTI"));
                    }

                    doc.setIdTipoDocRichiesti(
                        rs.getLong("ID_TIPO_DOC_RICHIESTI"));
                    doc.setDescrizione(rs.getString("DESCRIZIONE"));
                    doc.setOrdine(rs.getLong("ORDINE"));
                    listDoc = new ArrayList<DocumentiRichiestiDaVisualizzareDTO>();
                    listDoc.add(doc);
                    sezioneDocumentiRichiestiDTO.setList(listDoc);
                    lastIdSezione = idSezione;
                  }

                }
                else
                {
                  if ((isVisualizzazione
                      && rs.getLong("ID_DOCUMENTI_RICHIESTI") != 0)
                      || !isVisualizzazione)
                  {
                    doc = new DocumentiRichiestiDaVisualizzareDTO();

                    if (rs.getString("ID_DOCUMENTI_RICHIESTI") != null)
                    {
                      sezioneDocumentiRichiestiDTO.setContatoreDoc(
                          sezioneDocumentiRichiestiDTO.getContatoreDoc() + 1);
                      doc.setIdDocumentiRichiesti(
                          rs.getLong("ID_DOCUMENTI_RICHIESTI"));

                    }
                    doc.setIdTipoDocRichiesti(
                        rs.getLong("ID_TIPO_DOC_RICHIESTI"));
                    doc.setDescrizione(rs.getString("DESCRIZIONE"));
                    doc.setOrdine(rs.getLong("ORDINE"));
                    listDoc.add(doc);
                    lastIdSezione = idSezione;
                  }

                }

              }
              if (sezioneDocumentiRichiestiDTO != null)
                list.add(sezioneDocumentiRichiestiDTO);

              return list.isEmpty() ? null : list;
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

  public List<DocumentiRichiestiDTO> getDocumentiRichiesti(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDocumentiRichiesti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                    \n"
        + "     DR.ID_DOCUMENTI_RICHIESTI,                            \n"
        + "     DR.ID_PROCEDIMENTO_OGGETTO,                           \n"
        + "     DR.ALTRO_DOC_RICHIESTO                                \n"
        + " FROM                                                      \n"
        + "     IUF_T_DOCUMENTI_RICHIESTI DR                        \n"
        + " WHERE                                                     \n"
        + "     DR.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);

      List<DocumentiRichiestiDTO> result = queryForList(QUERY,
          mapParameterSource, DocumentiRichiestiDTO.class);
      return result;
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

  public List<String> getListTestoSezioni() throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getListTestoSezioni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                \n"
        + "     SDR.TESTO_SEZIONE                 \n"
        + " FROM                                  \n"
        + "     IUF_D_SEZIONE_DOC_RICHIESTI SDR \n"
        + " ORDER BY 								\n"
        + "	  SDR.CODICE						\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<String>>()
          {
            @Override
            public List<String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<String> list = new ArrayList<String>();
              while (rs.next())
              {
                if (rs.getString("TESTO_SEZIONE") != null)
                {
                  list.add(rs.getString("TESTO_SEZIONE"));
                }
                else
                {
                  list.add("-");
                }
              }

              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  public ReferenteProgettoDTO getReferenteProgettoByIdProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getReferenteProgettoByIdProcedimentoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                 \n"
        + "     TRP.COGNOME,                                       \n"
        + "     TRP.NOME,                                          \n"
        + "     TRP.CODICE_FISCALE,                                \n"
        + "     VDA.DESCRIZIONE_PROVINCIA,                         \n"
        + "     VDA.DESCRIZIONE_COMUNE,                            \n"
        + "	  TRP.EXT_ISTAT_COMUNE,								 \n"
        + "     TRP.CAP,                                           \n"
        + "     TRP.TELEFONO,                                      \n"
        + "     TRP.CELLULARE,                                     \n"
        + "     TRP.EMAIL                                          \n"
        + " FROM                                                   \n"
        + "     IUF_T_REFERENTE_PROGETTO TRP,                    \n"
        + "     SMRGAA_V_DATI_AMMINISTRATIVI VDA                   \n"
        + " WHERE                                                  \n"
        + "     ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "     AND VDA.ISTAT_COMUNE = TRP.EXT_ISTAT_COMUNE        \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);

      ReferenteProgettoDTO referente = queryForObject(QUERY, mapParameterSource,
          ReferenteProgettoDTO.class);
      return referente;
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

  public void insertReferenteProgettoByIdProcedimentoOggetto(
      long idProcedimentoOggetto, String nome, String cognome,
      String codiceFiscale, String comune, String cap, String telefono,
      String cellulare,
      String email) throws InternalUnexpectedException
  {
    String methodName = "insertReferenteProgettoByIdProcedimentoOggetto";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_REFERENTE_PROGETTO (    \n"
        + "     ID_REFERENTE_PROGETTO,                  \n"
        + "     ID_PROCEDIMENTO_OGGETTO,                \n"
        + "     NOME,                                   \n"
        + "     COGNOME,                                \n"
        + "     CODICE_FISCALE,                         \n"
        + "     CAP,                                    \n"
        + "     EXT_ISTAT_COMUNE,                       \n"
        + "     TELEFONO,                               \n"
        + "     CELLULARE,                              \n"
        + "     EMAIL                                   \n"
        + " ) VALUES (                                  \n"
        + "     :SEQ_IUF_T_REFERENTE_PROGETTO, \n"
        + "     :ID_PROCEDIMENTO_OGGETTO,               \n"
        + "     UPPER(:NOME),                           \n"
        + "     UPPER(:COGNOME),                        \n"
        + "     :CODICE_FISCALE,                        \n"
        + "     :CAP,                                   \n"
        + "     :EXT_ISTAT_COMUNE,                      \n"
        + "     :TELEFONO,                              \n"
        + "     :CELLULARE,                             \n"
        + "     :EMAIL                                  \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTReferenteProgetto = 0;
    try
    {
      seqIuffiTReferenteProgetto = getNextSequenceValue(
          "SEQ_IUF_T_REFERENTE_PROGETTO");
      mapParameterSource.addValue("SEQ_IUF_T_REFERENTE_PROGETTO",
          seqIuffiTReferenteProgetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("NOME", nome, Types.VARCHAR);
      mapParameterSource.addValue("COGNOME", cognome, Types.VARCHAR);
      mapParameterSource.addValue("CODICE_FISCALE", codiceFiscale,
          Types.VARCHAR);
      mapParameterSource.addValue("CAP", cap, Types.VARCHAR);
      mapParameterSource.addValue("EXT_ISTAT_COMUNE", comune, Types.VARCHAR);
      mapParameterSource.addValue("TELEFONO", telefono, Types.VARCHAR);
      mapParameterSource.addValue("CELLULARE", cellulare, Types.VARCHAR);
      mapParameterSource.addValue("EMAIL", email, Types.VARCHAR);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("SEQ_IUF_T_REFERENTE_PROGETTO",
                  seqIuffiTReferenteProgetto),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("NOME", nome),
              new LogParameter("COGNOME", cognome),
              new LogParameter("CODICE_FISCALE", codiceFiscale),
              new LogParameter("CAP", cap),
              new LogParameter("EXT_ISTAT_COMUNE", comune),
              new LogParameter("TELEFONO", telefono),
              new LogParameter("CELLULARE", cellulare),
              new LogParameter("EMAIL", email),
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

  public void updateReferenteProgettoByIdProcedimentoOggetto(
      long idProcedimentoOggetto, String nome, String cognome,
      String codiceFiscale, String comune, String cap, String telefono,
      String cellulare,
      String email) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE IUF_T_REFERENTE_PROGETTO                      \n"
        + " SET                                                    \n"
        + "     NOME = UPPER(:NOME),                               \n"
        + "     COGNOME = UPPER(:COGNOME),                         \n"
        + "     CODICE_FISCALE = :CODICE_FISCALE,                  \n"
        + "     CAP = :CAP,                                        \n"
        + "     EXT_ISTAT_COMUNE = :EXT_ISTAT_COMUNE,              \n"
        + "     TELEFONO = :TELEFONO,                              \n"
        + "     CELLULARE = :CELLULARE,                            \n"
        + "     EMAIL = :EMAIL                                     \n"
        + " WHERE                                                  \n"
        + "     ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("NOME", nome, Types.VARCHAR);
      mapParameterSource.addValue("COGNOME", cognome, Types.VARCHAR);
      mapParameterSource.addValue("CODICE_FISCALE", codiceFiscale,
          Types.VARCHAR);
      mapParameterSource.addValue("CAP", cap, Types.VARCHAR);
      mapParameterSource.addValue("EXT_ISTAT_COMUNE", comune, Types.VARCHAR);
      mapParameterSource.addValue("TELEFONO", telefono, Types.VARCHAR);
      mapParameterSource.addValue("CELLULARE", cellulare, Types.VARCHAR);
      mapParameterSource.addValue("EMAIL", email, Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("NOME", nome),
              new LogParameter("COGNOME", cognome),
              new LogParameter("CODICE_FISCALE", codiceFiscale),
              new LogParameter("CAP", cap),
              new LogParameter("EXT_ISTAT_COMUNE", comune),
              new LogParameter("TELEFONO", telefono),
              new LogParameter("CELLULARE", cellulare),
              new LogParameter("EMAIL", email)
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
  
  public void insertIuffiTAziendaBioMulti(
      long idAzienda, long idMetodi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
      throws InternalUnexpectedException
  {
    String methodName = "insertIuffiTAziendaBio";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_R_AZIENDA_BIO_MULTIFUNZ (    \n"
        + "     ID_AZIENDA_BIO,                  \n"
        + "     ID_MULTIFUNZIONALITA                \n"
        + " ) VALUES (                                  \n"
        + "     :ID_AZIENDA_BIO,                              \n"
        + "     :ID_MULTIFUNZIONALITA                             \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_AZIENDA_BIO",
          idAzienda, Types.NUMERIC);
      mapParameterSource.addValue("ID_MULTIFUNZIONALITA",
          idMetodi, Types.NUMERIC);
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
             
              new LogParameter("ID_AZIENDA_BIO",
                  idAzienda),
              new LogParameter("ID_FILIERA_PRODUTTIVA", idMetodi),

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
  
  public void insertCensitoPrelievoOgur(long idPianoDistretto, long idSpecie,
      int progressivo, int censito, int prelevato, BigDecimal percentuale,
      String esito) throws InternalUnexpectedException
  {
    String methodName = "insertCensitoPrelievoOgur";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_CENSITO_PRELIEVO_OGUR (    \n"
        + "     ID_CENSITO_PRELIEVO_OGUR,                  \n"
        + "     ID_PIANO_DISTRETTO_OGUR,                  \n"
        + "     ID_SPECIE_OGUR,                  \n"
        + "     PROGRESSIVO,                  \n"
        + "     CENSITO,                \n"
        + "     PRELEVATO,                  \n"
        + "     PERCENTUALE,                  \n"
        + "     ESITO_CONTROLLO                \n"
        + " ) VALUES (                                  \n"
        + "     :ID_CENSITO_PRELIEVO_OGUR,                  \n"
        + "     :ID_PIANO_DISTRETTO_OGUR,                  \n"
        + "     :ID_SPECIE_OGUR,                  \n"
        + "     :PROGRESSIVO,                  \n"
        + "     :CENSITO,                \n"
        + "     :PRELEVATO,                  \n"
        + "     :PERCENTUALE,                  \n"
        + "     :ESITO_CONTROLLO                \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      long seqIuffiTReferenteProgetto = 0;
      seqIuffiTReferenteProgetto = getNextSequenceValue(
          "SEQ_IUF_T_CENSITO_PRELI_OGUR");
      mapParameterSource.addValue("ID_CENSITO_PRELIEVO_OGUR",
          seqIuffiTReferenteProgetto + 1, Types.NUMERIC);
      mapParameterSource.addValue("ID_PIANO_DISTRETTO_OGUR", idPianoDistretto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_SPECIE_OGUR", idSpecie, Types.NUMERIC);
      mapParameterSource.addValue("PROGRESSIVO", progressivo, Types.NUMERIC);
      mapParameterSource.addValue("CENSITO", censito, Types.NUMERIC);
      mapParameterSource.addValue("PRELEVATO", prelevato, Types.NUMERIC);
      mapParameterSource.addValue("PERCENTUALE", percentuale, Types.NUMERIC);
      mapParameterSource.addValue("ESITO_CONTROLLO", esito, Types.VARCHAR);
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void insertDateCensimento(Long idPianoDistretto, Date data,
      Long metodo, BigDecimal valore, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String methodName = "insertDateCensimento";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String INSERT = " INSERT INTO IUF_T_DATE_CENS_OGUR (    \n"
        + "     ID_DATE_CENS_OGUR,                  \n"
        + "     ID_PIANO_DISTRETTO_OGUR,                  \n"
        + "     DATA_CENSIMENTO,                  \n"
        + "     ID_METODO_SPECIE,                  \n"
        + "     VALORE_METODO_CENSIMENTO,                \n"
        + "     DATA_AGGIORNAMENTO,                  \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO \n"
        + " ) VALUES (                                  \n"
        + "     :ID_DATE_CENS_OGUR,                  \n"
        + "     :ID_PIANO_DISTRETTO_OGUR,                  \n"
        + "     :DATA_CENSIMENTO,                  \n"
        + "     :ID_METODO_SPECIE,                  \n"
        + "     :VALORE_METODO_CENSIMENTO,                \n"
        + "     SYSDATE,                  \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO                \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      long seqIuffiTReferenteProgetto = 0;
      seqIuffiTReferenteProgetto = getNextSequenceValue(
          "SEQ_IUF_T_DATE_CENS_OGUR");
      mapParameterSource.addValue("ID_DATE_CENS_OGUR",
          seqIuffiTReferenteProgetto + 1, Types.NUMERIC);
      mapParameterSource.addValue("ID_PIANO_DISTRETTO_OGUR", idPianoDistretto,
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_CENSIMENTO", data, Types.DATE);
      mapParameterSource.addValue("ID_METODO_SPECIE", metodo, Types.NUMERIC);
      mapParameterSource.addValue("VALORE_METODO_CENSIMENTO", valore,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void insertConsistenzaAziendaleVegetale(
      LogOperationOggettoQuadroDTO logOperationOggQuadro,
      long idDatiProcedimento,
      long idConduzione, long idUtilizzo, long idUtilizzoDichiarato,
      String pubblica) throws InternalUnexpectedException
  {
    String methodName = "insertConsistenzaAziendaleVegetale";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_PUBBLICA_PARTICELLA (    \n"
        + "     ID_PUBBLICA_PARTICELLA,                  \n"
        + "     ID_DATI_PROCEDIMENTO,                  \n"
        + "     EXT_ID_CONDUZIONE_DICHIARATA,                  \n"
        + "     EXT_ID_UTILIZZO_DICHIARATO,                  \n"
        + "     FLAG_PUBBLICA                \n"
        + " ) VALUES (                                  \n"
        + "     :SEQ_IUF_T_PUBBLICA_PARTICELL,                  \n"
        + "     :ID_DATI_PROCEDIMENTO,                  \n"
        + "     :EXT_ID_CONDUZIONE_DICHIARATA,                  \n"
        + "     :EXT_ID_UTILIZZO_DICHIARATO,                  \n"
        + "     :FLAG_PUBBLICA                \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      long seqIuffiTReferenteProgetto = 0;
      seqIuffiTReferenteProgetto = getNextSequenceValue(
          "SEQ_IUF_T_PUBBLICA_PARTICELL");
      mapParameterSource.addValue("SEQ_IUF_T_PUBBLICA_PARTICELL",
          seqIuffiTReferenteProgetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DATI_PROCEDIMENTO", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_CONDUZIONE_DICHIARATA", idConduzione,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTILIZZO_DICHIARATO",
          idUtilizzoDichiarato, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_PUBBLICA", pubblica, Types.VARCHAR);
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void updateConsistenzaAziendaleVegetale(
      LogOperationOggettoQuadroDTO logOperationOggQuadro,
      long idDatiProcedimento, String pubblica)
      throws InternalUnexpectedException
  {
    String methodName = "updateConsistenzaAziendaleVegetale";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "UPDATE IUF_T_PUBBLICA_PARTICELLA SET FLAG_PUBBLICA = :FLAG_PUBBLICA WHERE ID_PUBBLICA_PARTICELLA = :ID_PUBBLICA_PARTICELLA";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      
      mapParameterSource.addValue("ID_PUBBLICA_PARTICELLA", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_PUBBLICA", pubblica, Types.VARCHAR);
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void updatePianoDistrettoOgur(long piano, int totaleC, int indetC,
      int totaleP, int indetP, BigDecimal max, String esito, long utente)
      throws InternalUnexpectedException
  {
    String methodName = "updatePianoDistrettoOgur";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "UPDATE IUF_T_PIANO_DISTRETTO_OGUR SET TOTALE_CENSITO = :TOTALE_CENSITO, INDETERMINATI_CENSITO = :INDETERMINATI_CENSITO, "
        + "TOTALE_PRELIEVO = :TOTALE_PRELIEVO, INDETERMINATI_PRELIEVO = :INDETERMINATI_PRELIEVO, MAX_CAPI_PRELIEVO = :MAX_CAPI_PRELIEVO, ESITO_TOTALE_PRELIEVO = :ESITO_TOTALE_PRELIEVO, "
        + "EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO WHERE ID_PIANO_DISTRETTO_OGUR = :ID_PIANO_DISTRETTO_OGUR";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {     
      mapParameterSource.addValue("ID_PIANO_DISTRETTO_OGUR", piano,
          Types.NUMERIC);
      mapParameterSource.addValue("TOTALE_CENSITO", totaleC, Types.NUMERIC);
      mapParameterSource.addValue("INDETERMINATI_CENSITO", indetC,
          Types.NUMERIC);
      mapParameterSource.addValue("TOTALE_PRELIEVO", totaleP, Types.NUMERIC);
      mapParameterSource.addValue("INDETERMINATI_PRELIEVO", indetP,
          Types.NUMERIC);
      mapParameterSource.addValue("MAX_CAPI_PRELIEVO", max, Types.NUMERIC);
      mapParameterSource.addValue("ESITO_TOTALE_PRELIEVO", esito,
          Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", utente,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void updateConsistenzaAziendaleAnimale(
      LogOperationOggettoQuadroDTO logOperationOggQuadro,
      long idDatiProcedimento, String pubblica)
      throws InternalUnexpectedException
  {
    String methodName = "updateConsistenzaAziendaleAnimale";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "UPDATE IUF_T_PUBBLICA_ALLEVAMENTO SET FLAG_PUBBLICA = :FLAG_PUBBLICA WHERE ID_PUBBLICA_ALLEVAMENTO = :ID_PUBBLICA_ALLEVAMENTO";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
     
      mapParameterSource.addValue("ID_PUBBLICA_ALLEVAMENTO", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_PUBBLICA", pubblica, Types.VARCHAR);
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void insertProdottiTrasformati(
      LogOperationOggettoQuadroDTO logOperationOggQuadro,
      long idDatiProcedimento, long idUtilizzoDichiarato, String pubblica)
      throws InternalUnexpectedException
  {
    String methodName = "insertConsistenzaAziendaleAnimale";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_PRODOTTO_TRASFORMATO (    \n"
        + "     ID_PRODOTTO_TRASFORMATO,                  \n"
        + "     ID_DATI_PROCEDIMENTO,                  \n"
        + "     ID_PRODOTTO,                  \n"
        + "     NOTE_BIOLOGICO                  \n"
        + " ) VALUES (                                  \n"
        + "     :SEQ_IUF_T_PRODOTTO_TRASFORMATO,                  \n"
        + "     :ID_DATI_PROCEDIMENTO,                  \n"
        + "     :ID_PRODOTTO,                  \n"
        + "     :NOTE_BIOLOGICO                  \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      long seqIuffiTReferenteProgetto = 0;
      seqIuffiTReferenteProgetto = getNextSequenceValue(
          "SEQ_IUF_T_PRODOTTO_TRASFORMA");
      mapParameterSource.addValue("SEQ_IUF_T_PRODOTTO_TRASFORMATO",
          seqIuffiTReferenteProgetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DATI_PROCEDIMENTO", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PRODOTTO", idUtilizzoDichiarato,
          Types.NUMERIC);
      mapParameterSource.addValue("NOTE_BIOLOGICO", pubblica, Types.VARCHAR);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void insertProduzioniCertificate(long idDatiProcedimento,
      Long idProduzioneCertificata, Long idProduzioneTradizionale,
      String flagBio) throws InternalUnexpectedException
  {
    String methodName = "insertProduzioniCertificate";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_PRODUZIONE_AZIENDA (    \n"
        + "     ID_PRODUZIONE_AZIENDA,                  \n"
        + "     ID_DATI_PROCEDIMENTO,                  \n"
        + "     ID_PRODUZIONE_CERTIFICATA,                  \n"
        + "     ID_PRODUZIONE_TRADIZIONALE,                  \n"
        + "     FLAG_BIO                  \n"
        + " ) VALUES (                                  \n"
        + "     :SEQ_IUF_T_PRODUZIONE_AZIENDA,                  \n"
        + "     :ID_DATI_PROCEDIMENTO,                  \n"
        + "     :ID_PRODUZIONE_CERTIFICATA,                  \n"
        + "     :ID_PRODUZIONE_TRADIZIONALE,                  \n"
        + "     :FLAG_BIO                  \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      long seqIuffiTReferenteProgetto = 0;
      seqIuffiTReferenteProgetto = getNextSequenceValue(
          "SEQ_IUF_T_PRODUZIONE_AZIENDA");
      mapParameterSource.addValue("SEQ_IUF_T_PRODUZIONE_AZIENDA",
          seqIuffiTReferenteProgetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DATI_PROCEDIMENTO", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PRODUZIONE_CERTIFICATA",
          idProduzioneCertificata, Types.NUMERIC);
      mapParameterSource.addValue("ID_PRODUZIONE_TRADIZIONALE",
          idProduzioneTradizionale, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_BIO", flagBio, Types.VARCHAR);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public long insertCanaliVendita(long idDatiProcedimento, 
      String altriCanali, String sitoWeb, String amazon, String orari,
      String indirizzo, String telefono,
      String email, String luogo, String info, String facebook,
      String instagram, String note, Long idImmagine)
      throws InternalUnexpectedException
  {
    String methodName = "insertCanaliVendita";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_CONTATTI (    \n"
        + "     ID_CONTATTI,                  \n"
        + "     ID_DATI_PROCEDIMENTO,                  \n"
        + "     ALTRO_CANALE_VENDITA,                  \n"
        + "     SITO_WEB,                  \n"
        + "     AMAZON,                  \n"
        + "     ORARI_APERTURA,                  \n"
        + "     INDIRIZZO_VENDITA,                  \n"
        + "     TELEFONO_VENDITA,                  \n"
        + "     EMAIL_VENDITA,                  \n"
        + "     DETTAGLI_MERCATI,                  \n"
        + "     COME_ARRIVARE,                  \n"
        + "     FACEBOOK,                  \n"
        + "     INSTAGRAM,                  \n"
        + "     NOTE,                  \n"
        + "     ID_IMG_AGRIQ                  \n"
        + " ) VALUES (                                  \n"
        + "     :SEQ_IUF_T_CONTATTI,                  \n"
        + "     :ID_DATI_PROCEDIMENTO,                  \n"
        + "     :ALTRO_CANALE_VENDITA,                  \n"
        + "     :SITO_WEB,                  \n"
        + "     :AMAZON,                  \n"
        + "     :ORARI_APERTURA,                  \n"
        + "     :INDIRIZZO_VENDITA,                  \n"
        + "     :TELEFONO_VENDITA,                  \n"
        + "     :EMAIL_VENDITA,                  \n"
        + "     :DETTAGLI_MERCATI,                  \n"
        + "     :COME_ARRIVARE,                  \n"
        + "     :FACEBOOK,                  \n"
        + "     :INSTAGRAM,                  \n"
        + "     :NOTE,                  \n"
        + "     :ID_IMG_AGRIQ                  \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      long seqIuffiTReferenteProgetto = 0;
      seqIuffiTReferenteProgetto = getNextSequenceValue("SEQ_IUF_T_CONTATTI");
      mapParameterSource.addValue("SEQ_IUF_T_CONTATTI",
          seqIuffiTReferenteProgetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DATI_PROCEDIMENTO", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ALTRO_CANALE_VENDITA", altriCanali,
          Types.VARCHAR);
      mapParameterSource.addValue("SITO_WEB", sitoWeb, Types.VARCHAR);
      mapParameterSource.addValue("AMAZON", amazon, Types.VARCHAR);
      mapParameterSource.addValue("ORARI_APERTURA", orari, Types.VARCHAR);
      mapParameterSource.addValue("INDIRIZZO_VENDITA", indirizzo,
          Types.VARCHAR);
      mapParameterSource.addValue("TELEFONO_VENDITA", telefono, Types.VARCHAR);
      mapParameterSource.addValue("EMAIL_VENDITA", email, Types.VARCHAR);
      mapParameterSource.addValue("DETTAGLI_MERCATI", luogo, Types.VARCHAR);
      mapParameterSource.addValue("COME_ARRIVARE", info, Types.VARCHAR);
      mapParameterSource.addValue("FACEBOOK", facebook, Types.VARCHAR);
      mapParameterSource.addValue("INSTAGRAM", instagram, Types.VARCHAR);
      mapParameterSource.addValue("NOTE", note, Types.VARCHAR);
      mapParameterSource.addValue("ID_IMG_AGRIQ", idImmagine, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      
      return seqIuffiTReferenteProgetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void insertCanaliContatti(long idDatiProcedimento, Long idImmagine)
      throws InternalUnexpectedException
  {
    String methodName = "insertCanaliContatti";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_R_CONTATTI_CANALE_VENDIT (    \n"
        + "     ID_CONTATTI,                  \n"
        + "     ID_CANALE_VENDITA                  \n"
        + " ) VALUES (                                  \n"
        + "     :ID_CONTATTI,                  \n"
        + "     :ID_CANALE_VENDITA                  \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONTATTI", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CANALE_VENDITA", idImmagine,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public VersamentoLicenzaDTO cercaIuv(String iuv, String cf, String cittadinanza)
      throws InternalUnexpectedException
  {
         String THIS_METHOD = "[" + THIS_CLASS + "::getImmagineDaVisualizzare]";
         if (logger.isDebugEnabled())
         {
           logger.debug(THIS_METHOD + " BEGIN.");
         }
    final String andCf = "a.codice_fiscale=:CF and a.FLAG_CITTADINO_ESTERO = 'N' and ";
    final String andEstero = "a.FLAG_CITTADINO_ESTERO = 'S' and ";
    String QUERY = "select a.codice_fiscale as cf, p.importo, p.data_pagamento_iuv as datapagamento, p.data_emissione_iuv as dataemissione, p.data_annullamento_iuv as dataannullamento, p.iuv, l.durata, l.descrizione\r\n" + 
        "from " + 
        "IUF_t_anagrafe_pesca a, " + 
        "IUF_r_pagamento_pesca p, " + 
        "IUF_d_tipo_licenza l " + 
        "where " + 
        "p.id_anagrafe_pesca=a.id_anagrafe_pesca and " + 
        "p.id_tipo_licenza=l.id_tipo_licenza and ";
    if(cittadinanza.equalsIgnoreCase("01")) {
      QUERY = QUERY + andCf;
    }
    else {
      QUERY = QUERY + andEstero;
    }   
    QUERY = QUERY + 
        "p.iuv=:IUV and " + 
        "p.data_annullamento_iuv is null and " + 
        "(p.data_pagamento_iuv is null or " + 
        " p.data_pagamento_iuv+l.durata >= sysdate) ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("IUV",
          iuv, Types.VARCHAR);
      if(cittadinanza.equalsIgnoreCase("01")) {
        mapParameterSource.addValue("CF",
            cf, Types.VARCHAR);
      }
      return queryForObject(QUERY, mapParameterSource,
          VersamentoLicenzaDTO.class);
    }
    catch (Exception e)
    {
      InternalUnexpectedException ex = new InternalUnexpectedException(e,
          new LogParameter[]
          {},
          new LogVariable[]
          {}, QUERY, mapParameterSource);
      logInternalUnexpectedException(ex, THIS_METHOD);
      throw ex;
    }
  }
  
  public String cercaAnagrafica(String cf)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::cercaAnagrafica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "select a.codice_fiscale from IUF_t_anagrafe_pesca a where a.codice_fiscale = :CF";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("CF",
          cf, Types.VARCHAR);

      return queryForString(QUERY, mapParameterSource);
    }
    catch (Exception e)
    {
      InternalUnexpectedException ex = new InternalUnexpectedException(e,
          new LogParameter[]
          {},
          new LogVariable[]
          {}, QUERY, mapParameterSource);
      logInternalUnexpectedException(ex, THIS_METHOD);
      throw ex;
    }
  }

  public ContenutoFileAllegatiDTO getImmagineDaVisualizzare(
      long idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImmagineDaVisualizzare]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
         final String QUERY =   " SELECT                                                      \n"
                    + "     PA.IMMAGINE AS CONTENUTO                                \n"
                    + " FROM                                                        \n"
                    + "     IUF_D_PROCEDIMENTO_AGRICOLO PA                        \n"
                    + " WHERE                                                       \n"
                    + "     PA.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO \n";

         MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
         try
         {
           mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO",
               idProcedimentoAgricolo, Types.NUMERIC);

      return queryForObject(QUERY, mapParameterSource,
          ContenutoFileAllegatiDTO.class);
    }
    catch (Exception e)
           {
             InternalUnexpectedException ex = new InternalUnexpectedException(e,
                 new LogParameter[]
                 {},
                 new LogVariable[]
                 {}, QUERY, mapParameterSource);
             logInternalUnexpectedException(ex, THIS_METHOD);
             throw ex;
           }
         }
   
  public void insertConsistenzaAziendaleAnimale(
      LogOperationOggettoQuadroDTO logOperationOggQuadro,
      long idDatiProcedimento,
      long idConduzione, long idUtilizzo, long idUtilizzoDichiarato,
      String pubblica) throws InternalUnexpectedException
  {
    String methodName = "insertConsistenzaAziendaleAnimale";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_PUBBLICA_ALLEVAMENTO (    \n"
        + "     ID_PUBBLICA_ALLEVAMENTO,                  \n"
        + "     ID_DATI_PROCEDIMENTO,                  \n"
        + "     EXT_ID_ALLEVAMENTO,                  \n"
        + "     EXT_ID_SPECIE_ANIMALE,                  \n"
        + "     EXT_ID_CATEGORIA_ANIMALE,                  \n"
        + "     FLAG_PUBBLICA                \n"
        + " ) VALUES (                                  \n"
        + "     :SEQ_IUF_T_PUBBLICA_ALLEVAMEN,                  \n"
        + "     :ID_DATI_PROCEDIMENTO,                  \n"
        + "     :EXT_ID_ALLEVAMENTO,                  \n"
        + "     :EXT_ID_SPECIE_ANIMALE,                  \n"
        + "     :EXT_ID_CATEGORIA_ANIMALE,                  \n"
        + "     :FLAG_PUBBLICA                \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      long seqIuffiTReferenteProgetto = 0;
      seqIuffiTReferenteProgetto = getNextSequenceValue(
          "SEQ_IUF_T_PUBBLICA_ALLEVAMEN");
      mapParameterSource.addValue("SEQ_IUF_T_PUBBLICA_ALLEVAMEN",
          seqIuffiTReferenteProgetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DATI_PROCEDIMENTO", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_ALLEVAMENTO", idConduzione,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_SPECIE_ANIMALE", idUtilizzo,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_CATEGORIA_ANIMALE",
          idUtilizzoDichiarato, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_PUBBLICA", pubblica, Types.VARCHAR);
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
  
  public void insertIuffiTAziendaBioFiliere(
      long idAzienda, long idMetodi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
      throws InternalUnexpectedException
  {
    String methodName = "insertIuffiTAziendaBio";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_R_AZIENDA_BIO_FILIERA_PR (    \n"
        + "     ID_AZIENDA_BIO,                  \n"
        + "     ID_FILIERA_PRODUTTIVA                \n"
        + " ) VALUES (                                  \n"
        + "     :ID_AZIENDA_BIO,                              \n"
        + "     :ID_FILIERA_PRODUTTIVA                             \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_AZIENDA_BIO",
          idAzienda, Types.NUMERIC);
      mapParameterSource.addValue("ID_FILIERA_PRODUTTIVA",
          idMetodi, Types.NUMERIC);
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
             
              new LogParameter("ID_AZIENDA_BIO",
                  idAzienda),
              new LogParameter("ID_FILIERA_PRODUTTIVA", idMetodi),

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
  
  public void insertIuffiTAziendaBioMetodiColtivazione(
      long idAzienda, long idMetodi,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
      throws InternalUnexpectedException
  {
    String methodName = "insertIuffiTAziendaBio";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_R_AZIENDA_BIO_METODO_COL(    \n"
        + "     ID_AZIENDA_BIO,                  \n"
        + "     ID_METODO_COLTIVAZIONE                \n"
        + " ) VALUES (                                  \n"
        + "     :ID_AZIENDA_BIO,                              \n"
        + "     :ID_METODO_COLTIVAZIONE                             \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_AZIENDA_BIO",
          idAzienda, Types.NUMERIC);
      mapParameterSource.addValue("ID_METODO_COLTIVAZIONE",
          idMetodi, Types.NUMERIC);
      
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
             
              new LogParameter("ID_AZIENDA_BIO",
                  idAzienda),
              new LogParameter("ID_METODO_COLTIVAZIONE", idMetodi),

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
  
  public int updateCaratterisicheAziendali(long azienda, String denominazione,
      String descodc, String desccatattiva, String descrizione,
      String altrotipoattivita,
      String altrafiliera,
      String altrafunz, String desctrasformazione,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateCaratterisicheAziendali]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                                        \n"
        + "   IUF_T_AZIENDA_BIO                              \n"
        + " SET                                                           \n"
        + "   DENOMINAZIONE              = :DENOMINAZIONE,              \n"
        + "   DESCRIZIONE = :DESCRIZIONE, \n"
        + "   ALTRO_TIPO_ATTIVITA              = :ALTRO_TIPO_ATTIVITA,              \n"
        + "   ALTRA_FILIERA_PRODUTTIVA              = :ALTRA_FILIERA_PRODUTTIVA,              \n"
        + "   ALTRA_MULTIFUNZIONALITA      = :ALTRA_MULTIFUNZIONALITA,       \n"
        + "   DESC_TRASFORMAZIONE_PRODOTTI      = :DESC_TRASFORMAZIONE_PRODOTTI       \n"
        + " WHERE                                                         \n"
        + "   ID_AZIENDA_BIO = :ID_AZIENDA_BIO          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("DENOMINAZIONE",
          denominazione, Types.VARCHAR);
      mapParameterSource.addValue("DESCRIZIONE",
          descrizione, Types.VARCHAR);
      mapParameterSource.addValue("ALTRO_TIPO_ATTIVITA",
          altrotipoattivita, Types.VARCHAR);
      mapParameterSource.addValue("ALTRA_FILIERA_PRODUTTIVA",
          altrafiliera, Types.VARCHAR);
      mapParameterSource.addValue("ALTRA_MULTIFUNZIONALITA",
          altrafunz, Types.VARCHAR);
      mapParameterSource.addValue("DESC_TRASFORMAZIONE_PRODOTTI",
          desctrasformazione, Types.VARCHAR);
      mapParameterSource.addValue("ID_AZIENDA_BIO",
          azienda, Types.NUMERIC);

      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("controlliInLocoInvestDTO",
                  null),
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

  public long insertIuffiTAziendaBio(
      long idDatiProcedimento, String denominazione,
      String descodc, String desccatattiva, String descrizione,
      String altrotipoattivita,
      String altrafiliera,
      String altrafunz, String desctrasformazione,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadro)
      throws InternalUnexpectedException
  {
    String methodName = "insertIuffiTAziendaBio";
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_AZIENDA_BIO (    \n"
        + "     ID_AZIENDA_BIO,                  \n"
        + "     ID_DATI_PROCEDIMENTO,                \n"
        + "     DENOMINAZIONE,                                   \n"
        + "     DESCRIZIONE,                                \n"
        + "     EXT_COD_UE_ODC,                         \n"
        + "     ALTRO_TIPO_ATTIVITA,                                    \n"
        + "     ALTRA_FILIERA_PRODUTTIVA,                       \n"
        + "     ALTRA_MULTIFUNZIONALITA,                               \n"
        + "     DESC_TRASFORMAZIONE_PRODOTTI                              \n"
        + " ) VALUES (                                  \n"
        + "     :SEQ_IUF_T_AZIENDA_BIO, \n"
        + "     :ID_DATI_PROCEDIMENTO,               \n"
        + "     :DENOMINAZIONE,                           \n"
        + "     :DESCRIZIONE,                        \n"
        + "     :EXT_COD_UE_ODC,                        \n"
        + "     :ALTRO_TIPO_ATTIVITA,                                   \n"
        + "     :ALTRA_FILIERA_PRODUTTIVA,                      \n"
        + "     :ALTRA_MULTIFUNZIONALITA,                              \n"
        + "     :DESC_TRASFORMAZIONE_PRODOTTI                             \n"
        + " )                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTReferenteProgetto = 0;
    try
    {
      seqIuffiTReferenteProgetto = getNextSequenceValue(
          "SEQ_IUF_T_AZIENDA_BIO");
      mapParameterSource.addValue("SEQ_IUF_T_AZIENDA_BIO",
          seqIuffiTReferenteProgetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DATI_PROCEDIMENTO",
          idDatiProcedimento, Types.NUMERIC);
      mapParameterSource.addValue("DENOMINAZIONE", denominazione,
          Types.VARCHAR);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      mapParameterSource.addValue("EXT_COD_UE_ODC", descodc,
          Types.VARCHAR);
      mapParameterSource.addValue("ALTRO_TIPO_ATTIVITA", altrotipoattivita,
          Types.VARCHAR);
      mapParameterSource.addValue("ALTRA_FILIERA_PRODUTTIVA", altrafiliera,
          Types.VARCHAR);
      mapParameterSource.addValue("ALTRA_MULTIFUNZIONALITA", altrafunz,
          Types.VARCHAR);
      mapParameterSource.addValue("DESC_TRASFORMAZIONE_PRODOTTI",
          desctrasformazione, Types.VARCHAR);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return seqIuffiTReferenteProgetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("SEQ_IUF_T_REFERENTE_PROGETTO",
                  seqIuffiTReferenteProgetto),
              new LogParameter("ID_DATI_PROCEDIMENTO",
                  idDatiProcedimento),
              new LogParameter("DENOMINAZIONE", denominazione),
              new LogParameter("DESCRIZIONE", descrizione),
              new LogParameter("EXT_COD_UE_ODC", descodc),
              new LogParameter("ALTRO_TIPO_ATTIVITA", altrotipoattivita),
              new LogParameter("ALTRA_FILIERA_PRODUTTIVA", altrafiliera),
              new LogParameter("ALTRA_MULTIFUNZIONALITA", altrafunz),
              new LogParameter("DESC_TRASFORMAZIONE_PRODOTTI",
                  desctrasformazione),
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

//  public void updateCaratteristicheAziendali(
//      long idProcedimentoOggetto, String denominazione,
  // String descodc, String desccatattiva, String descrizione, String
  // altrotipoattivita,
//      String altrafiliera,
//      String altrafunz, String desctrasformazione,
  // LogOperationOggettoQuadroDTO logOperationOggettoQuadro) throws
  // InternalUnexpectedException
//  {
//    String methodName = new Object()
//    {
//    }.getClass().getEnclosingMethod().getName();
//    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
//    if (logger.isDebugEnabled())
//    {
//      logger.debug(THIS_METHOD + " BEGIN.");
//    }
//    final String UPDATE = " UPDATE IUF_T_REFERENTE_PROGETTO                      \n"
//        + " SET                                                    \n"
//        + "     NOME = UPPER(:NOME),                               \n"
//        + "     COGNOME = UPPER(:COGNOME),                         \n"
//        + "     CODICE_FISCALE = :CODICE_FISCALE,                  \n"
//        + "     CAP = :CAP,                                        \n"
//        + "     EXT_ISTAT_COMUNE = :EXT_ISTAT_COMUNE,              \n"
//        + "     TELEFONO = :TELEFONO,                              \n"
//        + "     CELLULARE = :CELLULARE,                            \n"
//        + "     EMAIL = :EMAIL                                     \n"
//        + " WHERE                                                  \n"
//        + "     ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n";
//    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
//    try
//    {
//      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
//          idProcedimentoOggetto, Types.NUMERIC);
//      mapParameterSource.addValue("NOME", nome, Types.VARCHAR);
//      mapParameterSource.addValue("COGNOME", cognome, Types.VARCHAR);
//      mapParameterSource.addValue("CODICE_FISCALE", codiceFiscale,
//          Types.VARCHAR);
//      mapParameterSource.addValue("CAP", cap, Types.VARCHAR);
//      mapParameterSource.addValue("EXT_ISTAT_COMUNE", comune, Types.VARCHAR);
//      mapParameterSource.addValue("TELEFONO", telefono, Types.VARCHAR);
//      mapParameterSource.addValue("CELLULARE", cellulare, Types.VARCHAR);
//      mapParameterSource.addValue("EMAIL", email, Types.VARCHAR);
//      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
//    }
//    catch (Throwable t)
//    {
//      InternalUnexpectedException e = new InternalUnexpectedException(t,
//          new LogParameter[]
//          {
//              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
//                  idProcedimentoOggetto),
//              new LogParameter("NOME", nome),
//              new LogParameter("COGNOME", cognome),
//              new LogParameter("CODICE_FISCALE", codiceFiscale),
//              new LogParameter("CAP", cap),
//              new LogParameter("EXT_ISTAT_COMUNE", comune),
//              new LogParameter("TELEFONO", telefono),
//              new LogParameter("CELLULARE", cellulare),
//              new LogParameter("EMAIL", email)
//          },
//          new LogVariable[]
//          {}, UPDATE, mapParameterSource);
//      logInternalUnexpectedException(e, THIS_METHOD);
//      throw e;
//    }
//    finally
//    {
//      if (logger.isDebugEnabled())
//      {
//        logger.debug(THIS_METHOD + " END.");
//      }
//    }
//
//  }
  
  public String getNomeTabellaByIdDanno(Integer idDanno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT NOME_TABELLA FROM IUF_D_DANNO WHERE ID_DANNO = :ID_DANNO";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DANNO", idDanno, Types.NUMERIC);
      return queryForString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_DANNO", idDanno) },
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

  public DannoDaFaunaDTO getDatiIdentificativiDanniDaFauna(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                    \n"
        + "     TDDF.DATA_DANNO,                                                      \n"
        + "     VDA.DESCRIZIONE_COMUNE         AS COMUNE,                 			   \n"
        + "     VDA.DESCRIZIONE_PROVINCIA      AS PROVINCIA,                          \n"
        + "     VDA.ISTAT_PROVINCIA            AS ID_ISTAT_PROVINCIA, 	 			   \n"
        + "     VDA.ISTAT_COMUNE               AS ID_ISTAT_COMUNE, 				   \n"
        + "     DIDF.ID_ISTITUTO_DANNI_FAUNA   AS ID_ISTITUTO,                        \n"
        + "     DIDF.DESCRIZIONE               AS ISTITUTO,                           \n"
        + "     DIN.ID_ISTITUTO_NOMINATIVO AS ID_NOMINATIVO,                                                  \n"
        + "     DIN.DESCRIZIONE AS NOMINATIVO,                                                     \n"
        + "     TDDF.NOMIN_ISTITUTO_ALTRO AS NOMINATIVO_ALTRO,                                                     \n"
        + "     TDDF.FLAG_URGENZA_PERIZIA      AS URGENZA_PERIZIA,                    \n"
        + "     TDDF.NOTA_URGENZA,                        \n"
        + "     TDDF.NOTE                      AS NOTE,                               \n"
        + "     TDDF.ID_MOTIVO_URGENZA,                    \n"
        + "     (SELECT DESCRIZIONE FROM IUF_D_MOTIVO_URGENZA WHERE TDDF.ID_MOTIVO_URGENZA = ID_MOTIVO_URGENZA) AS MOTIVAZIONE,                    \n"
        + "     TDDF.ID_PROCEDIMENTO_OGGETTO					                       \n"
        + " FROM                                                                      \n"
        + "     IUF_T_DATI_DANNO_FAUNA TDDF,                                        \n"
        + "     SMRGAA_V_DATI_AMMINISTRATIVI VDA,                                     \n"
        + "     IUF_D_ISTITUTO_DANNI_FAUNA DIDF,                                     \n"
        + "     IUF_D_ISTITUTO_NOMINATIVO DIN                                     \n"
        + " WHERE                                                                     \n"
        + "     TDDF.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO               \n"
        + "     AND TDDF.EXT_ISTAT_COMUNE = VDA.ISTAT_COMUNE                          \n"
        + "     AND DIDF.ID_ISTITUTO_DANNI_FAUNA = TDDF.ID_ISTITUTO_DANNI_FAUNA       \n"
        + "     AND TDDF.ID_ISTITUTO_NOMINATIVO = DIN.ID_ISTITUTO_NOMINATIVO (+)     \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DannoDaFaunaDTO>()
          {
            @Override
            public DannoDaFaunaDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              DannoDaFaunaDTO danno = null;
              if (rs.next())
              {
                danno = new DannoDaFaunaDTO();
                danno.setDataDanno(rs.getDate("DATA_DANNO"));
                danno.setComune(rs.getString("COMUNE"));
                danno.setProvincia(rs.getString("PROVINCIA"));
                Long idIstituto = rs.getLong("ID_ISTITUTO");
                danno.setIdIstituto(idIstituto);
                danno.setIstituto(rs.getString("ISTITUTO"));
                danno.setIdNominativo(rs.getLong("ID_NOMINATIVO"));
                String nominativo = rs.getString("NOMINATIVO");
                if ((idIstituto != null && idIstituto == 4L)
                    || "Altro".equalsIgnoreCase(nominativo))
                {
                  nominativo = rs.getString("NOMINATIVO_ALTRO");
                }
                danno.setNominativo(nominativo);
                danno.setUrgenzaPerizia(
                    (rs.getString("URGENZA_PERIZIA").equals("S") ? true
                        : false));
                danno.setMotivazione(rs.getString("MOTIVAZIONE"));
                if (rs.getBigDecimal("ID_MOTIVO_URGENZA") != null)
                {
                  danno.setIdMotivoUrgenza(rs.getLong("ID_MOTIVO_URGENZA"));
                }
                danno.setIdProcedimentoOggetto(
                    rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
                danno.setIdIstatProvincia(rs.getString("ID_ISTAT_PROVINCIA"));
                danno.setIdIstatComune(rs.getString("ID_ISTAT_COMUNE"));
                danno.setNote(rs.getString("NOTE"));
                danno.setNoteUrgenza(rs.getString("NOTA_URGENZA"));
              }

              return danno;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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

  public void updateDatiIdentificativiDanniDaFauna(DannoDaFaunaDTO danno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE IUF_T_DATI_DANNO_FAUNA                          \n"
        + " SET                                                      \n"
        + "     DATA_DANNO = :DATA_DANNO,                            \n"
        + "     ID_ISTITUTO_DANNI_FAUNA = :ID_ISTITUTO_DANNI_FAUNA,  \n"
        + "     EXT_ISTAT_COMUNE = :EXT_ISTAT_COMUNE,                \n"
        + "     FLAG_URGENZA_PERIZIA = :FLAG_URGENZA_PERIZIA,        \n"
        + "     NOTA_URGENZA = :NOTA_URGENZA,          \n"
        + "     NOTE = :NOTE,                                        \n"
        + "     ID_ISTITUTO_NOMINATIVO = :ID_ISTITUTO_NOMINATIVO,    \n"
        + "     NOMIN_ISTITUTO_ALTRO = :NOMIN_ISTITUTO_ALTRO,         \n"
        + "     ID_MOTIVO_URGENZA = :ID_MOTIVO_URGENZA         \n"
        + " WHERE                                                    \n"
        + "     ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          danno.getIdProcedimentoOggetto(), Types.NUMERIC);
      mapParameterSource.addValue("DATA_DANNO", danno.getDataDanno(),
          Types.DATE);
      mapParameterSource.addValue("ID_ISTITUTO_DANNI_FAUNA",
          danno.getIdIstituto(), Types.NUMERIC);
      mapParameterSource.addValue("EXT_ISTAT_COMUNE", danno.getIdIstatComune(),
          Types.VARCHAR);
      mapParameterSource.addValue("FLAG_URGENZA_PERIZIA",
          danno.getUrgenzaPerizia() == true ? "S" : "N", Types.VARCHAR);
      mapParameterSource.addValue("NOTA_URGENZA", danno.getNoteUrgenza(),
          Types.VARCHAR);
      mapParameterSource.addValue("NOTE", danno.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("ID_ISTITUTO_NOMINATIVO",
          danno.getIdNominativo(),
          Types.NUMERIC);
      mapParameterSource.addValue("NOMIN_ISTITUTO_ALTRO", danno.getNominativo(),
          Types.VARCHAR);
      mapParameterSource.addValue("ID_MOTIVO_URGENZA",
          danno.getIdMotivoUrgenza(),
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {

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

  public void insertDatiIdentificativiDanniDaFauna(DannoDaFaunaDTO danno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " INSERT INTO IUF_T_DATI_DANNO_FAUNA ( \n"
        + "     ID_DATI_DANNO_FAUNA,               \n"
        + "     ID_PROCEDIMENTO_OGGETTO,           \n"
        + "     DATA_DANNO,                        \n"
        + "     ID_ISTITUTO_DANNI_FAUNA,           \n"
        + "     EXT_ISTAT_COMUNE,                  \n"
        + "     FLAG_URGENZA_PERIZIA,              \n"
        + "     NOTA_URGENZA,               \n"
        + "     NOTE,                              \n"
        + "     ID_ISTITUTO_NOMINATIVO,                 \n"
        + "     NOMIN_ISTITUTO_ALTRO,                 \n"
        + "     ID_MOTIVO_URGENZA                 \n"
        + " ) VALUES (                             \n"
        + "     :ID_DATI_DANNO_FAUNA,              \n"
        + "     :ID_PROCEDIMENTO_OGGETTO,         \n"
        + "     :DATA_DANNO,                       \n"
        + "     :ID_ISTITUTO_DANNI_FAUNA,          \n"
        + "     :EXT_ISTAT_COMUNE,                 \n"
        + "     :FLAG_URGENZA_PERIZIA,             \n"
        + "     :NOTA_URGENZA,              \n"
        + "     :NOTE,                             \n"
        + "     :ID_ISTITUTO_NOMINATIVO,                \n"
        + "     :NOMIN_ISTITUTO_ALTRO,                \n"
        + "     :ID_MOTIVO_URGENZA                \n"
        + " )                                      \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DATI_DANNO_FAUNA",
          getNextSequenceValue("SEQ_IUF_T_DATI_DANNO_FAUNA"), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          danno.getIdProcedimentoOggetto(), Types.NUMERIC);
      mapParameterSource.addValue("DATA_DANNO", danno.getDataDanno(),
          Types.DATE);
      mapParameterSource.addValue("ID_ISTITUTO_DANNI_FAUNA",
          danno.getIdIstituto(), Types.NUMERIC);
      mapParameterSource.addValue("EXT_ISTAT_COMUNE", danno.getIdIstatComune(),
          Types.VARCHAR);
      mapParameterSource.addValue("FLAG_URGENZA_PERIZIA",
          danno.getUrgenzaPerizia() == true ? "S" : "N", Types.VARCHAR);
      mapParameterSource.addValue("NOTA_URGENZA", danno.getNoteUrgenza(),
          Types.VARCHAR);
      mapParameterSource.addValue("NOTE", danno.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("ID_ISTITUTO_NOMINATIVO",
          danno.getIdNominativo(),
          Types.NUMERIC);
      mapParameterSource.addValue("NOMIN_ISTITUTO_ALTRO", danno.getNominativo(),
          Types.VARCHAR);
      mapParameterSource.addValue("ID_MOTIVO_URGENZA",
          danno.getIdMotivoUrgenza(),
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {

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

  public List<IstitutoDTO> getListaIstitutiDanniFauna(long idAmmCompetenza)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getListaIstitutiDanniFauna]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                            \n"
        + "     IDF.ID_ISTITUTO_DANNI_FAUNA   AS ID_ISTITUTO,                 \n"
        + "     IDF.DESCRIZIONE                                               \n"
        + " FROM                                                              \n"
        + "     IUF_D_ISTITUTO_DANNI_FAUNA IDF,                             \n"
        + "     IUF_R_ISTITUTO_TIPO_AMM ITA                                 \n"
        + " WHERE                                                             \n"
        + "     IDF.DATA_FINE_VALIDITA IS NULL                                \n"
        + "     AND IDF.ID_ISTITUTO_DANNI_FAUNA = ITA.ID_ISTITUTO_DANNI_FAUNA \n"
        + "     AND EXT_ID_TIPO_AMM IN (                                      \n"
        + "         SELECT                                                    \n"
        + "             TIPO_AMMINISTRAZIONE                                  \n"
        + "         FROM                                                      \n"
        + "             SMRCOMUNE_V_AMM_COMPETENZA                            \n"
        + "         WHERE                                                     \n"
        + "             ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA                \n"
        + "     )                                                             \n"
        + " ORDER BY                                                          \n"
        + "     IDF.DESCRIZIONE                                               \n";
    try
    {

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza,
          Types.NUMERIC);

      return queryForList(QUERY, mapParameterSource, IstitutoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idAmmCompetenza", idAmmCompetenza) },
          new LogVariable[]
          {}, QUERY,
          new MapSqlParameterSource());
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

  public List<IstitutoDTO> getListaNominativiDanniFauna(long idIstitutoDF)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getListaNominativiDanniFauna]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                            \n"
        + "     IDF.ID_ISTITUTO_NOMINATIVO   AS ID_ISTITUTO,                 \n"
        + "     IDF.DESCRIZIONE                                               \n"
        + " FROM                                                              \n"
        + "     IUF_D_ISTITUTO_NOMINATIVO IDF                               \n"
        + " WHERE                                                             \n"
        + "     IDF.DATA_FINE_VALIDITA IS NULL                                \n"
        + " AND IDF.ID_ISTITUTO_DANNI_FAUNA = :ID_ISTITUTO_DANNI_FAUNA        \n"
        + " ORDER BY                                                          \n"
        + "     IDF.DESCRIZIONE                                               \n";
    try
    {

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_ISTITUTO_DANNI_FAUNA", idIstitutoDF,
          Types.NUMERIC);

      return queryForList(QUERY, mapParameterSource, IstitutoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idIstitutoDF", idIstitutoDF) },
          new LogVariable[]
          {}, QUERY,
          new MapSqlParameterSource());
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

  public List<DecodificaDTO<Long>> getListaMotiviUrgenza(Date dataValidita)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getListaMotiviUrgenza]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    final String QUERY = " SELECT                                                            \n"
        + "   ID_MOTIVO_URGENZA AS ID,                 \n"
        + "   ID_MOTIVO_URGENZA AS CODICE,                 \n"
        + "   DESCRIZIONE                                               \n"
        + " FROM                                                              \n"
        + "   IUF_D_MOTIVO_URGENZA                               \n"
        + " WHERE                                                             \n"
        + "   TRUNC(NVL(:DATA_VALIDITA,SYSDATE)) BETWEEN DATA_INIZIO_VALIDITA AND NVL(DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY'))    \n"
        + " ORDER BY                                                          \n"
        + "   DESCRIZIONE                                               \n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("DATA_VALIDITA", dataValidita, Types.DATE);
      
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {  },
              new LogVariable[]
                  {}, QUERY,
                  new MapSqlParameterSource());
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

  public String getDecodificaMotivoUrgenza(long idMotivoUrgenza)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDecodificaMotivoUrgenza]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    final String QUERY = " SELECT                                                            \n"
        + "   DESCRIZIONE                                               \n"
        + " FROM                                                              \n"
        + "   IUF_D_MOTIVO_URGENZA                               \n"
        + " WHERE                                                             \n"
        + "   ID_MOTIVO_URGENZA = :ID_MOTIVO_URGENZA                           \n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_MOTIVO_URGENZA", idMotivoUrgenza,
          Types.NUMERIC);
      
      return queryForString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {  },
              new LogVariable[]
                  {}, QUERY,
                  new MapSqlParameterSource());
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

  public Integer getCountDanniFauna(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getCountDanniFauna]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                      \n"
        + "     COUNT(*)                                                \n"
        + " FROM                                                        \n"
        + "     IUF_T_DATI_DANNO_FAUNA TDDF                           \n"
        + " WHERE                                                       \n"
        + "     TDDF.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.queryForInt(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
          {}, QUERY,
          new MapSqlParameterSource());
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

  public void eliminaParticelleFauna(long idProcedimentoOggetto,
      long[] idsDannoFauna) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = " DELETE FROM IUF_T_PARTICELLE_FAUNA                        \n"
        + " WHERE ID_DANNO_FAUNA IN                                     \n"
        + "  (  SELECT ID_DANNO_FAUNA                                   \n"
        + "     FROM IUF_T_DANNO_FAUNA                                \n"
        + "     WHERE                            						\n"
        + "     ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 		\n"
        + getInCondition("ID_DANNO_FAUNA", idsDannoFauna)
        + " )"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idsDannoFauna", idsDannoFauna),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  public void eliminaDannoFauna(long idProcedimentoOggetto,
      long[] idsDannoFauna) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = " DELETE FROM IUF_T_DANNO_FAUNA                          \n"
        + " WHERE                                                  \n"
        + "     ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + getInCondition("ID_DANNO_FAUNA", idsDannoFauna);

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idsDannoFauna", idsDannoFauna),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
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

  private String queryDanniFaunaDettaglio = " WITH TMP_CONDUZIONE_UTILIZZO AS                                         \n"
      + " (                                                                           \n"
      + "     SELECT                                                                  \n"
      + "         CU.COMUNE AS ISTAT_COMUNE,                                          \n"
      + "         CU.DESC_COMUNE,                                                     \n"
      + "         CU.DESC_PROVINCIA,                                                  \n"
      + "         CU.SIGLA_PROVINCIA,                                                 \n"
      + "         CU.ID_UTILIZZO_DICHIARATO,                                          \n"
      + "         CU.ID_DICHIARAZIONE_CONSISTENZA,                                    \n"
      + "         CU.SEZIONE,                                                         \n"
      + "         CU.FOGLIO,                                                          \n"
      + "         CU.SUBALTERNO,                                                      \n"
      + "         CU.ID_UTILIZZO,                                                     \n"
      + "         CU.PARTICELLA,                                                      \n"
      + "         CU.DESC_TIPO_UTILIZZO,                                              \n"
      + "         CU.COD_TIPO_UTILIZZO,                                               \n"
      + "         '[' || CU.COD_TIPO_UTILIZZO || '] ' ||  CU.DESC_TIPO_UTILIZZO AS UTILIZZO,      \n"
      + "         NVL2(CU.COD_TIPO_UTILIZZO_SECONDARIO, '[' || CU.COD_TIPO_UTILIZZO_SECONDARIO || '] ' ||  CU.DESC_TIPO_UTILIZZO_SECONDARIO, '') AS UTILIZZO_SECONDARIO,      \n"
      + "         CU.DESC_ZONA_ALTIMETRICA,                                           \n"
      + "         CU.ID_ZONA_ALTIMETRICA,                                             \n"
      + "         CU.SUP_CATASTALE,                                                   \n"
      + "         CU.SUPERFICIE_UTILIZZATA,                                           \n"
      + "         CU.SUP_UTILIZZATA_SECONDARIA,                                       \n"
      + "         PO.ID_PROCEDIMENTO_OGGETTO                                          \n"
      + "     FROM                                                                    \n"
      + "         SMRGAA_V_CONDUZIONE_UTILIZZO CU,                                    \n"
      + "         IUF_T_PROCEDIMENTO_OGGETTO PO                                     \n"
      + "     WHERE                                                                   \n"
      + "         CU.ID_DICHIARAZIONE_CONSISTENZA = PO.EXT_ID_DICHIARAZIONE_CONSISTEN \n"
      + "         AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO           \n"
      + " )                                                                           \n";

  ;

  public List<DannoFaunaDTO> getListaDanniFaunaDettaglio(
      long idProcedimentoOggetto, long[] idsDannoFauna, boolean onlyLocalizzati)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String query = queryDanniFaunaDettaglio +
        " SELECT                                                                \n"
        + "     DC.*,                                                             \n"
        + "     PF.EXT_ID_UTILIZZO_DICHIARATO,                                    \n"
        + "     PF.SUPERFICIE_DANNEGGIATA,                                        \n"
        + "     PF.FLAG_UTILIZZO_SEC,                                        \n"
        + "     SF.ID_SPECIE_FAUNA,                                               \n"
        + "     SF.DESCRIZIONE AS DESC_SPECIE_FAUNA,                              \n"
        + "     DF.PROGRESSIVO,						                             \n"
        + "     DF.ID_DANNO_FAUNA,					                             \n"
        + "     DF.ULTERIORI_INFORMAZIONI,			                             \n"
        + "     DF.QUANTITA,							                             \n"
        + "     UM.DESCRIZIONE AS DESC_UNITA_MISURA,							     \n"
        + "     SF.ID_SPECIE_FAUNA,											     \n"
        + "     SF.DESCRIZIONE AS DESC_SPECIE_FAUNA,							     \n"
        + "     TDF.ID_TIPO_DANNO_FAUNA,                                          \n"
        + "     TDF.DESCRIZIONE AS DESC_TIPO_DANNO_FAUNA,                          \n"
        + "     PF.EXT_ID_UTILIZZO AS ID_UTILIZZO_RISCONTRATO,                           \n"
        + "     DECODE(PF.EXT_ID_UTILIZZO, NULL, NULL, (SELECT M.DESCRIZIONE_UTILIZZO FROM SMRGAA_V_MATRICE M WHERE M.ID_UTILIZZO = PF.EXT_ID_UTILIZZO AND ROWNUM=1)) AS DESCRIZIONE_UTILIZZO_RISC \n"
        + " FROM                                                                  \n"
        + "     IUF_T_DANNO_FAUNA DF,                                           \n"
        + "     IUF_R_DANNO_SPECIE DS,                                          \n"
        + "     IUF_D_SPECIE_FAUNA SF,                                          \n"
        + "     IUF_D_TIPO_DANNO_FAUNA TDF,                                     \n"
        + "     IUF_D_UNITA_MISURA UM,                                          \n"
        + "     IUF_T_PARTICELLE_FAUNA PF,                                      \n"
        + "     TMP_CONDUZIONE_UTILIZZO DC                                    \n"
        + " WHERE                                                                 \n"
        + "     DF.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO             \n"
        + "     AND TDF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA                       \n"
        + "     AND DF.ID_DANNO_SPECIE = DS.ID_DANNO_SPECIE                       \n"
        + "     AND DS.ID_TIPO_DANNO_FAUNA = TDF.ID_TIPO_DANNO_FAUNA              \n"
        + "     AND DS.ID_SPECIE_FAUNA = SF.ID_SPECIE_FAUNA                       \n"
        + "     AND DF.ID_DANNO_FAUNA = PF.ID_DANNO_FAUNA "
        + (!onlyLocalizzati ? "(+)" : "") + " \n"
        + "     AND PF.EXT_ID_UTILIZZO_DICHIARATO = DC.ID_UTILIZZO_DICHIARATO "
        + (!onlyLocalizzati ? "(+)" : "") + " \n";
    query = query + (idsDannoFauna != null
        ? getInCondition("DF.ID_DANNO_FAUNA", idsDannoFauna)
        : "");
    query = query + " ORDER BY                            \n"
        + "         DF.PROGRESSIVO ASC, 												\n"
        + "         DF.ID_DANNO_FAUNA,													\n"
     	+ "         DC.SIGLA_PROVINCIA,                                                 \n"
        + "         DC.DESC_COMUNE,                                                     \n"
        + "         DC.SEZIONE,                                                         \n"
        + "         DC.FOGLIO,                                                          \n"
        + "         DC.PARTICELLA,                                                      \n"
        + "         DC.SUBALTERNO                                                       \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(query, mapParameterSource, DannoFaunaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
          }, new LogVariable[]
          {}, query, mapParameterSource);
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

  public List<RiepilogoDannoFaunaDTO> getListaRiepilogoDanniFaunaDettaglio(
      long idProcedimentoOggetto, String[] idsRiepilogo)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    List<String> listIdsRiepilogo = null;
    if (idsRiepilogo != null)
    {
      listIdsRiepilogo = IuffiUtils.ARRAY.toListOfString(idsRiepilogo);
    }
    String inConditionIdDannoFauna = (idsRiepilogo != null ? getInCondition(
        "DF.ID_DANNO_FAUNA || '_' || NVL(PF.EXT_ID_UTILIZZO, CU.ID_UTILIZZO) || '_' || CU.ISTAT_COMUNE",
        listIdsRiepilogo) : " ");

    String query = queryDanniFaunaDettaglio +
        " , TMP_DANNO_FAUNA AS (                                                       \n"
        + "   SELECT                                                                     \n"
        + "      DF.ID_DANNO_FAUNA,                                                      \n"
        + "      DF.PROGRESSIVO,                                                         \n"
        + "      SF.ID_SPECIE_FAUNA,                                                     \n"
        + "      SF.DESCRIZIONE AS DESC_SPECIE_FAUNA,                                    \n"
        + "      TDF.ID_TIPO_DANNO_FAUNA,                                                \n"
        + "      TDF.DESCRIZIONE AS DESC_TIPO_DANNO_FAUNA,                               \n"
        + "      CU.ISTAT_COMUNE AS ISTAT_COMUNE,                                        \n"
        + "      CU.DESC_COMUNE,                                                         \n"
        + "      CU.ID_UTILIZZO AS ID_UTILIZZO_DICH,                                     \n"
        + "      CU.DESC_TIPO_UTILIZZO AS DESC_TIPO_UTILIZZO_DICH,                       \n"
        + "      PF.EXT_ID_UTILIZZO AS ID_UTILIZZO_RISCONTRATO,                  \n"
        + "      SUM(NVL(PF.SUPERFICIE_DANNEGGIATA,0)) AS SUPERFICIE_COINVOLTA           \n"
        + "  FROM                                                                        \n"
        + "      IUF_T_DANNO_FAUNA DF,                                                 \n"
        + "      IUF_R_DANNO_SPECIE DS,                                                \n"
        + "      IUF_D_SPECIE_FAUNA SF,                                                \n"
        + "      IUF_D_TIPO_DANNO_FAUNA TDF,                                           \n"
        + "      IUF_T_PARTICELLE_FAUNA PF,                                            \n"
        + "      TMP_CONDUZIONE_UTILIZZO CU                                              \n"
        + "  WHERE                                                                       \n"
        + "      DF.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                   \n"
        + "      AND DF.ID_DANNO_SPECIE = DS.ID_DANNO_SPECIE                             \n"
        + "      AND DS.ID_TIPO_DANNO_FAUNA = TDF.ID_TIPO_DANNO_FAUNA                    \n"
        + "      AND DS.ID_SPECIE_FAUNA = SF.ID_SPECIE_FAUNA                             \n"
        + "      AND DF.ID_DANNO_FAUNA = PF.ID_DANNO_FAUNA                               \n"      
        + "      AND PF.EXT_ID_UTILIZZO_DICHIARATO = CU.ID_UTILIZZO_DICHIARATO           \n"
        + inConditionIdDannoFauna
        + "  GROUP BY                                                                    \n"
        + "      DF.ID_DANNO_FAUNA,                                                      \n"
        + "      DF.PROGRESSIVO,                                                         \n"
        + "      SF.ID_SPECIE_FAUNA,                                                     \n"
        + "      SF.DESCRIZIONE,                                                         \n"
        + "      TDF.ID_TIPO_DANNO_FAUNA,                                                \n"
        + "      TDF.DESCRIZIONE,                                                        \n"
        + "      CU.ISTAT_COMUNE,                                                        \n"
        + "      CU.DESC_COMUNE,                                                         \n"
        + "      CU.ID_UTILIZZO,                                                         \n"
        + "      CU.DESC_TIPO_UTILIZZO,                                                  \n"
        + "      PF.EXT_ID_UTILIZZO                                              \n"
        + "  )                                                                           \n"
        + " SELECT * FROM (                                                              \n"
        + "      SELECT                                                                  \n"
        + "         DF.*,                                                                \n"
        + "         NVL(DF.ID_UTILIZZO_RISCONTRATO, ID_UTILIZZO_DICH) AS ID_UTILIZZO,    \n"
        + "         DECODE(DF.ID_UTILIZZO_RISCONTRATO, NULL, DF.DESC_TIPO_UTILIZZO_DICH , (SELECT M.DESCRIZIONE_UTILIZZO FROM SMRGAA_V_MATRICE M WHERE M.ID_UTILIZZO = DF.ID_UTILIZZO_RISCONTRATO AND ROWNUM=1)) AS DESC_TIPO_UTILIZZO, \n"
        + "         RPF.ID_RIEPILOGO_DANNO_FAUNA,                                        \n"
        + "         RPF.IMPORTO_DANNO_EFFETTIVO,                                         \n"
        + "         RPF.SUPERFICIE_ACCERTATA                                             \n"
        + "      FROM                                                                    \n"
        + "         TMP_DANNO_FAUNA DF,                                                  \n"
        + "         IUF_T_RIEPILOGO_DANNO_FAUNA RPF                                    \n"
        + "      WHERE                                                                   \n"
        + "         DF.ID_DANNO_FAUNA = RPF.ID_DANNO_FAUNA                               \n"
        + "         AND DF.ISTAT_COMUNE = RPF.EXT_ISTAT_COMUNE                           \n"
        + "         AND NVL(DF.ID_UTILIZZO_RISCONTRATO, DF.ID_UTILIZZO_DICH) = RPF.EXT_ID_UTILIZZO                             \n"
        + "      UNION                                                                   \n"
        + "      SELECT                                                                  \n"
        + "         DF.*,                                                                \n"
        + "         NVL(DF.ID_UTILIZZO_RISCONTRATO, ID_UTILIZZO_DICH) AS ID_UTILIZZO,    \n"
        + "         DECODE(DF.ID_UTILIZZO_RISCONTRATO, NULL, DF.DESC_TIPO_UTILIZZO_DICH , (SELECT M.DESCRIZIONE_UTILIZZO FROM SMRGAA_V_MATRICE M WHERE M.ID_UTILIZZO = DF.ID_UTILIZZO_RISCONTRATO AND ROWNUM=1)) AS DESC_TIPO_UTILIZZO, \n"
        + "         NULL AS ID_RIEPILOGO_DANNO_FAUNA,                                    \n"
        + "         NULL AS IMPORTO_DANNO_EFFETTIVO,                                     \n"
        + "         NULL AS SUPERFICIE_ACCERTATA                                         \n"
        + "      FROM                                                                    \n"
        + "         TMP_DANNO_FAUNA DF                                                   \n"
        + "      WHERE                                                                   \n"
        + "         NOT EXISTS (                                                         \n"
        + "             SELECT *                                                         \n"
        + "             FROM IUF_T_RIEPILOGO_DANNO_FAUNA RPF                           \n"
        + "             WHERE                                                            \n"
        + "                 RPF.ID_DANNO_FAUNA = DF.ID_DANNO_FAUNA                       \n"
        + "                 AND RPF.EXT_ISTAT_COMUNE = DF.ISTAT_COMUNE                   \n"
        + "                 AND NVL(DF.ID_UTILIZZO_RISCONTRATO, DF.ID_UTILIZZO_DICH) = RPF.EXT_ID_UTILIZZO                             \n"
        + "                \n"
        + "         )                                                                    \n"
        + "  )                                                                           \n"
        + "  ORDER BY PROGRESSIVO, DESC_SPECIE_FAUNA, DESC_COMUNE, DESC_TIPO_DANNO_FAUNA \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(query, mapParameterSource,
          RiepilogoDannoFaunaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
          }, new LogVariable[]
          {}, query, mapParameterSource);
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

  public RiepilogoDannoFaunaDTO getTotaliRiepilogoDanniFauna(
      long idProcedimentoOggetto)
          throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    String query = "SELECT SUM(TOT_SUP_DANNEGGIATA) SUPERFICIE_COINVOLTA, SUM(TOT_SUP_ACCERTATA) SUPERFICIE_ACCERTATA, SUM(TOT_IMPORTO_DANNO) IMPORTO_DANNO_EFFETTIVO FROM (SELECT "
        +
        "    nvl(SUM(nvl(superficie_danneggiata, 0)), 0) AS TOT_SUP_DANNEGGIATA, "
        +
        "    0 AS TOT_SUP_ACCERTATA, " +
        "    0 AS TOT_IMPORTO_DANNO " +
        "FROM " +
        "    IUF_t_particelle_fauna   pf, " +
        "    IUF_t_danno_fauna        df " +
        "WHERE " +
        "    df.id_danno_fauna = pf.id_danno_fauna " +
        "    AND df.id_procedimento_oggetto = :ID_PROCEDIMENTO_OGGETTO " +
        "UNION " +
        "SELECT " +
        "    0 AS TOT_SUP_DANNEGGIATA, " +
        "    nvl(SUM(nvl(superficie_accertata, 0)), 0) AS TOT_SUP_ACCERTATA, " +
        "    nvl(SUM(nvl(importo_danno_effettivo, 0)), 0)  AS TOT_IMPORTO_DANNO "
        +
        "FROM " +
        "    IUF_t_riepilogo_danno_fauna   rdf, " +
        "    IUF_t_danno_fauna             df " +
        "WHERE " +
        "    df.id_danno_fauna = rdf.id_danno_fauna " +
        "    AND df.id_procedimento_oggetto = :ID_PROCEDIMENTO_OGGETTO) ";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForObject(query, mapParameterSource,
          RiepilogoDannoFaunaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {
                  new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
              }, new LogVariable[]
                  {}, query, mapParameterSource);
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

  public List<DannoFaunaDTO> getListaDanniFauna(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return getListaDanniFauna(idProcedimentoOggetto, null);
  }

  public List<DannoFaunaDTO> getListaDanniFauna(long idProcedimentoOggetto,
      long[] idsDannoFauna) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String query = "SELECT \n"
        + "     SF.ID_SPECIE_FAUNA,                                              \n"
        + "     SF.DESCRIZIONE AS DESC_SPECIE_FAUNA,                             \n"
        + "     DF.PROGRESSIVO,						                             \n"
        + "     DF.ID_DANNO_FAUNA,					                             \n"
        + "     DF.ULTERIORI_INFORMAZIONI,			                             \n"
        + "     DF.QUANTITA,							                         \n"
        + "     DF.DATA_AGGIORNAMENTO,							                 \n"
        + "     DS.ID_DANNO_SPECIE,					                             \n"
        + "     UM.DESCRIZIONE AS DESC_UNITA_MISURA,							 \n"
        + "     TDF.ID_UNITA_MISURA,											 \n"
        + "     SF.ID_SPECIE_FAUNA,											     \n"
        + "     SF.DESCRIZIONE AS DESC_SPECIE_FAUNA,							 \n"
        + "     TDF.ID_TIPO_DANNO_FAUNA,                                         \n"
        + "     TDF.DESCRIZIONE AS DESC_TIPO_DANNO_FAUNA,                        \n"
        + "     DECODE(UL.ID_UTENTE_LOGIN, NULL, NULL, UL.COGNOME_UTENTE_LOGIN || ' ' || UL.NOME_UTENTE_LOGIN || ' (' || UL.DENOMINAZIONE || ')') AS DESC_UTENTE_AGGIORNAMENTO \n"
        + " FROM                                                                  \n"
        + "     IUF_T_DANNO_FAUNA DF,                                           \n"
        + "     IUF_R_DANNO_SPECIE DS,                                          \n"
        + "     IUF_D_SPECIE_FAUNA SF,                                          \n"
        + "     IUF_D_TIPO_DANNO_FAUNA TDF,                                     \n"
        + "     IUF_D_UNITA_MISURA UM,                                          \n"
        + "     PAPUA_V_UTENTE_LOGIN UL                                           \n"
        + " WHERE                                                                 \n"
        + "     DF.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO             \n"
        + "     AND TDF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA                       \n"
        + "     AND DF.ID_DANNO_SPECIE = DS.ID_DANNO_SPECIE                       \n"
        + "     AND DS.ID_TIPO_DANNO_FAUNA = TDF.ID_TIPO_DANNO_FAUNA              \n"
        + "     AND DS.ID_SPECIE_FAUNA = SF.ID_SPECIE_FAUNA                       \n"
        + "     AND DF.EXT_ID_UTENTE_AGGIORNAMENTO = UL.ID_UTENTE_LOGIN(+)        \n";
    if (idsDannoFauna != null)
    {
      query = query + getInCondition("DF.ID_DANNO_FAUNA", idsDannoFauna);
    }

    query = query
        + " ORDER BY DF.PROGRESSIVO ASC, DF.ID_DANNO_FAUNA                            \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(query, mapParameterSource, DannoFaunaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
          }, new LogVariable[]
          {}, query, mapParameterSource);
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

  public List<DecodificaDTO<Long>> getListaSpecieFauna()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT												\r\n" +
        "    ID_SPECIE_FAUNA AS ID,							\r\n" +
        "    ID_SPECIE_FAUNA AS CODICE,						\r\n" +
        "    DESCRIZIONE									\r\n" +
        "FROM 												\r\n" +
        "    IUF_D_SPECIE_FAUNA							\r\n" +
        "WHERE 												\r\n" +
        "    DATA_FINE_VALIDITA IS NULL 					\r\n" +
        "ORDER BY DESCRIZIONE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public List<DecodificaDTO<Long>> getListaTipoDannoFauna(long idSpecieFauna)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                     \n"
        + "     DS.ID_DANNO_SPECIE AS ID,                       		\n"
        + "     UM.DESCRIZIONE AS CODICE,                   		\n"
        + "     TDF.DESCRIZIONE                                      \n"
        + " FROM                                                     \n"
        + "     IUF_D_TIPO_DANNO_FAUNA TDF,                        \n"
        + "     IUF_R_DANNO_SPECIE DS,                             \n"
        + "     IUF_D_UNITA_MISURA UM 	                            \n"
        + " WHERE                                                    \n"
        + "     DS.ID_SPECIE_FAUNA = :ID_SPECIE_FAUNA                \n"
        + "     AND DS.DATA_FINE IS NULL                             \n"
        + "     AND DS.ID_TIPO_DANNO_FAUNA = TDF.ID_TIPO_DANNO_FAUNA \n"
        + "     AND TDF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA			\n"
        + "     AND TDF.DATA_FINE_VALIDITA IS NULL                   \n"
        + " ORDER BY                                                 \n"
        + "     TDF.DESCRIZIONE                                      \n"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_SPECIE_FAUNA", idSpecieFauna);
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_SPECIE_FAUNA", idSpecieFauna) },
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

  public long inserisciDannoFauna(long idProcedimentoOggetto,
      DannoFaunaDTO danno) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_DANNO_FAUNA  \n"
        + "  (                               \n"
        + "     ID_DANNO_FAUNA,              \n"
        + "     ID_PROCEDIMENTO_OGGETTO,     \n"
        + "     PROGRESSIVO,                 \n"
        + "     ID_DANNO_SPECIE,             \n"
        + "     ULTERIORI_INFORMAZIONI,      \n"
        + "     QUANTITA,                    \n"
        + "     DATA_AGGIORNAMENTO,          \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO  \n"
        + "  )                               \n"
        + " VALUES                           \n"
        + " (                                \n"
        + "     :ID_DANNO_FAUNA,             \n"
        + "     :ID_PROCEDIMENTO_OGGETTO,    \n"
        + "     (SELECT NVL(MAX(PROGRESSIVO),0)+1 FROM IUF_T_DANNO_FAUNA WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO),                \n"
        + "     :ID_DANNO_SPECIE,            \n"
        + "     :ULTERIORI_INFORMAZIONI,     \n"
        + "     :QUANTITA,                   \n"
        + "     SYSDATE,                     \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO \n"
        + " )                                \n"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long seqIuffiTDannoFauna = 0;
    try
    {
      seqIuffiTDannoFauna = getNextSequenceValue("SEQ_IUF_T_DANNO_FAUNA");
      mapParameterSource.addValue("ID_DANNO_FAUNA", seqIuffiTDannoFauna,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_DANNO_SPECIE", danno.getIdDannoSpecie(),
          Types.NUMERIC);
      mapParameterSource.addValue("ULTERIORI_INFORMAZIONI",
          danno.getUlterioriInformazioni(), Types.VARCHAR);
      mapParameterSource.addValue("QUANTITA", danno.getQuantita(),
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          danno.getExtIdUtenteAggiornamento(), Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return seqIuffiTDannoFauna;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("SEQ_IUF_T_DANNO_FAUNA", seqIuffiTDannoFauna),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("ID_DANNO_SPECIE", danno.getIdDannoSpecie()),
              new LogParameter("ULTERIORI_INFORMAZIONI",
                  danno.getUlterioriInformazioni()),
              new LogParameter("QUANTITA", danno.getQuantita()),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO",
                  danno.getExtIdUtenteAggiornamento())
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

  public void inserisciParticelleFauna(long idDannoFauna,
      List<ParticelleFaunaDTO> particelleFauna)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_PARTICELLE_FAUNA \n"
        + " (                                    \n"
        + "      ID_PARTICELLE_FAUNA,            \n"
        + "      EXT_ID_UTILIZZO_DICHIARATO,     \n"
        + "      SUPERFICIE_DANNEGGIATA,         \n"
        + "      FLAG_UTILIZZO_SEC,              \n"
        + "      ID_DANNO_FAUNA,                 \n"
        + "      EXT_ID_UTILIZZO         \n"
        + " )                                    \n"
        + " VALUES                               \n"
        + " (                                    \n"
        + "      :ID_PARTICELLE_FAUNA,           \n"
        + "      :EXT_ID_UTILIZZO_DICHIARATO,    \n"
        + "      :SUPERFICIE_DANNEGGIATA,        \n"
        + "      :FLAG_UTILIZZO_SEC,        \n"
        + "      :ID_DANNO_FAUNA,                 \n"
        + "      :ID_UTILIZZO_RISCONTRATO                 \n"
        + " )                                    \n";
    int size = particelleFauna.size();
    MapSqlParameterSource[] batchParameters = new MapSqlParameterSource[size];
    long seqsIuffiTParticelleFauna[] = new long[size];
    try
    {
      int i = 0;
      for (ParticelleFaunaDTO particella : particelleFauna)
      {
        seqsIuffiTParticelleFauna[i] = getNextSequenceValue(
            "SEQ_IUF_T_PARTICELLE_FAUNA");
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue("ID_PARTICELLE_FAUNA",
            seqsIuffiTParticelleFauna[i], Types.NUMERIC);
        mapParameterSource.addValue("EXT_ID_UTILIZZO_DICHIARATO",
            particella.getExtIdUtilizzoDichiarato(), Types.NUMERIC);
        mapParameterSource.addValue("SUPERFICIE_DANNEGGIATA",
        		particella.getSuperficieDanneggiata(), Types.NUMERIC);
        if (StringUtils.isBlank(particella.getFlagUtilizzoSec()))
        {
          particella.setFlagUtilizzoSec("N");
        }
        mapParameterSource.addValue("FLAG_UTILIZZO_SEC",
            particella.getFlagUtilizzoSec(), Types.VARCHAR);
        mapParameterSource.addValue("ID_DANNO_FAUNA", idDannoFauna,
            Types.NUMERIC);
        mapParameterSource.addValue("ID_UTILIZZO_RISCONTRATO",
            particella.getIdUtilizzoRiscontrato(),
            Types.NUMERIC);
        batchParameters[i++] = mapParameterSource;
      }

      namedParameterJdbcTemplate.batchUpdate(INSERT, batchParameters);
    }
    catch (Throwable t)
    {
      LogParameter[] parameters = new LogParameter[size * 5];
      int i = 0;
      int j=0;
      for (ParticelleFaunaDTO particella : particelleFauna)
      {
        parameters[i] = new LogParameter("ID_PARTICELLE_FAUNA",
            seqsIuffiTParticelleFauna[j++]);
        i++;
        parameters[i] = new LogParameter("EXT_ID_UTILIZZO_DICHIARATO",
            particella.getExtIdUtilizzoDichiarato());
        i++;
        parameters[i] = new LogParameter("SUPERFICIE_DANNEGGIATA",
            particella.getSuperficieDanneggiata());
        i++;
        parameters[i] = new LogParameter("ID_DANNO_FAUNA",
            particella.getIdDannoFauna());
        i++;
      }

      InternalUnexpectedException e = new InternalUnexpectedException(t,
          parameters,
          new LogVariable[]
          {}, INSERT, batchParameters);
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

  public void updateDannoFauna(long idProcedimentoOggetto, DannoFaunaDTO danno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE IUF_T_DANNO_FAUNA                                     \n"
        + " SET                                                            \n"
        + "     ID_DANNO_SPECIE = :ID_DANNO_SPECIE,                        \n"
        + "     ULTERIORI_INFORMAZIONI = :ULTERIORI_INFORMAZIONI,          \n"
        + "     QUANTITA = :QUANTITA,                                      \n"
        + "     DATA_AGGIORNAMENTO = SYSDATE,                              \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO \n"
        + " WHERE                                                          \n"
        + "     ID_DANNO_FAUNA = :ID_DANNO_FAUNA                           \n"
        + "     AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO     \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DANNO_SPECIE", danno.getIdDannoSpecie(),
          Types.NUMERIC);
      mapParameterSource.addValue("ULTERIORI_INFORMAZIONI",
          danno.getUlterioriInformazioni(), Types.VARCHAR);
      mapParameterSource.addValue("QUANTITA", danno.getQuantita(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          danno.getExtIdUtenteAggiornamento(), Types.NUMERIC);
      mapParameterSource.addValue("ID_DANNO_FAUNA", danno.getIdDannoFauna(),
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DANNO_SPECIE", danno.getIdDannoSpecie()),
              new LogParameter("ULTERIORI_INFORMAZIONI",
                  danno.getUlterioriInformazioni()),
              new LogParameter("QUANTITA", danno.getQuantita()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO",
                  danno.getExtIdUtenteAggiornamento()),
              new LogParameter("ID_DANNO_FAUNA", danno.getIdDannoFauna())
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

  /** CUIUFFI 312 **/

  public AccertamentoDannoDTO getAccertamentoDanno(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "SELECT                                \r\n" +
        "        AD.ID_ACCERTAMENTO_DANNO,                          \r\n" +
        "        AD.FLAG_SOPRALLUOGO AS SOPRALLUOGO,                 \r\n" +
        "        AD.DATA_SOPRALLUOGO,                          \r\n" +
        "        AD.PERITO,                          \r\n" +
        "        AD.NUMERO_PERIZIA,                          \r\n" +
        "        (SELECT NVL(SUM(NVL(rdf.importo_danno_effettivo,0)),0) FROM IUF_T_RIEPILOGO_DANNO_FAUNA RDF WHERE RDF.id_danno_fauna IN (SELECT DF.id_danno_fauna FROM IUF_T_DANNO_FAUNA DF WHERE DF.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO)) AS IMPORTO_TOTALE_ACCERTATO,                          \r\n"
        +
        "        AD.IMPORTO_RIPRISTINO,                          \r\n" +
        "        AD.SPESE_PERIZIA,                          \r\n" +
        "        AD.SPESE_PREVENZIONE,                          \r\n" +
        "        AD.DESCRIZIONE_PREVENZIONE,                          \r\n" +
        "        AD.FLAG_REITERATI_DANNI AS REITERATI_DANNI,    \r\n" +
        "        AD.NOTE    \r\n" +
        "FROM IUF_T_ACCERTAMENTO_DANNO AD                          \r\n" +
        "WHERE AD.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource,
          AccertamentoDannoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_SCORTA_MAGAZZINO", idProcedimentoOggetto)
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

  public List<DecodificaDTO<String>> getListaFunzionariIstruttoriByIdAmmComp(
      Long idProcedimento, Long idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT VT.ID_TECNICO AS ID,  VT.ID_TECNICO AS CODICE, (TT.COGNOME || ' ' || TT.NOME) AS DESCRIZIONE"
        +
        " FROM IUF_T_PROCEDIM_AMMINISTRAZIO PA, SMRCOMUNE_V_TECNICO VT, DB_TECNICO TT "
        +
        " WHERE PA.ID_PROCEDIMENTO =:ID_PROCEDIMENTO " +
        " AND VT.ID_AMM_COMPETENZA = PA.EXT_ID_AMM_COMPETENZA " +
        " AND VT.ID_UFFICIO_ZONA = PA.EXT_ID_UFFICIO_ZONA " +
        " AND VT.EXT_ID_PROCEDIMENTO =:ID_PROC_AGRICOLO " +
        " AND TT.ID_TECNICO = VT.ID_TECNICO ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_PROC_AGRICOLO", idProcedimentoAgricolo,
        Types.NUMERIC);

    try
    {
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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
  
  public BigDecimal getImportoTotaleAccertato(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT NVL(SUM(NVL(rdf.importo_danno_effettivo,0)),0)"
        + " FROM IUF_T_RIEPILOGO_DANNO_FAUNA RDF"
        + " WHERE RDF.id_danno_fauna IN (SELECT DF.id_danno_fauna FROM IUF_T_DANNO_FAUNA DF WHERE DF.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO)";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto,
        Types.NUMERIC);
   
    try
    {
      return queryForBigDecimal(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public long inserisciAccertamentoDanno(AccertamentoDannoDTO acdan,
      long idProcedimentoOggetto, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_ACCERTAMENTO_DANNO (                        \r\n"
        +
        " ID_ACCERTAMENTO_DANNO,                                   " +
        " ID_PROCEDIMENTO_OGGETTO,                                   " +
        " FLAG_SOPRALLUOGO,                                   " +
        " DATA_SOPRALLUOGO,                                   " +
        " PERITO,                                   " +
        " IMPORTO_RIPRISTINO,                                   " +
        " SPESE_PERIZIA,                                   " +
        " SPESE_PREVENZIONE,                                   " +
        " DESCRIZIONE_PREVENZIONE,                                   " +
        " FLAG_REITERATI_DANNI,                                   " +
        " NOTE,                                   " +
        " DATA_AGGIORNAMENTO,                                   " +
        " EXT_ID_UTENTE_AGGIORNAMENTO,                                   " +
        " NUMERO_PERIZIA)                                   \r\n" +
        "VALUES (                                         \r\n" +
        ":ID_ACCERTAMENTO_DANNO,  " +
        ":ID_PROCEDIMENTO_OGGETTO,  " +
        ":FLAG_SOPRALLUOGO, " +
        ":DATA_SOPRALLUOGO, " +
        ":PERITO, " +
        ":IMPORTO_RIPRISTINO, " +
        ":SPESE_PERIZIA,  " +
        ":SPESE_PREVENZIONE,  " +
        ":DESCRIZIONE_PREVENZIONE,  " +
        ":FLAG_REITERATI_DANNI, " +
        ":NOTE, " +
        "SYSDATE, " +
        ":EXT_ID_UTENTE_AGGIORNAMENTO,  " +
        ":NUMERO_PERIZIA " +
        ")";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long sequence = 0;
    try
    {
      sequence = getNextSequenceValue("SEQ_IUF_T_ACCERTAMENTO_DANNO");

      mapParameterSource.addValue("ID_ACCERTAMENTO_DANNO", sequence,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_SOPRALLUOGO", acdan.getSopralluogo(),
          Types.VARCHAR);
      mapParameterSource.addValue("DATA_SOPRALLUOGO",
          acdan.getDataSopralluogo(), Types.DATE);
      mapParameterSource.addValue("PERITO",
          acdan.getPerito(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_RIPRISTINO",
          acdan.getImportoRipristino(),
          Types.NUMERIC);
      mapParameterSource.addValue("SPESE_PERIZIA", acdan.getSpesePerizia(),
          Types.NUMERIC);
      mapParameterSource.addValue("SPESE_PREVENZIONE",
          acdan.getSpesePrevenzione(),
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE_PREVENZIONE",
          acdan.getDescrizionePrevenzione(),
          Types.VARCHAR);
      mapParameterSource.addValue("FLAG_REITERATI_DANNI",
          acdan.getReiteratiDanni(),
          Types.VARCHAR);
      mapParameterSource.addValue("ESITO_ISTRUTTORIA", acdan.getEsitoDomanda(),
          Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento,
          Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_PERIZIA", acdan.getNumeroPerizia(),
          Types.VARCHAR);
      mapParameterSource.addValue("NOTE", acdan.getNote(),
          Types.VARCHAR);
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_ACCERTAMENTO_DANNO", sequence),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("FLAG_SOPRALLUOGO", acdan.getSopralluogo()),
              new LogParameter("DATA_SOPRALLUOGO", acdan.getDataSopralluogo()),
              new LogParameter("PERITO", acdan.getPerito()),
              new LogParameter("IMPORTO_RIPRISTINO",
                  acdan.getImportoRipristino()),
              new LogParameter("SPESE_PERIZIA", acdan.getSpesePerizia()),
              new LogParameter("SPESE_PREVENZIONE",
                  acdan.getSpesePrevenzione()),
              new LogParameter("DESCRIZIONE_PREVENZIONE",
                  acdan.getDescrizionePrevenzione()),
              new LogParameter("FLAG_REITERATI_DANNI",
                  acdan.getReiteratiDanni()),
              new LogParameter("ESITO_ISTRUTTORIA", acdan.getEsitoDomanda()),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO",
                  extIdUtenteAggiornamento),
              new LogParameter("NUMERO_PERIZIA", acdan.getNumeroPerizia()),
              new LogParameter("NOTE", acdan.getNote())
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

  public long modificaAccertamentoDanno(AccertamentoDannoDTO acdan,
      long idProcedimentoOggetto, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String UPDATE = "UPDATE IUF_T_ACCERTAMENTO_DANNO                        \r\n"
        +
        " SET                                   " +
        " ID_ACCERTAMENTO_DANNO = :ID_ACCERTAMENTO_DANNO,                                   "
        + " ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO,                                   "
        + " FLAG_SOPRALLUOGO = :FLAG_SOPRALLUOGO,                                   "
        + " DATA_SOPRALLUOGO = :DATA_SOPRALLUOGO,                                   "
        + " PERITO = :PERITO,                                   " +
        " IMPORTO_RIPRISTINO = :IMPORTO_RIPRISTINO,                                   "
        + " SPESE_PERIZIA = :SPESE_PERIZIA,                                   "
        +
        " SPESE_PREVENZIONE = :SPESE_PREVENZIONE,                                   "
        + " DESCRIZIONE_PREVENZIONE = :DESCRIZIONE_PREVENZIONE,                                   "
        + " FLAG_REITERATI_DANNI = :FLAG_REITERATI_DANNI,                                   "
        + " DATA_AGGIORNAMENTO = SYSDATE,                                   " +
        " EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,                                   "
        + " NOTE = :NOTE,                                   \r\n"
        + " NUMERO_PERIZIA = :NUMERO_PERIZIA                                   \r\n"
        + " WHERE ID_ACCERTAMENTO_DANNO = :ID_ACCERTAMENTO_DANNO ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_ACCERTAMENTO_DANNO",
          acdan.getIdAccertamentoDanno(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_SOPRALLUOGO", acdan.getSopralluogo(),
          Types.VARCHAR);
      mapParameterSource.addValue("DATA_SOPRALLUOGO",
          acdan.getDataSopralluogo(), Types.DATE);
      mapParameterSource.addValue("PERITO",
          acdan.getPerito(), Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO_RIPRISTINO",
          acdan.getImportoRipristino(),
          Types.NUMERIC);
      mapParameterSource.addValue("SPESE_PERIZIA", acdan.getSpesePerizia(),
          Types.NUMERIC);
      mapParameterSource.addValue("SPESE_PREVENZIONE",
          acdan.getSpesePrevenzione(),
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE_PREVENZIONE",
          acdan.getDescrizionePrevenzione(),
          Types.VARCHAR);
      mapParameterSource.addValue("FLAG_REITERATI_DANNI",
          acdan.getReiteratiDanni(),
          Types.VARCHAR);
      mapParameterSource.addValue("ESITO_ISTRUTTORIA", acdan.getEsitoDomanda(),
          Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento,
          Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_PERIZIA", acdan.getNumeroPerizia(),
          Types.VARCHAR);
      mapParameterSource.addValue("NOTE", acdan.getNote(),
          Types.VARCHAR);
      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_ACCERTAMENTO_DANNO",
                  acdan.getIdAccertamentoDanno()),
              new LogParameter("ID_PROCEDIMENTO_OGGETTO",
                  idProcedimentoOggetto),
              new LogParameter("FLAG_SOPRALLUOGO", acdan.getSopralluogo()),
              new LogParameter("DATA_SOPRALLUOGO", acdan.getDataSopralluogo()),
              new LogParameter("PERITO", acdan.getPerito()),
              new LogParameter("IMPORTO_TOTALE_ACCERTATO",
                  acdan.getImportoTotaleAccertato()),
              new LogParameter("IMPORTO_RIPRISTINO",
                  acdan.getImportoRipristino()),
              new LogParameter("SPESE_PERIZIA", acdan.getSpesePerizia()),
              new LogParameter("SPESE_PREVENZIONE",
                  acdan.getSpesePrevenzione()),
              new LogParameter("DESCRIZIONE_PREVENZIONE",
                  acdan.getDescrizionePrevenzione()),
              new LogParameter("FLAG_REITERATI_DANNI",
                  acdan.getReiteratiDanni()),
              new LogParameter("ESITO_ISTRUTTORIA", acdan.getEsitoDomanda()),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO",
                  extIdUtenteAggiornamento),
              new LogParameter("NUMERO_PERIZIA", acdan.getNumeroPerizia()),
              new LogParameter("NOTE", acdan.getNote())
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

  public long eliminaAccertamentoDanno(long idAccertamentoDanno)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String DELETE = "DELETE FROM IUF_T_ACCERTAMENTO_DANNO WHERE ID_ACCERTAMENTO_DANNO = :ID_ACCERTAMENTO_DANNO ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_ACCERTAMENTO_DANNO", idAccertamentoDanno,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_ACCERTAMENTO_DANNO", idAccertamentoDanno),

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

  /** END CUIUFFI 312 **/

  public void deleteRiepilogoDannoFauna(List<Long> listIdsRiepilogoDannoFauna)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String DELETE = "DELETE FROM IUF_T_RIEPILOGO_DANNO_FAUNA WHERE 1=1 "
        + getInCondition("ID_RIEPILOGO_DANNO_FAUNA",
            listIdsRiepilogoDannoFauna);

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_RIEPILOGO_DANNO_FAUNA",
                  listIdsRiepilogoDannoFauna),
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

  public void deleteRiepilogoDannoFaunaByIdDannoFauna(long[] idsDannoFauna)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String DELETE = "DELETE FROM IUF_T_RIEPILOGO_DANNO_FAUNA WHERE 1=1 "
        + getInCondition("ID_DANNO_FAUNA", idsDannoFauna);

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DANNO_FAUNA", idsDannoFauna),
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

  public void insertRiepilogoDannoFauna(
      List<RiepilogoDannoFaunaDTO> riepilogoDanni)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_RIEPILOGO_DANNO_FAUNA ( \n"
        + "     ID_RIEPILOGO_DANNO_FAUNA,               \n"
        + "     ID_DANNO_FAUNA,                         \n"
        + "     EXT_ID_UTILIZZO,                        \n"
        + "     EXT_ISTAT_COMUNE,                       \n"
        + "     SUPERFICIE_ACCERTATA,                   \n"
        + "     IMPORTO_DANNO_EFFETTIVO                 \n"
        + " )                                           \n"
        + " VALUES (                                    \n"
        + "     :ID_RIEPILOGO_DANNO_FAUNA,              \n"
        + "     :ID_DANNO_FAUNA,                        \n"
        + "     :EXT_ID_UTILIZZO,                       \n"
        + "     :EXT_ISTAT_COMUNE,                      \n"
        + "     :SUPERFICIE_ACCERTATA,                  \n"
        + "     :IMPORTO_DANNO_EFFETTIVO                \n"
        + " )                                           \n";

    MapSqlParameterSource[] mapParameterSource = new MapSqlParameterSource[riepilogoDanni
        .size()];
    try
    {
      for (int i = 0; i < riepilogoDanni.size(); i++)
      {
        RiepilogoDannoFaunaDTO r = riepilogoDanni.get(i);
        r.setIdRiepilogoDannoFauna(
            getNextSequenceValue("SEQ_IUF_T_RIEPILOGO_DANNO_FA"));
        mapParameterSource[i] = new MapSqlParameterSource();
        mapParameterSource[i].addValue("ID_RIEPILOGO_DANNO_FAUNA",
            r.getIdRiepilogoDannoFauna(), Types.NUMERIC);
        mapParameterSource[i].addValue("ID_DANNO_FAUNA", r.getIdDannoFauna(),
            Types.NUMERIC);
        mapParameterSource[i].addValue("EXT_ID_UTILIZZO", r.getIdUtilizzo(),
            Types.NUMERIC);
        mapParameterSource[i].addValue("EXT_ISTAT_COMUNE", r.getIstatComune(),
            Types.VARCHAR);
        mapParameterSource[i].addValue("SUPERFICIE_ACCERTATA",
            r.getSuperficieAccertata(),
        	Types.NUMERIC);
        mapParameterSource[i].addValue("IMPORTO_DANNO_EFFETTIVO",
            r.getImportoDannoEffettivo(), Types.NUMERIC);
      }

      namedParameterJdbcTemplate.batchUpdate(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("riepilogoDanni", riepilogoDanni.toString())
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

  public List<ConcessioneDTO> getElencoConcessioni()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String query = " SELECT                                                      \n"
            + "     C.ID_CONCESSIONE,                                       \n"
            + "     C.ID_BANDO,                                             \n"
            + "     B.DENOMINAZIONE DENOMINAZIONE_BANDO,                    \n"
            + "     B.ANNO_CAMPAGNA,                                        \n"
            + "     AC.DESCRIZIONE AMM_COMPETENZA,                          \n"
            + "     AC.ID_AMM_COMPETENZA,                                   \n"
            + "     C.DATA_PROTOCOLLO,                                      \n"
            + "     C.NUMERO_PROTOCOLLO,                                    \n" 
            + "     TA.DESCRIZIONE ATTO,                                    \n" 
            + "     TA.ID_TIPO_CONCESSIONE ID_TIPO_ATTO,                    \n" 
            + "     SC.DESCRIZIONE STATO,                                   \n" 
            + "     SC.ID_STATO_CONCESSIONE,                                \n" 
            + "     IC.DATA_INIZIO DAL,                                     \n" 
            + "     C.NOTE                                                  \n"
            + " FROM                                                        \n" 
            + "     IUF_T_CONCESSIONE C,                                  \n" 
            + "     IUF_D_BANDO B,                                        \n" 
            + "     SMRCOMUNE.SMRCOMUNE_V_AMM_COMPETENZA AC,                \n" 
            + "     IUF_D_TIPO_ATTO_CONCESSIONE TA,                       \n" 
            + "     IUF_T_ITER_CONCESSIONE IC,                            \n" 
            + "     IUF_D_STATO_CONCESSIONE SC                            \n" 
            + " WHERE                                                       \n" 
            + "     C.ID_BANDO = B.ID_BANDO                                 \n"
            + "     AND AC.ID_AMM_COMPETENZA = C.EXT_ID_AMM_COMPETENZA      \n" 
            + "     AND TA.ID_TIPO_CONCESSIONE (+) = C.ID_TIPO_CONCESSIONE  \n" 
            + "     AND IC.ID_CONCESSIONE = C.ID_CONCESSIONE                \n"
            + "     AND IC.DATA_FINE IS NULL                                \n" 
            + "     AND SC.ID_STATO_CONCESSIONE = IC.ID_STATO_CONCESSIONE   \n"
            + " ORDER BY C.ID_CONCESSIONE                                   \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(query, mapParameterSource, ConcessioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          }, new LogVariable[]
          {}, query, mapParameterSource);
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

  public ConcessioneDTO getConcessione(long idConcessione)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String query = " SELECT                                                      \n"
            + "     C.ID_CONCESSIONE,                                       \n"
            + "     C.ID_BANDO,                                             \n"
            + "     B.DENOMINAZIONE DENOMINAZIONE_BANDO,                    \n"
            + "     B.ANNO_CAMPAGNA,                                        \n"
            + "     AC.DESCRIZIONE AMM_COMPETENZA,                          \n"
            + "     AC.ID_AMM_COMPETENZA,                                   \n"
            + "     AC.ID_AMM_COMPETENZA_PADRE ID_AMM_COMPETENZA_PADRE,     \n"
            + "     C.DATA_PROTOCOLLO,                                      \n"
            + "     C.NUMERO_PROTOCOLLO,                                    \n"
            + "     C.EXT_ID_VIS_MASSIVA_RNA,                               \n" 
            + "     TA.DESCRIZIONE ATTO,                                    \n" 
            + "     TA.ID_TIPO_ATTO,                                        \n" 
            + "     SC.DESCRIZIONE STATO,                                   \n" 
            + "     IC.DATA_INIZIO DAL,                                     \n" 
            + "     C.NOTE,                                                 \n"
            + "     SC.ID_STATO_CONCESSIONE                                 \n"
            + " FROM                                                        \n" 
            + "     IUF_T_CONCESSIONE C,                                  \n" 
            + "     IUF_D_BANDO B,                                        \n" 
            + "     SMRCOMUNE.SMRCOMUNE_V_AMM_COMPETENZA AC,                \n" 
            + "     IUF_D_TIPO_ATTO TA,                                   \n" 
            + "     IUF_T_ITER_CONCESSIONE IC,                            \n" 
            + "     IUF_D_STATO_CONCESSIONE SC                            \n" 
            + " WHERE                                                       \n" 
            + "     C.ID_BANDO = B.ID_BANDO                                 \n"
            + "     AND AC.ID_AMM_COMPETENZA = C.EXT_ID_AMM_COMPETENZA      \n" 
            + "     AND TA.ID_TIPO_ATTO (+) = C.ID_TIPO_CONCESSIONE         \n" 
            + "     AND IC.ID_CONCESSIONE = C.ID_CONCESSIONE                \n"
            + "     AND IC.DATA_FINE IS NULL                                \n" 
            + "     AND SC.ID_STATO_CONCESSIONE = IC.ID_STATO_CONCESSIONE   \n"
            + "     AND C.ID_CONCESSIONE = :ID_CONCESSIONE                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      return queryForObject(query, mapParameterSource, ConcessioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione", idConcessione)
          }, new LogVariable[]
          {}, query, mapParameterSource);
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

  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(
      long idConcessione) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String query = "    "
        + "   SELECT                                                                          \n"
        + "     PO.ID_PROCEDIMENTO_OGGETTO ID_PRATICA,                                        \n"
        + "     PC.ID_PRATICHE_CONCESSIONE,                                                   \n"
        + "     P.IDENTIFICATIVO PRATICA,                                                     \n"
        + "     DA.CUAA,                                                                      \n"
        + "     PC.ID_CONCESSIONE,                                                            \n"
        + "     DA.DENOMINAZIONE,                                                             \n"
        + "     SO.DESCRIZIONE STATO,                                                         \n"
        + "     PC.IMPORTO_LIQUIDAZIONE,                                                      \n"
        + "     PC.CODICE_VERCOR,                                                             \n"
        + "     PC.DATA_VISURA,                                                               \n"
        + "     PC.NOTE,                                                                      \n"
        + "     ( SELECT                                                                      \n"
        + "         SUM(IMPORTO_DANNO_EFFETTIVO)                                              \n"
        + "       FROM                                                                        \n"
        + "         IUF_T_RIEPILOGO_DANNO_FAUNA R,                                          \n"
        + "         IUF_T_PROCEDIMENTO_OGGETTO TPO,                                         \n"
        + "         IUF_T_DANNO_FAUNA DF                                                    \n"
        + "       WHERE                                                                       \n"
        + "         DF.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "         AND TPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO              \n"
        + "         AND TPO.ID_LEGAME_GRUPPO_OGGETTO = 9                                      \n"
        + "         AND R.ID_DANNO_FAUNA = DF.ID_DANNO_FAUNA) IMPORTO_PERIZIA                 \n"
        + "   FROM                                                                            \n"
        + "     IUF_T_PROCEDIMENTO_OGGETTO PO,                                              \n"
        + "     IUF_T_PROCEDIMENTO P,                                                       \n"
        + "     IUF_T_PRATICHE_CONCESSIONE PC,                                              \n"
        + "     IUF_D_STATO_OGGETTO SO,                                                     \n"
        + "     SMRGAA_V_DATI_ANAGRAFICI DA,                                                  \n"
        + "     IUF_T_PROCEDIMENTO_AZIENDA PA                                               \n"
        + "   WHERE                                                                           \n"
        + "     PC.ID_CONCESSIONE = :ID_CONCESSIONE                                           \n"
        + "     AND PC.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "     AND PA.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                                   \n"
        + "     AND PA.EXT_ID_AZIENDA = DA.ID_AZIENDA                                         \n"
        + "     AND DA.DATA_FINE_VALIDITA IS NULL                                             \n"
        + "     AND P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                                    \n"
        + "     AND SO.ID_STATO_OGGETTO = P.ID_STATO_OGGETTO                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);

      return queryForList(query, mapParameterSource,
          PraticaConcessioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
            new LogParameter("idConcessione", idConcessione)
          }, new LogVariable[]
          {}, query, mapParameterSource);
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

  public List<DecodificaDTO<Long>> getElencoAmmCompetenza(List<Long> idAmmComp,
      Long idPadre) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                    \n"
        + "   ID_AMM_COMPETENZA AS ID,                \n" 
        + "   DESCRIZIONE AS DESCRIZIONE,             \n" 
        + "   DESCRIZIONE AS CODICE                   \n" 
        + " FROM                                      \n"
        + "   SMRCOMUNE_V_AMM_COMPETENZA              \n"
        + " WHERE 1=1                                 \n";
         if(idAmmComp!=null && !idAmmComp.isEmpty())
           QUERY += getInCondition("ID_AMM_COMPETENZA", idAmmComp);
         if(idPadre!=null)
           QUERY += " AND ID_AMM_COMPETENZA_PADRE = :ID_AMM_COMPETENZA_PADRE   \n";
           QUERY += " ORDER BY DESCRIZIONE                      \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_AMM_COMPETENZA_PADRE", idPadre,
          Types.NUMERIC);
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_AMM_COMPETENZA_PADRE", idPadre) },
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

  public List<DecodificaDTO<Long>> getElencoBandi(int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                \n"
        + "   ID_BANDO AS ID,                                     \n" 
        + "   DENOMINAZIONE AS DESCRIZIONE,                       \n" 
        + "   DENOMINAZIONE AS CODICE                             \n" 
        + " FROM                                                  \n"
        + "   IUF_D_BANDO                                       \n"
        + " WHERE                                                 \n"
        + "   ID_PROCEDIMENTO_AGRICOLO=:ID_PROCEDIMENTO_AGRICOLO  \n"
        + "   AND FLAG_MASTER = 'N'                               \n"
        + " ORDER BY DENOMINAZIONE                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO",
          idProcedimentoAgricolo, Types.NUMERIC);
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoAgricolo",
              idProcedimentoAgricolo) },
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

  public List<PraticaConcessioneDTO> getElencoPraticheConcessione(long idBando,
      long idAmministrazione) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String query = "    "
        + "   SELECT                                                                          \n"
        + "     PO.ID_PROCEDIMENTO_OGGETTO ID_PRATICA,                                        \n"
        + "     P.IDENTIFICATIVO PRATICA,                                                    \n"
        + "     DA.CUAA,                                                                      \n"
        + "     DA.DENOMINAZIONE,                                                             \n"
        + "     SO.DESCRIZIONE STATO,                                                         \n"
        + "     ( SELECT                                                                      \n"
        + "         SUM(IMPORTO_DANNO_EFFETTIVO)                                              \n"
        + "       FROM                                                                        \n"
        + "         IUF_T_RIEPILOGO_DANNO_FAUNA R,                                          \n"
        + "         IUF_T_DANNO_FAUNA DF                                                    \n"
        + "       WHERE                                                                       \n"
        + "         DF.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "         AND R.ID_DANNO_FAUNA = DF.ID_DANNO_FAUNA) IMPORTO_PERIZIA                 \n"
        + "   FROM                                                                            \n"
        + "     IUF_T_PROCEDIMENTO_OGGETTO PO,                                              \n"
        + "     IUF_T_PROCEDIMENTO P,                                                       \n"
        + "     IUF_T_PROCEDIM_AMMINISTRAZIO PAM,                                           \n"
        + "     IUF_D_STATO_OGGETTO SO,                                                     \n"
        + "     SMRGAA_V_DATI_ANAGRAFICI DA,                                                  \n"
        + "     IUF_T_PROCEDIMENTO_AZIENDA PA                                               \n"
        + "   WHERE                                                                           \n"
        + "     P.ID_BANDO = :ID_BANDO                                                        \n"
        + "     AND PA.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                                   \n"
        + "     AND PAM.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                                  \n"
        + "     AND PAM.EXT_ID_AMM_COMPETENZA = :ID_AMMINISTRAZIONE                           \n"
        + "     AND PAM.DATA_FINE IS NULL                                                     \n"
        + "     AND PA.EXT_ID_AZIENDA = DA.ID_AZIENDA                                         \n"
        + "     AND DA.DATA_FINE_VALIDITA IS NULL                                             \n"
        + "     AND P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                                    \n"
        + "     AND P.NUMERO_VERCOD IS NULL                                                   \n"
        + "     AND P.DATA_VISURA IS NULL                                                     \n"
        + "     AND SO.ID_STATO_OGGETTO = 19                                                  \n"
        + "     AND SO.ID_STATO_OGGETTO = P.ID_STATO_OGGETTO                                  \n"
        + "     AND PO.ID_LEGAME_GRUPPO_OGGETTO = 9                                           \n"
        + "     AND PO.ID_PROCEDIMENTO_OGGETTO NOT IN (SELECT ID_PROCEDIMENTO_OGGETTO         \n"
        + "     FROM IUF_T_PRATICHE_CONCESSIONE WHERE FLAG_IMPORTO_CONCESSO = 'S')          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_AMMINISTRAZIONE", idAmministrazione,
          Types.NUMERIC);

      return queryForList(query, mapParameterSource,
          PraticaConcessioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBando", idBando),
              new LogParameter("idAmministrazione", idAmministrazione)
            
          }, new LogVariable[]
          {}, query, mapParameterSource);
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

  public Long insertConcessione(ConcessioneDTO concessione, long idUtenteLogin)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " "
        + " INSERT INTO IUF_T_CONCESSIONE   \n"
        + "  (                                \n"
        + "     ID_CONCESSIONE,               \n"
        + "     ID_BANDO,                     \n"
        + "     EXT_ID_AMM_COMPETENZA,        \n"
        + "     DATA_PROTOCOLLO,              \n"
        + "     NUMERO_PROTOCOLLO,            \n"
        + "     ID_TIPO_CONCESSIONE,          \n"
        + "     NOTE,                         \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,  \n"
        + "     DATA_AGGIORNAMENTO,           \n"
        + "     EXT_ID_VIS_MASSIVA_RNA        \n"
        + "  )                                \n"
        + " VALUES                            \n"
        + " (                                 \n"
        + "     :ID_CONCESSIONE,              \n"
        + "     :ID_BANDO,                    \n"
        + "     :EXT_ID_AMM_COMPETENZA,       \n"
        + "     NULL,                         \n"
        + "     NULL,                         \n"
        + "     NULL,                         \n"
        + "     :NOTE,                        \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO, \n"
        + "     SYSDATE,                      \n"
        + "     NULL                          \n"
        + " )                                 \n"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    Long idConcessione = null;
    try
    {
      idConcessione = getNextSequenceValue("SEQ_IUF_T_CONCESSIONE");
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", concessione.getIdBando(),
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_AMM_COMPETENZA",
          concessione.getIdAmmCompetenza(), Types.NUMERIC);
      mapParameterSource.addValue("NOTE", concessione.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione", concessione.getIdConcessione()),
              new LogParameter("ID_BANDO", concessione.getIdBando()),
              new LogParameter("EXT_ID_AMM_COMPETENZA",
                  concessione.getIdAmmCompetenza()),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin)
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
    return idConcessione;
  }

  public void insertIterConcessione(Long idConcessione, long idUtenteLogin,
      long idStato) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " "
        + " INSERT INTO IUF_T_ITER_CONCESSIONE   \n"
        + "  (                                \n"
        + "     ID_ITER_CONCESSIONE,          \n"
        + "     ID_CONCESSIONE,               \n"
        + "     ID_STATO_CONCESSIONE,         \n"
        + "     DATA_INIZIO,                  \n"
        + "     DATA_FINE,                    \n"
        + "     DATA_AGGIORNAMENTO,           \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO   \n"
        + "  )                                \n"
        + " VALUES                            \n"
        + " (                                 \n"
        + "     :ID_ITER_CONCESSIONE,         \n"
        + "     :ID_CONCESSIONE,              \n"
        + "     :ID_STATO,                    \n"
        + "     SYSDATE,                      \n"
        + "     NULL,                         \n"
        + "     SYSDATE,                      \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO  \n"
        + " )                                 \n"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      Long idIterConcessione = getNextSequenceValue(
          "SEQ_IUF_T_ITER_CONCESSIONE");
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_ITER_CONCESSIONE", idIterConcessione,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_STATO", idStato, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione", idConcessione),
              new LogParameter("idStato", idStato),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin)
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

  public void insertPraticaConcessione(Long idConcessione,
      PraticaConcessioneDTO pratica, Long idUtenteLogin,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " "
        + " INSERT INTO IUF_T_PRATICHE_CONCESSIONE   \n"
        + "  (                                  \n"
        + "     ID_PRATICHE_CONCESSIONE,        \n"
        + "     ID_CONCESSIONE,                 \n"
        + "     ID_PROCEDIMENTO_OGGETTO,        \n"
        + "     IMPORTO_LIQUIDAZIONE,           \n"
        + "     CODICE_VERCOR,                  \n"
        + "     DATA_VISURA,                    \n"
        + "     NOTE,                           \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,    \n"
        + "     DATA_AGGIORNAMENTO,             \n"
        + "     DATA_LIQUIDAZIONE,              \n"
        + "     IMPORTO_PERIZIA                 \n"
        + "  )                                  \n"
        + " VALUES                              \n"
        + " (                                   \n"
        + "     :ID_PRATICHE_CONCESSIONE,       \n"
        + "     :ID_CONCESSIONE,                \n"
        + "     :ID_PROCEDIMENTO_OGGETTO,       \n"
        + "     :IMPORTO_LIQUIDAZIONE,          \n"
        + "     :CODICE_VERCOR,                 \n"
        + "     :DATA_VISURA,                   \n"
        + "     :NOTE,                          \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO,   \n"
        + "     SYSDATE,                        \n"
        + "     NULL,                           \n"
        + "     :IMPORTO_PERIZIA                \n"
        + " )                                   \n"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      Long idPraticaConcessione = getNextSequenceValue(
          "SEQ_IUF_T_PRATICHE_CONCESSIO");
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PRATICHE_CONCESSIONE",
          idPraticaConcessione, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_LIQUIDAZIONE",
          pratica.getImportoLiquidazione(), Types.NUMERIC);
      mapParameterSource.addValue("CODICE_VERCOR", pratica.getCodiceVercor(),
          Types.VARCHAR);
      mapParameterSource.addValue("DATA_VISURA", pratica.getDataVisura(),
          Types.DATE);
      mapParameterSource.addValue("NOTE", pratica.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_PERIZIA",
          pratica.getImportoPerizia(), Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione", idConcessione),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("IMPORTO_LIQUIDAZIONE",
                  pratica.getImportoLiquidazione()),
              new LogParameter("CODICE_VERCOR", pratica.getCodiceVercor()),
              new LogParameter("DATA_VISURA", pratica.getDataVisuraStr()),
              new LogParameter("NOTE", pratica.getNote()),
              new LogParameter("IMPORTO_PERIZIA", pratica.getImportoPerizia()),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin),
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

  public void updateIterConcessione(long idConcessione, long idUtenteLogin)
      throws InternalUnexpectedException
  {

    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_ITER_CONCESSIONE                        \n"
        +
        " SET                                                                     \n"
        +
        "        DATA_FINE = SYSDATE,                                             \n"
        +
        "        EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO       \n"
        +
        " WHERE   ID_CONCESSIONE = :ID_CONCESSIONE                                \n"
        +
        "        AND DATA_FINE IS NULL                                            \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione",idConcessione)
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

  public void updateConcessione(long idConcessione, Long numeroProtocollo,
      Date dataProtocollo, Long idTipoAtto, long idUtenteLogin)
      throws InternalUnexpectedException
  {

    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_CONCESSIONE                                               \n"
        +
        " SET                                                                                       \n"
        +
        "        ID_TIPO_CONCESSIONE = :ID_TIPO_CONCESSIONE,                                        \n"
        +
        "         NUMERO_PROTOCOLLO = :NUMERO_PROTOCOLLO,                                           \n"
        +
        "         DATA_PROTOCOLLO = :DATA_PROTOCOLLO,                                               \n"
        +
        "         DATA_AGGIORNAMENTO = SYSDATE,                                                     \n"
        +
        "         EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO                        \n"
        +
        " WHERE   ID_CONCESSIONE = :ID_CONCESSIONE                                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_CONCESSIONE", idTipoAtto,
          Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_PROTOCOLLO", numeroProtocollo,
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_PROTOCOLLO", dataProtocollo,
          Types.DATE);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione",idConcessione),
              new LogParameter("idTipoAtto",idTipoAtto),
              new LogParameter("numeroProtocollo",numeroProtocollo),
              new LogParameter("dataProtocollo",dataProtocollo),
              new LogParameter("idUtenteLogin",idUtenteLogin)
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

  public List<DecodificaDTO<Long>> getElencoBandiConcessioni()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT DISTINCT                                         \n"
        + "   B.ID_BANDO AS ID,                                     \n" 
        + "   B.DENOMINAZIONE AS DESCRIZIONE,                       \n" 
        + "   B.DENOMINAZIONE AS CODICE                             \n" 
        + " FROM                                                    \n"
        + "   IUF_D_BANDO B,                                      \n"
        + "   IUF_T_CONCESSIONE C                                 \n"
        + " WHERE                                                   \n"
        + "   B.ID_BANDO = C.ID_BANDO                               \n"
        + " ORDER BY B.DENOMINAZIONE                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public List<DecodificaDTO<Long>> getElencoStatiConcessioni()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT DISTINCT                                         \n"
        + "   B.ID_STATO_CONCESSIONE AS ID,                         \n" 
        + "   B.DESCRIZIONE AS DESCRIZIONE,                         \n" 
        + "   B.DESCRIZIONE AS CODICE                               \n" 
        + " FROM                                                    \n"
        + "   IUF_D_STATO_CONCESSIONE B,                          \n"
        + "   IUF_T_ITER_CONCESSIONE IC,                          \n"
        + "   IUF_T_CONCESSIONE C                                 \n"
        + " WHERE                                                   \n"
        + "   B.ID_STATO_CONCESSIONE = IC.ID_STATO_CONCESSIONE      \n"
        + "   AND IC.DATA_FINE IS NULL                              \n"
        + "   AND IC.ID_CONCESSIONE = C.ID_CONCESSIONE              \n"
        + " ORDER BY B.DESCRIZIONE                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public List<DecodificaDTO<Long>> getElencoTipiAttiConcessione()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT DISTINCT                                         \n"
        + "   TA.ID_TIPO_CONCESSIONE AS ID,                                \n" 
        + "   TA.DESCRIZIONE AS DESCRIZIONE,                        \n" 
        + "   TA.DESCRIZIONE AS CODICE                              \n" 
        + " FROM                                                    \n"
        + "   IUF_D_TIPO_ATTO_CONCESSIONE TA,                                 \n"
        + "   IUF_T_CONCESSIONE C                                 \n"
        + " WHERE                                                   \n"
        + "   TA.ID_TIPO_CONCESSIONE = C.ID_TIPO_CONCESSIONE               \n"
        + " ORDER BY TA.DESCRIZIONE                                 \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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
  
  public List<DecodificaDTO<Long>> getElencoTipoAttoConcessione()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoTipoAttoConcessione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                           \n"
        + "   ID_TIPO_CONCESSIONE AS ID,   \n"
        + "   DESCRIZIONE AS CODICE,    \n"
        + "   DESCRIZIONE         \n"
        + " FROM                        \n"
        + "   IUF_D_TIPO_ATTO_CONCESSIONE         \n"
        + " ORDER BY DESCRIZIONE      \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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
  
  public List<DecodificaDTO<Long>> getMetodiCensimento(Long idSpecie)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " select mc.id_metodo_censimento as id, mc.desc_metodologia as descrizione, mc.id_metodo_censimento as codice from IUF_R_METODO_CENS_SPECIE mcs\r\n"
        +
        "  left join IUF_d_metodo_censimento mc on mc.id_metodo_censimento = mcs.id_metodo_censimento\r\n"
        +
          "  where mcs.id_specie_ogur = :ID_SPECIE";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_SPECIE",
          idSpecie, Types.NUMERIC);
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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
  
  public List<DecodificaDTO<Long>> getAmmCompetenzaConcessioni()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT DISTINCT                                         \n"
        + "   B.ID_AMM_COMPETENZA AS ID,                            \n" 
        + "   B.DESCRIZIONE AS DESCRIZIONE,                         \n" 
        + "   B.DESCRIZIONE AS CODICE                               \n" 
        + " FROM                                                    \n"
        + "   SMRCOMUNE.SMRCOMUNE_V_AMM_COMPETENZA B,               \n"
        + "   IUF_T_CONCESSIONE C                                 \n"
        + " WHERE                                                   \n"
        + "   B.ID_AMM_COMPETENZA = C.EXT_ID_AMM_COMPETENZA         \n" 
        + " ORDER BY B.DESCRIZIONE                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public boolean canCambioStato(long idConcessione)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::checkIfRecordFileNotInRDocSpesaPO]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                   \n"
        + "   COUNT(*)                                              \n"
        + " FROM                                                    \n"
        + "   IUF_T_PRATICHE_CONCESSIONE                          \n"
        + " WHERE                                                   \n"
        + "   ID_CONCESSIONE = :ID_CONCESSIONE                      \n"
        + "   AND (IMPORTO_LIQUIDAZIONE IS NULL                     \n"    
        + "   OR CODICE_VERCOR IS NULL                              \n"
        + "   OR DATA_VISURA IS NULL)                               \n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);

      return queryForLong(QUERY, mapParameterSource) == 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public void deleteIpotesiPrelievo(long idOgur)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteIpotesiPrelievo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_T_OGUR_IPOTESI_PRELIEVO          \n"
        + " WHERE                                                             \n"
        + "     ID_DISTRETTO IN (                                             \n"
        + "         SELECT                                                    \n"
        + "             ID_DISTRETTO                                          \n"
        + "         FROM                                                      \n"
        + "             IUF_T_OGUR_DISTRETTO                                \n"
        + "         WHERE                                                     \n"
        + "             ID_OGUR=:ID_OGUR                                      \n"
        + "     )                                                             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_OGUR",
          idOgur, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_OGUR",
                  idOgur),
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

  public void deleteAnniCensiti(long idOgur) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteAnniCensiti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_T_OGUR_ANNI_CENSITI              \n"
        + " WHERE                                                             \n"
        + "     ID_DISTRETTO IN (                                             \n"
        + "         SELECT                                                    \n"
        + "             ID_DISTRETTO                                          \n"
        + "         FROM                                                      \n"
        + "             IUF_T_OGUR_DISTRETTO                                \n"
        + "         WHERE                                                     \n"
        + "             ID_OGUR=:ID_OGUR                                      \n"
        + "     )                                                             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_OGUR",
          idOgur, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_OGUR",
                  idOgur),
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

  public void deleteOgurCentismento(long idOgur)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteOgurCentismento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_T_OGUR_CENSIMENTO                \n"
        + " WHERE                                                             \n"
        + "     ID_DISTRETTO IN (                                             \n"
        + "         SELECT                                                    \n"
        + "             ID_DISTRETTO                                          \n"
        + "         FROM                                                      \n"
        + "             IUF_T_OGUR_DISTRETTO                                \n"
        + "         WHERE                                                     \n"
        + "             ID_OGUR=:ID_OGUR                                      \n"
        + "     )                                                             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_OGUR",
          idOgur, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_OGUR",
                  idOgur),
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

  public List<OgurDTO> getElencoOgur(long idProcedimentoOggetto, Long idOgur)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY =  " SELECT                                                \n" 
        + "   O.ID_OGUR,                                                    \n"
        + "   O.ID_PROCEDIMENTO_OGGETTO,                                    \n"
        + "   O.PROGRESSIVO,                                                \n"
        + "   O.ID_SPECIE_OGUR,                                             \n"
        + "   O.SUPERFICIE_TOTALE_ATCCA,                                    \n"
        + "   S.DESCRIZIONE                                                 \n"
        + " FROM                                                            \n"
        + "   IUF_T_OGUR O,                                               \n"
        + "   IUFFI.IUF_D_SPECIE_OGUR S                                   \n"
        + " WHERE                                                           \n"
        + "   O.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO          \n"
        + "   AND O.ID_SPECIE_OGUR = S.ID_SPECIE_OGUR                       \n"
        + "   AND S.DATA_FINE_VALIDITA IS NULL                              \n";
    if(idOgur!=null)
      QUERY+="AND O.ID_OGUR = :ID_OGUR                                      \n";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_OGUR", idOgur, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, OgurDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public List<DecodificaDTO<Long>> getElencoSpecieOgur(boolean forInsert,
      Long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = "SELECT  ID_SPECIE_OGUR ID,                              \r\n"
        +
        "        DESCRIZIONE AS DESCRIZIONE,                                \r\n"
        +
        "        DESCRIZIONE AS CODICE                                      \r\n"
        +
        "FROM     IUF_D_SPECIE_OGUR                                       \r\n"
        +
        "WHERE DATA_FINE_VALIDITA IS NULL                                   \r\n"
        +
        "AND DATA_INIZIO_VALIDITA < SYSDATE                                 \r\n"
        +
        "AND FLAG_OGUR = 'S'                                                \r\n";
    if(forInsert)
      QUERY +=" AND ID_SPECIE_OGUR NOT IN                                   \r\n"
          + "(  SELECT ID_SPECIE_OGUR                                       \r\n"
          + "   FROM IUF_T_OGUR                                           \r\n" 
          + "   WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO)   \r\n"; 
    QUERY +="ORDER BY DESCRIZIONE                                           \r\n"; 
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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
  
  public List<Distretto> getElencoDistrettiOgur(boolean regionale,
      String idDistretto, long idSpecie, Long piano, long procedimento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoDistrettiOgur]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    Calendar calendar = GregorianCalendar.getInstance();
    String anno = calendar.get(Calendar.YEAR) + "";
    String tabella = "";
    String campo = "";
    String id = "";
    if (regionale)
    {
      tabella = "IUF_D_OGUR_DA_REGIONE D";
      campo = "ID_OGUR_DA_REGIONE";
      id = "ID_OGUR_DA_REGIONE";
    }
    else
    {
      tabella = "IUF_T_OGUR_DISTRETTO D";
      campo = "ID_DISTRETTO";
      id = "ID_DISTRETTO";
    }
    String QUERY = "select distinct(d." + id
        + ") as id_distretto, d.NOMIN_DISTRETTO, d.SUPERFICIE_DISTRETTO, d.sus, pd.ID_PIANO_DISTRETTO_OGUR, "
        +
        "pd.TOTALE_CENSITO, pd.totale_prelievo, pd.indeterminati_censito, cp1.etichetta as et1, "
        +
        "cp1.valore_da as valoreda1, cp1.valore_a as valorea1, cp2.etichetta as et2, cp2.valore_da as valoreda2, cp2.valore_a as valorea2, "
        +
        "cp3.etichetta as et3, cp3.valore_da as valoreda3, cp3.valore_a as valorea3, "
        +
        "cp4.etichetta as et4,  cp5.etichetta as et5,  cp6.etichetta as et6, cp4.valore_da as valoreda4, cp5.valore_da as valoreda5, cp4.valore_a as valorea4, cp5.valore_a as valorea5, "
        +
        "cpo1.censito as censito1, cpo2.censito as censito2, cpo3.censito as censito3, cpo4.censito as censito4,  cpo5.censito as censito5, ip.percentuale, pd.max_capi_prelievo, "
        + "pd.indeterminati_prelievo, cpo1.prelevato as prelievo1, cpo2.prelevato as prelievo2, cpo3.prelevato as prelievo3, cpo4.prelevato as prelievo4, cpo5.prelevato as prelievo5, cpo6.prelevato as prelievo6,"
        + "cpo1.percentuale as perc1, cpo2.percentuale as perc2, cpo3.percentuale as perc3, cpo4.percentuale as perc4, cpo5.percentuale as perc5, cpo6.percentuale as perc6, pd.esito_totale_prelievo, "
        + "CPO1.ESITO_CONTROLLO as esito1, CPO2.ESITO_CONTROLLO as esito2, CPO3.ESITO_CONTROLLO as esito3, CPO4.ESITO_CONTROLLO as esito4, CPO5.ESITO_CONTROLLO as esito5, CPO6.ESITO_CONTROLLO as esito6 "
        + " from " + tabella
        + " left join IUF_t_piano_distretto_ogur PD on d." + id + " = pd."
        + id
        + " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO1 on cpo1.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo1.progressivo = 1 and cpo1.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO2 on cpo2.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo2.progressivo = 2 and cpo2.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO3 on cpo3.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo3.progressivo = 3 and cpo3.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO4 on cpo4.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo4.progressivo = 4 and cpo4.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO5 on cpo5.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo5.progressivo = 5 and cpo5.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO6 on cpo6.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo6.progressivo = 6 and cpo6.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_R_CONTROLLI_PIANO_OGUR CP1 on cp1.progressivo = 1 and cp1.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_R_CONTROLLI_PIANO_OGUR CP2 on cp2.progressivo = 2 and cp2.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_R_CONTROLLI_PIANO_OGUR CP3 on cp3.progressivo = 3 and cp3.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_R_CONTROLLI_PIANO_OGUR CP5 on cp5.progressivo = 5 and cp5.id_specie_ogur = :ID_SPECIE "
        +
        " left join IUF_R_CONTROLLI_PIANO_OGUR CP6 on cp6.progressivo = 6 and cp6.id_specie_ogur = :ID_SPECIE "
        +
            " left join IUF_R_CONTROLLI_PIANO_OGUR CP4 on cp4.progressivo = 4 and cp4.id_specie_ogur = :ID_SPECIE  ";
    if (!regionale)
    {
      QUERY = QUERY
          + " left join IUF_t_ogur_ipotesi_prelievo ip on ip.id_distretto = d.id_distretto and ip.anno = "
          + anno;
    }
    else
    {
      QUERY = QUERY
          + " left join IUF_D_OGUR_DA_REGIONE ip on ip.id_ogur_da_regione = d.ID_OGUR_DA_REGIONE";
    }
    QUERY = QUERY + " where d." + campo + " IN" + idDistretto
        + " and pd.id_piano_ogur = (select id_piano_ogur from IUF_T_PIANO_OGUR where id_procedimento_oggetto = :id_procedimento_oggetto and id_specie_ogur = :ID_SPECIE)";
    if (piano != null)
    {
      QUERY = QUERY
          + " and pd.ID_PIANO_DISTRETTO_OGUR = :ID_PIANO_DISTRETTO_OGUR ";
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      
      mapParameterSource.addValue(campo, idDistretto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_SPECIE", idSpecie,
          Types.NUMERIC);
      mapParameterSource.addValue("id_procedimento_oggetto", procedimento,
          Types.NUMERIC);
      if (piano != null)
      {
        mapParameterSource.addValue("ID_PIANO_DISTRETTO_OGUR", piano,
            Types.NUMERIC);
      }
      return queryForList(QUERY, mapParameterSource,
          Distretto.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DATI_PROCEDIMENTO", null)
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
  
  public List<RicercaDistretto> getIdDistrettoOgur(Long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = "select pdo.id_distretto as iddistretto, pdo.ID_OGUR_DA_REGIONE as iddistrettoregione from IUF_T_PIANO_OGUR PO "
        +
        " left join IUF_T_PIANO_DISTRETTO_OGUR PDO on po.id_piano_ogur = pdo.id_piano_ogur "
        +
        " where po.id_procedimento_oggetto = :ID_PROCEDIMENTO_OGGETTO";   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          RicercaDistretto.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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
  
  public List<DataCensimento> getDateCensimento(long piano, long specie)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = "select co.data_censimento, mc.desc_metodologia, co.ID_DATE_CENS_OGUR as id, mc.ID_METODO_CENSIMENTO as metodo, co.VALORE_METODO_CENSIMENTO as descrizione, um.descrizione as unita from IUF_T_DATE_CENS_OGUR co "
        +
        "left join IUF_R_METODO_CENS_SPECIE cs on cs.id_specie_ogur = :ID_SPECIE   and co.id_metodo_specie = cs.id_metodo_specie "
        +
        "left join IUF_D_METODO_CENSIMENTO mc on mc.id_metodo_censimento = cs.id_metodo_censimento "
        + "left join IUF_D_UNITA_MISURA um on um.ID_UNITA_MISURA = cs.ID_UNITA_MISURA"
        + " where co.id_piano_distretto_ogur = :ID_PIANO";   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO", piano, Types.NUMERIC);
      mapParameterSource.addValue("ID_SPECIE", specie, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          DataCensimento.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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
  
  public long getMetodoSpecieCensimento(long metodo, long specie)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = "select id_metodo_specie from IUF_R_METODO_CENS_SPECIE where id_specie_ogur = :SPECIE and id_metodo_censimento = :METODO";   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("SPECIE", specie, Types.NUMERIC);
      mapParameterSource.addValue("METODO", metodo, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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
 
  public void inserisciOgur(Long idProcedimentoOggetto, Long idSpecieOgur,
      BigDecimal superficie, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::inserisciOgur]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " INSERT INTO IUF_T_OGUR    (          \n"
        + " ID_OGUR,                                              \n"
        + " ID_PROCEDIMENTO_OGGETTO,                              \n"
        + " PROGRESSIVO,                                          \n"
        + " SUPERFICIE_TOTALE_ATCCA,                              \n"
        + " ID_SPECIE_OGUR,                                       \n"
        + " DATA_AGGIORNAMENTO,                                   \n"
        + " EXT_ID_UTENTE_AGGIORNAMENTO                           \n"
        + " )   VALUES   (                                        \n"
        + " SEQ_IUF_T_OGUR.NEXTVAL,                             \n"
        + " :ID_PROCEDIMENTO_OGGETTO,                             \n"
        + " NVL((SELECT MAX(PROGRESSIVO)+1 FROM IUF_T_OGUR WHERE ID_PROCEDIMENTO_OGGETTO=:ID_PROCEDIMENTO_OGGETTO),1),                               \n"
        + " :SUPERFICIE,                                          \n"
        + " :ID_SPECIE_OGUR,                                      \n"
        + " SYSDATE,                                              \n"
        + " :EXT_ID_UTENTE_AGGIORNAMENTO                          \n"
        + " )                                                     \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
        mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_SPECIE_OGUR", idSpecieOgur,
          Types.NUMERIC);
        mapParameterSource.addValue("SUPERFICIE", superficie, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);

      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idSpecieOgur", idSpecieOgur),
              new LogParameter("superficie", superficie),
              new LogParameter("extIdUtenteAggiornamento",
                  extIdUtenteAggiornamento)
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

  public void updateOgur(Long idOgur, BigDecimal superficie,
      long extIdUtenteAggiornamento) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_OGUR                                \r\n"
        +
        " SET                                                                 \r\n"
        +
        "        SUPERFICIE_TOTALE_ATCCA = :SUPERFICIE,                       \r\n"
        +
        "        EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,  \r\n"
        +
        "        DATA_AGGIORNAMENTO = SYSDATE                                 \r\n"
        +
        " WHERE                                                               \r\n"
        +
        "    ID_OGUR = :ID_OGUR                                               \r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_OGUR", idOgur, Types.NUMERIC);
      mapParameterSource.addValue("SUPERFICIE", superficie, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idOgur",idOgur),
              new LogParameter("superficie",superficie)
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
  
  public void deleteIpotesiPrelievoDistretto(long idDistretto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteIpotesiPrelievoDistretto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_T_OGUR_IPOTESI_PRELIEVO          \n"
        + " WHERE                                                             \n"
        + "     ID_DISTRETTO = :ID_DISTRETTO                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDistretto", idDistretto)
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

  public void deleteAnniCensitiDistretto(long idDistretto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteAnniCensitiDistretto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_T_OGUR_ANNI_CENSITI              \n"
        + " WHERE                                                             \n"
        + "     ID_DISTRETTO = :ID_DISTRETTO                                  \n";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDistretto", idDistretto)
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

  public void deleteOgurCentismentoDistretto(long idDistretto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteOgurCentismentoDistretto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " DELETE FROM IUF_T_OGUR_CENSIMENTO                \n"
        + " WHERE                                                             \n"
        + "     ID_DISTRETTO = :ID_DISTRETTO                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDistretto", idDistretto)
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

  public OgurDTO getOgur(long idOgur, boolean showDetails)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getOgur]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                                                                  \n"
        + "   O.ID_OGUR,                                                                                            \n"
        + "   O.ID_PROCEDIMENTO_OGGETTO,                                                                            \n"
        + "   O.PROGRESSIVO,                                                                                        \n"
        + "   O.ID_SPECIE_OGUR,                                                                                     \n"
        + "   O.SUPERFICIE_TOTALE_ATCCA,                                                                            \n"
        + "   S.DESCRIZIONE,                                                                                        \n"
        + "   D.ID_DISTRETTO,                                                                                       \n"
        + "   D.SUPERFICIE_DISTRETTO,                                                                               \n"
        + "   D.NOMIN_DISTRETTO,                                                                                    \n"
        + "   D.SUPERF_VENAB_DISTRETTO,                                                                             \n"
        + "   D.SUS                                                                                                 \n";
        if(showDetails)  
          QUERY+= "   ,C.ID_CENSIMENTO,                                                                             \n"
        + "   C.ANNO ANNO_CENSIMENTO,                                                                               \n"
        + "   C.DENSITA_SUP_CENS,                                                                                   \n"
        + "   C.DENSITA_CAPI_SUS,                                                                                   \n"
        + "   C.DENSITA_OBIETTIVO,                                                                                  \n"
        + "   C.CONSISTENZA_POTENZ,                                                                                 \n"
        + "   A.ID_ANNI_CENSITI,                                                                                    \n"
        + "   A.ANNO,                                                                                               \n"
        + "   A.TOT_CENSITO,                                                                                        \n"
        + "   A.TOT_PRELEVATO,                                                                                      \n"
        + "   A.SUPERF_CENSITA,                                                                                     \n"
        + "   A.PIANO_NUMERICO,                                                                                     \n"
        + "   A.INCIDENTI_STRADALI,                                                                                 \n"
        + "   A.SUPERF_CENSITA,                                                                                     \n"
        + "   A.DANNI_CAUSATI,                                                                                      \n"
        + "   I.ID_IPOTESI_PRELIEVO,                                                                                \n"
        + "   I.ANNO ANNO_IPOTESI,                                                                                  \n"
        + "   I.PERCENTUALE                                                                                         \n";
        QUERY+= " FROM                                                                                              \n"
        + "   IUF_T_OGUR O,                                                                                       \n"
        + "   IUFFI.IUF_D_SPECIE_OGUR S,                                                                          \n"
        + "   IUF_T_OGUR_DISTRETTO D                                                                              \n";
      if(showDetails)  
        QUERY+="   ,IUF_T_OGUR_CENSIMENTO C,                                                                      \n"
             + "   IUF_T_OGUR_ANNI_CENSITI A,                                                                     \n"
             + "   IUF_T_OGUR_IPOTESI_PRELIEVO I                                                                  \n";
    QUERY += " WHERE                                                                                                \n"
          + "   O.ID_OGUR = :ID_OGUR                                                                                \n"
          + "   AND O.ID_SPECIE_OGUR = S.ID_SPECIE_OGUR                                                             \n"
          + "   AND S.DATA_FINE_VALIDITA IS NULL                                                                    \n"
          + "   AND O.ID_OGUR = D.ID_OGUR (+)                                                                       \n";
        if(showDetails)
          QUERY +="   AND D.ID_DISTRETTO = A.ID_DISTRETTO (+)                                                       \n"
                + "   AND D.ID_DISTRETTO = C.ID_DISTRETTO (+)                                                       \n"
                + "   AND D.ID_DISTRETTO = I.ID_DISTRETTO (+)                                                       \n";
        if(showDetails)
          QUERY+= " ORDER BY O.ID_OGUR, D.NOMIN_DISTRETTO, C.ID_CENSIMENTO, A.ID_ANNI_CENSITI, I.ID_IPOTESI_PRELIEVO \n";
        else
          QUERY+= " ORDER BY O.ID_OGUR, D.NOMIN_DISTRETTO                                                            \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_OGUR",idOgur, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<OgurDTO>()
          {
            @Override
            public OgurDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              OgurDTO ogur = new OgurDTO();
              List<DistrettoDTO> distretti = new ArrayList<>();
              List<AnnoCensitoDTO> anniCensiti = new ArrayList<>();
              List<IpotesiPrelievoDTO> ipotesiPrelievi = new ArrayList<>();
              Long idOgur = 0l;
              Long idOgurOld = -1l;
              Long idDistretto = 0l;
              Long idDistrettoOld = -1l;
              Long idIpotesiPrelievo = 0l;
              Long idIpotesiPrelievoOld = -1l;
              Long idAnnoCensito = 0l;
              Long idAnnoCensitoOld = -1l;
              AnnoCensitoDTO annoCensito = new AnnoCensitoDTO();
              DistrettoDTO distretto = new DistrettoDTO();

              while (rs.next())
              {
                idOgur = rs.getLong("ID_OGUR");
                if(idOgur.longValue()!=idOgurOld.longValue())
                {
                  ogur = new OgurDTO();
                  ogur.setIdOgur(idOgur);
                  ogur.setDescrizione(rs.getString("DESCRIZIONE"));
                  ogur.setIdSpecieOgur(rs.getLong("ID_SPECIE_OGUR"));
                  ogur.setSuperficieTotaleAtcca(
                      rs.getBigDecimal("SUPERFICIE_TOTALE_ATCCA"));
                  distretti = new ArrayList<>();
                  ogur.setDistretti(distretti);
                  idOgurOld = idOgur;
                }
                
                idDistretto=rs.getLong("ID_DISTRETTO");
                if (idDistretto.longValue() != idDistrettoOld.longValue()
                    && idDistretto.longValue() != 0)
                {
                  distretto = new DistrettoDTO();
                  distretto.setIdDistretto(idDistretto);
                  distretto.setIdOgur(idOgur);
                  distretto.setSuperficieDistretto(
                      rs.getBigDecimal("SUPERFICIE_DISTRETTO"));
                  distretto.setNominDistretto(rs.getString("NOMIN_DISTRETTO"));
                  distretto.setSuperfVenabDistretto(
                      rs.getBigDecimal("SUPERF_VENAB_DISTRETTO"));
                  distretto.setSus(rs.getBigDecimal("SUS"));
                  if(showDetails)
                  {
                    CensimentoDTO censimento = new CensimentoDTO();
                    censimento.setAnno(rs.getInt("ANNO_CENSIMENTO"));
                    censimento.setConsistenzaPotenz(
                        rs.getBigDecimal("CONSISTENZA_POTENZ"));
                    censimento.setDensitaCapiSus(
                        rs.getBigDecimal("DENSITA_CAPI_SUS"));
                    censimento.setDensitaObiettivo(
                        rs.getBigDecimal("DENSITA_OBIETTIVO"));
                    censimento.setDensitaSupCens(
                        rs.getBigDecimal("DENSITA_SUP_CENS"));
                    censimento.setIdCensimento(rs.getLong("ID_CENSIMENTO"));
                    censimento.setIdDistretto(idDistrettoOld);
                    distretto.setCensimento(censimento);
                  }
                  
                  distretti.add(distretto);
                  
                  anniCensiti = new ArrayList<>();
                  distretto.setAnniCensiti(anniCensiti);
                  ipotesiPrelievi = new ArrayList<>();
                  distretto.setIpotesiPrelievo(ipotesiPrelievi);
                  
                  idDistrettoOld = idDistretto;
                }
                
                if(showDetails)
                {
                  idAnnoCensito = rs.getLong("ID_ANNI_CENSITI");
                  if(idAnnoCensito.longValue()!=idAnnoCensitoOld.longValue())
                  {
                    annoCensito = new AnnoCensitoDTO();
                    annoCensito.setIdAnniCensiti(rs.getLong("ID_ANNI_CENSITI"));
                    annoCensito.setIdDistretto(idDistretto);
                    annoCensito.setAnno(rs.getInt("ANNO"));
                    annoCensito
                        .setDanniCausati(rs.getBigDecimal("DANNI_CAUSATI"));
                    annoCensito.setIncidentiStradali(
                        rs.getBigDecimal("INCIDENTI_STRADALI"));
                    annoCensito
                        .setPianoNumerico(rs.getBigDecimal("PIANO_NUMERICO"));
                    annoCensito
                        .setSuperfCensita(rs.getBigDecimal("SUPERF_CENSITA"));
                    annoCensito.setTotCensito(rs.getBigDecimal("TOT_CENSITO"));
                    annoCensito
                        .setTotPrelevato(rs.getBigDecimal("TOT_PRELEVATO"));
                    if(annoCensito.getAnno()!=0)
                    {
                      anniCensiti.add(annoCensito);
                      idAnnoCensitoOld = idAnnoCensito;
                    }
                  }
                  
                  idIpotesiPrelievo = rs.getLong("ID_IPOTESI_PRELIEVO");
                  if (idIpotesiPrelievo.longValue() != idIpotesiPrelievoOld
                      .longValue())
                  {
                    IpotesiPrelievoDTO ipotesi = new IpotesiPrelievoDTO();
                    ipotesi.setIdIpotesiPrelievo(idIpotesiPrelievo);
                    ipotesi.setIdDistretto(idDistretto);
                    ipotesi.setAnno(rs.getInt("ANNO_IPOTESI"));
                    ipotesi.setPercentuale(rs.getBigDecimal("PERCENTUALE"));
                    if(distretto.getIpotesiPrelievo(ipotesi.getAnno())!=null)
                      distretto.getIpotesiPrelievo(ipotesi.getAnno())
                          .setPercentuale(ipotesi.getPercentuale());

                    if (ipotesi.getAnno() != 0 && distretto
                        .getIpotesiPrelievo(ipotesi.getAnno()) == null)
                    {
                      ipotesiPrelievi.add(ipotesi);
                      idIpotesiPrelievoOld=idIpotesiPrelievo;
                    }
                  }
                }
                
              }
              return ogur;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idOgur", idOgur)
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

  public Long inserisciDistrettoOgur(long idOgur, String nominDistretto,
      BigDecimal superficieDistretto, BigDecimal superfVenabDistretto,
      BigDecimal sus, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::inserisciDistrettoOgur]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " INSERT INTO IUF_T_OGUR_DISTRETTO (   \n"
        + " ID_DISTRETTO,                                         \n"
        + " NOMIN_DISTRETTO,                                      \n"
        + " ID_OGUR,                                              \n"
        + " SUPERFICIE_DISTRETTO,                                 \n"
        + " SUPERF_VENAB_DISTRETTO,                               \n"
        + " SUS,                                                  \n"
        + " DATA_AGGIORNAMENTO,                                   \n"
        + " EXT_ID_UTENTE_AGGIORNAMENTO                           \n"
        + " )   VALUES   (                                        \n"
        + " :ID_DISTRETTO,                                        \n"
        + " :NOMIN_DISTRETTO,                                     \n"
        + " :ID_OGUR,                                             \n"
        + " :SUPERFICIE_DISTRETTO,                                \n"
        + " :SUPERF_VENAB_DISTRETTO,                              \n"
        + " :SUS,                                                 \n"
        + " SYSDATE,                                              \n"
        + " :EXT_ID_UTENTE_AGGIORNAMENTO                          \n"
        + " )                                                     \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      Long idDistretto = getNextSequenceValue("SEQ_IUF_T_OGUR_DISTRETTI");
        mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
        mapParameterSource.addValue("ID_OGUR", idOgur, Types.NUMERIC);
      mapParameterSource.addValue("NOMIN_DISTRETTO", nominDistretto,
          Types.VARCHAR);
      mapParameterSource.addValue("SUPERFICIE_DISTRETTO", superficieDistretto,
          Types.NUMERIC);
      mapParameterSource.addValue("SUPERF_VENAB_DISTRETTO",
          superfVenabDistretto, Types.NUMERIC);
        mapParameterSource.addValue("SUS", sus, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);

      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
      return idDistretto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idOgur", idOgur),
              new LogParameter("nominDistretto", nominDistretto),
              new LogParameter("superficieDistretto", superficieDistretto),
              new LogParameter("superfVenabDistretto", superfVenabDistretto),
              new LogParameter("sus", sus),
              new LogParameter("extIdUtenteAggiornamento",
                  extIdUtenteAggiornamento)
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

  public void updateDistrettoOgur(Long idDistretto, String nominDistretto,
      BigDecimal superficieDistretto, BigDecimal superfVenabDistretto,
      BigDecimal sus, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_OGUR_DISTRETTO                      \r\n"
        +
        " SET                                                                 \r\n"
        +
        "        NOMIN_DISTRETTO = :NOMIN_DISTRETTO,                          \r\n"
        +
        "        SUPERFICIE_DISTRETTO = :SUPERFICIE_DISTRETTO,                \r\n"
        +
        "        SUPERF_VENAB_DISTRETTO = :SUPERF_VENAB_DISTRETTO,            \r\n"
        +
        "        SUS = :SUS,                                                  \r\n"
        +
        "        EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,  \r\n"
        +
        "        DATA_AGGIORNAMENTO = SYSDATE                                 \r\n"
        +
        " WHERE                                                               \r\n"
        +
        "    ID_DISTRETTO = :ID_DISTRETTO                                     \r\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
      mapParameterSource.addValue("NOMIN_DISTRETTO", nominDistretto,
          Types.VARCHAR);
      mapParameterSource.addValue("SUPERFICIE_DISTRETTO", superficieDistretto,
          Types.NUMERIC);
      mapParameterSource.addValue("SUPERF_VENAB_DISTRETTO",
          superfVenabDistretto, Types.NUMERIC);
      mapParameterSource.addValue("SUS", sus, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
            new LogParameter("idDistretto", idDistretto),
            new LogParameter("nominDistretto", nominDistretto),
            new LogParameter("superficieDistretto", superficieDistretto),
            new LogParameter("superfVenabDistretto", superfVenabDistretto),
            new LogParameter("sus", sus),
              new LogParameter("extIdUtenteAggiornamento",
                  extIdUtenteAggiornamento)
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

  public List<OgurDTO> getElencoOgur(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getOgur]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                                                                  \n"
        + "   O.ID_OGUR,                                                                                            \n"
        + "   O.ID_PROCEDIMENTO_OGGETTO,                                                                            \n"
        + "   O.PROGRESSIVO,                                                                                        \n"
        + "   O.ID_SPECIE_OGUR,                                                                                     \n"
        + "   O.SUPERFICIE_TOTALE_ATCCA,                                                                            \n"
        + "   S.DESCRIZIONE,                                                                                        \n"
        + "   D.ID_DISTRETTO,                                                                                       \n"
        + "   D.SUPERFICIE_DISTRETTO,                                                                               \n"
        + "   D.NOMIN_DISTRETTO,                                                                                    \n"
        + "   D.SUPERF_VENAB_DISTRETTO,                                                                             \n"
        + "   D.SUS,                                                                                                \n"
        + "   C.ID_CENSIMENTO,                                                                                      \n"
        + "   C.ANNO ANNO_CENSIMENTO,                                                                               \n"
        + "   C.DENSITA_SUP_CENS,                                                                                   \n"
        + "   C.DENSITA_CAPI_SUS,                                                                                   \n"
        + "   C.DENSITA_OBIETTIVO,                                                                                  \n"
        + "   C.CONSISTENZA_POTENZ,                                                                                 \n"
        + "   A.ID_ANNI_CENSITI,                                                                                    \n"
        + "   A.ANNO,                                                                                               \n"
        + "   A.TOT_CENSITO,                                                                                        \n"
        + "   A.TOT_PRELEVATO,                                                                                      \n"
        + "   A.SUPERF_CENSITA,                                                                                     \n"
        + "   A.PIANO_NUMERICO,                                                                                     \n"
        + "   A.INCIDENTI_STRADALI,                                                                                 \n"
        + "   A.SUPERF_CENSITA,                                                                                     \n"
        + "   A.DANNI_CAUSATI,                                                                                      \n"
        + "   I.ID_IPOTESI_PRELIEVO,                                                                                \n"
        + "   I.ANNO ANNO_IPOTESI,                                                                                  \n"
        + "   I.PERCENTUALE                                                                                         \n"
        + " FROM                                                                                                    \n"
        + "   IUF_T_OGUR O,                                                                                       \n"
        + "   IUFFI.IUF_D_SPECIE_OGUR S,                                                                          \n"
        + "   IUF_T_OGUR_DISTRETTO D,                                                                             \n"
        + "   IUF_T_OGUR_CENSIMENTO C,                                                                            \n"
        + "   IUF_T_OGUR_ANNI_CENSITI A,                                                                          \n"
        + "   IUF_T_OGUR_IPOTESI_PRELIEVO I                                                                       \n"
        + " WHERE                                                                                                   \n"
        + "   O.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                  \n"
        + "   AND O.ID_SPECIE_OGUR = S.ID_SPECIE_OGUR                                                               \n"
        + "   AND S.DATA_FINE_VALIDITA IS NULL                                                                      \n"
        + "   AND O.ID_OGUR = D.ID_OGUR (+)                                                                         \n"
        + "   AND D.ID_DISTRETTO = A.ID_DISTRETTO (+)                                                               \n"
        + "   AND D.ID_DISTRETTO = C.ID_DISTRETTO (+)                                                               \n"
        + "   AND D.ID_DISTRETTO = I.ID_DISTRETTO (+)                                                               \n"
        + " ORDER BY O.ID_OGUR, D.NOMIN_DISTRETTO, C.ID_CENSIMENTO, A.ID_ANNI_CENSITI, I.ID_IPOTESI_PRELIEVO        \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<OgurDTO>>()
          {
            @Override
            public List<OgurDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              OgurDTO ogur = new OgurDTO();
              List<OgurDTO> ret = new ArrayList<>();
              List<DistrettoDTO> distretti = new ArrayList<>();
              List<AnnoCensitoDTO> anniCensiti = new ArrayList<>();
              List<IpotesiPrelievoDTO> ipotesiPrelievi = new ArrayList<>();
              Long idOgur = 0l;
              Long idOgurOld = -1l;
              Long idDistretto = 0l;
              Long idDistrettoOld = -1l;
              Long idIpotesiPrelievo = 0l;
              Long idIpotesiPrelievoOld = -1l;
              Long idAnnoCensito = 0l;
              Long idAnnoCensitoOld = -1l;
              while (rs.next())
              {
                idOgur = rs.getLong("ID_OGUR");
                if(idOgur.longValue()!=idOgurOld.longValue())
                {
                  ogur = new OgurDTO();
                  ogur.setIdOgur(idOgur);
                  ogur.setDescrizione(rs.getString("DESCRIZIONE"));
                  ogur.setIdSpecieOgur(rs.getLong("ID_SPECIE_OGUR"));
                  ogur.setSuperficieTotaleAtcca(
                      rs.getBigDecimal("SUPERFICIE_TOTALE_ATCCA"));
                  distretti = new ArrayList<>();
                  ogur.setDistretti(distretti);
                  ret.add(ogur);
                  idOgurOld = idOgur;
                }
                
                idDistretto=rs.getLong("ID_DISTRETTO");
                if (idDistretto.longValue() != idDistrettoOld.longValue()
                    && idDistretto.longValue() != 0)
                {
                  DistrettoDTO distretto = new DistrettoDTO();
                  distretto.setIdDistretto(idDistretto);
                  distretto.setIdOgur(idOgur);
                  distretto.setSuperficieDistretto(
                      rs.getBigDecimal("SUPERFICIE_DISTRETTO"));
                  distretto.setNominDistretto(rs.getString("NOMIN_DISTRETTO"));
                  distretto.setSuperfVenabDistretto(
                      rs.getBigDecimal("SUPERF_VENAB_DISTRETTO"));
                  distretto.setSus(rs.getBigDecimal("SUS"));
                  CensimentoDTO censimento = new CensimentoDTO();
                  censimento.setAnno(rs.getInt("ANNO_CENSIMENTO"));
                  censimento.setConsistenzaPotenz(
                      rs.getBigDecimal("CONSISTENZA_POTENZ"));
                  censimento
                      .setDensitaCapiSus(rs.getBigDecimal("DENSITA_CAPI_SUS"));
                  censimento.setDensitaObiettivo(
                      rs.getBigDecimal("DENSITA_OBIETTIVO"));
                  censimento
                      .setDensitaSupCens(rs.getBigDecimal("DENSITA_SUP_CENS"));
                  censimento.setIdCensimento(rs.getLong("ID_CENSIMENTO"));
                  censimento.setIdDistretto(idDistrettoOld);
                  distretto.setCensimento(censimento);
                  distretti.add(distretto);
                  
                  anniCensiti = new ArrayList<>();
                  distretto.setAnniCensiti(anniCensiti);
                  ipotesiPrelievi = new ArrayList<>();
                  distretto.setIpotesiPrelievo(ipotesiPrelievi);
                  idDistrettoOld = idDistretto;
                }
                
                idAnnoCensito = rs.getLong("ID_ANNI_CENSITI");
                if(idAnnoCensito.longValue()!=idAnnoCensitoOld.longValue())
                {
                  AnnoCensitoDTO annoCensito = new AnnoCensitoDTO();
                  annoCensito.setIdAnniCensiti(rs.getLong("ID_ANNI_CENSITI"));
                  annoCensito.setIdDistretto(idDistretto);
                  annoCensito.setAnno(rs.getInt("ANNO"));
                  annoCensito
                      .setDanniCausati(rs.getBigDecimal("DANNI_CAUSATI"));
                  annoCensito.setIncidentiStradali(
                      rs.getBigDecimal("INCIDENTI_STRADALI"));
                  annoCensito
                      .setPianoNumerico(rs.getBigDecimal("PIANO_NUMERICO"));
                  annoCensito
                      .setSuperfCensita(rs.getBigDecimal("SUPERF_CENSITA"));
                  annoCensito.setTotCensito(rs.getBigDecimal("TOT_CENSITO"));
                  annoCensito
                      .setTotPrelevato(rs.getBigDecimal("TOT_PRELEVATO"));
                  anniCensiti.add(annoCensito);
                  idAnnoCensitoOld = idAnnoCensito;
                }
                
                idIpotesiPrelievo = rs.getLong("ID_IPOTESI_PRELIEVO");
                if (idIpotesiPrelievo.longValue() != idIpotesiPrelievoOld
                    .longValue())
                {
                  IpotesiPrelievoDTO ipotesi = new IpotesiPrelievoDTO();
                  ipotesi.setIdIpotesiPrelievo(idIpotesiPrelievo);
                  ipotesi.setIdDistretto(idDistretto);
                  ipotesi.setAnno(rs.getInt("ANNO_IPOTESI"));
                  ipotesi.setPercentuale(rs.getBigDecimal("PERCENTUALE"));
                  ipotesiPrelievi.add(ipotesi);
                  idIpotesiPrelievoOld=idIpotesiPrelievo;
                }
                
              }
              return ret;
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

  public void insertAnnoCensito(Long idDistretto, AnnoCensitoDTO annoCensito,
      long extIdUtenteAggiornamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertAnnoCensito]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "INSERT INTO IUF_T_OGUR_ANNI_CENSITI ( \n"
        + " ID_ANNI_CENSITI, \n"
        + " ID_DISTRETTO, \n"
        + " ANNO, \n"
        + " TOT_CENSITO, \n"
        + " SUPERF_CENSITA, \n"
        + " PIANO_NUMERICO, \n"
        + " TOT_PRELEVATO, \n"
        + " DANNI_CAUSATI, \n"
        + " INCIDENTI_STRADALI, \n"
        + " DATA_AGGIORNAMENTO, \n"
        + " EXT_ID_UTENTE_AGGIORNAMENTO) VALUES ( \n"
        + " :ID_ANNI_CENSITI, \n"
        + " :ID_DISTRETTO, \n"
        + " :ANNO, \n"
        + " :TOT_CENSITO, \n"
        + " :SUPERF_CENSITA, \n"
        + " :PIANO_NUMERICO, \n"
        + " :TOT_PRELEVATO, \n"
        + " :DANNI_CAUSATI, \n"
        + " :INCIDENTI_STRADALI, \n"
        + " SYSDATE, \n"
        + " :EXT_ID_UTENTE_AGGIORNAMENTO)";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
        mapParameterSource = new MapSqlParameterSource();
      Long idAnniCensiti = getNextSequenceValue(
          "SEQ_IUF_T_OGUR_ANNI_CENSITI");
      mapParameterSource.addValue("ID_ANNI_CENSITI", idAnniCensiti,
          Types.NUMERIC);
        mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
        mapParameterSource.addValue("ANNO", annoCensito.getAnno(), Types.NUMERIC);
      mapParameterSource.addValue("TOT_CENSITO", annoCensito.getTotCensito(),
          Types.NUMERIC);
      mapParameterSource.addValue("SUPERF_CENSITA",
          annoCensito.getSuperfCensita(), Types.NUMERIC);
      mapParameterSource.addValue("PIANO_NUMERICO",
          annoCensito.getPianoNumerico(), Types.NUMERIC);
      mapParameterSource.addValue("TOT_PRELEVATO",
          annoCensito.getTotPrelevato(), Types.NUMERIC);
      mapParameterSource.addValue("DANNI_CAUSATI",
          annoCensito.getDanniCausati(), Types.NUMERIC);
      mapParameterSource.addValue("INCIDENTI_STRADALI",
          annoCensito.getIncidentiStradali(), Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);

      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDistretto", idDistretto)
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

  public void insertCensimento(Long idDistretto, CensimentoDTO censimento,
      long extIdUtenteAggiornamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertCensimento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "INSERT INTO IUF_T_OGUR_CENSIMENTO ( \n"
        + " ID_CENSIMENTO, \n"
        + " ID_DISTRETTO, \n"
        + " ANNO, \n"
        + " DENSITA_SUP_CENS, \n"
        + " DENSITA_CAPI_SUS, \n"
        + " DENSITA_OBIETTIVO, \n"
        + " CONSISTENZA_POTENZ, \n"
        + " DATA_AGGIORNAMENTO, \n"
        + " EXT_ID_UTENTE_AGGIORNAMENTO) VALUES ( \n"
        + " :ID_CENSIMENTO, \n"
        + " :ID_DISTRETTO, \n"
        + " :ANNO, \n"
        + " :DENSITA_SUP_CENS, \n"
        + " :DENSITA_CAPI_SUS, \n"
        + " :DENSITA_OBIETTIVO, \n"
        + " :CONSISTENZA_POTENZ, \n"
        + " SYSDATE, \n"
        + " :EXT_ID_UTENTE_AGGIORNAMENTO)";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
        mapParameterSource = new MapSqlParameterSource();
        Long idCensimento = getNextSequenceValue("SEQ_IUF_T_OGUR_CENSIMENTO");
        mapParameterSource.addValue("ID_CENSIMENTO", idCensimento, Types.NUMERIC);
        mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
        mapParameterSource.addValue("ANNO", censimento.getAnno(), Types.NUMERIC);
      mapParameterSource.addValue("DENSITA_SUP_CENS",
          censimento.getDensitaSupCens(), Types.NUMERIC);
      mapParameterSource.addValue("DENSITA_CAPI_SUS",
          censimento.getDensitaCapiSus(), Types.NUMERIC);
      mapParameterSource.addValue("DENSITA_OBIETTIVO",
          censimento.getDensitaObiettivo(), Types.NUMERIC);
      mapParameterSource.addValue("CONSISTENZA_POTENZ",
          censimento.getConsistenzaPotenz(), Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);

      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDistretto", idDistretto)
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

  public void insertIpotesiPrelievo(Long idDistretto,
      IpotesiPrelievoDTO ipotesi, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertIpotesiPrelievo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "INSERT INTO IUF_T_OGUR_IPOTESI_PRELIEVO ( \n"
        + " ID_IPOTESI_PRELIEVO, \n"
        + " ID_DISTRETTO, \n"
        + " ANNO, \n"
        + " PERCENTUALE, \n"
        + " DATA_AGGIORNAMENTO, \n"
        + " EXT_ID_UTENTE_AGGIORNAMENTO) VALUES ( \n"
        + " :ID_IPOTESI_PRELIEVO, \n"
        + " :ID_DISTRETTO, \n"
        + " :ANNO, \n"
        + " :PERCENTUALE, \n"
        + " SYSDATE,  \n"
        + " :EXT_ID_UTENTE_AGGIORNAMENTO)";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
        mapParameterSource = new MapSqlParameterSource();
      Long idIpotesiPrelievo = getNextSequenceValue(
          "SEQ_IUF_T_OGUR_IPOTESI_PRELI");
        mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
      mapParameterSource.addValue("ID_IPOTESI_PRELIEVO", idIpotesiPrelievo,
          Types.NUMERIC);
        mapParameterSource.addValue("ID_DISTRETTO", idDistretto, Types.NUMERIC);
        mapParameterSource.addValue("ANNO", ipotesi.getAnno(), Types.NUMERIC);
      mapParameterSource.addValue("PERCENTUALE", ipotesi.getPercentuale(),
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);

      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDistretto", idDistretto)
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

  public boolean isPraticaInBozza(long idConcessione)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::isPraticaInBozza]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                   \n"
        + "   COUNT(*)                                              \n"
        + " FROM                                                    \n"
        + "   IUF_T_CONCESSIONE C,                                \n"
        + "   IUF_T_ITER_CONCESSIONE IC                           \n"
        + " WHERE                                                   \n"
        + "   IC.ID_STATO_CONCESSIONE = 10                          \n"
        + "   AND C.ID_CONCESSIONE = :ID_CONCESSIONE                \n"
        + "   AND C.ID_CONCESSIONE = IC.ID_CONCESSIONE              \n"
        + "   AND IC.DATA_FINE IS NULL                              \n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);

      return queryForLong(QUERY, mapParameterSource) > 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public void aggiornaVisuraConcessione(long idConcessione,
      Long visuraRitornataDalWs, long idUtenteLogin)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE IUF_T_CONCESSIONE             \n"
        + " SET                                                    \n"
        + "     EXT_ID_VIS_MASSIVA_RNA = :EXT_ID_VIS_MASSIVA_RNA,  \n"
        + "     FLAG_AZIENDE_INVIATE_RNA = 'N',                    \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,       \n"
        + "     DATA_AGGIORNAMENTO = SYSDATE                                      \n"
        + " WHERE                                                  \n"
        + "     ID_CONCESSIONE = :ID_CONCESSIONE                   \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_VIS_MASSIVA_RNA",
          visuraRitornataDalWs, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione", idConcessione),
              new LogParameter("visuraRitornataDalWs", visuraRitornataDalWs)
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
  
  public void aggiornaFlagVisuraConcessione(long idConcessione,
      long idUtenteLogin) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE IUF_T_CONCESSIONE                            \n"
        + " SET                                                                   \n"
        + "     FLAG_AZIENDE_INVIATE_RNA = 'S',                                   \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,       \n"
        + "     DATA_AGGIORNAMENTO = SYSDATE                                      \n"
        + " WHERE                                                                 \n"
        + "     ID_CONCESSIONE = :ID_CONCESSIONE                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione", idConcessione)
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

  public void aggiornaNotePraticaConcessione(long idPraticaConcessione,
      String note, long idUtenteLogin) throws InternalUnexpectedException
  {

    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE IUF_T_PRATICHE_CONCESSIONE                   \n"
        + " SET                                                                   \n"
        + "     NOTE = :NOTE,                                                     \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,       \n"
        + "     DATA_AGGIORNAMENTO = SYSDATE                                      \n"
        + " WHERE                                                                 \n"
        + "     ID_PRATICHE_CONCESSIONE = :ID_PRATICHE_CONCESSIONE                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PRATICHE_CONCESSIONE",
          idPraticaConcessione, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      mapParameterSource.addValue("NOTE", note, Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPraticaConcessione", idPraticaConcessione),
              new LogParameter("note", note)
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

  public String getNotePratica(long idPraticaConcessione)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT NOTE FROM IUF_T_PRATICHE_CONCESSIONE WHERE ID_PRATICHE_CONCESSIONE = :ID_PRATICHE_CONCESSIONE";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PRATICHE_CONCESSIONE",
          idPraticaConcessione, Types.NUMERIC);
      return queryForString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idPraticaConcessione", idPraticaConcessione) },
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

  public boolean isPraticaInBozzaOrVisura(long idConcessione)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::isPraticaInBozza]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                   \n"
        + "   COUNT(*)                                              \n"
        + " FROM                                                    \n"
        + "   IUF_T_CONCESSIONE C,                                \n"
        + "   IUF_T_ITER_CONCESSIONE IC                           \n"
        + " WHERE                                                   \n"
        + "   (IC.ID_STATO_CONCESSIONE = 10                         \n"
        + "   OR IC.ID_STATO_CONCESSIONE = 20)                      \n"
        + "   AND C.ID_CONCESSIONE = :ID_CONCESSIONE                \n"
        + "   AND C.ID_CONCESSIONE = IC.ID_CONCESSIONE              \n"
        + "   AND IC.DATA_FINE IS NULL                              \n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);

      return queryForLong(QUERY, mapParameterSource) > 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<DatiAziendaDTO> getElencoAziendeConcessione(long idConcessione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoAziendeConcessione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                               \n"
        + "     DISTINCT DA.CUAA                                                \n"
        + " FROM                                                                \n"
        + "     IUF_T_PRATICHE_CONCESSIONE PC,                                \n"
        + "     IUF_T_CONCESSIONE C,                                          \n"
        + "     IUF_T_PROCEDIMENTO_OGGETTO PO,                                \n"
        + "     IUF_T_PROCEDIMENTO_AZIENDA PA,                                \n"
        + "     SMRGAA_V_DATI_ANAGRAFICI DA                                     \n"
        + " WHERE                                                               \n"
        + "     C.ID_CONCESSIONE = :ID_CONCESSIONE                              \n"
        + "     AND PC.ID_CONCESSIONE = C.ID_CONCESSIONE                        \n"
        + "     AND PC.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO     \n"
        + "     AND PA.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                     \n"
        + "     AND DA.ID_AZIENDA = PA.EXT_ID_AZIENDA                           \n"
        + "     AND DA.DATA_FINE_VALIDITA IS NULL                               \n"
        + " ORDER BY                                                            \n"
        + "   DA.CUAA                                                           \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DatiAziendaDTO>>()
          {
            @Override
            public List<DatiAziendaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<DatiAziendaDTO> list = new ArrayList<DatiAziendaDTO>();
              while (rs.next())
              {
                DatiAziendaDTO azienda = new DatiAziendaDTO();
                azienda.setCuaa(rs.getString("CUAA"));
                LocalDate date = LocalDate.now();
                int currentYear = date.getYear();
                int month = date.getMonthValue();
                Calendar cal = Calendar.getInstance();
                int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
                azienda.setDataConcessione(
                    lastDayOfMonth + "/" + month + "/" + currentYear);
                azienda.setDataFineEsercizio("31/12/"+currentYear);
                azienda.setDataInizioAnalisi("31/12/"+(currentYear-2));
                azienda.setCheckDeminimis("S");
                azienda.setCheckAiuti("N");
                azienda.setCheckDeggendorf("N");
                list.add(azienda);
              }

              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  public int getNumeroAziendeInseriteCorrettamente(List<String> cuaaAziende,
      Long extIdVisMassivaRna) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT COUNT(DISTINCT CUAA_AZIENDA) FROM REGATA_V_VISURE_RNA            \r\n"
        + " WHERE                                                                                 \r\n" 
        + "    ID_VIS_MASSIVA_RNA = :EXT_ID_VIS_MASSIVA_RNA                                       \r\n"
        + getInCondition("CUAA_AZIENDA", cuaaAziende);
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("EXT_ID_VIS_MASSIVA_RNA", extIdVisMassivaRna,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.queryForInt(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("extIdVisMassivaRna", extIdVisMassivaRna)
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

  public boolean esisteIdVisMassivaRnaSuVista(Long extIdVisMassivaRna)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::esisteIdVisMassivaRnaSuVista]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                   \n"
        + "   COUNT(*)                                              \n"
        + " FROM                                                    \n"
        + "   REGATA_V_VISURE_RNA                                   \n"
        + " WHERE                                                   \n"
        + "   ID_VIS_MASSIVA_RNA = :EXT_ID_VIS_MASSIVA_RNA          \n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("EXT_ID_VIS_MASSIVA_RNA", extIdVisMassivaRna,
          Types.NUMERIC);

      return queryForLong(QUERY, mapParameterSource) > 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public Long getIdRichiesta(long idConcessione)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT EXT_ID_VIS_MASSIVA_RNA FROM IUF_T_CONCESSIONE WHERE ID_CONCESSIONE = :ID_CONCESSIONE";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione,
          Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_CONCESSIONE", idConcessione) },
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

  public List<ProcedimentoOggettoVO> getAziendeDaVistaRegata(
      Long extIdVisMassivaRna) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getAziendeDaVistaRegata]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " "
        + " SELECT DISTINCT                                                             \n"                                
        + "     V.CUAA_AZIENDA,                                                         \n"
        + "     V.DATA_ESITO_RNA,                                                       \n"
        + "     V.VERCOR_RNA,                                                           \n"
        + "     V.IMP_CONCESSO_TOTALE,                                                  \n"
        + "     PC.IMPORTO_PERIZIA,                                                     \n"
        + "     PC.ID_PRATICHE_CONCESSIONE,                                             \n"
        + "     PO.ID_PROCEDIMENTO_OGGETTO,                                             \n"
        + "     P.IDENTIFICATIVO,                                                       \n"
        + "     PO.ID_PROCEDIMENTO                                                      \n"
        + " FROM                                                                        \n"
        + "     REGATA_V_VISURE_RNA V,                                                  \n"
        + "     IUF_T_CONCESSIONE C          ,                                        \n"
        + "     IUF_T_PROCEDIMENTO P,                                                 \n"
        + "     IUF_T_PROCEDIMENTO_AZIENDA PA,                                        \n"
        + "     SMRGAA_V_DATI_ANAGRAFICI DA,                                            \n"
        + "     IUF_T_PRATICHE_CONCESSIONE PC,                                        \n"
        + "     IUF_T_PROCEDIMENTO_OGGETTO PO                                         \n"
        + " WHERE                                                                       \n"
        + "     ID_VIS_MASSIVA_RNA = :ID_VIS_MASSIVA_RNA                                \n"
        + "     AND V.ID_VIS_MASSIVA_RNA = C.EXT_ID_VIS_MASSIVA_RNA                     \n"
        + "     AND C.ID_CONCESSIONE = PC.ID_CONCESSIONE                                \n"
        + "     AND PC.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO             \n"
        + "     AND P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                              \n"
        + "     AND V.CUAA_AZIENDA = DA.CUAA                                            \n"
        + "     AND DA.DATA_FINE_VALIDITA IS NULL                                       \n"
        + "     AND DA.ID_AZIENDA = PA.EXT_ID_AZIENDA                                   \n"
        + "     AND PA.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                              \n"
        + "     AND V.VERCOR_RNA IS NOT NULL                                            \n"
        + " ORDER BY                                                                    \n"
        + "     P.IDENTIFICATIVO                                                        \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_VIS_MASSIVA_RNA", extIdVisMassivaRna,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<ProcedimentoOggettoVO>>()
          {
            @Override
            public List<ProcedimentoOggettoVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<ProcedimentoOggettoVO> list = new ArrayList<ProcedimentoOggettoVO>();
              while (rs.next())
              {
                ProcedimentoOggettoVO proc = new ProcedimentoOggettoVO();
                proc.setCuaa(rs.getString("CUAA_AZIENDA"));
                proc.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                proc.setIdProcedimento(rs.getLong("ID_PROCEDIMENTO"));
                proc.setIdProcedimentoOggetto(
                    rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
                proc.setDataEsitoRna(rs.getDate("DATA_ESITO_RNA"));
                proc.setNumeroVercor(rs.getString("VERCOR_RNA"));
                proc.setImportoConcessoTotale(
                    rs.getBigDecimal("IMP_CONCESSO_TOTALE"));
                proc.setImportoPerizia(rs.getBigDecimal("IMPORTO_PERIZIA"));
                proc.setIdPraticheConcessione(
                    rs.getLong("ID_PRATICHE_CONCESSIONE"));
                list.add(proc);
              }

              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  public void updateVercorProcedimento(long idProcedimento, Date dataEsitoRna,
      String numeroVercor, long idUtenteLogin)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE IUF_T_PROCEDIMENTO                           \n"
        + " SET                                                                   \n"
        + "     NUMERO_VERCOD = :NUMERO_VERCOR,                                   \n"
        + "     DATA_VISURA = :DATA_VISURA,                                       \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,       \n"
        + "     DATA_ULTIMO_AGGIORNAMENTO = SYSDATE                               \n"
        + " WHERE                                                                 \n"
        + "     ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_VERCOR", numeroVercor, Types.VARCHAR);
      mapParameterSource.addValue("DATA_VISURA", dataEsitoRna, Types.DATE);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
            new LogParameter("idProcedimento", idProcedimento),
            new LogParameter("numeroVercor", numeroVercor),
            new LogParameter("idUtenteLogin", idUtenteLogin)
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

  public void updatePraticaConcessione(long idProcedimento,
      long idPraticheConcessione, String numeroVercor, Date dataEsitoRna,
      long idUtenteLogin) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE IUF_T_PRATICHE_CONCESSIONE                   \n"
        + " SET                                                                   \n"
        + "     IMPORTO_LIQUIDAZIONE =                                            \n"
        + "         NVL(( SELECT SUM(IMPORTO_DANNO_EFFETTIVO)                     \n"
        + "           FROM IUF_T_RIEPILOGO_DANNO_FAUNA R, IUF_T_DANNO_FAUNA D \n" 
        + "           WHERE D.ID_DANNO_FAUNA = R.ID_DANNO_FAUNA                   \n"
        + "           AND D.ID_PROCEDIMENTO_OGGETTO =                             \n"
        + "             ( SELECT MAX(PO.ID_PROCEDIMENTO_OGGETTO)                  \n"
        + "               FROM                                                    \n"
        + "                 IUF_T_PROCEDIMENTO P,                               \n"
        + "                 IUF_T_PROCEDIMENTO_OGGETTO PO                       \n"
        + "               WHERE                                                   \n"
        + "                 PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                \n"
        + "                 AND PO.ID_PROCEDIMENTO = :ID_PROCEDIMENTO             \n"
        + "                 AND PO.ID_LEGAME_GRUPPO_OGGETTO = 9                   \n"
        + "             )                                                         \n"
        + "         ),0),                                                         \n"
        + "     CODICE_VERCOR = :NUMERO_VERCOR,                                   \n"
        + "     DATA_VISURA = :DATA_VISURA,                                       \n"
        + "     DATA_AGGIORNAMENTO = SYSDATE,                                     \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO        \n"
        + " WHERE                                                                 \n"
        + "     ID_PRATICHE_CONCESSIONE = :ID_PRATICHE_CONCESSIONE                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PRATICHE_CONCESSIONE",
          idPraticheConcessione, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_VERCOR", numeroVercor, Types.VARCHAR);
      mapParameterSource.addValue("DATA_VISURA", dataEsitoRna, Types.DATE);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
            new LogParameter("idProcedimento", idProcedimento),
            new LogParameter("idPraticheConcessione", idPraticheConcessione),
            new LogParameter("numeroVercor", numeroVercor),
            new LogParameter("idUtenteLogin", idUtenteLogin)
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

  public List<DecodificaDTO<Long>> getElencoMetodiCensimento()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT                                    \n"
        + "    ID_METODO_CENSIMENTO ID,                             \n"
        + "    DESC_METODOLOGIA DESCRIZIONE,                        \n"
        + "    DESC_METODOLOGIA CODICE                              \n"
        + "  FROM                                                   \n"
        + "    IUF_D_METODO_CENSIMENTO MC                          \n"
        + "  WHERE                                                  \n"
        + "    DATA_INIZIO_VALIDITA <= SYSDATE                      \n"
        + "    AND NVL(DATA_FINE_VALIDITA, SYSDATE) >= SYSDATE AND MC.ID_METODO_CENSIMENTO IN (SELECT ID_METODO_CENSIMENTO FROM IUF_R_METODO_CENS_SPECIE WHERE ID_SPECIE_OGUR = 6)       \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public UnitaMisuraDTO getUnitaMisuraByIdMetodoCensimento(
      long idMetodoCensimento) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "select mcs.id_metodo_censimento, mcs.id_unita_misura, mc.desc_metodologia as descrizione, u.descrizione as descunita, mcs.id_metodo_specie from IUF_R_METODO_CENS_SPECIE mcs "
        +
        "left join IUF_D_METODO_CENSIMENTO mc on mc.id_metodo_censimento = mcs.id_metodo_censimento left join IUF_D_UNITA_MISURA U on u.id_unita_misura = mcs.id_unita_misura "
        +
        "where mc.data_fine_validita is null and mcs.id_specie_ogur = 6 and mcs.id_metodo_censimento = :ID_METODO_CENSIMENTO";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_METODO_CENSIMENTO", idMetodoCensimento);
      return queryForObject(QUERY, mapParameterSource, UnitaMisuraDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  public void deleteDateCensCinghiali(long idInfoCinghiale,
      Long progressivoInfo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteDateCensCinghiali]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
     String QUERY = " DELETE IUF_T_DATE_CENS_CINGHIALI                      \n"
        + " WHERE                                                             \n"
        + "     ID_INFO_CINGHIALI IN (                                        \n"
        + "         SELECT                                                    \n"
        + "             ID_INFO_CINGHIALI                                     \n"
        + "         FROM                                                      \n"
        + "             IUF_T_INFO_CINGHIALI                                \n"
        + "         WHERE                                                     \n"
        + "             ID_INFO_CINGHIALI = :ID_INFO_CINGHIALI                \n"
        + "     )                                                             \n";
    if(progressivoInfo!=null)
      QUERY+= " AND PROGR_INFO = :PROGR_INFO                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INFO_CINGHIALI", idInfoCinghiale,
          Types.NUMERIC);
      mapParameterSource.addValue("PROGR_INFO", progressivoInfo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idInfoCinghiale",idInfoCinghiale),
              new LogParameter("progressivoInfo",progressivoInfo)
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

  public PianoSelettivoDTO getPianoSelettivo(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                  \n"
      + "  C.ID_INFO_CINGHIALI,                                   \n"
      + "  C.CENSITI_ADULTI_M,                                    \n"
      + "  C.CENSITI_ADULTI_F,                                    \n"
      + "  C.CENSITI_PICCOLI_STRIATI,                             \n"
      + "  C.CENSITI_PICCOLI_ROSSI,                               \n"
      + "  C.PRELIEVO_ADULTI_M,                                   \n"
      + "  C.PRELIEVO_ADULTI_F,                                   \n"
      + "  C.PRELIEVO_PICCOLI,                                    \n"
      + "  M.ID_METODO_CENSIMENTO,                                \n"
      + "  C.VALORE_METODO_CENSIMENTO,                            \n"
      + "  C.DESCR_ALTRA_METOD,                                   \n"
      + "  C.DATA_AGGIORNAMENTO,                                  \n"
      + "  C.EXT_ID_UTENTE_AGGIORNAMENTO,                         \n"
      + "  U.ID_UNITA_MISURA,                                     \n"
      + "  U.CODICE DOC_UNITA_MISURA,                             \n"
      + "  U.DESCRIZIONE DESC_UNITA_MISURA,                       \n"
      + "  MC.DESC_METODOLOGIA                                     \n"
      + " FROM                                                    \n"
      + "   IUF_T_INFO_CINGHIALI C,                             \n"
      + "   IUF_R_METODO_CENS_SPECIE M,                          \n"
      + "   IUF_D_METODO_CENSIMENTO MC,                          \n"
      + "   IUF_D_UNITA_MISURA U                                \n"
      + " WHERE                                                   \n" 
      + "   C.ID_PROCEDIMENTO_OGGETTO=:ID_PROCEDIMENTO_OGGETTO    \n"
      + "   AND C.ID_METODO_SPECIE = M.ID_METODO_SPECIE and u.id_unita_misura = m.id_unita_misura AND MC.ID_METODO_CENSIMENTO = m.id_metodo_censimento\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto,
          Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource, PianoSelettivoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
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

  public Long inserisciInfoCinghiale(PianoSelettivoDTO piano,
      long idUtenteLogin) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT =    "INSERT INTO IUF_T_INFO_CINGHIALI (  \n"
        + "  ID_INFO_CINGHIALI,                  \n"
        + "  CENSITI_ADULTI_M,                   \n"
        + "  CENSITI_ADULTI_F,                   \n"
        + "  CENSITI_PICCOLI_STRIATI,            \n"
        + "  CENSITI_PICCOLI_ROSSI,              \n"
        + "  PRELIEVO_ADULTI_M,                  \n"
        + "  PRELIEVO_ADULTI_F,                  \n"
        + "  PRELIEVO_PICCOLI,                   \n"
        + "  ID_METODO_SPECIE,               \n"
        + "  VALORE_METODO_CENSIMENTO,           \n"
        + "  DESCR_ALTRA_METOD,                  \n"
        + "  DATA_AGGIORNAMENTO,                 \n"
        + "  EXT_ID_UTENTE_AGGIORNAMENTO,        \n"
        + "  ID_PROCEDIMENTO_OGGETTO             \n"
        + "  ) VALUES (                          \n"
        + "  :ID_INFO_CINGHIALI,                 \n"
        + "  :CENSITI_ADULTI_M,                  \n"
        + "  :CENSITI_ADULTI_F,                  \n"
        + "  :CENSITI_PICCOLI_STRIATI,           \n"
        + "  :CENSITI_PICCOLI_ROSSI,             \n"
        + "  :PRELIEVO_ADULTI_M,                 \n"
        + "  :PRELIEVO_ADULTI_F,                 \n"
        + "  :PRELIEVO_PICCOLI,                  \n"
        + "  :ID_METODO_CENSIMENTO,              \n"
        + "  :VALORE_METODO_CENSIMENTO,          \n"
        + "  :DESCR_ALTRA_METOD,                 \n"
        + "  SYSDATE,                            \n"
        + "  :EXT_ID_UTENTE_AGGIORNAMENTO,       \n"
        + "  :ID_PROCEDIMENTO_OGGETTO)           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    Long id = getNextSequenceValue("SEQ_IUF_T_INFO_CINGHIALI");
    try
    {
      mapParameterSource.addValue("ID_INFO_CINGHIALI", id , Types.NUMERIC); 
      mapParameterSource.addValue("CENSITI_ADULTI_M", piano.getCensitiAdultiM(),
          Types.NUMERIC);
      mapParameterSource.addValue("CENSITI_ADULTI_F", piano.getCensitiAdultiF(),
          Types.NUMERIC);
      mapParameterSource.addValue("CENSITI_PICCOLI_STRIATI",
          piano.getCensitiPiccoliStriati(), Types.NUMERIC);
      mapParameterSource.addValue("CENSITI_PICCOLI_ROSSI",
          piano.getCensitiPiccoliRossi(), Types.NUMERIC);
      mapParameterSource.addValue("PRELIEVO_ADULTI_M",
          piano.getPrelievoAdultiM(), Types.NUMERIC);
      mapParameterSource.addValue("PRELIEVO_ADULTI_F",
          piano.getPrelievoAdultiF(), Types.NUMERIC);
      mapParameterSource.addValue("PRELIEVO_PICCOLI",
          piano.getPrelievoPiccoli(), Types.NUMERIC);
   
      mapParameterSource.addValue("ID_METODO_CENSIMENTO",
          piano.getIdMetodoCensimento(), Types.NUMERIC);
      mapParameterSource.addValue("VALORE_METODO_CENSIMENTO",
          piano.getValoreMetodoCensimento(), Types.NUMERIC);
      mapParameterSource.addValue("DESCR_ALTRA_METOD",
          piano.getDescrAltraMetod(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          piano.getIdProcedimentoOggetto(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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
    return id;
  }

  public void updateInfoCinghiale(PianoSelettivoDTO piano,
      long extIdUtenteAggiornamento) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "UPDATE IUF_T_INFO_CINGHIALI SET                 \n"
        + "  CENSITI_ADULTI_M= :CENSITI_ADULTI_M,                            \n"
        + "  CENSITI_ADULTI_F= :CENSITI_ADULTI_F,                            \n"
        + "  CENSITI_PICCOLI_STRIATI= :CENSITI_PICCOLI_STRIATI,              \n"
        + "  CENSITI_PICCOLI_ROSSI= :CENSITI_PICCOLI_ROSSI,                  \n"
        + "  PRELIEVO_ADULTI_M= :PRELIEVO_ADULTI_M,                          \n"
        + "  PRELIEVO_ADULTI_F= :PRELIEVO_ADULTI_F,                          \n"
        + "  PRELIEVO_PICCOLI= :PRELIEVO_PICCOLI,                            \n"
        + "  ID_METODO_SPECIE= :ID_METODO_CENSIMENTO,                    \n"
        + "  VALORE_METODO_CENSIMENTO= :VALORE_METODO_CENSIMENTO,            \n"
        + "  DESCR_ALTRA_METOD= :DESCR_ALTRA_METOD,                          \n"
        + "  DATA_AGGIORNAMENTO= SYSDATE,                                    \n"
        + "  EXT_ID_UTENTE_AGGIORNAMENTO= :EXT_ID_UTENTE_AGGIORNAMENTO       \n"
        + " WHERE                                                            \n"
        + "  ID_PROCEDIMENTO_OGGETTO= :ID_PROCEDIMENTO_OGGETTO               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("CENSITI_ADULTI_M", piano.getCensitiAdultiM(),
          Types.NUMERIC);
      mapParameterSource.addValue("CENSITI_ADULTI_F", piano.getCensitiAdultiF(),
          Types.NUMERIC);
      mapParameterSource.addValue("CENSITI_PICCOLI_STRIATI",
          piano.getCensitiPiccoliStriati(), Types.NUMERIC);
      mapParameterSource.addValue("PRELIEVO_ADULTI_M",
          piano.getPrelievoAdultiM(), Types.NUMERIC);
      mapParameterSource.addValue("PRELIEVO_ADULTI_F",
          piano.getPrelievoAdultiF(), Types.NUMERIC);
      mapParameterSource.addValue("PRELIEVO_PICCOLI",
          piano.getPrelievoPiccoli(), Types.NUMERIC);
      mapParameterSource.addValue("CENSITI_PICCOLI_ROSSI",
          piano.getCensitiPiccoliRossi(), Types.NUMERIC);
      mapParameterSource.addValue("ID_METODO_CENSIMENTO",
          piano.getIdMetodoCensimento(), Types.NUMERIC);
      mapParameterSource.addValue("VALORE_METODO_CENSIMENTO",
          piano.getValoreMetodoCensimento(), Types.NUMERIC);
      mapParameterSource.addValue("DESCR_ALTRA_METOD",
          piano.getDescrAltraMetod(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          piano.getIdProcedimentoOggetto(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {  },
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

  public void inserisciDataCensimentoCinghiali(long idProcedimentoOggetto,
      Long idInfoCinghiali, Date data, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT =    "INSERT INTO IUF_T_DATE_CENS_CINGHIALI (  \n"
        + "  ID_DATE_CENS_CINGHIALI,             \n"
        + "  ID_INFO_CINGHIALI,                  \n"
        + "  PROGR_INFO,                         \n"
        + "  DATA_CENSIMENTO,                    \n"
        + "  DATA_AGGIORNAMENTO,                 \n"
        + "  EXT_ID_UTENTE_AGGIORNAMENTO,        \n"
        + "  ID_PROCEDIMENTO_OGGETTO             \n"
        + "  ) VALUES (                          \n"
        + "  :ID_DATE_CENS_CINGHIALI,            \n"
        + "  :ID_INFO_CINGHIALI,                 \n"
        + "  (SELECT NVL(MAX(PROGR_INFO),0)+1 FROM IUF_T_DATE_CENS_CINGHIALI WHERE ID_PROCEDIMENTO_OGGETTO=:ID_PROCEDIMENTO_OGGETTO),                 \n"
        + "  :DATA_CENSIMENTO,                   \n"
        + "  SYSDATE,                            \n"
        + "  :EXT_ID_UTENTE_AGGIORNAMENTO,       \n"
        + "  :ID_PROCEDIMENTO_OGGETTO)           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      Long id = getNextSequenceValue("SEQ_IUF_T_DATE_CENS_CINGHIAL");
      mapParameterSource.addValue("ID_DATE_CENS_CINGHIALI", id , Types.NUMERIC); 
      mapParameterSource.addValue("ID_INFO_CINGHIALI", idInfoCinghiali,
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_CENSIMENTO", data , Types.DATE); 
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
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

  public List<Date> getElencoDateCensimentoPianoSelettivo(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT                                    \n"
        + "    DATA_CENSIMENTO                                      \n"
        + "  FROM                                                   \n"
        + "    IUF_T_DATE_CENS_CINGHIALI                          \n"
        + "  WHERE                                                  \n"
        + "    ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO   \n"
        + "  ORDER BY                                               \n"
        + "     PROGR_INFO                                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<Date>>()
          {
            @Override
            public List<Date> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<Date> list = new ArrayList<Date>();
              while (rs.next())
              {
                Date data = rs.getDate("DATA_CENSIMENTO");
                list.add(data);
              }

              return list;
            }
          });
      }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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
  
  public Long callPckSmrgaaUtilityGraficheScriviParametri(
      Map<String, String> mapParametri)
      throws InternalUnexpectedException, ApplicationException
  {
    String THIS_METHOD = "callPckSmrgaaUtilityGraficheScriviParametri";
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
      }
      try
      {
      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
            .withCatalogName("SMRGAA.PCK_SMRGAA_UTILITY_GRAFICHE")
            .withProcedureName("ScriviParametri")
            .withoutProcedureColumnMetaDataAccess();
        setProcedureQueryTimeout(call);
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("pArrayParametri", new AbstractSqlTypeValue()
        {

        public Object createTypeValue(Connection con, int sqlType,
            String typeName) throws SQLException
          {
            Method method;
            try
            {
              method = con.getClass().getMethod("getUnderlyingConnection");
              con = (Connection) method.invoke(con, (Object[]) null);
            }
            catch (Exception e)
            {
            throw new SQLException(
                "Impossibile ottenere la connessione originale");
            }
            STRUCT[] array = new STRUCT[mapParametri.size()];
            int i = 0;  
          StructDescriptor desc = new StructDescriptor("SMRGAA.OBJ_PARAMETRI",
              con);
            for (String key : mapParametri.keySet())
            {
              String[] params = new String[]
              { key, (String) mapParametri.get(key) };
              array[i++] = new STRUCT(desc, con, params);
              }
          oracle.sql.ArrayDescriptor arrDesc = new oracle.sql.ArrayDescriptor(
              "SMRGAA.TBL_PARAMETRI", con);
            return new oracle.sql.ARRAY(arrDesc, con, array);
          }
        });
        args.put("pIdProcedimento", IuffiConstants.IUFFIWEB.ID_DANNI_FAUNA);
      call.addDeclaredParameter(
          new SqlParameter("pArrayParametri", Types.ARRAY));
      call.addDeclaredParameter(
          new SqlParameter("pIdProcedimento", Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("pIdTransitoParametroGrafico", Types.NUMERIC));
        call.addDeclaredParameter(new SqlOutParameter("pMsgErr", Types.VARCHAR));
        call.addDeclaredParameter(new SqlOutParameter("pCodErr", Types.NUMERIC));
        Map<String, Object> results = call.execute(args);
        BigDecimal token = null;
        if (((BigDecimal) results.get("pCodErr")).intValue() == 0)
        {
          token = (BigDecimal) results.get("pIdTransitoParametroGrafico");
        }
        else
        {
        throw new RuntimeException(
            "Errore nella scrittura dei parametri: codice: "
                + results.get("pCodErr")
          + ", descrizione: " + results.get("pMsgErr"));
        }
        return token == null ? null : token.longValue();
      }
      catch (Throwable t)
      {
        remapException(t, "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
        return null;
      }
      finally
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
        }
      }
  }
  
  private void remapException(Throwable t, String logHeader)
      throws InternalUnexpectedException, ApplicationException
  {
    if (t instanceof UncategorizedSQLException)
    {
      if (t.getCause() instanceof SQLTimeoutException)
      {
        logger.error(logHeader
            + " Si è verificato un errore di timeout nella chiamata PL/SQL.",
            t);
        throw new ApplicationException(
            "Si è verificato un problema con l'elaborazione della domanda grafica. Errore di timeout. In caso l'errore persista contattare l'assistenza tecnica.",
            IuffiConstants.SQL.SQL_TIMEOUT_EXCEPTION_CODE);
      }
    }
    InternalUnexpectedException e = new InternalUnexpectedException(
        t.getMessage(), t);
    logInternalUnexpectedException(e, logHeader);
    throw e;
  }
  
  protected void setProcedureQueryTimeout(SimpleJdbcCall call)
  {
    try
    {
      Map<String, String> mapParametri = getParametri(new String[]
      { "TIMEOUT_GRAFICA" });
      String sTimeout = mapParametri.get("TIMEOUT_GRAFICA");
      Integer secondi = IuffiUtils.NUMBERS.getIntegerNull(sTimeout);
      if (secondi == null)
      {
        logger.warn(
            "[PianoGraficoDAO.setProcedureQueryTimeout] il valore del parametro TIMEOUT_GRAFICA ["
                + sTimeout
            + "] non esistente o non valido, il query timeout viene impostato di default a 120 secondi");
        secondi = 120;
      }
      else
      {
        if (secondi < 60)
        {
          logger.warn(
              "[PianoGraficoDAO.setProcedureQueryTimeout] il valore del parametro TIMEOUT_GRAFICA ["
                  + sTimeout
              + "] è troppo basso, il query timeout viene impostato di default a 60 secondi");
          secondi = 60;
        }
      }
      if (secondi>=14400)
      {
        // Non si superano le 4 ore (meno 2 secondi per dare margine tra il
        // query timeout e la transazione)
        secondi = 14400-2;
      }
      call.getJdbcTemplate().setQueryTimeout(secondi);
    }
    catch (Exception e)
    {
      DumpUtils.logGenericException(logger, null, e, (LogParameter[]) null,
          null,
          "[" + THIS_CLASS + ".setProcedureQueryTimeout] ");
    }
  }
  
  public long getIdDichConsistenza(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH MAX_ISTANZA AS (SELECT MAX(C.ID_PROCEDIMENTO_OGGETTO) AS ID_PROCEDIMENTO_OGGETTO               \n"
        + "   FROM IUF_T_PROCEDIMENTO_OGGETTO A, IUF_T_PROCEDIMENTO B ,IUF_T_PROCEDIMENTO_OGGETTO C WHERE \n"
        + "   A.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO                                                             \n"
        + "   AND B.ID_PROCEDIMENTO = C.ID_PROCEDIMENTO                                                         \n"
        + "   AND A.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                          \n"
        + "   AND C.ID_PROCEDIMENTO_OGGETTO < :ID_PROCEDIMENTO_OGGETTO                                          \n"
        + "   )                                                                                                 \n"
        + " select                                                                                              \n"
        + "  NVL (EXT_ID_DICHIARAZIONE_CONSISTEN,                                                               \n"
        + "  (                                                                                                  \n"
        + "   SELECT A.EXT_ID_DICHIARAZIONE_CONSISTEN FROM IUF_T_PROCEDIMENTO_OGGETTO A, MAX_ISTANZA B        \n"
        + "   WHERE A.ID_PROCEDIMENTO_OGGETTO = B.ID_PROCEDIMENTO_OGGETTO                                       \n"
        + "  )                                                                                                  \n"
        + "  ) AS EXT_ID_DICHIARAZIONE_CONSISTEN                                                                \n"
        + " from                                                                                                \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO                                                                      \n"
        + " WHERE                                                                                               \n"
        + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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
  
  public List<DecodificaDTO<Long>> getZoneAltimetricheByIdAzienda(long idAzienda)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY =     
        " select distinct                                                                        \n"
            + "  a.id_zona_altimetrica AS ID,                                                          \n"
            + "  a.DESC_ZONA_ALTIMETRICA AS CODICE,                                                    \n"
            + "  a.DESC_ZONA_ALTIMETRICA AS DESCRIZIONE                                                \n"
            + "   from SMRGAA_V_UTE a where a.id_azienda = :ID_AZIENDA and a.data_fine_attivita_ute is null \n"
            + "   order by a.DESC_ZONA_ALTIMETRICA                                                     \n"
;

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_AZIENDA", idAzienda, Types.NUMERIC);
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {
                  new LogParameter("idAzienda", idAzienda)
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
  
  
  public DatiBilancioDTO getDatiBilancio(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    final String QUERY = " SELECT DAB.*,                           \n" +
        " (select distinct(desc_zona_altimetrica) from SMRGAA_V_UTE where ID_ZONA_ALTIMETRICA = DAB.EXT_ID_ZONA_ALTIMETRICA) as desc_zona_altimetrica   \n"
        +
        " FROM IUF_T_DATI_BILANCIO DAB     \n" + 
        " WHERE DAB.ID_PROCEDIMENTO_OGGETTO =:id_procedimento_oggetto ";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("id_procedimento_oggetto",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource, DatiBilancioDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {
                  new LogParameter("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto)
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
  
  public void deleteDatiBilancio(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "DELETE FROM IUF_T_DATI_BILANCIO WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    try
    {
      mapSqlParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto,
          Types.NUMERIC);   
      namedParameterJdbcTemplate.update(INSERT, mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("", null)
          },
          (LogVariable[]) null, INSERT, mapSqlParameterSource);
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
  
  public void insertDatiBilancio(DatiBilancioDTO dati, long utente)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO                        \n"
        + " IUF_T_DATI_BILANCIO                 \n"
        + " (                                  \n"
        + "    ID_DATI_BILANCIO,                \n"
        + "    EXT_ID_ZONA_ALTIMETRICA,                           \n"
        + "    DATA_SCAD_PRESTITO_PREC,                           \n"
        + "    FLAG_RITEN_ACCONTO,        \n"
        + "    TIPO_DOMANDA,                       \n"
        + "    DATA_ULTIMO_BILANCIO,                 \n"
        + "    IMP_CAPITALE_ANTIC_AMMISS,                    \n"
        + "    IMP_CREDITO_CLIENTI,                   \n"
        + "    IMP_FATTURATO,                   \n"
        + "    TEMPO_ESPOSIZ_DA_BILANCIO,                   \n"
        + "    TEMPO_ESPOSIZ_BENEF,                   \n"
        + "    DATA_DELIBERA_INTERVENTO,                   \n"
        + "    ID_PROCEDIMENTO_OGGETTO,                   \n"
        + "    MOTIVAZIONE_RIT_ACCONTO,                   \n"
        + "    DATA_AGGIORNAMENTO,                 \n"
        + "    EXT_ID_UTENTE_AGGIORNAMENTO,               \n"
        + "    IMPORTO_MATERIE_PRIME,                   \n"
        + "    IMPORTO_SERVIZI,                   \n"
        + "    IMPORTO_BENI_TERZI,                   \n"
        + "    IMPORTO_PERS_SALARI,                   \n"
        + "    IMPORTO_PERS_ONERI,                   \n"
        + "    IMPORTO_TOT_CONCEDIBILE                   \n"
        + " )                                  \n"
        + " VALUES                             \n"
        + " (                                  \n"
        + "    SEQ_IUF_T_DATI_BILANCIO.NEXTVAL, \n"
        + "    :EXT_ID_ZONA_ALTIMETRICA,                          \n"
        + "    :DATA_SCAD_PRESTITO_PREC,                          \n"
        + "    :FLAG_RITEN_ACCONTO,       \n"
        + "    :TIPO_DOMANDA,                      \n"
        + "    :DATA_ULTIMO_BILANCIO,                \n"
        + "    :IMP_CAPITALE_ANTIC_AMMISS,                   \n"
        + "    :IMP_CREDITO_CLIENTI,                  \n"
        + "    :IMP_FATTURATO,                  \n"
        + "    :TEMPO_ESPOSIZ_DA_BILANCIO,                  \n"
        + "    :TEMPO_ESPOSIZ_BENEF,                  \n"
        + "    :DATA_DELIBERA_INTERVENTO,                  \n"
        + "    :ID_PROCEDIMENTO_OGGETTO,                  \n"
        + "    :MOTIVAZIONE_RIT_ACCONTO,                  \n"
        + "    SYSDATE,               \n"
        + "    :EXT_ID_UTENTE_AGGIORNAMENTO,              \n"
        + "    :IMPORTO_MATERIE_PRIME,                   \n"
        + "    :IMPORTO_SERVIZI,                   \n"
        + "    :IMPORTO_BENI_TERZI,                   \n"
        + "    :IMPORTO_PERS_SALARI,                   \n"
        + "    :IMPORTO_PERS_ONERI,                   \n"
        + "    :IMPORTO_TOT_CONCEDIBILE                   \n"
        + " )                                  \n";

    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    try
    {
      mapSqlParameterSource.addValue("EXT_ID_ZONA_ALTIMETRICA",
          dati.getExtIdZonaAltimetrica(),
          Types.NUMERIC);   
      mapSqlParameterSource.addValue("DATA_SCAD_PRESTITO_PREC",
          dati.getDataScadPrestitoPrec(),
          Types.DATE);
      mapSqlParameterSource.addValue("FLAG_RITEN_ACCONTO",
          dati.getFlagRitenAcconto(),
          Types.VARCHAR);
      mapSqlParameterSource.addValue("TIPO_DOMANDA", dati.getTipoDomanda(),
          Types.VARCHAR);
      mapSqlParameterSource.addValue("DATA_ULTIMO_BILANCIO",
          dati.getDataUltimoBilancio(),
          Types.DATE);
      mapSqlParameterSource.addValue("IMP_CAPITALE_ANTIC_AMMISS",
          dati.getImpCapitaleAnticAmmiss(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("IMP_CREDITO_CLIENTI",
          dati.getImpCreditoClienti(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("IMP_FATTURATO", dati.getImpFatturato(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("TEMPO_ESPOSIZ_DA_BILANCIO",
          dati.getTempoEsposizDaBilancio(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("TEMPO_ESPOSIZ_BENEF",
          dati.getTempoEsposizBenef(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("DATA_DELIBERA_INTERVENTO",
          dati.getDataDeliberaIntervento(),
          Types.DATE);
      mapSqlParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          dati.getIdProcedimentoOggetto(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("MOTIVAZIONE_RIT_ACCONTO",
          dati.getMotivazioneRitAcconto(),
          Types.VARCHAR);
      mapSqlParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", utente,
          Types.NUMERIC);
      mapSqlParameterSource.addValue("IMPORTO_MATERIE_PRIME",
          dati.getImportoMateriePrime(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("IMPORTO_SERVIZI",
          dati.getImportoServizi(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("IMPORTO_BENI_TERZI",
          dati.getImportoBeniTerzi(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("IMPORTO_PERS_SALARI",
          dati.getImportoPersSalari(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("IMPORTO_PERS_ONERI",
          dati.getImportoPersOneri(),
          Types.NUMERIC);
      mapSqlParameterSource.addValue("IMPORTO_TOT_CONCEDIBILE",
          dati.getImportoTotConcedibile(),
          Types.NUMERIC);
     
      namedParameterJdbcTemplate.update(INSERT, mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("", null)
          },
          (LogVariable[]) null, INSERT, mapSqlParameterSource);
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
  
  public Long getIdUteMaxSuperficie(long idDichiarazioneConsistenza,
      boolean flagSauEqualsS) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        "select ID_UTE,SUM(SUPERFICIE_UTILIZZATA) from SMRGAA_V_CONDUZIONE_UTILIZZO \n"
            +
            "    where ID_DICHIARAZIONE_CONSISTENZA = :ID_DICHIARAZIONE_CONSISTENZA \n"
            +
        "    and  ID_TITOLO_POSSESSO <> 5 \n" );
    
    if(flagSauEqualsS)
      QUERY.append("    and FLAG_SAU = 'S' \n"); 
    else 
      QUERY.append("    and FLAG_SAU <> 'S' \n");
    
    QUERY.append("    GROUP BY ID_UTE \n" + 
        "    ORDER BY ID_UTE DESC");
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DICHIARAZIONE_CONSISTENZA",
          idDichiarazioneConsistenza, Types.NUMERIC);
      return queryForLong(QUERY.toString(), mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DICHIARAZIONE_CONSISTENZA",
                  idDichiarazioneConsistenza)
          }, new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public DecodificaDTO<Long> getZonAltimetricaByIdUte(long idUte)
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "select id_zona_altimetrica as id,desc_zona_altimetrica as descrizione, desc_zona_altimetrica as codice from SMRGAA_V_UTE where id_ute =:id_ute";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("id_ute",
          idUte, Types.NUMERIC);
      List<DecodificaDTO<Long>> list = queryForDecodificaLong(QUERY,
          mapParameterSource);
      if (list != null && !list.isEmpty())
      {
       return list.get(0); 
      }
      return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {
                  new LogParameter("id_ute", idUte)
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
  
  public List<DecodificaDTO<Long>> getDecodificheUtilizzo()
      throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT DISTINCT                         \n"
            + "     ID_UTILIZZO AS ID,                  \n"
            + "     CODICE_UTILIZZO AS CODICE,          \n"
            + "     DESCRIZIONE_UTILIZZO AS DESCRIZIONE \n"
            + " FROM                                    \n"
            + "     SMRGAA_V_MATRICE M                  \n"
            + " WHERE                                   \n"
            + "     ANNO_FINE_VALIDITA_UTILIZZO IS NULL \n"
            + " ORDER BY                                \n"
        + "     DESCRIZIONE_UTILIZZO                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
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

  public List<LogDTO> getElencoLog(Date istanzaDataDa, Date istanzaDataA,
      String rtesto) throws InternalUnexpectedException
  {
    String prep = "WHERE ";
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    String query = " SELECT                                                      \n"
            + "     L.ID_LOG,                                               \n"
            + "     L.DESCRIZIONE_ERRORE AS DESC_LOG,                       \n"
            + "     L.DATA_INSERIMENTO AS DATA_LOG                          \n"
            + " FROM                                                        \n" 
            + "     IUF_T_LOG L                                           \n";
    
    if (istanzaDataDa != null)
    {
      mapParameterSource.addValue("istanzaDataDa", istanzaDataDa, Types.DATE);
      query = query + prep + "L.DATA_INSERIMENTO >= :istanzaDataDa \n";
      prep = "AND ";
    }
    if (istanzaDataA != null)
    {
      mapParameterSource.addValue("istanzaDataA", istanzaDataA, Types.DATE);
      query = query + prep + "L.DATA_INSERIMENTO < :istanzaDataA \n";
      prep = "AND ";
    }
    if (rtesto != null && rtesto.length() > 0)
    {
      mapParameterSource.addValue("rtesto", "%" + rtesto + "%", Types.VARCHAR);
      query = query + prep + "L.DESCRIZIONE_ERRORE LIKE :rtesto \n";
    }
            
    query += " ORDER BY L.ID_LOG DESC                                     \n";
    
    try
    {
      return queryForList(query, mapParameterSource, LogDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          }, new LogVariable[]
          {}, query, mapParameterSource);
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

  public void updateProcedimentoPratica(long idPraticaConcessione) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "  UPDATE IUF_T_PROCEDIMENTO                              \n"
        + "    SET                                                                    \n" 
        + "    NUMERO_VERCOD = NULL,                                                  \n" 
        + "    DATA_VISURA = NULL                                                     \n" 
        + "    WHERE  ID_PROCEDIMENTO = (                                             \n"
        + "   SELECT ID_PROCEDIMENTO FROM IUF_T_PROCEDIMENTO_OGGETTO                 \n"
        + "   WHERE ID_PROCEDIMENTO_OGGETTO =                                         \n"
        + "     (SELECT ID_PROCEDIMENTO_OGGETTO FROM IUF_T_PRATICHE_CONCESSIONE     \n"
        + "       WHERE ID_PRATICHE_CONCESSIONE=:ID_PRATICA_CONCESSIONE))              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      mapParameterSource.addValue("ID_PRATICA_CONCESSIONE", idPraticaConcessione, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPraticaConcessione", idPraticaConcessione)
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

  public void updateProcedimentoPratiche(long idConcessione) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = "  UPDATE IUF_T_PROCEDIMENTO                              \n"
        + "    SET                                                                    \n" 
        + "    NUMERO_VERCOD = NULL,                                                  \n" 
        + "    DATA_VISURA = NULL                                                     \n" 
        + "    WHERE  ID_PROCEDIMENTO IN (                                            \n"
        + "   SELECT ID_PROCEDIMENTO FROM IUF_T_PROCEDIMENTO_OGGETTO                 \n"
        + "   WHERE ID_PROCEDIMENTO_OGGETTO IN                                        \n"
        + "     (SELECT ID_PROCEDIMENTO_OGGETTO FROM IUF_T_PRATICHE_CONCESSIONE     \n"
        + "       WHERE ID_CONCESSIONE=:ID_CONCESSIONE))                              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idConcessione", idConcessione)
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

  public String getStatoConcessione(long idConcessione) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT DESCRIZIONE                          \n"
        + " FROM                                                      \n"
        + "  IUF_T_CONCESSIONE C,                                   \n"
        + "  IUF_T_ITER_CONCESSIONE I,                              \n"
        + "  IUF_D_STATO_CONCESSIONE S                              \n"
        + " WHERE                                                     \n"
        + "   I.ID_STATO_CONCESSIONE = S.ID_STATO_CONCESSIONE         \n"
        + "   AND I.ID_CONCESSIONE = C.ID_CONCESSIONE                 \n"
        + "   AND C.ID_CONCESSIONE = :ID_CONCESSIONE                  \n"
        + "   AND I.DATA_FINE IS NULL                                 \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_CONCESSIONE", idConcessione, Types.NUMERIC);
      return queryForString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idConcessione", idConcessione) },
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

  public SoggettoDTO getSoggettoAnagrafePesca(String codiceFiscale)
      throws InternalUnexpectedException
  {
    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
      logger.debug(THIS_METHOD + " BEGIN.");
    final String QUERY = " SELECT                                                                                   \n"
        + "   A.ID_ANAGRAFE_PESCA,                                                                   \n"
        + "   A.FLAG_CITTADINO_ESTERO,                                                               \n"
        + "   A.CODICE_FISCALE,                                                                      \n"
        + "   A.COGNOME,                                                                             \n"
        + "   A.NOME,                                                                                \n"
        + "   A.SIGLA_PROVINCIA_RESIDENZA,                                                           \n"
        + "   A.EMAIL                                                                                \n"
        + " FROM                                                                                     \n"
        + "   IUF_T_ANAGRAFE_PESCA A                                                               \n"
        + " WHERE                                                                                    \n"
        + "   A.CODICE_FISCALE = :CODICE_FISCALE                                                     \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try {
      mapParameterSource.addValue("CODICE_FISCALE", codiceFiscale, Types.VARCHAR);
      return queryForObject(QUERY, mapParameterSource, SoggettoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[] {new LogParameter("codiceFiscale", codiceFiscale)}, new LogVariable[] {}, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
        logger.debug(THIS_METHOD + " END.");
    }
  }

  public String getNextCodiceFiscalePescaEstero()
    throws InternalUnexpectedException
  {
  String methodName = new Object()
  {
  }.getClass().getEnclosingMethod().getName();
  String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
  if (logger.isDebugEnabled())
  {
    logger.debug(THIS_METHOD + " BEGIN.");
  }
  final String QUERY =  " SELECT                                       \n"
      + "   LPAD(NVL(MAX(A.CODICE_FISCALE),0)+1,16,'0') as CODICE_FISCALE \n"
      + " FROM                                          \n"
      + "   IUF_T_ANAGRAFE_PESCA A                    \n"
      + " WHERE                                         \n"
        + "   A.FLAG_CITTADINO_ESTERO = 'S'        \n";
  MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
  try
  {
    return queryForString(QUERY, mapParameterSource);
  }
  catch (Throwable t)
  {
    InternalUnexpectedException e = new InternalUnexpectedException(t,
        new LogParameter[]
          {},
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

  public List<ImportoLicenzaDTO> getImportiLicenzePesca()
      throws InternalUnexpectedException
  {
  String methodName = new Object()
  {
  }.getClass().getEnclosingMethod().getName();
  String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
  if (logger.isDebugEnabled())
  {
    logger.debug(THIS_METHOD + " BEGIN.");
  }
    final String QUERY = " SELECT                                                   \n"
          + "   TL.ID_TIPO_LICENZA,                                    \n"
          + "   TL.DESCRIZIONE AS DESCR_TIPO_LICENZA,                  \n"
          + "   TL.DURATA,                                             \n"
          + "   SUM(IL.IMPORTO)  AS IMPORTO                                       \n"
          + " from                                                     \n"
          + "   IUF_R_IMPORTO_LICENZA IL,                            \n"
          + "   IUF_D_TIPO_LICENZA TL,                               \n"
          + "   IUF_D_ZONA_CACCIA_PESCA ZCP,                         \n"
          + "   IUF_D_TIPO_TASSA_LICENZA TTL                         \n"
          + " WHERE                                                    \n"
          + "   IL.ID_TIPO_TASSA_LICENZA = TTL.ID_TIPO_TASSA_LICENZA   \n"
          + "   AND IL.ID_TIPO_LICENZA = TL.ID_TIPO_LICENZA            \n"
          + "   AND TL.ID_ZONA_CACCIA_PESCA = ZCP.ID_ZONA_CACCIA_PESCA \n"
          + "   AND ZCP.DATA_FINE_VALIDITA IS NULL                     \n"
          + "   AND IL.DATA_FINE_VALIDITA IS NULL                      \n"
          + "   AND TL.DATA_FINE_VALIDITA IS NULL                      \n"
          + " group by TL.ID_TIPO_LICENZA,TL.DESCRIZIONE,TL.DURATA     \n"
        + " ORDER BY TL.DESCRIZIONE                                  \n";

  MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
  try
  {

    return queryForList(QUERY, mapParameterSource, ImportoLicenzaDTO.class);
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
  
  public List<ImportoLicenzaDTO> getAllImportiLicenzePesca()
      throws InternalUnexpectedException
  {
  String methodName = new Object()
  {
  }.getClass().getEnclosingMethod().getName();
  String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
  if (logger.isDebugEnabled())
  {
    logger.debug(THIS_METHOD + " BEGIN.");
  }
    final String QUERY = " SELECT                                      \n"
          + "   TL.ID_TIPO_LICENZA,                                    \n"
          + "   TL.DESCRIZIONE AS DESCR_TIPO_LICENZA,                  \n"
          + "   TL.DURATA,                                             \n"
          + "   TTL.DESCRIZIONE as DESCR_TASSA_TIPO_LICENZA,           \n"
          + "   IL.IMPORTO                                             \n"
          + " from                                                     \n"
          + "   IUF_R_IMPORTO_LICENZA IL,                            \n"
          + "   IUF_D_TIPO_LICENZA TL,                               \n"
          + "   IUF_D_ZONA_CACCIA_PESCA ZCP,                         \n"
          + "   IUF_D_TIPO_TASSA_LICENZA TTL                         \n"
          + " WHERE                                                    \n"
          + "   IL.ID_TIPO_TASSA_LICENZA = TTL.ID_TIPO_TASSA_LICENZA   \n"
          + "   AND IL.ID_TIPO_LICENZA = TL.ID_TIPO_LICENZA            \n"
          + "   AND TL.ID_ZONA_CACCIA_PESCA = ZCP.ID_ZONA_CACCIA_PESCA \n"
          + "   AND ZCP.DATA_FINE_VALIDITA IS NULL                     \n"
          + "   AND IL.DATA_FINE_VALIDITA IS NULL                      \n"
          + "   AND TL.DATA_FINE_VALIDITA IS NULL                      \n"
          //+ " group by TL.ID_TIPO_LICENZA,TL.DESCRIZIONE,TL.DURATA     \n"
        + " ORDER BY TL.DESCRIZIONE                                  \n";

  MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
  try
  {

    return queryForList(QUERY, mapParameterSource, ImportoLicenzaDTO.class);
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

  public void aggiornaAnagraficaPescatore(SoggettoDTO soggettoDTO)
      throws InternalUnexpectedException
  {
  String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
  String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
  if (logger.isDebugEnabled())
    logger.debug(THIS_METHOD + " BEGIN.");
  final String INSERT = " UPDATE IUF_T_ANAGRAFE_PESCA  SET     \n"
      + "   FLAG_CITTADINO_ESTERO = :FLAG_CITTADINO_ESTERO,        \n"
      + "   CODICE_FISCALE = :CODICE_FISCALE,                      \n"
      + "   COGNOME = :COGNOME,                \n"
      + "   NOME = :NOME,                      \n"
      + "   SIGLA_PROVINCIA_RESIDENZA = :SIGLA_PROVINCIA_RESIDENZA,\n"
      + "   EMAIL = :EMAIL                     \n"
      + "   WHERE ID_ANAGRAFE_PESCA = :ID_ANAGRAFE_PESCA           \n";
  MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
  try
  {
      mapParameterSource.addValue("ID_ANAGRAFE_PESCA", soggettoDTO.getIdAnagrafePesca(), Types.NUMERIC);
      mapParameterSource.addValue("FLAG_CITTADINO_ESTERO", soggettoDTO.getFlagCittadinoEstero(), Types.VARCHAR);
      mapParameterSource.addValue("CODICE_FISCALE", soggettoDTO.getCodiceFiscale(), Types.VARCHAR);
      mapParameterSource.addValue("COGNOME", soggettoDTO.getCognome(), Types.VARCHAR);
      mapParameterSource.addValue("NOME", soggettoDTO.getNome(), Types.VARCHAR);
      mapParameterSource.addValue("SIGLA_PROVINCIA_RESIDENZA", soggettoDTO.getSiglaProvinciaResidenza(), Types.VARCHAR);
      mapParameterSource.addValue("EMAIL", soggettoDTO.getEmail(), Types.VARCHAR);
    namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
  }
  catch (Throwable t)
  {
    InternalUnexpectedException e = new InternalUnexpectedException(t,
        new LogParameter[]
        {
              new LogParameter("ID_ANAGRAFE_PESCA",
                  soggettoDTO.getIdAnagrafePesca()),
              new LogParameter("FLAG_CITTADINO_ESTERO",
                  soggettoDTO.getFlagCittadinoEstero()),
              new LogParameter("CODICE_FISCALE",
                  soggettoDTO.getCodiceFiscale()),
            new LogParameter("COGNOME", soggettoDTO.getCognome()),
            new LogParameter("NOME", soggettoDTO.getNome()),
              new LogParameter("SIGLA_PROVINCIA_RESIDENZA",
                  soggettoDTO.getSiglaProvinciaResidenza()),
            new LogParameter("EMAIL", soggettoDTO.getEmail())

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
    return;
  }

  public Long inserisciAnagraficaPescatore(SoggettoDTO soggettoDTO)
      throws InternalUnexpectedException
  {
  String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
  String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
  if (logger.isDebugEnabled())
  {
    logger.debug(THIS_METHOD + " BEGIN.");
  }
  
  Long id_An = getNextSequenceValue("SEQ_IUF_T_ANAGRAFE_PESCA");
  
  final String INSERT = " INSERT INTO IUF_T_ANAGRAFE_PESCA (          \n"
      + "   ID_ANAGRAFE_PESCA,                          \n"
      + "   FLAG_CITTADINO_ESTERO,                      \n"
      + "   CODICE_FISCALE,                             \n"
      + "   COGNOME,                                    \n"
      + "   NOME,                                       \n"
      + "   SIGLA_PROVINCIA_RESIDENZA,                  \n"
      + "   EMAIL                                       \n"
      + " ) VALUES(                                     \n"
      + "   :ID_AN,           \n"
      + "   :FLAG_CITTADINO_ESTERO,                      \n"
      + "   :CODICE_FISCALE,                             \n"
      + "   :COGNOME,                                    \n"
      + "   :NOME,                                       \n"
      + "   :SIGLA_PROVINCIA_RESIDENZA,                  \n"
      + "   :EMAIL                                       \n"
      + " )                                             \n";
  MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
  try
  {
      mapParameterSource.addValue("ID_AN", id_An, Types.DECIMAL);
      mapParameterSource.addValue("FLAG_CITTADINO_ESTERO",
          soggettoDTO.getFlagCittadinoEstero(), Types.VARCHAR);
      mapParameterSource.addValue("CODICE_FISCALE",
          soggettoDTO.getCodiceFiscale(), Types.VARCHAR);
      mapParameterSource.addValue("COGNOME", soggettoDTO.getCognome(),
          Types.VARCHAR);
    mapParameterSource.addValue("NOME", soggettoDTO.getNome(), Types.VARCHAR);
      mapParameterSource.addValue("SIGLA_PROVINCIA_RESIDENZA",
          soggettoDTO.getSiglaProvinciaResidenza(), Types.VARCHAR);
      mapParameterSource.addValue("EMAIL", soggettoDTO.getEmail(),
          Types.VARCHAR);
    namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
  }
  catch (Throwable t)
  {
    InternalUnexpectedException e = new InternalUnexpectedException(t,
        new LogParameter[]
        {
              new LogParameter("FLAG_CITTADINO_ESTERO",
                  soggettoDTO.getFlagCittadinoEstero()),
              new LogParameter("CODICE_FISCALE",
                  soggettoDTO.getCodiceFiscale()),
            new LogParameter("COGNOME", soggettoDTO.getCognome()),
            new LogParameter("NOME", soggettoDTO.getNome()),
              new LogParameter("SIGLA_PROVINCIA_RESIDENZA",
                  soggettoDTO.getSiglaProvinciaResidenza()),
            new LogParameter("EMAIL", soggettoDTO.getEmail())

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
    return id_An;
  }
  
  public void inserisciPagamento(String iuv, String idAnagraficaPesca, String esito, String tariffa, String tipo_pagamento)  throws InternalUnexpectedException {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
      logger.debug(THIS_METHOD + " BEGIN.");
    
    String INSERT = " INSERT INTO IUF_R_PAGAMENTO_PESCA (\n"
        + "   ID_ANAGRAFE_PESCA,                      \n"
        + "   DATA_EMISSIONE_IUV,                     \n"
        + "   DATA_PAGAMENTO_IUV,                     \n"
        + "   ID_PAGAMENTO_PESCA,                     \n"
        + "   ID_TIPO_LICENZA,                        \n"
        + "   ID_TIPO_PAGAMENTO,                      \n"
        + "   IMPORTO,                                \n"
        + "   IUV                                     \n"
        + " ) VALUES(                                 \n"
        + "   :ID_ANAG,                               \n"
        + "   SYSDATE,                                \n";
        if(tipo_pagamento.equals("M1"))
            INSERT+= "   SYSDATE,                    \n";
        else
          INSERT+="   null,                    \n";
        INSERT+= "   SEQ_IUF_R_PAGAMENTO_PESCA.NEXTVAL,    \n"
        + "   :ID_TIPO_LICENZA,                       \n"
        + "   :ID_TIPO_PAGAMENTO,                     \n"
        + "   (select sum(importo) from IUF_r_importo_licenza where id_tipo_licenza=:ID_TIPO_LICENZA),                              \n"
        + "   :IUV                                    \n"
        + " )                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try{
        mapParameterSource.addValue("ID_ANAG", new BigDecimal(idAnagraficaPesca), Types.DECIMAL);
        mapParameterSource.addValue("ID_TIPO_LICENZA", new BigDecimal(tariffa), Types.DECIMAL);
        mapParameterSource.addValue("ID_TIPO_PAGAMENTO", tipo_pagamento.equals("M1")?1:2, Types.DECIMAL);
        mapParameterSource.addValue("IUV", iuv, Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }catch (Throwable t) {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[] {
             new LogParameter("ID_ANAG", idAnagraficaPesca),
             new LogParameter("ID_TIPO_LICENZA", tariffa),
             new LogParameter("ID_TIPO_PAGAMENTO", tipo_pagamento),
             new LogParameter("IUV", iuv)
          }, new LogVariable[] {}, INSERT, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }finally {
      if (logger.isDebugEnabled())
        logger.debug(THIS_METHOD + " END.");
    }
      return;
  }
}