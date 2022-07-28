package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ImportiRipartitiListaLiquidazioneDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -503661603458055569L;

  private String            operazione;
  private String            identificativo;
  private String            causalePagam;
  private String            cuaa;
  private String            denominazione;
  private Long              idListaLiqImpLiq;
  private BigDecimal        quotaUe;
  private BigDecimal        quotaReg;
  private BigDecimal        quotaNaz;
  private BigDecimal        importoTotale;
  private BigDecimal        importoPremio;
  private BigDecimal        anticipoErogato;

  public BigDecimal getImportoPremio()
  {
    return importoPremio;
  }

  public void setImportoPremio(BigDecimal importoPremio)
  {
    this.importoPremio = importoPremio;
  }

  public BigDecimal getAnticipoErogato()
  {
    return anticipoErogato;
  }

  public void setAnticipoErogato(BigDecimal anticipoErogato)
  {
    this.anticipoErogato = anticipoErogato;
  }

  public String getOperazione()
  {
    return operazione;
  }

  public void setOperazione(String operazione)
  {
    this.operazione = operazione;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = identificativo;
  }

  public String getCausalePagam()
  {
    return causalePagam;
  }

  public void setCausalePagam(String causalePagam)
  {
    this.causalePagam = causalePagam;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public BigDecimal getQuotaReg()
  {
    return quotaReg;
  }

  public void setQuotaReg(BigDecimal quotaReg)
  {
    this.quotaReg = quotaReg;
  }

  public BigDecimal getQuotaNaz()
  {
    return quotaNaz;
  }

  public void setQuotaNaz(BigDecimal quotaNaz)
  {
    this.quotaNaz = quotaNaz;
  }

  public BigDecimal getImportoTotale()
  {
    return importoTotale;
  }

  public void setImportoTotale(BigDecimal importoTotale)
  {
    this.importoTotale = importoTotale;
  }

  public Long getIdListaLiqImpLiq()
  {
    return idListaLiqImpLiq;
  }

  public void setIdListaLiqImpLiq(Long idListaLiqImpLiq)
  {
    this.idListaLiqImpLiq = idListaLiqImpLiq;
  }

  public BigDecimal getQuotaUe()
  {
    return quotaUe;
  }

  public void setQuotaUe(BigDecimal quotaUe)
  {
    this.quotaUe = quotaUe;
  }

}
