package it.csi.iuffi.iuffiweb.service;


import java.math.BigDecimal;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.manager.Formule;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RendicontazioneDTO;
import it.csi.iuffi.iuffiweb.model.RendicontazioneRequestDTO;


public class RendicontazioneDAO extends BaseDAO
{
  //Formule formule;
  
  private static final String THIS_CLASS = RendicontazioneDAO.class.getSimpleName();
  
  private static final String TAB_VISUAL = "SELECT " + 
      "a.id_missione,\r\n" +
      "a.numero_trasferta,\r\n" +
      "a.data_ora_inizio_missione," +
      "a.data_ora_fine_missione," +
      "b.id_rilevazione," +
      "c.id_anagrafica,\r\n" + 
      "b.id_tipo_area,\r\n" +
      "c.id_specie_vegetale as specie,\r\n" + 
      "'I' as tipo,\r\n" + 
      "v.genere_specie as nome_volgare,\r\n" + 
      "s.cognome||' '||s.nome as ispettore,\r\n" +
      "NVL(s.subcontractor,'N') AS subcontractor,\r\n" +
      "(SELECT LISTAGG(aa.cognome||' '||aa.nome, '; ') WITHIN GROUP(ORDER BY aa.cognome)\r\n" +
      "   FROM iuf_r_ispettore_aggiunto ia, iuf_t_anagrafica aa\r\n" +
      "  WHERE ia.id_missione = a.id_missione\r\n" +
      "    AND ia.id_anagrafica = aa.id_anagrafica) AS ispettori_aggiunti,\r\n" +
      "null as id_ispezione_visiva,\r\n" + 
      "s.paga_oraria, \r\n" + 
      "(select count(*) from IUF_R_ISP_VISIVA_SPEC_ON t,iuf_d_organismo_nocivo b " + 
      " where t.id_specie_on=b.id_organismo_nocivo and t.id_ispezione=c.id_ispezione and b.euro='S') as count_organismi," +
      " (SELECT LISTAGG(a.sigla, '; ') WITHIN GROUP (ORDER BY a.sigla) " +
      " from IUF_R_ISP_VISIVA_SPEC_ON t," +
      " iuf_d_organismo_nocivo a " +
      " where t.id_specie_on=a.id_organismo_nocivo and t.id_ispezione=c.id_ispezione and a.euro='S') as organismi," +
      " (SELECT LISTAGG(a.sigla, '; ') WITHIN GROUP (ORDER BY a.sigla) " +
      " from IUF_R_ISP_VISIVA_SPEC_ON t," +
      " iuf_d_organismo_nocivo a " +
      " where t.id_specie_on=a.id_organismo_nocivo and t.id_ispezione=c.id_ispezione and a.euro='N') as organismi_no_euro," +
      "ee.tipologia_campione as tipologia_campione,"+
      "(select count(*) from iuf_t_campionamento r where r.id_ispezione_visiva=c.id_ispezione and r.id_rilevazione = c.id_rilevazione AND r.id_specie_vegetale = c.id_specie_vegetale) as num_campioni,\r\n" +
      "0 as id_tipo_trappola,\r\n" +
      "c.latitudine,c.longitudine,\r\n"+
      "c.latitudine latitudinebd,c.longitudine longitudinebd,\r\n"+
      "0 as id_operazione,\r\n" +
      "'' as tipologia_trappola,\r\n" +
      "NVL((SELECT listagg(t1.codice_sfr,';') WITHIN GROUP (ORDER BY t1.codice_sfr) FROM iuf_t_trappola t1, iuf_t_trappolaggio t2\r\n" + 
      "                 WHERE t1.id_trappola = t2.id_trappola AND t2.id_rilevazione = b.id_rilevazione\r\n" + 
      "                   AND t2.id_ispezione_visiva = c.id_ispezione AND t2.id_operazione = 1 AND t1.codice_sfr IS NOT NULL),'') AS codice_sfr,\r\n" +
      
      "(select count(*) from iuf_t_trappolaggio aa,iuf_t_trappola bb where aa.id_trappola=bb.id_trappola\r\n" + 
      "and aa.id_rilevazione=b.id_rilevazione and aa.id_ispezione_visiva=c.id_ispezione) as num_trapp_tot,\r\n" + 
      "(select count(*) from iuf_t_trappolaggio aa,iuf_t_trappola bb where aa.id_trappola=bb.id_trappola\r\n" + 
      "and aa.id_rilevazione=b.id_rilevazione and aa.id_ispezione_visiva=c.id_ispezione and aa.id_operazione = 1) as num_trapp_piazz,\r\n" + 
      "(select count(*) from iuf_t_trappolaggio aa,iuf_t_trappola bb where aa.id_trappola=bb.id_trappola\r\n" + 
      "and aa.id_rilevazione=b.id_rilevazione and aa.id_ispezione_visiva=c.id_ispezione and aa.id_operazione in (2,3)) as num_trapp_sost_ric,\r\n" + 
      "(select count(*) from iuf_t_trappolaggio aa,iuf_t_trappola bb where aa.id_trappola=bb.id_trappola\r\n" + 
      "and aa.id_rilevazione=b.id_rilevazione and aa.id_ispezione_visiva=c.id_ispezione and aa.id_operazione = 4) as num_trapp_rimosse,\r\n" + 
      "NVL(c.superfice,0) as superfice,NVL(c.numero_piante,0) as numero_piante ,NVL(z.dettaglio_tipo_area,z.desc_tipo_area) as desc_tipo_area,NVL(z.velocita,0) as velocita,\r\n" +
      "z.codice_ufficiale,\r\n" +
      "z.typology_of_location,\r\n" +
      "xx.tipologia_test_di_laboratorio as test_lab,NVL(xx.costo,0) as costo_lab,\r\n" +
      "'' AS num_registro_lab,\r\n" +
      "kk.cod_campione, "+ 
      "(SELECT LISTAGG(a.sigla, '; ') WITHIN GROUP (ORDER BY a.sigla) \r\n" + 
      "from Iuf_r_Campionamento_Spec_On j,\r\n" + 
      "iuf_d_organismo_nocivo a \r\n" + 
      "where j.id_specie_on=a.id_organismo_nocivo and a.euro='S' and r.id_campionamento=j.id_campionamento) as organismi_campione, \r\n" + 
      "(SELECT LISTAGG(ot.sigla, '; ') WITHIN GROUP (ORDER BY ot.sigla)\r\n" + 
      "   FROM (SELECT DISTINCT oo.sigla,aa.id_ispezione_visiva\r\n" + 
      "           FROM iuf_t_trappolaggio aa, iuf_d_organismo_nocivo oo\r\n" + 
      "          WHERE aa.id_organismo_nocivo = oo.id_organismo_nocivo\r\n" + 
      "            AND oo.euro = 'S' AND aa.id_operazione = 1) ot\r\n" + 
      " WHERE ot.id_ispezione_visiva = c.id_ispezione) AS organismo_trappola,\r\n" +
      "b.flag_emergenza,\r\n" +
      "(SELECT COUNT(*) FROM iuf_r_ispettore_aggiunto ia WHERE ia.id_missione = a.id_missione) num_ispettori_aggiunti,\r\n" +
      "r.id_campionamento \r\n" +
      
      "FROM \r\n" +
      
      "iuf_t_missione a,\r\n" + 
      "iuf_t_rilevazione b,\r\n" + 
      "iuf_t_ispezione_visiva c,\r\n" + 
      "iuf_t_anagrafica s,\r\n" + 
      "iuf_d_specie_vegetale v,\r\n" + 
      "iuf_t_campionamento r,\r\n" + 
      "iuf_t_trappolaggio h,\r\n" + 
      "iuf_d_tipo_campione f,\r\n" + 
      "Iuf_d_Tipo_Area z,\r\n" + 
      "iuf_d_anfi xx,\r\n" + 
      "iuf_t_esito_campione kk, "+
      "iuf_d_tipo_campione ee "+
      "where a.id_missione=b.id_missione \r\n" + 
      "and b.id_rilevazione = c.id_rilevazione\r\n" + 
      "and r.id_ispezione_visiva(+)=c.id_ispezione\r\n" + 
      "and h.id_ispezione_visiva(+)=c.id_ispezione\r\n" + 
      "and c.id_specie_vegetale=v.id_specie_vegetale\r\n" + 
      "and s.id_anagrafica =c.id_anagrafica\r\n" + 
      "and r.id_tipo_campione=f.id_tipo_campione(+)\r\n" + 
      "and z.id_tipo_area=b.id_tipo_area\r\n "+
      "and r.id_campionamento=kk.id_campionamento(+)\r\n" + 
      "and kk.id_anfi=xx.id_anfi (+) \r\n"+
      "and ee.id_tipo_campione (+) = r.id_tipo_campione";

  private static final String TAB_CAMPIONAMENTO = "SELECT a.id_missione,\r\n" +
      "a.numero_trasferta,\r\n" +
      "a.data_ora_inizio_missione as data_ora_inizio_missione,a.data_ora_fine_missione,\r\n" + 
      "b.id_rilevazione,d.id_anagrafica,b.id_tipo_area,\r\n" + 
      "d.id_specie_vegetale as specie,'C' as tipo,v.genere_specie as nome_volgare,s.cognome||' '||s.nome as ispettore,\r\n" +
      "NVL(s.subcontractor,'N') AS subcontractor,\r\n" +
      "(SELECT LISTAGG(aa.cognome||' '||aa.nome, '; ') WITHIN GROUP(ORDER BY aa.cognome)\r\n" +
      "   FROM iuf_r_ispettore_aggiunto ia, iuf_t_anagrafica aa\r\n" +
      "  WHERE ia.id_missione = a.id_missione\r\n" +
      "    AND ia.id_anagrafica = aa.id_anagrafica) AS ispettori_aggiunti,\r\n" +
      "d.id_ispezione_visiva,s.paga_oraria,\r\n"+
      "(select count(*) from IUF_R_CAMPIONAMENTO_SPEC_ON t,iuf_d_organismo_nocivo b " +
      " where t.id_specie_on=b.id_organismo_nocivo and t.id_campionamento=d.id_campionamento and b.euro='S') as count_organismi," +
      " (SELECT LISTAGG(a.sigla, '; ')WITHIN GROUP (ORDER BY a.sigla) " +
      " from IUF_R_CAMPIONAMENTO_SPEC_ON t," +
      " iuf_d_organismo_nocivo a " +
      " where t.id_specie_on=a.id_organismo_nocivo and t.id_campionamento=d.id_campionamento and a.euro='S') as organismi," +
      " (SELECT LISTAGG(a.sigla, '; ')WITHIN GROUP (ORDER BY a.sigla) " +
      " from IUF_R_CAMPIONAMENTO_SPEC_ON t," +
      " iuf_d_organismo_nocivo a " +
      " where t.id_specie_on=a.id_organismo_nocivo and t.id_campionamento=d.id_campionamento and a.euro='N') as organismi_no_euro," +
      "ee.tipologia_campione as tipologia_campione,"+
      "1 as num_campioni,\r\n" + 
      "0 as id_tipo_trappola,\r\n" +
      "d.latitudine,\r\n" +
      "d.longitudine,\r\n" +
      "d.latitudine latitudinebd,d.longitudine longitudinebd,\r\n"+
      "0 as id_operazione,\r\n" +
      "'' as tipologia_trappola,\r\n" +
      "'' AS codice_sfr,\r\n" +
      "0 as num_trapp_tot,0 as num_trapp_piazz,0 as num_trapp_sost_ric,0 as num_trapp_rimosse, \r\n" + 
      "0 as superfice,0 as numero_piante,NVL(z.dettaglio_tipo_area,z.desc_tipo_area) as desc_tipo_area,NVL(z.velocita,0) as velocita, \r\n" + 
      "z.codice_ufficiale,\r\n" +
      "z.typology_of_location,\r\n" +
      "xx.tipologia_test_di_laboratorio as test_lab,NVL(xx.costo,0) as costo_lab,\r\n" +
      "kk.num_registro_lab,\r\n" +
      "kk.cod_campione, "+
      "(SELECT LISTAGG(a.sigla, '; ')WITHIN GROUP (ORDER BY a.sigla) \r\n" + 
      "from Iuf_r_Campionamento_Spec_On j, \r\n" + 
      "iuf_d_organismo_nocivo a \r\n" + 
      "where j.id_specie_on=a.id_organismo_nocivo and a.euro='S' and d.id_campionamento=j.id_campionamento) as organismi_campione,  \r\n" +
      "'' as organismo_trappola,\r\n" +
      "b.flag_emergenza,\r\n" +
      "(SELECT COUNT(*) FROM iuf_r_ispettore_aggiunto ia WHERE ia.id_missione = a.id_missione) num_ispettori_aggiunti,\r\n" +
      "d.id_campionamento \r\n" +
      
      "FROM \r\n" +
      
      "iuf_t_missione a,\r\n" + 
      "iuf_t_rilevazione b,\r\n" + 
      "iuf_t_campionamento d,\r\n" + 
      "iuf_t_anagrafica s,\r\n" + 
      "iuf_d_specie_vegetale v,\r\n" + 
      "Iuf_d_Tipo_Area z,\r\n" + 
      "iuf_d_anfi xx,\r\n" +
      "iuf_d_tipo_campione ee,"+
      "iuf_t_esito_campione kk "+
      "where b.id_missione=a.id_missione\r\n" + 
      "and d.id_rilevazione(+) = b.id_rilevazione\r\n" + 
      "and d.id_ispezione_visiva is null\r\n" + 
      "and d.id_specie_vegetale=v.id_specie_vegetale\r\n" + 
      "and s.id_anagrafica =d.id_anagrafica\r\n" + 
      "and z.id_tipo_area=b.id_tipo_area\r\n"+
      "and d.id_campionamento=kk.id_campionamento(+)\r\n" + 
      "and kk.id_anfi=xx.id_anfi(+) \r\n"+
      "and ee.id_tipo_campione=d.id_tipo_campione";
  
      private static final String TAB_TRAPPOLAGGIO = "SELECT a.id_missione,\r\n" +
          "a.numero_trasferta,\r\n" +
          "a.data_ora_inizio_missione,\r\n" +
          "a.data_ora_fine_missione,\r\n" + 
          "b.id_rilevazione,e.id_anagrafica,\r\n" +
          "b.id_tipo_area,f.id_specie_veg as specie,\r\n" + 
          "'T' as tipo,\r\n" +
          "v.genere_specie as nome_volgare,\r\n" +
          "s.cognome||' '||s.nome as ispettore,\r\n" + 
          "NVL(s.subcontractor,'N') AS subcontractor,\r\n" +
          "(SELECT LISTAGG(aa.cognome||' '||aa.nome, '; ') WITHIN GROUP(ORDER BY aa.cognome)\r\n" +
          "   FROM iuf_r_ispettore_aggiunto ia, iuf_t_anagrafica aa\r\n" +
          "  WHERE ia.id_missione = a.id_missione\r\n" +
          "    AND ia.id_anagrafica = aa.id_anagrafica) AS ispettori_aggiunti,\r\n" +
          "e.id_ispezione_visiva,\r\n" +
          "s.paga_oraria,\r\n" +
          "0 as count_organismi,\r\n" +
          "'' as organismi,\r\n" +
          "'' as organismi_no_euro,\r\n" +
          "'' as tipologia_campione, \r\n" +
          "0 as num_campioni,\r\n" +
          "f.id_tipo_trappola,\r\n" +
          "f.latitudine,\r\n" +
          "f.longitudine,\r\n" + 
          "f.latitudine latitudinebd,f.longitudine longitudinebd,\r\n" +
          "e.id_operazione,\r\n" +
          "g.tipologia_trappola,\r\n" +
          "f.codice_sfr,\r\n" +
          "(select count(*) from iuf_t_trappolaggio aa where aa.id_trappolaggio = e.id_trappolaggio) as num_trapp_tot,\r\n" + 
          "NVL((SELECT COUNT(*) FROM iuf_t_trappolaggio aa WHERE aa.id_trappolaggio = e.id_trappolaggio AND aa.id_operazione = 1),0) as num_trapp_piazz,\r\n" + 
          "NVL((SELECT COUNT(*) FROM iuf_t_trappolaggio aa WHERE aa.id_trappolaggio = e.id_trappolaggio AND aa.id_operazione IN (2,3)),0) as num_trapp_sost_ric,\r\n" + 
          "NVL((SELECT COUNT(*) FROM iuf_t_trappolaggio aa WHERE aa.id_trappolaggio = e.id_trappolaggio AND aa.id_operazione = 4 ),0) as num_trapp_rimosse,\r\n" +
          "0 as superfice,\r\n" + 
          "0 as numero_piante,\r\n" +
          "NVL(z.dettaglio_tipo_area,z.desc_tipo_area) as desc_tipo_area,\r\n" +
          "NVL(z.velocita,0) as velocita,\r\n" + 
          "z.codice_ufficiale,\r\n" +
          "z.typology_of_location,\r\n" +
          "'' as test_lab,\r\n" +
          "0 as costo_lab,\r\n" +
          "'' AS num_registro_lab,\r\n" +
          "'' as cod_campione,'' as organismi_campione,\r\n"+
          "(SELECT trap_inst.sigla\r\n" + 
          "   FROM (\r\n" + 
          "         SELECT org.sigla,tra.id_trappolaggio,tra.id_trappola\r\n" + 
          "           FROM iuf_t_trappolaggio tra, iuf_d_organismo_nocivo org\r\n" + 
          "          WHERE tra.id_organismo_nocivo = org.id_organismo_nocivo\r\n" + 
          "            AND tra.id_operazione = 1\r\n" + 
          "          ORDER BY tra.id_trappolaggio DESC) trap_inst\r\n" + 
          " WHERE trap_inst.id_trappola = e.id_trappola\r\n" + 
          "   AND ROWNUM = 1) AS organismo_trappola,\r\n" +
          "b.flag_emergenza,\r\n" +
          "(SELECT COUNT(*) FROM iuf_r_ispettore_aggiunto ia WHERE ia.id_missione = a.id_missione) num_ispettori_aggiunti,\r\n" +
          "NULL id_campionamento \r\n" +
          
          "FROM \r\n" +
          
          "iuf_t_missione a,\r\n" + 
          "iuf_t_rilevazione b,\r\n" + 
          "iuf_t_trappolaggio e,\r\n" + 
          "iuf_t_anagrafica s,\r\n" + 
          "iuf_t_trappola f,\r\n" + 
          "iuf_d_tipo_trappola g,\r\n" + 
          "iuf_d_specie_vegetale v,\r\n" + 
          "Iuf_d_Tipo_Area z\r\n" + 
          "where b.id_missione=a.id_missione\r\n" + 
          "and f.id_specie_veg=v.id_specie_vegetale and e.id_rilevazione = b.id_rilevazione and e.id_ispezione_visiva is null\r\n" + 
          //"and (b.visual  is not null or b.campionamento is not null or b.trappolaggio is not null)\r\n" + 
          "and s.id_anagrafica =e.id_anagrafica\r\n" + 
          "and e.id_trappola=f.id_trappola\r\n" + 
          "and f.id_tipo_trappola=g.id_tipo_trappola\r\n" +
          "and z.id_tipo_area=b.id_tipo_area";
      
   private static final String SELECT_ALL = "select tr.*,NVL(tv.num_verbale,tr.id_missione) num_verbale from (" +
        TAB_VISUAL +
       " UNION ALL \r\n" +
        TAB_CAMPIONAMENTO +
       " UNION ALL \r\n" +
        TAB_TRAPPOLAGGIO +
       ") tr, iuf_t_verbale tv\r\n" +
       "WHERE " +
       "tv.id_missione (+) = tr.id_missione \r\n";  //AND tr.id_missione BETWEEN 150 AND 157\r\n";

   private static final String SELECT_ON = "SELECT DISTINCT o.*\r\n" + 
       "  FROM (\r\n" + 
       "        SELECT vso.id_specie_on, m.data_ora_inizio_missione, r.flag_emergenza\r\n" + 
       "          FROM iuf_r_isp_visiva_spec_on vso, iuf_t_ispezione_visiva v, iuf_t_rilevazione r, iuf_t_missione m\r\n" + 
       "         WHERE vso.id_ispezione = v.id_ispezione\r\n" + 
       "           AND r.id_rilevazione = v.id_rilevazione\r\n" + 
       "           AND r.id_missione = m.id_missione\r\n" + 
       "        UNION\r\n" + 
       "        SELECT cso.id_specie_on, m.data_ora_inizio_missione, r.flag_emergenza\r\n" + 
       "          FROM iuf_r_campionamento_spec_on cso, iuf_t_campionamento c, iuf_t_rilevazione r, iuf_t_missione m\r\n" + 
       "         WHERE cso.id_campionamento = c.id_campionamento\r\n" + 
       "           AND r.id_rilevazione = c.id_rilevazione\r\n" + 
       "           AND r.id_missione = m.id_missione\r\n" + 
       "        UNION\r\n" + 
       "        SELECT tr.id_organismo_nocivo, m.data_ora_inizio_missione, r.flag_emergenza\r\n" + 
       "          FROM iuf_t_trappolaggio tr, iuf_t_rilevazione r, iuf_t_missione m\r\n" + 
       "         WHERE r.id_rilevazione = tr.id_rilevazione\r\n" + 
       "           AND r.id_missione = m.id_missione\r\n" + 
       "       ) t, iuf_d_organismo_nocivo o\r\n" + 
       " WHERE t.id_specie_on = o.id_organismo_nocivo \r\n" + 
       "   AND o.euro = 'S'\r\n" +
       "   AND to_char(t.data_ora_inizio_missione, 'yyyy') = :anno\r\n" + 
       "   AND NVL(t.flag_emergenza, 'N') LIKE NVL(:emergenza,'%')\r\n" +
       "   AND o.data_fine_validita IS NULL\r\n" +
       " ORDER BY UPPER(o.nome_latino)";

  public RendicontazioneDAO(){  }

  
  public List<RendicontazioneDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
     // return queryForList(SELECT_ALL, mapParameterSource,getRowMapperRendicontazione());
      logger.debug(SELECT_ALL);
      List<RendicontazioneRequestDTO> list = queryForList(SELECT_ALL, mapParameterSource,RendicontazioneRequestDTO.class);
      //da popolare
      return this.popolaListaRendicontazione(list);
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

  public List<RendicontazioneDTO> findByFilter(RendicontazioneDTO rendicontazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT = SELECT_ALL;
    
    if (rendicontazione.getAnno()!=null && rendicontazione.getAnno()>0)
      SELECT+=" AND to_char(tr.data_ora_inizio_missione,'yyyy') = :anno";
        
    //SELECT+=" AND tr.id_missione = 679";   // to remove
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("anno",rendicontazione.getAnno(), Types.INTEGER);
    
    if (StringUtils.isNotBlank(rendicontazione.getArea())) {     // Modifiche evolutive punto 1.1 (S.D.)
      SELECT += " AND NVL(tr.flag_emergenza,'N') = :emergenza";
      mapParameterSource.addValue("emergenza", rendicontazione.getArea());
    }

  //  SELECT += " and num_verbale = 'VERB_231_02'";
    SELECT += " ORDER BY tr.id_missione,data_ora_inizio_missione,num_verbale";
    try
    {
     // return queryForList(SELECT_ALL, mapParameterSource,getRowMapperRendicontazione());
      logger.debug(SELECT);
      List<RendicontazioneRequestDTO> list = queryForList(SELECT, mapParameterSource,RendicontazioneRequestDTO.class);
      //da popolare
      if (list !=null && list.size() > 0)
        return this.popolaListaRendicontazione(list);
      else
        return new ArrayList<RendicontazioneDTO>();
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

  private List<RendicontazioneDTO> popolaListaRendicontazione(List<RendicontazioneRequestDTO> request) {
    
    List<RendicontazioneDTO> lista = new ArrayList<RendicontazioneDTO>();
    /**
     * primo ciclo di calcolo sui valori di ingresso
     */
    //NumberFormat nf = new DecimalFormat("0.000");
    NumberFormat nf2 = new DecimalFormat("0.0000000000000");
    String appo = "new";
    double pesoVerbale = 0;
    
    for (RendicontazioneRequestDTO reqDto : request) {
      String verbale = (StringUtils.isNotBlank(reqDto.getNumVerbale())) ? reqDto.getNumVerbale() : reqDto.getIdMissione().toString();
      RendicontazioneDTO dto = new RendicontazioneDTO();
      dto.setNumeroVerbale(reqDto.getNumVerbale());
      dto.setNumeroTrasferta(reqDto.getNumeroTrasferta());
      dto.setDataMissione(reqDto.getDataOraInizioMissione());
      dto.setIdMissione(reqDto.getIdMissione());
      dto.setIdRilevazione(reqDto.getIdRilevazione());
      ////////determino ore totali siti
      Double oreTotaliSiti = Formule.oreTotaliSiti(reqDto.getDataOraInizioMissione(),reqDto.getDataOraFineMissione());
      oreTotaliSiti = oreTotaliSiti*(1+reqDto.getNumIspettoriAggiunti());
      
      int intPart = (int) oreTotaliSiti.doubleValue();
      double decimal = oreTotaliSiti - intPart;
      decimal = decimal * 60 / 100;
      oreTotaliSiti = intPart + decimal;
      String ore = nf2.format(oreTotaliSiti.doubleValue());
      dto.setOreTotaliSiti(Double.valueOf(ore.replace(',', '.')));
      dto.setOreTotaliSitiS((dto.getOreTotaliSiti().toString()).replace('.', ':'));
      ////////determino ore totali siti
      //DETERMINO SE LA RILEVAZIONE CONTIENE DEI VISUAL     
      Integer numTrapp = reqDto.getNumTrappPiazz()+reqDto.getNumTrappRimosse()+reqDto.getNumTrappSostRic();
      dto.setNumCampioni(reqDto.getNumCampioni());
      dto.setPesoRiga(Double.valueOf((nf2.format(Formule.pesoRiga(reqDto.getCountOrganismi(),reqDto.getNumeroPiante(),
                                      reqDto.getSuperfice(),reqDto.getNumCampioni(),numTrapp,reqDto.getVelocita()))).replace(',', '.')));
      dto.setTipologiaTrappola(reqDto.getTipologiaTrappola());
      dto.setLongitudine(reqDto.getLongitudine());
      dto.setLatitudine(reqDto.getLatitudine());
      dto.setLatitudinebd(new BigDecimal(reqDto.getLatitudine()));
      dto.setLongitudinebd(new BigDecimal(reqDto.getLongitudine()));
      dto.setNumeroPiante(reqDto.getNumeroPiante());
      dto.setSuperfice(reqDto.getSuperfice()/10000);
      dto.setDescTipoArea(reqDto.getDescTipoArea());
      dto.setCodiceUfficiale(reqDto.getCodiceUfficiale());
      dto.setTypologyOfLocation(reqDto.getTypologyOfLocation());
      dto.setIspettore((reqDto.getNumIspettoriAggiunti() > 0) ? reqDto.getIspettore()+"; "+reqDto.getIspettoriAggiunti() : reqDto.getIspettore());
      dto.setSubcontractor(reqDto.getSubcontractor());
      dto.setNumIspettori(reqDto.getNumIspettoriAggiunti()+1);
      dto.setDataInizioValidita(reqDto.getDataOraInizioMissione());
      dto.setTipo(reqDto.getTipo());
      if (reqDto.getTipo().equals("I")) {
        dto.setCostoOrarioTecIspezione(reqDto.getPagaOraria());
        dto.setNomeLatinoSpecie(reqDto.getNomeVolgare());
        dto.setOnSpecie(reqDto.getOrganismi());
        dto.setOnNoUeSpecie(reqDto.getOrganismiNoEuro());
        dto.setNumOnRimborso(reqDto.getCountOrganismi()); 
        dto.setOrganismoTrappola(reqDto.getOrganismoTrappola());
        if(reqDto.getNumCampioni()>0) {
          dto.setCostoOrarioTecCampionamento(reqDto.getPagaOraria());
          dto.setTipoCampione(reqDto.getTipologiaCampione()); 
          dto.setTestLab(reqDto.getTestLab());
          dto.setCostoLaboratorio(reqDto.getCostoLab());
          dto.setTotaleCostoEsami(reqDto.getCostoLab() * reqDto.getNumCampioni());
          dto.setOrganismiCampione(reqDto.getOrganismiCampione());
          dto.setIdCampionamento(reqDto.getIdCampionamento());
          dto.setCodCampione(reqDto.getCodCampione());
        }else {
          dto.setCostoOrarioTecCampionamento(Double.valueOf(0));
        }
        //controllo le trappole
        if(reqDto.getNumTrappPiazz()!=null && reqDto.getNumTrappPiazz()>0) {
          dto.setCodiceTrappola(reqDto.getCodiceSfr());
          dto.setNumPiazzamento(reqDto.getNumTrappPiazz());
          dto.setCostoOrarioTecTrappolaggio(reqDto.getPagaOraria());
        }
        if(reqDto.getNumTrappSostRic()!=null && reqDto.getNumTrappSostRic()>0) {
          dto.setNumRicSostTrappole(reqDto.getNumTrappSostRic());
          dto.setCostoOrarioTecTrappolaggio(reqDto.getPagaOraria());
        }
        if(reqDto.getNumTrappRimosse()!=null && reqDto.getNumTrappRimosse()>0) {
          dto.setNumRimozioneTrappole(reqDto.getNumTrappRimosse());     
          dto.setCostoOrarioTecTrappolaggio(reqDto.getPagaOraria());
        }
      }
      //DETERMINO SE LA RILEVAZIONE CONTIENE DEI CAMPIONI
      if(reqDto.getTipo().equals("C")) {
        //dto.setOnSpecie(reqDto.getOrganismi());
        dto.setOnSpecie(reqDto.getOrganismiCampione());
        dto.setOnNoUeSpecie(reqDto.getOrganismiNoEuro());
        dto.setCostoOrarioTecCampionamento(reqDto.getPagaOraria());
        dto.setCostoOrarioTecIspezione(Double.valueOf(0));
        dto.setCostoOrarioTecTrappolaggio(Double.valueOf(0));
        dto.setTipoCampione(reqDto.getTipologiaCampione());
        dto.setNomeLatinoSpecie(reqDto.getNomeVolgare());
        dto.setTestLab(reqDto.getTestLab());
        dto.setNumRegistroLab(reqDto.getNumRegistroLab());
        dto.setCostoLaboratorio(reqDto.getCostoLab());
        dto.setTotaleCostoEsami(reqDto.getCostoLab() * reqDto.getNumCampioni());
        dto.setOrganismiCampione(reqDto.getOrganismiCampione());
        dto.setIdCampionamento(reqDto.getIdCampionamento());
        dto.setCodCampione(reqDto.getCodCampione());
      }
      //DETERMINO SE LA RILEVAZIONE CONTIENE DEI TRAPPOLAGGI
      if(reqDto.getTipo().equals("T")) {
        dto.setOnSpecie(reqDto.getOrganismoTrappola());
        dto.setNumRicSostTrappole(reqDto.getNumTrappSostRic());
        dto.setNumPiazzamento(reqDto.getNumTrappPiazz());
        dto.setNumRimozioneTrappole(reqDto.getNumTrappRimosse());     
        dto.setCostoOrarioTecTrappolaggio(reqDto.getPagaOraria());
        dto.setCostoOrarioTecCampionamento(Double.valueOf(0));
        dto.setCostoOrarioTecIspezione(Double.valueOf(0));
        dto.setNomeLatinoSpecie(reqDto.getNomeVolgare());
        dto.setOrganismoTrappola(reqDto.getOrganismoTrappola());
        dto.setCodiceTrappola(reqDto.getCodiceSfr());
        //dto.setCodiceTrappola(reqDto.getCodiceTrappola());
      }  
     
      dto.setCodiceSfr(reqDto.getNumRegistroLab());     // Modificato il 31/01/2022 (S.D.) - Evolutiva
      dto.setFlagEmergenza(reqDto.getFlagEmergenza());
     /* dto.setCostoLaboratorio(rs.getDouble("costoLaboratorio"));
      dto.setTotaleCostoEsami(rs.getDouble("totaleCostoEsami"));*/

      lista.add(dto);

      if (appo.equals("new") || appo.equals(verbale)) {
        pesoVerbale += dto.getPesoRiga();
      }
      else
      {
        for (RendicontazioneDTO obj : lista) {
          
          if (obj.getNumeroVerbale().equals(appo)) {
            
            obj.setPesoVerbale(Double.valueOf((nf2.format(pesoVerbale)).replace(',', '.')));
            if(obj.getPesoVerbale()!=0){
              int intPart2 = (int) obj.getOreTotaliSiti().doubleValue();
              double decimal2 = obj.getOreTotaliSiti() - intPart2;
              decimal2 = decimal2 * 100 / 60;
              double ris = intPart2 + decimal2;
             // String ore = nf2.format(oreTotaliSiti.doubleValue());
               obj.setOreOperazioni(Double.valueOf((nf2.format(ris/pesoVerbale)).replace(',', '.')));
            }else
              obj.setOreOperazioni(Double.valueOf(0));
            
            Integer numTrappVi = (obj.getNumPiazzamento()!=null ? obj.getNumPiazzamento():0) +
                                  (obj.getNumRimozioneTrappole()!=null ? obj.getNumRimozioneTrappole():0) +
                                  (obj.getNumRicSostTrappole()!=null ? obj.getNumRicSostTrappole():0);                    
            obj.setNumOreIspezione(Double.valueOf((nf2.format(Formule.nHoursInspecting(obj.getPesoRiga(), obj.getOreOperazioni(), obj.getNumCampioni(), numTrappVi))).replace(',', '.')));
            obj.setTotaleCostoIspezione( (obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione():0) * (obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione():0));
            //campioni
            if(obj.getNumCampioni()!=null && obj.getNumCampioni()>0) {
                  obj.setNumOreRaccoltaCampioni(obj.getOreOperazioni());
                  obj.setTotaleCostoRaccCampioni(obj.getNumOreRaccoltaCampioni()*obj.getCostoOrarioTecCampionamento());
            } else {
              obj.setNumOreRaccoltaCampioni(Double.valueOf(0));
              obj.setTotaleCostoRaccCampioni(Double.valueOf(0));
            } 
            //trappole piazzate
            if(obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0){ 
              obj.setNumOrePiazzTrappole(obj.getNumPiazzamento()*obj.getOreOperazioni());
              obj.setCostoTotalePiazzTrappole(obj.getNumOrePiazzTrappole()*obj.getCostoOrarioTecTrappolaggio());
            }
            //trappole ric/sosti
            if(obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0){
              obj.setNumOreRicSostTrappole(obj.getNumRicSostTrappole()*obj.getOreOperazioni());
              obj.setCostoTotaleRicSostTrappole(obj.getNumOreRicSostTrappole()*obj.getCostoOrarioTecTrappolaggio());          
            }
            //trappole rimosse
            if(obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0){
              obj.setNumOreRimozioneTrappole(obj.getNumRimozioneTrappole()*obj.getOreOperazioni());
              obj.setCostoTotaleRitiroTrappole(obj.getNumOreRimozioneTrappole()*obj.getCostoOrarioTecTrappolaggio());         
            }
          }
        }
        pesoVerbale = dto.getPesoRiga();
      }
      appo = verbale;
    }

    for (RendicontazioneDTO obj:lista) {
      if(obj.getNumeroVerbale().equals(appo)) {
        obj.setPesoVerbale(Double.valueOf((nf2.format(pesoVerbale)).replace(',', '.')));
        if(obj.getPesoVerbale()!=0)
            obj.setOreOperazioni(Double.valueOf((nf2.format(obj.getOreTotaliSiti()/pesoVerbale)).replace(',', '.')));
        else
          obj.setOreOperazioni(Double.valueOf(0));
          
        Integer numTrappVi = (obj.getNumPiazzamento()!=null ? obj.getNumPiazzamento():0) +
                              (obj.getNumRimozioneTrappole()!=null ? obj.getNumRimozioneTrappole():0)+
                              (obj.getNumRicSostTrappole()!=null ? obj.getNumRicSostTrappole():0);
        obj.setNumOreIspezione(Double.valueOf((nf2.format(Formule.nHoursInspecting(obj.getPesoRiga(), obj.getOreOperazioni(), 
                                                obj.getNumCampioni(), numTrappVi))).replace(',', '.')));
        obj.setTotaleCostoIspezione( (obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione():0) * 
                                        (obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione():0));
        //campioni
        if(obj.getNumCampioni()!=null && obj.getNumCampioni()>0) {
              obj.setNumOreRaccoltaCampioni(obj.getOreOperazioni());
              obj.setTotaleCostoRaccCampioni(obj.getNumOreRaccoltaCampioni()*obj.getCostoOrarioTecCampionamento());
        } else {
          obj.setNumOreRaccoltaCampioni(Double.valueOf(0));
          obj.setTotaleCostoRaccCampioni(Double.valueOf(0));
        }
        //trappole piazzate
        if(obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0) {
          obj.setNumOrePiazzTrappole(obj.getNumPiazzamento()*obj.getOreOperazioni());
          obj.setCostoTotalePiazzTrappole(obj.getNumOrePiazzTrappole()*obj.getCostoOrarioTecTrappolaggio());
        }
        //trappole ric/sosti
        if(obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0){
          obj.setNumOreRicSostTrappole(obj.getNumRicSostTrappole()*obj.getOreOperazioni());
          obj.setCostoTotaleRicSostTrappole(obj.getNumOreRicSostTrappole()*obj.getCostoOrarioTecTrappolaggio());
        }
        //trappole rimosse
        if(obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0){
          obj.setNumOreRimozioneTrappole(obj.getNumRimozioneTrappole()*obj.getOreOperazioni());
          obj.setCostoTotaleRitiroTrappole(obj.getNumOreRimozioneTrappole()*obj.getCostoOrarioTecTrappolaggio());
        }
      }
    }
    return lista;
  }

  public List<OrganismoNocivoDTO> findOrganismiNocivi(RendicontazioneDTO rendicontazione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findOrganismiNocivi";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("anno", rendicontazione.getAnno(), Types.INTEGER);
    mapParameterSource.addValue("emergenza", rendicontazione.getArea());
    
   try
    {
      logger.debug(SELECT_ON);
      List<OrganismoNocivoDTO> list = queryForList(SELECT_ON, mapParameterSource, OrganismoNocivoDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_ON, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
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