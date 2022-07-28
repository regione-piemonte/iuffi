package it.csi.iuffi.iuffiweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.api.Coordinate;
import it.csi.iuffi.iuffiweb.model.api.Ubicazione;

public class IspezioneVisivaPiantaDTO extends TabelleDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Integer idIspezioneVisivaPianta;
  private Integer idIspezioneVisiva;
  private Integer idSpecieVegetale;

  private Integer numeroPianta;
  private Integer positivita;
  private Integer diametro;
  private String flagTreeClimberIspezione;
  private String flagTreeClimberTaglio;
  private String note1;
  private String note2;
  private String note3;
  @JsonIgnore
  private Double lat;
  @JsonIgnore
  private Double lon;
  //aggiuntivi
  private Coordinate coordinate;
  private Ubicazione ubicazione;
  private Integer quantita;
  private String descPositivita;
  private String specie;
  private String descDiametro;
  @JsonIgnore
  private Double latitudine;
  @JsonIgnore
  private Double longitudine;

  @JsonIgnore
  private String associatoP;
  @JsonIgnore
  private String modificato;
  @JsonIgnore
  private Integer idSpecie;
  @JsonIgnore
  private String nome;
  @JsonIgnore
  private String cognome;
  @JsonIgnore
  private String indirizzo;
  @JsonIgnore
  private String telefono;
  @JsonIgnore
  private String email;
  @JsonIgnore
  private String numero;
  
  
public Integer getIdSpecie()
  {
    return idSpecie;
  }

  public void setIdSpecie(Integer idSpecie)
  {
    this.idSpecie = idSpecie;
  }

public IspezioneVisivaPiantaDTO(Integer idIspezioneVisivaPianta,
      Integer idIspezioneVisiva, Integer idSpecieVegetale, Integer numeroPianta, Integer positivita,
      Integer diametro, String flagTreeClimberIspezione,
      String flagTreeClimberTaglio, String note1, String note2, String note3)
  {
    super();
    this.idIspezioneVisivaPianta = idIspezioneVisivaPianta;
    this.idIspezioneVisiva = idIspezioneVisiva;
    this.idSpecieVegetale = idSpecieVegetale;
    this.numeroPianta = numeroPianta;
//    this.latitudine = latitudine;
//    this.longitudine = longitudine;
//    this.nome = nome;
//    this.cognome = cognome;
//    this.indirizzo = indirizzo;
//    this.telefono = telefono;
//    this.email = email;
    this.positivita = positivita;
    this.diametro = diametro;
    this.flagTreeClimberIspezione = flagTreeClimberIspezione;
    this.flagTreeClimberTaglio = flagTreeClimberTaglio;
    this.note1 = note1;
    this.note2 = note2;
    this.note3 = note3;
  }
  
  public IspezioneVisivaPiantaDTO()
  {
    super();
    // TODO Auto-generated constructor stub
  }

 
  
  public String getAssociatoP()
  {
    return associatoP;
  }

  public void setAssociatoP(String associatoP)
  {
    this.associatoP = associatoP;
  }

  public String getModificato()
  {
    return modificato;
  }

  public void setModificato(String modificato)
  {
    this.modificato = modificato;
  }

  public Integer getIdIspezioneVisivaPianta()
  {
    return idIspezioneVisivaPianta;
  }

  public String getDescPositivita()
  {
    return descPositivita;
  }

  public void setDescPositivita(String descPositivita)
  {
    this.descPositivita = descPositivita;
  }

  public String getSpecie()
  {
    return specie;
  }

  public void setSpecie(String specie)
  {
    this.specie = specie;
  }

  public String getDescDiametro()
  {
    return descDiametro;
  }

  public void setDescDiametro(String descDiametro)
  {
    this.descDiametro = descDiametro;
  }

  public void setIdIspezioneVisivaPianta(Integer idIspezioneVisivaPianta)
  {
    this.idIspezioneVisivaPianta = idIspezioneVisivaPianta;
  }

  public Integer getIdIspezioneVisiva()
  {
    return idIspezioneVisiva;
  }

  public void setIdIspezioneVisiva(Integer idIspezioneVisiva)
  {
    this.idIspezioneVisiva = idIspezioneVisiva;
  }

  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }

  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }

  public Integer getNumeroPianta()
  {
    return numeroPianta;
  }

  public void setNumeroPianta(Integer numeroPianta)
  {
    this.numeroPianta = numeroPianta;
  }

  public Double getLat()
  {
    return lat;
  }

  public void setLat(Double lat)
  {
    this.lat = lat;
  }

  public Double getLon()
  {
    return lon;
  }

  public void setLon(Double lon)
  {
    this.lon = lon;
  }
//
//  public String getNome()
//  {
//    return nome;
//  }
//
//  public void setNome(String nome)
//  {
//    this.nome = nome;
//  }
//
//  public String getCognome()
//  {
//    return cognome;
//  }
//
//  public void setCognome(String cognome)
//  {
//    this.cognome = cognome;
//  }
//
//  public String getIndirizzo()
//  {
//    return indirizzo;
//  }
//
//  public void setIndirizzo(String indirizzo)
//  {
//    this.indirizzo = indirizzo;
//  }
//
//  public String getTelefono()
//  {
//    return telefono;
//  }
//
//  public void setTelefono(String telefono)
//  {
//    this.telefono = telefono;
//  }
//
//  public String getEmail()
//  {
//    return email;
//  }
//
//  public void setEmail(String email)
//  {
//    this.email = email;
//  }

  public Integer getPositivita()
  {
    return positivita;
  }

  public void setPositivita(Integer positivita)
  {
    this.positivita = positivita;
  }

  public Integer getDiametro()
  {
    return diametro;
  }

  public void setDiametro(Integer diametro)
  {
    this.diametro = diametro;
  }

  public String getFlagTreeClimberIspezione()
  {
    return flagTreeClimberIspezione;
  }

  public void setFlagTreeClimberIspezione(String flagTreeClimberIspezione)
  {
    this.flagTreeClimberIspezione = flagTreeClimberIspezione;
  }

  public String getFlagTreeClimberTaglio()
  {
    return flagTreeClimberTaglio;
  }

  public void setFlagTreeClimberTaglio(String flagTreeClimberTaglio)
  {
    this.flagTreeClimberTaglio = flagTreeClimberTaglio;
  }

  public String getNote1()
  {
    return note1;
  }

  public void setNote1(String note1)
  {
    this.note1 = note1;
  }

  public String getNote2()
  {
    return note2;
  }

  public void setNote2(String note2)
  {
    this.note2 = note2;
  }

  public String getNote3()
  {
    return note3;
  }

  public void setNote3(String note3)
  {
    this.note3 = note3;
  }
  
  public Coordinate getCoordinate()
  {
    return coordinate;
  }

  public void setCoordinate(Coordinate coordinate)
  {
    this.coordinate = coordinate;
  }

  public Ubicazione getUbicazione()
  {
    return ubicazione;
  }

  public void setUbicazione(Ubicazione ubicazione)
  {
    this.ubicazione = ubicazione;
  }

  public Integer getQuantita()
  {
    return quantita;
  }

  public void setQuantita(Integer quantita)
  {
    this.quantita = quantita;
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

  public String getNome()
  {
    return nome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String getCognome()
  {
    return cognome;
  }

  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }

  public String getIndirizzo()
  {
    return indirizzo;
  }

  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }

  public String getTelefono()
  {
    return telefono;
  }

  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getNumero()
  {
    return numero;
  }

  public void setNumero(String numero)
  {
    this.numero = numero;
  }




}
