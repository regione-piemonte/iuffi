package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class RigaJSONAllegatiInterventoDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -7488866811003755338L;
  protected long            idFileAllegatiIntervento;
  protected String          nomeLogico;
  protected String          nomeFisico;
  protected String          iconClassMimeType;

  public long getIdFileAllegatiIntervento()
  {
    return idFileAllegatiIntervento;
  }

  public void setIdFileAllegatiIntervento(long idFileAllegatiIntervento)
  {
    this.idFileAllegatiIntervento = idFileAllegatiIntervento;
  }

  public String getNomeLogico()
  {
    return nomeLogico;
  }

  public void setNomeLogico(String nomeLogico)
  {
    this.nomeLogico = nomeLogico;
  }

  public String getNomeFisico()
  {
    return nomeFisico;
  }

  public void setNomeFisico(String nomeFisico)
  {
    this.nomeFisico = nomeFisico;
  }

  public String getIconClassMimeType()
  {
    return iconClassMimeType;
  }

  public void setIconClassMimeType(String iconClassMimeType)
  {
    this.iconClassMimeType = iconClassMimeType;
  }
}
