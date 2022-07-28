package it.csi.iuffi.iuffiweb.dto.danniFauna;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class AccertamentoDannoDTO implements ILoggable
{

  private long idAccertamentoDanno;
  private String sopralluogo;
  private Date dataSopralluogo;
  private String perito;
  private String numeroPerizia;
  private BigDecimal importoTotaleAccertato;
  private BigDecimal importoRipristino;
  private BigDecimal spesePerizia;
  private BigDecimal spesePrevenzione;
  private String descrizionePrevenzione;
  private String reiteratiDanni;
  private String esitoDomanda;
  private String note;
  
  public long getIdAccertamentoDanno()
  {
    return idAccertamentoDanno;
  }
  public void setIdAccertamentoDanno(long idAccertamentoDanno)
  {
    this.idAccertamentoDanno = idAccertamentoDanno;
  }
  public String getSopralluogo()
  {
    return sopralluogo;
  }
  public void setSopralluogo(String sopralluogo)
  {
    this.sopralluogo = sopralluogo;
  }
  public Date getDataSopralluogo()
  {
    return dataSopralluogo;
  }
  public void setDataSopralluogo(Date dataSopralluogo)
  {
    this.dataSopralluogo = dataSopralluogo;
  }
  public String getPerito()
  {
    return perito;
  }
  public void setPerito(String perito)
  {
    this.perito = perito;
  }
  public String getNumeroPerizia()
  {
    return numeroPerizia;
  }
  public void setNumeroPerizia(String numeroPerizia)
  {
    this.numeroPerizia = numeroPerizia;
  }
  public BigDecimal getImportoTotaleAccertato()
  {
    return importoTotaleAccertato;
  }
  public void setImportoTotaleAccertato(BigDecimal importoTotaleAccertato)
  {
    this.importoTotaleAccertato = importoTotaleAccertato;
  }
  public BigDecimal getImportoRipristino()
  {
    return importoRipristino;
  }
  public void setImportoRipristino(BigDecimal importoRipristino)
  {
    this.importoRipristino = importoRipristino;
  }
  public BigDecimal getSpesePerizia()
  {
    return spesePerizia;
  }
  public void setSpesePerizia(BigDecimal spesePerizia)
  {
    this.spesePerizia = spesePerizia;
  }
  public BigDecimal getSpesePrevenzione()
  {
    return spesePrevenzione;
  }
  public void setSpesePrevenzione(BigDecimal spesePrevenzione)
  {
    this.spesePrevenzione = spesePrevenzione;
  }
  public String getDescrizionePrevenzione()
  {
    return descrizionePrevenzione;
  }
  public void setDescrizionePrevenzione(String descrizionePrevenzione)
  {
    this.descrizionePrevenzione = descrizionePrevenzione;
  }
  public String getReiteratiDanni()
  {
    return reiteratiDanni;
  }
  public void setReiteratiDanni(String reiteratiDanni)
  {
    this.reiteratiDanni = reiteratiDanni;
  }
  public String getEsitoDomanda()
  {
    return esitoDomanda;
  }
  public void setEsitoDomanda(String esitoDomanda)
  {
    this.esitoDomanda = esitoDomanda;
  }
  
  public String getDecodeSopralluogo() {
    return "S".equalsIgnoreCase(this.sopralluogo) ? "Sì" : "No";
  }

  public String getDecodeReiteratiDanni() {
    return "S".equalsIgnoreCase(this.reiteratiDanni) ? "Sì" : "No";
  }

  public String getDecodeEsitoDomanda() {
    return "S".equalsIgnoreCase(this.esitoDomanda) ? "Positivo" : "Negativo";
  }
  
  public String getDataSopralluogoFormatted() {
    return IuffiUtils.DATE.formatDate(this.dataSopralluogo);
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  
}
