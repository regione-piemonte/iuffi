package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DistrettoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -1500274836005348033L;

  private Long idDistretto;
  private Long idOgur;
  private String nominDistretto;
  private BigDecimal superficieDistretto;
  private BigDecimal superfVenabDistretto;
  private BigDecimal sus;
  private List<AnnoCensitoDTO> anniCensiti; 
  private CensimentoDTO censimento;
  private List<IpotesiPrelievoDTO> ipotesiPrelievo;
  
  public Long getIdDistretto()
  {
    return idDistretto;
  }
  public void setIdDistretto(Long idDistretto)
  {
    this.idDistretto = idDistretto;
  }
  public Long getIdOgur()
  {
    return idOgur;
  }
  public void setIdOgur(Long idOgur)
  {
    this.idOgur = idOgur;
  }
  public String getNominDistretto()
  {
    return nominDistretto;
  }
  public void setNominDistretto(String nominDistretto)
  {
    this.nominDistretto = nominDistretto;
  }
  public BigDecimal getSuperficieDistretto()
  {
    return superficieDistretto;
  }
  public void setSuperficieDistretto(BigDecimal superficieDistretto)
  {
    this.superficieDistretto = superficieDistretto;
  }
  public BigDecimal getSuperfVenabDistretto()
  {
    return superfVenabDistretto;
  }
  public void setSuperfVenabDistretto(BigDecimal superfVenabDistretto)
  {
    this.superfVenabDistretto = superfVenabDistretto;
  }
  public BigDecimal getSus()
  {
    return sus;
  }
  public void setSus(BigDecimal sus)
  {
    this.sus = sus;
  }
  public List<AnnoCensitoDTO> getAnniCensiti()
  {
    if(anniCensiti==null || anniCensiti.isEmpty())
    {
      anniCensiti = new ArrayList<>();
      AnnoCensitoDTO anno = new AnnoCensitoDTO();
      int currentYear = LocalDate.now().getYear();
      anno.setAnno(currentYear-5);
      anniCensiti.add(anno);
      
      AnnoCensitoDTO anno1 = new AnnoCensitoDTO();
      anno1.setAnno(currentYear-4);
      anniCensiti.add(anno1);
      
      AnnoCensitoDTO anno2 = new AnnoCensitoDTO();
      anno2.setAnno(currentYear-3);
      anniCensiti.add(anno2);
      
      AnnoCensitoDTO anno3 = new AnnoCensitoDTO();
      anno3.setAnno(currentYear-2);
      anniCensiti.add(anno3);
      
      AnnoCensitoDTO anno4 = new AnnoCensitoDTO();
      anno4.setAnno(currentYear-1);
      anniCensiti.add(anno4);
    }
    
    return anniCensiti;
  }
  public void setAnniCensiti(List<AnnoCensitoDTO> anniCensiti)
  {
    this.anniCensiti = anniCensiti;
  }
  public CensimentoDTO getCensimento()
  {
    if(censimento == null)
      censimento = new CensimentoDTO();
    
    return censimento;
  }
  public void setCensimento(CensimentoDTO censimento)
  {
    this.censimento = censimento;
  }
  public List<IpotesiPrelievoDTO> getIpotesiPrelievo()
  {
    if(ipotesiPrelievo==null || ipotesiPrelievo.isEmpty())
    {
      ipotesiPrelievo = new ArrayList<>();
      IpotesiPrelievoDTO anno = new IpotesiPrelievoDTO();
      int currentYear = LocalDate.now().getYear();
      anno.setAnno(currentYear);
      ipotesiPrelievo.add(anno);
      
      IpotesiPrelievoDTO anno2 = new IpotesiPrelievoDTO();
      anno2.setAnno(currentYear+1);
      ipotesiPrelievo.add(anno2);
      
      IpotesiPrelievoDTO anno3 = new IpotesiPrelievoDTO();
      anno3.setAnno(currentYear+2);
      ipotesiPrelievo.add(anno3);
      
      IpotesiPrelievoDTO anno4 = new IpotesiPrelievoDTO();
      anno4.setAnno(currentYear+3);
      ipotesiPrelievo.add(anno4);
      
      IpotesiPrelievoDTO anno5 = new IpotesiPrelievoDTO();
      anno5.setAnno(currentYear+4);
      ipotesiPrelievo.add(anno5);
    }
    return ipotesiPrelievo;
  }
  public void setIpotesiPrelievo(List<IpotesiPrelievoDTO> ipotesiPrelievo)
  {
    this.ipotesiPrelievo = ipotesiPrelievo;
  }
  
  public AnnoCensitoDTO getAnnoCensito(int anno){
    for(AnnoCensitoDTO a : getAnniCensiti())
    {
      if(a.getAnno()==anno)
        return a;
    }
    return null;
  }
  
  public IpotesiPrelievoDTO getIpotesiPrelievo(int anno){
    for(IpotesiPrelievoDTO a : getIpotesiPrelievo())
    {
      if(a.getAnno()==anno)
        return a;
    }
    return null;
  }
  
  public void setIpotesiPrelievo(int anno, BigDecimal percentuale){
    for(IpotesiPrelievoDTO a : getIpotesiPrelievo())
    {
      if(a.getAnno()==anno)
        a.setPercentuale(percentuale);
    }
  }
  
  
  

}
