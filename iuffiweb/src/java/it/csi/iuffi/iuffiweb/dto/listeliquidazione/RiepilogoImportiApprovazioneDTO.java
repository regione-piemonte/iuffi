package it.csi.iuffi.iuffiweb.dto.listeliquidazione;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RiepilogoImportiApprovazioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1692487726416094232L;
  protected long            idLivello;
  protected String          codiceLivello;
  protected String          causalePagamento;
  protected long            numPagamenti;
  protected BigDecimal      importoLiquidato;

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public String getCausalePagamento()
  {
    return causalePagamento;
  }

  public void setCausalePagamento(String causalePagamento)
  {
    this.causalePagamento = causalePagamento;
  }

  public long getNumPagamenti()
  {
    return numPagamenti;
  }

  public void setNumPagamenti(long numPagamenti)
  {
    this.numPagamenti = numPagamenti;
  }

  public BigDecimal getImportoLiquidato()
  {
    return importoLiquidato;
  }

  public void setImportoLiquidato(BigDecimal importoLiquidato)
  {
    this.importoLiquidato = importoLiquidato;
  }
}
