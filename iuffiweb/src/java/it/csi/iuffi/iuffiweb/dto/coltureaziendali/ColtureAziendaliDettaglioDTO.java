package it.csi.iuffi.iuffiweb.dto.coltureaziendali;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ColtureAziendaliDettaglioDTO extends SuperficiColtureDettaglioDTO implements ILoggable {
    private static final long serialVersionUID = -1132197678230336741L;

    private String bloccante;
    private String siglaProvincia;
    private BigDecimal produzioneTotaleDanno;
    private BigDecimal prezzoDanneggiato;
    private BigDecimal totaleEuroPlvOrd;
    private BigDecimal totaleEuroPlvEff;
    private BigDecimal euroDanno;
    private BigDecimal percentualeDanno;
    private BigDecimal giornateLavorateHa;
    private BigDecimal giornateLavorate;
    private String note;
    private String recordModificato;
    private String flagDanneggiato;
    private BigDecimal importoRimborso;
    private BigDecimal totConRimborsi;
    private Boolean isModificaProduzioneHaDannoDisabled;
    private Boolean isModificaPrezzoDanneggiatoDisabled;

    
    public String getFlagDanneggiato() {
        return flagDanneggiato;
    }

    public void setFlagDanneggiato(String flagDanneggiato) {
        this.flagDanneggiato = flagDanneggiato;
    }

    public BigDecimal getImportoRimborso() {
        return importoRimborso;
    }

    public void setImportoRimborso(BigDecimal importoRimborso) {
        this.importoRimborso = importoRimborso;
    }

    public BigDecimal getTotConRimborsi() {
        return totConRimborsi;
    }

    public void setTotConRimborsi(BigDecimal totConRimborsi) {
        this.totConRimborsi = totConRimborsi;
    }

    public Boolean getIsModificaProduzioneHaDannoDisabled() {
	return isModificaProduzioneHaDannoDisabled;
    }

    public void setIsModificaProduzioneHaDannoDisabled(Boolean isModificaProduzioneHaDannoDisabled) {
	this.isModificaProduzioneHaDannoDisabled = isModificaProduzioneHaDannoDisabled;
    }

    public Boolean getIsModificaPrezzoDanneggiatoDisabled() {
	return isModificaPrezzoDanneggiatoDisabled;
    }

    public void setIsModificaPrezzoDanneggiatoDisabled(Boolean isModificaPrezzoDanneggiatoDisabled) {
	this.isModificaPrezzoDanneggiatoDisabled = isModificaPrezzoDanneggiatoDisabled;
    }

    public BigDecimal getGiornateLavorateHa() {
        return giornateLavorateHa;
    }

    public void setGiornateLavorateHa(BigDecimal giornateLavorateHa) {
        this.giornateLavorateHa = giornateLavorateHa;
    }

    public void setEuroDanno(BigDecimal euroDanno) {
	this.euroDanno = euroDanno;
    }

    public String getBloccante() {
	return bloccante;
    }

    public BigDecimal getGiornateLavorate() {
	return giornateLavorate;
    }

    public void setGiornateLavorate(BigDecimal giornateLavorate) {
	this.giornateLavorate = giornateLavorate;
    }

    public void setBloccante(String bloccante) {
	this.bloccante = bloccante;
    }

    public String getSiglaProvincia() {
	return siglaProvincia;
    }

    public void setSiglaProvincia(String siglaProvincia) {
	this.siglaProvincia = siglaProvincia;
    }

    public BigDecimal getProduzioneTotaleDanno() {
	return produzioneTotaleDanno;
    }

    public String getProduzioneTotaleDannoFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(produzioneTotaleDanno);
    }

    public void setProduzioneTotaleDanno(BigDecimal produzioneTotaleDanno) {
	this.produzioneTotaleDanno = produzioneTotaleDanno;
    }

    public BigDecimal getPrezzoDanneggiato() {
	return prezzoDanneggiato;
    }

    public String getPrezzoDanneggiatoFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(prezzoDanneggiato);
    }

    public BigDecimal getTotaleEuroPlvOrd() {
	return totaleEuroPlvOrd;
    }

    public String getTotaleEuroPlvOrdFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(totaleEuroPlvOrd);
    }

    public String getGiornateLavorateHaFormatted() {
	if (giornateLavorateHa != null) return giornateLavorateHa.toString();
	return null;
    }
    
    public String getGiornateLavorateFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(giornateLavorate);
    }

    public void setTotaleEuroPlvOrd(BigDecimal totaleEuroPlvOrd) {
	this.totaleEuroPlvOrd = totaleEuroPlvOrd;
    }

    public void setPrezzoDanneggiato(BigDecimal prezzoDanneggiato) {
	this.prezzoDanneggiato = prezzoDanneggiato;
    }

    public BigDecimal getTotaleEuroPlvEff() {
	return totaleEuroPlvEff;
    }

    public String getTotaleEuroPlvEffFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(totaleEuroPlvEff);
    }
    
    public String getImportoRimborsoFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(importoRimborso);
    }    
    
    public String getTotConRimborsiFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(totConRimborsi);
    }

    public void setTotaleEuroPlvEff(BigDecimal totaleEuroPlvEff) {
	this.totaleEuroPlvEff = totaleEuroPlvEff;
    }
    
    public BigDecimal getEuroDanno() {
      if (this.getFlagDanneggiato() == null || this.getFlagDanneggiato().equalsIgnoreCase("N")) return null;
      if (this.getTotaleEuroPlvEff() == null) return null;
	return euroDanno;
    }

    public String getEuroDannoFormatted() {
	if (this.getFlagDanneggiato() == null || this.getFlagDanneggiato().equalsIgnoreCase("N")) return null;
	if (this.getTotaleEuroPlvEff() == null) return null;
	return IuffiUtils.FORMAT.formatDecimal2(euroDanno);
    }

    public BigDecimal getPercentualeDanno() {
      if (this.getFlagDanneggiato() == null || this.getFlagDanneggiato().equalsIgnoreCase("N")) return null;
      if (this.getTotaleEuroPlvEff() == null) return null;
	return percentualeDanno;
    }
    
    public String getPercentualeDannoFormatted() {
	if (this.getFlagDanneggiato() == null || this.getFlagDanneggiato().equalsIgnoreCase("N")) return null;
	if (this.getTotaleEuroPlvEff() == null) return null;
	return IuffiUtils.FORMAT.formatDecimal2(percentualeDanno);
    }

    public void setPercentualeDanno(BigDecimal percentualeDanno) {
	this.percentualeDanno = percentualeDanno;
    }

    public String getRecordModificato() {
	return recordModificato;
    }

    public void setRecordModificato(String recordModificato) {
	this.recordModificato = recordModificato;
    }

    public String getNote() {
	return note;
    }

    public void setNote(String note) {
	this.note = note;
    }
}
