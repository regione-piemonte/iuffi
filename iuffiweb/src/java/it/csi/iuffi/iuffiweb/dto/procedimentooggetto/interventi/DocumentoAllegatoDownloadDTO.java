package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DocumentoAllegatoDownloadDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3037786524614063714L;
  protected String          fileName;
  protected byte[]          bytes;

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public byte[] getBytes()
  {
    return bytes;
  }

  public void setBytes(byte[] bytes)
  {
    this.bytes = bytes;
  }
}
