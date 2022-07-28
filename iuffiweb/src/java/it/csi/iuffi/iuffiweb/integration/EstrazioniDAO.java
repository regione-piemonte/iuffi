package it.csi.iuffi.iuffiweb.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import it.csi.iuffi.iuffiweb.dto.RigaFiltroDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.DettaglioImportoDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.FlagEstrazioneDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.ImportiTotaliDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.NumeroLottoDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.estrazionecampione.RigaSimulazioneEstrazioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public class EstrazioniDAO extends BaseDAO
{
  private static final String THIS_CLASS = EstrazioniDAO.class.getSimpleName();

  public EstrazioniDAO()
  {
  }

  public ImportiTotaliDTO getImportiTotali(long idNumeroLotto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportiTotali]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                                                                              \n"
        + "   A.ID_NUMERO_LOTTO,                                                                                                                                \n"
        + "   A.IMPORTO_RICHIESTO_COMPLESSIVO,                                                                                                                  \n"
        + "   A.IMPORTO_RICHIESTO_ESTRAZIONE,                                                                                                                   \n"
        + "   A.ID_STATO_ESTRAZIONE,                                                                                                                            \n"
        + "   A.DATA_ELABORAZIONE,                                                                                                                              \n"
        + "   C.ID_LIVELLO,                                                                                                                                     \n"
        + "   C.CODICE,                                                                                                                                         \n"
        + "   B.IMPORTO_RICHIESTO,                                                                                                                               \n"
        + "   B.TIPO_IMPORTO,                                                                                                                                   \n"
        + "   DECODE(B.TIPO_IMPORTO, 'C', 'Imp. richiesto complessivo', 'A', 'Imp. estrazione Attuale', 'P', 'Imp. estrazioni Pregresse') AS DESCR_TIPO_IMPORTO \n"
        + " FROM                                                                                                                                                \n"
        + "   IUF_D_NUMERO_LOTTO A,                                                                                                                             \n"
        + "   IUF_T_IMPORTI_ESTRAZ_DP B,                                                                                                                        \n"
        + "   IUF_D_LIVELLO C                                                                                                                                   \n"
        + " WHERE                                                                                                                                               \n"
        + "   A.ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO                                                                                                              \n"
        + "   AND B.ID_NUMERO_LOTTO = A.ID_NUMERO_LOTTO                                                                                                         \n"
        + "   AND C.ID_LIVELLO = B.ID_LIVELLO                                                                                                                   \n"
        + "   AND B.TIPO_IMPORTO = 'C'                                                                                                                          \n"
        + " ORDER BY                                                                                                                                            \n"
        + "   B.PROGRESSIVO                                                                                                                                     \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<ImportiTotaliDTO>()
          {

            @Override
            public ImportiTotaliDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ImportiTotaliDTO importi = new ImportiTotaliDTO();
              List<DettaglioImportoDTO> elenco = null;
              DettaglioImportoDTO dettaglio = null;
              int c = 0;

              while (rs.next())
              {
                if (c == 0)
                {
                  importi.setDataEstrazioneAttuale(
                      rs.getDate("DATA_ELABORAZIONE"));
                  importi.setIdNumeroLotto(idNumeroLotto);
                  importi.setImpTotaleAttuale(
                      rs.getBigDecimal("IMPORTO_RICHIESTO_ESTRAZIONE"));
                  importi.setImpTotaleComplessivo(
                      rs.getBigDecimal("IMPORTO_RICHIESTO_COMPLESSIVO"));
                  elenco = new ArrayList<>();
                  importi.setElencoDettagliImporti(elenco);
                  c++;
                }
                dettaglio = new DettaglioImportoDTO();
                dettaglio.setCodiceLivello(rs.getString("CODICE"));
                dettaglio
                    .setDecodificaImporto(rs.getString("DESCR_TIPO_IMPORTO"));
                dettaglio.setIdLivello(rs.getLong("ID_LIVELLO"));
                dettaglio.setImporto(rs.getBigDecimal("IMPORTO_RICHIESTO"));
                dettaglio.setTipoImporto(rs.getString("TIPO_IMPORTO"));
                elenco.add(dettaglio);
              }
              return importi;
            }
          });
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

  public List<DettaglioImportoDTO> getImportiAttualiPrecedenti(
      long idNumeroLotto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportiAttualiPrecedenti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                     \n"
        + "   A.ID_NUMERO_LOTTO,                       \n"
        + "   A.ID_STATO_ESTRAZIONE,                   \n"
        + "   A.ID_TIPO_ESTRAZIONE,              	     \n"
        + "   B.ID_LIVELLO,                            \n"
        + "   C.CODICE AS CODICE_LIVELLO,              \n"
        + "   B.TIPO_IMPORTO,                          \n"
        + "   DTE.DESCRIZIONE AS DESCR_TIPO_ESTRAZIONE,  \n"
        + "   DSE.DESCRIZIONE AS DESCR_STATO_ESTRAZIONE, \n"
        + "   B.IMPORTO_RICHIESTO,                     \n"
        + "   B.IMPORTO_RICHIESTO_PARTE_CASUAL         \n"
        + " FROM                                       \n"
        + "   IUF_D_NUMERO_LOTTO A,                    \n"
        + "   IUF_T_IMPORTI_ESTRAZ_DP B,               \n"
        + "   IUF_D_STATO_ESTRAZIONE DSE,              \n"
        + "   IUF_D_TIPO_ESTRAZIONE DTE,               \n"
        + "   IUF_D_LIVELLO C                          \n"
        + " WHERE                                      \n"
        + "   A.ID_NUMERO_LOTTO = B.ID_NUMERO_LOTTO    \n"
        + "   AND A.ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO \n"
        + "   AND C.ID_LIVELLO = B.ID_LIVELLO          \n"
        + "   AND DSE.ID_STATO_ESTRAZIONE =A.ID_STATO_ESTRAZIONE                                                                                         \n"
        + "   AND DTE.ID_TIPO_ESTRAZIONE =A.ID_TIPO_ESTRAZIONE                                                                                           \n"
        + "   AND B.TIPO_IMPORTO IN ('A','P')          \n"
        + " ORDER BY B.PROGRESSIVO ASC                 \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
        Types.NUMERIC);
    try
    {
      return queryForList(QUERY, mapParameterSource, DettaglioImportoDTO.class);
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

  public List<RigaSimulazioneEstrazioneDTO> getElencoRisultati(
      long idNumeroLotto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoRisultati]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                                                                   \n"
        + "   A.ID_ANALISI_RISCHIO,                                                                                                                  \n"
        + "   L.ID_TIPO_ESTRAZIONE,                                                                                                                  \n"
        + "   L.ID_STATO_ESTRAZIONE,                                                                                                                 \n"
        + "   DTE.DESCRIZIONE AS DESCR_TIPO_ESTRAZIONE,                                                                                              \n"
        + "   DSE.DESCRIZIONE AS DESCR_STATO_ESTRAZIONE,                                                                                             \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO,	                                                                                                           \n"
        + "   A.EXT_ID_AMM_COMPETENZA,                                                                                                               \n"
        + "   VAC.DESCRIZIONE AS DESCR_ENTE_DELEGATO,                                                                                                \n"
        + "   DL.ID_LIVELLO,                                                                                                                         \n"
        + "   DL.CODICE AS COD_LIVELLO,                                                                                                              \n"
        + "   PO.IDENTIFICATIVO,                                                                                                                     \n"
        + "   DO.ID_OGGETTO,                                                                                                                         \n"
        + "   DO.DESCRIZIONE AS DESCR_TIPO_DOMANDA,                                                                                                  \n"
        + "   DSO.DESCRIZIONE AS DESCR_STATO,                                                                                                        \n"
        + "   VDA.CUAA AS CUAA_AZIE,                                                                                                                 \n"
        + "   VDA.DENOMINAZIONE AS DENOMIZIONE_AZIE,                                                                                                 \n"
        + "   (SELECT SUM(T.IMPORTO_RICHIESTO) FROM IUF_T_ANALISI_RISCHIO_IMP T WHERE T.ID_ANALISI_RISCHIO = A.ID_ANALISI_RISCHIO) AS IMP_RICHIESTO, \n"
        + "   A.PUNTEGGIO,                                                                                                                           \n"
        + "   A.CLASSE,                                                                                                                              \n"
        + "   A.FLAG_ESTRATTA,                                                                                                                       \n"
        + "   DECODE(A.FLAG_ESTRATTA,                                                                                                                \n"
        + "           'C', 'Casuale',                                                                                                                \n"
        + "           'R', 'Analisi del rischio',                                                                                                    \n"
        + "           'F', 'Analisi del rischio - marcatura forzata',                                                                                \n"
        + "           'A', 'Analisi del rischio - marcatura',                                                                                         \n"
        + "           'D', 'Dichiarazioni'                                                                                         					\n"
        + "           ) AS DESCR_FLAG_ESTRATTA                                                                                                       \n"
        + " FROM                                                                                                                                     \n"
        + "   IUF_T_ANALISI_RISCHIO A,                                                                                                               \n"
        + "   IUF_D_NUMERO_LOTTO L,                                                                                                                  \n"
        + "   SMRCOMUNE_V_AMM_COMPETENZA VAC,                                                                                                        \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                                                         \n"
        + "   IUF_R_PROCEDIMENTO_LIVELLO PL,                                                                                                         \n"
        + "   IUF_D_LIVELLO DL,                                                                                                                      \n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                                                                                       \n"
        + "   IUF_D_OGGETTO DO,                                                                                                                      \n"
        + "   IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                                                                                                    \n"
        + "   IUF_D_STATO_OGGETTO DSO,                                                                                                               \n"
        + "   SMRGAA_V_DATI_ANAGRAFICI VDA,                                                                                                          \n"
        + "   IUF_D_STATO_ESTRAZIONE DSE,                                                                                                            \n"
        + "   IUF_D_TIPO_ESTRAZIONE DTE,                                                                                                             \n"
        + "   IUF_T_PROCEDIMENTO_AZIENDA PAZ                                                                                                         \n"
        + " WHERE                                                                                                                                    \n"
        + "   A.ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO                                                                                                   \n"
        + "   AND A.ID_NUMERO_LOTTO =L.ID_NUMERO_LOTTO                                                                                                   \n"
        + "   AND DSE.ID_STATO_ESTRAZIONE =L.ID_STATO_ESTRAZIONE                                                                                         \n"
        + "   AND DTE.ID_TIPO_ESTRAZIONE =L.ID_TIPO_ESTRAZIONE                                                                                           \n"
        + "   AND A.EXT_ID_AMM_COMPETENZA = VAC.ID_AMM_COMPETENZA                                                                                    \n"
        + "   AND PO.ID_PROCEDIMENTO_OGGETTO = A.ID_PROCEDIMENTO_OGGETTO                                                                             \n"
        + "   AND PL.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                                                                                            \n"
        + "   AND PL.ID_LIVELLO = DL.ID_LIVELLO                                                                                                      \n"
        + "   AND LGO.ID_LEGAME_GRUPPO_OGGETTO = PO.ID_LEGAME_GRUPPO_OGGETTO                                                                         \n"
        + "   AND DO.ID_OGGETTO = LGO.ID_OGGETTO                                                                                                     \n"
        + "   AND IPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO                                                                           \n"
        + "   AND IPO.DATA_FINE IS NULL                                                                                                              \n"
        + "   AND DSO.ID_STATO_OGGETTO = IPO.ID_STATO_OGGETTO                                                                                        \n"
        + "   AND PAZ.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                                                                                           \n"
        + "   AND PAZ.DATA_FINE IS NULL                                                                                                              \n"
        + "   AND VDA.ID_AZIENDA = PAZ.EXT_ID_AZIENDA                                                                                                \n"
        + "   AND VDA.DATA_FINE_VALIDITA IS NULL                                                                                                     \n"
        + " ORDER BY                                                                                                                                 \n"
        + "   A.PUNTEGGIO DESC, A.CLASSE DESC, IMP_RICHIESTO  DESC, PO.ID_PROCEDIMENTO_OGGETTO,DL.ORDINAMENTO                                                                           \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<RigaSimulazioneEstrazioneDTO>>()
          {

            @Override
            public List<RigaSimulazioneEstrazioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<RigaSimulazioneEstrazioneDTO> list = new ArrayList<RigaSimulazioneEstrazioneDTO>();
              Long lastKey = null;
              List<LivelloDTO> livelli = null;
              while (rs.next())
              {
                long idProcOggetto = rs.getLong("ID_PROCEDIMENTO_OGGETTO");
                if (lastKey == null || lastKey != idProcOggetto)
                {
                  lastKey = idProcOggetto;
                  RigaSimulazioneEstrazioneDTO riga = new RigaSimulazioneEstrazioneDTO();
                  riga.setCuaAzie(rs.getString("CUAA_AZIE"));
                  riga.setDenominazioneAzie(rs.getString("DENOMIZIONE_AZIE"));
                  riga.setDescrEnteDelegato(
                      rs.getString("DESCR_ENTE_DELEGATO"));
                  riga.setDescrStato(rs.getString("DESCR_STATO"));
                  riga.setDescrTipoDomanda(rs.getString("DESCR_TIPO_DOMANDA"));
                  riga.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                  riga.setIdProcedimentoOggetto(
                      rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
                  riga.setImportoRichiesto(rs.getBigDecimal("IMP_RICHIESTO"));
                  riga.setPunteggio(rs.getBigDecimal("PUNTEGGIO"));
                  riga.setClasse(rs.getString("CLASSE"));
                  riga.setFlagEstratta(rs.getString("FLAG_ESTRATTA"));
                  riga.setFlagEstrattaDescr(
                      rs.getString("DESCR_FLAG_ESTRATTA"));
                  riga.setIdTipoEstrazione(rs.getLong("ID_TIPO_ESTRAZIONE"));
                  riga.setIdStatoEstrazione(rs.getLong("ID_STATO_ESTRAZIONE"));
                  riga.setDescrTipoEstrazione(
                      rs.getString("DESCR_TIPO_ESTRAZIONE"));
                  riga.setDescrStatoEstrazione(
                      rs.getString("DESCR_STATO_ESTRAZIONE"));

                  livelli = new ArrayList<>();
                  riga.setListLivelli(livelli);
                  list.add(riga);
                }
                LivelloDTO livello = new LivelloDTO();
                livello.setCodice(rs.getString("COD_LIVELLO"));
                livello.setIdLivello(rs.getLong("ID_LIVELLO"));
                livelli.add(livello);
              }
              return list;
            }
          });
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

  public MainControlloDTO callRegistraDP(long idNumeroLotto,
      long idTipoEstrazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "callRegistraDP";
    StringBuffer CALL = new StringBuffer(
        "PCK_IUF_ESTRAZ_CAMPIONE_DP.RegistraDP");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("PIDNUMEROLOTTO", idNumeroLotto, Types.NUMERIC);
      parameterSource.addValue("PIDTIPOESTRAZIONE", idTipoEstrazione,
          Types.NUMERIC);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_IUF_ESTRAZ_CAMPIONE_DP")
              .withProcedureName("RegistraDP")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlParameter("PIDNUMEROLOTTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PIDTIPOESTRAZIONE", java.sql.Types.NUMERIC));

      call.addDeclaredParameter(
          new SqlOutParameter("PRISULTATO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("PMESSAGGIO", java.sql.Types.VARCHAR));

      Map<String, Object> results = call.execute(parameterSource);

      MainControlloDTO dto = new MainControlloDTO();
      dto.setRisultato(((BigDecimal) results.get("PRISULTATO")).intValue());
      dto.setMessaggio(safeMessaggioPLSQL((String) results.get("PMESSAGGIO")));
      return dto;

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          CALL.toString());
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
      }
    }
  }

  public Boolean isEstrazioneAnnullabile(long idNumeroLotto,
      long idTipoEstrazione) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::isEstrazioneAnnullabile]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                           \n"
        + "   A.ID_ESTRAZIONE_CAMPIONE,				       \n"
        + "   MAX(B.ID_ESTRAZIONE_CAMPIONE) AS LAST_ID       \n"
        + " FROM                                             \n"
        + "   IUF_T_ESTRAZIONE_CAMPIONE A,                   \n"
        + "   IUF_T_ESTRAZIONE_CAMPIONE B                    \n"
        + " WHERE                                            \n"
        + "   A.ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO           \n"
        + "   AND A.ID_TIPO_ESTRAZIONE = B.ID_TIPO_ESTRAZIONE      \n"
        + "   AND A.ID_TIPO_ESTRAZIONE = :ID_TIPO_ESTRAZIONE \n"
        + " GROUP BY A.ID_ESTRAZIONE_CAMPIONE                \n"
        + " ORDER BY                                         \n"
        + "   A.ID_ESTRAZIONE_CAMPIONE DESC                  \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_TIPO_ESTRAZIONE", idTipoEstrazione,
        Types.NUMERIC);
    try
    {
      Boolean res = namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                if (rs.getLong("LAST_ID") == rs
                    .getLong("ID_ESTRAZIONE_CAMPIONE"))
                {
                  return Boolean.TRUE;
                }
              }

              return Boolean.FALSE;
            }
          });
      return res;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idNumeroLotto", idNumeroLotto),
              new LogParameter("idTipoEstrazione", idTipoEstrazione)
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

  public void updateTEstrazioneCampione(long idNumeroLotto, String motivo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateTEstrazioneCampione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                                        \n"
        + "   IUF_T_ESTRAZIONE_CAMPIONE                                  \n"
        + " SET                                                           \n"
        + "   DATA_ANNULLAMENTO                        = SYSDATE,                        \n"
        + "   MOTIVAZIONE_ANNULLAMENTO = :MOTIVAZIONE_ANNULLAMENTO \n"
        + " WHERE                                                         \n"
        + "   ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
          Types.NUMERIC);
      mapParameterSource.addValue("MOTIVAZIONE_ANNULLAMENTO", motivo,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idNumeroLotto", idNumeroLotto),
              new LogParameter("motivo", motivo)
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

  public void updateTAnalisiRischio(long idNumeroLotto, String flagAnnulato)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateTEstrazioneCampione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                  \n"
        + "   IUF_T_ANALISI_RISCHIO                 \n"
        + " SET                                     \n"
        + "   FLAG_ANNULLATO = :FLAG_ANNULLATO 		\n"
        + " WHERE                                 	\n"
        + "   ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO  	\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_ANNULLATO", flagAnnulato,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idNumeroLotto", idNumeroLotto),
              new LogParameter("flagAnnulato", flagAnnulato)
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

  public void updateStatoEstrazione(long idNumeroLotto, long idStatoEstrazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateStatoEstrazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                  		\n"
        + "   IUF_D_NUMERO_LOTTO                 			\n"
        + " SET                                     		\n"
        + "   ID_STATO_ESTRAZIONE = :ID_STATO_ESTRAZIONE 	\n"
        + " WHERE                                 			\n"
        + "   ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO  			\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_STATO_ESTRAZIONE", idStatoEstrazione,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idNumeroLotto", idNumeroLotto),
              new LogParameter("idStatoEstrazione", idStatoEstrazione)
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

  public List<RigaFiltroDTO> getElencoTipoEstrazioniCaricabili()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getFiltroElencoTipoEstrazioniCampione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                            \n"
        + "   A.ID_TIPO_ESTRAZIONE AS ID,     \n"
        + "   A.DESCRIZIONE AS LABEL          \n"
        + " FROM                              \n"
        + "   IUF_D_TIPO_ESTRAZIONE A         \n"
        + " WHERE                             \n"
        + "	A.CODICE IS NOT NULL 			\n"
        + "	AND A.FLAG_ATTIVO = 'S' 		\n"
        + " ORDER BY A.DESCRIZIONE ASC        \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      return queryForList(QUERY, mapParameterSource, RigaFiltroDTO.class);
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

  public Boolean esisteEstrazioneCaricata(long idTipoEstrazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::esisteEstrazioneCaricata]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                           \n"
        + "   A.ID_NUMERO_LOTTO                              \n"
        + " FROM                                             \n"
        + "   IUF_D_NUMERO_LOTTO A                           \n"
        + " WHERE                                            \n"
        + "   A.ID_STATO_ESTRAZIONE IN (1, 2)                \n"
        + "   AND A.ID_TIPO_ESTRAZIONE = :ID_TIPO_ESTRAZIONE \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_TIPO_ESTRAZIONE", idTipoEstrazione,
        Types.NUMERIC);
    try
    {
      Boolean res = namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return Boolean.TRUE;
              }

              return Boolean.FALSE;
            }
          });
      return res;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idTipoEstrazione", idTipoEstrazione)
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

  public long insertNumeroLotto(long idTipoEstrazione, String utenteDescrizione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertNumeroLotto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_D_NUMERO_LOTTO 	\n"
        + " (ID_NUMERO_LOTTO, 				\n"
        + "	 DATA_ELABORAZIONE, 			\n"
        + "	 DESCRIZIONE, 					\n"
        + "	 ID_TIPO_ESTRAZIONE, 			\n"
        + "  ID_STATO_ESTRAZIONE			\n"
        + ") 								\n"
        + "VALUES 							\n"
        + " (:ID_NUMERO_LOTTO, 				\n"
        + "	 SYSDATE,			 			\n"
        + "	 :DESCRIZIONE, 					\n"
        + "	 :ID_TIPO_ESTRAZIONE, 			\n"
        + "  1)								\n"; // ID_STATO_ESTRAZIONE = 1 (stato CARICATA)

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long idNumeroLotto = 0;
    try
    {
      idNumeroLotto = getNextSequenceValue("SEQ_IUF_D_NUMERO_LOTTO");
      mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_ESTRAZIONE", idTipoEstrazione,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", utenteDescrizione,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idTipoEstrazione", idTipoEstrazione),
              new LogParameter("utenteDescrizione", utenteDescrizione)
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
    return idNumeroLotto;
  }

  public MainControlloDTO callCaricaDP(long idNumeroLotto,
      long idTipoEstrazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "callCaricaDP";
    StringBuffer CALL = new StringBuffer(
        "PCK_IUF_ESTRAZ_CAMPIONE_DP.CaricaDP");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("PIDNUMEROLOTTO", idNumeroLotto, Types.NUMERIC);
      parameterSource.addValue("PIDTIPOESTRAZIONE", idTipoEstrazione,
          Types.NUMERIC);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_IUF_ESTRAZ_CAMPIONE_DP")
              .withProcedureName("CaricaDP")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlParameter("PIDNUMEROLOTTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PIDTIPOESTRAZIONE", java.sql.Types.NUMERIC));

      call.addDeclaredParameter(
          new SqlOutParameter("PRISULTATO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("PMESSAGGIO", java.sql.Types.VARCHAR));

      Map<String, Object> results = call.execute(parameterSource);

      MainControlloDTO dto = new MainControlloDTO();
      dto.setRisultato(((BigDecimal) results.get("PRISULTATO")).intValue());
      dto.setMessaggio(safeMessaggioPLSQL((String) results.get("PMESSAGGIO")));
      return dto;

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          CALL.toString());
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
      }
    }
  }

  public MainControlloDTO callEstraiDP(long idNumeroLotto,
      long idTipoEstrazione, String chkRegistra)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "callEstraiDP";
    StringBuffer CALL = new StringBuffer(
        "PCK_IUF_ESTRAZ_CAMPIONE_DP.EstraiDP");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("PIDNUMEROLOTTO", idNumeroLotto, Types.NUMERIC);
      parameterSource.addValue("PIDTIPOESTRAZIONE", idTipoEstrazione,
          Types.NUMERIC);
      parameterSource.addValue("PSALTAESTRAZIONE",
          (chkRegistra.equals("N") ? null : "S"), Types.VARCHAR);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_IUF_ESTRAZ_CAMPIONE_DP")
              .withProcedureName("EstraiDP")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlParameter("PIDNUMEROLOTTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PIDTIPOESTRAZIONE", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PSALTAESTRAZIONE", java.sql.Types.VARCHAR));

      call.addDeclaredParameter(
          new SqlOutParameter("PRISULTATO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("PMESSAGGIO", java.sql.Types.VARCHAR));

      Map<String, Object> results = call.execute(parameterSource);

      MainControlloDTO dto = new MainControlloDTO();
      dto.setRisultato(((BigDecimal) results.get("PRISULTATO")).intValue());
      dto.setMessaggio(safeMessaggioPLSQL((String) results.get("PMESSAGGIO")));
      return dto;

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          CALL.toString());
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
      }
    }
  }

  public NumeroLottoDTO getNumeroLottoDto(long idNumeroLotto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getNumeroLottoDto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                           \n"
        + "   A.ID_NUMERO_LOTTO,                             \n"
        + "   A.DATA_ELABORAZIONE,                           \n"
        + "   A.DESCRIZIONE,                                 \n"
        + "   A.ID_TIPO_ESTRAZIONE,                          \n"
        + "   A.ID_STATO_ESTRAZIONE ,                        \n"
        + "   A.NUMERO_ESTRAZIONE ,                          \n"
        + "   A.IMPORTO_RICHIESTO_COMPLESSIVO,               \n"
        + "   A.IMPORTO_RICHIESTO_ESTRAZIONE ,               \n"
        + "   DTE.DESCRIZIONE AS DESCR_TIPO_ESTRAZIONE,  	   \n"
        + "   DSE.DESCRIZIONE AS DESCR_STATO_ESTRAZIONE, 	   \n"
        + "   A.DATA_CUTOFF,                           	   \n"
        + "   (SELECT E.MOTIVAZIONE_ANNULLAMENTO  FROM IUF_T_ESTRAZIONE_CAMPIONE E WHERE E.ID_NUMERO_LOTTO = A.ID_NUMERO_LOTTO)  AS MOTIVAZIONE                          	   \n"
        + " FROM                                             \n"
        + "   IUF_D_NUMERO_LOTTO A,                          \n"
        + "   IUF_D_STATO_ESTRAZIONE DSE,              	   \n"
        + "   IUF_D_TIPO_ESTRAZIONE DTE                       \n"
        + " WHERE                                                 \n"
        + "  A.ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO 		        \n"
        + "   AND DSE.ID_STATO_ESTRAZIONE =A.ID_STATO_ESTRAZIONE  \n"
        + "   AND DTE.ID_TIPO_ESTRAZIONE =A.ID_TIPO_ESTRAZIONE    \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
        Types.NUMERIC);
    try
    {
      return queryForObject(QUERY, mapParameterSource, NumeroLottoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idNumeroLotto", idNumeroLotto)
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

  public void deleteTAnalisiRischioImp(long idNumeroLotto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteTAnalisiRischioImp]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = " DELETE FROM IUF_T_ANALISI_RISCHIO_IMP                                     \n"
        + "        WHERE ID_ANALISI_RISCHIO IN                                     \n"
        + "        (                                                              \n"
        + "             SELECT                                                    \n"
        + "              A.ID_ANALISI_RISCHIO                                      \n"
        + "             FROM                                                      \n"
        + "              IUF_T_ANALISI_RISCHIO A                                  \n"
        + "             WHERE                                                     \n"
        + "              A.ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO                      \n"
        + "         )                                                             \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idNumeroLotto", idNumeroLotto)
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

  public List<FlagEstrazioneDTO> getElencoFlagEstrazioni()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoFlagEstrazioni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                         \n"
        + "   ROWNUM AS ID_ESTRAZIONE,                     \n"
        + "   A.DESCRIZIONE,                               \n"
        + "   A.FLAG_CONTROLLO_IN_LOCO,                    \n"
        + "   A.FLAG_ESTRATTA                              \n"
        + " FROM                                           \n"
        + "   IUF_D_FLAG_ESTRATTA A                        \n"
        + " ORDER BY                                       \n"
        + "   A.FLAG_CONTROLLO_IN_LOCO, A.DESCRIZIONE DESC \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      return queryForList(QUERY, mapParameterSource, FlagEstrazioneDTO.class);
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

  public List<DettaglioImportoDTO> getElencoDettagliImportiMisure(
      long idNumeroLotto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoDettagliImportiMisure]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                                                                              \n"
        + "   MISURA.CODICE_LIVELLO CODICE,                                                                                                                     \n"
        + "   SUM(B.IMPORTO_RICHIESTO) AS IMPORTO_RICHIESTO,                                                                                                                              \n"
        + "   DECODE(B.TIPO_IMPORTO, 'C', 'Imp. richiesto complessivo', 'A', 'Imp. estrazione Attuale', 'P', 'Imp. estrazioni Pregresse') AS DESCR_TIPO_IMPORTO \n"
        + " FROM                                                                                                                                                \n"
        + "   IUF_D_NUMERO_LOTTO A,                                                                                                                             \n"
        + "   IUF_T_IMPORTI_ESTRAZ_DP B,                                                                                                                        \n"
        + "   IUF_D_LIVELLO W1,                                                                                                                                 \n"
        + "   IUF_R_LIVELLO_PADRE W2,                                                                                                                           \n"
        + "   IUF_R_LIVELLO_PADRE W3,                                                                                                                           \n"
        + "   IUF_D_LIVELLO MISURA,                                                                                                                             \n"
        + "   IUF_D_LIVELLO AZIONE                                                                                                                              \n"
        + " WHERE                                                                                                                                               \n"
        + "   A.ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO                                                                                                              \n"
        + "   AND B.ID_NUMERO_LOTTO = A.ID_NUMERO_LOTTO                                                                                                         \n"
        + "   AND W1.ID_LIVELLO = B.ID_LIVELLO                                                                                                                  \n"
        + "   AND W1.ID_LIVELLO = W2.ID_LIVELLO                                                                                                                 \n"
        + "   AND W3.ID_LIVELLO = AZIONE.ID_LIVELLO                                                                                                             \n"
        + "   AND W2.ID_LIVELLO_PADRE = W3.ID_LIVELLO                                                                                                           \n"
        + "   AND W3.ID_LIVELLO_PADRE = MISURA.ID_LIVELLO                                                                                                       \n"
        + "   AND B.TIPO_IMPORTO = 'C'                                                                                                                          \n"
        + " GROUP BY                                                                   \n"
        + "    MISURA.CODICE_LIVELLO,                                                          \n"
        + "    B.TIPO_IMPORTO                                                         \n"
        + " ORDER BY                                                                                                                                            \n"
        + "   MISURA.CODICE_LIVELLO                                                                                                                                     \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DettaglioImportoDTO>>()
          {

            @Override
            public List<DettaglioImportoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DettaglioImportoDTO> elenco = null;
              DettaglioImportoDTO dettaglio = null;
              int c = 0;

              while (rs.next())
              {
                if (c == 0)
                {
                  elenco = new ArrayList<>();
                  c++;
                }
                dettaglio = new DettaglioImportoDTO();
                dettaglio.setCodiceLivello(rs.getString("CODICE"));
                dettaglio
                    .setDecodificaImporto(rs.getString("DESCR_TIPO_IMPORTO"));
                dettaglio.setImporto(rs.getBigDecimal("IMPORTO_RICHIESTO"));
                elenco.add(dettaglio);
              }
              return elenco;
            }
          });
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

  public List<DettaglioImportoDTO> getImportiAttualiPrecedentiMisure(
      long idNumeroLotto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getImportiAttualiPrecedentiMisure]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                     \n"
        + "    MISURA.CODICE AS CODICE_LIVELLO,                                        \n"
        + "    DTE.DESCRIZIONE AS DESCR_TIPO_ESTRAZIONE,                               \n"
        + "    DSE.DESCRIZIONE AS DESCR_STATO_ESTRAZIONE,                              \n"
        + "    SUM(B.IMPORTO_RICHIESTO) AS IMPORTO_RICHIESTO,                          \n"
        + "    SUM(B.IMPORTO_RICHIESTO_PARTE_CASUAL) AS IMPORTO_RICHIESTO_PARTE_CASUAL \n"
        + " FROM                                                                       \n"
        + "   IUF_D_NUMERO_LOTTO A,                                                    \n"
        + "   IUF_T_IMPORTI_ESTRAZ_DP B,                                               \n"
        + "   IUF_D_STATO_ESTRAZIONE DSE,                                              \n"
        + "   IUF_D_TIPO_ESTRAZIONE DTE,                                               \n"
        + "   IUF_D_LIVELLO W1,                                                        \n"
        + "   IUF_R_LIVELLO_PADRE W2,                                                  \n"
        + "   IUF_R_LIVELLO_PADRE W3,                                                  \n"
        + "   IUF_D_LIVELLO MISURA,                                                    \n"
        + "   IUF_D_LIVELLO AZIONE                                                     \n"
        + " WHERE                                                                      \n"
        + "   W1.ID_LIVELLO = W2.ID_LIVELLO                                            \n"
        + "   AND A.ID_NUMERO_LOTTO = B.ID_NUMERO_LOTTO                                \n"
        + "   AND A.ID_NUMERO_LOTTO = :ID_NUMERO_LOTTO                                 \n"
        + "   AND W1.ID_LIVELLO = B.ID_LIVELLO                                         \n"
        + "   AND W3.ID_LIVELLO = AZIONE.ID_LIVELLO                                    \n"
        + "   AND W2.ID_LIVELLO_PADRE = W3.ID_LIVELLO                                  \n"
        + "   AND W3.ID_LIVELLO_PADRE = MISURA.ID_LIVELLO                              \n"
        + "   AND DSE.ID_STATO_ESTRAZIONE =A.ID_STATO_ESTRAZIONE                       \n"
        + "   AND DTE.ID_TIPO_ESTRAZIONE =A.ID_TIPO_ESTRAZIONE                         \n"
        + "   AND B.TIPO_IMPORTO IN ('A','P')                                          \n"
        + " GROUP BY                                                                   \n"
        + "    MISURA.CODICE,                                                          \n"
        + "    DTE.DESCRIZIONE,                                                        \n"
        + "    DSE.DESCRIZIONE                                                         \n"
        + " ORDER BY MISURA.CODICE ASC                                                 \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_NUMERO_LOTTO", idNumeroLotto,
        Types.NUMERIC);
    try
    {
      return queryForList(QUERY, mapParameterSource, DettaglioImportoDTO.class);
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

  public List<FlagEstrazioneDTO> getElencoFlagEstrazioniExPost()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoFlagEstrazioniExPost]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                         \n"
        + "  ROWNUM AS ID_ESTRAZIONE,                                                      \n"
        + "  A.DESCRIZIONE,                                                                \n"
        + "  A.FLAG_CONTROLLO_IN_LOCO,                                                     \n"
        + "  A.FLAG_ESTRATTA                                                               \n"
        + " FROM                                                                           \n"
        + "  IUF_D_FLAG_ESTRATTA A                                                         \n"
        + " WHERE                                                                          \n"
        + "   EXISTS (SELECT A1.ID_PROCEDIMENTO from                                       \n"
        + "   IUF_T_PROCEDIMENTO_ESTR_EXPO A1 where A1.FLAG_ESTRATTA = A.FLAG_ESTRATTA ) \n"
        + " ORDER BY                                                                       \n"
        + "  A.FLAG_CONTROLLO_IN_LOCO, A.DESCRIZIONE DESC                                  \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      return queryForList(QUERY, mapParameterSource, FlagEstrazioneDTO.class);
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

}
