package it.csi.iuffi.iuffiweb.model;

public class DataFilter
{

  private Integer idAnagrafica;
  private Integer idEnte;
  
  public DataFilter()
  {
    super();
  }

  public DataFilter(Integer idAnagrafica, Integer idEnte)
  {
    super();
    this.idAnagrafica = idAnagrafica;
    this.idEnte = idEnte;
  }

  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }

  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
  }

  public Integer getIdEnte()
  {
    return idEnte;
  }

  public void setIdEnte(Integer idEnte)
  {
    this.idEnte = idEnte;
  }

}
