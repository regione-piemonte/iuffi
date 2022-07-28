package it.csi.iuffi.iuffiweb.dto.procedimentooggetto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class IconaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5394637679244549075L;
  private int               idIcona;
  private String            descrizione;
  private String            tooltip;
  private String            nomeIcona;

  public int getIdIcona()
  {
    return idIcona;
  }

  public void setIdIcona(int idIcona)
  {
    this.idIcona = idIcona;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getTooltip()
  {
    return tooltip;
  }

  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }

  public String getNomeIcona()
  {
    return nomeIcona;
  }

  public void setNomeIcona(String nomeIcona)
  {
    this.nomeIcona = nomeIcona;
  }
}
