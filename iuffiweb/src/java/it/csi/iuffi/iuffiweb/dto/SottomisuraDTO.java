package it.csi.iuffi.iuffiweb.dto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SottomisuraDTO implements ILoggable
{
  private static final String     SENZA_AMBITO     = "Senza ambito";

  /** serialVersionUID */
  private static final long       serialVersionUID = 4601339650061084885L;

  private long                    idLivello;
  private String                  codice;
  private String                  descrizione;
  private List<TipoOperazioneDTO> tipiOperazione;

  public boolean isRed()
  {
    if (tipiOperazione != null)
      for (TipoOperazioneDTO s : tipiOperazione)
      {
        if (s.isRed())
          return true;
      }
    return false;
  }

  public BigDecimal getCostoTotaleDelta()
  {
    return getCostoTotale().subtract(getCostoTotaleUltimoApprovato());
  }

  public BigDecimal getCostoTotale()
  {

    BigDecimal costoTotale = BigDecimal.ZERO;
    if (tipiOperazione != null)
      for (TipoOperazioneDTO s : tipiOperazione)
      {
        costoTotale = costoTotale.add(s.getCostoTotale());
      }
    return costoTotale;
  }

  public BigDecimal getCostoTotaleUltimoApprovato()
  {

    BigDecimal costoTotaleUltimoApprovato = BigDecimal.ZERO;
    if (tipiOperazione != null)
      for (TipoOperazioneDTO s : tipiOperazione)
      {
        costoTotaleUltimoApprovato = costoTotaleUltimoApprovato
            .add(s.getCostoTotaleUltimoApprovato());
      }
    return costoTotaleUltimoApprovato;
  }

  public BigDecimal getRisorsePubblicheDelta()
  {
    return getRisorsePubbliche().subtract(getRisorsePubblicheUltimoApprovato());
  }

  public BigDecimal getRisorsePubbliche()
  {

    BigDecimal risorse = BigDecimal.ZERO;
    if (tipiOperazione != null)
      for (TipoOperazioneDTO s : tipiOperazione)
      {
        risorse = risorse.add(s.getRisorsePubbliche());
      }
    return risorse;
  }

  public BigDecimal getRisorsePubblicheUltimoApprovato()
  {

    BigDecimal risorse = BigDecimal.ZERO;
    if (tipiOperazione != null)
      for (TipoOperazioneDTO s : tipiOperazione)
      {
        risorse = risorse.add(s.getRisorsePubblicheUltimoApprovato());
      }
    return risorse;
  }

  public BigDecimal getRisorsePrivateDelta()
  {
    return getRisorsePrivate().subtract(getRisorsePrivateUltimoApprovato());
  }

  public BigDecimal getRisorsePrivate()
  {

    BigDecimal risorse = BigDecimal.ZERO;
    if (tipiOperazione != null)
      for (TipoOperazioneDTO s : tipiOperazione)
      {
        risorse = risorse.add(s.getRisorsePrivate());
      }
    return risorse;
  }

  public BigDecimal getRisorsePrivateUltimoApprovato()
  {

    BigDecimal risorse = BigDecimal.ZERO;
    if (tipiOperazione != null)
      for (TipoOperazioneDTO s : tipiOperazione)
      {
        risorse = risorse.add(s.getRisorsePrivateUltimoApprovato());
      }
    return risorse;
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

  public void setTipiOperazione(List<TipoOperazioneDTO> tipiOperazione)
  {
    this.tipiOperazione = tipiOperazione;
  }

  public List<TipoOperazioneDTO> getTipiOperazione()
  {
    return tipiOperazione;
  }

  public String getDescrizioneAmbitoTematico()
  {
    if (tipiOperazione != null && !tipiOperazione.isEmpty())
    {
      return IuffiUtils.STRING.nvl(
          tipiOperazione.get(0).getDescrizioneAmbitoTematico(), SENZA_AMBITO);
    }
    return SENZA_AMBITO;
  }

}
