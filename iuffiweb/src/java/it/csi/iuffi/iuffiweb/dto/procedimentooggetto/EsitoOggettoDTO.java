package it.csi.iuffi.iuffiweb.dto.procedimentooggetto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class EsitoOggettoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5916988902454815748L;
  private long              idEsito;
  private String            codice;
  private String            descrizione;
  private boolean           selected         = false;

  @Override
  public boolean equals(Object other)
  {
    if (other == null)
      return false;
    if (other == this)
      return true;
    if (!(other instanceof EsitoOggettoDTO))
      return false;
    EsitoOggettoDTO otherMyClass = (EsitoOggettoDTO) other;

    if (otherMyClass.getIdEsito() == idEsito)
      return true;
    else
      return false;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected(boolean selected)
  {
    this.selected = selected;
  }

}
