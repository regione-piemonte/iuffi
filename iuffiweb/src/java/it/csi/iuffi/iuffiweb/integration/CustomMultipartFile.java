package it.csi.iuffi.iuffiweb.integration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile {

  private final byte[] fileContent;

  private String fileName;

  private String contentType;

  private File file;

  private String destPath = System.getProperty("java.io.tmpdir");

  private FileOutputStream fileOutputStream;

  public CustomMultipartFile(byte[] fileData, String name) {
    this.fileContent = fileData;
    this.fileName = name;
    file = new File(destPath + fileName);

  }

  @Override
  public void transferTo(File dest) throws IOException, IllegalStateException {
    fileOutputStream = new FileOutputStream(dest);
    fileOutputStream.write(fileContent);
  }

  public void clearOutStreams() throws IOException {
    if (null != fileOutputStream) {
      fileOutputStream.flush();
      fileOutputStream.close();
      file.deleteOnExit();
    }
  }

  @Override
  public byte[] getBytes() throws IOException {
    return fileContent;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(fileContent);
  }

  @Override
  public String getContentType()
  {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String getName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getOriginalFilename()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long getSize()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isEmpty()
  {
    // TODO Auto-generated method stub
    return false;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public File getFile()
  {
    return file;
  }

  public void setFile(File file)
  {
    this.file = file;
  }

  public String getDestPath()
  {
    return destPath;
  }

  public void setDestPath(String destPath)
  {
    this.destPath = destPath;
  }

  public FileOutputStream getFileOutputStream()
  {
    return fileOutputStream;
  }

  public void setFileOutputStream(FileOutputStream fileOutputStream)
  {
    this.fileOutputStream = fileOutputStream;
  }

  public byte[] getFileContent()
  {
    return fileContent;
  }

  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }


}
