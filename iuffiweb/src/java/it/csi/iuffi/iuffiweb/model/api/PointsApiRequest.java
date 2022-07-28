package it.csi.iuffi.iuffiweb.model.api;

import java.util.List;

public class PointsApiRequest
{

  private List<Integer> organismi;
  private String tipo ;
  
  public PointsApiRequest()
  {
    super();
  }

  public List<Integer> getOrganismi()
  {
    return organismi;
  }

  public void setOrganismi(List<Integer> organismi)
  {
    this.organismi = organismi;
  }

  public String getTipo()
  {
    return tipo;
  }

  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }
  
}
