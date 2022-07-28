package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

public class DatiLocalizzazioneParticellarePerStampa
    extends RigaJSONConduzioneInteventoDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3923777807286264417L;
  protected String          descIntervento;
  protected Long            progressivo;
  protected BigDecimal      superficieEffettiva;
  protected BigDecimal      superficieIstruttoria;
  protected BigDecimal      superficieAccertataGis;

  public String getDescIntervento()
  {
    return descIntervento;
  }

  public void setDescIntervento(String descIntervento)
  {
    this.descIntervento = descIntervento;
  }

  public Long getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(Long progressivo)
  {
    this.progressivo = progressivo;
  }

  public BigDecimal getSuperficieEffettiva()
  {
    return superficieEffettiva;
  }

  public void setSuperficieEffettiva(BigDecimal superficieEffettiva)
  {
    this.superficieEffettiva = superficieEffettiva;
  }

  public BigDecimal getSuperficieIstruttoria()
  {
    return superficieIstruttoria;
  }

  public void setSuperficieIstruttoria(BigDecimal superficieIstruttoria)
  {
    this.superficieIstruttoria = superficieIstruttoria;
  }

  public BigDecimal getSuperficieAccertataGis()
  {
    return superficieAccertataGis;
  }

  public void setSuperficieAccertataGis(BigDecimal superficieAccertataGis)
  {
    this.superficieAccertataGis = superficieAccertataGis;
  }
}
