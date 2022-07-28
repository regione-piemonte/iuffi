package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadrodinamico;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class VoceElementoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 8262069086742171988L;
  protected String          codice;
  protected String          valore;
  protected long            idElementoQuadro;

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getValore()
  {
    return valore;
  }

  public void setValore(String valore)
  {
    this.valore = valore;
  }

  public long getIdElementoQuadro()
  {
    return idElementoQuadro;
  }

  public void setIdElementoQuadro(long idElementoQuadro)
  {
    this.idElementoQuadro = idElementoQuadro;
  }
}
