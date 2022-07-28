package it.csi.iuffi.iuffiweb.integration.cache;

import java.lang.reflect.Method;

public class Column
{
  private String   name;
  private boolean  whereClause;
  private String   defaultUpdateValue;
  private String   defaultInsertValue;
  private Class<?> type;
  private Method   getter;

  public Column(String name, boolean whereClause, String defaultInsertValue,
      String defaultUpdateValue, Class<?> type, Method getter)
  {
    this.name = name;
    this.whereClause = whereClause;
    this.defaultUpdateValue = defaultUpdateValue;
    this.defaultInsertValue = defaultInsertValue;
    this.type = type;
    this.getter = getter;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean isWhereClause()
  {
    return whereClause;
  }

  public void setWhereClause(boolean whereClause)
  {
    this.whereClause = whereClause;
  }

  public String getDefaultUpdateValue()
  {
    return defaultUpdateValue;
  }

  public void setDefaultUpdateValue(String defaultUpdateValue)
  {
    this.defaultUpdateValue = defaultUpdateValue;
  }

  public String getDefaultInsertValue()
  {
    return defaultInsertValue;
  }

  public void setDefaultInsertValue(String defaultInsertValue)
  {
    this.defaultInsertValue = defaultInsertValue;
  }

  public Class<?> getType()
  {
    return type;
  }

  public void setType(Class<?> type)
  {
    this.type = type;
  }

  public Method getGetter()
  {
    return getter;
  }

  public void setGetter(Method getter)
  {
    this.getter = getter;
  }

}
