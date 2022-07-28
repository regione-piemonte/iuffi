package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.trasformazioneprodotti;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ProdottoTrasformatoInserimento implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private String id; 
  private String descrizione;
  private String nomecampo;
  
  public String getId()
  {
    return id;
  }
  public void setId(String id)
  {
    this.id = id;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getNomecampo()
  {
    return nomecampo;
  }
  public void setNomecampo(String nomecampo)
  {
    this.nomecampo = nomecampo;
  }
  
  

  
  

}
