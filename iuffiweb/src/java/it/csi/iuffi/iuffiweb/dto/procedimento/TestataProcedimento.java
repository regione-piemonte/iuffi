package it.csi.iuffi.iuffiweb.dto.procedimento;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TestataProcedimento implements Serializable
{
  /** serialVersionUID */
  private static final long  serialVersionUID = 9155066856140321941L;
  public static final String SESSION_NAME     = "testataProcedimento";

  protected long             idAzienda;
  protected long             idAmmCompetenza;
  protected String           cuaa;
  protected String           denominazioneAzienda;
  protected Timestamp        dataCessazioneAzienda;
  protected Integer          annoCampagna;
  protected String           denominazioneBando;
  protected String           motivoCessazione;
  protected String           denominazioneAmmCompetenzaUno;
  protected String           denominazioneAmmCompetenzaDue;
  protected String           descBreveAmmCompetenza;
  protected String           descAmmCompetenza;
  protected String           descUfficioZona;
  protected String           descFunzionarioIstruttore;
  protected String           descStatoOggetto;
  protected String           descOggetto;
  protected List<String>     misure;
  protected List<String>     codMisure;
  private Date               dataInizioBando;

  public String getCuaa()
  {
    return this.cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazioneAzienda()
  {
    return this.denominazioneAzienda;
  }

  public void setDenominazioneAzienda(String denominazioneAzienda)
  {
    this.denominazioneAzienda = denominazioneAzienda;
  }

  public Timestamp getDataCessazioneAzienda()
  {
    return this.dataCessazioneAzienda;
  }

  public void setDataCessazioneAzienda(Timestamp dataCessazioneAzienda)
  {
    this.dataCessazioneAzienda = dataCessazioneAzienda;
  }

  public Integer getAnnoCampagna()
  {
    return this.annoCampagna;
  }

  public void setAnnoCampagna(Integer annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }

  public String getDenominazioneBando()
  {
    return this.denominazioneBando;
  }

  public void setDenominazioneBando(String denominazioneBando)
  {
    this.denominazioneBando = denominazioneBando;
  }

  public String getDescAmmCompetenza()
  {
    return this.descAmmCompetenza;
  }

  public void setDescAmmCompetenza(String descAmmCompetenza)
  {
    this.descAmmCompetenza = descAmmCompetenza;
  }

  public String getDescStatoOggetto()
  {
    return this.descStatoOggetto;
  }

  public void setDescStatoOggetto(String descStatoOggetto)
  {
    this.descStatoOggetto = descStatoOggetto;
  }

  public List<String> getMisure()
  {
    return misure;
  }

  public long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public void setMisure(List<String> misure)
  {
    this.misure = misure;
  }

  public void setDataInizioBando(Date dataInizioBando)
  {
    this.dataInizioBando = dataInizioBando;
  }

  public Date getDataInizioBando()
  {
    return dataInizioBando;
  }

  public String getDenominazioneAmmCompetenzaUno()
  {
    return denominazioneAmmCompetenzaUno;
  }

  public void setDenominazioneAmmCompetenzaUno(
      String denominazioneAmmCompetenzaUno)
  {
    this.denominazioneAmmCompetenzaUno = denominazioneAmmCompetenzaUno;
  }

  public String getDenominazioneAmmCompetenzaDue()
  {
    return denominazioneAmmCompetenzaDue;
  }

  public void setDenominazioneAmmCompetenzaDue(
      String denominazioneAmmCompetenzaDue)
  {
    this.denominazioneAmmCompetenzaDue = denominazioneAmmCompetenzaDue;
  }

  public String getDescBreveAmmCompetenza()
  {
    return descBreveAmmCompetenza;
  }

  public void setDescBreveAmmCompetenza(String descBreveAmmCompetenza)
  {
    this.descBreveAmmCompetenza = descBreveAmmCompetenza;
  }

  public String getDescOggetto()
  {
    return descOggetto;
  }

  public void setDescOggetto(String descOggetto)
  {
    this.descOggetto = descOggetto;
  }

  public List<String> getCodMisure()
  {
    return codMisure;
  }

  public void setCodMisure(List<String> codMisure)
  {
    this.codMisure = codMisure;
  }

  public String getMotivoCessazione()
  {
    return motivoCessazione;
  }

  public void setMotivoCessazione(String motivoCessazione)
  {
    this.motivoCessazione = motivoCessazione;
  }

  public long getIdAmmCompetenza()
  {
    return idAmmCompetenza;
  }

  public void setIdAmmCompetenza(long idAmmCompetenza)
  {
    this.idAmmCompetenza = idAmmCompetenza;
  }

  public String getDescUfficioZona()
  {
    return descUfficioZona;
  }

  public void setDescUfficioZona(String descUfficioZona)
  {
    this.descUfficioZona = descUfficioZona;
  }

  public String getDescFunzionarioIstruttore()
  {
    return descFunzionarioIstruttore;
  }

  public void setDescFunzionarioIstruttore(String descFunzionarioIstruttore)
  {
    this.descFunzionarioIstruttore = descFunzionarioIstruttore;
  }

}
