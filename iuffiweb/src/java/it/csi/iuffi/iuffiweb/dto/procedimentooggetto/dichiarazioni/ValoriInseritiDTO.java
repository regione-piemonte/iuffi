package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ValoriInseritiDTO implements ILoggable
{

  private static final long serialVersionUID = 3232376183922139291L;
  private Long              idSelezioneInfo;
  private String            valore;
  private long              posizione;

  public ValoriInseritiDTO()
  {
    super();
  }

  public ValoriInseritiDTO(long idSelezioneInfo, String valore, long posizione)
  {
    super();
    this.idSelezioneInfo = idSelezioneInfo;
    this.valore = valore;
    this.posizione = posizione;
  }

  public Long getIdSelezioneInfo()
  {
    return idSelezioneInfo;
  }

  public void setIdSelezioneInfo(Long idSelezioneInfo)
  {
    this.idSelezioneInfo = idSelezioneInfo;
  }

  public String getValore()
  {
    return valore;
  }

  public void setValore(String valore)
  {
    this.valore = valore;
  }

  public long getPosizione()
  {
    return posizione;
  }

  public void setPosizione(long posizione)
  {
    this.posizione = posizione;
  }

}
