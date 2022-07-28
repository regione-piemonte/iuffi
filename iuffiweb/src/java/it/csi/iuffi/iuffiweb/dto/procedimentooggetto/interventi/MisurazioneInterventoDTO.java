package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

public class MisurazioneInterventoDTO extends InfoMisurazioneIntervento
{
  /** serialVersionUID */
  private static final long serialVersionUID = 3821716346832411514L;
  private long              idMisurazioneIntervento;
  private Long              idUnitaMisura;
  private String            descUnitaMisura;

  public long getIdMisurazioneIntervento()
  {
    return idMisurazioneIntervento;
  }

  public void setIdMisurazioneIntervento(long idMisurazioneIntervento)
  {
    this.idMisurazioneIntervento = idMisurazioneIntervento;
  }

  public Long getIdUnitaMisura()
  {
    return idUnitaMisura;
  }

  public void setIdUnitaMisura(Long idUnitaMisura)
  {
    this.idUnitaMisura = idUnitaMisura;
  }

  public String getDescUnitaMisura()
  {
    return descUnitaMisura;
  }

  public void setDescUnitaMisura(String descUnitaMisura)
  {
    this.descUnitaMisura = descUnitaMisura;
  }
}
