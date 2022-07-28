package it.csi.iuffi.iuffiweb.dto.procedimentooggetto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AzioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5916988902454815748L;
  private String            codiceAzione;
  private String            labelAzione;
  private String            codiceCdu;
  private String            extCodMacroCdu;
  private String            tipoAzione;
  private Integer           priorita;
  private int               ordine;

  public String getCodiceAzione()
  {
    return codiceAzione;
  }

  public void setCodiceAzione(String codiceAzione)
  {
    this.codiceAzione = codiceAzione;
  }

  public String getLabelAzione()
  {
    return labelAzione;
  }

  public void setLabelAzione(String labelAzione)
  {
    this.labelAzione = labelAzione;
  }

  public String getCodiceCdu()
  {
    return codiceCdu;
  }

  public void setCodiceCdu(String codiceCdu)
  {
    this.codiceCdu = codiceCdu;
  }

  public String getExtCodMacroCdu()
  {
    return extCodMacroCdu;
  }

  public void setExtCodMacroCdu(String extCodMacroCdu)
  {
    this.extCodMacroCdu = extCodMacroCdu;
  }

  public String getTipoAzione()
  {
    return tipoAzione;
  }

  public void setTipoAzione(String tipoAzione)
  {
    this.tipoAzione = tipoAzione;
  }

  public Integer getPriorita()
  {
    return priorita;
  }

  public void setPriorita(Integer priorita)
  {
    this.priorita = priorita;
  }

  public int getOrdine()
  {
    return ordine;
  }

  public void setOrdine(int ordine)
  {
    this.ordine = ordine;
  }
}
