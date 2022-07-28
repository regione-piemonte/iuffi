package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class FileAllegatoInterventoDTO extends RigaJSONAllegatiInterventoDTO
    implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3302453483482558887L;
  protected byte[]          fileAllegato;

  public byte[] getFileAllegato()
  {
    return fileAllegato;
  }

  public void setFileAllegato(byte[] fileAllegato)
  {
    this.fileAllegato = fileAllegato;
  }

}
