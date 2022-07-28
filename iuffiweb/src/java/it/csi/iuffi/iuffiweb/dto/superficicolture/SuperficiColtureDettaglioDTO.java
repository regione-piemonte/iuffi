package it.csi.iuffi.iuffiweb.dto.superficicolture;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SuperficiColtureDettaglioDTO implements ILoggable {
    private static final long serialVersionUID = -1529306859941114441L;
    private long idSuperficieColtura;
    private String descProvincia;
    private String descComune;
    private String ubicazioneTerreno;
    private String tipoUtilizzoDescrizione;
    private String destinazioneDescrizione;
    private String dettaglioUsoDescrizione;
    private String qualitaUsoDescrizione;
    private String varietaDescrizione;
    private BigDecimal produzioneHa;
    private BigDecimal produzioneHaDanno;
    private BigDecimal produzioneDichiarata;
    private BigDecimal giornateLavorativeDich;
    private BigDecimal ufTotali;
    private BigDecimal qliReimpiegati;
    private BigDecimal ufReimpiegate;
    private BigDecimal plvTotQuintali;
    private BigDecimal prezzo;
    private BigDecimal plvTotDich;
    private String recordModificato;
    private String note;

    // proprietà non visualizzate a video
    private String extIstatComune;
    private BigDecimal extIdUtilizzo;
    private String codiceUtilizzo;
    private String codiceDestinazione;
    private String codiceDettaglioUso;
    private String codiceQualitaUso;
    private String codiceVarieta;
    private BigDecimal idBando;
    private String codTipoUtilizzo;
    private String descTipoUtilizzo;
    private String codTipoUtilizzoSecondario;
    private String descTipoUtilizzoSecondario;
    private String colturaSecondaria;
    private BigDecimal superficieUtilizzata;

    
    public BigDecimal getExtIdUtilizzo() {
	return extIdUtilizzo;
    }

    public void setExtIdUtilizzo(BigDecimal extIdUtilizzo) {
	this.extIdUtilizzo = extIdUtilizzo;
    }

    public String getCodiceUtilizzo() {
        return codiceUtilizzo;
    }

    public void setCodiceUtilizzo(String codiceUtilizzo) {
        this.codiceUtilizzo = codiceUtilizzo;
    }

    public String getCodiceDestinazione() {
        return codiceDestinazione;
    }

    public void setCodiceDestinazione(String codiceDestinazione) {
        this.codiceDestinazione = codiceDestinazione;
    }

    public String getCodiceDettaglioUso() {
        return codiceDettaglioUso;
    }

    public void setCodiceDettaglioUso(String codiceDettaglioUso) {
        this.codiceDettaglioUso = codiceDettaglioUso;
    }

    public String getCodiceQualitaUso() {
        return codiceQualitaUso;
    }

    public void setCodiceQualitaUso(String codiceQualitaUso) {
        this.codiceQualitaUso = codiceQualitaUso;
    }

    public String getCodiceVarieta() {
        return codiceVarieta;
    }

    public void setCodiceVarieta(String codiceVarieta) {
        this.codiceVarieta = codiceVarieta;
    }

    public BigDecimal getIdBando() {
	return idBando;
    }

    public void setIdBando(BigDecimal idBando) {
	this.idBando = idBando;
    }

    public BigDecimal getProduzioneHaDanno() {
	return produzioneHaDanno;
    }

    public void setProduzioneHaDanno(BigDecimal produzioneHaDanno) {
	this.produzioneHaDanno = produzioneHaDanno;
    }

    public long getIdSuperficieColtura() {
	return idSuperficieColtura;
    }

    public void setIdSuperficieColtura(long idSuperficieColtura) {
	this.idSuperficieColtura = idSuperficieColtura;
    }

    public String getExtIstatComune() {
	return extIstatComune;
    }

    public void setExtIstatComune(String extIstatComune) {
	this.extIstatComune = extIstatComune;
    }

    public String getDescProvincia() {
	return descProvincia;
    }

    public void setDescProvincia(String descProvincia) {
	this.descProvincia = descProvincia;
    }

    public String getDescComune() {
	return descComune;
    }

    public void setDescComune(String descComune) {
	this.descComune = descComune;
    }

    public String getCodTipoUtilizzo() {
	return codTipoUtilizzo;
    }

    public void setCodTipoUtilizzo(String codTipoUtilizzo) {
	this.codTipoUtilizzo = codTipoUtilizzo;
    }

    public String getDescTipoUtilizzo() {
	return descTipoUtilizzo;
    }

    public void setDescTipoUtilizzo(String descTipoUtilizzo) {
	this.descTipoUtilizzo = descTipoUtilizzo;
    }

    public String getCodTipoUtilizzoSecondario() {
	return codTipoUtilizzoSecondario;
    }

    public void setCodTipoUtilizzoSecondario(String codTipoUtilizzoSecondario) {
	this.codTipoUtilizzoSecondario = codTipoUtilizzoSecondario;
    }

    public String getDescTipoUtilizzoSecondario() {
	return descTipoUtilizzoSecondario;
    }

    public void setDescTipoUtilizzoSecondario(String descTipoUtilizzoSecondario) {
	this.descTipoUtilizzoSecondario = descTipoUtilizzoSecondario;
    }

    public String getColturaSecondaria() {
	return colturaSecondaria;
    }

    public void setColturaSecondaria(String colturaSecondaria) {
	this.colturaSecondaria = colturaSecondaria;
    }

    public BigDecimal getSuperficieUtilizzata() {
	return superficieUtilizzata;
    }

    public String getSuperficieUtilizzataFormatted() {
	return IuffiUtils.FORMAT.formatDecimal4(superficieUtilizzata);
    }

    public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata) {
	this.superficieUtilizzata = superficieUtilizzata;
    }

    public BigDecimal getProduzioneHa() {
	return produzioneHa;
    }

    public String getProduzioneHaFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(produzioneHa);
    }

    public String getProduzioneHaDannoFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(produzioneHaDanno);
    }

    public void setProduzioneHa(BigDecimal produzioneHa) {
	this.produzioneHa = produzioneHa;
    }

    public BigDecimal getProduzioneDichiarata() {
	return produzioneDichiarata;
    }

    public String getProduzioneDichiarataFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(produzioneDichiarata);
    }

    public void setProduzioneDichiarata(BigDecimal produzioneDichiarata) {
	this.produzioneDichiarata = produzioneDichiarata;
    }

    public BigDecimal getGiornateLavorativeDich() {
	return giornateLavorativeDich;
    }

    public void setGiornateLavorativeDich(BigDecimal giornateLavorativeDich) {
	this.giornateLavorativeDich = giornateLavorativeDich;
    }

    public BigDecimal getUfTotali() {
	return ufTotali;
    }

    public void setUfTotali(BigDecimal ufTotali) {
	this.ufTotali = ufTotali;
    }

    public BigDecimal getUfReimpiegate() {
	return ufReimpiegate;
    }

    public void setUfReimpiegate(BigDecimal ufReimpiegate) {
	this.ufReimpiegate = ufReimpiegate;
    }

    public BigDecimal getQliReimpiegati() {
	return qliReimpiegati;
    }

    public void setQliReimpiegati(BigDecimal qliReimpiegati) {
	this.qliReimpiegati = qliReimpiegati;
    }

    public BigDecimal getPlvTotQuintali() {
	return plvTotQuintali;
    }

    public String getPlvTotQuintaliFormtted() {
	return IuffiUtils.FORMAT.formatDecimal4(plvTotQuintali);
    }

    public void setPlvTotQuintali(BigDecimal plvTotQuintali) {
	this.plvTotQuintali = plvTotQuintali;
    }

    public BigDecimal getPrezzo() {
	return prezzo;
    }

    public String getPrezzoFormatted() {
	return IuffiUtils.FORMAT.formatDecimal2(prezzo);
    }

    public void setPrezzo(BigDecimal prezzo) {
	this.prezzo = prezzo;
    }

    public BigDecimal getPlvTotDich() {
	return plvTotDich;
    }

    public void setPlvTotDich(BigDecimal plvTotDich) {
	this.plvTotDich = plvTotDich;
    }

    public String getTipoUtilizzoDescrizione() {
	return tipoUtilizzoDescrizione;
    }

    public void setTipoUtilizzoDescrizione(String tipoUtilizzoDescrizione) {
	this.tipoUtilizzoDescrizione = tipoUtilizzoDescrizione;
    }

    public String getUbicazioneTerreno() {
	return ubicazioneTerreno;
    }

    public void setUbicazioneTerreno(String ubicazioneTerreno) {
	this.ubicazioneTerreno = ubicazioneTerreno;
    }

    public String getNote() {
	return note;
    }

    public void setNote(String note) {
	this.note = note;
    }

    public String getRecordModificato() {
	return recordModificato;
    }

    public void setRecordModificato(String recordModificato) {
	this.recordModificato = recordModificato;
    }

    public String getDestinazioneDescrizione() {
	return destinazioneDescrizione;
    }

    public void setDestinazioneDescrizione(String destinazioneDescrizione) {
	this.destinazioneDescrizione = destinazioneDescrizione;
    }

    public String getDettaglioUsoDescrizione() {
	return dettaglioUsoDescrizione;
    }

    public void setDettaglioUsoDescrizione(String dettaglioUsoDescrizione) {
	this.dettaglioUsoDescrizione = dettaglioUsoDescrizione;
    }

    public String getQualitaUsoDescrizione() {
	return qualitaUsoDescrizione;
    }

    public void setQualitaUsoDescrizione(String qualitaUsoDescrizione) {
	this.qualitaUsoDescrizione = qualitaUsoDescrizione;
    }

    public String getVarietaDescrizione() {
	return varietaDescrizione;
    }

    public void setVarietaDescrizione(String varietaDescrizione) {
	this.varietaDescrizione = varietaDescrizione;
    }
}
