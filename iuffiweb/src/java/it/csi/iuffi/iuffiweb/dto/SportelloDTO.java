package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SportelloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long id;
  private String abi;
  private String cab;
  private String indirizzo;
  private String provincia;
  private String comune;
  private String denominazione;
  private String cap;
  public long getId()
  {
    return id;
  }
  public void setId(long id)
  {
    this.id = id;
  }
  public String getAbi()
  {
    return abi;
  }
  public void setAbi(String abi)
  {
    this.abi = abi;
  }
  public String getCab()
  {
    return cab;
  }
  public void setCab(String cab)
  {
    this.cab = cab;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getProvincia()
  {
    return provincia;
  }
  public void setProvincia(String provincia)
  {
    this.provincia = provincia;
  }
  public String getComune()
  {
    return comune;
  }
  public void setComune(String comune)
  {
    this.comune = comune;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getCap()
  {
    return cap;
  }
  public void setCap(String cap)
  {
    this.cap = cap;
  }
  
  
 

}
