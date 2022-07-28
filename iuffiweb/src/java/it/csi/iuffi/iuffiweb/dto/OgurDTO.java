package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class OgurDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -4319665146005652383L;

  private Long idProcedimentoOggetto;
  private Long idOgur;
  private Long progressivo;
  private Long idSpecieOgur;
  private String descrizione;
  private BigDecimal superficieTotaleAtcca;
  private List<DistrettoDTO> distretti; 
  
  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }
  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }
  public Long getIdOgur()
  {
    return idOgur;
  }
  public void setIdOgur(Long idOgur)
  {
    this.idOgur = idOgur;
  }
  public Long getProgressivo()
  {
    return progressivo;
  }
  public void setProgressivo(Long progressivo)
  {
    this.progressivo = progressivo;
  }
  public Long getIdSpecieOgur()
  {
    return idSpecieOgur;
  }
  public void setIdSpecieOgur(Long idSpecieOgur)
  {
    this.idSpecieOgur = idSpecieOgur;
  }
  public BigDecimal getSuperficieTotaleAtcca()
  {
    return superficieTotaleAtcca;
  }
  public void setSuperficieTotaleAtcca(BigDecimal superficieTotaleAtcca)
  {
    this.superficieTotaleAtcca = superficieTotaleAtcca;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public List<DistrettoDTO> getDistretti()
  {
    return distretti;
  }
  public void setDistretti(List<DistrettoDTO> distretti)
  {
    this.distretti = distretti;
  }



}
