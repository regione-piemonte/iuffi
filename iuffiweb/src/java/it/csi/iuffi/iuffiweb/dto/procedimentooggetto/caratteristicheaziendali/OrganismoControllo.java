package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class OrganismoControllo implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private String codueodc;
  private String descodc;
  public String getCodueodc()
  {
    return codueodc;
  }
  public void setCodueodc(String codueodc)
  {
    this.codueodc = codueodc;
  }
  public String getDescodc()
  {
    return descodc;
  }
  public void setDescodc(String descodc)
  {
    this.descodc = descodc;
  }
  
}
