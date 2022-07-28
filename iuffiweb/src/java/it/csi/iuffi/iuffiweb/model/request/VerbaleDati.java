package it.csi.iuffi.iuffiweb.model.request;

import java.util.List;


import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.api.Coordinate;
import it.csi.iuffi.iuffiweb.model.api.VerbaleCampioni;
import it.csi.iuffi.iuffiweb.model.api.VerbaleTrappole;
import it.csi.iuffi.iuffiweb.model.api.VerbaleVisual;

public class VerbaleDati implements ILoggable
{
  private static final long serialVersionUID = 1L;
  private String numVerbale;
  private String rilevatore;
  private String data;
  private String oraInizio;
  private String oraFine;
  private List<VerbaleVisual> visual;
  private List<VerbaleCampioni> campioni;
  private List<VerbaleTrappole> trappole;
  public String getNumVerbale()
  {
    return numVerbale;
  }
  public void setNumVerbale(String numVerbale)
  {
    this.numVerbale = numVerbale;
  }
  public String getRilevatore()
  {
    return rilevatore;
  }
  public void setRilevatore(String rilevatore)
  {
    this.rilevatore = rilevatore;
  }
  public String getData()
  {
    return data;
  }
  public void setData(String data)
  {
    this.data = data;
  }
  public String getOraInizio()
  {
    return oraInizio;
  }
  public void setOraInizio(String oraInizio)
  {
    this.oraInizio = oraInizio;
  }
  public String getOraFine()
  {
    return oraFine;
  }
  public void setOraFine(String oraFine)
  {
    this.oraFine = oraFine;
  }
  public List<VerbaleVisual> getVisual()
  {
    return visual;
  }
  public void setVisual(List<VerbaleVisual> visual)
  {
    this.visual = visual;
  }

  public List<VerbaleCampioni> getCampioni()
  {
    return campioni;
  }
  public void setCampioni(List<VerbaleCampioni> campioni)
  {
    this.campioni = campioni;
  }
  public List<VerbaleTrappole> getTrappole()
  {
    return trappole;
  }
  public void setTrappole(List<VerbaleTrappole> trappole)
  {
    this.trappole = trappole;
  }  
}
  
  
//  //visual
//  private String comune;
//  private Coordinate coordinateGPS;
//  private String specieVegetaleIndagata;
//  private String tipologiaArea;
//  private String organismiNociviMonitorato;
//  private String note;
//  
//  //campioni
//  private int numCampioni;
//  private String codiceCampioni;
//  private String tipologiaCampioni;
//  
//  //trappolaggio
//  private String organismoNocivo;
//  private String tipologiaTrappola;
//  private Coordinate posizionamento;
//  private String raccolta;
//  private String codiceTrappola;
//  private String noteTrappola;
//  
//
//  private Integer idVisual;
//  public Integer getIdVisual()
//  {
//    return idVisual;
//  }
//  public void setIdVisual(Integer idVisual)
//  {
//    this.idVisual = idVisual;
//  }
//  public String getComune()
//  {
//    return comune;
//  }
//  public void setComune(String comune)
//  {
//    this.comune = comune;
//  }
//  public Coordinate getCoordinateGPS()
//  {
//    return coordinateGPS;
//  }
//  public void setCoordinateGPS(Coordinate coordinateGPS)
//  {
//    this.coordinateGPS = coordinateGPS;
//  }
//  public String getSpecieVegetaleIndagata()
//  {
//    return specieVegetaleIndagata;
//  }
//  public void setSpecieVegetaleIndagata(String specieVegetaleIndagata)
//  {
//    this.specieVegetaleIndagata = specieVegetaleIndagata;
//  }
//  public String getTipologiaArea()
//  {
//    return tipologiaArea;
//  }
//  public void setTipologiaArea(String tipologiaArea)
//  {
//    this.tipologiaArea = tipologiaArea;
//  }
//  public String getOrganismiNociviMonitorato()
//  {
//    return organismiNociviMonitorato;
//  }
//  public void setOrganismiNociviMonitorato(String organismiNociviMonitorato)
//  {
//    this.organismiNociviMonitorato = organismiNociviMonitorato;
//  }
//  public String getNote()
//  {
//    return note;
//  }
//  public void setNote(String note)
//  {
//    this.note = note;
//  }
//  public int getNumCampioni()
//  {
//    return numCampioni;
//  }
//  public void setNumCampioni(int numCampioni)
//  {
//    this.numCampioni = numCampioni;
//  }
//  public String getCodiceCampioni()
//  {
//    return codiceCampioni;
//  }
//  public void setCodiceCampioni(String codiceCampioni)
//  {
//    this.codiceCampioni = codiceCampioni;
//  }
//  public String getTipologiaCampioni()
//  {
//    return tipologiaCampioni;
//  }
//  public void setTipologiaCampioni(String tipologiaCampioni)
//  {
//    this.tipologiaCampioni = tipologiaCampioni;
//  }
//  public String getOrganismoNocivo()
//  {
//    return organismoNocivo;
//  }
//  public void setOrganismoNocivo(String organismoNocivo)
//  {
//    this.organismoNocivo = organismoNocivo;
//  }
//  public String getTipologiaTrappola()
//  {
//    return tipologiaTrappola;
//  }
//  public void setTipologiaTrappola(String tipologiaTrappola)
//  {
//    this.tipologiaTrappola = tipologiaTrappola;
//  }
//  public Coordinate getPosizionamento()
//  {
//    return posizionamento;
//  }
//  public void setPosizionamento(Coordinate posizionamento)
//  {
//    this.posizionamento = posizionamento;
//  }
//  public String getRaccolta()
//  {
//    return raccolta;
//  }
//  public void setRaccolta(String raccolta)
//  {
//    this.raccolta = raccolta;
//  }
//  public String getCodiceTrappola()
//  {
//    return codiceTrappola;
//  }
//  public void setCodiceTrappola(String codiceTrappola)
//  {
//    this.codiceTrappola = codiceTrappola;
//  }
//  public String getNoteTrappola()
//  {
//    return noteTrappola;
//  }
//  public void setNoteTrappola(String noteTrappola)
//  {
//    this.noteTrappola = noteTrappola;
//  }
//  
//}
