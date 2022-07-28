package it.csi.iuffi.iuffiweb.dto.internal;

public class MailAttachment
{

  private byte[] attach;
  private String fileName;
  private String fileType;

  public byte[] getAttach()
  {
    return attach;
  }

  public void setAttach(byte[] attach)
  {
    this.attach = attach;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public String getFileType()
  {
    return fileType;
  }

  public void setFileType(String fileType)
  {
    this.fileType = fileType;
  }

}
