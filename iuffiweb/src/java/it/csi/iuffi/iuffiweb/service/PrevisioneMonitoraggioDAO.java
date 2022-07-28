package it.csi.iuffi.iuffiweb.service;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.PianoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.PrevisioneMonitoraggioDTO;

public class PrevisioneMonitoraggioDAO extends BaseDAO
{
  
  private static final String THIS_CLASS = PrevisioneMonitoraggioDAO.class.getSimpleName();

  private static final String SELECT_PIANI_ALL = "SELECT t.*, CASE WHEN t.data_fine_validita IS NULL THEN 'N' ELSE 'S' END AS flag_archiviato " +
                                                  "FROM iuf_t_piano_monitoraggio t ORDER BY t.anno DESC, t.data_inserimento DESC";
  
  private static final String INSERT_PIANO = "INSERT INTO iuf_t_piano_monitoraggio(id_piano_monitoraggio, anno, versione, note, data_inserimento, data_inizio_validita, data_fine_validita, ext_id_utente_aggiornamento, data_ultimo_aggiornamento) \r\n" +
                                              "VALUES(seq_iuf_t_piano_monitoraggio.nextval, :anno, (SELECT NVL(MAX(TO_NUMBER(p.versione))+1,1) FROM iuf_t_piano_monitoraggio p WHERE p.anno = :anno)," +
                                                      ":note, SYSDATE, SYSDATE, NULL, :ext_id_utente_aggiornamento, SYSDATE)";

  private static final String SELECT_PIANO_BY_ID = "SELECT * FROM iuf_t_piano_monitoraggio WHERE id_piano_monitoraggio = :idPianoMonitoraggio";

  private static final String SELECT_ALL = "SELECT o.id_organismo_nocivo,\r\n" + 
                                            "       o.nome_latino,\r\n" + 
                                            "       o.sigla,\r\n" +
                                            "       r.ore AS ore_reg,\r\n" + 
                                            "       r.num_campioni AS num_campioni_reg,\r\n" + 
                                            "       r.num_analisi AS num_analisi_reg,\r\n" + 
                                            "       r.num_trappole AS num_trappole_reg,\r\n" + 
                                            "       e.ore_ispezioni_visive AS ore_visual_est, \r\n" + 
                                            "       e.num_campioni AS num_campioni_est, e.num_trappole AS num_trappole_est \r\n" + 
                                            "FROM iuf_d_organismo_nocivo o, iuf_t_previsione_on_reg r, iuf_t_previsione_on_est e \r\n" + 
                                            "WHERE o.id_organismo_nocivo = r.id_specie_on (+) \r\n" + 
                                            "AND o.id_organismo_nocivo = e.id_specie_on (+) \r\n" + 
                                            "ORDER BY nome_latino";
/*
  private static final String SELECT_PREVISIONE_BY_PIANO = "SELECT id_piano_monitoraggio,id_organismo_nocivo,nome_latino,sigla,\r\n" + 
      "     SUM(num_visual_reg) num_visual_reg,\r\n" + 
      "     SUM(ore_visual_reg) ore_visual_reg,\r\n" + 
      "     SUM(num_campioni_reg) num_campioni_reg,\r\n" + 
      "     SUM(ore_campioni_reg) ore_campioni_reg,\r\n" + 
      "     SUM(num_analisi_reg) num_analisi_reg,\r\n" + 
      "     SUM(num_trappole_reg) num_trappole_reg,\r\n" + 
      "     SUM(ore_trappole_reg) ore_trappole_reg,\r\n" + 
      "     SUM(num_visual_est) num_visual_est,\r\n" + 
      "     SUM(ore_visual_est) ore_visual_est,\r\n" + 
      "     SUM(num_campioni_est) num_campioni_est,\r\n" + 
      "     SUM(ore_campioni_est) ore_campioni_est,\r\n" + 
      "     SUM(num_analisi_est) num_analisi_est,\r\n" + 
      "     SUM(num_trappole_est) num_trappole_est,\r\n" + 
      "     SUM(ore_trappole_est) ore_trappole_est,\r\n" + 
      "     SUM(num_visual_av_reg) num_visual_av_reg,\r\n" + 
      "     SUM(ore_visual_av_reg) ore_visual_av_reg,\r\n" + 
      "     SUM(num_campioni_av_reg) num_campioni_av_reg,\r\n" + 
      "     SUM(ore_campioni_av_reg) ore_campioni_av_reg,\r\n" + 
      "     SUM(num_analisi_av_reg) num_analisi_av_reg,\r\n" + 
      "     SUM(num_trappole_av_reg) num_trappole_av_reg,\r\n" + 
      "     SUM(ore_trappole_av_reg) ore_trappole_av_reg,\r\n" + 
      "     SUM(num_visual_av_est) num_visual_av_est,\r\n" + 
      "     SUM(ore_visual_av_est) ore_visual_av_est,\r\n" + 
      "     SUM(num_campioni_av_est) num_campioni_av_est,\r\n" + 
      "     SUM(ore_campioni_av_est) ore_campioni_av_est,\r\n" + 
      "     SUM(num_analisi_av_est) num_analisi_av_est,\r\n" + 
      "     SUM(num_trappole_av_est) num_trappole_av_est,\r\n" + 
      "     SUM(ore_trappole_av_est) ore_trappole_av_est\r\n" + 
      "FROM (\r\n" + 
      "      SELECT r.id_piano_monitoraggio,\r\n" + 
      "             o.id_organismo_nocivo,\r\n" + 
      "             o.nome_latino,\r\n" + 
      "             o.sigla,\r\n" + 
      "             r.num_visual      AS num_visual_reg,\r\n" + 
      "             r.ore_visual      AS ore_visual_reg,\r\n" + 
      "             r.num_campioni    AS num_campioni_reg,\r\n" + 
      "             r.ore_campioni    AS ore_campioni_reg,\r\n" + 
      "             r.num_analisi     AS num_analisi_reg,\r\n" + 
      "             r.num_trappole    AS num_trappole_reg,\r\n" + 
      "             r.ore_trappole    AS ore_trappole_reg,\r\n" + 
      "             0                 AS num_visual_est,\r\n" + 
      "             0                 AS ore_visual_est,\r\n" + 
      "             0                 AS num_campioni_est,\r\n" + 
      "             0                 AS ore_campioni_est,\r\n" + 
      "             0                 AS num_analisi_est,\r\n" + 
      "             0                 AS num_trappole_est,\r\n" + 
      "             0                 AS ore_trappole_est,\r\n" + 
      "             0                 AS num_visual_av_reg,\r\n" + 
      "             0                 AS ore_visual_av_reg,\r\n" + 
      "             0                 AS num_campioni_av_reg,\r\n" + 
      "             0                 AS ore_campioni_av_reg,\r\n" + 
      "             0                 AS num_analisi_av_reg,\r\n" + 
      "             0                 AS num_trappole_av_reg,\r\n" + 
      "             0                 AS ore_trappole_av_reg,\r\n" + 
      "             0                 AS num_visual_av_est,\r\n" + 
      "             0                 AS ore_visual_av_est,\r\n" + 
      "             0                 AS num_campioni_av_est,\r\n" + 
      "             0                 AS ore_campioni_av_est,\r\n" + 
      "             0                 AS num_analisi_av_est,\r\n" + 
      "             0                 AS num_trappole_av_est,\r\n" + 
      "             0                 AS ore_trappole_av_est\r\n" + 
      "        FROM iuf_d_organismo_nocivo  o,\r\n" + 
      "             iuf_t_previsione_on_reg r,\r\n" + 
      "             iuf_t_piano_monitoraggio p\r\n" + 
      "       WHERE o.id_organismo_nocivo = r.id_specie_on\r\n" + 
      "         AND p.id_piano_monitoraggio = r.id_piano_monitoraggio\r\n" + 
      "      UNION\r\n" + 
      "      SELECT e.id_piano_monitoraggio,\r\n" + 
      "             o.id_organismo_nocivo,\r\n" + 
      "             o.nome_latino,\r\n" + 
      "             o.sigla,\r\n" + 
      "             0                  AS num_visual_reg,\r\n" + 
      "             0                  AS ore_visual_reg,\r\n" + 
      "             0                  AS num_campioni_reg,\r\n" + 
      "             0                  AS ore_campioni_reg,\r\n" + 
      "             0                  AS num_analisi_reg,\r\n" + 
      "             0                  AS num_trappole_reg,\r\n" + 
      "             0                  AS ore_trappole_reg,\r\n" + 
      "             e.num_visual       AS num_visual_est,\r\n" + 
      "             e.ore_visual       AS ore_visual_est,\r\n" + 
      "             e.num_campioni     AS num_campioni_est,\r\n" + 
      "             e.ore_campioni     AS ore_campioni_est,\r\n" + 
      "             e.num_analisi      AS num_analisi_est,\r\n" + 
      "             e.num_trappole     AS num_trappole_est,\r\n" + 
      "             e.ore_trappole     AS ore_trappole_est,\r\n" + 
      "             0                 AS num_visual_av_reg,\r\n" + 
      "             0                 AS ore_visual_av_reg,\r\n" + 
      "             0                 AS num_campioni_av_reg,\r\n" + 
      "             0                 AS ore_campioni_av_reg,\r\n" + 
      "             0                 AS num_analisi_av_reg,\r\n" + 
      "             0                 AS num_trappole_av_reg,\r\n" + 
      "             0                 AS ore_trappole_av_reg,\r\n" + 
      "             0                 AS num_visual_av_est,\r\n" + 
      "             0                 AS ore_visual_av_est,\r\n" + 
      "             0                 AS num_campioni_av_est,\r\n" + 
      "             0                 AS ore_campioni_av_est,\r\n" + 
      "             0                 AS num_analisi_av_est,\r\n" + 
      "             0                 AS num_trappole_av_est,\r\n" + 
      "             0                 AS ore_trappole_av_est\r\n" + 
      "        FROM iuf_d_organismo_nocivo  o,\r\n" + 
      "             iuf_t_previsione_on_est e,\r\n" + 
      "             iuf_t_piano_monitoraggio p\r\n" + 
      "       WHERE o.id_organismo_nocivo = e.id_specie_on\r\n" + 
      "         AND e.id_piano_monitoraggio = p.id_piano_monitoraggio\r\n" + 
      "      UNION\r\n" + 
      "      SELECT :idPianoMonitoraggio,id_organismo_nocivo,nome_latino,sigla,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\r\n" + 
      "        FROM iuf_d_organismo_nocivo o\r\n" + 
      "       WHERE o.data_fine_validita IS NULL\r\n" + 
      "      UNION\r\n" + 
      "      SELECT p.id_piano_monitoraggio, o.id_organismo_nocivo, o.nome_latino, o.sigla,\r\n" + 
      "             0 num_visual_reg,\r\n" + 
      "             0 ore_visual_reg,\r\n" + 
      "             0 num_campioni_reg,\r\n" + 
      "             0 ore_campioni_reg,\r\n" + 
      "             0 num_analisi_reg,\r\n" + 
      "             0 num_trappole_reg,\r\n" + 
      "             0 ore_trappole_reg,\r\n" + 
      "             0 num_visual_est,\r\n" + 
      "             0 ore_visual_est,\r\n" + 
      "             0 num_campioni_est,\r\n" + 
      "             0 ore_campioni_est,\r\n" + 
      "             0 num_analisi_est,\r\n" + 
      "             0 num_trappole_est,\r\n" + 
      "             0 ore_trappole_est,\r\n" + 
      "             SUM(NVL(visual_num_reg,0)) num_visual_av_reg,\r\n" + 
      "             ROUND(SUM(NVL(visual_ore_reg,0)),2) ore_visual_av_reg,\r\n" + 
      "             SUM(NVL(campioni_num_reg,0)) num_campioni_av_reg,\r\n" + 
      "             ROUND(SUM(NVL(campioni_ore_reg,0)),2) ore_campioni_av_reg,\r\n" + 
      "             (SELECT COUNT(id_esito_campione) FROM iuf_t_esito_campione ec, iuf_t_campionamento c, iuf_r_campionamento_spec_on cso, iuf_t_anagrafica ac\r\n" + 
      "                WHERE ec.id_campionamento = c.id_campionamento AND c.id_campionamento = cso.id_campionamento AND c.id_anagrafica = ac.id_anagrafica\r\n" + 
      "                AND NVL(ac.subcontractor,'N') = 'N' AND cso.id_specie_on = o.id_organismo_nocivo\r\n" + 
      "                AND to_number(to_char(c.data_ora_inizio,'YYYY')) = p.anno) num_analisi_av_reg,\r\n" + 
      "             SUM(NVL(trappole_num_reg,0)) num_trappole_av_reg,\r\n" + 
      "             ROUND(SUM(NVL(trappole_ore_reg,0)),2) ore_trappole_av_reg,\r\n" + 
      "             SUM(NVL(visual_num_est,0)) num_visual_av_est,\r\n" + 
      "             ROUND(SUM(NVL(visual_ore_est,0)),2) ore_visual_av_est,\r\n" + 
      "             SUM(NVL(campioni_num_est,0)) num_campioni_av_est,\r\n" + 
      "             ROUND(SUM(NVL(campioni_ore_est,0)),2) ore_campioni_av_est,\r\n" + 
      "             (SELECT COUNT(id_esito_campione) FROM iuf_t_esito_campione ec, iuf_t_campionamento c, iuf_r_campionamento_spec_on cso, iuf_t_anagrafica ac\r\n" + 
      "                WHERE ec.id_campionamento = c.id_campionamento AND c.id_campionamento = cso.id_campionamento AND c.id_anagrafica = ac.id_anagrafica\r\n" + 
      "                AND NVL(ac.subcontractor,'N') = 'S' AND cso.id_specie_on = o.id_organismo_nocivo\r\n" + 
      "                AND to_number(to_char(c.data_ora_inizio,'YYYY')) = p.anno) num_analisi_av_est,\r\n" + 
      "             SUM(NVL(trappole_num_est,0)) num_trappole_av_est,\r\n" + 
      "             ROUND(SUM(NVL(trappole_ore_est,0)),2) ore_trappole_av_est\r\n" + 
      "        FROM \r\n" + 
      "        (\r\n" + 
      "        SELECT att.id_specie_on,att.tipo,att.anno,att.subcontractor,\r\n" + 
      "               (SELECT SUM(ore_ispezione)\r\n" + 
      "                FROM (\r\n" + 
      "                      SELECT td.*,(CASE WHEN peso_verbale > 0 THEN ROUND(ore_totali_siti/peso_verbale,2) ELSE 0 END) AS ore_operazioni,\r\n" + 
      "                           /* inizio formula ore ispezione * /\r\n" + 
      "                           ROUND((CASE WHEN num_campioni>0\r\n" + 
      "                              THEN\r\n" + 
      "                                (CASE WHEN num_trapp_tot>0\r\n" + 
      "                                   THEN (peso_riga-1-num_trapp_tot)*(CASE WHEN peso_verbale > 0 THEN ore_totali_siti/peso_verbale ELSE 0 END)\r\n" + 
      "                                   ELSE (peso_riga-1)*(CASE WHEN peso_verbale > 0 THEN ore_totali_siti/peso_verbale ELSE 0 END) END)\r\n" + 
      "                              ELSE\r\n" + 
      "                                (CASE WHEN num_trapp_tot>0\r\n" + 
      "                                   THEN (peso_riga-num_trapp_tot)*(CASE WHEN peso_verbale > 0 THEN ROUND(ore_totali_siti/peso_verbale,2) ELSE 0 END)\r\n" + 
      "                                   ELSE peso_riga*(CASE WHEN peso_verbale > 0 THEN ore_totali_siti/peso_verbale ELSE 0 END) END)\r\n" + 
      "                            END),2) ore_ispezione\r\n" + 
      "                           /* fine formula ore ispezione * /\r\n" + 
      "                        FROM (\r\n" + 
      "                              SELECT op.*,\r\n" + 
      "                                     /* inizio formula peso riga * /\r\n" + 
      "                                    (CASE WHEN count_organismi>0 THEN\r\n" + 
      "                                                    CASE WHEN (ha/10000)>0 THEN\r\n" + 
      "                                                      CASE WHEN (ha/10000)>(velocita*0.0001) THEN LN(ha)/LN(velocita) ELSE 1 END\r\n" + 
      "                                                    ELSE\r\n" + 
      "                                                      CASE WHEN numero_piante>velocita THEN LN(numero_piante)/LN(velocita) ELSE 1 END\r\n" + 
      "                                                    END\r\n" + 
      "                                                  ELSE 0 END)\r\n" + 
      "                                             + (CASE WHEN num_campioni>0 THEN 1 ELSE 0 END)\r\n" + 
      "                                             + (CASE WHEN num_trapp_tot>0 THEN 1 ELSE 0 END) peso_riga,\r\n" + 
      "                                     /* fine formula peso riga * /\r\n" + 
      "                                     /* inizio peso verbale * /\r\n" + 
      "                                    (SELECT SUM(peso_riga)\r\n" + 
      "                                       FROM (\r\n" + 
      "                                          SELECT op.*,ROUND((CASE WHEN count_organismi>0 THEN\r\n" + 
      "                                                          CASE WHEN (ha/10000)>0 THEN\r\n" + 
      "                                                            CASE WHEN (ha/10000)>(velocita*0.0001) THEN LN(ha)/LN(velocita) ELSE 1 END\r\n" + 
      "                                                          ELSE\r\n" + 
      "                                                            CASE WHEN numero_piante>velocita THEN LN(numero_piante)/LN(velocita) ELSE 1 END\r\n" + 
      "                                                          END\r\n" + 
      "                                                        ELSE 0 END)\r\n" + 
      "                                                   + (CASE WHEN num_campioni>0 THEN 1 ELSE 0 END)\r\n" + 
      "                                                   + (CASE WHEN num_trapp_tot>0 THEN 1 ELSE 0 END),2) peso_riga\r\n" + 
      "                                           FROM (\r\n" + 
      "                                          SELECT a.id_missione,\r\n" + 
      "                                                   (SELECT COUNT(*) FROM iuf_t_ispezione_visiva iv, iuf_r_isp_visiva_spec_on ivo, iuf_d_organismo_nocivo oo\r\n" + 
      "                                                     WHERE iv.id_ispezione = ivo.id_ispezione\r\n" + 
      "                                                       AND iv.id_rilevazione = b.id_rilevazione\r\n" + 
      "                                                       AND ivo.id_specie_on = oo.id_organismo_nocivo\r\n" + 
      "                                                       AND oo.euro = 'S') AS count_organismi,\r\n" + 
      "                                                         (SELECT COUNT(*)\r\n" + 
      "                                                            FROM iuf_t_campionamento r\r\n" + 
      "                                                           WHERE r.id_ispezione_visiva = c.id_ispezione\r\n" + 
      "                                                             AND r.id_rilevazione = c.id_rilevazione) AS num_campioni,\r\n" + 
      "                                                         (SELECT COUNT(*)\r\n" + 
      "                                                            FROM iuf_t_trappolaggio aa, iuf_t_trappola bb\r\n" + 
      "                                                           WHERE aa.id_trappola = bb.id_trappola\r\n" + 
      "                                                             AND aa.id_rilevazione = b.id_rilevazione) AS num_trapp_tot,\r\n" + 
      "                                                         NVL(c.superfice, 0) AS ha,\r\n" + 
      "                                                         NVL(c.numero_piante, 0) AS numero_piante,\r\n" + 
      "                                                         NVL(z.velocita, 0) AS velocita\r\n" + 
      "                                                    FROM iuf_t_missione         a,\r\n" + 
      "                                                         iuf_t_rilevazione      b,\r\n" + 
      "                                                         iuf_t_ispezione_visiva c,\r\n" + 
      "                                                         iuf_d_specie_vegetale  v,\r\n" + 
      "                                                         iuf_d_tipo_area        z\r\n" + 
      "                                                   WHERE a.id_missione = b.id_missione\r\n" + 
      "                                                     AND b.id_rilevazione = c.id_rilevazione\r\n" + 
      "                                                     AND c.id_specie_vegetale = v.id_specie_vegetale (+)\r\n" + 
      "                                                     AND z.id_tipo_area = b.id_tipo_area\r\n" + 
      "                                                  UNION ALL\r\n" + 
      "                                                  SELECT a.id_missione,\r\n" + 
      "                                                   (SELECT COUNT(*) FROM iuf_t_campionamento c, iuf_r_campionamento_spec_on so, iuf_d_organismo_nocivo o\r\n" + 
      "                                                     WHERE c.id_campionamento = so.id_campionamento AND so.id_specie_on = o.id_organismo_nocivo\r\n" + 
      "                                                       AND c.id_rilevazione = b.id_rilevazione AND o.euro = 'S') AS count_organismi,\r\n" + 
      "                                                         (SELECT COUNT(*)\r\n" + 
      "                                                            FROM iuf_t_campionamento r\r\n" + 
      "                                                           WHERE r.id_rilevazione = b.id_rilevazione) AS num_campioni,\r\n" + 
      "                                                         0 AS num_trapp_tot,\r\n" + 
      "                                                         0 AS superfice,\r\n" + 
      "                                                         0 AS numero_piante,\r\n" + 
      "                                                         NVL(z.velocita, 0) AS velocita\r\n" + 
      "                                                    FROM iuf_t_missione        a,\r\n" + 
      "                                                         iuf_t_rilevazione     b,\r\n" + 
      "                                                         iuf_t_campionamento   d,\r\n" + 
      "                                                         iuf_d_specie_vegetale v,\r\n" + 
      "                                                         iuf_d_tipo_area       z\r\n" + 
      "                                                   WHERE b.id_missione = a.id_missione\r\n" + 
      "                                                     AND d.id_rilevazione = b.id_rilevazione\r\n" + 
      "                                                     AND d.id_ispezione_visiva IS NULL\r\n" + 
      "                                                     AND d.id_specie_vegetale = v.id_specie_vegetale\r\n" + 
      "                                                     AND z.id_tipo_area = b.id_tipo_area\r\n" + 
      "                                                  UNION ALL\r\n" + 
      "                                                  SELECT a.id_missione,\r\n" + 
      "                                                         0 AS count_organismi,\r\n" + 
      "                                                         0 AS num_campioni,\r\n" + 
      "                                                         (SELECT COUNT(*) FROM iuf_t_trappolaggio aa WHERE aa.id_trappolaggio = e.id_trappolaggio) AS num_trapp_tot,\r\n" + 
      "                                                         0 AS superfice,\r\n" + 
      "                                                         0 AS numero_piante,\r\n" + 
      "                                                         NVL(z.velocita, 0) AS velocita\r\n" + 
      "                                                    FROM iuf_t_missione        a,\r\n" + 
      "                                                         iuf_t_rilevazione     b,\r\n" + 
      "                                                         iuf_t_trappolaggio    e,\r\n" + 
      "                                                         iuf_t_trappola        f,\r\n" + 
      "                                                         iuf_d_specie_vegetale v,\r\n" + 
      "                                                         Iuf_d_Tipo_Area       z\r\n" + 
      "                                                   WHERE b.id_missione = a.id_missione\r\n" + 
      "                                                     AND f.id_specie_veg = v.id_specie_vegetale\r\n" + 
      "                                                     AND e.id_rilevazione = b.id_rilevazione\r\n" + 
      "                                                     AND e.id_ispezione_visiva IS NULL\r\n" + 
      "                                                     AND e.id_trappola = f.id_trappola\r\n" + 
      "                                                     AND z.id_tipo_area = b.id_tipo_area\r\n" + 
      "                                          ) op\r\n" + 
      "                                    ) tv\r\n" + 
      "                                   WHERE tv.id_missione = op.id_missione) AS peso_verbale,\r\n" + 
      "                                   /* fine peso verbale * /\r\n" + 
      "                                     (CASE WHEN ore>5.5 THEN ore-0.5 ELSE ore END) AS ore_totali_siti\r\n" + 
      "                               FROM (\r\n" + 
      "                                    SELECT id_missione,data_ora_inizio_missione,data_ora_fine_missione,ore,count_organismi,ha,numero_piante,velocita,id_specie_on,\r\n" + 
      "                                           SUM(num_campioni) AS num_campioni, SUM(num_trapp_tot) AS num_trapp_tot\r\n" + 
      "                                      FROM (\r\n" + 
      "                                            SELECT m.id_missione,\r\n" + 
      "                                                   m.data_ora_inizio_missione,\r\n" + 
      "                                                   m.data_ora_fine_missione,\r\n" + 
      "                                                   (m.data_ora_fine_missione-m.data_ora_inizio_missione)*24 AS ore,\r\n" + 
      "                                                   (SELECT COUNT(*) FROM iuf_t_ispezione_visiva iv, iuf_r_isp_visiva_spec_on ivo, iuf_d_organismo_nocivo oo\r\n" + 
      "                                                     WHERE iv.id_ispezione = ivo.id_ispezione\r\n" + 
      "                                                       AND iv.id_rilevazione = b.id_rilevazione\r\n" + 
      "                                                       AND ivo.id_specie_on = oo.id_organismo_nocivo\r\n" + 
      "                                                       AND oo.euro = 'S') AS count_organismi,\r\n" + 
      "                                                    0 num_campioni,\r\n" + 
      "                                                    0 num_trapp_tot,\r\n" + 
      "                                                   NVL(c.superfice, 0) AS ha,\r\n" + 
      "                                                   NVL(c.numero_piante, 0) AS numero_piante,\r\n" + 
      "                                                   NVL(z.velocita, 0) AS velocita,\r\n" + 
      "                                                   vso.id_specie_on\r\n" + 
      "                                              FROM iuf_t_missione         m,\r\n" + 
      "                                                   iuf_t_rilevazione      b,\r\n" + 
      "                                                   iuf_t_ispezione_visiva c,\r\n" + 
      "                                                   iuf_r_isp_visiva_spec_on vso,\r\n" + 
      "                                                   iuf_d_specie_vegetale  v,\r\n" + 
      "                                                   iuf_d_tipo_Area        z\r\n" + 
      "                                             WHERE m.id_missione = b.id_missione\r\n" + 
      "                                               AND b.id_rilevazione = c.id_rilevazione\r\n" + 
      "                                               AND c.id_specie_vegetale = v.id_specie_vegetale (+)\r\n" + 
      "                                               AND z.id_tipo_area = b.id_tipo_area\r\n" + 
      "                                               AND c.id_ispezione = vso.id_ispezione\r\n" + 
      "                                            UNION\r\n" + 
      "                                            SELECT m.id_missione,\r\n" + 
      "                                                   m.data_ora_inizio_missione,\r\n" + 
      "                                                   m.data_ora_fine_missione,\r\n" + 
      "                                                   (m.data_ora_fine_missione-m.data_ora_inizio_missione)*24 AS ore,\r\n" + 
      "                                                   (SELECT COUNT(*) FROM iuf_t_campionamento c, iuf_r_campionamento_spec_on so, iuf_d_organismo_nocivo o\r\n" + 
      "                                                     WHERE c.id_campionamento = so.id_campionamento AND so.id_specie_on = o.id_organismo_nocivo\r\n" + 
      "                                                       AND c.id_rilevazione = b.id_rilevazione AND o.euro = 'S') AS count_organismi,\r\n" + 
      "                                                   (SELECT COUNT(*)\r\n" + 
      "                                                      FROM iuf_t_campionamento r\r\n" + 
      "                                                     WHERE r.id_rilevazione = b.id_rilevazione) AS num_campioni,\r\n" + 
      "                                                   0 AS num_trapp_tot,\r\n" + 
      "                                                   0 AS superfice,\r\n" + 
      "                                                   0 AS numero_piante,\r\n" + 
      "                                                   NVL(z.velocita, 0) AS velocita,\r\n" + 
      "                                                   cso.id_specie_on\r\n" + 
      "                                              FROM iuf_t_missione        m,\r\n" + 
      "                                                   iuf_t_rilevazione     b,\r\n" + 
      "                                                   iuf_t_campionamento   d,\r\n" + 
      "                                                   iuf_r_campionamento_spec_on cso,\r\n" + 
      "                                                   iuf_d_specie_vegetale v,\r\n" + 
      "                                                   iuf_d_tipo_area       z\r\n" + 
      "                                             WHERE b.id_missione = m.id_missione\r\n" + 
      "                                               AND d.id_rilevazione = b.id_rilevazione\r\n" + 
      "                                               AND d.id_ispezione_visiva IS NULL --?\r\n" + 
      "                                               AND d.id_campionamento = cso.id_campionamento\r\n" + 
      "                                               AND d.id_specie_vegetale = v.id_specie_vegetale\r\n" + 
      "                                               AND z.id_tipo_area = b.id_tipo_area\r\n" + 
      "                                            UNION\r\n" + 
      "                                            SELECT m.id_missione,\r\n" + 
      "                                                   m.data_ora_inizio_missione,\r\n" + 
      "                                                   m.data_ora_fine_missione,\r\n" + 
      "                                                   (m.data_ora_fine_missione-m.data_ora_inizio_missione)*24 AS ore,\r\n" + 
      "                                                   0 AS count_organismi,\r\n" + 
      "                                                   0 AS num_campioni,\r\n" + 
      "                                                   (SELECT COUNT(*) FROM iuf_t_trappolaggio aa WHERE aa.id_rilevazione = b.id_rilevazione)/*1* / AS num_trapp_tot,\r\n" + 
      "                                                   0 AS superfice,\r\n" + 
      "                                                   0 AS numero_piante,\r\n" + 
      "                                                   NVL(z.velocita, 0) AS velocita,\r\n" + 
      "                                                   e.id_organismo_nocivo\r\n" + 
      "                                              FROM iuf_t_missione        m,\r\n" + 
      "                                                   iuf_t_rilevazione     b,\r\n" + 
      "                                                   iuf_t_trappolaggio    e,\r\n" + 
      "                                                   iuf_t_trappola        f,\r\n" + 
      "                                                   iuf_d_tipo_trappola   g,\r\n" + 
      "                                                   iuf_d_specie_vegetale v,\r\n" + 
      "                                                   iuf_d_tipo_Area       z\r\n" + 
      "                                             WHERE b.id_missione = m.id_missione\r\n" + 
      "                                               AND f.id_specie_veg = v.id_specie_vegetale\r\n" + 
      "                                               AND e.id_rilevazione = b.id_rilevazione\r\n" + 
      "                                               AND e.id_ispezione_visiva IS NULL\r\n" + 
      "                                               AND e.id_trappola = f.id_trappola\r\n" + 
      "                                               AND f.id_tipo_trappola = g.id_tipo_trappola\r\n" + 
      "                                               AND z.id_tipo_area = b.id_tipo_area\r\n" + 
      "                                     ) GROUP BY id_missione,data_ora_inizio_missione,data_ora_fine_missione,ore,count_organismi,ha,numero_piante,velocita,id_specie_on\r\n" + 
      "                                    ) op\r\n" + 
      "                    ) td) tt\r\n" + 
      "                WHERE to_number(to_char(tt.data_ora_inizio_missione,'YYYY')) = att.anno\r\n" + 
      "                  AND tt.id_specie_on = att.id_specie_on) AS ore_on,\r\n" + 
      "               COUNT(tipo) AS num_tipo\r\n" + 
      "          FROM (\r\n" + 
      "              SELECT v.id_rilevazione,to_number(to_char(m.data_ora_inizio_missione,'YYYY')) AS anno,vso.id_specie_on,'V' AS tipo,NVL(a.subcontractor,'N') subcontractor\r\n" + 
      "                FROM iuf_t_missione m, iuf_t_rilevazione ril,iuf_t_ispezione_visiva v, iuf_r_isp_visiva_spec_on vso, iuf_t_anagrafica a\r\n" + 
      "               WHERE v.id_ispezione = vso.id_ispezione AND m.id_missione = ril.id_missione\r\n" + 
      "                 AND ril.id_rilevazione = v.id_rilevazione AND v.id_anagrafica = a.id_anagrafica\r\n" + 
      "              UNION ALL\r\n" + 
      "              SELECT c.id_rilevazione,to_number(to_char(m.data_ora_inizio_missione,'YYYY')), cso.id_specie_on,'C',NVL(a.subcontractor,'N')\r\n" + 
      "                FROM iuf_t_missione m, iuf_t_rilevazione ril, iuf_t_campionamento c, iuf_r_campionamento_spec_on cso, iuf_t_anagrafica a\r\n" + 
      "               WHERE c.id_campionamento = cso.id_campionamento AND m.id_missione = ril.id_missione\r\n" + 
      "                 AND ril.id_rilevazione = c.id_rilevazione AND c.id_anagrafica = a.id_anagrafica\r\n" + 
      "              UNION ALL\r\n" + 
      "              SELECT t.id_rilevazione,to_number(to_char(m.data_ora_inizio_missione,'YYYY')), t.id_organismo_nocivo,'T',NVL(a.subcontractor,'N')\r\n" + 
      "                FROM iuf_t_missione m, iuf_t_rilevazione ril, iuf_t_trappolaggio t, iuf_t_anagrafica a\r\n" + 
      "               WHERE m.id_missione = ril.id_missione AND ril.id_rilevazione = t.id_rilevazione\r\n" + 
      "                 AND t.id_organismo_nocivo IS NOT NULL AND t.id_operazione = 1\r\n" + 
      "                 AND t.id_anagrafica = a.id_anagrafica\r\n" + 
      "              ) att\r\n" + 
      "      --WHERE att.id_rilevazione = pesi.id_rilevazione\r\n" + 
      "      GROUP BY att.id_specie_on,tipo,anno,subcontractor\r\n" + 
      "      )\r\n" + 
      "      PIVOT(\r\n" + 
      "          SUM(ore_on) AS ore,SUM(num_tipo) AS NUM\r\n" + 
      "          FOR subcontractor\r\n" + 
      "          IN ( \r\n" + 
      "              'N' AS reg,\r\n" + 
      "              'S' AS est\r\n" + 
      "          )\r\n" + 
      "      )\r\n" + 
      "      PIVOT(\r\n" + 
      "          SUM(reg_ore) AS ore_reg,SUM(est_ore) ore_est,SUM(reg_num) AS num_reg,SUM(est_num) num_est\r\n" + 
      "          FOR tipo\r\n" + 
      "          IN ( \r\n" + 
      "              'V' AS visual,\r\n" + 
      "              'C' AS campioni,\r\n" + 
      "              'T' AS trappole\r\n" + 
      "          )\r\n" + 
      "      ) dati, iuf_d_organismo_nocivo o, iuf_t_piano_monitoraggio p\r\n" + 
      "      WHERE dati.id_specie_on (+) = o.id_organismo_nocivo\r\n" + 
      "        AND NVL(dati.anno,p.anno) = p.anno\r\n" + 
      "        AND o.data_fine_validita IS NULL \r\n" + 
      "      GROUP BY p.id_piano_monitoraggio, o.id_organismo_nocivo, o.nome_latino, o.sigla, p.anno\r\n" + 
      "     )\r\n" + 
      " WHERE NVL(id_piano_monitoraggio,:idPianoMonitoraggio) = :idPianoMonitoraggio\r\n" + 
      " GROUP BY id_piano_monitoraggio,id_organismo_nocivo,nome_latino,sigla\r\n" + 
      " ORDER BY nome_latino";
*/
  private static final String SELECT_PREVISIONE_BY_PIANO = "SELECT id_piano_monitoraggio,id_organismo_nocivo,nome_latino,sigla,\r\n" + 
      "       SUM(num_visual_reg) num_visual_reg,\r\n" + 
      "       SUM(ore_visual_reg) ore_visual_reg,\r\n" + 
      "       SUM(num_campioni_reg) num_campioni_reg,\r\n" + 
      "       SUM(ore_campioni_reg) ore_campioni_reg,\r\n" + 
      "       SUM(num_analisi_reg) num_analisi_reg,\r\n" + 
      "       SUM(num_trappole_reg) num_trappole_reg,\r\n" + 
      "       SUM(ore_trappole_reg) ore_trappole_reg,\r\n" + 
      "       SUM(num_visual_est) num_visual_est,\r\n" + 
      "       SUM(ore_visual_est) ore_visual_est,\r\n" + 
      "       SUM(num_campioni_est) num_campioni_est,\r\n" + 
      "       SUM(ore_campioni_est) ore_campioni_est,\r\n" + 
      "       SUM(num_analisi_est) num_analisi_est,\r\n" + 
      "       SUM(num_trappole_est) num_trappole_est,\r\n" + 
      "       SUM(ore_trappole_est) ore_trappole_est,\r\n" + 
      "       SUM(num_visual_av_reg) num_visual_av_reg,\r\n" + 
      "       SUM(ore_visual_av_reg) ore_visual_av_reg,\r\n" + 
      "       SUM(num_campioni_av_reg) num_campioni_av_reg,\r\n" + 
      "       SUM(ore_campioni_av_reg) ore_campioni_av_reg,\r\n" + 
      "       SUM(num_analisi_av_reg) num_analisi_av_reg,\r\n" + 
      "       SUM(num_trappole_av_reg) num_trappole_av_reg,\r\n" + 
      "       SUM(ore_trappole_av_reg) ore_trappole_av_reg,\r\n" + 
      "       SUM(num_visual_av_est) num_visual_av_est,\r\n" + 
      "       SUM(ore_visual_av_est) ore_visual_av_est,\r\n" + 
      "       SUM(num_campioni_av_est) num_campioni_av_est,\r\n" + 
      "       SUM(ore_campioni_av_est) ore_campioni_av_est,\r\n" + 
      "       SUM(num_analisi_av_est) num_analisi_av_est,\r\n" + 
      "       SUM(num_trappole_av_est) num_trappole_av_est,\r\n" + 
      "       SUM(ore_trappole_av_est) ore_trappole_av_est\r\n" + 
      "  FROM (\r\n" + 
      "        SELECT r.id_piano_monitoraggio,\r\n" + 
      "               o.id_organismo_nocivo,\r\n" + 
      "               o.nome_latino,\r\n" + 
      "               o.sigla,\r\n" + 
      "               r.num_visual      AS num_visual_reg,\r\n" + 
      "               r.ore_visual      AS ore_visual_reg,\r\n" + 
      "               r.num_campioni    AS num_campioni_reg,\r\n" + 
      "               r.ore_campioni    AS ore_campioni_reg,\r\n" + 
      "               r.num_analisi     AS num_analisi_reg,\r\n" + 
      "               r.num_trappole    AS num_trappole_reg,\r\n" + 
      "               r.ore_trappole    AS ore_trappole_reg,\r\n" + 
      "               0                 AS num_visual_est,\r\n" + 
      "               0                 AS ore_visual_est,\r\n" + 
      "               0                 AS num_campioni_est,\r\n" + 
      "               0                 AS ore_campioni_est,\r\n" + 
      "               0                 AS num_analisi_est,\r\n" + 
      "               0                 AS num_trappole_est,\r\n" + 
      "               0                 AS ore_trappole_est,\r\n" + 
      "               0                 AS num_visual_av_reg,\r\n" + 
      "               0                 AS ore_visual_av_reg,\r\n" + 
      "               0                 AS num_campioni_av_reg,\r\n" + 
      "               0                 AS ore_campioni_av_reg,\r\n" + 
      "               0                 AS num_analisi_av_reg,\r\n" + 
      "               0                 AS num_trappole_av_reg,\r\n" + 
      "               0                 AS ore_trappole_av_reg,\r\n" + 
      "               0                 AS num_visual_av_est,\r\n" + 
      "               0                 AS ore_visual_av_est,\r\n" + 
      "               0                 AS num_campioni_av_est,\r\n" + 
      "               0                 AS ore_campioni_av_est,\r\n" + 
      "               0                 AS num_analisi_av_est,\r\n" + 
      "               0                 AS num_trappole_av_est,\r\n" + 
      "               0                 AS ore_trappole_av_est\r\n" + 
      "          FROM iuf_d_organismo_nocivo  o,\r\n" + 
      "               iuf_t_previsione_on_reg r,\r\n" + 
      "               iuf_t_piano_monitoraggio p\r\n" + 
      "         WHERE o.id_organismo_nocivo = r.id_specie_on\r\n" + 
      "           AND p.id_piano_monitoraggio = r.id_piano_monitoraggio\r\n" + 
      "        UNION\r\n" + 
      "        SELECT e.id_piano_monitoraggio,\r\n" + 
      "               o.id_organismo_nocivo,\r\n" + 
      "               o.nome_latino,\r\n" + 
      "               o.sigla,\r\n" + 
      "               0                  AS num_visual_reg,\r\n" + 
      "               0                  AS ore_visual_reg,\r\n" + 
      "               0                  AS num_campioni_reg,\r\n" + 
      "               0                  AS ore_campioni_reg,\r\n" + 
      "               0                  AS num_analisi_reg,\r\n" + 
      "               0                  AS num_trappole_reg,\r\n" + 
      "               0                  AS ore_trappole_reg,\r\n" + 
      "               e.num_visual       AS num_visual_est,\r\n" + 
      "               e.ore_visual       AS ore_visual_est,\r\n" + 
      "               e.num_campioni     AS num_campioni_est,\r\n" + 
      "               e.ore_campioni     AS ore_campioni_est,\r\n" + 
      "               e.num_analisi      AS num_analisi_est,\r\n" + 
      "               e.num_trappole     AS num_trappole_est,\r\n" + 
      "               e.ore_trappole     AS ore_trappole_est,\r\n" + 
      "               0                 AS num_visual_av_reg,\r\n" + 
      "               0                 AS ore_visual_av_reg,\r\n" + 
      "               0                 AS num_campioni_av_reg,\r\n" + 
      "               0                 AS ore_campioni_av_reg,\r\n" + 
      "               0                 AS num_analisi_av_reg,\r\n" + 
      "               0                 AS num_trappole_av_reg,\r\n" + 
      "               0                 AS ore_trappole_av_reg,\r\n" + 
      "               0                 AS num_visual_av_est,\r\n" + 
      "               0                 AS ore_visual_av_est,\r\n" + 
      "               0                 AS num_campioni_av_est,\r\n" + 
      "               0                 AS ore_campioni_av_est,\r\n" + 
      "               0                 AS num_analisi_av_est,\r\n" + 
      "               0                 AS num_trappole_av_est,\r\n" + 
      "               0                 AS ore_trappole_av_est\r\n" + 
      "          FROM iuf_d_organismo_nocivo  o,\r\n" + 
      "               iuf_t_previsione_on_est e,\r\n" + 
      "               iuf_t_piano_monitoraggio p\r\n" + 
      "         WHERE o.id_organismo_nocivo = e.id_specie_on\r\n" + 
      "           AND e.id_piano_monitoraggio = p.id_piano_monitoraggio\r\n" + 
      "        UNION\r\n" + 
      "        SELECT :idPianoMonitoraggio,id_organismo_nocivo,nome_latino,sigla,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\r\n" + 
      "          FROM iuf_d_organismo_nocivo o\r\n" + 
      "         WHERE o.data_fine_validita IS NULL\r\n" + 
      "        UNION\r\n" + 
      "        SELECT id_piano_monitoraggio,id_organismo_nocivo,nome_latino,sigla,\r\n" + 
      "               0 num_visual_reg,\r\n" + 
      "               0 ore_visual_reg,\r\n" + 
      "               0 num_campioni_reg,\r\n" + 
      "               0 ore_campioni_reg,\r\n" + 
      "               0 num_analisi_reg,\r\n" + 
      "               0 num_trappole_reg,\r\n" + 
      "               0 ore_trappole_reg,\r\n" + 
      "               0 num_visual_est,\r\n" + 
      "               0 ore_visual_est,\r\n" + 
      "               0 num_campioni_est,\r\n" + 
      "               0 ore_campioni_est,\r\n" + 
      "               0 num_analisi_est,\r\n" + 
      "               0 num_trappole_est,\r\n" + 
      "               0 ore_trappole_est,\r\n" + 
      "               reg_num_v AS num_visual_av_reg,\r\n" + 
      "               reg_ore_v AS ore_visual_av_reg,\r\n" + 
      "               reg_num_c AS num_campioni_av_reg,\r\n" + 
      "               reg_ore_c AS ore_campioni_av_reg,\r\n" + 
      "               reg_num_a AS num_analisi_av_reg,\r\n" + 
      "               reg_num_t AS num_trappole_av_reg,\r\n" + 
      "               reg_ore_t AS ore_trappole_av_reg,\r\n" + 
      "               est_num_v AS num_visual_av_est,\r\n" + 
      "               est_ore_v AS ore_visual_av_est,\r\n" + 
      "               est_num_c AS num_campioni_av_est,\r\n" + 
      "               est_ore_c AS ore_campioni_av_est,\r\n" + 
      "               est_num_a AS num_analisi_av_est,\r\n" + 
      "               est_num_t AS num_trappole_av_est,\r\n" + 
      "               est_ore_t AS ore_trappole_av_est\r\n" + 
      "               \r\n" + 
      "         FROM (\r\n" + 
      "\r\n" + 
      "        SELECT att.id_piano_monitoraggio,att.id_specie_on id_organismo_nocivo,o.nome_latino,o.sigla,att.subcontractor,ROUND(SUM(ore_visual),2) ore_visual,ROUND(SUM(ore_campioni),2) ore_campioni,ROUND(SUM(ore_trappole),2) ore_trappole,\r\n" + 
      "               SUM(decode(att.tipo,'V',1,0)) AS num_visual,SUM(DECODE(att.tipo,'C',1,0)) AS num_campioni,SUM(decode(att.tipo,'T',1,0)) AS num_trappole, SUM(DECODE(att.tipo,'A',1,0)) AS num_analisi\r\n" + 
      "          FROM (\r\n" + 
      "                SELECT p.id_piano_monitoraggio,vso.id_specie_on,NVL(a.subcontractor,'N') subcontractor,'V' AS tipo,\r\n" + 
      "                       NVL(((CASE WHEN NVL(v.data_ora_fine,r.data_ora_fine) < v.data_ora_inizio THEN v.data_ora_inizio ELSE NVL(v.data_ora_fine,r.data_ora_fine) END)-v.data_ora_inizio)*24,0) AS ore_visual,0 AS ore_campioni, 0 AS ore_trappole\r\n" + 
      "                  FROM iuf_t_ispezione_visiva v, iuf_t_piano_monitoraggio p, iuf_r_isp_visiva_spec_on vso, iuf_t_rilevazione r, iuf_t_anagrafica a, iuf_t_missione m\r\n" + 
      "                 WHERE v.id_ispezione = vso.id_ispezione AND v.id_anagrafica = a.id_anagrafica AND r.id_missione = m.id_missione\r\n" + 
      "                 AND v.id_rilevazione = r.id_rilevazione AND EXTRACT (YEAR FROM m.data_ora_inizio_missione) = p.anno\r\n" + 
      "                UNION ALL\r\n" + 
      "                SELECT p.id_piano_monitoraggio,cso.id_specie_on,NVL(a.subcontractor,'N'),'C',0,\r\n" + 
      "                       NVL(((CASE WHEN NVL(c.data_ora_fine,r.data_ora_fine) < c.data_ora_inizio THEN c.data_ora_inizio ELSE NVL(c.data_ora_fine,r.data_ora_fine) END)-c.data_ora_inizio)*24,0) AS ore_campioni,0\r\n" + 
      "                  FROM iuf_t_campionamento c, iuf_t_piano_monitoraggio p, iuf_r_campionamento_spec_on cso, iuf_t_rilevazione r, iuf_t_anagrafica a, iuf_t_missione m\r\n" + 
      "                 WHERE c.id_campionamento = cso.id_campionamento AND c.id_anagrafica = a.id_anagrafica AND r.id_missione = m.id_missione\r\n" + 
      "                 AND c.id_rilevazione = r.id_rilevazione AND EXTRACT (YEAR FROM m.data_ora_inizio_missione) = p.anno\r\n" + 
      "                UNION ALL\r\n" + 
      "                SELECT p.id_piano_monitoraggio,t.id_organismo_nocivo,NVL(a.subcontractor,'N'),'T',0,0,\r\n" + 
      "                       /* inizio calcolo data fine */\r\n" + 
      "                       NVL(((SELECT MIN(NVL(att1.data_ora_inizio,ril.data_ora_fine)) AS data_fine\r\n" + 
      "                               FROM (\r\n" + 
      "                                     SELECT t1.id_rilevazione,t1.data_ora_inizio FROM iuf_t_trappolaggio t1\r\n" + 
      "                                     UNION\r\n" + 
      "                                     SELECT v1.id_rilevazione,v1.data_ora_inizio FROM iuf_t_ispezione_visiva v1\r\n" + 
      "                                     UNION\r\n" + 
      "                                     SELECT c1.id_rilevazione,c1.data_ora_inizio FROM iuf_t_campionamento c1\r\n" + 
      "                                    ) att1, iuf_t_rilevazione ril\r\n" + 
      "                              WHERE att1.id_rilevazione = ril.id_rilevazione\r\n" + 
      "                                AND ril.id_rilevazione = r.id_rilevazione\r\n" + 
      "                                AND att1.data_ora_inizio > t.data_ora_inizio)\r\n" + 
      "                          /* fine calcolo data fine */\r\n" + 
      "                        -t.data_ora_inizio)*24,0) AS ore_trappole\r\n" + 
      "                  FROM iuf_t_trappolaggio t, iuf_t_piano_monitoraggio p, iuf_t_rilevazione r, iuf_t_anagrafica a, iuf_t_missione m\r\n" + 
      "                 WHERE t.id_rilevazione = r.id_rilevazione AND t.id_anagrafica = a.id_anagrafica AND r.id_missione = m.id_missione\r\n" + 
      "                 AND EXTRACT (YEAR FROM m.data_ora_inizio_missione) = p.anno\r\n" + 
      "                UNION ALL\r\n" + 
      "                SELECT p.id_piano_monitoraggio,cso.id_specie_on,NVL(ac.subcontractor,'N'),'A',0,0,0\r\n" + 
      "                  FROM iuf_t_esito_campione ec, iuf_t_campionamento c, iuf_r_campionamento_spec_on cso, iuf_t_anagrafica ac, iuf_t_rilevazione r, iuf_t_missione m, iuf_t_piano_monitoraggio p\r\n" + 
      "                 WHERE ec.id_campionamento = c.id_campionamento AND c.id_campionamento = cso.id_campionamento AND c.id_anagrafica = ac.id_anagrafica\r\n" + 
      "                   AND c.id_rilevazione = r.id_rilevazione AND r.id_missione = m.id_missione\r\n" + 
      "                   AND EXTRACT (YEAR FROM m.data_ora_inizio_missione) = p.anno\r\n" + 
      "        ) att, iuf_d_organismo_nocivo o\r\n" + 
      "        WHERE att.id_specie_on = o.id_organismo_nocivo\r\n" + 
      "          AND o.data_fine_validita IS NULL\r\n" + 
      "        GROUP BY att.id_piano_monitoraggio,att.id_specie_on,att.subcontractor,o.nome_latino,o.sigla\r\n" + 
      "        )\r\n" + 
      "              PIVOT(\r\n" + 
      "                  SUM(ore_visual) AS ore_v,SUM(ore_campioni) AS ore_c,SUM(ore_trappole) AS ore_t,SUM(num_visual) AS num_v,SUM(num_campioni) AS num_c,SUM(num_trappole) AS num_t,SUM(num_analisi) AS num_a\r\n" + 
      "                  FOR subcontractor\r\n" + 
      "                  IN ( \r\n" + 
      "                      'N' AS reg,\r\n" + 
      "                      'S' AS est\r\n" + 
      "                  )\r\n" + 
      "              )\r\n" + 
      "       )\r\n" + 
      "WHERE NVL(id_piano_monitoraggio,:idPianoMonitoraggio) = :idPianoMonitoraggio\r\n" + 
      "GROUP BY id_piano_monitoraggio,id_organismo_nocivo,nome_latino,sigla\r\n" + 
      "ORDER BY nome_latino";
  
  private static final String DELETE = "DELETE FROM iuf_t_piano_monitoraggio WHERE id_piano_monitoraggio = :idPianoMonitoraggio";

  private static final String INSERT_PREVISIONE_EST = "INSERT INTO iuf_t_previsione_on_est(id_previsione_on_est,id_specie_on," +
                                                            "num_visual,ore_visual,num_campioni,ore_campioni,num_analisi," +
                                                            "num_trappole,ore_trappole,ext_id_utente_aggiornamento," +
                                                            "data_ultimo_aggiornamento,id_piano_monitoraggio) \r\n" + 
                                                      "VALUES (seq_iuf_t_previsione_on_est.nextval," +
                                                            ":idOrganismoNocivo,:numVisual,:oreVisual,:numCampioni,:oreCampioni," +
                                                            ":numAnalisi,:numTrappole,:oreTrappole," + 
                                                            ":utente,SYSDATE,:idPianoMonitoraggio)";

  private static final String UPDATE_PREVISIONE_EST = "UPDATE iuf_t_previsione_on_est \r\n" +
                                                        "SET num_visual = :numVisual,\r\n" +
                                                        "    ore_visual = :oreVisual,\r\n" +
                                                        "    num_campioni = :numCampioni,\r\n" +
                                                        "    ore_campioni = :oreCampioni,\r\n" +
                                                        "    num_analisi = :numAnalisi,\r\n" +
                                                        "    num_trappole = :numTrappole,\r\n" +
                                                        "    ore_trappole = :oreTrappole,\r\n" +
                                                        "    ext_id_utente_aggiornamento = :utente,\r\n" +
                                                        "    data_ultimo_aggiornamento = SYSDATE \r\n" +
                                                        "WHERE id_piano_monitoraggio = :idPianoMonitoraggio \r\n" +
                                                        "AND id_specie_on = :idOrganismoNocivo";
  
  private static final String UPDATE_PREVISIONE_REG = "UPDATE iuf_t_previsione_on_reg \r\n" +
                                                        "SET num_visual = :numVisual,\r\n" +
                                                        "    ore_visual = :oreVisual,\r\n" +
                                                        "    num_campioni = :numCampioni,\r\n" +
                                                        "    ore_campioni = :oreCampioni,\r\n" +
                                                        "    num_analisi = :numAnalisi,\r\n" +
                                                        "    num_trappole = :numTrappole,\r\n" +
                                                        "    ore_trappole = :oreTrappole,\r\n" +
                                                        "    ext_id_utente_aggiornamento = :utente,\r\n" +
                                                        "    data_ultimo_aggiornamento = SYSDATE \r\n" +
                                                        "WHERE id_piano_monitoraggio = :idPianoMonitoraggio \r\n" +
                                                        "AND id_specie_on = :idOrganismoNocivo";

  private static final String INSERT_PREVISIONE_REG = "INSERT INTO iuf_t_previsione_on_reg(id_previsione_on_reg,id_specie_on," +
                                                            "num_visual,ore_visual,num_campioni,ore_campioni,num_analisi," +
                                                            "num_trappole,ore_trappole,ext_id_utente_aggiornamento," +
                                                            "data_ultimo_aggiornamento,id_piano_monitoraggio) \r\n" + 
                                                      "VALUES (seq_iuf_t_previsione_on_reg.nextval," +
                                                            ":idOrganismoNocivo,:numVisual,:oreVisual,:numCampioni,:oreCampioni," +
                                                            ":numAnalisi,:numTrappole,:oreTrappole," + 
                                                            ":utente,SYSDATE,:idPianoMonitoraggio)";

  public PrevisioneMonitoraggioDAO() {  }

  
  
  public List<PianoMonitoraggioDTO> findPianiAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findPianiAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_PIANI_ALL, mapParameterSource, PianoMonitoraggioDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_PIANI_ALL, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
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
   * @return
   * @throws InternalUnexpectedException
   */
  public PianoMonitoraggioDTO insertPiano(PianoMonitoraggioDTO piano) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertPiano]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String INSERT_PREVISIONE_REG = "INSERT INTO iuf_t_previsione_on_reg(id_previsione_on_reg, id_specie_on, ore_visual, num_campioni, num_analisi, num_trappole,\r\n" + 
                                  "       ext_id_utente_aggiornamento, data_ultimo_aggiornamento, id_piano_monitoraggio, num_visual, ore_campioni, ore_trappole)\r\n" + 
                                  " SELECT seq_iuf_t_previsione_on_reg.nextval, r.id_specie_on, r.ore_visual, r.num_campioni, r.num_analisi, r.num_trappole,\r\n" + 
                                  "       :ext_id_utente_aggiornamento, SYSDATE, :pianoMonitoraggio, r.num_visual, r.ore_campioni, r.ore_trappole\r\n" + 
                                  " FROM iuf_t_previsione_on_reg r\r\n" + 
                                  " WHERE r.id_piano_monitoraggio = (SELECT MAX(pr.id_piano_monitoraggio) \r\n" + 
                                  "                                   FROM iuf_t_previsione_on_reg pr, iuf_t_piano_monitoraggio p\r\n" + 
                                  "                                  WHERE pr.id_piano_monitoraggio = p.id_piano_monitoraggio\r\n" + 
                                  "                                     AND p.anno = :anno)";

    String INSERT_PREVISIONE_EST = "INSERT INTO iuf_t_previsione_on_est(id_previsione_on_est, id_specie_on, ore_visual, num_campioni, num_analisi, num_trappole,\r\n" + 
                                  "       ext_id_utente_aggiornamento, data_ultimo_aggiornamento, id_piano_monitoraggio, num_visual, ore_campioni, ore_trappole)\r\n" + 
                                  " SELECT seq_iuf_t_previsione_on_est.nextval, r.id_specie_on, r.ore_visual, r.num_campioni, r.num_analisi, r.num_trappole,\r\n" + 
                                  "       :ext_id_utente_aggiornamento, SYSDATE, :pianoMonitoraggio, r.num_visual, r.ore_campioni, r.ore_trappole\r\n" + 
                                  " FROM iuf_t_previsione_on_est r\r\n" + 
                                  " WHERE r.id_piano_monitoraggio = (SELECT MAX(pr.id_piano_monitoraggio) \r\n" + 
                                  "                                   FROM iuf_t_previsione_on_est pr, iuf_t_piano_monitoraggio p\r\n" + 
                                  "                                  WHERE pr.id_piano_monitoraggio = p.id_piano_monitoraggio\r\n" + 
                                  "                                     AND p.anno = :anno)";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {
      // Storicizzo eventuali altri piani dello stesso anno
      List<PianoMonitoraggioDTO> pianiDaStoricizzare = this.findByFilter(piano);
      if (pianiDaStoricizzare != null) {
        for (PianoMonitoraggioDTO p : pianiDaStoricizzare) {
          this.updateDataFineValidita(p.getIdPianoMonitoraggio());
        }
      }
      mapParameterSource.addValue("anno", piano.getAnno());
      mapParameterSource.addValue("note", piano.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", piano.getExtIdUtenteAggiornamento());
      namedParameterJdbcTemplate.update(INSERT_PIANO, mapParameterSource, holder, new String[] {"id_piano_monitoraggio"});
      piano.setIdPianoMonitoraggio(holder.getKey().intValue());
      
      mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("pianoMonitoraggio", piano.getIdPianoMonitoraggio());
      mapParameterSource.addValue("anno", piano.getAnno());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", piano.getExtIdUtenteAggiornamento());
      int count_reg = namedParameterJdbcTemplate.update(INSERT_PREVISIONE_REG, mapParameterSource);
      int count_est = namedParameterJdbcTemplate.update(INSERT_PREVISIONE_EST, mapParameterSource);
      return piano;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
          new LogVariable[]
              {}, INSERT_PIANO, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<PrevisioneMonitoraggioDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(SELECT_ALL, mapParameterSource, PrevisioneMonitoraggioDTO.class);
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

  public List<PianoMonitoraggioDTO> findByFilter(PianoMonitoraggioDTO piano) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("anno", piano.getAnno());
    String select = "SELECT * FROM iuf_t_piano_monitoraggio p WHERE p.anno = :anno AND p.data_fine_validita IS NULL ORDER BY p.id_piano_monitoraggio";

    try
    {
      List<PianoMonitoraggioDTO> list = queryForList(select, mapParameterSource, PianoMonitoraggioDTO.class);
      return list;
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

  public PianoMonitoraggioDTO findPianoById(Integer idPianoMonitoraggio) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findPianoById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idPianoMonitoraggio", idPianoMonitoraggio);
    
    try
    {
      PianoMonitoraggioDTO piano = queryForObject(SELECT_PIANO_BY_ID, mapParameterSource, PianoMonitoraggioDTO.class);
      return piano;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_PIANO_BY_ID, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public void updateDataFineValidita(Integer idPianoMonitoraggio) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateDataFineValidita";
    
    String UPDATE = "UPDATE iuf_t_piano_monitoraggio SET data_fine_validita = SYSDATE, data_ultimo_aggiornamento = SYSDATE " +
                    " WHERE id_piano_monitoraggio = :idPianoMonitoraggio";  

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
   
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idPianoMonitoraggio", idPianoMonitoraggio);
    try
    {
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, UPDATE, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public void remove(Integer idPianoMonitoraggio) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idPianoMonitoraggio", idPianoMonitoraggio);
    
    try
    {
      logger.debug(DELETE);
      logger.debug("idPianoMonitoraggio=" + idPianoMonitoraggio);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, DELETE, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw new InternalUnexpectedException(t);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<PrevisioneMonitoraggioDTO> findPrevisioneByIdPiano(Integer idPianoMonitoraggio) throws InternalUnexpectedException {
    
    final String THIS_METHOD = "findPrevisioneByIdPiano";
    
    List<PrevisioneMonitoraggioDTO> list = null;
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idPianoMonitoraggio", idPianoMonitoraggio);
    
    try
    {
      logger.debug(SELECT_PREVISIONE_BY_PIANO);
      logger.debug("idPianoMonitoraggio=" + idPianoMonitoraggio);
      list = queryForList(SELECT_PREVISIONE_BY_PIANO, mapParameterSource, PrevisioneMonitoraggioDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_PREVISIONE_BY_PIANO, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw new InternalUnexpectedException(t);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public long countPrevisioniByIdOrganismoNocivo(Integer idOrganismoNocivo) throws InternalUnexpectedException {
    
    final String THIS_METHOD = "findPrevisioneByIdOrganismoNocivo";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
    final String SELECT = "SELECT COUNT(*)\r\n" +
                            "FROM (SELECT id_specie_on FROM iuf_t_previsione_on_est e, iuf_t_piano_monitoraggio p\r\n" + 
                            "       WHERE e.id_piano_monitoraggio = p.id_piano_monitoraggio\r\n" + 
                            "         AND TRUNC(SYSDATE) BETWEEN TRUNC(p.data_inizio_validita) AND NVL(p.data_fine_validita,SYSDATE)\r\n" + 
                            "      UNION\r\n" + 
                            "      SELECT id_specie_on FROM iuf_t_previsione_on_reg r, iuf_t_piano_monitoraggio p\r\n" + 
                            "       WHERE r.id_piano_monitoraggio = p.id_piano_monitoraggio\r\n" + 
                            "         AND TRUNC(SYSDATE) BETWEEN TRUNC(p.data_inizio_validita) AND NVL(p.data_fine_validita,SYSDATE)\r\n" + 
                            "     )\r\n" + 
                            "WHERE id_specie_on = :idOrganismoNocivo";
    try
    {
      logger.debug(SELECT);
      logger.debug("idOrganismoNocivo=" + idOrganismoNocivo);
      long result = queryForLong(SELECT, mapParameterSource);
      return result;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw new InternalUnexpectedException(t);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public PrevisioneMonitoraggioDTO savePrevisione(PrevisioneMonitoraggioDTO previsione, Long idUtenteLogin) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertPrevisione]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    boolean stepOneOk = false;
    try  {
      try {
        // insert official (reg)
        mapParameterSource.addValue("idPianoMonitoraggio", previsione.getIdPianoMonitoraggio());
        mapParameterSource.addValue("idOrganismoNocivo", previsione.getIdOrganismoNocivo());
        mapParameterSource.addValue("numVisual", previsione.getNumVisualReg());
        mapParameterSource.addValue("oreVisual", previsione.getOreVisualReg());
        mapParameterSource.addValue("numCampioni", previsione.getNumCampioniReg());
        mapParameterSource.addValue("oreCampioni", previsione.getOreCampioniReg());
        mapParameterSource.addValue("numAnalisi", previsione.getNumAnalisiReg());
        mapParameterSource.addValue("numTrappole", previsione.getNumTrappoleReg());
        mapParameterSource.addValue("oreTrappole", previsione.getOreTrappoleReg());
        mapParameterSource.addValue("utente", idUtenteLogin);
        logger.debug(INSERT_PREVISIONE_REG);
        namedParameterJdbcTemplate.update(INSERT_PREVISIONE_REG, mapParameterSource, holder, new String[] {"id_previsione_on_reg"});
      } catch (DuplicateKeyException dke) {
          // update
          namedParameterJdbcTemplate.update(UPDATE_PREVISIONE_REG, mapParameterSource);
      }
      stepOneOk = true;
      PrevisioneMonitoraggioDTO previsioneOut = new PrevisioneMonitoraggioDTO();
      BeanUtils.copyProperties(previsione, previsioneOut);
      try {
        // insert subcontractor (est)
        mapParameterSource.addValue("idPianoMonitoraggio", previsione.getIdPianoMonitoraggio());
        mapParameterSource.addValue("idOrganismoNocivo", previsione.getIdOrganismoNocivo());
        mapParameterSource.addValue("numVisual", previsione.getNumVisualEst());
        mapParameterSource.addValue("oreVisual", previsione.getOreVisualEst());
        mapParameterSource.addValue("numCampioni", previsione.getNumCampioniEst());
        mapParameterSource.addValue("oreCampioni", previsione.getOreCampioniEst());
        mapParameterSource.addValue("numAnalisi", previsione.getNumAnalisiEst());
        mapParameterSource.addValue("numTrappole", previsione.getNumTrappoleEst());
        mapParameterSource.addValue("oreTrappole", previsione.getOreTrappoleEst());
        mapParameterSource.addValue("utente", idUtenteLogin);
        logger.debug(INSERT_PREVISIONE_EST);
        namedParameterJdbcTemplate.update(INSERT_PREVISIONE_EST, mapParameterSource, holder, new String[] {"id_previsione_on_est"});
      } catch (DuplicateKeyException dke) {
          // update
          namedParameterJdbcTemplate.update(UPDATE_PREVISIONE_EST, mapParameterSource);
      }
      return previsioneOut;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
          new LogVariable[]
              {}, (stepOneOk)?INSERT_PREVISIONE_REG:INSERT_PREVISIONE_EST, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
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
