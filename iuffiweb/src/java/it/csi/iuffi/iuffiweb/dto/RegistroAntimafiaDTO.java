package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RegistroAntimafiaDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -1617181316838536333L;

  // Dati generali
  private String            cuaa;
  private String            denominazione;
  private String            indirizzoSedeLegale;

  // Titolare rappresentante legale
  private String            codiceFiscale;
  private String            cognome;
  private String            nome;
  private String            telefono;
  private String            email;

  // Certificato antimafica
  private String            statoCertificato;
  private String            prefetturaRiferimento;
  private Date              dataRichiesta;
  private Date              dataScadenza;

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public String getIndirizzoSedeLegale()
  {
    return indirizzoSedeLegale;
  }

  public void setIndirizzoSedeLegale(String indirizzoSedeLegale)
  {
    this.indirizzoSedeLegale = indirizzoSedeLegale;
  }

  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
  }

  public String getCognome()
  {
    return cognome;
  }

  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }

  public String getNome()
  {
    return nome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String getTelefono()
  {
    return telefono;
  }

  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getStatoCertificato()
  {
    return statoCertificato;
  }

  public void setStatoCertificato(String statoCertificato)
  {
    this.statoCertificato = statoCertificato;
  }

  public String getPrefetturaRiferimento()
  {
    return prefetturaRiferimento;
  }

  public void setPrefetturaRiferimento(String prefetturaRiferimento)
  {
    this.prefetturaRiferimento = prefetturaRiferimento;
  }

  public Date getDataRichiesta()
  {
    return dataRichiesta;
  }

  public void setDataRichiesta(Date dataRichiesta)
  {
    this.dataRichiesta = dataRichiesta;
  }

  public Date getDataScadenza()
  {
    return dataScadenza;
  }

  public void setDataScadenza(Date dataScadenza)
  {
    this.dataScadenza = dataScadenza;
  }

  public String getDataRichiestaStr()
  {
    return IuffiUtils.DATE.formatDate(dataRichiesta);
  }

  public String getDataScadenzaStr()
  {
    return IuffiUtils.DATE.formatDate(dataScadenza);
  }
}
