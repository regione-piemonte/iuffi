package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RigaRendicontazioneSpese implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 515753983327456765L;
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
  private BigDecimal        importoSpesa;
  private BigDecimal        speseDaRendicontare;
  private BigDecimal        importoNonRiconosciutoPrec;
  private BigDecimal        importoDisponibilePrec;
  private BigDecimal        contributoRichiesto;
  private String            flagInterventoCompletato;
  private String            note;
  private long              idTipoLocalizzazione;
  private String            usaDocumentiSpesa;
  private String            hasDocumentoSpesa;
  private String            codiceLivello;

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

  public BigDecimal getImportoSpesa()
  {
    return importoSpesa;
  }

  public void setImportoSpesa(BigDecimal importoSpesa)
  {
    this.importoSpesa = importoSpesa;
  }

  public BigDecimal getSpeseDaRendicontare()
  {
    return speseDaRendicontare;
  }

  public void setSpeseDaRendicontare(BigDecimal speseDaRendicontare)
  {
    this.speseDaRendicontare = speseDaRendicontare;
  }

  public BigDecimal getContributoRichiesto()
  {
    return contributoRichiesto;
  }

  public void setContributoRichiesto(BigDecimal contributoRichiesto)
  {
    this.contributoRichiesto = contributoRichiesto;
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

  public BigDecimal getImportoNonRiconosciutoPrec()
  {
    return importoNonRiconosciutoPrec;
  }

  public void setImportoNonRiconosciutoPrec(
      BigDecimal importoNonRiconosciutoPrec)
  {
    this.importoNonRiconosciutoPrec = importoNonRiconosciutoPrec;
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

  public BigDecimal getImportoDisponibilePrec()
  {
    return importoDisponibilePrec;
  }

  public void setImportoDisponibilePrec(BigDecimal importoDisponibilePrec)
  {
    this.importoDisponibilePrec = importoDisponibilePrec;
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
