package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class PianoFinanziarioDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long    serialVersionUID = 4601339650061084885L;

  private BigDecimal           risPubblicheComplessive;
  private List<SottomisuraDTO> sottomisure;
  private Long                 idPianoFinanziario;

  public BigDecimal getTotCostoTotaleDelta()
  {
    return getTotCostoTotale().subtract(getTotCostoTotaleUltimoApprovato());
  }

  public BigDecimal getTotCostoTotale()
  {

    BigDecimal costoTotale = BigDecimal.ZERO;
    if (sottomisure != null)
      for (SottomisuraDTO s : sottomisure)
      {
        costoTotale = costoTotale.add(s.getCostoTotale());
      }
    return costoTotale;
  }

  public BigDecimal getTotCostoTotaleUltimoApprovato()
  {

    BigDecimal costoTotaleUltimoApprovato = BigDecimal.ZERO;
    if (sottomisure != null)
      for (SottomisuraDTO s : sottomisure)
      {
        costoTotaleUltimoApprovato = costoTotaleUltimoApprovato
            .add(s.getCostoTotaleUltimoApprovato());
      }
    return costoTotaleUltimoApprovato;
  }

  public BigDecimal getTotRisorsePubblicheDelta()
  {
    return getTotRisorsePubbliche()
        .subtract(getTotRisorsePubblicheUltimoApprovato());
  }

  public BigDecimal getTotRisorsePubbliche()
  {

    BigDecimal risorse = BigDecimal.ZERO;
    if (sottomisure != null)
      for (SottomisuraDTO s : sottomisure)
      {
        risorse = risorse.add(s.getRisorsePubbliche());
      }
    return risorse;
  }

  public BigDecimal getTotRisorsePubblicheUltimoApprovato()
  {

    BigDecimal risorse = BigDecimal.ZERO;
    if (sottomisure != null)
      for (SottomisuraDTO s : sottomisure)
      {
        risorse = risorse.add(s.getRisorsePubblicheUltimoApprovato());
      }
    return risorse;
  }

  public BigDecimal getTotRisorsePrivateDelta()
  {
    return getTotRisorsePrivate()
        .subtract(getTotRisorsePrivateUltimoApprovato());
  }

  public BigDecimal getTotRisorsePrivate()
  {

    BigDecimal risorse = BigDecimal.ZERO;
    if (sottomisure != null)
      for (SottomisuraDTO s : sottomisure)
      {
        risorse = risorse.add(s.getRisorsePrivate());
      }
    return risorse;
  }

  public BigDecimal getTotRisorsePrivateUltimoApprovato()
  {

    BigDecimal risorse = BigDecimal.ZERO;
    if (sottomisure != null)
      for (SottomisuraDTO s : sottomisure)
      {
        risorse = risorse.add(s.getRisorsePrivateUltimoApprovato());
      }
    return risorse;
  }

  public BigDecimal getRisPubblicheComplessive()
  {
    return (risPubblicheComplessive == null) ? new BigDecimal("0")
        : risPubblicheComplessive;
  }

  public void setRisPubblicheComplessive(BigDecimal risPubblicheComplessive)
  {
    this.risPubblicheComplessive = risPubblicheComplessive;
  }

  public BigDecimal getRisPubblicheAllocate()
  {
    return getTotRisorsePubbliche();
  }

  public BigDecimal getRisPubblicheDaAllocare()
  {
    BigDecimal ret = this.risPubblicheComplessive
        .subtract(getRisPubblicheAllocate());
    if (ret.signum() == -1)
      return BigDecimal.ZERO;
    return ret;
  }

  public List<SottomisuraDTO> getSottomisure()
  {
    return sottomisure;
  }

  public void setSottomisure(List<SottomisuraDTO> sottomisure)
  {
    this.sottomisure = sottomisure;
  }

  public Long getIdPianoFinanziario()
  {
    return idPianoFinanziario;
  }

  public void setIdPianoFinanziario(Long idPianoFinanziario)
  {
    this.idPianoFinanziario = idPianoFinanziario;
  }

}
