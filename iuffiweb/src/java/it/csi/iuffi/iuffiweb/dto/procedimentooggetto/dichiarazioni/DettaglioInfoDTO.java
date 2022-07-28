package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DettaglioInfoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long       serialVersionUID                = -7866141986745760614L;
  public static final String      FLAG_GESTIONE_FILE_OBBLIGATORIO = "O";
  public static final String      FLAG_GESTIONE_FILE_FACOLTATIVO  = "F";
  public static final String      FLAG_ALLEGATO_OBBLIGATORIO      = "S";
  public static final String      FLAG_ALLEGATO_NON_OBBLIGATORIO  = "N";
  private long                    idDettaglioInfo;
  private Long                    idSelezioneInfo;
  private long                    idTipoDocumento;
  private Long                    idLegameInfo;
  private String                  tipoVincolo;
  private String                  descrizione;
  private String                  flagObbligatorio;
  private String                  flagGestioneFile;
  private boolean                 isChecked;
  private List<ValoriInseritiDTO> valoriInseriti;

  public long getIdDettaglioInfo()
  {
    return idDettaglioInfo;
  }

  public void setIdDettaglioInfo(long idDettaglioInfo)
  {
    this.idDettaglioInfo = idDettaglioInfo;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
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

  public Long getIdSelezioneInfo()
  {
    return idSelezioneInfo;
  }

  public void setIdSelezioneInfo(Long idSelezioneInfo)
  {
    this.idSelezioneInfo = idSelezioneInfo;
  }

  public Long getIdLegameInfo()
  {
    return idLegameInfo;
  }

  public void setIdLegameInfo(Long idLegameInfo)
  {
    this.idLegameInfo = idLegameInfo;
  }

  public String getTipoVincolo()
  {
    return tipoVincolo;
  }

  public void setTipoVincolo(String tipoVincolo)
  {
    this.tipoVincolo = tipoVincolo;
  }

  public boolean isChecked()
  {
    return isChecked;
  }

  public void setChecked(boolean isChecked)
  {
    this.isChecked = isChecked;
  }

  public List<ValoriInseritiDTO> getValoriInseriti()
  {
    return valoriInseriti;
  }

  public void setValoriInseriti(List<ValoriInseritiDTO> valoriInseriti)
  {
    this.valoriInseriti = valoriInseriti;
  }

  public long getIdTipoDocumento()
  {
    return idTipoDocumento;
  }

  public void setIdTipoDocumento(long idTipoDocumento)
  {
    this.idTipoDocumento = idTipoDocumento;
  }

}
