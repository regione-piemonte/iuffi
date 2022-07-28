package it.csi.iuffi.iuffiweb.dto.superficicolture;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SuperficiColtureDettaglioParticellareDTO implements ILoggable
{
	private static final long serialVersionUID = 6926969568279519182L;
	private long idStoricoParticella;
	private String sezione;
	private long foglio;
	private long particella;
	private String subalterno;
	private BigDecimal supCatastale;
	private BigDecimal superficieUtilizzata;
	private String descTitoloPossesso;
	private String descZonaAltimetrica;
	public long getIdStoricoParticella()
	{
		return idStoricoParticella;
	}
	public void setIdStoricoParticella(long idStoricoParticella)
	{
		this.idStoricoParticella = idStoricoParticella;
	}
	public String getSezione()
	{
		return sezione;
	}
	public void setSezione(String sezione)
	{
		this.sezione = sezione;
	}
	public long getFoglio()
	{
		return foglio;
	}
	public void setFoglio(long foglio)
	{
		this.foglio = foglio;
	}
	public long getParticella()
	{
		return particella;
	}
	public void setParticella(long particella)
	{
		this.particella = particella;
	}
	public String getSubalterno()
	{
		return subalterno;
	}
	public void setSubalterno(String subalterno)
	{
		this.subalterno = subalterno;
	}
	public BigDecimal getSupCatastale()
	{
		return supCatastale;
	}
	public void setSupCatastale(BigDecimal supCatastale)
	{
		this.supCatastale = supCatastale;
	}
	public BigDecimal getSuperficieUtilizzata()
	{
		return superficieUtilizzata;
	}
	public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
	{
		this.superficieUtilizzata = superficieUtilizzata;
	}
	public String getDescTitoloPossesso()
	{
		return descTitoloPossesso;
	}
	public void setDescTitoloPossesso(String descTitoloPossesso)
	{
		this.descTitoloPossesso = descTitoloPossesso;
	}
	public String getDescZonaAltimetrica()
	{
		return descZonaAltimetrica;
	}
	public void setDescZonaAltimetrica(String descZonaAltimetrica)
	{
		this.descZonaAltimetrica = descZonaAltimetrica;
	}
}
