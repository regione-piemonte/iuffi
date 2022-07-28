package it.csi.iuffi.iuffiweb.model;

import java.io.IOException;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.integration.CustomMultipartFile;

public class EsitoCampioneDTO extends TabelleDTO implements ILoggable
{

  private static final long serialVersionUID = -3417367044402459761L;

  private Integer idEsitoCampione;
  private Integer idCampionamento;
  private String numRegistroLab;
  private String esito;
  private String nomeFile;
  private MultipartFile referto;
  private String codCampione;
  private Integer idAnfi;
  private String tipologiaTestDiLaboratorio;
  private String descrizione;
  private Integer idCodiceEsito;
  @JsonIgnore
  private String associato;
  @JsonIgnore
  private String modificato;

  @JsonIgnore  
  private byte[] fileByte;


  public EsitoCampioneDTO()
  {
    super();
  }
  
  public Integer getIdCampionamento()
  {
    return idCampionamento;
  }
  public void setIdCampionamento(Integer idCampionamento)
  {
    this.idCampionamento = idCampionamento;
  }
  public Integer getIdEsitoCampione()
  {
    return idEsitoCampione;
  }
  public void setIdEsitoCampione(Integer idEsitoCampione)
  {
    this.idEsitoCampione = idEsitoCampione;
  }
  public String getNumRegistroLab()
  {
    return numRegistroLab;
  }
  public void setNumRegistroLab(String numRegistroLab)
  {
    this.numRegistroLab = numRegistroLab;
  }
  public String getEsito()
  {
    return esito;
  }
  public void setEsito(String esito)
  {
    this.esito = esito;
  }
  public String getNomeFile()
  {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
  }
  public MultipartFile getReferto()
  {
    return referto;
  }
  public void setReferto(MultipartFile referto)
  {
    this.referto = referto;
  }
  public void setReferto(byte[] bytes)
  {
    if (bytes != null) {
      //String fileName = (this.nomeFile!=null)?this.nomeFile:"referto.pdf";
      String fileName = "referto.pdf"; 
      CustomMultipartFile customMultipartFile = new CustomMultipartFile(bytes, fileName);
      try {
        customMultipartFile.transferTo(customMultipartFile.getFile());
        this.referto = customMultipartFile;
      } catch (IllegalStateException e) {
          //log.info("IllegalStateException : " + e);
          e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
          //log.info("IOException : " + e);
      }
    } else
        this.referto = null;
  }

  public String getCodCampione()
  {
    return codCampione;
  }

  public void setCodCampione(String codCampione)
  {
    this.codCampione = codCampione;
  }

  public Integer getIdAnfi()
  {
    return idAnfi;
  }

  public void setIdAnfi(Integer idAnfi)
  {
    this.idAnfi = idAnfi;
  }

  public String getTipologiaTestDiLaboratorio()
  {
    return tipologiaTestDiLaboratorio;
  }

  public void setTipologiaTestDiLaboratorio(String tipologiaTestDiLaboratorio)
  {
    this.tipologiaTestDiLaboratorio = tipologiaTestDiLaboratorio;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public Integer getIdCodiceEsito()
  {
    return idCodiceEsito;
  }

  public void setIdCodiceEsito(Integer idCodiceEsito)
  {
    this.idCodiceEsito = idCodiceEsito;
  }
  
  public String getAssociato()
  {
    return associato;
  }

  public void setAssociato(String associato)
  {
    this.associato = associato;
  }
  
  public byte[] getFileByte()
  {
    return fileByte;
  }

  public void setFileByte(byte[] fileByte)
  {
    this.fileByte = fileByte;
  }
  
  public String getModificato()
  {
    return modificato;
  }

  public void setModificato(String modificato)
  {
    this.modificato = modificato;
  }

}
