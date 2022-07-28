package it.csi.iuffi.iuffiweb.model.api;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class Trappola implements ILoggable
{

  private static final long serialVersionUID = 1663396135549755015L;

  private Integer idTrappola;
  private Integer idTipoTrappola;
  private Double latitudine;
  private Double longitudine;
  private Integer idSpecieVeg;
  private String specie;
  private String dataInstallazione;
  private String dataRimozione;
  private String codiceSfr;
  private String descrTrappola;
  private Integer idOrganismoNocivo;
  
  public Trappola()
  {
    super();
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

  public String getDataInstallazione()
  {
    return dataInstallazione;
  }

  public void setDataInstallazione(String dataInstallazione)
  {
    this.dataInstallazione = dataInstallazione;
  }

  public String getDataRimozione()
  {
    return dataRimozione;
  }

  public void setDataRimozione(String dataRimozione)
  {
    this.dataRimozione = dataRimozione;
  }

  public String getCodiceSfr()
  {
    return codiceSfr;
  }

  public void setCodiceSfr(String codiceSfr)
  {
    this.codiceSfr = codiceSfr;
  }

  public String getSpecie()
  {
    return specie;
  }

  public void setSpecie(String specie)
  {
    this.specie = specie;
  }

  public String getDescrTrappola()
  {
    return descrTrappola;
  }

  public void setDescrTrappola(String descrTrappola)
  {
    this.descrTrappola = descrTrappola;
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

}
