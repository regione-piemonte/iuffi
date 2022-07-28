package it.csi.iuffi.iuffiweb.dto.estrazionecampione;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class NumeroLottoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 5918223009553272325L;

  private long              idNumeroLotto;
  private Date              dataElaborazione;
  private String            descrizione;
  private Long              idTipoEstrazione;
  private Long              idStatoEstrazione;
  private String            descrTipoEstrazione;
  private String            descrStatoEstrazione;
  private String            motivazione;
  private BigDecimal        importoRichiestoComplessivo;
  private BigDecimal        importoRichiestoEstrazione;
  private Long              numeroEstrazione;

  public long getIdNumeroLotto()
  {
    return idNumeroLotto;
  }

  public void setIdNumeroLotto(long idNumeroLotto)
  {
    this.idNumeroLotto = idNumeroLotto;
  }

  public Date getDataElaborazione()
  {
    return dataElaborazione;
  }

  public String getDataElaborazioneStr()
  {
    return IuffiUtils.DATE.formatDate(dataElaborazione);
  }

  public void setDataElaborazione(Date dataElaborazione)
  {
    this.dataElaborazione = dataElaborazione;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public Long getIdTipoEstrazione()
  {
    return idTipoEstrazione;
  }

  public void setIdTipoEstrazione(Long idTipoEstrazione)
  {
    this.idTipoEstrazione = idTipoEstrazione;
  }

  public Long getIdStatoEstrazione()
  {
    return idStatoEstrazione;
  }

  public void setIdStatoEstrazione(Long idStatoEstrazione)
  {
    this.idStatoEstrazione = idStatoEstrazione;
  }

  public String getDescrTipoEstrazione()
  {
    return descrTipoEstrazione;
  }

  public void setDescrTipoEstrazione(String descrTipoEstrazione)
  {
    this.descrTipoEstrazione = descrTipoEstrazione;
  }

  public String getDescrStatoEstrazione()
  {
    return descrStatoEstrazione;
  }

  public void setDescrStatoEstrazione(String descrStatoEstrazione)
  {
    this.descrStatoEstrazione = descrStatoEstrazione;
  }

  public BigDecimal getImportoRichiestoComplessivo()
  {
    return importoRichiestoComplessivo;
  }

  public void setImportoRichiestoComplessivo(
      BigDecimal importoRichiestoComplessivo)
  {
    this.importoRichiestoComplessivo = importoRichiestoComplessivo;
  }

  public BigDecimal getImportoRichiestoEstrazione()
  {
    return importoRichiestoEstrazione;
  }

  public void setImportoRichiestoEstrazione(
      BigDecimal importoRichiestoEstrazione)
  {
    this.importoRichiestoEstrazione = importoRichiestoEstrazione;
  }

  public String getMotivazione()
  {
    return motivazione;
  }

  public void setMotivazione(String motivazione)
  {
    this.motivazione = motivazione;
  }

  public Long getNumeroEstrazione()
  {
    return numeroEstrazione;
  }

  public void setNumeroEstrazione(Long numeroEstrazione)
  {
    this.numeroEstrazione = numeroEstrazione;
  }

}
