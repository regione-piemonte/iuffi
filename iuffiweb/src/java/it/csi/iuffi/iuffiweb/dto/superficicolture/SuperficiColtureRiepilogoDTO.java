package it.csi.iuffi.iuffiweb.dto.superficicolture;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SuperficiColtureRiepilogoDTO implements ILoggable
{
	private static final long serialVersionUID = -1529306859941114441L;
	private BigDecimal supCatastale;
	private BigDecimal superficieUtilizzata;
	private BigDecimal sauS,sauN,sauA;
	
	public BigDecimal getSupCatastale()
	{
		return supCatastale;
	}
	public String getSupCatastaleFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(supCatastale);
	}
	public void setSupCatastale(BigDecimal supCatastale)
	{
		this.supCatastale = supCatastale;
	}
	public BigDecimal getSuperficieUtilizzata()
	{
		return superficieUtilizzata;
	}
	public String getSuperficieUtilizzataFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(superficieUtilizzata);
	}
	public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
	{
		this.superficieUtilizzata = superficieUtilizzata;
	}
	public BigDecimal getSauS()
	{
		return sauS;
	}
	public String getSauSFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(sauS);
	}
	public void setSauS(BigDecimal sauS)
	{
		this.sauS = sauS;
	}
	public BigDecimal getSauN()
	{
		return sauN;
	}
	public String getSauNFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(sauN);
	}
	public void setSauN(BigDecimal sauN)
	{
		this.sauN = sauN;
	}
	public BigDecimal getSauA()
	{
		return sauA;
	}
	public String getSauAFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(sauA);
	}
	public void setSauA(BigDecimal sauA)
	{
		this.sauA = sauA;
	}
}
