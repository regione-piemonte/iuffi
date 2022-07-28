package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class InfoMisurazioneIntervento implements ILoggable
{
  /** serialVersionUID */
  private static final long  serialVersionUID           = -5738112803240625833L;
  public static final String CODICE_NESSUNA_MISURAZIONE = "NO_MISURA";
  private String             descMisurazione;
  private BigDecimal         valore;
  private String             codiceUnitaMisura;

  public String getDescMisurazione()
  {
    return descMisurazione;
  }

  public void setDescMisurazione(String descMisurazione)
  {
    this.descMisurazione = descMisurazione;
  }

  public BigDecimal getValore()
  {
    return valore;
  }

  public void setValore(BigDecimal valore)
  {
    this.valore = valore;
  }

  public String getCodiceUnitaMisura()
  {
    return codiceUnitaMisura;
  }

  public void setCodiceUnitaMisura(String codiceUnitaMisura)
  {
    this.codiceUnitaMisura = codiceUnitaMisura;
  }

  public boolean isMisuraVisibile()
  {
    return !CODICE_NESSUNA_MISURAZIONE.equals(codiceUnitaMisura);
  }
}
