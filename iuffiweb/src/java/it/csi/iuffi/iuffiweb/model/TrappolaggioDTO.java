package it.csi.iuffi.iuffiweb.model;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class TrappolaggioDTO extends TabelleDTO implements ILoggable
{

  private static final long serialVersionUID = -4605722644901668828L;
  
  private Integer idTrappolaggio;
  private Integer idRilevazione;
  private Long idMissione;
  private TrappolaDTO trappola;
  private String istatComune;
  //@NotNull
  private Date dataOraInizio;
  private Date dataOraFine;
  
  private Integer idOperazione;
  private Date dataTrappolaggio;
  private String note;
  private Integer idIspezioneVisiva;
  private Integer idAnagrafica;
  private Integer idOrganismoNocivo;
  @JsonIgnore
  private String nomeLatino;
  @JsonIgnore
  private String sigla;
 //aggiuntivi
  private String descrTrappola;
  private String descrOperazione;
  private String specie;
  private String pianta;
  private Boolean foto;
  @JsonIgnore
  private Integer idTrappola;
  @JsonIgnore
  private Date dataMissione;

  @JsonIgnore
  private String area;
  @JsonIgnore
  private String dettaglioArea;

  //ricerca
  @JsonIgnore
  private Integer anno;
  @JsonIgnore
  private Date dallaData;
  @JsonIgnore
  private Date allaData;
  @JsonIgnore
  private String dallaDataS;
  @JsonIgnore
  private String allaDataS;
  private String ispettore;
  @JsonIgnore
  private String ispettoreMissione;
  @JsonIgnore
  private List<Integer> tipoArea;
  @JsonIgnore
  private List<Integer> ispettoreAssegnato;
  @JsonIgnore
  private List<Integer> ispettoriSecondari;
  @JsonIgnore
  private List<Integer> specieVegetale;
  @JsonIgnore
  private List<Integer> organismoNocivo;
  @JsonIgnore
  private List<Integer> trappole;
  @JsonIgnore
  private List<Integer> campioni;   
  @JsonIgnore
  private String descrComune;
  @JsonIgnore
  private String organismi;
  private String oraInizio;
  private String oraFine;
  @JsonIgnore
  private String oraInizioMissione;
  @JsonIgnore
  private String oraFineMissione;
  @JsonIgnore
  private Integer idTipoArea;  
  @JsonIgnore  
  private Integer idSpecieVegetale;  
  @JsonIgnore
  private String codiceSfr;
  @JsonIgnore
  private String codiceTrappola;
  @JsonIgnore
  private Double latitudine;
  @JsonIgnore
  private Double longitudine;
  @JsonIgnore
  private String dettaglioTipoArea;

  // Sandro
  @JsonIgnore
  private Integer idStatoVerbale;


  public TrappolaggioDTO()
  {
    super();
  }
  


  public String getOraInizioMissione()
{
  return oraInizioMissione;
}



public void setOraInizioMissione(String oraInizioMissione)
{
  this.oraInizioMissione = oraInizioMissione;
}



public String getOraFineMissione()
{
  return oraFineMissione;
}



public void setOraFineMissione(String oraFineMissione)
{
  this.oraFineMissione = oraFineMissione;
}



  public TrappolaggioDTO(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }

  public Integer getAnno()
  {
    return anno;
  }

  public void setAnno(Integer anno)
  {
    this.anno = anno;
  }

  public Date getDallaData()
  {
    return dallaData;
  }

  public void setDallaData(Date dallaData)
  {
    this.dallaData = dallaData;
  }

  public Date getAllaData()
  {
    return allaData;
  }

  public void setAllaData(Date allaData)
  {
    this.allaData = allaData;
  }

  public String getDallaDataS()
  {
    return dallaDataS;
  }

  public void setDallaDataS(String dallaDataS)
  {
    this.dallaDataS = dallaDataS;
  }

  public String getAllaDataS()
  {
    return allaDataS;
  }

  public void setAllaDataS(String allaDataS)
  {
    this.allaDataS = allaDataS;
  }

  public List<Integer> getTipoArea()
  {
    return tipoArea;
  }

  public void setTipoArea(List<Integer> tipoArea)
  {
    this.tipoArea = tipoArea;
  }

  public List<Integer> getIspettoreAssegnato()
  {
    return ispettoreAssegnato;
  }

  public void setIspettoreAssegnato(List<Integer> ispettoreAssegnato)
  {
    this.ispettoreAssegnato = ispettoreAssegnato;
  }

  public List<Integer> getIspettoriSecondari()
  {
    return ispettoriSecondari;
  }

  public void setIspettoriSecondari(List<Integer> ispettoriSecondari)
  {
    this.ispettoriSecondari = ispettoriSecondari;
  }

  public List<Integer> getSpecieVegetale()
  {
    return specieVegetale;
  }

  public void setSpecieVegetale(List<Integer> specieVegetale)
  {
    this.specieVegetale = specieVegetale;
  }

  public List<Integer> getOrganismoNocivo()
  {
    return organismoNocivo;
  }

  public void setOrganismoNocivo(List<Integer> organismoNocivo)
  {
    this.organismoNocivo = organismoNocivo;
  }

  public List<Integer> getTrappole()
  {
    return trappole;
  }

  public void setTrappole(List<Integer> trappole)
  {
    this.trappole = trappole;
  }

  public List<Integer> getCampioni()
  {
    return campioni;
  }

  public void setCampioni(List<Integer> campioni)
  {
    this.campioni = campioni;
  }

  public Integer getIdTrappolaggio()
  {
    return idTrappolaggio;
  }
  public void setIdTrappolaggio(Integer idTrappolaggio)
  {
    this.idTrappolaggio = idTrappolaggio;
  }
  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }
  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }
  public TrappolaDTO getTrappola()
  {
    return trappola;
  }
  public void setTrappola(TrappolaDTO trappola)
  {
    this.trappola = trappola;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public Date getDataOraInizio()
  {
    return dataOraInizio;
  }
  public void setDataOraInizio(Date dataOraInizio)
  {
    this.dataOraInizio = dataOraInizio;
  }
  public Date getDataOraFine()
  {
    return dataOraFine;
  }
  public void setDataOraFine(Date dataOraFine)
  {
    this.dataOraFine = dataOraFine;
  }
  
  public Integer getIdOperazione()
  {
    return idOperazione;
  }

  public void setIdOperazione(Integer idOperazione)
  {
    this.idOperazione = idOperazione;
  }

  public String getSpecie()
  {
    return specie;
  }

  public void setSpecie(String specie)
  {
    this.specie = specie;
  }

  public String getDescrTrappola()
  {
    return descrTrappola;
  }

  public void setDescrTrappola(String descrTrappola)
  {
    this.descrTrappola = descrTrappola;
  }

  public String getIspettore()
  {
    return ispettore;
  }

  public void setIspettore(String ispettore)
  {
    this.ispettore = ispettore;
  }

  public String getDataOraInizioF()
  {
    return IuffiUtils.DATE.formatDate(dataOraInizio);
  }
  
  public String getDataOraInizioTF()
  {
    return IuffiUtils.DATE.formatDateTime(dataOraInizio);
  }
  
  public String getDataOraFineTF()
  {
    return IuffiUtils.DATE.formatDateTime(dataOraFine);
  }
  
  public void setDataOraInizioF(Date dataOraInizio)
  {
    this.dataOraInizio = dataOraInizio;
  }

  public String getDataTrappolaggioF()
  {
    return IuffiUtils.DATE.formatDate(dataTrappolaggio);
  }

  
  public String getDescrComune()
  {
    return descrComune;
  }

  public void setDescrComune(String descrComune)
  {
    this.descrComune = descrComune;
  }

  public String getOrganismi()
  {
    return organismi;
  }

  public void setOrganismi(String organismi)
  {
    this.organismi = organismi;
  }

  public Date getDataTrappolaggio()
  {
    return dataTrappolaggio;
  }

  public void setDataTrappolaggio(Date dataTrappolaggio)
  {
    this.dataTrappolaggio = dataTrappolaggio;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Integer getIdIspezioneVisiva()
  {
    return idIspezioneVisiva;
  }

  public void setIdIspezioneVisiva(Integer idIspezioneVisiva)
  {
    this.idIspezioneVisiva = idIspezioneVisiva;
  }

  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }

  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
  }

  public Long getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Long idMissione)
  {
    this.idMissione = idMissione;
  }

  public String getDescrOperazione()
  {
    return descrOperazione;
  }

  public void setDescrOperazione(String descrOperazione)
  {
    this.descrOperazione = descrOperazione;
  }

  public Date getDataMissione()
  {
    return dataMissione;
  }

  public void setDataMissione(Date dataMissione)
  {
    this.dataMissione = dataMissione;
  }

  public String getArea()
  {
    return area;
  }

  public void setArea(String area)
  {
    this.area = area;
  }

  public String getDettaglioArea()
  {
    return dettaglioArea;
  }

  public void setDettaglioArea(String dettaglioArea)
  {
    this.dettaglioArea = dettaglioArea;
  }

  public String getPianta()
  {
    return pianta;
  }

  public void setPianta(String pianta)
  {
    this.pianta = pianta;
  }

  public Integer getIdTrappola()
  {
    return idTrappola;
  }

  public void setIdTrappola(Integer idTrappola)
  {
    this.idTrappola = idTrappola;
  }

  public Boolean getFoto()
  {
    return foto;
  }

  public void setFoto(Boolean foto)
  {
    this.foto = foto;
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  public String getNomeLatino()
  {
    return nomeLatino;
  }

  public void setNomeLatino(String nomeLatino)
  {
    this.nomeLatino = nomeLatino;
  }

  public String getSigla()
  {
    return sigla;
  }

  public void setSigla(String sigla)
  {
    this.sigla = sigla;
  }
  
  
  public String getOraInizio() 
  {  
	return oraInizio;
  }

  public void setOraInizio(String oraInizio) 
  {
	this.oraInizio = oraInizio;
  }

  public String getDataOraInizioT()
  {  
    long ts = dataOraInizio.getTime();  
    
    return String.valueOf(ts);
  }

  public String getOraFine()
  {
    return oraFine;
  }

  public void setOraFine(String oraFine)
  {
    this.oraFine = oraFine;
  }

  public Integer getIdTipoArea()
  {
    return idTipoArea;
  }

  public void setIdTipoArea(Integer idTipoArea)
  {
    this.idTipoArea = idTipoArea;
  }

  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }

  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }

  public String getIspettoreMissione()
  {
    return ispettoreMissione;
  }

  public void setIspettoreMissione(String ispettoreMissione)
  {
    this.ispettoreMissione = ispettoreMissione;
  }
  public String getCodiceSfr()
  {
    return codiceSfr;
  }


  public void setCodiceSfr(String codiceSfr)
  {
    this.codiceSfr = codiceSfr;
  }
  
  public String getCodiceTrappola()
  {
    return codiceTrappola;
  }



  public void setCodiceTrappola(String codiceTrappola)
  {
    this.codiceTrappola = codiceTrappola;
  }

  public Double getLatitudine()
  {
    return latitudine;
  }



  public void setLatitudine(Double latitudine)
  {
    this.latitudine = latitudine;
  }



  public Double getLongitudine()
  {
    return longitudine;
  }



  public void setLongitudine(Double longitudine)
  {
    this.longitudine = longitudine;
  }


  public String getDettaglioTipoArea()
  {
    return dettaglioTipoArea;
  }



  public void setDettaglioTipoArea(String dettaglioTipoArea)
  {
    this.dettaglioTipoArea = dettaglioTipoArea;
  }



  public Integer getIdStatoVerbale()
  {
    return idStatoVerbale;
  }



  public void setIdStatoVerbale(Integer idStatoVerbale)
  {
    this.idStatoVerbale = idStatoVerbale;
  }


  
}
