package it.csi.iuffi.iuffiweb.integration;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.ElencoQueryBandoDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.reportistica.CellReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ColReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.GraficoVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ParametriQueryReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.RowsReportVO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public class ReportisticaDAO extends BaseDAO
{

  private static final String THIS_CLASS = ReportisticaDAO.class
      .getSimpleName();

  public ReportisticaDAO()
  {
  }

  public ReportVO getReportBando(ParametriQueryReportVO parametri)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getReportBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = parametri.getIstruzioneSQL().toUpperCase();
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      if (SELECT.contains(":ID_BANDO"))
        mapParameterSource.addValue("ID_BANDO", parametri.getIdBando(),
            Types.NUMERIC);
      if (SELECT.contains(":COD_ENTE_CAA"))
        mapParameterSource.addValue("COD_ENTE_CAA", parametri.getCodEnteCaa(),
            Types.VARCHAR);
      if (SELECT.contains(":IN_CONDITION_ID_PROCEDIMENTI"))
        SELECT = SELECT.replace(":IN_CONDITION_ID_PROCEDIMENTI", getInCondition(
            "P.ID_PROCEDIMENTO", parametri.getlIdsProcedimenti()));
      if(SELECT.contains(":COD_ENTI_ABILITATI"))
        SELECT = SELECT.replace(":COD_ENTI_ABILITATI", parametri.getCodEntiAbilitati());

      final long idTipoVisualizzazione = parametri.getIdTipoVisualizzazione();
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<ReportVO>()
          {
            @Override
            public ReportVO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              if (idTipoVisualizzazione == 4)
              {
                return elabReportProiezione(rs);
              }
              else
                if (idTipoVisualizzazione == 6)
                {
                  return elabReportIstogrammaStacked(rs);
                }
                else
                {
                  return elabReport(rs);
                }
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

  private ReportVO elabReportProiezione(ResultSet rs) throws SQLException
  {
    ReportVO reportVO = new ReportVO();
    ColReportVO colReportVO = null;
    CellReportVO cellReportVO = null;
    List<CellReportVO> listRow = null;
    RowsReportVO rowsReportVO = null;
    String valRiga;
    String valColonna;
    List<String> asseX = new ArrayList<String>();
    List<String> asseY = new ArrayList<String>();

    Vector<ColReportVO> colsDefinitions = new Vector<ColReportVO>();
    Vector<RowsReportVO> rowValues = new Vector<RowsReportVO>();

    // Creo la lista delle colonne ed elaboro le righe
    ResultSetMetaData rsmd = rs.getMetaData();
    // la prima colonna la leggo normalmente
    colReportVO = new ColReportVO();
    colReportVO.setId(rsmd.getColumnName(1));
    colReportVO.setLabel(rsmd.getColumnName(1));
    colReportVO.setType(getColumnTypeStr(rsmd.getColumnType(1)));
    colsDefinitions.add(colReportVO);

    while (rs.next())
    {

      // Gestisco righe
      valRiga = rs.getString(1);
      if (!asseX.contains(valRiga))
      {
        // In questo caso creo la riga
        asseX.add(valRiga);
        rowsReportVO = new RowsReportVO();
        listRow = new Vector<CellReportVO>();
        cellReportVO = new CellReportVO();
        cellReportVO.setV(rs.getObject(1));
        listRow.add(cellReportVO);
        rowsReportVO.addRowReport(listRow);
        rowValues.add(rowsReportVO);
      }
      cellReportVO = new CellReportVO();
      cellReportVO.setV(rs.getObject(3));
      listRow.add(cellReportVO);

      // Gestisco colonne
      valColonna = rs.getString(2);
      if (asseY.contains(valColonna))
      {
        // non devo fare nulla
      }
      else
      {
        asseY.add(valColonna);
        colReportVO = new ColReportVO();
        colReportVO.setId(rs.getString(2));
        colReportVO.setLabel(rs.getString(2));
        colReportVO.setType(ColReportVO.TYPE_NUMBER);
        colsDefinitions.add(colReportVO);
      }
    }

    reportVO.setColsDefinitions(colsDefinitions);
    reportVO.setRowValues(rowValues);
    return reportVO;
  }

  private ReportVO elabReportIstogrammaStacked(ResultSet rs) throws SQLException
  {
    ReportVO reportVO = new ReportVO();
    ColReportVO colReportVO = null;
    ColReportVO colReportVORef = null;
    CellReportVO cellReportVO = null;
    List<CellReportVO> listRow = null;
    RowsReportVO rowsReportVO = null;
    String valRiga;
    String valColonna;
    List<DecodificaDTO<String>> allRecords = new ArrayList<>();

    TreeMap<String, Long> mapColsIndex = new TreeMap<String, Long>();

    List<String> asseX = new ArrayList<String>();
    List<String> asseY = new ArrayList<String>();

    List<ColReportVO> colsDefinitions = new ArrayList<ColReportVO>();
    Vector<RowsReportVO> rowValues = new Vector<RowsReportVO>();

    // Creo la lista delle colonne ed elaboro le righe
    ResultSetMetaData rsmd = rs.getMetaData();
    // la prima colonna la leggo normalmente
    colReportVORef = new ColReportVO();
    colReportVORef.setId(rsmd.getColumnName(1));
    colReportVORef.setLabel(rsmd.getColumnName(1));
    colReportVORef.setType(getColumnTypeStr(rsmd.getColumnType(1)));

    long count = 1;

    while (rs.next())
    {
      allRecords.add(new DecodificaDTO<String>(rs.getString(1), rs.getString(2),
          rs.getString(3)));

      // Gestisco colonne
      valColonna = rs.getString(2);
      if (asseY.contains(valColonna))
      {
        // non devo fare nulla
      }
      else
      {
        asseY.add(valColonna);
        mapColsIndex.put(rs.getString(2), count);
        count++;
        colReportVO = new ColReportVO();
        colReportVO.setId(rs.getString(2));
        colReportVO.setLabel(rs.getString(2));
        colReportVO.setType(ColReportVO.TYPE_NUMBER);
        colsDefinitions.add(colReportVO);
      }
    }
    // Colonna per i totale
    /*
     * colReportVO = new ColReportVO(); colReportVO.setId("zztotale");
     * colReportVO.setLabel("zztotale"); colReportVO.setP(null, true);
     * colReportVO.setType(ColReportVO.TYPE_NUMBER);
     * colsDefinitions.add(colReportVO);
     */
    Collections.sort(colsDefinitions, new Comparator<ColReportVO>()
    {
      @Override
      public int compare(ColReportVO o1, ColReportVO o2)
      {
        return o1.getLabel().compareTo(o2.getLabel());
      }
    });

    /*
     * A questo punto è necessario "tappare i buchi", cioè andare a inserire
     * valori a 0 in modo che ogni valore dell'asseY abbia lo stesso numero di
     * valori della legenda (e nello stesso ordine)
     */

    if (allRecords != null)
    {
      HashMap<String, String> mapSuccessivo = new HashMap<>();

      for (int i = 0; i < colsDefinitions.size(); i++)
      {
        if (i == colsDefinitions.size() - 1)
        {
          mapSuccessivo.put(colsDefinitions.get(i).getLabel(), null);
        }
        else
        {
          mapSuccessivo.put(colsDefinitions.get(i).getLabel(),
              colsDefinitions.get(i + 1).getLabel());
        }
      }

      DecodificaDTO<String> row = null;
      for (int i = 0; i < allRecords.size(); i++)
      {
        row = allRecords.get(i);

        if (i < allRecords.size() - 1)
        {
          if (allRecords.get(i + 1).getId().equals(row.getId())
              && !allRecords.get(i + 1).getCodice()
                  .equals(mapSuccessivo.get(row.getCodice())))
          {
            allRecords.add(i + 1, new DecodificaDTO<String>(row.getId(),
                mapSuccessivo.get(row.getCodice()), "0"));
          }

          else
            if (!allRecords.get(i + 1).getId().equals(row.getId())
                && mapSuccessivo.get(row.getCodice()) != null)
            {
              allRecords.add(i + 1, new DecodificaDTO<String>(row.getId(),
                  mapSuccessivo.get(row.getCodice()), "0"));
              i--;
            }
        }
      }
      for (int i = 0; i < allRecords.size(); i++)
      {
        row = allRecords.get(i);
        if (row.getId().contains("VCO"))
        {
          row = allRecords.get(i);
        }
        // Gestisco righe
        valRiga = row.getId();
        if (!asseX.contains(valRiga))
        {
          /*
           * if(listRow!=null) { cellReportVO = new CellReportVO();
           * cellReportVO.setV(totaleColonna3); listRow.add(cellReportVO);
           * totaleColonna3 = 0; }
           */
          // In questo caso creo la riga
          asseX.add(valRiga);
          rowsReportVO = new RowsReportVO();
          listRow = new Vector<CellReportVO>();
          cellReportVO = new CellReportVO();
          cellReportVO.setV(row.getId());
          listRow.add(cellReportVO);
          rowsReportVO.addRowReport(listRow);
          rowValues.add(rowsReportVO);
        }

        cellReportVO = new CellReportVO();
        cellReportVO.setV(row.getDescrizione());
        listRow.add(cellReportVO);
      }
    }
    colsDefinitions.add(0, colReportVORef);
    reportVO.setColsDefinitions(colsDefinitions);
    reportVO.setRowValues(rowValues);
    return reportVO;
  }

  private String getColumnTypeStr(int type)
  {
    switch (type)
    {
      case Types.VARCHAR:
        return ColReportVO.TYPE_STRING;
      case Types.BOOLEAN:
        return ColReportVO.TYPE_BOOLEAN;
      case Types.DATE:
        return ColReportVO.TYPE_DATE;
      case Types.TIMESTAMP:
        return ColReportVO.TYPE_DATETIME;
      case Types.NUMERIC:
        return ColReportVO.TYPE_NUMBER;
      default:
        return ColReportVO.TYPE_STRING;
    }
  }

  private ReportVO elabReport(ResultSet rs) throws SQLException
  {
    ReportVO reportVO = new ReportVO();
    ColReportVO colReportVO = null;
    CellReportVO cellReportVO = null;
    List<CellReportVO> listRow = null;
    RowsReportVO rowsReportVO = null;

    Vector<ColReportVO> colsDefinitions = new Vector<ColReportVO>();
    Vector<RowsReportVO> rowValues = new Vector<RowsReportVO>();

    // Creo la lista delle colonne
    ResultSetMetaData rsmd = rs.getMetaData();

    for (int i = 1; i <= rsmd.getColumnCount(); i++)
    {
      colReportVO = new ColReportVO();
      colReportVO.setId(rsmd.getColumnName(i));
      colReportVO.setLabel(rsmd.getColumnName(i));
      colReportVO.setType(getColumnTypeStr(rsmd.getColumnType(i)));
      colsDefinitions.add(colReportVO);
    }

    while (rs.next())
    {
      rowsReportVO = new RowsReportVO();
      listRow = new Vector<CellReportVO>();

      for (int i = 1; i <= colsDefinitions.size(); i++)
      {
        cellReportVO = new CellReportVO();
        cellReportVO.setV(rs.getObject(i));
        listRow.add(cellReportVO);
      }

      rowsReportVO.addRowReport(listRow);
      rowValues.add(rowsReportVO);
    }

    reportVO.setColsDefinitions(colsDefinitions);
    reportVO.setRowValues(rowValues);
    return reportVO;
  }

  public List<DecodificaDTO<String>> elencoQueryBando(long idBando,
      boolean flagElenco, String attore) throws InternalUnexpectedException
  {
    String THIS_METHOD = "elencoQueryBando";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT.append(
          " SELECT                                                     \n"
              + "  A.ID_ELENCO_QUERY AS ID,                                  \n"
              + "  B.DESCRIZIONE_BREVE AS CODICE,                            \n"
              + "  B.DESCRIZIONE_ESTESA AS DESCRIZIONE                       \n"
              + " FROM                                                       \n"
              + "  IUF_R_ELENCO_QUERY_BANDO A,                               \n"
              + "  IUF_D_ELENCO_QUERY B,                                     \n"
              + "  IUF_D_TIPO_VISUALIZZAZIONE C                              \n"
              + " WHERE                                                      \n"
              + "  A.ID_BANDO = :ID_BANDO 									 \n"
              + "  AND A.EXT_COD_ATTORE = :ATTORE                            \n"
              + "  AND A.FLAG_VISIBILE = 'S'				                 \n"
              + "  AND A.ID_ELENCO_QUERY = B.ID_ELENCO_QUERY                 \n"
              + "  AND B.ID_TIPO_VISUALIZZAZIONE = C.ID_TIPO_VISUALIZZAZIONE \n");
      if (flagElenco)
      {
        SELECT.append("  AND C.FLAG_ELENCO = 'S'                   \n");
      }
      else
      {
        SELECT.append("  AND C.FLAG_ELENCO = 'N'                   \n");
      }
      SELECT.append(" ORDER BY A.ORDINAMENTO                         \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ATTORE", attore, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<DecodificaDTO<String>>>()
          {
            @Override
            public List<DecodificaDTO<String>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DecodificaDTO<String>> res = null;
              while (rs.next())
              {
                if (res == null)
                {
                  res = new Vector<DecodificaDTO<String>>();
                }
                res.add(new DecodificaDTO<String>(rs.getString("ID"),
                    rs.getString("CODICE"), rs.getString("DESCRIZIONE")));
              }
              return res;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT.toString());
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

  public GraficoVO getDatiGrafico(long idElencoQuery)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getDatiGrafico";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                  \n"
          + "   A.DESCRIZIONE_BREVE,                                  \n"
          + "   A.DESCRIZIONE_ESTESA,                                 \n"
          + "   A.ISTRUZIONE_SQL,                                     \n"
          + "   A.ID_TIPO_VISUALIZZAZIONE,                            \n"
          + "   B.DESCRIZIONE                                         \n"
          + " FROM                                                    \n"
          + "   IUF_D_ELENCO_QUERY A,                                 \n"
          + "   IUF_D_TIPO_VISUALIZZAZIONE B                          \n"
          + " WHERE                                                   \n"
          + "   A.ID_TIPO_VISUALIZZAZIONE = B.ID_TIPO_VISUALIZZAZIONE \n"
          + "   AND A.ID_ELENCO_QUERY = :ID_ELENCO_QUERY              \n";

      mapParameterSource.addValue("ID_ELENCO_QUERY", idElencoQuery,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<GraficoVO>()
          {
            @Override
            public GraficoVO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              GraficoVO graficoVO = new GraficoVO();
              graficoVO.setIdElencoQuery(idElencoQuery);
              while (rs.next())
              {
                graficoVO.setDescrBreve(rs.getString("DESCRIZIONE_BREVE"));
                graficoVO.setDescrEstesa(rs.getString("DESCRIZIONE_ESTESA"));
                graficoVO.setIstruzioneSQL(rs.getString("ISTRUZIONE_SQL"));
                graficoVO.setIdTipoVisualizzazione(
                    rs.getLong("ID_TIPO_VISUALIZZAZIONE"));
                graficoVO
                    .setDescrTipoVisualizzazione(rs.getString("DESCRIZIONE"));
              }
              return graficoVO;
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

  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getInformazioniBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                               \n"
          + "   B.ID_BANDO,                                                        \n"
          + "   B.ID_TIPO_LIVELLO,                                                 \n"
          + "   B.REFERENTE_BANDO,                                                 \n"
          + "   B.EMAIL_REFERENTE_BANDO,                                           \n"
          + "   B.FLAG_RIBASSO_INTERVENTI,                                         \n"
          + "   B.FLAG_MASTER,                                                     \n"
          + "   B.FLAG_DOMANDA_MULTIPLA,                                           \n"
          + "   B.ISTRUZIONE_SQL_FILTRO,                                           \n"
          + "   B.DESCRIZIONE_FILTRO,                                              \n"
          + "   DECODE(B.FLAG_MASTER,'S',NULL,B.DENOMINAZIONE) AS DENOMINAZIONE,   \n"
          + "   B.DATA_INIZIO,        											   \n"
          + "   B.DATA_FINE,        											   \n"
          + "   C.CODICE AS COD_TIPOLOGIA,                                         \n"
          + "   C.DESCRIZIONE AS TIPOLOGIA,                                        \n"
          + "   NVL(B.ANNO_CAMPAGNA,TO_CHAR(SYSDATE, 'YYYY')) AS ANNO_CAMPAGNA,    \n"
          + "   NVL(B.FLAG_TITOLARITA_REGIONALE, 'N') AS FLAG_TITOLARITA_REGIONALE, \n"
          + "   NVL(D.ID_BANDO_MASTER, B.ID_BANDO) AS ID_BANDO_MASTER              \n"
          + " FROM                                                                 \n"
          + "   IUF_D_BANDO B,                                                     \n"
          + "   IUF_D_TIPO_LIVELLO C,                                              \n"
          + "   IUF_R_BANDO_MASTER D                                               \n"
          + " WHERE                                                                \n"
          + "   C.ID_TIPO_LIVELLO = B.ID_TIPO_LIVELLO                              \n"
          + "   AND D.ID_BANDO(+) = B.ID_BANDO		                               \n"
          + "   AND B.ID_BANDO = :ID_BANDO                                         \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BandoDTO>()
          {
            @Override
            public BandoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BandoDTO bando = new BandoDTO();
              while (rs.next())
              {
                String flagMaster = rs.getString("FLAG_MASTER");
                bando.setIdBando(rs.getLong("ID_BANDO"));
                bando.setIdBandoMaster(rs.getLong("ID_BANDO_MASTER"));
                bando.setIdTipoLivello(rs.getLong("ID_TIPO_LIVELLO"));
                bando.setFlagTitolaritaRegionale(
                    rs.getString("FLAG_TITOLARITA_REGIONALE"));
                bando.setFlagDomandaMultipla(
                    rs.getString("FLAG_DOMANDA_MULTIPLA"));
                bando.setDenominazione(rs.getString("DENOMINAZIONE"));
                bando.setDescrTipoBando(rs.getString("TIPOLOGIA"));
                bando.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                bando.setCodiceTipoBando(rs.getString("COD_TIPOLOGIA"));
                bando.setDescrizioneFiltro(rs.getString("DESCRIZIONE_FILTRO"));
                bando.setIstruzioneSqlFiltro(
                    rs.getString("ISTRUZIONE_SQL_FILTRO"));
                bando.setReferenteBando(rs.getString("REFERENTE_BANDO"));
                bando.setEmailReferenteBando(
                    rs.getString("EMAIL_REFERENTE_BANDO"));
                bando.setFlagRibassoInterventi(
                    rs.getString("FLAG_RIBASSO_INTERVENTI"));

                bando.setFlagMaster(flagMaster);
                if (flagMaster.equals("N"))
                {
                  bando.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                  bando.setDataFine(rs.getTimestamp("DATA_FINE"));
                }
              }
              return bando;
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

  public boolean graficiTabellariPresenti(long idBando, String attore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "graficiTabellariPresenti";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                      \n"
          + "   A.ID_ELENCO_QUERY                                         \n"
          + " FROM                                                        \n"
          + "   IUF_R_ELENCO_QUERY_BANDO A,                               \n"
          + "   IUF_D_ELENCO_QUERY B,                                     \n"
          + "   IUF_D_TIPO_VISUALIZZAZIONE C                              \n"
          + " WHERE                                                       \n"
          + "   A.ID_BANDO = :ID_BANDO "
          + "  AND A.FLAG_VISIBILE = 'S'				                 \n"
          + "   AND A.EXT_COD_ATTORE = :ATTORE                                   \n"
          + "   AND A.ID_ELENCO_QUERY = B.ID_ELENCO_QUERY                 \n"
          + "   AND B.ID_TIPO_VISUALIZZAZIONE = C.ID_TIPO_VISUALIZZAZIONE \n"
          + "   AND C.FLAG_ELENCO = 'S'                                   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ATTORE", attore, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return true;
              }
              return false;
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

  public String getQueryParametroPagamenti() throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getQueryParametroPagamenti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT FILE_ALLEGATO FROM IUF_D_PARAMETRO WHERE CODICE = 'ESTRAI_PAGAMENTI' \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                byte[] sql = rs.getBytes("FILE_ALLEGATO");
                try
                {
                  return new String(sql, "UTF-8");
                }
                catch (UnsupportedEncodingException e)
                {
                  return null;
                }
              }
              else
              {
                return null;
              }
            }
          });
    }
    catch (Exception t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          null,
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

  public boolean hasExcelTemplate(long idElencoQuery)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "hasExcelTemplate";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   COUNT(*) AS NUM_EXCEL \n"
          + " FROM                                                \n"
          + "   IUF_D_ELENCO_QUERY Q                             \n"
          + " WHERE                                               \n"
          + "  Q.ID_ELENCO_QUERY = :ID_ELENCO_QUERY     \n"
          + "  AND LENGHT(Q.EXCEL_PARAMETRO)> 0 \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ELENCO_QUERY", idElencoQuery, Types.NUMERIC);

      return namedParameterJdbcTemplate.queryForLong(SELECT,
          parameterSource) > 0;
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

  public byte[] getExcelParametroDiElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getExcelParametroDiElencoQuery";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   EXCEL_TEMPLATE                                   \n"
          + " FROM                                                \n"
          + "   IUF_D_ELENCO_QUERY Q                             \n"
          + " WHERE                                               \n"
          + "  Q.ID_ELENCO_QUERY = :ID_ELENCO_QUERY     \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ELENCO_QUERY", idElencoQuery, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<byte[]>()
          {
            @Override
            public byte[] extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getBytes("EXCEL_TEMPLATE");
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

  public boolean hasExcelTemplateInElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "hasExcelTemplateInElencoQuery";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   COUNT(*) AS NUM_EXCEL \n"
          + " FROM                                                \n"
          + "   IUF_D_ELENCO_QUERY Q                             \n"
          + " WHERE                                               \n"
          + "  Q.ID_ELENCO_QUERY = :ID_ELENCO_QUERY     \n"
          + "  AND LENGTH(Q.EXCEL_TEMPLATE)> 0 \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ELENCO_QUERY", idElencoQuery, Types.NUMERIC);

      return namedParameterJdbcTemplate.queryForLong(SELECT,
          parameterSource) > 0;
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

  public List<ElencoQueryBandoDTO> getElencoReport(String extCodAttore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoReport";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 																\n"
          + "		A.ID_BANDO,														\n"
          + "		B.ID_ELENCO_QUERY,												\n"
          + "		B.DESCRIZIONE_BREVE DESCRIZIONE,								\n"
          + "		B.DESCRIZIONE_ESTESA INFO_AGGIUNTIVE,							\n"
          + "		C.DESCRIZIONE TIPOLOGIA_REPORT									\n"
          + " FROM																\n"
          + "		IUF_R_ELENCO_QUERY_BANDO A,										\n"
          + "		IUF_D_ELENCO_QUERY B,											\n"
          + "		IUF_D_TIPO_VISUALIZZAZIONE C,									\n"
          + "		IUF_D_BANDO D													\n"
          + " WHERE 																\n"
          + "		B.ID_ELENCO_QUERY = A.ID_ELENCO_QUERY							\n"
          + " 	AND C.ID_TIPO_VISUALIZZAZIONE = B.ID_TIPO_VISUALIZZAZIONE		\n"
          + " 	AND D.ID_BANDO = A.ID_BANDO										\n"
          + " 	AND C.ID_TIPO_VISUALIZZAZIONE=3									\n"
          + " 	AND D.FLAG_MASTER='S'											\n"
          + " 	AND A.EXT_COD_ATTORE=:EXT_COD_ATTORE							\n"
          + " ORDER BY A.ORDINAMENTO 												\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("EXT_COD_ATTORE", extCodAttore, Types.VARCHAR);

      return queryForList(SELECT, parameterSource, ElencoQueryBandoDTO.class);
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

  public List<ElencoQueryBandoDTO> getElencoGrafici(String extCodAttore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoGrafici";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 																\n"
          + "		A.ID_BANDO,														\n"
          + "		B.ID_ELENCO_QUERY,												\n"
          + "		B.DESCRIZIONE_BREVE DESCRIZIONE,								\n"
          + "		B.DESCRIZIONE_ESTESA INFO_AGGIUNTIVE,							\n"
          + "		C.DESCRIZIONE TIPOLOGIA_REPORT									\n"
          + " FROM																\n"
          + "		IUF_R_ELENCO_QUERY_BANDO A,										\n"
          + "		IUF_D_ELENCO_QUERY B,											\n"
          + "		IUF_D_TIPO_VISUALIZZAZIONE C,									\n"
          + "		IUF_D_BANDO D													\n"
          + " WHERE 																\n"
          + "		B.ID_ELENCO_QUERY = A.ID_ELENCO_QUERY							\n"
          + " 	AND C.ID_TIPO_VISUALIZZAZIONE = B.ID_TIPO_VISUALIZZAZIONE		\n"
          + " 	AND D.ID_BANDO = A.ID_BANDO										\n"
          + " 	AND C.ID_TIPO_VISUALIZZAZIONE!=3								\n"
          + " 	AND D.FLAG_MASTER='S'											\n"
          + " 	AND A.EXT_COD_ATTORE=:EXT_COD_ATTORE							\n"
          + " ORDER BY A.ORDINAMENTO 												\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("EXT_COD_ATTORE", extCodAttore, Types.VARCHAR);

      return queryForList(SELECT, parameterSource, ElencoQueryBandoDTO.class);
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

}
