package it.csi.iuffi.iuffiweb.model.api;

import java.io.Serializable;

public class Coordinate implements Serializable
{

  private Double latitudine;
  private Double longitudine;
  
  
  public Coordinate()
  {
    super();
  }
  public Coordinate(Double latitudine, Double longitudine)
  {
    super();
    this.latitudine = latitudine;
    this.longitudine = longitudine;
  }
  
  public Double getLatitudine()
  {
    return latitudine;
  }
  public void setLatitudine(Double latitudine)
  {
    this.latitudine = latitudine;
  }
  public Double getLongitudine()
  {
    return longitudine;
  }
  public void setLongitudine(Double longitudine)
  {
    this.longitudine = longitudine;
  }

}
