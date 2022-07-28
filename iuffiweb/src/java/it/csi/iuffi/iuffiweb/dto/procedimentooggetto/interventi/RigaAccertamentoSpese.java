package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RigaAccertamentoSpese implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 2227543651315107847L;
  private long              idIntervento;
  private long              idDettaglioIntervento;
  private int               progressivo;
  private String            descTipoIntervento;
  private String            descIntervento;
  private String            ulterioriInformazioni;
  private BigDecimal        spesaAmmessa;
  private BigDecimal        percentualeContributo;
  private BigDecimal        importoContributo;
  private BigDecimal        speseSostenute;
  private BigDecimal        speseAccertate;
  private BigDecimal        spesaSostenutaAttuale;
  private BigDecimal        speseAccertateAttuali;
  private BigDecimal        spesaRiconosciutaPerCalcolo;
  private BigDecimal        importoNonRiconosciuto;
  private BigDecimal        importoRispendibile;
  private BigDecimal        contributoCalcolato;
  private BigDecimal        contributoAbbattuto;
  private String            flagInterventoCompletato;
  private String            note;
  private long              idTipoLocalizzazione;
  private String            usaDocumentiSpesa;
  private String            hasDocumentoSpesa;

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
  }

  public long getIdDettaglioIntervento()
  {
    return idDettaglioIntervento;
  }

  public void setIdDettaglioIntervento(long idDettaglioIntervento)
  {
    this.idDettaglioIntervento = idDettaglioIntervento;
  }

  public int getProgressivo()
  {
    return progressivo;
  }

  public void setProgressivo(int progressivo)
  {
    this.progressivo = progressivo;
  }

  public String getDescTipoIntervento()
  {
    return descTipoIntervento;
  }

  public void setDescTipoIntervento(String descTipoIntervento)
  {
    this.descTipoIntervento = descTipoIntervento;
  }

  public String getDescIntervento()
  {
    return descIntervento;
  }

  public void setDescIntervento(String descIntervento)
  {
    this.descIntervento = descIntervento;
  }

  public String getUlterioriInformazioni()
  {
    return ulterioriInformazioni;
  }

  public void setUlterioriInformazioni(String ulterioriInformazioni)
  {
    this.ulterioriInformazioni = ulterioriInformazioni;
  }

  public BigDecimal getSpesaAmmessa()
  {
    return spesaAmmessa;
  }

  public void setSpesaAmmessa(BigDecimal spesaAmmessa)
  {
    this.spesaAmmessa = spesaAmmessa;
  }

  public BigDecimal getPercentualeContributo()
  {
    return percentualeContributo;
  }

  public void setPercentualeContributo(BigDecimal percentualeContributo)
  {
    this.percentualeContributo = percentualeContributo;
  }

  public BigDecimal getImportoContributo()
  {
    return importoContributo;
  }

  public void setImportoContributo(BigDecimal importoContributo)
  {
    this.importoContributo = importoContributo;
  }

  public BigDecimal getSpeseSostenute()
  {
    return speseSostenute;
  }

  public void setSpeseSostenute(BigDecimal speseSostenute)
  {
    this.speseSostenute = speseSostenute;
  }

  public BigDecimal getSpeseAccertate()
  {
    return speseAccertate;
  }

  public void setSpeseAccertate(BigDecimal speseAccertate)
  {
    this.speseAccertate = speseAccertate;
  }

  public BigDecimal getSpesaSostenutaAttuale()
  {
    return spesaSostenutaAttuale;
  }

  public void setSpesaSostenutaAttuale(BigDecimal spesaSostenutaAttuale)
  {
    this.spesaSostenutaAttuale = spesaSostenutaAttuale;
  }

  public BigDecimal getSpeseAccertateAttuali()
  {
    return speseAccertateAttuali;
  }

  public void setSpeseAccertateAttuali(BigDecimal speseAccertateAttuali)
  {
    this.speseAccertateAttuali = speseAccertateAttuali;
  }

  public BigDecimal getSpesaRiconosciutaPerCalcolo()
  {
    return spesaRiconosciutaPerCalcolo;
  }

  public void setSpesaRiconosciutaPerCalcolo(
      BigDecimal spesaRiconosciutaPerCalcolo)
  {
    this.spesaRiconosciutaPerCalcolo = spesaRiconosciutaPerCalcolo;
  }

  public BigDecimal getImportoNonRiconosciuto()
  {
    return importoNonRiconosciuto;
  }

  public void setImportoNonRiconosciuto(BigDecimal importoNonRiconosciuto)
  {
    this.importoNonRiconosciuto = importoNonRiconosciuto;
  }

  public BigDecimal getImportoRispendibile()
  {
    return importoRispendibile;
  }

  public void setImportoRispendibile(BigDecimal importoRispendibile)
  {
    this.importoRispendibile = importoRispendibile;
  }

  public BigDecimal getContributoCalcolato()
  {
    return contributoCalcolato;
  }

  public void setContributoCalcolato(BigDecimal contributoCalcolato)
  {
    this.contributoCalcolato = contributoCalcolato;
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

  public boolean isSpesaSostenutaAttualeValida()
  {
    return spesaSostenutaAttuale != null
        && spesaSostenutaAttuale.compareTo(BigDecimal.ZERO) > 0;
  }

  public BigDecimal getContributoAbbattuto()
  {
    return contributoAbbattuto;
  }

  public void setContributoAbbattuto(BigDecimal contributoAbbattuto)
  {
    this.contributoAbbattuto = contributoAbbattuto;
  }

  public long getIdTipoLocalizzazione()
  {
    return idTipoLocalizzazione;
  }

  public void setIdTipoLocalizzazione(long idTipoLocalizzazione)
  {
    this.idTipoLocalizzazione = idTipoLocalizzazione;
  }

  public String getUsaDocumentiSpesa()
  {
    return usaDocumentiSpesa;
  }

  public void setUsaDocumentiSpesa(String usaDocumentiSpesa)
  {
    this.usaDocumentiSpesa = usaDocumentiSpesa;
  }

  public String getHasDocumentoSpesa()
  {
    return hasDocumentoSpesa;
  }

  public void setHasDocumentoSpesa(String hasDocumentoSpesa)
  {
    this.hasDocumentoSpesa = hasDocumentoSpesa;
  }

}
