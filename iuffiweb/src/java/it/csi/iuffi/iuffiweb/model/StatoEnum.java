package it.csi.iuffi.iuffiweb.model;

public enum StatoEnum
{
 
  APERTA_BO("APERTA BO"),
  APERTA_APP("APERTA APP"),
  IN_CORSO("IN CORSO"),
  CHIUSA("CHIUSA");
  
  private String descrizione;

  StatoEnum(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getName() {
    return this.name();
  }

}
