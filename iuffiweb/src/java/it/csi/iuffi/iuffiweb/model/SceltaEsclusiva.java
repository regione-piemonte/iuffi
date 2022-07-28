package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SceltaEsclusiva implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 3696378108137287896L;
  
  private String id;
  private String descrizione;
  
  public String getId()
  {
    return id;
  }
  public void setId(String id)
  {
    this.id = id;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
 
  
}
