package it.csi.iuffi.iuffiweb.model;

import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.api.Coordinate;

public class ShapeDTO extends TabelleStoricizzateDTO implements ILoggable
{

  private static final long serialVersionUID = 832759932377954666L;

  private Integer idShapeMaster;
  private String descrizione;
  private Date dataAttivita;
  private Long idTipoAttivita;
  private List<Coordinate> details;
  private Integer idOrganismoNocivo;
  private String comune;
  private String campionamento;
  private String trappola;
  private String codTipoTrappola;
  private String descTipoTrappola;
  private String nVisual;
  private String note;
  private Long quadrante;
  
  public ShapeDTO()
  {
    super();
  } 

  public Integer getIdOrganismoNocivo()
  {
    return idOrganismoNocivo;
  }

  public void setIdOrganismoNocivo(Integer idOrganismoNocivo)
  {
    this.idOrganismoNocivo = idOrganismoNocivo;
  }

  public Integer getIdShapeMaster()
  {
    return idShapeMaster;
  }

  public void setIdShapeMaster(Integer idShapeMaster)
  {
    this.idShapeMaster = idShapeMaster;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public Date getDataAttivita()
  {
    return dataAttivita;
  }

  public void setDataAttivita(Date dataAttivita)
  {
    this.dataAttivita = dataAttivita;
  }

  public Long getIdTipoAttivita()
  {
    return idTipoAttivita;
  }

  public void setIdTipoAttivita(Long idTipoAttivita)
  {
    this.idTipoAttivita = idTipoAttivita;
  }

  public List<Coordinate> getDetails()
  {
    return details;
  }

  public void setDetails(List<Coordinate> details)
  {
    this.details = details;
  }

  public String getComune()
  {
    return comune;
  }

  public void setComune(String comune)
  {
    this.comune = comune;
  }

  public String getCampionamento()
  {
    return campionamento;
  }

  public void setCampionamento(String campionamento)
  {
    this.campionamento = campionamento;
  }

  public String getTrappola()
  {
    return trappola;
  }

  public void setTrappola(String trappola)
  {
    this.trappola = trappola;
  }

  public String getCodTipoTrappola()
  {
    return codTipoTrappola;
  }

  public void setCodTipoTrappola(String codTipoTrappola)
  {
    this.codTipoTrappola = codTipoTrappola;
  }

  public String getDescTipoTrappola()
  {
    return descTipoTrappola;
  }

  public void setDescTipoTrappola(String descTipoTrappola)
  {
    this.descTipoTrappola = descTipoTrappola;
  }

  public String getnVisual()
  {
    return nVisual;
  }

  public void setnVisual(String nVisual)
  {
    this.nVisual = nVisual;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public Long getQuadrante()
  {
    return quadrante;
  }

  public void setQuadrante(Long quadrante)
  {
    this.quadrante = quadrante;
  }

}
