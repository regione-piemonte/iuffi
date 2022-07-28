package it.csi.iuffi.iuffiweb.dto.datibilancio;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DatiBilancioDTO implements ILoggable
{
  
  private Long idProcedimentoOggetto;
  private Long       extIdZonaAltimetrica;
  private String     descZonaAltimetrica;
  private String     flagRitenAcconto;
  private String     motivazioneRitAcconto;
  private String     tipoDomanda;
  private BigDecimal impCapitaleAnticAmmiss;
  private BigDecimal impCreditoClienti;
  private BigDecimal impFatturato;
  private Long       tempoEsposizDaBilancio;
  private Long       tempoEsposizBenef;
  private Date       dataDeliberaIntervento;
  private Date       dataScadPrestitoPrec;
  private Date       dataUltimoBilancio;
  private BigDecimal importoMateriePrime;
  private BigDecimal importoServizi;
  private BigDecimal importoBeniTerzi;
  private BigDecimal importoPersSalari;
  private BigDecimal importoPersOneri;
  private BigDecimal importoTotConcedibile;

  public String getDescZonaAltimetrica()
  {
    return descZonaAltimetrica;
  }

  public void setDescZonaAltimetrica(String descZonaAltimetrica)
  {
    this.descZonaAltimetrica = descZonaAltimetrica;
  }

  public Date getDataScadPrestitoPrec()
  {
    return dataScadPrestitoPrec;
  }

  public void setDataScadPrestitoPrec(Date dataScadPrestitoPrec)
  {
    this.dataScadPrestitoPrec = dataScadPrestitoPrec;
  }

  public String getFlagRitenAcconto()
  {
    return flagRitenAcconto;
  }

  public void setFlagRitenAcconto(String flagRitenAcconto)
  {
    this.flagRitenAcconto = flagRitenAcconto;
  }

  public String getMotivazioneRitAcconto()
  {
    return motivazioneRitAcconto;
  }

  public void setMotivazioneRitAcconto(String motivazioneRitAcconto)
  {
    this.motivazioneRitAcconto = motivazioneRitAcconto;
  }

  public String getTipoDomanda()
  {
    return tipoDomanda;
  }

  public void setTipoDomanda(String tipoDomanda)
  {
    this.tipoDomanda = tipoDomanda;
  }

  public Date getDataUltimoBilancio()
  {
    return dataUltimoBilancio;
  }

  public void setDataUltimoBilancio(Date dataUltimoBilancio)
  {
    this.dataUltimoBilancio = dataUltimoBilancio;
  }

  public BigDecimal getImpCapitaleAnticAmmiss()
  {
    return impCapitaleAnticAmmiss;
  }

  public void setImpCapitaleAnticAmmiss(BigDecimal impCapitaleAnticAmmiss)
  {
    this.impCapitaleAnticAmmiss = impCapitaleAnticAmmiss;
  }

  public BigDecimal getImpCreditoClienti()
  {
    return impCreditoClienti;
  }

  public void setImpCreditoClienti(BigDecimal impCreditoClienti)
  {
    this.impCreditoClienti = impCreditoClienti;
  }

  public BigDecimal getImpFatturato()
  {
    return impFatturato;
  }

  public void setImpFatturato(BigDecimal impFatturato)
  {
    this.impFatturato = impFatturato;
  }

  public Long getTempoEsposizDaBilancio()
  {
    return tempoEsposizDaBilancio;
  }

  public void setTempoEsposizDaBilancio(Long tempoEsposizDaBilancio)
  {
    this.tempoEsposizDaBilancio = tempoEsposizDaBilancio;
  }

  public Long getTempoEsposizBenef()
  {
    return tempoEsposizBenef;
  }

  public void setTempoEsposizBenef(Long tempoEsposizBenef)
  {
    this.tempoEsposizBenef = tempoEsposizBenef;
  }

  public Date getDataDeliberaIntervento()
  {
    return dataDeliberaIntervento;
  }

  public void setDataDeliberaIntervento(Date dataDeliberaIntervento)
  {
    this.dataDeliberaIntervento = dataDeliberaIntervento;
  }

  public Long getExtIdZonaAltimetrica()
  {
    return extIdZonaAltimetrica;
  }

  public void setExtIdZonaAltimetrica(Long extIdZonaAltimetrica)
  {
    this.extIdZonaAltimetrica = extIdZonaAltimetrica;
  }

  public Long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(Long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }
  
  public BigDecimal getImportoMateriePrime()
  {
    return importoMateriePrime;
  }

  public void setImportoMateriePrime(BigDecimal importoMateriePrime)
  {
    this.importoMateriePrime = importoMateriePrime;
  }

  public BigDecimal getImportoServizi()
  {
    return importoServizi;
  }

  public void setImportoServizi(BigDecimal importoServizi)
  {
    this.importoServizi = importoServizi;
  }

  public BigDecimal getImportoBeniTerzi()
  {
    return importoBeniTerzi;
  }

  public void setImportoBeniTerzi(BigDecimal importoBeniTerzi)
  {
    this.importoBeniTerzi = importoBeniTerzi;
  }

  public BigDecimal getImportoPersSalari()
  {
    return importoPersSalari;
  }

  public void setImportoPersSalari(BigDecimal importoPersSalari)
  {
    this.importoPersSalari = importoPersSalari;
  }

  public BigDecimal getImportoPersOneri()
  {
    return importoPersOneri;
  }

  public void setImportoPersOneri(BigDecimal importoPersOneri)
  {
    this.importoPersOneri = importoPersOneri;
  }

  public BigDecimal getImportoTotConcedibile()
  {
    return importoTotConcedibile;
  }

  public void setImportoTotConcedibile(BigDecimal importoTotConcedibile)
  {
    this.importoTotConcedibile = importoTotConcedibile;
  }

  public String getDataDeliberaInterventoStr()
  {
    return IuffiUtils.DATE.formatDate(dataDeliberaIntervento);
  }

  public String getDataScadPrestitoPrecStr()
  {
    return IuffiUtils.DATE.formatDate(dataScadPrestitoPrec);
  }

  public String getDataUltimoBilancioStr()
  {
    return IuffiUtils.DATE.formatDate(dataUltimoBilancio);
  }
  
  public String getFlagRitenAccontoDecode(){
    if("S".equals(flagRitenAcconto)) {
      return "Sì";
    }
    return "No";
  }

  public String getTipoDomandaDecode(){
    String decode = "";
    if("N".equals(tipoDomanda)) {
      decode = "Nuova domanda";
    }else if("R".equals(tipoDomanda)) {
      decode = "Rinnovo domanda";
    }
    return decode;
  }

}
