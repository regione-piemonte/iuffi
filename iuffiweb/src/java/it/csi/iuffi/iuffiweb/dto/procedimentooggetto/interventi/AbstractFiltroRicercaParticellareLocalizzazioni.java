package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AbstractFiltroRicercaParticellareLocalizzazioni
    implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 7583741606020604692L;
  protected long            idProcedimentoOggetto;
  protected String          istatComune;
  protected String          sezione;
  protected long            foglio;
  protected Long            particella;
  protected String          subalterno;

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getSezione()
  {
    return sezione;
  }

  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }

  public long getFoglio()
  {
    return foglio;
  }

  public void setFoglio(long foglio)
  {
    this.foglio = foglio;
  }

  public Long getParticella()
  {
    return particella;
  }

  public void setParticella(Long particella)
  {
    this.particella = particella;
  }

  public String getSubalterno()
  {
    return subalterno;
  }

  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }

  public long getIdProcedimentoOggetto()
  {
    return idProcedimentoOggetto;
  }

  public void setIdProcedimentoOggetto(long idProcedimentoOggetto)
  {
    this.idProcedimentoOggetto = idProcedimentoOggetto;
  }
}