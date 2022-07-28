package it.csi.iuffi.iuffiweb.integration;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.commons.validator.GenericValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import it.csi.iuffi.iuffiweb.dto.AmmCompetenzaDTO;
import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.AzioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.OggettoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.QuadroDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.ControlloDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.AziendaAgricola;
import it.csi.papua.papuaserv.dto.gestioneutenti.EnteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class NuovoProcedimentoDAO extends BaseDAO
{

  private static final String THIS_CLASS = NuovoProcedimentoDAO.class
      .getSimpleName();

  public NuovoProcedimentoDAO()
  {
  }

  public BandoDTO getDettaglioBandoByIdBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDettaglioBandoByIdBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                   \n"
          + "  A.ID_BANDO,                                                             \n"
          + "  A.DATA_INIZIO,                                                          \n"
          + "  A.DATA_FINE,                                                            \n"
          + "  A.DENOMINAZIONE,                                                        \n"
          + "  A.ISTRUZIONE_SQL_FILTRO,                                                \n"
          + "  A.DESCRIZIONE_FILTRO,                                                   \n"
          + "  A.ANNO_CAMPAGNA,                                                        \n"
          + "  A.REFERENTE_BANDO,                                                      \n"
          + "  A.EMAIL_REFERENTE_BANDO,                                                \n"
          + "  A.FLAG_RIBASSO_INTERVENTI,                                              \n"
          + "  A.FLAG_DOMANDA_MULTIPLA,                                           	   \n"
          + "  E.ID_LEGAME_GRUPPO_OGGETTO,                                             \n"
          + "  D.ID_BANDO_OGGETTO			                                           \n"
          + " FROM                                                                     \n"
          + "  IUF_D_BANDO A,                                                          \n"
          + "  IUF_R_BANDO_OGGETTO D,                                                  \n"
          + "  IUF_R_LEGAME_GRUPPO_OGGETTO E,                                          \n"
          + "  IUF_D_GRUPPO_OGGETTO F                                                  \n"
          + " WHERE                                                                    \n"
          + "  D.ID_BANDO = A.ID_BANDO                                             	   \n"
          + "  AND D.ID_LEGAME_GRUPPO_OGGETTO = E.ID_LEGAME_GRUPPO_OGGETTO             \n"
          + "  AND F.ID_GRUPPO_OGGETTO = E.ID_GRUPPO_OGGETTO                           \n"
          + "  AND A.FLAG_MASTER <> 'S'                                                \n"
          + "  AND SYSDATE BETWEEN A.DATA_INIZIO AND NVL(A.DATA_FINE,SYSDATE)          \n"
          + "  AND D.FLAG_ATTIVO = 'S'                                                 \n"
          + "  AND A.ID_BANDO = :ID_BANDO                                                 \n"
          + " ORDER BY A.DATA_FINE, A.DENOMINAZIONE, F.ORDINE, E.ORDINE \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BandoDTO>()
          {
            @Override
            public BandoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BandoDTO bandoDTO = null;

              if (rs.next())
              {
                bandoDTO = new BandoDTO();
                bandoDTO.setIdBando(rs.getLong("ID_BANDO"));
                bandoDTO.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                bandoDTO.setIdLegameGruppoOggetto(
                    rs.getLong("ID_LEGAME_GRUPPO_OGGETTO"));
                bandoDTO.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                bandoDTO.setDataFine(rs.getTimestamp("DATA_FINE"));
                bandoDTO.setDenominazione(rs.getString("DENOMINAZIONE"));
                bandoDTO.setReferenteBando(rs.getString("REFERENTE_BANDO"));
                bandoDTO.setEmailReferenteBando(
                    rs.getString("EMAIL_REFERENTE_BANDO"));
                bandoDTO.setFlagRibassoInterventi(
                    rs.getString("FLAG_RIBASSO_INTERVENTI"));
                bandoDTO.setFlagDomandaMultipla(
                    rs.getString("FLAG_DOMANDA_MULTIPLA"));
                bandoDTO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                bandoDTO
                    .setDescrizioneFiltro(rs.getString("DESCRIZIONE_FILTRO"));
                bandoDTO.setIstruzioneSqlFiltro(
                    rs.getString("ISTRUZIONE_SQL_FILTRO"));
                bandoDTO.setLivelli(new ArrayList<LivelloDTO>());

              }
              return bandoDTO;
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

  public List<BandoDTO> getElencoBandiAttivi(int idProcedimentoAgricolo, UtenteAbilitazioni utenteAbilitazioni)
	  throws InternalUnexpectedException {
      String THIS_METHOD = "getElencoBandiAttivi";
      String SELECT = "";
      if (logger.isDebugEnabled()) {
	  logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
      }

      try {
	  boolean hasRuoloProfessionista = false;

	  if (utenteAbilitazioni.getRuolo().getIsList() != null
		  && Arrays.asList(utenteAbilitazioni.getRuolo().getIsList()).contains("isUtenteProfessionista")) {
	      hasRuoloProfessionista = true;
	  }
	  SELECT = " SELECT                                                                   \n"
		  + "  A.ID_BANDO,                                                             \n"
		  + "  A.DATA_INIZIO,                                                          \n"
		  + "  A.DATA_FINE,                                                            \n"
		  + "  A.DENOMINAZIONE,                                                        \n"
		  + "  A.ISTRUZIONE_SQL_FILTRO,                                                \n"
		  + "  A.DESCRIZIONE_FILTRO,                                                   \n"
		  + "  A.ANNO_CAMPAGNA,                                                        \n"
		  + "  A.REFERENTE_BANDO,                                                      \n"
		  + "  A.EMAIL_REFERENTE_BANDO,                                                \n"
		  + "  A.FLAG_RIBASSO_INTERVENTI,                                              \n"
		  + "  C.ID_LIVELLO,                                                           \n"
		  + "  C.CODICE,                                                               \n"
		  + "  C.DESCRIZIONE,                                                          \n"
		  + "  E.ID_LEGAME_GRUPPO_OGGETTO,                                             \n"
		  + "  EC.ID_EVENTO_CALAMITOSO,                                                \n"
		  + "  EC.DESCRIZIONE DESC_EVENTO_CALAMITOSO,                                  \n"
		  + "  EC.DATA_EVENTO,                                                         \n"
		  + "  D.ID_BANDO_OGGETTO			                                                 \n"
		  + " FROM                                                                     \n"
		  + "  IUF_D_BANDO A,                                                          \n"
		  + "  IUF_D_EVENTO_CALAMITOSO EC,                                             \n"
		  + "  IUF_R_LIVELLO_BANDO B,                                                  \n"
		  + "  IUF_D_LIVELLO C,                                                        \n"
		  + "  IUF_R_BANDO_OGGETTO D,                                                  \n"
		  + "  IUF_R_LEGAME_GRUPPO_OGGETTO E,                                          \n";
	  if (hasRuoloProfessionista) {
	      SELECT += " SMRGAA.SMRGAA_V_PROFESSIONISTI S,PAPUA.PAPUA_V_RUOLI_ANAG VRA, \n";
	  }
	  SELECT += "  IUF_D_GRUPPO_OGGETTO F                                                  \n"
		  + " WHERE                                                                    \n"
		  + "  A.ID_BANDO = B.ID_BANDO                                                 \n"
		  + "  AND A.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO              \n"
		  + "  AND B.ID_LIVELLO = C.ID_LIVELLO                                         \n"
		  + "  AND D.ID_BANDO = A.ID_BANDO                                             \n"
		  + "  AND D.ID_LEGAME_GRUPPO_OGGETTO = E.ID_LEGAME_GRUPPO_OGGETTO             \n"
		  + "  AND F.ID_GRUPPO_OGGETTO = E.ID_GRUPPO_OGGETTO                           \n"
		  + "  AND A.FLAG_MASTER <> 'S'                                                \n"
		  + "  AND SYSDATE BETWEEN A.DATA_INIZIO AND NVL(A.DATA_FINE,SYSDATE)          \n"
		  + "  AND D.FLAG_ATTIVO = 'S'                                                 \n"
		  + "  AND EC.ID_EVENTO_CALAMITOSO (+)= A.ID_EVENTO_CALAMITOSO                 \n";
	  if (hasRuoloProfessionista) {
	      SELECT += " AND VRA.ID_PROCEDIMENTO = S.ID_PROCEDIMENTO					\n"
		      + " AND S.ID_RUOLO = VRA.EXT_ID_RUOLO_ANAG									\n"
		      + " AND S.CODICE_FISCALE = :CODICE_FISCALE                       			\n"
		      + " AND S.ID_PROCEDIMENTO=:EXT_ID_PROCEDIMENTO				   	    		\n"
		      + " AND VRA.CODICE_RUOLO_PAPUA = :CODICE_RUOLO_PAPUA						\n"
		      + " AND SYSDATE BETWEEN S.DATA_INIZIO_VALIDITA_PROF_AZ AND NVL(DATA_FINE_VALIDITA_PROF_AZ,SYSDATE) 				\n"
		      + " AND A.ID_BANDO = 																								\n"
		      + "        CASE 																									\n"
		      + "          WHEN (																								\n"
		      + "              SELECT COUNT(*)																\n"
		      + "                FROM SMRGAA.SMRGAA_V_PROFESSIONISTI  															\n"
		      + "                  WHERE  ID_PROCEDIMENTO=:EXT_ID_PROCEDIMENTO												    \n"
		      + "                  AND  SYSDATE BETWEEN DATA_INIZIO_VALIDITA_PROF_AZ AND NVL(DATA_FINE_VALIDITA_PROF_AZ,SYSDATE) \n"
		      + "                  AND CODICE_FISCALE = :CODICE_FISCALE AND EXT_BANDO_TIPO_PRATICA IS NULL)					\n"
		      + "          = 0 																									\n"
		      + "          THEN A.ID_BANDO ELSE TO_NUMBER(S.EXT_BANDO_TIPO_PRATICA) END 											\n";
	  }
	  SELECT += " ORDER BY A.DATA_FINE, A.DENOMINAZIONE, C.ORDINAMENTO, F.ORDINE, E.ORDINE \n";

	  MapSqlParameterSource parameterSource = new MapSqlParameterSource();
	  parameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo, Types.NUMERIC);

	  if (hasRuoloProfessionista) {
	      parameterSource.addValue("CODICE_FISCALE", utenteAbilitazioni.getCodiceFiscale(), Types.VARCHAR);
	      parameterSource.addValue("EXT_ID_PROCEDIMENTO", idProcedimentoAgricolo, Types.NUMERIC);
	      parameterSource.addValue("CODICE_RUOLO_PAPUA", utenteAbilitazioni.getRuolo().getCodice(),
		      Types.VARCHAR);
	  }

	  return namedParameterJdbcTemplate.query(SELECT, parameterSource, new ResultSetExtractor<List<BandoDTO>>() {
	      @Override
	      public List<BandoDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
		  ArrayList<BandoDTO> list = new ArrayList<BandoDTO>();
		  BandoDTO bandoDTO = null;
		  Long lastIdBando = null;
		  long idBando = 0;
		  Long lastIdLivello = null;
		  long idLivello = 0;

		  while (rs.next()) {
		      idBando = rs.getLong("ID_BANDO");
		      idLivello = rs.getLong("ID_LIVELLO");

		      if (lastIdBando == null || lastIdBando != idBando) {
			  bandoDTO = new BandoDTO();
			  bandoDTO.setIdBando(idBando);
			  bandoDTO.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
			  bandoDTO.setIdLegameGruppoOggetto(rs.getLong("ID_LEGAME_GRUPPO_OGGETTO"));
			  bandoDTO.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
			  bandoDTO.setDataFine(rs.getTimestamp("DATA_FINE"));
			  bandoDTO.setDenominazione(rs.getString("DENOMINAZIONE"));
			  bandoDTO.setReferenteBando(rs.getString("REFERENTE_BANDO"));
			  bandoDTO.setEmailReferenteBando(rs.getString("EMAIL_REFERENTE_BANDO"));
			  bandoDTO.setFlagRibassoInterventi(rs.getString("FLAG_RIBASSO_INTERVENTI"));
			  bandoDTO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
			  bandoDTO.setDescrizioneFiltro(rs.getString("DESCRIZIONE_FILTRO"));
			  bandoDTO.setIstruzioneSqlFiltro(rs.getString("ISTRUZIONE_SQL_FILTRO"));
			  bandoDTO.setIdEventoCalamitoso(rs.getLong("ID_EVENTO_CALAMITOSO"));
			  bandoDTO.setDataEvento(rs.getDate("DATA_EVENTO"));
			  bandoDTO.setDescEventoCalamitoso(rs.getString("DESC_EVENTO_CALAMITOSO"));
			  bandoDTO.setLivelli(new ArrayList<LivelloDTO>());
			  try {
			      bandoDTO.setAllegati(getElencoAllegati(idBando));
			  } catch (InternalUnexpectedException e) {
			      throw new SQLException(e);
			  }
			  list.add(bandoDTO);
			  lastIdBando = idBando;
		      }

		      if (lastIdLivello == null || lastIdLivello != idLivello) {
			  LivelloDTO livelloDTO = new LivelloDTO();
			  livelloDTO.setCodice(rs.getString("CODICE"));
			  livelloDTO.setDescrizione(rs.getString("DESCRIZIONE"));
			  livelloDTO.setIdLivello(rs.getLong("ID_LIVELLO"));
			  bandoDTO.getLivelli().add(livelloDTO);
			  lastIdLivello = idLivello;
		      }
		  }
		  return list.isEmpty() ? null : list;
	      }
	  });
      } catch (Throwable t) {
	  InternalUnexpectedException e = new InternalUnexpectedException(t, SELECT);
	  logInternalUnexpectedException(e, "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
	  throw e;
      } finally {
	  if (logger.isDebugEnabled()) {
	      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
	  }
      }
  }

  public List<FileAllegatoDTO> getElencoAllegati(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoAllegati";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 							\n"
          + " 	ID_ALLEGATI_BANDO,				\n"
          + " 	DESCRIZIONE,					\n"
          + " 	NOME_FILE,						\n"
          + " 	ID_BANDO,						\n"
          + " 	ORDINE							\n"
          + " FROM 								\n"
          + "	IUF_D_ALLEGATI_BANDO		    \n"
          + " WHERE 							\n"
          + "   ID_BANDO = :ID_BANDO          	\n"
          + "   AND FLAG_VISIBILE = 'S'       	\n"
          + "ORDER BY ORDINE, NOME_FILE       	\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return queryForList(SELECT, parameterSource, FileAllegatoDTO.class);
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

  public List<OggettoDTO> getOggettiByIdBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getOggettiByIdBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                             \n"
          + "   B.CODICE AS CODICE_OGGETTO,                      \n"
          + "   B.DESCRIZIONE AS OGGETTO,                        \n"
          + "   A.DATA_INIZIO,                        			 \n"
          + "   A.DATA_FINE,                        			 \n"
          + "   F.CODICE AS CODICE_QUADRO,                       \n"
          + "   F.DESCRIZIONE AS QUADRO,                         \n"
          + "   H.ID_AZIONE,                                     \n"
          + "   G.EXT_COD_MACRO_CDU AS CU_REF,                   \n"
          + "   G.NOME_DOCUMENTO_CDU AS CU_DOC,                  \n"
          + "   H.LABEL AS AZIONE,                               \n"
          + "   H.TIPO_AZIONE                                    \n"
          + " FROM                                               \n"
          + "   IUF_R_BANDO_OGGETTO A,                           \n"
          + "   IUF_D_OGGETTO B,                                 \n"
          + "   IUF_R_LEGAME_GRUPPO_OGGETTO C,                 	 \n"
          + "   IUF_D_GRUPPO_OGGETTO D,                        	 \n"
          + "   IUF_R_QUADRO_OGGETTO E,                          \n"
          + "   IUF_D_QUADRO F,                                  \n"
          + "   IUF_R_QUADRO_OGGETTO_AZIONE G,                   \n"
          + "   IUF_D_AZIONE H                                   \n"
          + " WHERE                                              \n"
          + "   A.ID_BANDO = :ID_BANDO                           \n"
          + "   A.FLAG_ATTIVO = 'S'		                         \n"
          + "   AND A.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "   AND B.ID_OGGETTO = C.ID_OGGETTO                  \n"
          + "   AND C.ID_GRUPPO_OGGETTO =D.ID_GRUPPO_OGGETTO   	 \n"
          + "   AND E.ID_OGGETTO(+) = B.ID_OGGETTO               \n"
          + "   AND F.ID_QUADRO (+)= E.ID_QUADRO                 \n"
          + "   AND G.ID_QUADRO_OGGETTO(+) = E.ID_QUADRO_OGGETTO \n"
          + "   AND H.ID_AZIONE(+) = G.ID_AZIONE                 \n"
          + " ORDER BY B.ID_OGGETTO, E.ORDINE , G.ORDINE         \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<OggettoDTO>>()
          {
            @Override
            public List<OggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<OggettoDTO> list = new ArrayList<OggettoDTO>();
              OggettoDTO oggettoDTO = null;
              QuadroDTO quadroDTO = null;
              AzioneDTO azioneDTO = null;
              String lastCodOggetto = null;
              String codOggetto = "";
              String lastCodQuadro = null;
              String codQuadro = "";

              while (rs.next())
              {
                codOggetto = rs.getString("CODICE_OGGETTO");
                if (lastCodOggetto == null
                    || !lastCodOggetto.equals(codOggetto))
                {
                  oggettoDTO = new OggettoDTO();
                  oggettoDTO.setCodice(codOggetto);
                  oggettoDTO.setDescrizione(rs.getString("OGGETTO"));
                  oggettoDTO.setDataInizio(rs.getDate("DATA_INIZIO"));
                  oggettoDTO.setDataFine(rs.getDate("DATA_FINE"));
                  oggettoDTO.setQuadri(new ArrayList<QuadroDTO>());
                  list.add(oggettoDTO);
                  lastCodOggetto = codOggetto;
                }

                codQuadro = rs.getString("CODICE_QUADRO");
                if (lastCodQuadro == null || !lastCodQuadro.equals(codQuadro))
                {
                  quadroDTO = new QuadroDTO();
                  quadroDTO.setCodice(codQuadro);
                  quadroDTO.setDescrizione(rs.getString("QUADRO"));
                  quadroDTO.setAzioni(new ArrayList<AzioneDTO>());
                  oggettoDTO.getQuadri().add(quadroDTO);
                  lastCodQuadro = codQuadro;
                }

                azioneDTO = new AzioneDTO();
                azioneDTO.setIdAzione(rs.getLong("ID_AZIONE"));
                azioneDTO.setCodiceCU(rs.getString("CU_REF"));
                azioneDTO.setDocumentoCU(rs.getString("CU_DOC"));
                azioneDTO.setLabel(rs.getString("AZIONE"));
                quadroDTO.getAzioni().add(azioneDTO);
              }
              return list.isEmpty() ? null : list;
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

  public List<Long> getAziendeByCUAA(String cuaa, String partitaIva, String denominazione, UtenteAbilitazioni utenteAbilitazioni) throws InternalUnexpectedException {
    String THIS_METHOD = "getAziendeByCUAA";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled()) {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try 
    {
      SELECT.append( " SELECT                        \n"
            + "   A.ID_AZIENDA                       \n"
            + " FROM                                 \n"
            + " SMRGAA_V_DATI_ANAGRAFICI A           \n");
      if(utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" , SMRGAA_V_DATI_DELEGA B ");
      }

      SELECT.append(" WHERE                           \n"
              + "   A.DATA_FINE_VALIDITA IS NULL      \n"
              + "   AND A.DATA_CESSAZIONE IS NULL     \n");
      if(cuaa!=null && !"".equals(cuaa))
        SELECT.append(" AND A.CUAA = :CUAA            \n");
      if(partitaIva!=null && !"".equals(partitaIva))
        SELECT.append(" AND A.PARTITA_IVA = :PARTITA_IVA                          \n");
      if(denominazione!=null && !"".equals(denominazione))
        SELECT.append(" AND UPPER(A.DENOMINAZIONE) LIKE UPPER(:DENOMINAZIONE)     \n");
      
      
      if(utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" AND A.ID_AZIENDA = B.ID_AZIENDA                     \n"
                + " AND B.DATA_FINE_VALIDITA IS NULL                        \n"
                + " AND ( B.ID_INTERMEDIARIO_ZONA = :ID_INTERMEDIARIO       \n"
                + "    OR B.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO       \n"
                + "      OR B.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO )   \n");
      }
      else if(isBeneficiario(utenteAbilitazioni))
      {
        Vector<Long> aziende = new Vector<Long>();
        for(EnteLogin ente :utenteAbilitazioni.getEntiAbilitati())
        {
          if (ente.getAziendaAgricola()!=null)
          {
            aziende.add(ente.getAziendaAgricola().getIdAzienda());
          }
        }
        SELECT.append(" "+getInCondition("A.ID_AZIENDA", aziende));
      }
      
      SELECT.append(" ORDER BY A.DENOMINAZIONE ");
      
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("CUAA", cuaa, Types.VARCHAR);
      parameterSource.addValue("PARTITA_IVA", partitaIva, Types.VARCHAR);
      parameterSource.addValue("DENOMINAZIONE", denominazione, Types.VARCHAR);
      
      if(utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        parameterSource.addValue("ID_INTERMEDIARIO", utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario(), Types.NUMERIC);  
      }
      
      return namedParameterJdbcTemplate.query(SELECT.toString(), parameterSource, new ResultSetExtractor<List<Long>>()
      {
        @Override
        public List<Long> extractData(ResultSet rs) throws SQLException, DataAccessException
        {
          ArrayList<Long> list = new ArrayList<Long>();
          
          while (rs.next())
              {
            list.add(rs.getLong("ID_AZIENDA"));
              }
          return list.isEmpty() ? null : list;
        }
      });
    } catch (Throwable t) {
      InternalUnexpectedException e = new InternalUnexpectedException(t,SELECT.toString());
      logInternalUnexpectedException(e, "[" + THIS_CLASS + ":: "+ THIS_METHOD + "]");
      throw e;
    } finally {
      if (logger.isDebugEnabled()) {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
      }
    }
  }

  public List<Long> getAziendeByIdBando(long idBando,
      UtenteAbilitazioni utenteAbilitazioni) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAziendeByIdBando";
    StringBuffer SELECT = new StringBuffer("");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      String sql = getSQLByIdBando(idBando);

      SELECT.append(" SELECT 								\n"
          + " 	A1.ID_AZIENDA						\n"
          + " FROM 									\n"
          + "	SMRGAA_V_DATI_ANAGRAFICI  A1        	\n");

      if (!GenericValidator.isBlankOrNull(sql))
      {
        SELECT.append("	, ( " + sql + " ) SUB_SQL			\n");
      }

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" , SMRGAA_V_DATI_DELEGA C ");
      }

      SELECT.append(" WHERE 								\n"
          + " 	AND A1.DATA_CESSAZIONE IS NULL     \n"
          + " 	A1.DATA_FINE_VALIDITA IS NULL    	\n");

      if (!GenericValidator.isBlankOrNull(sql))
      {
        SELECT.append("   AND A1.ID_AZIENDA= SUB_SQL.ID_AZIENDA  	\n");
      }

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" AND A1.ID_AZIENDA = C.ID_AZIENDA						\n"
            + " AND C.DATA_FINE_VALIDITA IS NULL								\n"
            + " AND ( C.ID_INTERMEDIARIO_ZONA = :ID_INTERMEDIARIO  	\n"
            + "		 OR C.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO \n"
            + "      OR C.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO ) \n");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          Vector<Long> aziende = new Vector<Long>();
          for (EnteLogin ente : utenteAbilitazioni.getEntiAbilitati())
          {
            final AziendaAgricola aziendaAgricola = ente.getAziendaAgricola();
            if (aziendaAgricola != null)
            {
              aziende.add(aziendaAgricola.getIdAzienda());
            }
          }
          SELECT.append(" " + getInCondition("A1.ID_AZIENDA", aziende));
        }

      SELECT.append(" ORDER BY A1.DENOMINAZIONE ");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        parameterSource.addValue("ID_INTERMEDIARIO", utenteAbilitazioni
            .getEnteAppartenenza().getIntermediario().getIdIntermediario(),
            Types.NUMERIC);
      }

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<Long>>()
          {
            @Override
            public List<Long> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<Long> list = new ArrayList<Long>();
              while (rs.next())
              {
                list.add(rs.getLong("ID_AZIENDA"));
              }
              return list.isEmpty() ? null : list;
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

  private String getSQLByIdBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getSQLByIdBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 							\n"
          + " 	ISTRUZIONE_SQL_FILTRO  		\n"
          + " FROM 								\n"
          + "	IUF_D_BANDO				        \n"
          + " WHERE 							\n"
          + "   ID_BANDO = :ID_BANDO          	\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              String sql = "";
              while (rs.next())
              {
                sql = rs.getString("ISTRUZIONE_SQL_FILTRO");
              }
              return sql;
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

  public List<AziendaDTO> getDettaglioAziendeByIdBando(long idBando, UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDettaglioAziendeByIdBando";
    StringBuffer SELECT = new StringBuffer("");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
	boolean hasRuoloProfessionista = false;		

	String sql =  getSQLByIdBando(idBando);
	if(utenteAbilitazioni.getRuolo().getIsList()!=null && Arrays.asList(utenteAbilitazioni.getRuolo().getIsList()).contains("isUtenteProfessionista"))
	{
		hasRuoloProfessionista = true;
	}
	
      SELECT.append(" SELECT 								                           \n"
          + "    A1.ID_AZIENDA,                                                \n"
          + "    A1.CUAA,                                                      \n"
          + "    NVL(A1.PARTITA_IVA,'') AS PARTITA_IVA,                        \n"
          + "    A1.DENOMINAZIONE,                                             \n"
          + "    A1.DENOMINAZIONE || ' ' || A1.INTESTAZIONE_PARTITA_IVA AS DENOMINAZIONE_INTESTAZ,                                             \n"
          + "    A1.ID_FORMA_GIURIDICA,                                        \n"
          + "    A1.INDIRIZZO_SEDE_LEGALE,                                     \n"
          + "    A2.DESCRIZIONE_COMUNE,                                        \n"
          + "    PROC.ID_PROCEDIMENTO,                                         \n"
          + "    PROC.ID_STATO_OGGETTO,                                        \n"
          + "    PROC.IDENTIFICATIVO,                                          \n"
          + "    PROC.CONT1,                                    	           \n"
          + "    SC2.COGNOME || ' ' || SC2.NOME AS CONT2,                      \n"
          + "    PROC.DESCRIZIONE,                                             \n"
          + "    NVL(PROC.ID_PROCEDIMENTO_OGGETTO,'') AS PROCEDIMENTO_OGGETTO, \n"
          + "    NVL(PROC.DATA_FINE,'') AS DATA_FINE                           \n"
          + " FROM 									                           \n"
          + "   (SELECT                                                        \n"
          + "       D1.EXT_ID_AZIENDA,                                         \n"
          + "       B1.ID_PROCEDIMENTO,                                        \n"
          + "       B1.IDENTIFICATIVO,                                         \n"
          + "    	SC1.COGNOME || ' ' || SC1.NOME AS CONT1, 				   \n"
          + "       C1.ID_PROCEDIMENTO_OGGETTO,                                \n"
          + "       O1.ID_STATO_OGGETTO,    	                               \n"
          + "       O1.DESCRIZIONE,				                               \n"
          + "       C1.DATA_FINE                                               \n"
          + "     FROM                                                         \n"
          + "       IUF_T_PROCEDIMENTO B1,                                     \n"
          + "       IUF_T_PROCEDIMENTO_OGGETTO C1,                             \n"
          + "       IUF_D_STATO_OGGETTO O1,                            		   \n"
          + "       SMRGAA_V_SOGGETTI_COLLEGATI SC1,                          \n"
          + "       IUF_T_PROCEDIMENTO_AZIENDA D1                              \n"
          + "     WHERE                                                        \n"
          + "       B1.ID_PROCEDIMENTO = C1.ID_PROCEDIMENTO                    \n"
          + "       AND D1.ID_PROCEDIMENTO = B1.ID_PROCEDIMENTO                \n"
          + "       AND B1.ID_STATO_OGGETTO = O1.ID_STATO_OGGETTO              \n"
          + " AND SC1.ID_CONTITOLARE(+) = C1.EXT_ID_CONTITOLARE          		\n"
          + " AND ( SC1.ID_AZIENDA is null OR SC1.ID_AZIENDA = D1.EXT_ID_AZIENDA) 	\n"
          + "  AND SC1.DATA_FINE_VALIDITA IS NULL    \n"
          + "       AND B1.ID_STATO_OGGETTO < 90                               \n"
          + "       AND D1.DATA_FINE IS NULL                                   \n"
          + "       AND B1.ID_BANDO = :ID_BANDO ) PROC,                        \n"
          + "   SMRGAA_V_DATI_AMMINISTRATIVI A2,                        	   \n"
          + "    SMRGAA_V_SOGGETTI_COLLEGATI SC2,                        	   \n"
          + "	SMRGAA_V_DATI_ANAGRAFICI  A1        	\n");

      if (!GenericValidator.isBlankOrNull(sql))
      {
        SELECT.append("	, ( " + sql + " ) SUB_SQL			\n");
      }

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" , SMRGAA_V_DATI_DELEGA C ");
      }
      
	if(hasRuoloProfessionista)
	{
	  SELECT.append(" ,SMRGAA.SMRGAA_V_PROFESSIONISTI S,PAPUA.PAPUA_V_RUOLI_ANAG VRA \n");
	}

      SELECT.append(" WHERE 								    \n"
          + "  PROC.EXT_ID_AZIENDA(+) = A1.ID_AZIENDA \n"
          + "  AND A2.ISTAT_COMUNE = A1.ISTAT_COMUNE_SEDE_LEGALE \n"
          + "  AND A1.DATA_CESSAZIONE IS NULL     \n"
          + "   AND SC2.ID_AZIENDA = A1.ID_AZIENDA      \n"
          + "  AND SC2.ID_RUOLO = 1     \n"
          + "   AND SC2.DATA_FINE_VALIDITA IS NULL    \n"
          + "  AND A1.DATA_FINE_VALIDITA IS NULL    	\n");

      if (!GenericValidator.isBlankOrNull(sql))
      {
        SELECT.append("   AND A1.ID_AZIENDA= SUB_SQL.ID_AZIENDA  	\n");
      }
      
      if(hasRuoloProfessionista)
	  {
		  SELECT.append(" AND VRA.ID_PROCEDIMENTO = S.ID_PROCEDIMENTO				\n"
		 + " AND S.ID_RUOLO = VRA.EXT_ID_RUOLO_ANAG									\n"
		 + " AND S.CODICE_FISCALE = :CODICE_FISCALE                       			\n"
		 + " AND S.ID_PROCEDIMENTO=:EXT_ID_PROCEDIMENTO				   	    		\n"
		 + " AND VRA.CODICE_RUOLO_PAPUA = :CODICE_RUOLO_PAPUA						\n"
		 + " AND SYSDATE BETWEEN S.DATA_INIZIO_VALIDITA_PROF_AZ AND NVL(DATA_FINE_VALIDITA_PROF_AZ,SYSDATE) 				\n"
		 + " AND  S.EXT_BANDO_TIPO_PRATICA=:ID_BANDO"
		 + " AND A1.ID_AZIENDA = S.ID_AZIENDA																				\n");
	  }

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" AND A1.ID_AZIENDA = C.ID_AZIENDA						\n"
            + " AND C.DATA_FINE_VALIDITA IS NULL								\n"
            + " AND ( C.ID_INTERMEDIARIO_ZONA = :ID_INTERMEDIARIO  	\n"
            + "		 OR C.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO \n"
            + "      OR C.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO ) \n");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          Vector<Long> aziende = new Vector<Long>();
          for (EnteLogin ente : utenteAbilitazioni.getEntiAbilitati())
          {
            final AziendaAgricola aziendaAgricola = ente.getAziendaAgricola();
            if (aziendaAgricola != null)
            {
              aziende.add(aziendaAgricola.getIdAzienda());
            }
          }
          SELECT.append(" " + getInCondition("A1.ID_AZIENDA", aziende));
        }

      SELECT.append(
          " ORDER BY A1.DENOMINAZIONE, A1.ID_AZIENDA, PROC.ID_PROCEDIMENTO_OGGETTO DESC ");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        parameterSource.addValue("ID_INTERMEDIARIO", utenteAbilitazioni
            .getEnteAppartenenza().getIntermediario().getIdIntermediario(),
            Types.NUMERIC);
      }
	if(hasRuoloProfessionista)
	{
		parameterSource.addValue("CODICE_FISCALE", utenteAbilitazioni.getCodiceFiscale(), Types.VARCHAR);
		parameterSource.addValue("EXT_ID_PROCEDIMENTO", idProcedimentoAgricolo, Types.NUMERIC);
		parameterSource.addValue("CODICE_RUOLO_PAPUA", utenteAbilitazioni.getRuolo().getCodice(), Types.VARCHAR);
	}
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<AziendaDTO>>()
          {
            @Override
            public List<AziendaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<AziendaDTO> list = new ArrayList<AziendaDTO>();

              while (rs.next())
              {
                AziendaDTO azienda = new AziendaDTO();
                azienda.setIdAzienda(rs.getLong("ID_AZIENDA"));
                azienda.setIdFormaGiuridica(rs.getLong("ID_FORMA_GIURIDICA"));
                azienda.setCuaa(rs.getString("CUAA"));
                azienda.setDenominazione(rs.getString("DENOMINAZIONE"));
                azienda.setDenominazioneIntestazione(
                    rs.getString("DENOMINAZIONE_INTESTAZ"));
                azienda.setPartitaIva(rs.getString("PARTITA_IVA"));
                azienda.setSedeLegale(rs.getString("INDIRIZZO_SEDE_LEGALE"));
                azienda.setIdProcediemnto(rs.getLong("ID_PROCEDIMENTO"));
                azienda.setDescrComune(rs.getString("DESCRIZIONE_COMUNE"));
                azienda.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                azienda.setIdStatoOggetto(rs.getLong("ID_STATO_OGGETTO"));
                azienda.setDescrStatoOggetto(rs.getString("DESCRIZIONE"));

                azienda.setRichiedenteDescr(rs.getString("CONT1"));
                if (GenericValidator
                    .isBlankOrNull(azienda.getRichiedenteDescr()))
                {
                  azienda.setRichiedenteDescr(rs.getString("CONT2"));
                }

                String procOggetto = rs.getString("PROCEDIMENTO_OGGETTO");
                Date dataFine = rs.getDate("DATA_FINE");

                if (procOggetto == null || procOggetto.trim().length() <= 0)
                {
                  azienda.setProcedimentoEsistente(false);
                  azienda
                      .setAzione(IuffiConstants.PAGINATION.AZIONI.AGGIUNGI);
                  azienda.setTitleHref(
                      "Crea nuovo procedimento per questa azienda");
                  azienda.setAzioneHref(
                      "creaProcedimento_" + azienda.getIdAzienda() + ".do");
                  azienda.setProcesistente("N");
                }
                else
                {
                  azienda.setProcedimentoEsistente(true);
                  azienda
                      .setAzione(IuffiConstants.PAGINATION.AZIONI.DETTAGLIO);
                  azienda.setTitleHref(
                      "Procedimento esistente, visualizza o modifica dettaglio");
                  azienda.setAzioneHref(
                      "modificaProcedimento_" + azienda.getIdAzienda() + ".do");
                  azienda.setProcesistente("S");
                }

                if (dataFine == null)
                {
                  azienda.setProcedimentoAperto(false);
                }
                else
                {
                  azienda.setProcedimentoAperto(true);
                  azienda.setDataTrasmissione(
                      IuffiUtils.DATE.formatDate(dataFine));
                }

                list.add(azienda);
              }
              return list.isEmpty() ? null : list;
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

  public List<AziendaDTO> getDettaglioAziendeById(Vector<Long> vIdAzienda,
      long idBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDettaglioAziendeById";
    StringBuffer SELECT = new StringBuffer("");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT.append(
          " SELECT                                                           \n"
              + "    A1.ID_AZIENDA,                                                \n"
              + "    A1.CUAA,                                                      \n"
              + "    NVL(A1.PARTITA_IVA,'') AS PARTITA_IVA,                        \n"
              + "    A1.DENOMINAZIONE,                                             \n"
              + "    A1.DENOMINAZIONE || ' ' || A1.INTESTAZIONE_PARTITA_IVA AS DENOMINAZIONE_INTESTAZ,                                             \n"
              + "    A1.ID_FORMA_GIURIDICA,                                        \n"
              + "    A1.INDIRIZZO_SEDE_LEGALE,                                     \n"
              + "    (SELECT DESCRIZIONE_COMUNE FROM SMRGAA_V_DATI_AMMINISTRATIVI WHERE ISTAT_COMUNE = A1.ISTAT_COMUNE_SEDE_LEGALE AND DT_FINE_VALIDITA_COM IS NULL) DESCR_COMUNE,                                        \n"
              + "    (SELECT DESCRIZIONE_PROVINCIA FROM SMRGAA_V_DATI_AMMINISTRATIVI WHERE ISTAT_COMUNE = A1.ISTAT_COMUNE_SEDE_LEGALE AND DT_FINE_VALIDITA_COM IS NULL) DESCR_PROVINCIA,                                        \n"
              + "    PROC.ID_PROCEDIMENTO,                                         \n"
              + "    PROC.ID_STATO_OGGETTO,                                        \n"
              + "    PROC.IDENTIFICATIVO,                                          \n"
              + "    PROC.DESCRIZIONE,                                             \n"
              + "    PROC.CONT1,                                    	           \n"
              + "    SC2.COGNOME || ' ' || SC2.NOME AS CONT2,                      \n"
              + "    NVL(PROC.ID_PROCEDIMENTO_OGGETTO,'') AS PROCEDIMENTO_OGGETTO, \n"
              + "    NVL(PROC.DATA_FINE,'') AS DATA_FINE                           \n"
              + "  FROM                                                            \n"
              + "   SMRGAA_V_DATI_ANAGRAFICI A1,                                   \n"
              + "    SMRGAA_V_SOGGETTI_COLLEGATI SC2,                        	   \n"
              + "   (SELECT                                                        \n"
              + "       D1.EXT_ID_AZIENDA,                                         \n"
              + "       B1.ID_PROCEDIMENTO,                                        \n"
              + "       C1.ID_PROCEDIMENTO_OGGETTO,                                \n"
              + "       O1.ID_STATO_OGGETTO,    	                               \n"
              + "       SC1.COGNOME || ' ' || SC1.NOME AS CONT1, 				   \n"
              + "       B1.IDENTIFICATIVO,    	                                   \n"
              + "       O1.DESCRIZIONE,				                               \n"
              + "       C1.DATA_FINE                                               \n"
              + "     FROM                                                         \n"
              + "       IUF_T_PROCEDIMENTO B1,                                     \n"
              + "       IUF_T_PROCEDIMENTO_OGGETTO C1,                             \n"
              + "       IUF_D_STATO_OGGETTO O1,                            		   \n"
              + "       SMRGAA_V_SOGGETTI_COLLEGATI SC1,                          \n"
              + "       IUF_T_PROCEDIMENTO_AZIENDA D1                              \n"
              + "     WHERE                                                        \n"
              + "       B1.ID_PROCEDIMENTO = C1.ID_PROCEDIMENTO                    \n"
              + "       AND D1.ID_PROCEDIMENTO = B1.ID_PROCEDIMENTO                \n"
              + "       AND B1.ID_STATO_OGGETTO = O1.ID_STATO_OGGETTO              \n"
              + "       AND B1.ID_STATO_OGGETTO < 90                               \n"
              + " AND SC1.ID_CONTITOLARE(+) = C1.EXT_ID_CONTITOLARE          		\n"
              + " AND ( SC1.ID_AZIENDA is null OR SC1.ID_AZIENDA = D1.EXT_ID_AZIENDA) 	\n"
              + "  AND SC1.DATA_FINE_VALIDITA IS NULL    \n"
              + "       AND D1.DATA_FINE IS NULL                                   \n"
              + "       AND B1.ID_BANDO = :ID_BANDO ) PROC                         \n"
              + "  WHERE                                                           \n"
              + "  A1.DATA_FINE_VALIDITA IS NULL                                   \n"
              // + " AND A1.DATA_CESSAZIONE IS NULL \n"
              + "   AND SC2.ID_AZIENDA = A1.ID_AZIENDA      \n"
              + "  AND SC2.ID_RUOLO = 1     \n"
              + "   AND SC2.DATA_FINE_VALIDITA IS NULL    \n"
              + "  AND PROC.EXT_ID_AZIENDA(+) = A1.ID_AZIENDA                      \n"
              + " " + getInCondition("A1.ID_AZIENDA", vIdAzienda)
              + "  				   \n");
      SELECT.append(
          " ORDER BY A1.DENOMINAZIONE , A1.ID_AZIENDA, PROC.ID_PROCEDIMENTO_OGGETTO \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<AziendaDTO>>()
          {
            @Override
            public List<AziendaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<AziendaDTO> list = new ArrayList<AziendaDTO>();
              while (rs.next())
              {
                AziendaDTO azienda = new AziendaDTO();
                azienda.setIdAzienda(rs.getLong("ID_AZIENDA"));
                azienda.setIdFormaGiuridica(rs.getLong("ID_FORMA_GIURIDICA"));
                azienda.setCuaa(rs.getString("CUAA"));
                azienda.setDenominazione(rs.getString("DENOMINAZIONE"));
                azienda.setDenominazioneIntestazione(
                    rs.getString("DENOMINAZIONE_INTESTAZ"));
                azienda.setDescrComune(rs.getString("DESCR_COMUNE"));
                azienda.setDescrProvincia(rs.getString("DESCR_PROVINCIA"));
                
                azienda.setRichiedenteDescr(rs.getString("CONT1"));
                if (GenericValidator
                    .isBlankOrNull(azienda.getRichiedenteDescr()))
                {
                  azienda.setRichiedenteDescr(rs.getString("CONT2"));
                }

                azienda.setPartitaIva(rs.getString("PARTITA_IVA"));
                azienda.setSedeLegale(rs.getString("INDIRIZZO_SEDE_LEGALE"));
                azienda.setIdProcediemnto(rs.getLong("ID_PROCEDIMENTO"));
                azienda.setIdStatoOggetto(rs.getLong("ID_STATO_OGGETTO"));
                azienda.setDescrStatoOggetto(rs.getString("DESCRIZIONE"));

                String procOggetto = rs.getString("PROCEDIMENTO_OGGETTO");
                Date dataFine = rs.getDate("DATA_FINE");

                if (procOggetto == null || procOggetto.trim().length() <= 0)
                {
                  azienda.setProcedimentoEsistente(false);
                  azienda
                      .setAzione(IuffiConstants.PAGINATION.AZIONI.AGGIUNGI);
                  azienda.setTitleHref(
                      "Crea nuovo procedimento per questa azienda");
                  azienda.setAzioneHref(
                      "creaProcedimento_" + azienda.getIdAzienda() + ".do");
                  azienda.setProcesistente("N");
                }
                else
                {
                  azienda.setProcedimentoEsistente(true);
                  azienda
                      .setAzione(IuffiConstants.PAGINATION.AZIONI.DETTAGLIO);
                  azienda.setTitleHref(
                      "Procedimento esistente, visualizza o modifica dettaglio");
                  azienda.setAzioneHref(
                      "modificaProcedimento_" + azienda.getIdAzienda() + ".do");
                  azienda.setProcesistente("S");

                }

                if (dataFine == null)
                {
                  azienda.setProcedimentoAperto(false);
                }
                else
                {
                  azienda.setProcedimentoAperto(true);
                  azienda.setDataTrasmissione(
                      IuffiUtils.DATE.formatDate(dataFine));
                }

                list.add(azienda);
              }
              return list.isEmpty() ? null : list;
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

  public class ListControlloHandler implements SqlReturnType
  {
    // struct data from jdbc
    public Object getTypeValue(CallableStatement cs, int paramIndex,
        int sqlType, String typeName)
        throws SQLException
    {
      Array arrayObj = cs.getArray(1);
      if (arrayObj == null)
      {
        return null;
      }
      Object array = arrayObj.getArray();
      List<ControlloDTO> controlli = new ArrayList<ControlloDTO>();
      if (array != null)
      {
        for (Object elemento : (Object[]) array)
        {
          Object[] fields;
          fields = ((Struct) elemento).getAttributes();
          ControlloDTO controllo = new ControlloDTO();
          controllo.setCodice((String) fields[0]);
          controllo.setDescrizione((String) fields[1]);
          controllo.setMessaggioErrore((String) fields[2]);
          controlli.add(controllo);
        }
      }
      if (controlli.size() > 0)
      {
        return controlli.toArray(new ControlloDTO[controlli.size()]);
      }
      else
      {
        return null;
      }
    }
  }

  public MainControlloDTO callMainControlliGravi(long idBandoOggetto,
      Long idProcedimento, long idAzienda, long idUtenteLogin,
      Long codRaggruppamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "callMainControlliGravi";
    StringBuffer CALL = new StringBuffer(
        "PCK_IUF_CONTROLLI.MainControlliGravi");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    // DATI DI ESEMPIO PER AVERE UNA LISTA DI ERRORI GRAVI
    // idAzienda=37702;
    // idBandoOggetto=16;
    try
    {

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("PIDBANDOOGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("PIDPROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      parameterSource.addValue("PEXTIDAZIENDA", idAzienda, Types.NUMERIC);
      parameterSource.addValue("PEXTIDUTENTE", idUtenteLogin, Types.NUMERIC);
      parameterSource.addValue("PCODICERAGGRUPPAMENTO", codRaggruppamento,
          Types.NUMERIC);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_IUF_CONTROLLI")
              .withFunctionName("MainControlliGravi")
              .withoutProcedureColumnMetaDataAccess();
      call.addDeclaredParameter(new SqlOutParameter("RESULT",
          java.sql.Types.ARRAY, "LIST_CONTROLLO", new ListControlloHandler()));
      call.addDeclaredParameter(
          new SqlParameter("PIDBANDOOGGETTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PIDPROCEDIMENTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PEXTIDAZIENDA", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PEXTIDUTENTE", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PCODICERAGGRUPPAMENTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("PRISULTATO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("PMESSAGGIO", java.sql.Types.VARCHAR));
      Map<String, Object> results = call.execute(parameterSource);

      MainControlloDTO dto = new MainControlloDTO();
      dto.setRisultato(((BigDecimal) results.get("PRISULTATO")).intValue());
      dto.setMessaggio(safeMessaggioPLSQL((String) results.get("PMESSAGGIO")));
      dto.setControlli((ControlloDTO[]) results.get("RESULT"));
      return dto;

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

  public MainControlloDTO callMainCreazione(long idBando,
      long idLegameGruppoOggetto, Long idProcedimento, long idAzienda,
      long idUtenteLogin, String codAttore, Long codRaggruppamento,
      boolean forzaAlfanumerico, long idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "callMainCreazione";
    StringBuffer CALL = new StringBuffer(
        "PCK_IUF_GESTIONE_PROC_OGGETT.MainCreazione");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("PIDBANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("PIDLEGAMEGRUPPOOGGETTO", idLegameGruppoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("PIDPROCEDIMENTOAGRICOLO", idProcedimentoAgricolo,
              Types.NUMERIC);
      parameterSource.addValue("PIDPROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      parameterSource.addValue("PEXTIDAZIENDA", idAzienda, Types.NUMERIC);
      parameterSource.addValue("PEXTIDUTENTE", idUtenteLogin, Types.NUMERIC);
      parameterSource.addValue("PCODATTORE", codAttore, Types.VARCHAR);
      parameterSource.addValue("PCODICERAGGRUPPAMENTO", codRaggruppamento,
          Types.NUMERIC);
      parameterSource.addValue("PFORZAALFANUMERICO",
          forzaAlfanumerico ? "S" : "N", Types.VARCHAR);
    

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_IUF_GESTIONE_PROC_OGGETT")
              .withFunctionName("MainCreazione")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlOutParameter("RESULT", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PIDBANDO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PIDLEGAMEGRUPPOOGGETTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
              new SqlParameter("PIDPROCEDIMENTOAGRICOLO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PIDPROCEDIMENTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PEXTIDAZIENDA", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PCODICERAGGRUPPAMENTO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PFORZAALFANUMERICO", java.sql.Types.CHAR));
      call.addDeclaredParameter(
          new SqlParameter("PEXTIDUTENTE", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PCODATTORE", java.sql.Types.VARCHAR));
      call.addDeclaredParameter(
          new SqlOutParameter("PRISULTATO", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("PMESSAGGIO", java.sql.Types.VARCHAR));
      Map<String, Object> results = call.execute(parameterSource);

      MainControlloDTO dto = new MainControlloDTO();
      dto.setRisultato(((BigDecimal) results.get("PRISULTATO")).intValue());
      dto.setMessaggio(
          escapeHtml4(safeMessaggioPLSQL((String) results.get("PMESSAGGIO"))));
      dto.setIdProcedimento((BigDecimal) results.get("RESULT"));
      return dto;

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
  
  public MainControlloDTO callMainCreazione(long idBando,
	      long idLegameGruppoOggetto, Long idProcedimento, long idAzienda,
	      long idUtenteLogin, String codAttore, Long codRaggruppamento,
	      boolean forzaAlfanumerico) throws InternalUnexpectedException
	  {
	    String THIS_METHOD = "callMainCreazione";
	    StringBuffer CALL = new StringBuffer(
	        "PCK_IUF_GESTIONE_PROC_OGGETT.MainCreazione");
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
	    }
	    try
	    {
	      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
	      parameterSource.addValue("PIDBANDO", idBando, Types.NUMERIC);
	      parameterSource.addValue("PIDLEGAMEGRUPPOOGGETTO", idLegameGruppoOggetto,
	          Types.NUMERIC);
	      parameterSource.addValue("PIDPROCEDIMENTO", idProcedimento,
	          Types.NUMERIC);
	      parameterSource.addValue("PEXTIDAZIENDA", idAzienda, Types.NUMERIC);
	      parameterSource.addValue("PEXTIDUTENTE", idUtenteLogin, Types.NUMERIC);
	      parameterSource.addValue("PCODATTORE", codAttore, Types.VARCHAR);
	      parameterSource.addValue("PCODICERAGGRUPPAMENTO", codRaggruppamento,
	          Types.NUMERIC);
	      parameterSource.addValue("PFORZAALFANUMERICO",
	          forzaAlfanumerico ? "S" : "N", Types.VARCHAR);
	      
	      SimpleJdbcCall call = new SimpleJdbcCall(
	          (DataSource) appContext.getBean("dataSource"))
	              .withCatalogName("PCK_IUF_GESTIONE_PROC_OGGETT")
	              .withFunctionName("MainCreazione")
	              .withoutProcedureColumnMetaDataAccess();

	      call.addDeclaredParameter(
	          new SqlOutParameter("RESULT", java.sql.Types.NUMERIC));
	      call.addDeclaredParameter(
	          new SqlParameter("PIDBANDO", java.sql.Types.NUMERIC));
	      call.addDeclaredParameter(
	          new SqlParameter("PIDLEGAMEGRUPPOOGGETTO", java.sql.Types.NUMERIC));
	      call.addDeclaredParameter(
	          new SqlParameter("PIDPROCEDIMENTO", java.sql.Types.NUMERIC));
	      call.addDeclaredParameter(
	          new SqlParameter("PEXTIDAZIENDA", java.sql.Types.NUMERIC));
	      call.addDeclaredParameter(
	          new SqlParameter("PCODICERAGGRUPPAMENTO", java.sql.Types.NUMERIC));
	      call.addDeclaredParameter(
	          new SqlParameter("PFORZAALFANUMERICO", java.sql.Types.CHAR));
	      call.addDeclaredParameter(
	          new SqlParameter("PEXTIDUTENTE", java.sql.Types.NUMERIC));
	      call.addDeclaredParameter(
	          new SqlParameter("PCODATTORE", java.sql.Types.VARCHAR));
	      call.addDeclaredParameter(
	          new SqlOutParameter("PRISULTATO", java.sql.Types.NUMERIC));
	      call.addDeclaredParameter(
	          new SqlOutParameter("PMESSAGGIO", java.sql.Types.VARCHAR));
	      Map<String, Object> results = call.execute(parameterSource);

	      MainControlloDTO dto = new MainControlloDTO();
	      dto.setRisultato(((BigDecimal) results.get("PRISULTATO")).intValue());
	      dto.setMessaggio(
	          escapeHtml4(safeMessaggioPLSQL((String) results.get("PMESSAGGIO"))));
	      dto.setIdProcedimento((BigDecimal) results.get("RESULT"));
	      return dto;

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

  private boolean isBeneficiario(UtenteAbilitazioni utenteAbilitazioni)
  {
    return utenteAbilitazioni.getRuolo().isUtenteAziendaAgricola()
        || utenteAbilitazioni.getRuolo().isUtenteLegaleRappresentante()
        || utenteAbilitazioni.getRuolo().isUtenteTitolareCf();

  }

  public FileAllegatoDTO getFileAllegato(long idAllegatiBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getFileAllegato";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 							\n"
          + " 	ID_ALLEGATI_BANDO,				\n"
          + " 	DESCRIZIONE,					\n"
          + " 	NOME_FILE,						\n"
          + " 	ID_BANDO,						\n"
          + " 	FILE_ALLEGATO,					\n"
          + " 	ORDINE							\n"
          + " FROM 								\n"
          + "	IUF_D_ALLEGATI_BANDO		    \n"
          + " WHERE 							\n"
          + "   ID_ALLEGATI_BANDO = :ID_ALLEGATI_BANDO \n"
          + "   AND FLAG_VISIBILE = 'S'         \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ALLEGATI_BANDO", idAllegatiBando,
          Types.NUMERIC);

      return queryForObject(SELECT, parameterSource, FileAllegatoDTO.class);
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

  public List<AmmCompetenzaDTO> getAmmCompetenzaAssociate(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAmmCompetenzaAssociate";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   A.ID_BANDO,                                        \n"
          + "   B.ID_AMM_COMPETENZA,                               \n"
          + "   B.DESC_BREVE_TIPO_AMMINISTRAZ,                     \n"
          + "   B.DESCRIZIONE                                      \n"
          + " FROM                                                 \n"
          + "   IUF_D_BANDO_AMM_COMPETENZA A,                      \n"
          + "   IUF_D_BANDO C,				                       \n"
          + "   SMRCOMUNE_V_AMM_COMPETENZA B                       \n"
          + " WHERE                                                \n"
          + "   A.EXT_ID_AMM_COMPETENZA = B.ID_AMM_COMPETENZA      \n"
          + "   AND A.ID_BANDO = :ID_BANDO                         \n"
          + "   AND A.ID_BANDO = C.ID_BANDO                        \n"
          + "   AND C.FLAG_MASTER = 'N'                            \n"
          + " ORDER BY B.DESC_BREVE_TIPO_AMMINISTRAZ,B.DESCRIZIONE \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<AmmCompetenzaDTO>>()
          {
            @Override
            public List<AmmCompetenzaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<AmmCompetenzaDTO> list = new ArrayList<AmmCompetenzaDTO>();
              AmmCompetenzaDTO ammDTO = null;
              while (rs.next())
              {
                ammDTO = new AmmCompetenzaDTO();
                ammDTO.setIdBando(rs.getLong("ID_BANDO"));
                ammDTO.setIdAmmCompetenza(rs.getLong("ID_AMM_COMPETENZA"));
                ammDTO.setDescBreveTipoAmministraz(
                    rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                ammDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(ammDTO);
              }
              return list.isEmpty() ? null : list;
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

  public void updateTecnicoAndUfficioZonaIfModified(long idProcedimento,
      long extIdUfficioZona, long extIdTecnico,
      long idUtenteLogin) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "updateTecnicoAndUfficioZonaIfModified";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String UPDATE = " INSERT                                                                        \n"
        + " INTO                                                                          \n"
        + "   IUF_T_PROCEDIM_AMMINISTRAZIO                                              \n"
        + "   (                                                                           \n"
        + "     ID_PROCEDIM_AMMINISTRAZIONE,                                              \n"
        + "     ID_PROCEDIMENTO,                                                          \n"
        + "     EXT_ID_AMM_COMPETENZA,                                                    \n"
        + "     DATA_INIZIO,                                                              \n"
        + "     DATA_FINE,                                                                \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,                                              \n"
        + "     NOTE,                                                                     \n"
        + "     EXT_ID_TECNICO,                                                           \n"
        + "     EXT_ID_UFFICIO_ZONA                                                       \n"
        + "   )                                                                           \n"
        + "   (                                                                           \n"
        + "     SELECT                                                                    \n"
        + "       SEQ_IUF_T_PROCEDIM_AMMINISTR.NEXTVAL,                                 \n"
        + "       ID_PROCEDIMENTO,                                                        \n"
        + "       EXT_ID_AMM_COMPETENZA,                                                  \n"
        + "       SYSDATE,                                                                \n"
        + "       DATA_FINE,                                                              \n"
        + "       :EXT_ID_UTENTE_AGGIORNAMENTO,                                           \n"
        + "       NOTE,                                                                   \n"
        + "       :EXT_ID_TECNICO,                                                        \n"
        + "       :EXT_ID_UFFICIO_ZONA                                                    \n"
        + "     FROM                                                                      \n"
        + "       IUF_T_PROCEDIM_AMMINISTRAZIO PA                                       \n"
        + "     WHERE                                                                     \n"
        + "       PA.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                   \n"
        + "       AND DATA_FINE     IS NULL                                               \n"
        + "       AND NVL(TO_CHAR(PA.EXT_ID_TECNICO),' ')                                 \n"
        + "       || '_'                                                                  \n"
        + "       || NVL(TO_CHAR(PA.EXT_ID_UFFICIO_ZONA),'_') <> TO_CHAR(:EXT_ID_TECNICO) \n"
        + "       || '_'                                                                  \n"
        + "       || TO_CHAR(:EXT_ID_UFFICIO_ZONA)                                        \n"
        + "   )                                                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_TECNICO", extIdTecnico, Types.NUMERIC);
    mapParameterSource.addValue("EXT_ID_UFFICIO_ZONA", extIdUfficioZona,
        Types.NUMERIC);
    try
    {
      int numUpdated = namedParameterJdbcTemplate.update(UPDATE,
          mapParameterSource);
      if (numUpdated > 0)
      {
        /*
         * Ho registrato un record (numUpdate > 0 può voler dire solo ==1),
         * quindi vuol dire che almeno uno dei 2 campi EXT_ID_TECNICO,
         * EXT_ID_UFFICIO_ZONA è variato, quindi devo chiudere il vecchio record
         * attivo
         */
        UPDATE = " UPDATE                                                                    \n"
            + "   IUF_T_PROCEDIM_AMMINISTRAZIO                                          \n"
            + " SET                                                                       \n"
            + "   DATA_FINE = SYSDATE                                                     \n"
            + " WHERE                                                                     \n"
            + "   ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                      \n"
            + "   AND DATA_FINE  IS NULL                                                  \n"
            + "   AND NVL(TO_CHAR(EXT_ID_TECNICO),' ')                                 \n"
            + "   || '_'                                                                  \n"
            + "   || NVL(TO_CHAR(EXT_ID_UFFICIO_ZONA),'_') <> TO_CHAR(:EXT_ID_TECNICO) \n"
            + "   || '_'                                                                  \n"
            + "   || TO_CHAR(:EXT_ID_UFFICIO_ZONA)                                        \n";
        /* I parametri del MapSqlParameterSource sono sempre quelli di prima */
        namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      }
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimento", idProcedimento),
              new LogParameter("idProcedimento", extIdUfficioZona),
              new LogParameter("extIdTecnico", extIdTecnico),
              new LogParameter("idUtenteLogin", idUtenteLogin)
          },
          new LogVariable[]
          {
          // new LogVariable("idVariabile", idVariabile),
          }, UPDATE, mapParameterSource);
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

  public List<Procedimento> searchProcedimentiBandoEsistente(long idBando,
      long idAzienda) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::searchProcedimentiBandoEsistente]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuilder QUERY = new StringBuilder();
    QUERY.append(
        " SELECT                                                                 \n"
            + "   PPAZ1.ID_PROCEDIMENTO,                                               \n"
            + "   PPAZ1.IDENTIFICATIVO ,                                               \n"
            + "   DL.CODICE, DL.ID_LIVELLO                                             \n"
            + " FROM                                                                   \n"
            + "   IUF_T_DATI_PROCEDIMENTO DPAZ1,                                       \n"
            + "   IUF_T_PROCEDIMENTO_OGGETTO POAZ1,                                    \n"
            + "   IUF_T_PROCEDIMENTO PPAZ1,                                            \n"
            + "   IUF_T_PROCEDIMENTO_AZIENDA PROCAZAZ1,                                \n"
            + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGOAZ1,                                  \n"
            + "   IUF_D_OGGETTO OOAZ1,                                                 \n"
            + "   IUF_R_PROCEDIMENTO_LIVELLO PL,                                       \n"
            + "   IUF_D_LIVELLO DL,                                                    \n"
            + "   IUF_T_PROC_DOMANDA_PREC DP                                           \n"
            + " WHERE                                                                  \n"
            + "   DPAZ1.ID_PROCEDIMENTO_OGGETTO = POAZ1.ID_PROCEDIMENTO_OGGETTO        \n"
            + "   AND DPAZ1.ID_PROCEDIMENTO IS NULL                                    \n"
            + "   AND POAZ1.ID_PROCEDIMENTO = PPAZ1.ID_PROCEDIMENTO                    \n"
            + "   AND PPAZ1.ID_PROCEDIMENTO = PROCAZAZ1.ID_PROCEDIMENTO                \n"
            + "   AND PROCAZAZ1.EXT_ID_AZIENDA = :ID_AZIENDA                           \n"
            + "   AND PROCAZAZ1.DATA_FINE IS NULL                                      \n"
            + "   AND PPAZ1.ID_BANDO = :ID_BANDO                                       \n"
            + "   AND POAZ1.ID_LEGAME_GRUPPO_OGGETTO = LGOAZ1.ID_LEGAME_GRUPPO_OGGETTO \n"
            + "   AND LGOAZ1.ID_OGGETTO = OOAZ1.ID_OGGETTO                             \n"
            + "   AND PPAZ1.ID_STATO_OGGETTO BETWEEN 10 AND 90                         \n"
            + "   AND OOAZ1.CODICE = 'DA'                                              \n"
            + "   AND PL.ID_PROCEDIMENTO = PPAZ1.ID_PROCEDIMENTO                       \n"
            + "   AND PL.ID_LIVELLO = DL.ID_LIVELLO                                    \n"
            + "   AND DP.ID_PROCEDIMENTO_PREC(+) = PPAZ1.ID_PROCEDIMENTO               \n"
            + "   AND (DP.FLAG_ELABORATA IS NULL OR DP.FLAG_ELABORATA <> 'S')          \n"
            + "   AND EXISTS (SELECT PL2.ID_PROCEDIMENTO FROM IUF_R_PROCEDIMENTO_LIVELLO PL2 WHERE  \n"
            + "    PL2.ID_PROCEDIMENTO = PPAZ1.ID_PROCEDIMENTO  AND PL2.DATA_AMMISSIONE IS NOT NULL )  \n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_AZIENDA", idAzienda, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY.toString(),
          mapParameterSource, new ResultSetExtractor<List<Procedimento>>()
          {
            @Override
            public List<Procedimento> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<Procedimento> list = new ArrayList<Procedimento>();
              Procedimento proc = null;
              long idProcedimentoLast = -1;
              long idProcedimento;
              List<String> operazioni = null;

              while (rs.next())
              {
                idProcedimento = rs.getLong("ID_PROCEDIMENTO");
                if (idProcedimentoLast != idProcedimento)
                {
                  idProcedimentoLast = idProcedimento;
                  proc = new Procedimento();
                  list.add(proc);
                  proc.setIdProcedimento(idProcedimento);
                  proc.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                  operazioni = new ArrayList<>();
                  proc.setOperazioni(operazioni);
                }
                operazioni.add(rs.getString("CODICE"));
              }
              return list.isEmpty() ? null : list;
            }
          });

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBando", idBando),
              new LogParameter("idAzienda", idAzienda)
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

  public void inserisciProcDomandaPrec(int idProcedimentoVecchio, int idAzienda,
      long idBandoVecchio) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::inserisciProcDomandaPrec]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " INSERT                                      		\n"
        + " INTO                                        \n"
        + "   IUF_T_PROC_DOMANDA_PREC            		\n"
        + "   (                                         \n"
        + "     ID_PROC_DOMANDA_PREC,            		\n"
        + "     ID_PROCEDIMENTO_PREC,                   \n"
        + "     EXT_ID_AZIENDA,                  		\n"
        + "     ID_BANDO,                            	\n"
        + "     FLAG_ELABORATA            				\n"
        + "   )                                         \n"
        + "   VALUES                                    \n"
        + "   (                                         \n"
        + "     SEQ_IUF_T_PROC_DOMANDA_PREC.NEXTVAL,    \n"
        + "     :ID_PROCEDIMENTO,                       \n"
        + "     :ID_AZIENDA,                 			\n"
        + "     :ID_BANDO,                              \n"
        + "     'N'           							\n"
        + "   )                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimentoVecchio,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_AZIENDA", idAzienda, Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBandoVecchio, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PROCEDIMENTO", idProcedimentoVecchio),
              new LogParameter("ID_AZIENDA", idAzienda),
              new LogParameter("ID_BANDO", idBandoVecchio)
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

  public boolean callIsValidazioneGrafica(long idAzienda, String annoCampagna, long idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "callMainCreazione";
    StringBuffer CALL = new StringBuffer(
        "PCK_IUF_UTILITY.WrapperIsGraphicValidation");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      Integer idDichConsistenza = getLasDichiarazioneConsistenza(idAzienda,idProcedimentoAgricolo);
      if (idDichConsistenza == null)
      {
        throw new InternalUnexpectedException(
            "Dichiarazione di consistenza non trovata!", null);
      }

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("PIDDICHCONS", idDichConsistenza.intValue(),
          Types.NUMERIC);
      parameterSource.addValue("PANNOCAMPAGNA", annoCampagna, Types.NUMERIC);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_IUF_UTILITY")
              .withFunctionName("WrapperIsGraphicValidation")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlOutParameter("RESULT", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PIDDICHCONS", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("PANNOCAMPAGNA", java.sql.Types.NUMERIC));
      Map<String, Object> results = call.execute(parameterSource);

      BigDecimal ret = (BigDecimal) results.get("RESULT");
      return (ret != null && ret.intValue() == 0);

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

  public int updateNoteIterCorrenteByIdprocedimento(long idProcedimento, String note)
	  throws InternalUnexpectedException {
      final String THIS_METHOD = "updateNoteIterCorrenteByIdprocedimento";
      if (logger.isDebugEnabled()) {
	  logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
      }
      String UPDATE = " UPDATE IUF_T_ITER_PROCEDIMENTO_OGGE                                 \n"
	      + " SET                                                                   \n"
	      + "     NOTE = :NOTE                                                      \n"
	      + " WHERE                                                                 \n"
	      + "     ID_PROCEDIMENTO_OGGETTO = (SELECT MAX(PO.ID_PROCEDIMENTO_OGGETTO) \n"
	      + "     FROM IUF_T_PROCEDIMENTO_OGGETTO PO                                \n"
	      + "     WHERE                                                             \n"
	      + "     PO.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                             \n"
	      + "     AND PO.DATA_FINE IS NULL                                          \n"
	      + "     AND                                                               \n"
	      + "     (                                                                 \n"
	      + "         SELECT                                                        \n"
	      + "             COUNT(*)                                                  \n"
	      + "         FROM                                                          \n"
	      + "             IUF_T_ITER_PROCEDIMENTO_OGGE IPO                        \n"
	      + "         WHERE                                                         \n"
	      + "             PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO  \n"
	      + "     ) = 1)                                                            \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("NOTE", note, Types.VARCHAR);
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento, Types.NUMERIC);

      try {
	  int numUpdated = namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
	  return numUpdated;
      } catch (Throwable t) {
	  InternalUnexpectedException e = new InternalUnexpectedException(t, UPDATE);
	  logInternalUnexpectedException(e, "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
	  throw e;
      }
  }

}
