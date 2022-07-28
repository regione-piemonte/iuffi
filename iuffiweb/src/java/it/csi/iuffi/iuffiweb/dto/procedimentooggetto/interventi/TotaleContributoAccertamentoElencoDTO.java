package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TotaleContributoAccertamentoElencoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5559346638673433120L;
  protected BigDecimal      contributoErogabile;
  protected BigDecimal      compensazioneArrotondamenti;
  protected BigDecimal      contributoNonErogabile;
  protected BigDecimal      importoSanzioni;
  protected String          codiceOperazione;

  public BigDecimal getContributoErogabile()
  {
    return contributoErogabile;
  }

  public void setContributoErogabile(BigDecimal contributoErogabile)
  {
    this.contributoErogabile = contributoErogabile;
  }

  public BigDecimal getContributoNonErogabile()
  {
    return contributoNonErogabile;
  }

  public void setContributoNonErogabile(BigDecimal contributoNonErogabile)
  {
    this.contributoNonErogabile = contributoNonErogabile;
  }

  public BigDecimal getImportoSanzioni()
  {
    return importoSanzioni;
  }

  public void setImportoSanzioni(BigDecimal importoSanzioni)
  {
    this.importoSanzioni = importoSanzioni;
  }

  public String getCodiceOperazione()
  {
    return codiceOperazione;
  }

  public void setCodiceOperazione(String codiceOperazione)
  {
    this.codiceOperazione = codiceOperazione;
  }

  public BigDecimal getCompensazioneArrotondamenti()
  {
    return compensazioneArrotondamenti;
  }

  public void setCompensazioneArrotondamenti(
      BigDecimal compensazioneArrotondamenti)
  {
    this.compensazioneArrotondamenti = compensazioneArrotondamenti;
  }
}
