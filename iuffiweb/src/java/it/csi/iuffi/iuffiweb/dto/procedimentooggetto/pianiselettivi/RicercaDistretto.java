package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RicercaDistretto implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */
  
  private Long iddistretto;
  private Long iddistrettoregione;
  
  public Long getIddistretto()
  {
    return iddistretto;
  }
  public void setIddistretto(Long iddistretto)
  {
    this.iddistretto = iddistretto;
  }
  public Long getIddistrettoregione()
  {
    return iddistrettoregione;
  }
  public void setIddistrettoregione(Long iddistrettoregione)
  {
    this.iddistrettoregione = iddistrettoregione;
  } 
  
  
 
}
