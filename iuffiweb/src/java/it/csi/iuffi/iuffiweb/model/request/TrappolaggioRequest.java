package it.csi.iuffi.iuffiweb.model.request;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.TrappolaDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class TrappolaggioRequest implements ILoggable
{

  private static final long serialVersionUID = -4605722644901668828L;
  
  private Integer idTrappolaggio;
  private Integer idRilevazione;
  private Long idMissione;
  private TrappolaDTO trappola;
  private String istatComune;
  private Date dataOraInizio;
  private Date dataOraFine;
  private Integer idOperazione;
  private Date dataTrappolaggio;
  private String note;
  private Integer idIspezioneVisiva;
  private Integer idAnagrafica;
 //aggiuntivi
  private String descrTrappola;
  private String descrOperazione;
  private String specie;
  private String foto;
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
  private List<Integer> tipoArea;
  @JsonIgnore
  private List<Integer> ispettoreAssegnato;
  @JsonIgnore
  private List<Integer> ispettoriSecondari;
  @JsonIgnore
  private List<Integer> specieVegetale;
  private List<Integer> organismoNocivo;
  @JsonIgnore
  private List<Integer> tipoTrappola;
  @JsonIgnore
  private String descrComune;
  @JsonIgnore
  private String organismi;
  @JsonIgnore
  private String cuaa;
  @JsonIgnore
  private String obiettivoIndagineUfficiale;
  @JsonIgnore
  private String obiettivoEmergenza;  
  @JsonIgnore
  private String codiceSfr;
  @JsonIgnore
  private List<Integer> tipoOperazione;

  public TrappolaggioRequest()
  {
    super();
  }
  
  public TrappolaggioRequest(Integer idRilevazione)
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

  public List<Integer> getTipoTrappola()
  {
    return tipoTrappola;
  }

  public void setTipoTrappola(List<Integer> tipoTrappola)
  {
    this.tipoTrappola = tipoTrappola;
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

  public String getFoto()
  {
    return foto;
  }

  public void setFoto(String foto)
  {
    this.foto = foto;
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
  
  public String getCuaa() {
	return cuaa;
  }

  public void setCuaa(String cuaa) {
	this.cuaa = cuaa;
  }

  public String getObiettivoIndagineUfficiale() {
	return obiettivoIndagineUfficiale;
  }

  public void setObiettivoIndagineUfficiale(String obiettivoIndagineUfficiale) {
	this.obiettivoIndagineUfficiale = obiettivoIndagineUfficiale;
  }

  public String getObiettivoEmergenza() {
	return obiettivoEmergenza;
  }

  public void setObiettivoEmergenza(String obiettivoEmergenza) {
	this.obiettivoEmergenza = obiettivoEmergenza;
  }
  
  public String getCodiceSfr() {
	return codiceSfr;
  }

  public void setCodiceSfr(String codiceSfr) {
	this.codiceSfr = codiceSfr;
  }

  public boolean checkNull() throws IllegalAccessException {
    for (Field f : getClass().getDeclaredFields())
        if (f.get(this) != null && !f.getName().equalsIgnoreCase("serialVersionUID"))
            return false;
    return true;
  }
  
  public List<Integer> getTipoOperazione() {
	return tipoOperazione;
  }

  public void setTipoOperazione(List<Integer> tipoOperazione) {
	this.tipoOperazione = tipoOperazione;
  }

}
