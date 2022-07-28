package it.csi.iuffi.iuffiweb.dto.superficicolture;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SuperficiColturePlvVegetaleDTO implements ILoggable
{

	private static final long serialVersionUID = -2848214815963631185L;
	private long idUtilizzo;
	private String tipoUtilizzoDescrizione;
	private BigDecimal superficieUtilizzata;
	private BigDecimal produzioneQ;
	private BigDecimal giornateLavorate;
	private BigDecimal uf;
	private BigDecimal reimpieghiQ;
	private BigDecimal reimpieghiUf;
	private BigDecimal plv;
	private BigDecimal giornateLavPerSupUtil;
	public long getIdUtilizzo()
	{
		return idUtilizzo;
	}
	public void setIdUtilizzo(long idUtilizzo)
	{
		this.idUtilizzo = idUtilizzo;
	}
	public String getTipoUtilizzoDescrizione()
	{
		return tipoUtilizzoDescrizione;
	}
	public void setTipoUtilizzoDescrizione(String tipoUtilizzoDescrizione)
	{
		this.tipoUtilizzoDescrizione = tipoUtilizzoDescrizione;
	}
	public BigDecimal getSuperficieUtilizzata()
	{
		return superficieUtilizzata;
	}

	public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
	{
		this.superficieUtilizzata = superficieUtilizzata;
	}
	public BigDecimal getProduzioneQ()
	{
		return produzioneQ;
	}
	public void setProduzioneQ(BigDecimal produzioneQ)
	{
		this.produzioneQ = produzioneQ;
	}
	public BigDecimal getGiornateLavorate()
	{
		return giornateLavorate;
	}
	public void setGiornateLavorate(BigDecimal giornateLavorate)
	{
		this.giornateLavorate = giornateLavorate;
	}
	public BigDecimal getUf()
	{
		return uf;
	}
	public void setUf(BigDecimal uf)
	{
		this.uf = uf;
	}
	public BigDecimal getReimpieghiQ()
	{
		return reimpieghiQ;
	}
	public void setReimpieghiQ(BigDecimal reimpieghiQ)
	{
		this.reimpieghiQ = reimpieghiQ;
	}
	public BigDecimal getReimpieghiUf()
	{
		return reimpieghiUf;
	}
	public void setReimpieghiUf(BigDecimal reimpieghiUf)
	{
		this.reimpieghiUf = reimpieghiUf;
	}
	public BigDecimal getPlv()
	{
		return plv;
	}
	public void setPlv(BigDecimal plv)
	{
		this.plv = plv;
	}
	public BigDecimal getGiornateLavPerSupUtil()
	{
		return giornateLavPerSupUtil;
	}
	public void setGiornateLavPerSupUtil(BigDecimal giornateLavPerSupUtil)
	{
		this.giornateLavPerSupUtil = giornateLavPerSupUtil;
	}
	
	
	public String getSuperficieUtilizzataFormatted()
	{
		return superficieUtilizzata != null ? IuffiUtils.FORMAT.formatDecimal4(superficieUtilizzata) : "";
	}
	
	public String getProduzioneQFormatted()
	{
		return produzioneQ != null ? IuffiUtils.FORMAT.formatDecimal2(produzioneQ) : "";
	}
	
	public String getGiornateLavPerSupUtilFormatted()
	{
		return giornateLavPerSupUtil != null ? IuffiUtils.FORMAT.formatDecimal2(giornateLavPerSupUtil) : "";
	}
	
	public String getUfFormatted()
	{
		return uf != null ? IuffiUtils.FORMAT.formatDecimal2(uf) : "";
	}
	
	public String getReimpieghiQFormatted()
	{
		return reimpieghiQ != null ? IuffiUtils.FORMAT.formatDecimal2(reimpieghiQ) : "";
	}
	
	public String getReimpieghiUfFormatted()
	{
		return reimpieghiUf != null ? IuffiUtils.FORMAT.formatDecimal2(reimpieghiUf) : "";
	}
	
	public String getPlvFormatted()
	{
		return plv != null ? IuffiUtils.FORMAT.formatDecimal2(plv) : "" ;
	}
	

}
