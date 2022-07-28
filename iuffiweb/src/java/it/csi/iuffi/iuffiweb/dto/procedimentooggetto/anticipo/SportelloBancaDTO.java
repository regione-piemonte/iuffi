package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SportelloBancaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1355994747893174486L;
  protected int             idSportello;
  protected String          abi;
  protected String          denominazioneBanca;
  protected String          cab;
  protected String          denominazioneSportello;
  protected String          indirizzoSportello;
  protected String          capSportello;
  protected String          descrizioneComuneSportello;
  protected String          siglaProvinciaSportello;

  public int getIdSportello()
  {
    return idSportello;
  }

  public void setIdSportello(int idSportello)
  {
    this.idSportello = idSportello;
  }

  public String getAbi()
  {
    return abi;
  }

  public void setAbi(String abi)
  {
    this.abi = abi;
  }

  public String getDenominazioneBanca()
  {
    return denominazioneBanca;
  }

  public void setDenominazioneBanca(String denominazioneBanca)
  {
    this.denominazioneBanca = denominazioneBanca;
  }

  public String getCab()
  {
    return cab;
  }

  public void setCab(String cab)
  {
    this.cab = cab;
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

  public String getCapSportello()
  {
    return capSportello;
  }

  public void setCapSportello(String capSportello)
  {
    this.capSportello = capSportello;
  }

  public String getDescrizioneComuneSportello()
  {
    return descrizioneComuneSportello;
  }

  public void setDescrizioneComuneSportello(String descrizioneComuneSportello)
  {
    this.descrizioneComuneSportello = descrizioneComuneSportello;
  }

  public String getSiglaProvinciaSportello()
  {
    return siglaProvinciaSportello;
  }

  public void setSiglaProvinciaSportello(String siglaProvinciaSportello)
  {
    this.siglaProvinciaSportello = siglaProvinciaSportello;
  }

}