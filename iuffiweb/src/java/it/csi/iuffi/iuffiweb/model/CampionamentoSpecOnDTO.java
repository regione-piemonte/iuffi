package it.csi.iuffi.iuffiweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class CampionamentoSpecOnDTO extends TabelleDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -5822900977436974751L;
  
  private Integer idCampionamento;
  private Integer idSpecieOn;
  private String presenza;
  private String euro;
  private String emergenza;
  //aggiunti
  private String descSigla;
  private String nomeLatino;
  @JsonIgnore
  private String associato;

 
  public CampionamentoSpecOnDTO()
  {
    super();
  }
  
  
  public CampionamentoSpecOnDTO(Integer idCampionamento, Integer idSpecieOn,
      String presenza)
  {
    super();
    this.idCampionamento = idCampionamento;
    this.idSpecieOn = idSpecieOn;
    this.presenza = presenza;

  }
  public long getIdCampionamento()
  {
    return idCampionamento;
  }
  public void setIdCampionamento(Integer idCampionamento)
  {
    this.idCampionamento = idCampionamento;
  }
  public long getIdSpecieOn()
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
  public String getDescSigla()
  {
    return descSigla;
  }
  public void setDescSigla(String descSigla)
  {
    this.descSigla = descSigla;
  }
  public String getNomeLatino()
  {
    return nomeLatino;
  }
  public void setNomeLatino(String nomeLatino)
  {
    this.nomeLatino = nomeLatino;
  }
  public String getEuro()
  {
    return euro;
  }
  public void setEuro(String euro)
  {
    this.euro = euro;
  }
  public String getEmergenza()
  {
    return emergenza;
  }
  public void setEmergenza(String emergenza)
  {
    this.emergenza = emergenza;
  }
  public String getAssociato()
  {
    return associato;
  }

  public void setAssociato(String associato)
  {
    this.associato = associato;
  }

  
}
