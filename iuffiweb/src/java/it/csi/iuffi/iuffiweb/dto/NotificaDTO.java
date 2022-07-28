package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class NotificaDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 7048395105781721238L;

  private long              idNotifica;
  private String            note;
  private String            gravita;
  private String            descrizioneGravita;
  private Long              idGravitaNotifica;
  private long              idVisibilita;
  private Date              dataInizio;
  private Date              dataFine;
  private String            utente;
  private String            descrizione;
  private long              idUtente;
  private String            stato;
  private boolean           utenteCorrenteAbilitato;
  private long              amministrazioneCompetenzaProcedimento;

  public long getIdNotifica()
  {
    return idNotifica;
  }

  public void setIdNotifica(long idNotifica)
  {
    this.idNotifica = idNotifica;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getGravita()
  {
    return gravita;
  }

  public void setGravita(String gravita)
  {
    this.gravita = gravita;
  }

  public long getIdVisibilita()
  {
    return idVisibilita;
  }

  public void setIdVisibilita(long idVisibilita)
  {
    this.idVisibilita = idVisibilita;
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

  public String getUtente()
  {
    return utente;
  }

  public void setUtente(String utente)
  {
    this.utente = utente;
  }

  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public long getIdUtente()
  {
    return idUtente;
  }

  public void setIdUtente(long idUtente)
  {
    this.idUtente = idUtente;
  }

  public String getGravitaStr()
  {
    if (gravita == "W")
      return "Warning";
    else
      return "Blocking";
  }

  public void setGravitaStr(String gravitaStr)
  {
    if (gravitaStr != null)
    {
      if (gravitaStr.compareTo("Warning") == 0)
        setGravita("W");
      else
        setGravita("B");
    }
  }

  public String getStato()
  {
    return stato;
  }

  public void setStato(String stato)
  {
    this.stato = stato;
  }

  public String getDataInizioStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataInizio);
  }

  public String getDataFineStr()
  {
    return IuffiUtils.DATE.formatDateTime(dataFine);
  }

  public boolean isUtenteCorrenteAbilitato()
  {
    return utenteCorrenteAbilitato;
  }

  public void setUtenteCorrenteAbilitato(boolean utenteCorrenteAbilitato)
  {
    this.utenteCorrenteAbilitato = utenteCorrenteAbilitato;
  }

  public long getAmministrazioneCompetenzaProcedimento()
  {
    return amministrazioneCompetenzaProcedimento;
  }

  public void setAmministrazioneCompetenzaProcedimento(
      long amministrazioneCompetenzaProcedimento)
  {
    this.amministrazioneCompetenzaProcedimento = amministrazioneCompetenzaProcedimento;
  }

  public String getDescrizioneGravita()
  {
    return descrizioneGravita;
  }

  public void setDescrizioneGravita(String descrizioneGravita)
  {
    this.descrizioneGravita = descrizioneGravita;
  }

  public Long getIdGravitaNotifica()
  {
    return idGravitaNotifica;
  }

  public void setIdGravitaNotifica(Long idGravitaNotifica)
  {
    this.idGravitaNotifica = idGravitaNotifica;
  }

}
