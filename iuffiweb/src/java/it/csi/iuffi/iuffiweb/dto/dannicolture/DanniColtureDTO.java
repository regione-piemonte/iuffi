package it.csi.iuffi.iuffiweb.dto.dannicolture;

import java.math.BigDecimal;
import java.math.MathContext;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DanniColtureDTO implements ILoggable
{
	private static final long serialVersionUID = -1529306859941114441L;
	
	private long idUtilizzo;
	private Long codTipoUtilizzo;
	private String descTipoUtilizzo;
	private Long codTipoUtilizzoSecondario;
	private String descTipoUtilizzoSecondario;
	private String tipoUtilizzoDescrizione;
	private BigDecimal superficieUtilizzata;
	private BigDecimal totQliPlvOrd;
	private BigDecimal totEuroPlvOrd;
	private BigDecimal totQliPlvEff;
	private BigDecimal totEuroPlvEff;
	
	private BandoDTO bando;
	private BigDecimal ONE_HUNDRED = new BigDecimal("100.00");
	
	
	public long getIdUtilizzo()
	{
		return idUtilizzo;
	}
	public void setIdUtilizzo(long idUtilizzo)
	{
		this.idUtilizzo = idUtilizzo;
	}
	public Long getCodTipoUtilizzo()
	{
		return codTipoUtilizzo;
	}
	public void setCodTipoUtilizzo(Long codTipoUtilizzo)
	{
		this.codTipoUtilizzo = codTipoUtilizzo;
	}
	public String getDescTipoUtilizzo()
	{
		return descTipoUtilizzo;
	}
	public void setDescTipoUtilizzo(String descTipoUtilizzo)
	{
		this.descTipoUtilizzo = descTipoUtilizzo;
	}
	public Long getCodTipoUtilizzoSecondario()
	{
		return codTipoUtilizzoSecondario;
	}
	public void setCodTipoUtilizzoSecondario(Long codTipoUtilizzoSecondario)
	{
		this.codTipoUtilizzoSecondario = codTipoUtilizzoSecondario;
	}
	public String getDescTipoUtilizzoSecondario()
	{
		return descTipoUtilizzoSecondario;
	}
	public void setDescTipoUtilizzoSecondario(String descTipoUtilizzoSecondario)
	{
		this.descTipoUtilizzoSecondario = descTipoUtilizzoSecondario;
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
	public BigDecimal getTotQliPlvOrd()
	{
		return totQliPlvOrd;
	}
	public void setTotQliPlvOrd(BigDecimal totQliPlvOrd)
	{
		this.totQliPlvOrd = totQliPlvOrd;
	}
	public BigDecimal getTotEuroPlvOrd()
	{
		return totEuroPlvOrd;
	}
	public void setTotEuroPlvOrd(BigDecimal totEuroPlvOrd)
	{
		this.totEuroPlvOrd = totEuroPlvOrd;
	}
	public BigDecimal getTotQliPlvEff()
	{
		return totQliPlvEff;
	}
	public void setTotQliPlvEff(BigDecimal totQliPlvEff)
	{
		this.totQliPlvEff = totQliPlvEff;
	}
	public BigDecimal getTotEuroPlvEff()
	{
		return totEuroPlvEff;
	}
	public void setTotEuroPlvEff(BigDecimal totEuroPlvEff)
	{
		this.totEuroPlvEff = totEuroPlvEff;
	}
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	//PRODUZIONE PERDUTA
	//TODO: verificare
	public BigDecimal getProdPerduta()
	{
		BigDecimal prodPerduta=null;
		if(totEuroPlvOrd != null && totEuroPlvEff != null)
		{
			if(!(totEuroPlvOrd.equals(BigDecimal.ZERO) && totEuroPlvEff.equals(BigDecimal.ZERO)))
			{
				prodPerduta = totEuroPlvOrd.subtract(totEuroPlvEff);
			}
		}
		return prodPerduta;
	}
	
	public String getProdPerdutaFormatted()
	{
		
		BigDecimal prodPerduta = getProdPerduta();
		if(prodPerduta != null)
		{
			return IuffiUtils.FORMAT.formatDecimal2(prodPerduta);
		}
		else
		{
			return "0,00";
		}
	}
	
	public BigDecimal getPercDanno()
	{
		BigDecimal prodPerduta = getProdPerduta();
		if(prodPerduta == null)
		{
			return BigDecimal.ZERO;
		}
		if(totEuroPlvOrd != null && !totEuroPlvOrd.equals(BigDecimal.ZERO) && totEuroPlvEff != null)
		{
			return prodPerduta.divide(totEuroPlvOrd,MathContext.DECIMAL64).multiply(new BigDecimal("100.00"));
		}
		else
		{
			return null;
		}
	}
	
	public String getPercDannoFormatted()
	{
		BigDecimal percDanno = getPercDanno();
		if(percDanno != null)
		{
			return IuffiUtils.FORMAT.formatDecimal2(percDanno) + "%";
		}
		else
		{
			return "0,00%";
		}
	}
	
	public BandoDTO getBando()
	{
		return bando;
	}
	
	//settare il bando prima di richiamare i metodi della classe
	public void setBando(BandoDTO bando)
	{
		this.bando = bando;
	}
	
	public BigDecimal getPercContributoMaxConcessa()
	{
		BigDecimal prodPerduta = getProdPerduta();
		BigDecimal percContributoMax = bando.getPercContributoMaxConcessa(); 
		if(prodPerduta != null && percContributoMax != null)
		{
			return prodPerduta
					.multiply(percContributoMax)
					.divide(ONE_HUNDRED,MathContext.DECIMAL64);
		}
		else
		{
			return null;
		}
	}
	
	public String getPercContributoMaxConcessaFormatted()
	{
		BigDecimal percContributoMaxConcessa = bando.getPercContributoMaxConcessa();
		if(percContributoMaxConcessa != null)
		{
			return IuffiUtils.FORMAT.formatDecimal2(percContributoMaxConcessa);
		}
		else
		{
			return "";
		}
	}
	
	public BigDecimal getPercContributoErogabile()
	{
		BigDecimal prodPerduta = getProdPerduta();
		BigDecimal percContributoErogabile = bando.getPercContributoErogabile();
		if(prodPerduta != null && percContributoErogabile != null)
		{
			return prodPerduta
					.multiply(percContributoErogabile)
					.divide(ONE_HUNDRED,MathContext.DECIMAL64);
		}
		else
		{
			return null;
		}
	}
	
	public String getPercContributoErogabileFormatted()
	{
		BigDecimal percContributoErogabile = getPercContributoErogabile();
		if(percContributoErogabile != null)
		{
			return IuffiUtils.FORMAT.formatDecimal2(percContributoErogabile);
		}
		else
		{
			return "";
		}
	}
	
	//TODO:controllo aggiuntivo CU 307L
	public BigDecimal getContrMaxConcedibile()
	{
		BigDecimal percContributoMaxConcessa = bando.getPercContributoMaxConcessa();
		BigDecimal prodPerduta = getProdPerduta();
		BigDecimal contrMaxConcedibile=null;
		if(percContributoMaxConcessa != null && prodPerduta != null)
		{
			contrMaxConcedibile = prodPerduta.multiply(percContributoMaxConcessa).divide(ONE_HUNDRED,MathContext.DECIMAL64);
		}
		
		return contrMaxConcedibile;
	}
	
	public String getContrMaxConcedibileFormatted()
	{
		BigDecimal contrMaxConcedibile = getContrMaxConcedibile();
		if(contrMaxConcedibile!=null)
		{
			return IuffiUtils.FORMAT.formatDecimal2(contrMaxConcedibile);
		}
		return null;
	}
	
	//TODO:controllo aggiuntivo CU 307L
	public BigDecimal getContrErogabile()
	{
		BigDecimal percContributoMaxErogabile = bando.getPercContributoErogabile();
		BigDecimal prodPerduta = getProdPerduta();
		BigDecimal contrErogabile=null;
		if(percContributoMaxErogabile != null && prodPerduta != null)
		{
			contrErogabile = prodPerduta.multiply(percContributoMaxErogabile).divide(ONE_HUNDRED,MathContext.DECIMAL64);
		}
		return contrErogabile;
	}
	
	public String getContrErogabileFormatted()
	{
		BigDecimal contrErogabile = getContrErogabile();
		if(contrErogabile!=null)
		{
			return IuffiUtils.FORMAT.formatDecimal2(contrErogabile);
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
