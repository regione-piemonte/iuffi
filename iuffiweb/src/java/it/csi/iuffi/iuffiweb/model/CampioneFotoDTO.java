package it.csi.iuffi.iuffiweb.model;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class CampioneFotoDTO extends TabelleDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -3417367044402459761L;

  private long idCampionamento;
  private long idFoto;
  
  public long getIdCampionamento()
  {
    return idCampionamento;
  }
  public void setIdCampionamento(long idCampionamento)
  {
    this.idCampionamento = idCampionamento;
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
