package it.csi.iuffi.iuffiweb.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.SqlLobValue;

import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.ExcelRicevutePagInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.ExcelRigaDocumentoSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.ExcelRigaRicevutaPagDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.RegistroAntimafiaDTO;
import it.csi.iuffi.iuffiweb.dto.SportelloDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.login.ProcedimentoAgricoloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.RicevutaPagamentoVO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.ContenutoFileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo.DatiAnticipo;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.AnnoExPostsDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.ControlloAmministrativoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi.InfoExPostsDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliinlocomisureinvestimento.ControlliInLocoInvestDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliinlocomisureinvestimento.DatiSpecificiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.DataCensimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.Distretto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.solmr.dto.anag.ParticellaVO;

public class QuadroNewDAO extends QuadroDAO
{
  private static final String THIS_CLASS = QuadroNewDAO.class.getSimpleName();
  
  public List<DataCensimento> getDateCensimento(long piano, long specie) throws InternalUnexpectedException
  {
    String methodName = new Object()
    {
    }.getClass().getEnclosingMethod().getName();
    String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = "select co.data_censimento, mc.desc_metodologia, co.ID_DATE_CENS_OGUR as id, mc.ID_METODO_CENSIMENTO as metodo, co.VALORE_METODO_CENSIMENTO as descrizione, um.descrizione as unita from IUF_T_DATE_CENS_OGUR co " + 
        "left join IUF_R_METODO_CENS_SPECIE cs on cs.id_specie_ogur = :ID_SPECIE   and co.id_metodo_specie = cs.id_metodo_specie " + 
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
  
  public List<Distretto> getElencoDistrettiOgur(boolean regionale, String idDistretto, long idSpecie, Long piano, long procedimento) throws InternalUnexpectedException {
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
    if(regionale) {
      tabella = "IUF_D_OGUR_DA_REGIONE D";
      campo = "ID_OGUR_DA_REGIONE";
      id = "ID_OGUR_DA_REGIONE";
    }
    else{
      tabella = "IUF_T_OGUR_DISTRETTO D";
      campo = "ID_DISTRETTO";
      id = "ID_DISTRETTO";
    }
    String QUERY = "select distinct(d." + id + ") as id_distretto, d.NOMIN_DISTRETTO, d.SUPERFICIE_DISTRETTO, d.sus, pd.ID_PIANO_DISTRETTO_OGUR, " + 
        "pd.TOTALE_CENSITO, pd.totale_prelievo, pd.indeterminati_censito, cp1.etichetta as et1, " + 
        "cp1.valore_da as valoreda1, cp1.valore_a as valorea1, cp2.etichetta as et2, cp2.valore_da as valoreda2, cp2.valore_a as valorea2, " + 
        "cp3.etichetta as et3, cp3.valore_da as valoreda3, cp3.valore_a as valorea3, " + 
        "cp4.etichetta as et4,  cp5.etichetta as et5,  cp6.etichetta as et6, cp4.valore_da as valoreda4, cp5.valore_da as valoreda5, cp4.valore_a as valorea4, cp5.valore_a as valorea5, " + 
        "cpo1.censito as censito1, cpo2.censito as censito2, cpo3.censito as censito3, cpo4.censito as censito4,  cpo5.censito as censito5, ip.percentuale, pd.max_capi_prelievo, "
        + "pd.indeterminati_prelievo, cpo1.prelevato as prelievo1, cpo2.prelevato as prelievo2, cpo3.prelevato as prelievo3, cpo4.prelevato as prelievo4, cpo5.prelevato as prelievo5, cpo6.prelevato as prelievo6,"
        + "cpo1.percentuale as perc1, cpo2.percentuale as perc2, cpo3.percentuale as perc3, cpo4.percentuale as perc4, cpo5.percentuale as perc5, cpo6.percentuale as perc6, pd.esito_totale_prelievo, "
        + "CPO1.ESITO_CONTROLLO as esito1, CPO2.ESITO_CONTROLLO as esito2, CPO3.ESITO_CONTROLLO as esito3, CPO4.ESITO_CONTROLLO as esito4, CPO5.ESITO_CONTROLLO as esito5, CPO6.ESITO_CONTROLLO as esito6 "
        + " from " + tabella + " left join IUF_t_piano_distretto_ogur PD on d."+ id +" = pd."+ id
            + " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO1 on cpo1.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo1.progressivo = 1 and cpo1.id_specie_ogur = :ID_SPECIE " + 
            " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO2 on cpo2.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo2.progressivo = 2 and cpo2.id_specie_ogur = :ID_SPECIE " + 
            " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO3 on cpo3.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo3.progressivo = 3 and cpo3.id_specie_ogur = :ID_SPECIE " + 
            " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO4 on cpo4.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo4.progressivo = 4 and cpo4.id_specie_ogur = :ID_SPECIE " + 
            " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO5 on cpo5.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo5.progressivo = 5 and cpo5.id_specie_ogur = :ID_SPECIE " +
            " left join IUF_T_CENSITO_PRELIEVO_OGUR CPO6 on cpo6.id_piano_distretto_ogur = pd.id_piano_distretto_ogur and cpo6.progressivo = 6 and cpo6.id_specie_ogur = :ID_SPECIE " +
            " left join IUF_R_CONTROLLI_PIANO_OGUR CP1 on cp1.progressivo = 1 and cp1.id_specie_ogur = :ID_SPECIE " + 
            " left join IUF_R_CONTROLLI_PIANO_OGUR CP2 on cp2.progressivo = 2 and cp2.id_specie_ogur = :ID_SPECIE " + 
            " left join IUF_R_CONTROLLI_PIANO_OGUR CP3 on cp3.progressivo = 3 and cp3.id_specie_ogur = :ID_SPECIE " + 
            " left join IUF_R_CONTROLLI_PIANO_OGUR CP5 on cp5.progressivo = 5 and cp5.id_specie_ogur = :ID_SPECIE " +
            " left join IUF_R_CONTROLLI_PIANO_OGUR CP6 on cp6.progressivo = 6 and cp6.id_specie_ogur = :ID_SPECIE " +
            " left join IUF_R_CONTROLLI_PIANO_OGUR CP4 on cp4.progressivo = 4 and cp4.id_specie_ogur = :ID_SPECIE  ";
    if(!regionale) {
      QUERY = QUERY + " left join IUF_t_ogur_ipotesi_prelievo ip on ip.id_distretto = d.id_distretto and ip.anno = "+anno;
    }
    else {
      QUERY = QUERY + " left join IUF_D_OGUR_DA_REGIONE ip on ip.id_ogur_da_regione = d.ID_OGUR_DA_REGIONE";
    }
    QUERY = QUERY + " where d." +  campo + " IN" + idDistretto + " and pd.id_piano_ogur = (select id_piano_ogur from IUF_T_PIANO_OGUR where id_procedimento_oggetto = :id_procedimento_oggetto and id_specie_ogur = :ID_SPECIE)";
    if(piano != null) {
      QUERY = QUERY + " and pd.ID_PIANO_DISTRETTO_OGUR = :ID_PIANO_DISTRETTO_OGUR ";
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
      if(piano != null) {
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

  public DatiAnticipo getDatiAnticipo(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDatiAnticipo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = "  SELECT                                                                                    \n"
        + "         A.ID_ANTICIPO,                                                                     \n"
        + "         A.ID_PROCEDIMENTO,                                                                 \n"
        + "         AL.IMPORTO_INVESTIMENTO,                                                           \n"
        + "         AL.IMPORTO_AMMESSO,                                                                \n"
        + "         AL.IMPORTO_CONTRIBUTO,                                                             \n"
        + "         AL.IMPORTO_ANTICIPO,                                                               \n"
        + "         A.PERCENTUALE_ANTICIPO,                                                            \n"
        + "         A.IMPORTO_ANTICIPO,                                                                \n"
        + "         A.IMPORTO_FIDEIUSSIONE,                                                            \n"
        + "         A.NUMERO_FIDEIUSSIONE,                                                             \n"
        + "         A.DATA_STIPULA,                                                                    \n"
        + "         A.DATA_SCADENZA,                                                                   \n"
        + "         A.BENEFICIARIO_FIDEIUSSIONE,                                                       \n"
        + "         A.EXT_ID_SPORTELLO,                                                                \n"
        + "         A.ALTRO_ISTITUTO,                                                                  \n"
        + "         A.INDIRIZZO_ALTRO_ISTITUTO,                                                        \n"
        + "         A.EXT_ISTAT_COMUNE,                                                                \n"
        + "         A.ID_STATO_OGGETTO,                                                                \n"
        + "         BS.ABI,                                                                            \n"
        + "         BS.CAB,                                                                            \n"
        + "         BS.DENOMINAZIONE_BANCA,                                                            \n"
        + "         BS.DENOMINAZIONE_SPORTELLO,                                                        \n"
        + "         BS.INDIRIZZO AS INDIRIZZO_SPORTELLO,                                               \n"
        + "         BS.CAP AS CAP_SPORTELLO,                                                           \n"
        + "         BS.DESCRIZIONE_COMUNE_SPORTELLO,                                                   \n"
        + "         BS.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_SPORTELLO,                                   \n"
        + "         DA.DESCRIZIONE_COMUNE AS DESC_COMUNE_ALTRO_ISTITUTO,                               \n"
        + "         DA.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_ALTRO_ISTITUTO,                              \n"
        + "         DA.CAP AS CAP_ALTRO_ISTITUTO,                                                      \n"
        + "         L.CODICE_LIVELLO,                                                                  \n"
        + "         L.FLAG_ANTICIPO                                                                    \n"
        + "       FROM                                                                                 \n"
        + "         IUF_T_ANTICIPO A                                                                   \n"
        + "         LEFT JOIN SMRGAA_V_BANCA_SPORTELLO BS ON A.EXT_ID_SPORTELLO = BS.ID_SPORTELLO      \n"
        + "         LEFT JOIN SMRGAA_V_DATI_AMMINISTRATIVI DA ON A.EXT_ISTAT_COMUNE = DA.ISTAT_COMUNE, \n"
        + "         IUF_R_ANTICIPO_PROC_OGG APO,                                                       \n"
        + "         IUF_R_ANTICIPO_LIVELLO AL,                                                         \n"
        + "         IUF_D_LIVELLO L                                                                    \n"
        + "       WHERE                                                                                \n"
        + "         A.ID_ANTICIPO = APO.ID_ANTICIPO                                                    \n"
        + "         AND APO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                         \n"
        + "         AND A.ID_ANTICIPO = AL.ID_ANTICIPO                                                 \n"
        + "         AND AL.ID_LIVELLO = L.ID_LIVELLO                                                   \n"
        + "       ORDER BY                                                                             \n"
        + "         L.ORDINAMENTO                                                                      \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DatiAnticipo>()
          {

            @Override
            public DatiAnticipo extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              DatiAnticipo datiAnticipo = null;
              while (rs.next())
              {
                if (datiAnticipo == null)
                {
                  datiAnticipo = new DatiAnticipo();
                  datiAnticipo.setAbi(rs.getString("ABI"));
                  datiAnticipo.setDenominazioneBanca(
                      rs.getString("DENOMINAZIONE_BANCA"));
                  datiAnticipo.setCab(rs.getString("CAB"));
                  datiAnticipo.setDenominazioneSportello(
                      rs.getString("DENOMINAZIONE_SPORTELLO"));
                  datiAnticipo.setIndirizzoSportello(
                      rs.getString("INDIRIZZO_SPORTELLO"));
                  datiAnticipo.setCapSportello(rs.getString("CAP_SPORTELLO"));
                  datiAnticipo.setDescrizioneComuneSportello(
                      rs.getString("DESCRIZIONE_COMUNE_SPORTELLO"));
                  datiAnticipo.setSiglaProvinciaSportello(
                      rs.getString("SIGLA_PROVINCIA_SPORTELLO"));
                  datiAnticipo
                      .setCapAltroIstituto(rs.getString("CAP_ALTRO_ISTITUTO"));
                  datiAnticipo.setDescComuneAltroIstituto(
                      rs.getString("DESC_COMUNE_ALTRO_ISTITUTO"));
                  datiAnticipo.setSiglaProvinciaAltroIstituto(
                      rs.getString("SIGLA_PROVINCIA_ALTRO_ISTITUTO"));
                  datiAnticipo.setIdStatoOggetto(rs.getInt("ID_STATO_OGGETTO"));
                  datiAnticipo.setIdProcedimento(rs.getLong("ID_PROCEDIMENTO"));
                  datiAnticipo.setExtIdSportello(
                      getIntegerNull(rs, "EXT_ID_SPORTELLO"));
                  datiAnticipo.setAltroIstituto(rs.getString("ALTRO_ISTITUTO"));
                  datiAnticipo.setIndirizzoAltroIstituto(
                      rs.getString("INDIRIZZO_ALTRO_ISTITUTO"));
                  datiAnticipo
                      .setExtIstatComune(rs.getString("EXT_ISTAT_COMUNE"));
                  datiAnticipo.setIdAnticipo(rs.getLong("ID_ANTICIPO"));
                  datiAnticipo.setPercentualeAnticipo(
                      rs.getBigDecimal("PERCENTUALE_ANTICIPO"));
                  datiAnticipo
                      .setImportoAnticipo(rs.getBigDecimal("IMPORTO_ANTICIPO"));
                  datiAnticipo.setImportoFideiussione(
                      rs.getBigDecimal("IMPORTO_FIDEIUSSIONE"));
                  datiAnticipo.setNumeroFideiussione(
                      rs.getString("NUMERO_FIDEIUSSIONE"));
                  datiAnticipo.setDataStipula(rs.getDate("DATA_STIPULA"));
                  datiAnticipo.setDataScadenza(rs.getDate("DATA_SCADENZA"));
                  datiAnticipo.setBeneficiarioFideiussione(
                      rs.getString("BENEFICIARIO_FIDEIUSSIONE"));
                }
                datiAnticipo.addRigaRiepilogo(rs.getString("CODICE_LIVELLO"),
                    rs.getBigDecimal("IMPORTO_INVESTIMENTO"),
                    rs.getBigDecimal("IMPORTO_AMMESSO"),
                    rs.getBigDecimal("IMPORTO_CONTRIBUTO"),
                    rs.getBigDecimal("IMPORTO_ANTICIPO"),
                    rs.getString("FLAG_ANTICIPO"));

              }
              return datiAnticipo;
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

  public List<ImportoAnticipoLivelloDTO> getRipartizioneAnticipoSuLivelli(
      long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRipartizioneAnticipoSuLivelli";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " WITH                                                                                       \n"
        + " INTERVENTI AS                                                                              \n"
        + " (                                                                                          \n"
        + "   SELECT                                                                                   \n"
        + "     LBI.ID_LIVELLO,                                                                        \n"
        + "     SUM(DECODE(D.FLAG_ANTICIPO,'S',NVL(DI.IMPORTO_CONTRIBUTO,0),0)) AS IMPORTO_CONTRIBUTO, \n"
        + "     DECODE(D.FLAG_ANTICIPO,'S',D.ORDINAMENTO,'0') AS ORDINAMENTO                           \n"
        + "   FROM                                                                                     \n"
        + "     IUF_T_PROCEDIMENTO_OGGETTO PO,                                                         \n"
        + "     IUF_T_PROCEDIMENTO P,                                                                  \n"
        + "     IUF_T_INTERVENTO I,                                                                    \n"
        + "     IUF_T_DETTAGLIO_INTERVENTO DI,                                                         \n"
        + "     IUF_D_DESCRIZIONE_INTERVENTO DESCINT,                                                  \n"
        + "     IUF_D_CATEGORIA_INTERVENTO CI,                                                         \n"
        + "     IUF_R_LIV_BANDO_INTERVENTO LBI,                                                        \n"
        + "     IUF_D_LIVELLO D                                                                        \n"
        + "   WHERE                                                                                    \n"
        + "     PO.ID_PROCEDIMENTO_OGGETTO   = :ID_PROCEDIMENTO_OGGETTO                                \n"
        + "     AND P.ID_PROCEDIMENTO        = PO.ID_PROCEDIMENTO                                      \n"
        + "     AND PO.ID_PROCEDIMENTO       = I.ID_PROCEDIMENTO                                       \n"
        + "     AND I.ID_INTERVENTO          = DI.ID_INTERVENTO                                        \n"
        + "     AND DI.FLAG_TIPO_OPERAZIONE <> 'D'                                                     \n"
        + "     AND SYSDATE BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE,SYSDATE)                       \n"
        + "     AND I.ID_DESCRIZIONE_INTERVENTO       = DESCINT.ID_DESCRIZIONE_INTERVENTO              \n"
        + "     AND DESCINT.ID_CATEGORIA_INTERVENTO   = CI.ID_CATEGORIA_INTERVENTO(+)                  \n"
        + "     AND NVL(CI.FLAG_ESCLUDI_CATALOGO,'N') = :FLAG_ESCLUDI_CATALOGO                         \n"
        + "     AND I.ID_DESCRIZIONE_INTERVENTO       = LBI.ID_DESCRIZIONE_INTERVENTO                  \n"
        + "     AND P.ID_BANDO                        = LBI.ID_BANDO                                   \n"
        + "     AND LBI.ID_LIVELLO                    = D.ID_LIVELLO                                   \n"
        + "   GROUP BY                                                                                 \n"
        + "     LBI.ID_LIVELLO,                                                                        \n"
        + "     D.ORDINAMENTO,                                                                         \n"
        + "     D.FLAG_ANTICIPO                                                                        \n"
        + "   HAVING                                                                                   \n"
        + "     SUM(NVL(DI.IMPORTO_CONTRIBUTO,0)) > 0                                                  \n"
        + "   ORDER BY                                                                                 \n"
        + "     D.ORDINAMENTO DESC                                                                     \n"
        + " )                                                                                          \n"
        + " ,                                                                                          \n"
        + " TOTALI AS                                                                                  \n"
        + " (                                                                                          \n"
        + "   SELECT                                                                                   \n"
        + "     SUM(I2.IMPORTO_CONTRIBUTO) AS TOTALE                                                   \n"
        + "   FROM                                                                                     \n"
        + "     INTERVENTI I2                                                                          \n"
        + " )                                                                                          \n"
        + " ,                                                                                          \n"
        + " ANTICIPO AS                                                                                \n"
        + " (                                                                                          \n"
        + "   SELECT                                                                                   \n"
        + "     A.IMPORTO_ANTICIPO,                                                                    \n"
        + "     A.ID_ANTICIPO                                                                          \n"
        + "   FROM                                                                                     \n"
        + "     IUF_T_ANTICIPO A,                                                                      \n"
        + "     IUF_R_ANTICIPO_PROC_OGG APO                                                            \n"
        + "   WHERE                                                                                    \n"
        + "     APO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                 \n"
        + "     AND APO.ID_ANTICIPO         = A.ID_ANTICIPO                                            \n"
        + " )                                                                                          \n"
        + " SELECT                                                                                     \n"
        + " I.ID_LIVELLO,                                                                              \n"
        + " A.ID_ANTICIPO,                                                                             \n"
        + " ROUND(A.IMPORTO_ANTICIPO *                                                                 \n"
        + "       I.IMPORTO_CONTRIBUTO / T.TOTALE,2) AS IMPORTO_ANTICIPO                               \n"
        + " FROM                                                                                       \n"
        + " INTERVENTI I,                                                                              \n"
        + " TOTALI T,                                                                                  \n"
        + " ANTICIPO A                                                                                 \n"
        + " ORDER BY                                                                                   \n"
        + " I.ORDINAMENTO DESC                                                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_ESCLUDI_CATALOGO",
        IuffiConstants.FLAGS.NO, Types.CHAR);
    try
    {
      return queryForList(QUERY, mapParameterSource,
          ImportoAnticipoLivelloDTO.class);
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

  public Long getProcedimentoOggettoDomandaPrec(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getProcedimentoOggettoDomandaPrec";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT DISTINCT                                                \n"
        + "    D.ID_PROCEDIMENTO_OGGETTO                                   \n"
        + "  FROM                                                          \n"
        + "    IUF_T_DATI_PROCEDIMENTO A,                                  \n"
        + "    IUF_T_IMPEGNO_PROC_OGG B,                                   \n"
        + "    IUF_T_PROCEDIMENTO C,                                       \n"
        + "    IUF_T_PROCEDIMENTO_OGGETTO D,                               \n"
        + "    IUF_R_BANDO_OGGETTO E,                                      \n"
        + "    IUF_R_LEGAME_GRUPPO_OGGETTO F,                              \n"
        + "    IUF_D_OGGETTO G                                             \n"
        + "  WHERE                                                         \n"
        + "    A.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO        \n"
        + "    AND A.ID_DATI_PROCEDIMENTO = B.ID_DATI_PROCEDIMENTO         \n"
        + "    AND C.IDENTIFICATIVO = B.DOMANDA_ANNO_PRECEDENTE            \n"
        + "    AND D.ID_PROCEDIMENTO = C.ID_PROCEDIMENTO                   \n"
        + "    AND D.ID_LEGAME_GRUPPO_OGGETTO = E.ID_LEGAME_GRUPPO_OGGETTO \n"
        + "    AND F.ID_LEGAME_GRUPPO_OGGETTO = E.ID_LEGAME_GRUPPO_OGGETTO \n"
        + "    AND F.ID_OGGETTO = G.ID_OGGETTO                             \n"
        + "    AND G.CODICE = 'ISTPR'                                      \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
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
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  public List<RicevutaPagamentoVO> getElencoRicevutePagamento(
      long[] idsDocumentoSpesa) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getElencoRicevutePagamento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");

    }

    String QUERY = " SELECT                                                  			\n"
        + "   B.ID_DETT_RICEVUTA_PAGAMENTO,                         			\n"
        + "   B.ID_RICEVUTA_PAGAMENTO,                              			\n"
        + "   B.NUMERO_RICEVUTA_PAGAMENTO AS NUMERO,                			\n"
        + "   B.DATA_PAGAMENTO,                                     			\n"
        + "   B.ID_MODALITA_PAGAMENTO,                              			\n"
        + "   B.IMPORTO_PAGAMENTO,                                  			\n"
        + "   B.NOTE,                                               			\n"
        + "   C.DESCRIZIONE AS DESCR_MODALITA_PAGAMENTO,                      \n"
        + "      DECODE((SELECT DISTINCT B1.ID_RICEVUTA_PAGAMENTO                                            			\n"
        + "         FROM IUF_R_DOC_SPESA_PROC_OGG B1                                                         			\n"
        + "         WHERE B1.ID_RICEVUTA_PAGAMENTO = A.ID_RICEVUTA_PAGAMENTO), NULL, 'S', 'N') AS VISIBLE_ICONS		\n"

        // + " B.NOME_FILE_LOGICO_RICEVUTA_PAG AS NOME_LOGICO_FILE, \n"
        // + " B.NOME_FILE_FISICO_RICEVUTA_PAG AS NOME_FISICO_FILE \n"
        + " FROM                                                    			\n"
        + "   IUF_T_RICEVUTA_PAGAMENTO A,                           			\n"
        + "   IUF_T_DETT_RICEVUTA_PAGAMENT B,                       			\n"
        + "   IUF_D_MODALITA_PAGAMENTO C                       				\n"
        + " WHERE                                                   			\n"
        + "   A.ID_RICEVUTA_PAGAMENTO = B.ID_RICEVUTA_PAGAMENTO 				\n"
        + "   AND B.ID_MODALITA_PAGAMENTO = C.ID_MODALITA_PAGAMENTO(+) 		\n"
        + "   AND B.DATA_FINE IS NULL									 		\n"
        + "   " + getInCondition("A.ID_DOCUMENTO_SPESA", idsDocumentoSpesa)
        + "   \n"
        + " ORDER BY B.DATA_PAGAMENTO , B.NUMERO_RICEVUTA_PAGAMENTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(QUERY, mapParameterSource, RicevutaPagamentoVO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idsDocumentoSpesa", idsDocumentoSpesa) },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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
  
  public String getCodiceOggettoByIdLegameGO(long idLegameGruppoOggetto) throws InternalUnexpectedException {
      String THIS_METHOD = "[" + THIS_CLASS + "::getCodiceOggettoByIdLegameGO]";
      if (logger.isDebugEnabled()) {
	  logger.debug(THIS_METHOD + " BEGIN.");
      }

      String QUERY = " SELECT                       			                     		\n"
	      + "   O.CODICE											 				\n"
	      + " FROM                                                         		\n"
	      + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO, IUF_D_OGGETTO O														\n"
	      + " WHERE                                                        		\n"
	      + "   LGO.ID_LEGAME_GRUPPO_OGGETTO = :ID_LEGAME_GRUPPO_OGGETTO			\n"
	      + "	  AND O.ID_OGGETTO = LGO.ID_OGGETTO									\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      try {
	  mapParameterSource.addValue("ID_LEGAME_GRUPPO_OGGETTO", idLegameGruppoOggetto, Types.NUMERIC);

	  return queryForString(QUERY, mapParameterSource);
      } catch (Throwable t) {
	  InternalUnexpectedException e = new InternalUnexpectedException(t,
		  new LogParameter[] { new LogParameter("idLegameGruppoOggetto", idLegameGruppoOggetto) },
		  new LogVariable[] {}, QUERY, mapParameterSource);
	  logInternalUnexpectedException(e, THIS_METHOD);
	  throw e;
      } finally {
	  if (logger.isDebugEnabled()) {
	      logger.debug(THIS_METHOD + " END.");
	  }
      }
  }

  public long insertRicevutaPagamento(long idDocSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertRicevutaPagamento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_RICEVUTA_PAGAMENTO 	\n"
        + " (ID_RICEVUTA_PAGAMENTO, 								\n"
        + "	 ID_DOCUMENTO_SPESA) 									\n"
        + "VALUES 													\n"
        + " (:ID_RICEVUTA_PAGAMENTO,		 						\n"
        + "  :ID_DOCUMENTO_SPESA) 									\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long id = 0;
    try
    {
      id = getNextSequenceValue("SEQ_IUF_T_RICEVUTA_PAGAMENTO");
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", id, Types.NUMERIC);
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocSpesa,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_RICEVUTA_PAGAMENTO", id),
              new LogParameter("ID_DOCUMENTO_SPESA", idDocSpesa)
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

  public void insertDettaglioRicevutaPagamento(long idRicevutaPagamento,
      RicevutaPagamentoVO ricevuta, String codiceAttore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::insertDettaglioRicevutaPagamento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_DETT_RICEVUTA_PAGAMENT \n"
        + " (                                         \n"
        + "   ID_DETT_RICEVUTA_PAGAMENTO,             \n"
        + "   ID_RICEVUTA_PAGAMENTO,                  \n"
        + "   NUMERO_RICEVUTA_PAGAMENTO,              \n"
        + "   DATA_PAGAMENTO,                         \n"
        + "   ID_MODALITA_PAGAMENTO,                  \n"
        + "   IMPORTO_PAGAMENTO,                      \n"
        + "   NOTE,                                   \n"
        + "   NOME_FILE_LOGICO_RICEVUTA_PAG,          \n"
        + "   FILE_RICEVUTA_PAGAMENTO,                \n"
        + "   NOME_FILE_FISICO_RICEVUTA_PAG,          \n"
        + "   DATA_ULTIMO_AGGIORNAMENTO,              \n"
        + "   EXT_COD_ATTORE,                         \n"
        + "   DATA_INIZIO,                            \n"
        + "   DATA_FINE                               \n"
        + " )VALUES(                                  \n"
        + "   SEQ_IUF_T_DETT_RICEVUTA_PAGA.NEXTVAL, \n"
        + "   :ID_RICEVUTA_PAGAMENTO,                 \n"
        + "   :NUMERO_RICEVUTA_PAGAMENTO,             \n"
        + "   :DATA_PAGAMENTO,                        \n"
        + "   :ID_MODALITA_PAGAMENTO,                 \n"
        + "   :IMPORTO_PAGAMENTO,                     \n"
        + "   :NOTE,                                  \n"
        + "   :NOME_FILE_LOGICO_RICEVUTA_PAG,         \n"
        + "   :FILE_RICEVUTA_PAGAMENTO,               \n"
        + "   :NOME_FILE_FISICO_RICEVUTA_PAG,         \n"
        + "   SYSDATE,                                \n"
        + "   :EXT_COD_ATTORE,                        \n"
        + "   SYSDATE,                                \n"
        + "   NULL                                    \n"
        + " )                                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_RICEVUTA_PAGAMENTO",
          ricevuta.getNumero(), Types.VARCHAR);
      mapParameterSource.addValue("DATA_PAGAMENTO", ricevuta.getDataPagamento(),
          Types.DATE);
      mapParameterSource.addValue("ID_MODALITA_PAGAMENTO",
          ricevuta.getIdModalitaPagamento(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_PAGAMENTO",
          ricevuta.getImportoPagamento(), Types.NUMERIC);
      mapParameterSource.addValue("NOME_FILE_LOGICO_RICEVUTA_PAG",
          ricevuta.getNomeLogicoFile(), Types.VARCHAR);
      mapParameterSource.addValue("NOTE", ricevuta.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("FILE_RICEVUTA_PAGAMENTO",
          new SqlLobValue(ricevuta.getContenuto()), Types.BLOB);
      mapParameterSource.addValue("NOME_FILE_FISICO_RICEVUTA_PAG",
          ricevuta.getNomeFisicoFile(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_COD_ATTORE", codiceAttore,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ricevutaVO", ricevuta)
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

  public void updateRicevutaPagamento(RicevutaPagamentoVO ricevuta,
      String codiceAttore) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateRicevutaPagamento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String UPDATE = " UPDATE                                                      		  			\n"
        + "   IUF_T_DETT_RICEVUTA_PAGAMENT                               				\n"
        + " SET                                                           				\n"
        + "   NUMERO_RICEVUTA_PAGAMENTO = :NUMERO_RICEVUTA_PAGAMENTO,              	\n"
        + "   DATA_PAGAMENTO = :DATA_PAGAMENTO,                                    	\n"
        + "   ID_MODALITA_PAGAMENTO = :ID_MODALITA_PAGAMENTO,                      	\n"
        + "   IMPORTO_PAGAMENTO = :IMPORTO_PAGAMENTO,                                 \n"
        + "   NOTE = :NOTE,                                              	    	    \n"
        + "   NOME_FILE_LOGICO_RICEVUTA_PAG = :NOME_FILE_LOGICO_RICEVUTA_PAG,         \n"
        + "   FILE_RICEVUTA_PAGAMENTO = :FILE_RICEVUTA_PAGAMENTO,                    	\n";
    if (ricevuta.getContenuto() != null)
      UPDATE += "   NOME_FILE_FISICO_RICEVUTA_PAG = :NOME_FILE_FISICO_RICEVUTA_PAG,      \n";

    UPDATE += "   DATA_ULTIMO_AGGIORNAMENTO = SYSDATE,                         	    	\n"
        + "   EXT_COD_ATTORE = :EXT_COD_ATTORE  	                                  	\n"
        + " WHERE                                                                    	\n"
        + "   ID_DETT_RICEVUTA_PAGAMENTO = :ID_DETT_RICEVUTA_PAGAMENTO          	 	\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_RICEVUTA_PAGAMENTO",
          ricevuta.getIdDettRicevutaPagamento(), Types.NUMERIC);
      mapParameterSource.addValue("NUMERO_RICEVUTA_PAGAMENTO",
          ricevuta.getNumero(), Types.VARCHAR);
      mapParameterSource.addValue("DATA_PAGAMENTO", ricevuta.getDataPagamento(),
          Types.DATE);
      mapParameterSource.addValue("ID_MODALITA_PAGAMENTO",
          ricevuta.getIdModalitaPagamento(), Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_PAGAMENTO",
          ricevuta.getImportoPagamento(), Types.NUMERIC);
      mapParameterSource.addValue("NOTE", ricevuta.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("NOME_FILE_LOGICO_RICEVUTA_PAG",
          ricevuta.getNomeLogicoFile(), Types.VARCHAR);
      mapParameterSource.addValue("FILE_RICEVUTA_PAGAMENTO",
          new SqlLobValue(ricevuta.getContenuto()), Types.BLOB);
      mapParameterSource.addValue("NOME_FILE_FISICO_RICEVUTA_PAG",
          ricevuta.getNomeFisicoFile(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_COD_ATTORE", codiceAttore,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ricevutaVO", ricevuta)
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
   * public FileAllegatoDTO getFileAllegatoRicevutaPagamento(long
   * idDettRicevutaPagamento) throws InternalUnexpectedException { String
   * THIS_METHOD = "[" + THIS_CLASS + "::getFileAllegatoRicevutaPagamento]"; if
   * (logger.isDebugEnabled()) { logger.debug(THIS_METHOD + " BEGIN.");
   * logger.debug(THIS_METHOD + " idDettRicevutaPagamento = " +
   * idDettRicevutaPagamento); } final String QUERY =
   * " SELECT                                        								\n" +
   * "   FILE_RICEVUTA_PAGAMENTO,                             					\n" +
   * "   NOME_FILE_FISICO_RICEVUTA_PAG                          	    		\n" +
   * " FROM                                          		     	    		\n" +
   * "   IUF_T_DETT_RICEVUTA_PAGAMENT                    						\n" +
   * " WHERE                                         		     	    		\n" +
   * "   ID_DETT_RICEVUTA_PAGAMENTO = :ID_DETT_RICEVUTA_PAGAMENTO 				\n";
   * 
   * MapSqlParameterSource mapParameterSource = new MapSqlParameterSource(); try
   * { mapParameterSource.addValue("ID_DETT_RICEVUTA_PAGAMENTO",
   * idDettRicevutaPagamento, Types.NUMERIC);
   * 
   * return namedParameterJdbcTemplate.query(QUERY, mapParameterSource, new
   * ResultSetExtractor<FileAllegatoDTO>() {
   * 
   * @Override public FileAllegatoDTO extractData(ResultSet rs) throws
   * SQLException, DataAccessException { if (rs.next()) { FileAllegatoDTO file =
   * new FileAllegatoDTO();
   * file.setNomeFile(rs.getString("NOME_FILE_FISICO_RICEVUTA_PAG"));
   * file.setFileAllegato(rs.getBytes("FILE_RICEVUTA_PAGAMENTO")); return file;
   * } return null; } }); } catch (Throwable t) { InternalUnexpectedException e
   * = new InternalUnexpectedException(t, new LogParameter[] { new
   * LogParameter("idDettRicevutaPagamento", idDettRicevutaPagamento) }, new
   * LogVariable[] {}, QUERY, mapParameterSource);
   * logInternalUnexpectedException(e, THIS_METHOD); throw e; } finally { if
   * (logger.isDebugEnabled()) { logger.debug(THIS_METHOD + " END."); } } }
   */
  public RicevutaPagamentoVO getRicevutaPagamento(long idDettRicevutaPagamento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRicevutaPagamento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                  	\n"
        + "   B.ID_DETT_RICEVUTA_PAGAMENTO,                         	\n"
        + "   B.ID_RICEVUTA_PAGAMENTO,                              	\n"
        + "   B.NUMERO_RICEVUTA_PAGAMENTO AS NUMERO,                	\n"
        + "   B.DATA_PAGAMENTO,                                     	\n"
        + "   B.ID_MODALITA_PAGAMENTO,                              	\n"
        + "   B.IMPORTO_PAGAMENTO,                                  	\n"
        + "   B.NOTE,                                               	\n"
        + "   C.DESCRIZIONE AS DESCR_MODALITA_PAGAMENTO               \n"
        // + " B.NOME_FILE_LOGICO_RICEVUTA_PAG AS NOME_LOGICO_FILE, \n"
        // + " B.NOME_FILE_FISICO_RICEVUTA_PAG AS NOME_FISICO_FILE \n"
        + " FROM                                                    	\n"
        + "   IUF_T_DETT_RICEVUTA_PAGAMENT B,                       	\n"
        + "   IUF_D_MODALITA_PAGAMENTO C                       		\n"
        + " WHERE                                                   	\n"
        + "   B.ID_MODALITA_PAGAMENTO = C.ID_MODALITA_PAGAMENTO(+) 	\n"
        + "   AND B.DATA_FINE IS NULL							  		\n"

        + "   AND B.ID_DETT_RICEVUTA_PAGAMENTO = :ID_DETT_RICEVUTA_PAGAMENTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_RICEVUTA_PAGAMENTO",
          idDettRicevutaPagamento, Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource,
          RicevutaPagamentoVO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idDettRicevutaPagamento",
              idDettRicevutaPagamento) },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public String getFlagEstrattaControlliDichiarazione(long idProcedimento,
      long codiceRaggruppamento, long idProcedimentoOggetto,
      String codiceTipoBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getFlagEstrattaControlliDichiarazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = "";
    if ("P".equals(codiceTipoBando))
    {
      // misure a premio - vado su IUF_T_PROCEDIMENTO_ESTRATTO con
      // l'ID_PROCEDIMENTO
      QUERY = " SELECT                       			                     		\n"
          + "   PE.FLAG_ESTRATTA											 		\n"
          + " FROM                                                         		\n"
          + "   IUF_T_PROCEDIMENTO_ESTRATTO PE									\n"
          + " WHERE                                                        		\n"
          + "   PE.ID_PROCEDIMENTO = :ID_PROCEDIMENTO						 		\n";
    }
    else
    {
      // misure investimento/gal vado su IUF_T_PROCEDIMENTO_ESTRATTO con
      // l'ID_PROCEDIMENTO_OGGETTO del PO con stesso id_procedimento e
      // codice_raggruppamento del PO corrente
      QUERY = " SELECT         				                                  		\n"
          + "   PE.FLAG_ESTRATTA											 		\n"
          + " FROM                                                         		\n"
          + "   IUF_T_PROCEDIMENTO_ESTRATTO PE									\n"
          + " WHERE                                                        		\n"
          + "   PE.ID_PROCEDIMENTO_OGGETTO =    	     							\n"
          + "			(SELECT DISTINCT a1.ID_PROCEDIMENTO_OGGETTO    	     					\n"
          + "			FROM IUF_T_PROCEDIMENTO_OGGETTO a1, IUF_T_PROCEDIMENTO_ESTRATTO b1   	     					\n"
          + "			WHERE a1.ID_PROCEDIMENTO = :ID_PROCEDIMENTO    	     		\n"
          + "			AND a1.CODICE_RAGGRUPPAMENTO = :CODICE_RAGGRUPPAMENTO    		\n"
          + "			AND a1.ID_PROCEDIMENTO_OGGETTO = b1.ID_PROCEDIMENTO_OGGETTO    		\n"
          + "			AND a1.ID_PROCEDIMENTO_OGGETTO <> :ID_PROCEDIMENTO_OGGETTO)   	\n";
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RAGGRUPPAMENTO", codiceRaggruppamento,
          Types.NUMERIC);

      return queryForString(QUERY, mapParameterSource);
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

  public boolean controlloSommaRicevute(DocumentoSpesaVO documento,
      RicevutaPagamentoVO ricevuta, String flagRendicontazioneConIva)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "controlloSommaRicevute";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                               		\n"
        + "   SUM(IMPORTO_PAGAMENTO) AS SOMMA                       \n"
        + " FROM                                                    \n"
        + "   IUF_T_DETT_RICEVUTA_PAGAMENT A,                      \n"
        + "   IUF_T_RICEVUTA_PAGAMENTO B	                        \n"
        + " WHERE                                                   \n"
        + "   A.ID_RICEVUTA_PAGAMENTO = B.ID_RICEVUTA_PAGAMENTO		\n"
        + "   AND A.DATA_FINE IS NULL								\n"
        + "   AND B.ID_DOCUMENTO_SPESA     = :ID_DOCUMENTO_SPESA    \n";
    if (ricevuta.getIdDettRicevutaPagamento() != null)
      QUERY += "AND A.ID_DETT_RICEVUTA_PAGAMENTO <> :ID_RICEVUTA	\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    mapParameterSource.addValue("ID_DOCUMENTO_SPESA",
        documento.getIdDocumentoSpesa(), Types.VARCHAR);
    mapParameterSource.addValue("ID_RICEVUTA",
        ricevuta.getIdDettRicevutaPagamento(), Types.VARCHAR);
    mapParameterSource.addValue("IMPORTO_CORR", ricevuta.getImportoPagamento(),
        Types.NUMERIC);
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
                BigDecimal importoSpesa = documento.getImportoSpesa();

                if (documento.getImportoIva() != null)
                  importoSpesa = importoSpesa.add(documento.getImportoIva());

                BigDecimal sum = rs.getBigDecimal("SOMMA");
                if (sum == null)
                  sum = BigDecimal.ZERO;
                if ((sum.add(ricevuta.getImportoPagamento())
                    .subtract(importoSpesa)).compareTo(BigDecimal.ZERO) > 0)
                  return Boolean.FALSE;

                return Boolean.TRUE;
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
              new LogParameter("IMPORTO_MAX", documento.getImportoSpesa())
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

  public boolean canDeleteRicevuta(long idDettRicevutaPagamento)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::canDeleteRicevuta]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT           																		\n"
        + "   COUNT(*)                      													\n"
        + " FROM                            													\n"
        + "   IUF_R_DOC_SPESA_INT_PROC_OGG    													\n"
        + " WHERE                           													\n"
        + "   ID_DOCUMENTO_SPESA_INTERVEN IN 													\n"
        + "			(SELECT DISTINCT ID_DOCUMENTO_SPESA_INTERVEN 								\n"
        + "				FROM IUF_R_DOCUMENTO_SPESA_INTERV										\n"
        + "				WHERE ID_DOCUMENTO_SPESA = 												\n"
        + "				(SELECT DISTINCT ID_DOCUMENTO_SPESA 									\n"
        + "					FROM IUF_T_RICEVUTA_PAGAMENTO 										\n"
        + "					WHERE ID_RICEVUTA_PAGAMENTO = 										\n"
        + "					(SELECT DISTINCT ID_RICEVUTA_PAGAMENTO 								\n"
        + "						FROM IUF_T_DETT_RICEVUTA_PAGAMENT								\n"
        + "						WHERE ID_DETT_RICEVUTA_PAGAMENTO = :ID_DETT_RICEVUTA_PAGAMENTO)))\n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DETT_RICEVUTA_PAGAMENTO",
          idDettRicevutaPagamento, Types.NUMERIC);
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

  public void deleteRDocSpesaIntRic(long idIntervento, long idDocumentoSpesa,
      Long idRicevutaPagamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRDocSpesaIntRic]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_R_DOC_SPESA_INT_RICEV_PA    	\n"
        + " WHERE ID_INTERVENTO = :ID_INTERVENTO 							\n"
        + "	AND ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA						\n"
        + " AND ID_RICEVUTA_PAGAMENTO = :ID_RICEVUTA_PAGAMENTO				\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
              new LogParameter("idDettRicevutaPagamento", idRicevutaPagamento)
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

  public void insertRDocSpesaIntRic(long idIntervento, long idDocumentoSpesa,
      long idRicevutaPagamento, BigDecimal importo,
      long extIdUtenteAggiornamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertRDocSpesaIntRic]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_R_DOC_SPESA_INT_RICEV_PA 	\n"
        + " (ID_INTERVENTO, 											\n"
        + "	 ID_RICEVUTA_PAGAMENTO,										\n"
        + "	ID_DOCUMENTO_SPESA,											\n"
        + "	IMPORTO,													\n"
        + "	EXT_ID_UTENTE_AGGIORNAMENTO,								\n"
        + "	DATA_ULTIMO_AGGIORNAMENTO									\n"
        + ") 															\n"
        + "VALUES 														\n"
        + " (:ID_INTERVENTO,		 									\n"
        + "  :ID_RICEVUTA_PAGAMENTO, 									\n"
        + "	:ID_DOCUMENTO_SPESA, 										\n"
        + "	:IMPORTO, 													\n"
        + "	:EXT_ID_UTENTE_AGGIORNAMENTO, 								\n"
        + "	SYSDATE 													\n"
        + ") 															\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
              new LogParameter("idDettRicevutaPagamento", idRicevutaPagamento)
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

  public BigDecimal getImportoDocSpesaIntRic(long idIntervento,
      long idDocumentoSpesa, long idRicevutaPagamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoDocSpesaIntRic]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                         	\n"
        + "         IMPORTO                                       		   	\n"
        + "       FROM                                                     	\n"
        + "         IUF_R_DOC_SPESA_INT_RICEV_PA                          	\n"
        + " WHERE ID_INTERVENTO = :ID_INTERVENTO 							\n"
        + "	AND ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA						\n"
        + " AND ID_RICEVUTA_PAGAMENTO = :ID_RICEVUTA_PAGAMENTO				\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
              new LogParameter("idDettRicevutaPagamento",
                  idRicevutaPagamento) },
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

  public void deleteImportiVecchiInterventi() throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteImportiVecchiInterventi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_R_DOC_SPESA_INT_RICEV_PA A WHERE NOT EXISTS 	\n"
        + "(	SELECT 'x' 																\n"
        + "		FROM IUF_R_DOCUMENTO_SPESA_INTERV B  									\n"
        + "		WHERE A.ID_INTERVENTO = B.ID_INTERVENTO 								\n"
        + "		AND A.ID_DOCUMENTO_SPESA = B.ID_DOCUMENTO_SPESA)						\n";
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

  public BigDecimal getImportoGiaAssociato(long idIntervento,
      long idDocumentoSpesa, long idRicevutaPagamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoGiaAssociato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                         	\n"
        + "         SUM(IMPORTO)  as IMPORTO                       		   	\n"
        + "       FROM                                                     	\n"
        + "         IUF_R_DOC_SPESA_INT_RICEV_PA                          	\n"
        + " WHERE ID_INTERVENTO <> :ID_INTERVENTO 							\n"
        + "	AND ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA						\n"
        + " AND ID_RICEVUTA_PAGAMENTO = :ID_RICEVUTA_PAGAMENTO				\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
              new LogParameter("idDettRicevutaPagamento",
                  idRicevutaPagamento) },
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

  public Long getIdRicevutaPagamento(long idDettRicevutaPagamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getIdRicevutaPagamento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                                                 	   \n"
        + "  ID_RICEVUTA_PAGAMENTO                                                                                                 \n"
        + " FROM                                                                                                                   \n"
        + "   IUF_T_DETT_RICEVUTA_PAGAMENT                                                                                        \n"
        + " WHERE                                                                                                                  \n"
        + "   ID_DETT_RICEVUTA_PAGAMENTO = :ID_DETT_RICEVUTA_PAGAMENTO                                                             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DETT_RICEVUTA_PAGAMENTO",
          idDettRicevutaPagamento, Types.NUMERIC);

      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDettRicevutaPagamento",
                  idDettRicevutaPagamento)
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

  public Boolean canModifyRendicontazioneIva(long idProcedimento)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::existRecordInIUF_R_PROC_OGG_LIV]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                          					\n"
        + "   COUNT(*)                                     					\n"
        + " FROM                                           					\n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV I,								\n"
        + "	  IUF_T_DOCUMENTO_SPESA D										\n"
        + " WHERE                                          					\n"
        + "   D.ID_PROCEDIMENTO = :ID_PROCEDIMENTO 							\n"
        + "	  AND I.ID_DOCUMENTO_SPESA = D.ID_DOCUMENTO_SPESA				\n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
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

  public List<ExcelRicevutePagInterventoDTO> getElencoExcelRicevutePagamentoIntervento(
      long idProcedimento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoExcelRicevutePagamentoIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT                                                                                            \n"
        + "   DSI.ID_INTERVENTO,                                                                                  \n"
        + "   DI.PROGRESSIVO,        	       	                                                                   \n"
        + "   DDI.DESCRIZIONE AS DESCR_INTERVENTO,                                                                \n"
        + "   DSI.IMPORTO AS IMPORTO_INTERVENTO,                                                                  \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                        	         		                                \n"
        + "   TF.ID_FORNITORE,                                                                                    \n"
        + "   (TF.CODICE_FORNITORE || ' - ' || TF.RAGIONE_SOCIALE || ' - ' || to_char(DDS.DATA_DOCUMENTO_SPESA, 'dd/MM/yyyy') || ' - ' ||  DDS.NUMERO_DOCUMENTO_SPESA || ' - ' || DTDS.DESCRIZIONE) AS DESCR_FORNITORE,                              \n"
        + "   DS.ID_DOCUMENTO_SPESA,                                                                              \n"
        + "   DDS.ID_DETT_DOCUMENTO_SPESA,                                                                           \n"
        + "   DDS.DATA_DOCUMENTO_SPESA,                                                                           \n"
        + "   DDS.NUMERO_DOCUMENTO_SPESA,                                                                         \n"
        + "   DDS.ID_TIPO_DOCUMENTO_SPESA,                                                                        \n"
        + "   DTDS.DESCRIZIONE AS TIPO_DOCUMENTO,                                                                 \n"
        + "   DDS.DATA_PAGAMENTO,                                                                                 \n"
        + "   DDS.ID_DETT_DOCUMENTO_SPESA,                                                                        \n"
        + "   DDS.ID_MODALITA_PAGAMENTO,                                                                          \n"
        + "   DDS.IMPORTO_SPESA,                                                                                  \n"
        + "   DDS.IMPORTO_IVA_SPESA,                                                                              \n"
        + "   DDS.NOTE,                                                                                           \n"
        + "   DMP.DESCRIZIONE AS MOD_PAGAMENTO,                                                                   \n"
        + "   DRP.NUMERO_RICEVUTA_PAGAMENTO,                                                                      \n"
        + "   DRP.IMPORTO_PAGAMENTO,                                                                              \n"
        + "   DRP.DATA_PAGAMENTO AS DATA_PAG_RIC,                                                                 \n"
        + "   DRP.NOTE AS NOTE_RIC,                                                                               \n"
        /*
         * +
         * "    (SELECT SUM(PO1.IMPORTO_RENDICONTATO) FROM IUF_R_DOC_SPESA_INT_PROC_OGG PO1                      \n"
         * +
         * "  WHERE PO1.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN) AS IMPORTO_RENDICONTATO,    \n"
         */

        + "    DSI.IMPORTO AS  IMPORTO_ASSOCIATO,                                                             	\n"

        /*
         * +
         * "    ( SELECT SUM(S2.IMPORTO) FROM IUF_R_DOCUMENTO_SPESA_INTERV S2                    				\n"
         * +
         * "      WHERE S2.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                              				\n"
         * +
         * "    ) AS IMPORTO_ASSOCIATO ,                                                           				\n"
         */
        + "    ( SELECT SUM(S1.IMPORTO) FROM IUF_R_DOC_SPESA_INT_RICEV_PA S1                     				\n"
        + "      WHERE S1.ID_RICEVUTA_PAGAMENTO = RP.ID_RICEVUTA_PAGAMENTO                        				\n"
        + "      AND S1.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                				\n"
        + "      AND TI.ID_INTERVENTO = S1.ID_INTERVENTO				                            				\n"
        + "    ) AS PAGAMENTO_ASSOCIATO,                                                          				\n"
        + "																										\n"
        + "   DMPR.DESCRIZIONE AS DESCR_PAG_RICEVUTA,																\n"
        + " 	DRP.ID_DETT_RICEVUTA_PAGAMENTO                                                             			\n"
        + " FROM                                                                                                  \n"
        + "   IUF_T_DOCUMENTO_SPESA DS,                                                                           \n"
        + "   IUF_T_DETT_DOCUMENTO_SPESA DDS,                                                                     \n"
        + "   IUF_T_FORNITORE TF,                                                                                 \n"
        + "   IUF_D_TIPO_DOCUMENTO_SPESA DTDS,                                                                    \n"
        + "   IUF_D_MODALITA_PAGAMENTO DMP,                                                                       \n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV DSI,                                                                 \n"
        + "   IUF_T_INTERVENTO TI,                                                                                \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                                      \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DDI,                                                                   \n"
        + "   IUF_T_RICEVUTA_PAGAMENTO RP,                                                                        \n"
        + "   IUF_T_DETT_RICEVUTA_PAGAMENT DRP,                                                                  \n"
        + "   IUF_D_MODALITA_PAGAMENTO DMPR                                                                       \n"
        + " WHERE                                                                                                 \n"
        + "   DS.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                                               \n"
        + "   AND DDS.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                  \n"
        + "   AND DDS.DATA_FINE IS NULL						                                                    \n"
        + "   AND DTDS.ID_TIPO_DOCUMENTO_SPESA(+) = DDS.ID_TIPO_DOCUMENTO_SPESA                                   \n"
        + "   AND DMP.ID_MODALITA_PAGAMENTO(+) = DDS.ID_MODALITA_PAGAMENTO                                        \n"
        + "   AND DSI.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                  \n"
        + "   AND TI.ID_INTERVENTO = DSI.ID_INTERVENTO                                                            \n"
        + "   AND TI.ID_INTERVENTO = DI.ID_INTERVENTO                                                             \n"
        + "   AND TI.ID_PROCEDIMENTO = DS.ID_PROCEDIMENTO                                                         \n"
        + "   AND TI.ID_DESCRIZIONE_INTERVENTO   = DDI.ID_DESCRIZIONE_INTERVENTO                                  \n"
        + "   AND DDS.DATA_FINE IS NULL                                                                           \n"
        + "   AND TF.ID_FORNITORE (+)= DDS.ID_FORNITORE                                                           \n"
        + "   AND RP.ID_RICEVUTA_PAGAMENTO = DRP.ID_RICEVUTA_PAGAMENTO                                            \n"
        + "   AND RP.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                   \n"
        + "   AND DRP.DATA_FINE IS NULL						                                                    \n"
        + "   AND DMPR.ID_MODALITA_PAGAMENTO = DRP.ID_MODALITA_PAGAMENTO                                          \n"
        + " ORDER BY TI.ID_INTERVENTO, TF.ID_FORNITORE, DDS.NUMERO_DOCUMENTO_SPESA, DRP.NUMERO_RICEVUTA_PAGAMENTO \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<ExcelRicevutePagInterventoDTO>>()
          {

            @Override
            public List<ExcelRicevutePagInterventoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              String idIntervento;
              String idInterventoLast = "";
              String idDocumentoSpesa;
              String idDocumentoSpesaLast = "";
              long idDettRicLast = 0;
              long idDettRic;

              ExcelRicevutePagInterventoDTO riga = null;
              ExcelRigaDocumentoSpesaDTO doc = null;
              ExcelRigaRicevutaPagDTO ricevuta = null;
              ArrayList<ExcelRigaDocumentoSpesaDTO> documenti = null;
              ArrayList<ExcelRigaRicevutaPagDTO> ricevute = null;
              ArrayList<ExcelRicevutePagInterventoDTO> elenco = new ArrayList<ExcelRicevutePagInterventoDTO>();

              while (rs.next())
              {
                idIntervento = rs.getLong("ID_INTERVENTO") + " "
                    + rs.getLong("ID_FORNITORE");
                if (!idIntervento.equals(idInterventoLast))
                {
                  idInterventoLast = idIntervento;
                  riga = new ExcelRicevutePagInterventoDTO();
                  riga.setDescrIntervento(rs.getString("DESCR_INTERVENTO"));
                  riga.setUlterioriInformazioni(
                      rs.getString("ULTERIORI_INFORMAZIONI"));
                  riga.setDescrFornitore(rs.getString("DESCR_FORNITORE"));
                  riga.setProgressivo(rs.getString("PROGRESSIVO"));
                  documenti = new ArrayList<>();
                  riga.setDettaglioDocumento(documenti);
                  elenco.add(riga);
                  idDettRicLast = -1;
                }

                idDocumentoSpesa = rs.getLong("ID_INTERVENTO") + " "
                    + rs.getLong("ID_FORNITORE") + " "
                    + rs.getLong("ID_DETT_DOCUMENTO_SPESA");
                if (!idDocumentoSpesa.equals(idDocumentoSpesaLast))
                {
                  idDocumentoSpesaLast = idDocumentoSpesa;
                  doc = new ExcelRigaDocumentoSpesaDTO();
                  doc.setFornitore(rs.getString("DESCR_FORNITORE"));
                  doc.setIdFornitore(rs.getLong("ID_FORNITORE"));
                  riga.setDescrFornitore(doc.getFornitore());
                  doc.setIdDocumento(rs.getLong("ID_DOCUMENTO_SPESA"));
                  doc.setDataDocumentoStr(IuffiUtils.DATE
                      .formatDate(rs.getDate("DATA_DOCUMENTO_SPESA")));
                  doc.setTipoDocumento(rs.getString("TIPO_DOCUMENTO"));
                  doc.setImportoNetto(rs.getBigDecimal("IMPORTO_SPESA"));
                  doc.setImportoIva(rs.getBigDecimal("IMPORTO_IVA_SPESA"));
                  BigDecimal impo = BigDecimal.ZERO;
                  if (doc.getImportoNetto() != null)
                    impo = impo.add(doc.getImportoNetto());
                  if (doc.getImportoIva() != null)
                    impo = impo.add(doc.getImportoIva());
                  doc.setImportoLordo(impo);
                  // doc.setImportoRendicontato(rs.getBigDecimal("IMPORTO_RENDICONTATO"));
                  try
                  {
                    doc.setImportoRendicontato(findImportoRendicontaTO(
                        rs.getLong("ID_DOCUMENTO_SPESA"),
                        rs.getLong("ID_INTERVENTO")));
                  }
                  catch (InternalUnexpectedException e)
                  {
                    e.printStackTrace();
                    return null;
                  }
                  // doc.setImportoDaRendicontare(rs.getBigDecimal("IMPORTO_INTERVENTO"));
                  doc.setImportoAssociato(
                      rs.getBigDecimal("IMPORTO_ASSOCIATO"));
                  doc.setNoteDocumentoSpesa(rs.getString("NOTE"));
                  doc.setNumeroDocumento(
                      rs.getString("NUMERO_DOCUMENTO_SPESA"));
                  ricevute = new ArrayList<>();
                  doc.setRicevute(ricevute);
                  riga.getDettaglioDocumento().add(doc);
                }

                idDettRic = rs.getLong("ID_DETT_RICEVUTA_PAGAMENTO");
                if (idDettRic != idDettRicLast)
                {
                  idDettRicLast = idDettRic;
                  ricevuta = new ExcelRigaRicevutaPagDTO();
                  ricevuta.setDataPagamentoStr(IuffiUtils.DATE
                      .formatDate(rs.getDate("DATA_PAG_RIC")));
                  ricevuta.setImportoPagamento(
                      rs.getBigDecimal("IMPORTO_PAGAMENTO"));
                  ricevuta
                      .setModalitaPagamento(rs.getString("DESCR_PAG_RICEVUTA"));
                  ricevuta.setNote(rs.getString("NOTE_RIC"));
                  ricevuta.setNumeroPagamento(
                      rs.getString("NUMERO_RICEVUTA_PAGAMENTO"));
                  ricevuta.setImportoPagamentoAssociato(
                      rs.getBigDecimal("PAGAMENTO_ASSOCIATO"));
                  doc.getRicevute().add(ricevuta);
                }
              }
              return elenco;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimento", idProcedimento) },
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

  public List<ExcelRigaDocumentoSpesaDTO> getElencoExcelRicevutePagamento(
      long idProcedimento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoExcelRicevutePagamento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT                                                                                \n"
        + "    DS.ID_DOCUMENTO_SPESA,                                                             \n"
        + "    DDS.DATA_DOCUMENTO_SPESA,                                                          \n"
        + "    DDS.NUMERO_DOCUMENTO_SPESA,                                                        \n"
        + "    TF.ID_FORNITORE,                                                                   \n"
        + "    TF.CODICE_FORNITORE || ' - ' || TF.RAGIONE_SOCIALE AS DESCR_FORNITORE,             \n"
        + "    DDS.ID_TIPO_DOCUMENTO_SPESA,                                                       \n"
        + "    DTDS.DESCRIZIONE AS TIPO_DOCUMENTO,                                                \n"
        + "    DDS.DATA_PAGAMENTO,                                                                \n"
        + "    DDS.ID_MODALITA_PAGAMENTO,                                                         \n"
        + "    DDS.IMPORTO_SPESA,                                                                 \n"
        + "    DDS.IMPORTO_IVA_SPESA,                                                             \n"
        + "    DDS.NOTE,                                                                          \n"
        + "    DRP.NUMERO_RICEVUTA_PAGAMENTO,                                                     \n"
        + "    DRP.IMPORTO_PAGAMENTO,                                                             \n"
        + "    DMPR.DESCRIZIONE AS DESCR_PAG_RICEVUTA ,                                           \n"
        + "    DRP.DATA_PAGAMENTO AS DATA_PAG_RIC,                                                \n"
        + "    DRP.NOTE AS NOTE_RIC,                                                              \n"
        + "    ( SELECT SUM(S1.IMPORTO) FROM IUF_R_DOC_SPESA_INT_RICEV_PA S1                     \n"
        + "      WHERE S1.ID_RICEVUTA_PAGAMENTO = RP.ID_RICEVUTA_PAGAMENTO                        \n"
        + "      AND S1.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                \n"
        + "    ) AS PAGAMENTO_ASSOCIATO,                                                          \n"
        + "    ( SELECT SUM(S2.IMPORTO) FROM IUF_R_DOCUMENTO_SPESA_INTERV S2                    \n"
        + "      WHERE S2.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                              \n"
        + "    ) AS IMPORTO_ASSOCIATO                                                             \n"
        + "  FROM                                                                                 \n"
        + "    IUF_T_DOCUMENTO_SPESA DS,                                                          \n"
        + "    IUF_T_DETT_DOCUMENTO_SPESA DDS,                                                    \n"
        + "    IUF_T_FORNITORE TF,                                                                \n"
        + "    IUF_D_TIPO_DOCUMENTO_SPESA DTDS,                                                   \n"
        + "    IUF_T_RICEVUTA_PAGAMENTO RP,                                                       \n"
        + "    IUF_T_DETT_RICEVUTA_PAGAMENT DRP,                                                 \n"
        + "    IUF_D_MODALITA_PAGAMENTO DMPR                                                      \n"
        + "  WHERE                                                                                \n"
        + "    DS.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                              \n"
        + "    AND DDS.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                 \n"
        + "    AND DDS.DATA_FINE IS NULL							                                \n"
        + "    AND DTDS.ID_TIPO_DOCUMENTO_SPESA(+) = DDS.ID_TIPO_DOCUMENTO_SPESA                  \n"
        + "    AND DDS.DATA_FINE IS NULL                                                          \n"
        + "    AND TF.ID_FORNITORE (+) = DDS.ID_FORNITORE                                         \n"
        + "    AND RP.ID_RICEVUTA_PAGAMENTO = DRP.ID_RICEVUTA_PAGAMENTO (+)                       \n"
        + "    AND RP.ID_DOCUMENTO_SPESA (+)= DS.ID_DOCUMENTO_SPESA                               \n"
        + "    AND DRP.DATA_FINE IS NULL                                                          \n"
        + "    AND DMPR.ID_MODALITA_PAGAMENTO (+)= DRP.ID_MODALITA_PAGAMENTO                      \n"
        + "  ORDER BY DS.ID_DOCUMENTO_SPESA, DDS.NUMERO_DOCUMENTO_SPESA, TF.ID_FORNITORE,  DRP.NUMERO_RICEVUTA_PAGAMENTO \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<ExcelRigaDocumentoSpesaDTO>>()
          {

            @Override
            public List<ExcelRigaDocumentoSpesaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              long idDocumentoSpesa;
              long idDocumentoSpesaLast = 0;
              ExcelRigaDocumentoSpesaDTO doc = null;
              ExcelRigaRicevutaPagDTO ricevuta = null;
              ArrayList<ExcelRigaDocumentoSpesaDTO> documenti = new ArrayList<ExcelRigaDocumentoSpesaDTO>();
              ArrayList<ExcelRigaRicevutaPagDTO> ricevute = null;

              while (rs.next())
              {
                idDocumentoSpesa = rs.getLong("ID_DOCUMENTO_SPESA");
                if (idDocumentoSpesa != idDocumentoSpesaLast)
                {
                  idDocumentoSpesaLast = idDocumentoSpesa;
                  doc = new ExcelRigaDocumentoSpesaDTO();
                  doc.setDataDocumentoStr(IuffiUtils.DATE
                      .formatDate(rs.getDate("DATA_DOCUMENTO_SPESA")));
                  doc.setTipoDocumento(rs.getString("TIPO_DOCUMENTO"));
                  doc.setImportoNetto(rs.getBigDecimal("IMPORTO_SPESA"));
                  doc.setImportoIva(rs.getBigDecimal("IMPORTO_IVA_SPESA"));
                  doc.setImportoLordo(
                      IuffiUtils.NUMBERS.nvl(doc.getImportoNetto())
                          .add(IuffiUtils.NUMBERS.nvl(doc.getImportoIva())));
                  doc.setImportoAssociato(
                      rs.getBigDecimal("IMPORTO_ASSOCIATO"));
                  doc.setFornitore(rs.getString("DESCR_FORNITORE"));
                  doc.setNoteDocumentoSpesa(rs.getString("NOTE"));
                  doc.setNumeroDocumento(
                      rs.getString("NUMERO_DOCUMENTO_SPESA"));
                  ricevute = new ArrayList<>();
                  doc.setRicevute(ricevute);
                  documenti.add(doc);
                }

                ricevuta = new ExcelRigaRicevutaPagDTO();
                ricevuta.setDataPagamentoStr(
                    IuffiUtils.DATE.formatDate(rs.getDate("DATA_PAG_RIC")));
                ricevuta
                    .setImportoPagamento(rs.getBigDecimal("IMPORTO_PAGAMENTO"));
                ricevuta.setImportoPagamentoAssociato(
                    rs.getBigDecimal("PAGAMENTO_ASSOCIATO"));
                ricevuta
                    .setModalitaPagamento(rs.getString("DESCR_PAG_RICEVUTA"));
                ricevuta.setNote(rs.getString("NOTE_RIC"));
                ricevuta.setNumeroPagamento(
                    rs.getString("NUMERO_RICEVUTA_PAGAMENTO"));
                doc.getRicevute().add(ricevuta);

              }
              return documenti;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimento", idProcedimento) },
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

  public List<RegistroAntimafiaDTO> getRegistroAntimafia(String cuaa,
      String denominazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getRegistroAntimafia";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = "SELECT DISTINCT                                       	 		\n"
        + "     	DA.CUAA,               				     					\n"
        + "			DA.DENOMINAZIONE,                    						\n"
        + "			DA.INDIRIZZO_SEDE_LEGALE,	                   				\n"
        + "			RA.STATO_INTERNO STATO_CERTIFICATO,            				\n"
        + "     	RA.DATA_RICHIESTA,               				     		\n"
        + "			RA.DATA_SCADENZA,              		   						\n"
        + "			RA.SIGLA_PROV_PREFETTURA PREFETTURA_RIFERIMENTO 			\n"
        + "     FROM 														 	\n"
        + "			SMRGAA_V_DATI_ANAGRAFICI DA,								\n"
        + "			SIGOP.SIGOP_V_REGISTRO_ANTIMAFIA RA							\n"
        + "		WHERE 														 	\n";
    if (cuaa != null && !"".equals(cuaa))
      QUERY += "		DA.CUAA=:CUAA										\n";
    else
      if (denominazione != null && !"".equals(denominazione))
        QUERY += "		DA.DENOMINAZIONE LIKE :DENOMINAZIONE				\n";
    QUERY += " AND DA.DATA_FINE_VALIDITA IS NULL								\n";
    QUERY += " AND RA.CUAA = DA.CUAA											\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    if (cuaa != null && !"".equals(cuaa))
      mapParameterSource.addValue("CUAA", cuaa.trim(), Types.VARCHAR);
    else
      if (denominazione != null && !"".equals(denominazione))
        mapParameterSource.addValue("DENOMINAZIONE",
            "%" + denominazione.toUpperCase() + "%", Types.VARCHAR);
    try
    {
      return queryForList(QUERY, mapParameterSource,
          RegistroAntimafiaDTO.class);
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

  public List<RegistroAntimafiaDTO> getDatiAziendaRegistroAntimafia(String cuaa,
      String denominazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDatiAziendaRegistroAntimafia";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = "SELECT DISTINCT                                       	 		\n"
        + "     	DA.CUAA,               				     					\n"
        + "			DA.DENOMINAZIONE,                    						\n"
        + "			DA.INDIRIZZO_SEDE_LEGALE,	                   				\n"
        + "			RA.STATO_INTERNO STATO_CERTIFICATO,            				\n"
        + "     	RA.DATA_RICHIESTA,               				     		\n"
        + "			RA.DATA_SCADENZA,              		   						\n"
        + "			RA.SIGLA_PROV_PREFETTURA PREFETTURA_RIFERIMENTO 			\n"
        + "     FROM 														 	\n"
        + "			SMRGAA_V_DATI_ANAGRAFICI DA,								\n"
        + "			SIGOP.SIGOP_V_REGISTRO_ANTIMAFIA RA							\n"
        + "		WHERE 														 	\n";
    if (cuaa != null && !"".equals(cuaa))
      QUERY += "		DA.CUAA=:CUAA										\n";
    else
      if (denominazione != null && !"".equals(denominazione))
        QUERY += "		DA.DENOMINAZIONE LIKE :DENOMINAZIONE				\n";
    QUERY += " AND DA.DATA_FINE_VALIDITA IS NULL								\n";
    QUERY += " AND RA.CUAA (+)= DA.CUAA										\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    if (cuaa != null && !"".equals(cuaa))
      mapParameterSource.addValue("CUAA", cuaa.trim(), Types.VARCHAR);
    else
      if (denominazione != null && !"".equals(denominazione))
        mapParameterSource.addValue("DENOMINAZIONE",
            "%" + denominazione.toUpperCase() + "%", Types.VARCHAR);
    try
    {
      return queryForList(QUERY, mapParameterSource,
          RegistroAntimafiaDTO.class);
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

  public List<RegistroAntimafiaDTO> getDatiTitolareRegistroAntimafia(
      String cuaa, String denominazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDatiTitolareRegistroAntimafia";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = "SELECT DISTINCT                                       	 		\n"
        + "     	DA.CUAA,               				     					\n"
        + "			DA.DENOMINAZIONE,                    						\n"
        + "			DA.INDIRIZZO_SEDE_LEGALE,                   				\n"
        + "     	SC.CODICE_FISCALE,               				     		\n"
        + "			SC.COGNOME,                 		   						\n"
        + "			SC.NOME,					                   				\n"
        + "			SC.RES_TELEFONO TELEFONO,                   				\n"
        + "			SC.RES_MAIL EMAIL,			                   				\n"
        + "			RA.STATO_INTERNO STATO_CERTIFICATO,            				\n"
        + "     	RA.DATA_RICHIESTA,               				     		\n"
        + "			RA.DATA_SCADENZA,              		   						\n"
        + "			RA.SIGLA_PROV_PREFETTURA PREFETTURA_RIFERIMENTO 			\n"
        + "     FROM 														 	\n"
        + "			SMRGAA_V_DATI_ANAGRAFICI DA,								\n"
        + "			SMRGAA_V_SOGGETTI_COLLEGATI SC,								\n"
        + "			SIGOP.SIGOP_V_REGISTRO_ANTIMAFIA RA							\n"
        + "		WHERE 														 	\n";
    if (cuaa != null && !"".equals(cuaa))
      QUERY += "		DA.CUAA=:CUAA										\n";
    else
      if (denominazione != null && !"".equals(denominazione))
        QUERY += "		DA.DENOMINAZIONE LIKE :DENOMINAZIONE				\n";
    QUERY += " AND DA.ID_AZIENDA = SC.ID_AZIENDA								\n";
    QUERY += " AND DA.DATA_FINE_VALIDITA IS NULL								\n";
    QUERY += " AND SC.ID_RUOLO = 1											\n";
    QUERY += " AND SC.DATA_FINE_VALIDITA IS NULL								\n";
    QUERY += " AND RA.CUAA (+)= DA.CUAA										\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    if (cuaa != null && !"".equals(cuaa))
      mapParameterSource.addValue("CUAA", cuaa.trim(), Types.VARCHAR);
    else
      if (denominazione != null && !"".equals(denominazione))
        mapParameterSource.addValue("DENOMINAZIONE",
            "%" + denominazione.toUpperCase() + "%", Types.VARCHAR);
    try
    {
      return queryForList(QUERY, mapParameterSource,
          RegistroAntimafiaDTO.class);
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

  public BigDecimal getImportoDocSpesaIntRicPag(long idRicevutaPagamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoDocSpesaIntRic]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                         	\n"
        + "         SUM(IMPORTO) as IMPORTO                        		   	\n"
        + "       FROM                                                     	\n"
        + "         IUF_R_DOC_SPESA_INT_RICEV_PA                          	\n"
        + " WHERE 								 							\n"
        + " 	 ID_RICEVUTA_PAGAMENTO = :ID_RICEVUTA_PAGAMENTO				\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idDettRicevutaPagamento",
                  idRicevutaPagamento) },
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

  public void insertTDocSpesaFile(DocumentoSpesaVO documentoVO,
      String codAttore) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertTDocSpesaFile]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO IUF_T_DOCUMENTO_SPESA_FILE 	\n"
        + " (ID_DOCUMENTO_SPESA_FILE, 								\n"
        + "	 ID_DOCUMENTO_SPESA,									\n"
        + "	 NOME_FILE_LOGICO_DOCUMENTO_SPE,						\n"
        + "	 NOME_FILE_FISICO_DOCUMENTO_SPE,						\n"
        + "	 FILE_DOCUMENTO_SPESA,									\n"
        + "	 EXT_COD_ATTORE,										\n"
        + "	 DATA_ULTIMO_AGGIORNAMENTO								\n"
        + "	) 														\n"
        + "VALUES 													\n"
        + " (:ID_DOCUMENTO_SPESA_FILE, 								\n"
        + "	 :ID_DOCUMENTO_SPESA,									\n"
        + "	 :NOME_FILE_LOGICO_DOCUMENTO_SPE,						\n"
        + "	 :NOME_FILE_FISICO_DOCUMENTO_SPE,						\n"
        + "	 :FILE_DOCUMENTO_SPESA,									\n"
        + "	 :EXT_COD_ATTORE,										\n"
        + "	 SYSDATE												\n"
        + "	) 														\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long id = 0;
    try
    {
      id = getNextSequenceValue("SEQ_IUF_T_DOCUMENTO_SPESA_FI");
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA_FILE", id, Types.NUMERIC);
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA",
          documentoVO.getIdDocumentoSpesa(), Types.NUMERIC);
      mapParameterSource.addValue("NOME_FILE_LOGICO_DOCUMENTO_SPE",
          documentoVO.getNomeFileLogicoDocumentoSpe(), Types.VARCHAR);
      mapParameterSource.addValue("FILE_DOCUMENTO_SPESA",
          new SqlLobValue(documentoVO.getFileDocumentoSpesa()), Types.BLOB);
      mapParameterSource.addValue("NOME_FILE_FISICO_DOCUMENTO_SPE",
          documentoVO.getNomeFileFisicoDocumentoSpe(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_COD_ATTORE", codAttore, Types.VARCHAR);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DOCUMENTO_SPESA_FILE", id),
              new LogParameter("ID_DOCUMENTO_SPESA",
                  documentoVO.getIdDocumentoSpesa())
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

  public void updateTDocSpesaFile(DocumentoSpesaVO documentoVO,
      String codAttore) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateTDocSpesaFile]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String UPDATE = " UPDATE                                                                       \n"
        + "   IUF_T_DOCUMENTO_SPESA_FILE            		 	                         \n"
        + "   SET                                                                        \n"
        + "     NOME_FILE_LOGICO_DOCUMENTO_SPE = :NOME_FILE_LOGICO_DOCUMENTO_SPE	     \n";
    if (documentoVO.getFileDocumentoSpesa() != null)
    {
      UPDATE += "     ,FILE_DOCUMENTO_SPESA = :FILE_DOCUMENTO_SPESA,			       			 \n"
          + "     NOME_FILE_FISICO_DOCUMENTO_SPE = :NOME_FILE_FISICO_DOCUMENTO_SPE         \n";
    }
    UPDATE += "   ,EXT_COD_ATTORE = :EXT_COD_ATTORE          									 \n";

    UPDATE += "   WHERE ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA           						 \n"
        + "	 AND ID_DOCUMENTO_SPESA_FILE = :ID_DOCUMENTO_SPESA_FILE							 \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA",
          documentoVO.getIdDocumentoSpesa(), Types.NUMERIC);
      Long idDocSpesaFile = documentoVO.getIdDocumentoSpesaFile();
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA_FILE", idDocSpesaFile,
          Types.NUMERIC);
      String nomeLogico = documentoVO.getNomeFileLogicoDocumentoSpe();
      mapParameterSource.addValue("NOME_FILE_LOGICO_DOCUMENTO_SPE", nomeLogico,
          Types.VARCHAR);
      if (documentoVO.getFileDocumentoSpesa() != null)
      {
        mapParameterSource.addValue("FILE_DOCUMENTO_SPESA",
            new SqlLobValue(documentoVO.getFileDocumentoSpesa()), Types.BLOB);
        mapParameterSource.addValue("NOME_FILE_FISICO_DOCUMENTO_SPE",
            documentoVO.getNomeFileFisicoDocumentoSpe(), Types.VARCHAR);
      }
      mapParameterSource.addValue("EXT_COD_ATTORE", codAttore, Types.VARCHAR);

      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idDocumentoSpesa",
                  documentoVO.getIdDocumentoSpesa()),
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

  public List<DocumentoSpesaVO> getElencoAllegatiDocSpesa(long idDocumentoSpesa,
      ArrayList<String> idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoAllegatiDocSpesa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "   SELECT DISTINCT												\n"
        + "		 FIL.ID_DOCUMENTO_SPESA_FILE,										\n"
        + "		 PO.ID_PROCEDIMENTO_OGGETTO,										\n"
        + "		 (SELECT CODICE FROM IUF_D_ESITO WHERE CODICE = 'APP-N' 			\n"
        + "		  AND ID_ESITO IN (													\n"
        + "	      SELECT ID_ESITO FROM IUF_T_PROCEDIMENTO_OGGETTO PO1, IUF_R_DOC_SPESA_INT_ACC_SPES ACC1, IUF_R_DOCUMENTO_SPESA_INTERV DSI1				\n"
        + "	      WHERE PO1.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO					\n"
        + "	      AND PO1.ID_PROCEDIMENTO_OGGETTO<>PO.ID_PROCEDIMENTO_OGGETTO		\n"
        + "	  	  AND ACC1.ID_PROCEDIMENTO_OGGETTO = PO1.ID_PROCEDIMENTO_OGGETTO	\n"
        + "	  	  AND DSI1.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA					\n"
        + "	  	  AND DSI1.ID_DOCUMENTO_SPESA_INTERVEN = ACC1.ID_DOCUMENTO_SPESA_INTERVEN	\n"
        + "	     )) CODICE_ESITO,													\n"
        + "		 O.DESCRIZIONE AS DESCR_OGG,										\n"
        + "      NVL(SUBSTR(PO.IDENTIFICATIVO,13),'') as PREFIX,					\n"
        + "		 FIL.NOME_FILE_LOGICO_DOCUMENTO_SPE AS SUFFIX,             			\n"
        + "      FIL.NOME_FILE_FISICO_DOCUMENTO_SPE                     			\n"
        + "    FROM                                                     			\n"
        + "      IUF_T_DOCUMENTO_SPESA_FILE FIL,									\n"
        + "		 IUF_R_DOC_SPESA_PROC_OGG DSPO,										\n"
        + "		 IUF_R_LEGAME_GRUPPO_OGGETTO LGO,									\n"
        + "		 IUF_D_OGGETTO O,													\n"
        + "		 IUF_T_PROCEDIMENTO_OGGETTO PO                       				\n"
        + "    WHERE                                                    			\n"
        + "      FIL.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA      					\n"
        + "		 AND DSPO.ID_DOCUMENTO_SPESA_FILE (+)= FIL.ID_DOCUMENTO_SPESA_FILE  \n"
        + "		 AND PO.ID_PROCEDIMENTO_OGGETTO (+)= DSPO.ID_PROCEDIMENTO_OGGETTO   \n"
        + "		 AND LGO.ID_LEGAME_GRUPPO_OGGETTO (+)= PO.ID_LEGAME_GRUPPO_OGGETTO  \n"
        + "		 AND O.ID_OGGETTO (+)= LGO.ID_OGGETTO   							\n"
        + "		 ORDER BY PO.ID_PROCEDIMENTO_OGGETTO ASC, FIL.ID_DOCUMENTO_SPESA_FILE DESC	\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DocumentoSpesaVO>>()
          {
            @Override
            public List<DocumentoSpesaVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DocumentoSpesaVO> list = new ArrayList<>();
              while (rs.next())
              {
                DocumentoSpesaVO file = new DocumentoSpesaVO();
                String prefix = rs.getString("PREFIX");
                String suffix = rs.getString("SUFFIX");
                String nomeFileLogico = "";
                if (prefix != null && prefix != "")
                {
                  nomeFileLogico += prefix + " - ";
                  file.setPrefix(prefix);
                }
                nomeFileLogico += suffix;
                file.setIdDocumentoSpesaFile(
                    rs.getLong("ID_DOCUMENTO_SPESA_FILE"));
                file.setNomeFileFisicoDocumentoSpe(
                    rs.getString("NOME_FILE_FISICO_DOCUMENTO_SPE"));
                file.setNomeFileLogicoDocumentoSpe(nomeFileLogico);
                String descrOgg = rs.getString("DESCR_OGG");

                String codEsito = rs.getString("CODICE_ESITO");
                if (codEsito != null && "APP-N".equals(codEsito))
                  file.setPoApprovatoNegativo(true);

                if (idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato != null
                    && idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato
                        .contains("000"))
                {
                  // SOLO SE NON E' GIA STATO TUTTO RENDICONTATO
                  // SUM(IMPORTO_RENDICONTATO) di IUF_R_DOC_SPESA_INT_PROC_OGG
                  // per ID_DOCUMENTO SPESA
                  // == SUM(IMPORTO) di IUF_R_DOCUMENTO_SPESA_INTERV per
                  // ID_DOCUMENTO_SPESA
                  BigDecimal impAss;
                  BigDecimal impRend;
                  try
                  {
                    impAss = getImportoAssociatoDoc(idDocumentoSpesa);
                    impRend = getImportoRendicontatoDoc(idDocumentoSpesa);

                  }
                  catch (InternalUnexpectedException e)
                  {
                    return null;
                  }

                  if (impAss == null)
                    impAss = BigDecimal.ZERO;
                  if (impRend == null)
                    impRend = BigDecimal.ZERO;

                  if (impAss.compareTo(BigDecimal.ZERO) == 0
                      && impRend.compareTo(BigDecimal.ZERO) == 0)
                    list.add(file);

                  if (impAss.subtract(impRend).compareTo(BigDecimal.ZERO) != 0)
                    // if(prefix==null || prefix=="")
                    // {
                    list.add(file);
                  // }
                }

                if (idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato != null)
                {
                  if (idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato
                      .contains(descrOgg + " - " + prefix))
                  {
                    boolean insert = true;
                    for (DocumentoSpesaVO f : list)
                      if (f.getIdDocumentoSpesaFile() == file
                          .getIdDocumentoSpesaFile())
                        insert = false;

                    if (insert)
                      list.add(file);
                  }
                }
                else
                  list.add(file);
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

              new LogParameter("idDocumentoSpesa", idDocumentoSpesa)
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

  public boolean canDeleteDocSpesa(long idDocumentoSpesa)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::canDeleteDocSpesa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT           																		\n"
        + "   COUNT(*)                      													\n"
        + " FROM                            													\n"
        + "   IUF_R_DOC_SPESA_INT_PROC_OGG    													\n"
        + " WHERE                           													\n"
        + "   ID_DOCUMENTO_SPESA_INTERVEN IN 													\n"
        + "			(SELECT DISTINCT ID_DOCUMENTO_SPESA_INTERVEN 								\n"
        + "				FROM IUF_R_DOCUMENTO_SPESA_INTERV										\n"
        + "				WHERE ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA)							\n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
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

  public List<ProcedimentoOggettoDTO> getElencoProcOggDoc(long idDocumentoSpesa,
      ArrayList<String> idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getElencoProcOggDoc";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = "SELECT DISTINCT                                       	 				   	 				   	 				   	 				   	 				   	 		\n"
        + "      	NVL(SUBSTR(PO.IDENTIFICATIVO,13),'') as PREFIX,						   	 				   	 				   	 				   	 				   	 		\n"
        + "      	O.DESCRIZIONE AS DESCR_OGGETTO,										   	 				   	 				   	 				   	 				   	 		\n"
        + "      	PO.ID_PROCEDIMENTO_OGGETTO,										   	 				   	 				   	 				   	 				   	 			\n"
        + "      ((SELECT SUM(F3.IMPORTO_RENDICONTATO) FROM IUF_R_DOC_SPESA_INT_PROC_OGG F3  , IUF_R_DOCUMENTO_SPESA_INTERV C2                                                  \n"
        + "       WHERE F3.ID_DOCUMENTO_SPESA_INTERVEN = C2.ID_DOCUMENTO_SPESA_INTERVEN AND C2.ID_DOCUMENTO_SPESA = DSPO.ID_DOCUMENTO_SPESA                                       \n"
        + "       AND F3.ID_PROCEDIMENTO_OGGETTO = DSPO.ID_PROCEDIMENTO_OGGETTO															               							\n"
        + "     )	   	 				   	 				   	 				   	 				   	 		   	 				   	 				   	 						\n"
        + "	   	 				   	 				   	 				   	 				   	 		   	 				   	 				   	 							\n"
        + "       -                                                                                                                                                               \n"
        + "         ( SELECT NVL(SUM(C1.IMPORTO_DISPONIBILE),0) FROM IUF_R_DOC_SPESA_INT_ACC_SPES C1, IUF_R_DOCUMENTO_SPESA_INTERV C2                                          \n"
        + "          WHERE C1.ID_DOCUMENTO_SPESA_INTERVEN= C2.ID_DOCUMENTO_SPESA_INTERVEN AND C2.ID_DOCUMENTO_SPESA = DSPO.ID_DOCUMENTO_SPESA                                     \n"
        + "          AND C1.ID_PROCEDIMENTO_OGGETTO = DSPO.ID_PROCEDIMENTO_OGGETTO	)														            						\n"
        + ") AS IMPORTO_RENDICONTATO, 																							\n"

        + "		 (SELECT  COUNT(*) FROM 																						\n"
        + "				IUF_T_PROCEDIMENTO_OGGETTO PO1, 																		\n"
        + "				IUF_D_ESITO E																							\n"
        + "	      WHERE 																										\n"
        + "				PO1.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO																\n"
        + "				AND PO1.ID_ESITO = E.ID_ESITO																			\n"
        + "				AND E.CODICE = 'APP-N'																					\n"
        + "	      		AND PO1.ID_PROCEDIMENTO_OGGETTO<>PO.ID_PROCEDIMENTO_OGGETTO												\n"
        + "	      		AND PO1.CODICE_RAGGRUPPAMENTO = PO.CODICE_RAGGRUPPAMENTO												\n"
        + "	     ) NUMERO_IST_NEG,																								\n"
        + "	    PO.ID_PROCEDIMENTO_OGGETTO                                                  									\n"
        + "     FROM 														 													\n"
        + "			IUF_R_DOC_SPESA_PROC_OGG DSPO,																				\n"
        + "			IUF_T_PROCEDIMENTO_OGGETTO PO,																				\n"
        + "			IUF_R_LEGAME_GRUPPO_OGGETTO LGO,																			\n"
        + "			IUF_D_OGGETTO O																								\n"
        + "		WHERE 														 													\n"
        + "			DSPO.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA																\n"
        + " 		AND DSPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO												\n"
        + " 		AND LGO.ID_LEGAME_GRUPPO_OGGETTO = PO.ID_LEGAME_GRUPPO_OGGETTO												\n"
        + " 		AND LGO.ID_OGGETTO = O.ID_OGGETTO																			\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
        Types.NUMERIC);

    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<ProcedimentoOggettoDTO>>()
          {
            @Override
            public List<ProcedimentoOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<ProcedimentoOggettoDTO> list = new ArrayList<>();
              while (rs.next())
              {
                ProcedimentoOggettoDTO po = new ProcedimentoOggettoDTO();
                String prefix = rs.getString("PREFIX");
                po.setPrefix(prefix);
                po.setImportoRendicontato(
                    rs.getBigDecimal("IMPORTO_RENDICONTATO"));
                po.setIdProcedimentoOggetto(
                    rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
                po.setDescrOggetto(rs.getString("DESCR_OGGETTO"));
                String descrOgg = rs.getString("DESCR_OGGETTO");

                Integer numIstrAppN = rs.getInt("NUMERO_IST_NEG");
                po.setNumIstrNeg(numIstrAppN);

                if (idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato != null
                    && idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato
                        .contains("000"))
                {
                  // SOLO SE NON E' GIA STATO TUTTO RENDICONTATO
                  // SUM(IMPORTO_RENDICONTATO) di IUF_R_DOC_SPESA_INT_PROC_OGG
                  // per ID_DOCUMENTO SPESA
                  // == SUM(IMPORTO) di IUF_R_DOCUMENTO_SPESA_INTERV per
                  // ID_DOCUMENTO_SPESA
                  BigDecimal impAss;
                  BigDecimal impRend;
                  try
                  {
                    impAss = getImportoAssociatoDoc(idDocumentoSpesa);
                    impRend = getImportoRendicontatoDoc(idDocumentoSpesa);

                  }
                  catch (InternalUnexpectedException e)
                  {
                    return null;
                  }

                  if (impAss == null)
                    impAss = BigDecimal.ZERO;
                  if (impRend == null)
                    impRend = BigDecimal.ZERO;

                  if (impAss.subtract(impRend).compareTo(BigDecimal.ZERO) != 0)
                    // if(prefix==null || prefix=="")
                    // {
                    list.add(po);
                  // }
                }

                if (idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato != null)
                {
                  if (idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato
                      .contains(descrOgg + " - " + prefix))
                  {
                    boolean insert = true;
                    for (ProcedimentoOggettoDTO p : list)
                      if (p.getIdProcedimentoOggetto() == po
                          .getIdProcedimentoOggetto())
                        insert = false;

                    if (insert)
                      list.add(po);
                  }
                }
                else
                  list.add(po);

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

              new LogParameter("idDocumentoSpesa", idDocumentoSpesa)
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

  public List<DocumentoSpesaVO> getElencoDocumentiSpesaNew(long idProcedimento,
      ArrayList<String> idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoDocumentiSpesaNew]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    // idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato
    /*
     * è tipo formato dal CriterioNumIdentificativo___CodiceRaggruppamento
     * separati da &&& relativi a IUF_D_OGGETTO_IUF_T_PROCEDIMENTO_OGGETTO
     */
    final String QUERY = "   SELECT                                                                                                                                                              \n"
        + "      C.ID_DOCUMENTO_SPESA,                                                                                                                                            \n"
        + "      C.ID_DETT_DOCUMENTO_SPESA,                                                                                                                                       \n"
        + "      C.ID_FORNITORE,                                                                                                                                                  \n"
        + "      C.ID_TIPO_DOCUMENTO_SPESA,                                                                                                                                       \n"
        + "      C.ID_MODALITA_PAGAMENTO,                                                                                                                                         \n"
        + "      C.DATA_DOCUMENTO_SPESA,                                                                                                                                          \n"
        + "      C.NUMERO_DOCUMENTO_SPESA,                                                                                                                                        \n"
        + "      C.DATA_PAGAMENTO,                                                                                                                                                \n"
        + "      C.IMPORTO_SPESA,                                                                                                                                                 \n"
        + "      C.IMPORTO_IVA_SPESA IMPORTO_IVA,                                                                                                                                 \n"
        + "      C.NOTE,                                                                                                                                                          \n"
        + "      D.DESCRIZIONE AS DESCR_MOD_PAGAMENTO,                                                                                                                            \n"
        + "      E.DESCRIZIONE AS DESCR_TIPO_DOCUMENTO,                                                                                                                           \n"
        + "      F.CODICE_FORNITORE,                                                                                                                                              \n"
        + "      F.RAGIONE_SOCIALE,                                                                                                                                               \n"
        + "      F.INDIRIZZO_SEDE_LEGALE,                                                                                                                                         \n"
        + "                                                                                                                                                                       \n"
        + " (                                                                                                                                                                \n"
        + "         (SELECT NVL(SUM(F1.IMPORTO),0) FROM IUF_R_DOCUMENTO_SPESA_INTERV F1                                                                                         \n"
        + "         WHERE F1.ID_DOCUMENTO_SPESA = C.ID_DOCUMENTO_SPESA)    ) as IMPORTO_ASSOCIATO,																					\n"
        + "                                                                                                                                                                       \n"
        + "     "
        + "      DECODE((SELECT DISTINCT B1.ID_DOCUMENTO_SPESA                                                                                                                    \n"
        + "         FROM IUF_R_DOCUMENTO_SPESA_INTERV B1                                                                 	                      								\n"
        + "         WHERE B1.ID_DOCUMENTO_SPESA = C.ID_DOCUMENTO_SPESA), NULL, 'N', 'S') AS FLAG_HAS_INTERVENTI_ASSOCIATI, 														\n"

        + "      DECODE((SELECT DISTINCT B1.ID_DOCUMENTO_SPESA                                                                                                                    \n"
        + "         FROM IUF_R_DOC_SPESA_INT_PROC_OGG C1, IUF_R_DOCUMENTO_SPESA_INTERV B1                                                                                       \n"
        + "         WHERE B1.ID_DOCUMENTO_SPESA = C.ID_DOCUMENTO_SPESA AND B1.ID_DOCUMENTO_SPESA_INTERVEN = C1.ID_DOCUMENTO_SPESA_INTERVEN ), NULL, 'S', 'N') AS FLAG_ELIMINABILE \n"
        + "    FROM                                                                                                                                                               \n"
        + "      IUF_T_DOCUMENTO_SPESA A,                                                                                                                                         \n"
        + "      IUF_T_DETT_DOCUMENTO_SPESA C,                                                                                                                                    \n"
        + "      IUF_D_MODALITA_PAGAMENTO D,                                                                                                                                      \n"
        + "      IUF_D_TIPO_DOCUMENTO_SPESA E,                                                                                                                                    \n"
        + "      IUF_T_FORNITORE F                                                                                                                                                \n"
        + "    WHERE                                                                                                                                                              \n"
        + "      A.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                                                                                                             \n"
        + "      AND A.ID_DOCUMENTO_SPESA = C.ID_DOCUMENTO_SPESA                                                                                                                  \n"
        + "      AND C.DATA_FINE IS NULL                                                                                                                 							\n"
        + "      AND D.ID_MODALITA_PAGAMENTO(+) = C.ID_MODALITA_PAGAMENTO                                                                                                         \n"
        + "      AND E.ID_TIPO_DOCUMENTO_SPESA = C.ID_TIPO_DOCUMENTO_SPESA                                                                                                        \n"
        + "      AND F.ID_FORNITORE(+) = C.ID_FORNITORE                                                                                                                           \n"
        + "    ORDER BY C.ID_DOCUMENTO_SPESA, C.DATA_PAGAMENTO asc, C.DATA_DOCUMENTO_SPESA asc, C.NUMERO_DOCUMENTO_SPESA asc                                                      \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, DocumentoSpesaVO.class);
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

  public void deleteRDocSpesaIntRicPag(long[] idDocumentoSpesa,
      long idIntervento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRDocSpesaIntRicPag]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " DELETE                                                 \n"
        + " FROM                                                   \n"
        + "   IUF_R_DOC_SPESA_INT_RICEV_PA		                 \n"
        + " WHERE                                                  \n"
        + "   ID_INTERVENTO = :ID_INTERVENTO                       \n"
        + getInCondition("ID_DOCUMENTO_SPESA", idDocumentoSpesa);

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.VARCHAR);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
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

  public List<DocumentoSpesaVO> getElencoAllegatiDocSpesaWithPONotIn1090(
      long idDocumentoSpesa) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoAllegatiDocSpesaWithPONotIn1090]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "   SELECT DISTINCT												\n"
        + "		 FIL.ID_DOCUMENTO_SPESA_FILE,										\n"
        + "      NVL(SUBSTR(PO.IDENTIFICATIVO,13),'') as PREFIX,					\n"
        + "		 FIL.NOME_FILE_LOGICO_DOCUMENTO_SPE AS SUFFIX,             			\n"
        + "      FIL.NOME_FILE_FISICO_DOCUMENTO_SPE                     			\n"
        + "    FROM                                                     			\n"
        + "      IUF_T_DOCUMENTO_SPESA_FILE FIL,									\n"
        + "		 IUF_R_DOC_SPESA_PROC_OGG DSPO,										\n"
        + "		 IUF_T_ITER_PROCEDIMENTO_OGGE IT,									\n"
        + "		 IUF_T_PROCEDIMENTO_OGGETTO PO                       				\n"
        + "    WHERE                                                    			\n"
        + "      FIL.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA      					\n"
        + "		 AND DSPO.ID_DOCUMENTO_SPESA_FILE = FIL.ID_DOCUMENTO_SPESA_FILE  	\n"
        + "		 AND PO.ID_PROCEDIMENTO_OGGETTO = DSPO.ID_PROCEDIMENTO_OGGETTO   	\n"
        + "   	 AND PO.ID_PROCEDIMENTO_OGGETTO = IT.ID_PROCEDIMENTO_OGGETTO   		\n"
        + "   	 AND IT.DATA_FINE IS NULL                                     		\n"
        + "   	 AND IT.ID_STATO_OGGETTO NOT BETWEEN 10 AND 90                    	\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DocumentoSpesaVO>>()
          {
            @Override
            public List<DocumentoSpesaVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DocumentoSpesaVO> list = new ArrayList<>();
              while (rs.next())
              {
                DocumentoSpesaVO file = new DocumentoSpesaVO();
                String prefix = rs.getString("PREFIX");
                String suffix = rs.getString("SUFFIX");
                String nomeFileLogico = "";
                if (prefix != null && prefix != "")
                {
                  nomeFileLogico += prefix + " - ";
                  file.setPrefix(prefix);
                }
                nomeFileLogico += suffix;
                file.setIdDocumentoSpesaFile(
                    rs.getLong("ID_DOCUMENTO_SPESA_FILE"));
                file.setNomeFileFisicoDocumentoSpe(
                    rs.getString("NOME_FILE_FISICO_DOCUMENTO_SPE"));
                file.setNomeFileLogicoDocumentoSpe(nomeFileLogico);
                list.add(file);
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

              new LogParameter("idDocumentoSpesa", idDocumentoSpesa)
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

  public BigDecimal getSommaImportoRicevuteRendicontate(long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getSommaImportoRicevute]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                         	\n"
        + "        SUM(IMPORTO_rendicontato) AS IMPORTO                     \n"
        + "      FROM                                                     	\n"
        + "         IUF_R_DOC_SPESA_int_PROC_OGG	                        \n"
        + " 	WHERE 														\n"
        + "		 ID_DOCUMENTO_SPESA_INTERVEN IN (SELECT ID_DOCUMENTO_SPESA_INTERVEN FROM IUF_R_DOCUMENTO_SPESA_INTERV WHERE ID_DOCUMENTO_SPESA=:ID_DOCUMENTO_SPESA)					\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
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

  public BigDecimal getSommaImportoRicevute(long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getSommaImportoRicevute]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                         	\n"
        + "        SUM(IMPORTO) AS IMPORTO                                  \n"
        + "      FROM                                                     	\n"
        + "         IUF_R_DOC_SPESA_INT_RICEV_PA                          	\n"
        + " 	WHERE 														\n"
        + "		 ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA					\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
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

  public List<DocumentoSpesaVO> getElencoAllegatiDocSpesaGiaRendicontato(
      long idDocumentoSpesa, Object object) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoAllegatiDocSpesaGiaRendicontato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "   SELECT DISTINCT															\n"
        + "		 FIL.ID_DOCUMENTO_SPESA_FILE,													\n"
        + "		 FIL.NOME_FILE_LOGICO_DOCUMENTO_SPE AS SUFFIX,             						\n"
        + "      FIL.NOME_FILE_FISICO_DOCUMENTO_SPE                     						\n"
        + "    FROM                                                     						\n"
        + "      IUF_T_DOCUMENTO_SPESA_FILE FIL													\n"
        + "    WHERE                                                    						\n"
        + "      FIL.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA      								\n"
        + "		 AND FIL.ID_DOCUMENTO_SPESA_FILE NOT IN 										\n"
        + "			(	SELECT 																	\n"
        + "					ID_DOCUMENTO_SPESA_FILE 											\n"
        + "				FROM 																	\n"
        + "					IUF_R_DOC_SPESA_PROC_OGG DSPO 										\n"
        + "				WHERE     												   				\n"
        + "					DSPO.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA       )				\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DocumentoSpesaVO>>()
          {
            @Override
            public List<DocumentoSpesaVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DocumentoSpesaVO> list = new ArrayList<>();
              while (rs.next())
              {
                DocumentoSpesaVO file = new DocumentoSpesaVO();
                String prefix = "";
                String suffix = rs.getString("SUFFIX");
                String nomeFileLogico = "";
                if (prefix != null && prefix != "")
                {
                  nomeFileLogico += prefix + " - ";
                  file.setPrefix(prefix);
                }
                nomeFileLogico += suffix;
                file.setIdDocumentoSpesaFile(
                    rs.getLong("ID_DOCUMENTO_SPESA_FILE"));
                file.setNomeFileFisicoDocumentoSpe(
                    rs.getString("NOME_FILE_FISICO_DOCUMENTO_SPE"));
                file.setNomeFileLogicoDocumentoSpe(nomeFileLogico);
                list.add(file);

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

              new LogParameter("idDocumentoSpesa", idDocumentoSpesa)
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

  public boolean checkIfRecordFileNotInRDocSpesaPO(long idDocumentoSpesa)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::checkIfRecordFileNotInRDocSpesaPO]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT           															\n"
        + "   COUNT(*)                      													\n"
        + " FROM                            													\n"
        + "   IUF_T_DOCUMENTO_SPESA_FILE    													\n"
        + " WHERE                           													\n"
        + "   ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA											\n"
        + "	  AND ID_DOCUMENTO_SPESA_FILE NOT IN (SELECT ID_DOCUMENTO_SPESA_FILE FROM IUF_R_DOC_SPESA_PROC_OGG WHERE ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA)";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
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

  public boolean canDeleteRicevutaDocGiaRendicontato(
      long idDettRicevutaPagamento)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::canDeleteRicevutaDocGiaRendicontato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT           																		\n"
        + "   COUNT(*)                      													\n"
        + "	FROM IUF_R_DOC_SPESA_PROC_OGG  														\n"
        + "					WHERE ID_RICEVUTA_PAGAMENTO = 										\n"
        + "					(SELECT DISTINCT ID_RICEVUTA_PAGAMENTO 								\n"
        + "						FROM IUF_T_DETT_RICEVUTA_PAGAMENT								\n"
        + "						WHERE ID_DETT_RICEVUTA_PAGAMENTO = :ID_DETT_RICEVUTA_PAGAMENTO) \n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DETT_RICEVUTA_PAGAMENTO",
          idDettRicevutaPagamento, Types.NUMERIC);
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

  public BigDecimal getImportoAssociatoDoc(long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoAssociatoDoc]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                         	\n"
        + "         SUM(IMPORTO) AS IMPORTO                        		   	\n"
        + "     FROM                                                     	\n"
        + "         IUF_R_DOCUMENTO_SPESA_INTERV                         	\n"
        + " 	WHERE 														\n"
        + "	 		ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA					\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
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

  public BigDecimal getImportoRendicontatoDoc(long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoRendicontatoDoc]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    /*
     * final String QUERY =
     * "  SELECT                                         							\n" +
     * "         SUM(IMPORTO_RENDICONTATO) AS IMPORTO                    						\n"
     * +
     * "     FROM                                                     							\n"
     * +
     * "         IUF_R_DOC_SPESA_INT_PROC_OGG A,													\n"
     * +
     * "			IUF_R_DOCUMENTO_SPESA_INTERV B                        						\n"
     * + " 	WHERE 																				\n" +
     * "	 		B.ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA										\n" +
     * "	 		AND A.ID_DOCUMENTO_SPESA_INTERVEN = B.ID_DOCUMENTO_SPESA_INTERVEN				\n"
     */

    final String QUERY = "  "
        + " WITH PONUM AS (																			\n"
        + "  	SELECT DISTINCT PO.ID_PROCEDIMENTO_OGGETTO,											\n"
        + "			(	SELECT  																	\n"
        + "					COUNT(*) 																\n"
        + "				FROM 																		\n"
        + "					IUF_T_PROCEDIMENTO_OGGETTO PO1,											\n"
        + "					IUF_D_ESITO E															\n"
        + "				WHERE																		\n"
        + "					PO1.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO								\n"
        + "					AND PO1.ID_ESITO = E.ID_ESITO											\n"
        + "					AND E.CODICE = 'APP-N'													\n"
        + "					AND PO1.ID_PROCEDIMENTO_OGGETTO<>PO.ID_PROCEDIMENTO_OGGETTO				\n"
        + "					AND PO1.CODICE_RAGGRUPPAMENTO = PO.CODICE_RAGGRUPPAMENTO				\n"
        + "				) NUMERO_IST_NEG															\n"
        + "		FROM																				\n"
        + "			IUF_R_DOC_SPESA_INT_PROC_OGG A,													\n"
        + "			IUF_R_DOCUMENTO_SPESA_INTERV B, 												\n"
        + "			IUF_T_PROCEDIMENTO_OGGETTO PO													\n"
        + "		WHERE																				\n"
        + "			B.ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA										\n"
        + "			AND PO.ID_PROCEDIMENTO_OGGETTO = A.ID_PROCEDIMENTO_OGGETTO						\n"
        + "			AND A.ID_DOCUMENTO_SPESA_INTERVEN = B.ID_DOCUMENTO_SPESA_INTERVEN)				\n"
        + "SELECT                                         											\n"
        + "         SUM(IMPORTO_RENDICONTATO) AS IMPORTO                    						\n"
        + "     FROM                                                     							\n"
        + "         IUF_R_DOC_SPESA_INT_PROC_OGG A,													\n"
        + "			IUF_R_DOCUMENTO_SPESA_INTERV B,   											\n"
        + "			IUF_T_PROCEDIMENTO_OGGETTO PO,   												\n"
        + "			PONUM PON                        												\n"
        + " 	WHERE 																				\n"
        + "	 		B.ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA										\n"
        + "	 		AND A.ID_DOCUMENTO_SPESA_INTERVEN = B.ID_DOCUMENTO_SPESA_INTERVEN				\n"
        + "			AND PO.ID_PROCEDIMENTO_OGGETTO = A.ID_PROCEDIMENTO_OGGETTO						\n"
        + "			AND PON.ID_PROCEDIMENTO_OGGETTO=PO.ID_PROCEDIMENTO_OGGETTO						\n"
        + "	        AND PON.NUMERO_IST_NEG<2														\n"

    ;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
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

  public boolean ricEsisteInDocSpesIntRicPag(long idRicevutaPagamento)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::ricEsisteInDocSpesIntRicPag]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                          					\n"
        + "   COUNT(*)                                     					\n"
        + " FROM                                           					\n"
        + "   IUF_R_DOC_SPESA_INT_RICEV_PA 								\n"
        + " WHERE                                          					\n"
        + "   ID_RICEVUTA_PAGAMENTO = :ID_RICEVUTA_PAGAMENTO 				\n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource) != 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public BigDecimal getImportoPagamentoLordo(long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoPagamentoLordo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                         		\n"
        + "        SUM(DRP.IMPORTO_PAGAMENTO) AS IMPORTO                     	\n"
        + "      FROM                                                     		\n"
        + "         IUF_T_DETT_RICEVUTA_PAGAMENT DRP,							\n"
        + "			IUF_T_RICEVUTA_PAGAMENTO RP		                        	\n"
        + " 	WHERE 															\n"
        + "		 	RP.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA					\n"
        + "		 	AND RP.ID_RICEVUTA_PAGAMENTO = DRP.ID_RICEVUTA_PAGAMENTO	\n"
        + "			AND DRP.DATA_FINE IS NULL									\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa),
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

  public boolean canDeleteFile(long idDocumentoSpesaFile)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::canDeleteFile]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT           																		\n"
        + "   COUNT(*)                      													\n"
        + " FROM                            													\n"
        + "   IUF_R_DOC_SPESA_PROC_OGG	    													\n"
        + " WHERE                           													\n"
        + "   ID_DOCUMENTO_SPESA_FILE = :ID_DOCUMENTO_SPESA_FILE								\n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA_FILE",
          idDocumentoSpesaFile, Types.NUMERIC);
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

  public boolean docHasInterventi(long idDocSpesa)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::docHasInterventi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                          					\n"
        + "   COUNT(*)                                     					\n"
        + " FROM                                           					\n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV I								\n"
        + " WHERE                                          					\n"
        + "    I.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA					\n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocSpesa,
          Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource) != 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public boolean ricEsisteInDocSpesRicPag(long idRicevutaPagamento)
  {
    String THIS_METHOD = "[" + THIS_CLASS + ":ricEsisteInDocSpesRicPag]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                          					\n"
        + "   COUNT(*)                                     					\n"
        + " FROM                                           					\n"
        + "   IUF_R_DOC_SPESA_PROC_OGG 										\n"
        + " WHERE                                          					\n"
        + "   ID_RICEVUTA_PAGAMENTO = :ID_RICEVUTA_PAGAMENTO 				\n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource) != 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<DocumentoSpesaVO> getElencoAllegatiIdIntervento(
      long idDocumentoSpesa, long idIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoAllegatiIdIntervento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "   SELECT DISTINCT																							\n"
        + "		 FIL.ID_DOCUMENTO_SPESA_FILE,																					\n"
        + "		 PO.ID_PROCEDIMENTO_OGGETTO,																					\n"

        + "		 (SELECT CODICE FROM IUF_D_ESITO WHERE CODICE = 'APP-N' 														\n"
        + "		  AND ID_ESITO IN (																								\n"
        + "	      SELECT ID_ESITO FROM IUF_T_PROCEDIMENTO_OGGETTO PO1, IUF_R_DOC_SPESA_INT_ACC_SPES ACC1, IUF_R_DOCUMENTO_SPESA_INTERV DSI1				\n"
        + "	      WHERE PO1.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO																\n"
        + "	      AND PO1.ID_PROCEDIMENTO_OGGETTO<>PO.ID_PROCEDIMENTO_OGGETTO													\n"
        + "	  	  AND ACC1.ID_PROCEDIMENTO_OGGETTO = PO1.ID_PROCEDIMENTO_OGGETTO												\n"
        + "	  	  AND DSI1.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA																\n"
        + "	  	  AND DSI1.ID_DOCUMENTO_SPESA_INTERVEN = ACC1.ID_DOCUMENTO_SPESA_INTERVEN										\n"
        + "	     )) CODICE_ESITO,																								\n"

        + " decode(DSIPO_INTE.ID_PROCEDIMENTO_OGGETTO,null,'',NVL(SUBSTR(PO.IDENTIFICATIVO, 13), '')) as PREFIX,				\n"
        + "		 FIL.NOME_FILE_LOGICO_DOCUMENTO_SPE AS SUFFIX,             														\n"
        + "      FIL.NOME_FILE_FISICO_DOCUMENTO_SPE                     														\n"
        + "    FROM                                                     														\n"
        + "      IUF_T_DOCUMENTO_SPESA_FILE FIL,																				\n"
        + "		 IUF_R_DOC_SPESA_PROC_OGG DSPO,																					\n"
        + "	(SELECT DSIPO.ID_PROCEDIMENTO_OGGETTO, INTE.ID_INTERVENTO															\n"
        + "	         FROM IUF_R_DOC_SPESA_INT_PROC_OGG   DSIPO,																	\n"
        + "	              IUF_R_DOCUMENTO_SPESA_INTERV INTE																	\n"
        + "	        WHERE INTE.ID_DOCUMENTO_SPESA_INTERVEN =																	\n"
        + "	               DSIPO.ID_DOCUMENTO_SPESA_INTERVEN																	\n"
        + "	           AND INTE.ID_INTERVENTO = :ID_INTERVENTO) DSIPO_INTE,														\n"
        + "		 IUF_R_LEGAME_GRUPPO_OGGETTO LGO,																				\n"
        + "		 IUF_D_OGGETTO O,																								\n"
        + "		 IUF_T_PROCEDIMENTO_OGGETTO PO                       															\n"
        + "    WHERE                                                    														\n"
        + "      FIL.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA      																\n"
        + "		 AND DSPO.ID_DOCUMENTO_SPESA_FILE (+)= FIL.ID_DOCUMENTO_SPESA_FILE  											\n"
        + "		 AND PO.ID_PROCEDIMENTO_OGGETTO (+)= DSPO.ID_PROCEDIMENTO_OGGETTO   											\n"
        + "		 AND DSIPO_INTE.ID_PROCEDIMENTO_OGGETTO (+) = DSPO.ID_PROCEDIMENTO_OGGETTO  									\n"
        + "		 ORDER BY PO.ID_PROCEDIMENTO_OGGETTO ASC, FIL.ID_DOCUMENTO_SPESA_FILE DESC										\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DocumentoSpesaVO>>()
          {
            @Override
            public List<DocumentoSpesaVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DocumentoSpesaVO> list = new ArrayList<>();
              while (rs.next())
              {
                DocumentoSpesaVO file = new DocumentoSpesaVO();
                String prefix = rs.getString("PREFIX");
                String suffix = rs.getString("SUFFIX");
                String nomeFileLogico = "";
                if (prefix != null && prefix != "")
                {
                  nomeFileLogico += prefix + " - ";
                  file.setPrefix(prefix);
                }
                nomeFileLogico += suffix;
                file.setIdDocumentoSpesaFile(
                    rs.getLong("ID_DOCUMENTO_SPESA_FILE"));
                file.setNomeFileFisicoDocumentoSpe(
                    rs.getString("NOME_FILE_FISICO_DOCUMENTO_SPE"));
                file.setNomeFileLogicoDocumentoSpe(nomeFileLogico);
                String codEsito = rs.getString("CODICE_ESITO");
                if (codEsito != null && "APP-N".equals(codEsito))
                  file.setPoApprovatoNegativo(true);
                list.add(file);
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

              new LogParameter("idDocumentoSpesa", idDocumentoSpesa)
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

  public void deleteRDocSpesaInt(Long idIntervento, Long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRDocSpesaInt]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = "DELETE FROM IUF_R_DOCUMENTO_SPESA_INTERV    	\n"
        + " WHERE 						 									\n"
        + "	ID_INTERVENTO = :ID_INTERVENTO 									\n"
        + "	AND ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA						\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INTERVENTO", idIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idIntervento", idIntervento),
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa)
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

  public Long getIdDocumentoSpesaByIdRicevutaPagamento(Long idRicevutaPagamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getIdDocumentoSpesaByIdRicevutaPagamento]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                                                 	  	\n"
        + "  ID_DOCUMENTO_SPESA                                                                                                 	\n"
        + " FROM                                                                                                                  	\n"
        + "   IUF_T_RICEVUTA_PAGAMENTO                                                                                        		\n"
        + " WHERE                                                                                                                  	\n"
        + "   ID_RICEVUTA_PAGAMENTO = :ID_RICEVUTA_PAGAMENTO                                                             			\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);

      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idRicevutaPagamento", idRicevutaPagamento)
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

  public List<Long> getListInterventiByIdRicevutaPagamento(
      Long idRicevutaPagamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::searchDocumentoSpesaByKey]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuilder QUERY = new StringBuilder();
    QUERY.append(
        " SELECT ID_INTERVENTO	 							\n"
            + " FROM 												\n"
            + "    IUF_R_DOC_SPESA_INT_RICEV_PA 					\n"
            + " WHERE 											\n"
            + "   ID_RICEVUTA_PAGAMENTO = :ID_RICEVUTA_PAGAMENTO 	\n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_RICEVUTA_PAGAMENTO", idRicevutaPagamento,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY.toString(),
          mapParameterSource, new ResultSetExtractor<List<Long>>()
          {

            @Override
            public List<Long> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<Long> list = new ArrayList<Long>();

              while (rs.next())
              {
                list.add(rs.getLong("ID_INTERVENTO"));
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
              new LogParameter("idRicevutaPagamento", idRicevutaPagamento)
          },
          new LogVariable[]
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

  public boolean nonEsistonoRicNotInRDocSpesaPO(long idDocumentoSpesaFile)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::nonEsistonoRicNotInRDocSpesaPO]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT           																		\n"
        + "   COUNT(ID_RICEVUTA_PAGAMENTO)                      								\n"
        + " FROM                            													\n"
        + "   IUF_T_RICEVUTA_PAGAMENTO															\n"
        + " WHERE                           													\n"
        + "	   ID_DOCUMENTO_SPESA IN															\n"
        + "		 (	SELECT ID_DOCUMENTO_SPESA 													\n"
        + "			FROM IUF_T_DOCUMENTO_SPESA_FILE 											\n"
        + "			WHERE ID_DOCUMENTO_SPESA_FILE = :ID_DOCUMENTO_SPESA_FILE)					\n"
        + "	   AND ID_RICEVUTA_PAGAMENTO NOT IN 											    \n"
        + "	(SELECT  ID_RICEVUTA_PAGAMENTO 														\n"
        + "				FROM IUF_R_DOC_SPESA_PROC_OGG											\n"
        + "	 WHERE 																			    \n"
        + "		ID_DOCUMENTO_SPESA IN 															\n"
        + "		 (	SELECT ID_DOCUMENTO_SPESA 													\n"
        + "			FROM IUF_T_DOCUMENTO_SPESA_FILE 											\n"
        + "			WHERE ID_DOCUMENTO_SPESA_FILE = :ID_DOCUMENTO_SPESA_FILE)					\n"
        + "	)																					\n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA_FILE",
          idDocumentoSpesaFile, Types.NUMERIC);
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

  public List<RicevutaPagamentoVO> getElencoRicevutePagamentoDomande(
      long idDocumentoSpesa,
      ArrayList<Long> idsDomande) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getElencoRicevutePagamentoDomande";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");

    }

    String QUERY = " SELECT                                                  			\n"
        + "   B.ID_DETT_RICEVUTA_PAGAMENTO,                         			\n"
        + "   B.ID_RICEVUTA_PAGAMENTO,                              			\n"
        + "   B.NUMERO_RICEVUTA_PAGAMENTO AS NUMERO,                			\n"
        + "   B.DATA_PAGAMENTO,                                     			\n"
        + "   B.ID_MODALITA_PAGAMENTO,                              			\n"
        + "   B.IMPORTO_PAGAMENTO,                                  			\n"
        + "   B.NOTE,                                               			\n"
        + "	D.ID_PROCEDIMENTO_OGGETTO,										\n"
        + "   C.DESCRIZIONE AS DESCR_MODALITA_PAGAMENTO,                      \n"
        + "      DECODE((SELECT DISTINCT B1.ID_RICEVUTA_PAGAMENTO                                            			\n"
        + "         FROM IUF_R_DOC_SPESA_PROC_OGG B1                                                         			\n"
        + "         WHERE B1.ID_RICEVUTA_PAGAMENTO = A.ID_RICEVUTA_PAGAMENTO), NULL, 'S', 'N') AS VISIBLE_ICONS		\n"

        + " FROM                                                    			\n"
        + "   IUF_T_RICEVUTA_PAGAMENTO A,                           			\n"
        + "   IUF_T_DETT_RICEVUTA_PAGAMENT B,                       			\n"
        + "   IUF_D_MODALITA_PAGAMENTO C, 									\n"
        + "	IUF_R_DOC_SPESA_PROC_OGG D										\n"
        + " WHERE                                                   			\n"
        + "   A.ID_RICEVUTA_PAGAMENTO = B.ID_RICEVUTA_PAGAMENTO 				\n"
        + "   AND B.ID_MODALITA_PAGAMENTO = C.ID_MODALITA_PAGAMENTO(+) 		\n"
        + "   AND B.DATA_FINE IS NULL									 		\n"
        + "	AND A.ID_DOCUMENTO_SPESA = :ID_DOCUMENTO_SPESA					\n";
    if (idsDomande != null && !idsDomande.isEmpty())
      QUERY += "   " + getInCondition("D.ID_PROCEDIMENTO_OGGETTO", idsDomande)
          + "   	\n";
    QUERY += "	AND D.ID_RICEVUTA_PAGAMENTO = A.ID_RICEVUTA_PAGAMENTO			\n"
        + " ORDER BY B.DATA_PAGAMENTO , B.NUMERO_RICEVUTA_PAGAMENTO \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, RicevutaPagamentoVO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idDocumentoSpesa", idDocumentoSpesa) },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public List<ExcelRicevutePagInterventoDTO> getElencoExcelRicevutePagamentoInterventoPerDomanda(
      long idProcedimento, long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoExcelRicevutePagamentoInterventoPerDomanda]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT                                                                                            \n"
        + "   DSI.ID_INTERVENTO,                                                                                  \n"
        + "   DI.PROGRESSIVO,        	       	                                                                   \n"
        + "   DDI.DESCRIZIONE AS DESCR_INTERVENTO,                                                                \n"
        + "   DSI.IMPORTO AS IMPORTO_INTERVENTO,                                                                  \n"
        + "   DI.ULTERIORI_INFORMAZIONI,                        	         		                                \n"
        + "   TF.ID_FORNITORE,                                                                                    \n"
        + "   (TF.CODICE_FORNITORE || ' - ' || TF.RAGIONE_SOCIALE || ' - ' || to_char(DDS.DATA_DOCUMENTO_SPESA, 'dd/MM/yyyy') || ' - ' ||  DDS.NUMERO_DOCUMENTO_SPESA || ' - ' || DTDS.DESCRIZIONE) AS DESCR_FORNITORE,                              \n"
        + "   DS.ID_DOCUMENTO_SPESA,                                                                              \n"
        + "   DDS.ID_DETT_DOCUMENTO_SPESA,                                                                           \n"
        + "   DDS.DATA_DOCUMENTO_SPESA,                                                                           \n"
        + "   DDS.NUMERO_DOCUMENTO_SPESA,                                                                         \n"
        + "   DDS.ID_TIPO_DOCUMENTO_SPESA,                                                                        \n"
        + "   DTDS.DESCRIZIONE AS TIPO_DOCUMENTO,                                                                 \n"
        + "   DDS.DATA_PAGAMENTO,                                                                                 \n"
        + "   DDS.ID_DETT_DOCUMENTO_SPESA,                                                                        \n"
        + "   DDS.ID_MODALITA_PAGAMENTO,                                                                          \n"
        + "   DDS.IMPORTO_SPESA,                                                                                  \n"
        + "   DDS.IMPORTO_IVA_SPESA,                                                                              \n"
        + "   DDS.NOTE,                                                                                           \n"
        + "   DMP.DESCRIZIONE AS MOD_PAGAMENTO,                                                                   \n"
        + "   DRP.NUMERO_RICEVUTA_PAGAMENTO,                                                                      \n"
        + "   DRP.IMPORTO_PAGAMENTO,                                                                              \n"
        + "   DRP.DATA_PAGAMENTO AS DATA_PAG_RIC,                                                                 \n"
        + "   DRP.NOTE AS NOTE_RIC,                                                                               \n"
        /*
         * +
         * "    (SELECT SUM(PO1.IMPORTO_RENDICONTATO) FROM IUF_R_DOC_SPESA_INT_PROC_OGG PO1                      \n"
         * +
         * "  WHERE PO1.ID_DOCUMENTO_SPESA_INTERVEN = DSI.ID_DOCUMENTO_SPESA_INTERVEN"
         * +
         * "	AND PO1.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO) AS IMPORTO_RENDICONTATO,                \n"
         */

        + "    DSI.IMPORTO AS  IMPORTO_ASSOCIATO,                                                             	\n"

        /*
         * +
         * "    ( SELECT SUM(S2.IMPORTO) FROM IUF_R_DOCUMENTO_SPESA_INTERV S2                    				\n"
         * +
         * "      WHERE S2.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                              				\n"
         * +
         * "    ) AS IMPORTO_ASSOCIATO ,                                                           				\n"
         */
        + "    ( SELECT SUM(S1.IMPORTO) FROM IUF_R_DOC_SPESA_INT_RICEV_PA S1                     				\n"
        + "      WHERE S1.ID_RICEVUTA_PAGAMENTO = RP.ID_RICEVUTA_PAGAMENTO                        				\n"
        + "      AND S1.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                				\n"
        + "      AND TI.ID_INTERVENTO = S1.ID_INTERVENTO				                            				\n"
        + "    ) AS PAGAMENTO_ASSOCIATO,                                                          				\n"
        + "																										\n"
        + "   DMPR.DESCRIZIONE AS DESCR_PAG_RICEVUTA,																\n"
        + " 	DRP.ID_DETT_RICEVUTA_PAGAMENTO                                                             			\n"
        + " FROM                                                                                                  \n"
        + "   IUF_T_DOCUMENTO_SPESA DS,                                                                           \n"
        + "   IUF_T_DETT_DOCUMENTO_SPESA DDS,                                                                     \n"
        + "   IUF_T_FORNITORE TF,                                                                                 \n"
        + "   IUF_D_TIPO_DOCUMENTO_SPESA DTDS,                                                                    \n"
        + "   IUF_D_MODALITA_PAGAMENTO DMP,                                                                       \n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV DSI,                                                                 \n"
        + "   IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,                                                                 \n"
        + "   IUF_R_DOC_SPESA_INT_RICEV_PA DSIRP,                                                                 \n"
        + "   IUF_T_INTERVENTO TI,                                                                                \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI,                                                                      \n"
        + "   IUF_D_DESCRIZIONE_INTERVENTO DDI,                                                                   \n"
        + "   IUF_T_RICEVUTA_PAGAMENTO RP,                                                                        \n"
        + "   IUF_T_DETT_RICEVUTA_PAGAMENT DRP,                                                                  \n"
        + "   IUF_D_MODALITA_PAGAMENTO DMPR                                                                       \n"
        + " WHERE                                                                                                 \n"
        + "   DS.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                                               \n"
        + "   AND DDS.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                  \n"
        + "   AND DDS.DATA_FINE IS NULL						                                                    \n"
        + "   AND DTDS.ID_TIPO_DOCUMENTO_SPESA(+) = DDS.ID_TIPO_DOCUMENTO_SPESA                                   \n"
        + "   AND DMP.ID_MODALITA_PAGAMENTO(+) = DDS.ID_MODALITA_PAGAMENTO                                        \n"
        + "   AND DSI.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                  \n"
        + "   AND DSIPO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                        \n"
        + "   AND DSIRP.ID_INTERVENTO = DSI.ID_INTERVENTO					                                        \n"
        + "   AND DSIRP.ID_RICEVUTA_PAGAMENTO = RP.ID_RICEVUTA_PAGAMENTO   		                                \n"
        + "   AND DSIRP.ID_DOCUMENTO_SPESA = DSI.ID_DOCUMENTO_SPESA    		                                    \n"
        + "   AND DSI.ID_DOCUMENTO_SPESA_INTERVEN = DSIPO.ID_DOCUMENTO_SPESA_INTERVEN                             \n"
        + "   AND TI.ID_INTERVENTO = DSI.ID_INTERVENTO                                                            \n"
        + "   AND TI.ID_INTERVENTO = DI.ID_INTERVENTO                                                             \n"
        + "   AND TI.ID_PROCEDIMENTO = DS.ID_PROCEDIMENTO                                                         \n"
        + "   AND TI.ID_DESCRIZIONE_INTERVENTO   = DDI.ID_DESCRIZIONE_INTERVENTO                                  \n"
        + "   AND DDS.DATA_FINE IS NULL                                                                           \n"
        + "   AND TF.ID_FORNITORE (+)= DDS.ID_FORNITORE                                                           \n"
        + "   AND RP.ID_RICEVUTA_PAGAMENTO = DRP.ID_RICEVUTA_PAGAMENTO                                            \n"
        + "   AND RP.ID_DOCUMENTO_SPESA = DS.ID_DOCUMENTO_SPESA                                                   \n"
        + "   AND DRP.DATA_FINE IS NULL						                                                    \n"
        + "   AND DMPR.ID_MODALITA_PAGAMENTO = DRP.ID_MODALITA_PAGAMENTO                                          \n"
        + " ORDER BY TI.ID_INTERVENTO, TF.ID_FORNITORE, DDS.NUMERO_DOCUMENTO_SPESA, DRP.NUMERO_RICEVUTA_PAGAMENTO \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);

    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<ExcelRicevutePagInterventoDTO>>()
          {

            @Override
            public List<ExcelRicevutePagInterventoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              String idIntervento;
              String idInterventoLast = "";
              String idDocumentoSpesa;
              String idDocumentoSpesaLast = "";
              long idDettRicLast = 0;
              long idDettRic;

              ExcelRicevutePagInterventoDTO riga = null;
              ExcelRigaDocumentoSpesaDTO doc = null;
              ExcelRigaRicevutaPagDTO ricevuta = null;
              ArrayList<ExcelRigaDocumentoSpesaDTO> documenti = null;
              ArrayList<ExcelRigaRicevutaPagDTO> ricevute = null;
              ArrayList<ExcelRicevutePagInterventoDTO> elenco = new ArrayList<ExcelRicevutePagInterventoDTO>();

              while (rs.next())
              {
                idIntervento = rs.getLong("ID_INTERVENTO") + " "
                    + rs.getLong("ID_FORNITORE");
                if (!idIntervento.equals(idInterventoLast))
                {
                  idInterventoLast = idIntervento;
                  riga = new ExcelRicevutePagInterventoDTO();
                  riga.setDescrIntervento(rs.getString("DESCR_INTERVENTO"));
                  riga.setUlterioriInformazioni(
                      rs.getString("ULTERIORI_INFORMAZIONI"));
                  riga.setDescrFornitore(rs.getString("DESCR_FORNITORE"));
                  riga.setProgressivo(rs.getString("PROGRESSIVO"));
                  documenti = new ArrayList<>();
                  riga.setDettaglioDocumento(documenti);
                  elenco.add(riga);
                  idDettRicLast = -1;
                }

                idDocumentoSpesa = rs.getLong("ID_INTERVENTO") + " "
                    + rs.getLong("ID_FORNITORE") + " "
                    + rs.getLong("ID_DETT_DOCUMENTO_SPESA");
                if (!idDocumentoSpesa.equals(idDocumentoSpesaLast))
                {
                  idDocumentoSpesaLast = idDocumentoSpesa;
                  doc = new ExcelRigaDocumentoSpesaDTO();
                  doc.setFornitore(rs.getString("DESCR_FORNITORE"));
                  doc.setIdFornitore(rs.getLong("ID_FORNITORE"));
                  riga.setDescrFornitore(doc.getFornitore());
                  doc.setIdDocumento(rs.getLong("ID_DOCUMENTO_SPESA"));
                  doc.setDataDocumentoStr(IuffiUtils.DATE
                      .formatDate(rs.getDate("DATA_DOCUMENTO_SPESA")));
                  doc.setTipoDocumento(rs.getString("TIPO_DOCUMENTO"));
                  doc.setImportoNetto(rs.getBigDecimal("IMPORTO_SPESA"));
                  doc.setImportoIva(rs.getBigDecimal("IMPORTO_IVA_SPESA"));
                  BigDecimal impo = BigDecimal.ZERO;
                  if (doc.getImportoNetto() != null)
                    impo = impo.add(doc.getImportoNetto());
                  if (doc.getImportoIva() != null)
                    impo = impo.add(doc.getImportoIva());
                  doc.setImportoLordo(impo);
                  // doc.setImportoRendicontato(rs.getBigDecimal("IMPORTO_RENDICONTATO"));
                  try
                  {
                    doc.setImportoRendicontato(findImportoRendicontaTO(
                        rs.getLong("ID_DOCUMENTO_SPESA"),
                        rs.getLong("ID_INTERVENTO")));
                  }
                  catch (InternalUnexpectedException e)
                  {
                    e.printStackTrace();
                    return null;
                  }
                  // doc.setImportoDaRendicontare(rs.getBigDecimal("IMPORTO_INTERVENTO"));
                  doc.setImportoAssociato(
                      rs.getBigDecimal("IMPORTO_ASSOCIATO"));
                  doc.setNoteDocumentoSpesa(rs.getString("NOTE"));
                  doc.setNumeroDocumento(
                      rs.getString("NUMERO_DOCUMENTO_SPESA"));
                  ricevute = new ArrayList<>();
                  doc.setRicevute(ricevute);
                  riga.getDettaglioDocumento().add(doc);
                }

                idDettRic = rs.getLong("ID_DETT_RICEVUTA_PAGAMENTO");
                if (idDettRic != idDettRicLast)
                {
                  idDettRicLast = idDettRic;
                  ricevuta = new ExcelRigaRicevutaPagDTO();
                  ricevuta.setDataPagamentoStr(IuffiUtils.DATE
                      .formatDate(rs.getDate("DATA_PAG_RIC")));
                  ricevuta.setImportoPagamento(
                      rs.getBigDecimal("IMPORTO_PAGAMENTO"));
                  ricevuta
                      .setModalitaPagamento(rs.getString("DESCR_PAG_RICEVUTA"));
                  ricevuta.setNote(rs.getString("NOTE_RIC"));
                  ricevuta.setNumeroPagamento(
                      rs.getString("NUMERO_RICEVUTA_PAGAMENTO"));
                  ricevuta.setImportoPagamentoAssociato(
                      rs.getBigDecimal("PAGAMENTO_ASSOCIATO"));
                  doc.getRicevute().add(ricevuta);
                }
              }
              return elenco;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimento", idProcedimento) },
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

  public String callMainElaboraPartImpegno(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "callMainElaboraPartImpegno";
    StringBuffer CALL = new StringBuffer(
        "PCK_IUF_UTILITY_GRAFICO.MainElaboraPartImpegno");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("PIDPROCEDIMENTOOGGETTO", idProcedimentoOggetto,
          Types.NUMERIC);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_IUF_UTILITY_GRAFICO")
              .withProcedureName("MAINELABORAPARTIMPEGNO")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlParameter("PIDPROCEDIMENTOOGGETTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("PRISULTATO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("PMESSAGGIO", java.sql.Types.VARCHAR));
      Map<String, Object> results = call.execute(parameterSource);

      int result = ((Number) results.get("PRISULTATO")).intValue();
      if (result == 1)
      {
        return safeMessaggioPLSQL((String) results.get("PMESSAGGIO"));
      }
      else
      {
        return null;
      }
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

  public List<ParticellaVO> getConduzioneUtilizzo(long extIdParticella,
      Long idDichConsistenza) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getConduzioneUtilizzo";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                                      				\n"
        + "	   U.SUPERFICIE_UTILIZZATA,								  				\n"
        + "    U.COD_TIPO_UTILIZZO,                                 				\n"
        + "    U.DESC_TIPO_UTILIZZO,                            					\n"
        + "    U.CODICE_DESTINAZIONE COD_DESTINAZIONE,                              \n"
        + "    U.DESCRIZIONE_DESTINAZIONE DESCR_DESTINAZIONE,                       \n"
        + "    U.COD_DETTAGLIO_USO,                            						\n"
        + "    U.DESC_TIPO_DETTAGLIO_USO  DESC_DETTAGLIO_USO,	   					\n"
        + "    U.CODICE_QUALITA_USO COD_QUALITA_USO,                   				\n"
        + "    U.DESCRIZIONE_QUALITA_USO DESCR_QUALITA_USO,            				\n"
        + "    U.DESCRIZIONE_PRATICA_MANTENIMEN,		            				\n"
        + "    U.COD_TIPO_VARIETA,                                    				\n"
        + "    U.DESC_TIPO_VARIETA                                   				\n"
        + " FROM 																	\n"
        + "		SMRGAA_V_CONDUZIONE_UTILIZZO U	                       				\n"
        + " WHERE                                                   				\n"
        + "   	U.ID_DICHIARAZIONE_CONSISTENZA = :EXT_ID_DICHIARAZIONE_CONSISTEN 	\n"
        + "   	AND U.ID_PARTICELLA = :EXT_ID_PARTICELLA							\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("EXT_ID_DICHIARAZIONE_CONSISTEN",
          idDichConsistenza, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_PARTICELLA", extIdParticella,
          Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, ParticellaVO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("EXT_ID_DICHIARAZIONE_CONSISTEN",
              idDichConsistenza),
              new LogParameter("EXT_ID_PARTICELLA", extIdParticella) },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public BigDecimal getImportoAssociatoRic(long idDocumentoSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoAssociatoRic]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT                                         	\n"
        + "         SUM(IMPORTO) IMPORTO                                   	\n"
        + "       FROM                                                     	\n"
        + "         IUF_R_DOC_SPESA_INT_RICEV_PA                          	\n"
        + " WHERE															\n"
        + "			 ID_DOCUMENTO_SPESA =:ID_DOCUMENTO_SPESA				\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DOCUMENTO_SPESA", idDocumentoSpesa,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<BigDecimal>()
          {

            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BigDecimal ret = null;
              if (rs.next())
              {
                ret = rs.getBigDecimal("IMPORTO");
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
              new LogParameter("idDocumentoSpesa", idDocumentoSpesa) },
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

  public Long contaRecordRendicontazione(long idProcedimento, long idBando)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::contaRecordRendicontazione]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                          								\n"
        + "   COUNT(*)                                     								\n"
        + " FROM                                           								\n"
        + "   IUF_T_RENDICONTAZIONE_SPESE R, 											\n"
        + "   IUF_T_INTERVENTO I, 														\n"
        + "   IUF_R_LIVELLO_INTERVENTO LI,	 											\n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI 											\n"
        + " WHERE                                          								\n"
        + "	  R.ID_INTERVENTO = I.ID_INTERVENTO 										\n"
        + "	  AND LBI.ID_DESCRIZIONE_INTERVENTO = LI.ID_DESCRIZIONE_INTERVENTO			\n"
        + "	  AND LI.FLAG_DOCUMENTO_SPESA = 'S'											\n"
        + "	  AND LI.ID_DESCRIZIONE_INTERVENTO = I.ID_DESCRIZIONE_INTERVENTO			\n"
        + "	  AND LBI.ID_BANDO = :ID_BANDO												\n"
        + "	  AND LI.ID_LIVELLO = LBI.ID_LIVELLO										\n"
        + "	  AND R.IMPORTO_SPESA > 0													\n"
        + "   AND R.ID_PROCEDIMENTO_OGGETTO IN 											\n"
        + "	   (SELECT 																	\n"
        + "			PO.ID_PROCEDIMENTO_OGGETTO 											\n"
        + "		FROM 																	\n"
        + "			IUF_T_PROCEDIMENTO_OGGETTO PO 										\n"
        + "		WHERE 																	\n"
        + "			ID_PROCEDIMENTO = :ID_PROCEDIMENTO	)								\n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public Long contaInterventiRendicontazConFlagS(long idProcedimento,
      long idBando)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::contaInterventiRendicontazConFlagS]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                          								\n"
        + "   COUNT(*)                                     								\n"
        + " FROM                                           								\n"
        + "   IUF_R_PROCEDIMENTO_LIVELLO PL, 											\n"
        + "   IUF_T_INTERVENTO I, 														\n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DI, 											\n"
        + "   IUF_R_LIVELLO_INTERVENTO LI,	 											\n"
        + "   IUF_R_LIV_BANDO_INTERVENTO LBI 											\n"
        + " WHERE                                          								\n"
        + "	  PL.ID_LIVELLO = LBI.ID_LIVELLO 											\n"
        + "   AND I.ID_PROCEDIMENTO = :ID_PROCEDIMENTO									\n"
        + "	  AND I.ID_INTERVENTO = DI.ID_INTERVENTO									\n"
        + "	  AND DI.DATA_FINE IS NULL													\n"
        + "	  AND LI.ID_DESCRIZIONE_INTERVENTO = I.ID_DESCRIZIONE_INTERVENTO			\n"
        + "	  AND LI.FLAG_DOCUMENTO_SPESA = 'S'											\n"
        + "	  AND LBI.ID_DESCRIZIONE_INTERVENTO = LI.ID_DESCRIZIONE_INTERVENTO			\n"
        + "	  AND LBI.ID_BANDO = :ID_BANDO												\n"
        + "	  AND LI.ID_LIVELLO = LBI.ID_LIVELLO										\n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public Long contaRecordDocSpesa(long idProcedimento)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::contaRecordDocSpesa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                          								\n"
        + "   COUNT(*)                                     								\n"
        + " FROM                                           								\n"
        + "   IUF_T_DOCUMENTO_SPESA			 											\n"
        + " WHERE                                          								\n"
        + "	  ID_PROCEDIMENTO = :ID_PROCEDIMENTO										\n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public DecodificaDTO<Long> getInfoAziendaSubentranteVoltura(
      long idProcedimentoOggetto, long codiceRaggruppamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getInfoAziendaSubentranteVoltura]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "SELECT 														\n"
        + "	W.EXT_ID_AZIENDA AS ID, 													\n"
        + "	W.EXT_ID_AZIENDA AS CODICE, 												\n"
        + "	NVL(DA.MAIL,DA.PEC) AS DESCRIZIONE 											\n"
        + "FROM 																		\n"
        + "	IUF_W_PROCEDIMENTO_OGG_AZIEN W, 											\n"
        + "	IUF_T_PROCEDIMENTO_OGGETTO PO, 												\n"
        + "	SMRGAA.SMRGAA_V_DATI_ANAGRAFICI DA											\n"
        + "WHERE																		\n"
        + "PO.ID_PROCEDIMENTO_OGGETTO =:ID_PROCEDIMENTO_OGGETTO							\n"
        /*
         * + "(																			\n" +
         * "  SELECT DISTINCT ID_PROCEDIMENTO_OGGETTO FROM 								\n" +
         * " IUF_T_PROCEDIMENTO_OGGETTO PO1, 											\n" +
         * " IUF_R_LEGAME_GRUPPO_OGGETTO LGO, 											\n" +
         * " IUF_D_OGGETTO O, 															\n" +
         * " IUF_T_PROCEDIMENTO P														\n" +
         * " WHERE 																		\n" +
         * " P.ID_PROCEDIMENTO = (SELECT ID_PROCEDIMENTO FROM IUF_T_PROCEDIMENTO_OGGETTO PO2 WHERE ID_PROCEDIMENTO_OGGETTO=:ID_PROCEDIMENTO_OGGETTO)\n"
         * +
         * " AND PO1.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO 			\n"
         * +
         * " AND PO.CODICE_RAGGRUPPAMENTO=:CODICE_RAGGRUPPAMENTO 						\n"
         * + " AND LGO.ID_OGGETTO = O.ID_OGGETTO 											\n" +
         * " AND PO1.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO 								\n" +
         * " AND O.CODICE='DVOL'														\n" +
         * ")																			\n"
         */
        + "	AND PO.ID_PROCEDIMENTO_OGGETTO = W.ID_PROCEDIMENTO_OGGETTO 					\n"
        + "	AND DA.ID_AZIENDA = W.EXT_ID_AZIENDA										\n"
        + "	AND DA.DATA_FINE_VALIDITA IS NULL											\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    mapParameterSource.addValue("CODICE_RAGGRUPPAMENTO", codiceRaggruppamento,
        Types.NUMERIC);

    try
    {
      final List<DecodificaDTO<Long>> list = queryForDecodificaLong(QUERY,
          mapParameterSource);
      return list.isEmpty() ? null : list.get(0);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("codiceRaggruppamento", codiceRaggruppamento)

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

  public DatiSpecificiDTO getDatiSpecificiExPost(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDatiSpecificiExPost]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    final String QUERY = " WITH                                                                                                                    \n"
        + "   ESTRAZIONE AS                                                                                                         \n"
        + "   (                                                                                                                     \n"
        + "     SELECT                                                                                                              \n"
        + "       PO2.ID_PROCEDIMENTO,                                                                                              \n"
        + "       PE.ID_PROCEDIMENTO_OGGETTO,                                                                                       \n"
        + "       EC.DATA_ESTRAZIONE,                                                                                               \n"
        + "       L.NUMERO_ESTRAZIONE,                                                                                               \n"
        + "       TE.DESCRIZIONE AS DESC_TIPO_ESTRAZIONE,                                                                           \n"
        + "       PE.FLAG_ESTRATTA                                                                                                  \n"
        + "     FROM                                                                                                                \n"
        + "       IUF_T_PROCEDIMENTO_ESTR_EXPO PE,                                                                                   \n"
        + "       IUF_T_ESTRAZIONE_CAMPIONE EC,                                                                                     \n"
        + "       IUF_D_TIPO_ESTRAZIONE TE,                                                                                         \n"
        + "       IUF_D_NUMERO_LOTTO L, 		                                                                                     \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                                    \n"
        + "       IUF_T_PROCEDIMENTO_OGGETTO PO2                                                                                    \n"
        + "     WHERE                                                                                                               \n"
        + "       PO.ID_PROCEDIMENTO_OGGETTO       =:ID_PROCEDIMENTO_OGGETTO                                                        \n"
        + "       AND PO.ID_PROCEDIMENTO           = PO2.ID_PROCEDIMENTO                                                            \n"
        + "       AND PO2.ID_PROCEDIMENTO_OGGETTO <> PO.ID_PROCEDIMENTO_OGGETTO                                                     \n"
        + "       AND PO.CODICE_RAGGRUPPAMENTO     = PO2.CODICE_RAGGRUPPAMENTO                                                      \n"
        + "       AND PE.ID_PROCEDIMENTO   		   = PO2.ID_PROCEDIMENTO                                                    \n"
        + "       AND EC.ID_ESTRAZIONE_CAMPIONE    = PE.ID_ESTRAZIONE_CAMPIONE                                                      \n"
        + "       AND EC.ID_TIPO_ESTRAZIONE        = TE.ID_TIPO_ESTRAZIONE                                                          \n"
        + "       AND EC.ID_NUMERO_LOTTO           = L.ID_NUMERO_LOTTO (+)                                                          \n"
        + "       AND EC.FLAG_APPROVATA            = 'S'                                                                            \n"
        + "       AND EC.DATA_ANNULLAMENTO IS NULL                                                                                  \n"
        + "   )                                                                                                                     \n"
        + " SELECT                                                                                                                  \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO,                                                                                           \n"
        + "   CILI.FLAG_PREAVVISO,                                                                                                  \n"
        + "   CILI.DATA_PREAVVISO,                                                                                                  \n"
        + "   CILI.ID_TIPOLOGIA_PREAVVISO,                                                                                          \n"
        + "   DECODE(CILI.ID_TIPOLOGIA_PREAVVISO, 1, CILI.DESCRIZIONE_ALTRO_PREAVVISO, TP.DESCRIZIONE) AS DESC_TIPOLOGIA_PREAVVISO, \n"
        + "   CILI.FLAG_CONTROLLO,                                                                                                  \n"
        + "   DECODE(E.ID_PROCEDIMENTO_OGGETTO,NULL,'N','S') AS FLAG_SOTTOPOSTA_ESTRAZIONE,                                         \n"
        + "   NVL(E.FLAG_ESTRATTA,'N')                       AS FLAG_ESTRATTA,                                                      \n"
        + "   E.DATA_ESTRAZIONE,                                                                                                    \n"
        + "   E.NUMERO_ESTRAZIONE,                                                                                                    \n"
        + "   E.DESC_TIPO_ESTRAZIONE                                                                                                \n"
        + " FROM                                                                                                                    \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO                                                                                         \n"
        + " LEFT OUTER JOIN IUF_T_CONTROLLO_IN_LOCO_EXPO CILI                                                                     \n"
        + " ON                                                                                                                      \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = CILI.ID_PROCEDIMENTO_OGGETTO                                                             \n"
        + " LEFT OUTER JOIN IUF_D_TIPOLOGIA_PREAVVISO TP                                                                            \n"
        + " ON                                                                                                                      \n"
        + "   CILI.ID_TIPOLOGIA_PREAVVISO = TP.ID_TIPOLOGIA_PREAVVISO                                                               \n"
        + " LEFT OUTER JOIN ESTRAZIONE E                                                                                            \n"
        + " ON                                                                                                                      \n"
        + "   PO.ID_PROCEDIMENTO = E.ID_PROCEDIMENTO                                                                                \n"
        + " WHERE                                                                                                                   \n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                                 \n";
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForObject(QUERY, mapParameterSource, DatiSpecificiDTO.class);
    }
    catch (Exception t)
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

  public List<ControlloAmministrativoDTO> getControlliAmministrativiExPost(
      long idProcedimentoOggetto, String codiceQuadro, List<Long> ids)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getControlliAmministrativiExPost]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    StringBuilder queryBuilder = new StringBuilder(
        "  WITH                                                                                                                      \n"
            + "    ESITI AS                                                                                                                \n"
            + "    (                                                                                                                       \n"
            + "      SELECT                                                                                                                \n"
            + "        ECA.ID_QUADRO_OGG_CONTROLLO_AMM,                                                                                    \n"
            + "        ECA.ID_ESITO,                                                                                                       \n"
            + "        ECA.ID_ESITO_CONTROLLI_AMM,                                                                                         \n"
            + "        ECA.NOTE,                                                                                                           \n"
            + "        E.CODICE      AS CODICE_ESITO,                                                                                      \n"
            + "        E.DESCRIZIONE AS DESC_ESITO                                                                                         \n"
            + "      FROM                                                                                                                  \n"
            + "        IUF_T_ESITO_CONTROLLI_AMM ECA,                                                                                      \n"
            + "        IUF_D_ESITO E                                                                                                       \n"
            + "      WHERE                                                                                                                 \n"
            + "        ECA.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                                                              \n"
            + "        AND ECA.ID_ESITO            = E.ID_ESITO                                                                            \n"
            + "    )                                                                                                                       \n"
            + "  SELECT                                                                                                                    \n"
            + "    DECODE(CA.ID_CONTROLLO_AMMINISTRAT_PADRE,NULL, CA.ORDINAMENTO*1000,CA_PADRE.ORDINAMENTO*1000+CA.ORDINAMENTO) AS ORDINE, \n"
            + "    CA.ID_CONTROLLO_AMMINISTRAT_PADRE,                                                                                      \n"
            + "    CA.CODICE,                                                                                                              \n"
            + "    CA.ID_CONTROLLO_AMMINISTRATIVO,                                                                                         \n"
            + "    CA.ID_CONTROLLO_AMMINISTRAT_PADRE,                                                                                      \n"
            + "    BOQCA.ID_QUADRO_OGG_CONTROLLO_AMM,                                                                                      \n"
            + "    CA.DESCRIZIONE,                                                                                                         \n"
            + "    E.ID_ESITO_CONTROLLI_AMM,                                                                                               \n"
            + "    E.ID_ESITO,                                                                                                             \n"
            + "    E.CODICE_ESITO,                                                                                                         \n"
            + "    E.DESC_ESITO,                                                                                                           \n"
            + "    E.NOTE                                                                                                                  \n"
            + "  FROM                                                                                                                      \n"
            + "    IUF_D_CONTROLLO_AMMINISTRATI CA                                                                                       \n"
            + "    LEFT JOIN                                                                                                               \n"
            + "    IUF_D_CONTROLLO_AMMINISTRATI CA_PADRE                                                                                 \n"
            + "    ON                                                                                                                      \n"
            + "    CA.ID_CONTROLLO_AMMINISTRAT_PADRE = CA_PADRE.ID_CONTROLLO_AMMINISTRATIVO,                                               \n"
            + "    IUF_R_BAND_OG_QUAD_CONTR_AMM BOQCA,                                                                                   \n"
            + "    IUF_R_QUADRO_OGG_CONTROL_AMM QOCA                                                                                     \n"
            + "  LEFT JOIN ESITI E                                                                                                         \n"
            + "  ON                                                                                                                        \n"
            + "    QOCA.ID_QUADRO_OGG_CONTROLLO_AMM = E.ID_QUADRO_OGG_CONTROLLO_AMM,                                                       \n"
            + "    IUF_T_PROCEDIMENTO_OGGETTO PO,                                                                                          \n"
            + "    IUF_T_PROCEDIMENTO P,                                                                                                   \n"
            + "    IUF_R_BANDO_OGGETTO BO,                                                                                                 \n"
            + "    IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                                                                        \n"
            + "    IUF_D_QUADRO Q,                                                                                                         \n"
            + "    IUF_R_QUADRO_OGGETTO QO                                                                                                 \n"
            + "  WHERE                                                                                                                     \n"
            + "    PO.ID_PROCEDIMENTO_OGGETTO           = :ID_PROCEDIMENTO_OGGETTO                                                         \n"
            + "    AND PO.ID_PROCEDIMENTO               = P.ID_PROCEDIMENTO                                                                \n"
            + "    AND PO.ID_LEGAME_GRUPPO_OGGETTO      = LGO.ID_LEGAME_GRUPPO_OGGETTO                                                     \n"
            + "    AND LGO.ID_LEGAME_GRUPPO_OGGETTO     = BO.ID_LEGAME_GRUPPO_OGGETTO                                                      \n"
            + "    AND P.ID_BANDO                       = BO.ID_BANDO                                                                      \n"
            + "    AND CA.ID_CONTROLLO_AMMINISTRATIVO   = QOCA.ID_CONTROLLO_AMMINISTRATIVO                                                 \n"
            + "    AND QOCA.ID_QUADRO_OGG_CONTROLLO_AMM = BOQCA.ID_QUADRO_OGG_CONTROLLO_AMM                                                \n"
            + "    AND BOQCA.ID_BANDO_OGGETTO           = BO.ID_BANDO_OGGETTO                                                              \n"
            + "    AND Q.CODICE                         = :CODICE_QUADRO                                                                   \n"
            + "    AND Q.ID_QUADRO                      = QO.ID_QUADRO                                                                     \n"
            + " AND                                                                                                                                           \n"
            + "     (                                                                                                                                         \n"
            + "         NOT EXISTS (SELECT K.ID_QUADRO_OGG_CONTROLLO_AMM FROM IUF_R_BAN_OG_QUA_CON_AMM_LIV K WHERE K.ID_BANDO_OGGETTO = BO.ID_BANDO_OGGETTO \n"
            + "                        AND K.ID_QUADRO_OGG_CONTROLLO_AMM = BOQCA.ID_QUADRO_OGG_CONTROLLO_AMM )                                                \n"
            + "         OR                                                                                                                                    \n"
            + "         EXISTS (SELECT K.ID_QUADRO_OGG_CONTROLLO_AMM FROM IUF_R_BAN_OG_QUA_CON_AMM_LIV K, IUF_R_PROCEDIMENTO_LIVELLO PL                     \n"
            + "                    WHERE                                                                                                                      \n"
            + "                    PL.ID_LIVELLO = K.ID_LIVELLO                                                                                               \n"
            + "                    AND PL.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                                                                                 \n"
            + "                    AND K.ID_BANDO_OGGETTO = BO.ID_BANDO_OGGETTO                                                                               \n"
            + "                    AND K.ID_QUADRO_OGG_CONTROLLO_AMM = BOQCA.ID_QUADRO_OGG_CONTROLLO_AMM )                                                    \n"
            + "     )                                                                                                                                         \n"

            + "    AND QO.ID_OGGETTO                    = LGO.ID_OGGETTO                                                                   \n"
            + "    AND QO.ID_QUADRO_OGGETTO             = QOCA.ID_QUADRO_OGGETTO                                                           \n");
    if (ids != null && !ids.isEmpty())
    {
      queryBuilder.append(" AND (");
      int idx = 1;
      for (Long id : ids)
      {
        if (idx > 1)
        {
          queryBuilder.append(" OR ");
        }
        String nameID = "ID_" + idx;
        queryBuilder.append(" BOQCA.ID_QUADRO_OGG_CONTROLLO_AMM = :")
            .append(nameID);
        mapParameterSource.addValue(nameID, id, Types.NUMERIC);
        ++idx;
      }
      queryBuilder.append(" )");
    }
    queryBuilder.append(
        " ORDER BY                                                                   \n"
            + "   ORDINE ASC                                                       \n");
    final String QUERY = queryBuilder.toString();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("CODICE_QUADRO", codiceQuadro, Types.VARCHAR);
      return queryForList(QUERY, mapParameterSource,
          ControlloAmministrativoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("codiceQuadro", codiceQuadro),
              new LogParameter("ids", ids)
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

  public int updateControlliInLocoExPost(
      ControlliInLocoInvestDTO controlliInLocoInvestDTO)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateControlliInLocoExPost]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                                        \n"
        + "   IUF_T_CONTROLLO_IN_LOCO_EXPO                              \n"
        + " SET                                                           \n"
        + "   DATA_PREAVVISO              = :DATA_PREAVVISO,              \n"
        + "   DESCRIZIONE_ALTRO_PREAVVISO = :DESCRIZIONE_ALTRO_PREAVVISO, \n"
        + "   FLAG_CONTROLLO              = :FLAG_CONTROLLO,              \n"
        + "   FLAG_PREAVVISO              = :FLAG_PREAVVISO,              \n"
        + "   ID_TIPOLOGIA_PREAVVISO      = :ID_TIPOLOGIA_PREAVVISO       \n"
        + " WHERE                                                         \n"
        + "   ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("DATA_PREAVVISO",
          controlliInLocoInvestDTO.getDataPreavviso(), Types.DATE);
      mapParameterSource.addValue("DESCRIZIONE_ALTRO_PREAVVISO",
          controlliInLocoInvestDTO.getDescTipologiaPreavviso(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_CONTROLLO",
          controlliInLocoInvestDTO.getFlagControllo(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_PREAVVISO",
          controlliInLocoInvestDTO.getFlagPreavviso(), Types.VARCHAR);
      mapParameterSource.addValue("ID_TIPOLOGIA_PREAVVISO",
          controlliInLocoInvestDTO.getIdTipologiaPreavviso(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          controlliInLocoInvestDTO.getIdProcedimentoOggetto(), Types.NUMERIC);

      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("controlliInLocoInvestDTO",
                  controlliInLocoInvestDTO),
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

  public int insertControlliInLocoExPost(
      ControlliInLocoInvestDTO controlliInLocoInvestDTO)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertControlliInLocoExPost]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " INSERT                                          \n"
        + " INTO                                            \n"
        + "   IUF_T_CONTROLLO_IN_LOCO_EXPO                \n"
        + "   (                                             \n"
        + "     ID_CONTROLLO_IN_LOCO_EXPOST,                \n"
        + "     DATA_PREAVVISO,                             \n"
        + "     DESCRIZIONE_ALTRO_PREAVVISO,                \n"
        + "     FLAG_CONTROLLO,                             \n"
        + "     FLAG_PREAVVISO,                             \n"
        + "     ID_PROCEDIMENTO_OGGETTO,                    \n"
        + "     ID_TIPOLOGIA_PREAVVISO                      \n"
        + "   )                                             \n"
        + "   VALUES                                        \n"
        + "   (                                             \n"
        + "     SEQ_IUF_T_CONTROL_IN_LOCO_EX.NEXTVAL,     \n"
        + "     :DATA_PREAVVISO,                            \n"
        + "     :DESCRIZIONE_ALTRO_PREAVVISO,               \n"
        + "     :FLAG_CONTROLLO,                            \n"
        + "     :FLAG_PREAVVISO,                            \n"
        + "     :ID_PROCEDIMENTO_OGGETTO,                   \n"
        + "     :ID_TIPOLOGIA_PREAVVISO                     \n"
        + "   )                                             \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("DATA_PREAVVISO",
          controlliInLocoInvestDTO.getDataPreavviso(), Types.DATE);
      mapParameterSource.addValue("DESCRIZIONE_ALTRO_PREAVVISO",
          controlliInLocoInvestDTO.getDescTipologiaPreavviso(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_CONTROLLO",
          controlliInLocoInvestDTO.getFlagControllo(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_PREAVVISO",
          controlliInLocoInvestDTO.getFlagPreavviso(), Types.VARCHAR);
      mapParameterSource.addValue("ID_TIPOLOGIA_PREAVVISO",
          controlliInLocoInvestDTO.getIdTipologiaPreavviso(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          controlliInLocoInvestDTO.getIdProcedimentoOggetto(), Types.NUMERIC);

      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("controlliInLocoInvestDTO",
                  controlliInLocoInvestDTO),
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

  public List<InfoExPostsDTO> getElencoInformazioniExposts(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoInformazioniExposts]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT                                                      \n"
        + "   IE.ID_INFO_EXPOST,                                        \n"
        + "   IE.ID_RISCHIO_ELEVATO,                                    \n"
        + "   DRE.VALORE AS DESCR_RISCHIO_ELEVATO,                      \n"
        + "   AE.ID_ANNO_EXPOST,                                        \n"
        + "   DAE.VALORE,                                               \n"
        + "   IE.NOTE                                                   \n"
        + " FROM                                                        \n"
        + "   IUF_T_INFO_EXPOST IE,                                     \n"
        + "   IUF_R_ANNI_EXPOST AE,                                     \n"
        + "   IUF_T_DATI_PROCEDIMENTO DP,                               \n"
        + "   IUF_D_RISCHIO_ELEVATO DRE,                                \n"
        + "   IUF_D_ANNO_EXPOST DAE                                     \n"
        + " WHERE                                                       \n"
        + "   IE.ID_DATI_PROCEDIMENTO = DP.ID_DATI_PROCEDIMENTO         \n"
        + "   AND AE.ID_INFO_EXPOST(+) = IE.ID_INFO_EXPOST              \n"
        + "   AND DRE.ID_RISCHIO_ELEVATO = IE.ID_RISCHIO_ELEVATO        \n"
        + "   AND DAE.ID_ANNO_EXPOST = AE.ID_ANNO_EXPOST                \n"
        + "   AND DP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + " ORDER BY DRE.VALORE, DAE.VALORE                             \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<InfoExPostsDTO>>()
          {

            @Override
            public List<InfoExPostsDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<InfoExPostsDTO> list = null;
              long idInfoExposts;
              long idInfoExpostsLast = -1;
              InfoExPostsDTO info;
              AnnoExPostsDTO anno;
              List<AnnoExPostsDTO> anni;
              while (rs.next())
              {
                if (list == null)
                {
                  list = new ArrayList<InfoExPostsDTO>();
                }

                idInfoExposts = rs.getLong("ID_INFO_EXPOST");
                if (idInfoExpostsLast != idInfoExposts)
                {
                  idInfoExpostsLast = idInfoExposts;
                  info = new InfoExPostsDTO();
                  list.add(info);
                  info.setDescrRischioElevato(
                      rs.getString("DESCR_RISCHIO_ELEVATO"));
                  info.setIdInfoExPosts(idInfoExposts);
                  info.setIdRischioElevato(rs.getLong("ID_RISCHIO_ELEVATO"));
                  info.setNote(rs.getString("NOTE"));
                  anni = new ArrayList<>();
                  info.setAnniExPosts(anni);
                }

                anno = new AnnoExPostsDTO();
                anno.setIdAnnoExPosts(rs.getLong("ID_ANNO_EXPOST"));
                anno.setValore(rs.getString("VALORE"));
              }
              return list;
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

  public InfoExPostsDTO getInformazioniExposts(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getInformazioniExposts]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT                                                      \n"
        + "   IE.ID_INFO_EXPOST,                                        \n"
        + "   IE.ID_RISCHIO_ELEVATO,                                    \n"
        + "   DRE.VALORE AS DESCR_RISCHIO_ELEVATO,                      \n"
        + "   AE.ID_ANNO_EXPOST,                                        \n"
        + "   DAE.VALORE,                                               \n"
        + "   IE.NOTE                                                   \n"
        + " FROM                                                        \n"
        + "   IUF_T_INFO_EXPOST IE,                                     \n"
        + "   IUF_R_ANNI_EXPOST AE,                                     \n"
        + "   IUF_T_DATI_PROCEDIMENTO DP,                               \n"
        + "   IUF_D_RISCHIO_ELEVATO DRE,                                \n"
        + "   IUF_D_ANNO_EXPOST DAE                                     \n"
        + " WHERE                                                       \n"
        + "   IE.ID_DATI_PROCEDIMENTO = DP.ID_DATI_PROCEDIMENTO         \n"
        + "   AND AE.ID_INFO_EXPOST(+) = IE.ID_INFO_EXPOST              \n"
        + "   AND DRE.ID_RISCHIO_ELEVATO(+) = IE.ID_RISCHIO_ELEVATO        \n"
        + "   AND DAE.ID_ANNO_EXPOST(+) = AE.ID_ANNO_EXPOST                \n"
        + "   AND DP.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO \n"
        + " ORDER BY DRE.VALORE, DAE.VALORE                             \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<InfoExPostsDTO>()
          {

            @Override
            public InfoExPostsDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              long idInfoExposts;
              long idInfoExpostsLast = -1;
              InfoExPostsDTO info = null;
              AnnoExPostsDTO anno;
              List<AnnoExPostsDTO> anni = null;
              while (rs.next())
              {
                idInfoExposts = rs.getLong("ID_INFO_EXPOST");
                if (idInfoExpostsLast != idInfoExposts)
                {
                  idInfoExpostsLast = idInfoExposts;
                  info = new InfoExPostsDTO();
                  info.setDescrRischioElevato(
                      rs.getString("DESCR_RISCHIO_ELEVATO"));
                  info.setIdInfoExPosts(idInfoExposts);
                  info.setIdRischioElevato(rs.getLong("ID_RISCHIO_ELEVATO"));
                  info.setNote(rs.getString("NOTE"));
                  anni = new ArrayList<>();
                  info.setAnniExPosts(anni);
                }

                anno = new AnnoExPostsDTO();
                anno.setIdAnnoExPosts(rs.getLong("ID_ANNO_EXPOST"));
                anno.setValore(rs.getString("VALORE"));
                if (anno.getValore() != null)
                  anni.add(anno);
              }
              return info;
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

  public List<DecodificaDTO<Long>> getDecodificheRischioElevato()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDecodificheRischioElevato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT                                                   \n"
        + "   DRE.ID_RISCHIO_ELEVATO AS ID,                          \n"
        + "   DRE.VALORE AS DESCRIZIONE     \n"
        + " FROM                                                     \n"
        + "   IUF_D_RISCHIO_ELEVATO DRE                              \n"
        + " ORDER BY DRE.VALORE                            		   \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DecodificaDTO<Long>>>()
          {

            @Override
            public List<DecodificaDTO<Long>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DecodificaDTO<Long>> list = new ArrayList<>();
              while (rs.next())
              {
                list.add(new DecodificaDTO<Long>(rs.getLong("ID"),
                    rs.getString("DESCRIZIONE")));
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

  public List<AnnoExPostsDTO> getDecodificheAnnoExpost(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDecodificheAnnoExpost]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT                                            \n"
        + "   DRE.ID_ANNO_EXPOST AS ID,                       \n"
        + "   DRE.VALORE AS DESCRIZIONE, 						\n"
        + "	(SELECT NVL(COUNT(*),0) FROM IUF_T_INFO_EXPOST A , IUF_R_ANNI_EXPOST B, IUF_T_DATI_PROCEDIMENTO P \n"
        + "	WHERE A.ID_INFO_EXPOST = B.ID_INFO_EXPOST 								\n"
        + "	AND  P.ID_DATI_PROCEDIMENTO = A.ID_DATI_PROCEDIMENTO AND B.ID_ANNO_EXPOST = DRE.ID_ANNO_EXPOST					\n"
        + "	AND P.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO) AS CHECKED 	\n"
        + " FROM                                                     					\n"
        + "   IUF_D_ANNO_EXPOST DRE                              						\n"
        + " ORDER BY DRE.VALORE                            		   					\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
        idProcedimentoOggetto, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<AnnoExPostsDTO>>()
          {

            @Override
            public List<AnnoExPostsDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<AnnoExPostsDTO> list = new ArrayList<>();
              while (rs.next())
              {
                AnnoExPostsDTO anno = new AnnoExPostsDTO();
                anno.setChecked(rs.getLong("CHECKED") > 0);
                anno.setIdAnnoExPosts(rs.getLong("ID"));
                anno.setValore(rs.getString("DESCRIZIONE"));
                list.add(anno);
              }
              return list;
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

  public long inserInfoExPost(long idDatiProcedimento, InfoExPostsDTO info)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::inserInfoExPost]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_T_INFO_EXPOST 			\n"
        + " (                                         \n"
        + "   ID_INFO_EXPOST,             			\n"
        + "   ID_DATI_PROCEDIMENTO,                   \n"
        + "   ID_RISCHIO_ELEVATO,              		\n"
        + "   NOTE                         			\n"
        + " )VALUES(                                  \n"
        + "   :ID_INFO_EXPOST, 						\n"
        + "   :ID_DATI_PROCEDIMENTO,                  \n"
        + "   :ID_RISCHIO_ELEVATO,             		\n"
        + "   :NOTE                        			\n"
        + " )                                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_DATI_PROCEDIMENTO", idDatiProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_RISCHIO_ELEVATO",
          info.getIdRischioElevato(), Types.NUMERIC);
      mapParameterSource.addValue("NOTE", info.getNote(), Types.VARCHAR);
      long id = getNextSequenceValue("SEQ_IUF_T_INFO_EXPOST");
      mapParameterSource.addValue("ID_INFO_EXPOST", id, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return id;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DATI_PROCEDIMENTO", idDatiProcedimento),
              new LogParameter("ID_RISCHIO_ELEVATO",
                  info.getIdRischioElevato()),
              new LogParameter("NOTE", info.getNote())
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

  public void inserRAnniExPost(long idInfoExPosts, long idAnnoExPosts)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::inserInfoExPost]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT INTO IUF_R_ANNI_EXPOST 			\n"
        + " (                                         \n"
        + "   ID_INFO_EXPOST,             			\n"
        + "   ID_ANNO_EXPOST                   		\n"
        + " )VALUES(                                  \n"
        + "   :ID_INFO_EXPOST,                  		\n"
        + "   :ID_ANNO_EXPOST             			\n"
        + " )                                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_INFO_EXPOST", idInfoExPosts,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_ANNO_EXPOST", idAnnoExPosts,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_INFO_EXPOST", idInfoExPosts),
              new LogParameter("ID_ANNO_EXPOST", idAnnoExPosts)
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

  public boolean procedimentoHasOggettoInStato(long idProcedimento,
      String codiceOggetto, long idStatoOggetto)
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::procedimentoHasOggettoTrasmesso]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT           																		\n"
        + "   COUNT(*)                      													\n"
        + " FROM                            													\n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,    												\n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,    												\n"
        + "   IUF_D_OGGETTO O,    																\n"
        + "   IUF_T_ITER_PROCEDIMENTO_OGGE ITER  												\n"
        + " WHERE                           													\n"
        + "   PO.ID_PROCEDIMENTO_OGGETTO IN 													\n"
        + "			(SELECT DISTINCT ID_PROCEDIMENTO_OGGETTO 									\n"
        + "				FROM IUF_T_PROCEDIMENTO_OGGETTO 										\n"
        + "				WHERE ID_PROCEDIMENTO = :ID_PROCEDIMENTO)								\n"
        + "	  AND PO.ID_PROCEDIMENTO_OGGETTO = ITER.ID_PROCEDIMENTO_OGGETTO 					\n"
        + "	  AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO 					\n"
        + "	  AND LGO.ID_OGGETTO = O.ID_OGGETTO 												\n"
        + "	  AND O.CODICE = :CODICE_OGGETTO													\n"
        + "	  AND PO.ID_PROCEDIMENTO_OGGETTO = ITER.ID_PROCEDIMENTO_OGGETTO 					\n"
        + "	  AND ITER.ID_STATO_OGGETTO = :ID_STATO_OGGETTO		 								\n"
        + "	  AND ITER.DATA_FINE IS NULL						 								\n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_STATO_OGGETTO", idStatoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("CODICE_OGGETTO", codiceOggetto,
          Types.VARCHAR);

      return queryForLong(QUERY, mapParameterSource) != 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<ProcedimentoAgricoloDTO> getProcedimentiAgricoli(String gruppoHomePage) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getProcedimentiAgricoli]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY =    " SELECT                              \n"
        + "    PA.ID_PROCEDIMENTO_AGRICOLO,     \n"
        + "    PA.DESCRIZIONE,                  \n"
        + "    PA.CODICE,                       \n"
        + "    PA.DESCRIZIONE_ESTESA            \n"
        + "  FROM                               \n"
        + "    IUF_D_PROCEDIMENTO_AGRICOLO PA \n"
        + (StringUtils.isBlank(gruppoHomePage) ? "" : " WHERE UPPER(PA.GRUPPO_HOME_PAGE) LIKE UPPER(:GRUPPO_HOME_PAGE) \n")
        + "	 ORDER BY PA.ORDINAMENTO			\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    if(StringUtils.isNotBlank(gruppoHomePage)) {
      mapParameterSource.addValue("GRUPPO_HOME_PAGE", gruppoHomePage,
          Types.VARCHAR);
    }
    
    try
    {
      return queryForList(QUERY, mapParameterSource, ProcedimentoAgricoloDTO.class);
    }
    catch (Exception t)
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

  public boolean verificaEsistenzaProcedimentoAgricolo(
      int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::verificaEsistenzaProcedimentoAgricolo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                    \n"
        + "   COUNT(*)                                                \n"
        + " FROM                                                      \n"
        + "   IUF_D_PROCEDIMENTO_AGRICOLO PA                        \n"
        + " WHERE                                                     \n"
        + "   PA.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO \n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO",
          idProcedimentoAgricolo,
          Types.NUMERIC);
      try
      {
        return true;//queryForLong(QUERY, mapParameterSource) > 0;
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
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
  
  public boolean verificaEsistenzaGruppoHomePage(String gruppoHomePage) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::verificaEsistenzaGruppoHomePage]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                    \n"
        + "   COUNT(*)                                                \n"
        + " FROM                                                      \n"
        + "   IUF_D_PROCEDIMENTO_AGRICOLO PA                        \n"
        + " WHERE                                                     \n"
        + "   UPPER(PA.GRUPPO_HOME_PAGE) LIKE UPPER(:GRUPPO_HOME_PAGE) \n";

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("GRUPPO_HOME_PAGE",
          gruppoHomePage,
          Types.VARCHAR);
      try
      {
        return queryForLong(QUERY, mapParameterSource) > 0;
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
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }


	  
	public ContenutoFileAllegatiDTO getImmagineDaVisualizzare(long idProcedimentoAgricolo) throws InternalUnexpectedException {
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

		      return queryForObject(QUERY, mapParameterSource, ContenutoFileAllegatiDTO.class);
		    }catch (Exception e)
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
	}

	public List<DecodificaDTO<Long>> getListaTipiAiuto(long a, long b)
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

    String QUERY = "select ra.id_regime_aiuto as id, ra.descrizione_estesa as codice, ra.descrizione_breve as descrizione from IUF_D_REGIME_AIUTO ra where id_regime_aiuto in (\r\n" + 
        "select ap.id_regime_aiuto from IUF_t_regime_aiuto_previsto ap where ap.id_bando = :ID_BANDO)";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
//      mapParameterSource.addValue("ID_LIVELLO", a,
//          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", b,
          Types.NUMERIC);
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", b )
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
	
	public String getIdentificativoMinProcedimentoOggettoByProcedimento(long idProcedimento) throws InternalUnexpectedException {
		String THIS_METHOD = "[" + THIS_CLASS + "::getIdentificativoMinProcedimentoOggettoByProcedimento]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    String QUERY = 
	                   " SELECT SUBSTR(DA.IDENTIFICATIVO,1,11)         \n"
		    		 + " FROM                                          \n"
		    		 + "   (SELECT PO.IDENTIFICATIVO                   \n"
		    		 + "   FROM IUF_T_PROCEDIMENTO_OGGETTO PO        \n"
		    		 + "   WHERE PO.ID_PROCEDIMENTO = :ID_PROCEDIMENTO \n"
		    		 + "   AND PO.IDENTIFICATIVO IS NOT NULL           \n"
		    		 + "   ORDER BY PO.ID_PROCEDIMENTO_OGGETTO         \n"
		    		 + "   ) DA                                        \n"
		    		 + " WHERE ROWNUM = 1                              \n"
					 ;

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
		 mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento, Types.NUMERIC);

	      return queryForString(QUERY, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	    	 InternalUnexpectedException e = new InternalUnexpectedException(t,
	   	          new LogParameter[]
	   	          {  },
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

  public List<DecodificaDTO<Long>> getRecapitiAmm(long idAmmCompetenza, long extIdProcedimento, Long idTipoRecapito) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getPecAmmCompetenza]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String QUERY = " SELECT ID_TIPO_RECAPITO as ID, \n" + 
        " RECAPITO as CODICE, \n" + 
        " DESC_TIPO_RECAPITO DESCRIZIONE \n"
        + " FROM SMRCOMUNE_V_AMM_COMP_RECAPITI \n"
        + " WHERE ID_AMM_COMPETENZA = :ID_AMM_COMPETENZA \n"
        + "AND EXT_ID_PROCEDIMENTO = :EXT_ID_PROCEDIMENTO \n";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_AMM_COMPETENZA", idAmmCompetenza, Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_PROCEDIMENTO", extIdProcedimento, Types.NUMERIC);

    if(idTipoRecapito!=null) {
      QUERY += "AND ID_TIPO_RECAPITO = :ID_TIPO_RECAPITO \n";
      mapParameterSource.addValue("ID_TIPO_RECAPITO", idTipoRecapito, Types.NUMERIC);
    }

    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
       InternalUnexpectedException e = new InternalUnexpectedException(t,
              new LogParameter[]
              {  },
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

  public List<SportelloDTO> getSportelliList(String abi, String cab, String denominazione) throws InternalUnexpectedException {
    String THIS_METHOD = "[" + THIS_CLASS + "::getSportelliList]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String QUERY = "select cap, cab, abi, denominazione_banca as denominazione, indirizzo, descrizione_provincia as provincia, "
        + "descrizione_comune_sportello as comune, id_sportello as id from SMRGAA_V_BANCA_SPORTELLO  where 1=1 ";
    if(abi != null && !abi.equalsIgnoreCase("")) {
      QUERY = QUERY + " AND abi = :ABI";
      mapParameterSource.addValue("ABI", abi,
          Types.VARCHAR);
    }
    if(cab != null && !cab.equalsIgnoreCase("")) {
      QUERY = QUERY + " AND cab = :CAB";
      mapParameterSource.addValue("CAB", cab,
          Types.VARCHAR);
    }
    if(denominazione != null && !denominazione.equalsIgnoreCase("")) {
      QUERY = QUERY + " AND denominazione_banca like :DENOMINAZIONE";
      mapParameterSource.addValue("DENOMINAZIONE", "%"+denominazione.toUpperCase()+"%",
          Types.VARCHAR);
    }

    
    try
    {
     

      return queryForList(QUERY, mapParameterSource, SportelloDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ABI", abi)
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

 
}
