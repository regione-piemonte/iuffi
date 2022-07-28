package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AnnoCensitoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -3318714042817971833L;
  private Long idDistretto;
  private Long idAnniCensiti;
  private int anno;
  private BigDecimal totCensito;
  private BigDecimal superfCensita;
  private BigDecimal pianoNumerico;
  private BigDecimal totPrelevato;
  private BigDecimal danniCausati = BigDecimal.ZERO;
  private BigDecimal incidentiStradali  = BigDecimal.ZERO;
  
  public Long getIdDistretto()
  {
    return idDistretto;
  }
  public void setIdDistretto(Long idDistretto)
  {
    this.idDistretto = idDistretto;
  }
  public Long getIdAnniCensiti()
  {
    return idAnniCensiti;
  }
  public void setIdAnniCensiti(Long idAnniCensiti)
  {
    this.idAnniCensiti = idAnniCensiti;
  }
  public int getAnno()
  {
    return anno;
  }
  public void setAnno(int anno)
  {
    this.anno = anno;
  }
  public BigDecimal getTotCensito()
  {
    return totCensito;
  }
  public void setTotCensito(BigDecimal totCensito)
  {
    this.totCensito = totCensito;
  }
  public BigDecimal getSuperfCensita()
  {
    return superfCensita;
  }
  public void setSuperfCensita(BigDecimal superfCensita)
  {
    this.superfCensita = superfCensita;
  }
  public BigDecimal getPianoNumerico()
  {
    return pianoNumerico;
  }
  public void setPianoNumerico(BigDecimal pianoNumerico)
  {
    this.pianoNumerico = pianoNumerico;
  }
  public BigDecimal getTotPrelevato()
  {
    return totPrelevato;
  }
  public void setTotPrelevato(BigDecimal totPrelevato)
  {
    this.totPrelevato = totPrelevato;
  }
  public BigDecimal getDanniCausati()
  {
    return danniCausati;
  }
  public void setDanniCausati(BigDecimal danniCausati)
  {
    this.danniCausati = danniCausati;
  }
  public BigDecimal getIncidentiStradali()
  {
    return incidentiStradali;
  }
  public void setIncidentiStradali(BigDecimal incidentiStradali)
  {
    this.incidentiStradali = incidentiStradali;
  }

  

}
