package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class VisibilitaDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 7048395105781721238L;

  private long              id;
  private String            descrizione;

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
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
