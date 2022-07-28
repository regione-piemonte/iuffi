package it.csi.iuffi.iuffiweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TipoCampioneDTO extends TabelleStoricizzateDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 3020445815612502349L;
  

  private Integer idTipoCampione;
  private String tipologiaCampione;
  @JsonIgnore
  private String typologyOfSamples;
  
  
  public TipoCampioneDTO()
  {
    super();
  }
  
  @JsonIgnore
  private String flagArchiviato; 
  
  public Integer getIdTipoCampione()
  {
    return idTipoCampione;
  }
  public void setIdTipoCampione(Integer idTipoCampione)
  {
    this.idTipoCampione = idTipoCampione;
  }
  public String getTipologiaCampione()
  {
    return tipologiaCampione;
  }
  public void setTipologiaCampione(String tipologiaCampione)
  {
    this.tipologiaCampione = tipologiaCampione;
  }
  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }
  public String getTypologyOfSamples()
  {
    return typologyOfSamples;
  }
  public void setTypologyOfSamples(String typologyOfSamples)
  {
    this.typologyOfSamples = typologyOfSamples;
  }

  public String getTipoCampioneWithEnglish() {
    return (this.typologyOfSamples!=null)?this.tipologiaCampione + " (" + this.typologyOfSamples + ")" : this.tipologiaCampione;
  }

}
