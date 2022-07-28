package it.csi.iuffi.iuffiweb.service;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.FotoApiDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.GpsDTO;
import oracle.jdbc.OracleTypes;

public class GpsFotoDAO extends BaseDAO
{
  
  private static final String THIS_CLASS = GpsFotoDAO.class.getSimpleName();
  private LobHandler lobHandler;

  public GpsFotoDAO(){  }

   
  //private static final String SELECT_FOTO_BY_ID = "select c.* from iuf_t_foto c where c.id_foto =:id";

  private static final String SELECT_GPS_BY_ID ="";
  
  private static final String SELECT_ALL_FOTO = "SELECT tr.*, da.descrizione_comune descr_comune \r\n" +
      "  FROM (SELECT t.id_foto as id_foto,\r\n" + 
      "               t.data_foto,\r\n" + 
      "               t.nome_file,\r\n" + 
      "               t.tag," + 
      "               t.note, " +              
      "               t.ext_id_utente_aggiornamento,  " +            
      "               aa.nome_volgare as specie,\r\n" + 
      "               bb.dettaglio_tipo_area as area,\r\n" + 
      "                '' as trappola,\r\n" + 
      "                '' as campione,\r\n" + 
      "               a.id_ispezione_visiva,\r\n" + 
      "               0 AS id_campionamento,\r\n" + 
      "               0 AS id_trappolaggio,\r\n" + 
      "               g.id_rilevazione,\r\n" + 
      "               g.id_missione,\r\n" + 
      "               'I' AS tipologia,\r\n" + 
      "               g.id_tipo_area,\r\n" + 
      "               d.id_anagrafica as id_ispettore_evento,\r\n" + 
      "               rr.cognome || ' ' ||rr.nome as ispettore_evento,\r\n" +
      "               rr.id_ente,\r\n" +
      "               d.id_specie_vegetale AS id_specie,\r\n" + 
      "               0 AS id_tipo_campione,\r\n" + 
      "               0 AS id_trappola,\r\n" + 
      "               d.istat_comune,\r\n" + 
      "               d.latitudine,\r\n" + 
      "               d.longitudine,\r\n" + 
      "               h.id_ispettore_assegnato,\r\n" + 
      "               d.data_ora_inizio AS data_acquisizione,\r\n" +       
      "               UPPER(jj.cognome || ' ' ||jj.nome) as ispettore_assegnato_m,\r\n" + 
      "               d.id_ispezione as id_record,\r\n" +
      "               (SELECT LISTAGG(a.nome_latino, '; ') " + 
      "                   WITHIN GROUP (ORDER BY a.nome_latino) " + 
      "                    from IUF_R_ISP_VISIVA_SPEC_ON t, " + 
      "                   iuf_d_organismo_nocivo a where t.id_specie_on=a.id_organismo_nocivo " + 
      "                   and t.id_ispezione=d.id_ispezione) as organismi \r\n" +
      "          FROM IUF_T_FOTO             t,\r\n" + 
      "               iuf_r_isp_visiva_foto  a,\r\n" + 
      "               iuf_t_ispezione_visiva d,\r\n" + 
      "               iuf_t_rilevazione      g,\r\n" + 
      "               iuf_t_missione         h,\r\n" + 
      "               iuf_d_specie_vegetale aa,\r\n" + 
      "               iuf_d_tipo_area bb,\r\n" + 
      "               iuf_t_anagrafica jj,\r\n" + 
      "               iuf_t_anagrafica rr\r\n" + 
      "        WHERE t.id_foto = a.id_foto\r\n" + 
      "           AND d.id_ispezione = a.id_ispezione_visiva\r\n" + 
      "           AND d.id_rilevazione = g.id_rilevazione\r\n" + 
      "           AND g.id_missione = h.id_missione\r\n" + 
      "           AND aa.id_specie_vegetale=d.id_specie_vegetale\r\n" + 
      "           AND bb.id_tipo_area=g.id_tipo_area\r\n" + 
      "           AND jj.id_anagrafica=h.id_ispettore_assegnato\r\n" + 
      "            AND rr.id_anagrafica=d.id_anagrafica \r\n" + 
      "        UNION ALL\r\n" + 
      "        SELECT z.id_foto as id_foto,\r\n" + 
      "               z.data_foto,\r\n" + 
      "               z.nome_file,\r\n" + 
      "               z.tag," + 
      "               z.note, " +              
      "               z.ext_id_utente_aggiornamento,  " +            
      "               cc.nome_volgare as specie,\r\n" + 
      "               dd.dettaglio_tipo_area as area,\r\n" + 
      "                '' as trappola, \r\n" + 
      "               hh.tipologia_campione as campione,                   \r\n" + 
      "               0 AS id_ispezione_visiva,\r\n" + 
      "               b.id_campionamento,\r\n" + 
      "               0 AS id_trappolaggio,\r\n" + 
      "               g.id_rilevazione,\r\n" + 
      "               g.id_missione,\r\n" + 
      "               'C' AS tipologia,\r\n" + 
      "               g.id_tipo_area,\r\n" + 
      "               e.id_anagrafica as id_ispettore_evento,\r\n" + 
      "                rr.cognome || ' ' ||rr.nome as ispettore_evento,\r\n" + 
      "               rr.id_ente,\r\n" +
      "               e.id_specie_vegetale AS id_specie,\r\n" + 
      "               e.id_tipo_campione AS id_tipo_campione,\r\n" + 
      "               0 AS id_trappola,\r\n" + 
      "               e.istat_comune,\r\n" + 
      "               e.latitudine,\r\n" + 
      "               e.longitudine,\r\n" + 
      "               h.id_ispettore_assegnato,\r\n" + 
      "               e.data_ora_inizio AS data_acquisizione,\r\n" +            
      "               UPPER(jj.cognome || ' ' ||jj.nome) as ispettore_assegnato_m,\r\n" + 
      "               e.id_campionamento as id_record,\r\n" +
      "               (SELECT LISTAGG(a.nome_latino, '; ') " + 
      "               WITHIN GROUP (ORDER BY a.nome_latino) " +
      "                from IUF_R_CAMPIONAMENTO_SPEC_ON ggg, " +
      "               iuf_d_organismo_nocivo a where ggg.id_specie_on=a.id_organismo_nocivo " +
      "               and ggg.id_campionamento=e.id_campionamento) as organismi \r\n" +
      "          FROM IUF_T_FOTO          z,\r\n" + 
      "               iuf_r_campione_foto b,\r\n" + 
      "               iuf_t_campionamento e,\r\n" + 
      "               iuf_t_rilevazione   g,\r\n" + 
      "               iuf_t_missione      h,\r\n" + 
      "               iuf_d_specie_vegetale cc,\r\n" + 
      "               iuf_d_tipo_area dd,\r\n" + 
      "               iuf_d_tipo_campione hh,\r\n" + 
      "               iuf_t_anagrafica jj,\r\n" + 
      "               iuf_t_anagrafica rr\r\n" + 
      "         WHERE z.id_foto = b.id_foto\r\n" + 
      "           AND e.id_campionamento = b.id_campionamento\r\n" + 
      "           AND e.id_rilevazione = g.id_rilevazione\r\n" + 
      "           AND g.id_missione = h.id_missione\r\n" + 
      "           AND cc.id_specie_vegetale=e.id_specie_vegetale\r\n" + 
      "           AND dd.id_tipo_area=g.id_tipo_area\r\n" + 
      "           AND hh.id_tipo_campione=e.id_tipo_campione\r\n" + 
      "           AND jj.id_anagrafica=h.id_ispettore_assegnato\r\n" + 
      "           AND rr.id_anagrafica=e.id_anagrafica \r\n" + 
      "        UNION ALL\r\n" + 
      "        SELECT m.id_foto as id_foto,\r\n" + 
      "               m.data_foto,\r\n" + 
      "               m.nome_file,\r\n" + 
      "               m.tag,\r\n" + 
      "               m.note,\r\n" +
      "               m.ext_id_utente_aggiornamento,\r\n" +            
      "               sv.nome_volgare as specie, \r\n" +  
      "               ff.dettaglio_tipo_area as area,\r\n" + 
      "               gg.tipologia_trappola as trappola, \r\n" + 
      "               '' as campione,                          \r\n" + 
      "               0 AS id_ispezione_visiva,\r\n" + 
      "               0 AS id_campionamento,\r\n" + 
      "               c.id_trappolaggio,\r\n" + 
      "               g.id_rilevazione,\r\n" + 
      "               g.id_missione,\r\n" + 
      "               'T' AS tipologia,\r\n" + 
      "               g.id_tipo_area,\r\n" + 
      "               f.id_anagrafica as id_ispettore_evento,\r\n" + 
      "                rr.cognome || ' ' ||rr.nome as ispettore_evento,\r\n" + 
      "               rr.id_ente,\r\n" +
      "               0 AS id_specie,\r\n" + 
      "               0 AS id_tipo_campione,\r\n" + 
      "               tt.id_tipo_trappola AS id_trappola,\r\n" + 
      "               f.istat_comune, \r\n" + 
      "               tt.latitudine,\r\n" + 
      "               tt.longitudine,\r\n" + 
      "                h.id_ispettore_assegnato,\r\n" + 
      "               f.data_ora_inizio AS data_acquisizione,\r\n" +            
      "                UPPER(jj.cognome || ' ' ||jj.nome) as ispettore_assegnato_m,\r\n" + 
      "               f.id_trappolaggio as id_record,\r\n" +
      "                o.nome_latino as organismi \r\n" +
      "          FROM IUF_T_FOTO              m,\r\n" + 
      "               iuf_r_trappolaggio_foto c,\r\n" + 
      "               iuf_t_trappolaggio      f,\r\n" + 
      "               iuf_t_rilevazione       g,\r\n" + 
      "               iuf_t_missione          h,\r\n" + 
      "               iuf_d_tipo_area        ff,\r\n" + 
      "               iuf_d_tipo_trappola gg,\r\n" + 
      "               iuf_t_trappola tt,\r\n" + 
      "               iuf_t_anagrafica jj,\r\n" + 
      "               iuf_t_anagrafica rr,\r\n" +
      "               iuf_d_specie_vegetale sv,\r\n" +
      "               iuf_d_organismo_nocivo o \r\n" +
      "         WHERE m.id_foto = c.id_foto\r\n" + 
      "           AND f.id_trappola = tt.id_trappola\r\n" + 
      "           AND f.id_trappolaggio = c.id_trappolaggio\r\n" + 
      "           AND f.id_rilevazione = g.id_rilevazione\r\n" + 
      "           AND g.id_missione = h.id_missione\r\n" + 
      "           AND ff.id_tipo_area (+) = g.id_tipo_area\r\n" + 
      "           AND gg.id_tipo_trappola = tt.id_tipo_trappola\r\n" + 
      "           AND jj.id_anagrafica=h.id_ispettore_assegnato\r\n" +
      "           AND tt.id_specie_veg = sv.id_specie_vegetale (+) \r\n" +
      "           AND f.id_organismo_nocivo = o.id_organismo_nocivo (+) \r\n" +
      "           AND rr.id_anagrafica=f.id_anagrafica ) tr, smrgaa_v_dati_amministrativi da \r\n" + 
      "         WHERE tr.istat_comune = da.istat_comune (+) \r\n" +
      "           AND tr.id_foto IS NOT NULL\r\n";
  
  private static final String SELECT_ALL_GPS = "SELECT tr.*, da.descrizione_comune descr_comune\r\n" + 
      "  FROM (SELECT aa.nome_volgare AS specie,\r\n" + 
      "               bb.dettaglio_tipo_area AS area,\r\n" + 
      "               '' AS trappola,\r\n" + 
      "               '' AS campione,\r\n" + 
      "               0 AS id_campionamento,\r\n" + 
      "               0 AS id_trappolaggio,\r\n" + 
      "               d.id_ispezione AS id_ispezione_visiva,\r\n" + 
      "               g.id_rilevazione,\r\n" + 
      "               g.id_missione,\r\n" + 
      "               'I' AS tipologia,\r\n" + 
      "               g.id_tipo_area,\r\n" + 
      "               d.id_anagrafica,\r\n" + 
      "               an.id_ente,\r\n" + 
      "               an.cognome || ' ' || an.nome AS ispettore_evento, \r\n" +
      "               h.id_ispettore_assegnato,\r\n" + 
      "               d.id_specie_vegetale AS id_specie,\r\n" + 
      "               0 AS id_tipo_campione,\r\n" + 
      "               0 AS id_tipo_trappola,\r\n" + 
      "               d.istat_comune,\r\n" + 
      "               d.latitudine,\r\n" + 
      "               d.longitudine,\r\n" + 
      "               d.data_ora_inizio AS data_acquisizione,\r\n" + 
      "               d.id_ispezione AS id_record,\r\n" + 
      "               (SELECT LISTAGG(a.nome_latino, '; ') WITHIN GROUP(ORDER BY a.nome_latino)\r\n" + 
      "                  FROM IUF_R_ISP_VISIVA_SPEC_ON t, iuf_d_organismo_nocivo a\r\n" + 
      "                 WHERE t.id_specie_on = a.id_organismo_nocivo\r\n" + 
      "                   AND t.id_ispezione = d.id_ispezione) AS organismi\r\n" + 
      "          FROM iuf_t_ispezione_visiva d,\r\n" + 
      "               iuf_t_rilevazione      g,\r\n" + 
      "               iuf_t_missione         h,\r\n" + 
      "               iuf_d_specie_vegetale  aa,\r\n" + 
      "               iuf_d_tipo_area        bb,\r\n" + 
      "               iuf_t_anagrafica an\r\n" + 
      "         WHERE d.id_rilevazione = g.id_rilevazione\r\n" + 
      "           AND g.id_missione = h.id_missione\r\n" + 
      "           AND aa.id_specie_vegetale = d.id_specie_vegetale\r\n" + 
      "           AND bb.id_tipo_area = g.id_tipo_area\r\n" + 
      "           AND d.id_anagrafica = an.id_anagrafica\r\n" + 
      "        UNION ALL\r\n" + 
      "        SELECT cc.nome_volgare AS specie,\r\n" + 
      "               dd.dettaglio_tipo_area AS area,\r\n" + 
      "               '' AS trappola,\r\n" + 
      "               hh.tipologia_campione AS campione,\r\n" + 
      "               e.id_campionamento AS id_campionamento,\r\n" + 
      "               0 AS id_trappolaggio,\r\n" + 
      "               0 AS id_ispezione_visiva,\r\n" + 
      "               g.id_rilevazione,\r\n" + 
      "               g.id_missione,\r\n" + 
      "               'C' AS tipologia,\r\n" + 
      "               g.id_tipo_area,\r\n" + 
      "               e.id_anagrafica,\r\n" + 
      "               an.id_ente,\r\n" + 
      "               an.cognome || ' ' || an.nome AS ispettore_evento, \r\n" +
      "               h.id_ispettore_assegnato,\r\n" + 
      "               e.id_specie_vegetale AS id_specie,\r\n" + 
      "               e.id_tipo_campione AS id_tipo_campione,\r\n" + 
      "               0 AS id_tipo_trappola,\r\n" + 
      "               e.istat_comune,\r\n" + 
      "               e.latitudine,\r\n" + 
      "               e.longitudine,\r\n" + 
      "               e.data_ora_inizio AS data_acquisizione,\r\n" + 
      "               e.id_campionamento AS id_record,\r\n" + 
      "               (SELECT LISTAGG(a.nome_latino, '; ') WITHIN GROUP(ORDER BY a.nome_latino)\r\n" + 
      "                  FROM IUF_R_CAMPIONAMENTO_SPEC_ON ggg,\r\n" + 
      "                       iuf_d_organismo_nocivo      a\r\n" + 
      "                 WHERE ggg.id_specie_on = a.id_organismo_nocivo\r\n" + 
      "                   AND ggg.id_campionamento = e.id_campionamento) AS organismi\r\n" + 
      "          FROM iuf_t_campionamento   e,\r\n" + 
      "               iuf_t_rilevazione     g,\r\n" + 
      "               iuf_t_missione        h,\r\n" + 
      "               iuf_d_specie_vegetale cc,\r\n" + 
      "               iuf_d_tipo_area       dd,\r\n" + 
      "               iuf_d_tipo_campione   hh,\r\n" + 
      "               iuf_t_anagrafica an\r\n" + 
      "         WHERE e.id_rilevazione = g.id_rilevazione\r\n" + 
      "           AND g.id_missione = h.id_missione\r\n" + 
      "           AND cc.id_specie_vegetale = e.id_specie_vegetale\r\n" + 
      "           AND dd.id_tipo_area = g.id_tipo_area\r\n" + 
      "           AND hh.id_tipo_campione = e.id_tipo_campione\r\n" + 
      "           AND e.id_anagrafica = an.id_anagrafica\r\n" + 
      "        UNION ALL\r\n" + 
      "        SELECT sv.nome_volgare AS specie,\r\n" + 
      "               ff.dettaglio_tipo_area AS area,\r\n" + 
      "               gg.tipologia_trappola AS trappola,\r\n" + 
      "               '' AS campione,\r\n" + 
      "               0 AS id_campionamento,\r\n" + 
      "               c.id_trappola AS id_trappolaggio,\r\n" + 
      "               0 AS id_ispezione_visiva,\r\n" + 
      "               g.id_rilevazione,\r\n" + 
      "               g.id_missione,\r\n" + 
      "               'T' AS tipologia,\r\n" + 
      "               g.id_tipo_area,\r\n" + 
      "               f.id_anagrafica,\r\n" + 
      "               an.id_ente,\r\n" +
      "               an.cognome || ' ' || an.nome AS ispettore_evento, \r\n" +
      "               h.id_ispettore_assegnato,\r\n" + 
      "               0 AS id_specie,\r\n" + 
      "               0 AS id_tipo_campione,\r\n" + 
      "               c.id_tipo_trappola AS id_trappola,\r\n" + 
      "               f.istat_comune,\r\n" + 
      "               c.latitudine,\r\n" + 
      "               c.longitudine,\r\n" + 
      "               f.data_ora_inizio AS data_acquisizione,\r\n" + 
      "               f.id_trappolaggio AS id_record,\r\n" + 
      "               o.nome_latino AS organismi\r\n" + 
      "          FROM iuf_t_trappola         c,\r\n" + 
      "               iuf_t_trappolaggio     f,\r\n" + 
      "               iuf_t_rilevazione      g,\r\n" + 
      "               iuf_t_missione         h,\r\n" + 
      "               iuf_d_tipo_area        ff,\r\n" + 
      "               iuf_d_tipo_trappola    gg,\r\n" + 
      "               iuf_d_specie_vegetale  sv,\r\n" + 
      "               iuf_d_organismo_nocivo o,\r\n" + 
      "               iuf_t_anagrafica an\r\n" + 
      "         WHERE f.id_trappola = c.id_trappola\r\n" + 
      "           AND f.id_rilevazione = g.id_rilevazione\r\n" + 
      "           AND g.id_missione = h.id_missione\r\n" + 
      "           AND ff.id_tipo_area = g.id_tipo_area\r\n" + 
      "           AND c.id_specie_veg = sv.id_specie_vegetale(+)\r\n" + 
      "           AND f.id_organismo_nocivo = o.id_organismo_nocivo(+)\r\n" + 
      "           AND gg.id_tipo_trappola = c.id_tipo_trappola\r\n" + 
      "           AND f.id_anagrafica = an.id_anagrafica) tr,\r\n" + 
      "       smrgaa_v_dati_amministrativi da\r\n" + 
      " WHERE tr.istat_comune = da.istat_comune(+)\r\n" + 
      "   AND tr.latitudine IS NOT NULL\r\n";
/*
  private static final String FILTER_ORGANISMO_NOCIVO_MULTIPLE_1 = "AND (EXISTS (SELECT 1 FROM iuf_r_isp_visiva_spec_on vso\r\n" + 
      "                WHERE vso.id_ispezione = tr.id_ispezione_visiva\r\n" + 
      "                  AND vso.id_specie_on IN (%s))\r\n";

      private static final String FILTER_ORGANISMO_NOCIVO_MULTIPLE_2 =" OR\r\n" + 
      "        EXISTS (SELECT 1 FROM iuf_r_campionamento_spec_on cso\r\n" + 
      "                 WHERE cso.id_campionamento = tr.id_campionamento\r\n" + 
      "                   AND cso.id_specie_on IN (%s)))";
*/    
      private static final String SELECT_ALL_FOTO_DETTAGLIO = "SELECT tr.*\r\n" + 
          "  FROM (SELECT t.id_foto as id_foto,\r\n" + 
          "               t.data_foto,\r\n" + 
          "               t.nome_file,\r\n" + 
          "               t.tag," + 
          "               t.note, " +              
          "               t.foto, " +   
          "               t.ext_id_utente_aggiornamento,  " +            
          "               aa.genere_specie ||'-'|| aa.nome_volgare as specie,\r\n" + 
          "               bb.desc_tipo_area ||'-'|| bb.dettaglio_tipo_area as area,\r\n" + 
          "                '' as trappola,\r\n" + 
          "                '' as campione,\r\n" + 
          "               a.id_ispezione_visiva,\r\n" + 
          "               0 AS id_campionamento,\r\n" + 
          "               0 AS id_trappolaggio,\r\n" + 
          "               g.id_rilevazione,\r\n" + 
          "               g.id_missione,\r\n" + 
          "               'I' AS tipologia,\r\n" + 
          "               g.id_tipo_area,\r\n" + 
          "               d.id_anagrafica as id_ispettore_evento,\r\n" + 
          "               rr.cognome || ' ' ||rr.nome as ispettore_evento,\r\n" + 
          "               d.id_specie_vegetale AS id_specie,\r\n" + 
          "               0 AS id_tipo_campione,\r\n" + 
          "               0 AS id_trappola,\r\n" + 
          "               d.istat_comune,\r\n" + 
          "               d.latitudine,\r\n" + 
          "               d.longitudine,\r\n" + 
          "               h.id_ispettore_assegnato as id_ispettore_assegnato,\r\n" + 
          "               UPPER(jj.cognome || ' ' ||jj.nome) as ispettore_assegnato_m,\r\n" + 
          "               (SELECT LISTAGG(a.sigla, '; ') " + 
          "                   WITHIN GROUP (ORDER BY a.sigla) " + 
          "                    from IUF_R_ISP_VISIVA_SPEC_ON t, " + 
          "                   iuf_d_organismo_nocivo a where t.id_specie_on=a.id_organismo_nocivo " + 
          "                   and t.id_ispezione=d.id_ispezione) as organismi    " +           
          "          FROM iuf_t_foto             t,\r\n" + 
          "               iuf_r_isp_visiva_foto  a,\r\n" + 
          "               iuf_t_ispezione_visiva d,\r\n" + 
          "               iuf_t_rilevazione      g,\r\n" + 
          "               iuf_t_missione         h,\r\n" + 
          "               iuf_d_specie_vegetale aa,\r\n" + 
          "               iuf_d_tipo_area bb,\r\n" + 
          "               iuf_t_anagrafica jj,\r\n" + 
          "               iuf_t_anagrafica rr\r\n" + 
          "        WHERE t.id_foto = a.id_foto\r\n" + 
          "          AND d.id_ispezione = a.id_ispezione_visiva\r\n" + 
          "          AND d.id_rilevazione = g.id_rilevazione\r\n" + 
          "          AND g.id_missione = h.id_missione\r\n" + 
          "          AND aa.id_specie_vegetale=d.id_specie_vegetale\r\n" + 
          "          AND bb.id_tipo_area=g.id_tipo_area\r\n" + 
          "          AND jj.id_anagrafica=h.id_ispettore_assegnato\r\n" + 
          "          AND rr.id_anagrafica=d.id_anagrafica\r\n" + 
          "        UNION ALL\r\n" + 
          "        SELECT z.id_foto as id_foto,\r\n" + 
          "               z.data_foto,\r\n" + 
          "               z.nome_file,\r\n" + 
          "               z.tag," + 
          "               z.note, " +              
          "               z.foto, " +              
          "               z.ext_id_utente_aggiornamento,  " +            
          "               cc.genere_specie||'-'||cc.nome_volgare as specie,\r\n" + 
          "               dd.desc_tipo_area||'-'||dd.dettaglio_tipo_area as area,\r\n" + 
          "                '' as trappola, \r\n" + 
          "               hh.tipologia_campione as campione,                   \r\n" + 
          "               0 AS id_ispezione_visiva,\r\n" + 
          "               b.id_campionamento,\r\n" + 
          "               0 AS id_trappolaggio,\r\n" + 
          "               g.id_rilevazione,\r\n" + 
          "               g.id_missione,\r\n" + 
          "               'C' AS tipologia,\r\n" + 
          "               g.id_tipo_area,\r\n" + 
          "               e.id_anagrafica as id_ispettore_evento,\r\n" + 
          "                rr.cognome || ' ' ||rr.nome as ispettore_evento,\r\n" + 
          "               e.id_specie_vegetale AS id_specie,\r\n" + 
          "               e.id_tipo_campione AS id_tipo_campione,\r\n" + 
          "               0 AS id_trappola,\r\n" + 
          "               e.istat_comune,\r\n" + 
          "               e.latitudine,\r\n" + 
          "               e.longitudine,\r\n" + 
          "               h.id_ispettore_assegnato as id_ispettore_assegnato,\r\n" + 
          "               UPPER(jj.cognome || ' ' ||jj.nome) as ispettore_assegnato_m,\r\n" + 
          "               (SELECT LISTAGG(a.sigla, '; ') " + 
          "               WITHIN GROUP (ORDER BY a.sigla) " +
          "                from IUF_R_CAMPIONAMENTO_SPEC_ON ggg, " +
          "               iuf_d_organismo_nocivo a where ggg.id_specie_on=a.id_organismo_nocivo " +
          "               and ggg.id_campionamento=e.id_campionamento) as organismi " +          
          "          FROM iuf_t_foto          z,\r\n" + 
          "               iuf_r_campione_foto b,\r\n" + 
          "               iuf_t_campionamento e,\r\n" + 
          "               iuf_t_rilevazione   g,\r\n" + 
          "               iuf_t_missione      h,\r\n" + 
          "               iuf_d_specie_vegetale cc,\r\n" + 
          "               iuf_d_tipo_area dd,\r\n" + 
          "               iuf_d_tipo_campione hh,\r\n" + 
          "               iuf_t_anagrafica jj,\r\n" + 
          "               iuf_t_anagrafica rr\r\n" + 
          "         WHERE z.id_foto = b.id_foto\r\n" + 
          "           AND e.id_campionamento = b.id_campionamento\r\n" + 
          "           AND e.id_rilevazione = g.id_rilevazione\r\n" + 
          "           AND g.id_missione = h.id_missione\r\n" + 
          "           AND cc.id_specie_vegetale=e.id_specie_vegetale\r\n" + 
          "           AND dd.id_tipo_area=g.id_tipo_area\r\n" + 
          "           AND hh.id_tipo_campione=e.id_tipo_campione\r\n" + 
          "           AND jj.id_anagrafica=h.id_ispettore_assegnato\r\n" + 
          "           AND rr.id_anagrafica=e.id_anagrafica \r\n" + 
          "        UNION ALL\r\n" + 
          "        SELECT m.id_foto as id_foto,\r\n" + 
          "               m.data_foto,\r\n" + 
          "               m.nome_file,\r\n" + 
          "               m.tag," + 
          "               m.note, " +              
          "               m.foto, " +              
          "               m.ext_id_utente_aggiornamento,  " +            
          "               sv.genere_specie || '-' || sv.nome_volgare AS specie,\r\n" + 
          "               ff.desc_tipo_area||'-'||ff.dettaglio_tipo_area as area,\r\n" + 
          "               gg.tipologia_trappola as trappola, \r\n" + 
          "               '' as campione,                          \r\n" + 
          "               0 AS id_ispezione_visiva,\r\n" + 
          "               0 AS id_campionamento,\r\n" + 
          "               c.id_trappolaggio,\r\n" + 
          "               g.id_rilevazione,\r\n" + 
          "               g.id_missione,\r\n" + 
          "               'T' AS tipologia,\r\n" + 
          "               g.id_tipo_area,\r\n" + 
          "               f.id_anagrafica as id_ispettore_evento,\r\n" + 
          "                rr.cognome || ' ' ||rr.nome as ispettore_evento,\r\n" + 
          "               sv.id_specie_vegetale AS id_specie,\r\n" + 
          "               0 AS id_tipo_campione,\r\n" + 
          "               f.id_trappola AS id_trappola,\r\n" + 
          "               f.istat_comune, \r\n" + 
          "               tt.latitudine,\r\n" + 
          "               tt.longitudine,\r\n" + 
          "               h.id_ispettore_assegnato as id_ispettore_assegnato,\r\n" + 
          "               UPPER(jj.cognome || ' ' ||jj.nome) as ispettore_assegnato_m,\r\n" + 
          "               o.sigla AS organismi " +
          "          FROM iuf_t_foto              m,\r\n" +
          "               iuf_r_trappolaggio_foto c,\r\n" + 
          "               iuf_t_trappolaggio      f,\r\n" + 
          "               iuf_t_rilevazione       g,\r\n" + 
          "               iuf_t_missione          h,\r\n" + 
          "               iuf_d_tipo_area        ff,\r\n" + 
          "               iuf_d_tipo_trappola gg,\r\n" + 
          "               iuf_t_trappola tt,\r\n" + 
          "               iuf_t_anagrafica jj,\r\n" + 
          "               iuf_t_anagrafica rr,\r\n" +
          "               iuf_d_organismo_nocivo o,\r\n" +
          "               iuf_d_specie_vegetale sv\r\n" +
          "         WHERE m.id_foto = c.id_foto\r\n" + 
          "           AND f.id_trappola = tt.id_trappola\r\n" + 
          "           AND f.id_trappolaggio = c.id_trappolaggio\r\n" + 
          "           AND f.id_rilevazione = g.id_rilevazione\r\n" + 
          "           AND g.id_missione = h.id_missione\r\n" + 
          "           AND ff.id_tipo_area=g.id_tipo_area\r\n" + 
          "           AND gg.id_tipo_trappola = tt.id_tipo_trappola" + 
          "           AND jj.id_anagrafica=h.id_ispettore_assegnato\r\n" + 
          "           AND rr.id_anagrafica=f.id_anagrafica\r\n" +
          "           AND f.id_organismo_nocivo = o.id_organismo_nocivo (+)\r\n" +
          "           AND tt.id_specie_veg = sv.id_specie_vegetale (+)\r\n" +
          "         ) tr \r\n" +
          "           WHERE tr.id_foto IS NOT NULL\r\n";

      private static final String INSERT = "INSERT INTO iuf_t_foto(" +
         "id_foto," + 
         "foto," + 
         "nome_file," +
         "data_foto," +
         "tag," +
         "note," +
         "EXT_ID_UTENTE_AGGIORNAMENTO," +
         "DATA_ULTIMO_AGGIORNAMENTO," +
         "latitudine," +
         "longitudine" +
     ") " + 
     "VALUES(seq_iuf_t_foto.nextval, :foto, :nomeFile, sysdate, :tag, :note, :extidutente , SYSDATE, :latitudine, :longitudine)";
    
      private static final String INSERT_FOTO_CAMPIONE = "INSERT INTO IUF_R_CAMPIONE_FOTO(" +
          "id_campionamento," + 
          "id_foto," + 
          "EXT_ID_UTENTE_AGGIORNAMENTO," +
          "DATA_ULTIMO_AGGIORNAMENTO" +
      ") VALUES(:id_campione, :id_foto, :extidutente , SYSDATE)";

      private static final String INSERT_FOTO_VISUAL = "INSERT INTO IUF_R_ISP_VISIVA_FOTO(" +
          "id_ispezione_visiva," + 
          "id_foto," + 
          "EXT_ID_UTENTE_AGGIORNAMENTO," +
          "DATA_ULTIMO_AGGIORNAMENTO" +
      ") VALUES(:id_ispezione, :id_foto, :extidutente , SYSDATE)";

      private static final String INSERT_FOTO_TRAPPOLA = "INSERT INTO IUF_R_TRAPPOLAGGIO_FOTO(" +
          "id_trappolaggio," + 
          "id_foto," + 
          "EXT_ID_UTENTE_AGGIORNAMENTO," +
          "DATA_ULTIMO_AGGIORNAMENTO" +
      ") VALUES(:id_trappolaggio, :id_foto, :extidutente , SYSDATE)";

      private static final String SELECT_FOTO_TO_DELETE = " select * from ("+
          "select t.id_foto as idFoto,a.id_campionamento AS id_campionamento,0 AS id_trappolaggio,0 AS id_visual " +
          "from IUF_t_foto t, " +
          "iuf_r_campione_foto a " +
          "WHERE t.id_foto=a.id_foto " +
          "UNION " +
          "select t.id_foto as idFoto,0 AS id_campionamento ,b.ID_TRAPPOLAGGIO AS id_trappolaggio ,0 AS id_visual " +
          "from IUF_t_foto t, " +
          "iuf_r_trappolaggio_foto b " +
          "WHERE T.ID_FOTO=B.ID_FOTO " +
          "UNION " +
          "select t.id_foto as idFoto,0 AS id_campionamento,0 AS id_trappolaggio,c.ID_ISPEZIONE_VISIVA AS id_visual " +
          "from IUF_t_foto t, " +
          "iuf_r_isp_visiva_foto c " +
          "where T.ID_FOTO=C.ID_FOTO) tr " +
          "where tr.idfoto=:idFoto ";
      
      private static final String DELETE_FOTO = "delete from iuf_t_foto t where t.id_foto=:idFoto "; 
      
      private static final String DELETE_FOTO_TRAPPOLA = "delete from IUF_R_TRAPPOLAGGIO_FOTO t where t.id_foto=:idFoto and t.id_trappolaggio=:idTrappolaggio";

      private static final String DELETE_FOTO_CAMPIONE = "delete from IUF_R_CAMPIONE_FOTO t where t.id_foto=:idFoto and t.id_campionamento=:idCampionamento";

      private static final String DELETE_FOTO_VISUAL = "delete from IUF_R_ISP_VISIVA_FOTO t where t.id_foto=:idFoto and t.id_ispezione_visiva=:idIspezione";
      
      private static final String SELECT_FOTO_BY_AZIONE="select * "+
      "from (select t.id_foto          as idFoto,"+
      "         a.id_campionamento AS id_campionamento,"+
      "         0                  AS id_trappolaggio,"+
      "         0                  AS id_visual "+
      "     from IUF_t_foto t, iuf_r_campione_foto a"+
      "     WHERE t.id_foto = a.id_foto"+
      "    UNION"+
      "     select t.id_foto         as idFoto,"+
      "           0                 AS id_campionamento,"+
      "           b.ID_TRAPPOLAGGIO AS id_trappolaggio,"+
      "            0                 AS id_visual"+
      "        from IUF_t_foto t, iuf_r_trappolaggio_foto b"+
      "       WHERE T.ID_FOTO = B.ID_FOTO"+
      "      UNION"+
      "      select t.id_foto             as idFoto,"+
      "             0                     AS id_campionamento,"+
      "             0                     AS id_trappolaggio,"+
      "             c.ID_ISPEZIONE_VISIVA AS id_visual"+
      "       from IUF_t_foto t, iuf_r_isp_visiva_foto c"+
      "       where T.ID_FOTO = C.ID_FOTO) tr, iuf_t_foto t "+  
      "       where tr.idfoto=t.id_foto";
     // "       and tr.idvisual=65

      //-------------------------------------------------------------------------
      private static final String SELECT_FOTO_TRAPPOLA = "select count(*) from IUF_R_TRAPPOLAGGIO_FOTO t where t.id_trappolaggio=:id";

      private static final String SELECT_FOTO_CAMPIONE = "select count(*) from IUF_R_CAMPIONE_FOTO t where t.id_campionamento=:id";

      private static final String SELECT_FOTO_VISUAL = "select count(*) from IUF_R_ISP_VISIVA_FOTO t where t.id_ispezione_visiva=:id";
      
      //-------------------------------------------------------------------------
      
  public List<FotoDTO> findAllFoto() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL_FOTO);
      return queryForList(SELECT_ALL_FOTO, mapParameterSource, FotoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL_FOTO, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }


  public List<GpsDTO> findAllGps() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAllGps";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL_GPS);
      return queryForList(SELECT_ALL_GPS, mapParameterSource, GpsDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL_GPS, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<FotoDTO> findFotoByFilter(FotoDTO foto, Integer idAnagrafica, Integer idEnte, String orderBy) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findFotoByFilter";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String select = SELECT_ALL_FOTO;
    if(foto.getIdFoto()!=null && foto.getIdFoto()>0)  
      select+=" AND id_foto = :id ";

    if(foto.getAnno()!=null && foto.getAnno()>0)
      select+=" AND to_char(tr.data_foto,'yyyy') = :anno ";
    
    select+=" AND TRUNC(tr.data_foto) BETWEEN "+
          " TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2800'),'DD/MM/YYYY')";     
    
    if (foto.getIstatComune() != null && foto.getIstatComune().trim().length() > 0 || StringUtils.isNotBlank(foto.getDescComune())) {
     select += " AND NVL(tr.istat_comune,'.') = NVL(:istatComune,NVL(tr.istat_comune,'.')) \r\n" + 
         " AND UPPER(NVL(descrizione_comune,'.')) LIKE NVL(UPPER(:descComune),'%') ";
    if (foto.getIstatComune() != null && foto.getIstatComune().trim().length() > 0) {
       mapParameterSource.addValue("istatComune", foto.getIstatComune());
    }
    else
       mapParameterSource.addValue("istatComune", null, Types.NULL);

    if (StringUtils.isNotBlank(foto.getDescComune())) {
       mapParameterSource.addValue("descComune", foto.getDescComune());
    }
    else
       mapParameterSource.addValue("descComune", null, Types.NULL);
    }      
   
   //tipo area 
   if(foto.getTipoArea()!=null)  {
     String idArea="";
       for(Integer i : foto.getTipoArea()) {
         idArea+=i+",";
       }
       idArea=idArea.substring(0,idArea.length()-1);
     select+=String.format(" AND tr.id_tipo_area IN (%s) \r\n", idArea);
    }
   //ispettore assegnato
   if(foto.getIspettoreAssegnato()!=null)  {
     String idIsAss="";
     for(Integer i : foto.getIspettoreAssegnato()) {
       idIsAss+=i+",";
     }
     idIsAss=idIsAss.substring(0,idIsAss.length()-1);
     select+=String.format(" AND tr.id_ispettore_evento IN (%s) \r\n", idIsAss);
   }
   //specie vegetale
    if(foto.getSpecieVegetale()!=null)  {
      String idSpecie="";
      for(Integer i : foto.getSpecieVegetale()) {
        idSpecie+=i+",";
      }
      idSpecie=idSpecie.substring(0,idSpecie.length()-1);
    select+=String.format(" AND tr.id_specie IN (%s)", idSpecie);      
      
    }
    //organismo nocivo
    if(foto.getOrganismoNocivo()!=null)  {
      String idOn="";
      for(Integer i : foto.getOrganismoNocivo()) {
        idOn+=i+",";
      }
      idOn=idOn.substring(0,idOn.length()-1);
      String condition ="AND (EXISTS (SELECT 1 FROM iuf_r_isp_visiva_spec_on vso\r\n" + 
      "                WHERE vso.id_ispezione = tr.id_ispezione_visiva\r\n" + 
      "                  AND vso.id_specie_on IN ("+idOn+"))\r\n"+
      " OR\r\n" + 
      "        EXISTS (SELECT 1 FROM iuf_r_campionamento_spec_on cso\r\n" + 
      "                 WHERE cso.id_campionamento = tr.id_campionamento\r\n" + 
      "                   AND cso.id_specie_on IN ("+idOn+")))";
      select += condition;
    }
    //tipo trappole 
    if(foto.getTrappole()!=null)  {
      String idTrappole="";
      for(Integer i : foto.getTrappole()) {
        idTrappole+=i+",";
      }
      idTrappole=idTrappole.substring(0,idTrappole.length()-1);
    select+=String.format(" AND tr.id_trappola IN (%s)", idTrappole);
    }
    //tipo campioni
    if(foto.getCampioni()!=null)  {
      String idCampioni="";
      for(Integer i : foto.getCampioni()) {
        idCampioni+=i+",";
      }
      idCampioni=idCampioni.substring(0,idCampioni.length()-1);
    select+=String.format(" AND tr.id_tipo_campione IN (%s)", idCampioni);
    }
    // filtro per tipologia attività (Visual, Campionamento, Trappolaggio)
    if (foto.getVisual() == null)
      foto.setVisual(false);
    if (foto.getCampionamento() == null)
      foto.setCampionamento(false);
    if (foto.getTrappolaggio() == null)
      foto.setTrappolaggio(false);
    
    if (!(foto.getVisual() && foto.getCampionamento() && foto.getTrappolaggio())) {
      if (foto.getVisual() || foto.getCampionamento() || foto.getTrappolaggio()) {
        select += " AND tipologia IN (";
        int n = 0;
        if (foto.getVisual()) {
          select += "'I'";
          n++;
        }
        if (foto.getCampionamento()) {
          select += (n > 0) ? ",'C'" : "'C'";
          n++;
        }
        if (foto.getTrappolaggio()) {
          select += (n > 0) ? ",'T'" : "'T'";
        }
        select += ") \r\n";
      }
    }
    // Filtro per Anagrafica (ispettore loggato)
    if (idAnagrafica != null && idAnagrafica > 0) {
      select += " AND (tr.id_ispettore_assegnato = :idAnagrafica " +
                     "OR EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia " +
                                 "WHERE ia.id_missione = tr.id_missione AND ia.id_anagrafica = :idAnagrafica))";
      mapParameterSource.addValue("idAnagrafica", idAnagrafica);
    }
    // Filtro per Ente (ente ispettore loggato)
    if (idEnte != null && idEnte > 0) {
      select += " AND tr.id_ente = :idEnte";
      mapParameterSource.addValue("idEnte", idEnte);
    }
    //

    mapParameterSource.addValue("id",foto.getIdFoto(), Types.INTEGER);
    mapParameterSource.addValue("anno",foto.getAnno(), Types.INTEGER);
    mapParameterSource.addValue("dallaData", foto.getDallaDataS());
    mapParameterSource.addValue("allaData", foto.getAllaDataS());
    
    if (StringUtils.isNotBlank(orderBy))
      select += " ORDER BY " + orderBy;
    else
      select += " ORDER BY tr.data_foto DESC";
    try
    {
      logger.debug(select);
      List<FotoDTO> list = queryForList(select, mapParameterSource, FotoDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL_FOTO, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
   }
  
  public List<GpsDTO> findGpsByFilter(GpsDTO gps, Integer idAnagrafica, Integer idEnte, String orderBy) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findGpsByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String select= SELECT_ALL_GPS;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    if (gps.getAnno()!=null && gps.getAnno()>0)  
      select+=" AND to_char(tr.data_acquisizione,'yyyy') = :anno ";
      select+=" AND TRUNC(tr.data_acquisizione) BETWEEN "+
          " TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2800'),'DD/MM/YYYY')";     

    if (gps.getIstatComune() != null && gps.getIstatComune().trim().length() > 0 || StringUtils.isNotBlank(gps.getDescComune())) {
      select += " AND NVL(tr.istat_comune,'.') = NVL(:istatComune,NVL(tr.istat_comune,'.')) \r\n" + 
          " AND UPPER(NVL(descrizione_comune,'.')) LIKE NVL(UPPER(:descComune),'%') ";
      if (gps.getIstatComune() != null && gps.getIstatComune().trim().length() > 0) {
        mapParameterSource.addValue("istatComune", gps.getIstatComune());
      }
      else
        mapParameterSource.addValue("istatComune", null, Types.NULL);

      if (StringUtils.isNotBlank(gps.getDescComune())) {
        mapParameterSource.addValue("descComune", gps.getDescComune());
      }
      else
        mapParameterSource.addValue("descComune", null, Types.NULL);
    }      

    if(gps.getIdRecord()!=null && gps.getIdRecord()>0)  {
      select+=" AND id_record= :id AND tipologia= :tipologia ";
    }

    //tipo area 
    if(gps.getTipoArea()!=null)  {
      String idArea="";
      for(Integer i : gps.getTipoArea()) {
        idArea+=i+",";
      }
      idArea=idArea.substring(0,idArea.length()-1);
      select+=String.format(" AND tr.id_tipo_area IN (%s)",idArea);
    }
    // filtro multiplo per ispettore assegnato - deve cercare anche per ispettore aggiunto (Richiesta di Vilma del 18/03/21)
    if (gps.getIspettoreAssegnato() != null && gps.getIspettoreAssegnato().size() > 0) {
      String ispettori = "";
      for (int i=0; i<gps.getIspettoreAssegnato().size(); i++) {
        if (i > 0)
          ispettori += ",";
        ispettori += gps.getIspettoreAssegnato().get(i);
      }

      select += " AND tr.id_anagrafica IN (" + ispettori + ") \r\n";
    }
    //specie vegetale
    if(gps.getSpecieVegetale()!=null)  {
      String idSpecie="";
      for(Integer i : gps.getSpecieVegetale()) {
        idSpecie+=i+",";
      }
      idSpecie=idSpecie.substring(0,idSpecie.length()-1);
      select+=String.format(" AND tr.id_specie IN (%s)",idSpecie);      

    }
    //organismo nocivo
    if(gps.getOrganismoNocivo()!=null)  {
      String idOn="";
      for(Integer i : gps.getOrganismoNocivo()) {
        idOn+=i+",";
      }
      idOn=idOn.substring(0,idOn.length()-1);
      String condition ="AND (EXISTS (SELECT 1 FROM iuf_r_isp_visiva_spec_on vso\r\n" + 
          "                WHERE vso.id_ispezione = tr.id_ispezione_visiva\r\n" + 
          "                  AND vso.id_specie_on IN ("+idOn+"))\r\n"+
          " OR \r\n" + 
          "        EXISTS (SELECT 1 FROM iuf_r_campionamento_spec_on cso\r\n" + 
          "                 WHERE cso.id_campionamento = tr.id_campionamento\r\n" + 
          "                   AND cso.id_specie_on IN ("+idOn+")))";
      select += condition;
    }
    //tipo trappole 
    if(gps.getTrappole()!=null)  {
      String idTrappole="";
      for(Integer i : gps.getTrappole()) {
        idTrappole+=i+",";
      }
      idTrappole=idTrappole.substring(0,idTrappole.length()-1);
      select+=String.format(" AND tr.id_tipo_trappola IN (%s)",idTrappole);      
    }
    //tipo campioni
    if(gps.getCampioni()!=null)  {
      String idCampioni="";
      for(Integer i : gps.getCampioni()) {
        idCampioni+=i+",";
      }
      idCampioni=idCampioni.substring(0,idCampioni.length()-1);
      select+=String.format(" AND tr.id_tipo_campione IN (%s)",idCampioni);            
    }
    // filtro per tipologia attività (Visual, Campionamento, Trappolaggio)
    if (gps.getVisual() == null)
      gps.setVisual(false);
    if (gps.getCampionamento() == null)
      gps.setCampionamento(false);
    if (gps.getTrappolaggio() == null)
      gps.setTrappolaggio(false);
    
    if (!(gps.getVisual() && gps.getCampionamento() && gps.getTrappolaggio())) {
      if (gps.getVisual() || gps.getCampionamento() || gps.getTrappolaggio()) {
        select += " AND tipologia IN (";
        int n = 0;
        if (gps.getVisual()) {
          select += "'I'";
          n++;
        }
        if (gps.getCampionamento()) {
          select += (n > 0) ? ",'C'" : "'C'";
          n++;
        }
        if (gps.getTrappolaggio()) {
          select += (n > 0) ? ",'T'" : "'T'";
        }
        select += ") \r\n";
      }
    }
    // Filtro per Anagrafica (ispettore loggato)
    if (idAnagrafica != null && idAnagrafica > 0) {
      select += " AND (tr.id_ispettore_assegnato = :idAnagrafica " +
                     "OR EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia " +
                                 "WHERE ia.id_missione = tr.id_missione AND ia.id_anagrafica = :idAnagrafica))";
      mapParameterSource.addValue("idAnagrafica", idAnagrafica);
    }
    // Filtro per Ente (ente ispettore loggato)
    if (idEnte != null && idEnte > 0) {
      select += " AND tr.id_ente = :idEnte";
      mapParameterSource.addValue("idEnte", idEnte);
    }
    //
    mapParameterSource.addValue("anno",gps.getAnno(), Types.INTEGER);
    mapParameterSource.addValue("dallaData", gps.getDallaDataS());
    mapParameterSource.addValue("allaData", gps.getAllaDataS());
    mapParameterSource.addValue("id", gps.getIdRecord());
    mapParameterSource.addValue("tipologia", gps.getTipologia());
    
    if (StringUtils.isNotBlank(orderBy))
      select += " ORDER BY " + orderBy;
    else
      select += " ORDER BY tr.data_acquisizione DESC";
    
    try
    {
      logger.debug(select);
      List<GpsDTO> list = queryForList(select, mapParameterSource, GpsDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL_GPS, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public FotoDTO findFotoById(Long id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findFotoById";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String select= SELECT_ALL_FOTO_DETTAGLIO;
    if(id!=null && id>0)  
      select+=" AND tr.id_foto = :id ";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(select);
      FotoDTO foto = namedParameterJdbcTemplate.queryForObject(select, mapParameterSource, getRowMapperFoto());
      return foto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ALL_FOTO_DETTAGLIO, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public GpsDTO findGpsById(Long id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findGpsById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_GPS_BY_ID);
     // FotoDTO foto = namedParameterJdbcTemplate.queryForObject(SELECT_GPS_BY_ID, mapParameterSource, getRowMapperFoto());
      return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_GPS_BY_ID, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
 
  private RowMapper<FotoDTO> getRowMapperFoto() {
    return new RowMapper<FotoDTO>() {
      
      @Override
      public FotoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        FotoDTO foto = new FotoDTO();
        foto.setIdFoto(rs.getInt("id_foto"));
        foto.setNomeFile(rs.getString("nome_file"));
        foto.setDataFoto(rs.getDate("data_foto"));
        foto.setTag(rs.getString("tag"));
        foto.setNote(rs.getString("note"));
        foto.setIstatComune(rs.getString("istat_comune"));
        foto.setArea(rs.getString("area"));
        foto.setSpecie(rs.getString("specie"));
        foto.setOrganismi(rs.getString("organismi"));
        foto.setTrappola(rs.getString("trappola"));
        foto.setCampione(rs.getString("campione"));
        lobHandler = new OracleLobHandler();
        byte[] file = lobHandler.getBlobAsBytes(rs, "foto");
        if(file!= null) {
          String base64 = DatatypeConverter.printBase64Binary(file);
          foto.setBase64(base64);          
        }
        foto.setFoto(file);
        foto.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
        return foto;
      }
    };
  } 

  

  public Long insertFoto(FotoDTO foto) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertFoto]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      //(seq_iuf_t_foto.nextval, :foto, :nomeFile, sysdate, :tag, :note, :extidutente , SYSDATE, :latitudine, :longitudine)";
      if (foto.getFotoByte() != null) {
          byte[] bytes = foto.getFotoByte();
          mapParameterSource.addValue("foto", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), OracleTypes.BLOB);
      } else
          mapParameterSource.addValue("foto", null, Types.BLOB);

      mapParameterSource.addValue("nomeFile", foto.getNomeFile(), Types.VARCHAR);
      //mapParameterSource.addValue("dataFoto", foto.getDataFoto(), Types.DATE);      
      mapParameterSource.addValue("tag", foto.getTag(), Types.VARCHAR);
      mapParameterSource.addValue("note", foto.getNote(), Types.VARCHAR);  
      mapParameterSource.addValue("extidutente", foto.getExtIdUtenteAggiornamento(), Types.INTEGER);
      mapParameterSource.addValue("latitudine", foto.getLatitudine(), Types.DOUBLE);
      mapParameterSource.addValue("longitudine", foto.getLongitudine(), Types.DOUBLE);  
      
      logger.debug(INSERT);
      int result = namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_foto"});
      Long idFoto = holder.getKey().longValue();
      foto.setIdFoto(idFoto.intValue());
      
      return idFoto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
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
  
  
  public Long insertFotoCampione(Long idFoto, Long idCampione) throws InternalUnexpectedException
  {
    //KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertFotoCampione]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      //:id_campione, :id_foto
      mapParameterSource.addValue("id_campione",idCampione, Types.INTEGER);
      mapParameterSource.addValue("id_foto", idFoto, Types.INTEGER); 
      mapParameterSource.addValue("extidutente", "0", Types.INTEGER); 
      
      logger.debug(INSERT_FOTO_CAMPIONE);
      int result = namedParameterJdbcTemplate.update(INSERT_FOTO_CAMPIONE, mapParameterSource);     
      return idCampione;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
          new LogVariable[]
          {}, INSERT_FOTO_CAMPIONE, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  
  public Long insertFotoVisual(Long idFoto, Long idVisual) throws InternalUnexpectedException
  {
    //KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertFotoVisual]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      //:id_campione, :id_foto
      mapParameterSource.addValue("id_ispezione",idVisual, Types.INTEGER);
      mapParameterSource.addValue("id_foto", idFoto, Types.INTEGER); 
      mapParameterSource.addValue("extidutente", "0", Types.INTEGER); 
      
      logger.debug(INSERT_FOTO_VISUAL);
      int result = namedParameterJdbcTemplate.update(INSERT_FOTO_VISUAL, mapParameterSource);     
      return idVisual;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
          new LogVariable[]
          {}, INSERT_FOTO_VISUAL, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  
  public Long insertFotoTrappola(Long idFoto, Long idTrappola) throws InternalUnexpectedException
  {
    //KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertFotoTrappola]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      //:id_campione, :id_foto
      mapParameterSource.addValue("id_trappolaggio",idTrappola, Types.INTEGER);
      mapParameterSource.addValue("id_foto", idFoto, Types.INTEGER); 
      mapParameterSource.addValue("extidutente", "0", Types.INTEGER); 
      
      logger.debug(INSERT_FOTO_TRAPPOLA);
      int result = namedParameterJdbcTemplate.update(INSERT_FOTO_TRAPPOLA, mapParameterSource);     
      return idTrappola;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
          new LogVariable[]
          {}, INSERT_FOTO_TRAPPOLA, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
//------------------------ SEZIONE FOTO---------------
  public void removeFoto(Integer idFoto) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idFoto", idFoto);
    
    try
    {
      namedParameterJdbcTemplate.update(DELETE_FOTO, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, DELETE_FOTO, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      //throw e;
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

  
  public void removeFotoCampionamento(Integer idFoto, Integer idCampionamento) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idFoto", idFoto);
    mapParameterSource.addValue("idCampionamento", idCampionamento);
    try
    {
      namedParameterJdbcTemplate.update(DELETE_FOTO_CAMPIONE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, DELETE_FOTO_CAMPIONE, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      //throw e;
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

  public void removeFotoTrappolaggio(Integer idFoto, Integer idTrappolaggio) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idFoto", idFoto);
    mapParameterSource.addValue("idTrappolaggio", idTrappolaggio);
    try
    {
      namedParameterJdbcTemplate.update(DELETE_FOTO_TRAPPOLA, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, DELETE_FOTO_TRAPPOLA, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      //throw e;
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

  
  public void removeFotoIspezione(Integer idFoto, Integer idIspezione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "remove";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idFoto", idFoto);
    mapParameterSource.addValue("idIspezione", idIspezione);
    try
    {
      namedParameterJdbcTemplate.update(DELETE_FOTO_VISUAL, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, DELETE_FOTO_VISUAL, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      //throw e;
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

  
  public FotoApiDTO selectFotoToRemoveById(Integer idFoto) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idFoto", idFoto);
    
    try
    {
      logger.debug(SELECT_FOTO_TO_DELETE);
      return queryForObject(SELECT_FOTO_TO_DELETE, mapParameterSource, FotoApiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_FOTO_TO_DELETE, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  
  public List<FotoDTO> selectFotoByIdAzione(FotoApiDTO fotoApi) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String select = SELECT_FOTO_BY_AZIONE;
   
    if(fotoApi!=null && fotoApi.getIdCampionamento()!=null && fotoApi.getIdCampionamento()>0) {
      select += " and tr.id_campionamento =:idCampionamento";
    }else if(fotoApi!=null && fotoApi.getIdTrappolaggio()!=null && fotoApi.getIdTrappolaggio()>0) {
      select += " and tr.id_trappolaggio =:idTrappolaggio";
    }else {
      select += " and tr.id_visual =:idVisual";
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idCampionamento", fotoApi.getIdCampionamento(), Types.INTEGER);
    mapParameterSource.addValue("idTrappolaggio", fotoApi.getIdTrappolaggio(), Types.INTEGER);
    mapParameterSource.addValue("idVisual", fotoApi.getIdVisual(), Types.INTEGER);
    
    try
    {
      logger.debug(select);
      List<FotoDTO> lista = queryForList(select, mapParameterSource, FotoDTO.class);
      if(lista!=null) {
        for(FotoDTO foto: lista) {
          FotoDTO obj= this.findFotoById(Long.valueOf(foto.getIdFoto()));
          foto.setBase64(obj.getBase64());
        }
        return lista;        
      }else 
        return new ArrayList<FotoDTO>();
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

  
  public Long countFotoCampionamento(Long id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id, Types.INTEGER);
    try
    {
      logger.debug(SELECT_FOTO_CAMPIONE);
      return queryForLong(SELECT_FOTO_CAMPIONE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_FOTO_CAMPIONE, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public Long countFotoTrappolaggio(Long id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id, Types.INTEGER);
    try
    {
      logger.debug(SELECT_FOTO_TRAPPOLA);
      return queryForLong(SELECT_FOTO_TRAPPOLA, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_FOTO_TRAPPOLA, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  
  public Long countFotoVisual(Long id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id.intValue(), Types.INTEGER);
    try
    {
      logger.debug(SELECT_FOTO_VISUAL);
      Long ris= queryForLong(SELECT_FOTO_VISUAL, mapParameterSource);
      return ris;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_FOTO_VISUAL, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }
//------------------------ SEZIONE FOTO---------------  
  public LobHandler getLobHandler() {
    return lobHandler;
    }

    public void setLobHandler(LobHandler lobHandler) {
    this.lobHandler = lobHandler;
    }

}