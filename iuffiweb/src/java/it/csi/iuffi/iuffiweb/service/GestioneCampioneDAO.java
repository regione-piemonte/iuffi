package it.csi.iuffi.iuffiweb.service;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
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
import it.csi.iuffi.iuffiweb.model.CampionamentoDTO;
import it.csi.iuffi.iuffiweb.model.CampionamentoSpecOnDTO;
import it.csi.iuffi.iuffiweb.model.CodiceEsitoDTO;
import it.csi.iuffi.iuffiweb.model.EsitoCampioneDTO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.request.CampionamentoRequest;
import oracle.jdbc.OracleTypes;

public class GestioneCampioneDAO extends BaseDAO
{

  
  private static final String THIS_CLASS = GestioneCampioneDAO.class.getSimpleName();
  private LobHandler lobHandler;
  
  
  private static final String SELECT_CODICI_ESITO_VALIDI = "select * from IUF_D_CODICE_ESITO t where T.data_fine_validita is null ORDER BY CODICE";
  private static final String SELECT_CODICI_ESITO_ALL = "select * from IUF_D_CODICE_ESITO t ORDER BY CODICE";
  private static final String SELECT_CODICI_ESITO_ALL_ID_MULTIPLI = "select * from IUF_D_CODICE_ESITO t where ID_CODICE_ESITO in(%s)";
  private static final String SELECT_CODICI_ESITO_BY_ID = "select * from IUF_D_CODICE_ESITO t where T.id_codice_esito=:id";
 
  private static final String INSERT = "INSERT INTO iuf_t_campionamento(" +
    "id_campionamento," + 
   "id_rilevazione," + 
   "id_specie_vegetale," +
   "id_tipo_campione," +
   "istat_comune," +
   "latitudine," +
   "longitudine," +
   "ext_id_utente_aggiornamento," +
   "data_ultimo_aggiornamento," +
   "data_ora_inizio," +
   "data_ora_fine," +
   "id_anagrafica," +
   "id_ispezione_visiva"+
   ") " + 
   "VALUES(seq_iuf_t_campionamento.nextval, :idRilevazione, :idSpecieVegetale, :idTipoCampione, :istatComune, :latitudine, :longitudine, " + 
   ":ext_id_utente_aggiornamento, SYSDATE, :dataOraInizio, :dataOraFine, :idAnagrafica, :idIspezioneVisiva)";
  
  private static final String UPDATE = "UPDATE iuf_t_campionamento " +
  "set id_rilevazione = :idRilevazione, id_specie_vegetale = :idSpecieVegetale, id_tipo_campione = :idTipoCampione, " + 
  " istat_comune = :istatComune, latitudine = :latitudine, longitudine = :longitudine, data_rilevazione = NVL(:dataRilevazione,SYSDATE), ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE, " +
  " data_ora_inizio = :dataOraInizio, data_ora_fine = :dataOraFine, id_anagrafica = :idAnagrafica, note = :note WHERE id_campionamento = :idCampionamento";
  
  private static final String INSERT_CAMPIONE_SPECIE_ON = "INSERT INTO iuf_r_campionamento_spec_on(id_campionamento, id_specie_on, presenza, ext_id_utente_aggiornamento, data_ultimo_aggiornamento)"+
      "VALUES(:idCampionamento, :idSpecieOn, :presenza, :ext_id_utente_aggiornamento, SYSDATE)";
  
  
  private static final String UPDATE_CAMPIONE_SPECIE_ON = "UPDATE iuf_r_campionamento_spec_on set id_specie_on = :idSpecieOn, presenza = :presenza, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
  "where id_campionamento = :idCampionamento and id_specie_on = :idSpecieOn";

  
  private static final String FILTER_COMUNE = 
      "  AND EXISTS (SELECT 1 \r\n" +
      "                FROM (\r\n" +
      "                     SELECT cc.id_campionamento, cc.istat_comune, amm.descrizione_comune \r\n" + 
      "                      FROM iuf_t_rilevazione rr, iuf_t_campionamento cc, smrgaa_v_dati_amministrativi amm \r\n" + 
      "                     WHERE rr.id_rilevazione = cc.id_rilevazione (+) \r\n" + 
      "                       AND cc.istat_comune = amm.istat_comune (+) \r\n" +  
      "              ) WHERE id_campionamento = a.id_campionamento \r\n" +
      "                  AND istat_comune = NVL(:istatComune,istat_comune) \r\n" +
      "                  AND UPPER(descrizione_comune) LIKE NVL(UPPER(:descComune),'%')) \r\n";

  public GestioneCampioneDAO(){  }
  
 
  public List<CampionamentoDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT_ALL = " SELECT * FROM IUF_T_CAMPIONAMENTO t  ORDER BY t.ID_CAMPIONAMENTO ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL);
      return queryForList(SELECT_ALL, mapParameterSource, CampionamentoDTO.class);
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

  public List<CampionamentoDTO> findByFilter(CampionamentoRequest campione, Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    String SELECT_BY_FILTER = "select A.ID_CAMPIONAMENTO," +
        "A.ID_RILEVAZIONE," +
        "A.DATA_RILEVAZIONE," +
        "a.id_anagrafica," +
        "D.COGNOME || ' ' || D.NOME AS ISPETTORE," + 
        "F.DETTAGLIO_TIPO_AREA AS AREA," + 
        "E.NOME_VOLGARE AS SPECIE," + 
        "(SELECT LISTAGG(aa.nome_latino, '; ') \r\n" + 
        "                     WITHIN GROUP (ORDER BY aa.nome_latino)\r\n" + 
        "                      from IUF_R_CAMPIONAMENTO_SPEC_ON ggg, \r\n" + 
        "                     iuf_d_organismo_nocivo aa where ggg.id_specie_on=aa.id_organismo_nocivo\r\n" + 
        "                     and ggg.id_campionamento=a.id_campionamento) as ORGANISMI," +
        "c.ext_numero_aviv,"+
        "c.cuaa,"+
        "G.TIPOLOGIA_CAMPIONE as TIPO_CAMPIONE," + 
        "(CASE WHEN(select count(*) from iuf_r_campione_foto d where d.id_campionamento=a.id_campionamento)>0 THEN 'S' ELSE 'N' END) AS FOTO," + 
        "'ESITO' AS ESITO," +
        "m.id_missione," +
        "A.ID_TIPO_CAMPIONE," +
        "A.ID_SPECIE_VEGETALE," +
        "A.ISTAT_COMUNE," +
        "amm.descrizione_comune as comune," +
        "A.LATITUDINE," +
        "A.LONGITUDINE," +
        "A.ext_id_utente_aggiornamento," +
        "A.data_ultimo_aggiornamento," +
        "A.data_ora_inizio," +
        "A.data_ora_fine," +
        "a.id_ispezione_visiva," +
        //"i.num_registro_lab, " +
        "a.note,\r\n" +
        //"i.id_esito_campione, "+
        //"i.id_codice_esito "+
        "(SELECT NVL(MAX(v.id_stato_verbale),0) FROM iuf_t_verbale v WHERE v.id_missione = m.id_missione) id_stato_verbale, \r\n" +
        "i.superfice as superficie," + 
        "i.numero_piante as numero_piante " +
        "FROM IUF_T_CAMPIONAMENTO a,IUF_T_MISSIONE m," +
            "IUF_T_RILEVAZIONE C,IUF_T_ANAGRAFICA D,IUF_D_SPECIE_VEGETALE E,IUF_D_TIPO_AREA F," + 
            //"IUF_D_TIPO_CAMPIONE G,IUF_T_ESITO_CAMPIONE I,IUF_D_CODICE_ESITO H,smrgaa_v_dati_amministrativi amm " + 
            "IUF_D_TIPO_CAMPIONE G,smrgaa_v_dati_amministrativi amm, iuf_t_ispezione_visiva i " +
        "WHERE A.ID_RILEVAZIONE = C.ID_RILEVAZIONE " + 
        "AND C.ID_MISSIONE = m.ID_MISSIONE " + 
        "AND a.id_anagrafica = D.ID_ANAGRAFICA " + 
        "AND A.ID_SPECIE_VEGETALE = E.ID_SPECIE_VEGETALE " + 
        "AND C.ID_TIPO_AREA = F.ID_TIPO_AREA " + 
        "AND A.ID_TIPO_CAMPIONE = G.ID_TIPO_CAMPIONE " +
        "AND a.istat_comune = amm.istat_comune (+) " +
        "AND a.id_ispezione_visiva = i.id_ispezione (+) " +
        //"AND I.ID_CAMPIONAMENTO(+)=A.ID_CAMPIONAMENTO "+
        //"AND I.id_codice_esito=H.ID_CODICE_ESITO(+)"+
        "AND TRUNC(m.data_ora_inizio_missione) BETWEEN TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2100'),'DD/MM/YYYY') \r\n";

      if(campione.getIdRilevazione()!=null && campione.getIdRilevazione()>0)
          SELECT_BY_FILTER += " and A.ID_RILEVAZIONE =:idRilevazione ";
      if(campione.getIdCampionamento()!=null && campione.getIdCampionamento()>0)
        SELECT_BY_FILTER += " and A.ID_CAMPIONAMENTO =:idCampionamento ";      
      if(campione.getIdMissione()!=null && campione.getIdMissione()>0)
        SELECT_BY_FILTER += " and m.ID_MISSIONE =:idMissione ";
      if(campione.getIspettore()!=null && !campione.getIspettore().equals(""))
        SELECT_BY_FILTER += " and (UPPER (D.COGNOME) LIKE UPPER(:ispettore) OR UPPER (D.NOME) LIKE UPPER(:ispettore)) ";

      // filtro per obiettivo missione Indagine ufficiale
      if (campione.getObiettivoIndagineUfficiale() != null && campione.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
        //select += FILTER_OBIETTIVO_INDAGINE_UFFICIALE;
        if (campione.getObiettivoEmergenza() == null || !campione.getObiettivoEmergenza().equalsIgnoreCase("S")) {
          SELECT_BY_FILTER += " AND NVL(c.flag_emergenza,'N') = 'N' ";
        }
      }
      // filtro per obiettivo missione Emergenza
      if (campione.getObiettivoEmergenza() != null && campione.getObiettivoEmergenza().equalsIgnoreCase("S")) {
        if (campione.getObiettivoIndagineUfficiale() == null || !campione.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
          SELECT_BY_FILTER += " AND c.flag_emergenza = 'S' ";
        }
      }
      
      if(campione.getNumRegistroLab()!=null && !campione.getNumRegistroLab().equals(""))
       SELECT_BY_FILTER += " and i.num_registro_lab =:numRegistro ";

      if (campione.getIstatComune() != null && campione.getIstatComune().trim().length() > 0 || StringUtils.isNotBlank(campione.getDescComune())) {
        SELECT_BY_FILTER += FILTER_COMUNE;
        if (campione.getIstatComune() != null && campione.getIstatComune().trim().length() > 0) {
          mapParameterSource.addValue("istatComune", campione.getIstatComune());
        }
        else
          mapParameterSource.addValue("istatComune", null, Types.NULL);
        
        if (StringUtils.isNotBlank(campione.getDescComune())) {
          mapParameterSource.addValue("descComune", campione.getDescComune());
        }
        else
          mapParameterSource.addValue("descComune", null, Types.NULL);
      }      

      // filtro multiplo per tipo area
      if (campione.getTipoArea() != null && campione.getTipoArea().size() > 0) {
        String tipoAree = "";
        for (int i=0; i<campione.getTipoArea().size(); i++) {
          tipoAree += campione.getTipoArea().get(i);
          if (i < campione.getTipoArea().size()-1)
            tipoAree += ",";
        }
        SELECT_BY_FILTER += " AND c.id_tipo_area IN (" + tipoAree + ") ";
      }
      // filtro multiplo per ispettore assegnato
      if (campione.getIspettoreAssegnato() != null && campione.getIspettoreAssegnato().size() > 0) {
        String ispettori = "";
        for (int i=0; i<campione.getIspettoreAssegnato().size(); i++) {
          ispettori += campione.getIspettoreAssegnato().get(i);
          if (i < campione.getIspettoreAssegnato().size()-1)
            ispettori += ",";
        }
        SELECT_BY_FILTER += " AND a.id_anagrafica IN (" + ispettori + ") ";
      }
      // filtro multiplo per ispettori secondari
      if (campione.getIspettoriSecondari() != null && campione.getIspettoriSecondari().size() > 0) {
        String ispettori = "";
        for (int i=0; i<campione.getIspettoriSecondari().size(); i++) {
          ispettori += campione.getIspettoriSecondari().get(i);
          if (i < campione.getIspettoriSecondari().size()-1)
            ispettori += ",";
        }
        SELECT_BY_FILTER += " AND EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia WHERE ia.id_missione = m.id_missione AND ia.id_anagrafica IN (" + ispettori + ")) ";
      }
      // filtro multiplo per specie vegetale
      if (campione.getSpecieVegetale() != null && campione.getSpecieVegetale().size() > 0) {
        String specieVegetali = "";
        for (int i=0; i<campione.getSpecieVegetale().size(); i++) {
          specieVegetali += campione.getSpecieVegetale().get(i);
          if (i < campione.getSpecieVegetale().size()-1)
            specieVegetali += ",";
        }
        SELECT_BY_FILTER += " AND a.id_specie_vegetale IN (" + specieVegetali + ") ";
      }
      // filtro multiplo per organismo nocivo
      if (campione.getOrganismoNocivo() != null && campione.getOrganismoNocivo().size() > 0) {
        String organismiNocivi = "";
        for (int i=0; i<campione.getOrganismoNocivo().size(); i++) {
          organismiNocivi += campione.getOrganismoNocivo().get(i);
          if (i < campione.getOrganismoNocivo().size()-1)
            organismiNocivi += ",";
        }
        SELECT_BY_FILTER += " AND EXISTS (SELECT 1 FROM iuf_r_campionamento_spec_on o WHERE o.id_campionamento = a.id_campionamento AND o.id_specie_on IN (" + organismiNocivi + ")) ";
      }
      // filtro multiplo per tipologia campione
      if (campione.getTipoCampioni() != null && campione.getTipoCampioni().size() > 0) {
        String tipoCampioni = "";
        for (int i=0; i<campione.getTipoCampioni().size(); i++) {
          tipoCampioni += campione.getTipoCampioni().get(i);
          if (i < campione.getTipoCampioni().size()-1)
            tipoCampioni += ",";
        }
        SELECT_BY_FILTER += " AND a.id_tipo_campione IN (" + tipoCampioni + ") ";
      }
      
      //filtro per cuaa o n. autorizzazione
      if(campione.getCuaa()!=null && !campione.getCuaa().equals(""))
        SELECT_BY_FILTER += " AND (c.ext_numero_aviv =:cuaa OR c.cuaa=:cuaa) ";
      
      // filtro multiplo per tipo test
      if (campione.getTipologiaTest() != null && campione.getTipologiaTest().size() > 0) {
        String tipoTest = "";
        for (int i=0; i<campione.getTipologiaTest().size(); i++) {
          tipoTest += campione.getTipologiaTest().get(i);
          if (i < campione.getTipologiaTest().size()-1)
            tipoTest += ",";
        }
        SELECT_BY_FILTER += " AND i.id_esito_campione IN (" + tipoTest + ") ";
      }

      // filtro multiplo per esito test
      if (campione.getCodiciTest() != null && campione.getCodiciTest().size() > 0) {
        String codiciTest = "";
        for (int i=0; i<campione.getCodiciTest().size(); i++) {
          codiciTest += campione.getCodiciTest().get(i);
          if (i < campione.getCodiciTest().size()-1)
            codiciTest += ",";
        }
        SELECT_BY_FILTER += " AND i.id_codice_esito IN (" + codiciTest + ") ";
      }

      if (campione.getAnno() != null && campione.getAnno().intValue() > 0) {
        SELECT_BY_FILTER += " AND TO_CHAR(m.data_ora_inizio_missione,'YYYY') = :anno \r\n";
        mapParameterSource.addValue("anno", campione.getAnno().toString());
      }
      
      if (campione.getIdIspezioneVisiva() != null && campione.getIdIspezioneVisiva() > 0) {
        SELECT_BY_FILTER += " AND a.id_ispezione_visiva = :idIspezioneVisiva ";
        mapParameterSource.addValue("idIspezioneVisiva", campione.getIdIspezioneVisiva());
      }
      
      // Filtro per Anagrafica (ispettore loggato)
      if (idAnagrafica != null && idAnagrafica > 0) {
        SELECT_BY_FILTER += " AND (m.id_ispettore_assegnato = :idAnagrafica " +
                       "OR EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia " +
                                   "WHERE ia.id_missione = m.id_missione AND ia.id_anagrafica = :idAnagrafica))";
        mapParameterSource.addValue("idAnagrafica", idAnagrafica);
      }
      // Filtro per Ente (ente ispettore loggato)
      if (idEnte != null && idEnte > 0) {
        SELECT_BY_FILTER += " AND d.id_ente = :idEnte";
        mapParameterSource.addValue("idEnte", idEnte);
      }

      mapParameterSource.addValue("dallaData", (campione.getDallaData()!=null)? new SimpleDateFormat("dd/MM/yyyy").format(campione.getDallaData()):"");
      mapParameterSource.addValue("allaData", (campione.getAllaData()!=null)? new SimpleDateFormat("dd/MM/yyyy").format(campione.getAllaData()):"");
      mapParameterSource.addValue("idRilevazione", campione.getIdRilevazione(), Types.INTEGER);
      mapParameterSource.addValue("idCampionamento", campione.getIdCampionamento(), Types.INTEGER);
      mapParameterSource.addValue("idMissione", campione.getIdMissione(), Types.INTEGER);
      mapParameterSource.addValue("ispettore", "%"+campione.getIspettore()+"%", Types.VARCHAR);
      mapParameterSource.addValue("cuaa", campione.getCuaa(), Types.VARCHAR);
      mapParameterSource.addValue("emergenza", campione.getObiettivoEmergenza(), Types.VARCHAR);
      mapParameterSource.addValue("numRegistro", campione.getNumRegistroLab(), Types.VARCHAR);
      SELECT_BY_FILTER += " order by a.data_ora_inizio desc";

    try
    {
      logger.debug(SELECT_BY_FILTER);
      List<CampionamentoDTO> list = namedParameterJdbcTemplate.query(SELECT_BY_FILTER, mapParameterSource, getRowMapperCampionamento());
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_FILTER, mapParameterSource);
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
  
  public CampionamentoDTO findById(Integer idCampionamento) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT c.data_ora_inizio,\r\n" +
                          "       c.id_campionamento,\r\n" +
                          "       c.id_rilevazione,\r\n" +
                          "       a.cognome || ' ' || a.nome AS ispettore,\r\n" +
                          "       ta.desc_tipo_area AS area,\r\n" +
                          "       ta.dettaglio_tipo_area AS dettaglio_area,\r\n" +
                          "       sv.genere_specie AS specie,\r\n" +
                          "       sv.nome_volgare as pianta,\r\n" +
                          "       iv.numero_aviv,\r\n" +
                          "       c.id_specie_vegetale,\r\n" +
                          "       c.istat_comune,\r\n" +
                          "       v.descrizione_comune comune,\r\n" +
                          "       c.latitudine, \r\n" +
                          "       c.longitudine,\r\n" +
                          "       m.id_missione,\r\n" +
                          "       m.data_ora_inizio_missione AS data_missione,\r\n" +
                          "       r.cuaa,\r\n" +
                          "       c.id_tipo_campione,\r\n" +
                          "       tc.tipologia_campione tipo_campione,\r\n" +
                          "       c.id_anagrafica,\r\n" +
                          "       ac.cognome||' '||ac.nome ispettore_campionamento,\r\n" +
                          "       c.id_ispezione_visiva,\r\n" +
                          "       r.id_tipo_area,\r\n" +
                          "       c.note,\r\n" +
                          "  to_char(c.data_ora_inizio, 'HH24:MI:SS') as ora_inizio,\r\n" + 
                          "  to_char(c.data_ora_fine, 'HH24:MI:SS') as ora_fine,\r\n"+
                          "  to_char(m.data_ora_inizio_missione, 'HH24:MI:SS') as ora_inizio_missione,\r\n" + 
                          "  to_char(m.data_ora_fine_missione, 'HH24:MI:SS') as ora_fine_missione,\r\n"+
                          "  (SELECT LISTAGG(ispa.id_anagrafica, ', ') WITHIN GROUP (ORDER BY ispa.id_anagrafica) from IUF_R_ISPETTORE_AGGIUNTO ispa\r\n" + 
                          "  where ispa.id_missione=m.id_missione) as ispettori_aggiunti,\r\n" + 
                          "(CASE WHEN(select count(*) from iuf_r_campione_foto d where d.id_campionamento=c.id_campionamento)>0 THEN 'S' ELSE 'N' END) AS FOTO,\r\n" +
                          "(SELECT LISTAGG(aa.nome_latino, '; ') \r\n" + 
                          "                     WITHIN GROUP (ORDER BY aa.nome_latino)\r\n" + 
                          "                      from IUF_R_CAMPIONAMENTO_SPEC_ON ggg, \r\n" + 
                          "                     iuf_d_organismo_nocivo aa where ggg.id_specie_on=aa.id_organismo_nocivo\r\n" + 
                          "                     and ggg.id_campionamento=c.id_campionamento) as ORGANISMI \r\n" +
                          "FROM iuf_t_campionamento c, iuf_t_rilevazione r, iuf_t_missione m, iuf_d_tipo_campione tc,\r\n" +
                          "     iuf_t_anagrafica a, iuf_d_specie_vegetale sv, iuf_d_tipo_area ta,\r\n" +
                          "     iuf_t_ispezione_visiva iv, smrgaa_v_dati_amministrativi v,\r\n" +
                          "     iuf_t_anagrafica ac \r\n" +
                          "WHERE c.id_rilevazione = r.id_rilevazione\r\n" +
                          "  AND r.id_missione = m.id_missione\r\n" +
                          "  AND m.id_ispettore_assegnato = a.id_anagrafica\r\n" +
                          "  AND c.id_specie_vegetale = sv.id_specie_vegetale\r\n" +
                          "  AND r.id_tipo_area = ta.id_tipo_area\r\n" +
                          "  AND c.id_tipo_campione = tc.id_tipo_campione\r\n" +
                          "  AND c.istat_comune = v.ISTAT_COMUNE (+)\r\n" +
                          "  AND c.id_ispezione_visiva = iv.id_ispezione (+)\r\n" +
                          "  AND c.id_anagrafica = ac.id_anagrafica \r\n" +
                          "  AND c.id_campionamento = :idCampionamento";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idCampionamento", idCampionamento);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      //CampionamentoDTO campione = queryForObject(SELECT_BY_ID, mapParameterSource, CampionamentoDTO.class);
      CampionamentoDTO campione = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, mapParameterSource, new RowMapper<CampionamentoDTO>() {
                @Override  
                public CampionamentoDTO mapRow(ResultSet rs, int rownumber) throws SQLException {
                  CampionamentoDTO camp = new CampionamentoDTO();
                  camp.setDataOraInizio(rs.getTimestamp("data_ora_inizio"));
                  camp.setIdCampionamento(rs.getInt("id_campionamento"));
                  camp.setIdRilevazione(rs.getInt("id_rilevazione"));
                  camp.setIspettore(rs.getString("ispettore"));
                  camp.setArea(rs.getString("area"));
                  camp.setDettaglioArea(rs.getString("dettaglio_area"));
                  camp.setSpecie(rs.getString("specie"));
                  camp.setPianta(rs.getString("pianta"));
                  camp.setExtNumeroAviv(rs.getString("numero_aviv"));
                  camp.setIdSpecieVegetale(rs.getInt("id_specie_vegetale"));
                  camp.setIstatComune(rs.getString("istat_comune"));
                  camp.setComune(rs.getString("comune"));
                  camp.setLatitudine(rs.getDouble("latitudine"));
                  camp.setLongitudine(rs.getDouble("longitudine"));
                  camp.setIdMissione(rs.getInt("id_missione"));
                  camp.setDataMissione(rs.getTimestamp("data_missione"));
                  camp.setCuaa(rs.getString("cuaa"));
                  camp.setIdTipoCampione(rs.getInt("id_tipo_campione"));
                  camp.setTipoCampione(rs.getString("tipo_campione"));
                  camp.setIdAnagrafica(rs.getInt("id_anagrafica"));
                  camp.setIspettoreCampionamento(rs.getString("ispettore_campionamento"));
                  camp.setIdIspezioneVisiva(rs.getInt("id_ispezione_visiva"));
                  camp.setIdTipoArea(rs.getInt("id_tipo_area"));
                  camp.setNote(rs.getString("note"));
                  camp.setOraInizio(rs.getString("ora_inizio"));
                  camp.setOraFine(rs.getString("ora_fine"));
                  camp.setOraInizioMissione(rs.getString("ora_inizio_missione"));
                  camp.setOraFineMissione(rs.getString("ora_fine_missione"));
                  camp.setIspettoriAggiunti(rs.getString("ispettori_aggiunti"));
                  camp.setFoto(rs.getString("foto").equals("S"));
                  camp.setOrganismi(rs.getString("organismi"));
              return camp;
          }
      });
      return campione;
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
   
  public List<FotoDTO> findFotoByIdCampione(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findFotoByIdCampione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "select c.id_foto,c.nome_file,c.data_foto,c.tag,c.note,c.latitudine,c.longitudine " + 
        "from iuf_r_campione_foto a, iuf_t_campionamento b," + 
        "iuf_t_foto c " + 
        "where b.id_campionamento=a.id_campionamento " + 
        "and a.id_foto=c.id_foto ";
    if(id!=null && id>0)
      SELECT_BY_ID+= "and b.id_campionamento=:id";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      List<FotoDTO> fotoArray = new ArrayList<FotoDTO>();
      List<FotoDTO> foto = queryForList(SELECT_BY_ID, mapParameterSource, FotoDTO.class);
      if (foto != null) {
        for(FotoDTO objFoto: foto) {
          FotoDTO modifyFoto= this.findFotoById(objFoto.getIdFoto());
          modifyFoto.setLongitudine(objFoto.getLongitudine());
          modifyFoto.setLatitudine(objFoto.getLatitudine());
          fotoArray.add(modifyFoto);
       }
      }
      return fotoArray;
    } catch (Throwable t)
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

  public List<EsitoCampioneDTO> findEsitiByIdCampionamento(Integer idCampionamento) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findEsitoByIdCampione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT t.id_esito_campione,t.id_campionamento,t.num_registro_lab,t.id_codice_esito,t.cod_campione,\r\n" +
                          "       t.id_anfi,anfi.tipologia_test_di_laboratorio,t.nome_file,cod.descrizione\r\n" +
                          "  FROM iuf_t_esito_campione t, iuf_t_campionamento c, iuf_d_anfi anfi,IUF_D_CODICE_ESITO cod \r\n" + 
                          " WHERE t.id_campionamento = c.id_campionamento\r\n" + 
                          "   AND c.id_campionamento = :idCampionamento \r\n" +
                          "   AND t.id_anfi = anfi.id_anfi \r\n" +
                          "   AND t.id_codice_esito=cod.ID_CODICE_ESITO(+)" +
                          " ORDER BY 1";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idCampionamento", idCampionamento);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      List<EsitoCampioneDTO> esiti = queryForList(SELECT_BY_ID, mapParameterSource, EsitoCampioneDTO.class);
      return esiti;
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

  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findFotoById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "select c.id_foto,c.nome_file,c.data_foto,c.tag,c.note,c.foto," +
                                "c.ext_id_utente_aggiornamento " +
                           "from iuf_t_foto c " + 
                           "where c.id_foto =:id";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      FotoDTO foto = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, mapParameterSource, getRowMapperFoto());
      return foto;
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
  
  public List<CampionamentoSpecOnDTO> findOnByIdCampionamento(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findOnByIdCampionamento";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT t.id_campionamento,t.id_specie_on,t.presenza,t.ext_id_utente_aggiornamento," + 
                              "t.data_ultimo_aggiornamento,a.sigla as desc_sigla,a.nome_latino as nome_latino," +
                              "a.euro,a.flag_emergenza emergenza " +
                          "FROM IUF_R_CAMPIONAMENTO_SPEC_ON t,IUF_D_ORGANISMO_NOCIVO A " + 
                          "WHERE t.id_specie_on=a.id_organismo_nocivo " + 
                          "AND t.id_campionamento= :id " +
                          "ORDER BY a.nome_latino";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      List<CampionamentoSpecOnDTO> lista = queryForList(SELECT_BY_ID, mapParameterSource, CampionamentoSpecOnDTO.class);
      if(lista!=null && !lista.isEmpty()) {
        for(CampionamentoSpecOnDTO obj : lista) {
          obj.setAssociato("S");
        }        
      }
     return lista;
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
  
  public List<CampionamentoDTO> findByIdRilevazione(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByIdRilevazione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID_RIL  = "SELECT A.ID_CAMPIONAMENTO," +
                              "A.ID_RILEVAZIONE," +
                              "A.DATA_RILEVAZIONE," + 
                              "D.COGNOME || ' ' || D.NOME AS ISPETTORE," + 
                              "F.DESC_TIPO_AREA AS AREA," + 
                              "E.GENERE_SPECIE AS SPECIE," + 
                              "'ORGANISMI' as ORGANISMI," + 
                              "G.TIPOLOGIA_CAMPIONE as TIPOCAMPIONE," + 
                              //"'FOTO' AS FOTO," +   è un boolean l'attributo di CampionamentoDTO
                              "'NOTE' AS NOTE," + 
                              "'ESITO' AS ESITO," +
                              "B.ID_MISSIONE," +
                              "A.ID_TIPO_CAMPIONE," +
                              "A.ID_SPECIE_VEGETALE," +
                              "A.ISTAT_COMUNE," +
                              "A.LATITUDINE," +
                              "A.LONGITUDINE," +
                              "a.ext_id_utente_aggiornamento," +
                              "a.data_ultimo_aggiornamento " +
                              
                              "FROM IUF_T_CAMPIONAMENTO a,IUF_T_MISSIONE B," +
                              "IUF_T_RILEVAZIONE C,IUF_T_ANAGRAFICA D,IUF_D_SPECIE_VEGETALE E,IUF_D_TIPO_AREA F," + 
                              "IUF_D_TIPO_CAMPIONE G " + 

                              "WHERE A.ID_RILEVAZIONE=C.ID_RILEVAZIONE " + 
                              "AND C.ID_MISSIONE=B.ID_MISSIONE " + 
                              "AND B.ID_ISPETTORE_ASSEGNATO=D.ID_ANAGRAFICA " + 
                              "AND A.ID_SPECIE_VEGETALE=E.ID_SPECIE_VEGETALE " + 
                              "AND C.ID_TIPO_AREA=F.ID_TIPO_AREA " + 
                              "AND A.ID_TIPO_CAMPIONE=G.ID_TIPO_CAMPIONE "+
                              "AND A.ID_RILEVAZIONE= :id";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID_RIL);
      List<CampionamentoDTO> lista = queryForList(SELECT_BY_ID_RIL, mapParameterSource, CampionamentoDTO.class);
      return lista;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_ID_RIL, mapParameterSource);
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
        lobHandler = new OracleLobHandler();
        byte[] file = lobHandler.getBlobAsBytes(rs, "foto");
        String base64 = DatatypeConverter.printBase64Binary(file);
        foto.setBase64(base64);
        foto.setFoto(file);
        foto.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
        return foto;
      }
    };
  } 

  public LobHandler getLobHandler() {
    return lobHandler;
    }

  public void setLobHandler(LobHandler lobHandler) {
    this.lobHandler = lobHandler;
  }

  public void removeEsito(Integer idEsitoCampione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "removeEsito";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    final String DELETE_ESITO = "DELETE FROM iuf_t_esito_campione WHERE id_esito_campione = :idEsitoCampione";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idEsitoCampione", idEsitoCampione);

    try
    {
      logger.debug(DELETE_ESITO);
      namedParameterJdbcTemplate.update(DELETE_ESITO, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, DELETE_ESITO, mapParameterSource);
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

  public EsitoCampioneDTO findEsitoById(Integer idEsitoCampione) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findEsitoById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT e.* " +
                          "FROM iuf_t_esito_campione e " + 
                          "WHERE e.id_esito_campione = :idEsitoCampione";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idEsitoCampione", idEsitoCampione);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      EsitoCampioneDTO esito = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, mapParameterSource, getRowMapper());
      return esito;
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

  private RowMapper<EsitoCampioneDTO> getRowMapper() {
    return new RowMapper<EsitoCampioneDTO>() {
      
      @Override
      public EsitoCampioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        LobHandler lobHandler;
        EsitoCampioneDTO esito = new EsitoCampioneDTO();
        esito.setIdEsitoCampione(rs.getInt("id_esito_campione"));
        esito.setCodCampione(rs.getString("cod_campione"));
        esito.setIdAnfi(rs.getInt("id_anfi"));
        esito.setIdCodiceEsito(rs.getInt("id_codice_esito"));
        esito.setDataUltimoAggiornamento(rs.getTimestamp("data_ultimo_aggiornamento"));
        esito.setIdCampionamento(rs.getInt("id_campionamento"));
        esito.setNomeFile(rs.getString("nome_file"));
        esito.setNumRegistroLab(rs.getString("num_registro_lab"));
        lobHandler = new DefaultLobHandler();
        byte[] filePdf = lobHandler.getBlobAsBytes(rs, "referto");
        esito.setReferto(filePdf);
        esito.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
        return esito;
      }
    };
  }
  
  private RowMapper<CampionamentoDTO> getRowMapperCampionamento() {
    return new RowMapper<CampionamentoDTO>() {
      
      @Override
      public CampionamentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        CampionamentoDTO campionamento = new CampionamentoDTO();
        campionamento.setIdCampionamento(rs.getInt("id_campionamento"));
        campionamento.setIdRilevazione(rs.getInt("id_rilevazione"));
        campionamento.setDataRilevazioneF(rs.getTimestamp("data_rilevazione"));
        campionamento.setIspettore(rs.getString("ispettore"));
        campionamento.setArea(rs.getString("area"));
        campionamento.setSpecie(rs.getString("specie"));
        campionamento.setOrganismi(rs.getString("organismi"));
        campionamento.setExtNumeroAviv(rs.getString("ext_numero_aviv"));
        campionamento.setCuaa(rs.getString("cuaa"));
        campionamento.setTipoCampione(rs.getString("tipo_campione"));
        campionamento.setFoto((rs.getString("foto").equalsIgnoreCase("S"))?true:false);
        campionamento.setEsito(rs.getString("esito"));
        campionamento.setIdMissione(rs.getInt("id_missione"));
        campionamento.setIdTipoCampione(rs.getInt("id_tipo_campione"));
        campionamento.setIdSpecieVegetale(rs.getInt("id_specie_vegetale"));
        campionamento.setIstatComune(rs.getString("istat_comune"));
        campionamento.setComune(rs.getString("comune"));
        campionamento.setLatitudine(rs.getDouble("latitudine"));
        campionamento.setLongitudine(rs.getDouble("longitudine"));
        campionamento.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
        campionamento.setDataUltimoAggiornamento(rs.getTimestamp("data_ultimo_aggiornamento"));
        campionamento.setDataOraInizio(rs.getTimestamp("data_ora_inizio"));
        campionamento.setDataOraFine(rs.getTimestamp("data_ora_fine"));
        campionamento.setIdIspezioneVisiva(rs.getInt("id_ispezione_visiva"));
        campionamento.setNote(rs.getString("note"));
        campionamento.setIdAnagrafica(rs.getInt("id_anagrafica"));
        campionamento.setIdStatoVerbale(rs.getInt("id_stato_verbale"));
        campionamento.setSuperficie(rs.getInt("superficie"));
        campionamento.setNumeroPiante(rs.getInt("numero_piante"));
        return campionamento;
      }
    };
  }

  public EsitoCampioneDTO insertEsitoCampione(EsitoCampioneDTO esitoCampione) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();
    String THIS_METHOD = "[" + THIS_CLASS + "::insertEsitoCampione]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String INSERT = "INSERT INTO iuf_t_esito_campione(id_esito_campione, id_campionamento, cod_campione, "+
                                 "id_anfi, id_codice_esito, num_registro_lab, nome_file, referto," +
                                            "ext_id_utente_aggiornamento, data_ultimo_aggiornamento) " +
                          "VALUES(seq_iuf_t_esito_campione.nextval, :idCampionamento, :codCampione, :idAnfi, :idCodiceEsito, "
                          + ":numRegistroLab, :nomeFile, :referto, :utente, SYSDATE)";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {
      mapParameterSource.addValue("idCampionamento", esitoCampione.getIdCampionamento(), Types.VARCHAR);
      mapParameterSource.addValue("codCampione", esitoCampione.getCodCampione(), Types.VARCHAR);
      mapParameterSource.addValue("idAnfi", esitoCampione.getIdAnfi());
      mapParameterSource.addValue("idCodiceEsito", esitoCampione.getIdCodiceEsito(), Types.VARCHAR);
      mapParameterSource.addValue("numRegistroLab", esitoCampione.getNumRegistroLab(), Types.VARCHAR);
      mapParameterSource.addValue("nomeFile", esitoCampione.getNomeFile(), Types.VARCHAR);
      mapParameterSource.addValue("utente", esitoCampione.getExtIdUtenteAggiornamento());
      
      if (esitoCampione.getReferto() != null) {
          //byte[] bytes = esitoCampione.getReferto().getBytes();
          byte[] bytes = esitoCampione.getFileByte();
          mapParameterSource.addValue("referto", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), OracleTypes.BLOB);
      } else
          mapParameterSource.addValue("referto", null, Types.BLOB);
      
      logger.debug(INSERT);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_esito_campione"});
      esitoCampione.setIdEsitoCampione(holder.getKey().intValue());
      return esitoCampione;
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

  public void updateEsitoCampione(EsitoCampioneDTO esitoCampione) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateEsitoCampione]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String UPDATE = "UPDATE iuf_t_esito_campione " +
                          "SET cod_campione = :codCampione," +
                          " id_anfi = :idAnfi," +
                          " id_codice_esito = :idCodiceEsito," +
                          " num_registro_lab = :numRegistroLab," +
                          " nome_file = :nomeFile," +
                          " referto = :referto," +
                          " ext_id_utente_aggiornamento = :utente," +
                          " data_ultimo_aggiornamento = SYSDATE " +
                          "WHERE id_esito_campione = :idEsitoCampione";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try  {
      mapParameterSource.addValue("idEsitoCampione", esitoCampione.getIdEsitoCampione());
      mapParameterSource.addValue("codCampione", esitoCampione.getCodCampione(), Types.VARCHAR);
      mapParameterSource.addValue("idAnfi", esitoCampione.getIdAnfi());
      mapParameterSource.addValue("idCodiceEsito", esitoCampione.getIdCodiceEsito(), Types.VARCHAR);
      mapParameterSource.addValue("numRegistroLab", esitoCampione.getNumRegistroLab(), Types.VARCHAR);
      //mapParameterSource.addValue("nomeFile", esitoCampione.getNomeFile(), Types.VARCHAR);
      mapParameterSource.addValue("utente", esitoCampione.getExtIdUtenteAggiornamento());
      EsitoCampioneDTO old = this.findEsitoById(esitoCampione.getIdEsitoCampione());
      
     /* if (esitoCampione.getReferto() != null && esitoCampione.getReferto().getSize() > 0) {
        byte[] bytes = esitoCampione.getReferto().getBytes();
        mapParameterSource.addValue("referto", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), Types.BLOB);
        mapParameterSource.addValue("nomeFile", esitoCampione.getNomeFile(), Types.VARCHAR);
      }*/
      if (esitoCampione.getFileByte() != null && esitoCampione.getFileByte().length > 0) {
        byte[] bytes = esitoCampione.getFileByte();
        mapParameterSource.addValue("referto", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), Types.BLOB);
        mapParameterSource.addValue("nomeFile", esitoCampione.getNomeFile(), Types.VARCHAR);
      } else {
        if (StringUtils.isBlank(esitoCampione.getNomeFile())) {
          mapParameterSource.addValue("referto", null, Types.BLOB);
          mapParameterSource.addValue("nomeFile", null, Types.NULL);
        }
        else
          if (StringUtils.isNotBlank(old.getNomeFile()) && esitoCampione.getNomeFile().equals(old.getNomeFile()) && old.getReferto() != null) {
             byte[] bytes = old.getReferto().getBytes();
             mapParameterSource.addValue("referto", new SqlLobValue(new ByteArrayInputStream(bytes), bytes.length, new DefaultLobHandler()), Types.BLOB);
             mapParameterSource.addValue("nomeFile", esitoCampione.getNomeFile(), Types.VARCHAR);
          }
          else {
            mapParameterSource.addValue("referto", null, Types.BLOB);
            mapParameterSource.addValue("nomeFile", null, Types.NULL);
          }
      }
      logger.debug(UPDATE);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {},
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
  

  
  public Integer insert(CampionamentoDTO campione) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertCampionamento]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
       
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      mapParameterSource.addValue("idRilevazione", campione.getIdRilevazione());
      mapParameterSource.addValue("idSpecieVegetale", campione.getIdSpecieVegetale());
      mapParameterSource.addValue("idTipoCampione", campione.getIdTipoCampione());     
      mapParameterSource.addValue("istatComune", campione.getIstatComune(), Types.VARCHAR);
      mapParameterSource.addValue("latitudine", campione.getLatitudine());
      mapParameterSource.addValue("longitudine", campione.getLongitudine());  
      mapParameterSource.addValue("ext_id_utente_aggiornamento", campione.getExtIdUtenteAggiornamento());
      mapParameterSource.addValue("dataOraInizio", campione.getDataOraInizio()); 
      mapParameterSource.addValue("dataOraFine", campione.getDataOraFine()); 
      mapParameterSource.addValue("idAnagrafica", campione.getIdAnagrafica()); 
      if(campione.getIdIspezioneVisiva() == null || campione.getIdIspezioneVisiva().intValue() == 0) {     
        logger.debug("imposto a null idIspezioneVisiva");
        mapParameterSource.addValue("idIspezioneVisiva", null, Types.NULL); 
      }
      else {
        mapParameterSource.addValue("idIspezioneVisiva", campione.getIdIspezioneVisiva()); 
        logger.debug("imposto il valore di idIspezioneVisiva");
      }
     
      logger.debug(INSERT);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_campionamento"});
      Integer idCampionamento = holder.getKey().intValue();
      campione.setIdCampionamento(idCampionamento);
      return idCampionamento;
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
  
  public void update(CampionamentoDTO campione) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateCampionamento]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {

      mapParameterSource.addValue("idCampionamento", campione.getIdCampionamento());
      mapParameterSource.addValue("idRilevazione", campione.getIdRilevazione());
      mapParameterSource.addValue("idSpecieVegetale", campione.getIdSpecieVegetale());
      mapParameterSource.addValue("idTipoCampione", campione.getIdTipoCampione());
      mapParameterSource.addValue("istatComune", campione.getIstatComune(), Types.VARCHAR);
      mapParameterSource.addValue("latitudine", campione.getLatitudine());
      mapParameterSource.addValue("longitudine", campione.getLongitudine());      
      mapParameterSource.addValue("dataRilevazione", campione.getDataRilevazione()); 
      mapParameterSource.addValue("dataOraInizio", campione.getDataOraInizio());
      mapParameterSource.addValue("dataOraFine", campione.getDataOraFine());
      mapParameterSource.addValue("note", campione.getNote());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", campione.getExtIdUtenteAggiornamento());
      mapParameterSource.addValue("idAnagrafica", campione.getIdAnagrafica());
      
      logger.debug(UPDATE);
      int result = namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
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
  

  /**
   * @param idCampionamento
   * @throws InternalUnexpectedException
   */
  public void remove(Integer idCampionamento) throws InternalUnexpectedException
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
      mapParameterSource.addValue("idCampionamento", idCampionamento);
      //DELETE iuf_r_campionamento_spec_on
      DELETE = "DELETE FROM iuf_r_campionamento_spec_on i \r\n" + 
          "WHERE i.id_campionamento = :idCampionamento";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);       
      // DELETE iuf_r_campione_foto
      DELETE = "DELETE FROM iuf_r_campione_foto WHERE id_campionamento = :idCampionamento";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_esito_campione
      DELETE = "DELETE FROM iuf_t_esito_campione WHERE id_campionamento = :idCampionamento";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_campionamento
      DELETE = "DELETE FROM iuf_t_campionamento WHERE id_campionamento = :idCampionamento";
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
  
  public void insertCampioneSpecieOn(CampionamentoSpecOnDTO campione) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertCampioneSpecieOn]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
       
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      mapParameterSource.addValue("idCampionamento", campione.getIdCampionamento());
      mapParameterSource.addValue("idSpecieOn", campione.getIdSpecieOn());
      mapParameterSource.addValue("presenza", campione.getPresenza(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", campione.getExtIdUtenteAggiornamento());
    
      logger.debug(INSERT_CAMPIONE_SPECIE_ON);
      namedParameterJdbcTemplate.update(INSERT_CAMPIONE_SPECIE_ON, mapParameterSource, holder, new String[] {"id_campionamento"});

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {}, 
          new LogVariable[] 
          {}, INSERT_CAMPIONE_SPECIE_ON, mapParameterSource);
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

  
  public void updateCampioneSpecieOn(CampionamentoDTO campione) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateCampionamento]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {

      mapParameterSource.addValue("idCampionamento", campione.getIdCampionamento());
      mapParameterSource.addValue("idSpecieOn", campione.getIdSpecieOn());
      mapParameterSource.addValue("presenza", campione.getPresenza(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", campione.getExtIdUtenteAggiornamento());

      
      logger.debug(UPDATE_CAMPIONE_SPECIE_ON);
      int result = namedParameterJdbcTemplate.update(UPDATE_CAMPIONE_SPECIE_ON, mapParameterSource);
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
  
  public void removeCampioneSpecieOn(Integer idCampionamento) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "removeCampioneSpecieOn";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String DELETE = null;
    
    try
    {
      mapParameterSource.addValue("idCampionamento", idCampionamento);
      //DELETE iuf_r_campionamento_spec_on
      DELETE = "DELETE FROM iuf_r_campionamento_spec_on i \r\n" + 
          "WHERE i.id_campionamento = :idCampionamento";
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

  public void removeOrganismoNocivo(Integer idCampionamento, Integer idOrganismoNocivo) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "removeOrganismoNocivo";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String DELETE = null;
    
    try
    {
      mapParameterSource.addValue("idCampionamento", idCampionamento);
      mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
      //DELETE iuf_r_campionamento_spec_on
      DELETE = "DELETE FROM iuf_r_campionamento_spec_on i \r\n" + 
          "WHERE i.id_campionamento = :idCampionamento \r\n" +
          " AND i.id_specie_on = :idOrganismoNocivo";
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
  
  public List<CampionamentoDTO> findByIdIspezione(Integer idIspezioneVisiva) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID_ISPEZIONE = "select A.ID_CAMPIONAMENTO," +
        "A.ID_RILEVAZIONE," +
        "A.DATA_RILEVAZIONE," +
        "D.COGNOME || ' ' || D.NOME AS ISPETTORE," + 
        "F.DESC_TIPO_AREA AS AREA," + 
        "E.GENERE_SPECIE AS SPECIE," + 
        "'ORGANISMI' as ORGANISMI," + 
        "G.TIPOLOGIA_CAMPIONE as TIPO_CAMPIONE," + 
        "(CASE WHEN(select count(*) from iuf_r_campione_foto d where d.id_campionamento=a.id_campionamento)>0 THEN 'S' ELSE 'N' END) AS FOTO," + 
        "'ESITO' AS ESITO," +
        "m.id_missione," +
        "A.ID_TIPO_CAMPIONE," +
        "A.ID_SPECIE_VEGETALE," +
        "A.ISTAT_COMUNE," +
        "A.LATITUDINE," +
        "A.LONGITUDINE," +
        "A.ext_id_utente_aggiornamento," +
        "A.data_ultimo_aggiornamento," +
        "a.id_ispezione_visiva " +
        "FROM IUF_T_CAMPIONAMENTO a,IUF_T_MISSIONE m," +
            "IUF_T_RILEVAZIONE C,IUF_T_ANAGRAFICA D,IUF_D_SPECIE_VEGETALE E,IUF_D_TIPO_AREA F," + 
            "IUF_D_TIPO_CAMPIONE G " + 
        "WHERE A.ID_RILEVAZIONE = C.ID_RILEVAZIONE " + 
        "AND C.ID_MISSIONE = m.ID_MISSIONE " + 
        "AND m.ID_ISPETTORE_ASSEGNATO = D.ID_ANAGRAFICA " + 
        "AND A.ID_SPECIE_VEGETALE = E.ID_SPECIE_VEGETALE " + 
        "AND C.ID_TIPO_AREA = F.ID_TIPO_AREA " + 
        "AND A.ID_TIPO_CAMPIONE = G.ID_TIPO_CAMPIONE " + 
        "AND A.ID_ISPEZIONE_VISIVA = :idIspezioneVisiva \r\n";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idIspezioneVisiva", idIspezioneVisiva);
    
    try
    {
      logger.debug(SELECT_BY_ID_ISPEZIONE);
      List<CampionamentoDTO> campioni = namedParameterJdbcTemplate.query(SELECT_BY_ID_ISPEZIONE, mapParameterSource, new RowMapper<CampionamentoDTO>() {
                @Override  
                public CampionamentoDTO mapRow(ResultSet rs, int rownumber) throws SQLException {
                  CampionamentoDTO camp = new CampionamentoDTO();
                  camp.setIdCampionamento(rs.getInt("id_campionamento"));
                  camp.setIdRilevazione(rs.getInt("id_rilevazione"));
                  camp.setDataRilevazione(rs.getTimestamp("data_rilevazione"));
                  camp.setIspettore(rs.getString("ispettore"));
                  camp.setArea(rs.getString("area"));
                  camp.setSpecie(rs.getString("specie"));
                  camp.setTipoCampione(rs.getString("tipo_campione"));
                  camp.setFoto(rs.getString("foto").equals("S"));
                  camp.setIdMissione(rs.getInt("id_missione"));
                  camp.setIdTipoCampione(rs.getInt("id_tipo_campione"));
                  camp.setIdSpecieVegetale(rs.getInt("id_specie_vegetale"));
                  camp.setIstatComune(rs.getString("istat_comune"));
                  camp.setLatitudine(rs.getDouble("latitudine"));
                  camp.setLongitudine(rs.getDouble("longitudine"));
                  camp.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
                  camp.setDataUltimoAggiornamento(rs.getTimestamp("data_ultimo_aggiornamento"));
                  camp.setIdIspezioneVisiva(rs.getInt("id_ispezione_visiva"));
              return camp;
          }
        });
      return campioni;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_BY_ID_ISPEZIONE, mapParameterSource);
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

  
  
  public List<CodiceEsitoDTO> findValidiCodiciEsito() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_CODICI_ESITO_VALIDI);
      return queryForList(SELECT_CODICI_ESITO_VALIDI, mapParameterSource, CodiceEsitoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_CODICI_ESITO_VALIDI, mapParameterSource);
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

  
  public CodiceEsitoDTO findCodiceEsitoById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findCodiceEsitoById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    try
    {
      logger.debug(SELECT_CODICI_ESITO_BY_ID);
      return queryForObject(SELECT_CODICI_ESITO_BY_ID, mapParameterSource, CodiceEsitoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_CODICI_ESITO_BY_ID, mapParameterSource);
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
  
  public List<CodiceEsitoDTO> findAllCodiciEsito() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_CODICI_ESITO_ALL);
      return queryForList(SELECT_CODICI_ESITO_ALL, mapParameterSource, CodiceEsitoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_CODICI_ESITO_ALL, mapParameterSource);
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

  
  public List<CodiceEsitoDTO> findByIdMultipliCodiciEsito(String idMultipli) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
  //  mapParameterSource.addValue("idMultipli", idMultipli, Types.INTEGER);
    String select = String.format(SELECT_CODICI_ESITO_ALL_ID_MULTIPLI,idMultipli);
    try
    {
      List<CodiceEsitoDTO> list = queryForList(select, mapParameterSource, CodiceEsitoDTO.class);
      return list;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_CODICI_ESITO_ALL_ID_MULTIPLI, mapParameterSource);
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

  
  public byte[] getPdf(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getPdf";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    String SELECT_PDF = "select t.referto from IUF_T_ESITO_CAMPIONE t where t.id_esito_campione= :id";
    try
    {
      logger.debug(SELECT_PDF);
      byte[] pdf = namedParameterJdbcTemplate.queryForObject(SELECT_PDF, mapParameterSource, new BlobRowMapper());
      
      return pdf;
      
    } catch (EmptyResultDataAccessException e) {
        logger.debug("ID Verbale non trovato");
        return null;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
              {}, null, SELECT_PDF, mapParameterSource);
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
         Blob column = rs.getBlob("REFERTO");
         return column.getBytes(1L, (int) column.length());
    }
  }  
}
