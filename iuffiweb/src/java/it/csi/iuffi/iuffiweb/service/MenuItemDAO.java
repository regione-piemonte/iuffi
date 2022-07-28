package it.csi.iuffi.iuffiweb.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.iuffi.iuffiweb.dto.MenuItemDTO;
import it.csi.iuffi.iuffiweb.integration.BaseDAO;

public class MenuItemDAO extends BaseDAO
{
  
  //private static final String THIS_CLASS = MenuItemDAO.class.getSimpleName();

  public MenuItemDAO() {  }

  //private static final String SELECT_MENU = "SELECT * FROM iuf_d_menu_item WHERE NVL(id_parent,0) = :idParent AND show = 'S' ORDER BY seq_menu_item";
  
  //private static String SELECT_MENU = "SELECT * FROM iuf_d_menu_item m WHERE NVL(id_parent,0) = :idParent AND show = 'S' AND ";
      //+ "NOT EXISTS (SELECT 1 FROM iuf_r_disabilita_cdu a WHERE a.ext_cod_macro_cdu = m.use_case"
      //+ " AND a.ext_id_livello = :idLivello) ORDER BY seq_menu_item";
  
  private static final String SELECT_BREADCRUMBS = "SELECT d.*,LEVEL AS livello \r\n" + 
                                                     "FROM   iuf_d_menu_item d \r\n" + 
                                                     "WHERE d.show = 'S' OR d.id_menu_item = 0 \r\n" +
                                                    "START WITH d.id_menu_item = :idMenuItem \r\n" + 
                                                    "CONNECT BY PRIOR id_parent = id_menu_item \r\n" + 
                                                    "ORDER BY 1";

  private static final String SELECT_BREADCRUMBS_BY_PATH = "SELECT DISTINCT d.*,LEVEL AS livello \r\n" +
                                                            "FROM iuf_d_menu_item d \r\n" +
                                                            //"WHERE d.show = 'S' OR d.id_menu_item = 0 \r\n" +
                                                           "START WITH d.path LIKE :path \r\n" + 
                                                           "CONNECT BY PRIOR id_parent = id_menu_item \r\n" + 
                                                           "ORDER BY NVL(d.id_parent,0),d.id_menu_item";

  public List<MenuItemDTO> getMainMenu(int idParent, long[] idLivello, boolean readOnly)
  {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("idParent", idParent);
    //params.addValue("idLivello", idLivello);
    String SELECT_MENU = "SELECT * FROM iuf_d_menu_item m WHERE NVL(id_parent,0) = :idParent AND show = 'S' AND ";
    for (int i=0; i<idLivello.length; i++) {
      if(i==0) {
        SELECT_MENU += "(NOT EXISTS (SELECT 1 FROM iuf_r_disabilita_cdu a WHERE a.ext_cod_macro_cdu = m.use_case AND a.ext_id_livello =" + idLivello[0] + ")";
        }
      else {
        SELECT_MENU += " OR NOT EXISTS (SELECT 1 FROM iuf_r_disabilita_cdu a WHERE a.ext_cod_macro_cdu = m.use_case AND a.ext_id_livello =" + idLivello[i] + ")";
      }
    }
    SELECT_MENU += ") ";
    
    if (readOnly)
      SELECT_MENU += " AND NOT EXISTS (SELECT 1 FROM iuf_d_elenco_cdu e WHERE e.ext_cod_macro_cdu = m.use_case AND e.tipo_azione = 'W') ";
    
    SELECT_MENU += "ORDER BY seq_menu_item";
    
    logger.debug(SELECT_MENU);
    return namedParameterJdbcTemplate.query(SELECT_MENU, params, new RowMapper<MenuItemDTO>() {

      @Override
      public MenuItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MenuItemDTO(rs.getInt("id_menu_item"),
                              rs.getString("title_menu_item"),
                              rs.getInt("id_parent"),
                              rs.getString("use_case"),
                              rs.getString("path"),
                              rs.getInt("seq_menu_item"),
                              rs.getString("show"));
      }
    });
  }

  public List<MenuItemDTO> getBreadcrumbs(int idMenuItem)
  {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("idMenuItem", idMenuItem);
    logger.debug(SELECT_BREADCRUMBS);
    return namedParameterJdbcTemplate.query(SELECT_BREADCRUMBS, params, new RowMapper<MenuItemDTO>() {

      @Override
      public MenuItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MenuItemDTO(rs.getInt("id_menu_item"),
                              rs.getString("title_menu_item"),
                              rs.getInt("id_parent"),
                              rs.getString("use_case"),
                              rs.getString("path"),
                              rs.getInt("seq_menu_item"),
                              rs.getString("show"));
      }
    });
  }

  public List<MenuItemDTO> getBreadcrumbs(String path)
  {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("path", path);
    logger.debug(SELECT_BREADCRUMBS_BY_PATH);
    return namedParameterJdbcTemplate.query(SELECT_BREADCRUMBS_BY_PATH, params, new RowMapper<MenuItemDTO>() {

      @Override
      public MenuItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MenuItemDTO(rs.getInt("id_menu_item"),
                              rs.getString("title_menu_item"),
                              rs.getInt("id_parent"),
                              rs.getString("use_case"),
                              rs.getString("path"),
                              rs.getInt("seq_menu_item"),
                              rs.getString("show"));
      }
    });
  }

}