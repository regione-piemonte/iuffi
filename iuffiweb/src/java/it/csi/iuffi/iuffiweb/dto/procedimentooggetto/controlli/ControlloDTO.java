package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ControlloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idControllo;
  private Long              idEsecuzioneControllo;
  private String            codice;
  private String            descrizione;
  private String            descrizioneEstesa;

  private Long              idUtenteEsecuzione;
  private String            utenteEsecuzione;
  private Date              dataEsecuzione;
  private String            flagElaborazioneInCorso;

  private Long              idAnomalieControllo;
  private String            descrizioneAnomalia;
  private String            ulterioriInformazioni;
  private String            ulterioriInformazioniAnomalia;
  private String            gravita;
  private Long              idSoluzioneAnomalia;
  private String            flagAnomaliaGiustificabile;
  private boolean           anomaliaAntimafia;

  public long getIdControllo()
  {
    return idControllo;
  }

  public void setIdControllo(long idControllo)
  {
    this.idControllo = idControllo;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getDescrizioneEstesa()
  {
    return descrizioneEstesa;
  }

  public void setDescrizioneEstesa(String descrizioneEstesa)
  {
    this.descrizioneEstesa = descrizioneEstesa;
  }

  public Long getIdUtenteEsecuzione()
  {
    return idUtenteEsecuzione;
  }

  public void setIdUtenteEsecuzione(Long idUtenteEsecuzione)
  {
    this.idUtenteEsecuzione = idUtenteEsecuzione;
  }

  public Date getDataEsecuzione()
  {
    return dataEsecuzione;
  }

  public void setDataEsecuzione(Date dataEsecuzione)
  {
    this.dataEsecuzione = dataEsecuzione;
  }

  public String getFlagElaborazioneInCorso()
  {
    return flagElaborazioneInCorso;
  }

  public void setFlagElaborazioneInCorso(String flagElaborazioneInCorso)
  {
    this.flagElaborazioneInCorso = flagElaborazioneInCorso;
  }

  public Long getIdAnomalieControllo()
  {
    return idAnomalieControllo;
  }

  public void setIdAnomalieControllo(Long idAnomalieControllo)
  {
    this.idAnomalieControllo = idAnomalieControllo;
  }

  public String getDescrizioneAnomalia()
  {
    return descrizioneAnomalia;
  }

  public void setDescrizioneAnomalia(String descrizioneAnomalia)
  {
    this.descrizioneAnomalia = descrizioneAnomalia;
  }

  public String getUlterioriInformazioniAnomalia()
  {
    return ulterioriInformazioniAnomalia;
  }

  public void setUlterioriInformazioniAnomalia(
      String ulterioriInformazioniAnomalia)
  {
    this.ulterioriInformazioniAnomalia = ulterioriInformazioniAnomalia;
  }

  public String getGravita()
  {
    return gravita;
  }

  public void setGravita(String gravita)
  {
    this.gravita = gravita;
  }

  public String getUtenteEsecuzione()
  {
    return utenteEsecuzione;
  }

  public void setUtenteEsecuzione(String utenteEsecuzione)
  {
    this.utenteEsecuzione = utenteEsecuzione;
  }

  public Long getIdEsecuzioneControllo()
  {
    return idEsecuzioneControllo;
  }

  public void setIdEsecuzioneControllo(Long idEsecuzioneControllo)
  {
    this.idEsecuzioneControllo = idEsecuzioneControllo;
  }

  public String getUlterioriInformazioni()
  {
    return ulterioriInformazioni;
  }

  public void setUlterioriInformazioni(String ulterioriInformazioni)
  {
    this.ulterioriInformazioni = ulterioriInformazioni;
  }

  public Long getIdSoluzioneAnomalia()
  {
    return idSoluzioneAnomalia;
  }

  public void setIdSoluzioneAnomalia(Long idSoluzioneAnomalia)
  {
    this.idSoluzioneAnomalia = idSoluzioneAnomalia;
  }

  public String getFlagAnomaliaGiustificabile()
  {
    return flagAnomaliaGiustificabile;
  }

  public void setFlagAnomaliaGiustificabile(String flagAnomaliaGiustificabile)
  {
    this.flagAnomaliaGiustificabile = flagAnomaliaGiustificabile;
  }

  public boolean isAnomaliaAntimafia()
  {
    return anomaliaAntimafia;
  }

  public void setAnomaliaAntimafia(boolean anomaliaAntimafia)
  {
    this.anomaliaAntimafia = anomaliaAntimafia;
  }

}
