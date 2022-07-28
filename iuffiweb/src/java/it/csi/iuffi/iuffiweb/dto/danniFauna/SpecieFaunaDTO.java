package it.csi.iuffi.iuffiweb.dto.danniFauna;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SpecieFaunaDTO implements ILoggable
{
	private static final long serialVersionUID = -4646722311728715530L;
	private long idSpecieFauna;
	private String descrizione;
	private Date dataInizioValidita;
	private Date dataFineValidita;
	public long getIdSpecieFauna()
	{
		return idSpecieFauna;
	}
	public void setIdSpecieFauna(long idSpecieFauna)
	{
		this.idSpecieFauna = idSpecieFauna;
	}
	public String getDescrizione()
	{
		return descrizione;
	}
	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}
	public Date getDataInizioValidita()
	{
		return dataInizioValidita;
	}
	public void setDataInizioValidita(Date dataInizioValidita)
	{
		this.dataInizioValidita = dataInizioValidita;
	}
	public Date getDataFineValidita()
	{
		return dataFineValidita;
	}
	public void setDataFineValidita(Date dataFineValidita)
	{
		this.dataFineValidita = dataFineValidita;
	}
}
