package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale;


import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ConsistenzaAziendale implements ILoggable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** serialVersionUID */

  private List<ProduzioneVegetale> produzioniVegetali;
  private List<ProduzioneAnimale> produzioniAnimali;
  
  public List<ProduzioneVegetale> getProduzioniVegetali()
  {
    return produzioniVegetali;
  }
  public void setProduzioniVegetali(List<ProduzioneVegetale> produzioniVegetali)
  {
    this.produzioniVegetali = produzioniVegetali;
  }
  public List<ProduzioneAnimale> getProduzioniAnimali()
  {
    return produzioniAnimali;
  }
  public void setProduzioniAnimali(List<ProduzioneAnimale> produzioniAnimale)
  {
    this.produzioniAnimali = produzioniAnimale;
  }
  
  
  
  
}
