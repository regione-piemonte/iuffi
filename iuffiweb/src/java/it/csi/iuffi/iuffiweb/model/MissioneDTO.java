package it.csi.iuffi.iuffiweb.model;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.integration.CustomMultipartFile;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;


public class MissioneDTO extends TabelleDTO implements ILoggable
{
	private static final long serialVersionUID = 1L;
	private Long idMissione;
  private Long numeroTrasferta;
  
  @NotNull
  private Date dataOraInizioMissione;
  private Date dataOraFineMissione;
  
  @NotNull
  private Long idIspettoreAssegnato;
  private Date timestampInizioMissione;
  private Date timestampFineMissione;
  private MultipartFile pdfTrasferta;
  private String nomeFile;
  
  @NotNull
  private String oraInizio;
  private String oraFine;
  private String stato;
  private String nomeIspettore;    // nome ispettore assegnato
  private String cognomeIspettore; // cognome ispettore assegnato
  private List<AnagraficaDTO> ispettoriAggiunti;
  
  @JsonIgnore
  private List<Integer> ispettoriSelezionati;
  private List<RilevazioneDTO> rilevazioni;
  
  @JsonIgnore
  private String ispettore;
  private Integer numRilevazioni;
  
  @JsonIgnore
  private String visual;
  @JsonIgnore
  private String campionamento;
  @JsonIgnore
  private String trappolaggio;
  @JsonIgnore
  private String flagEmergenza;

  // Sandro
  @JsonIgnore
  private Integer idStatoVerbale;
  
  @JsonIgnore
  private Integer idVerbale;
  @JsonIgnore
  private String numVerbale;


public MissioneDTO() {
    super();
  }

  public MissioneDTO(String stato)
  {
    super();
    this.stato = stato;
  }

  public MissioneDTO(Long idMissione, Long numeroTrasferta,
      Date dataOraInizioMissione, Date dataOraFineMissione,
      Long idIspettoreAssegnato, Date timestampInizioMissione,
      Date timestampFineMissione, MultipartFile pdfTrasferta, String nomeFile,
      String oraInizio, String oraFine, String stato)
  {
    super();
    this.idMissione = idMissione;
    this.numeroTrasferta = numeroTrasferta;
    this.dataOraInizioMissione = dataOraInizioMissione;
    this.dataOraFineMissione = dataOraFineMissione;
    this.idIspettoreAssegnato = idIspettoreAssegnato;
    this.timestampInizioMissione = timestampInizioMissione;
    this.timestampFineMissione = timestampFineMissione;
    this.pdfTrasferta = pdfTrasferta;
    this.nomeFile = nomeFile;
    this.oraInizio = oraInizio;
    this.oraFine = oraFine;
    this.stato = stato;
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

  public Date getDataOraInizioMissione()
  {
    return dataOraInizioMissione;
  }

  public void setDataOraInizioMissione(Date dataOraInizioMissione)
  {
    this.dataOraInizioMissione = dataOraInizioMissione;
  }

  public Date getDataOraFineMissione()
  {
    return dataOraFineMissione;
  }

  public void setDataOraFineMissione(Date dataOraFineMissione)
  {
    this.dataOraFineMissione = dataOraFineMissione;
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

  public MultipartFile getPdfTrasferta()
  {
    return pdfTrasferta;
  }

  public void setPdfTrasferta(MultipartFile pdfTrasferta)
  {
    this.pdfTrasferta = pdfTrasferta;
  }

  public void setPdfTrasferta(byte[] bytes)
  {
    if (bytes != null) {
      String fileName = (this.nomeFile!=null)?this.nomeFile:"file.pdf";
      CustomMultipartFile customMultipartFile = new CustomMultipartFile(bytes, fileName);
      try {
        customMultipartFile.transferTo(customMultipartFile.getFile());
        this.pdfTrasferta = customMultipartFile;
      } catch (IllegalStateException e) {
          //log.info("IllegalStateException : " + e);
          e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
          //log.info("IOException : " + e);
      }
    } else
        this.pdfTrasferta = null;
  }

  public String getNomeFile()
  {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
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

  public String getStato()
  {
    return stato;
  }

  public void setStato(String stato)
  {
    this.stato = stato;
  }

  public List<AnagraficaDTO> getIspettoriAggiunti()
  {
    return ispettoriAggiunti;
  }

  public void setIspettoriAggiunti(List<AnagraficaDTO> ispettoriAggiunti)
  {
    this.ispettoriAggiunti = ispettoriAggiunti;
  }

  public List<Integer> getIspettoriSelezionati()
  {
    return ispettoriSelezionati;
  }

  public void setIspettoriSelezionati(List<Integer> ispettoriSelezionati)
  {
    this.ispettoriSelezionati = ispettoriSelezionati;
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

  public List<RilevazioneDTO> getRilevazioni()
  {
    return rilevazioni;
  }

  public void setRilevazioni(List<RilevazioneDTO> rilevazioni)
  {
    this.rilevazioni = rilevazioni;
  }
  
  public String getDataInizioMissioneS()
  {
    return IuffiUtils.DATE.formatDate(dataOraInizioMissione);
  }
  
  public void setDataInizioMissioneS(Date dataOraInizioMissione)
  {
    this.dataOraInizioMissione = dataOraInizioMissione;
  }
  
  public String getOraInizioS()
  {
    return (dataOraInizioMissione!=null)?(idMissione!=null || !(new SimpleDateFormat("HH:mm").format(dataOraInizioMissione).equals("00:00"))) ? new SimpleDateFormat("HH:mm").format(dataOraInizioMissione):null:null;
  }
  
  public String getOraFineS()
  {
    return (dataOraFineMissione!=null)?new SimpleDateFormat("HH:mm").format(dataOraFineMissione):null;
  }
  
  public String getIspettore()
  {
    return ispettore;
  }

  public void setIspettore(String ispettore)
  {
    this.ispettore = ispettore;
  }

  public String getCognomeNomeIspettore() {
    return this.cognomeIspettore + " " + this.getNomeIspettore();
  }

  public Integer getNumRilevazioni()
  {
    return numRilevazioni;
  }

  public void setNumRilevazioni(Integer numRilevazioni)
  {
    this.numRilevazioni = numRilevazioni;
  }
  
  public String getDataFineMissioneS()
  {
    return IuffiUtils.DATE.formatDate(dataOraFineMissione);
  }
  
  public void setDataFineMissioneS(Date dataOraFineMissione)
  {
    this.dataOraFineMissione = dataOraFineMissione;
  }

  public String getVisual()
  {
    return visual;
  }

  public void setVisual(String visual)
  {
    this.visual = visual;
  }

  public String getCampionamento()
  {
    return campionamento;
  }

  public void setCampionamento(String campionamento)
  {
    this.campionamento = campionamento;
  }

  public String getTrappolaggio()
  {
    return trappolaggio;
  }

  public void setTrappolaggio(String trappolaggio)
  {
    this.trappolaggio = trappolaggio;
  }

  public String getFlagEmergenza()
  {
    return flagEmergenza;
  }

  public void setFlagEmergenza(String flagEmergenza)
  {
    this.flagEmergenza = flagEmergenza;
  }

  public Integer getIdStatoVerbale() {
	return idStatoVerbale;
  }

  public void setIdStatoVerbale(Integer idStatoVerbale) {
	this.idStatoVerbale = idStatoVerbale;
  }
  
  public Integer getIdVerbale() {
		return idVerbale;
  }

  public void setIdVerbale(Integer idVerbale) {
		this.idVerbale = idVerbale;
  }

  public String getNumeroTrasfertaS() {
    return (numeroTrasferta==null)?null:String.valueOf(numeroTrasferta);
  }

  public String getNumVerbale()
  {
    return numVerbale;
  }

  public void setNumVerbale(String numVerbale)
  {
    this.numVerbale = numVerbale;
  }

}
