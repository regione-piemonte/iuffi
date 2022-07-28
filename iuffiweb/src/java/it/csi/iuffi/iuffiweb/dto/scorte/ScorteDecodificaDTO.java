package it.csi.iuffi.iuffiweb.dto.scorte;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ScorteDecodificaDTO implements ILoggable
{

	private static final long serialVersionUID = 1L;
	private long idScorta;
	private String descrizione;
	private Long idUnitaMisura;
	public long getIdScorta()
	{
		return idScorta;
	}
	public void setIdScorta(long idScorta)
	{
		this.idScorta = idScorta;
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
}
