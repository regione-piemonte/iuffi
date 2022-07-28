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
import java.util.TreeMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.RiduzioniSanzioniDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DocumentoAllegatoDownloadDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InterventoAccertamentoDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InterventoRendicontazioneDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaProspetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneDocumentiSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.TotaleContributoAccertamentoElencoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.UpdateSuperficieLocalizzazioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class RendicontazioneEAccertamentoSpeseDAO extends InterventiBaseDAO
{
  private static final String THIS_CLASS = RendicontazioneEAccertamentoSpeseDAO.class
      .getSimpleName();

  public List<TotaleContributoAccertamentoElencoDTO> getTotaleContributoErogabileNonErogabileESanzioni(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getTotaleContributoErogabileNonErogabileESanzioni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
      logger.debug(
          THIS_METHOD + " idProcedimentoOggetto = " + idProcedimentoOggetto);
    }
    final String QUERY = " SELECT                                                                 \n"
        + "   NVL(SUM(CONTRIBUTO_EROGABILE),0)     AS CONTRIBUTO_EROGABILE,        \n"
        + "   NVL(SUM(ARROTONDAMENTI),0)           AS COMPENSAZIONE_ARROTONDAMENTI,\n"
        + "   NVL(SUM(CONTRIBUTO_NON_EROGABILE),0) AS CONTRIBUTO_NON_EROGABILE,    \n"
        + "   NVL(SUM(IMPORTO_SANZIONI),0)         AS IMPORTO_SANZIONI,            \n"
        + "   L.CODICE AS CODICE_OPERAZIONE                                        \n"
        + " FROM                                                                   \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       NVL(POL.CONTRIBUTO_EROGABILE,0)     AS CONTRIBUTO_EROGABILE,     \n"
        + "       NVL(POL.ARROTONDAMENTI_CONTR_ABBATTUTO,0) AS ARROTONDAMENTI,     \n"
        + "       NVL(POL.CONTRIBUTO_NON_EROGABILE,0) AS CONTRIBUTO_NON_EROGABILE, \n"
        + "       0                                   AS IMPORTO_SANZIONI,         \n"
        + "       POL.ID_LIVELLO                                                   \n"
        + "     FROM                                                               \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL POL                               \n"
        + "     WHERE                                                              \n"
        + "       POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO           \n"
        + "     UNION                                                              \n"
        + "     SELECT                                                             \n"
        + "       0,0,0,                                                           \n"
        + "       NVL(POS.IMPORTO,0) AS IMPORTO_SANZIONI,                          \n"
        + "       POS.ID_LIVELLO                                                   \n"
        + "     FROM                                                               \n"
        + "       IUF_T_PROC_OGGETTO_SANZIONE POS                                  \n"
        + "     WHERE                                                              \n"
        + "       POS.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO           \n"
        + "   )                                                                    \n"
        + "   IMPORTI,                                                             \n"
        + "   IUF_D_LIVELLO L                                                      \n"
        + " WHERE                                                                  \n"
        + "   IMPORTI.ID_LIVELLO = L.ID_LIVELLO                                    \n"
        + " GROUP BY                                                               \n"
        + "   IMPORTI.ID_LIVELLO,                                                  \n"
        + "   L.CODICE                                                             \n"
        + " ORDER BY                                                               \n"
        + "   L.CODICE ASC                                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          TotaleContributoAccertamentoElencoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto",
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

  public int insertProcedimentoOggLivello(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertProcedimentoOggLivello";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String INSERT = " INSERT                                                                                             \n"
        + " INTO                                                                                               \n"
        + "   IUF_R_PROCEDIMENTO_OGG_LIVEL                                                                   \n"
        + "   (                                                                                                \n"
        + "     ID_PROCEDIMENTO_OGGETTO,                                                                       \n"
        + "     ID_LIVELLO,                                                                                    \n"
        + "     CONTRIBUTO_EROGABILE,                                                                          \n"
        + "     CONTRIBUTO_NON_EROGABILE,                                                                      \n"
        + "     CONTRIBUTO_CALCOLATO,                                                                          \n"
        + "     CONTRIBUTO_ABBATTUTO,                                                                          \n"
        + "     CONTRIBUTO_SANZIONABILE,                                                                       \n"
        + "     CONTRIBUTO_RISPENDIBILE                                                                        \n"
        + "   )                                                                                                \n"
        + "   (                                                                                                \n"
        + "     SELECT                                                                                         \n"
        + "       :ID_PROCEDIMENTO_OGGETTO,                                                                    \n"
        + "       ID_LIVELLO,                                                                                  \n"
        + "       0 AS CONTRIBUTO_EROGABILE,                                                                   \n"
        + "       0 AS CONTRIBUTO_NON_EROGABILE,                                                               \n"
        + "       CONTRIBUTO_CALCOLATO,                                                                        \n"
        + "       0 AS CONTRIBUTO_ABBATTUTO,                                                                   \n"
        + "       CONTRIBUTO_SANZIONABILE,                                                                     \n"
        + "       PARZIALE_CONTRIB_NON_RIC_RISP - CONTRIBUTO_SANZIONABILE AS CONTRIBUTO_RISPENDIBILE           \n"
        + "     FROM                                                                                           \n"
        + "       (                                                                                            \n"
        + "         SELECT                                                                                     \n"
        + "           LBI.ID_LIVELLO,                                                                          \n"
        + "           SUM(ASP.CONTRIBUTO_CALCOLATO) AS CONTRIBUTO_CALCOLATO,                                   \n"
        + "       CASE                                                                                         \n"
        + "         WHEN                                                                                       \n"
        + "           CASE                                                                                     \n"
        + "             WHEN SUM(DI.IMPORTO_CONTRIBUTO)> SUM(RS.IMPORTO_SPESA*DI.PERCENTUALE_CONTRIBUTO/100)   \n"
        + "             THEN SUM(RS.IMPORTO_SPESA                            *DI.PERCENTUALE_CONTRIBUTO/100)   \n"
        + "             ELSE SUM(DI.IMPORTO_CONTRIBUTO)                                                        \n"
        + "           END -                                                                                    \n"
        + "           CASE                                                                                     \n"
        + "             WHEN SUM(DI.IMPORTO_CONTRIBUTO)> SUM(ASP.IMPORTO_ACCERTATO*DI.PERCENTUALE_CONTRIBUTO/100)\n"
        + "             THEN SUM(ASP.IMPORTO_ACCERTATO                            *DI.PERCENTUALE_CONTRIBUTO/100)\n"
        + "             ELSE SUM(DI.IMPORTO_CONTRIBUTO)                                                        \n"
        + "           END > SUM(ASP.IMPORTO_NON_RICONOSCIUTO*DI.PERCENTUALE_CONTRIBUTO/100)                    \n"
        + "         THEN SUM(ASP.IMPORTO_NON_RICONOSCIUTO   *DI.PERCENTUALE_CONTRIBUTO/100)                    \n"
        + "         ELSE                                                                                       \n"
        + "           CASE                                                                                     \n"
        + "             WHEN SUM(DI.IMPORTO_CONTRIBUTO)> SUM(RS.IMPORTO_SPESA*DI.PERCENTUALE_CONTRIBUTO/100)   \n"
        + "             THEN SUM(RS.IMPORTO_SPESA                            *DI.PERCENTUALE_CONTRIBUTO/100)   \n"
        + "             ELSE SUM(DI.IMPORTO_CONTRIBUTO)                                                        \n"
        + "           END -                                                                                    \n"
        + "           CASE                                                                                     \n"
        + "             WHEN SUM(DI.IMPORTO_CONTRIBUTO)> SUM(ASP.IMPORTO_ACCERTATO*DI.PERCENTUALE_CONTRIBUTO/100)\n"
        + "             THEN SUM(ASP.IMPORTO_ACCERTATO                            *DI.PERCENTUALE_CONTRIBUTO/100)\n"
        + "             ELSE SUM(DI.IMPORTO_CONTRIBUTO)                                                        \n"
        + "           END                                                                                      \n"
        + "       END AS CONTRIBUTO_SANZIONABILE,                                                              \n"
        + "           SUM((RS.IMPORTO_SPESA - ASP.IMPORTO_CALCOLO_CONTRIBUTO)*DI.PERCENTUALE_CONTRIBUTO/100)   \n"
        + "              AS PARZIALE_CONTRIB_NON_RIC_RISP                                                      \n"
        + "         FROM                                                                                       \n"
        + "           IUF_T_RENDICONTAZIONE_SPESE RS,                                                          \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                                      \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO PO,                                                           \n"
        + "           IUF_T_PROCEDIMENTO P,                                                                    \n"
        + "           IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                                                      \n"
        + "           IUF_T_ACCERTAMENTO_SPESE ASP,                                                            \n"
        + "           IUF_R_LIV_BANDO_INTERVENTO LBI,                                                          \n"
        + "           IUF_T_INTERVENTO I,                                                                      \n"
        + "           IUF_T_DETTAGLIO_INTERVENTO DI                                                            \n"
        + "         WHERE                                                                                      \n"
        + "           PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "           AND PO_ORIG.ID_PROCEDIMENTO     = PO.ID_PROCEDIMENTO                                     \n"
        + "           AND RS.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO                             \n"
        + "           AND RS.ID_INTERVENTO = I.ID_INTERVENTO                                                   \n"
        + "           AND PO.CODICE_RAGGRUPPAMENTO    = PO_ORIG.CODICE_RAGGRUPPAMENTO                          \n"
        + "           AND PO.ID_PROCEDIMENTO_OGGETTO <> PO_ORIG.ID_PROCEDIMENTO_OGGETTO                        \n"
        + "           AND PO.ID_PROCEDIMENTO_OGGETTO  = IPO.ID_PROCEDIMENTO_OGGETTO                            \n"
        + "           AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                               \n"
        + "           AND IPO.DATA_FINE              IS NULL                                                   \n"
        + "           AND ASP.ID_PROCEDIMENTO_OGGETTO = PO_ORIG.ID_PROCEDIMENTO_OGGETTO                        \n"
        + "           AND RS.IMPORTO_SPESA            > 0                                                      \n"
        + "           AND PO_ORIG.ID_PROCEDIMENTO     = P.ID_PROCEDIMENTO                                      \n"
        + "           AND ASP.ID_INTERVENTO           = I.ID_INTERVENTO                                        \n"
        + "           AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO                          \n"
        + "           AND LBI.ID_BANDO                = P.ID_BANDO                                             \n"
        + "           AND I.ID_INTERVENTO             = DI.ID_INTERVENTO                                       \n"
        + "           AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                                                    \n"
        + "           AND DI.DATA_FINE               IS NULL                                                   \n"
        + "           AND PO.ID_PROCEDIMENTO_OGGETTO  =                                  \n"
        + "           (                                                                  \n"
        + "             SELECT                                                           \n"
        + "               MAX(PO2.ID_PROCEDIMENTO_OGGETTO)                               \n"
        + "             FROM                                                             \n"
        + "               IUF_T_PROCEDIMENTO_OGGETTO PO2,                                \n"
        + "               IUF_T_RENDICONTAZIONE_SPESE RS2,                               \n"
        + "               IUF_T_ITER_PROCEDIMENTO_OGGE IPO2                            \n"
        + "             WHERE                                                            \n"
        + "               PO2.ID_PROCEDIMENTO             = PO.ID_PROCEDIMENTO           \n"
        + "               AND PO2.CODICE_RAGGRUPPAMENTO   = PO.CODICE_RAGGRUPPAMENTO     \n"
        + "               AND PO2.ID_PROCEDIMENTO_OGGETTO = RS2.ID_PROCEDIMENTO_OGGETTO  \n"
        + "               AND PO2.ID_PROCEDIMENTO_OGGETTO = IPO2.ID_PROCEDIMENTO_OGGETTO \n"
        + "               AND IPO2.DATA_FINE             IS NULL                         \n"
        + "               AND IPO2.ID_STATO_OGGETTO BETWEEN 10 AND 90                    \n"
        + "           )                                                                  \n"

        + "         GROUP BY                                                                                   \n"
        + "           LBI.ID_LIVELLO                                                                           \n"
        + "       )                                                                                            \n"
        + "   )                                                                                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
          },
          new LogVariable[]
          {
          }, INSERT, mapParameterSource);
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

  public void deleteSanzioniAccertamentoAutomatiche(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "deleteSanzioniAccertamentoAutomatiche";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String INSERT = " DELETE FROM                                                          \n"
        + "   IUF_T_PROC_OGGETTO_SANZIONE                                        \n"
        + " WHERE                                                                \n"
        + "   ID_PROCEDIMENTO_OGGETTO       = :ID_PROCEDIMENTO_OGGETTO           \n"
        + "   AND ID_SANZIONE_INVESTIMENTO IN                                    \n"
        + "   (                                                                  \n"
        + "     SELECT                                                           \n"
        + "       SI.ID_SANZIONE_INVESTIMENTO                                    \n"
        + "     FROM                                                             \n"
        + "       IUF_D_SANZIONE_INVESTIMENTO SI                                 \n"
        + "     WHERE                                                            \n"
        + "       SI.ID_TIPOLOGIA_SANZIONE = 3                                   \n"
        + "   )                                                                  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
          },
          new LogVariable[]
          {
          }, INSERT, mapParameterSource);
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

  public void insertSanzioniAutomaticheAccertamento(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertSanzioniAutomaticheAccertamento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String INSERT = " INSERT INTO                                                                                    \n"
        + " IUF_T_PROC_OGGETTO_SANZIONE                                                                    \n"
        + " (                                                                                              \n"
        + "   ID_PROC_OGGETTO_SANZIONE,                                                                    \n"
        + "   ID_PROCEDIMENTO_OGGETTO,                                                                     \n"
        + "   ID_LIVELLO,                                                                                  \n"
        + "   ID_SANZIONE_INVESTIMENTO,                                                                    \n"
        + "   IMPORTO                                                                                      \n"
        + " )                                                                                              \n"
        + " (                                                                                              \n"
        + "   SELECT                                                                                       \n"
        + "     SEQ_IUF_T_PROC_OGGETTO_SANZI.NEXTVAL,                                                    \n"
        + "     :ID_PROCEDIMENTO_OGGETTO,                                                                  \n"
        + "     POS.ID_LIVELLO,                                                                            \n"
        + "     DSI.ID_SANZIONE_INVESTIMENTO,                                                              \n"
        + "     CASE WHEN POS.CONTRIBUTO_SANZIONABILE> POS.CONTRIBUTO_CALCOLATO \n"
        + "       THEN POS.CONTRIBUTO_CALCOLATO "
        + "       ELSE POS.CONTRIBUTO_SANZIONABILE "
        + "     END AS CONTRIBUTO_SANZIONABILE      \n"
        + "   FROM                                                                                         \n"
        + "     (                                                                                          \n"
        + "       SELECT                                                                                   \n"
        + "         L.CODICE_LIVELLO,                                                                      \n"
        + "         DECODE (NVL(SUM(POL.CONTRIBUTO_CALCOLATO),0), 0, DECODE(SUM(NVL(POL.CONTRIBUTO_SANZIONABILE,0)),0,0,1),                                    \n"
        + "                     NVL(SUM(POL.CONTRIBUTO_SANZIONABILE),0)/                                   \n"
        + "                     NVL(SUM(POL.CONTRIBUTO_CALCOLATO),0))*100 AS PERCENTUALE                   \n"
        + "       FROM                                                                                     \n"
        + "         IUF_R_PROCEDIMENTO_OGG_LIVEL POL,                                                    \n"
        + "         IUF_D_LIVELLO L                                                                        \n"
        + "       WHERE                                                                                    \n"
        + "         POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                 \n"
        + "         AND POL.ID_LIVELLO = L.ID_LIVELLO                                                      \n"
        + "       GROUP BY                                                                                 \n"
        + "           L.CODICE_LIVELLO                                                                     \n"
        + "     )                                                                                          \n"
        + "     C,                                                                                         \n"
        + "     IUF_R_PROCEDIMENTO_OGG_LIVEL POS,                                                        \n"
        + "     IUF_T_CONTROLLO_IN_LOCO_INVE CILI,                                                       \n"
        + "     IUF_D_SANZIONE_INVESTIMENTO DSI,                                                           \n"
        + "     IUF_D_LIVELLO L                                                                            \n"
        + "   WHERE                                                                                        \n"
        + "     C.CODICE_LIVELLO       = L.CODICE_LIVELLO                                                  \n"
        + "     AND C.PERCENTUALE               >= 10                                                      \n"
        + "     AND POS.CONTRIBUTO_SANZIONABILE > 0                                                        \n"
        + "     AND POS.CONTRIBUTO_CALCOLATO > 0                                                           \n"
        + "     AND POS.ID_LIVELLO = L.ID_LIVELLO                                                          \n"
        + "     AND CILI.ID_PROCEDIMENTO_OGGETTO(+) = POS.ID_PROCEDIMENTO_OGGETTO                          \n"
        + "     AND DSI.CODICE_IDENTIFICATIVO       = DECODE(NVL(CILI.FLAG_CONTROLLO,'N'),'S','S02','S01') \n"
        + "     AND DSI.DATA_FINE                  IS NULL                                                 \n"
        + "     AND POS.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                 \n"
        + " )                                                                                              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
          },
          new LogVariable[]
          {
          }, INSERT, mapParameterSource);
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

  public void updateContributoErogabileNonErogabileAcconto(
      long idProcedimentoOggetto, long idLivello)
      throws InternalUnexpectedException
  {
    {
      final String THIS_METHOD = "updateContributoErogabileNonErogabileAcconto";
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
      }
      String UPDATE = null;
      if (isImportoContributoSufficienteAcconto(idProcedimentoOggetto,
          idLivello))

      {
        UPDATE = " UPDATE                                                                \n"
            + "   IUF_R_PROCEDIMENTO_OGG_LIVEL POL                                  \n"
            + " SET                                                                   \n"
            + "   POL.CONTRIBUTO_EROGABILE = POL.CONTRIBUTO_CALCOLATO -               \n"
            + "   (                                                                   \n"
            + "     SELECT                                                            \n"
            + "       NVL(SUM(IMPORTO),0)                                             \n"
            + "     FROM                                                              \n"
            + "       IUF_T_PROC_OGGETTO_SANZIONE POS                                 \n"
            + "     WHERE                                                             \n"
            + "       POS.ID_PROCEDIMENTO_OGGETTO = POL.ID_PROCEDIMENTO_OGGETTO       \n"
            + "       AND POS.ID_LIVELLO          = POL.ID_LIVELLO                    \n"
            + "   )                                                                   \n"
            + "   ,                                                                   \n"
            + "   POL.CONTRIBUTO_NON_EROGABILE = POL.CONTRIBUTO_SANZIONABILE+         \n"
            + "   (                                                                   \n"
            + "     SELECT                                                            \n"
            + "       NVL(SUM(IMPORTO),0)                                             \n"
            + "     FROM                                                              \n"
            + "       IUF_T_PROC_OGGETTO_SANZIONE POS                                 \n"
            + "     WHERE                                                             \n"
            + "       POS.ID_PROCEDIMENTO_OGGETTO = POL.ID_PROCEDIMENTO_OGGETTO       \n"
            + "       AND POS.ID_LIVELLO          = POL.ID_LIVELLO                    \n"
            + "   )                                                                   \n"
            + " WHERE                                                                 \n"
            + "   POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO              \n"
            + "   AND POL.ID_LIVELLO = :ID_LIVELLO                                    \n";
      }
      else
      {
        UPDATE = " UPDATE                                                                \n"
            + "   IUF_R_PROCEDIMENTO_OGG_LIVEL POL                                  \n"
            + " SET                                                                   \n"
            + "   POL.CONTRIBUTO_EROGABILE =                                          \n"
            + "   (                                                                   \n"
            + "     SELECT                                                            \n"
            + "       NVL(SUM(DI.IMPORTO_CONTRIBUTO),0) AS IMPORTO_CONTRIBUTO         \n"
            + "     FROM                                                              \n"
            + "       IUF_T_INTERVENTO I,                                             \n"
            + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                  \n"
            + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                  \n"
            + "       IUF_T_PROCEDIMENTO P,                                           \n"
            + "       IUF_R_LIV_BANDO_INTERVENTO LBI                                  \n"
            + "     WHERE                                                             \n"
            + "       PO.ID_PROCEDIMENTO              = I.ID_PROCEDIMENTO             \n"
            + "       AND PO.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO      \n"
            + "       AND I.ID_INTERVENTO             = DI.ID_INTERVENTO              \n"
            + "       AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO \n"
            + "       AND P.ID_PROCEDIMENTO           = PO.ID_PROCEDIMENTO            \n"
            + "       AND LBI.ID_BANDO                = P.ID_BANDO                    \n"
            + "       AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                           \n"
            + "       AND DI.DATA_FINE               IS NULL                          \n"
            + "       AND LBI.ID_LIVELLO              = POL.ID_LIVELLO                \n"
            + "   )                                                                   \n"
            + "   -                                                                   \n"
            + "   (                                                                   \n"
            + "     SELECT                                                            \n"
            + "       NVL(SUM(PL.CONTRIBUTO_EROGABILE +                               \n"
            + "           PL.CONTRIBUTO_NON_EROGABILE),0) AS CONTRIBUTO               \n"
            + "     FROM                                                              \n"
            + "       IUF_R_PROCEDIMENTO_LIVELLO PL,                                  \n"
            + "       IUF_T_PROCEDIMENTO_OGGETTO PO                                   \n"
            + "     WHERE                                                             \n"
            + "       PL.ID_PROCEDIMENTO             = PO.ID_PROCEDIMENTO             \n"
            + "       AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       \n"
            + "       AND PL.ID_LIVELLO = POL.ID_LIVELLO                              \n"
            + "   )                                                                   \n"
            + "   - POL.CONTRIBUTO_SANZIONABILE -                                     \n"
            + "   (                                                                   \n"
            + "     SELECT                                                            \n"
            + "       NVL(SUM(IMPORTO),0)                                             \n"
            + "     FROM                                                              \n"
            + "       IUF_T_PROC_OGGETTO_SANZIONE POS                                 \n"
            + "     WHERE                                                             \n"
            + "       POS.ID_PROCEDIMENTO_OGGETTO = POL.ID_PROCEDIMENTO_OGGETTO       \n"
            + "       AND POS.ID_LIVELLO          = POL.ID_LIVELLO                    \n"
            + "   )                                                                   \n"
            + "   ,                                                                   \n"
            + "   POL.CONTRIBUTO_NON_EROGABILE = POL.CONTRIBUTO_SANZIONABILE+         \n"
            + "   (                                                                   \n"
            + "     SELECT                                                            \n"
            + "       NVL(SUM(IMPORTO),0)                                             \n"
            + "     FROM                                                              \n"
            + "       IUF_T_PROC_OGGETTO_SANZIONE POS                                 \n"
            + "     WHERE                                                             \n"
            + "       POS.ID_PROCEDIMENTO_OGGETTO = POL.ID_PROCEDIMENTO_OGGETTO       \n"
            + "       AND POS.ID_LIVELLO          = POL.ID_LIVELLO                    \n"
            + "   )                                                                   \n"
            + " WHERE                                                                 \n"
            + "   POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO              \n"
            + "   AND POL.ID_LIVELLO = :ID_LIVELLO                                    \n";
      }
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      try
      {
        namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      }
      catch (Throwable t)
      {
        InternalUnexpectedException e = new InternalUnexpectedException(t,
            new LogParameter[]
            {
                new LogParameter("idProcedimentoOggetto",
                    idProcedimentoOggetto),
                new LogParameter("idLivello", idLivello)
            },
            new LogVariable[]
            {
            }, UPDATE, mapParameterSource);
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

  public void updateContributoErogabileNonErogabileSaldo(
      long idProcedimentoOggetto, long idLivello)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateContributoErogabileNonErogabileSaldo";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String UPDATE = null;
    if (isImportoContributoSufficienteSaldo(idProcedimentoOggetto, idLivello))
    {
      UPDATE = " UPDATE                                                                \n"
          + "   IUF_R_PROCEDIMENTO_OGG_LIVEL POL                                  \n"
          + " SET                                                                   \n"
          + "   POL.CONTRIBUTO_EROGABILE = POL.CONTRIBUTO_CALCOLATO -               \n"
          + "   (                                                                   \n"
          + "     SELECT                                                            \n"
          + "       NVL(SUM(IMPORTO),0)                                             \n"
          + "     FROM                                                              \n"
          + "       IUF_T_PROC_OGGETTO_SANZIONE POS                                 \n"
          + "     WHERE                                                             \n"
          + "       POS.ID_PROCEDIMENTO_OGGETTO = POL.ID_PROCEDIMENTO_OGGETTO       \n"
          + "       AND POS.ID_LIVELLO          = POL.ID_LIVELLO                    \n"
          + "   )                                                                   \n"
          + "   -                                                                   \n"
          + "   (                                                                   \n"
          + "   SELECT                                                              \n"
          + "     NVL(SUM(NVL(AL.IMPORTO_ANTICIPO,0)),0) AS IMPORTO_ANTICIPO        \n"
          + "   FROM                                                                \n"
          + "     IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                               \n"
          + "     IUF_T_ANTICIPO A,                                                 \n"
          + "     IUF_R_ANTICIPO_LIVELLO AL                                         \n"
          + "   WHERE                                                               \n"
          + "     PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
          + "     AND A.ID_PROCEDIMENTO           = PO_ORIG.ID_PROCEDIMENTO         \n"
          + "     AND A.ID_STATO_OGGETTO          = 17                              \n"
          + "     AND A.ID_ANTICIPO               = AL.ID_ANTICIPO                  \n"
          + "     AND AL.ID_LIVELLO               = POL.ID_LIVELLO                  \n"
          + "   )                                                                   \n"
          + " WHERE                                                                 \n"
          + "   POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO              \n"
          + "   AND POL.ID_LIVELLO = :ID_LIVELLO                                    \n";
    }
    else
    {
      UPDATE = " UPDATE                                                                              \n"
          + "   IUF_R_PROCEDIMENTO_OGG_LIVEL POL                                                \n"
          + " SET                                                                                 \n"
          + "   POL.CONTRIBUTO_EROGABILE =                                                        \n"
          + "   (                                                                                 \n"
          + "     SELECT                                                                          \n"
          + "       NVL(SUM(DI.IMPORTO_CONTRIBUTO),0) AS IMPORTO_CONTRIBUTO                       \n"
          + "     FROM                                                                            \n"
          + "       IUF_T_INTERVENTO I,                                                           \n"
          + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                \n"
          + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                \n"
          + "       IUF_T_PROCEDIMENTO P,                                                         \n"
          + "       IUF_R_LIV_BANDO_INTERVENTO LBI                                                \n"
          + "     WHERE                                                                           \n"
          + "       PO.ID_PROCEDIMENTO              = I.ID_PROCEDIMENTO                           \n"
          + "       AND PO.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO                    \n"
          + "       AND I.ID_INTERVENTO             = DI.ID_INTERVENTO                            \n"
          + "       AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO               \n"
          + "       AND P.ID_PROCEDIMENTO           = PO.ID_PROCEDIMENTO                          \n"
          + "       AND LBI.ID_BANDO                = P.ID_BANDO                                  \n"
          + "       AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                                         \n"
          + "       AND DI.DATA_FINE               IS NULL                                        \n"
          + "       AND LBI.ID_LIVELLO              = POL.ID_LIVELLO                              \n"
          + "   )                                                                                 \n"
          + "   -                                                                                 \n"
          + "   (                                                                                 \n"
          + "     SELECT                                                                          \n"
          + "       NVL(SUM(PL.CONTRIBUTO_EROGABILE+PL.CONTRIBUTO_NON_EROGABILE),0) AS CONTRIBUTO \n"
          + "     FROM                                                                            \n"
          + "       IUF_R_PROCEDIMENTO_LIVELLO PL,                                                \n"
          + "       IUF_T_PROCEDIMENTO_OGGETTO PO                                                 \n"
          + "     WHERE                                                                           \n"
          + "       PL.ID_PROCEDIMENTO             = PO.ID_PROCEDIMENTO                           \n"
          + "       AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
          + "       AND PL.ID_LIVELLO = POL.ID_LIVELLO                                            \n"
          + "   ) -                                                                               \n"
          + "   (                                                                                 \n"
          + "     SELECT                                                                          \n"
          + "       NVL(SUM(IMPORTO),0)                                                           \n"
          + "     FROM                                                                            \n"
          + "       IUF_T_PROC_OGGETTO_SANZIONE POS                                               \n"
          + "     WHERE                                                                           \n"
          + "       POS.ID_PROCEDIMENTO_OGGETTO = POL.ID_PROCEDIMENTO_OGGETTO                     \n"
          + "       AND POS.ID_LIVELLO          = POL.ID_LIVELLO                                  \n"
          + "   )                                                                                 \n"
          + "   -                                                                                 \n"
          + "   POL.CONTRIBUTO_SANZIONABILE                                                       \n"
          + " WHERE                                                                               \n"
          + "   POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                            \n"
          + "   AND POL.ID_LIVELLO = :ID_LIVELLO                                                  \n";
    }
    final String QUERY_UPDATE_NON_EROGABILE = " UPDATE                                                                                                                                 \n"
        + "   IUF_R_PROCEDIMENTO_OGG_LIVEL POL2                                \n"
        + " SET                                                                  \n"
        + "   CONTRIBUTO_NON_EROGABILE =                                         \n"
        + "   (                                                                  \n"
        + "     SELECT                                                           \n"
        + "       POL.CONTRIBUTO_SANZIONABILE +                                  \n"
        + "   (                                                                \n"
        + "     SELECT                                                         \n"
        + "       NVL(SUM(IMPORTO),0)                                          \n"
        + "     FROM                                                           \n"
        + "       IUF_T_PROC_OGGETTO_SANZIONE POS                              \n"
        + "     WHERE                                                          \n"
        + "       POS.ID_PROCEDIMENTO_OGGETTO = POL.ID_PROCEDIMENTO_OGGETTO    \n"
        + "       AND POS.ID_LIVELLO          = POL.ID_LIVELLO                 \n"
        + "   )                                                                \n"
        + "     FROM                                                             \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL POL                             \n"
        + "     WHERE                                                            \n"
        + "       POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO         \n"
        + "       AND POL.ID_PROCEDIMENTO_OGGETTO = POL2.ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND POL.ID_LIVELLO = POL2.ID_LIVELLO                           \n"
        + "   )                                                                  \n"
        + " WHERE                                                                \n"
        + "   POL2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO            \n"
        + "   AND POL2.ID_LIVELLO = :ID_LIVELLO                                  \n";
    final String QUERY_UPDATE_NON_EROGABILE_SE_INFERIORE = " UPDATE                                                                    \n"
        + "   IUF_R_PROCEDIMENTO_OGG_LIVEL PROCOGGLIV                               \n"
        + " SET                                                                       \n"
        + "   CONTRIBUTO_NON_EROGABILE =                                              \n"
        + "   (                                                                       \n"
        + "     SELECT                                                                \n"
        + "       CONTRIBUTO.IMPORTO_CONTRIBUTO -                                     \n"
        + "       PL.CONTRIBUTO_EROGABILE -                                           \n"
        + "       PL.CONTRIBUTO_NON_EROGABILE -                                       \n"
        + "       POL.CONTRIBUTO_EROGABILE                                            \n"
        + "     FROM                                                                  \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL POL,                                 \n"
        + "       (                                                                   \n"
        + "         SELECT                                                            \n"
        + "           NVL(SUM(DI.IMPORTO_CONTRIBUTO),0) AS IMPORTO_CONTRIBUTO,        \n"
        + "           LBI.ID_LIVELLO,                                                 \n"
        + "           PO.ID_PROCEDIMENTO                                              \n"
        + "         FROM                                                              \n"
        + "           IUF_T_INTERVENTO I,                                             \n"
        + "           IUF_T_DETTAGLIO_INTERVENTO DI,                                  \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO PO,                                  \n"
        + "           IUF_T_PROCEDIMENTO P,                                           \n"
        + "           IUF_R_LIV_BANDO_INTERVENTO LBI                                  \n"
        + "         WHERE                                                             \n"
        + "           PO.ID_PROCEDIMENTO              = I.ID_PROCEDIMENTO             \n"
        + "           AND PO.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO      \n"
        + "           AND I.ID_INTERVENTO             = DI.ID_INTERVENTO              \n"
        + "           AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO \n"
        + "           AND P.ID_PROCEDIMENTO           = PO.ID_PROCEDIMENTO            \n"
        + "           AND LBI.ID_BANDO                = P.ID_BANDO                    \n"
        + "           AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                           \n"
        + "           AND DI.DATA_FINE               IS NULL                          \n"
        + "         GROUP BY                                                          \n"
        + "           LBI.ID_LIVELLO,                                                 \n"
        + "           PO.ID_PROCEDIMENTO                                              \n"
        + "       )                                                                   \n"
        + "       CONTRIBUTO,                                                         \n"
        + "       IUF_R_PROCEDIMENTO_LIVELLO PL                                       \n"
        + "     WHERE                                                                 \n"
        + "       POL.ID_PROCEDIMENTO_OGGETTO    = PROCOGGLIV.ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND CONTRIBUTO.ID_LIVELLO      = POL.ID_LIVELLO                     \n"
        + "       AND PL.ID_LIVELLO              = POL.ID_LIVELLO                     \n"
        + "       AND CONTRIBUTO.ID_PROCEDIMENTO = PL.ID_PROCEDIMENTO                 \n"
        + "       AND POL.ID_LIVELLO             = PROCOGGLIV.ID_LIVELLO              \n"
        + "   )                                                                       \n"
        + " WHERE                                                                     \n"
        + "   PROCOGGLIV.CONTRIBUTO_NON_EROGABILE <                                   \n"
        + "   (                                                                       \n"
        + "     SELECT                                                                \n"
        + "       CONTRIBUTO.IMPORTO_CONTRIBUTO -                                     \n"
        + "       PL.CONTRIBUTO_EROGABILE -                                           \n"
        + "       PL.CONTRIBUTO_NON_EROGABILE -                                       \n"
        + "       POL.CONTRIBUTO_EROGABILE                                            \n"
        + "     FROM                                                                  \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL POL,                                 \n"
        + "       (                                                                   \n"
        + "         SELECT                                                            \n"
        + "           NVL(SUM(DI.IMPORTO_CONTRIBUTO),0) AS IMPORTO_CONTRIBUTO,        \n"
        + "           LBI.ID_LIVELLO,                                                 \n"
        + "           PO.ID_PROCEDIMENTO                                              \n"
        + "         FROM                                                              \n"
        + "           IUF_T_INTERVENTO I,                                             \n"
        + "           IUF_T_DETTAGLIO_INTERVENTO DI,                                  \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO PO,                                  \n"
        + "           IUF_T_PROCEDIMENTO P,                                           \n"
        + "           IUF_R_LIV_BANDO_INTERVENTO LBI                                  \n"
        + "         WHERE                                                             \n"
        + "           PO.ID_PROCEDIMENTO              = I.ID_PROCEDIMENTO             \n"
        + "           AND PO.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO      \n"
        + "           AND I.ID_INTERVENTO             = DI.ID_INTERVENTO              \n"
        + "           AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO \n"
        + "           AND P.ID_PROCEDIMENTO           = PO.ID_PROCEDIMENTO            \n"
        + "           AND LBI.ID_BANDO                = P.ID_BANDO                    \n"
        + "           AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                           \n"
        + "           AND DI.DATA_FINE               IS NULL                          \n"
        + "         GROUP BY                                                          \n"
        + "           LBI.ID_LIVELLO,                                                 \n"
        + "           PO.ID_PROCEDIMENTO                                              \n"
        + "       )                                                                   \n"
        + "       CONTRIBUTO,                                                         \n"
        + "       IUF_R_PROCEDIMENTO_LIVELLO PL                                       \n"
        + "     WHERE                                                                 \n"
        + "       POL.ID_PROCEDIMENTO_OGGETTO    = :ID_PROCEDIMENTO_OGGETTO           \n"
        + "       AND CONTRIBUTO.ID_LIVELLO      = POL.ID_LIVELLO                     \n"
        + "       AND PL.ID_LIVELLO              = POL.ID_LIVELLO                     \n"
        + "       AND CONTRIBUTO.ID_PROCEDIMENTO = PL.ID_PROCEDIMENTO                 \n"
        + "       AND POL.ID_LIVELLO             = PROCOGGLIV.ID_LIVELLO              \n"
        + "   )                                                                       \n"
        + "   AND PROCOGGLIV.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       \n";

    final String UPDATE_NEGATIVI = " UPDATE                                                                        \n"
        + "   IUF_R_PROCEDIMENTO_OGG_LIVEL                         \n"
        + " SET                                                      \n"
        + "   CONTRIBUTO_EROGABILE     = 0                           \n"
        + " WHERE                                                    \n"
        + "   CONTRIBUTO_EROGABILE < 0                               \n"
        + "   AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "   AND ID_LIVELLO = :ID_LIVELLO                       \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      /*
       * Nel caso che i calcoli precedenti abbiano generato, per uno o più
       * livello, un valore del contributo erogabile negativo devo intervenire
       * per sanare la situazione. Aggiungo il valore assoluto del contributo
       * erogabile al contributo non erogabile e azzero il contributo erogabile.
       */
      namedParameterJdbcTemplate.update(UPDATE_NEGATIVI, mapParameterSource);
      /*
       * A questo punto aggiorno il contributo non erogabile, basandomi sui
       * contributo erogabile calcolato precedentemente, il calcolo, al
       * contrario di prima è sempre lo stesso
       */
      namedParameterJdbcTemplate.update(QUERY_UPDATE_NON_EROGABILE,
          mapParameterSource);
      /*
       * Eseguo un secondo update dell'erogabile aggiornando solo quei record
       * inferiori ad un certo valore. E' il modo più veloce ed efficiente che
       * ho trovato per fare l'update con il max(x,y), prima faccio l'update con
       * x e poi faccio l'update con y solo su quelli in cui x è < y. Per il
       * tipo di calcolo di x e y vedere l'analisi
       */
      namedParameterJdbcTemplate.update(QUERY_UPDATE_NON_EROGABILE_SE_INFERIORE,
          mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
          },
          new LogVariable[]
          {
          }, UPDATE, mapParameterSource);
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

  public boolean isImportoContributoSufficienteSaldo(long idProcedimentoOggetto,
      long idLivello)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "isImportoContributoSufficienteSaldo";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                                \n"
        + "   IMPORTO_CONTRIBUTO AS                                                             \n"
        + "   (                                                                                 \n"
        + "     SELECT                                                                          \n"
        + "       NVL(SUM(DI.IMPORTO_CONTRIBUTO),0) AS TOTALE                                   \n"
        + "     FROM                                                                            \n"
        + "       IUF_T_INTERVENTO I,                                                           \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                \n"
        + "       IUF_T_PROCEDIMENTO P,                                                         \n"
        + "       IUF_R_LIV_BANDO_INTERVENTO LBI                                                \n"
        + "     WHERE                                                                           \n"
        + "       PO.ID_PROCEDIMENTO              = I.ID_PROCEDIMENTO                           \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "       AND I.ID_INTERVENTO             = DI.ID_INTERVENTO                            \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO               \n"
        + "       AND P.ID_PROCEDIMENTO           = PO.ID_PROCEDIMENTO                          \n"
        + "       AND LBI.ID_BANDO                = P.ID_BANDO                                  \n"
        + "       AND LBI.ID_LIVELLO              = :ID_LIVELLO                                 \n"
        + "       AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                                         \n"
        + "       AND DI.DATA_FINE               IS NULL                                        \n"
        + "   )                                                                                 \n"
        + "   ,                                                                                 \n"
        + "   CALCOLATO_E_SANZIONABILE AS                                                       \n"
        + "   (                                                                                 \n"
        + "     SELECT                                                                          \n"
        + "       NVL(SUM(POL.CONTRIBUTO_CALCOLATO +POL.CONTRIBUTO_SANZIONABILE),0) AS TOTALE   \n"
        + "     FROM                                                                            \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL POL                                            \n"
        + "     WHERE                                                                           \n"
        + "       POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                        \n"
        + "       AND POL.ID_LIVELLO = :ID_LIVELLO                                              \n"
        + "   )                                                                                 \n"
        + "   ,                                                                                 \n"
        + "   CONTRIBUTO_EROGABILE_PREC AS                                                      \n"
        + "   (                                                                                 \n"
        + "     SELECT                                                                          \n"
        + "       NVL(SUM(RPOL.CONTRIBUTO_EROGABILE + RPOL.CONTRIBUTO_NON_EROGABILE),0)         \n"
        + "                                                                       AS CONTRIBUTO \n"
        + "     FROM                                                                            \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL RPOL,                                          \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO TPOD,                                              \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO TPOI,                                              \n"
        + "       IUF_D_ESITO DE                                                                \n"
        + "     WHERE                                                                           \n"
        + "       TPOI.ID_PROCEDIMENTO_OGGETTO = RPOL.ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       AND TPOI.ID_ESITO            = DE.ID_ESITO                                    \n"
        + "       AND DE.CODICE = 'APP-P'                                                        \n"
        + "       AND TPOI.ID_PROCEDIMENTO_OGGETTO < TPOD.ID_PROCEDIMENTO_OGGETTO               \n"
        + "       AND TPOI.ID_PROCEDIMENTO         = TPOD.ID_PROCEDIMENTO                       \n"
        + "       AND TPOD.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                   \n"
        + "       AND RPOL.ID_LIVELLO = :ID_LIVELLO                                             \n"
        + "   )                                                                                 \n"
        + " SELECT                                                                              \n"
        + "   IC.TOTALE - CS.TOTALE - PREC.CONTRIBUTO                                           \n"
        + " FROM                                                                                \n"
        + "   IMPORTO_CONTRIBUTO IC,                                                            \n"
        + "   CALCOLATO_E_SANZIONABILE CS ,                                                     \n"
        + "   CONTRIBUTO_EROGABILE_PREC PREC                                                    \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                Boolean result = Boolean.valueOf(
                    BigDecimal.ZERO.compareTo(rs.getBigDecimal(1)) <= 0);
                return result;
              }
              else
              {
                return Boolean.FALSE;
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

  public boolean isImportoContributoSufficienteAcconto(
      long idProcedimentoOggetto, long idLivello)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "isImportoContributoSufficienteAcconto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                                \n"
        + "   IMPORTO_CONTRIBUTO AS                                                             \n"
        + "   (                                                                                 \n"
        + "     SELECT                                                                          \n"
        + "       NVL(SUM(DI.IMPORTO_CONTRIBUTO),0) AS TOTALE                                   \n"
        + "     FROM                                                                            \n"
        + "       IUF_T_INTERVENTO I,                                                           \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                \n"
        + "       IUF_T_PROCEDIMENTO P,                                                         \n"
        + "       IUF_R_LIV_BANDO_INTERVENTO LBI                                                \n"
        + "     WHERE                                                                           \n"
        + "       PO.ID_PROCEDIMENTO              = I.ID_PROCEDIMENTO                           \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "       AND I.ID_INTERVENTO             = DI.ID_INTERVENTO                            \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO               \n"
        + "       AND P.ID_PROCEDIMENTO           = PO.ID_PROCEDIMENTO                          \n"
        + "       AND LBI.ID_BANDO                = P.ID_BANDO                                  \n"
        + "       AND LBI.ID_LIVELLO              = :ID_LIVELLO                                 \n"
        + "       AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                                         \n"
        + "       AND DI.DATA_FINE               IS NULL                                        \n"
        + "   )                                                                                 \n"
        + "   ,                                                                                 \n"
        + "   CALCOLATO_E_SANZIONABILE AS                                                       \n"
        + "   (                                                                                 \n"
        + "     SELECT                                                                          \n"
        + "       NVL(SUM(POL.CONTRIBUTO_CALCOLATO +POL.CONTRIBUTO_SANZIONABILE),0) AS TOTALE   \n"
        + "     FROM                                                                            \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL POL                                            \n"
        + "     WHERE                                                                           \n"
        + "       POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                        \n"
        + "       AND POL.ID_LIVELLO = :ID_LIVELLO                                              \n"
        + "   )                                                                                 \n"
        + "   ,                                                                                 \n"
        + "   SANZIONI AS                                                                       \n"
        + "   (                                                                                 \n"
        + "     SELECT                                                                          \n"
        + "       - NVL(SUM(POS.IMPORTO),0) AS TOTALE                                           \n"
        + "     FROM                                                                            \n"
        + "       IUF_T_PROC_OGGETTO_SANZIONE POS                                               \n"
        + "     WHERE                                                                           \n"
        + "       POS.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                        \n"
        + "       AND POS.ID_LIVELLO = :ID_LIVELLO                                              \n"
        + "   )                                                                                 \n"
        + "   ,                                                                                 \n"
        + "   CONTRIBUTO_EROGABILE_PREC AS                                                      \n"
        + "   (                                                                                 \n"
        + "     SELECT                                                                          \n"
        + "       NVL(SUM(PL.CONTRIBUTO_EROGABILE+PL.CONTRIBUTO_NON_EROGABILE),0) AS CONTRIBUTO \n"
        + "     FROM                                                                            \n"
        + "       IUF_R_PROCEDIMENTO_LIVELLO PL,                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO                                                 \n"
        + "     WHERE                                                                           \n"
        + "       PL.ID_PROCEDIMENTO             = PO.ID_PROCEDIMENTO                           \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                     \n"
        + "       AND PL.ID_LIVELLO = :ID_LIVELLO                                               \n"
        + "   )                                                                                 \n"
        + " SELECT                                                                              \n"
        + "   IC.TOTALE - CS.TOTALE - S.TOTALE - PREC.CONTRIBUTO                                \n"
        + " FROM                                                                                \n"
        + "   IMPORTO_CONTRIBUTO IC,                                                            \n"
        + "   CALCOLATO_E_SANZIONABILE CS ,                                                     \n"
        + "   SANZIONI S,                                                                       \n"
        + "   CONTRIBUTO_EROGABILE_PREC PREC                                                    \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                Boolean result = Boolean.valueOf(
                    BigDecimal.ZERO.compareTo(rs.getBigDecimal(1)) <= 0);
                return result;
              }
              else
              {
                return Boolean.FALSE;
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

  public void updateContributoAbbattutoAccertamentoSpese(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateContributoAbbattutoAccertamentoSpese";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String UPDATE = " UPDATE                                                                \n"
        + "   IUF_T_ACCERTAMENTO_SPESE ACC                                        \n"
        + " SET                                                                   \n"
        + "   ACC.CONTRIBUTO_ABBATTUTO =                                          \n"
        + "   (                                                                   \n"
        + "     SELECT                                                            \n"
        + "       DECODE (POL.CONTRIBUTO_CALCOLATO, 0, 0,                         \n"
        + "               ROUND(SP.CONTRIBUTO_CALCOLATO *                         \n"
        + "               POL.CONTRIBUTO_EROGABILE /                              \n"
        + "               POL.CONTRIBUTO_CALCOLATO,2)) AS CONTRIBUTO_ABBATTUTO    \n"
        + "     FROM                                                              \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL POL,                             \n"
        + "       IUF_T_ACCERTAMENTO_SPESE SP,                                    \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                  \n"
        + "       IUF_T_PROCEDIMENTO P,                                           \n"
        + "       IUF_R_LIV_BANDO_INTERVENTO LBI,                                 \n"
        + "       IUF_T_INTERVENTO I,                                             \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI                                   \n"
        + "     WHERE                                                             \n"
        + "       SP.ID_PROCEDIMENTO_OGGETTO  = POL.ID_PROCEDIMENTO_OGGETTO       \n"
        + "       AND POL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO    \n"
        + "       AND PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO             \n"
        + "       AND P.ID_BANDO                  = LBI.ID_BANDO                  \n"
        + "       AND SP.ID_INTERVENTO            = I.ID_INTERVENTO               \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO \n"
        + "       AND DI.ID_INTERVENTO            = I.ID_INTERVENTO               \n"
        + "       AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                           \n"
        + "       AND DI.DATA_FINE               IS NULL                          \n"
        + "       AND POL.ID_LIVELLO              = LBI.ID_LIVELLO                \n"
        + "       AND SP.ID_ACCERTAMENTO_SPESE    = ACC.ID_ACCERTAMENTO_SPESE     \n"
        + "   )                                                                   \n"
        + " WHERE                                                                 \n"
        + "   ACC.ID_PROCEDIMENTO_OGGETTO  = :ID_PROCEDIMENTO_OGGETTO             \n"
        + "   AND ACC.CONTRIBUTO_CALCOLATO >= 0                                   \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
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

  public void updateContributoAbbattutoPerLivelloDaAccertamentoSpeseEArrotondamenti(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateContributoAbbattutoPerLivelloDaAccertamentoSpeseEArrotondamenti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String UPDATE = " UPDATE                                                                \n"
        + "   IUF_R_PROCEDIMENTO_OGG_LIVEL POL                                  \n"
        + " SET                                                                   \n"
        + "   POL.CONTRIBUTO_ABBATTUTO =                                          \n"
        + "   (                                                                   \n"
        + "     SELECT                                                            \n"
        + "        NVL(SUM(NVL(ASP.CONTRIBUTO_ABBATTUTO,0)),0)                    \n"
        + "     FROM                                                              \n"
        + "       IUF_T_ACCERTAMENTO_SPESE ASP,                                   \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                  \n"
        + "       IUF_T_PROCEDIMENTO P,                                           \n"
        + "       IUF_R_LIV_BANDO_INTERVENTO LBI,                                 \n"
        + "       IUF_T_INTERVENTO I,                                             \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI                                   \n"
        + "     WHERE                                                             \n"
        + "       ASP.ID_PROCEDIMENTO_OGGETTO     = POL.ID_PROCEDIMENTO_OGGETTO   \n"
        + "       AND POL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO    \n"
        + "       AND PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO             \n"
        + "       AND P.ID_BANDO                  = LBI.ID_BANDO                  \n"
        + "       AND ASP.ID_INTERVENTO           = I.ID_INTERVENTO               \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO \n"
        + "       AND DI.ID_INTERVENTO            = I.ID_INTERVENTO               \n"
        + "       AND DI.FLAG_TIPO_OPERAZIONE    <> 'D'                           \n"
        + "       AND DI.DATA_FINE               IS NULL                          \n"
        + "       AND POL.ID_LIVELLO              = LBI.ID_LIVELLO                \n"
        + "     GROUP BY                                                          \n"
        + "       POL.ID_LIVELLO                                                  \n"
        + "   )                                                                   \n"
        + " WHERE                                                                 \n"
        + "   POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO              \n";

    String UPDATE_ARROTONDAMENTI = " UPDATE                                                                \n"
        + "   IUF_R_PROCEDIMENTO_OGG_LIVEL POL                                  \n"
        + " SET                                                                   \n"
        + "   POL.ARROTONDAMENTI_CONTR_ABBATTUTO = POL.CONTRIBUTO_EROGABILE -     \n"
        + "   POL.CONTRIBUTO_ABBATTUTO                                            \n"
        + " WHERE                                                                 \n"
        + "   POL.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      namedParameterJdbcTemplate.update(UPDATE_ARROTONDAMENTI,
          mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
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

  public Map<String, BigDecimal[]> getCalcoloImportiPerRendicontazioneSpese(
      long idProcedimentoOggetto,
      Date dataRiferimento,
      List<Long> ids)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getCalcoloImportiPerRendicontazioneSpese]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (ids == null)
    {
      ids = new ArrayList<Long>();
      ids.add(Long.MIN_VALUE);
    }
    String QUERY = ("  WITH                                                                                           \n"
        + "    RENDICONTAZIONE_CORRENTE AS                                                                  \n"
        + "    (                                                                                            \n"
        + "      SELECT                                                                                     \n"
        + "        RS.ID_INTERVENTO,                                                                        \n"
        + "        RS.CONTRIBUTO_RICHIESTO                                                                  \n"
        + "      FROM                                                                                       \n"
        + "        IUF_T_RENDICONTAZIONE_SPESE RS                                                           \n"
        + "      WHERE                                                                                      \n"
        + "        RS.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                    \n"
        + "        [ID_INTERVENTO_LIST]                                                                     \n"
        + "    )                                                                                            \n"
        + "    ,                                                                                            \n"
        + "    EROGABILE_PRECEDENTE AS                                                                      \n"
        + "    (                                                                                            \n"
        + "      SELECT                                                                                     \n"
        + "        NVL(SUM(PL.CONTRIBUTO_EROGABILE+PL.CONTRIBUTO_NON_EROGABILE),0) AS CONTRIBUTO_EROGABILE, \n"
        + "        L.CODICE_LIVELLO                                                                         \n"
        + "      FROM                                                                                       \n"
        + "        IUF_R_PROCEDIMENTO_LIVELLO PL,                                                           \n"
        + "        IUF_T_PROCEDIMENTO_OGGETTO PO,                                                           \n"
        + "        IUF_D_LIVELLO L                                                                          \n"
        + "      WHERE                                                                                      \n"
        + "        PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                    \n"
        + "        AND PO.ID_PROCEDIMENTO     = PL.ID_PROCEDIMENTO                                          \n"
        + "        AND PL.ID_LIVELLO = L.ID_LIVELLO                                                         \n"
        + "      GROUP BY                                                                                   \n"
        + "        L.CODICE_LIVELLO                                                                         \n"
        + "    )                                                                                            \n"
        + "  SELECT                                                                                         \n"
        + "    SUM(DI.IMPORTO_CONTRIBUTO) AS IMPORTO_CONTRIBUTO,                                            \n"
        + "    NVL(SUM(RC.CONTRIBUTO_RICHIESTO),0) +                                                        \n"
        + "    (                                                                                            \n"
        + "      SELECT                                                                                     \n"
        + "        EP.CONTRIBUTO_EROGABILE                                                                  \n"
        + "      FROM                                                                                       \n"
        + "        EROGABILE_PRECEDENTE EP                                                                  \n"
        + "      WHERE L.CODICE_LIVELLO = EP.CODICE_LIVELLO                                                 \n"
        + "    ) AS CONTRIBUTO_RICHIESTO,                                                                   \n"
        + "    L.CODICE_LIVELLO                                                                             \n"
        + "  FROM                                                                                           \n"
        + "    IUF_T_PROCEDIMENTO_OGGETTO PO,                                                               \n"
        + "    IUF_T_INTERVENTO I,                                                                          \n"
        + "    IUF_R_LIV_BANDO_INTERVENTO LBI,                                                              \n"
        + "    IUF_T_PROCEDIMENTO P,                                                                        \n"
        + "    IUF_D_LIVELLO L,                                                                             \n"
        + "    IUF_T_DETTAGLIO_INTERVENTO DI                                                                \n"
        + "  LEFT JOIN RENDICONTAZIONE_CORRENTE RC                                                          \n"
        + "  ON                                                                                             \n"
        + "    RC.ID_INTERVENTO = DI.ID_INTERVENTO                                                          \n"
        + "  WHERE                                                                                          \n"
        + "    PO.ID_PROCEDIMENTO_OGGETTO   = :ID_PROCEDIMENTO_OGGETTO                                      \n"
        + "    AND I.ID_PROCEDIMENTO        = PO.ID_PROCEDIMENTO                                            \n"
        + "    AND I.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                                                    \n"
        + "    AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO                              \n"
        + "    AND LBI.ID_BANDO = P.ID_BANDO                                                                \n"
        + "    AND LBI.ID_LIVELLO = L.ID_LIVELLO                                                            \n"
        + "    AND I.ID_INTERVENTO          = DI.ID_INTERVENTO                                              \n"
        + "    AND DI.FLAG_TIPO_OPERAZIONE <> 'D'                                                           \n"
        + "    AND NVL(:DATA_RIFERIMENTO, SYSDATE) BETWEEN DI.DATA_INIZIO AND                               \n"
        + "        NVL(DI.DATA_FINE, TO_DATE('31/12/9999', 'DD/MM/YYYY'))                                   \n"
        + "  GROUP BY                                                                                       \n"
        + "    PO.ID_PROCEDIMENTO,                                                                          \n"
        + "    L.CODICE_LIVELLO                                                                             \n")
            .replace("[ID_INTERVENTO_LIST]",
                getNotInCondition("RS.ID_INTERVENTO", ids));
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("DATA_RIFERIMENTO", dataRiferimento,
          Types.TIMESTAMP);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<String, BigDecimal[]>>()
          {

            @Override
            public Map<String, BigDecimal[]> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<String, BigDecimal[]> map = new HashMap<>();
              while (rs.next())
              {
                map.put(rs.getString("CODICE_LIVELLO"), new BigDecimal[]
                { rs.getBigDecimal("IMPORTO_CONTRIBUTO"),
                    rs.getBigDecimal("CONTRIBUTO_RICHIESTO") });
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

  public BigDecimal getPotenzialeErogabileRendicontazione(
      long idProcedimentoOggetto, boolean isSaldo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getPotenzialeErogabileRendicontazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY_SALDO = " WITH                                                                                                     \n"
        + "   EROGABILE_E_NON_EROGABILE AS                                                                           \n"
        + "   (                                                                                                      \n"
        + "     SELECT                                                                                               \n"
        + "       RPOL.ID_LIVELLO,                                                                                   \n"
        + "       SUM(RPOL.CONTRIBUTO_EROGABILE + RPOL.CONTRIBUTO_NON_EROGABILE) AS TOTALE_CONTRIBUTO                \n"
        + "     FROM                                                                                                 \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL RPOL,                                                               \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO TPOD,                                                                   \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO TPOI,                                                                   \n"
        + "       IUF_D_ESITO DE                                                                                     \n"
        + "     WHERE                                                                                                \n"
        + "       TPOI.ID_PROCEDIMENTO_OGGETTO = RPOL.ID_PROCEDIMENTO_OGGETTO                                        \n"
        + "       AND TPOI.ID_ESITO            = DE.ID_ESITO                                                         \n"
        + "       AND DE.CODICE = 'APP-P'                                                                            \n"
        + "       AND TPOI.ID_PROCEDIMENTO_OGGETTO < TPOD.ID_PROCEDIMENTO_OGGETTO                                    \n"
        + "       AND TPOI.ID_PROCEDIMENTO         = TPOD.ID_PROCEDIMENTO                                            \n"
        + "       AND TPOD.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                        \n"
        + "     GROUP BY RPOL.ID_LIVELLO                                                                             \n"
        + "   )                                                                                                      \n"
        + "   ,                                                                                                      \n"
        + "   RENDICONTAZIONE_CORRENTE AS                                                                            \n"
        + "   (                                                                                                      \n"
        + "     SELECT                                                                                               \n"
        + "       *                                                                                                  \n"
        + "     FROM                                                                                                 \n"
        + "       IUF_T_RENDICONTAZIONE_SPESE                                                                        \n"
        + "     WHERE                                                                                                \n"
        + "       ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                 \n"
        + "   )                                                                                                      \n"
        + "   ,                                                                                                      \n"
        + "   RENDICONTAZIONE AS                                                                                     \n"
        + "   (                                                                                                      \n"
        + "     SELECT                                                                                               \n"
        + "       LBI.ID_LIVELLO,                                                                                    \n"
        + "       NVL(SUM(DI.IMPORTO_CONTRIBUTO),0)   AS IMPORTO_CONTRIBUTO,                                         \n"
        + "       SUM(RS.CONTRIBUTO_RICHIESTO)  AS CONTRIBUTO_RICHIESTO                                              \n"
        + "     FROM                                                                                                 \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                     \n"
        + "       IUF_T_PROCEDIMENTO P,                                                                              \n"
        + "       IUF_T_INTERVENTO I,                                                                                \n"
        + "       IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                                              \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                                     \n"
        + "       IUF_D_TIPO_CLASSIFICAZIONE TC,                                                                     \n"
        + "       IUF_R_AGGREGAZIONE_INTERVENT AI,                                                                  \n"
        + "       IUF_D_TIPO_AGGREGAZIONE TA,                                                                        \n"
        + "       IUF_D_CATEGORIA_INTERVENTO CI,                                                                     \n"
        + "       RENDICONTAZIONE_CORRENTE RS,                                                                       \n"
        + "       IUF_R_LIV_BANDO_INTERVENTO LBI                                                                     \n"
        + "     WHERE                                                                                                \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                              \n"
        + "       AND PO.ID_PROCEDIMENTO     = P.ID_PROCEDIMENTO                                                     \n"
        + "       AND I.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                                                    \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE)                \n"
        + "       AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO                                           \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO                          \n"
        + "       AND DESCINT.ID_TIPO_CLASSIFICAZIONE   = TC.ID_TIPO_CLASSIFICAZIONE                                 \n"
        + "       AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)                              \n"
        + "       AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                                     \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO       = AI.ID_DESCRIZIONE_INTERVENTO                               \n"
        + "       AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE                                    \n"
        + "       AND DI.FLAG_TIPO_OPERAZIONE          <> :TIPO_OPERAZIONE_ELIMINAZIONE                              \n"
        + "       AND I.ID_INTERVENTO                   = RS.ID_INTERVENTO(+)                                        \n"
        + "       AND DESCINT.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO                              \n"
        + "       AND P.ID_BANDO                        = LBI.ID_BANDO                                               \n"
        + "     GROUP BY                                                                                             \n"
        + "       LBI.ID_LIVELLO                                                                                     \n"
        + "   )                                                                                                      \n"
        + "   ,                                                                                                      \n"
        + "   ANTICIPO AS                                                                                            \n"
        + "   (                                                                                                      \n"
        + "     SELECT                                                                                               \n"
        + "       AL.ID_LIVELLO,                                                                                     \n"
        + "       AL.IMPORTO_ANTICIPO                                                                                \n"
        + "     FROM                                                                                                 \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                                                \n"
        + "       IUF_T_ANTICIPO A,                                                                                  \n"
        + "       IUF_R_ANTICIPO_LIVELLO AL                                                                          \n"
        + "     WHERE                                                                                                \n"
        + "       PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                         \n"
        + "       AND A.ID_PROCEDIMENTO           = PO_ORIG.ID_PROCEDIMENTO                                          \n"
        + "       AND A.ID_STATO_OGGETTO          = 17                                                               \n"
        + "       AND A.ID_ANTICIPO               = AL.ID_ANTICIPO                                                   \n"
        + "   )                                                                                                      \n"
        + " SELECT                                                                                                   \n"
        + "   DECODE(SUM(R.CONTRIBUTO_RICHIESTO),NULL,0, NVL(SUM(                                                                                               \n"
        + "     CASE                                                                                                 \n"
        + "       WHEN R.IMPORTO_CONTRIBUTO<R.CONTRIBUTO_RICHIESTO + NVL(E.TOTALE_CONTRIBUTO,0)                      \n"
        + "       THEN R.IMPORTO_CONTRIBUTO                        - NVL(E.TOTALE_CONTRIBUTO,0) - NVL(A.IMPORTO_ANTICIPO,0) \n"
        + "       ELSE R.CONTRIBUTO_RICHIESTO                                                   - NVL(A.IMPORTO_ANTICIPO,0) \n"
        + "     END),0)) AS POTENZIALE_EROGABILE                                                                      \n"
        + " FROM                                                                                                     \n"
        + "   RENDICONTAZIONE R,                                                                                     \n"
        + "   EROGABILE_E_NON_EROGABILE E,                                                                           \n"
        + "   ANTICIPO A                                                                                             \n"
        + " WHERE                                                                                                    \n"
        + "   R.ID_LIVELLO     = E.ID_LIVELLO(+)                                                                     \n"
        + "   AND R.ID_LIVELLO = A.ID_LIVELLO(+)                                                                     \n";
    final String QUERY_ACCONTO = " WITH                                                                                      \n"
        + "   EROGABILE_E_NON_EROGABILE AS                                                            \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       RPOL.ID_LIVELLO,                                                                    \n"
        + "       SUM(RPOL.CONTRIBUTO_EROGABILE + RPOL.CONTRIBUTO_NON_EROGABILE) AS TOTALE_CONTRIBUTO \n"
        + "     FROM                                                                                  \n"
        + "       IUF_R_PROCEDIMENTO_OGG_LIVEL RPOL,                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO TPOD,                                                    \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO TPOI,                                                    \n"
        + "       IUF_D_ESITO DE                                                                      \n"
        + "     WHERE                                                                                 \n"
        + "       TPOI.ID_PROCEDIMENTO_OGGETTO = RPOL.ID_PROCEDIMENTO_OGGETTO                         \n"
        + "       AND TPOI.ID_ESITO            = DE.ID_ESITO                                          \n"
        + "       AND DE.CODICE = 'APP-P'                                                             \n"
        + "       AND TPOI.ID_PROCEDIMENTO_OGGETTO < TPOD.ID_PROCEDIMENTO_OGGETTO                     \n"
        + "       AND TPOI.ID_PROCEDIMENTO         = TPOD.ID_PROCEDIMENTO                             \n"
        + "       AND TPOD.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                         \n"
        + "     GROUP BY RPOL.ID_LIVELLO                                                              \n"
        + "   )                                                                                       \n"
        + "   ,                                                                                       \n"
        + "   ANTICIPO AS                                                                             \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       AL.ID_LIVELLO,                                                                      \n"
        + "       AL.IMPORTO_ANTICIPO                                                                 \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                                 \n"
        + "       IUF_T_ANTICIPO A,                                                                   \n"
        + "       IUF_R_ANTICIPO_LIVELLO AL                                                           \n"
        + "     WHERE                                                                                 \n"
        + "       PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                          \n"
        + "       AND A.ID_PROCEDIMENTO           = PO_ORIG.ID_PROCEDIMENTO                           \n"
        + "       AND A.ID_STATO_OGGETTO          = 17                                                \n"
        + "       AND A.ID_ANTICIPO               = AL.ID_ANTICIPO                                    \n"
        + "   )                                                                                       \n"
        + "   ,                                                                                       \n"
        + "   RENDICONTAZIONE_CORRENTE AS                                                             \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       *                                                                                   \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_RENDICONTAZIONE_SPESE                                                         \n"
        + "     WHERE                                                                                 \n"
        + "       ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                  \n"
        + "   )                                                                                       \n"
        + "   ,                                                                                       \n"
        + "   RENDICONTAZIONE AS                                                                      \n"
        + "   (                                                                                       \n"
        + "     SELECT                                                                                \n"
        + "       LBI.ID_LIVELLO,                                                                     \n"
        + "       NVL(SUM(DI.IMPORTO_CONTRIBUTO),0)   AS IMPORTO_CONTRIBUTO,                          \n"
        + "       SUM(RS.CONTRIBUTO_RICHIESTO) AS CONTRIBUTO_RICHIESTO                                \n"
        + "     FROM                                                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                      \n"
        + "       IUF_T_PROCEDIMENTO P,                                                               \n"
        + "       IUF_T_INTERVENTO I,                                                                 \n"
        + "       IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                               \n"
        + "       IUF_T_DETTAGLIO_INTERVENTO DI,                                                      \n"
        + "       IUF_D_TIPO_CLASSIFICAZIONE TC,                                                      \n"
        + "       IUF_R_AGGREGAZIONE_INTERVENT AI,                                                   \n"
        + "       IUF_D_TIPO_AGGREGAZIONE TA,                                                         \n"
        + "       IUF_D_CATEGORIA_INTERVENTO CI,                                                      \n"
        + "       RENDICONTAZIONE_CORRENTE RS,                                                        \n"
        + "       IUF_R_LIV_BANDO_INTERVENTO LBI                                                      \n"
        + "     WHERE                                                                                 \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                               \n"
        + "       AND PO.ID_PROCEDIMENTO     = P.ID_PROCEDIMENTO                                      \n"
        + "       AND I.ID_PROCEDIMENTO      = PO.ID_PROCEDIMENTO                                     \n"
        + "       AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE, SYSDATE) \n"
        + "       AND I.ID_INTERVENTO                   = DI.ID_INTERVENTO                            \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO           \n"
        + "       AND DESCINT.ID_TIPO_CLASSIFICAZIONE   = TC.ID_TIPO_CLASSIFICAZIONE                  \n"
        + "       AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)               \n"
        + "       AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                      \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO       = AI.ID_DESCRIZIONE_INTERVENTO                \n"
        + "       AND AI.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = TA.ID_TIPO_AGGREGAZIONE                     \n"
        + "       AND DI.FLAG_TIPO_OPERAZIONE          <> :TIPO_OPERAZIONE_ELIMINAZIONE               \n"
        + "       AND I.ID_INTERVENTO                   = RS.ID_INTERVENTO(+)                         \n"
        + "       AND DESCINT.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO               \n"
        + "       AND P.ID_BANDO                        = LBI.ID_BANDO                                \n"
        + "     GROUP BY                                                                              \n"
        + "       LBI.ID_LIVELLO                                                                      \n"
        + "   )                                                                                       \n"
        + " SELECT                                                                                    \n"
        + "   DECODE(SUM(R.CONTRIBUTO_RICHIESTO),NULL,0, NVL(SUM(                                                                                \n"
        + "     CASE                                                                                  \n"
        + "       WHEN R.IMPORTO_CONTRIBUTO<R.CONTRIBUTO_RICHIESTO + NVL(E.TOTALE_CONTRIBUTO,0) + NVL(A.IMPORTO_ANTICIPO,0) \n"
        + "       THEN R.IMPORTO_CONTRIBUTO                        - NVL(E.TOTALE_CONTRIBUTO,0) - NVL(A.IMPORTO_ANTICIPO,0) \n"
        + "       ELSE R.CONTRIBUTO_RICHIESTO                                                         \n"
        + "     END),0)) AS POTENZIALE_EROGABILE                                                      \n"
        + " FROM                                                                                      \n"
        + "   RENDICONTAZIONE R,                                                                      \n"
        + "   EROGABILE_E_NON_EROGABILE E,                                                            \n"
        + "   ANTICIPO A                                                                              \n"
        + " WHERE                                                                                     \n"
        + "   R.ID_LIVELLO = E.ID_LIVELLO(+)                                                          \n"
        + "   AND R.ID_LIVELLO = A.ID_LIVELLO(+)                                                      \n";
    final String QUERY = isSaldo ? QUERY_SALDO : QUERY_ACCONTO;
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
      return namedParameterJdbcTemplate.queryForObject(QUERY,
          mapParameterSource, BigDecimal.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("isSaldo", isSaldo),
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

  public BigDecimal getTotaliSanzioniPrecedentementeErogateRendicontazione(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getTotaliSanzioniPrecedentementeErogateRendicontazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                               \n"
        + "   NVL(SUM(POS.IMPORTO),0) AS IMPORTO_SANZIONI                        \n"
        + " FROM                                                                 \n"
        + "   IUF_T_PROC_OGGETTO_SANZIONE POS,                                   \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO ISTR,                                   \n"
        + "   IUF_D_ESITO E                                                      \n"
        + " WHERE                                                                \n"
        + "   PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO         \n"
        + "   AND ISTR.ID_PROCEDIMENTO        = PO_ORIG.ID_PROCEDIMENTO          \n"
        + "   AND POS.ID_PROCEDIMENTO_OGGETTO = ISTR.ID_PROCEDIMENTO_OGGETTO     \n"
        + "   AND ISTR.ID_ESITO               = E.ID_ESITO                       \n"
        + "   AND E.CODICE = 'APP-P'                                             \n"
        + "   AND ISTR.ID_PROCEDIMENTO_OGGETTO < PO_ORIG.ID_PROCEDIMENTO_OGGETTO \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
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
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
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

  public List<Long> getIdLivelliProcOggLivello(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getIdLivelliProcOggLivello]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT ID_LIVELLO FROM IUF_R_PROCEDIMENTO_OGG_LIVEL WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.queryForList(QUERY, mapParameterSource,
          Long.class);
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

  public void updateContributoErogabileNegativo(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateContributoErogabileNegativo";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String UPDATE = " UPDATE                                                                        \n"
        + "   IUF_R_PROCEDIMENTO_OGG_LIVEL                                              \n"
        + " SET                                                                           \n"
        + "   CONTRIBUTO_EROGABILE     = 0                                                \n"
        + " WHERE                                                                         \n"
        + "   CONTRIBUTO_EROGABILE < 0                                                    \n"
        + "   AND ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                      \n";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    try
    {
      mapSqlParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapSqlParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
          {}, UPDATE, mapSqlParameterSource);
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
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

  public List<RigaProspetto> getElencoProspetto(long IdProcedimento,
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getElencoProspetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = "  WITH ISTANZE AS (                                                                                                                                                                \n"
        + "  SELECT                                                                                                                                                                           \n"
        + "   A.ID_PROCEDIMENTO,                                                                                                                                                              \n"
        + "   B.ID_PROCEDIMENTO_OGGETTO,                                                                                                                                                      \n"
        + "   D.CODICE,                                                                                                                                                                       \n"
        + "   B.CODICE_RAGGRUPPAMENTO,                                                                                                                                                        \n"
        + "   D.DESCRIZIONE,                                                                                                                                                                  \n"
        + "   (SELECT C2.DATA_INIZIO FROM IUF_T_ITER_PROCEDIMENTO_OGGE C2 WHERE C2.ID_STATO_OGGETTO = 10 AND C2.ID_PROCEDIMENTO_OGGETTO = B.ID_PROCEDIMENTO_OGGETTO) AS DATA_PRESENTAZIONE, \n"
        + "   C.ID_LEGAME_GRUPPO_OGGETTO,                                                                                                                                                     \n"
        + "   C.ID_GRUPPO_OGGETTO                                                                                                                                                             \n"
        + "  FROM                                                                                                                                                                             \n"
        + "   IUF_T_PROCEDIMENTO A,                                                                                                                                                           \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO B,                                                                                                                                                   \n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO C,                                                                                                                                                  \n"
        + "   IUF_D_OGGETTO D                                                                                                                                                                 \n"
        + "  WHERE                                                                                                                                                                            \n"
        + "   A.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                                                                                                                            \n"
        + "   AND A.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO                                                                                                                                       \n"
        + "   AND B.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO                                                                                                                     \n"
        + "   AND C.ID_OGGETTO = D.ID_OGGETTO                                                                                                                                                 \n"
        + "   AND D.FLAG_ISTANZA = 'S'                                                                                                                                                        \n"
        + "   AND D.CODICE IN ('DACC', 'DSAL', 'DANT')                                                                                                                                        \n"
        + "   AND B.ID_PROCEDIMENTO_OGGETTO NOT IN (                                                                                                                                              \n"
        + "           SELECT                                                                                                                                                                  \n"
        + "            B3.ID_PROCEDIMENTO_OGGETTO                                                                                                                                             \n"
        + "          FROM                                                                                                                                                                     \n"
        + "           IUF_T_PROCEDIMENTO A3,                                                                                                                                                  \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO B3,                                                                                                                                          \n"
        + "           IUF_R_LEGAME_GRUPPO_OGGETTO C3,                                                                                                                                         \n"
        + "           IUF_D_OGGETTO D3                                                                                                                                                        \n"
        + "          WHERE                                                                                                                                                                    \n"
        + "           A3.ID_PROCEDIMENTO = A.ID_PROCEDIMENTO                                                                                                                                  \n"
        + "           AND A3.ID_PROCEDIMENTO = B3.ID_PROCEDIMENTO                                                                                                                             \n"
        + "           AND B3.ID_LEGAME_GRUPPO_OGGETTO = C3.ID_LEGAME_GRUPPO_OGGETTO                                                                                                           \n"
        + "           AND C3.ID_OGGETTO = D3.ID_OGGETTO                                                                                                                                       \n"
        + "           AND D3.FLAG_ISTANZA = 'S'                                                                                                                                               \n"
        + "           AND D3.CODICE IN ('DACC', 'DSAL', 'DANT')                                                                                                                               \n"
        + "           AND C3.ID_GRUPPO_OGGETTO = (                                                                                                                                            \n"
        + "               SELECT                                                                                                                                                              \n"
        + "                LGOISTR.ID_GRUPPO_OGGETTO                                                                                                                                          \n"
        + "               FROM                                                                                                                                                                \n"
        + "                 IUF_T_PROCEDIMENTO_OGGETTO POISTR,                                                                                                                                \n"
        + "                 IUF_R_LEGAME_GRUPPO_OGGETTO LGOISTR                                                                                                                               \n"
        + "               WHERE                                                                                                                                                               \n"
        + "                 POISTR.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO_ISTR                                                                                                    \n"
        + "                 AND POISTR.ID_LEGAME_GRUPPO_OGGETTO = LGOISTR.ID_LEGAME_GRUPPO_OGGETTO                                                                                            \n"
        + "           )                                                                                                                                                                       \n"
        + "         AND B3.CODICE_RAGGRUPPAMENTO = (                                                                                                                                          \n"
        + "                            SELECT                                                                                                                                                 \n"
        + "                             POISTR.CODICE_RAGGRUPPAMENTO                                                                                                                          \n"
        + "                            FROM                                                                                                                                                   \n"
        + "                              IUF_T_PROCEDIMENTO_OGGETTO POISTR                                                                                                                    \n"
        + "                            WHERE                                                                                                                                                  \n"
        + "                              POISTR.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO_ISTR                                                                                       \n"
        + "                        )                                                                                                                                                          \n"
        + "   )                                                                                                                                                                               \n"
        + "  ORDER BY C.ORDINE )                                                                                                                                                              \n"
        + "                                                                                                                                                                                   \n"
        + "                                                                                                                                                                                   \n"
        + "  SELECT                                                                                                                                                                           \n"
        + "   ISTANZE.*,                                                                                                                                                                      \n"
        + "   NVL(                                                                                                                                                                            \n"
        + "       (                                                                                                                                                                           \n"
        + "       SELECT                                                                                                                                                                      \n"
        + "              A.IMPORTO_ANTICIPO                                                                                                                                                   \n"
        + "           FROM                                                                                                                                                                    \n"
        + "              IUF_T_ANTICIPO A,                                                                                                                                                    \n"
        + "              IUF_R_ANTICIPO_PROC_OGG B                                                                                                                                            \n"
        + "           WHERE                                                                                                                                                                   \n"
        + "              B.ID_ANTICIPO = A.ID_ANTICIPO                                                                                                                                        \n"
        + "              AND A.ID_STATO_OGGETTO=17                                                                                                                                            \n"
        + "              AND B.ID_PROCEDIMENTO_OGGETTO = ISTANZE.ID_PROCEDIMENTO_OGGETTO                                                                                                      \n"
        + "       )                                                                                                                                                                           \n"
        + "   ,                                                                                                                                                                               \n"
        + "     (                                                                                                                                                                             \n"
        + "      SELECT                                                                                                                                                                       \n"
        + "           NVL( SUM(Z2.CONTRIBUTO_RICHIESTO),0)                                                                                                                                    \n"
        + "         FROM                                                                                                                                                                      \n"
        + "          IUF_T_RENDICONTAZIONE_SPESE Z2                                                                                                                                           \n"
        + "         WHERE                                                                                                                                                                     \n"
        + "              ISTANZE.ID_PROCEDIMENTO_OGGETTO = Z2.ID_PROCEDIMENTO_OGGETTO                                                                                                         \n"
        + "     )                                                                                                                                                                             \n"
        + "    )                                                                                                                                                                              \n"
        + "    AS CONTRIB_RICHIESTO,                                                                                                                                                          \n"
        + "    (                                                                                                                                                                              \n"
        + "      SELECT                                                                                                                                                                       \n"
        + "       NVL(SUM(Z2.IMPORTO),0)                                                                                                                                                      \n"
        + "      FROM                                                                                                                                                                         \n"
        + "       IUF_R_LEGAME_GRUPPO_OGGETTO C2,                                                                                                                                             \n"
        + "       IUF_D_OGGETTO D2,                                                                                                                                                           \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO B2,                                                                                                                                              \n"
        + "       IUF_T_PROC_OGGETTO_SANZIONE Z2                                                                                                                                              \n"
        + "      WHERE                                                                                                                                                                        \n"
        + "       C2.ID_GRUPPO_OGGETTO = ISTANZE.ID_GRUPPO_OGGETTO                                                                                                                            \n"
        + "       AND C2.ID_OGGETTO = D2.ID_OGGETTO                                                                                                                                           \n"
        + "       AND B2.ID_LEGAME_GRUPPO_OGGETTO = C2.ID_LEGAME_GRUPPO_OGGETTO                                                                                                               \n"
        + "       AND D2.CODICE IN ('ISACC', 'ISANT', 'ISSAL')                                                                                                                                \n"
        + "       AND B2.ID_PROCEDIMENTO_OGGETTO = Z2.ID_PROCEDIMENTO_OGGETTO                                                                                                                 \n"
        + "       AND B2.CODICE_RAGGRUPPAMENTO = ISTANZE.CODICE_RAGGRUPPAMENTO                                                                                                                \n"
        + "       AND B2.ID_PROCEDIMENTO = ISTANZE.ID_PROCEDIMENTO                                                                                                                            \n"
        + "   )AS IMPORTO_SANZIONI,                                                                                                                                                           \n"
        + "   (                                                                                                                                                                               \n"
        + "      SELECT                                                                                                                                                                       \n"
        + "       NVL(SUM(Z2.IMPORTO_LIQUIDATO),0)                                                                                                                                            \n"
        + "      FROM                                                                                                                                                                         \n"
        + "       IUF_R_LEGAME_GRUPPO_OGGETTO C2,                                                                                                                                             \n"
        + "       IUF_D_OGGETTO D2,                                                                                                                                                           \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO B2,                                                                                                                                              \n"
        + "       IUF_T_IMPORTI_LIQUIDATI Z2                                                                                                                                                  \n"
        + "      WHERE                                                                                                                                                                        \n"
        + "       C2.ID_GRUPPO_OGGETTO = ISTANZE.ID_GRUPPO_OGGETTO                                                                                                                            \n"
        + "       AND C2.ID_OGGETTO = D2.ID_OGGETTO                                                                                                                                           \n"
        + "       AND B2.ID_LEGAME_GRUPPO_OGGETTO = C2.ID_LEGAME_GRUPPO_OGGETTO                                                                                                               \n"
        + "       AND D2.CODICE IN ('ISACC', 'ISANT', 'ISSAL')                                                                                                                                \n"
        + "       AND B2.ID_PROCEDIMENTO_OGGETTO = Z2.ID_PROCEDIMENTO_OGGETTO                                                                                                                 \n"
        + "       AND B2.CODICE_RAGGRUPPAMENTO = ISTANZE.CODICE_RAGGRUPPAMENTO                                                                                                                \n"
        + "       AND B2.ID_PROCEDIMENTO = ISTANZE.ID_PROCEDIMENTO                                                                                                                            \n"
        + "   )AS IN_LIQUIDAZIONE                                                                                                                                                             \n"
        + "  FROM                                                                                                                                                                             \n"
        + "   ISTANZE                                                                                                                                                                         \n";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    try
    {
      mapSqlParameterSource.addValue("ID_PROCEDIMENTO", IdProcedimento,
          Types.NUMERIC);
      mapSqlParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO_ISTR",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapSqlParameterSource, RigaProspetto.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("IdProcedimento", IdProcedimento),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
          {}, QUERY, mapSqlParameterSource);
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
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

  public boolean isAziendaAttualmenteTipoEntePubblico(long idProcedimento)
      throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS
        + "::isAziendaAttualmenteTipoEntePubblico]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH                                                                                           \n"
        + "   PARAMETRO AS                                                                                 \n"
        + "   (                                                                                            \n"
        + "     SELECT                                                                                     \n"
        + "       VALORE                                                                                   \n"
        + "     FROM                                                                                       \n"
        + "       IUF_D_PARAMETRO                                                                          \n"
        + "     WHERE                                                                                      \n"
        + "       CODICE='ID_TP_ENTE_PUBBLICO'                                                             \n"
        + "   )                                                                                            \n"
        + "   ,                                                                                            \n"
        + "   ENTI_PUBBLICI AS                                                                             \n"
        + "   (                                                                                            \n"
        + "     SELECT                                                                                     \n"
        + "       TO_NUMBER(TRIM(REGEXP_SUBSTR(PARAMETRO.VALORE,'[^,]+', 1, LEVEL))) ID_TIPOLOGIA_AZIENDA, \n"
        + "       LEVEL                                                                                    \n"
        + "     FROM                                                                                       \n"
        + "       PARAMETRO                                                                                \n"
        + "       CONNECT BY REGEXP_SUBSTR(PARAMETRO.VALORE, '[^,]+', 1, LEVEL) IS NOT NULL                \n"
        + "   )                                                                                            \n"
        + " SELECT                                                                                         \n"
        + "   COUNT(*)                                                                                      \n"
        + " FROM                                                                                           \n"
        + "   IUF_T_PROCEDIMENTO_AZIENDA PA,                                                               \n"
        + "   SMRGAA_V_DATI_ANAGRAFICI DA                                                                  \n"
        + " WHERE                                                                                          \n"
        + "   PA.ID_PROCEDIMENTO         = :ID_PROCEDIMENTO                                                \n"
        + "   AND PA.DATA_FINE          IS NULL                                                            \n"
        + "   AND PA.EXT_ID_AZIENDA      = DA.ID_AZIENDA                                                   \n"
        + "   AND DA.DATA_FINE_VALIDITA IS NULL                                                            \n"
        + "   AND DA.ID_TIPOLOGIA_AZIENDA IN (SELECT EP.ID_TIPOLOGIA_AZIENDA FROM ENTI_PUBBLICI EP)        \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.queryForLong(QUERY,
          mapParameterSource) > 0;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimento", idProcedimento),
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

  /*
   * aggiunta metodi per unsplit riduzioni/sanzioni quando si confermano
   * modifiche all'accertamento spese
   */
  public String getFlagControllo(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getFlagControllo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                       		 \n"
        + "   FLAG_CONTROLLO											 \n"
        + " FROM                                                         \n"
        + "   IUF_T_CONTROLLO_IN_LOCO_INVE                             \n"
        + " WHERE                                                        \n"
        + "    ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO     \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForString(QUERY, mapParameterSource);
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

  public void deleteRiduzioneUnsplit(String flag, long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRiduzioneUnsplit]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String DELETE = " DELETE FROM IUF_T_PROC_OGGETTO_SANZIONE   POS                                 \n"
        + "        WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO		 	  	\n";

    if (flag != null && flag.compareTo("S") == 0)
      DELETE += "	AND  ID_SANZIONE_INVESTIMENTO = (SELECT  ID_SANZIONE_INVESTIMENTO FROM  IUF_D_SANZIONE_INVESTIMENTO WHERE CODICE_IDENTIFICATIVO = 'S01' )     \n";
    else
      DELETE += "	AND  ID_SANZIONE_INVESTIMENTO = (SELECT  ID_SANZIONE_INVESTIMENTO FROM  IUF_D_SANZIONE_INVESTIMENTO WHERE CODICE_IDENTIFICATIVO = 'S02' )     \n";

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
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
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

  public List<RiduzioniSanzioniDTO> getElencoRiduzioniSanzioni(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoRiduzioniSanzioni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT													\n"
        + " POS.ID_PROC_OGGETTO_SANZIONE as ID_PROC_OGG_SANZIONE,				\n"
        + "	TS.DESCRIZIONE AS TIPOLOGIA, 										\n"
        + "	L.CODICE AS OPERAZIONE, 											\n"
        + "	SI.DESCRIZIONE AS DESCRIZIONE, 										\n"
        + "	POS.NOTE AS NOTE, 													\n"
        + "	POS.IMPORTO AS IMPORTO_CURRENT,										\n"
        + "	(  SELECT 															\n"
        + "			SUM (IMPORTO) 												\n"
        + "		FROM 															\n"
        + "			IUF_T_PROC_OGGETTO_SANZIONE POS2 ,							\n"
        + "			 IUF_D_SANZIONE_INVESTIMENTO SI2						    \n"
        + "		WHERE 															\n"
        + "			POS2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO		\n"
        + "			AND POS2.ID_SANZIONE_INVESTIMENTO = SI2.ID_SANZIONE_INVESTIMENTO "
        + "			AND SI2.ID_TIPOLOGIA_SANZIONE = TS.ID_TIPOLOGIA_SANZIONE 	\n"
        + "			AND pos2.id_livello= pos.id_livello							\n"
        + "			AND SI2.ID_TIPOLOGIA_SANZIONE = 3							\n"
        + "			) AS IMPORTO_TOT,											\n"
        + "	(  SELECT 															\n"
        + "			COUNT (*) 													\n"
        + "		FROM 															\n"
        + "			IUF_T_PROC_OGGETTO_SANZIONE POS2, 							\n"
        + "			 IUF_D_SANZIONE_INVESTIMENTO SI2							\n"
        + "		WHERE 															\n"
        + "			POS2.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 	\n"
        + "			AND POS2.ID_SANZIONE_INVESTIMENTO = SI2.ID_SANZIONE_INVESTIMENTO "
        + "			AND SI2.ID_TIPOLOGIA_SANZIONE = 3							\n"
        + "			AND SI2.ID_TIPOLOGIA_SANZIONE = TS.ID_TIPOLOGIA_SANZIONE 	\n"
        + "			AND pos2.id_livello= pos.id_livello							\n"
        + "			) AS SPLITTED,												\n"
        + "	SI.ID_SANZIONE_INVESTIMENTO AS ID_DESCRIZIONE,						\n"
        + "	TS.ID_TIPOLOGIA_SANZIONE AS ID_TIPOLOGIA,							\n"
        + "	L.ID_LIVELLO AS ID_OPERAZIONE										\n"
        + "FROM     															\n"
        + "	 IUF_T_PROC_OGGETTO_SANZIONE POS, 									\n"
        + "	 IUF_D_SANZIONE_INVESTIMENTO SI,									\n"
        + "  IUF_D_LIVELLO L, 													\n"
        + "  IUF_D_TIPOLOGIA_SANZIONE TS    									\n"
        + "WHERE         														\n"
        + "  POS.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO 			\n"
        + "  AND L.ID_LIVELLO=POS.ID_LIVELLO									\n"
        + "  AND POS.ID_SANZIONE_INVESTIMENTO = SI.ID_SANZIONE_INVESTIMENTO 	\n"
        + "  AND TS.ID_TIPOLOGIA_SANZIONE = SI.ID_TIPOLOGIA_SANZIONE          	\n"
        + " ORDER BY L.CODICE, TS.ID_TIPOLOGIA_SANZIONE							\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<RiduzioniSanzioniDTO>>()
          {

            @Override
            public List<RiduzioniSanzioniDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<RiduzioniSanzioniDTO> list = new ArrayList<RiduzioniSanzioniDTO>();
              RiduzioniSanzioniDTO riduzione = null;
              long idTipologia = 0l;
              long idTipologiaOld = -1l;
              long idOperazione = 0l;
              long idOperazioneOld = -1l;
              while (rs.next())
              {
                idTipologia = rs.getLong("ID_TIPOLOGIA");
                idOperazione = rs.getLong("ID_OPERAZIONE");

                if (idOperazione != idOperazioneOld
                    || idTipologiaOld != idTipologia)
                {
                  riduzione = new RiduzioniSanzioniDTO();

                  riduzione = new RiduzioniSanzioniDTO();
                  riduzione
                      .setIdProcOggSanzione(rs.getLong("ID_PROC_OGG_SANZIONE"));
                  riduzione.setDescrizione(rs.getString("DESCRIZIONE"));
                  riduzione.setImporto(rs.getBigDecimal("IMPORTO_TOT"));
                  riduzione.setImportoFirstRecord(
                      rs.getBigDecimal("IMPORTO_CURRENT"));
                  riduzione.setNote(rs.getString("NOTE"));
                  riduzione.setTipologia(rs.getString("TIPOLOGIA"));
                  riduzione.setOperazione(rs.getString("OPERAZIONE"));
                  riduzione.setIdDescrizione(rs.getLong("ID_DESCRIZIONE"));
                  riduzione.setIdTipologia(rs.getLong("ID_TIPOLOGIA"));
                  riduzione.setIdOperazione(rs.getLong("ID_OPERAZIONE"));
                  int splitted = rs.getInt("SPLITTED");
                  if (splitted <= 1)
                    riduzione.setSplitted(false);
                  else
                    riduzione.setSplitted(true);

                  list.add(riduzione);
                  idTipologiaOld = idTipologia;
                  idOperazioneOld = idOperazione;
                }
                else
                  if (idOperazione == idOperazioneOld && idTipologia == 3)
                  {
                    riduzione.setImportoSecondRecordAfterSplit(
                        rs.getBigDecimal("IMPORTO_CURRENT"));
                    riduzione.setDescrizioneSecondRecordAfterSplit(
                        rs.getString("DESCRIZIONE"));
                    riduzione.setNoteB(rs.getString("NOTE"));
                    riduzione.setIdProcOggSanzioneSecondRecordAfterSplit(
                        rs.getLong("ID_PROC_OGG_SANZIONE"));
                    riduzione.setIdDescrizioneSecondRecordAfterSplit(
                        rs.getLong("ID_DESCRIZIONE"));
                  }
                  else
                    list.add(riduzione);

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

  public void inserisciSanzioneRiduzione(Long idTipologia, Long idOperazione,
      Long idDescrizione, String note,
      BigDecimal importo,
      Long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::inserisciSanzioneRiduzione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " INSERT                                     		\n"
        + " INTO                                        \n"
        + "   IUF_T_PROC_OGGETTO_SANZIONE               \n"
        + "   (                                         \n"
        + "     ID_PROC_OGGETTO_SANZIONE,               \n"
        + "     ID_SANZIONE_INVESTIMENTO,               \n"
        + "     NOTE,               				    \n"
        + "     IMPORTO,                     	        \n"
        + "     ID_LIVELLO,								\n"
        + "		ID_PROCEDIMENTO_OGGETTO	                \n"
        + "   )                                         \n"
        + "   VALUES                                    \n"
        + "   (                                         \n"
        + "     SEQ_IUF_T_PROC_OGGETTO_SANZI.NEXTVAL, \n"
        + "     :ID_SANZIONE_INVESTIMENTO,              \n"
        + "     :NOTE,              				    \n"
        + "     :IMPORTO,                               \n"
        + "     :ID_LIVELLO,      					    \n"
        + "     :ID_PROCEDIMENTO_OGGETTO	            \n"
        + "   )                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPOLOGIA_SANZIONE", idTipologia,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idOperazione, Types.NUMERIC);
      mapParameterSource.addValue("ID_SANZIONE_INVESTIMENTO", idDescrizione,
          Types.NUMERIC);
      mapParameterSource.addValue("NOTE", note, Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);

      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimento", idProcedimentoOggetto),
              new LogParameter("importo", importo),
              new LogParameter("idTipologia", idTipologia),
              new LogParameter("idTipologia", idDescrizione),
              new LogParameter("note", note),
              new LogParameter("idOperazione", idOperazione)
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
  /*
   * fine aggiunta metodi per unsplit riduzioni/sanzioni quando si confermano
   * modifiche all'accertamento spese
   */

  public void updateSuperficieEffettivaLocalizzazioneIntervento(
      long idDettIntervProcOgg,
      List<UpdateSuperficieLocalizzazioneDTO> list)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateSuperficieEffettivaLocalizzazioneIntervento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                                         \n"
        + "   IUF_W_LOCAL_INTERV_PROC_OGG                                  \n"
        + " SET                                                            \n"
        + "   SUPERFICIE_EFFETTIVA = :SUPERFICIE                           \n"
        + " WHERE                                                          \n"
        + "   ID_DETT_INTERV_PROC_OGG          = :ID_DETT_INTERV_PROC_OGG  \n"
        + "   AND EXT_ID_CONDUZIONE_DICHIARATA = :ID_CONDUZIONE_DICHIARATA \n"
        + "   AND EXT_ID_UTILIZZO_DICHIARATO   = :ID_UTILIZZO_DICHIARATO   \n";
    MapSqlParameterSource[] mapParameterSourceList = new MapSqlParameterSource[list
        .size()];
    int i = 0;
    for (UpdateSuperficieLocalizzazioneDTO superficie : list)
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_CONDUZIONE_DICHIARATA",
          superficie.getIdConduzioneDichiarata());
      mapParameterSource.addValue("ID_UTILIZZO_DICHIARATO",
          superficie.getIdUtilizzoDichiarato());
      mapParameterSource.addValue("SUPERFICIE", superficie.getSuperficie());
      mapParameterSourceList[i++] = mapParameterSource;
    }
    try
    {
      namedParameterJdbcTemplate.batchUpdate(QUERY, mapParameterSourceList);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntervento", idDettIntervProcOgg),
              new LogParameter("list", list)
          },
          new LogVariable[]
          {
          }, QUERY, mapParameterSourceList);
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

  public void updateSuperficieIstruttoriaLocalizzazioneIntervento(
      long idDettIntervProcOgg,
      List<UpdateSuperficieLocalizzazioneDTO> list)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateSuperficieIstruttoriaLocalizzazioneIntervento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                                         \n"
        + "   IUF_W_LOCAL_INTERV_PROC_OGG                                  \n"
        + " SET                                                            \n"
        + "   SUPERFICIE_ISTRUTTORIA = :SUPERFICIE                         \n"
        + " WHERE                                                          \n"
        + "   ID_DETT_INTERV_PROC_OGG          = :ID_DETT_INTERV_PROC_OGG  \n"
        + "   AND EXT_ID_CONDUZIONE_DICHIARATA = :ID_CONDUZIONE_DICHIARATA \n"
        + "   AND EXT_ID_UTILIZZO_DICHIARATO   = :ID_UTILIZZO_DICHIARATO   \n";
    MapSqlParameterSource[] mapParameterSourceList = new MapSqlParameterSource[list
        .size()];
    int i = 0;
    for (UpdateSuperficieLocalizzazioneDTO superficie : list)
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_CONDUZIONE_DICHIARATA",
          superficie.getIdConduzioneDichiarata());
      mapParameterSource.addValue("ID_UTILIZZO_DICHIARATO",
          superficie.getIdUtilizzoDichiarato());
      mapParameterSource.addValue("SUPERFICIE", superficie.getSuperficie());
      mapParameterSourceList[i++] = mapParameterSource;
    }
    try
    {
      namedParameterJdbcTemplate.batchUpdate(QUERY, mapParameterSourceList);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntervento", idDettIntervProcOgg),
              new LogParameter("list", list)
          },
          new LogVariable[]
          {
          }, QUERY, mapParameterSourceList);
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

  public List<UpdateSuperficieLocalizzazioneDTO> getRipartizioneSuperficiGisSuLocalizzazione(
      long idDettIntervProcOgg,
      int annoCampagna) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRipartizioneSuperficiGisSuLocalizzazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                                                                               \n"
        + "   ELEGGIBILITA_FITTIZIA AS                                                                                                         \n"
        + "   (                                                                                                                                \n"
        + "     SELECT                                                                                                                         \n"
        + "       CASE SUBSTR(DI.CODICE_IDENTIFICATIVO,8,4)                                                                                    \n"
        + "         WHEN 'TIP1'                                                                                                                \n"
        + "         THEN 97                                                                                                                    \n"
        + "         WHEN 'TIP2'                                                                                                                \n"
        + "         THEN 98                                                                                                                    \n"
        + "         WHEN 'TIP3'                                                                                                                \n"
        + "         THEN 96                                                                                                                    \n"
        + "       END AS CODICE                                                                                                                \n"
        + "     FROM                                                                                                                           \n"
        + "       IUF_W_DETT_INTERV_PROC_OGG DIPO,                                                                                             \n"
        + "       IUF_D_DESCRIZIONE_INTERVENTO DI,                                                                                             \n"
        + "       IUF_T_INTERVENTO I                                                                                                           \n"
        + "     WHERE                                                                                                                          \n"
        + "       DIPO.ID_DETT_INTERV_PROC_OGG    = :ID_DETT_INTERV_PROC_OGG                                                                   \n"
        + "       AND DIPO.ID_INTERVENTO          = I.ID_INTERVENTO                                                                            \n"
        + "       AND I.ID_DESCRIZIONE_INTERVENTO = DI.ID_DESCRIZIONE_INTERVENTO                                                               \n"
        + "   )                                                                                                                                \n"
        + "   ,                                                                                                                                \n"
        + "   LOCALIZZAZIONE AS                                                                                                                \n"
        + "   (                                                                                                                                \n"
        + "     SELECT                                                                                                                         \n"
        + "       CU.ID_PARTICELLA,                                                                                                            \n"
        + "       CU.ID_UTILIZZO_DICHIARATO,                                                                                                   \n"
        + "       CU.ID_CONDUZIONE_DICHIARATA,                                                                                                 \n"
        + "       LIPO.SUPERFICIE_EFFETTIVA                                                                                                    \n"
        + "     FROM                                                                                                                           \n"
        + "       IUF_W_LOCAL_INTERV_PROC_OGG LIPO,                                                                                            \n"
        + "       SMRGAA_V_CONDUZIONE_UTILIZZO CU                                                                                              \n"
        + "     WHERE                                                                                                                          \n"
        + "       LIPO.ID_DETT_INTERV_PROC_OGG    = :ID_DETT_INTERV_PROC_OGG                                                                   \n"
        + "       AND CU.ID_UTILIZZO_DICHIARATO   = LIPO.EXT_ID_UTILIZZO_DICHIARATO                                                            \n"
        + "       AND CU.ID_CONDUZIONE_DICHIARATA = LIPO.EXT_ID_CONDUZIONE_DICHIARATA                                                          \n"
        + "   )                                                                                                                                \n"
        + "   ,                                                                                                                                \n"
        + "   SUPERFICIE_GIS AS                                                                                                                \n"
        + "   (                                                                                                                                \n"
        + "     SELECT                                                                                                                         \n"
        + "       L.ID_PARTICELLA,                                                                                                             \n"
        + "       NVL(PCK_SMRGAA_LIBRERIA.GETELEGGIBILITACAMPAGNA(L.ID_PARTICELLA, :ANNO_CAMPAGNA, EF.CODICE),0) AS SUPERFICIE_ELEGGIBILE_FIT, \n"
        + "       SUM(L.SUPERFICIE_EFFETTIVA)                                                                    AS SUM_SUPERFICIE_EFFETTIVA,  \n"
        + "       COUNT(*)                                                                                       AS NUM                        \n"
        + "     FROM                                                                                                                           \n"
        + "       LOCALIZZAZIONE L,                                                                                                            \n"
        + "       ELEGGIBILITA_FITTIZIA EF                                                                                                     \n"
        + "     GROUP BY                                                                                                                       \n"
        + "       L.ID_PARTICELLA,                                                                                                             \n"
        + "       EF.CODICE                                                                                                                    \n"
        + "   )                                                                                                                                \n"
        + " SELECT                                                                                                                             \n"
        + "   L.ID_UTILIZZO_DICHIARATO,                                                                                                        \n"
        + "   L.ID_CONDUZIONE_DICHIARATA,                                                                                                      \n"
        + "   DECODE(SG.SUM_SUPERFICIE_EFFETTIVA, 0, ROUND(SG.SUPERFICIE_ELEGGIBILE_FIT/SG.NUM,4), ROUND((L.SUPERFICIE_EFFETTIVA *             \n"
        + "   SG.SUPERFICIE_ELEGGIBILE_FIT)                                            /SG.SUM_SUPERFICIE_EFFETTIVA,4)) AS SUPERFICIE          \n"
        + " FROM                                                                                                                               \n"
        + "   LOCALIZZAZIONE L,                                                                                                                \n"
        + "   SUPERFICIE_GIS SG                                                                                                                \n"
        + " WHERE                                                                                                                              \n"
        + "   L.ID_PARTICELLA = SG.ID_PARTICELLA                                                                                               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ANNO_CAMPAGNA", annoCampagna, Types.NUMERIC);
    mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG", idDettIntervProcOgg,
        Types.NUMERIC);

    try
    {
      return queryForList(QUERY, mapParameterSource,
          UpdateSuperficieLocalizzazioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("annoCampagna", annoCampagna)
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

  public void updateSuperficiGisLocalizzazione(long idDettIntervProcOgg,
      List<UpdateSuperficieLocalizzazioneDTO> superficiGis)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateSuperficiGisLocalizzazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                                         \n"
        + "   IUF_W_LOCAL_INTERV_PROC_OGG                                  \n"
        + " SET                                                            \n"
        + "   SUPERFICIE_ACCERTATA_GIS = :SUPERFICIE                       \n"
        + " WHERE                                                          \n"
        + "   ID_DETT_INTERV_PROC_OGG          = :ID_DETT_INTERV_PROC_OGG  \n"
        + "   AND EXT_ID_CONDUZIONE_DICHIARATA = :ID_CONDUZIONE_DICHIARATA \n"
        + "   AND EXT_ID_UTILIZZO_DICHIARATO   = :ID_UTILIZZO_DICHIARATO   \n";
    MapSqlParameterSource[] mapParameterSourceList = new MapSqlParameterSource[superficiGis
        .size()];
    int i = 0;
    for (UpdateSuperficieLocalizzazioneDTO superficie : superficiGis)
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DETT_INTERV_PROC_OGG",
          idDettIntervProcOgg, Types.NUMERIC);
      mapParameterSource.addValue("ID_CONDUZIONE_DICHIARATA",
          superficie.getIdConduzioneDichiarata());
      mapParameterSource.addValue("ID_UTILIZZO_DICHIARATO",
          superficie.getIdUtilizzoDichiarato());
      mapParameterSource.addValue("SUPERFICIE", superficie.getSuperficie());
      mapParameterSourceList[i++] = mapParameterSource;
    }
    try
    {
      namedParameterJdbcTemplate.batchUpdate(QUERY, mapParameterSourceList);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettIntervProcOgg", idDettIntervProcOgg),
              new LogParameter("superficiGis", superficiGis)
          },
          new LogVariable[]
          {
          }, QUERY, mapParameterSourceList);
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

  public void updateRProcedimentoOggettoSanzione(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateSanzioniAccertamento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String INSERT = " UPDATE                                              \n"
        + "   IUF_R_PROCEDIMENTO_OGG_LIVEL P                            \n"
        + " SET                                                           \n"
        + "   P.SANZIONI =                                                \n"
        + "   (                                                           \n"
        + "     SELECT                                                    \n"
        + "       SUM(POL.IMPORTO)				                          \n"
        + "     FROM                                                      \n"
        + "       IUF_T_PROC_OGGETTO_SANZIONE POL 	                      \n"
        + "     WHERE                                                     \n"
        + "       POL.ID_PROCEDIMENTO_OGGETTO = P.ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND POL.ID_LIVELLO          = P.ID_LIVELLO              \n"
        + "   )                                                           \n"
        + " WHERE                                                         \n"
        + "   P.ID_PROCEDIMENTO_OGGETTO   = :ID_PROCEDIMENTO_OGGETTO  	  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
          },
          new LogVariable[]
          {
          }, INSERT, mapParameterSource);
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

  public Map<Long, InterventoRendicontazioneDocumentiSpesaDTO> getRendicontazioneDocumentiSpesaPerIntervento(
      long idProcedimentoOggetto,
      List<Long> idIntervento) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRendicontazioneDocumentiSpesaPerIntervento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                                                                      \n"
        + "   ACCERTAMENTO AS                                                                                                         \n"
        + "   (                                                                                                                       \n"
        + "     SELECT                                                                                                                \n"
        + "       DSIAS.ID_DOCUMENTO_SPESA_INTERVEN,                                                                                  \n"
        + "       NVL(SUM(DECODE(DSIAS.ID_PROCEDIMENTO_OGGETTO, :ID_PROCEDIMENTO_OGGETTO, 0, NVL(DSIAS.IMPORTO_DISPONIBILE,0))),0) AS \n"
        + "       IMPORTO_DISPONIBILE                                                                                                 \n"
        + "     FROM                                                                                                                  \n"
        + "       IUF_R_DOC_SPESA_INT_ACC_SPES DSIAS,                                                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                                      \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                                                                 \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                                                                                 \n"
        + "       IUF_D_ESITO E                                                                                                       \n"
        + "     WHERE                                                                                                                 \n"
        + "       PO_ORIG.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                          \n"
        + "       AND PO_ORIG.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                                                                    \n"
        + "       AND DSIAS.ID_PROCEDIMENTO_OGGETTO = po.ID_PROCEDIMENTO_OGGETTO                                                      \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO                                                        \n"
        + "       AND IPO.DATA_FINE IS NULL                                                                                           \n"
        + "       AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                                                          \n"
        + "       AND PO.ID_ESITO = E.ID_ESITO                                                                                        \n"
        + "       AND E.CODICE = 'APP-P'                                                                                              \n"
        + "     GROUP BY DSIAS.ID_DOCUMENTO_SPESA_INTERVEN                                                                            \n"
        + "   )                                                                                                                       \n"
        + "   ,                                                                                                                       \n"
        + "   RENDICONTAZIONE_DOCUMENTI_PREC AS                                                                                       \n"
        + "   (                                                                                                                       \n"
        + "     SELECT                                                                                                                \n"
        + "       DSIPO.ID_DOCUMENTO_SPESA_INTERVEN,                                                                                  \n"
        + "       DSIPO.IMPORTO_RENDICONTATO                                                                                          \n"
        + "     FROM                                                                                                                  \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO_ORIG,                                                                                 \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTANZA,                                                                                 \n"
        + "       IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO                                                                                  \n"
        + "     WHERE                                                                                                                 \n"
        + "       PO_ORIG.ID_PROCEDIMENTO_OGGETTO     = :ID_PROCEDIMENTO_OGGETTO                                                      \n"
        + "       AND PO_ORIG.ID_PROCEDIMENTO         = ISTANZA.ID_PROCEDIMENTO                                                       \n"
        + "       AND ISTANZA.ID_PROCEDIMENTO_OGGETTO = DSIPO.ID_PROCEDIMENTO_OGGETTO                                                 \n"
        + "       AND EXISTS                                                                                                          \n"
        + "       (                                                                                                                   \n"
        + "         SELECT                                                                                                            \n"
        + "           *                                                                                                               \n"
        + "         FROM                                                                                                              \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO ISTRUTTORIA,                                                                         \n"
        + "           IUF_T_ITER_PROCEDIMENTO_OGGE ITPO,                                                                            \n"
        + "           IUF_D_ESITO DE,                                                                                                 \n"
        + "           IUF_T_ACCERTAMENTO_SPESE ASP                                                                                    \n"
        + "         WHERE                                                                                                             \n"
        + "           ISTRUTTORIA.ID_PROCEDIMENTO             = ISTANZA.ID_PROCEDIMENTO                                               \n"
        + "           AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = ASP.ID_PROCEDIMENTO_OGGETTO                                           \n"
        + "           AND ISTRUTTORIA.CODICE_RAGGRUPPAMENTO   = ISTANZA.CODICE_RAGGRUPPAMENTO                                         \n"
        + "           AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = ITPO.ID_PROCEDIMENTO_OGGETTO                                          \n"
        + "           AND ITPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                                                     \n"
        + "           AND ISTRUTTORIA.ID_ESITO  = DE.ID_ESITO                                                                         \n"
        + "           AND DE.CODICE             = 'APP-P'                                                                             \n"
        + "           AND ISTRUTTORIA.DATA_FINE < NVL(PO_ORIG.DATA_FINE,SYSDATE)                                                      \n"
        + "       )                                                                                                                   \n"
        + "   )                                                                                                                       \n"
        + " SELECT                                                                                                                    \n"
        + "   DI.PROGRESSIVO,                                                                                                         \n"
        + "   DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                                                                 \n"
        + "   F.RAGIONE_SOCIALE,                                                                                                      \n"
        + "   DDS.DATA_DOCUMENTO_SPESA,                                                                                               \n"
        + "   DDS.NUMERO_DOCUMENTO_SPESA,                                                                                             \n"
        + "   TDS.DESCRIZIONE AS DESC_TIPO_DOCUMENTO_SPESA,                                                                           \n"
        + "   DDS.DATA_PAGAMENTO,                                                                                                     \n"
        + "   MP.DESCRIZIONE AS DESC_MODALITA_PAGAMENTO,                                                                              \n"
        + "   DECODE(P.FLAG_RENDICONTAZIONE_CON_IVA,'S',DDS.IMPORTO_SPESA+DDS.IMPORTO_IVA_SPESA,DDS.IMPORTO_SPESA) AS IMPORTO_SPESA,  \n"
        + "   DSI.IMPORTO,                                                                                                            \n"
        + "   (                                                                                                                       \n"
        + "       SELECT                                                                                                              \n"
        + "         NVL(SUM(NVL(RDP.IMPORTO_RENDICONTATO,0)),0)                                                                       \n"
        + "       FROM                                                                                                                \n"
        + "         RENDICONTAZIONE_DOCUMENTI_PREC RDP                                                                                \n"
        + "       WHERE                                                                                                               \n"
        + "         DSI.ID_DOCUMENTO_SPESA_INTERVEN = RDP.ID_DOCUMENTO_SPESA_INTERVEN                                                 \n"
        + "   ) AS IMPORTO_RENDICONTATO_PREC,                                                                                         \n"
        + "   (                                                                                                                       \n"
        + "     SELECT                                                                                                                \n"
        + "       A.IMPORTO_DISPONIBILE                                                                                               \n"
        + "     FROM                                                                                                                  \n"
        + "       ACCERTAMENTO A                                                                                                      \n"
        + "     WHERE                                                                                                                 \n"
        + "       A.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN                                                     \n"
        + "   )                                                                                                       AS IMPORTO_DISPONIBILE, \n"
        + "   SUM(CASE WHEN DSIPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO THEN NVL(DSIPO.IMPORTO_RENDICONTATO,0) ELSE 0 END)               \n"
        + "       AS IMPORTO_RENDICONTATO,                                                                                                              \n"
        + "   DSI.ID_DOCUMENTO_SPESA_INTERVEN,                                                                                                          \n"
        + "   I.ID_INTERVENTO,                                                                                                                          \n"
        + "   DS.ID_DOCUMENTO_SPESA,                                                                                                                    \n"
        + "   DI.PERCENTUALE_CONTRIBUTO,                                                                                                                \n"
        + "   (SELECT RS.FLAG_INTERVENTO_COMPLETATO FROM IUF_T_RENDICONTAZIONE_SPESE RS WHERE RS.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO AND RS.ID_INTERVENTO = I.ID_INTERVENTO) as FLAG_INTERVENTO_COMPLETATO, \n"
        + "   (SELECT RS.NOTE FROM IUF_T_RENDICONTAZIONE_SPESE RS WHERE RS.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO AND RS.ID_INTERVENTO = I.ID_INTERVENTO) as NOTE, \n"
        + "   (SELECT SUM(IMPORTO) FROM IUF_R_DOC_SPESA_INT_RICEV_PA DSIRP WHERE DSIRP.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA AND DSIRP.ID_INTERVENTO = I.ID_INTERVENTO) AS IMPORTO_RICEVUTE_PAGAMENTO, "
        + "   L.CODICE_LIVELLO,                                                                                                                                       \n"
        + "   (                                                                                                                  \n"
        + "     SELECT                                                                                                           \n"
        + "       FIRST_VALUE(DECODE(PROC_OGG.IDENTIFICATIVO, NULL, '', SUBSTR(PROC_OGG.IDENTIFICATIVO, 13) || ' - ')                        \n"
        + "       || DSF.NOME_FILE_LOGICO_DOCUMENTO_SPE) OVER(ORDER BY DSPO.ID_PROCEDIMENTO_OGGETTO DESC NULLS LAST, \n"
        + "       DSF.ID_DOCUMENTO_SPESA_FILE DESC)                                                                              \n"
        + "     FROM                                                                                                             \n"
        + "       IUF_T_DOCUMENTO_SPESA_FILE DSF                                                                                 \n"
        + "     LEFT JOIN IUF_R_DOC_SPESA_PROC_OGG DSPO                                                                          \n"
        + "     ON                                                                                                               \n"
        + "       DSF.ID_DOCUMENTO_SPESA_FILE      = DSPO.ID_DOCUMENTO_SPESA_FILE                                                \n"
        + "       AND DSPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                    \n"
        + "     LEFT JOIN IUF_T_PROCEDIMENTO_OGGETTO PROC_OGG                                                                    \n"
        + "     ON                                                                                                               \n"
        + "       PROC_OGG.ID_PROCEDIMENTO_OGGETTO = DSPO.ID_PROCEDIMENTO_OGGETTO                                                \n"
        + "     WHERE                                                                                                            \n"
        + "       DSF.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                                 \n"
        + "       AND ROWNUM = 1                                                                                                 \n"
        + "   ) AS NOME_FILE,                                                                                                    \n"
        + "   (                                                                             \n"
        + "     SELECT                                                                      \n"
        + "       COUNT(*)                                                                  \n"
        + "     FROM                                                                        \n"
        + "       IUF_R_DOC_SPESA_PROC_OGG SPO,                                             \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTANZA,                                       \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTRUTTORIA,                                   \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                                       \n"
        + "       IUF_T_ACCERTAMENTO_SPESE ASP,                                             \n"
        + "       IUF_D_ESITO E                                                             \n"
        + "     WHERE                                                                       \n"
        + "       SPO.ID_DOCUMENTO_SPESA                  = DS.ID_DOCUMENTO_SPESA           \n"
        + "       AND SPO.ID_PROCEDIMENTO_OGGETTO         = ISTANZA.ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND ISTANZA.ID_PROCEDIMENTO             = ISTRUTTORIA.ID_PROCEDIMENTO     \n"
        + "       AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = ASP.ID_PROCEDIMENTO_OGGETTO     \n"
        + "       AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO     \n"
        + "       AND IPO.DATA_FINE                      IS NULL                            \n"
        + "       AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                \n"
        + "       AND ISTANZA.CODICE_RAGGRUPPAMENTO = ISTRUTTORIA.CODICE_RAGGRUPPAMENTO     \n"
        + "       AND ISTRUTTORIA.ID_ESITO          = E.ID_ESITO                            \n"
        + "       AND E.CODICE                      = 'APP-N'                               \n"
        + "   ) AS WARNING_DOCUMENTO                                                        \n"
        + " FROM                                                                                                                                        \n"
        + "   IUF_T_INTERVENTO I,                                                                                                                       \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                                                                            \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                                                                                     \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                                                            \n"
        + "   IUF_T_PROCEDIMENTO P,                                                                                                                    \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI,                                                                                                                    \n"
        + "   IUF_D_LIVELLO L,                                                                                                                    \n"
        + "   IUF_T_DOCUMENTO_SPESA DS,                                                                                                                 \n"
        + "   IUF_T_DETT_DOCUMENTO_SPESA DDS                                                                                                            \n"
        + " LEFT JOIN IUF_T_FORNITORE F                                                                                                                 \n"
        + " ON                                                                                                                                          \n"
        + "   DDS.ID_FORNITORE = F.ID_FORNITORE                                                                                                         \n"
        + " LEFT JOIN IUF_D_TIPO_DOCUMENTO_SPESA TDS                                                                                                    \n"
        + " ON                                                                                                                                          \n"
        + "   DDS.ID_TIPO_DOCUMENTO_SPESA = TDS.ID_TIPO_DOCUMENTO_SPESA                                                                                 \n"
        + " LEFT JOIN IUF_D_MODALITA_PAGAMENTO MP                                                                                                       \n"
        + " ON                                                                                                                                          \n"
        + "   DDS.ID_MODALITA_PAGAMENTO = MP.ID_MODALITA_PAGAMENTO ,                                                                                    \n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV DSI                                                                                                        \n"
        + " LEFT JOIN IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO                                                                                                \n"
        + " ON                                                                                                                                          \n"
        + "   DSIPO.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN                                                                       \n"
        + "   AND DSIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                                              \n"
        + " WHERE                                                                                                                                       \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                                                     \n"
        + getInCondition("I.ID_INTERVENTO", idIntervento)
        + "   AND P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO "
        + "   AND I.ID_DESCRIZIONE_INTERVENTO = LBI.ID_DESCRIZIONE_INTERVENTO  "
        + "   AND LBI.ID_BANDO = P.ID_BANDO    "
        + "   AND LBI.ID_LIVELLO = L.ID_LIVELLO "

        + "   AND I.ID_INTERVENTO        = DI.ID_INTERVENTO                                                                                             \n"
        + "   AND NVL(PO.DATA_FINE, SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE,SYSDATE)                                                       \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO = DESCINT.ID_DESCRIZIONE_INTERVENTO                                                                       \n"
        + "   AND PO.ID_PROCEDIMENTO          = DS.ID_PROCEDIMENTO                                                                                      \n"
        + "   AND DS.ID_DOCUMENTO_SPESA       = DDS.ID_DOCUMENTO_SPESA                                                                                  \n"
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DDS.DATA_INIZIO AND NVL(DDS.DATA_FINE, SYSDATE)                                                     \n"
        + "   AND DSI.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                                                        \n"
        + "   AND I.ID_INTERVENTO        = DSI.ID_INTERVENTO                                                                                            \n"
        + " GROUP BY                                                                                                                                    \n"
        + "   DI.PROGRESSIVO,                                                                                                                           \n"
        + "   P.FLAG_RENDICONTAZIONE_CON_IVA,                                                                                                           \n"
        + "   DI.PERCENTUALE_CONTRIBUTO,                                                                                                                \n"
        + "   DESCINT.DESCRIZIONE,                                                                                                                      \n"
        + "   F.RAGIONE_SOCIALE,                                                                                                                        \n"
        + "   DDS.DATA_DOCUMENTO_SPESA,                                                                                                                 \n"
        + "   DDS.NUMERO_DOCUMENTO_SPESA,                                                                                                               \n"
        + "   TDS.DESCRIZIONE,                                                                                                                          \n"
        + "   DDS.DATA_PAGAMENTO,                                                                                                                       \n"
        + "   MP.DESCRIZIONE,                                                                                                                           \n"
        + "   DDS.IMPORTO_SPESA,                                                                                                                        \n"
        + "   DDS.IMPORTO_IVA_SPESA,                                                                                                                    \n"
        + "   DSI.IMPORTO,                                                                                                                              \n"
        + "   DDS.NOME_FILE_FISICO_DOCUMENTO_SPE,                                                                                                       \n"
        + "   DSI.ID_DOCUMENTO_SPESA_INTERVEN,                                                                                                          \n"
        + "   I.ID_INTERVENTO,                                                                                                                          \n"
        + "   DS.ID_DOCUMENTO_SPESA,                                                                                                                    \n"
        + "   L.CODICE_LIVELLO                                                                                                                          \n"
        + " ORDER BY                                                                                                                                    \n"
        + "   DI.PROGRESSIVO ASC,                                                                                                                       \n"
        + "   DS.ID_DOCUMENTO_SPESA ASC                                                                                                                 \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<Long, InterventoRendicontazioneDocumentiSpesaDTO>>()
          {
            @Override
            public Map<Long, InterventoRendicontazioneDocumentiSpesaDTO> extractData(
                ResultSet rs)
                throws SQLException, DataAccessException
            {
              int lastProgressivo = Integer.MIN_VALUE;
              Map<Long, InterventoRendicontazioneDocumentiSpesaDTO> list = new TreeMap<>();
              InterventoRendicontazioneDocumentiSpesaDTO intervento = null;
              while (rs.next())
              {
                int progressivo = rs.getInt("PROGRESSIVO");
                if (progressivo != lastProgressivo)
                {
                  lastProgressivo = progressivo;
                  intervento = new InterventoRendicontazioneDocumentiSpesaDTO();
                  intervento.setProgressivo(progressivo);
                  intervento.setIdIntervento(rs.getLong("ID_INTERVENTO"));
                  intervento.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                  intervento.setPercentualeContributo(
                      rs.getBigDecimal("PERCENTUALE_CONTRIBUTO"));
                  intervento.setRendicontazione(new ArrayList<>());
                  intervento.setFlagInterventoCompletato(
                      rs.getString("FLAG_INTERVENTO_COMPLETATO"));
                  intervento.setNote(rs.getString("NOTE"));
                  intervento.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                  list.put(intervento.getIdIntervento(), intervento);
                }
                RigaRendicontazioneDocumentiSpesaDTO riga = new RigaRendicontazioneDocumentiSpesaDTO();
                intervento.getRendicontazione().add(riga);
                riga.setDataDocumentoSpesa(rs.getDate("DATA_DOCUMENTO_SPESA"));
                riga.setDataPagamento(rs.getDate("DATA_PAGAMENTO"));
                riga.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                riga.setDescModalitaPagamento(
                    rs.getString("DESC_MODALITA_PAGAMENTO"));
                riga.setDescTipoDocumentoSpesa(
                    rs.getString("DESC_TIPO_DOCUMENTO_SPESA"));
                riga.setIdDocumentoSpesaInterven(
                    rs.getLong("ID_DOCUMENTO_SPESA_INTERVEN"));
                riga.setImporto(rs.getBigDecimal("IMPORTO"));
                riga.setImportoRendicontato(
                    rs.getBigDecimal("IMPORTO_RENDICONTATO"));
                riga.setImportoDisponibile(
                    rs.getBigDecimal("IMPORTO_DISPONIBILE"));
                riga.setImportoRendicontatoPrec(
                    rs.getBigDecimal("IMPORTO_RENDICONTATO_PREC"));
                riga.setImportoSpesa(rs.getBigDecimal("IMPORTO_SPESA"));
                riga.setNomeFile(rs.getString("NOME_FILE"));
                riga.setNumeroDocumentoSpesa(
                    rs.getString("NUMERO_DOCUMENTO_SPESA"));
                riga.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
                riga.setIdDocumentoSpesa(rs.getLong("ID_DOCUMENTO_SPESA"));
                riga.setImportoRicevutePagamento(
                    rs.getBigDecimal("IMPORTO_RICEVUTE_PAGAMENTO"));
                riga.setWarningDocumento(rs.getInt("WARNING_DOCUMENTO") > 0);
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

  public int updateOrInsertRendicontazioneSpeseDocumenti(
      long idProcedimentoOggetto,
      RigaRendicontazioneDocumentiSpesaDTO riga, long idUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateRendicontazioneSpeseDocumenti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                                           \n"
        + "   IUF_R_DOC_SPESA_INT_PROC_OGG                                   \n"
        + " SET                                                              \n"
        + "   IMPORTO_RENDICONTATO = :IMPORTO_RENDICONTATO,                  \n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO     \n"
        + " WHERE                                                            \n"
        + "   ID_PROCEDIMENTO_OGGETTO         = :ID_PROCEDIMENTO_OGGETTO     \n"
        + "   AND ID_DOCUMENTO_SPESA_INTERVEN = :ID_DOCUMENTO_SPESA_INTERVEN \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_DOCUMENTO_SPESA_INTERVEN",
        riga.getIdDocumentoSpesaInterven(), Types.NUMERIC);
    mapParameterSource.addValue("IMPORTO_RENDICONTATO",
        riga.getImportoRendicontato(), Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
        idUtenteAggiornamento, Types.NUMERIC);
    int numRowsUpdated = 0;
    try
    {
      numRowsUpdated = namedParameterJdbcTemplate.update(QUERY,
          mapParameterSource);
      if (numRowsUpdated == 0)
      {
        QUERY = " INSERT INTO                                 \n"
            + "   IUF_R_DOC_SPESA_INT_PROC_OGG              \n"
            + "   (                                         \n"
            + "     ID_PROCEDIMENTO_OGGETTO,                \n"
            + "     IMPORTO_RENDICONTATO,                   \n"
            + "     ID_DOCUMENTO_SPESA_INTERVEN,            \n"
            + "     ID_DOC_SPESA_INT_PROC_OGG,              \n"
            + "     EXT_ID_UTENTE_AGGIORNAMENTO,            \n"
            + "     DATA_ULTIMO_AGGIORNAMENTO               \n"
            + "   )                                         \n"
            + "   VALUES                                    \n"
            + "   (                                         \n"
            + "     :ID_PROCEDIMENTO_OGGETTO,               \n"
            + "     :IMPORTO_RENDICONTATO,                  \n"
            + "     :ID_DOCUMENTO_SPESA_INTERVEN,           \n"
            + "     SEQ_IUF_R_DOC_SPE_INT_PROC_O.NEXTVAL, \n"
            + "     :EXT_ID_UTENTE_AGGIORNAMENTO,           \n"
            + "     SYSDATE                                 \n"
            + "   )                                         \n";
        numRowsUpdated = namedParameterJdbcTemplate.update(QUERY,
            mapParameterSource);
      }
      return numRowsUpdated;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("riga", riga)
          },
          new LogVariable[]
          {
              new LogVariable("numRowsUpdated", numRowsUpdated),
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

  public int deleteRendicontazioneSpeseDocumenti(long idProcedimentoOggetto,
      long idDocumentoSpesaInterven)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "deleteRendicontazioneSpeseDocumenti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " DELETE                                                           \n"
        + "   IUF_R_DOC_SPESA_INT_PROC_OGG                                   \n"
        + " WHERE                                                            \n"
        + "   ID_PROCEDIMENTO_OGGETTO         = :ID_PROCEDIMENTO_OGGETTO     \n"
        + "   AND ID_DOCUMENTO_SPESA_INTERVEN = :ID_DOCUMENTO_SPESA_INTERVEN \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_DOCUMENTO_SPESA_INTERVEN",
        idDocumentoSpesaInterven, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idDocumentoSpesaInterven",
                  idDocumentoSpesaInterven)
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

  public boolean checkIntegrityRendicontazioneSpeseDocumenti(
      long idProcedimentoOggetto, long idDocumentoSpesaInterven)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "checkIntegrityRendicontazioneSpeseDocumenti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                              \n"
        + "   MAX_ISTANZA_GRUPPO AS                                                           \n"
        + "   (                                                                               \n"
        + "     SELECT                                                                        \n"
        + "       MAX(ISTANZA.ID_PROCEDIMENTO_OGGETTO) AS ID_PROCEDIMENTO_OGGETTO             \n"
        + "     FROM                                                                          \n"
        + "       IUF_R_DOCUMENTO_SPESA_INTERV DSI,                                         \n"
        + "       IUF_T_DOCUMENTO_SPESA DS,                                                   \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTANZA                                          \n"
        + "     WHERE                                                                         \n"
        + "       DSI.ID_DOCUMENTO_SPESA_INTERVEN = :ID_DOCUMENTO_SPESA_INTERVEN              \n"
        + "       AND DSI.ID_DOCUMENTO_SPESA      = DS.ID_DOCUMENTO_SPESA                     \n"
        + "       AND DS.ID_PROCEDIMENTO          = ISTANZA.ID_PROCEDIMENTO                   \n"
        + "       AND EXISTS                                                                  \n"
        + "       (                                                                           \n"
        + "         SELECT                                                                    \n"
        + "           *                                                                       \n"
        + "         FROM                                                                      \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO ISTRUTTORIA,                                 \n"
        + "           IUF_T_ITER_PROCEDIMENTO_OGGE ITPO,                                    \n"
        + "           IUF_D_ESITO DE,                                                         \n"
        + "           IUF_T_ACCERTAMENTO_SPESE ASP                                            \n"
        + "         WHERE                                                                     \n"
        + "           ISTRUTTORIA.ID_PROCEDIMENTO             = ISTANZA.ID_PROCEDIMENTO       \n"
        + "           AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = ASP.ID_PROCEDIMENTO_OGGETTO   \n"
        + "           AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO <> ISTANZA.ID_PROCEDIMENTO_OGGETTO \n"
        + "           AND ISTRUTTORIA.CODICE_RAGGRUPPAMENTO   = ISTANZA.CODICE_RAGGRUPPAMENTO \n"
        + "           AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = ITPO.ID_PROCEDIMENTO_OGGETTO  \n"
        + "           AND ITPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                             \n"
        + "           AND ITPO.DATA_FINE IS NULL                                              \n"
        + "           AND ISTRUTTORIA.ID_ESITO = DE.ID_ESITO                                  \n"
        + "           AND DE.CODICE            = 'APP-P'                                      \n"
        + "       )                                                                           \n"
        + "     GROUP BY                                                                      \n"
        + "       ISTANZA.CODICE_RAGGRUPPAMENTO                                               \n"
        + "   UNION ALL                                                                       \n"
        + "     SELECT :ID_PROCEDIMENTO_OGGETTO FROM DUAL                                     \n"
        + "   )                                                                               \n"
        + "   ,                                                                               \n"
        + "   MAX_ISTRUTTORIA_GRUPPO AS                                                       \n"
        + "   (                                                                               \n"
        + "     SELECT                                                                        \n"
        + "       MAX(ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO) AS ID_PROCEDIMENTO_OGGETTO         \n"
        + "     FROM                                                                          \n"
        + "       IUF_R_DOCUMENTO_SPESA_INTERV DSI,                                         \n"
        + "       IUF_T_DOCUMENTO_SPESA DS,                                                   \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTRUTTORIA,                                     \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE ITPO,                                        \n"
        + "       IUF_D_ESITO DE                                                              \n"
        + "     WHERE                                                                         \n"
        + "       DSI.ID_DOCUMENTO_SPESA_INTERVEN         = :ID_DOCUMENTO_SPESA_INTERVEN      \n"
        + "       AND DSI.ID_DOCUMENTO_SPESA              = DS.ID_DOCUMENTO_SPESA             \n"
        + "       AND ISTRUTTORIA.ID_PROCEDIMENTO         = DS.ID_PROCEDIMENTO                \n"
        + "       AND ISTRUTTORIA.CODICE_RAGGRUPPAMENTO   = ISTRUTTORIA.CODICE_RAGGRUPPAMENTO \n"
        + "       AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = ITPO.ID_PROCEDIMENTO_OGGETTO      \n"
        + "       AND ITPO.DATA_FINE IS NULL                                                  \n"
        + "       AND ITPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                 \n"
        + "       AND ISTRUTTORIA.ID_ESITO = DE.ID_ESITO                                      \n"
        + "       AND DE.CODICE            = 'APP-P'                                          \n"
        + "     GROUP BY                                                                      \n"
        + "       ISTRUTTORIA.CODICE_RAGGRUPPAMENTO                                           \n"
        + "   )                                                                               \n"
        + "   ,                                                                               \n"
        + "   RENDICONTAZIONE AS                                                              \n"
        + "   (                                                                               \n"
        + "     SELECT                                                                        \n"
        + "       NVL(SUM(DSIPO.IMPORTO_RENDICONTATO),0) AS IMPORTO_RENDICONTATO              \n"
        + "     FROM                                                                          \n"
        + "       IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,                                         \n"
        + "       MAX_ISTANZA_GRUPPO ISTANZA                                                  \n"
        + "     WHERE                                                                         \n"
        + "       DSIPO.ID_DOCUMENTO_SPESA_INTERVEN = :ID_DOCUMENTO_SPESA_INTERVEN            \n"
        + "       AND DSIPO.ID_PROCEDIMENTO_OGGETTO = ISTANZA.ID_PROCEDIMENTO_OGGETTO         \n"
        + "   )                                                                               \n"
        + "   ,                                                                               \n"
        + "   ACCERTAMENTO AS                                                                 \n"
        + "   (                                                                               \n"
        + "     SELECT                                                                        \n"
        + "       NVL(SUM(DSIAS.IMPORTO_DISPONIBILE),0) AS IMPORTO_DISPONIBILE                \n"
        + "     FROM                                                                          \n"
        + "       IUF_R_DOC_SPESA_INT_ACC_SPES DSIAS,                                        \n"
        + "       MAX_ISTRUTTORIA_GRUPPO ISTRUTTORIA                                          \n"
        + "     WHERE                                                                         \n"
        + "       DSIAS.ID_DOCUMENTO_SPESA_INTERVEN = :ID_DOCUMENTO_SPESA_INTERVEN            \n"
        + "       AND DSIAS.ID_PROCEDIMENTO_OGGETTO = ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO     \n"
        + "   )                                                                               \n"
        + " SELECT                                                                            \n"
        + "   DSI.IMPORTO - R.IMPORTO_RENDICONTATO + A.IMPORTO_DISPONIBILE                    \n"
        + " FROM                                                                              \n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV DSI,                                             \n"
        + "   RENDICONTAZIONE R,                                                              \n"
        + "   ACCERTAMENTO A                                                                  \n"
        + " WHERE                                                                             \n"
        + "   DSI.ID_DOCUMENTO_SPESA_INTERVEN = :ID_DOCUMENTO_SPESA_INTERVEN                  \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_DOCUMENTO_SPESA_INTERVEN",
        idDocumentoSpesaInterven, Types.NUMERIC);
    try
    {
      BigDecimal value = namedParameterJdbcTemplate.queryForObject(QUERY,
          mapParameterSource, BigDecimal.class);
      return BigDecimal.ZERO.compareTo(value) <= 0;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDocumentoSpesaInterven",
                  idDocumentoSpesaInterven)
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

  public DocumentoAllegatoDownloadDTO getDocumentoSpesaCorrenteByIdProcedimentoOggetto(
      long idProcedimentoOggetto, long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDocumentoSpesaCorrenteByIdProcedimentoOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                  \n"
        + "   DOCUMENTI AS                                                        \n"
        + "   (                                                                   \n"
        + "     SELECT                                                            \n"
        + "       DSF.NOME_FILE_FISICO_DOCUMENTO_SPE,                             \n"
        + "       DSF.FILE_DOCUMENTO_SPESA,                                       \n"
        + "       ROWNUM AS IDX                                                   \n"
        + "     FROM                                                              \n"
        + "       IUF_T_DOCUMENTO_SPESA_FILE DSF                                  \n"
        + "     LEFT JOIN IUF_R_DOC_SPESA_PROC_OGG DSPO                           \n"
        + "     ON                                                                \n"
        + "       DSF.ID_DOCUMENTO_SPESA_FILE      = DSPO.ID_DOCUMENTO_SPESA_FILE \n"
        + "       AND DSPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO     \n"
        + "     WHERE                                                             \n"
        + "       DSF.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA                    \n"
        + "     ORDER BY                                                          \n"
        + "       DSPO.ID_PROCEDIMENTO_OGGETTO DESC NULLS LAST,                   \n"
        + "       DSF.ID_DOCUMENTO_SPESA_FILE DESC                                \n"
        + "   )                                                                   \n"
        + " SELECT                                                                \n"
        + "   *                                                                   \n"
        + " FROM                                                                  \n"
        + "   DOCUMENTI D                                                         \n"
        + " WHERE                                                                 \n"
        + "   ROWNUM = 1                                                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DocumentoAllegatoDownloadDTO>()
          {
            @Override
            public DocumentoAllegatoDownloadDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                DocumentoAllegatoDownloadDTO result = new DocumentoAllegatoDownloadDTO();
                result.setFileName(
                    rs.getString("NOME_FILE_FISICO_DOCUMENTO_SPE"));
                result.setBytes(rs.getBytes("FILE_DOCUMENTO_SPESA"));
                return result;
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
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa)
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

  public DocumentoAllegatoDownloadDTO getDocumentoSpesaInRendicontazioneByIdProcedimentOggettoIstruttoria(
      long idProcedimentoOggetto, long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDocumentoSpesaInRendicontazioneByIdProcedimentOggettoIstruttoria";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                    \n"
        + "   ISTANZA AS                                                            \n"
        + "   (                                                                     \n"
        + "     SELECT                                                              \n"
        + "       MAX(PO.ID_PROCEDIMENTO_OGGETTO) AS ID_PROCEDIMENTO_OGGETTO        \n"
        + "     FROM                                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                    \n"
        + "       IUF_T_RENDICONTAZIONE_SPESE Rs,                                   \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                               \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTRUTTORIA                            \n"
        + "     WHERE                                                               \n"
        + "       ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO    \n"
        + "       AND PO.ID_PROCEDIMENTO              = ISTRUTTORIA.ID_PROCEDIMENTO \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO      = IPO.ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND IPO.DATA_FINE                  IS NULL                        \n"
        + "       AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                        \n"
        + "       AND RS.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO       \n"
        + "   )                                                                     \n"
        + " SELECT                                                                  \n"
        + "   DSF.NOME_FILE_FISICO_DOCUMENTO_SPE,                                   \n"
        + "   DSF.FILE_DOCUMENTO_SPESA                                              \n"
        + " FROM                                                                    \n"
        + "   IUF_T_DOCUMENTO_SPESA_FILE DSF,                                       \n"
        + "   IUF_R_DOC_SPESA_PROC_OGG DSPO,                                        \n"
        + "   ISTANZA I                                                             \n"
        + " WHERE                                                                   \n"
        + "   DSF.ID_DOCUMENTO_SPESA    = :ID_DOCUMENTO_SPESA                       \n"
        + "   AND DSF.ID_DOCUMENTO_SPESA_FILE   = DSPO.ID_DOCUMENTO_SPESA_FILE      \n"
        + "   AND I.ID_PROCEDIMENTO_OGGETTO = DSPO.ID_PROCEDIMENTO_OGGETTO          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DocumentoAllegatoDownloadDTO>()
          {
            @Override
            public DocumentoAllegatoDownloadDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                DocumentoAllegatoDownloadDTO result = new DocumentoAllegatoDownloadDTO();
                result.setFileName(
                    rs.getString("NOME_FILE_FISICO_DOCUMENTO_SPE"));
                result.setBytes(rs.getBytes("FILE_DOCUMENTO_SPESA"));
                return result;
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
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa)
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

  public int updateOrInsertRendicontazioneSpesePerModificaDocumentiDiRendicontazione(
      long idProcedimentoOggetto,
      long idIntervento, String flagInterventoCompletato, String note)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::updateOrInsertRendicontazioneSpesePerModificaDocumentiDiRendicontazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String UPDATE = " UPDATE                                                                    \n"
        + "   IUF_T_RENDICONTAZIONE_SPESE                                             \n"
        + " SET                                                                       \n"
        + "   FLAG_INTERVENTO_COMPLETATO = :FLAG_INTERVENTO_COMPLETATO,               \n"
        + "   NOTE = :NOTE,                                                           \n"
        + "   IMPORTO_SPESA =                                                         \n"
        + "   (                                                                       \n"
        + "     SELECT                                                                \n"
        + "       NVL(SUM(NVL(DSIPO.IMPORTO_RENDICONTATO,0)),0)                       \n"
        + "     FROM                                                                  \n"
        + "       IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,                                 \n"
        + "       IUF_R_DOCUMENTO_SPESA_INTERV DSI                                  \n"
        + "     WHERE                                                                 \n"
        + "       DSIPO.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN \n"
        + "       AND DSIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
        + "       AND DSI.ID_INTERVENTO             = :ID_INTERVENTO                  \n"
        + "   )                                                                       \n"
        + " WHERE                                                                     \n"
        + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                      \n"
        + "   AND ID_INTERVENTO       = :ID_INTERVENTO                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_INTERVENTO_COMPLETATO",
          flagInterventoCompletato, Types.VARCHAR);
      mapParameterSource.addValue("NOTE", note, Types.VARCHAR);
      int numRowsUpdated = namedParameterJdbcTemplate.update(UPDATE,
          mapParameterSource);
      if (numRowsUpdated == 0)
      {
        UPDATE = " INSERT                                                                        \n"
            + " INTO                                                                          \n"
            + "   IUF_T_RENDICONTAZIONE_SPESE                                                 \n"
            + "   (                                                                           \n"
            + "     ID_RENDICONTAZIONE_SPESE,                                                 \n"
            + "     CONTRIBUTO_RICHIESTO,                                                     \n"
            + "     FLAG_INTERVENTO_COMPLETATO,                                               \n"
            + "     ID_INTERVENTO,                                                            \n"
            + "     ID_PROCEDIMENTO_OGGETTO,                                                  \n"
            + "     IMPORTO_SPESA,                                                            \n"
            + "     NOTE                                                                      \n"
            + "   )                                                                           \n"
            + "   (                                                                           \n"
            + "     SELECT                                                                    \n"
            + "       SEQ_IUF_T_RENDICONTAZIO_SPES.NEXTVAL,                                 \n"
            + "       CONTRIBUTO_RICHIESTO,                                                   \n"
            + "       FLAG_INTERVENTO_COMPLETATO,                                             \n"
            + "       ID_INTERVENTO,                                                          \n"
            + "       ID_PROCEDIMENTO_OGGETTO,                                                \n"
            + "       IMPORTO_SPESA,                                                          \n"
            + "       :NOTE                                                                   \n"
            + "     FROM                                                                      \n"
            + "       (                                                                       \n"
            + "         SELECT                                                                \n"
            + "           0                               AS CONTRIBUTO_RICHIESTO,            \n"
            + "           :FLAG_INTERVENTO_COMPLETATO     AS FLAG_INTERVENTO_COMPLETATO,      \n"
            + "           :ID_INTERVENTO                  AS ID_INTERVENTO,                   \n"
            + "           :ID_PROCEDIMENTO_OGGETTO        AS ID_PROCEDIMENTO_OGGETTO ,        \n"
            + "           NVL(SUM(NVL(DSIPO.IMPORTO_RENDICONTATO,0)),0) AS IMPORTO_SPESA      \n"
            + "         FROM                                                                  \n"
            + "           IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,                                 \n"
            + "           IUF_R_DOCUMENTO_SPESA_INTERV DSI                                  \n"
            + "         WHERE                                                                 \n"
            + "           DSIPO.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN \n"
            + "           AND DSIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
            + "           AND DSI.ID_INTERVENTO             = :ID_INTERVENTO                  \n"
            + "       )                                                                       \n"
            + "   )                                                                           \n";
        numRowsUpdated = namedParameterJdbcTemplate.update(UPDATE,
            mapParameterSource);
      }
      return numRowsUpdated;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idIntervento", idIntervento)
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

  public int ricalcolaContributoRichiesto(long idProcedimentoOggetto,
      long idIntervento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "ricalcolaContributoRichiesto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String UPDATE = " UPDATE                                                                  \n"
        + "   IUF_T_RENDICONTAZIONE_SPESE                                           \n"
        + " SET                                                                     \n"
        + "   CONTRIBUTO_RICHIESTO = NVL(                                           \n"
        + "   (                                                                     \n"
        + "   WITH                                                                  \n"
        + "     PERCENTUALE AS                                                      \n"
        + "     (                                                                   \n"
        + "       SELECT                                                            \n"
        + "         I.ID_INTERVENTO,                                                \n"
        + "         DI.PERCENTUALE_CONTRIBUTO                                       \n"
        + "       FROM                                                              \n"
        + "         IUF_T_INTERVENTO I,                                             \n"
        + "         IUF_T_DETTAGLIO_INTERVENTO DI                                   \n"
        + "       WHERE                                                             \n"
        + "         I.ID_INTERVENTO     = :ID_INTERVENTO                            \n"
        + "         AND I.ID_INTERVENTO = DI.ID_INTERVENTO                          \n"
        + "         AND DI.DATA_FINE IS NULL                                        \n"
        + "         AND NOT EXISTS                                                  \n"
        + "         (                                                               \n"
        + "           SELECT                                                        \n"
        + "             *                                                           \n"
        + "           FROM                                                          \n"
        + "             IUF_W_DETT_INTERV_PROC_OGG WTMP                             \n"
        + "           WHERE                                                         \n"
        + "             WTMP.ID_INTERVENTO               = I.ID_INTERVENTO          \n"
        + "             AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "         )                                                               \n"
        + "       UNION                                                             \n"
        + "       SELECT                                                            \n"
        + "         DIPO.ID_INTERVENTO,                                             \n"
        + "         DIPO.PERCENTUALE_CONTRIBUTO                                     \n"
        + "       FROM                                                              \n"
        + "         IUF_W_DETT_INTERV_PROC_OGG DIPO                                 \n"
        + "       WHERE                                                             \n"
        + "         DIPO.ID_INTERVENTO               = :ID_INTERVENTO               \n"
        + "         AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO     \n"
        + "     )                                                                   \n"
        + "   SELECT                                                                \n"
        + "     (SUM(DSIPO.IMPORTO_RENDICONTATO) * P.PERCENTUALE_CONTRIBUTO) / 100  \n"
        + "   FROM                                                                  \n"
        + "     IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,                                 \n"
        + "     IUF_R_DOCUMENTO_SPESA_INTERV DSI,                                 \n"
        + "     PERCENTUALE P                                                       \n"
        + "   WHERE                                                                 \n"
        + "     DSIPO.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN \n"
        + "     AND DSIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
        + "     AND DSI.ID_INTERVENTO             = :ID_INTERVENTO                  \n"
        + "     AND DSI.ID_INTERVENTO             = P.ID_INTERVENTO                 \n"
        + "   GROUP BY                                                              \n"
        + "     P.PERCENTUALE_CONTRIBUTO                                            \n"
        + "   ),0)                                                                  \n"
        + " WHERE                                                                   \n"
        + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "   AND ID_INTERVENTO       = :ID_INTERVENTO                              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
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
          {
          }, UPDATE, mapParameterSource);
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

  public String getInfoSePossibileRendicontazioneConIVAByIdProcedimentoOggetto(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getInfoSePossibileRendicontazioneConIVAByIdProcedimentoOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                                      \n"
        + "   CASE B.FLAG_RENDICONTAZIONE_DOC_SPESA                                     \n"
        + "     WHEN 'N'                                                                \n"
        + "     THEN 'X'                                                                \n"
        + "     ELSE                                                                    \n"
        + "       CASE P.FLAG_RENDICONTAZIONE_CON_IVA                                   \n"
        + "         WHEN 'X'                                                            \n"
        + "         THEN 'X'                                                            \n"
        + "         ELSE                                                                \n"
        + "           CASE SUM(DECODE(LI.FLAG_DOCUMENTO_SPESA,'S', 1,0))                \n"
        + "             WHEN 0                                                          \n"
        + "             THEN 'X'                                                        \n"
        + "             ELSE P.FLAG_RENDICONTAZIONE_CON_IVA                             \n"
        + "           END                                                               \n"
        + "       END                                                                   \n"
        + "   END AS FLAG                                                               \n"
        + " FROM                                                                        \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                            \n"
        + "   IUF_T_PROCEDIMENTO P,                                                     \n"
        + "   IUF_D_BANDO B,                                                            \n"
        + "   IUF_R_LIVELLO_INTERVENTO LI,                                              \n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI,                                           \n"
        + "   IUF_T_INTERVENTO I,                                                       \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                     \n"
        + "   IUF_D_TIPO_CLASSIFICAZIONE TC,                                            \n"
        + "   IUF_D_CATEGORIA_INTERVENTO CI                                             \n"
        + " WHERE                                                                       \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO            = :ID_PROCEDIMENTO_OGGETTO          \n"
        + "   AND PO.ID_PROCEDIMENTO                = P.ID_PROCEDIMENTO                 \n"
        + "   AND P.ID_BANDO                        = B.ID_BANDO                        \n"
        + "   AND LI.ID_LIVELLO                     = LBI.ID_LIVELLO                    \n"
        + "   AND LBI.ID_BANDO                      = B.ID_BANDO                        \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO \n"
        + "   AND LBI.ID_DESCRIZIONE_INTERVENTO     = LI.ID_DESCRIZIONE_INTERVENTO      \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO       = LI.ID_DESCRIZIONE_INTERVENTO      \n"
        + "   AND DESCINT.ID_TIPO_CLASSIFICAZIONE   = TC.ID_TIPO_CLASSIFICAZIONE        \n"
        + "   AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)     \n"
        + "   AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO            \n"
        + "   AND I.ID_PROCEDIMENTO                 = P.ID_PROCEDIMENTO                 \n"
        + " GROUP BY                                                                    \n"
        + "   B.FLAG_RENDICONTAZIONE_DOC_SPESA,                                         \n"
        + "   P.FLAG_RENDICONTAZIONE_CON_IVA                                            \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.NO, Types.VARCHAR);
    try
    {
      return namedParameterJdbcTemplate.queryForObject(QUERY,
          mapParameterSource, String.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
          },
          new LogVariable[]
          {
          // new LogVariable("idVariabile", idVariabile),
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

  public Map<Long, InterventoAccertamentoDocumentiSpesaDTO> getAccertamentoDocumentiSpesaPerIntervento(
      long idProcedimentoOggetto,
      List<Long> idIntervento) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getAccertamentoDocumentiSpesaPerIntervento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                    \n"
        + "   ISTANZA AS                                                            \n"
        + "   (                                                                     \n"
        + "     SELECT                                                              \n"
        + "       MAX(PO.ID_PROCEDIMENTO_OGGETTO) AS ID_PROCEDIMENTO_OGGETTO        \n"
        + "     FROM                                                                \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                    \n"
        + "       IUF_T_RENDICONTAZIONE_SPESE Rs,                                   \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                               \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTRUTTORIA                            \n"
        + "     WHERE                                                               \n"
        + "       ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO    \n"

        + "		  AND PO.CODICE_RAGGRUPPAMENTO = (SELECT CODICE_RAGGRUPPAMENTO FROM IUF_T_PROCEDIMENTO_OGGETTO WHERE ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO   )\n"

        + "       AND PO.ID_PROCEDIMENTO              = ISTRUTTORIA.ID_PROCEDIMENTO \n"
        + "       AND PO.ID_PROCEDIMENTO_OGGETTO      = IPO.ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND IPO.DATA_FINE                  IS NULL                        \n"
        + "       AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                        \n"
        + "       AND RS.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO       \n"
        + "   )                                                                     \n"
        + "  SELECT                                                                                                                                         \n"
        + "    DI.PROGRESSIVO,                                                                                                                              \n"
        + "    DESCINT.DESCRIZIONE AS DESC_INTERVENTO,                                                                                                      \n"
        + "    F.RAGIONE_SOCIALE,                                                                                                                           \n"
        + "    DDS.DATA_DOCUMENTO_SPESA,                                                                                                                    \n"
        + "    DDS.NUMERO_DOCUMENTO_SPESA,                                                                                                                  \n"
        + "    TDS.DESCRIZIONE AS DESC_TIPO_DOCUMENTO_SPESA,                                                                                                \n"
        + "    DDS.DATA_PAGAMENTO,                                                                                                                          \n"
        + "    MP.DESCRIZIONE AS DESC_MODALITA_PAGAMENTO,                                                                                                   \n"
        + "    DECODE(P.FLAG_RENDICONTAZIONE_CON_IVA,'S',DDS.IMPORTO_SPESA+DDS.IMPORTO_IVA_SPESA,DDS.IMPORTO_SPESA) AS IMPORTO_SPESA,                       \n"
        + "    SUM(DECODE(ACCSP.ID_PROCEDIMENTO_OGGETTO, PO.ID_PROCEDIMENTO_OGGETTO, ACCSP.IMPORTO_ACCERTATO,NULL)) AS IMPORTO_ACCERTATO,                   \n"
        + "    SUM(DECODE(ACCSP.ID_PROCEDIMENTO_OGGETTO, PO.ID_PROCEDIMENTO_OGGETTO, ACCSP.IMPORTO_NON_RICONOSCIUTO,NULL)) AS IMPORTO_NON_RICONOSCIUTO,     \n"
        + "    SUM(DECODE(ACCSP.ID_PROCEDIMENTO_OGGETTO, PO.ID_PROCEDIMENTO_OGGETTO, ACCSP.IMPORTO_DISPONIBILE,NULL)) AS IMPORTO_DISPONIBILE,               \n"
        + "    SUM(DECODE(ACCSP.ID_PROCEDIMENTO_OGGETTO, PO.ID_PROCEDIMENTO_OGGETTO, ACCSP.IMPORTO_CALCOLO_CONTRIBUTO,NULL)) AS IMPORTO_CALCOLO_CONTRIBUTO, \n"
        + "    DSI.IMPORTO,                                                                                                                                 \n"
        + "    (                                                                                                                                            \n"
        + "    SELECT SUM(DSIPO.IMPORTO_RENDICONTATO)                                                                                                       \n"
        + "    FROM IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO                                                                                                      \n"
        + "     WHERE                                                                                                                                       \n"
        + "     DSI.ID_DOCUMENTO_SPESA_INTERVEN = DSIPO.ID_DOCUMENTO_SPESA_INTERVEN                                                                         \n"
        + "     AND DSIPO.ID_PROCEDIMENTO_OGGETTO = (                                                                                                       \n"
        + "                           SELECT MAX(V3.ID_PROCEDIMENTO_OGGETTO)                                                                                \n"
        + "                           FROM                                                                                                                  \n"
        + "                           IUF_T_PROCEDIMENTO_OGGETTO V1,                                                                                        \n"
        + "                           IUF_T_PROCEDIMENTO_OGGETTO V3,                                                                                        \n"
        + "                           IUF_T_PROCEDIMENTO V2,                                                                                                \n"
        + "                           IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                                                                                   \n"
        + "                           IUF_R_DOC_SPESA_INT_PROC_OGG DOC                                                                                      \n"
        + "                           WHERE                                                                                                                 \n"
        + "                           V1.ID_PROCEDIMENTO = V2.ID_PROCEDIMENTO                                                                               \n"
        + "                           AND V1.ID_PROCEDIMENTO = V3.ID_PROCEDIMENTO                                                                           \n"
        + "                           AND V1.CODICE_RAGGRUPPAMENTO = V3.CODICE_RAGGRUPPAMENTO                                                               \n"
        + "                           AND V1.ID_PROCEDIMENTO_OGGETTO <> V3.ID_PROCEDIMENTO_OGGETTO                                                          \n"
        + "                           AND V1.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                             \n"
        + "                           AND V3.ID_PROCEDIMENTO_OGGETTO = DOC.ID_PROCEDIMENTO_OGGETTO                                                          \n"
        + "                           AND V3.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO                                                          \n"
        + "                           AND IPO.DATA_FINE IS NULL                                                                                             \n"
        + "                           AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                                                            \n"
        + "    )) AS IMPORTO_RENDICONTATO,                                                                                                                  \n"
        + "    (                                                                        \n"
        + "     SELECT                                                                  \n"
        + "       SUBSTR(PROC_OGG.IDENTIFICATIVO, 13) || ' - ' ||              \n"
        + "       DSF.NOME_FILE_LOGICO_DOCUMENTO_SPE                                    \n"
        + "     FROM                                                                    \n"
        + "       IUF_T_DOCUMENTO_SPESA_FILE DSF,                                       \n"
        + "       IUF_R_DOC_SPESA_PROC_OGG DSPO,                                        \n"
        + "       ISTANZA I,                                                            \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PROC_OGG                                   \n"
        + "     WHERE                                                                   \n"
        + "       DSF.ID_DOCUMENTO_SPESA    = DS.ID_DOCUMENTO_SPESA                     \n"
        + "       AND DSF.ID_DOCUMENTO_SPESA_FILE   = DSPO.ID_DOCUMENTO_SPESA_FILE      \n"
        + "       AND I.ID_PROCEDIMENTO_OGGETTO = DSPO.ID_PROCEDIMENTO_OGGETTO          \n"
        + "       AND I.ID_PROCEDIMENTO_OGGETTO = PROC_OGG.ID_PROCEDIMENTO_OGGETTO      \n"
        + "       AND ROWNUM = 1                                                        \n"
        + "    ) AS NOME_FILE,                                                          \n"
        + "    DSI.ID_DOCUMENTO_SPESA_INTERVEN,                                                                                                             \n"
        + "    I.ID_INTERVENTO,                                                                                                                             \n"
        + "    DS.ID_DOCUMENTO_SPESA,                                                                                                                       \n"
        + "    DI.PERCENTUALE_CONTRIBUTO,                                                                                                    \n"
        + "   (                                                                             \n"
        + "     SELECT                                                                      \n"
        + "       COUNT(*)                                                                  \n"
        + "     FROM                                                                        \n"
        + "       IUF_R_DOC_SPESA_PROC_OGG SPO,                                             \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTANZA,                                       \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO ISTRUTTORIA,                                   \n"
        + "       IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                                       \n"
        + "       IUF_T_ACCERTAMENTO_SPESE ASP,                                             \n"
        + "       IUF_D_ESITO E                                                             \n"
        + "     WHERE                                                                       \n"
        + "       SPO.ID_DOCUMENTO_SPESA                  = DS.ID_DOCUMENTO_SPESA           \n"
        + "       AND SPO.ID_PROCEDIMENTO_OGGETTO         = ISTANZA.ID_PROCEDIMENTO_OGGETTO \n"
        + "       AND ISTANZA.ID_PROCEDIMENTO             = ISTRUTTORIA.ID_PROCEDIMENTO     \n"
        + "       AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = ASP.ID_PROCEDIMENTO_OGGETTO     \n"
        + "       AND ISTRUTTORIA.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO     \n"
        + "       AND IPO.DATA_FINE                      IS NULL                            \n"
        + "       AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90                                \n"
        + "       AND ISTANZA.CODICE_RAGGRUPPAMENTO = ISTRUTTORIA.CODICE_RAGGRUPPAMENTO     \n"
        + "       AND ISTRUTTORIA.ID_ESITO          = E.ID_ESITO                            \n"
        + "       AND E.CODICE                      = 'APP-N'                               \n"
        + "   ) AS WARNING_DOCUMENTO                                                        \n"
        + "  FROM                                                                                                                                           \n"
        + "    IUF_T_INTERVENTO I,                                                                                                                          \n"
        + "    IUF_T_DETTAGLIO_INTERVENTO DI,                                                                                                               \n"
        + "    IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                                                                                        \n"
        + "    IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                                                               \n"
        + "    IUF_T_PROCEDIMENTO P,                                                                                                                        \n"
        + "    IUF_T_DOCUMENTO_SPESA DS,                                                                                                                    \n"
        + "    IUF_T_DETT_DOCUMENTO_SPESA DDS                                                                                                               \n"
        + "  LEFT JOIN IUF_T_FORNITORE F                                                                                                                    \n"
        + "  ON                                                                                                                                             \n"
        + "    DDS.ID_FORNITORE = F.ID_FORNITORE                                                                                                            \n"
        + "    AND F.DATA_FINE IS NULL                                                                                                                      \n"
        + "  LEFT JOIN IUF_D_TIPO_DOCUMENTO_SPESA TDS                                                                                                       \n"
        + "  ON                                                                                                                                             \n"
        + "    DDS.ID_TIPO_DOCUMENTO_SPESA = TDS.ID_TIPO_DOCUMENTO_SPESA                                                                                    \n"
        + "  LEFT JOIN IUF_D_MODALITA_PAGAMENTO MP                                                                                                          \n"
        + "  ON                                                                                                                                             \n"
        + "    DDS.ID_MODALITA_PAGAMENTO = MP.ID_MODALITA_PAGAMENTO ,                                                                                       \n"
        + "    IUF_R_DOCUMENTO_SPESA_INTERV DSI                                                                                                           \n"
        + "  LEFT JOIN IUF_R_DOC_SPESA_INT_ACC_SPES ACCSP                                                                                                  \n"
        + "  ON                                                                                                                                             \n"
        + "    ACCSP.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN                                                                          \n"
        + "  WHERE                                                                                                                                          \n"
        + "    PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                                                        \n"
        + getInCondition("I.ID_INTERVENTO", idIntervento)
        + "   AND I.ID_INTERVENTO        = DI.ID_INTERVENTO                                                                                             \n"
        + "   AND NVL(PO.DATA_FINE, SYSDATE) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE,SYSDATE)                                                       \n"
        + "   AND I.ID_DESCRIZIONE_INTERVENTO = DESCINT.ID_DESCRIZIONE_INTERVENTO                                                                       \n"
        + "   AND PO.ID_PROCEDIMENTO          = DS.ID_PROCEDIMENTO                                                                                      \n"
        + "   AND PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO                                                                                      \n"
        + "   AND DS.ID_DOCUMENTO_SPESA       = DDS.ID_DOCUMENTO_SPESA                                                                                  \n"
        + "   AND NVL(PO.DATA_FINE,SYSDATE) BETWEEN DDS.DATA_INIZIO AND NVL(DDS.DATA_FINE, SYSDATE)                                                     \n"
        + "   AND DSI.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                                                        \n"
        + "   AND I.ID_INTERVENTO        = DSI.ID_INTERVENTO                                                                                            \n"
        + " GROUP BY                                                                                                                                    \n"
        + "   DI.PROGRESSIVO,                                                                                                                           \n"
        + "   P.FLAG_RENDICONTAZIONE_CON_IVA,                                                                                                           \n"
        + "   DESCINT.DESCRIZIONE,                                                                                                                      \n"
        + "   F.RAGIONE_SOCIALE,                                                                                                                        \n"
        + "   DDS.DATA_DOCUMENTO_SPESA,                                                                                                                 \n"
        + "   DDS.NUMERO_DOCUMENTO_SPESA,                                                                                                               \n"
        + "   TDS.DESCRIZIONE,                                                                                                                          \n"
        + "   DDS.DATA_PAGAMENTO,                                                                                                                       \n"
        + "   MP.DESCRIZIONE,                                                                                                                           \n"
        + "   DDS.IMPORTO_SPESA,                                                                                                                        \n"
        + "   DDS.IMPORTO_IVA_SPESA,                                                                                                                    \n"
        + "   DSI.IMPORTO,                                                                                                                              \n"
        + "   DDS.NOME_FILE_FISICO_DOCUMENTO_SPE,                                                                                                       \n"
        + "   DSI.ID_DOCUMENTO_SPESA_INTERVEN,                                                                                                          \n"
        + "   I.ID_INTERVENTO,                                                                                                                          \n"
        + "   DS.ID_DOCUMENTO_SPESA,                                                                                                                    \n"
        + "   DI.PERCENTUALE_CONTRIBUTO                                                                                                                 \n"
        + " ORDER BY                                                                                                                                    \n"
        + "   DI.PROGRESSIVO ASC,                                                                                                                       \n"
        + "   DDS.DATA_PAGAMENTO ASC,                                                                                                                   \n"
        + "   DDS.DATA_DOCUMENTO_SPESA ASC,                                                                                                             \n"
        + "   DDS.NUMERO_DOCUMENTO_SPESA ASC                                                                                                            \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<Long, InterventoAccertamentoDocumentiSpesaDTO>>()
          {
            @Override
            public Map<Long, InterventoAccertamentoDocumentiSpesaDTO> extractData(
                ResultSet rs)
                throws SQLException, DataAccessException
            {
              int lastProgressivo = Integer.MIN_VALUE;
              Map<Long, InterventoAccertamentoDocumentiSpesaDTO> list = new TreeMap<>();
              InterventoAccertamentoDocumentiSpesaDTO intervento = null;
              while (rs.next())
              {
                int progressivo = rs.getInt("PROGRESSIVO");
                if (progressivo != lastProgressivo)
                {
                  lastProgressivo = progressivo;
                  intervento = new InterventoAccertamentoDocumentiSpesaDTO();
                  intervento.setProgressivo(progressivo);
                  intervento.setPercentualeContributo(
                      rs.getBigDecimal("PERCENTUALE_CONTRIBUTO"));
                  intervento.setIdIntervento(rs.getLong("ID_INTERVENTO"));
                  intervento.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                  intervento.setAccertamento(new ArrayList<>());
                  list.put(intervento.getIdIntervento(), intervento);
                }
                RigaAccertamentoDocumentiSpesaDTO riga = new RigaAccertamentoDocumentiSpesaDTO();
                final BigDecimal importoRendicontato = rs
                    .getBigDecimal("IMPORTO_RENDICONTATO");
                if (importoRendicontato != null)
                {
                  intervento.addToSpesaRendicontataAttuale(importoRendicontato);
                  intervento.getAccertamento().add(riga);
                  riga.setDataDocumentoSpesa(
                      rs.getDate("DATA_DOCUMENTO_SPESA"));
                  riga.setDataPagamento(rs.getDate("DATA_PAGAMENTO"));
                  riga.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                  riga.setDescModalitaPagamento(
                      rs.getString("DESC_MODALITA_PAGAMENTO"));
                  riga.setDescTipoDocumentoSpesa(
                      rs.getString("DESC_TIPO_DOCUMENTO_SPESA"));
                  riga.setIdDocumentoSpesaInterven(
                      rs.getLong("ID_DOCUMENTO_SPESA_INTERVEN"));
                  riga.setImporto(rs.getBigDecimal("IMPORTO"));
                  riga.setImportoRendicontato(importoRendicontato);
                  riga.setImportoSpesa(rs.getBigDecimal("IMPORTO_SPESA"));
                  riga.setNomeFile(rs.getString("NOME_FILE"));
                  riga.setNumeroDocumentoSpesa(
                      rs.getString("NUMERO_DOCUMENTO_SPESA"));
                  riga.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
                  riga.setIdDocumentoSpesa(rs.getLong("ID_DOCUMENTO_SPESA"));
                  riga.setImportoAccertato(
                      rs.getBigDecimal("IMPORTO_ACCERTATO"));
                  riga.setImportoNonRiconosciuto(
                      rs.getBigDecimal("IMPORTO_NON_RICONOSCIUTO"));
                  riga.setImportoDisponibile(
                      rs.getBigDecimal("IMPORTO_DISPONIBILE"));
                  riga.setImportoCalcoloContributo(
                      rs.getBigDecimal("IMPORTO_CALCOLO_CONTRIBUTO"));
                  riga.setWarningDocumento(rs.getInt("WARNING_DOCUMENTO") > 0);
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
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
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

  public int updateOrInsertAccertamentoSpeseDocumenti(
      long idProcedimentoOggetto,
      RigaAccertamentoDocumentiSpesaDTO riga, long idUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateRendicontazioneSpeseDocumenti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                                           \n"
        + "   IUF_R_DOC_SPESA_INT_ACC_SPES                                   \n"
        + " SET                                                              \n"
        + "   IMPORTO_ACCERTATO = :IMPORTO_ACCERTATO,                  \n"
        + "   IMPORTO_NON_RICONOSCIUTO = :IMPORTO_NON_RICONOSCIUTO,                  \n"
        + "   IMPORTO_DISPONIBILE = :IMPORTO_DISPONIBILE,                  \n"
        + "   IMPORTO_CALCOLO_CONTRIBUTO = :IMPORTO_CALCOLO_CONTRIBUTO,                  \n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO     \n"
        + " WHERE                                                            \n"
        + "   ID_PROCEDIMENTO_OGGETTO         = :ID_PROCEDIMENTO_OGGETTO     \n"
        + "   AND ID_DOCUMENTO_SPESA_INTERVEN = :ID_DOCUMENTO_SPESA_INTERVEN \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_DOCUMENTO_SPESA_INTERVEN",
        riga.getIdDocumentoSpesaInterven(), Types.NUMERIC);
    mapParameterSource.addValue("IMPORTO_ACCERTATO",
        riga.getImportoAccertatoBD(), Types.NUMERIC);
    mapParameterSource.addValue("IMPORTO_NON_RICONOSCIUTO",
        riga.getImportoNonRiconosciutoBD(), Types.NUMERIC);
    mapParameterSource.addValue("IMPORTO_DISPONIBILE",
        riga.getImportoDisponibileBD(), Types.NUMERIC);
    mapParameterSource.addValue("IMPORTO_CALCOLO_CONTRIBUTO",
        riga.getImportoCalcoloContributoBD(), Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
        idUtenteAggiornamento, Types.NUMERIC);
    int numRowsUpdated = 0;
    try
    {
      numRowsUpdated = namedParameterJdbcTemplate.update(QUERY,
          mapParameterSource);
      if (numRowsUpdated == 0)
      {
        QUERY = " INSERT INTO                                 \n"
            + "   IUF_R_DOC_SPESA_INT_ACC_SPES              \n"
            + "   (                                         \n"
            + "     ID_PROCEDIMENTO_OGGETTO,                \n"
            + "     IMPORTO_ACCERTATO,                   \n"
            + "     IMPORTO_NON_RICONOSCIUTO,                   \n"
            + "     IMPORTO_DISPONIBILE,                   \n"
            + "     IMPORTO_CALCOLO_CONTRIBUTO,                   \n"
            + "     ID_DOCUMENTO_SPESA_INTERVEN,            \n"
            + "     ID_DOC_SPESA_INT_ACC_SPESE,              \n"
            + "     EXT_ID_UTENTE_AGGIORNAMENTO,            \n"
            + "     DATA_ULTIMO_AGGIORNAMENTO               \n"
            + "   )                                         \n"
            + "   VALUES                                    \n"
            + "   (                                         \n"
            + "     :ID_PROCEDIMENTO_OGGETTO,               \n"
            + "     :IMPORTO_ACCERTATO,                  \n"
            + "     :IMPORTO_NON_RICONOSCIUTO,                  \n"
            + "     :IMPORTO_DISPONIBILE,                  \n"
            + "     :IMPORTO_CALCOLO_CONTRIBUTO,                  \n"
            + "     :ID_DOCUMENTO_SPESA_INTERVEN,           \n"
            + "     SEQ_IUF_R_DOC_SPE_INT_ACC_SP.NEXTVAL, \n"
            + "     :EXT_ID_UTENTE_AGGIORNAMENTO,           \n"
            + "     SYSDATE                                 \n"
            + "   )                                         \n";
        numRowsUpdated = namedParameterJdbcTemplate.update(QUERY,
            mapParameterSource);
      }
      return numRowsUpdated;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("riga", riga)
          },
          new LogVariable[]
          {
              new LogVariable("numRowsUpdated", numRowsUpdated),
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

  public int ricalcolaContributoAccertato(long idProcedimentoOggetto,
      long idIntervento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "ricalcolaContributoAccertato";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String UPDATE = " UPDATE                                                                  \n"
        + "   IUF_T_ACCERTAMENTO_SPESE                                           \n"
        + " SET                                                                     \n"
        + "   CONTRIBUTO_CALCOLATO =                                                \n"
        + "   (                                                                     \n"
        + "   WITH                                                                  \n"
        + "     PERCENTUALE AS                                                      \n"
        + "     (                                                                   \n"
        + "       SELECT                                                            \n"
        + "         I.ID_INTERVENTO,                                                \n"
        + "         DI.PERCENTUALE_CONTRIBUTO                                       \n"
        + "       FROM                                                              \n"
        + "         IUF_T_INTERVENTO I,                                             \n"
        + "         IUF_T_DETTAGLIO_INTERVENTO DI                                   \n"
        + "       WHERE                                                             \n"
        + "         I.ID_INTERVENTO     = :ID_INTERVENTO                            \n"
        + "         AND I.ID_INTERVENTO = DI.ID_INTERVENTO                          \n"
        + "         AND DI.DATA_FINE IS NULL                                        \n"
        + "         AND NOT EXISTS                                                  \n"
        + "         (                                                               \n"
        + "           SELECT                                                        \n"
        + "             *                                                           \n"
        + "           FROM                                                          \n"
        + "             IUF_W_DETT_INTERV_PROC_OGG WTMP                             \n"
        + "           WHERE                                                         \n"
        + "             WTMP.ID_INTERVENTO               = I.ID_INTERVENTO          \n"
        + "             AND WTMP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + "         )                                                               \n"
        + "       UNION                                                             \n"
        + "       SELECT                                                            \n"
        + "         DIPO.ID_INTERVENTO,                                             \n"
        + "         DIPO.PERCENTUALE_CONTRIBUTO                                     \n"
        + "       FROM                                                              \n"
        + "         IUF_W_DETT_INTERV_PROC_OGG DIPO                                 \n"
        + "       WHERE                                                             \n"
        + "         DIPO.ID_INTERVENTO               = :ID_INTERVENTO               \n"
        + "         AND DIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO     \n"
        + "     )                                                                   \n"
        + "   SELECT                                                                \n"
        + "     (SUM(DSIPO.IMPORTO_ACCERTATO) * P.PERCENTUALE_CONTRIBUTO) / 100  \n"
        + "   FROM                                                                  \n"
        + "     IUF_R_DOC_SPESA_INT_ACC_SPES DSIPO,                                 \n"
        + "     IUF_R_DOCUMENTO_SPESA_INTERV DSI,                                  \n"
        + "     PERCENTUALE P                                                       \n"
        + "   WHERE                                                                 \n"
        + "     DSIPO.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN \n"
        + "     AND DSIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
        + "     AND DSI.ID_INTERVENTO             = :ID_INTERVENTO                  \n"
        + "     AND DSI.ID_INTERVENTO             = P.ID_INTERVENTO                 \n"
        + "   GROUP BY                                                              \n"
        + "     P.PERCENTUALE_CONTRIBUTO                                            \n"
        + "   )                                                                     \n"
        + " WHERE                                                                   \n"
        + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                    \n"
        + "   AND ID_INTERVENTO       = :ID_INTERVENTO                              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
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
          {
          }, UPDATE, mapParameterSource);
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

  public void deleteDocSpesaProcOggPerProcedimentoOggettoByIdDocumentoSpesaList(
      long idProcedimentoOggetto,
      List<Long> ids) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "deleteDocSpesaProcOggPerProcedimentoOggettoByIdDocumentoSpesaList";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String DELETE = " DELETE FROM                                                          \n"
        + "   IUF_R_DOC_SPESA_PROC_OGG DSPO                                      \n"
        + " WHERE                                                                \n"
        + "   ID_PROCEDIMENTO_OGGETTO       = :ID_PROCEDIMENTO_OGGETTO           \n"
        + getInCondition("DSPO.ID_DOCUMENTO_SPESA", ids)
        + " AND NOT EXISTS ("
        + " SELECT *                                                               \n"
        + " FROM                                                                  \n"
        + "   IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,                                 \n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV DSI                                  \n"
        + " WHERE                                                                 \n"
        + "   DSI.ID_DOCUMENTO_SPESA_INTERVEN = DSIPO.ID_DOCUMENTO_SPESA_INTERVEN \n"
        + "   AND DSIPO.ID_PROCEDIMENTO_OGGETTO= :ID_PROCEDIMENTO_OGGETTO         \n"
        + "   AND DSI.ID_DOCUMENTO_SPESA= DSPO.ID_DOCUMENTO_SPESA                 \n"
        + " )";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("ids", ids),
          },
          new LogVariable[]
          {
          }, DELETE, mapParameterSource);
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

  public void insertDocSpesaProcOgg(long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertDocSpesaProcOgg";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String DELETE = " INSERT                                                   \n"
        + " INTO                                                     \n"
        + "   IUF_R_DOC_SPESA_PROC_OGG                               \n"
        + "   (                                                      \n"
        + "     ID_DOC_SPESA_PROC_OGG,                               \n"
        + "     ID_DOCUMENTO_SPESA,                                  \n"
        + "     ID_DOCUMENTO_SPESA_FILE,                             \n"
        + "     ID_PROCEDIMENTO_OGGETTO,                             \n"
        + "     ID_RICEVUTA_PAGAMENTO                                \n"
        + "   )                                                      \n"
        + "   (                                                      \n"
        + "     SELECT                                               \n"
        + "       SEQ_IUF_R_DOC_SPESA_PROC_OGG.NEXTVAL,              \n"
        + "       RP.ID_DOCUMENTO_SPESA,                             \n"
        + "       (                                                  \n"
        + "         SELECT                                           \n"
        + "           MAX(DSF.ID_DOCUMENTO_SPESA_FILE)               \n"
        + "         FROM                                             \n"
        + "           IUF_T_DOCUMENTO_SPESA_FILE DSF                 \n"
        + "         WHERE                                            \n"
        + "           DSF.ID_DOCUMENTO_SPESA = RP.ID_DOCUMENTO_SPESA \n"
        + "       ) AS ID_DOCUMENTO_SPESA_FILE,                      \n"
        + "       :ID_PROCEDIMENTO_OGGETTO,                          \n"
        + "       RP.ID_RICEVUTA_PAGAMENTO                           \n"
        + "     FROM                                                 \n"
        + "       IUF_T_RICEVUTA_PAGAMENTO RP                        \n"
        + "     WHERE                                                \n"
        + "       1=1                                                \n"
        + getInCondition("RP.ID_DOCUMENTO_SPESA", ids)
        + "   )                                                      \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("ids", ids),
          },
          new LogVariable[]
          {
          }, DELETE, mapParameterSource);
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
