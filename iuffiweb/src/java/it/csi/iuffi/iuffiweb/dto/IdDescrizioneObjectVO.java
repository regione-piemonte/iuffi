package it.csi.iuffi.iuffiweb.dto;

import java.io.Serializable;

public class IdDescrizioneObjectVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1177659585397223005L;
  private long              id;
  private String            descrizione;

  public IdDescrizioneObjectVO(long id, String descrizione)
  {
    this.setId(id);
    this.setDescrizione(descrizione);
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getValue()
  {
    return descrizione;
  }

  public void setValue(String descrizione)
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
