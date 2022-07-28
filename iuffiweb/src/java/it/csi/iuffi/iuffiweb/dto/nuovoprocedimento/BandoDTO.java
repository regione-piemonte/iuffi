package it.csi.iuffi.iuffiweb.dto.nuovoprocedimento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.dto.AmmCompetenzaDTO;
import it.csi.iuffi.iuffiweb.dto.FocusAreaDTO;
import it.csi.iuffi.iuffiweb.dto.SettoriDiProduzioneDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class BandoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long            serialVersionUID = 4601339650061084885L;

  private long                         idBando;
  private long                         idBandoOggetto;
  private long                         idLegameGruppoOggetto;
  private long                         idBandoMaster;
  private long                         idTipoLivello;
  private String                       denominazione;
  private String                       flagTitolaritaRegionale;
  private String                       flagDomandaMultipla;
  private String                       flagMaster;
  private String                       descrTipoBando;
  private String                       codiceTipoBando;
  private String                       annoCampagna;
  private String                       descrizione;
  private String                       referenteBando;
  private String                       emailReferenteBando;
  private String                       flagRibassoInterventi;
  private String                       istruzioneSqlFiltro;
  private String                       descrizioneFiltro;
  private Date                         dataInizio;
  private Date                         dataFine;
  private List<FileAllegatoDTO>        allegati;

  private List<LivelloDTO>             livelli;
  private List<SettoriDiProduzioneDTO> elencoSettori;
  private List<FocusAreaDTO>           elencoFocusArea;
  private List<AmmCompetenzaDTO>       amministrazioniCompetenza;

  private String                       flagGrafico;
  private String                       flagReport;
  private boolean                      haveReport       = false;
  private boolean                      haveChart        = false;
  private boolean                      haveGraduatorie  = false;
  
  //evento calamitoso
  private Long						   idEventoCalamitoso;
  private String					   descEventoCalamitoso;
  private Date						   dataEvento;
  
  //PERCENTUALE CONTRIBUTO
  private BigDecimal 					percContributoErogabile;
  private BigDecimal 					percContributoMaxConcessa;
  
  private int 							idProcedimentoAgricolo;

	public long getIdBando()
	{
		return idBando;
	}

	public void setIdBando(long idBando)
	{
		this.idBando = idBando;
	}

	public String getDenominazione()
	{
		return denominazione;
	}

	public void setDenominazione(String denominazione)
	{
		this.denominazione = denominazione;
	}

	public String getIstruzioneSqlFiltro()
	{
		return istruzioneSqlFiltro;
	}

	public void setIstruzioneSqlFiltro(String istruzioneSqlFiltro)
	{
		this.istruzioneSqlFiltro = istruzioneSqlFiltro;
	}

	public String getDescrizioneFiltro()
	{
		return descrizioneFiltro;
	}

	public void setDescrizioneFiltro(String descrizioneFiltro)
	{
		this.descrizioneFiltro = descrizioneFiltro;
	}

	public List<LivelloDTO> getLivelli()
	{
		return livelli;
	}

	public void setLivelli(List<LivelloDTO> livelli)
	{
		this.livelli = livelli;
	}

	public Date getDataInizio()
	{
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio)
	{
		this.dataInizio = dataInizio;
	}

	public Date getDataFine()
	{
		return dataFine;
	}

	public void setDataFine(Date dataFine)
	{
		this.dataFine = dataFine;
	}

	public String getAnnoCampagna()
	{
		return annoCampagna;
	}

	public String getAnnoCampagnaNVL()
	{
		return GenericValidator.isBlankOrNull(annoCampagna) ? "Non specificato" : annoCampagna;
	}

	public String getAnnoCampagnaValNVL()
	{
		return GenericValidator.isBlankOrNull(annoCampagna) ? "0" : annoCampagna;
	}

	public boolean isDefaultChecked()
	{
		return true;
	}

	public void setAnnoCampagna(String annoCampagna)
	{
		this.annoCampagna = annoCampagna;
	}

	public long getIdBandoOggetto()
	{
		return idBandoOggetto;
	}

	public void setIdBandoOggetto(long idBandoOggetto)
	{
		this.idBandoOggetto = idBandoOggetto;
	}

	public long getIdLegameGruppoOggetto()
	{
		return idLegameGruppoOggetto;
	}

	public void setIdLegameGruppoOggetto(long idLegameGruppoOggetto)
	{
		this.idLegameGruppoOggetto = idLegameGruppoOggetto;
	}

	public List<FileAllegatoDTO> getAllegati()
	{
		return allegati;
	}

	public void setAllegati(List<FileAllegatoDTO> allegati)
	{
		this.allegati = allegati;
	}

	public String getDescrizione()
	{
		return descrizione;
	}

	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}

	public String getReferenteBando()
	{
		return referenteBando;
	}

	public void setReferenteBando(String referenteBando)
	{
		this.referenteBando = referenteBando;
	}

	public String getEmailReferenteBando()
	{
		return emailReferenteBando;
	}

	public void setEmailReferenteBando(String emailReferenteBando)
	{
		this.emailReferenteBando = emailReferenteBando;
	}

	public String getFlagRibassoInterventi()
	{
		return flagRibassoInterventi;
	}

	public void setFlagRibassoInterventi(String flagRibassoInterventi)
	{
		this.flagRibassoInterventi = flagRibassoInterventi;
	}

	public long getIdBandoMaster()
	{
		return idBandoMaster;
	}

	public void setIdBandoMaster(long idBandoMaster)
	{
		this.idBandoMaster = idBandoMaster;
	}

	public String getFlagTitolaritaRegionale()
	{
		return flagTitolaritaRegionale;
	}

	public void setFlagTitolaritaRegionale(String flagTitolaritaRegionale)
	{
		this.flagTitolaritaRegionale = flagTitolaritaRegionale;
	}

	public String getFlagDomandaMultipla()
	{
		return flagDomandaMultipla;
	}

	public void setFlagDomandaMultipla(String flagDomandaMultipla)
	{
		this.flagDomandaMultipla = flagDomandaMultipla;
	}

	public String getDescrTipoBando()
	{
		return descrTipoBando;
	}

	public void setDescrTipoBando(String descrTipoBando)
	{
		this.descrTipoBando = descrTipoBando;
	}

	public String getCodiceTipoBando()
	{
		return codiceTipoBando;
	}

	public void setCodiceTipoBando(String codiceTipoBando)
	{
		this.codiceTipoBando = codiceTipoBando;
	}

	public long getIdTipoLivello()
	{
		return idTipoLivello;
	}

	public void setIdTipoLivello(long idTipoLivello)
	{
		this.idTipoLivello = idTipoLivello;
	}

	public String getFlagMaster()
	{
		return flagMaster;
	}

	public void setFlagMaster(String flagMaster)
	{
		this.flagMaster = flagMaster;
	}

	public String getDataInizioStr()
	{
		return IuffiUtils.DATE.formatDateTime(dataInizio);
	}

	public String getDataFineStr()
	{
		return IuffiUtils.DATE.formatDateTime(dataFine);
	}

	public String getFlagGrafico()
	{
		return flagGrafico;
	}

	public void setFlagGrafico(String flagGrafico)
	{
		this.flagGrafico = flagGrafico;
	}

	public String getFlagReport()
	{
		return flagReport;
	}

	public void setFlagReport(String flagReport)
	{
		this.flagReport = flagReport;
	}

	public boolean isHaveReport()
	{
		return haveReport;
	}

	public void setHaveReport(boolean haveReport)
	{
		this.haveReport = haveReport;
	}

	public boolean isHaveChart()
	{
		return haveChart;
	}

	public void setHaveChart(boolean haveChart)
	{
		this.haveChart = haveChart;
	}

	public boolean isHaveGraduatorie()
	{
		return haveGraduatorie;
	}

	public void setHaveGraduatorie(boolean haveGraduatorie)
	{
		this.haveGraduatorie = haveGraduatorie;
	}

	public String getElencoIdLivelli()
	{
		String s = "&&&";

		List<LivelloDTO> liv = this.getLivelli();
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				s += Long.toString(l.getIdLivello()) + "&&&";

			}
		return s;
	}

	public String getElencoCodiciLivelliHtml()
	{
		// return elencoCodiciLivelliHtml;
		String htmlElenco = "";

		List<LivelloDTO> liv = this.getLivelli();
		int i = 0;
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				htmlElenco = htmlElenco + l.getCodice();

				if (i < livelli.size() - 1)
				{
					htmlElenco = htmlElenco + "<br>";
				}
				i++;
			}

		return htmlElenco;
	}

	public String getElencoIdLivelli2()
	{
		String s = "&&&";

		List<LivelloDTO> liv = this.getLivelli();
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				s += Long.toString(l.getIdLivello()) + "&&&";

			}
		return s;
	}

	public String getElencoCodiciOperazione()
	{
		String s = "&&&";

		List<LivelloDTO> liv = this.getLivelli();
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				s += l.getCodiceLivello() + "&&&";

			}
		return s;
	}

	public String getElencoCodiciLivelliOperazione()
	{
		String s = "&&&";

		List<LivelloDTO> liv = this.getLivelli();
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				s += l.getCodiceLivello() + "&&&";

			}
		return s;
	}

	public String getElencoCodiciLivelli()
	{
		String s = "&&&";

		List<LivelloDTO> liv = this.getLivelli();
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				s += l.getCodice() + "&&&";

			}
		return s;
	}

	public String getElencoCodiciLivelliMisure()
	{
		String s = "&&&";

		List<LivelloDTO> liv = this.getLivelli();
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				s += l.getCodiceMisura() + "&&&";

			}
		return s;
	}

	public String getElencoAmministrazioni()
	{
		String s = "&&&";

		List<AmmCompetenzaDTO> liv = this.getAmministrazioniCompetenza();
		if (liv != null)
			for (AmmCompetenzaDTO l : liv)
			{
				s += l.getIdAmmCompetenza() + "&&&";

			}
		return s;
	}

	public String getElencoCodiciLivelliMisureHtml()
	{
		// return elencoCodiciLivelliHtml;
		String htmlElenco = "";
		List<String> lCodici = new ArrayList<String>();
		List<LivelloDTO> liv = this.getLivelli();
		int i = 0;
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				if (!lCodici.contains(l.getCodiceLivello()))
				{
					htmlElenco = htmlElenco + l.getCodiceLivello();
					lCodici.add(l.getCodiceLivello());
					if (i < livelli.size() - 1)
					{
						htmlElenco = htmlElenco + "<br>";
					}
				}

				i++;
			}

		return htmlElenco;
	}

	public String getElencoSettoriStr()
	{
		String s = "&&&";

		List<SettoriDiProduzioneDTO> liv = this.getElencoSettori();
		if (liv != null)
			for (SettoriDiProduzioneDTO l : liv)
			{
				s += l.getDescrizione() + "&&&";

			}
		return s;
	}

	public String getElencoFocusAreaStr()
	{
		String s = "&&&";

		List<FocusAreaDTO> liv = this.getElencoFocusArea();
		if (liv != null)
			for (FocusAreaDTO l : liv)
			{
				s += l.getCodice() + "&&&";

			}
		return s;
	}

	public String getElencoCodiciLivelliSottoMisure()
	{
		String s = "&&&";

		List<LivelloDTO> liv = this.getLivelli();
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				s += l.getCodiceSottoMisura() + "&&&";

			}
		return s;
	}

	public String getElencoDescrizioniLivelli()
	{
		String s = "&&&";

		List<LivelloDTO> liv = this.getLivelli();
		if (liv != null)
			for (LivelloDTO l : liv)
			{
				s += l.getDescrizione() + "&&&";

			}
		return s;
	}

	public List<SettoriDiProduzioneDTO> getElencoSettori()
	{
		return elencoSettori;
	}

	public void setElencoSettori(List<SettoriDiProduzioneDTO> elencoSettori)
	{
		this.elencoSettori = elencoSettori;
	}

	public List<FocusAreaDTO> getElencoFocusArea()
	{
		return elencoFocusArea;
	}

	public void setElencoFocusArea(List<FocusAreaDTO> elencoFocusArea)
	{
		this.elencoFocusArea = elencoFocusArea;
	}

	public List<AmmCompetenzaDTO> getAmministrazioniCompetenza()
	{
		return amministrazioniCompetenza;
	}

	public void setAmministrazioniCompetenza(List<AmmCompetenzaDTO> amministrazioniCompetenza)
	{
		this.amministrazioniCompetenza = amministrazioniCompetenza;
	}

	public String getAmministrazioniCompetenzaHtml()
	{
		String s = "<ul>";
		if (amministrazioniCompetenza != null)
			for (AmmCompetenzaDTO a : amministrazioniCompetenza)
				s += "<li>" + a.getDescrizione() + "</li>";
		return s + "</ul>";
	}

	public String getIdsAmministrazioniCompetenza()
	{
		String s = "&&&";
		if (amministrazioniCompetenza != null)
			for (AmmCompetenzaDTO a : amministrazioniCompetenza)
				s += a.getIdAmmCompetenza() + "&&&";
		return s;
	}

	public Long getIdEventoCalamitoso()
	{
		return idEventoCalamitoso;
	}

	public void setIdEventoCalamitoso(Long idEventoCalamitoso)
	{
		this.idEventoCalamitoso = idEventoCalamitoso;
	}

	public String getDescEventoCalamitoso()
	{
		return descEventoCalamitoso;
	}

	public Date getDataEvento()
	{
		return dataEvento;
	}

	public void setDataEvento(Date dataEvento)
	{
		this.dataEvento = dataEvento;
	}

	public void setDescEventoCalamitoso(String descEventoCalamitoso)
	{
		this.descEventoCalamitoso = descEventoCalamitoso;
	}
	
	public String getDescrizioneEvento()
	{
		if(dataEvento != null)
		{
			return IuffiUtils.DATE.getYearFromDate(dataEvento) + " - " + getDescEventoCalamitoso();
		}
		else
		{
			return null;
		}
	}
	 public String getDescrizioneEventoCompleta()
	  {
	    if(dataEvento != null)
	    {
	      return IuffiUtils.DATE.formatDate(dataEvento) + " - " + getDescEventoCalamitoso();
	    }
	    else
	    {
	      return null;
	    }
	  }

	public BigDecimal getPercContributoErogabile()
	{
		return percContributoErogabile;
	}
	
	public String getPercContributoErogabileFormatted()
	{
		if(percContributoErogabile != null)
		{
			return IuffiUtils.FORMAT.formatDecimal2(percContributoErogabile) + "%";
		}
		else
		{
			return null;
		}
	}

	public void setPercContributoErogabile(BigDecimal percContributoErogabile)
	{
		this.percContributoErogabile = percContributoErogabile;
	}

	public BigDecimal getPercContributoMaxConcessa()
	{
		return percContributoMaxConcessa;
	}
	
	public String getPercContributoMaxConcessaFormatted()
	{
		if(percContributoMaxConcessa != null)
		{
			return IuffiUtils.FORMAT.formatDecimal2(percContributoMaxConcessa) + "%";
		}
		else
		{
			return null;
		}
	}

	public void setPercContributoMaxConcessa(BigDecimal percContributoMaxConcessa)
	{
		this.percContributoMaxConcessa = percContributoMaxConcessa;
	}

	public int getIdProcedimentoAgricolo()
	{
		return idProcedimentoAgricolo;
	}

	public void setIdProcedimentoAgricolo(int idProcedimentoAgricolo)
	{
		this.idProcedimentoAgricolo = idProcedimentoAgricolo;
	}
	
}
