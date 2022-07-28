package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.conticorrenti;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ContoCorrenteDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idContoCorrente;
  private String            denominazioneBanca;
  private String            denominazioneSportello;
  private String            indirizzoSportello;
  private String            intestazione;
  private String            iban;

  public long getIdContoCorrente()
  {
    return idContoCorrente;
  }

  public void setIdContoCorrente(long idContoCorrente)
  {
    this.idContoCorrente = idContoCorrente;
  }

  public String getDenominazioneBanca()
  {
    return denominazioneBanca;
  }

  public void setDenominazioneBanca(String denominazioneBanca)
  {
    this.denominazioneBanca = denominazioneBanca;
  }

  public String getDenominazioneSportello()
  {
    return denominazioneSportello;
  }

  public void setDenominazioneSportello(String denominazioneSportello)
  {
    this.denominazioneSportello = denominazioneSportello;
  }

  public String getIndirizzoSportello()
  {
    return indirizzoSportello;
  }

  public void setIndirizzoSportello(String indirizzoSportello)
  {
    this.indirizzoSportello = indirizzoSportello;
  }

  public String getIntestazione()
  {
    return intestazione;
  }

  public void setIntestazione(String intestazione)
  {
    this.intestazione = intestazione;
  }

  public String getIban()
  {
    return iban;
  }

  public void setIban(String iban)
  {
    this.iban = iban;
  }

}
