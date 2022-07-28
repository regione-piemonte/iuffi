package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianografico;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AnomaliaVO implements ILoggable
{

  private static final long serialVersionUID = -8064889027606525236L;

  private String            codice;
  private String            descrizione;
  private String            descrizioneAnomalia;
  private String            ulterioriInformazioni;
  private String            gravita;

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getDescrizioneAnomalia()
  {
    return descrizioneAnomalia;
  }

  public void setDescrizioneAnomalia(String descrizioneAnomalia)
  {
    this.descrizioneAnomalia = descrizioneAnomalia;
  }

  public String getUlterioriInformazioni()
  {
    return ulterioriInformazioni;
  }

  public void setUlterioriInformazioni(String ulterioriInformazioni)
  {
    this.ulterioriInformazioni = ulterioriInformazioni;
  }

  public String getGravita()
  {
    return gravita;
  }

  public void setGravita(String gravita)
  {
    this.gravita = gravita;
  }

}
