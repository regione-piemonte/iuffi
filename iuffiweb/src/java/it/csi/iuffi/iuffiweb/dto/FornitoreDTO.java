package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class FornitoreDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 6035364196001956453L;

  private long              idFornitore;
  private String            codiceFornitore;
  private String            ragioneSociale;
  private String            indirizzoSedeLegale;
  private String            extIstatComuneSedeLegale;

  public long getIdFornitore()
  {
    return idFornitore;
  }

  public void setIdFornitore(long idFornitore)
  {
    this.idFornitore = idFornitore;
  }

  public String getRagioneSociale()
  {
    return ragioneSociale;
  }

  public void setRagioneSociale(String ragioneSociale)
  {
    this.ragioneSociale = ragioneSociale;
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

  public String getCodiceFornitore()
  {
    return codiceFornitore;
  }

  public void setCodiceFornitore(String codiceFornitore)
  {
    this.codiceFornitore = codiceFornitore;
  }

}
