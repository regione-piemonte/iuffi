package it.csi.iuffi.iuffiweb.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RilevazioneDTO  extends TabelleDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 3696378108137287896L;
  
  private Integer idRilevazione;
  private Integer idMissione;
  private Integer idAnagrafica;
  private Integer idTipoArea;
  private String visual;
  private String note;
  private String campionamento;
  private String trappolaggio;
  //new data ora
  @NotNull
  private Date dataOraInizio;
  
  private Date dataOraFine;
  
  @NotNull
  private Integer oraInizio;

  private Integer oraFine;
  
  private String flagEmergenza;
  //aggiunte
  private String operatore;
  private String area;
  // Aggiunte 15/03/21
  private String numeroAviv;
  private String cuaa;
  private Long idUte;
  //
  @JsonIgnore
  private String oraInizioS;
  @JsonIgnore
  private String oraFineS;
  
  // S.D.
  private List<IspezioneVisivaDTO> ispezioniVisive;
  private List<CampionamentoDTO> campionamenti;
  private List<TrappolaggioDTO> trappolaggi;
 
  
  public RilevazioneDTO()
  {
    super();
  }


  public RilevazioneDTO(Integer idRilevazione, Integer idMissione,
      Integer idAnagrafica, Integer idTipoArea, String visual, String note,
      String campionamento, String trappolaggio, Date dataOraInizio,
      Date dataOraFine, Integer oraInizio, Integer oraFine)
  {
    super();
    this.idRilevazione = idRilevazione;
    this.idMissione = idMissione;
    this.idAnagrafica = idAnagrafica;
    this.idTipoArea = idTipoArea;
    this.visual = visual;
    this.note = note;
    this.campionamento = campionamento;
    this.trappolaggio = trappolaggio;
    this.dataOraInizio = dataOraInizio;
    this.dataOraFine = dataOraFine;
    this.oraInizio = oraInizio;
    //this.minutiInizio = minutiInizio;
    this.oraFine = oraFine;
    //this.minutiFine = minutiFine;
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


//  public Integer getOraInizio()
//  {
//    return oraInizio;
//  }
//
//
//  public void setOraInizio(Integer oraInizio)
//  {
//    this.oraInizio = oraInizio;
//  }
//
//
//  public Integer getMinutiInizio()
//  {
//    return minutiInizio;
//  }
//
//
//  public void setMinutiInizio(Integer minutiInizio)
//  {
//    this.minutiInizio = minutiInizio;
//  }
  
  public String getFlagEmergenza()
  {
    return flagEmergenza;
  }


  public void setFlagEmergenza(String flagEmergenza)
  {
    this.flagEmergenza = flagEmergenza;
  }


//  public Integer getOraFine()
//  {
//    return oraFine;
//  }
//
//
//  public void setOraFine(Integer oraFine)
//  {
//    this.oraFine = oraFine;
//  }
//
//
//  public Integer getMinutiFine()
//  {
//    return minutiFine;
//  }
//
//
//  public void setMinutiFine(Integer minutiFine)
//  {
//    this.minutiFine = minutiFine;
//  }


  public Date getDataOraInizio()
  {
    return dataOraInizio;
  }

  public void setDataOraInizio(Date dataOraInizio)
  {
    this.dataOraInizio = dataOraInizio;
  }
  
  public Date getDataOraFine()
  {
    return dataOraFine;
  }

  public void setDataOraFine(Date dataOraFine)
  {
    this.dataOraFine = dataOraFine;
  }


  public Integer getOraInizio()
  {
    return oraInizio;
  }


  public void setOraInizio(Integer oraInizio)
  {
    this.oraInizio = oraInizio;
  }


  public Integer getOraFine()
  {
    return oraFine;
  }


  public void setOraFine(Integer oraFine)
  {
    this.oraFine = oraFine;
  }



  public String getOperatore()
  {
    return operatore;
  }


  public void setOperatore(String operatore)
  {
    this.operatore = operatore;
  }


  public String getArea()
  {
    return area;
  }


  public void setArea(String area)
  {
    this.area = area;
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


  public String getOraInizioS()
  {
    return oraInizioS;
  }


  public void setOraInizioS(String oraInizioS)
  {
    this.oraInizioS = oraInizioS;
  }


  public String getOraFineS()
  {
    return oraFineS;
  }


  public void setOraFineS(String oraFineS)
  {
    this.oraFineS = oraFineS;
  }

}
