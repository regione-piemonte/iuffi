package it.csi.iuffi.iuffiweb.model;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RendicontazioneRequestDTO /*extends TabelleStoricizzateDTO*/ implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4621339630061084884L;
  
  private Integer idMissione;
  private Date dataOraInizioMissione;
  private Date dataOraFineMissione;
  private String numeroTrasferta;
  private Integer idRilevazione;
  private Integer idAnagrafica;
  private Integer idTipoArea;
  private String visual;
  private String campionamento;
  private String trappolaggio;
  private Integer specie;
  private Integer rif;
  private String tipo;
  private String ispettore;
  private String numVerbale;
  private Integer idIspezioneVisiva;
  private Double pagaOraria;
  private String nomeVolgare;
  private String organismi;
  private String organismiNoEuro;
  private Integer countOrganismi;
  private String tipologiaCampione;
  private Integer numCampioni;
  private Integer id_tipo_trappola;
  private String latitudine;
  private String longitudine;
  private Integer id_operazione;
  private String tipologiaTrappola;
  private String codiceSfr;
  private Integer numTrappPiazz;
  private Integer numTrappSostRic;
  private Integer numTrappRimosse;
  private String descTipoArea;
  private Integer velocita; 
  private Double numOrePiazzTrappole;
  private Double numOreRicSostTrappole;
  private Double numOreRimozioneTrappole;
  
  private Double costoTotalePiazzTrappole;
  private Double costoTotaleRicSostTrappole;
  private Double costoTotaleRitiroTrappole;
  
  private Integer numeroPiante;
  private Double superfice;
  
  //test laboratorio
  private String testLab;
  private String numRegistroLab;
  private Double costoLab;
  private String codCampione;
  private String organismiCampione;
  private String organismoTrappola;
  private String subcontractor;
  
  private Integer numIspettoriAggiunti;
  
  private Long idCampionamento;
  private String codiceTrappola;
  
  private String codiceUfficiale;
  private String typologyOfLocation;
  
  private String flagEmergenza;
  private String ispettoriAggiunti;

  public Integer getIdMissione()
  {
    return idMissione;
  }
  public void setIdMissione(Integer idMissione)
  {
    this.idMissione = idMissione;
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
  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }
  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }
  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }
  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
  }
  public Integer getIdTipoArea()
  {
    return idTipoArea;
  }
  public void setIdTipoArea(Integer idTipoArea)
  {
    this.idTipoArea = idTipoArea;
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
  public Integer getSpecie()
  {
    return specie;
  }
  public void setSpecie(Integer specie)
  {
    this.specie = specie;
  }
  public Integer getRif()
  {
    return rif;
  }
  public void setRif(Integer rif)
  {
    this.rif = rif;
  }
  public String getTipo()
  {
    return tipo;
  }
  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }

  public String getNumVerbale()
  {
    return numVerbale;
  }
  public void setNumVerbale(String numVerbale)
  {
    this.numVerbale = numVerbale;
  }
  public String getIspettore()
  {
    return ispettore;
  }
  public void setIspettore(String ispettore)
  {
    this.ispettore = ispettore;
  }
  public Integer getIdIspezioneVisiva()
  {
    return idIspezioneVisiva;
  }
  public void setIdIspezioneVisiva(Integer idIspezioneVisiva)
  {
    this.idIspezioneVisiva = idIspezioneVisiva;
  }
  public Double getPagaOraria()
  {
    return pagaOraria;
  }
  public void setPagaOraria(Double pagaOraria)
  {
    this.pagaOraria = pagaOraria;
  }
  public String getNomeVolgare()
  {
    return nomeVolgare;
  }
  public void setNomeVolgare(String nomeVolgare)
  {
    this.nomeVolgare = nomeVolgare;
  }
  public String getOrganismi()
  {
    return organismi;
  }
  public void setOrganismi(String organismi)
  {
    this.organismi = organismi;
  }
  public String getOrganismiNoEuro()
  {
    return organismiNoEuro;
  }
  public void setOrganismiNoEuro(String organismiNoEuro)
  {
    this.organismiNoEuro = organismiNoEuro;
  }
  public Integer getCountOrganismi()
  {
    return countOrganismi;
  }
  public void setCountOrganismi(Integer countOrganismi)
  {
    this.countOrganismi = countOrganismi;
  }
 
  public Integer getNumCampioni()
  {
    return numCampioni;
  }
  public void setNumCampioni(Integer numCampioni)
  {
    this.numCampioni = numCampioni;
  }
  public String getTipologiaCampione()
  {
    return tipologiaCampione;
  }
  public void setTipologiaCampione(String tipologiaCampione)
  {
    this.tipologiaCampione = tipologiaCampione;
  }
  public Integer getId_tipo_trappola()
  {
    return id_tipo_trappola;
  }
  public void setId_tipo_trappola(Integer id_tipo_trappola)
  {
    this.id_tipo_trappola = id_tipo_trappola;
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
  public Integer getId_operazione()
  {
    return id_operazione;
  }
  public void setId_operazione(Integer id_operazione)
  {
    this.id_operazione = id_operazione;
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
  public Double getNumOreRimozioneTrappole()
  {
    return numOreRimozioneTrappole;
  }
  public void setNumOreRimozioneTrappole(Double numOreRimozioneTrappole)
  {
    this.numOreRimozioneTrappole = numOreRimozioneTrappole;
  }
  public Double getCostoTotalePiazzTrappole()
  {
    return costoTotalePiazzTrappole;
  }
  public void setCostoTotalePiazzTrappole(Double costoTotalePiazzTrappole)
  {
    this.costoTotalePiazzTrappole = costoTotalePiazzTrappole;
  }
  public Double getCostoTotaleRicSostTrappole()
  {
    return costoTotaleRicSostTrappole;
  }
  public void setCostoTotaleRicSostTrappole(Double costoTotaleRicSostTrappole)
  {
    this.costoTotaleRicSostTrappole = costoTotaleRicSostTrappole;
  }
  public Double getCostoTotaleRitiroTrappole()
  {
    return costoTotaleRitiroTrappole;
  }
  public void setCostoTotaleRitiroTrappole(Double costoTotaleRitiroTrappole)
  {
    this.costoTotaleRitiroTrappole = costoTotaleRitiroTrappole;
  }
  public Integer getNumTrappPiazz()
  {
    return numTrappPiazz;
  }
  public void setNumTrappPiazz(Integer numTrappPiazz)
  {
    this.numTrappPiazz = numTrappPiazz;
  }
  public Integer getNumTrappSostRic()
  {
    return numTrappSostRic;
  }
  public void setNumTrappSostRic(Integer numTrappSostRic)
  {
    this.numTrappSostRic = numTrappSostRic;
  }
  public Integer getNumTrappRimosse()
  {
    return numTrappRimosse;
  }
  public void setNumTrappRimosse(Integer numTrappRimosse)
  {
    this.numTrappRimosse = numTrappRimosse;
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
  public Double getCostoLab()
  {
    return costoLab;
  }
  public void setCostoLab(Double costoLab)
  {
    this.costoLab = costoLab;
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
  public String getOrganismoTrappola()
  {
    return organismoTrappola;
  }
  public void setOrganismoTrappola(String organismoTrappola)
  {
    this.organismoTrappola = organismoTrappola;
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
  public String getSubcontractor()
  {
    return subcontractor;
  }
  public void setSubcontractor(String subcontractor)
  {
    this.subcontractor = subcontractor;
  }
  public Integer getNumIspettoriAggiunti()
  {
    return numIspettoriAggiunti;
  }
  public void setNumIspettoriAggiunti(Integer numIspettoriAggiunti)
  {
    this.numIspettoriAggiunti = numIspettoriAggiunti;
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
  public String getIspettoriAggiunti()
  {
    return ispettoriAggiunti;
  }
  public void setIspettoriAggiunti(String ispettoriAggiunti)
  {
    this.ispettoriAggiunti = ispettoriAggiunti;
  }

}
