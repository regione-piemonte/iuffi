package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DatiProcedimento implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4716800768683507556L;
  private String            organismoDelegato;
  private String            note;
  private Date              dataUltimoAggiornamento;
  private long              extIdUtenteAggiornamento;
  private long              extIdAmmCompetenza;
  private String            codiceCup;

  private String            funzionario;
  private String            ufficio;
  private String            responsabile;
  private String            organismoDelegatoInfo;
  private String            vercod;
  private Date              dataVisuraCamerale;

  public String getOrganismoDelegato()
  {
    return organismoDelegato;
  }

  public void setOrganismoDelegato(String organismoDelegato)
  {
    this.organismoDelegato = organismoDelegato;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public long getExtIdAmmCompetenza()
  {
    return extIdAmmCompetenza;
  }

  public void setExtIdAmmCompetenza(long extIdAmmCompetenza)
  {
    this.extIdAmmCompetenza = extIdAmmCompetenza;
  }

  public String getCodiceCup()
  {
    return codiceCup;
  }

  public void setCodiceCup(String codiceCup)
  {
    this.codiceCup = codiceCup;
  }

  public String getFunzionario()
  {
    return funzionario;
  }

  public void setFunzionario(String funzionario)
  {
    this.funzionario = funzionario;
  }

  public String getUfficio()
  {
    return ufficio;
  }

  public void setUfficio(String ufficio)
  {
    this.ufficio = ufficio;
  }

  public String getResponsabile()
  {
    return responsabile;
  }

  public void setResponsabile(String responsabile)
  {
    this.responsabile = responsabile;
  }

  public String getOrganismoDelegatoInfo()
  {
    return organismoDelegatoInfo;
  }

  public void setOrganismoDelegatoInfo(String organismoDelegatoInfo)
  {
    this.organismoDelegatoInfo = organismoDelegatoInfo;
  }

  public String getOrganismoDelegatoEsteso()
  {
    if (organismoDelegatoInfo != null
        && organismoDelegatoInfo.trim().length() > 0)
      return organismoDelegato + " - " + organismoDelegatoInfo;
    else
      return organismoDelegato;
  }

  public Date getDataVisuraCamerale()
  {
    return dataVisuraCamerale;
  }

  public String getDataVisuraCameraleStr()
  {
    return IuffiUtils.DATE.formatDate(dataVisuraCamerale);
  }

  public void setDataVisuraCamerale(Date dataVisuraCamerale)
  {
    this.dataVisuraCamerale = dataVisuraCamerale;
  }

  public String getVercod()
  {
    return vercod;
  }

  public void setVercod(String vercod)
  {
    this.vercod = vercod;
  }

}
