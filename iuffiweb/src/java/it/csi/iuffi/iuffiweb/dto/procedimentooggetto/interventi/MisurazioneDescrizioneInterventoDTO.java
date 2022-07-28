package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

public class MisurazioneDescrizioneInterventoDTO extends InfoMisurazioneIntervento
{
	/** serialVersionUID */
	private static final long serialVersionUID = 3821716346832411514L;
	private long idMisurazioneIntervento;
	private long idDescrizioneIntervento;

	public long getIdMisurazioneIntervento()
	{
		return idMisurazioneIntervento;
	}

	public void setIdMisurazioneIntervento(long idMisurazioneIntervento)
	{
		this.idMisurazioneIntervento = idMisurazioneIntervento;
	}

	public long getIdDescrizioneIntervento()
	{
		return idDescrizioneIntervento;
	}

	public void setIdDescrizioneIntervento(long idDescrizioneIntervento)
	{
		this.idDescrizioneIntervento = idDescrizioneIntervento;
	}

}
