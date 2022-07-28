package it.csi.iuffi.iuffiweb.dto.scorte;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ScorteDTO implements ILoggable
{

	private static final long serialVersionUID = 1L;
	private long idProcedimentoOggetto;
	private long idScortaMagazzino;
	private long idScorta;
	private Long idUnitaMisura;
	private String descrizione;
	private BigDecimal quantita;
	private long progressivo;
	private String descUnitaMisura;
	private String descrizioneScorta;

	
	public long getIdProcedimentoOggetto()
	{
		return idProcedimentoOggetto;
	}
	public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
	{
		this.idProcedimentoOggetto = idProcedimentoOggetto;
	}
	public long getIdScortaMagazzino()
	{
		return idScortaMagazzino;
	}
	public void setIdScortaMagazzino(long idScortaMagazzino)
	{
		this.idScortaMagazzino = idScortaMagazzino;
	}
	public String getDescrizione()
	{
		return descrizione;
	}
	
	public String getDescrizionePerStampa()
	{
		if(descrizione == null || descrizione.equals("")) return "-";
		else return descrizione;
	}
	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}
	public BigDecimal getQuantita()
	{
		return quantita;
	}
	public void setQuantita(BigDecimal quantita)
	{
		this.quantita = quantita;
	}
	public String getQuantitaFormatter()
	{
		return IuffiUtils.FORMAT.formatGenericNumber(this.quantita,2,false);
	}
	public String getQuantitaUnitaMisuraFormatter(){
		return getQuantitaFormatter() + " (" + this.descUnitaMisura + ")";
	}
	public long getProgressivo()
	{
		return progressivo;
	}
	public void setProgressivo(long progressivo)
	{
		this.progressivo = progressivo;
	}
	public String getDescUnitaMisura()
	{
		return descUnitaMisura;
	}
	public void setDescUnitaMisura(String descUnitaMisura)
	{
		this.descUnitaMisura = descUnitaMisura;
	}
	public String getDescrizioneScorta()
	{
		return descrizioneScorta;
	}
	public void setDescrizioneScorta(String descrizioneScorta)
	{
		this.descrizioneScorta = descrizioneScorta;
	}
	public long getIdScorta()
	{
		return idScorta;
	}
	public void setIdScorta(long idScorta)
	{
		this.idScorta = idScorta;
	}
	public Long getIdUnitaMisura()
	{
		return idUnitaMisura;
	}
	public void setIdUnitaMisura(Long idUnitaMisura)
	{
		this.idUnitaMisura = idUnitaMisura;
	}
	public String getDescrizioneScortaDanno(){
		return descrizioneScorta + " - " + (descrizione == null ? "" : descrizione + " -" ) + " " + quantita.toString() + " " + descUnitaMisura;
	}
	
}
