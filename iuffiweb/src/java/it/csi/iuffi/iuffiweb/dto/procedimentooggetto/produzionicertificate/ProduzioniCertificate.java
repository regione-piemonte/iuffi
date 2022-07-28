package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate;


import java.util.List;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProduzioniCertificate implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */
  
  private long id;
  private long idprod; 
  private long idprodcert;
  private String descprod; 
  private String descprodcert;
  private String descqualita;
  private String bio;
  private List<DecodificaDTO<Integer>> produzioniCertificate;
  
  public long getIdprod()
  {
    return idprod;
  }
  public void setIdprod(long idprod)
  {
    this.idprod = idprod;
  }
  public long getIdprodcert()
  {
    return idprodcert;
  }
  public void setIdprodcert(long idprodcert)
  {
    this.idprodcert = idprodcert;
  }
  public String getDescprod()
  {
    return descprod;
  }
  public void setDescprod(String descprod)
  {
    this.descprod = descprod;
  }
  public String getDescprodcert()
  {
    return descprodcert;
  }
  public void setDescprodcert(String descprodcert)
  {
    this.descprodcert = descprodcert;
  }
  public String getBio()
  {
    return bio;
  }
  public void setBio(String bio)
  {
    this.bio = bio;
  }
  public String getDescqualita()
  {
    return descqualita;
  }
  public void setDescqualita(String descqualita)
  {
    this.descqualita = descqualita;
  }
  public List<DecodificaDTO<Integer>> getProduzioniCertificate()
  {
    return produzioniCertificate;
  }
  public void setProduzioniCertificate(List<DecodificaDTO<Integer>> produzioniCertificate)
  {
    this.produzioniCertificate = produzioniCertificate;
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
