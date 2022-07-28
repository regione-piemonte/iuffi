package it.csi.iuffi.iuffiweb.model.api;


import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;


public class Missione implements ILoggable
{

	private static final long serialVersionUID = 1L;

	private Long idMissione;
  private Long numeroTrasferta;
  
  @NotNull
  private String dataMissione;
 
  @NotNull
  private String oraInizio;
  private String oraFine;
  
  private Long idIspettoreAssegnato;
  
  @JsonIgnore
  private Date timestampInizioMissione;
  @JsonIgnore
  private Date timestampFineMissione;
  
  //private MultipartFile pdfTrasferta;
  //private String nomeFile;
  
  //private String stato;
  private String nomeIspettore;    // nome ispettore assegnato
  private String cognomeIspettore; // cognome ispettore assegnato
  @NotNull
  private String cfIspettore;      // codice fiscale ispettore assegnato
  private List<AnagraficaDTO> ispettoriAggiunti;
   
  private Integer numRilevazioni;
  private List<Rilevazione> rilevazioni;
  
  @JsonIgnore
  private Integer pdfSize;
  private boolean pdf;
  
  
  public Missione() {
    super();
  }

  public Long getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Long idMissione)
  {
    this.idMissione = idMissione;
  }

  public Long getNumeroTrasferta()
  {
    return numeroTrasferta;
  }

  public void setNumeroTrasferta(Long numeroTrasferta)
  {
    this.numeroTrasferta = numeroTrasferta;
  }

  public String getDataMissione()
  {
    return dataMissione;
  }

  public void setDataMissione(String dataMissione)
  {
    this.dataMissione = dataMissione;
  }

  public String getOraInizio()
  {
    return oraInizio;
  }

  public void setOraInizio(String oraInizio)
  {
    this.oraInizio = oraInizio;
  }

  public String getOraFine()
  {
    return oraFine;
  }

  public void setOraFine(String oraFine)
  {
    this.oraFine = oraFine;
  }

  public Long getIdIspettoreAssegnato()
  {
    return idIspettoreAssegnato;
  }

  public void setIdIspettoreAssegnato(Long idIspettoreAssegnato)
  {
    this.idIspettoreAssegnato = idIspettoreAssegnato;
  }

  public Date getTimestampInizioMissione()
  {
    return timestampInizioMissione;
  }

  public void setTimestampInizioMissione(Date timestampInizioMissione)
  {
    this.timestampInizioMissione = timestampInizioMissione;
  }

  public Date getTimestampFineMissione()
  {
    return timestampFineMissione;
  }

  public void setTimestampFineMissione(Date timestampFineMissione)
  {
    this.timestampFineMissione = timestampFineMissione;
  }

  public String getNomeIspettore()
  {
    return nomeIspettore;
  }

  public void setNomeIspettore(String nomeIspettore)
  {
    this.nomeIspettore = nomeIspettore;
  }

  public String getCognomeIspettore()
  {
    return cognomeIspettore;
  }

  public void setCognomeIspettore(String cognomeIspettore)
  {
    this.cognomeIspettore = cognomeIspettore;
  }

  public List<AnagraficaDTO> getIspettoriAggiunti()
  {
    return ispettoriAggiunti;
  }

  public void setIspettoriAggiunti(List<AnagraficaDTO> ispettoriAggiunti)
  {
    this.ispettoriAggiunti = ispettoriAggiunti;
  }

  public List<Rilevazione> getRilevazioni()
  {
    return rilevazioni;
  }

  public void setRilevazioni(List<Rilevazione> rilevazioni)
  {
    this.rilevazioni = rilevazioni;
  }

  public Integer getNumRilevazioni()
  {
    return numRilevazioni;
  }

  public void setNumRilevazioni(Integer numRilevazioni)
  {
    this.numRilevazioni = numRilevazioni;
  }

  public String getCfIspettore()
  {
    return cfIspettore;
  }

  public void setCfIspettore(String cfIspettore)
  {
    this.cfIspettore = cfIspettore;
  }

  public Integer getPdfSize()
  {
    return pdfSize;
  }

  public void setPdfSize(Integer pdfSize)
  {
    this.pdfSize = pdfSize;
    this.pdf = (pdfSize!=null && pdfSize.intValue()>0);
  }

  public boolean getPdf()
  {
    return (this.getPdfSize()!=null && this.getPdfSize().intValue()>0);
  }

}
