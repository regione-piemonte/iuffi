package it.csi.iuffi.iuffiweb.dto;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;

public class MenuItemDTO implements ILoggable
{
  /**
   * 
   */
  private static final long serialVersionUID = -1124651508195258589L;
  /** serialVersionUID */

  private int idMenuItem;
  private String titleMenuItem;
  private int idParent;
  private String useCase;
  private String path;
  private int seqMenuItem;
  private String show;
  
  //aggiuntivo per l'autorizzazione
  private long idLivello;
  

  public MenuItemDTO(int idMenuItem, String titleMenuItem, int idParent,
      String useCase, String path, int seqMenuItem, String show)
  {
    super();
    this.idMenuItem = idMenuItem;
    this.titleMenuItem = titleMenuItem;
    this.idParent = idParent;
    this.useCase = useCase;
    this.path = path;
    this.seqMenuItem = seqMenuItem;
    this.show = show;
  }
  
  public int getIdMenuItem()
  {
    return idMenuItem;
  }
  public void setIdMenuItem(int idMenuItem)
  {
    this.idMenuItem = idMenuItem;
  }
  public String getTitleMenuItem()
  {
    return titleMenuItem;
  }
  public void setTitleMenuItem(String titleMenuItem)
  {
    this.titleMenuItem = titleMenuItem;
  }
  public int getIdParent()
  {
    return idParent;
  }
  public void setIdParent(int idParent)
  {
    this.idParent = idParent;
  }
  public String getUseCase()
  {
    return useCase;
  }
  public void setUseCase(String useCase)
  {
    this.useCase = useCase;
  }
  public String getPath()
  {
    return path;
  }
  public void setPath(String path)
  {
    this.path = path;
  }
  public int getSeqMenuItem()
  {
    return seqMenuItem;
  }
  public void setSeqMenuItem(int seqMenuItem)
  {
    this.seqMenuItem = seqMenuItem;
  }
  public String getShow()
  {
    return show;
  }
  public void setShow(String show)
  {
    this.show = show;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

}
