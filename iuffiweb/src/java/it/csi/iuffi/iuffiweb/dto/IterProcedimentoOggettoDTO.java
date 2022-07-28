package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class IterProcedimentoOggettoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idIterProcedimentoOggett;
  private long              idStatoOggetto;
  private long              extIdUtenteAggiornamento;
  private String            descrizione;
  private String            note;
  private String            utenteAggiornamentoDescr;
  private Date              dataInizio;

  public long getIdIterProcedimentoOggett()
  {
    return idIterProcedimentoOggett;
  }

  public void setIdIterProcedimentoOggett(long idIterProcedimentoOggett)
  {
    this.idIterProcedimentoOggett = idIterProcedimentoOggett;
  }

  public long getIdStatoOggetto()
  {
    return idStatoOggetto;
  }

  public void setIdStatoOggetto(long idStatoOggetto)
  {
    this.idStatoOggetto = idStatoOggetto;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getUtenteAggiornamentoDescr()
  {
    return utenteAggiornamentoDescr;
  }

  public void setUtenteAggiornamentoDescr(String utenteAggiornamentoDescr)
  {
    this.utenteAggiornamentoDescr = utenteAggiornamentoDescr;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

}
