package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class PraticaConcessioneDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -6427903577889338214L;

  private Long idConcessione;
  private Long idPraticheConcessione;
  private Long idBando;
  private Long idAmmCompetenza;
  private Long idPratica;
  private String denominazioneBando;
  private String annoCampagna;
  private String ammCompetenza;
  private Date dataProtocollo;
  private Long numeroProtocollo;
  private String atto;
  private String stato;
  private Date dal;
  private String note;
  private String pratica;
  private BigDecimal importoPerizia;
  private BigDecimal importoLiquidazione;
  private String descrizioneStatoPratica;
  private String cuaa;
  private String denominazione;
  private String codiceVercor;
  private Date dataVisura;

  
  public String getCuaa()
  {
    return cuaa;
  }
  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public BigDecimal getImportoPerizia()
  {
    return importoPerizia!=null ? importoPerizia : BigDecimal.ZERO;
  }
  public void setImportoPerizia(BigDecimal importoPerizia)
  {
    this.importoPerizia = importoPerizia;
  }
  public BigDecimal getImportoLiquidazione()
  {
    return importoLiquidazione;
  }
  public void setImportoLiquidazione(BigDecimal importoLiquidazione)
  {
    this.importoLiquidazione = importoLiquidazione;
  }
  public String getDescrizioneStatoPratica()
  {
    return descrizioneStatoPratica;
  }
  public void setDescrizioneStatoPratica(String descrizioneStatoPratica)
  {
    this.descrizioneStatoPratica = descrizioneStatoPratica;
  }
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
  public String getPratica()
  {
    return pratica;
  }
  public void setPratica(String pratica)
  {
    this.pratica = pratica;
  }
  public String getCodiceVercor()
  {
    return codiceVercor;
  }
  public void setCodiceVercor(String codiceVercor)
  {
    this.codiceVercor = codiceVercor;
  }
  public Date getDataVisura()
  {
    return dataVisura;
  }
  public void setDataVisura(Date dataVisura)
  {
    this.dataVisura = dataVisura;
  }
  public Long getIdPratica()
  {
    return idPratica;
  }
  public void setIdPratica(Long idPratica)
  {
    this.idPratica = idPratica;
  }
  public String getDataVisuraStr()
  {
    return IuffiUtils.DATE.formatDate(dataVisura);
  }
  public String getCodiceVercorSN()
  {
    if(codiceVercor==null)
      return "N";
    return "S";
  }
  public Long getIdPraticheConcessione()
  {
    return idPraticheConcessione;
  }
  public void setIdPraticheConcessione(Long idPraticheConcessione)
  {
    this.idPraticheConcessione = idPraticheConcessione;
  }

}
