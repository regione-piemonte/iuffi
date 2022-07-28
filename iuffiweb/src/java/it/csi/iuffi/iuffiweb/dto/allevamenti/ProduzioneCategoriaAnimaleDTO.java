package it.csi.iuffi.iuffiweb.dto.allevamenti;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProduzioneCategoriaAnimaleDTO implements ILoggable
{	
	private static final long serialVersionUID = -2398432284414559512L;
	//D_PRODUZIONE
	private int idProduzione;
	private int extIdCategoriaAnimale;
	private int idUnitaMisura;
	private String descProduzione;
	private BigDecimal quantitaProdottaMin;
	private BigDecimal quantitaProdottaMedia;
	private BigDecimal quantitaProdottaMax;
	private BigDecimal prezzoMin;
	private BigDecimal prezzoMedio;
	private BigDecimal prezzoMax;
	
	//D_CATEGORIA_ANIMALE
	private BigDecimal pesoVivoMin;
	private BigDecimal pesoVivoMedio;
	private BigDecimal pesoVivoMax;
	private BigDecimal giornateLavorativeMin;
	private BigDecimal giornateLavorativeMedie;
	private BigDecimal giornateLavorativeMax;
	private Long consumoAnnuoUf;
	private BigDecimal numeroMaxAnimaliPerHa;
	private String ageaCod;
	
	//D_UNITA_MISURA
	private String codice;
	private Date dataFine;
	private String descUnitaMisura;
	
	//T_PRODUZIONE_VENDIBILE
	private long idProduzioneVendibile;
	private long idProduzioneZootecnica;
	private long numeroCapi;
	private BigDecimal quantitaProdotta;
	private BigDecimal quantitaReimpiegata;
	private BigDecimal prezzo;
	
	//V_ALLEVAMENTO
	private Long quantita;
	
	
	public int getIdProduzione()
	{
		return idProduzione;
	}
	public void setIdProduzione(int idProduzione)
	{
		this.idProduzione = idProduzione;
	}
	public int getExtIdCategoriaAnimale()
	{
		return extIdCategoriaAnimale;
	}
	public void setExtIdCategoriaAnimale(int extIdCategoriaAnimale)
	{
		this.extIdCategoriaAnimale = extIdCategoriaAnimale;
	}
	public int getIdUnitaMisura()
	{
		return idUnitaMisura;
	}
	public void setIdUnitaMisura(int idUnitaMisura)
	{
		this.idUnitaMisura = idUnitaMisura;
	}
	public String getDescProduzione()
	{
		return descProduzione;
	}
	public void setDescProduzione(String descProduzione)
	{
		this.descProduzione = descProduzione;
	}
	public BigDecimal getQuantitaProdottaMin()
	{
		return quantitaProdottaMin;
	}
	public void setQuantitaProdottaMin(BigDecimal quantitaProdottaMin)
	{
		this.quantitaProdottaMin = quantitaProdottaMin;
	}
	public BigDecimal getQuantitaProdottaMedia()
	{
		return quantitaProdottaMedia;
	}
	public void setQuantitaProdottaMedia(BigDecimal quantitaProdottaMedia)
	{
		this.quantitaProdottaMedia = quantitaProdottaMedia;
	}
	public BigDecimal getQuantitaProdottaMax()
	{
		return quantitaProdottaMax;
	}
	public void setQuantitaProdottaMax(BigDecimal quantitaProdottaMax)
	{
		this.quantitaProdottaMax = quantitaProdottaMax;
	}
	public BigDecimal getPrezzoMin()
	{
		return prezzoMin;
	}
	public void setPrezzoMin(BigDecimal prezzoMin)
	{
		this.prezzoMin = prezzoMin;
	}
	public BigDecimal getPrezzoMedio()
	{
		return prezzoMedio;
	}
	public void setPrezzoMedio(BigDecimal prezzoMedio)
	{
		this.prezzoMedio = prezzoMedio;
	}
	public BigDecimal getPrezzoMax()
	{
		return prezzoMax;
	}
	public void setPrezzoMax(BigDecimal prezzoMax)
	{
		this.prezzoMax = prezzoMax;
	}
	public BigDecimal getPesoVivoMin()
	{
		return pesoVivoMin;
	}
	public void setPesoVivoMin(BigDecimal pesoVivoMin)
	{
		this.pesoVivoMin = pesoVivoMin;
	}
	public BigDecimal getPesoVivoMedio()
	{
		return pesoVivoMedio;
	}
	public void setPesoVivoMedio(BigDecimal pesoVivoMedio)
	{
		this.pesoVivoMedio = pesoVivoMedio;
	}
	public BigDecimal getPesoVivoMax()
	{
		return pesoVivoMax;
	}
	public void setPesoVivoMax(BigDecimal pesoVivoMax)
	{
		this.pesoVivoMax = pesoVivoMax;
	}
	public BigDecimal getGiornateLavorativeMin()
	{
		return giornateLavorativeMin;
	}
	public void setGiornateLavorativeMin(BigDecimal giornateLavorativeMin)
	{
		this.giornateLavorativeMin = giornateLavorativeMin;
	}
	public BigDecimal getGiornateLavorativeMedie()
	{
		return giornateLavorativeMedie;
	}
	public void setGiornateLavorativeMedie(BigDecimal giornateLavorativeMedie)
	{
		this.giornateLavorativeMedie = giornateLavorativeMedie;
	}
	public BigDecimal getGiornateLavorativeMax()
	{
		return giornateLavorativeMax;
	}
	public void setGiornateLavorativeMax(BigDecimal giornateLavorativeMax)
	{
		this.giornateLavorativeMax = giornateLavorativeMax;
	}
	public Long getConsumoAnnuoUf()
	{
		return consumoAnnuoUf;
	}
	public void setConsumoAnnuoUf(Long consumoAnnuoUf)
	{
		this.consumoAnnuoUf = consumoAnnuoUf;
	}
	public BigDecimal getNumeroMaxAnimaliPerHa()
	{
		return numeroMaxAnimaliPerHa;
	}
	public void setNumeroMaxAnimaliPerHa(BigDecimal numeroMaxAnimaliPerHa)
	{
		this.numeroMaxAnimaliPerHa = numeroMaxAnimaliPerHa;
	}
	public String getAgeaCod()
	{
		return ageaCod;
	}
	public void setAgeaCod(String ageaCod)
	{
		this.ageaCod = ageaCod;
	}
	public String getCodice()
	{
		return codice;
	}
	public void setCodice(String codice)
	{
		this.codice = codice;
	}
	public Date getDataFine()
	{
		return dataFine;
	}
	public void setDataFine(Date dataFine)
	{
		this.dataFine = dataFine;
	}
	public String getDescUnitaMisura()
	{
		return descUnitaMisura;
	}
	public void setDescUnitaMisura(String descUnitaMisura)
	{
		this.descUnitaMisura = descUnitaMisura;
	}
	public long getIdProduzioneVendibile()
	{
		return idProduzioneVendibile;
	}
	public void setIdProduzioneVendibile(long idProduzioneVendibile)
	{
		this.idProduzioneVendibile = idProduzioneVendibile;
	}
	public long getIdProduzioneZootecnica()
	{
		return idProduzioneZootecnica;
	}
	public void setIdProduzioneZootecnica(long idProduzioneZootecnica)
	{
		this.idProduzioneZootecnica = idProduzioneZootecnica;
	}
	public long getNumeroCapi()
	{
		return numeroCapi;
	}
	public void setNumeroCapi(long numeroCapi)
	{
		this.numeroCapi = numeroCapi;
	}
	public BigDecimal getQuantitaProdotta()
	{
		return quantitaProdotta;
	}
	public void setQuantitaProdotta(BigDecimal quantitaProdotta)
	{
		this.quantitaProdotta = quantitaProdotta;
	}
	public BigDecimal getQuantitaReimpiegata()
	{
		return quantitaReimpiegata;
	}
	public void setQuantitaReimpiegata(BigDecimal quantitaReimpiegata)
	{
		this.quantitaReimpiegata = quantitaReimpiegata;
	}
	public BigDecimal getPrezzo()
	{
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo)
	{
		this.prezzo = prezzo;
	}
	public Long getQuantita()
	{
		return quantita;
	}
	public void setQuantita(Long quantita)
	{
		this.quantita = quantita;
	}
	
}
