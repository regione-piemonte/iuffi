package it.csi.iuffi.iuffiweb.dto.danniFauna;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ParticelleFaunaDTO implements ILoggable
{
	private static final long serialVersionUID = -7177237193060599946L;
	private long idParticelleFauna;
	private Long extIdUtilizzoDichiarato;
	private BigDecimal superficieDanneggiata;
	private Long idDannoFauna;
	private String flagUtilizzoSec;
	private Long idUtilizzoRiscontrato;

	public long getIdParticelleFauna()
	{
		return idParticelleFauna;
	}
	public void setIdParticelleFauna(long idParticelleFauna)
	{
		this.idParticelleFauna = idParticelleFauna;
	}
	public Long getExtIdUtilizzoDichiarato()
	{
		return extIdUtilizzoDichiarato;
	}
	public void setExtIdUtilizzoDichiarato(Long extIdUtilizzoDichiarato)
	{
		this.extIdUtilizzoDichiarato = extIdUtilizzoDichiarato;
	}
	public BigDecimal getSuperficieDanneggiata()
	{
		return superficieDanneggiata;
	}
	public void setSuperficieDanneggiata(BigDecimal superficieDanneggiata)
	{
		this.superficieDanneggiata = superficieDanneggiata;
	}
	public Long getIdDannoFauna()
	{
		return idDannoFauna;
	}
	public void setIdDannoFauna(Long idDannoFauna)
	{
		this.idDannoFauna = idDannoFauna;
	}
	
  public String getFlagUtilizzoSec()
  {
    return flagUtilizzoSec;
  }

  public void setFlagUtilizzoSec(String flagUtilizzoSec)
  {
    this.flagUtilizzoSec = flagUtilizzoSec;
  }
  public Long getIdUtilizzoRiscontrato()
  {
    return idUtilizzoRiscontrato;
  }
  public void setIdUtilizzoRiscontrato(Long idUtilizzoRiscontrato)
  {
    this.idUtilizzoRiscontrato = idUtilizzoRiscontrato;
  }
}
