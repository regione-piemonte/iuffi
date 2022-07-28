package it.csi.iuffi.iuffiweb.dto.estrazionecampione;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class FlagEstrazioneDTO implements ILoggable
{

  private static final long serialVersionUID = 5918223009553272325L;

  private long              idEstrazione;
  private String            descrizione;
  private String            flagControlloInLoco;
  private String            flagEstratta;

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getFlagControlloInLoco()
  {
    return flagControlloInLoco;
  }

  public void setFlagControlloInLoco(String flagControlloInLoco)
  {
    this.flagControlloInLoco = flagControlloInLoco;
  }

  public String getFlagEstratta()
  {
    return flagEstratta;
  }

  public void setFlagEstratta(String flagEstratta)
  {
    this.flagEstratta = flagEstratta;
  }

  public long getIdEstrazione()
  {
    return idEstrazione;
  }

  public void setIdEstrazione(long idEstrazione)
  {
    this.idEstrazione = idEstrazione;
  }

}
