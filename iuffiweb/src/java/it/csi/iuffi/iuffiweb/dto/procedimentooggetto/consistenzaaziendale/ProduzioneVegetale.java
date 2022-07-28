package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProduzioneVegetale implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private String comune;
  private String sez;
  private String fg;
  private String part;
  private String sub;
  private String usodelsuolo;
  private String produzionebiologica;
  private String pubblica;
  private long idconduzione;
  private long idutilizzodichirato;
  private long idutilizzo;
  private long id;
  
  
  public long getIdconduzione()
  {
    return idconduzione;
  }
  public void setIdconduzione(long idconduzione)
  {
    this.idconduzione = idconduzione;
  }
  public long getIdutilizzodichirato()
  {
    return idutilizzodichirato;
  }
  public void setIdutilizzodichirato(long idutilizzodichirato)
  {
    this.idutilizzodichirato = idutilizzodichirato;
  }
  public long getIdutilizzo()
  {
    return idutilizzo;
  }
  public void setIdutilizzo(long idutilizzo)
  {
    this.idutilizzo = idutilizzo;
  }
  public String getComune()
  {
    return comune;
  }
  public void setComune(String comune)
  {
    this.comune = comune;
  }
  public String getSez()
  {
    return sez;
  }
  public void setSez(String sez)
  {
    this.sez = sez;
  }
  public String getFg()
  {
    return fg;
  }
  public void setFg(String fg)
  {
    this.fg = fg;
  }
  public String getPart()
  {
    return part;
  }
  public void setPart(String part)
  {
    this.part = part;
  }
  public String getSub()
  {
    return sub;
  }
  public void setSub(String sub)
  {
    this.sub = sub;
  }
  
  public String getUsodelsuolo()
  {
    return usodelsuolo;
  }
  public void setUsodelsuolo(String usodelsuolo)
  {
    this.usodelsuolo = usodelsuolo;
  }
  public String getProduzionebiologica()
  {
    return produzionebiologica;
  }
  public void setProduzionebiologica(String produzionebiologica)
  {
    this.produzionebiologica = produzionebiologica;
  }
  public String getPubblica()
  {
    return pubblica;
  }
  public void setPubblica(String pubblica)
  {
    this.pubblica = pubblica;
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
