package it.csi.iuffi.iuffiweb.dto;

public class Link
{
  protected String  link;
  protected String  useCase;
  protected boolean readWrite;
  protected boolean forceVisualization;
  protected String  title;
  protected String  description;
  private String    onclick;

  public Link(String link,
      String useCase,
      boolean readWrite,
      String title,
      String description)
  {
    this.link = link;
    this.useCase = useCase;
    this.readWrite = readWrite;
    this.title = title;
    this.description = description;
  }

  public Link(String link,
      String useCase,
      String title,
      String description)
  {
    this.link = link;
    this.useCase = useCase;
    this.forceVisualization = true;
    this.title = title;
    this.description = description;
  }

  public Link(String link,
      String useCase,
      boolean readWrite,
      String title,
      String description, String onclick)
  {
    this.link = link;
    this.useCase = useCase;
    this.readWrite = readWrite;
    this.title = title;
    this.description = description;
    this.onclick = onclick;
  }

  public String getLink()
  {
    return link;
  }

  public void setLink(String link)
  {
    this.link = link;
  }

  public String getUseCase()
  {
    return useCase;
  }

  public void setUseCase(String useCase)
  {
    this.useCase = useCase;
  }

  public boolean isReadWrite()
  {
    return readWrite;
  }

  public void setReadWrite(boolean readWrite)
  {
    this.readWrite = readWrite;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getOnclick()
  {
    return onclick;
  }

  public void setOnclick(String onclick)
  {
    this.onclick = onclick;
  }

  public boolean isForceVisualization()
  {
    return forceVisualization;
  }

  public void setForceVisualization(boolean forceVisualization)
  {
    this.forceVisualization = forceVisualization;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((description == null) ? 0 : description.hashCode());
    result = prime * result + (forceVisualization ? 1231 : 1237);
    result = prime * result + ((link == null) ? 0 : link.hashCode());
    result = prime * result + ((onclick == null) ? 0 : onclick.hashCode());
    result = prime * result + (readWrite ? 1231 : 1237);
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    result = prime * result + ((useCase == null) ? 0 : useCase.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Link other = (Link) obj;
    if (description == null)
    {
      if (other.description != null)
        return false;
    }
    else
      if (!description.equals(other.description))
        return false;
    if (forceVisualization != other.forceVisualization)
      return false;
    if (link == null)
    {
      if (other.link != null)
        return false;
    }
    else
      if (!link.equals(other.link))
        return false;
    if (onclick == null)
    {
      if (other.onclick != null)
        return false;
    }
    else
      if (!onclick.equals(other.onclick))
        return false;
    if (readWrite != other.readWrite)
      return false;
    if (title == null)
    {
      if (other.title != null)
        return false;
    }
    else
      if (!title.equals(other.title))
        return false;
    if (useCase == null)
    {
      if (other.useCase != null)
        return false;
    }
    else
      if (!useCase.equals(other.useCase))
        return false;
    return true;
  }

}
