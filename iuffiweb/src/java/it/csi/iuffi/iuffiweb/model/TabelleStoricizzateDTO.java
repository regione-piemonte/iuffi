package it.csi.iuffi.iuffiweb.model;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.util.IuffiUtils;


public abstract class TabelleStoricizzateDTO
{
  
  @JsonIgnore
  private Long extIdUtenteAggiornamento;
  @JsonIgnore
  private Date dataUltimoAggiornamento;
  @JsonIgnore
  @NotNull
  private Date dataInizioValidita;
  @JsonIgnore
  private Date dataFineValidita;
  
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
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  
  public String getDataInizioValiditaF()
  {
    return IuffiUtils.DATE.formatDate(dataInizioValidita);
  }
  
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  
  public String getDataFineValiditaF()
  {
    return IuffiUtils.DATE.formatDate(dataFineValidita);
  }  
  
 
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
  
  public Boolean isAnnoCorrente() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    Integer annoCorrente = calendar.get(Calendar.YEAR);
    
    Calendar calendarValidita = Calendar.getInstance();
    if(getDataInizioValidita() != null) {
      calendarValidita.setTime(getDataInizioValidita());      
    }else {
      calendarValidita.setTime(new Date());
    }
    Integer annoInizioValidita = calendarValidita.get(Calendar.YEAR);
    
    if(annoInizioValidita.equals(annoCorrente)) {
      return Boolean.TRUE;
    }else {
      return Boolean.FALSE;
    }
  }

}
