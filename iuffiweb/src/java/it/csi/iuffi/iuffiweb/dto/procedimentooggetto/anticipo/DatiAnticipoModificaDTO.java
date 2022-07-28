package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DatiAnticipoModificaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6865684393141163651L;
  protected Integer         extIdSportello;
  protected String          altroIstituto;
  protected String          indirizzoAltroIstituto;
  protected String          extIstatComune;
  protected long            idAnticipo;
  protected BigDecimal      percentualeAnticipo;
  protected BigDecimal      importoAnticipo;
  protected BigDecimal      importoFideiussione;
  protected String          numeroFideiussione;
  protected Date            dataStipula;
  protected Date            dataScadenza;
  protected String          beneficiarioFideiussione;

  public Integer getExtIdSportello()
  {
    return extIdSportello;
  }

  public void setExtIdSportello(Integer extIdSportello)
  {
    this.extIdSportello = extIdSportello;
  }

  public String getAltroIstituto()
  {
    return altroIstituto;
  }

  public void setAltroIstituto(String altroIstituto)
  {
    this.altroIstituto = altroIstituto;
  }

  public String getIndirizzoAltroIstituto()
  {
    return indirizzoAltroIstituto;
  }

  public void setIndirizzoAltroIstituto(String indirizzoAltroIstituto)
  {
    this.indirizzoAltroIstituto = indirizzoAltroIstituto;
  }

  public String getExtIstatComune()
  {
    return extIstatComune;
  }

  public void setExtIstatComune(String extIstatComune)
  {
    this.extIstatComune = extIstatComune;
  }

  public long getIdAnticipo()
  {
    return idAnticipo;
  }

  public void setIdAnticipo(long idAnticipo)
  {
    this.idAnticipo = idAnticipo;
  }

  public BigDecimal getPercentualeAnticipo()
  {
    return percentualeAnticipo;
  }

  public void setPercentualeAnticipo(BigDecimal percentualeAnticipo)
  {
    this.percentualeAnticipo = percentualeAnticipo;
  }

  public BigDecimal getImportoAnticipo()
  {
    return importoAnticipo;
  }

  public void setImportoAnticipo(BigDecimal importoAnticipo)
  {
    this.importoAnticipo = importoAnticipo;
  }

  public BigDecimal getImportoFideiussione()
  {
    return importoFideiussione;
  }

  public void setImportoFideiussione(BigDecimal importoFideiussione)
  {
    this.importoFideiussione = importoFideiussione;
  }

  public String getNumeroFideiussione()
  {
    return numeroFideiussione;
  }

  public void setNumeroFideiussione(String numeroFideiussione)
  {
    this.numeroFideiussione = numeroFideiussione;
  }

  public Date getDataStipula()
  {
    return dataStipula;
  }

  public void setDataStipula(Date dataStipula)
  {
    this.dataStipula = dataStipula;
  }

  public Date getDataScadenza()
  {
    return dataScadenza;
  }

  public void setDataScadenza(Date dataScadenza)
  {
    this.dataScadenza = dataScadenza;
  }

  public String getBeneficiarioFideiussione()
  {
    return beneficiarioFideiussione;
  }

  public void setBeneficiarioFideiussione(String beneficiarioFideiussione)
  {
    this.beneficiarioFideiussione = beneficiarioFideiussione;
  }
}
