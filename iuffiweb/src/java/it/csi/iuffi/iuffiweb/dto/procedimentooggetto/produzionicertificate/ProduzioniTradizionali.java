package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate;


import java.util.List;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProduzioniTradizionali implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private long id; 
  private long idprod; 
  private long idprodtrad;
  private String descprod; 
  private String descprodtrad;
  private String bio;
  private List<DecodificaDTO<Integer>> produzioniTradizionali;
  
  public long getIdprod()
  {
    return idprod;
  }
  public void setIdprod(long idprod)
  {
    this.idprod = idprod;
  }
  public long getIdprodtrad()
  {
    return idprodtrad;
  }
  public void setIdprodtrad(long idprodtrad)
  {
    this.idprodtrad = idprodtrad;
  }
  public String getDescprod()
  {
    return descprod;
  }
  public void setDescprod(String descprod)
  {
    this.descprod = descprod;
  }
  public String getDescprodtrad()
  {
    return descprodtrad;
  }
  public void setDescprodtrad(String descprodtrad)
  {
    this.descprodtrad = descprodtrad;
  }
  public String getBio()
  {
    return bio;
  }
  public void setBio(String bio)
  {
    this.bio = bio;
  }
  public List<DecodificaDTO<Integer>> getProduzioniTradizionali()
  {
    return produzioniTradizionali;
  }
  public void setProduzioniTradizionali(List<DecodificaDTO<Integer>> produzioniTradizionali)
  {
    this.produzioniTradizionali = produzioniTradizionali;
  }
  public long getId()
  {
    return id;
  }
  public void setId(long id)
  {
    this.id = id;
  }
  
  
  
  
}
