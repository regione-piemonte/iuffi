package it.csi.iuffi.iuffiweb.dto.estrazionecampione;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DettaglioImportoDTO implements ILoggable
{

  private static final long serialVersionUID = 5918223009553272325L;

  private long              idLivello;
  private long              idStatoEstrazione;
  private long              idTipoEstrazione;
  private String            codiceLivello;
  private String            decodificaImporto;
  private String            descrTipoEstrazione;
  private String            descrStatoEstrazione;
  private String            tipoImporto;
  private BigDecimal        importo;
  private BigDecimal        importoRichiesto;
  private BigDecimal        importoRichiestoParteCasual;
  private BigDecimal        importoTotaleRichiestoComplessivo;

  public String getTipoImporto()
  {
    return tipoImporto;
  }

  public void setTipoImporto(String tipoImporto)
  {
    this.tipoImporto = tipoImporto;
  }

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

  public String getDecodificaImporto()
  {
    return decodificaImporto;
  }

  public void setDecodificaImporto(String decodificaImporto)
  {
    this.decodificaImporto = decodificaImporto;
  }

  public BigDecimal getImporto()
  {
    return importo;
  }

  public String getImportoStr()
  {
    if (importo == null)
    {
      return IuffiUtils.FORMAT.formatCurrency(BigDecimal.ZERO);
    }
    return IuffiUtils.FORMAT.formatCurrency(importo);
  }

  public String getImportoRichiestoStr()
  {
    if (importoRichiesto == null)
    {
      return IuffiUtils.FORMAT.formatCurrency(BigDecimal.ZERO);
    }
    return IuffiUtils.FORMAT.formatCurrency(importoRichiesto);
  }

  public String getImportoRichiestoParteCasualStr()
  {
    if (importoRichiestoParteCasual == null)
    {
      return IuffiUtils.FORMAT.formatCurrency(BigDecimal.ZERO);
    }
    return IuffiUtils.FORMAT.formatCurrency(importoRichiestoParteCasual);
  }

  public String getAnalisiRischioStr()
  {
    return IuffiUtils.FORMAT
        .formatCurrency(IuffiUtils.NUMBERS.nvl(importoRichiesto)
            .subtract(IuffiUtils.NUMBERS.nvl(importoRichiestoParteCasual)));
  }

  public BigDecimal getAnalisiRischio()
  {
    return IuffiUtils.NUMBERS.nvl(importoRichiesto)
        .subtract(IuffiUtils.NUMBERS.nvl(importoRichiestoParteCasual));
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }

  public BigDecimal getImportoRichiesto()
  {
    return importoRichiesto;
  }

  public void setImportoRichiesto(BigDecimal importoRichiesto)
  {
    this.importoRichiesto = importoRichiesto;
  }

  public BigDecimal getImportoRichiestoParteCasual()
  {
    return importoRichiestoParteCasual;
  }

  public void setImportoRichiestoParteCasual(
      BigDecimal importoRichiestoParteCasual)
  {
    this.importoRichiestoParteCasual = importoRichiestoParteCasual;
  }

  public BigDecimal getImportoTotaleRichiestoComplessivo()
  {
    return importoTotaleRichiestoComplessivo;
  }

  public void setImportoTotaleRichiestoComplessivo(
      BigDecimal importoTotaleRichiestoComplessivo)
  {
    this.importoTotaleRichiestoComplessivo = importoTotaleRichiestoComplessivo;
  }

  public String getPercImportoRichiestoStr()
  {
    BigDecimal tmp = (IuffiUtils.NUMBERS.nvl(importoRichiesto)
        .multiply(new BigDecimal(100)))
            .divide(
                IuffiUtils.NUMBERS.initNumberNvlZero(
                    importoTotaleRichiestoComplessivo, BigDecimal.ONE),
                2, BigDecimal.ROUND_HALF_UP);
    if (tmp == null)
    {
      return IuffiUtils.FORMAT.formatCurrency(BigDecimal.ZERO);
    }
    return IuffiUtils.FORMAT.formatCurrency(tmp);
  }

  public String getPercTotaleImportiEstrattiCasualeStr()
  {
    BigDecimal tmp = (IuffiUtils.NUMBERS.nvl(importoRichiestoParteCasual)
        .multiply(new BigDecimal(100)))
            .divide(IuffiUtils.NUMBERS.initNumberNvlZero(importoRichiesto,
                BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
    if (tmp == null)
    {
      return IuffiUtils.FORMAT.formatCurrency(BigDecimal.ZERO);
    }
    return IuffiUtils.FORMAT.formatCurrency(tmp);
  }

  public String getPercTotaleAnalisiDelRischioStr()
  {
    BigDecimal rischio = (IuffiUtils.NUMBERS.nvl(importoRichiestoParteCasual)
        .multiply(new BigDecimal(100))).divide(
            IuffiUtils.NUMBERS.nvl(importoRichiesto), 2,
            BigDecimal.ROUND_HALF_UP);
    BigDecimal tmp = (new BigDecimal("100")).subtract(
        IuffiUtils.NUMBERS.initNumberNvlZero(rischio, BigDecimal.ONE));
    if (tmp == null)
    {
      return IuffiUtils.FORMAT.formatCurrency(BigDecimal.ZERO);
    }
    return IuffiUtils.FORMAT.formatCurrency(tmp);
  }

  public String getDescrTipoEstrazione()
  {
    return descrTipoEstrazione;
  }

  public void setDescrTipoEstrazione(String descrTipoEstrazione)
  {
    this.descrTipoEstrazione = descrTipoEstrazione;
  }

  public String getDescrStatoEstrazione()
  {
    return descrStatoEstrazione;
  }

  public void setDescrStatoEstrazione(String descrStatoEstrazione)
  {
    this.descrStatoEstrazione = descrStatoEstrazione;
  }

  public long getIdStatoEstrazione()
  {
    return idStatoEstrazione;
  }

  public void setIdStatoEstrazione(long idStatoEstrazione)
  {
    this.idStatoEstrazione = idStatoEstrazione;
  }

  public long getIdTipoEstrazione()
  {
    return idTipoEstrazione;
  }

  public void setIdTipoEstrazione(long idTipoEstrazione)
  {
    this.idTipoEstrazione = idTipoEstrazione;
  }

}
