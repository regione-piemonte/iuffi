package it.csi.iuffi.iuffiweb.dto.procedimentooggetto;

import java.util.List;

public class QuadroOggettoDTO extends QuadroDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5916988902454815748L;
  private long              idQuadroOggetto;
  private long              idOggetto;
  private long              idBandoOggetto;
  private String            flagObbligatorio;
  private int               ordine;
  private List<AzioneDTO>   azioni;

  public long getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(long idOggetto)
  {
    this.idOggetto = idOggetto;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public String getFlagObbligatorio()
  {
    return flagObbligatorio;
  }

  public void setFlagObbligatorio(String flagObbligatorio)
  {
    this.flagObbligatorio = flagObbligatorio;
  }

  public int getOrdine()
  {
    return ordine;
  }

  public void setOrdine(int ordine)
  {
    this.ordine = ordine;
  }

  public List<AzioneDTO> getAzioni()
  {
    return azioni;
  }

  public void setAzioni(List<AzioneDTO> azioni)
  {
    this.azioni = azioni;
  }

  public long getIdQuadroOggetto()
  {
    return idQuadroOggetto;
  }

  public void setIdQuadroOggetto(long idQuadroOggetto)
  {
    this.idQuadroOggetto = idQuadroOggetto;
  }

  public boolean hasAzioneConCU(String cu)
  {
    if (azioni != null && cu != null)
    {
      for (AzioneDTO azione : azioni)
      {
        if (cu.equalsIgnoreCase(azione.getCodiceCdu()))
        {
          return true;
        }
      }
    }
    return false;
  }

  public AzioneDTO azioneConCU(String cu)
  {
    if (azioni != null && cu != null)
    {
      for (AzioneDTO azione : azioni)
      {
        if (cu.equalsIgnoreCase(azione.getCodiceCdu()))
        {
          return azione;
        }
      }
    }
    return null;
  }

  public AzioneDTO findAzioneByCodice(String name)
  {
    if (azioni != null && name != null)
    {
      for (AzioneDTO azione : azioni)
      {
        if (name.equalsIgnoreCase(azione.getCodiceAzione()))
        {
          return azione;
        }
      }
    }
    return null;
  }
}
