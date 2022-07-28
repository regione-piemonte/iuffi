package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.premioprestitoconduzione;


import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PremiColture implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private String tipo;
  private String superficie;
  private BigDecimal importounitario;
  private BigDecimal importopremio;
  private String importounitarioS;
  private String importopremioS;
  public String getTipo()
  {
    return tipo;
  }
  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }
  public String getSuperficie()
  {
    return superficie;
  }
  public void setSuperficie(String superficie)
  {
    this.superficie = superficie;
  }
  public BigDecimal getImportounitario()
  {
    return importounitario;
  }
  public void setImportounitario(BigDecimal importounitario)
  {
    this.importounitario = importounitario;
  }
  public BigDecimal getImportopremio()
  {
    return importopremio;
  }
  public void setImportopremio(BigDecimal importopremio)
  {
    this.importopremio = importopremio;
  }
  public String getImportounitarioS()
  {
    return importounitarioS;
  }
  public void setImportounitarioS(String importounitarioS)
  {
    this.importounitarioS = importounitarioS;
  }
  public String getImportopremioS()
  {
    return importopremioS;
  }
  public void setImportopremioS(String importopremioS)
  {
    this.importopremioS = importopremioS;
  }
 
 
  
  

}
