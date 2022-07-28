package it.csi.iuffi.iuffiweb.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.validator.GenericValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.AmmCompetenzaDTO;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.FiltroVO;
import it.csi.iuffi.iuffiweb.dto.FocusAreaDTO;
import it.csi.iuffi.iuffiweb.dto.GraduatoriaDTO;
import it.csi.iuffi.iuffiweb.dto.GravitaNotificaVO;
import it.csi.iuffi.iuffiweb.dto.GruppoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.IterProcedimentoGruppoDTO;
import it.csi.iuffi.iuffiweb.dto.NotificaDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.dto.RicercaProcedimentiVO;
import it.csi.iuffi.iuffiweb.dto.SettoriDiProduzioneDTO;
import it.csi.iuffi.iuffiweb.dto.VisibilitaDTO;
import it.csi.iuffi.iuffiweb.dto.gestioneeventi.EventiDTO;
import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.OggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.ProcedimentoGruppoVO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.EsitoOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.MacroCU;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class RicercaDAO extends BaseDAO
{
  private static final String THIS_CLASS = RicercaDAO.class.getSimpleName();

  public RicercaDAO()
  {
  }

  public List<LivelloDTO> getLivelliAttivi(int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getLivelliAttivi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT  DISTINCT                                                \n"
        + "   L.ID_LIVELLO,                                                  \n"
        + "   L.CODICE,                                                      \n"
        + "   L.DESCRIZIONE,                                                 \n"
        + "   L.CODICE || ' - ' || L.DESCRIZIONE AS DESCR_ESTESA,            \n"
        + "   L.ORDINAMENTO                                                  \n"
        + " FROM                                                             \n"
        + "   IUF_D_LIVELLO L,                                               \n"
        + "   IUF_R_LIVELLO_BANDO LB,                                        \n"
        + "   IUF_D_BANDO DB                                                 \n"
        + " WHERE                                                            \n"
        + "   LB.ID_LIVELLO = L.ID_LIVELLO                                   \n"
        + "   AND DB.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO     \n"
        + "   AND DB.ID_BANDO = LB.ID_BANDO                                  \n"
        + "   AND LB.ID_BANDO IS NOT NULL                                    \n"
        + "   AND DB.DATA_INIZIO <= SYSDATE                                  \n"
        + "   AND EXISTS (                                                   \n"
        + "       SELECT BO.ID_BANDO_OGGETTO FROM IUF_R_BANDO_OGGETTO BO     \n"
        + "         WHERE BO.ID_BANDO = DB.ID_BANDO AND BO.FLAG_ATTIVO = 'S' \n"
        + "         AND BO.DATA_INIZIO <= SYSDATE                            \n"
        + "         )                                                        \n"
        + " ORDER BY L.ORDINAMENTO                                           \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo, Types.VARCHAR);

    try
    {
      return queryForList(QUERY, mapParameterSource, LivelloDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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
  

  private String getQueryBandiAttivi(Boolean conEventoCalamitoso, int idProcedimentoAgricolo){
	  String QUERY_BANDI_ATTIVI =  "    FROM                                                                                           	\n"
					             + "      IUF_D_BANDO L,                                                                              \n"
					             + "      IUF_R_LIVELLO_BANDO LB,                                                                     \n"
					             + "      IUF_R_BANDO_OGGETTO BO	                                                                     \n";
	  
	if(conEventoCalamitoso){
		QUERY_BANDI_ATTIVI = QUERY_BANDI_ATTIVI+",      IUF_D_EVENTO_CALAMITOSO EC                                                                  \n";
	}
	QUERY_BANDI_ATTIVI = QUERY_BANDI_ATTIVI+ "    WHERE                                                                                          	\n"
					             + "      LB.ID_BANDO = L.ID_BANDO                                                                     	\n"
					             + "	  AND L.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO"
					             + "      AND BO.ID_BANDO = L.ID_BANDO                                                                 	\n"
					             + "      AND BO.FLAG_ATTIVO = 'S'                                                                     	\n"
					             + "      AND L.FLAG_MASTER <> 'S'                                                                     	\n";
	if(conEventoCalamitoso){
		QUERY_BANDI_ATTIVI = QUERY_BANDI_ATTIVI+"      AND L.ID_EVENTO_CALAMITOSO = EC.ID_EVENTO_CALAMITOSO                                         	\n";
	}
	QUERY_BANDI_ATTIVI = QUERY_BANDI_ATTIVI+ "   AND L.DATA_INIZIO <= SYSDATE                                  									\n"
					             + "   AND EXISTS (                                                   									\n"
					             + "       SELECT BO2.ID_BANDO_OGGETTO FROM IUF_R_BANDO_OGGETTO BO2     								\n"
					             + "         WHERE BO2.ID_BANDO = L.ID_BANDO AND BO2.FLAG_ATTIVO = 'S' 									\n"
					             + "               AND BO2.DATA_INIZIO <= SYSDATE	 													\n"
					             + "         )                                                        									\n"
					             + "      AND LB.ID_BANDO IS NOT NULL                                                                  	\n";
	 
	  return QUERY_BANDI_ATTIVI;
  }
  
  public List<BandoDTO> getBandiAttivi(long[] lIdLivelli,long[] lIdEventi, int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getLivelliAttivi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
	          "  SELECT DISTINCT                                                                                  \n"
            + "      L.ID_BANDO,                                                                                  \n"
            + "      L.DENOMINAZIONE,                                                                             \n"
            + "      L.DATA_INIZIO,                                                                               \n"
            + "      L.DATA_FINE,                                                                                 \n"
            + "      L.FLAG_MASTER,                                                                               \n"
            + "      L.ANNO_CAMPAGNA,                                                                             \n"
            + "		 L.ID_EVENTO_CALAMITOSO,																	  \n");
    if (lIdEventi != null && lIdEventi.length > 0)
    {
    	QUERY.append( "		 EC.DESCRIZIONE AS DESC_EVENTO,													  \n"
    				+ "		 EC.DATA_EVENTO,													  			  \n");
    }
    QUERY.append(  "      DECODE(L.ANNO_CAMPAGNA,NULL,'', L.ANNO_CAMPAGNA || ' - ') || L.DENOMINAZIONE || ' - data apertura: ' || TO_CHAR(TRUNC(L.DATA_INIZIO),\'DD/mm/YYYY\')  AS DESCRIZIONE, \n"
            	 + "      L.FLAG_TITOLARITA_REGIONALE                                                                  \n");
    	
    if (lIdEventi != null && lIdEventi.length > 0)
    {
    	QUERY.append(getQueryBandiAttivi(Boolean.TRUE, idProcedimentoAgricolo));
    }else{
    	QUERY.append(getQueryBandiAttivi(Boolean.FALSE, idProcedimentoAgricolo));
    }

    if (lIdLivelli != null && lIdLivelli.length > 0)
    {
    	QUERY.append(" " + getInCondition("LB.ID_LIVELLO", lIdLivelli) + " ");
    }
    
    if(lIdEventi != null && lIdEventi.length > 0)
    {
    	QUERY.append(" " + getInCondition("L.ID_EVENTO_CALAMITOSO", lIdEventi) + " ");
    }
    
	QUERY.append("   ORDER BY L.ANNO_CAMPAGNA, L.DENOMINAZIONE       \n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo);
    try
    {
      return queryForList(QUERY.toString(), mapParameterSource, BandoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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
  
	public List<EventiDTO> getEventiAttivi(long[] lIdLivelli, int idProcedimentoAgricolo) throws InternalUnexpectedException
	{
		String THIS_METHOD = "[" + THIS_CLASS + "::getLivelliAttivi]";
		if (logger.isDebugEnabled())
		{
			logger.debug(THIS_METHOD + " BEGIN.");
		}
		final StringBuffer QUERY = new StringBuffer(
	          	"  SELECT DISTINCT                                                                                  \n"
              + "		 L.ID_EVENTO_CALAMITOSO,																	  \n"	
              + "		 EC.DESCRIZIONE AS DESC_EVENTO,													  \n"	
              + "		 EC.DATA_EVENTO													  						  \n");	
		QUERY.append(getQueryBandiAttivi(Boolean.TRUE, idProcedimentoAgricolo));


		if (lIdLivelli != null && lIdLivelli.length > 0)
		{
			QUERY.append(" " + getInCondition("LB.ID_LIVELLO", lIdLivelli) + " ");
		}

		QUERY.append("   ORDER BY EC.DATA_EVENTO, EC.DESCRIZIONE       \n");

		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo);
		try
		{
			return queryForList(QUERY.toString(), mapParameterSource, EventiDTO.class);
		} 
		catch (Throwable t)
		{
			InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {},
					new LogVariable[] {}, QUERY.toString(), mapParameterSource);
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

  public List<AmmCompetenzaDTO> getAmministrazioniAttive(
      long[] lIdBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getAmministrazioniAttive]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        " SELECT DISTINCT                                 				\n"
            + "   A.ID_AMM_COMPETENZA,                          	\n"
            + "   DECODE(A.DENOMINAZIONE_1,NULL, A.DESCRIZIONE, A.DESCRIZIONE || ' - ' || A.DENOMINAZIONE_1) AS DESCRIZIONE,   \n"
            + "   A.DESC_ESTESA_TIPO_AMMINISTRAZ                	\n"
            + "  FROM                                           	\n"
            + "   SMRCOMUNE_V_AMM_COMPETENZA A,                 	\n"
            + "   IUF_T_PROCEDIM_AMMINISTRAZIO B,             	\n"
            + "   IUF_T_PROCEDIMENTO C                          	\n"
            + "  WHERE                                          	\n"
            + "   A.ID_AMM_COMPETENZA = B.EXT_ID_AMM_COMPETENZA 	\n"
            + "   AND B.ID_PROCEDIMENTO = C.ID_PROCEDIMENTO     	\n"
            + "   AND B.DATA_FINE IS NULL                       	\n");
    if (lIdBando != null && lIdBando.length > 0)
      QUERY.append(" " + getInCondition("C.ID_BANDO", lIdBando) + " ");
    QUERY.append("   ORDER BY DESCRIZIONE       \n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(QUERY.toString(), mapParameterSource,
          AmmCompetenzaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public List<ProcedimentoDTO> getStatiProcedimentiAttivi(
     long[] lIdLivelli, 
     long[] lIdBando,
     long[] lIdAmministrazioni) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getStatiProcedimentiAttivi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        "	WITH PROC_BOZZA_BANDI_SCAD AS (          		\n"
            + "		SELECT           							\n"
            + "			DISTINCT ID_PROCEDIMENTO          		\n"
            + "		FROM           								\n"
            + "			IUF_T_PROCEDIMENTO P,           		\n"
            + "			IUF_D_BANDO B          					\n"
            + "		WHERE           							\n"
            + "			ID_STATO_OGGETTO < 10          			\n"
            + " 		AND P.ID_BANDO = B.ID_BANDO           	\n"
            + " 		AND B.DATA_FINE < SYSDATE	           	\n"
            + "		)          									\n"
            + " SELECT DISTINCT                                 \n"
            + "   A.ID_STATO_OGGETTO,                           \n"
            + "   A.DESCRIZIONE AS DESCR_STATO_OGGETTO          \n"
            + " FROM                                            \n"
            + "   IUF_D_STATO_OGGETTO A,                        \n"
            + "   IUF_T_PROCEDIMENTO B,                         \n"
            + "   IUF_D_BANDO DB,                               \n"
            + "   IUF_R_LIVELLO_BANDO LB,                       \n"
            + "   IUF_T_PROCEDIM_AMMINISTRAZIO PA             \n"
            + " WHERE                                           \n"
            + "   A.ID_STATO_OGGETTO = B.ID_STATO_OGGETTO       \n"
            + "   AND DB.ID_BANDO = B.ID_BANDO                  \n"
            + "   AND LB.ID_BANDO = B.ID_BANDO                  \n"
            + "   AND PA.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO    \n"
            + "   AND PA.DATA_FINE IS NULL				        \n"
            + "	  AND B.ID_PROCEDIMENTO NOT IN (SELECT ID_PROCEDIMENTO FROM PROC_BOZZA_BANDI_SCAD)	");
    if (lIdLivelli != null && lIdLivelli.length > 0)
      QUERY.append(" " + getInCondition("LB.ID_LIVELLO", lIdLivelli) + " ");
    if (lIdBando != null && lIdBando.length > 0)
      QUERY.append(" " + getInCondition("B.ID_BANDO", lIdBando) + " ");
    if (lIdAmministrazioni != null && lIdAmministrazioni.length > 0)
      QUERY.append(
          " " + getInCondition("PA.EXT_ID_AMM_COMPETENZA", lIdAmministrazioni)
              + " ");

    QUERY.append(" ORDER BY A.DESCRIZIONE ");
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(QUERY.toString(), mapParameterSource,
          ProcedimentoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public List<GruppoOggettoDTO> getOggettiProcedimentiAttivi(
      long[] lIdLivelli, 
      long[] lIdBando,
      long[] lIdAmministrazioni, 
      long[] lIdStati,
      UtenteAbilitazioni utenteAbilitazioni) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getOggettiProcedimentiAttivi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuffer QUERY = new StringBuffer(
        "	WITH PROC_BOZZA_BANDI_SCAD AS (          						\n"
            + "		SELECT           											\n"
            + "			DISTINCT ID_PROCEDIMENTO          						\n"
            + "		FROM           												\n"
            + "			IUF_T_PROCEDIMENTO P,           						\n"
            + "			IUF_D_BANDO B          									\n"
            + "		WHERE           											\n"
            + "			ID_STATO_OGGETTO < 10          							\n"
            + " 		AND P.ID_BANDO = B.ID_BANDO           					\n"
            + " 		AND B.DATA_FINE < SYSDATE						        \n"
            + "		)          													\n"

            + " SELECT 	DISTINCT                      		  					\n"
            + "   A.ID_OGGETTO,                                               	\n"
            + "   A.CODICE AS COD_OGGETTO,                                    	\n"
            + "   A.DESCRIZIONE AS DESCR_OGGETTO,                             	\n"
            + "   A.FLAG_ISTANZA,				                             	\n"
            + "   NVL(E.ID_ESITO,-1) AS ID_ESITO,             	              	\n"
            + "   NVL(E.CODICE, '00') AS CODICE,                              	\n"
            + "   NVL(E.DESCRIZIONE, 'Aperto') AS DESCRIZIONE,                	\n"
            + "   SO.DESCRIZIONE AS DESCR_STATO_GRUPPO,  				  	  	\n"
            + "   SO.ID_STATO_OGGETTO AS ID_STATO_GRUPPO,  					  	\n"
            + "   GO.ORDINE,  									  			  	\n"
            + "	  GO.DESCRIZIONE DESCR_GO, 									  	\n"
            + "	  GO.ID_GRUPPO_OGGETTO,    									  	\n"
            + "   D.ORDINE, D.ID_LEGAME_GRUPPO_OGGETTO                    	  	\n");
    if (utenteAbilitazioni != null
        && (utenteAbilitazioni.getRuolo().isUtenteIntermediario()
            || isBeneficiario(utenteAbilitazioni)))
    {
      QUERY.append(" , (	SELECT  									\n"
          + "					NVL(OI.ID_OGGETTO,-1)  					\n"
          + "				FROM  										\n"
          + "					IUF_R_OGGETTO_ICONA OI  				\n"
          + "				WHERE  										\n"
          + "					OI.ID_ICONA = 23  						\n"
          + "					AND OI.ID_OGGETTO = A.ID_OGGETTO)  		\n"
          + "				AS ID_OGG_APPROV 						 	\n");
    }
    QUERY.append(" FROM                                              	\n"
        + "   IUF_D_OGGETTO A,                                            	\n"
        + "   IUF_D_GRUPPO_OGGETTO GO,                                    	\n"
        + "   IUF_T_PROCEDIMENTO B,                                       	\n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO C,                               	\n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO D,                              	\n"
        + "   IUF_T_ITER_PROCEDIMENTO_GRUP IPG,                         	\n"
        + "   IUF_D_STATO_OGGETTO SO,			                          	\n"
        + "   IUF_R_LIVELLO_BANDO LB,                         			  	\n"
        + "   IUF_D_ESITO E,                                              	\n"
        + "   IUF_T_PROCEDIM_AMMINISTRAZIO PA               			  	\n"
        + " WHERE                                                         	\n"
        + "   C.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO                       	\n"
        + "	  AND B.ID_PROCEDIMENTO NOT IN (SELECT ID_PROCEDIMENTO FROM PROC_BOZZA_BANDI_SCAD)	\n" // no
                                                                                                    // procedimenti
                                                                                                    // in
                                                                                                    // bozza
                                                                                                    // se
                                                                                                    // il
                                                                                                    // bando
                                                                                                    // è
                                                                                                    // scaduto
        + "   AND GO.ID_GRUPPO_OGGETTO = D.ID_GRUPPO_OGGETTO              	\n"
        + "   AND C.ID_LEGAME_GRUPPO_OGGETTO = D.ID_LEGAME_GRUPPO_OGGETTO 	\n"
        + "   AND E.ID_ESITO(+) = C.ID_ESITO                              	\n"
        + "   AND LB.ID_BANDO = B.ID_BANDO                    			  	\n"
        + "   AND IPG.ID_PROCEDIMENTO (+)= B.ID_PROCEDIMENTO		  	  	\n"
        + "   AND (IPG.CODICE_RAGGRUPPAMENTO IS NULL OR IPG.CODICE_RAGGRUPPAMENTO = C.CODICE_RAGGRUPPAMENTO)  	  	\n"
        + "   AND IPG.ID_STATO_OGGETTO = SO.ID_STATO_OGGETTO (+)	  	  	\n"
        + "   AND IPG.DATA_FINE IS NULL						   		  	  	\n"
        + "   AND PA.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO      			  	\n"
        + "   AND PA.DATA_FINE IS NULL					      			  	\n"
        + "   AND D.ID_OGGETTO = A.ID_OGGETTO                             	\n");

    if (lIdLivelli != null && lIdLivelli.length > 0)
      QUERY.append(" " + getInCondition("LB.ID_LIVELLO", lIdLivelli) + " ");
    if (lIdBando != null && lIdBando.length > 0)
      QUERY.append(" " + getInCondition("B.ID_BANDO", lIdBando) + " ");
    if (lIdStati != null && lIdStati.length > 0)
      QUERY.append(" " + getInCondition("B.ID_STATO_OGGETTO", lIdStati) + " ");
    if (lIdAmministrazioni != null && lIdAmministrazioni.length > 0)
      QUERY.append(
          " " + getInCondition("PA.EXT_ID_AMM_COMPETENZA", lIdAmministrazioni)
              + " ");

    QUERY.append(
        " ORDER BY GO.ID_GRUPPO_OGGETTO, GO.ORDINE, A.ID_OGGETTO, SO.ID_STATO_OGGETTO, D.ID_LEGAME_GRUPPO_OGGETTO, D.ORDINE");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY.toString(),
          mapParameterSource, new ResultSetExtractor<List<GruppoOggettoDTO>>()
          {
            @Override
            public List<GruppoOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GruppoOggettoDTO> listGruppi = new ArrayList<GruppoOggettoDTO>();
              List<EsitoOggettoDTO> listStatiGruppo = null;

              ArrayList<OggettoDTO> listOggetti = new ArrayList<OggettoDTO>();
              List<EsitoOggettoDTO> listEsitiOggetto = null;
              OggettoDTO oggettoDTO = null;
              EsitoOggettoDTO esitoOggettoDTO = null;
              GruppoOggettoDTO gruppoDTO = null;

              long idOggettoLast = -1;
              long idGruppoLast = -1;
              long idGruppo;
              long idStato, idStatoOld = -1;

              long idOggetto;
              long idOggAppr = 0;
              long idEsito;
              String flagIstanza = "";
              while (rs.next())
              {
                idGruppo = rs.getLong("ID_GRUPPO_OGGETTO");
                if (idGruppo != idGruppoLast)
                {
                  idGruppoLast = idGruppo;
                  gruppoDTO = new GruppoOggettoDTO();
                  gruppoDTO.setIdGruppoOggetto(idGruppo);
                  gruppoDTO.setDescrizione(rs.getString("DESCR_GO"));
                  listStatiGruppo = new ArrayList<EsitoOggettoDTO>();
                  EsitoOggettoDTO statoGruppo = new EsitoOggettoDTO();
                  statoGruppo.setIdEsito(0);
                  statoGruppo.setDescrizione("Non Presente");
                  listStatiGruppo.add(statoGruppo);
                  gruppoDTO.setStati(listStatiGruppo);
                  listGruppi.add(gruppoDTO);
                  idStatoOld = -2;
                  listOggetti = new ArrayList<>();
                }

                idStato = rs.getLong("ID_STATO_GRUPPO");
                if (idStato != idStatoOld && idStato != 0)
                {
                  idStatoOld = idStato;
                  EsitoOggettoDTO statoGruppo = new EsitoOggettoDTO();
                  statoGruppo.setIdEsito(rs.getLong("ID_STATO_GRUPPO"));
                  statoGruppo
                      .setDescrizione(rs.getString("DESCR_STATO_GRUPPO"));
                  if (!listStatiGruppo.contains(statoGruppo))
                    listStatiGruppo.add(statoGruppo);
                }

                idOggetto = rs.getLong("ID_OGGETTO");

                if (utenteAbilitazioni != null
                    && (utenteAbilitazioni.getRuolo().isUtenteIntermediario()
                        || isBeneficiario(utenteAbilitazioni)))
                {
                  idOggAppr = rs.getLong("ID_OGG_APPROV");
                  idEsito = rs.getLong("ID_ESITO");
                  flagIstanza = rs.getString("FLAG_ISTANZA");

                  // per le NON istanze
                  if ("N".equals(flagIstanza))
                    if (idOggAppr > 0)
                    {
                      if (idEsito != 0 && idEsito > 0 && idEsito != 26
                          && idEsito != 27 && idEsito != 28 && idEsito != 36
                          && idEsito != 37)
                      {
                        continue;
                      }
                    }
                }

                if (idOggetto != idOggettoLast)
                {
                  idOggettoLast = idOggetto;
                  oggettoDTO = new OggettoDTO();
                  oggettoDTO.setIdOggetto(idOggetto);
                  oggettoDTO.setCodice(rs.getString("COD_OGGETTO"));
                  oggettoDTO
                      .setDescrizioneGruppoOggetto(rs.getString("DESCR_GO"));
                  oggettoDTO.setDescrizione(rs.getString("DESCR_OGGETTO"));
                  oggettoDTO.setIdLegameGruppoOggetto(
                      rs.getLong("ID_LEGAME_GRUPPO_OGGETTO"));

                  listEsitiOggetto = new ArrayList<EsitoOggettoDTO>();
                  // Se beneficiario o intermediario non metto il "Non presente"
                  // per le NON istanze soggette ad approvazione
                  if (utenteAbilitazioni == null
                      || (!utenteAbilitazioni.getRuolo().isUtenteIntermediario()
                          && !isBeneficiario(utenteAbilitazioni))
                      || !"N".equals(rs.getString("FLAG_ISTANZA"))
                      || idOggAppr == 0)
                  {
                    esitoOggettoDTO = new EsitoOggettoDTO();
                    esitoOggettoDTO.setIdEsito(0);
                    esitoOggettoDTO.setCodice("0");
                    esitoOggettoDTO.setDescrizione("Non Presente");
                    listEsitiOggetto.add(esitoOggettoDTO);
                  }

                  oggettoDTO.setEsitiOggetto(listEsitiOggetto);
                  listOggetti.add(oggettoDTO);
                }

                if (rs.getLong("ID_ESITO") != 0)
                {
                  // Se beneficiario non metto l'esito "Aperto" per le NON
                  // istanze soggette ad approvazione
                  if (utenteAbilitazioni != null
                      && (utenteAbilitazioni.getRuolo().isUtenteIntermediario()
                          || isBeneficiario(utenteAbilitazioni))
                      && "N".equals(rs.getString("FLAG_ISTANZA"))
                      && idOggAppr > 0)
                  {
                    if (rs.getLong("ID_ESITO") != -1)
                    {
                      esitoOggettoDTO = new EsitoOggettoDTO();
                      esitoOggettoDTO.setIdEsito(rs.getLong("ID_ESITO"));
                      esitoOggettoDTO.setCodice(rs.getString("CODICE"));
                      esitoOggettoDTO
                          .setDescrizione(rs.getString("DESCRIZIONE"));
                      if (!listEsitiOggetto.contains(esitoOggettoDTO))
                        listEsitiOggetto.add(esitoOggettoDTO);
                    }
                  }
                  else
                  {
                    esitoOggettoDTO = new EsitoOggettoDTO();
                    esitoOggettoDTO.setIdEsito(rs.getLong("ID_ESITO"));
                    esitoOggettoDTO.setCodice(rs.getString("CODICE"));
                    esitoOggettoDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                    if (!listEsitiOggetto.contains(esitoOggettoDTO))
                      listEsitiOggetto.add(esitoOggettoDTO);
                  }
                }
                gruppoDTO.setOggetti(listOggetti);
              }

              return listGruppi.isEmpty() ? null : listGruppi;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  @SuppressWarnings("unchecked")
  public List<Long> searchIdProcedimenti(RicercaProcedimentiVO vo,
      UtenteAbilitazioni utenteAbilitazioni, String orderColumn,
      String orderType) throws InternalUnexpectedException
  {
    String THIS_METHOD = "searchIdProcedimenti";
    StringBuilder SELECT = new StringBuilder();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      // Gestione ORDER BY
      String orderbyCol = "";
      String orderbySep = "";
      if (orderColumn != null)
      {
        orderbySep = " , ";
        if (orderColumn.equals("identificativo"))
          orderbyCol = " P.IDENTIFICATIVO ";
        else
          if (orderColumn.equals("descrAmmCompetenza"))
            orderbyCol = " VAM.DESCRIZIONE ";
          else
            if (orderColumn.equals("annoCampagna"))
              orderbyCol = " B.ANNO_CAMPAGNA ";
            else
              if (orderColumn.equals("denominazioneBando"))
                orderbyCol = " B.DENOMINAZIONE ";
              else
                if (orderColumn.equals("descrizione"))
                  orderbyCol = " SO.DESCRIZIONE ";
                else
                  if (orderColumn.equals("dataUltimoAggiornamento"))
                    orderbyCol = " P.DATA_ULTIMO_AGGIORNAMENTO ";
                  else
                    if (orderColumn.equals("cuaa"))
                      orderbyCol = " VA.CUAA ";
                    else
                      if (orderColumn.equals("denominazioneAzienda"))
                        orderbyCol = " VA.DENOMINAZIONE ";
                      else
                        if (orderColumn.equals("indirizzoSedeLegale"))
                          orderbyCol = " VA.INDIRIZZO_SEDE_LEGALE ";
                        else
                          if (orderColumn.equals("denominzioneDelega"))
                            orderbyCol = " VD.DENOMINAZIONE ";
      }

      if (vo.getIstanzaDataA() != null || vo.getIstanzaDataDa() != null)
      {
        SELECT.append(" WITH PROCEDIMENTI_ESTRATTI AS (   ");
      }

      SELECT.append(
          "  SELECT  DISTINCT                                         						\n");
      if (vo.getIstanzaDataA() != null || vo.getIstanzaDataDa() != null)
      {
        SELECT.append("  (SELECT   														         	\n"
            + "				MAX(I2.DATA_INIZIO)    										        	\n"
            + "			FROM     															       	\n"
            + "				IUF_T_PROCEDIMENTO_OGGETTO I1,  							          	\n"
            + "				IUF_T_ITER_PROCEDIMENTO_OGGE I2           							\n"
            + "			WHERE 	 														          	\n"
            + "				I1.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO  					         	\n"
            + " 			AND I1.ID_PROCEDIMENTO_OGGETTO = I2.ID_PROCEDIMENTO_OGGETTO           	\n"
            + "				AND I2.ID_STATO_OGGETTO = 10 ) AS DATA_ULTIMA_IST ,                    	\n");
      }

      SELECT.append(
          "    P.ID_PROCEDIMENTO     						             						\n"
              + orderbySep + orderbyCol
              + "  FROM                                                      						\n"
              + "    IUF_T_PROCEDIMENTO P,                                   						\n"
              + "    IUF_T_PROCEDIMENTO_OGGETTO PO,                          						\n"
              + "    IUF_T_PROCEDIMENTO_AZIENDA PA,                          						\n"
              + "    IUF_T_PROCEDIM_AMMINISTRAZIO AM,                      						\n"
              + "    SMRGAA_V_DATI_ANAGRAFICI VA,                            						\n"
              + "    SMRCOMUNE_V_AMM_COMPETENZA VAM,                         						\n"
              + "    IUF_D_BANDO B,                                          						\n"
              + "    IUF_D_EVENTO_CALAMITOSO EC,                             						\n"
              + "    IUF_D_CATEGORIA_EVENTO CE,                             						\n"
    		  );

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" SMRGAA_V_DATI_DELEGA C, 								\n");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          SELECT.append(" SMRGAA_V_SOGGETTI_COLLEGATI C, 							\n");
        }

      if (vo.getVctIdLivelli() != null && vo.getVctIdLivelli().size() > 0)
      {
        SELECT.append(" IUF_R_LIVELLO_BANDO LIV,  								\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getProvSedeLegale())
          || !GenericValidator.isBlankOrNull(vo.getComuneSedeLegale()))
      {
        SELECT.append(" SMRGAA_V_DATI_AMMINISTRATIVI VDA,  						\n");
      }
      if (vo.getMapOggetti() != null)
      {
        SELECT.append(" IUF_D_ESITO DE,  										\n");
      }

      SELECT.append(
          " IUF_D_STATO_OGGETTO SO,                                  	\n"
              + "   SMRGAA_V_DATI_DELEGA VD                                  	\n"
              + " WHERE                                                      	\n"
              + "    P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                  	\n"
              + "    AND PA.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO              	\n"
              + "    AND PA.DATA_FINE IS NULL                                	\n"
              + "    AND AM.ID_PROCEDIMENTO(+) = P.ID_PROCEDIMENTO           	\n"
              + "    AND AM.DATA_FINE(+) IS NULL                             	\n"
              + "    AND VA.ID_AZIENDA = PA.EXT_ID_AZIENDA                   	\n"
              + "    AND VA.DATA_FINE_VALIDITA IS NULL                       	\n"
              + "    AND VAM.ID_AMM_COMPETENZA(+) = AM.EXT_ID_AMM_COMPETENZA 	\n"
              + "    AND B.ID_BANDO = P.ID_BANDO                             	\n"
              // escludere i proc con stato <10 dai bandi scaduti
              + " 	 AND ((B.DATA_FINE < SYSDATE AND P.ID_STATO_OGGETTO>=10) or (nvl(B.DATA_FINE,sysdate) >= SYSDATE)) \n"
              + "    AND SO.ID_STATO_OGGETTO = P.ID_STATO_OGGETTO            	\n"
              + "    AND VD.ID_AZIENDA(+) = PA.EXT_ID_AZIENDA                	\n"
              + "    AND VD.DATA_FINE_VALIDITA(+) IS NULL                    	\n"
              + "	 AND B.ID_EVENTO_CALAMITOSO = EC.ID_EVENTO_CALAMITOSO(+)	\n"
              + "	 AND EC.ID_CATEGORIA_EVENTO = CE.ID_CATEGORIA_EVENTO(+)		\n"
              + "	 AND B.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO \n"
              + "	 AND P.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO \n"
    		  );

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" AND VA.ID_AZIENDA = C.ID_AZIENDA					  	\n"
            + " AND C.DATA_FINE_VALIDITA IS NULL					  	\n"
            + " AND ( C.ID_INTERMEDIARIO_ZONA = :ID_INTERMEDIARIO  	  	\n"
            + "		 OR C.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO   	\n"
            + "      OR C.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO ) 	\n");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          SELECT.append(" AND VA.ID_AZIENDA = C.ID_AZIENDA						\n"
              + " AND C.DATA_FINE_RUOLO IS NULL							\n"
              + " AND C.CODICE_FISCALE = :CODICE_FISCALE  				\n");
        }

      if (vo.getVctIdLivelli() != null && vo.getVctIdLivelli().size() > 0)
      {
        SELECT.append("    AND EXISTS  											\n"
            + "		(	SELECT  										\n"
            + "				ID_PROCEDIMENTO  							\n"
            + "			FROM  											\n"
            + "				IUF_R_PROCEDIMENTO_LIVELLO 					\n"
            + "			WHERE  					 						\n"
            + "				ID_PROCEDIMENTO = P.ID_PROCEDIMENTO         \n"
            + "				AND ID_LIVELLO = LIV.ID_LIVELLO)            \n");

        SELECT.append(" AND LIV.ID_BANDO = P.ID_BANDO ");
        SELECT
            .append(" " + getInCondition("LIV.ID_LIVELLO", vo.getVctIdLivelli())
                + " 						\n");
      }
      if (vo.getVctIdEventi()!= null && vo.getVctIdEventi().size() > 0)
      {
          SELECT.append(" " + getInCondition("EC.ID_EVENTO_CALAMITOSO", vo.getVctIdEventi())
          + " 							\n");    	  
      }
      if (vo.getVctIdBando() != null && vo.getVctIdBando().size() > 0)
      {
        SELECT.append(" " + getInCondition("P.ID_BANDO", vo.getVctIdBando())
            + " 							\n");
      }
      if (vo.getVctIdAmministrazione() != null
          && vo.getVctIdAmministrazione().size() > 0)
      {
        SELECT.append(" " + getInCondition("AM.EXT_ID_AMM_COMPETENZA",
            vo.getVctIdAmministrazione()) + " 	\n");
      }
      if (vo.getVctIdStatoProcedimento() != null
          && vo.getVctIdStatoProcedimento().size() > 0)
      {
        SELECT.append(" " + getInCondition("P.ID_STATO_OGGETTO",
            vo.getVctIdStatoProcedimento()) + "		\n");
      }
      /* AGGIUNTA NOTIFICHE */
      if (vo.getVctNotifiche() != null && vo.getVctNotifiche().size() > 0)
      {
        SELECT.append(" AND ( P.ID_PROCEDIMENTO IN ( "
            + queryNotifiche(vo.getVctNotifiche()) + ")   		\n");
        if (vo.getVctNotifiche().contains("99"))
          SELECT.append(" OR P.ID_PROCEDIMENTO NOT IN ( " + queryNotificheAll()
              + ")    				\n");
        SELECT.append(" )");
      }
      /* END NOTIFICHE */
      if (!GenericValidator.isBlankOrNull(vo.getIdentificativo()))
      {
        SELECT.append(
            "   AND PO.IDENTIFICATIVO = :IDENTIFICATIVO              							\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getCuaa())
          || !GenericValidator.isBlankOrNull(vo.getCuaaProcedimenti()))
      {
        SELECT.append(" AND VA.CUAA = :CUAA 															\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getPiva()))
      {
        SELECT.append(
            " AND VA.PARTITA_IVA = :PARTITA_IVA 												\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getDenominazione()))
      {
        SELECT.append(
            " AND VA.DENOMINAZIONE LIKE :DENOMINAZIONE 										\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getProvSedeLegale()))
      {
        SELECT.append(
            " AND VDA.ISTAT_PROVINCIA = SUBSTR(VA.ISTAT_COMUNE_SEDE_LEGALE,1,3)				\n");
        SELECT.append(
            " AND VDA.SIGLA_PROVINCIA = :SIGLA_PROVINCIA 										\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getComuneSedeLegale()))
      {
        SELECT.append(
            " AND VDA.ISTAT_COMUNE = VA.ISTAT_COMUNE_SEDE_LEGALE 								\n");
        SELECT.append(
            " AND VDA.DESCRIZIONE_COMUNE = :DESCRIZIONE_COMUNE 								\n");
      }

      String sep = " ";
      if (vo.getMapOggetti() != null)
      {
        SELECT.append(
            " AND DE.ID_ESITO(+) = PO.ID_ESITO   												\n");
      }

      if (vo.getMapGruppi() != null)
      {

        SELECT.append(" AND ( \n");
        Iterator<Entry<Long, Vector<Long>>> it = vo.getMapGruppi().entrySet()
            .iterator();
        int count = 0;
        String tmp = " ";
        Vector<Long> vct = null;
        while (it.hasNext())
        {
          tmp = " ";
          @SuppressWarnings("rawtypes")
          Map.Entry pair = (Map.Entry) it.next();
          if (count > 0)
          {
            sep = "OR";
            if (!GenericValidator.isBlankOrNull(vo.getTipoFiltroOggetto()))
              sep = vo.getTipoFiltroOggetto();
          }

          vct = (Vector<Long>) pair.getValue();
          if (vct.contains(new Long(0))) // Non Presente --> 0
          {
            SELECT.append(" " + sep + " NOT EXISTS ( "
                + "	SELECT "
                + "		LGO.ID_GRUPPO_OGGETTO "
                + "	FROM "
                + "		IUF_R_LEGAME_GRUPPO_OGGETTO LGO, "
                + "		IUF_T_PROCEDIMENTO_OGGETTO PO2, "
                + "		IUF_T_PROCEDIMENTO P2 "
                + "	WHERE "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO "
                + "		AND P.ID_PROCEDIMENTO = P2.ID_PROCEDIMENTO "
                + "		AND P2.ID_PROCEDIMENTO = PO2.ID_PROCEDIMENTO "
                + "		AND LGO.ID_GRUPPO_OGGETTO = " + (Long) pair.getKey()
                + "  ) \n");
          }
          else
          {
            SELECT.append(" " + sep + "  EXISTS ( "
                + "	SELECT  "
                + "		LGO2.ID_GRUPPO_OGGETTO "
                + "	FROM "
                + "		IUF_R_LEGAME_GRUPPO_OGGETTO LGO2, "
                + "		IUF_T_ITER_PROCEDIMENTO_GRUP IPG2, "
                + "		IUF_T_PROCEDIMENTO_OGGETTO PO2 "
                + "	WHERE "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO2.ID_LEGAME_GRUPPO_OGGETTO "
                + "		AND P.ID_PROCEDIMENTO = PO2.ID_PROCEDIMENTO  "
                + "		AND IPG2.CODICE_RAGGRUPPAMENTO = PO2.CODICE_RAGGRUPPAMENTO  "
                + "		AND IPG2.DATA_FINE  IS NULL "
                + "		AND IPG2.ID_PROCEDIMENTO=PO2.ID_PROCEDIMENTO "
                + "		AND LGO2.ID_GRUPPO_OGGETTO = " + (Long) pair.getKey()
                + " "
                + "		AND "
                + getInCondition("IPG2.ID_STATO_OGGETTO", vct, false) + " "
                + tmp + " ) 	\n");
          }
          count++;
        }

        if (vo.getMapOggetti() == null)
          SELECT.append(" ) \n");

      }
      if (vo.getMapOggetti() != null)
      {
        if (vo.getMapGruppi() == null)
          SELECT.append("  AND ( ");
        Iterator<Entry<Long, Vector<Long>>> it2 = vo.getMapOggetti().entrySet()
            .iterator();
        int count2 = 0;
        if (vo.getMapGruppi() != null)
          count2 = 1;
        String sep2 = "";
        String tmp2 = " ";
        Vector<Long> vct2 = null;
        while (it2.hasNext())
        {
          tmp2 = " ";
          @SuppressWarnings("rawtypes")
          Map.Entry pair = (Map.Entry) it2.next();
          if (count2 > 0)
          {
            sep2 = "OR";
            if (!GenericValidator.isBlankOrNull(vo.getTipoFiltroOggetto()))
              sep2 = vo.getTipoFiltroOggetto();
          }

          vct2 = (Vector<Long>) pair.getValue();
          if (vct2.contains(new Long(-1))) // Aperto --> -1
            tmp2 = " OR PO2.ID_ESITO IS NULL";

          if (vct2.contains(new Long(0))) // Non Presente --> 0
          {
            SELECT.append(" " + sep2 + " NOT EXISTS ( "
                + "	SELECT "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO "
                + "	FROM "
                + "		IUF_R_LEGAME_GRUPPO_OGGETTO LGO, "
                + "		IUF_T_PROCEDIMENTO_OGGETTO PO2 "
                + "	WHERE "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO "
                + "	 	and P.ID_PROCEDIMENTO = PO2.ID_PROCEDIMENTO "
                + "		AND LGO.ID_LEGAME_GRUPPO_OGGETTO = "
                + (Long) pair.getKey() + "  ) 				\n");
          }
          else
          {
            SELECT.append(" " + sep2 + " EXISTS ( "
                + "	SELECT "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO "
                + "	FROM "
                + "		IUF_R_LEGAME_GRUPPO_OGGETTO LGO, "
                + "		IUF_T_PROCEDIMENTO_OGGETTO PO2	 "
                + "	WHERE "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO "
                + "		AND P.ID_PROCEDIMENTO = PO2.ID_PROCEDIMENTO "
                + "		AND LGO.ID_LEGAME_GRUPPO_OGGETTO = "
                + (Long) pair.getKey()
                + " 	AND (" + getInCondition("PO2.ID_ESITO", vct2, false) + " "
                + tmp2 + " ) )		 \n");
          }
          count2++;
        }
        SELECT.append(" ) \n");
      }

      if (vo.getVctFlagEstrazione() != null
          && vo.getVctFlagEstrazione().size() > 0)
      {
      	if(!IuffiUtils.VALIDATION.isValidVectorOfFlags(vo.getVctFlagEstrazione()))
      	{
      		  throw new ApplicationException("Invalid flags [10021]",10021); 
      	}
    	SELECT.append(" AND (   \n");
        SELECT.append(" EXISTS ( "
            + "	SELECT  "
            + "		A.ID_PROCEDIMENTO_ESTRATTO "
            + "	FROM "
            + "		IUF_T_PROCEDIMENTO_ESTRATTO A, "
            + "		IUF_T_ESTRAZIONE_CAMPIONE B "
            + "	WHERE "
            + "		A.ID_ESTRAZIONE_CAMPIONE = B.ID_ESTRAZIONE_CAMPIONE "
            + "		AND B.FLAG_APPROVATA = 'S' "
            + "		AND DATA_ANNULLAMENTO IS NULL "
            + "		AND ( A.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO  OR"
            + "		A.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO)	"
            + getInCondition("A.FLAG_ESTRATTA", vo.getVctFlagEstrazione())
            + " )   \n");

        SELECT.append(" )   \n");
      }

      if (vo.getVctFlagEstrazioneExPost() != null
          && vo.getVctFlagEstrazioneExPost().size() > 0)
      {
      	if(!IuffiUtils.VALIDATION.isValidVectorOfFlags(vo.getVctFlagEstrazioneExPost()))
      	{
      		  throw new ApplicationException("Invalid flags [10022]",10022); 
      	}
    	
    	SELECT.append(" AND (   \n");
        SELECT.append(" EXISTS ( "
            + "	SELECT  "
            + "		A.ID_PROCEDIMENTO_ESTR_EXPOST "
            + "	FROM "
            + "		IUF_T_PROCEDIMENTO_ESTR_EXPO A, "
            + "		IUF_T_ESTRAZIONE_CAMPIONE B "
            + "	WHERE "
            + "		A.ID_ESTRAZIONE_CAMPIONE = B.ID_ESTRAZIONE_CAMPIONE "
            + "		AND B.FLAG_APPROVATA = 'S' "
            + "		AND DATA_ANNULLAMENTO IS NULL "
            + "		AND ( A.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO  OR"
            + "		A.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO)	"
            + getInCondition("A.FLAG_ESTRATTA", vo.getVctFlagEstrazioneExPost())
            + " )   \n");

        SELECT.append(" )   \n");
      }

      /* WORKAROUND TEMPORANEO by Einaudi */
      /*
       * Dobbiamo impedire ai gal di vedere le pratiche dei bandi in cui possono
       * loro stessi presentare domanda come beneficiari, per questo motivo
       * abbiamo messo temporaneamente un parametro con l'elenco degli ID_BANDO
       * da escludere dalla ricerca (separati da virgola in modo che possa
       * essere "schiantato" in una NOT IN). Nel caso quindi che l'utente sia un
       * gal aggiungiamo una NOT IN con gli id_bando a cui non può accedere
       */
      if (utenteAbilitazioni.isUtenteGAL())
      {
        Map<String, String> mapParametri = getParametri(new String[]
        { "ESCLUDI_BANDI_GAL" });
        String escludiBandiGal = mapParametri == null ? null
            : mapParametri.get("ESCLUDI_BANDI_GAL");
        SELECT.append(" AND P.ID_BANDO NOT IN (").append(escludiBandiGal)
            .append(")");
      }

      // ultima istanza trasmessa da - a FINE

      // Gestione ORDER BY
      if (orderColumn != null)
      {
        SELECT.append(" ORDER BY " + orderbyCol);
        if (orderType != null && orderType.equals("ASC"))
          SELECT.append(" ASC ");
        else
          SELECT.append(" DESC ");
      }

      if (vo.getIstanzaDataA() != null || vo.getIstanzaDataDa() != null)
      {
        SELECT.append(" ) SELECT * FROM PROCEDIMENTI_ESTRATTI ");

        // ultima istanza trasmessa da - a INI
        SELECT.append(" WHERE    \n");
        if (vo.getIstanzaDataA() != null && vo.getIstanzaDataDa() != null)
        {
          SELECT.append(
              " TRUNC(DATA_ULTIMA_IST) BETWEEN TRUNC(:DATA_ISTANZA_DA) AND  TRUNC(:DATA_ISTANZA_A)  \n");
        }
        else
          if (vo.getIstanzaDataA() != null)
          {
            SELECT.append(
                " TRUNC(DATA_ULTIMA_IST) <=  TRUNC(:DATA_ISTANZA_A)  \n");
          }
          else
            if (vo.getIstanzaDataDa() != null)
            {
              SELECT.append(
                  " TRUNC(DATA_ULTIMA_IST) >=  TRUNC(:DATA_ISTANZA_DA)  \n");
            }
        SELECT.append("    \n");
      }

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      if (!GenericValidator.isBlankOrNull(vo.getIdentificativo()))
      {
        parameterSource.addValue("IDENTIFICATIVO",
            vo.getIdentificativo().trim().toUpperCase(), Types.VARCHAR);
      }
      if (!GenericValidator.isBlankOrNull(vo.getCuaa()))
      {
        parameterSource.addValue("CUAA", vo.getCuaa().trim().toUpperCase(),
            Types.VARCHAR);
      }
      if (!GenericValidator.isBlankOrNull(vo.getCuaaProcedimenti()))
      {
        parameterSource.addValue("CUAA",
            vo.getCuaaProcedimenti().trim().toUpperCase(), Types.VARCHAR);
      }

      if (vo.getIstanzaDataA() != null)
      {
        parameterSource.addValue("DATA_ISTANZA_A", vo.getIstanzaDataA(),
            Types.DATE);
      }

      if (vo.getIstanzaDataDa() != null)
      {
        parameterSource.addValue("DATA_ISTANZA_DA", vo.getIstanzaDataDa(),
            Types.DATE);
      }

      if (!GenericValidator.isBlankOrNull(vo.getPiva()))
      {
        parameterSource.addValue("PARTITA_IVA",
            vo.getPiva().trim().toUpperCase(), Types.VARCHAR);
      }
      if (!GenericValidator.isBlankOrNull(vo.getProvSedeLegale()))
      {
        parameterSource.addValue("SIGLA_PROVINCIA",
            vo.getProvSedeLegale().trim().toUpperCase(), Types.VARCHAR);
      }
      if (!GenericValidator.isBlankOrNull(vo.getComuneSedeLegale()))
      {
        parameterSource.addValue("DESCRIZIONE_COMUNE",
            vo.getComuneSedeLegale().trim().toUpperCase(), Types.VARCHAR);
      }
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        parameterSource.addValue("ID_INTERMEDIARIO", utenteAbilitazioni
            .getEnteAppartenenza().getIntermediario().getIdIntermediario(),
            Types.NUMERIC);
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          parameterSource.addValue("CODICE_FISCALE",
              utenteAbilitazioni.getCodiceFiscale().trim().toUpperCase(),
              Types.VARCHAR);
        }

      if (!GenericValidator.isBlankOrNull(vo.getDenominazione()))
      {
        parameterSource.addValue("DENOMINAZIONE",
            "%" + IuffiUtils.STRING.normalizeString(
                vo.getDenominazione().trim().toUpperCase(), " ") + "%",
            Types.VARCHAR);
      }

      parameterSource.addValue("ATTORE",
          IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni),
          Types.VARCHAR);
      parameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO",
              utenteAbilitazioni.getIdProcedimento(),
              Types.NUMERIC);
                

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
                long idPOld = rs.getLong("ID_PROCEDIMENTO");

                /*
                 * controllo se ho già inserito in lista questo id non mi serve
                 * sapere quanti sono, solo se sono uno o più di uno quindi
                 * appena trovo il secondo lo inserisco ed esco
                 */
                if (!list.contains(idPOld))
                  list.add(idPOld);

                if (list.size() > 1)
                  return list;
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

  public List<ProcedimentoOggettoVO> getDettaglioProcedimentiOggettiById(
      Vector<Long> vIdProcedimento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDettaglioProcedimentiOggettiById";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT.append(
          " SELECT                                      		 							\n"
              + "   PO.IDENTIFICATIVO,                                   							\n"
              + "   VAM.DESCRIZIONE AS DESCR_AMM_COMPETENZA,             							\n"
              + "   B.ANNO_CAMPAGNA,                                     							\n"
              + "   B.DENOMINAZIONE AS DENOMINAZIONE_BANDO,              							\n"
              + "   SO.DESCRIZIONE,                                      							\n"
              + "   P.DATA_ULTIMO_AGGIORNAMENTO,                         							\n"
              + "   VA.CUAA,                                             							\n"
              + "   VA.INDIRIZZO_SEDE_LEGALE,                            							\n"
              + "   VA.DENOMINAZIONE AS DENOMINAZIONE_AZIENDA,           							\n"
              + "   VD.DENOMINAZIONE AS DENOMINAZIONE_DELEGA,            							\n"
              + "   PO.ID_PROCEDIMENTO_OGGETTO,                          							\n"
              + "   '" + IuffiConstants.PAGINATION.AZIONI.DETTAGLIO
              + "' AS AZIONE,                     	\n"
              + "   'Visualizza Dettaglio' AS TITLE_HREF,                           			  	\n"
              + "   '../cuiuffi129/index_' || PO.ID_PROCEDIMENTO || '.do' AS AZIONE_HREF 		  	\n"
              + "  FROM                                                     						\n"
              + "    IUF_T_PROCEDIMENTO P,                                   						\n"
              + "    IUF_T_PROCEDIMENTO_OGGETTO PO,                          						\n"
              + "    IUF_T_PROCEDIMENTO_AZIENDA PA,                          						\n"
              + "    IUF_T_PROCEDIM_AMMINISTRAZIO AM,                      						\n"
              + "    SMRGAA_V_DATI_ANAGRAFICI VA,                            						\n"
              + "    SMRCOMUNE_V_AMM_COMPETENZA VAM,                         						\n"
              + "    IUF_D_BANDO B,                                          						\n"
              + "    IUF_D_STATO_OGGETTO SO,                                 						\n"
              + "    SMRGAA_V_DATI_DELEGA VD                                 						\n"
              + "  WHERE                                                     						\n"
              + "    P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                  						\n"
              + "    AND PA.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO              						\n"
              + "    AND PA.DATA_FINE IS NULL                                						\n"
              + "    AND AM.ID_PROCEDIMENTO(+) = P.ID_PROCEDIMENTO           						\n"
              + "    AND AM.DATA_FINE IS NULL                                						\n"
              + "    AND VA.ID_AZIENDA = PA.EXT_ID_AZIENDA                   						\n"
              + "    AND VA.DATA_FINE_VALIDITA IS NULL                       						\n"
              + "    AND VAM.ID_AMM_COMPETENZA(+) = AM.EXT_ID_AMM_COMPETENZA 						\n"
              + "    AND B.ID_BANDO = P.ID_BANDO                             						\n"
              + "    AND SO.ID_STATO_OGGETTO = P.ID_STATO_OGGETTO            						\n"
              + "    AND VD.ID_AZIENDA(+) = PA.EXT_ID_AZIENDA                						\n"
              + "    AND VD.DATA_FINE_VALIDITA(+) IS NULL                    						\n"
              + getInCondition("P.ID_PROCEDIMENTO", vIdProcedimento)
              + " 		 						\n");

      String decode = "";
      for (int i = 0; i < vIdProcedimento.size(); i++)
      {
        if (i == 0)
          decode = vIdProcedimento.get(i) + "," + (i + 1);
        else
          decode = decode + "," + vIdProcedimento.get(i) + "," + (i + 1);
      }
      SELECT.append(" ORDER BY DECODE(P.ID_PROCEDIMENTO," + decode + " ) ASC ");
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<ProcedimentoOggettoVO>>()
          {
            @Override
            public List<ProcedimentoOggettoVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<ProcedimentoOggettoVO> list = new ArrayList<ProcedimentoOggettoVO>();
              ProcedimentoOggettoVO procOggetto = null;
              while (rs.next())
              {
                procOggetto = new ProcedimentoOggettoVO();

                procOggetto.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                procOggetto.setDescrAmmCompetenza(
                    rs.getString("DESCR_AMM_COMPETENZA"));
                procOggetto.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                procOggetto
                    .setDenominazioneBando(rs.getString("DENOMINAZIONE_BANDO"));
                procOggetto.setDescrizione(rs.getString("DESCRIZIONE"));
                procOggetto.setDataUltimoAggiornamento(IuffiUtils.DATE
                    .formatDate(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO")));
                procOggetto.setCuaa(rs.getString("CUAA"));
                procOggetto.setIndirizzoSedeLegale(
                    rs.getString("INDIRIZZO_SEDE_LEGALE"));
                procOggetto.setDenominazioneAzienda(
                    rs.getString("DENOMINAZIONE_AZIENDA"));
                procOggetto.setDenominzioneDelega(
                    rs.getString("DENOMINAZIONE_DELEGA"));
                procOggetto.setIdProcedimentoOggetto(
                    rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
                procOggetto.setAzione(rs.getString("AZIONE"));
                procOggetto.setTitleHref(rs.getString("TITLE_HREF"));
                procOggetto.setAzioneHref(rs.getString("AZIONE_HREF"));

                list.add(procOggetto);
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

  public List<ComuneDTO> searchComuniPiemonte(String prov, String descrComune)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::searchComuni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        " SELECT  			            													\n"
            + "   ID_REGIONE,                								\n"
            + "   DESCRIZIONE_REGIONE,         								\n"
            + "   ISTAT_PROVINCIA,          									\n"
            + "   SIGLA_PROVINCIA, 											\n"
            + "	 DESCRIZIONE_PROVINCIA,										\n"
            + "	 ISTAT_COMUNE,												\n"
            + "	 DESCRIZIONE_COMUNE,										\n"
            + "	 CAP,														\n"
            + "	 FLAG_ESTERO												\n"
            + " FROM                           								\n"
            + "   SMRGAA_V_DATI_AMMINISTRATIVI 								\n"
            + " WHERE                          								\n");
    if (!GenericValidator.isBlankOrNull(prov))
      QUERY
          .append("   SIGLA_PROVINCIA LIKE :PROV_COMUNE AND								\n");
    if (!GenericValidator.isBlankOrNull(descrComune))
      QUERY.append(
          "   DESCRIZIONE_COMUNE LIKE :DESCRIZIONE_COMUNE 	AND					\n");
    QUERY.append(
        " ID_REGIONE='01' ORDER BY DESCRIZIONE_COMUNE, DESCRIZIONE_PROVINCIA       \n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("DESCRIZIONE_COMUNE",
        "%" + IuffiUtils.STRING.nvl(descrComune).trim().toUpperCase() + "%",
        Types.VARCHAR);
    mapParameterSource.addValue("PROV_COMUNE",
        "%" + IuffiUtils.STRING.nvl(prov).trim().toUpperCase() + "%",
        Types.VARCHAR);

    try
    {
      return queryForList(QUERY.toString(), mapParameterSource,
          ComuneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public List<GruppoOggettoDTO> getElencoOggetti(long idProcedimento,
      List<MacroCU> lMacroCdu, boolean oggettiChiusi,
      boolean isPerBeneficiarioOCAA, int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoOggetti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    List<String> lScdu = new ArrayList<>();
    for (MacroCU cdu : lMacroCdu)
    {
      lScdu.add(cdu.getCodice());
    }

    final StringBuffer QUERY = new StringBuffer(
        " SELECT                                \n"
            + "   PO.ID_PROCEDIMENTO,                            			     		\n"
            + "   PO.ID_PROCEDIMENTO_OGGETTO,                    			     		\n"
            + "   PO.EXT_COD_ATTORE,                    			    		 		\n"
            + "   PO.NOTE,        			            			    		 		\n"

            + "   BO.ID_BANDO_OGGETTO,                    			     	 			\n"
            + "   B.ID_TIPO_LIVELLO,                    			     	 			\n"

            + "   BO.FLAG_ATTIVO,		                    			     	 		\n"
            + "   PO.CODICE_RAGGRUPPAMENTO,                 			     	 		\n"
            + "   DGO.ID_GRUPPO_OGGETTO,                            			 		\n"
            + "   DGO.CODICE AS COD_GRUPPO_OGGETTO,                            			\n"
            + "   DGO.DESCRIZIONE AS DESCR_GRUPPO_OGGETTO,                     			\n"
            + "   DO.ID_OGGETTO,                                               			\n"
            + "   DO.CODICE AS COD_OGGETTO,                                    			\n"
            + "   DO.DESCRIZIONE AS DESCR_OGGETTO,                             			\n"
            + "   DO.FLAG_ISTANZA,				                             			\n"
            + "   DO.FLAG_AMMISSIONE,			                             			\n"
            + "   PO.IDENTIFICATIVO,                                           			\n"
            + "   PO.DATA_INIZIO,                                              			\n"
            + "   PO.DATA_FINE,                                                			\n"
            // + "(SELECT TR.DATA_INIZIO FROM IUF_T_ITER_PROCEDIMENTO_OGGE
            // TR \n"
            // + " WHERE TR.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO
            // \n"
            // + " AND TR.ID_STATO_OGGETTO = 10) AS DATA_TRASMISSIONE, \n"

            + "(SELECT MIN(PL2.DATA_AMMISSIONE) FROM IUF_R_PROCEDIMENTO_LIVELLO PL2    	\n"
            + "	  WHERE PL2.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO					 	\n"
            + "	 ) AS DATA_AMMISSIONE, 													\n"

            + " (SELECT B1.ID_PROCEDIMENTO_GRUPPO FROM IUF_T_PROCEDIMENTO_GRUPPO B1 \n"
            + " WHERE B1.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO \n"
            + " AND B1.CODICE_RAGGRUPPAMENTO = PO.CODICE_RAGGRUPPAMENTO \n"
            + " AND B1.DATA_FINE IS NULL AND B1.FLAG_GRUPPO_CHIUSO = 'S') AS FLG_GRUPPO_BLOCCATO, \n"

            + "   DE.CODICE AS COD_ESITO,                                      			\n"
            + "   DE.DESCRIZIONE AS DESCR_ESITO,                               			\n"
            + "   DSO.ID_STATO_OGGETTO,                                        			\n"
            + " (SELECT                                        							\n"
            + "		DPG.DESCRIZIONE                                        				\n"
            + "	FROM                                        							\n"
            + "		IUF_T_ITER_PROCEDIMENTO_GRUP IPG,                             	\n"
            + "		IUF_D_STATO_OGGETTO DPG                                       		\n"
            + "    WHERE                                        						\n"
            + "		DPG.ID_STATO_OGGETTO = IPG.ID_STATO_OGGETTO                     	\n"
            + "		AND IPG.DATA_FINE IS NULL                                       	\n"
            + "    	AND IPG.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                    	\n"
            + "	 	AND IPG.CODICE_RAGGRUPPAMENTO = PO.CODICE_RAGGRUPPAMENTO)			\n"
            + "  AS DESCR_STATO_GRUPPO,  												\n"
            + " (SELECT 																\n"
            + "		IPG.DATA_INIZIO 													\n"
            + "	FROM 																	\n"
            + "		IUF_T_ITER_PROCEDIMENTO_GRUP IPG									\n"
            + "    WHERE  																\n"
            + "		IPG.DATA_FINE IS NULL												\n"
            + "    	AND IPG.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO 						\n"
            + "		AND IPG.CODICE_RAGGRUPPAMENTO = PO.CODICE_RAGGRUPPAMENTO)			\n"
            + "  	AS DATA_STATO_GRUPPO,  											    \n"
            + "   DSO.DESCRIZIONE AS DESCR_STATO_OGGETTO                       			\n"
            + " FROM                                                           			\n"
            + "   IUF_T_PROCEDIMENTO P,			                             			\n"
            + "   IUF_T_PROCEDIMENTO_OGGETTO PO,                               			\n"
            + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                             			\n"
            + "   IUF_D_GRUPPO_OGGETTO DGO,                                    			\n"
            + "   IUF_D_OGGETTO DO,                                            			\n"
            + "   IUF_D_ESITO DE,                                              			\n"
            + "   IUF_T_ITER_PROCEDIMENTO_OGGE IPO,                          			\n"
            + "   IUF_D_STATO_OGGETTO DSO,                                     			\n"
            + "   IUF_R_BANDO_OGGETTO BO,	                                     		\n"
            + "   IUF_D_BANDO B				                                     		\n"

            + " WHERE                                                          			\n"
            + "   PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO   			\n"
            + "   AND P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO					 		\n"
            + "   AND B.ID_BANDO = P.ID_BANDO								 			\n"
            + "	  AND B.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO			\n"
            + "   AND DGO.ID_GRUPPO_OGGETTO = LGO.ID_GRUPPO_OGGETTO            			\n"
            + "   AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO 		\n"
            + "   AND BO.ID_BANDO = P.ID_BANDO								 			\n"
            + "   AND DO.ID_OGGETTO = LGO.ID_OGGETTO                           			\n"
            + "   AND DE.ID_ESITO(+) = PO.ID_ESITO                             			\n"
            + "   AND IPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO 			\n");

    if (oggettiChiusi)
    {
      QUERY.append(
          " AND PO.DATA_FINE IS NOT NULL                           			\n");
    }
    if (isPerBeneficiarioOCAA)
    {
      QUERY.append(
          " AND (DO.FLAG_ISTANZA = 'S' OR (DO.FLAG_ISTANZA = 'N' AND PO.DATA_FINE IS NOT NULL AND (DE.CODICE LIKE 'APP%' OR DE.CODICE LIKE 'NOLIQ%'))) ");
    }

    QUERY.append(
        "AND IPO.DATA_FINE IS NULL                                   			\n"
            + "   AND DSO.ID_STATO_OGGETTO = IPO.ID_STATO_OGGETTO              			\n"
            + "   AND PO.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                    			\n"
            + "   AND EXISTS (              											\n"
            + "		SELECT               		 										\n"
            + "			SQOA.EXT_COD_MACRO_CDU               		 					\n"
            + "		FROM              	              		               		 		\n"
            + "			IUF_R_BANDO_OGGETTO_QUADRO SBOQ,              		 			\n"
            + "			IUF_R_QUADRO_OGGETTO_AZIONE SQOA              		 			\n"
            + "		WHERE              		 											\n"
            + "			SBOQ.ID_BANDO_OGGETTO = BO.ID_BANDO_OGGETTO              		\n"
            + "			AND SQOA.ID_QUADRO_OGGETTO = SBOQ.ID_QUADRO_OGGETTO	        	\n"
            + " " + getInCondition("SQOA.EXT_COD_MACRO_CDU", lScdu)
            + " )																		\n"
            + " ORDER BY PO.CODICE_RAGGRUPPAMENTO, PO.DATA_FINE              			\n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo,
            Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY.toString(),
          mapParameterSource, new ResultSetExtractor<List<GruppoOggettoDTO>>()
          {
            @Override
            public List<GruppoOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GruppoOggettoDTO> list = new ArrayList<GruppoOggettoDTO>();
              ArrayList<OggettoDTO> oggettiList = null;
              GruppoOggettoDTO gruppoOggettoDTO = null;
              long lastIdGruppoOggetto = 0;
              long idGruppoOggetto = 0;

              long lastCodRaggruppamento = 0;
              long codRaggruppamento = 0;

              while (rs.next())
              {
                idGruppoOggetto = rs.getLong("ID_GRUPPO_OGGETTO");
                codRaggruppamento = rs.getLong("CODICE_RAGGRUPPAMENTO");

                if ((idGruppoOggetto != lastIdGruppoOggetto)
                    || (codRaggruppamento != lastCodRaggruppamento))
                {
                  lastIdGruppoOggetto = idGruppoOggetto;
                  lastCodRaggruppamento = codRaggruppamento;
                  gruppoOggettoDTO = new GruppoOggettoDTO();
                  gruppoOggettoDTO
                      .setCodice(rs.getString("COD_GRUPPO_OGGETTO"));
                  gruppoOggettoDTO
                      .setDescrizione(rs.getString("DESCR_GRUPPO_OGGETTO"));
                  gruppoOggettoDTO.setCodRaggruppamento(
                      rs.getString("CODICE_RAGGRUPPAMENTO"));
                  gruppoOggettoDTO
                      .setIdGruppoOggetto(rs.getLong("ID_GRUPPO_OGGETTO"));
                  gruppoOggettoDTO
                      .setIdProcedimento(rs.getLong("ID_PROCEDIMENTO"));
                  gruppoOggettoDTO
                      .setDescrStatoGruppo(rs.getString("DESCR_STATO_GRUPPO"));
                  gruppoOggettoDTO
                      .setDataStatoGruppo(rs.getTimestamp("DATA_STATO_GRUPPO"));
                  gruppoOggettoDTO.setGruppoBloccato(
                      rs.getString("FLG_GRUPPO_BLOCCATO") != null);
                  oggettiList = new ArrayList<OggettoDTO>();
                  gruppoOggettoDTO.setOggetti(oggettiList);
                  list.add(gruppoOggettoDTO);
                }

                OggettoDTO oggettoDTO = new OggettoDTO();
                oggettoDTO.setCodiceDomanda(rs.getString("IDENTIFICATIVO"));
                oggettoDTO.setCodice(rs.getString("COD_OGGETTO"));
                oggettoDTO.setDataFine(rs.getTimestamp("DATA_FINE"));
                oggettoDTO.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                oggettoDTO.setLastEsitoDescr(rs.getString("DESCR_ESITO"));
                oggettoDTO.setStato(rs.getString("DESCR_STATO_OGGETTO"));
                oggettoDTO.setDescrizione(rs.getString("DESCR_OGGETTO"));
                oggettoDTO.setFlagIstanza(rs.getString("FLAG_ISTANZA"));
                oggettoDTO.setIdProcedimentoOggetto(
                    rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
                oggettoDTO.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                oggettoDTO.setExtCodAttore(rs.getString("EXT_COD_ATTORE"));
                oggettoDTO.setFlagAttivo(rs.getString("FLAG_ATTIVO"));
                oggettoDTO.setNote(rs.getString("NOTE"));

                try
                {
                  // find funzionario istruttore
                  if (oggettoDTO.getDataFine() != null)
                  {
                    Long idTipoLivello = rs.getLong("ID_TIPO_LIVELLO");
                    if (idTipoLivello == 1 || idTipoLivello == 3) // INVESTIMENTO
                                                                  // E GAL
                      oggettoDTO
                          .setFunzionarioIstruttore(getFunzionarioIstruttoreInv(
                              oggettoDTO.getIdProcedimentoOggetto(),
                              idGruppoOggetto));
                    else
                      if (idTipoLivello == 2) // PREMIO
                        oggettoDTO.setFunzionarioIstruttore(
                            getFunzionarioIstruttorePremio(
                                oggettoDTO.getIdProcedimentoOggetto()));
                  }

                  if (oggettoDTO.getDataFine() == null
                      && "N".equals(oggettoDTO.getFlagIstanza()))
                    oggettoDTO.setFunzionarioIstruttore(
                        getFunzionarioIstruttoreDataFineNull(
                            oggettoDTO.getIdProcedimentoOggetto()));

                  if ("S".equals(oggettoDTO.getFlagIstanza()))
                  {
                    DecodificaDTO<String> protocollo = findProtocolloOggettoIstanza(
                        oggettoDTO.getIdProcedimentoOggetto());
                    oggettoDTO
                        .setDataTrasmissioneStr(protocollo.getDescrizione());
                    oggettoDTO.setNumeroProtocollo(protocollo.getCodice());
                  }
                  else
                  {
                    String flagApprovazione = rs.getString("FLAG_AMMISSIONE");
                    String codiceEsito = rs.getString("COD_ESITO");
                    Date dataAmmissione = rs.getTimestamp("DATA_AMMISSIONE");

                    // Per le non istanze, il numero protocollo lo prendo sempre
                    // da IUF_T_PROCEDIM_OGGETTO_STAMP
                    oggettoDTO.setNumeroProtocollo(findNumeroProtocollo(
                        oggettoDTO.getIdProcedimentoOggetto()));

                    if ("S".equals(flagApprovazione)
                        && "APP-P".equals(codiceEsito)
                        && dataAmmissione != null)
                    {
                      oggettoDTO.setDataTrasmissioneStr(
                          IuffiUtils.DATE.formatDate(dataAmmissione)); // PRS_R_PROCEDIMENTO_LIVELLO
                    }
                    else
                    {

                      if (POHasStampe(oggettoDTO.getIdProcedimentoOggetto()))
                      {
                        oggettoDTO.setDataTrasmissioneStr(
                            findDataProtocollNonIstanzaConStampe(
                                oggettoDTO.getIdProcedimentoOggetto()));// IUF_T_PROCEDIM_OGGETTO_STAMP
                        if (oggettoDTO.getDataTrasmissioneStr() == null
                            || oggettoDTO.getDataTrasmissioneStr().isEmpty()
                            || oggettoDTO.getDataTrasmissioneStr() == "")
                          oggettoDTO.setDataTrasmissioneStr(
                              findDataProtocolloNonIstanzaSenzaStampe(
                                  oggettoDTO.getIdProcedimentoOggetto()));// IUF_T_ITER_PROCEDIMENTO_OGGE

                      }
                      else
                      {
                        oggettoDTO.setDataTrasmissioneStr(
                            findDataProtocolloNonIstanzaSenzaStampe(
                                oggettoDTO.getIdProcedimentoOggetto()));// IUF_T_ITER_PROCEDIMENTO_OGGE
                      }
                    }

                  }
                }
                catch (InternalUnexpectedException e)
                {
                  e.printStackTrace();
                  return null;
                }
                oggettiList.add(oggettoDTO);
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
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public String getFunzionarioIstruttoreInv(long idProcedimentoOggetto,
      long idGruppoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getFunzionarioIstruttoreInv]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " 	SELECT 	DISTINCT																	\n"
        + "			T.NOME || ' ' || T.COGNOME AS	TECNICO											\n"
        + " 	FROM	                                                                         	\n"
        + "   		IUF_T_ESITO_TECNICO  A,															\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO PO,													\n"
        + "   		IUF_R_LEGAME_GRUPPO_OGGETTO LGO,												\n"
        + "   		IUF_R_QUADRO_OGGETTO QO,														\n"
        + "   		IUF_D_QUADRO Q,																	\n"
        + "			DB_TECNICO T																	\n"
        + " 	WHERE	                                                                        	\n"
        + "   		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\n"
        + "			AND A.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO						\n"
        + "			AND A.EXT_ID_TECNICO = T.ID_TECNICO												\n"
        + "			AND LGO.ID_LEGAME_GRUPPO_OGGETTO  = PO.ID_LEGAME_GRUPPO_OGGETTO					\n"
        + "			AND LGO.ID_GRUPPO_OGGETTO  = :ID_GRUPPO_OGGETTO									\n"
        + "			AND QO.ID_quadro = q.id_quadro													\n"
        + "			AND LGO.ID_OGGETTO  = QO.ID_OGGETTO												\n"
        + "			AND A.ID_QUADRO_OGGETTO = QO.ID_QUADRO_OGGETTO									\n"
        + "			AND Q.CODICE = 'ESIFN'															\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getString("TECNICO");
              }
              return new String();
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public String getFunzionarioIstruttorePremio(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getFunzionarioIstruttorePremio]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " 	SELECT 	DISTINCT																	\n"
        + "			T.NOME || ' ' || T.COGNOME AS	TECNICO											\n"
        + " 	FROM	                                                                         	\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO PO,													\n"
        + "			IUF_T_ESITO_FINALE E,															\n"
        + "			DB_TECNICO T																	\n"
        + " 	WHERE	                                                                        	\n"
        + "   		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\n"
        + "			AND E.EXT_ID_FUNZIONARIO_ISTRUTTORE = T.ID_TECNICO								\n"
        + "			AND E.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO						\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getString("TECNICO");
              }
              return new String();
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public String getFunzionarioIstruttoreDataFineNull(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getFunzionarioIstruttoreDataFineNull]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " 	SELECT 																				\n"
        + "			T.NOME || ' ' || T.COGNOME	AS	TECNICO											\n"
        + " 	FROM	                                                                         	\n"
        + "   		IUF_T_PROCEDIM_AMMINISTRAZIO PA,												\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO PO,													\n"
        + "			DB_TECNICO T																	\n"
        + " 	WHERE	                                                                        	\n"
        + "   		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\n"
        + "			AND PO.DATA_FINE IS NULL														\n"
        + "			AND PA.DATA_FINE IS NULL														\n"
        + "			AND PA.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO										\n"
        + "			AND PA.EXT_ID_TECNICO = T.ID_TECNICO											\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getString("TECNICO");
              }
              return new String();
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  private String findNumeroProtocollo(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::findNumeroProtocollo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "		SELECT 																				\n"
        + "			NVL(A.NUMERO_PROTOCOLLO_EMERGENZA, A.NUMERO_PROTOCOLLO) AS NUM_PROTOCOLLO		\n"
        + " 	FROM                                                                         		\n"
        + "   		IUF_T_PROCEDIM_OGGETTO_STAMP A,                                             	\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO PO,													\n"
        + "   		IUF_R_OGGETTO_ICONA B,                                                     		\n"
        + "   		DOQUIAGRI_V_TIPO_DOC C                                                     		\n"
        + " 	WHERE  		                                                                      	\n"
        + "   		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\n"
        + "   		AND A.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO						\n"
        + "   		AND A.DATA_FINE IS NULL															\n"
        + "   		AND A.ID_OGGETTO_ICONA = B.ID_OGGETTO_ICONA                                		\n"
        + "   		AND B.EXT_ID_TIPO_DOCUMENTO = C.ID_TIPO_DOCUMENTO                           	\n"
        + "   		AND C.ID_CATEGORIA_DOC = 2005 													\n"
        + "		ORDER BY NUM_PROTOCOLLO																\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getString("NUM_PROTOCOLLO");
              }
              return new String();
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  private boolean isBeneficiario(UtenteAbilitazioni utenteAbilitazioni)
  {
    return utenteAbilitazioni.getRuolo().isUtenteAziendaAgricola()
        || utenteAbilitazioni.getRuolo().isUtenteLegaleRappresentante()
        || utenteAbilitazioni.getRuolo().isUtenteTitolareCf();
  }

  @SuppressWarnings("unchecked")
  public List<ProcedimentoOggettoVO> searchProcedimenti(
      RicercaProcedimentiVO vo, UtenteAbilitazioni utenteAbilitazioni)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "searchProcedimenti";
    StringBuilder SELECT = new StringBuilder();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {

      SELECT.append(
          " WITH PROCEDIMENTI_ESTRATTI AS (   						 																			\n"
              + "  SELECT  DISTINCT                                     																			\n"
              + "   P.ID_PROCEDIMENTO,                              																				\n"
              // DATA_ULTIMA_IST
              + "  (	SELECT   																													\n"
              + "			MAX(I2.DATA_INIZIO)  																									\n"
              + "		FROM  																														\n"
              + "			IUF_T_PROCEDIMENTO_OGGETTO I1,  																						\n"
              + "			IUF_T_ITER_PROCEDIMENTO_OGGE I2  																						\n"
              + "		WHERE   																													\n"
              + "			I1.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO   																				\n"
              + " 			AND I1.ID_PROCEDIMENTO_OGGETTO = I2.ID_PROCEDIMENTO_OGGETTO 															\n"
              + "			AND I2.ID_STATO_OGGETTO = 10 ) AS DATA_ULTIMA_IST ,                                  									\n");
      // DESCR_ULTIMA_IST
      SELECT.append(
          "( 	SELECT   																											\n"
              + "					I4.DESCRIZIONE   																								\n"
              + "			   	FROM 																												\n"
              + "					IUF_T_PROCEDIMENTO_OGGETTO I1, IUF_T_ITER_PROCEDIMENTO_OGGE I2 , IUF_R_LEGAME_gRUPPO_OGGETTO I3,  			\n"
              + "						    	        IUF_D_OGGETTO I4																			\n"
              + "						    	      WHERE I1.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO     												\n"
              + "						    	      AND I1.ID_LEGAME_GRUPPO_OGGETTO = I3.ID_LEGAME_GRUPPO_OGGETTO  								\n"
              + "						    	      AND I4.ID_OGGETTO = I3.ID_OGGETTO  															\n"
              + "						    	      AND I1.ID_PROCEDIMENTO_OGGETTO = I2.ID_PROCEDIMENTO_OGGETTO AND I2.ID_STATO_OGGETTO = 10  	\n"
              + "						    	      AND I2.DATA_INIZIO = ( 	SELECT   															\n"
              + "						    	                            		MAX(II2.DATA_INIZIO)   											\n"
              + "						    	                          		FROM 																\n"
              + "																	IUF_T_PROCEDIMENTO_OGGETTO II1, 								\n"
              + "																	IUF_T_ITER_PROCEDIMENTO_OGGE II2   							\n"
              + "						    	                          		WHERE 																\n"
              + "																	II1.ID_PROCEDIMENTO = I1.ID_PROCEDIMENTO     					\n"
              + "						    	                          			AND II1.ID_PROCEDIMENTO_OGGETTO = II2.ID_PROCEDIMENTO_OGGETTO 	\n"
              + "																	AND II2.ID_STATO_OGGETTO = 10  									\n"
              + "						    	                          )  																		\n"
              + "						    	      AND I1.ID_PROCEDIMENTO_OGGETTO = 	( 															\n"
              + "																SELECT 																\n"
              + "																	MAX(X1.ID_PROCEDIMENTO_OGGETTO) 								\n"
              + "																FROM 																\n"
              + "																	IUF_T_PROCEDIMENTO_OGGETTO X1, 									\n"
              + "																	IUF_T_ITER_PROCEDIMENTO_OGGE X2 								\n"
              + "																WHERE 																\n"
              + "																	X1.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO							\n"
              + "    																AND  X1.ID_PROCEDIMENTO_OGGETTO = X2.ID_PROCEDIMENTO_OGGETTO 	\n"
              + "																	AND X2.DATA_INIZIO = (	SELECT 									\n"
              + "																								MAX(I2.DATA_INIZIO) 				\n"
              + "																						  	FROM 									\n"
              + "																								IUF_T_PROCEDIMENTO_OGGETTO I1, 		\n"
              + "																								IUF_T_ITER_PROCEDIMENTO_OGGE I2	\n"
              + "																							 WHERE 									\n"
              + "																								I1.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO 							\n"
              + " 																								AND I1.ID_PROCEDIMENTO_OGGETTO = I2.ID_PROCEDIMENTO_OGGETTO 	\n"
              + "																								AND I2.ID_STATO_OGGETTO = 10 ))									\n"
              + "						    	      ) AS DESCR_ULTIMA_IST , 							\n"
              // ALTRA ROBA
              + "   DLIV.ID_LIVELLO,                                     								\n"
              + "   DLIV.CODICE,                                         								\n"
              + "   DLIV.CODICE_LIVELLO,                                 								\n"
              + "   DLIV.CODICE_LIVELLO AS COD_LIVELLO,                  								\n"
              + "   PLIV.IMPORTO_INVESTIMENTO,                           								\n"
              + "   PLIV.SPESA_AMMESSA,                                  								\n"
              + "   PLIV.CONTRIBUTO_CONCESSO,                            								\n"
              + " (SELECT                                                								\n"
              + "   SUM(A.IMPORTO_LIQUIDATO)                             								\n"
              + " FROM                                                   								\n"
              + "   IUF_T_IMPORTI_LIQUIDATI A,                           								\n"
              + "   IUF_R_LISTA_LIQUIDAZ_IMP_LIQ B                       								\n"
              + " WHERE                                                  								\n"
              + "   A.ID_PROCEDIMENTO_OGGETTO IN ( 	SELECT ID_PROCEDIMENTO_OGGETTO 						\n"
              + "									FROM IUF_T_PROCEDIMENTO_OGGETTO 					\n"
              + "									WHERE ID_PROCEDIMENTO =  P.ID_PROCEDIMENTO) 		\n"
              + "   AND A.ID_IMPORTI_LIQUIDATI = B.ID_IMPORTI_LIQUIDATI  								\n"
              + "   AND B.FLAG_ESITO_LIQUIDAZIONE = 'S')                 								\n"
              + "	AS IMPORTO_LIQUIDATO,								 								\n"
              + " (SELECT                                                								\n"
              + "   A.NOME || ' ' || A.COGNOME                           								\n"
              + " FROM                                                   								\n"
              + "   DB_TECNICO A                            			 								\n"
              + " WHERE                                                  								\n"
              + "   A.ID_TECNICO = AM.EXT_ID_TECNICO)					 								\n"
              + "	AS TECNICO_ISTRUTTORE,								 								\n"
              + "  VAM.RESPONSABILE AS RESPONSABILE_PROCEDIMENTO,        								\n"
              + "   P.IDENTIFICATIVO,                                    								\n"
              + "   VAM.DESCRIZIONE AS DESCR_AMM_COMPETENZA,             								\n"
              + "   VAM.DENOMINAZIONE_1,             					 								\n"
              + "   UZ.DENOMINAZIONE AS DESCR_UFFICIO_ZONA,              								\n"
              + "   UZ.INDIRIZZO AS INDIRIZZO_UFFICIO_ZONA,              								\n"
              + "   B.ANNO_CAMPAGNA,                                     								\n"
              + "   B.DENOMINAZIONE AS DENOMINAZIONE_BANDO,              								\n"
              + "   B.ID_TIPO_LIVELLO AS ID_TIPO_BANDO,     	         								\n"
              + "   SO.DESCRIZIONE,                                      								\n"
              + "   P.DATA_ULTIMO_AGGIORNAMENTO,                         								\n"
              + "   VA.CUAA,                                             								\n"
              + "   VDA2.DESCRIZIONE_PROVINCIA,                          								\n"
              + "   VDA2.DESCRIZIONE_COMUNE,                             								\n"
              + "   VA.INDIRIZZO_SEDE_LEGALE,                            								\n"
              + "   VA.DENOMINAZIONE AS DENOMINAZIONE_AZIENDA,           								\n"
              + "    VA.DENOMINAZIONE || ' ' || VA.INTESTAZIONE_PARTITA_IVA AS DENOMINAZIONE_INTESTAZ,  \n"
              + "   VD.DENOMINAZIONE AS DENOMINAZIONE_DELEGA,            								\n"
              + "   '" + IuffiConstants.PAGINATION.AZIONI.DETTAGLIO
              + "' AS AZIONE,                     		\n"
              + "   'Visualizza Dettaglio' AS TITLE_HREF,                           			  		\n"
              + "   '../cuiuffi129/index_' || PO.ID_PROCEDIMENTO || '.do' AS AZIONE_HREF, 				\n"
              + "   VA.ID_AZIENDA ,                                      								\n"
              /* CONTEGGIO NOTIFICHE PER GRAVITA */
              + " (SELECT                                                	 							\n"
              + "   	COUNT (DISTINCT ID_NOTIFICA)                         							\n"
              + " 	FROM                                                   	 							\n"
              + "   	IUF_T_NOTIFICA NOTIF                      			 							\n"
              + " 	WHERE                                                  	 							\n"
              + "   	NOTIF.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO			 							\n"
              + "		AND NOTIF.ID_GRAVITA_NOTIFICA = 1					 							\n"
              + "		AND (NOTIF.DATA_FINE IS NULL OR NOTIF.DATA_FINE > SYSDATE)						\n"
              + "		) AS CNT_NOTIFICHE_W,	  							 							\n"
              + " (SELECT                                                	 							\n"
              + "   	COUNT (DISTINCT ID_NOTIFICA)                         							\n"
              + " 	FROM                                                   	 							\n"
              + "   	IUF_T_NOTIFICA NOTIF                      			 							\n"
              + " 	WHERE                                                  	 							\n"
              + "   	NOTIF.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO			 							\n"
              + "		AND NOTIF.ID_GRAVITA_NOTIFICA = 2					 							\n"
              + "		AND (NOTIF.DATA_FINE IS NULL OR NOTIF.DATA_FINE > SYSDATE)						\n"
              + "		) AS CNT_NOTIFICHE_G,	  							 							\n"
              + " (SELECT                                                	 							\n"
              + "   	COUNT (DISTINCT ID_NOTIFICA)                         							\n"
              + " 	FROM                                                   	 							\n"
              + "   	IUF_T_NOTIFICA NOTIF                      			 							\n"
              + " 	WHERE                                                  	 							\n"
              + "   	NOTIF.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO			 							\n"
              + "		AND NOTIF.ID_GRAVITA_NOTIFICA = 3					 							\n"
              + "		AND (NOTIF.DATA_FINE IS NULL OR NOTIF.DATA_FINE > SYSDATE)						\n"
              + "		) AS CNT_NOTIFICHE_B 	  							 							\n"
              /* end notifiche */
              + "  FROM                                                      							\n"
              + "    IUF_T_PROCEDIMENTO P,                                   							\n"
              + "    IUF_T_PROCEDIMENTO_OGGETTO PO,                          							\n"
              + "    IUF_T_PROCEDIMENTO_AZIENDA PA,                          							\n"
              + "    IUF_T_PROCEDIM_AMMINISTRAZIO AM,                      							\n"
              + "    DB_UFFICIO_ZONA UZ,				                     							\n"
              + "    SMRGAA_V_DATI_ANAGRAFICI VA,                            							\n"
              + "    SMRGAA_V_DATI_AMMINISTRATIVI VDA2,                      							\n"
              + "    SMRCOMUNE_V_AMM_COMPETENZA VAM,                         							\n"
              + "    IUF_R_PROCEDIMENTO_LIVELLO PLIV,                        							\n"
              + "    IUF_D_LIVELLO DLIV,	                        		 							\n"
              + "    IUF_D_BANDO B,                                          							\n");

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" SMRGAA_V_DATI_DELEGA C, ");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          SELECT.append(" SMRGAA_V_SOGGETTI_COLLEGATI C, ");
        }

      if (vo.getVctIdLivelli() != null && vo.getVctIdLivelli().size() > 0)
      {
        SELECT.append(" IUF_R_LIVELLO_BANDO LIV,  \n");
      }

      if (!GenericValidator.isBlankOrNull(vo.getProvSedeLegale())
          || !GenericValidator.isBlankOrNull(vo.getComuneSedeLegale()))
      {
        SELECT.append(
            " SMRGAA_V_DATI_AMMINISTRATIVI VDA,  					 								\n");
      }
      if (vo.getMapOggetti() != null)
      {
        SELECT.append(
            " IUF_D_ESITO DE, IUF_R_LEGAME_GRUPPO_OGGETTO LGO,  									 								\n");
      }
      SELECT.append(
          " IUF_D_STATO_OGGETTO SO,                                 								\n"
              + "   SMRGAA_V_DATI_DELEGA VD                                  								\n"
              + " WHERE                                                      								\n"
              + "   P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                  								\n"
              + "   AND PA.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO              								\n"
              + "   AND PA.DATA_FINE IS NULL                                								\n"
              + "   AND AM.ID_PROCEDIMENTO(+) = P.ID_PROCEDIMENTO           								\n"
              + "   AND AM.DATA_FINE(+) IS NULL                             								\n"
              + "   AND VA.ID_AZIENDA = PA.EXT_ID_AZIENDA                   								\n"
              + "   AND VA.DATA_FINE_VALIDITA IS NULL                       								\n"
              + "   AND VAM.ID_AMM_COMPETENZA(+) = AM.EXT_ID_AMM_COMPETENZA 								\n"
              + "   AND UZ.ID_UFFICIO_ZONA (+) = AM.EXT_ID_UFFICIO_ZONA     								\n"
              + " 	AND B.ID_BANDO = P.ID_BANDO              												\n"
              // escludere i proc con stato <10 dai bandi scaduti
              + " 	AND ((B.DATA_FINE < SYSDATE AND P.ID_STATO_OGGETTO>=10) or (nvl(B.DATA_FINE,sysdate) >= SYSDATE)) \n"
              + "   AND SO.ID_STATO_OGGETTO = P.ID_STATO_OGGETTO            								\n"
              + "   AND VD.ID_AZIENDA(+) = PA.EXT_ID_AZIENDA                								\n"
              + "   AND VDA2.ISTAT_COMUNE = VA.ISTAT_COMUNE_SEDE_LEGALE 	 								\n"
              + "   AND PLIV.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO			 								\n"
              + "   AND PLIV.ID_LIVELLO = DLIV.ID_LIVELLO 		     		 								\n"
              + "   AND VD.DATA_FINE_VALIDITA(+) IS NULL                    								\n"
              + "   AND B.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO 								\n"
              + "   AND P.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO 								\n"
              );

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(
            " AND VA.ID_AZIENDA = C.ID_AZIENDA													\n"
                + " AND C.DATA_FINE_VALIDITA IS NULL													\n"
                + " AND ( C.ID_INTERMEDIARIO_ZONA = :ID_INTERMEDIARIO  									\n"
                + "		 OR C.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO 								\n"
                + "      OR C.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO ) 								\n");
      }
      else if (isBeneficiario(utenteAbilitazioni))
        {
          SELECT.append(
              " AND VA.ID_AZIENDA = C.ID_AZIENDA													\n"
                  + " AND C.DATA_FINE_RUOLO IS NULL														\n"
                  + " AND C.CODICE_FISCALE = :CODICE_FISCALE  											\n");
        }
      
      if(utenteAbilitazioni.getRuolo().getIsList()!=null && Arrays.asList(utenteAbilitazioni.getRuolo().getIsList()).contains("isUtenteProfessionista") && vo.getIdAziedaIdBandiProfessionsita()!=null)
      {
        SELECT.append(" AND ( "); 
        Set<Long> keys = vo.getIdAziedaIdBandiProfessionsita().keySet();
        int i = 0;
        for(Long  idAzienda : keys)
        {
          if(i!=0)
          {
            SELECT.append(" OR  "); 
          }
          SELECT.append(" ( PA.EXT_ID_AZIENDA = " + idAzienda);
          if(!vo.getIdAziedaIdBandiProfessionsita().get(idAzienda).contains(null))
          {
            SELECT.append( getInCondition("P.ID_BANDO",vo.getIdAziedaIdBandiProfessionsita().get(idAzienda) ));
            SELECT.append(" ) "); 
          }

          i++;
        }
        SELECT.append(" )"); 
      }

      if (vo.getVctIdLivelli() != null && vo.getVctIdLivelli().size() > 0)
      {

        SELECT.append(" AND EXISTS 																			\n"
            + "		(SELECT 																		\n"
            + "			ID_PROCEDIMENTO 															\n"
            + "		FROM 																			\n"
            + "			IUF_R_PROCEDIMENTO_LIVELLO 													\n"
            + "		WHERE 																			\n"
            + "			ID_PROCEDIMENTO = P.ID_PROCEDIMENTO											\n"
            + "		 	AND ID_LIVELLO = LIV.ID_LIVELLO)                							\n");
        SELECT.append(" AND LIV.ID_BANDO = P.ID_BANDO ");
        SELECT
            .append(" " + getInCondition("LIV.ID_LIVELLO", vo.getVctIdLivelli())
                + " 							\n");
      }
      if (vo.getVctIdBando() != null && vo.getVctIdBando().size() > 0)
      {
        SELECT.append(" " + getInCondition("P.ID_BANDO", vo.getVctIdBando())
            + " 								\n");
      }
      if (vo.getVctIdAmministrazione() != null
          && vo.getVctIdAmministrazione().size() > 0)
      {
        SELECT.append(" " + getInCondition("AM.EXT_ID_AMM_COMPETENZA",
            vo.getVctIdAmministrazione()) + " 		\n");
      }
      if (vo.getVctIdStatoProcedimento() != null
          && vo.getVctIdStatoProcedimento().size() > 0)
      {
        SELECT.append(" " + getInCondition("P.ID_STATO_OGGETTO",
            vo.getVctIdStatoProcedimento()) + "			\n");
      }
      if (vo.getVctNotifiche() != null && vo.getVctNotifiche().size() > 0)
      {

        SELECT.append(" AND ( P.ID_PROCEDIMENTO IN ( "
            + queryNotifiche(vo.getVctNotifiche()) + ")   			\n");
        if (vo.getVctNotifiche().contains("99"))
          SELECT.append(" OR P.ID_PROCEDIMENTO NOT IN ( " + queryNotificheAll()
              + ")    					\n");
        SELECT.append(" )");
      }
      if (!GenericValidator.isBlankOrNull(vo.getIdentificativo()))
      {
        SELECT.append(
            "   AND PO.IDENTIFICATIVO = :IDENTIFICATIVO              							\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getCuaa())
          || !GenericValidator.isBlankOrNull(vo.getCuaaProcedimenti()))
      {
        SELECT.append(" AND VA.CUAA = :CUAA \n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getPiva()))
      {
        SELECT.append(
            " AND VA.PARTITA_IVA = :PARTITA_IVA 													\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getDenominazione()))
      {
        SELECT.append(
            " AND VA.DENOMINAZIONE LIKE :DENOMINAZIONE 											\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getProvSedeLegale()))
      {
        SELECT.append(
            " AND VDA.ISTAT_PROVINCIA = SUBSTR(VA.ISTAT_COMUNE_SEDE_LEGALE,1,3) 					\n");
        SELECT.append(
            " AND VDA.SIGLA_PROVINCIA = :SIGLA_PROVINCIA 											\n");
      }
      if (!GenericValidator.isBlankOrNull(vo.getComuneSedeLegale()))
      {
        SELECT.append(
            " AND VDA.ISTAT_COMUNE = VA.ISTAT_COMUNE_SEDE_LEGALE 									\n");
        SELECT.append(
            " AND VDA.DESCRIZIONE_COMUNE = :DESCRIZIONE_COMUNE 									\n");
      }

      String sep = " ";

      if (vo.getMapOggetti() != null)
      {
        SELECT.append(
            " AND LGO.ID_LEGAME_GRUPPO_OGGETTO = PO.ID_LEGAME_GRUPPO_OGGETTO   				\n");
        SELECT.append(
            " AND DE.ID_ESITO(+) = PO.ID_ESITO   												\n");
      }

      if (vo.getMapGruppi() != null)
      {

        SELECT.append(" AND ( \n");
        Iterator<Entry<Long, Vector<Long>>> it = vo.getMapGruppi().entrySet()
            .iterator();
        int count = 0;
        String tmp = " ";
        Vector<Long> vct = null;
        while (it.hasNext())
        {
          tmp = " ";
          @SuppressWarnings("rawtypes")
          Map.Entry pair = (Map.Entry) it.next();
          if (count > 0)
          {
            sep = "OR";
            if (!GenericValidator.isBlankOrNull(vo.getTipoFiltroOggetto()))
              sep = vo.getTipoFiltroOggetto();
          }

          vct = (Vector<Long>) pair.getValue();
          if (vct.contains(new Long(0))) // Non Presente --> 0
          {
            SELECT.append(" " + sep + " NOT EXISTS ( "
                + "	SELECT "
                + "		LGO.ID_GRUPPO_OGGETTO "
                + "	FROM "
                + "		IUF_R_LEGAME_GRUPPO_OGGETTO LGO, "
                + "		IUF_T_PROCEDIMENTO_OGGETTO PO2, "
                + "		IUF_T_PROCEDIMENTO P2 "
                + "	WHERE "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO "
                + "		AND P.ID_PROCEDIMENTO = P2.ID_PROCEDIMENTO "
                + "		AND P2.ID_PROCEDIMENTO = PO2.ID_PROCEDIMENTO "
                + "		AND LGO.ID_GRUPPO_OGGETTO = " + (Long) pair.getKey()
                + "  ) \n");
          }
          else
          {
            SELECT.append(" " + sep + "  EXISTS ( "
                + "	SELECT  "
                + "		LGO2.ID_GRUPPO_OGGETTO "
                + "	FROM "
                + "		IUF_R_LEGAME_GRUPPO_OGGETTO LGO2, "
                + "		IUF_T_ITER_PROCEDIMENTO_GRUP IPG2, "
                + "		IUF_T_PROCEDIMENTO_OGGETTO PO2 "
                + "	WHERE "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO2.ID_LEGAME_GRUPPO_OGGETTO "
                + "		AND P.ID_PROCEDIMENTO = PO2.ID_PROCEDIMENTO  "
                + "		AND IPG2.CODICE_RAGGRUPPAMENTO = PO2.CODICE_RAGGRUPPAMENTO  "
                + "		AND IPG2.DATA_FINE  IS NULL "
                + "		AND IPG2.ID_PROCEDIMENTO=PO2.ID_PROCEDIMENTO "
                + "		AND LGO2.ID_GRUPPO_OGGETTO = " + (Long) pair.getKey()
                + " "
                + "		AND "
                + getInCondition("IPG2.ID_STATO_OGGETTO", vct, false) + " "
                + tmp + " ) 	\n");
          }
          count++;
        }

        if (vo.getMapOggetti() == null)
          SELECT.append(" ) \n");

      }
      if (vo.getMapOggetti() != null)
      {

        if (vo.getMapGruppi() == null)
          SELECT.append("  AND ( ");

        Iterator<Entry<Long, Vector<Long>>> it2 = vo.getMapOggetti().entrySet()
            .iterator();
        int count2 = 0;
        if (vo.getMapGruppi() != null)
          count2 = 1;
        String sep2 = "";
        String tmp2 = " ";
        Vector<Long> vct2 = null;
        while (it2.hasNext())
        {
          tmp2 = " ";
          @SuppressWarnings("rawtypes")
          Map.Entry pair = (Map.Entry) it2.next();
          if (count2 > 0)
          {
            sep2 = "OR";
            if (!GenericValidator.isBlankOrNull(vo.getTipoFiltroOggetto()))
              sep2 = vo.getTipoFiltroOggetto();
          }

          vct2 = (Vector<Long>) pair.getValue();
          if (vct2.contains(new Long(-1))) // Aperto --> -1
            tmp2 = " OR PO2.ID_ESITO IS NULL";

          if (vct2.contains(new Long(0))) // Non Presente --> 0
          {
            SELECT.append(" " + sep2 + " NOT EXISTS ( "
                + "	SELECT "
                + "		LGO.ID_LEGAME_GRUPPO_OGGETTO "
                + "	FROM "
                + "		IUF_R_LEGAME_GRUPPO_OGGETTO LGO, "
                + "		IUF_T_PROCEDIMENTO_OGGETTO PO2 "
                + "	WHERE "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO "
                + "		AND P.ID_PROCEDIMENTO = PO2.ID_PROCEDIMENTO "
                + "		AND LGO.ID_LEGAME_GRUPPO_OGGETTO = "
                + (Long) pair.getKey() + "  ) 				\n");
          }
          else
          {
            SELECT.append(" " + sep2 + " EXISTS ( "
                + "	SELECT "
                + "		LGO.ID_LEGAME_GRUPPO_OGGETTO "
                + "	FROM "
                + "		IUF_R_LEGAME_GRUPPO_OGGETTO LGO, "
                + "		IUF_T_PROCEDIMENTO_OGGETTO PO2	 "
                + "	WHERE "
                + "		PO2.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO "
                + "		AND P.ID_PROCEDIMENTO = PO2.ID_PROCEDIMENTO "
                + "		AND LGO.ID_LEGAME_GRUPPO_OGGETTO = "
                + (Long) pair.getKey()
                + " 	AND (" + getInCondition("PO2.ID_ESITO", vct2, false) + " "
                + tmp2 + " ) )		 \n");
          }
          count2++;
        }
        SELECT.append(" ) \n");
      }

      if (vo.getVctFlagEstrazione() != null
          && vo.getVctFlagEstrazione().size() > 0)
      {
        
    	if(!IuffiUtils.VALIDATION.isValidVectorOfFlags(vo.getVctFlagEstrazione()))
    	{
    		  throw new ApplicationException("Invalid flags [10023]",10023); 
    	}
    	SELECT.append(" AND (   \n");
        SELECT.append(" EXISTS ( "
            + "	SELECT  "
            + "		A.ID_PROCEDIMENTO_ESTRATTO "
            + "	FROM "
            + "		IUF_T_PROCEDIMENTO_ESTRATTO A, "
            + "		IUF_T_ESTRAZIONE_CAMPIONE B "
            + "	WHERE "
            + "		A.ID_ESTRAZIONE_CAMPIONE = B.ID_ESTRAZIONE_CAMPIONE "
            + "		AND B.FLAG_APPROVATA = 'S' "
            + "		AND DATA_ANNULLAMENTO IS NULL "
            + "		AND ( A.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO  OR"
            + "		A.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO)	"
            + getInCondition("A.FLAG_ESTRATTA", vo.getVctFlagEstrazione())
            + " )   \n");

        SELECT.append(" )   \n");
      }

      if (vo.getVctFlagEstrazioneExPost() != null
          && vo.getVctFlagEstrazioneExPost().size() > 0)
      {
      	if(!IuffiUtils.VALIDATION.isValidVectorOfFlags(vo.getVctFlagEstrazioneExPost()))
      	{
      		  throw new ApplicationException("Invalid flags [10024]",10024); 
      	}
    	  
    	SELECT.append(" AND (   \n");
        SELECT.append(" EXISTS ( "
            + "	SELECT  "
            + "		A.ID_PROCEDIMENTO_ESTR_EXPOST "
            + "	FROM "
            + "		IUF_T_PROCEDIMENTO_ESTR_EXPO A, "
            + "		IUF_T_ESTRAZIONE_CAMPIONE B "
            + "	WHERE "
            + "		A.ID_ESTRAZIONE_CAMPIONE = B.ID_ESTRAZIONE_CAMPIONE "
            + "		AND B.FLAG_APPROVATA = 'S' "
            + "		AND DATA_ANNULLAMENTO IS NULL "
            + "		AND ( A.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO  OR"
            + "		A.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO)	"
            + getInCondition("A.FLAG_ESTRATTA", vo.getVctFlagEstrazioneExPost())
            + " )   \n");

        SELECT.append(" )   \n");
      }

      /* WORKAROUND TEMPORANEO by Einaudi */
      /*
       * Dobbiamo impedire ai gal di vedere le pratiche dei bandi in cui possono
       * loro stessi presentare domanda come beneficiari, per questo motivo
       * abbiamo messo temporaneamente un parametro con l'elenco degli ID_BANDO
       * da escludere dalla ricerca (separati da virgola in modo che possa
       * essere "schiantato" in una NOT IN). Nel caso quindi che l'utente sia un
       * gal aggiungiamo una NOT IN con gli id_bando a cui non può accedere
       */
      if (utenteAbilitazioni.isUtenteGAL())
      {
        Map<String, String> mapParametri = getParametri(new String[]
        { "ESCLUDI_BANDI_GAL" });
        String escludiBandiGal = mapParametri == null ? null
            : mapParametri.get("ESCLUDI_BANDI_GAL");
        SELECT.append(" AND P.ID_BANDO NOT IN (").append(escludiBandiGal)
            .append(")");
      }

      SELECT.append(
          " ORDER BY VA.DENOMINAZIONE, P.ID_PROCEDIMENTO DESC, DLIV.CODICE_LIVELLO\n");
      SELECT.append(" ) SELECT PROCEDIMENTI_ESTRATTI.*  "
          + "FROM PROCEDIMENTI_ESTRATTI");

      if (vo.getIstanzaDataA() != null || vo.getIstanzaDataDa() != null)
      {
        // ultima istanza trasmessa da - a INI
        SELECT.append(" WHERE    																					\n");
        if (vo.getIstanzaDataA() != null && vo.getIstanzaDataDa() != null)
        {
          SELECT.append(
              " TRUNC(DATA_ULTIMA_IST) BETWEEN TRUNC(:DATA_ISTANZA_DA) AND  TRUNC(:DATA_ISTANZA_A)  	\n");
        }
        else
          if (vo.getIstanzaDataA() != null)
          {
            SELECT.append(
                " TRUNC(DATA_ULTIMA_IST) <=  TRUNC(:DATA_ISTANZA_A) 	 									\n");
          }
          else
            if (vo.getIstanzaDataDa() != null)
            {
              SELECT.append(
                  " TRUNC(DATA_ULTIMA_IST) >=  TRUNC(:DATA_ISTANZA_DA)  									\n");
            }
        SELECT.append("    \n");
      }

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      if (!GenericValidator.isBlankOrNull(vo.getIdentificativo()))
      {
        parameterSource.addValue("IDENTIFICATIVO",
            vo.getIdentificativo().trim().toUpperCase(), Types.VARCHAR);
      }
      if (!GenericValidator.isBlankOrNull(vo.getCuaa()))
      {
        parameterSource.addValue("CUAA", vo.getCuaa().trim().toUpperCase(),
            Types.VARCHAR);
      }
      if (vo.getIstanzaDataA() != null)
      {
        parameterSource.addValue("DATA_ISTANZA_A", vo.getIstanzaDataA(),
            Types.DATE);
      }

      if (vo.getIstanzaDataDa() != null)
      {
        parameterSource.addValue("DATA_ISTANZA_DA", vo.getIstanzaDataDa(),
            Types.DATE);
      }
      if (!GenericValidator.isBlankOrNull(vo.getCuaaProcedimenti()))
      {
        parameterSource.addValue("CUAA",
            vo.getCuaaProcedimenti().trim().toUpperCase(), Types.VARCHAR);
      }
      if (!GenericValidator.isBlankOrNull(vo.getPiva()))
      {
        parameterSource.addValue("PARTITA_IVA",
            vo.getPiva().trim().toUpperCase(), Types.VARCHAR);
      }
      if (!GenericValidator.isBlankOrNull(vo.getProvSedeLegale()))
      {
        parameterSource.addValue("SIGLA_PROVINCIA",
            vo.getProvSedeLegale().trim().toUpperCase(), Types.VARCHAR);
      }
      if (!GenericValidator.isBlankOrNull(vo.getComuneSedeLegale()))
      {
        parameterSource.addValue("DESCRIZIONE_COMUNE",
            vo.getComuneSedeLegale().trim().toUpperCase(), Types.VARCHAR);
      }
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        parameterSource.addValue("ID_INTERMEDIARIO", utenteAbilitazioni
            .getEnteAppartenenza().getIntermediario().getIdIntermediario(),
            Types.NUMERIC);
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          parameterSource.addValue("CODICE_FISCALE",
              utenteAbilitazioni.getCodiceFiscale().trim().toUpperCase(),
              Types.VARCHAR);
        }

      if (!GenericValidator.isBlankOrNull(vo.getDenominazione()))
      {
        parameterSource.addValue("DENOMINAZIONE",
            "%" + IuffiUtils.STRING.normalizeString(
                vo.getDenominazione().trim().toUpperCase(), " ") + "%",
            Types.VARCHAR);
      }

      parameterSource.addValue("ATTORE",
          IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni),
          Types.VARCHAR);
      
      parameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO",
              utenteAbilitazioni.getIdProcedimento(),
              Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<ProcedimentoOggettoVO>>()
          {
            @Override
            public List<ProcedimentoOggettoVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<ProcedimentoOggettoVO> list = new ArrayList<ProcedimentoOggettoVO>();
              ArrayList<String> elencoLivelli = null;
              ProcedimentoOggettoVO procOggetto = null;
              long idProcedimentoLast = -1;
              long idProcedimento = -1;
              BigDecimal bdImportoInvestimento = null;
              BigDecimal bdSpesaAmmessa = null;
              BigDecimal bdContributoConcesso = null;
              List<LivelloDTO> livelli = null;
              LivelloDTO livello = null;
              String misura = "";
              String sottoMisura = "";

              while (rs.next())
              {
                // Devo controllare idProcedimento perchè la query mi
                // restituisce più record se l'azienda ha più
                // procediemnto_oggetti
                // Sono sicuro che prendo l'ultimo id_procedimento_oggetto
                // perchè è una condizione di orderBY
                idProcedimento = rs.getLong("ID_PROCEDIMENTO");
                if (idProcedimentoLast != idProcedimento)
                {
                  idProcedimentoLast = idProcedimento;
                  bdImportoInvestimento = BigDecimal.ZERO;
                  bdSpesaAmmessa = BigDecimal.ZERO;
                  bdContributoConcesso = BigDecimal.ZERO;

                  procOggetto = new ProcedimentoOggettoVO();
                  procOggetto.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                  procOggetto.setDescrAmmCompetenza(
                      rs.getString("DESCR_AMM_COMPETENZA"));
                  procOggetto.setDescrAmmCompetenzaInfo(
                      rs.getString("DENOMINAZIONE_1"));
                  procOggetto.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                  procOggetto.setDenominazioneBando(
                      rs.getString("DENOMINAZIONE_BANDO"));
                  procOggetto.setDenominazioneIntestazione(
                      rs.getString("DENOMINAZIONE_INTESTAZ"));
                  procOggetto.setDescrizione(rs.getString("DESCRIZIONE"));
                  procOggetto.setDataUltimoAggiornamento(IuffiUtils.DATE
                      .formatDate(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO")));
                  procOggetto.setCuaa(rs.getString("CUAA"));
                  procOggetto.setIndirizzoSedeLegale(
                      rs.getString("INDIRIZZO_SEDE_LEGALE"));
                  procOggetto.setDenominazioneAzienda(
                      rs.getString("DENOMINAZIONE_AZIENDA"));
                  procOggetto
                      .setDescrComune(rs.getString("DESCRIZIONE_COMUNE"));
                  procOggetto
                      .setDescrProvincia(rs.getString("DESCRIZIONE_PROVINCIA"));
                  procOggetto.setDenominzioneDelega(
                      rs.getString("DENOMINAZIONE_DELEGA"));
                  procOggetto.setAzione(rs.getString("AZIONE"));
                  procOggetto.setTitleHref(rs.getString("TITLE_HREF"));
                  procOggetto.setAzioneHref(rs.getString("AZIONE_HREF"));
                  procOggetto.setIdAzienda(rs.getLong("ID_AZIENDA"));
                  procOggetto.setIdProcedimento(rs.getLong("ID_PROCEDIMENTO"));
                  procOggetto.setResponsabileProcedimento(
                      rs.getString("RESPONSABILE_PROCEDIMENTO"));
                  procOggetto
                      .setTecnicoIstruttore(rs.getString("TECNICO_ISTRUTTORE"));
                  procOggetto.setImportoLiquidato(
                      rs.getBigDecimal("IMPORTO_LIQUIDATO"));
                  procOggetto
                      .setDescrUltimaIstanza(rs.getString("DESCR_ULTIMA_IST"));
                  procOggetto
                      .setDataUltimaIstanza(rs.getDate("DATA_ULTIMA_IST"));
                  String indirizzoUfficioZona = rs
                      .getString("INDIRIZZO_UFFICIO_ZONA");
                  String descrUfficioZona = rs.getString("DESCR_UFFICIO_ZONA");
                  if (indirizzoUfficioZona != null
                      && indirizzoUfficioZona.compareTo("") != 0)
                    procOggetto.setDescrUfficioZona(
                        descrUfficioZona + ", " + indirizzoUfficioZona);
                  else
                    procOggetto.setDescrUfficioZona(descrUfficioZona);

                  livelli = new ArrayList<LivelloDTO>();
                  procOggetto.setLivelli(livelli);
                  ;
                  elencoLivelli = new ArrayList<String>();
                  procOggetto.setElencoCodiciLivelli(elencoLivelli);

                  procOggetto.setCntNotificheB(rs.getInt("CNT_NOTIFICHE_B"));
                  procOggetto.setCntNotificheW(rs.getInt("CNT_NOTIFICHE_W"));
                  procOggetto.setCntNotificheG(rs.getInt("CNT_NOTIFICHE_G"));

                  list.add(procOggetto);
                }

                if (rs.getBigDecimal("IMPORTO_INVESTIMENTO") != null)
                {
                  bdImportoInvestimento = bdImportoInvestimento
                      .add(rs.getBigDecimal("IMPORTO_INVESTIMENTO"));
                }
                if (rs.getBigDecimal("SPESA_AMMESSA") != null)
                {
                  bdSpesaAmmessa = bdSpesaAmmessa
                      .add(rs.getBigDecimal("SPESA_AMMESSA"));
                }
                if (rs.getBigDecimal("CONTRIBUTO_CONCESSO") != null)
                {
                  bdContributoConcesso = bdContributoConcesso
                      .add(rs.getBigDecimal("CONTRIBUTO_CONCESSO"));
                }
                procOggetto.setProcedimContributoConcesso(bdContributoConcesso);
                procOggetto
                    .setProcedimImportoInvestimento(bdImportoInvestimento);
                procOggetto.setProcedimSpesaAmmessa(bdSpesaAmmessa);

                livello = new LivelloDTO();
                misura = rs.getString("CODICE_LIVELLO").split("\\.")[0];
                if (rs.getString("CODICE_LIVELLO").split("\\.").length > 1)
                {
                  sottoMisura = rs.getString("CODICE_LIVELLO").split("\\.")[1];
                  livello.setCodiceSottoMisura(misura + "." + sottoMisura);
                }

                livello.setIdLivello(rs.getLong("ID_LIVELLO"));
                livello.setCodice(rs.getString("CODICE"));
                livello.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                livello.setCodiceMisura(misura);
                livello.setDescrizione("DESCRIZIONE");
                livelli.add(livello);

                elencoLivelli.add(rs.getString("COD_LIVELLO"));
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

  private String queryNotifiche(Vector<String> vector)
  {
    return " SELECT                                                	 		\n"
        + "   	NOTIF.ID_PROCEDIMENTO				                 		\n"
        + " 	FROM                                                   	 		\n"
        + "   	IUF_T_NOTIFICA NOTIF                      			 		\n"
        + " 	WHERE                                                  	 		\n"
        + "		(NOTIF.DATA_FINE IS NULL OR NOTIF.DATA_FINE > SYSDATE)		\n"
        + " " + getInCondition("NOTIF.ID_GRAVITA_NOTIFICA", vector) + "			\n";
  }

  private String queryNotificheAll()
  {
    return "SELECT                                                	 		\n"
        + "   	NOTIF.ID_PROCEDIMENTO				                 		\n"
        + " 	FROM                                                   	 		\n"
        + "   	IUF_T_NOTIFICA NOTIF                      			 		\n"
        + " 	WHERE                                                  	 		\n"
        + "		(NOTIF.DATA_FINE IS NULL OR NOTIF.DATA_FINE > SYSDATE)		\n"
        + " 		AND NOTIF.ID_GRAVITA_NOTIFICA IN (1,2,3) 					\n";
  }

  public List<GruppoOggettoDTO> getElencoOggettiDisponibili(long idBando,
      boolean flagIstanza, Long idGruppoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoOggettiDisponibili]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        " SELECT                                                                \n"
            + "   LGO.ID_LEGAME_GRUPPO_OGGETTO,                                       \n"
            + "   GO.ID_GRUPPO_OGGETTO,                                               \n"
            + "   GO.DESCRIZIONE AS DESCR_GRUPPO_OGGETTO,                             \n"
            + "   GO.CODICE AS COD_GRUPPO_OGGETTO,                                    \n"
            + "   O.DESCRIZIONE AS DESCR_OGGETTO,                                     \n"
            + "   BO.ID_BANDO_OGGETTO                                                 \n"
            + " FROM                                                                  \n"
            + "   IUF_D_GRUPPO_OGGETTO        GO,                                     \n"
            + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                    \n"
            + "   IUF_D_OGGETTO               O,                                      \n"
            + "   IUF_R_BANDO_OGGETTO         BO                                      \n"
            + " WHERE                                                                 \n"
            + "   GO.ID_GRUPPO_OGGETTO = LGO.ID_GRUPPO_OGGETTO                        \n"
            + "   AND O.ID_OGGETTO = LGO.ID_OGGETTO                                   \n"
            + "   AND LGO.ID_LEGAME_GRUPPO_OGGETTO = BO.ID_LEGAME_GRUPPO_OGGETTO      \n"
            + "   AND SYSDATE BETWEEN BO.DATA_INIZIO AND                              \n"
            + "       NVL(BO.DATA_RITARDO, NVL(BO.DATA_FINE, SYSDATE))                \n"
            + "   AND BO.FLAG_ATTIVO = 'S'                                            \n"
            + "   AND O.FLAG_ISTANZA = :FLAG_ISTANZA                                  \n"
            + "   AND BO.ID_BANDO = :ID_BANDO                                         \n"
            + "   AND O.FLAG_ISTANZA = :FLAG_ISTANZA                                  \n");

    if (idGruppoOggetto != null && idGruppoOggetto.longValue() > 0)
    {
      QUERY.append("   AND GO.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO 		\n"
          + "		   AND LGO.ORDINE >= 									\n");
    }
    else
    {
      QUERY.append("   AND LGO.ORDINE = \n");
    }

    QUERY
        .append("        (SELECT MIN(LGOMIN.ORDINE)                          \n"
            + "           FROM IUF_R_LEGAME_GRUPPO_OGGETTO LGOMIN                     \n"
            + "          WHERE LGOMIN.ID_GRUPPO_OGGETTO = LGO.ID_GRUPPO_OGGETTO)      \n"
            + " ORDER BY GO.ORDINE, LGO.ORDINE                                        \n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    mapParameterSource.addValue("FLAG_ISTANZA", (flagIstanza) ? "S" : "N",
        Types.VARCHAR);

    if (idGruppoOggetto != null && idGruppoOggetto.longValue() > 0)
    {
      mapParameterSource.addValue("ID_GRUPPO_OGGETTO",
          idGruppoOggetto.longValue(), Types.NUMERIC);
    }

    try
    {
      return namedParameterJdbcTemplate.query(QUERY.toString(),
          mapParameterSource, new ResultSetExtractor<List<GruppoOggettoDTO>>()
          {
            @Override
            public List<GruppoOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GruppoOggettoDTO> list = new ArrayList<GruppoOggettoDTO>();
              ArrayList<OggettoDTO> oggettiList = null;
              GruppoOggettoDTO gruppoOggettoDTO = null;
              long lastIdGruppoOggetto = 0;
              long idGruppoOggetto = 0;

              while (rs.next())
              {
                idGruppoOggetto = rs.getLong("ID_GRUPPO_OGGETTO");
                if (idGruppoOggetto != lastIdGruppoOggetto)
                {
                  lastIdGruppoOggetto = idGruppoOggetto;
                  gruppoOggettoDTO = new GruppoOggettoDTO();
                  gruppoOggettoDTO
                      .setCodice(rs.getString("COD_GRUPPO_OGGETTO"));
                  gruppoOggettoDTO
                      .setDescrizione(rs.getString("DESCR_GRUPPO_OGGETTO"));
                  gruppoOggettoDTO
                      .setIdGruppoOggetto(rs.getLong("ID_GRUPPO_OGGETTO"));
                  oggettiList = new ArrayList<OggettoDTO>();
                  gruppoOggettoDTO.setOggetti(oggettiList);
                  list.add(gruppoOggettoDTO);
                }
                OggettoDTO oggettoDTO = new OggettoDTO();
                oggettoDTO.setDescrizione(rs.getString("DESCR_OGGETTO"));
                oggettoDTO.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                oggettoDTO.setIdLegameGruppoOggetto(
                    rs.getLong("ID_LEGAME_GRUPPO_OGGETTO"));
                oggettiList.add(oggettoDTO);
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
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public List<BandoDTO> getElencoBandi(String attore, int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoBandi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        " SELECT                                                                                                                                          \n"
            + "   A.ID_BANDO,                                                                                                                                            	\n"
            + "   A.DENOMINAZIONE,                                                                                                                                       	\n"
            + "   A.REFERENTE_BANDO,                                                                                                                                     	\n"
            + "   A.ANNO_CAMPAGNA,                                                                                                                                       	\n"
            + "   A.DATA_INIZIO,                                                                                                                                         	\n"
            + "   A.DATA_FINE,																																			\n"
            + "  DECODE(A.FLAG_TITOLARITA_REGIONALE,'S','Si','N','No','') AS FLAG_TITOLARITA_REGIONALE,  																	\n"
            + "  B.DESCRIZIONE AS DESCR_TIPO_BANDO,                     		 																							\n"
            + "   (SELECT                                                                                                                                                	\n"
            + "     QB.ID_ELENCO_QUERY                                                                                                                                   	\n"
            + "    FROM                                                                                                                                                  	\n"
            + "     IUF_R_ELENCO_QUERY_BANDO QB,                                                                                                                         	\n"
            + "     IUF_D_ELENCO_QUERY EQ,                                                                                                                               	\n"
            + "     IUF_D_TIPO_VISUALIZZAZIONE TV                                                                                                                        	\n"
            + "    WHERE                                                                                                                                                 	\n"
            + "     QB.ID_BANDO = A.ID_BANDO                                                                                                                       	 	\n"
            + "     AND QB.EXT_COD_ATTORE =:ATTORE                                                                                                                        \n"
            + "     AND QB.FLAG_VISIBILE = 'S'				                 																							\n"
            + "     AND EQ.ID_ELENCO_QUERY = QB.ID_ELENCO_QUERY                                                                                                          	\n"
            + "     AND TV.ID_TIPO_VISUALIZZAZIONE = EQ.ID_TIPO_VISUALIZZAZIONE                                                                                          	\n"
            + "     AND TV.FLAG_ELENCO = 'N'                                                                                                                             	\n"
            + "     AND ROWNUM = 1) AS FLAG_GRAFICO,                                                                                                                   	\n"
            + "     (SELECT                                                                                                                                              	\n"
            + "     QB.ID_ELENCO_QUERY                                                                                                                                   	\n"
            + "    FROM                                                                                                                                                  	\n"
            + "     IUF_R_ELENCO_QUERY_BANDO QB,                                                                                                                         	\n"
            + "     IUF_D_ELENCO_QUERY EQ,                                                                                                                               	\n"
            + "     IUF_D_TIPO_VISUALIZZAZIONE TV                                                                                                                        	\n"
            + "    WHERE                                                                                                                                                 	\n"
            + "     QB.ID_BANDO = A.ID_BANDO                                                                                                                             	\n"
            + "     AND QB.EXT_COD_ATTORE =:ATTORE                                                                                                                        \n"
            + "     AND QB.FLAG_VISIBILE = 'S'				                 																							\n"
            + "     AND EQ.ID_ELENCO_QUERY = QB.ID_ELENCO_QUERY                                                                                                          	\n"
            + "     AND TV.ID_TIPO_VISUALIZZAZIONE = EQ.ID_TIPO_VISUALIZZAZIONE                                                                                          	\n"
            + "     AND TV.FLAG_ELENCO = 'S'                                                                                                                             	\n"
            + "     AND ROWNUM = 1) AS FLAG_REPORT                                                                                                                       	\n"
            + " 	FROM                                                                                                                                                   	\n"
            + "   IUF_D_BANDO A,  																																		\n"
            + "   IUF_D_TIPO_LIVELLO B   																																	\n"
            + " 	WHERE                                                                                                                                                   \n"
            + "  	 A.FLAG_MASTER <> 'S'																																	\n"
            + "	  AND A.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO																								\n"
            + "   AND B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO 																												\n"
            + "   AND(                                                                                                                                                    \n"
            + "  		(SYSDATE BETWEEN A.DATA_INIZIO AND NVL(A.DATA_FINE,SYSDATE)																							\n"
            + "	AND  																																					\n"
            + "		 EXISTS (SELECT B.ID_BANDO FROM IUF_R_BANDO_OGGETTO B WHERE B.ID_BANDO = A.ID_BANDO AND B.FLAG_ATTIVO = 'S') 										\n"
            + "		)                                                  																								    \n"
            + "       OR 																																					\n"
            + "		(																																					\n"
            + "			A.DATA_FINE < SYSDATE 																															\n"
            + "				AND																																			\n"
            + "		 	EXISTS (SELECT A.ID_BANDO FROM IUF_T_PROCEDIMENTO C WHERE A.ID_BANDO = C.ID_BANDO) 																\n"
            + "  		 ) 																																					\n"
            + "   )                                                                                                                                                      	\n"
            + "  ORDER BY A.DENOMINAZIONE      																															\n");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ATTORE", attore, Types.VARCHAR);
    mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo, Types.VARCHAR);
    try
    {
      return queryForList(QUERY.toString(), mapParameterSource, BandoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public List<GraduatoriaDTO> getGraduatorieBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getGraduatorieBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               	\n"
          + "   G.ID_GRADUATORIA,                                  	\n"
          + "   G.DATA_APPROVAZIONE,                    		   	\n"
          + "   G.DESCRIZIONE                                      	\n"
          + " FROM                                                 	\n"
          + "   IUF_t_graduatoria G,                   			   	\n"
          + "   IUF_r_liv_bando_graduatoria BG                     	\n"
          + " WHERE                                                	\n"
          + "   bg.id_bando = :ID_BANDO							   	\n"
          + "   AND bg.id_graduatoria = g.id_graduatoria     	   	\n"
          + "	AND g.flag_approvata= 'S'							\n"
          + " ORDER BY G.DESCRIZIONE 								\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<GraduatoriaDTO>>()
          {
            @Override
            public List<GraduatoriaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GraduatoriaDTO> list = new ArrayList<GraduatoriaDTO>();
              GraduatoriaDTO grad = null;
              while (rs.next())
              {
                grad = new GraduatoriaDTO();
                grad.setIdGraduatoria(rs.getLong("ID_GRADUATORIA"));
                grad.setDescrizione(rs.getString("DESCRIZIONE"));
                grad.setDataApprovazione(rs.getTimestamp("DATA_APPROVAZIONE"));
                list.add(grad);
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

  public List<FileAllegatoDTO> getAllegatiGraduatoria(long idGraduatoria)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAllegatiGraduatoria";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   G.ID_ALLEGATI_GRADUATORIA,                         \n"
          + "   G.NOME_FILE,         			           		   \n"
          + "   G.DESCRIZIONE                                      \n"
          + " FROM                                                 \n"
          + "   IUF_t_allegati_graduatoria G                   	   \n"
          + " WHERE                                                \n"
          + "   G.id_graduatoria = :ID_GRADUATORIA				   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_GRADUATORIA", idGraduatoria, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<FileAllegatoDTO>>()
          {
            @Override
            public List<FileAllegatoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<FileAllegatoDTO> list = new ArrayList<FileAllegatoDTO>();
              FileAllegatoDTO all = null;
              while (rs.next())
              {
                all = new FileAllegatoDTO();
                all.setIdAllegatiBando(rs.getLong("ID_ALLEGATI_GRADUATORIA"));
                all.setDescrizione(rs.getString("DESCRIZIONE"));
                all.setNomeFile(rs.getString("NOME_FILE"));
                list.add(all);
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

  public FileAllegatoDTO getFileAllegatoGraduatoria(long idAllegatiGraduatoria)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getFileAllegatoGraduatoria";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               		\n"
          + "   FILE_ALLEGATO, NOME_FILE                         		\n"
          + " FROM                                                 		\n"
          + "   IUF_T_ALLEGATI_GRADUATORIA G                   	   		\n"
          + " WHERE                                                		\n"
          + "   G.ID_ALLEGATI_GRADUATORIA = :ID_ALLEGATI_GRADUATORIA	\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ALLEGATI_GRADUATORIA", idAllegatiGraduatoria,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<FileAllegatoDTO>()
          {
            @Override
            public FileAllegatoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              FileAllegatoDTO f = new FileAllegatoDTO();
              while (rs.next())
              {
                f.setFileAllegato(rs.getBytes("FILE_ALLEGATO"));
                f.setNomeFile(rs.getString("NOME_FILE"));

              }
              return f;
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

  public List<LivelloDTO> getLivelliBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getLivelliBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT                           \n"
          + "   L.ID_LIVELLO ID_LIVELLO,				\n"
          + "   L.DESCRIZIONE  DESCRIZIONE, 		    \n"
          + "   L.CODICE CODICE,              			\n"
          + "   L.CODICE_LIVELLO  						\n"
          + " FROM                                      \n"
          + "   IUF_R_LIVELLO_BANDO LB,					\n"
          + "	IUF_D_LIVELLO L                  	    \n"
          + " WHERE                                     \n"
          + "   LB.ID_BANDO = :ID_BANDO					\n"
          + " AND 										\n"
          + "	L.ID_LIVELLO  = LB.ID_LIVELLO			\n"
          + "	ORDER BY CODICE							\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> livelli = new LinkedList<LivelloDTO>();
              LivelloDTO l = null;
              String misura = "";
              String sottoMisura = "";

              while (rs.next())
              {
                l = new LivelloDTO();
                l.setIdLivello(rs.getLong("ID_LIVELLO"));
                l.setDescrizione(rs.getString("DESCRIZIONE"));
                l.setCodice(rs.getString("CODICE"));
                l.setCodiceLivello(rs.getString("CODICE_LIVELLO"));

                misura = rs.getString("CODICE_LIVELLO").split("\\.")[0];
                l.setCodiceMisura(misura);
                if (rs.getString("CODICE_LIVELLO").split("\\.").length > 1)
                {
                  sottoMisura = rs.getString("CODICE_LIVELLO").split("\\.")[1];
                  l.setCodiceSottoMisura(misura + "." + sottoMisura);
                }

                livelli.add(l);
              }
              if (livelli.isEmpty())
                return null;

              return livelli;
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

  public List<LivelloDTO> getElencoLivelliByProcedimentoAgricolo(int idProcedimentoAgricolo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoLivelliByProcedimentoAgricolo";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                          		    														\n"
          + " L.DESCRIZIONE AS  DESCRIZIONE,  		    														\n"
          + " L.ID_LIVELLO AS ID,  		    																	\n"
          + " L.CODICE AS CODICE,   		    																\n"
          + " L.CODICE_LIVELLO   		    																	\n"
          + " FROM      		    																			\n"
          + "	IUF_D_BANDO A,  		    																	\n"
          + "   IUF_D_TIPO_LIVELLO B,   																	    \n"
          + "   IUF_R_LIVELLO_BANDO LB,					 		  		    									\n"
          + "	IUF_D_LIVELLO L                  		    													\n"
          + " WHERE                         																	\n"
          + "    B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO  "
          + "	AND A.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO		    											\n"
          + "AND																								\n"
          + "   LB.ID_BANDO = A.ID_BANDO					  		    										\n"
          + " AND 																								\n"
          + "	L.ID_LIVELLO  = LB.ID_LIVELLO                       											\n"
          + "	AND																								\n"
          + " 	 A.FLAG_MASTER <> 'S'																			\n"
          + "   AND(                                                                                            \n"
          + "  		(SYSDATE BETWEEN A.DATA_INIZIO AND NVL(A.DATA_FINE,SYSDATE)									\n"
          + "	AND  																							\n"
          + "		 EXISTS (SELECT B.ID_BANDO                        											\n"
          + "				 FROM IUF_R_BANDO_OGGETTO B                       									\n"
          + "				 WHERE B.ID_BANDO = A.ID_BANDO AND B.FLAG_ATTIVO = 'S') 							\n"
          + "		)                                                  											\n"
          + "       OR 																							\n"
          + "		(																							\n"
          + "			A.DATA_FINE < SYSDATE 																	\n"
          + "				AND																					\n"
          + "		 	EXISTS (SELECT A.ID_BANDO                        										\n"
          + "					FROM IUF_T_PROCEDIMENTO C                        								\n"
          + "					WHERE A.ID_BANDO = C.ID_BANDO) 													\n"
          + "  		 ) 																							\n"
          + "   )                           																	\n"
          + " ORDER BY L.ORDINAMENTO                         													\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> livelli = new LinkedList<LivelloDTO>();
              LivelloDTO l = null;
              String misura = "";
              String sottoMisura = "";

              while (rs.next())
              {
                misura = rs.getString("CODICE").split("\\.")[0];
                l = new LivelloDTO();
                if (rs.getString("CODICE").split("\\.").length > 1)
                {
                  sottoMisura = rs.getString("CODICE").split("\\.")[1];
                  l.setCodiceSottoMisura(misura + "." + sottoMisura);
                }
                else
                {
                  continue;
                }

                l.setIdLivello(rs.getLong("ID"));
                l.setDescrizione(rs.getString("DESCRIZIONE"));
                l.setCodice(rs.getString("CODICE"));
                l.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                l.setCodiceMisura(misura);

                livelli.add(l);
              }
              if (livelli.isEmpty())
                return null;

              return livelli;
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
  
  public List<LivelloDTO> getElencoLivelli() throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoLivelli";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                          		    														\n"
          + " L.DESCRIZIONE AS  DESCRIZIONE,  		    														\n"
          + " L.ID_LIVELLO AS ID,  		    																	\n"
          + " L.CODICE AS CODICE,   		    																\n"
          + " L.CODICE_LIVELLO   		    																	\n"
          + " FROM      		    																			\n"
          + "	IUF_D_BANDO A,  		    																	\n"
          + "   IUF_D_TIPO_LIVELLO B,   																	    \n"
          + "   IUF_R_LIVELLO_BANDO LB,					 		  		    									\n"
          + "	IUF_D_LIVELLO L                  		    													\n"
          + " WHERE                         																	\n"
          + "    B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO  "
          + "AND																								\n"
          + "   LB.ID_BANDO = A.ID_BANDO					  		    										\n"
          + " AND 																								\n"
          + "	L.ID_LIVELLO  = LB.ID_LIVELLO                       											\n"
          + "	AND																								\n"
          + " 	 A.FLAG_MASTER <> 'S'																			\n"
          + "   AND(                                                                                            \n"
          + "  		(SYSDATE BETWEEN A.DATA_INIZIO AND NVL(A.DATA_FINE,SYSDATE)									\n"
          + "	AND  																							\n"
          + "		 EXISTS (SELECT B.ID_BANDO                        											\n"
          + "				 FROM IUF_R_BANDO_OGGETTO B                       									\n"
          + "				 WHERE B.ID_BANDO = A.ID_BANDO AND B.FLAG_ATTIVO = 'S') 							\n"
          + "		)                                                  											\n"
          + "       OR 																							\n"
          + "		(																							\n"
          + "			A.DATA_FINE < SYSDATE 																	\n"
          + "				AND																					\n"
          + "		 	EXISTS (SELECT A.ID_BANDO                        										\n"
          + "					FROM IUF_T_PROCEDIMENTO C                        								\n"
          + "					WHERE A.ID_BANDO = C.ID_BANDO) 													\n"
          + "  		 ) 																							\n"
          + "   )                           																	\n"
          + " ORDER BY L.ORDINAMENTO                         													\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> livelli = new LinkedList<LivelloDTO>();
              LivelloDTO l = null;
              String misura = "";
              String sottoMisura = "";

              while (rs.next())
              {
                misura = rs.getString("CODICE_LIVELLO").split("\\.")[0];
                l = new LivelloDTO();
                if (rs.getString("CODICE_LIVELLO").split("\\.").length > 1)
                {
                  sottoMisura = rs.getString("CODICE_LIVELLO").split("\\.")[1];
                  l.setCodiceSottoMisura(misura + "." + sottoMisura);
                }
                else
                {
                  continue;
                }

                l.setIdLivello(rs.getLong("ID"));
                l.setDescrizione(rs.getString("DESCRIZIONE"));
                l.setCodice(rs.getString("CODICE"));
                l.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                l.setCodiceMisura(misura);

                livelli.add(l);
              }
              if (livelli.isEmpty())
                return null;

              return livelli;
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

  public List<NotificaDTO> getNotifiche(long idProcedimento, String attore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getNotifiche";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 														\n"
          + "   N.ID_NOTIFICA ID_NOTIFICA, 								\n"
          + "	  N.NOTE NOTE, 												\n"
          + "	  G.GRAVITA GRAVITA, 										\n"
          + "	  G.DESCRIZIONE DESCR_GRAVITA, 								\n"
          + "	  N.ID_VISIBILITA ID_VISIBILITA,							\n"
          + "   N.DATA_INIZIO DATA_INIZIO,								\n"
          + "   N.DATA_FINE DATA_FINE,									\n"
          + "   V.DESCRIZIONE DESCRIZIONE,								\n"
          + "   N.EXT_ID_UTENTE_AGGIORNAMENTO	UTENTE				        \n"
          + " FROM														\n"
          + "	 IUF_T_NOTIFICA N,											\n"
          + "	 IUF_R_VISIBILITA_ATTORE VA,								\n"
          + "	 IUF_D_GRAVITA_NOTIFICA G,									\n"
          + "  IUF_D_VISIBILITA V											\n"
          + " WHERE														\n"
          + " N.ID_PROCEDIMENTO=:ID_PROCEDIMENTO							\n"
          + " AND															\n"
          + " N.ID_VISIBILITA=VA.ID_VISIBILITA							\n"
          + " AND															\n"
          + " N.ID_GRAVITA_NOTIFICA = G.ID_GRAVITA_NOTIFICA				\n"
          + " AND															\n"
          + " N.ID_VISIBILITA = V.ID_VISIBILITA							\n"
          + " AND															\n"
          + " VA.EXT_COD_ATTORE =:ATTORE									\n"
          + " ORDER BY DATA_INIZIO										\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ATTORE", attore, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<NotificaDTO>>()
          {
            @Override
            public List<NotificaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<NotificaDTO> notifiche = new LinkedList<NotificaDTO>();
              NotificaDTO n = null;
              while (rs.next())
              {
                n = new NotificaDTO();
                n.setIdNotifica(rs.getLong("ID_NOTIFICA"));
                n.setNote(rs.getString("NOTE"));
                n.setGravita(rs.getString("GRAVITA"));
                n.setDescrizioneGravita(rs.getString("DESCR_GRAVITA"));
                n.setIdVisibilita(rs.getLong("ID_VISIBILITA"));
                n.setDataFine(rs.getTimestamp("DATA_FINE"));
                n.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                n.setDescrizione(rs.getString("DESCRIZIONE"));
                n.setIdUtente(rs.getLong("UTENTE"));

                notifiche.add(n);
              }
              if (notifiche.isEmpty())
                return null;

              return notifiche;
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

  public NotificaDTO getNotificaById(long idNotifica)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getNotificaById";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 														\n"
          + "   N.ID_NOTIFICA ID_NOTIFICA, 								\n"
          + "	  N.NOTE NOTE, 												\n"
          + "	  N.ID_GRAVITA_NOTIFICA,									\n"
          + "	  G.GRAVITA GRAVITA, 										\n"
          + "	  G.DESCRIZIONE DESCR_GRAVITA, 								\n"
          + "	  N.ID_VISIBILITA ID_VISIBILITA,							\n"
          + "   N.DATA_INIZIO DATA_INIZIO,								\n"
          + "   N.DATA_FINE DATA_FINE,									\n"
          + "   V.DESCRIZIONE DESCRIZIONE,								\n"
          + "   N.EXT_ID_UTENTE_AGGIORNAMENTO	UTENTE				        \n"
          + " FROM														\n"
          + "	 IUF_T_NOTIFICA N,											\n"
          + "	 IUF_R_VISIBILITA_ATTORE VA,								\n"
          + "	 IUF_D_GRAVITA_NOTIFICA G,									\n"
          + "  IUF_D_VISIBILITA V											\n"
          + " WHERE														\n"
          + "  N.ID_VISIBILITA=VA.ID_VISIBILITA							\n"
          + " AND															\n"
          + "  N.ID_VISIBILITA = V.ID_VISIBILITA							\n"
          + " AND															\n"
          + "  N.ID_GRAVITA_NOTIFICA = G.ID_GRAVITA_NOTIFICA				\n"
          + " AND															\n"
          + "  N.ID_NOTIFICA =:ID_NOTIFICA								\n"
          + " ORDER BY DATA_INIZIO										\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_NOTIFICA", idNotifica, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<NotificaDTO>()
          {
            @Override
            public NotificaDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              NotificaDTO n = null;
              while (rs.next())
              {
                n = new NotificaDTO();
                n.setIdNotifica(rs.getLong("ID_NOTIFICA"));
                n.setNote(rs.getString("NOTE"));
                n.setGravita(rs.getString("GRAVITA"));
                n.setDescrizioneGravita(rs.getString("DESCR_GRAVITA"));
                n.setIdVisibilita(rs.getLong("ID_VISIBILITA"));
                n.setDataFine(rs.getTimestamp("DATA_FINE"));
                n.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                n.setDescrizione(rs.getString("DESCRIZIONE"));
                n.setIdUtente(rs.getLong("UTENTE"));
                n.setIdGravitaNotifica(rs.getLong("ID_GRAVITA_NOTIFICA"));
                return n;
              }
              return n;

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

  public List<VisibilitaDTO> getElencoVisibilitaNotifiche(String attore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getNotifiche";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 														\n"
          + "  V.ID_VISIBILITA ID, V.DESCRIZIONE DESCRIZIONE				\n"
          + " FROM														\n"
          + "	 IUF_R_VISIBILITA_ATTORE VA,								\n"
          + "  IUF_D_VISIBILITA V											\n"
          + " WHERE														\n"
          + " VA.ID_VISIBILITA = V.ID_VISIBILITA							\n"
          + " AND															\n"
          + " VA.EXT_COD_ATTORE =:ATTORE									\n"
          + " ORDER BY DESCRIZIONE										\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ATTORE", attore, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<VisibilitaDTO>>()
          {
            @Override
            public List<VisibilitaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<VisibilitaDTO> elencoVisibilita = new LinkedList<VisibilitaDTO>();
              VisibilitaDTO v = null;
              while (rs.next())
              {
                v = new VisibilitaDTO();
                v.setDescrizione(rs.getString("DESCRIZIONE"));
                v.setId(rs.getLong("ID"));

                elencoVisibilita.add(v);
              }
              if (elencoVisibilita.isEmpty())
                return null;

              return elencoVisibilita;
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

  public void deleteNotifica(long idNotifica) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteNotifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = " DELETE FROM IUF_T_NOTIFICA	                                        \n"
        + "        WHERE ID_NOTIFICA = :ID_NOTIFICA					            \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_NOTIFICA", idNotifica, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idNotifica", idNotifica),

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

  public void insertNotifica(NotificaDTO notifica, long idProcedimento,
      boolean isNewinsert) throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::insertNotifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String INSERT = "INSERT INTO IUF_T_NOTIFICA \n"
        + " (ID_NOTIFICA, NOTE, ID_GRAVITA_NOTIFICA, ID_VISIBILITA, ID_PROCEDIMENTO, DATA_INIZIO,";
    if (notifica.getStato().compareTo("Chiusa") == 0)
      INSERT += "DATA_FINE,";
    INSERT += "EXT_ID_UTENTE_AGGIORNAMENTO)  \n"
        + "VALUES 								      \n"
        + " (:ID_NOTIFICA,:NOTE,:ID_GRAVITA,:ID_VISIBILITA,:ID_PROCEDIMENTO,";
    if (isNewinsert)
      INSERT += " SYSDATE,";
    else
      INSERT += " :DATA_INIZIO,";

    if (notifica.getStato().compareTo("Chiusa") == 0)
      INSERT += "SYSDATE,";
    INSERT += " :EXT_ID_UTENTE_AGGIORNAMENTO)  \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      if (!isNewinsert)
        mapParameterSource.addValue("ID_NOTIFICA", notifica.getIdNotifica(),
            Types.NUMERIC);
      else
        mapParameterSource.addValue("ID_NOTIFICA",
            getNextSequenceValue("SEQ_IUF_T_NOTIFICA"), Types.NUMERIC);

      mapParameterSource.addValue("NOTE", notifica.getNote(), Types.VARCHAR);
      mapParameterSource.addValue("ID_GRAVITA", notifica.getIdGravitaNotifica(),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_VISIBILITA", notifica.getIdVisibilita(),
          Types.NUMERIC);
      if (!isNewinsert)
        mapParameterSource.addValue("DATA_INIZIO", notifica.getDataInizio(),
            Types.TIMESTAMP);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          notifica.getIdUtente(), Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimento", idProcedimento),
              new LogParameter("note", notifica.getNote()),
              new LogParameter("idNotifica", notifica.getIdNotifica()),
              new LogParameter("gravita", notifica.getGravita()),
              new LogParameter("idVisibilita", notifica.getIdVisibilita()),
              new LogParameter("dataInizio", notifica.getDataInizio()),
              new LogParameter("stato", notifica.getStato()),
              new LogParameter("extIdUtenteAggiornamento",
                  notifica.getIdUtente()),
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

  public List<LivelloDTO> getMisure() throws InternalUnexpectedException
  {
    String THIS_METHOD = "getMisure";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 														\n"
          + "  ID_LIVELLO, CODICE, DESCRIZIONE							\n"
          + " FROM														\n"
          + "  IUF_D_LIVELLO L											\n"
          + " WHERE														\n"
          + "  L.ID_TIPOLOGIA_LIVELLO = 1									\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> elencoMisure = new LinkedList<LivelloDTO>();
              LivelloDTO v = null;
              while (rs.next())
              {
                v = new LivelloDTO();
                v.setDescrizione(rs.getString("DESCRIZIONE"));
                v.setIdLivello(rs.getLong("ID_LIVELLO"));
                v.setCodice(rs.getString("CODICE"));

                elencoMisure.add(v);
              }
              if (elencoMisure.isEmpty())
                return null;

              return elencoMisure;
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

  public List<LivelloDTO> getOperazioni(Vector<Long> idMisureSelezionate)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getOperazioni";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 														\n"
          + "  L.ID_LIVELLO, L.CODICE, L.DESCRIZIONE						\n"
          + " FROM														\n"
          + "  IUF_D_LIVELLO L, IUF_R_LIVELLO_PADRE P						\n"
          + " WHERE														\n"
          + "	 L.ID_LIVELLO = P.ID_LIVELLO AND							\n"
          + "  L.ID_TIPOLOGIA_LIVELLO = 3 AND								\n"
          + "	 P.ID_LIVELLO_PADRE IN (									\n"
          + "		SELECT L2.ID_LIVELLO									\n"
          + "		FROM IUF_R_LIVELLO_PADRE LP,IUF_D_LIVELLO L2			\n"
          + "		WHERE LP.ID_LIVELLO = L2.ID_LIVELLO						\n"
          + "		AND L2.ID_TIPOLOGIA_LIVELLO = 2							\n"
          + getInCondition("LP.ID_LIVELLO_PADRE", idMisureSelezionate)
          + "	) ORDER BY L.ORDINAMENTO										";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> elencoMisure = new LinkedList<LivelloDTO>();
              LivelloDTO v = null;
              while (rs.next())
              {
                v = new LivelloDTO();
                v.setDescrizione(rs.getString("DESCRIZIONE"));
                v.setIdLivello(rs.getLong("ID_LIVELLO"));
                v.setCodice(rs.getString("CODICE"));
                elencoMisure.add(v);
              }
              if (elencoMisure.isEmpty())
                return null;

              return elencoMisure;
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

  public List<SettoriDiProduzioneDTO> getElencoSettoriBandi()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoSettoriBandi";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT                   \n"
          + "   S.CODICE,                       \n"
          + "   S.ID_SETTORE                    \n"
          + " FROM                              \n"
          + "   IUF_D_BANDO B,                  \n"
          + "   IUF_R_LIVELLO_BANDO L,          \n"
          + "   IUF_D_LIVELLO D,                \n"
          + "   IUF_D_SETTORE S                 \n"
          + " WHERE                             \n"
          + "   B.ID_BANDO = L.ID_BANDO         \n"
          + "   AND L.ID_LIVELLO = D.ID_LIVELLO \n"
          + "   AND D.ID_SETTORE = S.ID_SETTORE \n"
          + "   ORDER BY S.CODICE      			\n";

      return namedParameterJdbcTemplate.query(SELECT,
          (MapSqlParameterSource) null,
          new ResultSetExtractor<List<SettoriDiProduzioneDTO>>()
          {
            @Override
            public List<SettoriDiProduzioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<SettoriDiProduzioneDTO> list = new ArrayList<SettoriDiProduzioneDTO>();
              SettoriDiProduzioneDTO item = null;
              while (rs.next())
              {
                item = new SettoriDiProduzioneDTO();
                item.setIdSettore(rs.getLong("ID_SETTORE"));
                item.setDescrizione(rs.getString("CODICE"));
                list.add(item);
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

  public List<FocusAreaDTO> getElencoFocusAreaBandi(int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoFocusAreaBandi";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT   DISTINCT                       \n"
          + "   F.ID_FOCUS_AREA,                      \n"
          + "   D.CODICE                              \n"
          + " FROM                                    \n"
          + "   IUF_D_BANDO B,                        \n"
          + "   IUF_R_LIVELLO_BANDO L,                \n"
          + "   IUF_R_LIVELLO_FOCUS_AREA F,           \n"
          + "   IUF_D_FOCUS_AREA D                    \n"
          + " WHERE                                   \n"
          + "   B.ID_BANDO = L.ID_BANDO               \n"
          + "   AND B.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO"
          + "   AND F.ID_LIVELLO = L.ID_LIVELLO       \n"
          + "   AND D.ID_FOCUS_AREA = F.ID_FOCUS_AREA \n"
          + "   AND F.ID_PIANO_FINANZIARIO = 1        \n"
          + "   ORDER BY D.CODICE            		  \n";

      MapSqlParameterSource mapParameter = new MapSqlParameterSource();
      mapParameter.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo);
      return namedParameterJdbcTemplate.query(SELECT,
          mapParameter,
          new ResultSetExtractor<List<FocusAreaDTO>>()
          {
            @Override
            public List<FocusAreaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<FocusAreaDTO> list = new ArrayList<FocusAreaDTO>();
              FocusAreaDTO item = null;
              if(rs!=null){
            	  while (rs.next())
                  {
                    item = new FocusAreaDTO();
                    item.setIdFocusArea(rs.getLong("ID_FOCUS_AREA"));
                    item.setCodice(rs.getString("CODICE"));
                    list.add(item);
                  }                 
                }
               return list;
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

  public List<FocusAreaDTO> getElencoFocusArea(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoFocusArea";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                  \n"
          + "   F.ID_FOCUS_AREA,                      \n"
          + "   D.CODICE                              \n"
          + " FROM                                    \n"
          + "   IUF_D_BANDO B,                        \n"
          + "   IUF_R_LIVELLO_BANDO L,                \n"
          + "   IUF_R_LIVELLO_FOCUS_AREA F,           \n"
          + "   IUF_D_FOCUS_AREA D                    \n"
          + " WHERE                                   \n"
          + "   B.ID_BANDO = L.ID_BANDO               \n"
          + "   AND F.ID_LIVELLO = L.ID_LIVELLO       \n"
          + "   AND D.ID_FOCUS_AREA = F.ID_FOCUS_AREA \n"
          + "   AND F.ID_PIANO_FINANZIARIO = 1        \n"
          + "   AND B.ID_BANDO = :ID_BANDO            \n";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<FocusAreaDTO>>()
          {
            @Override
            public List<FocusAreaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<FocusAreaDTO> list = new ArrayList<FocusAreaDTO>();
              FocusAreaDTO item = null;
              while (rs.next())
              {
                item = new FocusAreaDTO();
                item.setIdFocusArea(rs.getLong("ID_FOCUS_AREA"));
                item.setCodice(rs.getString("CODICE"));
                list.add(item);
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

  public List<SettoriDiProduzioneDTO> getElencoSettori(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoSettori";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT                   \n"
          + "   S.CODICE,                       \n"
          + "   S.ID_SETTORE                    \n"
          + " FROM                              \n"
          + "   IUF_D_BANDO B,                  \n"
          + "   IUF_R_LIVELLO_BANDO L,          \n"
          + "   IUF_D_LIVELLO D,                \n"
          + "   IUF_D_SETTORE S                 \n"
          + " WHERE                             \n"
          + "   B.ID_BANDO = L.ID_BANDO         \n"
          + "   AND L.ID_LIVELLO = D.ID_LIVELLO \n"
          + "   AND D.ID_SETTORE = S.ID_SETTORE \n"
          + "   AND B.ID_BANDO = :ID_BANDO      \n";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<SettoriDiProduzioneDTO>>()
          {
            @Override
            public List<SettoriDiProduzioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<SettoriDiProduzioneDTO> list = new ArrayList<SettoriDiProduzioneDTO>();
              SettoriDiProduzioneDTO item = null;
              while (rs.next())
              {
                item = new SettoriDiProduzioneDTO();
                item.setIdSettore(rs.getLong("ID_SETTORE"));
                item.setDescrizione(rs.getString("CODICE"));
                list.add(item);
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

  public List<ProcedimentoOggettoVO> searchProcedimentiEstrazione(
      List<Long> idsP, List<Long> idsPO, List<Long> idsPExP,
      List<Long> idsPEOxP, UtenteAbilitazioni utenteAbilitazioni,
      HashMap<String, FiltroVO> mapFilters, String limit, String offset)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "searchProcedimentiEstrazione";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      /*
       * SELECT.append(
       * " SELECT *                                                                       \n"
       * +
       * "   FROM (                                                                       \n"
       * +
       * "      SELECT ROWNUM RN, TMP.*                                                   \n"
       * +
       * "         FROM (                                                                 \n"
       * );
       */

      SELECT.append("  SELECT  DISTINCT                                      \n"
          + "   P.ID_PROCEDIMENTO,                                   \n"
          + "   DLIV.ID_LIVELLO,                                     \n"
          + "   DLIV.CODICE_LIVELLO AS COD_LIVELLO,                  \n"
          + "   P.IDENTIFICATIVO,                                    \n"
          + "   VAM.DESCRIZIONE AS DESCR_AMM_COMPETENZA,             \n"
          + "   B.ANNO_CAMPAGNA,                                     \n"
          + "   B.DENOMINAZIONE AS DENOMINAZIONE_BANDO,              \n"
          + "   SO.DESCRIZIONE,                                      \n"
          + "   P.DATA_ULTIMO_AGGIORNAMENTO,                         \n"
          + "   VA.CUAA,                                             \n"
          + "   VDA2.DESCRIZIONE_PROVINCIA,                          \n"
          + "   VDA2.DESCRIZIONE_COMUNE,                             \n"
          + "   VA.INDIRIZZO_SEDE_LEGALE,                            \n"
          + "   VA.DENOMINAZIONE AS DENOMINAZIONE_AZIENDA,           \n"
          + "   VD.DENOMINAZIONE AS DENOMINAZIONE_DELEGA,            \n"
          + "   VA.ID_AZIENDA,                                       \n");

      if (idsPO != null || idsP != null)
        SELECT.append("    pestr.flag_estratta	     				         \n");
      if (idsPExP != null)
        SELECT.append("    pestrexp.flag_estratta	     				        \n");

      if (idsPO != null)
        SELECT.append(", po.IDENTIFICATIVO as IDENTIFICATIVO_OGGETTO,	 \n"
            + " 	po.id_procedimento_oggetto as ID_PROCEDIMENTO_OGGETTO,						 \n"
            + " 	ogg.descrizione as TIPO_OGGETTO,				 \n"
            + "		so2.descrizione as STATO_OGGETTO				 	 \n");

      SELECT.append("  FROM                                        \n"
          + "    IUF_T_PROCEDIMENTO P,                                   \n"
          //+ "    IUF_T_PROCEDIMENTO_OGGETTO PO,                          \n"
          //+ "    IUF_D_STATO_OGGETTO SO2,                              \n"
         // + "    IUF_T_ITER_PROCEDIMENTO_OGGE IPO2,                              \n"
          + "    IUF_T_PROCEDIMENTO_AZIENDA PA,                          \n"
          + "    IUF_T_PROCEDIM_AMMINISTRAZIO AM,                      \n"
          + "    SMRGAA_V_DATI_ANAGRAFICI VA,                            \n"
          + "    SMRGAA_V_DATI_AMMINISTRATIVI VDA2,                      \n"
          + "    SMRCOMUNE_V_AMM_COMPETENZA VAM,                         \n"
          + "    IUF_R_PROCEDIMENTO_LIVELLO PLIV,                        \n"
          + "    IUF_D_LIVELLO DLIV,	                        		 \n"
          + "    IUF_D_BANDO B,                                          \n");

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append(" SMRGAA_V_DATI_DELEGA C, ");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          SELECT.append(" SMRGAA_V_SOGGETTI_COLLEGATI C, ");
        }

      SELECT.append(
          " IUF_D_STATO_OGGETTO SO,                                  \n"
              + "   SMRGAA_V_DATI_DELEGA VD                                  \n");

      if (idsP != null || idsPO != null)
        SELECT.append(
            ",   IUF_T_PROCEDIMENTO_ESTRATTO pestr                       \n");

      if(idsPO!=null){
        SELECT.append( ",   IUF_T_PROCEDIMENTO_OGGETTO PO                  \n");
        SELECT.append( ",   IUF_D_STATO_OGGETTO SO2                        \n");
        SELECT.append( ",   IUF_T_ITER_PROCEDIMENTO_OGGE IPO2            \n");
      }
      
      if (idsPExP != null)
        SELECT.append(", 				                                            \n"
            + "		IUF_T_PROCEDIMENTO_ESTR_EXPO 		pestrexp	              \n");

            
      if (idsPO != null)
        SELECT.append(", 				 									                          \n"
            + "	  IUF_R_LEGAME_GRUPPO_OGGETTO	 lgo,					              \n"
            + " 	IUF_D_OGGETTO ogg,								 	                    \n"
            + "		IUF_T_ITER_PROCEDIMENTO_OGGE 		ipo			 	              \n"
            );

      SELECT
          .append(" WHERE                                                     \n"
              //+ "    P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                  \n"
              + "    PA.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                   \n"
              + "    AND B.ID_BANDO = P.ID_BANDO                              \n"
              + "    AND SO.ID_STATO_OGGETTO = P.ID_STATO_OGGETTO             \n"
              + "    AND PLIV.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO             \n"
              + "    AND PLIV.ID_LIVELLO = DLIV.ID_LIVELLO                    \n"
              + "    AND PA.DATA_FINE IS NULL                                 \n"
              + "    AND AM.ID_PROCEDIMENTO(+) = P.ID_PROCEDIMENTO            \n"
              + "    AND AM.DATA_FINE(+) IS NULL                              \n"
              + "    AND VA.ID_AZIENDA = PA.EXT_ID_AZIENDA                    \n"
              + "    AND VA.DATA_FINE_VALIDITA IS NULL                        \n"
              + "    AND VAM.ID_AMM_COMPETENZA(+) = AM.EXT_ID_AMM_COMPETENZA  \n"
              + "    AND VD.ID_AZIENDA(+) = PA.EXT_ID_AZIENDA                 \n"
              + "    AND VDA2.ISTAT_COMUNE = VA.ISTAT_COMUNE_SEDE_LEGALE 	    \n"
              + "    AND VD.DATA_FINE_VALIDITA(+) IS NULL                	    \n");

      if (idsP != null)
      {
        SELECT.append(getInCondition("p.id_procedimento", idsP));
        SELECT
            .append("   \n and pestr.id_procedimento = p.id_procedimento \n ");
      }

      if (idsPExP != null)
      {
        SELECT.append(getInCondition("p.id_procedimento", idsPExP));
        SELECT.append(
            "   \n and pestrexp.id_procedimento = p.id_procedimento \n ");
      } 
    
      if (idsPO != null)
      {
        SELECT.append(getInCondition("po.id_procedimento_oggetto", idsPO));
        SELECT.append("    and 														\n"
            + " pestr.id_procedimento_oggetto = po.id_procedimento_oggetto   		\n"          
            + "    AND PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO               \n"

            + " and	po.id_legame_gruppo_oggetto = lgo.id_legame_gruppo_oggetto		\n"
            + " and lgo.id_oggetto = ogg.id_oggetto									\n"
            + " and ipo.id_procedimento_oggetto = po.id_procedimento_oggetto        \n"
            + " and ipo2.id_procedimento_oggetto = po.id_procedimento_oggetto   \n"
            + " and ipo2.data_fine is null                          \n"
            + " and ipo2.id_stato_oggetto = so2.id_stato_oggetto            \n"
            + " 	\n");
      }

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append("   \n"
            + "	AND VA.ID_AZIENDA = C.ID_AZIENDA						\n"
            + " AND C.DATA_FINE_VALIDITA IS NULL						\n"
            + " AND ( C.ID_INTERMEDIARIO_ZONA = :ID_INTERMEDIARIO  		\n"
            + "		 OR C.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO 	\n"
            + "      OR C.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO ) 	\n");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          SELECT.append(" AND VA.ID_AZIENDA = C.ID_AZIENDA						\n"
              + " AND C.DATA_FINE_RUOLO IS NULL							\n"
              + " AND C.CODICE_FISCALE = :CODICE_FISCALE  				\n");
        }

      SELECT.append(" ORDER BY P.IDENTIFICATIVO,DLIV.CODICE_LIVELLO  			\n");
      if (idsPO != null)
      {
        SELECT.append(", TIPO_OGGETTO, IDENTIFICATIVO_OGGETTO 				\n");
      }

      // manage pagination
      /*
       * SELECT.append(
       * "           ) tmp                                                  \n"
       * +
       * "           WHERE ROWNUM <= :END_FETCH                                          \n"
       * +
       * "        )                                                                      \n"
       * +
       * "  WHERE RN > :START_FETCH                                                      \n"
       * );
       */

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      parameterSource.addValue("START_FETCH", offset, Types.NUMERIC);
      parameterSource.addValue("END_FETCH", (offset + limit + 10),
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<ProcedimentoOggettoVO>>()
          {
            @Override
            public List<ProcedimentoOggettoVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<ProcedimentoOggettoVO> list = new ArrayList<ProcedimentoOggettoVO>();
              ArrayList<String> elencoLivelli = null;
              ProcedimentoOggettoVO procOggetto = null;

              long idProcedimentoLast = -1;
              long idProcedimento = -1;
              long idProcedimentoOggettoLast = -1;
              long idProcedimentoOggetto = -1;

              while (rs.next())
              {
                // Devo controllare idProcedimento perchè la query mi
                // restituisce più record se l'azienda ha più
                // procediemnto_oggetti
                // Sono sicuro che prendo l'ultimo id_procedimento_oggetto
                // perchè è una condizione di orderBY
                if (idsP != null || idsPExP != null)
                  idProcedimento = rs.getLong("ID_PROCEDIMENTO");

                if (idsPO != null)
                  idProcedimentoOggetto = rs.getLong("ID_PROCEDIMENTO_OGGETTO");

                if (idProcedimentoLast != idProcedimento
                    || idProcedimentoOggettoLast != idProcedimentoOggetto)
                {
                  idProcedimentoLast = idProcedimento;
                  idProcedimentoOggettoLast = idProcedimentoOggetto;
                  procOggetto = new ProcedimentoOggettoVO();
                  procOggetto.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                  procOggetto.setDescrAmmCompetenza(
                      rs.getString("DESCR_AMM_COMPETENZA"));
                  procOggetto.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                  procOggetto.setDenominazioneBando(
                      rs.getString("DENOMINAZIONE_BANDO"));
                  procOggetto.setDescrizione(rs.getString("DESCRIZIONE"));
                  procOggetto.setDataUltimoAggiornamento(IuffiUtils.DATE
                      .formatDate(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO")));
                  procOggetto.setCuaa(rs.getString("CUAA"));
                  procOggetto.setIndirizzoSedeLegale(
                      rs.getString("INDIRIZZO_SEDE_LEGALE"));
                  procOggetto.setDenominazioneAzienda(
                      rs.getString("DENOMINAZIONE_AZIENDA"));
                  procOggetto
                      .setDescrComune(rs.getString("DESCRIZIONE_COMUNE"));
                  procOggetto
                      .setDescrProvincia(rs.getString("DESCRIZIONE_PROVINCIA"));
                  procOggetto.setDenominzioneDelega(
                      rs.getString("DENOMINAZIONE_DELEGA"));
                  procOggetto.setIdAzienda(rs.getLong("ID_AZIENDA"));
                  procOggetto.setIdProcedimento(rs.getLong("ID_PROCEDIMENTO"));
                  procOggetto.setFlagEstratta(rs.getString("FLAG_ESTRATTA"));

                  if (idsPO != null)
                  {
                    procOggetto.setPO(true);
                    procOggetto.setIdentificativoOggetto(
                        rs.getString("IDENTIFICATIVO_OGGETTO"));
                    procOggetto.setTipoOggetto(rs.getString("TIPO_OGGETTO"));
                    procOggetto.setStatoOggetto(rs.getString("STATO_OGGETTO"));
                  }
                  else
                    procOggetto.setPO(false);

                  elencoLivelli = new ArrayList<String>();
                  procOggetto.setElencoCodiciLivelli(elencoLivelli);
                  list.add(procOggetto);
                }

                elencoLivelli.add(rs.getString("COD_LIVELLO"));
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

  public List<Long> elencoIdProcedimentiEstratti(Long idEstrazione,
      String limit, String offset, HashMap<String, FiltroVO> mapFilters)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "elencoIdProcedimentiEstratti";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      if (limit != null && offset != null)
        SELECT.append(
            " SELECT *                                                        \n"
                + "   FROM (                                                                  \n"
                + "      SELECT ROWNUM RN, TMP.*                                              \n"
                + "         FROM (                                                            \n");

      SELECT.append(
          "  SELECT  DISTINCT                                          	\n"
              + "    P.ID_PROCEDIMENTO                                       	\n"
              + "  FROM                                                      	\n"
              + "    IUF_T_PROCEDIMENTO_ESTRATTO PE,                         	\n"
              + "    IUF_T_PROCEDIMENTO P	                      		     	\n"
              + "  where PE.ID_ESTRAZIONE_CAMPIONE =:ID_ESTRAZIONE  		 	\n"
              + "	and PE.id_procedimento = P.ID_PROCEDIMENTO   and 		 	\n"
              + " PE.ID_PROCEDIMENTO_OGGETTO is null           				 	\n");
      if (mapFilters != null && !mapFilters.isEmpty())
        if (mapFilters.containsKey("flagEstrattaStr"))
        {
          String s = "  AND PE.FLAG_ESTRATTA IN                             \n";
          final List<Long> values = mapFilters.get("flagEstrattaStr")
              .getValues();
          if (values != null)
          {
            s += "(";
            for (long id : values)
            {
              if (ProcedimentoOggettoVO.getFlagEstrattaFromId(id) != null)
                s += ProcedimentoOggettoVO.getFlagEstrattaFromId(id) + ",";
            }

            s = s.toString().substring(0, s.length() - 1); // RIMUOVO ULTIMA
                                                           // VIRGOLA
            SELECT.append(s + " ) ");
          }
        }
      if (limit != null && offset != null)
        SELECT.append(
            "           ) tmp                                              \n"
                + "           WHERE ROWNUM <= :END_FETCH                                          \n"
                + "        )                                                                      \n"
                + "  WHERE RN > :START_FETCH                                                      \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ESTRAZIONE", idEstrazione, Types.NUMERIC);

      if (limit != null && offset != null)
      {
        int offsetInt = Integer.parseInt(offset);
        int limitInt = Integer.parseInt(limit);
        int x = offsetInt + limitInt;
        parameterSource.addValue("START_FETCH", offsetInt, Types.NUMERIC);
        parameterSource.addValue("END_FETCH", x, Types.NUMERIC);
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
                list.add(rs.getLong("ID_PROCEDIMENTO"));
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

  public List<Long> elencoIdProcedimentiOggettoEstratti(Long idEstrazione,
      String limit, String offset, HashMap<String, FiltroVO> mapFilters)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "elencoIdProcedimentiOggettoEstratti";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      if (limit != null && offset != null)
        SELECT.append(
            " SELECT *                                                           	\n"
                + "   FROM (                                                                      \n"
                + "      SELECT ROWNUM RN, TMP.*                                                  \n"
                + "         FROM (                                                                \n");

      SELECT.append(
          "  SELECT  DISTINCT                                          			\n"
              + "    P.ID_PROCEDIMENTO_OGGETTO		                               		\n"
              + "  FROM                                                      			\n"
              + "    IUF_T_PROCEDIMENTO_ESTRATTO PE,                         			\n"
              + "    IUF_T_PROCEDIMENTO_OGGETTO P	                      	 			\n"
              + "  where PE.ID_ESTRAZIONE_CAMPIONE =:ID_ESTRAZIONE           			\n"
              + "	and PE.id_procedimento_OGGETTO = P.ID_PROCEDIMENTO_OGGETTO       	\n");
      if (mapFilters != null && !mapFilters.isEmpty())
        if (mapFilters.containsKey("flagEstrattaStr"))
        {
          String s = "  AND PE.FLAG_ESTRATTA IN                                    	\n";
          final List<Long> values = mapFilters.get("flagEstrattaStr")
              .getValues();
          if (values != null)
          {
            s += "(";
            for (long id : values)
            {
              if (ProcedimentoOggettoVO.getFlagEstrattaFromId(id) != null)
                s += ProcedimentoOggettoVO.getFlagEstrattaFromId(id) + ",";
            }

            s = s.toString().substring(0, s.length() - 1); // RIMUOVO ULTIMA
                                                           // VIRGOLA
            SELECT.append(s + " ) ");
          }
        }

      if (limit != null && offset != null)
        SELECT.append(
            "           ) tmp                                                  	\n"
                + "           WHERE ROWNUM <= :END_FETCH                                  \n"
                + "        )                                                              \n"
                + "  WHERE RN > :START_FETCH                                              \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ESTRAZIONE", idEstrazione, Types.NUMERIC);

      if (limit != null && offset != null)
      {
        int offsetInt = Integer.parseInt(offset);
        int limitInt = Integer.parseInt(limit);
        int x = offsetInt + limitInt;
        parameterSource.addValue("START_FETCH", offsetInt, Types.NUMERIC);
        parameterSource.addValue("END_FETCH", x, Types.NUMERIC);
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
                list.add(rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
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

  public int searchProcedimentiEstrazioneCount(List<Long> idsP,
      List<Long> idsPO, UtenteAbilitazioni utenteAbilitazioni,
      HashMap<String, FiltroVO> mapFilters) throws InternalUnexpectedException
  {
    String THIS_METHOD = "searchProcedimentiEstrazioneCount";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      SELECT.append(
          " select count(*) from( SELECT  DISTINCT                 	 		\n"
              + "   P.ID_PROCEDIMENTO                                   	 		\n");

      SELECT.append("  FROM                                        		\n"
          + "    IUF_T_PROCEDIMENTO P,                                   		\n"
          + "    IUF_T_PROCEDIMENTO_OGGETTO PO,                          		\n"
          + "    IUF_T_PROCEDIMENTO_AZIENDA PA,                          		\n"
          + "    IUF_T_PROCEDIM_AMMINISTRAZIO AM,                      		\n"
          + "    SMRGAA_V_DATI_ANAGRAFICI VA,                            		\n"
          + "    SMRGAA_V_DATI_AMMINISTRATIVI VDA2,                      		\n"
          + "    SMRCOMUNE_V_AMM_COMPETENZA VAM,                         		\n"
          + "    IUF_R_PROCEDIMENTO_LIVELLO PLIV,                        		\n"
          + "    IUF_D_LIVELLO DLIV,	                        		 		\n"
          + "    IUF_D_BANDO B,                                          		\n");

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {

        SELECT.append(" SMRGAA_V_DATI_DELEGA C, 										\n");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {

          SELECT.append(" SMRGAA_V_SOGGETTI_COLLEGATI C, 									\n");
        }

      SELECT.append(
          " IUF_D_STATO_OGGETTO SO,                                  			\n"
              + "   SMRGAA_V_DATI_DELEGA VD,                                 			\n"
              + "   IUF_T_PROCEDIMENTO_ESTRATTO pestr                        			\n");

      if (idsPO != null)
        SELECT.append(", 				 									 			\n"
            + "	    IUF_R_LEGAME_GRUPPO_OGGETTO	 lgo,					 			\n"
            + " 	IUF_D_OGGETTO ogg,								 	 			\n"
            + "		IUF_T_ITER_PROCEDIMENTO_OGGE 		ipo			 	 			\n");

      SELECT.append(
          " WHERE                                                   			\n"
              + "    P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                  			\n"
              + "    AND PA.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO              			\n"
              + "    AND PA.DATA_FINE IS NULL                                			\n"
              + "    AND AM.ID_PROCEDIMENTO(+) = P.ID_PROCEDIMENTO           			\n"
              + "    AND AM.DATA_FINE(+) IS NULL                             			\n"
              + "    AND VA.ID_AZIENDA = PA.EXT_ID_AZIENDA                   			\n"
              + "    AND VA.DATA_FINE_VALIDITA IS NULL                       			\n"
              + "    AND VAM.ID_AMM_COMPETENZA(+) = AM.EXT_ID_AMM_COMPETENZA 			\n"
              + "    AND B.ID_BANDO = P.ID_BANDO                             			\n"
              + "    AND SO.ID_STATO_OGGETTO = P.ID_STATO_OGGETTO            			\n"
              + "    AND VD.ID_AZIENDA(+) = PA.EXT_ID_AZIENDA                			\n"
              + "    AND VDA2.ISTAT_COMUNE = VA.ISTAT_COMUNE_SEDE_LEGALE 	 			\n"
              + "    AND VD.DATA_FINE_VALIDITA(+) IS NULL                	 			\n");

      if (idsP != null)
      {
        SELECT.append(getInCondition("p.id_procedimento", idsP));
        SELECT
            .append("   \n and pestr.id_procedimento = p.id_procedimento 	\n");
      }

      if (idsPO != null)
      {
        SELECT.append(getInCondition("po.id_procedimento_oggetto", idsPO));
        SELECT.append("    and 												\n"
            + " pestr.id_procedimento_oggetto = po.id_procedimento_oggetto   	\n"
            + " and	po.id_legame_gruppo_oggetto = lgo.id_legame_gruppo_oggetto	\n"
            + " and lgo.id_oggetto = ogg.id_oggetto								\n"
            + " and ipo.id_procedimento_oggetto = po.id_procedimento_oggetto	\n"
            + " 	\n");
      }

      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SELECT.append("   \n"
            + "	AND VA.ID_AZIENDA = C.ID_AZIENDA								\n"
            + " AND C.DATA_FINE_VALIDITA IS NULL								\n"
            + " AND ( C.ID_INTERMEDIARIO_ZONA = :ID_INTERMEDIARIO  				\n"
            + "		 OR C.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO 			\n"
            + "      OR C.ID_INTERMEDIARIO_REG  = :ID_INTERMEDIARIO ) 			\n");
      }
      else
        if (isBeneficiario(utenteAbilitazioni))
        {
          SELECT.append(" AND VA.ID_AZIENDA = C.ID_AZIENDA								\n"
              + " AND C.DATA_FINE_RUOLO IS NULL									\n"
              + " AND C.CODICE_FISCALE = :CODICE_FISCALE  						\n");
        }

      SELECT.append(" )");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      return namedParameterJdbcTemplate.queryForInt(SELECT.toString(),
          parameterSource);
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

  public List<ProcedimentoOggettoVO> getElencoTipologieEstrazione()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoTipologieEstrazione";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      SELECT
          .append("  SELECT  DISTINCT                                      \n" +
              "	pestr.flag_estratta	             					 \n" +
              "  FROM                                        			 \n" +
              "   IUF_T_PROCEDIMENTO_ESTRATTO pestr                    \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<ProcedimentoOggettoVO>>()
          {
            @Override
            public List<ProcedimentoOggettoVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<ProcedimentoOggettoVO> list = new ArrayList<ProcedimentoOggettoVO>();
              ProcedimentoOggettoVO procOggetto = null;

              while (rs.next())
              {
                procOggetto = new ProcedimentoOggettoVO();
                procOggetto.setFlagEstratta(rs.getString("FLAG_ESTRATTA"));
                list.add(procOggetto);
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

  public List<DecodificaDTO<String>> getGestoriFascicolo(long idBando,
      long idIntermediario) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getGestoriFascicolo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT DISTINCT                                     \n"
        + "   C.ID_INTERMEDIARIO_ZONA AS ID,                    \n"
        + "   C.ID_INTERMEDIARIO_ZONA AS CODICE,                \n"
        + "   C.DENOMINAZIONE AS DESCRIZIONE                    \n"
        + " FROM                                                \n"
        + "   IUF_T_PROCEDIMENTO A,                             \n"
        + "   IUF_T_PROCEDIMENTO_AZIENDA B,                     \n"
        + "   SMRGAA_V_DATI_DELEGA C                            \n"
        + " WHERE                                               \n"
        + "   A.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO             \n"
        + "   AND C.ID_AZIENDA = B.EXT_ID_AZIENDA               \n"
        + "   AND C.DATA_FINE_MANDATO IS NULL		              \n"
        + "   AND (                                             \n"
        + "       C.ID_INTERMEDIARIO_PROV = :ID_INTERMEDIARIO   \n"
        + "       OR C.ID_INTERMEDIARIO_ZONA= :ID_INTERMEDIARIO \n"
        + "       OR C.ID_INTERMEDIARIO_REG = :ID_INTERMEDIARIO \n"
        + "       )                                             \n"
        + "   AND A.ID_BANDO = :ID_BANDO                        \n"
        + "   AND B.DATA_FINE IS NULL                           \n"
        + " ORDER BY C.DENOMINAZIONE ASC                        \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    mapParameterSource.addValue("ID_INTERMEDIARIO", idIntermediario,
        Types.NUMERIC);
    try
    {
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public List<LivelloDTO> getMisureConFlagTipo(int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getMisureConFlagTipo";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT  											\n"
          + "		C.CODICE_LIVELLO,  										\n"
          + "		C.ID_LIVELLO,  											\n"
          + "		C.DESCRIZIONE,  										\n"
          + "		F.CODICE AS COD_TIPO									\n"
          + " FROM  							 							\n"
          + "		IUF_R_LIVELLO_PADRE A, 									\n"
          + "		IUF_R_LIVELLO_PADRE B, 									\n"
          + "		IUF_D_LIVELLO C, 			 							\n"
          + "		IUF_R_LIVELLO_BANDO D, 									\n"
          + "		IUF_D_BANDO E, 											\n"
          + "		IUF_D_TIPO_LIVELLO F			 						\n"
          + " WHERE  						 								\n"
          + "		D.ID_LIVELLO = B.ID_LIVELLO 							\n"
          + " 	AND E.ID_BANDO = D.ID_BANDO 							\n"
          + " 	AND E.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO 							\n"
          + " 	AND E.FLAG_MASTER='S' 									\n"
          + " 	AND F.ID_TIPO_LIVELLO = E.ID_TIPO_LIVELLO				\n"
          + " 	AND B.ID_LIVELLO_PADRE=A.ID_LIVELLO						\n"
          + " 	AND A.ID_LIVELLO_PADRE=C.ID_LIVELLO						\n"
          + " ORDER BY length(C.CODICE_LIVELLO),C.CODICE_LIVELLO			\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> elencoMisure = new LinkedList<LivelloDTO>();
              LivelloDTO v = null;
              Long idLivelloLast = -1l;
              while (rs.next())
              {
                Long idLivello = rs.getLong("ID_LIVELLO");
                if (idLivello != idLivelloLast)
                {
                  v = new LivelloDTO();
                  v.setIdLivello(rs.getLong("ID_LIVELLO"));
                  v.setCodice(rs.getString("CODICE_LIVELLO"));
                  v.setDescrizione(rs.getString("DESCRIZIONE"));
                  elencoMisure.add(v);
                  idLivelloLast = idLivello;
                }

                String codTipo = rs.getString("COD_TIPO");
                v.setCodiceTipoLivello(
                    v.getCodiceTipoLivello().concat(codTipo));

              }
              if (elencoMisure.isEmpty())
                return null;

              return elencoMisure;
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

  public List<DecodificaDTO<Long>> getTipiMisure(int idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getTipiMisure]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY =   " SELECT DISTINCT                                                \n"
			    		 + "     T.ID_TIPO_LIVELLO   AS ID,                                 \n"
			    		 + "     T.CODICE,                                                  \n"
			    		 + "     T.DESCRIZIONE                                              \n"
			    		 + " FROM                                                           \n"
			    		 + "     IUF_D_TIPO_LIVELLO T,                                    \n"
			    		 + "     IUF_D_BANDO D                                            \n"
			    		 + " WHERE                                                          \n"
			    		 + "     T.ID_TIPO_LIVELLO = D.ID_TIPO_LIVELLO                      \n"
			    		 + "     AND D.ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO", idProcedimentoAgricolo, Types.NUMERIC);

    try
    {
      return queryForDecodificaLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public List<LivelloDTO> getOperazioniMisureByTipo(
      Vector<Long> idMisureSelezionate, String codiceMisura)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getOperazioniMisureByTipo";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = "WITH  														\n"
          + "	LIVELLI  													\n"
          + "	AS(  														\n"
          + "	 SELECT 													\n"

          + "  	L.ID_LIVELLO,L.ORDINAMENTO, 							\n"
          + "	 	L.CODICE, 												\n"
          + "		L.DESCRIZIONE,											\n"
          + "		L.CODICE_LIVELLO										\n"
          + " FROM														\n"
          + "  IUF_D_LIVELLO L, IUF_R_LIVELLO_PADRE P						\n"
          + " WHERE														\n"
          + "	 L.ID_LIVELLO = P.ID_LIVELLO AND							\n"
          + "  L.ID_TIPOLOGIA_LIVELLO = 3 AND								\n"
          + "	 P.ID_LIVELLO_PADRE IN (									\n"
          + "		SELECT L2.ID_LIVELLO									\n"
          + "		FROM IUF_R_LIVELLO_PADRE LP,							\n"
          + "		IUF_D_LIVELLO L2										\n"
          + "		WHERE LP.ID_LIVELLO = L2.ID_LIVELLO						\n"
          + "		AND L2.ID_TIPOLOGIA_LIVELLO = 2							\n"
          + getInCondition("LP.ID_LIVELLO_PADRE", idMisureSelezionate)
          + "	) ORDER BY L.ORDINAMENTO									\n"
          + ")															\n"
          + " SELECT DISTINCT  											\n"
          + "		L.ID_LIVELLO, 											\n"
          + "		L.ORDINAMENTO, 											\n"
          + "		L.CODICE,  												\n"
          + "		L.DESCRIZIONE,  										\n"
          + "		L.CODICE_LIVELLO 										\n"
          + " FROM  							 							\n"
          + "		LIVELLI L, 												\n"
          + "		IUF_R_LIVELLO_PADRE A, 									\n"
          + "		IUF_R_LIVELLO_PADRE B, 									\n"
          + "		IUF_D_LIVELLO C, 			 							\n"
          + "		IUF_R_LIVELLO_BANDO D, 									\n"
          + "		IUF_D_BANDO E, 											\n"
          + "		IUF_D_TIPO_LIVELLO F			 						\n"
          + " WHERE  						 								\n"
          + "		D.ID_LIVELLO = B.ID_LIVELLO 							\n"
          + " 	AND E.ID_BANDO = D.ID_BANDO 							\n"
          + " 	AND E.FLAG_MASTER='S' 									\n"
          + " 	AND F.ID_TIPO_LIVELLO = E.ID_TIPO_LIVELLO				\n"
          + " 	AND B.ID_LIVELLO_PADRE=A.ID_LIVELLO						\n"
          + " 	AND A.ID_LIVELLO_PADRE=C.ID_LIVELLO						\n"
          + " 	AND L.ID_LIVELLO = B.ID_LIVELLO							\n";
      if (codiceMisura != null)
        SELECT += " 	AND F.CODICE = :COD									\n";
      SELECT += " ORDER BY L.ORDINAMENTO									\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("COD", codiceMisura, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> elencoMisure = new LinkedList<LivelloDTO>();
              LivelloDTO v = null;
              while (rs.next())
              {
                v = new LivelloDTO();
                v.setDescrizione(rs.getString("DESCRIZIONE"));
                v.setIdLivello(rs.getLong("ID_LIVELLO"));
                v.setCodice(rs.getString("CODICE"));
                elencoMisure.add(v);
              }
              if (elencoMisure.isEmpty())
                return null;

              return elencoMisure;
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

  public List<GravitaNotificaVO> getElencoGravitaNotifica()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoGravitaNotifica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "  SELECT  		                                                   \n"
        + "   ID_GRAVITA_NOTIFICA AS ID,                                     \n"
        + "   GRAVITA,                                                       \n"
        + "   DESCRIZIONE	                                                   \n"
        + " FROM                                                             \n"
        + "   IUF_D_GRAVITA_NOTIFICA                                         \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return queryForList(QUERY, mapParameterSource, GravitaNotificaVO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public DecodificaDTO<String> findProtocolloPratica(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::findProtocolloPratica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                       			   \n"
        + "   NVL(A.NUMERO_PROTOCOLLO, A.NUMERO_PROTOCOLLO_EMERGENZA) AS NUM_PROTOCOLLO, \n"
        + "   NVL(A.DATA_PROTOCOLLO, A.DATA_PROTOCOLLO_EMERGENZA) AS DATA_PROTOCOLLO     \n"
        + " FROM                                                                         \n"
        + "   IUF_T_PROCEDIM_OGGETTO_STAMP A,                                           \n"
        + "   IUF_R_OGGETTO_ICONA B                                                      \n"
        + " WHERE                                                                        \n"
        + "   A.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                       \n"
        + "   AND A.ID_OGGETTO_ICONA = B.ID_OGGETTO_ICONA                                \n"
        + "   AND B.EXT_ID_TIPO_DOCUMENTO = 2002                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DecodificaDTO<String>>()
          {
            @Override
            public DecodificaDTO<String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                DecodificaDTO<String> d = new DecodificaDTO<String>();
                d.setCodice(rs.getString("NUM_PROTOCOLLO"));
                d.setDescrizione(IuffiUtils.DATE
                    .formatDate(rs.getDate("DATA_PROTOCOLLO")));
                return d;
              }
              return new DecodificaDTO<String>();
            }
          });
    }
    catch (Throwable t)
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

  public DecodificaDTO<String> findProtocolloPratica(long idProcedimentoOggetto,
      long idCategoria) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::findProtocolloPratica]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                       			   \n"
        + "   NVL(A.NUMERO_PROTOCOLLO, A.NUMERO_PROTOCOLLO_EMERGENZA) AS NUM_PROTOCOLLO, \n"
        + "   NVL(A.DATA_PROTOCOLLO, A.DATA_PROTOCOLLO_EMERGENZA) AS DATA_PROTOCOLLO     \n"
        + " FROM                                                                         \n"
        + "   IUF_T_PROCEDIM_OGGETTO_STAMP A,                                           \n"
        + "   IUF_R_OGGETTO_ICONA B ,                                                     \n"
        + "   DOQUIAGRI_V_TIPO_DOC C                                                      \n"
        + " WHERE                                                                        \n"
        + "   A.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                       \n"
        + "   AND A.ID_OGGETTO_ICONA = B.ID_OGGETTO_ICONA                                \n"
        + "   AND B.EXT_ID_TIPO_DOCUMENTO = C.ID_TIPO_DOCUMENTO                                 \n"
        + "   AND A.DATA_FINE IS NULL  \n"
        + "   AND C.ID_CATEGORIA_DOC = :ID_CATEGORIA                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_CATEGORIA", idCategoria, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DecodificaDTO<String>>()
          {
            @Override
            public DecodificaDTO<String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                DecodificaDTO<String> d = new DecodificaDTO<String>();
                d.setCodice(rs.getString("NUM_PROTOCOLLO"));
                d.setDescrizione(IuffiUtils.DATE
                    .formatDate(rs.getDate("DATA_PROTOCOLLO")));
                return d;
              }
              return new DecodificaDTO<String>();
            }
          });
    }
    catch (Throwable t)
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

  public List<ProcedimentoDTO> getElencoProcedimentiRegistroFatture(
      long[] idsTipologiaDocumento, long[] idsModalitaPagamento,
      Long idFornitore,
      Date dataDa, Date dataA) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoProcedimentiRegistroFatture]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = "  SELECT  		                                                   								  \n"
        + "   P.ID_PROCEDIMENTO,		                                       								  \n"
        + "   L.ID_LIVELLO,		                                       								  	  \n"
        + "   L.CODICE,			                                       								  	  \n"
        + "   L.CODICE_LIVELLO,	                                      								  	  \n"
        + "   P.IDENTIFICATIVO,                                              								  \n"
        + "   B.DENOMINAZIONE AS DENOMINAZIONE_BANDO,						   								  \n"
        + "	B.ANNO_CAMPAGNA,                                               								  \n"
        + "   C.ID_DOCUMENTO_SPESA,                                                                         \n"
        + "   C.ID_DETT_DOCUMENTO_SPESA,                                                                    \n"
        + "   C.ID_FORNITORE,                                                                               \n"
        + "   C.ID_TIPO_DOCUMENTO_SPESA,                                                                    \n"
        + "   C.ID_MODALITA_PAGAMENTO,                                                                      \n"
        + "   C.DATA_DOCUMENTO_SPESA,                                                                       \n"
        + "   C.NUMERO_DOCUMENTO_SPESA,                                                                     \n"
        + "   C.DATA_PAGAMENTO,                                                                             \n"
        + "   C.IMPORTO_SPESA,                                                                              \n"
        + "   C.NOTE,                                                                                       \n"
        + "   C.NOME_FILE_LOGICO_DOCUMENTO_SPE,                                                             \n"
        + "   C.NOME_FILE_FISICO_DOCUMENTO_SPE,                                                             \n"
        + "   D.DESCRIZIONE AS DESCR_MOD_PAGAMENTO,                                                         \n"
        + "   E.DESCRIZIONE AS DESCR_TIPO_DOCUMENTO,                                                        \n"
        + "   F.CODICE_FORNITORE,                                                                           \n"
        + "   F.RAGIONE_SOCIALE,                                                                            \n"
        + "   F.INDIRIZZO_SEDE_LEGALE,                                                                      \n"
        + "   DSI.IMPORTO AS IMPORTO_RENDICONTATO,                                                          \n"
        + "   VA.CUAA AS CUAA_BENEFICIARIO,                         										  \n"
        + "   VA.INDIRIZZO_SEDE_LEGALE,                            										  \n"
        + "   VA.DENOMINAZIONE AS DENOMINAZIONE_AZIENDA,           										  \n"
        + "   VAM.DESCRIZIONE AS DESCR_AMM_COMPETENZA,             										  \n"
        + "   VAM.DENOMINAZIONE_1,             					 										  \n"
        + "   VD.DENOMINAZIONE AS DENOMINAZIONE_DELEGA,            										  \n"
        + "   DETTINT.PROGRESSIVO,					            										  \n"
        + "   OGG.DESCRIZIONE AS DESCR_OGGETTO	           												  \n"
        + " FROM                                                             	                              \n"
        + "   IUF_T_PROCEDIMENTO P, 	                                       	                              \n"
        + "	IUF_D_BANDO B,	                                       		   	                              \n"
        + "	IUF_D_LIVELLO L,                               				   	                              \n"
        + "	IUF_R_PROCEDIMENTO_LIVELLO PL,                            	   	                              \n"
        + "   IUF_T_DOCUMENTO_SPESA A,                                                                      \n"
        + "   IUF_T_DETT_DOCUMENTO_SPESA C,                                                                 \n"
        + "   IUF_D_MODALITA_PAGAMENTO D,                                                                   \n"
        + "   IUF_D_TIPO_DOCUMENTO_SPESA E,                                                                 \n"
        + "   IUF_R_DOCUMENTO_SPESA_INTERV DSI,                                                           \n"
        + "   IUF_T_FORNITORE F,                                                                            \n"
        + "   IUF_T_PROCEDIMENTO_AZIENDA PA,                                                                \n"
        + "   SMRCOMUNE_V_AMM_COMPETENZA VAM,                         									  \n"
        + "   SMRGAA_V_DATI_ANAGRAFICI VA,                            									  \n"
        + "   IUF_T_PROCEDIM_AMMINISTRAZIO AM,                            								  \n"
        + "   IUF_R_DOC_SPESA_INT_PROC_OGG DSIPO,			                            					  \n"
        + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,			                            					  \n"
        + "   IUF_D_OGGETTO OGG,							                            					  \n"
        + "   IUF_T_PROCEDIMENTO_OGGETTO PO,							                            		  \n"
        + "   SMRGAA_V_DATI_DELEGA VD,                                 									  \n"
        + "   IUF_T_DETTAGLIO_INTERVENTO DETTINT,                         								  \n"
        + "   IUF_R_PROCEDIMENTO_LIVELLO PLIV	                        		                              \n"
        + " WHERE	                                       					   	                              \n"
        + "	P.ID_BANDO = B.ID_BANDO	                                       	                              \n"
        + "	AND PL.ID_LIVELLO = L.ID_LIVELLO                               	                              \n"
        + "	AND PL.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO					   	                              \n"
        + "   AND PA.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO              		                              \n"
        + "   AND PA.DATA_FINE IS NULL                                		                              \n"
        + "   AND AM.ID_PROCEDIMENTO(+) = P.ID_PROCEDIMENTO           		                              \n"
        + "   AND AM.DATA_FINE(+) IS NULL                             		                              \n"
        + "   AND VA.ID_AZIENDA = PA.EXT_ID_AZIENDA                   		                              \n"
        + "   AND VA.DATA_FINE_VALIDITA IS NULL                       		                              \n"
        + "   AND B.ID_BANDO = P.ID_BANDO                             		                              \n"
        + "   AND A.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                                                     \n"
        + "   AND A.ID_DOCUMENTO_SPESA = C.ID_DOCUMENTO_SPESA                                               \n"
        + "   AND D.ID_MODALITA_PAGAMENTO(+) = C.ID_MODALITA_PAGAMENTO                                      \n"
        + "   AND E.ID_TIPO_DOCUMENTO_SPESA = C.ID_TIPO_DOCUMENTO_SPESA                                     \n"
        + "   AND F.ID_FORNITORE(+) = C.ID_FORNITORE                                                        \n"
        + "   AND C.ID_DOCUMENTO_SPESA = DSI.ID_DOCUMENTO_SPESA (+)                                         \n"
        + "   AND DSIPO.ID_DOCUMENTO_SPESA_INTERVEN (+) = DSI.ID_DOCUMENTO_SPESA_INTERVEN                   \n"
        + "   AND DSIPO.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO (+)                 		  	  \n"
        + "   AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO (+)                 		  	  \n"
        + "   AND LGO.ID_OGGETTO = OGG.ID_OGGETTO (+)                 		  							  \n"
        + "   AND VAM.ID_AMM_COMPETENZA(+) = AM.EXT_ID_AMM_COMPETENZA 									  \n"
        + "   AND DETTINT.ID_INTERVENTO(+) = DSI.ID_INTERVENTO		 									  \n"
        + "   AND DETTINT.DATA_FINE IS NULL							 									  \n"
        + "   AND VD.ID_AZIENDA(+) = PA.EXT_ID_AZIENDA               										  \n"
        + "   AND PLIV.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO			 		                              \n"
        + "   AND PLIV.ID_LIVELLO = L.ID_LIVELLO 		     		 			                              \n";
    if (idsTipologiaDocumento != null && idsTipologiaDocumento.length > 0)
      QUERY += getInCondition("C.ID_TIPO_DOCUMENTO_SPESA",
          idsTipologiaDocumento);
    if (idsModalitaPagamento != null && idsModalitaPagamento.length > 0)
      QUERY += getInCondition("D.ID_MODALITA_PAGAMENTO", idsModalitaPagamento);

    if (idFornitore != null)
      QUERY += "   AND F.ID_FORNITORE = :ID_FORNITORE                                                 \n";
    if (dataA != null)
      QUERY += "   AND TRUNC(C.DATA_DOCUMENTO_SPESA) <= TRUNC(:DATA_A)                                 \n";
    if (dataDa != null)
      QUERY += "   AND TRUNC(C.DATA_DOCUMENTO_SPESA) >= TRUNC(:DATA_DA)                                \n";
    QUERY += "  ORDER BY P.ID_PROCEDIMENTO, L.ID_LIVELLO, DETTINT.PROGRESSIVO					          \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_FORNITORE", idFornitore, Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_DOCUMENTO_SPESA",
          idsTipologiaDocumento, Types.NUMERIC);
      mapParameterSource.addValue("ID_MODALITA_PAGAMENTO", idsModalitaPagamento,
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_A", dataA, Types.DATE);
      mapParameterSource.addValue("DATA_DA", dataDa, Types.DATE);

      return namedParameterJdbcTemplate.query(QUERY.toString(),
          mapParameterSource, new ResultSetExtractor<List<ProcedimentoDTO>>()
          {
            @Override
            public List<ProcedimentoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<ProcedimentoDTO> list = new ArrayList<ProcedimentoDTO>();
              List<DocumentoSpesaVO> docs = null;
              List<LivelloDTO> livelli = null;

              ProcedimentoDTO dto = null;
              long idProcedimentoLast = -1;
              long idProcedimento;
              DocumentoSpesaVO doc = null;
              LivelloDTO liv = null;
              while (rs.next())
              {
                idProcedimento = rs.getLong("ID_PROCEDIMENTO");
                if (idProcedimento != idProcedimentoLast)
                {
                  idProcedimentoLast = idProcedimento;
                  dto = new ProcedimentoDTO();
                  dto.setIdProcedimento(idProcedimento);
                  dto.setIdentificativo(rs.getString("IDENTIFICATIVO"));
                  dto.setDenominazioneBando(
                      rs.getString("DENOMINAZIONE_BANDO"));
                  dto.setAnnoCampagna(rs.getLong("ANNO_CAMPAGNA"));
                  dto.setDenominazioneBeneficiario(
                      rs.getString("DENOMINAZIONE_AZIENDA"));
                  dto.setCuaaBeneficiario(rs.getString("CUAA_BENEFICIARIO"));
                  dto.setAmmCompetenza(rs.getString("DESCR_AMM_COMPETENZA"));
                  dto.setDenominazioneDelega(
                      rs.getString("DENOMINAZIONE_DELEGA"));
                  docs = new ArrayList<DocumentoSpesaVO>();
                  doc = new DocumentoSpesaVO();
                  dto.setDocumentiSpesa(docs);
                  livelli = new ArrayList<LivelloDTO>();
                  dto.setLivelli(livelli);
                  list.add(dto);
                }
                liv = new LivelloDTO();
                liv.setIdLivello(rs.getLong("ID_LIVELLO"));
                liv.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                liv.setCodice(rs.getString("CODICE"));

                if (!livelloGiaPresente(livelli, liv))
                  livelli.add(liv);

                doc = new DocumentoSpesaVO();
                doc.setTipoDomanda(rs.getString("DESCR_OGGETTO"));
                doc.setIdFornitore(rs.getLong("ID_FORNITORE"));
                doc.setCodiceFornitore(rs.getString("CODICE_FORNITORE"));
                doc.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
                doc.setIdDocumentoSpesa(rs.getLong("ID_DOCUMENTO_SPESA"));
                doc.setIdDettDocumentoSpesa(
                    rs.getLong("ID_DETT_DOCUMENTO_SPESA"));
                doc.setImportoRendicontato(
                    rs.getBigDecimal("IMPORTO_RENDICONTATO"));
                doc.setImportoSpesa(rs.getBigDecimal("IMPORTO_SPESA"));
                doc.setIdTipoDocumentoSpesa(
                    rs.getLong("ID_TIPO_DOCUMENTO_SPESA"));
                doc.setDescrTipoDocumento(rs.getString("DESCR_TIPO_DOCUMENTO"));
                doc.setDescrModPagamento(rs.getString("DESCR_MOD_PAGAMENTO"));
                doc.setDataPagamento(rs.getDate("DATA_PAGAMENTO"));
                doc.setDataDocumentoSpesa(rs.getDate("DATA_DOCUMENTO_SPESA"));
                doc.setNumeroDocumentoSpesa(
                    rs.getString("NUMERO_DOCUMENTO_SPESA"));
                doc.setProgressivo(rs.getInt("PROGRESSIVO"));
                docs.add(doc);

              }
              return list.isEmpty() ? new ArrayList<>() : list;
            }

            private boolean livelloGiaPresente(List<LivelloDTO> livelli,
                LivelloDTO liv)
            {

              if (livelli != null && liv != null)
                for (LivelloDTO l : livelli)
                {
                  if (l.getCodice().equals(liv.getCodice()))
                    return true;
                }
              return false;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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
  
  //TODO: FIXME: rimuovere, probabilmente in disuso
  public List<GruppoOggettoDTO> getStatiAmmProcedimentiAttivi(
      Vector<Long> lIdLivelli, Vector<Long> lIdBando,
      Vector<Long> lIdAmministrazioni,
      Vector<Long> lIdStatiProc) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getStatiAmmProcedimentiAttivi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final StringBuffer QUERY = new StringBuffer(
        " SELECT DISTINCT                                                         	\n"
            + "   A2.ID_STATO_OGGETTO,                                                  	\n"
            + "   A2.DESCRIZIONE AS DESCR_STATO_OGGETTO,                                	\n"
            + "   A2.ID_STATO_OGGETTO,					                              	\n"
            + "   GO.DESCRIZIONE AS DESCR_GRUPPO_OGGETTO,                               	\n"
            + "   GO.ID_GRUPPO_OGGETTO			                                      	\n"
            + " FROM                                                                    	\n"
            + "   IUF_D_STATO_OGGETTO A2,                                               	\n"
            + "   IUF_T_PROCEDIMENTO B,                                                 	\n"
            + "   IUF_T_PROCEDIMENTO_OGGETTO C,                                         	\n"
            + "   IUF_D_BANDO DB,                                                       	\n"
            + "   IUF_R_LIVELLO_BANDO LB,                                               	\n"
            + "   IUF_T_ITER_PROCEDIMENTO_GRUP IPG,                                   	\n"
            + "   IUF_T_PROCEDIM_AMMINISTRAZIO PA,                                    	\n"
            + "   IUF_R_LEGAME_GRUPPO_OGGETTO LGO,                                      	\n"
            + "   IUF_D_GRUPPO_OGGETTO GO			                                      	\n"
            + " WHERE                                                                   	\n"
            + "   IPG.ID_STATO_OGGETTO = A2.ID_STATO_OGGETTO                            	\n"
            + "   AND IPG.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO                           	\n"
            + "   AND C.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO     	                      	\n"
            + "   AND C.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO     		\n"
            + "   AND LGO.ID_GRUPPO_OGGETTO = GO.ID_GRUPPO_OGGETTO     					\n"
            + "   AND IPG.DATA_FINE IS NULL					    						\n"
            + "   AND IPG.CODICE_RAGGRUPPAMENTO = C.CODICE_RAGGRUPPAMENTO					\n"
            + "   AND DB.ID_BANDO = B.ID_BANDO                                          	\n"
            + "   AND LB.ID_BANDO = B.ID_BANDO                                          	\n"
            + "   AND PA.ID_PROCEDIMENTO = B.ID_PROCEDIMENTO                            	\n"
            + "   AND PA.DATA_FINE IS NULL				                              	\n");
    if (lIdLivelli != null && lIdLivelli.size() > 0)
      QUERY.append(" " + getInCondition("LB.ID_LIVELLO", lIdLivelli) + " ");
    if (lIdBando != null && lIdBando.size() > 0)
      QUERY.append(" " + getInCondition("B.ID_BANDO", lIdBando) + " ");
    if (lIdAmministrazioni != null && lIdAmministrazioni.size() > 0)
      QUERY.append(
          " " + getInCondition("PA.EXT_ID_AMM_COMPETENZA", lIdAmministrazioni)
              + " ");
    if (lIdStatiProc != null && lIdStatiProc.size() > 0)
      QUERY.append(
          " " + getInCondition("B.ID_STATO_OGGETTO", lIdStatiProc) + " ");

    QUERY.append(" ORDER BY GO.ID_GRUPPO_OGGETTO ");

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY.toString(),
          mapParameterSource, new ResultSetExtractor<List<GruppoOggettoDTO>>()
          {
            @Override
            public List<GruppoOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GruppoOggettoDTO> list = new ArrayList<GruppoOggettoDTO>();
              ArrayList<OggettoDTO> oggettiList = null;
              GruppoOggettoDTO gruppoOggettoDTO = null;
              long lastIdGruppoOggetto = 0;
              long idGruppoOggetto = 0;

              while (rs.next())
              {
                idGruppoOggetto = rs.getLong("ID_GRUPPO_OGGETTO");

                if ((idGruppoOggetto != lastIdGruppoOggetto))
                {
                  lastIdGruppoOggetto = idGruppoOggetto;
                  gruppoOggettoDTO = new GruppoOggettoDTO();
                  gruppoOggettoDTO.setIdGruppoOggetto(idGruppoOggetto);
                  gruppoOggettoDTO
                      .setDescrizione(rs.getString("DESCR_GRUPPO_OGGETTO"));
                  oggettiList = new ArrayList<OggettoDTO>();
                  OggettoDTO o = new OggettoDTO();
                  o.setIdStato(0l);
                  o.setCodice("0");
                  o.setStato("Non Presente");
                  oggettiList.add(o);
                  gruppoOggettoDTO.setOggetti(oggettiList);
                  list.add(gruppoOggettoDTO);
                }
                if (rs.getLong("ID_STATO_OGGETTO") != 0)
                {
                  OggettoDTO oggettoDTO = new OggettoDTO();
                  oggettoDTO.setStato(rs.getString("DESCR_STATO_OGGETTO"));
                  oggettoDTO.setIdStato(rs.getLong("ID_STATO_OGGETTO"));
                  oggettiList.add(oggettoDTO);
                }
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
          },
          new LogVariable[]
          {}, QUERY.toString(), mapParameterSource);
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

  public List<IterProcedimentoGruppoDTO> getIterGruppoOggetto(
      long idProcedimento, long codRaggruppamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getIterGruppoOggetto";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                                                        \n"
          + "   A.ID_ITER_PROCEDIMENTO_GRUPPO,                                                                                              \n"
          + "   A.ID_STATO_OGGETTO,                                                                                                         \n"
          + "   B.DESCRIZIONE,                                                                                                              \n"
          + "   A.DATA_INIZIO,                                                                                                              \n"
          + "   A.DATA_FINE,                                                                                                                \n"
          + "   A.NOTE,                                                                                                                     \n"
          + "   A.EXT_ID_UTENTE_AGGIORNAMENTO,                                                                                              \n"
          + "   DECODE(UL.ID_UTENTE_LOGIN, NULL, NULL, UL.COGNOME_UTENTE_LOGIN || ' ' || UL.NOME_UTENTE_LOGIN || '(' || UL.DENOMINAZIONE || ')') AS DESC_UTENTE_AGGIORNAMENTO \n"
          + " FROM                                                                                                                          \n"
          + "   IUF_T_ITER_PROCEDIMENTO_GRUP A,                                                                                           \n"
          + "   IUF_D_STATO_OGGETTO B,                                                                                                      \n"
          + "   PAPUA_V_UTENTE_LOGIN UL                                                                                                     \n"
          + " WHERE                                                                                                                         \n"
          + "   A.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                                                                        \n"
          + "   AND A.CODICE_RAGGRUPPAMENTO = :CODICE_RAGGRUPPAMENTO                                                                        \n"
          + "   AND A.ID_STATO_OGGETTO = B.ID_STATO_OGGETTO                                                                                 \n"
          + "   AND A.EXT_ID_UTENTE_AGGIORNAMENTO = UL.ID_UTENTE_LOGIN                                                                      \n"
          + " ORDER BY A.DATA_INIZIO                                                                                                        \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RAGGRUPPAMENTO", codRaggruppamento,
          Types.NUMERIC);

      return queryForList(SELECT, mapParameterSource,
          IterProcedimentoGruppoDTO.class);
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

  public DecodificaDTO<String> findProtocolloOggettoIstanza(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::findProtocolloOggettoIstanza]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " WITH PROTO_PO AS																\n"
        + "(SELECT 																					\n"
        + "			NVL(A.DATA_PROTOCOLLO_EMERGENZA, A.DATA_PROTOCOLLO) AS DATA_PROTOCOLLO, 		\n"
        + "			NVL(A.NUMERO_PROTOCOLLO_EMERGENZA, A.NUMERO_PROTOCOLLO) AS NUM_PROTOCOLLO,		\n"
        + "			PO.ID_PROCEDIMENTO_OGGETTO														\n"
        + " 	FROM	                                                                         	\n"
        + "   		IUF_T_PROCEDIM_OGGETTO_STAMP A,												\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO PO,													\n"
        + "   		IUF_R_OGGETTO_ICONA B,                                                     		\n"
        + "   		DOQUIAGRI_V_TIPO_DOC C                                                     		\n"
        + " 	WHERE  		                                                                      	\n"
        + "   		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\n"
        + "   		AND A.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO						\n"
        + "   		AND A.DATA_FINE IS NULL															\n"
        + "   		AND A.ID_OGGETTO_ICONA = B.ID_OGGETTO_ICONA                                		\n"
        + "   		AND B.EXT_ID_TIPO_DOCUMENTO = C.ID_TIPO_DOCUMENTO                           	\n"
        + "   		AND C.ID_CATEGORIA_DOC = 2001 													\n"
        + "			ORDER BY NUM_PROTOCOLLO),														\n"
        + "PROTO_ITER AS																			\n"
        + "	(SELECT 																				\n"
        + "			B.DATA_INIZIO AS DATA_PROTOCOLLO, 												\n"
        + "			NULL AS NUM_PROTOCOLLO,															\n"
        + "			A.ID_PROCEDIMENTO_OGGETTO														\n"
        + " 		FROM                                                                         	\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO A,                                             		\n"
        + "   		IUF_T_ITER_PROCEDIMENTO_OGGE B                                           		\n"
        + " 		WHERE                                                                        	\n"
        + "   		A.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                      		\n"
        + "   		AND A.ID_PROCEDIMENTO_OGGETTO = B.ID_PROCEDIMENTO_OGGETTO                      	\n"
        + "   		AND ID_STATO_OGGETTO = 10) 							                      		\n"
        + "	SELECT 																					\n"
        + "		AA.NUM_PROTOCOLLO AS NUM_PROTOCOLLO,  												\n"
        + "		NVL(AA.DATA_PROTOCOLLO, BB.DATA_PROTOCOLLO) AS DATA_PROTOCOLLO	 					\n"
        + "	FROM PROTO_PO AA, PROTO_ITER BB															\n"
        + " WHERE AA.ID_PROCEDIMENTO_OGGETTO = BB.ID_PROCEDIMENTO_OGGETTO (+)						\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DecodificaDTO<String>>()
          {
            @Override
            public DecodificaDTO<String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                DecodificaDTO<String> d = new DecodificaDTO<String>();
                d.setCodice(rs.getString("NUM_PROTOCOLLO"));
                d.setDescrizione(IuffiUtils.DATE
                    .formatDate(rs.getTimestamp("DATA_PROTOCOLLO")));
                return d;
              }
              return new DecodificaDTO<String>();
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public String findDataProtocollNonIstanzaConStampe(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::findDataProtocollNonIstanzaConStampe]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " 	SELECT 																				\n"
        + "			NVL(A.DATA_PROTOCOLLO_EMERGENZA, A.DATA_PROTOCOLLO) AS DATA_PROTOCOLLO, 		\n"
        + "			PO.ID_PROCEDIMENTO_OGGETTO														\n"
        + " 	FROM	                                                                         	\n"
        + "   		IUF_T_PROCEDIM_OGGETTO_STAMP A,												\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO PO,													\n"
        + "   		IUF_R_OGGETTO_ICONA B,                                                     		\n"
        + "   		DOQUIAGRI_V_TIPO_DOC C                                                     		\n"
        + " 	WHERE	                                                                        	\n"
        + "   		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\n"
        + "   		AND A.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO						\n"
        + "   		AND A.DATA_FINE IS NULL															\n"
        + "   		AND A.ID_OGGETTO_ICONA = B.ID_OGGETTO_ICONA                                		\n"
        + "   		AND B.EXT_ID_TIPO_DOCUMENTO = C.ID_TIPO_DOCUMENTO                           	\n"
        + "   		AND C.ID_CATEGORIA_DOC = 2005 													\n"
        + "			AND B.ID_ELENCO_CDU = 52														\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return IuffiUtils.DATE
                    .formatDate(rs.getTimestamp("DATA_PROTOCOLLO"));
              }
              return new String();
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public String findDataProtocolloNonIstanzaSenzaStampe(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::findProtocolloOggettoNonIstanzaSenzaStampe]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "		SELECT 																				\n"
        + "			B.DATA_INIZIO AS DATA_PROTOCOLLO, 												\n"
        + "			NULL AS NUM_PROTOCOLLO,															\n"
        + "			A.ID_PROCEDIMENTO_OGGETTO														\n"
        + " 	FROM                                                                         		\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO A,                                             		\n"
        + "   		IUF_T_ITER_PROCEDIMENTO_OGGE B                                           		\n"
        + " 	WHERE                                                                        		\n"
        + "   		A.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                      		\n"
        + "   		AND A.ID_PROCEDIMENTO_OGGETTO = B.ID_PROCEDIMENTO_OGGETTO                      	\n"
        + "   		AND ID_STATO_OGGETTO = 30 							                      		\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return IuffiUtils.DATE
                    .formatDate(rs.getTimestamp("DATA_PROTOCOLLO"));
              }
              return new String();
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<DecodificaDTO<String>> getElencoDescrizioniGruppi()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoDescrizioniGruppi]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT DISTINCT                   \n"
        + "   DESCRIZIONE AS ID,              \n"
        + "   DESCRIZIONE AS CODICE,          \n"
        + "   DESCRIZIONE AS DESCRIZIONE      \n"
        + " FROM                              \n"
        + "   IUF_D_GRUPPO_OGGETTO			\n"
        + " ORDER BY DESCRIZIONE             	\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public List<DecodificaDTO<String>> getElencoDescrizioneStatiOggetti()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getElencoDescrizioneStatiOggetti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT DISTINCT                   \n"
        + "   DESCRIZIONE AS ID,              \n"
        + "   DESCRIZIONE AS CODICE,          \n"
        + "   DESCRIZIONE AS DESCRIZIONE      \n"
        + " FROM                              \n"
        + "   IUF_D_STATO_OGGETTO 			\n"
        + " ORDER BY DESCRIZIONE             	\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  private boolean POHasStampe(long idProcedimentoOggetto)
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::POHasStampe]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = "		SELECT 																				\n"
        + "			COUNT(A.ID_PROCEDIM_OGGETTO_STAMPA)												\n"
        + " 	FROM                                                                         		\n"
        + "   		IUF_T_PROCEDIM_OGGETTO_STAMP A,												\n"
        + "   		IUF_T_PROCEDIMENTO_OGGETTO PO,													\n"
        + "   		IUF_R_OGGETTO_ICONA B,                                                     		\n"
        + "   		DOQUIAGRI_V_TIPO_DOC C                                                     		\n"
        + " 	WHERE                                                                        		\n"
        + "   		PO.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO							\n"
        + "   		AND A.ID_PROCEDIMENTO_OGGETTO  = PO.ID_PROCEDIMENTO_OGGETTO						\n"
        + "   		AND A.DATA_FINE IS NULL															\n"
        + "   		AND A.ID_OGGETTO_ICONA = B.ID_OGGETTO_ICONA                                		\n"
        + "   		AND B.EXT_ID_TIPO_DOCUMENTO = C.ID_TIPO_DOCUMENTO                           	\n"
        + "   		AND C.ID_CATEGORIA_DOC = 2005 													\n"
        + "			AND B.ID_ELENCO_CDU = 52														\n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO",
          idProcedimentoOggetto, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource) > 0;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<ProcedimentoGruppoVO> getElencoProcedimentoGruppo(
      long idProcedimento, long codiceRaggruppamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoProcedimentoGruppo]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                                                                                                      \n"
        + "   A.ID_PROCEDIMENTO_GRUPPO,                                                                                                                                 \n"
        + "   A.ID_PROCEDIMENTO,                                                                                                                                        \n"
        + "   A.CODICE_RAGGRUPPAMENTO,                                                                                                                                  \n"
        + "   A.EXT_ID_UTENTE_AGGIORNAMENTO AS EXT_ID_UTENTE,                                                                                                           \n"
        + "   A.DATA_INIZIO,                                                                                                                                            \n"
        + "   A.DATA_FINE,                                                                                                                                              \n"
        + "   A.FLAG_GRUPPO_CHIUSO,                                                                                                                                     \n"
        + "   DECODE(A.FLAG_GRUPPO_CHIUSO, 'N', 'Sbloccato', 'Bloccato') AS FLAG_GRUPPO_CHIUSO_DECOD,                                                                   \n"
        + "   DECODE(UL.ID_UTENTE_LOGIN, NULL, NULL, UL.COGNOME_UTENTE_LOGIN || ' ' || UL.NOME_UTENTE_LOGIN || '(' || UL.DENOMINAZIONE || ')') AS UTENTE_AGGIORNAMENTO, \n"
        + "   A.MOTIVAZIONI                                                                                                                                             \n"
        + " FROM                                                                                                                                                        \n"
        + "   IUF_T_PROCEDIMENTO_GRUPPO A,                                                                                                                              \n"
        + "   PAPUA_V_UTENTE_LOGIN UL                                                                                                                                   \n"
        + " WHERE                                                                                                                                                       \n"
        + "   A.EXT_ID_UTENTE_AGGIORNAMENTO = UL.ID_UTENTE_LOGIN                                                                                                        \n"
        + "   AND A.ID_PROCEDIMENTO = :ID_PROCEDIMENTO                                                                                                                  \n"
        + "   AND A.CODICE_RAGGRUPPAMENTO = :CODICE_RAGGRUPPAMENTO                                                                                                      \n"
        + " ORDER BY A.DATA_INIZIO                                                                                                                                      \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
        Types.NUMERIC);
    mapParameterSource.addValue("CODICE_RAGGRUPPAMENTO", codiceRaggruppamento,
        Types.NUMERIC);

    try
    {
      return queryForList(QUERY, mapParameterSource,
          ProcedimentoGruppoVO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public void storicizzaBloccoGruppoOggetto(long idProcedimento,
      long codRaggruppamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::storicizzaBloccoGruppoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                                                          \n"
        + "   IUF_T_PROCEDIMENTO_GRUPPO                                   \n"
        + " SET                                                           \n"
        + "   DATA_FINE = SYSDATE  										  \n"
        + " WHERE                                                         \n"
        + "   ID_PROCEDIMENTO = :ID_PROCEDIMENTO          				  \n"
        + "   AND CODICE_RAGGRUPPAMENTO = :CODICE_RAGGRUPPAMENTO          \n"
        + "   AND DATA_FINE IS NULL ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RAGGRUPPAMENTO", codRaggruppamento,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimento", idProcedimento),
              new LogParameter("codRaggruppamento", codRaggruppamento)
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

  public boolean isGruppoOggettoBloccato(long idProcedimento,
      long codRaggruppamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::isGruppoOggettoBloccato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " SELECT COUNT(*) FROM                                          \n"
        + "   IUF_T_PROCEDIMENTO_GRUPPO                                   \n"
        + " WHERE                                                         \n"
        + "   ID_PROCEDIMENTO = :ID_PROCEDIMENTO          				        \n"
        + "   AND CODICE_RAGGRUPPAMENTO = :CODICE_RAGGRUPPAMENTO          \n"
        + "   AND DATA_FINE IS NULL "
        + "   AND FLAG_GRUPPO_CHIUSO = 'S' ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RAGGRUPPAMENTO", codRaggruppamento,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.queryForLong(UPDATE,
          mapParameterSource) > 0;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimento", idProcedimento),
              new LogParameter("codRaggruppamento", codRaggruppamento)
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

  public int inserisciSbloccoGruppoOggetto(long idProcedimento,
      long codRaggruppamento, String note, long idUtenteLogin)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::inserisciSbloccoGruppoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " INSERT                                      \n"
        + " INTO                                        \n"
        + "   IUF_T_PROCEDIMENTO_GRUPPO            \n"
        + "   (                                         \n"
        + "     ID_PROCEDIMENTO_GRUPPO,            \n"
        + "     ID_PROCEDIMENTO,                        \n"
        + "     CODICE_RAGGRUPPAMENTO,                  \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,                            \n"
        + "     DATA_INIZIO,             \n"
        + "     FLAG_GRUPPO_CHIUSO,             \n"
        + "     DATA_FINE,             \n"
        + "     MOTIVAZIONI             \n"
        + "   )                                         \n"
        + "   VALUES                                    \n"
        + "   (                                         \n"
        + "     SEQ_IUF_T_PROCEDIMENTO_GRUPP.NEXTVAL,            \n"
        + "     :ID_PROCEDIMENTO,                        \n"
        + "     :CODICE_RAGGRUPPAMENTO,                  \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO,                            \n"
        + "     SYSDATE,             \n"
        + "     'N',             \n"
        + "     NULL,             \n"
        + "     :MOTIVAZIONI             \n"
        + "   )                                         \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO", idProcedimento,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RAGGRUPPAMENTO", codRaggruppamento,
          Types.NUMERIC);
      mapParameterSource.addValue("MOTIVAZIONI", note, Types.VARCHAR);
      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idUtenteLogin", idUtenteLogin),
              new LogParameter("idProcedimento", idProcedimento),
              new LogParameter("codRaggruppamento", codRaggruppamento),
              new LogParameter("note", note)
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

  public List<Long> elencoIdProcedimentiEstrattiExPost(Long idEstrazione,
      String limit, String offset,
      HashMap<String, FiltroVO> mapFilters) throws InternalUnexpectedException
  {
    String THIS_METHOD = "elencoIdProcedimentiEstrattiExPost";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      if (limit != null && offset != null)
        SELECT.append(
            " SELECT *                                                           	\n"
                + "   FROM (                                                                      \n"
                + "      SELECT ROWNUM RN, TMP.*                                                  \n"
                + "         FROM (                                                                \n");

      SELECT.append(
          "  SELECT  DISTINCT                                          			\n"
              + "    P.ID_PROCEDIMENTO		                               				\n"
              + "  FROM                                                      			\n"
              + "    IUF_T_PROCEDIMENTO_ESTR_EXPO PE,                         		\n"
              + "    IUF_T_PROCEDIMENTO_OGGETTO P	                      	 			\n"
              + "  where PE.ID_ESTRAZIONE_CAMPIONE =:ID_ESTRAZIONE           			\n"
              + "	and PE.id_procedimento = P.ID_PROCEDIMENTO					      	\n");
      if (mapFilters != null && !mapFilters.isEmpty())
        if (mapFilters.containsKey("flagEstrattaStr"))
        {
          String s = "  AND PE.FLAG_ESTRATTA IN                                    	\n";
          final List<Long> values = mapFilters.get("flagEstrattaStr")
              .getValues();
          if (values != null)
          {
            s += "(";
            for (long id : values)
            {
              if (ProcedimentoOggettoVO.getFlagEstrattaFromId(id) != null)
                s += ProcedimentoOggettoVO.getFlagEstrattaFromId(id) + ",";
            }

            s = s.toString().substring(0, s.length() - 1); // RIMUOVO ULTIMA
                                                           // VIRGOLA
            SELECT.append(s + " ) ");
          }
        }

      if (limit != null && offset != null)
        SELECT.append(
            "           ) tmp                                                  	\n"
                + "           WHERE ROWNUM <= :END_FETCH                                  \n"
                + "        )                                                              \n"
                + "  WHERE RN > :START_FETCH                                              \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ESTRAZIONE", idEstrazione, Types.NUMERIC);

      if (limit != null && offset != null)
      {
        int offsetInt = Integer.parseInt(offset);
        int limitInt = Integer.parseInt(limit);
        int x = offsetInt + limitInt;
        parameterSource.addValue("START_FETCH", offsetInt, Types.NUMERIC);
        parameterSource.addValue("END_FETCH", x, Types.NUMERIC);
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
                list.add(rs.getLong("ID_PROCEDIMENTO"));
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

  public List<Long> elencoIdProcedimentiOggettoEstrattiExPost(Long idEstrazione,
      String limit, String offset,
      HashMap<String, FiltroVO> mapFilters) throws InternalUnexpectedException
  {
    String THIS_METHOD = "elencoIdProcedimentiOggettoEstrattiExPost";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      if (limit != null && offset != null)
        SELECT.append(
            " SELECT *                                                           	\n"
                + "   FROM (                                                                      \n"
                + "      SELECT ROWNUM RN, TMP.*                                                  \n"
                + "         FROM (                                                                \n");

      SELECT.append(
          "  SELECT  DISTINCT                                          			\n"
              + "    P.ID_PROCEDIMENTO_OGGETTO                             				\n"
              + "  FROM                                                      			\n"
              + "    IUF_T_PROCEDIMENTO_ESTR_EXPO PE,                         		\n"
              + "    IUF_T_PROCEDIMENTO_OGGETTO P	                      	 			\n"
              + "  where PE.ID_ESTRAZIONE_CAMPIONE =:ID_ESTRAZIONE           			\n"
              + "	and PE.id_procedimento = P.ID_PROCEDIMENTO			\n");
      if (mapFilters != null && !mapFilters.isEmpty())
        if (mapFilters.containsKey("flagEstrattaStr"))
        {
          String s = "  AND PE.FLAG_ESTRATTA IN                                    	\n";
          final List<Long> values = mapFilters.get("flagEstrattaStr")
              .getValues();
          if (values != null)
          {
            s += "(";
            for (long id : values)
            {
              if (ProcedimentoOggettoVO.getFlagEstrattaFromId(id) != null)
                s += ProcedimentoOggettoVO.getFlagEstrattaFromId(id) + ",";
            }

            s = s.toString().substring(0, s.length() - 1); // RIMUOVO ULTIMA
                                                           // VIRGOLA
            SELECT.append(s + " ) ");
          }
        }

      if (limit != null && offset != null)
        SELECT.append(
            "           ) tmp                                                  	\n"
                + "           WHERE ROWNUM <= :END_FETCH                                  \n"
                + "        )                                                              \n"
                + "  WHERE RN > :START_FETCH                                              \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ESTRAZIONE", idEstrazione, Types.NUMERIC);

      if (limit != null && offset != null)
      {
        int offsetInt = Integer.parseInt(offset);
        int limitInt = Integer.parseInt(limit);
        int x = offsetInt + limitInt;
        parameterSource.addValue("START_FETCH", offsetInt, Types.NUMERIC);
        parameterSource.addValue("END_FETCH", x, Types.NUMERIC);
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
                list.add(rs.getLong("ID_PROCEDIMENTO_OGGETTO"));
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

  public DecodificaDTO<String> findProtocolloPraticaByIdPOAndCodice(long idProcedimentoOggetto, long idTipoDoc) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::findProtocolloPraticaByIdPOAndCodice]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = 
    		" SELECT                                                                       		    \n"
            + "   NVL(A.NUMERO_PROTOCOLLO, A.NUMERO_PROTOCOLLO_EMERGENZA) AS NUM_PROTOCOLLO, 		\n"
            + "   NVL(A.DATA_PROTOCOLLO, A.DATA_PROTOCOLLO_EMERGENZA) AS DATA_PROTOCOLLO     		\n"
            + " FROM                                                                         		\n"
            + "   IUF_T_PROCEDIM_OGGETTO_STAMP A,                                           		\n"
            + "   IUF_R_OGGETTO_ICONA B                                                      		\n"
            + " WHERE                                                                        		\n"
            + "   A.ID_PROCEDIMENTO_OGGETTO = :ID_PROCEDIMENTO_OGGETTO                       		\n"
            + "   AND A.ID_OGGETTO_ICONA = B.ID_OGGETTO_ICONA                                		\n"
            + "   AND B.EXT_ID_TIPO_DOCUMENTO = :ID_TIPO_DOC                                  		\n"
            + "ORDER BY 1																			\n";
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PROCEDIMENTO_OGGETTO", idProcedimentoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_DOC", idTipoDoc, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource, new ResultSetExtractor<DecodificaDTO<String>>()
      {
        @Override
        public DecodificaDTO<String> extractData(ResultSet rs) throws SQLException, DataAccessException
        {
          if (rs.next())
          {
            DecodificaDTO<String> d = new DecodificaDTO<String>();
            if(rs.getString("NUM_PROTOCOLLO")!=null &&rs.getString("DATA_PROTOCOLLO")!=null){
            	d.setCodice(rs.getString("NUM_PROTOCOLLO"));
	            d.setDescrizione(IuffiUtils.DATE.formatDate(rs.getDate("DATA_PROTOCOLLO")));
	            return d;
            }	            
          }
          return new DecodificaDTO<String>();
        }
      });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idProcedimentoOggetto", idProcedimentoOggetto),
              new LogParameter("idTipoDoc", idTipoDoc)
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
  
  public HashMap<String, List<String>> getAziendeBandiProfessionistaPerRiepilogo(
	  UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo) throws InternalUnexpectedException {
      String THIS_METHOD = "[" + THIS_CLASS + "::getAziendeBandiProfessionistaPerRiepilogo]";
      if (logger.isDebugEnabled()) {
	  logger.debug(THIS_METHOD + " BEGIN.");
      }

      String QUERY = " SELECT                                                              \n"
	      + "  S.ID_AZIENDA, EXT_BANDO_TIPO_PRATICA ID_BANDO, B.DENOMINAZIONE BANDO, A.CUAA		\n"
	      + " FROM                                                                      \n"
	      + "  SMRGAA.SMRGAA_V_PROFESSIONISTI S,									    \n"
	      + "  SMRGAA.SMRGAA_V_DATI_ANAGRAFICI A,									    \n"
	      + "  IUF_D_BANDO B,														    \n"
	      + "  PAPUA.PAPUA_V_RUOLI_ANAG VRA 		   								    \n"
	      + " WHERE                                                                     \n"
	      + "  VRA.ID_PROCEDIMENTO = S.ID_PROCEDIMENTO									\n"
	      + "  AND S.ID_RUOLO = VRA.EXT_ID_RUOLO_ANAG									\n"
	      + "  AND S.EXT_BANDO_TIPO_PRATICA = B.ID_BANDO								\n"
	      + "  AND S.ID_AZIENDA = A.ID_AZIENDA											\n"
	      + "  AND A.DATA_FINE_VALIDITA IS NULL											\n"
	      + "  AND S.CODICE_FISCALE = :CODICE_FISCALE                       			\n"
	      + "  AND S.ID_PROCEDIMENTO=:EXT_ID_PROCEDIMENTO				   	    		\n"
	      + "  AND VRA.CODICE_RUOLO_PAPUA = :CODICE_RUOLO_PAPUA							\n"
	      + "  AND SYSDATE BETWEEN S.DATA_INIZIO_VALIDITA_PROF_AZ AND NVL(DATA_FINE_VALIDITA_PROF_AZ,SYSDATE) 					\n"
	      + " ORDER BY S.ID_AZIENDA, EXT_BANDO_TIPO_PRATICA								\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      mapParameterSource.addValue("CODICE_FISCALE", utenteAbilitazioni.getCodiceFiscale(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_PROCEDIMENTO", idProcedimentoAgricolo, Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RUOLO_PAPUA", utenteAbilitazioni.getRuolo().getCodice(), Types.VARCHAR);

      try {
	  return namedParameterJdbcTemplate.query(QUERY.toString(), mapParameterSource,
		  new ResultSetExtractor<HashMap<String, List<String>>>() {

	      @Override
	      public HashMap<String, List<String>> extractData(ResultSet rs)
		      throws SQLException, DataAccessException {
		  HashMap<String, List<String>> map = new HashMap<>();
		  List<String> bandi = new ArrayList<>();
		  Long key = -1l;
		  Long keyOld = -2l;
		  while (rs.next()) {
		      key = rs.getLong("ID_AZIENDA");
		      if (key.longValue() != keyOld.longValue()) {
			  keyOld = key;

			  bandi = new ArrayList<>();
			  map.put(rs.getString("CUAA"), bandi);
		      }
		      bandi.add(rs.getString("BANDO"));
		  }
		  return map;
	      }
	  });
      } catch (Throwable t) {
	  InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {},
		  new LogVariable[] {}, QUERY, mapParameterSource);
	  logInternalUnexpectedException(e, THIS_METHOD);
	  throw e;
      } finally {
	  if (logger.isDebugEnabled()) {
	      logger.debug(THIS_METHOD + " END.");
	  }
      }
  }
  
  public String getNomeCognomeProfessionista(UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo)
	  throws InternalUnexpectedException {
      String THIS_METHOD = "[" + THIS_CLASS + "::getIdAziendeProfessionista]";
      if (logger.isDebugEnabled()) {
	  logger.debug(THIS_METHOD + " BEGIN.");
      }

      String QUERY = " SELECT                                                              \n"
	      + "  NOME || ' ' || COGNOME	NOMECOGNOME										\n"
	      + " FROM                                                                      \n"
	      + "  SMRGAA.SMRGAA_V_PROFESSIONISTI S,									    \n"
	      + "  PAPUA.PAPUA_V_RUOLI_ANAG VRA 		   								    \n"
	      + " WHERE                                                                     \n"
	      + "  VRA.ID_PROCEDIMENTO = S.ID_PROCEDIMENTO									\n"
	      + "  AND S.ID_RUOLO = VRA.EXT_ID_RUOLO_ANAG									\n"
	      + "  AND S.CODICE_FISCALE = :CODICE_FISCALE                       			\n"
	      + "  AND S.ID_PROCEDIMENTO=:EXT_ID_PROCEDIMENTO				   	    		\n"
	      + "  AND VRA.CODICE_RUOLO_PAPUA = :CODICE_RUOLO_PAPUA							\n"
	      + "  AND SYSDATE BETWEEN S.DATA_INIZIO_VALIDITA_PROF_AZ AND NVL(DATA_FINE_VALIDITA_PROF_AZ,SYSDATE) 					\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      mapParameterSource.addValue("CODICE_FISCALE", utenteAbilitazioni.getCodiceFiscale(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_PROCEDIMENTO", idProcedimentoAgricolo, Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RUOLO_PAPUA", utenteAbilitazioni.getRuolo().getCodice(), Types.VARCHAR);

      try {
	  return namedParameterJdbcTemplate.query(QUERY.toString(), mapParameterSource,
		  new ResultSetExtractor<String>() {

	      @Override
	      public String extractData(ResultSet rs) throws SQLException, DataAccessException {

		  while (rs.next()) {
		      return rs.getString("NOMECOGNOME");
		  }
		  return null;
	      }
	  });
      } catch (Throwable t) {
	  InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {},
		  new LogVariable[] {}, QUERY, mapParameterSource);
	  logInternalUnexpectedException(e, THIS_METHOD);
	  throw e;
      } finally {
	  if (logger.isDebugEnabled()) {
	      logger.debug(THIS_METHOD + " END.");
	  }
      }
  }
  
  public List<Long> getIdBandiPerAziendaProfessionista(UtenteAbilitazioni utenteAbilitazioni, int idProcedimentoAgricolo)
	  throws InternalUnexpectedException {

      String THIS_METHOD = "[" + THIS_CLASS + "::getIdBandiPerAziendaProfessionista]";
      if (logger.isDebugEnabled()) {
	  logger.debug(THIS_METHOD + " BEGIN.");
      }

      String QUERY = " SELECT                                                              \n"
	      + "  S.EXT_BANDO_TIPO_PRATICA												    \n"
	      + " FROM                                                                      \n"
	      + "  SMRGAA.SMRGAA_V_PROFESSIONISTI S,									    \n"
	      + "  PAPUA.PAPUA_V_RUOLI_ANAG VRA 		   								    \n"
	      + " WHERE                                                                     \n"
	      + "  VRA.ID_PROCEDIMENTO = S.ID_PROCEDIMENTO									\n"
	      + "  AND S.ID_RUOLO = VRA.EXT_ID_RUOLO_ANAG									\n"
	      + "  AND S.CODICE_FISCALE = :CODICE_FISCALE                       			\n"
	      + "  AND S.ID_PROCEDIMENTO=:EXT_ID_PROCEDIMENTO				   	    		\n"
	      + "  AND VRA.CODICE_RUOLO_PAPUA = :CODICE_RUOLO_PAPUA							\n"
	      + "  AND SYSDATE BETWEEN S.DATA_INIZIO_VALIDITA_PROF_AZ AND NVL(DATA_FINE_VALIDITA_PROF_AZ,SYSDATE) 					\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      mapParameterSource.addValue("CODICE_FISCALE", utenteAbilitazioni.getCodiceFiscale(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_PROCEDIMENTO", idProcedimentoAgricolo, Types.NUMERIC);
      mapParameterSource.addValue("CODICE_RUOLO_PAPUA", utenteAbilitazioni.getRuolo().getCodice(), Types.VARCHAR);

      try {
	  return namedParameterJdbcTemplate.query(QUERY.toString(), mapParameterSource,
		  new ResultSetExtractor<List<Long>>() {

	      @Override
	      public List<Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
		  ArrayList<Long> list = new ArrayList<Long>();

		  while (rs.next()) {
		      list.add(rs.getLong("EXT_BANDO_TIPO_PRATICA"));
		  }
		  return list;
	      }
	  });
      } catch (Throwable t) {
	  InternalUnexpectedException e = new InternalUnexpectedException(t, new LogParameter[] {},
		  new LogVariable[] {}, QUERY, mapParameterSource);
	  logInternalUnexpectedException(e, THIS_METHOD);
	  throw e;
      } finally {
	  if (logger.isDebugEnabled()) {
	      logger.debug(THIS_METHOD + " END.");
	  }
      }

  }

}