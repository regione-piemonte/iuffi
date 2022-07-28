package it.csi.iuffi.iuffiweb.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class PianoMonitoraggioDTO extends TabelleStoricizzateDTO implements ILoggable
{

  private static final long serialVersionUID = 1L;
  
  private Integer idPianoMonitoraggio;
  private Integer anno;
  private String versione;
  private Date dataInserimento;
  private String note;

  @JsonIgnore
  private String flagArchiviato;
  
  public PianoMonitoraggioDTO()
  {
    super();
  }
  
  public Integer getIdPianoMonitoraggio()
  {
    return idPianoMonitoraggio;
  }
  public void setIdPianoMonitoraggio(Integer idPianoMonitoraggio)
  {
    this.idPianoMonitoraggio = idPianoMonitoraggio;
  }
  public Integer getAnno()
  {
    return anno;
  }
  public void setAnno(Integer anno)
  {
    this.anno = anno;
  }
  public String getVersione()
  {
    return versione;
  }
  public void setVersione(String versione)
  {
    this.versione = versione;
  }
  public Date getDataInserimento()
  {
    return dataInserimento;
  }
  public void setDataInserimento(Date dataInserimento)
  {
    this.dataInserimento = dataInserimento;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }

  public String getDataInserimentoF()
  {
    return IuffiUtils.DATE.formatDate(dataInserimento);
  }  

}
