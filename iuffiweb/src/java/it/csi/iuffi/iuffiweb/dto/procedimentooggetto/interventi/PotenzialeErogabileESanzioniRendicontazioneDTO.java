package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PotenzialeErogabileESanzioniRendicontazioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -8718565412483714473L;
  protected BigDecimal      potenzialeErogabile;
  protected BigDecimal      totaleSanzioni;

  public BigDecimal getPotenzialeErogabile()
  {
    return potenzialeErogabile;
  }

  public void setPotenzialeErogabile(BigDecimal potenzialeErogabile)
  {
    this.potenzialeErogabile = potenzialeErogabile;
  }

  public BigDecimal getTotaleSanzioni()
  {
    return totaleSanzioni;
  }

  public void setTotaleSanzioni(BigDecimal totaleSanzioni)
  {
    this.totaleSanzioni = totaleSanzioni;
  }
}
