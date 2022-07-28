package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class InterventoAccertamentoDocumentiSpesaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long                       serialVersionUID = 2147868281407954334L;
  private long                                    idIntervento;
  private String                                  descIntervento;
  private int                                     progressivo;
  private String                                  flagInterventoCompletato;
  private String                                  note;
  private BigDecimal                              importoNonRiconosciutoSanzionabile;
  private BigDecimal                              importoNonRiconosciutoNonSanzionabile;
  private BigDecimal                              spesaAmmessa;
  private BigDecimal                              spesaRendicontataAttuale;
  private BigDecimal                              percentualeContributo;
  private List<RigaAccertamentoDocumentiSpesaDTO> accertamento;

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

  public List<RigaAccertamentoDocumentiSpesaDTO> getAccertamento()
  {
    return accertamento;
  }

  public void setAccertamento(
      List<RigaAccertamentoDocumentiSpesaDTO> accertamento)
  {
    this.accertamento = accertamento;
  }

  public String getDescrizioneEstesa()
  {
    return progressivo + " - " + descIntervento;
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

  public BigDecimal getImportoNonRiconosciutoSanzionabile()
  {
    return importoNonRiconosciutoSanzionabile;
  }

  public void setImportoNonRiconosciutoSanzionabile(
      BigDecimal importoNonRiconosciutoSanzionabile)
  {
    this.importoNonRiconosciutoSanzionabile = importoNonRiconosciutoSanzionabile;
  }

  public BigDecimal getImportoNonRiconosciutoNonSanzionabile()
  {
    return importoNonRiconosciutoNonSanzionabile;
  }

  public void setImportoNonRiconosciutoNonSanzionabile(
      BigDecimal importoNonRiconosciutoNonSanzionabile)
  {
    this.importoNonRiconosciutoNonSanzionabile = importoNonRiconosciutoNonSanzionabile;
  }

  public BigDecimal getSpesaAmmessa()
  {
    return spesaAmmessa;
  }

  public void setSpesaAmmessa(BigDecimal spesaAmmessa)
  {
    this.spesaAmmessa = spesaAmmessa;
  }

  public BigDecimal getSpesaRendicontataAttuale()
  {
    return spesaRendicontataAttuale;
  }

  public void setSpesaRendicontataAttuale(BigDecimal spesaRendicontataAttuale)
  {
    this.spesaRendicontataAttuale = spesaRendicontataAttuale;
  }

  public void addToSpesaRendicontataAttuale(BigDecimal importoRendicontato)
  {
    spesaRendicontataAttuale = IuffiUtils.NUMBERS
        .add(spesaRendicontataAttuale, importoRendicontato);
  }

  public BigDecimal getPercentualeContributo()
  {
    return percentualeContributo;
  }

  public void setPercentualeContributo(BigDecimal percentualeContributo)
  {
    this.percentualeContributo = percentualeContributo;
  }
}
