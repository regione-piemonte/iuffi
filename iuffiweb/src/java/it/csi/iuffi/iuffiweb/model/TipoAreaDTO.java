package it.csi.iuffi.iuffiweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TipoAreaDTO extends TabelleStoricizzateDTO implements ILoggable
{

  private static final long serialVersionUID = 1L;
  
  private Integer idTipoArea;
  private String descTipoArea;
  private String codiceUfficiale;
  private String codiceAppInCampo;
  private String typologyOfLocation;
  private String dettaglioTipoArea;
  private Integer velocita;
  
  @JsonIgnore
  private String flagArchiviato; 
 
  public TipoAreaDTO()
  {

  }

  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }

  public Integer getIdTipoArea()
  {
    return idTipoArea;
  }

  public void setIdTipoArea(Integer idTipoArea)
  {
    this.idTipoArea = idTipoArea;
  }

  public String getDescTipoArea()
  {
    return descTipoArea;
  }

  public void setDescTipoArea(String descTipoArea)
  {
    this.descTipoArea = descTipoArea;
  }

  public String getCodiceUfficiale()
  {
    return codiceUfficiale;
  }

  public void setCodiceUfficiale(String codiceUfficiale)
  {
    this.codiceUfficiale = codiceUfficiale;
  }

  public String getCodiceAppInCampo()
  {
    return codiceAppInCampo;
  }

  public void setCodiceAppInCampo(String codiceAppInCampo)
  {
    this.codiceAppInCampo = codiceAppInCampo;
  }

  public String getDettaglioTipoArea()
  {
    return dettaglioTipoArea;
  }

  public void setDettaglioTipoArea(String dettaglioTipoArea)
  {
    this.dettaglioTipoArea = dettaglioTipoArea;
  }

  public String getTypologyOfLocation()
  {
    return typologyOfLocation;
  }

  public void setTypologyOfLocation(String typologyOfLocation)
  {
    this.typologyOfLocation = typologyOfLocation;
  }

  public String getTipoAreaEDettaglio() {
    return this.descTipoArea + ((this.dettaglioTipoArea!=null && this.dettaglioTipoArea.trim().length()>0)?" - " + this.dettaglioTipoArea : "");
  }

  public Integer getVelocita()
  {
    return velocita;
  }

  public void setVelocita(Integer velocita)
  {
    this.velocita = velocita;
  }
}
