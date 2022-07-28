package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SettoriDiProduzioneDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -8588871550254625010L;
  /**
   * 
   */
  private long              idSettore;
  private String            descrizione;
  private Date              dataInizio;
  private Date              dataFine;

  public long getIdSettore()
  {
    return idSettore;
  }

  public void setIdSettore(long idSettore)
  {
    this.idSettore = idSettore;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public String getDataInizioStr()
  {
    return IuffiUtils.DATE.formatDate(dataInizio);
  }

  public String getDataFineStr()
  {
    return IuffiUtils.DATE.formatDate(dataFine);
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
}
