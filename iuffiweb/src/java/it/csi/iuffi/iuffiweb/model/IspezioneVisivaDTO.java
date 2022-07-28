package it.csi.iuffi.iuffiweb.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.api.OrganismiNocivi;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class IspezioneVisivaDTO extends TabelleDTO implements ILoggable
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -4821288731896425579L;
  private Integer idIspezione;
  private Integer idRilevazione;
  private Integer numeroAviv;
  private Integer idSpecieVegetale;
  private Integer superficie; 
  private Integer numeroPiante;
  private String  flagPresenzaOn;
  private String istatComune;
  private String comune;
  private Double latitudine;
  private Double longitudine;

  //aggiuntivi
  private String note;
  private String flagIndicatoreIntervento;
  private String riferimentoUbicazione;
  private String area;
  private String dettaglioArea; 
  private String pianta;
  private String ispettore; 

  private Integer idAnagrafica;
  private Integer idTipoArea;
  private String specie;
  private String nomeVolgareSpecie;
  //private Integer[] organismi;
  private OrganismiNocivi[] organismi;
  private Integer idMissione;
  private Integer idSpecieOn;
  //@NotNull
  private Date dataOraInizio;  
  private Date dataOraFine;
  private String onIspezionati;
  private String cuaa;
  private Integer idRecord;
  private IspezioneVisivaPiantaDTO[] ispezioni;
  private Boolean foto;
  private String flagTrovato;
  
  //Stefano
  private String dataIspezione;
  private String oraInizio;
  private String oraFine;
  
  @JsonIgnore
  private String campionamento;
  @JsonIgnore
  private String trappolaggio;

  // Sandro
  @JsonIgnore
  private Integer idStatoVerbale;

  
  public IspezioneVisivaDTO(Integer idIspezione, Integer idRilevazione,
      Integer numeroAviv, Integer idSpecieVegetale, Integer superficie,
      Integer numeroPiante, String flagPresenzaOn, String istatComune,
      Double latitudine, Double longitudine, String note, String flagIndicatoreIntervento,
      String riferimentoUbicazione, Integer idAnagrafica, IspezioneVisivaPiantaDTO[] ispezioni,
      Date dataOraInizio, Date dataOraFine)
  {
    super();
    this.idIspezione = idIspezione;
    this.idRilevazione = idRilevazione;
    this.numeroAviv = numeroAviv;
    this.idSpecieVegetale = idSpecieVegetale;
    this.superficie = superficie;
    this.numeroPiante = numeroPiante;
    this.flagPresenzaOn = flagPresenzaOn;
    this.istatComune = istatComune;
    this.latitudine = latitudine;
    this.longitudine = longitudine;
    this.note = note;
    this.flagIndicatoreIntervento = flagIndicatoreIntervento;
    this.riferimentoUbicazione = riferimentoUbicazione;
    this.idAnagrafica = idAnagrafica;
    this.ispezioni = ispezioni;
    this.dataOraInizio = dataOraInizio;
    this.dataOraFine = dataOraFine;
  }
  public IspezioneVisivaDTO()
  {
    super();
    // TODO Auto-generated constructor stub
  }

  public IspezioneVisivaDTO(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
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
  public Integer getNumeroAviv()
  {
    return numeroAviv;
  }
  public void setNumeroAviv(Integer numeroAviv)
  {
    this.numeroAviv = numeroAviv;
  }
  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }
  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
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
  public String getFlagPresenzaOn()
  {
    return flagPresenzaOn;
  }
  public void setFlagPresenzaOn(String flagPresenzaOn)
  {
    this.flagPresenzaOn = flagPresenzaOn;
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
//  public String getIspettore()
//  {
//    return ispettore;
//  }
//  public void setIspettore(String ispettore)
//  {
//    this.ispettore = ispettore;
//  }
//  public String getArea()
//  {
//    return area;
//  }
//  public void setArea(String area)
//  {
//    this.area = area;
//  }
//  public String getSpecie()
//  {
//    return specie;
//  }
//  public void setSpecie(String specie)
//  {
//    this.specie = specie;
//  }
//  public String getOrganismi()
//  {
//    return organismi;
//  }
//  public void setOrganismi(String organismi)
//  {
//    this.organismi = organismi;
//  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
//  public Integer getIdMissione()
//  {
//    return idMissione;
//  }
//  public void setIdMissione(Integer idMissione)
//  {
//    this.idMissione = idMissione;
//  }

  public String getFlagIndicatoreIntervento()
  {
    return flagIndicatoreIntervento;
  }
  public void setFlagIndicatoreIntervento(String flagIndicatoreIntervento)
  {
    this.flagIndicatoreIntervento = flagIndicatoreIntervento;
  }
  public String getRiferimentoUbicazione()
  {
    return riferimentoUbicazione;
  }
  public void setRiferimentoUbicazione(String riferimentoUbicazione)
  {
    this.riferimentoUbicazione = riferimentoUbicazione;
  }
  public String getArea()
  {
    return area;
  }
  public void setArea(String area)
  {
    this.area = area;
  }
  
//  public String getIspettore()
//  {
//    return ispettore;
//  }
//  public void setIspettore(String ispettore)
//  {
//    this.ispettore = ispettore;
//  }
  public String getSpecie()
  {
    return specie;
  }
  public void setSpecie(String specie)
  {
    this.specie = specie;
  }

  public Integer getIdMissione()
  {
    return idMissione;
  }
  public void setIdMissione(Integer idMissione)
  {
    this.idMissione = idMissione;
  }
  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }
  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
  }
  
  public IspezioneVisivaPiantaDTO[] getIspezioni()
  {
    return ispezioni;
  }
  
  public void setIspezioni(IspezioneVisivaPiantaDTO[] ispezioni)
  {
    this.ispezioni = ispezioni;
  }
  public String getIspettore()
  {
    return ispettore;
  }
  public void setIspettore(String ispettore)
  {
    this.ispettore = ispettore;
  }
  
 /* public Integer[] getOrganismi()
  {
    return organismi;
  }
  public void setOrganismi(Integer[] organismi)
  {
    this.organismi = organismi;
  }
  */
  public Integer getIdSpecieOn()
  {
    return idSpecieOn;
  }
  public void setIdSpecieOn(Integer idSpecieOn)
  {
    this.idSpecieOn = idSpecieOn;
  }
  public String getComune()
  {
    return comune;
  }
  public void setComune(String comune)
  {
    this.comune = comune;
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
  public String getDataInizio()
  {
    return IuffiUtils.DATE.formatDate(dataOraInizio);
  }
  public String getOnIspezionati()
  {
    return onIspezionati;
  }
  public void setOnIspezionati(String onIspezionati)
  {
    this.onIspezionati = onIspezionati;
  }
  public String getCuaa()
  {
    return cuaa;
  }
  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
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
  public Integer getIdRecord()
  {
    return idRecord;
  }
  public void setIdRecord(Integer idRecord)
  {
    this.idRecord = idRecord;
  }
  public Boolean getFoto()
  {
    return foto;
  }
  public void setFoto(Boolean foto)
  {
    this.foto = foto;
  }
  public Integer getIdTipoArea()
  {
    return idTipoArea;
  }
  public void setIdTipoArea(Integer idTipoArea)
  {
    this.idTipoArea = idTipoArea;
  }
  public OrganismiNocivi[] getOrganismi()
  {
    return organismi;
  }
  public void setOrganismi(OrganismiNocivi[] organismi)
  {
    this.organismi = organismi;
  }
  public String getNomeVolgareSpecie()
  {
    return nomeVolgareSpecie;
  }
  public void setNomeVolgareSpecie(String nomeVolgareSpecie)
  {
    this.nomeVolgareSpecie = nomeVolgareSpecie;
  }
  public String getFlagTrovato()
  {
    return flagTrovato;
  }
  public void setFlagTrovato(String flagTrovato)
  {
    this.flagTrovato = flagTrovato;
  }
  public String getDataIspezione()
  {
    return dataIspezione;
  }
  public void setDataIspezione(String dataIspezione)
  {
    this.dataIspezione = dataIspezione;
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
  public String getOraInizioS()
  {
    return (dataOraInizio!=null)?(idIspezione!=null || !(new SimpleDateFormat("HH:mm").format(dataOraInizio).equals("00:00"))) ? new SimpleDateFormat("HH:mm").format(dataOraInizio):null:null;
  }
  public String getOraFineS()
  {
    return (dataOraFine!=null)?new SimpleDateFormat("HH:mm").format(dataOraFine):null;
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
  public Integer getIdStatoVerbale()
  {
    return idStatoVerbale;
  }
  public void setIdStatoVerbale(Integer idStatoVerbale)
  {
    this.idStatoVerbale = idStatoVerbale;
  }

}
