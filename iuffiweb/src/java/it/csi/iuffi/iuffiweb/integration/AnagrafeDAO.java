package it.csi.iuffi.iuffiweb.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public class AnagrafeDAO extends BaseDAO
{

  private static final String THIS_CLASS = AnagrafeDAO.class
      .getSimpleName();

  public AnagrafeDAO()
  {
  }

  public List<BandoDTO> getAziendeByCUAA(String cuaa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAziendeByCUAA";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    try
    {
      SELECT = " SELECT ID_AZIENDA                 \n"
          + "  FROM DB_ANAGRAFICA_AZIENDA       \n"
          + "  WHERE DATA_FINE_VALIDITA IS NULL	\n"
          + "  AND DATA_CESSAZIONE IS NULL      \n"
          + "  AND CUAA = :CUAA                 \n";

      parameterSource.addValue("CUAA", cuaa, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<BandoDTO>>()
          {
            @Override
            public List<BandoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<BandoDTO> list = new ArrayList<BandoDTO>();
              BandoDTO bandoDTO = null;
              Long lastIdBando = null;
              long idBando = 0;
              while (rs.next())
              {
                idBando = rs.getLong("ID_BANDO");
                if (lastIdBando == null || lastIdBando != idBando)
                {
                  bandoDTO = new BandoDTO();
                  bandoDTO.setIdBando(idBando);
                  bandoDTO.setDataInizio(rs.getDate("DATA_INIZIO"));
                  bandoDTO.setDataFine(rs.getDate("DATA_FINE"));
                  bandoDTO.setDenominazione(rs.getString("DENOMINAZIONE"));
                  bandoDTO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                  bandoDTO
                      .setDescrizioneFiltro(rs.getString("DESCRIZIONE_FILTRO"));
                  bandoDTO.setIstruzioneSqlFiltro(
                      rs.getString("ISTRUZIONE_SQL_FILTRO"));
                  bandoDTO.setLivelli(new ArrayList<LivelloDTO>());
                  list.add(bandoDTO);
                  lastIdBando = idBando;
                }
                LivelloDTO livelloDTO = new LivelloDTO();
                livelloDTO.setCodice(rs.getString("CODICE"));
                livelloDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                livelloDTO.setIdLivello(rs.getLong("ID_BANDO"));
                bandoDTO.getLivelli().add(livelloDTO);
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

}
