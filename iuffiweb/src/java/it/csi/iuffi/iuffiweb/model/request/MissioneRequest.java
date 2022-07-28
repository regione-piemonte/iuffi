package it.csi.iuffi.iuffiweb.model.request;


import java.lang.reflect.Field;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;


public class MissioneRequest implements ILoggable
{

	private static final long serialVersionUID = 1L;
	
	private Long idMissione;
  private Long numeroTrasferta;
  private String dallaData;
  private String allaData;
  private Long idIspettore;
  private Boolean visual;
  private Boolean campionamento;
  private Boolean trappolaggio;
  private Integer idSpecieVegetale;
  private Integer idOrganismoNocivo;
  private String stato;
  private String cognomeIspettore;
  private String nomeIspettore;
  private String cfIspettore;
  private Long anno;
  private String istatComune;
  private String descComune;
  private String obiettivoNessuno;
  private String obiettivoIndagineUfficiale;
  private String obiettivoEmergenza;
  private List<Integer> tipoArea;
  private List<Integer> ispettoreAssegnato;
  private List<Integer> ispettoriSecondari;
  private List<Integer> specieVegetale;
  private List<Integer> organismoNocivo;
  @JsonIgnore
  private String verbaleAssente;
  @JsonIgnore
  private String verbaleBozza;
  @JsonIgnore
  private String verbaleConsolidato;
  @JsonIgnore
  private String verbaleArchiviato;
  @JsonIgnore
  private String numVerbale;
  
  
  public MissioneRequest() {
    super();
  }

  public MissioneRequest(Long idMissione)
  {
    this.idMissione = idMissione;
  }

  public MissioneRequest(Long idMissione, Long numeroTrasferta,
      String dallaData, String allaData, Long idIspettore,
      Boolean campionamento, Boolean trappolaggio, Integer idSpecieVegetale,
      Integer idOrganismoNocivo, String stato, String cognomeIspettore,
      String nomeIspettore, String cfIspettore)
  {
    super();
    this.idMissione = idMissione;
    this.numeroTrasferta = numeroTrasferta;
    this.dallaData = dallaData;
    this.allaData = allaData;
    this.idIspettore = idIspettore;
    this.campionamento = campionamento;
    this.trappolaggio = trappolaggio;
    this.idSpecieVegetale = idSpecieVegetale;
    this.idOrganismoNocivo = idOrganismoNocivo;
    this.stato = stato;
    this.cognomeIspettore = cognomeIspettore;
    this.nomeIspettore = nomeIspettore;
    this.cfIspettore = cfIspettore;
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

  public String getDallaData()
  {
    return dallaData;
  }

  public void setDallaData(String dallaData)
  {
    this.dallaData = dallaData;
  }

  public String getAllaData()
  {
    return allaData;
  }

  public void setAllaData(String allaData)
  {
    this.allaData = allaData;
  }

  public Long getIdIspettore()
  {
    return idIspettore;
  }

  public void setIdIspettore(Long idIspettore)
  {
    this.idIspettore = idIspettore;
  }

  public Boolean getVisual()
  {
    return visual;
  }

  public void setVisual(Boolean visual)
  {
    this.visual = visual;
  }

  public Boolean getCampionamento()
  {
    return campionamento;
  }

  public void setCampionamento(Boolean campionamento)
  {
    this.campionamento = campionamento;
  }

  public Boolean getTrappolaggio()
  {
    return trappolaggio;
  }

  public void setTrappolaggio(Boolean trappolaggio)
  {
    this.trappolaggio = trappolaggio;
  }

  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }

  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  public String getStato()
  {
    return stato;
  }

  public void setStato(String stato)
  {
    this.stato = stato;
  }

  public String getCognomeIspettore()
  {
    return cognomeIspettore;
  }

  public void setCognomeIspettore(String cognomeIspettore)
  {
    this.cognomeIspettore = cognomeIspettore;
  }

  public String getNomeIspettore()
  {
    return nomeIspettore;
  }

  public void setNomeIspettore(String nomeIspettore)
  {
    this.nomeIspettore = nomeIspettore;
  }

  public String getCfIspettore()
  {
    return cfIspettore;
  }

  public void setCfIspettore(String cfIspettore)
  {
    this.cfIspettore = cfIspettore;
  }

  public Long getAnno()
  {
    return anno;
  }

  public void setAnno(Long anno)
  {
    this.anno = anno;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getObiettivoNessuno()
  {
    return obiettivoNessuno;
  }

  public void setObiettivoNessuno(String obiettivoNessuno)
  {
    this.obiettivoNessuno = obiettivoNessuno;
  }

  public String getObiettivoIndagineUfficiale()
  {
    return obiettivoIndagineUfficiale;
  }

  public void setObiettivoIndagineUfficiale(String obiettivoIndagineUfficiale)
  {
    this.obiettivoIndagineUfficiale = obiettivoIndagineUfficiale;
  }

  public String getObiettivoEmergenza()
  {
    return obiettivoEmergenza;
  }

  public void setObiettivoEmergenza(String obiettivoEmergenza)
  {
    this.obiettivoEmergenza = obiettivoEmergenza;
  }

  public String getDescComune()
  {
    return descComune;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
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
  
  public String getVerbaleAssente() {
	return verbaleAssente;
  }

  public void setVerbaleAssente(String verbaleAssente) {
	this.verbaleAssente = verbaleAssente;
  }

  public String getVerbaleBozza() {
	return verbaleBozza;
  }

  public void setVerbaleBozza(String verbaleBozza) {
	this.verbaleBozza = verbaleBozza;
  }

  public String getVerbaleConsolidato() {
	return verbaleConsolidato;
  }

  public void setVerbaleConsolidato(String verbaleConsolidato) {
	this.verbaleConsolidato = verbaleConsolidato;
  }

  public String getVerbaleArchiviato() {
	return verbaleArchiviato;
  }

  public void setVerbaleArchiviato(String verbaleArchiviato) {
	this.verbaleArchiviato = verbaleArchiviato;
  }

  public String getNumVerbale()
  {
    return numVerbale;
  }

  public void setNumVerbale(String numVerbale)
  {
    this.numVerbale = numVerbale;
  }

  public boolean checkNull() throws IllegalAccessException {
    for (Field f : getClass().getDeclaredFields())
        if (f.get(this) != null && !f.getName().equalsIgnoreCase("serialVersionUID"))
            return false;
    return true;
  }

}
