package it.csi.iuffi.iuffiweb.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;


public abstract class TabelleDTO
{

  @JsonIgnore
  private Long extIdUtenteAggiornamento;
  @JsonIgnore
  private Date dataUltimoAggiornamento;

  public Long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }
  public void setExtIdUtenteAggiornamento(Long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
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
