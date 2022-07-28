package it.csi.iuffi.iuffiweb.dto.prestitiagrari;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class PrestitiAgrariDTO implements ILoggable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7333968660942102426L;
	private long idPrestitiAgrari;
	private Date dataScadenza;
	private String finalitaPrestito;
	private BigDecimal importo;
	private String istitutoErogante;
	private long progressivo;
	private long idProcedimentoOggetto;
	
	public long getIdPrestitiAgrari()
	{
		return idPrestitiAgrari;
	}
	public void setIdPrestitiAgrari(long idPrestitiAgrari)
	{
		this.idPrestitiAgrari = idPrestitiAgrari;
	}
	public Date getDataScadenza()
	{
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza)
	{
		this.dataScadenza = dataScadenza;
	}
	public String getDataScadenzaFormatted()
	{
		return IuffiUtils.DATE.formatDate(this.dataScadenza);
	}
	public String getFinalitaPrestito()
	{
		return finalitaPrestito;
	}
	public void setFinalitaPrestito(String finalitaPrestito)
	{
		this.finalitaPrestito = finalitaPrestito;
	}
	public BigDecimal getImporto()
	{
		return importo;
	}
	public void setImporto(BigDecimal importo)
	{
		this.importo = importo;
	}
	public String getIstitutoErogante()
	{
		return istitutoErogante;
	}
	public void setIstitutoErogante(String istitutoErogante)
	{
		this.istitutoErogante = istitutoErogante;
	}
	public long getProgressivo()
	{
		return progressivo;
	}
	public void setProgressivo(long progressivo)
	{
		this.progressivo = progressivo;
	}
	public long getIdProcedimentoOggetto()
	{
		return idProcedimentoOggetto;
	}
	public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
	{
		this.idProcedimentoOggetto = idProcedimentoOggetto;
	}
	
	
	
}
