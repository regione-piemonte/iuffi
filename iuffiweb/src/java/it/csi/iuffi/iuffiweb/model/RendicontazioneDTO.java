package it.csi.iuffi.iuffiweb.model;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RendicontazioneDTO extends TabelleStoricizzateDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4621339630061084884L;
  
  private String numeroVerbale;
  private Date dataMissione;
  private String numeroTrasferta;
  private Double oreTotaliSiti;
  private String oreTotaliSitiS;
  private Double oreOperazioni;
  private Double pesoVerbale; 
  private Double pesoRiga; 
  private Double numOreIspezione; 
  private Double costoOrarioTecIspezione; 
  private Double totaleCostoIspezione; 
  private String nomeLatinoSpecie; 
  private String onSpecie;
  private String onNoUeSpecie;
  private Integer numOnRimborso;
  private Double numOreRaccoltaCampioni;
  private Double costoOrarioTecCampionamento;
  private Double totaleCostoRaccCampioni;
  private String tipoCampione;
  private Integer numCampioni;
  //trappole
  private Double costoSingolaTrappola;
  private Double costoOrarioTecTrappolaggio;
  private Integer numPiazzamento;
  private Integer numRicSostTrappole;
  private Integer numRimozioneTrappole;
  
  private Double numOrePiazzTrappole;
  private Double numOreRicSostTrappole;
  private Double numOreRimozioneTrappole;
  private String descTipoArea;
  private Double costoTotalePiazzTrappole;
  private Double costoTotaleRicSostTrappole;
  private Double costoTotaleRitiroTrappole; 
  private Double costoLaboratorio;  
  private Double totaleCostoEsami;
  private String ispettore;
  private String latitudine;
  private String longitudine;
  private Integer numeroPiante;  
  private Double superfice;
  private String tipologiaTrappola;
  private String codiceSfr;
  private Integer anno;
  private Integer velocita;
  
  //test laboratorio
  private String testLab;
  private String numRegistroLab;
  private String codCampione;
  private String organismiCampione; 
  private String tipo;
  private String organismoTrappola;   
  private BigDecimal latitudinebd;
  private BigDecimal longitudinebd;
  private String subcontractor;
  
  // Aggiunto per filtro di ricerca area indenne/demarcata/tutte
  private String area;

  private Long idCampionamento;
  private String codiceTrappola;
  
  private String codiceUfficiale;
  private String typologyOfLocation;

  private String flagEmergenza;
  private Integer numIspettori;
  private Integer idMissione;
  private Integer idRilevazione;
  
  public String getNumeroVerbale()
  {
    return numeroVerbale;
  }
  public void setNumeroVerbale(String numeroVerbale)
  {
    this.numeroVerbale = numeroVerbale;
  }
  public Double getOreTotaliSiti()
  {
    return oreTotaliSiti;
  }
  public void setOreTotaliSiti(Double oreTotaliSiti)
  {
    this.oreTotaliSiti = oreTotaliSiti;
  }
  public Double getOreOperazioni()
  {
    return oreOperazioni;
  }
  public void setOreOperazioni(Double oreOperazioni)
  {
    this.oreOperazioni = oreOperazioni;
  }
  public Double getPesoVerbale()
  {
    return pesoVerbale;
  }
  public void setPesoVerbale(Double pesoVerbale)
  {
    this.pesoVerbale = pesoVerbale;
  }
  public Double getPesoRiga()
  {
    return pesoRiga;
  }
  public void setPesoRiga(Double pesoRiga)
  {
    this.pesoRiga = pesoRiga;
  }
  public Double getNumOreIspezione()
  {
    return numOreIspezione;
  }
  public void setNumOreIspezione(Double numOreIspezione)
  {
    this.numOreIspezione = numOreIspezione;
  }
  public Double getCostoOrarioTecIspezione()
  {
    return costoOrarioTecIspezione;
  }
  public void setCostoOrarioTecIspezione(Double costoOrarioTecIspezione)
  {
    this.costoOrarioTecIspezione = costoOrarioTecIspezione;
  }
  public Double getTotaleCostoIspezione()
  {
    return totaleCostoIspezione;
  }
  public void setTotaleCostoIspezione(Double totaleCostoIspezione)
  {
    this.totaleCostoIspezione = totaleCostoIspezione;
  }
  public String getNomeLatinoSpecie()
  {
    return nomeLatinoSpecie;
  }
  public void setNomeLatinoSpecie(String nomeLatinoSpecie)
  {
    this.nomeLatinoSpecie = nomeLatinoSpecie;
  }
  public String getOnSpecie()
  {
    return onSpecie;
  }
  public void setOnSpecie(String onSpecie)
  {
    this.onSpecie = onSpecie;
  }
  public String getOnNoUeSpecie()
  {
    return onNoUeSpecie;
  }
  public void setOnNoUeSpecie(String onNoUeSpecie)
  {
    this.onNoUeSpecie = onNoUeSpecie;
  }
  public Integer getNumOnRimborso()
  {
    return numOnRimborso;
  }
  public void setNumOnRimborso(Integer numOnRimborso)
  {
    this.numOnRimborso = numOnRimborso;
  }
  public Double getNumOreRaccoltaCampioni()
  {
    return numOreRaccoltaCampioni;
  }
  public void setNumOreRaccoltaCampioni(Double numOreRaccoltaCampioni)
  {
    this.numOreRaccoltaCampioni = numOreRaccoltaCampioni;
  }
  public Double getCostoOrarioTecCampionamento()
  {
    return costoOrarioTecCampionamento;
  }
  public void setCostoOrarioTecCampionamento(Double costoOrarioTecCampionamento)
  {
    this.costoOrarioTecCampionamento = costoOrarioTecCampionamento;
  }
  public Double getTotaleCostoRaccCampioni()
  {
    return totaleCostoRaccCampioni;
  }
  public void setTotaleCostoRaccCampioni(Double totaleCostoRaccCampioni)
  {
    this.totaleCostoRaccCampioni = totaleCostoRaccCampioni;
  }
  public String getTipoCampione()
  {
    return tipoCampione;
  }
  public void setTipoCampione(String tipoCampione)
  {
    this.tipoCampione = tipoCampione;
  }
  public Double getCostoSingolaTrappola()
  {
    return costoSingolaTrappola;
  }
  public void setCostoSingolaTrappola(Double costoSingolaTrappola)
  {
    this.costoSingolaTrappola = costoSingolaTrappola;
  }

 public Double getCostoOrarioTecTrappolaggio()
  {
    return costoOrarioTecTrappolaggio;
  }
  public void setCostoOrarioTecTrappolaggio(Double costoOrarioTecTrappolaggio)
  {
    this.costoOrarioTecTrappolaggio = costoOrarioTecTrappolaggio;
  }
  public Double getCostoTotaleRitiroTrappole()
  {
    return costoTotaleRitiroTrappole;
  }
  public void setCostoTotaleRitiroTrappole(Double costoTotaleRitiroTrappole)
  {
    this.costoTotaleRitiroTrappole = costoTotaleRitiroTrappole;
  }
  public Double getCostoLaboratorio()
  {
    return costoLaboratorio;
  }
  public void setCostoLaboratorio(Double costoLaboratorio)
  {
    this.costoLaboratorio = costoLaboratorio;
  }
  public Double getTotaleCostoEsami()
  {
    return totaleCostoEsami;
  }
  public void setTotaleCostoEsami(Double totaleCostoEsami)
  {
    this.totaleCostoEsami = totaleCostoEsami;
  }
  public Integer getNumPiazzamento()
  {
    return numPiazzamento;
  }
  public void setNumPiazzamento(Integer numPiazzamento)
  {
    this.numPiazzamento = numPiazzamento;
  }
  public Integer getNumRicSostTrappole()
  {
    return numRicSostTrappole;
  }
  public void setNumRicSostTrappole(Integer numRicSostTrappole)
  {
    this.numRicSostTrappole = numRicSostTrappole;
  }
  public Integer getNumRimozioneTrappole()
  {
    return numRimozioneTrappole;
  }
  public void setNumRimozioneTrappole(Integer numRimozioneTrappole)
  {
    this.numRimozioneTrappole = numRimozioneTrappole;
  }
  public Double getNumOrePiazzTrappole()
  {
    return numOrePiazzTrappole;
  }
  public void setNumOrePiazzTrappole(Double numOrePiazzTrappole)
  {
    this.numOrePiazzTrappole = numOrePiazzTrappole;
  }
  public Double getNumOreRicSostTrappole()
  {
    return numOreRicSostTrappole;
  }
  public void setNumOreRicSostTrappole(Double numOreRicSostTrappole)
  {
    this.numOreRicSostTrappole = numOreRicSostTrappole;
  }
  public Double getCostoTotalePiazzTrappole()
  {
    return costoTotalePiazzTrappole;
  }
  public void setCostoTotalePiazzTrappole(Double costoTotalePiazzTrappole)
  {
    this.costoTotalePiazzTrappole = costoTotalePiazzTrappole;
  }
  public Double getNumOreRimozioneTrappole()
  {
    return numOreRimozioneTrappole;
  }
  public void setNumOreRimozioneTrappole(Double numOreRimozioneTrappole)
  {
    this.numOreRimozioneTrappole = numOreRimozioneTrappole;
  }
  public Double getCostoTotaleRicSostTrappole()
  {
    return costoTotaleRicSostTrappole;
  }
  public void setCostoTotaleRicSostTrappole(Double costoTotaleRicSostTrappole)
  {
    this.costoTotaleRicSostTrappole = costoTotaleRicSostTrappole;
  }
  public Integer getNumCampioni()
  {
    return numCampioni;
  }
  public void setNumCampioni(Integer numCampioni)
  {
    this.numCampioni = numCampioni;
  }
  public String getOreTotaliSitiS()
  {
    return oreTotaliSitiS;
  }
  public void setOreTotaliSitiS(String oreTotaliSitiS)
  {
    this.oreTotaliSitiS = oreTotaliSitiS;
  }
  public String getIspettore()
  {
    return ispettore;
  }
  public void setIspettore(String ispettore)
  {
    this.ispettore = ispettore;
  }
  public String getLatitudine()
  {
    return latitudine;
  }
  public void setLatitudine(String latitudine)
  {
    this.latitudine = latitudine;
  }
  public String getLongitudine()
  {
    return longitudine;
  }
  public void setLongitudine(String longitudine)
  {
    this.longitudine = longitudine;
  }
  public Integer getNumeroPiante()
  {
    return numeroPiante;
  }
  public void setNumeroPiante(Integer numeroPiante)
  {
    this.numeroPiante = numeroPiante;
  }
  public Double getSuperfice()
  {
    return superfice;
  }
  public void setSuperfice(Double superfice)
  {
    this.superfice = superfice;
  }
  public String getTipologiaTrappola()
  {
    return tipologiaTrappola;
  }
  public void setTipologiaTrappola(String tipologiaTrappola)
  {
    this.tipologiaTrappola = tipologiaTrappola;
  }
  public String getDescTipoArea()
  {
    return descTipoArea;
  }
  public void setDescTipoArea(String descTipoArea)
  {
    this.descTipoArea = descTipoArea;
  }
  public Integer getAnno()
  {
    return anno;
  }
  public void setAnno(Integer anno)
  {
    this.anno = anno;
  }
  public Integer getVelocita()
  {
    return velocita;
  }
  public void setVelocita(Integer velocita)
  {
    this.velocita = velocita;
  }
  public String getTestLab()
  {
    return testLab;
  }
  public void setTestLab(String testLab)
  {
    this.testLab = testLab;
  }
  public String getCodCampione()
  {
    return codCampione;
  }
  public void setCodCampione(String codCampione)
  {
    this.codCampione = codCampione;
  }
  public String getOrganismiCampione()
  {
    return organismiCampione;
  }
  public void setOrganismiCampione(String organismiCampione)
  {
    this.organismiCampione = organismiCampione;
  }
  public String getTipo()
  {
    return tipo;
  }
  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }
  public String getOrganismoTrappola()
  {
    return organismoTrappola;
  }
  public void setOrganismoTrappola(String organismoTrappola)
  {
    this.organismoTrappola = organismoTrappola;
  }
  public Date getDataMissione()
  {
    return dataMissione;
  }
  public void setDataMissione(Date dataMissione)
  {
    this.dataMissione = dataMissione;
  }
  public String getDataMissioneS()
  {
    return IuffiUtils.DATE.formatDate(dataMissione);
  }
  public String getNumeroTrasferta()
  {
    return numeroTrasferta;
  }
  public void setNumeroTrasferta(String numeroTrasferta)
  {
    this.numeroTrasferta = numeroTrasferta;
  }
  public String getCodiceSfr()
  {
    return codiceSfr;
  }
  public void setCodiceSfr(String codiceSfr)
  {
    this.codiceSfr = codiceSfr;
  }
  public String getNumRegistroLab()
  {
    return numRegistroLab;
  }
  public void setNumRegistroLab(String numRegistroLab)
  {
    this.numRegistroLab = numRegistroLab;
  }
  public BigDecimal getLatitudinebd() {
    return this.latitudinebd;
  }
  public BigDecimal getLongitudinebd() {
    return this.longitudinebd;
  }
  public void setLatitudinebd(BigDecimal latitudinebd)
  {
    this.latitudinebd = latitudinebd;
  }
  public void setLongitudinebd(BigDecimal longitudinebd)
  {
    this.longitudinebd = longitudinebd;
  }
  public String getSubcontractor()
  {
    return subcontractor;
  }
  public void setSubcontractor(String subcontractor)
  {
    this.subcontractor = subcontractor;
  }
  public String getArea()
  {
    return area;
  }
  public void setArea(String area)
  {
    this.area = area;
  }
  public Long getIdCampionamento()
  {
    return idCampionamento;
  }
  public void setIdCampionamento(Long idCampionamento)
  {
    this.idCampionamento = idCampionamento;
  }
  public String getCodiceTrappola()
  {
    return codiceTrappola;
  }
  public void setCodiceTrappola(String codiceTrappola)
  {
    this.codiceTrappola = codiceTrappola;
  }
  public String getCodiceUfficiale()
  {
    return codiceUfficiale;
  }
  public void setCodiceUfficiale(String codiceUfficiale)
  {
    this.codiceUfficiale = codiceUfficiale;
  }
  public String getTypologyOfLocation()
  {
    return typologyOfLocation;
  }
  public void setTypologyOfLocation(String typologyOfLocation)
  {
    this.typologyOfLocation = typologyOfLocation;
  }
  public String getFlagEmergenza()
  {
    return flagEmergenza;
  }
  public void setFlagEmergenza(String flagEmergenza)
  {
    this.flagEmergenza = flagEmergenza;
  }
  public Integer getNumIspettori()
  {
    return numIspettori;
  }
  public void setNumIspettori(Integer numIspettori)
  {
    this.numIspettori = numIspettori;
  }
  public Integer getIdMissione()
  {
    return idMissione;
  }
  public void setIdMissione(Integer idMissione)
  {
    this.idMissione = idMissione;
  }
  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }
  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }

}
