package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SelezioneInfoInserimentoAllegatiDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -7658678531644584080L;
  private Long              idSelezioneInfo;
  private String            flagObbligatorio;
  private String            flagGestioneFile;

  public Long getIdSelezioneInfo()
  {
    return idSelezioneInfo;
  }

  public void setIdSelezioneInfo(Long idSelezioneInfo)
  {
    this.idSelezioneInfo = idSelezioneInfo;
  }

  public String getFlagObbligatorio()
  {
    return flagObbligatorio;
  }

  public void setFlagObbligatorio(String flagObbligatorio)
  {
    this.flagObbligatorio = flagObbligatorio;
  }

  public String getFlagGestioneFile()
  {
    return flagGestioneFile;
  }

  public void setFlagGestioneFile(String flagGestioneFile)
  {
    this.flagGestioneFile = flagGestioneFile;
  }
}
