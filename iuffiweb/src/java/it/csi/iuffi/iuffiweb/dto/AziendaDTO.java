package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AziendaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idAzienda;
  private long              idFormaGiuridica;
  private long              idStatoOggetto;
  private long              idProcediemnto;
  private long              extIdUtenteAggiornamento;
  private String            cuaa;
  private String            identificativo;
  private String            richiedenteDescr;
  private String            descrStatoOggetto;
  private String            denominazione;
  private String            denominazioneIntestazione;
  private String            partitaIva;
  private String            formaGiuridica;
  private String            sedeLegale;
  private String            descrComune;
  private String            descrProvincia;
  private String            indirizzoSedeLegale;
  private String            azione;
  private String            azioneHref;
  private String            procesistente;
  private String            note;
  private String            titleHref;
  private String            dataTrasmissione;
  private Date              dataCessazione;
  private Date              dataInizio;
  private Date              dataFine;
  private boolean           isProcedimentoEsistente;
  private boolean           isProcedimentoAperto;
  private String            rappresentanteLegale;

  public AziendaDTO(long idAzienda, String cuaa)
  {
    super();
    this.idAzienda = idAzienda;
    this.cuaa = cuaa;
  }

  public AziendaDTO()
  {
    super();
  }

  public long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public String getPartitaIva()
  {
    return partitaIva;
  }

  public void setPartitaIva(String partitaIva)
  {
    this.partitaIva = partitaIva;
  }

  public String getSedeLegale()
  {
    return sedeLegale;
  }

  public void setSedeLegale(String sedeLegale)
  {
    this.sedeLegale = sedeLegale;
  }

  public boolean isProcedimentoEsistente()
  {
    return isProcedimentoEsistente;
  }

  public void setProcedimentoEsistente(boolean isProcedimentoEsistente)
  {
    this.isProcedimentoEsistente = isProcedimentoEsistente;
  }

  public boolean isProcedimentoAperto()
  {
    return isProcedimentoAperto;
  }

  public void setProcedimentoAperto(boolean isProcedimentoAperto)
  {
    this.isProcedimentoAperto = isProcedimentoAperto;
  }

  public String getAzione()
  {
    return azione;
  }

  public void setAzione(String azione)
  {
    this.azione = azione;
  }

  public String getAzioneHref()
  {
    return azioneHref;
  }

  public void setAzioneHref(String azioneHref)
  {
    this.azioneHref = azioneHref;
  }

  public String getDataTrasmissione()
  {
    return dataTrasmissione;
  }

  public void setDataTrasmissione(String dataTrasmissione)
  {
    this.dataTrasmissione = dataTrasmissione;
  }

  public long getIdFormaGiuridica()
  {
    return idFormaGiuridica;
  }

  public void setIdFormaGiuridica(long idFormaGiuridica)
  {
    this.idFormaGiuridica = idFormaGiuridica;
  }

  public long getIdProcediemnto()
  {
    return idProcediemnto;
  }

  public void setIdProcediemnto(long idProcediemnto)
  {
    this.idProcediemnto = idProcediemnto;
  }

  public String getTitleHref()
  {
    return titleHref;
  }

  public void setTitleHref(String titleHref)
  {
    this.titleHref = titleHref;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public String getFormaGiuridica()
  {
    return formaGiuridica;
  }

  public void setFormaGiuridica(String formaGiuridica)
  {
    this.formaGiuridica = formaGiuridica;
  }

  public String getIndirizzoSedeLegale()
  {
    return indirizzoSedeLegale;
  }

  public void setIndirizzoSedeLegale(String indirizzoSedeLegale)
  {
    this.indirizzoSedeLegale = indirizzoSedeLegale;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Date getDataCessazione()
  {
    return dataCessazione;
  }

  public void setDataCessazione(Date dataCessazione)
  {
    this.dataCessazione = dataCessazione;
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

  public String getDescrComune()
  {
    return descrComune;
  }

  public void setDescrComune(String descrComune)
  {
    this.descrComune = descrComune;
  }

  public String getIdentificativo()
  {
    return identificativo;
  }

  public void setIdentificativo(String identificativo)
  {
    this.identificativo = identificativo;
  }

  public String getProcesistente()
  {
    return procesistente;
  }

  public void setProcesistente(String procesistente)
  {
    this.procesistente = procesistente;
  }

  public long getIdStatoOggetto()
  {
    return idStatoOggetto;
  }

  public void setIdStatoOggetto(long idStatoOggetto)
  {
    this.idStatoOggetto = idStatoOggetto;
  }

  public String getDescrStatoOggetto()
  {
    return descrStatoOggetto;
  }

  public String getRichiedenteDescr()
  {
    return richiedenteDescr;
  }

  public void setRichiedenteDescr(String richiedenteDescr)
  {
    this.richiedenteDescr = richiedenteDescr;
  }

  public void setDescrStatoOggetto(String descrStatoOggetto)
  {
    this.descrStatoOggetto = descrStatoOggetto;
  }

  public String getDenominazioneIntestazione()
  {
    return denominazioneIntestazione;
  }

  public void setDenominazioneIntestazione(String denominazioneIntestazione)
  {
    this.denominazioneIntestazione = denominazioneIntestazione;
  }

  public String getRappresentanteLegale()
  {
    return rappresentanteLegale;
  }

  public void setRappresentanteLegale(String rappresentanteLegale)
  {
    this.rappresentanteLegale = rappresentanteLegale;
  }

  public String getDescrProvincia()
  {
    return descrProvincia;
  }

  public void setDescrProvincia(String descrProvincia)
  {
    this.descrProvincia = descrProvincia;
  }

}
