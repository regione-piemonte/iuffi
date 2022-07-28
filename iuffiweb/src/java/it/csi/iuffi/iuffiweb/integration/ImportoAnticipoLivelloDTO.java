package it.csi.iuffi.iuffiweb.integration;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class ImportoAnticipoLivelloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5557015249349002707L;
  private long              idAnticipo;
  private long              idLivello;
  private BigDecimal        importoAnticipo;

  public long getIdAnticipo()
  {
    return idAnticipo;
  }

  public void setIdAnticipo(long idAnticipo)
  {
    this.idAnticipo = idAnticipo;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public BigDecimal getImportoAnticipo()
  {
    return importoAnticipo;
  }

  public void setImportoAnticipo(BigDecimal importoAnticipo)
  {
    this.importoAnticipo = importoAnticipo;
  }
}
