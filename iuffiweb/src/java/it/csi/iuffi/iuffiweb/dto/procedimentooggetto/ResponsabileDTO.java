package it.csi.iuffi.iuffiweb.dto.procedimentooggetto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ResponsabileDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5394637679244549075L;

  private String            descrizione;
  private String            codiceFiscale;

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
  }

}
