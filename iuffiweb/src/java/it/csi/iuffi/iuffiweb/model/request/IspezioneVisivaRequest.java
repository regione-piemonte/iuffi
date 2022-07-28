package it.csi.iuffi.iuffiweb.model.request;

import java.lang.reflect.Field;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class IspezioneVisivaRequest implements ILoggable
{

  private static final long serialVersionUID = 1L;
  
  private Integer idIspezione;
  private Integer idRilevazione;
  private String dallaData;
  private String allaData;
  private Long idIspettore;
  private Integer idSpecieVegetale;
  private Integer idOrganismoNocivo;
  private String stato;
  private String cognomeIspettore;
  private String nomeIspettore;
  private String cfIspettore;
  private Long anno;
  private String istatComune;
  private String obiettivoNessuno;
  private String obiettivoIndagineUfficiale;
  private String obiettivoEmergenza;
  private List<Integer> tipoArea;
  private List<Integer> ispettoreAssegnato;
  private List<Integer> ispettoriSecondari;
  private List<Integer> specieVegetale;
  private List<Integer> organismoNocivo;
  private String cuaa;
  private Integer idRecord;
  private Long idMissione;
  private String descComune;
  

  public IspezioneVisivaRequest() {
	    super();
  }

  public IspezioneVisivaRequest(Integer idIspezione)
  {
    this.idIspezione = idIspezione;
  }

  public IspezioneVisivaRequest(Integer idIspezione, Integer idRilevazione,
      String dallaData, String allaData, Long idIspettore,
      Integer idSpecieVegetale,
      Integer idOrganismoNocivo, String stato, String cognomeIspettore,
      String nomeIspettore, String cfIspettore)
  {
    super();
    this.idIspezione = idIspezione;
    this.idRilevazione = idRilevazione;
    this.dallaData = dallaData;
    this.allaData = allaData;
    this.idIspettore = idIspettore;
    this.idSpecieVegetale = idSpecieVegetale;
    this.idOrganismoNocivo = idOrganismoNocivo;
    this.stato = stato;
    this.cognomeIspettore = cognomeIspettore;
    this.nomeIspettore = nomeIspettore;
    this.cfIspettore = cfIspettore;
  }

  public Integer getIdIspezione()
  {
    return idIspezione;
  }

  public void setIdIspezione(Integer idIspezione)
  {
    this.idIspezione = idIspezione;
  }
  
  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }

  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
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
  
  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public Integer getIdRecord()
  {
    return idRecord;
  }

  public void setIdRecord(Integer idRecord)
  {
    this.idRecord = idRecord;
  }

  public Long getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Long idMissione)
  {
    this.idMissione = idMissione;
  }
  
  public String getDescComune() {
	return descComune;
  }

  public void setDescComune(String descComune) {
		this.descComune = descComune;
  }


  public boolean checkNull() throws IllegalAccessException {
    for (Field f : getClass().getDeclaredFields())
        if (f.get(this) != null && !f.getName().equalsIgnoreCase("serialVersionUID"))
            return false;
    return true;
  }

}
