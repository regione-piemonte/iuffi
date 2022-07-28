package it.csi.iuffi.iuffiweb.dto.danni;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.integration.QuadroIuffiDAO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DanniDTO implements ILoggable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6953036195439338566L;
	private long idDannoAtm;
	private int idDanno;
	private String tipoDanno;
	private long idElemento;
	private String denominazione;
	private BigDecimal quantita;
	private Long idUnitaMisura;
	
	private String descUnitaMisura;
	private BigDecimal importo;
	private String descrizione;
	private Long extIdEntitaDanneggiata;
	private long idProcedimentoOggetto;
	private String descEntitaDanneggiata;
	private long progressivo;
	private String nomeTabella;
	
	private List<Integer> listDanniEquivalentiConduzioni = QuadroIuffiDAO.getListDanniEquivalenti(IuffiConstants.DANNI.TERRENI_RIPRISTINABILI);
	
	public long getIdDannoAtm()
	{
		return idDannoAtm;
	}
	public void setIdDannoAtm(long idDannoAtm)
	{
		this.idDannoAtm = idDannoAtm;
	}
	public int getIdDanno()
	{
		return idDanno;
	}
	public void setIdDanno(int idDanno)
	{
		this.idDanno = idDanno;
	}
	public String getTipoDanno()
	{
		return tipoDanno;
	}
	public void setTipoDanno(String tipoDanno)
	{
		this.tipoDanno = tipoDanno;
	}
	
	public long getIdElemento()
	{
		return idElemento;
	}
	public void setIdElemento(long idElemento)
	{
		this.idElemento = idElemento;
	}
	public String getDenominazione()
	{
		return denominazione;
	}
	public void setDenominazione(String denominazione)
	{
		this.denominazione = denominazione;
	}
	public BigDecimal getQuantita()
	{
		return quantita;
	}
	public void setQuantita(BigDecimal quantita)
	{
		this.quantita = quantita;
	}
	public Long getIdUnitaMisura()
	{
		return idUnitaMisura;
	}

	public String getQuantitaUnitaMisuraFormatter()
	{
		if(listDanniEquivalentiConduzioni.contains(idDanno)
				|| idDanno == IuffiConstants.DANNI.ALTRO)
		{
			return IuffiUtils.FORMAT.formatDecimal4(quantita) + " (" + this.descUnitaMisura + ")";
		}
		else
		{
			return IuffiUtils.FORMAT.formatDecimal2(quantita) + " (" + this.descUnitaMisura + ")";
		}
	}
	public void setIdUnitaMisura(Long idUnitaMisura)
	{
		this.idUnitaMisura = idUnitaMisura;
	}
	public String getDescUnitaMisura()
	{
		return descUnitaMisura;
	}
	public void setDescUnitaMisura(String descUnitaMisura)
	{
		this.descUnitaMisura = descUnitaMisura;
	}
	public BigDecimal getImporto()
	{
		return importo;
	}
	public void setImporto(BigDecimal importo)
	{
		this.importo = importo;
	}
	public String getImportoFormatter()
	{
		return IuffiUtils.FORMAT.formatGenericNumber(importo, 2, true) + " &euro;";
	}
	public String getImportoFormattedStampa()
	{
		return IuffiUtils.FORMAT.formatGenericNumber(importo, 2, true) + " €";
	}
	public String getDescrizione()
	{
		return descrizione;
	}
	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}
	public Long getExtIdEntitaDanneggiata()
	{
		return extIdEntitaDanneggiata;
	}
	public void setExtIdEntitaDanneggiata(Long extIdEntitaDanneggiata)
	{
		this.extIdEntitaDanneggiata = extIdEntitaDanneggiata;
	}
	public long getIdProcedimentoOggetto()
	{
		return idProcedimentoOggetto;
	}
	public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
	{
		this.idProcedimentoOggetto = idProcedimentoOggetto;
	}
	public String getDescEntitaDanneggiata()
	{
		return descEntitaDanneggiata;
	}
	public void setDescEntitaDanneggiata(String descEntitaDanneggiata)
	{
		this.descEntitaDanneggiata = descEntitaDanneggiata;
	}
	
	public long getProgressivo()
	{
		return progressivo;
	}
	public void setProgressivo(long progressivo)
	{
		this.progressivo = progressivo;
	}
	public boolean getIsModificaQuantitaDisabled()
	{
		boolean disabled=false;
		switch(idDanno)
		{
		case IuffiConstants.DANNI.MACCHINA_AGRICOLA:
		case IuffiConstants.DANNI.ATTREZZATURA:
			disabled=true;
			break;
		}
		return disabled;
	}

	public String getDenominazioneFormatted()
	{
		if(denominazione == null)
		{
			return null;
		}
		return denominazione.replaceAll("<br/>", "\r\n");
	}
	public String getNomeTabella()
	{
		return nomeTabella;
	}
	public void setNomeTabella(String nomeTabella)
	{
		this.nomeTabella = nomeTabella;
	}
	
	
	
	
}
