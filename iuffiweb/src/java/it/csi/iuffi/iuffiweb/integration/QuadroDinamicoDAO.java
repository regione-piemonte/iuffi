package it.csi.iuffi.iuffiweb.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.validator.GenericValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.DatiComuniElemQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.DatoElementoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.ElementoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.QuadroDinamicoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico.VoceElementoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class QuadroDinamicoDAO extends BaseDAO
{
  private static final String THIS_CLASS = QuadroDinamicoDAO.class
      .getSimpleName();

  public QuadroDinamicoDAO()
  {
  }

  /**
   * 
   * @param codiceQuadro
   * @param idProcedimentoOggetto
   *          Se idProcedimentoOggetto è valorizzato allora leggo i dati del
   *          quadro dinamico, altrimenti se idProcedimento == null significa
   *          che il chiamante vuole solo la struttura del quadro dinamico
   * @param numProgressivoRecord
   *          valorizzato solo se valorizzato idProcedimentoOggetto
   * @return
   * @throws InternalUnexpectedException
   */
  public QuadroDinamicoDTO getQuadroDinamico(final String codiceQuadro,
      final Long idProcedimentoOggetto, Long numProgressivoRecord)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getQuadroDinamico]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder qb = new StringBuilder(
        " SELECT                                                                  \n"
            + "   QD.ISTRUZ_SQL_POST_SALVATAGGIO,                                       \n"
            + "   QD.ID_QUADRO,                                                         \n"
            + "   QD.FLAG_VISUALIZZAZIONE_ELENCO,                                       \n"
            + "   EQ.NOME_LABEL,                                                        \n"
            + "   EQ.FLAG_OBBLIGATORIO,                                                 \n"
            + "   EQ.ISTRUZIONE_SQL_CONTROLLI,                                          \n"
            + "   EQ.ID_TIPO_DATO,                                                      \n"
            + "   EQ.CODICE,                                                            \n"
            + "   EQ.ISTRUZIONE_SQL_ELENCO,                                             \n"
            + "   EQ.FLAG_PRESENZA_IN_ELENCO,                                           \n"
            + "   EQ.ORDINE_VISUALIZZAZIONE,                                            \n"
            + "   EQ.PRECISIONE,                                                        \n"
            + "   EQ.LUNGHEZZA,                                                         \n"
            + "   EQ.NOTE,                                                              \n"
            + "   EQ.ID_ELEMENTO_QUADRO,                                                \n"
            + "   EQ.LUNGHEZZA_MIN,                                                     \n"
            + "   EQ.FLAG_PROTETTO,                                                     \n"
            + "   EQ.FLAG_STAMPA,                                                       \n"
            + "   EQ.VALORE_DEFAULT,                                                    \n");
    if (idProcedimentoOggetto != null)
    {
      qb.append(
          "   DATI.ID_DATI_COMUNI_ELEM_QUADRO,                                     \n"
              + "   DATI.ID_DATO_ELEMENTO_QUADRO,                                         \n"
              + "   DATI.NUM_PROGRESSIVO_RECORD,                                          \n"
              + "   DATI.VALORE_ELEMENTO,                                                 \n");
    }
    qb.append(
        "   TD.CODICE AS CODICE_TIPO_DATO                                         \n");
    qb.append(
        " FROM                                                                    \n"
            + "   IUF_D_QUADRO Q,                                                       \n"
            + "   IUF_D_QUADRO_DINAMICO QD,                                             \n"
            + "   IUF_D_TIPO_DATO TD,                                                   \n");
    if (idProcedimentoOggetto != null)
    {
      qb.append(
          "   (                                                                     \n"
              + "     SELECT                                                              \n"
              + "       DEQ.ID_DATO_ELEMENTO_QUADRO,                                      \n"
              + "       DEQ.ID_ELEMENTO_QUADRO,                                           \n"
              + "       DEQ.VALORE_ELEMENTO,                                              \n"
              + "       DCEQ.ID_QUADRO,                                                   \n"
              + "       DCEQ.ID_DATI_COMUNI_ELEM_QUADRO,                                  \n"
              + "       DCEQ.NUM_PROGRESSIVO_RECORD                                       \n"
              + "     FROM                                                                \n"
              + "       IUF_T_DATI_COMUNI_ELEM_QUADR DCEQ,                               \n"
              + "       IUF_T_DATO_ELEMENTO_QUADRO DEQ                                    \n"
              + "     WHERE                                                               \n"
              + "       DCEQ.ID_DATI_COMUNI_ELEM_QUADRO  = DEQ.ID_DATI_COMUNI_ELEM_QUADRO \n"
              + "       AND DCEQ.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       \n");
      if (numProgressivoRecord != null)
      {
        qb.append(
            "       AND DCEQ.NUM_PROGRESSIVO_RECORD = :NUM_PROGRESSIVO_RECORD         \n");
      }
      qb.append(
          "   )                                                                     \n"
              + "   DATI,                                                                 \n");
    }
    qb.append(
        "   IUF_D_ELEMENTO_QUADRO EQ                                             \n"
            + " WHERE                                                                   \n"
            + "   QD.ID_QUADRO              = EQ.ID_QUADRO                              \n"
            + "   AND EQ.ID_TIPO_DATO       = TD.ID_TIPO_DATO                           \n"
            + "   AND EQ.ID_QUADRO          = Q.ID_QUADRO                               \n"
            + "   AND Q.CODICE              = :CODICE_QUADRO                            \n");
    if (idProcedimentoOggetto != null)
    {
      qb.append(
          "   AND EQ.ID_ELEMENTO_QUADRO = DATI.ID_ELEMENTO_QUADRO(+)                \n"
              + "   AND EQ.ID_QUADRO = DATI.ID_QUADRO(+)                                  \n");
    }
    qb.append(
        " ORDER BY                                                                \n"
            + "   EQ.ORDINE_VISUALIZZAZIONE ASC                                          \n");
    if (idProcedimentoOggetto != null)
    {

      qb.append(
          "  , DATI.ID_DATI_COMUNI_ELEM_QUADRO ASC,                                \n"
              + "   DATI.ID_DATO_ELEMENTO_QUADRO ASC                                    \n");
    }
    final String QUERY = qb.toString();
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("CODICE_QUADRO", codiceQuadro, Types.VARCHAR);
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    if (numProgressivoRecord != null
        && numProgressivoRecord.longValue() == Long.MAX_VALUE)
    {
      numProgressivoRecord = null;
    }
    mapParameterSource.addValue("NUM_PROGRESSIVO_RECORD", numProgressivoRecord,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<QuadroDinamicoDTO>()
          {
            @Override
            public QuadroDinamicoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              QuadroDinamicoDTO quadroDinamicoDTO = null;
              int lastIdElementoQuadro = Integer.MIN_VALUE;
              ElementoQuadroDTO elementoQuadro = null;
              Map<Long, DatiComuniElemQuadroDTO> tmpMapDatiComuni = new TreeMap<Long, DatiComuniElemQuadroDTO>();
              while (rs.next())
              {
                if (quadroDinamicoDTO == null)
                {
                  quadroDinamicoDTO = new QuadroDinamicoDTO();
                  quadroDinamicoDTO.setFlagVisualizzazioneElenco(
                      rs.getString("FLAG_VISUALIZZAZIONE_ELENCO"));
                  quadroDinamicoDTO.setIstruzSqlPostSalvataggio(
                      rs.getString("ISTRUZ_SQL_POST_SALVATAGGIO"));
                  quadroDinamicoDTO.setIdQuadro(rs.getLong("ID_QUADRO"));
                  quadroDinamicoDTO.setCodice(codiceQuadro);
                }

                int idElementoQuadro = rs.getInt("ID_ELEMENTO_QUADRO");
                if (elementoQuadro == null
                    || lastIdElementoQuadro != idElementoQuadro)
                {
                  lastIdElementoQuadro = idElementoQuadro;
                  elementoQuadro = new ElementoQuadroDTO();
                  elementoQuadro.setIdElementoQuadro(idElementoQuadro);
                  elementoQuadro.setNomeLabel(rs.getString("NOME_LABEL"));
                  elementoQuadro
                      .setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
                  elementoQuadro.setIstruzioneSqlControlli(
                      rs.getString("ISTRUZIONE_SQL_CONTROLLI"));
                  elementoQuadro.setIdTipoDato(rs.getLong("ID_TIPO_DATO"));
                  elementoQuadro.setCodice(rs.getString("CODICE"));
                  elementoQuadro.setIstruzioneSqlElenco(
                      rs.getString("ISTRUZIONE_SQL_ELENCO"));
                  elementoQuadro.setFlagPresenzaInElenco(
                      rs.getString("FLAG_PRESENZA_IN_ELENCO"));
                  elementoQuadro.setOrdineVisualizzazione(
                      rs.getInt("ORDINE_VISUALIZZAZIONE"));
                  elementoQuadro
                      .setPrecisione(getIntegerNull(rs, "PRECISIONE"));
                  elementoQuadro.setLunghezza(getIntegerNull(rs, "LUNGHEZZA"));
                  elementoQuadro.setNote(rs.getString("NOTE"));
                  elementoQuadro
                      .setLunghezzaMin(getIntegerNull(rs, "LUNGHEZZA_MIN"));
                  elementoQuadro.setFlagProtetto(rs.getString("FLAG_PROTETTO"));
                  elementoQuadro.setFlagStampa(rs.getString("FLAG_STAMPA"));
                  elementoQuadro
                      .setValoreDefault(rs.getString("VALORE_DEFAULT"));
                  elementoQuadro
                      .setCodiceTipoDato(rs.getString("CODICE_TIPO_DATO"));
                  quadroDinamicoDTO.getElementiQuadro().add(elementoQuadro);
                }
                if (idProcedimentoOggetto != null)
                {
                  /*
                   * Se idProcedimentoOggetto è valorizzato allora leggo i dati
                   * del quadro dinamico, altrimenti se idProcedimento == null
                   * significa che il chiamante vuole solo la struttura del
                   * quadro dinamico
                   */
                  Long idDatiComuniElemQuadro = getLongNull(rs,
                      "ID_DATI_COMUNI_ELEM_QUADRO");

                  if (idDatiComuniElemQuadro != null)
                  {
                    DatiComuniElemQuadroDTO datiComuniElemQuadroDTO = tmpMapDatiComuni
                        .get(idDatiComuniElemQuadro);
                    if (datiComuniElemQuadroDTO == null)
                    {
                      datiComuniElemQuadroDTO = new DatiComuniElemQuadroDTO();
                      datiComuniElemQuadroDTO
                          .setIdDatiComuniElemQuadro(idDatiComuniElemQuadro);
                      datiComuniElemQuadroDTO.setNumProgressivoRecord(
                          rs.getLong("NUM_PROGRESSIVO_RECORD"));
                      tmpMapDatiComuni.put(idDatiComuniElemQuadro,
                          datiComuniElemQuadroDTO);
                    }
                    BigDecimal idDatoElementoQuadro = rs
                        .getBigDecimal("ID_DATO_ELEMENTO_QUADRO");
                    if (idDatoElementoQuadro != null)
                    {
                      DatoElementoQuadroDTO dato = new DatoElementoQuadroDTO();
                      dato.setIdElementoQuadro(
                          rs.getLong("ID_ELEMENTO_QUADRO"));
                      dato.setValoreElemento(rs.getString("VALORE_ELEMENTO"));
                      datiComuniElemQuadroDTO.addDato(dato);
                    }
                  }
                }
              }
              if (!tmpMapDatiComuni.isEmpty())
              {
                for (DatiComuniElemQuadroDTO datiComuniElemQuadroDTO : tmpMapDatiComuni
                    .values())
                {
                  quadroDinamicoDTO
                      .addDatiComuniElemQuadro(datiComuniElemQuadroDTO);
                }
              }
              return quadroDinamicoDTO;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("codiceQuadro", codiceQuadro),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("numProgressivoRecord", numProgressivoRecord)
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
   * 
   * @param codiceQuadro
   * @param idProcedimentoOggetto
   *          Se idProcedimentoOggetto è valorizzato allora leggo i dati del
   *          quadro dinamico, altrimenti se idProcedimento == null significa
   *          che il chiamante vuole solo la struttura del quadro dinamico
   * @return
   * @throws InternalUnexpectedException
   */
  /*
   * public QuadroDinamicoMultiRecordDTO getQuadroDinamicoMultiRecord(final
   * String codiceQuadro, final Long idProcedimentoOggetto) throws
   * InternalUnexpectedException { String THIS_METHOD = "[" + THIS_CLASS +
   * "::getQuadroDinamicoMultiRecord]"; if (logger.isDebugEnabled()) {
   * logger.debug(THIS_METHOD + " BEGIN."); } StringBuilder qb = new
   * StringBuilder(
   * " SELECT                                                                  \n"
   * +
   * "   QD.ISTRUZ_SQL_POST_SALVATAGGIO,                                       \n"
   * +
   * "   QD.ID_QUADRO,                                                         \n"
   * +
   * "   QD.FLAG_VISUALIZZAZIONE_ELENCO,                                       \n"
   * +
   * "   EQ.NOME_LABEL,                                                        \n"
   * +
   * "   EQ.FLAG_OBBLIGATORIO,                                                 \n"
   * +
   * "   EQ.ISTRUZIONE_SQL_CONTROLLI,                                          \n"
   * +
   * "   EQ.ID_TIPO_DATO,                                                      \n"
   * +
   * "   EQ.CODICE,                                                            \n"
   * +
   * "   EQ.ISTRUZIONE_SQL_ELENCO,                                             \n"
   * +
   * "   EQ.FLAG_PRESENZA_IN_ELENCO,                                           \n"
   * +
   * "   EQ.ORDINE_VISUALIZZAZIONE,                                            \n"
   * +
   * "   EQ.PRECISIONE,                                                        \n"
   * +
   * "   EQ.LUNGHEZZA,                                                         \n"
   * +
   * "   EQ.NOTE,                                                              \n"
   * +
   * "   EQ.ID_ELEMENTO_QUADRO,                                                \n"
   * +
   * "   EQ.LUNGHEZZA_MIN,                                                     \n"
   * +
   * "   EQ.FLAG_PROTETTO,                                                     \n"
   * +
   * "   EQ.FLAG_STAMPA,                                                       \n"
   * +
   * "   EQ.VALORE_DEFAULT,                                                    \n"
   * ); if (idProcedimentoOggetto != null) { qb.append(
   * "   DATI.ID_DATI_COMUNI_ELEM_QUADRO,                                     \n"
   * +
   * "   DATI.ID_DATO_ELEMENTO_QUADRO,                                         \n"
   * +
   * "   DATI.NUM_PROGRESSIVO_RECORD,                                          \n"
   * +
   * "   DATI.VALORE_ELEMENTO,                                                 \n"
   * ); } qb.append(
   * "   TD.CODICE AS CODICE_TIPO_DATO                                         \n"
   * ); qb.append(
   * " FROM                                                                    \n"
   * +
   * "   IUF_D_QUADRO Q,                                                       \n"
   * +
   * "   IUF_D_QUADRO_DINAMICO QD,                                             \n"
   * +
   * "   IUF_D_TIPO_DATO TD,                                                   \n"
   * ); if (idProcedimentoOggetto != null) { qb.append(
   * "   (                                                                     \n"
   * +
   * "     SELECT                                                              \n"
   * +
   * "       DEQ.ID_DATO_ELEMENTO_QUADRO,                                      \n"
   * +
   * "       DEQ.ID_ELEMENTO_QUADRO,                                           \n"
   * +
   * "       DEQ.VALORE_ELEMENTO,                                              \n"
   * +
   * "       DCEQ.ID_QUADRO,                                                   \n"
   * +
   * "       DCEQ.ID_DATI_COMUNI_ELEM_QUADRO,                                  \n"
   * +
   * "       DCEQ.NUM_PROGRESSIVO_RECORD                                       \n"
   * +
   * "     FROM                                                                \n"
   * +
   * "       IUF_T_DATI_COMUNI_ELEM_QUADR DCEQ,                               \n"
   * +
   * "       IUF_T_DATO_ELEMENTO_QUADRO DEQ                                    \n"
   * +
   * "     WHERE                                                               \n"
   * +
   * "       DCEQ.ID_DATI_COMUNI_ELEM_QUADRO  = DEQ.ID_DATI_COMUNI_ELEM_QUADRO \n"
   * +
   * "       AND DCEQ.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO       \n"
   * ); qb.append(
   * "   )                                                                     \n"
   * +
   * "   DATI,                                                                 \n"
   * ); } qb.append(
   * "   IUF_D_ELEMENTO_QUADRO EQ                                             \n"
   * +
   * " WHERE                                                                   \n"
   * +
   * "   QD.ID_QUADRO              = EQ.ID_QUADRO                              \n"
   * +
   * "   AND EQ.ID_TIPO_DATO       = TD.ID_TIPO_DATO                           \n"
   * +
   * "   AND EQ.ID_QUADRO          = Q.ID_QUADRO                               \n"
   * +
   * "   AND Q.CODICE              = :CODICE_QUADRO                            \n"
   * ); if (idProcedimentoOggetto != null) { qb.append(
   * "   AND EQ.ID_ELEMENTO_QUADRO = DATI.ID_ELEMENTO_QUADRO(+)                \n"
   * +
   * "   AND EQ.ID_QUADRO = DATI.ID_QUADRO(+)                                  \n"
   * ); } qb.append(
   * " ORDER BY                                                                \n"
   * +
   * "   EQ.ORDINE_VISUALIZZAZIONE ASC,                                       \n"
   * +
   * "   EQ.ORDINE_VISUALIZZAZIONE ASC                                        \n"
   * ); if (idProcedimentoOggetto != null) {
   * 
   * qb.append(
   * "  , DATI.ID_ELEMENTO_QUADRO ASC,                                         \n"
   * +
   * "   DATI.NUM_PROGRESSIVO_RECORD ASC,                                     \n"
   * +
   * "   DATI.ID_DATO_ELEMENTO_QUADRO ASC                                     \n"
   * );
   * 
   * } final String QUERY = qb.toString(); MapSqlParameterSource
   * mapParameterSource = new MapSqlParameterSource();
   * mapParameterSource.addValue("CODICE_QUADRO", codiceQuadro, Types.VARCHAR);
   * mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
   * idProcedimentoOggetto, Types.NUMERIC); try { return
   * namedParameterJdbcTemplate.query(QUERY, mapParameterSource, new
   * ResultSetExtractor<QuadroDinamicoMultiRecordDTO>() {
   * 
   * @Override public QuadroDinamicoMultiRecordDTO extractData(ResultSet rs)
   * throws SQLException, DataAccessException { QuadroDinamicoMultiRecordDTO
   * quadroDinamicoDTO = null; int lastIdElementoQuadro = Integer.MIN_VALUE;
   * ElementoQuadroDTO elementoQuadro = null; Long idDatiComuniElemQuadro =
   * null; Long numProgressivoRecordCorrente = null; int numRecord = 0; int
   * maxNumRecord=0; while (rs.next()) { if (quadroDinamicoDTO == null) {
   * quadroDinamicoDTO = new QuadroDinamicoMultiRecordDTO();
   * quadroDinamicoDTO.setFlagVisualizzazioneElenco(rs.getString(
   * "FLAG_VISUALIZZAZIONE_ELENCO"));
   * quadroDinamicoDTO.setIstruzSqlPostSalvataggio(rs.getString(
   * "ISTRUZ_SQL_POST_SALVATAGGIO"));
   * quadroDinamicoDTO.setIdQuadro(rs.getLong("ID_QUADRO"));
   * quadroDinamicoDTO.setCodice(codiceQuadro); }
   * 
   * int idElementoQuadro = rs.getInt("ID_ELEMENTO_QUADRO"); if (elementoQuadro
   * == null || lastIdElementoQuadro != idElementoQuadro) { numRecord=0;
   * lastIdElementoQuadro = idElementoQuadro; elementoQuadro = new
   * ElementoQuadroDTO(); elementoQuadro.setIdElementoQuadro(idElementoQuadro);
   * elementoQuadro.setNomeLabel(rs.getString("NOME_LABEL"));
   * elementoQuadro.setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
   * elementoQuadro.setIstruzioneSqlControlli(rs.getString(
   * "ISTRUZIONE_SQL_CONTROLLI"));
   * elementoQuadro.setIdTipoDato(rs.getLong("ID_TIPO_DATO"));
   * elementoQuadro.setCodice(rs.getString("CODICE"));
   * elementoQuadro.setIstruzioneSqlElenco(rs.getString("ISTRUZIONE_SQL_ELENCO")
   * ); elementoQuadro.setFlagPresenzaInElenco(rs.getString(
   * "FLAG_PRESENZA_IN_ELENCO"));
   * elementoQuadro.setOrdineVisualizzazione(rs.getInt("ORDINE_VISUALIZZAZIONE")
   * ); elementoQuadro.setPrecisione(getIntegerNull(rs, "PRECISIONE"));
   * elementoQuadro.setLunghezza(getIntegerNull(rs, "LUNGHEZZA"));
   * elementoQuadro.setNote(rs.getString("NOTE"));
   * elementoQuadro.setLunghezzaMin(getIntegerNull(rs, "LUNGHEZZA_MIN"));
   * elementoQuadro.setFlagProtetto(rs.getString("FLAG_PROTETTO"));
   * elementoQuadro.setFlagStampa(rs.getString("FLAG_STAMPA"));
   * elementoQuadro.setValoreDefault(rs.getString("VALORE_DEFAULT"));
   * elementoQuadro.setCodiceTipoDato(rs.getString("CODICE_TIPO_DATO"));
   * quadroDinamicoDTO.addElemento(elementoQuadro); } if (idProcedimentoOggetto
   * != null) { /* Se idProcedimentoOggetto è valorizzato allora leggo i dati
   * del quadro dinamico, altrimenti se idProcedimento == null significa che il
   * chiamante vuole solo la struttura del quadro dinamico
   */
  /*
   * BigDecimal idDatoElementoQuadro =
   * rs.getBigDecimal("ID_DATO_ELEMENTO_QUADRO"); if (idDatoElementoQuadro !=
   * null) {
   * 
   * long numProgressivoRecord = rs.getLong("N" + "UM_PROGRESSIVO_RECORD"); if
   * (numProgressivoRecordCorrente == null || numProgressivoRecordCorrente !=
   * numProgressivoRecord) {
   * elementoQuadro.setNumProgressivoRecordCorrente(numProgressivoRecord);
   * numProgressivoRecordCorrente = numProgressivoRecord; numRecord++; if
   * (numRecord>maxNumRecord) { maxNumRecord=numRecord; } }
   * List<DatoElementoQuadroDTO> dati = elementoQuadro.getDati(); if (dati ==
   * null) { dati = new ArrayList<DatoElementoQuadroDTO>();
   * elementoQuadro.setDati(dati); } DatoElementoQuadroDTO dato = new
   * DatoElementoQuadroDTO(); dati.add(dato); long id =
   * rs.getLong("ID_DATI_COMUNI_ELEM_QUADRO");
   * dato.setIdDatiComuniElemQuadro(id); if (idDatiComuniElemQuadro == null ||
   * idDatiComuniElemQuadro.longValue()!=id) { DatiComuniElemQuadroDTO
   * datiComuniElemQuadroDTO = new DatiComuniElemQuadroDTO();
   * datiComuniElemQuadroDTO.setIdDatiComuniElemQuadro(id);
   * datiComuniElemQuadroDTO.setNumProgressivoRecord(numProgressivoRecord);
   * idDatiComuniElemQuadro = id;
   * quadroDinamicoDTO.addDatiComuniElemQuadro(datiComuniElemQuadroDTO); }
   * dato.setIdElementoQuadro(rs.getLong("ID_DATO_ELEMENTO_QUADRO"));
   * dato.setValoreElemento(rs.getString("VALORE_ELEMENTO")); } }
   * quadroDinamicoDTO.setIdDatiComuniElementoQuadro(idDatiComuniElemQuadro); }
   * quadroDinamicoDTO.setNumRecord(maxNumRecord);
   * quadroDinamicoDTO.resetNumProgressivoRecordCorrente(); return
   * quadroDinamicoDTO; } }); } catch (Throwable t) {
   * InternalUnexpectedException e = new InternalUnexpectedException(t, new
   * LogParameter[] { new LogParameter("codiceQuadro", codiceQuadro), new
   * LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, new
   * LogVariable[] {}, QUERY, mapParameterSource);
   * logInternalUnexpectedException(e, THIS_METHOD); throw e; } finally { if
   * (logger.isDebugEnabled()) { logger.debug(THIS_METHOD + " END."); } } }
   */

  public Map<Long, List<VoceElementoDTO>> getMapVociElementoPerQuadro(
      long idQuadro) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getMapVociElementoPerQuadro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                              \n"
        + "   VE.ID_ELEMENTO_QUADRO,                            \n"
        + "   VE.CODICE,                                        \n"
        + "   VE.VALORE                                         \n"
        + " FROM                                                \n"
        + "   IUF_D_VOCE_ELEMENTO VE,                           \n"
        + "   IUF_D_ELEMENTO_QUADRO EQ                          \n"
        + " WHERE                                               \n"
        + "   EQ.ID_QUADRO = :ID_QUADRO                         \n"
        + "   AND EQ.ID_ELEMENTO_QUADRO = VE.ID_ELEMENTO_QUADRO \n"
        + " ORDER BY                                            \n"
        + "   EQ.ID_ELEMENTO_QUADRO,                            \n"
        + "   VE.ORDINE_VISUALIZZAZIONE                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<Long, List<VoceElementoDTO>>>()
          {
            @Override
            public Map<Long, List<VoceElementoDTO>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<Long, List<VoceElementoDTO>> mapVoci = new HashMap<Long, List<VoceElementoDTO>>();
              long lastId = Long.MIN_VALUE;
              List<VoceElementoDTO> list = null;
              while (rs.next())
              {
                long idElementoQuadro = rs.getLong("ID_ELEMENTO_QUADRO");
                if (list == null || lastId != idElementoQuadro)
                {
                  list = new ArrayList<VoceElementoDTO>();
                  mapVoci.put(idElementoQuadro, list);
                  lastId = idElementoQuadro;
                }
                VoceElementoDTO voceElementoDTO = new VoceElementoDTO();
                voceElementoDTO.setIdElementoQuadro(idElementoQuadro);
                voceElementoDTO.setCodice(rs.getString("CODICE"));
                voceElementoDTO.setValore(rs.getString("VALORE"));
                list.add(voceElementoDTO);
              }
              return mapVoci;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idQuadro", idQuadro)
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

  public List<VoceElementoDTO> getVociElementoByCustomQuery(
      final long idElementoQuadro, final String QUERY,
      Long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getVociElementoByIdElementoQuadro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<VoceElementoDTO>>()
          {
            @Override
            public List<VoceElementoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<VoceElementoDTO> list = new ArrayList<VoceElementoDTO>();
              while (rs.next())
              {
                VoceElementoDTO voceElementoDTO = new VoceElementoDTO();
                voceElementoDTO.setIdElementoQuadro(idElementoQuadro);
                voceElementoDTO.setCodice(rs.getString("CODICE"));
                voceElementoDTO.setValore(rs.getString("VALORE"));
                list.add(voceElementoDTO);
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
              new LogParameter("QUERY", QUERY)
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

  public Long getIdDatiComuniElemQuadro(long idProcedimentoOggetto,
      long idQuadro, long numProgressivoRecord)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getIdDatiComuniElemQuadro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                       \n"
        + "   DCEQ.ID_DATI_COMUNI_ELEM_QUADRO                            \n"
        + " FROM                                                         \n"
        + "   IUF_T_DATI_COMUNI_ELEM_QUADR DCEQ                         \n"
        + " WHERE                                                        \n"
        + "   DCEQ.ID_PROCEDIMENTO_OGGETTO    = :ID_PROCEDIMENTO_OGGETTO \n"
        + "   AND DCEQ.ID_QUADRO              = :ID_QUADRO               \n"
        + "   AND DCEQ.NUM_PROGRESSIVO_RECORD = :NUM_PROGRESSIVO_RECORD  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("NUM_PROGRESSIVO_RECORD", numProgressivoRecord,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);
    try
    {
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("numProgressivoRecord", numProgressivoRecord),
              new LogParameter("idQuadro", idQuadro)
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

  public Long getMaxNumProgressivoRecord(long idProcedimentoOggetto,
      long idQuadro) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getMaxNumProgressivoRecord]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                      \n"
        + "   NVL(MAX(DCEQ.NUM_PROGRESSIVO_RECORD),0) + 1 AS MAX_NUM_PROGRESSIVO_RECORD \n"
        + " FROM                                                                        \n"
        + "   IUF_T_DATI_COMUNI_ELEM_QUADR DCEQ                                        \n"
        + " WHERE                                                                       \n"
        + "   DCEQ.ID_PROCEDIMENTO_OGGETTO    = :ID_PROCEDIMENTO_OGGETTO                \n"
        + "   AND DCEQ.ID_QUADRO              = :ID_QUADRO                              \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idQuadro", idQuadro)
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

  public Long insertDatiComuniElemQuadro(long idProcedimentoOggetto,
      long idQuadro, long numProgressivoRecord)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertDatiComuniElemQuadro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                           \n"
        + " INTO                             \n"
        + "   IUF_T_DATI_COMUNI_ELEM_QUADR  \n"
        + "   (                              \n"
        + "     ID_DATI_COMUNI_ELEM_QUADRO,  \n"
        + "     ID_PROCEDIMENTO_OGGETTO,     \n"
        + "     NUM_PROGRESSIVO_RECORD,      \n"
        + "     ID_QUADRO                    \n"
        + "   )                              \n"
        + "   VALUES                         \n"
        + "   (                              \n"
        + "     :ID_DATI_COMUNI_ELEM_QUADRO, \n"
        + "     :ID_PROCEDIMENTO_OGGETTO,    \n"
        + "     :NUM_PROGRESSIVO_RECORD,     \n"
        + "     :ID_QUADRO                   \n"
        + "   )                              \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    final long idDatiComuniElemQuadro = getNextSequenceValue(
        IuffiConstants.SQL.SEQUENCE.IUF_T_DATI_COMUNI_ELEM_Q);
    try
    {
      mapParameterSource.addValue("ID_DATI_COMUNI_ELEM_QUADRO",
          idDatiComuniElemQuadro, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);
      mapParameterSource.addValue("NUM_PROGRESSIVO_RECORD",
          numProgressivoRecord, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idDatiComuniElemQuadro;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDatiComuniElemQuadro",
                  idDatiComuniElemQuadro),
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto)
          },
          new LogVariable[]
          { new LogVariable("idDatiComuniElemQuadro", idDatiComuniElemQuadro) }, INSERT,
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

  public void batchInsertDatiElementoQuadro(Long idDatiComuniElemQuadro,
      Map<Long, String[]> mapValues) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::batchInsertDatiElementoQuadro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                      \n"
        + " INTO                                        \n"
        + "   IUF_T_DATO_ELEMENTO_QUADRO                \n"
        + "   (                                         \n"
        + "     VALORE_ELEMENTO,                        \n"
        + "     ID_ELEMENTO_QUADRO,                     \n"
        + "     ID_DATO_ELEMENTO_QUADRO,                \n"
        + "     ID_DATI_COMUNI_ELEM_QUADRO              \n"
        + "   )                                         \n"
        + "   VALUES                                    \n"
        + "   (                                         \n"
        + "     :VALORE_ELEMENTO,                       \n"
        + "     :ID_ELEMENTO_QUADRO,                    \n"
        + "     SEQ_IUF_T_DATO_ELEMENTO_QUAD.NEXTVAL, \n"
        + "     :ID_DATI_COMUNI_ELEM_QUADRO             \n"
        + "   )                                         \n"

    ;
    MapSqlParameterSource[] batchParameterSource = null;
    List<MapSqlParameterSource> listParameterSource = new ArrayList<MapSqlParameterSource>();
    for (Long idElementoQuadro : mapValues.keySet())
    {
      final String[] valoriElementi = mapValues.get(idElementoQuadro);
      if (valoriElementi != null)
      {
        for (String valoreElemento : valoriElementi)
        {
          if (!GenericValidator.isBlankOrNull(valoreElemento))
          {
            MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
            mapParameterSource.addValue("ID_DATI_COMUNI_ELEM_QUADRO",
                idDatiComuniElemQuadro, Types.NUMERIC);
            mapParameterSource.addValue("ID_ELEMENTO_QUADRO", idElementoQuadro,
                Types.NUMERIC);
            mapParameterSource.addValue("VALORE_ELEMENTO", valoreElemento,
                Types.VARCHAR);
            listParameterSource.add(mapParameterSource);
          }
        }
      }
    }
    batchParameterSource = listParameterSource
        .toArray(new MapSqlParameterSource[listParameterSource.size()]);
    try
    {
      namedParameterJdbcTemplate.batchUpdate(INSERT, batchParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDatiComuniElemQuadro",
                  idDatiComuniElemQuadro),
              new LogParameter("mapValues", mapValues)
          },
          null, INSERT, batchParameterSource);
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

  public String executeIstruzioneControlli(String istruzioneSqlControlli,
      String value) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::executeIstruzioneControlli]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = istruzioneSqlControlli;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("VALORE_ELEMENTO", value, Types.VARCHAR);
    try
    {
      return queryForString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("istruzioneSqlControlli",
                  istruzioneSqlControlli),
              new LogParameter("value", value)
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

  public String executeIstruzSqlPostSalvataggio(
      final String istruzSqlPostSalvataggio, long idDatiComuniElemQuadro,
      String tipoOperazione, long extIdUtenteAggiornamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::executeIstruzSqlPostSalvataggio]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT " + istruzSqlPostSalvataggio
        + "(:ID_DATI_COMUNI_ELEM_QUADRO, :TIPO_OPERAZIONE, :EXT_ID_UTENTE_AGGIORNAMENTO) FROM DUAL";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DATI_COMUNI_ELEM_QUADRO",
          idDatiComuniElemQuadro, Types.NUMERIC);
      mapParameterSource.addValue("TIPO_OPERAZIONE", tipoOperazione,
          Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      return namedParameterJdbcTemplate.queryForObject(QUERY,
          mapParameterSource, String.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
          new LogVariable[]
          {}, QUERY,
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

  public String getIstruzSqlPostSalvataggio(long idQuadroDinamico)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getIstruzSqlPostSalvataggio]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                     \n"
        + "   ISTRUZ_SQL_POST_SALVATAGGIO              \n"
        + " FROM                                       \n"
        + "   IUF_D_QUADRO_DINAMICO                    \n"
        + " WHERE                                      \n"
        + "   ID_QUADRO = " + idQuadroDinamico;

    try
    {
      return namedParameterJdbcTemplate.queryForObject(QUERY,
          (MapSqlParameterSource) null, String.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
          new LogVariable[]
          {}, QUERY,
          (MapSqlParameterSource) null);
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