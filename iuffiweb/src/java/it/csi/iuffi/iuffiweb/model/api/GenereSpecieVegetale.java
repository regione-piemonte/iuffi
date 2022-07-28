package it.csi.iuffi.iuffiweb.model.api;


import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class GenereSpecieVegetale implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  private Integer idGenereSpecie;
  private String genereSpecie;
  private String flagEuro;
  private String codiceEppo;
  
  private List<SpecieVegetale> listaSpecieVegetali;

  
  public GenereSpecieVegetale()
  {
    super();
  }

  public Integer getIdGenereSpecie()
  {
    return idGenereSpecie;
  }

  public void setIdGenereSpecie(Integer idGenereSpecie)
  {
    this.idGenereSpecie = idGenereSpecie;
  }

  public String getGenereSpecie()
  {
    return genereSpecie;
  }

  public void setGenereSpecie(String genereSpecie)
  {
    this.genereSpecie = genereSpecie;
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

  public List<SpecieVegetale> getListaSpecieVegetali()
  {
    return listaSpecieVegetali;
  }

  public void setListaSpecieVegetali(List<SpecieVegetale> listaSpecieVegetali)
  {
    this.listaSpecieVegetali = listaSpecieVegetali;
  }

}
