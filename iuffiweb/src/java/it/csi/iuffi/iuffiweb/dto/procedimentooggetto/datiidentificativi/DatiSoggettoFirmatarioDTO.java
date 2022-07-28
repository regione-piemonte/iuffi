package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DatiSoggettoFirmatarioDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1158027105590788266L;

  private String            descrizioneRuolo;
  private String            codiceFiscale;
  private String            cognome;
  private String            nome;
  private String            sesso;
  private String            indirizzoResidenza;
  private String            comuneCitta;
  private Date              nascitaData;
  private String            telefono;
  private String            mail;

  public String getDescrizioneRuolo()
  {
    return descrizioneRuolo;
  }

  public void setDescrizioneRuolo(String descrizioneRuolo)
  {
    this.descrizioneRuolo = descrizioneRuolo;
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

  public String getSesso()
  {
    return sesso;
  }

  public void setSesso(String sesso)
  {
    this.sesso = sesso;
  }

  public String getIndirizzoResidenza()
  {
    return indirizzoResidenza;
  }

  public void setIndirizzoResidenza(String indirizzoResidenza)
  {
    this.indirizzoResidenza = indirizzoResidenza;
  }

  public String getComuneCitta()
  {
    return comuneCitta;
  }

  public void setComuneCitta(String comuneCitta)
  {
    this.comuneCitta = comuneCitta;
  }

  public Date getNascitaData()
  {
    return nascitaData;
  }

  public void setNascitaData(Date nascitaData)
  {
    this.nascitaData = nascitaData;
  }

  public String getTelefono()
  {
    return telefono;
  }

  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }

  public String getMail()
  {
    return mail;
  }

  public void setMail(String mail)
  {
    this.mail = mail;
  }

}
