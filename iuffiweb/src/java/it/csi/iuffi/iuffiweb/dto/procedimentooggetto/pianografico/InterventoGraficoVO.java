package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianografico;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class InterventoGraficoVO implements ILoggable
{

  private static final long serialVersionUID = -8064889027606525236L;

  private long              idInterventoGrafico;
  private String            codiceIntervento;
  private String            descrizioneIntervento;
  private BigDecimal        supIntervento;

  public String getSupInterventoStr()
  {
    return IuffiUtils.FORMAT.formatDecimal4(supIntervento);
  }

  public BigDecimal getSupIntervento()
  {
    return supIntervento;
  }

  public void setSupIntervento(BigDecimal supIntervento)
  {
    this.supIntervento = supIntervento;
  }

  public long getIdInterventoGrafico()
  {
    return idInterventoGrafico;
  }

  public void setIdInterventoGrafico(long idInterventoGrafico)
  {
    this.idInterventoGrafico = idInterventoGrafico;
  }

  public String getCodiceIntervento()
  {
    return codiceIntervento;
  }

  public void setCodiceIntervento(String codiceIntervento)
  {
    this.codiceIntervento = codiceIntervento;
  }

  public String getDescrizioneIntervento()
  {
    return descrizioneIntervento;
  }

  public void setDescrizioneIntervento(String descrizioneIntervento)
  {
    this.descrizioneIntervento = descrizioneIntervento;
  }

}
