package it.csi.iuffi.iuffiweb.dto.procedimentooggetto;

import it.csi.iuffi.iuffiweb.dto.ElencoCduDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class OggettoIconaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1748814080412808194L;
  private long              idOggettoIcona;
  private int               idOggetto;
  private int               ordine;
  private Long              extIdTipoDocumento;
  private String            flagFirmaGrafometrica;
  private ElencoCduDTO      cdu;
  private String            flagStampaOggettoAperto;
  private IconaDTO          icona;

  public long getIdOggettoIcona()
  {
    return idOggettoIcona;
  }

  public void setIdOggettoIcona(long idOggettoIcona)
  {
    this.idOggettoIcona = idOggettoIcona;
  }

  public int getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(int idOggetto)
  {
    this.idOggetto = idOggetto;
  }

  public int getOrdine()
  {
    return ordine;
  }

  public void setOrdine(int ordine)
  {
    this.ordine = ordine;
  }

  public Long getExtIdTipoDocumento()
  {
    return extIdTipoDocumento;
  }

  public void setExtIdTipoDocumento(Long extIdTipoDocumento)
  {
    this.extIdTipoDocumento = extIdTipoDocumento;
  }

  public String getFlagFirmaGrafometrica()
  {
    return flagFirmaGrafometrica;
  }

  public void setFlagFirmaGrafometrica(String flagFirmaGrafometrica)
  {
    this.flagFirmaGrafometrica = flagFirmaGrafometrica;
  }

  public ElencoCduDTO getCdu()
  {
    return cdu;
  }

  public void setCdu(ElencoCduDTO cdu)
  {
    this.cdu = cdu;
  }

  public String getFlagStampaOggettoAperto()
  {
    return flagStampaOggettoAperto;
  }

  public void setFlagStampaOggettoAperto(String flagStampaOggettoAperto)
  {
    this.flagStampaOggettoAperto = flagStampaOggettoAperto;
  }

  public IconaDTO getIcona()
  {
    return icona;
  }

  public void setIcona(IconaDTO icona)
  {
    this.icona = icona;
  }
}
