package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class IterProcedimentoGruppoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idIterProcedimentoGruppo;
  private long              idStatoOggetto;
  private long              codiceRaggruppamento;
  private Date              dataInizio;
  private Date              dataFine;
  private long              extIdUtenteAggiornamento;
  private String            descrizione;
  private String            descUtenteAggiornamento;
  private String            note;

  public long getIdIterProcedimentoGruppo()
  {
    return idIterProcedimentoGruppo;
  }

  public void setIdIterProcedimentoGruppo(long idIterProcedimentoGruppo)
  {
    this.idIterProcedimentoGruppo = idIterProcedimentoGruppo;
  }

  public long getIdStatoOggetto()
  {
    return idStatoOggetto;
  }

  public void setIdStatoOggetto(long idStatoOggetto)
  {
    this.idStatoOggetto = idStatoOggetto;
  }

  public long getCodiceRaggruppamento()
  {
    return codiceRaggruppamento;
  }

  public void setCodiceRaggruppamento(long codiceRaggruppamento)
  {
    this.codiceRaggruppamento = codiceRaggruppamento;
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

  public String getDescUtenteAggiornamento()
  {
    return descUtenteAggiornamento;
  }

  public void setDescUtenteAggiornamento(String descUtenteAggiornamento)
  {
    this.descUtenteAggiornamento = descUtenteAggiornamento;
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
