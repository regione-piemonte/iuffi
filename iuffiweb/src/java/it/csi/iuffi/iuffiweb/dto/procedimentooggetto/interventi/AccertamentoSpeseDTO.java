package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AccertamentoSpeseDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 7423329216622772000L;
  protected String          flagInterventoCompletato;
  protected long            idAccertamentoSpese;
  protected long            idIntervento;
  protected long            idProcedimentoOggetto;
  protected BigDecimal      importoAccertato;
  protected BigDecimal      importoCalcoloContributo;
  protected BigDecimal      importoDisponibile;
  protected BigDecimal      importoNonRiconosciuto;
  protected BigDecimal      contributoCalcolato;
  protected String          note;
  protected boolean         usaDocumentiSpesa;

  public String getFlagInterventoCompletato()
  {
    return flagInterventoCompletato;
  }

  public void setFlagInterventoCompletato(String flagInterventoCompletato)
  {
    this.flagInterventoCompletato = flagInterventoCompletato;
  }

  public long getIdAccertamentoSpese()
  {
    return idAccertamentoSpese;
  }

  public void setIdAccertamentoSpese(long idAccertamentoSpese)
  {
    this.idAccertamentoSpese = idAccertamentoSpese;
  }

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public BigDecimal getImportoAccertato()
  {
    return importoAccertato;
  }

  public void setImportoAccertato(BigDecimal importoAccertato)
  {
    this.importoAccertato = importoAccertato;
  }

  public BigDecimal getImportoCalcoloContributo()
  {
    return importoCalcoloContributo;
  }

  public void setImportoCalcoloContributo(BigDecimal importoCalcoloContributo)
  {
    this.importoCalcoloContributo = importoCalcoloContributo;
  }

  public BigDecimal getImportoDisponibile()
  {
    return importoDisponibile;
  }

  public void setImportoDisponibile(BigDecimal importoDisponibile)
  {
    this.importoDisponibile = importoDisponibile;
  }

  public BigDecimal getImportoNonRiconosciuto()
  {
    return importoNonRiconosciuto;
  }

  public void setImportoNonRiconosciuto(BigDecimal importoNonRiconosciuto)
  {
    this.importoNonRiconosciuto = importoNonRiconosciuto;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public BigDecimal getContributoCalcolato()
  {
    return contributoCalcolato;
  }

  public void setContributoCalcolato(BigDecimal contributoCalcolato)
  {
    this.contributoCalcolato = contributoCalcolato;
  }

  public boolean isUsaDocumentiSpesa()
  {
    return usaDocumentiSpesa;
  }

  public void setUsaDocumentiSpesa(boolean usaDocumentiSpesa)
  {
    this.usaDocumentiSpesa = usaDocumentiSpesa;
  }
}
