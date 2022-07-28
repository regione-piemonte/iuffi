package it.csi.iuffi.iuffiweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class OrganismoNocivoDTO extends TabelleStoricizzateDTO implements ILoggable
{

  private static final long serialVersionUID = 1L;
  private Integer idOrganismoNocivo;
  private String nomeLatino;
  private String sigla;
  private String euro;
  private String flagEmergenza;

  @JsonIgnore
  private String flagPianificazione;

  @JsonIgnore
  private String flagArchiviato; 
  @JsonIgnore
  private String presenza;
  
  
  public OrganismoNocivoDTO()
  {
    super();
  }

  public OrganismoNocivoDTO(Integer idOrganismoNocivo)
  {
    super();
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  public OrganismoNocivoDTO(Integer idOrganismoNocivo, String sigla)
  {
    super();
    this.idOrganismoNocivo = idOrganismoNocivo;
    this.sigla = sigla;
  }

  public String getPresenza()
  {
    return presenza;
  }

  public void setPresenza(String presenza)
  {
    this.presenza = presenza;
  }

  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  public String getNomeLatino()
  {
    return nomeLatino;
  }

  public void setNomeLatino(String nomeLatino)
  {
    this.nomeLatino = nomeLatino;
  }

  public String getSigla()
  {
    return sigla;
  }

  public void setSigla(String sigla)
  {
    this.sigla = sigla;
  }

  public String getEuro()
  {
    return euro;
  }

  public void setEuro(String euro)
  {
    this.euro = euro;
  }

  public String getFlagEmergenza()
  {
    return flagEmergenza;
  }

  public void setFlagEmergenza(String flagEmergenza)
  {
    this.flagEmergenza = flagEmergenza;
  }

  public String getNomeCompleto() {
    return nomeLatino + " (" + sigla + ")";
  }

  public String getFlagPianificazione()
  {
    return flagPianificazione;
  }

  public void setFlagPianificazione(String flagPianificazione)
  {
    this.flagPianificazione = flagPianificazione;
  }

}
