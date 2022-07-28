package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class InterventoRendicontazioneDocumentiSpesaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long                          serialVersionUID = 2147868281407954334L;
  private long                                       idIntervento;
  private String                                     descIntervento;
  private int                                        progressivo;
  private BigDecimal                                 percentualeContributo;
  private String                                     flagInterventoCompletato;
  private String                                     note;
  private String                                     codiceLivello;
  private List<RigaRendicontazioneDocumentiSpesaDTO> rendicontazione;

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
  }

  public String getDescIntervento()
  {
    return descIntervento;
  }

  public void setDescIntervento(String descIntervento)
  {
    this.descIntervento = descIntervento;
  }

  public int getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(int progressivo)
  {
    this.progressivo = progressivo;
  }

  public List<RigaRendicontazioneDocumentiSpesaDTO> getRendicontazione()
  {
    return rendicontazione;
  }

  public void setRendicontazione(
      List<RigaRendicontazioneDocumentiSpesaDTO> rendicontazione)
  {
    this.rendicontazione = rendicontazione;
  }

  public String getDescrizioneEstesa()
  {
    return progressivo + " - " + descIntervento;
  }

  public BigDecimal getPercentualeContributo()
  {
    return percentualeContributo;
  }

  public void setPercentualeContributo(BigDecimal percentualeContributo)
  {
    this.percentualeContributo = percentualeContributo;
  }

  public String getFlagInterventoCompletato()
  {
    return flagInterventoCompletato;
  }

  public void setFlagInterventoCompletato(String flagInterventoCompletato)
  {
    this.flagInterventoCompletato = flagInterventoCompletato;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }
}
