package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

/**
 * @author Stefano Einaudi (70399)
 *
 */
public class FiltroRicercaParticelle
    extends AbstractFiltroRicercaParticellareLocalizzazioni implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -4431711845926257218L;
  protected List<Long>      idParticellaCertificata;

  public List<Long> getIdParticellaCertificata()
  {
    return idParticellaCertificata;
  }

  public void setIdParticellaCertificata(List<Long> idParticellaCertificata)
  {
    this.idParticellaCertificata = idParticellaCertificata;
  }
}