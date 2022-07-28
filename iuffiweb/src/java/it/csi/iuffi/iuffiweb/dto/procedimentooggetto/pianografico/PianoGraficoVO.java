package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianografico;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PianoGraficoVO implements ILoggable
{

  private static final long serialVersionUID = -8064889027606525236L;

  private long              idEsito;
  private long              idUtenteAggiornamento;
  private String            note;
  private String            codiceEsito;
  private String            descUtenteAggiornamento;
  private Date              dataUltimoAggiornamento;

  public String getCodiceEsito()
  {
    return codiceEsito;
  }

  public void setCodiceEsito(String codiceEsito)
  {
    this.codiceEsito = codiceEsito;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }

  public long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getDescUtenteAggiornamento()
  {
    return descUtenteAggiornamento;
  }

  public void setDescUtenteAggiornamento(String descUtenteAggiornamento)
  {
    this.descUtenteAggiornamento = descUtenteAggiornamento;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }
}
