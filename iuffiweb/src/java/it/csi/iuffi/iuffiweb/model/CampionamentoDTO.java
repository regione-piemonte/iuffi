package it.csi.iuffi.iuffiweb.model;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class CampionamentoDTO extends TabelleDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4621339630061084884L;
  
  private Integer idCampionamento;
  private Integer idRilevazione;
  private Integer idSpecieVegetale;
  private Integer idTipoCampione;
  private String istatComune;
  private String comune;
  private Double latitudine;
  private Double longitudine;
  
  private Date dataRilevazione;
  private Date dataMissione;
  private String pianta;
  
  private Integer idIspezioneVisiva;
  private Integer idAnagrafica;
  private String ispettoreCampionamento;
  
  //aggiuntivi
  private String ispettore;
  private String specie;
  private String area;
  private String dettaglioArea;
  private String organismi;
  private String tipoCampione;
  private String esito;
  private Boolean foto;
  private String fotoTrovata;
  private Integer idMissione;
  private Integer idRegistroCampioni;
  private String anagraficaAziendale;
  private Integer idTecnicoLab;
  private Integer idOrganismoNocivo;
  private Integer superficie; 
  private Integer numeroPiante;
  //ricerca
  private Integer anno;
  private Date dallaData;
  private Date allaData;
  private String dallaDataS;
  private String allaDataS;
  private List<Integer> tipoArea;
  private List<Integer> ispettoreAssegnato;
  private List<Integer> ispettoriSecondari;
  private List<Integer> specieVegetale;
  private List<Integer> organismoNocivo;
  private List<Integer> trappole;
  private List<Integer> campioni;  
  @JsonIgnore
  private List<Integer> ispettoreOperante;
  private Integer idSpecieOn;
  private String presenza;
  private Integer[] organismiNocivi;
  
  //Stefano
  private Date dataOraInizio;
  private Date dataOraFine;
  //private String dataCampionamento;
  private String oraInizio;
  private String oraFine;
  @JsonIgnore
  private String oraInizioMissione;
  @JsonIgnore
  private String oraFineMissione;
  @JsonIgnore
  private String extNumeroAviv;
  @JsonIgnore
  private String cuaa;
  @JsonIgnore
  private String ispettoriAggiunti;
  @JsonIgnore
  private Integer idTipoArea;
  private String note;

  // Sandro
  @JsonIgnore
  private Integer idStatoVerbale;

  public CampionamentoDTO()
  {
    super();
  }


  public CampionamentoDTO(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }


  public CampionamentoDTO(Integer idCampionamento, Integer idRilevazione,
      Integer idSpecieVegetale, Integer idTipoCampione, String istatComune,
      Integer idCategoria, Double latitudine,
      Double longitudine, Date dataRilevazione)
  {
    super();
    this.idCampionamento = idCampionamento;
    this.idRilevazione = idRilevazione;
    this.idSpecieVegetale = idSpecieVegetale;
    this.idTipoCampione = idTipoCampione;
    this.istatComune = istatComune;
    this.latitudine = latitudine;
    this.longitudine = longitudine;
    this.dataRilevazione = dataRilevazione;
  }

  
  public Integer getIdCampionamento()
  {
    return idCampionamento;
  }
  public void setIdCampionamento(Integer idCampionamento)
  {
    this.idCampionamento = idCampionamento;
  }
  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }
  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }
  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }
  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }
  public Integer getIdTipoCampione()
  {
    return idTipoCampione;
  }
  public void setIdTipoCampione(Integer idTipoCampione)
  {
    this.idTipoCampione = idTipoCampione;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
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
  public Date getDataRilevazione()
  {
    return dataRilevazione;
  }
  public void setDataRilevazione(Date dataRilevazione)
  {
    this.dataRilevazione = dataRilevazione;
  }
  public String getIspettore()
  {
    return ispettore;
  }

  public void setIspettore(String ispettore)
  {
    this.ispettore = ispettore;
  }

  public String getSpecie()
  {
    return specie;
  }

  public void setSpecie(String specie)
  {
    this.specie = specie;
  }

  public String getArea()
  {
    return area;
  }

  public void setArea(String area)
  {
    this.area = area;
  }

  public String getOrganismi()
  {
    return organismi;
  }

  public void setOrganismi(String organismi)
  {
    this.organismi = organismi;
  }

  public String getEsito()
  {
    return esito;
  }

  public void setEsito(String esito)
  {
    this.esito = esito;
  }

  public Integer getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Integer idMissione)
  {
    this.idMissione = idMissione;
  }

  public Integer getIdRegistroCampioni()
  {
    return idRegistroCampioni;
  }

  public void setIdRegistroCampioni(Integer idRegistroCampioni)
  {
    this.idRegistroCampioni = idRegistroCampioni;
  }

  public String getAnagraficaAziendale()
  {
    return anagraficaAziendale;
  }

  public void setAnagraficaAziendale(String anagraficaAziendale)
  {
    this.anagraficaAziendale = anagraficaAziendale;
  }

  public Integer getIdTecnicoLab()
  {
    return idTecnicoLab;
  }

  public void setIdTecnicoLab(Integer idTecnicoLab)
  {
    this.idTecnicoLab = idTecnicoLab;
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
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

  public String getTipoCampione()
  {
    return tipoCampione;
  }

  public void setTipoCampione(String tipoCampione)
  {
    this.tipoCampione = tipoCampione;
  }

  public String getDataRilevazioneF()
  {
    return IuffiUtils.DATE.formatDate(dataRilevazione);
  }
  
  public void setDataRilevazioneF(Date dataRilevazione)
  {
    this.dataRilevazione = dataRilevazione;
  }

  public String getComune()
  {
    return comune;
  }

  public void setComune(String comune)
  {
    this.comune = comune;
  }

  public Integer getIdIspezioneVisiva()
  {
    return idIspezioneVisiva;
  }

  public void setIdIspezioneVisiva(Integer idIspezioneVisiva)
  {
    this.idIspezioneVisiva = idIspezioneVisiva;
  }

  public String getDettaglioArea()
  {
    return dettaglioArea;
  }

  public void setDettaglioArea(String dettaglioArea)
  {
    this.dettaglioArea = dettaglioArea;
  }

  public Date getDataMissione()
  {
    return dataMissione;
  }

  public void setDataMissione(Date dataMissione)
  {
    this.dataMissione = dataMissione;
  }

  public String getPianta()
  {
    return pianta;
  }

  public void setPianta(String pianta)
  {
    this.pianta = pianta;
  }
  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }

  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
  }

  public String getIspettoreCampionamento()
  {
    return ispettoreCampionamento;
  }

  public void setIspettoreCampionamento(String ispettoreCampionamento)
  {
    this.ispettoreCampionamento = ispettoreCampionamento;
  }

  public Integer getIdSpecieOn()
  {
    return idSpecieOn;
  }

  public void setIdSpecieOn(Integer idSpecieOn)
  {
    this.idSpecieOn = idSpecieOn;
  }

  public String getPresenza()
  {
    return presenza;
  }

  public void setPresenza(String presenza)
  {
    this.presenza = presenza;
  }
  
  public Integer[] getOrganismiNocivi()
  {
    return organismiNocivi;
  }

  public void setOrganismiNocivi(Integer[] organismiNocivi)
  {
    this.organismiNocivi = organismiNocivi;
  }


  public Boolean getFoto()
  {
    return foto;
  }


  public void setFoto(Boolean foto)
  {
    this.foto = foto;
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
  

//  public String getDataCampionamento()
//  {
//    return dataCampionamento;
//  }
//
//
//  public void setDataCampionamento(String dataCampionamento)
//  {
//    this.dataCampionamento = dataCampionamento;
//  }


  public String getOraInizio()
  {
    return oraInizio;
  }


  public void setOraInizio(String oraInizio)
  {
    this.oraInizio = oraInizio;
  }
  
  
  public String getIspettoriAggiunti()
  {
    return ispettoriAggiunti;
  }


  public void setIspettoriAggiunti(String ispettoriAggiunti)
  {
    this.ispettoriAggiunti = ispettoriAggiunti;
  }



  public String getOraFine()
  {
    return oraFine;
  }


  public void setOraFine(String oraFine)
  {
    this.oraFine = oraFine;
  }
  
  public String getDataInizio()
  {
    return IuffiUtils.DATE.formatDate(dataOraInizio);
  }


  public String getFotoTrovata()
  {
    return fotoTrovata;
  }


  public void setFotoTrovata(String fotoTrovata)
  {
    this.fotoTrovata = fotoTrovata;
  }

  
  public String getDataInizioF()
  {
    return IuffiUtils.DATE.formatDate(dataOraInizio);
  }
  
  public void setDataInizioF(Date dataOraInizio)
  {
    this.dataOraInizio = dataOraInizio;
  }

  public String getExtNumeroAviv()
  {
    return extNumeroAviv;
  }


  public void setExtNumeroAviv(String extNumeroAviv)
  {
    this.extNumeroAviv = extNumeroAviv;
  }


  public String getCuaa()
  {
    return cuaa;
  }


  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
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


  public Integer getIdTipoArea()
  {
    return idTipoArea;
  }
  public void setIdTipoArea(Integer idTipoArea)
  {
    this.idTipoArea = idTipoArea;
  }


  public List<Integer> getIspettoreOperante()
  {
    return ispettoreOperante;
  }
  public void setIspettoreOperante(List<Integer> ispettoreOperante)
  {
    this.ispettoreOperante = ispettoreOperante;
  }


  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }


  public Integer getIdStatoVerbale()
  {
    return idStatoVerbale;
  }


  public void setIdStatoVerbale(Integer idStatoVerbale)
  {
    this.idStatoVerbale = idStatoVerbale;
  }

  public Integer getSuperficie()
  {
    return superficie;
  }


  public void setSuperficie(Integer superficie)
  {
    this.superficie = superficie;
  }


  public Integer getNumeroPiante()
  {
    return numeroPiante;
  }


  public void setNumeroPiante(Integer numeroPiante)
  {
    this.numeroPiante = numeroPiante;
  }


}
