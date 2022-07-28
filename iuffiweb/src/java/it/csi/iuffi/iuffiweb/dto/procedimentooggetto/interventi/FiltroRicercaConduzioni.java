package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class FiltroRicercaConduzioni
    extends AbstractFiltroRicercaParticellareLocalizzazioni implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4495306475917813974L;
  protected String[]        chiaviConduzioniInserite;

  public String[] getChiaviConduzioniInserite()
  {
    return chiaviConduzioniInserite;
  }

  public void setChiaviConduzioniInserite(String[] chiaviConduzioniInserite)
  {
    this.chiaviConduzioniInserite = chiaviConduzioniInserite;
  }
}