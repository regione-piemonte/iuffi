package it.csi.iuffi.iuffiweb.dto.allevamenti;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class AllevamentiDTO implements ILoggable
{
	private static final long serialVersionUID = -6768686711496833021L;
	
	//per Elenco e Dettaglio Allevamenti
	private String ubicazioneAllevamento;
	private String descrizioneSpecieAnimale;
	private String descrizioneCategoriaAnimale;
	private long quantita;
	private String unitaMisuraSpecie;
	private BigDecimal coefficienteUba;
	private BigDecimal unitaForaggere;
	private BigDecimal consumoAnnuoUf;
	private BigDecimal giornateLavorative;
	private BigDecimal plv;
	private Long idProduzioneZootecnica;
	private long idCategoriaAnimale; 
	private String istatComune;
	
	//per Dettaglio Allevamenti
	private String codiceAziendaZootecnica;
	private BigDecimal pesoVivoMedio;
	private BigDecimal giornateLavorativeMedie;
	private String note;
	
	//per Allevamenti singoli
	private long idAllevamento;
	private String denominazioneAllevamento;
	private String descrizioneComune;
	private String indirizzo;
	private String siglaProvincia;
	
	private Date dataUltimoAggiornamento;
	private Long extIdUtenteAggiornamento;
	
	public String getUbicazioneAllevamento()
	{
		return ubicazioneAllevamento;
	}
	public void setUbicazioneAllevamento(String ubicazioneAllevamento)
	{
		this.ubicazioneAllevamento = ubicazioneAllevamento;
	}
	public String getDescrizioneSpecieAnimale()
	{
		return descrizioneSpecieAnimale;
	}
	public void setDescrizioneSpecieAnimale(String descrizioneSpecieAnimale)
	{
		this.descrizioneSpecieAnimale = descrizioneSpecieAnimale;
	}
	public String getDescrizioneCategoriaAnimale()
	{
		return descrizioneCategoriaAnimale;
	}
	public void setDescrizioneCategoriaAnimale(String descrizioneCategoriaAnimale)
	{
		this.descrizioneCategoriaAnimale = descrizioneCategoriaAnimale;
	}
	public long getQuantita()
	{
		return quantita;
	}
	public void setQuantita(long quantita)
	{
		this.quantita = quantita;
	}
	public String getQuantitaUnitaMisura()
	{
		return quantita + " " + unitaMisuraSpecie;
	}
	public BigDecimal getCoefficienteUba()
	{
		return coefficienteUba;
	}
	public void setCoefficienteUba(BigDecimal coefficienteUba)
	{
		this.coefficienteUba = coefficienteUba;
	}
	public BigDecimal getConsumoAnnuoUf()
	{
		return consumoAnnuoUf;
	}
	public void setConsumoAnnuoUf(BigDecimal consumoAnnuoUf)
	{
		this.consumoAnnuoUf = consumoAnnuoUf;
	}

	
	public BigDecimal getGiornateLavorative()
	{
		return giornateLavorative;
	}
	public String getGiornateLavorativeFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(giornateLavorative);
	}
	public void setGiornateLavorative(BigDecimal giornateLavorative)
	{
		this.giornateLavorative = giornateLavorative;
	}
	public BigDecimal getPlv()
	{
		return plv;
	}
	public void setPlv(BigDecimal plv)
	{
		this.plv = plv;
	}
	public String getCodiceAziendaZootecnica()
	{
		return codiceAziendaZootecnica;
	}
	public void setCodiceAziendaZootecnica(String codiceAziendaZootecnica)
	{
		this.codiceAziendaZootecnica = codiceAziendaZootecnica;
	}
	public BigDecimal getUnitaForaggere()
	{
		return unitaForaggere;
	}
	public String getUnitaForaggereFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(unitaForaggere);
	}
	public void setUnitaForaggere(BigDecimal unitaForaggere)
	{
		this.unitaForaggere = unitaForaggere;
	}
	public BigDecimal getPesoVivoMedio()
	{
		return pesoVivoMedio;
	}
	public String getPesoVivoMedioFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(pesoVivoMedio);
	}
	public void setPesoVivoMedio(BigDecimal pesoVivoMedio)
	{
		this.pesoVivoMedio = pesoVivoMedio;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public Long getIdProduzioneZootecnica()
	{
		return idProduzioneZootecnica;
	}
	public void setIdProduzioneZootecnica(Long idProduzioneZootecnica)
	{
		this.idProduzioneZootecnica = idProduzioneZootecnica;
	}
	public String getUnitaMisuraSpecie()
	{
		return unitaMisuraSpecie;
	}
	public void setUnitaMisuraSpecie(String unitaMisuraSpecie)
	{
		this.unitaMisuraSpecie = unitaMisuraSpecie;
	}
	public BigDecimal getGiornateLavorativeMedie()
	{
		return giornateLavorativeMedie;
	}
	public String getGiornateLavorativeMedieFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(giornateLavorativeMedie);
	}
	public void setGiornateLavorativeMedie(BigDecimal giornateLavorativeMedie)
	{
		this.giornateLavorativeMedie = giornateLavorativeMedie;
	}
	public long getIdCategoriaAnimale()
	{
		return idCategoriaAnimale;
	}
	public void setIdCategoriaAnimale(long idCategoriaAnimale)
	{
		this.idCategoriaAnimale = idCategoriaAnimale;
	}
	public String getIstatComune()
	{
		return istatComune;
	}
	public void setIstatComune(String istatComune)
	{
		this.istatComune = istatComune;
	}
	public String getChiaveAllevamento()
	{
		return Long.toString(idCategoriaAnimale) + "_" + istatComune;
	}
	public long getIdAllevamento()
	{
		return idAllevamento;
	}
	public void setIdAllevamento(long idAllevamento)
	{
		this.idAllevamento = idAllevamento;
	}
	
	public String getDenominazioneAllevamento()
	{
		return denominazioneAllevamento;
	}
	public void setDenominazioneAllevamento(String denominazioneAllevamento)
	{
		this.denominazioneAllevamento = denominazioneAllevamento;
	}
	
	
	public String getDescrizioneComune()
	{
		return descrizioneComune;
	}
	public void setDescrizioneComune(String descrizioneComune)
	{
		this.descrizioneComune = descrizioneComune;
	}
	public String getIndirizzo()
	{
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo)
	{
		this.indirizzo = indirizzo;
	}
	public String getSiglaProvincia()
	{
		return siglaProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia)
	{
		this.siglaProvincia = siglaProvincia;
	}
	public String getDescrizioneAllevamento()
	{
		return (
				descrizioneCategoriaAnimale + ", " +
				denominazioneAllevamento + ", " +
				descrizioneComune + " (" +
				siglaProvincia + ")"
				);
	}
	
	public String getDescrizioneAllevamentoDanno()
	{
		return (
				descrizioneCategoriaAnimale
				);
	}
	
	public String getDenominazioneAllevamentoDanno()
	{
		return 
				denominazioneAllevamento + "<br/>" +
				descrizioneComune + " (" +
				siglaProvincia + ") <br/>" +
				indirizzo
				;
	}
	public Date getDataUltimoAggiornamento()
	{
		return dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
	{
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
	public Long getExtIdUtenteAggiornamento()
	{
		return extIdUtenteAggiornamento;
	}
	public void setExtIdUtenteAggiornamento(Long extIdUtenteAggiornamento)
	{
		this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
	}
}
