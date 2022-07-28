package it.csi.iuffi.iuffiweb.model.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.model.CampionamentoDTO;
import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.model.TrappolaggioDTO;

public class Rilevazione implements ILoggable
{

  private static final long serialVersionUID = 1L;

  private Integer idRilevazione;
  @NotNull
  private Integer idMissione;
 
  @JsonIgnore
  private String dataRilevazione;
 
  @NotNull
  private String oraInizio;
  private String oraFine;
  
  private Integer idAnagrafica;
  private Integer idTipoArea;
  private String visual;
  private String note;
  private String campionamento;
  private String trappolaggio;
  private String flagEmergenza;
  // Aggiunte 15/03/21
  private String numeroAviv;
  private String cuaa;
  private Long idUte;
  //
  
  // S.D.
  private List<IspezioneVisivaDTO> ispezioniVisive;
  private List<CampionamentoDTO> campionamenti;
  private List<TrappolaggioDTO> trappolaggi;

  public Rilevazione() {
    super();
  }
 
  
  public Integer getIdRilevazione()
  {
    return idRilevazione;
  }

  public void setIdRilevazione(Integer idRilevazione)
  {
    this.idRilevazione = idRilevazione;
  }

  public Integer getIdMissione()
  {
    return idMissione;
  }

  public void setIdMissione(Integer idMissione)
  {
    this.idMissione = idMissione;
  }
  
  public String getDataRilevazione()
  {
    return dataRilevazione;
  }

  public void setDataRilevazione(String dataRilevazione)
  {
    this.dataRilevazione = dataRilevazione;
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
  
  public Integer getIdAnagrafica()
  {
    return idAnagrafica;
  }


  public void setIdAnagrafica(Integer idAnagrafica)
  {
    this.idAnagrafica = idAnagrafica;
  }


  public Integer getIdTipoArea()
  {
    return idTipoArea;
  }


  public void setIdTipoArea(Integer idTipoArea)
  {
    this.idTipoArea = idTipoArea;
  }


  public String getVisual()
  {
    return visual;
  }


  public void setVisual(String visual)
  {
    this.visual = visual;
  }


  public String getNote()
  {
    return note;
  }


  public void setNote(String note)
  {
    this.note = note;
  }


  public String getCampionamento()
  {
    return campionamento;
  }


  public void setCampionamento(String campionamento)
  {
    this.campionamento = campionamento;
  }


  public String getTrappolaggio()
  {
    return trappolaggio;
  }


  public void setTrappolaggio(String trappolaggio)
  {
    this.trappolaggio = trappolaggio;
  }


  public String getFlagEmergenza()
  {
    return flagEmergenza;
  }


  public void setFlagEmergenza(String flagEmergenza)
  {
    this.flagEmergenza = flagEmergenza;
  }


  public String getNumeroAviv()
  {
    return numeroAviv;
  }


  public void setNumeroAviv(String numeroAviv)
  {
    this.numeroAviv = numeroAviv;
  }


  public String getCuaa()
  {
    return cuaa;
  }


  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }


  public Long getIdUte()
  {
    return idUte;
  }


  public void setIdUte(Long idUte)
  {
    this.idUte = idUte;
  }


  public List<IspezioneVisivaDTO> getIspezioniVisive()
  {
    return ispezioniVisive;
  }


  public void setIspezioniVisive(List<IspezioneVisivaDTO> ispezioniVisive)
  {
    this.ispezioniVisive = ispezioniVisive;
  }


  public List<CampionamentoDTO> getCampionamenti()
  {
    return campionamenti;
  }


  public void setCampionamenti(List<CampionamentoDTO> campionamenti)
  {
    this.campionamenti = campionamenti;
  }


  public List<TrappolaggioDTO> getTrappolaggi()
  {
    return trappolaggi;
  }


  public void setTrappolaggi(List<TrappolaggioDTO> trappolaggi)
  {
    this.trappolaggi = trappolaggi;
  }

}
