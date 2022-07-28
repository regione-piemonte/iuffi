package it.csi.iuffi.iuffiweb.integration;

import java.math.BigDecimal;

public class RigaAnticipoLivello extends ImportoAnticipoLivelloDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = -1821326745546025296L;
  private String            codiceLivello;
  private String            flagAnticipo;
  private BigDecimal        importoInvestimento;
  private BigDecimal        importoAmmesso;
  private BigDecimal        importoContributo;

  public BigDecimal getImportoInvestimento()
  {
    return importoInvestimento;
  }

  public void setImportoInvestimento(BigDecimal importoInvestimento)
  {
    this.importoInvestimento = importoInvestimento;
  }

  public BigDecimal getImportoAmmesso()
  {
    return importoAmmesso;
  }

  public void setImportoAmmesso(BigDecimal importoAmmesso)
  {
    this.importoAmmesso = importoAmmesso;
  }

  public BigDecimal getImportoContributo()
  {
    return importoContributo;
  }

  public void setImportoContributo(BigDecimal importoContributo)
  {
    this.importoContributo = importoContributo;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public String getFlagAnticipo()
  {
    return flagAnticipo;
  }

  public void setFlagAnticipo(String flagAnticipo)
  {
    this.flagAnticipo = flagAnticipo;
  }
}
