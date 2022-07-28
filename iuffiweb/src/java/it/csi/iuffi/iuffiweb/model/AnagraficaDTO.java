package it.csi.iuffi.iuffiweb.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AnagraficaDTO extends TabelleStoricizzateDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4621339630061084884L;
  
  private Integer idAnagrafica;
  
  @NotNull
  @NotBlank
  private String nome;

  @NotNull
  @NotBlank
  private String cognome;

  @NotNull
  @NotBlank
  private String cfAnagraficaEst;

  @JsonIgnore
  @NotNull
  @Min(value=1)
  private Double pagaOraria;

  @JsonIgnore
  private String subcontractor;

  @JsonIgnore
  @NotNull
  @Min(value=1)
  private Integer idEnte;

  @JsonIgnore
  private String enteAppartenenza;
  
  @JsonIgnore
  private String flagArchiviato; 
  
  @JsonIgnore
  private Integer idEntePapua; 
  
  @JsonIgnore
  private Long idAnagraficaEst;
  
  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }

  @JsonIgnore
  private Boolean active;

  public AnagraficaDTO()
  {
    super();
  }
   
  public AnagraficaDTO(Integer idAnagrafica)
  {
    super();
    this.idAnagrafica = idAnagrafica;
  }

  public AnagraficaDTO(String cfAnagraficaEst)
  {
    super();
    this.cfAnagraficaEst = cfAnagraficaEst;
  }

  public AnagraficaDTO(Boolean active)
  {
    super();
    this.active = active;
  }

  public AnagraficaDTO(Integer idAnagrafica, String nome, String cognome,
      String cfAnagraficaEst, Double pagaOraria,
      String subcontractor)
  {
    super();
    this.idAnagrafica = idAnagrafica;
    this.nome = nome;
    this.cognome = cognome;
    this.cfAnagraficaEst = cfAnagraficaEst;
    this.pagaOraria = pagaOraria;
    this.subcontractor = subcontractor;
  }

  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }

  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
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

  public Double getPagaOraria()
  {
    return pagaOraria;
  }

  public void setPagaOraria(Double pagaOraria)
  {
    this.pagaOraria = pagaOraria;
  }

  public String getSubcontractor()
  {
    return subcontractor;
  }

  public void setSubcontractor(String subcontractor)
  {
    this.subcontractor = subcontractor;
  }

  public String getCfAnagraficaEst()
  {
    return cfAnagraficaEst;
  }

  public void setCfAnagraficaEst(String cfAnagraficaEst)
  {
    this.cfAnagraficaEst = cfAnagraficaEst;
  }
  
  @JsonIgnore
  public String getCognomeNome() {
    return this.cognome + " " + this.nome;
  }

  public Integer getIdEnte()
  {
    return idEnte;
  }

  public void setIdEnte(Integer idEnte)
  {
    this.idEnte = idEnte;
  }

  public String getEnteAppartenenza()
  {
    return enteAppartenenza;
  }

  public void setEnteAppartenenza(String enteAppartenenza)
  {
    this.enteAppartenenza = enteAppartenenza;
  }

  public Boolean getActive()
  {
    return active;
  }

  public void setActive(Boolean active)
  {
    this.active = active;
  }

  public Integer getIdEntePapua()
  {
    return idEntePapua;
  }

  public void setIdEntePapua(Integer idEntePapua)
  {
    this.idEntePapua = idEntePapua;
  }

  public Long getIdAnagraficaEst()
  {
    return idAnagraficaEst;
  }

  public void setIdAnagraficaEst(Long idAnagraficaEst)
  {
    this.idAnagraficaEst = idAnagraficaEst;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((idAnagrafica == null) ? 0 : idAnagrafica.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AnagraficaDTO other = (AnagraficaDTO) obj;
    if (idAnagrafica == null)
    {
      if (other.idAnagrafica != null)
        return false;
    }
    else
      if (!idAnagrafica.equals(other.idAnagrafica))
        return false;
    return true;
  }

}
