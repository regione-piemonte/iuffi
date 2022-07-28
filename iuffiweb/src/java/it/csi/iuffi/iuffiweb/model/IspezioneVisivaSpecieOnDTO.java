package it.csi.iuffi.iuffiweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.api.Coordinate;
import it.csi.iuffi.iuffiweb.model.api.Ubicazione;

public class IspezioneVisivaSpecieOnDTO extends TabelleDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Integer idIspezioneVisiva;
  private Integer idSpecieOn;
  private Integer idSpecieVegetale;
  private String note;
  private String nomeLatino;
  private String sigla;
  private String euro;
  private String periodo;
  //aggiunti
  @JsonIgnore
  private String descSigla;
  //private Integer idOrganismo;
  private String flagTrovato;
  @JsonIgnore
  private String associato;
  @JsonIgnore
  private String presenza;
  
  public IspezioneVisivaSpecieOnDTO()
  {
    super();
    // TODO Auto-generated constructor stub
  }
  
  
  public IspezioneVisivaSpecieOnDTO(Integer idIspezioneVisiva, Integer idSpecieOn,
      String note, String nomelatino, String sigla, String euro, String periodo)
  {
    super();
    this.idIspezioneVisiva = idIspezioneVisiva;
    this.idSpecieOn = idSpecieOn;
    this.note = note;
    this.nomeLatino = nomeLatino;
    this.sigla = sigla;
    this.euro = euro;
    this.periodo = periodo;
  }
  
  public IspezioneVisivaSpecieOnDTO(Integer idIspezioneVisiva, Integer idSpecieOn,
      String presenza)
  {
    super();
    this.idIspezioneVisiva = idIspezioneVisiva;
    this.idSpecieOn = idSpecieOn;
    this.presenza = presenza;

  }
  public String getPresenza()
  {
    return presenza;
  }
  public void setPresenza(String presenza)
  {
    this.presenza = presenza;
  }

  public Integer getIdIspezioneVisiva()
  {
    return idIspezioneVisiva;
  }
  public void setIdIspezioneVisiva(Integer idIspezioneVisiva)
  {
    this.idIspezioneVisiva = idIspezioneVisiva;
  }
  public Integer getIdSpecieOn()
  {
    return idSpecieOn;
  }
  public void setIdSpecieOn(Integer idSpecieOn)
  {
    this.idSpecieOn = idSpecieOn;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public String getNomeLatino()
  {
    return nomeLatino;
  }
  public void setNomeLatino(String nomelatino)
  {
    this.nomeLatino = nomelatino;
  }
  public String getSigla()
  {
    return sigla;
  }
  public void setSigla(String sigla)
  {
    this.sigla = sigla;
  }
  public String getEuro()
  {
    return euro;
  }
  public void setEuro(String euro)
  {
    this.euro = euro;
  }
  public String getPeriodo()
  {
    return periodo;
  }
  public void setPeriodo(String periodo)
  {
    this.periodo = periodo;
  }
  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }
  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }


  public String getFlagTrovato()
  {
    return flagTrovato;
  }


  public void setFlagTrovato(String flagTrovato)
  {
    this.flagTrovato = flagTrovato;
  }
  public String getAssociato()
  {
    return associato;
  }

  public void setAssociato(String associato)
  {
    this.associato = associato;
  }


  public String getDescSigla()
  {
    return descSigla;
  }


  public void setDescSigla(String descSigla)
  {
    this.descSigla = descSigla;
  }


}
