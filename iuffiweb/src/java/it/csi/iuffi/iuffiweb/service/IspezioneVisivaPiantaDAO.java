package it.csi.iuffi.iuffiweb.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.iuffi.iuffiweb.dto.internal.LogParameter;
import it.csi.iuffi.iuffiweb.dto.internal.LogVariable;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;
import it.csi.iuffi.iuffiweb.model.DiametroDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaPiantaDTO;
import it.csi.iuffi.iuffiweb.model.PositivitaDTO;
import it.csi.iuffi.iuffiweb.model.api.Coordinate;
import it.csi.iuffi.iuffiweb.model.api.Ubicazione;

public class IspezioneVisivaPiantaDAO extends BaseDAO
{

 private static final String THIS_CLASS = IspezioneVisivaPiantaDAO.class.getSimpleName();
  
  public IspezioneVisivaPiantaDAO(){  }
  
  private static final String INSERT = "INSERT INTO iuf_t_isp_visiva_pianta(" +
     "id_ispezione_visiva_pianta," + 
     "id_ispezione_visiva," + 
     "id_specie_vegetale," +
     "numero_pianta," +
     "latitudine," +
     "longitudine," +
     "nome," +
     "cognome," +
     "indirizzo," +
     "telefono," +
     "email," +
     "numero," +
     "positivita,"+
     "diametro,"+
     "flag_tree_climber_ispezione," +
     "flag_tree_climber_taglio," +
     "note1," +
     "note2," +
     "note3," +
     "ext_id_utente_aggiornamento," +
     "data_ultimo_aggiornamento" +

 ") " + 
 "VALUES(seq_iuf_t_isp_visiva_pianta.nextval, :idIspezioneVisiva,  :idSpecieVegetale, :numeroPianta, :latitudine, :longitudine, :nome, :cognome, :indirizzo, :telefono, :email, :numero, :positivita, :diametro, :flagTreeClimberIspezione, :flagTreeClimberTaglio, :note1,  :note2, :note3," + 
 ":ext_id_utente_aggiornamento, SYSDATE)";
  
  private static final String UPDATE = "UPDATE iuf_t_isp_visiva_pianta " +
  "set id_specie_vegetale = :idSpecieVegetale, numero_pianta = :numeroPianta, latitudine = :latitudine, longitudine = :longitudine, " + 
  "nome = :nome, cognome = :cognome,  indirizzo = :indirizzo, telefono = :telefono, email = :email, numero = :numero, positivita = :positivita, diametro = :diametro, flag_tree_climber_ispezione = :flagTreeClimberIspezione, " +
  "flag_tree_climber_taglio = :flagTreeClimberTaglio, note1 = :note1, note2 = :note2, note3 = :note3, ext_id_utente_aggiornamento = :ext_id_utente_aggiornamento, data_ultimo_aggiornamento = SYSDATE " +
  "where id_ispezione_visiva_pianta = :idIspezioneVisivaPianta";
  
  /**
   * 
   * @return
   * @throws InternalUnexpectedException
   */
  public Integer insert(IspezioneVisivaPiantaDTO ispezioneVisivaPianta) throws InternalUnexpectedException
  {
    KeyHolder holder = new GeneratedKeyHolder();    
    String THIS_METHOD = "[" + THIS_CLASS + "::insertIspezione]";

    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
       
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      mapParameterSource.addValue("idIspezioneVisiva", ispezioneVisivaPianta.getIdIspezioneVisiva());
      mapParameterSource.addValue("idSpecieVegetale", ispezioneVisivaPianta.getIdSpecieVegetale());
      mapParameterSource.addValue("numeroPianta", ispezioneVisivaPianta.getNumeroPianta());
      mapParameterSource.addValue("latitudine", ispezioneVisivaPianta.getCoordinate().getLatitudine());
      mapParameterSource.addValue("longitudine", ispezioneVisivaPianta.getCoordinate().getLongitudine());
      mapParameterSource.addValue("nome", ispezioneVisivaPianta.getUbicazione().getNome(), Types.VARCHAR);     
      mapParameterSource.addValue("cognome", ispezioneVisivaPianta.getUbicazione().getCognome(), Types.VARCHAR);      
      mapParameterSource.addValue("indirizzo", ispezioneVisivaPianta.getUbicazione().getIndirizzo(), Types.VARCHAR);  
      mapParameterSource.addValue("telefono", ispezioneVisivaPianta.getUbicazione().getTelefono(), Types.VARCHAR);  
      mapParameterSource.addValue("email", ispezioneVisivaPianta.getUbicazione().getEmail(), Types.VARCHAR);  
      mapParameterSource.addValue("numero", ispezioneVisivaPianta.getUbicazione().getNumero(), Types.VARCHAR);  
      if(ispezioneVisivaPianta.getPositivita() == null || ispezioneVisivaPianta.getPositivita().intValue() == 0)
        mapParameterSource.addValue("positivita", null);
      else
        mapParameterSource.addValue("positivita", ispezioneVisivaPianta.getPositivita());        
      if(ispezioneVisivaPianta.getDiametro() == null || ispezioneVisivaPianta.getDiametro() == 0)
        mapParameterSource.addValue("diametro", null);
      else
        mapParameterSource.addValue("diametro", ispezioneVisivaPianta.getDiametro());
      mapParameterSource.addValue("flagTreeClimberIspezione", ispezioneVisivaPianta.getFlagTreeClimberIspezione(), Types.VARCHAR);
      mapParameterSource.addValue("flagTreeClimberTaglio", ispezioneVisivaPianta.getFlagTreeClimberTaglio(), Types.VARCHAR);
      mapParameterSource.addValue("note1", ispezioneVisivaPianta.getNote1(), Types.VARCHAR);
      mapParameterSource.addValue("note2", ispezioneVisivaPianta.getNote2(), Types.VARCHAR);
      mapParameterSource.addValue("note3", ispezioneVisivaPianta.getNote3(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", ispezioneVisivaPianta.getExtIdUtenteAggiornamento());     
      

      
      logger.debug(INSERT);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource, holder, new String[] {"id_ispezione_visiva_pianta"});
      Integer idIspezioneVisivaPianta = holder.getKey().intValue();
      ispezioneVisivaPianta.setIdIspezioneVisivaPianta(idIspezioneVisivaPianta);
      return idIspezioneVisivaPianta;
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

  
   public List<IspezioneVisivaPiantaDTO> findAll() throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findAll";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT_ALL = " SELECT * FROM IUF_T_ISP_VISIVA_PIANTA t  ORDER BY t.ID_ISPEZIONE_VISIVA_PIANTA ";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      logger.debug(SELECT_ALL);
      return queryForList(SELECT_ALL, mapParameterSource, IspezioneVisivaPiantaDTO.class);
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

   
   
//  public List<IspezioneVisivaPiantaDTO> findByFilter(IspezioneVisivaPiantaDTO ispezioneVisivaPianta) throws InternalUnexpectedException
//  {
//    final String THIS_METHOD = "findByFilter";
//    if (logger.isDebugEnabled())
//    {
//      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
//    }
//
//    String SELECT_BY_FILTER = "select A.ID_ISPEZIONE," + 
//        "A.ID_RILEVAZIONE," + 
//        "D.COGNOME || ' ' || D.NOME AS ISPETTORE," + 
//        "F.DESC_TIPO_AREA AS AREA," + 
//        "E.GENERE_SPECIE AS SPECIE," +
//        "A.NUMERO_AVIV," +
//        "A.ID_SPECIE_VEGETALE," +
//        "A.SUPERFICE as SUPERFICIE," +
//        "A.NUMERO_PIANTE," +
//        "A.FLAG_PRESENZA_ON," +
//        "A.ISTAT_COMUNE," +
//        "A.LATITUDINE," +
//        "A.LONGITUDINE," +
//        "A.FLAG_INDICATORE_INTERVENTO," +
//        "A.RIFERIMENTO_UBICAZIONE," +
//        "'ORGANISMI' as ORGANISMI," + 
//        "'FOTO' AS FOTO," + 
//        "A.NOTE," +
//        "A.ext_id_utente_aggiornamento," +
//        "A.data_ultimo_aggiornamento," +
//        "'ESITO' AS ESITO,B.ID_MISSIONE " +
//        
//        "from IUF_T_ISPEZIONE_VISIVA a,IUF_T_MISSIONE B," + 
//        "IUF_T_RILEVAZIONE C,IUF_T_ANAGRAFICA D,IUF_D_SPECIE_VEGETALE E,IUF_D_TIPO_AREA F " +
//        
//        "WHERE A.ID_RILEVAZIONE=C.ID_RILEVAZIONE " + 
//        "AND C.ID_MISSIONE=B.ID_MISSIONE " + 
//        "AND B.ID_ISPETTORE_ASSEGNATO=D.ID_ANAGRAFICA " +
//        "AND A.ID_SPECIE_VEGETALE=E.ID_SPECIE_VEGETALE " +
//        "AND C.ID_TIPO_AREA=F.ID_TIPO_AREA " +
//        "AND C.ID_RILEVAZIONE = NVL(:idRilevazione,C.ID_RILEVAZIONE) " +
//        "ORDER BY ID_ISPEZIONE";
//
//    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
//    
//    if (ispezioneVisivaPianta.getIdRilevazione() != null && ispezioneVisivaPianta.getIdRilevazione() > 0)
//      mapParameterSource.addValue("idRilevazione", ispezioneVisivaPianta.getIdRilevazione());
//    else
//      mapParameterSource.addValue("idRilevazione", null, Types.NULL);
//    
//    try
//    {
//      logger.debug(SELECT_BY_FILTER);
//      List<IspezioneVisivaPiantaDTO> list = queryForList(SELECT_BY_FILTER, mapParameterSource, IspezioneVisivaPiantaDTO.class);
//      return list;
//    }
//    catch (Throwable t)
//    {
//      InternalUnexpectedException e = new InternalUnexpectedException(t,
//          new LogParameter[]
//              {}, null, SELECT_BY_FILTER, mapParameterSource);
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

  
  public IspezioneVisivaPiantaDTO findById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

   // String SELECT_BY_ID = "SELECT * FROM IUF_T_ISP_VISIVA_PIANTA WHERE ID_ISPEZIONE_VISIVA_PIANTA = :id";

    String SELECT_BY_ID = "SELECT t.id_ispezione_visiva,id_ispezione_visiva_pianta,"+ 
        "       t.id_specie_vegetale,t.numero_pianta,t.latitudine,t.longitudine,\r\n" + 
        "       t.nome,t.cognome,t.indirizzo,t.telefono,t.email,t.positivita,t.diametro,\r\n" + 
        "       t.flag_tree_climber_ispezione,\r\n" + 
        "       t.flag_tree_climber_taglio,\r\n" + 
        "       t.note1, t.note2, t.note3, \r\n" + 
        "       t.numero_pianta AS quantita,\r\n" + 
        "       a.desc_positivita as desc_positivita,       \r\n" + 
        "       b.genere_specie || ' ' || b.nome_volgare as specie,       \r\n" + 
        "       c.desc_diametro AS desc_diametro,    \r\n" + 
        "       t.numero AS numero   \r\n" +        
        "       FROM IUF_T_ISP_VISIVA_PIANTA t,\r\n" + 
        "       iuf_d_positivita a,  \r\n" + 
        "       iuf_d_specie_vegetale b,  \r\n" + 
        "       iuf_d_diametro C  \r\n" + 
        "       WHERE t.ID_SPECIE_VEGETALE=b.id_specie_vegetale \r\n" + 
        "       AND t.positivita=a.Id_Positivita (+) \r\n" + 
        "       AND T.DIAMETRO=C.ID_DIAMETRO (+) \r\n" + 
        "       AND ID_ISPEZIONE_VISIVA_PIANTA = :id";    

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      IspezioneVisivaPiantaDTO ispezioneVisivaPianta = queryForObject(SELECT_BY_ID, mapParameterSource, IspezioneVisivaPiantaDTO.class);
      return ispezioneVisivaPianta;
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

  public List<IspezioneVisivaPiantaDTO> findByIdIspezioneVisiva(Integer idIspezioneVisiva) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

    String SELECT_BY_ID = "SELECT t.id_ispezione_visiva,id_ispezione_visiva_pianta,t.id_specie_vegetale, "+
         "      t.numero_pianta,t.latitudine,t.longitudine,\r\n" + 
        "       t.nome,t.cognome,t.indirizzo,t.telefono,t.email,t.positivita,t.diametro,\r\n" + 
        "       t.flag_tree_climber_ispezione,\r\n" + 
        "       t.flag_tree_climber_taglio,\r\n" + 
        "       t.note1, t.note2, t.note3, \r\n" + 
        "       t.numero_pianta AS quantita,\r\n" + 
        "       a.desc_positivita as desc_positivita,a.id_positivita as positivita,       \r\n" + 
        "       b.genere_specie || ' ' || b.nome_volgare as specie,       \r\n" + 
        "       c.desc_diametro AS desc_diametro, c.id_diametro as diametro,   \r\n" + 
        "       t.numero AS numero   \r\n" +        
        "       FROM IUF_T_ISP_VISIVA_PIANTA t,\r\n" + 
        "       iuf_d_positivita a,  \r\n" + 
        "       iuf_d_specie_vegetale b,  \r\n" + 
        "       iuf_d_diametro C  \r\n" + 
        "       WHERE ID_ISPEZIONE_VISIVA = :idIspezioneVisiva\r\n" + 
        "       AND t.ID_SPECIE_VEGETALE=b.id_specie_vegetale \r\n" + 
        "       AND t.positivita=a.Id_Positivita (+) \r\n" + 
        "       AND T.DIAMETRO=C.ID_DIAMETRO (+) \r\n" + 
        /*"       GROUP BY t.id_ispezione_visiva, t.id_specie_vegetale, t.numero_pianta, t.latitudine, t.longitudine,\r\n" + 
        "          t.nome, t.cognome, t.indirizzo,t.telefono,t.email,t.numero,t.positivita,t.diametro,\r\n" + 
        "          t.flag_tree_climber_ispezione,t.flag_tree_climber_taglio,\r\n" + 
        "          t.note1,t.note2,t.note3, a.desc_positivita,       \r\n" + 
        "          b.genere_specie || ' ' || b.nome_volgare,       \r\n" + 
        "          c.desc_diametro \r\n" +*/ 
        "ORDER BY 1,2,3";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("idIspezioneVisiva", idIspezioneVisiva);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      //List<IspezioneVisivaPiantaDTO> list = queryForList(SELECT_BY_ID, mapParameterSource, IspezioneVisivaPiantaDTO.class);
      List<IspezioneVisivaPiantaDTO> list = namedParameterJdbcTemplate.query(SELECT_BY_ID, mapParameterSource, getRowMapper());
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
  
  private RowMapper<IspezioneVisivaPiantaDTO> getRowMapper() {
    return new RowMapper<IspezioneVisivaPiantaDTO>() {
      
      @Override
      public IspezioneVisivaPiantaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        IspezioneVisivaPiantaDTO pianta = new IspezioneVisivaPiantaDTO();
        pianta.setIdIspezioneVisivaPianta(rs.getInt("id_ispezione_visiva_pianta"));
        pianta.setIdIspezioneVisiva(rs.getInt("id_ispezione_visiva"));
        pianta.setIdSpecieVegetale(rs.getInt("id_specie_vegetale"));
        pianta.setNumeroPianta(rs.getInt("numero_pianta"));
        pianta.setQuantita(rs.getInt("quantita"));
        pianta.setPositivita(rs.getInt("positivita"));
        pianta.setDiametro(rs.getInt("diametro"));
        pianta.setFlagTreeClimberIspezione(rs.getString("flag_tree_climber_ispezione"));
        pianta.setFlagTreeClimberTaglio(rs.getString("flag_tree_climber_taglio"));
        pianta.setNote1(rs.getString("note1"));
        pianta.setNote2(rs.getString("note2"));
        pianta.setNote3(rs.getString("note3"));
        pianta.setDescDiametro(rs.getString("desc_diametro"));
        pianta.setPositivita(rs.getInt("positivita"));
        pianta.setDiametro(rs.getInt("diametro"));
        pianta.setSpecie(rs.getString("specie"));
        pianta.setIdSpecie(rs.getInt("id_specie_vegetale"));
        pianta.setDescPositivita(rs.getString("desc_positivita"));
        //pianta.setDataUltimoAggiornamento(rs.getTimestamp("data_ultimo_aggiornamento"));
        //pianta.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
        if (rs.getDouble("latitudine") > 0) {
          Coordinate coordinate = new Coordinate(rs.getDouble("latitudine"), rs.getDouble("longitudine"));
          pianta.setCoordinate(coordinate);
        }
        if (rs.getString("nome") != null || rs.getString("cognome") != null || rs.getString("indirizzo") != null || rs.getString("telefono") != null || rs.getString("email") != null || rs.getString("numero") !=null) {
          Ubicazione ubicazione = new Ubicazione();
          ubicazione.setNome(rs.getString("nome"));
          ubicazione.setCognome(rs.getString("cognome"));
          ubicazione.setIndirizzo(rs.getString("indirizzo"));
          ubicazione.setTelefono(rs.getString("telefono"));
          ubicazione.setEmail(rs.getString("email"));
          ubicazione.setNumero(rs.getString("numero"));
          pianta.setUbicazione(ubicazione);
        }
        return pianta;
      }
    };
  }

  public void update(IspezioneVisivaPiantaDTO ispezioneVisivaPianta)throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::updateMissione]";
    
    if (logger.isDebugEnabled()){
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try  {
      
      mapParameterSource.addValue("idIspezioneVisivaPianta", ispezioneVisivaPianta.getIdIspezioneVisivaPianta());
     // mapParameterSource.addValue("idIspezioneVisiva", ispezioneVisivaPianta.getIdIspezioneVisiva());
      mapParameterSource.addValue("idSpecieVegetale", ispezioneVisivaPianta.getIdSpecieVegetale());
      mapParameterSource.addValue("numeroPianta", ispezioneVisivaPianta.getNumeroPianta());
      mapParameterSource.addValue("latitudine", ispezioneVisivaPianta.getCoordinate().getLatitudine());
      mapParameterSource.addValue("longitudine", ispezioneVisivaPianta.getCoordinate().getLongitudine());
      mapParameterSource.addValue("nome", ispezioneVisivaPianta.getUbicazione().getNome(), Types.VARCHAR);     
      mapParameterSource.addValue("cognome", ispezioneVisivaPianta.getUbicazione().getCognome(), Types.VARCHAR);      
      mapParameterSource.addValue("indirizzo", ispezioneVisivaPianta.getUbicazione().getIndirizzo(), Types.VARCHAR);  
      mapParameterSource.addValue("telefono", ispezioneVisivaPianta.getUbicazione().getTelefono(), Types.VARCHAR);  
      mapParameterSource.addValue("email", ispezioneVisivaPianta.getUbicazione().getEmail(), Types.VARCHAR);  
      mapParameterSource.addValue("numero", ispezioneVisivaPianta.getUbicazione().getNumero(), Types.VARCHAR);
      mapParameterSource.addValue("positivita", ispezioneVisivaPianta.getPositivita());
      mapParameterSource.addValue("diametro", ispezioneVisivaPianta.getDiametro());
      mapParameterSource.addValue("flagTreeClimberIspezione", ispezioneVisivaPianta.getFlagTreeClimberIspezione(), Types.VARCHAR);
      mapParameterSource.addValue("flagTreeClimberTaglio", ispezioneVisivaPianta.getFlagTreeClimberTaglio(), Types.VARCHAR);
      mapParameterSource.addValue("note1", ispezioneVisivaPianta.getNote1(), Types.VARCHAR);
      mapParameterSource.addValue("note2", ispezioneVisivaPianta.getNote2(), Types.VARCHAR);
      mapParameterSource.addValue("note3", ispezioneVisivaPianta.getNote3(), Types.VARCHAR);
      mapParameterSource.addValue("ext_id_utente_aggiornamento", ispezioneVisivaPianta.getExtIdUtenteAggiornamento());        
      
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


  
//  public List<FotoDTO> findFotoByIdIspezioneVisiva(Integer id) throws InternalUnexpectedException
//  {
//    final String THIS_METHOD = "findById";
//    if (logger.isDebugEnabled())
//    {
//      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
//    }
//
//    String SELECT_BY_ID = "select c.id_foto,c.foto,c.nome_file,c.data_foto,c.tag " + 
//        "from iuf_r_isp_visiva_foto a, iuf_t_ispezione_visiva b," + 
//        "iuf_t_foto c " + 
//        "where b.id_ispezione=a.id_ispezione_visiva " + 
//        "and a.id_foto=c.id_foto " + 
//        "and b.id_ispezione=:id";
//
//    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
//    mapParameterSource.addValue("id", id);
//    
//    try
//    {
//      logger.debug(SELECT_BY_ID);
//      List<FotoDTO> foto = queryForList(SELECT_BY_ID, mapParameterSource, FotoDTO.class);
//      return foto;
//    } catch (Throwable t)
//    {
//      InternalUnexpectedException e = new InternalUnexpectedException(t,
//          new LogParameter[]
//              {}, null, SELECT_BY_ID, mapParameterSource);
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
//
//  
//  public FotoDTO findFotoById(Integer id) throws InternalUnexpectedException
//  {
//    final String THIS_METHOD = "findById";
//    if (logger.isDebugEnabled())
//    {
//      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
//    }
//
//    String SELECT_BY_ID = "select c.id_foto,c.foto,c.nome_file,c.data_foto,c.tag " + 
//        "from iuf_t_foto c " + 
//        "where c.id_foto =:id";
//
//    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
//    mapParameterSource.addValue("id", id);
//    
//    try
//    {
//      logger.debug(SELECT_BY_ID);
//      //MissioneDTO missione = queryForObject(SELECT_BY_ID, mapParameterSource, MissioneDTO.class);
//      FotoDTO foto = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, mapParameterSource, getRowMapper());
//      return foto;
//    }
//    catch (Throwable t)
//    {
//      InternalUnexpectedException e = new InternalUnexpectedException(t,
//          new LogParameter[]
//              {}, null, SELECT_BY_ID, mapParameterSource);
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
//  
//  private RowMapper<FotoDTO> getRowMapper() {
//    return new RowMapper<FotoDTO>() {
//      
//      @Override
//      public FotoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
//        FotoDTO foto = new FotoDTO();
//        foto.setIdFoto(rs.getInt("id_foto"));
//        foto.setNomeFile(rs.getString("nome_file"));
//        foto.setDataFoto(rs.getDate("data_foto"));
//        foto.setTag(rs.getString("tag"));
//        foto.setNote(rs.getString("note"));
//        lobHandler = new OracleLobHandler();
//        byte[] file = lobHandler.getBlobAsBytes(rs, "foto");
//        foto.setFoto(file);
//        foto.setExtIdUtenteAggiornamento(rs.getLong("ext_id_utente_aggiornamento"));
//        return foto;
//      }
//    };
//  } 
//
//  public LobHandler getLobHandler() {
//    return lobHandler;
//  }
//
//  public void setLobHandler(LobHandler lobHandler) {
//    this.lobHandler = lobHandler;
//  }
//
//  
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

      // DELETE iuf_t_isp_visiva_pianta
      DELETE = "DELETE FROM iuf_t_isp_visiva_pianta i \r\n" + 
          "WHERE i.id_ispezione_visiva = :idIspezione";
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
  
  public void removePianta(Integer idIspezione, Integer idIspezioneVisivaPianta) throws InternalUnexpectedException
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
      mapParameterSource.addValue("idIspezioneVisivaPianta", idIspezioneVisivaPianta);

      // DELETE iuf_t_isp_visiva_pianta
      DELETE = "DELETE FROM iuf_t_isp_visiva_pianta i \r\n" + 
          "WHERE i.id_ispezione_visiva = :idIspezione and i.id_ispezione_visiva_pianta = :idIspezioneVisivaPianta";
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
  
  public IspezioneVisivaPiantaDTO findByIdIspVisPianta(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findById";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }

   // String SELECT_BY_ID = "SELECT * FROM IUF_T_ISP_VISIVA_PIANTA WHERE ID_ISPEZIONE_VISIVA_PIANTA = :id";

    String SELECT_BY_ID = "SELECT t.id_ispezione_visiva,id_ispezione_visiva_pianta,"+ 
        "       t.id_specie_vegetale,t.numero_pianta,t.latitudine,t.longitudine,\r\n" + 
        "       t.nome,t.cognome,t.indirizzo,t.telefono,t.email,t.positivita,t.diametro,\r\n" + 
        "       t.flag_tree_climber_ispezione,\r\n" + 
        "       t.flag_tree_climber_taglio,\r\n" + 
        "       t.note1, t.note2, t.note3, \r\n" + 
        "       t.numero_pianta AS quantita,\r\n" + 
        "       a.desc_positivita as desc_positivita,       \r\n" + 
        "       b.genere_specie || ' ' || b.nome_volgare as specie,       \r\n" + 
        "       c.desc_diametro AS desc_diametro,    \r\n" + 
        "       t.numero AS numero   \r\n" +        
        "       FROM IUF_T_ISP_VISIVA_PIANTA t,\r\n" + 
        "       iuf_d_positivita a,  \r\n" + 
        "       iuf_d_specie_vegetale b,  \r\n" + 
        "       iuf_d_diametro C  \r\n" + 
        "       WHERE t.ID_SPECIE_VEGETALE=b.id_specie_vegetale \r\n" + 
        "       AND t.positivita=a.Id_Positivita (+) \r\n" + 
        "       AND T.DIAMETRO=C.ID_DIAMETRO (+) \r\n" + 
        "       AND ID_ISPEZIONE_VISIVA_PIANTA = :id";    

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    
    try
    {
      logger.debug(SELECT_BY_ID);
      List<IspezioneVisivaPiantaDTO> ispezioneVisivaPiante = namedParameterJdbcTemplate.query(SELECT_BY_ID, mapParameterSource, getRowMapper());
      IspezioneVisivaPiantaDTO ispezioneVisivaPianta = ispezioneVisivaPiante.get(0);
      return ispezioneVisivaPianta;
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
  
  public List<PositivitaDTO> findPositivita() throws InternalUnexpectedException
 {
   final String THIS_METHOD = "findPositivita";
   if (logger.isDebugEnabled())
   {
     logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
   }
   String SELECT = " SELECT p.* FROM IUF_D_POSITIVITA p";

   MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
   List<PositivitaDTO> positivita;
   try
   {
     logger.debug(SELECT);
     positivita = queryForList(SELECT, mapParameterSource, PositivitaDTO.class);
     return positivita;
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
  
  public List<DiametroDTO> findDiametro() throws InternalUnexpectedException
 {
   final String THIS_METHOD = "findDiametro";
   if (logger.isDebugEnabled())
   {
     logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
   }
   String SELECT = " SELECT p.* FROM IUF_D_DIAMETRO p";

   MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
   List<DiametroDTO> diametro;
   try
   {
     logger.debug(SELECT);
     diametro =  queryForList(SELECT, mapParameterSource, DiametroDTO.class);
     return diametro;
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
  
  public PositivitaDTO findPositivitaById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findPositivitaById(Integer id)";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT = " SELECT p.* FROM IUF_D_POSITIVITA p where p.id_positivita=:id";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    PositivitaDTO positivita;
    try
    {
      logger.debug(SELECT);
      positivita = queryForObject(SELECT, mapParameterSource, PositivitaDTO.class);
      return positivita;
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
 

  public DiametroDTO findDiametroById(Integer id) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "findDiametroById(Integer id)";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String SELECT = " SELECT p.* FROM IUF_D_DIAMETRO p where p.id_diametro=:id";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("id", id);
    DiametroDTO diametro;
    try
    {
      logger.debug(SELECT);
      diametro = queryForObject(SELECT, mapParameterSource, DiametroDTO.class);
      return diametro;
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

}
