package it.csi.iuffi.iuffiweb.model;


import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class SpecieVegetaleDTO extends TabelleStoricizzateDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  private Integer idSpecieVegetale;
  private String genereSpecie;
  private String nomeVolgare;
  private String flagEuro;
  @JsonIgnore
  private String codiceEppo;

  @JsonIgnore
  private String flagArchiviato; 

  public SpecieVegetaleDTO()
  {
    super();
  }
  
  public SpecieVegetaleDTO(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }

  public String getFlagArchiviato()
  {
    return flagArchiviato;
  }

  public void setFlagArchiviato(String flagArchiviato)
  {
    this.flagArchiviato = flagArchiviato;
  }

  public Integer getIdSpecieVegetale()
  {
    return idSpecieVegetale;
  }
  public void setIdSpecieVegetale(Integer idSpecieVegetale)
  {
    this.idSpecieVegetale = idSpecieVegetale;
  }
  public String getGenereSpecie()
  {
    return genereSpecie;
  }
  public void setGenereSpecie(String genereSpecie)
  {
    this.genereSpecie = genereSpecie;
  }
  public String getNomeVolgare()
  {
    return nomeVolgare;
  }
  public void setNomeVolgare(String nomeVolgare)
  {
    this.nomeVolgare = nomeVolgare;
  }
  public String getFlagEuro()
  {
    return flagEuro;
  }
  public void setFlagEuro(String flagEuro)
  {
    this.flagEuro = flagEuro;
  }
  public String getCodiceEppo()
  {
    return codiceEppo;
  }

  public void setCodiceEppo(String codiceEppo)
  {
    this.codiceEppo = codiceEppo;
  }

  public String getGenereENomeVolgare() {
    return this.nomeVolgare + " - " + this.genereSpecie;
  }

  public String getPiantaECodiceEppo() {
    return this.nomeVolgare + " (" + this.codiceEppo + ")";
  }

}
