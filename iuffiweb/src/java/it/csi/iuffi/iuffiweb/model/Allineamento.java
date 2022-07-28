package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class Allineamento implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 3696378108137287896L;
  
  private String lastTimestamp;

  public Allineamento(String lastTimestamp)
  {
    this.lastTimestamp = lastTimestamp;
  }

  public String getLastTimestamp()
  {
    return lastTimestamp;
  }

  public void setLastTimestamp(String lastTimestamp)
  {
    this.lastTimestamp = lastTimestamp;
  }
  
  
}
