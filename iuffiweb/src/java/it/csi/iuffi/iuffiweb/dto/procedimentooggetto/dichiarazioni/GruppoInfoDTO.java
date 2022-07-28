package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class GruppoInfoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long      serialVersionUID = -7866141986745760614L;
  private long                   idGruppoInfo;
  private long                   idUtenteAggiornamento;
  private String                 descrizione;
  private List<DettaglioInfoDTO> dettaglioInfo;

  public long getIdGruppoInfo()
  {
    return idGruppoInfo;
  }

  public void setIdGruppoInfo(long idGruppoInfo)
  {
    this.idGruppoInfo = idGruppoInfo;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public List<DettaglioInfoDTO> getDettaglioInfo()
  {
    return dettaglioInfo;
  }

  public void setDettaglioInfo(List<DettaglioInfoDTO> dettaglioInfo)
  {
    this.dettaglioInfo = dettaglioInfo;
  }

  public long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

}
