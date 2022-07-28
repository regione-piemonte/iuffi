package it.csi.iuffi.iuffiweb.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.FotoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaSpecieOnDTO;
import it.csi.iuffi.iuffiweb.model.api.OrganismiNocivi;
import it.csi.iuffi.iuffiweb.model.request.IspezioneVisivaRequest;


public class GestioneVisualDAO extends BaseDAO
{

  
  private static final String THIS_CLASS = GestioneVisualDAO.class.getSimpleName();
  
  private LobHandler lobHandler;

  public GestioneVisualDAO(){  }
  
  private static final String INSERT = "INSERT INTO iuf_t_ispezione_visiva(" +
      "id_ispezione," + 
     "id_rilevazione," + 
     "numero_aviv," +
     "id_specie_vegetale," +
     "superfice," +
     "numero_piante," +
     "data_ora_inizio," +
     "data_ora_fine," +
     "flag_presenza_on," +
     "istat_comune," +
     "latitudine," +
     "longitudine," +
     "note," +
     "flag_indicatore_intervento," +
     "riferimento_ubicazione," +
     "ext_id_utente_aggiornamento," +
     "data_ultimo_aggiornamento," +
     "id_anagrafica," +
     "cuaa"+

 ") " + 
 "VALUES(seq_iuf_t_ispezione_visiva.nextval, :idRilevazione, :numeroAviv, :idSpecieVegetale, :superfice, :numeroPiante, :dataOraInizio, :dataOraFine, :flagPresenzaOn, :istatComune, :latitudine, :longitudine, :note, :flagIndicatoreIntervento, :riferimentoUbicazione," + 
 ":ext_id_utente_aggiornamento, SYSDATE, :idAnagrafica, :cuaa)";
  
  private static final String UPDATE = "UPDATE iuf_t_ispezione_visiva " +
  "set id_rilevazione = :idRilevazione, numero_aviv = :numeroAviv, id_specie_vegetale = :idSpecieVegetale, superfice = :superfice, numero_piante = :numeroPiante, " + 
  "data_ora_inizio = :dataOraInizio, data_ora_fine = :dataOraFine, flag_presenza_on = :flagPresenzaOn, istat_comune = :istatComune, latitudine = :latitudine, longitudine = :longitudine, note = :note, flag_indicatore_intervento = :flagIndicatoreIntervento, riferimento_ubicazione = :riferimentoUbicazione, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE, " +
  "id_anagrafica = :idAnagrafica, cuaa = :cuaa WHERE id_ispezione = :idIspezione";
  
  
  private static final String INSERT_SPECIE_ON = "INSERT INTO iuf_r_isp_visiva_spec_on(id_ispezione, id_specie_on, flag_trovato, ext_id_utente_aggiornamento, data_ultimo_aggiornamento)"+
  "VALUES(:idIspezione, :idSpecieOn, :flagTrovato, :ext_id_utente_aggiornamento, SYSDATE)";
  
  private static final String UPDATE_SPECIE_ON = "UPDATE iuf_r_isp_visiva_spec_on set id_specie_on = :idSpecieOn, flag_trovato = :flagTrovato, note = :note, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
  "where id_ispezione = :idIspezione and id_specie_on = :idSpecieOn";
  
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
      "              ) WHERE id_missione = B.id_missione AND id_specie_vegetale = NVL(:idSpecieVegetale,id_specie_vegetale))";

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
      "                     ) WHERE id_missione = B.id_missione AND id_specie_on = NVL(:idOrganismoNocivo,id_specie_on))";
  
  private static final String FILTER_ORGANISMO_NOCIVO_MULTIPLE = 
      "  AND EXISTS (SELECT 1\r\n" + 
      "                 FROM iuf_r_isp_visiva_spec_on vso\r\n" + 
      "                WHERE a.id_ispezione = vso.id_ispezione\r\n" + 
      "                  AND vso.id_specie_on IN (%s)) ";

  private static final String SELECT_BY_FILTER = "SELECT A.ID_ISPEZIONE," + 
      "A.ID_RILEVAZIONE," + 
      "D.COGNOME || ' ' || D.NOME AS ISPETTORE," + 
      "F.DESC_TIPO_AREA AS AREA," + 
      "F.DETTAGLIO_TIPO_AREA AS DETTAGLIO_AREA," + 
      "E.GENERE_SPECIE AS SPECIE," +
      "E.NOME_VOLGARE AS NOME_VOLGARE_SPECIE," +
      "A.NUMERO_AVIV," +
      "A.ID_SPECIE_VEGETALE," +
      "A.SUPERFICE as SUPERFICIE," +
      "A.NUMERO_PIANTE," +
      "A.FLAG_PRESENZA_ON," +
      "A.ISTAT_COMUNE," +
      "A.LATITUDINE," +
      "A.LONGITUDINE," +
      "A.FLAG_INDICATORE_INTERVENTO," +
      "A.RIFERIMENTO_UBICAZIONE," +
      "(SELECT LISTAGG(aa.nome_latino, '; ') \r\n" + 
      "                     WITHIN GROUP (ORDER BY aa.nome_latino)\r\n" + 
      "                      from IUF_R_ISP_VISIVA_SPEC_ON ggg, \r\n" + 
      "                     iuf_d_organismo_nocivo aa where ggg.id_specie_on=aa.id_organismo_nocivo\r\n" + 
      "                     and ggg.id_ispezione=a.id_ispezione) AS ON_ISPEZIONATI," +
      //"'FOTO' AS FOTO," + 
      "A.NOTE," +
      "A.ext_id_utente_aggiornamento," +
      "A.data_ultimo_aggiornamento," +
      "A.data_ora_inizio," +
      "D.cognome as cognome_ispettore, " +
      "D.nome as nome_ispettore, " +
      "C.cuaa," +
      "A.id_anagrafica," +
      "'ESITO' AS ESITO,B.ID_MISSIONE,\r\n" +
      "i.descrizione_comune comune, \r\n" +
      "DECODE((SELECT COUNT(*) FROM iuf_t_campionamento ca WHERE ca.id_ispezione_visiva = a.id_ispezione),0,'N','S') campionamento,\r\n" +
      "DECODE((SELECT COUNT(*) FROM iuf_t_trappolaggio tr WHERE tr.id_ispezione_visiva = a.id_ispezione),0,'N','S') trappolaggio,\r\n" +
      "(SELECT NVL(MAX(v.id_stato_verbale),0) FROM iuf_t_verbale v WHERE v.id_missione = b.id_missione) id_stato_verbale \r\n" +
      "FROM iuf_t_ispezione_visiva a, iuf_t_missione b, iuf_t_rilevazione c, iuf_t_anagrafica d," +
      "     iuf_d_specie_vegetale e, iuf_d_tipo_area f, iuf_r_isp_visiva_spec_on g, iuf_d_organismo_nocivo h," +
      "     smrgaa_v_dati_amministrativi i \r\n" +
      "WHERE A.ID_RILEVAZIONE=C.ID_RILEVAZIONE " + 
      "AND TRUNC(B.data_ora_inizio_missione) BETWEEN TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2100'),'DD/MM/YYYY') \r\n" +
      "AND C.ID_MISSIONE=B.ID_MISSIONE " + 
      "AND A.ID_ANAGRAFICA=D.ID_ANAGRAFICA " +
      "AND A.ID_SPECIE_VEGETALE=E.ID_SPECIE_VEGETALE " +
      "AND C.ID_TIPO_AREA=F.ID_TIPO_AREA (+) " +
      "AND a.id_ispezione=g.id_ispezione (+) " +
      "AND g.id_specie_on=h.id_organismo_nocivo (+) " +
      "AND C.ID_RILEVAZIONE = NVL(:idRilevazione,C.ID_RILEVAZIONE) " +
      //"AND B.id_ispettore_assegnato = NVL(:idIspettore,B.id_ispettore_assegnato) " +
      //"AND UPPER(D.nome) = UPPER(NVL(:nomeIspettore,D.nome))" +
      //"AND UPPER(D.cognome) = UPPER(NVL(:cognomeIspettore,D.cognome)) " +
      "AND a.istat_comune = i.ISTAT_COMUNE (+) \r\n";
      //"ORDER BY ID_ISPEZIONE";
  
  private static final String GROUP_BY = " GROUP BY A.ID_ISPEZIONE,\r\n" + 
      "       A.ID_RILEVAZIONE,\r\n" + 
      "       D.COGNOME || ' ' || D.NOME,\r\n" + 
      "       F.DESC_TIPO_AREA,\r\n" + 
      "       F.DETTAGLIO_TIPO_AREA, "+
      "       E.GENERE_SPECIE,\r\n" + 
      "       E.NOME_VOLGARE,\r\n" + 
      "       A.NUMERO_AVIV,\r\n" + 
      "       A.ID_SPECIE_VEGETALE,\r\n" + 
      "       A.SUPERFICE,\r\n" + 
      "       A.NUMERO_PIANTE,\r\n" + 
      "       A.FLAG_PRESENZA_ON,\r\n" + 
      "       A.ISTAT_COMUNE,\r\n" + 
      "       A.LATITUDINE,\r\n" + 
      "       A.LONGITUDINE,\r\n" + 
      "       A.FLAG_INDICATORE_INTERVENTO,\r\n" + 
      "       A.RIFERIMENTO_UBICAZIONE,\r\n" + 
      "       A.NOTE,\r\n" + 
      "       A.ext_id_utente_aggiornamento,\r\n" + 
      "       A.data_ultimo_aggiornamento,\r\n" + 
      "       A.data_ora_inizio,\r\n" + 
      "       D.cognome,\r\n" + 
      "       D.nome,\r\n" + 
      "       C.cuaa,\r\n" + 
      "       B.ID_MISSIONE,\r\n" +
      "		  A.id_anagrafica, \r\n" +
      "       i.descrizione_comune";

  private static final String SELECT_BY_ID = "select A.data_ora_inizio, A.data_ora_fine, A.ID_ISPEZIONE," + 
      "A.ID_RILEVAZIONE," + 
      "D.COGNOME || ' ' || D.NOME AS ISPETTORE,A.ID_ANAGRAFICA," + 
      "F.DESC_TIPO_AREA AS AREA,F.ID_TIPO_AREA," +
      "F.DETTAGLIO_TIPO_AREA AS DETTAGLIO_AREA," +
      "E.GENERE_SPECIE AS SPECIE," +
      "E.NOME_VOLGARE as PIANTA," +
      "A.NUMERO_AVIV," +
      "A.ID_SPECIE_VEGETALE," +
      "A.SUPERFICE as SUPERFICIE," +
      "A.NUMERO_PIANTE," +
      "A.FLAG_PRESENZA_ON," +
      "A.ISTAT_COMUNE," +
      "V.DESCRIZIONE_COMUNE COMUNE," +
      "A.LATITUDINE," +
      "A.LONGITUDINE," +
      "A.FLAG_INDICATORE_INTERVENTO," +
      "A.RIFERIMENTO_UBICAZIONE," +
      "(SELECT LISTAGG(aa.nome_latino, '; ') \r\n" + 
      "                     WITHIN GROUP (ORDER BY aa.nome_latino)\r\n" + 
      "                      from IUF_R_ISP_VISIVA_SPEC_ON ggg, \r\n" + 
      "                     iuf_d_organismo_nocivo aa where ggg.id_specie_on=aa.id_organismo_nocivo\r\n" + 
      "                     and ggg.id_ispezione=a.id_ispezione) as ON_ISPEZIONATI," + 
      "'' as ORGANISMI," + 
      //"'FOTO' AS FOTO," + 
      "A.NOTE," +
      "A.ext_id_utente_aggiornamento," +
      "A.data_ultimo_aggiornamento," +
      "'ESITO' AS ESITO,B.ID_MISSIONE, " +
      "C.CUAA "+
      "FROM IUF_T_ISPEZIONE_VISIVA a,IUF_T_MISSIONE B," + 
      "IUF_T_RILEVAZIONE C,IUF_T_ANAGRAFICA D,IUF_D_SPECIE_VEGETALE E,IUF_D_TIPO_AREA F,SMRGAA_V_DATI_AMMINISTRATIVI V " +
      
      "WHERE A.ID_RILEVAZIONE=C.ID_RILEVAZIONE " + 
      "AND C.ID_MISSIONE=B.ID_MISSIONE " + 
      "AND B.ID_ISPETTORE_ASSEGNATO=D.ID_ANAGRAFICA " +
      "AND A.ID_SPECIE_VEGETALE=E.ID_SPECIE_VEGETALE " +
      "AND C.ID_TIPO_AREA=F.ID_TIPO_AREA " +
     // "AND C.ID_RILEVAZIONE = NVL(:idRilevazione,C.ID_RILEVAZIONE) " +
      "AND a.istat_comune = v.ISTAT_COMUNE (+) "+
      " AND A.ID_ISPEZIONE = :id";
  
  private static final String FILTER_COMUNE = 
	      "                  AND a.istat_comune = NVL(:istatComune, a.istat_comune) \r\n" +
	      "                  AND UPPER(i.descrizione_comune) LIKE NVL(UPPER(:descComune), '%') \r\n";

  
  private static final String SELECT_CAMPIONAMENTI_COUNT = "SELECT COUNT(*) FROM iuf_t_campionamento WHERE id_ispezione_visiva = :idIspezioneVisiva";
  private static final String SELECT_TRAPPOLAGGI_COUNT = "SELECT COUNT(*) FROM iuf_t_trappolaggio WHERE id_ispezione_visiva = :idIspezioneVisiva";
		   
  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public Integer insert(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertIspezione]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
       
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      mapParameterSource.addValue("idRilevazione", ispezioneVisiva.getIdRilevazione());
      mapParameterSource.addValue("numeroAviv", ispezioneVisiva.getNumeroAviv(), Types.VARCHAR);
      mapParameterSource.addValue("idSpecieVegetale", ispezioneVisiva.getIdSpecieVegetale());
      mapParameterSource.addValue("superfice", ispezioneVisiva.getSuperficie());      
      mapParameterSource.addValue("numeroPiante", ispezioneVisiva.getNumeroPiante());
      mapParameterSource.addValue("dataOraInizio", ispezioneVisiva.getDataOraInizio());
      mapParameterSource.addValue("dataOraFine", ispezioneVisiva.getDataOraFine());
      mapParameterSource.addValue("flagPresenzaOn", ispezioneVisiva.getFlagPresenzaOn(), Types.VARCHAR);
      mapParameterSource.addValue("istatComune", ispezioneVisiva.getIstatComune(), Types.VARCHAR);
      mapParameterSource.addValue("latitudine", ispezioneVisiva.getLatitudine());
      mapParameterSource.addValue("longitudine", ispezioneVisiva.getLongitudine());
      mapParameterSource.addValue("note", ispezioneVisiva.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("flagIndicatoreIntervento", ispezioneVisiva.getFlagIndicatoreIntervento(), Types.VARCHAR);
      mapParameterSource.addValue("riferimentoUbicazione", ispezioneVisiva.getRiferimentoUbicazione(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", ispezioneVisiva.getExtIdUtenteAggiornamento());     
      mapParameterSource.addValue("idAnagrafica", ispezioneVisiva.getIdAnagrafica());   
      mapParameterSource.addValue("cuaa", ispezioneVisiva.getCuaa());
      

      
      logger.debug(INSERT);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_ispezione"});
      Integer idIspezione = holder.getKey().intValue();
      ispezioneVisiva.setIdIspezione(idIspezione);
      return idIspezione;
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

  
  public void insertSpecieOn(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertIspezioneSpecieOn]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
       
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      mapParameterSource.addValue("idIspezione", ispezioneVisiva.getIdIspezione());
      mapParameterSource.addValue("idSpecieOn", ispezioneVisiva.getIdSpecieOn());
      mapParameterSource.addValue("flagTrovato", ispezioneVisiva.getFlagTrovato());
      mapParameterSource.addValue("note", ispezioneVisiva.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", ispezioneVisiva.getExtIdUtenteAggiornamento());     
    
      logger.debug(INSERT_SPECIE_ON);
      namedParameterJdbcTemplate.update(INSERT_SPECIE_ON, mapParameterSource, holder, new String[] {"id_ispezione"});

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {}, 
          new LogVariable[] 
          {}, INSERT_SPECIE_ON, mapParameterSource);
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
  
   public List<IspezioneVisivaDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT_ALL = " SELECT * FROM IUF_T_ISPEZIONE_VISIVA t  ORDER BY t.ID_ISPEZIONE ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL);
      return queryForList(SELECT_ALL, mapParameterSource, IspezioneVisivaDTO.class);
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

   
   
  public List<IspezioneVisivaDTO> findByFilter(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilter";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_FILTER = "select A.ID_ISPEZIONE," + 
        "A.ID_RILEVAZIONE," + 
        "D.COGNOME || ' ' || D.NOME AS ISPETTORE," + 
        "F.DESC_TIPO_AREA AS AREA," + 
        "E.GENERE_SPECIE AS SPECIE," +
        "A.NUMERO_AVIV," +
        "A.ID_SPECIE_VEGETALE," +
        "A.SUPERFICE as SUPERFICIE," +
        "A.NUMERO_PIANTE," +
        "A.FLAG_PRESENZA_ON," +
        "A.ISTAT_COMUNE," +
        "V.DESCRIZIONE_COMUNE COMUNE," +
        "A.LATITUDINE," +
        "A.LONGITUDINE," +
        "A.FLAG_INDICATORE_INTERVENTO," +
        "A.RIFERIMENTO_UBICAZIONE," +
        "NULL as ORGANISMI," + 
        //"'FOTO' AS FOTO," + 
        "A.NOTE," +
        "A.DATA_ORA_INIZIO, " +
        "A.DATA_ORA_FINE, " +
        "A.ext_id_utente_aggiornamento," +
        "A.data_ultimo_aggiornamento," +
        "'ESITO' AS ESITO,B.ID_MISSIONE " +
        
        "FROM IUF_T_ISPEZIONE_VISIVA a,IUF_T_MISSIONE B," + 
        "IUF_T_RILEVAZIONE C,IUF_T_ANAGRAFICA D,IUF_D_SPECIE_VEGETALE E,IUF_D_TIPO_AREA F,SMRGAA_V_DATI_AMMINISTRATIVI V " +
        
        "WHERE A.ID_RILEVAZIONE=C.ID_RILEVAZIONE " + 
        "AND C.ID_MISSIONE=B.ID_MISSIONE " + 
        "AND B.ID_ISPETTORE_ASSEGNATO=D.ID_ANAGRAFICA " +
        "AND A.ID_SPECIE_VEGETALE=E.ID_SPECIE_VEGETALE " +
        "AND C.ID_TIPO_AREA=F.ID_TIPO_AREA " +
        "AND C.ID_RILEVAZIONE = NVL(:idRilevazione,C.ID_RILEVAZIONE) " +
        "AND a.istat_comune = v.ISTAT_COMUNE (+) " +
        "ORDER BY ID_ISPEZIONE";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    if (ispezioneVisiva.getIdRilevazione() != null && ispezioneVisiva.getIdRilevazione() > 0)
      mapParameterSource.addValue("idRilevazione", ispezioneVisiva.getIdRilevazione());
    else
      mapParameterSource.addValue("idRilevazione", null, Types.NULL);
    
    try
    {
      logger.debug(SELECT_BY_FILTER);
      List<IspezioneVisivaDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, IspezioneVisivaDTO.class);
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

  
  public IspezioneVisivaDTO findById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    //String SELECT_BY_ID = "SELECT * FROM IUF_T_ISPEZIONE_VISIVA WHERE ID_ISPEZIONE = :id";

 
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    //mapParameterSource.addValue("idRilevazione", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      IspezioneVisivaDTO ispezioneVisiva = queryForObject(SELECT_BY_ID, mapParameterSource, IspezioneVisivaDTO.class);
      return ispezioneVisiva;
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
  
  public List<FotoDTO> findFotoByIdIspezioneVisiva(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    /*String SELECT_BY_ID = "select c.id_foto,c.foto,c.nome_file,c.data_foto,c.tag " + 
        "from iuf_r_isp_visiva_foto a, iuf_t_ispezione_visiva b," + 
        "iuf_t_foto c " + 
        "where b.id_ispezione=a.id_ispezione_visiva " + 
        "and a.id_foto=c.id_foto " + 
        "and b.id_ispezione=:id";*/

    String SELECT_BY_ID = "SELECT t.id_foto as id_foto,\r\n" + 
        "                    t.data_foto,\r\n" + 
        "                    t.nome_file,\r\n" + 
        "                    t.tag,\r\n" + 
        "                     t.note,              \r\n" + 
        "                    t.ext_id_utente_aggiornamento,            \r\n" + 
        "                    aa.genere_specie ||'-'|| aa.nome_volgare as specie, \r\n" + 
        "                    bb.desc_tipo_area ||'-'|| bb.dettaglio_tipo_area as area, \r\n" + 
        "                     '' as trappola,\r\n" + 
        "                     '' as campione, \r\n" + 
        "                    a.id_ispezione_visiva,\r\n" + 
        "                     0 AS id_campionamento,\r\n" + 
        "                    0 AS id_trappolaggio,\r\n" + 
        "                    g.id_rilevazione, \r\n" + 
        "                    g.id_missione, \r\n" + 
        "                    'I' AS tipologia, \r\n" + 
        "                    g.id_tipo_area, \r\n" + 
        "                    d.id_anagrafica as id_ispettore_evento, \r\n" + 
        "                    rr.cognome || ' ' ||rr.nome as ispettore_evento, \r\n" + 
        "                    d.id_specie_vegetale AS id_specie,\r\n" + 
        "                    0 AS id_tipo_campione,\r\n" + 
        "                    0 AS id_trappola,\r\n" + 
        "                    d.istat_comune, \r\n" + 
        "                    d.latitudine, \r\n" + 
        "                    d.longitudine,\r\n" + 
        "                    h.id_ispettore_assegnato as id_ispettore_assegnato,\r\n" + 
        "                    UPPER(jj.cognome || ' ' ||jj.nome) as ispettore_assegnato_m,\r\n" + 
        "                    (SELECT LISTAGG(a.sigla, '; ') \r\n" + 
        "                        WITHIN GROUP (ORDER BY a.sigla)  \r\n" + 
        "                          from IUF_R_ISP_VISIVA_SPEC_ON t,  \r\n" + 
        "                         iuf_d_organismo_nocivo a where t.id_specie_on=a.id_organismo_nocivo \r\n" + 
        "                        and t.id_ispezione=d.id_ispezione) as organismi  \r\n" + 
        "               FROM IUF_T_FOTO             t, \r\n" + 
        "                     iuf_r_isp_visiva_foto  a, \r\n" + 
        "                    iuf_t_ispezione_visiva d, \r\n" + 
        "                     iuf_t_rilevazione      g, \r\n" + 
        "                     iuf_t_missione         h,\r\n" + 
        "                     iuf_d_specie_vegetale aa, \r\n" + 
        "                     iuf_d_tipo_area bb,\r\n" + 
        "                     iuf_t_anagrafica jj, \r\n" + 
        "                     iuf_t_anagrafica rr\r\n" + 
        "              WHERE t.id_foto = a.id_foto \r\n" + 
        "                 AND d.id_ispezione = a.id_ispezione_visiva \r\n" + 
        "                 AND d.id_rilevazione = g.id_rilevazione \r\n" + 
        "                 AND g.id_missione = h.id_missione\r\n" + 
        "                AND aa.id_specie_vegetale=d.id_specie_vegetale\r\n" + 
        "                AND bb.id_tipo_area=g.id_tipo_area \r\n" + 
        "                 AND jj.id_anagrafica=h.id_ispettore_assegnato\r\n" + 
        "                 AND rr.id_anagrafica=d.id_anagrafica     \r\n" + 
        "                and d.id_ispezione=:id ";
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

  
  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "select c.id_foto,c.foto,c.nome_file,c.data_foto,c.tag,c.note " + 
        "from iuf_t_foto c " + 
        "where c.id_foto =:id";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
      FotoDTO foto = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, mapParameterSource, getRowMapper());
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
  
  public List<IspezioneVisivaDTO> findByFilterFromRequest(IspezioneVisivaRequest ivr,Integer idAnagrafica, Integer idEnte) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findByFilterFromRequest";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    if (ivr.getIdIspezione() != null) {
      IspezioneVisivaDTO m = this.findById(ivr.getIdIspezione());
      List<IspezioneVisivaDTO> list = new ArrayList<IspezioneVisivaDTO>();
      list.add(m);
      return list;
    }
    
    String select = SELECT_BY_FILTER;

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    
    if (ivr.getAnno() != null) {
      select += " AND TO_CHAR(B.data_ora_inizio_missione,'YYYY') = :anno \r\n";
      mapParameterSource.addValue("anno", ivr.getAnno().toString());
    }
    mapParameterSource.addValue("dallaData", (ivr.getDallaData()!=null)?ivr.getDallaData().replaceAll("-", "/"):"");
    mapParameterSource.addValue("allaData", (ivr.getAllaData()!=null)?ivr.getAllaData().replaceAll("-", "/"):"");
    
    if (ivr.getIdIspettore()!=null && ivr.getIdIspettore()>0)
      mapParameterSource.addValue("idIspettore", ivr.getIdIspettore());
    else
      mapParameterSource.addValue("idIspettore", null, Types.NULL);

    if (ivr.getIdSpecieVegetale()!=null && ivr.getIdSpecieVegetale()>0) {
      select += FILTER_SPECIE_VEGETALE;
      mapParameterSource.addValue("idSpecieVegetale", ivr.getIdSpecieVegetale());
    }

    if (ivr.getIdOrganismoNocivo()!=null && ivr.getIdOrganismoNocivo()>0) {
      select += FILTER_ORGANISMO_NOCIVO;
      mapParameterSource.addValue("idOrganismoNocivo", ivr.getIdOrganismoNocivo());
    }
    
    if (ivr.getIstatComune() != null && ivr.getIstatComune().trim().length() > 0 || StringUtils.isNotBlank(ivr.getDescComune())) {
        select += FILTER_COMUNE;
        if (ivr.getIstatComune() != null && ivr.getIstatComune().trim().length() > 0) {
          mapParameterSource.addValue("istatComune", ivr.getIstatComune());
        }
        else
          mapParameterSource.addValue("istatComune", null, Types.NULL);
        
        if (StringUtils.isNotBlank(ivr.getDescComune())) {
          mapParameterSource.addValue("descComune", ivr.getDescComune());
        }
        else
          mapParameterSource.addValue("descComune", null, Types.NULL);
     } 

    // filtro multiplo per tipo area
    if (ivr.getTipoArea() != null && ivr.getTipoArea().size() > 0) {
      String tipoAree = "";
      for (int i=0; i<ivr.getTipoArea().size(); i++) {
        tipoAree += ivr.getTipoArea().get(i);
        if (i < ivr.getTipoArea().size()-1)
          tipoAree += ",";
      }
      select += " AND c.id_tipo_area IN (" + tipoAree + ") ";
    }
    // filtro multiplo per ispettore assegnato
    if (ivr.getIspettoreAssegnato() != null && ivr.getIspettoreAssegnato().size() > 0) {
      select += " AND a.id_anagrafica IN (";
      for (int i=0; i<ivr.getIspettoreAssegnato().size(); i++) {
        if (i > 0)
          select += ",";
        select += ivr.getIspettoreAssegnato().get(i);
      }
      select += ")\r\n";
    }
    // filtro multiplo per ispettori secondari
    if (ivr.getIspettoriSecondari() != null && ivr.getIspettoriSecondari().size() > 0) {
      select += " AND EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia \r\n" + 
          "                     WHERE ia.id_missione = b.id_missione\r\n" + 
          "                       AND ia.id_anagrafica IN (";
      for (int i=0; i<ivr.getIspettoriSecondari().size(); i++) {
        if (i > 0)
          select += ",";
        select += ivr.getIspettoriSecondari().get(i);
      }
      select += "))\r\n";
    }
    // filtro multiplo per specie vegetale
    if (ivr.getSpecieVegetale() != null && ivr.getSpecieVegetale().size() > 0) {
      String elements = "";
      for (int i=0; i<ivr.getSpecieVegetale().size(); i++) {
        if (i > 0)
          elements += ",";
        elements += ivr.getSpecieVegetale().get(i);
      }
      String condition = String.format(" AND a.id_specie_vegetale in (%s) ", elements);
      select += condition;
    }
    // filtro multiplo per organismo nocivo
    if (ivr.getOrganismoNocivo() != null && ivr.getOrganismoNocivo().size() > 0) {
      String elements = "";
      for (int i=0; i<ivr.getOrganismoNocivo().size(); i++) {
        if (i > 0)
          elements += ",";
        elements += ivr.getOrganismoNocivo().get(i);
      }
      String condition = String.format(FILTER_ORGANISMO_NOCIVO_MULTIPLE, elements);
      select += condition;
    }
    // filtro per obiettivo missione Nessuno
//    if (ivr.getObiettivoNessuno() != null && ivr.getObiettivoNessuno().equalsIgnoreCase("S")) {
//      select += FILTER_OBIETTIVO_NESSUNO;
//    }
    
    // filtro per obiettivo missione Indagine ufficiale
    if (ivr.getObiettivoIndagineUfficiale() != null && ivr.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
      //select += FILTER_OBIETTIVO_INDAGINE_UFFICIALE;
      if (ivr.getObiettivoEmergenza() == null || !ivr.getObiettivoEmergenza().equalsIgnoreCase("S")) {
        select += " AND NVL(c.flag_emergenza,'N') = 'N' ";
      }
    }
    // filtro per obiettivo missione Emergenza
    if (ivr.getObiettivoEmergenza() != null && ivr.getObiettivoEmergenza().equalsIgnoreCase("S")) {
      if (ivr.getObiettivoIndagineUfficiale() == null || !ivr.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
        select += " AND c.flag_emergenza = 'S' ";
      }
    }
    //filtro per cuaa o n. autorizzazione
    if (ivr.getCuaa()!=null && !ivr.getCuaa().equals("")) {
      select += " AND (c.ext_numero_aviv = " + "'" + ivr.getCuaa() + "'"  + " OR C.cuaa = " + "'" + ivr.getCuaa() + "')"; //" AND C.cuaa = " + "'" + ivr.getCuaa() + "'";
    }
    if (ivr.getIdMissione() != null && ivr.getIdMissione() > 0) {
      select += " AND b.id_missione = :idMissione \r\n";
      mapParameterSource.addValue("idMissione", ivr.getIdMissione());
    }
    
    if (ivr.getIdRilevazione() != null && ivr.getIdRilevazione() > 0)
      mapParameterSource.addValue("idRilevazione", ivr.getIdRilevazione());
    else
      mapParameterSource.addValue("idRilevazione", null, Types.NULL);

    // Filtro per Anagrafica (ispettore loggato)
    if (idAnagrafica != null && idAnagrafica > 0) {
      select += " AND (b.id_ispettore_assegnato = :idAnagrafica " +
                     "OR EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia " +
                                 "WHERE ia.id_missione = b.id_missione AND ia.id_anagrafica = :idAnagrafica))";
      mapParameterSource.addValue("idAnagrafica", idAnagrafica);
    }
    // Filtro per Ente (ente ispettore loggato)
    if (idEnte != null && idEnte > 0) {
      select += " AND d.id_ente = :idEnte";
      mapParameterSource.addValue("idEnte", idEnte);
    }

    // ********************* aggiungere emergenza quando ci sarà il campo in tabella
    select += "\r\n" + GROUP_BY + "\r\n ORDER BY a.data_ora_inizio DESC"; //ID_ISPEZIONE\";

    try
    {

      logger.debug(select);
      List<IspezioneVisivaDTO> ispezioniVisive = queryForList(select, mapParameterSource, IspezioneVisivaDTO.class);
      return ispezioniVisive;
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
  
  
  private RowMapper<FotoDTO> getRowMapper() {
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
      //  foto.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
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

  
  public void remove(Integer idIspezione) throws InternalUnexpectedException
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
      mapParameterSource.addValue("idIspezione", idIspezione);
      //DELETE iuf_r_extra_field_visual
      DELETE = "DELETE FROM iuf_r_extra_field_visual i \r\n" + 
          "WHERE i.id_ispezione =:idIspezione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_isp_visiva_spec_on
      DELETE = "DELETE FROM iuf_r_isp_visiva_spec_on i \r\n" + 
          "WHERE i.id_ispezione = :idIspezione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_r_isp_visiva_foto
      DELETE = "DELETE FROM iuf_r_isp_visiva_foto i \r\n" + 
          "WHERE i.id_ispezione_visiva = :idIspezione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_isp_visiva_pianta
      DELETE = "DELETE FROM iuf_t_isp_visiva_pianta i \r\n" + 
          "WHERE i.id_ispezione_visiva = :idIspezione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      /*
       * DA VALUTARE SE CANCELLANDO UNA VISUAL SI DEVE CANCELLARE ANCHE IL CAMPIONAMENTO E TRAPPOLAGGIO DERIVATI
       */
      // DELETE iuf_t_campionamento
      DELETE = "DELETE FROM iuf_t_campionamento c \r\n" + 
               "WHERE c.id_ispezione_visiva = :idIspezione";
      logger.debug(DELETE);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      // DELETE iuf_t_trappolaggio
      DELETE = "DELETE FROM iuf_t_trappolaggio t \r\n" + 
               "WHERE t.id_ispezione_visiva = :idIspezione";
      logger.debug(DELETE);
      int recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug("visual eliminati: " + recs);
      // DELETE iuf_t_trappola
      DELETE = "DELETE FROM iuf_t_trappola t \r\n" + 
                "WHERE NOT EXISTS (SELECT 1 FROM iuf_t_trappolaggio tr\r\n" + 
                "                   WHERE tr.id_trappola = t.id_trappola)";
      logger.debug(DELETE);
      recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
      logger.debug("trappole eliminate: " + recs);
      // DELETE iuf_t_ispezione_visiva
      DELETE = "DELETE FROM iuf_t_ispezione_visiva WHERE id_ispezione = :idIspezione";
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
//  public void update(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException
//  {
//    final String THIS_METHOD = "update";
//    if (logger.isDebugEnabled())
//    {
//      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
//    }
//
//    String UPDATE = "UPDATE IUF_D_CATEGORIA_ANAG SET DESCRIZIONE = :DESCRIZIONE, UTENTE = :UTENTE, data_ultimo_aggiornamento = SYSDATE "+
//        " WHERE ID_CATEGORIA_ANAG = :IDCATEGORIA";
//   
//    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
//   // mapParameterSource.addValue("DESCRIZIONE", categoria.getDescrizione() ,Types.VARCHAR);
//  //  mapParameterSource.addValue("ext_id_utente_aggiornamento", categoria.getUtente(),Types.VARCHAR);
//  //  mapParameterSource.addValue("IDCATEGORIA", categoria.getIdCategoriaAnag(),Types.VARCHAR);
//   
//    try
//    {
//      logger.debug(UPDATE);
//      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
//    }
//    catch (Throwable t)
//    {
//      InternalUnexpectedException e = new InternalUnexpectedException(t,
//          new LogParameter[]
//              {}, null, UPDATE, mapParameterSource);
//      logInternalUnexpectedException(e, THIS_METHOD);
//      throw e;
//    }
//    finally
//    {
//      if (logger.isDebugEnabled())
//      {
//        logger.debug(THIS_METHOD + " END.");
//      }
//    }
//  }


  public void update(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateVisual]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {

      mapParameterSource.addValue("idIspezione", ispezioneVisiva.getIdIspezione());
      mapParameterSource.addValue("idRilevazione", ispezioneVisiva.getIdRilevazione());
      mapParameterSource.addValue("numeroAviv", ispezioneVisiva.getNumeroAviv(), Types.VARCHAR);
      mapParameterSource.addValue("idSpecieVegetale", ispezioneVisiva.getIdSpecieVegetale());
      mapParameterSource.addValue("superfice", ispezioneVisiva.getSuperficie());      
      mapParameterSource.addValue("numeroPiante", ispezioneVisiva.getNumeroPiante());
      mapParameterSource.addValue("dataOraInizio", ispezioneVisiva.getDataOraInizio());
      mapParameterSource.addValue("dataOraFine", ispezioneVisiva.getDataOraFine());
      mapParameterSource.addValue("flagPresenzaOn", ispezioneVisiva.getFlagPresenzaOn(), Types.VARCHAR);
      mapParameterSource.addValue("istatComune", ispezioneVisiva.getIstatComune(), Types.VARCHAR);
      mapParameterSource.addValue("latitudine", ispezioneVisiva.getLatitudine());
      mapParameterSource.addValue("longitudine", ispezioneVisiva.getLongitudine());
      mapParameterSource.addValue("note", ispezioneVisiva.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("flagIndicatoreIntervento", ispezioneVisiva.getFlagIndicatoreIntervento(), Types.VARCHAR);
      mapParameterSource.addValue("riferimentoUbicazione", ispezioneVisiva.getRiferimentoUbicazione(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", ispezioneVisiva.getExtIdUtenteAggiornamento());
      mapParameterSource.addValue("idAnagrafica", ispezioneVisiva.getIdAnagrafica()); 
      mapParameterSource.addValue("cuaa", ispezioneVisiva.getCuaa());
      
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
  
  public void updateSpecieOn(IspezioneVisivaDTO ispezioneVisiva) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateVisual]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {

      mapParameterSource.addValue("idIspezione", ispezioneVisiva.getIdIspezione());
      mapParameterSource.addValue("note", ispezioneVisiva.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("idSpecieOn", ispezioneVisiva.getIdSpecieOn());
      mapParameterSource.addValue("flagTrovato", ispezioneVisiva.getFlagTrovato());
      mapParameterSource.addValue("ext_id_utente_aggiornamento", ispezioneVisiva.getExtIdUtenteAggiornamento());

      
      logger.debug(UPDATE_SPECIE_ON);
      int result = namedParameterJdbcTemplate.update(UPDATE_SPECIE_ON, mapParameterSource);
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

  public List<OrganismiNocivi> findOrganismiNociviFlagTrovatoByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findOrganismiNociviFlagTrovatoByVisual";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID_RIL = "SELECT t.id_specie_on,t.flag_trovato FROM iuf_r_isp_visiva_spec_on t \r\n" + 
                          "WHERE t.id_ispezione = :idIspezioneVisiva \r\n" +
                          "ORDER BY t.id_specie_on";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idIspezioneVisiva", idIspezioneVisiva);
    
    try
    {
      logger.debug(SELECT_BY_ID_RIL);
      List<OrganismiNocivi> list = queryForList(SELECT_BY_ID_RIL, mapParameterSource, OrganismiNocivi.class);
      return list;
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

  public List<Integer> findOrganismiNociviByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findOrganismiNociviByVisual";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT t.id_specie_on FROM iuf_r_isp_visiva_spec_on t \r\n" + 
                          "WHERE t.id_ispezione = :idIspezioneVisiva \r\n" +
                          "ORDER BY t.id_specie_on";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idIspezioneVisiva", idIspezioneVisiva);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      List<Integer> list = namedParameterJdbcTemplate.queryForList(SELECT_BY_ID, mapParameterSource, Integer.class);
      return list;
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

  public List<IspezioneVisivaSpecieOnDTO> findOrganismiNociviByVisualCompleto(Integer idIspezioneVisiva) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findOrganismiNociviByVisual";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT t.id_ispezione, t.id_specie_on as id_specie_on, "+
    "b.nome_latino as nome_latino,b.sigla as sigla,b.euro as euro,"+
    "d.id_specie_vegetale as id_specie_vegetale, "+
    "(CASE (select count(*) from iuf_r_specie_on_periodo f "+
    "     where f.id_specie_vegetale=d.id_specie_vegetale and f.id_on_da_monitorare=t.id_specie_on "+
    "     AND F.MESE_DI_MONITORAGGIO=TO_NUMBER(TO_CHAR(SYSDATE,'MM'))) "+
   " WHEN 0 THEN 'N' ELSE 'S' END) as periodo "+
    "FROM iuf_r_isp_visiva_spec_on t,"+
    "iuf_t_ispezione_visiva d,"+
    "iuf_d_organismo_nocivo b "+
    "WHERE t.id_ispezione = :idIspezioneVisiva "+
    "and t.id_specie_on=b.id_organismo_nocivo "+
    "and t.id_ispezione=d.id_ispezione";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idIspezioneVisiva", idIspezioneVisiva);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      List<IspezioneVisivaSpecieOnDTO> list = namedParameterJdbcTemplate.query(SELECT_BY_ID, mapParameterSource, getRowMapperSpecOn());
      return list;
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

  
  
  public Integer findCampionamentiCountByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findOrganismiNociviByVisual";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idIspezioneVisiva", idIspezioneVisiva);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      Integer count = namedParameterJdbcTemplate.queryForObject(SELECT_CAMPIONAMENTI_COUNT, mapParameterSource, Integer.class);
      return count;
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
  
  public Integer findTrappolaggiCountByVisual(Integer idIspezioneVisiva) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findOrganismiNociviByVisual";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idIspezioneVisiva", idIspezioneVisiva);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      Integer count = namedParameterJdbcTemplate.queryForObject(SELECT_TRAPPOLAGGI_COUNT, mapParameterSource, Integer.class);
      return count;
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
  
  public void removeOrganismoNocivo(Integer idIspezione, Integer idOrganismoNocivo) throws InternalUnexpectedException
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
      mapParameterSource.addValue("idIspezione", idIspezione);
      mapParameterSource.addValue("idOrganismoNocivo", idOrganismoNocivo);
      //DELETE iuf_r_campionamento_spec_on
      DELETE = "DELETE FROM iuf_r_isp_visiva_spec_on i \r\n" + 
          "WHERE i.id_ispezione = :idIspezione \r\n" +
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
  
  public List<IspezioneVisivaSpecieOnDTO> findOnByIdIspezione(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findOnByIdIspezione";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT t.id_ispezione,t.id_specie_on,t.flag_trovato as presenza,t.ext_id_utente_aggiornamento," + 
                              "t.data_ultimo_aggiornamento,a.sigla,a.nome_latino as nome_latino," +
                              "a.euro,a.flag_emergenza emergenza " +
                          "FROM IUF_R_ISP_VISIVA_SPEC_ON t,IUF_D_ORGANISMO_NOCIVO A " + 
                          "WHERE t.id_specie_on=a.id_organismo_nocivo " + 
                          "AND t.id_ispezione= :id " +
                          "ORDER BY a.nome_latino";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      List<IspezioneVisivaSpecieOnDTO> lista = queryForList(SELECT_BY_ID, mapParameterSource, IspezioneVisivaSpecieOnDTO.class);
      if(lista!=null && !lista.isEmpty()) {
          for(IspezioneVisivaSpecieOnDTO obj : lista) {
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
  
  private RowMapper<IspezioneVisivaSpecieOnDTO> getRowMapperSpecOn() {
    return new RowMapper<IspezioneVisivaSpecieOnDTO>() {
      
      @Override
      public IspezioneVisivaSpecieOnDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        IspezioneVisivaSpecieOnDTO specieOn = new IspezioneVisivaSpecieOnDTO();
        specieOn.setIdIspezioneVisiva(rs.getInt("id_ispezione"));
        specieOn.setIdSpecieOn(rs.getInt("id_specie_on"));
        specieOn.setEuro(rs.getString("euro"));
        specieOn.setNomeLatino(rs.getString("nome_latino"));
        specieOn.setSigla(rs.getString("sigla"));
        specieOn.setPeriodo(rs.getString("periodo"));
         return specieOn;
      }
    };
  }

  

  public void insertVisualSpecieOn(IspezioneVisivaSpecieOnDTO visual) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertVisualSpecieOn]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
       
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      mapParameterSource.addValue("idIspezione", visual.getIdIspezioneVisiva());
      mapParameterSource.addValue("idSpecieOn", visual.getIdSpecieOn());
      mapParameterSource.addValue("flagTrovato", visual.getPresenza(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", visual.getExtIdUtenteAggiornamento());
    
      logger.debug(INSERT_SPECIE_ON);
      namedParameterJdbcTemplate.update(INSERT_SPECIE_ON, mapParameterSource, holder, new String[] {"id_ispezione"});

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[]
          {}, 
          new LogVariable[] 
          {}, INSERT_SPECIE_ON, mapParameterSource);
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

 
  private RowMapper<OrganismiNocivi> getRowMapperOrgNoc() {
    return new RowMapper<OrganismiNocivi>() {
      
      @Override
      public OrganismiNocivi mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrganismiNocivi orgNoc = new OrganismiNocivi();
        orgNoc.setIdSpecieOn(rs.getInt("id_specie_on"));
        orgNoc.setFlagTrovato(rs.getString("flag_trovato"));
        return orgNoc;
      }
    };
  }

}
