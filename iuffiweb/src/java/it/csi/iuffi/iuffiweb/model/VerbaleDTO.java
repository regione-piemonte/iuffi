package it.csi.iuffi.iuffiweb.model;


import java.io.IOException;


import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.integration.CustomMultipartFile;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;


public class VerbaleDTO extends TabelleDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4621339630061084884L;
  
  private Integer idVerbale;
  private Date dataVerbale;
  private Integer idMissione;
  private Integer idIspettore;
  private MultipartFile pdfVerbale;
  private String numVerbale;
  private Integer idStatoVerbale;
  private long extIdDocumentoIndex;
  private String nomeFile;
  private byte[] pdfJasper;

 public VerbaleDTO()
  {
    super();
  }

  public VerbaleDTO(Integer idVerbale, Date dataVerbale, Integer idMissione,
      Integer idIspettore,MultipartFile pdfVerbale)
  {
    super();
    this.idVerbale = idVerbale;
    this.dataVerbale = dataVerbale;
    this.idMissione = idMissione;
    this.idIspettore = idIspettore;
    this.pdfVerbale=pdfVerbale;
  }


  public Integer getIdVerbale()
  {
    return idVerbale;
  }

  public void setIdVerbale(Integer idVerbale)
  {
    this.idVerbale = idVerbale;
  }

  public Date getDataVerbale()
  {
    return dataVerbale;
  }

  public void setDataVerbale(Date dataVerbale)
  {
    this.dataVerbale = dataVerbale;
  }

  public Integer getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Integer idMissione)
  {
    this.idMissione = idMissione;
  }

  public Integer getIdIspettore()
  {
    return idIspettore;
  }

  public void setIdIspettore(Integer idIspettore)
  {
    this.idIspettore = idIspettore;
  }

  public MultipartFile getPdfVerbale()
  {
    return pdfVerbale;
  }

  public void setPdfVerbale(MultipartFile pdfVerbale)
  {
    this.pdfVerbale = pdfVerbale;
  }
  
  public String getNumVerbale()
  {
    return numVerbale;
  }
  
  public void setPdfVerbale(byte[] bytes)
  {
    if (bytes != null) {
      String fileName = "file.pdf";//(this.nomeFile!=null)?this.nomeFile:"file.pdf";
      CustomMultipartFile customMultipartFile = new CustomMultipartFile(bytes, fileName);
      try {
        customMultipartFile.transferTo(customMultipartFile.getFile());
        this.pdfVerbale = customMultipartFile;
      } catch (IllegalStateException e) {
          //log.info("IllegalStateException : " + e);
          e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
          //log.info("IOException : " + e);
      }
    } else
        this.pdfVerbale = null;
  }

  public void setNumVerbale(String numVerbale)
  {
    this.numVerbale = numVerbale;
  }

  public Integer getIdStatoVerbale()
  {
    return idStatoVerbale;
  }

  public void setIdStatoVerbale(Integer idStatoVerbale)
  {
    this.idStatoVerbale = idStatoVerbale;
  }

  public String getDataVerbaleS()
  {
    return IuffiUtils.DATE.formatDate(dataVerbale);
  }
  
  public void setDataVerbaleS(Date dataVerbale)
  {
    this.dataVerbale = dataVerbale;
  }
  
  public long getExtIdDocumentoIndex() {
    return extIdDocumentoIndex;
  }

  public void setExtIdDocumentoIndex(long extIdDocumentoIndex) {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
  }

  public String getNomeFile() {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  public byte[] getPdfJasper()
  {
    return pdfJasper;
  }

  public void setPdfJasper(byte[] pdfJasper)
  {
    this.pdfJasper = pdfJasper;
  }

}
