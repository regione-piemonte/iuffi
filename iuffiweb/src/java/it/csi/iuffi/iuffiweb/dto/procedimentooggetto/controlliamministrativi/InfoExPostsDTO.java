package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi;

import java.util.List;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class InfoExPostsDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long    serialVersionUID = -4065008766920158339L;

  private long                 idInfoExPosts;
  private Long                 idRischioElevato;
  private String               descrRischioElevato;
  private String               note;
  private List<AnnoExPostsDTO> anniExPosts;

  public String getAnniHtml()
  {
    if (anniExPosts == null)
      return "";

    String html = "";
    for (AnnoExPostsDTO anno : anniExPosts)
    {
      html += "+" + anno.getValore() + " <br/>";
    }
    return html;
  }

  public long getIdInfoExPosts()
  {
    return idInfoExPosts;
  }

  public void setIdInfoExPosts(long idInfoExPosts)
  {
    this.idInfoExPosts = idInfoExPosts;
  }

  public Long getIdRischioElevato()
  {
    return idRischioElevato;
  }

  public void setIdRischioElevato(Long idRischioElevato)
  {
    this.idRischioElevato = idRischioElevato;
  }

  public String getDescrRischioElevato()
  {
    return descrRischioElevato;
  }

  public void setDescrRischioElevato(String descrRischioElevato)
  {
    this.descrRischioElevato = descrRischioElevato;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public List<AnnoExPostsDTO> getAnniExPosts()
  {
    return anniExPosts;
  }

  public void setAnniExPosts(List<AnnoExPostsDTO> anniExPosts)
  {
    this.anniExPosts = anniExPosts;
  }

}