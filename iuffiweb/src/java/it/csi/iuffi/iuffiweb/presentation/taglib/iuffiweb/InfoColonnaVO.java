package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

public class InfoColonnaVO
{
  private String  title;
  private String  name;
  private String  propertyName;
  private boolean sortable = false;

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public boolean isSortable()
  {
    return sortable;
  }

  public void setSortable(boolean sortable)
  {
    this.sortable = sortable;
  }

}
