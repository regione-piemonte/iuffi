package it.csi.iuffi.iuffiweb.dto;

import java.util.HashMap;

public class MapColonneNascosteVO
    extends HashMap<String, HashMap<String, Boolean>>
{

  private static final long serialVersionUID = -8411030316408596793L;

  public boolean visible(String key, String field)
  {
    if (this.containsKey(key))
    {
      if (this.get(key).containsKey(field))
      {
        return this.get(key).get(field).booleanValue();
      }
    }

    return false;
  }

  public boolean hidden(String key, String field)
  {
    if (this.containsKey(key))
    {
      if (this.get(key).containsKey(field))
      {
        return !this.get(key).get(field).booleanValue();
      }
    }

    return false;
  }

  public boolean hide(String key, String field, boolean defaultValue)
  {
    if (this.containsKey(key))
    {
      if (this.get(key).containsKey(field))
      {
        return this.get(key).get(field).booleanValue();
      }
    }

    return defaultValue;
  }

  public void removeTable(String tableID)
  {
    this.remove(tableID);
  }

}
