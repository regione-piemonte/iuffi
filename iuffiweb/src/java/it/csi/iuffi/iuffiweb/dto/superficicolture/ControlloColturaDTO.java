package it.csi.iuffi.iuffiweb.dto.superficicolture;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ControlloColturaDTO implements ILoggable
{

	private static final long serialVersionUID = 8874025131618444873L;
	private String bloccante;
	private String descrizioneAnomalia;
	private long idControlloColtura;
	private long idSuperficieColtura;
	
	public String getBloccante()
	{
		return bloccante;
	}
	public void setBloccante(String bloccante)
	{
		this.bloccante = bloccante;
	}
	public String getDescrizioneAnomalia()
	{
		return descrizioneAnomalia;
	}
	public void setDescrizioneAnomalia(String descrizioneAnomalia)
	{
		this.descrizioneAnomalia = descrizioneAnomalia;
	}
	public long getIdControlloColtura()
	{
		return idControlloColtura;
	}
	public void setIdControlloColtura(long idControlloColtura)
	{
		this.idControlloColtura = idControlloColtura;
	}
	public long getIdSuperficieColtura()
	{
		return idSuperficieColtura;
	}
	public void setIdSuperficieColtura(long idSuperficieColtura)
	{
		this.idSuperficieColtura = idSuperficieColtura;
	}
	
}
