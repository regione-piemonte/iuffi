package it.csi.iuffi.iuffiweb.dto.listeliquidazione;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RisorseImportiOperazioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -7612811632413079994L;
  protected long            idRisorseLivelloBando;
  protected String          codiceOperazione;
  protected BigDecimal      risorseAttivate;
  protected BigDecimal      importoInLiquidazione;
  protected long            numeroPagamentiLista;
  protected BigDecimal      importoDaLiquidare;
  protected BigDecimal      importoRimanente;
  protected String          raggruppamento;

  public String getCodiceOperazione()
  {
    return codiceOperazione;
  }

  public void setCodiceOperazione(String codiceOperazione)
  {
    this.codiceOperazione = codiceOperazione;
  }

  public BigDecimal getRisorseAttivate()
  {
    return risorseAttivate;
  }

  public void setRisorseAttivate(BigDecimal risorseAttivate)
  {
    this.risorseAttivate = risorseAttivate;
  }

  public BigDecimal getImportoInLiquidazione()
  {
    return importoInLiquidazione;
  }

  public void setImportoInLiquidazione(BigDecimal importoInLiquidazione)
  {
    this.importoInLiquidazione = importoInLiquidazione;
  }

  public long getNumeroPagamentiLista()
  {
    return numeroPagamentiLista;
  }

  public void setNumeroPagamentiLista(long numeroPagamentiLista)
  {
    this.numeroPagamentiLista = numeroPagamentiLista;
  }

  public BigDecimal getImportoDaLiquidare()
  {
    return importoDaLiquidare;
  }

  public void setImportoDaLiquidare(BigDecimal importoDaLiquidare)
  {
    this.importoDaLiquidare = importoDaLiquidare;
  }

  public BigDecimal getImportoRimanente()
  {
    return importoRimanente;
  }

  public void setImportoRimanente(BigDecimal importoRimanente)
  {
    this.importoRimanente = importoRimanente;
  }

  public void calcolaImportoRimanente()
  {
    importoRimanente = risorseAttivate.subtract(importoDaLiquidare)
        .subtract(importoInLiquidazione);
  }

  public long getIdRisorseLivelloBando()
  {
    return idRisorseLivelloBando;
  }

  public void setIdRisorseLivelloBando(long idRisorseLivelloBando)
  {
    this.idRisorseLivelloBando = idRisorseLivelloBando;
  }

  public String getRaggruppamento()
  {
    return raggruppamento;
  }

  public void setRaggruppamento(String raggruppamento)
  {
    this.raggruppamento = raggruppamento;
  }

}
