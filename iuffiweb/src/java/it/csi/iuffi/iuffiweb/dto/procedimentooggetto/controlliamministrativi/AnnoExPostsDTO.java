package it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlliamministrativi;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class AnnoExPostsDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -4065008766920158339L;

  private long              idAnnoExPosts;
  private String            valore;
  private boolean           checked;

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }

  public long getIdAnnoExPosts()
  {
    return idAnnoExPosts;
  }

  public void setIdAnnoExPosts(long idAnnoExPosts)
  {
    this.idAnnoExPosts = idAnnoExPosts;
  }

  public String getValore()
  {
    return valore;
  }

  public String getValoreHtml()
  {
    return "+" + valore;
  }

  public void setValore(String valore)
  {
    this.valore = valore;
  }
}