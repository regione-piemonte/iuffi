package it.csi.iuffi.iuffiweb.integration.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheManager
{
  protected static final Map<String, TableMappingForUpdateByAnnotation> TABLES_FOR_UPDATE = new HashMap<String, TableMappingForUpdateByAnnotation>();

  public static TableMappingForUpdateByAnnotation getTableDefinitionForUpdate(
      Class<?> objClass)
  {
    return TABLES_FOR_UPDATE.get(objClass.getName());
  }

  public static TableMappingForUpdateByAnnotation getTableDefinitionForUpdate(
      String name)
  {
    return TABLES_FOR_UPDATE.get(name);
  }

  public static TableMappingForUpdateByAnnotation addTableDefinitionForUpdate(
      String className, TableMappingForUpdateByAnnotation mapping)
  {
    synchronized (TABLES_FOR_UPDATE)
    {
      if (TABLES_FOR_UPDATE.get(className) == null)
      {
        TABLES_FOR_UPDATE.put(className, mapping);
      }
      else
      {
        // Mapping già esistente
      }
    }
    return mapping;
  }
}
