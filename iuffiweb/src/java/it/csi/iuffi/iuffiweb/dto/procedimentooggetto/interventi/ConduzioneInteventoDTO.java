package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ConduzioneInteventoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5067674552034245663L;
  private long              idConduzioneDichiarata;
  private long              idUtilizzoDichiarato;
  private String            istatComune;
  private String            sezione;
  private Integer           foglio;
  private int               particella;
  private String            subalterno;
  private BigDecimal        supCatastale;
  private String            descTipoUtilizzo;
  private BigDecimal        superficieUtilizzata;

  public long getIdConduzioneDichiarata()
  {
    return idConduzioneDichiarata;
  }

  public void setIdConduzioneDichiarata(long idConduzioneDichiarata)
  {
    this.idConduzioneDichiarata = idConduzioneDichiarata;
  }

  public long getIdUtilizzoDichiarato()
  {
    return idUtilizzoDichiarato;
  }

  public void setIdUtilizzoDichiarato(long idUtilizzoDichiarato)
  {
    this.idUtilizzoDichiarato = idUtilizzoDichiarato;
  }

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

  public BigDecimal getSupCatastale()
  {
    return supCatastale;
  }

  public void setSupCatastale(BigDecimal supCatastale)
  {
    this.supCatastale = supCatastale;
  }

  public String getDescTipoUtilizzo()
  {
    return descTipoUtilizzo;
  }

  public void setDescTipoUtilizzo(String descTipoUtilizzo)
  {
    this.descTipoUtilizzo = descTipoUtilizzo;
  }

  public BigDecimal getSuperficieUtilizzata()
  {
    return superficieUtilizzata;
  }

  public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
  {
    this.superficieUtilizzata = superficieUtilizzata;
  }
}
