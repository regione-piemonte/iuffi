package it.csi.iuffi.iuffiweb.dto.nuovoprocedimento;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class FileAllegatoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idBando;
  private long              idAllegatiBando;
  private String            descrizione;
  private String            nomeFile;
  private byte[]            fileAllegato;

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public long getIdAllegatiBando()
  {
    return idAllegatiBando;
  }

  public void setIdAllegatiBando(long idAllegatiBando)
  {
    this.idAllegatiBando = idAllegatiBando;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getNomeFile()
  {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
  }

  public byte[] getFileAllegato()
  {
    return fileAllegato;
  }

  public void setFileAllegato(byte[] fileAllegato)
  {
    this.fileAllegato = fileAllegato;
  }

  public String getCssClass()
  {
    return IuffiUtils.FILE.getDocumentCSSIconClass(nomeFile);
  }

}
