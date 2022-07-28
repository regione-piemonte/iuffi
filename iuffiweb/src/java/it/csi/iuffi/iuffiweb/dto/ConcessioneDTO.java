package it.csi.iuffi.iuffiweb.dto;

import java.util.Date;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ConcessioneDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -6427903577889338214L;

  private Long idConcessione;
  private Long idBando;
  private Long idAmmCompetenza;
  private Long idAmmCompetenzaPadre;
  private String denominazioneBando;
  private String annoCampagna;
  private String ammCompetenza;
  private Date dataProtocollo;
  private Date dataVisura;
  private Long numeroProtocollo;
  private String atto;
  private String stato;
  private Long idStatoConcessione;
  private Date dal;
  private String note;
  private List<PraticaConcessioneDTO> pratiche; 
  private Long idTipoAtto;
  private Long extIdVisMassivaRna;

  public Long getIdConcessione()
  {
    return idConcessione;
  }
  public void setIdConcessione(Long idConcessione)
  {
    this.idConcessione = idConcessione;
  }
  public Long getIdBando()
  {
    return idBando;
  }
  public void setIdBando(Long idBando)
  {
    this.idBando = idBando;
  }
  public String getDenominazioneBando()
  {
    return denominazioneBando;
  }
  public void setDenominazioneBando(String denominazioneBando)
  {
    this.denominazioneBando = denominazioneBando;
  }
  public String getAnnoCampagna()
  {
    return annoCampagna;
  }
  public void setAnnoCampagna(String annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }
  public String getAmmCompetenza()
  {
    return ammCompetenza;
  }
  public void setAmmCompetenza(String ammCompetenza)
  {
    this.ammCompetenza = ammCompetenza;
  }
  public Date getDataProtocollo()
  {
    return dataProtocollo;
  }
  public void setDataProtocollo(Date dataProtocollo)
  {
    this.dataProtocollo = dataProtocollo;
  }
  public Long getNumeroProtocollo()
  {
    return numeroProtocollo;
  }
  public void setNumeroProtocollo(Long numeroProtocollo)
  {
    this.numeroProtocollo = numeroProtocollo;
  }
  public String getAtto()
  {
    return atto;
  }
  public void setAtto(String atto)
  {
    this.atto = atto;
  }
  public String getStato()
  {
    return stato;
  }
  public void setStato(String stato)
  {
    this.stato = stato;
  }
  public Date getDal()
  {
    return dal;
  }
  public void setDal(Date dal)
  {
    this.dal = dal;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public String getDataProtocolloStr()
  {
    return dataProtocollo==null ? "" : IuffiUtils.DATE.formatDate(dataProtocollo);
  }
  public String getDalStr()
  {
    return dal==null ? "" : IuffiUtils.DATE.formatDate(dal);
  }
  public Long getIdAmmCompetenza()
  {
    return idAmmCompetenza;
  }
  public void setIdAmmCompetenza(Long idAmmCompetenza)
  {
    this.idAmmCompetenza = idAmmCompetenza;
  }
  public List<PraticaConcessioneDTO> getPratiche()
  {
    return pratiche;
  }
  public void setPratiche(List<PraticaConcessioneDTO> pratiche)
  {
    this.pratiche = pratiche;
  }
  public Long getIdAmmCompetenzaPadre()
  {
    return idAmmCompetenzaPadre;
  }
  public void setIdAmmCompetenzaPadre(Long idAmmCompetenzaPadre)
  {
    this.idAmmCompetenzaPadre = idAmmCompetenzaPadre;
  }
  public Date getDataVisura()
  {
    return dataVisura;
  }
  public void setDataVisura(Date dataVisura)
  {
    this.dataVisura = dataVisura;
  }
  public String getDataVisuraStr()
  {
    return IuffiUtils.DATE.formatDate(dataVisura);
  }
  public Long getIdTipoAtto()
  {
    return idTipoAtto;
  }
  public void setIdTipoAtto(Long idTipoAtto)
  {
    this.idTipoAtto = idTipoAtto;
  }
  public Long getIdStatoConcessione()
  {
    return idStatoConcessione;
  }
  public void setIdStatoConcessione(Long idStatoConcessione)
  {
    this.idStatoConcessione = idStatoConcessione;
  }
  public Long getExtIdVisMassivaRna()
  {
    return extIdVisMassivaRna;
  }
  public void setExtIdVisMassivaRna(Long extIdVisMassivaRna)
  {
    this.extIdVisMassivaRna = extIdVisMassivaRna;
  }
}
