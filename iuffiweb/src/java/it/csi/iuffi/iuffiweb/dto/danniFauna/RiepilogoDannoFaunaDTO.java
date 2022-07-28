package it.csi.iuffi.iuffiweb.dto.danniFauna;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RiepilogoDannoFaunaDTO implements ILoggable
{

	private Long progressivo;
	private long idSpecieFauna;
	private String descSpecieFauna;
	private Long idDannoFauna;
	private long idTipoDannoFauna;
	private String descTipoDannoFauna;
	private String istatComune;
	private String descComune;
	private long idUtilizzo;
	private String descTipoUtilizzo;
	private BigDecimal superficieCoinvolta;
	private BigDecimal importoDannoEffettivo;
	private BigDecimal superficieAccertata;
	private Long idRiepilogoDannoFauna;
	public Long getProgressivo()
	{
		return progressivo;
	}
	public void setProgressivo(Long progressivo)
	{
		this.progressivo = progressivo;
	}
	public long getIdSpecieFauna()
	{
		return idSpecieFauna;
	}
	public void setIdSpecieFauna(long idSpecieFauna)
	{
		this.idSpecieFauna = idSpecieFauna;
	}
	public String getDescSpecieFauna()
	{
		return descSpecieFauna;
	}
	public void setDescSpecieFauna(String descSpecieFauna)
	{
		this.descSpecieFauna = descSpecieFauna;
	}
	public long getIdTipoDannoFauna()
	{
		return idTipoDannoFauna;
	}
	public void setIdTipoDannoFauna(long idTipoDannoFauna)
	{
		this.idTipoDannoFauna = idTipoDannoFauna;
	}
	public String getDescTipoDannoFauna()
	{
		return descTipoDannoFauna;
	}
	public void setDescTipoDannoFauna(String descTipoDannoFauna)
	{
		this.descTipoDannoFauna = descTipoDannoFauna;
	}
	public String getIstatComune()
	{
		return istatComune;
	}
	public void setIstatComune(String istatComune)
	{
		this.istatComune = istatComune;
	}
	public String getDescComune()
	{
		return descComune;
	}
	public void setDescComune(String descComune)
	{
		this.descComune = descComune;
	}
	public long getIdUtilizzo()
	{
		return idUtilizzo;
	}
	public void setIdUtilizzo(long idUtilizzo)
	{
		this.idUtilizzo = idUtilizzo;
	}
	public String getDescTipoUtilizzo()
	{
		return descTipoUtilizzo;
	}
	public void setDescTipoUtilizzo(String descTipoUtilizzo)
	{
		this.descTipoUtilizzo = descTipoUtilizzo;
	}
	public BigDecimal getSuperficieCoinvolta()
	{
		return superficieCoinvolta;
	}
	public void setSuperficieCoinvolta(BigDecimal superficieCoinvolta)
	{
		this.superficieCoinvolta = superficieCoinvolta;
	}
	public BigDecimal getImportoDannoEffettivo()
	{
		return importoDannoEffettivo;
	}
	public void setImportoDannoEffettivo(BigDecimal importoDannoEffettivo)
	{
		this.importoDannoEffettivo = importoDannoEffettivo;
	}
	
	public Long getIdRiepilogoDannoFauna()
	{
		return idRiepilogoDannoFauna;
	}
	public void setIdRiepilogoDannoFauna(Long idRiepilogoDannoFauna)
	{
		this.idRiepilogoDannoFauna = idRiepilogoDannoFauna;
	}
	public Long getIdDannoFauna()
	{
		return idDannoFauna;
	}
	public void setIdDannoFauna(Long idDannoFauna)
	{
		this.idDannoFauna = idDannoFauna;
	}
	public BigDecimal getSuperficieAccertata()
	{
		return superficieAccertata;
	}
	public void setSuperficieAccertata(BigDecimal superficieAccertata)
	{
		this.superficieAccertata = superficieAccertata;
	}
	
	
	//end of auto generated
	

	public String getSuperficieCoinvoltaStr()
	{
		if(superficieCoinvolta == null)
		{
			return null;
		}
		return IuffiUtils.FORMAT.formatDecimal4(superficieCoinvolta);
	}
	public String getImportoDannoEffettivoStr()
	{
		if(importoDannoEffettivo == null)
		{
			return null;
		}
		return IuffiUtils.FORMAT.formatDecimal2(importoDannoEffettivo);
	}
	public String getSuperficieAccertataStr()
	{
		if(superficieAccertata == null)
		{
			return null;
		}
		else
		{
			return IuffiUtils.FORMAT.formatDecimal4(superficieAccertata);
		}
	}
	
	public String getIdRiepilogo()
	{
		return ""+ getIdDannoFauna() + "_" + getIdUtilizzo() + "_" + getIstatComune();
	}
	
	@Override
	public String toString()
	{
		return "RiepilogoDannoFaunaDTO [idDannoFauna=" + idDannoFauna + ", istatComune=" + istatComune + ", idUtilizzo="
				+ idUtilizzo + ", superficieCoinvolta=" + superficieCoinvolta + ", importoDannoEffettivo="
				+ importoDannoEffettivo + ", idRiepilogoDannoFauna=" + idRiepilogoDannoFauna + "]";
	}
	
	
	
}
