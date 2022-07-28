package it.csi.iuffi.iuffiweb.dto.assicurazionicolture;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class AssicurazioniColtureDTO implements ILoggable
{
	private static final long serialVersionUID = 1L;
	//IUF_T_ASSICURAZIONI_COLTURE
	private long idAssicurazioniColture;
	private long idProcedimentoOggetto;
	private Long idConsorzioDifesa;
	private String nomeEntePrivato;
	private String numeroSocioPolizza;
	private BigDecimal importoPremio;
	private BigDecimal importoAssicurato;
	private BigDecimal importoRimborso;
	
	//IUF_D_CONSORZIO_DIFESA
	private String extIdProvincia;
	private String descrizioneProvincia;
	private String descrizioneConsorzio;
	
	
	public String getNomeConsorzioEntePrivato()
	{
		if(getDescrizioneConsorzio() == null || getDescrizioneConsorzio().equals(""))
		{
			return getNomeEntePrivato();
		}
		else
		{
			return getDescrizioneConsorzio();
		}
	}
	
	public long getIdAssicurazioniColture()
	{
		return idAssicurazioniColture;
	}
	public void setIdAssicurazioniColture(long idAssicurazioniColture)
	{
		this.idAssicurazioniColture = idAssicurazioniColture;
	}
	public long getIdProcedimentoOggetto()
	{
		return idProcedimentoOggetto;
	}
	public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
	{
		this.idProcedimentoOggetto = idProcedimentoOggetto;
	}
	public Long getIdConsorzioDifesa()
	{
		return idConsorzioDifesa;
	}
	public void setIdConsorzioDifesa(Long idConsorzioDifesa)
	{
		this.idConsorzioDifesa = idConsorzioDifesa;
	}
	public String getNomeEntePrivato()
	{
		return nomeEntePrivato;
	}
	public void setNomeEntePrivato(String nomeEntePrivato)
	{
		this.nomeEntePrivato = nomeEntePrivato;
	}
	public String getNumeroSocioPolizza()
	{
		return numeroSocioPolizza;
	}
	public void setNumeroSocioPolizza(String numeroSocioPolizza)
	{
		this.numeroSocioPolizza = numeroSocioPolizza;
	}
	public BigDecimal getImportoPremio()
	{
		return importoPremio;
	}
	public String getImportoPremioFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(importoPremio);
	}

	public void setImportoPremio(BigDecimal importoPremio)
	{
		this.importoPremio = importoPremio;
	}
	public BigDecimal getImportoAssicurato()
	{
		return importoAssicurato;
	}
	public String getImportoAssicuratoFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(importoAssicurato);
	}
	public void setImportoAssicurato(BigDecimal importoAssicurato)
	{
		this.importoAssicurato = importoAssicurato;
	}
	public BigDecimal getImportoRimborso()
	{
		return importoRimborso;
	}
	public String getImportoRimborsoFormatted()
	{
		return IuffiUtils.FORMAT.formatDecimal2(importoRimborso);
	}
	public void setImportoRimborso(BigDecimal importoRimborso)
	{
		this.importoRimborso = importoRimborso;
	}
	public String getExtIdProvincia()
	{
		return extIdProvincia;
	}
	public void setExtIdProvincia(String extIdProvincia)
	{
		this.extIdProvincia = extIdProvincia;
	}
	public String getDescrizioneProvincia()
	{
		return descrizioneProvincia;
	}
	public void setDescrizioneProvincia(String descrizioneProvincia)
	{
		this.descrizioneProvincia = descrizioneProvincia;
	}
	public String getDescrizioneConsorzio()
	{
		return descrizioneConsorzio;
	}
	public void setDescrizioneConsorzio(String descrizioneConsorzio)
	{
		this.descrizioneConsorzio = descrizioneConsorzio;
	}
}
