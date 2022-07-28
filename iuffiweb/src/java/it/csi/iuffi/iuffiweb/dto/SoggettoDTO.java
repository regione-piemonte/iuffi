package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SoggettoDTO implements ILoggable
{

  /**
   * Serial ID
   */
  private static final long serialVersionUID = -402533523053198681L;
    

  private Long idAnagrafePesca;
  private String flagCittadinoEstero;
  private String nome;
  private String cognome;
  private String codiceFiscale;
  private String email;
  private String siglaProvinciaResidenza;
  
  
  
  public Long getIdAnagrafePesca()
  {
    return idAnagrafePesca;
  }

  public void setIdAnagrafePesca(Long idAnagrafePesca)
  {
    this.idAnagrafePesca = idAnagrafePesca;
  }

  public String getTipoCittadinanzaDescr()
  {
    if(flagCittadinoEstero==null)
      return null;
    return flagCittadinoEstero.equals("S") ? "Estera" : "Italiana";
  }
  
  public String getFlagCittadinoEstero()
  {
    return flagCittadinoEstero;
  }
  public void setFlagCittadinoEstero(String flagCittadinoEstero)
  {
    this.flagCittadinoEstero = flagCittadinoEstero;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  public String getNome()
  {
    return nome;
  }
  public void setNome(String nome)
  {
    this.nome = nome;
  }
  public String getCognome()
  {
    return cognome;
  }
  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }
  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
  }
  public String getSiglaProvinciaResidenza()
  {
    return siglaProvinciaResidenza;
  }
  public void setSiglaProvinciaResidenza(String siglaProvinciaResidenza)
  {
    this.siglaProvinciaResidenza = siglaProvinciaResidenza;
  }
}
