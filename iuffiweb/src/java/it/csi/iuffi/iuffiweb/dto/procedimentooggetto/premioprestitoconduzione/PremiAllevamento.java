package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.premioprestitoconduzione;


import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PremiAllevamento implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private String specie;
  private String categoria;
  private BigDecimal uba;
  private BigDecimal coefficiente;
  private String ubaS;
  private String coefficienteS;
  private BigDecimal importounitario;
  private BigDecimal importopremio;
  private String importounitarioS;
  private String importopremioS;
  public String getSpecie()
  {
    return specie;
  }
  public void setSpecie(String specie)
  {
    this.specie = specie;
  }
  public String getCategoria()
  {
    return categoria;
  }
  public void setCategoria(String categoria)
  {
    this.categoria = categoria;
  }
  public BigDecimal getUba()
  {
    return uba;
  }
  public void setUba(BigDecimal uba)
  {
    this.uba = uba;
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
  public BigDecimal getCoefficiente()
  {
    return coefficiente;
  }
  public void setCoefficiente(BigDecimal coefficiente)
  {
    this.coefficiente = coefficiente;
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
  public String getUbaS()
  {
    return ubaS;
  }
  public void setUbaS(String ubaS)
  {
    this.ubaS = ubaS;
  }
  public String getCoefficienteS()
  {
    return coefficienteS;
  }
  public void setCoefficienteS(String coefficienteS)
  {
    this.coefficienteS = coefficienteS;
  }
  
}
