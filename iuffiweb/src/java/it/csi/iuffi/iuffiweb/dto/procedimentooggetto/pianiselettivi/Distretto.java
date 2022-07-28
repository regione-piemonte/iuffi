package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi;


import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class Distretto implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */
  
  private Long idDistretto;
  private Long idPianoDistrettoOgur;
  private String nominDistretto;
  private BigDecimal superficieDistretto;
  private BigDecimal sus;
  private String tipo;
  private String totaleCensito;
  private String totalePrelievo;
  private String et1;
  private String et2;
  private String et3;
  private String et4;
  private String et5;
  private String et6;
  private BigDecimal valoreda1;
  private BigDecimal valoreda2;
  private BigDecimal valoreda3;
  private BigDecimal valoreda4;
  private BigDecimal valoreda5;
  private BigDecimal valoreda6;
  private BigDecimal valorea1;
  private BigDecimal valorea2;
  private BigDecimal valorea3;
  private BigDecimal valorea4;
  private BigDecimal valorea5;
  private BigDecimal valorea6;
  private String censito1;
  private String censito2;
  private String censito3;
  private String censito4;
  private String censito5;
  private String indeterminatiCensito;
  private BigDecimal percentuale;
  private String maxCapiPrelievo;
  private String indeterminatiPrelievo;
  private String prelievo1;
  private String prelievo2;
  private String prelievo3;
  private String prelievo4;
  private String prelievo5;
  private String prelievo6;
  private BigDecimal percentualeTotale;
  private BigDecimal perc1;
  private BigDecimal perc2;
  private BigDecimal perc3;
  private BigDecimal perc4;
  private BigDecimal perc5;
  private BigDecimal perc6;
  private String esitoTotalePrelievo;
  private String esito1;
  private String esito2;
  private String esito3;
  private String esito4;
  private String esito5;
  private String esito6;
  
  public String getEt6()
  {
    return et6;
  }
  public void setEt6(String et6)
  {
    this.et6 = et6;
  }
  public BigDecimal getValoreda6()
  {
    return valoreda6;
  }
  public void setValoreda6(BigDecimal valoreda6)
  {
    this.valoreda6 = valoreda6;
  }
  public BigDecimal getValorea6()
  {
    return valorea6;
  }
  public void setValorea6(BigDecimal valorea6)
  {
    this.valorea6 = valorea6;
  }
  public String getPrelievo6()
  {
    return prelievo6;
  }
  public void setPrelievo6(String prelievo6)
  {
    this.prelievo6 = prelievo6;
  }
  public BigDecimal getPerc6()
  {
    return perc6;
  }
  public void setPerc6(BigDecimal perc6)
  {
    this.perc6 = perc6;
  }
  public String getEsito6()
  {
    return esito6;
  }
  public void setEsito6(String esito6)
  {
    this.esito6 = esito6;
  }
  public String getEt5()
  {
    return et5;
  }
  public void setEt5(String et5)
  {
    this.et5 = et5;
  }
  public BigDecimal getValoreda5()
  {
    return valoreda5;
  }
  public void setValoreda5(BigDecimal valoreda5)
  {
    this.valoreda5 = valoreda5;
  }
  public BigDecimal getValorea5()
  {
    return valorea5;
  }
  public void setValorea5(BigDecimal valorea5)
  {
    this.valorea5 = valorea5;
  }
  public String getCensito5()
  {
    return censito5;
  }
  public void setCensito5(String censito5)
  {
    this.censito5 = censito5;
  }
  public String getPrelievo5()
  {
    return prelievo5;
  }
  public void setPrelievo5(String prelievo5)
  {
    this.prelievo5 = prelievo5;
  }
  public BigDecimal getPerc5()
  {
    return perc5;
  }
  public void setPerc5(BigDecimal perc5)
  {
    if(perc5 == null) {
      this.perc5 = new BigDecimal("0.00");
    }
    else {
      this.perc5 = perc5.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
  }
  public String getEsito5()
  {
    return esito5;
  }
  public void setEsito5(String esito5)
  {
    this.esito5 = esito5;
  }
  public String getEsito1()
  {
    return esito1;
  }
  public void setEsito1(String esito1)
  {
    this.esito1 = esito1;
  }
  public String getEsito2()
  {
    return esito2;
  }
  public void setEsito2(String esito2)
  {
    this.esito2 = esito2;
  }
  public String getEsito3()
  {
    return esito3;
  }
  public void setEsito3(String esito3)
  {
    this.esito3 = esito3;
  }
  public String getEsito4()
  {
    return esito4;
  }
  public void setEsito4(String esito4)
  {
    this.esito4 = esito4;
  }
  public BigDecimal getPerc1()
  {
    return perc1;
  }
  public void setPerc1(BigDecimal perc1)
  {
    if(perc1 == null) {
      this.perc1 = new BigDecimal("0.00");
    }
    else {
      this.perc1 = perc1.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
  }
  public BigDecimal getPerc2()
  {
    return perc2;
  }
  public void setPerc2(BigDecimal perc2)
  {
    if(perc2 == null) {
      this.perc2 = new BigDecimal("0.00");
    }
    else {
      this.perc2 = perc2.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
  }
  public BigDecimal getPerc3()
  {
    return perc3;
  }
  public void setPerc3(BigDecimal perc3)
  {
    if(perc3 == null) {
      this.perc3 = new BigDecimal("0.00");
    }
    else {
      this.perc3 = perc3.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
  }
  public BigDecimal getPerc4()
  {
    return perc4;
  }
  public void setPerc4(BigDecimal perc4)
  {
    if(perc4 == null) {
      this.perc4 = new BigDecimal("0.00");
    }
    else {
      this.perc4 = perc4.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
    
  }
  public String getPrelievo1()
  {
    return prelievo1;
  }
  public void setPrelievo1(String prelievo1)
  {
    this.prelievo1 = prelievo1;
  }
  public String getPrelievo2()
  {
    return prelievo2;
  }
  public void setPrelievo2(String prelievo2)
  {
    this.prelievo2 = prelievo2;
  }
  public String getPrelievo3()
  {
    return prelievo3;
  }
  public void setPrelievo3(String prelievo3)
  {
    this.prelievo3 = prelievo3;
  }
  public String getPrelievo4()
  {
    return prelievo4;
  }
  public void setPrelievo4(String prelievo4)
  {
    this.prelievo4 = prelievo4;
  }
  public Long getIdDistretto()
  {
    return idDistretto;
  }
  public void setIdDistretto(Long idDistretto)
  {
    this.idDistretto = idDistretto;
  }
  public String getNominDistretto()
  {
    return nominDistretto;
  }
  public void setNominDistretto(String nominDistretto)
  {
    this.nominDistretto = nominDistretto;
  }
  public BigDecimal getSuperficieDistretto()
  {
    return superficieDistretto;
  }
  public void setSuperficieDistretto(BigDecimal superficieDistretto)
  {
    this.superficieDistretto = superficieDistretto;
  }
  public BigDecimal getSus()
  {
    return sus;
  }
  public void setSus(BigDecimal sus)
  {
    this.sus = sus;
  }
  public String getTipo()
  {
    return tipo;
  }
  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }
  public String getTotaleCensito()
  {
    return totaleCensito;
  }
  public void setTotaleCensito(String totaleCensito)
  {
    this.totaleCensito = totaleCensito;
  }
  public String getEt1()
  {
    return et1;
  }
  public void setEt1(String et1)
  {
    this.et1 = et1;
  }
  public String getEt2()
  {
    return et2;
  }
  public void setEt2(String et2)
  {
    this.et2 = et2;
  }
  public String getEt3()
  {
    return et3;
  }
  public void setEt3(String et3)
  {
    this.et3 = et3;
  }
  public String getEt4()
  {
    return et4;
  }
  public void setEt4(String et4)
  {
    this.et4 = et4;
  }
  public BigDecimal getValoreda1()
  {
    return valoreda1;
  }
  public void setValoreda1(BigDecimal valoreda1)
  {
    this.valoreda1 = valoreda1;
  }
  public BigDecimal getValoreda2()
  {
    return valoreda2;
  }
  public void setValoreda2(BigDecimal valoreda2)
  {
    this.valoreda2 = valoreda2;
  }
  public BigDecimal getValoreda3()
  {
    return valoreda3;
  }
  public void setValoreda3(BigDecimal valoreda3)
  {
    this.valoreda3 = valoreda3;
  }
  public BigDecimal getValoreda4()
  {
    return valoreda4;
  }
  public void setValoreda4(BigDecimal valoreda4)
  {
    this.valoreda4 = valoreda4;
  }
  public BigDecimal getValorea1()
  {
    return valorea1;
  }
  public void setValorea1(BigDecimal valorea1)
  {
    this.valorea1 = valorea1;
  }
  public BigDecimal getValorea2()
  {
    return valorea2;
  }
  public void setValorea2(BigDecimal valorea2)
  {
    this.valorea2 = valorea2;
  }
  public BigDecimal getValorea3()
  {
    return valorea3;
  }
  public void setValorea3(BigDecimal valorea3)
  {
    this.valorea3 = valorea3;
  }
  public BigDecimal getValorea4()
  {
    return valorea4;
  }
  public void setValorea4(BigDecimal valorea4)
  {
    this.valorea4 = valorea4;
  }
  public String getCensito1()
  {
    return censito1;
  }
  public void setCensito1(String censito1)
  {
    this.censito1 = censito1;
  }
  public String getCensito2()
  {
    return censito2;
  }
  public void setCensito2(String censito2)
  {
    this.censito2 = censito2;
  }
  public String getCensito3()
  {
    return censito3;
  }
  public void setCensito3(String censito3)
  {
    this.censito3 = censito3;
  }
  public String getCensito4()
  {
    return censito4;
  }
  public void setCensito4(String censito4)
  {
    this.censito4 = censito4;
  }
  public String getIndeterminatiCensito()
  {
    return indeterminatiCensito;
  }
  public void setIndeterminatiCensito(String indeterminatiCensito)
  {
    this.indeterminatiCensito = indeterminatiCensito;
  }
  public BigDecimal getPercentuale()
  {
    return percentuale;
  }
  public void setPercentuale(BigDecimal percentuale)
  {
    this.percentuale = percentuale.setScale(2,BigDecimal.ROUND_HALF_UP);
  }
  public String getMaxCapiPrelievo()
  {
    return maxCapiPrelievo;
  }
  public void setMaxCapiPrelievo(String maxCapiPrelievo)
  {
    this.maxCapiPrelievo = maxCapiPrelievo;
  }
  public String getTotalePrelievo()
  {
    return totalePrelievo;
  }
  public void setTotalePrelievo(String totalePrelievo)
  {
    this.totalePrelievo = totalePrelievo;
  }
  public String getIndeterminatiPrelievo()
  {
    return indeterminatiPrelievo;
  }
  public void setIndeterminatiPrelievo(String indeterminatiPrelievo)
  {
    this.indeterminatiPrelievo = indeterminatiPrelievo;
  }
  public BigDecimal getPercentualeTotale()
  {
    return percentualeTotale;
  }
  public void setPercentualeTotale(BigDecimal percentualeTotale)
  {
    this.percentualeTotale = percentualeTotale.setScale(2,BigDecimal.ROUND_HALF_UP);
  }
  public Long getIdPianoDistrettoOgur()
  {
    return idPianoDistrettoOgur;
  }
  public void setIdPianoDistrettoOgur(Long idPianoDistrettoOgur)
  {
    this.idPianoDistrettoOgur = idPianoDistrettoOgur;
  }
  public String getEsitoTotalePrelievo()
  {
    return esitoTotalePrelievo;
  }
  public void setEsitoTotalePrelievo(String esitoTotalePrelievo)
  {
    this.esitoTotalePrelievo = esitoTotalePrelievo;
  }
  
  
}