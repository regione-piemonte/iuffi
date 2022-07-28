package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class EsitoVO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 6035364196001956453L;

  private long              idEsito;
  private String            esito;

  public EsitoVO()
  {
  }

  public String getEsito()
  {
    return esito;
  }

  public void setEsito(String esito)
  {
    this.esito = esito;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }

}
