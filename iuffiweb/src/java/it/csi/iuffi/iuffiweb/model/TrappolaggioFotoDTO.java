package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class TrappolaggioFotoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1548368469516643879L;
  
  private long idTrappolaggio;
  private long idFoto;

  public long getIdTrappolaggio()
  {
    return idTrappolaggio;
  }
  public void setIdTrappolaggio(long idTrappolaggio)
  {
    this.idTrappolaggio = idTrappolaggio;
  }
  public long getIdFoto()
  {
    return idFoto;
  }
  public void setIdFoto(long idFoto)
  {
    this.idFoto = idFoto;
  }
  
}
