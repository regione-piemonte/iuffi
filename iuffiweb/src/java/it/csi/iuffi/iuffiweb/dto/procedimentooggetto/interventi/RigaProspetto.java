package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RigaProspetto implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5559346638673433120L;
  protected String          codice;
  protected String          descrizione;
  protected long            idProcedimento;
  protected long            idProcedimentoOggetto;
  protected Date            dataPresentazione;
  protected BigDecimal      contribRichiesto;
  protected BigDecimal      importoSanzioni;
  protected BigDecimal      inLiquidazione;

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public long getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(long idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }

  public Date getDataPresentazione()
  {
    return dataPresentazione;
  }

  public void setDataPresentazione(Date dataPresentazione)
  {
    this.dataPresentazione = dataPresentazione;
  }

  public BigDecimal getContribRichiesto()
  {
    return contribRichiesto;
  }

  public void setContribRichiesto(BigDecimal contribRichiesto)
  {
    this.contribRichiesto = contribRichiesto;
  }

  public BigDecimal getImportoSanzioni()
  {
    return importoSanzioni;
  }

  public void setImportoSanzioni(BigDecimal importoSanzioni)
  {
    this.importoSanzioni = importoSanzioni;
  }

  public BigDecimal getInLiquidazione()
  {
    return inLiquidazione;
  }

  public void setInLiquidazione(BigDecimal inLiquidazione)
  {
    this.inLiquidazione = inLiquidazione;
  }

}
