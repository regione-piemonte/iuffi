package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

public class RigaJSONRendicontazioneSuperficiDTO
    extends RigaJSONConduzioneInteventoDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4679855804265127828L;
  protected String          superficieEffettiva;
  protected String          superficieIstruttoria;
  protected BigDecimal      superficieAccertataGis;

  public String getSuperficieEffettiva()
  {
    return superficieEffettiva;
  }

  public void setSuperficieEffettiva(String superficieEffettiva)
  {
    this.superficieEffettiva = superficieEffettiva;
  }

  public String getSuperficieIstruttoria()
  {
    return superficieIstruttoria;
  }

  public void setSuperficieIstruttoria(String superficieIstruttoria)
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
