package it.csi.iuffi.iuffiweb.dto.licenzapesca;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ImportoLicenzaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long idTipoLicenza;
  private String descrTipoLicenza;
  private String descrTassaTipoLicenza;
  private String durata;
  private BigDecimal importo;
  
  
  public String getDescrizioneEstesa() {
    String tmp = idTipoLicenza==3 ? "tassa": "tassa + sopratassa";
    return descrTipoLicenza+" - "+tmp+" ( totale "+IuffiUtils.FORMAT.formatCurrency(importo)+" € / durata "+durata+" gg )";
  }
  
 
  public long getIdTipoLicenza()
  {
    return idTipoLicenza;
  }


  public void setIdTipoLicenza(long idTipoLicenza)
  {
    this.idTipoLicenza = idTipoLicenza;
  }


  public String getDescrTipoLicenza()
  {
    return descrTipoLicenza;
  }
  public void setDescrTipoLicenza(String descrTipoLicenza)
  {
    this.descrTipoLicenza = descrTipoLicenza;
  }
 
  public String getDurata()
  {
    return durata;
  }
  public void setDurata(String durata)
  {
    this.durata = durata;
  }
  public BigDecimal getImporto()
  {
    return importo;
  }
  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }


  public String getDescrTassaTipoLicenza()
  {
    return descrTassaTipoLicenza;
  }


  public void setDescrTassaTipoLicenza(String descrTassaTipoLicenza)
  {
    this.descrTassaTipoLicenza = descrTassaTipoLicenza;
  }
  
  
  
}
