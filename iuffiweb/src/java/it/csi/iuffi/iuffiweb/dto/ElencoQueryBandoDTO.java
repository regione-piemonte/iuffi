package it.csi.iuffi.iuffiweb.dto;

import java.io.Serializable;

public class ElencoQueryBandoDTO implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1971848488455853056L;

  private Long              idElencoQuery;
  private String            infoAggiuntive;
  private String            descrizione;
  private String            tipologiaReport;
  private Long              idBando;

  public String getInfoAggiuntive()
  {
    return infoAggiuntive;
  }

  public void setInfoAggiuntive(String infoAggiuntive)
  {
    this.infoAggiuntive = infoAggiuntive;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getTipologiaReport()
  {
    return tipologiaReport;
  }

  public void setTipologiaReport(String tipologiaReport)
  {
    this.tipologiaReport = tipologiaReport;
  }

  public Long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(Long idBando)
  {
    this.idBando = idBando;
  }

  public Long getIdElencoQuery()
  {
    return idElencoQuery;
  }

  public void setIdElencoQuery(Long idElencoQuery)
  {
    this.idElencoQuery = idElencoQuery;
  }

}
