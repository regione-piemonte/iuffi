package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class TipoOperazioneDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID                = 4601339650061084885L;

  private long              idLivello;
  private String            codice;
  private String            descrizione;
  private BigDecimal        costoTotale                     = BigDecimal.ZERO;
  private BigDecimal        costoTotaleUltimoApprovato      = BigDecimal.ZERO;
  private BigDecimal        risorsePubbliche                = BigDecimal.ZERO;
  private BigDecimal        risorsePubblicheUltimoApprovato = BigDecimal.ZERO;
  private BigDecimal        risorsePrivate                  = BigDecimal.ZERO;
  private BigDecimal        risorsePrivateUltimoApprovato   = BigDecimal.ZERO;
  private Long              idAmbitoTematico;
  private String            descrizioneAmbitoTematico;
  private String            flagPropostaGal;
  private long              idLivFocusAreaLeader;
  private boolean           isRed;

  public BigDecimal getCostoTotaleDelta()
  {
    if (costoTotale == null)
      costoTotale = BigDecimal.ZERO;
    if (costoTotaleUltimoApprovato == null)
      costoTotaleUltimoApprovato = BigDecimal.ZERO;
    return costoTotale.subtract(costoTotaleUltimoApprovato);
  }

  public BigDecimal getRisorsePubblicheDelta()
  {
    if (risorsePubbliche == null)
      risorsePubbliche = BigDecimal.ZERO;
    if (risorsePubblicheUltimoApprovato == null)
      risorsePubblicheUltimoApprovato = BigDecimal.ZERO;
    return risorsePubbliche.subtract(risorsePubblicheUltimoApprovato);
  }

  public BigDecimal getRisorsePrivateDelta()
  {
    if (risorsePrivate == null)
      risorsePrivate = BigDecimal.ZERO;
    if (risorsePrivateUltimoApprovato == null)
      risorsePrivateUltimoApprovato = BigDecimal.ZERO;
    return risorsePrivate.subtract(risorsePrivateUltimoApprovato);
  }

  public BigDecimal getCostoTotaleUltimoApprovato()
  {
    return costoTotaleUltimoApprovato;
  }

  public void setCostoTotaleUltimoApprovato(
      BigDecimal costoTotaleUltimoApprovato)
  {
    this.costoTotaleUltimoApprovato = costoTotaleUltimoApprovato;
  }

  public BigDecimal getRisorsePubblicheUltimoApprovato()
  {
    return risorsePubblicheUltimoApprovato;
  }

  public void setRisorsePubblicheUltimoApprovato(
      BigDecimal risorsePubblicheUltimoApprovato)
  {
    this.risorsePubblicheUltimoApprovato = risorsePubblicheUltimoApprovato;
  }

  public BigDecimal getRisorsePrivateUltimoApprovato()
  {
    return risorsePrivateUltimoApprovato;
  }

  public void setRisorsePrivateUltimoApprovato(
      BigDecimal risorsePrivateUltimoApprovato)
  {
    this.risorsePrivateUltimoApprovato = risorsePrivateUltimoApprovato;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public BigDecimal getCostoTotale()
  {
    return costoTotale;
  }

  public void setCostoTotale(BigDecimal costoTotale)
  {
    this.costoTotale = costoTotale;
  }

  public BigDecimal getRisorsePubbliche()
  {
    return risorsePubbliche;
  }

  public void setRisorsePubbliche(BigDecimal risorsePubbliche)
  {
    this.risorsePubbliche = risorsePubbliche;
  }

  public BigDecimal getRisorsePrivate()
  {
    return risorsePrivate;
  }

  public void setRisorsePrivate(BigDecimal risorsePrivate)
  {
    this.risorsePrivate = risorsePrivate;
  }

  public Long getIdAmbitoTematico()
  {
    return idAmbitoTematico;
  }

  public void setIdAmbitoTematico(Long idAmbitoTematico)
  {
    this.idAmbitoTematico = idAmbitoTematico;
  }

  public String getDescrizioneAmbitoTematico()
  {
    return descrizioneAmbitoTematico;
  }

  public void setDescrizioneAmbitoTematico(String descrizioneAmbitoTematico)
  {
    this.descrizioneAmbitoTematico = descrizioneAmbitoTematico;
  }

  public boolean isPropostaGal()
  {
    return IuffiConstants.FLAGS.SI.equals(flagPropostaGal);
  }

  public String getFlagPropostaGal()
  {
    return flagPropostaGal;
  }

  public void setFlagPropostaGal(String flagPropostaGal)
  {
    this.flagPropostaGal = flagPropostaGal;
  }

  public long getIdLivFocusAreaLeader()
  {
    return idLivFocusAreaLeader;
  }

  public void setIdLivFocusAreaLeader(long idLivFocusAreaLeader)
  {
    this.idLivFocusAreaLeader = idLivFocusAreaLeader;
  }

  public boolean isRed()
  {
    return isRed;
  }

  public void setRed(boolean isRed)
  {
    this.isRed = isRed;
  }

}
