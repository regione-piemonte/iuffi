package it.csi.iuffi.iuffiweb.model;



public class AutorizzazioniDTO 
{
  
  private Long ExtIdLivello;
  private String ExtCodMacroCdu;
  
  public AutorizzazioniDTO(Long extIdLivello, String extCodMacroCdu)
  {
    super();
    ExtIdLivello = extIdLivello;
    ExtCodMacroCdu = extCodMacroCdu;
  }
  
  public AutorizzazioniDTO()
  {
    super();
    // TODO Auto-generated constructor stub
  }
  
  public Long getExtIdLivello()
  {
    return ExtIdLivello;
  }
  public void setExtIdLivello(Long extIdLivello)
  {
    this.ExtIdLivello = extIdLivello;
  }
  public String getExtCodMacroCdu()
  {
    return ExtCodMacroCdu;
  }
  public void setExtCodMacroCdu(String extCodMacroCdu)
  {
    this.ExtCodMacroCdu = extCodMacroCdu;
  }

  


}
