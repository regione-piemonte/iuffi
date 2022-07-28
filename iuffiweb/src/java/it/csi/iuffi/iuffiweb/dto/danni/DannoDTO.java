package it.csi.iuffi.iuffiweb.dto.danni;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DannoDTO implements ILoggable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5705927438610867066L;
	private long idDanno;
	private String descrizione;
	private Long idUnitaMisura;
	private String nomeTabella;
	private String codice;
	public long getIdDanno()
	{
		return idDanno;
	}
	public void setIdDanno(long idDanno)
	{
		this.idDanno = idDanno;
	}
	public String getDescrizione()
	{
		return descrizione;
	}
	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}
	public Long getIdUnitaMisura()
	{
		return idUnitaMisura;
	}
	public void setIdUnitaMisura(Long idUnitaMisura)
	{
		this.idUnitaMisura = idUnitaMisura;
	}
	public String getNomeTabella()
	{
		return nomeTabella;
	}
	public void setNomeTabella(String nomeTabella)
	{
		this.nomeTabella = nomeTabella;
	}
	public String getCodice()
	{
		return codice;
	}
	public void setCodice(String codice)
	{
		this.codice = codice;
	}
	
	
}
