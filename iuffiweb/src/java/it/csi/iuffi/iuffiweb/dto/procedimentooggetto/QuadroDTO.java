package it.csi.iuffi.iuffiweb.dto.procedimentooggetto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class QuadroDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5916988902454815748L;
  private long              idQuadro;
  private String            codQuadro;
  private String            descQuadro;

  public long getIdQuadro()
  {
    return idQuadro;
  }

  public void setIdQuadro(long idQuadro)
  {
    this.idQuadro = idQuadro;
  }

  public String getDescQuadro()
  {
    return descQuadro;
  }

  public void setDescQuadro(String descQuadro)
  {
    this.descQuadro = descQuadro;
  }

  public String getCodQuadro()
  {
    return codQuadro;
  }

  public void setCodQuadro(String codQuadro)
  {
    this.codQuadro = codQuadro;
  }
}
