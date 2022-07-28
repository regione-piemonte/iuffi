package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

/**
 * @author massimo.durando
 *
 */
public class SuperficieConduzione implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3062868939845015640L;
  protected long            idConduzioneDichiarata;
  protected long            idUtilizzoDichiarato;
  protected long            idDichiarazioneConsistenza;
  protected BigDecimal      superficieImpegno;

  // Particelle storico rif cu 280
  protected BigDecimal      superficiePremio;
  protected Long            annoRif;
  protected BigDecimal      deltaAnnoPrec;

  public BigDecimal getSuperficiePremio()
  {
    return superficiePremio;
  }

  public void setSuperficiePremio(BigDecimal superficiePremio)
  {
    this.superficiePremio = superficiePremio;
  }

  public Long getAnnoRif()
  {
    return annoRif;
  }

  public void setAnnoRif(Long annoRif)
  {
    this.annoRif = annoRif;
  }

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

  public long getIdDichiarazioneConsistenza()
  {
    return idDichiarazioneConsistenza;
  }

  public void setIdDichiarazioneConsistenza(long idDichiarazioneConsistenza)
  {
    this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
  }

  public BigDecimal getSuperficieImpegno()
  {
    return superficieImpegno;
  }

  public void setSuperficieImpegno(BigDecimal superficieImpegno)
  {
    this.superficieImpegno = superficieImpegno;
  }

  public BigDecimal getDeltaAnnoPrec()
  {
    return deltaAnnoPrec;
  }

  public void setDeltaAnnoPrec(BigDecimal deltaAnnoPrec)
  {
    this.deltaAnnoPrec = deltaAnnoPrec;
  }

}
