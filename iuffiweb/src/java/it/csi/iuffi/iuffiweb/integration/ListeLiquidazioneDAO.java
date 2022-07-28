package it.csi.iuffi.iuffiweb.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.ImportiRipartitiListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.StampaListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.DatiCreazioneListaDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.LivelliBandoDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoImportiApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoPraticheApprovazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONBandiNuovaListaDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RisorseImportiOperazioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.DumpUtils;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.smrcomms.siapcommws.dto.smrcomm.EsitoDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellEsitoVO;

public class ListeLiquidazioneDAO extends BaseDAO
{
  protected static final String THIS_CLASS = ListeLiquidazioneDAO.class
      .getSimpleName();

  public ListeLiquidazioneDAO()
  {
  }

  public List<RigaJSONElencoListaLiquidazioneDTO> getListeLiquidazione(
      Long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getListeLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                                                                          \n"
        + "   IMPORTI AS                                                                                                                  \n"
        + "   (                                                                                                                           \n"
        + "     SELECT                                                                                                                    \n"
        + "       LLIL.ID_LISTA_LIQUIDAZIONE,                                                                                             \n"
        + "       COUNT(*) NUM_PAGAMENTI,                                                                                                 \n"
        + "       SUM(IL.IMPORTO_LIQUIDATO) AS IMPORTO                                                                                    \n"
        + "     FROM                                                                                                                      \n"
        + "       IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL,                                                                                      \n"
        + "       IUF_T_IMPORTI_LIQUIDATI IL                                                                                              \n"
        + "     WHERE                                                                                                                     \n"
        + "       LLIL.ID_IMPORTI_LIQUIDATI = IL.ID_IMPORTI_LIQUIDATI                                                                     \n"
        + "     GROUP BY                                                                                                                  \n"
        + "       LLIL.ID_LISTA_LIQUIDAZIONE                                                                                              \n"
        + "   )                                                                                                                           \n"
        + " SELECT                                                                                                                        \n"
        + "   LL.ID_LISTA_LIQUIDAZIONE,                                                                                                   \n"
        + "   LL.NUMERO_LISTA,                                                                                                            \n"
        + "   LL.NOTE,                                                                                                                    \n"
        + "   B.ID_BANDO,                                                                                                                 \n"
        + "   B.DENOMINAZIONE AS DENOMINAZIONE_BANDO,                                                                                     \n"
        + "   B.ANNO_CAMPAGNA AS ANNO_CAMPAGNA_BANDO,                                                                                     \n"
        + "   LL.DATA_CREAZIONE,                                                                                                          \n"
        + "   LL.FLAG_STATO_LISTA,                                                                                                        \n"
        + "   DECODE(LL.FLAG_STATO_LISTA,'A','Approvata','B','Bozza','D','Da approvare', 'T', 'Trasmessa a OPR') AS DESC_STATO_LISTA,                                                \n"
        + "   IMP.NUM_PAGAMENTI,                                                                                                          \n"
        + "   IMP.IMPORTO,                                                                                                                \n"
        + "   TI.DESCRIZIONE AS DESC_TIPO_IMPORTO,                                                                                        \n"
        + "   LL.DATA_APPROVAZIONE,                                                                                                       \n"
        + "   DECODE(T.ID_TECNICO, NULL, NULL, T.COGNOME) AS COGNOME_TECNICO_LIQUIDATORE,                                   \n"
        + "   DECODE(T.ID_TECNICO, NULL, NULL, T.NOME) AS NOME_TECNICO_LIQUIDATORE,                                   \n"
        + "   DECODE(T.ID_TECNICO, NULL, NULL, T.CODICE_FISCALE) AS COD_FISC_TECNICO_LIQUIDATORE,                                   \n"
        + "   LL.FLAG_INVIO_SIGOP,                                                                                                        \n"
        + "   LL.DATA_INVIO_SIGOP,                                                                                                        \n"
        + "   LL.EXT_ID_AMM_COMPETENZA,                                                                                                   \n"
        + "   AC.DESCRIZIONE AS ORGANISMO_DELEGATO,                                                                                       \n"
        + "   AC.DENOMINAZIONE_1 AS DESC_ORGANISMO_DELEGATO,                                                                              \n"
        + "   DECODE(UL.ID_UTENTE_LOGIN, NULL, NULL, UL.COGNOME_UTENTE_LOGIN || ' ' || UL.NOME_UTENTE_LOGIN) AS DESC_UTENTE_AGGIORNAMENTO,\n"
        + "   FLL.ID_STATO_STAMPA,                                                                                                        \n"
        + "   LL.EXT_ID_TECNICO_LIQUIDATORE,                                                                                              \n"
        + "   NVL(FLL.NUMERO_PROTOCOLLO, FLL.NUMERO_PROTOCOLLO_EMERGENZA) AS NUMERO_PROTOCOLLO,                                              \n"
        + "   NVL(FLL.DATA_PROTOCOLLO, FLL.DATA_PROTOCOLLO_EMERGENZA) AS DATA_PROTOCOLLO,                                              \n"
        + "   LL.ID_TIPO_IMPORTO                                                                                                          \n"
        + " FROM                                                                                                                          \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL                                                                                                 \n"
        + "   LEFT JOIN DB_TECNICO T ON LL.EXT_ID_TECNICO_LIQUIDATORE = T.ID_TECNICO                                                      \n"
        + "   LEFT JOIN PAPUA_V_UTENTE_LOGIN UL ON LL.EXT_ID_UTENTE_AGGIORNAMENTO = UL.ID_UTENTE_LOGIN,                                   \n"
        + "   IUF_D_BANDO B,                                                                                                              \n"
        + "   IMPORTI IMP,                                                                                                                \n"
        + "   IUF_D_TIPO_IMPORTO TI,                                                                                                      \n"
        + "   SMRCOMUNE_V_AMM_COMPETENZA AC,                                                                                              \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION FLL                                                                                           \n"
        + " WHERE                                                                                                                         \n"
        + "   LL.ID_BANDO                  = B.ID_BANDO                                                                                   \n"
        + "   AND LL.ID_LISTA_LIQUIDAZIONE = IMP.ID_LISTA_LIQUIDAZIONE                                                                    \n"
        + "   AND LL.ID_TIPO_IMPORTO = TI.ID_TIPO_IMPORTO                                                                                 \n"
        + "   AND LL.EXT_ID_AMM_COMPETENZA = AC.ID_AMM_COMPETENZA                                                                         \n"
        + "   AND LL.ID_LISTA_LIQUIDAZIONE = FLL.ID_LISTA_LIQUIDAZIONE                                                                    \n"
        + (idListaLiquidazione != null
            ? "   AND LL.ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE                                                                     \n"
            : "")
        + " ORDER BY                                                                                                                       \n"
        + " B.DENOMINAZIONE, LL.NUMERO_LISTA                                                                                               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    try
    {
      return queryForList(QUERY, mapParameterSource,
          RigaJSONElencoListaLiquidazioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null, (LogVariable[]) null, QUERY,
          mapParameterSource);
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

  public List<Map<String, Object>> getAmministrazioniCompetenzaListe()
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getAmministrazioniCompetenzaListe";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT DISTINCT                                   \n"
        + "   AC.DESCRIZIONE  AS \"label\",                     \n"
        + "   AC.ID_AMM_COMPETENZA AS \"id\"                    \n"
        + " FROM                                              \n"
        + "   SMRCOMUNE_V_AMM_COMPETENZA AC,                  \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL                     \n"
        + " WHERE                                             \n"
        + "   LL.EXT_ID_AMM_COMPETENZA = AC.ID_AMM_COMPETENZA \n"
        + " ORDER BY                                          \n"
        + "   AC.DESCRIZIONE ASC                              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return this.namedParameterJdbcTemplate.queryForList(QUERY,
          mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null, (LogVariable[]) null, QUERY,
          mapParameterSource);
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

  public List<RigaJSONBandiNuovaListaDTO> getBandiProntiPerListeLiquidazione(
      List<Long> lIdAmmCompetenza)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getBandiProntiPerListeLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    StringBuilder sb = new StringBuilder();
    for (Long idAmmCompetenza : lIdAmmCompetenza)
    {
      if (sb.length() > 0)
      {
        sb.append(",");
      }
      sb.append(idAmmCompetenza);
    }
    final String QUERY = " SELECT                                                       \n"
        + "   DENOMINAZIONE_BANDO,                                       \n"
        + "   REFERENTE_BANDO,                                           \n"
        + "   ANNO_CAMPAGNA,                                             \n"
        + "   DATA_INIZIO_BANDO,                                         \n"
        + "   DESC_TIPO_BANDO,                                           \n"
        + "   ID_BANDO,                                                  \n"
        // + " ORDINAMENTO, \n"
        + "   listagg (CODICE_LIVELLO, ', ') WITHIN GROUP (              \n"
        + " ORDER BY                                                     \n"
        + "   CODICE_LIVELLO) CODICI_LIVELLO                             \n"
        + " FROM                                                         \n"
        + "   (                                                          \n"
        + "     SELECT                                                   \n"
        + "       B.DENOMINAZIONE AS DENOMINAZIONE_BANDO,                \n"
        + "       B.REFERENTE_BANDO,                                     \n"
        + "       B.ANNO_CAMPAGNA,                                       \n"
        + "       B.DATA_INIZIO  AS DATA_INIZIO_BANDO,                   \n"
        + "       TL.DESCRIZIONE AS DESC_TIPO_BANDO,                     \n"
        + "       B.ID_BANDO,                                            \n"
        + "       L.CODICE_LIVELLO,                                      \n"
        + "       MIN(L.ORDINAMENTO) AS ORDINAMENTO                      \n"
        + "     FROM                                                     \n"
        + "       IUF_D_BANDO B,                                         \n"
        + "       IUF_D_TIPO_LIVELLO TL,                                 \n"
        + "       IUF_R_LIVELLO_BANDO LB,                                \n"
        + "       IUF_D_LIVELLO L,                                       \n"
        + "       IUF_T_IMPORTI_LIQUIDATI IL,                            \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                         \n"
        + "       IUF_T_PROCEDIMENTO P,                                  \n"
        + "       IUF_D_ESITO E,                                         \n"
        + "       IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL                      \n"

        + "     WHERE                                                    \n"
        + "       B.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO                 \n"
        // solo per le misure ad investimento TEMPORANEO INIZIO
        // + " AND TL.CODICE = 'I' \n"
        // solo per le misure ad investimento TEMPORANEO FINE
        + "       AND B.ID_BANDO    = LB.ID_BANDO                        \n"

        + "             AND B.ID_BANDO = P.ID_BANDO                                 \n"
        + "             AND P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                  \n"
        + "             AND PO.ID_PROCEDIMENTO_OGGETTO = IL.ID_PROCEDIMENTO_OGGETTO \n"
        + "             AND PO.ID_ESITO = E.ID_ESITO                                \n"
        + "             AND E.CODICE LIKE 'APP-%'                                   \n"
        + "             AND IL.ID_IMPORTI_LIQUIDATI = LLIL.ID_IMPORTI_LIQUIDATI(+)  \n"
        + "             AND LLIL.FLAG_ESITO_LIQUIDAZIONE(+) <> 'R'                  \n"

        // + " AND B.DATA_FINE < SYSDATE \n"
        + "       AND LB.ID_LIVELLO = L.ID_LIVELLO                       \n"
        + "       AND EXISTS                                             \n"
        + "       (                                                      \n"
        + "         SELECT                                               \n"
        + "           *                                                  \n"
        + "         FROM                                                 \n"
        + "           IUF_T_PROCEDIMENTO P,                              \n"
        + "           IUF_T_PROCEDIM_AMMINISTRAZIO PA                  \n"
        + "         WHERE                                                \n"
        + "           B.ID_BANDO = P.ID_BANDO                            \n"
        + "           AND P.ID_STATO_OGGETTO BETWEEN 10 AND 90           \n"
        + "           AND P.ID_PROCEDIMENTO         = PA.ID_PROCEDIMENTO \n"
        + "           AND PA.DATA_FINE             IS NULL               \n"
        + "           AND PA.EXT_ID_AMM_COMPETENZA IN (" + sb.toString() + ")\n"
        + "       )                                                      \n"
        + "     GROUP BY                                                 \n"
        + "       B.DENOMINAZIONE,                                       \n"
        + "       B.REFERENTE_BANDO,                                     \n"
        + "       B.ANNO_CAMPAGNA,                                       \n"
        + "       B.DATA_INIZIO,                                         \n"
        + "       TL.DESCRIZIONE,                                        \n"
        + "       B.ID_BANDO,                                            \n"
        + "       L.CODICE_LIVELLO                                       \n"
        + "       HAVING SUM(DECODE(LLIL.ID_IMPORTI_LIQUIDATI, NULL, 1, 0))>0 "
        + "   )                                                          \n"
        + " GROUP BY                                                     \n"
        + "   DENOMINAZIONE_BANDO,                                       \n"
        + "   REFERENTE_BANDO,                                           \n"
        + "   ANNO_CAMPAGNA,                                             \n"
        + "   DATA_INIZIO_BANDO,                                         \n"
        + "   ID_BANDO,                                                  \n"
        + "   DESC_TIPO_BANDO/*,*/                                           \n"
        // + " ORDINAMENTO \n"
        + " ORDER BY                                                     \n"
        // + " ORDINAMENTO, \n"
        + "   DATA_INIZIO_BANDO ASC                                      \n";
    try
    {
      return queryForList(QUERY, mapParameterSource,
          RigaJSONBandiNuovaListaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null, (LogVariable[]) null, QUERY,
          mapParameterSource);
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

  public LivelliBandoDTO getLivelliBando(long idBando)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getLivelliBando";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                             \n"
        + "   B.DENOMINAZIONE,                 \n"
        + "   L.ID_LIVELLO,                    \n"
        + "   L.CODICE AS L_CODICE,            \n"
        + "   L.DESCRIZIONE AS L_DESCRIZIONE   \n"
        + " FROM                               \n"
        + "   IUF_D_BANDO B,                   \n"
        + "   IUF_R_LIVELLO_BANDO LB,          \n"
        + "   IUF_D_LIVELLO L                  \n"
        + " WHERE                              \n"
        + "   B.ID_BANDO = :ID_BANDO           \n"
        + "   AND B.ID_BANDO = LB.ID_BANDO     \n"
        // a prescindere escludo i bandi non chiusi per motivi di sicurezza
        // + " AND B.DATA_FINE < SYSDATE \n"
        + "   AND LB.ID_LIVELLO = L.ID_LIVELLO \n"
        + "   AND LB.ID_LIVELLO = L.ID_LIVELLO \n"
        + " ORDER BY                           \n"
        + "   L.ORDINAMENTO                    \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<LivelliBandoDTO>()
          {
            @Override
            public LivelliBandoDTO extractData(ResultSet rs) throws SQLException
            {
              LivelliBandoDTO result = null;
              if (rs.next())
              {
                result = new LivelliBandoDTO();
                result.setDenominazioneBando(rs.getString("DENOMINAZIONE"));
                final ArrayList<DecodificaDTO<Integer>> livelli = new ArrayList<DecodificaDTO<Integer>>();
                result.setLivelli(livelli);
                do
                {
                  livelli
                      .add(new DecodificaDTO<Integer>(rs.getInt("ID_LIVELLO"),
                          rs.getString("L_CODICE"),
                          rs.getString("L_DESCRIZIONE")));
                }
                while (rs.next());
              }
              return result;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          // new LogParameter("idVariabile", idVariabile),
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

  public List<DecodificaDTO<Long>> findAmministrazioniInProcedimentiBando(
      long idBando, List<Long> lIdAmmCompetenza)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAmministrazioniInProcedimentiBando";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    StringBuilder sb = new StringBuilder();
    for (Long idAmmCompetenza : lIdAmmCompetenza)
    {
      if (sb.length() > 0)
      {
        sb.append(",");
      }
      sb.append(idAmmCompetenza);
    }

    String QUERY = " SELECT                                                    \n"
        + "   AC.ID_AMM_COMPETENZA      AS ID,                        \n"
        + "   AC.CODICE_AMMINISTRAZIONE AS CODICE,                    \n"
        + "   AC.DESCRIZIONE                                          \n"
        + " FROM                                                      \n"
        + "   SMRCOMUNE_V_AMM_COMPETENZA AC                           \n"
        + " WHERE                                                     \n"
        + "   AC.ID_AMM_COMPETENZA IN (" + sb.toString() + ")             \n"
        + "   AND EXISTS                                              \n"
        + "   (                                                       \n"
        + "     SELECT                                                \n"
        + "       *                                                   \n"
        + "     FROM                                                  \n"
        + "       IUF_T_PROCEDIMENTO P,                               \n"
        + "       IUF_T_PROCEDIM_AMMINISTRAZIO PA                   \n"
        + "     WHERE                                                 \n"
        + "       P.ID_BANDO = :ID_BANDO                              \n"
        + "       AND P.ID_STATO_OGGETTO BETWEEN 10 AND 90            \n"
        + "       AND P.ID_PROCEDIMENTO        = PA.ID_PROCEDIMENTO   \n"
        + "       AND PA.DATA_FINE            IS NULL                 \n"
        + "       AND PA.EXT_ID_AMM_COMPETENZA = AC.ID_AMM_COMPETENZA \n"
        + "   )                                                       \n"
        + " ORDER BY                                                  \n"
        + "   AC.DESCRIZIONE ASC                                      \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBando", idBando),
              new LogParameter("lIdAmmCompetenza", lIdAmmCompetenza),
          },
          null, QUERY, mapParameterSource);
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

  public List<DecodificaDTO<Long>> getTecniciLiquidatori(long idAmmCompetenza, int idProcedimentoAgricoltura)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAmministrazioniInProcedimentiBando";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT DISTINCT                                                       \n"
        + "   T.ID_TECNICO AS ID,                                                          \n"
        + "	  T.COGNOME,                                                          		   \n"
        + "	  T.NOME ,                                                                     \n"
        + "   T.CODICE_FISCALE AS CODICE,                                                  \n"
        + "   T.COGNOME || ' ' || T.NOME || ' (' || T.CODICE_FISCALE || ')' AS DESCRIZIONE \n"
        + " FROM                                                                           \n"
        + "   SMRCOMUNE_V_TECNICO T                                                        \n"
        + " WHERE                                                                          \n"
        + "   T.ID_AMM_COMPETENZA       = :ID_AMM_COMPETENZA                               \n"
        + "   AND T.EXT_ID_PROCEDIMENTO = :EXT_ID_PROCEDIMENTO                             \n"
        + "   AND T.ID_TIPO_RUOLO       = :ID_TIPO_RUOLO                                   \n"
        + "   ORDER BY                                                                     \n"
        + "     T.COGNOME, T.NOME                                                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza,
        Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_PROCEDIMENTO",
        idProcedimentoAgricoltura, Types.NUMERIC);
    mapParameterSource.addValue("ID_TIPO_RUOLO",
        IuffiConstants.GENERIC.ID_TIPO_RUOLO_TECNICO_LIQUIDATORE,
        Types.NUMERIC);
    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idAmmCompetenza", idAmmCompetenza)
          },
          null, QUERY, mapParameterSource);
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

  public List<RisorseImportiOperazioneDTO> getDatiListaDaCreare(long idBando,
      long idAmmCompetenza, long idTipoImporto, List<Long> idsPODaEscludere)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDatiListaDaCreare";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT                                                         \n"
        + "   RLB.ID_RISORSE_LIVELLO_BANDO,                                         \n"
        + "   L.CODICE,                                                             \n"
        + "   RLB.RISORSE_ATTIVATE - 												\n"
        + "		NVL((																\n"
        + "			SELECT 															\n"
        + "				SUM(NVL(E.IMPORTO_ECONOMIA,0)) 								\n"
        + "			FROM 															\n"
        + "				IUF_T_ECONOMIA E 											\n"
        + "			WHERE 															\n"
        + "				E.ID_RISORSE_LIVELLO_BANDO = RLB.ID_RISORSE_LIVELLO_BANDO	\n"
        + "		),0) RISORSE_ATTIVATE,                                				\n"

        + "   RLB.RAGGRUPPAMENTO,                                                  \n"

        + "   L.ORDINAMENTO,                                                       \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       NVL(SUM(NVL(IL.IMPORTO_LIQUIDATO,0)),0)                          \n"
        + "     FROM                                                               \n"
        + "       IUF_T_IMPORTI_LIQUIDATI IL,                                      \n"
        + "       IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL,                               \n"
        + "       IUF_R_RISOR_LIV_BAND_IMP_LIQ RLBIL                             \n"
        + "     WHERE                                                              \n"
        + "       RLBIL.ID_RISORSE_LIVELLO_BANDO    = RLB.ID_RISORSE_LIVELLO_BANDO \n"
        + "       AND RLBIL.ID_IMPORTI_LIQUIDATI    = IL.ID_IMPORTI_LIQUIDATI      \n"
        + "       AND LLIL.ID_IMPORTI_LIQUIDATI     = IL.ID_IMPORTI_LIQUIDATI      \n"
        + "       AND LLIL.FLAG_ESITO_LIQUIDAZIONE <> 'R'                          \n"
        + "   ) AS IMPORTO_LIQUIDATO,                                              \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       COUNT(*)                                                         \n"
        + "     FROM                                                               \n"
        + "       IUF_T_IMPORTI_LIQUIDATI IL,                                      \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                   \n"
        + "       IUF_D_ESITO E,                                                   \n"
        + "       IUF_T_PROCEDIMENTO P,                                            \n"
        + "       IUF_T_PROCEDIM_AMMINISTRAZIO PA                                \n"
        + "     WHERE                                                              \n"
        + "       IL.ID_LIVELLO = RLB.ID_LIVELLO                                   \n"

        + "       AND ( IL.RAGGRUPPAMENTO = RLB.RAGGRUPPAMENTO      			   \n"
        + "					  OR (IL.RAGGRUPPAMENTO IS NULL          			   \n"
        + "							AND RLB.RAGGRUPPAMENTO IS NULL )    )          \n"

        + "       AND IL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO      \n"
        + "       AND PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                       \n";
    if (idsPODaEscludere != null && !idsPODaEscludere.isEmpty())
      QUERY += getNotInCondition("PO.ID_PROCEDIMENTO_OGGETTO",
          idsPODaEscludere);

    QUERY += "       AND P.ID_PROCEDIMENTO = PA.ID_PROCEDIMENTO                       \n"
        + "       AND PA.DATA_FINE IS NULL                                         \n"
        + "       AND PA.EXT_ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA                \n"
        + "       AND P.ID_BANDO = :ID_BANDO                                       \n"
        + "       AND NOT EXISTS                                                   \n"
        + "       (                                                                \n"
        + "         SELECT                                                         \n"
        + "           *                                                            \n"
        + "         FROM                                                           \n"
        + "           IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL                            \n"
        + "         WHERE                                                          \n"
        + "           LLIL.ID_IMPORTI_LIQUIDATI = IL.ID_IMPORTI_LIQUIDATI          \n"
        + "       )                                                                \n"
        + "       AND PO.ID_ESITO = E.ID_ESITO                                     \n"
        + "       AND E.CODICE LIKE :ESITO_PRATICHE_LIQUIDABILI                    \n"
        + "   ) AS NUM_DA_LIQUIDARE,                                               \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       NVL(SUM(NVL(IL.IMPORTO_LIQUIDATO,0)),0)                          \n"
        + "     FROM                                                               \n"
        + "       IUF_T_IMPORTI_LIQUIDATI IL,                                      \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                   \n"
        + "       IUF_T_PROCEDIMENTO P,                                            \n"
        + "       IUF_T_PROCEDIM_AMMINISTRAZIO PA,                               \n"
        + "       IUF_D_ESITO E                                                    \n"

        + "     WHERE                                                              \n"
        + "       IL.ID_LIVELLO = RLB.ID_LIVELLO                                   \n"
        + "       AND IL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO      \n";
    if (idsPODaEscludere != null && !idsPODaEscludere.isEmpty())
      QUERY += getNotInCondition("PO.ID_PROCEDIMENTO_OGGETTO",
          idsPODaEscludere);
    QUERY += "       AND PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                 \n"
        + "       AND P.ID_PROCEDIMENTO = PA.ID_PROCEDIMENTO                       \n"
        + "       AND PA.DATA_FINE IS NULL                                         \n"
        + "       AND PA.EXT_ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA                \n"
        + "       AND P.ID_BANDO = :ID_BANDO                                       \n"
        + "       AND NOT EXISTS                                                   \n"
        + "       (                                                                \n"
        + "         SELECT                                                         \n"
        + "           *                                                            \n"
        + "         FROM                                                           \n"
        + "           IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL                            \n"
        + "         WHERE                                                          \n"
        + "           LLIL.ID_IMPORTI_LIQUIDATI = IL.ID_IMPORTI_LIQUIDATI          \n"
        + "       )                                                                \n"
        + "       AND PO.ID_ESITO = E.ID_ESITO                                     \n"
        + "       AND E.CODICE LIKE :ESITO_PRATICHE_LIQUIDABILI                    \n"

        + "       AND ( IL.RAGGRUPPAMENTO = RLB.RAGGRUPPAMENTO      			   \n"
        + "					  OR (IL.RAGGRUPPAMENTO IS NULL          			   \n"
        + "							AND RLB.RAGGRUPPAMENTO IS NULL )    )          \n"

        + "   ) AS IMPORTO_DA_LIQUIDARE                                            \n"
        + " FROM                                                                   \n"
        + "   IUF_T_RISORSE_LIVELLO_BANDO RLB,                                     \n"
        + "   IUF_D_LIVELLO L                                                      \n"
        + " WHERE                                                                  \n"
        + "   NVL(RLB.DATA_FINE,SYSDATE)>=SYSDATE                                  \n"
        + "   AND RLB.FLAG_BLOCCATO      = 'N'                                     \n"
        + "   AND RLB.ID_BANDO           = :ID_BANDO                               \n"
        + "   AND RLB.ID_TIPO_IMPORTO    = :ID_TIPO_IMPORTO                        \n"
        + "   AND RLB.ID_LIVELLO         = L.ID_LIVELLO                            \n"
        + "   AND                                                                  \n"
        + "   (                                                                    \n"
        + "     RLB.FLAG_AMM_COMPETENZA = 'S'                                      \n"
        + "     OR EXISTS                                                          \n"
        + "     (                                                                  \n"
        + "       SELECT                                                           \n"
        + "         *                                                              \n"
        + "       FROM                                                             \n"
        + "         IUF_R_RISOR_LIV_BANDO_AMM_CO RLBAC                           \n"
        + "       WHERE                                                            \n"
        + "         RLBAC.ID_RISORSE_LIVELLO_BANDO  = RLB.ID_RISORSE_LIVELLO_BANDO \n"
        + "         AND RLBAC.EXT_ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA           \n"
        + "     )                                                                  \n"
        + "   )                                                                    \n"
        + " ORDER BY                                                               \n"
        + "   L.ORDINAMENTO ASC, RLB.RAGGRUPPAMENTO                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
        Types.NUMERIC);
    mapParameterSource.addValue("ESITO_PRATICHE_LIQUIDABILI",
        IuffiConstants.ESITO.LISTE_LIQUIDAZIONE.LIKE_ESITO_PRATICHE_LIQUIDABILI,
        Types.VARCHAR);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<RisorseImportiOperazioneDTO>>()
          {
            @Override
            public List<RisorseImportiOperazioneDTO> extractData(ResultSet rs)
                throws SQLException
            {
              List<RisorseImportiOperazioneDTO> result = new ArrayList<RisorseImportiOperazioneDTO>();
              while (rs.next())
              {
                RisorseImportiOperazioneDTO risorsa = new RisorseImportiOperazioneDTO();
                risorsa.setIdRisorseLivelloBando(
                    rs.getLong("ID_RISORSE_LIVELLO_BANDO"));
                risorsa.setCodiceOperazione(rs.getString("CODICE"));
                risorsa
                    .setRisorseAttivate(rs.getBigDecimal("RISORSE_ATTIVATE"));
                risorsa.setRaggruppamento(rs.getString("RAGGRUPPAMENTO"));
                risorsa.setImportoInLiquidazione(
                    rs.getBigDecimal("IMPORTO_LIQUIDATO"));
                risorsa.setNumeroPagamentiLista(rs.getLong("NUM_DA_LIQUIDARE"));
                risorsa.setImportoDaLiquidare(
                    rs.getBigDecimal("IMPORTO_DA_LIQUIDARE"));
                risorsa.calcolaImportoRimanente();
                result.add(risorsa);
              }
              return result;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          // new LogParameter("idVariabile", idVariabile),
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

  public Long getMaxIdListaLiquidazione(long idBando, long idAmmCompetenza,
      long idTipoImporto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getMaxIdListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                              \n"
        + "   MAX(LL.ID_LISTA_LIQUIDAZIONE)                     \n"
        + " FROM                                                \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL                       \n"
        + " WHERE                                               \n"
        + "   LL.ID_BANDO                  = :ID_BANDO          \n"
        + "   AND LL.EXT_ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA \n"
        + "   AND LL.ID_TIPO_IMPORTO       = :ID_TIPO_IMPORTO   \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
        Types.NUMERIC);
    try
    {
      return queryForLongNull(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          // new LogParameter("idVariabile", idVariabile),
          },
          new LogVariable[]
          {
          // new LogParameter("idVariabile", idVariabile),
          }, QUERY, mapParameterSource);
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

  public long insertListaLiquidazione(long idBando, long idAmmCompetenza,
      int idTipoImporto,
      long extIdTecnicoLiquidatore, long idUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " INSERT                              \n"
        + " INTO                                \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE          \n"
        + "   (                                 \n"
        + "     ID_LISTA_LIQUIDAZIONE,          \n"
        + "     ID_BANDO,                       \n"
        + "     FLAG_AMM_COMPETENZA,            \n"
        + "     NUMERO_LISTA,                   \n"
        + "     DATA_CREAZIONE,                 \n"
        + "     FLAG_STATO_LISTA,               \n"
        + "     ID_TIPO_IMPORTO,                \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,    \n"
        + "     EXT_ID_AMM_COMPETENZA,          \n"
        + "     EXT_ID_TECNICO_LIQUIDATORE      \n"
        + "   )                                 \n"
        + "   VALUES                            \n"
        + "   (                                 \n"
        + "     :ID_LISTA_LIQUIDAZIONE,         \n"
        + "     :ID_BANDO,                      \n"
        + "     :FLAG_AMM_COMPETENZA,           \n"
        + "     (                               \n"
        + "       SELECT                        \n"
        + "         NVL(MAX(NUMERO_LISTA),0)+1  \n"
        + "       FROM                          \n"
        + "         IUF_T_LISTA_LIQUIDAZIONE LL \n"
        + "       WHERE                         \n"
        + "         LL.ID_BANDO = :ID_BANDO     \n"
        + "     )                               \n"
        + "     ,                               \n"
        + "     SYSDATE,                        \n"
        + "     :FLAG_STATO_LISTA,              \n"
        + "     :ID_TIPO_IMPORTO,               \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO,   \n"
        + "     :EXT_ID_AMM_COMPETENZA,         \n"
        + "     :EXT_ID_TECNICO_LIQUIDATORE     \n"
        + "   )                                 \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long idListaLiquidazione = getNextSequenceValue(
        "SEQ_IUF_T_LISTA_LIQUIDAZIONE");
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_AMM_COMPETENZA",
        IuffiConstants.FLAGS.NO, Types.VARCHAR);
    mapParameterSource.addValue("FLAG_STATO_LISTA",
        IuffiConstants.STATO.LISTE_LIQUIDAZIONE.FLAG.BOZZA,
        Types.VARCHAR);
    mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
        Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
        idUtenteAggiornamento, Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_AMM_COMPETENZA", idAmmCompetenza,
        Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_TECNICO_LIQUIDATORE",
        extIdTecnicoLiquidatore, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
      return idListaLiquidazione;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
              new LogParameter("idBando", idBando),
              new LogParameter("idTipoImporto", idTipoImporto),
              new LogParameter("idUtenteAggiornamento", idUtenteAggiornamento),
              new LogParameter("idAmmCompetenza", idAmmCompetenza),
              new LogParameter("extIdTecnicoLiquidatore",
                  extIdTecnicoLiquidatore)
          },
          null, QUERY, mapParameterSource);
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

  public int insertRelazioneTraRisorseLivelloBandoEImportiLiquidati(
      long idBando, long idAmmCompetenza,
      int idTipoImporto, List<Long> idsPODaEscludere)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertRisorseLivelloBandoImportiLiquidatiPerListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " INSERT                                                                 \n"
        + " INTO                                                                   \n"
        + "   IUF_R_RISOR_LIV_BAND_IMP_LIQ                                       \n"
        + "   (                                                                    \n"
        + "     ID_RISORSE_LIVELLO_BANDO,                                          \n"
        + "     ID_IMPORTI_LIQUIDATI                                               \n"
        + "   )                                                                    \n"
        + " SELECT                                                                 \n"
        + "   RLB.ID_RISORSE_LIVELLO_BANDO,                                        \n"
        + "   IL.ID_IMPORTI_LIQUIDATI                                              \n"
        + " FROM                                                                   \n"
        + "   IUF_T_IMPORTI_LIQUIDATI IL,                                          \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                       \n"
        + "   IUF_T_PROCEDIMENTO P,                                                \n"
        + "   IUF_D_ESITO E,                                                       \n"
        + "   IUF_T_PROCEDIM_AMMINISTRAZIO PA,                                   \n"
        + "   IUF_T_RISORSE_LIVELLO_BANDO RLB                                      \n"
        + " WHERE                                                                  \n"
        + "   NVL(RLB.DATA_FINE,SYSDATE)>=SYSDATE                                  \n"
        + "   AND RLB.FLAG_BLOCCATO      = 'N'                                     \n"
        + "   AND RLB.ID_BANDO           = :ID_BANDO                               \n"
        + "   AND RLB.ID_TIPO_IMPORTO    = :ID_TIPO_IMPORTO                        \n"
        + "   AND                                                                  \n"
        + "   (                                                                    \n"
        + "     RLB.FLAG_AMM_COMPETENZA = 'S'                                      \n"
        + "     OR EXISTS                                                          \n"
        + "     (                                                                  \n"
        + "       SELECT                                                           \n"
        + "         *                                                              \n"
        + "       FROM                                                             \n"
        + "         IUF_R_RISOR_LIV_BANDO_AMM_CO RLBAC                           \n"
        + "       WHERE                                                            \n"
        + "         RLBAC.ID_RISORSE_LIVELLO_BANDO  = RLB.ID_RISORSE_LIVELLO_BANDO \n"
        + "         AND RLBAC.EXT_ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA           \n"
        + "     )                                                                  \n"
        + "   )                                                                    \n"
        + "   AND IL.ID_LIVELLO              = RLB.ID_LIVELLO                      \n"
        + "   AND IL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO          \n"
        + "   AND PO.ID_PROCEDIMENTO         = P.ID_PROCEDIMENTO                   \n";
    if (idsPODaEscludere != null && !idsPODaEscludere.isEmpty())
      QUERY += getNotInCondition("PO.ID_PROCEDIMENTO_OGGETTO",
          idsPODaEscludere);
    QUERY += "   AND P.ID_PROCEDIMENTO          = PA.ID_PROCEDIMENTO            \n"
        + "   AND PA.DATA_FINE              IS NULL                                \n"
        + "   AND PA.EXT_ID_AMM_COMPETENZA   = :ID_AMM_COMPETENZA                  \n"
        + "   AND P.ID_BANDO                 = RLB.ID_BANDO                        \n"
        + "   AND PO.ID_ESITO = E.ID_ESITO                                         \n"
        + "   AND E.CODICE LIKE :ESITO_PRATICHE_LIQUIDABILI                        \n"
        + "   AND NOT EXISTS                                                       \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       *                                                                \n"
        + "     FROM                                                               \n"
        + "       IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL                                \n"
        + "     WHERE                                                              \n"
        + "       LLIL.ID_IMPORTI_LIQUIDATI = IL.ID_IMPORTI_LIQUIDATI              \n"
        + "   )                                                                    \n"
        + "		AND ( IL.RAGGRUPPAMENTO = RLB.RAGGRUPPAMENTO      				   \n"
        + "					  OR (IL.RAGGRUPPAMENTO IS NULL          			   \n"
        + "							AND RLB.RAGGRUPPAMENTO IS NULL )    )          \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza,
        Types.NUMERIC);
    mapParameterSource.addValue("ESITO_PRATICHE_LIQUIDABILI",
        IuffiConstants.ESITO.LISTE_LIQUIDAZIONE.LIKE_ESITO_PRATICHE_LIQUIDABILI,
        Types.VARCHAR);
    try
    {
      return namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBando", idBando),
              new LogParameter("idTipoImporto", idTipoImporto),
              new LogParameter("idAmmCompetenza", idAmmCompetenza)
          },
          null, QUERY, mapParameterSource);
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

  public int insertRelazioneTraImportiLiquidatiEListaLiquidazione(
      long idListaLiquidazione, long idBando,
      long idAmmCompetenza, int idTipoImporto, List<Long> idsPODaEscludere)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertRelazioneTraImportiLiquidatiEListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " INSERT                                                                 \n"
        + " INTO                                                                   \n"
        + "   IUF_R_LISTA_LIQUIDAZ_IMP_LIQ                                         \n"
        + "   (                                                                    \n"
        + "     ID_LISTA_LIQUIDAZ_IMP_LIQ,                                         \n"
        + "     ID_LISTA_LIQUIDAZIONE,                                             \n"
        + "     ID_IMPORTI_LIQUIDATI,                                              \n"
        + "     FLAG_ESITO_LIQUIDAZIONE,                                           \n"
        + "     DATA_ULTIMO_AGGIORNAMENTO                                          \n"
        + "   )                                                                    \n"
        + " SELECT                                                                 \n"
        + "   SEQ_IUF_R_LIST_LIQUI_IMP_LIQ.NEXTVAL,                               \n"
        + "   :ID_LISTA_LIQUIDAZIONE,                                              \n"
        + "   IL.ID_IMPORTI_LIQUIDATI,                                             \n"
        + "   'N',                                                                 \n"
        + "   SYSDATE                                                              \n"
        + " FROM                                                                   \n"
        + "   IUF_T_IMPORTI_LIQUIDATI IL,                                          \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                       \n"
        + "   IUF_T_PROCEDIMENTO P,                                                \n"
        + "   IUF_D_ESITO E,                                                       \n"
        + "   IUF_T_PROCEDIM_AMMINISTRAZIO PA,                                   \n"
        + "   IUF_T_RISORSE_LIVELLO_BANDO RLB                                      \n"
        + " WHERE                                                                  \n"
        + "   NVL(RLB.DATA_FINE,SYSDATE)>=SYSDATE                                  \n"
        + "   AND RLB.FLAG_BLOCCATO      = 'N'                                     \n"
        + "   AND RLB.ID_BANDO           = :ID_BANDO                               \n"
        + "   AND RLB.ID_TIPO_IMPORTO    = :ID_TIPO_IMPORTO                        \n"
        + "   AND                                                                  \n"
        + "   (                                                                    \n"
        + "     RLB.FLAG_AMM_COMPETENZA = 'S'                                      \n"
        + "     OR EXISTS                                                          \n"
        + "     (                                                                  \n"
        + "       SELECT                                                           \n"
        + "         *                                                              \n"
        + "       FROM                                                             \n"
        + "         IUF_R_RISOR_LIV_BANDO_AMM_CO RLBAC                           \n"
        + "       WHERE                                                            \n"
        + "         RLBAC.ID_RISORSE_LIVELLO_BANDO  = RLB.ID_RISORSE_LIVELLO_BANDO \n"
        + "         AND RLBAC.EXT_ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA           \n"
        + "     )                                                                  \n"
        + "   )                                                                    \n"
        + "   AND PO.ID_ESITO = E.ID_ESITO                                         \n"
        + "   AND E.CODICE LIKE :ESITO_PRATICHE_LIQUIDABILI                        \n"
        + "   AND IL.ID_LIVELLO              = RLB.ID_LIVELLO                      \n"
        + "   AND IL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO          \n"
        + "   AND PO.ID_PROCEDIMENTO         = P.ID_PROCEDIMENTO                   \n";
    if (idsPODaEscludere != null && !idsPODaEscludere.isEmpty())
      QUERY += getNotInCondition("PO.ID_PROCEDIMENTO_OGGETTO",
          idsPODaEscludere);
    QUERY += "   AND P.ID_PROCEDIMENTO          = PA.ID_PROCEDIMENTO            \n"
        + "   AND PA.DATA_FINE              IS NULL                                \n"
        + "   AND PA.EXT_ID_AMM_COMPETENZA   = :ID_AMM_COMPETENZA                  \n"
        + "   AND P.ID_BANDO                 = RLB.ID_BANDO                        \n"
        + "   AND NOT EXISTS                                                       \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       *                                                                \n"
        + "     FROM                                                               \n"
        + "       IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL                                \n"
        + "     WHERE                                                              \n"
        + "       LLIL.ID_IMPORTI_LIQUIDATI = IL.ID_IMPORTI_LIQUIDATI              \n"
        + "   )                                                                    \n"

        + "       AND ( IL.RAGGRUPPAMENTO = RLB.RAGGRUPPAMENTO      			   \n"
        + "					  OR (IL.RAGGRUPPAMENTO IS NULL          			   \n"
        + "							AND RLB.RAGGRUPPAMENTO IS NULL )    )          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza,
        Types.NUMERIC);
    mapParameterSource.addValue("ESITO_PRATICHE_LIQUIDABILI",
        IuffiConstants.ESITO.LISTE_LIQUIDAZIONE.LIKE_ESITO_PRATICHE_LIQUIDABILI,
        Types.VARCHAR);
    try
    {
      return namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
              new LogParameter("idBando", idBando),
              new LogParameter("idTipoImporto", idTipoImporto),
              new LogParameter("idAmmCompetenza", idAmmCompetenza)
          },
          null, QUERY, mapParameterSource);
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

  public int insertImportiRipartitiConPercentuale(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertImportiRipartitiConPercentuale";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " INSERT                                                           \n"
        + " INTO                                                             \n"
        + "   IUF_R_IMPORTI_RIPARTITI                                        \n"
        + "   (                                                              \n"
        + "     ID_IMPORTI_RIPARTITI,                                        \n"
        + "     ID_LISTA_LIQUIDAZ_IMP_LIQ,                                   \n"
        + "     ID_VOCE_RIPARTIZIONE,                                        \n"
        + "     PERCENTUALE_RIPARTIZIONE,                                    \n"
        + "     IMPORTO_RIPARTITO                                            \n"
        + "   )                                                              \n"
        + " SELECT                                                           \n"
        + "   SEQ_IUF_R_IMPORTI_RIPARTITI.NEXTVAL,                           \n"
        + "   LLIL.ID_LISTA_LIQUIDAZ_IMP_LIQ,                                \n"
        + "   RC.ID_VOCE_RIPARTIZIONE,                                       \n"
        + "   RC.PERCENTUALE_RIPARTIZIONE,                                   \n"
        + "   ROUND(IL.IMPORTO_LIQUIDATO * RC.PERCENTUALE_RIPARTIZIONE/100,2)\n"
        + " FROM                                                             \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL,                                   \n"
        + "   IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL,                             \n"
        + "   IUF_T_IMPORTI_LIQUIDATI IL,                                    \n"
        + "   IUF_R_RIPARTIZIONE_CONTRIBUT RC                               \n"
        + " WHERE                                                            \n"
        + "   LL.ID_LISTA_LIQUIDAZIONE          = :ID_LISTA_LIQUIDAZIONE     \n"
        + "   AND LLIL.ID_LISTA_LIQUIDAZIONE    = LL.ID_LISTA_LIQUIDAZIONE   \n"
        + "   AND LLIL.ID_IMPORTI_LIQUIDATI     = IL.ID_IMPORTI_LIQUIDATI    \n"
        + "   AND IL.ID_LIVELLO                 = RC.ID_LIVELLO              \n"
        + "   AND RC.ID_TIPO_IMPORTO            = LL.ID_TIPO_IMPORTO         \n"
        + "   AND RC.FLAG_CRITERIO_RIPARTIZIONE = 'P'                        \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione) },
          null, QUERY, mapParameterSource);
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

  public int insertImportiRipartitiPerDifferenza(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertImportiRipartitiPerDifferenza";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " INSERT                                                              \n"
        + " INTO                                                                \n"
        + "   IUF_R_IMPORTI_RIPARTITI                                           \n"
        + "   (                                                                 \n"
        + "     ID_IMPORTI_RIPARTITI,                                           \n"
        + "     ID_LISTA_LIQUIDAZ_IMP_LIQ,                                      \n"
        + "     ID_VOCE_RIPARTIZIONE,                                           \n"
        + "     PERCENTUALE_RIPARTIZIONE,                                       \n"
        + "     IMPORTO_RIPARTITO                                               \n"
        + "   )                                                                 \n"
        + " SELECT                                                              \n"
        + "   SEQ_IUF_R_IMPORTI_RIPARTITI.NEXTVAL,                              \n"
        + "   LLIL.ID_LISTA_LIQUIDAZ_IMP_LIQ,                                   \n"
        + "   RC.ID_VOCE_RIPARTIZIONE,                                          \n"
        + "   RC.PERCENTUALE_RIPARTIZIONE,                                      \n"
        + "   IL.IMPORTO_LIQUIDATO -                                            \n"
        + "   (                                                                 \n"
        + "     SELECT                                                          \n"
        + "       NVL(SUM(IR.IMPORTO_RIPARTITO),0)                              \n"
        + "     FROM                                                            \n"
        + "       IUF_R_IMPORTI_RIPARTITI IR                                    \n"
        + "     WHERE                                                           \n"
        + "       IR.ID_LISTA_LIQUIDAZ_IMP_LIQ = LLIL.ID_LISTA_LIQUIDAZ_IMP_LIQ \n"
        + "   )                                                                 \n"
        + " FROM                                                                \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL,                                      \n"
        + "   IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL,                                \n"
        + "   IUF_T_IMPORTI_LIQUIDATI IL,                                       \n"
        + "   IUF_R_RIPARTIZIONE_CONTRIBUT RC                                  \n"
        + " WHERE                                                               \n"
        + "   LL.ID_LISTA_LIQUIDAZIONE          = :ID_LISTA_LIQUIDAZIONE        \n"
        + "   AND LLIL.ID_LISTA_LIQUIDAZIONE    = LL.ID_LISTA_LIQUIDAZIONE      \n"
        + "   AND LLIL.ID_IMPORTI_LIQUIDATI     = IL.ID_IMPORTI_LIQUIDATI       \n"
        + "   AND IL.ID_LIVELLO                 = RC.ID_LIVELLO                 \n"
        + "   AND RC.ID_TIPO_IMPORTO            = LL.ID_TIPO_IMPORTO            \n"
        + "   AND RC.FLAG_CRITERIO_RIPARTIZIONE = 'D'                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione)
          },
          null, QUERY, mapParameterSource);
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

  public int insertFileListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "insertFileListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " INSERT                                              \n"
        + " INTO                                                \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION                     \n"
        + "   (                                                 \n"
        + "     ID_FILE_LISTA_LIQUIDAZIONE,                     \n"
        + "     ID_LISTA_LIQUIDAZIONE,                          \n"
        + "     NOME_FILE,                                      \n"
        + "     ID_STATO_STAMPA,                                \n"
        + "     DATA_ULTIMO_AGGIORNAMENTO                       \n"
        + "   )                                                 \n"
        + " SELECT                                              \n"
        + "   SEQ_IUF_T_FILE_LISTA_LIQUIDA.NEXTVAL,           \n"
        + "   LL.ID_LISTA_LIQUIDAZIONE,                         \n"
        + "   'Lista di liquidazione N. '                       \n"
        + "   || LL.NUMERO_LISTA                                \n"
        + "   || '.pdf',                                        \n"
        + "   1,                                                \n"
        + "   SYSDATE                                           \n"
        + " FROM                                                \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL                       \n"
        + " WHERE                                               \n"
        + "   LL.ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione)
          },
          null, QUERY, mapParameterSource);
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

  public DatiCreazioneListaDTO getDatiCreazioneLista(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDatiCreazioneLista";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                  \n"
        + "   LL.ID_LISTA_LIQUIDAZIONE,                             \n"
        + "   LL.NUMERO_LISTA,                                      \n"
        + "   B.DENOMINAZIONE AS DENOMINAZIONE_BANDO                \n"
        + " FROM                                                    \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL,                          \n"
        + "   IUF_D_BANDO B                                         \n"
        + " WHERE                                                   \n"
        + "   LL.ID_BANDO                  = B.ID_BANDO             \n"
        + "   AND LL.ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    try
    {
      return queryForObject(QUERY, mapParameterSource,
          DatiCreazioneListaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
          },
          null, QUERY, mapParameterSource);
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

  public void deleteRisorseLivBandImpLiq(Long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRisorseLivBandImpLiq]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_R_RISOR_LIV_BAND_IMP_LIQ   		\n"
        + " WHERE ID_IMPORTI_LIQUIDATI IN (										\n"
        + " SELECT IL.ID_IMPORTI_LIQUIDATI FROM IUF_T_IMPORTI_LIQUIDATI IL,		\n"
        + "  IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LIL 									\n"
        + "  WHERE LIL.ID_LISTA_LIQUIDAZIONE = :idListaLiquidazione				\n"
        + "  AND LIL.ID_IMPORTI_LIQUIDATI = IL.ID_IMPORTI_LIQUIDATI)			\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("idListaLiquidazione", idListaLiquidazione,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione)
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

  public void deleteRImportiRipartiti(Long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRImportiRipartiti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_R_IMPORTI_RIPARTITI   						\n"
        + " WHERE ID_LISTA_LIQUIDAZ_IMP_LIQ IN (										\n"
        + " SELECT ID_LISTA_LIQUIDAZ_IMP_LIQ FROM IUF_R_LISTA_LIQUIDAZ_IMP_LIQ			\n"
        + "  WHERE ID_LISTA_LIQUIDAZIONE = :idListaLiquidazione			)				\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("idListaLiquidazione", idListaLiquidazione,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione)
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

  public StampaListaLiquidazioneDTO getStampaListaLiquidazione(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getStampaListaLiquidazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT *                                                  \n"
        + " FROM IUF_T_FILE_LISTA_LIQUIDAZION 						\n"
        + " WHERE ID_LISTA_LIQUIDAZIONE=:idListaLiquidazione		  	\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("idListaLiquidazione", idListaLiquidazione,
          Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource,
          StampaListaLiquidazioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione)
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

  public String getStatoListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getStatoListaLiquidazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                         		\n"
        + "   FLAG_STATO_LISTA						 		\n"
        + " FROM                                     		\n"
        + "   IUF_T_LISTA_LIQUIDAZIONE				 		\n"
        + " WHERE                                     		\n"
        + "   ID_LISTA_LIQUIDAZIONE =:idListaLiquidazione   \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("idListaLiquidazione", idListaLiquidazione,
          Types.NUMERIC);
      return queryForString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione)
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

  public void lockListaLiquidazione(long idListaLiquidazione)
  {
    final String QUERY = " SELECT 1 FROM IUF_D_BANDO WHERE ID_BANDO = (SELECT ID_BANDO FROM IUF_T_LISTA_LIQUIDAZIONE WHERE ID_LISTA_LIQUIDAZIONE=:ID_LISTA_LIQUIDAZIONE) FOR UPDATE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    queryForLong(QUERY, mapParameterSource);
  }

  public void ripristinaListaLiquidazione(
      StampaListaLiquidazioneDTO stampaListaLiq)
      throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::ripristinaListaLiquidazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " UPDATE                                                       		 \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION                              \n"
        + " SET                                                          \n"
        + "   DATA_ULTIMO_AGGIORNAMENTO = SYSDATE                        \n"
        + " WHERE                                                        \n"
        + "   ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE         	 \n";
    MapSqlParameterSource mapParameterSource = null;
    try
    {
      mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE",
          stampaListaLiq.getIdListaLiquidazione(), Types.NUMERIC);

      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_LISTA_LIQUIDAZIONE",
                  stampaListaLiq.getIdListaLiquidazione())
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

  public void setStatoListaLiquidazione(long idListaLiquidazione, int stato)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::setStatoListaLiquidazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " UPDATE                                                       		 \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION                              \n"
        + " SET                                                          \n"
        + "   ID_STATO_STAMPA = :ID_STATO_STAMPA,                        \n"
        + "   DATA_ULTIMO_AGGIORNAMENTO = SYSDATE                        \n"
        + " WHERE                                                        \n"
        + "   ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE         	 \n";
    MapSqlParameterSource mapParameterSource = null;
    try
    {
      mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_STATO_STAMPA", stato, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione)
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

  public byte[] getContenutoFileListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getContenutoFileListaLiquidazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
      logger
          .debug(THIS_METHOD + " idListaLiquidazione = " + idListaLiquidazione);
    }
    final String QUERY = " SELECT                                         \n"
        + "   EXT_ID_DOCUMENTO_INDEX,                      \n"
        + "   FILE_ALLEGATO                                \n"
        + " FROM                                           \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION								 \n"
        + " WHERE                                          \n"
        + "   ID_LISTA_LIQUIDAZIONE = :idListaLiquidazione \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("idListaLiquidazione", idListaLiquidazione,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<byte[]>()
          {
            @Override
            public byte[] extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                Long extIdDocumentoIndex = getLongNull(rs,
                    "EXT_ID_DOCUMENTO_INDEX");
                if (extIdDocumentoIndex != null)
                {
                  AgriWellEsitoVO esitoVO;
                  try
                  {
                    esitoVO = IuffiUtils.PORTADELEGATA
                        .getAgriwellCSIInterface()
                        .agriwellServiceLeggiDoquiAgri(extIdDocumentoIndex);
                  }
                  catch (Exception e)
                  {
                    /*
                     * Questo è l'unico modo che ho per rilanciare una eccezione
                     * senza cambiare la firma del metodo (che ovviamente non
                     * sarebbe più coerente con la definizione fatta nel
                     * ResultSetExtractor)
                     */
                    DumpUtils.logGenericException(logger, null, e,
                        (LogParameter[]) null, null, THIS_METHOD);
                    throw new RuntimeException(e);
                  }
                  final Integer esito = esitoVO == null ? null
                      : esitoVO.getEsito();
                  if (esito != null
                      && esito == IuffiConstants.SERVICE.AGRIWELL.RETURN_CODE.SUCCESS)
                  {
                    return esitoVO.getContenutoFile();
                  }
                  else
                  {
                    /*
                     * Questo è l'unico modo che ho per rilanciare una eccezione
                     * senza cambiare la firma del metodo (che ovviamente non
                     * sarebbe più coerente con la definizione fatta nel
                     * ResultSetExtractor)
                     */
                    throw new RuntimeException(
                        "Errore nel richiamo del servizio agriwellServiceLeggiDoquiAgri(): "
                            + esitoVO.getMessaggio());
                  }
                }
                else
                {
                  return rs.getBytes("FILE_ALLEGATO");
                }
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
              new LogParameter("idListaLiquidazione", idListaLiquidazione)
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

  public List<ImportiRipartitiListaLiquidazioneDTO> getImportiRipartitiListaLiquidazione(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getImportiRipartitiListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                       \n"
        + "   QQQ AS                                                                   \n"
        + "   (                                                                        \n"
        + "     SELECT DISTINCT                                                        \n"
        + "       LIV.CODICE           AS OPERAZIONE,                                  \n"
        + "       P.IDENTIFICATIVO     AS IDENTIFICATIVO,                              \n"
        + "       O.CAUSALE_PAGAMENTO  AS CAUSALE_PAGAM,                               \n"
        + "       ANA.CUAA             AS CUAA,                                        \n"
        + "       ANA.DENOMINAZIONE    AS DENOMINAZIONE,                               \n"
        + "       IL.IMPORTO_LIQUIDATO AS IMPORTO_TOTALE,                              \n"
        + "       LIV.ORDINAMENTO,                                                     \n"
        + "       LLIL.ID_LISTA_LIQUIDAZ_IMP_LIQ AS ID_LISTA_LIQ_IMP_LIQ,              \n"
        + "       IL.IMPORTO_COMPLESSIVO         AS IMPORTO_PREMIO,                    \n"
        + "       (                                                                    \n"
        + "         SELECT                                                             \n"
        + "           SUM(ILL.IMPORTO_LIQUIDATO)                                       \n"
        + "         FROM                                                               \n"
        + "           IUF_T_IMPORTI_LIQUIDATI ILL,                                     \n"
        + "           IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LL3,                                \n"
        + "           IUF_T_PROCEDIMENTO_OGGETTO po2,                                  \n"
        + "           IUF_R_LEGAME_GRUPPO_OGGETTO LGO2,                                \n"
        + "           IUF_D_OGGETTO O2                                                 \n"
        + "         WHERE                                                              \n"
        + "           ILL.ID_PROCEDIMENTO_OGGETTO      = po2.id_procedimento_oggetto   \n"
        + "           AND PO2.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO             \n"
        + "           AND PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO2.ID_LEGAME_GRUPPO_OGGETTO \n"
        + "           AND LGO2.ID_OGGETTO              = O2.ID_OGGETTO                 \n"
        + "           AND O2.CODICE                    = 'ANTPR'                       \n"
        + "           AND ILL.ID_LIVELLO               = LIV.ID_LIVELLO                \n"
        + "           AND LL3.FLAG_ESITO_LIQUIDAZIONE <> 'R'                           \n"
        + "           AND LL3.ID_IMPORTI_LIQUIDATI     = ILL.ID_IMPORTI_LIQUIDATI      \n"
        + "       ) AS ANTICIPO_EROGATO                                                \n"
        + "     FROM                                                                   \n"
        + "       IUF_T_LISTA_LIQUIDAZIONE LL,                                         \n"
        + "       IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL,                                   \n"
        + "       IUF_T_IMPORTI_LIQUIDATI IL,                                          \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                       \n"
        + "       IUF_T_PROCEDIMENTO P,                                                \n"
        + "       IUF_D_LIVELLO LIV,                                                   \n"
        + "       IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                     \n"
        + "       IUF_T_PROCEDIMENTO_AZIENDA PA,                                       \n"
        + "       IUF_D_OGGETTO O,                                                     \n"
        + "       IUF_D_BANDO B,                                                       \n"
        + "       IUF_R_BANDO_OGGETTO BO,                                              \n"
        + "       SMRGAA_V_DATI_ANAGRAFICI ANA                                         \n"
        + "     WHERE                                                                  \n"
        + "       LL.ID_LISTA_LIQUIDAZIONE        = LLIL.ID_LISTA_LIQUIDAZIONE         \n"
        + "       AND LLIL.ID_IMPORTI_LIQUIDATI   = IL.ID_IMPORTI_LIQUIDATI            \n"
        + "       AND IL.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO         \n"
        + "       AND IL.ID_LIVELLO               = LIV.ID_LIVELLO                     \n"
        + "       AND PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO                  \n"
        + "       AND PA.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO                  \n"
        + "       AND PA.EXT_ID_AZIENDA           = ANA.ID_AZIENDA                     \n"
        + "       AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO       \n"
        + "       AND LGO.ID_OGGETTO              = O.ID_OGGETTO                       \n"
        + "       AND BO.ID_BANDO                 = B.ID_BANDO                         \n"
        + "       AND B.ID_BANDO                  = LL.ID_BANDO                        \n"
        + "       AND PA.DATA_FINE               IS NULL                               \n"
        + "       AND ANA.DATA_FINE_VALIDITA     IS NULL                               \n"
        + "       AND LL.ID_LISTA_LIQUIDAZIONE    = :ID_LISTA_LIQUIDAZIONE             \n"
        + "       AND LIV.DATA_FINE              IS NULL                               \n"
        + "       AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO       \n"
        + "     ORDER BY                                                               \n"
        + "       LIV.ORDINAMENTO,                                                     \n"
        + "       ANA.DENOMINAZIONE                                                    \n"
        + "   )                                                                        \n"
        + " SELECT                                                                     \n"
        + "   QQQ.*,                                                                   \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       irip2.IMPORTO_RIPARTITO                                              \n"
        + "     FROM                                                                   \n"
        + "       IUF_r_importi_ripartiti irip2                                        \n"
        + "     WHERE                                                                  \n"
        + "       QQQ.ID_LISTA_LIQ_IMP_LIQ       =irip2.ID_LISTA_LIQUIDAZ_IMP_LIQ      \n"
        + "       AND irip2.id_voce_ripartizione = 1                                   \n"
        + "   ) AS quota_Ue,                                                           \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       irip2.IMPORTO_RIPARTITO                                              \n"
        + "     FROM                                                                   \n"
        + "       IUF_r_importi_ripartiti irip2                                        \n"
        + "     WHERE                                                                  \n"
        + "       QQQ.ID_LISTA_LIQ_IMP_LIQ       =irip2.ID_LISTA_LIQUIDAZ_IMP_LIQ      \n"
        + "       AND irip2.id_voce_ripartizione = 2                                   \n"
        + "   ) AS quota_Naz,                                                          \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       irip2.IMPORTO_RIPARTITO                                              \n"
        + "     FROM                                                                   \n"
        + "       IUF_r_importi_ripartiti irip2                                        \n"
        + "     WHERE                                                                  \n"
        + "       QQQ.ID_LISTA_LIQ_IMP_LIQ       =irip2.ID_LISTA_LIQUIDAZ_IMP_LIQ      \n"
        + "       AND irip2.id_voce_ripartizione = 3                                   \n"
        + "   ) AS quota_Reg                                                           \n"
        + " FROM                                                                       \n"
        + "   QQQ                                                                      \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
          Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          ImportiRipartitiListaLiquidazioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null, (LogVariable[]) null, QUERY,
          mapParameterSource);
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

  public List<RiepilogoImportiApprovazioneDTO> getRiepilogoImportiApprovazione(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRiepilogoImportiApprovazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                           \n"
        + "   IL.ID_LIVELLO,                                                 \n"
        + "   L.ORDINAMENTO,                                                 \n"
        + "   L.CODICE AS CODICE_LIVELLO,                                    \n"
        + "   O.CAUSALE_PAGAMENTO,                                           \n"
        + "   COUNT(*)                  AS NUM_PAGAMENTI,                    \n"
        + "   SUM(IL.IMPORTO_LIQUIDATO) AS IMPORTO_LIQUIDATO                 \n"
        + " FROM                                                             \n"
        + "   IUF_T_IMPORTI_LIQUIDATI IL,                                    \n"
        + "   IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL,                             \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                 \n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                               \n"
        + "   IUF_D_OGGETTO O,                                               \n"
        + "   IUF_D_LIVELLO L                                                \n"
        + " WHERE                                                            \n"
        + "   IL.ID_IMPORTI_LIQUIDATI         = LLIL.ID_IMPORTI_LIQUIDATI    \n"
        + "   AND LLIL.ID_LISTA_LIQUIDAZIONE  = :ID_LISTA_LIQUIDAZIONE       \n"
        + "   AND PO.ID_PROCEDIMENTO_OGGETTO  = IL.ID_PROCEDIMENTO_OGGETTO   \n"
        + "   AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO \n"
        + "   AND LGO.ID_OGGETTO              = O.ID_OGGETTO                 \n"
        + "   AND IL.ID_LIVELLO               = L.ID_LIVELLO                 \n"
        + " GROUP BY                                                         \n"
        + "   IL.ID_LIVELLO,                                                 \n"
        + "   L.CODICE,                                                      \n"
        + "   L.ORDINAMENTO,                                                 \n"
        + "   O.CAUSALE_PAGAMENTO                                            \n"
        + " ORDER BY                                                         \n"
        + "   L.ORDINAMENTO                                                  \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
          Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          RiepilogoImportiApprovazioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null, (LogVariable[]) null, QUERY,
          mapParameterSource);
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

  public void aggiornaStatoLista(long idListaLiquidazione,
      String flagStatoLista, long idUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "aggiornaStatoLista";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                                       \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE                                   \n"
        + " SET                                                          \n"
        + "   FLAG_STATO_LISTA            = :FLAG_STATO_LISTA,           \n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO \n"
        + " WHERE                                                        \n"
        + "   ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    mapParameterSource.addValue("FLAG_STATO_LISTA", flagStatoLista,
        Types.VARCHAR);
    mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
        idUtenteAggiornamento, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
              new LogParameter("flagStatoLista", flagStatoLista),
              new LogParameter("idUtenteAggiornamento", idUtenteAggiornamento)
          },
          null, QUERY, mapParameterSource);
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

  public void approvaLista(long idListaLiquidazione, long idUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "approvaLista";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                                       \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE                                   \n"
        + " SET                                                          \n"
        + "   FLAG_STATO_LISTA            = :FLAG_STATO_LISTA,           \n"
        + "   DATA_APPROVAZIONE            = SYSDATE,                    \n"
        + "   EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO \n"
        + " WHERE                                                        \n"
        + "   ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    mapParameterSource.addValue("FLAG_STATO_LISTA",
        IuffiConstants.STATO.LISTE_LIQUIDAZIONE.FLAG.APPROVATA,
        Types.VARCHAR);
    mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
        idUtenteAggiornamento, Types.NUMERIC);
    try
    {
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
              new LogParameter("idUtenteAggiornamento", idUtenteAggiornamento)
          },
          null, QUERY, mapParameterSource);
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

  public int approvaFileListaLiquidazione(long idListaLiquidazione,
      EsitoDocumentoVO esito, String originalFilename)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "approvaFileListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                               \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION                               \n"
        + " SET                                                           \n"
        + "   FILE_ALLEGATO               = NULL,                         \n"
        + "   EXT_ID_DOCUMENTO_INDEX      = :EXT_ID_DOCUMENTO_INDEX,      \n"
        + "   NUMERO_PROTOCOLLO = :NUMERO_PROTOCOLLO_DOCUMENTO,           \n"
        + "   DATA_PROTOCOLLO   = :DATA_PROTOCOLLO_DOCUMENTO,             \n"
        + "   NUMERO_PROTOCOLLO_EMERGENZA = :NUMERO_PROTOCOLLO_EMERGENZA, \n"
        + "   DATA_PROTOCOLLO_EMERGENZA   = :DATA_PROTOCOLLO_EMERGENZA,   \n"
        + "   ID_STATO_STAMPA             = :ID_STATO_STAMPA,             \n"
        + "   NOME_FILE                   = :NOME_FILE,                   \n"
        + "   DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE                       \n"
        + " WHERE                                                         \n"
        + "   ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("NOME_FILE",
          IuffiUtils.FILE.getSafeSubmittedFileName(originalFilename),
          Types.VARCHAR);
      mapParameterSource.addValue("ID_STATO_STAMPA",
          IuffiConstants.STATO.STAMPA.ID.FIRMATO_DIGITALMENTE,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_DOCUMENTO_INDEX",
          esito.getIdDocumentoIndex(), Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_PROTOCOLLO_DOCUMENTO",
          esito.getNumeroProtocolloDocumento(), Types.VARCHAR);
      mapParameterSource.addValue("DATA_PROTOCOLLO_DOCUMENTO",
          IuffiUtils.DATE.fromXMLGregorianCalendar(
              esito.getDataProtocolloDocumento()),
          Types.TIMESTAMP);
      mapParameterSource.addValue("NUMERO_PROTOCOLLO_EMERGENZA",
          esito.getNumeroProtocolloEmergenza(), Types.VARCHAR);
      mapParameterSource.addValue("DATA_PROTOCOLLO_EMERGENZA",
          IuffiUtils.DATE.fromXMLGregorianCalendar(
              esito.getDataProtocolloEmergenza()),
          Types.TIMESTAMP);
      return namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
              new LogParameter("stampaFirmata", idListaLiquidazione),
          },
          null, QUERY, mapParameterSource);
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

  public List<RiepilogoPraticheApprovazioneDTO> getRiepilogoPraticheInNuovaLista(
      long idBando, long idAmmCompetenza,
      int idTipoImporto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRiepilogoImportiApprovazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                             \n"
        + "   DA.CUAA,                                                             		\n"
        + "   DA.DENOMINAZIONE,                                                    		\n"
        + "   P.IDENTIFICATIVO,                                                    		\n"
        + "   P.ID_PROCEDIMENTO,                                                   		\n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO,                                          		\n"
        + "   L.CODICE AS CODICE_LIVELLO,                                          		\n"
        + "   O.CAUSALE_PAGAMENTO,                                                 		\n"
        + "   IL.IMPORTO_LIQUIDATO,                                                		\n"
        + "	  IL.IMPORTO_COMPLESSIVO AS IMPORTO_PREMIO,                            		\n"
        + "(SELECT                                                         		\n"
        + "     ILL2.IMPORTO_LIQUIDATO                                          			\n"
        + "		FROM                      	                      	                	\n"
        + "			IUF_T_PROCEDIMENTO_OGGETTO po2,                            			\n"
        + "      	IUF_R_LEGAME_GRUPPO_OGGETTO LGO2,                               	\n"
        + "     	IUF_D_OGGETTO O2                           				        	\n"
        + "			,IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL2,                                \n"
        + "			IUF_T_IMPORTI_LIQUIDATI ILL2                                        \n"
        + "     WHERE                       	                      					\n"
        + "			 PO2.ID_PROCEDIMENTO= P.ID_PROCEDIMENTO                            	\n"
        + "          AND PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO2.ID_LEGAME_GRUPPO_OGGETTO   \n"
        + "          AND LGO2.ID_OGGETTO  = O2.ID_OGGETTO                               \n"
        + "          AND O2.CODICE = 'ANTPR'                            				\n"
        + "    		 AND ILL2.ID_LIVELLO = L.ID_LIVELLO                            		\n"
        + "     	 AND ILL2.ID_IMPORTI_LIQUIDATI = LLIL2.ID_IMPORTI_LIQUIDATI         \n"
        + "     	 AND ILL2.ID_PROCEDIMENTO_OGGETTO = PO2.ID_PROCEDIMENTO_OGGETTO     \n"
        + "          AND PO.ID_PROCEDIMENTO_OGGETTO <>po2.id_procedimento_oggetto 		\n"
        + "     	 AND LLIL2.FLAG_ESITO_LIQUIDAZIONE <> 'R'                           \n"
        + " ) AS ANTICIPO_EROGATO   													\n"
        /*
         * +
         * "     (SELECT                                                         		\n"
         * +
         * "     ILL.IMPORTO_LIQUIDATO                                          			\n"
         * +
         * "     FROM                                                       				\n"
         * +
         * "     IUF_T_IMPORTI_LIQUIDATI ILL, IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LL3    		\n"
         * +
         * "     WHERE ILL.ID_PROCEDIMENTO_OGGETTO IN (                            		\n"
         * +
         * "     SELECT                       	                                		\n"
         * +
         * "			PO2.ID_PROCEDIMENTO_OGGETTO                                    		\n"
         * +
         * "		FROM                      	                      	                	\n"
         * +
         * "			IUF_T_PROCEDIMENTO_OGGETTO po2,                            			\n"
         * +
         * "      	IUF_R_LEGAME_GRUPPO_OGGETTO LGO2,                               	\n"
         * +
         * "     	IUF_D_OGGETTO O2,                           				        \n"
         * +
         * "			IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL2,                                 \n"
         * +
         * "			IUF_T_IMPORTI_LIQUIDATI ILL2                                        \n"
         * +
         * "     WHERE                       	                      					\n"
         * +
         * "			 PO2.ID_PROCEDIMENTO= P.ID_PROCEDIMENTO                            	\n"
         * +
         * "          AND PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO2.ID_LEGAME_GRUPPO_OGGETTO   \n"
         * +
         * "          AND LGO2.ID_OGGETTO  = O2.ID_OGGETTO                               \n"
         * +
         * "          AND O2.CODICE = 'ANTPR'                            				\n"
         * +
         * "    		 AND ILL2.ID_LIVELLO = L.ID_LIVELLO                            		\n"
         * +
         * "     	 AND ILL2.ID_IMPORTI_LIQUIDATI = LLIL2.ID_IMPORTI_LIQUIDATI         \n"
         * +
         * "     	 AND LLIL2.FLAG_ESITO_LIQUIDAZIONE <> 'R'                           \n"
         * +
         * "    )                                                                 		\n"
         * +
         * "		AND ILL.ID_LIVELLO = L.ID_LIVELLO                                       \n"
         * +
         * "     AND LL3.FLAG_ESITO_LIQUIDAZIONE <> 'R'    	 	                        \n"
         * +
         * "     AND LL3.ID_IMPORTI_LIQUIDATI =  ILL.ID_IMPORTI_LIQUIDATI                \n"
         * 
         * + " ) AS ANTICIPO_EROGATO   													\n"
         */
        + " FROM                                                                   		\n"
        + "   IUF_T_IMPORTI_LIQUIDATI IL,                                          		\n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                       		\n"
        + "   IUF_D_ESITO E,                                                       		\n"
        + "   IUF_T_PROCEDIMENTO P,                                                		\n"
        + "   IUF_T_PROCEDIM_AMMINISTRAZIO PA,                                   		\n"
        + "   IUF_T_RISORSE_LIVELLO_BANDO RLB,                                     		\n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                     		\n"
        + "   IUF_D_OGGETTO O,                                                     		\n"
        + "   IUF_D_LIVELLO L,                                                     		\n"
        + "   SMRGAA_V_DATI_ANAGRAFICI DA,                                         		\n"
        + "   IUF_T_PROCEDIMENTO_AZIENDA PAZ                                       		\n"
        + " WHERE                                                                  		\n"
        + "   NVL(RLB.DATA_FINE,SYSDATE)>=SYSDATE                                  		\n"
        + "   AND RLB.FLAG_BLOCCATO      = 'N'                                     		\n"
        + "   AND RLB.ID_BANDO           = :ID_BANDO                               		\n"
        + "   AND RLB.ID_TIPO_IMPORTO    = :ID_TIPO_IMPORTO                       		\n"
        + "   AND                                                                  \n"
        + "   (                                                                    \n"
        + "     RLB.FLAG_AMM_COMPETENZA = 'S'                                      \n"
        + "     OR EXISTS                                                          \n"
        + "     (                                                                  \n"
        + "       SELECT                                                           \n"
        + "         *                                                              \n"
        + "       FROM                                                             \n"
        + "         IUF_R_RISOR_LIV_BANDO_AMM_CO RLBAC                           \n"
        + "       WHERE                                                            \n"
        + "         RLBAC.ID_RISORSE_LIVELLO_BANDO  = RLB.ID_RISORSE_LIVELLO_BANDO \n"
        + "         AND RLBAC.EXT_ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA           \n"
        + "     )                                                                  \n"
        + "   )                                                                    \n"
        + "   AND PO.ID_ESITO = E.ID_ESITO                                         \n"
        + "   AND E.CODICE LIKE :ESITO_PRATICHE_LIQUIDABILI                        \n"
        + "   AND IL.ID_LIVELLO               = RLB.ID_LIVELLO                     \n"
        + "   AND IL.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO         \n"
        + "   AND PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO                  \n"
        + "   AND P.ID_PROCEDIMENTO           = PA.ID_PROCEDIMENTO                 \n"
        + "   AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO       \n"
        + "   AND LGO.ID_OGGETTO              = O.ID_OGGETTO                       \n"
        + "   AND IL.ID_LIVELLO               = L.ID_LIVELLO                       \n"
        + "   AND PA.DATA_FINE               IS NULL                               \n"
        + "   AND PA.EXT_ID_AMM_COMPETENZA    = :ID_AMM_COMPETENZA                 \n"
        + "   AND P.ID_BANDO                  = RLB.ID_BANDO                       \n"
        + "   AND PAZ.ID_PROCEDIMENTO         = P.ID_PROCEDIMENTO                  \n"
        + "   AND DA.ID_AZIENDA               = PAZ.EXT_ID_AZIENDA                 \n"
        + "   AND DA.DATA_FINE_VALIDITA      IS NULL                               \n"
        + "   AND PAZ.DATA_FINE              IS NULL                               \n"
        + "   AND NOT EXISTS                                                       \n"
        + "   (                                                                    \n"
        + "     SELECT                                                             \n"
        + "       *                                                                \n"
        + "     FROM                                                               \n"
        + "       IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL                                \n"
        + "     WHERE                                                              \n"
        + "       LLIL.ID_IMPORTI_LIQUIDATI = IL.ID_IMPORTI_LIQUIDATI              \n"
        + "   )                                                                    \n"

        + "       AND ( IL.RAGGRUPPAMENTO = RLB.RAGGRUPPAMENTO      			   \n"
        + "					  OR (IL.RAGGRUPPAMENTO IS NULL          			   \n"
        + "							AND RLB.RAGGRUPPAMENTO IS NULL )    )          \n"

        + " ORDER BY                                                               \n"
        + "   DA.DENOMINAZIONE ASC,                                                \n"
        + "   P.IDENTIFICATIVO ASC, CODICE_LIVELLO ASC                             \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza,
          Types.NUMERIC);
      mapParameterSource.addValue("ESITO_PRATICHE_LIQUIDABILI",
          IuffiConstants.ESITO.LISTE_LIQUIDAZIONE.LIKE_ESITO_PRATICHE_LIQUIDABILI,
          Types.VARCHAR);
      return queryForList(QUERY, mapParameterSource,
          RiepilogoPraticheApprovazioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null, (LogVariable[]) null, QUERY,
          mapParameterSource);
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

  public String getTitoloListaLiquidazioneByIdBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getTitoloListaLiquidazioneByIdBando]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT 								\n"
        + "DISTINCT 'Misura: ' || NVL(BB.CODICE_INTERVENTO_SIGOP,LIV.CODICE_LIVELLO) as RET         		\n"
        + "   FROM 												\n"
        + "		IUF_D_BANDO BB, 								\n"
        + "		IUF_R_LIVELLO_BANDO LIVB, 						\n"
        + "		IUF_D_LIVELLO W1, 								\n"
        + "		IUF_R_LIVELLO_PADRE W2, 						\n"
        + "		IUF_R_LIVELLO_PADRE W3,							\n"
        + "		IUF_D_LIVELLO LIV 								\n"
        + " WHERE W1.ID_LIVELLO = W2.ID_LIVELLO  				\n"
        + " AND W2.ID_LIVELLO_PADRE = W3.ID_LIVELLO 			\n"
        + " AND W3.ID_LIVELLO_PADRE = LIV.ID_LIVELLO  			\n"
        + " AND W1.ID_LIVELLO = LIVB.ID_LIVELLO   				\n"
        + " AND LIVB.ID_BANDO = BB.ID_BANDO   					\n"
        + " AND BB.ID_BANDO =:idBando 							\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("idBando", idBando, Types.NUMERIC);

      String res = namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {

            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              int nRecord = 0;
              String ret = "";
              while (rs.next())
              {
                {
                  ret = rs.getString("RET");
                  nRecord++;
                }

              }
              if (nRecord == 1)
                return ret;
              else
                return "********";

            }
          });
      return res;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
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

  public boolean checkTecnicoLiquidatoreNonIstruttore(long idBando,
      long idAmmCompetenza, long idTipoImporto,
      long idTecnico, Long idProcedimentoOggetto, List<Long> idsPODaEscludere,
      long idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "checkTecnicoLiquidatoreNonIstruttore";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                       \n"
        + "   TECNICO_LIQUIDATORE AS                                                   \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       CODICE_FISCALE                                                       \n"
        + "     FROM                                                                   \n"
        + "       SMRCOMUNE_V_TECNICO TEC                                              \n"
        + "     WHERE                                                                  \n"
        + "       TEC.ID_TECNICO = :ID_TECNICO                                         \n"
        + "       AND TEC.EXT_ID_PROCEDIMENTO = :EXT_ID_PROCEDIMENTO                   \n"
        + "   )                                                                        \n"
        + "   ,                                                                        \n"
        + "   PROC_OGG AS                                                              \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO                                           \n"
        + "     FROM                                                                   \n"
        + "       IUF_T_IMPORTI_LIQUIDATI IL,                                          \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                       \n"
        + "       IUF_T_PROCEDIMENTO P,                                                \n"
        + "       IUF_D_ESITO E,                                                       \n"
        + "       IUF_T_PROCEDIM_AMMINISTRAZIO PA,                                   \n"
        + "       IUF_T_RISORSE_LIVELLO_BANDO RLB                                      \n"
        + "     WHERE                                                                  \n"
        + "       NVL(RLB.DATA_FINE,SYSDATE)>=SYSDATE                                  \n"
        + "       AND RLB.FLAG_BLOCCATO      = 'N'                                     \n"
        + "       AND RLB.ID_BANDO           = :ID_BANDO                               \n"
        + "       AND RLB.ID_TIPO_IMPORTO    = :ID_TIPO_IMPORTO                        \n";
    if (idsPODaEscludere != null && !idsPODaEscludere.isEmpty())
      QUERY += getNotInCondition("PO.ID_PROCEDIMENTO_OGGETTO",
          idsPODaEscludere);
    if (idProcedimentoOggetto != null)
      QUERY += " AND PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO				   \n";
    QUERY += "       AND                                                                \n"
        + "       (                                                                    \n"
        + "         RLB.FLAG_AMM_COMPETENZA = 'S'                                      \n"
        + "         OR EXISTS                                                          \n"
        + "         (                                                                  \n"
        + "           SELECT                                                           \n"
        + "             *                                                              \n"
        + "           FROM                                                             \n"
        + "             IUF_R_RISOR_LIV_BANDO_AMM_CO RLBAC                           \n"
        + "           WHERE                                                            \n"
        + "             RLBAC.ID_RISORSE_LIVELLO_BANDO  = RLB.ID_RISORSE_LIVELLO_BANDO \n"
        + "             AND RLBAC.EXT_ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA           \n"
        + "         )                                                                  \n"
        + "       )                                                                    \n"
        + "       AND IL.ID_LIVELLO              = RLB.ID_LIVELLO                      \n"
        + "       AND IL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO          \n"
        + "       AND PO.ID_PROCEDIMENTO         = P.ID_PROCEDIMENTO                   \n"
        + "       AND P.ID_PROCEDIMENTO          = PA.ID_PROCEDIMENTO                  \n"
        + "       AND PA.DATA_FINE              IS NULL                                \n"
        + "       AND PA.EXT_ID_AMM_COMPETENZA   = :ID_AMM_COMPETENZA                  \n"
        + "       AND P.ID_BANDO                 = RLB.ID_BANDO                        \n"
        + "       AND PO.ID_ESITO                = E.ID_ESITO                          \n"
        + "       AND E.CODICE LIKE :ESITO_PRATICHE_LIQUIDABILI                        \n"
        + "       AND NOT EXISTS                                                       \n"
        + "       (                                                                    \n"
        + "         SELECT                                                             \n"
        + "           1                                                                \n"
        + "         FROM                                                               \n"
        + "           IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL                                \n"
        + "         WHERE                                                              \n"
        + "           LLIL.ID_IMPORTI_LIQUIDATI = IL.ID_IMPORTI_LIQUIDATI              \n"
        + "       )                                                                    \n"
        + "       AND                                                                  \n"
        + "       (                                                                    \n"
        + "         IL.RAGGRUPPAMENTO = RLB.RAGGRUPPAMENTO                             \n"
        + "         OR                                                                 \n"
        + "         (                                                                  \n"
        + "           IL.RAGGRUPPAMENTO      IS NULL                                   \n"
        + "           AND RLB.RAGGRUPPAMENTO IS NULL                                   \n"
        + "         )                                                                  \n"
        + "       )                                                                    \n"
        + "   )                                                                        \n"
        + "   ,                                                                        \n"
        + "   TECNICI_INVESTIMENTO AS                                                  \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       T.CODICE_FISCALE                                                     \n"
        + "     FROM                                                                   \n"
        + "       PROC_OGG PROC,                                                       \n"
        + "       IUF_T_ESITO_TECNICO ET,                                              \n"
        + "       IUF_R_QUADRO_OGGETTO QO,                                             \n"
        + "       IUF_D_QUADRO Q,                                                      \n"
        + "       SMRCOMUNE_V_TECNICO T,                                               \n"
        + "       TECNICO_LIQUIDATORE TL                                               \n"
        + "     WHERE                                                                  \n"
        + "       PROC.ID_PROCEDIMENTO_OGGETTO = ET.ID_PROCEDIMENTO_OGGETTO            \n"
        + "       AND ET.ID_QUADRO_OGGETTO     = QO.ID_QUADRO_OGGETTO                  \n"
        + "       AND QO.ID_QUADRO             = Q.ID_QUADRO                           \n"
        + "       AND Q.CODICE                IN ('ESIFN','CTRLC','CTRAM')             \n"
        + "       AND T.ID_TECNICO             = ET.EXT_ID_TECNICO                     \n"
        + "       AND TL.CODICE_FISCALE        = T.CODICE_FISCALE                      \n"
        + "       AND T.EXT_ID_PROCEDIMENTO = :EXT_ID_PROCEDIMENTO                   \n"
        + "   )                                                                        \n"
        + "   ,                                                                        \n"
        + "   TECNICI_PREMIO AS                                                        \n"
        + "   (                                                                        \n"
        + "     SELECT                                                                 \n"
        + "       T.CODICE_FISCALE                                                     \n"
        + "     FROM                                                                   \n"
        + "       PROC_OGG PROC,                                                       \n"
        + "       IUF_T_ESITO_FINALE EF,                                               \n"
        + "       SMRCOMUNE_V_TECNICO T,                                               \n"
        + "       SMRCOMUNE_V_TECNICO T2,                                              \n"
        + "       TECNICO_LIQUIDATORE TL,                                              \n"
        + "       IUF_T_ESITO_OPERAZIONE EO                                            \n"
        + "     WHERE                                                                  \n"
        + "       PROC.ID_PROCEDIMENTO_OGGETTO = EF.ID_PROCEDIMENTO_OGGETTO            \n"
        + "       AND T.ID_TECNICO             = EF.EXT_ID_FUNZIONARIO_ISTRUTTORE      \n"
        + "       AND T.EXT_ID_PROCEDIMENTO = :EXT_ID_PROCEDIMENTO                   \n"
        + "       AND T2.EXT_ID_PROCEDIMENTO = :EXT_ID_PROCEDIMENTO                   \n"
        + "       AND                                                                  \n"
        + "       (                                                                    \n"
        + "         TL.CODICE_FISCALE    = T.CODICE_FISCALE                            \n"
        + "         OR TL.CODICE_FISCALE = T2.CODICE_FISCALE                           \n"
        + "       )                                                                    \n"
        + "       AND EO.ID_ESITO_FINALE               = EF.ID_ESITO_FINALE            \n"
        + "       AND EO.EXT_ID_FUNZIONARIO_ISTRUTTORE = T2.ID_TECNICO                 \n"
        + "   )   ,                                                                     \n"
        // nuovo controllo TECNICI_VISITA_LUOGO
        + "   TECNICI_CONTOLLO_LOCO AS                                                  		\n"
        + "   (                                                                        		\n"
        + "     SELECT                                                                 		\n"
        + "       T.CODICE_FISCALE                                                     		\n"
        + "     FROM                                                                   		\n"
        + "       PROC_OGG PROC,                                                       		\n"
        + "       IUF_T_CONTROLLO_IN_LOCO CL,                                              	\n"
        + "       SMRCOMUNE_V_TECNICO T,                                               		\n"
        + "       TECNICO_LIQUIDATORE TL                                               		\n"
        + "     WHERE                                                                  		\n"
        + "       T.EXT_ID_PROCEDIMENTO = :EXT_ID_PROCEDIMENTO                   \n"
        + "       AND CL.ID_PROCEDIMENTO_OGGETTO IN (                                    	  		\n"
        + "			SELECT ID_PROCEDIMENTO_OGGETTO                                    		\n"
        + "			FROM IUF_T_PROCEDIMENTO_OGGETTO PO                                    	\n"
        + "			WHERE PO.ID_PROCEDIMENTO = (                                   			\n"
        + "				SELECT ID_PROCEDIMENTO                                    			\n"
        + "				FROM IUF_T_PROCEDIMENTO_OGGETTO                                    	\n"
        + "				WHERE ID_PROCEDIMENTO_OGGETTO = PROC.ID_PROCEDIMENTO_OGGETTO)       \n"
        + "       AND CL.EXT_ID_TECNICO = T.ID_TECNICO    		                 			\n"
        + "       AND TL.CODICE_FISCALE = T.CODICE_FISCALE                     				\n"
        + "   )  ),                                                                      		\n"
        // FINE CONTROLLO TECN_IN_LOCO
        // nuovo controllo TECNICI_VISITA_LUOGO
        + "   TECNICI_VISITA_LUOGO AS                                                  		\n"
        + "   (                                                                        		\n"
        + "     SELECT                                                                 		\n"
        + "       T.CODICE_FISCALE                                                     		\n"
        + "     FROM                                                                   		\n"
        + "       PROC_OGG PROC,                                                       		\n"
        + "       IUF_T_VISITA_LUOGO  VL,                                              		\n"
        + "       SMRCOMUNE_V_TECNICO T,                                               		\n"
        + "       TECNICO_LIQUIDATORE TL                                               		\n"
        + "     WHERE                                                                  		\n"
        + "       T.EXT_ID_PROCEDIMENTO = :EXT_ID_PROCEDIMENTO                   \n"
        + "       AND VL.ID_PROCEDIMENTO_OGGETTO IN (                                    			\n"
        + "			SELECT ID_PROCEDIMENTO_OGGETTO                                  		\n"
        + "			FROM IUF_T_PROCEDIMENTO_OGGETTO PO                                    	\n"
        + "			WHERE PO.ID_PROCEDIMENTO = (                                   			\n"
        + "				SELECT ID_PROCEDIMENTO                                    			\n"
        + "				FROM IUF_T_PROCEDIMENTO_OGGETTO                                    	\n"
        + "				WHERE ID_PROCEDIMENTO_OGGETTO = PROC.ID_PROCEDIMENTO_OGGETTO)       \n"
        + "       AND VL.EXT_ID_TECNICO       = T.ID_TECNICO    		                 		\n"
        + "       	AND T.CODICE_FISCALE       = TL.CODICE_FISCALE              	       		\n"
        + "   )   )                                                                     		\n"
        // FINE CONTROLLO tecnico_VISITA_LUOGO
        + " SELECT                                                                     	\n"
        + "   COUNT(*)                                                                 	\n"
        + " FROM                                                                       	\n"
        + "   (                                                                        	\n"
        + "     SELECT                                                                 	\n"
        + "       1                                                                    	\n"
        + "     FROM                                                                   	\n"
        + "       TECNICI_PREMIO                                                       	\n"
        + "     UNION                                                                  	\n"
        + "     SELECT                                                                 	\n"
        + "       1                                                                    	\n"
        + "     FROM                                                                   	\n"
        + "       TECNICI_INVESTIMENTO                                                 	\n"
        + "     UNION                                                                  	\n"
        + "     SELECT                                                                 	\n"
        + "       1                                                                    	\n"
        + "     FROM                                                                   	\n"
        + "       TECNICI_CONTOLLO_LOCO                                                	\n"
        + "   )                                                                        	\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
        Types.NUMERIC);
    if (idProcedimentoOggetto != null)
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);

    mapParameterSource.addValue("ID_TECNICO", idTecnico, Types.NUMERIC);
    mapParameterSource.addValue("ESITO_PRATICHE_LIQUIDABILI",
        IuffiConstants.ESITO.LISTE_LIQUIDAZIONE.LIKE_ESITO_PRATICHE_LIQUIDABILI,
        Types.VARCHAR);
    mapParameterSource.addValue("EXT_ID_PROCEDIMENTO", idProcedimentoAgricolo, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.queryForLong(QUERY,
          mapParameterSource) == 0;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBando", idBando),
              new LogParameter("idAmmCompetenza", idAmmCompetenza),
              new LogParameter("idTipoImporto", idTipoImporto),
              new LogParameter("idTecnico", idTecnico),
              new LogParameter("idProcedimentoAgricolo", idProcedimentoAgricolo)
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

  public void scriviEccezioneAgriwell(long idListaLiquidazione,
      Exception exception) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "scriviEccezioneAgriwell";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " UPDATE                                           \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION                  \n"
        + " SET                                              \n"
        + "   DESC_ANOMALIA = :DESCRIZIONE_ERRORE            \n"
        + " WHERE                                            \n"
        + "   ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      if (exception instanceof ApplicationException)
      {
        mapParameterSource.addValue("DESCRIZIONE_ERRORE",
            IuffiUtils.STRING.substr(exception.getMessage(), 0, 4000));
      }
      else
      {
        mapParameterSource.addValue("DESCRIZIONE_ERRORE",
            IuffiUtils.STRING
                .substr(DumpUtils.getStackTraceAsString(exception), 0, 4000));
      }
      mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Exception t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
              new LogParameter("stampaFirmata", idListaLiquidazione),
          },
          null, QUERY, mapParameterSource);
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

  public boolean isListaLiquidazioneCorrotta(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "isListaLiquidazioneCorrotta";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                            \n"
        + "   COUNT(*)                                                        \n"
        + " FROM                                                              \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL,                                    \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION FLL                               \n"
        + " WHERE                                                             \n"
        + "   LL.ID_LISTA_LIQUIDAZIONE           =  :ID_LISTA_LIQUIDAZIONE    \n"
        + "   AND LL.ID_LISTA_LIQUIDAZIONE       =  FLL.ID_LISTA_LIQUIDAZIONE \n"
        + "   AND LL.FLAG_STATO_LISTA            <> :FLAG_STATO_LISTA         \n"
        + "   AND "
        + "   ("
        + "     FLL.DATA_PROTOCOLLO            IS NOT NULL                      \n"
        + "     OR FLL.DATA_PROTOCOLLO_EMERGENZA   IS NOT NULL                  \n"
        + "     OR FLL.NUMERO_PROTOCOLLO           IS NOT NULL                  \n"
        + "     OR FLL.NUMERO_PROTOCOLLO_EMERGENZA IS NOT NULL                  \n"
        + "     OR FLL.EXT_ID_DOCUMENTO_INDEX      IS NOT NULL                  \n"
        + "    )                                                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    mapParameterSource.addValue("FLAG_STATO_LISTA",
        IuffiConstants.STATO.LISTE_LIQUIDAZIONE.FLAG.APPROVATA,
        Types.VARCHAR);
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
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
          },
          new LogVariable[]
          {
          }, QUERY, mapParameterSource);
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

  public String getEmailAmmCompetenzaLista(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "isListaLiquidazioneCorrotta";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                 \n"
        + "   AC.EMAIL                                             \n"
        + " FROM                                                   \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE L,                          \n"
        + "   SMRCOMUNE_V_AMM_COMPETENZA AC                        \n"
        + " WHERE                                                  \n"
        + "   L.EXT_ID_AMM_COMPETENZA     = AC.ID_AMM_COMPETENZA   \n"
        + "   AND L.ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    mapParameterSource.addValue("FLAG_STATO_LISTA",
        IuffiConstants.STATO.LISTE_LIQUIDAZIONE.FLAG.APPROVATA,
        Types.VARCHAR);
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
              new LogParameter("idListaLiquidazione", idListaLiquidazione),
          },
          new LogVariable[]
          {
          }, QUERY, mapParameterSource);
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

  public boolean isContenutoFileListaLiquidazioneDisponibile(
      long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getContenutoFileListaLiquidazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
      logger
          .debug(THIS_METHOD + " idListaLiquidazione = " + idListaLiquidazione);
    }
    final String QUERY = " SELECT                                           \n"
        + "   COUNT(*)                                       \n"
        + " FROM                                             \n"
        + "   IUF_T_FILE_LISTA_LIQUIDAZION                  \n"
        + " WHERE                                            \n"
        + "   ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE \n"
        + "   AND                                            \n"
        + "   (                                              \n"
        + "     EXT_ID_DOCUMENTO_INDEX IS NOT NULL           \n"
        + "     OR FILE_ALLEGATO       IS NOT NULL           \n"
        + "   )                                              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.queryForLong(QUERY,
          mapParameterSource) > 0;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idListaLiquidazione", idListaLiquidazione)
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

  public long getIdBandoByIdListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {

    final String THIS_METHOD = "getIdBandoByIdListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                     \n"
        + "   LL.ID_BANDO					                    \n"
        + " FROM                                                \n"
        + "   IUF_T_LISTA_LIQUIDAZIONE LL                       \n"
        + " WHERE                                               \n"
        + "   LL.ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
        Types.NUMERIC);
    try
    {
      return queryForLongNull(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          // new LogParameter("idVariabile", idVariabile),
          },
          new LogVariable[]
          {
          // new LogParameter("idVariabile", idVariabile),
          }, QUERY, mapParameterSource);
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

  public List<RiepilogoPraticheApprovazioneDTO> getRiepilogoPraticheListaLiquidazione(
      long idListaLiquidazione, boolean isPremio)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRiepilogoPraticheListaLiquidazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT DISTINCT                                                    \n"
        + "   DA.CUAA,                                                             		\n"
        + "   DA.DENOMINAZIONE,                                                    		\n"
        + "   P.IDENTIFICATIVO,                                                    		\n"
        + "   P.ID_PROCEDIMENTO,                                                   		\n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO,                                          		\n"
        + "   L.CODICE AS CODICE_LIVELLO,                                          		\n"
        + "   O.CAUSALE_PAGAMENTO,                                                 		\n"
        + "   IL.IMPORTO_LIQUIDATO                                                		\n";

    if (isPremio)
      QUERY += "	  ,IL.IMPORTO_COMPLESSIVO AS IMPORTO_PREMIO,                        \n"
          + "     ("
          /*
           * +
           * "SELECT                                                         		\n"
           * +
           * "     ILL.IMPORTO_LIQUIDATO                                          			\n"
           * +
           * "     FROM                                                       				\n"
           * +
           * "     IUF_T_IMPORTI_LIQUIDATI ILL, IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LL3    		\n"
           * +
           * "     WHERE ILL.ID_PROCEDIMENTO_OGGETTO IN (                            		\n"
           * +
           * "     SELECT                       	                                		\n"
           * +
           * "			PO2.ID_PROCEDIMENTO_OGGETTO                                    		\n"
           * +
           * "		FROM                      	                      	                	\n"
           * +
           * "			IUF_T_PROCEDIMENTO_OGGETTO po2,                            			\n"
           * +
           * "      	IUF_R_LEGAME_GRUPPO_OGGETTO LGO2,                               	\n"
           * +
           * "     	IUF_D_OGGETTO O2                           				        \n"
           * +
           * "			,IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL2,                                 \n"
           * +
           * "			IUF_T_IMPORTI_LIQUIDATI ILL2                                        \n"
           * +
           * "     WHERE                       	                      					\n"
           * +
           * "			 PO2.ID_PROCEDIMENTO= P.ID_PROCEDIMENTO                            	\n"
           * +
           * "          AND PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO2.ID_LEGAME_GRUPPO_OGGETTO   \n"
           * +
           * "          AND LGO2.ID_OGGETTO  = O2.ID_OGGETTO                               \n"
           * +
           * "          AND O2.CODICE = 'ANTPR'                            				\n"
           * +
           * "    		 AND ILL2.ID_LIVELLO = L.ID_LIVELLO                            		\n"
           * +
           * "     	 AND ILL2.ID_IMPORTI_LIQUIDATI = LLIL2.ID_IMPORTI_LIQUIDATI         \n"
           * +
           * "     	 AND LLIL2.FLAG_ESITO_LIQUIDAZIONE <> 'R'                           \n"
           * +
           * "    )                                                                 		\n"
           * +
           * "		AND ILL.ID_LIVELLO = L.ID_LIVELLO                                       \n"
           * +
           * "     AND LL3.FLAG_ESITO_LIQUIDAZIONE <> 'R'    	 	                        \n"
           * +
           * "     AND LL3.ID_IMPORTI_LIQUIDATI =  ILL.ID_IMPORTI_LIQUIDATI                \n"
           */
          + "SELECT                                                         		\n"
          + "     ILL2.IMPORTO_LIQUIDATO                                          			\n"
          + "		FROM                      	                      	                	\n"
          + "			IUF_T_PROCEDIMENTO_OGGETTO po2,                            			\n"
          + "      	IUF_R_LEGAME_GRUPPO_OGGETTO LGO2,                               	\n"
          + "     	IUF_D_OGGETTO O2                           				        	\n"
          + "			,IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL2,                                \n"
          + "			IUF_T_IMPORTI_LIQUIDATI ILL2                                        \n"
          + "     WHERE                       	                      					\n"
          + "			 PO2.ID_PROCEDIMENTO= P.ID_PROCEDIMENTO                            	\n"
          + "          AND PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO2.ID_LEGAME_GRUPPO_OGGETTO   \n"
          + "          AND LGO2.ID_OGGETTO  = O2.ID_OGGETTO                               \n"
          + "          AND O2.CODICE = 'ANTPR'                            				\n"
          + "    		 AND ILL2.ID_LIVELLO = L.ID_LIVELLO                            		\n"
          + "     	 AND ILL2.ID_IMPORTI_LIQUIDATI = LLIL2.ID_IMPORTI_LIQUIDATI         \n"
          + "     	 AND ILL2.ID_PROCEDIMENTO_OGGETTO = PO2.ID_PROCEDIMENTO_OGGETTO     \n"
          + "          AND PO.ID_PROCEDIMENTO_OGGETTO <>po2.id_procedimento_oggetto 		\n"
          + "     	 AND LLIL2.FLAG_ESITO_LIQUIDAZIONE <> 'R'                           \n"
          + " ) AS ANTICIPO_EROGATO   													\n";
    QUERY += " FROM                                                                  \n"
        + "   IUF_T_IMPORTI_LIQUIDATI IL,                                          		\n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                                       		\n"
        + "   IUF_T_PROCEDIMENTO P,                                                		\n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                     		\n"
        + "   IUF_D_OGGETTO O,                                                     		\n"
        + "   IUF_D_LIVELLO L,                                                     		\n"
        + "   SMRGAA_V_DATI_ANAGRAFICI DA,												\n"
        + "	  IUF_T_LISTA_LIQUIDAZIONE LIQ,                                        		\n"
        + "   IUF_T_PROCEDIMENTO_AZIENDA PAZ,                                     		\n"
        + "	  IUF_R_LISTA_LIQUIDAZ_IMP_LIQ LLIQ                                        	\n"
        + " WHERE                                                                  		\n"
        + "   LIQ.ID_LISTA_LIQUIDAZIONE = :ID_LISTA_LIQUIDAZIONE                   		\n"
        + "   AND LLIQ.ID_LISTA_LIQUIDAZIONE = LIQ.ID_LISTA_LIQUIDAZIONE               	\n"
        + "   AND IL.ID_IMPORTI_LIQUIDATI = LLIQ.ID_IMPORTI_LIQUIDATI               	\n"
        + "   AND IL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO               \n"
        + "   AND PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO                  		\n"
        + "   AND PAZ.ID_PROCEDIMENTO         = P.ID_PROCEDIMENTO                  		\n"
        + "   AND DA.ID_AZIENDA               = PAZ.EXT_ID_AZIENDA                 		\n"
        + "   AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO       		\n"
        + "   AND LGO.ID_OGGETTO              = O.ID_OGGETTO                       		\n"
        + "   AND IL.ID_LIVELLO               = L.ID_LIVELLO                       		\n"
        + "   AND DA.DATA_FINE_VALIDITA      IS NULL                               		\n"
        + "   AND PAZ.DATA_FINE              IS NULL                               		\n"
        + " ORDER BY                                                               		\n"
        + "   DA.DENOMINAZIONE ASC,                                                		\n"
        + "   P.IDENTIFICATIVO ASC, CODICE_LIVELLO ASC                             		\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_LISTA_LIQUIDAZIONE", idListaLiquidazione,
          Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource,
          RiepilogoPraticheApprovazioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null, (LogVariable[]) null, QUERY,
          mapParameterSource);
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

  public List<LivelloDTO> getElencoLivelli() throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoLivelli";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                          		    	\n"
          + " L.DESCRIZIONE AS  DESCRIZIONE,				\n"
          + " L.ID_LIVELLO AS ID,							\n"
          + " L.CODICE AS CODICE, 							\n"
          + " L.CODICE_LIVELLO  							\n"
          + " FROM    										\n"
          + "	IUF_D_BANDO A,								\n"
          + "   IUF_D_TIPO_LIVELLO B,   					\n"
          + "   IUF_R_LIVELLO_BANDO LB,					 	\n"
          + "	IUF_D_LIVELLO L                  	   		\n"
          + " WHERE  										\n"
          + "    B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO 		\n"
          + "   AND LB.ID_BANDO = A.ID_BANDO				\n"
          + "   AND L.ID_LIVELLO  = LB.ID_LIVELLO   		\n"
          + "	AND A.FLAG_MASTER <> 'S'                   	\n"
          + "	AND L.ID_TIPOLOGIA_LIVELLO = 3             	\n"
          + " ORDER BY ORDINAMENTO                         	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> livelli = new LinkedList<LivelloDTO>();
              LivelloDTO l = null;
              String misura = "";
              String sottoMisura = "";
              while (rs.next())
              {
                misura = rs.getString("CODICE_LIVELLO").split("\\.")[0];
                l = new LivelloDTO();
                if (rs.getString("CODICE_LIVELLO").split("\\.").length > 1)
                {
                  sottoMisura = rs.getString("CODICE_LIVELLO").split("\\.")[1];
                  l.setCodiceSottoMisura(misura + "." + sottoMisura);
                }

                l.setIdLivello(rs.getLong("ID"));
                l.setDescrizione(rs.getString("DESCRIZIONE"));
                l.setCodice(rs.getString("CODICE"));
                l.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                l.setCodiceMisura(misura);

                livelli.add(l);
              }
              if (livelli.isEmpty())
                return null;

              return livelli;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
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

  public List<LivelloDTO> getElencoLivelliByIdBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoLivelliByIdBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                          		    	\n"
          + " L.DESCRIZIONE AS  DESCRIZIONE,				\n"
          + " L.ID_LIVELLO AS ID,							\n"
          + " L.CODICE AS CODICE, 							\n"
          + " L.CODICE_LIVELLO  							\n"
          + " FROM    										\n"
          + "	IUF_D_BANDO A,								\n"
          + "   IUF_D_TIPO_LIVELLO B,   					\n"
          + "   IUF_R_LIVELLO_BANDO LB,					 	\n"
          + "	IUF_D_LIVELLO L                  	   		\n"
          + " WHERE  										\n"
          + "    B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO 		\n"
          + "   AND LB.ID_BANDO = A.ID_BANDO				\n"
          + "   AND L.ID_LIVELLO  = LB.ID_LIVELLO   		\n"
          + "	AND A.FLAG_MASTER <> 'S'                   	\n"
          + "	AND L.ID_TIPOLOGIA_LIVELLO = 3             	\n"
          + "   AND LB.ID_BANDO = :ID_BANDO					\n"
          + " ORDER BY ORDINAMENTO                         	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> livelli = new LinkedList<LivelloDTO>();
              LivelloDTO l = null;
              String misura = "";
              String sottoMisura = "";
              while (rs.next())
              {
                misura = rs.getString("CODICE_LIVELLO").split("\\.")[0];
                l = new LivelloDTO();
                if (rs.getString("CODICE_LIVELLO").split("\\.").length > 1)
                {
                  sottoMisura = rs.getString("CODICE_LIVELLO").split("\\.")[1];
                  l.setCodiceSottoMisura(misura + "." + sottoMisura);
                }

                l.setIdLivello(rs.getLong("ID"));
                l.setDescrizione(rs.getString("DESCRIZIONE"));
                l.setCodice(rs.getString("CODICE"));
                l.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                l.setCodiceMisura(misura);

                livelli.add(l);
              }
              if (livelli.isEmpty())
                return null;

              return livelli;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
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
  
  public String getCodClassLiquidazione(
      int idProcedimentoAgricolo, long idBando) throws InternalUnexpectedException
  {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String  QUERY = " SELECT "
        + "   NVL( (SELECT COD_CLASS_LIQUIDAZIONE FROM IUF_D_BANDO WHERE ID_BANDO=:ID_BANDO), COD_CLASS_LIQUIDAZIONE ) as COD_CLASS_LIQUIDAZIONE                  "
        + " FROM IUF_D_PROCEDIMENTO_AGRICOLO WHERE ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO " 
        
      ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo, Types.NUMERIC);
      return queryForString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {
                  new LogParameter("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo)
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
}
