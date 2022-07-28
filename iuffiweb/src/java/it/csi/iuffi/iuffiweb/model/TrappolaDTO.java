package it.csi.iuffi.iuffiweb.model;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class TrappolaDTO extends TabelleDTO implements ILoggable
{

  private static final long serialVersionUID = 1663396135549755015L;

  private Integer idTrappola;
  private Integer idTipoTrappola;
  private Double latitudine;
  private Double longitudine;
  private Integer idSpecieVeg;
  private Date dataInstallazione;
  private Date dataRimozione;
  private String codiceSfr;
  
  public TrappolaDTO()
  {
    super();
  }

  public TrappolaDTO(Integer idTrappola)
  {
    super();
    this.idTrappola = idTrappola;
  }

  public Integer getIdTrappola()
  {
    return idTrappola;
  }

  public void setIdTrappola(Integer idTrappola)
  {
    this.idTrappola = idTrappola;
  }

  public Integer getIdTipoTrappola()
  {
    return idTipoTrappola;
  }

  public void setIdTipoTrappola(Integer idTipoTrappola)
  {
    this.idTipoTrappola = idTipoTrappola;
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

  public Integer getIdSpecieVeg()
  {
    return idSpecieVeg;
  }

  public void setIdSpecieVeg(Integer idSpecieVeg)
  {
    this.idSpecieVeg = idSpecieVeg;
  }

  public Date getDataInstallazione()
  {
    return dataInstallazione;
  }

  public void setDataInstallazione(Date dataInstallazione)
  {
    this.dataInstallazione = dataInstallazione;
  }

  public String getDataInstallazioneF()
  {
    return IuffiUtils.DATE.formatDate(dataInstallazione);
  }
  
  public Date getDataRimozione()
  {
    return dataRimozione;
  }

  public void setDataRimozione(Date dataRimozione)
  {
    this.dataRimozione = dataRimozione;
  }

  public String getDataRimozioneF()
  {
    return IuffiUtils.DATE.formatDate(dataRimozione);
  }

  public String getCodiceSfr()
  {
    return codiceSfr;
  }

  public void setCodiceSfr(String codiceSfr)
  {
    this.codiceSfr = codiceSfr;
  }

}
