package it.csi.iuffi.iuffiweb.model;


import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;


public class GpsDTO extends TabelleDTO implements ILoggable
{

	private static final long serialVersionUID = 1L;
	
	private Integer idRecord;
	private Integer idRilevazione;
  private Integer idTipologia;
  private Double longitudine;
  private Double latitudine;
  private Integer idSpecieVegetale;
  private String dettaglio;
  private String genereSpecie;
  //campi ricerca
  private Integer anno;
  private Date dallaData;
  private Date allaData;
  private Boolean visual;
  private Boolean trappolaggio;
  private Boolean campionamento;
  private String ispettoreEvento;
  private String ispettoreAssegnatoM;
  private Integer numeroTrasferta;
  private String tipologia;
  private List<Integer> tipoArea;
  private List<Integer> ispettoreAssegnato;
  private List<Integer> ispettoriSecondari;
  private List<Integer> specieVegetale;
  private List<Integer> organismoNocivo;
  private List<Integer> trappole;
  private List<Integer> campioni;

  private String specie;
  private String area;
  private String trappola;
  private String campione;
  private Date dataAcquisizione;
  private String descrComune;
  private String organismi;    

  private String istatComune;
  private String descComune;
  private String comune;
  
  
  public String getDataAcquisizioneF()
  {
    return IuffiUtils.DATE.formatDate(dataAcquisizione);
  }
  
  public void setDataAcquisizioneF(Date dataAcquisizione)
  {
    this.dataAcquisizione = dataAcquisizione;
  }
   
  public String getIspettoreEvento()
  {
    return ispettoreEvento;
  }

  public String getDescComune()
  {
    return descComune;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }

  public String getComune()
  {
    return comune;
  }

  public void setComune(String comune)
  {
    this.comune = comune;
  }

  public void setIspettoreEvento(String ispettoreEvento)
  {
    this.ispettoreEvento = ispettoreEvento;
  }

  public String getIspettoreAssegnatoM()
  {
    return ispettoreAssegnatoM;
  }

  public void setIspettoreAssegnatoM(String ispettoreAssegnatoM)
  {
    this.ispettoreAssegnatoM = ispettoreAssegnatoM;
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

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
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

  public String getTrappola()
  {
    return trappola;
  }

  public void setTrappola(String trappola)
  {
    this.trappola = trappola;
  }

  public String getCampione()
  {
    return campione;
  }

  public void setCampione(String campione)
  {
    this.campione = campione;
  }

  
  public GpsDTO() {
    super();
  }

  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }

  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }

  public Integer getIdTipologia()
  {
    return idTipologia;
  }

  public void setIdTipologia(Integer idTipologia)
  {
    this.idTipologia = idTipologia;
  }

  public Double getLongitudine()
  {
    return longitudine;
  }

  public void setLongitudine(Double longitudine)
  {
    this.longitudine = longitudine;
  }

  public Double getLatitudine()
  {
    return latitudine;
  }

  public void setLatitudine(Double latitudine)
  {
    this.latitudine = latitudine;
  }

  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }

  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }

  public String getDettaglio()
  {
    return dettaglio;
  }

  public void setDettaglio(String dettaglio)
  {
    this.dettaglio = dettaglio;
  }

  public String getGenereSpecie()
  {
    return genereSpecie;
  }

  public void setGenereSpecie(String genereSpecie)
  {
    this.genereSpecie = genereSpecie;
  }

  public String getTipologia()
  {
    return tipologia;
  }

  public void setTipologia(String tipologia)
  {
    this.tipologia = tipologia;
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

  public Boolean getVisual()
  {
    return visual;
  }

  public void setVisual(Boolean visual)
  {
    this.visual = visual;
  }

  public Boolean getTrappolaggio()
  {
    return trappolaggio;
  }

  public void setTrappolaggio(Boolean trappolaggio)
  {
    this.trappolaggio = trappolaggio;
  }

  public Boolean getCampionamento()
  {
    return campionamento;
  }

  public void setCampionamento(Boolean campionamento)
  {
    this.campionamento = campionamento;
  }

  public Integer getNumeroTrasferta()
  {
    return numeroTrasferta;
  }

  public void setNumeroTrasferta(Integer numeroTrasferta)
  {
    this.numeroTrasferta = numeroTrasferta;
  }

  public String getDallaDataS()
  {
    return IuffiUtils.DATE.formatDate(dallaData);
  }

  public String getAllaDataS()
  {
    return IuffiUtils.DATE.formatDate(allaData);
  }

  public Date getDataAcquisizione()
  {
    return dataAcquisizione;
  }

  public void setDataAcquisizione(Date dataAcquisizione)
  {
    this.dataAcquisizione = dataAcquisizione;
  }

  public Integer getIdRecord()
  {
    return idRecord;
  }

  public void setIdRecord(Integer idRecord)
  {
    this.idRecord = idRecord;
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

  public boolean checkNull() throws IllegalAccessException {
    for (Field f : getClass().getDeclaredFields())
        if (f.get(this) != null && !f.getName().equalsIgnoreCase("serialVersionUID"))
            return false;
    return true;
  }

}
