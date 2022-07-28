package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class CensimentoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -8491016052147313355L;
  private Long idDistretto;
  private Long idCensimento;
  private int anno;
  private BigDecimal densitaSupCens;
  private BigDecimal densitaCapiSus;
  private BigDecimal densitaObiettivo;
  private BigDecimal consistenzaPotenz;
  
  public Long getIdDistretto()
  {
    return idDistretto;
  }
  public void setIdDistretto(Long idDistretto)
  {
    this.idDistretto = idDistretto;
  }
  public Long getIdCensimento()
  {
    return idCensimento;
  }
  public void setIdCensimento(Long idCensimento)
  {
    this.idCensimento = idCensimento;
  }
  public int getAnno()
  {
    if(anno==0)
      return LocalDate.now().getYear()-1;
    return anno;
  }
  public void setAnno(int anno)
  {
    this.anno = anno;
  }
  public BigDecimal getDensitaSupCens()
  {
    return densitaSupCens;
  }
  public void setDensitaSupCens(BigDecimal densitaSupCens)
  {
    this.densitaSupCens = densitaSupCens;
  }
  public BigDecimal getDensitaCapiSus()
  {
    return densitaCapiSus;
  }
  public void setDensitaCapiSus(BigDecimal densitaCapiSus)
  {
    this.densitaCapiSus = densitaCapiSus;
  }
  public BigDecimal getDensitaObiettivo()
  {
    return densitaObiettivo;
  }
  public void setDensitaObiettivo(BigDecimal densitaObiettivo)
  {
    this.densitaObiettivo = densitaObiettivo;
  }
  public BigDecimal getConsistenzaPotenz()
  {
    return consistenzaPotenz;
  }
  public void setConsistenzaPotenz(BigDecimal consistenzaPotenz)
  {
    this.consistenzaPotenz = consistenzaPotenz;
  }
    
  

}
