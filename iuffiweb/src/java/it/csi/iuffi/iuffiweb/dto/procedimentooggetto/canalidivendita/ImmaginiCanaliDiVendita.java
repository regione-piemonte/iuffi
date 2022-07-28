package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.canalidivendita;


import java.sql.Blob;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ImmaginiCanaliDiVendita implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private Long id;
  private String descrizione;
  private byte[] immagine;
  private boolean selezionata;
  
  public Long getId()
  {
    return id;
  }
  public void setId(Long id)
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
  public byte[] getImmagine()
  {
    return immagine;
  }
  public void setImmagine(byte[] immagine)
  {
    this.immagine = immagine;
  }
  public boolean isSelezionata()
  {
    return selezionata;
  }
  public void setSelezionata(boolean selezionata)
  {
    this.selezionata = selezionata;
  }
  
  
 
  
  
}
