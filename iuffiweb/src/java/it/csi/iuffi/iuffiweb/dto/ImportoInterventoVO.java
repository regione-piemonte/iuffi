package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ImportoInterventoVO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -2690992403448119930L;

  private long              idIntervento;
  private BigDecimal        importo;
  private BigDecimal        importoDaRendicontare;
  private long              idDocumentoSpesa;
  private long              idTipoDocumentoSpesa;
  private String            descrTipoDocumento;
  private String            descrIntervento;

  public long getIdIntervento()
  {
    return idIntervento;
  }

  public void setIdIntervento(long idIntervento)
  {
    this.idIntervento = idIntervento;
  }

  public BigDecimal getImporto()
  {
    return importo;
  }

  public String getImportoStr()
  {
    return IuffiUtils.FORMAT.formatGenericNumber(importo, 2, false);
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }

  public long getIdDocumentoSpesa()
  {
    return idDocumentoSpesa;
  }

  public void setIdDocumentoSpesa(long idDocumentoSpesa)
  {
    this.idDocumentoSpesa = idDocumentoSpesa;
  }

  public long getIdTipoDocumentoSpesa()
  {
    return idTipoDocumentoSpesa;
  }

  public void setIdTipoDocumentoSpesa(long idTipoDocumentoSpesa)
  {
    this.idTipoDocumentoSpesa = idTipoDocumentoSpesa;
  }

  public String getDescrTipoDocumento()
  {
    return descrTipoDocumento;
  }

  public void setDescrTipoDocumento(String descrTipoDocumento)
  {
    this.descrTipoDocumento = descrTipoDocumento;
  }

  public String getDescrIntervento()
  {
    return descrIntervento;
  }

  public void setDescrIntervento(String descrIntervento)
  {
    this.descrIntervento = descrIntervento;
  }

  public String getIdUnivoco()
  {
    return this.idIntervento + "_" + this.idDocumentoSpesa;
  }

  public BigDecimal getImportoDaRendicontare()
  {
    return importoDaRendicontare;
  }

  public void setImportoDaRendicontare(BigDecimal importoDaRendicontare)
  {
    this.importoDaRendicontare = importoDaRendicontare;
  }

}
