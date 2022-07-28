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
import it.csi.iuffi.iuffiweb.model.OperazioneTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.TipoTrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaggioDTO;
import it.csi.iuffi.iuffiweb.model.api.Trappola;
import it.csi.iuffi.iuffiweb.model.request.TrappolaggioRequest;

public class GestioneTrappolaDAO extends BaseDAO {

	private static final String THIS_CLASS = GestioneTrappolaDAO.class.getSimpleName();
	private LobHandler lobHandler;

	private static final String SELECT_STORIA_TRAPPOLA ="select b.cognome || ' ' || b.nome AS ispettore,t.data_ora_inizio,"+
	"t.data_ora_fine,i.desc_operazione_trappola as descr_operazione,t.id_organismo_nocivo,t.id_operazione, t.id_trappolaggio,a.codice_sfr "+ 
	"from IUF_T_TRAPPOLAGGIO t, iuf_t_trappola a, iuf_t_anagrafica b,"+
	"iuf_d_operazione_trappola i  "+
	"where a.codice_sfr= :codice and a.id_trappola=t.id_trappola and t.id_anagrafica=b.id_anagrafica "+
	"and t.id_operazione=i.id_operazione_trappola " + 
	"order by t.data_ora_inizio asc,t.id_trappolaggio";
	
	private static final String SELECT_TRAPPOLA ="select * from iuf_t_trappola a where a.codice_sfr= :codice";
	
    private static final String SELECT_ALL_ID_MULTIPLI = "select * from iuf_d_operazione_trappola t where id_operazione_trappola in(%s)";
    
	public GestioneTrappolaDAO() {
	}

	/**
	 * 
	 * @return
	 * @throws InternalUnexpectedException
	 */
	public TrappolaDTO insertTrappola(TrappolaDTO trappola) throws InternalUnexpectedException {
		KeyHolder holder = new GeneratedKeyHolder();
		String THIS_METHOD = "[" + THIS_CLASS + "::insertTrappola]";

		if (logger.isDebugEnabled()) {
			logger.debug(THIS_METHOD + " BEGIN.");
		}

		String INSERT_TRAPPOLA = "INSERT INTO iuf_t_trappola(id_trappola,id_tipo_trappola,latitudine,longitudine,id_specie_veg,"
				+ "data_installazione, data_rimozione, ext_id_utente_aggiornamento,data_ultimo_aggiornamento,codice_sfr) \r\n"
				+ "VALUES(seq_iuf_t_trappola.NEXTVAL,:idTipoTrappola,:latitudine,:longitudine,:idSpecieVegetale,"
				+ ":dataInstallazione, :dataRimozione, :ext_id_utente_aggiornamento,SYSDATE,:codiceSfr)";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idTipoTrappola", trappola.getIdTipoTrappola());
		mapParameterSource.addValue("latitudine", trappola.getLatitudine());
		mapParameterSource.addValue("longitudine", trappola.getLongitudine());
		mapParameterSource.addValue("idSpecieVegetale", trappola.getIdSpecieVeg());
		mapParameterSource.addValue("dataInstallazione", trappola.getDataInstallazione());
		mapParameterSource.addValue("dataRimozione", trappola.getDataRimozione());
		mapParameterSource.addValue("ext_id_utente_aggiornamento", trappola.getExtIdUtenteAggiornamento());
		mapParameterSource.addValue("codiceSfr", trappola.getCodiceSfr());
		try {
			logger.debug(INSERT_TRAPPOLA);
			@SuppressWarnings("unused")
			int result = namedParameterJdbcTemplate.update(INSERT_TRAPPOLA, mapParameterSource, holder,
					new String[] { "id_trappola" });
			Integer idTrappola = holder.getKey().intValue();
			trappola.setIdTrappola(idTrappola);
			return trappola;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {},
					new LogVariable[] {}, INSERT_TRAPPOLA, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			// throw e;
			throw new InternalUnexpectedException(t);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public void updateTrappola(TrappolaDTO trappola) throws InternalUnexpectedException {
		String THIS_METHOD = "[" + THIS_CLASS + "::updateTrappola]";

		if (logger.isDebugEnabled()) {
			logger.debug(THIS_METHOD + " BEGIN.");
		}

		String UPDATE_TRAPPOLA = "UPDATE iuf_t_trappola \r\n" + "SET id_tipo_trappola = :idTipoTrappola,\r\n"
				+ "latitudine = :latitudine,\r\n" + "longitudine = :longitudine,\r\n"
				+ "id_specie_veg = :idSpecieVegetale,\r\n" + "data_installazione = :dataInstallazione,\r\n"
				+ "data_rimozione = :dataRimozione, \r\n"
				+ "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento,\r\n"
				+ "data_ultimo_aggiornamento = SYSDATE,\r\n" + "codice_sfr = :codiceSfr \r\n"
				+ "WHERE id_trappola = :idTrappola";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idTrappola", trappola.getIdTrappola());
		mapParameterSource.addValue("idTipoTrappola", trappola.getIdTipoTrappola());
		mapParameterSource.addValue("latitudine", trappola.getLatitudine());
		mapParameterSource.addValue("longitudine", trappola.getLongitudine());
		mapParameterSource.addValue("idSpecieVegetale", trappola.getIdSpecieVeg());
		mapParameterSource.addValue("dataInstallazione", trappola.getDataInstallazione());
		mapParameterSource.addValue("dataRimozione", trappola.getDataRimozione());
		mapParameterSource.addValue("ext_id_utente_aggiornamento", trappola.getExtIdUtenteAggiornamento());
		mapParameterSource.addValue("codiceSfr", trappola.getCodiceSfr());
		try {
			logger.debug(UPDATE_TRAPPOLA);
			@SuppressWarnings("unused")
			int result = namedParameterJdbcTemplate.update(UPDATE_TRAPPOLA, mapParameterSource);
			// TrappolaDTO trapUpdated = (TrappolaDTO) holder;
			// return trapUpdated;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {},
					new LogVariable[] {}, UPDATE_TRAPPOLA, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public List<TrappolaggioDTO> findAll() throws InternalUnexpectedException {
		final String THIS_METHOD = "findAll";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}
		String SELECT_ALL = "SELECT * FROM IUF_T_TRAPPOLA t  ORDER BY t.ID_TRAPPOLA ";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		try {
			logger.debug(SELECT_ALL);
			return queryForList(SELECT_ALL, mapParameterSource, TrappolaggioDTO.class);
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, SELECT_ALL,
					mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	
	 public List<TrappolaggioDTO> findStoriaTrappolaByCodice(String codice) throws InternalUnexpectedException {
	    final String THIS_METHOD = "findStoriaTrappola";
	    if (logger.isDebugEnabled()) {
	      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
	    }  

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    mapParameterSource.addValue("codice", codice); 
	    
	    try {
	      logger.debug(SELECT_STORIA_TRAPPOLA);
	      return queryForList(SELECT_STORIA_TRAPPOLA, mapParameterSource, TrappolaggioDTO.class);
	    } catch (Throwable t) {
	      InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, SELECT_STORIA_TRAPPOLA,
	          mapParameterSource);
	      logInternalUnexpectedException(e, THIS_METHOD);
	      throw e;
	    } finally {
	      if (logger.isDebugEnabled()) {
	        logger.debug(THIS_METHOD + " END.");
	      }
	    }
	  }

	 
   public TrappolaDTO findTrappolaByCodiceSfr(String codice) throws InternalUnexpectedException {
     final String THIS_METHOD = "findTrappolaByCodiceSfr";
     if (logger.isDebugEnabled()) {
       logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
     }  

     MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
     mapParameterSource.addValue("codice", codice); 
     
     try {
       logger.debug(SELECT_TRAPPOLA);
       return queryForObject(SELECT_TRAPPOLA, mapParameterSource, TrappolaDTO.class);
     } catch (Throwable t) {
       InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, SELECT_TRAPPOLA,
           mapParameterSource);
       logInternalUnexpectedException(e, THIS_METHOD);
       throw e;
     } finally {
       if (logger.isDebugEnabled()) {
         logger.debug(THIS_METHOD + " END.");
       }
     }
   }

	public List<TipoTrappolaDTO> findTipoTrappolaByFilter(TipoTrappolaDTO tipoTrappola)
			throws InternalUnexpectedException {
		final String THIS_METHOD = "findByFilter";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_FILTER = "select DISTINCT A.TIPOLOGIA_TRAPPOLA AS TIPOLOGIA_TRAPPOLA,A.ID_TIPO_TRAPPOLA,"
				+ "A.SFR_CODE,C.NOME_VOLGARE AS SPECIE "
				+ "FROM IUF_D_TIPO_TRAPPOLA A,IUF_T_TRAPPOLA B,IUF_D_SPECIE_VEGETALE C "
				+ "WHERE A.ID_TIPO_TRAPPOLA=B.ID_TIPO_TRAPPOLA " + " AND B.ID_SPECIE_VEG=C.ID_SPECIE_VEGETALE";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

		try {
			logger.debug(SELECT_BY_FILTER);
			List<TipoTrappolaDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, TipoTrappolaDTO.class);
			return list;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_FILTER, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public List<Trappola> findTrappolaByCoordinates(Double latitudine, Double longitudine, Integer raggio)
			throws InternalUnexpectedException {
		// Restituisce le trappole nella zona in base alle coordinate geografiche di
		// un punto centrale
		final String THIS_METHOD = "findTrappolaByCoordinates";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}
		String SELECT = "SELECT t.id_trappola,\r\n" +
		                      "t.id_tipo_trappola,\r\n" +
		                      "t.latitudine,\r\n" +
		                      "t.longitudine,\r\n" +
		                      "t.id_specie_veg,\r\n" +
		                      "TO_CHAR(t.data_installazione,'DD-MM-YYYY') data_installazione,\r\n" +
		                      "TO_CHAR(t.data_rimozione,'DD-MM-YYYY') data_rimozione,\r\n" +
		                      "t.codice_sfr,\r\n" +
		                      "tt.tipologia_trappola descr_trappola,\r\n" +
		                      "sv.genere_specie specie,\r\n" +
		                      "MAX(tr.id_organismo_nocivo) id_organismo_nocivo \r\n" +
		               " FROM iuf_t_trappola t, iuf_d_tipo_trappola tt, iuf_d_specie_vegetale sv,iuf_t_trappolaggio tr \r\n" +
		               "WHERE t.id_tipo_trappola = tt.id_tipo_trappola \r\n " +
		               "AND t.id_specie_veg = sv.id_specie_vegetale \r\n" +
		               "AND t.latitudine BETWEEN :latitudine - :deltaLAT AND :latitudine + :deltaLAT \r\n" +
		               "AND t.longitudine BETWEEN :longitudine - :deltaLON AND :longitudine + :deltaLON \r\n" +
		               "AND t.id_trappola = tr.id_trappola \r\n" +
		            "GROUP BY t.id_trappola,\r\n" +
		               "t.id_tipo_trappola,\r\n" +
		               "t.latitudine,\r\n" +
		               "t.longitudine,\r\n" +
		               "t.id_specie_veg,\r\n" +
		               "TO_CHAR(t.data_installazione, 'DD-MM-YYYY'),\r\n" +
		               "TO_CHAR(t.data_rimozione, 'DD-MM-YYYY'),\r\n" +
		               "t.codice_sfr,\r\n" +
		               "tt.tipologia_trappola,\r\n" +
		               "sv.genere_specie \r\n" +
		            "ORDER BY latitudine,longitudine";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("latitudine", latitudine);
		mapParameterSource.addValue("longitudine", longitudine);

		Double delta = new Double(raggio) / new Double(111319);

		mapParameterSource.addValue("deltaLAT", delta);
		mapParameterSource.addValue("deltaLON", delta);

		try {
			logger.debug(SELECT);
			List<Trappola> list = queryForList(SELECT, mapParameterSource, Trappola.class);
			return list;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, SELECT,
					mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public List<TrappolaggioDTO> findByFilter(TrappolaggioDTO trappolaggio) throws InternalUnexpectedException {
		final String THIS_METHOD = "findByFilter";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_FILTER = "SELECT a.id_trappolaggio,\r\n" + "a.id_rilevazione,\r\n" + "a.id_trappola,\r\n"
				+ "a.istat_comune,\r\n" + "h.descrizione_comune descr_comune,\r\n" + "a.data_ora_inizio,\r\n"
				+ "a.data_ora_fine,\r\n" + "e.tipologia_trappola AS descr_trappola,\r\n"
				+ "d.genere_specie AS specie,\r\n"
				+ "(CASE WHEN(SELECT COUNT(*) FROM iuf_r_trappolaggio_foto d WHERE d.id_trappolaggio=a.id_trappolaggio)>0 THEN 'S' ELSE 'N' END) AS FOTO,\r\n"
				+ "g.cognome ||' '|| g.nome AS ispettore,\r\n" + "a.id_anagrafica,\r\n" + "a.id_operazione,\r\n"
				+ "i.desc_operazione_trappola descr_operazione,\r\n" + "a.data_trappolaggio,\r\n"
				+ "a.id_ispezione_visiva,\r\n" + "b.id_missione,\r\n" + "f.data_installazione,\r\n"
				+ "f.data_rimozione,\r\n" + "f.latitudine,\r\n" + "f.longitudine,\r\n" + "f.codice_sfr,\r\n"
				+ "f.id_specie_veg,\r\n" + "f.id_tipo_trappola,\r\n" + "a.id_organismo_nocivo,\r\n" + "a.note, \r\n" +"a.data_ora_inizio \r\n"
				+ "FROM iuf_t_trappolaggio a, iuf_t_missione b,\r\n"
				+ "iuf_t_rilevazione c, iuf_d_specie_vegetale d,\r\n" + "iuf_d_tipo_trappola e, iuf_t_trappola f,\r\n"
				+ "iuf_t_anagrafica g, smrgaa_v_dati_amministrativi h,\r\n" + "iuf_d_operazione_trappola i \r\n"
				+ "WHERE a.id_rilevazione = c.id_rilevazione \r\n" + "AND c.id_missione = b.id_missione \r\n"
				+ "AND f.id_trappola = a.id_trappola \r\n" + "AND f.id_specie_veg = d.id_specie_vegetale \r\n"
				+ "AND f.id_tipo_trappola = e.id_tipo_trappola \r\n"
				+ "AND a.id_rilevazione = NVL(:idRilevazione,a.id_rilevazione) \r\n"
				+ "AND a.id_trappola = NVL(:idTrappola,a.id_trappola) \r\n"
				+ "AND a.id_anagrafica = g.id_anagrafica \r\n" + "AND a.istat_comune = h.istat_comune (+) \r\n"
				+ "AND a.id_operazione = i.id_operazione_trappola (+) \r\n" + "ORDER BY a.id_trappolaggio";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	      
		if (trappolaggio.getIdRilevazione() != null && trappolaggio.getIdRilevazione() > 0)
			mapParameterSource.addValue("idRilevazione", trappolaggio.getIdRilevazione());
		else
			mapParameterSource.addValue("idRilevazione", null, Types.NULL);

		if (trappolaggio.getTrappola() != null && trappolaggio.getTrappola().getIdTrappola() != null
				&& trappolaggio.getTrappola().getIdTrappola().intValue() > 0)
			mapParameterSource.addValue("idTrappola", trappolaggio.getTrappola().getIdTrappola());
		else
			mapParameterSource.addValue("idTrappola", null, Types.NULL);

		try {
			logger.debug(SELECT_BY_FILTER);
			// List<TrappolaggioDTO> list = queryForList(SELECT_BY_FILTER,
			// mapParameterSource, TrappolaggioDTO.class);
			List<TrappolaggioDTO> list = namedParameterJdbcTemplate.query(SELECT_BY_FILTER, mapParameterSource,
					getRowMapper());
			return list;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_FILTER, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public List<TrappolaggioDTO> findByFilterByTrappolaggioRequest(TrappolaggioRequest trappolaggio, Integer idAnagrafica, Integer idEnte)
			throws InternalUnexpectedException {
		final String THIS_METHOD = "findByFilterByTrappolaggioRequest";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_FILTER = "SELECT a.id_trappolaggio,\r\n" +
		                              "a.id_rilevazione,\r\n" +
		                              "a.id_trappola,\r\n" +
		                              "a.istat_comune,\r\n" +
		                              "h.descrizione_comune descr_comune,\r\n" +
		                              "a.data_ora_inizio,\r\n" +
		                              "a.data_ora_fine,\r\n" +
		                              "e.tipologia_trappola AS descr_trappola,\r\n" +
		                              "d.genere_specie AS specie,\r\n" +
		                              "d.nome_volgare AS pianta,\r\n" +
		                              "(CASE WHEN(SELECT COUNT(*) FROM iuf_r_trappolaggio_foto d WHERE d.id_trappolaggio=a.id_trappolaggio)>0 THEN 'S' ELSE 'N' END) AS FOTO,\r\n" +
		                              "g.cognome ||' '|| g.nome AS ispettore,\r\n" +
		                              "a.id_anagrafica,\r\n" +
		                              "b.id_ispettore_assegnato,\r\n" + 
		                              "mag.cognome ||' '|| mag.nome as ispettore_missione,"+
		                              "a.id_operazione,\r\n" +
		                              "i.desc_operazione_trappola descr_operazione,\r\n" +
		                              "a.data_trappolaggio,\r\n" +
		                              "a.id_ispezione_visiva,\r\n" +
		                              "b.id_missione,\r\n" +
		                              "f.data_installazione,\r\n" +
		                              "f.data_rimozione,\r\n" +
		                              "f.latitudine,\r\n" +
		                              "f.longitudine,\r\n" +
		                              "f.codice_sfr,\r\n" +
		                              "f.id_specie_veg,\r\n" +
		                              "c.cuaa, \r\n" +
		                              "f.id_tipo_trappola,\r\n" +
		                              "a.id_organismo_nocivo,\r\n" +
		                              " to_char(a.data_ora_inizio, 'HH24:MI:SS') as ora_inizio,\r\n" + 
		                              " to_char(a.data_ora_fine, 'HH24:MI:SS') as ora_fine,\r\n"+		                              
		                              " to_char(b.data_ora_inizio_missione, 'HH24:MI:SS') as ora_inizio_missione,\r\n" + 
		                              " to_char(b.data_ora_fine_missione, 'HH24:MI:SS') as ora_fine_missione,\r\n"+		
		                              " c.id_tipo_area,\r\n" +		                              
		                              "a.note, \r\n" +
		                              "ar.dettaglio_tipo_area,\r\n" +
		                              "orn.nome_latino,\r\n" +
		                              "(SELECT NVL(MAX(v.id_stato_verbale),0) FROM iuf_t_verbale v WHERE v.id_missione = b.id_missione) id_stato_verbale \r\n" +
		                        "FROM iuf_t_trappolaggio a, iuf_t_missione b,\r\n" +
		                            "iuf_t_rilevazione c, iuf_d_specie_vegetale d,\r\n" +
		                            "iuf_d_tipo_trappola e, iuf_t_trappola f,\r\n" +
		                            "iuf_t_anagrafica g, smrgaa_v_dati_amministrativi h,\r\n" +
		                            "iuf_d_operazione_trappola i,iuf_t_anagrafica mag, \r\n" +
		                            "iuf_d_tipo_area ar,iuf_d_organismo_nocivo orn "+
		                        "WHERE a.id_rilevazione = c.id_rilevazione \r\n" +
		                        "AND c.id_missione = b.id_missione \r\n" +
		                        "AND f.id_trappola = a.id_trappola \r\n" +
		                        "AND ar.id_tipo_area=c.id_tipo_area\r\n" + 
                            "AND orn.id_organismo_nocivo (+) = a.id_organismo_nocivo "+
		                        "AND f.id_specie_veg = d.id_specie_vegetale (+) \r\n" +
		                        "AND f.id_tipo_trappola = e.id_tipo_trappola \r\n" +
		                        "AND a.id_rilevazione = NVL(:idRilevazione,a.id_rilevazione) \r\n" +
		                        "AND a.id_trappola = NVL(:idTrappola,a.id_trappola) \r\n" +
		                        "AND a.id_anagrafica = g.id_anagrafica \r\n" +
		                        "AND a.istat_comune = h.istat_comune (+) \r\n" +
		                        "AND mag.id_anagrafica = b.id_ispettore_assegnato "+
		                        "AND a.id_operazione = i.id_operazione_trappola (+) \r\n" +
		                        "AND TRUNC(a.data_trappolaggio) BETWEEN TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2100'),'DD/MM/YYYY') \r\n" +
		                        "AND a.id_trappolaggio = NVL(:idTrappolaggio,a.id_trappolaggio) \r\n";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

		if (trappolaggio.getIdRilevazione() != null && trappolaggio.getIdRilevazione() > 0)
			mapParameterSource.addValue("idRilevazione", trappolaggio.getIdRilevazione());
		else
			mapParameterSource.addValue("idRilevazione", null, Types.NULL);

		if (trappolaggio.getTrappola() != null && trappolaggio.getTrappola().getIdTrappola() != null
				&& trappolaggio.getTrappola().getIdTrappola().intValue() > 0)
			mapParameterSource.addValue("idTrappola", trappolaggio.getTrappola().getIdTrappola());
		else
			mapParameterSource.addValue("idTrappola", null, Types.NULL);

		if (trappolaggio.getIdTrappolaggio() != null && trappolaggio.getIdTrappolaggio().intValue() > 0)
			mapParameterSource.addValue("idTrappolaggio", trappolaggio.getIdTrappolaggio());
		else
			mapParameterSource.addValue("idTrappolaggio", null, Types.NULL);
/*
		if (trappolaggio.getIstatComune() != null && trappolaggio.getIstatComune().trim().length() > 0) {
			SELECT_BY_FILTER += " AND a.istat_comune = :istatComune ";
			mapParameterSource.addValue("istatComune", trappolaggio.getIstatComune());
		}
*/
	      
	      
    if (trappolaggio.getIstatComune() != null && trappolaggio.getIstatComune().trim().length() > 0 || StringUtils.isNotBlank(trappolaggio.getDescrComune())) {
      if (trappolaggio.getIstatComune() != null && trappolaggio.getIstatComune().trim().length() > 0) {
        SELECT_BY_FILTER += " AND a.istat_comune = :istatComune ";
        mapParameterSource.addValue("istatComune", trappolaggio.getIstatComune());
      }     
      if (StringUtils.isNotBlank(trappolaggio.getDescrComune())) {
        SELECT_BY_FILTER += " AND UPPER(h.descrizione_comune) LIKE NVL(UPPER(:descComune),'%') ";
        mapParameterSource.addValue("descComune", trappolaggio.getDescrComune());
      }
    }      

		
		// filtro multiplo per tipo area
		if (trappolaggio.getTipoArea() != null && trappolaggio.getTipoArea().size() > 0) {
			String tipoAree = "";
			for (int i = 0; i < trappolaggio.getTipoArea().size(); i++) {
				tipoAree += trappolaggio.getTipoArea().get(i);
				if (i < trappolaggio.getTipoArea().size() - 1)
					tipoAree += ",";
			}
			SELECT_BY_FILTER += " AND c.id_tipo_area IN (" + tipoAree + ") \r\n";
		}
		// filtro multiplo per ispettore assegnato
		if (trappolaggio.getIspettoreAssegnato() != null && trappolaggio.getIspettoreAssegnato().size() > 0) {
			String ispettori = "";
			for (int i = 0; i < trappolaggio.getIspettoreAssegnato().size(); i++) {
				ispettori += trappolaggio.getIspettoreAssegnato().get(i);
				if (i < trappolaggio.getIspettoreAssegnato().size() - 1)
					ispettori += ",";
			}
      SELECT_BY_FILTER += " AND a.id_anagrafica IN (" + ispettori + ") \r\n";
		}
		// filtro multiplo per specie vegetale
		if (trappolaggio.getSpecieVegetale() != null && trappolaggio.getSpecieVegetale().size() > 0) {
			String specieVegetali = "";
			for (int i = 0; i < trappolaggio.getSpecieVegetale().size(); i++) {
				specieVegetali += trappolaggio.getSpecieVegetale().get(i);
				if (i < trappolaggio.getSpecieVegetale().size() - 1)
					specieVegetali += ",";
			}
			SELECT_BY_FILTER += " AND f.id_specie_veg IN (" + specieVegetali + ") ";
		}
		// filtro multiplo per organismo nocivo
		if (trappolaggio.getOrganismoNocivo() != null && trappolaggio.getOrganismoNocivo().size() > 0) {
			String organismiNocivi = "";
			for (int i = 0; i < trappolaggio.getOrganismoNocivo().size(); i++) {
				organismiNocivi += trappolaggio.getOrganismoNocivo().get(i);
				if (i < trappolaggio.getOrganismoNocivo().size() - 1)
					organismiNocivi += ",";
			}
			SELECT_BY_FILTER += " AND a.id_organismo_nocivo IN (" + organismiNocivi + ") ";
		}
		// filtro multiplo per tipo trappola
		if (trappolaggio.getTipoTrappola() != null && trappolaggio.getTipoTrappola().size() > 0) {
			String tipoTrappole = "";
			for (int i = 0; i < trappolaggio.getTipoTrappola().size(); i++) {
				tipoTrappole += trappolaggio.getTipoTrappola().get(i);
				if (i < trappolaggio.getTipoTrappola().size() - 1)
					tipoTrappole += ",";
			}
			SELECT_BY_FILTER += " AND f.id_tipo_trappola IN (" + tipoTrappole + ") ";
		}
		
		// filtro multiplo per tipo operazione
		if (trappolaggio.getTipoOperazione() != null && trappolaggio.getTipoOperazione().size() > 0) {
			String tipoOperazione = "";
			for (int i = 0; i < trappolaggio.getTipoOperazione().size(); i++) {
				tipoOperazione += trappolaggio.getTipoOperazione().get(i);
				if (i < trappolaggio.getTipoOperazione().size() - 1)
					tipoOperazione += ",";
			}
			SELECT_BY_FILTER += " AND i.id_operazione_trappola IN (" + tipoOperazione + ") ";
		}

		
	    //filtro per cuaa o n. autorizzazione
	    if(trappolaggio.getCuaa()!=null && !trappolaggio.getCuaa().equals(""))
	       SELECT_BY_FILTER += " AND (c.ext_numero_aviv = " + "'" + trappolaggio.getCuaa() + "'"  + " OR C.cuaa = " + "'" + trappolaggio.getCuaa() + "')";
	
		if (trappolaggio.getCodiceSfr() != null && !trappolaggio.getCodiceSfr().equals("")) {
			  SELECT_BY_FILTER += " AND f.codice_sfr like " + "'%" + trappolaggio.getCodiceSfr() + "%' \r\n";
	      //mapParameterSource.addValue("codiceSfr", trappolaggio.getCodiceSfr());
	    }
	    
		if (trappolaggio.getIdMissione() != null && trappolaggio.getIdMissione() > 0) {
		  SELECT_BY_FILTER += " AND b.id_missione = :idMissione \r\n";
      mapParameterSource.addValue("idMissione", trappolaggio.getIdMissione());
    }
		
    // filtro per obiettivo missione Indagine ufficiale
    if (trappolaggio.getObiettivoIndagineUfficiale() != null && trappolaggio.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
      if (trappolaggio.getObiettivoEmergenza() == null || !trappolaggio.getObiettivoEmergenza().equalsIgnoreCase("S")) {
        SELECT_BY_FILTER += " AND NVL(c.flag_emergenza,'N') = 'N' ";
      }
    }
    // filtro per obiettivo missione Emergenza
    if (trappolaggio.getObiettivoEmergenza() != null && trappolaggio.getObiettivoEmergenza().equalsIgnoreCase("S")) {
      if (trappolaggio.getObiettivoIndagineUfficiale() == null || !trappolaggio.getObiettivoIndagineUfficiale().equalsIgnoreCase("S")) {
        SELECT_BY_FILTER += " AND c.flag_emergenza = 'S' ";
      }
    }

    if (trappolaggio.getIdIspezioneVisiva() != null && trappolaggio.getIdIspezioneVisiva() > 0) {
        SELECT_BY_FILTER += " AND a.id_ispezione_visiva = :idIspezioneVisiva ";
        mapParameterSource.addValue("idIspezioneVisiva", trappolaggio.getIdIspezioneVisiva());
    }
    
    // Filtro per Anagrafica (ispettore loggato)
    if (idAnagrafica != null && idAnagrafica > 0) {
    	SELECT_BY_FILTER += " AND (b.id_ispettore_assegnato = :idAnagrafica " +
                     "OR EXISTS (SELECT 1 FROM iuf_r_ispettore_aggiunto ia " +
                                 "WHERE ia.id_missione = b.id_missione AND ia.id_anagrafica = :idAnagrafica))";
      mapParameterSource.addValue("idAnagrafica", idAnagrafica);
    }
    // Filtro per Ente (ente ispettore loggato)
    if (idEnte != null && idEnte > 0) {
    	SELECT_BY_FILTER += " AND g.id_ente = :idEnte";
      mapParameterSource.addValue("idEnte", idEnte);
    }

		if (trappolaggio.getAnno() != null && trappolaggio.getAnno().intValue() > 0) {
			SELECT_BY_FILTER += " AND TO_CHAR(a.data_trappolaggio,'YYYY') = :anno \r\n";
			mapParameterSource.addValue("anno", trappolaggio.getAnno().toString());
		}
		mapParameterSource.addValue("dallaData",
				(trappolaggio.getDallaDataS() != null) ? trappolaggio.getDallaDataS() : "");
		mapParameterSource.addValue("allaData",
				(trappolaggio.getAllaDataS() != null) ? trappolaggio.getAllaDataS() : "");
		mapParameterSource.addValue("emergenza", trappolaggio.getObiettivoEmergenza(), Types.VARCHAR);

		SELECT_BY_FILTER += " ORDER BY a.data_ora_inizio DESC";

		try {
			logger.debug(SELECT_BY_FILTER);
			//List<TrappolaggioDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, TrappolaggioDTO.class);
			List<TrappolaggioDTO> list = namedParameterJdbcTemplate.query(SELECT_BY_FILTER, mapParameterSource, new RowMapper<TrappolaggioDTO>()
      {
			  @Override  
        public TrappolaggioDTO mapRow(ResultSet rs, int rownumber) throws SQLException {
			    TrappolaggioDTO trap = new TrappolaggioDTO();
			    trap.setIdTrappolaggio(rs.getInt("id_trappolaggio"));
			    trap.setIdRilevazione(rs.getInt("id_rilevazione"));
			    trap.setIdTrappola(rs.getInt("id_trappola"));
			    trap.setIstatComune(rs.getString("istat_comune"));
			    trap.setDescrComune(rs.getString("descr_comune"));
			    trap.setDataOraInizio(rs.getTimestamp("data_ora_inizio"));
			    trap.setDataOraFine(rs.getTimestamp("data_ora_fine"));
			    trap.setDescrTrappola(rs.getString("descr_trappola"));
			    trap.setSpecie(rs.getString("specie"));
			    trap.setPianta(rs.getString("pianta"));
			    trap.setFoto(rs.getString("foto").equals("S"));
			    trap.setIspettore(rs.getString("ispettore"));
	         trap.setIspettoreMissione(rs.getString("ispettore_missione"));
			    trap.setIdAnagrafica(rs.getInt("id_anagrafica"));
			    trap.setIdOperazione(rs.getInt("id_operazione"));
			    trap.setDescrOperazione(rs.getString("descr_operazione"));
			    trap.setDataTrappolaggio(rs.getTimestamp("data_trappolaggio"));
			    trap.setIdIspezioneVisiva(rs.getInt("id_ispezione_visiva"));
			    trap.setIdMissione(rs.getLong("id_missione"));
			    trap.setOraInizio(rs.getString("ora_inizio"));
			    trap.setOraFine(rs.getString("ora_fine"));
			    trap.setIdTipoArea(rs.getInt("id_tipo_area"));
			    trap.setIdSpecieVegetale(rs.getInt("id_specie_veg"));
			    trap.setOraInizioMissione(rs.getString("ora_inizio_missione"));
			    trap.setOraFineMissione(rs.getString("ora_fine_missione"));
			    trap.setIdOrganismoNocivo(rs.getInt("id_organismo_nocivo"));
			    trap.setNote(rs.getString("note"));
			    trap.setNomeLatino(rs.getString("nome_latino"));
			    trap.setDettaglioTipoArea(rs.getString("dettaglio_tipo_area"));
			    trap.setCodiceSfr(rs.getString("codice_sfr"));
			    trap.setIdStatoVerbale(rs.getInt("id_stato_verbale"));
			    return trap;
			  }
      });
			return list;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_FILTER, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public TrappolaggioDTO findById(Integer idTrappolaggio) throws InternalUnexpectedException {
		final String THIS_METHOD = "findById";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_ID = "SELECT * FROM iuf_t_trappolaggio WHERE id_trappolaggio = :idTrappolaggio";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idTrappolaggio", idTrappolaggio);

		try {
			logger.debug(SELECT_BY_ID);
			TrappolaggioDTO trappolaggio = queryForObject(SELECT_BY_ID, mapParameterSource, TrappolaggioDTO.class);
			return trappolaggio;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_ID, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public void remove(Integer idTrappolaggio) throws InternalUnexpectedException {
		final String THIS_METHOD = "remove";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String DELETE = "DELETE FROM iuf_r_trappolaggio_foto WHERE id_trappolaggio = :idTrappolaggio";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idTrappolaggio", idTrappolaggio);

		try {
			logger.debug(DELETE);
			namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
			DELETE = "DELETE FROM iuf_t_trappolaggio WHERE id_trappolaggio = :idTrappolaggio";
			logger.debug(DELETE);
			int recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
			logger.debug("trappolaggi eliminati: " + recs);
			// DELETE iuf_t_trappola
			DELETE = "DELETE FROM iuf_t_trappola t\r\n" + // Elimino eventuali
			// trappole inutilizzate
					"WHERE NOT EXISTS (SELECT 1 FROM iuf_t_trappolaggio tr " + "WHERE tr.id_trappola = t.id_trappola)";
			logger.debug(DELETE);
			recs = namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
			logger.debug("trappole eliminate: " + recs);
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, DELETE,
					mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public TrappolaggioDTO insert(TrappolaggioDTO trappolaggio) throws InternalUnexpectedException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String THIS_METHOD = "insert";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String INSERT = "INSERT INTO iuf_t_trappolaggio(id_trappolaggio,id_rilevazione,id_trappola,istat_comune,data_ora_inizio,data_ora_fine,"
				+ "id_operazione,ext_id_utente_aggiornamento,data_ultimo_aggiornamento,id_anagrafica,note,"
				+ "data_trappolaggio,id_ispezione_visiva,id_organismo_nocivo) "
				+ "VALUES(seq_iuf_t_trappolaggio.NEXTVAL,:idRilevazione,:idTrappola,:istatComune,:dataOraInizio,:dataOraFine,:idOperazione,"
				+ ":extIdUtenteAggiornamento,SYSDATE,:idAnagrafica,:note,:dataTrappolaggio,:idIspezioneVisiva,:idOrganismoNocivo)";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idRilevazione", trappolaggio.getIdRilevazione());
		mapParameterSource.addValue("idTrappola", trappolaggio.getTrappola().getIdTrappola());
		mapParameterSource.addValue("istatComune", trappolaggio.getIstatComune());
		mapParameterSource.addValue("dataOraInizio", trappolaggio.getDataOraInizio());
		mapParameterSource.addValue("dataOraFine", trappolaggio.getDataOraFine());
		mapParameterSource.addValue("idOperazione", trappolaggio.getIdOperazione());
		mapParameterSource.addValue("extIdUtenteAggiornamento", trappolaggio.getExtIdUtenteAggiornamento());
		mapParameterSource.addValue("idAnagrafica", trappolaggio.getIdAnagrafica());
		mapParameterSource.addValue("note", trappolaggio.getNote());
		mapParameterSource.addValue("dataTrappolaggio", trappolaggio.getDataTrappolaggio());
		mapParameterSource.addValue("idIspezioneVisiva", trappolaggio.getIdIspezioneVisiva());
		mapParameterSource.addValue("idOrganismoNocivo", trappolaggio.getIdOrganismoNocivo());

		try {
			logger.debug(INSERT);
			@SuppressWarnings("unused")
			int result = namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder,
					new String[] { "id_trappolaggio" });
			Integer idTrappolaggio = holder.getKey().intValue();
			trappolaggio.setIdTrappolaggio(idTrappolaggio);
			return trappolaggio;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, INSERT,
					mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public void update(TrappolaggioDTO trappolaggio) throws InternalUnexpectedException {
		final String THIS_METHOD = "update";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String UPDATE = "UPDATE iuf_t_trappolaggio \r\n" 
		        + "SET ext_id_utente_aggiornamento = :extIdUtenteAggiornamento,\r\n"
				+ " data_ultimo_aggiornamento = SYSDATE,\r\n"
				+ " id_anagrafica = :idAnagrafica,\r\n"
				+ " note = :note,\r\n" 
				+ " id_organismo_nocivo = :idOrganismoNocivo \r\n"
				+ "WHERE id_trappolaggio = :idTrappolaggio";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("extIdUtenteAggiornamento", trappolaggio.getExtIdUtenteAggiornamento());
		mapParameterSource.addValue("idAnagrafica", trappolaggio.getIdAnagrafica());
		mapParameterSource.addValue("note", trappolaggio.getNote());
		mapParameterSource.addValue("idOrganismoNocivo", trappolaggio.getIdOrganismoNocivo());
		mapParameterSource.addValue("idTrappolaggio", trappolaggio.getIdTrappolaggio());


		try {
			logger.debug(UPDATE);
			namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, UPDATE,
					mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	

	public List<FotoDTO> findFotoByIdTrappolaggio(Integer idTrappolaggio) throws InternalUnexpectedException {
		final String THIS_METHOD = "findFotoByIdTrappolaggio";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_ID = "SELECT c.id_foto,c.foto,c.nome_file,c.data_foto,c.tag,c.note,c.latitudine,c.longitudine "
				+ " FROM iuf_r_trappolaggio_foto a, iuf_t_trappolaggio b," + " iuf_t_foto c "
				+ " WHERE b.id_trappolaggio=a.id_trappolaggio " + " AND a.id_foto=c.id_foto "
				+ " AND b.id_trappolaggio= :idTrappolaggio";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idTrappolaggio", idTrappolaggio);

		try {
			logger.debug(SELECT_BY_ID);
			List<FotoDTO> fotoArray = new ArrayList<FotoDTO>();
			List<FotoDTO> foto = queryForList(SELECT_BY_ID, mapParameterSource, FotoDTO.class);
			if (foto != null) {
				for (FotoDTO objFoto : foto) {
					FotoDTO modifyFoto = this.findFotoById(objFoto.getIdFoto());
          modifyFoto.setLongitudine(objFoto.getLongitudine());
          modifyFoto.setLatitudine(objFoto.getLatitudine());
					fotoArray.add(modifyFoto);
				}
			}
			return fotoArray;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_ID, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException {
		final String THIS_METHOD = "findById";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_ID = "select c.id_foto,c.nome_file,c.data_foto,c.tag,c.note,c.foto, "
				+ "c.ext_id_utente_aggiornamento " + "from iuf_t_foto c " + "where c.id_foto =:id";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("id", id);

		try {
			logger.debug(SELECT_BY_ID);
			FotoDTO foto = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, mapParameterSource,
					getRowMapperFoto());
			return foto;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_ID, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public List<OrganismoNocivoDTO> findOnByIdTrappola(Integer id) throws InternalUnexpectedException {
		final String THIS_METHOD = "findOnByIdTrappola";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_ID = "select A.NOME_LATINO,A.SIGLA,A.EURO from IUF_D_TRAPPOLA_ON t,IUF_D_ORGANISMO_NOCIVO A "
				+ "WHERE A.ID_ORGANISMO_NOCIVO=T.ID_ON " + "AND T.ID_TRAPPOLA= :id";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("id", id);

		try {
			logger.debug(SELECT_BY_ID);
			List<OrganismoNocivoDTO> lista = queryForList(SELECT_BY_ID, mapParameterSource, OrganismoNocivoDTO.class);
			return lista;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_ID, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
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

	public TrappolaDTO findTrappolaById(Integer idTrappola) throws InternalUnexpectedException {
		final String THIS_METHOD = "findTrappolaById";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_ID = "SELECT * FROM iuf_t_trappola WHERE id_trappola = :idTrappola";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idTrappola", idTrappola);

		try {
			logger.debug(SELECT_BY_ID);
			TrappolaDTO trappola = queryForObject(SELECT_BY_ID, mapParameterSource, TrappolaDTO.class);
			return trappola;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_ID, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public Trappola findTrappolaByCodice(String codice) throws InternalUnexpectedException {
		final String THIS_METHOD = "findTrappolaByCodice";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT = "SELECT t.id_trappola, \r\n" 
		    + "t.id_tipo_trappola,\r\n"
				+ "tt.tipologia_trappola descr_trappola,\r\n" 
		    + "t.latitudine,\r\n" 
				+ "t.longitudine,\r\n"
				+ "t.id_specie_veg,\r\n" 
				+ "s.nome_volgare specie,\r\n"
				+ "TO_CHAR(t.data_installazione,'DD-MM-YYYY') data_installazione,\r\n"
				+ "TO_CHAR(t.data_rimozione,'DD-MM-YYYY') data_rimozione,\r\n" 
				+ "t.ext_id_utente_aggiornamento,\r\n"
				+ "t.data_ultimo_aggiornamento,\r\n" 
				+ "t.codice_sfr,\r\n"
				+ "MAX(tr.id_organismo_nocivo) id_organismo_nocivo \r\n"
				+ " FROM iuf_t_trappola t, iuf_d_tipo_trappola tt, iuf_d_specie_vegetale s, iuf_t_trappolaggio tr \r\n"
				+ "WHERE t.id_tipo_trappola = tt.id_tipo_trappola \r\n"
				+ "AND t.id_specie_veg = s.id_specie_vegetale \r\n" 
				+ "AND t.id_trappola = tr.id_trappola \r\n"
				+ "AND codice_sfr = :codice \r\n" 
				+ "GROUP BY t.id_trappola,\r\n" 
				+ "       t.id_tipo_trappola,\r\n"
				+ "       tt.tipologia_trappola,\r\n" 
				+ "       t.latitudine,\r\n" 
				+ "       t.longitudine,\r\n"
				+ "       t.id_specie_veg,\r\n" 
				+ "       s.nome_volgare,\r\n"
				+ "       TO_CHAR(t.data_installazione, 'DD-MM-YYYY'),\r\n"
				+ "       TO_CHAR(t.data_rimozione, 'DD-MM-YYYY'),\r\n" 
				+ "       t.ext_id_utente_aggiornamento,\r\n"
				+ "       t.data_ultimo_aggiornamento,\r\n" 
				+ "       t.codice_sfr";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("codice", codice);

		try {
			logger.debug(SELECT);
			Trappola trappola = queryForObject(SELECT, mapParameterSource, Trappola.class);
			return trappola;
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, SELECT,
					mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public void removeTrappola(Integer idTrappola, Long idUtente) throws InternalUnexpectedException {
		final String THIS_METHOD = "removeTrappola";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String UPDATE = "UPDATE iuf_t_trappola " + "SET data_rimozione = :dataRimozione,"
				+ "ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento," + "data_ultimo_aggiornamento = SYSDATE "
				+ "WHERE id_trappola = :idTrappola";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idTrappola", idTrappola);
		mapParameterSource.addValue("ext_id_utente_aggiornamento", idUtente);

		try {
			logger.debug(UPDATE);
			namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, UPDATE,
					mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	public void eliminaTrappola(Integer idTrappola) throws InternalUnexpectedException {
		final String THIS_METHOD = "eliminaTrappola";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String DELETE = "DELETE FROM iuf_t_trappola WHERE id_trappola = :idTrappola";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("idTrappola", idTrappola);

		try {
			logger.debug(DELETE);
			namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null, DELETE,
					mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}

	private RowMapper<TrappolaggioDTO> getRowMapper() {
		return new RowMapper<TrappolaggioDTO>() {

			@Override
			public TrappolaggioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				TrappolaggioDTO trappolaggio = new TrappolaggioDTO();
				trappolaggio.setIdTrappolaggio(rs.getInt("id_trappolaggio"));
				trappolaggio.setIdRilevazione(rs.getInt("id_rilevazione"));
				trappolaggio.setIstatComune(rs.getString("istat_comune"));
				trappolaggio.setDescrComune(rs.getString("descr_comune"));
				//trappolaggio.setDataOraInizio(rs.getDate("data_ora_inizio"));
				trappolaggio.setDataOraInizio(rs.getTimestamp("data_ora_inizio"));
				trappolaggio.setDataOraFine(rs.getDate("data_ora_fine"));
				String foto = rs.getString("foto");
				if (foto.equals("S"))
					trappolaggio.setFoto(true);
				else
					trappolaggio.setFoto(false);
				trappolaggio.setIspettore(rs.getString("ispettore"));
				trappolaggio.setIdOperazione(rs.getInt("id_operazione"));
				trappolaggio.setDataTrappolaggio(rs.getDate("data_trappolaggio"));
				trappolaggio.setDescrTrappola(rs.getString("descr_trappola"));
				trappolaggio.setSpecie(rs.getString("specie"));
				trappolaggio.setIdAnagrafica(rs.getInt("id_anagrafica"));
				trappolaggio.setIdIspezioneVisiva(rs.getInt("id_ispezione_visiva"));
				trappolaggio.setIdMissione(rs.getLong("id_missione"));
				trappolaggio.setDescrOperazione(rs.getString("descr_operazione"));
				trappolaggio.setNote(rs.getString("note"));
				trappolaggio.setIdOrganismoNocivo(rs.getInt("id_organismo_nocivo"));
				TrappolaDTO trappola = new TrappolaDTO();
				trappola.setIdTrappola(rs.getInt("id_trappola"));
				trappola.setCodiceSfr(rs.getString("codice_sfr"));
				trappola.setDataInstallazione(rs.getDate("data_installazione"));
				trappola.setDataRimozione(rs.getDate("data_rimozione"));
				trappola.setIdSpecieVeg(rs.getInt("id_specie_veg"));
				trappola.setIdTipoTrappola(rs.getInt("id_tipo_trappola"));
				trappola.setLatitudine(rs.getDouble("latitudine"));
				trappola.setLongitudine(rs.getDouble("longitudine"));
				trappolaggio.setTrappola(trappola);
				return trappolaggio;
			}
		};
	}

	public List<TrappolaggioDTO> findByIdIspezione(Integer idIspezioneVisiva) throws InternalUnexpectedException {
		final String THIS_METHOD = "findByIdISpezione";
		if (logger.isDebugEnabled()) {
			logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
		}

		String SELECT_BY_FILTER = "SELECT a.id_trappolaggio,\r\n"
		                               + "a.id_rilevazione,\r\n"
		                               + "a.id_trappola,\r\n"
				                           + "a.istat_comune,\r\n"
		                               + "h.descrizione_comune descr_comune,\r\n"
				                           + "a.data_ora_inizio,\r\n"
				                           + "a.data_ora_fine,\r\n"
				                           + "e.tipologia_trappola AS descr_trappola,\r\n"
				                           + "d.genere_specie AS specie,\r\n"
				                           + "d.nome_volgare AS pianta,\r\n"
				                           + "(CASE WHEN(SELECT COUNT(*) FROM iuf_r_trappolaggio_foto d WHERE d.id_trappolaggio=a.id_trappolaggio)>0 THEN 'S' ELSE 'N' END) AS FOTO,\r\n"
				                           + "g.cognome ||' '|| g.nome AS ispettore,\r\n"
				                           + "a.id_anagrafica,\r\n"
				                           + "a.id_operazione,\r\n"
				                           + "i.desc_operazione_trappola descr_operazione,\r\n"
				                           + "a.data_trappolaggio,\r\n"
				                           + "a.id_ispezione_visiva,\r\n"
				                           + "b.id_missione,\r\n"
				                           + "f.data_installazione,\r\n"
				                           + "f.data_rimozione,\r\n"
				                           + "f.latitudine,\r\n"
				                           + "f.longitudine,\r\n"
				                           + "f.codice_sfr,\r\n"
				                           + "f.id_specie_veg,\r\n"
				                           + "f.id_tipo_trappola,\r\n"
				                           + "a.id_organismo_nocivo,\r\n"
				                           + "a.note \r\n"
				                     + "FROM iuf_t_trappolaggio a, iuf_t_missione b,\r\n"
				                         + "iuf_t_rilevazione c, iuf_d_specie_vegetale d,\r\n"
				                         + "iuf_d_tipo_trappola e, iuf_t_trappola f,\r\n"
				                         + "iuf_t_anagrafica g, smrgaa_v_dati_amministrativi h,\r\n"
				                         + "iuf_d_operazione_trappola i \r\n"
				                     + "WHERE a.id_rilevazione = c.id_rilevazione \r\n"
				                     + "AND c.id_missione = b.id_missione \r\n"
				                     + "AND f.id_trappola = a.id_trappola \r\n"
				                     + "AND f.id_specie_veg = d.id_specie_vegetale \r\n"
				                     + "AND f.id_tipo_trappola = e.id_tipo_trappola \r\n"
				                     //+ "AND a.id_rilevazione = NVL(:idRilevazione,a.id_rilevazione) \r\n"
				                     //+ "AND a.id_trappola = NVL(:idTrappola,a.id_trappola) \r\n"
				                     + "AND a.id_anagrafica = g.id_anagrafica \r\n"
				                     + "AND a.istat_comune = h.istat_comune (+) \r\n"
				                     + "AND a.id_operazione = i.id_operazione_trappola (+) \r\n"
				                     //+ "AND TRUNC(a.data_trappolaggio) BETWEEN TO_DATE(NVL(:dallaData,'01/01/2000'),'DD/MM/YYYY') AND TO_DATE(NVL(:allaData,'31/12/2100'),'DD/MM/YYYY') \r\n"
				                     //+ "AND a.id_trappolaggio = NVL(:idTrappolaggio,a.id_trappolaggio) "
				                     + "AND a.id_ispezione_visiva = :idIspezioneVisiva \r\n";

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		//mapParameterSource.addValue("idRilevazione", null, Types.NULL);
		//mapParameterSource.addValue("idTrappola", null, Types.NULL);
		//mapParameterSource.addValue("idTrappolaggio", null, Types.NULL);
		mapParameterSource.addValue("idIspezioneVisiva", idIspezioneVisiva);
		//mapParameterSource.addValue("dallaData", "");
		//mapParameterSource.addValue("allaData", "");

		SELECT_BY_FILTER += " ORDER BY a.id_trappolaggio";

		try {
			logger.debug(SELECT_BY_FILTER);
			List<TrappolaggioDTO> list = namedParameterJdbcTemplate.query(SELECT_BY_FILTER, mapParameterSource, new RowMapper<TrappolaggioDTO>()
      {
			  @Override
		    public TrappolaggioDTO mapRow(ResultSet rs, int rownumber) throws SQLException {  
			  TrappolaggioDTO trappolaggio = new TrappolaggioDTO();
			  trappolaggio.setIdTrappolaggio(rs.getInt("id_trappolaggio"));
			  trappolaggio.setIdRilevazione(rs.getInt("id_rilevazione"));
			  trappolaggio.setIstatComune(rs.getString("istat_comune"));
        trappolaggio.setDescrComune(rs.getString("descr_comune"));
        trappolaggio.setDataOraInizio(rs.getTimestamp("data_ora_inizio"));
        trappolaggio.setDataOraFine(rs.getTimestamp("data_ora_fine"));
        trappolaggio.setDescrTrappola(rs.getString("descr_trappola"));
        trappolaggio.setSpecie(rs.getString("specie"));
        trappolaggio.setPianta(rs.getString("pianta"));
        String foto = rs.getString("foto");
        if (foto.equals("S"))
          trappolaggio.setFoto(true);
        else
          trappolaggio.setFoto(false);

        trappolaggio.setIspettore(rs.getString("ispettore"));
        trappolaggio.setIdAnagrafica(rs.getInt("id_anagrafica"));
        trappolaggio.setIdOperazione(rs.getInt("id_operazione"));
        trappolaggio.setDescrOperazione(rs.getString("descr_operazione"));
        trappolaggio.setDataTrappolaggio(rs.getTimestamp("data_trappolaggio"));
        trappolaggio.setIdIspezioneVisiva(rs.getInt("id_ispezione_visiva"));
        trappolaggio.setIdMissione(rs.getLong("id_missione"));
        TrappolaDTO trappola = new TrappolaDTO();
        trappola.setDataInstallazione(rs.getTimestamp("data_installazione"));
        trappola.setDataRimozione(rs.getTimestamp("data_rimozione"));
        trappola.setLatitudine(rs.getDouble("latitudine"));
        trappola.setLongitudine(rs.getDouble("longitudine"));
        trappola.setCodiceSfr(rs.getString("codice_sfr"));
        trappola.setIdSpecieVeg(rs.getInt("id_specie_veg"));
        trappola.setIdTipoTrappola(rs.getInt("id_tipo_trappola"));
        trappola.setIdTrappola(rs.getInt("id_trappola"));
        trappolaggio.setTrappola(trappola);
        trappolaggio.setIdOrganismoNocivo(rs.getInt("id_organismo_nocivo"));
        trappolaggio.setNote(rs.getString("note"));
			  return trappolaggio;
      }});
			// List<TrappolaggioDTO> list =
			// namedParameterJdbcTemplate.query(SELECT_BY_FILTER, mapParameterSource,
			// getRowMapper());
			return list;
			
		} catch (Throwable t) {
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {}, null,
					SELECT_BY_FILTER, mapParameterSource);
			logInternalUnexpectedException(e, THIS_METHOD);
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(THIS_METHOD + " END.");
			}
		}
	}
	
	  public List<OperazioneTrappolaDTO> findTipoOperazione() throws InternalUnexpectedException
	  {
	    final String THIS_METHOD = "findTipoOperazione";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
	    }
	    String SELECT = " SELECT p.* FROM IUF_D_OPERAZIONE_TRAPPOLA p";

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    List<OperazioneTrappolaDTO> tipoOperazione;
	    try
	    {
	      logger.debug(SELECT);
	      tipoOperazione = queryForList(SELECT, mapParameterSource, OperazioneTrappolaDTO.class);
	      return tipoOperazione;
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
	  
	  public List<OperazioneTrappolaDTO> findByIdMultipli(String idMultipli) throws InternalUnexpectedException
	  {
	    final String THIS_METHOD = "findById";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
	    }

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	  //  mapParameterSource.addValue("idMultipli", idMultipli, Types.INTEGER);
	    String select = String.format(SELECT_ALL_ID_MULTIPLI,idMultipli);
	    try
	    {
	      List<OperazioneTrappolaDTO> list = queryForList(select, mapParameterSource, OperazioneTrappolaDTO.class);
	      return list;
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	              {}, null, SELECT_ALL_ID_MULTIPLI, mapParameterSource);
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
