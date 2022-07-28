package it.csi.iuffi.iuffiweb.dto.coltureaziendali;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class UtilizzoReseDTO implements ILoggable {

    private static final long serialVersionUID = 4174010140204588163L;

    private BigDecimal extIdUtilizzo;
    private BigDecimal resaMin;
    private BigDecimal resaMax;
    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;
    private BigDecimal giornateLavorateMin;
    private BigDecimal giornateLavorateMax;
    

    public BigDecimal getExtIdUtilizzo() {
        return extIdUtilizzo;
    }

    public void setExtIdUtilizzo(BigDecimal extIdUtilizzo) {
        this.extIdUtilizzo = extIdUtilizzo;
    }

    public BigDecimal getResaMin() {
	return resaMin;
    }

    public void setResaMin(BigDecimal resaMin) {
	this.resaMin = resaMin;
    }

    public BigDecimal getResaMax() {
	return resaMax;
    }

    public void setResaMax(BigDecimal resaMax) {
	this.resaMax = resaMax;
    }

    public BigDecimal getPrezzoMin() {
	return prezzoMin;
    }

    public void setPrezzoMin(BigDecimal prezzoMin) {
	this.prezzoMin = prezzoMin;
    }

    public BigDecimal getPrezzoMax() {
	return prezzoMax;
    }

    public void setPrezzoMax(BigDecimal prezzoMax) {
	this.prezzoMax = prezzoMax;
    }

    public BigDecimal getGiornateLavorateMin() {
	return giornateLavorateMin;
    }

    public void setGiornateLavorateMin(BigDecimal giornateLavorateMin) {
	this.giornateLavorateMin = giornateLavorateMin;
    }

    public BigDecimal getGiornateLavorateMax() {
	return giornateLavorateMax;
    }

    public void setGiornateLavorateMax(BigDecimal giornateLavorateMax) {
	this.giornateLavorateMax = giornateLavorateMax;
    }
    
}
