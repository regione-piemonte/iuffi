package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class IntegrazioneAlPremioDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -8099703135799887178L;

  private Long              idLivello;
  private String            operazione;
  private BigDecimal        contributoConcesso;
  private BigDecimal        totaleLiquidato;
  private BigDecimal        economie;
  private BigDecimal        contributoIntegrazione;
  private BigDecimal        contributoRiduzioniSanzioni;
  private BigDecimal        maxRiduzioniSanzioni;

  public Long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(Long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getOperazione()
  {
    return operazione;
  }

  public void setOperazione(String operazione)
  {
    this.operazione = operazione;
  }

  public BigDecimal getContributoConcesso()
  {
    return contributoConcesso;
  }

  public void setContributoConcesso(BigDecimal contributoConcesso)
  {
    this.contributoConcesso = contributoConcesso;
  }

  public BigDecimal getTotaleLiquidato()
  {
    return totaleLiquidato;
  }

  public void setTotaleLiquidato(BigDecimal totaleLiquidato)
  {
    this.totaleLiquidato = totaleLiquidato;
  }

  public BigDecimal getEconomie()
  {
    return economie;
  }

  public void setEconomie(BigDecimal economie)
  {
    this.economie = economie;
  }

  public BigDecimal getContributoIntegrazione()
  {
    return contributoIntegrazione != null ? contributoIntegrazione
        : BigDecimal.ZERO;
  }

  public void setContributoIntegrazione(BigDecimal contributoIntegrazione)
  {
    this.contributoIntegrazione = contributoIntegrazione;
  }

  public BigDecimal getContributoRiduzioniSanzioni()
  {
    return contributoRiduzioniSanzioni;
  }

  public void setContributoRiduzioniSanzioni(
      BigDecimal contributoRiduzioniSanzioni)
  {
    this.contributoRiduzioniSanzioni = contributoRiduzioniSanzioni;
  }

  public BigDecimal getMaxRiduzioniSanzioni()
  {
    return maxRiduzioniSanzioni;
  }

  public void setMaxRiduzioniSanzioni(BigDecimal maxRiduzioniSanzioni)
  {
    this.maxRiduzioniSanzioni = maxRiduzioniSanzioni;
  }

}
