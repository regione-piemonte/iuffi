package it.csi.iuffi.iuffiweb.model.api;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class Trappolaggio implements ILoggable
{

  private static final long serialVersionUID = 6032549812641701650L;

  private Integer idTrappolaggio;
  private Integer idRilevazione;
  private Long idMissione;
  private Trappola trappola;
  private String istatComune;
  private Integer idOperazione;
  private String dataTrappolaggio;
  private String dataOraInizio;
  private String dataOraFine;
  private String note;
  private Integer idIspezioneVisiva;
  private Integer idAnagrafica;
  private String ispettore;
  private String foto;
  private Integer idOrganismoNocivo;
  
  public Trappolaggio()
  {
    super();
  }

  public Integer getIdTrappolaggio()
  {
    return idTrappolaggio;
  }

  public void setIdTrappolaggio(Integer idTrappolaggio)
  {
    this.idTrappolaggio = idTrappolaggio;
  }

  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }

  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }

  public Trappola getTrappola()
  {
    return trappola;
  }

  public void setTrappola(Trappola trappola)
  {
    this.trappola = trappola;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public Integer getIdOperazione()
  {
    return idOperazione;
  }

  public void setIdOperazione(Integer idOperazione)
  {
    this.idOperazione = idOperazione;
  }

  public String getDataTrappolaggio()
  {
    return dataTrappolaggio;
  }

  public void setDataTrappolaggio(String dataTrappolaggio)
  {
    this.dataTrappolaggio = dataTrappolaggio;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Integer getIdIspezioneVisiva()
  {
    return idIspezioneVisiva;
  }

  public void setIdIspezioneVisiva(Integer idIspezioneVisiva)
  {
    this.idIspezioneVisiva = idIspezioneVisiva;
  }

  public Long getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Long idMissione)
  {
    this.idMissione = idMissione;
  }

  public String getDataOraInizio()
  {
    return dataOraInizio;
  }

  public void setDataOraInizio(String dataOraInizio)
  {
    this.dataOraInizio = dataOraInizio;
  }

  public String getDataOraFine()
  {
    return dataOraFine;
  }

  public void setDataOraFine(String dataOraFine)
  {
    this.dataOraFine = dataOraFine;
  }

  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }

  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
  }

  public String getFoto()
  {
    return foto;
  }

  public void setFoto(String foto)
  {
    this.foto = foto;
  }

  public String getIspettore()
  {
    return ispettore;
  }

  public void setIspettore(String ispettore)
  {
    this.ispettore = ispettore;
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

}
