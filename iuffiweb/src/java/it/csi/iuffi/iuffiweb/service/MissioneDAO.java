package it.csi.iuffi.iuffiweb.service;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.VerbaleDTO;
import it.csi.iuffi.iuffiweb.model.api.Missione;
import it.csi.iuffi.iuffiweb.model.request.MissioneRequest;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import oracle.jdbc.OracleTypes;

public class MissioneDAO extends BaseDAO
{

  
  private static final String THIS_CLASS = MissioneDAO.class.getSimpleName();

  public MissioneDAO(){  }

  private static final String INSERT = "INSERT INTO iuf_t_missione(" +
             "id_missione," + 
            "numero_trasferta," + 
            "data_ora_inizio_missione," +
            "data_ora_fine_missione," +
            "id_ispettore_assegnato," +
            "timestamp_inizio_missione," +
            "timestamp_fine_missione," +
            "pdf_trasferta," +
            "nome_file," +
            "stato," +
            "ext_id_utente_aggiornamento," +
            "data_ultimo_aggiornamento" +
        ") " + 
        "VALUES(seq_iuf_t_missione.nextval, :numeroTrasferta, :dataOraInizio, :dataOraFine, :idIspettoreAssegnato, SYSDATE, NULL, :pdfTrasferta, :nomeFile," +
            ":stato, :ext_id_utente_aggiornamento, SYSDATE)";

  private static final String UPDATE = "UPDATE iuf_t_missione SET numero_trasferta = :numeroTrasferta," +
                                              "data_ora_inizio_missione = :dataOraInizio," +
                                              "data_ora_fine_missione = :dataOraFine," +
                                              "id_ispettore_assegnato = :idIspettoreAssegnato," +
                                              "pdf_trasferta = :pdfTrasferta," +
                                              "nome_file = :nomeFile," +
                                              "stato = :stato," +
                                              "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento," +
                                              "data_ultimo_aggiornamento = SYSDATE," +
                                              "timestamp_fine_missione = (CASE WHEN :dataOraFine IS NOT NULL THEN SYSDATE ELSE NULL END)" +
                                        " WHERE id_missione = :idMissione";
  
  private static final String SELECT_ALL = "SELECT a.id_missione," +
                                                 "to_char(a.data_ora_inizio_missione, 'HH24:MI:SS') as ora_inizio," + 
                                                 "to_char(a.timestamp_fine_missione, 'HH24:MI:SS') as ora_fine,"+
                                                "a.numero_trasferta," + 
                                                "a.data_ora_inizio_missione," + 
                                                "a.data_ora_fine_missione," + 
                                                "a.id_ispettore_assegnato," + 
                                                "a.timestamp_inizio_missione," + 
                                                "a.timestamp_fine_missione," + 
                                                "a.nome_file," + 
                                                "a.ext_id_utente_aggiornamento," + 
                                                "a.data_ultimo_aggiornamento," + 
                                                "a.stato,(b.cognome || ' ' || b.nome) as ispettore " + 
                                           " FROM iuf_t_missione a,iuf_t_anagrafica b" + 
                                           " WHERE a.id_ispettore_assegnato=b.id_anagrafica" + 
                                           " ORDER by id_missione DESC";
  
  private static final String SELECT_BY_ID = "SELECT id_missione, \r\n" + 
                                                "numero_trasferta, \r\n" + 
                                                "data_ora_inizio_missione, \r\n" + 
                                                "data_ora_fine_missione, \r\n" + 
                                                "id_ispettore_assegnato, \r\n" + 
                                                "timestamp_inizio_missione, \r\n" + 
                                                "timestamp_fine_missione, \r\n" + 
                                                "pdf_trasferta, \r\n" + 
                                                "nome_file, \r\n" + 
                                                "ext_id_utente_aggiornamento, \r\n" + 
                                                "data_ultimo_aggiornamento, \r\n" + 
                                                "stato\r\n" + 
                                              " FROM iuf_t_missione " +
                                              "WHERE id_missione = :idMissione ORDER by id_missione DESC";

  private static final String SELECT_ISPETTORI = "SELECT a.* FROM iuf_t_anagrafica a, iuf_r_ispettore_aggiunto ia " +
                                                  "WHERE ia.id_anagrafica = a.id_anagrafica " +
                                                  "AND ia.id_missione = :idMissione " +
                                                  "ORDER BY a.cognome,a.nome";
  
  private static final String INSERT_ISPETTORI_AGGIUNTI = "INSERT INTO iuf_r_ispettore_aggiunto(id_missione, id_anagrafica, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) " +
                                                          "VALUES(:idMissione, :idAnagrafica, :ext_id_utente_aggiornamento, SYSDATE)";
  
  private static final String DELETE_ISPETTORE_AGGIUNTO = "DELETE FROM iuf_r_ispettore_aggiunto WHERE id_missione = :idMissione AND id_anagrafica = :idAnagrafica";

  private static final String SELECT_BY_FILTER = "SELECT m.id_missione, \r\n" +
      "       m.numero_trasferta,\r\n" +
      "       m.data_ora_inizio_missione,\r\n" +
      "       m.data_ora_fine_missione,\r\n" +
      "       to_char(m.data_ora_inizio_missione, 'HH24:MI') as ora_inizio,\r\n" +
      "       to_char(m.data_ora_fine_missione, 'HH24:MI') as ora_fine,\r\n" +
      "       m.id_ispettore_assegnato,\r\n" +
      "       a.cognome as cognome_ispettore,\r\n" +
      "       a.nome as nome_ispettore,\r\n" +
      "       m.timestamp_inizio_missione,\r\n" +
      "       m.timestamp_fine_missione,\r\n" +
      //"       m.pdf_trasferta, \r\n" + 
      "       m.nome_file,\r\n" +
      "       m.ext_id_utente_aggiornamento,\r\n" +
      "       m.data_ultimo_aggiornamento,\r\n" +
      "       m.stato,\r\n" +
      "       (SELECT LISTAGG(cognome_nome,', ') WITHIN GROUP (ORDER BY seq)\r\n" + 
      "        FROM (\r\n" + 
      "              SELECT m.id_missione,t.cognome||' '||t.nome AS cognome_nome,0 AS seq FROM iuf_t_missione m,iuf_t_anagrafica t\r\n" +
      "              WHERE m.id_ispettore_assegnato = t.id_anagrafica\r\n" +
      "              UNION\r\n" +
      "              SELECT ia.id_missione,a.cognome||' '||a.nome, 1 AS seq FROM iuf_r_ispettore_aggiunto ia,iuf_t_anagrafica a\r\n" +
      "              WHERE ia.id_anagrafica = a.id_anagrafica\r\n" +
      "              ) WHERE id_missione = m.id_missione) AS ispettore,\r\n" +

      "       DECODE((SELECT COUNT(*) FROM iuf_t_ispezione_visiva v, iuf_t_rilevazione r WHERE v.id_rilevazione = r.id_rilevazione AND r.id_missione = m.id_missione),0,'N','S') visual,\r\n" +
      "       DECODE((SELECT COUNT(*) FROM iuf_t_campionamento ca, iuf_t_rilevazione r WHERE ca.id_rilevazione = r.id_rilevazione AND r.id_missione = m.id_missione),0,'N','S') campionamento,\r\n" +
      "       DECODE((SELECT COUNT(*) FROM iuf_t_trappolaggio tr, iuf_t_rilevazione r WHERE tr.id_rilevazione = r.id_rilevazione AND r.id_missione = m.id_missione),0,'N','S') trappolaggio,\r\n" +
      "       DECODE((SELECT COUNT(*) FROM iuf_t_rilevazione ril WHERE ril.id_missione = m.id_missione AND ril.flag_emergenza = 'S'),0,'N','S') flag_emergenza,\r\n" +
      "       (SELECT NVL(MAX(v.id_stato_verbale),0) FROM iuf_t_verbale v WHERE v.id_missione = m.id_missione) id_stato_verbale \r\n" +
      " FROM iuf_t_missione m, iuf_t_anagrafica a \r\n" + 
      "WHERE m.id_ispettore_assegnato = a.id_anagrafica \r\n" +
      "  AND TRUNC(m.data_ora_inizio_missione) BETWEEN TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2100'),'DD/MM/YYYY') \r\n" + 
      "  AND NVL(m.numero_trasferta,0) = NVL(:numeroTrasferta,NVL(m.numero_trasferta,0)) \r\n" + 
      "  AND m.id_ispettore_assegnato = NVL(:idIspettore,m.id_ispettore_assegnato)\r\n" +
      "  AND UPPER(a.nome) = UPPER(NVL(:nomeIspettore,a.nome))\r\n" +
      "  AND UPPER(a.cognome) = UPPER(NVL(:cognomeIspettore,a.cognome))\r\n";

  private static final String SELECT_MISSIONI_API = "SELECT m.id_missione, \r\n" + 
      "       m.numero_trasferta, \r\n" + 
      "       TO_CHAR(m.data_ora_inizio_missione,'DD-MM-YYYY') as data_missione, \r\n" +
      "       TO_CHAR(m.data_ora_inizio_missione,'HH24:MI') as ora_inizio, \r\n" +
      "       TO_CHAR(m.data_ora_fine_missione,'HH24:MI') as ora_fine, \r\n" +
      "       m.data_ora_fine_missione, \r\n" + 
      "       m.id_ispettore_assegnato, \r\n" +
      "       a.cognome as cognome_ispettore,\r\n" +
      "       a.nome as nome_ispettore,\r\n" +
      "       m.timestamp_inizio_missione,\r\n" + 
      "       m.timestamp_fine_missione,\r\n" +
      "       NVL(dbms_lob.getlength(m.pdf_trasferta),0) pdf_size,\r\n" +
      "       m.nome_file, \r\n" + 
      "       m.ext_id_utente_aggiornamento, \r\n" + 
      "       m.data_ultimo_aggiornamento, \r\n" + 
      "       m.stato,\r\n" +
      "       (select count(*) from iuf_t_rilevazione ril where ril.id_missione = m.id_missione) as num_rilevazioni\r\n" +
      " FROM iuf_t_missione m, iuf_t_anagrafica a \r\n" + 
      "WHERE m.id_ispettore_assegnato = a.id_anagrafica " +
      "  AND TRUNC(m.data_ora_inizio_missione) BETWEEN TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2100'),'DD/MM/YYYY') \r\n" + 
      "  AND NVL(m.numero_trasferta,0) = NVL(:numeroTrasferta,NVL(m.numero_trasferta,0))\r\n" +
      "  AND m.id_ispettore_assegnato = NVL(:idIspettore,m.id_ispettore_assegnato)\r\n" +
      "  AND UPPER(a.nome) = UPPER(NVL(:nomeIspettore,a.nome))\r\n" +
      "  AND UPPER(a.cognome) = UPPER(NVL(:cognomeIspettore,a.cognome))\r\n" +
      "  AND m.id_missione = NVL(:idMissione,m.id_missione)\r\n" +
      "  AND (EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia,iuf_t_anagrafica an\r\n" + 
      "               WHERE ia.id_anagrafica = an.id_anagrafica AND ia.id_missione = m.id_missione\r\n" + 
      "               AND an.cf_anagrafica_est = NVL(:cfIspettore,an.cf_anagrafica_est)) OR a.cf_anagrafica_est = NVL(:cfIspettore,a.cf_anagrafica_est)) ";

  private static final String FILTER_TRAPPOLAGGIO = " AND EXISTS (SELECT 1 FROM iuf_t_rilevazione r, iuf_t_trappolaggio t WHERE r.id_rilevazione = t.id_rilevazione AND r.id_missione = m.id_missione)\r\n";
  
  private static final String FILTER_CAMPIONAMENTO = " AND EXISTS (SELECT 1 FROM iuf_t_rilevazione r, iuf_t_campionamento c WHERE r.id_rilevazione = c.id_rilevazione AND r.id_missione = m.id_missione)\r\n";
  
  private static final String FILTER_VISUAL = " AND EXISTS (SELECT 1 FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v WHERE r.id_rilevazione = v.id_rilevazione AND r.id_missione = m.id_missione)\r\n";
  
  private static final String FILTER_SPECIE_VEGETALE = 
      "  AND EXISTS (SELECT 1 \r\n" + 
      "                FROM (\r\n" + 
      "                    SELECT r.id_missione,c.id_specie_vegetale\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_campionamento c\r\n" + 
      "                     WHERE r.id_rilevazione = c.id_rilevazione\r\n" + 
      "                    UNION\r\n" + 
      "                    SELECT r.id_missione,tr.id_specie_veg\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_trappolaggio t, iuf_t_trappola tr\r\n" + 
      "                     WHERE r.id_rilevazione = t.id_rilevazione AND t.id_trappola = tr.id_trappola\r\n" + 
      "                    UNION\r\n" + 
      "                    SELECT r.id_missione, v.id_specie_vegetale\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v\r\n" + 
      "                     WHERE r.id_rilevazione = v.id_rilevazione\r\n" + 
      "              ) WHERE id_missione = m.id_missione AND id_specie_vegetale = NVL(:idSpecieVegetale,id_specie_vegetale))\r\n";
  
  private static final String FILTER_SPECIE_VEGETALE_MULTIPLE = 
      "  AND EXISTS (SELECT 1 \r\n" + 
      "                FROM (\r\n" + 
      "                    SELECT r.id_missione,c.id_specie_vegetale\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_campionamento c\r\n" + 
      "                     WHERE r.id_rilevazione = c.id_rilevazione\r\n" + 
      "                    UNION\r\n" + 
      "                    SELECT r.id_missione,tr.id_specie_veg\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_trappolaggio t, iuf_t_trappola tr\r\n" + 
      "                     WHERE r.id_rilevazione = t.id_rilevazione AND t.id_trappola = tr.id_trappola\r\n" + 
      "                    UNION\r\n" + 
      "                    SELECT r.id_missione, v.id_specie_vegetale\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v\r\n" + 
      "                     WHERE r.id_rilevazione = v.id_rilevazione\r\n" + 
      "              ) WHERE id_missione = m.id_missione AND id_specie_vegetale IN (%s))\r\n";
  
  private static final String FILTER_ORGANISMO_NOCIVO = 
      "  AND EXISTS (\r\n" + 
      "              SELECT 1\r\n" + 
      "              FROM (\r\n" + 
      "                    SELECT r.id_missione,cso.id_specie_on\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_campionamento c, iuf_r_campionamento_spec_on cso\r\n" + 
      "                     WHERE r.id_rilevazione = c.id_rilevazione AND c.id_campionamento = cso.id_campionamento\r\n" + 
      "                    UNION\r\n" + 
      "                    SELECT r.id_missione, vso.id_specie_on\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v, iuf_r_isp_visiva_spec_on vso\r\n" + 
      "                     WHERE r.id_rilevazione = v.id_rilevazione AND v.id_ispezione = vso.id_ispezione\r\n" + 
      "                     ) WHERE id_missione = m.id_missione AND id_specie_on = NVL(:idOrganismoNocivo,id_specie_on))\r\n";

  private static final String FILTER_ORGANISMO_NOCIVO_MULTIPLE = 
      "  AND EXISTS (\r\n" + 
      "              SELECT 1\r\n" + 
      "              FROM (\r\n" + 
      "                    SELECT r.id_missione,cso.id_specie_on\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_campionamento c, iuf_r_campionamento_spec_on cso\r\n" + 
      "                     WHERE r.id_rilevazione = c.id_rilevazione AND c.id_campionamento = cso.id_campionamento\r\n" + 
      "                    UNION\r\n" + 
      "                    SELECT r.id_missione, vso.id_specie_on\r\n" + 
      "                      FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v, iuf_r_isp_visiva_spec_on vso\r\n" + 
      "                     WHERE r.id_rilevazione = v.id_rilevazione AND v.id_ispezione = vso.id_ispezione\r\n" + 
      "                     ) WHERE id_missione = m.id_missione AND id_specie_on IN (%s))\r\n";

  private static final String FILTER_COMUNE = 
      "  AND EXISTS (SELECT 1 \r\n" +
      "                FROM (\r\n" +
      "                    SELECT r.id_missione, c.istat_comune, amm.descrizione_comune \r\n" +
      "                      FROM iuf_t_rilevazione r, iuf_t_campionamento c, smrgaa_v_dati_amministrativi amm \r\n" +
      "                     WHERE r.id_rilevazione = c.id_rilevazione (+) \r\n" +
      "                       AND c.istat_comune = amm.istat_comune (+) \r\n" +
      "                    UNION\r\n" +
      "                    SELECT r.id_missione, t.istat_comune, amm.descrizione_comune \r\n" +
      "                      FROM iuf_t_rilevazione r, iuf_t_trappolaggio t, smrgaa_v_dati_amministrativi amm \r\n" +
      "                     WHERE r.id_rilevazione = t.id_rilevazione (+) \r\n" +
      "                       AND t.istat_comune = amm.istat_comune (+) \r\n" +
      "                    UNION\r\n" +
      "                    SELECT r.id_missione, v.istat_comune, amm.descrizione_comune \r\n" +
      "                      FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v, smrgaa_v_dati_amministrativi amm \r\n" +
      "                     WHERE r.id_rilevazione = v.id_rilevazione (+) \r\n" +
      "                       AND v.istat_comune = amm.istat_comune (+) \r\n" +
      "              ) WHERE id_missione = m.id_missione \r\n" +
      "                  AND istat_comune = NVL(:istatComune,istat_comune) \r\n" +
      "                  AND UPPER(descrizione_comune) LIKE NVL(UPPER(:descComune),'%')) \r\n";

  private static final String FILTER_OBIETTIVO_NESSUNO =  // aggiungere anche emergenza una volta che ci srà il campo in tabella!!!
      "  AND NOT EXISTS (SELECT 1 \r\n" + 
      "                    FROM ( \r\n" + 
      "                          SELECT r.id_missione, o.euro\r\n" + 
      "                            FROM iuf_t_rilevazione r, iuf_t_campionamento c,\r\n" + 
      "                                 iuf_r_campionamento_spec_on cso, iuf_d_organismo_nocivo o\r\n" + 
      "                           WHERE r.id_rilevazione = c.id_rilevazione AND c.id_campionamento = cso.id_campionamento\r\n" + 
      "                             AND cso.id_specie_on = o.id_organismo_nocivo\r\n" + 
      "                          UNION \r\n" + 
      "                          SELECT r.id_missione, o.euro\r\n" + 
      "                            FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v,\r\n" + 
      "                                 iuf_r_isp_visiva_spec_on vso, iuf_d_organismo_nocivo o\r\n" + 
      "                           WHERE r.id_rilevazione = v.id_rilevazione AND v.id_ispezione = vso.id_ispezione\r\n" + 
      "                             AND vso.id_specie_on = o.id_organismo_nocivo\r\n" + 
      "                           ) WHERE id_missione = m.id_missione AND euro = 'S')\r\n";

  private static final String FILTER_OBIETTIVO_INDAGINE_UFFICIALE =
      "      AND EXISTS (SELECT 1 \r\n" +
      "                    FROM ( \r\n" +
      "                          SELECT r.id_missione, o.euro\r\n" +
      "                            FROM iuf_t_rilevazione r, iuf_t_campionamento c,\r\n" +
      "                                 iuf_r_campionamento_spec_on cso, iuf_d_organismo_nocivo o\r\n" +
      "                           WHERE r.id_rilevazione = c.id_rilevazione AND c.id_campionamento = cso.id_campionamento\r\n" +
      "                             AND cso.id_specie_on = o.id_organismo_nocivo\r\n" +
      "                          UNION \r\n" +
      "                          SELECT r.id_missione, o.euro\r\n" +
      "                            FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v,\r\n" +
      "                                 iuf_r_isp_visiva_spec_on vso, iuf_d_organismo_nocivo o\r\n" +
      "                           WHERE r.id_rilevazione = v.id_rilevazione AND v.id_ispezione = vso.id_ispezione\r\n" +
      "                             AND vso.id_specie_on = o.id_organismo_nocivo\r\n" +
      "                          UNION \r\n" +
      "                          SELECT r.id_missione, o.euro\r\n" +
      "                            FROM iuf_t_rilevazione r, iuf_t_trappolaggio t, iuf_d_organismo_nocivo o\r\n" +
      "                           WHERE r.id_rilevazione = t.id_rilevazione\r\n" +
      "                             AND t.id_organismo_nocivo = o.id_organismo_nocivo\r\n" +
      "                           ) WHERE id_missione = m.id_missione AND euro = 'S')\r\n";

  private static final String FILTER_OBIETTIVO_EMERGENZA =
      " AND EXISTS (SELECT 1 FROM iuf_t_rilevazione r WHERE r.id_missione = m.id_missione AND r.flag_emergenza = 'S')\r\n"; 
  
  private static final String SELECT_MISSIONE_BY_FILTER_FOR_VERBALI = "SELECT m.id_missione, \r\n" +
      "       m.numero_trasferta, \r\n" +
      "       m.data_ora_inizio_missione, \r\n" +
      "       m.data_ora_fine_missione, \r\n" +
      "       to_char(m.data_ora_inizio_missione, 'HH24:MI') as ora_inizio," +
      "       to_char(m.data_ora_fine_missione, 'HH24:MI') as ora_fine," +
      "       m.id_ispettore_assegnato, \r\n" +
      "       a.cognome as cognome_ispettore,\r\n" +
      "       a.nome as nome_ispettore,\r\n" +
      "       m.timestamp_inizio_missione, \r\n" +
      "       m.timestamp_fine_missione, \r\n" +
      //"       m.pdf_trasferta, \r\n" + 
      "       m.nome_file, \r\n" +
      "       m.ext_id_utente_aggiornamento, \r\n" +
      "       m.data_ultimo_aggiornamento, \r\n" +
      "       m.stato,\r\n" +
      "		    v.id_stato_verbale, \r\n" +
      "       v.id_verbale, \r\n" +
      "       (SELECT listagg(cognome_nome,', ') WITHIN GROUP (ORDER BY seq)\r\n" + 
      "        FROM (\r\n" + 
      "              SELECT m.id_missione,t.cognome||' '||t.nome AS cognome_nome,0 AS seq FROM iuf_t_missione m,iuf_t_anagrafica t\r\n" +
      "              WHERE m.id_ispettore_assegnato = t.id_anagrafica\r\n" +
      "              UNION\r\n" +
      "              SELECT ia.id_missione,a.cognome||' '||a.nome, 1 AS seq FROM iuf_r_ispettore_aggiunto ia,iuf_t_anagrafica a\r\n" +
      "              WHERE ia.id_anagrafica = a.id_anagrafica\r\n" +
      "              ) WHERE id_missione = m.id_missione) AS ispettore,\r\n" +
      "       v.num_verbale \r\n" +
      " FROM iuf_t_missione m, iuf_t_anagrafica a, iuf_t_verbale v\r\n" + 
      " WHERE m.id_ispettore_assegnato = a.id_anagrafica \r\n" +
      "  AND TRUNC(m.data_ora_inizio_missione) BETWEEN TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2100'),'DD/MM/YYYY') \r\n" + 
      "  AND NVL(m.numero_trasferta,0) = NVL(:numeroTrasferta,NVL(m.numero_trasferta,0)) \r\n" + 
      "  AND m.id_ispettore_assegnato = NVL(:idIspettore,m.id_ispettore_assegnato)\r\n" +
      "  AND UPPER(a.nome) = UPPER(NVL(:nomeIspettore,a.nome))\r\n" +
      "  AND UPPER(a.cognome) = UPPER(NVL(:cognomeIspettore,a.cognome))\r\n" +
      "  AND m.id_missione = v.id_missione (+)\r\n" +
      "  AND m.data_ora_fine_missione IS NOT NULL \r\n";
  
  private static final String SELECT_VERBALI_BY_MISSIONE = "        SELECT m.id_missione, \r\n" + 
      "       v.id_verbale,\r\n" + 
      "       v.data_verbale,\r\n" + 
      "       v.pdf_verbale,\r\n" +
      "       (SELECT listagg(cognome_nome,', ') WITHIN GROUP (ORDER BY seq)\r\n" + 
      "        FROM (\r\n" + 
      "              SELECT m.id_missione,t.cognome||' '||t.nome AS cognome_nome,0 AS seq FROM iuf_t_missione m,iuf_t_anagrafica t\r\n" + 
      "              WHERE m.id_ispettore_assegnato = t.id_anagrafica\r\n" + 
      "              UNION\r\n" + 
      "              SELECT ia.id_missione,a.cognome||' '||a.nome, 1 AS seq FROM iuf_r_ispettore_aggiunto ia,iuf_t_anagrafica a\r\n" + 
      "              WHERE ia.id_anagrafica = a.id_anagrafica\r\n" + 
      "              ) WHERE id_missione = m.id_missione) AS ispettore \r\n" + 
      " FROM iuf_t_missione m, iuf_t_anagrafica a, iuf_t_verbale v \r\n" + 
      "WHERE m.id_ispettore_assegnato = a.id_anagrafica \r\n" + 
      "AND TRUNC(v.data_verbale) BETWEEN TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2100'),'DD/MM/YYYY') \r\n" + 
      "  AND NVL(m.numero_trasferta,0) = NVL(:numeroTrasferta,NVL(m.numero_trasferta,0)) \r\n" + 
      "  AND m.id_ispettore_assegnato = NVL(:idIspettore,m.id_ispettore_assegnato)  AND UPPER(a.nome) = UPPER(NVL(null,a.nome))  AND UPPER(a.cognome) = UPPER(NVL(null,a.cognome))  AND m.id_missione = v.id_missione (+) \r\n"+
      "AND v.id_verbale IS NOT NULL\r\n" +
      "AND NVL(v.id_stato_verbale,0) != 2\r\n";
    

//private static final String FILTER_VERBALE_ASSENTE =  " AND NOT EXISTS (SELECT 1 FROM iuf_t_verbale v WHERE v.id_missione = m.id_missione) \r\n";
//private static final String FILTER_VERBALE_BOZZA = " AND v.id_stato_verbale = 1 \r\n";
//private static final String FILTER_VERBALE_CONSOLIDATO = " AND v.id_stato_verbale = 2 \r\n";
//private static final String FILTER_VERBALE_ARCHIVIATO = " AND v.id_stato_verbale = 3 \r\n";

  private LobHandler lobHandler;

  
  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public Long insertMissione(MissioneDTO missione) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertMissione]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {

      if (missione.getNumeroTrasferta() != null)
        mapParameterSource.addValue("numeroTrasferta", missione.getNumeroTrasferta());
      else
        mapParameterSource.addValue("numeroTrasferta", null, Types.NULL);
      
      mapParameterSource.addValue("dataOraInizio", missione.getDataOraInizioMissione());
      mapParameterSource.addValue("dataOraFine", missione.getDataOraFineMissione());
      mapParameterSource.addValue("idIspettoreAssegnato", missione.getIdIspettoreAssegnato());      
      mapParameterSource.addValue("pdfTrasferta", missione.getPdfTrasferta(), Types.BLOB);

      if (missione.getPdfTrasferta() != null) {
          byte[] bytes = missione.getPdfTrasferta().getBytes();
          mapParameterSource.addValue("pdfTrasferta", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), OracleTypes.BLOB);
      } else
          mapParameterSource.addValue("pdfTrasferta", null, Types.BLOB);

      mapParameterSource.addValue("nomeFile", missione.getNomeFile(), Types.VARCHAR);
      mapParameterSource.addValue("stato", missione.getStato(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", missione.getExtIdUtenteAggiornamento());
      logger.debug(INSERT);
      int result = namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_missione"});
      Long idMissione = holder.getKey().longValue();
      missione.setIdMissione(idMissione);
      inserisciIspettoriAggiunti(missione);
      return idMissione;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {
              //new LogParameter("idProcedimento", idProcedimento),
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

  public void updateMissione(MissioneDTO missione) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateMissione]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      MissioneDTO old = this.findById(missione.getIdMissione());
      
      if (missione.getNumeroTrasferta() != null)
        mapParameterSource.addValue("numeroTrasferta", missione.getNumeroTrasferta());
      else
        mapParameterSource.addValue("numeroTrasferta", null, Types.NULL);
      
      mapParameterSource.addValue("dataOraInizio", missione.getDataOraInizioMissione());
      mapParameterSource.addValue("dataOraFine", missione.getDataOraFineMissione());
      mapParameterSource.addValue("idIspettoreAssegnato", missione.getIdIspettoreAssegnato(), Types.INTEGER);      
   
      if (missione.getPdfTrasferta() != null && missione.getPdfTrasferta().getSize() > 0) {
          byte[] bytes = missione.getPdfTrasferta().getBytes();
          //mapParameterSource.addValue("pdfTrasferta", new SqlLobValue(bytes, new OracleLobHandler()), OracleTypes.BLOB);
          mapParameterSource.addValue("pdfTrasferta", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), Types.BLOB);
          //mapParameterSource.addValue("pdfTrasferta", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), OracleTypes.BLOB);
          mapParameterSource.addValue("nomeFile", missione.getNomeFile(), Types.VARCHAR);
      }
      else {
        if (StringUtils.isBlank(missione.getNomeFile())) {
          mapParameterSource.addValue("pdfTrasferta", null, Types.BLOB);
          mapParameterSource.addValue("nomeFile", null, Types.NULL);
        }
        else
        if (StringUtils.isNotBlank(old.getNomeFile()) && missione.getNomeFile().equals(old.getNomeFile()) && old.getPdfTrasferta() != null) {
          byte[] bytes = old.getPdfTrasferta().getBytes();
          mapParameterSource.addValue("pdfTrasferta", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), Types.BLOB);
          //mapParameterSource.addValue("pdfTrasferta", new SqlLobValue(bytes));
          mapParameterSource.addValue("nomeFile", missione.getNomeFile(), Types.VARCHAR);
        }
        else {
          mapParameterSource.addValue("pdfTrasferta", null, Types.BLOB);
          mapParameterSource.addValue("nomeFile", null, Types.NULL);
        }
      }
    
      if (StringUtils.isBlank(missione.getStato())) {
        missione.setStato(old.getStato());
      }
      mapParameterSource.addValue("stato", missione.getStato(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", missione.getExtIdUtenteAggiornamento());
      mapParameterSource.addValue("idMissione", missione.getIdMissione(), Types.INTEGER);
      logger.debug(UPDATE);
      int result = namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      inserisciIspettoriAggiunti(missione);
      
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {
              //new LogParameter("idProcedimento", idProcedimento),
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

  private void inserisciIspettoriAggiunti(MissioneDTO missione) throws InternalUnexpectedException {
    List<AnagraficaDTO> ispettoriPresenti = this.getIspettoriAggiunti(missione.getIdMissione());
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    if (ispettoriPresenti != null && ispettoriPresenti.size() > 0) {
      for (int j=0; j<ispettoriPresenti.size(); j++) {
        boolean trovato = false;
        if (missione.getIspettoriSelezionati() != null && missione.getIspettoriSelezionati().size() > 0) {
          for (int i=0; i<missione.getIspettoriSelezionati().size(); i++) {
            if (ispettoriPresenti.get(j).getIdAnagrafica().intValue() == missione.getIspettoriSelezionati().get(i).intValue()) {
              trovato = true;
              break;
            }
          }
        }
        if (trovato == false) {
          // delete
          mapParameterSource = new MapSqlParameterSource();
          mapParameterSource.addValue("idMissione", missione.getIdMissione());
          mapParameterSource.addValue("idAnagrafica", ispettoriPresenti.get(j).getIdAnagrafica());
          logger.debug(DELETE_ISPETTORE_AGGIUNTO);
          int result = namedParameterJdbcTemplate.update(DELETE_ISPETTORE_AGGIUNTO, mapParameterSource);
        }
      }
    }

    if (missione.getIspettoriSelezionati() != null) {
      for (int i=0; i<missione.getIspettoriSelezionati().size(); i++) {
        int idAnagrafica = missione.getIspettoriSelezionati().get(i);
        mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue("idMissione", missione.getIdMissione());
        mapParameterSource.addValue("idAnagrafica", idAnagrafica);
        mapParameterSource.addValue("ext_id_utente_aggiornamento", missione.getExtIdUtenteAggiornamento());
        try {
          // insert
          logger.debug(INSERT_ISPETTORI_AGGIUNTI);
          int result = namedParameterJdbcTemplate.update(INSERT_ISPETTORI_AGGIUNTI, mapParameterSource);
        } catch (DuplicateKeyException dke) {
          // ispettore già presente per la missione
        }
      }
    }

  }
  
  public List<MissioneDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL);
      return queryForList(SELECT_ALL, mapParameterSource, MissioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<MissioneDTO> findByDateAndIspettore(String dataOraInizio, Integer idIspettore, Long idMissione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByDateAndIspettore";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    final String SELECT = "SELECT m2.* \r\n" + 
                            "FROM (SELECT m.id_missione,m.id_ispettore_assegnato id_ispettore\r\n" + 
                            "       FROM iuf_t_missione m\r\n" + 
                            "      UNION\r\n" + 
                            "      SELECT ia.id_missione,ia.id_anagrafica\r\n" + 
                            "       FROM iuf_r_ispettore_aggiunto ia) mis,\r\n" + 
                            "      iuf_t_missione m2\r\n" + 
                            "WHERE m2.id_missione = mis.id_missione \r\n" + 
                            "AND mis.id_ispettore = :idIspettore \r\n" + 
                            "AND TRUNC(to_date(:dataOraInizio,'DD/MM/YYYY HH24:MI:SS')) = TRUNC(m2.data_ora_inizio_missione) \r\n" +
                            "AND m2.id_missione != NVL(:idMissione,0)";
    try
    {
      mapParameterSource.addValue("idIspettore", idIspettore);
      mapParameterSource.addValue("dataOraInizio", dataOraInizio, Types.VARCHAR);
      mapParameterSource.addValue("idMissione", idMissione);
      logger.debug(SELECT);
      return queryForList(SELECT, mapParameterSource, MissioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
  
  public List<AnagraficaDTO> getIspettoriAggiunti(Long idMissione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getIspettoriAggiunti";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    try
    {
      logger.debug(SELECT_ISPETTORI);
      List<AnagraficaDTO> list = queryForList(SELECT_ISPETTORI, mapParameterSource, AnagraficaDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ISPETTORI, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public MissioneDTO findById(Long idMissione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      MissioneDTO missione = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, mapParameterSource, getRowMapper());
      return missione;
    } catch (EmptyResultDataAccessException e) {
        logger.debug("ID Missione non trovato");
        return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_ID, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<MissioneDTO> findByFilter(MissioneRequest mr, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    if (mr.getIdMissione() != null) {
      MissioneDTO m = this.findById(mr.getIdMissione());
      List<MissioneDTO> list = new ArrayList<MissioneDTO>();
      list.add(m);
      return list;
    }
    
    String select = String.format(SELECT_BY_FILTER, "");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    if (mr.getAnno() != null) {
      select += " AND TO_CHAR(m.data_ora_inizio_missione,'YYYY') = :anno \r\n";
      mapParameterSource.addValue("anno", mr.getAnno().toString());
    }
    mapParameterSource.addValue("dallaData", (mr.getDallaData()!=null)?mr.getDallaData().replaceAll("-", "/"):"");
    mapParameterSource.addValue("allaData", (mr.getAllaData()!=null)?mr.getAllaData().replaceAll("-", "/"):"");
    if (mr.getNumeroTrasferta() != null)
      mapParameterSource.addValue("numeroTrasferta", mr.getNumeroTrasferta());
    else
      mapParameterSource.addValue("numeroTrasferta", null, Types.NULL);
    
    if (mr.getIdIspettore()!=null && mr.getIdIspettore() > 0)
      mapParameterSource.addValue("idIspettore", mr.getIdIspettore());
    else
      mapParameterSource.addValue("idIspettore", null, Types.NULL);

    if (mr.getNomeIspettore() != null && mr.getNomeIspettore().trim().length() > 0)
      mapParameterSource.addValue("nomeIspettore", mr.getNomeIspettore());
    else
      mapParameterSource.addValue("nomeIspettore", null, Types.NULL);

    if (mr.getCognomeIspettore() != null && mr.getCognomeIspettore().trim().length() > 0)
      mapParameterSource.addValue("cognomeIspettore", mr.getCognomeIspettore());
    else
      mapParameterSource.addValue("cognomeIspettore", null, Types.NULL);

    if (mr.getIdSpecieVegetale()!=null && mr.getIdSpecieVegetale()>0) {
      select += FILTER_SPECIE_VEGETALE;
      mapParameterSource.addValue("idSpecieVegetale", mr.getIdSpecieVegetale());
    }

    if (mr.getIdOrganismoNocivo()!=null && mr.getIdOrganismoNocivo()>0) {
      select += FILTER_ORGANISMO_NOCIVO;
      mapParameterSource.addValue("idOrganismoNocivo", mr.getIdOrganismoNocivo());
    }
    
    if (mr.getIstatComune() != null && mr.getIstatComune().trim().length() > 0 || StringUtils.isNotBlank(mr.getDescComune())) {
      select += FILTER_COMUNE;
      if (mr.getIstatComune() != null && mr.getIstatComune().trim().length() > 0) {
        mapParameterSource.addValue("istatComune", mr.getIstatComune());
      }
      else
        mapParameterSource.addValue("istatComune", null, Types.NULL);
      
      if (StringUtils.isNotBlank(mr.getDescComune())) {
        mapParameterSource.addValue("descComune", mr.getDescComune());
      }
      else
        mapParameterSource.addValue("descComune", null, Types.NULL);
    }      

    if (mr.getVisual() != null && mr.getVisual()) {
      select += FILTER_VISUAL;
    }

    if (mr.getCampionamento() != null && mr.getCampionamento()) {
      select += FILTER_CAMPIONAMENTO;
    }
    
    if (mr.getTrappolaggio() != null && mr.getTrappolaggio()) {
      select += FILTER_TRAPPOLAGGIO;
    }
    // filtro multiplo per tipo area
    if (mr.getTipoArea() != null && mr.getTipoArea().size() > 0) {
      select += " AND EXISTS (SELECT 1 FROM iuf_t_rilevazione r \r\n" + 
          "                     WHERE r.id_missione = m.id_missione\r\n" + 
          "                       AND r.id_tipo_area IN (";
      for (int i=0; i<mr.getTipoArea().size(); i++) {
        if (i > 0)
          select += ",";
        select += mr.getTipoArea().get(i);
      }
      select += "))\r\n";
    }
    // filtro multiplo per ispettore assegnato - deve cercare anche per ispettore aggiunto (Richiesta di Vilma del 18/03/21)
    if (mr.getIspettoreAssegnato() != null && mr.getIspettoreAssegnato().size() > 0) {
      String ispettori = "";
      for (int i=0; i<mr.getIspettoreAssegnato().size(); i++) {
        if (i > 0)
          ispettori += ",";
        ispettori += mr.getIspettoreAssegnato().get(i);
      }

      select += " AND (m.id_ispettore_assegnato IN (" + ispettori + ")" + 
                    " OR EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia \r\n" + 
          "                     WHERE ia.id_missione = m.id_missione\r\n" + 
          "                       AND ia.id_anagrafica IN (" + ispettori + "))) \r\n";
    }
    // filtro multiplo per specie vegetale
    if (mr.getSpecieVegetale() != null && mr.getSpecieVegetale().size() > 0) {
      String elements = "";
      for (int i=0; i<mr.getSpecieVegetale().size(); i++) {
        if (i > 0)
          elements += ",";
        elements += mr.getSpecieVegetale().get(i);
      }
      String condition = String.format(FILTER_SPECIE_VEGETALE_MULTIPLE, elements);
      select += condition;
    }
    // filtro multiplo per organismo nocivo
    if (mr.getOrganismoNocivo() != null && mr.getOrganismoNocivo().size() > 0) {
      String elements = "";
      for (int i=0; i<mr.getOrganismoNocivo().size(); i++) {
        if (i > 0)
          elements += ",";
        elements += mr.getOrganismoNocivo().get(i);
      }
      String condition = String.format(FILTER_ORGANISMO_NOCIVO_MULTIPLE, elements);
      select += condition;
    }
    // filtro per obiettivo missione "nessuno"
    /*
    if (mr.getObiettivoNessuno() != null && mr.getObiettivoNessuno().equalsIgnoreCase("S")) {
      select += FILTER_OBIETTIVO_NESSUNO;
    }
    */
    // filtro per obiettivo missione "indagine ufficiale"
    if (mr.getObiettivoIndagineUfficiale() != null && mr.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
      select += FILTER_OBIETTIVO_INDAGINE_UFFICIALE;
    }
    // filtro per obiettivo missione "emergenza"
    if (mr.getObiettivoEmergenza() != null && mr.getObiettivoEmergenza().equalsIgnoreCase("S")) {
      select += FILTER_OBIETTIVO_EMERGENZA;
    }
    // Filtro per Anagrafica (ispettore loggato)
    if (idAnagrafica != null && idAnagrafica > 0) {
      select += " AND (m.id_ispettore_assegnato = :idAnagrafica " +
                     "OR EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia " +
                                 "WHERE ia.id_missione = m.id_missione AND ia.id_anagrafica = :idAnagrafica))";
      mapParameterSource.addValue("idAnagrafica", idAnagrafica);
    }
    // Filtro per Ente (ente ispettore loggato)
    if (idEnte != null && idEnte > 0) {
      select += " AND a.id_ente = :idEnte";
      mapParameterSource.addValue("idEnte", idEnte);
    }
    select += " ORDER BY m.data_ora_inizio_missione DESC,m.data_ora_fine_missione DESC";
    try
    {
      logger.debug(select);
      List<MissioneDTO> missioni = queryForList(select, mapParameterSource, MissioneDTO.class);
      return missioni;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, select, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<Missione> getMissioni(MissioneRequest mr) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getMissioni";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String select = SELECT_MISSIONI_API;

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", mr.getIdMissione());
    mapParameterSource.addValue("dallaData", (mr.getDallaData()!=null)? mr.getDallaData().replaceAll("-","/"):"");
    mapParameterSource.addValue("allaData", (mr.getAllaData()!=null)? mr.getAllaData().replaceAll("-","/"):"");
    if (mr.getNumeroTrasferta() != null)
      mapParameterSource.addValue("numeroTrasferta", mr.getNumeroTrasferta());
    else
      mapParameterSource.addValue("numeroTrasferta", null, Types.NULL);
    
    if (mr.getIdIspettore()!=null && mr.getIdIspettore()>0)
      mapParameterSource.addValue("idIspettore", mr.getIdIspettore());
    else
      mapParameterSource.addValue("idIspettore", null, Types.NULL);

    if (mr.getNomeIspettore() != null && mr.getNomeIspettore().trim().length() > 0)
      mapParameterSource.addValue("nomeIspettore", mr.getNomeIspettore());
    else
      mapParameterSource.addValue("nomeIspettore", null, Types.NULL);

    if (mr.getCognomeIspettore() != null && mr.getCognomeIspettore().trim().length() > 0)
      mapParameterSource.addValue("cognomeIspettore", mr.getCognomeIspettore());
    else
      mapParameterSource.addValue("cognomeIspettore", null, Types.NULL);
    /*
    if (mr.getCfIspettore() != null && mr.getCfIspettore().trim().length() > 0)
      mapParameterSource.addValue("cfIspettore", mr.getCfIspettore());
    else
      mapParameterSource.addValue("cfIspettore", null, Types.NULL);
     */
    mapParameterSource.addValue("cfIspettore", mr.getCfIspettore());
    
    if (mr.getIdSpecieVegetale()!=null && mr.getIdSpecieVegetale()>0) {
      select += FILTER_SPECIE_VEGETALE;
      mapParameterSource.addValue("idSpecieVegetale", mr.getIdSpecieVegetale());
    }

    if (mr.getIdOrganismoNocivo()!=null && mr.getIdOrganismoNocivo()>0) {
      select += FILTER_ORGANISMO_NOCIVO;
      mapParameterSource.addValue("idOrganismoNocivo", mr.getIdOrganismoNocivo());
    }
    
    if (mr.getVisual() != null && mr.getVisual()) {
      select += FILTER_VISUAL;
    }
    
    if (mr.getCampionamento() != null && mr.getCampionamento()) {
      select += FILTER_CAMPIONAMENTO;
    }
    
    if (mr.getTrappolaggio() != null && mr.getTrappolaggio()) {
      select += FILTER_TRAPPOLAGGIO;
    }
    select += " ORDER by m.id_missione";
    try
    {
      logger.debug(select);
      List<Missione> missioni = queryForList(select, mapParameterSource, Missione.class);
      if (missioni == null)
        missioni = new ArrayList<Missione>();
      return missioni;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, select, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public LobHandler getLobHandler() {
    return lobHandler;
    }

    public void setLobHandler(LobHandler lobHandler) {
    this.lobHandler = lobHandler;
    }

    
  private RowMapper<MissioneDTO> getRowMapper() {
    return new RowMapper<MissioneDTO>() {
      
      @Override
      public MissioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        LobHandler lobHandler;
        MissioneDTO missione = new MissioneDTO();
        missione.setIdMissione(rs.getLong("id_missione"));
        missione.setDataUltimoAggiornamento(rs.getTimestamp("data_ultimo_aggiornamento"));
        missione.setDataOraInizioMissione(rs.getTimestamp("data_ora_inizio_missione"));
        missione.setDataOraFineMissione(rs.getTimestamp("data_ora_fine_missione"));
        missione.setIdIspettoreAssegnato(rs.getLong("id_ispettore_assegnato"));
        missione.setNomeFile(rs.getString("nome_file"));
        missione.setNumeroTrasferta(rs.getLong("numero_trasferta"));
        if (rs.wasNull()) {
          missione.setNumeroTrasferta(null);
        }
        //missione.setOraFine(rs.getString("comune_nascita"));
        //missione.setOraInizio(rs.getString("data_nascita"));
        lobHandler = new DefaultLobHandler();
        /*lobHandler = new OracleLobHandler();*/
        byte[] filePdf = lobHandler.getBlobAsBytes(rs, "pdf_trasferta");
        missione.setPdfTrasferta(filePdf);
        //Blob pdf = rs.getBlob("pdf_trasferta");
        //missione.setPdfTrasferta(pdf.getBytes(1L, (int) pdf.length()));
        missione.setStato(rs.getString("stato"));
        missione.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
        return missione;
      }
    };
  } 

  public byte[] getPdfTrasferta(Long idMissione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getPdfTrasferta";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    String SELECT_PDF_TRASFERTA = "SELECT t.pdf_trasferta FROM iuf_t_missione t WHERE t.id_missione = :idMissione";
    try
    {
      logger.debug(SELECT_BY_ID);
      byte[] pdf = namedParameterJdbcTemplate.queryForObject(SELECT_PDF_TRASFERTA, mapParameterSource, new BlobRowMapper());
      
      return pdf;
      
    } catch (EmptyResultDataAccessException e) {
        logger.debug("ID Missione non trovato");
        return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_PDF_TRASFERTA, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
  
  public class BlobRowMapper implements RowMapper<byte[]> {

    public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
         Blob column = rs.getBlob("PDF_TRASFERTA");
         return column.getBytes(1L, (int) column.length());
    }
  }
  
  
  public void remove(Integer idMissione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String DELETE = null;
    
    try
    {
      // DELETE ISPETTORI AGGIUNTI
      DELETE = "DELETE FROM iuf_r_ispettore_aggiunto WHERE id_missione = :idMissione";
      mapParameterSource.addValue("idMissione", idMissione);
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE VERBALE
      DELETE = "DELETE FROM iuf_t_verbale WHERE id_missione = :idMissione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE SPECIE/ON PRECARICATE
      DELETE = "DELETE FROM iuf_t_specie_on_precaricate WHERE id_missione = :idMissione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE_R_CAMPIONAMENTO_SPEC_ON
      DELETE = "DELETE FROM iuf_r_campionamento_spec_on r \r\n" + 
          "WHERE EXISTS (SELECT 1 FROM iuf_t_campionamento c,iuf_t_rilevazione ril \r\n" + 
          "              WHERE c.id_rilevazione = ril.id_rilevazione\r\n" + 
          "                AND c.id_campionamento = r.id_campionamento\r\n" + 
          "                AND ril.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_campione_foto
      DELETE = "DELETE FROM iuf_r_campione_foto f \r\n" +
                "WHERE EXISTS (SELECT 1 FROM iuf_t_campionamento c,iuf_t_rilevazione ril \r\n" + 
                "               WHERE c.id_rilevazione = ril.id_rilevazione \r\n" +
                "                 AND c.id_campionamento = f.id_campionamento \r\n" +
                "                 AND ril.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_esito_campione
      DELETE = "DELETE FROM iuf_t_esito_campione e \r\n" +
                "WHERE EXISTS (SELECT 1 FROM iuf_t_campionamento c,iuf_t_rilevazione ril \r\n" + 
                "               WHERE c.id_rilevazione = ril.id_rilevazione \r\n" +
                "                 AND c.id_campionamento = e.id_campionamento \r\n" +
                "                 AND ril.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE CAMPIONAMENTO
      DELETE = "DELETE FROM iuf_t_campionamento t \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_rilevazione r \r\n" +
          "              WHERE t.id_rilevazione = r.id_rilevazione\r\n" +
          "                AND r.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_trappolaggio_foto
      DELETE = "DELETE FROM iuf_r_trappolaggio_foto f \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_rilevazione r, iuf_t_trappolaggio t\r\n" +
          "              WHERE t.id_rilevazione = r.id_rilevazione\r\n" +
          "                AND t.id_trappolaggio = f.id_trappolaggio\r\n" +
          "                AND r.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_trappolaggio
      DELETE = "DELETE FROM iuf_t_trappolaggio t \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_rilevazione r\r\n" +
          "              WHERE r.id_rilevazione = t.id_rilevazione\r\n" +
          "                AND r.id_missione = :idMissione)";
      logger.debug(DELETE);
      int recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug("trappolaggi eliminati: " + recs);
      // DELETE iuf_t_trappola
      DELETE = "DELETE FROM iuf_t_trappola t\r\n" + 
                "WHERE NOT EXISTS (SELECT 1 FROM iuf_t_trappolaggio tr, iuf_t_rilevazione r\r\n" + 
                "                   WHERE tr.id_rilevazione = r.id_rilevazione\r\n" + 
                "                     AND tr.id_trappola = t.id_trappola)";
      logger.debug(DELETE);
      recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug("trappole eliminate: " + recs);
      // DELETE iuf_r_isp_visiva_spec_on
      DELETE = "DELETE FROM iuf_r_isp_visiva_spec_on i \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v \r\n" +
          "              WHERE r.id_rilevazione = v.id_rilevazione\r\n" +
          "                AND v.id_ispezione = i.id_ispezione\r\n" +
          "                AND r.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_isp_visiva_foto
      DELETE = "DELETE FROM iuf_r_isp_visiva_foto i \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v \r\n" +
          "              WHERE r.id_rilevazione = v.id_rilevazione\r\n" +
          "                AND v.id_ispezione = i.id_ispezione_visiva\r\n" +
          "                AND r.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_isp_visiva_foto
      DELETE = "DELETE FROM iuf_t_isp_visiva_pianta i \r\n" + 
          "WHERE EXISTS (SELECT 1 FROM iuf_t_rilevazione r, iuf_t_ispezione_visiva v \r\n" +
          "              WHERE r.id_rilevazione = v.id_rilevazione\r\n" +
          "                AND v.id_ispezione = i.id_ispezione_visiva\r\n" +
          "                AND r.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_ispezione_visiva
      DELETE = "DELETE FROM iuf_t_ispezione_visiva i \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_rilevazione r\r\n" +
          "              WHERE r.id_rilevazione = i.id_rilevazione\r\n" +
          "                AND r.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_percorso_rilevazioni
      DELETE = "DELETE FROM iuf_t_percorso_rilevazioni pr \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_rilevazione r\r\n" +
          "              WHERE r.id_rilevazione = pr.id_rilevazione\r\n" +
          "                AND r.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_check_list_sop
      DELETE = "DELETE FROM iuf_r_check_list_sop cls \r\n" +
          "WHERE EXISTS (SELECT 1 FROM iuf_t_sopralluogo_vivaio sv\r\n" +
          "              WHERE sv.id_sopralluogo = cls.id_sopralluogo_vivaio\r\n" +
          "                AND sv.id_missione = :idMissione)";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_sopralluogo_vivaio
      DELETE = "DELETE FROM iuf_t_sopralluogo_vivaio sv WHERE sv.id_missione = :idMissione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_rilevazione
      DELETE = "DELETE FROM iuf_t_rilevazione WHERE id_missione = :idMissione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_missione
      DELETE = "DELETE FROM iuf_t_missione WHERE id_missione = :idMissione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, DELETE, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
  
  public List<MissioneDTO> findMissioneForVerbali(MissioneRequest mr, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findMissioneForVerbali";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    if (mr.getIdMissione() != null) {
      MissioneDTO m = this.findById(mr.getIdMissione());
      List<MissioneDTO> list = new ArrayList<MissioneDTO>();
      list.add(m);
      return list;
    }
    
    String select = String.format(SELECT_MISSIONE_BY_FILTER_FOR_VERBALI, "");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    if (mr.getAnno() != null) {
      select += " AND TO_CHAR(m.data_ora_inizio_missione,'YYYY') = :anno \r\n";
      mapParameterSource.addValue("anno", mr.getAnno().toString());
    }
    mapParameterSource.addValue("dallaData", (mr.getDallaData()!=null)?mr.getDallaData().replaceAll("-", "/"):"");
    mapParameterSource.addValue("allaData", (mr.getAllaData()!=null)?mr.getAllaData().replaceAll("-", "/"):"");
    if (mr.getNumeroTrasferta() != null)
      mapParameterSource.addValue("numeroTrasferta", mr.getNumeroTrasferta());
    else
      mapParameterSource.addValue("numeroTrasferta", null, Types.NULL);
    
    if (mr.getIdIspettore()!=null && mr.getIdIspettore()>0)
      mapParameterSource.addValue("idIspettore", mr.getIdIspettore());
    else
      mapParameterSource.addValue("idIspettore", null, Types.NULL);

    if (mr.getNomeIspettore() != null && mr.getNomeIspettore().trim().length() > 0)
      mapParameterSource.addValue("nomeIspettore", mr.getNomeIspettore());
    else
      mapParameterSource.addValue("nomeIspettore", null, Types.NULL);

    if (mr.getCognomeIspettore() != null && mr.getCognomeIspettore().trim().length() > 0)
      mapParameterSource.addValue("cognomeIspettore", mr.getCognomeIspettore());
    else
      mapParameterSource.addValue("cognomeIspettore", null, Types.NULL);

    if (mr.getIdSpecieVegetale()!=null && mr.getIdSpecieVegetale()>0) {
      select += FILTER_SPECIE_VEGETALE;
      mapParameterSource.addValue("idSpecieVegetale", mr.getIdSpecieVegetale());
    }

    if (mr.getIdOrganismoNocivo()!=null && mr.getIdOrganismoNocivo()>0) {
      select += FILTER_ORGANISMO_NOCIVO;
      mapParameterSource.addValue("idOrganismoNocivo", mr.getIdOrganismoNocivo());
    }

    if (mr.getIstatComune() != null && mr.getIstatComune().trim().length() > 0 || StringUtils.isNotBlank(mr.getDescComune())) {
        select += FILTER_COMUNE;
        if (mr.getIstatComune() != null && mr.getIstatComune().trim().length() > 0) {
          mapParameterSource.addValue("istatComune", mr.getIstatComune());
        }
        else
          mapParameterSource.addValue("istatComune", null, Types.NULL);
        
        if (StringUtils.isNotBlank(mr.getDescComune())) {
          mapParameterSource.addValue("descComune", mr.getDescComune());
        }
        else
          mapParameterSource.addValue("descComune", null, Types.NULL);
    }     
    
    if (mr.getVisual() != null && mr.getVisual()) {
      select += FILTER_VISUAL;
    }

    if (mr.getCampionamento() != null && mr.getCampionamento()) {
      select += FILTER_CAMPIONAMENTO;
    }
    
    if (mr.getTrappolaggio() != null && mr.getTrappolaggio()) {
      select += FILTER_TRAPPOLAGGIO;
    }
    // filtro multiplo per tipo area
    if (mr.getTipoArea() != null && mr.getTipoArea().size() > 0) {
      select += " AND EXISTS (SELECT 1 FROM iuf_t_rilevazione r \r\n" + 
          "                     WHERE r.id_missione = m.id_missione\r\n" + 
          "                       AND r.id_tipo_area IN (";
      for (int i=0; i<mr.getTipoArea().size(); i++) {
        if (i > 0)
          select += ",";
        select += mr.getTipoArea().get(i);
      }
      select += "))\r\n";
    }
    // filtro multiplo per ispettore assegnato
    if (mr.getIspettoreAssegnato() != null && mr.getIspettoreAssegnato().size() > 0) {
      select += " AND m.id_ispettore_assegnato IN (";
      for (int i=0; i<mr.getIspettoreAssegnato().size(); i++) {
        if (i > 0)
          select += ",";
        select += mr.getIspettoreAssegnato().get(i);
      }
      select += ")\r\n";
    }
    // filtro multiplo per ispettori secondari
    if (mr.getIspettoriSecondari() != null && mr.getIspettoriSecondari().size() > 0) {
      select += " AND EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia \r\n" + 
          "                     WHERE ia.id_missione = m.id_missione\r\n" + 
          "                       AND ia.id_anagrafica IN (";
      for (int i=0; i<mr.getIspettoriSecondari().size(); i++) {
        if (i > 0)
          select += ",";
        select += mr.getIspettoriSecondari().get(i);
      }
      select += "))\r\n";
    }
    // filtro multiplo per specie vegetale
    if (mr.getSpecieVegetale() != null && mr.getSpecieVegetale().size() > 0) {
      String elements = "";
      for (int i=0; i<mr.getSpecieVegetale().size(); i++) {
        if (i > 0)
          elements += ",";
        elements += mr.getSpecieVegetale().get(i);
      }
      String condition = String.format(FILTER_SPECIE_VEGETALE_MULTIPLE, elements);
      select += condition;
    }
    // filtro multiplo per organismo nocivo
    if (mr.getOrganismoNocivo() != null && mr.getOrganismoNocivo().size() > 0) {
      String elements = "";
      for (int i=0; i<mr.getOrganismoNocivo().size(); i++) {
        if (i > 0)
          elements += ",";
        elements += mr.getOrganismoNocivo().get(i);
      }
      String condition = String.format(FILTER_ORGANISMO_NOCIVO_MULTIPLE, elements);
      select += condition;
    }
    // filtro per obiettivo missione "nessuno"
    if (mr.getObiettivoNessuno() != null && mr.getObiettivoNessuno().equalsIgnoreCase("S")) {
      select += FILTER_OBIETTIVO_NESSUNO;
    }
    // filtro per obiettivo missione "indagine ufficiale"
    if (mr.getObiettivoIndagineUfficiale() != null && mr.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
      select += FILTER_OBIETTIVO_INDAGINE_UFFICIALE;
    }
    
    //filtro per stato verbale 
    if ((mr.getVerbaleAssente() != null && mr.getVerbaleAssente().equalsIgnoreCase("S")) 
      || (mr.getVerbaleBozza() != null && mr.getVerbaleBozza().equalsIgnoreCase("S")) 
      || (mr.getVerbaleConsolidato() != null && mr.getVerbaleConsolidato().equalsIgnoreCase("S")) 
      || (mr.getVerbaleArchiviato() != null && mr.getVerbaleArchiviato().equalsIgnoreCase("S"))) {
    	
      select += " AND (";
      
      //filtro stato verbale assente
      if(mr.getVerbaleAssente() != null && mr.getVerbaleAssente().equalsIgnoreCase("S")) {
    	  select += "NOT EXISTS (SELECT 1 FROM iuf_t_verbale v WHERE v.id_missione = m.id_missione) ";
      }
      
      //filtro per stato verbale bozza
      if ((mr.getVerbaleAssente() != null && mr.getVerbaleAssente().equalsIgnoreCase("S")) 
    		  && (mr.getVerbaleBozza() != null && mr.getVerbaleBozza().equalsIgnoreCase("S"))) {
        select += " OR v.id_stato_verbale = " + IuffiConstants.IUFFI.VERBALE_BOZZA;
      }
      else if (mr.getVerbaleBozza() != null && mr.getVerbaleBozza().equalsIgnoreCase("S"))
    	  select += "v.id_stato_verbale = " + IuffiConstants.IUFFI.VERBALE_BOZZA;
      
      //filtro per stato verbale consolidato
      if (((mr.getVerbaleAssente() != null && mr.getVerbaleAssente().equalsIgnoreCase("S")) ||
          ((mr.getVerbaleBozza() != null && mr.getVerbaleBozza().equalsIgnoreCase("S")))) && 
        (mr.getVerbaleConsolidato() != null && mr.getVerbaleConsolidato().equalsIgnoreCase("S"))) {
        select += " OR v.id_stato_verbale = " + IuffiConstants.IUFFI.VERBALE_CONSOLIDATO;
      }
      else if(mr.getVerbaleConsolidato() != null && mr.getVerbaleConsolidato().equalsIgnoreCase("S"))
    	  select += "v.id_stato_verbale = " + IuffiConstants.IUFFI.VERBALE_CONSOLIDATO; 
      
      //filtro per stato verbale archiviato
      if (((mr.getVerbaleAssente() != null && mr.getVerbaleAssente().equalsIgnoreCase("S")) ||
    	   (mr.getVerbaleBozza() != null && mr.getVerbaleBozza().equalsIgnoreCase("S")) || 
    	   ((mr.getVerbaleConsolidato() != null && mr.getVerbaleConsolidato().equalsIgnoreCase("S")))) && 
    	   (mr.getVerbaleArchiviato() != null && mr.getVerbaleArchiviato().equalsIgnoreCase("S"))) {
    	        select += " OR v.id_stato_verbale = " + IuffiConstants.IUFFI.VERBALE_ARCHIVIATO;
      }
      else if (mr.getVerbaleArchiviato() != null && mr.getVerbaleArchiviato().equalsIgnoreCase("S")) 
        select += "v.id_stato_verbale = " + IuffiConstants.IUFFI.VERBALE_ARCHIVIATO;     
    	
      select += ")";
    }
    
//    //filtro per stato verbale bozza
//    if (mr.getVerbaleBozza() != null && mr.getVerbaleBozza().equalsIgnoreCase("S")) {
//      select += FILTER_VERBALE_BOZZA;
//    }
//    
//    //filtro per stato verbale consolidato
//    if (mr.getVerbaleConsolidato() != null && mr.getVerbaleConsolidato().equalsIgnoreCase("S")) {
//      select += FILTER_VERBALE_CONSOLIDATO;
//    }
//    
//    //filtro per stato verbale archiviato
//    if (mr.getVerbaleArchiviato() != null && mr.getVerbaleArchiviato().equalsIgnoreCase("S")) {
//      select += FILTER_VERBALE_ARCHIVIATO;
//    }
    
    if (StringUtils.isNotBlank(mr.getNumVerbale())) {
      select += " AND UPPER(v.num_verbale) LIKE UPPER(:numVerbale)||'%' \r\n"; 
      mapParameterSource.addValue("numVerbale", mr.getNumVerbale());
    }
    
    // Filtro per Anagrafica (ispettore loggato)
    if (idAnagrafica != null && idAnagrafica > 0) {
      select += " AND (m.id_ispettore_assegnato = :idAnagrafica " +
                     "OR EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia " +
                                 "WHERE ia.id_missione = m.id_missione AND ia.id_anagrafica = :idAnagrafica))";
      mapParameterSource.addValue("idAnagrafica", idAnagrafica);
    }
    // Filtro per Ente (ente ispettore loggato)
    if (idEnte != null && idEnte > 0) {
      select += " AND a.id_ente = :idEnte";
      mapParameterSource.addValue("idEnte", idEnte);
    }
    
    select += " ORDER BY m.data_ora_inizio_missione DESC,m.data_ora_fine_missione DESC";
    try
    {
      logger.debug(select);
      List<MissioneDTO> missioni = queryForList(select, mapParameterSource, MissioneDTO.class);
      return missioni;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, select, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
  
  
  public List<VerbaleDTO> findVerbaliByMissione(MissioneRequest mr) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

//    if (mr.getIdMissione() != null) {
//      MissioneDTO m = this.findById(mr.getIdMissione());
//      List<MissioneDTO> list = new ArrayList<MissioneDTO>();
//      list.add(m);
//      return list;
//    }
    
    String select = String.format(SELECT_VERBALI_BY_MISSIONE, "");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    if (mr.getAnno() != null) {
      select += " AND TO_CHAR(v.data_verbale,'YYYY') = :anno \r\n";
      mapParameterSource.addValue("anno", mr.getAnno().toString());
    }
    mapParameterSource.addValue("dallaData", (mr.getDallaData()!=null)?mr.getDallaData().replaceAll("-", "/"):"");
    mapParameterSource.addValue("allaData", (mr.getAllaData()!=null)?mr.getAllaData().replaceAll("-", "/"):"");
    if (mr.getNumeroTrasferta() != null)
      mapParameterSource.addValue("numeroTrasferta", mr.getNumeroTrasferta());
    else
      mapParameterSource.addValue("numeroTrasferta", null, Types.NULL);
    
    if (mr.getIdIspettore()!=null && mr.getIdIspettore()>0)
      mapParameterSource.addValue("idIspettore", mr.getIdIspettore());
    else
      mapParameterSource.addValue("idIspettore", null, Types.NULL);

    if (mr.getNomeIspettore() != null && mr.getNomeIspettore().trim().length() > 0)
      mapParameterSource.addValue("nomeIspettore", mr.getNomeIspettore());
    else
      mapParameterSource.addValue("nomeIspettore", null, Types.NULL);

    if (mr.getCognomeIspettore() != null && mr.getCognomeIspettore().trim().length() > 0)
      mapParameterSource.addValue("cognomeIspettore", mr.getCognomeIspettore());
    else
      mapParameterSource.addValue("cognomeIspettore", null, Types.NULL);

    if (mr.getIdSpecieVegetale()!=null && mr.getIdSpecieVegetale()>0) {
      select += FILTER_SPECIE_VEGETALE;
      mapParameterSource.addValue("idSpecieVegetale", mr.getIdSpecieVegetale());
    }

    if (mr.getIdOrganismoNocivo()!=null && mr.getIdOrganismoNocivo()>0) {
      select += FILTER_ORGANISMO_NOCIVO;
      mapParameterSource.addValue("idOrganismoNocivo", mr.getIdOrganismoNocivo());
    }
    
    if (mr.getIstatComune() != null && mr.getIstatComune().trim().length() > 0) {
      select += FILTER_COMUNE;
      mapParameterSource.addValue("istatComune", mr.getIstatComune());
    }

    if (mr.getVisual() != null && mr.getVisual()) {
      select += FILTER_VISUAL;
    }

    if (mr.getCampionamento() != null && mr.getCampionamento()) {
      select += FILTER_CAMPIONAMENTO;
    }
    
    if (mr.getTrappolaggio() != null && mr.getTrappolaggio()) {
      select += FILTER_TRAPPOLAGGIO;
    }
    // filtro multiplo per tipo area
    if (mr.getTipoArea() != null && mr.getTipoArea().size() > 0) {
      select += " AND EXISTS (SELECT 1 FROM iuf_t_rilevazione r \r\n" + 
          "                     WHERE r.id_missione = m.id_missione\r\n" + 
          "                       AND r.id_tipo_area IN (";
      for (int i=0; i<mr.getTipoArea().size(); i++) {
        if (i > 0)
          select += ",";
        select += mr.getTipoArea().get(i);
      }
      select += "))\r\n";
    }
    // filtro multiplo per ispettore assegnato
    if (mr.getIspettoreAssegnato() != null && mr.getIspettoreAssegnato().size() > 0) {
      select += " AND m.id_ispettore_assegnato IN (";
      for (int i=0; i<mr.getIspettoreAssegnato().size(); i++) {
        if (i > 0)
          select += ",";
        select += mr.getIspettoreAssegnato().get(i);
      }
      select += ")\r\n";
    }
    // filtro multiplo per ispettori secondari
    if (mr.getIspettoriSecondari() != null && mr.getIspettoriSecondari().size() > 0) {
      select += " AND EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia \r\n" + 
          "                     WHERE ia.id_missione = m.id_missione\r\n" + 
          "                       AND ia.id_anagrafica IN (";
      for (int i=0; i<mr.getIspettoriSecondari().size(); i++) {
        if (i > 0)
          select += ",";
        select += mr.getIspettoriSecondari().get(i);
      }
      select += "))\r\n";
    }
    // filtro multiplo per specie vegetale
    if (mr.getSpecieVegetale() != null && mr.getSpecieVegetale().size() > 0) {
      String elements = "";
      for (int i=0; i<mr.getSpecieVegetale().size(); i++) {
        if (i > 0)
          elements += ",";
        elements += mr.getSpecieVegetale().get(i);
      }
      String condition = String.format(FILTER_SPECIE_VEGETALE_MULTIPLE, elements);
      select += condition;
    }
    // filtro multiplo per organismo nocivo
    if (mr.getOrganismoNocivo() != null && mr.getOrganismoNocivo().size() > 0) {
      String elements = "";
      for (int i=0; i<mr.getOrganismoNocivo().size(); i++) {
        if (i > 0)
          elements += ",";
        elements += mr.getOrganismoNocivo().get(i);
      }
      String condition = String.format(FILTER_ORGANISMO_NOCIVO_MULTIPLE, elements);
      select += condition;
    }
    // filtro per obiettivo missione "nessuno"
    if (mr.getObiettivoNessuno() != null && mr.getObiettivoNessuno().equalsIgnoreCase("S")) {
      select += FILTER_OBIETTIVO_NESSUNO;
    }
    // filtro per obiettivo missione "indagine ufficiale"
    if (mr.getObiettivoIndagineUfficiale() != null && mr.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
      select += FILTER_OBIETTIVO_INDAGINE_UFFICIALE;
    }
    
    // filtro per obiettivo missione "emergenza"
    select += " ORDER BY m.data_ora_inizio_missione DESC,m.data_ora_fine_missione DESC";
    try
    {
      logger.debug(select);
      List<VerbaleDTO> verbali = queryForList(select, mapParameterSource, VerbaleDTO.class);
      return verbali;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, select, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public int getIdStatoVerbaleByIdMissione(long idMissione)
  {
    // Modificato il 10/05/2021 (S.D.)
    // Restituisce il max stato verbale per la missione, se non esiste alcun verbale restituisce 0
    String query = "SELECT NVL(MAX(v.id_stato_verbale),0) FROM iuf_t_verbale v WHERE v.id_missione = :idMissione";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idMissione", idMissione);
    Long result = namedParameterJdbcTemplate.queryForLong(query, mapParameterSource);
    return result.intValue();
  }

}