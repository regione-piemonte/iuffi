package it.csi.iuffi.iuffiweb.model;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PrevisioneMonitoraggioDTO implements ILoggable
{

  private static final long serialVersionUID = 1L;
  
  private Integer idOrganismoNocivo;
  private String nomeLatino;
  private String sigla;
  private BigDecimal oreVisualReg;
  private Integer numVisualReg;
  private BigDecimal oreCampioniReg;
  private Integer numCampioniReg;
  private Integer numAnalisiReg;
  private BigDecimal oreTrappoleReg;
  private Integer numTrappoleReg;
  private BigDecimal oreVisualEst;
  private Integer numVisualEst;
  private BigDecimal oreCampioniEst;
  private Integer numCampioniEst;
  private Integer numAnalisiEst;
  private BigDecimal oreTrappoleEst;
  private Integer numTrappoleEst;
  private BigDecimal oreVisualAvReg;
  private Integer numVisualAvReg;
  private BigDecimal oreCampioniAvReg;
  private Integer numCampioniAvReg;
  private Integer numAnalisiAvReg;
  private BigDecimal oreTrappoleAvReg;
  private Integer numTrappoleAvReg;
  private BigDecimal oreVisualAvEst;
  private Integer numVisualAvEst;
  private BigDecimal oreCampioniAvEst;
  private Integer numCampioniAvEst;
  private Integer numAnalisiAvEst;
  private BigDecimal oreTrappoleAvEst;
  private Integer numTrappoleAvEst;
  private Integer idPianoMonitoraggio;
  
  public PrevisioneMonitoraggioDTO()
  {
    super();
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  public String getNomeLatino()
  {
    return nomeLatino;
  }

  public void setNomeLatino(String nomeLatino)
  {
    this.nomeLatino = nomeLatino;
  }

  public Integer getNumCampioniReg()
  {
    return numCampioniReg;
  }

  public void setNumCampioniReg(Integer numCampioniReg)
  {
    this.numCampioniReg = numCampioniReg;
  }

  public Integer getNumTrappoleReg()
  {
    return numTrappoleReg;
  }

  public void setNumTrappoleReg(Integer numTrappoleReg)
  {
    this.numTrappoleReg = numTrappoleReg;
  }

  public Integer getNumCampioniEst()
  {
    return numCampioniEst;
  }

  public void setNumCampioniEst(Integer numCampioniEst)
  {
    this.numCampioniEst = numCampioniEst;
  }

  public Integer getNumTrappoleEst()
  {
    return numTrappoleEst;
  }

  public void setNumTrappoleEst(Integer numTrappoleEst)
  {
    this.numTrappoleEst = numTrappoleEst;
  }

  public Integer getNumAnalisiReg()
  {
    return numAnalisiReg;
  }

  public void setNumAnalisiReg(Integer numAnalisiReg)
  {
    this.numAnalisiReg = numAnalisiReg;
  }

  public BigDecimal getOreVisualEst()
  {
    return oreVisualEst;
  }

  public void setOreVisualEst(BigDecimal oreVisualEst)
  {
    this.oreVisualEst = oreVisualEst;
  }

  public String getSigla()
  {
    return sigla;
  }

  public void setSigla(String sigla)
  {
    this.sigla = sigla;
  }

  public Integer getIdPianoMonitoraggio()
  {
    return idPianoMonitoraggio;
  }

  public void setIdPianoMonitoraggio(Integer idPianoMonitoraggio)
  {
    this.idPianoMonitoraggio = idPianoMonitoraggio;
  }

  public BigDecimal getOreVisualReg()
  {
    return oreVisualReg;
  }

  public void setOreVisualReg(BigDecimal oreVisualReg)
  {
    this.oreVisualReg = oreVisualReg;
  }

  public Integer getNumVisualReg()
  {
    return numVisualReg;
  }

  public void setNumVisualReg(Integer numVisualReg)
  {
    this.numVisualReg = numVisualReg;
  }

  public BigDecimal getOreCampioniReg()
  {
    return oreCampioniReg;
  }

  public void setOreCampioniReg(BigDecimal oreCampioniReg)
  {
    this.oreCampioniReg = oreCampioniReg;
  }

  public BigDecimal getOreTrappoleReg()
  {
    return oreTrappoleReg;
  }

  public void setOreTrappoleReg(BigDecimal oreTrappoleReg)
  {
    this.oreTrappoleReg = oreTrappoleReg;
  }

  public Integer getNumVisualEst()
  {
    return numVisualEst;
  }

  public void setNumVisualEst(Integer numVisualEst)
  {
    this.numVisualEst = numVisualEst;
  }

  public BigDecimal getOreCampioniEst()
  {
    return oreCampioniEst;
  }

  public void setOreCampioniEst(BigDecimal oreCampioniEst)
  {
    this.oreCampioniEst = oreCampioniEst;
  }

  public BigDecimal getOreTrappoleEst()
  {
    return oreTrappoleEst;
  }

  public void setOreTrappoleEst(BigDecimal oreTrappoleEst)
  {
    this.oreTrappoleEst = oreTrappoleEst;
  }

  public Integer getNumAnalisiEst()
  {
    return numAnalisiEst;
  }

  public void setNumAnalisiEst(Integer numAnalisiEst)
  {
    this.numAnalisiEst = numAnalisiEst;
  }

  public BigDecimal getOreVisualAvReg()
  {
    return oreVisualAvReg;
  }

  public void setOreVisualAvReg(BigDecimal oreVisualAvReg)
  {
    this.oreVisualAvReg = oreVisualAvReg;
  }

  public Integer getNumVisualAvReg()
  {
    return numVisualAvReg;
  }

  public void setNumVisualAvReg(Integer numVisualAvReg)
  {
    this.numVisualAvReg = numVisualAvReg;
  }

  public BigDecimal getOreCampioniAvReg()
  {
    return oreCampioniAvReg;
  }

  public void setOreCampioniAvReg(BigDecimal oreCampioniAvReg)
  {
    this.oreCampioniAvReg = oreCampioniAvReg;
  }

  public Integer getNumCampioniAvReg()
  {
    return numCampioniAvReg;
  }

  public void setNumCampioniAvReg(Integer numCampioniAvReg)
  {
    this.numCampioniAvReg = numCampioniAvReg;
  }

  public Integer getNumAnalisiAvReg()
  {
    return numAnalisiAvReg;
  }

  public void setNumAnalisiAvReg(Integer numAnalisiAvReg)
  {
    this.numAnalisiAvReg = numAnalisiAvReg;
  }

  public BigDecimal getOreTrappoleAvReg()
  {
    return oreTrappoleAvReg;
  }

  public void setOreTrappoleAvReg(BigDecimal oreTrappoleAvReg)
  {
    this.oreTrappoleAvReg = oreTrappoleAvReg;
  }

  public Integer getNumTrappoleAvReg()
  {
    return numTrappoleAvReg;
  }

  public void setNumTrappoleAvReg(Integer numTrappoleAvReg)
  {
    this.numTrappoleAvReg = numTrappoleAvReg;
  }

  public BigDecimal getOreVisualAvEst()
  {
    return oreVisualAvEst;
  }

  public void setOreVisualAvEst(BigDecimal oreVisualAvEst)
  {
    this.oreVisualAvEst = oreVisualAvEst;
  }

  public Integer getNumVisualAvEst()
  {
    return numVisualAvEst;
  }

  public void setNumVisualAvEst(Integer numVisualAvEst)
  {
    this.numVisualAvEst = numVisualAvEst;
  }

  public BigDecimal getOreCampioniAvEst()
  {
    return oreCampioniAvEst;
  }

  public void setOreCampioniAvEst(BigDecimal oreCampioniAvEst)
  {
    this.oreCampioniAvEst = oreCampioniAvEst;
  }

  public Integer getNumCampioniAvEst()
  {
    return numCampioniAvEst;
  }

  public void setNumCampioniAvEst(Integer numCampioniAvEst)
  {
    this.numCampioniAvEst = numCampioniAvEst;
  }

  public Integer getNumAnalisiAvEst()
  {
    return numAnalisiAvEst;
  }

  public void setNumAnalisiAvEst(Integer numAnalisiAvEst)
  {
    this.numAnalisiAvEst = numAnalisiAvEst;
  }

  public BigDecimal getOreTrappoleAvEst()
  {
    return oreTrappoleAvEst;
  }

  public void setOreTrappoleAvEst(BigDecimal oreTrappoleAvEst)
  {
    this.oreTrappoleAvEst = oreTrappoleAvEst;
  }

  public Integer getNumTrappoleAvEst()
  {
    return numTrappoleAvEst;
  }

  public void setNumTrappoleAvEst(Integer numTrappoleAvEst)
  {
    this.numTrappoleAvEst = numTrappoleAvEst;
  }

  public String getNomeCompleto() {
    return nomeLatino + " (" + sigla + ")";
  }

}
