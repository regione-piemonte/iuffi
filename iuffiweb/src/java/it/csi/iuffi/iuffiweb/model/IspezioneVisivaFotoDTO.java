package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class IspezioneVisivaFotoDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 4361095397004175776L;
  
  
  private long idIspezioneVisiva;
  private long idFoto;
  
  public long getIdIspezioneVisiva()
  {
    return idIspezioneVisiva;
  }
  public void setIdIspezioneVisiva(long idIspezioneVisiva)
  {
    this.idIspezioneVisiva = idIspezioneVisiva;
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
