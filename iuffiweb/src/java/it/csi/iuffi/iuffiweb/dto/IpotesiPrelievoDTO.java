package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class IpotesiPrelievoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 9106852619328954139L;
  private Long idDistretto;
  private Long idIpotesiPrelievo;
  private int anno;
  private BigDecimal percentuale;
  
  public Long getIdDistretto()
  {
    return idDistretto;
  }
  public void setIdDistretto(Long idDistretto)
  {
    this.idDistretto = idDistretto;
  }
  public Long getIdIpotesiPrelievo()
  {
    return idIpotesiPrelievo;
  }
  public void setIdIpotesiPrelievo(Long idIpotesiPrelievo)
  {
    this.idIpotesiPrelievo = idIpotesiPrelievo;
  }
  public int getAnno()
  {
    return anno;
  }
  public void setAnno(int anno)
  {
    this.anno = anno;
  }
  public BigDecimal getPercentuale()
  {
    return percentuale;
  }
  public void setPercentuale(BigDecimal percentuale)
  {
    this.percentuale = percentuale;
  }

}