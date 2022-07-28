package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.partecipanti;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PartecipanteDTO implements ILoggable
{

  private static final long serialVersionUID = 4005650835165074626L;

  private Long              idPartecipante;
  private Long              idDatiProcedimento;
  private Long              extIdAzienda;
  private String            cuaa;
  private String            denominazione;
  private String            indirizzoSedeLegale;
  private String            extIstatComuneSedeLegale;
  private String            extIstatProvincia;
  private String            capSedeLegale;
  private String            mail;
  private String            comuneSedeLegale;
  private boolean           isDatiAnagrafe   = false;
  private boolean           isDatiTributaria = false;

  public Long getIdPartecipante()
  {
    return idPartecipante;
  }

  public void setIdPartecipante(long idPartecipante)
  {
    this.idPartecipante = idPartecipante;
  }

  public Long getIdDatiProcedimento()
  {
    return idDatiProcedimento;
  }

  public void setIdDatiProcedimento(long idDatiProcedimento)
  {
    this.idDatiProcedimento = idDatiProcedimento;
  }

  public Long getExtIdAzienda()
  {
    return extIdAzienda;
  }

  public void setExtIdAzienda(Long extIdAzienda)
  {
    this.extIdAzienda = extIdAzienda;
  }

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

  public String getExtIstatComuneSedeLegale()
  {
    return extIstatComuneSedeLegale;
  }

  public void setExtIstatComuneSedeLegale(String extIstatComuneSedeLegale)
  {
    this.extIstatComuneSedeLegale = extIstatComuneSedeLegale;
  }

  public String getCapSedeLegale()
  {
    return capSedeLegale;
  }

  public void setCapSedeLegale(String capSedeLegale)
  {
    this.capSedeLegale = capSedeLegale;
  }

  public String getMail()
  {
    return mail;
  }

  public void setMail(String mail)
  {
    this.mail = mail;
  }

  public String getComuneSedeLegale()
  {
    return comuneSedeLegale;
  }

  public void setComuneSedeLegale(String comuneSedeLegale)
  {
    this.comuneSedeLegale = comuneSedeLegale;
  }

  public boolean isDatiAnagrafe()
  {
    return isDatiAnagrafe;
  }

  public void setDatiAnagrafe(boolean isDatiAnagrafe)
  {
    this.isDatiAnagrafe = isDatiAnagrafe;
  }

  public boolean isDatiTributaria()
  {
    return isDatiTributaria;
  }

  public void setDatiTributaria(boolean isDatiTributaria)
  {
    this.isDatiTributaria = isDatiTributaria;
  }

  public String getExtIstatProvincia()
  {
    return extIstatProvincia;
  }

  public void setExtIstatProvincia(String extIstatProvincia)
  {
    this.extIstatProvincia = extIstatProvincia;
  }

}
