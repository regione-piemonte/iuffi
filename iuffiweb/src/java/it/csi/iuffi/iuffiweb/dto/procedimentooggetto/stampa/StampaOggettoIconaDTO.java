package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class StampaOggettoIconaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3297599163002020596L;
  protected long            idOggettoIcona;
  protected long            idIconaStampa;
  protected long            extIdTipoDocumento;
  protected int             idElencoCdu;
  protected String          flagStampaOggettoAperto;
  protected String          flagFirmaGrafometrica;
  protected String          flagDaFirmare;
  protected String          flagDaProtocollare;
  protected String          descTipoDocumento;
  protected String          flagInviaMail;
  protected String          flagVisibileTutti;

  public long getIdOggettoIcona()
  {
    return idOggettoIcona;
  }

  public void setIdOggettoIcona(long idOggettoIcona)
  {
    this.idOggettoIcona = idOggettoIcona;
  }

  public long getIdIconaStampa()
  {
    return idIconaStampa;
  }

  public void setIdIconaStampa(long idIconaStampa)
  {
    this.idIconaStampa = idIconaStampa;
  }

  public long getExtIdTipoDocumento()
  {
    return extIdTipoDocumento;
  }

  public void setExtIdTipoDocumento(long extIdTipoDocumento)
  {
    this.extIdTipoDocumento = extIdTipoDocumento;
  }

  public int getIdElencoCdu()
  {
    return idElencoCdu;
  }

  public void setIdElencoCdu(int idElencoCdu)
  {
    this.idElencoCdu = idElencoCdu;
  }

  public String getFlagStampaOggettoAperto()
  {
    return flagStampaOggettoAperto;
  }

  public void setFlagStampaOggettoAperto(String flagStampaOggettoAperto)
  {
    this.flagStampaOggettoAperto = flagStampaOggettoAperto;
  }

  public String getFlagFirmaGrafometrica()
  {
    return flagFirmaGrafometrica;
  }

  public void setFlagFirmaGrafometrica(String flagFirmaGrafometrica)
  {
    this.flagFirmaGrafometrica = flagFirmaGrafometrica;
  }

  public String getDescTipoDocumento()
  {
    return descTipoDocumento;
  }

  public void setDescTipoDocumento(String descTipoDocumento)
  {
    this.descTipoDocumento = descTipoDocumento;
  }

  public String getFlagDaFirmare()
  {
    return flagDaFirmare;
  }

  public void setFlagDaFirmare(String flagDaFirmare)
  {
    this.flagDaFirmare = flagDaFirmare;
  }

  public String getFlagDaProtocollare()
  {
    return flagDaProtocollare;
  }

  public void setFlagDaProtocollare(String flagDaProtocollare)
  {
    this.flagDaProtocollare = flagDaProtocollare;
  }

  public String getFlagInviaMail()
  {
    return flagInviaMail;
  }

  public void setFlagInviaMail(String flagInviaMail)
  {
    this.flagInviaMail = flagInviaMail;
  }

  public String getFlagVisibileTutti()
  {
    return flagVisibileTutti;
  }

  public void setFlagVisibileTutti(String flagVisibileTutti)
  {
    this.flagVisibileTutti = flagVisibileTutti;
  }

}
