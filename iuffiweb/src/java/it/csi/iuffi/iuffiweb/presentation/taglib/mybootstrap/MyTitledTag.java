package it.csi.iuffi.iuffiweb.presentation.taglib.mybootstrap;

/**
 * 
 * @author Stefano Einaudi (70399)
 *
 *         Classe che implementa un tag generico con Titolo (Utilizzata come
 *         base per permettere al tag Title di essere utilizzato in pi� di un
 *         tag, basta che quest'ultimo derivi da questa classe
 */
public abstract class MyTitledTag extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = -4119385161046230600L;
  protected String          title;

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }
}
