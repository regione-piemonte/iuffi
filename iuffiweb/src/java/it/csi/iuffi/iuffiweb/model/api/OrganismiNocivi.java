package it.csi.iuffi.iuffiweb.model.api;

public class OrganismiNocivi
{

  private Integer idSpecieOn;
  private String flagTrovato;
  
  
  public OrganismiNocivi()
  {
    super();
  }


  public Integer getIdSpecieOn()
  {
    return idSpecieOn;
  }


  public void setIdSpecieOn(Integer idSpecieOn)
  {
    this.idSpecieOn = idSpecieOn;
  }


  public String getFlagTrovato()
  {
    return flagTrovato;
  }


  public void setFlagTrovato(String flagTrovato)
  {
    this.flagTrovato = flagTrovato;
  }


  public OrganismiNocivi(Integer idSpecieOn, String flagTrovato)
  {
    super();
    this.idSpecieOn = idSpecieOn;
    this.flagTrovato = flagTrovato;
  }



 
  
}
