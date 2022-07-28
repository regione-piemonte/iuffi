package it.csi.iuffi.iuffiweb.model.api;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TipoArea implements ILoggable
{

  private static final long serialVersionUID = 7283933391064037130L;
  
  private String descTipoArea;
  private String codiceUfficiale;
  private String codiceAppInCampo;
  private List<DettaglioTipoArea> dettaglioTipoAree;
  
  
  public TipoArea()
  {

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

  public List<DettaglioTipoArea> getDettaglioTipoAree()
  {
    return dettaglioTipoAree;
  }

  public void setDettaglioTipoAree(List<DettaglioTipoArea> dettaglioTipoAree)
  {
    this.dettaglioTipoAree = dettaglioTipoAree;
  }

}
