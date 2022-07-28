package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DatiIdentificativiModificaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long      serialVersionUID = 1158027105590788266L;
  private Long                   idAmmCompetenza;
  private Long                   idContitolare;
  private String                 note;
  private SettoriDiProduzioneDTO settore;

  public Long getIdAmmCompetenza()
  {
    return idAmmCompetenza;
  }

  public void setIdAmmCompetenza(Long idAmmCompetenza)
  {
    this.idAmmCompetenza = idAmmCompetenza;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Long getIdContitolare()
  {
    return idContitolare;
  }

  public void setIdContitolare(Long idContitolare)
  {
    this.idContitolare = idContitolare;
  }

  public SettoriDiProduzioneDTO getSettore()
  {
    return settore;
  }

  public void setSettore(SettoriDiProduzioneDTO settore)
  {
    this.settore = settore;
  }

}
