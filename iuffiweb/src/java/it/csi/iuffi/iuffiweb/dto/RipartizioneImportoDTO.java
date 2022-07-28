package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RipartizioneImportoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -7448530957723985481L;

  private Long              idListaLiquidazione;
  private Long              idImportoLiquidato;
  private Long              idListaLiquidazioneImpLiq;
  private Long              idImportoRipartito;
  private String            voceRipartizione;
  private BigDecimal        percentualeRipartizione;
  private BigDecimal        importoRipartito;

  public Long getIdListaLiquidazione()
  {
    return idListaLiquidazione;
  }

  public void setIdListaLiquidazione(Long idListaLiquidazione)
  {
    this.idListaLiquidazione = idListaLiquidazione;
  }

  public Long getIdImportoLiquidato()
  {
    return idImportoLiquidato;
  }

  public void setIdImportoLiquidato(Long idImportoLiquidato)
  {
    this.idImportoLiquidato = idImportoLiquidato;
  }

  public Long getIdListaLiquidazioneImpLiq()
  {
    return idListaLiquidazioneImpLiq;
  }

  public void setIdListaLiquidazioneImpLiq(Long idListaLiquidazioneImpLiq)
  {
    this.idListaLiquidazioneImpLiq = idListaLiquidazioneImpLiq;
  }

  public Long getIdImportoRipartito()
  {
    return idImportoRipartito;
  }

  public void setIdImportoRipartito(Long idImportoRipartito)
  {
    this.idImportoRipartito = idImportoRipartito;
  }

  public String getVoceRipartizione()
  {
    return voceRipartizione;
  }

  public void setVoceRipartizione(String voceRipartizione)
  {
    this.voceRipartizione = voceRipartizione;
  }

  public BigDecimal getPercentualeRipartizione()
  {
    return percentualeRipartizione;
  }

  public void setPercentualeRipartizione(BigDecimal percentualeRipartizione)
  {
    this.percentualeRipartizione = percentualeRipartizione;
  }

  public BigDecimal getImportoRipartito()
  {
    return importoRipartito;
  }

  public void setImportoRipartito(BigDecimal importoRipartito)
  {
    this.importoRipartito = importoRipartito;
  }

}
