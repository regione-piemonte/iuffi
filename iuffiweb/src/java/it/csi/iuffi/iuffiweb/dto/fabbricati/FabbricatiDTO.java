package it.csi.iuffi.iuffiweb.dto.fabbricati;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class FabbricatiDTO implements ILoggable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long idUte;
	private long idFabbricato;
	private String descrizioneComune;
	private String siglaProvincia;
	private String indirizzo;
    private String tipoFabbricato;
    private String tipologia;
    private BigDecimal dimensione;
    private BigDecimal superficie;
    private String unitaMisuraTipologiaFabbr;
   
    private String denominazione;
    private BigDecimal larghezza;
    private BigDecimal lunghezza;
    private BigDecimal altezza;
    private Long annoCostruzione;
    private Long utmX,utmY;
    private String note;
    private Date dataInizioValFabbr;
    private Date dataFineValFabbr;
    
    
    public long getIdUte()
	{
		return idUte;
	}
	public void setIdUte(long idUte)
	{
		this.idUte = idUte;
	}
	
	public long getIdFabbricato()
	{
		return idFabbricato;
	}
	public void setIdFabbricato(long idFabbricato)
	{
		this.idFabbricato = idFabbricato;
	}
	public String getDescrizioneComune()
	{
		return descrizioneComune;
	}
	public void setDescrizioneComune(String descrizioneComune)
	{
		this.descrizioneComune = descrizioneComune;
	}
	
	public String getSiglaProvincia()
	{
		return siglaProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia)
	{
		this.siglaProvincia = siglaProvincia;
	}
	public String getIndirizzo()
	{
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo)
	{
		this.indirizzo = indirizzo;
	}
	public String getTipoFabbricato()
	{
		return tipoFabbricato;
	}
	public void setTipoFabbricato(String tipoFabbricato)
	{
		this.tipoFabbricato = tipoFabbricato;
	}
	public String getTipologia()
	{
		return tipologia;
	}
	public void setTipologia(String tipologia)
	{
		this.tipologia = tipologia;
	}
	public BigDecimal getDimensione()
	{
		return dimensione;
	}
	public void setDimensione(BigDecimal dimensione)
	{
		this.dimensione = dimensione;
	}
	
	public BigDecimal getSuperficie()
	{
		return superficie;
	}
	public String getSuperficieFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(superficie);
	}
	public void setSuperficie(BigDecimal superficie)
	{
		this.superficie = superficie;
	}
	public String getUnitaMisuraTipologiaFabbr()
	{
		return unitaMisuraTipologiaFabbr;
	}
	public void setUnitaMisuraTipologiaFabbr(String unitaMisuraTipologiaFabbr)
	{
		this.unitaMisuraTipologiaFabbr = unitaMisuraTipologiaFabbr;
	}
	public String getUte()
	{
		return descrizioneComune + " (" + siglaProvincia + "), " + indirizzo;
	}
	public String getDimensioneFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(dimensione) + " " + unitaMisuraTipologiaFabbr;
	}
	public BigDecimal getLarghezza()
	{
		return larghezza;
	}
	public void setLarghezza(BigDecimal larghezza)
	{
		this.larghezza = larghezza;
	}
	public BigDecimal getLunghezza()
	{
		return lunghezza;
	}
	public void setLunghezza(BigDecimal lunghezza)
	{
		this.lunghezza = lunghezza;
	}
	public BigDecimal getAltezza()
	{
		return altezza;
	}
	public void setAltezza(BigDecimal altezza)
	{
		this.altezza = altezza;
	}
	public Long getAnnoCostruzione()
	{
		return annoCostruzione;
	}
	public void setAnnoCostruzione(Long annoCostruzione)
	{
		this.annoCostruzione = annoCostruzione;
	}
	public Long getUtmX()
	{
		return utmX;
	}
	public void setUtmX(Long utmX)
	{
		this.utmX = utmX;
	}
	public Long getUtmY()
	{
		return utmY;
	}
	public void setUtmY(Long utmY)
	{
		this.utmY = utmY;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public Date getDataInizioValFabbr()
	{
		return dataInizioValFabbr;
	}
	public void setDataInizioValFabbr(Date dataInizioValFabbr)
	{
		this.dataInizioValFabbr = dataInizioValFabbr;
	}
	public Date getDataFineValFabbr()
	{
		return dataFineValFabbr;
	}
	public void setDataFineValFabbr(Date dataFineValFabbr)
	{
		this.dataFineValFabbr = dataFineValFabbr;
	}
	public String getDenominazione()
	{
		return denominazione;
	}
	public void setDenominazione(String denominazione)
	{
		this.denominazione = denominazione;
	}
	public String getCoordinateUtm()
	{
		try
		{
			return utmX.toString() + "," + utmY.toString();
		}
		catch(NullPointerException e)
		{
			return "";
		}
	}
	
	public String getDataInizioValFabbrFormatted()
	{
		return IuffiUtils.DATE.formatDate(dataInizioValFabbr);
	}
	
	public String getDataFineValFabbrFormatted()
	{
		if(dataFineValFabbr != null)
		{
			return IuffiUtils.DATE.formatDate(dataFineValFabbr);
		}
		else
		{
			return "";
		}
	}
	
	public String getLarghezzaFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(larghezza);
	}
	
	public String getLunghezzaFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(lunghezza);
	}
	
	public String getAltezzaFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(altezza);
	}
	
	public String getDescrizioneFabbricatoDanno()
	{
		return tipoFabbricato + " - " + descrizioneComune + " (" + siglaProvincia +")";
	}
	
	
	
}
