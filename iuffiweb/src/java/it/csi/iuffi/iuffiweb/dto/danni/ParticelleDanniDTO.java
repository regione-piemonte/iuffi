package it.csi.iuffi.iuffiweb.dto.danni;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ParticelleDanniDTO implements ILoggable
{
	private static final long serialVersionUID = 8632415387388345485L;
	private long idUtilizzoDichiarato;
	private String istatComune;
	private String sezione;
	private String descSezione;
	private String descComune;
	private String comune;
	private long foglio;
	private long particella;
	private String subalterno;
	private BigDecimal supCatastale;
	private String occupazioneSuolo;
	private String destinazione;
	private String uso;
	private String qualita;
	private String varieta;
	private String flagArboreo; 
	private BigDecimal superficieUtilizzata;
	private String descProvincia;
	private String descTipoUtilizzo;
	private String descZonaAltimetrica;
	
	private Long idDannoAtm;
	private Long progressivo;
	
	private BigDecimal superficieCoinvolta;
	private String superficieCoinvoltaStr;
	private boolean errorSuperficieCoinvolta;
	private String descErrorSuperficieCoinvolta;
	
	private String occupazioneSuoloSecondario;
	private String destinazioneSecondario;
	private String usoSecondario;
	private String qualitaSecondario;
	private BigDecimal supUtilizzataSecondaria;
	private String colturaSecondaria; 
	
	private Long idUtilizzoRiscontrato;
	
	public String getId()
	{
		return Long.toString(idUtilizzoDichiarato);
	}
	
	public long getIdUtilizzoDichiarato()
	{
		return idUtilizzoDichiarato;
	}

	public void setIdUtilizzoDichiarato(long idUtilizzoDichiarato)
	{
		this.idUtilizzoDichiarato = idUtilizzoDichiarato;
	}
	
	public String getFlagArboreo()
	{
		return flagArboreo;
	}

	public void setFlagArboreo(String flagArboreo)
	{
		this.flagArboreo = flagArboreo;
	}

	public String getIstatComune()
	{
		return istatComune;
	}
	public void setIstatComune(String istatComune)
	{
		this.istatComune = istatComune;
	}
	public String getComune()
	{
		return comune;
	}
	public void setComune(String comune)
	{
		this.comune = comune;
	}
	
	public String getDescComune()
	{
		return descComune;
	}

	public void setDescComune(String descComune)
	{
		this.descComune = descComune;
	}

	public String getSezione()
	{
		return sezione;
	}
	public void setSezione(String sezione)
	{
		this.sezione = sezione;
	}
	
	public String getDescSezione()
	{
		return descSezione;
	}
	public void setDescSezione(String descSezione)
	{
		this.descSezione = descSezione;
	}
	public long getFoglio()
	{
		return foglio;
	}
	public void setFoglio(long foglio)
	{
		this.foglio = foglio;
	}
	public long getParticella()
	{
		return particella;
	}
	public void setParticella(long particella)
	{
		this.particella = particella;
	}
	public String getSubalterno()
	{
		return subalterno;
	}
	public void setSubalterno(String subalterno)
	{
		this.subalterno = subalterno;
	}
	public BigDecimal getSupCatastale()
	{
		return supCatastale;
	}
	public void setSupCatastale(BigDecimal supCatastale)
	{
		this.supCatastale = supCatastale;
	}
	public String getOccupazioneSuolo()
	{
		return occupazioneSuolo;
	}
	public void setOccupazioneSuolo(String occupazioneSuolo)
	{
		this.occupazioneSuolo = occupazioneSuolo;
	}
	public String getDestinazione()
	{
		return destinazione;
	}
	public void setDestinazione(String destinazione)
	{
		this.destinazione = destinazione;
	}
	public String getUso()
	{
		return uso;
	}
	public void setUso(String uso)
	{
		this.uso = uso;
	}
	public String getQualita()
	{
		return qualita;
	}
	public void setQualita(String qualita)
	{
		this.qualita = qualita;
	}
	public String getVarieta()
	{
		return varieta;
	}
	public void setVarieta(String varieta)
	{
		this.varieta = varieta;
	}
	public BigDecimal getSuperficieUtilizzata()
	{
		return superficieUtilizzata;
	}
	public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
	{
		this.superficieUtilizzata = superficieUtilizzata;
	}

	public String getDescProvincia()
	{
		return descProvincia;
	}

	public void setDescProvincia(String descProvincia)
	{
		this.descProvincia = descProvincia;
	}

	public String getDescTipoUtilizzo()
	{
		return descTipoUtilizzo;
	}

	public void setDescTipoUtilizzo(String descTipoUtilizzo)
	{
		this.descTipoUtilizzo = descTipoUtilizzo;
	}

	public Long getIdDannoAtm()
	{
		return idDannoAtm;
	}

	public void setIdDannoAtm(Long idDannoAtm)
	{
		this.idDannoAtm = idDannoAtm;
	}

	public Long getProgressivo()
	{
		return progressivo;
	}

	public void setProgressivo(Long progressivo)
	{
		this.progressivo = progressivo;
	}

	public String getSupCatastaleFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(supCatastale);
	}

	public String getSuperficieUtilizzataFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal4(superficieUtilizzata);
	}

	public String getDescZonaAltimetrica()
	{
		return descZonaAltimetrica;
	}

	public void setDescZonaAltimetrica(String descZonaAltimetrica)
	{
		this.descZonaAltimetrica = descZonaAltimetrica;
	}

	public BigDecimal getSuperficieCoinvolta()
	{
		return superficieCoinvolta;
	}

	public void setSuperficieCoinvolta(BigDecimal superficieCoinvolta)
	{
		this.superficieCoinvolta = superficieCoinvolta;
	}

	public String getSuperficieCoinvoltaStr()
	{
		return superficieCoinvoltaStr;
	}

	public void setSuperficieCoinvoltaStr(String superficieCoinvoltaStr)
	{
		this.superficieCoinvoltaStr = superficieCoinvoltaStr;
	}

	public boolean isErrorSuperficieCoinvolta()
	{
		return errorSuperficieCoinvolta;
	}

	public void setErrorSuperficieCoinvolta(boolean errorSuperficieCoinvolta)
	{
		this.errorSuperficieCoinvolta = errorSuperficieCoinvolta;
	}

	public String getDescErrorSuperficieCoinvolta()
	{
		return descErrorSuperficieCoinvolta;
	}

	public void setDescErrorSuperficieCoinvolta(String descErrorSuperficieCoinvolta)
	{
		this.descErrorSuperficieCoinvolta = descErrorSuperficieCoinvolta;
	}
	
  public String getOccupazioneSuoloSecondario()
  {
    return occupazioneSuoloSecondario;
  }

  public void setOccupazioneSuoloSecondario(String occupazioneSuoloSecondario)
  {
    this.occupazioneSuoloSecondario = occupazioneSuoloSecondario;
  }

  public String getDestinazioneSecondario()
  {
    return destinazioneSecondario;
  }

  public void setDestinazioneSecondario(String destinazioneSecondario)
  {
    this.destinazioneSecondario = destinazioneSecondario;
  }

  public String getUsoSecondario()
  {
    return usoSecondario;
  }

  public void setUsoSecondario(String usoSecondario)
  {
    this.usoSecondario = usoSecondario;
  }

  public String getQualitaSecondario()
  {
    return qualitaSecondario;
  }

  public void setQualitaSecondario(String qualitaSecondario)
  {
    this.qualitaSecondario = qualitaSecondario;
  }

  public BigDecimal getSupUtilizzataSecondaria()
  {
    return supUtilizzataSecondaria;
  }

  public void setSupUtilizzataSecondaria(BigDecimal supUtilizzataSecondaria)
  {
    this.supUtilizzataSecondaria = supUtilizzataSecondaria;
  }

  public String getColturaSecondaria()
  {
    return colturaSecondaria;
  }

  public void setColturaSecondaria(String colturaSecondaria)
  {
    this.colturaSecondaria = colturaSecondaria;
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
