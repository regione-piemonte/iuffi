package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class DettaglioInterventoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long             serialVersionUID = 425456870780042684L;
  protected RigaElencoInterventi        intervento;
  protected List<DecodificaDTO<String>> comuni;

  public RigaElencoInterventi getIntervento()
  {
    return intervento;
  }

  public void setIntervento(RigaElencoInterventi intervento)
  {
    this.intervento = intervento;
  }

  public List<DecodificaDTO<String>> getComuni()
  {
    return comuni;
  }

  public void setComuni(List<DecodificaDTO<String>> comuni)
  {
    this.comuni = comuni;
  }
}
