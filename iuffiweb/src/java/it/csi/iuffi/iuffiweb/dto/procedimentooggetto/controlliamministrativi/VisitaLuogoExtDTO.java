package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi;

public class VisitaLuogoExtDTO extends VisitaLuogoDTO
{
  /** serialVersionUID */
  private static final long serialVersionUID = -1245111590030635208L;
  private String            descTecnico;
  private String            codiceEsito;
  private String            descEsito;

  public String getDescTecnico()
  {
    return descTecnico;
  }

  public void setDescTecnico(String descTecnico)
  {
    this.descTecnico = descTecnico;
  }

  public String getCodiceEsito()
  {
    return codiceEsito;
  }

  public void setCodiceEsito(String codiceEsito)
  {
    this.codiceEsito = codiceEsito;
  }

  public String getDescEsito()
  {
    return descEsito;
  }

  public void setDescEsito(String descEsito)
  {
    this.descEsito = descEsito;
  }
}
