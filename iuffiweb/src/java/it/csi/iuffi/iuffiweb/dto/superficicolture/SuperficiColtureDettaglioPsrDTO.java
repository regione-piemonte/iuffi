package it.csi.iuffi.iuffiweb.dto.superficicolture;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SuperficiColtureDettaglioPsrDTO implements ILoggable
{

	private static final long serialVersionUID = 6585785000899219880L;
	private long idSuperficieColtura;
	private BigDecimal produzioneHaMedia;
	private BigDecimal produzioneHaMin;
	private BigDecimal produzioneHaMax;
	private BigDecimal produzioneHa;
	private BigDecimal giornateLavorate;
	private BigDecimal giornateLavorateMedie;
	private BigDecimal giornateLavorateMin;
	private BigDecimal giornateLavorateMax;
	private BigDecimal giornateLavorativeTot;
	private BigDecimal giornateLavorativeTotCalc;
	private BigDecimal ufTot;
	private BigDecimal ufTotCalc;
	private BigDecimal qliReimpiegati;
	private BigDecimal ufReimpiegate;
	private BigDecimal plvTotQ;
	private BigDecimal plvTotQCalc;
	private BigDecimal prezzo;
	private BigDecimal prezzoMedio;
	private BigDecimal prezzoMin;
	private BigDecimal prezzoMax;
	private BigDecimal plvTotDich;
	private BigDecimal plvTotDichCalc;
	
	private BigDecimal ufProdotte;
	private BigDecimal sumSuperficieUtilizzata;
	private BigDecimal prodTotale;
	private BigDecimal prodTotaleCalc;
	private String note;
	
	public long getIdSuperficieColtura()
	{
		return idSuperficieColtura;
	}
	public void setIdSuperficieColtura(long idSuperficieColtura)
	{
		this.idSuperficieColtura = idSuperficieColtura;
	}
	
	public BigDecimal getProduzioneHaMedia()
	{
		return produzioneHaMedia;
	}
	public String getProduzioneHaMediaFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(produzioneHaMedia);
	}
	public void setProduzioneHaMedia(BigDecimal produzioneHaMedia)
	{
		this.produzioneHaMedia = produzioneHaMedia;
	}
	
	public BigDecimal getProduzioneHa()
	{
		return produzioneHa;
	}
	public String getProduzioneHaFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(produzioneHa);
	}
	public void setProduzioneHa(BigDecimal produzioneHa)
	{
		this.produzioneHa = produzioneHa;
	}
	
	public BigDecimal getGiornateLavorate()
	{
		return giornateLavorate;
	}
	public String getGiornateLavorateFormatted()
	{
		return IuffiUtils.FORMAT.formatGenericNumber(giornateLavorate, 1, true, true);
	}
	public void setGiornateLavorate(BigDecimal giornateLavorate)
	{
		this.giornateLavorate = giornateLavorate;
	}
	
	public BigDecimal getGiornateLavorateMedie()
	{
		return giornateLavorateMedie;
	}
	public String getGiornateLavorateMedieFormatted()
	{
		return IuffiUtils.FORMAT.formatGenericNumber(giornateLavorateMedie, 1, true, true);
	}	
	public void setGiornateLavorateMedie(BigDecimal giornateLavorateMedie)
	{
		this.giornateLavorateMedie = giornateLavorateMedie;
	}
	
	public BigDecimal getQliReimpiegati()
	{
		return qliReimpiegati;
	}
	public String getQliReimpiegatiFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(qliReimpiegati);
	}	
	public void setQliReimpiegati(BigDecimal qliReimpiegati)
	{
		this.qliReimpiegati = qliReimpiegati;
	}
	
	public BigDecimal getUfReimpiegate()
	{
		return ufReimpiegate;
	}
	public String getUfReimpiegateFormatted()
	{
		return IuffiUtils.FORMAT.formatGenericNumber(ufReimpiegate, 0, false, true);
	}		
	public void setUfReimpiegate(BigDecimal ufReimpiegate)
	{
		this.ufReimpiegate = ufReimpiegate;
	}
	
	public BigDecimal getPrezzo()
	{
		return prezzo;
	}
	public String getPrezzoFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(prezzo);
	}	
	public void setPrezzo(BigDecimal prezzo)
	{
		this.prezzo = prezzo;
	}
	
	public BigDecimal getPrezzoMedio()
	{
		return prezzoMedio;
	}
	public String getPrezzoMedioFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(prezzoMedio);
	}	
	public void setPrezzoMedio(BigDecimal prezzoMedio)
	{
		this.prezzoMedio = prezzoMedio;
	}
	
	public BigDecimal getUfProdotte()
	{
		return ufProdotte;
	}
	public void setUfProdotte(BigDecimal ufProdotte)
	{
		this.ufProdotte = ufProdotte;
	}
	public BigDecimal getSumSuperficieUtilizzata()
	{
		return sumSuperficieUtilizzata;
	}
	public void setSumSuperficieUtilizzata(BigDecimal sumSuperficieUtilizzata)
	{
		this.sumSuperficieUtilizzata = sumSuperficieUtilizzata;
	}
	public BigDecimal getPlvTotQ()
	{
		return plvTotQ;
	}
	public String getPlvTotQFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(plvTotQ);
	}
	public void setPlvTotQ(BigDecimal plvTotQ)
	{
		this.plvTotQ = plvTotQ;
	}
	
	public BigDecimal getPlvTotQCalc()
	{
		return plvTotQCalc;
	}
	public String getPlvTotQCalcFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(plvTotQCalc);
	}
	public void setPlvTotQCalc(BigDecimal plvTotQCalc)
	{
		this.plvTotQCalc = plvTotQCalc;
	}
	
	public BigDecimal getProdTotale()
	{
		return prodTotale;
	}
	public String getProdTotaleFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(prodTotale);
	}
	
	public void setProdTotale(BigDecimal prodTotale)
	{
		this.prodTotale = prodTotale;
	}
	public BigDecimal getProdTotaleCalc()
	{
		return prodTotaleCalc;
	}
	public String getProdTotaleCalcFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(prodTotaleCalc);
	}
	public void setProdTotaleCalc(BigDecimal prodTotaleCalc)
	{
		this.prodTotaleCalc = prodTotaleCalc;
	}
	public BigDecimal getGiornateLavorativeTot()
	{
		return giornateLavorativeTot;
	}
	public String getGiornateLavorativeTotFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(giornateLavorativeTot);
	}	
	public void setGiornateLavorativeTot(BigDecimal giornateLavorativeTot)
	{
		this.giornateLavorativeTot = giornateLavorativeTot;
	}
	
	public BigDecimal getGiornateLavorativeTotCalc()
	{
		return giornateLavorativeTotCalc;
	}
	public String getGiornateLavorativeTotCalcFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(giornateLavorativeTotCalc);
	}
	public void setGiornateLavorativeTotCalc(BigDecimal giornateLavorativeTotCalc)
	{
		this.giornateLavorativeTotCalc = giornateLavorativeTotCalc;
	}
	
	public BigDecimal getUfTot()
	{
		return ufTot;
	}
	public String getUfTotFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(ufTot);
	}
	public void setUfTot(BigDecimal ufTot)
	{
		this.ufTot = ufTot;
	}
	
	public BigDecimal getUfTotCalc()
	{
		return ufTotCalc;
	}
	public String getUfTotCalcFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(ufTotCalc);
	}
	public void setUfTotCalc(BigDecimal ufTotCalc)
	{
		this.ufTotCalc = ufTotCalc;
	}
	
	public BigDecimal getPlvTotDich()
	{
		return plvTotDich;
	}
	public String getPlvTotDichFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(plvTotDich);
	}
	public void setPlvTotDich(BigDecimal plvTotDich)
	{
		this.plvTotDich = plvTotDich;
	}
	
	public BigDecimal getPlvTotDichCalc()
	{
		return plvTotDichCalc;
	}
	public String getPlvTotDichCalcFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(plvTotDichCalc);
	}
	public void setPlvTotDichCalc(BigDecimal plvTotDichCalc)
	{
		this.plvTotDichCalc = plvTotDichCalc;
	}
	public BigDecimal getProduzioneHaMin()
	{
		return produzioneHaMin;
	}
	public void setProduzioneHaMin(BigDecimal produzioneHaMin)
	{
		this.produzioneHaMin = produzioneHaMin;
	}
	public BigDecimal getProduzioneHaMax()
	{
		return produzioneHaMax;
	}
	public void setProduzioneHaMax(BigDecimal produzioneHaMax)
	{
		this.produzioneHaMax = produzioneHaMax;
	}
	public BigDecimal getGiornateLavorateMin()
	{
		return giornateLavorateMin;
	}
	public void setGiornateLavorateMin(BigDecimal giornateLavorateMin)
	{
		this.giornateLavorateMin = giornateLavorateMin;
	}
	public BigDecimal getGiornateLavorateMax()
	{
		return giornateLavorateMax;
	}
	public void setGiornateLavorateMax(BigDecimal giornateLavorateMax)
	{
		this.giornateLavorateMax = giornateLavorateMax;
	}
	public BigDecimal getPrezzoMin()
	{
		return prezzoMin;
	}
	public void setPrezzoMin(BigDecimal prezzoMin)
	{
		this.prezzoMin = prezzoMin;
	}
	public BigDecimal getPrezzoMax()
	{
		return prezzoMax;
	}
	public void setPrezzoMax(BigDecimal prezzoMax)
	{
		this.prezzoMax = prezzoMax;
	}
	
	public BigDecimal getReimpieghiQuantita()
	{
		BigDecimal zero = new BigDecimal("0");
		if(qliReimpiegati != null && qliReimpiegati.compareTo(zero)>0)
		{
			return qliReimpiegati;
		}
		else if(ufReimpiegate != null && ufReimpiegate.compareTo(zero)>0)
		{
			return ufReimpiegate;
		}
		else
		{
			return new BigDecimal("0");
		}
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
}
