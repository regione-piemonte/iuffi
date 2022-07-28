package it.csi.iuffi.iuffiweb.dto;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DocumentoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 6035364196001956453L;

  private long              idDocumento;
  private String            descrizione;
  private List<EsitoDTO>    esitiPossibili;
  private long              idEsito;
  private String            descrizioneEsito;
  private String            noteEsito;

  public DocumentoDTO()
  {
  }

  public List<EsitoDTO> getEsitiPossibili()
  {
    return esitiPossibili;
  }

  public void setEsitiPossibili(List<EsitoDTO> esitiPossibili)
  {
    if (esitiPossibili != null)
      this.esitiPossibili = esitiPossibili;
  }

  public long getIdDocumento()
  {
    return idDocumento;
  }

  public void setIdDocumento(long idDocumento)
  {
    this.idDocumento = idDocumento;
  }

  public long getIdEsito()
  {
    return idEsito;
  }

  public void setIdEsito(long idEsito)
  {
    this.idEsito = idEsito;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getNoteEsito()
  {
    return noteEsito;
  }

  public void setNoteEsito(String noteEsito)
  {
    this.noteEsito = noteEsito;
  }

  public String getDescrizioneEsito()
  {
    return descrizioneEsito;
  }

  public void setDescrizioneEsito(String descrizioneEsito)
  {
    this.descrizioneEsito = descrizioneEsito;
  }

}
