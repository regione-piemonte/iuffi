package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public abstract class AbstractRigaJSONCatastoInteventoDTO implements ILoggable
{
  /** serialVersionUID */
  protected static final long serialVersionUID = 5067674552034245663L;
  protected String            istatComune;
  protected String            descComune;
  protected String            sezione;
  protected Integer           foglio;
  protected int               particella;
  protected String            subalterno;
  protected BigDecimal        supCatastale;

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getSezione()
  {
    return sezione;
  }

  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }

  public Integer getFoglio()
  {
    return foglio;
  }

  public void setFoglio(Integer foglio)
  {
    this.foglio = foglio;
  }

  public int getParticella()
  {
    return particella;
  }

  public void setParticella(int particella)
  {
    this.particella = particella;
  }

  public String getSubalterno()
  {
    return subalterno;
  }

  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }

  public String getSupCatastale()
  {
    return IuffiUtils.FORMAT.formatDecimal4(supCatastale);
  }

  public void setSupCatastale(BigDecimal supCatastale)
  {
    this.supCatastale = supCatastale;
  }

  public String getDescComune()
  {
    return descComune;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
}
